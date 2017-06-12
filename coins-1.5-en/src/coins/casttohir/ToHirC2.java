/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.casttohir;

import java.util.ListIterator;

import coins.ir.IrList;
import coins.ir.hir.ConstNode; //##77
import coins.ir.hir.Exp;
import coins.ir.hir.ExpListExp;
import coins.ir.hir.ExpStmt;
import coins.ir.hir.FunctionExp;
import coins.ir.hir.HIR;
import coins.ir.hir.IfStmt;
import coins.ir.hir.LoopStmt;
import coins.ir.hir.ReturnStmt;
import coins.ir.hir.SetDataStmt;
import coins.ir.hir.SubpNode;
import coins.ir.hir.SwitchStmt;
import coins.ir.hir.VarNode;
import coins.sym.Elem;
import coins.sym.PointerType;
import coins.sym.StructType;
import coins.sym.SubpType;
import coins.sym.Sym;
import coins.sym.Type;
import coins.sym.UnionType;
import coins.sym.Var;
import coins.sym.VectorType;

/**
 * Do error check, replace symbols, and convert expressions to HIR-C
 * expressions that can be easily treated.
 *
 * Do not do cast for + - * / % &lt;&lt; &gt;&gt; & | ^ because such cast
 * should be done after the expansion of assignment operator += -= etc.
 * Cast transformations for function parameter, initializer, and assignment
 * statements are already done.
 *
 * In more detail, do followings.
 * <pre>
 * Set the type of node.
 * Issue warning messages and error messages.
 * Replace symbols that can be combined.
 * Do following transformations:
 *   array[index] --&gt; *(decay(array)+index)
 *   !v --&gt; v==0
 *   If conditional expression in if-statement, etc. is a scalar
 *     expression, change it to a comparison expression.
 *   If comparison expression E or short circuit conditional expression E
 *     are included in a expression, then change E to E?1:0.
 *   Change expression statements as follows:
 *     a<b;  --&gt; a,b;
 *     a&&b; --&gt; a?b:0;
 *     a||b; --&gt; a?0:b;
 *
 *   int a[10], r, x, y, z;
 *
 *   a[i];              // *(a+i);
 *   if(!x ) r =!x?y:z; // if( x==0 ) r = x==0?y:z;
 *   if( x ) r = x?y:z; // if( x!=0 ) r = x!=0?y:z;
 *
 *   r = !x;   // r = x==0?1:0;
 *   r = x<y;  // r = x<y?1:0;
 *   r = x&&y; // r = x!=0 && y!=0 ? 1 : 0;
 *   !x;       // x;
 *   x<y;      // x,y;
 *   x&&y;     // x?y:0;
 *   x||y;     // x?0:y;
 * </pre>
 * @author  Shuichi Fukuda
**/
public class ToHirC2 extends ToHirVisit
{
  private final ToHirCast toCast;

