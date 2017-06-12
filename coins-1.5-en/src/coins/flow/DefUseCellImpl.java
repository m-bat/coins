/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.flow;

import coins.FlowRoot;
import coins.ir.IR;
import coins.ir.IrList;

/** DefUseCellImpl class:
 *
 *  Def-use list cell representaing
 *  a definition and list of its use points.
 **/
public class
  DefUseCellImpl
  implements DefUseCell
{

//====== Fields ======

  public final FlowRoot
    flowRoot; // Used to access Root information.

  protected IR
    fDefNode = null; // IR node defining a symbol.

  protected IrList
    fUseList = null; // List of IR nodes using the symbol.

//====== Constructors ======

  public
    DefUseCellImpl(FlowRoot pFlowRoot, IR pDefNode)
  {
    flowRoot = pFlowRoot;
    fDefNode = pDefNode;
    fUseList = flowRoot.hirRoot.hir.irList();
  }

//====== Methods ======

  public IR
    getDefNode()
  {
    return fDefNode;
  }

  public coins.ir.IrList
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

} // DefUseCellImpl class
