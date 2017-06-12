/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.opt;

import coins.backend.Data;
import coins.backend.Function;
import coins.backend.LocalTransformer;
import coins.backend.Op;
import coins.backend.Root;
import coins.backend.Storage;
import coins.backend.Type;
import coins.backend.ana.DominanceFrontiers;
import coins.backend.ana.Dominators;
import coins.backend.ana.EnumRegVars;
import coins.backend.ana.LiveVariableAnalysis;
import coins.backend.ana.LiveVariableSlotwise;
import coins.backend.ana.ScanVarReference;
import coins.backend.cfg.BasicBlk;
import coins.backend.cfg.FlowGraph;
import coins.backend.lir.LirNode;
import coins.backend.lir.LirSymRef;
import coins.backend.sym.Symbol;
import coins.backend.util.BiLink;
import coins.backend.util.ImList;
import java.util.Stack;


/** Transform into SSA (Static Single Assignment) form. */
public class Ssa {

  /** Trigger class for pruned SSA. **/
  private static class SsaTrigger implements LocalTransformer {

    /** Type of SSA. **/
    private String type;

    /** Return SSA type. **/
    SsaTrigger(String type) {
      this.type = type;
    }

    public boolean doIt(Function func, ImList args) {
      (new Ssa(type == "pruned")).doIt(func, args);
      return true;
    }

    public boolean doIt(Data data, ImList args) { return true; }

    public String name() { return "Ssa(" + type + ")"; }

    public String subject() { return "Conversion to " + type + " SSA Form"; }
  }


  /** Return transformer object. **/
  public static LocalTransformer trigger(String type) {
    return new SsaTrigger(type);
  }


  private Root root;

  private Function function;
  private Dominators dom;

  private boolean pruned;

  /** Create a transformer to SSA form. **/
  public Ssa(boolean pruned) {
    this.pruned = pruned;
  }

  /** Create a transformer which converts a function to Minimal SSA form. **/
  public static Ssa minimal() {
    return new Ssa(false);
  }

  /** Create a transformer which converts a function to Pruned SSA form. **/
  public static Ssa pruned() {
    return new Ssa(true);
  }


  private EnumRegVars rn;
  private boolean[] dojob;

  private static final boolean keepOriginalSymbol = false;

  public void doIt(Function f) { doIt(f, null); }

  /** Rewrite function f to SSA form. */
  public void doIt(Function f, ImList args) {
    function = f;
    root = f.root;
    FlowGraph flowGraph = f.flowGraph();
    DominanceFrontiers df = (DominanceFrontiers)f.require(DominanceFrontiers.analyzer);
    dom = (Dominators)f.require(Dominators.analyzer);

    rn = (EnumRegVars)f.require(EnumRegVars.analyzer);
    ScanVarReference sc = (ScanVarReference)f.require(ScanVarReference.analyzer);
    int nRegvars = rn.nRegvars();

    // Flags for variables
    dojob = new boolean[nRegvars];
    if (args != null && !args.atEnd()) {
      for (ImList p = args; !p.atEnd(); p = p.next()) {
        Symbol var = (Symbol)p.elem();
        dojob[rn.index(var)] = true;
      }
    } else {
      for (int i = 1; i < nRegvars; i++)
        dojob[i] = true;
    }


    LiveVariableAnalysis live = null;
    if (pruned)
      live = (LiveVariableAnalysis)f.require(LiveVariableSlotwise.analyzer);

    if (root.traceOK("Ssa", 2))
      dom.printIt(root.debOut);

    int insBound = f.newLir.idBound();

    // Collect assignments to variables.


    // Insert PHI instructions.
    BasicBlk[] worklist = new BasicBlk[insBound];
    int[] inWork = new int[insBound];
    int[] hasAlready = new int[insBound];
    int nwork = 0;
    int iterCount = 0;
    for (int i = 1; i < nRegvars; i++) {
      if (sc.useSites[i] == null || sc.defSites[i] == null || !dojob[i])
        continue;

      iterCount++;
      for (BiLink p = sc.defSites[i].first(); !p.atEnd(); p = p.next()) {
        BasicBlk blk = (BasicBlk)p.elem();
        inWork[blk.id] = iterCount;
        worklist[nwork++] = blk;
      }
      while (nwork > 0) {
        BasicBlk blk = worklist[--nwork];
        for (BiLink q = df.frontiers[blk.id].first(); !q.atEnd(); q = q.next()) {
          BasicBlk front = (BasicBlk)q.elem();
          if (hasAlready[front.id] < iterCount
              && (!pruned || live.isLiveAtEntry(i, front))) {
            // insert PHI at front
            int nPreds = front.predList().length() + 1;
            LirNode[] operand = new LirNode[nPreds];
            operand[0] = function.newLir.symRef(rn.toSymbol(i));
            for (int j = 1; j < nPreds; j++)
              operand[j] = operand[0];
            front.instrList().addFirst(f.newLir.operator(Op.PHI,
                                                         rn.toSymbol(i).type,
                                                         operand, null));
            hasAlready[front.id] = iterCount;
            if (inWork[front.id] < iterCount) {
              inWork[front.id] = iterCount;
              worklist[nwork++] = front;
            }
          }
        }
      }
    }

    if (root.traceOK("Ssa", 2)) {
      root.debOut.println("After PHI insertion:");
      flowGraph.printIt(root.debOut);
    }

    // Rename variable references.
    // LirNode undefinedValue = function.newLir
    //   .operator(Op.UNDEFINED, Type.UNKNOWN, new LirNode[0], null);
    int[] nextVar = new int[nRegvars];
    LirNode[] curVar = new LirNode[nRegvars];
    // for (int i = 1; i < nRegvars; i++)
    //   curVar[i] = undefinedValue;
    for (int i = 1; i < nRegvars; i++)
      curVar[i] = function.newLir.symRef(rn.toSymbol(i));

    renameInBlock(new Stack(), flowGraph.entryBlk(), nextVar, curVar);

    function.touch();
    function.setForm(Function.FORM_SSA);
  }


