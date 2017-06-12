/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.gen;

import coins.backend.BackVersion;
import coins.backend.CantHappenException;
import coins.backend.Data;
import coins.backend.Function;
import coins.backend.GlobalTransformer;
import coins.backend.LocalTransformer;
import coins.backend.Transformer;
import coins.backend.Module;
import coins.backend.Op;
import coins.backend.Root;
import coins.backend.Storage;
import coins.backend.SyntaxError;
import coins.backend.TargetMachine;
import coins.backend.MachineParams;
import coins.backend.Type;
import coins.backend.cfg.BasicBlk;
import coins.backend.cfg.FlowGraph;
import coins.backend.lir.LirFactory;
import coins.backend.lir.LirFconst;
import coins.backend.lir.LirIconst;
import coins.backend.lir.LirLabelRef;
import coins.backend.lir.LirString;
import coins.backend.lir.LirNode;
import coins.backend.lir.LirSymRef;
import coins.backend.lir.LirBinOp;
import coins.backend.lir.LirUnaOp;
import coins.backend.lir.LirNaryOp;
import coins.backend.lir.LirVisitor;
import coins.backend.sym.Label;
import coins.backend.sym.SymAuto;
import coins.backend.sym.SymStatic;
import coins.backend.sym.Symbol;
import coins.backend.util.BiLink;
import coins.backend.util.BiList;
import coins.backend.util.BitMapSet;
import coins.backend.util.ImList;
import coins.backend.util.QuotedString;
import coins.backend.util.Misc;
import coins.backend.util.NumberSet;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Map;
import java.util.HashMap;

/**
 ** Abstract code generator.
 ** Generators for specific machines are defined as subclasses of this class.
 **/
public abstract class CodeGenerator {

  /** Following fields can be seen by subclasses. **/

  /** Commonly used type constants. **/
  static final int I64 = Type.type(Type.INT, 64);
  static final int I32 = Type.type(Type.INT, 32);
  static final int I16 = Type.type(Type.INT, 16);
  static final int I8 = Type.type(Type.INT, 8);
  static final int F128 = Type.type(Type.FLOAT, 128);
  static final int F64 = Type.type(Type.FLOAT, 64);
  static final int F32 = Type.type(Type.FLOAT, 32);

  /** Global variables **/
  Root root;

  /** L-Module **/
  Module module;

  /** Debugging message **/
  PrintWriter debOut;

  /** Feature of Machine **/
  MachineParams machineParams;

  /** Address type **/
  int addrType;

  /** Type of parameter words on stack **/
  int typeParamWord = I32;

  /** CPU name **/
  String machineName;

  /** OS convention **/
  String convention;

  /** Function attribute tables. **/
  Map funcAttrTbl = new HashMap();

  /** Flag speed/space tradeoff. **/
  boolean optSpeed = true;

  /** Next label counter. **/
  private int nextLabelNumber = 1;


  /** Keeps track of function's attributes. **/
  static class FunctionAttr {

    /** Parent object. **/
    Function func;

    /** Creator **/
    FunctionAttr(Function func) {
      this.func = func;
    }

    /** Stack space required for call **/
    int requiredStack;

    /** True if this is variable argument function. **/
    boolean isVarArg;

    /** Pointer to value returned (if the function returns struct) **/
    LirNode retPtr;

    /** Offset of the first non-register parameter (i.e. on stack) **/
    int stackParamOffset;
  }


  /** scratchpad variables. **/

  /** Current function **/
  Function func;

  /** LIR factory **/
  LirFactory lir;

  /** Output Writer **/
  PrintWriter asmWriter = null;

  private String currentSegment = null;

  /** Dynamic programming state table.
   ** Each entry corresponds to one LirNode. **/
  // State[] stateVec;

  /** Creator **/
  public CodeGenerator() {}

  /** Set root pointer etc.
   ** This class is created by newInstance()
   **  so constructor can't take arguments. **/
  public void initialize(Root root, Module module, TargetMachine target,
                         String machine, String convention) {
    if (this.root != null)
      throw new CantHappenException("initialize again");
    this.root = root;
    this.module = module;
    this.machineName = machine.intern();
    this.convention = convention.intern();
    machineParams = target.machineParams;
    this.addrType = machineParams.typeAddress();
    debOut = root.debOut;

    if (root.isOptionSet("optspace"))
      this.optSpeed = false;

    initializeMachineDep();
  }


  /** Set assembler output stream. **/
  public void setAsmStream(OutputStream stream) {
    asmWriter = new PrintWriter(insertPostProcessor(stream));
  }

  /** Insert post-processor filter pass. Subclasses may override it. **/
  OutputStream insertPostProcessor(OutputStream stream) { return stream; }

  /** Notify end of assembly to post-processor. Subclasses may override it. **/
  void notifyEndToPostProcessor() {}


  /** Do Machine-dependent initialization (subclass may override this). **/
  void initializeMachineDep() {}


  /** Common interface for Dynamic Programming Table **/
  //  static abstract class State {
  //    /** Following fields are initialized by subclasses. **/
  //    int[] rule;
  //    int[] cost;

  //    /** Fill this state for node t. **/
  //    abstract void label(LirNode t, State kid[]);

  //    /** Return production rule object. **/
  //    abstract Rule ruleFor(int goal);
  //  }


  /** Node of matching tree (or DAG) **/
  class Match {
    /** Original LIR operator being matched. **/
    LirNode orig;

    /** The rule best-matched. **/
    Rule rule;

    /** cost **/
    int cost1, cost2;

    /** Destination register variable node. **/
    LirNode dest;

    /** Source register variable nodes. **/
    LirNode src[];

    /** Direct children of this operator.
     **  e.g. (SUB I32 kid[0] kid[1]) **/
    Match[] kid;

    /** Place of leaf nodes in the rule.
     **  e.g. at rule (SUB I32 _ (MEM I32 _)), 
     **  rLeaf[0]=this, rLeafIndex[0]=0,
     **  rLeaf[1]=kid[1], rLeafIndex[1]=0   **/
    Match[] rLeaf;
    int[] rLeafIndex;

    /** Return leaf node in the current rule. **/
    private Match ruleLeaf(int n) { return rLeaf[n].kid[rLeafIndex[n]]; }


    /** Place of leaf node in the output instruction tree.
     **  e.g. at tree (SUB I32 _ (MEM I32 (ADD I32 _ _))), 
     **  tLeaf[0]=this, tLeafIndex[0]=0,
     **  tLeaf[1]=kid[1].kid[0], tLeafIndex[1]=0
     **  tLeaf[2]=kid[1].kid[0], tLeafIndex[2]=1   **/
    Match[] tLeaf;
    int[] tLeafIndex;

    /** Return leaf node in the output instruction tree. **/
    private Match treeLeaf(int n) { return tLeaf[n].kid[tLeafIndex[n]]; }

    /** Root of subinstruction tree. **/
    Match[] subtrees;

    /** Sethi-Ullman number. **/
    int suNumber;

    /** Code generation order. **/
    int[] order;
    
    /** Label table. **/
    Map genLabelTbl;

    /** Create match object. **/
    Match(LirNode orig, Match[] kid, Rule rule, int cost1, int cost2) {
      this.orig = orig;
      this.kid = kid;
      this.rule = rule;
      this.cost1 = cost1;
      this.cost2 = cost2;
      this.src = new LirNode[kid.length];

      if (!isDerived()) {
        int n = numRuleLeaf(0);
        rLeaf = new Match[n];
        rLeafIndex = new int[n];
        fillRuleLeaf(this, 0);
      }
    }

    /** Return the number of leaves in current rule. **/
    private int numRuleLeaf(int n) {
      for (int i = 0; i < kid.length; i++) {
        if (kid[i].isDerived())
          n = kid[i].numRuleLeaf(n);
        else
          n++;
      }
      return n;
    }

    /** Set positions of rule-leaves on rLeaf+rLeafIndex. **/
    private int fillRuleLeaf(Match base, int n) {
      for (int i = 0; i < kid.length; i++) {
        if (kid[i].isDerived())
          n = kid[i].fillRuleLeaf(base, n);
        else {
          base.rLeaf[n] = this;
          base.rLeafIndex[n++] = i;
        }
      }
      return n;
    }



    /** Set root of subinstruction trees. **/
    private void setTreeLeaf() {
      int n = numTreeLeaf(0);
      tLeaf = new Match[n];
      tLeafIndex = new int[n];
      fillTreeLeaf(this, 0);
      for (int i = 0; i < n; i++)
        treeLeaf(i).setTreeLeaf();
    }

    /** Return the number of subinstructions. **/
    private int numTreeLeaf(int n) {
      for (int i = 0; i < kid.length; i++) {
        if (!kid[i].isCore())
          n = kid[i].numTreeLeaf(n);
        else
          n++;
      }
      return n;
    }

    /** Set positions of subinstruction trees **/
    private int fillTreeLeaf(Match base, int n) {
      for (int i = 0; i < kid.length; i++) {
        if (!kid[i].isCore())
          n = kid[i].fillTreeLeaf(base, n);
        else {
          base.tLeaf[n] = this;
          base.tLeafIndex[n++] = i;
        }
      }
      return n;
    }


    /** Subsidiary of printIt **/
    private void printIt(PrintWriter out, String indent) {
      if (rule != null) {
        out.println(indent + (isCore() ? "*" : "") + rule.toString()
                    + (dest != null ? " [dest=" + dest + "]" : "")
                    + " SU=" + suNumber);
        for (int i = 0; i < kid.length; i++)
          kid[i].printIt(out, "  " + indent);
      } else
        out.println(indent + "(no rule)");
    }

    /** Print contents of Match instance on Stream out for debugging. **/
    void printIt(PrintWriter out) {
      printIt(out, "");
    }

    /** Return true if this node has assembler code to be generated. **/
    boolean hasCode() { return rule.code != null; }

    /** Return true if this node is the root of a instruction tree. **/
    boolean isCore() { return hasCode() || dest != null; }

    /** Return true if this node is derived rule. **/
    boolean isDerived() { return rule.isDerived; }



    /** Return true if this match is cheaper than x **/
    boolean isCheaperThan(Match x) {
      if (optSpeed)
        return (cost1 < x.cost1 || (cost1 == x.cost1 && cost2 < x.cost2));
      else
        return (cost2 < x.cost2 || (cost2 == x.cost2 && cost1 < x.cost1));
    }


    /** Return true if node is a register variable node. **/
    /* use LirNode#isRegisterOperand() instead.
    private boolean isRegNode(LirNode node) {
      return node.opCode == Op.REG
        || (node.opCode == Op.SUBREG && node.kid(0).opCode == Op.REG);
    }
    */

