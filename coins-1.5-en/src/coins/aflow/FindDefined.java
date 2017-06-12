/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow;

import java.util.Iterator;

import coins.aflow.util.FAList;
import coins.sym.FlowAnalSym;


public abstract class FindDefined extends FlowAdapter {
    public FindDefined(FlowResults pResults) {
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
        SetRefReprList lBBlockSetRefReprs = (SetRefReprList) (fResults.get("BBlockSetRefReprs",
                pBBlock));
        SetRefRepr lSetRefRepr;
        FlowAnalSym lDef; //##11
        int lExpBitPosition;
        FlowAnalSymVector lDefined = pBBlock.getSubpFlow().flowAnalSymVector();
        FAList lSymIndexTable = (FAList) fResults.get("SymIndexTable",
                pBBlock.getSubpFlow());
        if (lBBlockSetRefReprs != null) { //##53
        Iterator lIt = lBBlockSetRefReprs.iterator();

        for (; lIt.hasNext();) {
            lSetRefRepr = (SetRefRepr) lIt.next();
            addDefined(lSetRefRepr, lDefined);
        }
        } //##53
        register(pBBlock, lDefined);
    }

    //	public abstract void findDefined(SetRefRepr pSetRefRepr);
    protected abstract void addDefined(SetRefRepr pSetRefRepr,
        FlowAnalSymVector pDefined);

    protected abstract void register(BBlock pBBlock, FlowAnalSymVector pDefined);
}
