/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.ana;

// import coins.backend.CollectVarInTree;
import coins.backend.Function;
import coins.backend.LocalAnalysis;
import coins.backend.LocalAnalyzer;
import coins.backend.Op;
import coins.backend.Root;
import coins.backend.cfg.BasicBlk;
import coins.backend.lir.LirNode;
import coins.backend.sym.Symbol;
import coins.backend.util.BiLink;
import coins.backend.util.BitMapSet;
import coins.backend.util.HashNumberSet;
import coins.backend.util.Misc;
import coins.backend.util.NumberSet;
import coins.backend.util.VectorSet;
import java.io.PrintWriter;
import java.util.Iterator;


/** Create interference graph and disturbance graph **/
public class InterferenceGraph implements LocalAnalysis {

  /** Factory class of InterferenceGraph */
  private static class Analyzer implements LocalAnalyzer {
    public LocalAnalysis doIt(Function func) {
      return new InterferenceGraph(func, false);
    }

    public String name() { return "InterferenceGraph"; }
  }

  /** Factory singleton. */
  public static final Analyzer analyzer = new Analyzer();

  /** Factory class of InterferenceGraph */
  private static class Analyzer2 implements LocalAnalyzer {
    public LocalAnalysis doIt(Function func) {
      return new InterferenceGraph(func, true);
    }

    public String name() { return "InterferenceGraph2"; }
  }

  /** Factory singleton. */
  public static final Analyzer2 analyzerCopyNotInterfere = new Analyzer2();


  private Root root;
  private Function function;
  private int timeStamp;
  private EnumRegVars rn;
  private LiveVariableAnalysis liveInfo;

  private NumberSet adjMatrix;

  private static final boolean UseAdjMatrix = false;

  /** Array of set of other variables which are simultaneously live. **/
  private NumberSet[] interfereSets;

  /** Array of set of other variables which appears in one's life. **/
  private NumberSet[] disturbSets;

