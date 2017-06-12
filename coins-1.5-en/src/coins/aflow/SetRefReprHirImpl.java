/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import coins.HirRoot;
import coins.SourceLanguage;
import coins.aflow.util.FlowError;
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
import coins.ir.hir.VarNode; //##71
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
public class SetRefReprHirImpl extends SetRefReprImpl implements SetRefReprHir
{
    //##25 Stmt fStmt;
    protected Stmt fStmt;  //##25
    public final HirRoot hirRoot;
    public final Flow flow;

//##25    protected SetRefReprHirImpl(Stmt pSubtree, BBlock pBBlock)
    protected SetRefReprHirImpl(HIR pSubtree, BBlock pBBlock)  //##25
    {
        super(pSubtree, pBBlock);
        flow = flowRoot.aflow;
        hirRoot = flowRoot.hirRoot;
        //##25 fStmt = pSubtree;
        if (pSubtree instanceof Stmt) //##25
          fStmt = (Stmt)pSubtree;     //##25
        fOpCode = pSubtree.getOperator();

        switch (fOpCode)
        {
            case HIR.OP_ASSIGN:
                fFlags.setFlag(SETS, true);

                break;

            case HIR.OP_IF:
                fIR = ((IfStmt) pSubtree).getIfCondition();
                fFlags.setFlag(HAS_CONTROL, true);

                break;

            case HIR.OP_INDEXED_LOOP:
                break;
                //            throw new UnsupportedOperationException("Indexed loop unsupported.");

            case HIR.OP_SWITCH:
                fIR = ((SwitchStmt) pSubtree).getSelectionExp();
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
        }
    }

    public Stmt getStmt()
    {
        return fStmt;
    }

    public List useNodeList()
    {
        return useNodeList(true, true);
    }

    public List exps()
    {
        List lExps = expsUnder((HIR) getIR());

        //                if (sets())
        lExps.remove(defNode());

        return lExps;
    }

    public IR defNode()
    {
        if (sets())
        {
            // Fukuda
              return (IR)FlowUtil.getQualVarNode((HIR)getIR().getChild1());
            //return getIR().getChild1();
        }

        return null;
    }
    /** modSyms: //##25-1
     * Get the set of FlowAnalSym symbols whose value is set
     * during the evaluation of the subtree corresponding to this
     * SetRefRepr.
     * @return the set of symbols to be modified.
     */
    public Set modSyms() // To be improved.
    {
        Exp lDefNode = (Exp) defNode();
        HIR lHIR;
        HIR lHIR0;
        int lOpCode;
        int lDerefDepth;
        Set lModSyms = new HashSet();

        //		int lTypeKind;
        lHIR = lDefNode;

        if (lDefNode == null)
        {
            return lModSyms;
        }

        //##85 if (true)
        {
            modSymsUnder(lDefNode, lModSyms);

            return lModSyms;
        }
        /* //##85
        Nodes:
            for (HirIterator lIt = FlowUtil.hirIterator(lDefNode); lIt.hasNext();)
            {
                lHIR = lIt.next();

                //			System.out.println("lHIR " + lHIR);
                lDerefDepth = 0;
                lHIR0 = lHIR;

                Sym lSym = lHIR.getSym();

                //			System.out.println("lSym " + lSym);
                if (!(lSym instanceof FlowAnalSym))
                {
                    continue;
                }

                while (true)
                {
                    if (lHIR0 == lDefNode)
                    {
                        break;
                    }

                    lOpCode = ((HIR) lHIR0.getParent()).getOperator();

                    switch (lOpCode)
                    {
                        case HIR.OP_SUBS:
                        case HIR.OP_QUAL:

                            if (lHIR0.getParent().getChild2() == lHIR0)
                            {
                                continue Nodes;
                            } else
                            {
                                break;
                            }

                        case HIR.OP_ARROW:

                            if (lHIR0.getParent().getChild2() == lHIR0)
                            {
                                continue Nodes;
                            } else
                            {
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

                    lHIR0 = (HIR) lHIR0.getParent();

                    //				System.out.println("lDefNode " + lDefNode);
                    //				System.out.println("lHIR0 " + lHIR0);
                }

                if (lDerefDepth <= 0)
                {
                    lModSyms.add(lSym);
                }
            }
            //			//		System.out.println("lModSyms: " + lModSyms);

            return lModSyms;
      */ //##85
    }

