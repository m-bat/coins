/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.ana;

import coins.backend.Function;
import coins.backend.LocalAnalysis;
import coins.backend.LocalAnalyzer;
import coins.backend.Op;
import coins.backend.TargetMachine;
import coins.backend.MachineParams;
import coins.backend.cfg.BasicBlk;
import coins.backend.lir.LirLabelRef;
import coins.backend.lir.LirNode;
import coins.backend.util.BiLink;
import coins.backend.util.BiList;
import coins.backend.util.NumberSet;
import java.util.Iterator;
import java.io.PrintWriter;


/**
 *  Scan program and collect register variable use/def statistics.
 **/
public class ScanVarReference implements LocalAnalysis {

  /** Factory class of ScanVarReferences. */
  private static class Analyzer implements LocalAnalyzer {
    public LocalAnalysis doIt(Function func) {
      return new ScanVarReference(func);
    }

    public String name() { return "ScanVarReference"; }
  }


  /** Factory singleton. */
  public static final Analyzer analyzer = new Analyzer();


  /** defSites[v]: List of basic blocks which has definitions of v **/
  public final BiList[] defSites;

  /** useSites[v]: List of basic blocks which has upward exposed uses of v **/
  public final BiList[] useSites;

  /** outUseSites[v]: List of basic blocks variables used after **/
  public final BiList[] outUseSites;

  /** phiDefSite[v]: Basic block of PHI-definition point of v. **/
  public final BasicBlk[] phiDefSite;


  /** working set (should be assigned null at end) **/
  private EnumRegVars rn;
  private int nPhyReg;
  private int maxvar;
  private int[] def;
  private int[] use;

  private Function function;
  private int timeStamp;
  private MachineParams target;
  private int nPhyRegs;

  private ScanVarReference(Function f) {
    function = f;
    timeStamp = f.timeStamp();
    target = f.module.targetMachine.machineParams;

    rn = (EnumRegVars)f.require(EnumRegVars.analyzer);
    maxvar = rn.nRegvars();
    nPhyRegs = rn.nPhyRegs();

    int nblks = f.flowGraph().idBound();
    defSites = new BiList[maxvar];
    useSites = new BiList[maxvar];
    outUseSites = new BiList[maxvar];
    phiDefSite = new BasicBlk[maxvar];

    def = new int[maxvar];
    use = new int[maxvar];

    LirNode.Scanner sc = new LirNode.Scanner();

    for (BiLink p = f.flowGraph().basicBlkList.first(); !p.atEnd(); p = p.next()) {
      BasicBlk blk = (BasicBlk)p.elem();

      // Check uses in instructions list.
      for (BiLink q = blk.instrList().first(); !q.atEnd(); q = q.next()) {
        LirNode ins = (LirNode)q.elem();

        if (ins.opCode == Op.PHI) {
          phiDefSite[rn.index(ins.kid(0))] = blk;
          int n = ins.nKids();
          for (int i = 1; i < n; i++) {
            if (ins.kid(i).kid(0).opCode == Op.REG) {
              BasicBlk b = ((LirLabelRef)ins.kid(i).kid(1)).label.basicBlk();
              int vid = rn.index(ins.kid(i).kid(0));
              if (outUseSites[vid] == null)
                outUseSites[vid] = new BiList();
              outUseSites[vid].addNew(b);
            }
          }
        } else {
          for (Iterator it = sc.forUses(ins); it.hasNext(); ) {
            int v = rn.index((LirNode)it.next());

            if (def[v] != blk.id && use[v] != blk.id) {
              use[v] = blk.id;
              if (useSites[v] == null)
                useSites[v] = new BiList();
              useSites[v].add(blk);
            }
          }
          for (Iterator it = sc.forDefs(ins); it.hasNext(); ) {
            int v = rn.index((LirNode)it.next());

            if (def[v] != blk.id) {
              def[v] = blk.id;
              if (defSites[v] == null)
                defSites[v] = new BiList();
              defSites[v].add(blk);
            }
            if (v < nPhyRegs) {
              // Have to set subregister's definition information.
              //  if %eax defined then also set %ax,%al,%ah defined.
              short[] subRegs = target.subRegs(v);
              for (int i = 0; i < subRegs.length; i++) {
                int w = subRegs[i];
                if (def[w] != blk.id) {
                  def[w] = blk.id;
                  if (defSites[w] == null)
                    defSites[w] = new BiList();
                  defSites[w].add(blk);
                }
              }
            }
          }
        }
      }
    }
  }



  /** Return true if this analysis is up to date. */
  public boolean isUpToDate() {
    return (timeStamp == function.timeStamp());
  }


  /** Debug print entries required by interface. **/

  public void printBeforeFunction(PrintWriter output) {}

  public void printBeforeBlock(BasicBlk blk, PrintWriter output) {
  }

  public void printAfterBlock(BasicBlk blk, PrintWriter output) {
  }

  public void printBeforeStmt(LirNode stmt, PrintWriter output) {}

  public void printAfterStmt(LirNode stmt, PrintWriter output) {}

  public void printAfterFunction(PrintWriter output) {
    for (int i = 0; i < maxvar; i++) {
      if (useSites[i] != null || outUseSites[i] != null) {
        output.print(rn.toString(i) + " used: ");
        boolean beginning = true;
        if (useSites[i] != null) {
          for (BiLink p = useSites[i].first(); !p.atEnd(); p = p.next()) {
            BasicBlk b = (BasicBlk)p.elem();
            if (!beginning) output.print(",");
            output.print("#" + b.id);
            beginning = false;
          }
        }
        if (outUseSites[i] != null) {
          for (BiLink p = outUseSites[i].first(); !p.atEnd(); p = p.next()) {
            BasicBlk b = (BasicBlk)p.elem();
            if (!beginning) output.print(",");
            output.print("after #" + b.id);
            beginning = false;
          }
        }
        beginning = true;
        if (defSites[i] != null) {
          output.print("  defined: ");
          for (BiLink p = defSites[i].first(); !p.atEnd(); p = p.next()) {
            BasicBlk b = (BasicBlk)p.elem();
            if (!beginning) output.print(",");
            output.print("#" + b.id);
            beginning = false;
          }
        }
        if (phiDefSite[i] != null)
          output.print("   Phi-defined: #" + phiDefSite[i].id);
        output.println();
      }
    }
  }

}
