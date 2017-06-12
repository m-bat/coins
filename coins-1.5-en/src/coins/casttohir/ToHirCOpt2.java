/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.casttohir;

import coins.ir.hir.ConstNode;
import coins.ir.hir.Exp;
import coins.ir.hir.HIR;
import coins.ir.hir.SymNode;
import coins.ir.hir.VarNode;
import coins.sym.Const;
import coins.sym.Sym;
import coins.sym.Type;

/**
 * Do HIR-C optimization for arithmetic expression and conditional expression.
 * Optimization level is controlled by compile option.
 * ToHirCOpt2 does kind 4 through kind 8 optimizations described in ToHirCOpt.
 *
 * @author  Shuichi Fukuda
**/
public class ToHirCOpt2 extends ToHirCOpt
{
  protected ToHirCOpt2 toOpt;

  protected final SideEffectCutter  cutter;

  private static final int
    BIG     =  4, // big
    MAX     =  2, // maximum value
    EQUAL   =  0, // equal
    MIN     = -2, // minimum value
    SMALL   = -4, // small
    CANNOT  =  9, // undecidable
    CANNOT2 = -9; // undecidable

  //-------------------------------------------------------------------
  /**
   * Constructor.
   *
   * @param  tohir Offers cooperation with the object of other packages.
  **/
  public ToHirCOpt2(ToHir tohir)
  {
    super(tohir);
    message(1,"ToHirCOpt2\n"); //##71
    cutter   = new SideEffectCutter(tohir,buffer);
  }
  //-------------------------------------------------------------------
  /**
   * Make sure child of this object.
   *
   * @return  Child of this object.
  **/
  protected ToHirCOpt2 sureChild()
  {
    if( toOpt==null )
      toOpt = new ToHirCOpt2(toHir);
    return toOpt;
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
    toHir.debug.print( level, "CP", mes );
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
    super.atAdd(e);
    // v+0 --> v
    // v-0 --> v
    Exp e1 = e.getExp1();
    Exp e2 = e.getExp2();
    if( is0(e1) )
    {
      if (fDbgLevel > 3) //##67
        message(4,"0+v 0-v --> v: "+ToC.tos(e));
      return e2;
    }
    if( is0(e2) )
    {
      if (fDbgLevel > 3) //##67
        message(4,"v+0 v-0 --> v: "+ToC.tos(e));
      return e1;
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
    super.atSub(e);
    // v+0 --> v
    // v-0 --> v
    Exp e1 = e.getExp1();
    Exp e2 = e.getExp2();
    if( is0(e1) )
    {
      if (fDbgLevel > 3) //##67
        message(4,"0+v 0-v --> v: "+ToC.tos(e));
      return e2;
    }
    if( is0(e2) )
    {
      if (fDbgLevel > 3) //##67
        message(4,"v+0 v-0 --> v: "+ToC.tos(e));
      return e1;
    }
    // Until here, do the same operation as atAdd.
    // v-v --> 0 (v is non-volatile integer or pointer expression)
    if( e1.getOperator()==HIR.OP_VAR
    &&  e2.getOperator()==HIR.OP_VAR
    &&  ((VarNode)e1).getVar()==((VarNode)e2).getVar() )
    {
      Type t1 = e1.getType();
      if( !t1.isVolatile()
      &&  toHir.isIntegral(t1) )
        return toCast.cast(t1,toHir.new0Node());
    }
    return e;
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
    // v*0    --> 0
    Exp e1 = e.getExp1();
    Exp e2 = e.getExp2();
    if( is0(e1) )
    {
      if (fDbgLevel > 3) //##67
        message(4,"0*v --> 0: "+ToC.tos(e));
      cutter.visitExp(e2); // Sweep out side effects.
      return toHir.new0Node();
    }
    if( is0(e2) )
    {
      if (fDbgLevel > 3) //##67
        message(4,"v*0 --> 0: "+ToC.tos(e));
      cutter.visitExp(e1); // Sweep out side effects.
      return toHir.new0Node();
    }
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At mul expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atDiv(Exp e)
  {
    super.atDiv(e);
    // v/0    --> warning
    // v/1    --> v
    Exp e1 = e.getExp1();
    Exp e2 = e.getExp2();
    if( is0(e2) )
    {
      toHir.warning("Division by zero.");
    }
    else if( is1(e2) )
    {
      if (fDbgLevel > 3) //##67
        message(4,"v/1 --> v: "+ToC.tos(e));
      return e1;
    }
    return e;
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
    super.atMod(e);
    // v%0    --> warning
    // v%1    --> 0
    Exp e1 = e.getExp1();
    Exp e2 = e.getExp2();
    if( is0(e2) )
    {
      toHir.warning("Division by zero.");
    }
    else if( is1(e2) )
    {
      if (fDbgLevel > 3) //##67
        message(4,"v%1 --> 0: "+ToC.tos(e));
      cutter.visitExp(e1); // Sweep out side effects.
      return toHir.new0Node();
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
    super.atAnd(e);
    // v&0    --> 0
    // v&ALL1 --> v
    Exp e1 = e.getExp1();
    Exp e2 = e.getExp2();
    if( is0(e1) )
    {
      if (fDbgLevel > 3) //##67
        message(4,"0&v --> 0"+e);
      cutter.visitExp(e2); // Sweep out side effects.
      return toHir.new0Node();
    }
    if( is0(e2) )
    {
      if (fDbgLevel > 3) //##67
        message(4,"v&0 --> 0: "+ToC.tos(e));
      cutter.visitExp(e1); // Sweep out side effects.
      return toHir.new0Node();
    }
    if( e1.getOperator()==HIR.OP_CONST
    &&  masksPerfectly(e2,(Const)e1.getSym()) )
    {
      if (fDbgLevel > 3) //##67
        message(4,"ALL1&v --> v: "+ToC.tos(e));
      return e2;
    }
    if( e2.getOperator()==HIR.OP_CONST
    &&  masksPerfectly(e1,(Const)e2.getSym()) )
    {
      if (fDbgLevel > 3) //##67
        message(4,"v&ALL1 --> v: "+ToC.tos(e));
      return e1;
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
  protected Exp atOr(Exp e)
  {
    super.atOr(e);
    // v|0    --> v
    // v|ALL1 --> ALL1
    Exp e1 = e.getExp1();
    Exp e2 = e.getExp2();
    if( is0(e1) )
    {
      if (fDbgLevel > 3) //##67
        message(4,"0|v --> v: "+ToC.tos(e));
      return e2;
    }
    if( is0(e2) )
    {
      if (fDbgLevel > 3) //##67
        message(4,"v|0 --> v: "+ToC.tos(e));
      return e1;
    }
    if( e1.getOperator()==HIR.OP_CONST
    &&  masksPerfectly(e2,(Const)e1.getSym()) )
    {
      if (fDbgLevel > 3) //##67
        message(4,"ALL1|v --> ALL1: "+ToC.tos(e));
      cutter.visitExp(e2); // Sweep out side effects.
      return e1;
    }
    if( e2.getOperator()==HIR.OP_CONST
    &&  masksPerfectly(e1,(Const)e2.getSym()) )
    {
      if (fDbgLevel > 3) //##67
        message(4,"v|ALL1 --> ALL1: "+ToC.tos(e));
      cutter.visitExp(e1); // Sweep out side effects.
      return e2;
    }
    return e;
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
    super.atXor(e);
    // v^0    --> v
    Exp e1 = e.getExp1();
    Exp e2 = e.getExp2();
    if( is0(e1) )
    {
      if (fDbgLevel > 3) //##67
        message(4,"v^0 --> v: "+ToC.tos(e));
      return e2;
    }
    if( is0(e2) )
    {
      if (fDbgLevel > 3) //##67
        message(4,"0^v --> v: "+ToC.tos(e));
      return e1;
    }
    return e;
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
    super.atCmpEq(e);
    Exp e1 = e.getExp1();
    Exp e2 = e.getExp2();

    switch( getCompareRelation(e1,e2) )
    {
    case EQUAL: return alwaysTrue("==",e);
    case BIG:
    case SMALL: return alwaysFalse("==",e);
    }

    // 1) (a<b? CONST_X: CONST_Y)==CONST_X  -->  a<b
    // 2) (a<b? CONST_X: CONST_Y)==CONST_Y  -->  a>=b  (invert operator)
    if( e1.getOperator()==HIR.OP_CONST )
    {
      Exp tmp=e1; e1=e2; e2=tmp;
    }
    if( e1.getOperator()==HIR.OP_SELECT
    &&  e2.getOperator()==HIR.OP_CONST )
    {
      Exp e12 = e1.getExp2();
      Exp e13 = (Exp)e1.getChild(3);
      if( e12.getOperator()==HIR.OP_CONST
      &&  e13.getOperator()==HIR.OP_CONST )
        switch( getCompareRelationOfConst((Const)e12.getSym(),(Const)e2.getSym()) )
        {
        case EQUAL:
          switch( getCompareRelationOfConst((Const)e13.getSym(),(Const)e2.getSym()) )
          {
          case BIG: case SMALL:
            if (fDbgLevel > 3) //##67
              message(4,"simplize: "+ToC.tos(e));
            return e1.getExp1(); // 1)
          }
          break;
        case BIG: case SMALL:
          switch( getCompareRelationOfConst((Const)e13.getSym(),(Const)e2.getSym()) )
          {
          case EQUAL:
            if (fDbgLevel > 3) //##67
              message(4,"imvert: "+ToC.tos(e));
            return inverter.visitExp( e1.getExp1() ); // 2)
          }
          break;
        }
    }

    // // (addr|decay object) == (addr|decay same object) --> true
    // // (addr|decay object) == (addr|decay other object) --> false
    // // (addr|decay object) == 0 --> false
    Sym   s1, s2;
    Const c1, c2;
    if( (s1=findAddressedSym(e1))!=null )
    {
      if( (s2=findAddressedSym(e2))!=null ) // addr == addr
      {
        if (fDbgLevel > 3) //##67
          message(4,"addr==addr --> "+(s1==s2)+": "+ToC.tos(e));
        return s1==s2
             ? toHir.newTrueNode()
             : toHir.newFalseNode();
      }
      if( (c2=findConstantSym(e2))!=null && c2.longValue()==0 ) // addr == 0
      {
        if (fDbgLevel > 3) //##67
          message(4,"addr==0 --> false: "+ToC.tos(e));
        return toHir.newFalseNode();
      }
    }
    else if( (c1=findConstantSym(e1))!=null && c1.longValue()==0 )
    {
      if( (s2=findAddressedSym(e2))!=null ) // 0 == addr
      {
        if (fDbgLevel > 3) //##67
          message(4,"0==addr --> false: "+ToC.tos(e));
        return toHir.newFalseNode();
      }
    }
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
    super.atCmpNe(e);
    Exp e1 = e.getExp1();
    Exp e2 = e.getExp2();

    switch( getCompareRelation(e1,e2) )
    {
    case EQUAL: return alwaysFalse("!=",e);
    case BIG:
    case SMALL: return alwaysTrue("!=",e);
    }

    // 1) (a<b? CONST_X: CONST_Y)!=CONST_Y  -->  a<b
    // 2) (a<b? CONST_X: CONST_Y)!=CONST_X  -->  a>=b  (invert operator)
    if( e1.getOperator()==HIR.OP_CONST )
    {
      Exp tmp=e1; e1=e2; e2=tmp;
    }
    if( e1.getOperator()==HIR.OP_SELECT
    &&  e2.getOperator()==HIR.OP_CONST )
    {
      Exp e12 = e1.getExp2();
      Exp e13 = (Exp)e1.getChild(3);
      if( e12.getOperator()==HIR.OP_CONST
      &&  e13.getOperator()==HIR.OP_CONST )
        switch( getCompareRelationOfConst((Const)e12.getSym(),(Const)e2.getSym()) )
        {
        case EQUAL:
          switch( getCompareRelationOfConst((Const)e13.getSym(),(Const)e2.getSym()) )
          {
          case BIG: case SMALL:
            if (fDbgLevel > 3) //##67
              message(4,"invert: "+ToC.tos(e));
            return inverter.visitExp( e1.getExp1() ); // 2)
          }
          break;
        case BIG: case SMALL:
          switch( getCompareRelationOfConst((Const)e13.getSym(),(Const)e2.getSym()) )
          {
          case EQUAL:
            if (fDbgLevel > 3) //##67
              message(4,"simplize: "+ToC.tos(e));
            return e1.getExp1(); // 1)
          }
          break;
        }
    }
    // (addr|decay object) != (addr|decay same object) --> false
    // (addr|decay object) != (addr|decay other object) --> true
    // (addr|decay object) != 0 --> true
    Sym   s1, s2;
    Const c1, c2;
    if( (s1=findAddressedSym(e1))!=null )
    {
      if( (s2=findAddressedSym(e2))!=null ) // addr != addr
      {
        if (fDbgLevel > 3) //##67
          message(4,"addr!=addr --> "+(s1!=s2)+": "+ToC.tos(e));
        return s1!=s2
             ? toHir.newTrueNode()
             : toHir.newFalseNode();
      }
      if( (c2=findConstantSym(e2))!=null && c2.longValue()==0 ) // addr != 0
      {
        if (fDbgLevel > 3) //##67
          message(4,"addr!=0 --> true: "+ToC.tos(e));
        return toHir.newTrueNode();
      }
    }
    else if( (c1=findConstantSym(e1))!=null && c1.longValue()==0 )
    {
      if( (s2=findAddressedSym(e2))!=null ) // 0 != addr
      {
        if (fDbgLevel > 3) //##67
          message(4,"0!=addr --> true: "+ToC.tos(e));
        return toHir.newTrueNode();
      }
    }
    return e;
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
    super.atCmpGt(e);
    Exp e1 = e.getExp1();
    Exp e2 = e.getExp2();

    switch( getCompareRelation(e1,e2) )
    {
    case SMALL:
    case MIN:
    case EQUAL: return alwaysFalse(">",e);
    case BIG:   return alwaysTrue(">",e);
    }
    return e;
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
    super.atCmpGe(e);
    Exp e1 = e.getExp1();
    Exp e2 = e.getExp2();

    switch( getCompareRelation(e1,e2) )
    {
    case SMALL: return alwaysFalse(">=",e);
    case EQUAL:
    case MAX:
    case BIG:   return alwaysTrue(">=",e);
    }
    return e;
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
    super.atCmpLt(e);
    Exp e1 = e.getExp1();
    Exp e2 = e.getExp2();

    switch( getCompareRelation(e1,e2) )
    {
    case SMALL: return alwaysTrue("<",e);
    case EQUAL:
    case MAX:
    case BIG:   return alwaysFalse("<",e);
    }
    return e;
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
    super.atCmpLe(e);
    Exp e1 = e.getExp1();
    Exp e2 = e.getExp2();

    switch( getCompareRelation(e1,e2) )
    {
    case SMALL:
    case MIN:
    case EQUAL: return alwaysTrue("<=",e);
    case BIG:   return alwaysFalse("<=",e);
    }
    return e;
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
    super.atLShift(e);
    Exp e1 = e.getExp1();
    Exp e2 = e.getExp2();

    // v<<0 --> v
    // v<<(number larger than type size) --> 0  (with warning)
    if( e2.getOperator()==HIR.OP_CONST )
    {
      long value = ((Const)e2.getSym()).longValue();
      if( value==0 )
      {
        if (fDbgLevel > 3) //##67
          message(4,"v<<0 --> v: "+ToC.tos(e));
        return e1;
      }
      long size = 8*e.getType().getSizeValue();
      if( value<=-size || size<=value )
      {
        toHir.warning("left shift of more than type size.");
        // Fold E1<<-E2 assuming it to be the same as E1>>E2.
        if( value<=-size && e.getType().isUnsigned()
        ||  size<=value )
        {
          if (fDbgLevel > 3) //##67
            message(4,"v<<MORE --> 0: "+ToC.tos(e));
          cutter.visitExp(e1); // Sweep out side effects.
          return toHir.new0Node();
        }
      }
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
  protected Exp atARShift(Exp e) // >>
  {
    super.atARShift(e);
    Exp e1 = e.getExp1();
    Exp e2 = e.getExp2();

    // v>>0 --> v
    // v>>(number larger than type size)
    // -->  0 (warinig where v is unsigned integer)
    if( e2.getOperator()==HIR.OP_CONST )
    {
      long value = ((ConstNode)e2).getIntValue();
      if( value==0 )
      {
        if (fDbgLevel > 3) //##67
          message(4,"v>>0 --> v: "+ToC.tos(e));
        return e1;
      }
      long size = 8*e1.getType().getSizeValue();
      if( value<=-size || size<=value )
      {
        toHir.warning("arithmetic right shift of more than type size.");
        // Fold E1>>-E2 assuming it to be the same as E1<<E2.
        if( value<=-size
        ||  size<=value && e1.getType().isUnsigned() )
        {
          if (fDbgLevel > 3) //##67
            message(4,"v>>MORE --> 0: "+ToC.tos(e));
          cutter.visitExp(e1); // Sweep out side effects.
          return toHir.new0Node();
        }
      }
    }
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
    super.atRShift(e);
    Exp e1 = e.getExp1();
    Exp e2 = e.getExp2();

    // v>>>0 --> v
    // v>>>(number more than type size) --> 0 (with warning)
    if( e2.getOperator()==HIR.OP_CONST )
    {
      long value = ((ConstNode)e2).getIntValue();
      if( value==0 )
      {
        if (fDbgLevel > 3) //##67
          message(4,"v>>>0 --> v: "+ToC.tos(e));
        return e1;
      }
      long size = 8*e1.getType().getSizeValue();
      if( value<=-size
      ||  size<=value )
      {
        toHir.warning("right shift of more than type size.");
        if (fDbgLevel > 3) //##67
          message(4,"v>>>MORE --> 0: "+ToC.tos(e));
        cutter.visitExp(e1); // Sweep out side effects.
        return toHir.new0Node();
      }
    }
    return e;
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
    Exp e1 = e.getExp1();
    Exp e2 = e.getExp2();

    // v-v --> 0 (where type of v is non-volatile integer or pointer)
    if( e1.getOperator()==HIR.OP_VAR
    &&  e2.getOperator()==HIR.OP_VAR
    &&  ((VarNode)e1).getVar()==((VarNode)e2).getVar() )
    {
      Type t1 = e1.getType();
      if( !t1.isVolatile() )
        return toCast.cast(t1,toHir.new0Node());
    }
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
    ToHirCOpt2  child = sureChild();
    Exp e1 = child.visitExp(e.getExp1());
    if( e1.getOperator()==HIR.OP_CONST )
    {
      if( ((ConstNode)e1).getIntValue()==0 )
      {
        // false && e2 --> side effect of e1,false
        if (fDbgLevel > 3) //##67
          message(4,"false&&e2 --> false: "+ToC.tos(e));
        buffer.addPrev(child.buffer); // Side effect of e1
        return e1;
      }
      else
      {
        // true && e2 --> side effect of e1, side effect of e2,e2
        if (fDbgLevel > 3) //##67
          message(4,"true&&e2 --> e2: "+ToC.tos(e));
        buffer.addPrev(child.buffer);
        return visitExp(e.getExp2());
      }
    }
    e.setChild1( child.buffer.toExp(e1) );
    Exp e2 = child.visitExp(e.getExp2());
    e.setChild2( child.buffer.toExp(e2) );
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
    ToHirCOpt2 child = sureChild();
    Exp e1 = child.visitExp(e.getExp1());
    if( e1.getOperator()==HIR.OP_CONST )
    {
      if( ((ConstNode)e1).getIntValue()==0 )
      {
        // false || e2 --> side effect of e1, side effect of e2,e2
        if (fDbgLevel > 3) //##67
          message(4,"false||e2 --> e2: "+ToC.tos(e));
        buffer.addPrev(child.buffer);
        return visitExp(e.getExp2());
      }
      else
      {
        // true  || e2 --> side effect of e1,true
        if (fDbgLevel > 3) //##67
          message(4,"true||e2 --> true: "+ToC.tos(e));
        buffer.addPrev(child.buffer);
        return e1;
      }
    }
    e.setChild1( child.buffer.toExp(e1) );
    Exp e2 = child.visitExp(e.getExp2());
    e.setChild2( child.buffer.toExp(e2) );
    return e;
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
    ToHirCOpt2  child = sureChild();
    Exp e1 = child.visitExp(e.getExp1());
    if( e1.getOperator()==HIR.OP_CONST )
    {
      if( e1.getConstSym().doubleValue()==0 )
      {
        // false --> side effect of e1,e3
        if (fDbgLevel > 3) //##67
          message(4,"false?e2:e3 --> e3: "+ToC.tos(e));
        buffer.addPrev(child.buffer);
        return visitExp((Exp)e.getChild(3));
      }
      else
      {
        // true --> side effect of e1,e2
        if (fDbgLevel > 3) //##67
          message(4,"true?e2:e3 --> e2: "+ToC.tos(e));
        buffer.addPrev(child.buffer);
        return visitExp(e.getExp2());
      }
    }
    e.setChild1( child.buffer.toExp(e1) );
    e.setChild2( child.buffer.toExp(child.visitExp( e.getExp2() )) );
    e.setChild(3,child.buffer.toExp(child.visitExp( (Exp)e.getChild(3) )) );
    return e;
  }
  //-------------------------------------------------------------------
  // PRIVATE
  //-------------------------------------------------------------------
  /**
   * Return true if e is constant 0.
   *
   * @param   e Exp
   * @return  True if e is constant 0.
  **/
  private boolean is0(Exp e)
  {
    return e.getOperator()==HIR.OP_CONST
        && toHir.isArithmetic(e.getType())
        && e.getConstSym().doubleValue()==0;
  }
  //-------------------------------------------------------------------
  /**
   * Return true if e is constant 1.
   *
   * @param   e Exp
   * @return  True if e is constant 1.
  **/
  private boolean is1(Exp e)
  {
    return e.getOperator()==HIR.OP_CONST
        && toHir.isArithmetic(e.getType())
        && e.getConstSym().doubleValue()==1;
  }
  //-------------------------------------------------------------------
  /**
   * Return true if accuracy of the type of e1 is completely masked with c2.
   *
   * @param   e1 Exp
   * @param   c2 Const
   * @return  True if The type of e1 is completely masked with c2.
  **/
  private boolean masksPerfectly(Exp e1,Const c2)
  {
    if( toHir.isIntegral(e1.getType())
    &&  toHir.isIntegral(c2.getSymType()) )
    {
      long max = ~( (-1L)<<(e1.getType().getSizeValue()*8) );
      long value = c2.longValue();
      return (max|value)==value;
    }
    return false;
  }
  //-------------------------------------------------------------------
  /**
   * Find constant symbol other than character string.
   * If not found, return null.
   *
   * @param   e Expression.
   * @return  Constant symbol.
  **/
  private Const findConstantSym(Exp e)
  {
    for(;;)
      switch( e.getOperator() )
      {
      case HIR.OP_CONV:
        e = e.getExp1();
        continue;
      case HIR.OP_CONST:
        Const c = (Const)((ConstNode)e).getSym();
        if( c.getSymKind()!=Sym.KIND_STRING_CONST )
          return c;
      default:
        return null;
      }
  }
  //-------------------------------------------------------------------
  /**
   * Find symbol whose address is taken.
   * It may be a character string. If not found, return null.
   *
   * @param   e Expression.
   * @return  Address taken symbol.
  **/
  private Sym findAddressedSym(Exp e)
  {
    for(;;)
      switch( e.getOperator() )
      {
      case HIR.OP_CONV:
        e = e.getExp1();
        if( e.getType().getSizeValue()<toHir.typeVoidPtr.getSizeValue() )
          return null;
        continue;
      case HIR.OP_ADDR:
      case HIR.OP_DECAY:
        switch( e.getExp1().getOperator() )
        {
        case HIR.OP_VAR:
        //case HIR.OP_PARAM:
        case HIR.OP_SUBP:
          return ((SymNode)e.getExp1()).getSym();
        default:
          return null;
        }
      case HIR.OP_CONST:
        Const c = (Const)((ConstNode)e).getSym();
        if( c.getSymKind()==Sym.KIND_STRING_CONST )
          return c;
      default:
        return null;
      }
  }
  //-------------------------------------------------------------------
  /**
   * Get compare relation between expressions e1 and e2.
   * If both expressions are constant, then return EQUAL or SAMLL or BIG,
   * else return CANNOT. The comparison is assumed to do type conversion
   * of C language if required.
   *
   * @param   e1 Expression.
   * @param   e2 Expression.
   * @return  EQUAL or SAMLL or BIG or CANNOT.
  **/
  private int getCompareRelation(Exp e1,Exp e2)
  {
    if( e1.getOperator()==HIR.OP_CONST )
      if( e2.getOperator()==HIR.OP_CONST )
        return getCompareRelationOfConst( (Const)e1.getSym(), (Const)e2.getSym() );
      else
        return getCompareRelationOfType( (Const)e1.getSym(), e2.getType() );
    else if( e2.getOperator()==HIR.OP_CONST )
      return -getCompareRelationOfType( (Const)e2.getSym(), e1.getType() );
    else
      return CANNOT;
  }
  //-------------------------------------------------------------------
  /**
   * Get compare relation between constant expressions c1 and c2.
   * If both expressions are scalar, then return EQUAL or SAMLL or BIG,
   * else return CANNOT.
   *
   * @param   e1 Constant expression.
   * @param   e2 Constant expression.
   * @return  EQUAL or SAMLL or BIG or CANNOT.
  **/
  private int getCompareRelationOfConst(Const c1,Const c2)
  {
    if( toHir.isScalar(c1.getSymType())
    &&  toHir.isScalar(c2.getSymType()) )
    {
      double d1 = c1.doubleValue();
      double d2 = c2.doubleValue();
      if (fDbgLevel > 3) //##67
        message(6,"getCompareRelationOfConst D1="+d1+" D2="+d2);
      return d1==d2 ? EQUAL : d1<d2 ? SMALL : BIG;
    }
    else
      return CANNOT;
  }
  //-------------------------------------------------------------------
  /**
   * Get compare relation between constant expression c1
   * and maximum value representable by the type t2.
   *
   * @param   e1 Constant expression.
   * @param   t2 Type expression.
   * @return  MIN or MAX or SAMLL or BIG or CANNOT.
  **/
  private int getCompareRelationOfType(Const c1,Type t2)
  {
    Type t1 = c1.getSymType();
    Type t  = toCast.getCompareType(t1,t2);
    if( toHir.isIntegral(t) )
      if( t.isUnsigned() )
      {
        double d1 = c1.longValue();
        double max = java.lang.Math.pow( 256, t2.getSizeValue() )-1;
        if (fDbgLevel > 3) //##67
          message(6,"getCompareRelationOfType UNSIGNED D1="+d1+" MAX="+max);
        if( d1<0 )
          d1 = d1-Long.MIN_VALUE-Long.MIN_VALUE;

        if( d1==0 )
          return MIN;
        if( d1<max )
          return CANNOT;
        if( d1==max )
          return MAX;
        else
          return BIG;
      }
      else
      {
        double d1 = c1.longValue();
        double min = -java.lang.Math.pow( 256, t2.getSizeValue() )/2;
        double max = java.lang.Math.pow( 256, t2.getSizeValue() )/2-1;
        if (fDbgLevel > 3) //##67
          message(6,"getCompareRelationOfType SIGNED D1="+d1+" MIN="+min+" MAX="+max);
        if( d1<min )
          return SMALL;
        if( d1==min )
          return MIN;
        if( d1<max )
          return CANNOT;
        if( d1==max )
          return MAX;
        return BIG;
      }
    else
      return CANNOT;
  }
  //-------------------------------------------------------------------
  /**
   * Sweep out the side effect from a always true conditional expression.
   *
   * @param   op Operator of the expression.
   * @param   e Always true expression.
   * @return  True expression.
  **/
  private Exp alwaysTrue(String op,Exp e)
  {
    //toHir.warning("comparision "+op+" is always true.");
    if (fDbgLevel > 3) //##67
      message(4,op+" --> true: "+ToC.tos(e));
    cutter.visitExp(e); // Sweep out side effects.
    return toHir.newTrueNode();
  }
  //-------------------------------------------------------------------
  /**
   * Sweep out the side effect from a always false conditional expression.
   *
   * @param   op Operator of the expression.
   * @param   e Always false expression.
   * @return  False expression.
  **/
  private Exp alwaysFalse(String op,Exp e)
  {
    //toHir.warning("comparision "+op+" is always false.");
    if (fDbgLevel > 3) //##67
      message(4,op+" --> false: "+ToC.tos(e));
    cutter.visitExp(e); // Sweep out side effects.
    return toHir.newFalseNode();
  }
  //-------------------------------------------------------------------
}
