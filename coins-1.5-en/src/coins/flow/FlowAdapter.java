/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * FlowAdatper.java
 *
 */
package coins.flow;

import coins.FlowRoot;
import coins.IoRoot;
import coins.aflow.FlowResults; //##70
import coins.ir.hir.HIR; //##63
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * FlowAdapter contains methods to expand the flow analysis capability.
 * A subclass of this may be added to afford new analysis requirements or
 * new methods commonly used in several cases may be added to this class
 * without disturbing other classes.
 */
public class
FlowAdapter
{

  public final FlowRoot flowRoot;
  protected IoRoot ioRoot;
  protected SubpFlow fSubpFlow;
  protected FlowResults fResults = null; //##70

  public final HIR dummyUninitialization;
  public final HIR dummySettingByParam;

  TreeStructure fDominatorTree = null;
  TreeStructure fPostDominatorTree = null;
  List fDominatorListsOfSubp[];
  List fPostDominatorListsOfSubp[];

  public FlowAdapter(FlowRoot pFlowRoot)
  {
    flowRoot = pFlowRoot;
    ioRoot = flowRoot.ioRoot;
    fSubpFlow = flowRoot.fSubpFlow;
    dummyUninitialization = flowRoot.hirRoot.hir.nullNode();
    dummySettingByParam = flowRoot.hirRoot.hir.nullNode();
  } // FlowAdapter

  public List
    getDominators(BBlock pBBlock)
  {
    if (prepareDominators()) {
      return fDominatorListsOfSubp[pBBlock.getBBlockNumber()];
    }else {
      ioRoot.msgRecovered.put(
        "getDominators return null.");
      return null;
    }
  } // getDominators

  public List
   getPostDominators(BBlock pBBlock)
 {
   if (preparePostDominators()) {
     return fPostDominatorListsOfSubp[pBBlock.getBBlockNumber()];
   }else {
     ioRoot.msgRecovered.put(
       "getPostDominators return null.");
     return null;
   }
 } // getPostDominators

 /**
   * Makes a (post)dominator tree for the given list of BBlocks.
   * pEntry will be the root of the tree. The list of BBlockspBBlocks
   * should be connected and every BBlock should be reachable from
   * (should reach) pBBlock.
   * This method is a modified version of makeDominatorTreeFor
   * in coins.aflow.MakeDominatorTree.
   * @param pIsDom if true, find dominator tree,
   * otherwise, find postdominator tree.
   * @return the resultant coins.flow.util.TreeStructure object.
   */
  public TreeStructure makeDominatorTreeFor(List pBBlocks, BBlock pEntry,
    boolean pIsDom)
  {
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
    List lDomOrPostDomLists[];

    if (pIsDom) {
      if (fDominatorListsOfSubp == null) //##63
          fDominatorListsOfSubp
            = new ArrayList[fSubpFlow.getNumberOfBBlocks() + 1]; //##63
      lDomOrPostDomLists = fDominatorListsOfSubp;
    }else {
      if (fPostDominatorListsOfSubp == null) //##63
        fPostDominatorListsOfSubp
          = new ArrayList[fSubpFlow.getNumberOfBBlocks() + 1]; //##63
      lDomOrPostDomLists = fPostDominatorListsOfSubp;
    }
    for (lIt = pBBlocks.iterator();
         lIt.hasNext(); ) {
      lBBlock = (BBlock)lIt.next();
      if (lBBlock == null) //##63
        continue; //##63
      lPredList = getIn(lBBlock, pIsDom);
      lBBlock.setWork(new ArrayList());
      ((List)lBBlock.getWork()).addAll(lPredList);
      ((List)lBBlock.getWork()).retainAll(pBBlocks);
      lDom = new ArrayList();
      if (lBBlock == pEntry) {
        lDom.add(lBBlock);
      }
      else {
        lDom.addAll(pBBlocks);
      }
      lDoms.put(lBBlock, lDom);
    }
    lDom = new ArrayList();
    do {
      lChanged = false;
      for (lIt = pBBlocks.iterator();
           lIt.hasNext(); ) {
        lBBlock = (BBlock)lIt.next();
        if (lBBlock == null) //##63
          continue; //##63
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
        }
      }
    }
    while (lChanged);

    for (lIt = pBBlocks.iterator();
         lIt.hasNext(); ) {
      lBBlock = (BBlock)lIt.next();
      if (lBBlock == null) //##63
        continue; //##63
      lDom = (List)lDoms.get(lBBlock);
      lSize = lDom.size();
      // save(lBBlock, lDom);
      if (pIsDom) {
        fDominatorListsOfSubp[lBBlock.getBBlockNumber()] = lDom;
      }else {
        fPostDominatorListsOfSubp[lBBlock.getBBlockNumber()] = lDom;

      }

      for (Iterator lIt0 = lDom.iterator(); lIt0.hasNext(); ) {
        lDomBBlock = (BBlock)lIt0.next();
        // link(lTree, lDomBBlock, lBBlock);
        if (((List)lDoms.get(lDomBBlock)).size() == (lSize - 1)) {
          if (pIsDom) {
            //  protected void linkDominator(TreeStructure pTree, BBlock pParent, BBlock pChild) {
            //      ((BBlock) pParent).getDominatedChildrenForSubpFlow().add(pChild);
            //     ((BBlock) pChild).setImmediateDominatorForSubpFlow((BBlock) pParent);
            // }
            lDomBBlock.getDominatedChildren().add(lBBlock);
            lBBlock.setImmediateDominator(lDomBBlock);
          }else {
            // protected void linkPostDominator(TreeStructure pTree, BBlock pParent, BBlock pChild) {
            //    ((BBlock) pParent).getPostdominatedChildrenForSubpFlow().add(pChild);
            //     ((BBlock) pChild).setImmediatePostdominatorForSubpFlow((BBlock) pParent);
            // }
            lDomBBlock.getPostDominatedChildren().add(lBBlock);
            lBBlock.setImmediatePostDominator(lDomBBlock);
          }

          break;
        }
      }
    }

    return lTree;
  }
