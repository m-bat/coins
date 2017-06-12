/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.opt;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import coins.FlowRoot; //##60
//##60 import coins.aflow.AssignFlowExpId;
import coins.flow.BBlockSubtreeIterator; //##60
//##60 import coins.aflow.FlowExpId;
//##60 import coins.aflow.FlowResults;
import coins.flow.SetRefRepr; //##60
import coins.flow.SetRefReprHirEImpl; //##60
import coins.flow.SetRefReprList; //##60
import coins.flow.SubpFlow; //##60
import coins.flow.SubpFlowImpl; //##68
import coins.alias.RecordAlias; //##56
import coins.ir.IR;
import coins.ir.hir.AssignStmt;
import coins.ir.hir.Exp;
import coins.ir.hir.HIR;
import coins.ir.hir.VarNode;
import coins.sym.FlowAnalSym;
import coins.sym.Type;
import coins.sym.Var;
import coins.MachineParam; //##25
import coins.flow.BBlock; //##60
import coins.flow.BBlockHir; //##60
import coins.flow.BBlockHirImpl; //##60
import coins.flow.BBlockNodeIterator; //##57
//##60 import coins.flow.HashBasedFlowExpId; //##57
//##60 import coins.flow.SetRefReprHirEImpl; //##25
import coins.alias.AliasAnal; //##55
import coins.alias.RecordAlias; //##25
import coins.ir.hir.AssignStmt; //##25
import coins.ir.hir.BlockStmt; //##25
import coins.ir.hir.ExpStmt; //##25
import coins.ir.hir.FunctionExp; //##25
import coins.ir.hir.HirIterator; //##25
import coins.ir.hir.HirList; //##25
import coins.ir.hir.IfStmt; //##57
import coins.ir.hir.LabeledStmt; //##25
import coins.ir.hir.LoopStmt; //##57
import coins.ir.hir.SymNode;  //##82
import coins.ir.hir.Stmt; //##25
import coins.ir.hir.SubpDefinition; //##25
import coins.ir.hir.SwitchStmt; //##57
import coins.sym.EnumType; //##25
import coins.sym.ExpId; //##25
import coins.sym.ExpIdImpl; //##25
import coins.sym.PointerType; //##25
import coins.sym.Sym; //##25
import java.util.ArrayList; //##57
import java.util.HashMap; //##25
import java.util.List; //##25

/**
 * This class performs common subexpresssion elimination operations that are specific to HIR.
 */
