/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.opt;

import java.util.Iterator;

import coins.FlowRoot;
//##63 import coins.aflow.BBlock;
import coins.flow.BBlock; //##63
import coins.aflow.FlowResults;
//##63 import coins.aflow.FlowUtil;
import coins.flow.FlowUtil; //##63
//##63 import coins.aflow.NodeIterator;
import coins.flow.NodeIterator; //##63
//##63 import coins.aflow.SetRefRepr;
import coins.flow.SetRefRepr; //##63
//##63 import coins.aflow.SetRefReprList;
import coins.flow.SetRefReprList; //##63
//##63 import coins.aflow.SubpFlow;
import coins.flow.SubpFlow; //##63
//##63 import coins.aflow.util.CoinsIterator;
import coins.ir.IR;
import coins.ir.hir.HIR;

/**
 *
 * Constant folding basic logic class.
 *
 */
public class ConstFolding
{

  final static int JAVA_BYTE_SIZE = 1, JAVA_CHAR_SIZE = 2,
    JAVA_SHORT_SIZE = 2, JAVA_INT_SIZE = 4, JAVA_LONG_SIZE = 8;

  //        public final SymRoot symRoot;
  public final FlowRoot flowRoot;
  FlowResults fResults;
  protected SubpFlow fSubpFlow; //##63

  /**
   *
   * ConstFolding
   *
   * Creates new ConstFolding
   *
   **/
  public ConstFolding(FlowResults pResults)
  {
    flowRoot = pResults.flowRoot;
    //            symRoot = flowRoot.symRoot;
    fResults = pResults;
    fSubpFlow = flowRoot.fSubpFlow; //##63
  }

  /**
   *
   * Performs constant folding for the subprogram that corresponds to the given SubpFlow.
   *
   * @return true if the subprogram changed (optimized).
   *
   */
  public boolean doSubp(SubpFlow pSubpFlow)
  {
    boolean lChanged = false;
    BBlock lBBlock;

    //##63 for (Iterator lIt = pSubpFlow.getReachableBBlocks().iterator();
    for (Iterator lIt = pSubpFlow.getListOfBBlocksFromEntry().iterator(); //##63
         lIt.hasNext(); ) {
      lBBlock = (BBlock)lIt.next();
      lChanged = doBBlock(lBBlock) || lChanged;
    }
    return lChanged;
  }

  /**
   *
   * Performs constant folding for the given BBlock
   *
   * @return true if the underlying IR changed (optimized).
   *
   */
  public boolean doBBlock(BBlock pBBlock)
  {
    if ((pBBlock == null) || (pBBlock.getBBlockNumber() == 0))
      return false; //##63
    //##63 SetRefReprList lSetRefReprs = (SetRefReprList)fResults.get(
    //##63   "BBlockSetRefReprs", pBBlock);
    SetRefReprList lSetRefReprs = fSubpFlow.getSetRefReprList(pBBlock); //##63
    SetRefRepr lSetRefRepr;
    IR lIR, lNewNode = null, lParent;
    boolean lFolded = false;
    final Object lDeletedFlag = new Object();

    //##63 for (CoinsIterator lSetRefReprIt = lSetRefReprs.coinsIterator();
    for (Iterator lSetRefReprIt = lSetRefReprs.iterator(); //##63
         lSetRefReprIt.hasNext(); ) {
      lSetRefRepr = (SetRefRepr)lSetRefReprIt.next();
      //##63 NodeIterator lNodeListIt = lSetRefRepr.nodeIterator();
      NodeIterator lNodeListIt = lSetRefRepr.nodeListIterator(); //##63

      for (; lNodeListIt.hasNext(); ) {
        lIR = (IR)lNodeListIt.next();
        if (!isDeleted(lIR, lDeletedFlag)) {
          if (FlowUtil.isConstNode(lIR)) {
            lParent = lIR.getParent();
            // There seems to be an occasion where the top node is a const node.
            while (lParent != null) {
              lNewNode = OptUtil.fold(lParent, flowRoot);
              if (lNewNode != lParent) {
                flagDeleted(lParent, lDeletedFlag);
                if (FlowUtil.isConstNode(lNewNode))
                  lParent = lNewNode.getParent();
                else
                  break;
              }
              else
                break;
            }
            if (lNewNode != lIR.getParent())
              lFolded = true;
          }
        }
      }

    }
    return lFolded;

  }

  /**
   *
   * flagDeleted
   *
   *
   **/
  private static void flagDeleted(IR pParent, Object pDeleteFlag)
  {
    ((HIR)pParent).setWork(pDeleteFlag); //##54

    if (pParent instanceof HIR) {
      for (int i = 1; i <= pParent.getChildCount(); i++) {
        if (pParent.getChild(i) != null) //##77
        ((HIR)pParent.getChild(i)).setWork(pDeleteFlag); //##54
      }
    }
    else {
      throw new OptError(); // 2004.06.01 S.Noishi
      /*
                   LIRNode lLIRNode =(LIRNode)pParent;
                   if (lLIRNode.hasLeftChild())
          lLIRNode.getLeftChild().setWork(pDeleteFlag);
                   if (lLIRNode.hasRightChild())
          lLIRNode.getRightChild().setWork(pDeleteFlag);
       */
    }
  }

  /**
   *
   * isDeleted
   *
   *
   **/
  private static boolean isDeleted(IR pIR, Object pDeleteFlag)
  {
    return ((HIR)pIR).getWork() == pDeleteFlag; //##54
  }
}
