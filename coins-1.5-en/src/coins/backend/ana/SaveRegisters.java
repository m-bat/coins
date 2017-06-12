/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.ana;

import coins.backend.Function;
import coins.backend.LocalAnalysis;
import coins.backend.LocalAnalyzer;
import coins.backend.Op;
import coins.backend.Root;
import coins.backend.MachineParams;
import coins.backend.cfg.BasicBlk;
import coins.backend.lir.LirNode;
import coins.backend.sym.Symbol;
import coins.backend.util.BiLink;
import coins.backend.util.BitMapSet;
import coins.backend.util.Misc;
import coins.backend.util.NumberSet;
import java.io.PrintWriter;
import java.util.Iterator;


/** Compute list of registers you must save. **/
public class SaveRegisters implements LocalAnalysis {

  /** Factory class of InterferenceGraph */
  private static class Analyzer implements LocalAnalyzer {
    public LocalAnalysis doIt(Function func) {
      return new SaveRegisters(func);
    }

    public String name() { return "SaveRegisters"; }
  }

  /** Factory singleton. */
  public static final Analyzer analyzer = new Analyzer();

  /** Registers you must save. **/
  public final BitMapSet calleeSave;

  private Root root;
  private Function function;
  private int timeStamp;
  private EnumRegVars rn;

  private MachineParams m;

  /** List of registers **/
  
  /** Create instance. **/
  private SaveRegisters(Function func) {
    function = func;
    root = func.root;
    timeStamp = func.timeStamp();

    rn = (EnumRegVars)func.require(EnumRegVars.analyzer);

    LirNode.Scanner scan = new LirNode.Scanner();

    m = func.module.targetMachine.machineParams;
    calleeSave = (BitMapSet)m.regSetMap(m.getRegSet("*reg-callee-saves*")).clone();
    BitMapSet notUsed = (BitMapSet)calleeSave.clone();

    for (BiLink p = func.flowGraph().basicBlkList.first(); !p.atEnd();
         p = p.next()) {
      BasicBlk blk = (BasicBlk)p.elem();

      for (BiLink q = blk.instrList().first(); !q.atEnd(); q = q.next()) {
        LirNode stmt = (LirNode)q.elem();

        for (Iterator it = scan.forDefs(stmt); it.hasNext(); ) {
          int def = rn.index((LirNode)it.next());
          if (def != 0)
            m.removeRegister(notUsed, def);
        }
        for (Iterator it = scan.forClobbers(stmt); it.hasNext(); ) {
          int def = rn.index((LirNode)it.next());
          if (def != 0)
            m.removeRegister(notUsed, def);
        }
      }
    }

    calleeSave.subtract(notUsed);
  }


  /** Return true if this analysis is up to date. */
  public boolean isUpToDate() {
    return (timeStamp == function.timeStamp());
  }


  /** Debug print entries required by interface. **/

  public void printBeforeFunction(PrintWriter output) {}
  public void printBeforeBlock(BasicBlk blk, PrintWriter output) {}
  public void printAfterBlock(BasicBlk blk, PrintWriter output) {}
  public void printBeforeStmt(LirNode stmt, PrintWriter output) {}
  public void printAfterStmt(LirNode stmt, PrintWriter output) {}

  public void printAfterFunction(PrintWriter output) {
    output.println();
    output.print("Registers should be saved:");
    
    for (NumberSet.Iterator it = calleeSave.iterator(); it.hasNext(); ) {
      int reg = it.next();
      output.print(" " + m.registerToString(reg));
    }
    output.println();
    output.println();
  }

}
