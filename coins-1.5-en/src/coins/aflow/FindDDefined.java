/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * FindDDefined.java
 *
 * Created on March 13, 2003, 3:14 PM
 */
package coins.aflow;

import coins.sym.FlowAnalSym;


/**
 *
 * @author  hasegawa
 */
public class FindDDefined extends FindDefined {
    /** Creates a new instance of FindDDefined */
    public FindDDefined(FlowResults pResults) {
        super(pResults);
    }

    protected void addDefined(SetRefRepr pSetRefRepr, FlowAnalSymVector pDefined) {
        FlowAnalSym lDef = (FlowAnalSym) pSetRefRepr.defSym();

        if (lDef != null) {
            int lExpBitPosition = pDefined.getSubpFlow().getSymIndexTable()
                                          .indexOf(lDef);
            pDefined.setBit(lExpBitPosition); // Set the Defined vector's bit.
        }
    }

    protected void register(BBlock pBBlock, FlowAnalSymVector pDefined) {
        pBBlock.setDDefined(pDefined);
    }

    public void find(SetRefRepr pSetRefRepr) {
        SubpFlow lSubpFlow = pSetRefRepr.getBBlock().getSubpFlow();
        FlowAnalSymVector lDefined = lSubpFlow.flowAnalSymVector();
        FlowAnalSym lDefSym = pSetRefRepr.defSym();

        if (lDefSym != null) {
            lDefined.setBit(lSubpFlow.getSymIndexTable().indexOf(lDefSym));
        }

        fResults.put("DDefined", pSetRefRepr, lDefined);
    }
}
