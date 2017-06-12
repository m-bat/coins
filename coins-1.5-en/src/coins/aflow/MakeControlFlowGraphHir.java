/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * MakeControlFlowGraphHir.java
 *
 * Created on June 18, 2002, 1:49 PM
 */
package coins.aflow;

import java.util.Iterator;

import coins.ir.IrList;
import coins.ir.hir.BlockStmt;
import coins.ir.hir.Exp;
import coins.ir.hir.HIR;
import coins.ir.hir.HirIterator;
import coins.ir.hir.IfStmt;
import coins.ir.hir.JumpStmt;
import coins.ir.hir.LabeledStmt;
import coins.ir.hir.LoopStmt;
import coins.ir.hir.Stmt;
import coins.ir.hir.SubpDefinition;
import coins.ir.hir.SwitchStmt;
import coins.sym.Label;


/**
 *
 * @author  hasegawa
 */
public class MakeControlFlowGraphHir extends MakeControlFlowGraph {
    public final Flow flow;

    /** Creates new MakeControlFlowGraphHir */
    public MakeControlFlowGraphHir(FlowResults pResults) {
        super(pResults);
        flow = flowRoot.aflow;
    }

    public void find(Label pLabel, SubpFlow pSubpFlow) {
        find(pSubpFlow);
    }

    /**
     * <p>Performs the following:</p>
     * <ul>
     * <li>Makes a naive control flow graph based on the HIR specification.</li>
     * <li>Labels entry and exit BBlocks.</li>
     * <li>Removes BBlocks that can never gain the control.</li>
     * </ul>
     */
    void makeControlFlowGraph(SubpDefinition pSubpDef, SubpFlow pSubpFlow) {
        BBlock firstBBlock;
        BBlock lastBBlock;
        Stmt lHirBody = pSubpDef.getHirBody();
        generateBBlocks(lHirBody, pSubpFlow);
        flowRoot.ioRoot.dbgFlow.print(2, //##11 //##25
            "makeControlFlowGraph", "of HIR"); //##11

        firstBBlock = null;
        lastBBlock = null;
        lastBBlock = makeEdge(lHirBody, firstBBlock);
        firstBBlock = findEntryBlock();
        deleteEdge(firstBBlock);
        recordReachableBBlocks();
        findExitBlock();
        pSubpFlow.correlateBBlockAndIR(); //##25
    }

