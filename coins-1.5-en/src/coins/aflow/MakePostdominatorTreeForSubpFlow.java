/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow;

import java.util.List;

import coins.aflow.util.TreeStructure;


public class MakePostdominatorTreeForSubpFlow extends MakeDominatorTree {
    private SubpFlow fSubpFlow;

    public MakePostdominatorTreeForSubpFlow(SubpFlow pSubpFlow) {
        fSubpFlow = pSubpFlow;
    }

    /**
     * Makes a postdominator tree object for the SubpFlow object this object is associated with.
     */
    public void makePostdominatorTree() {
        makeDominatorTreeFor(
            fSubpFlow, //##70
            fSubpFlow.getReachableBBlocks(),
            fSubpFlow.getExitBBlock(), false);
    }

    protected void saveDom(BBlock pBBlock, List pDom) {
        pBBlock.setPostdomForSubpFlow(pDom);
    }

    protected void link(TreeStructure pTree, BBlock pParent, BBlock pChild) {
        ((BBlock) pParent).getPostdominatedChildrenForSubpFlow().add(pChild);
        ((BBlock) pChild).setImmediatePostdominatorForSubpFlow((BBlock) pParent);
    }
}
