/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
//##60 package coins.aflow;
package coins.flow; //##60

import coins.FlowRoot; //##60
import coins.IoRoot; //##60
import coins.flow.FAList; //##60
import coins.alias.AliasAnal; //##56
import coins.alias.RecordAlias; //##56
import coins.alias.alias2.AliasAnalHir2; //##56
import coins.ir.hir.HIR;
import coins.ir.hir.HirIterator;
import coins.ir.hir.LabelDef; //##25
import coins.ir.hir.LabeledStmt;
import coins.ir.hir.SubpDefinition; //##25
import coins.sym.ExpId; //##25
import coins.sym.FlowAnalSym;
import coins.sym.Sym;
import coins.sym.SymIterator;

/**
 * HIR data flow initiation class.
 */
public class InitiateFlowHir
  extends InitiateFlow
{
  protected FlowRoot flowRoot; //##60
  protected IoRoot ioRoot; //##60
  protected SubpFlow fSubpFlow; //##60
  protected int fDbgLevel; //##60
  public InitiateFlowHir() //##60
  {
  }

  /**
   * <p>Initiates data flow analysis for the given SubpFlow.</p>
   *
   * <p>Does the following:
   * <li>Link function calls' argument nodes to the argument list node (holds as a map entry in FlowResults).<li>
   * <li>Flags whether each BBlock
   * <ul><li>has any assignment to the contents of pointers.</li>
   *   <li>ever accesses a pointer variable.</li>
   *   <li>ever accesses a structure/union variable.</li>
   *   <li>ever calls other functions.</li>
   * </ul>
   * <li>Sets a unique index to each node.</li>
     * <li>link nodes to the enclosing BBlock (as a map entry in FlowResults).</li>
   * <li>Sets a unique index to each accessed symbol, and hold a table of such symbols.</li>
   * <li>Assigns FlowExpIds to the pertinent nodes</li>.
   * <li>Do preparation for alias analysis</li>.
   * </ul></p>
   */
  //##60 public void initiate(SubpFlow pSubpFlow)
  public void initiateDataFlow(SubpFlow pSubpFlow)
  {
    //##60 AssignFlowExpId lAssigner = pSubpFlow.assigner();
    fSubpFlow = pSubpFlow; //##60
    flowRoot = ((SubpFlowImpl)pSubpFlow).flowRoot; //##60
    ioRoot = ((SubpFlowImpl)pSubpFlow).ioRoot; //##60
    HIR lNode;
    Sym lSym;
    FlowAnalSym lFlowAnalSym;
    int lIndex = 0;
    int lUsedSymCount = 0;
    BBlock lCurrentBBlock = null;
    fDbgLevel = ioRoot.dbgFlow.getLevel(); //##60
    //  FAList lFlowIrLinkCells = new FAList();
    FAList lSymIndexTable = new FAList();
    HIR lParent;
    HIR lHirListNode = null;
//##25 BEGIN
    SubpDefinition lSubpDefinition =
      pSubpFlow.getSubpDefinition();
    if (fDbgLevel > 0)
      flowRoot.ioRoot.dbgFlow.print(2, "InitiateFlowHir.initiateDataFlow",
        lSubpDefinition.getSubpSym().toString());
    // pSubpFlow.clear();
    //##92 BEGIN
    if (pSubpFlow.getNumberOfBBlocks() == 0) {
      flowRoot.ioRoot.dbgFlow.print(2, "controlFlowAnal is not yet done.",
        " Do it.");
      pSubpFlow.getFlowRoot().flow.controlFlowAnal(pSubpFlow);
    }
    //##92 END
    pSubpFlow.computeSetOfGlobalVariables();
    pSubpFlow.computeSetOfAddressTakenVariables();
    // Assign FlowExpId to expression nodes.
    //##60 lAssigner.assign(); //##25
    if (! pSubpFlow.isComputed(pSubpFlow.DF_EXPID)) //##60
      ((HirSubpFlowImpl)pSubpFlow).allocateExpIdForSubp(); //##60
    // Check existence of pointer/struct/union/call
    // and make ExpNodeList for ExpId.
//##25 END
    //##60 BEGIN
    int fBBlockCount = pSubpFlow.getNumberOfBBlocks();
    ((SubpFlowImpl)pSubpFlow).hasCall = new boolean[fBBlockCount + 1];
    ((SubpFlowImpl)pSubpFlow).hasUsePointer = new boolean[fBBlockCount + 1];
    ((SubpFlowImpl)pSubpFlow).hasStructUnion = new boolean[fBBlockCount + 1];
    ((SubpFlowImpl)pSubpFlow).hasPointerAssign = new boolean[fBBlockCount + 1];
    //##65 ((SubpFlowImpl)pSubpFlow).fArrayOfSetRefReprList = new SetRefReprList[
    //##65   fBBlockCount + 1];
    //##60 END
    //##60 for (HirIterator lHirIterator = FlowUtil.hirIterator(
    for (HirIterator lHirIterator = flowRoot.hirRoot.hir.hirIterator( //##60
      pSubpFlow.getSubpDefinition().getHirBody());
         lHirIterator.hasNext(); ) {
      lNode = (HIR)lHirIterator.next();

      // System.out.println(HIR.OP_CODE_NAME[lNode.getOperator()]);
      if (lNode != null) {
        //##25 BEGIN
        if (fDbgLevel > 0)
          flowRoot.flow.dbg(7, " initiate ", lNode.toString());
        FlowAnalSym lSym2 = lNode.getSymOrExpId();
        if (lSym2 != null) {
          if (lSym2 instanceof ExpId) {
            if (lCurrentBBlock == null) {
              if (fDbgLevel > 0)
                flowRoot.flow.dbg(2, " lCurrentBBlock is null for ",
                  lNode.toString());
            }
            else
              ((BBlockHirImpl)lCurrentBBlock).addToExpNodeList((ExpId)lSym2,
                lNode);
          }
        }
        //##25 END
        switch (lNode.getOperator()) {
          //##25 BEGIN
          case HIR.OP_LABEL_DEF:

            //##60 lCurrentBBlock = (BBlockHir)fResults.getBBlockForLabel(
            lCurrentBBlock = pSubpFlow.getBBlockForLabel(((LabelDef)lNode).
              getLabel());
            if (lCurrentBBlock == null) {
              ioRoot.msgRecovered.put(5555,
                "Control flow graph not created before data flow analysis."
                + lNode.toString()); //##25
              if (fDbgLevel > 0)
                flowRoot.flow.dbg(2,
                  "Control flow graph not created before data flow analysis."
                  + lNode.toString()); //##25
            }
            break;
            //##25 END
          case HIR.OP_LABELED_STMT:

            // System.out.println("Label");
            //##60 lCurrentBBlock = (BBlockHir)fResults.getBBlockForLabel(
            lCurrentBBlock = pSubpFlow.getBBlockForLabel(
              ((LabeledStmt)lNode).getLabel());

            if (lCurrentBBlock == null) {
              ioRoot.msgRecovered.put(5555,
                "Control flow graph not created before data flow analysis.");
              if (fDbgLevel > 0)
                flowRoot.flow.dbg(2,
                  "Control flow graph not created before data flow analysis."
                  + lNode.toString()); //##25
            }

            break;

          case HIR.OP_LIST:
            break;

          case HIR.OP_CONTENTS:

            if (((lParent = (HIR)lNode.getParent()) != null) &&
                (lParent.getOperator() == HIR.OP_ASSIGN)) {
              //##60 fResults.put("HasPointerAssign", lCurrentBBlock, "True");
              ((SubpFlowImpl)pSubpFlow).
                hasPointerAssign[lCurrentBBlock.getBBlockNumber()] = true;
            }

            break;

          case HIR.OP_ARROW:
          case HIR.OP_ADDR:
            ((SubpFlowImpl)pSubpFlow).
              hasPointerAssign[lCurrentBBlock.getBBlockNumber()] = true;

            break;

          case HIR.OP_QUAL:

            ((SubpFlowImpl)pSubpFlow).
              hasStructUnion[lCurrentBBlock.getBBlockNumber()] = true; //##60

            break;

          case HIR.OP_CALL:

            ((SubpFlowImpl)pSubpFlow).
              hasCall[lCurrentBBlock.getBBlockNumber()] = true; //##60
            ((SubpFlowImpl)pSubpFlow).fSubtreesContainingCall.add(lNode);
            for (HIR lHir = (HIR)lNode.getParent(); lHir != null;
                 lHir = (HIR)lHir.getParent()) {
              ((SubpFlowImpl)pSubpFlow).fSubtreesContainingCall.add(lNode);
            }
        }

        pSubpFlow.setBBlock((HIR)lNode, lCurrentBBlock); //##60
      }
    }

    //##56 BEGIN
    if (pSubpFlow.getRecordAlias() == null) {
      AliasAnal lAliasAnal = new AliasAnalHir2(((SubpFlowImpl)pSubpFlow).
        flowRoot.hirRoot);
      RecordAlias lRecordAlias = new RecordAlias(lAliasAnal,
        pSubpFlow.getSubpDefinition(), pSubpFlow); //##62
      pSubpFlow.setRecordAlias(lRecordAlias);
    }
    //##56 END
  }
}