  //-------------------------------------------------------------------
  public ToHirC2(ToHir tohir)
  {
    super(tohir);
    message(1,"ToHirC2\n"); //##71
    toCast = new ToHirCast(tohir);
  }
  //-------------------------------------------------------------------
  /**
   * Output debug message.
   *
   * @param  level Debug level.
   * @param  mes Debug message.
  **/
  protected void message(int level,String mes)
  {
    toHir.debug.print(level,"C2",mes);
  }
  //-------------------------------------------------------------------
  // statement converter
  //-------------------------------------------------------------------
  /**
   * At if statement node.
   *
   * @param  s IfStmt
  **/
  protected void atIf(IfStmt s)
  {
    s.setIfCondition( conditionCast(visitExp(s.getIfCondition())) );
    visitStmt(s.getThenPart());
    visitStmt(s.getElsePart());
  }
  //-------------------------------------------------------------------
  /**
   * At while statement node.
   *
   * @param  s LoopStmt
  **/
  protected void atWhile(LoopStmt s)
  {
    s.setLoopStartCondition( conditionCast(visitExp(s.getLoopStartCondition())) );
    visitStmt(s.getLoopBodyPart());
  }
  //-------------------------------------------------------------------
  /**
   * At for statement node. If conditional expression contains expantion,
   * it is put in the loop of 'for'.
   *
   * @param  s LoopStmt
  **/
  protected void atFor(LoopStmt s)
  {
    s.setLoopStartCondition( conditionCast(visitExp(s.getLoopStartCondition())) );
    visitStmt(s.getLoopInitPart());
    visitStmt(s.getLoopBodyPart());
    visitStmt(s.getLoopStepPart());
  }
  //-------------------------------------------------------------------
  /**
   * At do-while statement node. If conditional expression contains expantion,
   * it is put in the loop of 'do-while'.
   *
   * @param  s LoopStmt
  **/
  protected void atUntil(LoopStmt s)
  {
    visitStmt(s.getLoopBodyPart());
    s.setLoopEndCondition( conditionCast(visitExp(s.getLoopEndCondition())) );
  }
  //-------------------------------------------------------------------
  /**
   * At switch statement node. If conditional expression contains backword
   * expantion, it is put out in front of 'switch'.
   *
   * @param  s SwitchStmt
  **/
  protected void atSwitch(SwitchStmt s)
  {
    Exp cond = selectionCast(visitExp(s.getSelectionExp()));
    if( toHir.isIntegral(cond.getType()) )
      s.setSelectionExp(cond); //SF041025
    else
      toHir.error("bad control expression");
    visitStmt(s.getBodyStmt());
  }
  //-------------------------------------------------------------------
  /**
   * At return statement node.
   *
   * @param  s ReturnStmt
  **/
  protected void atReturn(ReturnStmt s)
  {
    Exp  retexp  = selectionCast(visitExp(s.getReturnValue()));
    Type rettype = toHir.symRoot.subpCurrent.getReturnValueType();

    if( retexp!=null ) //'return' with value.
    {
      if( rettype!=toHir.symRoot.typeVoid ) //function returning value.
      {
        retexp = toCast.assignCast(rettype,retexp);
        if( retexp==null )
          toHir.error("incompatible type in return");
      }
      else //function returning void.
      {
        if( retexp.getType().getTypeKind()!=Type.KIND_VOID )
          toHir.warning("'return' with a value, in function returning void");
        s.insertPreviousStmt( toHir.newExpStmt(retexp) );
        retexp = null;
      }
    }
    else //'return' without value.
    {
      if( rettype!=toHir.symRoot.typeVoid ) //function returning value.
      {
        toHir.warning("'return' without value, in function returning value");
        if( rettype.isScalar() )
          retexp = toCast.cast( rettype, toHir.new0Node() );
        else
          retexp = toHir.newTempVarNode(rettype);
      }
    }
    s.setReturnValue(retexp);
  }
  //-------------------------------------------------------------------
  /**
   * At expression statement node.
   *
   * @param  s ExpStmt
  **/
  protected void atExpStmt(ExpStmt s)
  {
    s.setExp( selectionCast(visitExp(s.getExp())) ); //SF041030
    // selectionCast is necessary to correspond to a&&b;
  }
  //-------------------------------------------------------------------
  /**
   * At datacode statement node.
   *
   * @param  s SetDataStmt
  **/
  protected void atSetDataStmt(SetDataStmt s)
  {
    s.setRightSide( atInitializer( s.getLeftSide().getType(), s.getRightSide() ) );
  }
  //-------------------------------------------------------------------
  // expression converter
  //-------------------------------------------------------------------
  /**
   * At variable node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atVar(VarNode e)
  {
    e.setType( e.getVar().getSymType() );
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At function node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atSubp(SubpNode e)
  {
    e.setType( e.getSubp().getSymType() );
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At subscript expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atSubs(Exp e) // []
  {
    Exp e1 = selectionCast(visitExp(e.getExp1()));
    Exp e2 = selectionCast(visitExp(e.getExp2()));

    int k1 = e1.getType().getTypeKind();
    if( k1!=Type.KIND_VECTOR && k1!=Type.KIND_POINTER )
    {
      Exp tmp=e1; e1=e2; e2=tmp;
      k1 = e1.getType().getTypeKind();
      if( k1!=Type.KIND_VECTOR && k1!=Type.KIND_POINTER )
      {
        toHir.error("[] requires array or pointer: "+ToC.tos(e));
        return e;
      }
    }
    if( !toHir.isIntegral(e2.getType()) )
    {
      toHir.error("[] requires integer type: "+ToC.tos(e));
      return e;
    }
    // array[index]   --> *(decay(array)+index)
    // pointer[index] --> *(pointer+index)
    if( k1==Type.KIND_VECTOR )
    {
      ////////SF040820[
      //if( ((VectorType)e1.getType()).getElemCount()==0 )
      //  toHir.warning("array is incomplete type or size 0: "+ToC.tos(e1));
      ////////SF040820]
      return hir.contentsExp( hir.exp(HIR.OP_ADD,toHir.decayExp(e1),e2) );
    }
    else
    {
      return hir.contentsExp( hir.exp(HIR.OP_ADD,e1.getExp1(),e2) );
    }
  }
  //-------------------------------------------------------------------
  /**
   * At function call expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atCall(FunctionExp e)
  {
    // visit function specification
    Exp e1 = e.getFunctionSpec();
    e1 = visitExp(e1);
    e.setFunctionSpec(e1);

    // get function type
    Type t1 = e1.getType();
    while( t1.getTypeKind()==Type.KIND_POINTER )
      t1 = ((PointerType)t1).getPointedType();
    if( t1.getTypeKind()!=Type.KIND_SUBP )
    {
      toHir.error("function call requires function or pointer to function");
      return e;
    }
    SubpType functype = (SubpType)t1;
    e.setType( functype.getReturnType() );
    IrList actuallist = e.getParamList();
    IrList formallist = functype.getParamTypeList();

    // check parameter count
    if( !functype.hasNoParamSpec() ) //SF041030
    {
      if( actuallist.size()<formallist.size() )
      {
        toHir.error("too few function parameters: "+ToC.tos(e1));
        return e;
      }
      if( !functype.hasOptionalParam()
      &&  formallist.size()<actuallist.size() )
      {
        //##90 toHir.error("too much function parameters: "+ToC.tos(e1));
        toHir.warning("too much function parameters: "+ToC.tos(e1)); //##90
        return e;
      }
    }
    // visit paramerets
    ListIterator actualiter = actuallist.iterator();
    ListIterator formaliter = formallist.iterator();
    while( formaliter.hasNext() ) // each formal parameter
    {
      Exp actualexp = (Exp)actualiter.next();
      Exp newexp = toCast.assignCast(
        (Type)formaliter.next(),
        selectionCast(visitExp(actualexp)) );
      if( newexp==null )
      {
        toHir.error("incompatible type for argument: "+ToC.tos(actualexp));
      }
      else if( newexp!=actualexp )
      {
        actualiter.set(newexp);
        newexp.setParent(actuallist);
      }
    }
    while( actualiter.hasNext() ) // each optional parameter
    {
      Exp actualexp = (Exp)actualiter.next();
      Exp newexp = toCast.daPromotion( selectionCast(visitExp(actualexp)) );
      if( newexp!=actualexp )
      {
        actualiter.set(newexp);
        newexp.setParent(actuallist);
      }
    }
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At add expression node.
   * <pre>
   * Process
   *   arithmetic +/- arithmetic  (normal case)
   *   pointer +/- integral
   *   (The case pointer - pointer is treated in atOffset.)
   * </pre>
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atAdd(Exp e)
  {
    Exp  e1 = selectionCast(visitExp(e.getExp1()));
    Exp  e2 = selectionCast(visitExp(e.getExp2()));
    Type t1 = e1.getType();
    Type t2 = e2.getType();
    e.setChildren(e1,e2);
    Type t = toCast.getPointerOpType(t1,t2);
    if( t!=null )
    {
      e.setType(t);
      toHir.setFlagPointerOperation(e1);
      return e;
    }
    t = toCast.getUacType(t1,t2); // Normal arithmetic case.
    if( t!=null ) // arithmetic OP arithmetic
    {
      //!! e.setChildren( toCast.cast(t,e1), toCast.cast(t,e2) );
      e.setType(t);
      return e;
    }
    toHir.error(toHir.getOp(e)+
      " requires arithmetic type or pointer and integer type: "+ToC.tos(e));
    e.setType(toHir.symRoot.typeInt);
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At sub expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atSub(Exp e)
  {
    return atAdd(e);
  }
  //-------------------------------------------------------------------
  /**
   * At mul expression node. (OP_DIV also come here)
   * Both operands should be arithmetic type.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atMul(Exp e)
  {
    Exp  e1 = selectionCast(visitExp(e.getExp1()));
    Exp  e2 = selectionCast(visitExp(e.getExp2()));
    Type t1 = e1.getType();
    Type t2 = e2.getType();
    e.setChildren(e1,e2);
    Type t = toCast.getUacType(t1,t2); // Normal arithmetic conversion.
    if( t!=null )
    {
      e.setType(t);
      //##77 BEGIN
      if ((e.getOperator() == HIR.OP_DIV)&&
          (e2 instanceof ConstNode)&&
          (t2.isInteger()&&(((ConstNode)e2).getLongValue() == 0))) {
        toHir.warning("Zero divide " + ToC.tos(e));
      }
      //##77 END
    }
    else
    {
      toHir.error(toHir.getOp(e)+" requires arithmetic type: "+ToC.tos(e));
      e.setType(toHir.symRoot.typeInt);
    }
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At div expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atDiv(Exp e)
  {
    return atMul(e);
  }
  //-------------------------------------------------------------------
  /**
   * At mod expression node.
   * Both operands should be arithmetic type.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atMod(Exp e) // % & ^ |
  {
    Exp  e1 = selectionCast(visitExp(e.getExp1()));
    Exp  e2 = selectionCast(visitExp(e.getExp2()));
    Type t1 = e1.getType();
    Type t2 = e2.getType();
    e.setChildren(e1,e2);
    Type t;
    if( toHir.isIntegral(t1)
    &&  toHir.isIntegral(t2)
    &&  (t=toCast.getUacType(t1,t2))!=null )
    {
      e.setType(t);
    //##77 BEGIN
    if ((e.getOperator() == HIR.OP_MOD)&&
        (e2 instanceof ConstNode)&&
        (t2.isInteger()&&(((ConstNode)e2).getLongValue() == 0))) {
      toHir.warning("Zero divide " + ToC.tos(e));
    }
    //##77 END

    }
    else
    {
      toHir.error(toHir.getOp(e)+" requires integer type: "+ToC.tos(e));
      e.setType(toHir.symRoot.typeInt);
    }
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At and expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atAnd(Exp e)
  {
    return atMod(e);
  }
  //-------------------------------------------------------------------
  /**
   * At or expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atOr(Exp e)
  {
    return atMod(e);
  }
  //-------------------------------------------------------------------
  /**
   * At xor expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atXor(Exp e)
  {
    return atMod(e);
  }
  //-------------------------------------------------------------------
  /**
   * At EQ expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atCmpEq(Exp e) // ==
  {
    Exp  e1 = selectionCast(visitExp(e.getExp1()));
    Exp  e2 = selectionCast(visitExp(e.getExp2()));
    e.setChildren(e1,e2);
    Type t = toCast.getCompareType( e1.getType(), e2.getType() );
    if( t!=null )
      e.setChildren( toCast.cast(t,e1), toCast.cast(t,e2) ); //!!
    else
      toHir.error(
        "comparision requires arithmetic types or pointers to compatible types");
    e.setType(toHir.symRoot.typeBool);
    //return selectionCast(e);
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At NE expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atCmpNe(Exp e) // !=
  {
    return atCmpEq(e);
  }
  //-------------------------------------------------------------------
  /**
   * At GT expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atCmpGt(Exp e) // >
  {
    return atCmpEq(e);
  }
  //-------------------------------------------------------------------
  /**
   * At GE expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atCmpGe(Exp e) // >=
  {
    return atCmpEq(e);
  }
  //-------------------------------------------------------------------
  /**
   * At LT expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atCmpLt(Exp e) // <
  {
    return atCmpEq(e);
  }
  //-------------------------------------------------------------------
  /**
   * At LE expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atCmpLe(Exp e) // <=
  {
    return atCmpEq(e);
  }
  //-------------------------------------------------------------------
  /**
   * At L-shift expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
 protected Exp atLShift(Exp e) // <<
  {
    Exp  e1 = selectionCast(visitExp(e.getExp1()));
    Exp  e2 = selectionCast(visitExp(e.getExp2()));
    e.setChildren(e1,e2);
    if( toHir.isIntegral(e1.getType())
    &&  toHir.isIntegral(e2.getType()) )
    {
      //!! e.setChildren( e1=iPromotion(e1), e1=iPromotion(e2) );
      Type t1 = toHir.iPromotedType(e1.getType()); //SF04125
      e.setType(t1);
    }
    else
    {
      toHir.error("<< >> requires integer type");
      e.setType(toHir.symRoot.typeInt);
    }
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At arithmetic R-shift expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atRShift(Exp e) // >>
  {
    return atLShift(e);
  }
  //##81 BEGIN
  //-------------------------------------------------------------------
  /**
   * At arithmetic R-shift expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atARShift(Exp e) // >>
  {
    return atLShift(e);
  }

  //##81 END
  //-------------------------------------------------------------------
  /**
   * At not expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atNot(Exp e) // ~@
  {
    Exp  e1 = selectionCast(visitExp(e.getExp1()));
    Type t1 = e1.getType();
    e.setChild1(e1);
    if( toHir.isIntegral(t1) )
    {
      e.setType( toHir.iPromotedType(t1) ); //SF04125
    }
    else
    {
      toHir.error("~ requires integer type");
      e.setType(toHir.symRoot.typeInt);
    }
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At negative expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atNeg(Exp e) // -@
  {
    Exp  e1 = selectionCast(visitExp(e.getExp1()));
    Type t1 = e1.getType();
    e.setChild1(e1);
    if( toHir.isArithmetic(t1) )
    {
      e.setType( toHir.iPromotedType(t1) ); //SF04125
    }
    else
    {
      toHir.error("unary - requires arithmetic type");
      e.setType(toHir.symRoot.typeInt);
    }
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At address expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atAddr(Exp e) // &@
  {
    Exp  e1 = selectionCast(visitExp(e.getExp1()));
    e.setChild1(e1);
    isAddressAcquirableLvalue(e1); //SF050215
    e.setType( sym.pointerType(e1.getType()) );
    return e;
  }
  //-------------------------------------------------------------------
  // chech the object where the object indicated by e is included
  // is not specified for register.
  private void isAddressAcquirableLvalue(Exp e) //SF050215
  {
    switch( e.getOperator() )
    {
    case HIR.OP_CONST:
      if( e.getConstSym().getSymKind()!=Sym.KIND_STRING_CONST )
        toHir.error("invalid lvalue in unary '&'");
      return;
    case HIR.OP_VAR:
      if( e.getVar().getStorageClass()==Var.VAR_REGISTER )
        toHir.error("cannot take address of register: "+e.getVar().getName());
      return;
    case HIR.OP_QUAL:
      Elem elem = e.getExp2().getElem();
      if( elem.isBitField() )
        toHir.error("cannot take address of bitfield: "+elem.getName());
    case HIR.OP_SUBS:
      isAddressAcquirableLvalue(e.getExp1());
      return;
    case HIR.OP_ARROW:
      elem = e.getExp2().getElem();
      if( elem.isBitField() )
        toHir.error("cannot take address of bitfield: "+elem.getName());
    case HIR.OP_SUBP:
    case HIR.OP_CONTENTS:
      return;
    }
    toHir.error("invalid lvalue in unary '&'");
  }
  //-------------------------------------------------------------------
  /**
   * At cast expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atConv(Exp e) // cast
  {
    Exp  e1 = selectionCast(visitExp(e.getExp1()));
    e.setChild1(e1);
    if( e.getType()!=toHir.symRoot.typeVoid )
      if( toHir.isScalar(e1.getType()) )
        e = toCast.cast(e.getType(),e1);
      else
        toHir.error("cast requires scalar type: "+ToC.tos(e));
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At indirection expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atContents(Exp e) // *@
  {
    Exp  e1 = selectionCast(visitExp(e.getExp1()));
    Type t1 = e1.getType();
    e.setChild1(e1);
    if( t1.getTypeKind()==Type.KIND_POINTER )
      e.setType( ((PointerType)t1).getPointedType() );
    else
      toHir.error("unary * requires pointer type: "+ToC.tos(e));
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At assign expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atAssign(Exp e)
  {
    if (toHir.fDbgLevel > 4) //##85
      message(6,"atAssign "+e.toStringWithChildren()); //##85
    Exp  e1 = visitExp(e.getExp1());
    Type t1 = e1.getType();
    Exp  e2 = selectionCast( visitExp(e.getExp2()) );
    //SF050215[
    //Exp  e2tmp = inInitBlock()
    //           ? toCast.assignCastAsInit( t1, e2 )
    //           : toCast.assignCast( t1, e2 );
    if( !inInitBlock() ) {
      //##85 isModifiableLvalue(e1);
      isModifiableLvalue(e1, e2, e); //##85
    }
    Exp e2tmp = toCast.assignCast(t1,e2);
    //SF050215]
    if( e2tmp!=null )
      ;
    else
      toHir.error("incompatible types in assignment: "+ToC.tos(e));
    e.setChildren(e1,e2);
    e.setType(t1);
    return e;
  }
  //-------------------------------------------------------------------
  // check whether the expression is modifiable lvalue.
  //##85 @param  e  expression
  // @param  pLhs  left  hand side expression //##85
  // @param  pRhs  right hand side expression //##85
  //##85 private void isModifiableLvalue(Exp e) //SF050215
  private void isModifiableLvalue(Exp pLhs, Exp pRhs, Exp pAssign)
  {
    if( pLhs.getType().getTypeKind()==Type.KIND_VECTOR ) {
      if (! toHir.fAssignsForInitiation.contains(pAssign)) {
        toHir.error("lvalue has array type: " + ToC.tos(pLhs));
        if (fDbgLevel > 0)
          message(4, " fAssignForInitiation " + toHir.fAssignsForInitiation
                  + " pAssign " + pAssign); //##87
      }
    }else if( pLhs.getType().getSizeExp()==null ) {
      toHir.error("lvalue has incomplete type: "+ToC.tos(pLhs));
    }else if( pLhs.getType().isConst() )
    { //##85 BEGIN
      if (toHir.fAssignsForInitiation.contains(pAssign)) {
        if (! pRhs.getFlag(HIR.FLAG_CONST_EXP))
          toHir.warning("assignment of non-constant to read-only location: "
                      +ToC.tos(pLhs));
      }else {
      //##85 END
        toHir.error("assignment to read-only location: "+ToC.tos(pLhs));
      } //##85
    } //##85
  }
  //-------------------------------------------------------------------
  /**
   * At offset(difference of address) expression node.
   * Both operands should be pointer and their pointed types
   * should be compatible with each other disregarding type qualifier.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atOffset(Exp e)
  {
    Exp  e1 = selectionCast(visitExp(e.getExp1()));
    Exp  e2 = selectionCast(visitExp(e.getExp2()));
    Type t1 = e1.getType();
    Type t2 = e2.getType();
    e.setChildren(e1,e2);

    if( t1.getTypeKind()==Type.KIND_POINTER //(1) //SF020917
    &&  t2.getTypeKind()==Type.KIND_POINTER ) // Both operands are pointer.
    {
      Type ptd1 = ((PointerType)t1).getPointedType();
      Type ptd2 = ((PointerType)t2).getPointedType();
      if( ptd1.getSizeValue()>0 )
        if( ptd2.getSizeValue()>0 ) // Both operand types are complete type.
          if( toHir.isCompatible(ptd1,ptd2,false) ) // Pointed types should be
                             // compatible if type qualifiers are disregarded.
            ;
          else
            toHir.error("- requires compatible pointer: "+ToC.tos(e));
        else
          toHir.error("pointer is an incomplete type or size is 0: "+ToC.tos(e2));
      else
        toHir.error("pointer is an incomplete type or size is 0: "+ToC.tos(e1));
    }
    else
      toHir.fatal("atOffset: "+ToC.tos(e));
    //##81 e.setType( toHir.symRoot.typeInt );
    //##81 BEGIN
    Type lResultType = toHir.symRoot.typeInt;
    if (toHir.symRoot.typeOffset.getSizeValue() > lResultType.getSizeValue())
      lResultType = toHir.symRoot.typeLong;
    if (toHir.symRoot.typeOffset.getSizeValue() > lResultType.getSizeValue())
      lResultType = toHir.symRoot.typeLongLong;
    e.setType(lResultType);
    //##81 END
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At logical-and expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atLgAnd(Exp e) // &&
  {
    Exp  e1 = conditionCast(visitExp(e.getExp1()));
    Exp  e2 = conditionCast(visitExp(e.getExp2()));
    e.setChildren(e1,e2);
    e.setType(toHir.symRoot.typeBool);
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At logical-or expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atLgOr(Exp e) // ||
  {
    return atLgAnd(e);
  }
  //-------------------------------------------------------------------
  /**
   * At selection expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atSelect(Exp e)
  {
    Exp  e1 = conditionCast(visitExp(e.getExp1()));
    Exp  e2 = selectionCast(visitExp(e.getExp2()));
    Exp  e3 = selectionCast(visitExp((Exp)e.getChild(3)));
    e.setChildren(e1,e2,e3);
    Type t = toCast.getSelectType( e2.getType(), e3.getType() );
    if( t!=null )
    {
      e.setType(t);
    }
    else
    {
      toHir.error("?: requires compatible type: "+ToC.tos(e));
      e.setType(toHir.symRoot.typeInt);
    }
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At comma expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atComma(Exp e)
  {
    Exp  e1 = selectionCast(visitExp(e.getExp1()));
    Exp  e2 = selectionCast(visitExp(e.getExp2()));
    e.setChildren(e1,e2);
    e.setType( e2.getType() );
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At logical-not expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atEqZero(Exp e) // !
  {
    Exp  e1 = visitExp(e.getExp1());
    Type t1 = e1.getType();
    e.setChild1( conditionCast(e1) ); //SF041220
    if( toHir.isScalar(t1) )
    {
      //SF041220[
      //if( t1.getTypeKind()==Type.KIND_BOOL )
      //  e.setChild1(e1);
      //else
      //  e = hir.exp( HIR.OP_CMP_EQ, e1, toHir.new0Node() );
      //SF041220]
    }
    else
    {
      toHir.error("! requires scalar type: "+ToC.tos(e));
    }

    e.setType(toHir.symRoot.typeBool);
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At pre-operator expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atPre(int op,Exp e)
  {
    Exp  e1 = selectionCast(visitExp(e.getExp1()));
    Type t1 = e1.getType();
    e.setChild1(e1);
    e.setType(t1);
    if( toHir.isScalar(t1) )
      ;
    else
      toHir.error(toHir.getOp(e)+" requires scalar type: "+ToC.tos(e));
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At post-operator expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atPost(int op,Exp e)
  {
    return atPre(op,e);
  }
  //-------------------------------------------------------------------
  /**
   * At add-assign expression node.
   * <pre>
   * Process
   *   arithmetic += arithmetic
   *   pointer += integral
   * </pre>
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atAddAssign(Exp e)
  {
    Exp  e1 = visitExp(e.getExp1());
    Exp  e2 = selectionCast(visitExp(e.getExp2()));
    Type t1 = e1.getType();
    Type t2 = e2.getType();
    e.setChildren(e1,e2);
    e.setType(t1);
    //##85 isModifiableLvalue(e1); //SF050215
    isModifiableLvalue(e1, e2, e); //##85
    if( toCast.getPointerOpType(t1,t2)!=null ) // 2)
    {
      toHir.setFlagPointerOperation(e1);
      return e;
    }
    if( toCast.getUacType(t1,t2)!=null ) // 1)
    {
      return e;
    }
    toHir.error(toHir.getOp(e)+
      " requires arithmetic type or pointer and integer type: "+ToC.tos(e));
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At sub-assign expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atSubAssign(Exp e)
  {
    return atAddAssign(e);
  }
  //-------------------------------------------------------------------
  /**
   * At mul-assign expression node.
   * <pre>
   *   arithmetic *= arithmetic
   * </pre>
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atMulAssign(Exp e)
  {
    Exp  e1 = visitExp(e.getExp1());
    Exp  e2 = selectionCast(visitExp(e.getExp2()));
    Type t1 = e1.getType();
    Type t2 = e2.getType();
    e.setChildren(e1,e2);
    e.setType(t1);
    //##85 isModifiableLvalue(e1); //SF050215
    isModifiableLvalue(e1, e2, e); //##85
    if( toCast.getUacType(t1,t2)!=null ) // arithmetic OP arithmetic //(1)
    {
      //!! e.setChildren( toCast.cast(t,e1), toCast.cast(t,e2) );
      return e;
    }
    toHir.error(toHir.getOp(e)+" requires arithmetic type: "+ToC.tos(e));
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At div-assign expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atDivAssign(Exp e)
  {
    return atMulAssign(e);
  }
  //-------------------------------------------------------------------
  /**
   * At mod-assign expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atModAssign(Exp e)
  {
    Exp  e1 = visitExp(e.getExp1());
    Exp  e2 = selectionCast(visitExp(e.getExp2()));
    Type t1 = e1.getType();
    Type t2 = e2.getType();
    e.setChildren(e1,e2);
    e.setType(t1);
    //##85 isModifiableLvalue(e1); //SF050215
    isModifiableLvalue(e1, e2, e); //##85
    if( toHir.isIntegral(t1)
    &&  toHir.isIntegral(t2)
    &&  toCast.getUacType(t1,t2)!=null )
    {
      //!! e.setChildren( toCast.cast(t,e1), toCast.cast(t,e2) );
      return e;
    }
    toHir.error(toHir.getOp(e)+" requires integer types: "+ToC.tos(e));
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At L-shift-assign expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atLShiftAssign(Exp e)
  {
    Exp  e1 = visitExp(e.getExp1());
    Exp  e2 = selectionCast(visitExp(e.getExp2()));
    Type t1 = e1.getType();
    Type t2 = e2.getType();
    e.setChildren(e1,e2);
    e.setType(t1);
    //##85 isModifiableLvalue(e1); //SF050215
    isModifiableLvalue(e1, e2, e); //##85
    if( toHir.isIntegral(e1.getType())
    &&  toHir.isIntegral(e2.getType()) )
    {
      //!! e.setChildren( e1=iPromotion(e1), e1=iPromotion(e2) );
      return e;
    }
    toHir.error(toHir.getOp(e)+" requires integer types: "+ToC.tos(e));
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At R-shift-assign expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atRShiftAssign(Exp e)
  {
    return atLShiftAssign(e);
  }
  //-------------------------------------------------------------------
  /**
   * At and-assign expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atAndAssign(Exp e)
  {
    return atModAssign(e);
  }
  //-------------------------------------------------------------------
  /**
   * At or-assign expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atOrAssign(Exp e)
  {
    return atModAssign(e);
  }
  //-------------------------------------------------------------------
  /**
   * At xor-assign expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atXorAssign(Exp e)
  {
    return atModAssign(e);
  }
  //-------------------------------------------------------------------
  /**
   * At expression list node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atExpList(ExpListExp e)
  {
    Type t = e.getType();
    switch( t.getTypeKind() )
    {
      case Type.KIND_VECTOR: // array initializer
      {
        long size = e.size(); //((VectorType)t).getElemCount();
        Type elemtype = ((VectorType)t).getElemType();

        //SF041111 if( elemtype==toHir.symRoot.getCharType() )
        //SF041111   break; // string initializer  str[] = "str"
        //##64 for( int i=0; i<size; i++ ) // each array element
        ExpListExp lNewList = (ExpListExp)hir.expList(null); //##64
        for (ListIterator lIterator = e.iterator(); //##64
             lIterator.hasNext(); ) //##64
        {
          //SF040920 Exp ei = e.get(i);
          //##64 Exp ei = e.getExp(i); //SF040920
          Exp ei = (Exp)lIterator.next(); //##64
          if( ei.getOperator()==HIR.OP_EXPREPEAT )
          {
            //##64 ei.setChild1( atInitializer(elemtype,ei.getExp1()) );
            lNewList.add(hir.exp(HIR.OP_EXPREPEAT,
                 atInitializer(elemtype,ei.getExp1()),
                 (Exp)ei.getExp2().copyWithOperands()));
            break;
          }
          //##64 e.setExp( i, atInitializer(elemtype,ei) );
          Exp ei2 = atInitializer(elemtype,ei); //##64
          if (ei2 != null) //##64
            ei2.cutParentLink(); //##64
          lNewList.add((Exp)atInitializer(elemtype,ei));
         }
        //##64 break;
        lNewList.setType(t); //##64
        return lNewList; //##64
      }
      case Type.KIND_STRUCT: // struct initializer
      {
        IrList il = ((StructType)t).getElemList();

        //##64 int  size = e.size(); //il.size();
        //##64 for( int i=0; i<size && e.getExp(i)!=null; i++ ) // each struct element
        //##64   e.setExp(
        //##64     i,
        //##64     atInitializer( ((Elem)il.get(i)).getSymType(), e.getExp(i) ) );
        //##64 break;
        //##64 BEGIN
        ExpListExp lStructList = (ExpListExp)hir.expList(null); //##64
        ListIterator lValueIterator = e.iterator();
        for (ListIterator lElemIterator = il.iterator();
             lElemIterator.hasNext(); ) {
          Elem lElem = (Elem)lElemIterator.next();
          if (lValueIterator.hasNext()) {
            Exp lExp = atInitializer(lElem.getSymType(),
                                     (Exp)lValueIterator.next());
            if (lExp != null)
              lExp.cutParentLink();
            lStructList.add(lExp);
          }
        }
        lStructList.setType(t);
        return lStructList;
        //##64 END
      }
      case Type.KIND_UNION: // union initializer
      {
        IrList il = ((UnionType)t).getElemList();
        if( e/*il*/.size()>0 ) // has union element ?
          e.setExp(
            0,
            atInitializer( ((Elem)il.get(0)).getSymType(), e.getExp(0) ) );
        break;
      }
      default:
        toHir.fatal("atExpListExp  TYPE="+t);
    }
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At initialization expression.
   * Visit the expression e as type t.
   *
   * @param  t Type of expression to be visited.
   * @param  e Expression to be visited.
   * @return Resultant expression (expression visited over).
  **/
  private Exp atInitializer(Type t,Exp e)
  {
    if (toHir.fDbgLevel > 1) //##85
      message(4,"atInitializer ="+t+" exp "+e); //##85
    e = visitExp(e);
    if( e.getOperator()==HIR.OP_EXPLIST )
      return e;
    //Exp etmp = toCast.assignCastAsInit(t,e); //SF050215
    Exp etmp = toCast.assignCast(t,e); //SF050215
    if( etmp!=null )
      return etmp;
    toHir.error("invalid initializer of "+t+": "+ToC.tos(e));
    return e;
  }
  //-------------------------------------------------------------------
  // utility
  //-------------------------------------------------------------------
  /**
   * Convert expression e to comparison expression.
   * If the expression e is nether comparison expression nor short circuit
   * conditional expression, convert it to e != 0.<br>
   * Ex. if( a ) --&gt; if( a!=0 )
   *
   * @param   e Expression to be converted.
   * @return  Converted result.
  **/
  private Exp conditionCast(Exp e)
  {
    if( e==null )
      return toHir.newTrueNode();
    switch( e.getOperator() )
    {
    case HIR.OP_LG_AND: // Comparison or conditional expression.
    case HIR.OP_LG_OR:
    case HIR.OP_EQ_ZERO:
    case HIR.OP_CMP_EQ:
    case HIR.OP_CMP_NE:
    case HIR.OP_CMP_GT:
    case HIR.OP_CMP_GE:
    case HIR.OP_CMP_LT:
    case HIR.OP_CMP_LE:
      return e;
    default: // Convert to comparison expression.
      if( !toHir.isScalar(e.getType()) )
        toHir.error("bad conditional expression");
      return hir.exp( HIR.OP_CMP_NE, e, toHir.new0Node() );
    }
  }
  //-------------------------------------------------------------------
  /**
   * Convert expression e to select expression.
   * If the expression e is either comparison expression or short circuit
   * conditional expression, convert it to e?1:0.<br>
   * Ex. a = a==1; --&gt; a = a==1?1:0;
   *
   * @param   e Eexpression to be converted.
   * @return  Converted result.
  **/
  private Exp selectionCast(Exp e)
  {
    if( e==null )
      return null; //SF041206
    switch( e.getOperator() )
    {
    case HIR.OP_LG_AND: // Comparison or conditional expression.
    case HIR.OP_LG_OR:
    case HIR.OP_EQ_ZERO:
    case HIR.OP_CMP_EQ:
    case HIR.OP_CMP_NE:
    case HIR.OP_CMP_GT:
    case HIR.OP_CMP_GE:
    case HIR.OP_CMP_LT:
    case HIR.OP_CMP_LE:
      e = hir.exp( HIR.OP_SELECT, e, toHir.newTrueNode(), toHir.newFalseNode() );
      e.setType(toHir.symRoot.typeBool);
      return e;
    default: // Convert to comparison expression.
      return e;
    }
  }
  //-------------------------------------------------------------------
}
