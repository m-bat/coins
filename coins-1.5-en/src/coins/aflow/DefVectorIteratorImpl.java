/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow;

import coins.ir.IR;


/** DefVectorIteratorImpl class:  (##6)
 *
 **/
public class DefVectorIteratorImpl extends PointVectorIteratorImpl
    implements DefVectorIterator {
    private final FlowResults fResults;

    public DefVectorIteratorImpl(DefVector pDefVector) {
        super(pDefVector);
        fResults = ((DefVectorImpl) pDefVector).fResults;
    }

    public IR nextDef() {
        SetRefRepr lNextSetRefRepr = nextSetRefRepr();

        if (lNextSetRefRepr != null) {
            return lNextSetRefRepr.getIR();
        }

        return null;
    }
     // nextDefNode

    public SetRefRepr nextSetRefRepr() {
        int lNextIndex = nextIndex();

        if (lNextIndex == 0) {
            return null;
        } else {
            return (SetRefRepr) ((DefVector) fVect).getSubpFlow().getSetRefReprs()
                                 .get(lNextIndex);
        }
    }
}
 // DefVectorIteratorImpl class