    //##25 private void modSymsUnder(HIR pHIR, Set pModSyms)
    protected void modSymsUnder(HIR pHIR, Set pModSyms)  //##25
    {
        Sym lSym;
        lSym = pHIR.getSym();

        int lOpCode = pHIR.getOperator();

        if (pHIR instanceof Stmt)
        {
            throw new FlowError("Invalid argument.");
        }

        if (lSym instanceof FlowAnalSym)
        {
            pModSyms.add(pHIR.getSym());
        }

        switch (lOpCode)
        {
            case HIR.OP_SUBS:
                modSymsUnder(((SubscriptedExp) pHIR).getArrayExp(), pModSyms);

                break;

            case HIR.OP_CONTENTS:
            case HIR.OP_ARROW:
            case HIR.OP_UNDECAY:
                pModSyms.addAll(fBBlock.getSubpFlow().getSymIndexTable());
//                fResults.put("WritesToIndefiniteAddress", this, new Boolean(true));

                break;
            case HIR.OP_QUAL:
                // Fukuda
                modSymsUnder(FlowUtil.getQualVarNode(pHIR),pModSyms);
                //modSymsUnder(((QualifiedExp)pHIR).getQualifierExp(), pModSyms);
                break;

        }
    }

    //##25 private Set flowAnalSymsUnder(HIR pHIR)
    protected Set flowAnalSymsUnder(HIR pHIR)  //##25
    {
        Set lFlowAnalSyms = new HashSet();
        Sym lSym;

        for (HirIterator lIt = FlowUtil.hirIterator(pHIR); lIt.hasNext();)
            if ((lSym = lIt.next().getSym()) instanceof FlowAnalSym)
            {
                lFlowAnalSyms.add(lSym);
            }

        return lFlowAnalSyms;
    }

    public Set lhsSyms()
    {
        return flowAnalSymsUnder((HIR) defNode());
    }

    //##25 private List expsUnder(HIR pSubtree)
    protected List expsUnder(HIR pSubtree)  //##25
    {
        HIR lHir;
        List lExps = new ArrayList();

        for (HirIterator lHirIt = FlowUtil.hirIterator(pSubtree);
        lHirIt.hasNext();)
        {
            lHir = lHirIt.next();

            if (FlowUtil.shouldAssignFlowExpId(lHir))
            {
                lExps.add(lHir);
            }
        }

        return lExps;
    }

    //##25 private Set flowAnalSymNodesUnder(HIR pSubtree)
    protected Set flowAnalSymNodesUnder(HIR pSubtree)  //##25
    {
        HIR lHir;
        Set lFlowAnalSymNodes = new HashSet();
        HIR lListItem;

        for (HirIterator lHirIt = FlowUtil.hirIterator(pSubtree);
        lHirIt.hasNext();)
        {
            if ((lHir = lHirIt.next()).getSym() instanceof FlowAnalSym)
            {
                lFlowAnalSymNodes.add(lHir);
            }
        }

        return lFlowAnalSymNodes;
    }

    protected  //##25
    List useNodeList(boolean pFromTop, boolean pFromLeft)
    {
        List lUseNodeList = new ArrayList();
        HIR lHir;
        NodeListIterator lIt = FlowUtil.nodeListIterator(getIR(), pFromTop,
        pFromLeft);
        int lOpCode;
        Sym lSym;

        for (; lIt.hasNext();)
        {
            lHir = (HIR) lIt.next();

            if (((lSym = lHir.getSym()) instanceof FlowAnalSym ||
            lSym instanceof Subp))
            {
                lUseNodeList.add(lHir);
            }
        }

        lUseNodeList.remove(lHir = (HIR) defNode());

        while ((lHir != null) &&
        ((lOpCode = lHir.getOperator()) == HIR.OP_SUBS))
        {
            //			System.out.println("Removed: " +        lUseNodeList.remove(lHir.getChild1()));
            lHir = (HIR) lHir.getChild1();
        }

        lUseNodeList.remove(lHir);
        flow.dbg(5, "SetRefReprHirImpl: lUseNodeList = ", lUseNodeList);

        return lUseNodeList;
    }

