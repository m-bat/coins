/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.HirRoot;
import coins.sym.SymTable;

/**
 * Block representing a sequence of statements.
**/
public class
BlockStmtImpl extends StmtImpl implements BlockStmt
{

  SymTable fLocalSymTable;
  Stmt     fLastStmt     = null;
  boolean  fSubpBodyFlag = false;  // true if this represents the body
                                   // of a subprogram. //### UNNECESSARY ?

    /** BlockStmt
     *  Build block statement that make a sequence of statement to be
     *      treated as one statement.
     *  pStmtSequence sequence of statements to be included
     *      in the block. It may be null at the creation of block statement
     *      if statements are to be added by AddFirstStmt, AddNextStmt
     *      of HIR_interface.
     *  Return the subtree of the built statement
     *      with statement body operator opBlock.
     *  @param pHirRoot instance of HirRoot common to all HIR nodes.
     **/
public
BlockStmtImpl( HirRoot pHirRoot )
{
    super(pHirRoot, HIR.OP_BLOCK);
    fChildCount = 1;
    fType = hirRoot.symRoot.typeVoid;
}

public
BlockStmtImpl( HirRoot pHirRoot, Stmt pStmtSequence )
{
    super(pHirRoot, HIR.OP_BLOCK);
    fChildCount = 1;
    fType = hirRoot.symRoot.typeVoid;
    if (pStmtSequence != null)
        addFirstStmt(pStmtSequence);
}

public SymTable
getSymTable() { return fLocalSymTable; }

public void
setSymTable( SymTable pSymTable ) { fLocalSymTable = pSymTable; }

public Stmt
addFirstStmt( Stmt pStatement ) {
  if (pStatement == null)
    return (Stmt)fChildNode1;
  if (fDbgLevel > 0) { //##58
    hirRoot.ioRoot.dbgHir.print( 5, "addFirstStmt",
          hirRoot.ioRoot.toStringObject(pStatement));
    if (pStatement instanceof LabeledStmt)
      hirRoot.ioRoot.dbgHir.print( 5, " " + ((LabeledStmt)pStatement).getLabel().toStringShort()); //###
  }
  if (fChildNode1 == null) { // Child1 is the first statement of a block.
    fChildNode1 = pStatement;
    fLastStmt  = pStatement;
    ((StmtImpl)pStatement).fNextStmt = null;  //##53
    ((StmtImpl)pStatement).fPrevStmt = null;  //##53
  }else {               // Already there is a child. Insert in front of it.
    if (pStatement != fChildNode1) {  // Escape from self loop.
      ((StmtImpl)pStatement).fNextStmt = (Stmt)fChildNode1;
      ((StmtImpl)fChildNode1).fPrevStmt = pStatement;
    }
    fChildNode1 = pStatement;  // First statement.
  }
  pStatement.cutParentLink(); //##58
  ((HIR_Impl)pStatement).fParentNode = this;
  if ((pStatement.getLabel() != null)||pStatement.isMultiBlock())
    fMultiBlock = true;
  if (fDbgLevel > 0) //##58
    hirRoot.ioRoot.dbgHir.print( 6, " first "
                 + hirRoot.ioRoot.toStringObject(fChildNode1)
                 + " last " + hirRoot.ioRoot.toStringObject(fLastStmt));
  return (Stmt)fChildNode1;
} // addFirstStmt

public Stmt
addLastStmt( Stmt pStatement ) {
  if (pStatement == null)
    return fLastStmt;
  if (fDbgLevel > 0) //##58
    hirRoot.ioRoot.dbgHir.print( 5, "addLastStmt",
                 pStatement.toString());
  if (pStatement instanceof LabeledStmt) //###
    hirRoot.ioRoot.dbgHir.print( 5, " " + ((LabeledStmt)pStatement).getLabel().toStringShort()); //###
  if (fLastStmt == null) { // Child1 is the first statement of a block.
    addFirstStmt(pStatement);
    fLastStmt = pStatement;
  }else  {
    if (fDbgLevel > 0) //##67 // THIS CAUSES AbstractMethodError for Fortran program Nakata Mail 060125:w

      hirRoot.ioRoot.dbgHir.print( 5, "fLastStmt", fLastStmt.toString()); //###
    if (pStatement != fLastStmt) {
      ((StmtImpl)fLastStmt).fNextStmt = pStatement;
      ((StmtImpl)pStatement).fPrevStmt = fLastStmt;
      pStatement.cutParentLink(); //##58
      ((StmtImpl)pStatement).fParentNode = this;
      ((StmtImpl)pStatement).fNextStmt = null;  //##58
      //##58 fLastStmt = pStatement;
    }
  }
   fLastStmt = pStatement;
  if ((pStatement.getLabel() != null)||pStatement.isMultiBlock())
    fMultiBlock = true;
  if (fDbgLevel > 0) //##58
    hirRoot.ioRoot.dbgHir.print( 6, " last "
     + hirRoot.ioRoot.toStringObject(fLastStmt) + " previous "
     + hirRoot.ioRoot.toStringObject(fLastStmt.getPreviousStmt()));
  return pStatement;
} // addLastStmt

public void
addBeforeBranchStmt( Stmt pStatement ) {  // (##6)
  Stmt lStmt;
  if (fDbgLevel > 0) //##58
    hirRoot.ioRoot.dbgHir.print( 5, "addBeforeBranchStmt",
                 hirRoot.ioRoot.toStringObject(pStatement));
  lStmt = getLastStmt();
  if (fDbgLevel > 0) //##58
    hirRoot.ioRoot.dbgHir.print( 5, "lastStmt",
                 hirRoot.ioRoot.toStringObject(lStmt));
  if (lStmt != null) {
    if (lStmt.isBranchStmt())
      lStmt.insertPreviousStmt(pStatement, (BlockStmt)this);
    else if (lStmt.getOperator() == HIR.OP_BLOCK)
      ((BlockStmt)lStmt).addBeforeBranchStmt(pStatement);
    else
      addLastStmt(pStatement);
  }else
    addLastStmt(pStatement);
  if (fDbgLevel > 0) //##58
    hirRoot.ioRoot.dbgHir.print( 6, " last "
                 + hirRoot.ioRoot.toStringObject(fLastStmt));
} // addBeforeBranchStmt

public Stmt
getFirstStmt() { return (Stmt)fChildNode1; }

public Stmt
getLastStmt() { return fLastStmt; }

public void
setFirstStmt( Stmt pStmt ) {
  fChildNode1 = pStmt;
  if (pStmt != null)
    ((HIR_Impl)pStmt).fParentNode = this;
}

public void
setLastStmt( Stmt pStmt ) {
  fLastStmt = pStmt;
  if (fChildNode1 == null)
    fChildNode1 = pStmt;
}

public void
replaceFirstStmt( Stmt pStmt ) {
  Stmt lFirstStmt = getFirstStmt();
  if (lFirstStmt == null)
    addFirstStmt(pStmt);
  else {
    fChildNode1 = pStmt;
    ((StmtImpl)pStmt).fNextStmt = lFirstStmt.getNextStmt();
    ((StmtImpl)pStmt).fPrevStmt = null;
    ((StmtImpl)pStmt).fParentNode = this;
    ((StmtImpl)lFirstStmt).fParentNode = null;
  }
} // replaceFirstStmt

public boolean
getSubpBodyFlag() { return fSubpBodyFlag; }

public void
setSubpBodyFlag( boolean pFlag ) { fSubpBodyFlag = pFlag; }

public Object
clone() throws CloneNotSupportedException {
  Stmt lFirstStmt, lToStmt, lFromStmt;
  BlockStmtImpl lTree;
  try {
    lTree = (BlockStmtImpl)(super.clone());
    lTree.fLocalSymTable = fLocalSymTable;
    lTree.fSubpBodyFlag  = fSubpBodyFlag;
    lFirstStmt = lTree.getFirstStmt();  // cloned by HIR_Impl.clone().
    lTree.fLastStmt      = lFirstStmt;
    lToStmt = lFirstStmt;
    if (lToStmt != null) {
      ((StmtImpl)lToStmt).fPrevStmt = null;
      lFromStmt = getFirstStmt();  // Original child.
      while ((lFromStmt != null)&&(lFromStmt.getNextStmt() != null)) {
        lToStmt = (Stmt)(lFromStmt.getNextStmt().copyWithOperands());
        lTree.addLastStmt(lToStmt);
        lFromStmt = lFromStmt.getNextStmt();
      }
    }else
      lTree.fLastStmt = null;
  }catch (CloneNotSupportedException e) {
    hirRoot.ioRoot.msgRecovered.put(1100,
      "CloneNotSupportedException(HIR_Impl) " + this.toString());
    return null;
  }
  return (Object)lTree;
} // clone
                                   // of a subprogram.
public void
accept( HirVisitor pVisitor )
{
  pVisitor.atBlockStmt(this);
}

public String
toString()
{
  String lString = super.toString();
  if (fDbgLevel >= 5 ) {
    if (getChild1() == this)
      System.out.print(" Self recursion " + lString);
    else
      lString = lString + " first " + hirRoot.ioRoot.toStringObject(getChild1())
        + " last " + hirRoot.ioRoot.toStringObject(fLastStmt);
  }
  return lString;
}

} // BlockStmtImpl
