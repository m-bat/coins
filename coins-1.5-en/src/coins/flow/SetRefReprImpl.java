/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
//##60 package coins.aflow;
package coins.flow; //##60

import java.util.ArrayList; //##70
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import coins.FlowRoot;
import coins.SymRoot;
import coins.ir.IR;
import coins.ir.hir.HIR; //##60
import coins.sym.ExpId; //##60
import coins.sym.FlagBox;
import coins.sym.FlagBoxImpl;
import coins.sym.FlowAnalSym;
import coins.sym.Sym;

//##10 : Modified on Dec. 2001. by Tan

/**
 * Implementation of the SetRefRepr interface
 *
 */
public abstract class SetRefReprImpl
  implements SetRefRepr //##8
{
  public final FlowRoot flowRoot; // Used to access Root information. //##8
  public final SymRoot symRoot;
  public final SubpFlow fSubpFlow; //##62
  public final Flow flow; //##62
  protected IR fIR; //##11
    // fIR usually refers to the top subtree corresponding to this SetRefREpr,
    // but in case of IfStmt it refers to if-condition expression,
    // in case of SwitchStmt, it refers to selection-expression.
  protected List fUseNodeList = null; //##60
  //##60 protected List fUseExpIdList; //##11
    // fUseNodeList contains the nodes that have FlowAnalSym.

  //  protected boolean fSets = false; //##11
  protected int fOpCode; //##11
  //##60 FlowResults fResults;
  protected FlagBox fFlags = new FlagBoxImpl();
  protected BBlock fBBlock; // BBlock corresponding to this SetRefRepr.
  /** Symbol or ExpId defined by this SetRefRepr. */
  //##25
  protected FlowAnalSym fDefSym = null; //##25
    // fDefSym is source symbol or ExpId defined by subtree
    // corresponding to this SetRefRepr.

//##60 BEGIN
  //##60 protected java.util.Set fExpIds = new HashSet();
  //##60 protected java.util.Set fUseExpIds = new HashSet();

  /** fModSyms contains Symbols that may be modified by the node
   * corresponding to this.
   * ExpId's for intermediate results are excluded but ExpId
   * for compound variable in LHS of AssignStmt is included.  */
  protected Set
    fModSyms = new HashSet();

  /** fModSymsStmt contains Symbols that may be modified by the simple
   * statement containing the subtree corresponding to this
   * SetRefRepr. It is allocated for each simple statement and
   * refered from all subexpressions in the simple statement. */
  protected Set
    fModSymsStmt = null;

  /** fModSYms00 contains symbols in fModSyms and symbols
   * that may be modified by subprogram is hasCall() is true
   * for this SetRefRepr. (It is computed only when
   * modSyms00() is called at the first time. */
  protected java.util.Set fModSyms00 = null; //##60

  /** fLeafOperands contains symbols of subexpressions used as operands
   *  by the subtree corresponding to this.
   *  ExpId's for intermediate results are excluded. */
  protected Set
    fLeafOperands = new HashSet();

  /** fOperandExp contains ExpId's of subexpressions used as direct operands
   * by the subtree corresponding to this.
   * Leaf operands are excluded. */
  protected Set
    fOperandExp = new HashSet();

  /** fAllSubexps contains ExpId's of all subexpressions under the node
   * corresponding to this.
   * Leaf operands are excluded. */
  protected Set
    fAllSubexps = new HashSet();

  /** fCallIncluded is True if call with side effect is included
   *    in the subtree fIR,
   *  false otherwize;  */
  protected boolean
    //##71 fCallIncluded = false;
    fCallWithSideEffectIncluded = false; //##71

  protected java.util.Set fLhsSyms = null; //##60
  protected java.util.Set fUseSyms = null; //##60
  protected java.util.Set fUseFlowAnalSyms = null; //##60
  protected java.util.Set fExpIdSet = null; //##60
  protected java.util.List fExps = null; //##60
  protected java.util.List fCallNodes = null; //##60
  protected java.util.List fUseSymList = null; //##70

  //##60 END

  protected ExpId fDefExpId;
  protected ExpId fCorrespondingExpId; //##62
  public final int
    fDbgLevel;
//##60 END

//##62 BEGIN
  public SetRefReprImpl(IR pIR, SubpFlow pSubpFlow, ExpId pExpId)
  {
    fSubpFlow = pSubpFlow;
    fIR = pIR;
    flowRoot = pSubpFlow.getFlowRoot();
    flow = flowRoot.flow; //##62
    symRoot = flowRoot.symRoot;
    fDbgLevel = flowRoot.ioRoot.dbgFlow.getLevel();
    fCorrespondingExpId = pExpId;
    fBBlock = pSubpFlow.getBBlock((HIR)pIR); //##62
    if (fDbgLevel > 3) {
      flowRoot.flow.dbg(4, "SetRefReprImpl", pIR.toStringShort());
      if (pExpId != null)
        flowRoot.flow.dbg(4, pExpId.getName()); //##62
    }
  } // SetRefReprImpl

//##62 END
  public IR getIR()
  {
    return fIR;
  }

  public java.util.Set useSyms()
  {
    if (fUseSyms != null) //##60
      return fUseSyms; //##60
    Sym lSym;
    java.util.Set lUseFlowAnalSyms = new HashSet();
/* //##70
    for (Iterator lIt = useNodeIterator(); lIt.hasNext(); ) {
      lSym = ((IR)lIt.next()).getSym();

      if (lSym instanceof FlowAnalSym) {
        lUseFlowAnalSyms.add(lSym);
      }
    }
 */ //##70
    List lUseSymList = useSymList();      //##70
    lUseFlowAnalSyms.addAll(lUseSymList); //##70
    //		lUseFlowAnalSyms.removeAll(getModSyms());
    fUseSyms = lUseFlowAnalSyms; //##60
    if (fDbgLevel >= 4)          //##97
    	flow.dbg(4, "useSyms of " + ((HIR)fIR).toStringShort(), //##97
    			fUseSyms.toString());  //##97
    return lUseFlowAnalSyms;
  }

//##70 BEGIN
  public List useSymList()
  {
    if (fUseSymList != null)
      return fUseSymList;
    Sym lSym;
    List lUseFlowAnalSymList = new ArrayList();
    for (Iterator lIt = useNodeIterator(); lIt.hasNext(); ) {
      lSym = ((IR)lIt.next()).getSym();
      if (lSym instanceof FlowAnalSym) {
        lUseFlowAnalSymList.add(lSym);
      }
    }
    fUseSymList = lUseFlowAnalSymList;
    return fUseSymList;
  } // useSymList
//##70 END
  public Set getUseFlowAnalSyms() //##60;
  {
    //##60 return useSyms();
    if (fUseFlowAnalSyms == null) { //##60
      //##97 fUseFlowAnalSyms = useSyms(); //##60
      // Should be copied because fUseFlowAnalSyms may be modified in findExposed //##97
      fUseFlowAnalSyms = new HashSet();   //##97
      fUseFlowAnalSyms.addAll(useSyms()); //##97
    }
    return fUseFlowAnalSyms;
  }

  public IR topUseNode()
  {
    if (sets()) {
      return fIR.getChild2();
    }
    else {
      return fIR;
    }
  }

  //##60 public FlowExpId defFlowExpId()
  public ExpId defExpId() //##60
  {
    //##60 return fResults.getFlowExpIdForNode(defNode());
    return fDefExpId; //##60
  }

  public FlowAnalSym getDefSym() //##25
  {
    return fDefSym;
  }

  public FlowAnalSym getDefFlowAnalSym() //##60
  {
    return fDefSym;
  }

  public String toString()
  {
    //##25  return "SetRefRepr " + fIR;
    return "SetRefRepr " + fIR.toString(); //##25
  }

   /**
   * Returns the Set of nodes that have FlowAnalSym attached and are not a Def node.
   *
   * Returns the Set of nodes that have FlowAnalSym attached and are not a Def node.
   * @see #defNode()
   */

  //    abstract public java.util.Set getUseNodes();
  public Iterator expIterator()
  {
    return exps().iterator();
  }

  public ListIterator expListIterator()
  {
    return exps().listIterator();
  }

  public ListIterator expListIteratorFromBottom()
  {
    List lExps = exps();

    return lExps.listIterator(lExps.size());
  }

  /**
     * Get the list of expressions (that have ExpId) under the associated subtree.
   * defNode() expression is excluded.
   * @return the list of expressions
   */
  abstract public List exps();

  //  abstract List getUseNodeList(boolean pFromTop, boolean pFromLeft);
  abstract List exps(boolean pFromTop, boolean pFromLeft);

  public ListIterator expListIterator(boolean pFromTop, boolean pFromLeft)
  {
    return exps(pFromTop, pFromLeft).listIterator();
  }

  public boolean getFlag(int pFlagNumber)
  {
    return fFlags.getFlag(pFlagNumber);
  }

  public void setFlag(int pFlagNumber, boolean pYesNo)
  {
    fFlags.setFlag(pFlagNumber, pYesNo);
  }

  public boolean allFalse()
  {
    return fFlags.allFalse();
  }

  public boolean sets()
  {
    return fFlags.getFlag(SETS);
  }

  public boolean hasControl()
  {
    return fFlags.getFlag(HAS_CONTROL);
  }

  public boolean isReturn()
  {
    return fFlags.getFlag(IS_RETURN);
  }

  public BBlock getBBlock()
  {
    return fBBlock;
  }

//##62 BEGIN
  public ExpId
    getCorrespondingExpId()
  {
    return fCorrespondingExpId;
  }

//##62 END
}