    // Fukuda 2005.07.07
    //Stmt nextStmt; // Used in makeEdge.  //##53
    Stmt lGlobalNextStmt; // Used in makeEdge. //##70
    /**
     *
     * makeEdge:
     *  Scans HIR trees and builds the flow graph.
     *  Stmt pstmt: the current Stmt
     *  BBlock pblock   : the current BBlock
     *
     **/
    private BBlock makeEdge(Stmt pstmt, BBlock pblock) {
        //##53 Stmt nextStmt;

        // Fukuda 2005.07.07
        Stmt nextStmt;
        // Fukuda 2005.07.07
        Stmt nextStmt1;
        LabeledStmt lastStmt;
        Label l;
        IfStmt stmtIF;
        LoopStmt stmtLOOP;
        SwitchStmt stmtSWITCH;
        Stmt   loopInitPart;  //##53
        BBlock loopInitBlock; //##53
        BBlock currBlock;
        BBlock thenBlock;
        BBlock elseBlock;
        BBlock LabelBlock;
        BBlock bodyBlock;
        BBlock stepBlock;
        BBlock loopbackBlock;
        BBlock CondInitBlock;
        BBlock endBlock;
        BBlock defaultBlock;
        BBlock switchBlock;
        LabelBlock = null;
        BBlock lLoopStepBlock; //##70

        int lop;
        nextStmt = pstmt;
        currBlock = pblock;
        lGlobalNextStmt = nextStmt; //##70

        flow.dbg(5, "\nmakeEdge", "to stmt= " + pstmt + " from B" + pblock); //##53

        while (nextStmt != null) {
            lop = nextStmt.getOperator();
            flow.dbg(5, "\nMakeEdge", "nextStmt = " + nextStmt +
                     " nextToNext " + ioRoot.toStringObject(nextStmt.getNextStmt())
                     + " lGlobalNextStmt " + lGlobalNextStmt); //##53 //##70

            switch (lop) {
            case HIR.OP_IF:
                stmtIF = (IfStmt) nextStmt;
                thenBlock = makeEdge(stmtIF.getThenPart(), currBlock);
                elseBlock = makeEdge(stmtIF.getElsePart(), currBlock);
                LabelBlock = (BBlock) fResults.getBBlockForLabel(stmtIF.getEndLabel());

                //				LabelBlock =stmtIF.getEndLabel().getBBlock();
                addEdge(thenBlock, LabelBlock);
                addEdge(elseBlock, LabelBlock);
                //##53 currBlock = LabelBlock;
                //##53 nextStmt = nextStmt.getNextStmt();
                //##53 BEGIN
                LabeledStmt lIfEnd = (LabeledStmt)stmtIF.getChild(4);
                flow.dbg(6, " ", "if-end " + lIfEnd.toStringShort());
                if (lIfEnd.getStmt() != null)
                  currBlock = makeEdge(lIfEnd.getStmt(), LabelBlock);
                else
                  currBlock = LabelBlock;
                nextStmt = getNextStmtSeeingAncestor(stmtIF);
                flow.dbg(6, " next-of-if " + nextStmt+ " lGlobalNextStmt " + lGlobalNextStmt); //##70
                lGlobalNextStmt = nextStmt; //##70
                //##53 END

                break;

            case HIR.OP_LABELED_STMT:
                LabelBlock = (BBlock) fResults.getBBlockForLabel(((LabeledStmt) nextStmt).getLabel());
                addEdge(currBlock, LabelBlock);
                currBlock = LabelBlock;
                nextStmt1 = ((LabeledStmt) nextStmt).getStmt();

                if (nextStmt1 == null) {
                    //##53 nextStmt = nextStmt.getNextStmt();
                    nextStmt = getNextStmtSeeingAncestor(nextStmt);  //##53
                    flow.dbg(6, " next-of-labeledSt " + nextStmt+ " lGlobalNextStmt " + lGlobalNextStmt); //##70
                } else {
                    nextStmt = nextStmt1;
                }
                lGlobalNextStmt = nextStmt; //##70

                break;

            case HIR.OP_SWITCH:

                int CaseCount;
                stmtSWITCH = (SwitchStmt) nextStmt;
                CaseCount = stmtSWITCH.getCaseCount();

                for (int i = 0; i < CaseCount; i++) {
                    //					LabelBlock=stmtSWITCH.getCaseLabel(i).getBBlock();
                    LabelBlock = (BBlock) fResults.getBBlockForLabel(stmtSWITCH.getCaseLabel(
                                i));
                    addEdge(currBlock, LabelBlock);
                }

                //				LabelBlock=stmtSWITCH.getDefaultLabel().getBBlock();
                defaultBlock = (BBlock) fResults.getBBlockForLabel(stmtSWITCH.getDefaultLabel());
                endBlock = (BBlock) fResults.getBBlockForLabel(stmtSWITCH.getEndLabel());
                if (defaultBlock == null)
                  addEdge(currBlock, endBlock);
                else
                  addEdge(currBlock, defaultBlock);
                bodyBlock = makeEdge(stmtSWITCH.getBodyStmt(), currBlock);

                //				endBlock=stmtSWITCH.getEndLabel().getBBlock();
                addEdge(bodyBlock, endBlock);
                //##53 currBlock = endBlock;
                //##53 nextStmt = nextStmt.getNextStmt();
                //##53 BEGIN
                LabeledStmt lSwitchEnd = (LabeledStmt)stmtSWITCH.getChild(4);
                flow.dbg(6, " ", "switch-end " + lSwitchEnd.toStringShort());
                if (lSwitchEnd.getStmt() != null)
                  currBlock = makeEdge(lSwitchEnd.getStmt(), endBlock);
                else
                  currBlock = endBlock;
                nextStmt = getNextStmtSeeingAncestor(stmtSWITCH);
                flow.dbg(6, " next-of-switch " + nextStmt+ " lGlobalNextStmt " + lGlobalNextStmt); //##70
                lGlobalNextStmt = nextStmt; //##70
                //##53 END

                break;

            case HIR.OP_WHILE:
            case HIR.OP_FOR:
            case HIR.OP_INDEXED_LOOP:
            case HIR.OP_UNTIL:
                stmtLOOP = (LoopStmt) nextStmt;
                l = stmtLOOP.getLoopBackLabel();

                //				loopbackBlock = (BBlock)fResults.getBBlockForLabel( l);
                loopbackBlock = (BBlock) fResults.getBBlockForLabel(l);
                lLoopStepBlock = (BBlock) fResults.getBBlockForLabel(stmtLOOP.getLoopStepLabel()); //##70
                //				endBlock = stmtLOOP.getLoopEndLabel().getBBlock();
                endBlock = (BBlock) fResults.getBBlockForLabel(stmtLOOP.getLoopEndLabel());
                flow.dbg(6, "Loop", "backBlock " + loopbackBlock + " stepLabel " +
                         stmtLOOP.getLoopStepLabel() + " stepBlock " +
                         lLoopStepBlock + " endBlock " + endBlock); //##70
                /* //##53
                CondInitBlock = makeEdge(stmtLOOP.getConditionalInitPart(),
                        currBlock);

                if (CondInitBlock == currBlock) {
                    addEdge(currBlock, loopbackBlock);
                    bodyBlock = makeEdge(stmtLOOP.getLoopBodyPart(),
                            loopbackBlock);

                    //						currBlock.getSuccEdge(loopbackBlock).flagBox().setFlag(Edge.LOOP_BACK_EDGE, true); //## Tan //##9
                } else {
                    bodyBlock = makeEdge(stmtLOOP.getLoopBodyPart(),
                            CondInitBlock);
                    addEdge(CondInitBlock, endBlock);
                }
                */ //##53
                //##53 BEGIN
                loopInitPart = stmtLOOP.getLoopInitPart();
                // loopInitPart may contain jump-to-loopBodyPart to implement
                // conditional initiation.
                // Make edge from currBlock to LoopInitPart.
               flow.dbg(6, " make-edge to loop-init part from " + currBlock );  //##70
               loopInitBlock = makeEdge(loopInitPart, currBlock);
                // How about loopInitBlock to loopBackBlock ?
                // Add edge from currBlock to loopBackBlock ?
                flow.dbg(6, " add-edge to loop-back block from " + currBlock + " initBlock " + loopInitBlock);  //##70
                addEdge(currBlock, loopbackBlock);
                // Make edge from loopbackBlock to loop-body part.
                flow.dbg(6, " make-edge to loop-body part from loop-back block " + loopbackBlock );  //##70
                bodyBlock = makeEdge(stmtLOOP.getLoopBodyPart(),
                           loopbackBlock);
                //##53 END
                l = stmtLOOP.getLoopStepLabel();

                if (l != null) {
                    stepBlock = (BBlock) fResults.getBBlockForLabel(l);

                    //						addEdge(bodyBlock,stepBlock);
                    if (stepBlock != null) {
                        // How about from bodyBlock to stepBlock ?
                        // The label of step block is attached at the end of body block.
                        if (bodyBlock != stepBlock) {
                          flow.dbg(5, " add edge", "from loop-body part " + bodyBlock
                            + " to stepBlock " + stepBlock); //##70
                          addEdge(bodyBlock, stepBlock);
                        }
                        // Make edge from stepBlock to loopbackBlock.
                        flow.dbg(5, " add edge", "from loop-step block " + stepBlock
                          + " to loop-back block " + loopbackBlock); //##70
                        addEdge(stepBlock, loopbackBlock);
                        stepBlock.getSuccEdge(loopbackBlock).flagBox().setFlag(Edge.LOOP_BACK_EDGE,
                            true); //## Tan //##9
                        // Make edge from stepBlock to loop-step part.
                        flow.dbg(5, " make-edge to loop-step part from stepBlock " + stepBlock); //##70
                        BBlock lStepBlock2 = makeEdge(stmtLOOP.getLoopStepPart(),
                                                      stepBlock);  //##53
                   } else {
                     flow.dbg(4, " stepBlock of " + stmtLOOP + " is null"); //##70
                     if (bodyBlock != null) { // no step part (or merged with the body)
                       // Add edge from bodyBlock to loopbackBlock.
                       flow.dbg(5, " add edge from bodyBlock " + bodyBlock + " to loop-back block " + loopbackBlock); //##70
                       addEdge(bodyBlock, loopbackBlock);
                       bodyBlock.getSuccEdge(loopbackBlock).flagBox().setFlag(Edge.
                         LOOP_BACK_EDGE,
                         true); //## Tan //##9
                     }
                     else {
                       //System.out.println("Returned or Jumped");//
                     }
                   }

                    if (stmtLOOP.getLoopEndCondition() == (Exp) null) {
                        // Add edge from loopbackBlock to endBlock.
                        addEdge(loopbackBlock, endBlock);
                    } else if (stepBlock != null) {
                        // End condition is not null.
                        // Add edge from stepBlock to endBlock.
                        // How about stepBlock to end-condition part ?
                        addEdge(stepBlock, endBlock);
                    } else {
                        // End condition is not null and
                        // stepBlock is null.
                        // Add edge from bodyBlock to endBlock.
                        // How about bodyBlock to end-condition block ?
                        addEdge(bodyBlock, endBlock);
                       }
                } else {
                    // No loop-step label.
                    // Add edge from bodyBlock to loopbackBlock.
                    addEdge(bodyBlock, loopbackBlock);
                    // Add edge from loopbackBlock to endBlock.
                    addEdge(loopbackBlock, endBlock);
                   }

                //##53 currBlock = endBlock;
                //##53 nextStmt = nextStmt.getNextStmt();
                //##53 BEGIN
                LabeledStmt lLoopEnd = (LabeledStmt)stmtLOOP.getChild(7);
                flow.dbg(6, " ", "loop-end " + lLoopEnd.toStringShort());
                if (lLoopEnd.getStmt() != null) {
                  currBlock = makeEdge(lLoopEnd.getStmt(), endBlock);
                }else
                  currBlock = endBlock;
                flow.dbg(5, " get next statement", "of loop " + stmtLOOP.toStringShort());
                nextStmt = getNextStmtSeeingAncestor(stmtLOOP);
                flow.dbg(6, " next-of-loop " + nextStmt+ " lGlobalNextStmt " + lGlobalNextStmt); //##70
                lGlobalNextStmt = nextStmt; //##70
               //##53 END

                break;

            case HIR.OP_RETURN:
                currBlock = null;
                //##53 nextStmt = nextStmt.getNextStmt();
                nextStmt = getNextStmtSeeingAncestor(nextStmt);  //##53
                flow.dbg(6, " next-of-return " + nextStmt+ " lGlobalNextStmt " + lGlobalNextStmt); //##70
                lGlobalNextStmt = nextStmt; //##70
                break;

            case HIR.OP_JUMP:
                l = ((JumpStmt) nextStmt).getLabel();
                LabelBlock = (BBlock) fResults.getBBlockForLabel(l);
                addEdge(currBlock, LabelBlock);
                currBlock = null;
                //##53 nextStmt = nextStmt.getNextStmt();
                nextStmt = getNextStmtSeeingAncestor(nextStmt);  //##53
                flow.dbg(6, " next-of-jump " + nextStmt+ " lGlobalNextStmt " + lGlobalNextStmt); //##70
                lGlobalNextStmt = nextStmt; //##70
                break;

            case HIR.OP_BLOCK:
                currBlock = makeEdge(((BlockStmt) nextStmt).getFirstStmt(),
                        currBlock); //## Fukuda 020322
                //##53 nextStmt = ((BlockStmt) nextStmt).getNextStmt(); //## Fukuda 020322
             //global nextStmt ??
                //##70 nextStmt = getNextStmtSeeingAncestor(nextStmt);  //##53
                nextStmt = getNextStmtSeeingAncestor(lGlobalNextStmt); //##70
                flow.dbg(6, " next-of-block " + nextStmt+ " lGlobalNextStmt " + lGlobalNextStmt); //##70
                lGlobalNextStmt = nextStmt; //##70
                break;

            default:
              // Non-control statement.
              //##53 nextStmt = nextStmt.getNextStmt();
              nextStmt = getNextStmtSeeingAncestor(nextStmt);  //##53
              flow.dbg(6, " next-of-default " + nextStmt+ " lGlobalNextStmt " + lGlobalNextStmt); //##70
              lGlobalNextStmt = nextStmt; //##70
            }
        } // end of while
        flow.dbg(5, " return " + currBlock + " for " + pstmt+ " lGlobalNextStmt " + lGlobalNextStmt); //##70
        return currBlock;
    }

/* //##70
//##53 BEGIN
    private Stmt
    getNextStmtSeeingAncestor( Stmt pStmt )
    {
      System.out.print("\n getNextStmtSeeingAncestor " + pStmt); //###
      if (pStmt == null)
        return null;
      if (pStmt.getNextStmt() != null) {
        System.out.print(" return " + pStmt.getNextStmt()); //###
        return pStmt.getNextStmt();
      }
      HIR lAncestor = pStmt;
      do {
        lAncestor = (HIR) lAncestor.getParent();
        System.out.print(" ancestor " + lAncestor); //###
        if (lAncestor == null)
          return null;
        if ( (lAncestor instanceof IfStmt) ||
             (lAncestor instanceof LoopStmt) ||
             (lAncestor instanceof SwitchStmt)) {
          System.out.print(" return null"); //###
          return null;
        }
        else if (lAncestor.getNextStmt() != null) {
          System.out.print(" return " + lAncestor.getNextStmt()); //###
          return lAncestor.getNextStmt();
        }
      }while (lAncestor != null);
      return null;
  }
  //##53 END
*/ //##70

//##70 BEGIN
private Stmt
  getNextStmtSeeingAncestor(Stmt pStmt)
{
  Stmt lNextStmt; //##70
  flow.dbg(5, " getNextStmtSeeingAncestor " + pStmt);
  if (pStmt == null)
    return null;
  if (pStmt.getNextStmt() != null)
    return pStmt.getNextStmt();
  // getNextStmt() is null. Get the next statement of ancestor.
  HIR lAncestor = pStmt;
  do {
    lAncestor = (HIR)lAncestor.getParent();
    if (lAncestor == null)
      return null;
    flow.dbg(5, " ancestor " + lAncestor.toStringShort());
    if ((lAncestor instanceof IfStmt) ||
        (lAncestor instanceof LoopStmt) ||
        (lAncestor instanceof SwitchStmt)) {
      flow.dbg(5, " Parent is If/Loop/Switch. return null. ");
      return null;
    }else if (lAncestor instanceof BlockStmt) {
      flow.dbg(5, "\n End of BlockStmt. Get nextstmt of " + lAncestor.toStringShort());
      //##70 return getNextStmtSeeingAncestor((Stmt)lAncestor);
      lNextStmt = getNextStmtSeeingAncestor((Stmt)lAncestor); //##70
      flow.dbg(6, " next-of-BlockStmt in getNextStmt " + lNextStmt); //##70
      return lNextStmt; //##70
    }else if (lAncestor.getNextStmt() != null)
      return lAncestor.getNextStmt();
  }
  while (lAncestor != null);
  return null;
} // getNextStmtSeeingAncestor

//##70 END

