/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow;

import java.util.List;

import coins.aflow.util.TreeStructure;


/**
 * Makes a dominator tree for a SubpFlow
 */
public class MakeDominatorTreeForSubpFlow extends MakeDominatorTree {
    private SubpFlow fSubpFlow;

    public MakeDominatorTreeForSubpFlow(SubpFlow pSubpFlow) {
        fSubpFlow = pSubpFlow;
    }

    /**
     * Makes a dominator tree for the SubpFlow object this object is associated with.
     */
    public void makeDominatorTree() {
        makeDominatorTreeFor(
          fSubpFlow, //##70
            fSubpFlow.getReachableBBlocks(),
            fSubpFlow.getEntryBBlock(), true);
    }

    protected void saveDom(BBlock pBBlock, List pDom) {
        pBBlock.setDomForSubpFlow(pDom);
    }

    protected void link(TreeStructure pTree, BBlock pParent, BBlock pChild) {
        ((BBlock) pParent).getDominatedChildrenForSubpFlow().add(pChild);
        ((BBlock) pChild).setImmediateDominatorForSubpFlow((BBlock) pParent);
    }


}
