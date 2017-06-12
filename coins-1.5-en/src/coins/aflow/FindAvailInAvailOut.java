/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * FindAvailInAvailOut.java
 *
 * Created on June 17, 2002, 5:54 PM
 */
package coins.aflow;

import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
 *
 * @author  hasegawa
 */
public abstract class FindAvailInAvailOut extends FlowAdapter {
    /** Creates new FindAvailInAvailOut */
    public FindAvailInAvailOut(FlowResults pResults) {
        super(pResults);
    }

    /**
     * Finds AvailIn and AvailOut vectors for all the <code>BBlock</code>s in <code>pSubpFlow</code>.
     */
    public void find(SubpFlow pSubpFlow) {
        BBlock lBBlock;
        boolean lChanged;
        List lPredList;
        BBlock lPredBBlock;
        ExpVector lAvailIn;
        ExpVector lPredEGen;
        ExpVector lPredEKill;
        ExpVector lPredAvailIn;
        ExpVector lSurvived = pSubpFlow.expVector();
        ExpVector lPredAvailOut = pSubpFlow.expVector();
        ExpVector lEGen;
        ExpVector lEKill;
        ExpVector lAvailOut;

        final ExpVector lZeroVect = pSubpFlow.expVector();
        ioRoot.dbgFlow.print(2, "FindAvailInAvailOut",
                             pSubpFlow.getSubpSym().getName()); //##56
        // Initialize.
        for (Iterator lCFGIterator = pSubpFlow.getBBlocks().iterator();
                lCFGIterator.hasNext();) {
            lBBlock = (BBlock) lCFGIterator.next();
            lAvailIn = pSubpFlow.expVector();

            if (!lBBlock.isEntryBBlock()) {
                lZeroVect.vectorNot(lAvailIn);
            }

            registerAvailIn(lBBlock, lAvailIn);
        }

        // Solve by iteration.
        do {
            lChanged = false;

            for (Iterator lCFGIterator = pSubpFlow.getBBlocksFromEntry()
                                                  .iterator();
                    lCFGIterator.hasNext();) {
                ExpVector lNewAvailIn = pSubpFlow.expVector();
                lBBlock = (BBlock) lCFGIterator.next();

                if (!lBBlock.isEntryBBlock()) {
                    lPredList = lBBlock.getPredList();
                    lZeroVect.vectorNot(lNewAvailIn);

                    for (Iterator lPredIterator = lPredList.iterator();
                            lPredIterator.hasNext();) {
                        lPredBBlock = (BBlock) lPredIterator.next();
                        lPredEGen = getEGen(lPredBBlock);
                        lPredEKill = getEKill(lPredBBlock);
                        lPredAvailIn = getAvailIn(lPredBBlock);
                        lPredAvailIn.vectorSub(lPredEKill, lSurvived);
                        lPredEGen.vectorOr(lSurvived, lPredAvailOut);
                        lNewAvailIn.vectorAnd(lPredAvailOut, lNewAvailIn);
                    }

                    if (!lNewAvailIn.vectorEqual(getAvailIn(lBBlock))) {
                        lChanged = true;
                        registerAvailIn(lBBlock, lNewAvailIn);
                    }
                }
            }
        } while (lChanged);

        // Compute the AvailOut vectors.
        for (Iterator lCFGIterator = pSubpFlow.getBBlocks().iterator();
                lCFGIterator.hasNext();) {
            lBBlock = (BBlock) lCFGIterator.next();
            ioRoot.dbgFlow.print(4, "Compute AvailOut of ", lBBlock.toString()); //##56Z
            lEGen = getEGen(lBBlock);
            lEKill = getEKill(lBBlock);
            lAvailIn = getAvailIn(lBBlock);
            ioRoot.dbgFlow.print(4, " EGen ", lEGen.toString()); //##56
            ioRoot.dbgFlow.print(4, " EKill ", lEKill.toString()); //##56
            ioRoot.dbgFlow.print(4, " AvailIn ", lAvailIn.toString()); //##56
            lAvailIn.vectorSub(lEKill, lSurvived);
            lAvailOut = pSubpFlow.expVector();
            lEGen.vectorOr(lSurvived, lAvailOut);
            registerAvailOut(lBBlock, lAvailOut);
            ioRoot.dbgFlow.print(4, " AvailOut ", lAvailOut.toString()); //##56
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

        //		ExpVector lPrevSurvived = lSubpFlow.expVector();
        ExpVector lPrevAvailIn;
        ExpVector lAvailIn = lSubpFlow.expVector();

        if (lSetRefReprs.isEmpty()) {
            getAvailIn(lBBlock).vectorCopy(lAvailIn);
        } else if (lBBlock.getSetRefReprs().get(0) == pSetRefRepr) {
            getAvailIn(lBBlock).vectorCopy(lAvailIn);
        } else {
            lPrevSetRefRepr = (SetRefRepr) lSetRefReprs.get(lSetRefReprs.indexOf(
                        pSetRefRepr) - 1);
            lPrevAvailIn = getAvailIn(lPrevSetRefRepr);
            lPrevAvailIn.vectorOr(ExpVectorImpl.forSet(
                    (Set) fResults.get("UseFlowExpIdsForSetRefRepr",
                        lPrevSetRefRepr), lSubpFlow), lAvailIn);
            lAvailIn.vectorSub(getEKill(lPrevSetRefRepr), lAvailIn);

            //				lPrevSetRefRepr.
            //				if (lPrevSetRefRepr.sets())
            //					lPrevSurvived.setBit(lSubpFlow.getSetReprs().indexOf(lPrevSetRefRepr));
            //				lAvailIn = lPrevAvailIn;
        }

        registerAvailIn(pSetRefRepr, lAvailIn);
    }

    protected abstract ExpVector getEKill(SetRefRepr pSetRefRepr);

    protected abstract ExpVector getAvailIn(SetRefRepr pSetRefRepr);

    protected abstract void registerAvailIn(SetRefRepr pSetRefRepr,
        ExpVector pAvailIn);

    protected abstract ExpVector getEGen(BBlock pBBlock);

    protected abstract ExpVector getEKill(BBlock pBBlock);

    protected abstract ExpVector getAvailIn(BBlock pBBlock);

    protected abstract void registerAvailIn(BBlock pBBlock, ExpVector pAvailIn);

    protected abstract void registerAvailOut(BBlock pBBlock, ExpVector pAvailOut);
}
