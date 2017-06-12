/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;


//========================================//
//                        Jan. 19, 2001.
//                            (##2): modified on Nov. 2000.
//                            (##3): modified on Jan. 2001.

/**
 *  Assign-statement.
**/ 
public interface
AssignStmt extends Stmt
{

/** getLeftSide
 *  getRightSide
 *  These methods get a component of "this" assign statement.  (##2)
 *  If "this" is not an assignment statement, then they return null.
 *  "this" should be AssignStmt statement built by buildAssign.
 *  getLeftSide  return the left hand side expression (pLeftSide).
 *  getRightSide return the right hand side expression (pRightSide).
 **/
Exp getLeftSide();   // (##2)

/** getLeftSide
 *  getRightSide
 *  These methods get a component of "this" assign statement.  (##2)
 *  If "this" is not an assignment statement, then they return null.
 *  "this" should be AssignStmt statement built by buildAssign.
 *  getLeftSide  return the left hand side expression (pLeftSide).
 *  getRightSide return the right hand side expression (pRightSide).
 **/
Exp getRightSide();  // (##2)

////////////////////SF031012[
void setLeftSide(Exp pOperand);
void setRightSide(Exp pOperand);
////////////////////SF031012]

} // AssignStmt interface
