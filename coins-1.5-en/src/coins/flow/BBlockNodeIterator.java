/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.flow;

import coins.ir.IR;
import coins.ir.hir.HIR;

/** BBlockNodIterator inteterface
 *
 *  Basic block node iterator
 *  to traverse node in a basic block.
 *  in the order of from top to bottom,
 *  from left to right.
 *  If the basic block containes a list of subtrees,
 *  then the list is traversed from head to tail
 *  traversing each subtree.
 **/
public interface
  BBlockNodeIterator
{

  /** next
   *  Get the next node in this basic block.
   *  By repetitively invoking "next", all nodes in the basic block
   *  are traversed.
   **/
  public IR
    next();

  /** hasNext
   *  @return true if there is next node remaining in the basic block.
   **/
  public boolean
    hasNext();

//##62  public IR      // Get the node that is an instance of Stmt
//##62    nextStmt(); //##60

//##62 public boolean
//##62   hasNextStmt();

  /** getNextExecutableNode
   *  Get the node that refer/set data or change control flow directly.
   *  The iterator skips such non-executable nodes as
   *  labelNode, blockNode, listNode, stmtNode,
   *  ifNode, forNode, whileNode, untilNode, switchNode,
   *  progNode, subpDefNode, labelDefNode, infNode, subpNode,
   *  typeNode, labeledStmtNode with non-null Stmt body, nullNode
   *  and get executable statement body or expression
   *  under the skipped node.
   *  If a labeled statement has null statement body,
   *  it is not skipped.
   **/
  public HIR
    getNextExecutableNode();

} // BBLockNodeIterator interface
