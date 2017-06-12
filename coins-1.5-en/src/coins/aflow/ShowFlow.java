/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import coins.FlowRoot;
import coins.IoRoot;
import coins.SymRoot;
import coins.aflow.util.BitVector;
import coins.aflow.util.FAList;
import coins.aflow.util.FlowError; // 2004.06.01 S.Noishi
import coins.ir.IR;
//import coins.ir.lir.LIRTree; // 2004.05.31 S.Noishi
//import coins.ir.lir.LIRTreeListIterator; // 2004.05.31 S.Noishi
import coins.sym.FlowAnalSym;
import coins.sym.SymTable;
import coins.sym.Var;


//##8 public class ShowFlow extends Root {
//##25 System.out.println() is changed to System.out.print("\n").
public class ShowFlow { //##8

    /**
     * Used to access Root information.
     */
    public final FlowRoot flowRoot; //##8 Used to access Root information.

    /**
     * Used to access Root information.
     */
    public final IoRoot ioRoot; //##8 Used to access Root information.

    /**
     * Used to access Root information.
     */
    public final SymRoot symRoot;
    final FlowResults fResults;

    /**
     * Creates a new instance of ShowFlow.
     */
    public ShowFlow(FlowResults pResults) {
        fResults = pResults;
        flowRoot = pResults.flowRoot;
        ioRoot = flowRoot.ioRoot;
        symRoot = flowRoot.symRoot;
    }

    /**
     * Prints control flow information.
     */
    public void showControlFlow(SubpFlow pSubpFlow) {
        BBlock b;
        BBlock edge;
        IR s; //## Tan
        List l;
        ListIterator Ie;
        Iterator Ib;
        BBlockSubtreeIterator Is;
        int maxBBlockNo;
        int i;
        boolean first;

        //		maxBBlockNo = pSubpFlow.getNumberOfRelevantBBlocks();
        maxBBlockNo = pSubpFlow.getNumberOfBBlocks();

        ioRoot.printOut.print("=====[Basic Block]=====\n"); //##25

        for (i = 1; i <= maxBBlockNo; i++) {
            //
            //  Print Basic Block
            //
            b = pSubpFlow.getBBlock(i);

//            if (!pSubpFlow.getReachableBBlocks().contains(b)) {
//                continue;
//            }

            ioRoot.printOut.print("BlockNO ="); //##8
            ioRoot.printOut.print(b.getBBlockNumber()+ "\n"); //##25

            if (b.isEntryBBlock() == true) {
                ioRoot.printOut.print("(ENTRY BLOCK)\n"); //##25
            }

            if (b.isExitBBlock() == true) {
                ioRoot.printOut.print("(EXIT BLOCK)\n"); //##8
            }

            //
            //  Print Succ List
            //
            ioRoot.printOut.print("	 ==Succ List==>("); //##8
            l = b.getSuccList();

            first = true;

            for (Ie = l.listIterator(); Ie.hasNext();) {
                edge = (BBlock) Ie.next();

                if (first == true) {
                    first = false;
                } else {
                    ioRoot.printOut.print(","); //##8
                }

                ioRoot.printOut.print(edge.getBBlockNumber()); //##8
            }

            ioRoot.printOut.print(")\n"); //##25

            ioRoot.printOut.print("	 ==Pred List==>("); //##8
            l = b.getPredList();
            first = true;

            for (Ie = l.listIterator(); Ie.hasNext();) {
                edge = (BBlock) Ie.next();

                if (first == true) {
                    first = false;
                } else {
                    ioRoot.printOut.print(","); //##8
                }

                ioRoot.printOut.print(edge.getBBlockNumber()); //##8
            }

            ioRoot.printOut.print(")\n"); //##25

            if (flowRoot.isHirAnalysis()) {
                ioRoot.printOut.print("	 ==Stmt List==\n"); //##25
            } else {
                ioRoot.printOut.print("	 ==Instr List==\n"); //##25
            }

            if (b instanceof BBlockHir) {
                BBlockStmtIterator lIt = new BBlockStmtIterator((BBlockHir) b);

                while ((s = (IR) lIt.next()) != BBlockStmtIterator.EOB) {
                    //					s = (IR)lIt.next();  //## Tan
                    ioRoot.printOut.print("	 "); //##8
                    ioRoot.printOut.print(s + "\n"); //##25
                }
            } else {
    throw new FlowError(); // 2004.06.01 S.Noishi
    /*
                for (LIRTreeListIterator lIt = ((BBlockLir) b).getLirTreeList()
                                                .getIterator(); lIt.hasNext();) {
                    s = (IR) lIt.next(); //## Tan
                    ioRoot.printOut.print("        " +
                        ((LIRTree) s).dumpTree()+"\n"); //##25
                }
    */
            }
        }
    }

