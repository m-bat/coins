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
import coins.ir.hir.HIR; //##25

//##25: Extention of SetRefReprImpl for extended optimization.

/**
 * Implementation of the SetRefRepr interface
 */
public abstract class
SetRefReprEImpl extends SetRefReprImpl implements SetRefRepr //##8
 {
/* //##25 BEGIN
  public final FlowRoot flowRoot; // Used to access Root information. //##8
  public final SymRoot symRoot;
  protected IR fIR; //##11
  protected List fUseNodeList; //##11
  protected List fUseExpIdList; //##11

  //    protected boolean fSets = false; //##11
  protected int fOpCode; //##11
  FlowResults fResults;
  protected FlagBox fFlags = new FlagBoxImpl();
  protected BBlock fBBlock;
 */ //##25 END

protected
SetRefReprEImpl(IR pIR, BBlock pBBlock) {
  super(pIR, pBBlock);
/*  //##25 BEGIN
    fIR = pIR;
    fBBlock = pBBlock;
    fResults = pBBlock.results();
    flowRoot = fResults.flowRoot;
    symRoot = flowRoot.symRoot;
 */ //##25 END
}

/*  //##25 BEGIN
  public IR getIR() {
    return fIR;
  }
 */ //##25 END

public java.util.Set
useSyms()
{
    Sym lSym;
    java.util.Set lUseFlowAnalSyms = new HashSet();

    if (fIR instanceof HIR) {
      for (Iterator lIt = useNodeIterator(); lIt.hasNext();) {
        lSym = ((HIR) lIt.next()).getSymOrExpId();
        if (lSym instanceof FlowAnalSym) {
          lUseFlowAnalSyms.add(lSym);
        }
      }
    }else {
      for (Iterator lIt = useNodeIterator(); lIt.hasNext();) {
        lSym = ((IR) lIt.next()).getSym();
        if (lSym instanceof FlowAnalSym) {
          lUseFlowAnalSyms.add(lSym);
        }
      }
    }
    //    lUseFlowAnalSyms.removeAll(getModSyms());
    return lUseFlowAnalSyms;
} // useSyms

/*  //##25 BEGIN
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
 */ //##25 END

  /*public Sym getDefSym()
  {
      Sym lElem;
      IR lDefNode = defNode();
      //        if (lDefNode.getOperator() == HIR.OP_QUAL)
      //            return ((QualifiedExpImpl)lDefNode).getQualifiedElem();
      if (lDefNode != null)
          return lDefNode.getSym();
      return null;
  }
   */
/*  //##25 BEGIN
  public String toString() {
    return "SetRefRepr " + fIR;
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
 */ //##25 END

  /**
   * Returns the Set of nodes that have FlowAnalSym attached and are not a Def node.
   *
   * Returns the Set of nodes that have FlowAnalSym attached and are not a Def node.
   * @see #defNode()
   */

/*  //##25 BEGIN
  //    abstract public java.util.Set getUseNodes();
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

  //    abstract List getUseNodeList(boolean pFromTop, boolean pFromLeft);
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
 */  //##25 END

  /** modSyms0() - global syms not appearing in the Subp that this SetRefRepr belongs to.
   */
/*  //##25 BEGIN
  public Set modSyms00() {
    Set lModSyms0 = modSyms0();
    lModSyms0.retainAll(fBBlock.getSubpFlow().getSymIndexTable());

    return lModSyms0;
  }

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
 */  //##25 END

//  public List callNodes()
//  {
//  }

}
