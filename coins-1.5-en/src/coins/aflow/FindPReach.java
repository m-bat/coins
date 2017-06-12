/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow;



//
//  FindReach.java
//  CoinsCC020402
//
//  Created by Satoshi Hasegawa on Sun Apr 14 2002.
//
public class FindPReach extends FindReach {
    public FindPReach(FlowResults pResults) {
        super(pResults);
    }

    protected DefVector getDef(BBlock pBBlock) {
        return pBBlock.getPDef();
    }

    protected DefVector getKill(BBlock pBBlock) {
        return pBBlock.getDKill();
    }

    protected DefVector getReach(BBlock pBBlock) {
        return pBBlock.getPReach();
    }

    protected void register(BBlock pBBlock, DefVector pVect) {
        pBBlock.setPReach(pVect);
    }

    protected DefVector getKill(SetRefRepr pSetRefRepr) {
        return pSetRefRepr.getDKill();
    }

    protected DefVector getReach(SetRefRepr pSetRefRepr) {
        return pSetRefRepr.getPReach();
    }

    protected void register(SetRefRepr pSetRefRepr, DefVector pDefVector) {
        fResults.put("PReach", pSetRefRepr, pDefVector);
    }

    protected boolean defs(SetRefRepr pSetRefRepr) {
        return pSetRefRepr.sets() ||
               //##71 pSetRefRepr.hasCall();
               pSetRefRepr.hasCallWithSideEffect(); //##71
    }
}
