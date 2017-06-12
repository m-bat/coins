/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.ana;

import coins.backend.Function;
import coins.backend.LocalAnalysis;
import coins.backend.cfg.BasicBlk;
import coins.backend.cfg.FlowGraph;


/** Data Flow Analysis Tool */
public abstract class DataFlowAnalysis implements LocalAnalysis {

  /** Copy of CFG timestamp to be analyzed. */
  protected int timeStamp;
  protected Function function;

  protected boolean isForward;

  /** Solve data flow equations for Function f. */
  public DataFlowAnalysis(Function f) {
    function = f;
    FlowGraph g = f.flowGraph();
    timeStamp = f.timeStamp();

    DFST dfst = (DFST)function.require(DFST.analyzer);

    BasicBlk[] blks = dfst.blkVectorByPre();
    int maxDfn = dfst.maxDfn;
    int idBound = g.idBound();
    
    initialize();

    boolean changed = true;
    while (changed) {
      changed = false;

      if (isForward) {
        for (int i = 1; i <= maxDfn; i++) {
          confluence(blks[i]);
          changed |= transfer(blks[i]);
        }
      } else {
        for (int i = maxDfn; i >= 1; i--) {
          confluence(blks[i]);
          changed |= transfer(blks[i]);
        }
      }
    }

    windUp();
  }


  /** Return true if this analysis is up to date. */
  public boolean isUpToDate() {
    return (timeStamp == function.timeStamp());
  }



  /* Problem-Oriented Methods which subclasses should implement. */

  /** Initialize problem-oriented data structure. **/
  abstract void initialize();

  /** Supply confluence operator for block blk. It is either Meet or Join. **/
  abstract void confluence(BasicBlk blk);

  /** Supply transfer function for block blk. */
  abstract boolean transfer(BasicBlk blk);

  /** Finalize problem-oriented data structure. **/
  abstract void windUp();
}
