/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * FindDefInDefOut.java
 *
 * Created on June 21, 2002, 1:41 PM
 */
package coins.aflow;

import java.util.Iterator;
import java.util.List;


/**
 *
 * @author  hasegawa
 */
public abstract class FindDefInDefOut extends FlowAdapter {
    /** Creates new FindDefInDefOut */
    public FindDefInDefOut(FlowResults pResults) {
        super(pResults);
    }

    /**
     * Finds DefIn and DefOut vectors for all the <code>BBlock</code>s in <code>pSubpFlow</code>.
     */
    public void find(SubpFlow pSubpFlow) {
        BBlock lBBlock;
        boolean lChanged;
        List lPredList;
        BBlock lPredBBlock;
        FlowAnalSymVector lDefIn;
        FlowAnalSymVector lPredDefined;
        FlowAnalSymVector lPredDefIn;
        FlowAnalSymVector lPredDefOut = pSubpFlow.flowAnalSymVector();
        FlowAnalSymVector lDefined;
        FlowAnalSymVector lDefOut;

        final FlowAnalSymVector lZeroVect = pSubpFlow.flowAnalSymVector();

        // Initialize.
        for (Iterator lCFGIterator = pSubpFlow.getBBlocks().iterator();
                lCFGIterator.hasNext();) {
            lBBlock = (BBlock) lCFGIterator.next();
            lDefIn = pSubpFlow.flowAnalSymVector();

            if (!lBBlock.isEntryBBlock()) {
                lZeroVect.vectorNot(lDefIn);
            }

            registerDefIn(lBBlock, lDefIn);

            //			fResults.put("DefIn", lBBlock, lDefIn);
        }

        // Solve by iteration.
        do {
            lChanged = false;

            for (Iterator lCFGIterator = pSubpFlow.getBBlocksFromEntry()
                                                  .iterator();
                    lCFGIterator.hasNext();) {
                FlowAnalSymVector lNewDefIn = pSubpFlow.flowAnalSymVector();
                lBBlock = (BBlock) lCFGIterator.next();

                if (!lBBlock.isEntryBBlock()) {
                    lPredList = lBBlock.getPredList();
                    lZeroVect.vectorNot(lNewDefIn);

                    for (Iterator lPredIterator = lPredList.iterator();
                            lPredIterator.hasNext();) {
                        lPredBBlock = (BBlock) lPredIterator.next();
                        lPredDefined = getDefined(lPredBBlock); // (FlowAnalSymVector)fResults.get("Defined", lPredBBlock);
                        lPredDefIn = getDefIn(lPredBBlock); // (FlowAnalSymVector)fResults.get("DefIn", lPredBBlock);
                        lPredDefined.vectorOr(lPredDefIn, lPredDefOut);
                        lNewDefIn.vectorAnd(lPredDefOut, lNewDefIn);
                    }

                    if (!lNewDefIn.vectorEqual(getDefIn(lBBlock)))
                    //				if (!lNewDefIn.vectorEqual((FlowAnalSymVector)fResults.get("DefIn", lBBlock)))
                     {
                        lChanged = true;
                        registerDefIn(lBBlock, lNewDefIn);

                        //					fResults.put("DefIn", lBBlock, lNewDefIn);
                    }
                }
            }
        } while (lChanged);

        for (Iterator lCFGIterator = pSubpFlow.getBBlocks().iterator();
                lCFGIterator.hasNext();) {
            lBBlock = (BBlock) lCFGIterator.next();
            lDefined = getDefined(lBBlock); // (FlowAnalSymVector)fResults.get("Defined", lBBlock);
            lDefIn = getDefIn(lBBlock); // (FlowAnalSymVector)fResults.get("DefIn", lBBlock);
            lDefOut = pSubpFlow.flowAnalSymVector();
            lDefined.vectorOr(lDefIn, lDefOut);
            registerDefOut(lBBlock, lDefOut);

            //			fResults.put("DefOut", lBBlock, lDefOut);
        }
    }

    public void find(BBlock pBBlock) {
        find(pBBlock.getSubpFlow());
    }

    public void find(SetRefRepr pSetRefRepr) {
        BBlock lBBlock = pSetRefRepr.getBBlock();
        SetRefReprList lSetRefReprs = lBBlock.getSetRefReprs();
        SetRefRepr lPrevSetRefRepr;
        SubpFlow lSubpFlow = lBBlock.getSubpFlow();

        //		DefVector lPrevSurvived = lSubpFlow.defVector();
        //		FlowAnalSymVector lDefIn;
        FlowAnalSymVector lDefIn = lSubpFlow.flowAnalSymVector();

        if (lSetRefReprs.isEmpty()) {
            getDefIn(lBBlock).vectorCopy(lDefIn);
        } else if (lBBlock.getSetRefReprs().get(0) == pSetRefRepr) {
            getDefIn(lBBlock).vectorCopy(lDefIn);
        } else {
            lPrevSetRefRepr = (SetRefRepr) lSetRefReprs.get(lSetRefReprs.indexOf(
                        pSetRefRepr) - 1);
            lDefIn = getDefIn(lPrevSetRefRepr);

            //				lDefIn.vectorSub(getKill(lPrevSetRefRepr), lPrevSurvived);
            //				if (lPrevSetRefRepr.sets())
            lDefIn.vectorOr(getDefined(lPrevSetRefRepr), lDefIn);

            //					lPrevSurvived.setBit(lSubpFlow.getSetReprs().indexOf(lPrevSetRefRepr));
            //				l
        }

        registerDefIn(pSetRefRepr, lDefIn);
    }

    protected abstract void registerDefIn(SetRefRepr pSetRefRepr,
        FlowAnalSymVector pDefIn);

    protected abstract void registerDefIn(BBlock pBBlock,
        FlowAnalSymVector pDefIn);

    protected abstract void registerDefOut(BBlock pBBlock,
        FlowAnalSymVector pDefOut);

    //	protected abstract FlowAnalSymVector getDefined(SetRefRepr pSetRefRepr);
    protected abstract FlowAnalSymVector getDefined(BBlock pBBlock);

    protected abstract FlowAnalSymVector getDefIn(BBlock pBBlock);

    protected abstract FlowAnalSymVector getDefined(SetRefRepr pSetRefRepr);

    protected abstract FlowAnalSymVector getDefIn(SetRefRepr pSetRefRepr);
}
