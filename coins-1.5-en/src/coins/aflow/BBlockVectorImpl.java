/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow;

import coins.aflow.util.BitVectorImpl;

/** BBlockVectorImpl class:
 *
 **/
public class BBlockVectorImpl extends BitVectorImpl implements BBlockVector {
    private final SubpFlow fSubpFlow;
    protected final FlowResults fResults;

    protected BBlockVectorImpl(SubpFlow pSubpFlow) {
        this(pSubpFlow, false);
    }

    private BBlockVectorImpl(SubpFlow pSubpFlow, boolean pAll) {
        fSubpFlow = pSubpFlow;
        fResults = fSubpFlow.results();

        //		if (pAll)
        fBitLength = pSubpFlow.getBBlockTable().size();

        //		else
        //			fBitLength = pSubpFlow.getNumberOfRelevantBBlocks();
        fLongWordLength = (fBitLength / 64) + 1;
        fVectorWord = new long[fLongWordLength];
    }

    public SubpFlow getSubpFlow() {
        return fSubpFlow;
    }
}
 // BBlockVectorImpl class