    /**
     * Prints dominator tree info for the given <code>SubpFlow</code>.
     */
    public void showDominatorTree(SubpFlow pSubpFlow) {
        BBlock lBBlock;
        List lDom;

        ioRoot.printOut.print("=====[ Dominators ]=====\n"); //##25

        for (Iterator lIt = pSubpFlow.getBBlocks().iterator(); lIt.hasNext();) {
            lBBlock = (BBlock) lIt.next();
            lDom = lBBlock.getDomForSubpFlow();
            showDominator(lDom, lBBlock);

            //                        System.out.println(lBBlock + " immediatedom: " + lBBlock.getImmediateDominatorForSubpFlow() + " dom children: " + lBBlock.getDominatedChildrenForSubpFlow());
        }
    }

    /**
     * Prints postdominator tree info for the given <code>SubpFlow</code>.
     */
    public void showPostdominatorTree(SubpFlow pSubpFlow) {
        BBlock lBBlock;
        List lPostdom;

        ioRoot.printOut.print("=====[ Postdominators ]=====\n"); //##25

        for (Iterator lIt = pSubpFlow.getBBlocks().iterator(); lIt.hasNext();) {
            lBBlock = (BBlock) lIt.next();
            lPostdom = lBBlock.getPostdomForSubpFlow();
            showPostdominator(lPostdom, lBBlock);
        }
    }

    void showDominator(List pDom, BBlock pBBlock) {
        ioRoot.printOut.print(pBBlock+ "\n"); //##25
        ioRoot.printOut.print(" " + pDom + "\n"); //##25
    }

    void showPostdominator(List pPostdom, BBlock pBBlock) {
        showDominator(pPostdom, pBBlock);
    }

    /**
     * Prints dominator tree info for the given <code>SubpFlow</code> (immediate dominators only).
     */
    public void showImmediateDominators(SubpFlow pSubpFlow) {
        BBlock lBBlock;
        ioRoot.printOut.print("=====[ Immediate dominators ]=====\n"); //##25

        for (Iterator lIt = pSubpFlow.getReachableBBlocks().iterator();
                lIt.hasNext();) {
            lBBlock = (BBlock) lIt.next();
            ioRoot.printOut.print(lBBlock + ": ");
            ioRoot.printOut.print(lBBlock.getImmediateDominatorForSubpFlow()+"\n"); //##25
        }
    }

    /**
     * Prints postdominator tree info for the given <code>SubpFlow</code> (immediate postdominators only).
     */
    public void showImmediatePostdominators(SubpFlow pSubpFlow) {
        BBlock lBBlock;
        ioRoot.printOut.print("=====[ Immediate postdominators ]=====\n"); //##25

        for (Iterator lIt = pSubpFlow.getReachableBBlocks().iterator();
                lIt.hasNext();) {
            lBBlock = (BBlock) lIt.next();
            ioRoot.printOut.print(lBBlock + ": ");
            ioRoot.printOut.print(lBBlock.
              getImmediatePostdominatorForSubpFlow() + "\n"); //##25
        }
    }

    /**
     *
     * showVector(BitVector, String)
     *
     */
    public void showVector(BitVector pBitVector, String pComment) {
        ioRoot.printOut.print(pComment+"\n"); //##25
        pBitVector.toString();
        ioRoot.printOut.print(pBitVector+"\n"); //##25
    }

//    /**
//    * Prints the DefUseList for all the FlowAnalSyms accessed in the given SubpFlow.
//    */
//    public void showDDefUse(SubpFlow pSubpFlow) {
//        final SymTable lSymTableCurrent = symRoot.symTableCurrent;
//        SymTable lSymTable;
//
//        FlowAnalSym lFlowAnalSym;
//        ioRoot.printOut.println("=====[DDefUse]====="); //##8
//
//        for (Iterator lIt = ((FAList) fResults.get("SymIndexTable", pSubpFlow)).iterator();
//                lIt.hasNext();) {
//            lFlowAnalSym = (FlowAnalSym) lIt.next();
//
//            if (lFlowAnalSym instanceof Var) {
//                ioRoot.printOut.println(lFlowAnalSym);
//                ioRoot.printOut.println(fResults.get("DDefUseList",
//                        lFlowAnalSym, pSubpFlow));
//            }
//        }
//    }

