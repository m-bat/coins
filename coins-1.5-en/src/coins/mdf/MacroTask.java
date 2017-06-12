/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.mdf;

import coins.aflow.BBlock;
import coins.sym.Label;
import coins.PassException;
import coins.aflow.BBlockSubtreeIterator;
import coins.ir.hir.Stmt;
import coins.ir.hir.ExpStmt;
import coins.ir.hir.Exp;
import coins.ir.hir.FunctionExp;
import coins.ir.hir.AssignStmt;
import coins.sym.Sym;
import coins.ir.hir.HIR;
import coins.ir.hir.ReturnStmt;
import coins.ir.hir.SubpNode;

import java.util.List;
import java.util.Iterator;
import java.util.LinkedList;
import java.io.OutputStreamWriter;
import java.io.IOException;

/**
 * This class represents a macro task.
 * Each macro tasks include some basic blocks as a list.
 * And also, each macro tasks have their entry and exit basic blocks.
 **/
public class MacroTask{
  /** The threshold for debugging output **/
  public static final int THR=MdfEnvironment.OptThr;
  /** The list of macro tasks which are the predecessor of the current macro
      task **/
  public final LinkedList predList;
  /** The list of macro tasks which are the successor of the current macro 
      task **/
  public final LinkedList succList;
  /** The label of the current macro task **/
  public final Label label;
  /** The environment of the MDF module **/
  private MdfEnvironment env;
  /** The unique number of the current macro task **/
  private int number;
  /** The list of the basic blocks which is inside of the current macro 
      task **/
  private LinkedList blkList;
  /** The reserved word for the system call `exit()'**/
  private final String exitName="exit";

  /**
   * Constructor:
   * @param e The environment of the MDF module
   * @param num The unique number of the current macro task
   * @param blk The entry basic block of the current macro task
   * @param lab The label of the current macro task
   **/
  public MacroTask(MdfEnvironment e,int num,BBlock blk,Label lab){
    env=e;
    label=lab;
    number=num;
    blkList=new LinkedList();
    blkList.add(blk);

    predList=new LinkedList();
    succList=new LinkedList();
  }

  /**
   * Constructor:
   * @param e The environment of the MDF module
   * @param num The unique number of the current macro task
   * @param lab The label of the current macro task
   **/
  public MacroTask(MdfEnvironment e,int num,Label lab){
    env=e;
    label=lab;
    number=num;

    blkList=new LinkedList();
    predList=new LinkedList();
    succList=new LinkedList();
  }

  /**
   * Get the unique number of the current macro task.
   * @return The unique number of the current macro task
   **/
  public int taskNumber(){
    return(number);
  }

  /**
   * Get the entry basic block of the current macro task.
   * @return The entry basic block of the current macro task.
   * @throws PassException Any exceptions in it
   **/
  public BBlock entryBlk() throws PassException{
    BBlock entryBlk=null;

    for(Iterator ite=blkList.iterator();ite.hasNext();){
      BBlock blk=(BBlock)ite.next();
      if(blk.getPredList().size()==0)
        entryBlk=blk;

      for(Iterator pite=blk.getPredList().iterator();pite.hasNext();){
        BBlock pred=(BBlock)pite.next();
        if(!blkList.contains(pred)){
          if(entryBlk!=null && !entryBlk.equals(blk)){
            System.err.println("MacroTask ERROR : several entry block in MT "+
                               number);
            throw new PassException("mdf",
                                    "several entry block in a macrotask");
          }
          entryBlk=blk;
        }
      }
    }
    return(entryBlk);
  }

