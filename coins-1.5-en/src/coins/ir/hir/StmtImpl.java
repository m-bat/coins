/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.HirRoot;
import coins.IoRoot;
import coins.ir.IR;
import coins.ir.IrList;
import coins.sym.Label;

/**
 *  Statement
**/
public class
StmtImpl extends HIR_Impl implements Stmt
{

//====== Field declarations ======//

  protected Stmt    fNextStmt = null;
  protected Stmt    fPrevStmt = null;
  protected boolean fMultiBlock = false; // true if this statement
                               // is composed of multiple basic blocks.
                               // Be careful in delete.
  ///////////////////////////////////// S.Fukuda
  private String fFileName = null;
  private int  fLineNumber = 0;
  /////////////////////////////////////

//====== Constructors ======//

/** Default constructor for subclasses.
**/
public
StmtImpl() { }

public
StmtImpl( HirRoot pHirRoot )
{
  super(pHirRoot, HIR.OP_STMT);
}

protected
StmtImpl( HirRoot pHirRoot, int pOperator )
{
  super(pHirRoot, pOperator);
  fType = hirRoot.symRoot.typeVoid;
}

//====== Methods to handle statement ======//

    /** addNextStmt
     *  Add a statement as the one next to this statement.
     *  If "this" is the last statement, append one as the next
     *  statement. If "this" has already the next statement,
     *  insert in front of the next statement.
     *  "this" should be a statement node.
     *  @param pStatement statement subtree to be added.
     *  @return the added statement node.
     **/
public Stmt
addNextStmt( Stmt pStatement )
{
  Stmt      lStmt;
  if ((pStatement == null)||(pStatement == this))
    return pStatement;
  if (fDbgLevel > 3) //##58
    hirRoot.ioRoot.dbgHir.print(5, "\naddNextStmt " +
    pStatement.toStringShort() + " curr " + toStringShort());
  lStmt = pStatement;
  if ((lStmt.getParent() != null)||
      (lStmt.getNextStmt() != null)||
      (lStmt.getPreviousStmt() != null))
    lStmt = (Stmt)(lStmt.copyWithOperands());
  if (fParentNode instanceof BlockStmt) {
    ((StmtImpl)lStmt).fParentNode = fParentNode;
    ((StmtImpl)lStmt).fNextStmt   = fNextStmt;
    ((StmtImpl)lStmt).fPrevStmt   = this;
    if (fNextStmt != null)
      ((StmtImpl)fNextStmt).fPrevStmt = lStmt;
    fNextStmt = lStmt;
    if (((BlockStmt)fParentNode).getLastStmt() == this)
      ((BlockStmtImpl)fParentNode).setLastStmt(lStmt);
  }
  else { // This Stmt is not included directly in a block.  BEGIN
    // Record linkages and cut linkages.
    HIR lParent2 = (HIR)getParent();
    Stmt lPrev2 = getPreviousStmt();
    Stmt lNext2 = getNextStmt();
    int lChildNum = getChildNumber();
    cutParentLink();
    fNextStmt = null;
    fPrevStmt = null;
    Stmt lCombinedStmt = this.combineStmt(lStmt, false);
    // Set the recorded linkages to the combined statement.
    if ((lParent2 != null) && (lChildNum > 0))
      lParent2.replaceSource(lChildNum, lCombinedStmt);
    ((StmtImpl)lCombinedStmt).fNextStmt = lNext2;
    ((StmtImpl)lCombinedStmt).fPrevStmt = lPrev2;
    if (lPrev2 != null)
      ((StmtImpl)lPrev2).fNextStmt = lCombinedStmt;
    if (lNext2 != null)
      ((StmtImpl)lNext2).fPrevStmt = lCombinedStmt;
  }
  ((StmtImpl)lStmt).printLinkage("addNextStmt");
  return lStmt;
} // addNextStmt

public Stmt
addNextStmt( Stmt pStatement, BlockStmt pBlock )
{
  return addNextStmt(pStatement);
}
    /** getNextStmt
     *  Get the statement next to "this" one.
     *  "this" should be a statement node.
     *  @return the statement next to "this" one.
     *      Return null if there is no next statement
     *      (if this is the last statement of a block).
     **/
public Stmt
getNextStmt() {
   //##59 if ((fParentNode != null)&&
   //##59     (fParentNode.getOperator() == HIR.OP_LABELED_STMT))
   //##59   return ((Stmt)fParentNode).getNextStmt();
   //##59 else
    return fNextStmt;
} // getNextStmt

public Stmt
getUpperStmt() {
  HIR lUpperNode;
  lUpperNode = (HIR)(this.getParent());
  while ((lUpperNode != null)&&
         (lUpperNode.isStmt() == false)) {
    lUpperNode = (HIR)(lUpperNode.getParent());
  }
  return (Stmt)lUpperNode;
} // getUpperStmt

/** insertPreviousStmt
 *  Insert a statement in front of "this" one.
 *  @param pStatement statement subtree to be inserted.
 *  @return the inserted statement node (pStatement).
**/
public Stmt
insertPreviousStmt( Stmt pStatement )
{
  Stmt      lStmt, lResult;
  if ((pStatement == null)||(pStatement == this))
  //##58 if (pStatement == null)
    return pStatement;
  if (fDbgLevel > 3) //##58
    hirRoot.ioRoot.dbgHir.print(5, "\ninsertPreviousStmt " +
    pStatement.toStringShort() + " curr " + toStringShort());
  if (fDbgLevel >= 6)
    pStatement.print(3, true);
  if (fPrevStmt != null)
    return fPrevStmt.addNextStmt(pStatement);
  else {  // This is the first Stmt of a block or a Stmt not in a block.
    lStmt = pStatement;
    if ((lStmt.getParent() != null)||
        (lStmt.getNextStmt() != null)||    //##53
        (lStmt.getPreviousStmt() != null)) //##53
      //##58 lStmt = (Stmt)(lStmt.copyWithOperands());
      lStmt = (Stmt)(lStmt.copyWithOperands());
    if (fParentNode instanceof BlockStmt) {
      // This is the first statement of a block.
      fPrevStmt = lStmt;
      ((StmtImpl)lStmt).fNextStmt = this;
      ((StmtImpl)lStmt).fPrevStmt = null;
      ((StmtImpl)lStmt).fParentNode = fParentNode;
      if (((BlockStmt)fParentNode).getFirstStmt() == this)
        ((BlockStmtImpl)fParentNode).setFirstStmt(lStmt);
      //##59 if (((BlockStmt)fParentNode).getLastStmt() == this) //##58
      //##59   ((BlockStmtImpl)fParentNode).setLastStmt(lStmt);  //##58
    }else { // This is a statement whose parent is not a block.  BEGIN
     // Record linkages and cut linkages.
     HIR lParent2 = (HIR)getParent();
     Stmt lPrev2 = getPreviousStmt();
     Stmt lNext2 = getNextStmt();
     int lChildNum = getChildNumber();
     cutParentLink();
     fNextStmt = null;
     fPrevStmt = null;
     Stmt lCombinedStmt = lStmt.combineStmt(this, false);
     // Set the recorded linkages to the combined statement.
     if ((lParent2 != null)&&(lChildNum > 0))
       lParent2.replaceSource(lChildNum, lCombinedStmt);
     ((StmtImpl)lCombinedStmt).fNextStmt = lNext2;
     ((StmtImpl)lCombinedStmt).fPrevStmt = lPrev2;
     if (lPrev2 != null)
       ((StmtImpl)lPrev2).fNextStmt = lCombinedStmt;
     if (lNext2 != null)
       ((StmtImpl)lNext2).fPrevStmt = lCombinedStmt;
    }
  }
  ((StmtImpl)lStmt).printLinkage("insertPreviousStmt");
  return lStmt;
} // insertPreviousStmt

public Stmt
insertPreviousStmt( Stmt pStatement, BlockStmt pBlock ) // To be deleted
{
  return insertPreviousStmt(pStatement);
}

    /** getPreviousStmt
     *  Get the statement previous to this one.
     *  "this" should be a statement node.
     *  @return the previous statement. If there is no previous
     *      one, return null.
     **/
public Stmt
getPreviousStmt() { return fPrevStmt; }

public Stmt
combineStmt( Stmt pStmt, boolean pBeforeBranch ) {
  Stmt lLeadingStmt = this, lStmt, lResult = null;
  LabeledStmt lLabeledStmt;
  if ((pStmt == null)||(pStmt == this))
    return this;
  if (fDbgLevel > 3) //##58
    hirRoot.ioRoot.dbgHir.print( 4, "combineStmt",
      toStringShort() + " and " + pStmt.toStringShort()
      + " parent "+ fParentNode); //##60
  lStmt = pStmt;
  if ((lStmt.getParent() != null)||
      (lStmt.getNextStmt() != null)||     //##53
      (lStmt.getPreviousStmt() != null))  //##53
    //##58 lStmt = (Stmt)(lStmt.copyWithOperands());
    lStmt = (Stmt)(lStmt.copyWithOperandsChangingLabels(null)); //##58
  /* //##65
  if ((lLeadingStm instanceof LabeledStmt)&&            //##52
      (lStmt.getLabel() == null)&& //##60 REPLACE
      (((LabeledStmt)lLeadingStm).getStmt() == null)) { //##52
    ((LabeledStmt)lLeadingStm).setStmt(lStmt);          //##52
    lResult = lLeadingStm;                              //##52
   */ //##65
  //##65 BEGIN
  if (lLeadingStmt instanceof LabeledStmt) {
    LabeledStmt lLabeledStmt2 = (LabeledStmt)lLeadingStmt;
    if (lLabeledStmt2.getStmt() == null) {
      lLabeledStmt2.setStmt(lStmt);
    }else {
      lLabeledStmt2.getStmt().combineStmt(lStmt, pBeforeBranch );
    }
    lResult = lLabeledStmt2;
  //##65 END
  }else if (lLeadingStmt.getOperator() == HIR.OP_BLOCK) {
    if (pBeforeBranch) {
      ((BlockStmt)lLeadingStmt).addBeforeBranchStmt(lStmt);
    }else
      ((BlockStmt)lLeadingStmt).addLastStmt(lStmt);
    lResult = lLeadingStmt;
  }else {
    HIR lParent2 = (HIR)lLeadingStmt.getParent();
    Stmt lPrev2 = lLeadingStmt.getPreviousStmt();
    Stmt lNext2 = lLeadingStmt.getNextStmt();
    int lChildNum = lLeadingStmt.getChildNumber();
    lLeadingStmt.cutParentLink();
    ((StmtImpl)lLeadingStmt).fNextStmt = null;
    ((StmtImpl)lLeadingStmt).fPrevStmt = null;
    lResult = hirRoot.hir.blockStmt(lLeadingStmt);
    if (pBeforeBranch)
      ((BlockStmt)lResult).addBeforeBranchStmt(lStmt);
    else
      ((BlockStmt)lResult).addLastStmt(lStmt);
    //##60 if (lLeadingStm.getLabel() != null) { // Inherit LabelDefList
    //##60   lLabeledStmt = hirRoot.hir.labeledStmt(null, lResult);
    //##60   lLabeledStmt.setLabelDefList(((LabeledStmt)lLeadingStm).getLabelDefList());
    //##60   lResult = lLabeledStmt;
    //##60 }
    // Set the recorded linkages to the combined statement.
    if ((lParent2 != null)&&(lChildNum > 0))
      lParent2.replaceSource(lChildNum, lResult);
    ((StmtImpl)lResult).fNextStmt = lNext2;
    ((StmtImpl)lResult).fPrevStmt = lPrev2;
    if (lPrev2 != null)
      ((StmtImpl)lPrev2).fNextStmt = lResult;
    if (lNext2 != null)
      ((StmtImpl)lNext2).fPrevStmt = lResult;
  }
  ((StmtImpl)lStmt).printLinkage("combinedStmt");
  ((StmtImpl)lResult).printLinkage("combinedResult");  //##53
  return lResult;
} // combineStmt

public Stmt
traceLastStmt() {
  Stmt lStmt = this;
  while (((StmtImpl)lStmt).fNextStmt != null)
    lStmt = ((StmtImpl)lStmt).fNextStmt;
  return lStmt;
}

public Stmt
traceFirstStmt() {
  Stmt lStmt = this;
  while (((StmtImpl)lStmt).fPrevStmt != null)
    lStmt = ((StmtImpl)lStmt).fPrevStmt;
  return lStmt;
}

/** deleteThisStmt
 *  Delete this statement and return the next statement.
 *  "this" should be a statement node. If this is the first
 *  statement of a block, then the next statement becomes the first
 *  statement of the block. If the block contains only one statement,
 *  then the block becomes an empty block. "this" statement is not
 *  erazed but bypassed in the chain of statements.
 *  See deleteThisStmt() of LabeledStmt.
 *  @return the statement next to this statement. If there is
 *      no next statement, return null.
**/
public Stmt
deleteThisStmt() {
  Stmt lNext = fNextStmt;
  BlockStmt lBlock = null;
  if (fDbgLevel > 2) //##58
    hirRoot.ioRoot.dbgHir.print(3, "deleteThisStmt", toString());
  cutLabelLinkOfStmt(this);
  if (fPrevStmt != null)
    ((StmtImpl)fPrevStmt).fNextStmt = fNextStmt;
  if (fNextStmt != null)
    ((StmtImpl)fNextStmt).fPrevStmt = fPrevStmt;
  if (fParentNode instanceof BlockStmt) {
    if (((BlockStmt)fParentNode).getFirstStmt() == this)
      ((BlockStmtImpl)fParentNode).setFirstStmt(fNextStmt);
    if (((BlockStmt)fParentNode).getLastStmt() == this)
        ((BlockStmtImpl)fParentNode).setLastStmt(fPrevStmt);
  }
  if (isMultiBlock())
    adjustMultiBlockFlag();

  ////////////////////SF031107[
  if( fParentNode!=null )
  {
    int childcount = fParentNode.getChildCount();
    for( int i=1; i<=childcount; i++ )
      if( fParentNode.getChild(i)==this )
      {
        fParentNode.setChild(i,null);
        break;
      }
  }
  ////////////////////SF031107]
  if (fPrevStmt != null)
    ((StmtImpl)fPrevStmt).printLinkage("prev of deleted node");
  if (lNext != null)
  ((StmtImpl)lNext).printLinkage("next of deleted node");
  if ((fDbgLevel > 4)&&(fParentNode != null)&&
      (fParentNode instanceof BlockStmt))
    hirRoot.ioRoot.dbgHir.print(5, " first stmt of block " +
      IoRoot.toStringObjectShort(fParentNode.getChild1()));
  fNextStmt = null;
  fPrevStmt = null;
  return lNext;
} // deleteThisStmt

public void
cutLabelLinkOfStmt( Stmt pStmt )
{
  Stmt lStmt;
  HirList lLabelDefList;
  LabelDef lLabelDef;
  Label    lLabel;
  for ( HirIterator lIterator = pStmt.hirIterator(pStmt);
        lIterator.hasNextStmt(); ){ //##60
    lStmt = lIterator.nextStmt(); //##60
    if (lStmt != null) {
      if (fDbgLevel > 2) //##58
        hirRoot.ioRoot.dbgHir.print(3, " cutLabelLinkOfStmt " + lStmt.toStringShort());
      if (lStmt instanceof LabeledStmt){
        lLabelDefList = (HirList)((LabeledStmt)lStmt).getLabelDefList();
        for ( java.util.Iterator lLabelIterator = lLabelDefList.iterator();
              lLabelIterator.hasNext(); ) {
          lLabelDef = (LabelDef)lLabelIterator.next();
          if (lLabelDef != null) {
            lLabel = lLabelDef.getLabel();
            lLabel.setHirPosition(null);
            if (fDbgLevel > 2) //##58
              hirRoot.ioRoot.dbgHir.print(3, "label " + lLabel.toStringShort());
          }
        }
      }
    }
  }
} // cutLabelLinkOfStmt

    /** deleteNextStmt
     *  Delete the statement next to this statement and return the
     *  statement next ot the next statement, which may be null.
     *  "this" should be a statement node. The next statement is not
     *  erazed but bypassed in the chain of statements.
     *  @return the statement next to the next statement. If there is
     *      no next statement, return null without any deletion.
     **/
public Stmt
deleteNextStmt() {
  if (fNextStmt != null)
    return fNextStmt.deleteThisStmt();
  else
    return null;
} // deleteNextStmt

    /** deletePreviousStmt
     *  Delete the statement that preceeds this statement and return
     *  a statement previous to the previous one. If there is no
     *  previous statement, do nothing.
     *  "this" should be a statement node. If the previous one is
     *  the first statement of a block, then this statement becomes
     *  the first statement of the block. The previous statement is not
     *  erazed but bypassed in the chain of statements.
     *  @return the statement previous to the previous statement.
     *     If there is no previous statement or no previous to the
     *     previous statement, return null.
     **/
public Stmt
deletePreviousStmt() {
  if (fPrevStmt != null)
    return fPrevStmt.deleteThisStmt();
  else
    return null;
} // deletePreviousStmt

public void
isolateThisStmt()
{
  Stmt lNext = fNextStmt;
  BlockStmt lBlock = null;
  if (fPrevStmt != null)
    ((StmtImpl)fPrevStmt).fNextStmt = null;
  if (fNextStmt != null)
    ((StmtImpl)fNextStmt).fPrevStmt = null;
  fNextStmt = null;
  fPrevStmt = null;
  cutParentLink();
} // isolateThisStmt

public Stmt  //##56
replaceThisStmtWith( Stmt pStmt )
{
  Stmt lResult = pStmt; //##56
  if (pStmt == null)
    deleteThisStmt();
  else if (this == pStmt)
    return lResult;
  else {
    if (fDbgLevel > 2) //##58
      hirRoot.ioRoot.dbgHir.print(3, "replaceThisStmtWith",
      pStmt.toString() + " this " + toString());
    if ((pStmt.getParent() == this)||
        (getParent() == pStmt)) {
      hirRoot.ioRoot.msgRecovered.put(1112,
        "self recursion in replaceThisStmtWith " + toStringShort()
        + " " + pStmt.toStringShort());
      return lResult;
    }
    ((StmtImpl)pStmt).fNextStmt = fNextStmt;
    ((StmtImpl)pStmt).fPrevStmt = fPrevStmt;
    ((StmtImpl)pStmt).fParentNode = fParentNode;
    if (fPrevStmt != null)
      ((StmtImpl)fPrevStmt).fNextStmt = pStmt;
    if (fNextStmt != null)
      ((StmtImpl)fNextStmt).fPrevStmt = pStmt;

    if( fParentNode!=null )
    {
      int childcount = fParentNode.getChildCount();
      for( int i=1; i<=childcount; i++ )
        if( fParentNode.getChild(i)==this )
        {
          fParentNode.setChild(i,pStmt);
          break;
        }
    }

    fPrevStmt = null;
    fNextStmt = null;
  }
  cutParentLink();
  cutLabelLinkOfStmt(this);
  printLinkage("replaceThisStmtWith-result"); //##70
  //##81 return lResult;  //##56
  return pStmt; //##81
} // replaceThisStmtWith


    /** attachLabel
     *  Attach the definition of a label (pLabel) to this statement. (##2)
     *  A statement may have multiple labels (set of labels).        (##2)
     *  If this statement has already a label different from pLabel, then
     *  pLabel is added as the last label attached to the statement. (##2)
     *  If pLabel is already attached to this statement, then
     *  pLabel is not attached to avoid duplication.
     *  "this" should be a statement node.
     *  @param pLabel label to be attached to this statement.
     *  Note:                                                     (##2)
     *    Let attach label L1 to statement S and denote the resultant
     *    statement as S1, and let attach label L2 to S1 and denote the
     *    resultant statement as S2, then we say that S is attached with
     *    labels L1 and L2, or S has set of labels L1 and L2.
     *    In this case, L2 is the heading label of S and the label
     *    next to L2 is L1. (In C notation, if "L2: L1: S", S has set of
     *    labels L1 and L2, and the label next to L2 is L1.)
     **/
public LabeledStmt
attachLabel( Label pLabel ) {
  if (fDbgLevel > 3) //##58
    hirRoot.ioRoot.dbgHir.print( 5, "attachLabel", pLabel.toString() +
        " to " + toString());
  IrList lLabelDefList;
  LabeledStmt lLabeledStmt = getLabeledStmt();
  if (lLabeledStmt == null) {
    lLabeledStmt = this.attachLabelAsFirstOne(pLabel);
  }else {
    lLabelDefList = lLabeledStmt.getLabelDefList();
    if (! lLabelDefList.contains(pLabel))
      lLabelDefList.add(hirRoot.hir.labelDef(pLabel));
        // Addition should be at tail position.
  }
  pLabel.setHirPosition(lLabeledStmt);
  if (fDbgLevel > 3) //##58
    hirRoot.ioRoot.dbgHir.print( 5, "return " + lLabeledStmt.toString());
  return lLabeledStmt;
} // attachLabel

public LabeledStmt
attachLabelAsFirstOne( Label pLabel ) {
  Stmt        lPrev, lNext;
  HIR         lParent;
  IrList      lLabelDefList;
  int         lChildNumber;
  LabeledStmt lLabeledStmt = getLabeledStmt();
  if (fDbgLevel > 3) //##58
    hirRoot.ioRoot.dbgHir.print( 5, "attachLabelAsFirstOne",
        pLabel.toString() + " to " + toString());
  if (lLabeledStmt == null) {
    lPrev   = getPreviousStmt();
    lNext   = getNextStmt();
    lParent = (HIR)getParent();
    lChildNumber = this.getChildNumber();
    lLabeledStmt = hirRoot.hir.labeledStmt(pLabel, this);
    ((StmtImpl)lLabeledStmt).fPrevStmt = lPrev;
    if (lPrev != null)
      ((StmtImpl)lPrev).fNextStmt = lLabeledStmt;
    ((StmtImpl)lLabeledStmt).fNextStmt = lNext;
    if (lNext != null)
      ((StmtImpl)lNext).fPrevStmt = lLabeledStmt;
    ((StmtImpl)lLabeledStmt).fParentNode = lParent;
    if (lChildNumber > 0)
      lParent.setChild(lChildNumber, lLabeledStmt);
    fPrevStmt = null;
    fNextStmt = null;
  }else {
    lLabelDefList = lLabeledStmt.getLabelDefList();
    if (! lLabelDefList.contains(pLabel)) {
      lLabelDefList.add(0, hirRoot.hir.labelDef(pLabel));
        // Add at the first position.
    }else { // pLabel is already contained.
      if (lLabelDefList.getFirst() != pLabel) { // Not at 1st position.
        lLabelDefList.remove(lLabelDefList.indexOf(pLabel)); // Remove it
        lLabelDefList.add(0, hirRoot.hir.labelDef(pLabel)); // add at 1st position.
      }
    }
  }
  if (pLabel != null)
    pLabel.setHirPosition(lLabeledStmt);
  if (fDbgLevel > 3) //##58
    hirRoot.ioRoot.dbgHir.print( 5, "return " + lLabeledStmt.toString());
  return lLabeledStmt;
} // attachLabelAsFirstOne

public IrList
getLabelDefList() {             // (##4)
  LabeledStmt lLabeledStmt = getLabeledStmt();
  if (lLabeledStmt == null) {
    return null;
  }else
    return (IrList)lLabeledStmt.getLabelDefList();
} // getLabelDefList

    /** getLabel
     *  Get the label attached to this statement.
     *  "this" should be a statement node.
     *  @return the label attached to this statement,
     *      return null if no label is attached to this statement.
     *      If the statement has multiple labels, then the heading (##2)
     *      one of the labels is returned.
     **/
public Label
getLabel() {
  IrList lLabelDefList;
  lLabelDefList = getLabelDefList();
  if (lLabelDefList == null)
    return null;
  else
    return ((LabelDef)(lLabelDefList.getFirst())).getLabel();
} // getLabel

public Stmt
getStmtWithLabel( Label pLabel ){
  if (hirRoot.symRoot.subpCurrent != null)
    return hirRoot.symRoot.subpCurrent.getStmtWithLabel(pLabel);
  else
    return null;
}

public LabeledStmt
getLabeledStmt() {
  if (getOperator() == HIR.OP_LABELED_STMT)
    return (LabeledStmt)this;
  else {
    if ((fParentNode != null)&&
        (fParentNode.getOperator() == HIR.OP_LABELED_STMT))
      return (LabeledStmt)fParentNode;
  }
  return null;
} // getLabeledStmt

public LabeledStmt
backTraceLabeledStmt() {
  return null;            //##
} // backTraceLabeledStmt

/** getBlockStmt
 *  Get the block statement containing this statement.
 *  @return BlockStmt containing this statement.
 *      If this statement is not contained in BlockStmt,
 *      return null.
**/
public BlockStmt
getBlockStmt()
{
  IR   lParent;
  Stmt lStmt = this;
  while (lStmt != null) {
     if (lStmt.getOperator() == HIR.OP_BLOCK) {
       if (fDbgLevel > 3) //##60
         hirRoot.ioRoot.dbgHir.print( 7, "getBlockStmt", lStmt.toString()); //##60
       return (BlockStmt)lStmt;
     }else
    if ((((StmtImpl)lStmt).fParentNode != null)&&
             (((StmtImpl)lStmt).fParentNode.getOperator()
                                 == HIR.OP_BLOCK)) {
      if (fDbgLevel > 3) //##58
        hirRoot.ioRoot.dbgHir.print( 8, "getBlockStmt",  //##60
            ((StmtImpl)lStmt).fParentNode.toString());
      return (BlockStmt)(((StmtImpl)lStmt).fParentNode);
    }
    lParent = lStmt.getParent();
    while ((lParent != null)&&(!(lParent instanceof Stmt)))
      lParent = lParent.getParent();
    if (lParent == null)
      return null;
    lStmt = (Stmt)lParent;
    if (fDbgLevel > 3) //##58
      hirRoot.ioRoot.dbgHir.print( 8, " parent " + hirRoot.ioRoot.toStringObject(lStmt)); //##60
  }
  if (fDbgLevel > 3) //##58
    hirRoot.ioRoot.dbgHir.print( 8, "getBlockStmt", "null"); //##60
  return null;
} // getBlockStmt

/* //##60
public BlockStmt
getBlockStmtPrev() {  // DELETE
  Stmt lStmt = this;
  while (lStmt != null) {
     if (lStmt.getOperator() == HIR.OP_BLOCK) {
       hirRoot.ioRoot.dbgHir.print( 7, "getBlockStmt", lStmt.toString()); //##60
       return (BlockStmt)lStmt;
     }else
    if ((((StmtImpl)lStmt).fParentNode != null)&&
             (((StmtImpl)lStmt).fParentNode.getOperator()
                                 == HIR.OP_BLOCK)) {
      if (fDbgLevel > 0) //##58
        hirRoot.ioRoot.dbgHir.print( 7, "getBlockStmt", //##60
            ((StmtImpl)lStmt).fParentNode.toString());
      return (BlockStmt)(((StmtImpl)lStmt).fParentNode);
    }
    lStmt = ((StmtImpl)lStmt).fPrevStmt;
    if (fDbgLevel > 0) //##58
      hirRoot.ioRoot.dbgHir.print( 8, " PrevStmt " + hirRoot.ioRoot.toStringObject(lStmt)); //##60
  }
  if (fDbgLevel > 0) //##58
    hirRoot.ioRoot.dbgHir.print( 7, "getBlockStmt", "null"); //##60
  return null;
} // getBlockStmtPrev
*/ //##60

public boolean
isMultiBlock() { return fMultiBlock; }

public void              // Adjust the multi block flag of the parent
adjustMultiBlockFlag() { // by the deletion of this statement.
  if (fParentNode == null)
    return;
  if (fParentNode.getOperator() == HIR.OP_BLOCK) {
    ((StmtImpl)fParentNode).fMultiBlock = false;
    for (Stmt lStmt = ((BlockStmt)fParentNode).getFirstStmt();
         lStmt != null;
         lStmt = lStmt.getNextStmt()) {
      if ((lStmt.getLabel() != null)||lStmt.isMultiBlock()) {
        ((StmtImpl)fParentNode).fMultiBlock = true;
        break;
      }
    }
  }
} // adjustMultiBlockFlag

public void              // Adjust the multi block flag of the parent
adjustMultiBlockFlag( Stmt pStmt ) {  // by the insertion of this statement.
  if ((fParentNode == null)||(! pStmt.isMultiBlock()))
    return;
  ((StmtImpl)fParentNode).fMultiBlock = true;
} // adjustMultiBlockFlag

public boolean
isLoopStmt() { return false; }

public boolean
isBranchStmt() {
  switch (getOperator()) {
  //##60 case HIR.OP_IF:
  case HIR.OP_JUMP:
  //##60 case HIR.OP_SWITCH:
  //##60 case HIR.OP_CALL:
  case HIR.OP_RETURN:
    return true;
  case HIR.OP_LABELED_STMT:
    if (((LabeledStmt)this).getStmt() == null) //##60
      return false; //##60
    else  //##60
      return ((LabeledStmt)this).getStmt().isBranchStmt();
  default:
    return false;
  }
} // isBranchStmt

/** Get the ancestor control statement (IfStmt, LoopStmt, SwitchStmt)
 * containing pHir as conditional Exp or ExpStmt.
 * @return the ancestor control statement if found,
 *  or return null if pHir is not a conditional Exp of control Stmt.
 */
public Stmt
ancestorControlStmtOfConditionalExp(HIR pHir)
{
  Stmt lResult = null; //##71
  HIR lHir = pHir;
  if (pHir == null)
    return null;
  if (fDbgLevel > 3)
    hirRoot.ioRoot.dbgHir.print(4, "ancestorControlStmtOfConditionalExp",
      pHir.toStringWithChildren()); //##71
  Stmt lContainingStmt = (Stmt)lHir.getStmtContainingThisNode();
  HIR lParent = lContainingStmt;
//##71 while (lParent instanceof LabeledStmt)
  //##71   lParent = (HIR)lParent.getParent();
  //##71 BEGIN
  if (lParent instanceof ExpStmt)
    lParent = (HIR)lParent.getParent();
  while (lParent instanceof LabeledStmt)
    lParent = (HIR)lParent.getParent();
  if (fDbgLevel > 3)
    hirRoot.ioRoot.dbgHir.print(5, " lParent:" + lParent.toStringShort()); //##71
  //##71 END
  if (lParent instanceof IfStmt) {
    if (((IfStmt)lParent).getIfCondition().contains(lHir))
      lResult = (Stmt)lParent;
  }else if (lParent instanceof LoopStmt) {
    /* //##71
    LoopStmt lLoopStmt = (LoopStmt)lParent;
    if ((lLoopStmt.getLoopStartCondition() != null)&&
        (lLoopStmt.getLoopStartCondition().contains(lHir)))
      lResult = lLoopStmt;
    else if ((lLoopStmt.getLoopEndCondition() != null)&&
        (lLoopStmt.getLoopEndCondition().contains(lHir)))
    */ //##71
   if ((lContainingStmt instanceof ExpStmt)||
       (lHir instanceof ExpStmt)||
       (lHir.getType() == hirRoot.symRoot.typeBool))
     lResult = (Stmt)lParent;
  }else if (lParent instanceof SwitchStmt) {
    if (((SwitchStmt)lParent).getSelectionExp().contains(lHir))
      lResult = (Stmt)lParent;
  }
  if (fDbgLevel > 3)
    hirRoot.ioRoot.dbgHir.print(4, " result " + lResult); //##71
  return lResult;
} //ancestorControlStmtOfConditionalExp

/** Combine pStmt with conditional expression part pCond
 * of control statement so that pStmt should be executed before
 * pCond.
 * See IfStmt, LoopStmt, SwitchStmt where this method is actually
 * defined.
 * @param pStmt statement to be executed before pCond.
 * @param pCond conditional expression to be combined with pStmt.
 */
public void
combineWithConditionalExp(Stmt pStmt, HIR pCond) //##53
{
  return;
} // combineWithConditionalExp

public Object
clone()  throws CloneNotSupportedException {
  Stmt lTree;
  try {
    lTree = (Stmt)super.clone();
    if (fDbgLevel > 4) //##58
      hirRoot.ioRoot.dbgHir.print( 9, "clone of Stmt " + lTree.toString());
  }catch (CloneNotSupportedException e) {
    hirRoot.ioRoot.msgRecovered.put(1100,
      "CloneNotSupportedException(StmtImpl) " + this.toString());
    return null;
  }
  ((StmtImpl)lTree).fMultiBlock = fMultiBlock;
  // fNextStmt, fPrevStmt are set in BlockStmt.clone().
  ((StmtImpl)lTree).fNextStmt = null;
  ((StmtImpl)lTree).fPrevStmt = null;
  return (Object)lTree;
} // clone

/////////////////////////////////////// S.Fukuda 2002.10.30 begin
public final void setFileName(String pFileName)
{
  fFileName = pFileName;
}
public final String getFileName()
{
  if (fFileName != null) //##62
    return fFileName;
  else //##62
    return hirRoot.ioRoot.getSourceFilePath(); //##62
}
public final void setLineNumber(int pLineNumber){
  fLineNumber = pLineNumber;
}
public final int getLineNumber(){
  return fLineNumber;
}
public final void copyPosition(Stmt pStmt){
  fFileName   = ((StmtImpl)pStmt).fFileName;   //##62
  fLineNumber = ((StmtImpl)pStmt).fLineNumber; //##62
}
/////////////////////////////////////// S.Fukuda 2002.10.30 end
public String
toStringDetail(){
  String lString = super.toStringDetail();
  //##61 BEGIN
  if ((fFileName != null)&&
     (hirRoot.ioRoot.dbgHir.getLevel() > 1))
      lString = lString + " file " + fFileName;
  if (fLineNumber > 0)
      lString = lString + " line " + fLineNumber;
  //##61 END
  if (fDbgLevel >= 4) {
    BlockStmt lBlockStmt = getBlockStmt();
    if (lBlockStmt != null)
      lString = lString + " " + lBlockStmt.toString();
  }
  return lString;
} // toStringDetail

public void
printLinkage( String pHeader )
{
  //##70 BEGIN
  if (fDbgLevel >= 1) {
    HIR lParent = (HIR)getParent();
    if (lParent != null) {
      if (!(lParent instanceof BlockStmt)) {
        int lChildNum = getChildNumber();
        if (lParent.getChild(lChildNum) != this) {
          hirRoot.ioRoot.msgWarning.put(" Bad parent-link in " + pHeader +
            " for " + toStringShort() + " parent " + lParent.toStringShort()
            + " child " + lChildNum);
        }
      }
    }
    Stmt lPrev = getPreviousStmt();
    if (lPrev != null) {
      if (lPrev.getNextStmt() != this) {
        hirRoot.ioRoot.msgWarning.put(" Bad previous-link in " + pHeader +
         " for " + toStringShort() + " prev " + lPrev.toStringShort());
      }
    }
    Stmt lNext = getNextStmt();
    if (lNext != null) {
      if (lNext.getPreviousStmt() != this) {
        hirRoot.ioRoot.msgWarning.put(" Bad next-link in " + pHeader +
         " for " + toStringShort() + " next " + lNext.toStringShort());
      }
    }
  }
  //##70 END
  if (fDbgLevel >= 7) {
    hirRoot.ioRoot.dbgHir.print(1, " " + pHeader, toStringShort());
    hirRoot.ioRoot.dbgHir.printObject(1, "prev", fPrevStmt);
    hirRoot.ioRoot.dbgHir.printObject(1, "next", fNextStmt);
    hirRoot.ioRoot.dbgHir.printObject(1, "parent", getParent());
  }
} // printLinkage

} // StmtImpl class
