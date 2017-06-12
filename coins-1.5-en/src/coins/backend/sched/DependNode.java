/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */

package coins.backend.sched;

import java.util.Iterator;

import coins.backend.CantHappenException;
import coins.backend.Op;
import coins.backend.lir.LirNode;
import coins.backend.util.BiList;
import coins.backend.MachineParams;

/**
 * LirNode with several information for dependency and scheduling.
 */
public class DependNode implements Comparable{
  static final int LAST_TIME = 100000;
  static int counter = 0; // counter for number of DepndNodes
  int number; 
  BiList trueDependOn; // list of flow-dependent DependNodes
  BiList falseDependOn; // list of false-dependent DependNodes
  BiList dependOn; // list of flow/false-dependent DependNodes
  BiList beDepended; // list of DependNodes which are dependent on this 
  LirNode instr; // instructon of this node
  LirNodeInf lirInf;
  BiList input; // input (ex. register) of this instruction
  BiList output; // output of this instruction
  int latency; // latency of this instruction
  int scheduleTime;
  boolean visited = false;//visited for path length calculation
  boolean dependMark = false;
  int pathLength = -1;
  int machineCodeSize;

  /** Flag indicates that this node has delayed-operation slot. **/
  boolean hasDelaySlot;

  public DependNode(LirNode ln, Schedule schedule){
    number = counter++;
    trueDependOn = new BiList();
    falseDependOn = new BiList();
    beDepended = new BiList();
    instr = ln;
    lirInf = new LirNodeInf(instr, schedule);
    input = lirInf.input();
    output = lirInf.output();
  }
  
  public int compareTo(Object dn){
	  return this.number - ((DependNode)dn).number;
  }
  void setMachineCodeSize(int size){
  	machineCodeSize = size;
  } 
	
  void setLatency(int cost){
    latency = lirInf.latency(cost);
  }

  public void letHaveDelaySlot() { hasDelaySlot = true; }

  public boolean hasDelaySlot() { return hasDelaySlot; }


  DependNode setScheduleTime(int n){
    if (instr.isBranch() || instr.opCode == Op.EPILOGUE 
    		|| instr.opCode == Op.PARALLEL)
      scheduleTime = LAST_TIME;// must be scheduled at last
    else
      scheduleTime = n;
    return this;
  }
  /**
   * return scheduleTime and post-devrement it
   */
  int scheduleTimeDcr(){
    if (scheduleTime > 0)
      return scheduleTime--; // post-decrement
    else
      return 0;
  }
  /**
   * Return true if this DependNode depends on dn, and 
   * add this to dn.beDepended.
   * If it is true dependent add dn to trueDependOn.
   * If it is false dependent add dn to falseDependOn.
   * @param dn
   * @return trueDepend || falseDepend
   */
  boolean dependOn(DependNode dn){
    boolean trueDepend = false;
    boolean falseDepend = false;
    if (this == dn)
      return false;
    for (Iterator it = input.iterator(); it.hasNext(); ){
      LirNode ln = (LirNode)it.next();
      if (contains(dn.output,ln)){
        trueDepend = true;
      }
    }
    for (Iterator it = output.iterator(); it.hasNext(); ){
      LirNode ln = (LirNode)it.next();
      if (contains(dn.output,ln) || contains(dn.input, ln))
        falseDepend = true;
    }
    if (trueDepend || falseDepend){
      if (!dn.beDepended.contains(this))
        dn.beDepended.add(this);
      if (trueDepend && !trueDependOn.contains(dn))
        trueDependOn.add(dn);
      if (falseDepend && !falseDependOn.contains(dn))
        falseDependOn.add(dn);
    }
    return trueDepend || falseDepend;
  }
  
  /**
   * Return the list of DependNodes on which this node depends
   * directly or indirectly
   */
  BiList dependOn(BiList list){
	  mark();
	  dependMark = false;
	  BiList dependList = new BiList();
	  for (Iterator it = list.iterator(); it.hasNext(); ){
		  DependNode dn = (DependNode)it.next();
		  if (dn.dependMark){
			  dn.dependMark = false;
			  dependList.add(dn);
		  }
		  if (dn == this)
			  break;
	  }
	 return dependList;
  }
  /**
   * Mark the DependNodes on which this node depends
   * directly or indirectly
   */
  void mark(){
	  for (Iterator it = trueDependOn.iterator(); it.hasNext(); ){
		  DependNode dn = (DependNode)it.next();
		  if(!dn.dependMark){
			dn.dependMark = true;
			dn.mark();
		  }
	  }
	  for (Iterator it = falseDependOn.iterator(); it.hasNext(); ){
		  DependNode dn = (DependNode)it.next();
		  if(!dn.dependMark){
			dn.dependMark = true;
			dn.mark();
		  }
	  }
  }
  
