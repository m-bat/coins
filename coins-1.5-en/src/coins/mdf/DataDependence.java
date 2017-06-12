/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.mdf;

import coins.PassException;
import coins.sym.FlowAnalSym;
import coins.aflow.BBlock;
import coins.aflow.FlowAnalSymVector;
import coins.ir.hir.HIR;
import coins.ir.hir.LabeledStmt;
import coins.ir.hir.BlockStmt;
import coins.ir.hir.LoopStmt;
import coins.ir.hir.IfStmt;
import coins.ir.hir.SwitchStmt;
import coins.ir.hir.AssignStmt;
import coins.sym.Label;

import java.util.Stack;
import java.util.Iterator;
import java.util.Hashtable;
import java.util.List;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Find data dependences between macro tasks.
 **/
public class DataDependence{
  /** The threshold of debug output **/
  public static final int THR=MdfEnvironment.OptThr;
  /** The threshold of debug output **/
  public static final int THR2=MdfEnvironment.AllThr;
  /** The environment of MDF module **/
  private MdfEnvironment env;
  /** The current macro flow graph **/
  private MacroFlowGraph mfg;
  /** The map for data dependences **/
  private Hashtable dependMap;
  /** The map between the macro task and the variables which are defined in 
      that macro task **/
  private Hashtable defMap;
  /** The map between the macro task and the variables which are not defined
      but used in that macro task **/
  private Hashtable useMap;
  /** Utilities **/
  private Util util;

  /**
   * Constructor:
   * @param e The environment of the MDF module
   * @param g The current macro flow graph
   **/
  public DataDependence(MdfEnvironment e,MacroFlowGraph g) 
    throws PassException{
    env=e;
    mfg=g;

    util=new Util(env,mfg);
    dependMap=new Hashtable();

    //mfg.subpDef.print(0);

    // Find the definition and use of the variables in each macro task.
    findDefUse();

    for(ListIterator ite=mfg.listIterator();ite.hasNext();){
      // `mt' is the current macro task.
      MacroTask mt=(MacroTask)ite.next();
      // The list of macro tasks which are on the path from the entry
      // macro task to the current macro task.
      LinkedList list=util.findPreBlks(mt,new LinkedList());

      // The list of macro tasks which are data depended by `mt'.
      LinkedList dependList=new LinkedList();

      // The bit vector which means the variables that are not defined
      // but used in the current macro task.
      FlowAnalSymVector useVector=(FlowAnalSymVector)useMap.get(mt);

      // The bit vector which means the variables that are defined in the
      // current macro task.
      FlowAnalSymVector defVector=(FlowAnalSymVector)defMap.get(mt);

      // Iterate all over the macro tasks which are on the path from the
      // entry macro task to the current macro task.
      for(Iterator listIte=list.iterator();listIte.hasNext();){
        // The control can flow to the current macro task through `preTask'.
        MacroTask preTask=(MacroTask)listIte.next();
        // If `mt' is equal to `preTask' or `preTask' is already data depended
        // by `mt' then do nothing.
        if(preTask!=mt && !dependList.contains(preTask)){
          // The bit vector which means the variables that are defined in
          // `preTask'.
          FlowAnalSymVector preDefVect=(FlowAnalSymVector)defMap.get(preTask);
          // The bit vector which means the variables that are not defined 
          // but used in `preTask'.
          FlowAnalSymVector preUseVect=(FlowAnalSymVector)useMap.get(preTask);

          // The bit vector which is stored the data dependences.
          FlowAnalSymVector ansVector=mfg.subpFlow.flowAnalSymVector();

          // Check the data flow dependence
          preDefVect.vectorAnd(useVector,ansVector);
          // `mt' depend on `preTask'
          if(!ansVector.isZero()){
            dependList.add(preTask);
          }
          else{
            ansVector.vectorReset();
//            FlowAnalSymVector tmpVector=mfg.subpFlow.flowAnalSymVector();
//            FlowAnalSymVector outVect=mfg.subpFlow.flowAnalSymVector();
            // The bit vector which means the variables that may be LIVE OUT
            // from `preTask'.
/**            
            for(Iterator dite=mt.exitBlks().iterator();dite.hasNext();){
              BBlock exitBlk=(BBlock)dite.next();
              FlowAnalSymVector exitVect=exitBlk.getPLiveOut();
              exitVect.vectorOr(outVect,outVect);
            }
**/
            // Check the reverse data dependences.
//            outVect.vectorAnd(defVector,tmpVector);
//            preUseVect.vectorAnd(tmpVector,ansVector);
            preUseVect.vectorAnd(defVector,ansVector);
            // If `mt' depend on `preTask'
            if(!ansVector.isZero()){
              dependList.add(preTask);
            }
            else{
              ansVector.vectorReset();
//              tmpVector.vectorReset();

              // Check the data define order dependences.
//              outVect.vectorAnd(preDefVect,tmpVector);
//              tmpVector.vectorAnd(defVector,ansVector);
              defVector.vectorAnd(preDefVect,ansVector);
              // If `mt' depend on `preTask'
              if(!ansVector.isZero()){
                dependList.add(preTask);
              }
            }
          }
        }
      }
      dependMap.put(mt,dependList);
    }  

  }

