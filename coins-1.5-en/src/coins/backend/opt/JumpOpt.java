/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.opt;

import coins.backend.Data;
import coins.backend.Function;
import coins.backend.LocalTransformer;
import coins.backend.Op;
import coins.backend.ana.DFST;
import coins.backend.cfg.BasicBlk;
import coins.backend.cfg.FlowGraph;
import coins.backend.lir.LirNode;
import coins.backend.sym.Label;
import coins.backend.util.BiLink;
import coins.backend.util.ImList;


/** Remove Jump-only or unreachable blocks.
 *   An example of FlowGraph/BasicBlk transformation. */
public class JumpOpt {

  /** Trigger class. **/
  private static class Trigger implements LocalTransformer {
    public boolean doIt(Function func, ImList args) {
      (new JumpOpt()).doIt(func);
      return true;
    }

    public boolean doIt(Data data, ImList args) { return true; }


    public String name() { return "JumpOpt"; }

    public String subject() { return "Jump Instruction Optimization"; }
  }

  /** Trigger class singleton. **/
  public static final Trigger trig = new Trigger();

  public JumpOpt() {}

  /** Optimize jumps */
  public void doIt(Function f) {
    FlowGraph g = f.flowGraph();
    DFST dfst = (DFST)f.require(DFST.analyzer);

    // Remove jump-to-jumps.
    BasicBlk[] check = new BasicBlk[g.idBound()];
    for (BiLink p = g.basicBlkList.first(); !p.atEnd(); p = p.next()) {
      BasicBlk blk = (BasicBlk)p.elem();
      check[blk.id] = blk;
      for (BiLink s = blk.succList().first(); !s.atEnd(); s = s.next()) {
        BasicBlk target = (BasicBlk)s.elem();

        BasicBlk destination = finalDestination(target, blk, check);
        if (destination != target)
          blk.replaceSucc(target, destination);
      }
    }

    dfst = (DFST)f.require(DFST.analyzer);

    // Remove unreachable block except exit block.
    for (BiLink p = g.basicBlkList.first(); !p.atEnd(); ) {
      BasicBlk blk = (BasicBlk)p.elem();
      BiLink next = p.next();
      if (dfst.dfn[blk.id] == 0 && blk != g.exitBlk()) {
        // Unreachable block, remove it
        blk.clearEdges();
        p.unlink();
      }
      p = next;
    }

    // Merge adjacent blocks if their successor/predecessor is only one.
    for (BiLink p = g.basicBlkList.first(); !p.atEnd(); p = p.next()) {
      BasicBlk blk = (BasicBlk)p.elem();

      //if (blk == g.entryBlk())
      //  continue;

      while (!p.next().atEnd()) {
        BasicBlk nextBlk = (BasicBlk)p.next().elem();
        if (nextBlk == g.exitBlk())
          break;
        if (!(blk.succList().length() == 1
              && blk.succList().first().elem() == nextBlk
              && nextBlk.predList().length() == 1))
          break;

        // remove last instruction
        if (!blk.instrList().isEmpty()) {
          BiLink lastp = blk.instrList().last();
          switch (((LirNode)lastp.elem()).opCode) {
          case Op.JUMP:
          case Op.JUMPC:
          case Op.JUMPN:
            lastp.unlink();
          }
        }
        // merge adjacent blocks.
        blk.instrList().addAll(nextBlk.instrList());
        nextBlk.clearEdges();
        blk.maintEdges();
        p.next().unlink();
      }
    }

    g.touch();
  }


  /** Return final destination of blk. **/
  private BasicBlk finalDestination(BasicBlk blk, BasicBlk mark,
                                    BasicBlk[] check) {
    if (check[blk.id] == mark)
      return blk;

    check[blk.id] = mark;
    if (blk.instrList().isEmpty())
      return blk;

    LirNode ins = (LirNode)blk.instrList().first().elem();
    if (ins.opCode != Op.JUMP)
      return blk;

    Label[] targets = ins.getTargets();
    return finalDestination(targets[0].basicBlk(), mark, check);
  }

}
