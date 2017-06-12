/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.HirRoot;
import coins.ir.IrList;
import coins.sym.Label;
import coins.sym.SubpImpl; //##62

/**
 *
 * Label definition
 *
**/
public class
LabelDefImpl extends HIR_Impl implements LabelDef
{

//==== Fields ====//

    /** fLabelSym Label symbol defined by this node.
    **/
    private Label fLabelSym;

    /** fBBlock Basic block containing this node.
    **/
    protected coins.aflow.BBlock fBBlock;

    /** fLabeledStmt Labeled statement to which this node is attached.
    **/
    protected LabeledStmt fLabeledStmt = null;

//==== Constructor ====//

public
LabelDefImpl( HirRoot pHirRoot, Label pLabelSym)
{
    super(pHirRoot, HIR.OP_LABEL_DEF);
    fLabelSym = pLabelSym;	 //# Should be list of label.
    fChildCount = 0;
    if (hirRoot.symRoot.subpCurrent != null)
      ((SubpImpl)hirRoot.symRoot.subpCurrent).addToLabelDefList(pLabelSym); //##62
    fType = hirRoot.symRoot.typeVoid;
}

//==== Methods ====//

public Label
getLabel() { return fLabelSym; }

public void
setLabel( Label pLabel ) { fLabelSym = pLabel; }

/* //##91
public void
setBBlock( coins.aflow.BBlock pBBlock ) {
  fBBlock = pBBlock;
  if (fLabelSym != null)
    fLabelSym.setBBlock(pBBlock);
}
*/

public LabeledStmt
getLabeledStmt() {
  return fLabeledStmt;
}

public void
setLabeledStmt( LabeledStmt pLabeledStmt ) {
    fLabeledStmt = pLabeledStmt;
}

public Object
clone() {
  LabelDef lTree;
  try {
    lTree = (LabelDef)super.clone();
  }catch (CloneNotSupportedException e) {
    hirRoot.ioRoot.msgRecovered.put(1100,
                    "CloneNotSupportedException(LabelDefImpl) "
                     + this.toString());
    return null;
  }
  ((LabelDefImpl)lTree).fLabelSym = fLabelSym;
  ((LabelDefImpl)lTree).fBBlock = fBBlock;       //  CHANGE
  ((LabelDefImpl)lTree).fLabeledStmt = fLabeledStmt;  // CHANGE
  return (Object)lTree;
} // clone

/* //##85
public HIR
copyWithOperandsChangingLabels( IrList pLabelCorrespondence ) {
  HIR lNode = super.copyWithOperandsChangingLabels(pLabelCorrespondence);
  ((LabelDefImpl)lNode).fLabelSym
      = hirRoot.hirModify.getNewLabel(fLabelSym, pLabelCorrespondence);
  return (HIR)lNode;
} // copyWithOperandsChangingLabels
*/ //##85

public String
toString() {
  return "labelDef " + getIndex() + " " + fLabelSym.getName(); //##51
} // toString

public void
accept( HirVisitor pVisitor )
{
  pVisitor.atLabelDef(this);
}

}  // LabelDefImpl class
