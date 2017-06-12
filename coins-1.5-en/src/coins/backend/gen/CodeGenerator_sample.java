/*
Productions:
 1: xreg -> (REG I32)
 2: reg -> xreg
 3: xfreg -> (REG F64)
 4: freg -> xfreg
 5: const -> (INTCONST _)
 6: void -> (SET I32 xreg reg)
 7: void -> (SET F64 xfreg freg)
 8: reg -> (ADD I32 reg regconst)
 9: regconst -> reg
 10: regconst -> const
 11: freg -> (ADD F64 freg freg)
 12: mem -> (MEM I32 addr)
 13: memf -> (MEM F64 addr)
 14: addr -> (STATIC I32)
 15: addr -> reg
 16: reg -> mem
 17: freg -> memf
 18: reg -> const
 19: void -> (SET I32 mem reg)
 20: void -> (SET F64 memf freg)
 21: label -> (LABEL _)
 22: void -> (JUMP _ label)
*/
/*
Sorted Productions:
 9: regconst -> reg
 15: addr -> reg
 2: reg -> xreg
 4: freg -> xfreg
 10: regconst -> const
 18: reg -> const
 16: reg -> mem
 17: freg -> memf
 5: const -> (INTCONST _)
 14: addr -> (STATIC I32)
 1: xreg -> (REG I32)
 3: xfreg -> (REG F64)
 21: label -> (LABEL _)
 8: reg -> (ADD I32 reg regconst)
 11: freg -> (ADD F64 freg freg)
 12: mem -> (MEM I32 addr)
 13: memf -> (MEM F64 addr)
 6: void -> (SET I32 xreg reg)
 19: void -> (SET I32 mem reg)
 7: void -> (SET F64 xfreg freg)
 20: void -> (SET F64 memf freg)
 22: void -> (JUMP _ label)
*/
/*
Productions:
 1: _rewr -> (FLOATCONST F64)
 2: const -> (INTCONST _)
 3: _1 -> (ADD I32 _ const)
 4: _rewr -> (ADD I32 _1 const)
 5: _rewr -> (JUMPN _)
 6: _rewr -> (SET _)
*/
/*
Sorted Productions:
 2: const -> (INTCONST _)
 1: _rewr -> (FLOATCONST F64)
 3: _1 -> (ADD I32 _ const)
 4: _rewr -> (ADD I32 _1 const)
 6: _rewr -> (SET _)
 5: _rewr -> (JUMPN _)
*/
/* ----------------------------------------------------------
%   Copyright (C) 2004 The Coins Project Group               
%       (Read COPYING for detailed information.)             
----------------------------------------------------------- */
package coins.backend.gen;

import coins.backend.CantHappenException;
import coins.backend.Function;
import coins.backend.Module;
import coins.backend.Op;
import coins.backend.SyntaxError;
import coins.backend.Type;
import coins.backend.lir.LirFactory;
import coins.backend.lir.LirFconst;
import coins.backend.lir.LirIconst;
import coins.backend.lir.LirNode;
import coins.backend.lir.LirSymRef;
import coins.backend.sym.SymAuto;
import coins.backend.sym.SymStatic;
import coins.backend.sym.Symbol;
import coins.backend.util.BiLink;
import coins.backend.util.BiList;
import coins.backend.util.ImList;
import coins.backend.util.Misc;
import java.io.PrintWriter;

// imports here





public class CodeGenerator_sample extends CodeGenerator {

  /** State vector for labeling LIR nodes. Suffix is a LirNode's id. **/
  State[] stateVec;

  /** RewrState vector **/
  private RewrState[] rewrStates;

  /** Create code generator engine. **/
  public CodeGenerator_sample() {}


  /** State label for rewriting engine. **/
  class RewrState {
    static final int NNONTERM = 4;
    static final int NRULES = 6 + 1;
    static final int START_NT = 1;

    static final int NT__ = 0;
    static final int NT__rewr = 1;
    static final int NT_const = 2;
    static final int NT__1 = 3;

    String nontermName(int nt) {
      switch (nt) {
      case NT__: return "_";
      case NT__rewr: return "_rewr";
      case NT_const: return "const";
      case NT__1: return "_1";
      default: return null;
      }
    };

