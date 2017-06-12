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
import coins.backend.util.BiList;
import coins.backend.util.UnionFind;
import java.io.PrintWriter;


/** Detect loop structures in the flow graph. <br>
 *   Algorithm used is taken from:<br>
 *    R. Endre Tarjan, "Testing Flow Graph Reducibility", 
 *    Journal of Computer and System Sciences 9, 355-365 (1974). */
public class LoopAnalysis implements LocalAnalysis {

  /** Factory class of Dominators. */
  private static class Analyzer implements LocalAnalyzer {
    public LocalAnalysis doIt(Function func) {
      return new LoopAnalysis(func);
    }

    public String name() { return "LoopAnalysis"; }
  }

  public static final Analyzer analyzer = new Analyzer();

  /** isLoop[i] is true if basic block <code>i</code> is a loop entry. **/
  public final boolean[] isLoop;

  /** loopHeader[i] is a entry block of basic block <code>i</code>. **/
  public final BasicBlk[] loopHeader;

  /** isLoop[i] is true if the loop beginning at basic block <code>i</code>
   *   has multiple entries (i.e. irreducible). **/
  public final boolean[] multiEntry;

  /** hasExii[i] is true if the loop beginning at basic block <code>i</code>
   *   has exit. **/
  public final boolean[] hasExit;

  /** nestLevel[i] is depth of the loop beginning at basic block <code>i</code>. */
  public final int[] nestLevel;

  /** Children of blocks in the loop tree. **/
  public final BiList[] kids;

  /** Function **/
  private Function func;

  /** Flow graph analyzed. */
  private FlowGraph flowGraph;

  /** Depth First Spanning Tree **/
  private DFST dfst;

  private int timeStamp;


  /** Find loops in the function. */
  private LoopAnalysis(Function func) {
    this.func = func;
    flowGraph = func.flowGraph();
    timeStamp = flowGraph.timeStamp();
    dfst = (DFST)func.require(DFST.analyzer);

    int n = dfst.maxDfn;
    UnionFind uf = new UnionFind(n + 1);
    int[] name = new int[n + 1];

    int[] added = new int[n + 1];
    int[] body = new int[n + 1];
    int[] worklist = new int[n + 1];

    int nBlks = flowGraph.idBound();
    loopHeader = new BasicBlk[nBlks];
    isLoop = new boolean[nBlks];
    multiEntry = new boolean[nBlks];
    hasExit = new boolean[nBlks];
    nestLevel = new int[nBlks];
    kids = new BiList[nBlks];

    BasicBlk[] v = dfst.blkVectorByPre();

    for (int w = 1; w <= n; w++) {
      name[w] = w;
      added[w] = 0;
    }

    /* For each w in g (reverse preorder) */
    for (int w = n; w >= 1; w--) {
      /* collect nodes x where x->w is a retreating edge */
      int nb = 0, nw = 0;
      for (BiLink s = v[w].predList().first(); !s.atEnd(); s = s.next()) {
        int x = dfst.dfnPre[((BasicBlk)s.elem()).id];
        if (x == 0) continue;

        /* is a retreating edge? */
        if (dfst.dfn[v[x].id] >= dfst.dfn[v[w].id]) {
          int xx = name[uf.find(x)];
          if (added[xx] != w) {
            worklist[nw++] = body[nb++] = xx;
            added[xx] = w;
          }
	}
      }

      if (nw > 0) {
        /* w is a loop header. */
        isLoop[v[w].id] = true;

        while (nw > 0) {
          int x = worklist[--nw];
          for (BiLink s = v[x].predList().first(); !s.atEnd(); s = s.next()) {
            int y = dfst.dfnPre[((BasicBlk)s.elem()).id];
            if (y == 0) continue;
            /* is y->x tree/advancing/cross edge? */
            if (dfst.dfn[v[y].id] < dfst.dfn[v[x].id]) {
              int yy = name[uf.find(y)];
              /* is yy a descendant of w? */
              if (w <= yy && dfst.dfn[v[w].id] <= dfst.dfn[v[yy].id]) {
                if (added[yy] != w && yy != w) {
                  worklist[nw++] = body[nb++] = yy;
                  added[yy] = w;
                }
              } else {
                /* yy is out of loop; so x is an entry of the loop. */
                if (x != w)
                  multiEntry[v[w].id] = true; /* another entry, irreducible */
              }
            }
          }
        }

        for (int i = 0; i < nb; i++) {
          int x = body[i];
          if (x != w)
            loopHeader[v[x].id] = v[w];
          name[uf.union(x, w)] = w;
        }


        /* Test if this loop has exit. */

        boolean itHas = false;

        /* has w an edge to outside of the loop? */
        for (BiLink s = v[w].succList().first(); !s.atEnd(); s = s.next()) {
          int y = dfst.dfnPre[((BasicBlk)s.elem()).id];
          if (name[uf.find(y)] != w) {
            itHas = true;
            break;
          }
        }

        while (nb > 0 && !itHas) {
          int x = body[--nb];
          
          /* has x an edge to outside of the loop? */
          for (BiLink s = v[x].succList().first(); !s.atEnd(); s = s.next()) {
            int y = dfst.dfnPre[((BasicBlk)s.elem()).id];
            if (name[uf.find(y)] != w) {
              itHas = true;
              break;
            }
          }
        }
        hasExit[v[w].id] = itHas;
      }
    }


    // Construct loop tree and count nest level.
    kids[0] = new BiList();
    for (int w = 1; w <= n; w++) {
      BasicBlk blk = v[w];
      if (isLoop[blk.id] && kids[blk.id] == null)
        kids[blk.id] = new BiList();
      BasicBlk parent = loopHeader[blk.id];
      if (parent != null) {
        kids[parent.id].add(blk);
        nestLevel[blk.id] = nestLevel[parent.id] + 1;
      }
      if (isLoop[blk.id]) {
        if (parent == null)
          kids[0].add(blk); // collect toplevel loops in kids[0].
        nestLevel[blk.id]++;
      }
    }

  }


  
  /** Return true if this analysis is up to date. */
  public boolean isUpToDate() {
    return (timeStamp == flowGraph.timeStamp());
  }




