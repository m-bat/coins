/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.simd;

import java.util.Vector;
import coins.backend.Op;
import coins.backend.lir.LirNode;

public class LirMemUtil {
  public static boolean hasMemDef(LirNode inst) {
    switch(inst.opCode) {
      case Op.PARALLEL: {
        // We assume every component has a same form.
        return hasMemDef(inst.kid(0));
      }
      case Op.SET: {
        if(inst.kid(0).opCode==Op.MEM) return true;
        return false;
      }
      default:  return false;
    }
  }
  public static LirNode getMemName(LirNode inst) {
    switch(inst.opCode) {
      case Op.PARALLEL: {
        // We assume every component has a same form.
        return getMemName(inst.kid(0));
      }
      case Op.SET: {
        if(inst.kid(0).opCode!=Op.MEM) return null;
        return getMemBase(inst.kid(0).kid(0));
      }
      default:  return null;
    }
  }
  public static LirNode getMemBase(LirNode mem) {
    switch(mem.opCode) {
      case Op.MEM:
        {
          return getMemBase(mem.kid(0));
        }
      case Op.REG:
      case Op.FRAME:
      case Op.STATIC:
        {
          return mem;
        }
      case Op.ADD:
      case Op.SUB:
      case Op.MUL:
        {
          if(mem.kid(0).opCode==Op.INTCONST)
            return getMemBase(mem.kid(1));
          return getMemBase(mem.kid(0));
        }
      default:  return null;
    }
  }
  public static void findMemUse(Vector lirs, int i, LirNode base, Vector out) {
    for(int j=i; j<lirs.size(); j++) {
      LirNode inst=(LirNode)lirs.elementAt(j);
      if(hasMemDef(inst)) {
        LirNode mb=getMemName(inst);
        if(mb==base) return;
      }
      Vector mus=new Vector();
      getMemUse(inst, mus);
      for(int k=0; k<mus.size(); k++) {
        if(mus.elementAt(k)!=base) continue;
        out.addElement(inst);
        break;
      }
    }
  }
  public static void getMemUse(LirNode inst, Vector out) {
    switch(inst.opCode) {
      case Op.MEM:
        {
          LirNode base=getMemBase(inst);
          if(base!=null) out.addElement(base);
          return;
        }
      case Op.SET:
        {
          getMemUse(inst.kid(1), out);
          return;
        }
      case Op.PARALLEL:
        {
          for(int i=0; i<inst.nKids(); i++) {
            LirNode kInst=inst.kid(i);
            if(kInst.opCode==Op.SET) getMemUse(kInst.kid(1), out);
          }
          return;
        }
      // unary oprators
      case Op.NEG:
      case Op.BNOT:
      case Op.CONVSX:
      case Op.CONVZX:
      case Op.CONVIT:
      case Op.CONVFX:
      case Op.CONVFT:
      case Op.CONVFI:
      case Op.CONVSF:
      case Op.CONVUF:
        {
          getMemUse(inst.kid(0), out);
          return;
        }
      // binary operators
      case Op.LSHU:
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
      case Op.TSTEQ:
      case Op.TSTNE:
      case Op.TSTLTS:
      case Op.TSTLES:
      case Op.TSTGTS:
      case Op.TSTGES:
      case Op.TSTLTU:
      case Op.TSTLEU:
      case Op.TSTGTU:
      case Op.TSTGEU:
        {
          getMemUse(inst.kid(0), out);
          getMemUse(inst.kid(1), out);
          return;
        }
      default:  return;
    }
  }
}