public class CommonSubexpElimHirE
  extends CommonSubexpElimHir
{

  //##60 protected SubpFlow fSubpFlow;

  protected AliasAnal //##55
    fAlias;

  protected RecordAlias
    fRecordAlias;

  /** fAvailable
   *  Set of ExpId's for currently available expressions in BBlock . */
  protected Set
    fAvailableExps;

  /** fAssigned:
   *  Set of available temporal variables in BBlock. */
  protected Set
    fAvailableTemps;

  protected Set
    fReplacedNodes;

  //##78 BEGIN
  // Map recording latest expression node for each ExpId.
  protected Map
    fLatestNodeForExpId;
  //##78 END

  protected int[]
    fExpCost; // Cost estimation for each node indexed by getIndex().

  protected int
    fIndexMin, fIndexMax;

  protected boolean //##25-1
    fBeforePRE; // True if CSE before PRE.

  /** fThreshold
   * Replacement threshold.
   * Expressions with cost less than fThreshold are not
   * replaced by temporal but recomputed each time.
   */
  protected int
    fThreshold;

  public
//##60 CommonSubexpElimHirE(FlowResults pResults, int pThreshold )
    CommonSubexpElimHirE(FlowRoot pFlowRoot, int pThreshold) //##60
  {
    //##60 super(pResults);
    super(pFlowRoot); //##60
    if (fDbgLevel > 0) //##58
      flowRoot.ioRoot.dbgOpt1.print(1, "\nCommonSubexpElimHirE instantiation " +
        flowRoot.subpUnderAnalysis.toStringShort() +
        " threshold " + pThreshold + "\n");
    fGlobalExpTempMap = new HashMap(); //##25-1
    fGlobalTempExpMap = new HashMap(); //##57
    fThreshold = pThreshold; //##56
  } // CommonSubexpElimHirE

  /** Constructor CommonSubexpElimHirE
   * @param pResults   instance of FlowResults.
   * @param pRecordAlias instance of RecordAlias
   * @param pBeforePRE true if invoked as the preparatory optimization
   *   for partial redundancy elimination, false otherwise.
   */
  public
    CommonSubexpElimHirE( //##60 FlowResults pResults,
    FlowRoot pFlowRoot, //##60
    //##62 RecordAlias pRecordAlias,
    int pThreshold, //##56
    boolean pBeforePRE) //##25-1
  {
    //##60 super(pResults);
    super(pFlowRoot); //##60
    //##56 fRecordAlias = pRecordAlias;
    if (fDbgLevel > 0) //##58
      flowRoot.ioRoot.dbgOpt1.print(1,
        "\nCommonSubexpElimHirE. threshold="
        + pThreshold + "\n");
    fBeforePRE = pBeforePRE; //##25-1
    fGlobalExpTempMap = new HashMap(); //##25-1
    fGlobalTempExpMap = new HashMap(); //##57
    fThreshold = pThreshold; //##56
  } // CommonSubexpElimHirE

  /**
   * Performs the common subexpression elimination within the given BBlock.
     * If call encountered,global variable values are assumed to be not available.
   * @return true if the underlying IR has changed (optimized).
   */
  public boolean
    doBBlockLocal(BBlock pBBlock)
  {
    if ((pBBlock == null)||(pBBlock.getBBlockNumber() == 0))
      return false; //##63
    //##60 SetRefReprList lSetRefReprs = (SetRefReprList)fResults.get("BBlockSetRefReprs", pBBlock);
    if (fDbgLevel > 0) { //##58
      flowRoot.ioRoot.dbgOpt1.print(2, "doBBlockLocal Extended ",
        pBBlock.toString());
      //##60 flowRoot.ioRoot.dbgOpt1.print(6, " lSetRefReprs ", lSetRefReprs.toString());
    }
    boolean lChanged = false; //##60
    boolean lEliminated = false;
//##25  Set lLeafOperands;
//##25  Set lDefined;
//##25  Set lKilled;
    Stmt lStmt;
    HIR lHir;
    //##60 fSubpFlow = pBBlock.getSubpFlow();
    fRecordAlias = fSubpFlow.getRecordAlias();
    // List up statements in pBBlock.
    // Do not use BBlockStmtIterator directly in restructuring loop.
    // If it is used, restructuring may conflict with the iterator.
    List lStmtList = new ArrayList(50);
    //##60 for (coins.aflow.BBlockSubtreeIterator lIterator = new coins.aflow.BBlockStmtIterator((BBlockHir) pBBlock);
    for (coins.flow.BBlockSubtreeIterator lIterator
         //##60 = new coins.flow.BBlockSubtreeIterator((BBlockHir) pBBlock);
         = new coins.flow.BBlockHirSubtreeIteratorImpl(flowRoot,
      (BBlockHir)pBBlock); //##62
         lIterator.hasNext(); ) { // Do for each statement.
      //##60 lStmt = (Stmt)lIterator.next();
      //##63 lStmt = (Stmt)((coins.flow.BBlockHirSubtreeIteratorImpl)lIterator).next(); //##62
      //##63 if (lStmt == null) continue;
      //##63 lStmtList.add(lStmt);
      lHir = (HIR)((coins.flow.BBlockHirSubtreeIteratorImpl)lIterator).next(); //##63
      if (lHir instanceof Stmt) //##63
        lStmtList.add((Stmt)lHir); //##63
    }
    //##57 fExpTempMap = new HashMap();
    fAvailableExps  = new HashSet();
    fAvailableTemps = new HashSet();
    fReplacedNodes  = new HashSet(); //##57
    fLatestNodeForExpId = new HashMap(); //##78
    for (Iterator lIterator = lStmtList.iterator();
         lIterator.hasNext(); ) { // Do for each statement.
      lEliminated = false; //##60
      lStmt = (Stmt)lIterator.next();
      if (lStmt == null)
        continue;
      if ((lStmt instanceof LabeledStmt) ||
          (lStmt instanceof BlockStmt)) {
        //##57 lStmt = ((LabeledStmt)lStmt).getStmt();
        if (fDbgLevel > 3) //##58
          flowRoot.ioRoot.dbgOpt1.print(4, "lapping Stmt", lStmt.toString()
            + " continue");
        continue;
      }
      if (fDbgLevel > 2) //##58
        flowRoot.ioRoot.dbgOpt1.print(3, "Stmt", lStmt.toString());
      if (lStmt instanceof AssignStmt) {
        if (lStmt.getChild1().getChildCount() > 0) { // LHS is not simple Var.
          // Try to eliminate subexpressions of the left hand side.
          lEliminated = tryToEliminateSubexp((HIR)lStmt.getChild1(), pBBlock,
            lStmt);
        }
        // Try to eliminate subexpressions of the right hand side.
        if (tryToEliminateExp((HIR)lStmt.getChild2(), pBBlock, lStmt))
          lEliminated = true;
        //##65 adjustAvailability(lStmt);
        //##78 adjustAvailability(lStmt, lStmt); //##65
        adjustAvailability(lStmt, lStmt, pBBlock); //##78
      }
      else { // Not AssignStmt
        // Try to eliminate common subexpressions in the children
        // of thte statement.
        //##60 lEliminated = tryToEliminateSubexp(lStmt, pBBlock, lStmt);
        lEliminated = tryToEliminateSubexp(lStmt, pBBlock, lStmt) | lEliminated; //##60
      }
      lChanged = lChanged | lEliminated; //##60
    }
    //##60 return lEliminated;
    return lChanged; //##60
  } // doBBlockLocal

  /** tryToEliminateExp //##25
   * Try to eliminate pExp or its subexpressions if their values
   * are already computed in pBBlock.
   * Adjust available expressions at exit.
   * @param pExp expression to be examined for elimination.
   * @param pBBlock basic block containing pExp.
   * @param pStmt statement containing pExp.
   * @return true if elimination is done, false if no elimination
   *  is done.
   */
  protected boolean //##25
    tryToEliminateExp(HIR pExp, BBlock pBBlock, Stmt pStmt)
  {
    //##78 boolean lEliminated;
    boolean lEliminated = false;
    if (pExp == null)
      return false;
    if (fDbgLevel > 3) //##58
      flowRoot.ioRoot.dbgOpt1.print(4, "tryToEliminateExp", pExp.toStringShort());
    if (pExp.getChildCount() == 0) {
      if (pExp instanceof HirList) {
        // Try to eliminate subexp of the list elements.
        lEliminated = tryToEliminateSubexp(pExp, pBBlock, pStmt);
      }else {
        // If the loading cost of global variable exceeds
        // the threshold, try to replace the variable by a temporal.
        if ((pExp.getOperator() == HIR.OP_VAR) &&
            (pExp.getIndex() >= fIndexMin) && // Exclude added Stmt.
            (pExp.getIndex() <= fIndexMax) &&
            (fExpCost[pExp.getIndex() - fIndexMin] > fThreshold)) {
          //##78 if (toBeExcluded(pExp))
          //##78   lEliminated = false;
          //##78 else {
          if (! toBeExcluded(pExp)) { //##78
            lEliminated = replaceAvailableExp(pExp, pBBlock, pStmt);
            //##78 return lEliminated;
          }
        }
        //##78 lEliminated = false;
      }
    }
    else {
      // Child count > 0. Try to replace this expression (pExp).
      //##78 if (toBeExcluded(pExp))
      //##78   lEliminated = false;
      //##78 else
      if (! toBeExcluded(pExp)) //##78
        lEliminated = replaceAvailableExp(pExp, pBBlock, pStmt);
      if (!lEliminated) {
        // pExp can not be eliminated.
        // Try to eliminate its subexpressions.
        lEliminated = tryToEliminateSubexp(pExp, pBBlock, pStmt);
      }
      //##65 adjustAvailability(pExp);
      //##78 adjustAvailability(pExp, pStmt); //##65
      adjustAvailability(pExp, pStmt, pBBlock); //##78
    }
    if (fDbgLevel > 3) //##78
      flowRoot.ioRoot.dbgOpt1.print(5, " IS " + pExp.toStringShort()
        + " CHANGED:" + lEliminated); //##78
    return lEliminated;
  } // tryToEliminateExp

  /** tryToEliminateSubexp //##25
   * Try to eliminate subexpressions of pExp if their values
   * are already computed in pBBlock.
   * If pExp is HirList, then try for each list element.
   * Availability adjustment is done for each subexpression
   * (not for pExp).
   * @param pExp expression whose subexpression are to be
   *   examined for elimination.
   * @param pBBlock basic block containing pExp.
   * @param pStmt statement containing pExp.
   * @return true if elimination is done, false if no elimination
   *  is done.
   */
  protected boolean //##25
    tryToEliminateSubexp(HIR pExp, BBlock pBBlock, Stmt pStmt)
  {
    boolean lChanged = false; //##60
    //##78 boolean lEliminated = false;
    if (pExp == null)
      return false;
    if (fDbgLevel > 3) //##58
      flowRoot.ioRoot.dbgOpt1.print(4, " tryToEliminateSubexp",
        pExp.toStringShort());
    if (pExp.getIndex() == 0) { //##52
      if (fDbgLevel > 3) //##58
        flowRoot.ioRoot.dbgOpt1.print(4,
          "Generated HIR in tryToEliminateSubexp " + pExp.toString());
      return false;
    }
    if (pExp instanceof HirList) {
      // Apply the elimination procedure to the elements of the list.
      for (java.util.ListIterator lIterator = ((HirList)pExp).iterator();
           lIterator.hasNext(); ) {
        Object lElem = lIterator.next();
        if (lElem instanceof Exp) {
          if (tryToEliminateExp((Exp)lElem, pBBlock, pStmt))
            lChanged = true;
            // If any one is changed then this expression is changed.
        }
      }
    }
    else if (pExp instanceof AssignStmt) { // Exclude LHS itself
      // but apply the elimination procedure to its components.
      Exp lLHS = (Exp)pExp.getChild1();
      if (tryToEliminateSubexp(lLHS, pBBlock, pStmt))
        lChanged = true;
      // Apply the elimination procedure to RHS.
      // If it is changed then pExp is changed.
      if (tryToEliminateExp((Exp)pExp.getChild2(), pBBlock, pStmt))
        lChanged = true;
    }
    else {
      // Non-assign statement.
      if (pExp.getChildCount() == 0) {
        // It may be list, etc.
        lChanged = tryToEliminateExp(pExp, pBBlock, pStmt);
      }
      else {
        for (int i = 1; i <= pExp.getChildCount(); i++) {
          HIR lChild = (HIR)pExp.getChild(i); // Process each child.
          if (lChild == null)
            continue;
          if (lChild.getIndex() == 0) { //##52
            if (fDbgLevel > 3) //##58
              flowRoot.ioRoot.dbgOpt1.print(4,
                "Generated HIR in tryToEliminateSubexp " + lChild.toString());
            continue;
          }
          if (fSubpFlow.getBBlockOfIR(lChild.getIndex()) == pBBlock) {
            if (tryToEliminateExp(lChild, pBBlock, pStmt))
              lChanged = true;
              // If any one of children is changed then pExp is changed.
          }
        }
      }
    }
    return lChanged;
  } // tryToEliminateSubexp

  /** Expressions to be excluded from replacement are
   *  FunctionExp, List, comparison expression, undecay,
   *  BlockStmt, left hand side of AssignStmt, and
   *  expressions whose type is vector/struct/union/Subp.
   * @return true if pHir should be excluded from elimination
   *   else return false.
   */
  boolean
    toBeExcluded(HIR pHir)
  {
    boolean lExclude; //##65
    if (fDbgLevel > 3) //##58
      flowRoot.ioRoot.dbgOpt1.print(4, " toBeExcluded",
        pHir.toStringShort());
    HIR lHir = pHir; //##60
    HIR lParent = (HIR)pHir.getParent(); //##60
    int lOpCode = pHir.getOperator();
    if (lOpCode == HIR.OP_EXP_STMT) {
      lHir = ((ExpStmt)pHir).getExp(); //##60
      lOpCode = lHir.getOperator();
    }
    if ((lHir.getChildCount() == 0)&&
        (! (lHir instanceof VarNode))) {
      //##65 return true;
      lExclude = true; //##65
    }else if (lHir instanceof FunctionExp) {
       //##82 BEGIN
       // Exclude FunctionExp if the function has side effect.
       Sym lSubpSym = null;
       SymNode lSubpNode = ((FunctionExp)lHir).getFunctionNode();
       if (lSubpNode != null)
         lSubpSym = lSubpNode.getSymNodeSym();
       if ((lSubpSym != null)&&
            fFunctionsWithoutSideEffect.contains(lSubpSym.getName().intern())) {
          lExclude = false;
        }else
          lExclude = true;
      //##82 END
    }else if ((lHir instanceof List) || // Exclude List
        // Exclude compare exp in IfStmt, LoopStmt
        ((lOpCode >= HIR.OP_CMP_EQ) &&
         (lOpCode <= HIR.OP_CMP_LE)) ||
        (lOpCode == HIR.OP_DECAY) ||   // Exclude DECAY
        (lOpCode == HIR.OP_UNDECAY) || // Exclude UNDECAY
        (lOpCode == HIR.OP_CONTENTS) || // Contents may cause side effect //##60
        (lOpCode == HIR.OP_BLOCK) ||   // Exclude BlockStmt
        (lParent.getOperator() == HIR.OP_ADDR)|| //##60
        (lParent.getOperator() == HIR.OP_CONTENTS) || //##60
        ((lHir instanceof VarNode)&& // variable with high cost //##60
         (lHir.getType() instanceof PointerType))) {
      //##65 return true;
      lExclude = true; //##65
    }else {
      //##53 BEGIN
      // Exclude left hand side of AssignStmt.
      if ((pHir.getParent()instanceof AssignStmt) &&
          (pHir.getParent().getChild1() == pHir)) {
        //##65 return true;
        lExclude = true; //##65
        //##53 END
      }
      else {
        Type lType = pHir.getType();
        if (lType.isBasicType() ||
            (lType instanceof PointerType) ||
            (lType instanceof EnumType)) {
          if (containsCall(pHir)) //##55
            //##65 return true; //##55
            lExclude = true; //##65
          else
            //##65 return false;
            lExclude = false; //##65
        }
        else // Vector/Struct/Union/Subp/etc.
          //##65 return true;
          lExclude = true; //##65
      }
    }
    if (fDbgLevel > 3) //##65
      flowRoot.ioRoot.dbgOpt1.print(4, " " + lExclude); //##65
    return lExclude; //##65
  } // toBeExcluded

  public boolean
    containsCall(HIR pHir) //##55
  {
    for (HirIterator lIterator = pHir.hirIterator(pHir);
         lIterator.hasNext(); ) {
      HIR lHir = lIterator.next();
      if ((lHir != null) && (lHir.getOperator() == HIR.OP_CALL))
        return true;
    }
    return false;
  } // containsCall

  /** adjustAvailability
   *  Add pExp and all its subexpressions to fAvailableExps.
   *  If pExp modifies some variable, then remove all expressions
   *  using it as a leaf operand from fAvailableExps; and
   *  remove the variable from fAvailableTemps.
   *  @param pExp Expression to be adjusted.
   *  @param pStmt Statement including pExp. //##65
   */
  protected void
    //##65 adjustAvailability(HIR pExp)
    //##78 adjustAvailability(HIR pExp, Stmt pStmt ) //##65
    adjustAvailability(HIR pExp, Stmt pStmt, BBlock pBBlock ) //##78
  { // To be refined. This costs N1*N2*N3.
    if (fDbgLevel > 2) //##58
      flowRoot.ioRoot.dbgOpt1.print(3, "  adjustAvailability",
        pExp.toStringShort() + " in "
        + flowRoot.ioRoot.toStringObjectShort(pStmt));
    if (pExp.getIndex() == 0) { //##52
      if (fDbgLevel > 3) //##58
        flowRoot.ioRoot.dbgOpt1.print(4,
          " Generated HIR for adjustAvailability " + pExp.toString());
      return;
    }
    SetRefReprHirEImpl lSetRefRepr;
    //##62  = (SetRefReprHirEImpl)fSubpFlow.getSetRefReprOfIR(pExp.getIndex());
    //##65  = (SetRefReprHirEImpl)fSubpFlow.getSetRefReprOfIR(pExp); //##62
    lSetRefRepr  = (SetRefReprHirEImpl)fSubpFlow.getSetRefReprOfIR(pStmt); //##65
    if (lSetRefRepr == null) //##65
      lSetRefRepr  = (SetRefReprHirEImpl)fSubpFlow.getSetRefReprOfIR(pExp); //##65
    if (lSetRefRepr == null) { //##52
      if (fDbgLevel > 3) //##58
        flowRoot.ioRoot.dbgOpt1.print(4, " SetRefRepr is null " + pExp.toString());
      return;
    }
    if (fDbgLevel > 3) //##58
      flowRoot.ioRoot.dbgOpt1.print(4, "lSetRefRepr", lSetRefRepr
        +" modSyms " + lSetRefRepr.toString()+
        " modSymStmt " + lSetRefRepr.modSymsStmt()); //##65
    Set lModSyms = new HashSet();
    Set lModSymsAndAliases;
    Set lLeafOperands;
    Set lLeafOperandsAndAliases;
    Var lTempVar;
    // Escape from ConcurrentModificationException by "new".
    lModSyms.addAll(lSetRefRepr.modSyms());
    lModSyms.addAll(lSetRefRepr.modSymsStmt()); //##65
    if (fDbgLevel > 3) //##58
      flowRoot.ioRoot.dbgOpt1.print(4, " lModSyms " + lModSyms.toString());
    Set lAllSubexps = lSetRefRepr.allSubexps();
    if (lAllSubexps.size() > 0)
      fAvailableExps.addAll(lSetRefRepr.allSubexps());
    Set lAvailableExps = new HashSet();
    // Escape from ConcurrentModificationException by "new".
    lAvailableExps.addAll(fAvailableExps);
    if (lModSyms.size() > 0) {
      lModSymsAndAliases = fRecordAlias.aliasSymGroup(lModSyms);
      if (fDbgLevel > 3) { //##58
        flowRoot.ioRoot.dbgOpt1.print(4, "  modSymsAndAliases ",
          lModSymsAndAliases.toString());
        flowRoot.ioRoot.dbgOpt1.print(4, "  fGlobalExpTempMap ",
          fGlobalExpTempMap.toString());
      }
      for (Iterator lModIterator = lModSymsAndAliases.iterator();
           lModIterator.hasNext(); ) {
        Sym lSym = (Sym)lModIterator.next();
        for (Iterator lAvailIterator = fAvailableExps.iterator();
             lAvailIterator.hasNext(); ) {
          ExpId lExpId = (ExpId)lAvailIterator.next();
          if (lExpId == null)
            continue;
          ExpId lExpIdR = lExpId.getExpInf().getRValueExpId(); //##65
          if (lExpIdR != null) //##65
            lExpId = lExpIdR; //##65
          HIR lExp = (HIR)lExpId.getLinkedNode();
          if (lExp != null) {
            /* //##78
            if (fDbgLevel >= 5) {
              System.out.print("\n AvailCheck " + lExpId.toString()
                + " is killedBy " + lSym.toStringShort()); //##57
              flowRoot.ioRoot.dbgOpt1.print(5, " remove "
                  + lExpId.toStringShort()); //##78
            }
            */ //##78
            SetRefReprHirEImpl lExpSetRefRepr
              = (SetRefReprHirEImpl)fSubpFlow.getSetRefReprOfIR(lExp); //##62
              //##62 = (SetRefReprHirEImpl)fSubpFlow.getSetRefReprOfIR(lExp.getIndex());
            if (lExpSetRefRepr == null) { //##52
              if (fDbgLevel > 3) //##58
                flowRoot.ioRoot.dbgOpt1.print(4, " SetRefRepr is null for "
                  + lExp.toStringShort()
                  + " operandSet " + lExpId.getOperandSet() //##65
                  );
            //##65   continue;
            //##65 }
            //##65 lLeafOperands = lExpSetRefRepr.leafOperands();
            //##65 lLeafOperands.addAll(lExpSetRefRepr.modSyms()); // modSyms are also leaf operand.
            //##65 BEGIN
              lLeafOperands = lExpId.getOperandSet();
            }else {
              lLeafOperands = lExpSetRefRepr.leafOperands();
              lLeafOperands.addAll(lExpSetRefRepr.modSyms()); // modSyms are also leaf operand.
            //##65 END
            }
            lLeafOperandsAndAliases = fRecordAlias.aliasSymGroup(lLeafOperands);
            if (fDbgLevel >= 5)
              System.out.print("\n Examine " + lExp.toStringShort() +
                " " + lExpId.getName() +
                " leafs " + lLeafOperandsAndAliases.toString());
            if (lLeafOperandsAndAliases.contains(lSym)
                &&lAvailableExps.contains(lExpId) //##78
                ) {
              if (fDbgLevel > 3) //##58
                flowRoot.ioRoot.dbgOpt1.print(5, "\n  " +
                  lExp.toStringShort() + " is killed by " +
                  lSym.getName() + " remove " + lExpId.toStringShort());
              lAvailableExps.remove(lExpId);
              if (fLatestNodeForExpId.containsKey(lExpId)) //##78
                fLatestNodeForExpId.put(lExpId, null); //##78
               /*
                 // Remove tne corresponding temporal from fAvailableTemps.
                 if (fGlobalExpTempMap.containsKey(lExpId)) {
                   lTempVar = (Var)fGlobalExpTempMap.get(lExpId);
                   fAvailableTemps.remove(lTempVar);
                   flowRoot.ioRoot.dbgOpt1.print(5, " remove "
                   + lTempVar.toStringShort());
                 }
               */
            }
          }
        }
        if (fDbgLevel > 3)
          flowRoot.ioRoot.dbgOpt1.print(5, "   adjusted available exps ",
            fSubpFlow.sortExpIdCollection(lAvailableExps).toString());
        // Remove temporal variables from fAvailableTemps
        // if corresponding expression is killed by lModSymsAndAliases.
        ExpId lExpId2;
        //##60 HashBasedFlowExpId lTempExpId;
        ExpId lTempExpId; //##60
        for (Iterator lTempIterator = fAvailableTemps.iterator();
             lTempIterator.hasNext(); ) {
          lTempVar = (Var)lTempIterator.next();
          lExpId2 = (ExpId)fGlobalTempExpMap.get(lTempVar);
          if (fDbgLevel >= 5)
            System.out.print("\n examine " + lTempVar.getName()
              + " " + lExpId2.getName());
          /* //##78
          //##60 lTempExpId = (HashBasedFlowExpId)lExpId2.getFlowExpId();
          lTempExpId = lExpId2; //##60
          Set lOperandSet = lTempExpId.getOperandSet();
          if (lOperandSet.contains(lSym)) {
            if (fDbgLevel > 3) //##58
              flowRoot.ioRoot.dbgOpt1.print(5, " remove "
                + lTempVar.toStringShort());
            lTempIterator.remove();
          }
          */ //##78
          //##78 BEGIN
          if (! lAvailableExps.contains(lExpId2)) {
            if (fDbgLevel > 3) //##58
              flowRoot.ioRoot.dbgOpt1.print(5, " remove "
                + lTempVar.toStringShort());
            lTempIterator.remove();
          }
          //##789 END
        }
      }
    }
    fAvailableExps = lAvailableExps;
    if ((pExp.getExpId() != null) &&
        (!toBeExcluded(pExp))) { //##56
      fAvailableExps.add(pExp.getExpId());
    }
    if (fDbgLevel > 3) { //##58
      flowRoot.ioRoot.dbgOpt1.print(5, "adjustAvailability result:",
        pExp.toStringShort());
      flowRoot.ioRoot.dbgOpt1.print(5, "   fAvailableExps ",
        fSubpFlow.sortExpIdCollection(fAvailableExps).toString());
      flowRoot.ioRoot.dbgOpt1.print(5, "   fAvailableTemps ",
        fAvailableTemps.toString());
    }
  } // adjustAvailability

  /** replaceAvailableExp
   *  Replace the computation of pExp by a temporal variable
   *  if all of its operands are in fAvailableExps.
   * @param pExp expression to be examined for replacement.
   * @param pBBlock basic block containing pExp.
   * @param pStmt statement containing pExp.
   *  @return true if actually replaced, false otherwise.
   */
  protected boolean //##25
    replaceAvailableExp(HIR pExp, BBlock pBBlock, Stmt pStmt)
  {
    if (fDbgLevel > 3) //##58
      flowRoot.ioRoot.dbgOpt1.print(4, " replaceAvailableExp",
        pExp.toStringShort());
    boolean lReplaced = false;
    int lIrIndex = pExp.getIndex();
    if (pExp.getIndex() == 0) { //##52
      if (fDbgLevel > 3) //##58
        flowRoot.ioRoot.dbgOpt1.print(4,
          " Generated HIR for replaceAvailableExp " + pExp.toString());
      return false;
    }
    //##60 SetRefReprHirEImpl lSetRefRepr
    SetRefRepr lSetRefRepr //##60
      //##60   = (SetRefReprHirEImpl)fSubpFlow.getSetRefReprOfIR(lIrIndex);
      //##62 = (SetRefRepr)fSubpFlow.getSetRefReprOfIR(lIrIndex); //##60
      = (SetRefReprHirEImpl)fSubpFlow.getSetRefReprOfIR(pExp); //##62
    if (lSetRefRepr == null)
      return false;
    Sym lSym = pExp.getSymOrExpId();
    if (pExp.getOperator() == HIR.OP_VAR) {
      // This may be a variable with high loading cost.
      lSym = pExp.getExpId();
    }
    //##57 Set lOperands = ((SetRefReprHirEImpl)lSetRefRepr).operandExp();
    Set lOperands = ((SetRefReprHirEImpl)lSetRefRepr).leafOperands(); //##57
    if (fDbgLevel >= 5) {
      System.out.print("  symOrExpId " + lSym+"\n");
      System.out.print("  leafOperands " + lOperands+"\n");
      System.out.print("  fAvailableExps "
        + fSubpFlow.sortExpIdCollection(fAvailableExps)+"\n");
    }
    if (lSym instanceof ExpId) {
      if (fAvailableExps.contains(lSym) && // Operands are available
          /* //##60
           (pExp.getOperator() != HIR.OP_CONTENTS) && // contents causes side effect. //##60
           (fAvailableExps.contains(lSym) || // Operands are available
            ((pExp.getOperator() == HIR.OP_VAR)&& // or variable with high cost
          (pExp.getParent().getParent().getOperator() != HIR.OP_ADDR)&& //##60
         (pExp.getParent().getParent().getOperator() != HIR.OP_CONTENTS)&& //##60
             (! (pExp.getType() instanceof PointerType)))) && //##60
          */
          (!toBeExcluded(pExp)) && //##60
          (fExpCost[pExp.getIndex() - fIndexMin] >= fThreshold)) { // of high cost
        lReplaced = replaceExp(pExp, pBBlock, pStmt);
        if (lReplaced) //##78
          return true;
      }
      //##78 BEGIN
      // Record the node corresponding to the ExpId.
      if (!(pExp instanceof Var))
        fLatestNodeForExpId.put(lSym, pExp);
      //##78 END
    }
    //##78 return false;
    return lReplaced; //##78
  } // replaceAvailableExp

  /** replaceExp
   *  Replace the computation of pExp by a temporal variable.
   *  If pExp has no corresponding temporal, generate a temporal.
   *  If the corresponding temporal is already available,
   *  just replece by it.
   *  If the corresponding temporal is not available,
   *  insert the computation of the temporal in front of pStmt
   *  and replace by it.
   *  Add the temporal to the set of availabel temporals.
   * @param pExp expression to be replaced.
   * @param pBBlock basic block containing pExp.
   * @param pStmt statement containing pExp.
   *  @return true if actually replaced, false otherwise.
   */
  protected boolean //##25
    replaceExp(HIR pExp, BBlock pBBlock, Stmt pStmt)
  {
    boolean lReplaced = false; //##78
    Var lInsertedLHSVar;
    Var lGlobalLHSVar = null; //##25-1
    HIR lLatestNode, lLatestNode0, lExp0;
    Exp lInsertedRHS;
    Stmt lStmtOfLatestNode;
    ExpId lExpId = pExp.getExpId();
    VarNode lInsertedLHS;
    VarNode lNewNode, lNewNode0;
    AssignStmt lAssign;
    AssignStmt lInsertedStmt;
    // List lExpNodeList = ((BBlockHirImpl)pBBlock).getExpNodeList(lExpId);
    if (fDbgLevel > 2) //##58
      flowRoot.ioRoot.dbgOpt1.print(3, "replaceExp", pExp.toStringShort()
        + " " + lExpId.getName()); //##25
      // Offset type variable creation does not seem to be supported by the backend.
    if (pExp.getType().getTypeKind() == Type.KIND_OFFSET)
      return lReplaced;
    // No point in substituting address operator
    if (pExp.getOperator() == HIR.OP_ADDR)
      return lReplaced;
    // Before replacing pExp, try to replace subexpressions of pExp. //##25-1
    //##78 if (pExp.getChildCount() > 0)
    //##78  lReplaced = tryToReplaceSubexp(pExp, pBBlock, pStmt);
      //##57 if (fExpTempMap.containsKey(lExpId))
      //##57   lInsertedLHSVar = (Var)fExpTempMap.get(lExpId);
    if (fGlobalExpTempMap.containsKey(lExpId)) //##57
      lInsertedLHSVar = (Var)fGlobalExpTempMap.get(lExpId); //##57
    else {
      lInsertedLHSVar = symRoot.symTableCurrent.generateVar(pExp.getType(),
        symRoot.subpCurrent);
      //##57 fExpTempMap.put(lExpId, lInsertedLHSVar);
      recordTempExpCorrespondence(lInsertedLHSVar, (Exp)pExp); //##57
    }
    if (fDbgLevel > 2) //##58
      flowRoot.ioRoot.dbgOpt1.print(3, " temp " + lInsertedLHSVar.toStringShort()
        + " for " + lExpId); //##25
      /* //##57
         if (fBeforePRE) { // Invoked for partial resundancy elimination.
        // Use always the same temporal for expressions with the same ExpId
        // in a subprogram.
        if (fGlobalExpTempMap.containsKey(lExpId))
         lGlobalLHSVar = (Var)fGlobalExpTempMap.get(lExpId);
        else {
         lGlobalLHSVar = lInsertedLHSVar;
         fGlobalExpTempMap.put(lExpId, lGlobalLHSVar);
        }
        flowRoot.ioRoot.dbgOpt1.print(3, " globalTemp " + lGlobalLHSVar.toStringShort()); //##25
        lInsertedLHSVar = lGlobalLHSVar;
         }
       */
      //##57
    HIR lInsertionPoint;
    HIR lLatestCall = null;
    boolean lGlobalVarReplacement;
    if ((pExp.getOperator() == HIR.OP_VAR) &&
        ((Var)pExp.getSym()).isGlobal())
      lGlobalVarReplacement = true;
    else
      lGlobalVarReplacement = false;
    if (!fAvailableTemps.contains(lInsertedLHSVar)) {
      // Temporal variable is not available. Insert assign statement.
      //##78 lLatestNode = getLatestNodeOfExp(pExp, pBBlock);
      if (fLatestNodeForExpId.containsKey(lExpId)) //##78
        lLatestNode = (HIR)fLatestNodeForExpId.get(lExpId); //##78
      else //##78
        lLatestNode = null; //##78
      if (fDbgLevel > 2) //##58
        flowRoot.ioRoot.dbgOpt1.print(3, " latestNode " + lLatestNode); //##78
      if (lLatestNode == null)
        return lReplaced;
      lInsertionPoint = lLatestNode;
      Stmt lInsertionPointStmt = lInsertionPoint.getStmtContainingThisNode();
      if (lGlobalVarReplacement) {
        lLatestCall = getLatestCall(pExp, pBBlock);
        if (lLatestCall != null) {
          Stmt lLatestCallStmt = lLatestCall.getStmtContainingThisNode();
          Stmt lNextToLatestCall = lLatestCallStmt.getNextStmt();
          if ((lNextToLatestCall == null) ||
              (lNextToLatestCall.getIndex() > pExp.getIndex())) {
            if (fDbgLevel > 2) //##58
              flowRoot.ioRoot.dbgOpt1.print(3, " do not replace. ");
            return lReplaced; // Do not replace.
          }
          if (lNextToLatestCall.getIndex() > lInsertionPointStmt.getIndex())
            lInsertionPoint = lNextToLatestCall;
        }
      }
      lStmtOfLatestNode = lInsertionPoint.getStmtContainingThisNode();
      lInsertedRHS = (Exp)lLatestNode.copyWithOperands();
      lInsertedLHS = hir.varNode(lInsertedLHSVar);
      lInsertedStmt = hir.assignStmt(lInsertedLHS, lInsertedRHS);
      insertTheStatement(lStmtOfLatestNode, lInsertedStmt);
      lNewNode0 = hir.varNode(lInsertedLHSVar);
      //##57 OptUtil.replaceNode(lLatestNode, lNewNode0);
      if (lLatestNode.getIndex() > lStmtOfLatestNode.getIndex()) //##57
        replaceTheExpression(lLatestNode, lNewNode0, pBBlock); //##57
      fAvailableTemps.add(lInsertedLHSVar); //##57
      //##65 if (fDbgLevel > 2) //##58
      //##65   flowRoot.ioRoot.dbgOpt1.print(3, " insert", lInsertedRHS.toString() +
      //##65     " before " + lStmtOfLatestNode.toStringShort());
      if ((pExp.getChildNumber() > 0) && // Do not replace if pExp
          (lLatestNode.getChildNumber() > 0) &&
          (pExp != lLatestNode)) { // was already replaced.
        lNewNode = hir.varNode(lInsertedLHSVar);
        //##65 if (fDbgLevel > 2) //##58
        //##65   flowRoot.ioRoot.dbgOpt1.print(3, " replaceNode",
        //##65     pExp.toStringShort() + " by " + lNewNode.toStringShort()); //##25
          //## OptUtil.replaceNode(pExp, lNewNode);
        replaceTheExpression(pExp, lNewNode, pBBlock);
      }
    }
    {
      //##57 if (fBeforePRE)
      //##57   lInsertedLHSVar = lGlobalLHSVar;
      lNewNode = hir.varNode(lInsertedLHSVar);
      //##65 if (fDbgLevel > 2) //##58
      //##65   flowRoot.ioRoot.dbgOpt1.print(3, " replaceNode",
      //##65     pExp.toStringShort() + " by " + lNewNode.toStringShort()); //##25
        //## OptUtil.replaceNode(pExp, lNewNode);
      replaceTheExpression(pExp, lNewNode, pBBlock);
      lReplaced = true; //##78
    }
    if (fDbgLevel > 2) //##78
      flowRoot.ioRoot.dbgOpt1.print(3, " replaceExp " + pExp.toStringShort()
        + " IS " + lReplaced); //##78
    return lReplaced; //##78
  } // replaceExp

  /** tryToReplaceSubexp
   *  Try to replace subexpressions of pExp which is to be replaced.
   * (By the modification for speeding up SetRefRepr computation,
   *  this method will always return without replacing subexpressions. 0601)
   * @param pExp expression whose subexpression are to be
   *   examined for relacement.
   * @param pBBlock basic block containing pExp.
   * @param pStmt statement containing pExp.
   */
  protected boolean //##25
    tryToReplaceSubexp(HIR pExp, BBlock pBBlock, Stmt pStmt)
  {
    boolean lReplaced = false; //##78
    if ((pExp == null) || (pExp.getChildCount() == 0))
      return lReplaced;
    if (fDbgLevel > 3) //##58
      flowRoot.ioRoot.dbgOpt1.print(4, "  tryToReplaceSubexp",
        pExp.toStringShort());
    int lIrIndex;
    //##60 SetRefReprHirEImpl lSetRefRepr;
    SetRefRepr lSetRefRepr; //##60
    Sym lSym;
    Set lOperands;
    if (pExp.getIndex() == 0) { //##52
      if (fDbgLevel > 3) //##58
        flowRoot.ioRoot.dbgOpt1.print(4,
          " Generated HIR in tryToReplaceSubexp "
          + pExp.toString());
      return lReplaced;
    }
    for (int i = 1; i <= pExp.getChildCount(); i++) {
      HIR lChild = (HIR)pExp.getChild(i);
      lIrIndex = lChild.getIndex();
      if (lIrIndex == 0) { //##52
        if (fDbgLevel > 3) //##58
          flowRoot.ioRoot.dbgOpt1.print(4,
            " Generated HIR in tryToReplaceSubexp "
            + lChild.toString());
        return lReplaced;
      }
      //##60 lSetRefRepr = (SetRefReprHirEImpl) fSubpFlow.getSetRefReprOfIR(lIrIndex);
      //##62 lSetRefRepr = fSubpFlow.getSetRefReprOfIR(lIrIndex); //##60
      lSetRefRepr = (SetRefReprHirEImpl)fSubpFlow.getSetRefReprOfIR(lChild); //##62
      // By the modification for speeding up SetRefREpr computation,
      // SetRefRepr of subtxpressions will be always null,
      // and so, always return without replacing subexp. 060113
      if (lSetRefRepr == null) {
        if (fDbgLevel > 3) //##58
          flowRoot.ioRoot.dbgOpt1.print(4, " do not replace index " + lIrIndex);
        return lReplaced;
      }
      lSym = lChild.getSymOrExpId();
      if ((lSym instanceof ExpId) &&
          fAvailableExps.contains(lSym) &&
          (fExpCost[pExp.getIndex() - fIndexMin] >= fThreshold)) {
        if (!toBeExcluded(lChild))
          lReplaced = replaceExp(lChild, pBBlock, pStmt);
      }
    }
    if (fDbgLevel > 2) //##78
      flowRoot.ioRoot.dbgOpt1.print(3, " tryToReplaceSubExp " + pExp.toStringShort()
        + " IS " + lReplaced); //##78
    return lReplaced; //##78
  } // tryToReplaceSubexp

//##57 BEGIN
  /** replaceTheExpression
   * Replace pOldExp with pNewExp after recording all nodes of pOldExp
   * to fReplacedNodes so that they should not be replaced in later
   * processing.
   * @param pOldExp expression to be replaced.
   * @param pNewExp expression to be used in replacement.
   * @param pBBlock basic block containing pOldExp.
   */
  protected boolean
    replaceTheExpression(HIR pOldExp, HIR pNewExp, BBlock pBBlock)
  {
    boolean lReplaced = false; //##78
    if (pOldExp == null)
      return lReplaced;
    if (fDbgLevel > 2) //##58
      flowRoot.ioRoot.dbgOpt1.print(3, "replaceTheExpression",
        pOldExp.toStringShort() + " with " + pNewExp.toStringShort());
    if ((pOldExp.getChildNumber() < 0) &&
        !(pOldExp.getParent()instanceof HirList) &&
        !(pOldExp.getParent()instanceof BlockStmt)) {
      if (fDbgLevel > 0) //##58
        flowRoot.ioRoot.dbgOpt1.print(2,
          "Parameter of replaceTheExpression", pOldExp.toString()
          + " should have childNumber ");
      return lReplaced;
    }
    HIR lHir;
    //##60 for (BBlockNodeIterator lIterator = pBBlock.bblockNodeIterator();
    for (HirIterator lIterator = pOldExp.hirIterator(pOldExp);
        lIterator.hasNext(); ) {
      lHir = (HIR)lIterator.next();
      if (lHir != null)
        fReplacedNodes.add(lHir);
    }
    OptUtil.replaceNode(pOldExp, pNewExp);
    return true; //##78
  } // replaceTheExpression

  protected void
    insertTheStatement(Stmt pInsertionPoint, Stmt pStmtToBeInserted)
  {
    if (fDbgLevel > 2) //##58
      flowRoot.ioRoot.dbgOpt1.print(3, "insertTheStatement",
        pStmtToBeInserted.toString()
        + " at " + pInsertionPoint.toString());
    Stmt lInsertionPoint = pInsertionPoint;
    if (lInsertionPoint instanceof LabeledStmt) {
      Stmt lStmtBody = ((LabeledStmt)lInsertionPoint).getStmt();
      if (lStmtBody == null) {
        ((LabeledStmt)lInsertionPoint).setStmt(pStmtToBeInserted);
        return;
      }
      else
        lInsertionPoint = lStmtBody;
    }
    if (lInsertionPoint instanceof ExpStmt) {
      Exp lExp = ((ExpStmt)lInsertionPoint).getExp();
      if (lExp.getType() == symRoot.typeBool) {
        HIR lContainingStmt = (HIR)pInsertionPoint.getParent();
        if ((lContainingStmt instanceof IfStmt) ||
            (lContainingStmt instanceof LoopStmt) ||
            (lContainingStmt instanceof SwitchStmt)) {
          ((Stmt)lContainingStmt).combineWithConditionalExp(pStmtToBeInserted,
            lExp);
          return;
        }
      }
    }
    lInsertionPoint.insertPreviousStmt(pStmtToBeInserted,
      lInsertionPoint.getBlockStmt());
  } //insertTheStatement

//##57 END

  /** getLatestNodeOfExp
   *  Get the latest node having maximum node index among the nodes
   *  whose node index is less than that of pExp.
   *  It is assumed that the node index shows the order of
   *  execution within a basic block.
   * @param pExp expression to be replaced.
   * @param pBBlock basic block containing pExp.
   * @return the latest node if found, null if not found.
   */
  protected HIR
    getLatestNodeOfExp(HIR pExp, BBlock pBBlock)
  { // THIS METHOD IS NOT USED ANY MORE. //##78
    HIR lHir, lHirPrev;
    ExpId lExpId;
    List lNodeList;
    int lIndex, lIndexPrev;
    if (pExp == null)
      return null;
    lExpId = pExp.getExpId();
    if (fDbgLevel > 3) //##58
      flowRoot.ioRoot.dbgOpt1.print(4, "getLatestNodeOfExp", pExp.toStringShort()
        + " expId " + lExpId + " " + pBBlock);
    lNodeList = ((BBlockHirImpl)pBBlock).getExpNodeList(lExpId);
    if (lNodeList == null) {
      if (fDbgLevel > 3) //##58
        flowRoot.ioRoot.dbgOpt1.print(4, " not found ");
      return null;
    }
    if (fDbgLevel > 3) { //##58
      flowRoot.ioRoot.dbgOpt1.print(5, " NodeList", lNodeList.toString());
      flowRoot.ioRoot.dbgOpt1.print(5, " fReplacedNodes", fReplacedNodes.toString());
    }
    lIndex = pExp.getIndex();
    lHirPrev = pExp;
    for (Iterator lIterator = lNodeList.iterator();
         lIterator.hasNext(); ) {
      lHir = (HIR)lIterator.next();
      if (fReplacedNodes.contains(lHir))  // Exclude replaced node.
        continue;
      lIndexPrev = lHir.getIndex();
      if (lIndexPrev >= lIndex)
        break;
      lHirPrev = lHir;
    }
    if (lHirPrev != null) {
      HIR lParent = (HIR)lHirPrev.getParent();
      if ((lParent instanceof AssignStmt) && // Exclude LHS of AssignStmt
          (lParent.getChild1() == lHirPrev))
        lHirPrev = null;
      if (lHirPrev == pExp) {
        if (pExp.getOperator() != HIR.OP_VAR) //##57
          lHirPrev = null; // No previous Exp.
      }
    }
    if (lHirPrev != null) {
      if (fDbgLevel > 3) //##58
        flowRoot.ioRoot.dbgOpt1.print(4, " latest " + lHirPrev.toStringShort());
    }else {
        if (fDbgLevel > 3) //##58
          flowRoot.ioRoot.dbgOpt1.print(4, " not found ");
    }
    return lHirPrev;
  } // getLatestNodeOfExp

//##57 BEGIN
  protected HIR
    getLatestCall(HIR pExp, BBlock pBBlock)
  {
    if (fDbgLevel > 3) //##58
      flowRoot.ioRoot.dbgOpt1.print(5, "getLatestCall", pExp.toStringShort()
        + " " + pBBlock);
    int lIndex = pExp.getIndex();
    HIR lStmtWithCall = null;
    if ((pExp.getOperator() == HIR.OP_VAR) &&
        ((Var)pExp.getSym()).isGlobal()) {
      ExpId lExpIdG = pExp.getExpId();
      for (BBlockSubtreeIterator lIteratorS = pBBlock.bblockSubtreeIterator();
           lIteratorS.hasNext(); ) {
        HIR lSubtree = (Stmt)lIteratorS.next();
        if (lSubtree instanceof BlockStmt)
          lSubtree = ((BlockStmt)lSubtree).getFirstStmt();
        if (lSubtree == null)
          continue;
        if (lSubtree.getIndex() == 0)
          continue; // Skip generated statement.
        if (lSubtree.getIndex() > lIndex)
          break;
        SetRefReprHirEImpl lSetRefRepr
          //##60   = (SetRefReprHirEImpl)fSubpFlow.getSetRefReprOfIR(lSubtree.getIndex());
          //##62 = (SetRefReprHirEImpl)fSubpFlow.getSetRefReprOfIR(lSubtree.getIndex()); //##60
          = (SetRefReprHirEImpl)fSubpFlow.getSetRefReprOfIR(lSubtree); //##62
        if ((lSetRefRepr != null) &&
            lSetRefRepr.hasCallWithSideEffect()) { //##71
          lStmtWithCall = lSubtree;
        }
      }
    }
    if (lStmtWithCall != null) {
      if (fDbgLevel > 3) //##58
        flowRoot.ioRoot.dbgOpt1.print(4,
          " getLatestCall  " + lStmtWithCall.toStringShort());
    }
    return lStmtWithCall;
  } // getLatestCall

//##57 END

  /** estimateExpCost
   *  Estimate the approximate cost of subexpressions
   * in the HIR body of pSubpDef by formula
   *   (Exp cost) = (node cost) + (cost of subexpressions)
   * The costs of VarNode and contents node are
   * machineParam.costOfInstruction(MachineParam.COST_INDEX_TEMP_LOAD).
   * The cost of nodes producing no instructions
   *  (DECAY, UNDECAY, EXP_STMT, SEQ, etc.) is 0 (child cost only).
   * The costs of other executable nodes are 1.
   * The node costs are recorded in fExpCost array.
   * @param pSubpDef Subprogram definition.
   */
  public void
    estimateExpCost(SubpDefinition pSubpDef)
  {
    int lIndex;
    int lIndexMax = pSubpDef.getNodeIndexMax();
    if (lIndexMax < ((SubpFlowImpl)fSubpFlow).fMaxIndexOfCopiedNode) //##83
      lIndexMax = ((SubpFlowImpl)fSubpFlow).fMaxIndexOfCopiedNode; //##83
    HIR lNode;
    fIndexMin = pSubpDef.getNodeIndexMin();
    fIndexMax = pSubpDef.getNodeIndexMax();
    //##65 fExpCost = new int[lIndexMax - fIndexMin + 1];
    fExpCost = new int[(lIndexMax - fIndexMin) * 2 + 1]; //##65
    // fMaxIndexOfCopiedNode >= pSubpDef.getNodeIndexMax() because
    // r-value subtrees are copied in allocating ExpId.
    if (fDbgLevel > 0)
      dbg(2, "\n estimateExpCost " + pSubpDef.getSubpSym().toStringShort()
        + " index " + fIndexMin + "-" + lIndexMax);
    estimateNodeCost(pSubpDef.getHirBody(), false);
    //##68 BEGIN
    if (((SubpFlowImpl)fSubpFlow).fSubtreesCopied != null)  {//##70
      for (Iterator lIter = ((SubpFlowImpl)fSubpFlow).fSubtreesCopied.iterator();
           lIter.hasNext(); ) {
        HIR lSubtree = (HIR)lIter.next();
        estimateNodeCost(lSubtree, false);
      }
    } //##70
    //##68 END
  } // estimateExpCost

  /** estimateExpCost
   *  Estimate the approximate cost of each node
   * in subtree pHir.
   * @param pHir HIR subtree.
   */
  private int
    estimateNodeCost(HIR pHir, boolean pLValue)
  {
    int lCost = 0;
    int lChildCost;
    if (pHir == null)
      return 0;
    int lOpCode = pHir.getOperator();
    int lChildCount = pHir.getChildCount();
    switch (lOpCode) {
      case HIR.OP_BLOCK:
        for (Stmt lStmt = ((BlockStmt)pHir).getFirstStmt(); lStmt != null;
             lStmt = lStmt.getNextStmt()) {
          lCost = lCost + estimateNodeCost(lStmt, pLValue);
        }
        break;
      case HIR.OP_LIST:
        for (Iterator lIterator = ((HirList)pHir).iterator();
             lIterator.hasNext(); ) {
          lCost = lCost + estimateNodeCost((HIR)lIterator.next(), pLValue);
        }
        break;
      case HIR.OP_VAR:
        if (pLValue)
          lCost = 1;
        else {
          Var lVar = (Var)pHir.getSym();
          if (lVar.isGlobal())
            lCost = flowRoot.ioRoot.machineParam
              .costOfInstruction(MachineParam.COST_INDEX_GLOBAL_LOAD);
          else
            lCost = flowRoot.ioRoot.machineParam
              .costOfInstruction(MachineParam.COST_INDEX_TEMP_LOAD);
        }
        break;
      case HIR.OP_CONTENTS:
        lCost = flowRoot.ioRoot.machineParam
          .costOfInstruction(MachineParam.COST_INDEX_TEMP_LOAD)
          + estimateNodeCost((HIR)pHir.getChild1(), true);
        break;
      case HIR.OP_SUBS:
        lChildCost = estimateNodeCost((HIR)pHir.getChild1(), true)
          + estimateNodeCost((HIR)pHir.getChild2(), false);
        if (pLValue)
          lCost = 1 + lChildCost;
        else
          lCost = flowRoot.ioRoot.machineParam
            .costOfInstruction(MachineParam.COST_INDEX_TEMP_LOAD)
            + lChildCost;
        break;
      case HIR.OP_ASSIGN:
        lChildCost = estimateNodeCost((HIR)pHir.getChild1(), true)
          + estimateNodeCost((HIR)pHir.getChild2(), false);
        lCost = lChildCost + 1;
        break;
      case HIR.OP_CALL:
        lCost = estimateNodeCost((HIR)pHir.getChild1(), true)
          + estimateNodeCost((HIR)pHir.getChild2(), false)
          + flowRoot.ioRoot.machineParam
          .costOfInstruction(MachineParam.COST_INDEX_CALL);
        break;
      case HIR.OP_ADD:
      case HIR.OP_SUB:
      case HIR.OP_MULT:
      case HIR.OP_DIV:
      case HIR.OP_MOD:
      case HIR.OP_OR:
      case HIR.OP_XOR:
      case HIR.OP_CMP_EQ:
      case HIR.OP_CMP_NE:
      case HIR.OP_CMP_GT:
      case HIR.OP_CMP_GE:
      case HIR.OP_CMP_LT:
      case HIR.OP_CMP_LE:
      case HIR.OP_SHIFT_LL:
      case HIR.OP_SHIFT_R:
      case HIR.OP_SHIFT_RL:
        lChildCost = estimateNodeCost((HIR)pHir.getChild1(), false)
          + estimateNodeCost((HIR)pHir.getChild2(), false);
        if (lOpCode == HIR.OP_MULT)
          lCost = lChildCost + 2;
        else if ((lOpCode == HIR.OP_DIV) || (lOpCode == HIR.OP_MOD))
          lCost = lChildCost + 4;
        else
          lCost = lChildCost + 1;
        break;
      case HIR.OP_NOT:
      case HIR.OP_NEG:
        lCost = 1 + estimateNodeCost((HIR)pHir.getChild1(), false);
        break;
      case HIR.OP_JUMP:
        lCost = 2 + estimateNodeCost((HIR)pHir.getChild1(), false);
        break;
      case HIR.OP_IF:
        lChildCost = estimateNodeCost((HIR)pHir.getChild1(), false)
          + (estimateNodeCost((HIR)pHir.getChild2(), false)
             + estimateNodeCost((HIR)pHir.getChild(3), false) + 1) / 2
          + 2;
        break;
      case HIR.OP_DECAY:
      case HIR.OP_ADDR:
      case HIR.OP_UNDECAY:

        // Add children cost only.
        for (int lChild = 1; lChild <= lChildCount; lChild++) {
          lCost = lCost + estimateNodeCost((HIR)pHir.getChild(lChild), true);
        }
        break;
      case HIR.OP_ENCLOSE:
      case HIR.OP_EXP_STMT:
      case HIR.OP_LABEL_DEF:
      case HIR.OP_LABELED_STMT:
      case HIR.OP_SEQ:

        // Add children cost only.
        for (int lChild = 1; lChild <= lChildCount; lChild++) {
          lCost = lCost + estimateNodeCost((HIR)pHir.getChild(lChild), pLValue);
        }

        //##56 lCost = lCost + 1;
        break;
      default:

        // lCost = (cost of children) + 1;
        for (int lChild = 1; lChild <= lChildCount; lChild++) {
          lCost = lCost + estimateNodeCost((HIR)pHir.getChild(lChild), pLValue);
        }
        lCost = lCost + 1;
    } // switch
    //##83 fExpCost[pHir.getIndex() - fIndexMin] = lCost;
    if (fDbgLevel > 3) //##58
      flowRoot.ioRoot.dbgOpt1.print(5, " nodeCost",
        lCost + " " + pHir.toStringShort());
    fExpCost[pHir.getIndex() - fIndexMin] = lCost; //##83
    return lCost;
  } //estimateNodeCost

  void
    dbg(int level, Object pObject)
  {
    flowRoot.ioRoot.dbgOpt1.printObject(level, "", pObject);
  } // dbg

  void
    dbg(int level, String pHeader, Object pObject)
  {
    flowRoot.ioRoot.dbgOpt1.printObject(level, pHeader, pObject);
    flowRoot.ioRoot.dbgOpt1.println(level);
  } //dbg

} // CommonSubexpElimHirE
