/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend;

import coins.backend.cfg.BasicBlk;
import coins.backend.cfg.FlowGraph;
import coins.backend.lir.LirFactory;
import coins.backend.lir.LirNode;
import coins.backend.opt.JumpOpt;
import coins.backend.opt.JumpCanon;
import coins.backend.regalo.LiveRange;
import coins.backend.regalo.RegisterAllocation;
import coins.backend.sym.Label;
import coins.backend.sym.SymTab;
import coins.backend.sym.Symbol;
import coins.backend.sym.SymAuto;
import coins.backend.util.BiLink;
import coins.backend.util.BiList;
import coins.backend.util.ImList;
import coins.backend.util.QuotedString;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/** Represent a function. */
public class Function extends ModuleElement {

  /** Factory object for creating LirNode **/
  public final LirFactory newLir;

  /** Local symbol table */
  public final SymTab localSymtab;

  /** Control flow graph (in case of CFG mode)
   **  @deprecated  use flowGraph() instead.  */
  private FlowGraph flowGraph;

  /** List of L-expression (in case of non-CFG mode) **/
  private BiList lirList;


  /** Original prologue instruction. **/
  public final LirNode origPrologue;

  /** Original epilogue instruction. **/
  public final LirNode origEpilogue;

  //Begin(2010.1)
  /** RegisterAllocation trigger. **/
  public static LocalTransformer registerAllocationTrig=RegisterAllocation.trig;
  //End(2010.1)

  /** Label table */
  private Map labelTable = new HashMap();

  /** LIR Node LABEL variant serial number generator */
  private int labelVariantCounter = 1;
  
  /** Result of analyses, instances of LocalAnalysis */
  private Map analyses = new HashMap();


  /** Time Stamp of this function. **/
  private int timeStamp = 0;

  /** Form Status: FORM_NORMAL, FORM_SSA, FORM_SSA2 */
  public static final int FORM_NORMAL = 0;
  public static final int FORM_SSA = 1;
  public static final int FORM_SSA2 = 2;
  private int form = FORM_NORMAL;

  /** Parse S-expression function description and convert to internal form */
  public Function(Module module, ImList ptr)
    throws SyntaxError  {
    super(module, ((QuotedString)ptr.elem2nd()).body);

    newLir = module.newLir;
    symbol.setBody(this);

    localSymtab = new SymTab(module);

    reload(ptr);

    // Save original PROLOGUE/EPILOGUE arguments for later use.

    LirNode inst = null;
    BiLink p = flowGraph().entryBlk().instrList().first();
    LirNode op = (LirNode)p.elem();
    if (op.opCode == Op.PROLOGUE)
      inst = op;
    origPrologue = inst;

    inst = null;
    for (p = flowGraph().exitBlk().instrList().first();
         !p.atEnd(); p = p.next()) {
      op = (LirNode)p.elem();
      if (op.opCode == Op.EPILOGUE)
        inst = op;
    }
    origEpilogue = inst;
  }


  /** Return first instruction block. **/
  public BiList firstInstrList() {
    if (flowGraph != null)
      return flowGraph.entryBlk().instrList();
    else
      return lirList;
  }


