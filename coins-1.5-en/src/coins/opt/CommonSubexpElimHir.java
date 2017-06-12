/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.opt;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import coins.FlowRoot; //##60
//##60 import coins.aflow.AssignFlowExpId;
//##60 import coins.aflow.FlowExpId;
//##60 import coins.aflow.FlowResults;
//##60 import coins.aflow.HashBasedFlowExpId; //##57
import coins.flow.SetRefRepr; //##60
//##60 import coins.aflow.SetRefReprList;
import coins.flow.SetRefReprList; //##60
import coins.flow.SubpFlow; //##60
import coins.ir.IR;
import coins.ir.hir.AssignStmt;
import coins.ir.hir.Exp;
import coins.ir.hir.HIR;
import coins.ir.hir.VarNode;
import coins.sym.ExpId; //##57
import coins.sym.FlowAnalSym;
import coins.sym.Type;
import coins.sym.Var;

/**
 * This class performs common subexpresssion elimination operations that are specific to HIR.
 */
public class CommonSubexpElimHir
  extends CommonSubexpElim
{
  public final HIR hir;

  /** fExpTempMap
   * key: ExpId,
   * value: Temporal variable used to replace Exp represented by the key
   *  in a basic block (created for each basic block). */
  //##57 protected Map
  //##57   fExpTempMap;

  /** fGlobalExpTempMap
   * key: ExpId,
   * value: Global mapping (within a subprogram) of ExpId and temporal
   *  variable used to replace the corresponding expression.
   *  This is used for common subexpression elimination invoked
   *  by partial redundancy elimination. */
  protected Map
    fGlobalExpTempMap;

  /** fGlobalTempExpMap
   * key: Temporal variable,
   * value: Global mapping (within a subprogram) of  temporal variable
   *  and ExpId used to replace the corresponding expression.
   *  This is used for common subexpression elimination invoked
   *  by partial redundancy elimination. */
  protected Map
    fGlobalTempExpMap;

  //##60 public CommonSubexpElimHir(FlowResults pResults)
  public CommonSubexpElimHir(FlowRoot pFlowRoot)
  {
    super(pFlowRoot);
    hir = flowRoot.hirRoot.hir;
    //   hir = pOptRoot.hirRoot.hir;
  }

  HIR eliminateSimple(HIR pCompoundNode, HIR pVarNode,
    SubpFlow pSubpFlow, Set pModSyms, boolean pMod) //##60
  {
    if (((HIR)pCompoundNode).getOperator() == HIR.OP_ADDR)
      return null;

    VarNode lNewNode = (VarNode)((VarNode)pVarNode).copyWithOperands();
    OptUtil.replaceNode(pCompoundNode, lNewNode);

    IR lIR = lNewNode;
    if (fDbgLevel > 3) //##58
      flowRoot.ioRoot.dbgOpt1.print(4, "elimSimple ", pVarNode.toString()); //##25
    return pVarNode;

  }

  HIR eliminateComplex(HIR pCompoundNode, HIR pPrevCompoundNode,
    SetRefRepr pPrevCalcSetRefRepr, SetRefReprList pSetRefReprs,
    Map pCompoundToNodeAndSetRefRepr, Map pContainsMap) //##60
  {
    AssignStmt lAssign;
    AssignStmt lInsertedStmt;
    Exp lInsertedRHS;
    Var lInsertedLHSVar;
    VarNode lInsertedLHS;
    VarNode lNewNode, lNewNode0;
    int lOpCode;

    // Offset type variable creation does not seem to be supported by the backend.
    if (((HIR)pCompoundNode).getType().getTypeKind() == Type.KIND_OFFSET)
      return null;

    // No point in substituting address operator
    if (((HIR)pCompoundNode).getOperator() == HIR.OP_ADDR)
      return null;

    // Array object copying shouldn't take place when simply its element is referenced.
    if (((HIR)pCompoundNode).getType().getTypeKind() == Type.KIND_VECTOR &&
        pCompoundNode.getParent().getOperator() == HIR.OP_SUBS)
      return null;

    if (fDbgLevel > 3) //##58
      flowRoot.ioRoot.dbgOpt1.print(4, "elimComplex ",
        pCompoundNode.toString() + " prev " + pPrevCompoundNode.toString() +
        " pPrevCalcSetRefRepr " + pPrevCalcSetRefRepr.toString()
        + " pSetRefReprs " + pSetRefReprs.toString()
        + " pCompoundToNodeAndSetRefRepr "
        + pCompoundToNodeAndSetRefRepr.toString() + " pContainsMap "
        + pContainsMap.toString()); //##25

    if (pPrevCalcSetRefRepr.sets())
      lAssign = (AssignStmt)pPrevCalcSetRefRepr.getIR();
    else
      return null; // Not yet.
    lInsertedLHSVar = symRoot.symTableCurrent.generateVar(((HIR)
      pPrevCompoundNode).getType(), symRoot.subpCurrent);
    lInsertedRHS = (Exp)((HIR)pPrevCompoundNode).copyWithOperands();
    recordTempExpCorrespondence(lInsertedLHSVar, (Exp)pPrevCompoundNode); //##57
    lInsertedLHS = hir.varNode(lInsertedLHSVar);
    lInsertedStmt = hir.assignStmt(lInsertedLHS, lInsertedRHS);
    lAssign.insertPreviousStmt(lInsertedStmt, lAssign.getBlockStmt());

    // Make SetRefRepr and insert to the list and reregister.

    lNewNode = hir.varNode(lInsertedLHSVar);
    lNewNode0 = hir.varNode(lInsertedLHSVar);
    OptUtil.replaceNode(pPrevCompoundNode, lNewNode0);
    OptUtil.replaceNode(pCompoundNode, lNewNode);
    return lInsertedLHS;
  }

  //##25 void registerUseSyms(IR pSubtree, Map pContainsMap, Object pCompound)
  protected void registerUseSyms(HIR pSubtree, Map pContainsMap,
    Object pCompound) //##60
  {
    HIR lHIR;
    FlowAnalSym lFlowAnalSym;
    ExpId lExpId = (ExpId)pCompound; //##60

    for (Iterator lIt = lExpId.getOperandSet().iterator(); lIt.hasNext(); ) {
      lFlowAnalSym = (FlowAnalSym)lIt.next();
      if (pContainsMap.get(lFlowAnalSym) == null) {
        Set lSet = new HashSet();
        lSet.add(lExpId);
        pContainsMap.put(lFlowAnalSym, lSet);
      }
      else
        ((Set)pContainsMap.get(lFlowAnalSym)).add(lExpId);
    }

  }

  Set operandSet(Object o)
  {
    return ((ExpId)o).getOperandSet(); //##60
  }

  void reregisterSubexps(Object pCompound, SetRefRepr pSetRefRepr,
    Map pCompoundToNodeAndSetRefRepr)
  {
  }

  //##57 BEGIN

  public void
    recordTempExpCorrespondence(Var pTempVar, Exp pExp)
  {
    if (fDbgLevel > 3) //##58
      flowRoot.ioRoot.dbgOpt1.print(4, " recordTempExpCorrespondence",
        pTempVar.getName() + " " + pExp.toStringWithChildren());
    //##60 flowRoot.aflow.getSubpFlow().setExpOfTemp(pTempVar, (Exp)pExp.copyWithOperands());
    flowRoot.flow.getSubpFlow().setExpOfTemp(pTempVar,
      (Exp)pExp.copyWithOperands()); //##60
    ExpId lExpId = pExp.getExpId();
    if (lExpId != null) {
      fGlobalExpTempMap.put(lExpId, pTempVar);
      fGlobalTempExpMap.put(pTempVar, lExpId);
      if (fDbgLevel > 3) //##58
        flowRoot.ioRoot.dbgOpt1.print(4, " expId " + lExpId.toStringShort());
    }
  }
//##57 END

}
