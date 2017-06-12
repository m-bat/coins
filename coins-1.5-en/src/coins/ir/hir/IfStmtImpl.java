/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.HirRoot;
import coins.ir.IR;
import coins.sym.Label;

/**
 *  If-statement class.
 *  If then-part is not given, NullNode with label is inserted. (2001/6)
**/
public class
IfStmtImpl extends StmtImpl implements IfStmt
{

/* Constructor */
public
IfStmtImpl( HirRoot pHirRoot, Exp pCondition, Stmt pThenPart, Stmt pElsePart )
{
  super(pHirRoot);
  Label lThenLabel, lElseLabel, lEndIfLabel;
  LabeledStmt lThenPart = null, lElsePart = null, lEndIfPart;
  if (pThenPart != null)
    lThenPart = pThenPart.getLabeledStmt();
  if (lThenPart == null) {
    lThenLabel = hirRoot.symRoot.symTableCurrent.generateLabel();
    lThenPart  = (LabeledStmt)(new LabeledStmtImpl(hirRoot, lThenLabel, pThenPart));
  }else { // Then part is a labeled statement.
    lThenLabel = lThenPart.getLabel();
    if (lThenLabel.getLabelKind() != Label.UNCLASSIFIED_LABEL) {
      lThenLabel = hirRoot.symRoot.symTableCurrent.generateLabel();
      lThenPart.attachLabelAsFirstOne(lThenLabel);
    }
  }
  lThenLabel.setLabelKind(Label.THEN_LABEL);
  lThenLabel.setOriginHir(this);
  if (pElsePart != null)
    lElsePart = pElsePart.getLabeledStmt();
  if (lElsePart == null) {
    lElseLabel = hirRoot.symRoot.symTableCurrent.generateLabel();
    lElsePart  = (LabeledStmt)(new LabeledStmtImpl(hirRoot, lElseLabel, pElsePart));
  }else { // Else part is a labeled statement.
    lElseLabel = lElsePart.getLabel();
    if (lElseLabel.getLabelKind() != Label.UNCLASSIFIED_LABEL) {
      lElseLabel = hirRoot.symRoot.symTableCurrent.generateLabel();
      lElsePart.attachLabelAsFirstOne(lElseLabel);
    }
  }
  lElseLabel.setLabelKind(Label.ELSE_LABEL);
  lElseLabel.setOriginHir(this);
  lEndIfLabel = hirRoot.symRoot.symTableCurrent.generateLabel();
  lEndIfPart  = (LabeledStmt)(new LabeledStmtImpl(hirRoot, lEndIfLabel, null));
  lEndIfLabel.setLabelKind(Label.END_IF_LABEL);
  lEndIfLabel.setOriginHir(this);
  fOperator = OP_IF;
  fAdditionalChild = new HIR[2];
  fChildCount = 4;
  setChildren(pCondition, lThenPart, lElsePart, lEndIfPart);
  fMultiBlock = true;
  fType = hirRoot.symRoot.typeVoid;
} // IfStmtImpl

    /** getIfCondition
     *  Get the condition part of if-statement.
     *  "this" should be IfStmt statement built by buildIf.
     *  @return the pCondition expression subtree if "this" is
     *      if-statement, otherwise return null.
     **/
public Exp
getIfCondition() {
    return (Exp)fChildNode1;
}

public void
setIfCondition(Exp pCondition) {
  setChild1((IR)pCondition);
}

public LabeledStmt
getThenPart() {
    return (LabeledStmt)fChildNode2;
}

public LabeledStmt
getElsePart() {
    return (LabeledStmt)fAdditionalChild[0];
}

public Label
getEndLabel() {
    return (Label)((LabeledStmt)fAdditionalChild[1]).getLabel();
}

public void
addToThenPart( Stmt pStmt, boolean pBeforeBranch ) {
  LabeledStmt lThenPart;
  Stmt lStmtBody = null, lNewBody = null;
  if (pStmt == null)
    return;
  lThenPart = (LabeledStmt)getThenPart();
  lStmtBody = lThenPart.getStmt();
  if (lStmtBody != null)
    lNewBody = lStmtBody.combineStmt(pStmt, pBeforeBranch);
  if (lNewBody != lStmtBody)
    lThenPart.replaceSource2(lNewBody);
} // addToThenPart

public void
addToElsePart( Stmt pStmt, boolean pBeforeBranch ) {
  LabeledStmt lElsePart;
  Stmt lStmtBody = null, lNewBody = null;
  if (pStmt == null)
    return;
  lElsePart = (LabeledStmt)getElsePart();
  lStmtBody = lElsePart.getStmt();
  if (lStmtBody != null)
    lNewBody = lStmtBody.combineStmt(pStmt, pBeforeBranch);
  if (lNewBody != lStmtBody)
    lElsePart.replaceSource2(lNewBody);
} // addToElsePart

public void
replaceThenPart( LabeledStmt pNewThenPart ) {
  if (getThenPart() != null)
    getThenPart().getLabel().setLabelKind(Label.UNCLASSIFIED_LABEL);
  Label lLabel = pNewThenPart.getLabel();
  replaceSource2(pNewThenPart);
  lLabel.setLabelKind(Label.THEN_LABEL);
  lLabel.setOriginHir(this);
} // replaceThenPart

public void
replaceElsePart( LabeledStmt pNewElsePart ) {
  if (getElsePart() != null)
    getElsePart().getLabel().setLabelKind(Label.UNCLASSIFIED_LABEL);
  Label lLabel = pNewElsePart.getLabel();
  replaceSource(3, pNewElsePart);
  lLabel.setLabelKind(Label.ELSE_LABEL);
  lLabel.setOriginHir(this);
} // replaceElsePart

/**Combine pStmt with conditional expression part pCond
 * of control statement so that pStmt should be executed before
 * pCond.
 * @param pStmt statement to be executed before pCond.
 * @param pCond conditional expression to be combined with pStmt.
 */
public void
combineWithConditionalExp(Stmt pStmt, HIR pCond) //##53
{
  hirRoot.ioRoot.dbgHir.print(4, "combineWithConditionalExp ",
              toStringShort() + " Stmt " + pStmt.toStringWithChildren()
              + " Cond " + pCond.toStringWithChildren()); //##71
  insertPreviousStmt(pStmt);
  return;
} // combineWithConditionalExp


public void
accept( HirVisitor pVisitor )
{
  pVisitor.atIfStmt(this);
}

} // IfStmtImpl class
