/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * FindDAvailInAvailOut.java
 *
 * Created on March 17, 2003, 1:51 PM
 */
package coins.aflow;


/**
 *
 * @author  hasegawa
 */
public class FindDAvailInAvailOut extends FindAvailInAvailOut {
    /** Creates a new instance of FindDAvailInAvailOut */
    public FindDAvailInAvailOut(FlowResults pResults) {
        super(pResults);
    }

    protected void registerAvailIn(BBlock pBBlock, ExpVector pAvailIn) {
        pBBlock.setDAvailIn(pAvailIn);
    }

    protected void registerAvailOut(BBlock pBBlock, ExpVector pAvailOut) {
        pBBlock.setDAvailOut(pAvailOut);
    }

    protected ExpVector getEKill(BBlock pBBlock) {
      flow.dbg(5, " getEKill " + pBBlock + " " + pBBlock.getPEKill()); //##53
        return pBBlock.getPEKill();
    }

    protected ExpVector getAvailIn(BBlock pBBlock) {
        return pBBlock.getDAvailIn();
    }

    protected ExpVector getEGen(BBlock pBBlock) {
      flow.dbg(5, " getEGen " + pBBlock + " " + pBBlock.getDEGen()); //##56
        return pBBlock.getDEGen();
    }

    protected ExpVector getAvailIn(SetRefRepr pSetRefRepr) {
        return pSetRefRepr.getDAvailIn();
    }

    protected void registerAvailIn(SetRefRepr pSetRefRepr, ExpVector pAvailIn) {
        fResults.put("DAvailIn", pSetRefRepr, pAvailIn);
    }

    protected ExpVector getEKill(SetRefRepr pSetRefRepr) {
        return pSetRefRepr.getPEKill();
    }
}
