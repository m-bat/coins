/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * FindPLiveInLiveOut.java
 *
 * Created on March 13, 2003, 5:59 PM
 */
package coins.aflow;


/**
 *
 * @author  hasegawa
 */
public class FindPLiveInLiveOut extends FindLiveInLiveOut {
    /** Creates a new instance of FindPLiveInLiveOut */
    public FindPLiveInLiveOut(FlowResults pResults) {
        super(pResults);
    }

    protected FlowAnalSymVector getExposed(BBlock pBBlock) {
        return pBBlock.getPExposed();
    }

    protected FlowAnalSymVector getDefined(BBlock pBBlock) {
        return pBBlock.getDDefined();
    }

    protected FlowAnalSymVector getLiveOut(BBlock pBBlock) {
        return pBBlock.getPLiveOut();
    }

    protected void registerLiveIn(BBlock pBBlock, FlowAnalSymVector pLiveIn) {
        pBBlock.setPLiveIn(pLiveIn);
    }

    protected void registerLiveOut(BBlock pBBlock, FlowAnalSymVector pLiveOut) {
        pBBlock.setPLiveOut(pLiveOut);
    }

    protected FlowAnalSymVector getExposed(SetRefRepr pSetRefRepr) {
        return pSetRefRepr.getPExposed();
    }

    protected FlowAnalSymVector getDefined(SetRefRepr pSetRefRepr) {
        return pSetRefRepr.getDDefined();
    }

    protected FlowAnalSymVector getLiveOut(SetRefRepr pSetRefRepr) {
        return pSetRefRepr.getPLiveOut();
    }

    protected void register(SetRefRepr pSetRefRepr, FlowAnalSymVector pLiveOut) {
        fResults.put("PLiveOut", pSetRefRepr, pLiveOut);
    }
}
