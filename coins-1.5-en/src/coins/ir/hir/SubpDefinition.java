/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.sym.Label;
import coins.sym.Subp;
import coins.sym.SymTable;

/**
 *  Subprogram definition node.
 *
**/
public interface
SubpDefinition extends HIR {

/**
 * Get the symbol table local to the subprogram.
 * @return the symbol table.
 */
public SymTable
getSymTable();

/**
 * Get the subprogram symbol of this subprogram definition.
 * @return the subprogram symbol.
 */
public Subp
getSubpSym();

/**
<PRE>
 * Get the block containing statements that initiate
 * variables local to the subprogram.
 * Note:
 *   In C language, initial value specification should be
 *   evaluated at the position where the initial value
 *   declaration statement encountered. This means that
 *   the initiation statement in HIR should be placed
 *   at the position where the declaration statement is
 *   encountered, just as executable statement. Thus, if
 *   the source language is C, getInitiationPart() will
 *   return empty BlockStmt or null.
</PRE>
 * @return the block statement containing initiation
 *   statements that should be executed at the entry point
 *   of the subprogram.
 */
public BlockStmt
getInitiationPart();

/**
 * Add pInitiationStmt to the tail of the block containing
 * initiation statements that should be executed at the entry
 * to the subprogram. (If C is the source language, this
 * method is not used.)
 * @param pInitiationStmt to be added to the initiation block.
 */
public void
addInitiationStmt( Stmt pInitiationStmt );

/**
 * Get the body part of the subprogram.
 * It contains the sequence of executable statements
 * in the subprogram.
 * @return the body part of the subprogram.
 */
public Stmt
getHirBody();

/**
 * Set pHirBody as the body part of the subprogram.
 * @param pHirBody body part to be set.
 */
public void
setHirBody( BlockStmt pHirBody );

/**
 * Get the start label that is attached at the head
 * of subprogram body.
 * @return the start label.
 */
public Label
getStartLabel();

/* //##91
public Label
getEndLabel();
*/

/** printHir
 *  Print the HIR body of this subprogram.
 *  @param pHeader Header message to be printed.
**/
public void
printHir( String pHeader );

/**
 * Get the node index attached at the first node
 * of the subprogram definition. It is the minimum
 * index of the subprogram definition.
 * @return the minimum index.
 */
public int
getNodeIndexMin();

/**
 * Get the node index attached at the last node
 * of the subprogram definition. It is the maximum
 * index of the subprogram definition.
 * @return the maximum index.
 */
public int
getNodeIndexMax();

} // SubpDefinition interface

