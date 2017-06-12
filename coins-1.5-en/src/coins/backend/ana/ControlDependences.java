/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.ana;

import coins.backend.Function;
import coins.backend.LocalAnalysis;
import coins.backend.LocalAnalyzer;
import coins.backend.cfg.BasicBlk;
import coins.backend.cfg.FlowGraph;
import coins.backend.lir.LirNode;
import coins.backend.util.BiLink;
import coins.backend.util.BiList;
import java.io.PrintWriter;


/** Find Control Dependences of the CFG. */
public class ControlDependences implements LocalAnalysis {

  /** Factory class of ControlDependences. */
  private static class Analyzer implements LocalAnalyzer {
    public LocalAnalysis doIt(Function func) {
      return new ControlDependences(func);
    }

    public String name() { return "ControlDependences"; }
  }

  /** Factory singleton. */
  public static final Analyzer analyzer = new Analyzer();

  /** BasicBlk-id-indexed array, whose elements are dominance
   *   frontiers of the block. */
  public final BiList frontiers[];

  private Function function;
  private FlowGraph flowGraph;

  /** Copy of CFG timestamp to be analyzed. */
  private int timeStamp;


  /** Create control dependences */
  private ControlDependences(Function func) {
    function = func;
    flowGraph = func.flowGraph();
    timeStamp = flowGraph.timeStamp();

    frontiers = new BiList[flowGraph.idBound()];
    addFrontiers(flowGraph.exitBlk(),
                 (Postdominators)func.require(Postdominators.analyzer));
  }


  private void addFrontiers(BasicBlk blk, Postdominators d) {

    for (BiLink p = d.kids[blk.id].first(); !p.atEnd(); p = p.next())
      addFrontiers((BasicBlk)p.elem(), d);

    frontiers[blk.id] = new BiList();
    for (BiLink p = blk.predList().first(); !p.atEnd(); p = p.next()) {
      BasicBlk pred = (BasicBlk)p.elem();
      if (d.idom[pred.id] != blk)
        frontiers[blk.id].add(pred);
    }
    // Check dummy edges for dealing with infinite loops.
    for (BiLink p = blk.dummyPredList().first(); !p.atEnd(); p = p.next()) {
      BasicBlk pred = (BasicBlk)p.elem();
      if (d.idom[pred.id] != blk)
        frontiers[blk.id].add(pred);
    }

    for (BiLink p = d.kids[blk.id].first(); !p.atEnd(); p = p.next()) {
      BasicBlk kid = (BasicBlk)p.elem();
      for (BiLink q = frontiers[kid.id].first(); !q.atEnd(); q = q.next()) {
        BasicBlk front = (BasicBlk)q.elem();
        if (d.idom[front.id] != blk)
          frontiers[blk.id].addNew(front);
      }
    }

  }
      

  /** Return true if this analysis is up to date. */
  public boolean isUpToDate() {
    return (timeStamp == flowGraph.timeStamp());
  }


  /** Dump control dependences on stream out (OBSOLETED) **/
  public void printIt(PrintWriter out) { printAfterFunction(out); }

  /** Debug print entries required by interface. **/
  public void printBeforeFunction(PrintWriter output) {}
  public void printBeforeBlock(BasicBlk blk, PrintWriter output) {}
  public void printAfterBlock(BasicBlk blk, PrintWriter output) {}
  public void printBeforeStmt(LirNode stmt, PrintWriter output) {}
  public void printAfterStmt(LirNode stmt, PrintWriter output) {}

  /** Dump control dependences on stream out */
  public void printAfterFunction(PrintWriter out) {
    ReverseDFST dfst = (ReverseDFST)function.require(ReverseDFST.analyzer);
    out.println();
    out.println("Control Dependences:");
    for (BiLink p = flowGraph.basicBlkList.first(); !p.atEnd(); p = p.next()) {
      BasicBlk blk = (BasicBlk)p.elem();
      if (dfst.dfn[blk.id] != 0) {
        out.print("#" + blk.id + ": ");
        boolean top = true;
        for (BiLink f = frontiers[blk.id].first(); !f.atEnd(); f = f.next()) {
          if (!top) out.print(",");
          out.print("#" + ((BasicBlk)f.elem()).id);
          top = false;
        }
        out.println();
      }
    }
  }

}
