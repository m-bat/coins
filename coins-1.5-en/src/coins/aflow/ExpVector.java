/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow;

import java.util.Set;

import coins.aflow.util.BitVector;


/** ExpVector interface
 * An <code>ExpVector</code> is a <code>BitVector</code> each of whose bit corresponds to a <code>FlowExpId</code>.
*/
public interface ExpVector extends BitVector {
    /**
     * SubpFlow instance associated with this ExpVector.
     */
    SubpFlow getSubpFlow();

    /**
     * Returns the <code>Set</code> of <code>FlowExpId</code>s whose corresponding bits are set in this <code>ExpVector</code>.
     */
    Set exps();

    /**
     * Returns <code>true</code> if this <code>ExpVector</code>'s bit corresponding to the given argument <code>pFlowExpId</code> is set. Returns false otherwise.
     */
    boolean contains(FlowExpId pFlowExpId);

    ExpVectorIterator expVectorIterator();

    String toStringShort(); //##25
}


// ExpVector interface