    final int[] rule = new int[NNONTERM];

    boolean rewritten;

    void record(int nt, int rule) {
      if (this.rule[nt] == 0) {
        this.rule[nt] = rule;
        switch (nt) {
        }
      }
    }

    LirNode labelAndRewrite(LirNode t, RewrState kids[], String phase,
                            BiList pre, BiList post) {
      switch (t.opCode) {
      case Op.INTCONST:
        record(NT_const, 2);
        break;
      case Op.FLOATCONST:
        if (t.type == 1028) {
          if (phase == "late")  {
            rewritten = true;
            return lir.node(Op.MEM, 1028, lir.node(Op.STATIC, 514, module.constToData(t)));
          }
        }
        break;
      case Op.ADD:
        if (t.type == 514) {
          if (kids[1].rule[NT_const] != 0) record(NT__1, 3);
          if (kids[0].rule[NT__1] != 0) if (kids[1].rule[NT_const] != 0)  {
            rewritten = true;
            return lir.node(Op.ADD, 514, t.kid(0).kid(0), lir.node(Op.ADD, 514, t.kid(0).kid(1), t.kid(1)));
          }
        }
        break;
      case Op.SET:
        if (phase == "late") if (Type.tag(t.type) == Type.AGGREGATE)  {
          rewritten = true;
          return rewriteAggregateCopy(t, pre);
        }
        break;
      case Op.JUMPN:
        if (phase == "late")  {
          rewritten = true;
          return rewriteJumpn(t, pre);
        }
        break;
      }
      return null;
    }

    public String toString() {
      StringBuffer buf = new StringBuffer();

      buf.append("State(");
      boolean comma = false;
      for (int i = 0; i < NNONTERM; i++) {
        if (rule[i] != 0) {
          if (comma) buf.append(",");
          buf.append(nontermName(i));
          comma = true;
        }
      }
      buf.append(")");

      return buf.toString();
    }

  }


  void initRewriteLabeling() {
    rewrStates = new RewrState[0];
  }

  /** set rewrStates. **/
  private void setRewrStates(int index, RewrState v) {
    if (index >= rewrStates.length) {
      RewrState[] w = new RewrState[Misc.clp2(index + 1)];
      for (int i = 0; i < rewrStates.length; i++)
        w[i] = rewrStates[i];
      rewrStates = w;
    }
    rewrStates[index] = v;
  }

  /** Return RewrState array. **/
  private RewrState getRewrStates(int index) {
    if (index < rewrStates.length)
      return rewrStates[index];
    else
      return null;
  }

  /** Rewrite L-expression. **/
  LirNode rewriteTree(LirNode tree, String phase, BiList pre, BiList post) {
    RewrState s = getRewrStates(tree.id);
    if (s != null && !s.rewritten)
      return tree;

    for (;;) {
      int n = nActualOperands(tree);
      RewrState[] kidst = new RewrState[n];
      for (int i = 0; i < n; i++) {
        LirNode r = rewriteTree(tree.kid(i), phase, pre, post);
        if (r != tree.kid(i))
          tree.setKid(i, r);
        kidst[i] = getRewrStates(tree.kid(i).id);
      }

      s = new RewrState();
      setRewrStates(tree.id, s);

      // rescanning disabled?
      if (disableRewrite.contains(tree.id))
        return tree;

      LirNode newTree = s.labelAndRewrite(tree, kidst, phase, pre, post);
      if (newTree == null)
        return tree;
      tree = newTree;

      if (false) {
        debOut.println("rewrite to: "
                       + (disableRewrite.contains(tree.id) ? "!" : "")
                       + tree);
      }
    }
  }



  /** State label for instruction selection engine. **/
  class State {
    static final int NNONTERM = 12;
    static final int NRULES = 22 + 1;
    static final int START_NT = 6;

    static final int NT__ = 0;
    static final int NT_reg = 1;
    static final int NT_freg = 2;
    static final int NT_xreg = 3;
    static final int NT_xfreg = 4;
    static final int NT_const = 5;
    static final int NT_void = 6;
    static final int NT_regconst = 7;
    static final int NT_mem = 8;
    static final int NT_addr = 9;
    static final int NT_memf = 10;
    static final int NT_label = 11;

