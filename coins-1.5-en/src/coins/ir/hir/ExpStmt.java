/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;


//========================================//
//                            (##5): modified on Apr. 2001.

/**
 * ExpStmt
 *    Expression treated as a statement.
**/
public interface
ExpStmt extends Stmt {

/**
 * Get the expression attached as the operand of this
 * statement.
 * @return the expression.
 */
public Exp
getExp();

/**
 * Set the expression pExp as the operand of
 * this statement.
 * @param pExp expression to be set.
 */
public void
setExp( Exp pExp );

} // ExpStmt interface
