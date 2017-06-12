/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.mdf;

import coins.PassException;
import coins.ir.hir.LabelDef;
import coins.ir.IrList;
import coins.ir.hir.SubpDefinition;
import coins.HirRoot;
import coins.SymRoot;
import coins.aflow.Flow;
import coins.FlowRoot;
import coins.aflow.BBlock;
import coins.aflow.FlowResults;
import coins.aflow.SubpFlow;
import coins.aflow.ShowFlow;
import coins.aflow.RegisterFlowAnalClasses;
import coins.aflow.BBlock;
import coins.ir.hir.HIR;
import coins.aflow.BBlockSubtreeIterator;
import coins.ir.hir.Stmt;
import coins.ir.hir.LabeledStmt;
import coins.ir.hir.LoopStmt;
import coins.ir.hir.BlockStmt;
import coins.ir.hir.HirIterator;
import coins.sym.Label;
import coins.sym.SymTable;
import coins.sym.Sym;
import coins.lparallel.LoopParallelImpl;
import coins.lparallel.RegisterClasses;
import coins.lparallel.LoopTable;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.LinkedList;
import java.util.Hashtable;
import java.util.List;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * This class represents a macro flow graph.
 * Divide a input program into macro tasks and make their predecessors
 * and successors.
 **/
public class MacroFlowGraph{
  /**
   * This class individuate each conditional branches.
   **/
  class ControlBranch{
    /** The unque number for each conditional branches **/
    private int number;
    /** The map between source macro task and target macro task **/
    private Hashtable srcMap;
    /** The map between the unique number and macro tasks **/
    private Hashtable branchTable;

    private MacroFlowGraph mfg;

    /**
     * Constructor:
     * Collect the macro tasks which have the conditional branches and 
     * inidividuate them.
     **/
    ControlBranch(MacroFlowGraph mfg){
      number=0;
      srcMap=new Hashtable();
      branchTable=new Hashtable();

      // The macro tasks which have plural successors have the conditional
      // branches
      for(ListIterator ite=mfg.listIterator();ite.hasNext();){
        MacroTask mt=(MacroTask)ite.next();
        if(mt.succList.size()>1){
          // The `mt' has the conditional branchs

          //env.output.println(mt);
          //number+=mt.succList.size();

          // Get `dstMap'
          Hashtable dstMap=(Hashtable)srcMap.get(mt);
          if(dstMap==null){
            dstMap=new Hashtable();
            srcMap.put(mt,dstMap);
          }
          // Register the successors of `mt' with `dstMap'
          for(ListIterator succIte=mt.succList.listIterator();
              succIte.hasNext();){
            MacroTask succ=(MacroTask)succIte.next();
            Integer num=(Integer)dstMap.get(succ);
            if(num==null){
              // Make a relation between the unique number and macro tasks
              MacroTask[] br=new MacroTask[2];
              br[0]=mt; // source
              br[1]=succ; // target
              //branchTable.put(String.valueOf(number).intern(),br);
              branchTable.put(new Integer(number),br);
              // Put the unique number into the conditional branch
              dstMap.put(succ,new Integer(number++));
            }
          }
        }
      }
    }
    
    /**
     * Get the number of macro tasks which have the conditional branches.
     * @return the number of macro tasks which have the conditional branches
     **/
    int size(){
      return(number);
    }

    /**
     * Get a pair of macro task, that consist of source and target of the
     * conditional branch.
     * The first element of the array means the source, and the second element
     * of the array means the target.
     * @param num The unique number for the conditional branches
     * @return The pair of the macro tasks
     **/
    MacroTask[] branchPair(int num){
      return((MacroTask[])branchTable.get(new Integer(num)));
    }

    /**
     * Get the unique number of the conditinal branches.
     * @param src The source macro task of the conditional branch
     * @param dst The target macro task of the conditional branch
     * @return The unique number of the conditional branch
     **/
    int branchUniqueNum(MacroTask src,MacroTask dst){
      Hashtable dstMap=(Hashtable)srcMap.get(src);
      if(dstMap==null) return(-1);

      Integer num=(Integer)dstMap.get(dst);
      if(num==null) return(-1);

      return(num.intValue());
    }

