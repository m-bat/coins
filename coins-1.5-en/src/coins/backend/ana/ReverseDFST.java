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


/** Reverse Depth First Spanning Tree of the CFG. */
public class ReverseDFST implements LocalAnalysis {

  /** Factory class of ReverseDFST. */
  private static class Analyzer implements LocalAnalyzer {
    public LocalAnalysis doIt(Function func) {
      return new ReverseDFST(func.flowGraph());
    }

    public String name() { return "ReverseDFST"; }
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
  private ReverseDFST(FlowGraph g) {
    flowGraph = g;

    timeStamp = flowGraph.timeStamp();
    int nblks = flowGraph.idBound();
    dfn = new int[nblks];
    dfnPre = new int[nblks];
    parent = new BasicBlk[nblks];

    cpre = crpost = 0;
    depthFirstSearch(flowGraph.exitBlk(), null);
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
    for (BiLink p = blk.predList().first(); !p.atEnd(); p = p.next()) {
      BasicBlk y = (BasicBlk)p.elem();
      if (dfnPre[y.id] == 0)
        depthFirstSearch(y, blk);
    }
    for (BiLink p = blk.dummyPredList().first(); !p.atEnd(); p = p.next()) {
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



  /** Debug print entries required by interface. **/
  public void printBeforeFunction(PrintWriter output) {}

  public void printBeforeBlock(BasicBlk blk, PrintWriter output) {
    output.println("    Reverse DFN=(" + dfnPre[blk.id] + "," + dfn[blk.id] + ")");
  }

  public void printAfterBlock(BasicBlk blk, PrintWriter output) {}
  public void printBeforeStmt(LirNode stmt, PrintWriter output) {}
  public void printAfterStmt(LirNode stmt, PrintWriter output) {}

  public void printAfterFunction(PrintWriter out) {}

  public void printIt(PrintWriter out) {}

}
