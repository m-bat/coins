/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */

package coins.backend.regalo;

import coins.backend.Data;
import coins.backend.Function;
import coins.backend.LocalTransformer;
import coins.backend.Op;
import coins.backend.Root;
import coins.backend.Storage;
import coins.backend.cfg.BasicBlk;
import coins.backend.lir.LirNode;
import coins.backend.lir.LirSymRef;
import coins.backend.opt.Ssa;
import coins.backend.sym.Symbol;
import coins.backend.util.BiLink;
import coins.backend.util.ImList;
import coins.backend.util.UnionFind;


/** Separate variables into live-ranges. **/
public class LiveRange {

  /** Trigger class. **/
  private static class Trigger implements LocalTransformer {
    public boolean doIt(Function func, ImList args) {
      (new LiveRange(func)).doIt(args);
      return true;
    }

    public boolean doIt(Data data, ImList args) { return true; }

    public String name() { return "LiveRange"; }

    public String subject() { return "Live Range Identification"; }
  }

  /** Trigger class singleton. **/
  public static final Trigger trig = new Trigger();


    
  private Function func;
  private Root root;

  private UnionFind ufo;
  private Symbol[] symVector;

  /** Construct session object. **/
  private LiveRange(Function func) {
    this.func = func;
    root = func.root;
  }

  /** Main program follows. **/
  private void doIt(ImList args) {
    // Convert to SSA form.
    func.apply(Ssa.trigger("pruned"), args);

    symVector = func.symVector();
    ufo = new UnionFind(symVector.length);
    boolean[] used = new boolean[symVector.length];

    // Scan PHI instructions
    for (BiLink p = func.flowGraph().basicBlkList.first(); !p.atEnd(); p = p.next()) {
      BasicBlk blk = (BasicBlk)p.elem();

      for (BiLink q = blk.instrList().first(); !q.atEnd(); q = q.next()) {
        LirNode ins = (LirNode)q.elem();

        if (ins.opCode == Op.PHI) {
          Symbol dst = ((LirSymRef)ins.kid(0)).symbol;
          int n = ins.nKids();
          for (int i = 1; i < n; i++) {
            if (ins.kid(i).kid(0).opCode == Op.REG) {
              Symbol src = ((LirSymRef)ins.kid(i).kid(0)).symbol;
              join(dst, src);
            }
          }
        }
      }
    }

    // Rename to new name; remove phi
    for (BiLink p = func.flowGraph().basicBlkList.first(); !p.atEnd(); p = p.next()) {
      BasicBlk blk = (BasicBlk)p.elem();

      for (BiLink q = blk.instrList().first(); !q.atEnd(); ) {
        LirNode ins = (LirNode)q.elem();
        BiLink next = q.next();

        if (ins.opCode == Op.PHI)
          q.unlink();
        else
          renameRegvars(ins, used);

        q = next;
      }
    }

    // Squeeze unused register variables
    for (BiLink p = func.localSymtab.symbols().first(); !p.atEnd(); p = p.next()) {
      Symbol sym = (Symbol)p.elem();

      if (sym.storage == Storage.REG && !used[sym.id])
        func.localSymtab.remove(sym);
    }
  }


  /** Rename variables and check uses **/
  private void renameRegvars(LirNode tree, boolean[] used) {
    int n = tree.nKids();
    for (int i = 0; i < n; i++) {
      if (tree.kid(i).opCode == Op.REG) {
        Symbol old = ((LirSymRef)tree.kid(i)).symbol;
        Symbol neu = nameof(old);
        if (neu != old) {
          tree.setKid(i, func.newLir.symRef(neu));
          used[neu.id] = true;
        } else
          used[old.id] = true;
      }
      else
        renameRegvars(tree.kid(i), used);
    }
  }



  /** Dressed Union-Find **/

  /** Return real name of x. **/
  Symbol nameof(Symbol x) {
    return symVector[ufo.find(x.id)];
  }

  /** Coalesce x & y and leave smaller-numbered symbol's name. **/
  void join(Symbol x, Symbol y) {
    int ix = ufo.find(x.id);
    int iy = ufo.find(y.id);
    if (ix != iy) {
      int i = ufo.union(ix, iy);
      symVector[i] = ix < iy ? symVector[ix] : symVector[iy];
    }
  }
}
