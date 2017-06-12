/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.sym.Label;

//========================================//
//                        Jan. 19, 2001.
//                            (##2): modified on Nov. 2000.
//                            (##3): modified on Jan. 2001.

/**
 *  If-statement.
**/
public interface
IfStmt extends Stmt
{

/** IfStmt        (##3)
 *  Build an if-statement.
 *  @param pCondition conditional expression subtree.
 *  @param pThenPart then-part statement that is executed when
 *      pCondition is true.
 *  @param pElsePart else-part statement that is executed when
 *      pCondition is false.
 *  @return the subtree of the built statement
 *      with statement body operator opIf.
 **/
// Constructor      (##3)
// IfStmt( Exp pCondition, Stmt pThenPart, Stmt pElsePart );

/** getIfCondition  Get the condition part of if-statement.
 *  "this" should be IfStmt statement built by ifStmt.
 *  @return the pCondition expression subtree if
 *      "this" is if-statement, otherwise return null.
 **/
Exp
getIfCondition();

/** setIfCondition  Set the condition part of if-statement.
 *  "this" should be IfStmt statement built by ifStmt.
 *  @param pCondition Condition expression to be set.
 **/
void
setIfCondition(Exp pCondition);

/** getThenPart
 *  Get the then-part of if-statement.
 *  "this" should be IfStmt statement built by ifStmt.
 *  @return the pThenPart subtree if "this" is if-statement,
 *      otherwise return null.
 **/
LabeledStmt 
getThenPart();

/** getElsePart
 *  Get the else-part of if-statement.
 *  "this" should be IfStmt statement built by ifStmt.
 *  @return the pElsePart subtree if "this" is if-statement,
 *      otherwise return null.
 **/
LabeledStmt 
getElsePart();

/** getEndLabel                      (##2)
 *  Get the end label of if-statement.
 *  The end label is a label indicating the end of if-statement.
 *  "this" should be IfStmt statement built by ifStmt.
 *  @return the end label of this if-statement.
 **/
Label
getEndLabel();

/** addToThenPart
 *  Add pStmt to the tail of then-part of this if-statement.
 *  If pBeforeBranch is true, the addition is made before
 *  a branch statement in the then-part if there is a branch.
 *  If then-part is a non-block statement, then it is
 *  changed to a block statement containing pStmt.
 *  The then-part should not be null.
 *  @param pStmt Statement to be added.
 *  @param pBeforeBranch true if addition before branch point.
**/
public void
addToThenPart( Stmt pStmt, boolean pBeforeBranch );

/** addToElsePart
 *  Add pStmt to the tail of else-part of this if-statement.
 *  If pBeforeBranch is true, the addition is made before
 *  a branch statement in the else-part if there is a branch.
 *  If else-part is a non-block statement, then it is
 *  changed to a block statement containing pStmt.
 *  The else-part should not be null.
 *  @param pStmt Statement to be added.
 *  @param pBeforeBranch true if addition before branch point.
**/
public void
addToElsePart( Stmt pStmt, boolean pBeforeBranch );

/** replaceThenPart
 *  Replace the then-part of this if-statement by pNewThenPart.
 *  The label kind of the label in pNewThenPart is set to Label.THEN_LABEL.
 *  @param pNewThenPart Statement used for replacement.
**/
public void
replaceThenPart( LabeledStmt pNewThenPart );

/** replaceThenPart
 *  Replace the else-part of this if-statement by pNewElsePart.
 *  The label kind of the label in pNewElsePart is set to Label.ELSE_LABEL.
 *  @param pNewElsePart Statement used for replacement.
**/
public void
replaceElsePart( LabeledStmt pNewElsePart );

} // IfStmt interface
