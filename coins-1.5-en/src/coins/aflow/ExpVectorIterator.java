/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow;

import coins.aflow.util.BitVectorIterator;


/** ExpVectorIterator interface:  (##6)
 *  Interface to traverse all expressions
 *  that has true bit in specified ExpVector.
*/
public interface ExpVectorIterator extends BitVectorIterator {
    /** nextExpNode:
     *  Get the next IR node that has true bit in the given ExpVector
     *  (ExpVector used to instanciate this iterator).
     *  Nodes that have false bit 0 in the ExpVector are skipped.
     *  If the last node has false bit , then null is
     *  returned at the last time. Therefore, having <code>hasNext</code> returned <code>true</code> does not guarantee this method returns meaningful (non-null) value.
    */

    //public IR
    //nextExpNode();

    /** nextFlowAnalSym:
     *  Get the next FlowAnalSym symbol that has true bit in the given
     *  ExpVector. FlowAnalSym that have false bit in the ExpVector
     *  are skipped.
     *  If the last FlowAnalSym has false bit , then null is
     *  returned at the last time. Therefore, having <code>hasNext</code> returned <code>true</code> does not guarantee this method returns meaningful (non-null) value.
    */
    public FlowExpId nextFlowExpId(); //##11
}
 // ExpVectorIterator interface
