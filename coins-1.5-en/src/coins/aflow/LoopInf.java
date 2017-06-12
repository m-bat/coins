/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow;

import java.util.List;

import coins.ir.IR;


/** LoopInf interface  //##10
 *  Interface for loop information.
**/
public interface LoopInf {
    public static final int // Flag number used in getFlag/setFlag.
     IRREDUCIBLE = 1;
    public static final int // Flag number used in getFlag/setFlag.
     HAS_CALL = 7;
    public static final int // Flag number used in getFlag/setFlag.
     HAS_PTR_ASSIGN = 8;
    public static final int // Flag number used in getFlag/setFlag.
     USE_PTR = 9;
    public static final int // Flag number used in getFlag/setFlag.
     HAS_STRUCT_UNION = 10 // Contains struct/union set/ref. //##10
    ;

    /** getParent
     *  setParent
     *  Get/set parent LoopInf of this LoopInf,
     *  where, parent LoopInf is the LoopInf corresponding to
     *  the loop directly containing the loop correnponding to
     *  this LoopInf.
     *  If there is no parent, then getParent rerurns null.
     *  @param pParent: parent LoopInf.
    **/
    public LoopInf getParent();

    public void setParent(LoopInf pParent);

    /** getFirstChild
     *  setFirstChild
     *  Get/set the first child LoopInf of this LoopInf,
     *  where, the first child LoopInf is the LoopInf corresponding to
     *  the first loop directly contained in the loop correnponding to
     *  this LoopInf.
     *  If there is no child, then getFirstChild rerurns null.
     *  @param pChild: the first child LoopInf.
    **/
    public LoopInf getFirstChild();

    public void setFirstChild(LoopInf pChild);

    /** getNextBrother
     *  setNextBrother
     *  Get/set the next brother LoopInf of this LoopInf,
     *  where, the next brother LoopInf is the LoopInf
     *  having the same parent LoopInf as this LoopInf.
     *  If there is no next brother, then getNextBrother rerurns null.
     *  @param pBrother: the next brother LoopInf.
    **/
    public LoopInf getNextBrother();

    public void setNextBrother(LoopInf pBrother);

    public List getBBlockList();

    /** getEntryBBlock:
     *  Get entry BBlock of the loop corresponding to this LoopInf.
     *  If there are several entry (irreducible loop),
     *  get the BBlock that is set by setEntryBBlock.
     *  @return thte entry BBlock.
    **/
    public BBlock getEntryBBlock();

    /** setEntryBBlock:
     *  Set entry BBlock of the loop corresponding to this LoopInf.
     *  If there are several entry (irreducible loop),
     *  select one of them as principal entry BBlock
     *  and set it as the entry BBlock.
     *  @param pEntryBBlock: principal entry BBlock.
     *  The entry BBlock is added to the BBlock list of this LoopInf
     *  and its parent.
    **/
    public void setEntryBBlock(BBlock pEntryBBlock);

    /** addBBlock:
     *  Add pBBlock to the BBlock list of this LoopInf.
     *  If pBBlock has not yet linked to LoopInf, then it is linked
     *  to this LoopInf (inner-most LoopInf containing pBBlock).
     *  If this LoopInf has parent, then pBBlock is added
     *  to the parent LoopInf, too.
     *  @param pBBlock: BBlock to be added.
    **/
    public void addBBlock(BBlock pBBlock);

    /** deleteBBlock:
     *  Delete pBBlock from the BBlock list of this LoopInf.
     *  The linkage to LoopInf of pBBlock is nullified.
     *  If this LoopInf has parent, then pBBlock is deleted
     *  from the parent LoopInf, too.
     *  @param pBBlock: BBlock to be deleted.
    **/
    public void deleteBBlock(BBlock pBBlock);

    /** getAlternateEntryNodeList:
     *  Get the list of alternate entry node.
     *  If there is no alternate entry node (reducible loop)
     *  then return null.
     *  @return the list of alternate entry node.
    **/
    public List getAlternateEntryNodeList();

    /** getAlternateEntryBBlockList:
     *  Get the list of alternate entry BBlock.
     *  If there is no alternate entry BBlock (reducible loop)
     *  then return null.
     *  @return the list of alternate entry BBlock.
    **/
    public List getAlternateEntryBBlockList();

    /** addAlternateEntryNode:
     *  Add pEntryNode as an alternate entry node of this LoopInf.
     *  Duplication is avoided.
     *  @param pEntryNode: an alternate entry node.
    **/
    public void addAlternateEntryNode(IR pEntryNode);

    /** addAlternateEntryBBlock:
     *  Add pEntryBBlock as an alternate entry BBlock of this LoopInf.
     *  Duplication is avoided.
     *  @param pEntryBBlock: an alternate entry BBlock.
    **/
    public void addAlternateEntryBBlock(BBlock pEntryBBlock);

    /** getFlag:
     *  setFlag:
     *  getFlag returns the value (true/false) of the flag indicated
     *  by pFlagNumber.
     *  setFlag sets the flag of specified number.
     *  @param pFlagNumber: flag identification number (see below).
     *  @param pYesNo: true or false to be set to the flag.
    **/
    boolean getFlag(int pFlagNumber);

    void setFlag(int pFlagNumber, boolean pYesNo);

    /** propagateFlag:
     *  Set flag of pFlagNumber to be true and if this has parent,
     *  then set the same flag of the parent and its ancestors.
    **/
    public void propagateFlag(int pFlagNumber);

    /** print:
     *  Print this LoopInf and its children and brother LoopInf
     *  for debugging purpose
     *  if dbgFlow >= pDebigLevel.
    **/
    public void print(int pDebugLevel);
}
 // LoopInf interface