    /** Return true if register <code>reg</code> satisfies
     **  constraint <code>regset</code>. **/
    private boolean satisfies(LirNode reg, String regset) {
      // Physical register is OK anyway.
      //  (should check if reg is a member of regset...)
      if (reg.isPhysicalRegister())
        return true;
      if (reg.opCode == Op.SUBREG)
        return false;
      return getRegsetOf(reg) == regset;
    }

    /** Return number of available registers for regset s. **/
    private int availRegs(String s) {
      if (s == null)
        throw new CantHappenException("null pointer");
      return machineParams.nAvail(machineParams.getRegSet(s));
    }

    /** Return true if <node>reg</node> is acceptable as destination. **/
    private boolean acceptableAsDestination(LirNode reg) {
      if (dest != null)
        return dest == reg;

      String regset = null;
      if (rule.regsets != null)
        regset = rule.regsets[0];

      if (regset == null)
        return false;

      if (rule.eqregs != 0) {
        long w = rule.eqregs >> 1;
        for (int i = 0; i < rLeaf.length; w >>= 1, i++) {
          if ((w & 1) != 0) {
            LirNode srcReg = ruleLeaf(i).orig;
            if (root.traceOK("TMD", 1))
              root.debOut.println("eqreg check: " + reg + " vs " + srcReg
                                  + " = " + (srcReg == reg));
            if (srcReg != reg)
              return false;
          }
        }
      }
      return satisfies(reg, regset);
    }


    /** Remove SET operation if possible. **/
    Match removeSet(boolean pass1) {
      if (orig.opCode == Op.SET
          && orig.kid(0).isRegisterOperand() && kid[1].hasCode()) {
        LirNode reg = orig.kid(0);
        if (!pass1 || kid[1].acceptableAsDestination(reg)) {
          kid[1].dest = reg;
          return kid[1];
        }
      }
      return this;
    }

    

    /** Add CLOBBERs **/
    private LirNode addClobber(LirNode node) {
      if (rule.clobber == null)
        return node;

      try {
        return lir.node
          (Op.PARALLEL, Type.UNKNOWN, node,
           lir.decodeLir(new ImList("CLOBBER", rule.clobber),
                         func, func.module));
      } catch (SyntaxError e) {
        throw new CantHappenException("clobber syntax: " + e);
      }
    }


    /** Assign destination register variable. **/
    void allocTemp(LirNode required, String regset) {
      if (isDerived())
        throw new CantHappenException();

      // allocate destination.
      if (dest == null && rule.regsets[0] != null) {
        if (hasCode()) {
          if (required != null && regset == rule.regsets[0])
            dest = required;
          else {
            dest = func.newTemp(machineParams.getRegSetType(rule.regsets[0]));
            setRegsetOf(dest, rule.regsets[0]);
          }
        } else if (orig.isRegisterOperand())
          dest = orig;
      }

      // process equate register.
      for (int i = 0; i < rLeaf.length; i++) {
        if (((rule.eqregs >>> (i + 1)) & 1) == 1)
          rLeaf[i].src[rLeafIndex[i]] = dest;
      }

      for (int i = 0; i < rLeaf.length; i++) {
        ruleLeaf(i).allocTemp(rLeaf[i].src[rLeafIndex[i]],
                              rule.regsets[i + 1]);
        if (rule.regsets[i + 1] != null
            && rLeaf[i].src[rLeafIndex[i]] == null) {
          LirNode reg = ruleLeaf(i).dest;
          if (reg != null) {
            if (!satisfies(reg, rule.regsets[i + 1])) {
              reg = func.newTemp(reg.type);
              setRegsetOf(reg, rule.regsets[i + 1]);
            }
            rLeaf[i].src[rLeafIndex[i]] = reg;
          }
        }
      }
    }


    /** Compute Sethi-Ullman number and reorder instructions. **/
    void scheduleBySuNumber() {
      for (int i = 0; i < tLeaf.length; i++)
        treeLeaf(i).scheduleBySuNumber();

      // sort descending suNumber order.
      if (order == null)
        order = new int[tLeaf.length];
      for (int i = 0; i < tLeaf.length; i++)
        order[i] = i;
      for (int i = 0; i < order.length; i++) {
        for (int j = i + 1; j < order.length; j++) {
          Match it = treeLeaf(order[i]);
          Match jt = treeLeaf(order[j]);
          if (it.suNumber < jt.suNumber
              || (it.suNumber == jt.suNumber && it.isCheaperThan(jt))) {
            int w = order[i];
            order[i] = order[j];
            order[j] = w;
          }
        }
      }

      // Estimate register-usage.
      suNumber = 0;
      for (int i = 0; i < order.length; i++) {
        int su = treeLeaf(order[i]).suNumber;
        if (su > suNumber)
          suNumber = su;
        else if (su == suNumber && su > 0)
          suNumber++;
      }
      if (hasCode() && rule.regsets[0] != null && suNumber == 0)
        suNumber = 1;
    }



    /** Subsidary of makeupTree. **/
    private LirNode makeupTree1() {
      if (rule.isChain) {
        if (kid[0].isCore())
          return src[0];
        else
          return kid[0].makeupTree1();
      } else {
        LirNode node = lir.makeShallowCopy(orig);
        for (int i = 0; i < kid.length; i++) {
          if (kid[i].isCore())
            node.setKid(i, src[i]);
          else
            node.setKid(i, kid[i].makeupTree1());
        }
        return node;
      }
    }


    /** Make up LIR instruction tree. **/
    private LirNode makeupTree() {
      LirNode node;
      if (kid.length == 0)
        node = orig;
      else
        node = makeupTree1();
      if (dest != null)
        node = lir.node(Op.SET, dest.type, dest, node);
      node = addClobber(node);
      if (rule.useAfterDef)
        node = node.replaceOptions
          (lir, new ImList("&use-after-def", node.opt));
      return node;
    }


    /** Decompose original LIR tree. **/
    void generateDecomposed(BiList list) {
      BiList after = new BiList();
      for (int i = 0; i < tLeaf.length; i++) {
        int j = order[i];
        treeLeaf(j).generateDecomposed(list);
        LirNode s = tLeaf[j].src[tLeafIndex[j]];
        LirNode d = treeLeaf(j).dest;
        if (s != d) {
          LirNode copy = lir.node(Op.SET, s.type, s, d);
          int x, y;
          if (s.isPhysicalRegister())
            x = 1;
          else {
            String sRegset = getRegsetOf(s);
            if (sRegset == null)
              throw new Error("Variable " + s + " has no regset.");
            x = availRegs(sRegset);
          }
          if (d.isPhysicalRegister())
            y = 1;
          else {
            String dRegset = getRegsetOf(d);
            if (dRegset == null)
              throw new Error("Variable " + d + " has no regset.");
            y = availRegs(dRegset);
          }
          if (x > y)
            list.add(copy);
          else
            after.add(copy);
          if (root.traceOK("TMD", 1))
            debOut.println("  inserted: " + copy);
        }
      }
      list.concatenate(after);

      if (hasCode())
        list.add(makeupTree());
    }


    /** Generate decomposed LIR from match tree. **/
    void decompLir(BiList list) {
      allocTemp(null, null);
      setTreeLeaf();
      scheduleBySuNumber();
      if (root.traceOK("TMD", 1))
        printIt(debOut);
      generateDecomposed(list);
    }


    /** Skip non-operational rules at root. **/
    Match skipNonOpRules() {
      if (hasCode() || kid.length > 1)
        return this;
      return kid[0].skipNonOpRules();
    }



    /** Generate assembler in ImList form. **/
    ImList quiltCode() {
      return quiltList(rule.code);
    }

    private ImList quiltList(ImList list) {
      if (list == null)
        return ImList.Empty;
      if (list.atEnd())
        return list;
      Object obj = quiltObj(list.elem());
      if (obj instanceof ImList)
        return ((ImList)obj).append(quiltList(list.next()));
      else
        return new ImList(obj, quiltList(list.next()));
    }

    private Object quiltObj(Object elem) {
      if (elem instanceof ImList) {
        ImList form = quiltList((ImList)elem);
        if (!form.atEnd() && form.elem() instanceof String) {
          Object result = expandBuildMacro(form);
          if (result != null)
            return result;
        }
        return new ImList(form);
      }
      else if (elem instanceof String) {
        String word = (String)elem;
        if (word.charAt(0) == '$') {
          if (word.charAt(1) == '$')
            return orig;
          else if (word.charAt(1) == 'L')
            return genLabel(word.substring(2));    // generate label

          int n = Integer.parseInt(word.substring(1));
          if (n == 0)
            return quiltValue();
          else
            return ruleLeaf(n - 1).quiltValue();
        }
        else
          return word;
      }
      else if (elem instanceof LirNode)
        return quiltLir((LirNode)elem);
      else
        return elem;
    }

    /** Expand current rule's value field. **/
    private Object quiltValue() {
      if (rule.value != null)
        return quiltList(rule.value);
      else if (dest != null)
        return quiltLir(dest);
      else if (kid.length > 0)
        return kid[0].quiltValue();
      else
        return quiltLir(orig);
    }


    /** Look for label bind to labelid
     ** or generate new one if not yet bound. **/
    private String genLabel(String labelid) {
      if (genLabelTbl == null)
        genLabelTbl = new HashMap();
      Label label = (Label)genLabelTbl.get(labelid);
      if (label == null) {
        label = module.newLabel();
        genLabelTbl.put(labelid, label);
      }
      return label.name();
    }

  }


  /** Set register set for REG symbol. **/
  private static void setRegsetOf(Symbol sym, String regset) {
    if (regset == null)
      throw new CantHappenException("regset set to null");
    sym.setOpt(ImList.list("&regset", regset));
  }


  /** Get register set for REG symbol. **/
  private static String getRegsetOf(Symbol sym) {
    for (ImList p = sym.opt(); !p.atEnd(); p = p.next()) {
      if (p.elem() == "&regset")
        return (String)p.elem2nd();
    }
    return null;
  }
          
  /** Set register set for REG node. **/
  private static void setRegsetOf(LirNode node, String regset) {
    setRegsetOf(((LirSymRef)node).symbol, regset);
  }

  /** Set dontspill flag to register symbol. **/
  private static void setDontspill(Symbol sym) {
    sym.setOpt(ImList.list("&dontspill"));
  }

  /** Set dontspill flag to register node. **/
  private static void setDontspill(LirNode node) {
    setDontspill(((LirSymRef)node).symbol);
  }


