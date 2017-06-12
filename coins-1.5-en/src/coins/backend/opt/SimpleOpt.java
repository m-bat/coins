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
import coins.backend.util.BiLink;
import coins.backend.util.ImList;
import java.math.BigInteger;


/** Simple optimization. **/
public class SimpleOpt {

  /** Trigger class. **/
  private static class Trigger implements LocalTransformer {
    public boolean doIt(Function func, ImList args) {
      (new SimpleOpt()).doIt(func);
      return true;
    }

    public boolean doIt(Data data, ImList args) { return true; }

    public String name() { return "SimpleOpt"; }

    public String subject() {
      return "Simple Optimization (constant folding etc.)";
    }
  }

  /** Trigger class singleton. **/
  public static final Trigger trig = new Trigger();

  private Function function;
  private LirFactory newLir;

  private BasicBlk[] regDefAtBlk;
  private long[] regIValue;
  private double[] regFValue;

  public SimpleOpt() {}

  public void doIt(Function f) {
    function = f;
    FlowGraph flowGraph = f.flowGraph();
    newLir = f.newLir;

    int nsyms = f.localSymtab.idBound();
    regDefAtBlk = new BasicBlk[nsyms];
    regIValue = new long[nsyms];
    regFValue = new double[nsyms];

    for (BiLink p = flowGraph.basicBlkList.first(); !p.atEnd(); p = p.next()) {
      BasicBlk blk = (BasicBlk)p.elem();
      for (BiLink q = blk.instrList().first(); !q.atEnd(); q = q.next()) {
        LirNode ins = (LirNode)q.elem();

        switch (ins.opCode) {
        case Op.SET:
          replaceNth(ins, 1, blk);
          if (ins.kid(0).opCode == Op.MEM)
            optTree(ins.kid(0), blk);
          if (ins.kid(0).opCode == Op.REG) {
            int symid = ((LirSymRef)ins.kid(0)).symbol.id;
            regDefAtBlk[symid] = null;
            switch (Type.tag(ins.type)) {
            case Type.INT:
              if (ins.kid(1).opCode == Op.INTCONST) {
                regDefAtBlk[symid] = blk;
                regIValue[symid] = ((LirIconst)ins.kid(1)).value;
              }
              break;
            case Type.FLOAT:
              if (ins.kid(1).opCode == Op.FLOATCONST) {
                regDefAtBlk[symid] = blk;
                regFValue[symid] = ((LirFconst)ins.kid(1)).value;
              }
              break;
            }
          }
          break;

        case Op.CALL:
          // callee and parameters
          replaceNth(ins, 0, blk);
          replaceNth(ins, 1, blk);
          // l-value which stores returned value
          if (ins.kid(2).nKids() > 0) {
            LirNode lvalue = ins.kid(2).kid(0);
            if (lvalue.opCode == Op.MEM)
              optTree(lvalue, blk);
            else if (lvalue.opCode == Op.REG)
              regDefAtBlk[((LirSymRef)lvalue).symbol.id] = null;
          }
          break;

        case Op.ASM:
          replaceNth(ins, 1, blk);
          for (int j = 2; j < 3; j++) {
            for (int i = 0; i < ins.kid(j).nKids(); i++) {
              LirNode lvalue = ins.kid(j).kid(i);
              if (lvalue.opCode == Op.MEM)
                optTree(lvalue, blk);
              else if (lvalue.opCode == Op.REG)
                regDefAtBlk[((LirSymRef)lvalue).symbol.id] = null;
            }
          }
          break;

        case Op.JUMPC:
          replaceNth(ins, 0, blk);
          LirNode target = null;
          if (ins.kid(0).opCode == Op.INTCONST) {
            if (((LirIconst)ins.kid(0)).value != 0)
              target = ins.kid(1);
            else
              target = ins.kid(2);
            q.setElem(newLir.operator(Op.JUMP, Type.UNKNOWN, target, null));
            blk.maintEdges();
          }
          break;

        case Op.PARALLEL:
          // do not touch in parallel.
        case Op.CLOBBER:
        case Op.PROLOGUE:
          // all operands are defined, not used
          break;

        default:
          optTree(ins, blk);
          break;
        }
      }
    }
  }


  /** Transform node's nth child. **/
  private void replaceNth(LirNode node, int nth, BasicBlk blk) {
    LirNode newKid = optTree(node.kid(nth), blk);
    if (newKid != node.kid(nth)) {
      function.touch();
      node.setKid(nth, newKid);
    }
  }


  /** Transform subnode node. **/
  private LirNode optTree(LirNode node, BasicBlk blk) {
    if (node.opCode == Op.REG) {
      // return the value of variable
      //  if definition is in the same basic block.
      int symid = ((LirSymRef)node).symbol.id;
      if (regDefAtBlk[symid] == blk) {
        switch (Type.tag(node.type)) {
        case Type.INT:
          return newLir.iconst(node.type, regIValue[symid], null);
        case Type.FLOAT:
          return newLir.fconst(node.type, regFValue[symid], null);
        }
      }
      return node;
    }

    int n = node.nKids();
    for (int i = 0; i < n; i++)
      replaceNth(node, i, blk);

    return newLir.foldConstant(node);
  }

}
