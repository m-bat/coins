/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.mdf;

import coins.sym.Var;
import coins.sym.Sym;
import coins.sym.Type;
import coins.sym.SymTable;

/**
 * Declaration of the local variables.
 **/
class DeclareLocalVariables{
  /** The threshold of debug output **/
  public static final int THR=MdfEnvironment.OptThr;
  /** The variable `_MDF_PUT_POS' **/
  final Var putPos;
  /** The variable `_MDF_GET_POS' **/
  final Var getPos;
  /** The variable `_MDF_ALL_FINISH' **/
  final Var allFinishVar;
  /** The variable `_MDF_EXEC' **/
  final Var execCond;
  /** The variable `_MDF_NON_EXEC' **/
  final Var nonExecCond;
  /** The variable `_MDF_ACCESS' **/
  final Var dataAccessCond;
  /** The variable `_MDF_CURRENT_STATUS' **/
  final Var curStat;
  /** The variable `_MDF_FINISH_COND' **/
  final Var finishCondition;
  /** The variable `_MDF_READY_QUEUE' **/
  final Var readyQueue;
  /** The variable `_MDF_ALREADY_PUT' **/
  final Var alreadyPut;
  /** The variable `_mdf_return_status' **/
  final Var returnStatus;
  /** The variable `_MDF_OMP_LOCK' **/
  final Var ompLock;
//  final Var taskMap;