  /** Print loop structure (OBSOLETED). */
  public void printIt(PrintWriter out) { printAfterFunction(out); }

  /** Debug print entries required by interface. **/
  public void printBeforeFunction(PrintWriter output) {}
  public void printBeforeBlock(BasicBlk blk, PrintWriter output) {}
  public void printAfterBlock(BasicBlk blk, PrintWriter output) {}
  public void printBeforeStmt(LirNode stmt, PrintWriter output) {}
  public void printAfterStmt(LirNode stmt, PrintWriter output) {}

  /** Print loop structure. */
  public void printAfterFunction(PrintWriter out) {
    BasicBlk[] v = dfst.blkVectorByPre();
    int n = dfst.maxDfn;

    out.println("Loop Structure:");
    for (int i = 1; i <= n; i++) {
      if (isLoop[v[i].id]) {
        for (int k = 0; k < nestLevel[v[i].id]; k++)
          out.print("  ");
        out.print("Loop starts at #" + v[i].id + ": ");
        boolean first = true;
        for (int j = i; j <= n; j++) {
          if (loopHeader[v[j].id] != null && loopHeader[v[j].id].id == v[i].id) {
            if (!first) out.print(",");
            out.print("#" + v[j].id);
            first = false;
          }
        }
        if (multiEntry[v[i].id])
          out.print(" (multi)");
        if (!hasExit[v[i].id])
          out.print(" (infinite)");
        out.println();
      }
    }

    out.println();
    out.println("Loop Structure:");
    for (BiLink p = kids[0].first(); !p.atEnd(); p = p.next()) {
      BasicBlk blk = (BasicBlk)p.elem();
      printLoopTree(blk, out);
    }
  }

  private void printLoopTree(BasicBlk loop, PrintWriter out) {
    for (int i = 0; i < nestLevel[loop.id]; i++)
      out.print("  ");
    out.print("#" + loop.id);
    if (multiEntry[loop.id])
      out.print(" (multi)");
    if (!hasExit[loop.id])
      out.print(" (infinite)");
    out.print(":");
    String prefix = " ";
    for (BiLink p = kids[loop.id].first(); !p.atEnd(); p = p.next()) {
      BasicBlk blk = (BasicBlk)p.elem();
      out.print(prefix + "#" + blk.id);
      prefix = ",";
    }
    out.println();
    for (BiLink p = kids[loop.id].first(); !p.atEnd(); p = p.next()) {
      BasicBlk blk = (BasicBlk)p.elem();
      if (isLoop[blk.id])
        printLoopTree(blk, out);
    }
  }
}
