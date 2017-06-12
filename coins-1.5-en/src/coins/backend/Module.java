/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend;

import coins.alias.anallir.AliasAnalyzer;  //##100 K. Mori (titech)
import coins.alias.anallir.AliasInformation;  //##100 K. Mori (titech)

import coins.backend.ana.ControlDependences;
import coins.backend.ana.DominanceFrontiers;
import coins.backend.ana.Dominators;
import coins.backend.ana.LoopAnalysis;
import coins.backend.ana.Postdominators;
import coins.backend.ana.ReverseDFST;
import coins.backend.ana.LiveVariableSlotwise;
import coins.backend.lir.LirFactory;
import coins.backend.lir.LirNode;
import coins.backend.opt.IntroVirReg;
import coins.backend.opt.JumpCanon;
import coins.backend.opt.JumpOpt;
import coins.backend.opt.LoopInversion;
import coins.backend.opt.PreHeaders;
import coins.backend.opt.SimpleOpt;
import coins.backend.opt.If2Jumpc;
import coins.backend.sym.Label;
import coins.backend.sym.SymTab;
import coins.backend.sym.Symbol;
import coins.backend.util.BiLink;
import coins.backend.util.BiList;
import coins.backend.util.ImList;
import coins.backend.util.QuotedString;
import coins.backend.util.Misc;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;


/**
 * Represent module, the whole input of the compiler.
 */
public class Module {

  static final String DEFAULT_TARGET = "sparc";
  static final String DEFAULT_CONVENTION = "standard";

  /** Module name */
  public final String name;

  /** Global symbol table */
  public final SymTab globalSymtab = new SymTab(this);

  /** List of functions/data */
  public final BiList elements = new BiList();

  /** Collection of global variables **/
  public final Root root;

  /** Target Machine **/
  public final TargetMachine targetMachine;

  /** LirNode factory object **/
  public final LirFactory newLir;

  /** Current line number **/
  private int currentLineNo;

  /** Module creation time. **/
  private long lap;
  
  public static AliasInformation aliasInf;  //##100 K. Mori (titech)

  /** Create a module */
  public Module(Object sexp, Root root)
    throws SyntaxErrorException {
    this(sexp, DEFAULT_TARGET, DEFAULT_CONVENTION, root);
  }

  /** Create a module */
  public Module(Object sexp, String targetName, String convention, Root root)
    throws SyntaxErrorException {
    this.root = root;

    if (targetName == null)
      targetName = DEFAULT_TARGET;

    if (convention == null)
      convention = DEFAULT_CONVENTION;

    if (root.dispIntervalTime)
      lap = root.timer.checkPoint();

    // Name standard transformers.
    root.registerTransformer(IntroVirReg.trig);
    root.registerTransformer(If2Jumpc.trig);
    root.registerTransformer(SimpleOpt.trig);
    root.registerTransformer(JumpOpt.trig);
    root.registerTransformer(JumpCanon.trig);
    root.registerTransformer(PreHeaders.trig);
    root.setHook("LoopInversion", new BiList());
    if (root.optLoopInversion)
      root.addHook("LoopInversion", LoopInversion.trig);

    root.registerTransformer(coins.backend.opt.Profiler.trig);
    if (root.isOptionSet("profile"))
      root.addHook("+AfterEarlyRewriting", "Profiler");

    root.registerTransformer(Function.toMachineCodeTrig);
    root.registerTransformer(coins.backend.regalo.LiveRange.trig);

    try {
      // Create an instance of LirFactory.
      newLir = new LirFactory(this);

      // Initialize Target Machine
      targetMachine = new TargetMachine(globalSymtab, targetName, convention, this);
      if (root.dispIntervalTime)
        root.debOut.println("TMD initialization: "
                            + root.timer.getIntervalTime()
                            + " lap:" + root.timer.getIntervalTime(lap));

      if (!(sexp instanceof ImList))
        throw new SyntaxError("Expected (");
      // root.debOut.println("Module:doCodegen() sexp = " + (ImList)sexp);
      ImList ptr = (ImList)sexp;
      if (ptr.elem() != Keyword.MODULE)
        throw new SyntaxError("Expected MODULE");
      if ((ptr = ptr.next()).atEnd())
        throw new SyntaxError("Expected module name");

      name = ((QuotedString)ptr.elem()).body;

      // Parse top-level S-expressions
      while (!(ptr = ptr.next()).atEnd()) {
        ImList top = (ImList)ptr.elem();
        String kw = (String)top.elem();
        if (kw == Keyword.SYMTAB)
          doSymtbl(top);
        else if (kw == Keyword.DATA)
          elements.add(new Data(this, top));
        else if (kw == Keyword.FUNCTION)
          elements.add(new Function(this, top));
        else if (kw == "LINE")
          currentLineNo = Integer.parseInt((String)top.elem2nd());
        else if (kw == "INFO") {
          if (!top.next().atEnd() && top.elem2nd() == "LINE")
            currentLineNo = Integer.parseInt((String)top.elem3rd());
        }
        else
          throw new SyntaxError("Unexpected " + kw);
      }

      if (root.dispIntervalTime)
        root.debOut.println("ListLIR->LirNode: "
                            + root.timer.getIntervalTime()
                            + " lap:" + root.timer.getIntervalTime(lap));

    } catch (SyntaxError e) {
      root.debOut.println(e.getMessage());
      throw new SyntaxErrorException(e.getMessage());
    }
  }


