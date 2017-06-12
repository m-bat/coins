/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.mdf;

import coins.PassException;
import coins.ir.hir.ConstNode;
import coins.Registry;
import coins.ir.hir.HIR;
import coins.ir.IrList;
import coins.ir.hir.Stmt;
import coins.ir.hir.LabeledStmt;
import coins.ir.hir.ReturnStmt;
import coins.ir.hir.BlockStmt;
import coins.ir.hir.IfStmt;
import coins.ir.hir.ExpStmt;
import coins.ir.hir.Exp;
import coins.ir.hir.LoopStmt;
import coins.ir.hir.VarNode;
import coins.ir.hir.HirSeq;
import coins.ir.hir.SwitchStmt;
import coins.ir.hir.JumpStmt;
import coins.ir.hir.HirList;
import coins.ir.hir.SubpNode;
import coins.aflow.BBlock;
import coins.aflow.BBlockSubtreeIterator;
import coins.sym.FlowAnalSym;
import coins.sym.Param;
import coins.sym.Subp;
import coins.sym.Label;
import coins.sym.Sym;
import coins.sym.SymTable;
import coins.sym.Type;
import coins.sym.Var;

import java.util.ListIterator;
import java.util.LinkedList;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * Change the structure of the program into `switch-case' structure.
 * Each `case' structure include one macro-task.
 * This class also insert OpenMP pragmas, and wrap the `switch-case' structure
 * up in `while' loop.
 **/
class ChangeStructure{
  /** The threshold of debug output **/
  public static final int THR=MdfEnvironment.OptThr;
  /** The environment of the MDF module **/
  private MdfEnvironment env;
  /** The current macro flow graph **/
  private MacroFlowGraph mfg;
  /** The label which is the end of the `switch-case' structure **/
  private Label endLabel;
  /** The symbol table of the current function **/
  private SymTable symTab;
  /** The factory of HIR **/
  private HIR hirFact;
  /** The factory of symbols **/
  private Sym symFact;
  /** Declaration of the extern variables **/
  private DeclareExternVariables decExtern;
  /** Declaration of the PTEST variables **/
  private DeclarePtestVariables decPtest;
  /** Declaration of the global variables **/
  private DeclareGlobalVariables decGlobal;
  /** Declaration of the local variables **/
  private DeclareLocalVariables decLocal;
  /** Declaration of the pragmas **/
  private DeclarePragmas decPragma;
  /** The conditions in MDF **/
  private MdfConditions cond;
  /** The extra jump statement **/
  private JumpStmt extraJump=null;

