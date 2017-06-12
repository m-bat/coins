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
import coins.ir.hir.AssignStmt;
import coins.ir.hir.Exp;
import coins.ir.hir.FunctionExp;
import coins.ir.hir.HIR;
import coins.ir.hir.HirIterator;
import coins.ir.hir.IfStmt;
import coins.ir.hir.Stmt;
import coins.ir.hir.SubscriptedExp;
import coins.ir.hir.SwitchStmt;
import coins.sym.FlowAnalSym;
import coins.sym.Subp;
import coins.sym.Sym;
import coins.sym.Type;
import coins.sym.Var;       //##57
import coins.ir.hir.BlockStmt; //##25
import coins.ir.hir.ConstNode; //##25
import coins.ir.hir.HirList; //##25
import coins.ir.hir.JumpStmt; //##25
import coins.ir.hir.LabeledStmt; //##25
import coins.ir.hir.LoopStmt; //##25
import coins.ir.hir.NullNode; //##25
import coins.ir.hir.ReturnStmt; //##25
import coins.ir.hir.SymNode; //##71
import coins.sym.ExpId; //##25
import coins.sym.VectorType; //##25
import coins.sym.StructType; //##25
import coins.sym.UnionType; //##25


//##25 : SetRefRepsHirImpl is extended to SetRefReprHirEImpl on Sep. 2003 by Tan
//       to do optimization for array elements.

/**
 * Implementation of the SetRefRepr interface for HIR
 */
