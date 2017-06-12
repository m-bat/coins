/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow;

import coins.aflow.util.BitVector;


/** PointVector interface
 *
 **/
public interface PointVector extends BitVector {
    /**
     * Returns a SubpFlow object this PointVector is associated with.
     */
    SubpFlow getSubpFlow();
}
 // PointVector interface
