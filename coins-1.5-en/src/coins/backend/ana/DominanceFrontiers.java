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


/** Find dominance frontiers of the control flow graph. */
public class DominanceFrontiers implements LocalAnalysis {

  /** Factory class of DominanceFrontiers. */
  private static class Analyzer implements LocalAnalyzer {
    public LocalAnalysis doIt(Function func) {
      return new DominanceFrontiers(func);
    }

    public String name() { return "DominanceFrontiers"; }
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


  /** Create dominance frontiers */
  private DominanceFrontiers(Function func) {
    function = func;
    flowGraph = func.flowGraph();
    timeStamp = flowGraph.timeStamp();

    frontiers = new BiList[flowGraph.idBound()];
    addFrontiers(flowGraph.entryBlk(),
                 (Dominators)func.require(Dominators.analyzer));
  }


  private void addFrontiers(BasicBlk blk, Dominators d) {

    for (BiLink p = d.kids[blk.id].first(); !p.atEnd(); p = p.next())
      addFrontiers((BasicBlk)p.elem(), d);

    frontiers[blk.id] = new BiList();
    for (BiLink p = blk.succList().first(); !p.atEnd(); p = p.next()) {
      BasicBlk succ = (BasicBlk)p.elem();
      if (d.idom[succ.id] != blk)
        frontiers[blk.id].add(succ);
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


  /** Dump dominance frontiers on stream out (OBSOLETED) **/
  public void printIt(PrintWriter out) { printAfterFunction(out); }

  /** Debug print entries required by interface. **/
  public void printBeforeFunction(PrintWriter output) {}
  public void printBeforeBlock(BasicBlk blk, PrintWriter output) {}
  public void printAfterBlock(BasicBlk blk, PrintWriter output) {}
  public void printBeforeStmt(LirNode stmt, PrintWriter output) {}
  public void printAfterStmt(LirNode stmt, PrintWriter output) {}

  /** Dump dominance frontiers on stream out */
  public void printAfterFunction(PrintWriter out) {
    DFST dfst = (DFST)function.require(DFST.analyzer);
    out.println();
    out.println("Dominance Frontiers:");
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
