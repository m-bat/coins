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
import coins.backend.sym.Symbol;
import coins.backend.util.BiLink;
import coins.backend.util.BiList;
import coins.backend.util.HashNumberSet;
import coins.backend.util.Misc;
import coins.backend.util.NumberSet;
import java.io.PrintWriter;


/** Yet another live variable analysis.
 *  Analysis done by slotwise (each variables separately) */
public class LiveVariableSlotwise
  implements LocalAnalysis, LiveVariableAnalysis {

  /** Factory class of LiveVariableSlotwise. */
  private static class Analyzer implements LocalAnalyzer {
    public LocalAnalysis doIt(Function func) {
      return new LiveVariableSlotwise(func);
    }

    public String name() { return "LiveVariableSlotwise"; }
  }


  /** Factory singleton. */
  public static final Analyzer analyzer = new Analyzer();


  /** Live information for each basic block **/
  private NumberSet [] in;
  private NumberSet [] out;

  private EnumRegVars rn;

  private Function function;
  private FlowGraph flowGraph;
  private int timeStamp;

  /** Construct live variable information */
  private LiveVariableSlotwise(Function f) {
    function = f;
    flowGraph = f.flowGraph();
    timeStamp = function.timeStamp();

    rn = (EnumRegVars)f.require(EnumRegVars.analyzer);
    ScanVarReference sc = (ScanVarReference)f.require(ScanVarReference.analyzer);
    int maxvar = rn.nRegvars();

    in = new NumberSet[flowGraph.idBound()];
    out = new NumberSet[flowGraph.idBound()];

    for (BiLink p = flowGraph.basicBlkList.first(); !p.atEnd(); p = p.next()) {
      BasicBlk blk = (BasicBlk)p.elem();
      in[blk.id] = new HashNumberSet(maxvar);
      out[blk.id] = new HashNumberSet(maxvar);
    }

    // Compute live-in and live-out set.

    BasicBlk [] worklist = new BasicBlk[flowGraph.idBound()];
    int [] incheck = new int[flowGraph.idBound()];
    int [] outcheck = new int[flowGraph.idBound()];

    for (int vid = 1; vid < maxvar; vid++) {
      if (sc.useSites[vid] == null && sc.outUseSites[vid] == null)
        continue;

      // slotwise analysis for v.
      int nw = 0;
      // add use sites to worklist.
      if (sc.useSites[vid] != null) {
        for (BiLink q = sc.useSites[vid].first(); !q.atEnd(); q = q.next()) {
          BasicBlk blk = (BasicBlk)q.elem();
          incheck[blk.id] = vid;
          in[blk.id].add(vid);
          worklist[nw++] = blk;
        }
      }
      if (sc.defSites[vid] != null) {
        for (BiLink q = sc.defSites[vid].first(); !q.atEnd(); q = q.next()) {
          BasicBlk blk = (BasicBlk)q.elem();
          incheck[blk.id] = vid;
        }
      }
      if (sc.outUseSites[vid] != null) {
        for (BiLink q = sc.outUseSites[vid].first(); !q.atEnd(); q = q.next()) {
          BasicBlk blk = (BasicBlk)q.elem();
          out[blk.id].add(vid);
          outcheck[blk.id] = vid;
          if (incheck[blk.id] != vid) {
            incheck[blk.id] = vid;
            in[blk.id].add(vid);
            worklist[nw++] = blk;
          }
        }
      }
      
      // do while worklist not empty
      while (nw > 0) {
        BasicBlk blk = worklist[--nw];

        if (sc.phiDefSite[vid] == blk)
          continue;

        for (BiLink r = blk.predList().first(); !r.atEnd(); r = r.next()) {
          BasicBlk pred = (BasicBlk)r.elem();
          if (outcheck[pred.id] != vid) {
            outcheck[pred.id] = vid;
            out[pred.id].add(vid);
            if (incheck[pred.id] != vid) {
              incheck[pred.id] = vid;
              in[pred.id].add(vid);
              worklist[nw++] = pred;
            }
          }
        }
      }

    }

  }



  /* Interface of Live Variable Analysis */


  /** Return true if variable regvar is live at entry of blk. **/
  public boolean isLiveAtEntry(Symbol regvar, BasicBlk blk) {
    return in[blk.id].contains(rn.index(regvar));
  }

  /** Return true if variable regvar is live at entry of blk. **/
  public boolean isLiveAtEntry(int regvar, BasicBlk blk) {
    return in[blk.id].contains(regvar);
  }

  /** Return true if variable regvar is live at exit of blk. **/
  public boolean isLiveAtExit(Symbol regvar, BasicBlk blk) {
    return out[blk.id].contains(rn.index(regvar));
  }

  /** Return true if variable regvar is live at exit of blk. **/
  public boolean isLiveAtExit(int regvar, BasicBlk blk) {
    return out[blk.id].contains(regvar);
  }



  /** Return the list of live variables at exit of basic block blk. **/
  public BiList liveOut(BasicBlk blk) {
    BiList live = new BiList();
    for (NumberSet.Iterator it = out[blk.id].iterator(); it.hasNext(); )
      live.add(rn.toSymbol(it.next()));
    return live;
  }


  /** Return the list of live variables at entry of basic block blk. **/
  public BiList liveIn(BasicBlk blk) {
    BiList live = new BiList();
    for (NumberSet.Iterator it = in[blk.id].iterator(); it.hasNext(); )
      live.add(rn.toSymbol(it.next()));
    return live;
  }


  /** Return set of live variable numbers at entry of basic block. **/
  public NumberSet liveInSet(BasicBlk blk) { return in[blk.id]; }

  /** Return set of live variable numbers at exit of basic block. **/
  public NumberSet liveOutSet(BasicBlk blk) { return out[blk.id]; }

  /** Copy set of live variable numbers at exit of block blk to NumberSet x. **/
  public void getLiveOutSet(NumberSet x, BasicBlk blk) {
    x.copy(out[blk.id]);
  }
  

  /** Add set of live variable numbers at exit of block blk to NumberSet x. **/
  public void addLiveOutSet(NumberSet x, BasicBlk blk) {
    x.addAll(out[blk.id]);
  }
  

  /** Copy set of live variable numbers at entry to NumberSet x. **/
  public void getLiveInSet(NumberSet x, BasicBlk blk) {
    x.copy(in[blk.id]);
  }
  

  /** Add set of live variable numbers at entry to NumberSet x. **/
  public void addLiveInSet(NumberSet x, BasicBlk blk) {
    x.addAll(in[blk.id]);
  }
 


  /** Return true if this analysis is up to date. */
  public boolean isUpToDate() {
    return (timeStamp == function.timeStamp());
  }


  /** Print Live variables */
  private void printLive(String head, NumberSet live, PrintWriter output) {
    output.print(head);
    int[] v = new int[live.size()];
    live.toArray(v);
    Misc.sort(v, v.length);
    for (int i = 0; i < v.length; i++)
      output.print(" " + rn.toString(v[i]));
    output.println();
  }

  /** Debug print entries required by interface. **/

  public void printBeforeFunction(PrintWriter output) {}

  public void printBeforeBlock(BasicBlk blk, PrintWriter output) {
    printLive("    in:", in[blk.id], output);
  }

  public void printAfterBlock(BasicBlk blk, PrintWriter output) {
    printLive("   out:", out[blk.id], output);
  }

  public void printBeforeStmt(LirNode stmt, PrintWriter output) {}

  public void printAfterStmt(LirNode stmt, PrintWriter output) {}

  public void printAfterFunction(PrintWriter output) {}

}