  /** Get register set for REG node. **/
  private static String getRegsetOf(LirNode node) {
    return getRegsetOf(((LirSymRef)node).symbol);
  }



  /** Return function attributes. **/
  FunctionAttr getFunctionAttr(Function func) {
    FunctionAttr attr = (FunctionAttr)funcAttrTbl.get(func);
    if (attr == null) {
      attr = newFunctionAttr(func);
      funcAttrTbl.put(func, attr);
    }
    return attr;
  }



  /** Flags for disabling rescanning. **/
  NumberSet disableRewrite;

  /** Disable rewriting of LIR node. **/
  LirNode noRescan(LirNode node) {
    disableRewrite.add(node.id);
    return node;
  }


  /** Pre-rewrite of L-Function. **/
  public void prerewrite(Function f, String phase) {
    func = f;
    lir = f.newLir;

    disableRewrite = new BitMapSet();
    initRewriteLabeling();
    BiList pre = new BiList();
    BiList post = new BiList();

    BiList list = func.lirList();
    for (BiLink q = list.first(); !q.atEnd(); q = q.next()) {
      LirNode ins = (LirNode)q.elem();
      if (root.traceOK("TMD", 2))
        debOut.println("rewriting: " + ins);
      LirNode newIns = rewriteTree(ins, phase, pre, post);
      if (newIns != ins)
        q.setElem(newIns);
      if (!post.isEmpty()) {
        q.addAllAfter(post);
        post.clear();
      }
      if (!pre.isEmpty()) {
        BiLink prev = q.prev();
        q.addAllBefore(pre);
        pre.clear();
        q = prev;
      }
    }
  }

  /** Initialization for rewriting. **/
  abstract void initRewriteLabeling();

  /** Rewrite L-expression. **/
  abstract LirNode rewriteTree(LirNode tree, String phase, BiList pre, BiList post);


  /** Early time pre-rewriting of LIR, function by function. **/
  final LocalTransformer localEarlyRewritingTrig = new LocalTransformer() {
      public boolean doIt(Function func, ImList args) {
        prerewrite(func, "early");
        return true;
      }

      public boolean doIt(Data data, ImList args) { return true; }
      
      public String name() { return "LocalEarlyRewriting"; }

      public String subject() { return "Early-time Rewriting (local)"; }
    };


  /** Return early time pre-rewriting sequence. **/
  public Transformer[] earlyRewritingSequence() {
    return new Transformer[] {
      localEarlyRewritingTrig
    };
  }


  /** Late time pre-rewriting of LIR, function by function. **/
  final LocalTransformer localLateRewritingTrig = new LocalTransformer() {
      public boolean doIt(Function func, ImList args) {
        prerewrite(func, "late");
        return true;
      }

      public boolean doIt(Data data, ImList args) { return true; }
      
      public String name() { return "LocalLateRewriting"; }

      public String subject() { return "Late-time Rewriting (local)"; }
    };


  final LocalTransformer ProcessFramesTrig = new LocalTransformer() {
      public boolean doIt(Function f, ImList args) {
        func = f;
        lir = f.newLir;
        processFrames();
        return true;
      }

      public boolean doIt(Data data, ImList args) { return true; }
      
      public String name() { return "ProcessFrames"; }

      public String subject() { return "Set up frame offsets in symbol table"; }
    };


  /** Return late time pre-rewriting sequence. **/
  public Transformer[] lateRewritingSequence() {
    return new Transformer[] {
      localLateRewritingTrig,
      ProcessFramesTrig
    };
  }



  private BiList markNode(BiList list, int op, NumberSet done) {
    for (BiLink p = list.first(); !p.atEnd(); p = p.next()) {
      LirNode ins = (LirNode)p.elem();
      if (ins.opCode == op)
        done.add(ins.id);
    }
    return list;
  }


  /** Return frame size (in positive) of function. **/
  int frameSize(Function f) {
    int loc = 0;
    for (BiLink p = f.localSymtab.symbols().first(); !p.atEnd();
         p = p.next()) {
      SymAuto var = (SymAuto)p.elem();
      if (var.storage == Storage.FRAME) {
        int off = var.offset();
        if (off != 0 && off < loc)
          loc = off;
      }
    }
    return -loc;
  }

  boolean frameIsEmpty(Function f){
	for (BiLink p = f.localSymtab.symbols().first(); !p.atEnd();
         p = p.next()) {
      SymAuto var = (SymAuto)p.elem();
      if (var.storage == Storage.FRAME) 
    	  return false;
	}
	return true;
  }

  /** Assign frame offset to FRAME variables and rewrite FRAME. **/
  private int processFrames() {
    // Assign frame offset to FRAME variables.
    int loc = -frameSize(func);

    for (BiLink p = func.localSymtab.symbols().first(); !p.atEnd();
         p = p.next()) {

      SymAuto var = (SymAuto)p.elem();
      switch(var.storage) {
      case Storage.FRAME:
        int off = var.offset();
        if (off == 0) {
          loc -= Type.bytes(var.type);
          loc &= -(var.boundary != 0 ? var.boundary : alignForType(var.type));
          var.setOffset(loc);
        }
        break;
      case Storage.REG:
        if (getRegsetOf(var) == null) {
          String regset = defaultRegsetForType(var.type);
          if (regset != null)
            setRegsetOf(var, regset);
          else {
            // Drop REG to FRAME if no registers available.
            func.localSymtab.addSymbol
              (var.name.replace('%', '#'), Storage.FRAME, var.type,
               var.boundary, 0, null);
          }
        }
        break;
      }
    }

    // Rewrite FRAME variables.
    for (BiLink p = func.flowGraph().basicBlkList.first(); !p.atEnd();
         p = p.next()) {
      BasicBlk blk = (BasicBlk)p.elem();

      for (BiLink q = blk.instrList().first(); !q.atEnd(); q = q.next())
        rewriteFramesInTree((LirNode)q.elem());
    }

    return loc & -alignForType(F64);
  }


  /** Rewrite FRAME variables in a tree. **/
  private LirNode rewriteFramesInTree(LirNode tree) {
    if (tree.opCode == Op.REG
        && !tree.isPhysicalRegister() && getRegsetOf(tree) == null)
      tree = dropRegToFrame(tree);
    int n = tree.nKids();
    for (int i = 0; i < n; i++) {
      LirNode orig = tree.kid(i);
      LirNode repl = rewriteFramesInTree(orig);
      if (repl != orig)
        tree.setKid(i, repl);
    }
    if (tree.opCode == Op.FRAME)
      return rewriteFrame(tree);
    else
      return lir.foldConstant(tree);
  }


  /** Convert (REG t ...) node to (MEM t (FRAME I32 ...)). **/
  LirNode dropRegToFrame(LirNode node) {
    Symbol frame = mapRegToFrame(((LirSymRef)node).symbol);
    if (frame == null)
      throw new CantHappenException("No FRAME for REG: "
                                    +  ((LirSymRef)node).symbol);
    return lir.node(Op.MEM, node.type, lir.symRef(frame));
  }

  /** Return FRAME variable symbol entry which corresponds to REG variable. **/
  Symbol mapRegToFrame(Symbol reg) {
    return func.localSymtab.get(reg.name.replace('%', '#'));
  }

  /** Reserve frame variable. **/
  SymAuto reserveFrame(String name, int type) {
    SymAuto sym = (SymAuto)func.localSymtab.get(name);
    if (sym == null) {
      sym = (SymAuto)func.addSymbol(name, Storage.FRAME, type,
                                    alignForType(type), 0, null);
      int loc = (-frameSize(func) - Type.bytes(type)) & -alignForType(type);
      sym.setOffset(loc);
    }
    return sym;
  }


  static final int INLINECOPYUNIT = 8;

  /** Get alignment option. **/
  private int getAlignment(LirNode memExp) {
    for (ImList p = memExp.opt; !p.atEnd(); p = p.next()) {
      if (p.elem() == "&align")
        return Integer.parseInt((String)p.elem2nd());
    }
    return 1;
  }


  /** Rewrite aggregate copy. Subclasses may override this. **/
  LirNode rewriteAggregateCopy(LirNode node, BiList pre) {

    if (node.kid(0).opCode != Op.MEM || node.kid(1).opCode != Op.MEM
        || node.kid(1).type != node.type)
      throw new CantHappenException("Malformed aggregate copy");

    if (node.kid(0).type != node.type)
      debOut.println("Warning: copying objects whose sizes are different");

    // get smaller alignment.
    int align = getAlignment(node.kid(0));
    int align2 = getAlignment(node.kid(1));
    if (align2 < align)
      align = align2;

    int elemType;
    switch (align) {
    case 1:
      elemType = Type.type(Type.INT, 8);
      break;
    case 2:
      elemType = Type.type(Type.INT, 16);
      break;
    case 4:
    default:
      elemType = Type.type(Type.INT, 32);
      align = 4;
      break;
    }

    int bytes = Type.bytes(node.type);
    if (bytes != 0) {
      LirNode dst = node.kid(0).kid(0);
      LirNode src = node.kid(1).kid(0);
      LirNode srcPtr = func.newTemp(addrType);
      LirNode dstPtr = func.newTemp(addrType);
      pre.add(lir.node(Op.SET, addrType, srcPtr, src));
      pre.add(lir.node(Op.SET, addrType, dstPtr, dst));
      if (bytes/align <= INLINECOPYUNIT) {
        for (int j = 0; j < bytes; j += align) {
          pre.add
            (lir.node
             (Op.SET, elemType,
              lir.node
              (Op.MEM, elemType,
               (j == 0 ? dstPtr : lir.node
                (Op.ADD, addrType, dstPtr, lir.iconst(addrType, j)))),
              lir.node
              (Op.MEM, elemType,
               (j == 0 ? srcPtr : lir.node
                (Op.ADD, addrType, srcPtr, lir.iconst(addrType, j))))));
        }
      } else {
        Label loopTop = func.newLabel();
        Label loopExit = func.newLabel();
        LirNode counter = func.newTemp(addrType);
        pre.add(lir.node(Op.SET, addrType, counter,
                          lir.iconst(addrType, bytes)));
        pre.add(lir.node(Op.DEFLABEL, addrType, lir.labelRef(loopTop)));
        pre.add
          (lir.node
           (Op.SET, elemType,
            lir.node(Op.MEM, elemType, dstPtr),
            lir.node(Op.MEM, elemType, srcPtr)));
        pre.add
          (lir.node
           (Op.SET, addrType, srcPtr,
            lir.node(Op.ADD, addrType, srcPtr, lir.iconst(addrType, align))));
        pre.add
          (lir.node
           (Op.SET, addrType, dstPtr,
            lir.node(Op.ADD, addrType, dstPtr, lir.iconst(addrType, align))));
        pre.add
          (lir.node
           (Op.SET, addrType, counter,
            lir.node(Op.SUB, addrType, counter, lir.iconst(addrType, align))));
        pre.add
          (lir.node
           (Op.JUMPC, 0,
            lir.node(Op.TSTNE, addrType, counter, lir.iconst(addrType, 0)),
            lir.labelRef(loopTop), lir.labelRef(loopExit)));
        pre.add(lir.node(Op.DEFLABEL, addrType, lir.labelRef(loopExit)));
      }
    }

    node = (LirNode)pre.last().elem();
    pre.last().unlink();
    return node;
  }


