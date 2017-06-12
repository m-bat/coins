/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.mdf;

import coins.aflow.BBlock;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Iterator;

/**
 * Find the control dependences between macro tasks.
 **/
public class ControlDependence{
  /** The current macro flow graph **/
  private MacroFlowGraph mfg;
  /** The map for control dependences **/
  private Hashtable dependMap;
  /** The environment of the MDF module **/
  private MdfEnvironment env;
  /** Utilities **/
  private Util util;
  /** The post-dominator information **/
  private PostDominators pdom;

  /**
   * Constructor:
   * @param e The environment of the MDF module
   * @param g The current macro flow graph
   **/
  public ControlDependence(MdfEnvironment e,MacroFlowGraph g){
    env=e;
    mfg=g;
    util=new Util(env,mfg);

    dependMap=new Hashtable();
    pdom=new PostDominators(env,mfg);

    // Analyze into each macro task
    for(ListIterator ite=mfg.listIterator();ite.hasNext();)
      analyze2((MacroTask)ite.next());
  }

  /**
   * Return the list of macro tasks which are control depended by `mt'.
   * @param mt The current macro task
   * @return The list of macro tasks which are control depended by `mt'
   **/
  public LinkedList depend(MacroTask mt){
    LinkedList list=(LinkedList)dependMap.get(mt);
    if(list==null) return(new LinkedList());
    return(list);
  }

  /**
   * Debug print
   **/
  void print(){
    for(ListIterator ite=mfg.listIterator();ite.hasNext();){
      MacroTask mt=(MacroTask)ite.next();
      LinkedList list=depend(mt);
      String str=mt.toString()+" CD{ ";
      for(ListIterator listIte=list.listIterator();listIte.hasNext();){
        MacroTask task=(MacroTask)listIte.next();
        str=str+task.taskNumber()+" ";
      }
      str=str+"}";
      env.output.println(str);
    }
  }

  /**
   * Find control dependences.
   * @param mt The current macro task
   **/
  private void analyze2(MacroTask mt){
    // The list of macro tasks which are control depended by `mt'.
    LinkedList depends=new LinkedList();
    int number=mt.taskNumber();

    for(Iterator ite=mfg.listIterator();ite.hasNext();){
      MacroTask task=(MacroTask)ite.next();
      if(pdom.pdom[task.taskNumber()][number]==0){
        for(Iterator site=task.succList.iterator();site.hasNext();){
          MacroTask succ=(MacroTask)site.next();
          if(pdom.pdom[succ.taskNumber()][number]==1 && 
             !depends.contains(task)){
            depends.add(task);
          }
        }
      }
    }
    dependMap.put(mt,depends);
  }

  /**
   * Find control dependences.
   * @param mt The current macro task
   **/
/**
  private void analyze(MacroTask mt){
    // The list of macro tasks which are control depended by `mt'.
    LinkedList depends=new LinkedList();

    // Get all macro tasks which are on the path from the entry macro task
    // to the current macro task.
    LinkedList preBlk=util.findPreBlks(mt,new LinkedList());

    //env.output.println("-------------- "+mt.taskNumber()+"\t");
    for(Iterator ite=preBlk.iterator();ite.hasNext();){
      MacroTask task=(MacroTask)ite.next();
      //env.output.print(" "+task.taskNumber());

      if(task!=mt && task.succList.size()>1){
        for(Iterator succIte=task.succList.iterator();succIte.hasNext();){
          MacroTask succ=(MacroTask)succIte.next();
          if(!preBlk.contains(succ) && !depends.contains(task)){
            depends.add(task);
          }
        }
      }
    }
    //env.output.println();
    dependMap.put(mt,depends);
  }
**/
}
