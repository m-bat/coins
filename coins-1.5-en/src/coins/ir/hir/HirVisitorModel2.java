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

/** HirVisitorModel2
 *  Visitor model for processing HIR nodes where
 *  each acceptor calls visitChildren method to traverse its children.
 *  (This point differs from HirVisitorModel1.)
 *  User may extends this class to do their own processing
 *  by overriding some methods of the form atXxx in concern.
 *  Example of the extension of HirVisitorModel2: PrintVisitor.java
 *  @see HirVisitorModel1
**/
public class
HirVisitorModel2 implements HirVisitor
{

  public final HirRoot hirRoot;
  public final SymRoot symRoot;
  public final IoRoot  ioRoot;
  protected final int fDbgLevel;  //##58

public
HirVisitorModel2( HirRoot pHirRoot )
{
  hirRoot = pHirRoot;
  symRoot = pHirRoot.symRoot;
  ioRoot  = pHirRoot.ioRoot;
  fDbgLevel = ioRoot.dbgHir.getLevel(); //##58
}

public void
visit( HIR pHir )
{
  if (pHir != null) {
    pHir.accept(this);
  }
} // visit

public void
visitChildren( HIR pHir )
{
  HIR lChildNode1, lChildNode2, lChildNode;
  int lChildCount, i;
  if (pHir == null)
    return;
  lChildCount = pHir.getChildCount();
  if (lChildCount == 0)
    return;
  lChildNode1 = (HIR)pHir.getChild1();
  lChildNode2 = (HIR)pHir.getChild2();
  if (lChildNode1 != null) {
    lChildNode1.accept(this);
  }
  if (lChildNode2 != null) {
    lChildNode2.accept(this);
  }
  if (lChildCount > 2) {
    for (i = 3; i <= lChildCount; i++) {
      lChildNode = (HIR)pHir.getChild(i);
      if (lChildNode != null) {
        lChildNode.accept(this);
      }
    }
  }
} // visitChildren

public void atProgram(coins.ir.hir.Program p) { visitChildren(p); }
public void atSubpDefinition(coins.ir.hir.SubpDefinition p) { visitChildren(p); }
public void atBlockStmt(coins.ir.hir.BlockStmt p) {
  // Traverse statements of the block because
  // visitChildren does not traverse the statements of the block.
  for (Stmt lStmt = p.getFirstStmt(); lStmt != null;
       lStmt = lStmt.getNextStmt()) {
    lStmt.accept(this);
  }
} // atBlockStmt

public void atHirList(coins.ir.hir.HirList p) {
  // Traverse elements of the list because
  // visitChildren does not traverse the elements of the list.
  HIR lHir;
  for (Iterator lIterator = p.iterator(); lIterator.hasNext(); ) {
    lHir = (HIR)lIterator.next();
    if (lHir != null) {
      lHir.accept(this);
    }
  }
} // atHirList

public void atIrList(coins.ir.IrList p) { visitChildren((HIR)p); }
public void atHirSeq(coins.ir.hir.HirSeq p) { visitChildren((HIR)p); }
public void atInfNode(coins.ir.hir.InfNode p) {  }
public void atInfStmt(coins.ir.hir.InfStmt p) { visitChildren(p); }
public void atVarNode(coins.ir.hir.VarNode p) {  }
public void atElemNode(coins.ir.hir.ElemNode p) {  }
public void atSubpNode(coins.ir.hir.SubpNode p) {  }
public void atTypeNode(coins.ir.hir.TypeNode p) {  }
public void atConstNode(coins.ir.hir.ConstNode p) {  }
public void atLabelNode(coins.ir.hir.LabelNode p) {  }
public void atSymNode(coins.ir.hir.SymNode p) {  }
public void atNullNode(coins.ir.hir.NullNode p) {  }
public void atLabelDef(coins.ir.hir.LabelDef p) { visitChildren(p); }
public void atExp(coins.ir.hir.Exp p) { visitChildren(p); }
public void atSubscriptedExp(coins.ir.hir.SubscriptedExp p) { visitChildren(p); }
public void atQualifiedExp(coins.ir.hir.QualifiedExp p) { visitChildren(p); }
public void atPointedExp(coins.ir.hir.PointedExp p) { visitChildren(p); }
public void atFunctionExp(coins.ir.hir.FunctionExp p) { visitChildren(p); }
public void atAssignStmt(coins.ir.hir.AssignStmt p) { visitChildren(p); }
public void atIfStmt(coins.ir.hir.IfStmt p) { visitChildren(p); }
public void atWhileStmt(coins.ir.hir.WhileStmt p) { visitChildren(p); }
public void atForStmt(coins.ir.hir.ForStmt p) { visitChildren(p); }
public void atUntilStmt(coins.ir.hir.UntilStmt p) { visitChildren(p); }
public void atRepeatStmt(coins.ir.hir.RepeatStmt p) { visitChildren(p); }
public void atIndexedLoopStmt(coins.ir.hir.IndexedLoopStmt p) { visitChildren(p); }
public void atLoopStmt(coins.ir.hir.LoopStmt p) { visitChildren(p); }
public void atLabeledStmt(coins.ir.hir.LabeledStmt p) { visitChildren(p); }
public void atReturnStmt(coins.ir.hir.ReturnStmt p) { visitChildren(p); }
public void atJumpStmt(coins.ir.hir.JumpStmt p) { visitChildren(p); }
public void atSwitchStmt(coins.ir.hir.SwitchStmt p) { visitChildren(p); }
public void atExpStmt(coins.ir.hir.ExpStmt p) { visitChildren(p); }
public void atPhiExp(coins.ir.hir.PhiExp p) { visitChildren(p); }
public void atAsmStmt(coins.ir.hir.AsmStmt p) { visitChildren(p); } //##70

} // HirVisitorMode2

