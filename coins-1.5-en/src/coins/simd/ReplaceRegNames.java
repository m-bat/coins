/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.simd;

import coins.backend.Function;
import coins.backend.cfg.BasicBlk;
import coins.backend.lir.LirBinOp;
import coins.backend.lir.LirFactory;
import coins.backend.lir.LirNode;
import coins.backend.lir.LirSymRef;
import coins.backend.lir.LirUnaOp;
import coins.backend.Op;
import coins.backend.Type;
import coins.backend.util.BiLink;
import coins.backend.util.BiList;
import coins.backend.util.ImList;
import coins.backend.sym.Symbol;
import coins.backend.sym.SymAuto;

import java.lang.String;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

public class ReplaceRegNames {
  Name name;
  Function func;
  LirFactory newLir;
  ReversibleMap rmap=new ReversibleMap();
  HashMap ntmap=new HashMap();  // a name transition map.

  ReplaceRegNames(Function f) {
    this.name=new Name();
    this.func=f;
    this.newLir=func.newLir;
  }
  public void toNewName(BasicBlk blk) {
if(blk==null) System.out.println("blk : null");
if(blk.instrList()==null) System.out.println("instrList : null");
if(blk.instrList().first()==null) System.out.println("lir : null");
    for(BiLink lir=blk.instrList().first(); !lir.atEnd(); lir=lir.next()) {
      LirNode ins=(LirNode)lir.elem();
      LirNode nins=(LirNode)rmap.get(ins);
      if(nins!=null) {
        lir.setElem(nins);
        return;
      }
      if(ins.opCode==Op.SET) {
        LirNode r=replRight(ins.kid(1));
        LirNode l=replLeft(ins.kid(0));
        LirNode newins=new LirBinOp(ins.id, Op.SET, ins.type, l, r, ins.opt);
        rmap.put(ins, newins);
        lir.setElem(newins);
      }
    }
  }
  public void toNewName(Vector in) {
    for(int i=0; i<in.size(); i++) {
      LirNode ins=(LirNode)in.elementAt(i);
      LirNode newins=(LirNode)rmap.get(ins);
      if(newins!=null) {
        in.setElementAt(newins, i);
        return;
      }
      if(ins.opCode==Op.SET) {
        LirNode r=replRight(ins.kid(1));
        LirNode l=replLeft(ins.kid(0));
        newins=new LirBinOp(ins.id, Op.SET, ins.type, l, r, ins.opt);
        rmap.put(ins, newins);
        in.setElementAt(newins, i);
      }
    }
  }
  private LirNode replRight(LirNode lexp) {
    LirNode nlexp=(LirNode)rmap.get(lexp);
    if(nlexp!=null) return nlexp;
    switch(lexp.opCode) {
      case Op.REG:
        {
          LirSymRef reg=(LirSymRef)lexp;
          SymAuto sym=(SymAuto)reg.symbol;
          String symname=sym.name;
          String s=name.refName(symname);
          if(s==null) {
            s=name.newName(symname);
          }
          Symbol nsym=func.addSymbol(s, sym.storage, sym.type,
                        sym.boundary, sym.offset(), sym.opt());
          nlexp=new LirSymRef(reg.id, Op.REG, reg.type, nsym, reg.opt);
          rmap.put(lexp, nlexp);
          putNtmap(lexp, nlexp);
          return nlexp;
        }
      case Op.SUBREG:
        {
          LirBinOp olexp=(LirBinOp)lexp;
          LirNode arg0=replRight(olexp.kid(0));
          nlexp=new LirBinOp(olexp.id, olexp.opCode, olexp.type, arg0,
                          olexp.kid(1), olexp.opt);
          rmap.put(lexp, nlexp);
          putNtmap(lexp, nlexp);
          return nlexp;
        }
//
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
      case Op.MEM:
        {
          LirUnaOp olexp=(LirUnaOp)lexp;
          LirNode arg0=replRight(olexp.kid(0));
          nlexp=new LirUnaOp(olexp.id, olexp.opCode, olexp.type, arg0,
                          olexp.opt);
          rmap.put(lexp, nlexp);
          return nlexp;
        }
//
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
          LirBinOp olexp=(LirBinOp)lexp;
          LirNode arg0=replRight(olexp.kid(0));
          LirNode arg1=replRight(olexp.kid(1));
          nlexp=new LirBinOp(olexp.id, olexp.opCode, olexp.type, arg0,
                          arg1, olexp.opt);
          rmap.put(lexp, nlexp);
          return nlexp;
        }
//
      default:
        return lexp;
    }
  }
  private LirNode replLeft(LirNode lexp) {
    LirNode nlexp;
    switch(lexp.opCode) {
      case Op.REG:
        {
          LirSymRef reg=(LirSymRef)lexp;
          SymAuto sym=(SymAuto)reg.symbol;
          String symname=sym.name;
          String s=name.newName(symname);
          Symbol nsym=func.addSymbol(s, sym.storage, sym.type,
                        sym.boundary, sym.offset(), sym.opt());
          nlexp=new LirSymRef(reg.id, Op.REG, reg.type, nsym, reg.opt);
          rmap.put(lexp, nlexp);
          putNtmap(lexp, nlexp);
          return nlexp;
        }
      default:
        nlexp=(LirNode)rmap.get(lexp);
        if(nlexp!=null) return nlexp;
        return replRight(lexp);
    }
  }
  public void toOldName(BasicBlk blk) {
    for(BiLink lir=blk.instrList().first(); !lir.atEnd(); lir=lir.next()) {
      LirNode ins=(LirNode)lir.elem();
      LirNode newins=(LirNode)rmap.revget(ins);
      if(newins!=null) {
        lir.setElem(newins);
        return;
      };
      if(ins.opCode==Op.SET) {
        LirNode l=replToOldName(ins.kid(0));
        LirNode r=replToOldName(ins.kid(1));
        newins=new LirBinOp(ins.id, Op.SET, ins.type, l, r, ins.opt);
        lir.setElem(newins);
      }
    }
  }
  public void toOldName(Vector in) {
    for(int i=0; i<in.size(); i++) {
      LirNode ins=(LirNode)in.elementAt(i);
      LirNode newins=toOldName(ins);
      in.setElementAt(newins, i);
    }
  }
  private LirNode toOldName(LirNode ins) {
    LirNode newins=(LirNode)rmap.revget(ins);
    if(newins!=null) return newins;
    if(ins.opCode==Op.SET) {
      LirNode l=replToOldName(ins.kid(0));
      LirNode r=replToOldName(ins.kid(1));
      newins=new LirBinOp(ins.id, Op.SET, ins.type, l, r, ins.opt);
      return newins;
    }
    if(ins.opCode==Op.PARALLEL) {
      LirNode[] kids=new LirNode[ins.nKids()];
      for(int i=0; i<ins.nKids(); i++) {
        LirNode lnode=toOldName(ins.kid(i));
        kids[i]=lnode;
      }
      newins=newLir.operator(Op.PARALLEL, Type.UNKNOWN, kids, ImList.Empty);
      return newins;
    }
    return ins;
  }
  private LirNode replToOldName(LirNode lexp) {
    LirNode nlexp=(LirNode)rmap.revget(lexp);
    if(nlexp!=null) return nlexp;
    switch(lexp.opCode) {
      case Op.REG:
        {
          LirSymRef reg=(LirSymRef)lexp;
          SymAuto sym=(SymAuto)reg.symbol;
          String symname=sym.name;
          String s=name.origName(symname);
          if(s==symname) return lexp;
          Symbol nsym=func.addSymbol(s, sym.storage, sym.type,
                        sym.boundary, sym.offset(), sym.opt());
          nlexp=new LirSymRef(reg.id, Op.REG, reg.type, nsym, reg.opt);
          return nlexp;
        }
      case Op.SUBREG:
        {
          LirBinOp olexp=(LirBinOp)lexp;
          LirNode arg0=replToOldName(olexp.kid(0));
          nlexp=new LirBinOp(olexp.id, olexp.opCode, olexp.type, arg0,
                          olexp.kid(1), olexp.opt);
          return nlexp;
        }
//
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
      case Op.MEM:
        {
          LirUnaOp olexp=(LirUnaOp)lexp;
          LirNode arg0=replToOldName(olexp.kid(0));
          nlexp=new LirUnaOp(olexp.id, olexp.opCode, olexp.type, arg0,
                          olexp.opt);
          return nlexp;
        }
//
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
          LirBinOp olexp=(LirBinOp)lexp;
          LirNode arg0=replToOldName(olexp.kid(0));
          LirNode arg1=replToOldName(olexp.kid(1));
          nlexp=new LirBinOp(olexp.id, olexp.opCode, olexp.type, arg0,
                          arg1, olexp.opt);
          return nlexp;
        }
//
      default:
        return lexp;
    }
  }
  private void putNtmap(LirNode e1, LirNode e2) {
    List v;
    if(!ntmap.containsKey(e1)) {
      v=new LinkedList();
      v.add(e1);
    }
    else {
      v=(List)ntmap.get(e1);
    }
    if(!v.contains(e2)) v.add(e2);
    ntmap.put(e1, v);
  }
  public Collection getNtmap() { return ntmap.values(); }
  public Vector getReplRegPairs() {
    Set s=rmap.entries();
    Vector out=new Vector();
    // Filter regs
    Iterator it=s.iterator();
    while(it.hasNext()) {
      Map.Entry pair=(Map.Entry)it.next();
      if(!(pair.getValue() instanceof LirNode)) continue;
      if(((LirNode)pair.getValue()).opCode==Op.REG) out.addElement(pair);
    }
//
for(int i=0; i<out.size(); i++) {
  Map.Entry pair=(Map.Entry)out.elementAt(i);
  LirNode p1=(LirNode)pair.getKey();
  LirNode p2=(LirNode)pair.getValue();
  System.out.println("Entry pair: key="+p1+" ,value="+p2);
}
//
    return out;
  }
}
