/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.mdf;

import coins.PassException;
import java.util.LinkedList;
import java.util.Iterator;

/**
 * The conditions for the dynamic scheduler
 **/
public class MdfConditions{
  /** The environment of the MDF module **/
  private MdfEnvironment env;
  /** The current macr flow graph **/
  private MacroFlowGraph mfg;
  /** The current execution decision conditions **/
  public final ExecutionDecisionCondition exec;
  /** The current non-execution decision conditions **/
  public final NonExecutionDecisionCondition nonExec;
  /** The current data access conditions **/
  public final DataAccessCondition access;
  /** The current finish conditions **/
  public final FinishCondition finish;

  /**
   * Constructor
   * @param e The environment of the MDF module
   * @param g The current macro flow graph
   * @throws PassException Any exceptions in it
   **/
  public MdfConditions(MdfEnvironment e,MacroFlowGraph g) throws PassException{
    env=e;
    mfg=g;

    exec=new ExecutionDecisionCondition(env,mfg);
    nonExec=new NonExecutionDecisionCondition(env,mfg);
    access=new DataAccessCondition(env,mfg);
    finish=new FinishCondition(env,mfg);

//    mfg.subpDef.print(0);
//    mfg.print();
//    exec.showVector("EXEC");
//    nonExec.showVector("NON EXEC");
//    access.showVector("ACCESS");
//    finish.showVector("FINISH");
  }

  /**
   * Get the bit vector which means the ready tasks.
   * @return The bit vector which means the ready tasks
   **/
  int[] readyTasks(){
    LinkedList ready=new LinkedList();

    for(Iterator ite=mfg.listIterator();ite.hasNext();){
      MacroTask mt=(MacroTask)ite.next();

//      env.output.println(exec.isReady[mt.taskNumber()]+" "+
//                         nonExec.isReady[mt.taskNumber()]+" "+
//                         access.isReady[mt.taskNumber()]);
      if(exec.isReady[mt.taskNumber()] && 
         nonExec.isReady[mt.taskNumber()] && 
         access.isReady[mt.taskNumber()]){
        ready.add(mt);
        env.println("MDF : Ready Task : "+mt.taskNumber(),env.OptThr);
      }      
    }

    // We can do static scheduling here
    int[] result=new int[ready.size()];
    int count=0;
    for(Iterator ite=ready.iterator();ite.hasNext();){
      MacroTask mt=(MacroTask)ite.next();
      result[count]=mt.taskNumber();
      count++;
    }

    return(result);
  }
}