  /**
   * Constructor.
   * Change the structure of the program.
   * @param e The environment of the MDF module
   * @param graph The current macro flow graph
   * @throws PassException Any exception in the MDF module 
   **/
  ChangeStructure(MdfEnvironment e,MacroFlowGraph graph) throws PassException{
    env=e;
    mfg=graph;
    cond=new MdfConditions(env,mfg);
    symTab=mfg.symTab;//subpDef.getSubpSym().getSymTable();
    hirFact=mfg.hirRoot.hir;
    symFact=mfg.symRoot.sym;
    decPragma=new DeclarePragmas(env,mfg);

    //mfg.subpDef.print(0);

    // The end label of the `switch-case' structure.
    endLabel=symTab.generateLabel();

    // Declarate the global variables and externed variables.
    // Set the global symbol table as the current one.
    mfg.symRoot.symTableCurrent=mfg.symRoot.symTableRoot;

    decExtern=new DeclareExternVariables(symFact,mfg.symRoot);
    decGlobal=new DeclareGlobalVariables(symFact,mfg.symRoot);

    if(env.opt.isSet("mdf-ptest")){
      decPtest=new DeclarePtestVariables(symFact,mfg.symRoot);
    }

    // Declarate the local variables.
    // Set the local symbol table as the current one.
    mfg.symRoot.symTableCurrent=mfg.symTab;

    decLocal=new DeclareLocalVariables(env,mfg);


    // This BLOCK means whole program. It is registerd with `mdf.subpDef'.
    BlockStmt allBlk=hirFact.blockStmt(null);

    // Initialize the variables related macro data flow processing.
    VarNode taskInitVar=hirFact.varNode((Var)mfg.taskSym);
    Stmt taskInitAssign=hirFact.assignStmt(taskInitVar,
                                           hirFact.intConstNode(-1));
    allBlk.addLastStmt(taskInitAssign);

    // Initialize the variable which checks whether the program is done.
    VarNode finishVar=hirFact.varNode(decLocal.allFinishVar);
    Stmt finishInitAssign=hirFact.assignStmt(finishVar,
                                             hirFact.intConstNode(0));
    allBlk.addLastStmt(finishInitAssign);

    // Initilize the current thread number (mdf_threadID=1).
    VarNode idVarNode=hirFact.varNode((Var)mfg.idSym);
    Stmt idAssign=hirFact.assignStmt(idVarNode,hirFact.intConstNode(0));
    allBlk.addLastStmt(idAssign);


    // Make `omp_init_lock'.
    VarNode ompLockNode=hirFact.varNode(decLocal.ompLock);
    Exp ompLockExp=hirFact.exp(HIR.OP_ADDR,ompLockNode);
    SubpNode ompInitLockNode=hirFact.subpNode(decExtern.initLock);
    IrList ompInitLockParam=hirFact.irList();
    ompInitLockParam.add(ompLockExp);
    Stmt ompInitLockCall=hirFact.callStmt(ompInitLockNode,ompInitLockParam);
    ompInitLockCall.addInf(Registry.INF_KIND_OPEN_MP,decPragma.ifdef);
    // Append `omp_init_lock' into the program.
    allBlk.addLastStmt(ompInitLockCall);

    // Initialize the dynamic scheduler.
    BlockStmt initTaskBlk=makeInitTask();
    initTaskBlk.addInf(Registry.INF_KIND_OPEN_MP,decPragma.endif);
    allBlk.addLastStmt(initTaskBlk);

    // add the statement which means that set the number of threads.
    int numThreads=env.DEFAULT_NUM_THREADS;
    if(env.opt.isSet("omp-threads")){
      numThreads=Integer.parseInt(env.opt.getArg("omp-threads"));
    }
    env.println("MDF : The number of threads ==> "+numThreads,THR);

    SubpNode ompSetNumThreadsNode=hirFact.subpNode(decExtern.setNumThreads);
    IrList ompSetNumThreadsParam=hirFact.irList();
    ompSetNumThreadsParam.add(hirFact.intConstNode(numThreads));
    Stmt ompSetNumThreadsCall=hirFact.callStmt(ompSetNumThreadsNode,
                                               ompSetNumThreadsParam);
    ompSetNumThreadsCall.addInf(Registry.INF_KIND_OPEN_MP,decPragma.ifdef);
    allBlk.addLastStmt(ompSetNumThreadsCall);
    Stmt nullStmt=hirFact.nullStmt();
    nullStmt.addInf(Registry.INF_KIND_OPEN_MP,decPragma.endif);
    allBlk.addLastStmt(nullStmt);

    // Make OpenMP pragma blocks.
    // #pragma omp parallel private(mdf_threadID) firstprivate(mdf_task)
    BlockStmt pragmaBlock=hirFact.blockStmt(null);

    pragmaBlock.addInf(Registry.INF_KIND_OPEN_MP,decPragma.parallel);

    // Append the pragmas into the program.
    allBlk.addLastStmt(pragmaBlock);

    if(env.opt.isSet("mdf-ptest")){
      // Make `mdf_watching_report'
      SubpNode watchingReportNode=hirFact.subpNode(decPtest.watchingReport);
      //IrList ompDestroyLockParam=hirFact.irList();
      //ompDestroyLockParam.add(ompLockExp);
      Stmt watchingReportCall=hirFact.callStmt(watchingReportNode,
                                               hirFact.irList());

      watchingReportCall.addInf(Registry.INF_KIND_OPEN_MP,decPragma.ptest);

      allBlk.addLastStmt(watchingReportCall);

      nullStmt=hirFact.nullStmt();
      nullStmt.addInf(Registry.INF_KIND_OPEN_MP,decPragma.endif);
      // Append `mdf_watching_report' into the program.
      allBlk.addLastStmt(nullStmt);
    }

    // Make `omp_destroy_lock'
    ompLockNode=hirFact.varNode(decLocal.ompLock);
    ompLockExp=hirFact.exp(HIR.OP_ADDR,ompLockNode);
    SubpNode ompDestroyLockNode=hirFact.subpNode(decExtern.destroyLock);
    IrList ompDestroyLockParam=hirFact.irList();
    ompDestroyLockParam.add(ompLockExp);
    Stmt ompDestroyLockCall=hirFact.callStmt(ompDestroyLockNode,
                                             ompDestroyLockParam);
    ompDestroyLockCall.addInf(Registry.INF_KIND_OPEN_MP,decPragma.ifdef);

    // Append `omp_destroy_lock' into the program.
    allBlk.addLastStmt(ompDestroyLockCall);

    // Make a return statement.
    //Stmt returnStmt=hirFact.returnStmt((Exp)hirFact.intConstNode(0));
    Stmt returnStmt=null;
    if(decLocal.returnStatus==null)
      returnStmt=hirFact.returnStmt();
    else{
      Exp statusExp=(Exp)hirFact.varNode(decLocal.returnStatus);
      returnStmt=hirFact.returnStmt(statusExp);
    }

    returnStmt.addInf(Registry.INF_KIND_OPEN_MP,decPragma.endif);
    allBlk.addLastStmt(returnStmt);

    // There are no more addition to `allBlk'.
    // Below here, this module append a pieces of program into `pragmaBlock'.

    // Make `id = omp_get_thread_num()'
    idVarNode=hirFact.varNode((Var)mfg.idSym);
    SubpNode getThreadNode=hirFact.subpNode(decExtern.getThreadNum);
    Stmt getThreadCall=hirFact.callStmt(getThreadNode,hirFact.irList());
    Stmt getThreadAssign=hirFact.assignStmt(idVarNode,
                                            ((ExpStmt)getThreadCall).getExp());
    getThreadAssign.addInf(Registry.INF_KIND_OPEN_MP,decPragma.ifdef);

    // Append the function which get the current thread number into 
    // `pragmaBlock'.
    pragmaBlock.addLastStmt(getThreadAssign);

    // Make `ifdef' and `endif'.
    nullStmt=hirFact.nullStmt();
    nullStmt.addInf(Registry.INF_KIND_OPEN_MP,decPragma.endif);
    pragmaBlock.addLastStmt(nullStmt);

    // Append `mdf_thread_start(mdf_threadID)' into `pragmaBlock'.
    if(env.opt.isSet("mdf-ptest")){
      idVarNode=hirFact.varNode((Var)mfg.idSym);
      SubpNode threadStartNode=hirFact.subpNode(decPtest.threadStart);
      IrList threadStartParam=hirFact.irList();
      threadStartParam.add(idVarNode);
      Stmt threadStartCall=hirFact.callStmt(threadStartNode,threadStartParam);

      threadStartCall.addInf(Registry.INF_KIND_OPEN_MP,decPragma.ptest);

   
      pragmaBlock.addLastStmt(threadStartCall);
      
      nullStmt=hirFact.nullStmt();
      nullStmt.addInf(Registry.INF_KIND_OPEN_MP,decPragma.endif);
      pragmaBlock.addLastStmt(nullStmt);
    }
    // Make the loop structure which wraps over all the current function.
    BlockStmt whileBody=hirFact.blockStmt(null);
    Exp whileCond=hirFact.exp(HIR.OP_CMP_NE,hirFact.intConstNode(1),
                              hirFact.intConstNode(0));
    Label whileEndLabel=symTab.generateLabel();
    Label whileBackLabel=symTab.generateLabel();
    Label whileStepLabel=symTab.generateLabel();

    // There are no more addition to `pramgaBlock'.
    // Below here, this module append a pieces of program into `whileBody'.

    // Make a break point from the loop, means `get out of the loop'.
    finishVar=hirFact.varNode(decLocal.allFinishVar);
    Exp breakWhileCond=hirFact.exp(HIR.OP_CMP_NE,finishVar,
                                   hirFact.intConstNode(0));
    JumpStmt breakWhileStmt=hirFact.jumpStmt(whileEndLabel);
    IfStmt breakWhileIf=hirFact.ifStmt(breakWhileCond,
                                       breakWhileStmt,null);

    // Append conditional branch expression which is a break point from
    // the loop into `whileBody'.
    whileBody.addLastStmt(breakWhileIf);
    
    //omp_set_lock(&flock);
    ompLockNode=hirFact.varNode(decLocal.ompLock);
    ompLockExp=hirFact.exp(HIR.OP_ADDR,ompLockNode);
    SubpNode ompSetLockNode=hirFact.subpNode(decExtern.setLock);
    IrList ompSetLockParam=hirFact.irList();
    ompSetLockParam.add(ompLockExp);
    Stmt ompSetLockCall=hirFact.callStmt(ompSetLockNode,ompSetLockParam);
    ompSetLockCall.addInf(Registry.INF_KIND_OPEN_MP,decPragma.ifdef);

    // Append `omp_set_lock' function to `whileBody'
    whileBody.addLastStmt(ompSetLockCall);
    
    BlockStmt getTaskBlk=makeGetTask();
    getTaskBlk.addInf(Registry.INF_KIND_OPEN_MP,decPragma.endif); 
    // Append `getTaskBlk' to `whileBody'
    whileBody.addLastStmt(getTaskBlk);
    
    //omp_unset_lock(&flock);
    ompLockNode=hirFact.varNode(decLocal.ompLock);
    ompLockExp=hirFact.exp(HIR.OP_ADDR,ompLockNode);
    SubpNode ompUnsetLockNode=hirFact.subpNode(decExtern.unsetLock);
    IrList ompUnsetLockParam=hirFact.irList();
    ompUnsetLockParam.add(ompLockExp);
    Stmt ompUnsetLockCall=hirFact.callStmt(ompUnsetLockNode,ompUnsetLockParam);
    ompUnsetLockCall.addInf(Registry.INF_KIND_OPEN_MP,decPragma.ifdef);

    // Append `omp_unset_lock' function to `whileBody'
    whileBody.addLastStmt(ompUnsetLockCall);

    // Make a `switch-case' structure.
    VarNode taskVar=hirFact.varNode((Var)mfg.taskSym);
    // The list of labels which mean the cases of the `switch-case' structure
    HirList labelList=(HirList)hirFact.irList(new LinkedList());
    BlockStmt switchBody=makeSwitchBody(labelList);
    HIR switchStmt=hirFact.switchStmt(taskVar,
                                      labelList,
                                      symTab.generateLabel(),
                                      switchBody,
                                      endLabel);
    //switchStmt.print(0);

    // Add `endif' statement into switch expression
    switchStmt.addInf(Registry.INF_KIND_OPEN_MP,decPragma.endif);
    // Append switch expression into `whileBody'
    whileBody.addLastStmt((Stmt)switchStmt);

    // Make while expression
    Stmt whileStmt=hirFact.whileStmt(whileBackLabel,
                                     whileCond,
                                     whileBody,
                                     whileStepLabel,
                                     whileEndLabel);
    whileStmt.removeInf(Registry.INF_KIND_OPEN_MP);

    // Append while expression to `pragmaBlock'
    pragmaBlock.addLastStmt(whileStmt);

    // Append `mdf_watching_end' function to `pragmaBlock'
    if(env.opt.isSet("mdf-ptest")){
      idVarNode=hirFact.varNode((Var)mfg.idSym);
      SubpNode watchingEndNode=hirFact.subpNode(decPtest.watchingEnd);
      IrList watchingEndParam=hirFact.irList();
      watchingEndParam.add(idVarNode);
      Stmt watchingEndCall=hirFact.callStmt(watchingEndNode,watchingEndParam);

      watchingEndCall.addInf(Registry.INF_KIND_OPEN_MP,decPragma.ptest);
    
      pragmaBlock.addLastStmt(watchingEndCall);
      
      nullStmt=hirFact.nullStmt();
      nullStmt.addInf(Registry.INF_KIND_OPEN_MP,decPragma.endif);
      pragmaBlock.addLastStmt(nullStmt);
    }

    // Register the current symbol table with the current program
    allBlk.setSymTable(symTab);
    mfg.subpDef.setHirBody(allBlk);
    //mfg.subpDef.print(0);
  }

