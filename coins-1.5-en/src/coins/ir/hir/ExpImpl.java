/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import java.util.Iterator;

import coins.HirRoot;
import coins.ir.IrList;
import coins.sym.Const;
import coins.sym.Elem;
import coins.sym.Label;
import coins.sym.PointerType;
import coins.sym.Subp;
import coins.sym.SubpType;
import coins.sym.Sym;
import coins.sym.SymTable;
import coins.sym.Type;
import coins.sym.Var;
import coins.sym.VectorType;
import coins.sym.VectorTypeImpl;

//========================================//

/** ExpImpl
 *  HIR expression class.
 *  The expression class Exp is a subclass of HIR.
**/
public class
ExpImpl extends HIR_Impl implements Exp
{

//====== Constructors ======//

public
ExpImpl( HirRoot pHirRoot )
{
  super(pHirRoot, HIR.OP_NULL);
  fType = hirRoot.symRoot.typeVoid;
  setFlag(HIR.FLAG_CONST_EXP, true);
}

public
ExpImpl( HirRoot pHirRoot, int pOperator )
{
  super(pHirRoot, pOperator);
  fType = hirRoot.symRoot.typeVoid;
  if (pOperator == HIR.OP_NULL) {
    setFlag(HIR.FLAG_CONST_EXP, true);
  }
}

/**
 * Build an instance of unary expression.
 *  The type of resultant expression is set according to the rule
 *  described in the HIR specifications, however, it may be necessary
 *  to do language-wise treatment. In such case, front-end part of the
 *  language should set type after creating the instance of Exp.
 *  (In C, expressions related to pointer and vector are specially
 *   treated in C front end ToHirC.)
 * @param pHirRoot Specify the current instance of HirRoot.
 * @param pOperator Operator of the unary expression.
 * @param pExp1 Operand expression.
 */
public
ExpImpl( HirRoot pHirRoot, int pOperator, Exp pExp1 )
{
  super(pHirRoot, pOperator);
  fChildNode1 = pExp1;
  if (fChildNode1 != null)
    ((HIR_Impl)fChildNode1).fParentNode = this;
  fChildCount = 1;
  switch (pOperator) {
  case HIR.OP_ADDR:
    fType = hirRoot.sym.pointerType(pExp1.getType());
    if ((pExp1 instanceof VarNode)||
        (pExp1 instanceof SubpNode)) {
      setFlag(HIR.FLAG_CONST_EXP, true);
    }
    break;
  case HIR.OP_SIZEOF:
    fType = hirRoot.symRoot.typeInt;
    if (pExp1 instanceof VarNode) {
      setFlag(HIR.FLAG_CONST_EXP, true);
    }
    break;
  case HIR.OP_CONTENTS:fType = pExp1.getType().getPointedType(); break;
  case HIR.OP_OFFSET:  fType = hirRoot.symRoot.typeOffset; break;
  case HIR.OP_DECAY:
    if (pExp1.getType().getTypeKind() == Type.KIND_VECTOR)
      fType = hirRoot.sym.pointerType(
              ((VectorTypeImpl)pExp1.getType()).getElemType());
    else
      fType = hirRoot.sym.pointerType(pExp1.getType());
    break;
  default:
    fType = pExp1.getType();
  }
  if (pExp1.getFlag(HIR.FLAG_CONST_EXP))
    setFlag(HIR.FLAG_CONST_EXP, true);
} // ExpImpl 1
/**
 * Build an instance of binary expression.
 *  The type of resultant expression is set according to the rule
 *  described in the HIR specifications, however, it may be necessary
 *  to do language-wise treatment. In such case, front-end part of the
 *  language should set type after creating the instance of Exp.
 *  (In C, expressions related to pointer and vector are specially
 *   treated in C front end ToHirC.)
 * @param pHirRoot Specify the current instance of HirRoot.
 * @param pOperator Operator of the binary expression.
 * @param pExp1 Operand 1 expression.
 * @param pExp1 Operand 2 expression.
 */
public
ExpImpl( HirRoot pHirRoot, int pOperator, Exp pExp1, Exp pExp2 )
{
  super(pHirRoot, pOperator);
  fChildNode1 = pExp1;
  fChildNode2 = pExp2;
  if (fChildNode1 != null)
    ((HIR_Impl)fChildNode1).fParentNode = this;
  if (fChildNode2 != null)
    ((HIR_Impl)fChildNode2).fParentNode = this;
  fChildCount = 2;
  switch (pOperator) {
  case  OP_SUBS:
    fType = ((VectorTypeImpl)pExp1.getType()).getElemType();
    break;
  case OP_QUAL:
  case OP_ARROW:
  case OP_COMMA: // S.Fukuda 2002.10.30
    fType = pExp2.getType();
    break;
  //case OP_INDEX: //SF040525
  //  fType = hirRoot.symRoot.typeOffset; //SF040525
  //  break; //SF040525
  case OP_CMP_EQ:
  case OP_CMP_NE:
  case OP_CMP_GT:
  case OP_CMP_GE:
  case OP_CMP_LT:
  case OP_CMP_LE:
    fType = hirRoot.symRoot.typeBool;
    break;
  case OP_OFFSET:
    fType = hirRoot.symRoot.typeOffset;
    break;
  default:
    fType = pExp1.getType();
  }
  if ((pExp1.getFlag(HIR.FLAG_CONST_EXP))&&
      (pExp2.getFlag(HIR.FLAG_CONST_EXP)))
    setFlag(HIR.FLAG_CONST_EXP, true);
} // ExpImpl 2

public
ExpImpl( HirRoot pHirRoot, int pOperator, Exp pExp1, Exp pExp2, Exp pExp3 )
{
  super(pHirRoot, pOperator);
  fAdditionalChild = new HIR[1];
  fChildCount = 3;
  fChildNode1 = pExp1;
  fChildNode2 = pExp2;
  fAdditionalChild[0] = pExp3;
  if (fChildNode1 != null)
    ((HIR_Impl)fChildNode1).fParentNode = this;
  if (fChildNode2 != null)
    ((HIR_Impl)fChildNode2).fParentNode = this;
  if (pExp3 != null)
    ((HIR_Impl)fAdditionalChild[0]).fParentNode = this;
  fType = pExp2.getType();
} // ExpImpl 3

/** getConstSym
 *  Get constant symbol attached to this node.
 *  "this" should be a constant node.
 *  @return constant symbol attached to this node.
 **/
public Const getConstSym() {
    return null;
}

/** getSym
 *  Get symbol from SymNode.
 *  "this" should be a SymNode
 *  (either VarNode, SubpNode, LabelNode, ElemNode, or FieldNode). (##2)
 *  @return the symbol attached to the node
 *    (either Var, Subp, Label, Elem, or Field).
 **/
public Sym
getSym()  {
  if ((fOperator >= HIR.OP_CONST)&&
      (fOperator <= HIR.OP_ELEM))
    return ((SymNode)this).getSymNodeSym();
  else return null;
} // getSym

public Var   getVar()   { return null; }
public Subp  getSubp()  { return null; }
public Label getLabel() { return null; }
public Elem  getElem()  { return null; }

/** getExp1
 *  Get source operand 1 from unary or binary expression.
 *  "this" should be either unary or binary expression.
 *  @return the source operand 1 expression of this node.
 **/
public Exp
getExp1() {
  return (Exp)fChildNode1;
}

/** getExp2
 *  Get source operand 2 from binary expression.
 *  "this" should be a binary expression.
 *  @return the source operand 2 expression of this node.
 **/
public Exp
getExp2() {
  return (Exp)fChildNode2;
}

public Exp
getArrayExp() { return null; }       // SubscriptedExp only

public Exp
getSubscriptExp() { return null; }   // SubscriptedExp only

public Exp
getElemSizeExp() { return null; }   // SubscriptedExp only

public Exp
getPointerExp() { return null; }   // return the pointer expression part (pPointer).

public Elem
getPointedElem() { return null; }  // return the pointed element part    (pElem).

public Exp
getQualifierExp() { return null; }    // return qualifier part (pQualifier).

public Elem
getQualifiedElem() { return null; } // return qualified element part (pelement).

/** getSubpSpec                                           (##2)
 *  getActualParamList
 *  Get a component expression of the function expression. (##2)
 *  "this" should be a node built by functionExp.
 *  getSubpSpec        return the expression specifying the subprogram
 *                      to be called (pSubpSpec).              (##2)
 *  getActualParamList return the actual parameter list (pParamList).
 *                      If this has no parameter, then return null.
 **/
public Exp
getSubpSpec() {
  if (getOperator() == HIR.OP_CALL)
    return ((FunctionExp)this).getFunctionSpec();
  else
    return null;
}

public IrList
getActualParamList() {
  if (getOperator() == HIR.OP_CALL)
    return ((FunctionExp)this).getParamList();
  else
    return null;
}

/** findSubpType
 *  Find SubpType represented by this expression.
 *  If this is SubpNode then return SubpType pointed by this node type,
 *  else decompose this expression to find Subpnode.
 *  If illegal type is encountered, return null.
**/
public SubpType
findSubpType()
{
  Type lSubpType;
  Exp  lSubpExp;
  if (fDbgLevel > 0) //##58
    hirRoot.ioRoot.dbgHir.print(4, "findSubpType", toString());
  switch (getOperator()) {
  case HIR.OP_SUBP:
  case HIR.OP_VAR:
  case HIR.OP_PARAM:
  case HIR.OP_ELEM:
    lSubpType =  getType();
    if (lSubpType.getTypeKind() == Type.KIND_POINTER) {
      lSubpType = ((PointerType)lSubpType).getPointedType();
      if (lSubpType.getTypeKind() == Type.KIND_SUBP) {
        if (fDbgLevel > 0) //##58
          hirRoot.ioRoot.dbgHir.print(4, "findSubpType return",
                lSubpType.toString());
        return (SubpType)lSubpType;
      }
    }
    hirRoot.ioRoot.msgRecovered.put(1020,
         "Function pointer is requested for " + this.toString());
    return null;
  case  OP_SUBS:
    lSubpExp = getExp1();
    break;
  case OP_QUAL:
  case OP_ARROW:
    lSubpExp = getExp2();
    break;
  default:
    lSubpExp = getExp1();
    break;
  }
  if (lSubpExp != null) {
    return lSubpExp.findSubpType();
  }else {
    hirRoot.ioRoot.msgRecovered.put(1020,
         "Function pointer is requested for " + this.toString());
    return null;
  }
} // findSubpType

/** isEvaluable:
 *  See if "this" expression can be currently evaluated or not.
 *  @return true if this expression can be evaluated as constant value
 *      at the invocation of this method, false otherwise.
**/
public boolean
isEvaluable()
{
  if (fDbgLevel > 0) //##58
    hirRoot.ioRoot.dbgHir.print(4, "  isEvaluable " , this.toString());
  if (this instanceof ConstNode) {
    return true;
  }else if (getFlag(HIR.FLAG_CONST_EXP)) {
    if (this.getOperator() == OP_ADDR)
      return false;
    if (this.getOperator() == OP_NULL)
      return false;
    if (this instanceof VarNode)
      return false;
    else { // Neither ConstNode nore VarNode.
      for (int lChildC = 1; lChildC <= getChildCount(); lChildC++) {
        HIR lChild = (HIR)getChild(lChildC);
        if (! (lChild instanceof Exp)||
            (! ((Exp)lChild).isEvaluable()))
          return false;
      }
      if (fDbgLevel > 0) //##58
        hirRoot.ioRoot.dbgHir.print(4, " result true ");
      return true; // All children are evaluable.
    }
  }
  else { // FLAG_CONST_EXP is false.
    if ((this instanceof VarNode)||
       (this.getOperator() == OP_ADDR)||
       (this.getOperator() == OP_NULL))
      return false;
    for (int lChildC = 1; lChildC <= getChildCount(); lChildC++) {
      HIR lChild = (HIR)getChild(lChildC);
      if (! (lChild instanceof Exp)||
          (! ((Exp)lChild).isEvaluable()))
        return false;
    }
    if (fDbgLevel > 0) //##58
      hirRoot.ioRoot.dbgHir.print(4, " result true ");
    return true; // All children are evaluable.
  }
} // isEvaluable

//SF050111[
///** evaluate:
// *  Evaluate "this" expression.
// *  "this" should be an expression with getFlag(HIR.FLAG_CONST_EXP) true.
// *  If not, this is not evaluable and null is returned.
// *  @return constant node as the result of evaluation.
//**/
//public ConstNode
//evaluate()
//{
//  if (this.isEvaluable()) {
//    Exp lExp = (Exp)this.copyWithOperands();
//    if (lExp instanceof ConstNode)
//      return (ConstNode)lExp;
//    FlowRoot lFlowRoot = hirRoot.getFlowRoot();
//    if (lFlowRoot == null)
//      lFlowRoot = new FlowRoot(hirRoot);
//    HIR lEvaluatedExp = null;
//    switch (lExp.getOperator()) {
//      case OP_CONV:
//      case OP_NOT:
//      case OP_NEG:
//      case OP_SIZEOF:
//        lEvaluatedExp = coins.opt.ConstFoldingHir.foldUnary(lExp, lFlowRoot);
//        break;
//      case OP_ADD:
//      case OP_SUB:
//      case OP_MULT:
//      case OP_DIV:
//      case OP_MOD:
//      case OP_AND:
//      case OP_OR:
//      case OP_XOR:
//      case OP_CMP_EQ:
//      case OP_CMP_NE:
//      case OP_CMP_GT:
//      case OP_CMP_GE:
//      case OP_CMP_LT:
//      case OP_CMP_LE:
//      case OP_SHIFT_LL:
//      case OP_SHIFT_R:
//      case OP_SHIFT_RL:
//        lEvaluatedExp = coins.opt.ConstFoldingHir.foldBinary(lExp, lFlowRoot);
//      default: break;
//    }
//    if (lEvaluatedExp != null)
//      if (fDbgLevel > 0) //##58
//        hirRoot.ioRoot.dbgHir.print(2, " evaluate result ",
//            lEvaluatedExp.toStringWithChildren());
//    if (lEvaluatedExp instanceof ConstNode)
//      return (ConstNode)lEvaluatedExp;
//  }
//  return null;
//} // evaluate
//
//public int
//evaluateAsInt()
//{
//  ConstNode lConstValue = evaluate();
//  if ((lConstValue != null)&&(lConstValue instanceof ConstNode))
//    return lConstValue.getIntValue();
//  else
//    return 0;
//}
//
//public float
//evaluateAsFloat()
//{
//  ConstNode lConstValue = evaluate();
//  if ((lConstValue != null)&&(lConstValue instanceof ConstNode))
//    return lConstValue.getConstSym().floatValue();
//  else
//    return 0.0f;
//}
//
//public double
//evaluateAsDouble() {
//  ConstNode lConstValue = evaluate();
//  if ((lConstValue != null)&&(lConstValue instanceof ConstNode))
//    return lConstValue.getConstSym().doubleValue();
//  else
//    return 0.0;
//}

/**
 * Evaluate "this" expression.
 * @return  constant as the result of evaluation or null(when failing in the
 *          evaluation)
**/
public Const evaluate()
{
  Const c1;
  Const c2;
  Boolean b1;
  Boolean b2;
  int r;

  //##81 BEGIN
  Exp lExp1 = null, lExp2 = null;
  Const lEvalExp1 = null, lEvalExp2 = null;
  Type lType1 = null, lType2 = null;
  Type lResultType, lConvType;
  lResultType = getType();
  if (lResultType.getTypeRank() < hirRoot.symRoot.typeInt.getTypeRank()) {
    // Integral promotion.
    lResultType = hirRoot.symRoot.typeInt;
  }
  //---- Adjust operand types.
  //     (See adjustTypesOfBinaryOperands)
  if (fChildNode1 instanceof Exp) {
    lExp1 = (Exp)fChildNode1;
    lType1 = lExp1.getType();
  }
  if (fChildNode2 instanceof Exp) {
    lExp2 = (Exp)fChildNode2;
    lType2 = lExp2.getType();
  }
  if (fDbgLevel > 3)
    hirRoot.ioRoot.dbgHir.print(6, "evaluate", toString());
  int lOperator = getOperator();
  switch( lOperator ) {
  case HIR.OP_CONST:
  case HIR.OP_VAR:
  case HIR.OP_NEG:
  case HIR.OP_NOT:
  case HIR.OP_SHIFT_LL:
  case HIR.OP_SHIFT_R:
  case HIR.OP_SHIFT_RL:
  case HIR.OP_CONV:
  case HIR.OP_SELECT:
  case HIR.OP_COMMA:
    break;

  // Binary expression
  case HIR.OP_ADD:
  case HIR.OP_SUB:
  case HIR.OP_MULT:
  case HIR.OP_DIV:
  case HIR.OP_MOD:
  case HIR.OP_AND:
  case HIR.OP_OR:
  case HIR.OP_XOR:
  case HIR.OP_LG_AND:
  case HIR.OP_LG_OR:
  case HIR.OP_CMP_EQ:
  case HIR.OP_CMP_NE:
  case HIR.OP_CMP_GT:
  case HIR.OP_CMP_GE:
  case HIR.OP_CMP_LT:
  case HIR.OP_CMP_LE:
    // See ISO/IEC 9899-1999 Programming language C section 6.3.1.8.
    if (lType1 != lType2) {
      int lResultRank = lResultType.getTypeRank();
      int lRank1 = lType1.getTypeRank();
      int lRank2 = lType2.getTypeRank();
    if (lType1.isFloating() && lType2.isFloating()) {
      if (lRank1 > lRank2)
        lExp2 = lExp2.convExp(lType1, lExp2);
      else if (lRank2 > lRank1)
        lExp1 = lExp1.convExp(lType2, lExp1);
    }else if (lType1.isFloating() && lType2.isInteger()) {
      lExp2 = lExp2.convExp(lType1, lExp2);
    }else if (lType2.isFloating() && lType1.isInteger()) {
      lExp1 = lExp1.convExp(lType2, lExp1);
    }else if ((lType1.isUnsigned() && lType2.isUnsigned())||
          ((! lType1.isUnsigned())&&(! lType2.isUnsigned()))) {
        if ((lRank1 > 0)&&(lRank2 > 0)) { //##85
          // Not pointer, void nor aggregate. //##85
          if (lRank1 > lRank2) {
            lExp2 = lExp2.convExp(lType1, lExp2);
          }
          else if (lRank2 > lRank1) {
            lExp1 = lExp1.convExp(lType2, lExp1);
          }
        } //##85
      }else if (lType1.isUnsigned() &&
                (lType1.getSizeValue() >= lType2.getSizeValue())) {
        lExp2 = lExp2.convExp(lType1, lExp2);
      }else if (lType2.isUnsigned() &&
                (lType2.getSizeValue() >= lType1.getSizeValue())) {
        lExp1 = lExp1.convExp(lType2, lExp1);
      }else if ((! lType1.isUnsigned())&&
                (lType1.getSizeValue() > lType2.getSizeValue())) {
        lExp2 = lExp2.convExp(lType1, lExp2);
      }else if ((! lType2.isUnsigned())&&
                (lType2.getSizeValue() > lType1.getSizeValue())) {
        lExp1 = lExp1.convExp(lType2, lExp1);
      }else {
        lConvType = hirRoot.symRoot.typeU_Char;
        if ((lConvType.getSizeValue() < lType1.getSizeValue())||
            (lConvType.getSizeValue() < lType2.getSizeValue()))
          lConvType = hirRoot.symRoot.typeU_Short;
        if ((lConvType.getSizeValue() < lType1.getSizeValue())||
            (lConvType.getSizeValue() < lType2.getSizeValue()))
         lConvType = hirRoot.symRoot.typeU_Int;
       if ((lConvType.getSizeValue() < lType1.getSizeValue())||
           (lConvType.getSizeValue() < lType2.getSizeValue()))
         lConvType = hirRoot.symRoot.typeU_Long;
       if ((lConvType.getSizeValue() < lType1.getSizeValue())||
           (lConvType.getSizeValue() < lType2.getSizeValue()))
         lConvType = hirRoot.symRoot.typeU_LongLong;
       if (lRank1 < lConvType.getTypeRank()) {
         lExp1 = lExp1.convExp(lConvType, lExp1);
       }
       if (lRank2 < lConvType.getTypeRank()) {
         lExp2 = lExp2.convExp(lConvType, lExp2);
       }
       //---- Evaluate
       if ((lExp1.getType() != lResultType)||
           (lExp2.getType() != lResultType)) {
         Const lEval1 = (Const)lExp1.evaluate();
         Const lEval2 = (Const)lExp2.evaluate();
         if ((lEval1 != null)&&(lEval2 != null)) {
           Exp lTempExp = hirRoot.hir.exp(lOperator,
                     hirRoot.hir.constNode(lEval1),
                     hirRoot.hir.constNode(lEval2));
           Exp lEvalExp = hirRoot.hir.convExp(lResultType, lTempExp);
           Const lEvalResult = lEvalExp.evaluate();
           if (fDbgLevel > 3)
             hirRoot.ioRoot.dbgHir.print(6, " convConst " + lExp1 + " " + lExp2
                + " to " + lEvalResult);
           return lEvalResult;
         }
       }
     }
    } // end of if(lType1 != lType2)
    break;
  default:
  } // switch end
  if ((lOperator != HIR.OP_CONST)&&(lOperator != HIR.OP_VAR)&&
      (lOperator != HIR.OP_COMMA)&&(lOperator != HIR.OP_SELECT)) {
    if (lExp1 != null)
      lEvalExp1 = (Const)lExp1.evaluate();
    if (lExp2 != null)
      lEvalExp2 = (Const)lExp2.evaluate();
  }
  if (fDbgLevel > 3)
    hirRoot.ioRoot.dbgHir.print(6, " eval " + lEvalExp1 + " " + lEvalExp2);
  //##81 END
  //##81 switch( getOperator() )
  switch (lOperator) //##81
  {
  case HIR.OP_CONST:
    return getConstSym();

  case HIR.OP_VAR:
    if( getVar().getSymType().isConst()
    && !getVar().getSymType().isVolatile() )
      return getVar().getInitialValue().evaluate();
    return null;

  case HIR.OP_NEG:
    //##81 if( (c1=getExp1().evaluate())!=null )
    //##81   return hirRoot.sourceLanguage.evaluateNeg(c1);
    if (lEvalExp1 != null) { //##81
      return hirRoot.sourceLanguage.evaluateNeg(lEvalExp1); //##81
    }
    return null;

  case HIR.OP_ADD:
    //##81 if( (c1=getExp1().evaluate())!=null
    //##81 &&  (c2=getExp2().evaluate())!=null )
    //##81   return hirRoot.sourceLanguage.evaluateAdd(c1,c2);
    if ((lEvalExp1 != null)&&(lEvalExp2 != null)) //##81
      return hirRoot.sourceLanguage.evaluateAdd(lEvalExp1, lEvalExp2); //##81
    return null;

  case HIR.OP_SUB:
    //##81 if( (c1=getExp1().evaluate())!=null
    //##81 &&  (c2=getExp2().evaluate())!=null )
    //##81   return hirRoot.sourceLanguage.evaluateSub(c1,c2);
    if ((lEvalExp1 != null)&&(lEvalExp2 != null)) //##81
      return hirRoot.sourceLanguage.evaluateSub(lEvalExp1, lEvalExp2); //##81
    return null;

  case HIR.OP_MULT:
    //##81 if( (c1=getExp1().evaluate())!=null
    //##81 &&  (c2=getExp2().evaluate())!=null )
    //##81   return hirRoot.sourceLanguage.evaluateMult(c1,c2);
    if ((lEvalExp1 != null)&&(lEvalExp2 != null)) //##81
      return hirRoot.sourceLanguage.evaluateMult(lEvalExp1, lEvalExp2); //##81
    return null;

  case HIR.OP_DIV:
    //##81 if( (c1=getExp1().evaluate())!=null
    //##81 &&  (c2=getExp2().evaluate())!=null )
    //##81   return hirRoot.sourceLanguage.evaluateDiv(c1,c2);
    if ((lEvalExp1 != null)&&(lEvalExp2 != null)) //##81
      return hirRoot.sourceLanguage.evaluateDiv(lEvalExp1, lEvalExp2); //##81
    return null;

  case HIR.OP_MOD:
    //##81 if( (c1=getExp1().evaluate())!=null
    //##81 &&  (c2=getExp2().evaluate())!=null )
    //##81   return hirRoot.sourceLanguage.evaluateMod(c1,c2);
    if ((lEvalExp1 != null)&&(lEvalExp2 != null)) //##81
      return hirRoot.sourceLanguage.evaluateMod(lEvalExp1, lEvalExp2); //##81
    return null;

  case HIR.OP_NOT:
    //##81 if( (c1=getExp1().evaluate())!=null )
    //##81   return hirRoot.sourceLanguage.evaluateNot(c1);
    if (lEvalExp1 != null) //##81
      return hirRoot.sourceLanguage.evaluateNot(lEvalExp1); //##81
    return null;

  case HIR.OP_SHIFT_LL:
    //##81 if( (c1=getExp1().evaluate())!=null
    //##81 &&  (c2=getExp2().evaluate())!=null )
    //##81   return hirRoot.sourceLanguage.evaluateShiftLl(c1,c2);
    if ((lEvalExp1 != null)&&(lEvalExp2 != null)) //##81
      return hirRoot.sourceLanguage.evaluateShiftLl(lEvalExp1, lEvalExp2); //##81
    return null;

  case HIR.OP_SHIFT_R:
    //##81 if( (c1=getExp1().evaluate())!=null
    //##81 &&  (c2=getExp2().evaluate())!=null )
    //##81   return hirRoot.sourceLanguage.evaluateShiftRa(c1,c2);
    if ((lEvalExp1 != null)&&(lEvalExp2 != null)) //##81
      return hirRoot.sourceLanguage.evaluateShiftRa(lEvalExp1, lEvalExp2); //##81
    return null;

  case HIR.OP_SHIFT_RL:
    //##81 if( (c1=getExp1().evaluate())!=null
    //##81 &&  (c2=getExp2().evaluate())!=null )
    //##81   return hirRoot.sourceLanguage.evaluateShiftRl(c1,c2);
    if ((lEvalExp1 != null)&&(lEvalExp2 != null)) //##81
      return hirRoot.sourceLanguage.evaluateShiftRl(lEvalExp1, lEvalExp2); //##81
    return null;

  case HIR.OP_AND:
    //##81 if( (c1=getExp1().evaluate())!=null
    //##81 &&  (c2=getExp2().evaluate())!=null )
    //##81   return hirRoot.sourceLanguage.evaluateAnd(c1,c2);
    if ((lEvalExp1 != null)&&(lEvalExp2 != null)) //##81
      return hirRoot.sourceLanguage.evaluateAnd(lEvalExp1, lEvalExp2); //##81
    return null;

  case HIR.OP_OR:
    //##81 if( (c1=getExp1().evaluate())!=null
    //##81 &&  (c2=getExp2().evaluate())!=null )
    //##81   return hirRoot.sourceLanguage.evaluateOr(c1,c2);
    if ((lEvalExp1 != null)&&(lEvalExp2 != null)) //##81
      return hirRoot.sourceLanguage.evaluateOr(lEvalExp1, lEvalExp2); //##81
    return null;

  case HIR.OP_XOR:
    //##81 if( (c1=getExp1().evaluate())!=null
    //##81 &&  (c2=getExp2().evaluate())!=null )
    //##81   return hirRoot.sourceLanguage.evaluateXor(c1,c2);
    if ((lEvalExp1 != null)&&(lEvalExp2 != null)) //##81
      return hirRoot.sourceLanguage.evaluateXor(lEvalExp1, lEvalExp2); //##81
    return null;

  case HIR.OP_CONV:
    //##81 if( (c1=getExp1().evaluate())!=null
    if ((lEvalExp1 != null) //##81
    &&  getType()!=null )
    //##81   return hirRoot.sourceLanguage.evaluateCast(getType(),c1);
      return hirRoot.sourceLanguage.evaluateCast(getType(), lEvalExp1); //##81
    return null;

  case HIR.OP_SELECT:
    if( (c1=getExp1().evaluate())!=null )
      switch( hirRoot.sourceLanguage.evaluateCondition(c1) )
      {
      case 1: return getExp2().evaluate();
      case 0: return ((Exp)getChild(3)).evaluate();
      }
    return null;

  case HIR.OP_COMMA:
    return getExp2().evaluate();

  case HIR.OP_LG_AND:
    //##81 if( (c1=getExp1().evaluate())!=null )
    //##81   switch( hirRoot.sourceLanguage.evaluateCondition(c1) )
    if (lEvalExp1 != null) //##81
      switch( hirRoot.sourceLanguage.evaluateCondition(lEvalExp1) ) //##81
      {
      case 0: return hirRoot.symRoot.boolConstFalse;
      case 1:
        //##81 if( (c2=getExp2().evaluate())!=null )
        //##81 switch( hirRoot.sourceLanguage.evaluateCondition(c2) )
        if( lEvalExp2 != null ) //##81
          switch( hirRoot.sourceLanguage.evaluateCondition(lEvalExp2) ) //##81
          {
            case 0: return hirRoot.symRoot.boolConstFalse;
            case 1: return hirRoot.symRoot.boolConstTrue;
          }
      }
    return null;

  case HIR.OP_LG_OR:
    //##81 if( (c1=getExp1().evaluate())!=null )
    //##81   switch( hirRoot.sourceLanguage.evaluateCondition(c1) )
    if (lEvalExp1 != null) //##81
      switch( hirRoot.sourceLanguage.evaluateCondition(lEvalExp1) ) //##81
      {
      case 1: return hirRoot.symRoot.boolConstTrue;
      case 0:
        //##81 if( (c2=getExp2().evaluate())!=null )
        //##81 switch( hirRoot.sourceLanguage.evaluateCondition(c2) )
        if( lEvalExp2 != null ) //##81
          switch( hirRoot.sourceLanguage.evaluateCondition(lEvalExp2) ) //##81
          {
            case 1: return hirRoot.symRoot.boolConstTrue;
            case 0: return hirRoot.symRoot.boolConstFalse;
          }
      }
    return null;

  case HIR.OP_CMP_EQ:
    //##81 if( (c1=getExp1().evaluate())!=null
    //##81 &&  (c2=getExp2().evaluate())!=null )
    //##81   switch( hirRoot.sourceLanguage.evaluateRelation(c1,c2) )
    if ((lEvalExp1 != null)&&(lEvalExp2 != null)) //##81
      switch (hirRoot.sourceLanguage.evaluateRelation(lEvalExp1, lEvalExp2)) //##81
      {
      case 0: return hirRoot.symRoot.boolConstTrue;
      case 1:
      case-1: return hirRoot.symRoot.boolConstFalse;
      }
    return null;

  case HIR.OP_CMP_NE:
    //##81 if( (c1=getExp1().evaluate())!=null
    //##81 &&  (c2=getExp2().evaluate())!=null )
    //##81   switch( hirRoot.sourceLanguage.evaluateRelation(c1,c2) )
    if ((lEvalExp1 != null)&&(lEvalExp2 != null)) //##81
      switch (hirRoot.sourceLanguage.evaluateRelation(lEvalExp1, lEvalExp2)) //##81
      {
      case 0: return hirRoot.symRoot.boolConstFalse;
      case 1:
      case-1: return hirRoot.symRoot.boolConstTrue;
      }
    return null;

  case HIR.OP_CMP_GT:
    //##81 if( (c1=getExp1().evaluate())!=null
    //##81 &&  (c2=getExp2().evaluate())!=null )
    //##81   switch( hirRoot.sourceLanguage.evaluateRelation(c1,c2) )
    if ((lEvalExp1 != null)&&(lEvalExp2 != null)) //##81
      switch (hirRoot.sourceLanguage.evaluateRelation(lEvalExp1, lEvalExp2)) //##81
      {
      case 1: return hirRoot.symRoot.boolConstTrue;
      case 0:
      case-1: return hirRoot.symRoot.boolConstFalse;
      }
    return null;

  case HIR.OP_CMP_GE:
    //##81 if( (c1=getExp1().evaluate())!=null
    //##81  &&  (c2=getExp2().evaluate())!=null )
    //##81   switch( hirRoot.sourceLanguage.evaluateRelation(c1,c2) )
    if ((lEvalExp1 != null)&&(lEvalExp2 != null)) //##81
      switch (hirRoot.sourceLanguage.evaluateRelation(lEvalExp1, lEvalExp2)) //##81
      {
      case 1:
      case 0: return hirRoot.symRoot.boolConstTrue;
      case-1: return hirRoot.symRoot.boolConstFalse;
      }
    return null;

  case HIR.OP_CMP_LT:
    //##81 if( (c1=getExp1().evaluate())!=null
    //##81 &&  (c2=getExp2().evaluate())!=null )
    //##81   switch( hirRoot.sourceLanguage.evaluateRelation(c1,c2) )
    if ((lEvalExp1 != null)&&(lEvalExp2 != null)) //##81
      switch (hirRoot.sourceLanguage.evaluateRelation(lEvalExp1, lEvalExp2)) //##81
      {
      case 1:
      case 0: return hirRoot.symRoot.boolConstFalse;
      case-1: return hirRoot.symRoot.boolConstTrue;
      }
    return null;

  case HIR.OP_CMP_LE:
    //##81 if( (c1=getExp1().evaluate())!=null
    //##81 &&  (c2=getExp2().evaluate())!=null )
    //##81   switch( hirRoot.sourceLanguage.evaluateRelation(c1,c2) )
    if ((lEvalExp1 != null)&&(lEvalExp2 != null)) //##81
      switch (hirRoot.sourceLanguage.evaluateRelation(lEvalExp1, lEvalExp2)) //##81
      {
      case 1: return hirRoot.symRoot.boolConstFalse;
      case 0:
      case-1: return hirRoot.symRoot.boolConstTrue;
      }
    return null;
  }
  return null;
}

public int evaluateAsInt()
{
  return (int)evaluateAsLong();
}

public long evaluateAsLong()
{
  Const c = evaluate();
  if( c!=null && c.getSymKind()==Sym.KIND_INT_CONST )
    return c.longValue();
  return 0;
}

public float evaluateAsFloat()
{
  return (float)evaluateAsDouble();
}

public double evaluateAsDouble()
{
  Const c = evaluate();
  if( c!=null )
    return c.doubleValue();
  return 0;
}

//##84 BEGIN
public Exp
adjustTypesOfBinaryOperands( Exp pExp1, Exp pExp2 )
{
  //-- This routine is the same as the part commented as
  //   "Adjust operand types" of evaluate() method.
  Exp lExp1 = null, lExp2 = null;
  Type lType1 = null, lType2 = null;
  Type lConvType;
  lExp1 = pExp1;
  lExp2 = pExp2;
  lType1 = lExp1.getType();
  lType2 = lExp2.getType();
  if (fDbgLevel > 3)
    hirRoot.ioRoot.dbgHir.print(6, "evaluate", toString());
  int lOperator = getOperator();
  switch( lOperator ) {
  case HIR.OP_CONST:
  case HIR.OP_VAR:
  case HIR.OP_NEG:
  case HIR.OP_NOT:
  case HIR.OP_SHIFT_LL:
  case HIR.OP_SHIFT_R:
  case HIR.OP_SHIFT_RL:
  case HIR.OP_CONV:
  case HIR.OP_SELECT:
  case HIR.OP_COMMA:
    break;

  // Binary expression
  case HIR.OP_ADD:
  case HIR.OP_SUB:
  case HIR.OP_MULT:
  case HIR.OP_DIV:
  case HIR.OP_MOD:
  case HIR.OP_AND:
  case HIR.OP_OR:
  case HIR.OP_XOR:
  case HIR.OP_LG_AND:
  case HIR.OP_LG_OR:
  case HIR.OP_CMP_EQ:
  case HIR.OP_CMP_NE:
  case HIR.OP_CMP_GT:
  case HIR.OP_CMP_GE:
  case HIR.OP_CMP_LT:
  case HIR.OP_CMP_LE:
    // See ISO/IEC 9899-1999 Programming language C section 6.3.1.8.
    if (lType1 != lType2) {
//      int lResultRank = lResultType.getTypeRank();
      int lRank1 = lType1.getTypeRank();
      int lRank2 = lType2.getTypeRank();
    if (lType1.isFloating() && lType2.isFloating()) {
      if (lRank1 > lRank2)
        lExp2 = lExp2.convExp(lType1, lExp2);
      else if (lRank2 > lRank1)
        lExp1 = lExp1.convExp(lType2, lExp1);
    }else if (lType1.isFloating() && lType2.isInteger()) {
      lExp2 = lExp2.convExp(lType1, lExp2);
    }else if (lType2.isFloating() && lType1.isInteger()) {
      lExp1 = lExp1.convExp(lType2, lExp1);
    }else if ((lType1.isUnsigned() && lType2.isUnsigned())||
          ((! lType1.isUnsigned())&&(! lType2.isUnsigned()))) {
        if (lRank1 > lRank2) {
          lExp2 = lExp2.convExp(lType1, lExp2);
        }else if (lRank2 > lRank1) {
          lExp1 = lExp1.convExp(lType2, lExp1);
        }
      }else if (lType1.isUnsigned() &&
                (lType1.getSizeValue() >= lType2.getSizeValue())) {
        lExp2 = lExp2.convExp(lType1, lExp2);
      }else if (lType2.isUnsigned() &&
                (lType2.getSizeValue() >= lType1.getSizeValue())) {
        lExp1 = lExp1.convExp(lType2, lExp1);
      }else if ((! lType1.isUnsigned())&&
                (lType1.getSizeValue() > lType2.getSizeValue())) {
        lExp2 = lExp2.convExp(lType1, lExp2);
      }else if ((! lType2.isUnsigned())&&
                (lType2.getSizeValue() > lType1.getSizeValue())) {
        lExp1 = lExp1.convExp(lType2, lExp1);
      }else {
        lConvType = hirRoot.symRoot.typeU_Char;
        if ((lConvType.getSizeValue() < lType1.getSizeValue())||
            (lConvType.getSizeValue() < lType2.getSizeValue()))
          lConvType = hirRoot.symRoot.typeU_Short;
        if ((lConvType.getSizeValue() < lType1.getSizeValue())||
            (lConvType.getSizeValue() < lType2.getSizeValue()))
         lConvType = hirRoot.symRoot.typeU_Int;
       if ((lConvType.getSizeValue() < lType1.getSizeValue())||
           (lConvType.getSizeValue() < lType2.getSizeValue()))
         lConvType = hirRoot.symRoot.typeU_Long;
       if ((lConvType.getSizeValue() < lType1.getSizeValue())||
           (lConvType.getSizeValue() < lType2.getSizeValue()))
         lConvType = hirRoot.symRoot.typeU_LongLong;
       if (lRank1 < lConvType.getTypeRank()) {
         lExp1 = lExp1.convExp(lConvType, lExp1);
       }
       if (lRank2 < lConvType.getTypeRank()) {
         lExp2 = lExp2.convExp(lConvType, lExp2);
       }
     }
    } // end of if(lType1 != lType2)
    break;
  default:
  } // switch end
  Exp lPair = hirRoot.hir.exp(HIR.OP_SEQ, lExp1, lExp2);
  if (fDbgLevel > 3)
    hirRoot.ioRoot.dbgHir.print(6, "adjustTypesOfBinaryOperands",
        lExp1.toStringWithChildren() + " " + lExp2.toStringWithChildren());
  return lPair;
} // adjustTypesOfBinaryOperands

//##84 END

/**
 * Fold "this" expression.
 * evaluate() is called by recursive. If the evaluation succeeded, former node
 * is substituted for the constant node generated as evaluation result.
 * @return  constant as the result of evaluation or null (when failing in the
 *          evaluation)
**/
public Exp fold()
{
  switch( getOperator() )
  {
  case HIR.OP_CONST:
    return this;

  case HIR.OP_EXPLIST:
    for( Iterator i=((ExpListExp)this).iterator(); i.hasNext(); )
      ((Exp)i.next()).fold();
    return this;

  case HIR.OP_EXPREPEAT:
    getExp1().fold();
    return this;

  default:
    for(int i=1; i<=getChildCount(); i++)
      ((Exp)getChild(i)).fold();
    Const c = evaluate();
    if( c!=null )
      return (Exp)replaceThisNode(hirRoot.hir.constNode(c));
    return this;
  }
}
//SF050111]

public Stmt
initiateArray(
    Exp pArray, Exp pInitExp,
    Exp pFrom, Exp pTo, Subp pSubp )
{
  SubpDefinition lSubpDefinition;
  SymTable lSymTable;
  Type  lType, lElemType;
  Stmt  lLoopStmt, lLoopInit, lLoopBody, lLoopStep;
  Exp   lInitExp, lLoopCond;
  Label lLoopBackLabel, lLoopStepLabel, lLoopEndLabel;
  HIR   lHir;
  Var   lTemp;
  lType = pArray.getType();
  if (lType.getTypeKind() == Type.KIND_VECTOR)
    lElemType = ((VectorType)lType).getElemType();
  else
    return null; // Not a vector. Do nothing.
  if (pSubp == null) { // Global initiation.
    //  REFINE
  }else { // Local initiation.
    lSubpDefinition = pSubp.getSubpDefinition();
    lSymTable = pSubp.getSymTable();
    lHir = hirRoot.symRoot.getHirRoot().hir;
    lTemp = lSymTable.generateVar(hirRoot.symRoot.typeInt, pSubp);
    if (pInitExp == null) {
      lInitExp = lHir.intConstNode(0);
      if (lElemType != hirRoot.symRoot.typeInt)
        lInitExp = lHir.convExp(lElemType, lInitExp);
    }else
      lInitExp = pInitExp;
    lLoopInit = lHir.assignStmt(lHir.varNode(lTemp), pFrom);
    lLoopBody = lHir.assignStmt(
                  lHir.subscriptedExp(pArray, lHir.varNode(lTemp)),
                  lInitExp);
    lLoopStep = lHir.assignStmt(
                  lHir.varNode(lTemp),
                  lHir.exp(HIR.OP_ADD, lHir.varNode(lTemp),
                           lHir.intConstNode(1)));
    lLoopCond = lHir.exp(HIR.OP_CMP_LE,
                         lHir.varNode(lTemp), pTo);
    lLoopBackLabel = lSymTable.generateLabel();
    lLoopStepLabel = lSymTable.generateLabel();
    lLoopEndLabel  = lSymTable.generateLabel();
    lLoopStmt = lHir.forStmt(lLoopInit, lLoopBackLabel,
                  lLoopCond, lLoopBody, lLoopStepLabel,
                  lLoopStep, lLoopEndLabel);
    return lLoopStmt;
  }
  return null;
} // initiateArray

public String
getValueString()
{
  String lString;
  if (isEvaluable()) {
    Type lType = getType();
    if (lType.isInteger())
      lString = "" + evaluateAsInt();
    else if (lType.isFloating()) {
      if (lType.getTypeKind() == Type.KIND_FLOAT)
        lString = "" + evaluateAsFloat();
      else
        lString = "" + evaluateAsDouble();
    }else {
      lString = toStringWithChildren();
    }
  }else {
    lString = toStringWithChildren();
  }
  return lString;
} // getValueString

public boolean
isLValue()      // Not accurate.
{
  HIR lParent = (HIR)getParent();
  if (lParent == null)
    return true;
  switch (lParent.getOperator()) {
  case OP_ASSIGN:
    if (lParent.getChild1() == this)
      return true;
    else return false;
  case OP_ADDR:
    return true;
  default:
    return false;
  }
} // isLValue()

public void
accept( HirVisitor pVisitor )
{
  pVisitor.atExp(this);
}

} // ExpImpl
