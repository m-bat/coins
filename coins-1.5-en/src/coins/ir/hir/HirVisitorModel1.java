/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import java.util.Iterator;

import coins.HirRoot;
import coins.IoRoot;
import coins.SymRoot;

//========================================//
//                        Oct. 2002.

/** HirVisitorModel1
 *  Visitor model for processing HIR nodes where
 *  visiting procedure is written in the method visit of this class
 *  and each method of the form atXxx called from acceptor of HIR class
 *  does a processing without writing traverse procedure.
 *  (This visitor model does not call visitChildren
 *   that is called from HirVisitorModel2.)
 *  User may extends this class to do their own processing
 *  by overriding visit method to visit nodes in concern and
 *  overriding some methods of the form atXxx in concern.
 *  Example of the extension of HirVisitorModel1: PrintStmtVisitor.java
 *  See HirVisitorModel2.
**/
public class
HirVisitorModel1 implements HirVisitor
{

  public final HirRoot hirRoot;
  public final SymRoot symRoot;
  public final IoRoot  ioRoot;
  protected final int fDbgLevel; //##58

public
HirVisitorModel1( HirRoot pHirRoot )
{
  hirRoot = pHirRoot;
  symRoot = pHirRoot.symRoot;
  ioRoot  = pHirRoot.ioRoot;
  fDbgLevel = ioRoot.dbgHir.getLevel(); //##58
}

/** visit
 *  Procedure to visit nodes of pHir.
 *  You may override this method to selectively visit
 *  nodes in concern.
 *  If there is a probability of setting programRoot as pHir,
 *  Program node should be selected as a node to be accepted
 *  because, elements of subprogram definition list is not
 *  traversed by HirIterator.
 *  @param pHir root of subtree whose nodes are to be visited.
**/
public void
visit( HIR pHir )
{
  HirIterator lHirIterator;
  HIR lNode, lStmt;

  for (lHirIterator = hirRoot.hir.hirIterator(pHir);
       lHirIterator.hasNext(); ) {
    lNode = lHirIterator.next();
    if (lNode != null) {
      lNode.accept(this);
    }
  }
} // visit

public void atProgram(coins.ir.hir.Program p) {
  ioRoot.printOut.println(" atProgram " + p.toString());
  // Traverse SubpDefinition list of the Program because
  // HirIterator does not traverse the elements of the list.
  HIR lHir;
  for (Iterator lIterator = p.getSubpDefinitionList().iterator();
       lIterator.hasNext(); ) {
    lHir = (HIR)lIterator.next();
    if (lHir != null)
      visit(lHir);
  }
}
public void atSubpDefinition(coins.ir.hir.SubpDefinition p) {}

public void atHirList(coins.ir.hir.HirList p) {
  // Traverse elements of the list because
  // HirIterator does not traverse the elements of the list.
  HIR lHir;
  for (Iterator lIterator = p.iterator(); lIterator.hasNext(); ) {
    lHir = (HIR)lIterator.next();
    if (lHir != null)
      visit(lHir);
  }
} // atHirList

public void atIrList(coins.ir.IrList p) {}
public void atHirSeq(coins.ir.hir.HirSeq p) {}
public void atInfNode(coins.ir.hir.InfNode p) {}
public void atInfStmt(coins.ir.hir.InfStmt p) {}
public void atVarNode(coins.ir.hir.VarNode p) {}
public void atElemNode(coins.ir.hir.ElemNode p) {}
public void atSubpNode(coins.ir.hir.SubpNode p) {}
public void atTypeNode(coins.ir.hir.TypeNode p) {}
public void atConstNode(coins.ir.hir.ConstNode p) {}
public void atLabelNode(coins.ir.hir.LabelNode p) {}
public void atSymNode(coins.ir.hir.SymNode p) {}
public void atNullNode(coins.ir.hir.NullNode p) {}
public void atLabelDef(coins.ir.hir.LabelDef p) {}
public void atExp(coins.ir.hir.Exp p) {}
public void atSubscriptedExp(coins.ir.hir.SubscriptedExp p) {}
public void atQualifiedExp(coins.ir.hir.QualifiedExp p) {}
public void atPointedExp(coins.ir.hir.PointedExp p) {}
public void atFunctionExp(coins.ir.hir.FunctionExp p) {}
public void atAssignStmt(coins.ir.hir.AssignStmt p) {}
public void atIfStmt(coins.ir.hir.IfStmt p) {}
public void atWhileStmt(coins.ir.hir.WhileStmt p) {}
public void atForStmt(coins.ir.hir.ForStmt p) {}
public void atUntilStmt(coins.ir.hir.UntilStmt p) {}
public void atRepeatStmt(coins.ir.hir.RepeatStmt p) {}
public void atIndexedLoopStmt(coins.ir.hir.IndexedLoopStmt p) {}
public void atLoopStmt(coins.ir.hir.LoopStmt p) {}
public void atLabeledStmt(coins.ir.hir.LabeledStmt p) {}
public void atBlockStmt(coins.ir.hir.BlockStmt p) {}
public void atReturnStmt(coins.ir.hir.ReturnStmt p) {}
public void atJumpStmt(coins.ir.hir.JumpStmt p) {}
public void atSwitchStmt(coins.ir.hir.SwitchStmt p) {}
public void atExpStmt(coins.ir.hir.ExpStmt p) {}
public void atPhiExp(coins.ir.hir.PhiExp p) {}
public void atAsmStmt(coins.ir.hir.AsmStmt p) {} //##70

} // HirVisitorModel1

