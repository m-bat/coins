/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;


/** HirIterator interface
 *  Traverse HIR node in depth first order.
**/
public interface
HirIterator  {

//====== Methods to traverse nodes in this subtree ======//

/** next
 *  Get the next node and advance current point for iteration.
 *  If the next node is IrList or HirList or ExpListExp,
 *  the elements of the list are traversed.
 *
**/
public HIR
next();

public boolean             // True if there is remaining node,
hasNext();                 // false if no node is remaining.

//##60 public void                // Replace the node to be get next time.
//##60 replaceNext( HIR pNext );  // (used in BBlockNodeIterator only.)

/** next
 *  Get the node that refer/set data or change control flow directly.
 *  If the next node is IrList or HirList,
 *  the elements of the list are traversed.
**/
public HIR                 // Get the node that refer/set data or
getNextExecutableNode();   // change control flow directly.

/** nextStmt
 *  Get the next statement skipping other nodes that are not Stmt.
 *  In compound statements, statements contained in the compound
 *  statements are get in depth-first order.
 *  @return the next statement.
**/
public Stmt
nextStmt();

public boolean             // True if there is remaining Stmt,
hasNextStmt();             // false if no Stmt is remaining.

public Stmt      // Get the node that is an instance of Stmt
getNextStmt();   // This should be replaced by nextStmt().

/* //##60
public HIR                 // See the current node without
peekCurrent();             // advancing the current point.

public HIR                 // See the next node without
peekNext();                // advancing the current point.

public int
getStackDepth();           // Used only in HirNodeIterator.

public HIR
getParentNode();           // Used only in HirNodeIterator.

*/ //##60

} // HirIterator interface

