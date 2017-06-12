/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow;

import coins.ir.IR;


/** DefVectorIterator interface:  (##6)
 *  Interface to traverse all nodes defining some symbol value
 *  in specified DefVector.
**/
public interface DefVectorIterator extends PointVectorIterator {
    /** nextDef:
     *  Get the next IR node that defines some symbol
     *  in the given DefVector (DefVector used to instanciate this iterator).
     *  Nodes that have 0 in the DefVector are skipped.
     *  If the last node is not a defining node, then null is
     *  returned at the last time. Therefore, having <code>hasNext</code> returned <code>true</code> does not guarantee this method returns meaningful (non-null) value.
     *
    **/
    public IR nextDef();

    /**
     * Get the <code>SetRefRepr</code> that corresponds to the next bit that is on in this <code>BitVector</code>. This method returns the <code>SetRefRepr</code> object that wraps the node that will be returned by <code>nextDef</code>. If there is no SetRefReprs remaining, then returns null. Therefore, having <code>hasNext</code> returned <code>true</code> does not guarantee this method returns meaningful (non-null) value.
     *
     */
    public SetRefRepr nextSetRefRepr();
}
 // DefVectorIterator interface
