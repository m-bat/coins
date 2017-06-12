/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.flow;

import java.util.ArrayList; //##63
import java.util.Iterator;
import java.util.List; //##63
import java.util.Map;  //##63
import java.util.HashMap; //##63
import java.util.Set; //##63
import java.lang.StringBuffer; //##63

import coins.FlowRoot;
import coins.ir.IR;
import coins.ir.hir.HIR;
import coins.sym.FlowAnalSym;

/** UseDefListImpl class
 *  Use-def list representaing list of use nodes of a symbol
 *  where each use node has a list of its def points.
 *  UseDefList will be accessed from Var, Reg, ExpId, etc.
 **/
public class
  UseDefListImpl
  implements UseDefList
{

//====== Fields ======

  public final FlowRoot
    flowRoot; // Used to access Root information.

  protected Map
    fUseDefChainMap; // Map of UseDefChains using use-node as a key.

  protected List
    fUseDefChainList = null;  // List of UseDefChains.

//====== Constructor ======

  public
    UseDefListImpl(FlowRoot pFlowRoot)
  {
    flowRoot = pFlowRoot;
    fUseDefChainMap = new HashMap();
  }

/* //##63
  public
    UseDefListImpl(FlowRoot pFlowRoot, FlowAnalSym pSym)
  {
    flowRoot = pFlowRoot;
    fUseDefChainList = flowRoot.hirRoot.hir.irList();
    ((SubpFlowImpl)flowRoot.fSubpFlow).setUseDefList(pSym, this);
  }
 */ //##63

//====== Methods ======

  public UseDefChain
    addUseDefChain(IR pUseNode)
  {
    if (! fUseDefChainMap.containsKey(pUseNode)) {
      UseDefChain lUseDefChain = (UseDefChain)
        (new UseDefChainImpl(flowRoot, pUseNode));
      fUseDefChainMap.put(pUseNode, lUseDefChain);
      return lUseDefChain;
    }else
      return (UseDefChain)fUseDefChainMap.get(pUseNode);
  } // addUseDefChain

  /** getUseDefChain
   *  Get UseDefChain having pUseDefNode as its use node.
   **/
  public UseDefChain
    getUseDefChain(IR pUseNode)
  {
    if (fUseDefChainMap.containsKey(pUseNode)) {
      return (UseDefChain)fUseDefChainMap.get(pUseNode);
    }else
      return addUseDefChain(pUseNode);
  } // getUseDefChain IR

  public UseDefChain
    getOrAddUseDefChain( IR pUseNode )
{
  UseDefChain lUseDefChain = getUseDefChain(pUseNode);
  if (lUseDefChain == null) {
    lUseDefChain = addUseDefChain(pUseNode);
  }
  return lUseDefChain;
} // getOrAddUseDefChain


public List
  getUseDefChainList()
{
  if (fUseDefChainList == null) {
  fUseDefChainList = new ArrayList();
  Set lUseNodeSet = fUseDefChainMap.keySet();
  for (Iterator lIt = lUseNodeSet.iterator();
       lIt.hasNext(); ) {
    IR lUseNode = (IR)lIt.next();
    fUseDefChainList.add(lUseNode);
  }
}
return fUseDefChainList;

}
public void
  print()
{
  flowRoot.ioRoot.printOut.print("\nUseDefList ");
  flowRoot.ioRoot.printOut.print(toStringByName());
}

public String
 toString()
{
  return toStringByName();
}

  public String
    toStringByName()
  {
    StringBuffer lBuffer = new StringBuffer();
    lBuffer.append("\nUseDefList ");
    UseDefChain lUseDefChain = null, lChain;
    Set lUseNodeSet = fUseDefChainMap.keySet();
    List lUseNodeList = FlowUtil.sortSetOfNodesByIndex(lUseNodeSet,
      flowRoot.fSubpFlow.getIrIndexMax()); //##65
    HIR[] lNullDefList = new HIR[lUseNodeSet.size() + 1]; //##65
    int lNullDefIndex = 0; //##65
    List lDefList; //##63
    IR lDefNode, lUseNode;
    //##65 for (Iterator lIterator = lUseNodeSet.iterator();
    for (Iterator lIterator = lUseNodeList.iterator(); //##65
         lIterator.hasNext(); ) {
      lUseNode = (IR)(lIterator.next());
      if (lUseNode != null) {
        lUseDefChain = (UseDefChain)fUseDefChainMap.get(lUseNode);
        if (lUseDefChain == null)
          continue;
        //##65 BEGIN
        if ((lUseDefChain.getDefList() == null)||
            (lUseDefChain.getDefList().isEmpty())||
            ((lUseDefChain.getDefList().size() == 1)&&
             ((lUseDefChain.getDefList().get(0) == null)||
              (((HIR)lUseDefChain.getDefList().get(0)).getIndex() == 0)))) {
          lNullDefList[lNullDefIndex] = (HIR)lUseDefChain.getUseNode();
          lNullDefIndex++;
        }else {
        //##65 END
          lBuffer.append("\n " + lUseDefChain.toStringByName());
        }
       }
    }
    //##65 BEGIN
    if (lNullDefIndex > 0) {
      lBuffer.append("\n use nodes with no def node:");
      for (int lIndex = 0; lIndex < lNullDefIndex; lIndex++) {
        lBuffer.append(" " + lNullDefList[lIndex].toStringShort());
      }
    }
    //##65 END
    return lBuffer.toString();
  } // toStringByName

} // UseDefListImpl class