  /**
   * Get the list of basic blocks in the current macro task, the basic blocks
   * means the exit block from the current macro task.
   * @return The list of the basic block which means the exit blocks
   **/
  public LinkedList exitBlks(){
    LinkedList exitBlks=new LinkedList();

    for(Iterator ite=blkList.iterator();ite.hasNext();){
      BBlock blk=(BBlock)ite.next();

      if(blk.getSuccList().size()==0){
        exitBlks.add(blk);
      }
      else{
        for(Iterator site=blk.getSuccList().iterator();site.hasNext();){
          BBlock succ=(BBlock)site.next();
          if(!blkList.contains(succ)){
            if(!exitBlks.contains(blk)){
              exitBlks.add(blk);
            }
          }
        }
      }
    }
    return(exitBlks);
  }

  /** 
   * Check whether the current macro task has some return statements.
   * If so, return true.
   * @return True if the current macro task has some return statements.
   *         Otherwise return false
   **/
  boolean hasReturn(){
    for(Iterator bite=blkList.iterator();bite.hasNext();){
      BBlock blk=(BBlock)bite.next();
      for(BBlockSubtreeIterator ite=blk.bblockSubtreeIterator();
          ite.hasNext();){
        Stmt node=(Stmt)ite.next();
        if(node instanceof ReturnStmt){
          // return statement
          return(true);
        }
      }
    }
    return(false);
  }

  /**
   * check whether the current macro task has some function calls.
   * If so, return true.
   * @return True if the current macro task has some function calls.
   *         Otherwise return false
   **/
  boolean hasCall(){
    for(Iterator bite=blkList.iterator();bite.hasNext();){
      BBlock blk=(BBlock)bite.next();
      for(BBlockSubtreeIterator ite=blk.bblockSubtreeIterator();
          ite.hasNext();){
        Stmt node=(Stmt)ite.next();
        if(node instanceof ExpStmt){
          HIR mayCall=((ExpStmt)node).getExp();
          if(mayCall!=null && mayCall.getOperator()==HIR.OP_CALL){
            //env.output.println(node.toStringWithChildren());
            Exp exp=((ExpStmt)node).getExp();
            if(exp instanceof FunctionExp){
              //env.output.println(exp.toStringWithChildren());
              SubpNode callee=((FunctionExp)exp).getFunctionNode();
              if(callee!=null){
                return(true);
              }
            }
          }
        }
      }
    }
    return(false);
  }

  /**
   * Get the basic blocks which the current macro task includes.
   * The basic blocks are sorted by reverse post order.
   * @return The basic blocks which the current macro task includes
   * @throws PassException Any exceptions in it
   **/
  public BBlock[] blks() throws PassException{
    LinkedList sortedList=postOrdering(entryBlk(),new LinkedList(),
                                       new LinkedList());

    Object[] o=sortedList.toArray();
    BBlock[] blks=new BBlock[o.length];
    for(int i=0;i<o.length;i++){
      blks[i]=(BBlock)o[o.length-1-i];
//      env.output.print(" "+blks[i].getBBlockNumber());
    }
//    env.output.println();

    return(blks);
  }

  /**
   * Sort the basic blocks in the current macro task by post ordering.
   * @param blk The target basic block
   * @param list The list of the basic blocks
   * @param visited The list of the basic blocks which are already visited
   * @return The sorted list of the basic blocks
   **/
  private LinkedList postOrdering(BBlock blk,LinkedList list,
                                  LinkedList visited){
    if(visited.contains(blk)) return(list);
    visited.add(blk);
    //env.output.println("MT["+number+"] : "+blk.getBBlockNumber());
//    env.output.println("MT["+number+"] : "+blk.getLabel());
    if(list.contains(blk) || !blkList.contains(blk)) return(list);

    for(Iterator ite=blk.getSuccList().iterator();ite.hasNext();){
      BBlock succ=(BBlock)ite.next();
      postOrdering(succ,list,visited);
    }
    list.add(blk);

    return(list);
  }

  /**
   * Add the specified basic block into the current macro task.
   * @param blk The basic block which should be added
   * @return The basic block which has been added into the current macro task
   **/
  BBlock addBasicBlk(BBlock blk){
    if(blk==null) return(null);
    if((blk.getPredList().size()==0 && !blk.isEntryBBlock())){
//      env.output.println("hoge "+blk.getLabel());
      return(null);
    }

    blkList.add(blk);

    return(blk);
  }

