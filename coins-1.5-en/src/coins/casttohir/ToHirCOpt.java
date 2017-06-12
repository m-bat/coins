/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.casttohir;

import coins.ir.hir.AssignStmt; //##85
import coins.ir.hir.ConstNode;
import coins.ir.hir.Exp;
import coins.ir.hir.ExpStmt;
import coins.ir.hir.FunctionExp;
import coins.ir.hir.HIR;
import coins.ir.hir.HirList;
import coins.ir.hir.IfStmt;
import coins.ir.hir.LoopStmt;
import coins.ir.hir.ReturnStmt;
import coins.ir.hir.SwitchStmt;
import coins.ir.hir.VarNode;
import coins.sym.PointerType;
import coins.sym.Sym;
import coins.sym.Type;
import coins.sym.Var;

/**
 * Do simple optimizations (optimizations of expressions and conditions)
 * for HIR-C.
 * The level of optimizations can be specified by compiler option.
 * ToHirCOpt does the optimizations of kind 1 throgh 3
 * as default optimizations.
 * <pre>
 * Expression optimization
 *
 * Kind 1. Non-volatile const-variable with initial value
 *   is changed to constant.
 *   ( At present, this transformation is bypassed
 *     because C parser ignores const qualifier.)
 *
 * Kind 2. Optimize pointer expressions
 *   Convert pointer arithmetic expression to array subscript expression.
 *   This conversion makes easy to find candidate arrays of parallelization
 *   without doing pointer analysis.
 *   a. (DECAY(array)+i+j)  --&gt;  array[i+j]
 *   b. (pointer+i+j)       --&gt;  UNDECAY(pointer)[i+j]
 *
 *   However, it is limited that b. is done when the following conditions are
 *   satisfied. The pointer which satisfies these conditions is
 *   "Array parameter to which the content is not changed."
 *   . The value is not substituted.
 *   . The value is not acquired (is not copied).
 *   . The address is not operated. (ptr+int ptr-int)
 *   . The address is not taken. (&ptr)
 *   . The number of elements is not 0. (The pointer type maintains the number
 *       of elements when declared as an array parameter of C language.
 *       'Pointer type with range information' )
 *
 * Kind 3. Eliminate mutually opposing operations
 *   *&v    --&gt;  v
 *   &*v    --&gt;  v
 *   ~~v    --&gt;  v
 *   -(-v)  --&gt;  v
 *   DECAY(UNDECAY(pointer))  --&gt;  pointer
 *   UNDECAY(DECAY(array))    --&gt;  array
 *
 * Kind 4. Do optimization for special constants
 *   Special constants:
 *   . 0
 *   . 1
 *   . ALL_BITS_ARE_1: constant having 1 on all bit positions
 *       and having precesion greater or equal than that of
 *       at least one operand.
 *   . MORE_THAN_SIZE: constant having bit size greater than
 *       that of resultant expression.
 *
 *     v +  0  --&gt;  v
 *     v -  0  --&gt;  v
 *     v -  v  --&gt;  0 (v is non volatile integer or pointer)
 *     v *  0  --&gt;  0
 *     v /  0  --&gt;  issue warning
 *     v /  1  --&gt;  v
 *     v %  0  --&gt;  issue warning
 *     v %  1  --&gt;  0
 *     v &  0  --&gt;  0
 *     v &  ALL_BITS_ARE_1  --&gt;  v
 *     v |  0               --&gt;  v
 *     v |  ALL_BITS_ARE_1  --&gt;  ALL_BITS_ARE_1
 *     v &lt;&lt; 0               --&gt;  v
 *     v &lt;&lt; MORE_THAN_SIZE  --&gt;  0
 *     v &gt;&gt; 0               --&gt;  v
 *     v &gt;&gt; MORE_THAN_SIZE  --&gt;  0 (v is unsigned integer)
 *     v &gt;&gt;&gt;MORE_THAN_SIZE  --&gt;  0
 *
 *     [ 0 -  v  --&gt;  -v (not supported) ]
 *
 * Condition optimization
 *
 * Kind 5. Do inversion of comparison
 *     !(a&lt;b)  --&gt;  a&gt;=b
 *     (&gt;, &lt;=, &gt;=, ==, != are transformed in the similar way)
 *   Before reaching to here, logical-not has been already converted to equality
 *   expression and comparison has been converted to conditional expression
 *   in ToHirC2. So, in actual coding, they are transformed in the following
 *   way:
 *    (a&lt;b ? CONST_X : CONST_Y)==CONST_X  --&gt;  a&lt;b
 *    (a&lt;b ? CONST_X : CONST_Y)==CONST_Y  --&gt;  a&gt;=b (inversion of comparison)
 *    (a&lt;b ? CONST_X : CONST_Y)!=CONST_Y  --&gt;  a&lt;b
 *    (a&lt;b ? CONST_X : CONST_Y)!=CONST_X  --&gt;  a&gt;=b (inversion of comparison)
 *  (&gt;, &lt;=, &gt;=, ==, != are transformed in the similar way)
 *
 * 6. Convert comparison that leads to always true or always false
 *   to constant issuing warning
 *     constant-a comparison-operator constant-b  --&gt; true or false
 *     variable &gt;  maximal value  --&gt; false
 *     variable &lt;  minimum value  --&gt; false
 *     variable &gt;= minimum value  --&gt; true
 *     variable &lt;= maximum value  --&gt; true
 *   Following transformations are applied not only ADDR but also DECAY:
 *     (ADDR object-x) == (ADDR object-x)  --&gt; true
 *     (ADDR object-x) == (ADDR object-y)  --&gt; false
 *     (ADDR object-x) == 0                --&gt; false
 *     (ADDR object-x) != (ADDR object-x)  --&gt; false
 *     (ADDR object-x) != (ADDR object-y)  --&gt; true
 *     (ADDR object-x) != 0                --&gt; true
 *
 * 7. If left operand of && or || is true or false, then
 *   change it to (side effect expression of left operand)
 *   followed with comma and (right operand or trure or false)
 *    e1 && e2
 *      false && e2  --&gt;  (side effect of e1),false
 *      true  && e2  --&gt;  (side effect of e1),e2
 *    e1 || e2
 *      false || e2  --&gt;  (side effect of e1),e2
 *      true  || e2  --&gt;  (side effect of e1),true
 * 8. If conditional expression part c of select expression is always
 *   true or false, then change it to (side effect of c) followed by
 *   comma and (2nd or 3rd operand)
 *      e1 ? e2 : e3  --&gt; (side effect of e1), e2 when e1 is always true
 *      e1 ? e2 : e3  --&gt; (side effect of e1), e3 when e1 is always false
 *      r = (a=1)&gt;0 ? 1 : 0;  --&gt;  r = (a=1) , 1;
 *
 * Note
 *   Side effects are placed at the point immediately preceeding the
 *   side effect completion point and combined with rest of given expression
 *   by using comma operator. Foe example,
 *   . if( f()*0 != 0 ) --&gt; if( f(),false )
 *   . a += f()*0; --&gt; f(),a+=0; --&gt; f();
 * Note
 *   Side effect completion point (sequence point)
 *   . after evaluation of an actual parameter
 *   . logical-and expression &&
 *   . logical-or  expression ||
 *   . selection   expression ?:
 *   . comma       expression ,
 *   . initiation  expression
 *   . expression statement (ExpStmt)
 *   . conditional expression part of if/switch/while/do-while
 *   . three parts s1, e2, s3 of for-statement for(s1; e2; s3)
 *   . optional return value expression of return statement
 * </pre>
 *  @author  Shuichi Fukuda
**/
public class ToHirCOpt extends ToHirVisit
{
  protected final ToHir             toHir;
  protected final HIR               hir;
  protected final Sym               sym;
  protected final ToHirCast         toCast;
  protected final SideEffectBuffer  buffer;
  protected final ConditionInverter inverter;