/*
  protected void saveDominator(BBlock pBBlock, List pDom) {
      pBBlock.setDomForSubpFlow(pDom);
  }

  protected void savePostDominator(BBlock pBBlock, List pDom) {
      pBBlock.setPostdomForSubpFlow(pDom);
  }
*/
  private List getIn(BBlock pBBlock, boolean pIsDom)
  {
    return pIsDom ? pBBlock.getPredList() : pBBlock.getSuccList();
  }

  protected boolean
    prepareDominators()
  {
    if (fSubpFlow.isComputed(fSubpFlow.CF_CFG)) {
      if (fDominatorTree == null) {
        fDominatorTree = makeDominatorTreeFor(fSubpFlow.getListOfBBlocksFromEntry(),
          fSubpFlow.getEntryBBlock(), true);
      }
      if (fDominatorListsOfSubp == null) {
        fDominatorListsOfSubp = new ArrayList[fSubpFlow.getNumberOfBBlocks() +
          1];
        fDominatorListsOfSubp[0] = new ArrayList(); // Empty.
        for (int i = 1; i <= fSubpFlow.getNumberOfBBlocks(); i++) {
          BBlock lBBlock = fSubpFlow.getBBlock(i);
          fDominatorListsOfSubp[i] =
            fDominatorTree.ancestorsOf(lBBlock);
          fDominatorListsOfSubp[i].add(lBBlock);
        }
      }
      return true;
    }
    else {
      ioRoot.msgRecovered.put(
        "controlFlowAnalysis() should be called before dominance analysis.");
      return false;
    }
  } // prepareDominators

  protected boolean
    preparePostDominators()
  {
    if (fSubpFlow.isComputed(fSubpFlow.CF_CFG)) {
      if (fPostDominatorTree == null) {
        fPostDominatorTree = makeDominatorTreeFor(fSubpFlow.getListOfBBlocksFromEntry(),
          fSubpFlow.getEntryBBlock(), false);
      }
      if (fPostDominatorListsOfSubp == null) {
        fPostDominatorListsOfSubp = new ArrayList[fSubpFlow.getNumberOfBBlocks() +
          1];
        fPostDominatorListsOfSubp[0] = new ArrayList(); // Empty.
        for (int i = 1; i <= fSubpFlow.getNumberOfBBlocks(); i++) {
          BBlock lBBlock = fSubpFlow.getBBlock(i);
          fPostDominatorListsOfSubp[i] =
            fPostDominatorTree.ancestorsOf(lBBlock);
          fPostDominatorListsOfSubp[i].add(lBBlock);
        }
      }
      return true;
    }
    else {
      ioRoot.msgRecovered.put(
        "controlFlowAnalysis() should be called before dominance analysis.");
      return false;
    }
  } // preparePostDominators

//##70 BEGIN
public void
setFlowResults( FlowResults pResults )
{
  fResults = pResults;
}

public FlowResults
getFlowResults()
{
  return fResults;
}
//##70 END
} // FlowAdapter