  /**
   * Delete dependent information from true/false-DependOn
   * and set schedule time.
   * @param dn
   * @return true if this becomes independent
   */
  public boolean deleteDepend(DependNode dn){
    if (falseDependOn.removeEqual(dn) != null)
      if (instr.isBranch() || instr.opCode == Op.EPILOGUE 
    		  || instr.opCode == Op.PARALLEL)
        scheduleTime = LAST_TIME;
      else
        scheduleTime = 0;
    if (trueDependOn.removeEqual(dn) != null)
      if (instr.isBranch() || instr.opCode == Op.EPILOGUE 
    		  || instr.opCode == Op.PARALLEL)
        scheduleTime = LAST_TIME;
      else
        scheduleTime = dn.latency > 0 ? dn.latency - 1 : 0;
    return trueDependOn.isEmpty() && falseDependOn.isEmpty();
  }
  /**
   * @return true if this is a call instruction
   */
  public boolean isCall(){
    if (instr.opCode == Op.CALL)
      return true;
    if(instr.opCode == Op.PARALLEL){
      if (instr.kid(0).opCode == Op.CALL)
        return true;
    }
    return false;
  }

  /** Return callee register. **/
  LirNode getCalleeReg() {
    return lirInf.getCalleeReg(instr);
  }

  /**
   * return true if bl contains ln.
   * @param bl list of LirNode
   * @param ln LirNode (register node or MEM)
   * @return bl contains ln
   */
  boolean contains(BiList bl, LirNode ln){
	  if (ln == LirNodeInf.MEM || ln == LirNodeInf.STACK_REG)
		  return bl.contains(ln);
	  for (Iterator it = bl.iterator(); it.hasNext(); ){
          LirNode nextLn = (LirNode)it.next();
          if (nextLn == LirNodeInf.MEM || nextLn == LirNodeInf.STACK_REG)
        	      continue;
          if (lirInf.machineParams.isOverlapped(ln, nextLn)){
          	  return true;
          }
    }
    return false;
  }
	
  /**
   * Returns the maximum path length from this node.
   * A path is a sequence of dependent nodes.
   * A path length is the sum of the latencys of nodes of the path. 
   * @return this.pathLength
   */
  int pathLength(){
    if(visited){
      if (this.pathLength < 0)
        throw new CantHappenException("cycle of DependNodes");
      return this.pathLength;
    }
    visited = true;
    int maxLength = 0;
    for (Iterator it = beDepended.iterator(); it.hasNext(); ){
      DependNode dn = (DependNode)it.next();
      int length = dn.pathLength();
      if (length > maxLength)
        maxLength = length;
    }
    this.pathLength = latency + maxLength;
    return this.pathLength;
  }
	
  public String toString(){
    StringBuffer sb = new StringBuffer("\nDependNode number "+number+"\n");
    sb.append("  instruction:\n  ");
    sb.append(instr.toString()+(hasDelaySlot()? " hasDelaySlot": ""));
    sb.append("\n  input:\n  ");
    sb.append(input.toString());
    sb.append("\n  output:\n  ");
    sb.append(output.toString());
    sb.append("\n  latency:"+latency+"\n");
    sb.append("  scheduleTime:"+scheduleTime+"\n");
    sb.append("  trueDependOn: ");
    Iterator it = trueDependOn.iterator();
    while (it.hasNext())
      sb.append(((DependNode)it.next()).number+" ");
    sb.append(",  falseDependOn: ");
    it = falseDependOn.iterator();
    while (it.hasNext())
      sb.append(((DependNode)it.next()).number+" ");
    sb.append("\n  beDepended: ");
    it = beDepended.iterator();
    while (it.hasNext())
      sb.append(((DependNode)it.next()).number+" ");
    return sb.toString();
  }
}

