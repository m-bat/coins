/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.ana;

import coins.backend.CollectVarInTree;
import coins.backend.Function;
import coins.backend.LocalAnalysis;
import coins.backend.LocalAnalyzer;
import coins.backend.Op;
import coins.backend.cfg.BasicBlk;
import coins.backend.lir.LirLabelRef;
import coins.backend.lir.LirNode;
import coins.backend.sym.Symbol;
import coins.backend.util.BiLink;
import coins.backend.util.BiList;
import coins.backend.util.BitMapSet;
import coins.backend.util.NumberSet;
import java.io.PrintWriter;


/** Live variable analysis.
 *  Set information is represented by bitmap vectors. */
public class LiveVariableBitMap extends DataFlowAnalysis
  implements LiveVariableAnalysis {

  /** Factory class of LiveVariableBitMap. */
  private static class Analyzer implements LocalAnalyzer {
    public LocalAnalysis doIt(Function func) {
      return new LiveVariableBitMap(func);
    }

    public String name() { return "LiveVariableBitMap"; }
  }


  /** Factory singleton. */
  public static final Analyzer analyzer = new Analyzer();


  /** Live information for each basic block **/
  private BitMapSet [] in;
  private BitMapSet [] out;
  private BitMapSet [] gen;
  private BitMapSet [] kill;


  private EnumRegVars rn;


  /** Construct live variable information */
  private LiveVariableBitMap(Function f) {
    super(f);
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
  



  /* Problem-Oriented Methods called by super class (DataFlowAnalysis). */

  /** Initialize problem-oriented data structure. **/
  void initialize() {
    isForward = false; // live variable anal. is backward-direction problem

    int maxBlks = function.flowGraph().idBound();

    rn = (EnumRegVars)function.require(EnumRegVars.analyzer);
    int nRegvars = rn.nRegvars();
    CollectVarInTree cv = new CollectVarInTree(function);

    in = new BitMapSet[maxBlks];
    out = new BitMapSet[maxBlks];
    kill = new BitMapSet[maxBlks];
    gen = new BitMapSet[maxBlks];
    for (int i = 0; i < maxBlks; i++) {
      in[i] = new BitMapSet(nRegvars);
      out[i] = new BitMapSet(nRegvars);
    }
    NumberSet w = new BitMapSet(nRegvars);

    // Compute kill and gen.
    for (BiLink p = function.flowGraph().basicBlkList.first(); !p.atEnd();
         p = p.next()) {
      BasicBlk blk = (BasicBlk)p.elem();
      kill[blk.id] = new BitMapSet(nRegvars);
      gen[blk.id] = new BitMapSet(nRegvars);

      // Scan LirNodes list backward
      for (BiLink q = blk.instrList().last(); !q.atEnd(); q = q.prev()) {
        LirNode stmt = (LirNode)q.elem();
        if (stmt.opCode != Op.PHI) {
          cv.getDefVars(stmt, w);
          kill[blk.id].addAll(w);
          gen[blk.id].removeAll(w);
          cv.getUseVars(stmt, w);
          kill[blk.id].removeAll(w);
          gen[blk.id].addAll(w);
        }
      }
    }

  }



  /** Supply confluence operator (=join at here) for block blk. **/
  void confluence(BasicBlk blk) {
    out[blk.id].clear();
    for (BiLink s = blk.succList().first(); !s.atEnd(); s = s.next()) {
      BasicBlk succ = (BasicBlk)s.elem();
      out[blk.id].join(in[succ.id]);

      /** Special treatment of PHI functions. **/
      /** x0 = phi(x1, x2*, x3); --> kill x0, gen x2 only if x0 is live **/

      for (BiLink q = succ.instrList().first(); !q.atEnd(); q = q.next()) {
        LirNode stmt = (LirNode)q.elem();
        if (stmt.opCode != Op.PHI)
          break;

        // Mark RHS 'used' only if LHS of PHI is live.
        if (out[blk.id].exist(rn.index(stmt.kid(0)))) {
          // Scan RHS of PHI which belongs to this block.
          int n = stmt.nKids();
          for (int i = 1; i < n; i++) {
            if (stmt.kid(i).kid(1).opCode == Op.LABEL
                && ((LirLabelRef)stmt.kid(i).kid(1)).label == blk.label()
                && stmt.kid(i).kid(0).opCode == Op.REG)
              out[blk.id].add(rn.index(stmt.kid(i).kid(0)));
          }
        }

        // Kill LHS of PHI.
        out[blk.id].remove(rn.index(stmt.kid(0)));
      }
    }
  }

  /** Supply transfer function for block blk. */
  boolean transfer(BasicBlk blk) {
    BitMapSet newin = (BitMapSet)out[blk.id].clone();
    newin.subtract(kill[blk.id]);
    newin.join(gen[blk.id]);
    boolean changed = !newin.equals(in[blk.id]);
    in[blk.id] = newin;
    return changed;
  }


  /** Finalization. **/
  void windUp() { }


  /** Print Live variables */
  private void printLive(String head, NumberSet live, PrintWriter output) {
    output.print(head);
    for (NumberSet.Iterator it = live.iterator(); it.hasNext(); )
      output.print(" " + rn.toString(it.next()));
    output.println();
  }

  /** Debug print entries required by interface. **/

  public void printBeforeFunction(PrintWriter output) {}

  public void printBeforeBlock(BasicBlk blk, PrintWriter output) {
    printLive("   kill:", kill[blk.id], output);
    printLive("    gen:", gen[blk.id], output);
    printLive("    in:", in[blk.id], output);
  }

  public void printAfterBlock(BasicBlk blk, PrintWriter output) {
    printLive("   out:", out[blk.id], output);
  }

  public void printBeforeStmt(LirNode stmt, PrintWriter output) {}

  public void printAfterStmt(LirNode stmt, PrintWriter output) {
    //    printLive("  live:", liveAt[stmt.id], output);
  }

  public void printAfterFunction(PrintWriter output) {}

}