  /** Return current line number. **/
  public int getCurrentLineNo() { return currentLineNo; }


  private void doSymtbl(ImList ptr) throws SyntaxError {
    // Parse symbol table
    while (!(ptr = ptr.next()).atEnd()) {
      globalSymtab.addSymbol((ImList)ptr.elem());
    }
    globalSymtab.sanitize();
  }


  /** Add global symbol */
  public Symbol addSymbol(String name, int storage, int type, int boundary,
                          String segment, String linkage, ImList opt) {
    return globalSymtab.addSymbol(name, storage, type, boundary,
                                  segment, linkage, opt);
  }


  /** Parse DATA node **/
  void doData(ImList node) throws SyntaxError {
    elements.add(new Data(this, node));
  }


  /** Add new DATA object. **/
  public void addData(Symbol sym, LirNode value) {
    elements.add(new Data(this, sym, value));
  }


  /** Label generator **/
  private int labelGenerator = 1;

  /** Label table **/
  private Map labelTable = new HashMap();

  /** Look up label by final name. **/
  public Label lookupLabel(String internalName) {
    return (Label)labelTable.get(internalName);
  }

  /** Generate new label name and return it.
   **  Returned string is interned. **/
  private String genNewLabelName() {
    return (".L" + labelGenerator++).intern();
  }

  /** Create new label instance with final name and return it. **/
  public Label newLabel() {
    Label label = new Label(genNewLabelName());
    labelTable.put(label.name(), label);
    return label;
  }

  /** Rename existing label to final name. **/
  public void renameLabelToFinal(Label label) {
    if (lookupLabel(label.name()) != null)
      return;

    label.rename(genNewLabelName());
    labelTable.put(label.name(), label);
  }


  /** Symbol's id generator **/
  private int symbolIdGenerator = 1;

  /** Generate new symbol's id **/
  public int genSymbolId() { return symbolIdGenerator++; }

  /** Return maximum symbol id plus 1. **/
  public int symbolIdBound() { return symbolIdGenerator; }


  /** Find symbol whose name is <code>name</code> in this context. **/
  public Symbol getSymbol(String name) {
    return globalSymtab.get(name);
  }


  /** Apply some analysis on each function */
  public void apply(LocalAnalyzer analyzer) {
    for (BiLink p = elements.first(); !p.atEnd(); p = p.next()) {
      if (p.elem() instanceof Function)
        ((Function)p.elem()).apply(analyzer);
    }
  }

  /** Require some analysis for each function */
  public void require(LocalAnalyzer analyzer) {
    for (BiLink p = elements.first(); !p.atEnd(); p = p.next()) {
      if (p.elem() instanceof Function)
        ((Function)p.elem()).require(analyzer);
    }
  }




  /** Apply transformation (generic form). **/
  public void apply(Object trans) {
    if (trans instanceof ImList)
      apply((ImList)trans);
    else if (trans instanceof BiList)
      apply((BiList)trans);
    else if (trans instanceof Transformer[])
      apply((Transformer[])trans);
    else if (trans instanceof Transformer)
      apply((Transformer)trans);
    else if (trans instanceof String[])
      apply((String[])trans);
    else if (trans instanceof String)
      apply((String)trans);
    else
      throw new CantHappenException("Unexpected type: " + trans.getClass());
  }

  /** Apply transformation assigned to Hook. **/
  public void apply(String hook) {
    Object trans = root.getHook(hook);
    if (trans == null) {
      // Error unless hook name starts with +.
      if (hook.charAt(0) != '+')
        throw new CantHappenException("Undefined Hook: " + hook);
    } else
      apply(trans);
  }

