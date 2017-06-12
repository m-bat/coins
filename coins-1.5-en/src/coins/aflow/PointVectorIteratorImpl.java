/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow;

import coins.aflow.util.BitVector;
import coins.aflow.util.BitVectorIteratorImpl;
import coins.aflow.util.FlowError;
import coins.ir.IR;


/** PointVectorIteratorImpl class:  (##6)
 *
**/
public class PointVectorIteratorImpl extends BitVectorIteratorImpl
    implements PointVectorIterator {
    protected BitVector fVect;

    public PointVectorIteratorImpl(PointVector pPointVector) //##8
     {
        super((BitVector) pPointVector);
        fVect = pPointVector;
    }

    public IR nextPoint() {
        throw new FlowError();
    }
     // nextPoint
}
 // PointVectorIteratorImpl class
