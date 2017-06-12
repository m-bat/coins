/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import java.util.Iterator;

import coins.HirRoot;
import coins.ir.IR;
import coins.ir.IrList;
import coins.sym.Label;
import coins.sym.Subp;
import coins.sym.SubpImpl;
import coins.sym.Sym;

/**
 *
 * LabeledStmtImpl Labeled statement class
 *
**/
public class
LabeledStmtImpl extends StmtImpl implements LabeledStmt
{

  protected Sym fFileNameSym;
  protected int fLineNumber;

public
LabeledStmtImpl( HirRoot pHirRoot, Label pLabel, Stmt pStmt )
{
    // Add pLabel to label list of subpCurrent and record
    // this Stmt to Stmt list of pLabel.
    // Relations between pStmt and others (such as previousStmt,
    // nextStmt, parentNode) are not kept. If it is necessary
    // to transfer them to this LabeledStmt, use attachLabel method.
  super(pHirRoot);
  Stmt        lPrev, lNext;
  HIR         lParent;
  IrList      lLabelDefList;
  int         lChildNumber;
  fOperator = OP_LABELED_STMT;
  lLabelDefList = hirRoot.hir.irList();
  LabelDef lLabelDefNode = hirRoot.hir.labelDef(pLabel);
  lLabelDefList.add(lLabelDefNode);
  fChildNode1 = (IR)lLabelDefList;
  fChildNode2 = (IR)pStmt;
  ((HIR_Impl)fChildNode1).setParent(this); //##54
  if (fChildNode2 != null)
    ((HIR_Impl)fChildNode2).setParent(this); //##54
  fChildCount = 2;
  lLabelDefNode.setLabeledStmt(this);
  ////////////////////////////////////// S.Fukuda 2002.7.2 begin
  if( hirRoot.symRoot.subpCurrent!=null )
  ////////////////////////////////////// S.Fukuda 2002.7.2 end
  //##62 ((SubpImpl)hirRoot.symRoot.subpCurrent).addLabeledStmtList(pLabel, this);
  if (pLabel != null)
    pLabel.setHirPosition(this);
  if (pStmt != null) {
    fType = pStmt.getType();
    if (((StmtImpl)pStmt).fPrevStmt != null)
      ((StmtImpl)(((StmtImpl)pStmt).fPrevStmt)).fNextStmt = null;
    if (((StmtImpl)pStmt).fNextStmt != null)
      ((StmtImpl)(((StmtImpl)pStmt).fNextStmt)).fPrevStmt = null;
    ((StmtImpl)pStmt).fPrevStmt = null;  // Cut off relations.
    ((StmtImpl)pStmt).fNextStmt = null;
  }else
    fType = hirRoot.symRoot.typeVoid;
} // LabeledStmt

public IrList
getLabelDefList() {
  return (IrList)fChildNode1;
}

public void
setLabelDefList( IrList pLabelDefList ) {
  setChild1((IR)pLabelDefList);
}

public Label
getLabel() {
  if ((fChildNode1 != null)&&
      (getLabelDefList().getFirst() != null))
    return ((LabelDef)(getLabelDefList().getFirst())).getLabel();
  else
    return null;
} // getLabel

public Stmt
getStmt() {
  return (Stmt)fChildNode2;
}

public void
setStmt(Stmt pStmt) {
  if(pStmt != null) {
    ((HIR_Impl)pStmt).setParent(this); //##54
    ((StmtImpl)pStmt).fNextStmt = null;
    ((StmtImpl)pStmt).fPrevStmt = null;
  }
  fChildNode2 = (IR)pStmt;
  if (fDbgLevel > 3) //##58
    hirRoot.ioRoot.dbgHir.print( 8, "LabeledStmt.setStmt",
       "" +fChildNode1 + ":" + fChildNode2);
}

public Stmt
deleteLabel( Label pLabel )
{
  IrList   lLabelDefList = getLabelDefList();
  LabelDef lLabelDef;
  Stmt     lStmt;
  if (fDbgLevel > 2) //##58
    hirRoot.ioRoot.dbgHir.print(3, "deleteLabel", pLabel.getName()
    + " of " + toString());
  for (Iterator lIterator = lLabelDefList.iterator();
       lIterator.hasNext(); ) {
    lLabelDef = (LabelDef)lIterator.next();
    if (lLabelDef.getLabel() == pLabel) {
      lLabelDefList.remove(lLabelDef);
      pLabel.setHirPosition(null); // Reset
      break;
    }
  }
  if (lLabelDefList.isEmpty()) {
    lStmt = getStmt();
    if ((lStmt == null)||
        (lStmt instanceof NullNode)) {
      this.deleteThisStmt();
      return null;
    }else {
      this.replaceThisStmtWith(lStmt);
      return lStmt;
    }
  }
  return this;
} // deleteLabel

public void
replaceLabelNodesReferingThisStmtToNewOne( Label pNewLabel )
{
  IrList   lLabelDefList = getLabelDefList();
  LabelDef lLabelDef;
  Label    lOldLabel;
  if (fDbgLevel > 2) //##58
    hirRoot.ioRoot.dbgHir.print(3, "replaceLabelNodesReferingThisStmtToNewOne",
     "to " + pNewLabel.getName());
  for (Iterator lIterator = lLabelDefList.iterator();
       lIterator.hasNext(); ) {
    lLabelDef = (LabelDef)lIterator.next();
    lOldLabel = lLabelDef.getLabel();
    lOldLabel.replaceHirLabel(pNewLabel);
  }
} // replaceLabelNodesReferingThisStmtToNewOne


////////////////////SF031111[
public void
merge(LabeledStmt from)
{
  IrList tolist   = this.getLabelDefList();
  IrList fromlist = from.getLabelDefList();
FROMLOOP:
  for( java.util.ListIterator fromiter = fromlist.iterator(); fromiter.hasNext(); )
  {
    LabelDef fromdef   = (LabelDef)fromiter.next();
    Label    fromlabel = fromdef.getLabel();
    for( java.util.ListIterator toiter = tolist.iterator(); toiter.hasNext(); )
    {
      LabelDef todef   = (LabelDef)toiter.next();
      Label    tolabel = todef.getLabel();
      if( tolabel.getLabelKind()==Label.JUMP_LABEL
      &&  tolabel.getLabelKind()==Label.JUMP_LABEL ) // replace only jump(=goto)
      {
        fromlabel.replaceHirLabel(tolabel);
        continue FROMLOOP;
      }
    }
    // not found the same kind label. add fromlabel to tolabel list.
    fromlabel.setHirPosition(this);
    fromdef.setLabeledStmt(this);
    tolist.add(fromdef);
    ((HIR_Impl)fromdef).setParent(tolist); // why is this line necessary ?
  }
}
////////////////////SF031111]
/** explicitLabelReference
 *  Get a LabelNode refering explicitly a label attached to
 *  this statement. If there are multiple LabelNodes refering
 *  explicitly, then the first one in the refering list
 *  will be returned.
 *  Before invoking this method, label reference list should be
 *  constructed by using coins.flow.LabelRefListBuilder.
 *  If there is no explicit reference, then null is returned.
 *  This method is used to examine whether this statement is
 *  a target of some explicit branch.
 *  Explicit reference is made by JumpStmt  and case list of
 *  SwitchStmt (and break statement in C).
 *  Other generated labels such as THEN_LABEL, ELSE_LABEL, END_IF_LABEL,
 *  LOOP_BACK_LABEL, etc. do not appear explicitly in jump statement
 *  and they are treated as implicit reference.
 *  @return a LabelNode refering explicitly some label of this
 *      statement. If there is no explicit reference, return null.
**/

//##76 /* //##62
public LabelNode
explicitLabelReference()
{
  LabelDef lLabelDef;
  Label    lLabel;
  IrList lLabelDefList = getLabelDefList();
  IrList lHirRefList;
  if (lLabelDefList == null)
    return null;
  for (Iterator lIterator = lLabelDefList.iterator();
       lIterator.hasNext(); ) {
    lLabelDef = (LabelDef)lIterator.next();
    lLabel = lLabelDef.getLabel();
    if (lLabel == null)
      break;
    lHirRefList = lLabel.getHirRefList();
    if (lHirRefList == null)
      break;
    if (lHirRefList.isEmpty()) //##76
      return null; //##76
    return (LabelNode)lHirRefList.get(0);
  }
  return null;
} // explicitLabelReference
//##76 */ //##62

/** deleteThisStmt
 *  Delete labels defined by this statement from the label definition
 *  list of the current subprogram and then delete this statement.
**/
/* //##62
public Stmt
deleteThisStmt() {
  IrList   lLabelDefList = getLabelDefList();
  IrList   lLabelDefListOfSubp = null;
  LabelDef lLabelDef;
  Label    lLabel;
  Subp     lSubpCurrent = hirRoot.symRoot.subpCurrent;
  if (fDbgLevel > 0) //##58
    hirRoot.ioRoot.dbgHir.print(3, "deleteThisStmt(LabeledStmt)", toString());
  if (lSubpCurrent != null)
    lLabelDefListOfSubp =  lSubpCurrent.getLabelDefList();
  if (lLabelDefListOfSubp != null) {
    for (java.util.Iterator lIterator = lLabelDefList.iterator();
         lIterator.hasNext(); ) {
      lLabelDef = (LabelDef)lIterator.next();
      lLabel = lLabelDef.getLabel();
      lLabelDefListOfSubp.remove(lLabel);
    }
  }
  return super.deleteThisStmt();
} // deleteThisStmt
 */ //##62

public Object
clone()  throws CloneNotSupportedException {
  LabeledStmt lTree;
  try {
    lTree = (LabeledStmt)super.clone();
  }catch (CloneNotSupportedException e) {
    hirRoot.ioRoot.msgRecovered.put(1100,
      "CloneNotSupportedException(labeledStmtImpl) " + this.toString());
    return null;
  }
  ((LabeledStmtImpl)lTree).fFileNameSym = fFileNameSym;
  ((LabeledStmtImpl)lTree).fLineNumber  = fLineNumber;
  return (Object)lTree;
} // clone

public String
toString() {
  String lString = super.toString();
  return lString;
} // toString

public void
accept( HirVisitor pVisitor )
{
  pVisitor.atLabeledStmt(this);
}

} // LabeledStmtImpl class
