/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;
//========================================//
//                        Oct. 2002.

/** HirVisitor
 *  Visitor for processing HIR nodes
**/
public interface
HirVisitor
{
public void atProgram(coins.ir.hir.Program p);
public void atSubpDefinition(coins.ir.hir.SubpDefinition p);
public void atHirList(coins.ir.hir.HirList p);
public void atIrList(coins.ir.IrList p);
public void atHirSeq(coins.ir.hir.HirSeq p);
public void atInfNode(coins.ir.hir.InfNode p);
public void atInfStmt(coins.ir.hir.InfStmt p);
public void atVarNode(coins.ir.hir.VarNode p);
public void atElemNode(coins.ir.hir.ElemNode p);
public void atSubpNode(coins.ir.hir.SubpNode p);
public void atTypeNode(coins.ir.hir.TypeNode p);
public void atConstNode(coins.ir.hir.ConstNode p);
public void atLabelNode(coins.ir.hir.LabelNode p);
public void atSymNode(coins.ir.hir.SymNode p);
public void atNullNode(coins.ir.hir.NullNode p);
public void atLabelDef(coins.ir.hir.LabelDef p);
public void atExp(coins.ir.hir.Exp p);
public void atSubscriptedExp(coins.ir.hir.SubscriptedExp p);
public void atQualifiedExp(coins.ir.hir.QualifiedExp p);
public void atPointedExp(coins.ir.hir.PointedExp p);
public void atFunctionExp(coins.ir.hir.FunctionExp p);
public void atAssignStmt(coins.ir.hir.AssignStmt p);
public void atIfStmt(coins.ir.hir.IfStmt p);
public void atWhileStmt(coins.ir.hir.WhileStmt p);
public void atForStmt(coins.ir.hir.ForStmt p);
public void atUntilStmt(coins.ir.hir.UntilStmt p);
public void atRepeatStmt(coins.ir.hir.RepeatStmt p);
public void atIndexedLoopStmt(coins.ir.hir.IndexedLoopStmt p);
public void atLoopStmt(coins.ir.hir.LoopStmt p);
public void atLabeledStmt(coins.ir.hir.LabeledStmt p);
public void atBlockStmt(coins.ir.hir.BlockStmt p);
public void atReturnStmt(coins.ir.hir.ReturnStmt p);
public void atJumpStmt(coins.ir.hir.JumpStmt p);
public void atSwitchStmt(coins.ir.hir.SwitchStmt p);
public void atExpStmt(coins.ir.hir.ExpStmt p);
public void atPhiExp(coins.ir.hir.PhiExp p);
public void atAsmStmt(coins.ir.hir.AsmStmt p); //##70

} // HirVisitor

