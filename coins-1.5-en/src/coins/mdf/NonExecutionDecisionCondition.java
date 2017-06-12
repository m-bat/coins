/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.mdf;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * The non-execution decision condition is a condition used by the dynamic 
 * scheduler. The condition means whether the macro task should execute or not.
 **/
public class NonExecutionDecisionCondition extends Conditions{
  /**
   * Constructor
   * @param e The environment of the MDF module
   * @param g The current macro flow graph
   **/
  public NonExecutionDecisionCondition(MdfEnvironment e,MacroFlowGraph g){
    super(e,g);

    int offset=mfg.bound();
    Util util=new Util(env,mfg);

    for(Iterator ite=mfg.listIterator();ite.hasNext();){
      boolean ready=true;
      MacroTask mt=(MacroTask)ite.next();

      LinkedList preBlks=util.findPreBlks(mt,new LinkedList());
      for(Iterator bite=preBlks.iterator();bite.hasNext();){
        MacroTask preBlk=(MacroTask)bite.next();
        if(preBlk!=mt){
          for(Iterator site=preBlk.succList.iterator();site.hasNext();){
            MacroTask succ=(MacroTask)site.next();
            
            if(!preBlks.contains(succ)){
              int num=mfg.controlBranch.branchUniqueNum(preBlk,succ);
              vector[mt.taskNumber()][num+offset]=1;
              ready=false;
            }
          }
        }
      }

      if(ready){
        isReady[mt.taskNumber()]=true;
//        for(int i=0;i<vector[mt.taskNumber()].length;i++){
//          vector[mt.taskNumber()][i]=1;
//        }
      }
    }
  }
}
