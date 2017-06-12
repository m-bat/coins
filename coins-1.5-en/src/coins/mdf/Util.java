/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.mdf;

import coins.MachineParam;
import coins.aflow.BBlock;
import coins.sym.Type;

import java.util.LinkedList;
import java.util.Iterator;

/**
 * The utilities for the MDF module.
 **/
class Util{
  /** The environment of the MDF module **/
  private MdfEnvironment env;
  /** The current macro flow graph **/
  private MacroFlowGraph mfg;

  /**
   * Constructor:
   * @param e The environment of the MDF module
   * @param g The current macro flow graph
   **/
  Util(MdfEnvironment e, MacroFlowGraph g){
    env=e;
    mfg=g;
  }

  /**
   * Get the list of the macro tasks which is on the path from the specified
   * macro task to the entry macro task of the current macro flow graph.
   * @param mt The current macro task
   * @param list The list of the macro tasks
   * @return The list of the macro tasks
   **/
  LinkedList findPreBlks(MacroTask mt,LinkedList list){
    if(mt==null || list.contains(mt)) return(list);
    list.add(mt);

    for(Iterator ite=mt.predList.iterator();ite.hasNext();){
      list=findPreBlks((MacroTask)ite.next(),list);
    }
    return(list);
  }

  /**
   * Get the list of the macro tasks which is on the path from the specified
   * macro task to the exit macro task of the current macro flow graph.
   * @param mt The current macro task
   * @param list The list of the macro tasks
   * @return The list of the macro tasks
   **/
  LinkedList findPostBlks(MacroTask mt,LinkedList list){
    if(mt==null || list.contains(mt)) return(list);
    list.add(mt);

    for(Iterator ite=mt.succList.iterator();ite.hasNext();){
      list=findPostBlks((MacroTask)ite.next(),list);
    }
    return(list);
  }

  /**
   * Get the list of the basic blocks which is on the path from the specified
   * basic block to the entry basic block of the specified macro task.
   * @param mt The current macro task
   * @param blk The current basic block
   * @param list The list of the basic blocks
   * @return The list of the basic blocks
   **/
  LinkedList findMtPreBlks(MacroTask mt,BBlock blk,LinkedList list){
    if(list.contains(blk) || mfg.macroTask(blk)!=mt) return(list);
    list.add(blk);

    for(Iterator ite=blk.getPredList().iterator();ite.hasNext();){
      list=findMtPreBlks(mt,(BBlock)ite.next(),list);
    }

    return(list);
  }

  /**
   * Get the size of bit vector. The size of 1 word are depended upon the
   * target machine.
   * @return The size of bit vector
   **/
  int vectorSize(){
    int arraySize=numberOfConditions();
    int vectSize=arraySize/wordSize();
    if((arraySize%wordSize())!=0) // round up
      vectSize=vectSize+1;

    return(vectSize);
  }

  /**
   * Get the one word size. It is depend on the target machine architecture.
   * @return The word size
   **/
  int wordSize(){
//    return(MachineParam.SIZEOF_INT*8);
    return(env.ioRoot.machineParam.evaluateSize(Type.KIND_INT)*8);
  }

  /**
   * Get the number of the conditions which construct the executable
   * conditions.
   * @return The number of the conditions which construct the executable
   *         conditions
   **/
  int numberOfConditions(){
    int number=mfg.bound();
    number=number+mfg.controlBranch.size();

    return(number);
  }
}
