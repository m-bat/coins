/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.flow;

import java.util.ArrayList;
import java.util.Set; //##63
import java.util.HashSet; //##63

import coins.FlowRoot;
import coins.IoRoot;
import coins.ir.IR;
import coins.ir.hir.BlockStmt;
import coins.ir.hir.ExpStmt;  //##65
import coins.ir.hir.HIR;
import coins.ir.hir.HIR_Impl;
import coins.ir.hir.IfStmt;
import coins.ir.hir.LabeledStmt;
import coins.ir.hir.LoopStmt;
import coins.ir.hir.Stmt;
import coins.ir.hir.SwitchStmt;
import coins.sym.Label;

/** BBlockSubtreeIteratorImpl class
 *
 *  Basic block subtree iterator
 *  to traverse HIR top-subtrees in a basic block.
 */
public class
//##63  BBlockHirSubtreeIteratorImpl
  BBlockSubtreeIteratorImpl //##63
  implements BBlockSubtreeIterator
{

//====== Fields ======

  public final FlowRoot
    flowRoot; // Used to access Root information.

  public final IoRoot
    ioRoot; // Used to access Root information.

  private BBlock
    fBBlock; // Basic block specified for this iterator.

  protected SubpFlow
    fSubpFlow;        //##62

  protected int
    fNextStmtIndex; //##62

  protected boolean
    fHeaderPassed = false;

  protected HIR
    fCurrSubtree = null, //##65
    fNextSubtree = null; //##65

  protected final int //##60
    fDbgLevel; //##60

//====== Constructors ======

  public
//##63    BBlockHirSubtreeIteratorImpl(
    BBlockSubtreeIteratorImpl( //##63
    FlowRoot pFlowRoot,
    BBlock pBBlock)
  {
    flowRoot = pFlowRoot;
    ioRoot = pFlowRoot.ioRoot;
    fSubpFlow = flowRoot.fSubpFlow; //##62
    fDbgLevel = ioRoot.dbgFlow.getLevel(); //##60
    if ((pBBlock != null)&&
        flowRoot.isHirAnalysis()) { //##65
      if (fDbgLevel > 3)
        ioRoot.dbgFlow.print(4, "BBlockSubtreeIteratorImpl",
          "B" + pBBlock.getBBlockNumber()
          + " fIrLink " + ((BBlockImpl)pBBlock).fIrLink +
          " fIteratorInitiated " +
          ((SubpFlowImpl)fSubpFlow).fIteratorInitiated + "\n"); //##60
      if (((HirSubpFlowImpl)fSubpFlow).fStmtExpSeq == null) {
        if (fDbgLevel > 3)
          ioRoot.dbgFlow.print(4, "compute fStmtExpSeq for BBlockHirSubtreeIterator",
            fSubpFlow.getSubpSym().getName() +
            " IrIndexMin " + fSubpFlow.getIrIndexMin() +
            " IrIndexMax " + fSubpFlow.getIrIndexMax()); //##60
        boolean lJumpReturnAppeared = false;  //##63
        ((HirSubpFlowImpl)fSubpFlow).fStmtExpSeq = new HIR[
              (fSubpFlow.getIrIndexMax() - fSubpFlow.getIrIndexMin())/2 + 2]; //##65
         ((HirSubpFlowImpl)fSubpFlow).fStmtExpSeqIndexForBBlock = new int[
           fSubpFlow.getNumberOfBBlocks() + 2];
         ((HirSubpFlowImpl)fSubpFlow).fStmtExpCount = 0;
         Set lConditionalAndSelectionExp = new HashSet(); //##65
         int lBBlockNum;
         int lIndex = fSubpFlow.getIrIndexMin();
         while (lIndex <= fSubpFlow.getIrIndexMax()) {
           IR lNode = fSubpFlow.getIndexedNode(lIndex);
           lIndex++;
           if (lNode == null) //##65
             continue; //##65
           int lOperator = lNode.getOperator(); //##65
           if (lNode instanceof Stmt) {
             //##63 BEGIN
             if (lJumpReturnAppeared) {
               while ((lIndex <= fSubpFlow.getIrIndexMax())&&
                      (! (lNode instanceof LabeledStmt))) {
                 // Skip to LabeledStmt because lNode can not be reached.
                 lNode = fSubpFlow.getIndexedNode(lIndex);
                 if (lNode != null) //##77
                   lOperator = lNode.getOperator(); //##77
                 lIndex++;
               }
               lJumpReturnAppeared = false;
             }
             //##63 END
             switch (lOperator) { //##65
               case HIR.OP_LABELED_STMT:
                 BBlock lBBlock = fSubpFlow.getBBlock((HIR)lNode); //##63
                 if ((lBBlock == null)||(lBBlock.getBBlockNumber() == 0)) //##63
                   break; //##63
                 lBBlockNum = lBBlock.getBBlockNumber();
                 ((HirSubpFlowImpl)fSubpFlow).fStmtExpSeqIndexForBBlock[lBBlockNum]
                   = ((HirSubpFlowImpl)fSubpFlow).fStmtExpCount;
                 ((HirSubpFlowImpl)fSubpFlow).fStmtExpSeq[
                   ((HirSubpFlowImpl)fSubpFlow).fStmtExpCount] = (HIR)lNode;
                 ((HirSubpFlowImpl)fSubpFlow).fStmtExpCount++;
                 break;
               case HIR.OP_RETURN:
                 lJumpReturnAppeared = true; //##63
               case HIR.OP_ASSIGN:
               case HIR.OP_ASM: //##70
               case HIR.OP_EXP_STMT:
                 ((HirSubpFlowImpl)fSubpFlow).fStmtExpSeq[
                   ((HirSubpFlowImpl)fSubpFlow).fStmtExpCount] = (HIR)lNode;
                 ((HirSubpFlowImpl)fSubpFlow).fStmtExpCount++;
                 break;
               case HIR.OP_IF: //##63
                 lConditionalAndSelectionExp.add(((IfStmt)lNode).getIfCondition()); //##63
                 break;
               //##65 BEGIN
               case HIR.OP_SWITCH:
                 lConditionalAndSelectionExp.add(((SwitchStmt)lNode).getSelectionExp());
                 break;
               //##65 END
               case HIR.OP_JUMP:
                 lJumpReturnAppeared = true; //##63
               default:
                 break;
             }
             //##63 BEGIN
             }else { // Not Stmt
               if (lConditionalAndSelectionExp.contains(lNode)) { // Record conditional exp.
                 ((HirSubpFlowImpl)fSubpFlow).fStmtExpSeq[
                   ((HirSubpFlowImpl)fSubpFlow).fStmtExpCount] = (HIR)lNode;
                 ((HirSubpFlowImpl)fSubpFlow).fStmtExpCount++;
                 lConditionalAndSelectionExp.remove(lNode);
               }
               //##63 END
               //##65 BEGIN
               else if ((lOperator == HIR.OP_CALL)&&
                        (! (lNode.getParent() instanceof ExpStmt))) {
                 ((HirSubpFlowImpl)fSubpFlow).fStmtExpSeq[
                   ((HirSubpFlowImpl)fSubpFlow).fStmtExpCount] = (HIR)lNode;
                 ((HirSubpFlowImpl)fSubpFlow).fStmtExpCount++;
               }
               //##65 END
           }
         } // End of while loop
         if (fDbgLevel > 3) {
           ioRoot.dbgFlow.print(4, "  fStmtCount "
             + ((HirSubpFlowImpl)fSubpFlow).fStmtExpCount + "\n"); //##65
           if (fDbgLevel >= 4) {
             for (int lIndex2 = 0; lIndex2 < ((HirSubpFlowImpl)fSubpFlow).fStmtExpCount;
                  lIndex2++) {
               if (((HirSubpFlowImpl)fSubpFlow).fStmtExpSeq[lIndex2] != null) //##94
               ioRoot.dbgFlow.print(4, ((HirSubpFlowImpl)fSubpFlow).
                 fStmtExpSeq[lIndex2].toStringShort());
             }
             //##65 BEGIN
             for (int lIndex3 = 0; lIndex3 < fSubpFlow.getNumberOfBBlocks();
                  lIndex3++) {
               ioRoot.dbgFlow.print(4, " " + ((HirSubpFlowImpl)fSubpFlow).
                 fStmtExpSeqIndexForBBlock[lIndex3]);
             }
             //##65 END
           }
         }
         ((SubpFlowImpl)fSubpFlow).fIteratorInitiated = true; //##65
      } // End of fStmtExpSeq==null
      fNextStmtIndex = ((HirSubpFlowImpl)fSubpFlow).fStmtExpSeqIndexForBBlock[
        pBBlock.getBBlockNumber()];
       fHeaderPassed = false;
       fNextSubtree =
         ((HirSubpFlowImpl)fSubpFlow).fStmtExpSeq[fNextStmtIndex]; //##65
    }
  } // BBlockSubtreeIteratorImpl

//====== Methods to traverse subtrees in a basic block ======

  /** next:
   *  Get the next top subtree in the specified basic block.
   *  If no one is left, return null.
   **/
  public IR
    next()
  {
    if (fDbgLevel > 3)
      ioRoot.dbgHir.print(7, " next of",
        ioRoot.toStringObjectShort(fCurrSubtree));
    if (((SubpFlowImpl)fSubpFlow).fIteratorInitiated) {
      IR lNextSubtree = null;
      if (fNextStmtIndex < ((HirSubpFlowImpl)fSubpFlow).fStmtExpCount) {
        lNextSubtree =
          ((HirSubpFlowImpl)fSubpFlow).fStmtExpSeq[fNextStmtIndex];
        if (lNextSubtree instanceof LabeledStmt) {
          if (fHeaderPassed)
            lNextSubtree = null;
          else
            fHeaderPassed = true;
        }
      }
      if (fDbgLevel > 3)
        ioRoot.dbgHir.print(5, " next is " + ioRoot.toStringObjectShort(lNextSubtree));
      fNextStmtIndex++;
      fCurrSubtree = (HIR)lNextSubtree; //##65
      return fCurrSubtree;
    //##65 BEGIN
    }else {
      // fStmtExpSeq is no more effective.
      if (fNextSubtree instanceof LabeledStmt) {
        if (fHeaderPassed) {
          fCurrSubtree = null;
        }else {
          fHeaderPassed = true;
          if (fCurrSubtree == null) {
            // This case is the first node of BBlock.
            fCurrSubtree = fNextSubtree;
            fNextSubtree = getNextSubtree(fCurrSubtree, false);
          }else {
            fCurrSubtree = fNextSubtree;
            fNextSubtree = null;
          }
        }
      }else {
        if (fNextSubtree != null) {
          fCurrSubtree = fNextSubtree;
          fNextSubtree = null;
        }else if (fCurrSubtree != null) {
          fCurrSubtree = getNextSubtree(fCurrSubtree, true);
          fNextSubtree = null;
        }
      }
      if (fDbgLevel > 3)
        ioRoot.dbgHir.print(4, " fIteratorInitiated=false "
          + ioRoot.toStringObjectShort(fCurrSubtree));
      return fCurrSubtree;
      //##65 END
    }
  } // next

  public boolean
    hasNext()
  {
    if (fDbgLevel > 3)
      ioRoot.dbgHir.print(7, " hasNext "
        + ioRoot.toStringObjectShort(fCurrSubtree) +
        " " + ioRoot.toStringObjectShort(fCurrSubtree)); //###
    if (((SubpFlowImpl)fSubpFlow).fIteratorInitiated) {
      IR lNextSubtree;
      if (fNextStmtIndex < ((HirSubpFlowImpl)fSubpFlow).fStmtExpCount) {
        lNextSubtree =
          ((HirSubpFlowImpl)fSubpFlow).fStmtExpSeq[fNextStmtIndex];
        if ((lNextSubtree instanceof LabeledStmt) && fHeaderPassed) {
          return false;
        }
        else
          return true;
      }
      return false;
    //##65 BEGIN
    }else {
      if (fNextSubtree != null)
        return true;
      if (fCurrSubtree == null) {
        // fNextSubtree == null and fCurrSubtree == null.
        return false;
      }else
        fNextSubtree = getNextSubtree(fCurrSubtree, false);
      if (fNextSubtree != null)
        return true;
      else
        return false;
    //##65 END
    }
  } // hasNext

  //##65 BEGIN
  protected HIR
    getNextSubtree( HIR pHir, boolean pGet )
  {
    if (pHir == null)
      return null;
    HIR lHir = ((HIR_Impl)pHir).getNextNode();
    if (fDbgLevel > 3)
      ioRoot.dbgHir.print(5, " getNextSubtree"
        ,pHir.toStringShort() + " next " + ioRoot.toStringObjectShort(lHir));
    while (lHir != null) {
      int lOperator = lHir.getOperator();
      if (lHir instanceof Stmt) {
        switch (lOperator) {
          case HIR.OP_LABELED_STMT:
            if (fHeaderPassed)
              return null;
            else {
              if (pGet)
                fHeaderPassed = true;
            }
          case HIR.OP_ASSIGN:
          case HIR.OP_ASM: //##70
          case HIR.OP_EXP_STMT:
          case HIR.OP_RETURN:
          case HIR.OP_IF: //##63
            return lHir;
          case HIR.OP_CALL:
            if (lHir.getParent() instanceof ExpStmt)
              break;
            else
              return lHir;
          default:
            break;
        }
      }else {
        HIR lParent = (HIR)lHir.getParent();
        if (lParent instanceof IfStmt) {
          if (lHir == ((IfStmt)lParent).getIfCondition())
            return lHir;
        }else if (lParent instanceof SwitchStmt) {
          if (lHir == ((SwitchStmt)lParent).getSelectionExp())
            return lHir;
        }
      }
      lHir = ((HIR_Impl)lHir).getNextNode();
    }
    return null;
  } // getNextSubtree
  //##65 END

} // BBlockSubtreeIteratorImpl class