  /**
   * Return the list of macro tasks which are data depended by `mt'.
   * @param mt The current macro task
   * @return The list of macro tasks which are data depended by `mt'
   **/
  public LinkedList depend(MacroTask mt){
    LinkedList list=(LinkedList)dependMap.get(mt);
    if(list==null) return(new LinkedList());
    return(list);
  }

  /**
   * Return the list of variables which uses thread private.
   * Thread private variables mean that the variables defined and used
   * in the same macro task, not LIVE OUT from the macro task.
   * @return The list of variables which uses thread private
   * @throws PassException Any exception in it
   **/
  LinkedList threadPrivateVariable() throws PassException{
    LinkedList result=new LinkedList();
    FlowAnalSymVector resVect=mfg.subpFlow.flowAnalSymVector();
    findDefUse();
    for(ListIterator ite=mfg.listIterator();ite.hasNext();){
      // `mt' is the current macro task
      MacroTask mt=(MacroTask)ite.next();
      // The bit vector which means the variables that are not defined but
      // used in the current macro task.
      FlowAnalSymVector useVector=(FlowAnalSymVector)useMap.get(mt);
      // Correct the variables that are not defined but used in each macro 
      // tasks.
      resVect.vectorOr(useVector,resVect);
    }

    resVect.vectorNot(resVect);
    for(Iterator ite=resVect.flowAnalSyms().iterator();ite.hasNext();){
      FlowAnalSym s=(FlowAnalSym)ite.next();
      if(!s.isGlobal() && mfg.symTab.isInThisSymTable(s)){
        //env.output.println(s.getName());
        result.add(s);
      }
    }

    return(result);
  }

