/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.sym.Label;

/**
 * JumpStmt interface
**/
public interface
JumpStmt extends Stmt {

/**
 * Get the label of the jump statement.
 * @return the label.
 */
public Label
getLabel();

/**
 * Change the label of this statement to pNewLabel.
 * @param pNewLabel
 */
public void
changeJumpLabel( Label pNewLabel );

}  // JumpStmt interface