  /** Reload new L-function description from sexp. **/
  public void reload(ImList ptr) throws SyntaxError {
    ptr = ptr.next();
    
    // Check name of the function
    if (!symbol.name.equals(((QuotedString)ptr.elem()).body))
      throw new SyntaxError("Expecting function " + symbol.name + "but: "
                            + (QuotedString)ptr.elem());

    ptr = ptr.next();

    // Parse symbol table
    //   make each id unique
    localSymtab.clear();
    ImList symp = (ImList)ptr.elem();
    //## System.out.print(" symp "+symp.toString() + " " + symp.elem()); //###
    if (symp.elem() == Keyword.SYMTAB) {
      for (symp = symp.next(); !symp.atEnd(); symp = symp.next()) {
        ImList def = (ImList)symp.elem();
        if (def.elem2nd() == Keyword.STATIC)
          module.globalSymtab.addSymbol(def);
        else
          localSymtab.addSymbol(def);
      }
      ptr = ptr.next();
    }

    // Parse body part

    lirList = new BiList();
    for (; !ptr.atEnd(); ptr = ptr.next()) {
      ImList stmt = (ImList)ptr.elem();
      //## System.out.print(" stmt "+stmt.toString() + " " + stmt.elem()); //###     
      if (stmt.elem() == Keyword.DATA)
        module.doData(stmt);
      else {
        try {
          LirNode lir = newLir.decodeLir(stmt, this, module);
          if (lir != null) {
            if (lir.opCode != Op.LINE || root.sourceDebugInfo)
              lirList.add(lir);
          }
        } catch (SyntaxError e) {
          throw new SyntaxError(e + ", at: " + stmt);
        } catch (Exception e) {
          throw new CantHappenException(e + ", at: " + stmt);
        }
      }
    }

    flowGraph = null;

    if (root.traceOK("Load", 1)) {
      // Dump function
      root.debOut.println();
      root.debOut.println("Function Just after read:");
      printIt(root.debOut);
    }

  }


  /** Return original prologue instruction. **/

  /** Return Control flow graph of this L-function.
   **  If L-function is not in CFG, changed to CFG automatically. **/
  public FlowGraph flowGraph() {

    if (flowGraph == null) {
      // change to CFG.
      clearLabels();
      flowGraph = new FlowGraph(this, lirList);
      renameLabels();
      purgeAnalysis();
      lirList = null;
    }

    return flowGraph;
  }




  /** Return Instruction list of this L-function.
   **  If L-function is in CFG mode, changed to non-CFG automatically. **/
  public BiList lirList() {
    if (lirList == null) {
      // change CFG to non-CFG.
      lirList = new BiList();

      for (BiLink p = flowGraph.basicBlkList.first(); !p.atEnd(); p = p.next()) {
        BasicBlk blk = (BasicBlk)p.elem();
        lirList.add
          (newLir.node
           (Op.DEFLABEL, Type.UNKNOWN, newLir.labelRef(blk.label())));
        lirList.concatenate(blk.instrList());
      }

      flowGraph = null;
    }
    return lirList;
  }




  /** Built-in Transformers. **/

  /** Launcher object for CFG transformation. **/
  public static ToCFG toCFG = new ToCFG();

  private static class ToCFG implements LocalTransformer {
    public boolean doIt(Function func, ImList args) {
      func.flowGraph();
      return true;
    }

    public boolean doIt(Data data, ImList args) { return true; }

    public String name() { return "ToCFG"; }

    public String subject() { return "Conversion to Control Flow Graph"; }
  }

    

  /** Transform to linear (non-CFG). **/
  public static ToLinear toLinear = new ToLinear();

  private static class ToLinear implements LocalTransformer {
    public boolean doIt(Function func, ImList args) {
      func.lirList();
      return true;
    }

    public boolean doIt(Data data, ImList args) { return true; }

    public String name() { return "ToLinear"; }

    public String subject() { return "Conversion to Linear (non-CFG) Form"; }
  }



  /** Generate new label and return it. **/
  public Label newLabel() {
    Label label = module.newLabel();
    labelTable.put(label.name(), label);
    return label;
  }


  /** Install new label whose name is <code>name</code> to this function.
   *  If null is given, generate new unique label. */
  public Label internLabel(String name) throws SyntaxError {
    Label label = (Label)labelTable.get(name);
    if (label == null) {
      label = module.lookupLabel(name);
      if (label != null)
        throw new SyntaxError("Label " + name + " conflits");
      label = new Label(name);
      labelTable.put(name, label);
    }
    return label;
  }


  /** Rename labels to final name. **/
  private void renameLabels() {
    for (BiLink p = flowGraph.basicBlkList.first(); !p.atEnd(); p = p.next()) {
      BasicBlk blk = (BasicBlk)p.elem();

      Label label = blk.label();
      String oldName = label.name();
      if (module.lookupLabel(oldName) == null) {
        module.renameLabelToFinal(label);
        labelTable.remove(oldName);
        labelTable.put(label.name(), label);
      }
    }
  }


