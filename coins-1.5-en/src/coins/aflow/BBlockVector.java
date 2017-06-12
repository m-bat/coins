/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow;

import coins.aflow.util.BitVector;


/** BBlockVector interface
 *  Each bit in this <code>BitVector</code> represents a <code>BBlock</code> object whose number property (that can be queried by <code>getBBlockNumber()</code>) corresponds to the bit position. Used e.g. when constructing the dominator tree.
**/
public interface BBlockVector extends BitVector {
    /**
     * Returns the <code>SubpFlow</code> object associated with this <code>BBlockVector</code> object.
     */
    SubpFlow getSubpFlow();
}
 // BBlockVector interface