  /**
   * Generate the initialize part of the MDF program.
   * @return The initialize part
   **/
  BlockStmt makeInitTask(){
    Util util=new Util(env,mfg);
    int arraySize=util.numberOfConditions();
    int vectSize=util.vectorSize();
    int condSize=mfg.bound();
    BlockStmt initTaskBlk=hirFact.blockStmt(null);


    // Initialize MDF_EXEC_COND, MDF_NON_EXEC_COND, MDF_DATA_ACCESS_COND
    Type type_uint=mfg.symRoot.typeU_Int;

    // make execNode and append
    for(int i=0;i<condSize;i++){
      int[] exec=cond.exec.bitVector(i);
      for(int j=0;j<vectSize;j++){
        VarNode execNode=hirFact.varNode(decLocal.execCond);
        Exp subExp1=hirFact.subscriptedExp(execNode,
                                           hirFact.intConstNode(i));
        Exp subExp2=hirFact.subscriptedExp(subExp1,
                                           hirFact.intConstNode(j));
        long tmp=Long.parseLong(Integer.toHexString(exec[j]),16);
        ConstNode cst=hirFact.constNode(symFact.intConst(tmp,type_uint));
        Stmt assignStmt=hirFact.assignStmt(subExp2,cst);
        initTaskBlk.addLastStmt(assignStmt);
      }
    }

    // make nonExecNode and append
    for(int i=0;i<condSize;i++){
      int[] nonExec=cond.nonExec.bitVector(i);
      for(int j=0;j<vectSize;j++){
        VarNode nonExecNode=hirFact.varNode(decLocal.nonExecCond);
        Exp subExp1=hirFact.subscriptedExp(nonExecNode,
                                           hirFact.intConstNode(i));
        Exp subExp2=hirFact.subscriptedExp(subExp1,
                                           hirFact.intConstNode(j));
        long tmp=Long.parseLong(Integer.toHexString(nonExec[j]),16);
        ConstNode cst=hirFact.constNode(symFact.intConst(tmp,type_uint));
        Stmt assignStmt=hirFact.assignStmt(subExp2,cst);
        initTaskBlk.addLastStmt(assignStmt);
      }
    }

    // make dataAccessNode and append
    for(int i=0;i<condSize;i++){
      int[] access=cond.access.bitVector(i);
      for(int j=0;j<vectSize;j++){
        VarNode dataAccessNode=hirFact.varNode(decLocal.dataAccessCond);
        Exp subExp1=hirFact.subscriptedExp(dataAccessNode,
                                           hirFact.intConstNode(i));
        Exp subExp2=hirFact.subscriptedExp(subExp1,
                                           hirFact.intConstNode(j));
        long tmp=Long.parseLong(Integer.toHexString(access[j]),16);
        ConstNode cst=hirFact.constNode(symFact.intConst(tmp,type_uint));
        Stmt assignStmt=hirFact.assignStmt(subExp2,cst);
        initTaskBlk.addLastStmt(assignStmt);
      }
    }

    // Initialize MDF_FINISH_COND
    for(int i=0;i<arraySize;i++){
      int[] finish=cond.finish.bitVector(i);
      for(int j=0;j<vectSize;j++){
        VarNode finishNode=hirFact.varNode(decLocal.finishCondition);
        Exp subExp1=hirFact.subscriptedExp(finishNode,
                                           hirFact.intConstNode(i));
        Exp subExp2=hirFact.subscriptedExp(subExp1,
                                           hirFact.intConstNode(j));
        long tmp=Long.parseLong(Integer.toHexString(finish[j]),16);
        ConstNode cst=hirFact.constNode(symFact.intConst(tmp,type_uint));
        Stmt assignStmt=hirFact.assignStmt(subExp2,cst);
        initTaskBlk.addLastStmt(assignStmt);
      }
    }

    // Initialize MDF_CURRENT_STATUS
    for(int i=0;i<vectSize;i++){
      VarNode statNode=hirFact.varNode(decLocal.curStat);
      Exp subExp1=hirFact.subscriptedExp(statNode,hirFact.intConstNode(i));
      Stmt assignStmt=hirFact.assignStmt(subExp1,hirFact.intConstNode(0));
      initTaskBlk.addLastStmt(assignStmt);
    }

    int[] readyTask=cond.readyTasks();
    int[] mark=new int[condSize];

    for(int i=0;i<condSize;i++) mark[i]=0;

    for(int i=0;i<readyTask.length;i++){
      mark[readyTask[i]]=1;
      VarNode readyQueueNode=hirFact.varNode(decLocal.readyQueue);
      Exp subExp1=hirFact.subscriptedExp(readyQueueNode,
                                         hirFact.intConstNode(i));
      Stmt assignStmt=hirFact.assignStmt(subExp1,
                                         hirFact.intConstNode(readyTask[i]));
      initTaskBlk.addLastStmt(assignStmt);
    }

    for(int i=0;i<condSize;i++){
      VarNode alreadyPutNode=hirFact.varNode(decLocal.alreadyPut);
      Exp subExp1=hirFact.subscriptedExp(alreadyPutNode,
                                         hirFact.intConstNode(i));
      Stmt assignStmt=hirFact.assignStmt(subExp1,
                                         hirFact.intConstNode(mark[i]));
      initTaskBlk.addLastStmt(assignStmt);
    }

    // _MDF_PUT_POS=0
    VarNode varNode=hirFact.varNode(decLocal.putPos);
    Stmt assign=hirFact.assignStmt(varNode,
                                   hirFact.intConstNode(readyTask.length));
    initTaskBlk.addLastStmt(assign);

    // _MDF_GET_POS=0
    varNode=hirFact.varNode(decLocal.getPos);
    assign=hirFact.assignStmt(varNode,hirFact.intConstNode(0));
    initTaskBlk.addLastStmt(assign);

    return(initTaskBlk);
  }

