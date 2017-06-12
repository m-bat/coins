/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow;

import java.util.ArrayList;
import java.util.List;

import coins.sym.FlowAnalSym;


/**
 * Initiate data flow analysis. this class's initiate(SubpFlow) method should be called before doing other flow analyses.
 */
public abstract class InitiateFlow extends FlowAdapter {
    protected List fBBlockList = new ArrayList(); // List of BBlocks relevant for DFA //##11
    protected FlowAnalSym[] fFlowAnalSymTable; //##11

    InitiateFlow(FlowResults pResults) {
        super(pResults);
    }

    public void find(SubpFlow pSubpFlow) {
        initiate(pSubpFlow);
    }

    abstract void initiate(SubpFlow pSubpFlow);
}