    private void generateBBlocks(Stmt pHirBody, SubpFlow pSubpFlow) {
        HIR lNode;
        IrList lLabelDefList;
        Label lLabel;
        BBlockHir lBBlock;
        flow.dbg(2, "generateBBlocks",
           pSubpFlow.getSubpSym().getName()); //##25
        // for (HirIterator lHirIterator = new FlowUtil(fResults).hirIterator(pHirBody);
        for (HirIterator lHirIterator = flowRoot.hirRoot.hir.hirIterator(
                    pHirBody); lHirIterator.hasNext();) {
            lNode = lHirIterator.next();
            flow.dbg(5, "generateBBlocks", "lNode = " + lNode);

            if (lNode instanceof LabeledStmt) {
                lBBlock = (BBlockHir) pSubpFlow.bblock((LabeledStmt) lNode); // Create a basic block.
                lLabelDefList = ((LabeledStmt) lNode).getLabelDefList();

                for (Iterator lIterator = lLabelDefList.iterator();
                        lIterator.hasNext();) { // Set link between HIR & Label.
                    lLabel = ((coins.ir.hir.LabelDef) (lIterator.next())).getLabel(); //###12
                    fResults.put("BBlockForLabel", lLabel, lBBlock);
                    flow.dbg(5, "generateBBlocks",
                        "Label: " + lLabel + ", BBlock: " + lBBlock);
                }
            }
        }
    }
}
