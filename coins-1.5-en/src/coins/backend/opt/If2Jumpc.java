/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.opt;

import coins.backend.Data;
import coins.backend.Function;
import coins.backend.LocalTransformer;
import coins.backend.Op;
import coins.backend.Type;
import coins.backend.cfg.BasicBlk;
import coins.backend.cfg.FlowGraph;
import coins.backend.lir.LirFactory;
import coins.backend.lir.LirFconst;
import coins.backend.lir.LirIconst;
import coins.backend.lir.LirNode;
import coins.backend.lir.LirSymRef;
import coins.backend.sym.Label;
import coins.backend.util.BiLink;
import coins.backend.util.BiList;
import coins.backend.util.ImList;
import coins.backend.TargetMachine;


/** Convert (IF t cond then else) to JUMPC. **/
public class If2Jumpc {

  /** Trigger class. **/
  private static class Trigger implements LocalTransformer {
    public boolean doIt(Function func, ImList args) {
      (new If2Jumpc()).doIt(func);
      return true;
    }

    public boolean doIt(Data data, ImList args) { return true; }

    public String name() { return "If2Jumpc"; }

    public String subject() {
      return "Converting IFs to JUMPCs";
    }
  }

  /** Trigger class singleton. **/
  public static final Trigger trig = new Trigger();

  private Function function;
  private TargetMachine tm;
  private LirFactory lir;

  public If2Jumpc() {}

  public void doIt(Function f) {
    function = f;
    FlowGraph flowGraph = f.flowGraph();
    tm = f.module.targetMachine;
    lir = f.newLir;

    for (BiLink q = function.lirList().first(); !q.atEnd(); q = q.next()) {
      LirNode ins = (LirNode)q.elem();
      BiList pre = new BiList();
      replaceTree(ins, q);
    }
  }


  /** Transform L-expression. **/
  private LirNode replaceTree(LirNode node, BiLink here) {
    if (node.opCode == Op.IF) {
      LirNode tmp = function.newTemp(node.type);
      LirNode thenl = lir.labelRef(function.newLabel());
      LirNode elsel = lir.labelRef(function.newLabel());
      LirNode merge = lir.labelRef(function.newLabel());

      LirNode cnode = replaceTree(node.kid(0), here);
      here.addBefore
        (lir.node
         (Op.JUMPC, Type.UNKNOWN, cnode, thenl, elsel));
      here.addBefore(lir.node(Op.DEFLABEL, Type.UNKNOWN, thenl));

      LirNode tnode = replaceTree(node.kid(1), here);
      here.addBefore(lir.node(Op.SET, node.type, tmp, tnode));
      here.addBefore(lir.node(Op.JUMP, Type.UNKNOWN, merge));

      here.addBefore(lir.node(Op.DEFLABEL, Type.UNKNOWN, elsel));

      LirNode enode = replaceTree(node.kid(2), here);
      here.addBefore(lir.node(Op.SET, node.type, tmp, enode));
      here.addBefore(lir.node(Op.DEFLABEL, Type.UNKNOWN, merge));
      return tmp;
    } else {
      int n = node.nKids();
      for (int i = 0; i < n; i++)
        node.setKid(i, replaceTree(node.kid(i), here));

      return node;
    }
  }

}