  /**
   * Make two lists of variables.
   *  (1) The variables which are not defined but used in each macro tasks.
   *  (2) The variables which are defined in each macro tasks.
   * @throws PassException Any exception in it
   **/
  private void findDefUse() throws PassException{
    // Make a dominator tree.
    mfg.subpFlow.makeDominatorTree();

    // The map between macro tasks and the variables which are defined in the 
    // macro task.
    defMap=new Hashtable();
    // The map between macro tasks and the variables which are used in the
    // macro task.
    useMap=new Hashtable();
    // Iterate all over the macro tasks in the current macro flow graph.
    for(ListIterator ite=mfg.listIterator();ite.hasNext();){
      MacroTask mt=(MacroTask)ite.next();

      // The bit vector which means the variables that are defined in the 
      // current macro task.
      FlowAnalSymVector defVector=(FlowAnalSymVector)defMap.get(mt);
      if(defVector==null){
        defVector=mfg.subpFlow.flowAnalSymVector();
        defMap.put(mt,defVector);
      }

      // The bit vector which means the variables that are not defined but
      // used in the current macro task.
      FlowAnalSymVector useVector=(FlowAnalSymVector)useMap.get(mt);
      if(useVector==null){
        useVector=mfg.subpFlow.flowAnalSymVector();
        useMap.put(mt,useVector);
      }

      // The basic blocks in the current macro task.
      BBlock[] blks=mt.blks();
      for(int i=0;i<blks.length;i++){
//env.output.println("%%%%%%%%%%%% "+blks[i].getBBlockNumber());
        // The variables which are defined in the current basic block.
        FlowAnalSymVector blkDefVector=blks[i].getPDefined();
//        FlowAnalSymVector blkDefVector=blks[i].getDDefined();

        defVector.vectorOr(blkDefVector,defVector);
        //env.output.print(blks[i].getLabel().getName()+" : ");

        // The variables which are not defined but used in the current basic 
        // block.
        FlowAnalSymVector blkUseVector=blks[i].getPExposed();

//        FlowAnalSymVector blkUseVector=blks[i].getDExposed();

        // The list of the basic blocks which dominates `blks[i]'.
        List dom=blks[i].getDomForSubpFlow();
        //env.output.println(blkUseVector);

        // The list of basic blocks which are followed from `blks[i]' using
        // their predecessors.
        LinkedList preBlks=util.findMtPreBlks(mt,blks[i],new LinkedList());


        FlowAnalSymVector tmpVect=mfg.subpFlow.flowAnalSymVector();
        blkUseVector.vectorCopy(tmpVect);
        for(ListIterator preIte=preBlks.listIterator();preIte.hasNext();){
          BBlock preBlk=(BBlock)preIte.next();

//env.output.println("$$$$$$$$$$$$ "+preBlk.getBBlockNumber());

          if(preBlk!=blks[i] && dom.contains(preBlk)){
            //env.output.print(preBlk.getLabel().getName()+" ");
            // The variables which are not defined but used in the current
            // basic block except the variables which are defined in `preBlk'.
            FlowAnalSymVector preDefVector=preBlk.getPDefined();
//            FlowAnalSymVector preDefVector=preBlk.getDDefined();
            tmpVect.vectorSub(preDefVector,tmpVect);
          }
        }
        //env.output.println(blkUseVector);
        useVector.vectorOr(tmpVect,useVector);
      }

      // debug print start
      env.print("MDF : def["+mt.taskNumber()+"] : "+defVector+
                "\n      ",THR2);
      for(Iterator defIte=defVector.flowAnalSyms().iterator();
          defIte.hasNext();){
        FlowAnalSym s=(FlowAnalSym)defIte.next();
        env.print(" "+s.getName(),THR2);
      }
      env.println("",THR2);

      env.print("MDF : use["+mt.taskNumber()+"] : "+useVector+
                "\n      ",THR2);
      for(Iterator useIte=useVector.flowAnalSyms().iterator();
          useIte.hasNext();){
        FlowAnalSym s=(FlowAnalSym)useIte.next();
        env.print(" "+s.getName(),THR2);
      }
      env.println("",THR2);
      // debug print end


    }
  }

  /**
   * Debug print
   **/
  void print(){
    for(ListIterator ite=mfg.listIterator();ite.hasNext();){
      MacroTask mt=(MacroTask)ite.next();
      LinkedList list=(LinkedList)dependMap.get(mt);
      String str=mt.toString()+" DD{ ";
      for(ListIterator listIte=list.listIterator();listIte.hasNext();){
        MacroTask task=(MacroTask)listIte.next();
        str=str+task.taskNumber()+" ";
      }
      str=str+"} "+mt.hasCall();
      env.output.println(str);
    }
  }
}
