/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * FindDDefInDefOut.java
 *
 * Created on March 13, 2003, 6:00 PM
 */
package coins.aflow;


/**
 *
 * @author  hasegawa
 */
public class FindDDefInDefOut extends FindDefInDefOut {
    /** Creates a new instance of FindDDefInDefOut */
    public FindDDefInDefOut(FlowResults pResults) {
        super(pResults);
    }

    protected void registerDefIn(BBlock pBBlock, FlowAnalSymVector pDefIn) {
        pBBlock.setDDefIn(pDefIn);
    }

    protected void registerDefOut(BBlock pBBlock, FlowAnalSymVector pDefOut) {
        pBBlock.setDDefOut(pDefOut);
    }

    protected FlowAnalSymVector getDefined(BBlock pBBlock) {
        return pBBlock.getDDefined();
    }

    protected FlowAnalSymVector getDefIn(BBlock pBBlock) {
        return pBBlock.getDDefIn();
    }

    protected FlowAnalSymVector getDefIn(SetRefRepr pSetRefRepr) {
        return pSetRefRepr.getDDefIn();
    }

    protected FlowAnalSymVector getDefined(SetRefRepr pSetRefRepr) {
        return pSetRefRepr.getDDefined();
    }

    protected void registerDefIn(SetRefRepr pSetRefRepr,
        FlowAnalSymVector pDefIn) {
        fResults.put("DDefIn", pSetRefRepr, pDefIn);
    }
}