    /**
     * Get the decision which way to branch.
     * @param src The macro task which `dst' is control depended
     * @param dst The macro task which control depend on `src'
     * @return The list of decisions which are needed to reach from `src' to
     *         `dst' in flow.
     **/
    int[] whichCond(MacroTask src,MacroTask dst){
      Util util=new Util(env,mfg);
      LinkedList list=util.findPreBlks(dst,new LinkedList());
      LinkedList result=new LinkedList();

      for(ListIterator ite=src.succList.listIterator();ite.hasNext();){
        MacroTask succ=(MacroTask)ite.next();
        if(list.contains(succ)) result.add(succ);
      }

      int[] res=new int[result.size()];
      int i=0;
      for(ListIterator ite=result.listIterator();ite.hasNext();){
        MacroTask mt=(MacroTask)ite.next();
        res[i++]=branchUniqueNum(src,mt);
      }

      return(res);
    }
  }

  /** The current sub program **/
  public final SubpDefinition subpDef;
  /** The current SymRoot **/
  final SymRoot symRoot;
  /** The current HirRoot **/
  public final HirRoot hirRoot;
  /** The current SubpFlow **/
  final SubpFlow subpFlow;
  /** The current symbol table **/
  final SymTable symTab;
  /** This symbol represents the ID on threads **/
  final Sym idSym;
  /** This symbol represents the macro task number **/
  final Sym taskSym;
  /** The environment of the MDF module **/
  private MdfEnvironment env;
  /** The unique number for macro tasks **/
  private int taskNumber;
  /** The list of macro tasks **/
  private LinkedList macroTasks;
  /** The map between basic blocks and macro tasks **/
  private Hashtable taskMapBBlock;
  /** The map between macro tasks and labels **/
  private Hashtable taskMapLabel;
  /** The conditional branches in the current function **/
  final ControlBranch controlBranch;

  /**
   * Constructor:
   * @param e The environment of the MDF module
   * @param hRoot The current HirRoot
   * @param subpDefinition The current sub program
   **/
  public MacroFlowGraph(MdfEnvironment e,HirRoot hRoot,
                        SubpDefinition subpDefinition) throws PassException{
    env=e;
    taskNumber=0;
    subpDef=subpDefinition;
    hirRoot=hRoot;
    symRoot=hirRoot.symRoot;
//    symTab=subpDef.getSubpSym().getSymTable();
    symTab=subpDef.getSymTable();

    macroTasks=new LinkedList();
    taskMapBBlock=new Hashtable();
    taskMapLabel=new Hashtable();

//    subpDef.print(0);

    // The variable represents the ID for threads.
    idSym=symTab.searchOrAdd("_mdf_threadID".intern(),Sym.KIND_VAR,null,true,
                             false);
    idSym.setSymType(symRoot.typeInt);

    // The variable represents the task number.
    taskSym=symTab.searchOrAdd("_mdf_task".intern(),Sym.KIND_VAR,null,true,
                                false);
    taskSym.setSymType(symRoot.typeInt);

    //subpDef.print(0);

    // Make a control flow graph (CFG)
    FlowResults.putRegClasses(new RegisterFlowAnalClasses());

    FlowRoot flowRoot=hirRoot.getFlowRoot();
    if(flowRoot==null){
      flowRoot=new FlowRoot(hirRoot);
      hirRoot.attachFlowRoot(flowRoot);
    }

    Flow flow=flowRoot.aflow;

    FlowResults results=new FlowResults(flowRoot);
    subpFlow=flow.subpFlow(subpDef,results);
    flowRoot.aflow.setSubpFlow(subpFlow);
    

    subpFlow.controlFlowAnal();
    subpFlow.makeDominatorTree();

//    flow.doHir();

    //divideLoops();
    divide(results);
    makeGraph();
    cleanUp();

    controlBranch=new ControlBranch(this);

//    subpDef.print(0);
  }

  /**
   * Hook point for dividing the do-all loop.
   * Divide a large do-all loop into some loops by using the information
   * generated in the loop parallelization module.
   **/
  private void divideLoops(){
/*
    FlowResults.putRegClasses(new RegisterClasses());
    FlowRoot flowRoot=new FlowRoot(hirRoot);
    LoopParallelImpl loopPara=new LoopParallelImpl(hirRoot,env.ioRoot,
                                                   subpDef,flowRoot);
    loopPara.LoopAnal();

    LinkedList loopInfo=(LinkedList)loopPara.fresults.get("LoopParallelList",
                                                          subpFlow);

    for(Iterator ite=loopInfo.iterator();ite.hasNext();){
      LoopTable loopTable=(LoopTable)ite.next();
      if(loopTable.getParaFlag()){
        env.output.println("Parallel Loop");
      }
    }
*/
  }