  private static class Pair {
    Symbol orig;
    LirNode top;

    Pair(Symbol orig, LirNode top) {
      this.orig = orig;
      this.top = top;
    }
  }
    

  // Replace variables in block blk.
  private void renameInBlock(Stack stack, BasicBlk blk, int[] nextVar, LirNode[] curVar) {
    int level = stack.size();

    if (root.traceOK("Ssa", 2))
      root.debOut.println("** Entering block #" + blk.id);

    // Replace in this block
    for (BiLink q = blk.instrList().first(); !q.atEnd(); q = q.next()) {
      LirNode ins = (LirNode)q.elem();
      // replace all (REG v) occurence in right hand side
      replaceRhs(ins, curVar);

      // replace left hand side
      if (ins.opCode == Op.PARALLEL) {
        int n = ins.nKids();
        for (int i = 0; i < n; i++)
          replaceLhs(stack, ins.kid(i), nextVar, curVar);
      } else
        replaceLhs(stack, ins, nextVar, curVar);
    }

    // Replace variables propagating to successor's PHI instructions.
    for (BiLink s = blk.succList().first(); !s.atEnd(); s = s.next()) {
      BasicBlk succ = (BasicBlk)s.elem();
      int n = succ.predList().whereIs(blk);
      
      for (BiLink q = succ.instrList().first(); !q.atEnd(); q = q.next()) {
        LirNode ins = (LirNode)q.elem();
        if (ins.opCode == Op.PHI) {
          if (ins.kid(1 + n).opCode != Op.REG)
            throw new IllegalArgumentException();
          Symbol origVar = ((LirSymRef)ins.kid(1 + n)).symbol;
          if (root.traceOK("Ssa", 2))
            root.debOut.println("** replace PHI: " + origVar.name + " to "
                                + curVar[rn.index(origVar)]);
          ins.setKid(1 + n, function.newLir
                     .operator(Op.LIST, Type.UNKNOWN,
                               curVar[rn.index(origVar)],
                               function.newLir.labelRef(blk.label()), null));
        }
      }
    }

    // Replace in immediate dominatee blocks.
    for (BiLink p = dom.kids[blk.id].first(); !p.atEnd(); p = p.next()) {
      BasicBlk kid = (BasicBlk)p.elem();
      renameInBlock(stack, kid, nextVar, curVar);
    }

    // Restore curVar as first state upon entry.
    while (stack.size() > level) {
      Pair p = (Pair)stack.pop();
      curVar[rn.index(p.orig)] = p.top;
    }
  }


