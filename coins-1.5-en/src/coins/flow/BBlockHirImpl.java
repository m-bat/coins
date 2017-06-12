/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.flow;

import java.util.ArrayList; //##60
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import coins.CompileError;
import coins.FlowRoot;
import coins.ir.IR;
import coins.ir.hir.BlockStmt;
import coins.ir.hir.Exp;
import coins.ir.hir.HIR;
import coins.ir.hir.IfStmt;
import coins.ir.hir.JumpStmt;
import coins.ir.hir.LabeledStmt;
import coins.ir.hir.LoopStmt;
import coins.ir.hir.Stmt;
//##60 import coins.ir.lir.LIR;
//##60 import coins.ir.lir.LIRTree;
import coins.sym.Const;
import coins.sym.ExpId;
import coins.sym.Label;

/** BBlockHirImpl
 *  Implements BBlock methods applicalble for HIR.
 **/
public class
  BBlockHirImpl
  extends coins.flow.BBlockImpl
  implements coins.flow.BBlockHir
{

//======== Fields ========

  private HIR // First subtree of this BBlock.
    fFirstSubtree = null; // Null while it is not yet obtained.

  private HIR // Last subtree of this BBlock.
    fLastSubtree = null; // Null while it is not yet obtained.

//====== Constructors ========

  /** Create new basic block corresponding to the HIR labeled statement
   *  pLabeledStmt.
   **/
  public
    BBlockHirImpl(FlowRoot pFlowRoot,
                  LabeledStmt pLabeledStmt, int pBBlockNumber)
  {
    super(pFlowRoot, pBBlockNumber);
    Label lLabel;
    lLabel = null;
    fIrLink = (IR)pLabeledStmt; // ()
    if (pLabeledStmt != null)
      lLabel = pLabeledStmt.getLabel();
    if (lLabel != null)

//##60   lLabel.setBBlock((BBlock)this);
      flowRoot.fSubpFlow.setBBlock(lLabel, (BBlock)this); //##60
  } // BBlockHirImpl

//====== Interface for control flow analysis ======

//------ Methods to get attributes ------

  public void
    setIrLink(LabeledStmt pLabeledStmt) // ()
  {
    fIrLink = (IR)pLabeledStmt;
    Label lLabel = pLabeledStmt.getLabel();
    if (lLabel != null) {
      //##60 lLabel.setBBlock((BBlock)this);
      flowRoot.fSubpFlow.setBBlock(lLabel, (BBlock)this); //##60
      fIrLink = pLabeledStmt; //##60
    }
  }

//------ Methods for control flow analysis ------

//------ Methods to see the characteristics of basic block ------

//------ Get/set phase-wise information ------

//------ Iterators and methods for traversing ------

  public BBlockSubtreeIterator
    bblockSubtreeIterator()
  {
    return (BBlockSubtreeIterator)
      (new BBlockHirSubtreeIteratorImpl(flowRoot, this)); //##62
  }

  public BBlockNodeIterator
    bblockNodeIterator()
  {
    return (BBlockNodeIterator)(new BBlockHirNodeIteratorImpl(flowRoot, this));
  }

  public IR
    getFirstSubtree()
  {
    BBlockSubtreeIterator lIterator;
    HIR lSubtree;
    lSubtree = fFirstSubtree;
    if (getFlag(BBlock.IS_MODIFIED))
      lSubtree = null;
    if (lSubtree == null) { // Not yet obtained.
      for (lIterator = bblockSubtreeIterator();
           lIterator.hasNext() && (lSubtree == null); ) {
        lSubtree = (HIR)lIterator.next();
      }
    }
    fFirstSubtree = lSubtree; // Record as the first subtree.
    return lSubtree;
  } // getFirstSubtree

  public IR
    getLastSubtree()
  {
    BBlockSubtreeIterator lIterator;
    HIR lSubtree, lPrevSubtree;
    lSubtree = fLastSubtree;
    if (getFlag(BBlock.IS_MODIFIED))
      lSubtree = null;
    lPrevSubtree = lSubtree;
    if ((lSubtree == null) ||
        getFlag(BBlock.IS_MODIFIED)) { // Not yet obtained.
      for (lIterator = bblockSubtreeIterator();
           lIterator.hasNext(); ) {
        if (lSubtree != null) // Record non-null subtree.
          lPrevSubtree = lSubtree;
        lSubtree = (HIR)lIterator.next();
      }
    }
    if (lSubtree == null)
      lSubtree = lPrevSubtree;
    fLastSubtree = lSubtree; // Record as the last subtree.
    return lSubtree;
  } // getLastSubtree

//------ Methods for printing ------

  /** printSubtrees
   *  Print the sequence of subtrees contained in this block.
   *  The order of print is the same as that of bblockSubtreeIterator.
   *  "this" is any basic block.
   **/
  public void
    printSubtrees()
  {
    BBlockSubtreeIterator lIterator;
    HIR lSubtree;
    flowRoot.ioRoot.printOut.print(
      "\nBBlock %d subtrees" + getBlockNumber());
    for (lIterator = bblockSubtreeIterator();
         lIterator.hasNext(); ) {
      lSubtree = (HIR)lIterator.next();
      if (lSubtree != null)
        lSubtree.print(2); // Print with indent 2.
    }
  } // printSubtrees

//======== Methods to modify CFG ========

//======== Elementary methods that change CFG ========

// Following methods are changed to be private in BBlock class
//   changeEdge, addEdge, deleteEdge, deleteBBlock,  ()
//   addToPredList, addToSuccList,
//   deleteFromPredList, deleteFromSuccList.

  /** addToPredList
   *  addToSuccList
   *  Add the basic block pPred/pSucc to the list of predecessors/successors
   *  of this basic block, and this block is added to the list of
   *  successors/predecessors of pPred/pSucc.
   *  @param pPred basic block to be added to the predecessor list
   *    of this basic block.
   *  @param pSucc basic block to be added to the successor list
   *    of this basic block.
   *  Note For method xxxPredxxx, take 1st word of predecessor/successor
   *    or the like, and for method xxxSuccxxx, take 2nd word of
   *    predecessor/successor or the like. The same rule applies to
   *    other methods in this interface.
   **/
 //====== Non-public methods used in modifying CFG ======

//====== Methods for data flow analysis ======

//------ See data flow information of a symbol or expression ------

//------ Get/set bit vector for data flow analysis ------

  /** addToExpNodeList //##25
   *  Add pExpNode to the list corresponding to pExpId to show
   *  that the expression for pExpId is computed at pExpNode.
   *  @param pExpId ExpId for pExpNode.
   *  @param pExpNode Expression other than LHS of assignment.
   */
  public void //##25
    addToExpNodeList(ExpId pExpId, HIR pExpNode)
  {
    List lNodeList;
    if (!fExpNodeListMap.containsKey(pExpId)) {
      lNodeList = new ArrayList();
      fExpNodeListMap.put(pExpId, lNodeList);
    }
    else
      lNodeList = (List)fExpNodeListMap.get(pExpId);
    if (fDbgLevel > 3)
      flowRoot.ioRoot.dbgFlow.print(5, " addToExpNodeList",
        pExpNode.toStringShort()
        + " " + pExpId + " " + this);
    lNodeList.add(pExpNode);
  } // addToExpNodeList

  public List //##25
    getExpNodeList(ExpId pExpId)
  {
    if (fExpNodeListMap.containsKey(pExpId)) {
      return (List)fExpNodeListMap.get(pExpId);
    }
    else
      return null;
  } // getExpNodeList

  public Stmt
    getFirstStmt()
  {
    return (Stmt)getIrLink();
  } // getFirstStmt

  public Stmt
    getLastStmt()
  {
    Stmt lPreviousStmt = null;
    Stmt lStmt;
    //##65 for (BBlockSubtreeIterator lIterator = bblockSubtreeIterator();
    for (BBlockStmtIterator lIterator = new BBlockStmtIterator(this); //##65
         lIterator.hasNext(); ) {
      lStmt = (Stmt)lIterator.next();
      if (lStmt == null)
        break;
      else
        lPreviousStmt = lStmt;
    }
    return lPreviousStmt;
  } // getLastStmt

} // BBlockHirImpl class