  /** Return the location of parameter which should be reside on memory. **/
  int paramToBeSaved() {
    FunctionAttr at = getFunctionAttr(func);
    
    int wordsize = Type.bytes(typeParamWord);
    int regLimit = clcvnRegLimit();

    LirNode node = null;
    for (BiLink p = func.firstInstrList().first(); !p.atEnd(); p = p.next()) {
      node = (LirNode)p.elem();
      if (node.opCode == Op.PROLOGUE)
        break;
    }

    int n = node.nKids();
    int paramLoc = 0;
    int paramLoc0 = 0;
    if (func.origEpilogue.nKids() > 1) {
      if (Type.tag(func.origEpilogue.kid(1).type) == Type.AGGREGATE
          && clcvnStructReturnAsFirst()) {
        paramLoc0 = paramLoc = wordsize;
      }
    }
    for (int i = 1; i < n; i++) {
      LirNode x = node.kid(i);
      int b = (Type.bytes(x.type) + wordsize - 1) & -wordsize;
      paramLoc0 = paramLoc;
      paramLoc += b;
      if (paramLoc > regLimit)
        return paramLoc0;
    }

    return regLimit;
  }


  /** Return offset of the formal parameter (used by va_start). **/
  int paramOffset(LirNode arg) {
    int regLimit = clcvnRegLimit();
    int wordsize = Type.bytes(typeParamWord);

    LirNode node = null;
    for (BiLink p = func.firstInstrList().first(); !p.atEnd(); p = p.next()) {
      node = (LirNode)p.elem();
      if (node.opCode == Op.PROLOGUE)
        break;
    }

    int paramLoc = 0;
    if (func.origEpilogue.nKids() > 1) {
      if (Type.tag(func.origEpilogue.kid(1).type) == Type.AGGREGATE
          && clcvnStructReturnAsFirst()) {
        paramLoc = wordsize;
      }
    }
    int n = node.nKids();
    for (int i = 1; i < n; i++) {
      LirNode x = node.kid(i);
      if (equalArg(arg, x))
        return paramLoc;
      paramLoc += (Type.bytes(x.type) + wordsize - 1) & -wordsize;
    }
    return 0;
  }


  /** Return true if variable x and y are same. **/
  boolean equalArg(LirNode x, LirNode y) {
    if (x.opCode == Op.MEM)
      x = x.kid(0);
    if (y.opCode == Op.MEM)
      y = y.kid(0);
    return ((LirSymRef)x).symbol == ((LirSymRef)y).symbol;
  }



  private int tmpCount = 1;

  /** Rewrite PROLOGUE **/
  LirNode rewritePrologue(LirNode node, BiList post) {
    int regLimit = clcvnRegLimit();
    int wordsize = Type.bytes(typeParamWord);

    BiList sync = new BiList();
    FunctionAttr at = getFunctionAttr(func);
    BiList alist = new BiList();

    int paramLoc = 0;

    at.stackParamOffset = (at.isVarArg ?  0 : paramToBeSaved());

    if (func.origEpilogue.nKids() > 1
        && Type.tag(func.origEpilogue.kid(1).type) == Type.AGGREGATE
        && clcvnStructReturnAsFirst()) {
      // First parameter is a pointer to struct returning value.
      at.retPtr = func.newTemp(addrType);
      LirNode src = clcvnParamWord(at.retPtr.type, paramLoc, false);
      if (src.isRegisterOperand())
        alist.add(src);
      post.add(lir.node(Op.SET, at.retPtr.type, at.retPtr, src));
      paramLoc += wordsize;
    }

    for (int loc = at.stackParamOffset; loc < regLimit; loc += wordsize) {
      sync.add
        (lir.node
         (Op.SET, typeParamWord, clcvnParamMem(typeParamWord, loc, false),
          clcvnParamReg(typeParamWord, loc, false)));
    }

    int n = node.nKids();
    for (int i = 1; i < n; i++) {
      LirNode arg = node.kid(i);
      switch (Type.tag(arg.type)) {
      case Type.INT:
        if (Type.bytes(arg.type) > wordsize) {
          int m = (Type.bytes(arg.type) + wordsize - 1) / wordsize;
          for (int j = 0; j < m; j++) {
            LirNode src = clcvnParamWord(typeParamWord, paramLoc, false);
            if (src.isRegisterOperand())
              alist.add(src);
            post.add(clcvnSetPartialWord(arg, j, src));
            paramLoc += wordsize;
          }
        } else {
          LirNode src = clcvnParamWord(arg.type, paramLoc, false);
          if (src.isRegisterOperand())
            alist.add(src);
          post.add(lir.node(Op.SET, arg.type, arg, src));
          paramLoc += wordsize;
        }
        break;
      case Type.FLOAT:
      case Type.AGGREGATE:
        {
          int m = (Type.bytes(arg.type) + wordsize - 1) / wordsize;
          if (arg.opCode == Op.MEM) {
            if (paramLoc < at.stackParamOffset) {
              // store parameters passed by registers on memory.
              int loc = 0;
              for (int j = 0; j < m; j++) {
                LirNode src = clcvnParamReg(typeParamWord, paramLoc + loc, false);
                alist.add(src);
                post.add
                  (lir.node
                   (Op.SET, typeParamWord, lir.node
                    (Op.MEM, typeParamWord, lir.node
                     (Op.ADD, addrType, arg.kid(0), lir.iconst(addrType, loc))),
                    src));
                loc += wordsize;
              }
            } else {
              // set location to symbol table
              if (arg.kid(0).opCode != Op.FRAME)
                throw new CantHappenException("Malformed aggregate parameter");
              SymAuto var = (SymAuto)((LirSymRef)arg.kid(0)).symbol;
              var.setOffset(clcvnParamOffset(paramLoc));
            }
          } else {
            if (paramLoc < at.stackParamOffset) {
              // store parameters passed by registers on memory.
              SymAuto sym = reserveFrame(".TMP_" + (tmpCount++), arg.type);
              int loc = 0;
              for (int j = 0; j < m; j++) {
                LirNode src = clcvnParamReg(typeParamWord, paramLoc + loc, false);
                alist.add(src);
                post.add
                  (lir.node
                   (Op.SET, typeParamWord, lir.node
                    (Op.MEM, typeParamWord, lir.node
                     (Op.ADD, addrType, lir.symRef(sym), lir.iconst(addrType, loc))),
                    src));
                loc += wordsize;
              }

              post.add(lir.node(Op.SET, arg.type, arg, lir.node
                                (Op.MEM, arg.type, lir.symRef(sym))));
            } else {
              post.add(lir.node(Op.SET, arg.type, arg,
                                clcvnParamMem(arg.type, paramLoc, false)));
            }
          }
          paramLoc += m * wordsize;
        }
        break;
      }
    }
    post.addAllFirst(sync);

    // Put new PROLOGUE operator.
    LirNode[] regArgs = new LirNode[alist.length()];
    {
      BiLink p = alist.first();
      for (int i = 0; i < regArgs.length; i++) {
	regArgs[i] = (LirNode)p.elem();
        p = p.next();
      }
    }
    return lir.node(Op.PROLOGUE, Type.UNKNOWN, regArgs);
  }


  /** Rewrite EPILOGUE **/
  LirNode rewriteEpilogue(LirNode node, BiList pre) {
    if (node.nKids() < 2)
      return node;

    LirNode ret = node.kid(1);
    LirNode reg = clcvnReturnValue(ret.type);

    FunctionAttr at = getFunctionAttr(func);

    if (Type.tag(ret.type) == Type.AGGREGATE) {
      LirNode ptr;
      if (clcvnStructReturnAsFirst()) {
        if (at.retPtr == null)
          throw new CantHappenException("missing pointer to value returned");
        pre.add(lir.node
                (Op.SET, ret.type, lir.node
                 (Op.MEM, ret.type, at.retPtr), ret));
      } else {
        pre.add(lir.node
                (Op.SET, ret.type, lir.node
                 (Op.MEM, ret.type, clcvnStructReturnPtr(false)), ret));
      }
      return node;
    } else {
      pre.add(lir.node(Op.SET, ret.type, reg, ret));
      return lir.node(Op.EPILOGUE, Type.UNKNOWN, node.kid(0), reg);
    }
  }
  
  
  /** Return true if node is a complex one. **/
  boolean isComplex(LirNode node) {
    switch (node.opCode) {
    case Op.INTCONST:
    case Op.REG:
    case Op.STATIC:
    case Op.FRAME:
      return false;
    default:
      return true;
    }
  }


  /** Return physical register node. **/
  LirNode phyReg(String name) {
    return lir.symRef(module.globalSymtab.get(name));
  }

