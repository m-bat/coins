/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow;

import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Set;

import coins.aflow.util.FAList;
import coins.sym.FlowAnalSym;


public abstract class FindDef extends FlowAdapter {
    public FindDef(FlowResults pResults) {
        super(pResults);
    }

    public void find() {
        find(fResults.flowRoot.subpFlow);
    }

    /**
     * Finds the Def vectors for all the <code>BBlock</code>s in <code>pSubpFlow</code>.
     */
    public void find(SubpFlow pSubpFlow) {
        BBlock lBBlock;

        for (Iterator lIt = pSubpFlow.getReachableBBlocks().iterator();
                lIt.hasNext();)
            find((BBlock) lIt.next());
    }

    /**
     * Finds the Def vector for <code>pBBlock</code>.
     */
    public void find(BBlock pBBlock) {
        ioRoot.dbgFlow.print(5, "findDef", "B" + pBBlock.getBBlockNumber()); //###

        SetRefRepr lSetRefRepr;

        //                SetRepr lSetRepr;
        FlowAnalSym lDef; //##11
        Set lBBlockDefSet = new HashSet(); // Set that incrementally stores the variables defined in the BBlock and is referred to to determine if the current SetRefRepr qualifies as an element of Def.
        int lDefBitPosition;
        FAList lSubsts = (FAList) fResults.get("SubpFlowSetReprs",
                pBBlock.getSubpFlow());
        DefVector lDefVect = pBBlock.getSubpFlow().defVector();
        SetRefReprList lBBlockSetRefReprs = (SetRefReprList) fResults.get("BBlockSetRefReprs",
                pBBlock);

        for (ListIterator lBBlockSetRefReprListIterator = lBBlockSetRefReprs.listIterator(
                    lBBlockSetRefReprs.size());
                lBBlockSetRefReprListIterator.hasPrevious();) {
            lSetRefRepr = (SetRefRepr) lBBlockSetRefReprListIterator.previous();
            ioRoot.dbgFlow.print(5, "lSetRefRepr " + lSetRefRepr,
              "lDefVect " + lDefVect + " lSubsts " + lSubsts); //###

            if (addDefs(lBBlockDefSet, lSetRefRepr)) {
                lDefVect.setBit(lSubsts.indexOf(lSetRefRepr));
            }

            //			Set lLHSSyms = lSetRefRepr.getLHSSyms();
            //			if (!lLHSSyms.removeAll(lBBlockDefSet))
            //				lDefVect.setBit(lSubsts.indexOf(lSetRefRepr));
            //			lBBlockDefSet.addAll(lSetRefRepr.getModSyms());
        }

        register(pBBlock, lDefVect);
    }

    protected abstract boolean addDefs(Set pDefSet, SetRefRepr pSetRefRepr);

    abstract protected void register(BBlock pBBlock, DefVector pVect);

}
