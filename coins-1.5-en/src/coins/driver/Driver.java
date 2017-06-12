/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.driver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.PushbackReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import coins.FlowRoot;
import coins.HirRoot;
import coins.IoRoot;
import coins.PassException;
import coins.Registry;
import coins.SymRoot;
import coins.backend.Module;
// Begin(2010.1)
import coins.backend.Function;
import coins.backend.regalo.OptimisticCoalescingAllocation;
// End(2010.1)
import coins.backend.Root;
import coins.backend.sched.Schedule;
import coins.backend.SyntaxError;
import coins.backend.SyntaxErrorException;
import coins.backend.sim.SimFuncTable;
import coins.backend.sim.SimulationData;
import coins.backend.util.ImList;
import coins.cfront.Cfront;
import coins.flow.Flow;
import coins.flow.FlowImpl;
import coins.hir2c.HirBaseToCImpl;
import coins.hir2lir.ConvToNewLIR;
import coins.hir2lir.ReformHir;
import coins.ir.hir.HIR;
import coins.ir.hir.SimplifyHir;
import coins.ir.hir.TestHir;
import coins.lir2c.LirToC;
import coins.lparallel.LoopPara;
import coins.mdf.MdfDriver;
import coins.opt.Opt;
import coins.sym.TestSym;

/**
 * A driver implementation using the COINS Compiler Driver API.
 * This driver is a working sample of the COINS Compiler Driver API, and can
 * be a modification basis or an inheritance base class of your own compiler
 * driver.
 **/