  /** Apply transformations listed in the <code>transList</code>. **/
  public void apply(ImList transList) {
    for (ImList p = transList; !p.atEnd(); p = p.next())
      apply(p.elem());
  }

  /** Apply transformations listed in the <code>transList</code>. **/
  public void apply(BiList transList) {
    for (BiLink p = transList.first(); !p.atEnd(); p = p.next())
      apply(p.elem());
  }

  /** Apply transformations listed in the <code>transVector</code>. **/
  public void apply(Object[] transVector) {
    for (int i = 0; i < transVector.length; i++)
      apply(transVector[i]);
  }


  /** Apply tranformation on module without argument. **/
  public void apply(Transformer xformer) {
    apply(xformer, ImList.list());
  }


  /** Do some transform(or optimization) on each function with trace dump. **/
  public void apply(Transformer xformer, ImList args) {
    if (root.traceOK(xformer.name(), 2)) {
      root.debOut.println();
      root.debOut.println("Before " + xformer.name()
                          + " (" + xformer.subject() + "):");
      printIt(root.debOut);
    }
    if (root.GCflush)
      root.timer.gcReport(root.debOut);
    long start = root.timer.getLaptime();

    if (xformer instanceof LocalTransformer) {
      for (BiLink p = elements.first(); !p.atEnd(); p = p.next()) {
        if (p.elem() instanceof Function)
          ((LocalTransformer)xformer).doIt((Function)p.elem(), args);
        else if (p.elem() instanceof Data)
          ((LocalTransformer)xformer).doIt((Data)p.elem(), args);
      }
    } else
      ((GlobalTransformer)xformer).doIt(this, args);

    if (root.dispIntervalTime)
      root.debOut.println(xformer.name() + ": " + root.timer.getIntervalTime(start) + "  lap:" + root.timer.getIntervalTime(lap));

    if (root.traceOK(xformer.name(), 1)) {
      root.debOut.println();
      root.debOut.println("After " + xformer.name()
                          + " (" + xformer.subject() + "):");
      printIt(root.debOut);
    }
  }


  private Symbol[] constDataTbl = new Symbol[0];

  /** set constDataTbl. **/
  private void setConstDataTbl(int index, Symbol sym) {
    if (index >= constDataTbl.length) {
      Symbol[] w = new Symbol[Misc.clp2(index + 1)];
      for (int i = 0; i < constDataTbl.length; i++)
        w[i] = constDataTbl[i];
      constDataTbl = w;
    }
    constDataTbl[index] = sym;
  }

  private Symbol getConstDataTbl(int index) {
    if (index < constDataTbl.length)
      return constDataTbl[index];
    else
      return null;
  }


  /** Label(.LCxxx) generator **/
  private int constNumber = 1;

  /** Convert immediate constant (INTCONST/FLOATCONST) to
   **  DATA component.
   ** @param value constant node to be converted
   ** @return Symbol entry **/
  public Symbol constToData(LirNode value) {
    Symbol sym = getConstDataTbl(value.id);
    if (sym == null) {
      sym = addSymbol(".LC" + constNumber++,
                      Storage.STATIC, value.type,
                      targetMachine.alignForType(value.type),
                      ".text", "LDEF", null);
      addData(sym, value);
      setConstDataTbl(value.id, sym);
    }
    return sym;
  }



  /** Print L-module in standard form. */
  public void printStandardForm(PrintWriter out) {
    out.println("(MODULE \"" + name + "\"");

    // print symbol table
    globalSymtab.printStandardForm(out, "  ");

    // print body
    for (BiLink p = elements.first(); !p.atEnd(); p = p.next())
      ((ModuleElement)p.elem()).printStandardForm(out);

    out.println(")");
  }


  /** Print current module status */
  public void printIt(PrintWriter out) { printIt(out, null); }

  /** Print current module status with analyses */
  public void printIt(PrintWriter out, LocalAnalyzer[] anals) {
    out.println("Module \"" + name + "\":");
    out.print("Global ");
    globalSymtab.printIt(out, root.traceOK("LIR", 3));
    for (BiLink p = elements.first(); !p.atEnd(); p = p.next())
      ((ModuleElement)p.elem()).printIt(out, anals);
    out.println("End Module");
    out.println();
  }


  /** Return string representation of this object. **/
  public String toString() {  return "<Module " + name + ">"; }


  /** Convert to S-expression LIR format. **/
  public Object toSexp() {
    ImList list = ImList.Empty;

    list = new ImList("MODULE", list);
    list = new ImList(new QuotedString(name), list);
    list = new ImList(globalSymtab.toSexp(), list);
    for (BiLink p = elements.first(); !p.atEnd(); p = p.next())
      list = new ImList(((ModuleElement)p.elem()).toSexp(), list);
    return list.destructiveReverse();
  }
  


