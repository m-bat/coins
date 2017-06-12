/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */

package coins.backend.sched;

import coins.backend.MachineParams;
import coins.backend.Op;
import coins.backend.Type;
import coins.backend.lir.LirIconst;
import coins.backend.lir.LirNode;
import coins.backend.util.BiList;

/**
 * Class to generate information of LirNode instruction
 */
public class LirNodeInf {
  static final int LOAD_LATENCY = 4;
  static final int PARALLEL_LATENCY = 0;
  static final LirNode MEM = new LirIconst(0, 0, 0, null);//represent MEM
  static final LirNode STACK_REG = new LirIconst(0,0,0,null);//represent STACK_REG
  LirNode lirNode;
  Schedule schedule;
  MachineParams machineParams;
  LirNodeInf(LirNode ln, Schedule schedule){
    lirNode = ln;
    this.schedule = schedule;
    this.machineParams = schedule.codeGen.getMachineParams();
  }
  /**
   * Return list of input(use) registers or MEM of this LirNode
   */
  BiList input(){
    BiList bl = new BiList();
    addRegUse(bl, lirNode);
    addMemUse(bl, lirNode);
    return bl;
  }
  /**
   * Return list of output(def) registers or MEM of this LirNode
   */
  BiList output(){
    BiList bl = new BiList();
    addRegDef(bl, lirNode);
    addMemDef(bl, lirNode);
    return bl;
  }
  /**
   * Retrun latency of this LirNode
   * @param cost
   */
  int latency(int cost){
    LirNode ln = lirNode;
    if (lirNode.opCode == Op.PARALLEL){
    	return PARALLEL_LATENCY;
    }
    if (ln.opCode == Op.SET){
      int n = ln.nKids();
      if (n == 2 && ln.kid(1).opCode == Op.STATIC)
        return 1;
      for (int i = 1; i < n; i++)
        if (containsMEM(ln.kid(i)) && cost < LOAD_LATENCY)
          return LOAD_LATENCY;
      return cost;
    }
    else if (ln.isBranch())
      return 0; // be scheduled at end
    else
      return cost;
  }
  /**
   * Return true if LirNode ln contains Op.MEM 
   * @param ln
   */
  boolean containsMEM(LirNode ln){
    switch (ln.opCode) {
    case Op.MEM:
      return true;
    default:
      {
        int n = ln.nKids();
        for (int i = 0; i < n; i++)
          if(containsMEM(ln.kid(i)))
            return true;
        return false;
      }
    }
  }
  /**
   * Add constant MEM to BiList bl, if ln is a SET instruction
   * and right hand child has a MEM instruction, or if ln is a
   * CALL instruction (because stack memory may be used as parameters)
   * @param bl
   * @param ln
   */
  void addMemUse(BiList bl, LirNode ln){
    int n = ln.nKids();
    switch(ln.opCode) {
    case Op.PARALLEL:
 //     for (int i = 0; i < n; i++)
 //       addMemUse(bl, ln.kid(i));
      break;
    case Op.MEM:
      bl.add(MEM);
      break;
    case Op.SET:	
      for (int i = 1; i < n; i++)
        addMemUse(bl, ln.kid(i));
      break;
//    case Op.ASM:
//      for (int i = 1; i < n; i++)
//        addMemUse(bl, ln.kid(i));
//      break;
    case Op.CALL:
      bl.add(MEM);
      break;
    default:
      {
        for (int i = 0; i < n; i++)
          addMemUse(bl,ln.kid(i));
      }
      break;
    }
  }
  /**
   * Add constant MEM to BiList bl, if ln is a SET instruction
   * and left hand child has a MEM instruction
   * @param bl
   * @param ln
   */
  void addMemDef(BiList bl, LirNode ln){
    switch(ln.opCode){
    case Op.PARALLEL:
//      if (ln.kid(0).opCode == Op.ASM)
//    	  break;
//      for (int i = 0; i < ln.nKids(); i++)
//        addMemDef(bl, ln.kid(i));
      break;
    case Op.SET:
      addMemUse(bl, ln.kid(0));
      break;
//    case Op.CALL:
//      int n = ln.kid(2).nKids();
//      for (int i = 0; i < n; i++)
//        addMemUse(bl, ln.kid(2).kid(i));
//      break;
    default:
      break;
    }
  }
  /**
   * Add registers used in LirNode ln to BiList bl
   * @param bl
   * @param ln
   */
  void addRegUse(BiList bl, LirNode ln){
    switch (ln.opCode) {
    case Op.PARALLEL:
      {
        int n = ln.nKids();
        for (int i = n - 1; i >= 0; i--)
          addRegUse(bl, ln.kid(i));
      }
      break;
    case Op.SET:
      if (!isRegisterOperand(ln.kid(0)))
        addRegUse(bl, ln.kid(0));
      addRegUse(bl, ln.kid(1));
      break;

    case Op.CLOBBER:
    case Op.PROLOGUE:
      // all operands are defined, not used
      break;

    case Op.CALL:
      // callee and parameters
      addRegUse(bl, ln.kid(0));
      addRegUse(bl, ln.kid(1));
      break;
    case Op.ASM:
        addRegUse(bl, ln.kid(1)); // in-list
        addRegUse(bl, ln.kid(3)); // in/out-list
        break;
      
    case Op.SUBREG:
    case Op.REG:
    	  LirNode temp;
      if (isStackReg(ln))
        temp = STACK_REG;
      else
    	    temp = ln;
      if (!bl.contains(temp))
        bl.add(temp);
      break;

    default:
      {
        int n = ln.nKids();
        for (int i = 0; i < n; i++)
          addRegUse(bl,ln.kid(i));
      }
      break;
    }
  }
  /**
   * Add registers defined in LirNode ln to BiList bl
   * @param bl
   * @param ln
   */
  void addRegDef(BiList bl, LirNode ln){
    switch (ln.opCode) {
/*    case Op.PARALLEL:
      {
        int n = ln.nKids();
        for (int i = n - 1; i >= 0; i--)
          addRegDef(bl, ln.kid(i));
      }
      break;*/

    case Op.SET:
      if (isRegisterOperand(ln.kid(0)))
        addReg(bl, ln.kid(0));
      break;

    case Op.CLOBBER:
      {
        int n = ln.nKids();
        for (int i = 0; i < n; i++)
          addReg(bl, ln.kid(i));
      }
      break;

/*    case Op.CALL:
      {
        int n = ln.kid(2).nKids();
        for (int i = 0; i < n; i++)
          addReg(bl, ln.kid(2).kid(i));
      }
      break;*/
      
 /*   case Op.ASM:
    { for (int k = 2; k < 4; k++){
        int n = ln.kid(k).nKids();
        for (int i = 0; i < n; i++)
          addReg(bl, ln.kid(k).kid(i));
      }
    }
    break;*/

    case Op.PROLOGUE:
      {
        int n = ln.nKids();
        for (int i = 1; i < n; i++)
          addReg(bl, ln.kid(i));
      }
      break;
    }
  }
  void addReg(BiList bl, LirNode ln){
	  LirNode temp;
    switch (ln.opCode) {
    case Op.SUBREG:
 //     addReg(bl, ln.kid(0));
 //     break;
    case Op.REG:
      if (isStackReg(ln))
        temp = STACK_REG;
      else
 //   	    temp = machineParams.superRegisterNode(ln);
    	    temp = ln;
      if (!bl.contains(temp))
        bl.add(temp);
      break;
    }
  }
  
  LirNode getCalleeReg(LirNode ln){
    switch (ln.opCode){
    case Op.PARALLEL:
      return getCalleeReg(ln.kid(0));
    case Op.CALL:
      LirNode callee = ln.kid(0);
      if (callee.opCode == Op.REG){
        if (isStackReg(callee))
          return STACK_REG;
        else
 //         return machineParams.superRegisterNode(callee);
        	  return callee;
      }
    default:
      return null;
    }
  }

  private boolean isRegisterOperand(LirNode ln) {
    return (ln.opCode == Op.REG
            || ln.opCode == Op.SUBREG && ln.kid(0).opCode == Op.REG);
  }
  private boolean isStackReg(LirNode ln){
    if ( ! schedule.isX86 )
    	  return false;
    if (ln.opCode == Op.SUBREG)
      return isStackReg(ln.kid(0));
    if (ln.opCode == Op.REG){
    	  return (Type.tag(ln.type) == Type.FLOAT);
    }
    return false;
  }
}


