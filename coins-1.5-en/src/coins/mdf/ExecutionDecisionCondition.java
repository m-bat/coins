/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.mdf;

import java.util.Iterator;

/**
 * The excecution decision condition is a condition used by the dynamic
 * scheduler. The condition means when a macro task can execute.
 **/
public class ExecutionDecisionCondition extends Conditions{
  /**
   * Constructor
   * @param e The environment of the MDF module
   * @param g The current macro flow graph
   **/
  public ExecutionDecisionCondition(MdfEnvironment e,MacroFlowGraph g){
    super(e,g);

    int offset=mfg.bound();

    ControlDependence cdep=new ControlDependence(env,mfg);
//    cdep.print();
    PostDominators pdom=new PostDominators(env,mfg);
//    pdom.print();

    for(Iterator ite=mfg.listIterator();ite.hasNext();){
      boolean ready=true;
      MacroTask mt=(MacroTask)ite.next();

      for(Iterator cite=cdep.depend(mt).iterator();cite.hasNext();){
        MacroTask cdepMt=(MacroTask)cite.next();

        for(Iterator site=cdepMt.succList.iterator();site.hasNext();){
          MacroTask succ=(MacroTask)site.next();

          if(pdom.pdom[succ.taskNumber()][mt.taskNumber()]==1){
            int num=mfg.controlBranch.branchUniqueNum(cdepMt,succ);
            vector[mt.taskNumber()][num+offset]=1;
            ready=false;
          }
        }
      }

      if(ready){
        isReady[mt.taskNumber()]=true;
        for(int i=0;i<vector[mt.taskNumber()].length;i++){
          vector[mt.taskNumber()][i]=1;
        }
      }
    }
  }
}
