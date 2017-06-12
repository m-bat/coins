/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.ir.IrList;
import coins.sym.Label;

/**
 *  switch modify class.
 *  (by S. Fukuda)
**/
public class
SwitchModify extends SwitchStmtImpl {

// Components of switch statement:
//   child1: Case selection expression.
//   child2: List of constants and statement labels.
//   child3: Collection of statements to be selected.
//   child4: Indicate end of case statement.

private IrList fCaseList;
private HirSeq fSwitchCase;
private Label  fDefaultLabel;
private Label  fEndLabel;
private Stmt   fSwitchBody;

//## Increment reference count of case label.

//## fMultiBlock = true;

public void
replaceSwitchCaseLabel( Label pNewLabel ) {

}


} // SwitchModify class
