/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * FindControlFlowGraph.java
 *
 * Created on June 18, 2002, 1:35 PM
 */
package coins.aflow;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import coins.FlowRoot;
import coins.IoRoot;
import coins.ir.hir.SubpDefinition;
import coins.sym.Label;


/**
 *
 * @author  hasegawa
 */
public abstract class MakeControlFlowGraph // extends FlowAdapter
 {
    SubpFlow fSubpFlow;
    protected FlowResults fResults;
    public final FlowRoot flowRoot;
    public final IoRoot ioRoot;

    /** Creates new MakeControlFlowGraph */
    MakeControlFlowGraph(FlowResults pResults) {
        fResults = pResults;
        flowRoot = fResults.flowRoot;
        ioRoot = flowRoot.ioRoot;
    }

    /**
     * Make a new control flow graph.
     */
    public void find(SubpFlow pSubpFlow) {
        if (!pSubpFlow.results().fCFGInfo.isEmpty()) {
            ioRoot.msgRecovered.put(5555,
                "MakeControlFlowGraph: CFG info already exists. Call FlowResults#clearAll before begining a new analysis.");
        }

        fSubpFlow = pSubpFlow;
        makeControlFlowGraph(pSubpFlow.getSubpDefinition(), pSubpFlow);
    }

    abstract void makeControlFlowGraph(SubpDefinition pSubpDef,
        SubpFlow pSubpFlow);

    BBlock findEntryBlock() {
        Label lStartLabel;
        BBlock lEntryBBlock;
        lStartLabel = fSubpFlow.getSubpSym().getStartLabel();
        lEntryBBlock = (BBlock) fResults.getBBlockForLabel(lStartLabel);
        fSubpFlow.setEntryBBlock(lEntryBBlock);

        //		lEntryBBlock.setFlag(BBlock.IS_ENTRY, true);
        return lEntryBBlock;
    }
     // findEntryBBlock;

    /**
     *
     * Finds the exit BBlock.
     *
     **/
    BBlock findExitBlock() {
        int maxBBlockNo;
        int i;
        BBlock b;
        BBlock lExitBBlock = null;
        List l;

        for (Iterator lIt = fSubpFlow.getReachableBBlocks().iterator();
                lIt.hasNext();) {
            b = (BBlock) lIt.next();
            l = b.getSuccList();

            if (l.size() == 0) {
                //				b.setFlag(b.IS_EXIT,true); //## Tan
                lExitBBlock = b;
            } else {
                //				b.setFlag(b.IS_EXIT,false); //## Tan
            }
        }

        fSubpFlow.setExitBBlock(lExitBBlock);

        return lExitBBlock;
    }

    /**
     *
     * addEdge:
     * Add an edge between the given BBlocks.
     **/
    void addEdge(BBlock ppred, BBlock psucc) {
        ioRoot.dbgFlow.print(6, "addEdge ");

        if (psucc == null) {
            return;
        }

        if (ppred == null) {
            return;
        }

        ioRoot.dbgFlow.print(6,
            "from " + ppred.getBBlockNumber() + " to " +
            psucc.getBBlockNumber());

        psucc.addToPredList(ppred);
        ppred.addToSuccList(psucc);
    }

    /*
     * Deletes edges that are never visited.
     *
     * @param pBBlock the entry BBlock edges that are never visited starting from which are to be deleted.
     */
    void deleteEdge(BBlock pBBlock) {
        int maxBBlockNo;
        BBlockVector mark;
        BBlock Curr;
        BBlock Succ;
        BBlock Pred;
        List l;
        List delList;
        ListIterator Ie;
        int i;

        maxBBlockNo = fSubpFlow.getNumberOfBBlocks();
        mark = new BBlockVectorImpl(fSubpFlow);

        markRootDom(pBBlock, mark);
        delList = new LinkedList();

        for (i = 1; i <= maxBBlockNo; i++) {
            if (mark.getBit(domBitLookUp(i)) == 0) {
                Curr = fSubpFlow.getBBlock(i);

                //
                // Delete Succ List
                //
                l = Curr.getSuccList();
                ListCopy(l, delList);

                for (Ie = delList.listIterator(); Ie.hasNext();) {
                    Succ = (BBlock) Ie.next();
                    Succ.deleteFromPredList(Curr);
                    Curr.deleteFromSuccList(Succ);
                }

                //
                // Delete Pred List
                //
                l = Curr.getPredList();
                ListCopy(l, delList);

                for (Ie = delList.listIterator(); Ie.hasNext();) {
                    Pred = (BBlock) Ie.next();
                    Pred.deleteFromPredList(Curr);
                    Curr.deleteFromPredList(Pred);
                }
            }
        }
    }

    /**
     * Records the BBlocks in the flow in the order suitable for solving data flow equations. BBlocks that are never visited are not included.
     **/
    void recordReachableBBlocks() {
        int i;
        List lBBlockList;
        int lBBlockCount = fSubpFlow.getNumberOfBBlocks();
        lBBlockList = new ArrayList();

        BBlock lBBlock;
        flowRoot.aflow.dbg(1, "recordReachableBBlocks", "BBlockCount " + lBBlockCount); //##25;
        for (i = 1; i <= lBBlockCount; i++) {
            lBBlock = fSubpFlow.getBBlock(i);

            if (!lBBlock.getPredList().isEmpty() || lBBlock.isEntryBBlock()) { // BBlocks that are never visited had their already Edges cut.
                lBBlockList.add(lBBlock);
            }
        }

        fSubpFlow.setReachableBBlocks(lBBlockList);
    }

    /*
     *
     * Flags the BBlocks that are reachable from the entry BBlock.
     * If a BBlock is reachable, the corresponding bit in pmark will be set.
     *
     */
    private void markRootDom(BBlock pBBlock, BBlockVector pmark) {
        BBlock b;
        BBlock edge;
        List l;
        ListIterator Ie;
        l = pBBlock.getSuccList();
        pmark.setBit(domLookUp(pBBlock.getBBlockNumber())); // Set bit

        for (Ie = l.listIterator(); Ie.hasNext();) {
            b = (BBlock) Ie.next();

            if (pmark.getBit(domBitLookUp(b.getBBlockNumber())) == 0) {
                markRootDom(b, pmark);
            }
        }
    }

    private int domLookUp(int ppNo) {
        int lBitPosition = ppNo;

        return lBitPosition;
    }

    /**
     *
     * domBitLookUp:
     * Maps the bit position in BBlockVector to the BBlockNumber.
     **/
    public int domBitLookUp(int pBitPos) {
        int lBlockNo = pBitPos;

        return lBlockNo;
    }

    /*
     * ListCopy:
     * Copies the elements of the first paramter to the second parameter.
     *
     */
    private static void ListCopy(List from, List to) {
        ListIterator Ie;
        to.clear();

        for (Ie = from.listIterator(); Ie.hasNext();) {
            to.add(Ie.next());
        }
    }

    void unifyBBlocks() {
        BBlock lBBlock;
        List lSuccList;
        BBlock lSuccBBlock;
        Set lDeletedBBlocks = new HashSet();

        for (Iterator lIt = fSubpFlow.getBBlockTable().iterator();
                lIt.hasNext();) {
            lBBlock = (BBlock) lIt.next();

            if (!lDeletedBBlocks.contains(lBBlock)) {
                while (((lSuccList = lBBlock.getSuccList()).size() == 1) &&
                        ((lSuccBBlock = (BBlock) lSuccList.get(0)).getPredList()
                              .size() == 1)) {
                    lBBlock.fuseSuccessor(lSuccBBlock);

                    //						fSubpFlow.getReachableBBlocks().remove(lSuccBBlock);
                    lDeletedBBlocks.add(lSuccBBlock);
                }
            }
        }

        //	fSubpFlow.getReachableBBlocks().removeAll(lDeletedBBlocks);
    }
}
