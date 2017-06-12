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


/** Optimization on Aggregate Copy Propagation. **/
public class AggregatePropagation {

  /** Trigger class. **/
  private static class Trigger implements LocalTransformer {
    public boolean doIt(Function func, ImList args) {
      (new AggregatePropagation()).doIt(func);
      return true;
    }

    public boolean doIt(Data data, ImList args) { return true; }

    public String name() { return "AggregatePropagation"; }

    public String subject() { return "Aggregate Copy Propagation"; }
  }

  /** Trigger class singleton. **/
  public static final Trigger trig = new Trigger();


  private LirNode structRetVar;
  private LirNode structRetPtrVar;
  private LirFactory lir;

  private int typeAddress;

  public AggregatePropagation() {}

  public void doIt(Function func) {
    lir = func.newLir;
    typeAddress = func.module.targetMachine.typeAddress;

    structRetVar = null;

    for (BiLink p = func.flowGraph().exitBlk().instrList().first();
         !p.atEnd(); p = p.next()) {
      LirNode op = (LirNode)p.elem();
      if (op.opCode == Op.EPILOGUE
          && op.nKids() == 2
          && op.kid(1).opCode == Op.MEM && Type.isAggregate(op.kid(1).type)
          && op.kid(1).kid(0).opCode == Op.FRAME) {
        structRetVar = op.kid(1).kid(0);
        p.setElem(lir.node(Op.EPILOGUE, Type.UNKNOWN, op.kid(0)));
        break;
      }
    }

    if (structRetVar == null)
      return;

    func.touch();

    structRetPtrVar = func.newReg(".strretp", typeAddress);

    for (BiLink p = func.flowGraph().basicBlkList.first(); !p.atEnd(); p = p.next()) {
      BasicBlk blk = (BasicBlk)p.elem();
      for (BiLink q = blk.instrList().first(); !q.atEnd(); q = q.next()) {
        LirNode stmt = (LirNode)q.elem();

        LirNode newStmt = testAndRewrite(stmt);
        if (newStmt != stmt)
          q.setElem(newStmt);
      }
    }

    // remove return value variable.
    func.localSymtab.remove(((LirSymRef)structRetVar).symbol);
  }


  /** Rewrite aggregate FRAME variables to return value pointer. **/
  private LirNode testAndRewrite(LirNode node) {
    int n = node.nKids();
    for (int i = 0; i < n; i++)
      node.setKid(i, testAndRewrite(node.kid(i)));

    if (node.opCode == Op.FRAME
        && ((LirSymRef)node).symbol == ((LirSymRef)structRetVar).symbol)
        return structRetPtrVar;
    return node;
  }
}