  /** Remove all label-BasicBlk bindings. **/
  private void clearLabels() {
    Set set = labelTable.entrySet();
    for (Iterator it = set.iterator(); it.hasNext(); ) {
      Map.Entry e = (Map.Entry)it.next();
      Label label = (Label)e.getValue();
      label.clear();
    }
  }

  /** Reserve label variant number <code>variant</code>. **/
  public void reserveLabelVariantNo(int variant) {
    if (variant >= labelVariantCounter)
      labelVariantCounter = variant + 1;
  }



  /** Add local symbol */
  public Symbol addSymbol(String name, int storage, int type,
                          int boundary, int offset, ImList opt) {
    return localSymtab.addSymbol(name, storage, type, boundary, offset, opt);
  }


  /** Add temporary symbol */
  public Symbol addSymbol(Symbol original, int type) {
    return localSymtab.addSymbol(original, type);
  }


  /** Find symbol whose name is <code>name</code>. **/
  public Symbol getSymbol(String name) {
    Symbol sym = localSymtab.get(name);
    if (sym == null)
      sym = module.globalSymtab.get(name);
    return sym;
  }



  /** Return vector of symbols visible from this function. **/
  public Symbol[] symVector() {
    Symbol[] vec = new Symbol[localSymtab.idBound()];
    module.globalSymtab.makeReverseIndex(vec);
    localSymtab.makeReverseIndex(vec);
    return vec;
  }


  /** Return frame size of this function (value is positive). **/
  public int frameSize() {
    int loc = 0;
    for (BiLink p = localSymtab.symbols().first(); !p.atEnd(); p = p.next()) {
      SymAuto var = (SymAuto)p.elem();
      if (var.storage == Storage.FRAME) {
        int off = var.offset();
        if (off != 0 && off < loc)
          loc = off;
      }
    }
    return -loc;
  }


  private int tempCounter = 1;

  /** Create new temporary register variable. **/
  public LirNode newTemp(int type) {
    Symbol newVar = addSymbol(".T" + tempCounter++ + "%",
                              Storage.REG, type, 0, 0, null);
    return newLir.symRef(newVar);
  }

  /** Create new named register variable. **/
  public LirNode newReg(String name, int type) {
    Symbol newVar = addSymbol(name, Storage.REG, type, 0, 0, null);
    return newLir.symRef(newVar);
  }

  /** Create new frame variable. **/
  public LirNode newFrame(String name, int type) {
    if (name == null)
      name = ".TF" + tempCounter++;
    Symbol var = localSymtab.get(name);
    if (var != null) {
      if (var.type != type)
        throw new CantHappenException();
    } else
      var = addSymbol(name, Storage.FRAME, type, 0, 0, null);
    return newLir.symRef(var);
  }




  /** Set form **/
  public void setForm(int form) {
    this.form = form;
  }

  /** Get form **/
  public int form() { return form; }


  /** Return this function's version number. **/
  public int timeStamp() { return timeStamp; }


  /** Notify this function having been modified. **/
  public void touch() { timeStamp++; }


  /** Purge former analysis */
  public void purgeAnalysis() {
    analyses.clear();
  }

  /** Apply some analysis */
  public LocalAnalysis apply(LocalAnalyzer analyzer) {
    long start = 0;
    if (root.GCflush) 
      root.timer.gcReport(root.debOut);
    if (root.dispIntervalTime)
      start = root.timer.getLaptime();

    LocalAnalysis analysis = analyzer.doIt(this);
    analyses.put(analyzer, analysis);

    if (root.dispIntervalTime)
      root.debOut.println("- Function " + symbol.name + ": "
                          + analyzer.name() + ": "
                          + root.timer.getIntervalTime(start)
                          + " lap:" + module.elapsedTime());
    if (root.traceOK(analyzer.name(), 1)) {
      root.debOut.println();
      root.debOut.println("Analysis " + analyzer.name() + ":");
      printIt(root.debOut, new LocalAnalysis[]{ analysis });
    }

    return analysis;
  }