    String nontermName(int nt) {
      switch (nt) {
      case NT__: return "_";
      case NT_reg: return "reg";
      case NT_freg: return "freg";
      case NT_xreg: return "xreg";
      case NT_xfreg: return "xfreg";
      case NT_const: return "const";
      case NT_void: return "void";
      case NT_regconst: return "regconst";
      case NT_mem: return "mem";
      case NT_addr: return "addr";
      case NT_memf: return "memf";
      case NT_label: return "label";
      default: return null;
      }
    };

    final int[] rule = new int[NNONTERM];
    final int[] cost1 = new int[NNONTERM];
    final int[] cost2 = new int[NNONTERM];

    void record(int nt, int cost1, int cost2, int rule) {
      if (this.rule[nt] == 0
          || (optSpeed ?
              (cost1 < this.cost1[nt]
               || cost1 == this.cost1[nt] && cost2 < this.cost2[nt])
              : (cost2 < this.cost2[nt]
                 || cost2 == this.cost2[nt] && cost1 < this.cost1[nt]))) {
        this.rule[nt] = rule;
        this.cost1[nt] = cost1;
        this.cost2[nt] = cost2;
        switch (nt) {
        case NT_reg:
          record(NT_regconst, 0 + cost1, 0 + cost2, 9);
          record(NT_addr, 0 + cost1, 0 + cost2, 15);
          break;
        case NT_xreg:
          record(NT_reg, 0 + cost1, 0 + cost2, 2);
          break;
        case NT_xfreg:
          record(NT_freg, 0 + cost1, 0 + cost2, 4);
          break;
        case NT_const:
          record(NT_regconst, 0 + cost1, 0 + cost2, 10);
          record(NT_reg, 1 + cost1, 1 + cost2, 18);
          break;
        case NT_mem:
          record(NT_reg, 1 + cost1, 1 + cost2, 16);
          break;
        case NT_memf:
          record(NT_freg, 1 + cost1, 1 + cost2, 17);
          break;
        }
      }
    }

    void label(LirNode t, State kids[]) {
      switch (t.opCode) {
      case Op.INTCONST:
        record(NT_const, 0, 0, 5);
        break;
      case Op.STATIC:
        if (t.type == 514) {
          record(NT_addr, 0, 0, 14);
        }
        break;
      case Op.REG:
        if (t.type == 514) {
          record(NT_xreg, 0, 0, 1);
        }
        if (t.type == 1028) {
          record(NT_xfreg, 0, 0, 3);
        }
        break;
      case Op.LABEL:
        record(NT_label, 0, 0, 21);
        break;
      case Op.ADD:
        if (t.type == 514) {
          if (kids[0].rule[NT_reg] != 0) if (kids[1].rule[NT_regconst] != 0) record(NT_reg, 1 + kids[0].cost1[NT_reg] + kids[1].cost1[NT_regconst], 1 + kids[0].cost2[NT_reg] + kids[1].cost2[NT_regconst], 8);
        }
        if (t.type == 1028) {
          if (kids[0].rule[NT_freg] != 0) if (kids[1].rule[NT_freg] != 0) record(NT_freg, 1 + kids[0].cost1[NT_freg] + kids[1].cost1[NT_freg], 1 + kids[0].cost2[NT_freg] + kids[1].cost2[NT_freg], 11);
        }
        break;
      case Op.MEM:
        if (t.type == 514) {
          if (kids[0].rule[NT_addr] != 0) record(NT_mem, 0 + kids[0].cost1[NT_addr], 0 + kids[0].cost2[NT_addr], 12);
        }
        if (t.type == 1028) {
          if (kids[0].rule[NT_addr] != 0) record(NT_memf, 0 + kids[0].cost1[NT_addr], 0 + kids[0].cost2[NT_addr], 13);
        }
        break;
      case Op.SET:
        if (t.type == 514) {
          if (kids[0].rule[NT_xreg] != 0) if (kids[1].rule[NT_reg] != 0) record(NT_void, 1 + kids[0].cost1[NT_xreg] + kids[1].cost1[NT_reg], 1 + kids[0].cost2[NT_xreg] + kids[1].cost2[NT_reg], 6);
          if (kids[0].rule[NT_mem] != 0) if (kids[1].rule[NT_reg] != 0) record(NT_void, 1 + kids[0].cost1[NT_mem] + kids[1].cost1[NT_reg], 1 + kids[0].cost2[NT_mem] + kids[1].cost2[NT_reg], 19);
        }
        if (t.type == 1028) {
          if (kids[0].rule[NT_xfreg] != 0) if (kids[1].rule[NT_freg] != 0) record(NT_void, 1 + kids[0].cost1[NT_xfreg] + kids[1].cost1[NT_freg], 1 + kids[0].cost2[NT_xfreg] + kids[1].cost2[NT_freg], 7);
          if (kids[0].rule[NT_memf] != 0) if (kids[1].rule[NT_freg] != 0) record(NT_void, 1 + kids[0].cost1[NT_memf] + kids[1].cost1[NT_freg], 1 + kids[0].cost2[NT_memf] + kids[1].cost2[NT_freg], 20);
        }
        break;
      case Op.JUMP:
        if (kids[0].rule[NT_label] != 0) record(NT_void, 1 + kids[0].cost1[NT_label], 1 + kids[0].cost2[NT_label], 22);
        break;
      }
    }

