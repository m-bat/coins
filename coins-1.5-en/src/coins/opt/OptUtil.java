/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.opt;

import java.math.BigDecimal;
import java.math.BigInteger;

import coins.FlowRoot;
import coins.MachineParam;
import coins.SymRoot;
import coins.flow.FlowUtil; //##60
import coins.flow.SubpFlowImpl; //##65
import coins.ir.IR;
import coins.ir.hir.BlockStmt; //##65
import coins.ir.hir.ConstNode;
import coins.ir.hir.FunctionExp;
import coins.ir.hir.HIR;
import coins.ir.hir.HirList;
import coins.ir.hir.HIR_Impl;  //##54
import coins.ir.hir.LabeledStmt; //##65
import coins.ir.hir.Stmt; //##65
//import coins.ir.lir.Call; // 2004.05.31 S.Noishi
//import coins.ir.lir.LConst; // 2004.05.31 S.Noishi
//import coins.ir.lir.LIRFactory; // 2004.05.31 S.Noishi
//import coins.ir.lir.LIRNode; // 2004.05.31 S.Noishi
//import coins.ir.lir.Ltype; // 2004.05.31 S.Noishi
import coins.sym.Sym;
import coins.sym.Type;
import coins.sym.TypeImpl;  //##51
/**
*
* OptUtil
*
*
* Utility methods that perform some modifications on the program.
* Those that do not perform modifications should be moved to FlowUtil.
*
*
*/
public class OptUtil
{
    /**
    *
    * peplaceNode
    *
    * No longer necessary?
    *
    */
     public static void replaceNode(IR pOld, IR pNew)
    {
        HIR lParent;
        HirList lHirList;

        if (pOld instanceof HIR)
        {
          //##57 BEGIN
          if (pNew instanceof HIR) {
            ((HIR)pOld).replaceThisNode((HIR)pNew);
            ((SubpFlowImpl)((HIR_Impl)pNew).hirRoot.getFlowRoot().fSubpFlow).
               fIteratorInitiated = false; //##65
          }else {
          //##57 END
            lParent = (HIR)pOld.getParent();
            if (lParent != null) { // 2004.08.03 I.Fukuda
              if (lParent.getOperator() == HIR.OP_LIST) {
                lHirList = (HirList)lParent;
                lHirList.set(lHirList.indexOf(pOld), pNew);
                ((HIR_Impl)pNew).setParent(lHirList); //##54
              }
              else
                lParent.replaceSource(((HIR)pOld).getChildNumber(), pNew);
            }
          } //##57
        } else
            throw new OptError(); // 2004.06.01 S.Noishi
        // ((LIRNode)pOld).replace((LIRNode)pNew);
    }
  //##65 BEGIN

  /**
   * Replace pOldStmt by pNewStmt.
   * @param pOldStmt statement to be deleted.
   * @param pNewStmt statement to override.
   */
  public static void
    replaceStmt( Stmt pOldStmt, Stmt pNewStmt )
  {
    if (pOldStmt == null)
      return;
    pOldStmt.replaceThisStmtWith(pNewStmt);
    ((SubpFlowImpl)((HIR_Impl)pOldStmt).hirRoot.getFlowRoot().fSubpFlow).
       fIteratorInitiated = false;
  }
  /**
   * Add pNewStmt so that it follows pCurrentStmt.
   * If pCurrentStmt is not given, do nothing.
   * @param pCurrentStmt show the position to insert.
   * @param pNewStmt statement to be inserted.
   */
  public static void
    addNextStmt( Stmt pCurrentStmt, Stmt pNewStmt )
  {
    if (pCurrentStmt == null)
      return;
    pCurrentStmt.addNextStmt(pNewStmt);
    ((SubpFlowImpl)((HIR_Impl)pNewStmt).hirRoot.getFlowRoot().fSubpFlow).
       fIteratorInitiated = false;
  } // addNextStmt

  /**
   * Insert pNewStmt in front of pCurrentStmt.
   * If pCurrentStmt is null, do nothing.
   * @param pCurrentStmt show the place to insert.
   * @param pNewStmt statement to be inserted.
   */
  public static void
    insertPreviousStmt( Stmt pCurrentStmt, Stmt pNewStmt )
  {
    if (pCurrentStmt == null)
      return;
    pCurrentStmt.insertPreviousStmt(pNewStmt);
    ((SubpFlowImpl)((HIR_Impl)pCurrentStmt).hirRoot.getFlowRoot().fSubpFlow).
       fIteratorInitiated = false;
  } //insertPreviousStmt

