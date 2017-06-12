/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import coins.FlowRoot;
import coins.SymRoot;
import coins.ir.IR;
import coins.sym.FlagBox;
import coins.sym.FlagBoxImpl;
import coins.sym.FlowAnalSym;
import coins.sym.Sym;


//##10 : Modified on Dec. 2001. by Tan

/**
 * Implementation of the SetRefRepr interface
 *
 */
public abstract class SetRefReprImpl implements SetRefRepr //##8
 {
    public final FlowRoot flowRoot; // Used to access Root information. //##8
    public final SymRoot symRoot;
    protected IR fIR; //##11
    protected List fUseNodeList; //##11
    protected List fUseExpIdList; //##11

    //        protected boolean fSets = false; //##11
    protected int fOpCode; //##11
    FlowResults fResults;
    protected FlagBox fFlags = new FlagBoxImpl();
    protected BBlock fBBlock;
    /** Symbol or ExpId defined by this SetRefRepr. */ //##25
    protected FlowAnalSym fDefSym; //##25

    /** SetRefReprImpl //##25-1
     * Make an instance of SetRefRepr for the subtree pIR
     * in the basic block pBBlock.
     * @param pIR: IR subtree.
     * @param pBBlock: Basic block
     */
    protected SetRefReprImpl(IR pIR, BBlock pBBlock) {
        fIR = pIR;
        fBBlock = pBBlock;
        fResults = pBBlock.results();
        flowRoot = fResults.flowRoot;
        symRoot = flowRoot.symRoot;
        flowRoot.aflow.dbg(4, "SetRefReprImpl",
                           " IR " + pIR.toString()); //##25
    }

    public IR getIR() {
        return fIR;
    }

    public java.util.Set useSyms() {
        Sym lSym;
        java.util.Set lUseFlowAnalSyms = new HashSet();

        for (Iterator lIt = useNodeIterator(); lIt.hasNext();) {
            lSym = ((IR) lIt.next()).getSym();

            if (lSym instanceof FlowAnalSym) {
                lUseFlowAnalSyms.add(lSym);
            }
        }

        //		lUseFlowAnalSyms.removeAll(getModSyms());
        return lUseFlowAnalSyms;
    }

    public IR topUseNode() {
        if (sets()) {
            return fIR.getChild2();
        } else {
            return fIR;
        }
    }

    public Set getFlowExpIds() {
        return (Set) fResults.get("FlowExpIdsForSetRefRepr", this);
    }

    public Set getUseFlowExpIds() {
        return (Set) fResults.get("UseFlowExpIdsForSetRefRepr", this);
    }

    public FlowExpId defFlowExpId() {
        return fResults.getFlowExpIdForNode(defNode());
    }

    /*public Sym getDefSym()
    {
            Sym lElem;
            IR lDefNode = defNode();
            //                if (lDefNode.getOperator() == HIR.OP_QUAL)
            //                      return ((QualifiedExpImpl)lDefNode).getQualifiedElem();
            if (lDefNode != null)
                    return lDefNode.getSym();
            return null;
    }
     */

    public FlowAnalSym getDefSym()  //##25
    {
      return fDefSym;
    }

    public String toString() {
      //##25  return "SetRefRepr " + fIR;
        return "SetRefRepr " + fIR.toString(); //##25
      }

    public NodeIterator nodeIterator() {
        return FlowUtil.nodeIterator(fIR);
    }

    public NodeListIterator nodeListIterator() {
        return FlowUtil.nodeListIterator(fIR);
    }

    public NodeListIterator nodeListIterator(boolean pFromTop, boolean pFromLeft) {
        return FlowUtil.nodeListIterator(fIR, pFromTop, pFromLeft);
    }

    public Iterator useNodeIterator() {
        return useNodeList().iterator();
    }

    /**
     * Returns the Set of nodes that have FlowAnalSym attached and are not a Def node.
     *
     * Returns the Set of nodes that have FlowAnalSym attached and are not a Def node.
     * @see #defNode()
     */

    //      abstract public java.util.Set getUseNodes();
    public Iterator expIterator() {
        return exps().iterator();
    }

    public ListIterator expListIterator() {
        return exps().listIterator();
    }

    public ListIterator expListIteratorFromBottom() {
        List lExps = exps();

        return lExps.listIterator(lExps.size());
    }

    abstract public List exps();

    //        abstract List getUseNodeList(boolean pFromTop, boolean pFromLeft);
    abstract List exps(boolean pFromTop, boolean pFromLeft);

    public ListIterator expListIterator(boolean pFromTop, boolean pFromLeft) {
        return exps(pFromTop, pFromLeft).listIterator();
    }

    public boolean getFlag(int pFlagNumber) {
        return fFlags.getFlag(pFlagNumber);
    }

    public void setFlag(int pFlagNumber, boolean pYesNo) {
        fFlags.setFlag(pFlagNumber, pYesNo);
    }

    public boolean allFalse() {
        return fFlags.allFalse();
    }

    public boolean sets() {
        return fFlags.getFlag(SETS);
    }

    public boolean hasControl() {
        return fFlags.getFlag(HAS_CONTROL);
    }

    public boolean isReturn() {
        return fFlags.getFlag(IS_RETURN);
    }

    public DefVector getDKill() {
        return (DefVector) fResults.get("DKill", this);
    }

    public DefVector getPReach() {
        return (DefVector) fResults.get("PReach", this);
    }

    public BBlock getBBlock() {
        return fBBlock;
    }

    public DefVector getDReach() {
        return (DefVector) fResults.get("DReach", this);
    }

    public DefVector getPKill() {
        return (DefVector) fResults.get("PKill", this);
    }
//
//    /** modSyms0() - global syms not appearing in the Subp that this SetRefRepr belongs to.
//     */
//    public Set modSyms00() {
//        Set lModSyms0 = modSyms0();
//        lModSyms0.retainAll(fBBlock.getSubpFlow().getSymIndexTable());
//
//        return lModSyms0;
//    }

    public ExpVector getDAvailIn() {
        return (ExpVector) fResults.get("DAvailIn", this);
    }

    public ExpVector getPEKill() {
        return (ExpVector) fResults.get("PEKill", this);
    }

    public FlowAnalSymVector getDDefIn() {
        return (FlowAnalSymVector) fResults.get("DDefIn", this);
    }

    public FlowAnalSymVector getDDefined() {
        return (FlowAnalSymVector) fResults.get("DDefined", this);
    }

    public FlowAnalSymVector getPDefined() {
        return (FlowAnalSymVector) fResults.get("PDefined", this);
    }

    public FlowAnalSymVector getDExposed() {
        return (FlowAnalSymVector) fResults.get("DExposed", this);
    }

    public FlowAnalSymVector getDUsed() {
        return (FlowAnalSymVector) fResults.get("DUsed", this);
    }

    public FlowAnalSymVector getPExposed() {
        return (FlowAnalSymVector) fResults.get("PExposed", this);
    }

    public FlowAnalSymVector getPLiveOut() {
        return (FlowAnalSymVector) fResults.get("PLiveOut", this);
    }

    public FlowAnalSymVector getPUsed() {
        return (FlowAnalSymVector) fResults.get("PUsed", this);
    }

//    abstract Set modSyms0();

}
