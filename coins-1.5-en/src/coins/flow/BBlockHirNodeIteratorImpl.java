/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.flow;

import coins.FlowRoot;
import coins.ir.IR;
import coins.ir.hir.BlockStmt;
import coins.ir.hir.HIR;
import coins.ir.hir.HirList;
import coins.ir.hir.ExpListExp;
import coins.ir.hir.LabeledStmt;
import coins.ir.hir.Stmt;

import java.util.ArrayList;
import java.util.Iterator;

/** BBlockHirNodeIteratorImpl class
 *
 *  Basic block node iterator
 *  to traverse node in a basic block.
 **/
public class
  BBlockHirNodeIteratorImpl
  implements BBlockNodeIterator
{

//====== Fields ======

  public final FlowRoot
    flowRoot; // Used to access Root information.

  private boolean
    fHeaderPassed = false; // true when header LabeledStmt has passed.

  private boolean
    fEndLabelAppeared = false; // true when endPointLabel appeared.

  //##60 BEGIN
  protected int
    fDbgLevel;

  private BBlock
   fBBlock;

  protected int
    fNextNodeIndex; //##62

  public final SubpFlow
    fSubpFlow;      //##62

  protected Stmt    //##62
    fNextStmt;

  protected boolean
    fJumpReturnAppeared;  //##63

//====== Constructors ======

  public
    BBlockHirNodeIteratorImpl(FlowRoot pFlowRoot, BBlock pBBlock)
  {
    flowRoot = pFlowRoot;
    fSubpFlow = flowRoot.fSubpFlow; //##62
    fDbgLevel = pFlowRoot.ioRoot.dbgFlow.getLevel();
    fBBlock = pBBlock;
    if (pBBlock != null) {
      HIR lSubtree = (HIR)pBBlock.getIrLink();
      if (fDbgLevel > 3)
        flowRoot.ioRoot.dbgFlow.print(4, "BBlockHirNodeIteratorImpl",
          " B" + pBBlock.getBBlockNumber() +
          " " + flowRoot.ioRoot.toStringObjectShort(lSubtree));
      //##62 recordNodesInBBlock(lSubtree);
      fBBlock = pBBlock;
      //##62 fNodeListIterator = fNodeList.iterator();
      //##62 fStmtListIterator = fStmtList.iterator();
      fNextNodeIndex = lSubtree.getIndex(); //##62
      // First node of BBlock should be a LabeledStmt node.
      while ((fNextNodeIndex <= fSubpFlow.getIrIndexMax()) &&
             (fSubpFlow.getIndexedNode(fNextNodeIndex) == null) &&
             (!(fSubpFlow.getIndexedNode(fNextNodeIndex)instanceof LabeledStmt))) {
        fNextNodeIndex++;
      }
      fJumpReturnAppeared = false; //##63
    }
  } // BBlockHirNodeIteratorImpl

//====== Methods to traverse nodes in a basic block ======

  public IR
    next()
  {
    /* //##63
    while ((fNextNodeIndex <= fSubpFlow.getIrIndexMax()) &&
           (fSubpFlow.getIndexedNode(fNextNodeIndex) == null) &&
           (!(fSubpFlow.getIndexedNode(fNextNodeIndex)instanceof LabeledStmt))) {
      fNextNodeIndex++;
    }
    */ //##63
    IR lNextNode = fSubpFlow.getIndexedNode(fNextNodeIndex);
    if (lNextNode instanceof LabeledStmt) {
      if (fHeaderPassed)
        return null;
      fHeaderPassed = true;
    }
    if ((lNextNode != null)&&
        ((lNextNode.getOperator() == HIR.OP_JUMP)||
         (lNextNode.getOperator() == HIR.OP_RETURN))) //##63
      fJumpReturnAppeared = true;//##63
    fNextNodeIndex++;
    if (fDbgLevel > 3)
      flowRoot.ioRoot.dbgFlow.print(5, " next",
        flowRoot.ioRoot.toStringObjectShort(lNextNode));
    return lNextNode;
  } // next

  public boolean
  hasNext()
{
 if (fNextNodeIndex > fSubpFlow.getIrIndexMax())
   return false;
 HIR lNode = (HIR)fSubpFlow.getIndexedNode(fNextNodeIndex);
 if (fJumpReturnAppeared && (lNode instanceof Stmt)) //##63
   return false;  //##63
 if (! (lNode instanceof LabeledStmt))
    return true;
 if (fHeaderPassed)
   return false;
 else
 return true;
} // hasNext

  public HIR // Get the node that refer/set data or
    getNextExecutableNode()
  { // change control flow directly.
    HIR lNextNode = null;
    boolean lExecutable = false;
    for (lNextNode = (HIR)next(); (lExecutable == false) && hasNext();
         lNextNode = (HIR)next()) {
      if (lNextNode != null) {
        switch (lNextNode.getOperator()) {
          case HIR.OP_PROG:
          case HIR.OP_SUBP_DEF:
          case HIR.OP_LABEL_DEF:
          case HIR.OP_INF:
          case HIR.OP_SUBP:
          case HIR.OP_TYPE:
          case HIR.OP_LABEL:
          case HIR.OP_LIST:
          case HIR.OP_SEQ:
          case HIR.OP_ENCLOSE:
          case HIR.OP_LABELED_STMT:
          case HIR.OP_IF:
          case HIR.OP_WHILE:
          case HIR.OP_FOR:
          case HIR.OP_UNTIL:
          case HIR.OP_INDEXED_LOOP:
          case HIR.OP_JUMP:
          case HIR.OP_SWITCH:
          case HIR.OP_BLOCK:
          case HIR.OP_EXP_STMT:
          case HIR.OP_NULL:
            lExecutable = false;
            break;
          default:
            lExecutable = true;
        }
      }
    }
    if (fDbgLevel > 3)
      flowRoot.ioRoot.dbgFlow.print(5, "getNextExecutableNode",
        lNextNode.toStringShort());
    return lNextNode;
  } // getNextExecutableNode

} // BBLockNodeIteratorImpl class
