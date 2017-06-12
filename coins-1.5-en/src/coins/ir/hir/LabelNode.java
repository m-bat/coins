/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.sym.Label;

/**
 *
 * LabelNode: Label reference node.
 *
**/ 
public interface 
LabelNode extends SymNode {

/** getLabel
 *  Get the label attached to this LabelNode attached as its symbol attribute.
 *  @return the label of this node.
**/
public Label
getLabel();

}
