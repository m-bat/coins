/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.HirRoot;
import coins.ir.IrList;
import coins.sym.Label;

/**
 *
 * Label reference node.
 *
**/
public class
LabelNodeImpl extends SymNodeImpl implements LabelNode
{
    /* constructor */
public
LabelNodeImpl( HirRoot pHirRoot, Label pLabel)
{
  super(pHirRoot);
  fOperator = HIR.OP_LABEL;
  fSym      = pLabel;
  fChildCount = 0;
  fType = hirRoot.symRoot.typeVoid;
}

public Label
getLabel() { return (Label)fSym; }

/* //##85
public HIR
copyWithOperandsChangingLabels( IrList pLabelCorrespondence ) {
  HIR lNode = super.copyWithOperandsChangingLabels(pLabelCorrespondence);
  ((LabelNodeImpl)lNode).fSym
      = hirRoot.hirModify.getNewLabel((Label)fSym, pLabelCorrespondence);
  return lNode;
} // copyWithOperandsChangingLabels
*/ //##85

public void
accept( HirVisitor pVisitor )
{
  pVisitor.atLabelNode(this);
}

} // LabelNodeImpl class
