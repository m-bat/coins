/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import coins.FlowRoot;
import coins.aflow.util.FlowError;
import coins.ir.IR;


/** DefUseListImpl class:
 *  Def-use list representaing list of definitions of a symbol
 *  where each definition has list of its use points.
 *  A DefUseList will be associated with a FlowAnalSym **/
public class DefUseListImpl implements DefUseList //##8
 {
    //====== Fields ======//
    public final FlowRoot flowRoot; // Used to access Root information. //##8
    protected List fDefUseCellList; // List of DefUseCells.
    protected HashMap fHashCell;     // Hash. // I.Fukuda

    DefUseListImpl(FlowRoot pFlowRoot) //##8
     {
        flowRoot = pFlowRoot; //##8
        fDefUseCellList = new ArrayList(); //##8
        fHashCell = new HashMap(); //##8
    }

    //====== Methods ======//
    public DefUseCell addDefUseCell(IR pDefNode) {
        DefUseCell lDefUseCell = (DefUseCell) (new DefUseCellImpl(flowRoot,
                pDefNode)); //##8
        fHashCell.put(pDefNode,lDefUseCell); // Fukuda
        fDefUseCellList.add(lDefUseCell);

        return lDefUseCell;
    }
     // addDefUseCell

    /** getDefUseCell:
     *  Get DefUseCell having pDefNode as its definie node.
     **/
    public DefUseCell getDefUseCell(IR pDefNode) {
        DefUseCell lCell = getDefUseCellRaw(pDefNode);
         

        if (lCell == null) {
            throw new FlowError("DefUseCell corresponding to the Def node " +
                pDefNode + " not found.");
        }

        return lCell;
    }
     // getDefUseCell

    DefUseCell getOrAddDefUseCell(IR pDefNode) {
        DefUseCell lDefUseCell;
        // 
        //if ((lDefUseCell = getDefUseCellRaw(pDefNode)) == null) {
        //    return addDefUseCell(pDefNode);
        //}
        //return lDefUseCell;
        if (fHashCell.containsKey(pDefNode) == true)
            return getDefUseCellRaw(pDefNode);
        else
            return addDefUseCell(pDefNode);

    }

    public DefUseCell getDefUseCellRaw(IR pDefNode) {
        DefUseCell lDefUseCell = null;
        DefUseCell lCell;
        /*
        for (Iterator lIterator = fDefUseCellList.iterator();
                lIterator.hasNext();) {
            lCell = (DefUseCell) (lIterator.next());

            if (lCell.getDefNode() == pDefNode) {
                lDefUseCell = lCell;

                break;
            }
        }
        */
        lDefUseCell = (DefUseCell) fHashCell.get(pDefNode);
        return lDefUseCell;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        DefUseCell lDefUseCell;

        for (java.util.Iterator lDefUseCellListIterator = fDefUseCellList.iterator();
                lDefUseCellListIterator.hasNext();) {
            lDefUseCell = (DefUseCell) lDefUseCellListIterator.next();
            sb.append(lDefUseCell.toString());
        }

        return sb.toString();
    }

    public List getDefUseCells() {
        return fDefUseCellList;
    }
}
 // DefUseListImpl class
