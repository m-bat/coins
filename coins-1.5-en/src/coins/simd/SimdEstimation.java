/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.simd;

import coins.backend.Function;
import coins.backend.Op;
import coins.backend.cfg.BasicBlk;
import coins.backend.lir.LirNode;
import coins.backend.lir.PickUpVariable;
import coins.backend.util.BiLink;
import coins.backend.util.BiList;

import java.util.Vector;

/**
 * Class for estimation of SIMD optimization results
 */
public class SimdEstimation {
  private Function func;
  /**
   * Constrructs a SimdEstimation object
   */
  public SimdEstimation(Function func) {
    this.func=func;
  }
  /**
   * Do each estimation
   */
  public void doIt() throws SimdOptException {
    if(!estimateDefUse()) throw new SimdOptException("SimdEstimation found unused simd regs.");
  }
  private boolean estimateDefUse() {
    for(BiLink bp=func.flowGraph().basicBlkList.first(); !bp.atEnd(); bp=bp.next()) {
      BasicBlk blk=(BasicBlk)bp.elem();
      BiList instrList=blk.instrList();
      Object[] instrs=instrList.toArray();
      for(int i=0; i<instrs.length; i++) {
        LirNode ins=(LirNode)instrs[i];
        if(ins.opCode!=Op.PARALLEL || ins.kid(0).opCode!=Op.SET ||
            ins.kid(0).kid(0).opCode!=Op.SUBREG) continue;
        LirNode reg=ins.kid(0).kid(0).kid(0);
        if(reg.opCode!=Op.REG) continue;
        // reg is defined
        boolean score=false;
        for(int j=i+1; j<instrs.length; j++) {
          if(used(reg, (LirNode)instrs[j])) {
            score=true;
            break;
          }
          if(defined(reg, (LirNode)instrs[j])) break;
        }
        // reg is not used
        if(!score) {
//System.out.println("unused reg : "+reg);
//System.out.println("in : "+ins);
          return false;
        }
      }
    }
    return true;
  }
  /*
   * Checks if the given REG is used as a simd register in a LirNode
   */
  private boolean used(LirNode reg, LirNode ins) {
    if(reg.opCode!=Op.REG) return false;
    if(ins.opCode!=Op.PARALLEL) return false;
    boolean result=false;
    for(int i=0; i<ins.nKids(); i++) {
      if(ins.kid(i).opCode!=Op.SET) return false;
      result=(result | isContained(reg, ins.kid(i).kid(1)));
    }
    return result;
  }
  private boolean defined(LirNode reg, LirNode ins) {
    if(reg.opCode!=Op.REG) return false;
    if(ins.opCode!=Op.PARALLEL) return false;
    boolean result=false;
    for(int i=0; i<ins.nKids(); i++) {
      if(ins.kid(i).opCode!=Op.SET) return false;
      result=(result | isContained(reg, ins.kid(i).kid(0)));
    }
    return result;
  }
  class RegCollector implements PickUpVariable {
    private Vector reglist;
    RegCollector() {
      reglist=new Vector();
    }
    public void meetVar(LirNode node) {
      if(!reglist.contains(node)) reglist.addElement(node);
    }
    void subregFilter() {
      for(int i=0; i<reglist.size(); i++) {
        LirNode nd=(LirNode)reglist.elementAt(i);
        if(nd.opCode==Op.SUBREG) {
          reglist.setElementAt(nd.kid(0), i);
        }
      }
    }
    boolean contains(LirNode node) {
      return reglist.contains(node);
    }
    Vector reglist() {
      return reglist;
    }
    void printIt() {
      System.out.println("RegCollector:");
      for(int i=0; i<reglist.size(); i++) {
        System.out.println("  "+((LirNode)reglist.elementAt(i)).toString());
      }
    }
  }
  private boolean isContained(LirNode reg, LirNode nd) {
    RegCollector rc=new RegCollector();
    nd.pickUpUses(rc);
    rc.subregFilter();
    return rc.contains(reg);
  }
}
