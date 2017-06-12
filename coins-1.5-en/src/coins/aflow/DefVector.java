/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow;

import java.util.Set;


/** DefVector interface
 * A bit in this <code>BitVector</code> corresponds to a <code>SetRefRepr</code>. The bit length corresponds to the number of <code>SetRefRepr</code>s in a <code>SubpFlow</code>.
 **/
public interface DefVector extends PointVector {
    /**
     * Returns the Set view of this DefVector (in terms of the set of SetRefReprs).
     */
    Set getSetRefReprs();

    /**
     * Returns true if this DefVector's bit corresponding to the given argument is set.
     */
    boolean contains(SetRefRepr pSetRefRepr);

    DefVectorIterator defVectorIterator();
}
 // DefVector interface