  /** Rewrite CALL. Subclasses may override this. **/
  LirNode rewriteCall(LirNode node, BiList pre, BiList post) {
    BiList list = new BiList();
    LirNode callee = node.kid(0);
    LirNode ret = null;
    if (node.kid(2).nKids() > 0)
      ret = node.kid(2).kid(0);
    int n = node.kid(1).nKids();
    LirNode[] args = new LirNode[n];
    for (int i = 0; i < n; i++)
      args[i] = node.kid(1).kid(i);

    // if callee is not simple, split
    if (isComplex(callee)) {
      LirNode copy = func.newTemp(callee.type);
      list.add(lir.node(Op.SET, callee.type, copy, callee));
      node.setKid(0, copy);
    }

    if (ret != null && Type.tag(ret.type) == Type.AGGREGATE) {
      // treat struct return
      if (ret.opCode != Op.MEM)
        throw new CantHappenException();

      if (clcvnStructReturnAsFirst()) {
        //  standard: pass address of value returned as first argument
        LirNode[] newArgs = new LirNode[1 + args.length];
        newArgs[0] = ret.kid(0);
        for (int i = 0; i < args.length; i++)
          newArgs[i + 1] = args[i];
        args = newArgs;
      } else {
        //  sparc: pass address of value returned as hidden pointer
        list.add(lir.node(Op.SET, ret.kid(0).type,
                          clcvnStructReturnPtr(true), ret.kid(0)));
      }
    }

    // parameters
    int wordsize = Type.bytes(typeParamWord);
    int regLimit = clcvnRegLimit();
    int paramLoc = 0;
    BiList plist = new BiList();
    BiLink ip = plist;
    BiList qlist = new BiList();
    BiLink iq = qlist;

    BiList alist = new BiList();
    for (int i = 0; i < args.length; i++) {
      LirNode arg = args[i];
      ip = plist.first();
      iq = qlist;
      switch (Type.tag(arg.type)) {
      case Type.INT:
        if (isComplex(arg)) {
          LirNode tmp = func.newTemp(arg.type);
          ip.addBefore(lir.node(Op.SET, arg.type, tmp, arg));
          arg = tmp;
        }
        if (Type.bytes(arg.type) > wordsize) {
          int m = (Type.bytes(arg.type) + wordsize - 1) / wordsize;
          for (int j = 0; j < m; j++) {
            LirNode dst = clcvnParamWord(typeParamWord, paramLoc, true);
            BiLink p = ip;
            if (dst.isRegisterOperand()) {
              alist.add(dst);
              p = iq;
            }
            p.addBefore(lir.node
                        (Op.SET, dst.type, dst, clcvnPartialWord(arg, j)));
            paramLoc += wordsize;
          }
        } else {
          LirNode dst = clcvnParamWord(arg.type, paramLoc, true);
          BiLink p = ip;
          if (dst.isRegisterOperand()) {
            alist.add(dst);
            p = iq;
          }
          p.addBefore(lir.node(Op.SET, dst.type, dst, arg));
          paramLoc += wordsize;
        }
        break;

      case Type.FLOAT:
      case Type.AGGREGATE:
        if (arg.opCode == Op.MEM) {
          LirNode lval = arg.kid(0);
          if (isComplex(lval)) {
            LirNode tmp = func.newTemp(addrType);
            ip.addBefore
              (lir.node
               (Op.SET, addrType, tmp, lval));
            lval = tmp;
          }

          // copy to registers & copy to stack
          int size = Type.bytes(arg.type);
          int ptr = 0;
          while (ptr < size && paramLoc < regLimit) {
            LirNode tmp = func.newTemp(typeParamWord);
            LirNode dst = clcvnParamReg(typeParamWord, paramLoc, true);
            alist.add(dst);
            ip.addBefore(lir.node
                         (Op.SET, tmp.type, tmp, lir.node
                          (Op.MEM, tmp.type, lir.node
                           (Op.ADD, addrType, lval,
                            lir.iconst(addrType, ptr)))));
            iq.addBefore(lir.node(Op.SET, dst.type, dst, tmp));
            ptr += wordsize;
            paramLoc += wordsize;
          }

          if (ptr < size) {
            int agtype = Type.type(Type.AGGREGATE, (size - ptr) * 8);
            ip.addBefore
              (lir.node
               (Op.SET, agtype,
                clcvnParamMem(agtype, paramLoc, true), lir.node
                (Op.MEM, agtype, lir.node
                 (Op.ADD, addrType, lval, lir.iconst(addrType, ptr)))));
            paramLoc += (size - ptr + wordsize - 1) & -wordsize;
          }
        }
        else if (Type.tag(arg.type) == Type.FLOAT) {
          if (paramLoc >= regLimit) {
            // put entire object on stack memory
            ip.addBefore
              (lir.node
               (Op.SET, arg.type, clcvnParamMem(arg.type, paramLoc, true), arg));
          } else {
            if (isComplex(arg)) {
              LirNode tmp = func.newTemp(arg.type);
              ip.addBefore(lir.node(Op.SET, arg.type, tmp, arg));
              arg = tmp;
            }
            // copy float object to registers/memory.
            clcvnPassFloatRegMem(paramLoc, arg, ip, iq, alist);
          }
          paramLoc += (Type.bytes(arg.type) + wordsize - 1) & -wordsize;
        }
        else
          throw new CantHappenException();
        break;

      default:
        throw new CantHappenException();

      }
    }

    pre.concatenate(list);
    pre.concatenate(plist);
    pre.concatenate(qlist);

    // Keep required stack space
    FunctionAttr at = getFunctionAttr(func);
    if (paramLoc > at.requiredStack)
      at.requiredStack = paramLoc;

    // return register
    LirNode retReg = null;
    if (ret != null && Type.tag(ret.type) != Type.AGGREGATE)
      retReg = clcvnReturnValue(ret.type);

    // create new CALL node
    LirNode[] regArgs = new LirNode[alist.length()];
    {
      BiLink p = alist.first();
      for (int i = 0; i < regArgs.length; i++) {
	regArgs[i] = (LirNode)p.elem();
        p = p.next();
      }
    }

    try {
      LirNode retNode = lir.node(Op.LIST, Type.UNKNOWN, new LirNode[0]);
      if (retReg != null)
        retNode = lir.node(Op.LIST, Type.UNKNOWN, retReg);
      node = lir.node
        (Op.PARALLEL, Type.UNKNOWN,
         noRescan
         (lir.node
          (Op.CALL, Type.UNKNOWN, node.kid(0),
           lir.node(Op.LIST, Type.UNKNOWN, regArgs),
           retNode)),
         lir.decodeLir(new ImList("CLOBBER", clcvnClobbers()), func, module));
    } catch (SyntaxError e) {
      throw new CantHappenException();
    }

    // copy return value
    if (retReg != null) {
      LirNode tmp = func.newTemp(ret.type);
      post.add(lir.node(Op.SET, ret.type, tmp, retReg));
      post.add(lir.node(Op.SET, ret.type, ret, tmp));
    }

    return node;
  }
  

  
  /*** Following methods should be redefined by *.tmd **/

  /** Return number of registers multiply word-size. **/
  int clcvnRegLimit() {
    return 0;
  }

  /** Return parameter word - either register or memory. **/
  LirNode clcvnParamWord(int type, int location, boolean caller) {
    return clcvnParamMem(type, location, caller);
  }

  /** Return parameter register **/
  LirNode clcvnParamReg(int type, int location, boolean caller) {
    return null;
  }

  /** Return parameter memory **/
  LirNode clcvnParamMem(int type, int location, boolean caller) {
    return null;
  }

  /** Pass floating point number to register/ register and memory. **/
  void clcvnPassFloatRegMem(int location, LirNode arg, BiLink memp, BiLink regp, BiList alist) {
    memp.addBefore
      (lir.node(Op.SET, arg.type, clcvnParamMem(arg.type, location, true), arg));
    int ptr = location;
    while (ptr < clcvnRegLimit()) {
      LirNode dst = clcvnParamWord(typeParamWord, ptr, true);
      alist.add(dst);
      regp.addBefore
        (lir.node(Op.SET, dst.type, dst, clcvnParamMem(dst.type, ptr, true)));
      ptr += Type.bytes(typeParamWord);
    }
  }

  /** Return offset of parameter **/
  int clcvnParamOffset(int location) {
    return 0;
  }

  /** Return return register **/
  LirNode clcvnReturnValue(int type) {
    return null;
  }

  /** Set partial word of object **/
  LirNode clcvnSetPartialWord(LirNode lhs, int part, LirNode rhs) {
    return null;
  }

  /** Return partial word of object **/
  LirNode clcvnPartialWord(LirNode exp, int part) {
    return null;
  }

  /** Return clobber list **/
  ImList clcvnClobbers() {
    return ImList.Empty;
  }

  /** Return true if struct return address **/
  boolean clcvnStructReturnAsFirst() {
    return true;
  }

  /** Create code that sets address of struct to hidden parameter.
      Subclasses may override this. **/
  LirNode clcvnStructReturnPtr(boolean caller) {
    return null;
  }

              

  /** Rewrite JUMPN. Subclasses may override this. **/
  LirNode rewriteJumpn(LirNode node, BiList pre) {
    LirNode exp = node.kid(0);
    LirNode caselist = node.kid(1);
    LirNode defaultlabel = node.kid(2);

    LirNode reg = exp;
    if (exp.opCode != Op.REG) {
      reg = func.newTemp(exp.type);
      pre.add(lir.node(Op.SET, reg.type, reg, exp));
    }
    int n = caselist.nKids();
    for (int i = 0; i < n; i++) {
      LirNode v = caselist.kid(i).kid(0);
      LirNode tlabel = caselist.kid(i).kid(1);
      LirNode flabel = lir.labelRef(func.newLabel());
      pre.add(lir.node
               (Op.JUMPC, Type.UNKNOWN,
                lir.node(Op.TSTEQ, machineParams.typeBool(), reg, v),
                tlabel, flabel));
      pre.add(lir.node(Op.DEFLABEL, Type.UNKNOWN, flabel));
    }
    return lir.node(Op.JUMP, Type.UNKNOWN, defaultlabel);
  }


  /** Rewrite ASM **/
  LirNode rewriteAsm(LirNode node, BiList pre, BiList post) {
    LirNode inList = node.kid(1);
    LirNode outList = node.kid(2);
    LirNode inoutList = node.kid(3);

    ImList argtype = null, clobber = null;
    for (ImList p = node.opt.scanOpt(); !p.atEnd(); p = p.next().scanOpt()) {
      if (p.elem() == "&argtype")
        argtype = (ImList)p.elem2nd();
      else if (p.elem() == "&clobber")
        clobber = (ImList)p.elem2nd();
      else
        throw new CantHappenException("asm: unknown option: " + p);
    }

    if (argtype == null && clobber == null)
      return node;

    ImList p = argtype;

    for (int i = 0; i < inList.nKids(); i++) {
      if (p.atEnd())
        throw new CantHappenException("asm: argtype deficit");
      String type = (String)p.elem();
      if (type.charAt(0) == '%')
        inList.setKid(i, loadReg(inList.kid(i), type, pre));
      p = p.next();
    }

    for (int i = 0; i < outList.nKids(); i++) {
      if (p.atEnd())
        throw new CantHappenException("asm: argtype deficit");
      String type = (String)p.elem();
      if (type.charAt(0) == 'w')
        type = type.substring(1);
      if (type.charAt(0) == '%')
        outList.setKid(i, restoreReg(outList.kid(i), type, post));
      p = p.next();
    }

    for (int i = 0; i < inoutList.nKids(); i++) {
      if (p.atEnd())
        throw new CantHappenException("asm: argtype deficit");
      String type = (String)p.elem();
      if (type.charAt(0) == 'm')
        type = type.substring(1);
      if (type.charAt(0) == '%')
        inoutList.setKid(i, loadRestoreReg(inoutList.kid(i), type, pre, post));
      p = p.next();
    }

    if (!p.atEnd())
      throw new CantHappenException("asm: argtype surplus");

    return lir.operator
      (Op.PARALLEL, Type.UNKNOWN,
       lir.node
       (Op.ASM, Type.UNKNOWN,
        new LirNode[]{node.kid(0), inList, outList, inoutList}),
       lir.node(Op.CLOBBER, Type.UNKNOWN, parseClobber(clobber)),
       ImList.list("&use-after-def", "&clobber-after-def"));
  }


