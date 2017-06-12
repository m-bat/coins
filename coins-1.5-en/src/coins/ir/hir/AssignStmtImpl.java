/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.HirRoot;
import coins.ir.IR;

/**
 *  Assignment statement class.
**/
public class
AssignStmtImpl extends StmtImpl implements AssignStmt
{

public
AssignStmtImpl( HirRoot pHirRoot, Exp pLeftSide, Exp pRightSide )
{
  super(pHirRoot, HIR.OP_ASSIGN);
  Exp lRightSide = pRightSide;
  fChildCount = 2;
  setChildren(pLeftSide, lRightSide);
  fType = lRightSide.getType();
}

    /** getLeftSide
     *  getRightSide
     *  These methods get a component of "this" assign statement.  (##2)
     *  If "this" is not an assignment statement, then they return null.
     *  "this" should be AssignStmt statement built by buildAssign.
     *  getLeftSide  return the left hand side expression (pLeftSide).
     *  getRightSide return the right hand side expression (pRightSide).
     **/
public Exp
getLeftSide() {  return (Exp)fChildNode1; }

public Exp
getRightSide() { return (Exp)fChildNode2; }

////////////////////SF031012[
public void
setLeftSide(Exp pOperand){
  fChildNode1 = pOperand;
  if( pOperand!=null )
    ((HIR_Impl)pOperand).fParentNode = this;
}

public void
setRightSide(Exp pOperand){
  fChildNode2 = pOperand;
  if( pOperand!=null )
    ((HIR_Impl)pOperand).fParentNode = this;
}
////////////////////SF031012]

public HIR
replaceSource1( HIR pOperand ) {
  fChildNode2 = pOperand;
  if (pOperand != null)
    ((HIR_Impl)pOperand).fParentNode = this;
  return pOperand;
} // replaceSource1

public HIR
replaceSource2( HIR pOperand ) {
  return pOperand;
} // replaceSource2

public HIR
replaceSource( int pNumber, HIR pOperand ){
  if (pNumber == 1) {
    fChildNode2 = pOperand;
  }
  if (pOperand != null)
    ((HIR_Impl)pOperand).fParentNode = this;
  return pOperand;
} // replaceSource

/** replaceResultOperand
 *  Replace result variable of "this" node by pOperand.
 *  "this" should be a node that can have variable as its
 *  result operand, that is, "this" should be HIR assign node or
 *  LIR store node.
 *  If "this" is HIR assign node, its child 1 representing left
 *  hand side variable is replaced by pOperand.
 *  If "this" is LIR store node, the result variable is replaced
 *  by pOperand which may be a simple variable or a register
 *  or some other expression representing address of variable. (##2)
 *  @param pOperand node that takes place of result variable.
 *      If "this" is HIR node then it should be HIR node,
 *      if "this" is LIR node then it should be LIR node.
 **/
public void
replaceResultOperand( IR pOperand ) {
  fChildNode1 = pOperand;
  if (pOperand != null)
    ((HIR_Impl)pOperand).fParentNode = this;
}

    /** replaceResultVar  to be DELETED. Use replaceResultOperand.
     *  Replace result variable of "this" node by pOperand.
     *  "this" should be a node that can have variable as its
     *  result operand, that is, "this" should be HIR assign node or
     *  LIR store node.
     *  If "this" is HIR assign node, its child 1 representing left
     *  hand side variable is replaced by pOperand.
     *  If "this" is LIR store node, the result variable is replaced
     *  by pOperand which may be a simple variable or a register
     *  or some other expression representing address of variable. (##2)
     *  @param pOperand node that takes place of result variable.
     *      If "this" is HIR node then it should be HIR node,
     *      if "this" is LIR node then it should be LIR node.
     **/
public void
replaceResultVar( IR pOperand ) {
  fChildNode1 = pOperand;
  if (pOperand != null)
    ((HIR_Impl)pOperand).fParentNode = this;
}

public void
accept( HirVisitor pVisitor )
{
  pVisitor.atAssignStmt(this);
}

} // AssignStmtImpl class