  /** Interfaces for driver. **/

  /** Compile S-expression LIR. **/
  public static void doCompile(ImList sexp, String targetName, String convention, Root root)
    throws SyntaxErrorException {

    Module compileUnit = new Module(sexp, targetName, convention, root);
    compileUnit.apply(new String[] {
      "IntroVirReg",
      "EarlyRewriting",
      "+AfterEarlyRewriting",
      "If2Jumpc",
      "+BeforeBasicOpt",
      "SimpleOpt",
      "JumpOpt",
      "PreHeaders",
      "LoopInversion",
      "+AfterBasicOpt",
      "+BeforeCodeGeneration",
      "LateRewriting",
      "+AfterLateRewriting",
      "ToMachineCode",
      "+AfterToMachineCode",
      "ConvToAsm"
    });
  }
  

  /** Convert LIR S-expression LIR to internal form
   **  and convert frame variables into register ones. **/
  public static Module loadSLir(ImList sexp, String targetName, String convention, Root root)
    throws SyntaxErrorException {

    /*
    if (root.dumpSexp)
      root.debOut.println(sexp.toString());
    */

    // Read LIR
    Module compileUnit = new Module(sexp, targetName, convention, root);
    if (root.traceOK("LIR", 1)) {
      root.debOut.println();
      root.debOut.println("After CFG created:");
      compileUnit.printIt(root.debOut);
    }
    //##100 BEGIN  K. Mori (titech)
    //if execute regpromote
    if(root.spec.getCoinsOptions().isSet("regpromote-ex")) {
    	try {
    		aliasInf = new AliasAnalyzer().analyze(compileUnit);
    	}
        // Catch the IllegalArgumentException (Runtime Exception)
        // caused by an assignment to non-variable (register) object.
    	//catch(IllegalArgumentException e) {
    	catch(RuntimeException e) {
    	    aliasInf = null;
    	}
    }
    //##100 END  K. Mori (titech)

    compileUnit.apply(new String[] {
      "IntroVirReg",
      "EarlyRewriting",
      "+AfterEarlyRewriting",
      "If2Jumpc" });

    return compileUnit;
  }





  /** Do basic optimizations. **/
  public void basicOptimization() {
    apply(new String[] {
      "+BeforeBasicOpt",
      "SimpleOpt",
      "JumpOpt",
      "PreHeaders",
      "LoopInversion",
      "+AfterBasicOpt" });
  }


  /** Specify assembler output stream. **/
  public void setAsmOut(OutputStream codeStream) {
    targetMachine.setAsmStream(codeStream);
  }


  /** Write machine code to codeStream **/
  public void generateCode(OutputStream codeStream) {
    targetMachine.setAsmStream(codeStream);
    generateCode();
  }


  /** Write machine code to codeStream with specific transformers. **/
  public void generateCodeWith(OutputStream codeStream, Object trans) {
    targetMachine.setAsmStream(codeStream);
    generateCodeWith(trans);
  }


  /** Write machine code. **/
  public void generateCode() {
    generateCodeWith(new String[] {
      "+BeforeCodeGeneration",
      "LateRewriting",
      "+AfterLateRewriting",
      "ToMachineCode",
      "+AfterToMachineCode",
      "ConvToAsm"
    });
  }


  /** Write machine code with specific transformers. **/
  public void generateCodeWith(Object trans) {
    /**
       if (root.asmStream() == null)
         throw new CantHappenException("asm stream not specified");
    **/

    if (root.traceOK("LIR", 1)) {
      root.debOut.println();
      root.debOut.println("Before Code Generation:");
      printIt(root.debOut,
              new LocalAnalyzer[]{Dominators.analyzer,
                                  DominanceFrontiers.analyzer,
                                  Postdominators.analyzer,
                                  ControlDependences.analyzer,
                                  LoopAnalysis.analyzer,
                                  ReverseDFST.analyzer,
                                  LiveVariableSlotwise.analyzer});
    }

    apply(trans);

    if (root.traceOK("ListDump", 1)) {
      root.debOut.println("External-LIR Format:");
      ImList.printIt(root.debOut, toSexp());
    }

    if (root.dispIntervalTime)
      root.debOut.println("BackEnd Total: " + root.timer.getIntervalTime(lap));
  }


  /** Return elapsed time since module created. **/
  public long elapsedTime() {
    return root.timer.getIntervalTime(lap);
  }

}