  public static void
    deleteStmt( Stmt pStmt )
  {
    if (pStmt == null)
      return;
    pStmt.deleteThisStmt();
    ((SubpFlowImpl)((HIR_Impl)pStmt).hirRoot.getFlowRoot().fSubpFlow).
       fIteratorInitiated = false;
  } // deleteStmt

  public static void
    addFirstStmt( BlockStmt pBlock, Stmt pStmt )
  {
    if ((pBlock == null)||(pStmt == null))
      return;
    pBlock.addFirstStmt(pStmt);
    ((SubpFlowImpl)((HIR_Impl)pStmt).hirRoot.getFlowRoot().fSubpFlow).
       fIteratorInitiated = false;
  } // addFirstStmt

  /**
   * Add pStmt at the tail of pBlock if the tail of pBlock is
   * not a branch statement. If the tail of pBlock is a branch statement, then
   * insert pStmt in front of the branch statement.
   * @param pBlock BlockStmt in which pStmt is to be added.
   * @param pStmt statement to be added.
   */
  public static void
    addLastStmt( BlockStmt pBlock, Stmt pStmt )
  {
    if ((pBlock == null)||(pStmt == null))
      return;
    //##71 pBlock.addLastStmt(pStmt);
    pBlock.addBeforeBranchStmt(pStmt); //##71
    ((SubpFlowImpl)((HIR_Impl)pStmt).hirRoot.getFlowRoot().fSubpFlow).
       fIteratorInitiated = false;
  } // addLastStmt

  public static void
    setStmt( LabeledStmt pLabeledStmt, Stmt pStmt )
  {
    if (pLabeledStmt == null)
      return;
    pLabeledStmt.setStmt(pStmt);
    ((SubpFlowImpl)((HIR_Impl)pLabeledStmt).hirRoot.getFlowRoot().fSubpFlow).
       fIteratorInitiated = false;
  }
  //##65 END

