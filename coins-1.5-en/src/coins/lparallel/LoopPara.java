/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.lparallel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet; //##74
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set; //##74

import java.text.ParseException;

import coins.FlowRoot;
import coins.HirRoot;
import coins.IoRoot;
import coins.PassException;
import coins.SymRoot;
import coins.aflow.FlowResults;
import coins.driver.CoinsOptions;
import coins.driver.CommandLine;
import coins.driver.CompileSpecification;
import coins.driver.CompileStatus;
import coins.driver.CompilerDriver;
import coins.driver.Driver;
import coins.driver.Suffix;
import coins.driver.TemporaryFileManager;
import coins.driver.Trace;
import coins.ir.IrList; //##74
import coins.ir.hir.BlockStmt; //##74
import coins.ir.hir.HIR;
import coins.ir.hir.InfStmt; //##74
import coins.ir.hir.Program;
import coins.ir.hir.Stmt; //##74
import coins.ir.hir.SubpDefinition;
import coins.ir.hir.TestHir;
import coins.opt.Opt;
import coins.sym.StringConst; //##74
import coins.sym.Subp; //##74
import coins.sym.Sym; //##74
import coins.sym.TestSym;

/**
 *
 *
 * A driver implementation using COINS Compiler Driver API.
 *
 *
 **/
