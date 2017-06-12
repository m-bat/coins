/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.flow;

import coins.ir.IR;
import coins.ir.hir.HIR;

/** BBlockSubtreeIterator traverses top-subtrees in the specified BBlock.
 * IR tree should not be changed after instanciation of
 * BBlockSubtreeIteratorImpl until all invocations of
 *   hasNext() and next() are finished.
 * Traversed top-subtrees are
 *   LabeledStmt, AssignStmt, ExpStmt, ReturnStmt,
 *   IfStmt, LoopStmt, SwitchStmt
 *   Conditional expression in IfStmt and LoopStmt
 *   Case-selection expression in SwitchStmt
 *   Call subtree (irrespective of contained in ExpStmt or Exp)
**/
public interface
  BBlockSubtreeIterator
{

  /** next
   *  Get the next top subtree in the basic block indicated by this iterator.
   *  If no one is left, return null.
   *  In HIR, statements other than
   *    LabeledStmt, AssignStmt, ExpStmt, ReturnStmt,
   *    IfStmt, LoopStmt, SwitchStmt //##65
   *  are skipped because other statements does not set/refer data
   *  directly. (Other statements may set/refer data indirectly but
   *  such set/referes can be caught by above statements included
   *  in them. LabeledStmt is picked up to see BBlock boundary.)
   *  Expressions treated as top subtree in BBlockSubtreeIterator are
   *    conditional expressin in IfStmt, LoopStmt,
   *    case-selection expression in SwitchStmt,  //##65
   *    call-subtree in Exp. //##65
   *  Conditional expression in IfStmt, LoopStmt
   *  The end of top subtree iteration should be checked not by null of next()
   *  but by hasNext() because there might be a case where the last top
   *  subtree is null (in such case as there remains a sequence of nodes but
   *  no top subtree is remaining).
   **/
  public IR
    next();

  /** hasNext
   *  @return true if there remains thte next top subtree in the basic block.
   **/
  public boolean
    hasNext();

} // BBlockStmtIterator interface
