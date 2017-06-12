/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.ir.IrList;
import coins.sym.Label;

/**
 *
 * LabeledStmt Labeled statement interface
 *
**/
public interface
LabeledStmt extends Stmt
{

Stmt
getStmt();  // Get statement part of this labeled statement.

/**
 * Set the statement part of this labeled statement.
 * @param pStmt Statement to be set.
 */
public void
setStmt(Stmt pStmt); // Set the statement part of this labeled statement.

/**
 * Set thte label definition list of this statement.
 * @param pLabelDefList label definition list to be set.
 */
public void
setLabelDefList( IrList pLabelDefList );

/** deleteLabel
<PRE>
 *  Delete LabelDef having pLabel as its label from
 *  the list of LabelDef nodes of this statement.
 *  If no label remains after deletion,
 *  this statement is changed to a statement without label.
 *  If no label remains and statement body is either null or
 *  NullNode, then this statement is deleted and null is returned.
</PRE>
 *  @param pLabel label to be deleted.
 *  @return the statement without pLabel or return null.
**/
public Stmt
deleteLabel( Label pLabel );

/** explicitLabelReference
<PRE>
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
</PRE>
 *  @return a LabelNode refering explicitly some label of this
 *      statement. If there is no explicit reference, return null.
**/

//##62 public LabelNode
//##62 explicitLabelReference();

/** replaceLabelNodesReferingThisStmtToNewOne
 *  Replace each LabelNode that refers a label defined by this statement
 *  to a LabelNode having pNewLabel.
 *  If this statement has multiple labels, all LabelNodes refering
 *  them are replaced by instanciating new LabelNode.
 *  @param pNewLabel Label to which refering LabelNodes are to be changed.
**/
public void
replaceLabelNodesReferingThisStmtToNewOne( Label pNewLabel );

////////////////////SF031111[
/**
  Merging of LabeledStmt.
  The label which given LabeledStmt includes is added to this LabeledStmt.
  However, if the same kind of label as the added label already exists,
  this method replaces instead of adds.
  Moreover, the label not referred is not added.

  @param  from  LabeledStmt
**/
public void
merge(LabeledStmt from);
////////////////////SF031111]

} // LabeledStmt interface
