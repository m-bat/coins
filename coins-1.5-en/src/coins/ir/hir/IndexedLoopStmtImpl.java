/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.HirRoot;
import coins.IoRoot;
import coins.sym.Label;
import coins.sym.SymTable;
import coins.sym.Type;
import coins.sym.Var;

/**
 *  IndexedLoop-statement class.
**/
public class
IndexedLoopStmtImpl extends LoopStmtImpl implements IndexedLoopStmt
{
  protected Var fLoopIndex;
  protected Exp fStartValue;
  protected Exp fEndValue;
  protected Exp fStepValue;
  protected boolean fUpward;
public
IndexedLoopStmtImpl(
    HirRoot pHirRoot,
    Var     pLoopIndex,
    Exp     pStartValue,
    Exp     pEndValue,
    Exp     pStepValue,
    boolean pUpward,
    Stmt    pLoopBody)
{
  super(pHirRoot);
  fOperator = HIR.OP_INDEXED_LOOP;
  Stmt  lInitStmt, lInitPart, lStepPart;
  Exp   lCondition, lStartValue, lStepValue, lEndValue, lLoopCount;
  Label lLoopBackLabel, lLoopStepLabel, lLoopEndLabel;
  Var   lStepVar = null, lEndVar = null;
  HIR   hir = pHirRoot.hir;
  Type  lType = pLoopIndex.getSymType();
  int   lTypeKind = lType.getTypeKind();
  int   lCmpOperator, lStepOperator;
  SymTable lSymTable = pHirRoot.symRoot.symTableCurrentSubp;
  lLoopBackLabel = lSymTable.generateLabel();
  lLoopStepLabel = lSymTable.generateLabel();
  lLoopEndLabel  = lSymTable.generateLabel();
  if (fDbgLevel > 0) //##58
    hirRoot.ioRoot.dbgHir.print(5, "indexedLoopStmtImpl(",
      pLoopIndex.toString() + ", " + pStartValue.toString() + ", " +
      pEndValue.toString( ) + ", " +
      IoRoot.toStringObject(pStepValue) + " upward " + pUpward + ")");
  if ((lTypeKind <= Type.KIND_INT_UPPER_LIM)&&
      (lTypeKind >= Type.KIND_SHORT)) {
    lInitStmt = hirRoot.hir.assignStmt(hir.varNode(pLoopIndex), pStartValue);
    if (pStepValue == null) // If step value is not given, assume 1.
      lStepValue = hir.constNode(pHirRoot.symRoot.intConst1);
    else if (pStepValue instanceof ConstNode)
      lStepValue = pStepValue;
    else {
      lStepVar = lSymTable.generateVar(lType);
      lStepValue = hir.varNode(lStepVar);
    }
    if (pEndValue instanceof ConstNode)
      lEndValue = pEndValue;
    else {
      lEndVar = lSymTable.generateVar(lType);
      lEndValue = hir.varNode(lEndVar);
    }
    if ((lStepVar == null)&&(lEndVar == null))
      lInitPart = lInitStmt;
    else {
      lInitPart = hir.blockStmt(lInitStmt);
      if (lEndVar != null)
        ((BlockStmt)lInitPart).addLastStmt(
            hir.assignStmt(hir.varNode(lEndVar), pEndValue));
      if (lStepVar != null)
        ((BlockStmt)lInitPart).addLastStmt(
            hir.assignStmt(hir.varNode(lStepVar), pStepValue));
    }
    if (pUpward) {
      lCmpOperator  = HIR.OP_CMP_LE;
      lStepOperator = HIR.OP_ADD;
    }else {
      lCmpOperator  = HIR.OP_CMP_GE;
      lStepOperator = HIR.OP_SUB;
    }
    lCondition = hir.exp(lCmpOperator, hir.varNode(pLoopIndex),
                         lEndValue);
    lStepPart = hir.assignStmt(hir.varNode(pLoopIndex),
                               hir.exp(lStepOperator, hir.varNode(pLoopIndex),
                                       lStepValue));
  }else { // LoopIndex is not integer.
    // Compute loop count and control by a generated integer loop index.
    // Loop count is MAX( INT( (<expr2> - <expr1> + <expr3>) / <expr3>), 0)
    Var lLoopIndexInt = lSymTable.generateVar(pHirRoot.symRoot.typeInt);
    Var lEndValueInt = lSymTable.generateVar(pHirRoot.symRoot.typeInt);
    Exp lCountF, lCountInt;
    // Adjust expression type.
    if (pStartValue.getType() == lType)
      lStartValue = pStartValue;
    else
      lStartValue = hir.convExp(lType, pStartValue);
    if (pEndValue.getType() == lType)
      lEndValue = pEndValue;
    else
      lEndValue = hir.convExp(lType, pEndValue);
    if (pStepValue == null)  // If step value is not given, assume 1.
      lStepValue = hir.constNode(pHirRoot.symRoot.sym.floatConst(1.0, lType));
    else if (pStepValue.getType() == lType)
      lStepValue = pStepValue;
    else
      lStepValue = hir.convExp(lType, pStepValue);
    if (pUpward) {
    lCountF = hir.exp(HIR.OP_DIV,
               hir.exp(HIR.OP_ADD,
                hir.exp(HIR.OP_SUB, lEndValue, lStartValue),
                lStepValue),
               (Exp)lStepValue.copyWithOperands());
    }else {
      lCountF = hir.exp(HIR.OP_DIV,
                 hir.exp(HIR.OP_ADD,
                  hir.exp(HIR.OP_SUB, lStartValue, lEndValue),
                  lStepValue),
                 (Exp)lStepValue.copyWithOperands());
    }
    lCountInt = hir.convExp(hirRoot.symRoot.typeInt, lCountF);
    lInitPart = hirRoot.hir.assignStmt(hir.varNode(pLoopIndex), pStartValue);
    ((BlockStmt)lInitPart).addLastStmt(
      hir.assignStmt(hir.varNode(lLoopIndexInt),
                     hir.constNode(pHirRoot.symRoot.intConst1)));
    ((BlockStmt)lInitPart).addLastStmt(
      hir.assignStmt(hir.varNode(lEndValueInt), lCountInt));
    if ((pStepValue != null)&&
        (!(pStepValue instanceof ConstNode))) {
      lStepVar = lSymTable.generateVar(lType);
      ((BlockStmt)lInitPart).addLastStmt(
          hir.assignStmt(hir.varNode(lStepVar), lStepValue));
      lStepValue = hir.varNode(lStepVar);
    }
    if (pUpward) {
      lCmpOperator  = HIR.OP_CMP_LE;
      lStepOperator = HIR.OP_ADD;
    }else {
      lCmpOperator  = HIR.OP_CMP_GE;
      lStepOperator = HIR.OP_SUB;
    }
    lCondition = hir.exp(HIR.OP_CMP_LE,
                         hir.varNode(lLoopIndexInt),
                         hir.varNode(lEndValueInt));
    lStepPart = hir.blockStmt(hir.assignStmt(hir.varNode(pLoopIndex),
                               hir.exp(lStepOperator,
                                       hir.varNode(pLoopIndex),
                                       lStepValue)));
    ((BlockStmt)lStepPart).addLastStmt(
      hir.assignStmt(hir.varNode(lLoopIndexInt),
        hir.exp(HIR.OP_ADD,
          hir.varNode(lLoopIndexInt),
          hir.constNode(pHirRoot.symRoot.intConst1))));
  }
  setChildrenOfLoop(lInitPart, lLoopBackLabel, null,
                  lCondition, pLoopBody,
                  lLoopStepLabel, null, lStepPart, lLoopEndLabel,
                  null);
  fLoopIndex  = pLoopIndex;
  fStartValue = (Exp)pStartValue.copyWithOperands();
  fEndValue   = (Exp)lEndValue.copyWithOperands();
  fStepValue  = (Exp)lStepValue.copyWithOperands();
  fUpward     = pUpward;
  fType = pHirRoot.symRoot.typeVoid;
} // IndexedLoopStmtImpl

public Var
getLoopIndex()
{
  return fLoopIndex;
}

public Exp
getStartValue()
{
  return fStartValue;
}

public Exp
getEndValue()
{
  return fEndValue;
}
public Exp
getStepValue()
{
  return fStepValue;
}

public boolean
isUpward()
{
  return fUpward;
}

public void
accept( HirVisitor pVisitor )
{
  pVisitor.atIndexedLoopStmt(this);
}

} // IndexedLoopStmtImpl class