    /**
    *
    * TypeForLType
    *
    * Returns coins.sym.Type type that corresponds to the given pLtype.
    *
    *
    */
    /* 2004.05.31 S.Noishi
    public static Type typeForLtype(Ltype pLtype, SymRoot pSymRoot) // For SPARC
    {
        if (pLtype.equals(Ltype.I8))
            return pSymRoot.typeShort;
        if (pLtype.equals(Ltype.I16))
            return pSymRoot.typeShort;
        if (pLtype.equals(Ltype.I32))
            return pSymRoot.typeInt;
        else if (pLtype.equals(Ltype.F32))
            return pSymRoot.typeFloat;
        else if (pLtype.equals(Ltype.F64))
            return pSymRoot.typeDouble;
        else
            throw new UnsupportedOperationException(); // To be done.
    }
    */
    /**
    *
    * isCall
    *
    **/
    public static boolean isCall(IR pIR)
    {
        // if (pIR instanceof FunctionExp || pIR instanceof Call)
        if (pIR instanceof FunctionExp ) // 2004.06.01 S.Noishi
            return true;
        return false;
    }
    /**
    *
    * fold
    *
    **/
    public static IR fold(IR pIR, FlowRoot pFlowRoot)
    {
        if (FlowUtil.getChildCount(pIR) == 1)
        {
            if (pFlowRoot.isHirAnalysis())
                return ConstFoldingHir.foldUnary((HIR)pIR, pFlowRoot);
            else
                throw new OptError(); // 2004.06.01 S.Noishi
                // return ConstFoldingLir.foldUnary((LIRNode)pIR, pFlowRoot);
        } else if (FlowUtil.getChildCount(pIR) == 2)
        {
            if (pFlowRoot.isHirAnalysis())
                return ConstFoldingHir.foldBinary((HIR)pIR, pFlowRoot);
            else
                throw new OptError(); // 2004.06.01 S.Noishi
                // return ConstFoldingLir.foldBinary((LIRNode)pIR, pFlowRoot);
        } else
            return pIR;
    }
    /**
    *
    * createConstNodeFromConstNode
    *
    **/
    public static IR createConstNodeFromConstNode(IR pConstNode, FlowRoot pFlowRoot)
    {
        ConstNode lNewConstNode;
        int lTypeKind;
        int NewTypeKind;
        HIR hir = pFlowRoot.hirRoot.hir;
        Sym sym = pFlowRoot.symRoot.sym;

        if (pConstNode instanceof HIR)
        {
            lTypeKind = ((ConstNode)pConstNode).getType().getTypeKind() ;
            if (((ConstNode)pConstNode).isTrueConstNode())
                lNewConstNode = hir.trueNode();
            else if (((ConstNode)pConstNode).isFalseConstNode())
                lNewConstNode = hir.falseNode();
            else if (lTypeKind <= Type.KIND_INT_UPPER_LIM) {
                lNewConstNode = hir.constNode(
                sym.intConst(((ConstNode)pConstNode).getConstSym().longValue(),
                                       ((ConstNode)pConstNode).getType()));
            } else if (lTypeKind == Type.KIND_VECTOR)
                lNewConstNode = (ConstNode)((HIR)pConstNode).copyWithOperands();
            else
                lNewConstNode = hir.constNode(
                               sym.floatConst(((ConstNode)pConstNode).getConstSym().doubleValue(),
                                 ((ConstNode)pConstNode).getType()));

            NewTypeKind = ((ConstNode)lNewConstNode).getType().getTypeKind() ;
            //System.out.println("Debug lTypeKind"+lTypeKind);
            //System.out.println("Debug NewTypeKind"+NewTypeKind);

            return lNewConstNode;
        } else {
            throw new OptError(); // 2004.06.01 S.Noishi
            /*
            LIRFactory lirFactory = pFlowRoot.lirRoot.lirFactory;
            LConst lConstNode = (LConst)pConstNode;
            Ltype lLtype = lConstNode.getLtype();
            Number lValue = ((LConst)pConstNode).getValue();
            if (lValue != null)
            {
                if (lLtype.isInteger())
                {
                    if (lLtype == Ltype.I8)
                        lValue = new Byte(lValue.byteValue());
                    else if (lLtype == Ltype.I16)
                        lValue = new Short(lValue.shortValue());
                    else if (lLtype == Ltype.I32)
                        lValue = new Integer(lValue.intValue());
                    else if (lLtype == Ltype.I64)
                        lValue = new Long(lValue.longValue());
                    else
                        throw new RuntimeException("Unexpected behavior.");
                } else {
                    if (lLtype == Ltype.F32)
                        lValue = new Float(lValue.floatValue());
                    else if (lLtype == Ltype.F64)
                        lValue = new Double(lValue.doubleValue());
                    else if (lLtype == Ltype.F128)
                        ;
                        //    throw new RuntimeException("Unexpected behavior.");
                    //                              lValue = new BigDecimal(lValue.doubleValue());
                }
                return lirFactory.newConst(lLtype, lValue);
            } else // Should be string type
                return lirFactory.newConst(lLtype, lConstNode.getSymbol());
            */
        }
    }
    /**
    *
    * wrapCallNode
    *
    * If pCallNode is a FunctionExp node, wraps the node with an ExpStmt. Does nothing otherwise.
    *
    */
    public static IR wrapCallNode(IR pCallNode, FlowRoot pFlowRoot)
    {
        if (pCallNode instanceof FunctionExp)
            return pFlowRoot.hirRoot.hir.expStmt((FunctionExp)pCallNode);
        else
            return pCallNode;
    }
    /**
    *
    * createConstNodeFromPrimitive
    *
    * @param pLtype integral Ltype.
    *
    **/
    /* 2004.05.31 S.Noishi
    public static LConst createConstNodeFromPrimitive(Ltype pLtype, long pLong, FlowRoot pFlowRoot)
    {
        LIRFactory lirFactory = pFlowRoot.lirRoot.lirFactory;
        Class lClass = getNumberClassForLtype(pLtype);
        Number lNum;
        if (lClass == java.lang.Byte.class)
            lNum = new Byte((byte)pLong);
        else if (lClass == java.lang.Short.class)
            lNum = new Short((short)pLong);
        else if (lClass == java.lang.Integer.class)
            lNum = new Integer((int)pLong);
        else if (lClass == java.lang.Long.class)
            lNum = new Long(pLong);
        else if (lClass == java.math.BigInteger.class)
            lNum = BigInteger.valueOf(pLong);
        else
            throw new OptError("Unexpected Ltype.");
        return lirFactory.newConst(pLtype, lNum);
    }
    */
    /**
    *
    * createConstNodeFromPrimitive
    *
    * @param pLtype floating-point Ltype.
    *
    **/
    /* 2004.05.31 S.Noishi
    public static LConst createConstNodeFromPrimitive(Ltype pLtype,double pDouble,FlowRoot pFlowRoot)
    {
        LIRFactory lirFactory = pFlowRoot.lirRoot.lirFactory;
        Class lClass = getNumberClassForLtype(pLtype);
        Number lNum;
        try
        {
            if (lClass.equals(Class.forName("java.lang.Float")))
                lNum = new Float(pDouble);
            else if (lClass.equals(Class.forName("java.lang.Double")))
                lNum = new Double(pDouble);
            else if (lClass.equals(Class.forName("java.math.BigDecimal")))
                lNum = new BigDecimal(pDouble);
            else
                throw new RuntimeException("Unexpected class.");
        } catch (Exception e)
        {
            throw new RuntimeException("Class not found.");
        }
        return lirFactory.newConst(pLtype, lNum);
    }
    */
    /**
    *
    * createConstNodeFromNumber
    *
    *  The combination pLtype = (I128/F128 or greater) and
    *                              pNumber = (Long/Double or smaller) is not allowed.
    *
    **/
    /* 2004.05.31 S.Noishi
    public static LConst createConstNodeFromNumber(Ltype pLtype, Number pNumber, FlowRoot pFlowRoot)
    {
        LIRFactory lirFactory = pFlowRoot.lirRoot.lirFactory;
        long lIntValue;
        Class lClass = getNumberClassForLtype(pLtype);

        if (FlowUtil.hasJavaPrimitive(pLtype))
            if (pLtype.isInteger())
            {
                lIntValue = pNumber.longValue();
                if (lClass == Byte.class)
                    pNumber = new Byte((byte)lIntValue);
                else if (lClass == Short.class)
                    pNumber = new Short((short)lIntValue);
                else if (lClass == Integer.class)
                    pNumber = new Integer((int)lIntValue);
                else if (lClass == Long.class )
                    pNumber = new Long(lIntValue);
                else if (pNumber instanceof BigDecimal)
                    pNumber = ((BigDecimal)pNumber).toBigInteger();
            } else
            {
                if (lClass == Float.class)
                    pNumber = new Float(pNumber.floatValue());
                else if (lClass == Double.class)
                    pNumber = new Double(pNumber.doubleValue());
                else if (pNumber instanceof BigInteger)
                    pNumber = new BigDecimal((BigInteger)pNumber);
            }
        // else pNumber should already be BigInteger/BigDecimal
        return lirFactory.newConst(pLtype, pNumber);
    }
    */
    /**
    *
    * getNumberClassForLType
    *
    **/
    /* 2004.05.31 S.Noishi
    private static Class getNumberClassForLtype(Ltype pLtype)
    {
        try
        {
            if (pLtype == Ltype.I8)
                return Class.forName("java.lang.Byte");
            else if (pLtype == Ltype.I16)
                return Class.forName("java.lang.Short");
            else if (pLtype == Ltype.I32)
                return Class.forName("java.lang.Integer");
            else if (pLtype == Ltype.I64)
                return Class.forName("java.lang.Long");
            else if (pLtype == Ltype.F32)
                return Class.forName("java.lang.Float");
            else if (pLtype == Ltype.F64)
                return Class.forName("java.lang.Double");
            else if (pLtype.isInteger())
                return Class.forName("java.math.BigInteger");
            else
                return Class.forName("java.math.BigDecimal");
        } catch (ClassNotFoundException e)
        {
            throw new RuntimeException("Class not found.");
        }
    }
    */
    /**
    *
    * ceateConstNodeFromPrimitive
    *
    **/
    public static ConstNode createConstNodeFromPrimitive(long pIntVal,Type pType,FlowRoot pFlowRoot)
    {
        int lTypeKind = pType.getTypeKind();
        HIR lhir = pFlowRoot.hirRoot.hir;
        Sym lsym = pFlowRoot.symRoot.sym;
        if (lTypeKind == Type.KIND_CHAR || lTypeKind == Type.KIND_U_CHAR)
            return lhir.constNode(lsym.charConst((char)pIntVal, pType));
        else
            return lhir.constNode(lsym.intConst(pIntVal, pType));
       //        else
       //            throw new OptError("Unexpected.");
    }
    /**
    *
    * ceateConstNodeFromPrimitive
    *
    **/
    public static ConstNode createConstNodeFromPrimitive(double pFloatVal,
                                                         Type pType,FlowRoot pFlowRoot) {

        return pFlowRoot.hirRoot.hir.constNode(pFlowRoot.symRoot.sym.floatConst(pFloatVal, pType));
    }
    /**
    *
    * isOutOfValueRange
    *
    **/
    public static boolean isOutOfValueRange(Type pType) {

        //##51 return MachineParam.evaluateSize(pType.getTypeKind()) > 8;
        return ((TypeImpl)pType).symRoot.machineParam.evaluateSize(pType.getTypeKind()) > 8; //##51
    }
    /**
    *
    * create0ConstNode
    *
    **/
    public static ConstNode create0ConstNode(Type pType, FlowRoot pFlowRoot)
    {
        HIR hir = pFlowRoot.hirRoot.hir;
        Sym sym = pFlowRoot.symRoot.sym;

        return pType.isInteger() ?
               hir.constNode(sym.intConst(0, pType)) :
               hir.constNode(sym.floatConst(0.0, pType));
    }
}