  /**
   * Constructor
   * @param env The environment of the MDF module
   * @param mfg The current macro flow graph
   **/
  DeclareLocalVariables(MdfEnvironment env,MacroFlowGraph mfg){
    Sym symFact=mfg.symRoot.sym;
    SymTable symTab=mfg.symTab;

    // The position which the macro task is put into the ready task queue.
    if(symTab.search("_MDF_PUT_POS".intern())==null){
      putPos=symFact.defineVar("_MDF_PUT_POS".intern(),mfg.symRoot.typeInt);
      putPos.setVisibility(Sym.SYM_PRIVATE);
    }
    else
      putPos=(Var)symTab.search("_MDF_PUT_POS".intern());

    // The position which the macro task is pop from the ready task queue.
    if(symTab.search("_MDF_GET_POS".intern())==null){
      getPos=symFact.defineVar("_MDF_GET_POS".intern(),mfg.symRoot.typeInt);
      getPos.setVisibility(Sym.SYM_PRIVATE);
    }
    else
      getPos=(Var)symTab.search("_MDF_GET_POS".intern());

    // The variable which checks whether all macro tasks are marked finish.
    if(symTab.search("_MDF_ALL_FINISH".intern())==null){
      allFinishVar=symFact.defineVar("_MDF_ALL_FINISH".intern(),
                                     mfg.symRoot.typeInt.makeVolatileType());
      allFinishVar.setVisibility(Sym.SYM_PRIVATE);
    }
    else
      allFinishVar=(Var)symTab.search("_MDF_ALL_FINISH".intern());

    // The number of atomic conditions.
    Util util=new Util(env,mfg);
    int numberOfAtomicCond=util.numberOfConditions();

    // The size of the vector which include the earliest executable conditions
    int vectorSize=util.vectorSize();

    // The number of macro tasks.
    //int numberOfTasks=mfg.numberOfMacroTasks();
    int numberOfTasks=mfg.bound();

    // debug output
    env.println("MDF : Number of MacroTasks : "+numberOfTasks,THR);
    env.println("MDF : Number of Conditions : "+numberOfAtomicCond,THR);
    env.println("MDF : Size of Vector       : "+vectorSize,THR);

    // The one dimension array which means bit vector.
    // The size of the vector is as follows:
    // e=the number of the atomic conditions in the current earliest 
    //   executable condition
    // size=e/32  (the remainder is rounded up)
    Type vt1=symFact.vectorType(mfg.symRoot.typeU_Int,vectorSize);

    // The two dimension array. The number of this array are the number of the
    // macro tasks times the size of bit vector: vt1.
    Type vt2=symFact.vectorType(vt1,numberOfTasks);

    // The one dimension array. The number of this array are the number of the
    // macro tasks.
    Type vt3=symFact.vectorType(mfg.symRoot.typeU_Int,numberOfTasks);

    // The one dimension array. The number of this array are the number of the
    // atomic conditions in the current earliest executable condition.
    Type vt4=symFact.vectorType(vt1,numberOfAtomicCond);

    // The one dimension array. The number of this array are the number of the
    // edges in the current macro flow graph.
    Type vt5=symFact.vectorType(mfg.symRoot.typeU_Int,vectorSize);

    // The variable which means the map between macro tasks and the unique 
    // number of the conditions.
//    taskMap=symFact.defineVar("_MDF_TASK_MAP".intern(),vt5);
//    taskMap.setVisibility(Sym.SYM_PRIVATE);

    // 
    if(symTab.search("_MDF_CURRENT_STATUS".intern())==null){
      curStat=symFact.defineVar("_MDF_CURRENT_STATUS".intern(),vt5);
      curStat.setVisibility(Sym.SYM_PRIVATE);
    }
    else
      curStat=(Var)symTab.search("_MDF_CURRENT_STATUS".intern());


    // 
    if(symTab.search("_MDF_EXEC".intern())==null){
      execCond=symFact.defineVar("_MDF_EXEC".intern(),vt2);
      execCond.setVisibility(Sym.SYM_PRIVATE);
    }
    else
      execCond=(Var)symTab.search("_MDF_EXEC".intern());

    // 
    if(symTab.search("_MDF_NON_EXEC".intern())==null){
      nonExecCond=symFact.defineVar("_MDF_NON_EXEC".intern(),vt2);
      nonExecCond.setVisibility(Sym.SYM_PRIVATE);
    }
    else
      nonExecCond=(Var)symTab.search("_MDF_NON_EXEC".intern());

    // 
    if(symTab.search("_MDF_ACCESS".intern())==null){
      dataAccessCond=symFact.defineVar("_MDF_ACCESS".intern(),vt2);
      dataAccessCond.setVisibility(Sym.SYM_PRIVATE);
    }
    else
      dataAccessCond=(Var)symTab.search("_MDF_ACCESS".intern());

    // The variable which means the elements that renew the earliest 
    // executable conditions when each of macro tasks is finished.
    if(symTab.search("_MDF_FINISH_COND".intern())==null){
      finishCondition=symFact.defineVar("_MDF_FINISH_COND".intern(),vt4);
      finishCondition.setVisibility(Sym.SYM_PRIVATE);
    }
    else
      finishCondition=(Var)symTab.search("_MDF_FINISH_COND".intern());

    // The variables which means the ready macro task queue.
    if(symTab.search("_MDF_READY_QUEUE".intern())==null){
      readyQueue=symFact.defineVar("_MDF_READY_QUEUE".intern(),vt3);
      readyQueue.setVisibility(Sym.SYM_PRIVATE);
    }
    else
      readyQueue=(Var)symTab.search("_MDF_READY_QUEUE".intern());

    // The flag which checks whether the macro task is already put into
    // the ready task queue.
    if(symTab.search("_MDF_ALREADY_PUT".intern())==null){
      alreadyPut=symFact.defineVar("_MDF_ALREADY_PUT".intern(),vt3);
      alreadyPut.setVisibility(Sym.SYM_PRIVATE);
    }
    else
      alreadyPut=(Var)symTab.search("_MDF_ALREADY_PUT".intern());

    // Return status of the current sub program.
    if(symTab.search("_mdf_return_status".intern())==null){
      Type subpType=mfg.subpDef.getSubpSym().getReturnValueType();
      if(subpType.getTypeKind()!=Type.KIND_VOID){
        returnStatus=symFact.defineVar("_mdf_return_status".intern(),
                                       subpType);
env.output.println(returnStatus);
        returnStatus.setVisibility(Sym.SYM_PRIVATE);
      }
      else{
        returnStatus=null;
      }
    }
    else
      returnStatus=(Var)symTab.search("_mdf_return_status".intern());

    // OpenMP lock variable
    String lockVarName="_MDF_OMP_LOCK_"+mfg.subpDef.getSubpSym().getName();
    ompLock=symFact.defineVar(lockVarName.intern(),
                              symFact.pointerType(mfg.symRoot.typeVoid));
    ompLock.setVisibility(Sym.SYM_PRIVATE);
  }
}
