/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.cfg;

import coins.backend.Function;
import coins.backend.LocalAnalysis;
import coins.backend.Op;
import coins.backend.Type;
import coins.backend.lir.LirLabelRef;
import coins.backend.lir.LirNode;
import coins.backend.sym.Label;
import coins.backend.util.BiLink;
import coins.backend.util.BiList;
import coins.backend.util.ImList;
import coins.backend.util.QuotedString;
import java.io.PrintWriter;
import java.util.Iterator;


/** Represent Control Flow Graph, a directed graph whose
 *   nodes are basic blocks. */
public class FlowGraph {
  /** Function owning this CFG. */
  public final Function function;

  /** List of basic blocks. */
  public final BiList basicBlkList = new BiList();

  /** Identifier of the next allocated block */
  private int blkCounter = 1;

  /** Maximum number of DFN */
  private int maxDfn;

  /** Last modified time but not real time-of-the-day. Incremented
   * when the graph touched. */
  private int timeStamp = 0;

  /** Time stamp of DFST computation. */
  private int dfstTimeStamp = 0;

  /** Entry Basic Block **/
  private BasicBlk entryBlk;

  /** Exit Basic Block **/
  private BasicBlk exitBlk;

  /** Split LIR instruction list into basic blocks
   *   and build up CFG. */
  public FlowGraph(Function f, BiList anInstrList) {
    function = f;

    // Partition instructions into basic blocks.
    boolean prevJump = false;
    boolean prevLabel = false;
    BiLink left;
    for (BiLink ptr = anInstrList.first(); !ptr.atEnd(); ptr = left) {
      left = ptr.next();
      LirNode node = (LirNode)ptr.elem();
      // node.setRoot(true);
      if (ptr != anInstrList.first()
          && (prevJump || (node.opCode == Op.DEFLABEL && !prevLabel))) {
        BiList fragment = anInstrList;
        anInstrList = anInstrList.split(ptr);
        if (!prevJump) {
          // add explicit JUMP instruction goes to next block
          
          fragment.add(function.newLir.operator(Op.JUMP, Type.UNKNOWN,
                                                node.kid(0), null));
        }
        BasicBlk newBlk = newBasicBlk(fragment); 
        if (((LirNode)fragment.first().elem()).opCode == Op.EPILOGUE)
          exitBlk = newBlk;
        basicBlkList.add(newBlk);
      }
      if (node.opCode == Op.DEFLABEL) {
        prevLabel = true;
        prevJump = false;
      } else if (node.isBranch()) {
        prevJump = true;
        prevLabel = false;
      } else {
        prevLabel = prevJump = false;
      }
    }
    if (!anInstrList.isEmpty()) {
      // last fragment
      BasicBlk newBlk = newBasicBlk(anInstrList); 
      if (((LirNode)anInstrList.first().elem()).opCode == Op.EPILOGUE)
          exitBlk = newBlk;
      basicBlkList.add(newBlk);
    }

    entryBlk = (BasicBlk)basicBlkList.first().elem();

    // Add edges
    for (BiLink bp = basicBlkList.first(); !bp.atEnd(); bp = bp.next()) {
      BasicBlk blk = (BasicBlk)bp.elem();
      blk.maintEdges();
    }

    // Unify Labels
    for (BiLink bp = basicBlkList.first(); !bp.atEnd(); bp = bp.next()) {
      BasicBlk blk = (BasicBlk)bp.elem();

      if (!blk.instrList().isEmpty()) {
        LirNode ins = (LirNode)blk.instrList().last().elem();
        unifyLabels(ins);
      }
    }

    touch();
    dfstOrder();
  }

  private void unifyLabels(LirNode ins) {
    int n = ins.nKids();
    for (int i = 0; i < n; i++) {
      if (ins.kid(i) instanceof LirLabelRef) {
        Label x = ((LirLabelRef)ins.kid(i)).label;
        if (x.basicBlk().label() != x)
          ins.setKid(i, function.newLir.labelRef(x.basicBlk().label()));
      } else {
        unifyLabels(ins.kid(i));
      }
    }
  }


  /** Return last modified time of the graph. Time is just a counter
      rather than actual time-of-the-day. */
  public int timeStamp() { return timeStamp; }

  /** Notify that the graph has been modified. */
  public void touch() {
    timeStamp++;
    function.touch();
  }

  /** Create new basic block with instruction list <code>instr</code>. */
  BasicBlk newBasicBlk(BiList instr) {
    return new BasicBlk(this, blkCounter++, instr);
  }


