/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow;

import coins.ir.IR;


/** BBlockSubtreeIterator interface  // (##6)
 *
 *  Basic block subtree iterator
 *  to traverse top-subtree in a basic block.
**/
public interface BBlockSubtreeIterator {
    /** next:
     *  Get the next top subtree in the basic block indicated by this iterator.
     *  If no one is left, return null.
     *  The end of top subtree iteration should be checked not by null of next()
     *  but by hasNext() because there might be a case where the last top
     *  subtree is null (in such case as there remains a sequence of nodes but
     *  no top subtree is remaining).                     //##7
    **/
    public IR next();

    /** hasNext:
     *  @return true if there remains thte next top subtree in the basic block.
    **/
    public boolean hasNext();
}
 // BBlockStmtIterator interface
