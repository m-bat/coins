/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * FindLiveInLiveOut.java
 *
 * Created on June 21, 2002, 10:29 AM
 */
package coins.aflow;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


/**
 *
 * @author  hasegawa
 */
public abstract class FindLiveInLiveOut extends FlowAdapter {
    /** Creates new FindLiveInLiveOut */
    public FindLiveInLiveOut(FlowResults pResults) {
        super(pResults);
    }

    /**
     * Finds LiveIn and LiveOut vectors for all the <code>BBlock</code>s in <code>pSubpFlow</code>.
     */
    public void find(SubpFlow pSubpFlow) {
        BBlock lBBlock;
        boolean lChanged;
        List lSuccList;
        BBlock lSuccBBlock;
        FlowAnalSymVector lLiveOut;
        List lBBlocks = pSubpFlow.getBBlocksFromEntry();
        FlowAnalSymVector lSuccExposed;
        FlowAnalSymVector lSuccDefined;
        FlowAnalSymVector lSuccLiveOut;
        FlowAnalSymVector lSurvived = pSubpFlow.flowAnalSymVector();
        FlowAnalSymVector lSuccLiveIn = pSubpFlow.flowAnalSymVector();
        FlowAnalSymVector lExposed;
        FlowAnalSymVector lDefined;
        FlowAnalSymVector lLiveIn;

        // Initialize.
        for (Iterator lCFGIterator = pSubpFlow.getBBlocks().iterator();
                lCFGIterator.hasNext();) {
            lBBlock = (BBlock) lCFGIterator.next();

            if (lBBlock.isExitBBlock()) {
                lLiveOut = FlowUtil.staticSymVector(pSubpFlow);
            } else {
                lLiveOut = pSubpFlow.flowAnalSymVector();
            }

            registerLiveOut(lBBlock, lLiveOut);

            //			fResults.put("LiveOut", lBBlock, lLiveOut);
        }

        // Solve by iteration.
        do {
            lChanged = false;

            for (ListIterator lCFGIterator = lBBlocks.listIterator(
                        lBBlocks.size()); lCFGIterator.hasPrevious();) {
                lBBlock = (BBlock) lCFGIterator.previous();

                if (!lBBlock.isExitBBlock()) {
                    FlowAnalSymVector lNewLiveOut = pSubpFlow.flowAnalSymVector();
                    lSuccList = lBBlock.getSuccList();

                    for (Iterator lSuccIterator = lSuccList.iterator();
                            lSuccIterator.hasNext();) {
                        lSuccBBlock = (BBlock) lSuccIterator.next();
                       lSuccExposed = getExposed(lSuccBBlock);
                        lSuccDefined = getDefined(lSuccBBlock); //(FlowAnalSymVector)fResults.get("Defined", lSuccBBlock);
                        lSuccLiveOut = getLiveOut(lSuccBBlock); // (FlowAnalSymVector)fResults.get("LiveOut", lSuccBBlock);;
                        lSuccLiveOut.vectorSub(lSuccDefined, lSurvived);
                        lSuccExposed.vectorOr(lSurvived, lSuccLiveIn);
                        lNewLiveOut.vectorOr(lSuccLiveIn, lNewLiveOut);
                    }

                    if (!lNewLiveOut.vectorEqual(getLiveOut(lBBlock))) {
                        lChanged = true;
                        registerLiveOut(lBBlock, lNewLiveOut);

                        //						fResults.put("LiveOut", lBBlock, lNewLiveOut);
                    }
                }
            }
        } while (lChanged);

        for (Iterator lCFGIterator = lBBlocks.iterator();
                lCFGIterator.hasNext();) {
            lBBlock = (BBlock) lCFGIterator.next();
            lExposed = getExposed(lBBlock); // (FlowAnalSymVector)fResults.get("Exposed", lBBlock);
            lDefined = getDefined(lBBlock); // (FlowAnalSymVector)fResults.get("Defined", lBBlock);
            lLiveOut = getLiveOut(lBBlock); //(FlowAnalSymVector)fResults.get("LiveOut", lBBlock);
            lLiveOut.vectorSub(lDefined, lSurvived);
            lLiveIn = pSubpFlow.flowAnalSymVector();
            lExposed.vectorOr(lSurvived, lLiveIn);
            registerLiveIn(lBBlock, lLiveIn);

            //			fResults.put("LiveIn", lBBlock, lLiveIn);
        }
    }

    public void find(BBlock pBBlock) {
        find(pBBlock.getSubpFlow());
    }

    protected abstract FlowAnalSymVector getExposed(BBlock pBBlock);

    protected abstract FlowAnalSymVector getDefined(BBlock pBBlock);

    protected abstract FlowAnalSymVector getLiveOut(BBlock pBBlock);

    protected abstract void registerLiveIn(BBlock pBBlock,
        FlowAnalSymVector pLiveIn);

    protected abstract void registerLiveOut(BBlock pBBlock,
        FlowAnalSymVector pLiveOut);

    public void find(SetRefRepr pSetRefRepr) {
        BBlock lBBlock = pSetRefRepr.getBBlock();
        SetRefReprList lSetRefReprs = lBBlock.getSetRefReprs();
        SetRefRepr lNextSetRefRepr;
        SubpFlow lSubpFlow = lBBlock.getSubpFlow();

        //		FlowAnalSymVector lNextSurvived = flow.flowAnalSymVector(lSubpFlow);
        FlowAnalSymVector lNextLiveOut;
        FlowAnalSymVector lLiveOut = flow.flowAnalSymVector(lSubpFlow);

        if (lSetRefReprs.isEmpty()) {
            getLiveOut(lBBlock).vectorCopy(lLiveOut);
        } else if (lSetRefReprs.get(lSetRefReprs.size() - 1) == pSetRefRepr) {
            getLiveOut(lBBlock).vectorCopy(lLiveOut);
        } else {
            lNextSetRefRepr = (SetRefRepr) lSetRefReprs.get(lSetRefReprs.indexOf(
                        pSetRefRepr) + 1);
            lNextLiveOut = getLiveOut(lNextSetRefRepr);
            lNextLiveOut.vectorSub(getDefined(lNextSetRefRepr), lLiveOut);
            lNextLiveOut.vectorOr(getExposed(lNextSetRefRepr), lLiveOut);

            //				if (defs(lNextSetRefRepr))
            //					lNextSurvived.setBit(lSubpFlow.getSetReprs().indexOf(lNextSetRefRepr));
            //				lLiveOut = lNextSurvived;
        }

        register(pSetRefRepr, lLiveOut);
    }

    protected abstract FlowAnalSymVector getExposed(SetRefRepr pSetRefRepr);

    protected abstract FlowAnalSymVector getDefined(SetRefRepr pSetRefRepr);

    protected abstract FlowAnalSymVector getLiveOut(SetRefRepr pSetRefRepr);

    protected abstract void register(SetRefRepr pSetRefRepr,
        FlowAnalSymVector pLiveOut);
}