    protected  //##25
    List exps(boolean pFromTop, boolean pFromLeft)
    {
        List lExpList = new ArrayList();
        HIR lHir;
        NodeListIterator lIt = FlowUtil.nodeListIterator(getIR(), pFromTop,
        pFromLeft);
        int lOpCode;
        Sym lSym;

        for (; lIt.hasNext();)
        {
            lHir = (HIR) lIt.next();

            //			if ((lOpCode = lHir.getOperator()) == HIR.OP_VAR || lOpCode == HIR.OP_PARAM || lOpCode == HIR.OP_ELEM || )
            //			if ((lSym = lHir.getSym()) instanceof FlowAnalSym || lSym instanceof Subp)
            if (fResults.getRaw("FlowExpIdForNode", lHir) != null)
            {
                lExpList.add(lHir);
            }
        }

        lExpList.remove(defNode());

        return lExpList;
    }

    //##71 public boolean hasCall()
    public boolean hasCallWithSideEffect() // See hasCall of SetRefREprHirEImpl.//##71
    {
        for (Iterator lIt = expIterator(); lIt.hasNext();)
            if (((HIR) lIt.next()).getOperator() == HIR.OP_CALL)
            {
                return true;
            }

        return false;
    }

    public List callNodes()
    {
        HIR lHIR;
        List lCallNodes = new ArrayList();
        for (Iterator lIt = expIterator(); lIt.hasNext();)
        {
            lHIR = (HIR)lIt.next();
            if (lHIR.getOperator() == HIR.OP_CALL)
                lCallNodes.add(lHIR);
        }
        return lCallNodes;
    }

