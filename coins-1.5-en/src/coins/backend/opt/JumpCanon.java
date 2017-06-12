/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.opt;

import coins.backend.Data;
import coins.backend.Function;
import coins.backend.LocalTransformer;
import coins.backend.Op;
import coins.backend.cfg.BasicBlk;
import coins.backend.cfg.FlowGraph;
import coins.backend.lir.LirFactory;
import coins.backend.lir.LirNode;
import coins.backend.sym.Label;
import coins.backend.util.BiLink;
import coins.backend.util.ImList;


/** Canonicalize JUMP instructions so that
 *   their false-target have the label of the following block. **/
public class JumpCanon {

  /** Trigger class. **/
  private static class Trigger implements LocalTransformer {
    public boolean doIt(Function func, ImList args) {
      (new JumpCanon()).doIt(func);
      return true;
    }

    public boolean doIt(Data data, ImList args) { return true; }

    public String name() { return "JumpCanon"; }

    public String subject() { return "Jump Instruction Canonicalization"; }
  }

  /** Trigger class singleton. **/
  public static final Trigger trig = new Trigger();



  public JumpCanon() {}

  /** Canonicalize jumps */
  public void doIt(Function f) {
    FlowGraph g = f.flowGraph();
    LirFactory newLir = f.newLir;

    for (BiLink p = g.basicBlkList.first(); !p.atEnd(); p = p.next()) {
      BasicBlk blk = (BasicBlk)p.elem();

      BasicBlk nextblk = null;
      if (!p.next().atEnd())
        nextblk = (BasicBlk)p.next().elem();
      BiLink lastp = blk.instrList().last();
      if (!lastp.atEnd()) {
        LirNode ins = (LirNode)lastp.elem();
        if (ins.opCode == Op.JUMPC) {
          Label[] targets = ins.getTargets();
          if (targets[1].basicBlk() == nextblk)
            ;
          else if (targets[0].basicBlk() == nextblk) {
            // Reverse condition and swap true/false labels.
            int testOp = flipCondition(ins.kid(0).opCode);
            ins.setKid(0, newLir.operator(testOp,
                                          ins.kid(0).type,
                                          ins.kid(0).kid(0),
                                          ins.kid(0).kid(1), null));
            LirNode temp = ins.kid(1);
            ins.setKid(1, ins.kid(2));
            ins.setKid(2, temp);
          }
          else {
            // insert new block and rewrite false-target
            BasicBlk newblk = f.flowGraph().insertNewBlkBefore(nextblk);
            LirNode jmp = (LirNode)newblk.instrList().last().elem();
            jmp.setKid(0, ins.kid(2));
            ins.setKid(2, f.newLir.labelRef(newblk.label()));

            newblk.maintEdges();
            blk.maintEdges();
          }
        }
      }
    }
  }

  
  /** Flip test instruction **/
  private int flipCondition(int testOp) {
    switch (testOp) {
    case Op.TSTEQ: return Op.TSTNE;
    case Op.TSTNE: return Op.TSTEQ;
    case Op.TSTLTS: return Op.TSTGES;
    case Op.TSTLES: return Op.TSTGTS;
    case Op.TSTGTS: return Op.TSTLES;
    case Op.TSTGES: return Op.TSTLTS;
    case Op.TSTLTU: return Op.TSTGEU;
    case Op.TSTLEU: return Op.TSTGTU;
    case Op.TSTGTU: return Op.TSTLEU;
    case Op.TSTGEU: return Op.TSTLTU;
    default:
      throw new IllegalArgumentException("non-test op");
    }
  }
}
