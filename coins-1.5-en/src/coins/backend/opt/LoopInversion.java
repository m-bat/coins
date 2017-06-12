/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.opt;

import coins.backend.CantHappenException;
import coins.backend.Data;
import coins.backend.Function;
import coins.backend.LocalTransformer;
import coins.backend.Op;
import coins.backend.Root;
import coins.backend.ana.LoopAnalysis;
import coins.backend.cfg.BasicBlk;
import coins.backend.cfg.FlowGraph;
import coins.backend.lir.LirNode;
import coins.backend.util.BiLink;
import coins.backend.util.BiList;
import coins.backend.util.ImList;


/** Convert while-do loops to repeat-until form. **/
public class LoopInversion {

  /** Trigger class. **/
  private static class Trigger implements LocalTransformer {
    public boolean doIt(Function func, ImList args) {
      (new LoopInversion()).doIt(func);
      return true;
    }

    public boolean doIt(Data data, ImList args) { return true; }

    public String name() { return "LoopInversion"; }

    public String subject() { return "Loop Inversion Optimization"; }
  }

  /** Trigger class singleton. **/
  public static final Trigger trig = new Trigger();

  private Root root;
  private FlowGraph g;
  private LoopAnalysis loop;

  private int[] order;
  private BiLink[] link;

  public LoopInversion() {}


  public void doIt(Function f) {
    root = f.root;
    g = f.flowGraph();
    loop = (LoopAnalysis)f.require(LoopAnalysis.analyzer);

    order = new int[g.idBound()];
    link = new BiLink[g.idBound()];
    /*
    if (false && root.dumpModule) {
      root.debOut.println("Loop Inversion for " + f.symbol.printName());
    }
    */

    // Set up link and order.
    int i = 1;
    for (BiLink p = g.basicBlkList.first(); !p.atEnd(); p = p.next()) {
      BasicBlk blk = (BasicBlk)p.elem();

      link[blk.id] = p;
      order[blk.id] = i++;
    }

    // invert top-level loops.
    for (BiLink p = loop.kids[0].first(); !p.atEnd(); p = p.next()) {
      BasicBlk head = (BasicBlk)p.elem();

      invertLoop(head);
    }
    // End of loop inversion.

    // Notify CFG modification.
    g.touch();
  }



  /** Invert the loop beginning at head. **/
  private void invertLoop(BasicBlk head) {
    // process subloops first
    for (BiLink p = loop.kids[head.id].first(); !p.atEnd(); p = p.next()) {
      BasicBlk blk = (BasicBlk)p.elem();

      if (loop.isLoop[blk.id])
        invertLoop(blk);
    }

    // Find tail
    BasicBlk tail = null;
    BasicBlk exit = null;
    for (BiLink p = head.predList().first(); !p.atEnd(); p = p.next()) {
      BasicBlk blk = (BasicBlk)p.elem();

      // blk part of loop?
      if (loop.loopHeader[blk.id] != head)
        continue;
      // Unconditional jump to head?
      if (((LirNode)blk.instrList().last().elem()).opCode != Op.JUMP)
        continue;
      // blk below head?
      if (order[blk.id] <= order[head.id])
        continue;
      // Is tail's next block out of loop?
      BasicBlk next = (BasicBlk)link[blk.id].next().elem();
      if (loop.loopHeader[next.id] == head)
        continue;

      // Look for best exit point in next block's predecessors
      for (BiLink q = next.predList().first(); !q.atEnd(); q = q.next()) {
        BasicBlk x = (BasicBlk)q.elem();
        Object succ[] = x.succList().toArray();
        // in this loop?
        if ((x == head || loop.loopHeader[x.id] == head)
            // ending with JUMPC?
            && ((LirNode)x.instrList().last().elem()).opCode == Op.JUMPC
            // either of successors in this loop?
            && (loop.loopHeader[((BasicBlk)succ[0]).id] == head
                || loop.loopHeader[((BasicBlk)succ[1]).id] == head)) {
          exit = x;
          tail = blk;
          break;
        }
      }
      if (tail != null)
        break;
    }
    if (tail == null)
      return;
    /*
    if (false && root.dumpModule) {
      root.debOut.println("head:#" + head.id + " exit:#" + exit.id
                          + " tail: #" + tail.id);
    }
    */

    // swap [head..exit], [next(exit)..tail]
    BiList upper = g.basicBlkList.split(link[head.id]);
    BiList lower = upper.split(link[exit.id].next());
    BiList after = lower.split(link[tail.id].next());
    BiLink top = lower.first();
    g.basicBlkList.concatenate(lower);
    g.basicBlkList.concatenate(upper);
    g.basicBlkList.concatenate(after);

    // renumber
    int n = order[head.id];
    for (BiLink p = top; ; p = p.next()) {
      if (p.atEnd())
        throw new CantHappenException("loop inversion");
      BasicBlk blk = (BasicBlk)p.elem();

      order[blk.id] = n++;
      /*
      if (false && root.dumpModule)
        root.debOut.println("renumber #" + blk.id);
      */
      if (blk == exit)
        break;
    }
  }
}
