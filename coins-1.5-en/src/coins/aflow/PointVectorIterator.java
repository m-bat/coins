/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow;

import coins.aflow.util.BitVectorIterator;
import coins.ir.IR;


/** PointVectorIterator interface:  (##6)
 *  Interface to traverse all nodes that have true bit
 *  in specified PointVector.
**/
public interface PointVectorIterator extends BitVectorIterator {
    /** nextPoint:
     *  Get the next IR node that defines some symbol
     *  in the given DefVector (DefVector used to instanciate this iterator).
     *  Nodes that have 0 in the DefVector are skipped.
     *  If the last node is not a defining node, then null is
     *  returned at the last time.
    **/
    public IR nextPoint();
}
 // PointVectorIterator interface
