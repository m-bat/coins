/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.HirRoot;

//========================================//
//                        Oct. 2002.

/** PrintStmtVisitor
 *  Example of the use of Visitor for printing HIR statement
 *  (Extends HirVisitorModel1.)
**/
public class
PrintStmtVisitor extends coins.ir.hir.HirVisitorModel1
{

public
PrintStmtVisitor( HirRoot pHirRoot )
{
  super(pHirRoot);
}

public void
visit( HIR pHir )
{
  coins.ir.hir.HirIterator lHirIterator;
  HIR lNode, lStmt;

  if (fDbgLevel > 0) //##58
    ioRoot.dbgHir.print(2, "Visit every Stmt nodes", "by PrintStmtVisitor\n");
  for (lHirIterator = hirRoot.hir.hirIterator(pHir);
       lHirIterator.hasNext(); ) {
    lNode = lHirIterator.next();
    if ((lNode instanceof coins.ir.hir.Stmt)||
        (lNode instanceof coins.ir.hir.SubpDefinition)||
        (lNode instanceof coins.ir.hir.Program)) { // Program should be
                                                   // traversed.
      lNode.accept(this);
    }
  }
} // visit

public void atSubpDefinition(coins.ir.hir.SubpDefinition p) {
  ioRoot.printOut.println(" atSubpDefinition " +
      p.getSubpSym().getName()); }
public void atInfStmt(coins.ir.hir.InfStmt p) {
  ioRoot.printOut.println(" atInfStmt " + p.toString());}
public void atAssignStmt(coins.ir.hir.AssignStmt p) {
  ioRoot.printOut.println(" atAssignStmt " + p.toString());}
public void atIfStmt(coins.ir.hir.IfStmt p) {
  ioRoot.printOut.println(" atIfStmt " + p.toString());}
public void atWhileStmt(coins.ir.hir.WhileStmt p) {
  ioRoot.printOut.println(" atWhileStmt " + p.toString());}
public void atForStmt(coins.ir.hir.ForStmt p) {
  ioRoot.printOut.println(" atForStmt " + p.toString());}
public void atUntilStmt(coins.ir.hir.UntilStmt p) {
  ioRoot.printOut.println(" atUntilStmt " + p.toString());}
public void atLabeledStmt(coins.ir.hir.LabeledStmt p) {
  ioRoot.printOut.println(" atLabeledStmt " + p.toString());}
public void atBlockStmt(coins.ir.hir.BlockStmt p) {
  ioRoot.printOut.println(" atBlockStmt " + p.toString());}
public void atReturnStmt(coins.ir.hir.ReturnStmt p) {
  ioRoot.printOut.println(" atReturnStmt " + p.toString());}
public void atJumpStmt(coins.ir.hir.JumpStmt p) {
  ioRoot.printOut.println(" atJumpStmt " + p.toString());}
public void atSwitchStmt(coins.ir.hir.SwitchStmt p) {
  ioRoot.printOut.println(" atSwitchStmt " + p.toString());}
public void atExpStmt(coins.ir.hir.ExpStmt p) {
  ioRoot.printOut.println(" atExpStmt " + p.toString());}

} // PrintVisitor

