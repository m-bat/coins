/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.sym.SymTable;

//========================================//

/**
 *
 * Block representing a sequence of statements.
 *
**/
public interface
BlockStmt extends Stmt
{

/** getSymTable
 *  @return the symbol table containing symbols locally declared
 *      in this BlockStmt. If there is no local symbols declared
 *      in this block, then the symbol table may be null.
**/
public SymTable
getSymTable();

/** setSymTable
 *  Set pSymTable as the local symbol tabel of this BlockStmt.
 *  @param pSymTable local symbol tabel to be attached to this
 *      block.
**/
public void
setSymTable( SymTable pSymTable );

/** addFirstStmt
<PRE>
 *  Add pStatement as the first statement of this block.
 *  If this block has already has several statements, this method inserts
 *  pStatement in front of the existing statements.
 *  If pStatement is null, no statement is added and the current
 *  first statement is returned.
 *  This method  changes statement linkages (linkages
 *  get by getNextStmt(), getPreviousStmt(), getParent()). If these
 *  linkages are used later, then make a copy (by copyWithOperands())
 *  and use the copy as the parameter.
</PRE>
 *  @param pStatement Statement that was added.
 *  @return the new first statement of the block.
**/
public Stmt addFirstStmt( Stmt pStatement );

/** addLastStmt
<PRE>
 *  Add pStatement as the last statement of this block.
 *  If this block has no statement, this method does the same
 *  operation as that of addFirstStmt.
 *  Successive call of addlastStmt add a sequence of statements
 *  to this block in the order of addition.
 *  If this block has already labeled statement as the last statement
 *  where the label of the last statement is either RETURN_POINT_LABEL
 *  or LOOP_STEP_LABEL, then pStatement is inserted in front of the
 *  last labeled statement.
 *  If pStatement is null, no statement is added and the current
 *  last statement is returned.
 *  This method  changes statement linkages (linkages
 *  get by getNextStmt(), getPreviousStmt(), getParent()). If these
 *  linkages are used later, then make a copy (by copyWithOperands())
 *  and use the copy as the parameter.
</PRE>
 *  @param pStatement Statement that was added.
 *  @return the new last statement of the block.
**/
public Stmt addLastStmt( Stmt pStatement );

/** addBeforeBranchStmt    (##6)
 *  If the last statement of this block is a branch statement
 *  (jump, if, switch, call, return), then insert pStatement
 *  in front of it. If the last statement is not a branch,
 *  then add pStatement as the last statement of this block.
 *  This method  changes statement linkages (linkages
 *  get by getNextStmt(), getPreviousStmt(), getParent()). If these
 *  linkages are used later, then make a copy (by copyWithOperands())
 *  and use the copy as the parameter.
 *  @param pStatement Statement that was added.
**/
public void
addBeforeBranchStmt( Stmt pStatement );

/** getFirstStrmt
<PRE>
 *  Get the first statement of this block.
 *  If this block has no statement, then null is returned.
 *  After getting the first statement, successive call of
 *  getNextStmt() will traverse all statements in this block.
 *    for (Stmt lStmt = lBBlock.getFirstStmt(); lStmt != null;
 *         lStmt = lStmt.getNextStmt()) { ...... }
</PRE>
 *  @return the first statement of this block.
**/
public Stmt getFirstStmt();

/** getLastStmt
 *  Get the last statement of this block.
 *  If this block has no statement, then null is returned.
 *  @return the last statement of this block.
**/
public Stmt getLastStmt();

public boolean
getSubpBodyFlag(); // UNNECESSARY ?

public void
setSubpBodyFlag( boolean pFlag ); // UNNECESSARY ?

} // BlockStmt interface
