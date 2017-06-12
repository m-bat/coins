/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.mdf;

import java.util.ListIterator;

/**
 * The bit vector for the status of the MDF.
 * It is used by the dynamic scheduler in running time.
 **/
abstract class Conditions{
  protected final MdfEnvironment env;
  /** The current macro flow graph **/
  protected final MacroFlowGraph mfg;
  /** The conditions which set into the executable conditions when the macro
      task finishes executing **/
  protected final int[][] vector;
  /** The size of the elements of this condition **/
  protected final int elemSize;
  /** Ths size of the bit vector **/
  protected final int vectSize;
  /** The condition whether the bit is ready **/
  protected final boolean[] isReady;

  /**
   * Constructor:
   * @param e The environment of the MDF module
   * @param g The current macro flow graph
   **/
  protected Conditions(MdfEnvironment e,MacroFlowGraph g){
    env=e;
    mfg=g;

    // The size of the bit vector for the executable condition
    elemSize=mfg.bound()+mfg.controlBranch.size();

    // `offset' means the maximum number of the ID of the macro tasks.
    vectSize=mfg.bound();

    // The conditions which set into the executable conditions when the macro
    // task finishes executing
    vector=new int[vectSize][elemSize];

    isReady=new boolean[elemSize];

    for(int i=0;i<vectSize;i++)
      for(int j=0;j<elemSize;j++) vector[i][j]=0;

  }

  /**
   * Constructor:
   * @param e The environment of the MDF module
   * @param g The current macro flow graph
   * @param size The size of the bit vector
   **/
  protected Conditions(MdfEnvironment e,MacroFlowGraph g,int size){
    env=e;
    mfg=g;

    // The size of the bit vector for the executable condition
    elemSize=mfg.bound()+mfg.controlBranch.size();

    // `offset' means the maximum number of the ID of the macro tasks.
    vectSize=size;

    // The conditions which set into the executable conditions when the macro
    // task finishes executing
    vector=new int[vectSize][elemSize];

    isReady=new boolean[elemSize];

    for(int i=0;i<vectSize;i++)
      for(int j=0;j<elemSize;j++) vector[i][j]=0;

  }

  /**
   * Get the bit vector which means the condition when the specified macro
   * task is finished executing.
   * @param id The unique number of each macro tasks
   * @return The bit vector which means the condition for the dynamic scheduler
   **/
  protected int[] bitVector(int id){
    Util util=new Util(env,mfg);
    int[] target=vector[id];
    int wordSize=util.wordSize();
    int[] vect=new int[util.vectorSize()];

    for(int i=0;i<vect.length;i++){
      vect[i]=0; // initialize
      for(int j=0;j<wordSize;j++){
        if(target.length<=(j+i*wordSize)){
          break;
        }
        else if(target[j+i*wordSize]==1){
          int mask=1<<(wordSize-j-1);
          vect[i]=vect[i]|mask;
        }
      }
    }

    return(vect);
  }

  /**
   * Debug print of the bit vector.
   * @param name The name of the bit vector
   **/
  protected void showVector(String name){
    env.output.println(name);
    for(int i=0;i<vectSize;i++){
      env.output.print("Cond["+i+"] : ");
      for(int j=0;j<elemSize;j++)
        env.output.print(vector[i][j]);
      env.output.println();
    }
  }

  /**
   * Get the string image of the bit vector.
   * @return The string image of the bit vector
   **/
  public String toString(){
    String result="";

    for(ListIterator ite=mfg.listIterator();ite.hasNext();){
      MacroTask mt=(MacroTask)ite.next();
      result=result+"MT["+mt.taskNumber()+"] : "+toString(mt)+"\n";
    }
    return(result);
  }

  /**
   * Get the string image of the bit vector which is belong to the
   * specified macro task.
   * @param mt The target macro task
   * @return The string image of the bit vector
   **/
  public String toString(MacroTask mt){
    String result="";
    boolean hasPrev=false;

    // The condition when the target macro task is finished executing.
    for(int i=0;i<mfg.bound();i++){
      if(vector[mt.taskNumber()][i]==1){
        if(hasPrev){
          result=result+" & "+i;
        }
        else{
          result=result+i;
          hasPrev=true;
        }
      }
    }

    // The condition when the flow from the target macro task is decided.
    for(int i=mfg.bound();i<elemSize;i++){
      if(vector[mt.taskNumber()][i]==1){
        int number=i-mfg.bound();
        MacroTask[] pair=mfg.controlBranch.branchPair(number);
        if(pair!=null){
          if(hasPrev){
            result=result+" & ("+pair[0].taskNumber()+" -> "+
              pair[1].taskNumber()+")";
          }
          else{
            result=result+"("+pair[0].taskNumber()+" -> "+
              pair[1].taskNumber()+")";
            hasPrev=true;
          }
        }
      }
    }

    return(result);
  }
}
