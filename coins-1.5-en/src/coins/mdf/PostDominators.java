/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.mdf;

import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.Stack;

/**
 * Analyze postdominators in the current macro flow graph.
 **/
class PostDominators{
  /** The current macro flow graph **/
  private MacroFlowGraph mfg;
  /** The environment of the MDF module **/
  private MdfEnvironment env;
  /** The postdominator **/
  public final int[][] pdom;

  /**
   * Constructor
   * @param e The environment of the MDF module
   * @param graph The current macro flow graph
   **/
  PostDominators(MdfEnvironment e,MacroFlowGraph graph){
    env=e;
    mfg=graph;
    pdom=new int[mfg.bound()][mfg.bound()];

    // initialize
    for(int i=0;i<mfg.bound();i++)
      for(int j=0;j<mfg.bound();j++)
        pdom[i][j]=0;

    List exitBlks=mfg.exitBlks();
    for(Iterator ite=mfg.listIterator();ite.hasNext();){
      MacroTask mt=(MacroTask)ite.next();
      if(exitBlks.contains(mt)) 
        pdom[mt.taskNumber()][mt.taskNumber()]=1;
      else
        for(int i=0;i<mfg.bound();i++) pdom[mt.taskNumber()][i]=1;
    }

    boolean changed=true;
    while(changed){
      changed=false;
      for(Iterator ite=exitBlks.iterator();ite.hasNext();){
        LinkedList visited=new LinkedList();
        MacroTask mt=(MacroTask)ite.next();
        Stack stack=new Stack();
        stack.push(mt);
        visited.add(mt);

        while(!stack.empty()){
          mt=(MacroTask)stack.pop();
          //env.output.println("MT "+mt.taskNumber());

          for(Iterator site=mt.succList.iterator();site.hasNext();){
            MacroTask succ=(MacroTask)site.next();
            for(int i=0;i<mfg.bound();i++){
              if(i!=mt.taskNumber() && pdom[mt.taskNumber()][i]==1 &&
                 pdom[mt.taskNumber()][i]!=pdom[succ.taskNumber()][i]){
                pdom[mt.taskNumber()][i]=0;
                changed=true;
              }
            }
          }
          
          for(Iterator pite=mt.predList.iterator();pite.hasNext();){
            MacroTask pred=(MacroTask)pite.next();
            if(!visited.contains(pred)){
              stack.push(pred);
              visited.add(pred);
            }
          }
        }
      }
    }
    //print();
  }

  /**
   * Debug print
   **/
  void print(){
    // debug print
    for(int i=0;i<mfg.bound();i++){
      env.output.print("MT["+i+"] ");
      for(int j=0;j<mfg.bound();j++)
        env.output.print(pdom[i][j]);
      env.output.println();
    }
  }
}
