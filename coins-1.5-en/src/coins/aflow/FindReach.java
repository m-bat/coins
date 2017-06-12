/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow;

import java.util.Iterator;
import java.util.List;


//
//  FindReach.java
//  CoinsCC020402
//
//  Created by Satoshi Hasegawa on Sun Apr 14 2002.
//
public abstract class FindReach extends FlowAdapter {
    public FindReach(FlowResults pResults) {
        super(pResults);
    }

    /**
     * Finds Reach vectors for all the <code>BBlock</code>s in <code>pSubpFlow</code>.
     */
    public void find(SubpFlow pSubpFlow) {
        BBlock lBBlock;
        boolean lChanged;
        List lPredList;
        BBlock lPredBBlock;
        DefVector lReach;
        DefVector lPredDef;
        DefVector lPredKill;
        DefVector lPredReach;
        DefVector lSurvived = pSubpFlow.defVector();
        DefVector lPredReachOut = pSubpFlow.defVector();

        // Initialize.
        for (Iterator lCFGIterator = pSubpFlow.getBBlocks().iterator();
                lCFGIterator.hasNext();) {
            lBBlock = (BBlock) lCFGIterator.next();
            lReach = pSubpFlow.defVector();
            register(lBBlock, lReach);
        }

        // Solve by iteration.
        do {
            lChanged = false;

            for (Iterator lCFGIterator = pSubpFlow.getBBlocksFromEntry()
                                                  .iterator();
                    lCFGIterator.hasNext();) {
                DefVector lNewReach = pSubpFlow.defVector();
                lBBlock = (BBlock) lCFGIterator.next();
                lPredList = lBBlock.getPredList();

                for (Iterator lPredIterator = lPredList.iterator();
                        lPredIterator.hasNext();) { //Find the union of all incoming flows(lPredReachs).
                    lPredBBlock = (BBlock) lPredIterator.next();
                    lPredDef = getDef(lPredBBlock);
                    lPredKill = getKill(lPredBBlock);
                    lPredReach = getReach(lPredBBlock);
                    ;
                    lPredReach.vectorSub(lPredKill, lSurvived);
                    lPredDef.vectorOr(lSurvived, lPredReachOut);
                    lNewReach.vectorOr(lPredReachOut, lNewReach);
                }

                if (!lNewReach.vectorEqual(getReach(lBBlock))) { //If Reach has changed, continue the loop.
                    lChanged = true;
                    register(lBBlock, lNewReach);
                }
            }
        } while (lChanged);
    }

    public void find(BBlock pBBlock) {
        find(pBBlock.getSubpFlow());
    }

    public void find(SetRefRepr pSetRefRepr) {
        BBlock lBBlock = pSetRefRepr.getBBlock();
        SetRefReprList lSetRefReprs = lBBlock.getSetRefReprs();
        SetRefRepr lPrevSetRefRepr;
        SubpFlow lSubpFlow = lBBlock.getSubpFlow();
        DefVector lPrevSurvived = lSubpFlow.defVector();
        DefVector lPrevReach;
        DefVector lReach = lSubpFlow.defVector();

        if (lSetRefReprs.isEmpty()) {
            getReach(lBBlock).vectorCopy(lReach);
        } else if (lBBlock.getSetRefReprs().get(0) == pSetRefRepr) {
            getReach(lBBlock).vectorCopy(lReach);
        } else {
            lPrevSetRefRepr = (SetRefRepr) lSetRefReprs.get(lSetRefReprs.indexOf(
                        pSetRefRepr) - 1);
            lPrevReach = getReach(lPrevSetRefRepr);

            lPrevReach.vectorSub(getKill(lPrevSetRefRepr), lPrevSurvived);

            if (defs(lPrevSetRefRepr)) {
                lPrevSurvived.setBit(lSubpFlow.getSetRefReprs().indexOf(lPrevSetRefRepr));
            }

            lReach = lPrevSurvived;
        }

        register(pSetRefRepr, lReach);
    }

    protected abstract boolean defs(SetRefRepr pSetRefRepr);

    protected abstract DefVector getDef(BBlock pBBlock);

    protected abstract DefVector getKill(BBlock pBBlock);

    protected abstract DefVector getReach(BBlock pBBlock);

    protected abstract void register(BBlock pBBlock, DefVector pVect);

    protected abstract DefVector getKill(SetRefRepr pSetRefRepr);

    protected abstract DefVector getReach(SetRefRepr pSetRefRepr);

    protected abstract void register(SetRefRepr pSetRefRepr,
        DefVector pDefVector);
}
