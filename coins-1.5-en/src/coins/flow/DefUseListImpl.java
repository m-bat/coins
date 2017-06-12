/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.flow;

import java.util.Iterator;

import coins.FlowRoot;
import coins.ir.IR;
import coins.ir.IrList;
import coins.ir.hir.HIR; //##60
import coins.ir.hir.SymNode; //##73
import coins.sym.FlowAnalSym;
import java.util.ArrayList; //##63
import java.util.HashMap; //##63
import java.util.List; //##63
import java.util.Set; //##63

/** DefUseListImpl class
 *  Def-use list representaing list of definitions of a symbol
 *  where each definition has list of its use points.
 *  DefUseList will be accessed from Var, Reg, ExpId, etc.
 **/
public class
  DefUseListImpl
  implements DefUseList
{

//====== Fields ======

  public final FlowRoot
    flowRoot; // Used to access Root information.

    protected List
      fDefUseChainList = null; // List of DefUseChains defined.

  protected HashMap  // Map def-node to DefUseChain.
    fDefUseMap;       //##63

  protected final FlowAnalSym // Symbol defined by def-nodes.
    fDefUseSym; // This may be null if defined symbols is not restricted
                // to one symbol.

protected HashMap  // Map symbol to its DefUseChain. //##70
  fDefUseMapOfSym = null;

//====== Constructor ======

  public
    DefUseListImpl(FlowRoot pFlowRoot)
  {
    flowRoot = pFlowRoot;
//##63     fDefUseChainList = flowRoot.hirRoot.hir.irList();
    fDefUseMap = new HashMap(); //##63
    fDefUseSym = null; //##63
    if (flowRoot.ioRoot.dbgFlow.getLevel() > 3)
      flowRoot.ioRoot.dbgFlow.print(6, " DefUseListImpl "); //##65
  }

//====== Methods ======

  public DefUseChain
    addDefUseChain(IR pDefNode)
  {
    if (flowRoot.ioRoot.dbgFlow.getLevel() > 3)
      flowRoot.ioRoot.dbgFlow.print(6, " addDefUseChain "
        + pDefNode.toStringShort() + " "); //##65
    if (fDefUseMap.containsKey(pDefNode)) {
      return (DefUseChain)fDefUseMap.get(pDefNode);
    }
    DefUseChain lDefUseChain = (DefUseChain)
      (new DefUseChainImpl(flowRoot, pDefNode));
//##63     fDefUseChainList.add(lDefUseChain);
    fDefUseMap.put(pDefNode, lDefUseChain); //##63
    return lDefUseChain;
  } // addDefUseChain

  /** getDefUseChain
   *  Get DefUseChain having pDefNode as its definie node.
   **/
  public DefUseChain
    getDefUseChain(IR pDefNode)
  {
    DefUseChain lDefUseChain = (DefUseChain)fDefUseMap.get(pDefNode);
    if (lDefUseChain == null) {
      if (flowRoot.ioRoot.dbgFlow.getLevel() > 0 ) {
        flowRoot.ioRoot.dbgFlow.print(1, "\nDefUseChain corresponding to the Def node " +
          pDefNode + " not found.");
        print();
      }
      throw new FlowError("DefUseChain corresponding to the Def node " +
        pDefNode + " not found.");
    }
    return lDefUseChain;
  } // getDefUseChain IR


public List
  getDefUseChainList()
{
  if (fDefUseChainList == null) {
    fDefUseChainList = new ArrayList();
    Set lDefNodeSet = fDefUseMap.keySet();
    for (Iterator lIt = lDefNodeSet.iterator();
         lIt.hasNext(); ) {
      IR lDefNode = (IR)lIt.next();
      fDefUseChainList.add(lDefNode);
    }
  }
  return fDefUseChainList;
} // getDefUseChainList()

//##70 BEGIN
public List
  getDefUseChainListOfSym( FlowAnalSym pSym )
{
  List lDefUseChainListOfSym;
  if (fDefUseMapOfSym == null)
    fDefUseMapOfSym = new HashMap();
  //##92 BEGIN
  if (pSym == null) {
    if (flowRoot.ioRoot.dbgFlow.getLevel() >= 4)
      flowRoot.ioRoot.dbgFlow.print(4, "\n getDefUseChainListOfSym null");
    return new ArrayList();
  }
  //##92 END
  if (fDefUseMapOfSym.containsKey(pSym)) {
    lDefUseChainListOfSym = (List)fDefUseMapOfSym.get(pSym);
  }else {
    Set lDefNodeSet = fDefUseMap.keySet();
    lDefUseChainListOfSym = new ArrayList();
    for (Iterator lIt = lDefNodeSet.iterator();
         lIt.hasNext(); ) {
      HIR lDefNode = (HIR)lIt.next();
      //##73 if (lDefNode.getSym() == pSym) {
      //##73 BEGIN
      HIR lLHS = (HIR)lDefNode.getChild1();
      FlowAnalSym lSym = null;
      if (lLHS != null)
        lSym = lLHS.getSymOrExpId();
      if (lSym == pSym) {
      //##73 END
        //##73 DefUseChain lDefUseChain = getDefUseChain(lDefNode);
        DefUseChain lDefUseChain = getDefUseChain(lDefNode); //##73
        lDefUseChainListOfSym.add(lDefUseChain);
      }
    }
    fDefUseMapOfSym.put(pSym, lDefUseChainListOfSym);
  }
  if (flowRoot.ioRoot.dbgFlow.getLevel() >= 4) {
    flowRoot.ioRoot.dbgFlow.print(4, "\n getDefUseChainListOfSym "
      + pSym.getName()); //##73
    for (Iterator lIt = lDefUseChainListOfSym.iterator();
         lIt.hasNext(); ) {
      DefUseChain lDefUseChain2 = (DefUseChain)lIt.next();
      flowRoot.ioRoot.dbgFlow.print(4, "\n  "
       + lDefUseChain2.toStringByName());
    }
  }
  return lDefUseChainListOfSym;
} // getDefUseChainListOfSym
//##70 END

  DefUseChain getOrAddDefUseChain(IR pDefNode) {
      if (fDefUseMap.containsKey(pDefNode) == true)
          return (DefUseChain)fDefUseMap.get(pDefNode);
      else
          return addDefUseChain(pDefNode);

  }

  public void
    print()
  {
    flowRoot.ioRoot.printOut.print("\nDefUseList ");
    DefUseChain lDefUseChain = null, lChain;
    //##63 IrList lUseList;
    List lUseList;
    //##60 IR   lDefNode, lUseNode;
    HIR lDefNode, lUseNode;
    List lDefNodeList = FlowUtil.sortSetOfNodesByIndex(fDefUseMap.keySet(),
      flowRoot.fSubpFlow.getIrIndexMax()); //##65
    //##65 for (Iterator lIterator = fDefUseMap.keySet().iterator();
    for (Iterator lIterator = lDefNodeList.iterator(); //##65
         lIterator.hasNext(); ) {
      lDefNode = (HIR)lIterator.next(); //##63
      //##63 lChain = (DefUseChain)(lIterator.next());
      //##63 lDefNode = (HIR)lChain.getDefNode();
      if (lDefNode != null) {
        lDefUseChain = getDefUseChain(lDefNode); //##63
        if (lDefUseChain != null) {
          flowRoot.ioRoot.printOut.print("\n " + lDefUseChain.toStringByName());
        }
      }
    }
  } // print

} // DefUseListImpl class