  /**
   * Generate the dynamic scheduler
   * @return The dynamic scheduler
   **/
  BlockStmt makeGetTask(){
    Util util=new Util(env,mfg);
    int arraySize=util.numberOfConditions();
    int vectSize=util.vectorSize();
    int condSize=mfg.bound();

    BlockStmt getTaskBlk=hirFact.blockStmt(null);
    SymTable localSymTab=mfg.symRoot.symTableCurrent.pushSymTable(null);
    getTaskBlk.setSymTable(localSymTab);

    Var iVar=symFact.defineVar("i".intern(),mfg.symRoot.typeInt);
    iVar.setVisibility(Sym.SYM_PRIVATE);
    Var jVar=symFact.defineVar("j".intern(),mfg.symRoot.typeInt);
    jVar.setVisibility(Sym.SYM_PRIVATE);
    Var isOkVar=symFact.defineVar("isOk".intern(),mfg.symRoot.typeInt);
    isOkVar.setVisibility(Sym.SYM_PRIVATE);
    Var isOk2Var=symFact.defineVar("isOk2".intern(),mfg.symRoot.typeInt);
    isOkVar.setVisibility(Sym.SYM_PRIVATE);
    Var tmpVar=symFact.defineVar("tmp".intern(),mfg.symRoot.typeInt);
    isOkVar.setVisibility(Sym.SYM_PRIVATE);
    Var vPosVar=symFact.defineVar("v_pos".intern(),mfg.symRoot.typeInt);
    isOkVar.setVisibility(Sym.SYM_PRIVATE);
    Var wPosVar=symFact.defineVar("w_pos".intern(),mfg.symRoot.typeInt);
    isOkVar.setVisibility(Sym.SYM_PRIVATE);
    Var maskVar=symFact.defineVar("mask".intern(),mfg.symRoot.typeInt);
    isOkVar.setVisibility(Sym.SYM_PRIVATE);

    // if(mdf_task>=0)
    Exp ifCmp=hirFact.exp(HIR.OP_CMP_GE,
                          hirFact.varNode((Var)mfg.taskSym),
                          hirFact.intConstNode(0));
    BlockStmt thenPart=hirFact.blockStmt(null);
    Stmt ifStmt=hirFact.ifStmt(ifCmp,thenPart,null);
    getTaskBlk.addLastStmt(ifStmt);


    // for(i=0;i<vect_size;i++)
    // Loop init
    VarNode varNode=hirFact.varNode(iVar);
    Stmt loopInit=hirFact.assignStmt(varNode,hirFact.intConstNode(0));
    // Loop condition
    varNode=hirFact.varNode(iVar);
    Exp cmp=hirFact.exp(HIR.OP_CMP_LT,varNode,hirFact.intConstNode(vectSize));
    // Loop body
    BlockStmt loopBody=hirFact.blockStmt(null);
    // Loop step
    varNode=hirFact.varNode(iVar);
    Exp add=hirFact.exp(HIR.OP_ADD,varNode,hirFact.intConstNode(1));
    varNode=hirFact.varNode(iVar);
    Stmt loopStep=hirFact.assignStmt(varNode,add);
    // Make a loop statement
    Stmt forStmt=hirFact.forStmt(loopInit,cmp,loopBody,loopStep);
    thenPart.addLastStmt(forStmt);


    // MDF_CUR_STAT[i]=MDF_CUR_STAT[i] | MDF_FINISH_COND[mdf_task][i]
    varNode=hirFact.varNode(decLocal.curStat);
    Exp subExp1=hirFact.subscriptedExp(varNode,
                                       hirFact.varNode(iVar));
    VarNode varNode2=hirFact.varNode(decLocal.finishCondition);
    Exp subExp2=hirFact.subscriptedExp(varNode2,
                                       hirFact.varNode((Var)mfg.taskSym));
    Exp subExp3=hirFact.subscriptedExp(subExp2,
                                       hirFact.varNode(iVar));
    Exp or=hirFact.exp(HIR.OP_OR,subExp1,subExp3);
    varNode=hirFact.varNode(decLocal.curStat);
    subExp1=hirFact.subscriptedExp(varNode,
                                   hirFact.varNode(iVar));
    Stmt assign=hirFact.assignStmt(subExp1,or);
    loopBody.addLastStmt(assign);
    

    // for(i=0;i<task_size;i++)
    // Loop init
    varNode=hirFact.varNode(iVar);
    loopInit=hirFact.assignStmt(varNode,hirFact.intConstNode(0));
    // Loop condition
    varNode=hirFact.varNode(iVar);
    cmp=hirFact.exp(HIR.OP_CMP_LT,varNode,hirFact.intConstNode(condSize));
    // Loop body
    loopBody=hirFact.blockStmt(null);
    // Loop step
    varNode=hirFact.varNode(iVar);
    add=hirFact.exp(HIR.OP_ADD,varNode,hirFact.intConstNode(1));
    varNode=hirFact.varNode(iVar);
    loopStep=hirFact.assignStmt(varNode,add);
    // Make a loop statement
    forStmt=hirFact.forStmt(loopInit,cmp,loopBody,loopStep);
    thenPart.addLastStmt(forStmt);


    // if(_MDF_ALREADY_PUT[i]==0)
    varNode=hirFact.varNode(decLocal.alreadyPut);
    subExp1=hirFact.subscriptedExp(varNode,
                                   hirFact.varNode(iVar));
    ifCmp=hirFact.exp(HIR.OP_CMP_EQ,subExp1,hirFact.intConstNode(0));
    // then part (2)
    BlockStmt thenPart2=hirFact.blockStmt(null);
    // make a if statement
    ifStmt=hirFact.ifStmt(ifCmp,thenPart2,null);
    loopBody.addLastStmt(ifStmt);


    // for then part
    // isOk=0
    varNode=hirFact.varNode(isOkVar);
    assign=hirFact.assignStmt(varNode,hirFact.intConstNode(0));
    thenPart2.addLastStmt(assign);

    // for(j=0;j<vect_size;j++)
    // Loop init
    varNode=hirFact.varNode(jVar);
    loopInit=hirFact.assignStmt(varNode,hirFact.intConstNode(0));
    // Loop condition
    varNode=hirFact.varNode(jVar);
    cmp=hirFact.exp(HIR.OP_CMP_LT,varNode,hirFact.intConstNode(vectSize));
    // Loop body
    BlockStmt loopBody2=hirFact.blockStmt(null);
    // Loop step
    varNode=hirFact.varNode(jVar);
    add=hirFact.exp(HIR.OP_ADD,varNode,hirFact.intConstNode(1));
    varNode=hirFact.varNode(jVar);
    loopStep=hirFact.assignStmt(varNode,add);
    // Make a loop statement
    forStmt=hirFact.forStmt(loopInit,cmp,loopBody2,loopStep);
    thenPart2.addLastStmt(forStmt);

    Label breakLab=((LoopStmt)forStmt).getLoopEndLabel();

    // for loopBody2
    // tmp=MDF_CUR_STAT[j] & MDF_NON_EXEC[i][j]
    varNode=hirFact.varNode(decLocal.curStat);
    subExp1=hirFact.subscriptedExp(varNode,hirFact.varNode(jVar));
    varNode2=hirFact.varNode(decLocal.nonExecCond);
    subExp2=hirFact.subscriptedExp(varNode2,hirFact.varNode(iVar));
    subExp3=hirFact.subscriptedExp(subExp2,hirFact.varNode(jVar));
    Exp and=hirFact.exp(HIR.OP_AND,subExp1,subExp3);
    assign=hirFact.assignStmt(hirFact.varNode(tmpVar),and);
    loopBody2.addLastStmt(assign);


    // if(tmp!=0)
    varNode=hirFact.varNode(tmpVar);
    ifCmp=hirFact.exp(HIR.OP_CMP_NE,varNode,hirFact.intConstNode(0));
    // then part (3)
    BlockStmt thenPart3=hirFact.blockStmt(null);
    // make a if statement
    ifStmt=hirFact.ifStmt(ifCmp,thenPart3,null);
    loopBody2.addLastStmt(ifStmt);


    // isOk=1
    varNode=hirFact.varNode(isOkVar);
    assign=hirFact.assignStmt(varNode,hirFact.intConstNode(1));
    thenPart3.addLastStmt(assign);

    // break
    JumpStmt breakJump=hirFact.jumpStmt(breakLab);
    thenPart3.addLastStmt(breakJump);


    // if(isOk==1)
    varNode=hirFact.varNode(isOkVar);
    ifCmp=hirFact.exp(HIR.OP_CMP_EQ,varNode,hirFact.intConstNode(1));
    // then part (3)
    thenPart3=hirFact.blockStmt(null);
    // make a if statement
    ifStmt=hirFact.ifStmt(ifCmp,thenPart3,null);
    thenPart2.addLastStmt(ifStmt);
    

    // v_pos=i/word_size
    varNode=hirFact.varNode(iVar);
    Exp div=hirFact.exp(HIR.OP_DIV,varNode,
                        hirFact.intConstNode(util.wordSize()));
    varNode=hirFact.varNode(vPosVar);
    assign=hirFact.assignStmt(varNode,div);
    thenPart3.addLastStmt(assign);

    // w_pos=i%word_size
    varNode=hirFact.varNode(iVar);
    Exp mod=hirFact.exp(HIR.OP_MOD,varNode,
                        hirFact.intConstNode(util.wordSize()));
    varNode=hirFact.varNode(wPosVar);
    assign=hirFact.assignStmt(varNode,mod);
    thenPart3.addLastStmt(assign);

    // mask=1<<(word_size-w_pos)
    varNode=hirFact.varNode(wPosVar);
    Exp sub=hirFact.exp(HIR.OP_SUB,hirFact.intConstNode(util.wordSize()-1),
                        varNode);
    Exp lsh=hirFact.exp(HIR.OP_SHIFT_LL,hirFact.intConstNode(1),sub);
    varNode=hirFact.varNode(maskVar);
    assign=hirFact.assignStmt(varNode,lsh);
    thenPart3.addLastStmt(assign);
    
    // MDF_CUR_STAT[v_pos]=MDF_CUR_STAT[v_pos] | mask
    varNode=hirFact.varNode(decLocal.curStat);
    subExp1=hirFact.subscriptedExp(varNode,hirFact.varNode(vPosVar));
    varNode=hirFact.varNode(maskVar);
    or=hirFact.exp(HIR.OP_OR,subExp1,varNode);
    varNode=hirFact.varNode(decLocal.curStat);
    subExp1=hirFact.subscriptedExp(varNode,hirFact.varNode(vPosVar));
    assign=hirFact.assignStmt(subExp1,or);
    thenPart3.addLastStmt(assign);

    // MDF_ALREADY_PUT[i]=1
    varNode=hirFact.varNode(decLocal.alreadyPut);
    subExp1=hirFact.subscriptedExp(varNode,hirFact.varNode(iVar));
    assign=hirFact.assignStmt(subExp1,hirFact.intConstNode(1));
    thenPart3.addLastStmt(assign);




    // for(i=0;i<task_size;i++)
    // Loop init
    varNode=hirFact.varNode(iVar);
    loopInit=hirFact.assignStmt(varNode,hirFact.intConstNode(0));
    // Loop condition
    varNode=hirFact.varNode(iVar);
    cmp=hirFact.exp(HIR.OP_CMP_LT,varNode,hirFact.intConstNode(condSize));
    // Loop body
    loopBody=hirFact.blockStmt(null);
    // Loop step
    varNode=hirFact.varNode(iVar);
    add=hirFact.exp(HIR.OP_ADD,varNode,hirFact.intConstNode(1));
    varNode=hirFact.varNode(iVar);
    loopStep=hirFact.assignStmt(varNode,add);
    // Make a loop statement
    forStmt=hirFact.forStmt(loopInit,cmp,loopBody,loopStep);
    thenPart.addLastStmt(forStmt);


    // if(_MDF_ALREADY_PUT[i]==0)
    varNode=hirFact.varNode(decLocal.alreadyPut);
    subExp1=hirFact.subscriptedExp(varNode,
                                   hirFact.varNode(iVar));
    ifCmp=hirFact.exp(HIR.OP_CMP_EQ,subExp1,hirFact.intConstNode(0));
    // then part (2)
    thenPart2=hirFact.blockStmt(null);
    // make a if statement
    ifStmt=hirFact.ifStmt(ifCmp,thenPart2,null);
    loopBody.addLastStmt(ifStmt);


    // for then part
    // isOk=0
    varNode=hirFact.varNode(isOkVar);
    assign=hirFact.assignStmt(varNode,hirFact.intConstNode(0));
    thenPart2.addLastStmt(assign);

    // for(j=0;j<vect_size;j++)
    // Loop init
    varNode=hirFact.varNode(jVar);
    loopInit=hirFact.assignStmt(varNode,hirFact.intConstNode(0));
    // Loop condition
    varNode=hirFact.varNode(jVar);
    cmp=hirFact.exp(HIR.OP_CMP_LT,varNode,hirFact.intConstNode(vectSize));
    // Loop body
    loopBody2=hirFact.blockStmt(null);
    // Loop step
    varNode=hirFact.varNode(jVar);
    add=hirFact.exp(HIR.OP_ADD,varNode,hirFact.intConstNode(1));
    varNode=hirFact.varNode(jVar);
    loopStep=hirFact.assignStmt(varNode,add);
    // Make a loop statement
    forStmt=hirFact.forStmt(loopInit,cmp,loopBody2,loopStep);
    thenPart2.addLastStmt(forStmt);

    breakLab=((LoopStmt)forStmt).getLoopEndLabel();

    // for loopBody2
    // tmp=MDF_CUR_STAT[j] & MDF_EXEC[i][j]
    varNode=hirFact.varNode(decLocal.curStat);
    subExp1=hirFact.subscriptedExp(varNode,hirFact.varNode(jVar));
    varNode2=hirFact.varNode(decLocal.execCond);
    subExp2=hirFact.subscriptedExp(varNode2,hirFact.varNode(iVar));
    subExp3=hirFact.subscriptedExp(subExp2,hirFact.varNode(jVar));
    and=hirFact.exp(HIR.OP_AND,subExp1,subExp3);
    assign=hirFact.assignStmt(hirFact.varNode(tmpVar),and);
    loopBody2.addLastStmt(assign);


    // if(tmp!=0)
    varNode=hirFact.varNode(tmpVar);
    ifCmp=hirFact.exp(HIR.OP_CMP_NE,varNode,hirFact.intConstNode(0));
    // then part (3)
    thenPart3=hirFact.blockStmt(null);
    // make a if statement
    ifStmt=hirFact.ifStmt(ifCmp,thenPart3,null);
    loopBody2.addLastStmt(ifStmt);


    // isOk=1
    varNode=hirFact.varNode(isOkVar);
    assign=hirFact.assignStmt(varNode,hirFact.intConstNode(1));
    thenPart3.addLastStmt(assign);

    // break
    breakJump=hirFact.jumpStmt(breakLab);
    thenPart3.addLastStmt(breakJump);


    // if(isOk==1)
    varNode=hirFact.varNode(isOkVar);
    ifCmp=hirFact.exp(HIR.OP_CMP_EQ,varNode,hirFact.intConstNode(1));
    // then part (3)
    thenPart3=hirFact.blockStmt(null);
    // make a if statement
    ifStmt=hirFact.ifStmt(ifCmp,thenPart3,null);
    thenPart2.addLastStmt(ifStmt);


    // for then part
    // isOk2=1
    varNode=hirFact.varNode(isOk2Var);
    assign=hirFact.assignStmt(varNode,hirFact.intConstNode(1));
    thenPart3.addLastStmt(assign);

    // for(j=0;j<vect_size;j++)
    // Loop init
    varNode=hirFact.varNode(jVar);
    loopInit=hirFact.assignStmt(varNode,hirFact.intConstNode(0));
    // Loop condition
    varNode=hirFact.varNode(jVar);
    cmp=hirFact.exp(HIR.OP_CMP_LT,varNode,hirFact.intConstNode(vectSize));
    // Loop body
    loopBody2=hirFact.blockStmt(null);
    // Loop step
    varNode=hirFact.varNode(jVar);
    add=hirFact.exp(HIR.OP_ADD,varNode,hirFact.intConstNode(1));
    varNode=hirFact.varNode(jVar);
    loopStep=hirFact.assignStmt(varNode,add);
    // Make a loop statement
    forStmt=hirFact.forStmt(loopInit,cmp,loopBody2,loopStep);
    thenPart3.addLastStmt(forStmt);

    breakLab=((LoopStmt)forStmt).getLoopEndLabel();

    // for loopBody2
    // tmp=MDF_CUR_STAT[j] & MDF_ACCESS[i][j]
    varNode=hirFact.varNode(decLocal.curStat);
    subExp1=hirFact.subscriptedExp(varNode,hirFact.varNode(jVar));
    varNode2=hirFact.varNode(decLocal.dataAccessCond);
    subExp2=hirFact.subscriptedExp(varNode2,hirFact.varNode(iVar));
    subExp3=hirFact.subscriptedExp(subExp2,hirFact.varNode(jVar));
    and=hirFact.exp(HIR.OP_AND,subExp1,subExp3);
    assign=hirFact.assignStmt(hirFact.varNode(tmpVar),and);
    loopBody2.addLastStmt(assign);


    // if(tmp!=MDF_ACCESS[i][j])
    varNode=hirFact.varNode(tmpVar);
    varNode2=hirFact.varNode(decLocal.dataAccessCond);
    subExp1=hirFact.subscriptedExp(varNode2,hirFact.varNode(iVar));
    subExp2=hirFact.subscriptedExp(subExp1,hirFact.varNode(jVar));
    ifCmp=hirFact.exp(HIR.OP_CMP_NE,varNode,subExp2);
    // then part (3)
    BlockStmt thenPart4=hirFact.blockStmt(null);
    // make a if statement
    ifStmt=hirFact.ifStmt(ifCmp,thenPart4,null);
    loopBody2.addLastStmt(ifStmt);


    // isOk2=0
    varNode=hirFact.varNode(isOk2Var);
    assign=hirFact.assignStmt(varNode,hirFact.intConstNode(0));
    thenPart4.addLastStmt(assign);

    // break
    breakJump=hirFact.jumpStmt(breakLab);
    thenPart4.addLastStmt(breakJump);


    // if(isOk2==1)
    varNode=hirFact.varNode(isOk2Var);
    ifCmp=hirFact.exp(HIR.OP_CMP_EQ,varNode,hirFact.intConstNode(1));
    // then part (3)
    thenPart4=hirFact.blockStmt(null);
    // make a if statement
    ifStmt=hirFact.ifStmt(ifCmp,thenPart4,null);
    thenPart3.addLastStmt(ifStmt);


    // MDF_READY_QUEUE[MDF_PUT_POS]=i
    varNode=hirFact.varNode(decLocal.readyQueue);
    subExp1=hirFact.subscriptedExp(varNode,
                                   hirFact.varNode(decLocal.putPos));
    assign=hirFact.assignStmt(subExp1,hirFact.varNode(iVar));
    thenPart4.addLastStmt(assign);

    // MDF_PUT_POS=MDF_PUT_POS+1
    varNode=hirFact.varNode(decLocal.putPos);
    add=hirFact.exp(HIR.OP_ADD,varNode,hirFact.intConstNode(1));
    varNode=hirFact.varNode(decLocal.putPos);
    assign=hirFact.assignStmt(varNode,add);
    thenPart4.addLastStmt(assign);

    // MDF_ALREADY_PUT[i]=1
    varNode=hirFact.varNode(decLocal.alreadyPut);
    subExp1=hirFact.subscriptedExp(varNode,hirFact.varNode(iVar));
    assign=hirFact.assignStmt(subExp1,hirFact.intConstNode(1));
    thenPart4.addLastStmt(assign);

    // if(_MDF_PUT_POS>_MDF_GET_POS)
    ifCmp=hirFact.exp(HIR.OP_CMP_GT,
                      hirFact.varNode(decLocal.putPos),
                      hirFact.varNode(decLocal.getPos));
    // then part
    thenPart=hirFact.blockStmt(null);
    // else part
    BlockStmt elsePart=hirFact.blockStmt(null);
    // make a if statement
    ifStmt=hirFact.ifStmt(ifCmp,thenPart,elsePart);
    getTaskBlk.addLastStmt(ifStmt);

    // for then part
    // mdf_task=_MDF_READY_QUEUE[_MDF_GET_POS]
    varNode=hirFact.varNode(decLocal.readyQueue);
    subExp1=hirFact.subscriptedExp(varNode,
                                   hirFact.varNode(decLocal.getPos));
    assign=hirFact.assignStmt(hirFact.varNode((Var)mfg.taskSym),subExp1);
    thenPart.addLastStmt(assign);
    // _MDF_GET_POS=_MDF_GET_POS + 1
    varNode=hirFact.varNode(decLocal.getPos);
    add=hirFact.exp(HIR.OP_ADD,varNode,hirFact.intConstNode(1));
    varNode=hirFact.varNode(decLocal.getPos);
    assign=hirFact.assignStmt(varNode,add);
    thenPart.addLastStmt(assign);
    
    // for else part
    // mdf_task=-1
    assign=hirFact.assignStmt(hirFact.varNode((Var)mfg.taskSym),
                              hirFact.intConstNode(-1));
    elsePart.addLastStmt(assign);


    localSymTab.popSymTable();
    return(getTaskBlk);
  }

