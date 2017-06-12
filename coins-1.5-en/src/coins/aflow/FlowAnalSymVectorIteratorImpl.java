/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * FlowAnalSymVectorIteratorImpl.java
 *
 * Created on September 25, 2002, 9:37 AM
 */
package coins.aflow;

import coins.aflow.util.BitVectorIteratorImpl;
import coins.aflow.util.FAList;
import coins.sym.FlowAnalSym;


/**
 *
 * @author  hasegawa
 */
public class FlowAnalSymVectorIteratorImpl extends BitVectorIteratorImpl
    implements FlowAnalSymVectorIterator {
    FlowResults fResults;
    protected FlowAnalSymVector fVect;

    /** Creates a new instance of FlowAnalSymVectorIteratorImpl */
    public FlowAnalSymVectorIteratorImpl(FlowAnalSymVector pFlowAnalSymVector) {
        super(pFlowAnalSymVector);
        fResults = ((FlowAnalSymVectorImpl) pFlowAnalSymVector).fResults;
        fVect = pFlowAnalSymVector;
    }

    public FlowAnalSym nextFlowAnalSym() {
        int lNextExpIndex = 0;
        FlowAnalSym lIndexedSym; //##11
        lNextExpIndex = nextIndex();

        if (lNextExpIndex == 0) {
            return null;
        }

        lIndexedSym = (FlowAnalSym) ((FAList) fResults.get("SymIndexTable",
                fVect.getSubpFlow())).get(lNextExpIndex);

        return lIndexedSym;
    }
     // nextExpId
}