public class LoopPara
  extends Driver
{
  public TemporaryFileManager fTemporaryFileManager;
  File fHir2CFile;
  public //##74
  //##74 boolean fstophir2c;
  boolean fstophir2c = true; //##74
  String fOpenMPTmpFileName;
  private static final String HIR_OPT_OPTION = "hirOpt";
  private final static char OPT_OPTION_DELIMITER = '/';
  protected static final String DEFAULT_OPENMP_NAME = "omcc";
  protected Set fSubprogramsToBeParallelized; //##74

  //##74 protected boolean
  public boolean //##74
  makeCSourceFromHirBase(
    //##74 String timing,
    String pModifier, //##74
    HirRoot hirRoot,
    SymRoot symRoot,
    IoRoot io) throws IOException, PassException
  {
    if (fTemporaryFileManager == null)  //##74
      fTemporaryFileManager = new TemporaryFileManager(); //##74
    //##74 fHir2CFile = createHir2CFile(io);
    fHir2CFile = createHir2CFile(io, pModifier); //##74
    FileOutputStream out = new FileOutputStream(fHir2CFile);
    callHirBaseToC(hirRoot, symRoot, io, out);
    return true;
  }

  /**
   *
   * createHir2CFile:
   *
   *
   **/
  //##74 private File createHir2CFile(IoRoot io)
  private File createHir2CFile(IoRoot io, String pModifier) //##74
    throws IOException, PassException
  {
    File openMPFile;
    if (fstophir2c == true) {
      CoinsOptions coinsOptions =
        io.getCompileSpecification().getCoinsOptions();
      File source = io.getSourceFile();
      String sourcePath = source.getPath();
      String root = sourcePath.substring(0, sourcePath.lastIndexOf('.'));
      //##74 String Hir2CFileName = root.concat("-loop.c");
      String Hir2CFileName = root.concat("-" + pModifier + ".c"); //##74
      //
      // ex) filename =foo-loop.c
      //
      openMPFile = new File(Hir2CFileName);
    }
    else {
      //
      // ex) filename = /tmp/COINS****.c
      //
      openMPFile = createTmpFile(".c");
    }
    return (openMPFile);
  }

  /**
   *
   * createTmpFile:
   *
   *
   **/
  private File createTmpFile(String suffix) throws IOException, PassException
  {
    return fTemporaryFileManager.createTemporaryFile("COINSLOOP", suffix);
  }

  /**
   *
   * OpenMPCompile:
   *
   *
   **/
  protected void OpenMPCompile(String timing,
    HirRoot hirRoot,
    SymRoot symRoot,
    OutputStream out,
    IoRoot io) throws IOException, PassException
  {
    Trace trace = io.getCompileSpecification().getTrace();
    CoinsOptions coinsOptions = io.getCompileSpecification().getCoinsOptions();
    CompileSpecification spec = io.getCompileSpecification();
    File source = io.getSourceFile();
    List options = new LinkedList();
    String sourcePath = source.getPath();

    File tmpFile = createTmpFile(".s");
    String commandName = DEFAULT_OPENMP_NAME;
    options.add(0, "-S");
    options.add(1, fHir2CFile.getPath());
    options.add(2, "-o");
    options.add(3, tmpFile.getPath());
    String[] commandLine = makeCommandLine(commandName, options);
    //
    //   ex) omcc -S hir2cfile -o tmpfile
    //
    int ret = runProgram(commandLine, null, null, io);
    if (ret != 0) {
      throw new PassException("OpenMP Compiler",
        "Error(s) in OpenMP Compiler.");
    }
    else {
      storeToFile(new FileInputStream(tmpFile), out);
    }
  }

  /**
   *
   * storeToFile:
   *
   *
   **/
  private void storeToFile(InputStream in, OutputStream lOut) throws
    IOException
  {
    int len;
    byte[] buffer = new byte[4096];
    while ((len = in.read(buffer)) > 0) {
      lOut.write(buffer, 0, len);
    }
    lOut.close();
  }

  /**
   *
   * LoopParallel:
   *
   *
   **/
  public void LoopParallel(HirRoot hirRoot, SymRoot symRoot, IoRoot ioRoot)
  {

    LoopParallelImpl LoopParallel;
    FlowRoot flowRoot = new FlowRoot(hirRoot);
    fSubprogramsToBeParallelized =
      getSubprogramsToBeParallelized(hirRoot, symRoot, ioRoot); //##74
    coins.ir.IrList SubpDefList =
      ((Program)hirRoot.programRoot).getSubpDefinitionList();
    String Opt;
    FlowResults.putRegClasses(new RegisterClasses());
    Iterator subpDefIterator = SubpDefList.iterator();

    CoinsOptions coinsOptions =
      ioRoot.getCompileSpecification().getCoinsOptions();
    if (coinsOptions.isSet("LoopOpt")) {
      Opt = coinsOptions.getArg("LoopOpt");
    }
    else {
      Opt = "";

    }
    while (subpDefIterator.hasNext()) {
      SubpDefinition subpDef =
        (SubpDefinition)(subpDefIterator.next());
      if (fSubprogramsToBeParallelized.contains(subpDef.getSubpSym())) { //##74
        LoopParallel =
          new LoopParallelImpl(hirRoot, ioRoot, subpDef, flowRoot);
        LoopParallel.LoopAnal();
        LoopParallel.ReconstructHir();
        LoopParallel.SetOpenMPInfo();
      } //##74
    }

  }

  //##74 BEGIN
  public void
  hir2OpenMP(HirRoot pHirRoot, SymRoot pSymRoot, IoRoot pIoRoot)
  throws IOException, PassException
{
  LoopParallel(pHirRoot, pSymRoot, pIoRoot);
  fTemporaryFileManager = new TemporaryFileManager();
  fstophir2c = true;
  boolean lSucceeded = makeCSourceFromHirBase("loop", pHirRoot, pSymRoot, pIoRoot);
  if (! lSucceeded) {
    pIoRoot.msgError.put("hir2OpenMP(PASSExcept)");
    throw new PassException("HIR to C", "Stop HIR TO C");
  }
} // hir2OpenMP

public Set
getSubprogramsToBeParallelized(HirRoot hirRoot, SymRoot symRoot, IoRoot ioRoot)
{
  //-- Collect information given by pragmas for parallelization
  //     from statements outside subprograms (included in initiation part).
  //##74 BEGIN
  Set lToBeParallelized = new HashSet();
  Set lSubprograms = new HashSet();
  Program lProgram = (Program)hirRoot.programRoot;
  for (Iterator lIter = lProgram.getSubpDefinitionList().iterator();
       lIter.hasNext(); ) {
    SubpDefinition lSubpDef = (SubpDefinition)lIter.next();
    lSubprograms.add(lSubpDef.getSubpSym());
  }
  ioRoot.dbgPara1.print(3, "Subprograms Defined "
    + lSubprograms + "\n");
  //##74 END
  HIR lProgInitPart = (HIR)lProgram.getInitiationPart();
  if (lProgInitPart instanceof BlockStmt) {
    for (Stmt lStmt = ((BlockStmt)lProgInitPart).getFirstStmt();
         lStmt != null;
         lStmt = lStmt.getNextStmt()) {
      if ((lStmt instanceof InfStmt) &&
      //##74    (((InfStmt)lStmt).getInfKind() == "doAll"))
      (((InfStmt)lStmt).getInfKind() == "parallel")) //##74
      {
        ioRoot.dbgPara1.print(3, lStmt.toString() + "\n");
        //##74 IrList lOptionList = ((InfStmt)lStmt).getInfList("doAll");
        IrList lOptionList = ((InfStmt)lStmt).getInfList("parallel"); //##74
        int lIndex;
        Object lObject = lOptionList.get(0);
        ioRoot.dbgPara1.print(3, " option name " + lObject
          + " " + lObject.getClass() + " " + lOptionList + "\n");
        String lOptionName;
        if (lObject instanceof String) {
          lOptionName = (String)lObject;
        } else if (lObject instanceof StringConst) {
          lOptionName = ((StringConst)lObject).getStringBody();
        } else if (lObject instanceof Sym) {
          lOptionName = ((Sym)lObject).getName();
        } else {
          ioRoot.dbgPara1.print(1, "\nUnknown option kind" + lObject + "\n");
          continue;
        }
        lOptionName = lOptionName.intern();
        //##74 if (lOptionName == "subpParallel")
        if (lOptionName == "doAllFunc") //##74
        {
          lIndex = 0;
          for (Iterator lIt = lOptionList.iterator();
               lIt.hasNext(); lIndex++) {
            Object lSubp = lIt.next();
            ioRoot.dbgPara1.print(4, " " + lSubp
              + " " + lSubp.getClass() + "\n");
            if ((lSubp instanceof Subp)) {
              lToBeParallelized.add(lSubp);
            }
          }
          //##74 fStatementsToBeDeleted.add(lStmt);
          //##74 fChanged = true;
        }
        //##74 else if (lOptionName == "subpParallelAll")
         else if (lOptionName == "doAllFuncAll") //##74
        {
          lToBeParallelized.addAll(lSubprograms);
        } else {
          ioRoot.dbgPara1.print(1, "\nUnknown option " + lOptionName + "\n");
          continue;
        }
      } // End for lparallel option
    } // End for each Stmt in InitiationPart
  } // End of InitiationPart processing.
  ioRoot.dbgPara1.print(2, "To be parallelized", lToBeParallelized.toString());
  return lToBeParallelized;
} // getSubprogramsToBeParallelized

  //##74 END

  /**
   * Compiler.<br>
   *
   * This sample compiler has eight passes:
   * <ol>
   *   <li> C source to HIR-C(*),
   *   <li> HIR-C to HIR-Base(*),
   *   <li> Flow analysis on HIR(*),
   *   <li> Optimization and Parallelization on HIR,
   *   <li> HIR to LIR(*),
   *   <li> Assembly code generation(*).
   * </ol>
   *
   * Five of the above (*) are already implemented.
   *
   * @param sourceFile the source file name.
   * @param in input.
   * @param out output.
   * @param spec the command line options and arguments.
   **/
  public void compile(File sourceFile,
    Suffix suffix,
    InputStream in,
    OutputStream out,
    IoRoot io) throws IOException, PassException
  {

    FlowRoot hirFlowRoot;
    CoinsOptions coinsOptions = io.getCompileSpecification().getCoinsOptions();
    boolean hirAnal = coinsOptions.isSet("hirAnal");
    //boolean lirAnal = coinsOptions.isSet("lirAnal");
    //boolean optLab1 = coinsOptions.isSet("optLabel1"); //##19
    //boolean optLab2 = coinsOptions.isSet("optLabel2"); //##19
    boolean hirOpt = coinsOptions.isSet(HIR_OPT_OPTION);

    //##74 fstophir2c = coinsOptions.isSet("hir2c");

    Trace trace = io.getCompileSpecification().getTrace();

    SymRoot symRoot = new SymRoot(io);
    HirRoot hirRoot = new HirRoot(symRoot);
    symRoot.attachHirRoot(hirRoot);
    symRoot.initiate();

    /* pass 1 -- AST to HIR-C */

    //##71 trace.trace(myName, 5000, "compile(pass 1 -- AST to HIR-C)");
    //##71 hirRoot.programRoot = makeHirCFromCSource(hirRoot, in, io);

    /* pass 2 -- HIR-C to HIR-Base */

    //##71 trace.trace(myName, 5000, "compile(pass 2 --  HIR-C to HIR-Base)");
    //##71 hirRoot.programRoot
    //##71   = makeHirBaseFromC(hirRoot, (HIR)hirRoot.programRoot, io);

    makeHirFromSource(sourceFile, hirRoot, suffix, in, io); //##71
    //deleteUselessLabelsOfHir(hirRoot, optLab1, optLab2); //##19
    makeCSourceFromHirBase("new", hirRoot, symRoot, io);

    /* pass 3 -- Optimization and Parallelization on HIR */

    if (hirOpt) {
      hirFlowRoot = new FlowRoot(hirRoot);
      optimizeHir(hirRoot, hirFlowRoot, symRoot, io);
    }
    LoopParallel(hirRoot, symRoot, io);

    /* pass4  -- checking HIR and Symbol Table */
    testSym(hirRoot, io);

    hirFlowRoot = new FlowRoot(hirRoot);
    testHir(hirRoot, hirFlowRoot, io);

    /* pass 5 -- HIR to C Source  */

    trace.trace(myName, 5000, "compile(pass 4 -- HIR to C Source) ");
    if (makeCSourceFromHirBase("loop", hirRoot, symRoot, io) == true) {

      /* pass 5 -- OpenMP Compile  */
      trace.trace(myName, 5000, "compile(pass 5 -- OpenMP Compile)");
      if (fstophir2c == false) {
        OpenMPCompile("loop", hirRoot, symRoot, out, io);
      }
      else {
        trace.trace(myName, 5000, "compile(PASSExcept)");
        throw new PassException("HIR to C", "Stop HIR TO C");
      }
      trace.trace(myName, 5000, "compile(END)");
    }
  }

  /**
   * Tests Symbol Table before converting HIR to LIR.
   *
   * @param hirRoot an HirRoot object.
   * @param io an IoRoot object.
   * @throws IOException any IO error.
   * @throws PassException unrecoverable error(s) found in processing.
   **/
  protected void testSym(HirRoot pHirRoot, IoRoot io) throws IOException,
    PassException
  {
    CoinsOptions coinsOptions = io.getCompileSpecification().getCoinsOptions();
    if (coinsOptions.isSet(DEBUG_OPTION) ||
        coinsOptions.isSet(CHECK_SYMBOL_TABLE_OPTION)) {
      new TestSym(pHirRoot);
    }
  }

  /**
   * Tests HIR before converting it to LIR.
   *
   * @param hirRoot an HirRoot object.
   * @param hirFlowRoot an HIR-based flow analysis result.
   * @param io an IoRoot object.
   * @throws IOException any IO error.
   * @throws PassException unrecoverable error(s) found in processing.
   **/
  protected void testHir(HirRoot hirRoot, FlowRoot hirFlowRoot, IoRoot io) throws
    IOException, PassException
  {
    CoinsOptions coinsOptions = io.getCompileSpecification().getCoinsOptions();
    if (coinsOptions.isSet(DEBUG_OPTION) ||
        coinsOptions.isSet(CHECK_HIR_OPTION)) {
      new TestHir(hirRoot, hirFlowRoot);
    }
  }

  /**
   *
   * HIR-level optimizations.
   *
   * @param hirRoot an HirRoot object
   * @param hirFlowRoot an HIR-based flow analysis result
   * @param symRoot a SymRoot object
   * @param io an IoRoot object
   **/
  protected void optimizeHir(HirRoot hirRoot, FlowRoot hirFlowRoot,
    SymRoot symRoot, IoRoot io)
  {
    String lOptArg = io.getCompileSpecification().getCoinsOptions().getArg(
      HIR_OPT_OPTION);
    List lOpts = new ArrayList();
    if (includedInDelimitedList("cf", OPT_OPTION_DELIMITER, lOptArg)) {
      lOpts.add("cf");
    }
    if (includedInDelimitedList("cpf", OPT_OPTION_DELIMITER, lOptArg)) {
      lOpts.add("cpf");
    }
    if (includedInDelimitedList("cse", OPT_OPTION_DELIMITER, lOptArg)) {
      lOpts.add("cse");
    }
    if (includedInDelimitedList("dce", OPT_OPTION_DELIMITER, lOptArg)) {
      lOpts.add("dce");
    }
    boolean lOptimized = new Opt(hirFlowRoot).doHir(lOpts);
  }

  /**
   * Assembler.<br>
   *
   * Invokes an assembler command with options specified in a command line.<br>
   *
   * Input lines can be read from an <tt>InputStream</tt>.  Output lines
   * should be written to a <tt>File</tt>.  The original source file name of
   * the input can be obtained as a <tt>File</tt>.  Command line options and
   * arguments can be obtained from a <tt>CompileSpecification</tt>.
   *
   * @param sourceFile the source file name
   * @param in the <tt>InputStream</tt>
   * @param out the output <tt>File</tt>
   * @param spec the command line options and arguments
   **/
  public void assemble(File sourceFile,
    Suffix suffix,
    InputStream in,
    File out,
    IoRoot io) throws IOException, PassException
  {
    CompileSpecification spec;
    spec = io.getCompileSpecification();
    Trace trace = spec.getTrace();
    File tmpFile = createTmpFile(".s");
    FileOutputStream tmpout = new FileOutputStream(tmpFile);
    storeToFile(in, (OutputStream)tmpout);

    trace.trace(myName, 5000, "assemble");
    List options = spec.getAssemblerOptions();

    trace.trace(myName, 5000, "assemble(1)");
    options.add("-c");
    options.add("-o");
    options.add(out.getPath());
    options.add(tmpFile.getPath());

    CoinsOptions coinsOptions = spec.getCoinsOptions();
    String commandName = DEFAULT_OPENMP_NAME;
    if (coinsOptions.isSet(ASSEMBLER_NAME_OPTION)) {
      commandName = coinsOptions.getArg(ASSEMBLER_NAME_OPTION);
    }
    String[] commandLine = makeCommandLine(commandName, options);
    if (runProgram(commandLine, in, null, io) != 0) {
      throw new PassException(sourceFile,
        "assembler", "Error(s) in assembler.");
    }
  }

  /**
   * Linker.<br>
   *
   * Invokes a linker command with options specified in a command line.<br>
   *
   * Output executable file should be written to a <tt>File</tt>.  Command
   * line options and arguments can be obtained from a CompileSpecification.
   *
   * @param out the output <tt>File</tt>
   * @param spec the command line options and arguments
   **/
  public void link(File out, IoRoot io) throws IOException, PassException
  {
    Trace trace = io.getCompileSpecification().getTrace();
    List options = io.getCompileSpecification().getLinkerOptions();
    options.add(0, "-o");
    options.add(1, out.getPath());
    trace.trace(myName, 5000, "link(1)");
    CoinsOptions coinsOptions = io.getCompileSpecification().getCoinsOptions();
    String commandName = DEFAULT_OPENMP_NAME;
    if (coinsOptions.isSet(LINKER_NAME_OPTION)) {
      commandName = coinsOptions.getArg(LINKER_NAME_OPTION);
    }
    String[] commandLine = makeCommandLine(commandName, options);
    if (runProgram(commandLine,
                   null, null, io) != 0) {
      throw new PassException("linker", "Error(s) in linker.");
    }
  }

  public void cleanup(CompileSpecification spec)
  {

    CoinsOptions lCoinsOptions = spec.getCoinsOptions();
    if (!lCoinsOptions.isSet(lCoinsOptions.DEBUG)) {
      spec.getTrace().trace("Driver", 5000, "cleaning up...");
      fTemporaryFileManager.cleanupTemporaryFiles();
    }

  }

  /**
   * Loads default settings specified in a default setting file located in the
   * library directory
   *
   * @param spec a CompileSpecification object.
   **/
  private void loadDefaultSettings(CompileSpecification spec)
  {
    defaultSettings = new Properties();
    CoinsOptions coinsOptions = spec.getCoinsOptions();
    File libDir = coinsOptions.getLibDir();
    File settingFile = new File(libDir, DEFAULT_SETTING);
    if (settingFile.exists()) {
      try {
        defaultSettings.load(new FileInputStream(settingFile));
      }
      catch (IOException e) {
        spec.getWarning().warning("Couldn't load the default setting file: "
          + settingFile.getPath());
      }
    }
  }

  protected void go(String[] args)
  {
    try {
      CompileSpecification spec = new CommandLine(args);
      fTemporaryFileManager = new TemporaryFileManager();
      loadDefaultSettings(spec);
      int status = new CompilerDriver(spec).go(this);
      cleanup(spec);
      System.exit(status);
    }
    catch (ParseException e) {
      System.err.println(e.getMessage());
      System.exit(CompileStatus.ABNORMAL_END);
    }
  }

  /**
   * A main function.<br>
   *
   * Makes a compile specification from a command line.  Creates an compiler
   * driver API object giving the compile specification.  Creates a driver
   * implementation object and pass it to the API object to start compilation.
   *
   * @param args a command line.
   **/
  public static void main(String[] args)
  {
    new LoopPara().go(args);
  }
}