  /** Create interference graph of register variables in function func. **/
  private InterferenceGraph(Function func, boolean copyNotInterfere) {
    function = func;
    root = func.root;
    timeStamp = func.timeStamp();

    rn = (EnumRegVars)func.require(EnumRegVars.analyzer);
    liveInfo = (LiveVariableAnalysis)func.require(LiveVariableSlotwise.analyzer);
    int nRegvars = rn.nRegvars();

    // CollectVarInTree cv = new CollectVarInTree(func);
    LirNode.Scanner scan = new LirNode.Scanner();
    if (UseAdjMatrix)
      adjMatrix = new BitMapSet(nRegvars * (nRegvars - 1) / 2);

    interfereSets = new NumberSet[nRegvars];
    for (int i = 0; i < nRegvars; i++)
      interfereSets[i] = new HashNumberSet(nRegvars);

    disturbSets = new NumberSet[nRegvars];
    for (int i = 0; i < nRegvars; i++)
      disturbSets[i] = new BitMapSet(nRegvars);

    NumberSet live = new VectorSet(nRegvars);
    NumberSet work = new VectorSet(nRegvars);

    for (BiLink p = func.flowGraph().basicBlkList.first(); !p.atEnd();
         p = p.next()) {
      BasicBlk blk = (BasicBlk)p.elem();

      liveInfo.getLiveOutSet(live, blk);

      for (BiLink q = blk.instrList().last(); !q.atEnd(); q = q.prev()) {
        LirNode stmt = (LirNode)q.elem();

        if (stmt.opCode == Op.PHI) {
          int def = rn.index(stmt.kid(0));
          
          for (NumberSet.Iterator it = live.iterator(); it.hasNext(); ) {
            int vliv = it.next();
            setInterfere(def, vliv);
            setDisturb(def, vliv);
          }
          live.remove(def);
        }
        else if (copyNotInterfere && isCopyStatement(stmt)) {
          int def = rn.index(stmt.kid(0));
          int use = rn.index(stmt.kid(1));
          for (NumberSet.Iterator it = live.iterator(); it.hasNext(); ) {
            int vliv = it.next();
            if (vliv != use) {
              setInterfere(def, vliv);
              setDisturb(def, vliv);
            }
          }
          live.remove(def);
          live.add(use);
          for (NumberSet.Iterator it = live.iterator(); it.hasNext(); ) {
            int vliv = it.next();
            setDisturb(use, vliv);
          }
        }
        else {
          boolean useAfterDef = stmt.opt.locate("&use-after-def") != null;
          boolean clobberAfterDef = stmt.opt.locate("&clobber-after-def") != null;
          if (useAfterDef) {
            for (Iterator it = scan.forUses(stmt); it.hasNext(); ) {
              int use = rn.index((LirNode)it.next());
              if (use != 0)
                live.add(use);
            }
            for (Iterator it = scan.forUses(stmt); it.hasNext(); ) {
              int use = rn.index((LirNode)it.next());
              if (use != 0) {
                for (NumberSet.Iterator is = live.iterator(); is.hasNext(); )
                  setDisturb(use, is.next());
              }
            }
          }

          if (clobberAfterDef) {
            work.clear();
            for (Iterator it = scan.forClobbers(stmt); it.hasNext(); ) {
              int def = rn.index((LirNode)it.next());
              if (def != 0) {
                work.add(def);
                for (NumberSet.Iterator is = live.iterator(); is.hasNext(); ) {
                  int vliv = is.next();
                  setInterfere(def, vliv);
                  setDisturb(def, vliv);
                }
              }
            }
            live.removeAll(work);          
          }

          work.clear();
          for (Iterator it = scan.forDefs(stmt); it.hasNext(); ) {
            int def = rn.index((LirNode)it.next());
            if (def != 0) {
              work.add(def);
              for (NumberSet.Iterator is = live.iterator(); is.hasNext(); ) {
                int vliv = is.next();
                setInterfere(def, vliv);
                setDisturb(def, vliv);
              }
            }
          }
          live.removeAll(work);

          if (!clobberAfterDef) {
            work.clear();
            for (Iterator it = scan.forClobbers(stmt); it.hasNext(); ) {
              int def = rn.index((LirNode)it.next());
              if (def != 0) {
                work.add(def);
                for (NumberSet.Iterator is = live.iterator(); is.hasNext(); ) {
                  int vliv = is.next();
                  setInterfere(def, vliv);
                  setDisturb(def, vliv);
                }
              }
            }
            live.removeAll(work);
          }

          if (!useAfterDef) {
            for (Iterator it = scan.forUses(stmt); it.hasNext(); ) {
              int use = rn.index((LirNode)it.next());
              if (use != 0)
                live.add(use);
            }
            for (Iterator it = scan.forUses(stmt); it.hasNext(); ) {
              int use = rn.index((LirNode)it.next());
              if (use != 0) {
                for (NumberSet.Iterator is = live.iterator(); is.hasNext(); )
                  setDisturb(use, is.next());
              }
            }
          }

        }
      }
    }
  }




  /** Return true if stmt is copy register to register */
  private boolean isCopyStatement(LirNode stmt) {
    return (stmt.opCode == Op.SET
            && stmt.kid(0).opCode == Op.REG
            && stmt.kid(1).opCode == Op.REG);
  }



  /** Add new edge (x, y) to IG. Return true if it's new. **/
  public boolean setInterfere(Symbol x, Symbol y) {
    return setInterfere(rn.index(x), rn.index(y));
  }

  public boolean setInterfere(int x, int y) {
    if (x >= 0 && y >= 0 && x != y) {
      if (UseAdjMatrix) {
        if (x < y) { int w = x; x = y; y = w; }
        if (!adjMatrix.contains(x * (x - 1) / 2 + y)) {
          adjMatrix.add(x * (x - 1) / 2 + y);
          interfereSets[x].add(y);
          interfereSets[y].add(x);
          return true;
        }
      } else {
        if (!interfereSets[x].contains(y)) {
          interfereSets[x].add(y);
          interfereSets[y].add(x);
          return true;
        }
      }
    }
    return false;
  }