    // Obviously temporary
    //##25 private static boolean checkWhetherBranch(Stmt pStmt)
      protected static boolean checkWhetherBranch(Stmt pStmt)  //##25
    {
        Stmt lStmt = pStmt.getUpperStmt();

        if (lStmt == null)
        {
            return false;
        }

        if (lStmt.getOperator() == HIR.OP_UNTIL)
        {
            return true;
        }

        lStmt = lStmt.getUpperStmt();

        if (lStmt == null)
        {
            return false;
        }

        switch (lStmt.getOperator())
        {
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
        Exp lDefNode = (Exp) defNode();

        if (!sets())
        {
            return false;
        }

        switch (lDefNode.getOperator())
        {
            case HIR.OP_VAR:
                return true;

            case HIR.OP_SUBS:
                return hasDefiniteValue(lDefNode);

            case HIR.OP_CONTENTS:
                return hasDefiniteValue((Exp) lDefNode.getChild1());

            default:
                throw new FlowError("Unexpected");
        }
    }

    //##25 private static boolean hasDefiniteValue(Exp pExp)
    protected static boolean hasDefiniteValue(Exp pExp)  //##25
    {
        switch (pExp.getOperator())
        {
            case HIR.OP_VAR:

                if (pExp.getSym().getSymType().getTypeKind() == Type.KIND_VECTOR)
                {
                    return true;
                }

                return false;

            case HIR.OP_CALL:
                return false;

            default:

                for (int i = 1; i <= pExp.getChildCount(); i++)
                    if (!hasDefiniteValue((Exp) pExp.getChild(i)))
                    {
                        return false;
                    }

                return true;
        }
    }

    public FlowAnalSym defSym()
    {
        if (sets())
        {
            return (FlowAnalSym) defNode().getSym();
        }

        return null;
    }

    public Set modSyms00()
    {
        Set lModSyms = modSyms();
        flow.dbg(5, "modSyms00 " + lModSyms); //##57
        //##71 if (hasCall())
        if (hasCallWithSideEffect()) //##71
        {
          //##57 lModSyms.addAll(fBBlock.getSubpFlow().getSymIndexTable());
          if (flow.fSubpFlow != null)                               //##57
            lModSyms.addAll(flow.fSubpFlow.setOfGlobalVariables()); //##57
        }
        flow.dbg(5, " result " + lModSyms); //##57
        return lModSyms;
    }

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

    /*
    public String toString()
    {
            StringBuffer lBuff = new StringBuffer();
            print((HIR)getIR(), 0, false, lBuff);
            return lBuff.toString();
    }

    private void
    print(HIR pHIR, int pIndent, boolean pDetail, StringBuffer pBuff)
    {
    //                HIR pHIR = (HIR)getIR();
    //                StringBuffer lBuff = new StringBuffer();
            String lineImage, lSpace;
            lSpace = hirRoot.ir.getIndentSpace(pIndent);
            if (pDetail)
            {
                    if (pHIR.getChildCount() == 0)
                    {
                            lineImage = lSpace + "<" + pHIR.toStringDetail();
                    }else
                    {
                            if (pHIR instanceof HirSeq)
                                    lineImage = lSpace + "(seq "  + pHIR.getIndex() + " " + pHIR.getType().toStringShort();
                            else
                                    lineImage = lSpace + "(" + pHIR.toStringDetail();
                    }
            }else
            {
                    if (pHIR.getChildCount() == 0)
                    {
                            lineImage = lSpace + "<" + pHIR.toString();
                    }else
                    {
                            if (pHIR instanceof HirSeq)
                                    lineImage = lSpace + "(seq " + pHIR.getIndex();
                            else
                                    lineImage = lSpace + "(" + pHIR.toString();
                    }
            }
            if (pHIR.getChildCount() == 0)
            {
                    if (pHIR instanceof HirList)
                    {
                            print((HirList)pHIR, pIndent, pDetail, pBuff);
                    }else
                            pBuff.append("\n" + lineImage + ">"); //##8
            }else
            {
                    pBuff.append("\n" + lineImage);
    //                        hirRoot.ioRoot.printOut.print("\n" + lineImage); //##8
                    if (pHIR.getChild1() != null)
                    {
                            print((HIR)pHIR.getChild1(),  pIndent+1, pDetail , pBuff);
                            //## ((HIR)fChildNode1).print( pIndent+1, pDetail );
                    }else
                            pBuff.append("\n" + lSpace + "<null 0 void>"); //##8
                    if (pHIR.getChildCount() >= 2)
                    {
                            if (pHIR.getChild2() != null)
                                    print((HIR)pHIR.getChild2(), pIndent+1, pDetail, pBuff);
                            else
                                    pBuff.append("\n" + lSpace + "<null 0 void>");
                            for (int i = 2; i < pHIR.getChildCount(); i++)
                            {
                                    if (pHIR.getChild(i) != null)
                                            print((HIR)pHIR.getChild(i), pIndent + 1, pDetail, pBuff);
                                    else
                                            pBuff.append("\n" + lSpace + "<null 0 void>");
    //                                                hirRoot.ioRoot.printOut.print("\n" + lSpace + "<null 0 void>"); //##8
                            }
                    }
                    if (!(this instanceof HirList))
                            pBuff.append(")");
    //                                hirRoot.ioRoot.printOut.print(")"); //##8
            }
            if (pHIR.getNextStmt() != null)
                    print(pHIR.getNextStmt(), pIndent, pDetail, pBuff);
    //                        pBuff.append(getNextStmt().print(pIndent, pDetail));
    //                        getNextStmt().print( pIndent, pDetail );
    } // print

    private void print(HirList pHirList, int pIndent, boolean pDetail, StringBuffer pBuff)
    {
     */
}
// SetRefReprHirImpl