  /**
   * Hook point for optimization after generating the macro flow graph.
   **/
  private void cleanUp(){
/**
    // Concatenate redundant macro tasks.
    LinkedList removed=new LinkedList();
    boolean changed=true;
    while(changed){
      changed=false;
      for(Iterator iterator=macroTasks.iterator();iterator.hasNext();){
        MacroTask mt=(MacroTask)iterator.next();
        if(!removed.contains(mt)){
          if(mt.succList.size()==1){
            MacroTask succ=(MacroTask)mt.succList.get(0);
            if(succ.predList.size()==1){
              changed=true;
              mt.concat(succ);
              removed.add(succ);
              BBlock[] succBlks=succ.blks();
              for(int i=0;i<succBlks.length;i++){
                taskMapBBlock.remove(succBlks[i]);
                taskMapBBlock.put(mt,succBlks[i]);
              }
            }
          }
        }
      }
    }

    for(Iterator iterator=removed.iterator();iterator.hasNext();){
      MacroTask mt=(MacroTask)iterator.next();
      macroTasks.remove(mt);
    }
**/
  }

  /**
   * Geneate the macro flow graph.
   * @throws PassException Any exceptions in it
   **/
  private void makeGraph() throws PassException{
    // Make successors and predecessors.
    for(Iterator iterator=macroTasks.iterator();iterator.hasNext();){
      MacroTask mt=(MacroTask)iterator.next();

      if(!mt.hasReturn()){
        for(Iterator eite=mt.exitBlks().iterator();eite.hasNext();){
          BBlock exitBlk=(BBlock)eite.next();
          for(Iterator ite=exitBlk.getSuccList().iterator();ite.hasNext();){
            BBlock blk=(BBlock)ite.next();
            MacroTask succ=(MacroTask)taskMapBBlock.get(blk);
            // If `succ' is equal to the current macro task, it is not a
            // successor.
            if(mt!=succ && !mt.succList.contains(succ)){
              mt.succList.add(succ);
              if(!succ.predList.contains(mt))
                succ.predList.add(mt);
            }
          }
        }
      }
    }
  }

  /**
   * Divide the current control flow graph into some macro tasks.
   * @param results The result of the control flow analysis
   **/
  private void divide(FlowResults results){
    FindScc findScc=new FindScc(env,subpFlow);
//    findScc.print();

    LinkedList visited=new LinkedList();
    for(Iterator ite=findScc.scc.iterator();ite.hasNext();){
      LinkedList elem=(LinkedList)ite.next();
      if(elem.size()==0) continue;

      MacroTask mt=null;
      for(Iterator eite=elem.iterator();eite.hasNext();){
        BBlock blk=(BBlock)eite.next();
        if(visited.contains(blk)) continue;
        visited.add(blk);

        if(mt==null)
          mt=new MacroTask(env,taskNumber++,symTab.generateLabel());

        taskMapBBlock.put(blk,mt);
        taskMapLabel.put(blk.getLabel(),mt);

        mt.addBasicBlk(blk);

        Stmt stmt=null;
        for(BBlockSubtreeIterator subIte=blk.bblockSubtreeIterator();
            subIte.hasNext();){
          stmt=(Stmt)subIte.next();
          if(stmt instanceof LabeledStmt){
            IrList list=((LabeledStmt)stmt).getLabelDefList();
            //env.output.println(list);
            for(Iterator lite=list.iterator();lite.hasNext();){
              Label ll=((LabelDef)lite.next()).getLabel();
              //env.output.println("  "+ll);
              MacroTask m=(MacroTask)taskMapLabel.get(ll);
              if(m!=null) taskMapLabel.remove(ll);
              taskMapLabel.put(ll,mt);
            }
          }
          else if(!(stmt instanceof BlockStmt)){
            //env.output.println(stmt);
            break;
          }
        }
        if(stmt instanceof LoopStmt){
          TreatLoop data=new TreatLoop(env,this,(LoopStmt)stmt,
                                       new LinkedList());
          for(int i=1;i<=stmt.getChildCount();i++){
            HIR child=(HIR)stmt.getChild(i);
            LinkedList labList=data.getLabelsInLoop(child,new LinkedList());
            
            for(Iterator labIte=labList.iterator();labIte.hasNext();){
              LabeledStmt labeledStmt=(LabeledStmt)labIte.next();
              BBlock blkInLoop=null;
              blkInLoop=results.getBBlockForLabel(labeledStmt.getLabel());
              
              if(blkInLoop!=null){
                visited.add(blkInLoop);
                taskMapBBlock.put(blkInLoop,mt);
                taskMapLabel.put(blkInLoop.getLabel(),mt);
                mt.addBasicBlk(blkInLoop);
              }
            }
          }
        }
      }
      if(mt!=null) macroTasks.add(mt);
    }
  }

