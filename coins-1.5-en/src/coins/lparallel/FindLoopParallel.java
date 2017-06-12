/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.lparallel;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.HashSet; //##74
import coins.HirRoot;
import coins.IoRoot;
import coins.aflow.BBlock;
import coins.aflow.DefUseCell;
import coins.aflow.DefUseList;
import coins.aflow.Flow;
import coins.aflow.FlowAdapter;
import coins.aflow.FlowAnalSymVector;
import coins.aflow.FlowAnalSymVectorImpl;
import coins.aflow.FlowResults;
import coins.aflow.SetRefRepr;
import coins.aflow.SetRefReprList;
import coins.aflow.SubpFlow;
import coins.aflow.UDChain;
import coins.aflow.UDList;
import coins.ir.IR;
import coins.ir.hir.AssignStmt;
import coins.ir.hir.BlockStmt;
import coins.ir.hir.ConstNode;
import coins.ir.hir.Exp;
import coins.ir.hir.ExpImpl;
import coins.ir.hir.HIR;
import coins.ir.hir.Stmt;
import coins.ir.hir.SubpDefinition;
import coins.ir.hir.SubpNode;
import coins.ir.hir.VarNode;
import coins.sym.Const;
import coins.sym.Sym;
import coins.sym.FlowAnalSym;
import coins.sym.SymTable;
import coins.sym.Var;
import coins.sym.Subp;

