/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.casttohir;

import coins.ir.IR;
import coins.ir.IrList;
import coins.ir.hir.ElemNode;
import coins.ir.hir.Exp;
import coins.ir.hir.ExpStmt;
import coins.ir.hir.FunctionExp;
import coins.ir.hir.HIR;
import coins.ir.hir.IfStmt;
import coins.ir.hir.LabeledStmt;
import coins.ir.hir.LoopStmt;
import coins.ir.hir.ReturnStmt;
import coins.ir.hir.SetDataStmt; //SF050111
import coins.ir.hir.Stmt;
import coins.ir.hir.SwitchStmt;
import coins.ir.hir.VarNode;
import coins.sym.Elem;
import coins.sym.PointerType;
import coins.sym.Type;

/**
 * Convert HIR-C to HIR-base.
 *
 * @author  Shuichi Fukuda
**/
public class ToHirBase extends ToHirVisit
{
  private final ToHirSym         toSym;
  private final ToHirCast        toCast;
  private final SideEffectBuffer buffer;
  private final Type             integralOffset;

  private ToHirBase child;

  //-------------------------------------------------------------------
  /**
   * Constructor.
   *
   * @param  tohir Offers cooperation with the object of other packages.
  **/
  public ToHirBase(ToHir tohir)
  {
    super(tohir);
    message(1,"ToHirBase\n"); //##71
    toSym  = new ToHirSym(tohir);
    toCast = new ToHirCast(tohir);
    buffer = new SideEffectBuffer(tohir);

    long offsetsize = tohir.symRoot.typeOffset.getSizeValue(); //SF041206
    integralOffset = tohir.symRoot.typeInt.getSizeValue()==offsetsize
                   ? tohir.symRoot.typeInt
                   : tohir.symRoot.typeChar.getSizeValue()==offsetsize
                   ? tohir.symRoot.typeChar
                   : tohir.symRoot.typeShort.getSizeValue()==offsetsize
                   ? tohir.symRoot.typeShort
                   : tohir.symRoot.typeLong.getSizeValue()==offsetsize
                   ? tohir.symRoot.typeLong
                   : tohir.symRoot.typeLongLong.getSizeValue()==offsetsize
                   ? tohir.symRoot.typeLongLong
                   : null;
    if( integralOffset==null )
      tohir.error("There is no integral type which matches to the offset type");
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
    toHir.debug.print( level, "B1", mes );
  }
  //-------------------------------------------------------------------
  /**
   * Create child instance, and return it.
   *
   * @return  Child ToHirBase.
  **/
  private ToHirBase sureChild()
  {
    if( child==null )
      child = new ToHirBase(toHir);
    return child;
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
    visitStmt(s.getThenPart());
    visitStmt(s.getElsePart());
    visitExpAsIfCondition(s);
    buffer.addToStmtPrev(s,false);
  }
  //-------------------------------------------------------------------
  /**
   * At while statement node.
   *
   * @param  s LoopStmt
  **/
  protected void atWhile(LoopStmt s)
  {
    visitStmt(s.getLoopBodyPart());

    Exp cond = s.getLoopStartCondition();
    if( cond!=null )
      switch( cond.getOperator() )
      {
      case HIR.OP_LG_AND:
      case HIR.OP_LG_OR:
        s.setLoopStartCondition( toHir.newTrueNode() );
        IfStmt ifstmt=toHir.newIfStmt( cond, null, hir.jumpStmt(s.getLoopEndLabel()) );
        buffer.getBlockStmt( s.getLoopBodyPart() ).addFirstStmt(ifstmt);
        atIf(ifstmt);
        break;
      default:
        cond = visitExp(cond);
        if( buffer.isEmpty() )
          s.setLoopStartCondition(cond);
        else
        {
          s.setLoopStartCondition( toHir.newTrueNode() );
          ifstmt = toHir.newIfStmt( cond, null, hir.jumpStmt(s.getLoopEndLabel()) );
          buffer.getBlockStmt( s.getLoopBodyPart() ).addFirstStmt(ifstmt);
          buffer.addToStmtPrev(ifstmt,false);
        }
        break;
      }
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
    visitStmt(s.getLoopInitPart());
    visitStmt(s.getLoopBodyPart());
    visitStmt(s.getLoopStepPart());

    Exp cond = s.getLoopStartCondition();
    if( cond!=null )
      switch( cond.getOperator() )
      {
      case HIR.OP_LG_AND:
      case HIR.OP_LG_OR:
        s.setLoopStartCondition( toHir.newTrueNode() );
        IfStmt ifstmt=toHir.newIfStmt( cond, null, hir.jumpStmt(s.getLoopEndLabel()) );
        buffer.getBlockStmt( s.getLoopBodyPart() ).addFirstStmt(ifstmt);
        atIf(ifstmt);
        break;
      default:
        cond = visitExp(cond);
        if( buffer.isEmpty() )
          s.setLoopStartCondition(cond);
        else
        {
          s.setLoopStartCondition( toHir.newTrueNode() );
          ifstmt = toHir.newIfStmt( cond, null, hir.jumpStmt(s.getLoopEndLabel()) );
          buffer.getBlockStmt( s.getLoopBodyPart() ).addFirstStmt(ifstmt);
          buffer.addToStmtPrev(ifstmt,false);
        }
        break;
      }
  }
  //-------------------------------------------------------------------
  /**
   * At do-while statement node. If conditional expression contains
   * expantion, it is put in the loop of 'do-while'.
   *
   * @param  s LoopStmt
  **/
  protected void atUntil(LoopStmt s)
  {
    visitStmt(s.getLoopBodyPart());

    Exp cond = s.getLoopEndCondition();
    if( cond!=null )
      switch( cond.getOperator() )
      {
      case HIR.OP_LG_AND:
      case HIR.OP_LG_OR:
        s.setLoopEndCondition( toHir.newTrueNode() );
        IfStmt ifstmt=toHir.newIfStmt( cond, null, hir.jumpStmt(s.getLoopEndLabel()) );
        buffer.getBlockStmt( s.getLoopBodyPart() ).addLastStmt(ifstmt);
        atIf(ifstmt);
        break;
      default:
        cond = visitExp(cond);
        if( buffer.isEmpty() )
          s.setLoopEndCondition(cond); //Start->End
        else
        {
          s.setLoopEndCondition( toHir.newTrueNode() ); //Start->End
          ifstmt = toHir.newIfStmt(
            cond, null, hir.jumpStmt(s.getLoopEndLabel()) );
          buffer.getBlockStmt( s.getLoopBodyPart() ).addLastStmt(ifstmt); //First->Last
          buffer.addToStmtPrev(ifstmt,false);
        }
        break;
      }
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
    visitStmt(s.getBodyStmt());

    s.setSelectionExp( //SF041025
      toCast.iPromotion(visitExpAsSequencePoint(s.getSelectionExp())) );
    buffer.addToStmtPrev(s,false);
  }
  //-------------------------------------------------------------------
  /**
   * At return statement node.
   *
   * @param  s: ReturnStmt
  **/
  protected void atReturn(ReturnStmt s)
  {
    if( s.getReturnValue()!=null ) //SF041206
    {
      Exp e1 = visitExpAsSequencePoint(s.getReturnValue()); //SF040615
      s.setReturnValue(e1); //SF040615
      s.setType(e1.getType()); //SF040615
    }
    buffer.addToStmtPrev(s,false);
  }
  //-------------------------------------------------------------------
  /**
   * At expression statement node.
   *
   * @param  s ExpStmt
  **/
  protected void atExpStmt(ExpStmt s)
  {
    visitExpAsExpStmt(s.getExp());
    buffer.addToStmtPrev(s,false);
    s.deleteThisStmt();
  }
  //-------------------------------------------------------------------
  /**
   * At datacode statement node.
   *
   * @param  s SetDataStmt
  **/
  protected void atSetDataStmt(SetDataStmt s) //SF050111
  {
    if (fDbgLevel >= 4)  //##77
      toHir.debug.print(4, "atSetData " + s.toString()); //##77
    s.getRightSide().fold(); //SF050111
    //##77 BEGIN
    Exp lRightSideOld = s.getRightSide();
    Exp lRightSideNew = visitExp(lRightSideOld);
    if (lRightSideNew != lRightSideOld) {
      s.setRightSide(lRightSideNew);
    }
    //##77 END
  }
  //-------------------------------------------------------------------
  // visit expression
  //-------------------------------------------------------------------
  /**
   * Visit the expression e.
   * Treat the end of evaluation as the sequence point (side effect
   * completion point) of this expression.
   * The swept out statements (side effect statements generated during the
   * evaluation of the expression e) are put in the buffer.prevList.
   *
   * @param   e Expression to be visited.
   * @return  The expression already visited.
  **/
  protected Exp visitExpAsSequencePoint(Exp e)
  {
    Exp visitedexp = visitExp(e);
    if( !buffer.isEmptyNext() )
    {
      VarNode tempvar = toHir.newTempVarNode(visitedexp.getType());
      buffer.addPrev( // Insert the expanded expression to buffer.prevList.
        toHir.newAssignStmt((Exp)tempvar.copyWithOperands(),visitedexp) );
      buffer.moveNextToPrev(); // Move contents of nextList to prevList.
      return tempvar;
    }
    else
      return visitedexp;
  }
  //-------------------------------------------------------------------
  /**
   * Do the same as visitExpAsSequencePoint but this is for an expression
   * whose value is not used (in such cases as procedure call (ExpStmt),
   * non-last expression of comma expression (x of (x, y)), etc.).
   *
   * @param   e Expression to be visited.
  **/
  protected void visitExpAsExpStmt(Exp e)
  {
    e.setParent(null);
    Exp visitedexp = visitExp(e);
    if( visitedexp!=null )
        buffer.addPrev( toHir.newExpStmt(visitedexp) );
    buffer.moveNextToPrev(); // Move contents of nextList to prevList.
  }
  //-------------------------------------------------------------------
  /**
   * Visit the conditional expression of if-statement.
   * If the conditional expression contains && or || operator,
   * delete them by changing it to nested if-statement.
   * The resultant conditional expression should be a comparison
   * expression. If not so, error message will be issued.
   * <pre>
   * if( a&&b||c&&d ) x; else y;
   * -->
   * if(a&&b)
   * T1: x;
   * else
   * F1: if(c&&d)
   *     T2: goto T1:
   *     else
   *     F2: y;
   * -->
   * if(a)
   * T1: if(b)
   *     T3: x;
   *     else
   *     F3: goto F1;
   * else
   * F1: if(c)
   *     T2: if(d)
   *         T4: goto T1:
   *         else
   *         F4: goto F2;
   *     else
   *     F2: y;
   * </pre>
   * @param  s: If-statement to be visited.
  **/
  private void visitExpAsIfCondition(IfStmt s1)
  {
    Exp         cond1 = s1.getIfCondition();
    LabeledStmt then1 = (LabeledStmt)s1.getThenPart();
    LabeledStmt else1 = (LabeledStmt)s1.getElsePart();
    sureChild();
    switch( cond1.getOperator() )
    {
    case HIR.OP_LG_AND:
      IfStmt s2 = toHir.newIfStmt(cond1.getExp2(),null,null); // Inner IfStmt.
      if( then1!=null && then1.getStmt()!=null )
      {
        s1.replaceThenPart( s2.getThenPart() );
        s2.replaceThenPart( then1 );
      }
      if( else1!=null && else1.getStmt()!=null )
        s2.getElsePart().setStmt( hir.jumpStmt(else1.getLabel()) );
      child.visitExpAsIfCondition(s2);
      child.buffer.addPrev(s2);
      s1.getThenPart().setStmt( child.buffer.toStmt() ); // Connect to outer IfStmt.
      s1.setIfCondition( cond1.getExp1() );
      visitExpAsIfCondition(s1);
      return;

    case HIR.OP_LG_OR:
      s2 = toHir.newIfStmt(cond1.getExp2(),null,null); // Inner IfStmt.
      if( then1!=null && then1.getStmt()!=null )
        s2.getThenPart().setStmt( hir.jumpStmt(then1.getLabel()) );
      if( else1!=null && else1.getStmt()!=null )
      {
        s1.replaceElsePart( s2.getElsePart() );
        s2.replaceElsePart( else1 );
      }
      child.visitExpAsIfCondition(s2);
      child.buffer.addPrev(s2);
      s1.getElsePart().setStmt( child.buffer.toStmt() ); // Connect to outer IfStmt.
      s1.setIfCondition( cond1.getExp1() );
      visitExpAsIfCondition(s1);
      return;

    case HIR.OP_CMP_EQ:
    case HIR.OP_CMP_NE:
    case HIR.OP_CMP_GT:
    case HIR.OP_CMP_GE:
    case HIR.OP_CMP_LT:
    case HIR.OP_CMP_LE:
      s1.setIfCondition( child.visitExp(cond1) );
      buffer.add(child.buffer);
      return;
    case HIR.OP_CONST: //##62
      // s1.setIfCondition( cond1 );
      return; //##62
    default:
      toHir.fatal("visitExpAsIfCondition");
      return;
    }
  }
  //-------------------------------------------------------------------
  // expression converter
  //-------------------------------------------------------------------
  /**
   * At subscript expression node.
   *
   * @param   e: Exp
   * @return  Exp
  **/
  protected Exp atSubs(Exp e) // []
  {
    super.atSubs(e);
    e.setChild2( toCast.cast(integralOffset,e.getExp2()) ); //SF041206
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At function call expression node.
   *
   * @param   e: Exp
   * @return  Exp
  **/
  protected Exp atCall(FunctionExp e)
  {
    // Visit function specification.
    e.setFunctionSpec( visitExp(e.getFunctionSpec()) );

    // Visit paramerets.
    sureChild();
    IrList actuallist = e.getParamList(); // Actual parameter list.
    java.util.ListIterator actualiter = actuallist.iterator();
    while( actualiter.hasNext() ) // For each optional parameter.
    {
      Exp actualexp = (Exp)actualiter.next();
      Exp newexp    = child.visitExpAsSequencePoint(actualexp);
      if( newexp!=actualexp )
      {
        actualiter.set(newexp);
        newexp.setParent(actuallist);
      }
    }
    // Make sure the completion of side effects at sequence point of function call.
    if( !child.buffer.isEmpty() )
    {
      buffer.add(child.buffer);
      Exp lvalue = getLvalueNode(e,e.getType());
      if( lvalue!=null )
      {
        buffer.addPrev( toHir.newAssignStmt((Exp)lvalue.copyWithOperands(),e) );
        return lvalue;
      }
    }
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At add expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atAdd(Exp e)
  {
    if (fDbgLevel >= 4)  //##77
      toHir.debug.print(4, "atAdd " + e.toString()); //##77
    super.atAdd(e);

    Exp e1 = e.getExp1();
    Exp e2 = e.getExp2();
    Type t1 = e1.getType();
    Type t2 = e2.getType();

    if( t1.getTypeKind()==Type.KIND_POINTER
    &&  toHir.isIntegral(t2) ) // pointer OP integer
    {
      if (fDbgLevel >= 4)  //##77
        toHir.debug.print(4, " pointer addition " + e1.toString() + " " + e2.toString()); //##77
      Type ptd = ((PointerType)t1).getPointedType(); // Left operand pointed type.
      e.setChild2( // pointer+index --> pointer+(pointedTypeSize*index)
        hir.exp(
          HIR.OP_MULT,
          (Exp)ptd.getSizeExp().copyWithOperands(),
          hir.convExp(integralOffset,
                      //##92 e2
                      (Exp)e2.copyWithOperands() //##92
                      ) )); //SF041206
      e.setFlag(HIR.FLAG_C_PTR,false);
    }
    else // arithmetic OP arithmetic
    {
      Type t = e.getType();
      e.setChildren( toCast.cast(t,e1), toCast.cast(t,e2) );
    }
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
   * At mul expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atMul(Exp e)
  {
    super.atMul(e);
    Type t = e.getType();
    Exp e1 = toCast.cast( t, e.getExp1() );
    Exp e2 = toCast.cast( //SF041206
      t.getTypeKind()==Type.KIND_OFFSET ? integralOffset : t,
      e.getExp2() );
    e.setChildren(e1,e2);
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
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atMod(Exp e)
  {
    return atMul(e);
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
    return atMul(e);
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
    return atMul(e);
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
    return atMul(e);
  }
  //-------------------------------------------------------------------
  /**
   * At EQ expression node.
   * <pre>
   * 1)  a==b;      =>  a; b;
   * 2)  x = a==b;  =>  if(a==b) x=1; else x=0;
   * 3)  x + a==b;  =>  if(a==b) tmp=1; else tmp=0; x+tmp;
   * </pre>
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atCmpEq(Exp e) // ==
  {
    Exp e1 = e.getExp1();
    Exp e2 = e.getExp2();
    Type t = toCast.getCompareType( e1.getType(), e2.getType() );
    e.setChild1( toCast.cast(t,visitExpAsSequencePoint(e1)) );
    e.setChild2( toCast.cast(t,visitExpAsSequencePoint(e2)) );
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
    Exp e1 = visitExp(e.getExp1());
    Exp e2 = visitExp(e.getExp2());
    e.setChildren( toCast.iPromotion(e1), toCast.iPromotion(e2) );
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At logical R-shift expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atRShift(Exp e) // >>>
  {
    Exp e1 = toCast.iPromotion( visitExp(e.getExp1()) );
    Exp e2 = toCast.iPromotion( visitExp(e.getExp2()) );
    if( !e1.getType().isUnsigned() )
      e = hir.exp(HIR.OP_SHIFT_R,e1,e2);
    else
      e.setChildren(e1,e2);
    return e;
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
    Exp e1 = toCast.iPromotion( visitExp(e.getExp1()) );
    Exp e2 = toCast.iPromotion( visitExp(e.getExp2()) );
    if( e1.getType().isUnsigned() )
      e = hir.exp(HIR.OP_SHIFT_RL,e1,e2);
    else
      e.setChildren(e1,e2);
    return e;
  }
  //##81 END
  //-------------------------------------------------------------------
  /**
   * At logical not expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atNot(Exp e) // ~@
  {
    super.atNot(e);
    e.setChild1(toCast.iPromotion(e.getExp1()));
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At negative expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atNeg(Exp e) // -@ //SF041004
  {
    super.atNeg(e);
    e.setChild1(toCast.iPromotion(e.getExp1()));
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At assign expression node.
   * <pre>
   * At first, visit the left operand and immediately store its result
   * as left operand. (The left operand may be refered in the process
   * of visiting the right operand.)
   * Second, visit the right operand and then do following operations
   * according to its returned expression:
   * 1) If same to the left operand, return the left operand;
   * 2) If null, return the left operand because an AssignStmt is already
   *    sweped out.
   * 3) If not null, sweep out AssignStmt during which do following operations
   *    according to parent type:
   *   a) If the parent is null, return null after sweeping out AssignStmt.
   *   b) If the parent is not null, return expression with less overhead
   *      by doing following transformation:
   *     i)   a = b[i]  --&gt;  a = b[i];  (return the left side a)
   *     ii)  a[i] = 1  --&gt;  a[i] = 1;  (return the right side 1)
   *     iii) a[i] = b[j] --&gt; t=b[j]; a[i]=t; (return the temporal variable t)
   * </pre>
   * @param   e Exp to be visited.
   * @return  Exp as above.
  **/
  protected Exp atAssign(Exp e)
  {
    super.atAssign(e);
    Exp e1 = e.getExp1();
    Exp e2 = e.getExp2();
    Type t1 = e1.getType();
    e.setType(t1); //SF040615

    if( e2==e1     // 1)
    ||  e2==null ) // 2)
    {
      return e.getParent()!=null? e1: null; //SF030412
    }
    // 3)
    Exp e2tmp = toCast.assignCast(t1,e2); //SF050215
    if( e2tmp==null )
    {
      toHir.error( "invalid assignment from '"+e2.getType()+"' to '"+t1+"'");
      return e;
    }
    e2 = e2tmp;
    if( e.getParent()==null ) // a)
    {
      buffer.addPrev( toHir.newAssignStmt(e1,e2) );
      return null;
    }
    // b)
    switch( e1.getOperator() ) // i)
    {
    case HIR.OP_VAR:
    case HIR.OP_PARAM:
      buffer.addPrev( toHir.newAssignStmt((Exp)e1.copyWithOperands(),e2) );
      return e1;
    }
    switch( e2.getOperator() ) // ii)
    {
    case HIR.OP_VAR:
    case HIR.OP_PARAM:
    case HIR.OP_CONST:
      buffer.addPrev( toHir.newAssignStmt(e1,(Exp)e2.copyWithOperands()) );
      return e2;
    }
    // iii)
    VarNode tempvar = toHir.newTempVarNode(t1);
    buffer.addPrev( toHir.newAssignStmt((Exp)tempvar.copyWithOperands(),e2) );
    buffer.addPrev( toHir.newAssignStmt(e1,(Exp)tempvar.copyWithOperands()) );
    return tempvar;
  }
  //-------------------------------------------------------------------
  /**
   * At offset(difference of address) expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atOffset(Exp e)
  {
    super.atOffset(e);
    Exp  e1 = e.getExp1();
    Exp  e2 = e.getExp2();
    Type t1 = e1.getType();
    Type ptd1 = ((PointerType)t1).getPointedType();

    // (div  int                      // e
    //   (sub  _offset                // offset
    //     (pointer1  PTR)            // e1
    //     (pointer2  PTR))           // e2
    //   (pointedtypesize  _offset))  // ptrsize
    Exp offset = hir.exp( HIR.OP_SUB, e1, e2 );
    offset.setType(toHir.symRoot.typeOffset);
    Exp ptrsize = (Exp)ptd1.getSizeExp().copyWithOperands();
    ptrsize.setType(toHir.symRoot.typeOffset);
    e = hir.exp( HIR.OP_DIV, offset, ptrsize );
    //##81 e.setType(toHir.symRoot.typeInt);
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
    e.getParent().print(10);
    toHir.fatal("atLgAnd");
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
    Exp e1 = e.getExp1();
    Exp e2 = e.getExp2();
    Exp e3 = (Exp)e.getChild(3);
    Stmt thenstmt, elsestmt;
    sureChild();
    Exp lvalue = getLvalueNode(e,e.getType());
    if( lvalue==null ) // 1)
    {
      child.visitExpAsExpStmt(e2);
      thenstmt = child.buffer.toStmt();
      child.visitExpAsExpStmt(e3);
      elsestmt = child.buffer.toStmt();
    }
    else // 2) 3)
    {
      child.visitExpAsExpStmt(
        hir.exp(HIR.OP_ASSIGN,(Exp)lvalue.copyWithOperands(),e2) );
      thenstmt = child.buffer.toStmt();
      child.visitExpAsExpStmt(
        hir.exp(HIR.OP_ASSIGN,(Exp)lvalue.copyWithOperands(),e3) );
      elsestmt = child.buffer.toStmt();
    }
    IfStmt ifstmt = toHir.newIfStmt(e1,thenstmt,elsestmt);
    visitExpAsIfCondition(ifstmt);
    buffer.addPrev(ifstmt);
    return lvalue;
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
    Exp e1 = e.getExp1();
    //SF041222[
    //visitExpAsExpStmt(e1);
    sureChild();
    child.visitExpAsExpStmt(e1);
    buffer.add(child.buffer);
    //SF041222]

    Exp e2 = e.getExp2();
    e2.setParent(e.getParent());
    return visitExp(e2);
  }
  //-------------------------------------------------------------------
  /**
   * At pre-operator expression node.
   * <pre>
   *    1)  ++v       =>  ( v=v+1, v )
   *    2)  ++*p      =>  ( *p=*p+1, *p )
   *    3)  ++*(p+j)  =>  ( tmp=p+j, *tmp=*tmp+1, *tmp )
   * else)  ++s.m     =>  ( tmp=&s.m, *tmp=*tmp+1, *tmp )
   * </pre>
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atPre(int op,Exp e)
  {
    //##71 Exp e1 = getLittleOverheadLvalueNode( visitExp(e.getExp1()) );
    Exp e1 = visitExp(e.getExp1()); //##71
    buffer.addPrev( newOpAssignStmt(op,e1,toHir.new1Node()) );
    return e.getParent()!=null? e1: null;
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
    //##71 Exp e1 = getLittleOverheadLvalueNode( visitExp(e.getExp1()) );
    Exp e1 = visitExp(e.getExp1()); //##71
    buffer.addNext( newOpAssignStmt(op,e1,toHir.new1Node()) );
    return e.getParent()!=null? e1: null;
  }
  //-------------------------------------------------------------------
  /**
   * at op-assign expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  private Exp atOpAssign(int op,Exp e)
  {
    Exp e1 = getLittleOverheadLvalueNode( visitExp(e.getExp1()) );
    Exp e2 = visitExp(e.getExp2());
    buffer.addPrev( newOpAssignStmt(op,e1,e2) );
    return e.getParent()!=null? e1: null;
  }
  //-------------------------------------------------------------------
  /**
   * At add-assign expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atAddAssign(Exp e)
  {
    return atOpAssign(HIR.OP_ADD,e);
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
    return atOpAssign(HIR.OP_SUB,e);
  }
  //-------------------------------------------------------------------
  /**
   * At mul-assign expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atMulAssign(Exp e)
  {
    return atOpAssign(HIR.OP_MULT,e);
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
    return atOpAssign(HIR.OP_DIV,e);
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
    return atOpAssign(HIR.OP_MOD,e);
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
    return atOpAssign(HIR.OP_SHIFT_LL,e);
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
    if( e.getExp1().getType().isUnsigned() )
      return atOpAssign(HIR.OP_SHIFT_RL,e);
    else
      return atOpAssign(HIR.OP_SHIFT_R,e);
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
    return atOpAssign(HIR.OP_AND,e);
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
    return atOpAssign(HIR.OP_OR,e);
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
    return atOpAssign(HIR.OP_XOR,e);
  }
  //-------------------------------------------------------------------
  // node utility
  //-------------------------------------------------------------------
  /**
   * Get l-value node to store the result of parameter e.
   * <pre>
   * The l-value is required if the parameter e is
   * a condition expression (?:), or a function call expression
   * that requires treatment for side-effect completion point.
   *
   * 1) If parent of e is null, return null.
   *   a?b:c;  --&gt;  if(a) b; else c;
   *
   * 2) If parent of e is AssignStmt, return the left side expression r.
   *   r = a?b:c;  --&gt;  if(a) r=b; else r=c;
   *
   *   If r is a complex expression, generate a temporal tmp and return
   *   *tmp in order to decrease overhead.
   *     s.e = a?b:c;  --&gt;  tmp=&s.e; if(a) *tmp=b; else *tmp=c;
   *
   *   The tmp is generated in getLittleOverheadLvalueNode.
   *
   * 3) If the parent of e is a statement neither AssignStmt nore assignment
   *    expression, then return tmp.
   *   x + a?b:c  --&gt;  if(a) tmp=b; else tmp=c; x+tmp;
   * </pre>
   * @param   e Expression to be expanded to if-statement.
   * @param   t Type of tmp used in the case 3).
   * @return  L-value with less overhead.
  **/
  private Exp getLvalueNode(Exp e,Type t)
  {
    IR parent = e.getParent();
    //IR parent = e;
    //do
    //  parent = parent.getParent(); //SF041004
    //while( parent!=null && parent.getOperator()==HIR.OP_CONV ); //SF041004

    if( parent==null ) // 1)
      return null;

    if( parent.getOperator()==HIR.OP_ASSIGN ) // 2)
    {
      Exp e1 = ((Exp)parent).getExp1();
      e1 = getLittleOverheadLvalueNode(e1);
      ((HIR)parent).setChild1(e1);
      return e1;
    }
    return toHir.newTempVarNode(t); // 3)
  }
  //-------------------------------------------------------------------
  /**
   * Get l-value with less overhead assuming that the parameter e
   * is an l-value expression already visited.
   * <pre>
   *    parameter        statement  returned
   *       e             inserted   expression
   *  ----------------------------------------
   *     1)  v        --&gt;                v
   *     2)  *p       --&gt;                *p
   *     3)  *(p+i)   --&gt;  tmp=p+i       *tmp
   *     4)  s.m      --&gt;                s.m    (m is bit-field)
   *     5)  s[i].m   --&gt;  tmp=&s[i]     tmp->m (m is bit-field)
   *     6)  p->m     --&gt;                p->m   (m is bit-field)
   *     7)  (p+i)->m --&gt;  tmp=p+i       tmp->m (m is bit-field)
   *  else)  s.m      --&gt;  tmp=&s.m      *tmp
   * </pre>
   * @param   e L-value expression already visited.
   * @return  L-value expression with less overhead.
  **/
  private Exp getLittleOverheadLvalueNode(Exp e)
  {
    switch( e.getOperator() )
    {
    case HIR.OP_VAR: // 1)
    case HIR.OP_PARAM:
      return e;

    case HIR.OP_CONTENTS:
      Exp e1 = e.getExp1();
      switch( e1.getOperator() )
      {
      case HIR.OP_VAR: // 2)
      case HIR.OP_PARAM:
        return e;

      default: // 3)
        VarNode tmpvar = toHir.newTempVarNode(e1.getType());
        buffer.addPrev(
          toHir.newAssignStmt(
            (Exp)tmpvar.copyWithOperands(),
            e1 ) );
        return hir.contentsExp(tmpvar);
      }

    case HIR.OP_QUAL:
      e1 = e.getExp1();
      Elem elem = e.getExp2().getElem();
      if( elem.isBitField() )
        switch( e1.getOperator() )
        {
          case HIR.OP_VAR: // 4)
          case HIR.OP_PARAM:
            return e;

          default: // 5)
            VarNode tmpvar = toHir.newTempVarNode(
              sym.pointerType(e1.getType()) );
            buffer.addPrev(
              toHir.newAssignStmt(
                (Exp)tmpvar.copyWithOperands(),
                toHir.addrExp(e1) ) );
            return hir.pointedExp(tmpvar,(ElemNode)e.getExp2());
        }
      break;

    case HIR.OP_ARROW:
      e1 = e.getExp1();
      elem = e.getExp2().getElem();
      if( elem.isBitField() )
        switch( e1.getOperator() )
        {
          case HIR.OP_VAR: // 6)
          case HIR.OP_PARAM:
            return e;

          default: // 7)
            VarNode tmpvar = toHir.newTempVarNode(e1.getType());
            buffer.addPrev(
              toHir.newAssignStmt(
                (Exp)tmpvar.copyWithOperands(),
                e1 ) );
            return hir.pointedExp(tmpvar,(ElemNode)e.getExp2());
        }
      break;
    }
    // else)
    VarNode tmpvar = toHir.newTempVarNode( sym.pointerType(e.getType()) );
    buffer.addPrev(
      toHir.newAssignStmt(
        (Exp)tmpvar.copyWithOperands(),
        toHir.addrExp(e) ) );
    return hir.contentsExp(tmpvar);
  }
  //-------------------------------------------------------------------
  /**
  * Create HIR-base node of "e1 = e1 op e2".
  *
  * @param   op HIR.OP_ADD/SUB/MUL/DIV/MOD/SHIFT_L/SHIFT_R/AND/OR/XOR
  * @param   e1 Exp
  * @param   e2 Exp
  * @return  AssignStmt
  **/
  private Stmt newOpAssignStmt(int op,Exp e1,Exp e2)
  {
    Type t1 = e1.getType();
    Type t2 = e2.getType();
    Exp opexp;
    if( t1.getTypeKind()!=Type.KIND_POINTER )
    {
      // arith + arith --> (uactype)arith + (uactype)arith
      Type uactype = toCast.getUacType(t1,t2);
      opexp =
        hir.exp(
          op,
          toCast.cast(uactype,(Exp)e1.copyWithOperands()),
          toCast.cast(uactype,e2) );
    }
    else // pointer+index --> pointer+(sizeof(pointedtype)*index)
    {
      Type pointedtype = ((PointerType)t1).getPointedType();
      opexp =
        hir.exp(
          op,
          (Exp)e1.copyWithOperands(),
          hir.exp(
            HIR.OP_MULT,
            (Exp)pointedtype.getSizeExp().copyWithOperands(),
            hir.convExp(toHir.symRoot.typeOffset,
                        //##92 e2
                        (Exp)e2.copyWithOperands() //##92
                        ) ) );
    }
    return
      toHir.newAssignStmt(
        (Exp)e1.copyWithOperands(),
        toCast.assignCast(t1,opexp) ); //SF050215
  }
  //-------------------------------------------------------------------
}
