/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.mdf;

import coins.PassException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * The data access condition is a condition about data dependency.
 * It is used by the dynamic scheduler.
 **/
public class DataAccessCondition extends Conditions{
  /**
   * Constructor
   * @param e The environmet of the MDF module
   * @param g The current macro flow graph
   * @throws PassException Any exception in it
   **/
  public DataAccessCondition(MdfEnvironment e,MacroFlowGraph g)
    throws PassException{
    super(e,g);

    DataDependence ddep=new DataDependence(env,mfg);
//    ddep.print();
    List exitTasks=mfg.exitBlks();

    for(Iterator ite=mfg.listIterator();ite.hasNext();){
      boolean ready=true;
      MacroTask mt=(MacroTask)ite.next();

      if(exitTasks.contains(mt)){
        Util util=new Util(env,mfg);
        List preBlks=util.findPreBlks(mt,new LinkedList());

        for(Iterator pite=preBlks.iterator();pite.hasNext();){
          MacroTask preBlk=(MacroTask)pite.next();
          if(preBlk!=mt)
            vector[mt.taskNumber()][preBlk.taskNumber()]=1;
        }

        if(ddep.depend(mt).size()>0 || 
           (mt.succList.size()==0 && mt.predList.size()!=0))
          ready=false;
      }
      else{
        LinkedList ddepList=ddep.depend(mt);
        for(Iterator dite=ddepList.iterator();dite.hasNext();){
          MacroTask ddepMt=(MacroTask)dite.next();
          vector[mt.taskNumber()][ddepMt.taskNumber()]=1;
          ready=false;
        }
      }
      if(ready){
        isReady[mt.taskNumber()]=true;
      }
    }
  }
}
