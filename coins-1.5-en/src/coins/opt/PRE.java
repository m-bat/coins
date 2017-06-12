/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.opt;

import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import coins.FlowRoot;
import coins.IoRoot;
import coins.SymRoot;
import coins.driver.CompileThread; //##59
//##60 import coins.aflow.AssignFlowExpId;
//##60 import coins.aflow.BBlockSubtreeIterator;
import coins.flow.BBlockHirSubtreeIteratorImpl; //##60
import coins.flow.BBlockStmtIterator; //##63
import coins.flow.ControlFlow; //##60
import coins.flow.ControlFlowImpl; //##60
import coins.flow.DataFlow; //##60
import coins.flow.ExpVector; //##60
import coins.flow.ExpVectorImpl; //##60
import coins.flow.ExpVectorIterator; //##60
import coins.flow.InitiateFlowHir; //##60
import coins.flow.SetRefReprList; //##60
import coins.flow.ShowControlFlow; //##60
import coins.flow.ShowDataFlow; //##60
import coins.flow.ShowDataFlowByName; //##60
//##60 import coins.aflow.FlowAnalSymVector;
//##60 import coins.aflow.FlowExpId;
//##60 import coins.aflow.FlowResults;
//##60 import coins.aflow.InitiateFlowHir;
//##60 import coins.flow.SetRefRepr;  //##60
//##60 import coins.aflow.SetRefReprList;
import coins.flow.SubpFlow; //##60
//##60 import coins.aflow.util.BitVector;
//##60 import coins.aflow.util.BitVectorIterator;
//##60 import coins.aflow.util.FAList;
//##60 import coins.aflow.util.FAListIterator;
import coins.alias.AliasAnal;
import coins.alias.RecordAlias;
import coins.ir.IR;
import coins.ir.hir.AssignStmt;
import coins.ir.hir.BlockStmt;
import coins.ir.hir.Exp;
import coins.ir.hir.HIR;
import coins.ir.hir.SubsPtrTransformation;
import coins.ir.hir.VarNode;
import coins.flow.BBlock; //##60
import coins.flow.BBlockHir; //##60
import coins.flow.BBlockHirImpl; //##60
import coins.flow.BBlockNodeIterator; //##60
import coins.flow.BBlockHirSubtreeIteratorImpl; //##60
import coins.flow.BBlockSubtreeIterator; //##60
import coins.flow.ControlFlow; //##60
import coins.flow.DataFlow; //##60
import coins.flow.FAList; //##60
import coins.flow.Flow; //##60
import coins.flow.FlowImpl; //##60
import coins.flow.FlowAnalSymVector; //##60
import coins.flow.HirSubpFlowImpl; //##60
import coins.flow.SetRefReprHirEImpl; //##60
import coins.flow.SubpFlowImpl;
import coins.alias.AliasAnal; //##55
import coins.alias.alias2.AliasAnalHir2; //##55
import coins.alias.AliasGroup;
import coins.alias.RecordAlias;
import coins.ir.hir.AssignStmt;
import coins.ir.hir.BlockStmt;
import coins.ir.hir.ExpStmt;
import coins.ir.hir.FunctionExp;
import coins.ir.hir.HirIterator;
import coins.ir.hir.HIR;
import coins.ir.hir.HirList;
import coins.ir.hir.HirVisitorModel2;
import coins.ir.hir.IfStmt;
import coins.ir.hir.IfStmtImpl;
import coins.ir.hir.JumpStmt;
import coins.ir.hir.LabeledStmt;
import coins.ir.hir.LoopStmt;
import coins.ir.hir.LoopStmtImpl;
import coins.ir.hir.ReturnStmt;
import coins.ir.hir.Stmt;
import coins.ir.hir.SubpDefinition;
import coins.ir.hir.SwitchStmt;
import coins.ir.hir.SwitchStmtImpl;
import coins.sym.EnumType;
import coins.sym.ExpId;
import coins.sym.ExpIdImpl;
import coins.sym.FlowAnalSym;
import coins.sym.Label;
import coins.sym.PointerType;
import coins.sym.Sym;
import coins.sym.Type;
import coins.sym.Var;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

/** PRE
 <PRE>
 * Partial redundancy elimination.
 * This is invoked when option -coins:hirOpt=pre is given.
 * See
 *   I. Nakata: Compiler Construction and Optimization, Asakura Shoten, 1999.
 *   D. M. Dhamdhere: E-path_PRE - Partial Redundancy Elimination Made Easy,
 *   SIGPLAN Notices, Vol. 37(8), pp.53-65 (Aug. 2002).
 *
 Symbols and equations according to the E-path_PRE:
 -------------------------------------------------
   locally available   // EGen (downward exposed)
       // e is computed and after the computation, operands are not changed.
   available   // DAvailIn
       // e is already computed on any path to this basic block and
       // after the computation, its operands are not changed.
   partially available //
   locally anticipable // AntLoc (upward exposed)
       // e appears in this basic block and its operands are not set in
       // preceding operations (before use) in the basic block.
   safe  // Either anticipable or available.
   e-path([b_i ... b_k]) = set of eliminatable computation e included
  in b_k, i.e.
  {e | e is locally available in b_i
   and locally anticipable in b_k } &
   empty((b_i ... b_k)) &  // not computed in intermediate point
   e is safe at exit of each node on the path [b_i ... b_k),
  where, b_i, ..., b_k are basic block i, ..., basic block k, respectively.
  e-path suffix is the maximal suffix of an E-path such that
  AntIn * (not AvIn) = true for each node in it.
 Data flow properties
 --------------------
   Comp_i  : e is locally available in b_i
   Antloc_i  : e is locally anticipable in b_i
   Transp_i  : b_i does not contain definitions of e's operands
   AvIn_i  : e is available at entry of b_i
   AvOut_i   : e is available at exit  of b_i
   AntIn_i   : e is anticipable at entry of b_i
   AntOut_i  : e is anticipable at exit  of b_i
   EpsIn_i   : entry of b_i is in an e-path suffix
   EpsOut_i  : exit  of b_i is in an e-path suffix
   Redund_i  : Occurrence of e in b_i is redundant
   Insert_i  : Insert t_e := e in node b_i
   Insert_i_j : Insert t_e := e along edge (b_i, b_j)
   SaIn_i  : A Save must be inserted above the entry of b_i
   SaOut_i   : A Save must be inserted above the exit  of b_i
   Save_i  : e should be saved in t_e in node b_i
  where, t_e is a temporal variable to hold the value of e.
 Data flow equations
 -------------------
   AvIn_i    = PAI_p (AvOut_p)
   AvOut_i   = AvIn_i * Transp_i + Comp_i
   AntIn_i   = AntOut_i * Transp_i + Antloc_i
   AntOut_i  = PAI_s (AntIn_s)
   EpsIn_i   = (SIGMA_p (AvOut_p + EpsOut_p)) * AntIn_i * (not AvIn_i)
   EpsOut_i  = EpsIn_i * (not Antloc_i)
   Redund_i  = (EpsIn_i + AvIn_i) * Antloc_i
   Insert_i  = (not AvOut_i) * (not EpsOut_i) * PAI_s(EpsIn_s)
   Insert_i_j = (not AvOut_i) * (not EpsOut_i) * (not Insert_i) * EpsIn_j
   SaOut_i   = SIGMA_s (EpsIn_s + Redund_s + SaIn_s) * AvOut_i
   SaIn_i    = SaOut_i * (not Comp_i)
   Save_i    = SaOut_i * Comp_i * (not (Redund_i * Transp_i))
  where, _s means successor and _p means predecessor.
 *
 * Optimization using E-path_PRE is done by
 *   Save the value of e: computation t_e is inserted before an
 *   occurrence of e and the occurrence of e is replaced by t_e
 *   (as indicated by Save_i).
 *   Insert an evaluation of e: A computation t_e = e; is inserted
 *   (as indicated by Insert_i and Insert_i_j).
 *   Eliminate a redundant evaluation of e: An occurrence of e is
 *   replaced by t_e (as indicated by Redund_i).
 </PRE>
 */
