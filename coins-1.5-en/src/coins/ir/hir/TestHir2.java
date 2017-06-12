/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import java.util.Iterator;
import coins.HirRoot;
import coins.sym.Sym;
import coins.sym.Var;

/**
 * Example of using HirVisitorModel2.
 * Do primary optimization for HIR before flow analysis.
**/
public class
TestHir2 extends HirVisitorModel2
{

  HIR hir;
  Sym sym;

public
TestHir2( HirRoot pHirRoot )
{
  super(pHirRoot);
  hir = pHirRoot.hir;
  sym = pHirRoot.sym;
}

/**
 * Reorder commutative operators that can be exchanged and
 * evaluate the expression if possible.
 * @param p Expression to be transformed.
 */
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
  ioRoot.dbgHir.print( 2, " TestHir2 atProgram " , p.toString());
  visitChildren(p);
}
public void atSubpDefinition(coins.ir.hir.SubpDefinition p) {
  ioRoot.dbgHir.print( 2, " atSubpDefinition " ,
        p.getSubpSym().getName()); visitChildren(p);
}

public void atBlockStmt(coins.ir.hir.BlockStmt p) {
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
public void atHirSeq(coins.ir.hir.HirSeq p) {
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
public void atLoopStmt(coins.ir.hir.LoopStmt p) {
  ioRoot.dbgHir.print( 2, " atLoopStmt " , p.toString());
  visitChildren(p); }
public void atWhileStmt(coins.ir.hir.WhileStmt p) {
  ioRoot.dbgHir.print( 2, " atWhileStmt " , p.toString());
  visitChildren(p); }
public void atForStmt(coins.ir.hir.ForStmt p) {
  ioRoot.dbgHir.print( 2, " atForStmt " , p.toString());
  visitChildren(p);
  Var lTemp = hirRoot.symRoot.symTableCurrent.generateVar(hirRoot.symRoot.typeInt);
  Stmt lStmt1 = hirRoot.hir.assignStmt(hirRoot.hir.varNode(lTemp),
                hirRoot.hir.intConstNode(1));
    Stmt lStmt2 = hirRoot.hir.assignStmt(hirRoot.hir.varNode(lTemp),
                     hirRoot.hir.exp(HIR.OP_ADD,
                        hirRoot.hir.varNode(lTemp),
                        hirRoot.hir.intConstNode(2)));
  p.addToConditionalInitPart(lStmt1);
}
public void atUntilStmt(coins.ir.hir.UntilStmt p) {
  ioRoot.dbgHir.print( 2, " atUntilStmt " , p.toString());
  visitChildren(p);
}
public void atRepeatStmt(coins.ir.hir.RepeatStmt p)
{
  ioRoot.dbgHir.print( 2, " atRepeatStmt " , p.toString());
  visitChildren(p);
  Var lTemp = hirRoot.symRoot.symTableCurrent.generateVar(hirRoot.symRoot.typeInt);
  Stmt lStmt1 = hirRoot.hir.assignStmt(hirRoot.hir.varNode(lTemp),
                hirRoot.hir.intConstNode(1));
    Stmt lStmt2 = hirRoot.hir.assignStmt(hirRoot.hir.varNode(lTemp),
                     hirRoot.hir.exp(HIR.OP_ADD,
                        hirRoot.hir.varNode(lTemp),
                        hirRoot.hir.intConstNode(2)));
  p.addToConditionalInitPart(lStmt1);
}
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

/**
 *
 * @param pExp
 * @return reordered expression.
 */
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

/**
 * Return true if the top operator is commutative
 * (exchangeable).
 * @param pExp Expression to be checked.
 * @return true if commutative.
 */
public boolean
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

/**
 * Return true if the top operator of the expression is
 * associative (either can be combined to left or to right).
 * @param pExp Expression to be checked.
 * @return true if the expression is associative.
 */
public boolean
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

} // TestHir2
