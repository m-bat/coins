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
import java.util.ArrayList; //##63
import java.util.List; //##63

/** UseDefChainImpl class
 *
 *  Use-def list cell representaing
 *  a use node and list of its definition points.
 **/
public class
  UseDefChainImpl
  implements UseDefChain
{

//====== Fields ======

  public final FlowRoot
    flowRoot; // Used to access Root information.

  protected IR
    fUseNode = null; // IR node using a symbol.

  //##63 protected IrList
  protected List //##63
    fDefList = null; // List of IR nodes defining the symbol.

//====== Constructors ======

  public
    UseDefChainImpl(FlowRoot pFlowRoot, IR pUseNode)
  {
    flowRoot = pFlowRoot;
    fUseNode = pUseNode;
    //##63 fDefList = flowRoot.hirRoot.hir.irList();
    fDefList = new ArrayList(); //##63
  }

//====== Methods ======

  public IR
    getUseNode()
  {
    return fUseNode;
  }

  //##63 public coins.ir.IrList
  public List
    getDefList()
  {
    return fDefList;
  }

  public void
    addDefNode(IR pDefNode)
  {
    if (!fDefList.contains(pDefNode))
      fDefList.add(pDefNode);
  }

//##63 BEGIN
  public String
    toString()
  {
    return "UseDefChain of " + fUseNode.toStringShort();
  }
  public String
    toStringByName()
  {
    StringBuffer lBuffer = new StringBuffer();
    if (fUseNode != null) {
      Sym lUseSym = fUseNode.getSym();
      if (lUseSym != null)
        lBuffer.append("use " + fUseNode.toStringShort() + " " +
          lUseSym.getName() + " def");
      else
        lBuffer.append("use " + fUseNode.toStringShort() + " def");
      if (fDefList != null) {
        for (Iterator lNodeIt = fDefList.iterator();
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

} // UseDefChainImpl class
