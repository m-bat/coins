/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.flow;

import java.util.ArrayList;
import java.util.HashMap; //##70
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import coins.HirRoot;
import coins.SourceLanguage;
//##60 import coins.aflow.util.FlowError;
import coins.ir.IR;
import coins.ir.hir.AsmStmt; //##70
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
import coins.sym.Var; //##57
import coins.ir.hir.BlockStmt; //##25
import coins.ir.hir.ConstNode; //##25
import coins.ir.hir.HirList; //##25
import coins.ir.hir.JumpStmt; //##25
import coins.ir.hir.LabeledStmt; //##25
import coins.ir.hir.LoopStmt; //##25
import coins.ir.hir.NullNode; //##25
import coins.ir.hir.ReturnStmt; //##25
import coins.ir.hir.SymNode; //##62
import coins.ir.hir.VarNode; //##62
import coins.sym.ExpId; //##25
import coins.sym.VectorType; //##25
import coins.sym.StructType; //##25
import coins.sym.UnionType; //##25
import coins.casttohir.ParseString; //##70

//##25 : SetRefRepsHirImpl is extended to SetRefReprHirEImpl on Sep. 2003 by Tan
//     to do optimization for array elements.

/**
 * Implementation of the SetRefRepr interface for HIR
 */
public class
  SetRefReprHirEImpl
  extends SetRefReprHirImpl
  implements SetRefRepr
{
  //======= From aflow.SetRefReprHirImpl ==================
  protected Stmt fStmt; //##25
  public final HirRoot hirRoot;
  public final Flow flow;
  //=========================
  // Fields inherited from SetRefReprHirImpl
  //   Stmt fStmt;
  //   public final HirRoot hirRoot;
  //   public final Flow flow;

  // Fields inherited from SetRefReprImpl:
  //   public final FlowRoot flowRoot;
  //   public final SymRoot symRoot;
  //   protected IR fIR;
  //   protected List fUseNodeList;
  //    Not used in original aflow and opt.
  //   protected List fUseExpIdList;
  //    Not used in original aflow and opt.
  //   protected int fOpCode;
  //   FlowResults fResults;
  //   protected FlagBox fFlags = new FlagBoxImpl();
  //   protected BBlock fBBlock;
  //   protected FlowAnalSym fDefSym;

  /** SetRefReprHirEImpl instanciates SetRefRepr for the subtree pSubtree
   *  by computing SetRefRepr for child subtrees.
   *  For leaf children, SetRefRepr is not created.
   *  Required information fSetRefReprTable, fIrIndexMin, fIrIndexMax,
   *  and each item of fSetRefReprTable are set by HirSubpFlowImpl
   * (and LirSubpFlowImpl).
   * DataFlowImpl.recordSetRefRepr() eventually instanctiates this.
   * @param pSubtree HIR subtree for which SetRefRepr is to be computed.
   * @param pBBlock Basic block containing pSubtree.
   * @param pDef true if pSubtree defines a symbol,
   *    false otherwise.
   */
  public
    SetRefReprHirEImpl(HIR pSubtree, BBlock pBBlock, boolean pDef,Set pModSymsStmt)
  {
    //##62 super(pSubtree, pBBlock);
    super((IR)pSubtree, ((BBlockImpl)pBBlock).flowRoot.fSubpFlow,
        ((BBlockImpl)pBBlock).flowRoot.fSubpFlow.getExpId(pSubtree));

    // flow = flowRoot.aflow;
    flow = flowRoot.flow; //##60
    hirRoot = flowRoot.hirRoot;
    // fStmt = pSubtree;
    // fOpCode = pSubtree.getOperator();
    FlowAnalSym lSym = pSubtree.getSymOrExpId();
    if (lSym instanceof ExpId) { // Set ExpId by this node.
      fFlags.setFlag(SETS, true);
      fDefSym = lSym;
    }
    //##70 if (pSubtree instanceof AssignStmt)
    if ((pSubtree instanceof AssignStmt)||  //##70
        (pSubtree instanceof AsmStmt)) {    //##70
      fModSymsStmt = new HashSet(); // Make new set for this statement.
    }else
      fModSymsStmt = pModSymsStmt; // Use the inherited set.
    // Compute set/ref information of the subtree fIR.
    computeSetRefRepr((HIR)fIR, pBBlock, pDef, fModSymsStmt);
    //##60 flowRoot.aflow.getSubpFlow().setSetRefReprOfIR(this, pSubtree.getIndex());
    //##62 computeSetRefReprOfSubtree((HIR)fIR, pDef, fModSymsStmt); //##62
    //##62 BEGIN
    ExpId lExpId = fSubpFlow.getExpId(pSubtree);
    if (lExpId != null) { // Record SetRefRepr for ExpId.
      //##63 lExpId.getExpInf().setSetRefRepr(this);
      lExpId.setSetRefRepr(this); //##63
    }
    //##62 END
    if (fDbgLevel > 3) {
      flow.dbg(4, "SetRefReprHirEImpl " + fIR.toStringShort()
               + "\n   defSym ", fDefSym);
      flow.dbg(4, "  modSyms ", fModSyms);
      flow.dbg(4, "  leafOperands ", fLeafOperands);
      //##78 flow.dbg(4, "  operandExp ", fOperandExp);
      flow.dbg(4, "  operandExp ", fSubpFlow.sortExpIdCollection(fOperandExp)); //##78
      //##78 flow.dbg(4, "  allSubexp ", fAllSubexps);
      flow.dbg(4, "  allSubexp ", fSubpFlow.sortExpIdCollection(fAllSubexps)); //##78
      if (fModSymsStmt != null)
        flow.dbg(4, "  modSymsStmt ", fModSymsStmt);
      // flow.dbg(6, "  modSymsAll ", fModSymsAll);
      // flow.dbg(6, "  operandsAll ", fOperandsAll);
    }
  } // SetRefReprHirEImpl

  /** computeSetRefRepr
   * Compute defined symbols (fDefSym) and set of possibly modified variables
   * (fModSyms), variables used as leaf operand (fLeafOperands).
   * for the HIR subtree pHir within the basic block pBBlock.
   * Fields fDefSym, fModSyms, fLeafOperands are computed and
   * and SETS flag of fFlags are set true if some symbol is defined.
   * The branchs outside pBBlock are not processed.
   * Traverse should be in the sequence of evaluation at execution time.
   * @param pHir subtree to be processed.
   * @param pBBlock basic block within which variables are scanned.
   * @param pDef true if traversing in define mode, false otherwise.
   * @param pModSymsStmt set of symbols whose value may be modified
   *   by the simple statement containing pHir.
   */
  protected void
    computeSetRefRepr(HIR pHir, BBlock pBBlock, boolean pDef,
    Set pModSymsStmt)
  {
    boolean lDef;
    if (pHir == null)
      return;
    Set lSet; //##71
    if (fDbgLevel > 3)
      flow.dbg(5, " computeSetRefRepr ", pHir.toStringShort()
               + " pDef " + pDef + " pModSymsStmt " + pModSymsStmt);
    if (pModSymsStmt != null)
      fModSymsStmt = pModSymsStmt;
    FlowAnalSym lSym = pHir.getSymOrExpId();
    if (lSym != null) {
      if (lSym instanceof ExpId) {
        if (pDef) { //##65
          fFlags.setFlag(SETS, true); // ExpId is set by this node
          fDefSym = lSym;
        }
        //##62 BEGIN
        ExpId lExpId = (ExpId)lSym;
        SetRefReprHirEImpl lSetRefReprOfExpId =
          //##63 (SetRefReprHirEImpl)lExpId.getExpInf().getSetRefRepr();
          (SetRefReprHirEImpl)lExpId.getSetRefRepr(); //##63
        if (lSetRefReprOfExpId != null) {
          // SetRefRepr is already computed.
          if (fDbgLevel > 3)
            flow.dbg(5, " already computed " + lExpId.getName());
          fCallWithSideEffectIncluded = fCallWithSideEffectIncluded |
            lSetRefReprOfExpId.fCallWithSideEffectIncluded;
          fLeafOperands.addAll(lSetRefReprOfExpId.fLeafOperands);
          fModSyms.addAll(lSetRefReprOfExpId.fModSyms);
          if (pModSymsStmt != null)
            pModSymsStmt.addAll(fModSyms);
          fAllSubexps.addAll(lSetRefReprOfExpId.fAllSubexps);
          return;
        }
        //##62 END
        //##62 ((BBlockHirImpl)pBBlock).addToExpNodeList((ExpId)lSym, pHir);
        // addToExpNodeList is done in setExpId of SubpFlowImpl. //##62
      }
      else if (lSym.isGlobal()) { //##57
        // Global variable reference is treated by common subexp elimination.
        //##62 ((BBlockHirImpl)pBBlock).addToExpNodeList(pHir.getExpId(), pHir); //##57
      } //##57
    }
    switch (pHir.getOperator()) {
      case HIR.OP_ASSIGN:
        HIR lLHS = (HIR)fIR.getChild1(); // Left-hand-side of AssignStmt.
        fDefSym = lLHS.getSymOrExpId();
        if (fDbgLevel > 3)
          flow.dbg(5, " defSym " + fDefSym + " " + fSubpFlow.getExpId(lLHS)); //##65
        if (fDefSym != null) {
          fModSyms.add(fDefSym);
          //##53 BEGIN
          fModSymsStmt.add(fDefSym); // Same effect as pModSymsStmt.add(fDefSym)
          if (pHir.getType().getTypeKind() == Type.KIND_POINTER) {
            // Pointer assignment. REFINE
            //##60 if (flow.fSubpFlow.setOfAddressTakenVariables() != null)
            if (flowRoot.fSubpFlow.setOfAddressTakenVariables() != null) //##60
              //##60 fModSymsStmt.addAll(flow.fSubpFlow.setOfAddressTakenVariables());
              fModSymsStmt.addAll(flowRoot.fSubpFlow.
                setOfAddressTakenVariables()); //##60
          }
          //##53 END
          //##60 BEGIN
          else if (lLHS.getOperator() == HIR.OP_CONTENTS) {
            HIR lLvalue = (HIR)lLHS.getChild1();
            /* //##71
            FlowAnalSym lLhsSym = lLvalue.getSymOrExpId();
            if (lLhsSym != null) {
              fModSyms.add(lLhsSym);
              fModSymsStmt.add(lLhsSym);
            }
            */ //##71
            lSet = symsModifiedForLhsExp((Exp)lLvalue); //##71
            fModSyms.addAll(lSet);      //##71
            fModSymsStmt.addAll(lSet);  //##71
          }
          //##60 END
        }
        // Process the right-hand-side (r-value)
        processTheChild((HIR)fIR.getChild2(), pBBlock, false, fModSymsStmt); //##53
        if (lLHS.getChildCount() > 0)
          processTheChild(lLHS, pBBlock, true, fModSymsStmt);
        break;
      case HIR.OP_BLOCK:
        for (Stmt lStmt = ((BlockStmt)pHir).getFirstStmt();
             lStmt != null;
             lStmt = lStmt.getNextStmt()) { // Process statements.
          if (fDbgLevel > 3)
            flow.dbg(4, " next subtree in BlockStmt in computeSetRefRepr",
              lStmt.toStringShort()); //##62
          // If LabeledStmt, then the basic block is terminated.
          if (lStmt instanceof LabeledStmt) //##62
            break; //##62
          processTheChild(lStmt, pBBlock, false, fModSymsStmt);
          if ((lStmt instanceof JumpStmt) ||
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
        if ((lOpCode != HIR.OP_ADDR) && (lOpCode != HIR.OP_SUBP))
          processTheChild((HIR)pHir.getChild1(), pBBlock, false, fModSymsStmt);
        if (pHir.getChild2()instanceof HirList) { // Process parameters.
          for (Iterator lIterator = ((HirList)pHir.getChild2()).iterator();
               lIterator.hasNext(); ) {
            HIR lArg = (HIR)lIterator.next();
            if ((lArg != null) && (!(lArg instanceof NullNode)))
              processTheChild(lArg, pBBlock, false, fModSymsStmt);
          }
        }
        // Global variable modification should be treated
        // in CommomSubexpElim and PRE.
        /* //##71
        Set lGlobalVariables = fBBlock.getSubpFlow().setOfGlobalVariables();
        if (lGlobalVariables != null) {
          fModSyms.addAll(lGlobalVariables);
          //##57 BEGIN
          //##62for (Iterator lIteratorG = lGlobalVariables.iterator();
          //##62     lIteratorG.hasNext(); ) {
          //##62  Var lVarG = (Var)lIteratorG.next();
          //##62}
          //##57 END
        }
        */ //##71
       //##71 BEGIN
       boolean lWithSideEffect = false; //##71
       SymNode lSubpNode = null;
       if (pHir.getChild1() instanceof SymNode)
         lSubpNode = (SymNode)pHir.getChild1();
       else if ((pHir.getChild1().getOperator() == HIR.OP_ADDR)&&
                (pHir.getChild1().getChild1() instanceof SymNode))
         lSubpNode = (SymNode)pHir.getChild1().getChild1();
       if ((lSubpNode == null)||
           (! symRoot.sourceLanguage.functionsWithoutSideEffect.
            contains(lSubpNode.getSymNodeSym().getName()))) {
         fCallWithSideEffectIncluded = true;
         lWithSideEffect = true;
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
            &&lWithSideEffect) {        //##71
          for (Iterator lIter2 = fLeafOperands.iterator();
               lIter2.hasNext(); ) {
            Sym lLeafSym = (Sym)lIter2.next();
            if ( // Aggregates may be modified ?
                (lLeafSym.getSymType()instanceof VectorType) ||
                (lLeafSym.getSymType()instanceof StructType) ||
                (lLeafSym.getSymType()instanceof UnionType) ||
                (lLeafSym.getFlag(Sym.FLAG_ADDRESS_TAKEN))) {
              fModSyms.add(lLeafSym);
            }
          }
        }
        break;
      }
      //##70 BEGIN
      case HIR.OP_ASM: {
        ConstNode lChild1 = (ConstNode)pHir.getChild1();
        HirList lExpList = (HirList)pHir.getChild2();
        String lInstructions = lChild1.getSymNodeSym().getName();
        if ((lInstructions.length() >= 7)&&
            (lInstructions.substring(1, 7).intern()== "#param")) {
          Set lDelimiters = ParseString.fromStringToSet(",()[]{}+-*/:;%$#&@!~|<>?^\n");
          Set lSpaces = ParseString.fromStringToSet(" \t");
          Set lIdSpChars = ParseString.fromStringToSet("_");
          String lParameterPart = lInstructions.substring(7);
          if (fDbgLevel > 3) {
            flow.dbg(4, "ASM(#param", lParameterPart +
              " " + lExpList.toStringWithChildren() + ")");
            flow.dbg(5, "delim " + lDelimiters + " IdSp " + lIdSpChars);
          }
          ParseString lParseString = new ParseString(lParameterPart,
            lDelimiters, lSpaces, lIdSpChars);
          char[] lAccessMark = new char[100];
          // lAccessMark indicates access mode of parameters by
          //   'r': read, 'w': write, 'm': read and write.
          boolean lAccessPrefix = true; // Access prefix is expected.
          int lIndex = 0;
          for ( ; lParseString.hasNext()&&(lIndex < 100); ) {
            String lItem = lParseString.getNextToken();
            if (lItem == "\n") {
              break;
            }else if (lItem == ",") {
               lIndex++;
               lAccessPrefix = true;
            }else if (lItem == "w") {
              if (lAccessPrefix) {
                lAccessMark[lIndex] = 'w';
              }
              lAccessPrefix = false;
            }else if (lItem == "m") {
              if (lAccessPrefix) {
                lAccessMark[lIndex] = 'm';
              }
              lAccessPrefix = false;
           }else {
             if (lAccessPrefix) {
               lAccessMark[lIndex] = 'r';
             }
             lAccessPrefix = false;
           }
          } // End of parameter-loop.
          if (fDbgLevel > 3) {
            flow.dbg(4, "\n lAccessMark");
            for (int i = 0; i <= lIndex; i++) {
              flow.dbg(4, " " + lAccessMark[i]);
            }
          }
          if (((SubpFlowImpl)fSubpFlow).fMultipleSetRef == null) {
            ((SubpFlowImpl)fSubpFlow).fMultipleSetRef = new HashMap();
          }
          HirList lRefNodes = hirRoot.hir.hirList(); // Refered or modified operands.
          HirList lSetNodes = hirRoot.hir.hirList(); // Write or modified operands.
          HirList lWriteNodes = hirRoot.hir.hirList(); // Write operands.
          int lExpIndex = 0;
          for (Iterator lIter = lExpList.iterator();
               lIter.hasNext(); lExpIndex++) {
            HIR lHir = (HIR)lIter.next();
            if ((lAccessMark[lExpIndex] == 'w')||
                (lAccessMark[lExpIndex] == 'm')) {
              // Write access.
              lSetNodes.add(lHir);
              if (lAccessMark[lExpIndex] == 'w')
                lWriteNodes.add(lHir);
              fDefSym = lHir.getSymOrExpId();
              if (fDbgLevel > 3) {
                flow.dbg(4, lHir.toStringShort() +
                  " defSym=" + fDefSym + " " + fSubpFlow.getExpId(lHir));
              }
              if (fDefSym != null) {
                fModSyms.add(fDefSym);
                fModSymsStmt.add(fDefSym); // Same effect as pModSymsStmt.add(fDefSym)
                if (lHir.getType().getTypeKind() == Type.KIND_POINTER) {
                  // Pointer assignment. REFINE
                  if (flowRoot.fSubpFlow.setOfAddressTakenVariables() != null)
                    fModSymsStmt.addAll(flowRoot.fSubpFlow.
                      setOfAddressTakenVariables());
                }
                else if (lHir.getOperator() == HIR.OP_CONTENTS) {
                  HIR lLvalue = (HIR)lHir.getChild1();
                  FlowAnalSym lLhsSym = lLvalue.getSymOrExpId();
                  if (lLhsSym != null) {
                    fModSyms.add(lLhsSym);
                    fModSymsStmt.add(lLhsSym);
                  }
                }
              }
              if (lHir.getChildCount() > 0)
                processTheChild(lHir, pBBlock, true, fModSymsStmt);
            }
            if (lAccessMark[lExpIndex] != 'w') {
              // Process as r-value
              lRefNodes.add(lHir);
              processTheChild(lHir, pBBlock, false, fModSymsStmt); //##53
            }
          } // End of ExpList iteration.
          HIR lSeq = hirRoot.hir.hirSeq(lRefNodes, lSetNodes, lWriteNodes);
          ((SubpFlowImpl)fSubpFlow).fMultipleSetRef.put(pHir, lSeq);
          if (fDbgLevel > 3) {
            flow.dbg(4, "map " + pHir.toStringShort(), "to " +
                lSeq.toStringWithChildren());
          }
        }else {
          // No access infromation is given.
          if (fDbgLevel > 1)
            flow.dbg(2, "No param is given in", pHir.toStringWithChildren()); //##53
        }
        break;
      }
      //##70 END
      case HIR.OP_ARROW: // Child1 is use only.
        processTheChild((HIR)pHir.getChild1(), pBBlock, false, fModSymsStmt);
        processTheChild((HIR)pHir.getChild2(), pBBlock, pDef, fModSymsStmt);
        if (pDef) // Add the set of address taken variables.
          if (fBBlock.getSubpFlow().setOfAddressTakenVariables() != null)
            fModSyms.addAll(fBBlock.getSubpFlow().setOfAddressTakenVariables());
        break;
        //##25 BEGIN
      case HIR.OP_QUAL: // Child1 is use only.
        processTheChild((HIR)pHir.getChild1(), pBBlock, false, fModSymsStmt);
        processTheChild((HIR)pHir.getChild2(), pBBlock, pDef, fModSymsStmt);
        break;
        //##25 END
      case HIR.OP_CONTENTS:

        //##53 BEGIN
        processTheChild((HIR)pHir.getChild1(), pBBlock, pDef, fModSymsStmt);
        if ((pDef) &&
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
        if (pDef &&
            (pHir.getChild1().getOperator() == HIR.OP_DECAY)) {
          processTheChild((HIR)pHir.getChild1(), pBBlock, pDef, fModSymsStmt);
          processTheChild((HIR)pHir.getChild2(), pBBlock, false, fModSymsStmt);
        }
        else {
          processTheChild((HIR)pHir.getChild1(), pBBlock, false, fModSymsStmt);
          processTheChild((HIR)pHir.getChild2(), pBBlock, false, fModSymsStmt);
        }
        break;
      case HIR.OP_SUBS: // Child2 is use only.
        processTheChild((HIR)pHir.getChild1(), pBBlock, pDef, fModSymsStmt);
        processTheChild((HIR)pHir.getChild2(), pBBlock, false, fModSymsStmt);
        break;
      case HIR.OP_IF: // Process condition part only.
        processTheChild(((IfStmt)pHir).getIfCondition(), pBBlock, false,
          fModSymsStmt);
        break;
        //## case HIR.OP_LOOP: // Process InitPart and ConditionalInitPart only.
      case HIR.OP_FOR:
      case HIR.OP_WHILE:
      case HIR.OP_UNTIL:
        processTheChild(((LoopStmt)pHir).getLoopInitPart(), pBBlock, false,
          fModSymsStmt);
        processTheChild(((LoopStmt)pHir).getConditionalInitPart(), pBBlock, false,
          fModSymsStmt);
        break;
      case HIR.OP_LABELED_STMT: // Process statement body only.
        processTheChild((HIR)pHir.getChild2(), pBBlock, false, fModSymsStmt);
        break;
      default:
        lSym = pHir.getSymOrExpId();
        if (pHir.getChildCount() == 0) {
          if (lSym != null) {
            if (pDef)
              fModSyms.add(lSym); // The symbol is defined.
            else
              fLeafOperands.add(lSym); // The symbol is used as operand.
          }
        }
        else { // With children
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
      if (fDbgLevel > 3)
        flow.dbg(5, " fModSymsStmt", fModSymsStmt.toString()); //##53
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
    processTheChild(HIR pHir, BBlock pBBlock, boolean pDef,
                    Set pModSymsStmt)
  {
    SetRefReprHirEImpl lSetRefRepr;

    if (pHir == null)
      return;
    if (fDbgLevel > 3)
      flow.dbg(5, "  processTheChild ", pHir + " " + pDef);
    //##62 BEGIN
    if (pHir instanceof VarNode) {
      Var lVar = ((VarNode)pHir).getVar();
      if (pDef)
        fModSyms.add(lVar);
      else
        fLeafOperands.add(lVar);
    }else {
      if (pHir instanceof SymNode) // ConstNode, etc.
        return;
      ExpId lExpId = fSubpFlow.getExpId(pHir);
      if (lExpId != null) {
        SetRefReprHirEImpl lSetRefReprOfExpId =
          //##63 (SetRefReprHirEImpl)lExpId.getExpInf().getSetRefRepr();
          (SetRefReprHirEImpl)lExpId.getSetRefRepr(); //##63
        if (lSetRefReprOfExpId != null) {
          if (fDbgLevel > 3)
            flow.dbg(5, " already computed " + lExpId.getName()
              + " modSyms " + lSetRefReprOfExpId.fModSyms);
          fCallWithSideEffectIncluded = fCallWithSideEffectIncluded |
            lSetRefReprOfExpId.fCallWithSideEffectIncluded;
          fLeafOperands.addAll(lSetRefReprOfExpId.fLeafOperands);
          fModSyms.addAll(lSetRefReprOfExpId.fModSyms);
          if (pModSymsStmt != null)
            pModSymsStmt.addAll(fModSyms);
          fAllSubexps.addAll(lSetRefReprOfExpId.fAllSubexps);
          return;
        }
      }
      //##62 END
      lSetRefRepr = new SetRefReprHirEImpl(
        (HIR)pHir, pBBlock, pDef, pModSymsStmt);
      fCallWithSideEffectIncluded = fCallWithSideEffectIncluded
        | lSetRefRepr.fCallWithSideEffectIncluded;
      fLeafOperands.addAll(lSetRefRepr.fLeafOperands);
      fModSyms.addAll(lSetRefRepr.fModSyms);
      fAllSubexps.addAll(lSetRefRepr.fAllSubexps);
      if (pDef) {
        if (pHir.getOperator() == HIR.OP_UNDECAY)
          fModSyms.add(pHir.getChild1().getSym());
      }
      else {
        Sym lSym = pHir.getSymOrExpId();
        if (lSym instanceof ExpId) {
          //##65 ((BBlockHirImpl)pBBlock).addToExpNodeList((ExpId)lSym, pHir);
          // addToExpNodeList is called from selectExpId of SubpFlowImpl. //##65
          fOperandExp.add(lSym);
        }
      }
    }
  } // processTheChild

//##62 BEGIN

//##62 END
/** //##97
  // Use getStmt() of SetRefReprHirImpl. //##97
  public Stmt
    getStmt() //##25
  {
    if (fIR instanceof Stmt)
      return fStmt;
    else
      return ((HIR)fIR).getStmtContainingThisNode();
  }
**/ //##97
  
  public IR
    defNode() //##25
  {
    if (fOpCode == HIR.OP_ASSIGN) {
      return getIR().getChild1();
    }
    else {
      if (fDefSym != null)
        return fIR;
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
    //##60 flowRoot.aflow.dbg(6, " modSyms" + fModSyms.toString());
    if (fDbgLevel > 3)
      flowRoot.flow.dbg(6, " modSyms" + fModSyms.toString()); //##60
    return fModSyms;
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
      //##60 flowRoot.aflow.dbg(5, " modSymsStmt()" + lModSymsStmt.toString());
    return lModSymsStmt;
  } // modSymsStmt

  /** modSyms0 returns the set of symbols that are possibly defined
   * in this SetRefRepr as modSyms() and symbols externally defined
   * (i.e. via external calls) if call is included.
   * @return the set of symbols that are possibly defined
   *     including external symbols if call is included.
   **/
  public Set
    modSyms0()
  {
    Set lModSyms = fModSyms;
    if (fCallWithSideEffectIncluded)
      lModSyms.addAll(fBBlock.getSubpFlow().setOfGlobalVariables());
      //##60 flowRoot.aflow.dbg(5, "modSyms ", lModSyms.toString());
    if (fDbgLevel > 3)
      flowRoot.flow.dbg(5, "modSyms0 ", lModSyms.toString()); //##60
    return lModSyms;
  } // modSyms0

  /** leafOperands
   *  Get the set of symbols used as operands gathered from
   *  all leaf nodes of the subtree corresponding to this.
   *  ExpId's for intermediate results are excluded.
   **/
  public Set
    leafOperands() //##25
  {
    return fLeafOperands;
  }

  /** operandExp
   * ExpId's of subexpressions used as direct operands
   * by the node corresponding to this.
   * Leaf operands are excluded.
   **/
  public Set
    operandExp() //##25
  {
    return fOperandExp;
  }

  /** operandExp
   * ExpId's of all subexpressions of the subtree corresponding to this.
   * Leaf operands are excluded.
   **/
  public Set
    allSubexps() //##25
  {
    return fAllSubexps;
  }

  //##71 public boolean hasCall() //##60
  public boolean hasCallWithSideEffect() //##71
  {
    return fCallWithSideEffectIncluded;
  }

} // SetRefReprHirEImpl
