/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * FindPExposedUsed.java
 *
 * Created on March 13, 2003, 4:43 PM
 */
package coins.aflow;

import java.util.HashSet;
import java.util.Set;

import coins.ir.IR;
import coins.sym.FlowAnalSym;


/**
 *
 * @author  hasegawa
 */
public class FindPExposedUsed extends FindExposedUsed {
    /** Creates a new instance of FindPExposedUsed */
    public FindPExposedUsed(FlowResults pResults) {
        super(pResults);
    }

    protected void registerExposed(BBlock pBBlock, FlowAnalSymVector pExposed) {
        pBBlock.setPExposed(pExposed);
    }

    //	protected void addExposedUsed(SetRefRepr pSetRefRepr, Set pExposedFlowAnalSyms, Set pUsedFlowAnalSyms)
    //	{
    //		Set lUsed = pSetRefRepr.getUseSyms();
    //		if (pSetRefRepr.hasCall())
    //			for (SymIterator lIt = symRoot.symTableRoot.getSymIterator(); lIt.hasNext();)
    //				lUsed.add(lIt.next());
    //		pUsedFlowAnalSyms.addAll(lUsed);
    //		pExposedFlowAnalSyms.remove(pSetRefRepr.getDefSym());
    //		pExposedFlowAnalSyms.addAll(lUsed);
    //	}
    //
    protected void registerUsed(BBlock pBBlock, FlowAnalSymVector pUsed) {
        pBBlock.setPUsed(pUsed);
    }

    public void find(SetRefRepr pSetRefRepr) {
        boolean lCalled = false;
        SubpFlow lSubpFlow = pSetRefRepr.getBBlock().getSubpFlow();

        //		FlowAnalSymVector lExposed = flow.flowAnalSymVector(lSubpFlow);
        //		FlowAnalSymVector lUsed = flow.flowAnalSymVector(lSubpFlow);
        Set lExposedFlowAnalSyms = new HashSet();
        Set lUsedFlowAnalSyms = new HashSet();
        IR lIR;
        FlowAnalSym lFlowAnalSym;

        for (NodeIterator lIt = pSetRefRepr.nodeIterator(); lIt.hasNext();) {
            lIR = (IR) lIt.next();

            if (lIR == pSetRefRepr.defNode()) {
                continue;
            }

            if (FlowUtil.readsFromIndefiniteAddress(lIR)) {
                lExposedFlowAnalSyms.addAll(lSubpFlow.getSymIndexTable());
                lUsedFlowAnalSyms.addAll(lSubpFlow.getSymIndexTable());

                break;
            }

            lFlowAnalSym = FlowUtil.flowAnalSym(lIR);

            if (lFlowAnalSym != null) {
                lExposedFlowAnalSyms.add(lFlowAnalSym);
                lUsedFlowAnalSyms.add(lFlowAnalSym);
            }
        }

        fResults.put("PExposed", pSetRefRepr,
            FlowAnalSymVectorImpl.forSet(lExposedFlowAnalSyms, lSubpFlow));
        fResults.put("PUsed", pSetRefRepr,
            FlowAnalSymVectorImpl.forSet(lUsedFlowAnalSyms, lSubpFlow));
    }

    protected FlowAnalSymVector getDefined(SetRefRepr pSetRefRepr) {
        return pSetRefRepr.getDDefined();
    }

    protected FlowAnalSymVector getExposed(SetRefRepr pSetRefRepr) {
        return pSetRefRepr.getPExposed();
    }

    protected FlowAnalSymVector getUsed(SetRefRepr pSetRefRepr) {
        return pSetRefRepr.getPUsed();
    }
}
