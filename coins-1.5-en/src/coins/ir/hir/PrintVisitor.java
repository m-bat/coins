/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import java.util.Iterator;

import coins.HirRoot;

//========================================//
//                        Oct. 2002.

/** PrintVisitor
 *  Visitor for printing HIR
 *  (Example of extending HirVisitorModel2.)
**/
public class
PrintVisitor extends HirVisitorModel2
{

public
PrintVisitor( HirRoot pHirRoot )
{
  super(pHirRoot);
}

public void atProgram(coins.ir.hir.Program p) {
  ioRoot.printOut.println(" atProgram " + p.toString()); visitChildren(p); }
public void atSubpDefinition(coins.ir.hir.SubpDefinition p) {
  ioRoot.printOut.println(" atSubpDefinition " +
        p.getSubpSym().getName()); visitChildren(p);
}

public void atBlockStmt(coins.ir.hir.BlockStmt p) {
  ioRoot.printOut.println(" atBlockStmt " + p.toString()); visitChildren(p);
  for (Stmt lStmt = p.getFirstStmt(); lStmt != null;
       lStmt = lStmt.getNextStmt()) {
    lStmt.accept(this);
  }
}

public void atHirList(coins.ir.hir.HirList p) {
  ioRoot.printOut.println(" atHirList " + p.toString());
  // Traverse elements of the list because
  // visitChildren does not traverse the elements of the list.
  HIR lHir;
  for (Iterator lIterator = p.iterator(); lIterator.hasNext(); ) {
    lHir = (HIR)lIterator.next();
    if (lHir != null) {
      visit(lHir);
    }
  }
} // atHirList

public void atIrList(coins.ir.IrList p) {
  ioRoot.printOut.println(" atIrList " + p.toString()); visitChildren((HIR)p); }
public void atHirSeq(coins.ir.hir.HirSeq p) {
  ioRoot.printOut.println(" atHirSeq " + p.toString()); visitChildren((HIR)p); }
public void atInfNode(coins.ir.hir.InfNode p) {
  ioRoot.printOut.println(" atInfNode " + p.toString()); visitChildren(p); }
public void atInfStmt(coins.ir.hir.InfStmt p) {
  ioRoot.printOut.println(" atInfStmt " + p.toString()); visitChildren(p); }
public void atVarNode(coins.ir.hir.VarNode p) {
  ioRoot.printOut.println(" atVarNode " + p.toString()); visitChildren(p); }
public void atElemNode(coins.ir.hir.ElemNode p) {
  ioRoot.printOut.println(" atElemNode " + p.toString()); visitChildren(p); }
public void atSubpNode(coins.ir.hir.SubpNode p) {
  ioRoot.printOut.println(" atSubpNode " + p.toString()); visitChildren(p); }
public void atTypeNode(coins.ir.hir.TypeNode p) {
  ioRoot.printOut.println(" atTypeNode " + p.toString()); visitChildren(p); }
public void atConstNode(coins.ir.hir.ConstNode p) {
  ioRoot.printOut.println(" atConstNode " + p.toString()); visitChildren(p); }
public void atLabelNode(coins.ir.hir.LabelNode p) {
  ioRoot.printOut.println(" atLabelNode " + p.toString()); visitChildren(p); }
public void atSymNode(coins.ir.hir.SymNode p) {
  ioRoot.printOut.println(" atSymNode " + p.toString()); visitChildren(p); }
public void atNullNode(coins.ir.hir.NullNode p) {
  ioRoot.printOut.println(" atNullNode " + p.toString()); visitChildren(p); }
public void atLabelDef(coins.ir.hir.LabelDef p) {
  ioRoot.printOut.println(" atLabelDef " + p.toString()); visitChildren(p); }
public void atExp(coins.ir.hir.Exp p) {
  ioRoot.printOut.println(" atExp " + p.toString()); visitChildren(p); }
public void atSubscriptedExp(coins.ir.hir.SubscriptedExp p) {
  ioRoot.printOut.println(" atSubscriptedExp " + p.toString()); visitChildren(p); }
public void atQualifiedExp(coins.ir.hir.QualifiedExp p) {
  ioRoot.printOut.println(" atQualifiedExp " + p.toString()); visitChildren(p); }
public void atPointedExp(coins.ir.hir.PointedExp p) {
  ioRoot.printOut.println(" atPointedExp " + p.toString()); visitChildren(p); }
public void atFunctionExp(coins.ir.hir.FunctionExp p) {
  ioRoot.printOut.println(" atFunctionExp " + p.toString()); visitChildren(p); }
public void atAssignStmt(coins.ir.hir.AssignStmt p) {
  ioRoot.printOut.println(" atAssignStmt " + p.toString()); visitChildren(p); }
public void atIfStmt(coins.ir.hir.IfStmt p) {
  ioRoot.printOut.println(" atIfStmt " + p.toString()); visitChildren(p); }
public void atWhileStmt(coins.ir.hir.WhileStmt p) {
  ioRoot.printOut.println(" atWhileStmt " + p.toString()); visitChildren(p); }
public void atForStmt(coins.ir.hir.ForStmt p) {
  ioRoot.printOut.println(" atForStmt " + p.toString()); visitChildren(p); }
public void atUntilStmt(coins.ir.hir.UntilStmt p) {
  ioRoot.printOut.println(" atUntilStmt " + p.toString()); visitChildren(p); }
public void atLabeledStmt(coins.ir.hir.LabeledStmt p) {
  ioRoot.printOut.println(" atLabeledStmt " + p.toString()); visitChildren(p); }
public void atReturnStmt(coins.ir.hir.ReturnStmt p) {
  ioRoot.printOut.println(" atReturnStmt " + p.toString()); visitChildren(p); }
public void atJumpStmt(coins.ir.hir.JumpStmt p) {
  ioRoot.printOut.println(" atJumpStmt " + p.toString()); visitChildren(p); }
public void atSwitchStmt(coins.ir.hir.SwitchStmt p) {
  ioRoot.printOut.println(" atSwitchStmt " + p.toString()); visitChildren(p); }
public void atExpStmt(coins.ir.hir.ExpStmt p) {
  ioRoot.printOut.println(" atExpStmt " + p.toString()); visitChildren(p); }
public void atPhiExp(coins.ir.hir.PhiExp p) {
  ioRoot.printOut.println(" atPhiExp " + p.toString()); visitChildren(p); }

} // PrintVisitor