  /**
   * Concatenate macro tasks.
   * @param mt The macro task which should be concatenated with the current 
   *           macro task
   * @return The macro task which has been concatenated.
   * @throws PassException Any exceptions in it
   **/
  MacroTask concat(MacroTask mt) throws PassException{
    // The current macro task is as a predecessor of `mt'.
    if(mt.predList.size()==1 && succList.size()==1 &&
       mt.predList.contains(this)){
      //exitBlk=mt.exitBlk();

      BBlock blks[]=mt.blks();
      for(int i=0;i<blks.length;i++){
        if(!blkList.contains(blks[i])) blkList.add(blks[i]);
      }

      succList.clear();
      for(Iterator ite=mt.succList.iterator();ite.hasNext();){
        MacroTask succ=(MacroTask)ite.next();
        succ.predList.remove(mt);
        if(!succ.equals(this)){
          succList.add(succ);
          if(!succ.predList.contains(this))succ.predList.add(this);
        }
      }

      return(this);
    }
    // The current macro task is as a successor of `mt'.
    else if(predList.size()==1 && mt.succList.size()==1 &&
            predList.contains(mt)){
      //entryBlk=mt.entryBlk();

      BBlock blks[]=mt.blks();
      for(int i=0;i<blks.length;i++){
        if(!blkList.contains(blks[i])) blkList.add(blks[i]);
      }

      predList.clear();
      for(Iterator ite=mt.predList.iterator();ite.hasNext();){
        MacroTask pred=(MacroTask)ite.next();
        pred.succList.remove(mt);
        if(!pred.equals(this)){
          predList.add(pred);
          if(!pred.succList.contains(this))pred.succList.add(this);
        }
      }

      return(this);
    }

    // Return `null' when the current macro task has not been concatenated.
    return(null);
  }

  /**
   * Get the string image of the current macro task.
   * @return The string image of the current macro task
   **/
  public String toString(){
    String str="MT["+number+"] : block{ ";

    for(Iterator ite=blkList.iterator();ite.hasNext();){
      BBlock blk=(BBlock)ite.next();
      str=str+blk.getBBlockNumber()+" ";
    }

    str=str+"} pred{ ";
    for(Iterator ite=predList.iterator();ite.hasNext();){
      MacroTask mt=(MacroTask)ite.next();
      str=str+mt.taskNumber()+" ";
    }

    str=str+"} succ{ ";
    for(Iterator ite=succList.iterator();ite.hasNext();){
      MacroTask mt=(MacroTask)ite.next();
      str=str+mt.taskNumber()+" ";
    }

    str=str+"}";
    return(str);
  }

  /**
   * Print the node as
   * <a href="http://www.research.att.com/sw/tools/graphviz/">graphviz</a>
   * format.
   * @param output The output stream
   **/
  void printGraph(OutputStreamWriter output){
    try{
      output.write(number+" [fontsize=10,shape=box,label=\"");
    }
    catch(IOException e){
      env.output.print(number+" [fontsize=10,label=\"");
    }

    // print labels
    try{
      output.write("MT : "+number+"\\l\"]\n");
    }
    catch(IOException e){
      env.output.print("MT : "+number+" \\l\"]\n");
    }

    // print successors
    try{
      output.write(number+" -> {");
    }
    catch(IOException e){
      env.output.print(number+" -> {");
    }
    for(Iterator ite=succList.iterator();ite.hasNext();){
      MacroTask mt=(MacroTask)ite.next();
      try{
        output.write(mt.taskNumber()+" ");
      }
      catch(IOException e){
        env.output.println(mt.taskNumber()+" ");
      }
    }
    try{
      output.write("}\n");
    }
    catch(IOException e){
      env.output.println("}");
    }
  }
}
