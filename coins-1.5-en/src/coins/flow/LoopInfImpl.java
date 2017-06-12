/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.flow;

import java.util.LinkedList;
import java.util.List;

import coins.FlowRoot;
import coins.ir.IR;
import coins.ir.hir.LoopStmt;
import coins.sym.FlagBox;
import coins.sym.FlagBoxImpl;

/** LoopInfImpl
 *  Loop information class.
 *  LoopInf IS NOT USED ANY MORE. lparallel used LoopInfo of lparallel. //##78
 **/
public class
  LoopInfImpl
  implements LoopInf
{

//==== Fields ====

  protected final FlowRoot
    flowRoot;

  protected LoopInf
    fParent,
    fFirstChild,
    fNextBrother;

  protected LinkedList
    fBBlockList;

  protected IR
    fEntryNode;

  protected LinkedList
    fAlternateEntryNodeList;

  protected BBlock
    fEntryBBlock;

  protected LinkedList
    fAlternateEntryBBlockList;

  protected FlagBox
    fFlagBox;

//==== Constructor ====

  public
    LoopInfImpl(FlowRoot pFlowRoot, IR pEntryNode)
  {
    flowRoot = pFlowRoot;
    fParent = null;
    fFirstChild = null;
    fNextBrother = null;
    fBBlockList = new LinkedList();
    fEntryNode = pEntryNode;
    if (flowRoot.isHirAnalysis()) {
      if ((pEntryNode != null) && (pEntryNode instanceof LoopStmt)) {
//##60   ((LoopStmt)pEntryNode).setLoopInf(this);
      }
    }
    else if (flowRoot.isLirAnalysis()) {
      //## REFINE
    }

    fAlternateEntryNodeList = null;
    fAlternateEntryBBlockList = null;
    fFlagBox = (FlagBox)(new FlagBoxImpl());

  } // LoopInfImpl

//==== Public methods ====

  public LoopInf
    getParent()
  {
    return fParent;
  }

  public void
    setParent(LoopInf pParent)
  {
    fParent = pParent;
  }

  public LoopInf
    getFirstChild()
  {
    return fFirstChild;
  }

  public void
    setFirstChild(LoopInf pChild)
  {
    fFirstChild = pChild;
  }

  public LoopInf
    getNextBrother()
  {
    return fNextBrother;
  }

  public void
    setNextBrother(LoopInf pBrother)
  {
    fNextBrother = pBrother;
  }

  public BBlock
    getEntryBBlock()
  {
    return fEntryBBlock;
  }

  public void
    setEntryBBlock(BBlock pEntryBBlock)
  {
    fEntryBBlock = pEntryBBlock;
    if (fEntryNode == null)
      fEntryNode = pEntryBBlock.getIrLink();
    addBBlock(pEntryBBlock);
  } // setEntryBBlock

  public List
    getBBlockList()
  {
    return fBBlockList;
  }

  public void
    addBBlock(BBlock pBBlock)
  {
    /* //##78
    if (!fBBlockList.contains(pBBlock))
      fBBlockList.add(pBBlock);
    if (pBBlock.getLoopInf() == null)
      pBBlock.setLoopInf(this); // This should be done before
      // adding to parent so that inner-most LoopInf
      // is to be linked to pBBlock.
    if (fParent != null)
      fParent.addBBlock(pBBlock);
    */ //##78
  } // addBBlock

  public void
    deleteBBlock(BBlock pBBlock)
  {
    /* //##78
    fBBlockList.remove(pBBlock);
    pBBlock.setLoopInf(null);
    if (fParent != null)
      deleteBBlock(pBBlock);
   */ //##78
  } // deleteBBlock

  public List
    getAlternateEntryNodeList()
  {
    return fAlternateEntryNodeList;
  }

  public List
    getAlternateEntryBBlockList()
  {
    return fAlternateEntryBBlockList;
  }

  public void
    addAlternateEntryNode(IR pEntryNode)
  {
    if (fAlternateEntryNodeList == null)
      fAlternateEntryNodeList = new LinkedList();
    if (!fAlternateEntryNodeList.contains(pEntryNode))
      fAlternateEntryNodeList.add(pEntryNode);
  } // addAlternateEntryNode

  public void
    addAlternateEntryBBlock(BBlock pEntryBBlock)
  {
    if (fAlternateEntryBBlockList == null)
      fAlternateEntryBBlockList = new LinkedList();
    if (!fAlternateEntryBBlockList.contains(pEntryBBlock))
      fAlternateEntryBBlockList.add(pEntryBBlock);
  } // addAlternateEntryBBlock

  public boolean
    getFlag(int pFlagNumber)
  {
    return fFlagBox.getFlag(pFlagNumber);
  }

  public void
    setFlag(int pFlagNumber, boolean pYesNo)
  {
    fFlagBox.setFlag(pFlagNumber, pYesNo);
  }

  public void
    propagateFlag(int pFlagNumber)
  {
    fFlagBox.setFlag(pFlagNumber, true);
    if (fParent != null)
      fParent.propagateFlag(pFlagNumber);
  }

//==== Debug print ====

  public void
    print(int pDebugLevel)
  {
    BBlock lBBlock;
    if (pDebugLevel <= flowRoot.ioRoot.dbgFlow.getLevel()) {
      flowRoot.ioRoot.dbgFlow.print(pDebugLevel, "LoopInf",
        "Entry node " + fEntryNode.toString() +
        " Entry BBlock B" + fEntryBBlock.getBlockNumber()
        + "\n  BBlockList");
      for (java.util.ListIterator lIterator = fBBlockList.listIterator();
           lIterator.hasNext(); ) {
        lBBlock = (BBlock)(lIterator.next());
        if (lBBlock != null)
          flowRoot.ioRoot.dbgFlow.print(pDebugLevel, " B" +
            lBBlock.getBlockNumber());
      }
      flowRoot.ioRoot.dbgFlow.print(pDebugLevel,
        "\n  Flags HasCall " +
        fFlagBox.getFlag(LoopInf.HAS_CALL)
        + " ptrAssign " +
        fFlagBox.getFlag(LoopInf.HAS_PTR_ASSIGN)
        + " usePtr " +
        fFlagBox.getFlag(LoopInf.USE_PTR)
        + " hasStructUnion " +
        fFlagBox.getFlag(LoopInf.HAS_STRUCT_UNION));
      if (fFirstChild != null)
        fFirstChild.print(pDebugLevel);
      if (fNextBrother != null)
        fNextBrother.print(pDebugLevel);
    }
  } // print

} // LoopInfImpl class
