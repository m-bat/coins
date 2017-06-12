/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.opt;

import coins.backend.Data;
import coins.backend.Function;
import coins.backend.LocalTransformer;
import coins.backend.Op;
import coins.backend.Storage;
import coins.backend.Type;
import coins.backend.cfg.BasicBlk;
import coins.backend.lir.LirBinOp;
import coins.backend.lir.LirFconst;
import coins.backend.lir.LirIconst;
import coins.backend.lir.LirLabelRef;
import coins.backend.lir.LirNaryOp;
import coins.backend.lir.LirNode;
import coins.backend.lir.LirSymRef;
import coins.backend.lir.LirUnaOp;
import coins.backend.lir.LirVisitor;
import coins.backend.sym.SymAuto;
import coins.backend.sym.Symbol;
import coins.backend.util.BiLink;
import coins.backend.util.ImList;
import java.util.Iterator;


/** Virtual Register Replacement */
public class IntroVirReg {

  /** Trigger class. **/
  private static class Trigger implements LocalTransformer {
    public boolean doIt(Function func, ImList args) {
      (new IntroVirReg()).doIt(func);
      return true;
    }

    public boolean doIt(Data data, ImList args) { return true; }

    public String name() { return "IntroVirReg"; }

    public String subject() { return "Virtual Register Replacement"; }
  }

  /** Trigger class singleton. **/
  public static final Trigger trig = new Trigger();


  private Symbol[] shadow;
  
  public IntroVirReg() { }

  // ##Should consult TMD##
  private boolean isRegisterType(int type) {
    return Type.tag(type) != Type.AGGREGATE;
  }

  public void doIt(Function f) {

    shadow = new Symbol[f.localSymtab.idBound()];
    final boolean[] isAddrTaken = new boolean[f.localSymtab.idBound()];

    LirVisitor v = new LirVisitor() {
        LirNode lastOp = null;

        public void visit(LirFconst node) {}
        public void visit(LirIconst node) {}
        public void visit(LirLabelRef node) {}

        public void visit(LirSymRef node) {
          if (node.opCode == Op.FRAME) {
            SymAuto sym = (SymAuto)node.symbol;
            if (lastOp.opCode != Op.MEM || lastOp.type != sym.type
                || lastOp.opt.locate("&V") != null)
              isAddrTaken[sym.id] = true;
          }
        }

        public void visit(LirUnaOp node) {
          if (node.opCode == Op.LABEL)
            return;
          lastOp = node;
          node.kid(0).accept(this);
        }

        public void visit(LirBinOp node) {
          lastOp = node;
          node.kid(0).accept(this);
          lastOp = node;
          node.kid(1).accept(this);
        }

        public void visit(LirNaryOp node) {
          int n = node.nKids();
          for (int i = 0; i < n; i++) {
            lastOp = node;
            node.kid(i).accept(this);
          }
        }
      };
    
    // Mark address taken variables
    for (BiLink p = f.flowGraph().basicBlkList.first(); !p.atEnd(); p = p.next()) {
      BasicBlk blk = (BasicBlk)p.elem();
      for (BiLink q = blk.instrList().first(); !q.atEnd(); q = q.next()) {
        LirNode stmt = (LirNode)q.elem();
        stmt.accept(v);
      }
    }

    // Allocate virtual registers
    for (Iterator it = f.localSymtab.iterator(); it.hasNext(); ) {
      Symbol var = (Symbol)it.next();
      if (var.storage == Storage.FRAME && !isAddrTaken[var.id]
          && isRegisterType(var.type)) {
        String name = (var.name + "%").intern();
        Symbol reg = f.addSymbol(name, Storage.REG, var.type, var.boundary, 0, null);
        shadow[var.id] = reg;
      }
    }

    // Replace (REG "tXXX") for (MEM (FRAME "xxx"))
    for (BiLink p = f.flowGraph().basicBlkList.first(); !p.atEnd(); p = p.next()) {
      BasicBlk blk = (BasicBlk)p.elem();
      for (BiLink q = blk.instrList().first(); !q.atEnd(); q = q.next()) {
        LirNode stmt = (LirNode)q.elem();
        replaceAll(stmt, f);
      }
    }

    // Remove unused (FRAME)'s
    for (Iterator it = f.localSymtab.iterator(); it.hasNext(); ) {
      Symbol var = (Symbol)it.next();
      if (var.id < shadow.length && shadow[var.id] != null)
        f.localSymtab.remove(var);
    }

    f.touch();
  }

  private LirNode replaceAll(LirNode node, Function f) {
    if (node.opCode == Op.LABEL)
      return node;
    if (node.opCode == Op.MEM) {
      LirNode mem = node.kid(0);
      if (mem.opCode == Op.FRAME) {
        Symbol var = ((LirSymRef)mem).symbol;
        Symbol reg = shadow[var.id];
        if (reg != null)
          return f.newLir.symRef(reg);
      }
    }
    int n = node.nKids();
    for (int i = 0; i < n; i++) {
      LirNode oldsrc = node.kid(i);
      LirNode newsrc = replaceAll(oldsrc, f);
      if (newsrc != oldsrc)
        node.setKid(i, newsrc);
    }
    return node;
  }


}
