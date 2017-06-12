/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.opt;

import coins.backend.Data;
import coins.backend.Function;
import coins.backend.LocalTransformer;
import coins.backend.ana.DFST;
import coins.backend.ana.LoopAnalysis;
import coins.backend.ana.ReverseDFST;
import coins.backend.cfg.BasicBlk;
import coins.backend.cfg.FlowGraph;
import coins.backend.util.ImList;


/** Augment CFG so as to make exit reachable from every basic block.
 ** It finds infinite loop and then adds false edge going to exit. **/
public class AugmentCFG {

  /** Trigger class. **/
  private static class Trigger implements LocalTransformer {
    public boolean doIt(Function func, ImList args) {
      (new AugmentCFG()).doIt(func);
      return true;
    }

    public boolean doIt(Data data, ImList args) { return true; }

    public String name() { return "AugmentCFG"; }

    public String subject() { return "Adding False Edges to Infinite Loops"; }
  }

  /** Trigger class singleton. **/
  public static final Trigger trig = new Trigger();


  private Function function;

  public AugmentCFG() {}

  /** Augment CFG main process. */
  public void doIt(Function f) {
    function = f;
    FlowGraph flowGraph = f.flowGraph();

    for (;;) {
      DFST dfst = (DFST)function.require(DFST.analyzer);
      int maxDfn = dfst.maxDfn;
      ReverseDFST rdfst = (ReverseDFST)function.require(ReverseDFST.analyzer);

      BasicBlk [] v = dfst.blkVectorByRPost();
      boolean ok = true;
      for (int i = maxDfn; i >= 1; i--) {
        if (rdfst.dfn[v[i].id] == 0) {
          ok = false;
          break;
        }
      }
      if (ok) return;		// all blocks are exit-reachable.


      // There's a block that do not reach to exit.
      LoopAnalysis loop = (LoopAnalysis)function.require(LoopAnalysis.analyzer);
      
      for (int i = maxDfn; i >= 1; i--) {
        if (rdfst.dfn[v[i].id] == 0
            && loop.isLoop[v[i].id] && !loop.hasExit[v[i].id]) {
          // infinite loop found.
          v[i].addDummyEdge(flowGraph.exitBlk());
          flowGraph.touch();
          break;
        }
      }
    }
  }

}