  /** Insert new empty basic block before block x. */
  public BasicBlk insertNewBlkBefore(BasicBlk x) {
    BiList list = new BiList();
    BasicBlk blk = newBasicBlk(list);
    list.add(function.newLir.operator(Op.JUMP, Type.UNKNOWN,
                                      function.newLir.labelRef(x.label()),
                                      null) );
    basicBlkList.locate(x).addBefore(blk);
    blk.maintEdges();
    return blk;
  }


  /** Return maximum block numer + 1. */
  public int idBound() { return blkCounter; }
  

  /** Make Depth First Spanning Tree. Fields dfn(Depth First Number in
   *  rever postorder), dfnPre(preorder), parent are set.
   * @deprecated use DFST class instead. */
  public void dfstOrder() { dfstOrderHook(null); }

  /** Depth First Ordering
   * @deprecated use DFST class instead. */
  public void dfstOrderHook(DfstHook h) {
    // Has DFST been made already?
    if (dfstTimeStamp >= timeStamp)
      return;

    dfstTimeStamp = timeStamp;

    for (BiLink p = basicBlkList.first(); !p.atEnd(); p = p.next()) {
      BasicBlk x = (BasicBlk)p.elem();
      x.dfn = x.dfnPre = 0;
      x.parent = null;
    }

    int[] cpre = new int[1];
    int[] crpost = new int[1];
    entryBlk().depthFirstSearch(h, null, cpre, crpost);
    maxDfn = crpost[0];

    for (BiLink p = basicBlkList.first(); !p.atEnd(); p = p.next()) {
      BasicBlk x = (BasicBlk)p.elem();
      if (x.dfn != 0)
        x.dfn = crpost[0] - x.dfn + 1;
    }
  }

  /** Return maximum number of DFN (depth first number).
   * @deprecated use DFST class instead. */
  public int maxDfn() { return maxDfn; }

  /** Return the vector of basic block indexed by DFN reverse
   * postorder. Zeroth element is null, entry block is in [1].
   * @deprecated use DFST class instead. */
  public BasicBlk[] blkVectorByRPost() {
    dfstOrder();
    BasicBlk[] vec = new BasicBlk[maxDfn + 1];
    for (BiLink p = basicBlkList.first(); !p.atEnd(); p = p.next()) {
      BasicBlk blk = (BasicBlk)p.elem();
      if (blk.dfn != 0)
        vec[blk.dfn] = blk;
    }
    return vec;
  }

  /** Return the vector of basic block indexed by DFN preorder.
   * Zeroth element is null, entry block is in [1].
   * @deprecated use DFST class instead. */
  public BasicBlk[] blkVectorByPre() {
    dfstOrder();
    BasicBlk[] vec = new BasicBlk[maxDfn + 1];
    for (BiLink p = basicBlkList.first(); !p.atEnd(); p = p.next()) {
      BasicBlk blk = (BasicBlk)p.elem();
      if (blk.dfnPre != 0)
        vec[blk.dfnPre] = blk;
    }
    return vec;
  }

  /** Return the entry basic block. */
  public BasicBlk entryBlk() { return entryBlk; }

  /** Return the exit basic block. */
  public BasicBlk exitBlk() { return exitBlk; }

  /** Return the list of basic blocks. */
  // public BiList basicBlkList() { return basicBlkList; }

  /** Return iterator for accessing basic blocks. */
  public Iterator basicBlkIterator() { return basicBlkList.iterator(); }


  /** Convert to external LIR format. **/
  public Object toSexp() {
    ImList list = ImList.Empty;
    for (BiLink bp = basicBlkList.first(); !bp.atEnd(); bp = bp.next()) {
      BasicBlk blk = (BasicBlk)bp.elem();
      // concatenate sublist
      list = ((ImList)blk.toSexp()).destructiveReverse(list);
    }
    return list.destructiveReverse();
  }


  /** Print standard form */
  public void printStandardForm(PrintWriter output, String indent) {
    for (BiLink bp = basicBlkList.first(); !bp.atEnd(); bp = bp.next()) {
      BasicBlk blk = (BasicBlk)bp.elem();
      blk.printStandardForm(output, indent);
    }
  }

  /** Print CFG */
  public void printIt(PrintWriter output) {
    dfstOrder();
    for (BiLink bp = basicBlkList.first(); !bp.atEnd(); bp = bp.next()) {
      BasicBlk blk = (BasicBlk)bp.elem();
      blk.printIt(output);
    }
  }

  /** Print CFG with callback */
  public void printIt(PrintWriter output, LocalAnalysis anals[]) {
    dfstOrder();
    for (BiLink bp = basicBlkList.first(); !bp.atEnd(); bp = bp.next()) {
      BasicBlk blk = (BasicBlk)bp.elem();
      blk.printIt(output, anals);
    }
  }

}
