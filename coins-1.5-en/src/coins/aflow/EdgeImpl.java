/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow;

import coins.FlowRoot;
import coins.sym.FlagBox;
import coins.sym.FlagBoxImpl;


/** EdgeImpl class
 *  Class for edge connecting basic blocks.
**/
public class EdgeImpl implements Edge //##8
 {
    //==== Fields ====//
    public final FlowRoot flowRoot; // Used to access Root information. //##8
    protected BBlock fFromBBlock; // Start point BBlock.
    protected BBlock fToBBlock; // End point BBlock.
    protected FlagBox fFlagBox;

    //==== Constructor ====//
    protected EdgeImpl(FlowRoot pFlowRoot, //##8
        BBlock pFromBBlock, BBlock pToBBlock) {
        flowRoot = pFlowRoot; //##8
        fFromBBlock = pFromBBlock;
        fToBBlock = pToBBlock;
        fFlagBox = (FlagBox) (new FlagBoxImpl());
    }

    //==== Access methods ====//
    public BBlock getFromBBlock() {
        return fFromBBlock;
    }

    public BBlock getToBBlock() {
        return fToBBlock;
    }

    public FlagBox flagBox() {
        return fFlagBox;
    }

    public void setFromBBlock(BBlock pBBlock) {
        fFromBBlock = pBBlock;
    }

    public void setToBBlock(BBlock pBBlock) {
        fToBBlock = pBBlock;
    }
}
 // EdgeImpl class
