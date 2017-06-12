/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow;

import java.util.Iterator;
import java.util.List;

import coins.aflow.util.FAList;


/**
 * Stores <code>SetRefRepr</code> objects into <code>BBlock</code>s.
 */
abstract public class RecordSetRefReprs extends FlowAdapter {
    public RecordSetRefReprs(FlowResults pResults) {
        super(pResults);
    }

    /**
     * Stores <code>SetRefRepr</code> objects into <code>BBlock</code>s of the given <code>SubpFlow</code>, and also store value-setting <code>SetRefRepr</code> objects that belong to the given <code>SubpFlow</code> into a list the <code>SubpFlow</code> object holds.
     */
    public void find(SubpFlow pSubpFlow) {
        List lBBlocks = pSubpFlow.getReachableBBlocks();
        FAList lSubpFlowSetReprs = new FAList();

        for (Iterator lIt = lBBlocks.iterator(); lIt.hasNext();) {
            BBlock lBBlock = (BBlock) lIt.next();
            record(lBBlock, lSubpFlowSetReprs);
        }

        pSubpFlow.setSetRefReprs(lSubpFlowSetReprs);
    }

    /**
     * Stores <code>SetRefRepr</code> objects into the given <code>BBlock</code>, and also value-setting <code>SetRefRepr</code> objects into the parent <code>SubpFlow</code> object of the given <code>BBlock</code>.
     */
    public void find(BBlock pBBlock) {
        find(pBBlock.getSubpFlow());
    }

    /**
     * Stores <code>SetRefRepr</code> objects into the given <code>BBlock</code>, and also value-setting <code>SetRefRepr</code> objects the given <code>BBlock</code> holds into the given list <code>pSubpFlowSetReprs</code>.
     */
    abstract protected void record(BBlock pBBlock, FAList pSubpFlowSetReprs);
}