  /**
   * Return the boundary of the list of the macro tasks.
   * @return The boundary of the list of the macro tasks
   **/
  int bound(){
    return(taskNumber);
  }

  /**
   * Return the number of the macro tasks.
   * @return The number of the macro tasks
   **/
  int numberOfMacroTasks(){
    return(macroTasks.size());
  }

  /**
   * Search the macro task which includes the specified basic block.
   * @param blk The basic block
   * @return The macro task which includes `blk'
   **/
  MacroTask macroTask(BBlock blk){
    return((MacroTask)taskMapBBlock.get(blk));
  }

  /**
   * Search the macro task which includes the specified label.
   * @param label The label
   * @return The macro task which includes `label'
   **/
  MacroTask macroTask(Label label){
    return((MacroTask)taskMapLabel.get(label));
  }

  /**
   * Get the list iterator of the macro flow graph.
   * @return The list iterator
   **/
  public ListIterator listIterator(){
    return(macroTasks.listIterator());
  }

  /**
   * Get the entry macro task of the current macro flow graph.
   * The entry means the macro task which has no predecessors.
   * If there are no entry macro task, then return `null'.
   * @return The entry macro task of the current macro flow graph
   **/
  MacroTask entryBlk(){
    for(Iterator ite=macroTasks.iterator();ite.hasNext();){
      MacroTask mt=(MacroTask)ite.next();
      if(mt.predList.size()==0) return(mt);
    }
    return(null);
  }

  /**
   * Get the list of the exit macro tasks of the current macro flow graph.
   * The exit means the macro task which has no successors.
   * @return The list of exit macro tasks
   **/
  List exitBlks(){
    LinkedList exitBlks=new LinkedList();
    for(ListIterator ite=macroTasks.listIterator(macroTasks.size());
        ite.hasPrevious();){
      MacroTask mt=(MacroTask)ite.previous();
      if(mt.succList.size()==0 && !exitBlks.contains(mt))
        exitBlks.add(mt);
    }
    return(exitBlks);
  }

  /**
   * Debug print
   **/
  void print(){
    env.output.println("== MacroFlowGraph for "+
                       subpDef.getSubpSym().getName()+" ==");
    for(Iterator ite=macroTasks.iterator();ite.hasNext();){
      MacroTask mt=(MacroTask)ite.next();
      env.output.println(mt);
    }
  }

  /**
   * Print the node as
   * <a href="http://www.research.att.com/sw/tools/graphviz/">graphviz</a>
   * format.
   * @param filename The file name for output
   **/
  void printGraph(String filename) throws PassException{
    OutputStreamWriter output=new OutputStreamWriter(System.out);
    try{
      FileOutputStream outStream=new FileOutputStream(filename);
      output=new OutputStreamWriter(outStream);
    }
    catch(FileNotFoundException e){}
    try{
      output.write("digraph G {\n");
    }
    catch(IOException e){
      env.output.println("digraph G {");
    }

    // print control flow
    DataDependence ddep=new DataDependence(env,this);
    for(Iterator ite=macroTasks.iterator();ite.hasNext();){
      MacroTask mt=(MacroTask)ite.next();
      mt.printGraph(output);

      // print data dependence
      try{
        output.write(mt.taskNumber()+" -> {");
      }
      catch(IOException e){
        env.output.print(mt.taskNumber()+" -> {");
      }

      for(Iterator dite=ddep.depend(mt).iterator();dite.hasNext();){
        MacroTask dMt=(MacroTask)dite.next();
        try{
          output.write(dMt.taskNumber()+" ");
        }
        catch(IOException e){
          env.output.print(dMt.taskNumber()+" ");
        }
      }
      try{
        output.write("} [color=red,dir=back]\n");
      }
      catch(IOException e){
        env.output.println("} [color=red]");
      }
    }


    try{
      output.write("}\n");
      output.close();
    }
    catch(IOException e){
      env.output.println("}");
    }
  }
}