    /**
    * Prints the UDList for all the FlowAnalSyms accessed in the given SubpFlow.
    */
    public void showDUseDef(SubpFlow pSubpFlow) {
        final SymTable lSymTableCurrent = symRoot.symTableCurrent;
        SymTable lSymTable;

        FlowAnalSym lFlowAnalSym;
        ioRoot.printOut.print("=====[DUseDef]=====\n"); //##25

        for (Iterator lIt = ((FAList) fResults.get("SymIndexTable", pSubpFlow)).iterator();
                lIt.hasNext();) {
            lFlowAnalSym = (FlowAnalSym) lIt.next();

            if (lFlowAnalSym instanceof Var) {
                ioRoot.printOut.print(lFlowAnalSym+"\n"); //##25
                ioRoot.printOut.print(fResults.get("DUDList", //##25
                   lFlowAnalSym, pSubpFlow)+"\n"); //##25
            }
        }
    }

    public void showPDefUse(SubpFlow pSubpFlow) {
        final SymTable lSymTableCurrent = symRoot.symTableCurrent;
        SymTable lSymTable;

        FlowAnalSym lFlowAnalSym;
        ioRoot.printOut.print("=====[DefUse]=====\n"); //##25

        for (Iterator lIt = ((FAList) fResults.get("SymIndexTable", pSubpFlow)).iterator();
                lIt.hasNext();) {
            lFlowAnalSym = (FlowAnalSym) lIt.next();

            if (lFlowAnalSym instanceof Var) {
                ioRoot.printOut.print(lFlowAnalSym+"\n"); //##25

                Object o = fResults.get("DefUseList", lFlowAnalSym, pSubpFlow);
                ioRoot.printOut.print(fResults.get("DefUseList",
                        lFlowAnalSym, pSubpFlow)+"\n"); //##25
            }
        }
    }

    public void showPUseDef(SubpFlow pSubpFlow) {
        final SymTable lSymTableCurrent = symRoot.symTableCurrent;
        SymTable lSymTable;

        FlowAnalSym lFlowAnalSym;
        ioRoot.printOut.print("=====[UseDef]=====\n"); //##25

        for (Iterator lIt = ((FAList) fResults.get("SymIndexTable", pSubpFlow)).iterator();
                lIt.hasNext();) {
            lFlowAnalSym = (FlowAnalSym) lIt.next();

            if (lFlowAnalSym instanceof Var) {
                ioRoot.printOut.print(lFlowAnalSym+"\n"); //##25
                ioRoot.printOut.print(fResults.get("UDList",  //##25
                    lFlowAnalSym, pSubpFlow)+"\n"); //##25
            }
        }
    }

    /**
     * A more descriptive version than showVector(BitVector, String)
     */
    public void showVectorByName(BitVector pVect, String pComment) {
        ioRoot.printOut.print("(" + pComment + ")\n"); //##25
        if (pVect == null) { //##70
          ioRoot.printOut.print("  is null\n"); //##70
        }else { //##70
          ioRoot.printOut.print(pVect.toStringDescriptive() + "\n"); //##25
        }
    }

    /**
     * A more descriptive version than showVector(String, SubpFlow)
     */
    public void showVectorsByName(String pAnalName, SubpFlow pSubpFlow) {
        BBlock b;
        BitVector lVect;

        //## ListIterator  Ie; //## Tan //##9
        java.util.ListIterator Ie; //## Tan //##9
        int i;
        ioRoot.printOut.print("=====[" + pAnalName + "(B)]=====\n"); //##25

        for (Ie = pSubpFlow.getBBlocksFromEntry().listIterator(); Ie.hasNext();) {
            b = (BBlock) Ie.next();
            lVect = (BitVector) fResults.get(pAnalName, b);
            showVectorByName(lVect, "BBlock " + b.getBBlockNumber());
        }
    }

    public void showVectorsByName(String pAnalName, BBlock pBBlock) {
        SetRefRepr lSetRefRepr;
        BitVector lBitVect;

        ioRoot.printOut.print("====[" + pAnalName + " for SetRefReprs in "
            + pBBlock + "]====\n"); //##25
        //##41 BEGIN
        if (! fResults.containsKey(pAnalName)) {
          ioRoot.printOut.print(" showVectorsByName parameter error "
              + pAnalName + " not registered\n"); //##41
          return;
        }
        //##41 END
        for (Iterator lIt = pBBlock.getSetRefReprs().iterator(); lIt.hasNext();) {
            lSetRefRepr = (SetRefRepr) lIt.next();
            //##41 BEGIN
            if (! fResults.containsKey(pAnalName, lSetRefRepr)) {
              ioRoot.printOut.print(" showVectorsByName parameter error "
                 + pAnalName + " not registered\n"); //##41
              break;
            }
            //##41 END
            lBitVect = (BitVector) fResults.get(pAnalName, lSetRefRepr);
            showVectorByName(lBitVect, lSetRefRepr.toString());
        }
    }
}
