/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * ConstFoldingHir.java
 *
 * Created on July 31, 2002, 11:08 AM
 */

package coins.opt;

import java.math.BigDecimal;
import java.math.BigInteger;

import coins.FlowRoot;
import coins.SymRoot;
import coins.aflow.FlowResults;
//##63 import coins.aflow.FlowUtil;
import coins.flow.FlowUtil; //##63
import coins.flow.SubpFlow; //##63
import coins.flow.HirSubpFlowImpl; //##63
import coins.ir.IR;
import coins.ir.hir.ConstNode;
import coins.ir.hir.Exp;
import coins.ir.hir.HIR;
import coins.sym.BoolConst;
import coins.sym.Const;
import coins.sym.FloatConst;
import coins.sym.IntConst;
import coins.sym.Sym;
import coins.sym.Type;

/**
 * This class performs constant folding operations that are specific to HIR.
 *
 * @author  hasegawa
 */
public strictfp class ConstFoldingHir
  extends ConstFolding
{

  public final HIR hir;
  public final Opt opt;
  /**
   *
   * ConstFoldingHir
   *
   * Creates new ConstFoldingHir
   *
   **/
  public ConstFoldingHir(FlowResults pResults)
  {

    super(pResults);
    hir = flowRoot.hirRoot.hir;
    opt = new Opt(flowRoot);
    fSubpFlow.computeBBlockSetRefReprs(); //##63
  }

  /**
   *
   * isConstNode
   *
   *
   **/
  boolean isConstNode(IR pIR)
  {

    if (pIR instanceof ConstNode) {
      return true;
    }
    return false;

  }

  /**
   *
   * getChild1
   *
   *
   **/
  IR getChild1(IR pIR)
  {

    return pIR.getChild1();

  }

  /**
   *
   * getChild2
   *
   *
   **/
  IR getChild2(IR pIR)
  {

    return pIR.getChild2();

  }

  /**
   *
   * foldUnary
   *
   *
   **/
  public static HIR foldUnary(HIR pParent, FlowRoot pFlowRoot)
  {
    int lOpCode = pParent.getOperator();
    boolean lBoolResult = false;
    long lIntResult = 0;
    double lFloatResult = 0.0;
    Type lType = pParent.getType();
    ConstNode lNewConstNode;
    SymRoot lSymRoot = pFlowRoot.symRoot;
    Sym sym = lSymRoot.sym;
    ConstNode lConstNode = (ConstNode)pParent.getChild1();
    Const lConst = lConstNode.getConstSym().getConstSym();
    boolean lBoolVal = false;
    long lIntVal = 0;
    double lFloatVal = 0.0;
    boolean lIsBasic = lType.isBasicType();
    boolean lIsBool = lType.getTypeKind() == Type.KIND_BOOL;
    boolean lIsInt = pParent.getType().isInteger();
    boolean lIsFloat = pParent.getType().isFloating();
    boolean lChildIsBool = lConst.getSymType().getSymKind() == Type.KIND_BOOL;
    boolean lChildIsInt = lConst.getSymType().isInteger();
    boolean lChildIsFloat = lConst.getSymType().isFloating();

    if (lType.getTypeKind() == Type.KIND_OFFSET) {
      return pParent;
    }

    if (lChildIsBool) {
      lBoolVal = lConst == lSymRoot.boolConstTrue;
    }
    if (lChildIsInt) {
      lIntVal = evaluateAsItsType((IntConst)lConst, lConst.getSymType());
    }
    else if (lChildIsFloat) {
      lFloatVal = evaluateAsItsType((FloatConst)lConst, lConst.getSymType());

    }
    switch (lOpCode) {
      case HIR.OP_ENCLOSE:
      case HIR.OP_RETURN:
      case HIR.OP_EXP_STMT:
      case HIR.OP_DECAY:
      case HIR.OP_ADDR:
        return pParent;
      case HIR.OP_NOT:
        lBoolResult = !lBoolVal;
        lIntResult = ~lIntVal;
        break;
      case HIR.OP_NEG:
        lIntResult = -lIntVal;
        lFloatResult = -lFloatVal;
        break;
      case HIR.OP_CONV:
        return pParent;
        // 2004.08.09 I.Fukuda  unsigned int x; double y ; y= (double) x;
        /*
                     if (lIsBool && lChildIsInt)
            lBoolResult = lIntVal != 0;
                     else if (lIsBool && lChildIsFloat)
            lBoolResult = lFloatVal != 0;
                     else if (lIsInt && lChildIsBool)
            lIntResult = lBoolVal ? 1 : 0;
                     else if (lIsInt && lChildIsFloat)
            lIntResult = evaluateAsIntType(lFloatVal, lType);
                     else if (lIsFloat && lChildIsBool)
            lFloatResult = lBoolVal ? 1.0 : 0.0;
                     else if (lIsFloat && lChildIsInt)
            lFloatResult = evaluateAsFloatType(lIntVal, lType);
                     else {
            lBoolResult = lBoolVal;
            lIntResult = lIntVal;
            lFloatResult = lFloatVal;
                     }
                     break;
      */
      default:
        throw new OptError(
          "Operator \"" + HIR.OP_CODE_NAME[lOpCode] +
          "\" not yet taken care of.");

    }

    //
    // Workaround code (Code generator doesn't allow integral constants
    // other than I32, and HIR2LIR does not support Boolean constant)
    // Creating a short constant will overwrite the int const with the same value.
    // Is this OK?
    //

    if (!lType.isFloating() && lType.getSizeValue() != 4) { //|| lIsBool)
      return pParent;
    }

    if (lIsBasic) {
      if (lIsBool) {
        lNewConstNode = pFlowRoot.hirRoot.hir.constNode(
          lSymRoot.sym.boolConst(lBoolResult));
      }
      else if (lIsInt) {
        lNewConstNode = OptUtil.createConstNodeFromPrimitive(lIntResult, lType,
          pFlowRoot);
      }
      else if (lIsFloat) {
        lNewConstNode = OptUtil.createConstNodeFromPrimitive(lFloatResult,
          lType, pFlowRoot);
      }
      else {
        throw new OptError("Unexpected.");
      }
    }
    else {
      return pParent;
    }

    OptUtil.replaceNode(pParent, lNewConstNode);

    return lNewConstNode;
  }

  /**
   *
   * foldBinary
   *
   *
   **/
  public static HIR foldBinary(HIR pParent, FlowRoot pFlowRoot)
  {
    final HIR hir = pFlowRoot.hirRoot.hir;
    final SymRoot symRoot = pFlowRoot.symRoot;
    final Sym sym = symRoot.sym;
    final HIR lChild = (HIR)pParent.getChild1();
    final HIR lChild0 = (HIR)pParent.getChild2();
    boolean lBoolResult;
    long lIntResult;
    double lFloatResult;
    Exp lNewExpNode = null;
    int lOpCode = pParent.getOperator();
    Type lType = ((HIR)pParent).getType();
    Type lChildType = lChild.getType();
    int lTypeKind = lType.getTypeKind();
    boolean lIsIntType = lType.isInteger();
    boolean lIsChildBoolType = lChildType.getTypeKind() == Type.KIND_BOOL;
    boolean lIsChildIntType = lChildType.isInteger();
    boolean lIsSigned = !lType.isUnsigned();
    SubpFlow lSubpFlow = pFlowRoot.fSubpFlow; //##63

    if (lTypeKind == Type.KIND_OFFSET) {
      return pParent;
    }

    ConstNode lConstNode = null;
    ConstNode lConstNode0 = null;
    Const lConstSym = null;
    Const lConstSym0 = null;
    boolean lTrivialCheck = false;
    boolean lTrivialCheck0 = false;
    boolean lIsOne = false;
    boolean lIsOne0 = false;
    boolean lIsZero = false;
    boolean lIsZero0 = false;
    boolean lIsMinus1 = false;
    boolean lIsMinus10 = false;
    boolean lIsFalse = false;
    boolean lIsFalse0 = false;
    boolean lIsTrue = false;
    boolean lIsTrue0 = false;

    //        long lDivisori;
    //        double lDivisorf;

    if (lChild instanceof ConstNode) {
      lConstNode = (ConstNode)lChild;
      lConstSym = lConstNode.getConstSym();
      lTrivialCheck = true;
    }

    if (lChild0 instanceof ConstNode) {
      lConstNode0 = (ConstNode)lChild0;
      lConstSym0 = lConstNode0.getConstSym();
      lTrivialCheck0 = true;
    }

    if (!(lTrivialCheck || lTrivialCheck0)) {
      return pParent;
    }

    if (lTrivialCheck) {
      if (lConstSym.equals(symRoot.boolConstFalse)) {
        lIsFalse = true;
      }
      else if (lConstSym.equals(symRoot.boolConstTrue)) {
        lIsTrue = true;
      }
      else if (IsZeroConst(lConstNode, symRoot) == true) { // 2004.08.09 I.Fukuda
        lIsZero = true;
      }
      else if (lConstSym.equals(symRoot.intConst1)) {
        lIsOne = true;
      }
      else if (lConstSym instanceof IntConst &&
               evaluateAsItsType((IntConst)lConstSym, lChildType) == -1) {
        lIsMinus1 = true;
      }
    }
    if (lTrivialCheck0) {
      if (lConstSym0.equals(symRoot.boolConstFalse)) {
        lIsFalse0 = true;
      }
      else if (lConstSym0.equals(symRoot.boolConstTrue)) {
        lIsTrue0 = true;
      }
      else if (IsZeroConst(lConstNode0, symRoot) == true) { // 2004.08.09 I.Fukuda
        lIsZero0 = true;
      }
      else if (lConstSym0.equals(symRoot.intConst1)) {
        lIsOne0 = true;
      }
      else if (lConstSym0 instanceof IntConst &&
               evaluateAsItsType((IntConst)lConstSym0, lConstSym0.getSymType()) ==
               -1) {
        lIsMinus10 = true;
      }
    }
    if (lTrivialCheck && lTrivialCheck0) {
      switch (lOpCode) {
        //case HIR.OP_INDEX:
        case HIR.OP_ASSIGN:
        case HIR.OP_UNDECAY:
        case HIR.OP_SETDATA:
        case HIR.OP_SUBS:
          return pParent;
        case HIR.OP_ADD:
          if (lIsIntType) {
            lIntResult = lConstSym.longValue() + lConstSym0.longValue();
            lNewExpNode = hir.constNode(sym.intConst(lIntResult, lType));
          }
          else {
            return pParent;
            /*
               lFloatResult = evaluateAsItsType((FloatConst)lConstSym, lType)
               + evaluateAsItsType((FloatConst)lConstSym0, lType);
               lNewExpNode = hir.constNode(sym.floatConst(lFloatResult, lType));
             */
          }
          break;
        case HIR.OP_SUB:
          if (lIsIntType) {
            lIntResult = lConstSym.longValue() - lConstSym0.longValue();
            lNewExpNode = hir.constNode(sym.intConst(lIntResult, lType));
          }
          else {
            return pParent;
            /*
               lFloatResult = evaluateAsItsType((FloatConst)lConstSym, lType)
                         - evaluateAsItsType((FloatConst)lConstSym0, lType);
               lNewExpNode = hir.constNode(sym.floatConst(lFloatResult, lType));
             */
          }
          break;
        case HIR.OP_MULT:
          if (lIsIntType) {
            lIntResult = lConstSym.longValue() * lConstSym0.longValue();
            lNewExpNode = hir.constNode(sym.intConst(lIntResult, lType));
          }
          else {
            return pParent;
            /*
               lFloatResult = evaluateAsItsType((FloatConst)lConstSym, lType)
             * evaluateAsItsType((FloatConst)lConstSym0, lType);
               lNewExpNode = hir.constNode(sym.floatConst(lFloatResult, lType));
             */
          }
          break;
        case HIR.OP_DIV:
          if (lIsIntType) {
            if (lIsZero0) {
              return pParent;
            }
            if (lIsSigned) {
              lIntResult = evaluateAsItsType((IntConst)lConstSym, lType)
                / evaluateAsItsType((IntConst)lConstSym0, lType);
            }
            else if (hasJavaPrimitive(lType)) {
              BigInteger lDividend = evaluateAsUnsigned((IntConst)lConstSym,
                lType);
              BigInteger lDivisor = evaluateAsUnsigned((IntConst)lConstSym0,
                lType);
              lIntResult = lDividend.divide(lDivisor).longValue();
            }
            else {
              return pParent;
            }
            lNewExpNode = hir.constNode(sym.intConst(lIntResult, lType));
          }
          else {
            return pParent;
            /*
               lFloatResult = evaluateAsItsType((FloatConst)lConstSym, lType)
               / evaluateAsItsType((FloatConst)lConstSym0, lType);
               lNewExpNode = hir.constNode(sym.floatConst(lFloatResult, lType));
             */
          }
          break;
        case HIR.OP_MOD:
          if (lIsZero0) {
            return pParent;
          }
          if (lIsSigned) {
            lIntResult = evaluateAsItsType((IntConst)lConstSym, lType) %
              evaluateAsItsType((IntConst)lConstSym0, lType);
          }
          else {
            if (hasJavaPrimitive(lType)) {
              BigInteger lDividend = evaluateAsUnsigned((IntConst)lConstSym,
                lType);
              BigInteger lDivisor = evaluateAsUnsigned((IntConst)lConstSym0,
                lType);
              lIntResult = lDividend.remainder(lDivisor).longValue();
            }
            else {
              return pParent;
            }
          }
          lNewExpNode = hir.constNode(sym.intConst(lIntResult, lType));
          break;
        case HIR.OP_AND:
          lIntResult = evaluateAsItsType((IntConst)lConstSym, lChildType) &
            evaluateAsItsType((IntConst)lConstSym0, lChildType);
          if (lIsIntType) {
            lNewExpNode = hir.constNode(sym.intConst(lIntResult, lType));
          }
          else {
            lNewExpNode = hir.constNode(sym.boolConst(lIntResult != 0));
          }
          break;
        case HIR.OP_OR:
          lIntResult = evaluateAsItsType((IntConst)lConstSym, lChildType) |
            evaluateAsItsType((IntConst)lConstSym0, lChildType);
          if (lIsIntType) {
            lNewExpNode = hir.constNode(sym.intConst(lIntResult, lType));
          }
          else {
            lNewExpNode = hir.constNode(sym.boolConst(lIntResult != 0));
          }
          break;
        case HIR.OP_XOR:
          lIntResult = evaluateAsItsType((IntConst)lConstSym, lChildType) ^
            evaluateAsItsType((IntConst)lConstSym0, lChildType);
          if (lIsIntType) {
            lNewExpNode = hir.constNode(sym.intConst(lIntResult, lType));
          }
          else {
            lNewExpNode = hir.constNode(sym.boolConst(lIntResult != 0));
          }
          break;
        case HIR.OP_CMP_EQ:
          if (lIsChildBoolType) {
            lBoolResult =
              ((BoolConst)lConstSym).intValue() ==
              ((BoolConst)lConstSym0).intValue();
          }
          else if (lIsChildIntType) {
            lBoolResult =
              evaluateAsItsType((IntConst)lConstSym, lChildType) ==
              evaluateAsItsType((IntConst)lConstSym0, lChildType);
          }
          else {
            lBoolResult =
              evaluateAsItsType((FloatConst)lConstSym, lChildType) ==
              evaluateAsItsType((FloatConst)lConstSym0, lChildType);
          }
          lNewExpNode = hir.constNode(sym.boolConst(lBoolResult));
          break;
        case HIR.OP_CMP_NE:
          if (lIsChildBoolType) {
            lBoolResult =
              ((BoolConst)lConstSym).intValue() !=
              ((BoolConst)lConstSym0).intValue();
          }
          else if (lIsChildIntType) {
            lBoolResult =
              evaluateAsItsType((IntConst)lConstSym, lChildType) !=
              evaluateAsItsType((IntConst)lConstSym0, lChildType);
          }
          else {
            lBoolResult =
              evaluateAsItsType((FloatConst)lConstSym, lChildType) !=
              evaluateAsItsType((FloatConst)lConstSym0, lChildType);
          }
          lNewExpNode = hir.constNode(sym.boolConst(lBoolResult));
          break;
        case HIR.OP_CMP_GT:
          if (lIsChildIntType) {
            if (lIsSigned) {
              lBoolResult = evaluateAsItsType((IntConst)lConstSym, lChildType)
                > evaluateAsItsType((IntConst)lConstSym0, lChildType);
            }
            else {
              lBoolResult = evaluateAsUnsigned(
                (IntConst)lConstSym, lChildType).compareTo(
                evaluateAsUnsigned((IntConst)lConstSym0, lChildType)) == 1;
            }
          }
          else {
            lBoolResult = (evaluateAsItsType(
              (FloatConst)lConstSym, lChildType) >
              evaluateAsItsType((FloatConst)lConstSym0, lChildType));
          }
          lNewExpNode = hir.constNode(sym.boolConst(lBoolResult));
          break;
        case HIR.OP_CMP_GE:
          if (lIsChildIntType) {
            if (lIsSigned) {
              lBoolResult = evaluateAsItsType((IntConst)lConstSym, lChildType)
                >= evaluateAsItsType((IntConst)lConstSym0, lChildType);
            }
            else {
              lBoolResult = evaluateAsUnsigned((IntConst)lConstSym,
                lChildType).compareTo(
                evaluateAsUnsigned((IntConst)lConstSym0, lChildType)) != -1;
            }
          }
          else {
            lBoolResult = (evaluateAsItsType((FloatConst)lConstSym, lChildType)
              >= evaluateAsItsType((FloatConst)lConstSym0, lChildType));
          }
          lNewExpNode = hir.constNode(sym.boolConst(lBoolResult));
          break;
        case HIR.OP_CMP_LT:
          if (lIsChildIntType) {
            if (lIsSigned) {
              lBoolResult = evaluateAsItsType((IntConst)lConstSym, lChildType)
                < evaluateAsItsType((IntConst)lConstSym0, lChildType);
            }
            else {
              lBoolResult = evaluateAsUnsigned(
                (IntConst)lConstSym, lChildType).compareTo(
                evaluateAsUnsigned((IntConst)lConstSym0, lChildType)) == -1;
            }
          }
          else {
            lBoolResult = (evaluateAsItsType((FloatConst)lConstSym, lChildType)
              < evaluateAsItsType((FloatConst)lConstSym0, lChildType));
          }
          lNewExpNode = hir.constNode(sym.boolConst(lBoolResult));
          break;
        case HIR.OP_CMP_LE:
          if (lIsChildIntType) {
            if (lIsSigned) {
              lBoolResult = evaluateAsItsType((IntConst)lConstSym, lChildType)
                <= evaluateAsItsType((IntConst)lConstSym0, lChildType);
            }
            else {
              lBoolResult = evaluateAsUnsigned((IntConst)lConstSym,
                lChildType).compareTo(
                evaluateAsUnsigned((IntConst)lConstSym0, lChildType)) != 1;
            }
          }
          else {
            lBoolResult = (evaluateAsItsType((FloatConst)lConstSym, lChildType)
              <= evaluateAsItsType((FloatConst)lConstSym0, lChildType));
          }
          lNewExpNode = hir.constNode(sym.boolConst(lBoolResult));
          break;
        case HIR.OP_SHIFT_LL:
        case HIR.OP_SHIFT_R:
        case HIR.OP_SHIFT_RL:
        case HIR.OP_ASM:  //##85
          return pParent;
          /*============================================================
           case HIR.OP_SHIFT_LL:
               lIntResult = lConstSym.longValue() << lConstSym0.longValue();
               lNewExpNode = hir.constNode(sym.intConst(lIntResult, lType));
               break;
           case HIR.OP_SHIFT_R:
               //if (lIsSigned) {
               //    lIntResult = evaluateAsItsType((IntConst)lConstSym, lType)
               //                        >> lConstSym0.longValue();
               //} else {
               //
                   lIntResult = evaluateAsItsType((IntConst)lConstSym, lType)
                                       >>> lConstSym0.longValue();
               }
               lNewExpNode = hir.constNode(sym.intConst(lIntResult, lType));
               break;
           case HIR.OP_SHIFT_RL:
               //
               //  2004.08.10 I.Fukuda
               //  int c=1;
               //  printf("%08X %08lX\n",0x7FFFFFFFU>>c,0x7FFFFFFFUL>>c);
               //  result -->  3FFFFFFF 3FFFFFFF
               //
               //long lMask = 1L << (lType.getSizeValue() * 8 - 1);
               //lIntResult = (evaluateAsItsType((IntConst)lConstSym, lType) & lMask)
               //                        >>> lConstSym0.longValue();
               //
               lIntResult = evaluateAsItsType((IntConst)lConstSym, lType)
                                       >>> lConstSym0.longValue();
               lNewExpNode = hir.constNode(sym.intConst(lIntResult, lType));
               break;
        ==================================================*/
        default:
          throw new OptError("Operator " + HIR.OP_CODE_NAME[lOpCode] +
            "not yet taken care of.");
      }
    }
    else {
      switch (lOpCode) {
        //case HIR.OP_INDEX:
        case HIR.OP_ASSIGN:
        case HIR.OP_UNDECAY:
        case HIR.OP_SETDATA:
          return pParent;

        case HIR.OP_SUBS:
          return pParent;
        case HIR.OP_ADD:
          if (lIsZero) {
            lNewExpNode = (Exp)lChild0;
          }
          else if (lIsZero0) {
            lNewExpNode = (Exp)lChild;
          }
          else {
            return pParent;
          }
          break;
        case HIR.OP_SUB:
          return pParent;
        case HIR.OP_MULT:
          if (lIsZero) {
            //##63 if (FlowUtil.hasCallUnder(lChild0))
            if (lSubpFlow.hasCallUnder(lChild0)) {
              return pParent;
            }
            lNewExpNode = hir.constNode(lConstSym);
          }
          else if (lIsZero0) {
            //##63 if (FlowUtil.hasCallUnder(lChild))
            if (lSubpFlow.hasCallUnder(lChild)) { //##63
              return pParent;
            }
            lNewExpNode = hir.constNode(lConstSym0);
          }
          else if (lIsOne) {
            lNewExpNode = (Exp)lChild0;
          }
          else if (lIsOne0) {
            lNewExpNode = (Exp)lChild;
          }
          else if (lIsMinus1) {
            lNewExpNode = hir.exp(HIR.OP_NEG, (Exp)lChild0);
          }
          else if (lIsMinus10) {
            lNewExpNode = hir.exp(HIR.OP_NEG, (Exp)lChild);
          }
          else {
            return pParent;
          }
          break;
        case HIR.OP_DIV:
          if (lIsZero0) {
            return pParent;
          }
          if (lIsZero) {
            if (lSubpFlow.hasCallUnder(lChild0)) { //##63
              return pParent;
            }
            lNewExpNode = hir.constNode(lConstSym);
          }
          else if (lIsOne0) {
            lNewExpNode = (Exp)lChild;
          }
          else if (lIsMinus10 && lIsSigned) {
            lNewExpNode = hir.exp(HIR.OP_NEG, (Exp)lChild);
          }
          else {
            return pParent;
          }
          break;
        case HIR.OP_MOD:
          if (lIsZero0) {
            return pParent;
          }
          if (lIsZero) {
            if (lSubpFlow.hasCallUnder(lChild0)) { //##63
              return pParent;
            }
            lNewExpNode = hir.constNode(lConstSym);
          }
          else if (lIsOne0 || lIsMinus10 && lIsSigned) {
            if (lSubpFlow.hasCallUnder(lChild0)) { //##63
              return pParent;
            }
            lNewExpNode = hir.constNode(symRoot.intConst0);
          }
          else {
            return pParent;
          }
          break;
        case HIR.OP_AND:
          if (lIsFalse || lIsZero) {
            if (lSubpFlow.hasCallUnder(lChild0)) { //##63
              return pParent;
            }
            lNewExpNode = (Exp)lChild;
          }
          else if (lIsTrue || lIsMinus1) {
            lNewExpNode = (Exp)lChild0;
          }
          else if (lIsFalse0 || lIsZero0) {
            if (lSubpFlow.hasCallUnder(lChild)) { //##63
              return pParent;
            }
            lNewExpNode = (Exp)lChild0;
          }
          else if (lIsTrue0 || lIsMinus10) {
            lNewExpNode = (Exp)lChild;
          }
          else {
            return pParent;
          }
          break;
        case HIR.OP_OR:
          if (lIsFalse || lIsZero) {
            lNewExpNode = (Exp)lChild0;
          }
          else if (lIsTrue || lIsMinus1) {
            if (lSubpFlow.hasCallUnder(lChild0)) { //##63
              return pParent;
            }
            lNewExpNode = (Exp)lChild;
          }
          else if (lIsFalse0 || lIsZero0) {
            lNewExpNode = (Exp)lChild;
          }
          else if (lIsTrue0 || lIsMinus10) {
            if (lSubpFlow.hasCallUnder(lChild)) { //##63
              return pParent;
            }
            lNewExpNode = (Exp)lChild0;
          }
          else {
            return pParent;
          }
          break;
        case HIR.OP_XOR:
          if (lIsFalse || lIsZero) {
            lNewExpNode = (Exp)lChild0;
          }
          else if (lIsTrue || lIsMinus1) {
            lNewExpNode = hir.exp(HIR.OP_NOT, (Exp)lChild0);
          }
          else if (lIsFalse0 || lIsZero0) {
            lNewExpNode = (Exp)lChild;
          }
          else if (lIsTrue0 || lIsMinus10) {
            lNewExpNode = hir.exp(HIR.OP_NOT, (Exp)lChild);
          }
          else {
            return pParent;
          }
          break;
        case HIR.OP_SHIFT_LL:
        case HIR.OP_SHIFT_R:
        case HIR.OP_SHIFT_RL:
          if (lIsZero) {
            if (lSubpFlow.hasCallUnder(lChild0)) { //##63
              return pParent;
            }
            lNewExpNode = hir.constNode(lConstSym);
          }
          else if (lIsZero0) {
            lNewExpNode = (Exp)lChild;
          }
          else {
            return pParent;
          }
          break;
        case HIR.OP_CMP_EQ:
        case HIR.OP_CMP_NE:
        case HIR.OP_CMP_GT:
        case HIR.OP_CMP_GE:
        case HIR.OP_CMP_LT:
        case HIR.OP_CMP_LE:
        case HIR.OP_ASM:  //##85
          return pParent;
        default:
          throw
            new OptError("Operator " + HIR.OP_CODE_NAME[lOpCode] +
            "not yet taken care of.");
      }
    }
    //
    // Workaround code (Code generator doesn't allow integral constants other than I32,
    //    and HIR2LIR does not support Boolean constant)

    Const llConst = lNewExpNode.getConstSym();

    if (llConst != null &&
        (!llConst.getSymType().isFloating() && lType.getSizeValue() != 4
         || lType.getTypeKind() == Type.KIND_BOOL)) {
      return pParent;
    }

    OptUtil.replaceNode(pParent, lNewExpNode);
    return lNewExpNode;
  }

  /**
   *
   * evaluteAsItsType
   *
   *
   **/
  private static long evaluateAsItsType(IntConst pConst, Type pType)
  {
    switch ((int)pType.getSizeValue()) {
      case 1:
        return (byte)pConst.longValue();
      case 2:
        return pConst.shortValue();
      case 4:
        return pConst.intValue();
      case 8:
        return pConst.longValue();
      default:
        throw new OptError();
    }
  }

  /**
   *
   * evaluteAsItsType
   *
   *
   **/
  private static double evaluateAsItsType(FloatConst pConst, Type pType)
  {
    switch ((int)pType.getSizeValue()) {
      case 4:
        return pConst.floatValue();
      case 8:
        return pConst.doubleValue();
      default:
        throw new OptError();
    }
  }

  /**
   *
   * evaluteAsFloatType
   *
   *
   **/

  private static double evaluateAsFloatType(long pLong, Type pType)
  {
    switch ((int)pType.getSizeValue()) {
      case 4:
        return (float)pLong;
      case 8:
        return (double)pLong;
      default:
        throw new OptError();
    }
  }

  /**
   *
   * evaluteAsIntType
   *
   *
   **/
  private static long evaluateAsIntType(double pDouble, Type pType)
  {
    BigInteger lBigInt = new BigDecimal(pDouble).toBigInteger();
    switch ((int)pType.getSizeValue()) {
      case 1:
        return lBigInt.byteValue();
      case 2:
        return lBigInt.shortValue();
      case 4:
        return lBigInt.intValue();
      case 8:
        return lBigInt.longValue();
      default:
        throw new OptError();
    }
  }

  /**
   *
   * evaluteAsUnsigned
   *
   *
   **/

  private static BigInteger evaluateAsUnsigned(IntConst pConst, Type pType)
  {
    String lMask;

    // 2004.08.05 I.Fukuda
    // 0xff -> ff
    switch ((int)pType.getSizeValue()) {
      case JAVA_BYTE_SIZE:
        lMask = "ff";
        break;
      case JAVA_CHAR_SIZE:
        lMask = "ffff";
        break;
      case JAVA_INT_SIZE:
        lMask = "ffffffff";
        break;
      case JAVA_LONG_SIZE:
        lMask = "ffffffffffffffff";
        break;
      default:
        throw new OptError();
    }
    // 2004.08.05 I.Fukuda
    //return BigInteger.valueOf(pConst.longValue()).and(new BigInteger(lMask, 0x10));
    return BigInteger.valueOf(pConst.longValue()).and(new BigInteger(lMask, 16));
  }

  /**
   *
   * hasjavaPrimitive
   *
   *
   **/
  private static boolean hasJavaPrimitive(Type pType)
  {
    switch ((int)pType.getSizeValue()) {
      case JAVA_BYTE_SIZE:
      case JAVA_SHORT_SIZE:
      case JAVA_INT_SIZE:
      case JAVA_LONG_SIZE:
        return true;
      default:
        return false;
    }
  }

  /**
   *
   * IsZeroConst
   *
   *
   **/
  private static boolean IsZeroConst(ConstNode pConstNode, SymRoot symRoot)
  {
    Const lConstSym = null;
    lConstSym = pConstNode.getConstSym();
    if (lConstSym.equals(symRoot.doubleConst0) == true) {
      return true;
    }
    if (lConstSym.equals(symRoot.floatConst0) == true) {
      return true;
    }
    if (lConstSym.equals(symRoot.intConst0) == true) {
      return true;
    }
    if (lConstSym.equals(symRoot.longConst0) == true) {
      return true;
    }
    Type lType = ((HIR)pConstNode).getType();
    if (lType.isInteger() == true) {
      if (lConstSym.longValue() == 0) {
        return true;
      }
    }
    if (lType.isUnsigned() == true) {
      if (lConstSym.longValue() == 0) {
        return true;
      }
    }
    return false;
  }
}
