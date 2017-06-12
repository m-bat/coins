/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.flow;

import coins.FlowRoot;
import coins.ir.IR;
import coins.ir.IrList;
import coins.sym.Sym; //##63
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

/** DefUseChainImpl class
 *
 *  Def-use list chain representaing
 *  a definition and list of its use points.
 **/
public class
  DefUseChainImpl
  implements DefUseChain
{

//====== Fields ======

  public final FlowRoot
    flowRoot; // Used to access Root information.

  protected IR
    fDefNode = null; // IR node defining a symbol.

  //##63 protected IrList
  protected List //##63
    fUseList = null; // List of IR nodes using the symbol.

//====== Constructors ======

  public
    DefUseChainImpl(FlowRoot pFlowRoot, IR pDefNode)
  {
    flowRoot = pFlowRoot;
    fDefNode = pDefNode;
    //##63 fUseList = flowRoot.hirRoot.hir.irList();
    fUseList = new ArrayList();
    if (flowRoot.ioRoot.dbgFlow.getLevel() > 3)
      flowRoot.ioRoot.dbgFlow.print(6, "DefUseChainImpl",
        "def " + pDefNode.toStringShort());
  }

//====== Methods ======

  public IR
    getDefNode()
  {
    return fDefNode;
  }

  //##63 public coins.ir.IrList
  public List
    getUseList()
  {
    return fUseList;
  }

  public void
    addUseNode(IR pUseNode)
  {
    if (!fUseList.contains(pUseNode))
      fUseList.add(pUseNode);
  }

//##63 BEGIN

  public Sym getDefSym()
  {
    Sym lSym = null;
    if (fDefNode == null)
      return null;
    /* //##70
    lSym = fDefNode.getSym();
    if (lSym == null) {
      SetRefRepr lSetRefRepr = flowRoot.fSubpFlow.getSetRefReprOfIR(fDefNode);
      if (lSetRefRepr != null)
      lSym = lSetRefRepr.getDefSym();
    }
    */ //##70
    //##70 BEGIN
    SetRefRepr lSetRefRepr = flowRoot.fSubpFlow.getSetRefReprOfIR(fDefNode);
    if (lSetRefRepr != null)
      lSym = lSetRefRepr.getDefSym();
    if (lSym == null)
      lSym = fDefNode.getSym();
    //##70 END
    return lSym;
  } // getDefSym

  public String
    toStringByName()
  {
    StringBuffer lBuffer = new StringBuffer();
    if (fDefNode != null) {
      Sym lDefSym = getDefSym();
      if (lDefSym != null)
        lBuffer.append("def " + fDefNode.toStringShort() +
          " " + lDefSym.getName()+  " use");
      else
        lBuffer.append("def " + fDefNode.toStringShort() + " use");
      if (fUseList != null) {
        for (Iterator lNodeIt = fUseList.iterator();
             lNodeIt.hasNext(); ) {
          IR lNode = (IR)lNodeIt.next();
          if (lNode != null)
            lBuffer.append(" " + lNode.toStringShort());
        }
      }
    }
    return lBuffer.toString();
  } // toStringByName
//##63 END

} // DefUseChainImpl class
