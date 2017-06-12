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
import java.io.PrintWriter;


/** Depth First Spanning Tree of the CFG. */
public class DFST implements LocalAnalysis {

  /** Factory class of DFST. */
  private static class Analyzer implements LocalAnalyzer {
    public LocalAnalysis doIt(Function func) {
      return new DFST(func.flowGraph());
    }

    public String name() { return "DFST"; }
  }


  /** Factory singleton. */
  public static final Analyzer analyzer = new Analyzer();


  /** Depth First Number (reverse postorder) **/
  public final int[] dfn;

  /** Depth First Number (preorder) **/
  public final int[] dfnPre;

  /** Link to parent basic block. **/
  public final BasicBlk[] parent;

  /** Maximum dfn **/
  public final int maxDfn;



  /** Flow Graph */
  private FlowGraph flowGraph;

  /** Copy of CFG timestamp to be analyzed. */
  private int timeStamp;

  /** Working variables **/
  private int cpre;
  private int crpost;

  /** Class constructor **/
  private DFST(FlowGraph g) {
    flowGraph = g;

    timeStamp = flowGraph.timeStamp();
    int nblks = flowGraph.idBound();
    dfn = new int[nblks];
    dfnPre = new int[nblks];
    parent = new BasicBlk[nblks];

    cpre = crpost = 0;
    depthFirstSearch(flowGraph.entryBlk(), null);
    maxDfn = cpre;

    for (BiLink p = flowGraph.basicBlkList.first(); !p.atEnd(); p = p.next()) {
      BasicBlk x = (BasicBlk)p.elem();
      if (dfn[x.id] != 0)
        dfn[x.id] += 1 - crpost;
    }
  }


  /** Depth First Search */
  void depthFirstSearch(BasicBlk blk, BasicBlk from) {
    dfnPre[blk.id] = ++cpre;
    parent[blk.id] = from;
    for (BiLink p = blk.succList().first(); !p.atEnd(); p = p.next()) {
      BasicBlk y = (BasicBlk)p.elem();
      if (dfnPre[y.id] == 0)
        depthFirstSearch(y, blk);
    }
    dfn[blk.id] = --crpost;
  }


  /** Return true if this analysis is up to date. */
  public boolean isUpToDate() {
    return (timeStamp == flowGraph.timeStamp());
  }


  /** Return the vector of basic block in DFN reverse postorder.
   *   Zeroth element is null, entry block is in [1]. */
  public BasicBlk[] blkVectorByRPost() {
    BasicBlk[] vec = new BasicBlk[maxDfn + 1];
    for (BiLink p = flowGraph.basicBlkList.first(); !p.atEnd(); p = p.next()) {
      BasicBlk blk = (BasicBlk)p.elem();
      if (dfn[blk.id] != 0)
        vec[dfn[blk.id]] = blk;
    }
    return vec;
  }


  /** Return the vector of basic block in DFN preorder.
   *   Zeroth element is null, entry block is in [1]. */
  public BasicBlk[] blkVectorByPre() {
    BasicBlk[] vec = new BasicBlk[maxDfn + 1];
    for (BiLink p = flowGraph.basicBlkList.first(); !p.atEnd(); p = p.next()) {
      BasicBlk blk = (BasicBlk)p.elem();
      if (dfnPre[blk.id] != 0)
        vec[dfnPre[blk.id]] = blk;
    }
    return vec;
  }


  /** Return true if node x is an ancestor of node y in the DFST. */
  public boolean isAncestorOf(BasicBlk x, BasicBlk y) {
    return (dfnPre[x.id] <= dfnPre[y.id] && dfn[x.id] <= dfn[y.id]);
  }



  /** Debug print entries required by interface. **/
  public void printBeforeFunction(PrintWriter output) {}

  public void printBeforeBlock(BasicBlk blk, PrintWriter output) {
    output.println("    DFN=(" + dfnPre[blk.id] + "," + dfn[blk.id] + ")");
  }

  public void printAfterBlock(BasicBlk blk, PrintWriter output) {}
  public void printBeforeStmt(LirNode stmt, PrintWriter output) {}
  public void printAfterStmt(LirNode stmt, PrintWriter output) {}

  public void printAfterFunction(PrintWriter out) {}

  public void printIt(PrintWriter out) {}

}
