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
import coins.backend.opt.AugmentCFG;
import coins.backend.util.BiLink;
import coins.backend.util.BiList;
import java.io.PrintWriter;
import java.util.Iterator;


/** Find Postdominators of the control flow graph. */
public class Postdominators implements LocalAnalysis {

  /** Factory class of Postdominators. */
  private static class Analyzer implements LocalAnalyzer {
    public LocalAnalysis doIt(Function func) {
      return new Postdominators(func);
    }

    public String name() { return "Postdominators"; }
  }

  /** Factory singleton. */
  public static final Analyzer analyzer = new Analyzer();

  /** BasicBlk-id-indexed array, whose elements are the parents
   *    of the block in the Postdominator tree. */
  public final BasicBlk [] idom;

  /** BasicBlk-id-indexed array, whose elements are the lists of children
   *   nodes in the Postdominator tree. */
  public final BiList [] kids;

  /** Function **/
  private Function function;

  /** Flow Graph */
  private FlowGraph flowGraph;

  /** Copy of CFG timestamp to be analyzed. */
  private int timeStamp;

  private ReverseDFST dfst;

  /** Create Postdominator tree for Graph g. */
  private Postdominators(Function f) {
    function = f;
    flowGraph = f.flowGraph();
    // If there are infinite loops, attach dummy edges
    function.apply(AugmentCFG.trig);

    dfst = (ReverseDFST)function.require(ReverseDFST.analyzer);
    timeStamp = flowGraph.timeStamp();

    int maxDfn = dfst.maxDfn;
    int idBound = flowGraph.idBound();

    // allocate widom
    int[] widom = new int[maxDfn + 1];
    BasicBlk [] blkdfn = new BasicBlk[maxDfn + 1];
    for (BiLink p = flowGraph.basicBlkList.first(); !p.atEnd(); p = p.next()) {
      BasicBlk blk = (BasicBlk)p.elem();
      if (dfst.dfn[blk.id] != 0) {
        blkdfn[dfst.dfn[blk.id]] = blk;
        if (dfst.parent[blk.id] != null)
          widom[dfst.dfn[blk.id]] = dfst.dfn[dfst.parent[blk.id].id];
      }
    }

    // Compute Postdominators
    boolean change;
    do {
      change = false;
      for (int i = 2; i <= maxDfn; i++) {
        int x = widom[i];
        for (BiLink p = blkdfn[i].succList().first(); !p.atEnd(); p = p.next()) {
          int y = dfst.dfn[((BasicBlk)p.elem()).id];
          while (x != y) {
            if (x > y)
              x = widom[x];
            else /* if (y > x) */
              y = widom[y];
          }
        }
        // additional check for dummy edges.
        for (BiLink p = blkdfn[i].dummySuccList().first(); !p.atEnd(); p = p.next()) {
          int y = dfst.dfn[((BasicBlk)p.elem()).id];
          while (x != y) {
            if (x > y)
              x = widom[x];
            else /* if (y > x) */
              y = widom[y];
          }
        }
        if (x != widom[i] && x != 0) {
          widom[i] = x;
          change = true;
        }
      }
    } while (change);

    // Now widom represents Postdominator tree.

    // Copy widom to idom.
    idom = new BasicBlk[idBound];
    for (int i = 1; i <= maxDfn; i++)
      idom[blkdfn[i].id] = blkdfn[widom[i]];

    // Set up children
    kids = new BiList[idBound];
    for (BiLink p = flowGraph.basicBlkList.first(); !p.atEnd(); p = p.next())
      kids[((BasicBlk)p.elem()).id] = new BiList();
    for (int i = 1; i <= maxDfn; i++) {
      if (widom[i] != 0)
        kids[blkdfn[widom[i]].id].add(blkdfn[i]);
    }
  }

  /** Return true if this analysis is up to date. */
  public boolean isUpToDate() {
    return (timeStamp == flowGraph.timeStamp());
  }

  /** Return immediate Postdominator of block blk. */
  public BasicBlk immPostdominator(BasicBlk blk) {
    return idom[blk.id];
  }

  /** Return children (immediate dominatees) list iterator of block blk. */
  public Iterator children(BasicBlk blk) {
    return kids[blk.id].iterator();
  }


  /** Test if the block x dominates y. */
  public boolean dominates(BasicBlk x, BasicBlk y) {
    // This is naive implementation: had better use O(1) algorithm.
    while (dfst.dfn[y.id] < dfst.dfn[x.id])
      y = idom[y.id];
    return y == x;
  }



  /** Debug print entries required by interface. **/
  public void printBeforeFunction(PrintWriter output) {}
  public void printBeforeBlock(BasicBlk blk, PrintWriter output) {}
  public void printAfterBlock(BasicBlk blk, PrintWriter output) {}
  public void printBeforeStmt(LirNode stmt, PrintWriter output) {}
  public void printAfterStmt(LirNode stmt, PrintWriter output) {}

  /** Dump Postdominator tree on stream out */
  public void printAfterFunction(PrintWriter out) {
    out.println();
    out.println("Postdominator Tree:");
    dumpIt(flowGraph.exitBlk(), "", "", out);
  }

  /** Dump Postdominator tree on stream out (OBSOLETED) */
  public void printIt(PrintWriter out) {
    out.println();
    out.println("Postdominator Tree:");
    dumpIt(flowGraph.exitBlk(), "", "", out);
  }

  private void dumpIt(BasicBlk blk, String pref1, String pref2, PrintWriter out) {
    out.print(pref1);
    if (pref2.length() > 0)
      out.print(" +-");
    out.print("#" + blk.id);
    if (!kids[blk.id].isEmpty())
      out.print("'s children: ");
    boolean first = true;
    for (BiLink p = kids[blk.id].first(); !p.atEnd(); p = p.next()) {
      out.print(first ? "#" : ",#");
      out.print(((BasicBlk)p.elem()).id);
      first = false;
    }
    out.println();
    String pref = pref1 + pref2;
    for (BiLink p = kids[blk.id].first(); !p.atEnd(); p = p.next())
      dumpIt((BasicBlk)p.elem(), pref, p.next().atEnd() ? "   " : " | ", out);
  }
}