public class FindLoopParallel
  extends FlowAdapter
{
  private SubpFlow fSubpFlow;
  private BBlock LoopBackBodyBBlock;
  private BBlock EntryBodyBBlock;
  private BBlock ExitBodyBBlock;
  private IoRoot ioRoot;
  private ArrayAreaAnalyzer ArrayAnalysis;
  private HirRoot HirRoot;
  private HIR hir;

  private LoopTable fLoopTable;
  private LoopUtil fUtil;
  private CheckLoopBody fCheckLoopBody;
  private Flow fFlow;
  protected PreDefinedFunctions predefinedFunctions; //##71
  private Set fTraversedBBlocks; //##74
  /**
   *
   * FindLoopParallel:
   *
   *  @param pResults  FlowResults
   *
   **/
  public FindLoopParallel(FlowResults pResults)
  {
    super(pResults);
    fFlow = pResults.flowRoot.aflow;
    ioRoot = pResults.flowRoot.ioRoot; //##73
    predefinedFunctions = new PreDefinedFunctions(
      pResults.flowRoot.symRoot.sourceLanguage); //##71
  }

  /**
   *
   * find:
   *
   *  Loop parallelization analysis processing of specified LoopTable.
   *
   *  @param  pSubpFlow SubpFlow
   *  @param  pLoopTable the table for loop
   *
   **/
  public void find(SubpFlow pSubpFlow, LoopTable pLoopTable)
  {
    ioRoot.dbgPara1.print(1, "\nFindLoopParallel.find " + pSubpFlow.getSubpSym().getName()); //##74
    fLoopTable = pLoopTable;
    fSubpFlow = pSubpFlow;

    if (fLoopTable.analysis_flag == true) {
      return;
    }
    fUtil = new LoopUtil(fResults, fSubpFlow);
    fCheckLoopBody = new CheckLoopBody(fLoopTable, fUtil);

    HirRoot = fResults.flowRoot.hirRoot;
    hir = fResults.flowRoot.hirRoot.hir;

    LoopBackBodyBBlock = (BBlock)fResults.getBBlockForLabel(
      fLoopTable.LoopStmt.getLoopBackLabel());
    EntryBodyBBlock = (BBlock)fResults.getBBlockForLabel(
      fLoopTable.LoopStmt.getLoopBodyLabel());
    ExitBodyBBlock = (BBlock)fResults.getBBlockForLabel(
      fLoopTable.LoopStmt.getLoopStepLabel());

    fUtil.getLoopBBlockList(fLoopTable.BBlockList,
      LoopBackBodyBBlock, ExitBodyBBlock, EntryBodyBBlock);

    ArrayAnalysis = new ArrayAreaAnalyzer(hir, fUtil);
    ArrayAnalysis.setLoopExitBBlock(ExitBodyBBlock);
    ArrayAnalysis.setFlowResults(fResults);
    RegionOp regOp;
    regOp = new RegionOpImpl();
    ArrayAnalysis.setRegOp(regOp);

    boolean para_flag = true;

    for (Iterator Ie = fLoopTable.InnerLoopList.listIterator(); Ie.hasNext(); ) {
      LoopTable lTable = (LoopTable)Ie.next();
      fResults.get("LoopParallel", pSubpFlow, (LoopTable)lTable);
      if (lTable.analysis_flag == false) {
        para_flag = false;
      }
    }
//
// Trace Flag
//
//    if (fLoopTable.InnerLoop == true) fUtil.TraceFlag = true;
//    else fUtil.TraceFlag = false;
    if (fLoopTable.OuterLoop == null) {
      fUtil.TraceFlag = true;
    }
    else {
      fUtil.TraceFlag = false;
    }
    fUtil.TraceFlag = true;

    fUtil.Trace("---pass:Find---------------------------------", 1);
    fUtil.Trace("---pass:Find:NestLevel=" + pLoopTable.fNestLevel, 1);
    fUtil.Trace("---pass:Find:For=" + pLoopTable.LoopStmt.toString(), 1);
    fUtil.Trace("---pass:Find:Entry=" + EntryBodyBBlock.getBBlockNumber(), 1);
    fUtil.Trace("---pass:Find:InnerLoop=" + pLoopTable.InnerLoop, 1);
    fUtil.Trace("---pass:Find---------------------------------", 1);
    if (para_flag == true) {

      fUtil.Trace("para_flag == true", 5);

      if (fCheckLoopBody.HIR_CheckLoopBody() == true) {

        fUtil.Trace("CheckLoopBody == true", 5);

        fUtil.getLoopInnerBBlockList(fLoopTable);
        if (CheckLoop() == true) {
          fUtil.TraceBBlockList(fLoopTable); // Trace BBlock
          invariant();
          induction();
          //fUtil.Trace("DebugInduction*************",5);
          fLoopTable.DebugInductionList(fUtil);
          reduction();
          if (para_loop() == true) {
            Normalize_loopIndex();
            fLoopTable.DebugInductionList(fUtil);

            if (make_ref() == true) {
              make_BlockRef();
              ParallelCheck();
              OpenMPCheck();
            }
            fLoopTable.analysis_flag = true;
          }
        }
        else {
          fLoopTable.setParaFlag(fUtil, false, "CheckLoop");
        }
      }
      else {
        fLoopTable.setParaFlag(fUtil, false, "CheckLoopBody");
      }
    }
    else {
      fLoopTable.setParaFlag(fUtil, false, "(para_flag == false)");
    }
    fResults.put("LoopParallel", pSubpFlow, fLoopTable);
    if (ioRoot.dbgPara1.getLevel() > 1) { //##73
      ioRoot.dbgPara1.print(3, "\nput LoopParallel " + fLoopTable); //##74
      fLoopTable.print(pLoopTable.fNestLevel); //##73
    }
  }

  /**
   *
   * CheckLoop:
   *
   * It investigates whether a loop is effective as control flow graph.
   *
   *
   **/
  private boolean CheckLoop()
  {
    fUtil.Trace("---pass:CheckLoop", 1);
    fUtil.Trace("pass:CheckLoop BBlockSize= " + fLoopTable.InnerBBlockList.size(),
                5);
    for (ListIterator Ie = fLoopTable.InnerBBlockList.listIterator();
         Ie.hasNext(); ) {
      BBlock CurrBlock = (BBlock)Ie.next();
      fUtil.Trace("---pass:CheckLoop:" + CurrBlock.toString(), 5);
      if (CurrBlock == LoopBackBodyBBlock) {
        continue;
      }
      if (CurrBlock == ExitBodyBBlock) {
        continue;
      }

      List SuccList = CurrBlock.getSuccList();
      for (ListIterator Ie1 = SuccList.listIterator(); Ie1.hasNext(); ) {
        BBlock Succ = (BBlock)Ie1.next();
        if (fLoopTable.BBlockList.contains(Succ) == false) {
          fUtil.Trace("---pass:CheckLoop:SUCC" + Succ.toString(), 1);
          return false;
        }
      }

      List PredList = CurrBlock.getPredList();
      for (ListIterator Ie2 = PredList.listIterator(); Ie2.hasNext(); ) {
        BBlock Pred = (BBlock)Ie2.next();
        if (fLoopTable.BBlockList.contains(Pred) == false) {
          fUtil.Trace("---pass:CheckLoop:Pred" + Pred.getLabel().toString(), 1);
          return false;
        }
      }
    }
    if (fLoopTable.BBlockList.contains(ExitBodyBBlock) == false) {
      fUtil.Trace("Exit BBlock=" + ExitBodyBBlock, 1); //##70
      for (ListIterator Ie3 = fLoopTable.BBlockList.listIterator(); Ie3.hasNext(); ) {
        BBlock DebugB = (BBlock)Ie3.next();
        fUtil.Trace("CheckBody BBlock=" + DebugB.getBBlockNumber(), 1);
      }
      return false;
    }
    return true;
  }

  /**
   *
   * para_loop:
   *
   *
   * It investigates whether basic induction exists in a loop.
   *
   **/
  private boolean para_loop()
  {

    boolean loop_ctr = false;
    fUtil.Trace("---pass:para_loop", 1);

    for (Iterator Ie = fLoopTable.IndList.iterator(); Ie.hasNext(); ) {
      BasicInduction IndTable = (BasicInduction)Ie.next();
      if (IndTable.loop_ctr == true) {
        loop_ctr = true;
        break;
      }
    }
    if (loop_ctr == false) {
      fUtil.Trace("PARA_LOOP=false1", 5);
      return false;
    }
    fUtil.Trace("para_loop=true", 5);
    return exit_cont();
  }

  /**
   *
   * exit_cont:
   *
   * A conditional expression is investigated.
   *
   **/
  private boolean exit_cont()
  {
    Exp Cond;
    Exp Exp1;
    Exp Exp2;
    Exp finalExp;
    int op;
    boolean para_flag;

    //
    // startCondition:
    //
    fUtil.Trace("---pass:exit_cont", 1);
    Cond = fLoopTable.LoopStmt.getLoopStartCondition();
    if (Cond == null) {
      return false;
    }
    op = Cond.getOperator();
    para_flag = false;

    Exp1 = fUtil.SkipConv(Cond.getExp1());
    Exp2 = fUtil.SkipConv(Cond.getExp2());
    finalExp = null;

    switch (op) {
      case HIR.OP_CMP_GT:
      case HIR.OP_CMP_GE:
      case HIR.OP_CMP_LT:
      case HIR.OP_CMP_LE:
        if (fLoopTable.fInv.IsInvariant(Exp1) == true) {
          para_flag = true;
          finalExp = Exp1;
        }
        else if (fLoopTable.fInv.IsInvariant(Exp2) == true) {
          para_flag = true;
          finalExp = Exp2;
        }
    }
    if (para_flag == true) {
      fLoopTable.finalExp = finalExp;
      setFinalExp((Exp)finalExp, fLoopTable.finalExpList);
    }
    return para_flag;
  }

  /**
   *
   * setFinalExp:
   *
   * A Leaf node (HIR-VarNode) is looked for.
   *
   * @param finalNode HIR-Node
   * @param finalList list of HIR-VarNode
   *
   **/
  private void setFinalExp(Exp finalNode, LinkedList finalList)
  {
    int ChildCount;
    int i;
    ChildCount = finalNode.getChildCount();

    if (ChildCount == 0) { // LeafNode
      if (fUtil.IsVarNode((HIR)finalNode) == true) {
        finalList.add(finalNode);
      }
    }
    else {
      for (i = 1; i <= ChildCount; i++) {
        HIR ChildNode = (HIR)finalNode.getChild(i);
        if (!(ChildNode instanceof Exp)) {
          continue; // ex) Functions
        }
        setFinalExp((Exp)ChildNode, finalList);
      }
    }
  }

  /**
   *
   * invariant:
   *
   * invariant is looked for.
   *
   **/
  private void invariant()
  {
    fUtil.Trace("---pass:invariant", 1);
    fLoopTable.fInv = new Invariant(fLoopTable, fSubpFlow, fResults, fUtil);
  }

  /**
   *
   * Normalize_loopIndex:
   *  LoopIndex is Normalized.
   *
   *  for (i=e1;i<e2;i=i+e3)
   *  ==>for (#tmp=0;#tmp<(e2-e1)/e3 ;#tmp= #tmp+1)
   *
   **/
  private void Normalize_loopIndex()
  {

    BasicInduction IndTable;
    boolean LastData;
    fUtil.Trace("---pass:Normalize_loopIndex", 1);

    IndTable = fLoopTable.LoopCtrInduction;

    fLoopTable.repeat_no_normalize = fLoopTable.repeat_no_value;
    LastData = true;

    Exp Cond = fLoopTable.LoopStmt.getLoopStartCondition();
    int op = Cond.getOperator();
    fLoopTable.zero_check = false;
    if (IndTable.InitConstValue != 0 || IndTable.InitConstFlag == false ||
        IndTable.inc != 1 || op != HIR.OP_CMP_LT) {

      SubpDefinition subp = fSubpFlow.getSubpDefinition();
      SymTable LocalSymTable = subp.getSubpSym().getSymTable();
      Var tmpVar = LocalSymTable.generateVar(
        IndTable.DefVarNode.getType(),
        subp.getSubpSym());
      tmpVar.setSymType(IndTable.DefVarNode.getType());

      // --------------------------
      // for (i=e1;i<e2;i=i+e3)
      // =>for (#tmp=0;#tmp<(e2-e1)/e3 ;#tmp= #tmp+1)
      // --------------------------
      // [Init]
      // #tmp = 0;
      //

      fLoopTable.addInit = (Stmt)hir.assignStmt(hir.varNode(tmpVar),
        hir.intConstNode(0));

      //[Cond]
      // #tmp <  (e2-e1)/e3;
      //
      //

      if (IndTable.InitConstFlag == true && fLoopTable.const_repeat_no == true) {
        long e1, e2, e3, e4;
        e1 = IndTable.InitConstValue;
        e2 = fLoopTable.repeat_no_value;
        e3 = IndTable.inc;
        e4 = GetCondition_No(e1, e2, e3); // (e2-e1)/e3
        fLoopTable.addCond = new ExpImpl(HirRoot, HIR.OP_CMP_LT,
          hir.varNode(tmpVar),
          hir.intConstNode((int)e4));

        fLoopTable.repeat_no_normalize = e4;

      }
      else {
        Exp e1;
        Exp e2;
        Exp e3;
        Exp e4;

        if (IndTable.InitConstFlag == true) {
          e1 = (Exp)hir.intConstNode((int)IndTable.InitConstValue);
        }
        else {
          e1 = (Exp)hir.varNode(IndTable.InitConstNode.getVar());

        }
        if (fLoopTable.const_repeat_no == true) {
          e2 = (Exp)hir.intConstNode((int)fLoopTable.repeat_no_value);
        }
        else {
          e2 = (Exp)fLoopTable.finalExp.copyWithOperands();

        }
        e4 = GetCondition_Exp(e1, e2, IndTable.inc);

        fLoopTable.addCond = new ExpImpl(HirRoot, HIR.OP_CMP_LT,
          hir.varNode(tmpVar), e4);
      }

      // [Step]
      // #tmp =  #tmp+1;
      //

      fUtil.Trace("---pass:Normalize_loopIndex(ADDSTEP)", 1);
      VarNode Def;
      VarNode Use;
      Def = (VarNode)hir.varNode(tmpVar);
      Use = (VarNode)hir.varNode(tmpVar);
      fLoopTable.addStep = (Stmt)hir.assignStmt(Def,
        new ExpImpl(HirRoot,
                    HIR.OP_ADD,
                    Use,
                    hir.intConstNode(1)));

      //
      // Change LoopCtrInduction
      //

      IndTable.loop_ctr = false;
      BasicInduction CtrIndTable = new BasicInduction(
        (AssignStmt)fLoopTable.addStep, Def, Use, 1);
      fLoopTable.LoopCtrInduction = CtrIndTable;
      fLoopTable.IndList.add(CtrIndTable);
      CtrIndTable.InitConstFlag = true;
      CtrIndTable.InitConstValue = 0;
      CtrIndTable.loop_ctr = true;
    }
    fUtil.Trace("repeat_no_normalize =" + fLoopTable.repeat_no_normalize, 5);
    fLoopTable.LoopCtrInduction.LastConstValue = fLoopTable.repeat_no_normalize -
      1;
    fUtil.Trace("LastConstValue=" + fLoopTable.LoopCtrInduction.LastConstValue,
                5);
    fUtil.Trace("LastData=" + LastData, 5);
    if (fLoopTable.const_repeat_no == true) {
      SetInductionLastData();
    }
    if (fLoopTable.repeat_no_normalize > 0) {
      if (fLoopTable.LoopCtrInduction.InitConstFlag == true) {
        fLoopTable.zero_check = true;
      }
    }
  }

  /**
   *
   * GetCondition_No :
   *
   *   The condition value of a loop is acquired.
   *
   *   Normalize=> (1)OP=CMP_LT
   *               (2) STEP =+1
   *       case 1   x < n   -->  n
   *       case 2   x <= n  --> (n+1)
   *       case 3   x  > n  -->  n
   *       case 4   x  >= n -->  (n+1)
   *       case 5   n < x   -->  n
   *       case 6   n <= x  --> (n+1)
   *       case 7   n  > x  -->  (n)
   *       case 8   n  >= x -->  (n+1)
   *
   *      @param e1      A formula (e4 = (e2-e1)/e3) is referred to.
   *      @param e2      A formula (e4 = (e2-e1)/e3) is referred to.
   *      @param e3      A formula (e4 = (e2-e1)/e3) is referred to.
   *
   **/
  private long GetCondition_No(long e1, long e2, long e3)
  {
    Exp Cond;
    Exp Exp;
    int op;
    long e4;

    fUtil.Trace("---pass:GetCondition_No", 1);
    boolean IsEqu = false;

    Cond = fLoopTable.LoopStmt.getLoopStartCondition();
    op = Cond.getOperator();

    fUtil.Trace("---pass:GetCondition_No op= " + op, 1);

    if (op == HIR.OP_CMP_GE || op == HIR.OP_CMP_LE) {
      IsEqu = true; // case 2,4,6,8

    }
    if (IsEqu == true) {
      e4 = (long)((e2 - e1) / e3) + 1;
    }
    else {
      e4 = (long)Math.ceil((double)(e2 - e1) / (double)e3);

    }
    return e4;
  }

  /**
   *
   * GetCondition_Exp :
   *
   *   The condition exp  of a loop is acquired.
   *
   *   Normalize=> (1)OP=CMP_LT
   *               (2) STEP =+1
   *
   *       case 1   x < n   -->  n
   *       case 2   x <= n  --> (n+1)
   *       case 3   x  > n  -->  n
   *       case 4   x  >= n -->  (n+1)
   *       case 5   n < x   -->  n
   *       case 6   n <= x  --> (n+1)
   *       case 7   n  > x  -->  (n)
   *       case 8   n  >= x -->  (n+1)
   *
   *      @param e1      A formula (e4 = (e2-e1)/e3) is referred to.
   *      @param e2      A formula (e4 = (e2-e1)/e3) is referred to.
   *      @param e3      A formula (e4 = (e2-e1)/e3) is referred to.
   *
   *
   *
   **/
  //##70 private Exp GetCondition_Exp(Exp e1,Exp e2,long e3) {
  private Exp GetCondition_Exp(Exp pE1, Exp pE2, long e3)
  {
    Exp Cond;
    Exp Exp;
    int op;
    Exp e3Exp;
    Exp e4Exp;
    Exp e1 = (Exp)pE1.copyWithOperands(); //##70
    Exp e2 = (Exp)pE2.copyWithOperands(); //##70

    boolean IsEqu = false;
    fUtil.Trace("---pass:GetCondition_Exp", 1);

    Cond = fLoopTable.LoopStmt.getLoopStartCondition();
    op = Cond.getOperator();
    e3Exp = (Exp)hir.intConstNode((int)e3);
    if (op == HIR.OP_CMP_GE || op == HIR.OP_CMP_LE) {
      IsEqu = true; // case 2,4,6,8

    }
    if (IsEqu == true) {
      if (e3 == 1) {
        fLoopTable.repeat_no_normalize += 1;
        e4Exp = new ExpImpl(HirRoot, HIR.OP_ADD,
          new ExpImpl(HirRoot, HIR.OP_SUB, e2, e1),
          hir.intConstNode(1));
      }
      else if (e3 == -1) {
        fLoopTable.repeat_no_normalize *= -1;
        fLoopTable.repeat_no_normalize += 1;
        e4Exp = new ExpImpl(HirRoot, HIR.OP_ADD,
          new ExpImpl(HirRoot, HIR.OP_DIV,
          new ExpImpl(HirRoot, HIR.OP_SUB, e2, e1),
          e3Exp), hir.intConstNode(1));
      }
      else {
        fLoopTable.repeat_no_normalize += 1;
        e4Exp = new ExpImpl(HirRoot, HIR.OP_ADD,
          new ExpImpl(HirRoot, HIR.OP_DIV,
          new ExpImpl(HirRoot, HIR.OP_SUB, e2, e1),
          e3Exp), hir.intConstNode(1));
      }
    }
    else {
      if (e3 == 1) {
        e4Exp = new ExpImpl(HirRoot, HIR.OP_SUB, e2, e1);
      }
      else if (e3 == -1) {
        fLoopTable.repeat_no_normalize *= -1;
        e4Exp = new ExpImpl(HirRoot, HIR.OP_SUB, e1, e2);
      }
      else {
        e4Exp = new ExpImpl(HirRoot,
          HIR.OP_DIV,
          new ExpImpl(HirRoot, HIR.OP_ADD,
          new ExpImpl(HirRoot, HIR.OP_ADD,
          new ExpImpl(HirRoot, HIR.OP_SUB, e2, e1), e3Exp),
          hir.intConstNode( -1)), e3Exp);
      }
    }
    fUtil.Trace(" e4Exp " + e4Exp.toStringWithChildren(), 3); //###

    return e4Exp;
  }

  /**
   *
   * SetInductionLastData:
   *
   * The lastvalue of an induction variable is set up.
   *
   **/
  private void SetInductionLastData()
  {
    long LastValue = 0; // LoopControl var (Last Value)

    fUtil.Trace("---pass:SetInductionLastData", 1);
    if (fLoopTable.const_repeat_no == false) {
      return;
    }

    LastValue = fLoopTable.LoopCtrInduction.LastConstValue;

    for (ListIterator Ie = fLoopTable.IndList.listIterator(); Ie.hasNext(); ) {
      BasicInduction IndTable = (BasicInduction)Ie.next();
      IndTable.SetInductionLastData(LastValue, fUtil);
    }
  }

  /**
   *
   * induction:
   *
   * induction  is looked for.
   *
   **/
  private void induction()
  {
    ListIterator Ie;
    Iterator Iu;
    BBlock CurrBlock;
    IR IRnode;
    Exp LeftExp, RightExp, Exp1, Exp2;
    VarNode DefVarNode, UseVarNode, InitDefNode;
    Const ConstSym;
    UDChain indUDChain;
    UDList indUDList;
    long inc;
    int Op;
    boolean ind_flag;
    BasicInduction IndTable;

    fUtil.Trace("---pass:induction", 1);

    for (Ie = fLoopTable.InnerBBlockList.listIterator(); Ie.hasNext(); ) {
      CurrBlock = (BBlock)Ie.next();
      fUtil.Trace("inductionBBlock =" + CurrBlock.getBBlockNumber(), 1);
      if (fUtil.dom_check(CurrBlock, ExitBodyBBlock) != true) {
        continue;
      }
      SetRefReprList llist = (SetRefReprList)fResults.get("BBlockSetRefReprs",
        CurrBlock);

      for (Iterator Ie1 = llist.iterator(); Ie1.hasNext(); ) {
        SetRefRepr lSetRefRepr = (SetRefRepr)Ie1.next();
        IRnode = lSetRefRepr.getIR();
        ind_flag = false;
        inc = 0;

        if (IRnode == null) {
          break;
        }
        if (IRnode.getOperator() != HIR.OP_ASSIGN) {
          continue;
        }

        LeftExp = ((AssignStmt)IRnode).getLeftSide();

        RightExp = ((AssignStmt)IRnode).getRightSide();
        LeftExp = fUtil.SkipConv(LeftExp);
        RightExp = fUtil.SkipConv(RightExp);
        Op = RightExp.getOperator();

        fUtil.Trace("Debug IR=" + IRnode.toString(), 1);
        fUtil.Trace("Debug Left=" + LeftExp.toString(), 1);
        if (fUtil.IsVarNode((HIR)LeftExp) != true) {
          continue;
        }
        fUtil.Trace("Debug LeftOp=" + LeftExp.getOperator(), 1);

        if (fUtil.def_check(fLoopTable, (AssignStmt)IRnode) != true) {
          continue;
        }
        fUtil.Trace("Debug LeftOp2=" + LeftExp.getOperator(), 1);
        //
        // Get DefVarNode,UseVarNode
        //
        // [Stmt]
        //   DefVarNode = UseVarNode + inc
        //   DefVarNode = inc        + UseVarNode
        //   DefVarNode = UseVarNode - inc
        //

        DefVarNode = (VarNode)LeftExp;
        UseVarNode = null;
        if (Op == HIR.OP_ADD) {
          Exp1 = fUtil.SkipConv(RightExp.getExp1());
          Exp2 = fUtil.SkipConv(RightExp.getExp2());
          if (fUtil.IsVarNode((HIR)Exp1) == true) {
            //
            // DefVarNode = UseVarNode + inc
            //
            if (DefVarNode.getVar() != Exp1.getVar()) {
              continue;
            }
            if (Exp2.getOperator() != HIR.OP_CONST) {
              continue;
            }
            UseVarNode = (VarNode)Exp1;
            ConstSym = ((ConstNode)Exp2).getConstSym();
            if (ConstSym.getSymType().isInteger() == false) {
              continue;
            }
            inc = ConstSym.intValue();
            ind_flag = true;
          }
          else {
            if (DefVarNode.getVar() != Exp2.getVar()) {
              continue;
            }
            if (Exp1.getOperator() != HIR.OP_CONST) {
              continue;
            }
            UseVarNode = (VarNode)Exp2;
            ConstSym = ((ConstNode)Exp1).getConstSym();
            if (ConstSym.getSymType().isInteger() == false) {
              continue;
            }
            inc = ConstSym.intValue();
            ind_flag = true;
          }
        }
        else if (Op == HIR.OP_SUB) {
          Exp1 = fUtil.SkipConv(RightExp.getExp1());
          Exp2 = fUtil.SkipConv(RightExp.getExp2());
          if (fUtil.IsVarNode(Exp1) == true) {
            if (DefVarNode.getVar() != Exp1.getVar()) {
              continue;
            }
            if (Exp2.getOperator() != HIR.OP_CONST) {
              continue;
            }
            UseVarNode = (VarNode)Exp1;
            ConstSym = ((ConstNode)Exp2).getConstSym();
            if (ConstSym.getSymType().isInteger() == false) {
              continue;
            }
            inc = ConstSym.intValue() * -1;
            ind_flag = true;
          }
          else {
            if (DefVarNode.getVar() != Exp2.getVar()) {
              continue;
            }
            if (Exp1.getOperator() != HIR.OP_CONST) {
              continue;
            }
            UseVarNode = (VarNode)Exp2;
            ConstSym = ((ConstNode)Exp1).getConstSym();
            if (ConstSym.getSymType().isInteger() == false) {
              continue;
            }
            inc = ConstSym.intValue();
            ind_flag = true;
          }
        }
        else {
          continue;
        }
        if (inc == 0) {
          continue;
        }

        IndTable = make_BasicindT(
          (AssignStmt)IRnode, DefVarNode, UseVarNode, inc);
        make_ind(IndTable);

      } // SubTree
    } // BBlockList

  }

  /**
   *
   * make_BasicindT:
   *
   * An induction table is created.
   *
   * @param pstmt HIR-AssignStmt
   * @param Def   HIR-DefNode
   * @param Use   HIR-UseNode
   * @param inc   increment value
   *
   *
   *
   **/
  private BasicInduction
    make_BasicindT(AssignStmt pstmt, VarNode Def, VarNode Use, long inc)
  {

    BasicInduction IndTable = new BasicInduction(pstmt, Def, Use, inc);

    UDChain indUDChain;
    UDList indUDList;
    Stmt opStmt;
    Exp RightExp;
    Const ConstSym;
    int InitDefCount = 0;
    VarNode InitDef = null;

    fUtil.Trace("---pass:make_BasicInduction", 1);
    fUtil.Trace("make_BasicInduction=" + Def.toString(), 5);
    fUtil.Trace("make_BasicInduction Parent=" + Def.getParent().toString(), 5);

    fLoopTable.IndList.add(IndTable);
    IndTable.InitConstFlag = false;
    IndTable.loop_ctr = false;
    IndTable.InitConstValue = 0;

    //
    //  Get UD-Chain
    //

    indUDList = (UDList)fResults.get("UDList", Use.getVar(), fSubpFlow);
    indUDChain = indUDList.getUDChain(Use);

    for (Iterator Ie = indUDChain.getDefList().iterator(); Ie.hasNext(); ) {
      //VarNode Node = (VarNode)Ie.next();
      IR Node = (IR)Ie.next();
      if (Node == UDChain.UNINITIALIZED) {
        continue;
      }
      if (Node.getOperator() != HIR.OP_ASSIGN) {
        continue;
      }

      fUtil.Trace("make_BasicInduction :Node=" + Node.toString(), 5);
      HIR ChildNode = (HIR)((AssignStmt)Node).getLeftSide();
      if (ChildNode == Def) {
        continue;
      }
      if (fUtil.IsVarNode(ChildNode) == true) {
        fUtil.Trace("make_BasicInduction :ChildNode=" + ChildNode.toString(), 5);
        InitDefCount++;
        InitDef = (VarNode)ChildNode;
      }
    }

    if (InitDefCount != 1) { // Check Only One
      InitDef = null;

    }
    if (InitDef != null) {
      fUtil.Trace("InitDefB " + InitDef +
                  " " +(BBlock)fResults.get("BBlockForNode", InitDef) +
                  " UseB " + Use +
                  " " + (BBlock)fResults.get("BBlockForNode", Use), 5);
      if (fUtil.dom_check(
        (BBlock)fResults.get("BBlockForNode", InitDef),
        (BBlock)fResults.get("BBlockForNode", Use)) == true) {
        fUtil.Trace("InitDef " + InitDef + " dominates Use " + Use, 5);
        opStmt = (Stmt)InitDef.getParent();
        if (opStmt.getOperator() == HIR.OP_ASSIGN) {
          RightExp = ((AssignStmt)opStmt).getRightSide();
          RightExp = fUtil.SkipConv(RightExp);
          if (RightExp.getOperator() == HIR.OP_CONST) {
            ConstSym = ((ConstNode)RightExp).getConstSym();
            if (ConstSym.getSymType().isInteger() == true) {
              IndTable.InitConstValue = ConstSym.intValue();
              IndTable.InitDefList.clear();
              IndTable.InitConstFlag = true;
            }
          }
        }
      }
    }
    if (IndTable.InitConstFlag == false) {

      // [Original]
      //   if ( x== y)
      //        j =1;
      //    eles
      //        j =2;
      //    for (i=0;i<10;i++) {
      //        j=j+1;
      //    }
      //
      // [Convert]
      //   if ( x== y)
      //        j =1;       //stmt1
      //    eles
      //        j =2;       // stmt2
      //    var0 = j;       // stmt3
      //    for (i=0;i<10;i++) {
      //        j=i*1+ var0;
      //    }
      //

      SubpDefinition subp = fSubpFlow.getSubpDefinition();
      SymTable LocalSymTable = subp.getSubpSym().getSymTable();

      Var tmpVar = LocalSymTable.generateVar(((HIR)Use).getType(),
        subp.getSubpSym());
      tmpVar.setSymType(((HIR)Use).getType());
      VarNode tmpNode = (VarNode)hir.varNode(tmpVar);

      //
      //  Add Conditional Part:
      //
      //   var0 = j;     // stmt3
      //

      HIR LeftNode = fUtil.getTopVarNode((HIR)Use);
      fLoopTable.addConditionPart.add(
        //##70    (Stmt) hir.assignStmt(tmpNode,(Exp)LeftNode.copyWithOperands())  );
        (Stmt)hir.assignStmt((VarNode)tmpNode.copyWithOperands(),
        (Exp)LeftNode.copyWithOperands())); //##70
      fLoopTable.addConditionDefList.add(tmpNode);
      fUtil.Trace("addConditionDefList=" + tmpNode.toString(), 5);

      IndTable.InitConstNode = tmpNode;

      // Add InitDefList (Assign or  Functions  or UnInitialized)
      //
      // j= 1 // stmt1
      // j= 2 // stmt2
      //

      indUDList = (UDList)fResults.get("UDList", Use.getVar(), fSubpFlow);
      indUDChain = indUDList.getUDChain(Use);
      for (Iterator Ie = indUDChain.getDefList().iterator(); Ie.hasNext(); ) {
        IR Node = (IR)Ie.next();
        if (Node == indUDChain.UNINITIALIZED) {
          continue;
        }
        if (fUtil.loop_body(fLoopTable, (HIR)Node) == false) {
          IndTable.InitDefList.add(Node);
        }
      }
    }
    IndTable.loop_ctr = IsLoopControl(Use, inc);
    if (IndTable.loop_ctr == true) {
      fLoopTable.LoopCtrInduction = IndTable;
    }
    return IndTable;
  }

  /**
   *
   * make_ind:
   *
   *
   * induction-Exp  is looked for.
   *
   * @param IndTable  indtiction table
   *
   **/
  private void make_ind(BasicInduction IndTable)
  {
    DefUseList DUList;
    DefUseCell DUChain;
    VarNode DefVarNode;
    Exp Exp1;
    Exp Exp2;
    HIR opStmt;
    int op;
    boolean ind_flag;
    long ind_init = 0;
    long ind_inc = 0;
    Exp ind_Exp = null;
    Const ConstSym;

    DefVarNode = IndTable.DefVarNode;
    fUtil.Trace("make_ind Def" + DefVarNode.toString(), 5);

    DUList = (DefUseList)fResults.get("DefUseList", DefVarNode.getVar(),
      fSubpFlow);
    fUtil.Trace("make_ind DefParent " + DefVarNode.getParent().toString(), 5);

    DUChain = (DefUseCell)DUList.getDefUseCell(fUtil.getParentASSIGN(DefVarNode));
    for (Iterator Iu = DUChain.getUseList().iterator(); Iu.hasNext(); ) {
      IR Use = (IR)Iu.next();
      if (!(Use instanceof VarNode)) {
        continue; // ? Function
      }
      VarNode UseNode = (VarNode)Use;
      ind_flag = false;
      fUtil.Trace("make_ind Loop Debug= " + UseNode.toString(), 5);
      if (IndTable.UseVarNode == UseNode) {
        continue;
      }
      if (fUtil.loop_body(fLoopTable, UseNode) == true) {
        fUtil.Trace("make_ind loop_body=true ", 5);
        HIR tmpHir = (HIR)fUtil.getTopVarNode((HIR)UseNode);
        opStmt = (HIR)tmpHir.getParent();
        fUtil.Trace("make_ind loop_body:pearent Stmt " + opStmt.toString(), 5);
        op = opStmt.getOperator();

        switch (op) {
          case HIR.OP_ADD: //  i+C or C+i
            Exp1 = fUtil.SkipConv(((Exp)opStmt).getExp1());
            if (Exp1 == UseNode) {
              Exp1 = fUtil.SkipConv(((Exp)opStmt).getExp2());
            }
            if (Exp1.getOperator() == HIR.OP_CONST) {
              ConstSym = ((ConstNode)Exp1).getConstSym();
              if (ConstSym.getSymType().isInteger() == true) {
                ind_flag = true;
                ind_Exp = (Exp)opStmt;
                ind_inc = 1;
                ind_init = ConstSym.intValue();
              }
            }
            break;
          case HIR.OP_SUB: //  i-C
            Exp1 = fUtil.SkipConv(((Exp)opStmt).getExp1());
            Exp2 = fUtil.SkipConv(((Exp)opStmt).getExp2());
            if (Exp1 == UseNode && Exp2.getOperator() == HIR.OP_CONST) {
              ConstSym = ((ConstNode)Exp2).getConstSym();
              if (ConstSym.getSymType().isInteger() == true) {
                ind_flag = true;
                ind_Exp = (Exp)opStmt;
                ind_inc = 1;
                ind_init = 0 - ConstSym.intValue();
              }
            }
            break;
          case HIR.OP_MULT: // C1*i+C2,i*C1+C2,C2+i*C1,C2+C1*i

            // C1*i-C2,i*C1-C2
            // C1*i
            Exp c1 = fUtil.SkipConv(((Exp)opStmt).getExp1());
            if (c1 == UseNode) {
              c1 = fUtil.SkipConv(((Exp)opStmt).getExp2()); // Change
            }
            if (c1.getOperator() == HIR.OP_CONST) {
              ConstSym = ((ConstNode)c1).getConstSym();
              if (ConstSym.getSymType().isInteger() == true) {
                ind_inc = ConstSym.intValue();

                IR ExpParent = opStmt.getParent();
                int opParent = ExpParent.getOperator();

                switch (opParent) {
                  case HIR.OP_ADD:
                    Exp1 = fUtil.SkipConv(((Exp)ExpParent).getExp1());
                    if (Exp1 == opStmt) {
                      Exp1 = fUtil.SkipConv(((Exp)ExpParent).getExp2());
                    }
                    if (Exp1.getOperator() == HIR.OP_CONST) {
                      ConstSym = ((ConstNode)Exp1).getConstSym();
                      if (ConstSym.getSymType().isInteger() == true) {
                        ind_flag = true;
                        ind_Exp = (Exp)ExpParent;
                        ind_init = ConstSym.intValue();
                      }
                    }
                    break;
                  case HIR.OP_SUB:
                    Exp1 = fUtil.SkipConv(((Exp)ExpParent).getExp2());
                    if (Exp1.getOperator() == HIR.OP_CONST) {
                      ConstSym = ((ConstNode)Exp1).getConstSym();
                      if (ConstSym.getSymType().isInteger() == true) {
                        ind_flag = true;
                        ind_Exp = (Exp)ExpParent;
                        ind_init = 0 - ConstSym.intValue();
                      }
                    }
                    break;
                    //case HIR.OP_INDEX : // i (C1*i + C2 : C2=0)
                  case HIR.OP_SUBS: // i (C1*i + C2 : C2=0)
                  case HIR.OP_ASSIGN:
                  case HIR.OP_CMP_EQ:
                  case HIR.OP_CMP_NE:
                  case HIR.OP_CMP_GT:
                  case HIR.OP_CMP_GE:
                  case HIR.OP_CMP_LT:
                  case HIR.OP_CMP_LE:
                    ind_flag = true;
                    ind_Exp = (Exp)opStmt;
                    ind_init = 0;
                    break;
                }
              } // if (Const)
            }
            break;
          case HIR.OP_SUBS: // i (C1*i + C2 : C1=1,C2=0)
            //case HIR.OP_INDEX :
          case HIR.OP_ASSIGN:
          case HIR.OP_CMP_EQ:
          case HIR.OP_CMP_NE:
          case HIR.OP_CMP_GT:
          case HIR.OP_CMP_GE:
          case HIR.OP_CMP_LT:
          case HIR.OP_CMP_LE:
            ind_flag = true;
            ind_Exp = UseNode;
            ind_init = 0;
            ind_inc = 1;
            break;
        }
        if (ind_flag == true) {
          make_indExpT(ind_Exp, ind_inc, ind_init, IndTable);
        }
      }
    } // Loop DU-Chain
  }

  /**
   *
   * make_indExpT:
   *
   * An induction-Exp table is created.
   *
   *
   * @param ind_ExpNode
   *
   *
   **/
  private void make_indExpT(Exp ind_ExpNode, long ind_inc, long ind_init,
    BasicInduction indTable)
  {
    fUtil.Trace("make_indExpT " + ind_ExpNode + " inc " + ind_inc + " init " + ind_init, 4); //##72
    IndExp indExpTable;
    boolean ConstFlg;
    long InitConstValue;
    long inc = 0;
    long init = 0;
    long last = 0;
    if (indTable.InitConstFlag == true) {
      ConstFlg = true;
      InitConstValue = indTable.InitConstValue;
    }
    else {
      ConstFlg = false;
      InitConstValue = 0;
    }
    // 2004.07.21 I.fukuda
    //if (fUtil.NodeOrder(indTable.UseVarNode,ind_ExpNode) > 0) {
    //    init= ind_init+(indTable.InitConstValue+indTable.inc)*ind_inc;
    //} else {
    //   init= ind_init+(indTable.InitConstValue)*ind_inc;
    //}
    fUtil.Trace("NodeOrder  Exp1= " + indTable.UseVarNode.toString(), 5);
    fUtil.Trace("NodeOrder  Exp2= " + ind_ExpNode.toString(), 5);
    //if (indTable.loop_ctr == true) {
    //    init= ind_init+(indTable.InitConstValue)*ind_inc;
    //} else {
    if (fUtil.NodeOrder(indTable.UseVarNode, ind_ExpNode) < 0) {
      //
      // Exp2;  a[j];
      // ..
      // Exp1;   j=j+1;
      init = ind_init + (indTable.InitConstValue) * ind_inc;
    }
    else {
      //
      // Exp1;  j= j+1;
      // ..
      // Exp2;  a[j];
      init = ind_init + (indTable.InitConstValue + indTable.inc) * ind_inc;
    }
    //}

    inc = ind_inc * indTable.inc;
    // ex)
    //     for (i=0;i<10;i++)
    //         2*i +3;
    //                     init  = 3+*inductin_initValue(0)*ind_inc(2) = 3
    //                     inc   = ind_ind(2)*inductin_inc(1)          = 2
    //                     last  = xxxxx
    //
    indExpTable = new IndExp();
    indExpTable.IndTable = indTable;
    indExpTable.setExpData(ind_ExpNode, inc, init, last, ConstFlg,
      indTable.InitConstNode);
    indExpTable.DebugIndExp(fUtil);
    indTable.indExpList.add(indExpTable);
    // Debug..
    //fUtil.Trace("Debug :Node"+ind_ExpNode.toString(),5);
    //fUtil.Trace("Debug :make_indExpT",5);
    //fLoopTable.DebugInductionList(fUtil);
    //fUtil.Trace("Debug :make_indExpT",5);

  }

  /**
   *
   * reduction:
   *
   *
   *
   **/
  private void reduction()
  {
    ListIterator Ie;
    BBlock CurrBlock;
    IR IRnode;
    Exp LeftExp, RightExp;
    DefUseList DUList;
    DefUseCell DUChain;
    VarNode DefVarNode, UseVarNode, InitDefNode;
    int Op;

    fUtil.Trace("---pass:reduction", 1);

    for (Ie = fLoopTable.BBlockList.listIterator(); Ie.hasNext(); ) {
      CurrBlock = (BBlock)Ie.next();
      if (fUtil.dom_check(CurrBlock, ExitBodyBBlock) != true) {
        continue;
      }
      SetRefReprList llist = (SetRefReprList)fResults.get("BBlockSetRefReprs",
        CurrBlock);

      for (Iterator Ie1 = llist.iterator(); Ie1.hasNext(); ) {
        SetRefRepr lSetRefRepr = (SetRefRepr)Ie1.next();
        IRnode = lSetRefRepr.getIR();

        if (IRnode == null) {
          break;
        }
        if (IRnode.getOperator() != HIR.OP_ASSIGN) {
          continue;
        }
        LeftExp = ((AssignStmt)IRnode).getLeftSide();
        RightExp = ((AssignStmt)IRnode).getRightSide();
        LeftExp = fUtil.SkipConv(LeftExp);
        RightExp = fUtil.SkipConv(RightExp);

        if (fUtil.IsVarNode((HIR)LeftExp) == false) {
          continue;
        }
        if (fUtil.IsInduction(fLoopTable, (VarNode)LeftExp) == true) {
          continue;
        }
        if (fUtil.def_check(fLoopTable, (AssignStmt)IRnode) == false) {
          continue;
        }
        DUList = (DefUseList)fResults.get("DefUseList", LeftExp.getVar(),
          fSubpFlow);
        fUtil.Trace("Assign=" + fUtil.getParentASSIGN(LeftExp).toString(), 5);
        DUChain = (DefUseCell)DUList.getDefUseCell(fUtil.getParentASSIGN(
          LeftExp));
        int useCount = 0;
        UseVarNode = null;
        for (Iterator Iu = DUChain.getUseList().iterator(); Iu.hasNext(); ) {
          IR Use = (IR)Iu.next();
          if (!(Use instanceof VarNode)) {
            continue;
          }
          if (fUtil.loop_body(fLoopTable, (VarNode)Use) == false) {
            continue;
          }

          UseVarNode = (VarNode)Use;
          useCount++;

        }
        if (useCount != 1) {
          continue;
        }
        if (getRightReductionVar((Exp)LeftExp, (Exp)RightExp) != UseVarNode) {
          continue;
        }
        make_ReductionT((AssignStmt)IRnode, UseVarNode);
      }
    } // BBlockList
    InnerLoopReduction();
  }

  /**
   *
   * InnerLoopReduction:
   *
   *
   *   for(i=0;i<10;i++)  <---  add  Reductin v
   *   for(j=0;j<10;j++)  <==   innerLoop Reduction v
   *
   *
   *
   **/
  private void InnerLoopReduction()
  {
    FlowAnalSymVector ddef;
    FlowAnalSymVector use;
    LinkedList ReductionList = new LinkedList();
    LinkedList RemoveList = new LinkedList();

    for (Iterator Ie = fLoopTable.InnerLoopList.listIterator(); Ie.hasNext(); ) {
      LoopTable lTable = (LoopTable)Ie.next();
      addReductionList(ReductionList, lTable.ReductionADDList);
      addReductionList(ReductionList, lTable.ReductionMULList);
      addReductionList(ReductionList, lTable.ReductionSUBList);
    }

    for (Iterator Ie1 = fLoopTable.BBlockList.listIterator(); Ie1.hasNext(); ) {

      BBlock CurrBlock = (BBlock)Ie1.next();
      ddef = (FlowAnalSymVector)fResults.get("DDefined", CurrBlock);
      use = (FlowAnalSymVector)fResults.get("PUsed", CurrBlock);
      Set ddefsymset = (Set)ddef.flowAnalSyms();
      Set usesymset = (Set)use.flowAnalSyms();
      RemoveList.clear();
      for (Iterator Ie2 = ReductionList.listIterator(); Ie2.hasNext(); ) {
        Reduction reduction = (Reduction)Ie2.next();
        if (fResults.get("BBlockForNode", reduction.DefVarNode) == CurrBlock) {
          continue;
        }
        if (ddefsymset.contains(reduction.DefVarNode.getVar())) {
          RemoveList.add(reduction);
          continue;
        }
        if (usesymset.contains(reduction.DefVarNode.getVar())) {
          RemoveList.add(reduction);
          continue;
        }
      }
      for (Iterator Ie3 = RemoveList.listIterator(); Ie3.hasNext(); ) {
        Reduction reduction = (Reduction)Ie3.next();
        ReductionList.remove(reduction);
      }
    }
    //
    // add Reduction List
    //
    for (Iterator Ie4 = ReductionList.listIterator(); Ie4.hasNext(); ) {
      Reduction reduction = (Reduction)Ie4.next();
      switch (reduction.op) {
        case HIR.OP_ADD:
          fLoopTable.ReductionADDList.add(reduction);
          break;
        case HIR.OP_MULT:
          fLoopTable.ReductionMULList.add(reduction);
          break;
        case HIR.OP_SUB:
          fLoopTable.ReductionSUBList.add(reduction);
          break;
      }
    }
  }

  /**
   *
   * addReductionList:
   *
   *
   *
   **/
  private void addReductionList(LinkedList to, LinkedList from)
  {

    for (Iterator Ie = from.listIterator(); Ie.hasNext(); ) {
      Reduction fromReduction = (Reduction)Ie.next();
      boolean addFlag = true;
      for (Iterator Ie1 = to.listIterator(); Ie1.hasNext(); ) {
        Reduction toReduction = (Reduction)Ie1.next();
        if (fromReduction.DefVarNode.getVar() == toReduction.DefVarNode.getVar()) {
          to.remove(toReduction);
          addFlag = false;
          break;
        }
      }
      if (addFlag == true) {
        to.add(fromReduction);
      }
    }
  }

  /**
   *
   * getRightReduction:
   *
   *
   *
   **/
  private VarNode getRightReductionVar(Exp LeftExp, Exp RightExp)
  {

    int ChildCount;
    int i;
    VarNode Var;

    if (fUtil.IsVarNode(RightExp) == true) {
      if (fUtil.EQVar(LeftExp, (VarNode)RightExp) == true) {
        return (VarNode)RightExp;
      }
      else {
        return (VarNode)null;
      }
    }

    ChildCount = RightExp.getChildCount();

    if (ChildCount != 0) {
      for (i = 1; i <= ChildCount; i++) {
        HIR ChildNode = (HIR)RightExp.getChild(i);
        if (!(ChildNode instanceof Exp)) {
          return (VarNode)null; // ex) Functions
        }
        Var = getRightReductionVar(LeftExp, (Exp)ChildNode);
        if (Var != null) {
          return Var;
        }
      }
    }
    return (VarNode)null;
  }

  /**
   *
   * make_ReductionT:
   *
   *
   *
   **/
  private void make_ReductionT(AssignStmt assignNode, VarNode RightExp)
  {
    HIR p;
    HIR Var;
    int subCount = 0;
    int mulCount = 0;
    int addCount = 0;
    int nodeCount = 0;
    Reduction reduct;

    VarNode LeftExp = (VarNode)fUtil.SkipConv((Exp)assignNode.getLeftSide());
    Var = (HIR)fUtil.getTopVarNode((HIR)RightExp);
    p = (HIR)Var.getParent();

    while (assignNode != p) {
      if (p.getOperator() == HIR.OP_ADD) {
        addCount++;
      }
      else if (p.getOperator() == HIR.OP_MULT) {
        mulCount++;
      }
      else if (p.getOperator() == HIR.OP_SUB) {
        subCount++;
      }
      nodeCount++;
      p = (HIR)p.getParent();
    }

    if (addCount == nodeCount) {
      //
      //  ex )   v = v + x[i]; // reduction(v:+)
      //
      fLoopTable.ReductionADDList.add((Reduction)
        new Reduction(assignNode, LeftExp, RightExp, HIR.OP_ADD));

    }
    else if (mulCount == nodeCount) {
      //
      //  ex )   v = v * x[i];  // reduction(v:*)
      //
      fLoopTable.ReductionMULList.add((Reduction)
        new Reduction(assignNode, LeftExp, RightExp, HIR.OP_MULT));
    }
    else if (subCount == nodeCount) {
      if (nodeCount == 1) {
        //
        //  ex )   v = v - x[i]; // reduction(v:-)
        //
        fLoopTable.ReductionSUBList.add((Reduction)
          new Reduction(assignNode, LeftExp, RightExp, HIR.OP_SUB));
      }
    }
  }

  /**
   *
   * IsLoopControl:
   *
   *
   *
   **/
  private boolean IsLoopControl(VarNode node, long inc)
  {
    Exp Cond;
    Exp Exp1;
    Exp Exp2;
    int op;
    boolean result;
    boolean leftUse;

    //
    // startCondition:
    //

    fUtil.Trace("---pass:IsLoopControl", 1);
    Cond = fLoopTable.LoopStmt.getLoopStartCondition();
    if (Cond == null) {
      return false;
    }
    op = Cond.getOperator();
    result = false;
    leftUse = false;
    Exp2 = null;

    switch (op) {
      case HIR.OP_CMP_GT:
      case HIR.OP_CMP_GE:
      case HIR.OP_CMP_LT:
      case HIR.OP_CMP_LE:
        if (fUtil.EQVar(fUtil.SkipConv(Cond.getExp1()), node) == true) {
          result = true;
          leftUse = true;
          Exp2 = fUtil.SkipConv(Cond.getExp2());
        }
        else if (fUtil.EQVar(fUtil.SkipConv(Cond.getExp2()), node) == true) {
          leftUse = false;
          result = true;
          Exp2 = fUtil.SkipConv(Cond.getExp1());
        }
        else {
          result = false;
        }
    }
    if (result == true) {
      if (Exp2.getOperator() == HIR.OP_CONST) {
        Const ConstSym = ((ConstNode)Exp2).getConstSym();
        if (ConstSym.getSymType().isInteger() == false) {
          result = false;
        }
        else {
          //  ex) repeat_no_value= 10  condition:i<10
          fLoopTable.repeat_no_value = ConstSym.intValue();
          fLoopTable.const_repeat_no = true;
        }
      }
      else {
        if (fLoopTable.fInv.IsInvariant(Exp2) == true) {
          result = true;
        }
        else {
          result = false;
        }
      }
      if (leftUse == true) {
        //
        // ex)
        //    for (i=0;i>10;i++)
        //    for (i=0;i<10;i--)
        //
        if ((op == HIR.OP_CMP_GE || op == HIR.OP_CMP_GT) && inc > 0) {
          result = false;
        }
        if ((op == HIR.OP_CMP_LE || op == HIR.OP_CMP_LT) && inc < 0) {
          result = false;
        }
      }
      else {
        //
        // ex)
        //    for (i=0;10<i;i--)
        //    for (i=0;10>i;i++)
        //
        if ((op == HIR.OP_CMP_GE || op == HIR.OP_CMP_GT) && inc < 0) {
          result = false;
        }
        if ((op == HIR.OP_CMP_LE || op == HIR.OP_CMP_LT) && inc > 0) {
          result = false;
        }
      }
    }
    return result;
  }

  /**
   *
   * make_ref:
   *
   *
   **/
  private boolean make_ref()
  {
    Ref_Table refTable;
    LinkedList stm_refP_DDEF;
    LinkedList stm_refP_MOD;
    LinkedList stm_refP_USE;
    LinkedList stm_refP_EUSE;

    HIR tmpHirNode;
    Ref_Array refArray;
    LinkedList tmpArrayNodeList;
    boolean para_flag;

    fUtil.Trace("---pass:make_ref", 1);

    para_flag = true;
    tmpArrayNodeList = new LinkedList();
    stm_refP_DDEF = new LinkedList();
    stm_refP_MOD = new LinkedList();
    stm_refP_USE = new LinkedList();
    stm_refP_EUSE = new LinkedList();

    for (ListIterator Ie = fLoopTable.InnerBBlockList.listIterator();
         Ie.hasNext(); ) {
      BBlock CurrBlock = (BBlock)Ie.next();
      refTable = new Ref_Table(fSubpFlow, CurrBlock);
      fLoopTable.refList.add(refTable);
      refTable.ddef = (FlowAnalSymVector)fResults.get("DDefined", CurrBlock);
      refTable.ddef.vectorCopy(refTable.mod);
      refTable.use = (FlowAnalSymVector)fResults.get("PUsed", CurrBlock);
      refTable.euse = (FlowAnalSymVector)fResults.get("PExposed", CurrBlock);

      //
      //    Set RefArray
      //    DDEF ,MODE,USE,EUSE (ARRAY)
      //
      //

      SetRefReprList llist = (SetRefReprList)fResults.get("BBlockSetRefReprs",
        CurrBlock);

      for (Iterator Ie1 = llist.iterator(); Ie1.hasNext(); ) {
        SetRefRepr lSetRefRepr = (SetRefRepr)Ie1.next();
        IR stmt = (IR)lSetRefRepr.getIR();
        stm_refP_DDEF.clear();
        stm_refP_DDEF.clear();
        stm_refP_MOD.clear();
        stm_refP_USE.clear();
        stm_refP_EUSE.clear();
        tmpArrayNodeList.clear();

        ArrayAnalysis.getArrayList(stmt, tmpArrayNodeList);

        int op = stmt.getOperator();

        if (op == HIR.OP_ASSIGN) {
          tmpHirNode = (HIR)fUtil.SkipConv(((AssignStmt)stmt).getLeftSide());
          if (tmpHirNode.getOperator() == HIR.OP_SUBS) {

            fUtil.Trace("make refArray:" + tmpHirNode.toString(), 5);
            refArray = ArrayAnalysis.make_ref_Array(
              (Exp)tmpHirNode, fLoopTable.IndList, fLoopTable.fInv);
            refArray.DebugArrayRef(fUtil);
            //ArrayAnalysis.addaryelm(stm_refP_DDEF,refArray);
            ArrayAnalysis.addaryelm(stm_refP_DDEF, refArray);
            ArrayAnalysis.addaryelm(stm_refP_MOD, refArray);

            tmpArrayNodeList.remove(tmpHirNode);
            for (ListIterator Ia = tmpArrayNodeList.listIterator(); Ia.hasNext(); ) {
              refArray = ArrayAnalysis.make_ref_Array(
                (Exp)Ia.next(), fLoopTable.IndList, fLoopTable.fInv);
              ArrayAnalysis.addaryelm(stm_refP_USE, refArray);
              ArrayAnalysis.addaryelm(stm_refP_EUSE, refArray);
            }
          }
          tmpHirNode = (HIR)fUtil.SkipConv(((AssignStmt)stmt).getRightSide());
        }
        else {
          tmpHirNode = (HIR)stmt;
        }

        tmpArrayNodeList.clear();
        ArrayAnalysis.getArrayList(tmpHirNode, tmpArrayNodeList);
        for (ListIterator Ia = tmpArrayNodeList.listIterator(); Ia.hasNext(); ) {
          refArray = ArrayAnalysis.make_ref_Array(
            (Exp)Ia.next(), fLoopTable.IndList, fLoopTable.fInv);
          ArrayAnalysis.addaryelm(stm_refP_USE, refArray);
          ArrayAnalysis.addaryelm(stm_refP_EUSE, refArray);
        }
        ArrayAnalysis.addaryelmList(stm_refP_USE, refTable.useArrayList);
        ArrayAnalysis.addaryelmList(stm_refP_MOD, refTable.modArrayList);
        ArrayAnalysis.subaryelmList(refTable.ddefArrayList, stm_refP_EUSE);
        ArrayAnalysis.addaryelmList(stm_refP_EUSE, refTable.euseArrayList);
        ArrayAnalysis.addaryelmList(stm_refP_DDEF, refTable.ddefArrayList);
      } // Loop  BBlock-SubTree
    } // Loop BBlock
    return setPrivateVariables();
  }

  /**
   *
   * setPrivateVariables:
   *
   *
   *
   **/
  private boolean setPrivateVariables()
  {
    Ref_Table refTable;
    boolean para_flag;

    fUtil.Trace("---pass:setPrivateVariables", 1);

    para_flag = true;

    //
    //   Set Private Variables.
    //
    for (ListIterator IeRef = fLoopTable.refList.listIterator(); IeRef.hasNext(); ) {
      refTable = (Ref_Table)IeRef.next();
      Set symset = refTable.ddef.flowAnalSyms(); // DDEF BitVector
      for (Iterator IeSym = symset.iterator(); IeSym.hasNext(); ) {
        Sym defVar = (Sym)IeSym.next();
        boolean dom_check = fUtil.dom_check(refTable.BasicBlock, ExitBodyBBlock);
        if (setLastPrivate(defVar, refTable.BasicBlock, dom_check) == false) {
          para_flag = false;
        }
      }

    } // Loop ref-List
    for (Iterator Ie = fLoopTable.InnerLoopList.listIterator(); Ie.hasNext(); ) {
      LoopTable lTable = (LoopTable)Ie.next();
      for (Iterator Ie1 = lTable.varUnParalellLastPrivateList.listIterator();
           Ie1.hasNext(); ) {
        fLoopTable.varUnParalellLastPrivateList.add(Ie1.next());
        fLoopTable.setParaFlag(fUtil, false, "setPrivateVariables");
        para_flag = false;
      }
      //if (lTable.getParaFlag(fUtil,"setPrivateVariavles") == false) {
      for (Iterator Ie1 = lTable.Private.iterator(); Ie1.hasNext(); ) {
        fLoopTable.Private.add(Ie1.next());
      }
      //}
      if (lTable.getParaFlag(fUtil, "setPrivateVariavles") == true) {
        for (Iterator Ie1 = lTable.addConditionDefList.iterator(); Ie1.hasNext(); ) {
          fLoopTable.Private.add(Ie1.next());
        }
      }
      for (Iterator Ie1 = lTable.LastPrivate.iterator(); Ie1.hasNext(); ) {
        VarNode varNode = (VarNode)Ie1.next();
        BBlock vBBlock = (BBlock)fResults.get("BBlockForNode", varNode);
        boolean dom_check;
        BBlock eBBlock = (BBlock)fResults.getBBlockForLabel(
          lTable.LoopStmt.getLoopEndLabel());
        if (lTable.zero_check == true) {
          dom_check = fUtil.dom_check(eBBlock, ExitBodyBBlock);
        }
        else {
          dom_check = false;
        }
        if (setLastPrivate((Sym)varNode.getVar(), vBBlock, dom_check) == false) {
          para_flag = false;
        }
      }
    }
    return para_flag;
  }

  /*
   *
   * setLastPrivate:
   *
   *
   **/
  private boolean setLastPrivate(Sym defVar, BBlock pBBlock, boolean dom_check)
  {
    boolean para_flag;
    fUtil.Trace("setLastPrivate " + defVar.getName(), 2); //##72
    DefUseList DUList = (DefUseList)fResults.get("DefUseList", defVar,
      fSubpFlow);
    List Cells = DUList.getDefUseCells();
    para_flag = true;
    for (Iterator Ic = Cells.iterator(); Ic.hasNext(); ) {
      DefUseCell DUChain = (DefUseCell)Ic.next();
      IR Node = (IR)DUChain.getDefNode();
      if (Node == DUChain.UNINITIALIZED) {
        continue;
      }
      if (Node.getOperator() != HIR.OP_ASSIGN) {
        continue; // ? Function
      }

      HIR ChildNode = (HIR)((AssignStmt)Node).getLeftSide();
      ChildNode = (HIR)fUtil.SkipConv((Exp)ChildNode);
      fUtil.Trace("DEBUG1-->case1-2=" + ChildNode.toString(), 5);
      if (fUtil.IsScalar(ChildNode) == false) {
        continue;
      }
      if (fResults.get("BBlockForNode", ChildNode) != pBBlock) {
        continue;
      }
      boolean privateExist = false;
      for (Iterator Iu = DUChain.getUseList().iterator(); Iu.hasNext(); ) {
        IR Use = (IR)Iu.next();
        if (!(Use instanceof VarNode)) {
          continue;
        }
        HIR UseNode = (HIR)Use;
        if (!(Use instanceof VarNode)) {
          int op = UseNode.getOperator();
          if (op == HIR.OP_SUBP) {
            SubpNode sNode = (SubpNode)UseNode;
            Subp sSym = sNode.getSubp();
            String fname = sSym.getName();
            //##71 PreDefinedFunctions PreDefined=new PreDefinedFunctions();
            //##71 if (PreDefined.contains(fname) == true)
            if (predefinedFunctions.contains(fname)) { //##71
              continue;
            }
          }
        }
        if (fUtil.loop_body(fLoopTable, UseNode) == false) {
          if (dom_check == true) {
            fUtil.Trace(" dom_check is true. Add to LastPrivate " + ChildNode.toStringWithChildren(), 2); //##72
            fLoopTable.LastPrivate.add(ChildNode);
            privateExist = false;
            break;
          }
          else {
            //
            // unParalleizable
            //
            privateExist = false;
            para_flag = false;
            Var var = ((VarNode)ChildNode).getVar();
            if (fLoopTable.varUnParalellLastPrivateList.contains(var) == false) {
              fLoopTable.varUnParalellLastPrivateList.add(var);
            }
            fLoopTable.setParaFlag(fUtil, false, "setLastPrivate");
          }
        }
        else {
          privateExist = true;
        }
      } // Loop DUChain
      if (privateExist == true) {
        if (fUtil.IsAutoVarNode(ChildNode) == true) {
          fUtil.Trace(" privateExit is true. add to Private " + ChildNode.toStringWithChildren(), 2); //##72
          fLoopTable.Private.add(ChildNode);
        }
        else {
          fUtil.Trace(" privateExit is true. add to LastPrivate " + ChildNode.toStringWithChildren(), 2); //##72
          fLoopTable.LastPrivate.add(ChildNode);
        }
      }
      else {
        // I.Fukuda
        // add 2005.03.09
        //
        if (para_flag == true) {
          if (fUtil.IsAutoVarNode(ChildNode) == false) {
            if (dom_check == false) {
              //
              // unParalleizable
              //
              privateExist = false;
              para_flag = false;
              Var var = ((VarNode)ChildNode).getVar();
              if (fLoopTable.varUnParalellLastPrivateList.contains(var) == false) {
                fLoopTable.varUnParalellLastPrivateList.add(var);
              }
            }
            else {
              fUtil.Trace(" dom_check is true. add to LastPrivate " + ChildNode.toStringWithChildren(), 2); //##72
              fLoopTable.LastPrivate.add(ChildNode);
            }
          }
        }
      }
    }
    return para_flag;
  }

  /**
   *
   * make_BlockRef:
   *
   *
   *
   **/
  private void make_BlockRef()
  {
    fUtil.Trace("make_BlockRef Entry " + EntryBodyBBlock.getBBlockNumber(), 5); //##70
    Ref_Table AllRef = getBBlockRef(EntryBodyBBlock);
    fTraversedBBlocks = new HashSet(); //##74
    integrateBlockRef(EntryBodyBBlock, ExitBodyBBlock, AllRef);
    reg_unify(AllRef, getBBlockRef(ExitBodyBBlock));
    fUtil.Trace("ExitBBlock=" + ExitBodyBBlock.getBBlockNumber(), 5);

    AllRef.ddef.vectorCopy(fLoopTable.ControlRef.ddef);
    AllRef.mod.vectorCopy(fLoopTable.ControlRef.mod);
    AllRef.use.vectorCopy(fLoopTable.ControlRef.use);
    AllRef.euse.vectorCopy(fLoopTable.ControlRef.euse);

    fLoopTable.ControlRef.modArrayList =
      ArrayAnalysis.make_refArrayCellList(AllRef.modArrayList,
      Ref_Array.REF_MOD);
    fLoopTable.ControlRef.useArrayList =
      ArrayAnalysis.make_refArrayCellList(AllRef.useArrayList,
      Ref_Array.REF_USE);
    fLoopTable.ControlRef.ddefArrayList =
      ArrayAnalysis.make_refArrayCellList(AllRef.ddefArrayList,
      Ref_Array.REF_DDEF);
    fLoopTable.ControlRef.euseArrayList =
      ArrayAnalysis.make_refArrayCellList(AllRef.euseArrayList,
      Ref_Array.REF_EUSE);
  }

  /**
   *
   * integrateBlockRef:
   *
   *
   **/
  private void integrateBlockRef(BBlock StartBBlock, BBlock EndBBlock,
    Ref_Table refTable)
  {
    BBlock CurrBBlock;
    BBlock DomBBlock;
    CurrBBlock = StartBBlock;
    fTraversedBBlocks.add(CurrBBlock); //##74
    DomBBlock = EndBBlock; //##70
    fUtil.Trace("---pass:integrateBlockRef", 1);
    refTable = getBBlockRef(StartBBlock);
    do {
      // Get DomBBlock;
      if (CurrBBlock == null) { //##70
        break; //##70
      }
      DomBBlock = CurrBBlock.getImmediatePostdominatorForSubpFlow();

      fUtil.Trace("Loop StartBBlock=" + StartBBlock.getBBlockNumber(), 5);
      fUtil.Trace("Loop EndBBlock=" + EndBBlock.getBBlockNumber(), 5);
      fUtil.Trace("Loop Curr=" + CurrBBlock.getBBlockNumber(), 5);
      if (DomBBlock != null) { //##70
        fUtil.Trace("Loop Dom=" + DomBBlock.getBBlockNumber(), 5);

      }
      if (fLoopTable.InnerBBlockList.contains(DomBBlock) == true) {
        if (IsRedundant(CurrBBlock) == false) { //  redundant Basic Block
          Ref_Table SuccALLRef = new Ref_Table(fSubpFlow, null);
          List SuccList = CurrBBlock.getSuccList();
          boolean SuccExist;
          SuccExist = false;

          for (ListIterator Ie = SuccList.listIterator(); Ie.hasNext(); ) {
            BBlock Succ = (BBlock)Ie.next();
            if (Succ != DomBBlock) {
              Ref_Table SuccRef = getBBlockRef(Succ);
              integrateBlockRef(Succ, DomBBlock, SuccRef);
              if (SuccExist == true) {
                reg_fuse(SuccALLRef, SuccRef);
              }
              else {
                SuccExist = true;
                reg_copy(SuccALLRef, SuccRef);
              }
            }
          }

          reg_unify(refTable, SuccALLRef);
          reg_unify(refTable, getBBlockRef(DomBBlock));
        }
        else {
          if (StartBBlock != CurrBBlock) {
            reg_unify(refTable, getBBlockRef(CurrBBlock));
          }
          DomBBlock = CurrBBlock.getImmediatePostdominatorForSubpFlow();
        }
        CurrBBlock = DomBBlock;
        // if (fTraversedBBlocks.contains(CurrBBlock)) //##74
        //   break; //##74
        fTraversedBBlocks.add(CurrBBlock); //##74
      }
      else {
         //
        // Inner Loop
        //
        //  for (i=0;i<10;i++)
        //     for (j=0;j<10;j++) <= ExpandLoop
        //
        //
        LoopTable ExpandLoop = Expand_Anal(fLoopTable, DomBBlock);
        if (ExpandLoop != null) { //##70
          reg_unify(refTable, ExpandLoop.ExpandRef);
          CurrBBlock = fUtil.LoopNextBBlock(ExpandLoop);
          // if (fTraversedBBlocks.contains(CurrBBlock)) //##74
          //   break; //##74
          fTraversedBBlocks.add(CurrBBlock); //##74
          reg_unify(refTable, getBBlockRef(CurrBBlock));
          //##70 BEGIN
        }
        else {
          CurrBBlock = EndBBlock;
          break;
          //##70 END
        }
      }

    }
    while (DomBBlock != EndBBlock);

    // Debug ...
    fUtil.Trace("REF refddef ", 5);
    fUtil.Trace(refTable.ddef.toStringDescriptive(), 5);
    fUtil.Trace("REF refmod ", 5);
    fUtil.Trace(refTable.mod.toStringDescriptive(), 5);
    fUtil.Trace("EUSEF refeuse ", 5);
    fUtil.Trace(refTable.euse.toStringDescriptive(), 5);
  }

  /**
   *
   * Expand_Anal:
   *
   *
   **/
  LoopTable Expand_Anal(LoopTable pLoopTable, BBlock pBBlock)
  {
    LoopTable lTable;
    lTable = null;
    fUtil.Trace("---pass:Expand_Anal", 1);

    for (Iterator Ie = fLoopTable.InnerLoopList.iterator(); Ie.hasNext(); ) {
      lTable = (LoopTable)Ie.next(); // Child Loop
      if (lTable.BBlockList.contains(pBBlock) == true) {
        break;
      }
    }
    if (lTable == null) {
      fUtil.Trace("---pass:Expand Error! ", 1);
      return null; //##70
    }
    lTable.ControlRef.mod.vectorCopy(lTable.ExpandRef.mod);
    lTable.ControlRef.use.vectorCopy(lTable.ExpandRef.use);
    lTable.ControlRef.euse.vectorCopy(lTable.ExpandRef.euse);
    //   IF (n> 0)
    //     DDEF(1)
    //    else
    //     DDEF(2) (null)
    //
    if (lTable.zero_check == true) {
      fUtil.Trace("DEBUG Expand DDEF (COPY) " + lTable.repeat_no_normalize, 1);
      lTable.ControlRef.ddef.vectorCopy(lTable.ExpandRef.ddef);
    }
    else {
      fUtil.Trace("DEBUG Expand DDEF (NULL) ", 1);
      lTable.ExpandRef.ddef = new FlowAnalSymVectorImpl(fSubpFlow);
    }

    //
    // Private
    //
    if (lTable.getParaFlag(fUtil, "ExpandAnal") == true) {
      for (Iterator Ie = lTable.Private.iterator(); Ie.hasNext(); ) {
        VarNode varNode = (VarNode)Ie.next();
        FlowAnalSym varSym = (FlowAnalSym)varNode.getVar();
        lTable.ExpandRef.ddef.remove(varSym);
        lTable.ExpandRef.mod.remove(varSym);
        lTable.ExpandRef.use.remove(varSym);
        lTable.ExpandRef.euse.remove(varSym);
      }
    }
    //
    // Array :
    //
    ArrayAnalysis.ExpandArrayList(lTable, lTable.ExpandRef.modArrayList,
      lTable.ControlRef.modArrayList, null, Ref_Array.REF_MOD);
    ArrayAnalysis.ExpandArrayList(lTable, lTable.ExpandRef.useArrayList,
      lTable.ControlRef.useArrayList, null, Ref_Array.REF_USE);
    ArrayAnalysis.ExpandArrayList(lTable, lTable.ExpandRef.ddefArrayList,
      lTable.ControlRef.ddefArrayList, null, Ref_Array.REF_DDEF);
    ArrayAnalysis.ExpandArrayList(lTable, lTable.ExpandRef.euseArrayList,
      lTable.ControlRef.euseArrayList, lTable.ControlRef.ddefArrayList,
      Ref_Array.REF_EUSE);
    LinkedList tmpddefList;
    tmpddefList = new LinkedList();
    ArrayAnalysis.ExpandArrayList(lTable, tmpddefList,
      lTable.ControlRef.ddefArrayList, null, Ref_Array.REF_DDEF);
    if (lTable.zero_check == true) {
      lTable.ExpandRef.ddefArrayList = tmpddefList;
    }
    else {
      lTable.ExpandRef.ddefArrayList = tmpddefList;
      //ArrayAnalysis.ExpandArrayDDEF(tmpddefList,lTable.ExpandRef.ddefArrayList);
    }
    return lTable;
  }

  /**
   *
   * IsRedunant:
   *
   *
   **/
  boolean IsRedundant(BBlock pBBlock)
  {
    List SuccList = pBBlock.getSuccList();
    if (SuccList.size() == 1) {
      BBlock SuccBBlock = (BBlock)SuccList.get(0);
      List PredList = SuccBBlock.getPredList();
      if (PredList.size() == 1) {
        return true;
      }
    }
    return false;
  }

  /**
   *
   * reg_unify:
   *
   *
   **/
  private void reg_unify(Ref_Table pre_ref, Ref_Table suc_ref)
  {

    FlowAnalSymVector tmpBit;
    LinkedList tmpList;

    fUtil.Trace("---pass:reg_unify", 1);

    tmpBit = fSubpFlow.flowAnalSymVector();
    tmpList = new LinkedList();

    // MOD = MOD(Pre) + MOD(Succ)
    // USE = USE(Pre) + USE(Succ)
    //EUSE =EUSE(Pre) + ( EUSE(Succ) - DDEF(Pre) )
    //DDEF =DDEF(Pre) + DDEF(Succ)

    // variables(scalar).
    pre_ref.mod.vectorOr(suc_ref.mod, pre_ref.mod);
    pre_ref.use.vectorOr(suc_ref.use, pre_ref.use);
    suc_ref.euse.vectorSub(pre_ref.ddef, tmpBit);
    pre_ref.euse.vectorOr(tmpBit, pre_ref.euse);
    pre_ref.ddef.vectorOr(suc_ref.ddef, pre_ref.ddef);

    // Array .
    ArrayAnalysis.addaryelmList(suc_ref.modArrayList, pre_ref.modArrayList);
    ArrayAnalysis.addaryelmList(suc_ref.useArrayList, pre_ref.useArrayList);

    ArrayAnalysis.addaryelmList(suc_ref.euseArrayList, tmpList);
    fUtil.Trace("DEBUG EUSESIZE=" + tmpList.size(), 5);
    ArrayAnalysis.subaryelmList(pre_ref.ddefArrayList, tmpList);
    ArrayAnalysis.addaryelmList(tmpList, pre_ref.euseArrayList);
    ArrayAnalysis.addaryelmList(suc_ref.ddefArrayList, pre_ref.ddefArrayList);
  }

  /**
   *
   * reg_fuse:
   *
   *
   **/
  private void reg_fuse(Ref_Table then_ref, Ref_Table else_ref)
  {

    fUtil.Trace("---pass:reg_fuse", 1);
    // MOD = MOD(Then) + MOD(Else)
    // USE = USE(Then) + USE(Else)
    //EUSE =EUSE(Then) +EUSE(Else)
    //DDEF =DDEF(Then) *DDEF(Else)

    // variables(scalar).
    else_ref.mod.vectorOr(then_ref.mod, then_ref.mod);
    else_ref.use.vectorOr(then_ref.use, then_ref.use);
    else_ref.euse.vectorOr(then_ref.euse, then_ref.euse);
    else_ref.ddef.vectorAnd(then_ref.ddef, then_ref.ddef);

    // Array .
    ArrayAnalysis.addaryelmList(else_ref.modArrayList, then_ref.modArrayList);
    ArrayAnalysis.addaryelmList(else_ref.useArrayList, then_ref.useArrayList);
    ArrayAnalysis.addaryelmList(else_ref.euseArrayList, then_ref.euseArrayList);
    fUtil.Trace("reg_fuse Then DDEF size=" + then_ref.ddefArrayList.size(), 5);
    fUtil.Trace("reg_fuse else DDEF size=" + else_ref.ddefArrayList.size(), 5);
    ArrayAnalysis.mularyelmList(else_ref.ddefArrayList, then_ref.ddefArrayList);
    fUtil.Trace("reg_fuse Then DDEF size=" + then_ref.ddefArrayList.size(), 5);
    fUtil.Trace("reg_fuse else DDEF size=" + else_ref.ddefArrayList.size(), 5);
  }

  /**
   *
   * reg_copy:
   *
   *
   **/
  private void reg_copy(Ref_Table to_ref, Ref_Table from_ref)
  {

    // variables(scalar).
    from_ref.mod.vectorCopy(to_ref.mod);
    from_ref.use.vectorCopy(to_ref.use);
    from_ref.euse.vectorCopy(to_ref.euse);
    from_ref.ddef.vectorCopy(to_ref.ddef);

    // Array .
    //ArrayAnalysis.addaryelmList(from_ref.modArrayList,to_ref.modArrayList);
    //ArrayAnalysis.addaryelmList(from_ref.useArrayList,to_ref.useArrayList);
    //ArrayAnalysis.addaryelmList(from_ref.euseArrayList,to_ref.euseArrayList);
    //ArrayAnalysis.addaryelmList(from_ref.ddefArrayList,to_ref.ddefArrayList);
    to_ref.modArrayList = from_ref.modArrayList;
    to_ref.useArrayList = from_ref.useArrayList;
    to_ref.euseArrayList = from_ref.euseArrayList;
    to_ref.ddefArrayList = from_ref.ddefArrayList;
  }

  /**
   *
   * getBBlockRef:
   *
   *
   **/
  private Ref_Table getBBlockRef(BBlock pBBlock)
  {
    for (Iterator Ie = fLoopTable.refList.iterator(); Ie.hasNext(); ) {
      Ref_Table BBlockRef = (Ref_Table)Ie.next();
      if (BBlockRef.BasicBlock == pBBlock) {
        return BBlockRef;
      }
    }
    return (Ref_Table)null;
  }

  /**
   *
   * ParallelCheck:
   *
   *
   **/
  private void ParallelCheck()
  {

    FlowAnalSymVector mod_euse;
    Set symset;

    fUtil.Trace("---pass:ParallelCheck", 1);
    fLoopTable.setParaFlag(fUtil, true, "ParallelCheck");

    //
    // Reduction:
    //
    for (Iterator Ie = fLoopTable.ReductionADDList.iterator(); Ie.hasNext(); ) {
      Reduction red = (Reduction)Ie.next();
      fLoopTable.Private.remove(red.DefVarNode);
      fLoopTable.Private.remove(red.UseVarNode);
      fLoopTable.LastPrivate.remove(red.DefVarNode);
      fLoopTable.LastPrivate.remove(red.UseVarNode);
    }
    for (Iterator Ie = fLoopTable.ReductionSUBList.iterator(); Ie.hasNext(); ) {
      Reduction red = (Reduction)Ie.next();
      fLoopTable.Private.remove(red.DefVarNode);
      fLoopTable.Private.remove(red.UseVarNode);
      fLoopTable.LastPrivate.remove(red.DefVarNode);
      fLoopTable.LastPrivate.remove(red.UseVarNode);
    }
    for (Iterator Ie = fLoopTable.ReductionMULList.iterator(); Ie.hasNext(); ) {
      Reduction red = (Reduction)Ie.next();
      fLoopTable.Private.remove(red.DefVarNode);
      fLoopTable.Private.remove(red.UseVarNode);
      fLoopTable.LastPrivate.remove(red.DefVarNode);
      fLoopTable.LastPrivate.remove(red.UseVarNode);
    }
    //
    // Check var : mod-euse
    //
    mod_euse = new FlowAnalSymVectorImpl(fSubpFlow);
    fLoopTable.ControlRef.mod.vectorAnd(fLoopTable.ControlRef.euse, mod_euse);

    symset = mod_euse.flowAnalSyms();
    //fLoopTable.para_flag = true;

    for (Iterator IeSym = symset.iterator(); IeSym.hasNext(); ) {
      Var v = (Var)IeSym.next();
      fUtil.Trace("---ParallelCheck: mod_euse:" + v.toString(), 5);
      if (GetInduction(v) == null) {
        if (fUtil.IsReduction(fLoopTable, v) == true) {
          continue;
        }
        /*
                        if (fUtil.IsPrivate(fLoopTable,v) == true)
                            continue;
         */
        fLoopTable.setParaFlag(fUtil, false, "Check var mod:euse");
        fLoopTable.varResultList.add(v);

      }
    }
    ArrayAnalysis.TraceArrayCellList("MOD", fLoopTable.ControlRef.modArrayList);
    ArrayAnalysis.TraceArrayCellList("USE", fLoopTable.ControlRef.useArrayList);
    ArrayAnalysis.TraceArrayCellList("DDEF",
      fLoopTable.ControlRef.ddefArrayList);
    ArrayAnalysis.TraceArrayCellList("EUSE",
      fLoopTable.ControlRef.euseArrayList);

    //
    // [ARRAY]
    // true-dependence:
    //

    Judgement("true-dependence",
              fLoopTable.ControlRef.modArrayList,
              fLoopTable.ControlRef.euseArrayList,
              fLoopTable.mod_euseResultList);

    //
    // [ARRAY]
    // anti-dependence:
    //

    Judgement("anti-dependence",
              fLoopTable.ControlRef.useArrayList,
              fLoopTable.ControlRef.modArrayList,
              fLoopTable.use_modResultList);

    //
    // [ARRAY]
    // output-dependence:
    //
    fUtil.Trace("[ARRAY]output-dependence:", 5);
    Judgement("output-dependence",
              fLoopTable.ControlRef.modArrayList,
              fLoopTable.ControlRef.modArrayList,
              fLoopTable.mod_modResultList);

    //
    // addLastPrivate
    //

    for (Iterator Ie = fLoopTable.ArrayLastPrivate.iterator(); Ie.hasNext(); ) {
      Exp tmp = (Exp)Ie.next();
      while (tmp != null) {
        if (fUtil.IsVarNode((HIR)tmp)) {
          fUtil.Trace("ArrayLastPrivate" + tmp.toString(), 5);
          fLoopTable.LastPrivate.add(tmp);
          break;
        }
        tmp = tmp.getExp1();
      }
    }
  }

  /**
   *
   * Judgement:
   *
   *
   **/
  private void Judgement(String JudgeName, LinkedList ArrayCellList1,
    LinkedList ArrayCellList2,
    LinkedList indResultList)
  {
    boolean mod_flg;
    LinkedList refResultList;
    fUtil.Trace(JudgeName, 5);
    refResultList = new LinkedList();

    for (Iterator Ie1 = ArrayCellList1.iterator(); Ie1.hasNext(); ) {
      RefArrayCell Cell1 = (RefArrayCell)Ie1.next();
      for (Iterator Ie2 = ArrayCellList2.iterator(); Ie2.hasNext(); ) {
        RefArrayCell Cell2 = (RefArrayCell)Ie2.next();
        if (Cell1.ArrayName == Cell2.ArrayName) {
          for (Iterator Ie3 = Cell2.ArrayRef.iterator(); Ie3.hasNext(); ) {
            Ref_Array ref = (Ref_Array)Ie3.next();
            LinkedList JudgeList = new LinkedList();
            JudgeList.add(ref);

            if (Cell1.ArrayAnal == Ref_Array.REF_MOD &&
                Cell2.ArrayAnal == Ref_Array.REF_MOD) {
              mod_flg = true;
            }
            else {
              mod_flg = false;

              //
              // [ refJudge ]
              //
            }
            refResultList.clear();
            int refResult = ArrayAnalysis.refJudge(
              fLoopTable,
              Cell1.ArrayRef,
              Cell1.ArrayAnal,
              JudgeList,
              Cell2.ArrayAnal,
              refResultList,
              fLoopTable.ArrayLastPrivate);
            if (refResult != -1) {
              fUtil.Trace("refResult value =" + refResult, 1);
              fUtil.Trace("refResult size =" + refResultList.size(), 1);
              //
              // [ indJudge ]
              //
              int indResult = ArrayAnalysis.indJudge(
                fLoopTable,
                refResultList,
                mod_flg,
                indResultList,
                fLoopTable.ArrayLastPrivate);
              if (indResult != -1) {
                fUtil.Trace("result size =" + indResultList.size(), 1);
                fLoopTable.setParaFlag(fUtil, false, JudgeName);
                return;
              }
              else {
                return;
                //
                // Array Last-Private
                //
                //
                //if (fLoopTable.ArrayLastPrivate.size() !=0 ) {
                //    fUtil.Trace("Array-LastPrivate="+indResult,5);
                //    fUtil.Trace("result size ="+indResultList.size(),1);
                //    fUtil.Trace("result size ="+refResultList.size(),1);
                //    fLoopTable.setParaFlag(fUtil,false,JudgeName);
                //    ArrayAnalysis.ListAppend(refResultList,indResultList);
                //    fUtil.Trace("result size ="+indResultList.size(),1);
                //    fUtil.Trace("result size ="+refResultList.size(),1);
                //}
              }
            }
          }
        }
      }
    }
  }

  /**
   *
   * OpenMPCheck:
   *
   *
   **/
  private void OpenMPCheck()
  {
    Stmt nextStmt;
    int count;

    fUtil.Trace("---pass:OpenMPCheck", 1);

    //
    // init Part
    //

    nextStmt = fLoopTable.LoopStmt.getLoopInitPart();
    count = 0;
    while (nextStmt != null) {
      if (nextStmt.getOperator() == HIR.OP_BLOCK) {
        nextStmt = ((BlockStmt)nextStmt).getFirstStmt();
        continue;
      }
      if (nextStmt.getOperator() == HIR.OP_EXP_STMT) {
        nextStmt = nextStmt.getNextStmt();
        continue;
      }
      if (nextStmt.getOperator() != HIR.OP_ASSIGN) {
        fUtil.Trace("ERROR1" + nextStmt.getOperator(), 5);
        fLoopTable.setParaFlag(fUtil, false, "OpenMP (1)");
        return;
      }
      Exp LeftExp = ((AssignStmt)nextStmt).getLeftSide();
      LeftExp = fUtil.SkipConv(LeftExp);
      if (fUtil.IsVarNode(LeftExp) == false) {
        fLoopTable.setParaFlag(fUtil, false, "OpenMP (2)");
        return;
      }
      if (GetInduction((Var)LeftExp.getVar()) == null) {
        fLoopTable.setParaFlag(fUtil, false, "OpenMP (3)");
        return;
      }

      nextStmt = nextStmt.getNextStmt();
      ++count;
    }
    if (count == 0 && fLoopTable.addInit == null) {
      fLoopTable.setParaFlag(fUtil, false, "OpenMP (4)");
      return;
    }

    nextStmt = fLoopTable.LoopStmt.getLoopStepPart();
    count = 0;
    while (nextStmt != null) {
      if (nextStmt.getOperator() == HIR.OP_BLOCK) {
        nextStmt = ((BlockStmt)nextStmt).getFirstStmt();
        continue;
      }
      if (nextStmt.getOperator() == HIR.OP_EXP_STMT) {
        nextStmt = nextStmt.getNextStmt();
        return;
      }
      Exp LeftExp = ((AssignStmt)nextStmt).getLeftSide();
      LeftExp = fUtil.SkipConv(LeftExp);
      if (GetInduction((Var)LeftExp.getVar()) == null) {
        fLoopTable.setParaFlag(fUtil, false, "OpenMP (5)");
        return;
      }
      nextStmt = nextStmt.getNextStmt();
      ++count;
    }
    if (count == 0 && fLoopTable.addStep == null) {
      fLoopTable.setParaFlag(fUtil, false, "OpenMP (6)");
      return;
    }
    //
    // Common & Struct
    //
    /*
             for (Iterator Ie =fLoopTable.Private.iterator();Ie.hasNext();) {
            HIR privateNode =(HIR)Ie.next();
            if( privateNode.getParent().getOperator() == HIR.OP_QUAL) {
                fLoopTable.StructPrivate.add(privateNode);
            }
             }
       for (Iterator Ie =fLoopTable.LastPrivate.iterator();Ie.hasNext();) {
            HIR privateNode =(HIR)Ie.next();
            if( privateNode.getParent().getOperator() == HIR.OP_QUAL) {
                fLoopTable.StructPrivate.add(privateNode);
            }
             }
       for (Iterator Ie =fLoopTable.ArrayLastPrivate.iterator();Ie.hasNext();) {
            HIR privateNode =(HIR)Ie.next();
            if( privateNode.getParent().getOperator() == HIR.OP_QUAL) {
                fLoopTable.StructPrivate.add(privateNode);
            }
             }
             if (fLoopTable.StructPrivate.size() != 0) {
            fLoopTable.setParaFlag(fUtil,false,"OpenMP (7)");
            return ;
             }
     */
  }

  /**
   *
   * GetInduction:
   *
   *
   */
  private BasicInduction GetInduction(Var v)
  {
    for (Iterator Ie = fLoopTable.IndList.iterator(); Ie.hasNext(); ) {
      BasicInduction IndTable = (BasicInduction)Ie.next();
      if (IndTable.DefVarNode.getVar() == v) {
        return (IndTable);
      }
    }
    return null;
  }
}
