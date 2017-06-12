/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.ana;

import coins.backend.Function;
import coins.backend.LocalAnalysis;
import coins.backend.LocalAnalyzer;
import coins.backend.Op;
import coins.backend.Storage;
import coins.backend.TargetMachine;
import coins.backend.MachineParams;
import coins.backend.cfg.BasicBlk;
import coins.backend.cfg.FlowGraph;
import coins.backend.lir.LirNode;
import coins.backend.lir.LirSymRef;
import coins.backend.sym.Symbol;
import coins.backend.util.BiLink;
import coins.backend.util.NumberSet;
import java.io.PrintWriter;


/** Numbering register variables. */
public class EnumRegVars implements LocalAnalysis {

  /** Factory class of EnumRegVars. */
  private static class Analyzer implements LocalAnalyzer {
    public LocalAnalysis doIt(Function func) {
      return new EnumRegVars(func);
    }

    public String name() { return "EnumRegVars"; }
  }


  /** Factory singleton. */
  public static final Analyzer analyzer = new Analyzer();

  private Function function;
  private FlowGraph flowGraph;
  private int timeStamp;

  private int nRegvars;
  private int nPhyRegs;
  private int[] regvarIndex;
  private Symbol[] regvarVec;

  private MachineParams target;

  /** Constructor */
  private EnumRegVars(Function f) {
    function = f;

    target = function.module.targetMachine.machineParams;
    flowGraph = f.flowGraph();
    timeStamp = f.timeStamp();

    int maxSyms = function.localSymtab.idBound();
    nRegvars = target.nRegisters();
    if (false){
      for (BiLink p = function.module.globalSymtab.symbols().first();
           !p.atEnd(); p = p.next()) {
        if (((Symbol)p.elem()).storage == Storage.REG)
          nRegvars++;
      }
    }
    nPhyRegs = nRegvars;
    for (BiLink p = function.localSymtab.symbols().first(); !p.atEnd();
         p = p.next()) {
      if (((Symbol)p.elem()).storage == Storage.REG)
        nRegvars++;
    }
    regvarVec = new Symbol[nRegvars];
    regvarIndex = new int[maxSyms];
    for (int i = 0; i < maxSyms; i++)
      regvarIndex[i] = -1;
    for (int i = 0; i < nPhyRegs; i++)
      regvarVec[i] = target.registerSymbol(i);
    for (BiLink p = function.module.globalSymtab.symbols().first();
         !p.atEnd(); p = p.next()) {
      Symbol sym = (Symbol)p.elem();
      if (sym.storage == Storage.REG) {
        regvarIndex[sym.id] = target.registerIndex(sym);
      }
    }
    int n = nPhyRegs;
    for (BiLink p = function.localSymtab.symbols().first();
         !p.atEnd(); p = p.next()) {
      Symbol sym = (Symbol)p.elem();
      if (sym.storage == Storage.REG) {
        regvarVec[n] = sym;
        regvarIndex[sym.id] = n;
        n++;
      }
    }
  }


  /** Return number of register variables */
  public int nRegvars() { return nRegvars; }


  /** Return number of real register variables */
  public int nPhyRegs() { return nPhyRegs; }


  /** Convert register variable number to symbol. */
  public Symbol toSymbol(int index) { return regvarVec[index]; }


  /** Convert register variable number to string. */
  public String toString(int index) {
    if (index < nPhyRegs)
      return target.registerToString(index);
    else
      return regvarVec[index].printName();
  }

  /** Convert set of register variables s to string. **/
  public String toString(NumberSet s) {
    StringBuffer buf = new StringBuffer();
    boolean first = true;
    for (NumberSet.Iterator it = s.iterator(); it.hasNext(); ) {
      if (!first)
        buf.append(" ");
      buf.append(toString(it.next()));
      first = false;
    }
    return buf.toString();
  }

  /** Return index of register variable represented in LirNode node. **/
  public int index(LirNode node) {
    if (node.opCode != Op.REG
        && (node.opCode != Op.SUBREG || node.kid(0).opCode != Op.REG))
      return 0;
    int x = target.registerIndex(node);
    if (x != 0)
      return x;
    if (node.opCode == Op.SUBREG)
      node = node.kid(0);
    return index(((LirSymRef)node).symbol);
  }


  /** Return index of register variable represented in Symbol sym. **/
  public int index(Symbol sym) {
    if (sym.id < regvarIndex.length)
      return regvarIndex[sym.id];
    else
      return 0;
  }


  /** Return true if this analysis is up to date. */
  public boolean isUpToDate() {
    return (timeStamp == function.timeStamp());
  }


  /** Print internal state for debugging (OBSOLETED). **/
  public void printIt(PrintWriter out) {  }

  /** Debug print entries required by interface. **/

  public void printBeforeFunction(PrintWriter output) {}
  public void printBeforeBlock(BasicBlk blk, PrintWriter output) {}
  public void printAfterBlock(BasicBlk blk, PrintWriter output) {}
  public void printBeforeStmt(LirNode stmt, PrintWriter output) {}
  public void printAfterStmt(LirNode stmt, PrintWriter output) {}

  public void printAfterFunction(PrintWriter output) {
    output.println();
    output.println("Register variables numbered as:");
    for (int i = 1; i < nRegvars; i++) {
      output.println(i + ": " + toString(i));
    }
  }

}