  /** Convert list of registers into vector of REG-expressions. **/
  private LirNode[] parseClobber(ImList clobbers) {
    if (clobbers == null)
      return new LirNode[0];

    LirNode[] vec = new LirNode[clobbers.length()];
    int i = 0;
    for (ImList p = clobbers; !p.atEnd(); p = p.next()) {
      Symbol reg = func.getSymbol((String)p.elem());
      vec[i++] = reg != null ? lir.symRef(reg)
                             : lir.node(Op.UNDEFINED, Type.UNKNOWN);
    }
    return vec;
  }

  private LirNode asPhysicalRegister(String name) {
    Symbol sym = func.getSymbol(name);
    if (sym == null)
      return null;
    if (false) debOut.println("asPhysicalRegister: " + sym);
    return lir.symRef(sym);
  }

  private LirNode loadReg(LirNode src, String regset, BiList pre) {
    LirNode reg = asPhysicalRegister(regset);
    if (reg != null) {
      pre.add(lir.node(Op.SET, reg.type, reg, src));
    } else {
      reg = func.newTemp(src.type);
      pre.add(lir.node(Op.SET, reg.type, reg, src));
      setRegsetOf(reg, ("*reg-" + regset.substring(1) + "*").intern());
      setDontspill(reg);
    }
    return reg;
  }

  private LirNode restoreReg(LirNode dst, String regset, BiList post) {
    LirNode reg = asPhysicalRegister(regset);
    if (reg != null) {
      post.add(lir.node(Op.SET, dst.type, dst, reg));
    } else {
      reg = func.newTemp(dst.type);
      post.add(lir.node(Op.SET, dst.type, dst, reg));
      setRegsetOf(reg, ("*reg-" + regset.substring(1) + "*").intern());
      setDontspill(reg);
    }
    return reg;
  }

  private LirNode loadRestoreReg(LirNode src, String regset,
                                 BiList pre, BiList post) {
    LirNode reg = asPhysicalRegister(regset);
    if (reg != null) {
      pre.add(lir.node(Op.SET, reg.type, reg, src));
      post.add(lir.node(Op.SET, src.type, src, reg));
    } else {
      reg = func.newTemp(src.type);
      pre.add(lir.node(Op.SET, reg.type, reg, src));
      post.add(lir.node(Op.SET, src.type, src, reg));
      setRegsetOf(reg, ("*reg-" + regset.substring(1) + "*").intern());
      setDontspill(reg);
    }
    return reg;
  }


  /** Return the number of operands excluding CLOBBER and USE. **/
  int nActualOperands(LirNode node) {
    int n = node.nKids();
    if (node.opCode == Op.PARALLEL) {
      for (int i = 0; i < n; i++) {
        switch (node.kid(i).opCode) {
        case Op.CLOBBER:
        case Op.USE:
          return i;
        }
      }
    }
    return n;
  }


  /** Rewrite a function so that
   **  each LIR tree corresponds to single machine instruction. **/
  public void instructionSelection(Function f) {
    func = f;
    lir = f.newLir;

    if (root.traceOK("TMD", 1)) {
      debOut.println();
      debOut.println("Instruction Selection:");
    }

    processFrames();

    initLabeling(lir);
    // stateVec = new State[lir.idBound()];
    
    for (BiLink p = f.flowGraph().basicBlkList.first(); !p.atEnd(); p = p.next()) {
      BasicBlk blk = (BasicBlk)p.elem();

      BiList newInstr = new BiList(); // put new instructions here
      for (BiLink q = blk.instrList().first(); !q.atEnd(); q = q.next()) {
        LirNode ins = (LirNode)q.elem();

        switch (ins.opCode) {
        case Op.PROLOGUE:
        case Op.EPILOGUE:
        case Op.LINE:
        case Op.INFO:
          // For the time being do not touch them
          newInstr.add(ins);
          break;

        case Op.PARALLEL:
          if (ins.kid(0).opCode == Op.ASM) {
            newInstr.add(ins);
            break;
          }
        // fall thru
        default:
          if (root.traceOK("TMD", 1)) {
            debOut.println();
            debOut.println("Matching: " + ins);
          }
          try {
            // If PARALLEL has only one operand, peel it
            LirNode origIns = ins;
            if (ins.opCode == Op.PARALLEL && nActualOperands(ins) <= 1)
              ins = ins.kid(0);

            labelTree(ins);
            if (root.traceOK("TMD", 2))
              printLabel(ins, "");
            Match tree = reduce(ins, startNT()).skipNonOpRules();
            tree = tree.removeSet(true);
            tree.decompLir(newInstr);

            if (origIns != ins) {
              // recover original parallel instruction.
              LirNode lastop = (LirNode)newInstr.last().elem();
              if (lastop.opCode != Op.PARALLEL) {
                LirNode node = lir.makeShallowCopy(origIns);
                node.setKid(0, lastop);
                newInstr.last().setElem(node);
              }
            }

          } catch(NoMatchException e) {
            debOut.println();
            debOut.println("No Match for " + ins);
            debOut.println("State:");
            printLabel(ins, "");
            throw new Error("compilation aborted.");
          }
          break;
        }
      }

      if (false) {
        debOut.println("Rewritten LIR for block #" + blk.id + ":");
        for (BiLink q = newInstr.first(); !q.atEnd(); q = q.next())
          debOut.println("  " + (LirNode)q.elem());
      }

      blk.setInstrList(newInstr);
    }

    func.touch();

    func = null;
  }



  /** Generate header on assembly file. **/
  public void genHeader(Module mod) {
    emitIdent(asmWriter, "Coins Compiler version: coins-"
              + coins.Version.version + " + " + BackVersion.version);
    emitComment(asmWriter, "JavaCG for target:" + machineName
                       + " convention:" + convention);
    emitBeginningOfModule(mod, asmWriter);
  }



  public LocalTransformer convToAsm() {
    return new LocalTransformer() {
        public boolean doIt(Function func, ImList args) {
          convToAsm(func);
          return true;
        }

        public boolean doIt(Data data, ImList args) {
          convToAsm(data);
          return true;
        }

        public String name() { return "ConvToAsm"; }

        public String subject() { return "Converting to Assembly Language"; }
      };
  }


  /** Convert FUNCTION (already instruction-selected) to assembly code. **/
  private void convToAsm(Function f) {
    func = f;
    lir = f.newLir;

    BiList list = buildCode(f);

    if (root.traceOK("TMD", 1)) {
      debOut.println();
      debOut.println("After buildCode (S-expression assembly code generaion):");
      debOut.println("Function \"" + f.symbol.name + "\":");
      for (BiLink p = list.first(); !p.atEnd(); p = p.next())
        debOut.println("  " + p.elem());
      debOut.println("End Function");
      debOut.println();
    }

    // Peep-hole optimization goes here
    peepHoleOpt(list);

    // Final Code Emission.
    asmWriter.println();
    if (root.sourceDebugInfo)
      asmWriter.println(emitTop(ImList.list("line", "" + func.sourceLineNo)));

    emitSegment(asmWriter, f.symbol.segment);
    emitAlign(asmWriter, f.symbol.boundary);
    emitLinkage(asmWriter, f.symbol);
    emitCodeLabel(asmWriter, makeAsmSymbol(f.symbol.name));

    emitCode(list, asmWriter);
    asmWriter.println();

    func = null;
  }


  /** Emit named constant. **/
  public void emitNamedConst(String name, LirNode value) {
    emitSegment(asmWriter, segmentForConst());
    emitAlign(asmWriter, alignForType(value.type));
    emitDataLabel(asmWriter, name);
    emitData(asmWriter, value.type, value);
  }


  /** Convert FUNCTION to S-expression assembly code. **/
  BiList buildCode(Function f) {
    FlowGraph flowGraph = f.flowGraph();

    if (root.traceOK("TMD", 1)) {
      debOut.println();
      debOut.println("Final Code Emission:");
    }

    initLabeling(lir);
    // stateVec = new State[lir.idBound()];
    BiList asmList = new BiList();

    boolean firstblk = true;
    for (BiLink p = flowGraph.basicBlkList.first(); !p.atEnd(); p = p.next()) {
      BasicBlk blk = (BasicBlk)p.elem();

      if (!firstblk)
        asmList.add(ImList.list("deflabel", blk.label()));
      firstblk = false;

      BiLink delayOpLink = null;
      for (BiLink q = blk.instrList().first(); !q.atEnd(); q = q.next()) {
        LirNode ins = (LirNode)q.elem();

        switch (ins.opCode) {
        case Op.PROLOGUE:
          asmList.add(ImList.list("prologue", func));
          break;

        case Op.EPILOGUE:
          String rettype = "normal";
          if (f.origEpilogue.nKids() >= 2
              && Type.tag(f.origEpilogue.kid(1).type) == Type.AGGREGATE)
            rettype = "aggregate";
          asmList.add(ImList.list("epilogue", func, rettype));
          break;

        case Op.INFO:
          // #pragma ignored
          break;

        case Op.LINE:
          asmList.add(ImList.list("line", ins.kid(0)));
          break;

        case Op.PARALLEL:
          if (ins.kid(0).opCode == Op.ASM) {
            asmList.add(buildAsm(ins.kid(0)));
            break;
          }
        // fall thru
        default:
          if (root.traceOK("TMD", 1)) {
            debOut.println();
            debOut.println("Matching: " + ins);
          }
          try {
            boolean delayOp = (ins.opt.locate("&delay") != null);

            // If PARALLEL has only one operand, peel it
            if (ins.opCode == Op.PARALLEL && nActualOperands(ins) <= 1)
              ins = ins.kid(0);

            labelTree(ins);
            Match tree = reduce(ins, startNT()).skipNonOpRules();
            tree = tree.removeSet(false);
            if (root.traceOK("TMD", 1))
              tree.printIt(debOut);
            ImList codeList = tree.quiltCode();
            for (ImList s = codeList; !s.atEnd(); s = s.next()) {
              if (root.traceOK("TMD", 3))
                debOut.println(s.elem().toString());
              if (s.elem() instanceof ImList
                  && ((ImList)s.elem()).elem() == "delayslot"
                  && delayOpLink != null) {
                // Move delay instruction to here.
                Object op = delayOpLink.elem();
                delayOpLink.unlink();
                asmList.add(op);
                delayOpLink = null;
              } else {
                asmList.add(s.elem());
              }
            }
            if (delayOp)
              delayOpLink = asmList.last();

          } catch(NoMatchException e) {
            debOut.println();
            debOut.println("No Match for " + ins);
            debOut.println("State:");
            printLabel(ins, "");
            throw new Error("compilation aborted.");
          }
          break;
        }
      }
    }

    return asmList;
  }


