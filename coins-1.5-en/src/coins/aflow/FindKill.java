/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow;

import java.util.Iterator;
import java.util.ListIterator;

import coins.aflow.util.FAList;
import coins.alias.RecordAlias;  //##53
import coins.sym.FlowAnalSym;


public abstract class FindKill extends FlowAdapter {
    public FindKill(FlowResults pResults) {
        super(pResults);
    }

    public void find(SubpFlow pSubpFlow) {
        for (Iterator lIt = pSubpFlow.getBBlocks().iterator(); lIt.hasNext();)
            find((BBlock) lIt.next());
    }

    /**
     * Finds and sets the Kill vector for the given BBlock using the Defined vector for that BBlock.
     *
     * @param pBBlock BBlock whose Kill vector to find.
     */
    public void find(BBlock pBBlock) {
        SetRefRepr lSetRefRepr;
        SubpFlow lSubpFlow = pBBlock.getSubpFlow();
        DefVector lDefVect = lSubpFlow.defVector();
        SetRefReprList lSetRefReprs = pBBlock.getSetRefReprs();
        FAList lSetReprs = lSubpFlow.getSetRefReprs();
        SetRefRepr lFirst = null;
        SetRefRepr lLast = null;

        for (SetRefReprIterator lIt = pBBlock.getSetRefReprs()
                                             .setRefReprIterator();
                lIt.hasNext();) {
            lSetRefRepr = (SetRefRepr) lIt.next();

            if (lSetRefRepr.sets()) {
                if (lFirst == null) {
                    lFirst = lSetRefRepr;
                }

                lDefVect.vectorOr(getKill(lSetRefRepr), lDefVect);
                lLast = lSetRefRepr;
            }
        }

        if (lFirst != null) {
            for (int lIndex = lSetReprs.indexOf(lFirst);
                    lIndex <= lSetReprs.indexOf(lLast); lIndex++)
                lDefVect.resetBit(lIndex);
        }

        register(pBBlock, lDefVect);

        //		fResults.put("Kill", pBBlock, lDefVect);
    }

    public void find(SetRefRepr pSetRefRepr) {
        SetRefRepr lSetRefRepr;
        SubpFlow lSubpFlow = pSetRefRepr.getBBlock().getSubpFlow();
        FlowAnalSym lDefFlowAnalSym = pSetRefRepr.defSym();
        DefVector lDefVect = lSubpFlow.defVector();
        FAList lSetRefReprs = lSubpFlow.getSetRefReprs();

        for (ListIterator lIt = lSetRefReprs.listIterator(); lIt.hasNext();) {
            lSetRefRepr = (SetRefRepr) lIt.next();

            //			if (lSetRefRepr.hasCall())
            //				handleCall(lSetRefRepr);
            if (kills(pSetRefRepr, lSetRefRepr)) {
//                System.out.println(lIt.previousIndex());
                int i = lIt.previousIndex();
                lDefVect.setBit(i);
            }
        }

        lDefVect.resetBit(lSetRefReprs.indexOf(pSetRefRepr));
        flow.dbg(6, getClass() + ": Kill for " + pSetRefRepr + "\n",
            lDefVect.toStringDescriptive());
        register(pSetRefRepr, lDefVect);

        //		fResults.put("Kill", pSetRefRepr, lDefVect);
    }

//##53 BEGIN


protected abstract boolean
killsByAlias(SetRefRepr pKiller, SetRefRepr pKillee, RecordAlias pRecordAlias);

//##53 END

    //	protected abstract void handleCall(SetRefRepr pSetRefRepr);
    protected abstract DefVector getKill(SetRefRepr pSetRefRepr);
    protected abstract boolean kills(SetRefRepr pKiller, SetRefRepr pKillee);
    protected abstract void register(SetRefRepr pSetRefRepr, DefVector pDefVect);
    protected abstract void register(BBlock pBBlock, DefVector pDefVect);
}
