/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.simd;

import coins.backend.cfg.BasicBlk;
import coins.backend.lir.LirNode;
import coins.backend.lir.LirSymRef;
import coins.backend.Op;
import coins.backend.util.BiLink;
import coins.backend.util.BiList;
import coins.backend.sym.Symbol;

import java.lang.String;
import java.util.HashMap;
import java.util.Vector;

public class RegGroups {
  private HashMap regRefs;
  RegGroups(BasicBlk blk) {
    regRefs=new HashMap();
    for(BiLink lir=blk.instrList().first(); !lir.atEnd(); lir=lir.next()) {
      LirNode ins=(LirNode)lir.elem();
      if(ins.opCode==Op.SET) addRegGroup(ins);
      else if(ins.opCode==Op.PARALLEL) {
        for(int i=0; i<ins.nKids(); i++) addRegGroup(ins.kid(i));
      }
    }
  }
  RegGroups(Vector is) {
    regRefs=new HashMap();
    for(int n=0; n<is.size(); n++) {
      LirNode ins=(LirNode)is.elementAt(n);
      if(ins.opCode==Op.SET) addRegGroup(ins);
      else if(ins.opCode==Op.PARALLEL) {
        for(int i=0; i<ins.nKids(); i++) addRegGroup(ins.kid(i));
      }
    }
  }
  private void addRegGroup(LirNode ins) {
    if(ins.kid(0).opCode==Op.REG) {
      LirNode lnode=pickupBasicNode(ins.kid(0));
      LirNode rnode=pickupBasicNode(ins.kid(1));
      if(lnode!=null) regRefs.put(lnode, rnode);
    }
  }
  private LirNode pickupBasicNode(LirNode lexp) {
    switch(lexp.opCode) {
      case Op.REG:
      case Op.FRAME:
        {
//          LirSymRef reg=(LirSymRef)lexp;
//          return ((Symbol)reg.symbol).name;
          return lexp;
        }
      case Op.MEM:
        {
          LirNode res=pickupBasicNode(lexp.kid(0));
          if(res==null) return lexp;
          if(res.opCode==Op.REG) {
            return lexp;
          }
          else {
            return res;
          }
        }
      case Op.ADD:
      case Op.SUB:
      case Op.MUL:
      case Op.DIVS:
      case Op.DIVU:
      case Op.MODS:
      case Op.MODU:
      case Op.BAND:
      case Op.BOR:
      case Op.BXOR:
      case Op.LSHS:
      case Op.RSHS:
      case Op.RSHU:
        {
          if(lexp.kid(1).opCode==Op.INTCONST) return pickupBasicNode(lexp.kid(0));
          return null;
        }
      default:
        return null;
    }
  }
  public boolean isEquivalent(LirNode reg1, LirNode reg2) {
    if(reg1.opCode!=reg2.opCode) return false;
    if(reg1.type!=reg2.type) return false;
    if(reg1.opCode==Op.INTCONST || reg1.opCode==Op.FLOATCONST) return true;
    LirNode rn1=rootNode(pickupBasicNode(reg1));
    LirNode rn2=rootNode(pickupBasicNode(reg2));
    if(rn1==null || rn2==null) return true;
    if(rn1.equals(rn2)) return true;
    return false;
  }
  private LirNode rootNode(LirNode regnode) {
// Begin(2005.1.13)
    if(regnode==null) return null;
// End(2005.1.13)
    LirNode rn=regnode;
    LirNode parent=regnode;
    while(parent!=null) {
      rn=parent;
      parent=(LirNode)regRefs.get(rn);
    }
    if(rn.opCode==Op.MEM) return pickupBasicNode(rn.kid(0));
    if(rn.opCode==Op.REG) return null;
    return rn;
  }
}