  /** Require analysis. */
  public LocalAnalysis require(LocalAnalyzer analyzer) {
    LocalAnalysis ana = (LocalAnalysis)analyses.get(analyzer);
    if (ana == null || !ana.isUpToDate())
      ana = apply(analyzer);
    return ana;
  }


  /** Apply transformation (generic form). **/
  public boolean apply(Object trans) {
    if (trans instanceof ImList)
      return apply((ImList)trans);
    else if (trans instanceof BiList)
      return apply((BiList)trans);
    else if (trans instanceof Object[])
      return apply((Object[])trans);
    else if (trans instanceof LocalTransformer)
      return apply((LocalTransformer)trans);
    else if (trans instanceof String)
      return apply(root.getHook((String)trans));
    else
      throw new CantHappenException("Unexpected type: " + trans.getClass());
  }


  /** Apply transformation assigned to Hook. **/
  public boolean apply(String hook) {
    Object trans = root.getHook(hook);
    if (trans == null) {
      // Error unless hook name starts with +.
      if (hook.charAt(0) != '+')
        throw new CantHappenException("Undefined Hook: " + hook);
      return true;
    } else
      return apply(trans);
  }

  /** Apply transformations listed in the <code>transList</code>. **/
  public boolean apply(ImList transList) {
    for (ImList p = transList; !p.atEnd(); p = p.next())
      if (!apply(p.elem()))
        return false;
    return true;
  }


  /** Apply transformations listed in the <code>transList</code>. **/
  public boolean apply(BiList transList) {
    for (BiLink p = transList.first(); !p.atEnd(); p = p.next())
      if (!apply(p.elem()))
        return false;
    return true;
  }


  /** Apply transformations listed in the <code>transVector</code>. **/
  public boolean apply(Object[] transVector) {
    for (int i = 0; i < transVector.length; i++)
      if (!apply(transVector[i]))
        return false;
    return true;
  }


  /** Apply some transformation/optimization. */
  public boolean apply(LocalTransformer xformer) {
    return apply(xformer, ImList.list());
  }


  /** Apply some transformation/optimization with argument. */
  public boolean apply(LocalTransformer xformer, ImList args) {

    if (root.traceOK(xformer.name(), 2)) {
      root.debOut.println();
      root.debOut.println("Before " + xformer.name()
                          + " (" + xformer.subject() + "):");
      printIt(root.debOut);
    }
    if (root.GCflush)
      root.timer.gcReport(root.debOut);
    long start = root.timer.getLaptime();

    boolean ok = xformer.doIt(this, args);

    if (root.dispIntervalTime)
      root.debOut.println("- Function " + symbol.name + ": "
                          + xformer.name() + ": "
                          + root.timer.getIntervalTime(start)
                          + " lap:" + module.elapsedTime());

    if (root.traceOK(xformer.name(), 1)) {
      root.debOut.println();
      root.debOut.println("After " + xformer.name()
                          + " (" + xformer.subject() + "):");
      printIt(root.debOut);
    }

    return ok;
  }



  /** Transformer which converts a function to machine instructions. **/
  public static final LocalTransformer toMachineCodeTrig = new LocalTransformer() {
    public boolean doIt(Function func, ImList args) {
      func.toMachineCode();
      return true;
    }

    public boolean doIt(Data data, ImList args) { return true; }
      
    public String name() { return "ToMachineCode"; }
      
    public String subject() { return "Machine Instruction Conversion"; }
      
    };

  private static final int REGALOLIMIT = 7;
 
