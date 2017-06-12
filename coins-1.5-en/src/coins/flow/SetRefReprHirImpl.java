/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
//##60 package coins.aflow;
package coins.flow; //##60

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import coins.HirRoot;
import coins.SourceLanguage;
//##63 import coins.aflow.util.FlowError;
import coins.ir.IR;
import coins.ir.hir.Exp;
import coins.ir.hir.FunctionExp;
import coins.ir.hir.HIR;
import coins.ir.hir.HirIterator;
import coins.ir.hir.IfStmt;
import coins.ir.hir.QualifiedExp;
import coins.ir.hir.Stmt;
import coins.ir.hir.SubscriptedExp;
import coins.ir.hir.SwitchStmt;
import coins.ir.hir.VarNode;  //##71
import coins.sym.ExpId;
import coins.sym.FlowAnalSym;
import coins.sym.Subp;
import coins.sym.Sym;
import coins.sym.Type;

//##11 : Modified on Feb. 2002. by Tan

/**
 * Implementation of the SetRefRepr interface for HIR
 *
 *
 */
public class SetRefReprHirImpl
  extends SetRefReprImpl
  //##60 implements SetRefReprHir
  implements SetRefRepr
{
  //##25 Stmt fStmt;
  protected Stmt fStmt; //##25
  public final HirRoot hirRoot;
  //##62 public final Flow flow;

//##25  protected SetRefReprHirImpl(Stmt pSubtree, BBlock pBBlock)
//##60  protected SetRefReprHirImpl(HIR pSubtree, BBlock pBBlock)  //##25
  public SetRefReprHirImpl(HIR pSubtree, BBlock pBBlock) //##60
  {
    super((IR)pSubtree, ((BBlockImpl)pBBlock).flowRoot.fSubpFlow,
          ((BBlockImpl)pBBlock).flowRoot.fSubpFlow.getExpId(pSubtree));
    //##60 flow = flowRoot.aflow;
    //##62 flow = flowRoot.flow; //##60
    hirRoot = flowRoot.hirRoot;
    //##25 fStmt = pSubtree;
    setInformation(pSubtree); //##62
  } // constructor

//##62 BEGIN
  public SetRefReprHirImpl(IR pHir, SubpFlow pSubpFlow, ExpId pExpId)
  {
    super(pHir, pSubpFlow, pExpId);
    hirRoot = flowRoot.hirRoot;
    setInformation((HIR)pHir);
  }
  protected void setInformation(HIR pSubtree)
  {
    flow.dbg(5, " setInformation ", pSubtree.toStringShort()); //##62
    //##25 fStmt = pSubtree;
    if (pSubtree instanceof Stmt) //##25
      fStmt = (Stmt)pSubtree; //##25
    fOpCode = pSubtree.getOperator();
    switch (fOpCode) {
      case HIR.OP_ASSIGN:
        fFlags.setFlag(SETS, true);
        fDefSym = defSym(); //##60
        break;
      case HIR.OP_IF:
        fIR = ((IfStmt)pSubtree).getIfCondition();
        fFlags.setFlag(HAS_CONTROL, true);
        break;
      case HIR.OP_INDEXED_LOOP:
        break;
     // throw new UnsupportedOperationException("Indexed loop unsupported.");
      case HIR.OP_SWITCH:
        fIR = ((SwitchStmt)pSubtree).getSelectionExp();
        fFlags.setFlag(HAS_CONTROL, true);
        break;
      case HIR.OP_RETURN:
        fFlags.setFlag(HAS_CONTROL, true);
        fFlags.setFlag(IS_RETURN, true);
        break;
      case HIR.OP_EXP_STMT:
        boolean lCheck = checkWhetherBranch(fStmt);
        fFlags.setFlag(HAS_CONTROL, lCheck);
        break;
      case HIR.OP_STMT_UPPER:
        //##25  throw new IllegalArgumentException();
      default:
        //##25 throw new IllegalArgumentException("Unexpected argument. " +
        //##25   pSubtree.getIrName() + " may not be implemented yet.");
        break;
    }
  } // setInformation
//##62 END

  public Stmt getStmt()
  {
	if (fStmt != null) //##97
      return fStmt;
	//##97 BEGIN
	else {
	  if (fIR instanceof Stmt) {
		fStmt = (Stmt)fIR;
	    return fStmt;
	  }else {
		fStmt = ((HIR)fIR).getStmtContainingThisNode();
	    return fStmt;
	  }
	}
	//##97 END
  }

  /**
   * Returns the List of nodes that are associated with a Sym  that
   * are used (read), in the depth first order
   * that appears under the subtree returned by getIR().
   * Sym may be FlowAnalSyms and Subp.
   * Child 1 of SUBS node is excluded. ??
   * Nodes that correspond to Syms that are possibly used
   * are included.
   */
  public List useNodeList()
  {
    //##60 return useNodeList(true, true);
    if (fUseNodeList == null) //##60
      fUseNodeList = useNodeList(true, true); //##60
    return fUseNodeList;    //##60
  }

  /**
   * Get the list of expressions (that have ExpId) under the associated subtree.
   * defNode() expression is excluded.
   * @return the list of expressions
   */
  public List exps()
  {
    if (fExps != null)
      return fExps;
    fExps = expsUnder((HIR)getIR());
    if (defNode() != null)
      fExps.remove(defNode());
    return fExps;
  }

  public IR defNode()
  {
    if (sets()) {
      return getIR().getChild1();
    }
    return null;
  }

  /** modSyms //##25-1
   * Get the set of FlowAnalSym symbols whose value is set
   * during the evaluation of the subtree corresponding to this
   * SetRefRepr.
   * @return the set of symbols to be modified.
   */
  public Set modSyms() // Overridden by SetRefReprHirEImpl
  {
    if (fModSyms != null) //##60
      return fModSyms;    //##60
    Exp lDefNode = (Exp)defNode();
    HIR lHIR;
    HIR lHIR0;
    int lOpCode;
    int lDerefDepth;
    Set lModSyms = new HashSet();
    //		int lTypeKind;
    lHIR = lDefNode;
    if (lDefNode == null) {
      fModSyms = lModSyms; //##60
      return lModSyms;
    }
    //##85 if (true)
    {
      modSymsUnder(lDefNode, lModSyms);
      fModSyms = lModSyms; //##60
      return lModSyms;
    }
    /* //##85
    Nodes:
      //##60 for (HirIterator lIt = FlowUtil.hirIterator(lDefNode); lIt.hasNext();)
      for (HirIterator lIt = hirRoot.hir.hirIterator(lDefNode);
           lIt.hasNext(); ) {
      lHIR = lIt.next();
      //			System.out.println("lHIR " + lHIR);
      lDerefDepth = 0;
      lHIR0 = lHIR;
      Sym lSym = lHIR.getSym();
      //			System.out.println("lSym " + lSym);
      if (!(lSym instanceof FlowAnalSym)) {
        continue;
      }
      while (true) {
        if (lHIR0 == lDefNode) {
          break;
        }
        lOpCode = ((HIR)lHIR0.getParent()).getOperator();
        switch (lOpCode) {
          case HIR.OP_SUBS:
          case HIR.OP_QUAL:
            if (lHIR0.getParent().getChild2() == lHIR0) {
              continue Nodes;
            }
            else {
              break;
            }
          case HIR.OP_ARROW:
            if (lHIR0.getParent().getChild2() == lHIR0) {
              continue Nodes;
            }
            else {
              lDerefDepth++;

              break;
            }
          case HIR.OP_CONTENTS:
            lDerefDepth++;
            break;
          case HIR.OP_ADDR:
            lDerefDepth--;
            break;
          default:
            break;
        }
        lHIR0 = (HIR)lHIR0.getParent();

        //				System.out.println("lDefNode " + lDefNode);
        //				System.out.println("lHIR0 " + lHIR0);
      }
      if (lDerefDepth <= 0) {
        lModSyms.add(lSym);
      }
    }
    //			//		System.out.println("lModSyms: " + lModSyms);
    fModSyms = lModSyms; //##60
    return lModSyms;
    */ //##85
  } // modSyms

  //##25 private void modSymsUnder(HIR pHIR, Set pModSyms)
  // modSymsUnder is not used in SetRefREprHirEImpl.
  // It is used in old version.
  protected void modSymsUnder(HIR pHIR, Set pModSyms) //##25
  {
    Sym lSym;
    lSym = pHIR.getSym();

    int lOpCode = pHIR.getOperator();

    if (pHIR instanceof Stmt) {
      throw new FlowError("Invalid argument.");
    }

    if (lSym instanceof FlowAnalSym) {
      pModSyms.add(pHIR.getSym());
    }

    switch (lOpCode) {
      case HIR.OP_SUBS:
        modSymsUnder(((SubscriptedExp)pHIR).getArrayExp(), pModSyms);
        break;
      case HIR.OP_CONTENTS:
      case HIR.OP_ARROW:
      case HIR.OP_UNDECAY:
        //##60 pModSyms.addAll(fBBlock.getSubpFlow().getSymIndexTable());
        //##62 pModSyms.addAll(((FlowImpl)flowRoot.flow).fSubpFlow.getSymIndexTable()); //##60
        pModSyms.addAll(((FlowImpl)flowRoot.flow).fSubpFlow.setOfAddressTakenVariables()); //##62
        pModSyms.addAll(((FlowImpl)flowRoot.flow).fSubpFlow.setOfGlobalVariables()); //##62
//  fResults.put("WritesToIndefiniteAddress", this, new Boolean(true));
        break;
      case HIR.OP_QUAL:
        modSymsUnder(((QualifiedExp)pHIR).getQualifierExp(), pModSyms);
        break;
    }
  } // modSymsUnder

  //##25 private Set flowAnalSymsUnder(HIR pHIR)
  // flowAnalSymsUnder is used in lhsSyms
  protected Set flowAnalSymsUnder(HIR pHIR) //##25
  {
    Set fFlowAnalSymsUnder = new HashSet();
    Sym lSym;
    ExpId lExpId;
    HIR lHir;
    //##60 for (HirIterator lIt = FlowUtil.hirIterator(pHIR); lIt.hasNext();)
    for (HirIterator lIt = hirRoot.hir.hirIterator(pHIR); //##60
         lIt.hasNext(); ) {
      lHir = (HIR)lIt.next();
      if (lHir == null)
        continue;
      lSym = lHir.getSym();
      if (lSym instanceof FlowAnalSym) {
        fFlowAnalSymsUnder.add(lSym);
      }else {
        lExpId = flowRoot.fSubpFlow.getExpId(lHir);
        if (lExpId != null)
          fFlowAnalSymsUnder.add(lExpId);
      }
    }
    return fFlowAnalSymsUnder;
  } // flowAnalSymsUnder

//##71 BEGIN
  /**
   * Get the set of symbols that may be modified when
   * pExp is used as LHS (left hand side) expression.
   * @param pExp LHS expression that is used as LHS operand
   *    of AssignStmt, call-operand, etc.
   * @return the set of symbols that may be modified.
   */
  protected Set
  symsModifiedForLhsExp( Exp pExp )
  {
    Set lResult = new HashSet();
    int lOperator = pExp.getOperator();
    int lTypeKind;
    Sym  lSym;
    Exp lChild;
    if (pExp != null) {
      if (pExp instanceof VarNode) {
        lResult.add(((VarNode)pExp).getSymNodeSym());
      }else {
        switch (pExp.getOperator()) {
          case HIR.OP_CONTENTS:
            lResult = symsModifiedForLhsExp((Exp)pExp.getChild1());
            break;
          case HIR.OP_ADD:
          case HIR.OP_SUB:
            if (pExp.getType().getTypeKind() == Type.KIND_POINTER) {
              lResult = symsModifiedForLhsExp((Exp)pExp.getChild1());
            }
            break;
          case HIR.OP_ARROW:
            lResult = symsModifiedForLhsExp((Exp)pExp.getChild1());
            break;

          default:
            break;
        }
      }
    }
    return lResult;
  } // symsModifiedForLhsExp
//##71 END

//##60 BEGIN
  protected Set expIdSet()
  {
    if (fExpIdSet != null)
      return fExpIdSet;

    FlowAnalSym lSym;
    Set lNonExpId = new HashSet();
    Set lFlowAnalSyms = flowAnalSymsUnder((HIR)fIR);
    for (Iterator lIterator = lFlowAnalSyms.iterator();
         lIterator.hasNext(); ) {
      lSym = (FlowAnalSym)lIterator.next();
      if ((lSym != null)&&(!(lSym instanceof ExpId)))
        lNonExpId.add(lSym);
    }
    if (! lNonExpId.isEmpty())
      lFlowAnalSyms.removeAll(lNonExpId);
    fExpIdSet = lFlowAnalSyms;
    return fExpIdSet;
  } // expIdSet
//##60 END

  public Set lhsSyms()
  {
    //##60 return flowAnalSymsUnder((HIR)defNode());
    //##60 BEGIN
    if (fLhsSyms == null)
      fLhsSyms = flowAnalSymsUnder((HIR)defNode());
    return fLhsSyms;
    //##60 END
  } // lhsSyms

  //##25 private List expsUnder(HIR pSubtree)
  protected List expsUnder(HIR pSubtree)
  {                // Called only from exps().
    HIR lHir;
    List lExps = new ArrayList();
    //##60 for (HirIterator lHirIt = FlowUtil.hirIterator(pSubtree);
    for (HirIterator lHirIt = hirRoot.hir.hirIterator(pSubtree); //##60
         lHirIt.hasNext(); ) {
      lHir = lHirIt.next();
      if (FlowUtil.shouldAssignFlowExpId(lHir)) {
        lExps.add(lHir);
      }
    }
    return lExps;
  } // expsUnder
  /**
   * List up nodes using FlowAnalSym and Subp
   * (exclude child1 of SUBS node)
   * @param pFromTop not used in this method.
   * @param pFromLeft not used in this method.
   * @return thelist of use-nodes.
   */
  protected //##25
    List useNodeList(boolean pFromTop, boolean pFromLeft)
  {                    // Called only from useNodeList().
    List lUseNodeList = new ArrayList();
    HIR lHir;
    NodeListIterator lIt = FlowUtil.nodeListIterator(getIR(), pFromTop,
      pFromLeft);
    int lOpCode;
    Sym lSym;
    for (; lIt.hasNext(); ) {
      lHir = (HIR)lIt.next();

      if (((lSym = lHir.getSym())instanceof FlowAnalSym ||
           lSym instanceof Subp)) {
        lUseNodeList.add(lHir);
      }
    }
    lUseNodeList.remove(lHir = (HIR)defNode());
    while ((lHir != null) &&
           ((lOpCode = lHir.getOperator()) == HIR.OP_SUBS)) {
      lHir = (HIR)lHir.getChild1();
    }
    lUseNodeList.remove(lHir);
    fUseNodeList = lUseNodeList; //##60
    if (fDbgLevel > 0)
      flow.dbg(5, "SetRefReprHirImpl: lUseNodeList = ", lUseNodeList);
    return lUseNodeList;
  } // useNodeList

  protected //##25
    List exps(boolean pFromTop, boolean pFromLeft)
  {
    if (fDbgLevel > 0)
      flow.dbg(5, "exps " + pFromTop + " " + pFromLeft
             + " " + getIR().toStringShort());  //##60
    List lExpList = new ArrayList();
    HIR lHir;
    NodeListIterator lIt = FlowUtil.nodeListIterator(getIR(), pFromTop,
      pFromLeft);
    int lOpCode;
    Sym lSym;

    for (; lIt.hasNext(); ) {
      lHir = (HIR)lIt.next();

      //			if ((lOpCode = lHir.getOperator()) == HIR.OP_VAR || lOpCode == HIR.OP_PARAM || lOpCode == HIR.OP_ELEM || )
      //			if ((lSym = lHir.getSym()) instanceof FlowAnalSym || lSym instanceof Subp)
      //##60 if (fResults.getRaw("FlowExpIdForNode", lHir) != null)
      if (flow.getSubpFlow().getExpId(lHir) != null) { //##60
        lExpList.add(lHir);
      }
    }

    lExpList.remove(defNode());

    return lExpList;
  } // exps

  //##71 public boolean hasCall() // See hasCall of SetRefREprHirEImpl.
  public boolean hasCallWithSideEffect() // See hasCall of SetRefREprHirEImpl.
  {
    for (Iterator lIt = expIterator(); lIt.hasNext(); )
      if (((HIR)lIt.next()).getOperator() == HIR.OP_CALL) {
        return true;
      }

    return false;
  }

  public List callNodes()
  {
    if (fCallNodes != null) //##60
      return fCallNodes;    //##60
    HIR lHIR;
    List lCallNodes = new ArrayList();
    for (Iterator lIt = expIterator(); lIt.hasNext(); ) {
      lHIR = (HIR)lIt.next();
      if (lHIR.getOperator() == HIR.OP_CALL)
        lCallNodes.add(lHIR);
    }
    fCallNodes = lCallNodes; //##60
    return lCallNodes;
  }

  // Obviously temporary
  //##25 private static boolean checkWhetherBranch(Stmt pStmt)
  protected static boolean checkWhetherBranch(Stmt pStmt) //##25
  {
    Stmt lStmt = pStmt.getUpperStmt();

    if (lStmt == null) {
      return false;
    }

    if (lStmt.getOperator() == HIR.OP_UNTIL) {
      return true;
    }

    lStmt = lStmt.getUpperStmt();

    if (lStmt == null) {
      return false;
    }

    switch (lStmt.getOperator()) {
      case HIR.OP_WHILE:
      case HIR.OP_FOR:
      case HIR.OP_UNTIL:
      case HIR.OP_INDEXED_LOOP:
        return true;

      default:
        return false;
    }
  }

  public boolean writesToDefiniteAddress()
  {
    Exp lDefNode = (Exp)defNode();

    if (!sets()) {
      return false;
    }

    switch (lDefNode.getOperator()) {
      case HIR.OP_VAR:
        return true;

      case HIR.OP_SUBS:
        return hasDefiniteValue(lDefNode);

      case HIR.OP_CONTENTS:
        return hasDefiniteValue((Exp)lDefNode.getChild1());

      default:
        throw new FlowError("Unexpected");
    }
  }

  //##25 private static boolean hasDefiniteValue(Exp pExp)
  protected static boolean hasDefiniteValue(Exp pExp) //##25
  {
    switch (pExp.getOperator()) {
      case HIR.OP_VAR:

        if (pExp.getSym().getSymType().getTypeKind() == Type.KIND_VECTOR) {
          return true;
        }

        return false;

      case HIR.OP_CALL:
        return false;

      default:

        for (int i = 1; i <= pExp.getChildCount(); i++)
          if (!hasDefiniteValue((Exp)pExp.getChild(i))) {
            return false;
          }

        return true;
    }
  }

  public FlowAnalSym defSym()
  {
    if ((fDefSym == null)&&sets()) {
      //##60 return (FlowAnalSym)defNode().getSym();
      fDefSym = (FlowAnalSym)defNode().getSym(); //##60
    }
    if ((fDefSym != null)&&(fDbgLevel > 0))
        flow.dbg(6, "defSym " + fDefSym.getName());
    return fDefSym;
  } // defSym

  public Set modSyms00()
  {
    if (fModSyms00 != null) //##60
      return fModSyms00;    //##60
    Set lModSyms = modSyms();
    if (fDbgLevel > 0)
      flow.dbg(5, "modSyms00 " + lModSyms); //##57
    if (hasCallWithSideEffect()) {
      //##57 lModSyms.addAll(fBBlock.getSubpFlow().getSymIndexTable());
      //##60 if (flow.fSubpFlow != null)       //##57
      //##60   lModSyms.addAll(flow.fSubpFlow.setOfGlobalVariables()); //##57
      if (flowRoot.fSubpFlow != null) //##57
        lModSyms.addAll(flowRoot.fSubpFlow.setOfGlobalVariables()); //##57
    }
    if (fDbgLevel > 0)
      flow.dbg(5, " result " + lModSyms); //##57
    fModSyms00 = lModSyms; //##60
    return lModSyms;
  } // modSyms00

//##60 BEGIN
  public NodeListIterator nodeListIterator()
  {
    return FlowUtil.nodeListIterator(fIR);
  }

  public Iterator useNodeIterator()
  {
    if (fUseNodeList == null)
      fUseNodeList = useNodeList();
    return fUseNodeList.iterator();
  }

//##60 END
}
