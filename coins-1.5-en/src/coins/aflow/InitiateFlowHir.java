/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow;

import coins.aflow.util.FAList;
import coins.alias.AliasAnal;            //##56
import coins.alias.RecordAlias;          //##56
import coins.alias.alias2.AliasAnalHir2; //##56
import coins.ir.hir.HIR;
import coins.ir.hir.HirIterator;
import coins.ir.hir.LabelDef;       //##25
import coins.ir.hir.LabeledStmt;
import coins.ir.hir.SubpDefinition; //##25
import coins.sym.ExpId;             //##25
import coins.sym.FlowAnalSym;
import coins.sym.Sym;
import coins.sym.SymIterator;


/**
 * HIR data flow initiation class.
 */
public class InitiateFlowHir extends InitiateFlow {
    public InitiateFlowHir(FlowResults pResults) {
        super(pResults);
    }

    /**
     * <p>Initiates data flow analysis for the given SubpFlow.</p>
     *
     * <p>Does the following:
     * <li>Link function calls' argument nodes to the argument list node (holds as a map entry in FlowResults).<li>
     * <li>Flags whether each BBlock
     * <ul><li>has any assignment to the contents of pointers.</li>
     *     <li>ever accesses a pointer variable.</li>
     *     <li>ever accesses a structure/union variable.</li>
     *     <li>ever calls other functions.</li>
     * </ul>
     * <li>Sets a unique index to each node.</li>
     * <li>link nodes to the enclosing BBlock (as a map entry in FlowResults).</li>
     * <li>Sets a unique index to each accessed symbol, and hold a table of such symbols.</li>
     * <li>Assigns FlowExpIds to the pertinent nodes</li>.
     * <li>Do preparation for alias analysis</li>.
     * </ul></p>
     */
    public void initiate(SubpFlow pSubpFlow) {
        AssignFlowExpId lAssigner = pSubpFlow.assigner();

        HIR lNode;
        Sym lSym;
        FlowAnalSym lFlowAnalSym;
        int lIndex = 0;
        int lUsedSymCount = 0;
        BBlock lCurrentBBlock = null;

        //                FAList lFlowIrLinkCells = new FAList();
        FAList lSymIndexTable = new FAList();
        HIR lParent;
        HIR lHirListNode = null;
//##25 BEGIN
        SubpDefinition lSubpDefinition =
            pSubpFlow.getSubpDefinition();
        flowRoot.aflow.dbg(2, "initiateFlowHir.initiate",
            lSubpDefinition.getSubpSym().toString());
        // pSubpFlow.clear();
        pSubpFlow.computeSetOfGlobalVariables();
        pSubpFlow.computeSetOfAddressTakenVariables();
        // Assign FlowExpId to expression nodes.
        lAssigner.assign(); //##25
        // Check existence of pointer/struct/union/call
        // and make ExpNodeList for ExpId.
//##25 END
        for (HirIterator lHirIterator = FlowUtil.hirIterator(
                    pSubpFlow.getSubpDefinition().getHirBody());
                lHirIterator.hasNext();) {
            lNode = (HIR) lHirIterator.next();

            // System.out.println(HIR.OP_CODE_NAME[lNode.getOperator()]);
            if (lNode != null) {
      //##25 BEGIN
      flowRoot.aflow.dbg(5, " initiate ", lNode.toString());
      FlowAnalSym lSym2 = lNode.getSymOrExpId();
      if (lSym2 != null) {
        if (lSym2 instanceof ExpId) {
          if (lCurrentBBlock == null) {
            flowRoot.aflow.dbg(2, " lCurrentBBlock is null for ",
              lNode.toString());
          }else
           ((BBlockHirImpl)lCurrentBBlock).addToExpNodeList((ExpId)lSym2,
              lNode);
        }
      }
      //##25 END
                switch (lNode.getOperator()) {
                  //##25 BEGIN
                case HIR.OP_LABEL_DEF:
                  lCurrentBBlock = (BBlockHir)fResults.getBBlockForLabel(
                    ((LabelDef) lNode).getLabel());
                  if (lCurrentBBlock == null) {
                     ioRoot.msgRecovered.put(5555,
                         "Control flow graph not created before data flow analysis."
                       + lNode.toString()); //##25
                     flowRoot.aflow.dbg(2,
                         "Control flow graph not created before data flow analysis."
                       + lNode.toString()); //##25
                  }
                  break;
                  //##25 END
                case HIR.OP_LABELED_STMT:

                    // System.out.println("Label");
                    lCurrentBBlock = (BBlockHir)fResults.getBBlockForLabel(
                      ((LabeledStmt) lNode).getLabel());

                    if (lCurrentBBlock == null) {
                        ioRoot.msgRecovered.put(5555,
                            "Control flow graph not created before data flow analysis.");
                        flowRoot.aflow.dbg(2,
                            "Control flow graph not created before data flow analysis."
                            + lNode.toString()); //##25
                    }

                    break;

                case HIR.OP_LIST:

//                    for (Iterator lArgIt = ((HirList) lNode).iterator();
//                            lArgIt.hasNext();)
//                        fResults.setHirListForNode((HIR) lArgIt.next(),
//                            (HirList) lNode);

                    break;

                case HIR.OP_CONTENTS:

                    if (((lParent = (HIR) lNode.getParent()) != null) &&
                            (lParent.getOperator() == HIR.OP_ASSIGN)) {
                        fResults.put("HasPointerAssign", lCurrentBBlock, "True");
                    }

                    break;

                case HIR.OP_ARROW:
                case HIR.OP_ADDR:
                    fResults.put("UsePointer", lCurrentBBlock, "True");

                    break;

                case HIR.OP_QUAL:
                    fResults.put("HasStructUnion", lCurrentBBlock, "True");

                    break;

                case HIR.OP_CALL:
                    fResults.put("HasCall", lCurrentBBlock, "True");
                }
                
                // Fukuda
                // ADD -----------------------------------------------
                if (FlowUtil.IsComplexElemNode(lNode) == true)
                        continue;
                HIR parentUseNode =(HIR)((HIR)lNode).getParent();
                boolean complexNode= false;
                if (FlowUtil.IsComplexQUAL((HIR)parentUseNode) == true) {
                    if (FlowUtil.IsComplexNode((HIR)lNode) == false) {
                        continue;
                    }
                    complexNode= true;
                }
                // ADD ----------------------------------------------- 
                //##25 lNode.setIndex(++lIndex); // Set index to node.

                fResults.put("BBlockForNode", lNode, lCurrentBBlock);


                lSym = lNode.getSym();

                if (lSym != null) {
                    if (lSym instanceof FlowAnalSym) {
                        lFlowAnalSym = (FlowAnalSym) lSym;

                        if (!lSymIndexTable.contains(lFlowAnalSym)) {
                            // Fukuda
                            //lFlowAnalSym.setIndex(++lUsedSymCount); // Set index to symbol.
                            //lSymIndexTable.add(lFlowAnalSym);
                           
                             if(FlowUtil.IsVarSyms(lFlowAnalSym) == true || complexNode == true) {
                                lFlowAnalSym.setIndex(++lUsedSymCount); // Set index to symbol.
                                //System.out.println("initiate Function: ADD lSymIndexTable="
                                //                 +lFlowAnalSym.getName());
                                lSymIndexTable.add(lFlowAnalSym);
                             } else {
                                 if (lNode.getChildCount() == 0) {
                                    lFlowAnalSym.setIndex(++lUsedSymCount); // Set index to symbol.
                                 //    System.out.println("initiate Function: ADD lSymIndexTable="
                                 //                +lFlowAnalSym.getName());
                                     lSymIndexTable.add(lFlowAnalSym);
                                 }
                             }

                        }
                    }
                }
                pSubpFlow.setBBlockOfIR(lCurrentBBlock, lNode.getIndex()); //##25
            }
        }

        for (SymIterator lSymIt = symRoot.symTableRoot.getSymIterator();
                lSymIt.hasNext();) {
            lSym = lSymIt.next();

            if (lSym instanceof FlowAnalSym && !lSymIndexTable.contains(lSym)) {
                ((FlowAnalSym) lSym).setIndex(++lUsedSymCount);
                lSymIndexTable.add(lSym);
            }
        }

        fResults.put("SymIndexTable", pSubpFlow, lSymIndexTable);

        //##25 lAssigner.assign();
        //##56 BEGIN
        if (pSubpFlow.getRecordAlias() == null) {
          AliasAnal lAliasAnal = new AliasAnalHir2(hirRoot);
          RecordAlias lRecordAlias = new RecordAlias(lAliasAnal,
            pSubpFlow.getSubpDefinition());
          pSubpFlow.setRecordAlias(lRecordAlias);
        }
        //##56 END
    }
}
