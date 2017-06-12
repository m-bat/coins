/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.ir.IrList;
import coins.sym.Label;

//========================================//

/** Stmt
 *  Stmt (statement) class interface.
 *  Stmt is a subclass of HIR.
 *  See HIR.
**/
public interface
Stmt extends HIR
{

/** addNextStmt
<PRE>
 *  Add a statement as the one next to this statement.
 *  If "this" is included directly in a block, append pStatement as the
 *  next statement. If "this" is the last statement in the block, then
 *  pStatement becomes the last statement in the block.
 *  If "this" is not included directly in a block, a new block is
 *  created combining "this" and pStatement, and "this"
 *  is replaced by the created block.
 *  If pStatement is null, do nothing.
 *  It is not recommended to add statement having non-null linkage
 *  (getNextStmt(), getPreviousStmt(), or getParent() are not null)
 *  because the addition will change these linkages. In such case,
 *  copy of the statement should be used (see copyWithOperands() of HIR).
</PRE>
 *  @param pStatement statement subtree to be added.
 *  @return the added statement node.
**/
public Stmt
addNextStmt( Stmt pStatement );

/** addNextStmt
 *  Add a statement as the one next to this statement.
 *  @param pStatement statement subtree to be added.
 *  @param pBlock BlockStmt that directly includes
 *      this Stmt. If it is unknown, give null.
 *  @return the added statement node.
 *  ***deprecated use addNextStmt(Stmt pStatement)
**/
//##68 public Stmt
//##68 addNextStmt( Stmt pStatement, BlockStmt pBlock ); // To be deleted

/** getNextStmt
 *  Get the statement next to "this" one.
 *  @return the statement next to "this" one.
 *      Return null if there is no next statement
 *      (if this is the last statement of a block).
**/
//## Stmt           // Declared in HIR.
//## getNextStmt(); // Already declared in HIR interface.

/**
 * Get the statement that includes this statement.
 * If there is not, return null.
 * @return the upper statement.
 */
Stmt
getUpperStmt();

/** combineStmt
<PRE>
 *  Combine this statement and pStmt making them as one statement.
 *  If this statement is a block, pStmt is added as the last statement
 *  of the block.
 *  If this statement is a labeled statement, then pStmt
 *  is combined with its statement body
 *  (((LabeledStmt)this).getStmt().combineStmt(pStmt, pBeforeBranch). //##65
 *  In other case, a new block statement
 *  including this and pStmt is created and pStmt is made
 *  as the next one to this statement.
 *  If pBeforeBranch is true and this statement has branch
 *  as the last statement (if this is a block), then
 *  pStatement is inserted before the branch statement.
 *  If pStmt is null, do nothing.
 *  It is not recommended to combine statement having non-null linkage
 *  (getNextStmt(), getPreviousStmt(), or getParent() are not null)
 *  because the addition will change these linkages. In such case,
 *  copy of the statement should be used (see copyWithOperands() of HIR).
 *  This method (combineStmt) changes statement linkages (linkages
 *  get by getNextStmt(), getPreviousStmt(), getParent()) of this
 *  statement. If these linkages are used later, then make copy and
 *  use the copy in applying combineStmt.
 *  combineStmt makes neither a copy of pStmt nore a copy of this Stmt. //##60
 *  If copy is required, then make copy before calling combineStmt.
 *  If label is to be changed so as not to make duplicated label,
 *  it is necessary to change it before calling combineStmt (see
 *  copyWithOperandsChangingLabels). //##60
 *  The combined result replaces this statement and inherit its linkages.
 *  It may be the same to
 *     Stmt ltmt3 = lStmt1.combineStmt(
 *                   ((Stmt)lStmt2.copyWithOperands(), true);
 *     lStmt1.replaceThisStmtWith(lStmt3);
</PRE>
 *  @param pStmt Statement to be combined as the next one to
 *      this statement.
 *  @param pBeforeBranch if true, insertion is done before the
 *      branch statement.
 *  @return the block statement containing pStmt.
**/
public Stmt
combineStmt( Stmt pStmt, boolean pBeforeBranch );

/** insertPreviousStmt
<PRE>
 *  Insert a statement in front of "this" one.
 *  If "this" is included directly in a block, insert pStatement
 *  in front of this statement. If "this" is the first statement in the
 *  block, then pStatement becomes the first statement in the block.
 *  If "this" is not included directly in a block, a new block is
 *  created combining pStatement and "this", and "this"
 *  is replaced by the created block.
 *  If pStatement is null, do nothing.
 *  It is not recommended to insert statement having non-null linkage
 *  (getNextStmt(), getPreviousStmt(), or getParent() are not null)
 *  because the addition will change these linkages. In such case,
 *  copy of the statement should be used (see copyWithOperands() of HIR).
</PRE>
 *  @param pStatement statement subtree to be inserted.
 *  @return the inserted statement node (pStatement).
**/
public Stmt
insertPreviousStmt( Stmt pStatement );

/** insertPreviousStmt
<PRE>
 *  Insert a statement in front of "this" one.
 *  It is not recommended to insert statement having non-null linkage
 *  (getNextStmt(), getPreviousStmt(), or getParent() are not null)
 *  because the addition will change these linkages. In such case,
 *  copy of the statement should be used (see copyWithOperands() of HIR).
</PRE>
 *  @param pStatement statement subtree to be inserted.
 *  @param pBlock Block statement containing this statement.
 *      If it is unknown, give null.
 *  @return the inserted statement node (pStatement).
**/
public Stmt
insertPreviousStmt( Stmt pStatement, BlockStmt pBlock ); // To be deleted

/** getPreviousStmt
 *  Get the statement previous to this one.
 *  @return the previous statement. If there is no previous
 *      one, return null.
**/
Stmt
getPreviousStmt();

/**
<PRE>
 * Combine pStmt with conditional expression part pCond
 * of control statement so that pStmt should be executed before
 * pCond.
 * See IfStmt, LoopStmt, SwitchStmt where this method is actually
 * defined.
</PRE>
 * @param pStmt statement to be executed before pCond
 *     (unnecessary to prepare copy by copyWithOperands).
 * @param pCond conditional expression to be combined with pStmt
 *     (unnecessary to prepare copy by copyWithOperands).
 */
public void
combineWithConditionalExp(Stmt pStmt, HIR pCond); //##53

/** Get the ancestor control statement (IfStmt, LoopStmt, SwitchStmt)
 * containing pHir as conditional Exp or ExpStmt.
 * @return the ancestor control statement if found, or return null
 *     if not found.
 */
public Stmt
ancestorControlStmtOfConditionalExp(HIR pHir);

/** deleteThisStmt
<PRE>
 *  Delete this statement and return the next statement.
 *  If this is the first statement of a block,
 *  then the next statement becomes the first statement of the block.
 *  If the block contains only one statement,
 *  then the block becomes an empty block.
 *  If this is a LabeledStmt, then the labels defined by this
 *  statement are removed from the list of labels of the
 *  current subprogram (hirRoot.symRoot.subpCurrent).
 *  "this" statement is not erazed but bypassed in the chain
 *  of statements.
</PRE>
 *  @return the statement next to this statement. If there is
 *      no next statement, return null.
**/
Stmt
deleteThisStmt();

/** deleteNextStmt
<PRE>
 *  Delete the statement next to this statement and return the
 *  statement next ot the next statement, which may be null.
 *  The next statement is not erazed but bypassed
 *  in the chain of statements.
</PRE>
 *  @return the statement next to the next statement. If there is
 *      no next statement, return null without any deletion.
**/
//# Stmt
//# deleteNextStmt();

/** deletePreviousStmt
 *  Delete the statement that preceeds this statement and return
 *  a statement previous to the previous one. If there is no
 *  previous statement, do nothing.
 *  If the previous one is the first statement of a block,
 *  then this statement becomes the first statement of the block.
 *  The previous statement is not erazed but bypassed
 *  in the chain of statements.
 *  @return the statement previous to the previous statement.
 *     If there is no previous statement or no previous to the
 *     previous statement, return null.
**/
//# Stmt
//# deletePreviousStmt();

/** cutLabelLinkOfStmt
<PRE>
 *  For labels defined in pStmt, execute setHirPosition(null)
 *  to prepare for deletion of pStmt.
 *  If pStmt is a compound statement, then all statements in
 *  pStmt are searched for labels defined in pStmt.
 *  If no label is defined by pStmt, no effect remains.
</PRE>
 *  @param pStmt Statement to be searched for label definition
 *      to cut definition point relation.
**/
public void
cutLabelLinkOfStmt( Stmt pStmt );

/** isolateThisStmt
 *  Cut previous/next link and parent link of this statement.
**/
public void
isolateThisStmt();

/** replaceThisStmtWith
<PRE>
 *  Replace this statement with pStmt which is made
 *  to have the same linkage with other nodes as this statement
 *  and this statement is made isolated.
 *  pStmt should not be a statement contained in this statement
 *  as its subtree because this statement will be deleted in this
 *  method. If this statement is to be replaced with its subtree,
 *  then copy the subtree by copyWithOperandsChangingLabels(null)
 *  and replace by the copied subtree.
</PRE>
 *  @param pStmt statement used to replace this.
 *  @return pStmt //##81
**/
//##56 public void
public Stmt  //##56
replaceThisStmtWith( Stmt pStmt );

/** copyStmt
 *  Create a statement subtree copying this statement and return it.
 *  If this statement contains a label definition, a new label is
 *  generated and label fields refering the label in the created
 *  subtree are changed to the generated label.
 *  "this" should be a statement node.
 *  @return the statement created by copying.
**/
//# Stmt
//# copyStmt();

/** moveStmtToNext
 *  Move this statement to the position next to pMoveTo statement,
 *  that is, do deleteThisStmt and pMoveTo.addNextStmt(this).
 *  "this" should be a statement node.
 *  @param pMoveTo statement next to which this statement is to
 *      be moved.
 *  @return the moved statement (this statement).
**/
//# Stmt
//# moveStmtToNext( Stmt pMoveTo );

/** moveStmtToPrevious
 *  Move this statement to the position previous to pMoveTo
 *  statement, that is, do deleteThisStmt and
 *  pMoveTo.insertPreviousStmt(this).
 *  "this" should be a statement node.
 *  @param pMoveTo statement next to which this statement is
 *      to be moved.
 *  @return the moved statement (this statement).
**/
//# Stmt
//# moveStmtToPrevious( Stmt pMoveTo );

/** attachLabel
<PRE>
 *  Attach the definition of a label (pLabel) to this statement. (##2)
 *  A statement may have multiple labels (list of labels).       (##2)
 *  If this statement has already a label different from pLabel, then
 *  pLabel is added as the last label attached to the statement. (##2)
 *  If pLabel is already attached to this statement, then
 *  pLabel is not attached to avoid duplication.
 *  "this" should be a statement node.
 *  Note:                                                     (##2)
 *    Let attach label L1 to statement S and denote the resultant
 *    statement as S1, and let attach label L2 to S1 and denote the
 *    resultant statement as S2, then we say that S is attached with
 *    labels L1 and L2, or S has list of labels L1 and L2.
 *    In this case, L2 is the heading label of S and the label
 *    next to L2 is L1. (In C notation, if "L2: L1: S", S has list
 *    of labels L1 and L2, and the label next to L2 is L1.)
 *    The first label attached to the statement represents
 *    a relation between other statement such that then-part,
 *    else-part, loop-back point, etc. (see Label interface).
</PRE>
 *  @param pLabel label to be attached to this statement.
 *  @return labeled statement to which the label is attached. (##5)
**/
LabeledStmt                  // (##5)
attachLabel( Label pLabel );

/** attachLabelAsFirstOne
 *  Attach pLabel as the first label of this statement.
**/
public LabeledStmt
attachLabelAsFirstOne( Label pLabel );

public IrList
getLabelDefList();

/** getLabel
 *  Get the label attached to this statement.
 *  If the statement has multiple labels, then the heading (##2)
 *  one of the labels is returned.
 *  @return the label attached to this statement,
 *      return null if no label is attached to this statement.
**/
Label
getLabel();

/** getLabeledStmt
 * @return thes statement if this is a LabeledStmt
 * else if the parent is LabeledStmt then return the parent
 * else return null.
 */
public LabeledStmt
getLabeledStmt();

/** getBlockStmt
 *  Get the block statement containing this statement.
 *  If this statement is not contained in BlockStmt,
 *  return null.
 *  @return BlockStmt containing this statement.
**/
public BlockStmt
getBlockStmt();

/** getStmtComment
 *  Get the comment located at the end of this statement.
 *  "this" should be a statement node.
**/
//# String
//# getStmtComment();

/** isMultiBlock
 *  true if this statement is composed of multiple basic blocks
 *  or a jump statement.
**/
public boolean
isMultiBlock();

/** isBranch
 * Return true if this statement is either
 * //##60 IfStmt, JumpStmt, SwitchStmt, CallStmt, ReturnStmt,
 * JumpStmt, ReturnStmt, //##60
 * or LabeledStmt with one of above statement body.
 * @return true if this statement is a branch statement.
**/
public boolean
isBranchStmt();

/////////////////////////////////////// S.Fukuda 2002.10.30 begin
/**
<PRE>
 * Set the absolute path of the source program file
 * containing this statement.
 * In C language, if this statement is included
 * in a file specified by #include, then give the name
 * of the file specified by #include (it may differ
 * statement by statement within a subprogram).
 * If setFileName is not called for this statement,
 * then the name of compiler input file is taken as
 * the default value by getFileName().
</PRE>
 * @param pFileName name of the source program file
 *   containing this statement.
 */
public void   setFileName(String pFileName);

/**
 * Get the absolute path of the file containing this
 * statement (see setFileName method).
 * If setFileName is not called for this statement,
 * then the name of compiler input file is returned as
 * the default value.
 * @return the absolute path of the file containing this
 * statement.
 */
public String getFileName();

/**
 * Set the line number of this statement within the
 * file specified by setFileName. The first line of
 * the file is line number 1.
 * @param pLineNumber the line number of this statement within
 *     the source file.
 */
public void   setLineNumber(int pLineNumber);

/**
 * Get the line number of this statement.
 * The line number is set by setLineNumber method.
 * @return the line number of this statement.
 */
public int    getLineNumber();

/**
 * Copy the file name and line number of pStatement
 * to this statement.
 * @param pStmt statement from which file name and
 *     line number are to be copied.
 */
public void   copyPosition(Stmt pStmt);
/////////////////////////////////////// S.Fukuda 2002.10.30 end

} // Stmt interface