  /** Convert to machine instructions. **/
  private void toMachineCode() {
    // Separate variables into live-ranges.
    apply(LiveRange.trig);

    // More JumpOpt 
    apply(JumpOpt.trig);

    // Canonicalize JUMPC instructions
    apply(JumpCanon.trig);

    // Call Hook.
    apply("+BeforeFirstInstSel");

    // Instruction selection.
    apply(module.targetMachine.instSelTrig);

    // Call Hook.
    apply("+AfterFirstInstSel");

    int count = 0;
    for (;;) {
      if (count++ >= REGALOLIMIT)
        throw new CantHappenException("Looks like an infinite loop during Register allocation");

      // Allocate registers.
      // Begin(2010.1)
      //if (apply(RegisterAllocation.trig))
      if(apply(registerAllocationTrig))
      //End 2010.1.22
        break;
      
      // Call Hook.
      apply("+BeforeSecondInstSel");

      // Instruction selection.
      apply(module.targetMachine.instSelTrig);

      // Call Hook.
      apply("+AfterSecondInstSel");
    }

    // Remove redundant jumps
    postProcess();
  }



  /** Remove redundant jumps **/
  private void postProcess() {
    BiLink p = flowGraph().basicBlkList.first();
    if (p.atEnd())
      return;

    for (;;) {
      BiLink next = p.next();
      if (next.atEnd())
        break;

      BasicBlk blk = (BasicBlk)p.elem();
      BasicBlk nextBlk = (BasicBlk)next.elem();

      LirNode ins = (LirNode)blk.instrList().last().elem();
      if (ins.opCode == Op.JUMP) {
          Label[] targets = ins.getTargets();
          if (targets[0].basicBlk() == nextBlk)
            blk.instrList().last().unlink();
      }

      p = next;
    }
  }




  /** Convert to external LIR format. **/
  public Object toSexp() {
    ImList list = ImList.Empty;
    list = new ImList("FUNCTION", list);
    list = new ImList(new QuotedString(symbol.name), list);
    list = new ImList(localSymtab.toSexp(), list);
    // concatenate sublist
    list = ((ImList)flowGraph().toSexp()).destructiveReverse(list);
    return list.destructiveReverse();
  }

  /** Print L-function in standard form. */
  public void printStandardForm(PrintWriter out) {
    out.println("(FUNCTION \"" + symbol.name + "\"");
    // print symbol table
    localSymtab.printStandardForm(out, "  ");
    // print body
    flowGraph().printStandardForm(out, "  ");
    out.println(")");
  }


  /** Dump internal data structure of the Function object. */

  private static final LocalAnalysis[] emptyAnares = new LocalAnalysis[0];

  public void printIt(PrintWriter out) { printIt(out, emptyAnares); }


  /** Dump internal data structure of the Function with some analyses. */
  public void printIt(PrintWriter out, LocalAnalyzer[] anals) {
    LocalAnalysis [] anares;
    if (anals != null) {
      anares = new LocalAnalysis[anals.length];
      for (int i = 0; i < anals.length; i++)
        anares[i] = require(anals[i]);
    } else
      anares = emptyAnares;
    printIt(out, anares);
  }


  /** Dump internal data structure of the Function with some analyses. */
  public void printIt(PrintWriter out, LocalAnalysis[] anals) {
    out.println();
    out.println("Function \"" + symbol.name + "\":");

    for (int i = 0; i < anals.length; i++)
      anals[i].printBeforeFunction(out);

    out.print(" Local ");
    localSymtab.printIt(out);
    out.println();
    if (flowGraph != null) {
      out.println(" Control Flow Graph:");
      flowGraph().printIt(out, anals);
    } else {
      out.println(" L-expression List:");
      for (BiLink p = lirList().first(); !p.atEnd(); p = p.next()) {
        LirNode node = (LirNode)p.elem();
        if (node.opCode == Op.DEFLABEL)
          out.print("  ");
        else
          out.print("   ");
        out.println(node.toString());
      }
    }
    for (int i = 0; i < anals.length; i++)
      anals[i].printAfterFunction(out);

    out.println("End Function");
    out.println();
  }

  public String toString() {
    return "<Function " + symbol.name + ">";
  }
}
