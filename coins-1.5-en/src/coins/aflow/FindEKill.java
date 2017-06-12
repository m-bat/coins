/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * FindEKill.java
 *
 * Created on June 13, 2002, 2:15 PM
 */
package coins.aflow;

import coins.alias.RecordAlias; //##53
import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Set;

import coins.aflow.util.FAList;


/** FindEKill
 *
 * Extended by FindPEKill.
 * @author  hasegawa
 */
public abstract class FindEKill extends FlowAdapter {
    public FindEKill(FlowResults pResults) {
        super(pResults);
    }

    /**
     * Finds the EKill vectors for all the <code>BBlock</code>s in <code>pSubpFlow</code>.
     */
    public void find(SubpFlow pSubpFlow) {
        for (Iterator lIt = pSubpFlow.getBBlocks().iterator(); lIt.hasNext();)
            find((BBlock) lIt.next());
    }

    /**
     * Finds the EKill vector for <code>pBBlock</code>.
     */
    public void find(BBlock pBBlock) {
        FlowExpId lExpId;

        //	FlowAnalSymVector lUseVarVector;
        //	FlowAnalSymVector lDefined = (FlowAnalSymVector)fResults.get("Defined", pBBlock);
        //		System.out.println("lDefined = " + lDefined);
        Set lEKillSet = new HashSet();
        ExpVector lEKill;

        //	FlowAnalSymVector lInt = pBBlock.getSubpFlow().flowAnalSymVector();
        //	FlowAnalSymVector lZeroVector = pBBlock.getSubpFlow().flowAnalSymVector();
        FAList lFlowExpIdTable = (FAList) fResults.get("FlowExpIdTable",
                pBBlock.getSubpFlow());

        for (Iterator lIt = lFlowExpIdTable.iterator(); lIt.hasNext();) {
            lExpId = (FlowExpId) lIt.next();

            //			lEKillSet.addAll(lExpId.
            addEKill(lEKillSet, lExpId, pBBlock);
        }

        lEKill = ExpVectorImpl.forSet(lEKillSet, pBBlock.getSubpFlow());

        flowRoot.aflow.dbg(2, " FindEKill", pBBlock.toString() + " " +
                           lEKill); //##56
        //		System.out.println("lEKill = " + lEKill);
        //fResults.put("EKill", pBBlock, lEKill);
        register(pBBlock, lEKill);
    }

    public void find(SetRefRepr pSetRefRepr) {
        SetRefRepr lSetRefRepr;
        SubpFlow lSubpFlow = pSetRefRepr.getBBlock().getSubpFlow();
        FAList lFlowExpIdTable = (FAList) fResults.get("FlowExpIdTable",
                lSubpFlow);
        FlowExpId lFlowExpId;
        ExpVector lExpVect = lSubpFlow.expVector();

        for (ListIterator lIt = lFlowExpIdTable.listIterator(); lIt.hasNext();) {
            lFlowExpId = (FlowExpId) lIt.next();

            if (kills(pSetRefRepr, lFlowExpId)) {
                lExpVect.setBit(lIt.previousIndex());
            }
        }

        register(pSetRefRepr, lExpVect);

        //		fResults.put("Kill", pSetRefRepr, lExpVect);
    }

//##53 BEGIN
/**
 * Finds the EKill vector for pBBlock considering alias.
 */
protected abstract void
addEKill(Set pEKillSet, FlowExpId pFlowExpId,
         BBlock pBBlock, RecordAlias pRecordAlias);

protected abstract boolean
killsByAlias(SetRefRepr pSetRefRepr,
      FlowExpId pFlowExpId, RecordAlias pRecordAlias);

//##53 END

    protected abstract void addEKill(Set pEKillSet, FlowExpId pFlowExpId,
        BBlock pBBlock);

    protected abstract boolean kills(SetRefRepr pSetRefRepr,
        FlowExpId pFlowExpId);

    protected abstract void register(BBlock pBBlock, ExpVector pEKill);

    protected abstract void register(SetRefRepr pSetRefRepr, ExpVector pEKill);
}