  private ImList buildAsm(LirNode ins) {
    return ImList.list
      ("genasm", ((LirString)ins.kid(0)).string,
       (listify(ins.kid(1)).append
        (listify(ins.kid(2)).append(listify(ins.kid(3))))) );
  }
  
  private ImList listify(LirNode ins) {
    ImList list = ImList.Empty;
    int n = ins.nKids();
    while (--n >= 0)
      list = new ImList(quiltAsmOperand(ins.kid(n)), list);
    return list;
  }

  private Object quiltAsmOperand(LirNode node) {
    switch (node.opCode) {
    case Op.ADD:
      if (node.kid(0).opCode == Op.REG && node.kid(1).opCode == Op.INTCONST)
        return new Long(((LirIconst)node.kid(1)).signedValue());
    case Op.SUB:
      if (node.kid(0).opCode == Op.REG && node.kid(1).opCode == Op.INTCONST)
        return new Long(-((LirIconst)node.kid(1)).signedValue());
    }
    return quiltLir(node);
  }

  public void prepareCodeInfo(Function f) {
    func = f;
    lir = f.newLir;
    initLabeling(lir);
  }

  /** Return cost and misc. information about instruction tree.
   ** @param ins instruction L-expression tree which are alpready reduced.
   ** @return ImList of (hasDelaySlot, cost) **/
  public ImList codeInfo(LirNode ins) {
    try {
      // If PARALLEL has only one operand, peel it
      if (ins.opCode == Op.PARALLEL && nActualOperands(ins) <= 1)
        ins = ins.kid(0);

      labelTree(ins);
      Match tree = reduce(ins, startNT()).skipNonOpRules();
      tree = tree.removeSet(false);
      ImList codeList = tree.quiltCode();
      return ImList.list(new Boolean(tree.rule.hasDelaySlot),
                         new Integer(tree.cost1),
                         new Integer(codeList.length()));
      // return ImList.list(new Boolean(tree.rule.hasDelaySlot), new Integer(tree.cost));

    } catch(NoMatchException e) {
      debOut.println();
      debOut.println("No Match for " + ins);
      debOut.println("State:");
      printLabel(ins, "");
      throw new Error("compilation aborted.");
    }
  }


  /** Expand LirNode **/
  Object quiltLirDefault(LirNode node) {
    switch (node.opCode) {
    case Op.SUBREG:
      node = node.kid(0);
      // fall thru
    case Op.REG:
      return ((LirSymRef)node).symbol.name;
    case Op.STATIC:
      return makeAsmSymbol(((LirSymRef)node).symbol.name);
    case Op.INTCONST:
      return new Long(((LirIconst)node).signedValue());
    case Op.FLOATCONST:
      return new Double(((LirFconst)node).value);
    case Op.LABEL:
      return ((LirLabelRef)node).label.name();
    default:
      return node.toString();
    }
  }


  /** Peep-hole optimizer.
   ** This method is dummy; Subclasses may override this. **/
  void peepHoleOpt(BiList list) {}


  /** Emit S-expression assembly code to file. **/
  void emitCode(BiList asmList, PrintWriter file) {
    for (BiLink p = asmList.first(); !p.atEnd(); p = p.next()) {
      String line = emitObjectX(p.elem(), true);
      if (line.indexOf('\n') < 0)
        file.println(line);
      else {
        // 2 or more lines.
        int len = line.length();
        for (int i = 0; i < len; ) {
          int j = line.indexOf('\n', i);
          if (j < 0) j = len;
          file.println(line.substring(i, j));
          i = j + 1;
        }
      }
    }
  }


  /** Convert toplevel S-expression assembly code to string. **/
  String emitTop(Object x) { return emitObjectX(x, true); }

  /** Convert S-expression assembly code to string. **/
  String emitObject(Object x) { return emitObjectX(x, false); }

  /** Convert S-expression assembly code to string.
   ** @param top true if this is a top-level. **/
  String emitObjectX(Object x, boolean top) {
    if (x instanceof ImList)
      return emitList((ImList)x, top);
    else if (x instanceof LirNode) // obsoleted
      return emitLir((LirNode)x);
    else
      return x.toString();
  }


  /** Default expansion action for S-expression. **/
  String emitListDefault(ImList form, boolean topLevel) {
    if (form.atEnd())
      return "";

    // Default action for (deflabel) macro.
    if (form.elem() == "deflabel") {
      if (form.elem2nd() instanceof LirLabelRef)
        return makeLabelDef(((LirLabelRef)form.elem2nd()).label.name());
      else
        return makeLabelDef(form.elem2nd().toString());
    }

    StringBuffer res = new StringBuffer();
    if (topLevel)
      res.append("\t");
    res.append((String)form.elem());
    boolean first = true;
    for (form = form.next(); !form.atEnd(); form = form.next()) {
      if (first)
        res.append(topLevel ? "\t" : "(");
      else
        res.append(",");
      res.append(emitObject(form.elem()));
      first = false;
    }
    if (!topLevel && !first)
      res.append(")");
    return res.toString();
  }


  /** Default expansion action for LirNode. (obsoleted) **/
  String emitLirDefault(LirNode node) {
    switch (node.opCode) {
    case Op.SUBREG:
      node = node.kid(0);
      // fall thru
    case Op.REG:
      return ((LirSymRef)node).symbol.name;
    case Op.STATIC:
      return makeAsmSymbol(((LirSymRef)node).symbol.name);
    case Op.INTCONST:
      return Long.toString(((LirIconst)node).signedValue());
    case Op.FLOATCONST:
      return Double.toString(((LirFconst)node).value);
    case Op.LABEL:
      return ((LirLabelRef)node).label.name();
    default:
      return node.toString();
    }
  }




  /** Convert DATA to assembly code. **/
  private void convToAsm(Data data) {
    if (data.components[0].opCode == Op.SPACE) {
      emitCommon(asmWriter, data.symbol,
                 (int)((LirIconst)data.components[0].kid(0)).value);
    } else {
      emitSegment(asmWriter, data.symbol.segment);
      emitAlign(asmWriter, data.symbol.boundary);
      emitLinkage(asmWriter, data.symbol);
      emitDataLabel(asmWriter, makeAsmSymbol(data.symbol.name));

      for (int i = 0; i < data.components.length; i++) {
        LirNode node = data.components[i];
        switch (node.opCode) {
        case Op.ZEROS:
          emitZeros(asmWriter, (int)((LirIconst)node.kid(0)).value);
          break;
        case Op.LIST:
          int n = node.nKids();
          for (int j = 0; j < n; j++)
            emitData(asmWriter, node.type, node.kid(j));
          break;

        case Op.SPACE:
          throw new CantHappenException("SPACE in middle");

        default:
          throw new CantHappenException();
        }
      }
    }
  }


  /** Generate trailer on assembly file. **/
  public void genTrailer(Module mod) {
    emitEndOfModule(mod, asmWriter);
  }


  /** Close output stream. **/
  public void close() {
    asmWriter.close();
    notifyEndToPostProcessor();
  }



  /** Change default segment to <code>segment</code>. **/
  void emitSegment(PrintWriter file, String segment) {
    if (currentSegment == null || !currentSegment.equals(segment)) {
      if (currentSegment != null)
        emitEndOfSegment(file, currentSegment);
      emitBeginningOfSegment(file, segment);
      currentSegment = segment;
    }
  }


  /** Print label of tree <code>t</code>. **/
  void printLabel(LirNode t, String indent) {
    debOut.println(indent + t + " : " + showLabel(t)); // stateVec[t.id]);
    int n = t.nKids();
    for (int i = 0; i < n; i++)
      printLabel(t.kid(i), indent + "  ");
  }



  /** Reduce labeled tree <code>t</code>
      with goal nonterminal <code>goal</code>. **/
  Match reduce(LirNode t, int goal) throws NoMatchException {
    Rule rule = getRule(t, goal); // stateVec[t.id].ruleFor(goal);
    int cost1 = getCost1(t, goal); // stateVec[t.id].cost1[goal];
    int cost2 = getCost2(t, goal); // stateVec[t.id].cost2[goal];
    if (rule == null)
      throw new NoMatchException();

    Match[] mkid = null;
    int n = rule.subgoals.length;
    mkid = new Match[n];
    if (rule.isChain)
      mkid[0] = reduce(t, rule.subgoals[0]);
    else {
      for (int i = 0; i < n; i++)
        mkid[i] = reduce(t.kid(i), rule.subgoals[i]);
    }
    return new Match(t, mkid, rule, cost1, cost2);
  }




  /** Used by subclasses(*.tmd) **/

  /** Return true if node is a simple variable. **/
  boolean isSimple(LirNode node) {
    if (node.opCode == Op.REG)
      return true;
    if (node.opCode == Op.MEM) {
      switch (node.kid(0).opCode) {
      case Op.STATIC:
      case Op.FRAME:
        return true;
      }
    }
    return false;
  }

  private static final double FLT2_32 = 4294967296.0; // 2^32
  private static final double FLT2_64 = 18446744073709551616.0; // 2^64


