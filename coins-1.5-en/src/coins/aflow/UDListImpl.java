/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import coins.FlowRoot;
import coins.aflow.util.FlowError;
import coins.ir.IR;


public class UDListImpl implements UDList {
    //====== Fields ======//
    public final FlowRoot flowRoot; // Used to access Root information. //##8
    protected List fUDChainList; // List of UDChains.

    protected UDListImpl(FlowRoot pFlowRoot) //##8
     {
        flowRoot = pFlowRoot; //##8
        fUDChainList = new ArrayList(); //##8
    }

    //====== Methods ======//
    public UDChain addUDChain(IR pUseNode) {
        UDChain lUDChain = (UDChain) (new UDChainImpl(flowRoot, pUseNode)); //##8
        fUDChainList.add(lUDChain);

        return lUDChain;
    }
     // addUDChain

    /** getUDChain:
     *  Get UDChain having pUseNode as its definie node.
     **/
    public UDChain getUDChain(IR pUseNode) {
        UDChain lUDChain = getUDChainRaw(pUseNode);

        if (lUDChain == null) {
            throw new FlowError("UDChain corresponding to the Use node " +
                pUseNode + " not found.");
        }

        return lUDChain;
    }

    UDChain getOrAddUDChain(IR pUseNode) {
        UDChain lUDChain;

        if ((lUDChain = getUDChainRaw(pUseNode)) == null) {
            return addUDChain(pUseNode);
        }

        return lUDChain;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        UDChain lUDChain;

        for (java.util.Iterator lUDChainListIterator = fUDChainList.iterator();
                lUDChainListIterator.hasNext();) {
            lUDChain = (UDChain) lUDChainListIterator.next();
            sb.append(lUDChain.toString());
        }

        return sb.toString();
    }

    public List getUDChains() {
        return fUDChainList;
    }

    public UDChain getUDChainRaw(IR pUseNode) {
        UDChain lUDChain = null;
        UDChain lChain;

        for (Iterator lIterator = fUDChainList.iterator(); lIterator.hasNext();) {
            lChain = (UDChain) (lIterator.next());

            if (lChain.getUseNode() == pUseNode) {
                lUDChain = lChain;

                break;
            }
        }

        return lUDChain;
    }
}