public class
SetRefReprHirEImpl extends SetRefReprHirImpl implements SetRefReprHir
{
  // Fields inherited from SetRefReprHirImpl
  //   Stmt fStmt;
  //   public final HirRoot hirRoot;
  //   public final Flow flow;

  // Fields inherited from SetRefReprImpl:
  //   public final FlowRoot flowRoot;
  //   public final SymRoot symRoot;
  //   protected IR fIR;
  //   protected List fUseNodeList;
  //                  Not used in original aflow and opt.
  //   protected List fUseExpIdList;
  //                  Not used in original aflow and opt.
  //   protected int fOpCode;
  //   FlowResults fResults;
  //   protected FlagBox fFlags = new FlagBoxImpl();
  //   protected BBlock fBBlock;
  //   protected FlowAnalSym fDefSym;

  /** fModSyms: Symbols that may be modified by the node
   * corresponding to this.
   * ExpId's for intermediate results are excluded but ExpId
   * for compound variable in LHS of AssignStmt is included.  */
  protected Set
    fModSyms = new HashSet();

  /** fModSymsStmt: Symbols that may be modified by the simple
   * statement containing the subtree corresponding to this
   * SetRefRepr. It is allocated for each simple statement and
   * refered from all subexpressions in the simple statement. */
  protected Set
    fModSymsStmt = null;

/** fModSymsAndAliases: Symbols that may be modified by the node
 * corresponding to this.
 * Symbols in alias group of fModSyms are included.
 * ExpId's for intermediate results are excluded but ExpId
 * for compound variable in LHS of AssignStmt is included.  */
//protected Set
//  fModSymsAndAliases = new HashSet();

/** fLeafOperands: Symbols of subexpressions used as operands
 *  by the node corresponding to this.
 *  ExpId's for intermediate results are excluded. */
  protected Set
    fLeafOperands = new HashSet();

/** fLeafOperandsAndAliases: Symbols of subexpressions used as operands
 *  by the node corresponding to this.
 *  Symbols in alias group of fLeafOperands are included.
 *  ExpId's for intermediate results are excluded. */
//  protected Set
//    fLeafOperandsAndAliases = new HashSet();

/** fOperandExp: ExpId's of subexpressions used as direct operands
 * by the node corresponding to this.
 * Leaf operands are excluded. */
  protected Set
    fOperandExp = new HashSet();

/** fAllSubexps: ExpId's of all subexpressions under the node
 * corresponding to this.
 * Leaf operands are excluded. */
  protected Set
     fAllSubexps = new HashSet();

 /** fCallIncluded: True if call with side effect is included in the  subtree fIR,
  *  false if call with side effect is not included.  */ //##71
 protected boolean
    //##71 fCallIncluded = false;
    fCallWithSideEffectIncluded = false;

/** SetRefReprHirEImpl instanciates SetRefRepr for the subtree pSubtree
 *  by computing SetRefRepr for child subtrees.
 *  For leaf children, SetRefRepr is not created.
 *  Required information fSetRefReprTable, fIrIndexMin, fIrIndexMax,
 *  and each item of fSetRefReprTable are set by HirSubpFlowImpl
 * (and LirSubpFlowImpl).
 * @param pSubtree: HIR subtree for which SetRefRepr is to be computed.
 * @param pBBlock: Basic block containing pSubtree.
 * @param pDef: true if pSubtree defines a symbol,
 *              false otherwise.
 */
  public
  SetRefReprHirEImpl(HIR pSubtree, BBlock pBBlock, boolean pDef,
      Set pModSymsStmt)
  {
    super(pSubtree, pBBlock);
    // flow = flowRoot.aflow;
    // hirRoot = flowRoot.hirRoot;
    // fStmt = pSubtree;
    // fOpCode = pSubtree.getOperator();
    FlowAnalSym lSym = pSubtree.getSymOrExpId();
    if (lSym != null) {
      if (lSym instanceof ExpId) {
        fFlags.setFlag(SETS, true);
        fDefSym = lSym;
      }
    }
    if (pSubtree instanceof AssignStmt)
      fModSymsStmt = new HashSet();
    else
      fModSymsStmt = pModSymsStmt;
    computeSetRefRepr( (HIR)fIR, pBBlock, pDef, fModSymsStmt);
    flowRoot.aflow.getSubpFlow().setSetRefReprOfIR(this, pSubtree.getIndex());
    flow.dbg(4, "SetRefReprHirEImpl " + fIR.toString()+ " defSym ", fDefSym);
    flow.dbg(4, "  modSyms ", fModSyms);
    flow.dbg(4, "  leafOperands ", fLeafOperands);
    flow.dbg(4, "  operandExp ", fOperandExp);
    flow.dbg(4, "  allSubexp ", fAllSubexps);
    if (fModSymsStmt != null)
      flow.dbg(4, "  modSymsStmt ", fModSymsStmt);
    // flow.dbg(6, "  modSymsAll ", fModSymsAll);
    // flow.dbg(6, "  operandsAll ", fOperandsAll);
  } // SetRefReprHirEImpl

  /** computeSetRefRepr:
   * Compute defined symbols (fDefSym) and set of possibly modified variables
   * (fModSyms), variables used as leaf operand (fLeafOperands).
   * for the HIR subtree pHir within the basic block pBBlock.
   * Fields fDefSym, fModSyms, fLeafOperands are computed and
   * and SETS flag of fFlags are set true if some symbol is defined.
   * The branchs outside pBBlock are not processed.
   * Traverse should be in the sequence of evaluation at execution time.
   * @param pHir: subtree to be processed.
   * @param pBBlock: basic block within which variables are scanned.
   * @param pDef: true if traversing in define mode, false otherwise.
   * @param pModSymsStmt: set of symbols whose value may be modified
   *     by the simple statement containing pHir.
   */
  protected void
  computeSetRefRepr( HIR pHir, BBlock pBBlock, boolean pDef,
   Set pModSymsStmt )
  {
    boolean lDef;
    Set lSet; //##71
    if (pHir != null) {
      flow.dbg(5, "  computeSetRefRepr ", pHir.toStringShort());
      if (pModSymsStmt != null)
        fModSymsStmt = pModSymsStmt;
      FlowAnalSym lSym = pHir.getSymOrExpId();
      if (lSym != null) {
        if (lSym instanceof ExpId) {
          fFlags.setFlag(SETS, true);
          fDefSym = lSym;
          ((BBlockHirImpl)pBBlock).addToExpNodeList((ExpId)lSym, pHir);
        }else if (lSym.isGlobal()) { //##57
          ((BBlockHirImpl)pBBlock).addToExpNodeList(pHir.getExpId(), pHir); //##57
        } //##57
      }
      switch (pHir.getOperator()) {
        case HIR.OP_ASSIGN:
          //##71 fDefSym = ((HIR)fIR.getChild1()).getSymOrExpId();
          HIR lLHS = (HIR)fIR.getChild1(); // Left-hand-side of AssignStmt. //##71
          fDefSym = lLHS.getSymOrExpId(); //##71
          flow.dbg(5, " defSym " + fDefSym); //##71
          if (fDefSym != null) {
            fModSyms.add(fDefSym);
            //##53 BEGIN
            fModSymsStmt.add(fDefSym);
            if (pHir.getType().getTypeKind() == Type.KIND_POINTER) {
              // Pointer assignment. REFINE
              if (flow.fSubpFlow.setOfAddressTakenVariables() != null)
                fModSymsStmt.addAll(flow.fSubpFlow.setOfAddressTakenVariables());
            }
            //##53 END
            //##71 BEGIN
            else if (lLHS.getOperator() == HIR.OP_CONTENTS) {
              HIR lLvalue = (HIR)lLHS.getChild1();
              lSet = symsModifiedForLhsExp((Exp)lLvalue);
              fModSyms.addAll(lSet);
              fModSymsStmt.addAll(lSet);
            }
            //##71 END
          }
          processTheChild( (HIR)fIR.getChild2(), pBBlock, false, fModSymsStmt); //##53
          if (fIR.getChild1().getChildCount() > 0)
            processTheChild( (HIR)fIR.getChild1(), pBBlock,  true, fModSymsStmt);
          break;
        case HIR.OP_BLOCK:
          for (Stmt lStmt = ((BlockStmt)pHir).getFirstStmt();
               lStmt != null;
               lStmt = lStmt.getNextStmt()) { // Process statements.
            processTheChild(lStmt, pBBlock, false, fModSymsStmt);
            if ((lStmt instanceof JumpStmt)||
               (lStmt instanceof ReturnStmt))
              break; // The basic block is terminated.
          }
          break;
        case HIR.OP_EXP_STMT: {
          HIR lChild = (HIR)pHir.getChild1();
          processTheChild(lChild, pBBlock, false, fModSymsStmt);
          break;
        }
        case HIR.OP_CALL: {
          int lOpCode = pHir.getChild1().getOperator();
          //##71 fCallIncluded = true;
          if ((lOpCode != HIR.OP_ADDR)&&(lOpCode != HIR.OP_SUBP))
            processTheChild((HIR)pHir.getChild1(), pBBlock, false, fModSymsStmt);
          if (pHir.getChild2() instanceof HirList) { // Process parameters.
            for (Iterator lIterator = ((HirList)pHir.getChild2()).iterator();
                 lIterator.hasNext(); ) {
              HIR lArg = (HIR) lIterator.next();
              if ((lArg != null)&&(!(lArg instanceof NullNode)))
                processTheChild(lArg, pBBlock, false, fModSymsStmt);
            }
          }
          //##71 BEGIN
          boolean lWithSideEffect = false;
          SymNode lSubpNode = null;
          if (pHir.getChild1() instanceof SymNode)
            lSubpNode = (SymNode)pHir.getChild1();
          else if ((pHir.getChild1().getOperator() == HIR.OP_ADDR)&&
                   (pHir.getChild1().getChild1() instanceof SymNode))
            lSubpNode = (SymNode)pHir.getChild1().getChild1();
          if ((lSubpNode == null)||
              (! symRoot.sourceLanguage.functionsWithoutSideEffect.
               contains(lSubpNode.getSymNodeSym()))) {
            fCallWithSideEffectIncluded = true;
            flow.dbg(4, pHir.toStringShort(), "may change global variables.");
         //##71 END
            Set lGlobalVariables = fBBlock.getSubpFlow().setOfGlobalVariables();
            if (lGlobalVariables != null) {
              fModSyms.addAll(lGlobalVariables);
            }
          //##71 BEGIN
          }else {
            flow.dbg(2, pHir.toStringShort(), "has no side effect.");
          } //##71 END
          if ((fLeafOperands.size() > 0)
              &&lWithSideEffect) //##71
          {
            for (Iterator lIter2 = fLeafOperands.iterator();
                 lIter2.hasNext(); ) {
              Sym lLeafSym = (Sym)lIter2.next();
              if ((lLeafSym.getSymType() instanceof VectorType)||
                  (lLeafSym.getSymType() instanceof StructType)||
                  (lLeafSym.getSymType() instanceof UnionType)||
                  (lLeafSym.getFlag(Sym.FLAG_ADDRESS_TAKEN))) {
                fModSyms.add(lLeafSym);
              }
            }
          }
          break;
        }
        case HIR.OP_ARROW: // Child1 is use only.
          processTheChild((HIR)pHir.getChild1(), pBBlock, false, fModSymsStmt);
          processTheChild((HIR)pHir.getChild2(), pBBlock, pDef, fModSymsStmt);
          if (pDef) // Add the set of address taken variables.
            if (fBBlock.getSubpFlow().setOfAddressTakenVariables() != null)
              fModSyms.addAll(fBBlock.getSubpFlow().setOfAddressTakenVariables());
          break;
        //##25 BEGIN
        case HIR.OP_QUAL:  // Child1 is use only.
          processTheChild((HIR)pHir.getChild1(), pBBlock, false, fModSymsStmt);
          processTheChild((HIR)pHir.getChild2(), pBBlock, pDef, fModSymsStmt);
          break;
        //##25 END
        case HIR.OP_CONTENTS:
          //##53 BEGIN
          processTheChild((HIR)pHir.getChild1(), pBBlock, pDef, fModSymsStmt);
          if ((pDef)&&
              (fBBlock.getSubpFlow().setOfAddressTakenVariables() != null)) {
              // Add the set of address taken variables.
              fModSyms.addAll(fBBlock.getSubpFlow().setOfAddressTakenVariables());
          }
          break;
          //##53 END
        case HIR.OP_DECAY:
          processTheChild((HIR)pHir.getChild1(), pBBlock, pDef, fModSymsStmt);
          break;
        case HIR.OP_ADD:
        case HIR.OP_SUB:
          if (pDef&&
              (pHir.getChild1().getOperator() == HIR.OP_DECAY)) {
            processTheChild((HIR)pHir.getChild1(), pBBlock, pDef, fModSymsStmt);
            processTheChild((HIR)pHir.getChild2(), pBBlock, false, fModSymsStmt);
          }else {
            processTheChild((HIR)pHir.getChild1(), pBBlock, false, fModSymsStmt);
            processTheChild((HIR)pHir.getChild2(), pBBlock, false, fModSymsStmt);
          }
          break;
        case HIR.OP_SUBS: // Child2 is use only.
          processTheChild((HIR)pHir.getChild1(), pBBlock, pDef, fModSymsStmt);
          processTheChild((HIR)pHir.getChild2(), pBBlock, false, fModSymsStmt);
          break;
        case HIR.OP_IF: // Process condition part only.
          processTheChild(((IfStmt)pHir).getIfCondition(), pBBlock, false, fModSymsStmt);
          break;
        //## case HIR.OP_LOOP: // Process InitPart and ConditionalInitPart only.
        case HIR.OP_FOR:
        case HIR.OP_WHILE:
        case HIR.OP_UNTIL:
          processTheChild(((LoopStmt)pHir).getLoopInitPart(), pBBlock, false, fModSymsStmt);
          processTheChild(((LoopStmt)pHir).getConditionalInitPart(), pBBlock, false, fModSymsStmt);
          break;
        case HIR.OP_LABELED_STMT: // Process statement body only.
          processTheChild((HIR)pHir.getChild2(), pBBlock, false, fModSymsStmt);
          break;
        default:
        lSym = pHir.getSymOrExpId();
        if (pHir.getChildCount() == 0) {
          if (lSym != null) {
            if (pDef)
              fModSyms.add(lSym);
            else
              fLeafOperands.add(lSym); // Symbol used as operand.
          }
        }else { // With children
          if (lSym instanceof ExpId)
            fAllSubexps.add(lSym);
          for (int i = 1; i <= pHir.getChildCount(); i++) {
            HIR lChild = (HIR)pHir.getChild(i);
            if (lChild instanceof LabeledStmt)
              break;
            if (lChild != null) {
              if ((lChild.getOperator() == HIR.OP_DECAY) ||
                  (lChild.getOperator() == HIR.OP_UNDECAY))
                processTheChild(lChild, pBBlock, pDef, fModSymsStmt);
              else
                processTheChild(lChild, pBBlock, false, fModSymsStmt);
            }
          }
        }
     }
     if (fModSymsStmt != null) {
       fModSymsStmt.addAll(fModSyms);
       flow.dbg(5, " fModSymsStmt", fModSymsStmt.toString()); //##53
     }
    }
  } // computeSetRefRepr

  /** processTheChild:
   *  Compute SetRefRepr for pHir if pHir is not a ConstNode.
   *  Compute fCallIncluded, fLeafOperands, fModSyms and
   *  addToExpNodeList
   * @param pHir: child to be processed.
   * @param pBBlock: basic block containing pHir.
   * @param pDef: true if set mode, false if reference mode.
   * @param pModSymsStmt set of symbols modified by the including statement.
   */
  private void
  processTheChild( HIR pHir, BBlock pBBlock, boolean pDef,
     Set pModSymsStmt )
  {
    SetRefReprHirEImpl lSetRefRepr;
    if ((pHir != null)&&(!(pHir instanceof ConstNode))) {
      flow.dbg(5, "  processTheChild ", pHir + " " + pDef);
      lSetRefRepr = new SetRefReprHirEImpl(
                  (HIR)pHir, pBBlock, pDef, pModSymsStmt);
      fCallWithSideEffectIncluded =
        fCallWithSideEffectIncluded | lSetRefRepr.fCallWithSideEffectIncluded;
      fLeafOperands.addAll(lSetRefRepr.fLeafOperands);
      fModSyms.addAll(lSetRefRepr.fModSyms);
      fAllSubexps.addAll(lSetRefRepr.fAllSubexps);
      if (pDef) {
        if (pHir.getOperator() == HIR.OP_UNDECAY)
          fModSyms.add(pHir.getChild1().getSym());
      }else {
        Sym lSym = pHir.getSymOrExpId();
        if (lSym instanceof ExpId) {
          ((BBlockHirImpl)pBBlock).addToExpNodeList((ExpId)lSym, pHir);
          fOperandExp.add(lSym);
        }
      }
    }
  } // processTheChild

  public Stmt
  getStmt()    //##25
  {
    if (fIR instanceof Stmt)
      return fStmt;
    else
    return ((HIR)fIR).getStmtContainingThisNode();
  }

  public IR
  defNode() //##25
  {
    if (fOpCode == HIR.OP_ASSIGN ) {
       // Fukuda
     // return getIR().getChild1();
      HIR def  =(HIR)FlowUtil.getQualVarNode((HIR)getIR().getChild1());
      //System.out.println("defNode ="+def.toString());
      return  def;
    }else {
      if (fDefSym != null)
        // Fukuda
        // return fIR;
        return (IR)FlowUtil.getQualVarNode((HIR)getIR().getChild1());
      else
        return null;
    }
  } // defNode

/** modSyms returns the set of symbols that are possibly defined
 * in this SetRefRepr. Symbols externally defined
 * (i.e. via external calls) are not included.
 * LHS symbol or ExpId if AssignStmt; ExpId if Exp;
 * array symbol if SubscriptedExp;
 * all variables if pointed var assignment;
 * @return the set of symbols that are possibly defined.
**/
public Set modSyms()
{
  flowRoot.aflow.dbg(6, " modSyms" + fModSyms.toString());
  //  Fukuda
  //return fModSyms;
  return addModSymsSet(fModSyms);
} // modSyms

/** modSymsStmt returns the set of symbols that are possibly defined
   * in the this SetRefRepr. Symbols externally defined
   * (i.e. via external calls) are not included.
   * LHS symbol or ExpId if AssignStmt; ExpId if Exp;
   * array symbol if SubscriptedExp;
   * all variables if pointed var assignment;
   * @return the set of symbols that are possibly defined.
**/
public Set
modSymsStmt()
{
  Set lModSymsStmt = fModSymsStmt;
  if (lModSymsStmt == null)
    lModSymsStmt = new HashSet();
  flowRoot.aflow.dbg(5, " modSymsStmt()" + lModSymsStmt.toString());
  return lModSymsStmt;
} // modSymsStmt

/** modSyms0 returns the set of symbols that are possibly defined
 * in this SetRefRepr as modSyms() and symbols externally defined
 * (i.e. via external calls) if call is included.
 * @return the set of symbols that are possibly defined
 *       including external symbols if call is included.
**/
public Set
modSyms0()
{
  Set lModSyms = fModSyms;
  if (fCallWithSideEffectIncluded)
    lModSyms.addAll(fBBlock.getSubpFlow().setOfGlobalVariables());
  flowRoot.aflow.dbg(5, "modSyms ", lModSyms.toString());
  return lModSyms;
} // modSyms0

/** leafOperands:
 *  Get the set of symbols used as operands gathered from
 *  all leaf nodes of the subtree corresponding to this.
 *  ExpId's for intermediate results are excluded.
 **/
public Set
leafOperands() //##25
{
  return fLeafOperands;
}

/** operandExp:
 * ExpId's of subexpressions used as direct operands
 * by the node corresponding to this.
 * Leaf operands are excluded.
 **/
public Set
operandExp() //##25
{
  return fOperandExp;
}

/** operandExp:
 * ExpId's of all subexpressions of the subtree corresponding to this.
 * Leaf operands are excluded.
 **/
public Set
allSubexps() //##25
{
  return fAllSubexps;
}
//----------------------------------------------------------------
//
// addModSymsSet:
//
//                                                       I.Fukuda
//----------------------------------------------------------------
    private Set addModSymsSet( Set pModSyms) {
        Set lModSyms = new HashSet();
        for (Iterator lt = pModSyms.iterator(); lt.hasNext();) {
                Sym lSym = (Sym)lt.next();
               lModSyms.add(lSym);
       }
       return lModSyms;
    }

    //##71 BEGIN
    public boolean hasCallWithSideEffect()
    {
      return fCallWithSideEffectIncluded;
    }
    //##71 END

} // SetRefReprHirEImpl