    public String toString() {
      StringBuffer buf = new StringBuffer();

      buf.append("State(");
      boolean comma = false;
      for (int i = 0; i < NNONTERM; i++) {
        if (rule[i] != 0) {
          if (comma) buf.append(",");
          buf.append(nontermName(i) + ":" + rule[i] + "[" + cost1[i] + "." + cost2[i] + "]");
          comma = true;
        }
      }
      buf.append(")");

      return buf.toString();
    }

    // State methods here
  }


  private static final Rule[] rulev = new Rule[State.NRULES];

  static {
    rrinit0();
  }
  static private void rrinit0() {
    rulev[9] = new Rule(9, true, false, 7, "9: regconst -> reg", null, null, null, 0, false, false, new int[]{1}, new String[]{null, "*reg-I32*"});
    rulev[15] = new Rule(15, true, false, 9, "15: addr -> reg", null, null, null, 0, false, false, new int[]{1}, new String[]{null, "*reg-I32*"});
    rulev[2] = new Rule(2, true, false, 1, "2: reg -> xreg", null, null, null, 0, false, false, new int[]{3}, new String[]{"*reg-I32*", null});
    rulev[4] = new Rule(4, true, false, 2, "4: freg -> xfreg", null, null, null, 0, false, false, new int[]{4}, new String[]{"*reg-F64*", null});
    rulev[10] = new Rule(10, true, false, 7, "10: regconst -> const", null, null, null, 0, false, false, new int[]{5}, new String[]{null, null});
    rulev[18] = new Rule(18, true, false, 1, "18: reg -> const", ImList.list(ImList.list("mov","$1","$0")), null, null, 0, false, false, new int[]{5}, new String[]{"*reg-I32*", null});
    rulev[16] = new Rule(16, true, false, 1, "16: reg -> mem", ImList.list(ImList.list("mov","$1","$0")), null, null, 0, false, false, new int[]{8}, new String[]{"*reg-I32*", null});
    rulev[17] = new Rule(17, true, false, 2, "17: freg -> memf", ImList.list(ImList.list("movf","$1","$0")), null, null, 0, false, false, new int[]{10}, new String[]{"*reg-F64*", null});
    rulev[5] = new Rule(5, false, false, 5, "5: const -> (INTCONST _)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[14] = new Rule(14, false, false, 9, "14: addr -> (STATIC I32)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[1] = new Rule(1, false, false, 3, "1: xreg -> (REG I32)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[3] = new Rule(3, false, false, 4, "3: xfreg -> (REG F64)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[21] = new Rule(21, false, false, 11, "21: label -> (LABEL _)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[8] = new Rule(8, false, false, 1, "8: reg -> (ADD I32 reg regconst)", ImList.list(ImList.list("add","$2","$1","$0")), null, null, 0, false, false, new int[]{1,7}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[11] = new Rule(11, false, false, 2, "11: freg -> (ADD F64 freg freg)", ImList.list(ImList.list("addf","$2","$1","$0")), null, null, 0, false, false, new int[]{2,2}, new String[]{"*reg-F64*", "*reg-F64*", "*reg-F64*"});
    rulev[12] = new Rule(12, false, false, 8, "12: mem -> (MEM I32 addr)", null, ImList.list(ImList.list("mem","$1")), null, 0, false, false, new int[]{9}, new String[]{null, null});
    rulev[13] = new Rule(13, false, false, 10, "13: memf -> (MEM F64 addr)", null, ImList.list(ImList.list("mem","$1")), null, 0, false, false, new int[]{9}, new String[]{null, null});
    rulev[6] = new Rule(6, false, false, 6, "6: void -> (SET I32 xreg reg)", ImList.list(ImList.list("mov","$2","$1")), null, null, 0, false, false, new int[]{3,1}, new String[]{null, null, "*reg-I32*"});
    rulev[19] = new Rule(19, false, false, 6, "19: void -> (SET I32 mem reg)", ImList.list(ImList.list("mov","$2","$1")), null, null, 0, false, false, new int[]{8,1}, new String[]{null, null, "*reg-I32*"});
    rulev[7] = new Rule(7, false, false, 6, "7: void -> (SET F64 xfreg freg)", ImList.list(ImList.list("movf","$2","$1")), null, null, 0, false, false, new int[]{4,2}, new String[]{null, null, "*reg-F64*"});
    rulev[20] = new Rule(20, false, false, 6, "20: void -> (SET F64 memf freg)", ImList.list(ImList.list("mov","$2","$1")), null, null, 0, false, false, new int[]{10,2}, new String[]{null, null, "*reg-F64*"});
    rulev[22] = new Rule(22, false, false, 6, "22: void -> (JUMP _ label)", ImList.list(ImList.list("jump","$1")), null, null, 0, false, false, new int[]{11}, new String[]{null, null});
  }


  /** Return default register set for type. **/
  String defaultRegsetForType(int type) {
    switch (type) {
    case 514: return "*reg-I32*";
    case 1028: return "*reg-F64*";
    default:
      return null;
    }
  }


  void initLabeling(LirFactory lir) {
    stateVec = new State[lir.idBound()];
  }

  String showLabel(LirNode t) {
    return stateVec[t.id].toString();
  }


  void labelTree(LirNode t) {
    if (stateVec[t.id] == null) {
      int n = nActualOperands(t);
      State[] kid = new State[n];
      for (int i = 0; i < n; i++) {
        LirNode s = t.kid(i);
        labelTree(s);
        kid[i] = stateVec[s.id];
      }

      State st = new State();
      stateVec[t.id] = st;
      st.label(t, kid);
    }
  }

  Rule getRule(LirNode t, int goal) {
    return rulev[stateVec[t.id].rule[goal]];
  }

  int getCost1(LirNode t, int goal) {
    return stateVec[t.id].cost1[goal];
  }

  int getCost2(LirNode t, int goal) {
    return stateVec[t.id].cost2[goal];
  }

  int startNT() { return State.START_NT; }

  /* String nameOfNT(int nt) { return nontermNamev[nt]; } */

  /** Expand building-macro. **/
  Object expandBuildMacro(ImList form) {
    String name = (String)form.elem();
    return null;
  }

  /** Expand building-macro, for LirNode **/
  Object quiltLir(LirNode node) {
    switch (node.opCode) {
    default:
      return quiltLirDefault(node);
    }
  }

  /** Expand emit-macro for list form. **/
  String emitList(ImList form, boolean topLevel) {
    String name = (String)form.elem();
    if (name == "mem")
      return jmac1(emitObject(form.elem(1)));
    return emitListDefault(form, topLevel);
  }

  /** Expand emit-macro for LirNode. **/
  String emitLir(LirNode node) {
    switch (node.opCode) {
    default:
      return emitLirDefault(node);
    }
  }

  // CodeGenerator methods here
  
  String jmac1(String x) { return "[" + x + "]"; }
  
  // String makeAsmSymbol(String symbol) { return "_" + symbol; }
  
}