  // Replace variables in right hand side
  private LirNode replaceRhs(LirNode ins, LirNode[] curVar) {
    switch (ins.opCode) {
    case Op.REG:
      if (!ins.isPhysicalRegister())
        return curVar[rn.index(ins)];
      break;
    case Op.PHI:
    case Op.PROLOGUE:
      break;
    case Op.SET:
      replaceRhs(ins.kid(0), curVar);
      ins.setKid(1, replaceRhs(ins.kid(1), curVar));
      break;
    case Op.CALL:
      ins.setKid(0, replaceRhs(ins.kid(0), curVar));
      replaceRhs(ins.kid(1), curVar);
      break;
    default:
      int n = ins.nKids();
      for (int i = 0; i < n; i++)
        ins.setKid(i, replaceRhs(ins.kid(i), curVar));
      break;
    }
    return ins;
  }


  private void replaceLhs(Stack stack, LirNode ins, int[] nextVar, LirNode[] curVar) {
    switch (ins.opCode) {
    case Op.PHI:
    case Op.SET:
      {
        LirNode lhs = ins.kid(0);
        if (lhs.opCode == Op.REG && !lhs.isPhysicalRegister()) {
          Symbol origVar = ((LirSymRef)lhs).symbol;
          if (dojob[rn.index(origVar)]) {
            stack.push(new Pair(origVar, curVar[rn.index(origVar)]));
            ins.setKid(0, createNewVar(origVar, nextVar, curVar));
          }
        }
      }
      break;
      
    case Op.PROLOGUE:
      {
        int n = ins.nKids();
        for (int i = 1; i < n; i++) {
          LirNode lhs = ins.kid(i);
          if (lhs.opCode == Op.REG && !lhs.isPhysicalRegister()) {
            Symbol origVar = ((LirSymRef)lhs).symbol;
            if (dojob[rn.index(origVar)]) {
              stack.push(new Pair(origVar, curVar[rn.index(origVar)]));
              ins.setKid(i, createNewVar(origVar, nextVar, curVar));
            }
          }
        }
      }
      break;

    case Op.CALL:
      {
        int n = ins.kid(2).nKids();
        for (int i = 0; i < n; i++) {
          LirNode lhs = ins.kid(2).kid(i);
          if (lhs.opCode == Op.REG && !lhs.isPhysicalRegister()) {
            Symbol origVar = ((LirSymRef)lhs).symbol;
            if (dojob[rn.index(origVar)]) {
              stack.push(new Pair(origVar, curVar[rn.index(origVar)]));
              ins.kid(2).setKid(i, createNewVar(origVar, nextVar, curVar));
            }
          }
        }
      }
      break;

    case Op.CLOBBER:
      {
        int n = ins.nKids();
        for (int i = 0; i < n; i++) {
          LirNode lhs = ins.kid(i);
          if (lhs.opCode == Op.REG && !lhs.isPhysicalRegister()) {
            Symbol origVar = ((LirSymRef)lhs).symbol;
            if (dojob[rn.index(origVar)]) {
              stack.push(new Pair(origVar, curVar[rn.index(origVar)]));
              ins.setKid(0, createNewVar(origVar, nextVar, curVar));
            }
          }
        }
      }
      break;
    }
  }

  /** Introduce new SSA variable. **/
  private LirNode createNewVar(Symbol origVar, int[] nextVar, LirNode[] curVar) {
    Symbol newVar;

    if (keepOriginalSymbol) {
      newVar = function.addSymbol(origVar, origVar.type);
    } else {
      String newName
        = origVar.name
        + (origVar.name.charAt(origVar.name.length() - 1) != '%' ? "%" : "")
        + nextVar[rn.index(origVar)];
      newVar = function.addSymbol(newName, Storage.REG, origVar.type,
                                  origVar.boundary, 0, origVar.opt());
      nextVar[rn.index(origVar)]++;
      // newVar.setShadow(origVar);
    }
    if (root.traceOK("Ssa", 2))
      root.debOut.println("** replace LHS: " + origVar.name + " to " + newVar.name);
    curVar[rn.index(origVar)] = function.newLir.symRef(newVar);
    return curVar[rn.index(origVar)];
  }
}

