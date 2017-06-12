/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.sym.Label;

/**
 *
 * Label definition
 *
**/
public interface
LabelDef extends HIR {

public Label getLabel();

/** setLabel
 *  Set pLabel as the label of this node.
 *  (Unnecessary to use this method except for modifying HIR
 *   because labelDef method sets it.)
**/
public void  setLabel( Label pLabel );

/* //##91
public void setBBlock( coins.aflow.BBlock pBBlock );
*/
LabeledStmt
getLabeledStmt();

void
setLabeledStmt( LabeledStmt pLabeledStmt );

} // LabelDef interface