  /** Rewrite CONVUF **/
  LirNode rewriteCONVUF(LirNode node, BiList pre) {
    LirNode src = node.kid(0);
    if (!isSimple(src)) {
      src = func.newTemp(src.type);
      pre.add(lir.node(Op.SET, src.type, src, node.kid(0)));
    }
    LirNode dst = func.newTemp(node.type);
    pre.add(lir.node
            (Op.SET, dst.type, dst, lir.node
             (Op.CONVSF, dst.type, src)));
    Label tlabel = func.newLabel();
    Label flabel = func.newLabel();
    pre.add(lir.node
            (Op.JUMPC, Type.UNKNOWN, lir.node
             (Op.TSTGES, I32, src, lir.iconst(src.type, 0)),
             lir.labelRef(tlabel), lir.labelRef(flabel)));
    pre.add(lir.node
            (Op.DEFLABEL, Type.UNKNOWN, lir.labelRef(flabel)));
    pre.add(lir.node
            (Op.SET, dst.type, dst, lir.node
             (Op.ADD, dst.type, dst,
              lir.fconst(dst.type, Type.bits(src.type) > 32 ? FLT2_64 : FLT2_32))));
    pre.add(lir.node
            (Op.DEFLABEL, Type.UNKNOWN, lir.labelRef(tlabel)));
    return dst;
  }


  private static final double FLT2_31 = 2147483648.0;
  private static final double FLT2_63 = 9223372036854775808.0;


  /** Rewrite CONVFU(x) to:
   **  if (x < 2147483648) CONVFS(x)
   **  else CONVFS(x - 2147483648) + 214748648 **/
  LirNode rewriteCONVFU(LirNode node, BiList pre) {
    LirNode src = node.kid(0);
    if (src.opCode != Op.REG) {
      src = func.newTemp(src.type);
      pre.add(lir.node(Op.SET, src.type, src, node.kid(0)));
    }
    Label tlabel = func.newLabel();
    Label flabel = func.newLabel();
    Label elabel = func.newLabel();
    LirNode dst = func.newTemp(node.type);
    double boundary = Type.bits(dst.type) > 32 ? FLT2_63 : FLT2_31;
    long bias = Type.bits(dst.type) > 32 ? -9223372036854775808L : -2147483648;
    pre.add(lir.node
            (Op.JUMPC, Type.UNKNOWN, lir.node
             (Op.TSTGES, I32, src,
              lir.fconst(src.type, boundary)),
             lir.labelRef(tlabel), lir.labelRef(flabel)));
    pre.add(lir.node
            (Op.DEFLABEL, Type.UNKNOWN, lir.labelRef(tlabel)));
    pre.add(lir.node
            (Op.SET, dst.type, dst, lir.node
             (Op.ADD, dst.type, lir.node
              (Op.CONVFS, dst.type, lir.node
               (Op.SUB, src.type, src, lir.fconst(src.type, boundary))),
              lir.iconst(dst.type, bias))));
    pre.add(lir.node(Op.JUMP, Type.UNKNOWN, lir.labelRef(elabel)));
    pre.add(lir.node(Op.DEFLABEL, Type.UNKNOWN, lir.labelRef(flabel)));
    pre.add(lir.node
            (Op.SET, dst.type, dst, lir.node
             (Op.CONVFS, dst.type, src)));
    pre.add(lir.node(Op.DEFLABEL, Type.UNKNOWN, lir.labelRef(elabel)));
    return dst;
  }



  /** Following methods must be implemented by subclasses. **/

  /** Methods automatically redefined by Tmd2Java. **/

  /** Initialize for labeling. **/
  abstract void initLabeling(LirFactory lir);

  /** Return label state **/
  abstract String showLabel(LirNode t);

  /** Label LIR tree t. **/
  abstract void labelTree(LirNode t);

  /** Return the chosen rule for LIR node t and goal nonterminal goal. **/
  abstract Rule getRule(LirNode t, int goal);

  /** Return the cost for LIR node t and goal nonterminal goal. **/
  abstract int getCost1(LirNode t, int goal);
  abstract int getCost2(LirNode t, int goal);

  /** Return start nonterminal symbol. **/
  abstract int startNT();

  /** Return default register set for type. **/
  abstract String defaultRegsetForType(int type);

  /** Return visible name of nonterminal <code>nt</code>. **/
  /* abstract String nameOfNT(int nt); */

  /** Expand emit-macro for list form. **/
  abstract String emitList(ImList form, boolean topLevel);

  /** Expand emit-macro for LirNode. **/
  abstract String emitLir(LirNode node);

  /** Expand building macro for list form. **/
  abstract Object expandBuildMacro(ImList form);

  /** Expand building macro for LirNode. **/
  abstract Object quiltLir(LirNode node);


  /**
   ** Methods you should redefine.
   ** Following implementations are not for use.
   **/


  /** Rewrite FRAME node to target machine form. **/
  LirNode rewriteFrame(LirNode node) {
    Symbol fp = func.module.globalSymtab.get("%fp");
    int off = ((SymAuto)((LirSymRef)node).symbol).offset();
    return lir.node
      (Op.ADD, node.type, lir.symRef(fp), lir.iconst(I32, (long)off));
  }


  /** Print .ident in assembly language. **/
  void emitIdent(PrintWriter out, String ident) {
    out.println(" .ident \"" + ident + "\"");
  }

  /** print comment in assembly language. **/
  void emitComment(PrintWriter out, String comment) {
    out.println("/* " + comment + " */");
  }

  /** Emit beginning of module **/
  void emitBeginningOfModule(Module mod, PrintWriter out) { /* do nothing */ }

  /** Emit ending of module **/
  void emitEndOfModule(Module mod, PrintWriter out) { /* do nothing */ }

  /** Emit beginning of segment **/
  void emitBeginningOfSegment(PrintWriter out, String segment) {
    out.println("\t.section \"" + segment + "\"");
  }

  /** Emit end of segment **/
  void emitEndOfSegment(PrintWriter out, String segment) {
    /* do nothing */
  }

  /** Emit linkage information of symbol **/
  void emitLinkage(PrintWriter out, SymStatic symbol) {
    if (symbol.linkage == "XDEF")
      out.println("\t.global\t" + makeAsmSymbol(symbol.name));
  }

  /** Emit label for data **/
  void emitDataLabel(PrintWriter out, String label) {
    out.println(makeLabelDef(label));
  }

  /** Emit label for code **/
  void emitCodeLabel(PrintWriter out, String label) {
    out.println(makeLabelDef(label));
  }

  /** Emit align (Bytesize) **/
  void emitAlign(PrintWriter out, int align) {
    out.println("\t.align\t" + align);
  }

  /** Emit data common **/
  void emitCommon(PrintWriter out, SymStatic symbol, int bytes) {
    if (symbol.linkage == "LDEF")
      out.println("\t.lcomm\t" + makeAsmSymbol(symbol.name)
                  + "," + bytes);
    else
      out.println("\t.comm\t"  + makeAsmSymbol(symbol.name)
                  + ","  + bytes + "," + symbol.boundary);
  }

  /** Emit data zeros **/
  void emitZeros(PrintWriter out, int bytes) {
    if (bytes > 0)
      out.println("\t.skip\t" + bytes);
  }


  /** Convert address expression to external form. **/
  class LexpToString implements LirVisitor {
    private StringBuffer buf;

    String convert(LirNode node) {
      buf = new StringBuffer();
      node.accept(this);
      return buf.toString();
    }

    public void visit(LirIconst node) {
      buf.append(node.signedValue());
    }

    public void visit(LirSymRef node) {
      buf.append(makeAsmSymbol(node.symbol.name));
    }

    public void visit(LirLabelRef node) {
      buf.append(node.label.name());
    }

    public void visit(LirBinOp node) {
      switch (node.opCode) {
      case Op.ADD:
        node.kid(0).accept(this);
        buf.append('+');
        node.kid(1).accept(this);
        break;
      case Op.SUB:
        node.kid(0).accept(this);
        buf.append('-');
        node.kid(1).accept(this);
        break;
      default:
        throw new CantHappenException("not an address expression: " + node);
      }
    }
      
    public void visit(LirNaryOp node) {
      throw new CantHappenException("not an address expression: " + node);
    }

    public void visit(LirUnaOp node) {
      throw new CantHappenException("not an address expression: " + node);
    }

    public void visit(LirFconst node) {
      throw new CantHappenException("not an address expression: " + node);
    }
  }

  LexpToString lexpConv = new LexpToString();


  /** Emit asm instruction. **/
  String emitAsmCode(String format, ImList args) {
    StringBuffer buf = new StringBuffer();
    int n = format.length();
    for (int i = 0; i < n; ) {
      char c = format.charAt(i++);
      if (c == '%' && i < n) {
        c = format.charAt(i++);
        if (c == '%')
          buf.append(c);
        else if (Character.isDigit(c)) {
          buf.append(args.elem(c - '1').toString());
        }
        else {
          buf.append('%');
          buf.append(c);
        }
      } else
        buf.append(c);
    }
    return buf.toString();
  }


  /** Emit data for big endian machine.
   ** Redefine this method if your machine is little endian. **/
  void emitData(PrintWriter out, int type, LirNode node) {
    if (type == I64) {
      long v = ((LirIconst)node).signedValue();
      out.println("\t.long\t" + ((v >> 32) & 0xffffffffL)
                  + "," + (v & 0xffffffffL));
    }
    else if (type == I32) {
      out.println("\t.long\t" + lexpConv.convert(node));
    }
    else if (type == I16) {
      out.println("\t.short\t" + ((LirIconst)node).signedValue());
    }
    else if (type == I8) {
      out.println("\t.byte\t" + ((LirIconst)node).signedValue());
    }
    else if (type == F64) {
      double value = ((LirFconst)node).value;
      long bits = Double.doubleToLongBits(value);
      out.println("\t.long\t0x" + Long.toString((bits >> 32) & 0xffffffffL, 16)
                  + ",0x" + Long.toString(bits & 0xffffffffL, 16)
                  + " /* " + value + " */");
    }
    else if (type == F32) {
      double value = ((LirFconst)node).value;
      long bits = Float.floatToIntBits((float)value);
      out.println("\t.long\t0x" + Long.toString(bits & 0xffffffffL, 16)
                  + " /* " + value + " */");
    }
    else {
      throw new CantHappenException("unknown type: " + type);
    }
  }

  /** Prepare new function attribute information. **/
  FunctionAttr newFunctionAttr(Function f) {
    FunctionAttr at = new FunctionAttr(f);
    at.requiredStack = clcvnRegLimit();
    return at;
  }

  /** Return alignment for type. **/
  public int alignForType(int type) { return 4; }

  /** Return segment for read-only constant. **/
  String segmentForConst() { return ".text"; }

  /** Make symbol in assembly language form.
   ** Prepend '_' in case of old a.out binary. **/
  String makeAsmSymbol(String symbol) {
    return symbol;
  }

  /** Return label definition **/
  String makeLabelDef(String label) {
    return label + ":";
  }

  /** Return machineParams **/
  public MachineParams getMachineParams() {
    return machineParams;
  }
}
