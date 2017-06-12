/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow;

import java.util.ArrayList;
import java.util.List;

import coins.FlowRoot;
import coins.ir.IR;


/** DefUseCellImpl class:
 *
 *  Def-use list cell representaing
 *  a definition and list of its use points.
 **/
public class DefUseCellImpl implements DefUseCell //##8
 {
    //====== Fields ======//
    public final FlowRoot flowRoot; // Used to access Root information. //##8
    IR fDefNode = null; // IR node defining a symbol.

    //	IrList
    List fUseList = null; // List of IR nodes using the symbol.

    //====== Constructors ======//
    protected DefUseCellImpl(FlowRoot pFlowRoot, IR pDefNode) //##8
     {
        flowRoot = pFlowRoot; //##8
        fDefNode = pDefNode;
        fUseList = new ArrayList();
    }

    //====== Methods ======//
    public IR getDefNode() {
        return fDefNode;
    }

    public List getUseList() {
        return fUseList;
    }

    public void addUseNode(IR pUseNode) {
        if (!fUseList.contains(pUseNode)) {
            fUseList.add(pUseNode);
        }
    }

    public String toString() {
        IR lDefNode;
        IR lUseNode;
        StringBuffer sb = new StringBuffer();
        sb.append(" def "); //##8
        lDefNode = getDefNode(); //##13 Tan

        sb.append(getDefNode() + "\n"); // 2004.05.31 S.Noishi
        /*
        if ((lDefNode != null) &&
                (lDefNode instanceof coins.ir.lir.AbstractLIRNode)) {
            sb.append(lDefNode + //##13 Tan
                "[" + ((coins.ir.lir.AbstractLIRNode) lDefNode).getTreeIndex() +
                "]" + "\n"); //##13 Tan
        } else { //##13 Tan
            sb.append(getDefNode() + "\n"); //##13 Tan
        }
	*/

        if (!getUseList().isEmpty()) {
            sb.append("  use "); //##8
        }

        for (java.util.ListIterator lUseListIterator = getUseList()
                                                           .listIterator();
                lUseListIterator.hasNext();) { //## Tan //##9
            lUseNode = (IR) lUseListIterator.next();

            if (lUseListIterator.previousIndex() != 0) {
                sb.append("      "); //##8
            }

            sb.append(lUseNode + "\n"); // 2004.06.01 S.Noishi
            /*
            if ((lUseNode != null) &&
                    (lUseNode instanceof coins.ir.lir.AbstractLIRNode)) {
                sb.append(lUseNode + " " +
                    ((coins.ir.lir.AbstractLIRNode) lUseNode).getParent() + //##13 Tan
                    "[" +
                    ((coins.ir.lir.AbstractLIRNode) lUseNode).getTreeIndex() +
                    "]" + "\n"); //##13 Tan
            } else { //##13 Tan
                sb.append(lUseNode + "\n"); //##13 Tan
            }
            */
        }

        return sb.toString();
    }
}
 // DefUseCellImpl class