public class
  PRE
  extends CommonSubexpElimHirE
{

  //-- Fields inherited from CommonSubexpElim
  // public final FlowRoot flowRoot;
  // public final SymRoot  symRoot;
  // public final Sym    sym;
  // public FlowResults  fResults;
  //-- Fields inherited from CommonSubexpElimHir
  // public final HIR    hir;
  //-- Fields inherited from CommonSubexpElimHirE
  // protected SubpFlow    fSubpFlow;
  // protected AliasAnalHir1 fAlias;
  // protected RecordAlias   fRecordAlias;
  // protected Map   fExpTempMap;
  // protected Set   fAvailableExps;
  // protected Set   fAvailableTemps;
  // protected int[]   fExpCost;
  // protected int   fIndexMin;

  public final IoRoot ioRoot;
  public final Flow flow;

  //##60 protected ShowFlow
  protected ShowDataFlow //##60
    fShowFlow;

  protected SubpDefinition
    fSubpDef;

  protected boolean
    fRegularSubp = true;

  protected boolean
    fSubsPtr;      // Subscript-to-pointer transformation.

  protected int
    fTraceLevel;

  CompileThread fCompileThread; //##59
  static long fTimeInMillis = 0;
  boolean fCompileTime = false;

//-- ExpVector to represent data flow information.
  ExpVector fComp[];
  ExpVector fAntloc[];
  ExpVector fTransp[];
  ExpVector fAvailIn[];
  ExpVector fAvailOut[];
  ExpVector fAntIn[];
  ExpVector fAntOut[];
  ExpVector fEpsIn[];
  ExpVector fEpsOut[];
  ExpVector fRedund[];
  ExpVector fInsert[];
  List fInsertEdge[]; // InsertEdge[i] is a list of Insert_i_j where j is
  // arranged in the same order as successor list.
  ExpVector fSaveIn[];
  ExpVector fSaveOut[];
  ExpVector fSave[];
  ExpVector fAllZero;
  ExpVector fAllOne;

  /** PRE
   * Construct an instance of PRE for specified subprogram.
   * @param pResults Instance of FlowResults.
   * @param pSubpDef Subprogram definition as the target of PRE
   *   transformation.
   * @param pThreshold  Replacement threshold;
   *   Expressions with cost less than pThreshold are not
   *   replaced by temporal but recomputed each time.
   */
  public
//##60 PRE( FlowResults pResults, SubpDefinition pSubpDef,
    PRE(SubpFlow pSubpFlow, SubpDefinition pSubpDef, //##60
        //##62 AliasAnal pAliasAnal, //##56
        boolean pSubsPtr, //##56 subscript-to-pointer transformation.
        int pThreshold)
  {
    //##60 super(pResults, pThreshold);
    super(((SubpFlowImpl)pSubpFlow).flowRoot, pThreshold);
    ioRoot = flowRoot.ioRoot;
    flow = flowRoot.flow;
    //##60 fSubpFlow = flow.getSubpFlow();
    ((SubpFlowImpl)fSubpFlow).fHirAnalExtended = true; // HIR
    fSubpDef = pSubpDef;
    //##62 fAlias = pAliasAnal; //##56

    //##60 fShowFlow = new ShowFlow(fResults);
    // flowRoot.aflow.setSubpFlow(fSubpFlow);
    fSubsPtr = pSubsPtr;
    fTraceLevel = ioRoot.dbgOpt1.getLevel();
    fCompileThread = ((CompileThread)Thread.currentThread()); //##59
    dbg(1, "\nPRE threshold=" + pThreshold +
        " subsptr " + pSubsPtr + " trace level=" + fTraceLevel);
    dbg(1, " subp " + fSubpDef.getSubpSym().toString() + "\n");
  } // PRE

  /** doPRE
   *  Do partial redundancy elimination for the PRE instance.
   *  Before actual PRE transformation, this invokes
   *  local common subexpression elimination,
   *  Subs-expression to pointer-expression transformation, and
   *  critical edge removal transformation.
   *  Data flow computation is executed because the local common subexpression
   *  elimination may have changed the program.
   *  After PRE, pointer-expression to Subs-expression transformation
   *  takes place.
   * @return true if some expressions are transformed, false if no expression
   *   is transformed.
   **/
  public boolean
    doPRE()
  {
    Calendar lCalendar = Calendar.getInstance(); //###
    boolean lChanged = false;
    boolean lChanged1, lChanged2, lChanged3, lChanged4, lChanged5,
            lChanged6;
    dbg(1, "\ndoPRE", fSubpFlow.getSubpSym().toString() + "\n");
    if (fCompileTime)
      printTimeInMillis("Start PRE " + fSubpFlow.getSubpSym().toString(), true, true);

    Map lPtrSubsCorrespondence = new HashMap();
    lChanged1 = subscriptToPointerTransformation(lPtrSubsCorrespondence);
    if (lChanged1) {
      fSubpFlow.resetControlAndDataFlowInformation();
      lChanged = true;
    }
    if (fTraceLevel >= 2) {
      System.out.print("\nControl flow analysis before PRE \n");
    }
    if (! fSubpFlow.isComputed(fSubpFlow.CF_CFG)) //##60
      flow.controlFlowAnal(fSubpFlow); //##60
    if (fSubpFlow.isFailed()) //##78
      return lChanged;  //##78
    //##100 BEGIN
    int lComplexityLevel = fSubpFlow.getComplexityLevel();
    if (lComplexityLevel >= 4) {
      if (fTraceLevel > 0) {
    	System.out.print("\nComplexity level = " + lComplexityLevel +
    			" NodeCount=" + fSubpFlow.getNumberOfNodes() +
    			" UsedSymCount=" + fSubpFlow.getUsedSymCount() +
    			" BBlockCount=" + fSubpFlow.getNumberOfBBlocks() +
    			" labelDefCount=" + fSubpFlow.getLabelDefCount());
        System.out.print("\nSKIP PRE optimization ");
      }
      return lChanged;
    }
    //##100 END
    //##60 ControlFlow fControlFlow = new ControlFlowImpl(flowRoot,
    //##60   fSubpFlow, fSubpFlow.getSubpDefinition());
    //##60 ShowFlow lShowFlow = new ShowFlow(fResults);
    lChanged2 = normalizeHir();
    if (fCompileTime)
      printTimeInMillis(" time after preparatpryTransformation", true, true);
    //##60 fSubpFlow.clear();
    //##60 fResults.clearAll();

    if (lChanged2) {
      if (fSubpFlow.isComputed(fSubpFlow.CF_CFG)) {
        fSubpFlow.clearDataFlow();
      }else {
        fSubpFlow.resetControlAndDataFlowInformation();
      }
      lChanged = true;
    }
    lChanged3 = localCommonSubexpElimination();
    if (fCompileTime)
      printTimeInMillis(" time after localCommonSubexpElimination", true, true);
    if (lChanged3) {
      fSubpFlow.resetControlAndDataFlowInformation();
      lChanged = true;
    }
    if (fTraceLevel >= 3) {
      System.out.print("\nHIR before partial redundancy elimination");
      fSubpDef.print(2, true);
    }
    //-- Prepare for PRE:
    //   Clear flow information;
    //   Do control flow analysis;
    //   Do alias analysis;
    //   Prepare for data flow analysis;
    fAvailableExps = new HashSet();
    fAvailableTemps = new HashSet();
    fReplacedNodes = new HashSet();
    estimateExpCost(fSubpDef);
    if (lChanged3) {
      flowRoot.flow.controlFlowAnal(fSubpFlow);
      if (fSubpFlow.isFailed()) //##78
        return lChanged;  //##78
      if (fCompileTime)
        printTimeInMillis(" time after controlFlowAnal", true, true);
        //-- Do dataflow analysis.
      flowRoot.flow.dataFlowAnal(fSubpDef); //##60

    }
    fRecordAlias = fSubpFlow.getRecordAlias(); //##56
    //##57 collectTempsUsedInReplacement();
    initiateDataFlowInformation();
    if (fCompileTime)
      printTimeInMillis(" time after initiateDataFlowInf", true, true);
      //-- Solve dataflow equations for PRE.
    solveDataFlowEquations();
    if (fCompileTime)
      printTimeInMillis(" time after solveDataFlowEquation", true, true);

      //-- Eliminate redundant expressions.
    selectExpsForPRE();
    lChanged4 = eliminateRedundantExpressions();
    if (fCompileTime)
      printTimeInMillis(" time after eliminateRedundantExpressions", true, true);
    if (lChanged4) {
      lChanged = true;
      if (fTraceLevel > 2)
        fSubpFlow.getSubpDefinition().printHir("Changed HIR");
    }
    if (fTraceLevel > 2)
      dbg(3, "\n transformed is " + lChanged4 + "\n");
    if (fSubsPtr) {
      SubsPtrTransformation lSubsPtrTransformation
        = new SubsPtrTransformation(flowRoot.hirRoot);
      lChanged5 = lSubsPtrTransformation.ptrToSubsTransformation(fSubpDef,
        lPtrSubsCorrespondence);
      lChanged = lChanged | lChanged5;
    }
    else
      lChanged5 = false;
    if (lChanged3 | lChanged4 | lChanged5) {
      fSubpDef.finishHir();
      fSubpFlow.setComputedFlag(fSubpFlow.CF_INDEXED);
      //##62 fSubpFlow.setComputedFlag(fSubpFlow.CF_CFG);
      fSubpFlow.setFlowAnalStateLevel(Flow.STATE_CFG_RESTRUCTURING);
    }else {
      if (fTraceLevel > 0)
        dbg(1, "\n No change by this partial redundancy elimination \n");
    }
    if (fCompileTime)
      printTimeInMillis(" time end of PRE", true, true);
    return lChanged;
  } // doPRE

  /** preparatoryTransformation
   * Remove critical edges.
   * @param pPtrSubsCorrespondence record pointer-Subs correspondence.
   * @return true if HIR is changed, false if no change.
   */
  protected boolean
   subscriptToPointerTransformation(Map pPtrSubsCorrespondence)
 {
   boolean lTransformed1 = false;
   if (fTraceLevel > 0)
     dbg(2, " Preparatory transformation - subscriptToPointer \n");
   if (fSubsPtr) {
     SubsPtrTransformation lSubsPtrTransformation = new SubsPtrTransformation(
       flowRoot.hirRoot);
     lTransformed1 = lSubsPtrTransformation.subsToPtrTrasnsformation(fSubpDef,
       pPtrSubsCorrespondence);
   }
   if (fTraceLevel > 0)
     dbg(3, " transformed is " + lTransformed1 + "\n");
   return lTransformed1;
 } //subscriptToPointerTransformation

  protected boolean
    normalizeHir()
  {
    boolean lTransformed2 = false;
    if (fTraceLevel > 0)
      dbg(2, "Preparatory transformation - normalize\n");
    NormalizeHir normalizeHir = new NormalizeHir(flowRoot, fSubpDef);
    lTransformed2 = normalizeHir.processCriticalEdge();
    if (lTransformed2) {
      if (fTraceLevel >= 4) {
        dbg(2, "\n After processCriticalEdges ");
        fSubpDef.print(2, true);
      }
      fSubpDef.finishHir();
      fSubpFlow.setComputedFlag(fSubpFlow.CF_INDEXED);
      if (fTraceLevel >= 5) {
        dbg(2, "\n Before checkCriticalEdges ");
        fSubpDef.print(2, true);
      }
      normalizeHir.checkCriticalEdges(fSubpFlow, fSubpDef);
    }
    if (fTraceLevel > 2)
      dbg(3, " transformed is " + lTransformed2 + "\n");
    return lTransformed2;
  } //preparatoryTransformation

  protected boolean
    localCommonSubexpElimination()
  {
    if (fTraceLevel > 0)
      dbg(2, "\n localCommonSubexpElimination \n");
    //---- Step 1. Eliminate common subexpressions in each basic block.
    //##62 ((SubpFlowImpl)fSubpFlow).allocateSetRefReprTable();
    if (flow.getFlowAnalStateLevel() < Flow.STATE_CFG_AVAILABLE) { //##60
      flowRoot.flow.controlFlowAnal(fSubpFlow); //##60
    }
    if (flow.getFlowAnalStateLevel() < Flow.STATE_HIR_FLOW_AVAILABLE) //##60
      flowRoot.flow.dataFlowAnal(fSubpDef); //##60
    RecordAlias lRecordAlias = fSubpFlow.getRecordAlias(); //##56
    CommonSubexpElimHirE cse = new CommonSubexpElimHirE(((SubpFlowImpl)
      fSubpFlow).flowRoot,
      //##62 lRecordAlias,
      fThreshold, true); //##56
    //##60 ((SubpFlowImpl)fSubpFlow).fRecordSetRefReprs.find(fSubpFlow);
    cse.estimateExpCost(fSubpDef);
    boolean lOptimized = cse.doBBlockLocal(fSubpFlow);
    if (lOptimized) {
      if (fTraceLevel >= 4) {
        System.out.println("\nHIR after common subexpression elimination");
        fSubpDef.print(1, true);
      }
    }
    if (fTraceLevel > 2)
      dbg(3, " transformed is " + lOptimized + "\n");
    return lOptimized;
  } // localCSE

  protected void
    collectTempsUsedInReplacement()
  {
    fGlobalExpTempMap = new HashMap();
    Stmt lStmt;
    Var lVar;
    Exp lRHS;
    ExpId lExpId;
    if (fTraceLevel > 0)
      dbg(2, "\n collectTempsUsedInReplacement ");
    ArrayList fBBlockTable = fSubpFlow.getBBlockTable();
    for (Iterator lBBlockIterator = fBBlockTable.iterator();
         lBBlockIterator.hasNext(); ) {
      BBlock lBBlock = (BBlock)lBBlockIterator.next();
      if (lBBlock.getBBlockNumber() == 0) //##60
        continue; //##60
      //##63 for (coins.flow.BBlockSubtreeIterator lIterator //##60
      //##63      = new coins.flow.BBlockHirSubtreeIteratorImpl(flowRoot,
      //##63                (BBlockHir)lBBlock); //##60
      for (BBlockStmtIterator lIterator
           = fSubpFlow.bblockStmtIterator((BBlockHir)lBBlock);  //##63
           lIterator.hasNext(); ) { // Do for each statement.
        lStmt = (Stmt)lIterator.next();
        if (lStmt instanceof LabeledStmt)
          lStmt = ((LabeledStmt)lStmt).getStmt();
        if (lStmt == null)
          continue;
        if (fTraceLevel > 3)
          dbg(4, lStmt.toString());
        if (lStmt instanceof AssignStmt) {
          if (lStmt.getChild1()instanceof VarNode) {
            lVar = (Var)((VarNode)lStmt.getChild1()).getSymNodeSym();
            if (lVar.getFlag(Sym.FLAG_GENERATED_SYM)) {
              lRHS = (Exp)lStmt.getChild2();
              //##60 if (fResults.containsKey("ExpIdForNode", lRHS)) {
              //##60   lExpId = fResults.getExpIdForNode(lRHS);
              lExpId = fSubpFlow.getExpId(lRHS); //##60
              if (lExpId != null)
                fGlobalExpTempMap.put(lExpId, lVar);
                //##60 }
            }
          }
        }
      }
    }
    if (fTraceLevel > 2)
      dbg(3, "\n fGlobalExpTempMap " + fGlobalExpTempMap.toString());
  } // collectTempsUsedInReplacement

  /** initiateDataFlowInformation
   * Initiate data flow information required to solve the data flow
   * equations given by Dhamdhere.
   */
  protected void
    initiateDataFlowInformation()
  {
    int fBBlockCount;
    int lBBlockNum;
    fBBlockCount = fSubpFlow.getNumberOfBBlocks();
    if (fTraceLevel > 0) {
      dbg(2, "\n initiateDataFlowInformation for PRE BBlockCount " + fBBlockCount + " ");
      if (fCompileTime)
        printTimeInMillis("initiateDataFlowInformation for PRE " , true, true);
      if (fSubpFlow.getRecordAlias() != null) {
        if (fTraceLevel > 0)
          dbg(2, "considering alias \n");
      }
    }
    ArrayList fBBlockTable = fSubpFlow.getBBlockTable();
    ExpVector lTemp1 = fSubpFlow.expVector();
    ExpVector lTemp2 = fSubpFlow.expVector();
    ExpVector lTemp3 = fSubpFlow.expVector();
    fAllZero = fSubpFlow.expVector();
    fAllOne = fSubpFlow.expVector();
    fAllZero.vectorNot(fAllOne);
    fComp = new ExpVector[fBBlockCount + 1];
    fAntloc = new ExpVector[fBBlockCount + 1];
    fTransp = new ExpVector[fBBlockCount + 1];
    fAvailIn = new ExpVector[fBBlockCount + 1];
    fAvailOut = new ExpVector[fBBlockCount + 1];
    fAntIn = new ExpVector[fBBlockCount + 1];
    fAntOut = new ExpVector[fBBlockCount + 1];
    fEpsIn = new ExpVector[fBBlockCount + 1];
    fEpsOut = new ExpVector[fBBlockCount + 1];
    fRedund = new ExpVector[fBBlockCount + 1];
    fInsert = new ExpVector[fBBlockCount + 1];
    fInsertEdge = new ArrayList[fBBlockCount + 1];
    fSaveIn = new ExpVector[fBBlockCount + 1];
    fSaveOut = new ExpVector[fBBlockCount + 1];
    fSave = new ExpVector[fBBlockCount + 1];
    for (Iterator lIterator = fBBlockTable.iterator();
         lIterator.hasNext(); ) {
      BBlock lBBlock = (BBlock)lIterator.next();
      if (lBBlock == null)  //##62
        continue;           //##62
      lBBlockNum = lBBlock.getBBlockNumber();
      if (fTraceLevel > 2)
        dbg(3, "BBlockNum " + lBBlockNum +
            " " + lBBlock.getLabel()+ "\n");
        //##60 SetRefReprList lSetRefReprs = (SetRefReprList)fResults.get("BBlockSetRefReprs", lBBlock);
      SetRefReprList lSetRefReprs = fSubpFlow.getSetRefReprList(lBBlock);
      if (fTraceLevel > 3)
        if (lSetRefReprs != null) {
          ioRoot.dbgOpt1.print(6, " lSetRefReprs ", lSetRefReprs.toString());
        }
      FlowAnalSymVector lExposed, lDefined;
      ExpVector lAvailIn, lAvailOut, lEGen, lEKill, lEKillAll;
      fComp[lBBlockNum] = fSubpFlow.expVector();
      fAntloc[lBBlockNum] = fSubpFlow.expVector();
      fTransp[lBBlockNum] = fSubpFlow.expVector();
      fAvailIn[lBBlockNum] = fSubpFlow.expVector();
      fAvailOut[lBBlockNum] = fSubpFlow.expVector();
      fAntIn[lBBlockNum] = fSubpFlow.expVector();
      fAvailOut[lBBlockNum] = fSubpFlow.expVector();
      fEpsIn[lBBlockNum] = fSubpFlow.expVector();
      fEpsOut[lBBlockNum] = fSubpFlow.expVector();
      fRedund[lBBlockNum] = fSubpFlow.expVector();
      fInsert[lBBlockNum] = fSubpFlow.expVector();
      fSaveIn[lBBlockNum] = fSubpFlow.expVector();
      fSaveOut[lBBlockNum] = fSubpFlow.expVector();
      fSave[lBBlockNum] = fSubpFlow.expVector();
      fInsertEdge[lBBlockNum] = new ArrayList(); //##53
      if (lBBlockNum == 0) //##60
        continue; //##60
      //##60 lAvailIn = (ExpVector)fResults.get("DAvailIn", lBBlock);
      lAvailIn = lBBlock.getAvailIn(); //##60
      if (lAvailIn != null)
        lAvailIn.vectorCopy(fAvailIn[lBBlockNum]);
      if (fTraceLevel > 2)
        dbg(3, " AvailIn", fAvailIn[lBBlockNum].toStringShort());
        //##60 lAvailOut = (ExpVector)fResults.get("DAvailOut", lBBlock);
      lAvailOut = lBBlock.getAvailOut(); //##60
      if (lAvailOut != null) {
        lAvailOut.vectorCopy(fAvailOut[lBBlockNum]);
      }
      if (fTraceLevel > 2)
        dbg(3, " AvailOut", fAvailOut[lBBlockNum].toStringShort());
        //##60 lEGen = (ExpVector)fResults.get("DEGen", lBBlock);
      lEGen = lBBlock.getEGen(); //##60
      if (lEGen != null)
        lEGen.vectorCopy(fComp[lBBlockNum]);
      if (fTraceLevel > 2)
        dbg(3, " Comp", fComp[lBBlockNum].toStringShort());
      fAntloc[lBBlockNum] = computeAntloc(lBBlock);
      if (fTraceLevel > 2)
        dbg(3, " Antloc", fAntloc[lBBlockNum].toStringShort());
        // Transp = (not EKill) - Defined
        //##60 lEKill = (ExpVector)fResults.get("PEKill", lBBlock);
      lEKillAll = lBBlock.getEKillAll(); //##62
      if (lEKillAll != null) {
        lEKillAll.vectorNot(lTemp1); //##62
        //##60 lDefined = (FlowAnalSymVector) fResults.get("PDefined", lBBlock);
        lDefined = (FlowAnalSymVector)lBBlock.getDefined(); //##60
        if (lDefined != null) {
          lTemp2 = lDefined.flowAnalSymToExpVector();
          // lTemp2= lDefined
          lTemp1.vectorSub(lTemp2, fTransp[lBBlockNum]);
          // fTransp[lBBlockNum] = lTemp1 - lTemp2
        }
        else
          lTemp1.vectorCopy(fTransp[lBBlockNum]);
      }
      if (fTraceLevel > 2)
        dbg(3, " Transp", fTransp[lBBlockNum].toStringShort());
        //-- Initial value setting for other items.
        // fAntloc[lBBlockNum].vectorCopy(fAntIn[lBBlockNum]); // AntIn = Antloc
      fAllOne.vectorCopy(fAntOut[lBBlockNum]); // AntOut = allOne
      fAvailIn[lBBlockNum].vectorNot(fEpsIn[lBBlockNum]); // EpsIn = not AvailIn
      fAntloc[lBBlockNum].vectorNot(fEpsOut[lBBlockNum]); // EpsOut = not Antloc
      fAntloc[lBBlockNum].vectorCopy(fRedund[lBBlockNum]); // Redund = Antloc
      fAvailOut[lBBlockNum].vectorNot(fInsert[lBBlockNum]); // Insert = not AvailOut
      fComp[lBBlockNum].vectorNot(fSaveIn[lBBlockNum]); // SaveIn = not Comp
      fAvailOut[lBBlockNum].vectorCopy(fSaveOut[lBBlockNum]); // SaveOut = not AvailOut
      fComp[lBBlockNum].vectorCopy(fSave[lBBlockNum]); // Save = Comp
    }
  } // initiateDataFlowInformation

  /**
   * Compute AntLoc for pBBLock, that is,
   * expressions whose operands are not set by preceeding
   * operations in pBBlock.
   * @param pBBlock basic block.
   * @return AntLoc vector of pBBlock.
   */
  protected ExpVector
    computeAntloc(BBlock pBBlock)
  {
    ExpVector lAntloc = fSubpFlow.expVector();
    FlowAnalSymVector lExposed;
    ExpId lExpId;
    HIR lHir;
    Set lLeafOperands;
    Set lExposedSet;
    //##60 lExposed = (FlowAnalSymVector)fResults.get("PExposed", pBBlock);
    lExposed = (FlowAnalSymVector)pBBlock.getExposed(); //##60
    // If all leaf operands of an expression are exposed-use,
    // then the expression is in AntLoc, that is, the value
    // of the expression can be anticipated at entry to pBBlock.
    lExposedSet = lExposed.flowAnalSyms();
    if (fTraceLevel > 3) {
      dbg(4, "computeAntloc B" + pBBlock.getBBlockNumber());
      dbg(5, " Exposed " + lExposed.toStringShort() + lExposedSet);
    }
    for (BBlockNodeIterator lIterator = pBBlock.bblockNodeIterator();
         lIterator.hasNext(); ) {
      lHir = (HIR)lIterator.next();
      if (fTraceLevel > 3) {
        if (lHir != null) //##60
          dbg(5, " lHir " + lHir.toStringShort());
      }
      if ((lHir instanceof Exp) && (lHir.getChildCount() > 0)) {
        // lHir is non-leaf expression.
        SetRefReprHirEImpl lExpSetRefRepr
          //##62 = (SetRefReprHirEImpl)fSubpFlow.getSetRefReprOfIR(lHir.getIndex());
          = (SetRefReprHirEImpl)fSubpFlow.getSetRefReprOfIR(lHir); //##62
        if (lExpSetRefRepr != null) { //##53
          lLeafOperands = lExpSetRefRepr.leafOperands();
          if (fTraceLevel > 3)
            dbg(5, " " + lHir.toStringShort() + " leafOperands " +
                lLeafOperands.toString());
          if (lExposedSet.containsAll(lLeafOperands)) {
            //##60 if (fResults.containsKey("ExpIdForNode", lHir)) {
            //##60  lExpId = fResults.getExpIdForNode(lHir);
            lExpId = fSubpFlow.getExpId(lHir);
            if (fTraceLevel > 3)
              dbg(5, " expId " + lExpId);
            if (lExpId != null) {
              lAntloc.setBit(lExpId.getIndex());
            }
            //##60 }
          }
        }
      }
    }
    ExpVector lEKillAll = pBBlock.getEKillAll(); //##62
    lAntloc.vectorSub(lEKillAll, lAntloc); //##62
    if (fTraceLevel > 3)
      dbg(5, " antloc " + lAntloc.toStringShort());
    return lAntloc;
  } // computeAntloc

  protected void
    solveDataFlowEquations()
  {
    boolean lChanged = false;
    BBlock lBBlock, lPredBBlock, lSuccBBlock;
    int lBBlockNum;
    if (fDbgLevel > 0) //##58
      dbg(2, " solveDataFlowEquqtions \n");
    ExpVector lTemp1 = fSubpFlow.expVector();
    ExpVector lTemp2 = fSubpFlow.expVector();
    ExpVector lTemp3 = fSubpFlow.expVector();
    ExpVector lTemp4 = fSubpFlow.expVector();
    ExpVector lTemp5 = fSubpFlow.expVector();

    //---- Solve AntIn, AntOut ----//
    // AvIn_i  = PAI_p (AvOut_p)
    // AvOut_i = AvIn_i * Transp_i + Comp_i
    // AntIn_i   = AntOut_i * Transp_i + Antloc_i
    // AntOut_i  = PAI_s (AntIn_s)
    do {
      lChanged = false;
      for (Iterator lCFGIterator =
           fSubpFlow.getListOfBBlocksFromExit().iterator();
           lCFGIterator.hasNext(); ) {
        ExpVector lNewAntIn = fSubpFlow.expVector();
        ExpVector lNewAntOut = fSubpFlow.expVector();
        ExpVector lSuccAntIn;
        lBBlock = (BBlock)lCFGIterator.next();
        lBBlockNum = lBBlock.getBBlockNumber();
        //### System.out.print("\n BBlock " + lBBlockNum); //###
        List lSuccList = lBBlock.getSuccList();
        if (lSuccList.isEmpty()) {
          fAllZero.vectorCopy(lNewAntOut);
        }
        else {
          fAllOne.vectorCopy(lNewAntOut);
          for (Iterator lSuccIterator = lSuccList.iterator();
               lSuccIterator.hasNext(); ) {
            lSuccBBlock = (BBlock)lSuccIterator.next();
            lSuccAntIn = fAntIn[lSuccBBlock.getBBlockNumber()];
            //### System.out.print("\n SuccB" + lSuccBBlock.getBBlockNumber() +
            //###        " SuccAntIn " + lSuccAntIn + " OldAntOut " + lNewAntOut); //###
            lNewAntOut.vectorAnd(lSuccAntIn, lNewAntOut);
            //### System.out.print("\n    NewAntOut(SuccAntIn & OldAntOut) " + lNewAntOut); //###
          }
        }
        if (!lNewAntOut.vectorEqual(fAntOut[lBBlockNum]))
          lChanged = true;
        fAntOut[lBBlockNum] = lNewAntOut;
        //### System.out.print("\n  OldAntOut " + lNewAntOut); //###
        //### System.out.print("\n  Transp " + fTransp[lBBlockNum]); //###
        lNewAntOut.vectorAnd(fTransp[lBBlockNum], lTemp1);
        //### System.out.print("\n  OldAntIn(Transp & OldAntOut) " + lTemp1); //###
        //### System.out.print("\n  Transp " + fTransp[lBBlockNum]); //###
        //### System.out.print("\n  Antloc " + fAntloc[lBBlockNum]); //###
        lTemp1.vectorOr(fAntloc[lBBlockNum], lNewAntIn);
        //### System.out.print("\n  NewAntIn(Antloc | OldAntIn) " + lNewAntIn); //###
        if (!lNewAntIn.vectorEqual(fAntIn[lBBlockNum]))
          lChanged = true;
        fAntIn[lBBlockNum] = lNewAntIn;
      }
    }
    while (lChanged);
    if (fDbgLevel >= 2) {
      printExpVectorArray2(" AntIn  B", fAntIn, "AntOut B", fAntOut);
    }

    //---- Solve EpsIn, EpsOut ----//
    // EpsIn_i  = (SIGMA_p (AvOut_p + EpsOut_p)) * AntIn_i * (not AvIn_i)
    // EpsOut_i = EpsIn_i * (not Antloc_i)
    do {
      lChanged = false;
      for (Iterator lCFGIterator =
           fSubpFlow.getListOfBBlocksFromEntry().iterator();
           lCFGIterator.hasNext(); ) {
        ExpVector lNewEpsIn = fSubpFlow.expVector();
        ExpVector lNewEpsOut = fSubpFlow.expVector();
        ExpVector lPredEpsOut;
        ExpVector lPredAvailOut;
        lBBlock = (BBlock)lCFGIterator.next();
        lBBlockNum = lBBlock.getBBlockNumber();
        List lPredList = lBBlock.getPredList();
        fAllZero.vectorCopy(lTemp1);
        // Compute lTemp1 = SIGMA_p (AvOut_p + EpsOut_p)
        for (Iterator lPredIterator = lPredList.iterator();
             lPredIterator.hasNext(); ) {
          lPredBBlock = (BBlock)lPredIterator.next();
          lPredEpsOut = fEpsOut[lPredBBlock.getBBlockNumber()];
          lPredAvailOut = fAvailOut[lPredBBlock.getBBlockNumber()];
          lPredAvailOut.vectorOr(lTemp1, lTemp1);
          lPredEpsOut.vectorOr(lTemp1, lTemp1);
        }
        lTemp1.vectorAnd(fAntIn[lBBlockNum], lTemp2);
        fAvailIn[lBBlockNum].vectorNot(lTemp3);
        lTemp2.vectorAnd(lTemp3, lNewEpsIn);
        if (!lNewEpsIn.vectorEqual(fEpsIn[lBBlockNum]))
          lChanged = true;
        fEpsIn[lBBlockNum] = lNewEpsIn;
        fAntloc[lBBlockNum].vectorNot(lTemp4);
        lNewEpsIn.vectorAnd(lTemp4, lNewEpsOut);
        if (!lNewEpsOut.vectorEqual(fEpsOut[lBBlockNum]))
          lChanged = true;
        fEpsOut[lBBlockNum] = lNewEpsOut;
      }
    }
    while (lChanged);
    if (fDbgLevel >= 2) {
      printExpVectorArray2(" EpsIn  B", fEpsIn, "EpsOut B", fEpsOut);
    }

    //---- Redund ----//
    // Redund_i = (EpsIn_i + AvIn_i) * Antloc_i
    for (Iterator lCFGIterator =
         fSubpFlow.getListOfBBlocksFromEntry().iterator();
         lCFGIterator.hasNext(); ) {
      ExpVector lRedund = fSubpFlow.expVector();
      lBBlock = (BBlock)lCFGIterator.next();
      lBBlockNum = lBBlock.getBBlockNumber();
      fEpsIn[lBBlockNum].vectorOr(fAvailIn[lBBlockNum], lTemp1);
      lTemp1.vectorAnd(fAntloc[lBBlockNum], lTemp2);
      lTemp2.vectorCopy(lRedund);
      fRedund[lBBlockNum] = lRedund;
      if (fTraceLevel > 1)
        dbg(2, " Redund B" + lBBlockNum + ' '
            + fRedund[lBBlockNum].toStringDescriptive() + '\n');
    }

    //---- Insert ----//
    // Insert_i = (not AvOut_i) * (not EpsOut_i) * PAI_s(EpsIn_s)
    ExpVector lPaiEpsIn = fSubpFlow.expVector();
    ExpVector lSuccEpsIn = fSubpFlow.expVector();
    for (Iterator lCFGIterator =
         fSubpFlow.getListOfBBlocksFromExit().iterator();
         lCFGIterator.hasNext(); ) {
      ExpVector lNewInsert = fSubpFlow.expVector(); // Result vector
      // should be instanciated for each BBlock.
      lBBlock = (BBlock)lCFGIterator.next();
      lBBlockNum = lBBlock.getBBlockNumber();
      fAllOne.vectorCopy(lPaiEpsIn);
      List lSuccList = lBBlock.getSuccList();
      if (lSuccList.isEmpty()) {
        lNewInsert.vectorReset();
      }
      else {
        for (Iterator lSuccIterator = lSuccList.iterator();
             lSuccIterator.hasNext(); ) {
          lSuccBBlock = (BBlock)lSuccIterator.next();
          lSuccEpsIn = fEpsIn[lSuccBBlock.getBBlockNumber()];
          lPaiEpsIn.vectorAnd(lSuccEpsIn, lPaiEpsIn);
        }
        fAvailOut[lBBlockNum].vectorNot(lTemp1);
        fEpsOut[lBBlockNum].vectorNot(lTemp2);
        lTemp1.vectorAnd(lTemp2, lTemp3);
        lTemp3.vectorAnd(lPaiEpsIn, lNewInsert);
      }
      if (!lNewInsert.vectorEqual(fInsert[lBBlockNum]))
        lChanged = true;
      fInsert[lBBlockNum] = lNewInsert;
      if (fTraceLevel > 1)
        dbg(2, " Insert B" + lBBlockNum + " "
            + fInsert[lBBlockNum].toStringDescriptive() + '\n');
    }

    //---- InsertEdge ----//
    // Insert_ij = (not AvOut_i) * (not EpsOut_i) * (not Insert_i) * EpsIn_j
    ExpVector lFromPart = fSubpFlow.expVector();
    ExpVector lToPart = fSubpFlow.expVector();
    int lSuccNum;
    for (Iterator lCFGIterator =
         fSubpFlow.getListOfBBlocksFromExit().iterator();
         lCFGIterator.hasNext(); ) {
      lBBlock = (BBlock)lCFGIterator.next();
      lBBlockNum = lBBlock.getBBlockNumber();
      //##53 fInsertEdge[lBBlockNum] = new ArrayList();
      List lSuccList = lBBlock.getSuccList();
      fAvailOut[lBBlockNum].vectorNot(lFromPart);
      fEpsOut[lBBlockNum].vectorNot(lTemp1);
      lFromPart.vectorAnd(lTemp1, lFromPart);
      fInsert[lBBlockNum].vectorNot(lTemp2);
      lFromPart.vectorAnd(lTemp2, lFromPart);
      for (Iterator lSuccIterator = lSuccList.iterator();
           lSuccIterator.hasNext(); ) {
        ExpVector lEdgeValue = fSubpFlow.expVector(); // EdgeValue should be
        // instanciated for each successor.
        lSuccBBlock = (BBlock)lSuccIterator.next();
        lSuccNum = lSuccBBlock.getBBlockNumber();
        lToPart = fEpsIn[lSuccNum];
        lFromPart.vectorAnd(lToPart, lEdgeValue);
        fInsertEdge[lBBlockNum].add(lEdgeValue);
        if (fTraceLevel > 1)
          dbg(2, " InsertEdge B" + lBBlockNum + " to " + lSuccNum + " "
              + lEdgeValue.toStringDescriptive() + '\n');
      }
    }

    //---- SaveIn, SaveOut ----//
    // SaOut_i = SIGMA_s (EpsIn_s + Redund_s + SaIn_s) * AvOut_i
    // SaIn_i  = SaOut_i * (not Comp_i)
    ExpVector lSigmaPart = fSubpFlow.expVector();
    do {
      lChanged = false;
      for (Iterator lCFGIterator =
           fSubpFlow.getListOfBBlocksFromExit().iterator();
           lCFGIterator.hasNext(); ) {
        ExpVector lNewSaveIn = fSubpFlow.expVector();
        ExpVector lNewSaveOut = fSubpFlow.expVector();
        lBBlock = (BBlock)lCFGIterator.next();
        lBBlockNum = lBBlock.getBBlockNumber();
        lSigmaPart.vectorReset();
        List lSuccList = lBBlock.getSuccList();
        for (Iterator lSuccIterator = lSuccList.iterator();
             lSuccIterator.hasNext(); ) {
          lSuccBBlock = (BBlock)lSuccIterator.next();
          lSuccNum = lSuccBBlock.getBBlockNumber();
          fEpsIn[lSuccNum].vectorOr(lSigmaPart, lSigmaPart);
          fRedund[lSuccNum].vectorOr(lSigmaPart, lSigmaPart);
          fSaveIn[lSuccNum].vectorOr(lSigmaPart, lSigmaPart);
        }
        lSigmaPart.vectorAnd(fAvailOut[lBBlockNum], lNewSaveOut);
        if (!lNewSaveOut.vectorEqual(fSaveOut[lBBlockNum]))
          lChanged = true;
        fSaveOut[lBBlockNum] = lNewSaveOut;
        fComp[lBBlockNum].vectorNot(lTemp1);
        fSaveOut[lBBlockNum].vectorAnd(lTemp1, lNewSaveIn);
        if (!lNewSaveIn.vectorEqual(fSaveIn[lBBlockNum]))
          lChanged = true;
        fSaveIn[lBBlockNum] = lNewSaveIn;
      }
    }
    while (lChanged);
    if (fDbgLevel >= 2) {
      printExpVectorArray2(" SaveIn B", fSaveIn, "SaveOut B", fSaveOut);
    }

    //---- Save ----//
    // Save_i = SaOut_i * Comp_i * (not (Redund_i * Transp_i))
    for (Iterator lCFGIterator =
         fSubpFlow.getListOfBBlocksFromEntry().iterator();
         lCFGIterator.hasNext(); ) {
      ExpVector lNewSave = fSubpFlow.expVector();
      lBBlock = (BBlock)lCFGIterator.next();
      lBBlockNum = lBBlock.getBBlockNumber();
      fSaveOut[lBBlockNum].vectorAnd(fComp[lBBlockNum], lTemp1);
      fRedund[lBBlockNum].vectorAnd(fTransp[lBBlockNum], lTemp2);
      lTemp2.vectorNot(lTemp3);
      lTemp1.vectorAnd(lTemp3, lNewSave);
      fSave[lBBlockNum] = lNewSave;
      if (fTraceLevel > 1)
        dbg(2, " Save B" + lBBlockNum + ' '
            + fSave[lBBlockNum].toStringDescriptive() + '\n');
    }

  } // solveDataFlowEquations

  /** selectExpForPRE
   * Select expressions listed up as redundant exp in some basic block
   * and their cost is greater or equal to fThreshold.
   * If an expression is selected, its subexpressions are not
   * selected so that replacement takes place for the largest redundant
   * expression.
   * Left hand side expression of AssignStmt is not selected
   * because it can not be replaced by temporal variable.
   * Generate temporal variable for each of the selected expressions
   * to represent its value, and put it in fExpTempMap using
   * corresponding ExpId as a key.
   */
  protected void
    selectExpsForPRE()
  {
    if (fTraceLevel > 1)
      dbg(2, "\n selectExpsForPRE \n");
    ArrayList fBBlockTable = fSubpFlow.getBBlockTable();
    BBlock lBBlock;
    int lBBlockNum;
    int ExpIdNum;
    Set lAllSelectedExpId = new HashSet(); // Cumulative set of ExpIds
    // of selected expressions in all basic blocks.
    for (Iterator lCFGIterator =
         fSubpFlow.getListOfBBlocksFromEntry().iterator();
         lCFGIterator.hasNext(); ) { // Traverse all BBlocks.
      lBBlock = (BBlock)lCFGIterator.next();
      lBBlockNum = lBBlock.getBBlockNumber();
      Set lRedundantExpId = fRedund[lBBlockNum].exps(); // Set of ExpIds
      // for redundant expressions.
      Set lSelectedExpId = new HashSet(); // Selected redundant expressions.
      selectLargestRedundantExpsInBBlock(lRedundantExpId, lSelectedExpId,
        lBBlock); // Select largest redundant expressions in this BBlock.
      if (fTraceLevel > 1)
        dbg(2, "\nSelected redundant expressions in B" + lBBlockNum + " "
            + lSelectedExpId.toString());
      lAllSelectedExpId.addAll(lSelectedExpId);
    }
    if (fTraceLevel > 1)
      dbg(2,
          "\nAll selected redundant expressions " + lAllSelectedExpId.toString());
      //-- Allocate temporal variable for each redundant expression and
      //   put it in fExpTempMap using ExpId as its key.
      //##57 fExpTempMap = new HashMap();
    Var lTemp;
    Exp lExp;
    for (Iterator lIterator = lAllSelectedExpId.iterator();
         lIterator.hasNext(); ) {
      ExpId lExpId = (ExpId)lIterator.next();
      lExp = (Exp)lExpId.getLinkedNode();
      if (fTraceLevel > 3)
        dbg(4, "\n redundant " + lExp.toStringWithChildren() +
            " index " + lExp.getIndex() +
            " const " + fExpCost[lExp.getIndex() - fIndexMin]);
      if (fExpCost[lExp.getIndex() - fIndexMin] >= fThreshold) {
        if (fGlobalExpTempMap.containsKey(lExpId))
          lTemp = (Var)fGlobalExpTempMap.get(lExpId);
        else
          lTemp = symRoot.symTableCurrentSubp.generateVar(lExp.getType());
          //##57 fExpTempMap.put(lExpId, lTemp);
        recordTempExpCorrespondence(lTemp, lExp); //##57
        if (fTraceLevel > 2)
          dbg(3, " selected");
      }
    }
    dbg(2, " \n");
  } // selectExpsForPRE

  /** selectLargestRedundantExpsInBBlock
   * selects redundant expressions that are not included in larger
   * redundant expression in the specified basic block pBBlock.
   * @param pRedundantExps set of ExpIds representing all redundant
   *   expressions in pBBlock.
   * @param pSelectedExps set of ExpIds representing selected
   *   redundant expressions.
   * @param pBBlock basic block.
   */
  void
    selectLargestRedundantExpsInBBlock(Set pRedundantExps,
    Set pSelectedExps, BBlock pBBlock)
  {
    //##63 for (coins.flow.BBlockSubtreeIterator lIterator //##60
    //##63      = new coins.flow.BBlockHirSubtreeIteratorImpl(flowRoot,
    /* //##68
    for (coins.flow.BBlockStmtIterator lIterator //##63
          = new coins.flow.BBlockStmtIterator((BBlockHir)pBBlock); //##63
         lIterator.hasNext(); ) { // Do for each statement in pBBlock.
      Stmt lStmt = (Stmt)lIterator.next();
    if (lStmt instanceof LabeledStmt)
      lStmt = ((LabeledStmt)lStmt).getStmt();
    if (lStmt == null)
      continue;
    if (fTraceLevel > 2)
      flowRoot.ioRoot.dbgOpt1.print(3, "Stmt", lStmt.toString());
    selectLargestRedundantExps((HIR)lStmt, pRedundantExps,
      pSelectedExps, pBBlock);
    */ //##68
    //##68 BEGIN
    for (coins.flow.BBlockSubtreeIterator lIterator
         = new coins.flow.BBlockHirSubtreeIteratorImpl(
            flowRoot,(BBlockHir)pBBlock);
         lIterator.hasNext(); ) {
      HIR lSubtree = (HIR)lIterator.next();
      if (lSubtree instanceof LabeledStmt) {
        lSubtree = ((LabeledStmt)lSubtree).getStmt();
      // } else if ((lSubtree.getType() == symRoot.typeBool)&&
      //           (lSubtree.getParent() instanceof IfStmt)) {
      }
      if (lSubtree == null)
        continue;
      if (fTraceLevel > 2)
        flowRoot.ioRoot.dbgOpt1.print(3, "lSubtree", lSubtree.toString());
      selectLargestRedundantExps(lSubtree, pRedundantExps,
        pSelectedExps, pBBlock);
      //##68 END
    }

  } // selectLargestRedundantExpsInBBlock

  /** selectLargestRedundantExps
   * selects ExpIds representing largest redundant expressions
   * in the given HIR subtree pHir. If an expression is found to be
   * redundant, then it is selected but its subexpressions are not
   * selected.
   * @param pHir HIR subtree within which redundant expressions are
   *   to be searched.
   * @param pRedundantExps set of all redundant expressions in pBBlock.
   * @param pSelectedExps set of selected redundant expressions.
   * @param pBBlock basic block.
   */
  void
    selectLargestRedundantExps(HIR pHir, Set pRedundantExps,
    Set pSelectedExps, BBlock pBBlock)
  {
    if (pHir == null)
      return;
    if (fTraceLevel > 3)
      flowRoot.ioRoot.dbgOpt1.print(4, " selectLargestRedundantExps",
        pHir.toStringShort() + " " + pRedundantExps);
    if (pHir.getIndex() == 0) {
      if (fTraceLevel > 3)
        flowRoot.ioRoot.dbgOpt1.print(4, " Generated HIR");
      return;
    }
    if (pHir instanceof HirList) { // Process list elements.
      for (java.util.ListIterator lIterator = ((HirList)pHir).iterator();
           lIterator.hasNext(); ) {
        Object lElem = lIterator.next();
        if (lElem instanceof Exp) {
          selectLargestRedundantExps((Exp)lElem, pRedundantExps,
            pSelectedExps, pBBlock);
        }
      }
    }
    else if (pHir instanceof AssignStmt) { // Exclude left hand side
      HIR lLHS = (HIR)pHir.getChild1(); // but process its children.
      for (int i = 1; i <= lLHS.getChildCount(); i++) { // REFINE for global var.
        selectLargestRedundantExps((HIR)lLHS.getChild(i), pRedundantExps,
          pSelectedExps, pBBlock);
      }
      selectLargestRedundantExps((Exp)pHir.getChild2(), pRedundantExps,
        pSelectedExps, pBBlock); // Process right hand side.
    }
    else {
      //##60 if (fResults.containsKey("ExpIdForNode", pHir)) {
      //##60 ExpId lExpId = fResults.getExpIdForNode(pHir);
      ExpId lExpId = fSubpFlow.getExpId(pHir);
      if ((lExpId != null) &&
          pRedundantExps.contains(lExpId) &&
          (!lExpId.isLHS()) && //##53
          isRegisterizableExp(pHir)) { // This exp is redundant.
        pSelectedExps.add(lExpId); // Select it.
        return; // Do not select its subexpressions.
      }
      //##60 }
      // pHir is not redundant. Process its children.
      for (int i = 1; i <= pHir.getChildCount(); i++) {
        HIR lChild = (HIR)pHir.getChild(i); // Process each child.
        if (lChild == null)
          continue;
        if (lChild.getIndex() == 0) {
          if (fTraceLevel > 3)
            flowRoot.ioRoot.dbgOpt1.print(4,
              "Generated HIR " + lChild.toString());
          continue;
        }
        if (fSubpFlow.getBBlockOfIR(lChild.getIndex()) == pBBlock) {
          // Process the child if it is in pBBlock.
          selectLargestRedundantExps((HIR)lChild, pRedundantExps,
            pSelectedExps, pBBlock);
        }
      }
    }
  } // selectLargestRedundantExps

  protected boolean
    eliminateRedundantExpressions()
  {
    if (fDbgLevel > 0) //##58
      dbg(1, "\neliminateRedundantExpressions \n");
    boolean lChanged = false;
    BBlock lBBlock;
    int lBBlockNum;
    Exp lExp, lSaveExp, lInsertExp, lRedundExp;
    Var lTemp;
    if (fTraceLevel > 1)
      dbg(2, " fGlobalExpTempMap " + fGlobalExpTempMap.toString());
    for (Iterator lCFGIterator =
         fSubpFlow.getListOfBBlocksFromEntry().iterator();
         lCFGIterator.hasNext(); ) {
      lBBlock = (BBlock)lCFGIterator.next();
      lBBlockNum = lBBlock.getBBlockNumber();
      if (fTraceLevel > 2)
        dbg(3, "\n Basic block " + lBBlockNum + '\n');

        //== Traverse statements in this basic block and
        //   find expressions to save to temporal variable,
        //   find expressions to be deleted and replaced it by the
        //   corresponding temporal variable.
        // There is no multiple computation of one expression
        // in a basic block because local common subexpression
        // elimination is already performed.

        //== Save: Assign expressions in fSave to corresponding
        //   temporal and replace the expression by the temporal.
      Set lSaveExps = fSave[lBBlockNum].exps();
      Set lRedundExps = fRedund[lBBlockNum].exps();
      if (fTraceLevel > 2) {
        dbg(3, "\n saveExps " + lSaveExps.toString());
        dbg(3, "\n redundExps " + lRedundExps.toString());
      }
      if ((!lSaveExps.isEmpty()) || (!lRedundExps.isEmpty())) {
        Set lGeneratedStmt = new HashSet();
        Set lSaveProcessed = new HashSet();
        Set lRedundProcessed = new HashSet();
        Stmt lStmt; // Stmt containing the subtree in BBlock.
       //##63  for (BBlockSubtreeIterator lSubtreeIterator = lBBlock.
       //##63       bblockSubtreeIterator();
       //##68 for (BBlockStmtIterator lStmtIterator
       //##68      = fSubpFlow.bblockStmtIterator((BBlockHir)lBBlock);  //##63
       //##68       lStmtIterator.hasNext(); ) {
       //##68   //##60 lStmt = (Stmt)lSubtreeIterator.next();
       //##68   HIR lHir = (HIR)lStmtIterator.next(); //##63
       for (BBlockSubtreeIterator lSubtreeIterator = lBBlock.
            bblockSubtreeIterator();               //##68
            lSubtreeIterator.hasNext(); ) {        //##68
         HIR lHir = (HIR)lSubtreeIterator.next();  //##68
          if (fTraceLevel > 3)
            dbg(4, "\nlHir " + lHir);
          if (lHir == null) //##65
            break; //##65
          if (lHir instanceof Stmt)
            lStmt = (Stmt)lHir;
          else {
            //##68 continue; // REFINE
            //##68 BEGIN
            if ((lHir.getType() == symRoot.typeBool)&&
                (lHir.getParent() instanceof IfStmt)){
              // Conditional expression of IfStmt.
              lStmt = (Stmt)lHir.getParent();
            }else
              continue;
            //##68 END
          }
          HIR lParent = (HIR)lStmt.getParent(); //##71
          int lChildNumber = lStmt.getChildNumber(); //##71
          if (lStmt instanceof LabeledStmt)
            lStmt = ((LabeledStmt)lStmt).getStmt();
          if (lStmt == null)
            continue;
          if (lStmt instanceof BlockStmt)
            continue; // Advance to the 1-st statement in the block.
          if (lGeneratedStmt.contains(lStmt)) // Skip generated statements.
            continue;
          //##71 HIR lParent = (HIR)lStmt.getParent();
          //##71 int lChildNumber = lStmt.getChildNumber();
          Stmt lChangedStmt = lStmt;
          if (fTraceLevel > 3)
            dbg(4, " lParent " + lParent.toString() + " childNumber "
                + lChildNumber + '\n');
            //-- Find expressions to be saved, and if found,
            //   insert assign statement to save and replace the
            //   expression by the temporal variable.
          for (Iterator lIterator = lSaveExps.iterator();
               lIterator.hasNext(); ) {
            ExpId lExpId = (ExpId)lIterator.next();
            if (fTraceLevel > 3)
              dbg(4, "\n ExpId of SaveExp " + lExpId.toString());
            if (lSaveProcessed.contains(lExpId)) {
              dbg(4, " Exp of " + lExpId.toString() + " is already saved. ");
              continue; // Already saved.
            }
            lSaveExp = findExpWithExpId(lStmt, lExpId);
            if (fTraceLevel > 3)
              dbg(4, "\n Save Exp " + lSaveExp + " of "+ lStmt.toStringShort());
            if (lSaveExp != null) { // Expression with lExpId was found.
              //##57 lTemp = (Var)fExpTempMap.get(lExpId);
              lTemp = (Var)fGlobalExpTempMap.get(lExpId); //##57
              if (lTemp != null) { // It is not an excluded expression.
                boolean lSaved = false;
                Stmt lAssignStmt = makeAssignStmt(lTemp, (Exp)lSaveExp);
                if (fTraceLevel > 3)
                  dbg(4, "\n Generate " + lAssignStmt.toStringWithChildren());
                if (lStmt instanceof ExpStmt) {
                  Stmt lControlStmt = lStmt.
                    ancestorControlStmtOfConditionalExp(lSaveExp);
                  if (lControlStmt != null) {
                    if (fTraceLevel > 3)
                      dbg(4, " lControlStmt " + lControlStmt.toStringShort());
                    // lChangedStmt = ???
                    lControlStmt.combineWithConditionalExp(lAssignStmt,
                      lSaveExp);
                    replaceExpWithTemp(lSaveExp, lTemp);
                    lSaved = true;
                  }else { //##71
                    dbg(1, " Control-statement for ",
                      lStmt.toStringWithChildren() + " not found"); //##71
                  }
                }
                else { // lStmt is a statement other than ExpStmt.
                  if (fTraceLevel > 3)
                    dbg(4, " Containing-Stmt is not ExpStmt " + lStmt.toStringShort());
                  if ((lStmt instanceof LoopStmt) &&
                      ((((LoopStmt)lStmt).getLoopStartCondition() != null) && //##68
                       (((LoopStmt)lStmt).getLoopStartCondition()
                          .contains(lSaveExp))|| //##68
                       (((LoopStmt)lStmt).getLoopEndCondition() != null) &&
                       (((LoopStmt)lStmt).getLoopEndCondition()
                          .contains(lSaveExp)))) {                 // lChangedStmt = ???
                    ((LoopStmtImpl)lStmt).combineWithConditionalExp(
                      lAssignStmt, lSaveExp);
                    replaceExpWithTemp(lSaveExp, lTemp);
                    lSaved = true;
                  }
                  else if ((lStmt instanceof IfStmt) &&
                    (((IfStmt)lStmt).getIfCondition().
                    contains(lSaveExp))) {
                    // lChangedStmt = ???
                    ((IfStmtImpl)lStmt).combineWithConditionalExp(lAssignStmt,
                      lSaveExp);
                    replaceExpWithTemp(lSaveExp, lTemp);
                    lSaved = true;
                  }
                  else if ((lStmt instanceof SwitchStmt) &&
                    (((SwitchStmt)lStmt).getSelectionExp().contains(
                    lSaveExp))) {
                    // lChangedStmt = ???
                    ((SwitchStmtImpl)lStmt).combineWithConditionalExp(
                      lAssignStmt, lSaveExp);
                    replaceExpWithTemp(lSaveExp, lTemp);
                    lSaved = true;
                  }
                }
                if (!lSaved) {
                  //##65 lStmt.insertPreviousStmt(lAssignStmt);
                  OptUtil.insertPreviousStmt(lStmt, lAssignStmt); //##65
                  replaceExpWithTemp(lSaveExp, lTemp);  // Shold be executed in all cases ?
                }
                lChanged = true;
                lGeneratedStmt.add(lAssignStmt);
                lSaveProcessed.add(lExpId);
                if (fTraceLevel > 2)
                  dbg(3, " Saved " + lAssignStmt.toStringWithChildren() + "\n");
              }else { //##71
                if (fTraceLevel > 2) //##71
                  dbg(4, " Unnecessary to save " + lSaveExp.toStringShort()); //##71
              }
            }
            // lStmt may be replaced.
            // Take it again from parent (because it may be replaced).
            lStmt = lChangedStmt;
          }
          //-- Find redundant expressions and replace it
          //  by the corresponding temporal variable.
          for (Iterator lIterator = lRedundExps.iterator();
               lIterator.hasNext(); ) {
            ExpId lExpId = (ExpId)lIterator.next();
            if (lRedundProcessed.contains(lExpId))
              continue;
            lRedundExp = findExpWithExpId(lStmt, lExpId);
            if (lRedundExp != null) {
              if (fTraceLevel > 3)
                dbg(4, "\n lRedundExp " + lRedundExp.toStringShort());
              //##57 lTemp = (Var)fExpTempMap.get(lExpId);
              lTemp = (Var)fGlobalExpTempMap.get(lExpId); //##57
              if (lTemp != null) {
                replaceExpWithTemp(lRedundExp, lTemp);
                lChanged = true;
                lRedundProcessed.add(lExpId);
                if (fTraceLevel > 2)
                  dbg(3,
                    " Replace redundant " + lRedundExp.toStringWithChildren()
                    + " by " + lTemp.toStringShort() + "\n");
              }
            }
          }
        } // End of traverse
        // Save expressions that does not appear in this BBlock.
        // Add statements to save them to the tail of BBlock.
        if (!lSaveExps.isEmpty()) {
          if (fTraceLevel > 2)
            dbg(3, "\nSave expressions that does not appear in this BBlock " + lBBlock); //##71
          for (Iterator lIterator = lSaveExps.iterator();
               lIterator.hasNext(); ) {
            ExpId lExpId = (ExpId)lIterator.next();
            if (fTraceLevel > 3)
              dbg(4, "\n advance to " + lExpId); //##71
            if (lSaveProcessed.contains(lExpId)) {
              if (fTraceLevel > 3)
                dbg(4, " It is already saved. "); //##71
              continue; // Already saved.
            }
            //##57 Var lTemp2 = (Var)fExpTempMap.get(lExpId);
            Var lTemp2 = (Var)fGlobalExpTempMap.get(lExpId); //##57
            if (lTemp2 == null) {
              if (fTraceLevel > 3)
                dbg(4, " It is not a selected expression. "); //##71
              continue; // It is not a selected expression.
            }
            HIR lLinkedExp = (HIR)lExpId.getLinkedNode();
            if (lLinkedExp != null) {
              Stmt lAssignStmt = makeAssignStmt(lTemp2, (Exp)lLinkedExp);
              if (fTraceLevel > 3)
                dbg(4, "\nSave " + lLinkedExp.toStringWithChildren());
              Stmt lContainingStmt = lLinkedExp.getStmtContainingThisNode();
              addLastStmtOfBBlock(lAssignStmt, lBBlock);
            }
          }
        }
      }

      //== Insert: For each expressions in fInsert, insert
      //   assignment to the corresponding temporal at exit point.
      Set lInsertExps = fInsert[lBBlockNum].exps();
      if (fTraceLevel > 2)
        dbg(3, "\n insertExps " + lInsertExps.toString());
      for (Iterator lIterator = lInsertExps.iterator();
           lIterator.hasNext(); ) {
        ExpId lExpId = (ExpId)lIterator.next();
        //##65 lExp = (Exp)lExpId.getLinkedNode();
        lExp = (Exp)lExpId.getCopiedExp(); //##65
        //##57 lTemp = (Var)fExpTempMap.get(lExpId);
        lTemp = (Var)fGlobalExpTempMap.get(lExpId); //##57
        if (lTemp != null) {
          Stmt lAssignStmt = makeAssignStmt(lTemp, lExp);
          insertTailStmtCaringBranch(lBBlock, lAssignStmt);
          lChanged = true;
          if (fTraceLevel > 2)
            dbg(3, " Insert " + lAssignStmt.toStringWithChildren() + "\n");
        }
      }

      //== Insert to edge
      BBlock lSuccBBlock;
      ExpVector lEdgeValue;
      List lSuccList = lBBlock.getSuccList();
      Set lEdgeExps;
      int lSuccIndex = 0;
      for (Iterator lSuccIterator = lSuccList.iterator();
           lSuccIterator.hasNext(); ) {
        lSuccBBlock = (BBlock)lSuccIterator.next();
        if (!fInsertEdge[lBBlockNum].isEmpty()) {
          lEdgeValue = (ExpVector)fInsertEdge[lBBlockNum].get(lSuccIndex);
          if (lEdgeValue != null) {
            lEdgeExps = lEdgeValue.exps();
            for (Iterator lIterator = lEdgeExps.iterator();
                 lIterator.hasNext(); ) {
              ExpId lExpId = (ExpId)lIterator.next();
              lExp = (Exp)lExpId.getLinkedNode();
              //##57 lTemp = (Var)fExpTempMap.get(lExpId);
              lTemp = (Var)fGlobalExpTempMap.get(lExpId); //##57
              if (lTemp != null) {
                Stmt lAssignStmt = makeAssignStmt(lTemp, lExp);
                insertToEdge(lBBlock, lSuccBBlock, lAssignStmt);
                lChanged = true;
                if (fTraceLevel > 2)
                  dbg(3, " edge from " + lBBlock.toString() + " to " +
                    lSuccBBlock.toString() + " " +
                    lAssignStmt.toStringWithChildren() + "\n");
              }
            }
          }
        }
        lSuccIndex++;
      } // End of successor iteration

    } // End of BBlock iteration
    return lChanged;
  } // eliminateRedundantExpressions

  /** findNodeWithExpId
   * Find node with pExpId in the subtree pHir.
   * If end of BBlock encountered, return null.
   * @param pHir subtree within which expression is searched.
   * @param pExpId ExpId of expression to be searched.
   * @return the expression found or null if not found.
   */
  protected Exp
//##60 findExpWithExpId( HIR pHir, ExpId pExpId )
    findExpWithExpId(HIR pHir, ExpId pExpId) //##60
  {
    HIR lHir;
    if (fTraceLevel > 3)
      dbg(4, "\n findExpWithExpId " + pExpId.toStringShort() +
          " in " + pHir.toStringShort());
    boolean lNewBBlock = false;
    for (HirIterator lIterator = hir.hirIterator(pHir);
         lIterator.hasNext(); ) {
      lHir = lIterator.next();
      if (lHir instanceof LabeledStmt) {
        if (lNewBBlock)
          return null;
        else
          lNewBBlock = true;
      }
      if ((lHir instanceof Exp) && (lHir.getChildCount() > 0)) {
        //##60 if (fResults.containsKey("ExpIdForNode", lHir)) {
        //##60   if (fResults.getExpIdForNode(lHir) == pExpId) {
        if (fSubpFlow.getExpId(lHir) == pExpId) {
          if (fTraceLevel > 3)
            dbg(4, " found " + lHir.toStringWithChildren());
          return (Exp)lHir; // Found.
        }
        //##60 }
      }
    }
    return null; // Not found.
  } // findNodeWithExpId

  public void
    replaceExpWithTemp(Exp pExp, Var pVar)
  {
    HIR lParent = (HIR)pExp.getParent();
    if ((lParent instanceof AssignStmt) &&
        (lParent.getChild1()instanceof VarNode) &&
        (pExp.getChildNumber() == 2) &&
        (((VarNode)lParent.getChild1()).getSymNodeSym() == pVar)) {
      // LHS and RHS are the same in AssignStmt.
      //##65 ((AssignStmt)lParent).deleteThisStmt();
      OptUtil.deleteStmt((AssignStmt)lParent); //##65
      return;
    }
    VarNode lVarNode = hir.varNode(pVar);
    //##65 pExp.replaceThisNode(lVarNode);
    OptUtil.replaceNode(pExp, lVarNode); //##65
  } // replaceExpWithTemp

  /** makeAssignStmt
   * Make assign statement
   *   pVar = pExp.copyWithOperands()
   * @param pVar left hand side variable.
   * @param pExp right hand side expression.
   * @return assignment statement.
   */
  public Stmt
    makeAssignStmt(Var pVar, Exp pExp)
  {
    return hir.assignStmt(hir.varNode(pVar), (Exp)pExp.copyWithOperands());
  }

  /** insertToEdge
   * There are following cases in the relations of edge and basic blocks:
   *   case 1: pFrom has only 1 successor pTo and pTo has only one predecessor;
   *   case 2: pFrom has only 1 successor pTo and pTo has multiple prececessor;
   *   case 3: pFrom has multiple successor and pTo has only 1 predecessor;
   *   case 4: pFrom has multiple successor and pTo has multiple predecessor;
   * In case 1 and case 2, the operation to be inserted should be placed
   * as the last statement (before unconditional goto) of pFrom BBlock.
   * In case 3, the operation to be inserted should be placed placed
   * as the first statement of pTo BBlock (after label definition).
   * If critical edges have already been removed, case 4 does not occur
   * and the insertion point can be simply determined.
   * @param pFrom Edge start point.
   * @param pTo Edge end point.
   * @param pStmt Statement to be inserted.
   */
  public void
    insertToEdge(BBlock pFrom, BBlock pTo, Stmt pStmt)
  {
    if (pStmt == null)
      return;
    if (fTraceLevel > 3)
      dbg(4, " InsertToEdge from " + pFrom.toString() +
          " to " + pTo.toString() + pStmt.toStringShort() + '\n');
    BBlock lBBlock;
    if (pFrom.getSuccList().size() == 1) {
      insertTailStmtCaringBranch(pFrom, pStmt);
    }
    else if (pTo.getPredList().size() == 1) {
      insertHeadStmtCaringBranch(pTo, pStmt);
    }
    else {
      if (fTraceLevel > 0)
        dbg(1,
            " Critical edge encountered in InsertToEdge from " + pFrom.toString() +
            " to " + pTo.toString() + pStmt.toStringShort() + '\n');
    }
  } // insertToEdge

  /**
   * Insert pStmt as the head Stmt of pBBlock.
   * If the first Stmt of pBBlock is a conditional Exp of
   * control statement, then insert pStmt to all predecessors.
   * @param pBBlock
   * @param pStmt
   */
  public void
    insertHeadStmtCaringBranch(BBlock pBBlock, Stmt pStmt)
  {
    if (pStmt == null)
      return;
    if (fTraceLevel > 3)
      dbg(4, " InsertHeadStmtCaringBranch " + pBBlock.toString()
          + " " + pStmt.toStringWithChildren() + '\n');
    LabeledStmt lFirstStmt = (LabeledStmt)pBBlock.getIrLink();
    Stmt lStmtBody = lFirstStmt.getStmt();
    if (lStmtBody != null) {
      //##71 if (lStmtBody instanceof ExpStmt)
      if (isConditionalExpOfControlStmt(lStmtBody)) //##71
      {
        if (fTraceLevel > 3)
          dbg(4, "  Insert before ExpStmt " + lStmtBody.toStringShort()
              + " Insert to all predecessors.\n");
        List lPredList = pBBlock.getPredList();
        BBlock lPredBBlock;
        for (Iterator lIterator = lPredList.iterator();
             lIterator.hasNext(); ) {
          lPredBBlock = (BBlock)lIterator.next();
          //##53 insertTailStmt(lPredBBlock, pStmt);
          addLastStmtOfBBlock(pStmt, pBBlock); //##53
        }
      }
      else {
        //##65 lStmtBody.insertPreviousStmt(pStmt);
        OptUtil.insertPreviousStmt(lStmtBody, pStmt); //##65
      }
    }
    else // Stmt body is null.
      //##65 lFirstStmt.setStmt(pStmt);
      OptUtil.setStmt(lFirstStmt, pStmt); //##65
  } // insertHeadStmtCaringBranch

  /**
   * Add pStmt at the tail of pBBlock.
   * If pBBlock has tail statement that is a conditional Exp of some
   * control statement, then add pStmt to all successor BBlock
   *  of pBBlock.
   * @param pBBlock BBlock to which pStmt is to be added.
   * @param pStmt statement to be added.
   */
  public void
    insertTailStmtCaringBranch(BBlock pBBlock, Stmt pStmt)
  {
    if (pStmt == null)
      return;
    if (fTraceLevel > 3)
      dbg(4, " InsertTailStmtCaringBranch in " + pBBlock.toString() +
          " " + pStmt.toStringWithChildren());
    Stmt lLastStmt = ((BBlockHir)pBBlock).getLastStmt();
    if (fTraceLevel > 3)
      dbg(4, "  lastStmt " + lLastStmt + '\n');
    //##65 if (lLastStmt instanceof LabeledStmt)
    //##65   lLastStmt = ((LabeledStmt)lLastStmt).getStmt();
    //##65 BEGIN
    Stmt lChildStmt = null; //##71
    while ((lLastStmt instanceof LabeledStmt)||
           (lLastStmt instanceof BlockStmt)) {
      if (lLastStmt instanceof LabeledStmt) {
        //##71 lLastStmt = ((LabeledStmt)lLastStmt).getStmt();
        lChildStmt = ((LabeledStmt)lLastStmt).getStmt(); //##71
      }else { // BlockStmt
        //##71 lLastStmt = ((BlockStmt)lLastStmt).getLastStmt();
        lChildStmt = ((BlockStmt)lLastStmt).getLastStmt(); //##71
      }
      if (lChildStmt == null)   //##71
        break;                  //##71
      lLastStmt = lChildStmt;   //##71
    }
    if (lChildStmt instanceof ExpStmt) //##71
      lLastStmt = lChildStmt;  //##71
    //##65 END
    //##71 if (lLastStmt instanceof ExpStmt)
    if (isConditionalExpOfControlStmt(lLastStmt)) //##71
    {
      if (fTraceLevel > 3)
        dbg(4, " Insert after ExpStmt " + lLastStmt.toStringShort()
            + " Insert to all successors.\n");
      List lSuccList = pBBlock.getSuccList();
      BBlock lSuccBBlock;
      for (Iterator lSuccIterator = lSuccList.iterator();
           lSuccIterator.hasNext(); ) {
        lSuccBBlock = (BBlock)lSuccIterator.next();
        //###53 insertHeadStmt(lSuccBBlock, pStmt);
        addFirstStmtOfBBlock(pStmt, pBBlock);
      }
    }
    else if ((lLastStmt instanceof IfStmt) ||
             (lLastStmt instanceof SwitchStmt) ||
             (lLastStmt instanceof LoopStmt) ||
             (lLastStmt instanceof ReturnStmt) ||
             (lLastStmt instanceof JumpStmt)) {
      //##65 lLastStmt.insertPreviousStmt(pStmt);
      OptUtil.insertPreviousStmt(lLastStmt, pStmt); //##65
    }
    else {
      if (lLastStmt != null) {
        //##65 lLastStmt.addNextStmt(pStmt);
        OptUtil.addNextStmt(lLastStmt, pStmt); //##65
      }else {
        // lLastStmt is null.
        //##60 ((Stmt)((BBlockHir)pBBlock).getIrLink()).combineStmt(pStmt, true);
        Stmt lLinkedStmt = (Stmt)((BBlockHir)pBBlock).getIrLink(); //##60
        Stmt lCombinedStmt = lLinkedStmt.combineStmt(pStmt, true); //##60
        //##65 lLinkedStmt.replaceThisStmtWith(lCombinedStmt); //##60
        OptUtil.replaceStmt(lLinkedStmt, lCombinedStmt); //##65
      }
    }
  } // insertTailStmtCaringBranch

  public void
    addFirstStmtOfBBlock(Stmt pStmt, BBlock pBBlock)
  {
    if (pStmt == null)
      return;
    LabeledStmt lLabeledStmt = (LabeledStmt)pBBlock.getIrLink();
    Stmt lFirstStmt = lLabeledStmt.getStmt();
    if (lFirstStmt == null) {
      //##65 lLabeledStmt.setStmt(pStmt);
      OptUtil.setStmt(lLabeledStmt, pStmt); //##65
    }else if (lFirstStmt instanceof BlockStmt) {
      //##65 ((BlockStmt)lFirstStmt).addFirstStmt(pStmt);
      OptUtil.addFirstStmt((BlockStmt)lFirstStmt, pStmt); //##65
    }
    else {
      lFirstStmt.cutParentLink();
      Stmt lCombinedStmt = ((Stmt)pStmt.copyWithOperands());
      lCombinedStmt.combineStmt(lFirstStmt, true);
      //##65 lLabeledStmt.setStmt(lCombinedStmt);
      OptUtil.setStmt(lLabeledStmt, lCombinedStmt); //##65
    }
    if (fTraceLevel > 3)
      dbg(4, "addFirstStmt", pBBlock.toString() + " " +
          lLabeledStmt.toStringWithChildren());
  } // addFirstStmtOfBBlock

  public void
    addLastStmtOfBBlock(Stmt pStmt, BBlock pBBlock)
  {
    Stmt lNextStmt, lLastStmt = null;
    if (pStmt == null)
      return;
    if (fTraceLevel > 3)
      dbg(4, "\naddLastStmtOfBBlock", pBBlock.toString() + " " +
          pStmt.toStringWithChildren()); //##65
      //##63 for (BBlockSubtreeIterator lIterator = pBBlock.bblockSubtreeIterator();
    for (BBlockStmtIterator lIterator
         = fSubpFlow.bblockStmtIterator((BBlockHir)pBBlock); //##63
         lIterator.hasNext(); ) {
      lNextStmt = (Stmt)lIterator.next();
      if (lNextStmt != null) {
        //##65 lLastStmt = lNextStmt;
        //##65 BEGIN
        switch (lNextStmt.getOperator()) {
          case HIR.OP_ASSIGN:
          case HIR.OP_BLOCK:
          case HIR.OP_EXP_STMT:
            lLastStmt = lNextStmt;
            break;
          default:
        }
        //##65 END
      }
      else //##65
        break; //##65
    }
    if (lLastStmt == null) {
      addFirstStmtOfBBlock(pStmt, pBBlock);
    }
    else {
      //##71 BEGIN
      if (fTraceLevel > 3)
        dbg(4, " lLastStmt " + lLastStmt.toStringShort());
      if (isConditionalExpOfControlStmt(lLastStmt)) {
        Stmt lControlStmt =
          lLastStmt.ancestorControlStmtOfConditionalExp(lLastStmt);
        lControlStmt.combineWithConditionalExp(pStmt, lLastStmt);
      }
      //##71 END
      //##65 BEGIN
      else if (lLastStmt instanceof LabeledStmt) {
        // The BBlock has no statement other than the LabeledStmt
        // linked to the BBlock. Insert pStmt in front of the
        // body of the LabeledStmt.
        LabeledStmt lLabeledStmt = (LabeledStmt)lLastStmt;
        if (lLabeledStmt.getStmt() == null) {
          //##65 lLabeledStmt.setStmt(pStmt);
          OptUtil.setStmt(lLabeledStmt, pStmt); //##65
        }
        else {
          //##65 lLabeledStmt.getStmt().insertPreviousStmt(pStmt);
          OptUtil.insertPreviousStmt(lLabeledStmt.getStmt(), pStmt); //##65
        }
      }
      //##65 END
      else if (lLastStmt instanceof BlockStmt) {
        //##65 ((BlockStmt)lLastStmt).addLastStmt(pStmt);
        OptUtil.addLastStmt((BlockStmt)lLastStmt, pStmt); //##65
      }
      else if (lLastStmt.getParent()instanceof BlockStmt) { //##65
        OptUtil.addNextStmt(lLastStmt, pStmt); //##65
      } //##65
      else {
        //##60 lLastStmt.combineStmt(pStmt, true);
        Stmt lCopiedStmt = (Stmt)lLastStmt.copyWithOperands(); //##60
        Stmt lCombinedStmt = lCopiedStmt.combineStmt(pStmt, true); //##60
        //##65 lLastStmt.replaceThisStmtWith(lCombinedStmt); //##60
        OptUtil.replaceStmt(lLastStmt, lCombinedStmt); //##65
      }
    }
    if (fTraceLevel > 3)
      dbg(4, "addLastStmt", pBBlock.toString() + " " +
          lLastStmt.toStringWithChildren());
  } // addLastStmtOfBBlock

  /**
   * Return true if pHir is a conditional expression of
   * control statement (IfStmt, LoopStmt, SwitchStmt) or
   * return true if its parent is a LabeledStmt that is a
   * conditional expression part of control statement.
   * Return false in other cases.
   * @param pHir subtree to be examined.
   * @return true if pHir is a conditional expression
   *     of control statement.
   */
  public boolean
  isConditionalExpOfControlStmt( HIR pHir )
{
  if (fTraceLevel > 3)
   dbg(4, "\nisConditionalExpOfControlStmt " +  pHir);
  boolean lAnswer = false;
  if (pHir != null) {
    Stmt lContainingStmt = pHir.getStmtContainingThisNode();
    if ( (lContainingStmt instanceof ExpStmt) &&
         (lContainingStmt.getChild1() != null) &&  //##78
        ( ( (HIR) lContainingStmt.getChild1()).getType()
         == symRoot.typeBool)) {
      Stmt lContainingControlStmt = lContainingStmt.
          ancestorControlStmtOfConditionalExp(pHir);
      if (lContainingControlStmt != null) {
        lAnswer = true;
      }
    }
  }
  if (fTraceLevel > 3)
    dbg(4, " answer " + lAnswer);
  return lAnswer;
} // isConditionalExpOfControlStmt

  /**
   * Decide whether pHir is an expression that can be
   * replaced by a register containing the value of the expression.
   * @param pHir HIR subtree to be examined.
   * @return true if can be replaced by register, false otherwise.
   */
  protected boolean
    isRegisterizableExp(HIR pHir) // REFINE use !toBeExcluded(pHir)
  {
    if (pHir == null)
      return false;
    if (!pHir.getType().isScalar())
      return false;
    //##60 BEGIN
    if (toBeExcluded(pHir))
      return false;
    //## The rest of this function can be deleted.
    // Exclude left hand side of AssignStmt.
    HIR lParent = (HIR)pHir.getParent();
    if (lParent instanceof AssignStmt) {
      if (lParent.getChild1() == pHir)
        return false;
    }
    //##60 END
    switch (pHir.getOperator()) {
      case HIR.OP_SUBS:
      case HIR.OP_INDEX:
      case HIR.OP_QUAL:
      case HIR.OP_ARROW:
      case HIR.OP_ADD:
      case HIR.OP_SUB:
      case HIR.OP_MULT:
      case HIR.OP_DIV:
      case HIR.OP_MOD:
      case HIR.OP_AND:
      case HIR.OP_OR:
      case HIR.OP_XOR:
      case HIR.OP_SHIFT_LL:
      case HIR.OP_SHIFT_R:
      case HIR.OP_SHIFT_RL:
      case HIR.OP_NOT:
      case HIR.OP_NEG:
      case HIR.OP_ADDR:
      case HIR.OP_CONV:
      case HIR.OP_DECAY:
      case HIR.OP_UNDECAY:
      case HIR.OP_SIZEOF:
      case HIR.OP_CALL:  //##82
        return true;
      case HIR.OP_CONTENTS:  // May cause side effect. //##60
        return false;
      default:
    }
    return false;
  } // isRegisterizableExp

  protected void
    printExpVectorArray(String pHeader, ExpVector pVectorArray[])
  {
    for (int lIndex = 1; lIndex < pVectorArray.length; lIndex++) {
      System.out.print(pHeader + lIndex + ' '
        + pVectorArray[lIndex].toStringShort() + '\n');
    }
  } // printBitVectorArray

  protected void
    printExpVectorArray2(String pHeader1, ExpVector pVectorArray1[],
    String pHeader2, ExpVector pVectorArray2[])
  {
    for (int lIndex = 1; lIndex < pVectorArray1.length; lIndex++) {
      if (pVectorArray1[lIndex] != null)
        System.out.print(pHeader1 + lIndex + ' '
          + pVectorArray1[lIndex].toStringShort() + '\n');
      else
        System.out.print(pHeader1 + lIndex + '\n');
      if (pVectorArray2[lIndex] != null)
        System.out.print(pHeader2 + lIndex + ' '
          + pVectorArray2[lIndex].toStringShort() + '\n');
      else
        System.out.print(pHeader2 + lIndex + '\n');
    }
  } // printBitVectorArray

  /** printTimeInMillis prints the current time if required.
   * "diff" means the elapsed time from the previous call of
   * this method in milli-seconds.
   * @param pHeader is the heading message to be printed with the time.
   * @param pPrint true if the time is to be printed.
   * @param pMonitor true if the time is to be displayed on monitor.
   * @return the current time in milli-seconds.
   */
  public long
    printTimeInMillis(String pHeader, boolean pPrint, boolean pMonitor)
  {
    long lInterval;
    long lTimeInMillis = fCompileThread.elapsedTime(); //##59
    if (fTimeInMillis == 0)
      lInterval = 0;
    else
      lInterval = lTimeInMillis - fTimeInMillis;
    if (pPrint)
      System.out.print('\n' + pHeader + " time " + lTimeInMillis
        + " diff " + lInterval + " ");
    if (pMonitor)
      System.err.print('\n' + pHeader + " time " + lTimeInMillis
        + " diff " + lInterval + " ");
    fTimeInMillis = lTimeInMillis;
    return lTimeInMillis;
  }

} // PRE
