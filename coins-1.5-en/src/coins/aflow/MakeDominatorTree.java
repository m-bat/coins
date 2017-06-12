/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow;

import coins.IoRoot; //##70
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashSet; //##72

import coins.aflow.util.TreeStructure;

/**
 * Makes a dominator tree for the given list of BBlocks.
 */
public class MakeDominatorTree
{
  protected SubpFlow fSubpFlow; //##70
  protected IoRoot ioRoot; //##70
  /**
   * Makes a (post)dominator tree for the given list of BBlocks. <code>pEntry</code> will be the root of the tree. The list of BBlocks <code>pBBlocks</code> should be connected and every BBlock should be reachable from (should reach) <code>pBBlock</code>.
   *
   * @param pIsDom if true, find dominator tree, otherwise, find postdominator tree.
   * @return the resultant coins.aflow.util.TreeStructure object.
   */
  public TreeStructure makeDominatorTreeFor(
    SubpFlow pSubpFlow, //##70 060823
    List pBBlocks, BBlock pEntry,
    boolean pIsDom)
  {
    fSubpFlow = pSubpFlow; //##70
    ioRoot = ((SubpFlowImpl)pSubpFlow).ioRoot; //##70
    if (ioRoot.dbgFlow.getLevel() > 0) {
      ioRoot.dbgFlow.print(3, "\nmakeDominatorTreeFor " + pBBlocks +
        " entry/exit " + pEntry + " isDom " + pIsDom); //##70
    }
    TreeStructure lTree = new TreeStructure(pBBlocks);
    Iterator lIt;
    BBlock lBBlock;
    BBlock lPredBBlock;
    BBlock lDomBBlock;
    List lPredList;
    Map lDoms = new HashMap();
    List lDom;
    int lSize;
    boolean lChanged;

    for (lIt = pBBlocks.iterator(); lIt.hasNext(); ) {
      lBBlock = (BBlock)lIt.next();
      lPredList = getIn(lBBlock, pIsDom);
      lBBlock.setWork(new ArrayList());
      ((List)lBBlock.getWork()).addAll(lPredList);
      ((List)lBBlock.getWork()).retainAll(pBBlocks); // Remove unreachable BBlocks
      lDom = new ArrayList();

      if (lBBlock == pEntry) {
        lDom.add(lBBlock);
      }
      else {
        //##70 060823 lDom.addAll(pBBlocks); //## ?
        lDom.addAll(pBBlocks); //##72
        /* //##72
        //##70 BEGIN 060823
        List lDomList;
        if (pIsDom) {
          lDomList = lBBlock.getDomForSubpFlow(); //##70 060823
        }
        else {
          lDomList = lBBlock.getPostdomForSubpFlow();
        }
        if (lDomList != null) {
          lDom.addAll(lDomList);
        }
        else {
          if (ioRoot.dbgFlow.getLevel() > 0) {
            ioRoot.dbgFlow.print(3, " DomList is null for " + lBBlock);
            //##70 END 060823
          }
        }
        */ //##72
      }

      lDoms.put(lBBlock, lDom);
    }

    lDom = new ArrayList();

    do {
      lChanged = false;
      if (ioRoot.dbgFlow.getLevel() > 0) {
        ioRoot.dbgFlow.print(5, "\nStart new iteration"); //##72
      }
      for (lIt = pBBlocks.iterator(); lIt.hasNext(); ) {
        lBBlock = (BBlock)lIt.next();

        if (lBBlock != pEntry) {
          lDom = new ArrayList();
          lDom.addAll(pBBlocks);
          for (Iterator lPredIt = ((List)lBBlock.getWork()).iterator();
               lPredIt.hasNext(); ) {
            lPredBBlock = (BBlock)lPredIt.next();
            lDom.retainAll((List)lDoms.get(lPredBBlock));
          }

          if (!lDom.contains(lBBlock)) {
            lDom.add(lBBlock);
          }

          if (!lDom.equals(lDoms.get(lBBlock))) {
            lChanged = true;
            lDoms.put(lBBlock, lDom);
          }
          if (ioRoot.dbgFlow.getLevel() > 0)
            ioRoot.dbgFlow.print(5, "\n " + lBBlock + ":" + lDom); //##72
        }
      }
    }
    while (lChanged);

    for (lIt = pBBlocks.iterator(); lIt.hasNext(); ) {
      lBBlock = (BBlock)lIt.next();
      lDom = (List)lDoms.get(lBBlock);
      lSize = lDom.size();
      saveDom(lBBlock, lDom);
      if (ioRoot.dbgFlow.getLevel() > 0)
        ioRoot.dbgFlow.print(5, "\nlDom of " + lBBlock + ":" + lDom); //##72

      for (Iterator lIt0 = lDom.iterator(); lIt0.hasNext(); ) {
        lDomBBlock = (BBlock)lIt0.next();

        if (((List)lDoms.get(lDomBBlock)).size() == (lSize - 1)) {
          link(lTree, lDomBBlock, lBBlock);
          //##72 BEGIN
          if (ioRoot.dbgFlow.getLevel() > 0) {
            if (pIsDom) {
              ioRoot.dbgFlow.print(3, " immediate-dom of " + lBBlock,
                "is " + lDomBBlock);
            }
            else {
              ioRoot.dbgFlow.print(3, " immediate-post-dom of " + lBBlock,
                "is " + lDomBBlock); //##70
            }
          }
          //##72 END
          break;
        }
      }
    }

    return lTree;
  }

  protected void saveDom(BBlock pBBlock, List pDom)
  {
  }

  /**
   * Links the given two nodes in the given tree structure.
   */
  protected void link(TreeStructure pTree, BBlock pParent, BBlock pChild)
  {
    pTree.link(pParent, pChild);
  }

  private List getIn(BBlock pBBlock, boolean pIsDom)
  {
    return pIsDom ? pBBlock.getPredList() : pBBlock.getSuccList();
  }
}
