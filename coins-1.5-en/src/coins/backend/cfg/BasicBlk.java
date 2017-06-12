/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.cfg;

import coins.backend.LocalAnalysis;
import coins.backend.Op;
import coins.backend.lir.LirLabelRef;
import coins.backend.lir.LirNode;
import coins.backend.sym.Label;
import coins.backend.util.BiLink;
import coins.backend.util.BiList;
import coins.backend.util.ImList;
import coins.backend.util.QuotedString;
import java.io.PrintWriter;


/** Represent basic block, a sequence of LIR instructions
 * without intervening JUMPs. */
public class BasicBlk {
  /** FlowGraph owning this block. */
  public final FlowGraph flowGraph;

  /** Identifier of this block. */
  public final int id;

  /** Instructions list */
  private BiList instrList;

  /** Successors of this block. */
  private BiList succList = new BiList();

  /** Predecessors of this basic block. */
  private BiList predList = new BiList();

  /** Dummy successor list (for postdominance/control dependece) **/
  private BiList dummySuccList = new BiList();

  /** Dummy predecessor list (for postdominance/control dependece) **/
  private BiList dummyPredList = new BiList();

  /** DFST Reverse postorder number */
  int dfn;

  /** DFST Preorder number */
  int dfnPre;

  /** Parent node in the DFST. */
  BasicBlk parent;

  /** This block's label */
  private Label label;

  /** Create basic block with the fragment of instruction list.
   *  Called only by FlowGraph. */
  BasicBlk(FlowGraph g, int idnum, BiList instr) {
    flowGraph = g;
    id = idnum;
    instrList = instr;

    // Let labels point to this block
    for (BiLink insp = instrList.first(); !insp.atEnd();) {
      LirNode node = (LirNode)insp.elem();
      BiLink next = insp.next();
      if (node.opCode == Op.DEFLABEL) {
        ((LirLabelRef)node.kid(0)).label.setBasicBlk(this);
        if (label == null)
          label = ((LirLabelRef)node.kid(0)).label;
        // Delete DEFLABEL ops.
        insp.unlink();
      }
      insp = next;
    }
    // If no labels are shown, add arbitrary label
    if (label == null) {
      Label label = g.function.newLabel();
      label.setBasicBlk(this);
      setLabel(label);
    }
  }

  /** Return instruction list */
  public BiList instrList() { return instrList; }

  /** Replace instruction list */
  public void setInstrList(BiList list) { instrList = list; }

  /** Return label of this block */
  public Label label() { return label; }

  /** Replace label */
  public void setLabel(Label l) { label = l; }

  /** Return list of successors */
  public BiList succList() { return succList; }

  /** Return list of predecessors */
  public BiList predList() { return predList; }

  /** Return list of dummy successors. */
  public BiList dummySuccList() { return dummySuccList; }

  /** Return list of dummy predecessors. */
  public BiList dummyPredList() { return dummyPredList; }

  /**
   * @deprecated	use DFST class instead.
   */
  public int dfn() { return dfn; }

  /**
   * @deprecated	use DFST class instead.
   */
  public int dfnPre() { return dfnPre; }

  /**
   * @deprecated	use DFST class instead.
   */
  public BasicBlk parent() { return parent; }

  /**
   * @deprecated	use DFST class instead.
   */
  public boolean isAncestorOf(BasicBlk x) {
    return (dfnPre <= x.dfnPre && dfn <= x.dfn);
  }

  /**
   * @deprecated	use DFST class instead.
   */
  public boolean isDescendantOf(BasicBlk x) {
    return (x.dfnPre <= dfnPre && x.dfn <= dfn);
  }

  /** Add an edge from this block to toBlk */
  public void addEdge(BasicBlk toBlk) {
    succList.addNew(toBlk);
    toBlk.predList.addNew(this);
  }

  /** Remove the edge from this block to toBlk */
  public void removeEdge(BasicBlk toBlk) {
    succList.remove(toBlk);
    toBlk.predList.remove(this);
  }

  /** Add an dummy edge from this block to toBlk */
  public void addDummyEdge(BasicBlk toBlk) {
    dummySuccList.addNew(toBlk);
    toBlk.dummyPredList.addNew(this);
  }

  /** Remove the dummy edge from this block to toBlk */
  public void removeDummyEdge(BasicBlk toBlk) {
    dummySuccList.remove(toBlk);
    toBlk.dummyPredList.remove(this);
  }

  /** Remove all edges from this block */
  public void clearEdges() {
    for (BiLink p = succList.first(); !p.atEnd(); ) {
      BasicBlk target = (BasicBlk)p.elem();
      p = p.next();
      target.predList.remove(this);
    }
    succList.clear();
  }

