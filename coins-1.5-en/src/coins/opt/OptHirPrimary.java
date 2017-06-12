/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.opt;

import java.util.Iterator;
import coins.HirRoot;
import coins.ir.hir.*;

/** OptHirPrimary:
 * Do primary optimization for HIR before flow analysis.
 * This is not included in the HIR optimization set
 * because this changes the order of expression evaluation.
 * This is intended to show how to modify HIR and
 * how to traverse HIR nodes by using the visitor model.
 *
 * This module changes the order of expressions according to
 * comuttative rule and associative rule, and then
 * does constant folding optimization.
 * Ex.
 *   ff(1) + 1 + 2     ==> 3 + ff(1)
 *   y = x * 2 * 3     ==> 6 * x
 *   z = 3 + y + 4 + 5 ==> z = 12 + y
 *
 * This module can be invoked from
 *   modifyHirIfNecessary of coins/ir/hir/HirModify
 * as it is shown by comments in HirModify.
**/
public class
OptHirPrimary extends HirVisitorModel2
{

public
OptHirPrimary( HirRoot pHirRoot )
{
  super(pHirRoot);
}

public void atExp(coins.ir.hir.Exp p)
{
  ioRoot.dbgHir.print( 2, " atExp " , p.toString());
  ioRoot.dbgHir.print( 2, " isEvaluable " + p.isEvaluable());
  visitChildren(p);
  Exp lEvaluated = null;
  Exp lEvaluatedOperand = null;
  Exp lReordered = reorderOperands(p);
  Exp lChild1 = (Exp)lReordered.getChild1();
  Exp lChild2 = (Exp)lReordered.getChild2();
  if (lChild1.isEvaluable()&&(lChild2 != null)&&
      (! lChild2.isEvaluable())) {
    Exp lGrandChild1 = (Exp)lChild2.getChild1();
    if ((lGrandChild1 != null)&&
        lGrandChild1.isEvaluable()&&
        (lChild2.getOperator() == p.getOperator())&&
        isAssociative(p)) {
      Exp lEvaluable = hirRoot.hir.exp(p.getOperator(),
         (Exp)lChild1.copyWithOperands(),
         (Exp)lGrandChild1.copyWithOperands());
      if (p.getType().isInteger()) {
        ioRoot.dbgHir.print( 2, " evaluable " + lEvaluable.toStringWithChildren());
        lEvaluatedOperand = hirRoot.hir.intConstNode(lEvaluable.evaluateAsInt());
        lEvaluated = hirRoot.hir.exp(p.getOperator(), lEvaluatedOperand,
            (Exp)((Exp)lChild2.getChild2()).copyWithOperands());
        ioRoot.dbgHir.print( 2, " evaluate " + lEvaluated.toStringWithChildren());
      }
    }
  }
  if (lEvaluated != null) {
    p.replaceThisNode(lEvaluated);
  }else if (p != lReordered) {
    p.replaceThisNode(lReordered);
  }
}

public void atProgram(coins.ir.hir.Program p) {
  ioRoot.dbgHir.print( 2, " atProgram " , p.toString());
  visitChildren(p); }
public void atSubpDefinition(coins.ir.hir.SubpDefinition p) {
  ioRoot.dbgHir.print( 2, " atSubpDefinition " ,
        p.getSubpSym().getName()); visitChildren(p);
}

public void atBlockStmt(coins.ir.hir.BlockStmt p) { //##38
  ioRoot.dbgHir.print( 2, " atBlockStmt " , p.toString());
  visitChildren(p);
  for (Stmt lStmt = p.getFirstStmt(); lStmt != null;
       lStmt = lStmt.getNextStmt()) {
    lStmt.accept(this);
  }
}

public void atHirList(coins.ir.hir.HirList p) {
  ioRoot.dbgHir.print( 2, " atHirList " , p.toString());
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
  ioRoot.dbgHir.print( 2, " atIrList " , p.toString());
  visitChildren((HIR)p); }
public void atHirSeq(coins.ir.hir.HirSeq p) { //##38
  ioRoot.dbgHir.print( 2, " atHirSeq " , p.toString());
  visitChildren((HIR)p); }
public void atInfNode(coins.ir.hir.InfNode p) {
  ioRoot.dbgHir.print( 2, " atInfNode " , p.toString());
  visitChildren(p); }
public void atInfStmt(coins.ir.hir.InfStmt p) {
  ioRoot.dbgHir.print( 2, " atInfStmt " , p.toString());
  visitChildren(p); }
public void atVarNode(coins.ir.hir.VarNode p) {
  ioRoot.dbgHir.print( 2, " atVarNode " , p.toString());
  visitChildren(p); }
public void atElemNode(coins.ir.hir.ElemNode p) {
  ioRoot.dbgHir.print( 2, " atElemNode " , p.toString());
  visitChildren(p); }
public void atSubpNode(coins.ir.hir.SubpNode p) {
  ioRoot.dbgHir.print( 2, " atSubpNode " , p.toString());
  visitChildren(p); }
public void atTypeNode(coins.ir.hir.TypeNode p) {
  ioRoot.dbgHir.print( 2, " atTypeNode " , p.toString());
  visitChildren(p); }
public void atConstNode(coins.ir.hir.ConstNode p) {
  ioRoot.dbgHir.print( 2, " atConstNode " , p.toString());
  visitChildren(p); }
public void atLabelNode(coins.ir.hir.LabelNode p) {
  ioRoot.dbgHir.print( 2, " atLabelNode " , p.toString());
  visitChildren(p); }
public void atSymNode(coins.ir.hir.SymNode p) {
  ioRoot.dbgHir.print( 2, " atSymNode " , p.toString());
  visitChildren(p); }
public void atNullNode(coins.ir.hir.NullNode p) {
  ioRoot.dbgHir.print( 2, " atNullNode " , p.toString());
  visitChildren(p); }
public void atLabelDef(coins.ir.hir.LabelDef p) {
  ioRoot.dbgHir.print( 2, " atLabelDef " , p.toString());
  visitChildren(p); }
public void atSubscriptedExp(coins.ir.hir.SubscriptedExp p) {
  ioRoot.dbgHir.print( 2, " atSubscriptedExp " , p.toString());
  visitChildren(p); }
public void atQualifiedExp(coins.ir.hir.QualifiedExp p) {
  ioRoot.dbgHir.print( 2, " atQualifiedExp " , p.toString());
  visitChildren(p); }
public void atPointedExp(coins.ir.hir.PointedExp p) {
  ioRoot.dbgHir.print( 2, " atPointedExp " , p.toString());
  visitChildren(p); }
public void atFunctionExp(coins.ir.hir.FunctionExp p) {
  ioRoot.dbgHir.print( 2, " atFunctionExp " , p.toString());
  visitChildren(p); }
public void atAssignStmt(coins.ir.hir.AssignStmt p) {
  ioRoot.dbgHir.print( 2, " atAssignStmt " , p.toString());
  visitChildren(p); }
public void atIfStmt(coins.ir.hir.IfStmt p) {
  ioRoot.dbgHir.print( 2, " atIfStmt " , p.toString());
  visitChildren(p); }
public void atWhileStmt(coins.ir.hir.WhileStmt p) {
  ioRoot.dbgHir.print( 2, " atWhileStmt " , p.toString());
  visitChildren(p); }
public void atForStmt(coins.ir.hir.ForStmt p) {
  ioRoot.dbgHir.print( 2, " atForStmt " , p.toString());
  visitChildren(p); }
public void atUntilStmt(coins.ir.hir.UntilStmt p) {
  ioRoot.dbgHir.print( 2, " atUntilStmt " , p.toString());
  visitChildren(p); }
public void atLabeledStmt(coins.ir.hir.LabeledStmt p) {
  ioRoot.dbgHir.print( 2, " atLabeledStmt " , p.toString());
  visitChildren(p); }
public void atReturnStmt(coins.ir.hir.ReturnStmt p) {
  ioRoot.dbgHir.print( 2, " atReturnStmt " , p.toString());
  visitChildren(p); }
public void atJumpStmt(coins.ir.hir.JumpStmt p) {
  ioRoot.dbgHir.print( 2, " atJumpStmt " , p.toString());
  visitChildren(p); }
public void atSwitchStmt(coins.ir.hir.SwitchStmt p) {
  ioRoot.dbgHir.print( 2, " atSwitchStmt " , p.toString());
  visitChildren(p); }
public void atExpStmt(coins.ir.hir.ExpStmt p) {
  ioRoot.dbgHir.print( 2, " atExpStmt " , p.toString());
  visitChildren(p); }
public void atPhiExp(coins.ir.hir.PhiExp p) {
  ioRoot.dbgHir.print( 2, " atPhiExp " , p.toString());
  visitChildren(p); }

Exp
reorderOperands( Exp pExp )
{
  if ((pExp == null )||
      (pExp.getChildCount() != 2))
    return pExp;
  Exp lChild1 = (Exp)pExp.getChild1();
  Exp lChild2 = (Exp)pExp.getChild2();
  if (lChild2.isEvaluable()&&
      (! lChild1.isEvaluable())&&
      isCommutative(pExp)) {
    Exp lExp = hirRoot.hir.exp(pExp.getOperator(),
             (Exp)lChild2.copyWithOperands(),
             (Exp)lChild1.copyWithOperands());
    ioRoot.dbgHir.print( 2, "\n  reorder " + lExp.toStringWithChildren());
    return lExp;
  }else
    return pExp;
} // reorderOperands

boolean
isCommutative( Exp pExp )
{
  if (pExp == null)
    return false;
  switch (pExp.getOperator()) {
  case HIR.OP_ADD:
  case HIR.OP_MULT:
  case HIR.OP_AND:
  case HIR.OP_OR:
  case HIR.OP_XOR:
    return true;
  default:
    return false;
  }
} // isCommutative

boolean
isAssociative( Exp pExp )
{
  if (pExp == null)
    return false;
  switch (pExp.getOperator()) {
  case HIR.OP_ADD:
  case HIR.OP_MULT:
  case HIR.OP_AND:
  case HIR.OP_OR:
  case HIR.OP_XOR:
    return true;
  default:
    return false;
  }
} // isAssociative

} // PrintVisitor
