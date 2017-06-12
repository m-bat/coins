/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.sym.Const;
import coins.sym.Label;

/**
 *  switch statement interface.
**/
public interface
SwitchStmt extends Stmt {

/**
 *  Get selection expression
 *  @return the selection expression.
**/
public Exp
getSelectionExp();

/**
 *  Set selection expression
 *  @param pSelectionExp Selection expression of this switch statement.
**/
public void
setSelectionExp(Exp pSelectionExp);

/**
 *  @return the number of case branches of this switch.
**/
public int
getCaseCount();

/**
 *  Get the n-th (head is 0) case constant.
 *  If out of range , return null
 *  @return the n-th case constant.
**/
public Const
getCaseConst(int index);

/** getCaseLabel
 * Get n-th (head is 0) case label.
 * If out of range , return null.
 *  @return the n-th case label.
**/
public Label
getCaseLabel(int index);

/** getCaseLabelNode 
 * Get LabelNode of n-th (head is 0) case label.
 * If out of range , return null.
 *  @return the n-th case label node.
**/
public LabelNode
getCaseLabelNode(int index);

/**
 *  Get default label
 *  @return the default label
**/
public Label
getDefaultLabel();

/**
 *  Get default label node
 *  @return the default label node
**/
public LabelNode
getDefaultLabelNode(); 

/**
 *  Get break destination label (switch-end label) of this
 *  switch statement.
 *  @return the break destination label.
**/
public Label
getEndLabel();

/**
 *  Get the switch end node to where break statements jump.
 *  @return the switch end node with switch-end label.
 */
public LabeledStmt
getSwitchEndNode(); 

/**
 *  Gget switch-body statement that contains case statements.
 *  (Usually, it is BlockStmt.)
 *  @return the body statement of this switch statement.
**/
public Stmt
getBodyStmt();

} // SwitchStmt interface
