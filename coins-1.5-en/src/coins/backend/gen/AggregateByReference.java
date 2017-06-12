/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.gen;

import coins.backend.CantHappenException;
import coins.backend.Data;
import coins.backend.Function;
import coins.backend.LocalTransformer;
import coins.backend.Op;
import coins.backend.Storage;
import coins.backend.Type;
import coins.backend.cfg.BasicBlk;
import coins.backend.lir.LirFactory;
import coins.backend.lir.LirNode;
import coins.backend.lir.LirSymRef;
import coins.backend.sym.Symbol;
import coins.backend.util.BiLink;
import coins.backend.util.ImList;


/** Convert aggregate parameter passing from by-value to by-reference. */
public class AggregateByReference {

  /** Trigger class. **/
  private static class Trigger implements LocalTransformer {
    public boolean doIt(Function func, ImList args) {
      (new AggregateByReference()).doIt(func);
      return true;
    }

    public boolean doIt(Data data, ImList args) { return true; }

    public String name() { return "AggregateByReference"; }

    public String subject() { return "Rewrite Aggregate Parameter Passing"; }
  }

  /** Trigger class singleton. **/
  public static final Trigger trig = new Trigger();


  public AggregateByReference() {}

  public void doIt(Function func) {
    LirFactory lir = func.newLir;

    // Change callee
    LirNode[] map = new LirNode[func.localSymtab.idBound()];

    for (BiLink p = func.flowGraph().basicBlkList.first(); !p.atEnd(); p = p.next()) {
      BasicBlk blk = (BasicBlk)p.elem();
      for (BiLink q = blk.instrList().first(); !q.atEnd(); q = q.next()) {
        LirNode stmt = (LirNode)q.elem();
        if (stmt.opCode == Op.PROLOGUE) {
          int n = stmt.nKids();
          for (int i = 1; i < n; i++) {
            if (stmt.kid(i).opCode == Op.MEM
                && Type.tag(stmt.kid(i).type) == Type.AGGREGATE) {
              if (stmt.kid(i).kid(0).opCode != Op.FRAME)
                throw new CantHappenException("expecting FRAME: " + stmt);

              Symbol var = ((LirSymRef)stmt.kid(i).kid(0)).symbol;
              String name = (var.name + "%").intern();
              LirNode reg = lir.symRef
                (func.addSymbol(name, Storage.REG,
                                func.module.targetMachine.typeAddress,
                                0, 0, null));
              map[var.id] = reg;
              stmt.setKid(i, reg);
              func.localSymtab.remove(var);
            }
          }
        }
        rewriteAggRef(stmt, map);
      }
    }

    // Change caller
    int tmpn = 1;
    for (BiLink p = func.flowGraph().basicBlkList.first(); !p.atEnd(); p = p.next()) {
      BasicBlk blk = (BasicBlk)p.elem();
      for (BiLink q = blk.instrList().first(); !q.atEnd(); q = q.next()) {
        LirNode stmt = (LirNode)q.elem();
        if (stmt.opCode == Op.CALL) {
          LirNode args = stmt.kid(1);
          int n = args.nKids();
          for (int i = 0; i < n; i++) {
            if (args.kid(i).opCode == Op.MEM
                && Type.tag(args.kid(i).type) == Type.AGGREGATE) {
              // make a copy of aggregate.
              int type = args.kid(i).type;
              String name = ".AG" + tmpn++;
              LirNode copy = lir.symRef
                (func.addSymbol(name, Storage.FRAME, type, 0, 0, null));
              q.addBefore(lir.operator
                          (Op.SET, type, lir.operator
                           (Op.MEM, type, copy, null),
                           args.kid(i), null));
              args.setKid(i, copy);
            }
          }
        }
      }
    }
  }


  /** Rewrite aggregate FRAME variables in <code>node</code>. **/
  private LirNode rewriteAggRef(LirNode node, LirNode[] map) {
    int n = node.nKids();
    for (int i = 0; i < n; i++)
      node.setKid(i, rewriteAggRef(node.kid(i), map));
    if (node.opCode == Op.FRAME) {
      LirNode reg = map[((LirSymRef)node).symbol.id];
      if (reg != null)
        return reg;
    }
    return node;
  }
}