  public boolean unsetInterfere(int x, int y) {
    if (x >= 0 && y >= 0 && x != y) {
      if (UseAdjMatrix) {
        if (x < y) { int w = x; x = y; y = w; }
        if (adjMatrix.contains(x * (x - 1) / 2 + y)) {
          adjMatrix.remove(x * (x - 1) / 2 + y);
          interfereSets[x].remove(y);
          interfereSets[y].remove(x);
          return true;
        }
      } else {
        if (interfereSets[x].contains(y)) {
          interfereSets[x].remove(y);
          interfereSets[y].remove(x);
          return true;
        }
      }
    }
    return false;
  }

  public void setDisturb(Symbol x, Symbol y) {
    setDisturb(rn.index(x), rn.index(y));
  }

  public void setDisturb(int x, int y) {
    if (x >= 0 && y >= 0 && x != y
        && !disturbSets[y].contains(x)) {
      disturbSets[y].add(x);
    }
  }

  /** Return true if register x interferes register y.
   *  Names of register variables are index numbers. **/
  public boolean interfere(int x, int y) {
    if (x == y)
      return false;
    if (UseAdjMatrix) {
      if (x < y) { int w = x; x = y; y = w; }
      return adjMatrix.contains(x * (x - 1) / 2 + y);
    } else {
      return interfereSets[x].contains(y);
    }
  }

  
  /** Return true if register x interferes register y **/
  public boolean interfere(Symbol x, Symbol y) {
    return interfere(rn.index(x), rn.index(y));
  }

  /** Return the list of variables interfering x. **/
  public NumberSet interfereSet(int x) {
    return interfereSets[x];
  }

  /** Return the list of variables interfering x. **/
  public NumberSet interfereSet(Symbol x) {
    return interfereSets[rn.index(x)];
  }

  /** Return the set of variables disturbing x. **/
  public NumberSet disturbSet(int x) {
    return disturbSets[x];
  }

  /** Return the set of variables disturbing x. **/
  public NumberSet disturbSet(Symbol x) {
    return disturbSets[rn.index(x)];
  }

  /** Return disturbing factor of x. **/
  public int disturbingFactor(int x) {
    int n = 0;
    for (int i = 0; i < disturbSets.length; i++) {
      if (disturbSets[i].contains(x))
        n++;
    }
    return n;
  }

  /** Return disturbed factor of x. **/
  public int disturbedFactor(int x) {
    return disturbSets[x].size();
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

    int n = rn.nRegvars();
    int pn = rn.nPhyRegs();
    int[] v = new int[n];
    
    output.println();
    output.println("Interference Graph:");
    int popul = 0;
    for (int i = 1; i < n; i++) {
      int m = interfereSets[i].size();
      if (i >= pn || m != 0) {
        output.print(rn.toString(i) + ":");
        popul += m;
        interfereSets[i].toArray(v);
        Misc.sort(v, m);
        for (int j = 0; j < m; j++)
          output.print(" " + rn.toString(v[j]));
        if (false && interfereSets[i] instanceof HashNumberSet) {
          int size = ((HashNumberSet)interfereSets[i]).tableSize();
          output.print(" (tablesize: " + size + " " + (size * 100)/n + "%)");
        }
        output.println();
      }
    }
    output.println("IG Total entry: " + popul + ", "
                   + (popul*100)/(n*n) + "%");

    output.println();
    output.println("Disturbance Graph:");
    popul = 0;
    for (int i = 1; i < n; i++) {
      int m = disturbSets[i].size();
      if (i >= pn || m != 0) {
        output.print(rn.toString(i) + ":");
        popul += m;
        disturbSets[i].toArray(v);
        Misc.sort(v, m);
        for (int j = 0; j < m; j++)
          output.print(" " + rn.toString(v[j]));
        if (false && interfereSets[i] instanceof HashNumberSet) {
          int size = ((HashNumberSet)interfereSets[i]).tableSize();
          output.print(" (tablesize: " + size + " " + (size * 100)/n + "%)");
        }
        output.println();
      }
    }
    output.println("DG Total entry: " + popul + ", "
                   + (popul*100)/(n*n) + "%");
  }

}