  //-------------------------------------------------------------------
  /**
   * Constructor.
   *
   * @param  tohir Offers cooperation with the object of other packages.
  **/
  public ToHirCOpt(ToHir tohir)
  {
    super(tohir);
    toHir    = tohir;
    hir      = tohir.hirRoot.hir;
    sym      = tohir.hirRoot.sym;
    message(1,"ToHirCOpt\n"); //##71
    toCast   = new ToHirCast(tohir);
    buffer   = new SideEffectBuffer(tohir);
    inverter = new ConditionInverter(tohir);
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
    toHir.debug.print( level, "CO", mes );
  }
  //-------------------------------------------------------------------
  /**
   * At if statement node.
   *
   * @param  s IfStmt
  **/
  protected void atIf(IfStmt s)
  {
    super.atIf(s);
    s.setIfCondition( buffer.toExp( s.getIfCondition() ) );
  }
  //-------------------------------------------------------------------
  /**
   * At while statement node.
   *
   * @param  s LoopStmt
  **/
  protected void atWhile(LoopStmt s)
  {
    super.atWhile(s);
    s.setLoopStartCondition( buffer.toExp( s.getLoopStartCondition() ) );
  }
  //-------------------------------------------------------------------
  /**
   * At for statement node.
   *
   * @param  s LoopStmt
  **/
  protected void atFor(LoopStmt s)
  {
    super.atFor(s);
    s.setLoopStartCondition( buffer.toExp( s.getLoopStartCondition() ) );
  }
  //-------------------------------------------------------------------
  /**
   * At do-while statement node.
   *
   * @param  s LoopStmt
  **/
  protected void atUntil(LoopStmt s)
  {
    super.atUntil(s);
    s.setLoopEndCondition( buffer.toExp( s.getLoopEndCondition() ) );
  }
  //-------------------------------------------------------------------
  /**
   * At switch statement node.
   *
   * @param  s SwitchStmt
  **/
  protected void atSwitch(SwitchStmt s)
  {
    super.atSwitch(s);
    s.setSelectionExp( buffer.toExp( s.getSelectionExp() ) );
  }
  //-------------------------------------------------------------------
  /**
   * At return statement node.
   *
   * @param  s ReturnStmt
  **/
  protected void atReturn(ReturnStmt s)
  {
    s.setReturnValue( buffer.toExp(visitExp( s.getReturnValue() )) );
  }
  //-------------------------------------------------------------------
  /**
   * At expression statement node.
   *
   * @param  s ExpStmt
  **/
  protected void atExpStmt(ExpStmt s)
  {
    s.setExp( buffer.toExp(visitExp( s.getExp() )) );
  }
  //-------------------------------------------------------------------
  /**
   * At variable node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atVar(VarNode e)
  {
    Type t = ((Var)e.getSymNodeSym()).getSymType();
    //##85 BEGIN
    HIR lParent = (HIR)e.getParent();
    if (lParent != null) //###
    if ((lParent.getOperator() == HIR.OP_ASSIGN)&&
        (e.getChildNumber() == 1)) {
      // If the type of Assign statement is qualified with const,
      // its LHS must be qualified with const and
      // it must be initial value assignment statement.
      return e;
    }
    //##85 END
    if( !inInitBlock()
    &&  e.getParent().getOperator()!=HIR.OP_ADDR // not addressed
    &&  !t.isVolatile()                          // non-volatile
    &&  t.isConst() )                            // const variable is used
    {
      Exp init = ((Var)e.getSym()).getInitialValue();
      if( init!=null
      &&  init.getOperator()==HIR.OP_CONST ) // having initial value
      //!! init.getOperator()==OP_EXPLIST should also be treated
      {
        if (fDbgLevel > 3) //##67
          message(4,"const "+((Var)e.getSym()).getName()+" --> "+init);
        return toCast.cast( t, (Exp)init.copyWithOperands() ); //SF041206
      }
    }
    return e;
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
    e.setFunctionSpec( buffer.toExp(visitExp(e.getFunctionSpec())) );

    HirList actuallist = (HirList)e.getParamList();
    for( java.util.ListIterator i=actuallist.iterator(); i.hasNext(); )
    {
      Exp re = buffer.toExp(visitExp( (Exp)i.next() ));
      i.set(re);
      re.setParent(actuallist);
    }
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At not expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atNot(Exp e) // ~
  {
    // ~~v --> v
    Exp e1 = e.getExp1();
    if( e1.getOperator()==HIR.OP_NOT )
    {
      if (fDbgLevel > 3) //##67
        message(4,"~~v --> v: "+ToC.tos(e));
      return visitExp(e1.getExp1());
    }
    return super.atNot(e);
  }
  //-------------------------------------------------------------------
  /**
   * At negative expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atNeg(Exp e) // -
  {
    // --v --> v
    Exp e1 = e.getExp1();
    if( e1.getOperator()==HIR.OP_NEG )
    {
      if (fDbgLevel > 3) //##67
        message(4,"--v --> v: "+ToC.tos(e));
      return visitExp(e1.getExp1());
    }
    return super.atNeg(e);
  }
  //-------------------------------------------------------------------
  /**
   * At address expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atAddr(Exp e) // &
  {
    // &*v --> v
    Exp e1 = e.getExp1();
    if( e1.getOperator()==HIR.OP_CONTENTS )
    {
      if (fDbgLevel > 3) //##67
        message(4,"&*v --> v: "+ToC.tos(e));
      return visitExp(e1.getExp1());
    }
    return super.atAddr(e);
  }
  //-------------------------------------------------------------------
  /**
   * At decay expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atDecay(Exp e)
  {
    // decay(undecay(pointer)) --> pointer
    Exp e1 = e.getExp1();
    if( e1.getOperator()==HIR.OP_UNDECAY )
    {
      if (fDbgLevel > 3) //##67
        message(4,"decay(undecay(v)) --> v: "+ToC.tos(e));
      return visitExp(e1.getExp1());
    }
    return super.atDecay(e);
  }
  //-------------------------------------------------------------------
  /**
   * At undecay expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atUndecay(Exp e)
  {
    // undecay(decay(array))   --> array
    Exp e1 = e.getExp1();
    if( e1.getOperator()==HIR.OP_DECAY )
    {
      if (fDbgLevel > 3) //##67
        message(4,"undecay(decay(v)) --> v: "+ToC.tos(e));
      return visitExp(e1.getExp1());
    }
    return super.atUndecay(e);
  }
  //-------------------------------------------------------------------
  /**
   * At indirection expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atContents(Exp e) // *
  {
    // *&v --> v
    Exp e1 = e.getExp1();
    if( e1.getOperator()==HIR.OP_ADDR )
    {
      if (fDbgLevel > 3) //##67
        message(4,"*&v --> v: "+ToC.tos(e));
      return visitExp(e1.getExp1());
    }
    super.atContents(e);

    // *(pointer+i+j) --> undecay(pointer)[i+j]
    Exp ptrnode = findUndecayablePointerNode( e.getExp1() );
    if( ptrnode!=null )
    {
      if( ptrnode.getOperator()==HIR.OP_DECAY ) // decay(array) --> array[e1]
      {
        if (fDbgLevel > 3) //##67
          message(4,"*(decay(a)+i) --> a[i]: "+ToC.tos(e));
        return toHir.subsExp(
          ptrnode.getExp1(),
          toCast.iPromotion(e.getExp1()) ); //SF040716
      }
      else // pointer --> undecay(pointer)[e1]
      {
        if (fDbgLevel > 3) //##67
          message(4,"*(ptr+i) --> undecay(ptr)[i]: "+ToC.tos(e));
        long elemcount = ((PointerType)ptrnode.getType()).getElemCount();
        if (elemcount <= 0) //##81
          elemcount = 1;    //##81
        return toHir.subsExp(
          hir.undecayExp(ptrnode,elemcount),
          toCast.iPromotion(e.getExp1()) ); //SF040716
      }
    }
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
    e = inverter.visitExp(e.getExp1());
    return visitExp(e);
  }
  //-------------------------------------------------------------------
  // PRIVATE
  //-------------------------------------------------------------------
  /**
   * Get undecayable pointer node to do pointer expression transformations
   * such as
   * <pre>
   *   *(decay(a)+i) --&gt; a[i]
   *   *(ptr+i) --&gt; undecay(ptr)[i]
   * </pre>
   * If the pointer expression e given as a parameter is an expression that
   * can be undecayed, then return the pointer node.
   * If no such pointer is found, return null.
   * Undecayable pointers are
   * <pre>
   * - (decay str) where str is character string
   * - (decay var) where var is array variable
   * - pointer that has no assignment statement setting value to it and has no addr operation to it.
   * </pre>
   * When a pointer node is returned, the given pointer expression is changed
   * to a subscript expression of subs operator.
   * <pre>
   *   ptr+i+j --&gt; i+j and return ptr
   *   ptr     --&gt; 0   and return ptr
   * </pre>
   * @param   e Pointer expresion.
   * @return  Pointer node or null.
  **/
  private Exp findUndecayablePointerNode(Exp e)
  {
    switch( e.getOperator() )
    {
    case HIR.OP_DECAY: // decay(obj)
      if( isObject(e.getExp1()) ) //SF050119
      {
        ((HIR)e.getParent()).setChild1( toHir.new0Node() );
        return e;
      }
      return null;

    case HIR.OP_VAR: // ptr
      Sym s = ((VarNode)e).getSymNodeSym();
      //message(6,"VAR="+s+
      //         " ELEMCOUNT="+((PointerType)s.getSymType()).getElemCount()+
      //         " ADDRESS="+s.getFlag(Sym.FLAG_ADDRESS_TAKEN)+
      //         " POINTER="+s.getFlag(Sym.FLAG_POINTER_OPERATION));
      if( !s.getFlag(Sym.FLAG_ADDRESS_TAKEN)
      &&  !s.getFlag(Sym.FLAG_VALUE_IS_ASSIGNED)
      &&  ((PointerType)s.getSymType()).getElemCount()!=0 )
      {
        ((HIR)e.getParent()).setChild1( toHir.new0Node() );
        return e;
      }
      //##81 BEGIN
      if (s.getSymType().isRestrict()||
          toHir.safeArrayAll ||((toHir.symRoot.subpCurrent != null)&&
          toHir.symRoot.subpCurrent.isSafeArrayAll())||
          toHir.symRoot.safeArray.contains(s)) {
        ((HIR)e.getParent()).setChild1( toHir.new0Node() );
        return e;
      }
      //##81 END
      return null;

    case HIR.OP_ADD: // e1 + i //SF041004
      Exp ret = findUndecayablePointerNode( e.getExp1() );
      if( ret!=null )
      {
        if( e.getExp1().getOperator()==HIR.OP_CONST
        &&  ((ConstNode)e.getExp1()).isIntConst0() )
          ((HIR)e.getParent()).setChild1(
            toCast.iPromotion(e.getExp2()) );
        else
          e.setType(toHir.symRoot.typeInt);
      }
      return ret;

    case HIR.OP_SUB: // e1 - i //SF041004
      ret = findUndecayablePointerNode( e.getExp1() );
      if( ret!=null )
      {
        if( e.getExp1().getOperator()==HIR.OP_CONST
        &&  ((ConstNode)e.getExp1()).isIntConst0() )
          ((HIR)e.getParent()).setChild1(
            hir.exp( HIR.OP_NEG, toCast.iPromotion(e.getExp2()) ) );
        else
          e.setType(toHir.symRoot.typeInt);
      }
      return ret;

    default:
      return null;
    }
  }
  //SF050119[
  private boolean isObject(Exp e)
  {
    switch( e.getOperator() )
    {
    case HIR.OP_CONST: // decay(str)
    case HIR.OP_VAR:   // decay(var)
      return true;
    case HIR.OP_SUBS:  // decay(subs(array,index))
    case HIR.OP_QUAL:
      return isObject(e.getExp1());
    }
    return false;
  }
  //SF050119]
  //-------------------------------------------------------------------
}
