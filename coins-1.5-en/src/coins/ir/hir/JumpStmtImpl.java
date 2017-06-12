/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.HirRoot;
import coins.sym.Label;
import coins.sym.LabelImpl; //##62

/**
 * JumpStmt class
**/
public class
JumpStmtImpl extends StmtImpl implements JumpStmt {

    /** Constructors */
public
JumpStmtImpl( HirRoot pHirRoot, Label pLabelSym)
{
  this( pHirRoot, new LabelNodeImpl(pHirRoot,pLabelSym) );
}

public
JumpStmtImpl( HirRoot pHirRoot, LabelNode pLabelNode)
{
  super(pHirRoot, HIR.OP_JUMP);
  fChildCount = 1;
  setChild1(pLabelNode);
  fType = hirRoot.symRoot.typeVoid;
  fMultiBlock = true;
  pLabelNode.getLabel().setLabelKind(Label.JUMP_LABEL);
  ((LabelImpl)pLabelNode.getLabel()).addToHirRefList(pLabelNode); //SF031111 //##62
}

public Label
getLabel() { return (Label)(fChildNode1.getSym()); }

public void
changeJumpLabel( Label pNewLabel ) {
  Label lOldLabel = ((LabelNode)getChild1()).getLabel();
  setChild1(hirRoot.hir.labelNode(pNewLabel));
  pNewLabel.setLabelKind(Label.JUMP_LABEL);
}

////////////////////SF031111[
public Stmt
deleteThisStmt() {
  LabelNode labelnode = (LabelNode)fChildNode1;
  ((LabelImpl)labelnode.getLabel()).removeFromHirRefList(labelnode); //##62
  return super.deleteThisStmt();
} // deleteThisStmt
////////////////////SF031111]

public void
accept( HirVisitor pVisitor )
{
  pVisitor.atJumpStmt(this);
}

}  // JumpStmtImpl class
