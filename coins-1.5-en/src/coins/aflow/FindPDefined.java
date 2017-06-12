/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow;



public class FindPDefined extends FindDefined {
    public FindPDefined(FlowResults pResults) {
        super(pResults);
    }

    protected void addDefined(SetRefRepr pSetRefRepr, FlowAnalSymVector pDefined) {
        pDefined.vectorOr(FlowAnalSymVectorImpl.forSet(
                pSetRefRepr.modSyms00(), pDefined.getSubpFlow()), pDefined);
    }

    protected void register(BBlock pBBlock, FlowAnalSymVector pDefined) {
        pBBlock.setPDefined(pDefined);
    }

    public void find(SetRefRepr pSetRefRepr) {
        fResults.put("PDefined", pSetRefRepr,
            FlowAnalSymVectorImpl.forSet(pSetRefRepr.modSyms00(),
                pSetRefRepr.getBBlock().getSubpFlow()));
    }
}