  /** Remove all dummy edges from this block */
  public void clearDummyEdges() {
    for (BiLink p = dummySuccList.first(); !p.atEnd(); ) {
      BasicBlk target = (BasicBlk)p.elem();
      p = p.next();
      target.dummyPredList.remove(this);
    }
    dummySuccList.clear();
  }

  /** Maintain edges from this block.
   * Automatically checks JUMP(/C/N) instructions  */
  public void maintEdges() {
    flowGraph.touch();
    clearEdges();

    // check last instruction
    if (!instrList.isEmpty()) {
      Label[] targets = ((LirNode)instrList.last().elem()).getTargets();
      if (targets != null) {
        for (int i = 0; i < targets.length; i++)
          addEdge(targets[i].basicBlk());
      }
    }
  }

  /** Change successor x to y. If do not have x, throw Error */
  public void replaceSucc(BasicBlk x, BasicBlk y) {
    LirNode jumpOp = (LirNode)instrList.last().elem();
    jumpOp.replaceLabel(x.label, y.label, flowGraph.function.newLir);
    maintEdges();
  }

  /** Depth First Search */
  void depthFirstSearch(DfstHook h, BasicBlk from, int[] cpre, int[] crpost) {
    if (h != null)
      h.preOrder(this, from);
    dfnPre = ++cpre[0];
    parent = from;
    for (BiLink p = succList.first(); !p.atEnd(); p = p.next()) {
      BasicBlk y = (BasicBlk)p.elem();
      if (y.dfnPre == 0)
        y.depthFirstSearch(h, this, cpre, crpost);
    }
    if (h != null)
      h.postOrder(this);
    dfn = ++crpost[0];
  }

  /** Convert to external LIR format. **/
  public Object toSexp() {
    ImList list = ImList.Empty;
    if (flowGraph.entryBlk() != this)
      list = new ImList
        (ImList.list("DEFLABEL", new QuotedString(label.toString())), list);
    for (BiLink insp = instrList.first(); !insp.atEnd(); insp = insp.next()) {
      LirNode node = (LirNode)insp.elem();
      list = new ImList(node.toSexp(), list);
    }
    return list.destructiveReverse();
  }

    
  /** Print this block in standard form */
  public void printStandardForm(PrintWriter output, String indent) {
    // omit label if first block
    if (flowGraph.entryBlk() != this)
      output.println(indent + "(DEFLABEL \"" + label + "\")");
    for (BiLink insp = instrList.first(); !insp.atEnd(); insp = insp.next()) {
      LirNode node = (LirNode)insp.elem();
      output.println(indent + node.toString());
    }
  }

  private static final LocalAnalysis[] emptyAnares = new LocalAnalysis[0];

  /** Print this block */
  public void printIt(PrintWriter output) {
    printIt(output, emptyAnares);
  }

  /** Print this block with callback */
  public void printIt(PrintWriter output, LocalAnalysis anals[]) {
    output.println();
    output.print("  #" + id + " Basic Block");
    if (label != null)
      output.print(" (" + label + ")");
    output.print(": DFN=(" + dfnPre + "," + dfn + "),");
    if (parent != null)
      output.print(" parent=#" + parent.id + ",");
    output.print(" <-(");
    boolean middle = false;
    for (BiLink blkp = predList.first(); !blkp.atEnd(); blkp = blkp.next()) {
      BasicBlk blk = (BasicBlk)blkp.elem();
      output.print((middle ? ",#" : "#") + blk.id);
      middle = true;
    }
    for (BiLink blkp = dummyPredList.first(); !blkp.atEnd(); blkp = blkp.next()) {
      BasicBlk blk = (BasicBlk)blkp.elem();
      output.print((middle ? ",*#" : "*#") + blk.id);
      middle = true;
    }
    output.print(") ->(");
    middle = false;
    for (BiLink blkp = succList.first(); !blkp.atEnd(); blkp = blkp.next()) {
      BasicBlk blk = (BasicBlk)blkp.elem();
      output.print((middle ? ",#" : "#") + blk.id);
      middle = true;
    }
    for (BiLink blkp = dummySuccList.first(); !blkp.atEnd(); blkp = blkp.next()) {
      BasicBlk blk = (BasicBlk)blkp.elem();
      output.print((middle ? ",*#" : "*#") + blk.id);
      middle = true;
    }
    output.println(")");

    for (int i = 0; i < anals.length; i++)
      anals[i].printBeforeBlock(this, output);

    for (BiLink insp = instrList.first(); !insp.atEnd(); insp = insp.next()) {
      LirNode node = (LirNode)insp.elem();
      for (int i = 0; i < anals.length; i++)
        anals[i].printBeforeStmt(node, output);
      output.print("    ");
      output.println(node.toString());
      for (int i = 0; i < anals.length; i++)
        anals[i].printAfterStmt(node, output);
    }

    for (int i = 0; i < anals.length; i++)
      anals[i].printAfterBlock(this, output);
  }

}