public class Driver
  implements CompilerImplementation
{
  /**
   * Verbose flag.
   **/
  protected static final String VERBOSE_FLAG = CompileSpecification.VERBOSE;

  /**
   *
   * A default preprocessor command name, which is used to invoke a process if
   * not specified by a corresponding compile option.
   **/
  protected static final String DEFAULT_PREPROCESSOR_NAME = "cpp";

  /**
   *
   * A default assembler command name, which is used to invoke a process if
   * not specified by a corresponding compile option.
   **/
  //##98 protected static final String DEFAULT_ASSEMBLER_NAME = "gas";
  protected static final String DEFAULT_ASSEMBLER_NAME = "as"; //##98

  /**
   *
   * A default linker command name, which is used to invoke a process if not
   * specified by a corresponding compile option.
   **/
  protected static final String DEFAULT_LINKER_NAME = "gcc";

  /**
   * An option name to specify a preprocessor command name.  When
   * "<tt>-coins:preprocessor=<i>foo</i></tt>" is specified as a compile
   * option, a command <i>foo</i> is used to invoke a process instead of the
   * default setting.
   **/
  protected static final String PREPROCESSOR_NAME_OPTION = "preprocessor";

  /**
   * An option name to specify an assembler command name.  When
   * "<tt>-coins:assembler=<i>foo</i></tt>" is specified as a compile option,
   * a command <i>foo</i> is used to invoke a process instead of the default
   * setting.
   **/
  protected static final String ASSEMBLER_NAME_OPTION = "assembler";

  /**
   * An option name to specify a linker command name.  When
   * "<tt>-coins:linker=<i>foo</i></tt>" is specified as a compile option, a
   * command <i>foo</i> is used to invoke a process instead of the default
   * setting.
   **/
  protected static final String LINKER_NAME_OPTION = "linker";

  /**
   * An option name to specify when HIR-Base to C should be invoked.  When
   * <tt>-coins:hir2c=<i>t<sub>1</sub></i>/t<sub>2</sub>/.../t<sub>n</sub></tt>
   * is specified, the HIR base structure is translated into a C source
   * program and written to a file named as
   * <tt><i>foo</i>-hir-<i>t</i>.c</tt>, where <i>t</i> should be one of
   * timing specifiers, and <i>foo</i> is the original file name root of the
   * source program.  Valid timing specifiers are:
   * <ul>
   *   <li> <tt>new</tt>, just after the creation, </li>
   *   <li> <tt>flo</tt>, after the data flow analysis and </li>
   *   <li> <tt>opt</tt>, after the optimization. </li>
   * </ul>
   * The timing specifiers can be altered and expanded in the derived drivers.
   **/
  protected static final String HIR_TO_C_OPTION = "hir2c";

  /**
   * An option name to specify dump HIR-Base at specified timings.  When
   * <tt>-coins:dumphir=<i>t<sub>1</sub></i>/t<sub>2</sub>/.../t<sub>n</sub>
   * </tt> is specified, the HIR-Base structure is dumped into a file named as
   * <tt><i>foo</i>-<i>t</i>.hir</tt>, where <i>t</i> is one of timing
   * specifiers, and <i>foo</i> is the original file name root of the source
   * program.  Valid timing specifiers are:
   * <ul>
   *   <li> <tt>new</tt>, just after the creation, </li>
   *   <li> <tt>flo</tt>, after the data flow analysis and </li>
   *   <li> <tt>opt</tt>, after the optimization. </li>
   * </ul>
   * The timing specifiers can be altered and expanded in the derived drivers.
   **/
  protected static final String DUMP_HIR_OPTION = "dumphir";

  /**
   * An option name to specify when LIR to C should be invoked.  When
   * <tt>-coins:lir2c=<i>t<sub>1</sub></i>/t<sub>2</sub>/.../t<sub>n</sub></tt>
   * is specified, the LIR structure is translated into a C source program and
   * written to a file named as <tt><i>foo</i>-lir-<i>t</i>.c, where <i>t</i>
   * should be one of timing specifiers, and <i>foo</i> is the original file
   * name root of the source program.  Valid timing specifiers are:
   * <ul>
   *   <li> <tt>new</tt>, just after the creation, </li>
   *   <li> <tt>flo</tt>, after the data flow analysis and </li>
   *   <li> <tt>opt</tt>, after the optimization. </li>
   * </ul>
   * The timing specifiers can be altered and expanded in the derived drivers.
   **/
  protected static final String LIR_TO_C_OPTION = "lir2c";

  /**
   * A delimiter sign to delimit arguments of HIR_TO_C_OPTION.
   **/
  protected static final char HIR_TO_C_OPTION_DELIMITER = '/';

  /**
   * A delimiter sign to delimit arguments of DUMP_HIR_OPTION.
   **/
  protected static final char DUMP_HIR_OPTION_DELIMITER = '/';

  /**
   * A delimiter sign to delimit arguments of LIR_TO_C_OPTION.
   **/
  protected static final char LIR_TO_C_OPTION_DELIMITER = '/';

  //##74 BEGIN
  /**
   * A delimiter sign to delimit arguments of other COINS_OPTION.
   **/
  protected static final char COINS_OPTION_DELIMITER = '/';
  //##74 END

  /**
   * An option name to terminate compilation of current file after HIR-Base to
   * C is performed.
   **/
  protected static final String STOP_AFTER_HIR_TO_C_OPTION = "stopafterhir2c";

  /**
   * An option name to terminate compilation of current file after LIR to C is
   * performed.
   **/
  protected static final String STOP_AFTER_LIR_TO_C_OPTION = "stopafterlir2c";

  /**
   * An option name to invoke HIR flow analysis.
   **/
  protected static final String HIR_FLOW_ANAL_OPTION = "hirAnal";

  /**
   * A delimiter sign to delimit arguments of HIR_FLOW_ANAL_OPTION.
   **/
  protected static final char FLOW_ANAL_OPTION_DELIMITER = '/';

  /**
   * The maximum optimization level specified by -O option.
   **/
  protected static final int MAX_OPTIMIZATION_LEVEL = 4;

  /**
   * The implied optimization level when -O option is specified without a
   * level.
   **/
  protected static final int DEFAULT_OPTIMIZATION_LEVEL = 1;

  /**
   * Optimization arguments specified to HIR_OPT_OPTION at variaous
   * optimization levels.  HIR_OPTIMIZATION_ARGS[i] is specified for -Oi.
   **/
  protected static final String[] HIR_OPTIMIZATION_ARGS
    = {
    //##76 null,
    "noSimplify",   //##76
    "cf",
    "cf",
    "inline/cf/pre", //##78
    "inline/loopexp/cf/cpf/pre"}; //##67

  /**
   * Optimization arguments specified to SSA_OPTION at various optimization
   * levels.  SSA_OPTIMIZATION_ARGS[i] is specified for -Oi.
   **/
  protected static final String[] SSA_OPTIMIZATION_ARGS
    = {
    null,
    //##83 "prun/cpyp/cstp/dce/ebe/srd3",
    "prun/cpyp/cstp/dce/ebe/srd3",  //##83
    "prun/divex/cse/cstp/hli/osr/hli/cstp/cpyp/preqp/cstp/rpe/dce/srd3",
    "prun/divex/cse/cstp/hli/osr/hli/cstp/cpyp/preqp/cstp/rpe/dce/srd3",
    "prun/divex/cse/cstp/hli/osr/hli/cstp/cpyp/preqp/cstp/rpe/dce/srd3"};

  /**
   * Optimization arguments specified to LIR optimization at variaous
   * optimization levels.  LIR_OPTIMIZATION_ARGS[i] is specified for -Oi.
   **/
/*  //##100
  protected static final String[] LIR_OPTIMIZATION_ARGS
    = {
    null,
    "loopinversion",
    "loopinversion,schedule-after,regpromote",
    "loopinversion,schedule,pipelining,regpromote",
    "loopinversion,schedule,pipelining,regpromote"};
*/  //##100
  //##100 BEGIN
  protected static final String[][] LIR_OPTIMIZATION_ARGS
  = {
	  {null},
	  {"loopinversion"},
	  {"loopinversion", "schedule-after", "regpromote"},
	  {"loopinversion", "schedule", "pipelining", "regpromote-ex"}, //##100 120217
	  {"loopinversion", "schedule", "pipelining", "regpromote-ex"}, //##100 120217
  };
  //##100 END
  /**
   * An option name to invoke HIR optimization
   **/
  protected static final String HIR_OPT_OPTION = "hirOpt";

  /**
   * An option argument for HIR_OPT_OPTION
   **/
  protected static final String HIR_OPT_ARG_FROMC = "fromc";

  /**
   * A delimiter sign to delimit arguments of HIR_OPT_OPTION.
   **/
  protected final static char OPT_OPTION_DELIMITER = '/';

  /**
   * An option name to specify target architecture.
   **/
  protected static final String TARGET_ARCH_OPTION = "target-arch";

  /**
   * An option name to specify target architecture convention.
   **/
  protected static final String TARGET_CONVENTION_OPTION = "target-convention";

  /**
   * An option name to specify whether HIR is checked before converting HIR to
   * LIR.
   **/
  protected static final String CHECK_HIR_OPTION = "testHir";

  /**
   * An option name to specify whether a Symbol Table is checked before
   * converting HIR to LIR.
   **/
  protected static final String CHECK_SYMBOL_TABLE_OPTION = "testSym";

  /**
   * An option name to specify an old version of LIR should be used.
   **/
  protected static final String OLD_LIR_OPTION = "oldlir";

  /**
   * An option name to specify a new version of LIR should be used.
   **/
  protected static final String NEW_LIR_OPTION = "newlir";

  /**
   * A default option name to specify whether version of LIR should be used.
   **/
  protected static final String DEFAULT_LIR_OPTION = NEW_LIR_OPTION;

  /**
   * A suffix option to write a new LIR source as compile output.
   **/
  protected static final String OUT_NEW_LIR_OPTION = "out-newlir";

  /**
   * Site-local default settings.
   **/
  protected Properties defaultSettings;

  /**
   * A default setting file name.
   **/
  protected static final String DEFAULT_SETTING = "settings";

  /**
   * A default setting property name of linker options.
   **/
  protected static final String DEFAULT_LINKER_OPTIONS_PROPERTY
    = "defaultLinkerOptions";

  /**
   * A default setting property name of system include path options.
   **/
  protected static final String SYSTEM_INCLUDE_PATH_PROPERTY
    = "systemIncludePath";

  /**
   * An option name to specify SSA options.
   **/
  protected static final String SSA_OPTION = "ssa-opt";

  /**
   * An option name to specify LIR optimization options.
   **/
  protected static final String LIR_OPT = "lir-opt";

  /**
   * An option name to invoke SMP parallelization.
   **/
  protected static final String MACRO_DATA_FLOW_OPTION = "mdf";

  //##74 BEGIN
  /**
   * An option name to invoke do-all-loop parallelization.
     * If its value is OpenMP, then C program with OpenMP directives is generated.
   * (parallelDoAll=OpenMP)
   * If its value is number, then machine code is generated treating
   * the number as the maximum degree of parallelization.
   * (parallelDoAll=n, where n is the maximum degree of parallelization)
   **/
  protected static final String PARALLEL_DO_ALL = "parallelDoAll";

  /**
   * An option name to invoke do-all-loop parallelization
   * to generate C+OpenMP program.
   **/
  protected static final String OPENMP_OPTION = "OpenMP";

  /**
   * An option name to invoke SMP coarse grain parallelization.
   **/
  protected static final String COARSE_GRAIN_PARALLEL = "coarseGrainParallel";

  /**
   * Abbreviated option name to invoke SMP parallelization.
   **/
  protected static final String CG_PARALLEL = "cgParallel";

  /**
   * An option name to invoke profiling simulator.
   **/
  protected static final String SIMULATE_OPTION = "simulate";

  //##74 END

  /**
   * Option name to specify debuggin mode.
   **/
  protected static final String DEBUG_OPTION = "debug";

  protected FlowRoot hirFlowRoot = null; //##65
  /**
   *
   * A name in trace messages.  Derived classes can override it.
   **/
  protected String myName = "Driver";

  private void traceCommandLine(String[] commandArray,
    Trace trace, int level)
  {
    StringBuffer buffer = new StringBuffer();
    int i;
    int size = commandArray.length;
    buffer.append(commandArray[0]);
    for (i = 1; i < size; i++) {
      buffer.append(" " + commandArray[i]);
    }
    trace.trace(myName, level, buffer.toString());
  }

  private String[] concatLists(List list1, List list2)
  {
    int size1 = list1.size();
    int size2 = list2.size();
    String[] result = new String[size1 + size2];
    int i;

    for (i = 0; i < size1; i++) {
      result[i] = (String)list1.get(i);
    }
    for (i = 0; i < size2; i++) {
      result[size1 + i] = (String)list2.get(i);
    }
    return result;
  }

  private List evaluateCommandName(String name)
  {
    int size = name.length();
    int i;
    char quote = ' ';
    char c;
    StringBuffer s = new StringBuffer();
    boolean escaped = false;
    List result = new ArrayList();
    name = name.trim();

    for (i = 0; i < size; i++) {
      if (escaped) {
        escaped = false;
        s.append(name.charAt(i));
      }
      else if (Character.isWhitespace(name.charAt(i))) {
        /* word delimitor */
        if (quote == ' ') {
          if (s.length() > 0) {
            result.add(s.toString());
            s = new StringBuffer();
          }
        }
        else {
          s.append(name.charAt(i));
        }
      }
      else {
        switch (c = name.charAt(i)) {
          case '\'':
          case '\"':
            if (quote == ' ') {
              /* quoted part starts */
              quote = c;
            }
            else if (quote == c) {
              /* quoted part ends */
              quote = ' ';
            }
            else {
              /* already in another quoted part, do nothing */
              s.append(c);
            }
            break;
          case '\\': /* An escape character.
                       It should be command. */
            escaped = true;
             break;
          default:
            s.append(name.charAt(i));
            break;
        }
      }
    }
    if (s.length() > 0) {
      result.add(s.toString());
    }
    return result;
  }

  /**
   *
   * Constructs a command line string specifying a command name and options
   * for it.  The specified command name can include some options following a
   * true command name.
   *
   * @param the command name.
   * @param options a list of String's, each of which is an option string.
   **/
  protected String[] makeCommandLine(String name, List options)
  {
    List list = evaluateCommandName(name);
    return concatLists(list, options);
  }

  /**
   *
   * An external command runner.  Sub-classes can use this.<br />
   *
   * A command line to invoke the external command should be given by an array
   * of String, each element of which should contain exactly one command word
   * (i.e., a command name or an argument to be passed to the command) and
   * should be listed in order.
   *
   * @param commandLine the command line.
   * @param in an InputStream from which the input data can be read.
   * @param out an OutputStream to which the output data should be written.
   * @param io the <tt>IoRoot</tt>.
   * @return an exit status of the external command.
   * @throws IOException failed to execute/write to/read from the external
   * process.
   **/
  protected int runProgram(String[] commandLine,
    InputStream in,
    OutputStream out,
    IoRoot io) throws IOException, PassException
  {
    CompileSpecification spec = io.getCompileSpecification();
    Trace trace = spec.getTrace();
    if (trace.shouldTrace(myName, 5000)) {
      traceCommandLine(commandLine, spec.getTrace(), 5000);
    }
    if (spec.isSet(VERBOSE_FLAG)) {
      StringBuffer message = new StringBuffer("% " + commandLine[0]);
      for (int i = 1; i < commandLine.length; ++i) {
        message.append(" " + commandLine[i]);
      }
      io.msgNote.put(message.toString());
    }
    Process p = Runtime.getRuntime().exec(commandLine);
    Thread t1 = new StreamCopier(in, p.getOutputStream());
    Thread t2 = new StreamCopier(p.getInputStream(), out);
    Thread t3 = new StreamCopier(p.getErrorStream(), System.err);
    t1.start();
    t2.start();
    t3.start();
    int exitStatus;
    while (true) {
      try {
        t1.join();
        p.getOutputStream().close();
        exitStatus = p.waitFor();
        t2.join();
        t3.join();
        break;
      }
      catch (InterruptedException e) {
        /* loop again */
      }
    }
    p.destroy();
    return exitStatus;
  }

  /**
   *
   * An external command runner.  Sub-classes can use this.<br />
   *
   * A command line to invoke the external command should be given by two
   * arguments: a command name in String and arguments listed in a List of
   * String.  Each element of the argument list should contain exactly one
   * command word and should be listed in order.
   *
   * @param command an external command name.
   * @param arguments arguments to be given to the external command.
   * @param in an InputStream from which the input data can be read.
   * @param out an OutputStream to which the output data should be written.
   * @param io the <tt>IoRoot</tt>.
   * @return an exit status of the external command.
   * @throws IOException failed to execute/write to/read from the external
   * process.
   **/
  protected int runProgram(String command, List arguments,
    InputStream in, OutputStream out,
    IoRoot io) throws IOException, PassException
  {
    int size = arguments.size();
    String commandLine[] = new String[size + 1];
    commandLine[0] = command;
    int i;
    for (i = 0; i < size; i++) {
      commandLine[i + 1] = (String)arguments.get(i);
    }
    return runProgram(commandLine, in, out, io);
  }

  /**
   *
   * An external command runner.  Sub-classes can use this.<br />
   *
   * A command line to invoke the external command should be given by a List
   * of String, each element of which should contain exactly one command word
   * (i.e., a command name or an argument to be passed to the command) and
   * should be listed in order.  Therefore, the command line must contain at
   * least one word (the command name).
   *
   * @param commandStrings a command line to invoke the external command.
   * @param in an InputStream from which the input data can be read.
   * @param out an OutputStream to which the output data should be written.
   * @param io the <tt>IoRoot</tt>.
   * @return an exit status of the external command.
   * @throws IOException failed to execute/write to/read from the external
   * process.
   * @throws PassException size of commandString is less than 1.
   **/
  protected int runProgram(List commandStrings,
    InputStream in, OutputStream out,
    IoRoot io) throws IOException, PassException
  {
    int size = commandStrings.size();
    String commandName = (String)commandStrings.get(0);
    List arguments;
    if (size == 1) {
      arguments = new ArrayList();
    }
    else {
      arguments = commandStrings.subList(1, size);
    }
    return runProgram(commandName, arguments, in, out, io);
  }

  /**
   * Sets default linker options specified in a library file to the end of a
   * linker option string.
   *
   * @param spec a CompileSpecification object.
   * @param options a list of linker options where the options to be set.
   **/
  protected void setSystemIncludePathOptions(CompileSpecification spec,
    List options)
  {
    String s = defaultSettings.getProperty(SYSTEM_INCLUDE_PATH_PROPERTY);
    if ((s != null) && (!s.equals(""))) {
      //##98 options.addAll(separateDelimitedList(s, ' '));
      //##98 BEGIN
      List lPaths = separateDelimitedListExcludingQuote(s, ' ');
      for (Iterator lIt = lPaths.iterator(); lIt.hasNext(); ) {
    	String lPath = (String)lIt.next();
    	lPath = "-I" + lPath;
    	options.add(lPath);
      }
      Trace trace = spec.getTrace();
      if (trace.shouldTrace(myName,5000)){
    	trace.trace(myName, 5000, "setSystemIncludePathOptions "+ s);
    	trace.trace(myName, 5000, "separateDelimitedListExcludingQuote "+ separateDelimitedListExcludingQuote(s, ' '));
    	trace.trace(myName, 5000, "resultant options "+ options);
      }
      //##98 END         	  
    }
  }

  /**
   * Preprocessor.<br>
   *
   * Invokes a preprocessor command with options specified in a command line.
   *
   * @param sourceFile the source file name.
   * @param suffix suffix rule of the source file.
   * @param out output.
   * @param io the <tt>IoRoot</tt>.
   * @throws IOException any IO error.
   * @throws PassException unrecoverable error(s) found in processing.
   **/
  public void preprocess(File sourceFile,
    Suffix suffix,
    OutputStream out,
    IoRoot io) throws IOException, PassException
  {
    CompileSpecification spec = io.getCompileSpecification();
    List options = spec.getPreprocessorOptions();
    setSystemIncludePathOptions(spec, options);
    options.add(sourceFile.getPath());
    CoinsOptions coinsOptions = spec.getCoinsOptions();
    String commandName = DEFAULT_PREPROCESSOR_NAME;
    if (coinsOptions.isSet(PREPROCESSOR_NAME_OPTION)) {
      commandName = coinsOptions.getArg(PREPROCESSOR_NAME_OPTION);
    }
    String[] commandLine = makeCommandLine(commandName, options);
    try {
      if (runProgram(commandLine, null, out, io) != 0) {
        throw new PassException(sourceFile,
          "preprocessor", "Error(s) in preprocessor.");
      }
    }
    finally {
      out.close();
    }
  }

  /**
   *
   * Makes an HIR-C tree from a C source program.  Derived classes can
   * override this method.
   *
   * @param hirRoot an HirRoot object.
   * @param in an input stream from which the C source program can be read.
   * @param io an IoRoot object.
   * @return a HIR root node.
   * @throws IOException any IO error.
   * @throws PassException unrecoverable error(s) found in processing.
   **/
  /* //##71
     protected HIR makeHirCFromCSource(HirRoot hirRoot, InputStream in, IoRoot io)
    throws IOException, PassException {
    /*---- AstToHirC converts ASTList to HIR-C.
       - ASTList is created inside AstToHirC.  ----*/
   /* //##71
      CompileSpecification spec = io.getCompileSpecification();
      Trace trace = spec.getTrace();
      trace.trace(myName, 5000, "makeHirCFromCSource");
      CoinsOptions coinsOptions = spec.getCoinsOptions();
      boolean fromcSpecified = false;
      if (coinsOptions.isSet(HIR_OPT_OPTION)) {
        fromcSpecified
    = includedInDelimitedList(HIR_OPT_ARG_FROMC,
            OPT_OPTION_DELIMITER,
            coinsOptions.getArg(HIR_OPT_OPTION));
      }
      ToHir tohir
      = new ToHir(hirRoot, coinsOptions.isSet(OLD_LIR_OPTION), fromcSpecified);
      new ToHirC(tohir).astToHirC(in);
      new ToHirC2(tohir).visitProgram();
      if (fromcSpecified) {
        new ToHirCOpt2(tohir).visitProgram();
      } else {
        new ToHirCOpt(tohir).visitProgram();
      }
      HIR hir = (Program)hirRoot.programRoot;
      hir.finishHir(); //##62
      if (trace.shouldTrace("HIR", 4)) {
        trace.trace("HIR", 4, "\nHIR-C ");
       //##62  hir.setIndexNumberToAllNodes(1);
        hir.print(0);
      }
      if (io.addToTotalErrorCount(0) > 0) {
    /* Just querying the error count.
        Why a querying method is not provided? */
    /* //##71
     throw new PassException(io.getSourceFile(),
           "Ast to HIR-C", "Error(s) in parsing source.");
       }
       return hir;
     }
     */
    //##71

    /**
     *
     * Makes an HIR-Bsae tree from an HIR-C tree.
     *
     * @param hirRoot an HirRoot object.
     * @param hir a root node of HIR-C tree.
     * @param io an IoRoot object.
     * @return a root node of an HIR-Base tree.
     * @throws IOException any IO error.
     * @throws PassException unrecoverable error(s) found in processing.
     **/
    /* //##71
       protected HIR makeHirBaseFromC(HirRoot hirRoot, HIR hir, IoRoot io)
      throws IOException, PassException {
      /*---- HirCToHirBase converts HIR-C to HIR-base. ----*/
     /* //##71
       CompileSpecification spec = io.getCompileSpecification();
       Trace trace = spec.getTrace();
       trace.trace(myName, 5000, "makeHirBaseFromC");
       CoinsOptions coinsOptions = spec.getCoinsOptions();
       boolean fromcSpecified = false;
       if (coinsOptions.isSet(HIR_OPT_OPTION)) {
         fromcSpecified =
        includedInDelimitedList(HIR_OPT_ARG_FROMC,
           OPT_OPTION_DELIMITER,
           coinsOptions.getArg(HIR_OPT_OPTION));
       }
       ToHir tohir
        = new ToHir(hirRoot, coinsOptions.isSet(OLD_LIR_OPTION), fromcSpecified);
       new ToHirBase(tohir).visitProgram();
       if (fromcSpecified) {
         new ToHirBaseOpt(tohir).visitProgram();
       }
       hir.setIndexNumberToAllNodes(1);
       if (trace.shouldTrace("HIR", 3)) {
         trace.trace("HIR", 3, "\nHIR-base ");
         hir.print(0);
       }
       if (trace.shouldTrace("Sym", 3)) {
         trace.trace("Sym", 3, "\nSym after HIR generation ");
         SymRoot symroot = hirRoot.symRoot;
         symroot.symTable.printSymTableAllDetail(symroot.symTableRoot);
       }
       if (io.addToTotalErrorCount(0) > 0) {
        /* Just querying the error count.
         Why a querying method is not provided? */
      /* //##71
         throw new PassException(io.getSourceFile(), "HIR-C to HIR-Base",
            "Error(s) in making HIR-Base.");
        }
        if (hir.isTree()) {
          trace.trace("HIR", 2, "\nHIR-base does not violate tree structure.");
        }
        return hir;
         }
       */
      //##71
      /**
       * Reads HIR structure from an input file.
       *
       * @param sourceFile the input file.
       * @param hirRoot an HirRoot object to where the HIR is stored.
       * @param in
       * @param io
       * @return the HIR read from sourceFile.
       * @throws IOException any IO error.
         * @throws PassException couldn't read the HIR since a class in the file is
       *         not found.
       **/
      private HIR readHIR(File sourceFile, HirRoot hirRoot,
        InputStream in, IoRoot io) throws IOException, PassException
      {
        ObjectInputStream oin = new ObjectInputStream(in);
        try {
          return (HIR)oin.readObject();
        }
        catch (ClassNotFoundException e) {
          io.msgError.put(sourceFile + ": class not found: " + e.getMessage());
          throw new PassException(sourceFile, myName,
            "class not found: " + e.getMessage());
        }
      }

  /**
   * HIR tree creation from source code.
   *
   * @param sourceFile the source file.
   * @param hirRoot an HirRoot object.
   * @param suffix suffix rule of the source file.
   * @param in an input stream from which the C source program can be read.
   * @param io an IoRoot object.
   * @throws IOException any IO error.
   * @throws PassException unrecoverable error(s) found in processing.
   **/
  protected void makeHirFromSource(File sourceFile, HirRoot hirRoot,
    Suffix suffix, InputStream in, IoRoot io) throws IOException, PassException
  {
    if (suffix.getLanguageName().equals("C")) {
      //##71 hirRoot.programRoot = makeHirCFromCSource(hirRoot, in, io);
      //##71 hirRoot.programRoot
      //##71 = makeHirBaseFromC(hirRoot, (HIR)hirRoot.programRoot, io);
      new Cfront(sourceFile, suffix, in, io, hirRoot).
        makeHirFromCSource();
    }
    else if (suffix.getLanguageName().equals("HIR")) {
      hirRoot.programRoot = readHIR(sourceFile, hirRoot, in, io);
    }
    else {
      io.msgError.put(sourceFile + ": Unknown programming language: "
        + suffix.getLanguageName());
      throw new PassException(myName,
        "Unknown language " + suffix.getLanguageName());
    }
  }

  /**
   *
   * Makes an HIR-based flow analysis.
   *
   * @param hirRoot an HirRoot object.
   * @param symRoot a SymRoot object.
   * @param io an IoRoot object.
   * @return a FlowRoot object which contains the flow analysis result.
   * @throws IOException any IO error.
   * @throws PassException unrecoverable error(s) found in processing.
   **/
  protected FlowRoot makeHIRFlowAnalysis(HirRoot hirRoot,
    SymRoot symRoot,
    IoRoot io) throws IOException, PassException
  {
    /*---- HIR flow analysys ----*/

    Trace trace = io.getCompileSpecification().getTrace();
    trace.trace(myName, 5000, "makeHIRFlowAnalysis");

    //##63 FlowRoot flowRoot = new FlowRoot(hirRoot);
    if (hirFlowRoot == null) { //##65
      hirFlowRoot = new FlowRoot(hirRoot); //##65
    }
    else {                                    //##94
      hirFlowRoot.resetAllFlowAnalSymLinks(); //##94
    }                                         //##94
    FlowRoot flowRoot = hirFlowRoot; //##65
    Flow lFlow = new FlowImpl(flowRoot); //##63
    CompileSpecification lSpec = io.getCompileSpecification();
    String lFlowArg = lSpec.getCoinsOptions().getArg(HIR_FLOW_ANAL_OPTION);
    if (includedInDelimitedList("cfg", FLOW_ANAL_OPTION_DELIMITER, lFlowArg)) {
      flowRoot.setFlowAnalOption(FlowRoot.OPTION_STANDARD_CONTROL_FLOW, true);
    }
    if (includedInDelimitedList("mdf", FLOW_ANAL_OPTION_DELIMITER, lFlowArg)) {
      flowRoot.setFlowAnalOption(FlowRoot.OPTION_MINIMAL_DATA_FLOW, true);
    }
    if (includedInDelimitedList("dfg", FLOW_ANAL_OPTION_DELIMITER, lFlowArg)) {
      flowRoot.setFlowAnalOption(FlowRoot.OPTION_STANDARD_DATA_FLOW, true);
    }
    if (lFlowArg.length() == 0) {
      flowRoot.setFlowAnalOption(FlowRoot.OPTION_STANDARD_DATA_FLOW, true);
    }

    lFlow.doHir();
    if (lSpec.getTrace().shouldTrace("HIR", 3)) {
      System.out.println("HIR after flow analysis.");
      hirRoot.ir.print(0);
    }

    return flowRoot;
  }

  /**
   *
   * HIR-level optimizations.
   *
   * @param hirRoot an HirRoot object.
   * @param symRoot a SymRoot object.
   * @param io an IoRoot object.
   * @throws IOException any IO error.
   * @throws PassException unrecoverable error(s) found in processing.
   **/
  protected void optimizeHirBeforeFlowAnalysis(HirRoot hirRoot,
    SymRoot symRoot,
    IoRoot io) throws IOException, PassException
  {
    CompileSpecification spec = io.getCompileSpecification();
    Trace trace = spec.getTrace();
    trace.trace(myName, 5000, "optimizeHirBeforeFlowAnalysis");
  }

  /**
   *
   * Basic HIR-level optimizations.
   *
   * @param hirRoot an HirRoot object.
   * @param hirFlowRoot an HIR-based flow analysis result.
   * @param symRoot a SymRoot object.
   * @param io an IoRoot object.
   * @throws IOException any IO error.
   * @throws PassException unrecoverable error(s) found in processing.
   **/
  protected void basicHIROptimizations(HirRoot hirRoot,
    FlowRoot hirFlowRoot,
    SymRoot symRoot,
    IoRoot io) throws IOException, PassException
  {
    CoinsOptions coinsOptions = io.getCompileSpecification().getCoinsOptions();
    //##70 if (coinsOptions.isSet(HIR_OPT_OPTION)) {
    new Opt(hirFlowRoot).doHir(); //##64
    //##70 }
  }

  /**
   *
   * HIR-level optimizations.
   *
   * @param hirRoot an HirRoot object.
   * @param hirFlowRoot an HIR-based flow analysis result.
   * @param symRoot a SymRoot object.
   * @param io an IoRoot object.
   * @throws IOException any IO error.
   * @throws PassException unrecoverable error(s) found in processing.
   **/
  protected void optimizeHirAfterFlowAnalysis(HirRoot hirRoot,
    FlowRoot hirFlowRoot,
    SymRoot symRoot,
    IoRoot io) throws IOException, PassException
  {
    CompileSpecification spec = io.getCompileSpecification();
    Trace trace = spec.getTrace();
    trace.trace(myName, 5000, "optimizeHirAfterFlowAnalysis");

    basicHIROptimizations(hirRoot, hirFlowRoot, symRoot, io);
    /* //##74
         if(spec.getCoinsOptions().isSet(MACRO_DATA_FLOW_OPTION)){
      MdfDriver mdfDriver = new MdfDriver(hirRoot, io, spec);
      mdfDriver.invoke();
         }
     */
    //##74
  }

  /**
   *
   * Makes an LIR tree from an HIR tree.
   *
   * @param hirRoot an HirRoot object.
   * @param io an IoRootobject.
   * @param sourceFile the source file name.
   * @param out output.
   * @param isLirOutput whether the out is for LIR or not.
   * @return a new LIR object.
   * @throws IOException any IO error.
   * @throws PassException unrecoverable error(s) found in processing.
   **/
  protected ImList makeNewLirFromHir(HirRoot hirRoot,
    IoRoot io,
    File sourceFile,
    OutputStream out,
    boolean isLirOutput) throws PassException
  {
    /*---- HIR to new LIR  Conversion ----*/
    CompileSpecification spec = io.getCompileSpecification();
    CoinsOptions coinsOptions = spec.getCoinsOptions();
    Trace trace = spec.getTrace();
    trace.trace(myName, 5000, "Make new LIR from HIR");

    if (trace.shouldTrace("HIR", 1)) {
      trace.trace("HIR", 1, "HIR before HirToNewLir conversion");
      ((HIR)hirRoot.programRoot).print(0, true);
    }
    if (trace.shouldTrace("Sym", 2)) {
      trace.trace("Sym", 2, "Sym before HirToNewLir conversion ");
      hirRoot.symRoot.symTable.printSymTableAll(hirRoot.symRoot.symTableRoot);
      hirRoot.symRoot.symTableConst.printSymTable();
    }

    ConvToNewLIR newconvert = new ConvToNewLIR(sourceFile, out, hirRoot);
    ImList sexp = newconvert.doConvert((HIR)hirRoot.programRoot);
    if (isLirOutput) {
      newconvert.print(sexp);
    }
    return sexp;
  }

  /**
   *
   * Tests whether a string includes a term as itself or one of its elements
   * delimited by a specified delimiter.  For example,
   * includedInDelimitedList("ab", ".", "ab.cd.ef") returns true, while
   * includedInDelimitedList("ab", ".", "abc.def") returns false.
   *
   * @param term the term to be searched for.
   * @param delimiter the delimiter.
   * @param list the string to be tested.
   * @return true if the list includes the term; false otherwise.
   **/
  protected boolean includedInDelimitedList(String term,
    char delimiter,
    String list)
  {
    int i;
    while ((i = list.indexOf(delimiter)) != -1) {
      if (term.equals(list.substring(0, i))) {
        return true;
      }
      else {
        list = list.substring(i + 1);
      }
    }
    return term.equals(list);
  }

  /**
   *
   * Breaks a string into a list with a specified delimiter.
   *
   * @param s the string.
   * @param delimiter the delimiter.
   * @return the list.
   **/
  protected List separateDelimitedList(String s, char delimiter)
  {
    List l = new ArrayList();
    int i;
    while ((i = s.indexOf(delimiter)) != -1) {
      l.add(s.substring(0, i));
      s = s.substring(i + 1);
    }
    l.add(s);
    return l;
  }
//##98 BEGIN
protected List separateDelimitedListExcludingQuote(String s, char delimiter)   
{                                                                
  List l = new ArrayList();                                      
  int i, leng, j1, j2;
  leng = s.length();
  String lStringBody=s;
  j1 = 0;
  j2 = leng;
	if ((lStringBody.charAt(0) == '"')||
		(lStringBody.charAt(0) == '\''))
	  j1 = 1;
	if ((lStringBody.charAt(leng-1) == '"')||
		(lStringBody.charAt(leng-1) == '\''))
	  j2 = leng-1;
	if ((j1 != 0)||(j2 != leng))
		lStringBody = lStringBody.substring(j1, j2);
  while ((i = lStringBody.indexOf(delimiter)) != -1) { 
	l.add(lStringBody.substring(0, i));
	lStringBody = lStringBody.substring(i + 1); 
  }
  l.add(lStringBody);  
  return l;                                                      
}
//##98 END
  
  private List initTimingList(CoinsOptions coinsOptions,
    String option,
    char delimiter)
  {
    if (coinsOptions.isSet(option)) {
      return separateDelimitedList(coinsOptions.getArg(option), delimiter);
    }
    else {
      return new ArrayList();
    }
  }

  private List initHirToCTimingList(CoinsOptions coinsOptions)
  {
    return initTimingList(coinsOptions,
      HIR_TO_C_OPTION,
      HIR_TO_C_OPTION_DELIMITER);
  }

  private List initDumpHirTimingList(CoinsOptions coinsOptions)
  {
    return initTimingList(coinsOptions,
      DUMP_HIR_OPTION,
      DUMP_HIR_OPTION_DELIMITER);
  }

  private List initLirToCTimingList(CoinsOptions coinsOptions)
  {
    return initTimingList(coinsOptions,
      LIR_TO_C_OPTION,
      LIR_TO_C_OPTION_DELIMITER);
  }

  /**
   *
   * Tests whether the specified timing specifier is included in the specified
   * <tt>-coins:hir2c</tt> option argument.
   *
   * @param timing the timng specifier.
   * @param hirToCTimings a List of String's.
   * @param coinsOptions a CoinsOption object.
   * @return true if the List includes the timing specifier.
   **/
  protected boolean matchHirToCTiming(String timing,
    List hirToCTimings,
    CoinsOptions coinsOptions)
  {
    if (coinsOptions.isSet(HIR_TO_C_OPTION)) {
      return hirToCTimings.contains(timing);
    }
    else {
      return false;
    }
  }

  /**
   *
   * Tests whether the specified timing specifier is included in the specified
   * <tt>-coins:dumphir</tt> option argument.
   *
   * @param timing the timng specifier.
   * @param dumpHirTimings a List of String's.
   * @param coinsOptions a CoinsOption object.
   * @return true if the List includes the timing specifier.
   **/
  protected boolean matchDumpHirTiming(String timing,
    List dumpHirTimings,
    CoinsOptions coinsOptions)
  {
    if (coinsOptions.isSet(DUMP_HIR_OPTION)) {
      return dumpHirTimings.contains(timing);
    }
    else {
      return false;
    }
  }

  /**
   *
   * Translates HIR-Base into a C source program and writes it to an
   * OutputStream.
   *
   * @param hirRoot an HirRoot object.
   * @param symRoot a SymRoot object.
   * @param io the <tt>IoRoot</tt>.
   * @param out an OutputStream to which the C source program is written.
   * @throws IOException any IO error.
   * @throws PassException unrecoverable error(s) found in processing.
   **/
  protected void callHirBaseToC(HirRoot hirRoot,
    SymRoot symRoot,
    IoRoot io,
    OutputStream out) throws IOException, PassException
  {
    CompileSpecification spec = io.getCompileSpecification();
    Trace trace = spec.getTrace();
    trace.trace(myName, 5000, "callHirBaseToC");

    HirBaseToCImpl HirToC;
    HirToC = new HirBaseToCImpl(hirRoot, symRoot,
      new PrintStream(out), trace);
    /* PrintStream should be OutputStream */
    HirToC.Converter();
  }

  /**
   *
   * Dump HIR-Base into an ObjectOutputStream.
   *
   * @param hirRoot an HirRoot object.
   * @param symRoot a SymRoot object.
   * @param io the <tt>IoRoot</tt>.
   * @param out an ObjectOutputStream to which the HIR is dumped.
   * @throws IOException any IO error.
   * @throws PassException unrecoverable error(s) found in processing.
   **/
  protected void dumpHirBase(HirRoot hirRoot,
    SymRoot symRoot,
    IoRoot io,
    ObjectOutputStream out) throws IOException, PassException
  {
    CompileSpecification spec = io.getCompileSpecification();
    Trace trace = spec.getTrace();
    trace.trace(myName, 5000, "dumpHirBase");

    try {
      out.writeObject(hirRoot.programRoot);
    }
    catch (NotSerializableException e) {
      String message
        = "Failed to dump HIR (" + e.getMessage() + " is not serializable).";
      io.msgError.put(message);
      throw new PassException(io.getSourceFile(), "dumphir", message);
    }
  }

  /**
   *
   * Aborts the current compile unit if the stopafterhir2c option is set and
   * all specified C source files are generated.
   *
   * @param timing the timing specifier.
   * @param hirToCTimings a List of String's.
   * @param coinsOptions a CoinsOption object.
   * @param source the source file name of the current compile unit.
   * @throws IOException any IO error.
   * @throws PassException unrecoverable error(s) found in processing.
   **/
  protected void checkHirToCStopCondition(String timing,
    List hirToCTimings,
    CoinsOptions coinsOptions,
    File source) throws IOException, PassException
  {
    if (coinsOptions.isSet(STOP_AFTER_HIR_TO_C_OPTION)) {
      hirToCTimings.remove(timing);
      if (hirToCTimings.size() == 0) {
        throw new PassStopException(source, "HIR to C", "Stop after HIR to C");
      }
    }
  }

  /**
   *
   * Translates HIR-Base into a C source program and writes it to an
   * OutputStream when <tt>-coins:hir2c=<i>t</i></tt> is specified and the
   * timing parameter equals to <i>t</i>.
   *
   * @param timing the timing parameter.
   * @param hirToCTimings a List of String's.
   * @param hirRoot an HirRoot object.
   * @param symRoot a SymRoot object.
   * @param io an IoRoot object.
   * @param out an OutputStream to which the C source program is written.
   * @return true if the C source code is written; false otherwise.
   * @throws IOException any IO error.
   * @throws PassException unrecoverable error(s) found in processing.
   **/
  protected boolean makeCSourceFromHirBase(String timing,
    List hirToCTimings,
    HirRoot hirRoot,
    SymRoot symRoot,
    IoRoot io,
    OutputStream out) throws IOException, PassException
  {
    CompileSpecification spec = io.getCompileSpecification();
    Trace trace = spec.getTrace();
    CoinsOptions coinsOptions = spec.getCoinsOptions();
    if (coinsOptions.isSet(HIR_TO_C_OPTION)) {
      trace.trace(myName, 5000,
                  "makeCSourceFromHirBase(" + timing + "): "
                  + coinsOptions.getArg(HIR_TO_C_OPTION));
    }
    else {
      trace.trace(myName, 5000,
                  "makeCSourceFromHirBase(" + timing + ")");
    }
    if (matchHirToCTiming(timing, hirToCTimings, coinsOptions)) {
      callHirBaseToC(hirRoot, symRoot, io, out);
      checkHirToCStopCondition(timing, hirToCTimings,
        coinsOptions, io.getSourceFile());
      return true;
    }
    else {
      return false;
    }
  }

  /**
   *
   * Translates HIR-Base into a C source program and writes it to a file name
   * as <tt>foo-hir-<i>t</i>.c</tt>. when <tt>-coins:hir2c=<i>t</i></tt> is
   * specified and the timing parameter equals to <i>t</i>.
   *
   * @param timing the timing parameter.
   * @param hirToCTimings a List of String's.
   * @param hirRoot an HirRoot object.
   * @param symRoot a SymRoot object.
   * @param io an IoRoot object.
   * @return true if the C soruce code is written; false otherwise.
   * @throws IOException any IO error.
   * @throws PassException unrecoverable error(s) found in processing.
   **/
  protected boolean makeCSourceFromHirBase(String timing,
    List hirToCTimings,
    HirRoot hirRoot,
    SymRoot symRoot,
    IoRoot io) throws IOException, PassException
  {
    CompileSpecification spec = io.getCompileSpecification();
    Trace trace = spec.getTrace();
    CoinsOptions coinsOptions = spec.getCoinsOptions();
    if (coinsOptions.isSet(HIR_TO_C_OPTION)) {
      trace.trace(myName, 5000,
                  "makeCSourceFromHirBase(" + timing + "): "
                  + coinsOptions.getArg(HIR_TO_C_OPTION));
    }
    else {
      trace.trace(myName, 5000,
                  "makeCSourceFromHirBase(" + timing + ")");
    }
    if (matchHirToCTiming(timing, hirToCTimings, coinsOptions)) {
      File source = io.getSourceFile();
      String sourcePath = source.getPath();
      String root = sourcePath.substring(0, sourcePath.lastIndexOf('.'));
      String dest = root.concat("-hir-" + timing + ".c");
      FileOutputStream out = new FileOutputStream(new File(dest));
      callHirBaseToC(hirRoot, symRoot, io, out);
      checkHirToCStopCondition(timing, hirToCTimings, coinsOptions, source);
      return true;
    }
    else {
      return false;
    }
  }

  /**
   *
   * Dumps HIR-Base into an ObjectOutputStream when
   * <tt>-coins:dumphir=<i>t</i></tt> is specified and the timing parameter
   * equals to <i>t</i>.
   *
   * @param timing the timing parameter.
   * @param dumpHirTimings a List of String's.
   * @param hirRoot an HirRoot object.
   * @param symRoot a SymRoot object.
   * @param io an IoRoot object.
   * @param out an ObjectOutputStream to which the C source program is written.
   * @return true if the C source code is written; false otherwise.
   * @throws IOException any IO error.
   * @throws PassException unrecoverable error(s) found in processing.
   **/
  protected boolean dumpHirBase(String timing,
    List dumpHirTimings,
    HirRoot hirRoot,
    SymRoot symRoot,
    IoRoot io,
    ObjectOutputStream out) throws IOException, PassException
  {
    CompileSpecification spec = io.getCompileSpecification();
    Trace trace = spec.getTrace();
    CoinsOptions coinsOptions = spec.getCoinsOptions();
    if (coinsOptions.isSet(DUMP_HIR_OPTION)) {
      trace.trace(myName, 5000,
                  "dumpHirBase(" + timing + "): "
                  + coinsOptions.getArg(DUMP_HIR_OPTION));
    }
    else {
      trace.trace(myName, 5000,
                  "dumpHirBase(" + timing + ")");
    }
    if (matchDumpHirTiming(timing, dumpHirTimings, coinsOptions)) {
      dumpHirBase(hirRoot, symRoot, io, out);
      return true;
    }
    else {
      return false;
    }
  }

  /**
   *
   * Dumps HIR-Base into a file name as <tt>foo-<i>t</i>.hir</tt>. when
   * <tt>-coins:dumphir=<i>t</i></tt> is specified and the timing parameter
   * equals to <i>t</i>.
   *
   * @param timing the timing parameter.
   * @param dumpHirTimings a List of String's.
   * @param hirRoot an HirRoot object.
   * @param symRoot a SymRoot object.
   * @param io an IoRoot object.
   * @return true if the C source code is written; false otherwise.
   * @throws IOException any IO error.
   * @throws PassException unrecoverable error(s) found in processing.
   **/
  protected boolean dumpHirBase(String timing,
    List dumpHirTimings,
    HirRoot hirRoot,
    SymRoot symRoot,
    IoRoot io) throws IOException, PassException
  {
    CompileSpecification spec = io.getCompileSpecification();
    Trace trace = spec.getTrace();
    CoinsOptions coinsOptions = spec.getCoinsOptions();
    if (coinsOptions.isSet(DUMP_HIR_OPTION)) {
      trace.trace(myName, 5000,
                  "dumpHirBase(" + timing + "): "
                  + coinsOptions.getArg(DUMP_HIR_OPTION));
    }
    else {
      trace.trace(myName, 5000, "dumpHirBase(" + timing + ")");
    }
    if (matchDumpHirTiming(timing, dumpHirTimings, coinsOptions)) {
      File source = io.getSourceFile();
      String sourcePath = source.getPath();
      String root = sourcePath.substring(0, sourcePath.lastIndexOf('.'));
      /* -c- is tentative implementation */
      String dest = root.concat("-c-" + timing + ".hir");
      FileOutputStream out = new FileOutputStream(new File(dest));
      ObjectOutputStream oout = new ObjectOutputStream(out);
      dumpHirBase(hirRoot, symRoot, io, oout);
      oout.close();
      return true;
    }
    else {
      return false;
    }
  }

  /**
   *
   * Tests whether the specified timing specifier is included in the specified
   * <tt>-coins:lir2c</tt> option argument.
   *
   * @param timing: the timing specifier.
   * @param lirToCTimings: a List of String's.
   * @param coinsOptions: a CoinsOption object.
   * @return true if the option argument includes the timing specifier.
   **/
  protected boolean matchLirToCTiming(String timing,
    List lirToCTimings,
    CoinsOptions coinsOptions)
  {
    if (coinsOptions.isSet(LIR_TO_C_OPTION)) {
      return lirToCTimings.contains(timing);
    }
    else {
      return false;
    }
  }

  /**
   *
   * Translates LIR into a C source program and writes it to an OutputStream.
   *
   * @param unit a Module object.
   * @param io the <tt>IoRoot</tt>.
   * @param dest a name of a file to which the C source program is written.
   * @throws IOException any IO error.
   * @throws PassException unrecoverable error(s) found in processing.
   **/
  protected void callLirToC(Module unit, IoRoot io, String dest) throws
    IOException, PassException
  {
    LirToC lir2c = new LirToC(unit, dest);
    lir2c.invoke();
  }

  /**
   *
   * Aborts the current compile unit if the stopafterlir2c option is set and
   * all specified C source files are generated.
   *
   * @param timing the timing specifier.
   * @param lirToCTimings a List of String's.
   * @param coinsOptions a CoinsOption object.
   * @param source the source file name of the current compile unit.
   * @throws IOException any IO error.
   * @throws PassException unrecoverable error(s) found in processing.
   **/
  protected void checkLirToCStopCondition(String timing,
    List lirToCTimings,
    CoinsOptions coinsOptions,
    File source) throws IOException, PassException
  {
    if (coinsOptions.isSet(STOP_AFTER_LIR_TO_C_OPTION)) {
      lirToCTimings.remove(timing);
      if (lirToCTimings.size() == 0) {
        throw new PassStopException(source, "LIR to C", "Stop after LIR to C");
      }
    }
  }

  /**
   *
   * Translates LIR into a C source program and writes it to an OutputStream
   * when <tt>-coins:lir2c=<i>t</i></tt> is specified and the timing parameter
   * equals to <i>t</i>.
   *
   * @param timing the timing parameter.
   * @param lirToCTimings a List of String's.
   * @param unit a Module object.
   * @param io an IoRoot object.
   * @param dest a name of a file to which the C source program is written.
   * @return true if the C source code is written; false otherwise.
   * @throws IOException any IO error.
   * @throws PassException unrecoverable error(s) found in processing.
   **/
  protected boolean makeCSourceFromLir(String timing,
    List lirToCTimings,
    Module unit,
    IoRoot io,
    String dest) throws IOException, PassException
  {
    CompileSpecification spec = io.getCompileSpecification();
    Trace trace = spec.getTrace();
    trace.trace(myName, 5000, "makeCSourceFromLir");

    CoinsOptions coinsOptions = spec.getCoinsOptions();
    if (matchLirToCTiming(timing, lirToCTimings, coinsOptions)) {
      callLirToC(unit, io, dest);
      checkLirToCStopCondition(timing, lirToCTimings, coinsOptions,
        io.getSourceFile());
      return true;
    }
    else {
      return false;
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
   * Translates LIR into a C source program and writes it to a file name as
   * <tt>foo-lir-<i>t</i>.c</tt>. when <tt>-coins:lir2c=<i>t</i></tt> is
   * specified and the timing parameter equals to <i>t</i>.
   *
   * @param timing the timing parameter.
   * @param lirToCTimings a List of String's.
   * @param unit an LModule object.
   * @param io an IoRoot object.
   * @return true if the C source code is written; false otherwise.
   * @throws IOException any IO error.
   * @throws PassException unrecoverable error(s) found in processing.
   **/
  protected boolean makeCSourceFromLir(String timing,
    List lirToCTimings,
    Module unit,
    IoRoot io) throws IOException, PassException
  {
    CompileSpecification spec = io.getCompileSpecification();
    Trace trace = spec.getTrace();
    trace.trace(myName, 5000, "makeCSourceFromLir");

    CoinsOptions coinsOptions = spec.getCoinsOptions();
    if (matchLirToCTiming(timing, lirToCTimings, coinsOptions)) {
      File source = io.getSourceFile();
      String sourcePath = source.getPath();
      String root = sourcePath.substring(0, sourcePath.lastIndexOf('.'));
      String dest = root.concat("-lir-" + timing + ".c");
      callLirToC(unit, io, dest);
      checkLirToCStopCondition(timing, lirToCTimings, coinsOptions, source);
      return true;
    }
    else {
      return false;
    }
  }

  //##82 BEGIN
  // Process the case where simulate option is specified.
  void processSimulate(
    IoRoot io,
    CompileSpecification spec,
    OutputStream out,
    coins.snapshot.SnapShot snap,
    ImList sexp,
    List lirToCTimings) throws IOException, PassException
  {
    SymRoot symRoot = io.symRoot;
    Trace trace = spec.getTrace();
    //   process options.
    CoinsOptions coinsOptions = spec.getCoinsOptions();
    CoinsOptions coinsopt= coinsOptions;
    String simv = coinsopt.getArg("simulate");
    int simulateCount = 0, simulateMem = 0;
    //  simulateCount = 0: none, 1: function, 2: bblock, 3: both

    if (simv == null) {
      simulateCount = 2;
    }
    else if (simv.equals("")) {
      simulateCount = 2;
    }
    else {
      Map lOptionMap = coinsopt.parseArgument(simv, '/', '.');
      List lKeyList = (List)lOptionMap.get("item_key_list");
      for (Iterator lIter = lKeyList.iterator();
           lIter.hasNext(); ) {
        String lOpt = ((String)lIter.next()).intern();
        if (lOpt == "memAccess") {
          simulateMem = 1;
        }
        else if (lOpt == "function") {
          simulateCount += 1;
        }
        else if (lOpt == "bblock") {
          simulateCount += 2;
        }
      }
    }
    // Simulation code generation.
    Root root = new Root(spec, new PrintWriter(System.out, true));
    String hostarch, hostspec;

    root.setSimulationData(new SimulationData());
    String targetName
      = coinsOptions.getArg(CommandLine.COINS_TARGET_NAME_OPTION);
    String targetConvention
      = coinsOptions.getArg(CommandLine.COINS_TARGET_CONVENTION_OPTION);
    trace.trace(myName, 5000, "target name = " + targetName);
    Module unit;

    //  1stly, get LIR at "after code generation"

    unit = Module.loadSLir(sexp, targetName, targetConvention, root);
    makeCSourceFromLir("new", lirToCTimings, unit, io);

    if (spec.getCoinsOptions().isSet("snapshot")) {
      snap.shot(unit, "Generated LIR");

    }
    unit.basicOptimization();

    makeCSourceFromLir("opt", lirToCTimings, unit, io);

    if (spec.getCoinsOptions().isSet("snapshot")) {
      snap.shot(unit, "Optimized LIR");

    }
    if (spec.getCoinsOptions().isSet("snapshot")) {
      snap.generateXml();

      //  unit.generateSimulationCodeACG(out);
    }
    unit.generateCodeWith(out,
      new String[] {
      "+BeforeCodeGeneration",
      "LateRewriting",
      "+AfterLateRewriting",
      "ToMachineCode",
      "+AfterToMachineCode",
    });

    if (trace.shouldTrace("Sym", 1)) {
      trace.trace("Sym", 1, "\nSym after code generation (ACG)");
      symRoot.symTable.printSymTableAllDetail(); //##70
    }

    ((SimulationData)(root.simulationData())).setACGModule(unit);

    //  2ndly, get LIR at "before code generation"

    unit = Module.loadSLir(sexp, targetName, targetConvention, root);
    makeCSourceFromLir("new", lirToCTimings, unit, io);

    if (spec.getCoinsOptions().isSet("snapshot")) {
      snap.shot(unit, "Generated LIR");

    }
    unit.basicOptimization();

    makeCSourceFromLir("opt", lirToCTimings, unit, io);

    if (spec.getCoinsOptions().isSet("snapshot")) {
      snap.shot(unit, "Optimized LIR");

    }
    if (spec.getCoinsOptions().isSet("snapshot")) {
      snap.generateXml();

      // unit.generateSimulationCodeBCG(out);
    }
    unit.generateCodeWith(out,
      new String[] {
      "+BeforeCodeGeneration"
    });

    if (trace.shouldTrace("Sym", 1)) {
      trace.trace("Sym", 1, "\nSym after code generation ");
      symRoot.symTable.printSymTableAllDetail(); //##70
    }

    ((SimulationData)(root.simulationData())).setBCGModule(unit);

    //  3rdly, get LIR at "before code generation of x86"

    CoinsOptions copt = spec.getCoinsOptions();
    getHostArchitectureParameters(copt);

    hostarch = copt.getArg("simulate-host-arch");
    hostspec = copt.getArg("simulate-host-spec");

    unit = Module.loadSLir(sexp, hostarch, hostspec, root);
    makeCSourceFromLir("new", lirToCTimings, unit, io);

    if (spec.getCoinsOptions().isSet("snapshot")) {
      snap.shot(unit, "Generated LIR");

    }
    unit.basicOptimization();

    makeCSourceFromLir("opt", lirToCTimings, unit, io);

    if (spec.getCoinsOptions().isSet("snapshot")) {
      snap.shot(unit, "Optimized LIR");

    }
    if (spec.getCoinsOptions().isSet("snapshot")) {
      snap.generateXml();

    }
    unit.generateCodeWith(out,
      new String[] {
      "+BeforeCodeGeneration"
    });

    ((SimulationData)(root.simulationData())).setBCGhostModule(unit);
    //  Adding Simulation log code
    //  rest processing of code generation: generate assemble code

    if (trace.shouldTrace("Sym", 1)) {
      trace.trace("Sym", 1, "\nSym before code generation host");
      symRoot.symTable.printSymTableAllDetail(); //##70
    }

    SimFuncTable simfunctable =
      ((SimulationData)(root.simulationData())).SetupSimFuncTable(
      simulateCount,
      simulateMem);

    simfunctable.symbolTable(root, simfunctable.acg, "Target");
    simfunctable.symbolTable(root, simfunctable.bcghost, "Host");

    if (spec.getCoinsOptions().isSet("simulateLog")) {
      System.out.println("Simulattion LIR ( before ):");
      ImList.printIt(root.debOut, unit.toSexp());
    }

    simfunctable.analyze(root, targetName, hostarch,
      simulateCount, simulateMem);

    if (spec.getCoinsOptions().isSet("simulateLog")) {
      System.out.println("Simulattion LIR (after ):");
      ImList.printIt(root.debOut, unit.toSexp());
    }

    //  unit.generateSimulationCodePostprocess(out);
    unit.generateCodeWith(out,
      new String[] {
      "LateRewriting",
      "+AfterLateRewriting",
      "ToMachineCode",
      "+AfterToMachineCode",
      "ConvToAsm"
    });

  } // processSimulate
  //##82 END

  /**
   *
   * Makes an LIR structure from an LIR source.
   *
   * @param in input.
   * @param io the <tt>IoRoot</tt>.
   * @return a new LIR object
   * @throws IOException any IO error.
   * @throws PassException unrecoverable error(s) found in processing.
   **/
  protected ImList makeLIRFromLIRSource(InputStream in, IoRoot io) throws
    IOException, PassException
  {
    Object tmp;
    try {
      tmp = ImList.readSexp(new PushbackReader(new InputStreamReader(in)));
      if (tmp == null || !(tmp instanceof ImList)) {
        throw new SyntaxErrorException("Bad New LIR.");
      }
    }
    catch (SyntaxError e) {
      throw new SyntaxErrorException(e.getMessage());
    }
    return (ImList)tmp;
  }


  /**
   *
   * Checks integrity of specified LIR related options:
   * <ul>
   *   <li> -coins:oldlir and -coins:newlir cannot be specified at same time.
   *        </li>
   *   <li> -coins:oldlir cannot be specified for LIR source files.</li>
   *   <li> -coins:oldir and -coins:out-newlir cannot be specified at same
   *        time.</li>
   * </ul>
   *
   * @param sourceFile the input file.
   * @param useOldLir whether -coins:oldlir is specified or not.
   * @param useNewLir whether -coins:newlir is specified or not.
   * @param isLirOutput whether -coins:suffixoption=out-newlir is specified or
   * not.
   * @param skipHIR whether the input file is an LIR source file or not.
   * @param io the <tt>IoRoot</tt>.
   * @throws PassException at least one integrity condition is not satisfied.
   **/
  protected void checkLIROptionsIntegrity(File sourceFile,
    boolean useOldLir,
    boolean useNewLir,
    boolean isLirOutput,
    boolean skipHIR,
    IoRoot io) throws PassException
  {
    Trace trace = io.getCompileSpecification().getTrace();
    trace.trace(myName, 5000,
                "checkLIROptionsIntegrity:"
                + " useOldLir = " + useOldLir
                + " useNewLir = " + useNewLir
                + " isLirOutput = " + isLirOutput
                + " skipHIR = " + skipHIR);
    if (useOldLir) {
      PassException e;
      e = new PassException(sourceFile.getPath(),
        "COINS option " + OLD_LIR_OPTION
        + " is obsoleted");
      io.msgError.put(e.getMessage());
      throw e;
    }
  }

  /**
   *
   * Interprets -On option into COINS options.
   *
   * @param spec a CompileSpecification object.
   * @param coinsOptions a CoinsOptions object.
   **/
  protected void setOptimizationOptions(CompileSpecification spec,
    CoinsOptions coinsOptions,
    boolean useNewLir)
  {
    if (spec.isSet(CompileSpecification.OPTIMIZE_LEVEL)) {
      List argList = (List)spec.getArg(CompileSpecification.OPTIMIZE_LEVEL);
      int optLevel;
      if (argList.size() == 0) {
        optLevel = DEFAULT_OPTIMIZATION_LEVEL;
      }
      else {
        optLevel = 0;
        Iterator i = argList.iterator();
        while (i.hasNext()) {
          int l = Integer.parseInt((String)i.next());
          if (l > optLevel) {
            optLevel = l;
          }
        }
      }
      if (optLevel > MAX_OPTIMIZATION_LEVEL) {
        optLevel = MAX_OPTIMIZATION_LEVEL;
      }
      spec.getTrace().trace("Driver", 1, " Specified optimization level= " + optLevel); //##100
      if (HIR_OPTIMIZATION_ARGS[optLevel] != null) {
        coinsOptions.set(HIR_OPT_OPTION, HIR_OPTIMIZATION_ARGS[optLevel]);
        spec.getTrace().trace("Driver", 1, "set hirOpt= " + HIR_OPTIMIZATION_ARGS[optLevel]); //##100    
      }
      if ((SSA_OPTIMIZATION_ARGS[optLevel] != null) && useNewLir) {
        coinsOptions.set(SSA_OPTION, SSA_OPTIMIZATION_ARGS[optLevel]);
        spec.getTrace().trace("Driver", 1, "set ssaOpt= " + SSA_OPTIMIZATION_ARGS[optLevel]); //##100    
      }
      if ((LIR_OPTIMIZATION_ARGS[optLevel] != null) && useNewLir) {
        //##100 coinsOptions.set(LIR_OPTIMIZATION_ARGS[optLevel]);
        //##100 BEGIN
     	String[] lirArgs = LIR_OPTIMIZATION_ARGS[optLevel];
     	for(int i = 0; i < lirArgs.length; i++){
          coinsOptions.set(lirArgs[i]);
          spec.getTrace().trace("Driver", 1, "set lirCoinsOpt= " + lirArgs[i]);    
     	}
      	//##100 END
      }
    }
  }

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
   *   <li> LIR Flow Analysis,
   *   <li> Optimization and Parallelization on LIR, and
   *   <li> Assembly code generation(*).
   * </ol>
   *
   * Five of the above (*) are already implemented.
   *
   * @param sourceFile the source file name.
   * @param suffix suffix rule of the source file.
   * @param in input.
   * @param out output.
   * @param io the <tt>IoRoot</tt>.
   * @throws IOException any IO error.
   * @throws PassException unrecoverable error(s) found in processing.
   **/
  public void compile(File sourceFile,
    Suffix suffix,
    InputStream in,
    OutputStream out,
    IoRoot io) throws IOException, PassException
  {

    CompileSpecification spec = io.getCompileSpecification();

    CoinsOptions coinsOptions = spec.getCoinsOptions();
    List hirToCTimings = initHirToCTimingList(coinsOptions);
    List dumpHirTimings = initDumpHirTimingList(coinsOptions);
    List lirToCTimings = initLirToCTimingList(coinsOptions);

    Trace trace = spec.getTrace();

    SymRoot symRoot = new SymRoot(io);
    HirRoot hirRoot = new HirRoot(symRoot);
    symRoot.attachHirRoot(hirRoot);
    symRoot.initiate();

    boolean useOldLir = false; //##1.4.4.3-
    boolean useNewLir = true; //##1.4.4.3-
    coinsOptions.set(NEW_LIR_OPTION); //##1.4.4.3-  no more old Lir

    trace.trace("Driver", 1, "Compile " + sourceFile);  //##100
    boolean skipHIR = suffix.getLanguageName().equals("LIR");
    boolean isLirOutput = false;
    if (suffix.getSuffixOption() != null) {
      trace.trace(myName, 5000, "suffix option: " + suffix.getSuffixOption());
      isLirOutput = suffix.getSuffixOption().equals(OUT_NEW_LIR_OPTION);
    }

    CheckOptions lCheckOptions = new CheckOptions(spec, io); //##71
    lCheckOptions.isOptionsAreCorrect(); //##71
    //##65 FlowRoot hirFlowRoot = null;
    FlowRoot lirFlowRoot = null;
    ImList sexp = null;
    coins.snapshot.SnapShot snap = null;
    if (coinsOptions.isSet("snapshot")) {
      snap = new coins.snapshot.SnapShot(sourceFile, "coins snapsnot");

    }
    checkLIROptionsIntegrity(sourceFile,
      useOldLir, useNewLir, isLirOutput, skipHIR, io);

    setOptimizationOptions(spec, coinsOptions, useNewLir);
    trace.trace(myName, 5000,
                "equivalent COINS options: " + coinsOptions.toString());

    if (!skipHIR) {
      /* pass 1 -- Source to HIR */
      makeHirFromSource(sourceFile, hirRoot, suffix, in, io);

      if (coinsOptions.isSet("snapshot")) {
        snap.shot(hirRoot, "Generated HIR");

        /* pass 2 -- HIR Optimizations Before Flow Analysis */
      }
      optimizeHirBeforeFlowAnalysis(hirRoot, symRoot, io);
      makeCSourceFromHirBase("new", hirToCTimings, hirRoot, symRoot, io);
      dumpHirBase("new", dumpHirTimings, hirRoot, symRoot, io);

      /* pass 3 -- flow analysis */
      if (coinsOptions.isSet(HIR_FLOW_ANAL_OPTION)) {
        hirFlowRoot = makeHIRFlowAnalysis(hirRoot, symRoot, io);
      }
      else {
        //##78 if (hirFlowRoot == null) { //##65
        //##94 BEGIN
        if (hirFlowRoot != null) {
          // Reset previous flow sym links.
          hirFlowRoot.resetAllFlowAnalSymLinks();
        }
        //##94 END
        hirFlowRoot = new FlowRoot(hirRoot);
        //##78 }
      }
      makeCSourceFromHirBase("flo", hirToCTimings, hirRoot, symRoot, io);
      dumpHirBase("flo", dumpHirTimings, hirRoot, symRoot, io);

      /* pass 4 -- HIR Optimizations & Parallelizations after Flow Analysis */
      optimizeHirAfterFlowAnalysis(hirRoot, hirFlowRoot, symRoot, io);
      makeCSourceFromHirBase("opt", hirToCTimings, hirRoot, symRoot, io);
      dumpHirBase("opt", dumpHirTimings, hirRoot, symRoot, io);

      /* pass 5 -- Reform HIR and check HIR and Symbol Table */
      //##70 BEGIN
      //##74 ReformHir lReformHir = new ReformHir(hirRoot);
      //##70 END

      //##74 BEGIN
      LoopPara lLoopPara = new LoopPara();
      if (coinsOptions.isSet(PARALLEL_DO_ALL)) {
        String lParallelArg = coinsOptions.getArg(PARALLEL_DO_ALL);
        if ((lParallelArg != null) &&
            includedInDelimitedList("OpenMP", COINS_OPTION_DELIMITER,
          lParallelArg)) {
          trace.trace(myName, 5000,
            "Do-all loop parallelization generating OpenMP");
          // Do-all-loop parallelization
          // to generate C with OpenMP directives.
          lLoopPara.hir2OpenMP(hirRoot, symRoot, io);
        }
        else if ((lParallelArg.charAt(0) >= '0') &&
                 (lParallelArg.charAt(0) <= '9')) {
          trace.trace(myName, 5000,
            "Do-all loop parallelization generating Machine code");
          // Parallelization of do-all loops generating machine code or
          ReformHir lReformHir = new ReformHir(hirRoot);
        }
        else {
          io.msgRecovered.put("Unknown option value " +
            lParallelArg + " for parallelDoAll. Ignore the option.");
        }
      }
      else if (coinsOptions.isSet(COARSE_GRAIN_PARALLEL) ||
               coinsOptions.isSet(CG_PARALLEL) ||
               coinsOptions.isSet("mdf")) {
        trace.trace(myName, 5000,
                    "Coarse grain parallelization generating OpenMP");
        // Macro Data Flow Parallelization
        MdfDriver mdfDriver = new MdfDriver(hirRoot, io, spec);
        // mdfDriver.invoke();
        mdfDriver.hir2OpenMP(hirRoot, symRoot, io, lLoopPara);
      }
      else if (coinsOptions.isSet(SIMULATE_OPTION)) {
        trace.trace(myName, 5000, "Generation of simulation code for profiling");
        // Generation of simulation code for profiling.
        ReformHir lReformHir = new ReformHir(hirRoot);
      }
      //##74 END

      testSym(hirRoot, io);
      testHir(hirRoot, hirFlowRoot, io);

      //##76 BEGIN
      // Simplify HIR by deleting null-else part, etc.
      // This does nothing if hirOpt=noSimplify is specified.
      SimplifyHir lSimplifyHir = new SimplifyHir(hirRoot, false);
      //##76 END
      if (coinsOptions.isSet("snapshot")) {
        snap.shot(hirRoot, "Optimized HIR");

        /* pass 6 -- HIR to LIR */
      }
      sexp = makeNewLirFromHir(hirRoot, io, sourceFile, out, isLirOutput);
      if (isLirOutput) {
        trace.trace(myName, 5000, "LIR file is created. Quitting compile.");
        return;
      }

    }
    else { /* (! useOldLir) && skipHIR */
      sexp = makeLIRFromLIRSource(in, io);
    }

    /* pass 7 -- Code generation */
    //##82 if (spec.getCoinsOptions().isSet("simulate"))
    if (! coinsOptions.isSet("simulate")) {
      /*  normal code generation */
      Root root = new Root(spec, new PrintWriter(System.out, true));
      String targetName
        = coinsOptions.getArg(CommandLine.COINS_TARGET_NAME_OPTION);
      String targetConvention
        = coinsOptions.getArg(CommandLine.COINS_TARGET_CONVENTION_OPTION);
      trace.trace(myName, 5000, "target name = " + targetName);

      //regpromote have priority over regpromote-ex  //##100 K. Mori (titech)
      if (coinsOptions.isSet("regpromote")){
      //System.out.println("Driver : attachRegpromote"); //##100 K. Mori (titech)
        coins.backend.contrib.RegPromote.attachRegPromote(root);
      }
      //##100 BEGIN  K. Mori (titech)
      else if(coinsOptions.isSet("regpromote-ex")){
    	  //System.out.println("Driver : attachRegpromoteEX");
          coins.backend.contrib.RegPromoteEX.attachRegPromoteEX(root);
      }
      //##100 END  K. Mori (titech)
      if (coinsOptions.isSet("pipelining")){
        Schedule.attachScheduleBefore(root);
          Schedule.attachScheduleAfter(root);
      }
      if (!coinsOptions.isSet("pipelining")){
         if (coinsOptions.isSet("schedule-after")) {
             Schedule.attachScheduleAfter(root);
         }
         if (coinsOptions.isSet("schedule")) {
           Schedule.attachScheduleBefore(root);
             Schedule.attachScheduleAfter(root);
         }
      }
      // Begin(2010.1)
      if(coinsOptions.isSet("regalloc")) {
 //   	  System.out.println("regalloc");
    	  String regallocval=coinsOptions.getArg("regalloc");
    	  if(regallocval.equals("oca")) {
    		  Function.registerAllocationTrig=OptimisticCoalescingAllocation.trig;
//    		  System.out.println("regalloc=oca");
    	  }
      }
      // End(2010.1)
      Module unit;
      unit = Module.loadSLir(sexp, targetName, targetConvention, root);
      makeCSourceFromLir("new", lirToCTimings, unit, io);

      if (coinsOptions.isSet("snapshot")) {
        snap.shot(unit, "Generated LIR");
      }

        /* LIR/SSA optimization */ //##1.4.4.3-
      boolean isLirSsaOpt = false;
      if (coinsOptions.isSet(coins.ssa.OptionName.LIR_OPT)) {
        unit.apply(new coins.ssa.LirOptDriver(unit, io, spec, coins.ssa.OptionName.LIR_OPT));
        isLirSsaOpt = true;
      }
      else if  (coinsOptions.isSet(coins.ssa.OptionName.LIR_OPT2)) {
        unit.apply(new coins.ssa.LirOptDriver(unit, io, spec, coins.ssa.OptionName.LIR_OPT2));
        isLirSsaOpt = true;
      }
      else if (coinsOptions.isSet(coins.ssa.OptionName.SSA_OPT)) {
        unit.apply(new coins.ssa.SsaDriver(unit, io, spec));
        isLirSsaOpt = true;
      }
      if (isLirSsaOpt)  {
        /* Simple/JumpOpt again */
          unit.apply(coins.backend.opt.SimpleOpt.trig);
          unit.apply(coins.backend.opt.JumpOpt.trig);
      }
      else {
          unit.basicOptimization();
      }

      if (coinsOptions.isSet("simd")) {
        unit.apply(new coins.simd.SimdDriver(unit, io, spec));
      }
      makeCSourceFromLir("opt", lirToCTimings, unit, io);

      if (coinsOptions.isSet("snapshot")) {
        snap.shot(unit, "Optimized LIR");

      }
      if (coinsOptions.isSet("snapshot")) {
        snap.generateXml();
      }


      unit.generateCode(out);

      if (trace.shouldTrace("Sym", 1)) {
        trace.trace("Sym", 1, "\nSym after code generation ");
        symRoot.symTable.printSymTableAllDetail(); //##70
      }
    }
    //##82 BEGIN
    else {
      // spec.getCoinsOptions().isSet("simulate")
      // Process the case of having "simulate" option.
      processSimulate(io, spec, out, snap, sexp, lirToCTimings);
    } // end of simulate
    //##82 END
  } // compile

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
   * @param sourceFile the source file name.
   * @param suffix suffix rule of the source file.
   * @param in the <tt>InputStream</tt>.
   * @param out the output <tt>File</tt>.
   * @param io the <tt>IoRoot</tt>.
   * @throws IOException any IO error.
   * @throws PassException unrecoverable error(s) found in processing.
   **/
  public void assemble(File sourceFile,
    Suffix suffix,
    InputStream in,
    File out,
    IoRoot io) throws IOException, PassException
  {
    Trace trace = io.getCompileSpecification().getTrace();

    trace.trace(myName, 5000, "assemble");
    List options = io.getCompileSpecification().getAssemblerOptions();
    options.add("-o");
    options.add(out.getPath());
    CoinsOptions coinsOptions = io.getCompileSpecification().getCoinsOptions();
    String commandName = DEFAULT_ASSEMBLER_NAME;
    if (coinsOptions.isSet(ASSEMBLER_NAME_OPTION)) {
      commandName = coinsOptions.getArg(ASSEMBLER_NAME_OPTION);
    }
    String[] commandLine = makeCommandLine(commandName, options);
    if (runProgram(commandLine, in, io.msgOut, io) != 0) {
      throw new PassException(sourceFile,
        "assembler", "Error(s) in assembler.");
    }
  }

  /**
   * Sets default linker options specified in a library file to the end of a
   * linker option string.
   *
   * @param spec a CompileSpecification object.
   * @param options a list of linker options where the options to be set.
   **/
  protected void setDefaultLinkerOptions(CompileSpecification spec,
    List options)
  {
    String s = defaultSettings.getProperty(DEFAULT_LINKER_OPTIONS_PROPERTY);
    if ((s != null) && (!s.equals(""))) {
      //##98 options.addAll(separateDelimitedList(s, ' '));
      //##98 BEGIN
        List lPaths = separateDelimitedListExcludingQuote(s, ' ');
        for (Iterator lIt = lPaths.iterator(); lIt.hasNext(); ) {
      	String lPath = (String)lIt.next();
      	lPath = "-L" + lPath;
      	options.add(lPath);
        }
        Trace trace = spec.getTrace();
        if (trace.shouldTrace(myName,5000)){
      	trace.trace(myName, 5000, "setDefaultLinkerOptions "+ s);
      	trace.trace(myName, 5000, "separateDelimitedListExcludingQuote "+ separateDelimitedListExcludingQuote(s, ' '));
      	trace.trace(myName, 5000, "resultant options "+ options);
        }
      //##98 END
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
   * @param out the output <tt>File</tt>.
   * @param io the <tt>IoRoot</tt>.
   * @throws IOException any IO error.
   * @throws PassException unrecoverable error(s) found in processing.
   **/
  public void link(File out, IoRoot io) throws IOException, PassException
  {
    CompileSpecification spec = io.getCompileSpecification();
    Trace trace = spec.getTrace();
    List options = spec.getLinkerOptions();
    options.add(0, "-o");
    options.add(1, out.getPath());
    setDefaultLinkerOptions(spec, options);
    trace.trace(myName, 5000, "link(1)");
    CoinsOptions coinsOptions = spec.getCoinsOptions();
    String commandName = DEFAULT_LINKER_NAME;
    if (coinsOptions.isSet(LINKER_NAME_OPTION)) {
      commandName = coinsOptions.getArg(LINKER_NAME_OPTION);
    }
    String[] commandLine = makeCommandLine(commandName, options);
    if (runProgram(commandLine, null, io.msgOut, io) != 0) {
      throw new PassException("linker", "Error(s) in linker.");
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

  /**
     Simulation program generator needs an interface for splitting target
     architecture and convention.
   **/
  /*
    Get HOST TARGET ARCHITECTURE = simulate-host
     HOST_ARCHITECTURE = x86
     HOST_SPECIFICATION = standard
   */
  private void getHostArchitectureParameters(CoinsOptions copt)
  {
    String lbopt = (String)copt.getArg("simulate-host");
    String lbname;
    String lbconv;
    if (lbopt == null) {
      lbname = "x86";
      lbconv = "standard";
    }
    else {
      int lbdi = lbopt.indexOf('-');
      if (lbdi == -1) {
        lbname = lbopt;
        lbconv = "standard";
      }
      else {
        lbname = lbopt.substring(0, lbdi);
        lbconv = lbopt.substring(lbdi + 1);
      }
    }
    copt.set("simulate-host-arch", lbname);
    copt.set("simulate-host-spec", lbconv);
    String dummy1;
    String dummy2;
    dummy1 = copt.getArg("simulate-host-arch");
    dummy2 = copt.getArg("simulate-host-spec");
  }

  /**
   *
   * Makes a compile specification from a command line.  Creates an compiler
   * driver API object giving the compile specification.  Creates a driver
   * implementation object and pass it to the API object to start compilation.
   * Exit()s with a return code of the compilation.
   *
   * @param args a command line.
   */
  protected void go(String[] args)
  {
    try {

      CompileSpecification spec = new CommandLine(args);
      loadDefaultSettings(spec);

      //
      // Initialize.
      //
      Root.init(spec);

      int status = new CompilerDriver(spec).go(this);
      System.exit(status);
    }
    catch (ParseException e) {
      System.err.println(e.getMessage());
      System.exit(CompileStatus.ABNORMAL_END);
    }
  }

  /**
   * A main function to invoke a driver instance.
   *
   * @param args a command line.
   **/
  public static void main(String[] args)
  {
    new Driver().go(args);
  }
}