  /**
   * Generate the body part of the `switch-case' structure.
   * @return The body part of the `switch-case' structure
   * @throws PassException Any exception in MDF module
   **/
  BlockStmt makeSwitchBody(HirList labelList) throws PassException{
    // The body of `switch-case' structure.
    BlockStmt switchBody=hirFact.blockStmt(null);
    // The list of the labels which are already iterated over.
    LinkedList visited=new LinkedList();

    // Change the structure of the program into `switch-case' structure.
    // Iterate all over the macro tasks in the current macro flow graph.
    for(ListIterator ite=mfg.listIterator();ite.hasNext();){
      MacroTask mt=(MacroTask)ite.next();

      BlockStmt stmt=hirFact.blockStmt(null);
      BBlock[] blks=mt.blks();
      LinkedList ignoreBlk=new LinkedList();

      // Iterate all over the basic blocks in the current macro flow graph.
      for(int i=0;i<blks.length;i++){
        // find whether mt needs extra jump statement
        if(mt.succList.size()>1 && mt.exitBlks().contains(blks[i]) && 
           blks[i].getSuccList().size()==1){
          Stmt lastStmt=null;
          for(BBlockSubtreeIterator bite=blks[i].bblockSubtreeIterator();
              bite.hasNext();){
            lastStmt=(Stmt)bite.next();
          }
          //env.output.println(lastStmt);
          if(lastStmt!=null && !(lastStmt instanceof JumpStmt)){
            //env.output.println(lastStmt.toStringWithChildren());
            Label lab=((BBlock)blks[i].getSuccList().get(0)).getLabel();
            if(extraJump==null)
              extraJump=hirFact.jumpStmt(lab);
            else{
              System.err.println("extraJump is not null");
              throw new PassException("mdf","internal error");
            }
            //env.output.println("find : "+extraJump);
          }
        }
          
        Label blkLabel=blks[i].getLabel();
        if(!visited.contains(blkLabel)){
          visited.add(blkLabel);
//          env.output.println(blkLabel);

          // Iterate all over the statements in the current basic block.
          for(BBlockSubtreeIterator subIte=blks[i].bblockSubtreeIterator();
              subIte.hasNext();){
            Stmt node=(Stmt)subIte.next();
            Stmt nodeOrg=node;

            if(ignoreBlk.contains(node.getUpperStmt())) continue;

            //env.output.println(node.toStringDetail());
            //env.output.println("  - "+node.getUpperStmt());

            // Ignore BLOCK statements.
            if(!(node instanceof BlockStmt)){
              if(node instanceof LabeledStmt){
                int kind=node.getLabel().getLabelKind();
                if(kind!=Label.SWITCH_CASE_LABEL &&
                   kind!=Label.ENTRY_LABEL &&
                   kind!=Label.SWITCH_DEFAULT_LABEL){
                  //env.output.println(node.toStringWithChildren());
                  Stmt labStmt=(Stmt)hirFact.labeledStmt(node.getLabel(),
                                                         hirFact.nullStmt());
                  stmt.addLastStmt(labStmt);
                }
                continue;
              }
              // Copy the original HIR statements.
              try{
                node=(Stmt)node.hirClone();
              }
              catch(Exception ex){
                System.err.println("Fail cloning");
                throw new PassException("mdf","fail node cloning");
              }
              //env.output.println(blks[i].getBBlockNumber()+" : "+node);

              // If the current statement is IF statement.
              if(node instanceof IfStmt){
                // In IF statement, if `then' part or `else' part is outside
                // of the current macro task, the conditional branch should be
                // registered with the dynamic scheduler.
                TreatIf data=new TreatIf(env,mfg,(IfStmt)node,visited,
                                         cond);
                data.replaceReturn(endLabel,decLocal);
                data.replace(mt,endLabel);
              }

              // If the current statement is SWITCH statement.
              else if(node instanceof SwitchStmt){
                // In SWITCH statement, if each `case' is outside of the 
                // current macro task, the conditional branch should be
                // registered with the dynamic scheduler.
                TreatSwitch data=new TreatSwitch(env,mfg,(SwitchStmt)node,
                                                 visited,cond);
                data.replaceReturn(endLabel,decLocal);
                data.replace(mt,endLabel);
              }

              // If the current statement is FOR, WHILE or UNTIL statement.
              else if(node instanceof LoopStmt){
                TreatLoop data=new TreatLoop(env,mfg,(LoopStmt)node,visited,
                                             cond);
                data.replaceReturn(endLabel,decLocal);
                data.replace(mt,endLabel);

                for(int ii=1;ii<=node.getChildCount();ii++){
                  HIR child=(HIR)node.getChild(ii);
                  LinkedList labList=data.getLabelsInLoop(child,
                                                          new LinkedList());

                  for(ListIterator labIte=labList.listIterator();
                      labIte.hasNext();){
                    LabeledStmt labeledStmt=(LabeledStmt)labIte.next();
                    visited.add(labeledStmt.getLabel());
                  }
                }

                // Ignore the initial part of the current loop.
                Stmt initStmt=((LoopStmt)nodeOrg).getLoopInitPart();
                if(initStmt!=null){
                  //env.output.println("+++++++++++++ "+initStmt);
                  ignoreBlk.add(initStmt);
                }
              }

              // If the current statement is RETURN statement.
              else if(node instanceof ReturnStmt){
                // RETURN statement means the end of macro data flow 
                // processing.
                //env.output.println(node.toStringWithChildren());
                //env.output.println(node.getChild(1));

                if(decLocal.returnStatus!=null){
                  Exp statusExp=(Exp)hirFact.varNode(decLocal.returnStatus);
                  Stmt setStatus=hirFact.assignStmt(statusExp,
                                                    (Exp)node.getChild(1));
                  //stmt.addLastStmt(setStatus);
                  node=setStatus;
                }
                else node=null;
                
/*
                VarNode finishVar=hirFact.varNode(decLocal.allFinishVar);
                Stmt finishAssign=hirFact.assignStmt(finishVar,
                                                     hirFact.intConstNode(1));
                node=finishAssign;
*/
                //env.output.println("++++ "+node);
              }
              // Collect statements which are included in one macro task.
              //env.output.println("ADDED "+node);
              if(node!=null)
                stmt.addLastStmt((Stmt)node);
            }
          }
        }
      }
      //stmt.print(0);

      if(extraJump!=null){
        stmt.addLastStmt(extraJump);
        extraJump=null;
        //env.output.println("\n\naaaaaaaaaaaaaaaaaaaaaaaa");
        //stmt.print(0);
      }

      if(env.opt.isSet("mdf-ptest")){
        // Append `mdf_mt_start_time' at the head of a macro task.
        Stmt nullStmt=hirFact.nullStmt();
        nullStmt.addInf(Registry.INF_KIND_OPEN_MP,decPragma.endif);
        stmt.addFirstStmt(nullStmt);

        VarNode taskVarNode=hirFact.varNode((Var)mfg.taskSym);
        VarNode idVarNode=hirFact.varNode((Var)mfg.idSym);
        SubpNode mtStartTimeNode=hirFact.subpNode(decPtest.mtStartTime);
        IrList mtStartTimeParam=hirFact.irList();
        mtStartTimeParam.add(idVarNode);
        mtStartTimeParam.add(taskVarNode);
        Stmt mtStartTimeCall=hirFact.callStmt(mtStartTimeNode,
                                              mtStartTimeParam);

        mtStartTimeCall.addInf(Registry.INF_KIND_OPEN_MP,decPragma.ptest);

    
        stmt.addFirstStmt(mtStartTimeCall);

        // Append `mdf_mt_end_time' at the tail of a macro task.
        idVarNode=hirFact.varNode((Var)mfg.idSym);
        SubpNode mtEndTimeNode=hirFact.subpNode(decPtest.mtEndTime);
        IrList mtEndTimeParam=hirFact.irList();
        mtEndTimeParam.add(idVarNode);
        Stmt mtEndTimeCall=hirFact.callStmt(mtEndTimeNode,mtEndTimeParam);

        mtEndTimeCall.addInf(Registry.INF_KIND_OPEN_MP,decPragma.ptest);

        stmt.addBeforeBranchStmt(mtEndTimeCall);

        nullStmt=hirFact.nullStmt();
        nullStmt.addInf(Registry.INF_KIND_OPEN_MP,decPragma.endif);
        stmt.addBeforeBranchStmt(nullStmt);
      }
      // If the current macro task is the exit task, then append the break
      // point from the loop structure.
      if(mfg.exitBlks().contains(mt)){
        VarNode finishVar=hirFact.varNode(decLocal.allFinishVar);
        Stmt finishAssign=hirFact.assignStmt(finishVar,
                                             hirFact.intConstNode(1));
        stmt.addBeforeBranchStmt(finishAssign);
      }

      Stmt lastNode=stmt.getLastStmt();
      if(lastNode instanceof JumpStmt){
        Label jumpLabel=((JumpStmt)lastNode).getLabel();
        MacroTask target=mfg.macroTask(jumpLabel);
        stmt.addBeforeBranchStmt(cond.finish.finishCond(mt,target));

        if(!endLabel.equals(jumpLabel) && !mt.equals(target))
          ((JumpStmt)lastNode).changeJumpLabel(endLabel);

      }
      else{
        stmt.addLastStmt((Stmt)hirFact.jumpStmt(endLabel));
        stmt.addBeforeBranchStmt(cond.finish.finishCond(mt,null));
      }
      
      // Make a `case' of the `switch-case' structure.
      LabeledStmt switchLabel=hirFact.labeledStmt(mt.label,stmt);
      switchBody.addLastStmt((Stmt)switchLabel);
      HirSeq seq=hirFact.hirSeq(hirFact.intConstNode(mt.taskNumber()),
                                hirFact.labelNode(mt.label));
      labelList.add(seq);
    }
    return(switchBody);
  }
}
