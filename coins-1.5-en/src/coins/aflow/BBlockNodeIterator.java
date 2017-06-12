/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow;

import coins.ir.IR;

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
	
	/** next:
	 *  Get the next node in this basic block.
	 *  By repetitively invoking "next", all nodes in the basic block
	 *  are traversed.
	 **/
	public IR
	next();
	
	/** hasNext:
	 *  @return true if there is next node remaining in the basic block.
	 **/
	public boolean
	hasNext();
	
	/** getNextExecutableNode:
         *  Get the node that refer/set data or change control flow directly. For what nodes refer/set data or change control flow, see { FlowUtil#isExecutable(coins.ir.IR) FlowUtil.isExecutable(coins.ir.IR)}.
	 **/
	public IR
	getNextExecutableNode();
	
} // BBLockNodeIterator interface

