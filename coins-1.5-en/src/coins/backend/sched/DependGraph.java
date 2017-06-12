/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */

package coins.backend.sched;

import java.util.Iterator;

import coins.backend.Function;
import coins.backend.Op;
import coins.backend.Root;
import coins.backend.lir.LirNode;
import coins.backend.util.BiList;
import coins.backend.util.ImList;

/**
 * A DependGraph is made from a basic block. 
 * It has two lists of DependNodes, schedulabale and unSchedulable.
 */
public class DependGraph {
  static final int LAST_TIME = DependNode.LAST_TIME;
  Function func;
  Root root;
  BiList schedulable; // list of independent DepndNodes
  BiList unSchedulable; // list of DependNodes which depend on other DependNodes.
  DependNode lastBranch; // null if the last instruction is not a branch.
	
  public DependGraph(Function f){
    func = f;
    root = f.root;
    schedulable = new BiList();
    unSchedulable = new BiList();
    lastBranch = null;
  }
  public void newSegment(){
  	schedulable = new BiList();
    unSchedulable = new BiList();
    lastBranch = null;
  }
	
  /**
   * Add a DependNode dn to the schedulable list if it is independent,
   * otherwise add to the unSchedulable list.
   * @param dn
   */
  public void add(DependNode dn){
    if (!foundDepend(dn)){
      if (dn.instr.opCode == Op.EPILOGUE || dn.instr.isBranch()
    		  || dn.instr.opCode == Op.PARALLEL)
        schedulable.add(dn.setScheduleTime(LAST_TIME));
      else	
        schedulable.add(dn.setScheduleTime(0));
    }
    else{
      unSchedulable.add(dn);
    }
  }
	
  /**
   * If the schedulable list or the unShedulable list contains
   * a DependNode on which the DependNode dn depends, return true.
   * Otherwose, return false. 
   * Dependent information is set during the search.
   * @param dn
   */
  boolean foundDepend(DependNode dn){
    boolean result = false;
    for (Iterator it = schedulable.iterator(); it.hasNext(); ){
      DependNode node = (DependNode)it.next();
      if (dn.dependOn(node))
        result = true;
    }
    for (Iterator it = unSchedulable.iterator(); it.hasNext(); ){
      DependNode node = (DependNode)it.next();
      if (dn.dependOn(node))
        result = true;
    }
    return result;
  }
 
  /**
   * Set dn to lastBranch.
   * This block has dn as the last branch instruction.
   * @param dn
   */
  public void hasBranch(DependNode dn){
    lastBranch = dn;
  }

  /**
   * Schedule all instructions in this DependGraph.
   * If a Call instruction is followed by a nop,
   * the nop is replaced by the previous instruction.
   * If a instruction has no dependant and the last branch
   * instruction is followed by a nop,
   * the nop is replaced by the instruction.
   * @return scheduled asm list
   */
  BiList scheduleInst(){
    BiList bl = new BiList();
    DependNode prevNode = null;
    DependNode prevPrevNode = null;
    while (!schedulable.isEmpty()){
      DependNode dn = schedule();
      schedulable.remove(dn);
      if (dn.isCall() && dn.hasDelaySlot() && !bl.isEmpty()
          && prevNode.instr.opCode != Op.PROLOGUE) {
     	LirNode ln = dn.getCalleeReg();
      	if (ln == null || !prevNode.output.contains(ln))
          bl.last().setElem(addDelayMark((LirNode)bl.last().elem()));
      	else{
      	  if (prevPrevNode != null && !prevPrevNode.beDepended.contains(prevNode)
      	  		&& prevNode.machineCodeSize == 1
				&& prevPrevNode.instr.opCode != Op.PROLOGUE)
      	    bl.last().prev().setElem(addDelayMark((LirNode)bl.last().prev().elem()));
      	}
      }
      else if (!dn.instr.isBranch() && dn.beDepended.isEmpty()
               && lastBranch != null // && !lastFilled
               && lastBranch.hasDelaySlot() && dn.instr.opCode != Op.PROLOGUE
               && dn.instr.opCode != Op.PARALLEL
               && dn.machineCodeSize == 1) {
        dn.instr = addDelayMark(dn.instr);
      }
      bl.add(dn.instr);
      prevPrevNode = prevNode;
      prevNode = dn;
      findSchedulable(dn);
    }
    return bl;
  }

  /** Add &delay mark to LIR instruction. **/
  private LirNode addDelayMark(LirNode ins) {
    return func.newLir.replaceOptions(ins, new ImList("&delay", ins.opt));
  }

  /**
   * Schedule all LirNodes in this DependGraph.
   * @return scheduled LirNode list
   */
  BiList scheduleLir(){
    BiList bl = new BiList();
    while (!schedulable.isEmpty()){
      DependNode dn = schedule();
      schedulable.remove(dn);
      bl.add(dn.instr);
      findSchedulable(dn);
    }
    return bl;
  }
  /**
   * Select next DependNode from schedulable list.
   * @return scheduled DependNode
   */
  DependNode schedule(){
    int minScheduleTime = LAST_TIME;
    for (Iterator it = schedulable.iterator(); it.hasNext(); ){
      DependNode dn1 = (DependNode)it.next();
      if (dn1.scheduleTime < minScheduleTime)
        minScheduleTime = dn1.scheduleTime;
    }
    int maxPathLength = -1;
    int maxLatency = -1;
    DependNode dnP = null;
    DependNode dnL = null;
    for (Iterator it = schedulable.iterator(); it.hasNext(); ){
      DependNode dn1 = (DependNode)it.next();
      if (dn1.scheduleTimeDcr() == minScheduleTime){
        if (dn1.latency > maxLatency){
          maxLatency = dn1.latency;
          dnL = dn1;
        }
        int pathLength = dn1.pathLength();
        if (pathLength > maxPathLength){
          maxPathLength = pathLength;
          dnP = dn1;
        }
      }
    }

    if (maxLatency >= LirNodeInf.LOAD_LATENCY)
      dnP = dnL;
    if (root.traceOK("TMD", 1)){
      root.debOut.println("----scheduled DependNode----");
      root.debOut.println(dnP);
    }
    return dnP;
  }
  /**
   * Find newly schedulable DependNode after updating
   * the dependent information by scheduled dn.
   * @param dn
   */
  void findSchedulable(DependNode dn){
    for (Iterator it = unSchedulable.iterator(); it.hasNext(); ){
      DependNode node = (DependNode)it.next();
      if (node.deleteDepend(dn)){
        schedulable.add(node);
        unSchedulable.remove(node);
      }
    }
  } 
	
  public String toString(){
    StringBuffer sb = new StringBuffer("******DependGraph******\n");
    if (lastBranch != null){
      sb.append("\n has last branch ------>\n");
    }
    sb.append("\nschedulable: -- list of schedulable nodes --\n");
    Iterator it = schedulable.iterator();
    while (it.hasNext()){
      sb.append(((DependNode)it.next()).toString()+"\n");
    }
    sb.append("\nunSchedulable: -- list of unschedulable nodes --\n");
    it = unSchedulable.iterator();
    while (it.hasNext()){
      sb.append(((DependNode)it.next()).toString()+"\n");
    }
    return sb.toString();
  }
}


