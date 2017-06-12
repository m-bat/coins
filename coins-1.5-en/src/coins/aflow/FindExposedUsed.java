/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * FindExposed.java
 *
 * Created on June 10, 2002, 2:10 PM
 */
package coins.aflow;

import java.util.Iterator;
import java.util.ListIterator;


/**
 *
 * @author  hasegawa
 */
public abstract class FindExposedUsed extends FlowAdapter {
    public FindExposedUsed(FlowResults pResults) {
        super(pResults);
    }

    public void find(SubpFlow pSubpFlow) {
        BBlock lBBlock;

        for (Iterator lIt = pSubpFlow.getBBlocks().iterator(); lIt.hasNext();) {
            lBBlock = (BBlock) lIt.next();
            find(lBBlock);
        }
    }

    public void find(BBlock pBBlock) {
        SetRefReprList lBBlockSetRefReprList = (SetRefReprList) fResults.get("BBlockSetRefReprs",
                pBBlock);
        SetRefRepr lSetRefRepr;

        //		FlowAnalSym lDefSym; //##11
        //		int lExpBitPosition;
        //		java.util.Set lExposedFlowAnalSyms = new HashSet();
        //		Set lUsedFlowAnalSyms = new HashSet();
        //		Set lSetRefReprUseFlowAnalSyms;
        SubpFlow lSubpFlow = pBBlock.getSubpFlow();
        FlowAnalSymVector lExposed = flow.flowAnalSymVector(lSubpFlow);
        FlowAnalSymVector lUsed = flow.flowAnalSymVector(lSubpFlow);
        if (lBBlockSetRefReprList != null) //##53
        for (ListIterator lBBlockSetRefReprIterator = lBBlockSetRefReprList.listIterator(
                    lBBlockSetRefReprList.size());
                lBBlockSetRefReprIterator.hasPrevious();) { // Iterate from the bottom of the BBlock for SetRefRepr
            lSetRefRepr = (SetRefRepr) lBBlockSetRefReprIterator.previous();
            lExposed.vectorSub(getDefined(lSetRefRepr), lExposed);
            lExposed.vectorOr(getExposed(lSetRefRepr), lExposed);
            lUsed.vectorOr(getUsed(lSetRefRepr), lUsed);

            //			addExposedUsed(lSetRefRepr, lExposedFlowAnalSyms, lUsedFlowAnalSyms);
        }

        //		lExposed = FlowAnalSymVectorImpl.forSet(lExposedFlowAnalSyms, pBBlock.getSubpFlow());
        registerExposed(pBBlock, lExposed);

        //		lUsed = FlowAnalSymVectorImpl.forSet(lUsedFlowAnalSyms, pBBlock.getSubpFlow());
        registerUsed(pBBlock, lUsed);
    }

    public abstract void find(SetRefRepr pSetRefRepr);

    protected abstract FlowAnalSymVector getDefined(SetRefRepr pSetRefRepr);

    protected abstract FlowAnalSymVector getExposed(SetRefRepr pSetRefRepr);

    protected abstract FlowAnalSymVector getUsed(SetRefRepr pSetRefRepr);

    //	protected abstract void addExposedUsed(SetRefRepr pSetRefRepr, Set pExposedFlowAnalSyms, Set pUsedFlowAnalSyms);
    protected abstract void registerExposed(BBlock pBBlock,
        FlowAnalSymVector pExposed);

    protected abstract void registerUsed(BBlock pBBlock, FlowAnalSymVector pUsed);
}
