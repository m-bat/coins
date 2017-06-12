/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow;

import java.util.ArrayList;
import java.util.List;

import coins.FlowRoot;
import coins.ir.IR;


public class UDChainImpl implements UDChain {
    //====== Fields ======//
    public final FlowRoot flowRoot; // Used to access Root information. //##8
    protected IR fUseNode = null; // IR node using a symbol. 
    protected List fDefList = null; // List of IR nodes defining the symbol. 

    //====== Constructors ======//
    protected UDChainImpl(FlowRoot pFlowRoot, IR pUseNode) //##8
     {
        flowRoot = pFlowRoot; //##8
        fUseNode = pUseNode;
        fDefList = new ArrayList();
    }

    //====== Methods ======//
    public IR getUseNode() {
        return fUseNode;
    }

    public List getDefList() {
        return fDefList;
    }

    public void addDefNode(IR pDefNode) {
        if (!fDefList.contains(pDefNode)) {
            fDefList.add(pDefNode);
        }
    }

    public String toString() {
        IR lUseNode;
        IR lDefNode;
        StringBuffer sb = new StringBuffer();
        sb.append(" use "); //##8
        lUseNode = getUseNode(); //##13 Tan

        sb.append(getUseNode() + "\n"); // 2004.06.01 S.Noishi
	/*
        if ((lUseNode != null) &&
                (lUseNode instanceof coins.ir.lir.AbstractLIRNode)) {
            sb.append(lUseNode + " " +
                ((coins.ir.lir.AbstractLIRNode) lUseNode).getParent() + //##13 Tan
                "[" + ((coins.ir.lir.AbstractLIRNode) lUseNode).getTreeIndex() +
                "]" + "\n"); //##13 Tan
        } else { //##13 Tan
            sb.append(getUseNode() + "\n"); //##13 Tan
        }
	*/

        if (!getDefList().isEmpty()) {
            sb.append("  def "); //##8
        }

        for (java.util.ListIterator lDefListIterator = getDefList()
                                                           .listIterator();
                lDefListIterator.hasNext();) { //## Tan //##9
            lDefNode = (IR) lDefListIterator.next();

            if (lDefListIterator.previousIndex() != 0) {
                sb.append("      "); //##8
            }

            sb.append(lDefNode + "\n"); // 2004.06.01 S.Noishi
            /*
            if ((lDefNode != null) &&
                    (lDefNode instanceof coins.ir.lir.AbstractLIRNode)) {
                sb.append(lDefNode + " " +
                    ((coins.ir.lir.AbstractLIRNode) lDefNode).getParent() + //##13 Tan
                    "[" +
                    ((coins.ir.lir.AbstractLIRNode) lDefNode).getTreeIndex() +
                    "]" + "\n"); //##13 Tan
            } else { //##13 Tan
                sb.append(lDefNode + "\n"); //##13 Tan
            }
            */
        }

        return sb.toString();
    }
}
