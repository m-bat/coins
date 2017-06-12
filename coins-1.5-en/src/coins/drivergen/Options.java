/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.drivergen;

import coins.driver.Driver;
import coins.driver.Suffix;
import coins.IoRoot;

import coins.driver.CompileSpecification;
import coins.driver.Trace;
import coins.driver.CoinsOptions;
import coins.driver.CommandLine;


import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;


public class Options {
  /** source file name which is the argument of the process **/
  public static File sourceFile;

  /** suffix rule of the source file which is the argument of process **/
  public static Suffix suffix;

  /** input which is the argument of process **/
  public static InputStream in;

  /** output which is the argument of process **/
  public static OutputStream out;

  /** IoRoot which is the argument of process **/
  public static IoRoot io;

  /** A name in trace messages.  Derived classes can override it. **/
  protected String myName = "Options";

  /** Compilespecification **/
  public CompileSpecification spec;

  /** CoinsOptions **/
  public CoinsOptions coinsOptions;

  /** trace message class **/
  public Trace trace;

  /* -------------------------------------------------------------------- */
  /**
   *
   * A default preprocessor command name, which is used to invoke a process if
   * not specified by a corresponding compile option.
   **/
  public static final String DEFAULT_PREPROCESSOR_NAME = "cpp";

  /**
   *
   * A default assembler command name, which is used to invoke a process if
   * not specified by a corresponding compile option.
   **/
  public static final String DEFAULT_ASSEMBLER_NAME = "gas";

  /**
   *
   * A default linker command name, which is used to invoke a process if not
   * specified by a corresponding compile option.
   **/
  public static final String DEFAULT_LINKER_NAME = "gcc";

  /**
   * An option name to specify a preprocessor command name.  When
   * "<tt>-coins:preprocessor=<i>foo</i></tt>" is specified as a compile
   * option, a command <i>foo</i> is used to invoke a process instead of the
   * default setting.
   **/
  public static final String PREPROCESSOR_NAME_OPTION = "preprocessor";

  /**
   * An option name to specify an assembler command name.  When
   * "<tt>-coins:assembler=<i>foo</i></tt>" is specified as a compile option,
   * a command <i>foo</i> is used to invoke a process instead of the default
   * setting.
   **/
  public static final String ASSEMBLER_NAME_OPTION = "assembler";

  /**
   * An option name to specify a linker command name.  When
   * "<tt>-coins:linker=<i>foo</i></tt>" is specified as a compile option, a
   * command <i>foo</i> is used to invoke a process instead of the default
   * setting.
   **/
  public static final String LINKER_NAME_OPTION = "linker";

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
  public static final String HIR_TO_C_OPTION = "hir2c";

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
  public static final String DUMP_HIR_OPTION = "dumphir";

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
  public static final String LIR_TO_C_OPTION = "lir2c";

  /**
   * A delimiter sign to delimit arguments of HIR_TO_C_OPTION.
   **/
  public static final char HIR_TO_C_OPTION_DELIMITER = '/';

  /**
   * A delimiter sign to delimit arguments of DUMP_HIR_OPTION.
   **/
  public static final char DUMP_HIR_OPTION_DELIMITER = '/';

  /**
   * A delimiter sign to delimit arguments of LIR_TO_C_OPTION.
   **/
  public static final char LIR_TO_C_OPTION_DELIMITER = '/';

  /**
   * An option name to terminate compilation of current file after HIR-Base to
   * C is performed.
   **/
  public static final String STOP_AFTER_HIR_TO_C_OPTION = "stopafterhir2c";

  /**
   * An option name to terminate compilation of current file after LIR to C is
   * performed.
   **/
  public static final String STOP_AFTER_LIR_TO_C_OPTION = "stopafterlir2c";

  /**
   * An option name to invoke HIR flow analysis.
   **/
  public static final String HIR_FLOW_ANAL_OPTION = "hirAnal";

  /**
   * A delimiter sign to delimit arguments of HIR_FLOW_ANAL_OPTION.
   **/
  public static final char FLOW_ANAL_OPTION_DELIMITER = '/';

  /**
   * The maximum optimization level specified by -O option.
   **/
  public static final int MAX_OPTIMIZATION_LEVEL = 4;

  /**
   * The implied optimization level when -O option is specified without a
   * level.
   **/
  public static final int DEFAULT_OPTIMIZATION_LEVEL = 1;

  /**
   * An option name to invoke HIR optimization
   **/
  public static final String HIR_OPT_OPTION = "hirOpt";

  /**
   * An option argument for HIR_OPT_OPTION
   **/
  public static final String HIR_OPT_ARG_FROMC = "fromc";

  /**
   * A delimiter sign to delimit arguments of HIR_OPT_OPTION.
   **/
  public final static char OPT_OPTION_DELIMITER = '/';

  /**
   * An option name to specify target architecture.
   **/
  public static final String TARGET_ARCH_OPTION = "target-arch";

  /**
   * An option name to specify target architecture convention.
   **/
  public static final String TARGET_CONVENTION_OPTION = "target-convention";

  /**
   * An option name to specify whether HIR is checked before converting HIR to
   * LIR.
   **/
  public static final String CHECK_HIR_OPTION = "testHir";

  /**
   * An option name to specify whether a Symbol Table is checked before
   * converting HIR to LIR.
   **/
  public static final String CHECK_SYMBOL_TABLE_OPTION = "testSym";

  /**
   * An option name to specify an old version of LIR shuould be used.
   **/
  public static final String OLD_LIR_OPTION = "oldlir";

  /**
   * An option name to specify a new version of LIR shuould be used.
   **/
  public static final String NEW_LIR_OPTION = "newlir";

  /**
   * A default option name to specify whether version of LIR shuould be used.
   **/
  public static final String DEFAULT_LIR_OPTION = NEW_LIR_OPTION;

  /**
   * A suffix option to write a new LIR source as compile output.
   **/
  public static final String OUT_NEW_LIR_OPTION = "out-newlir";

  /**
   * Site-local default settings.
   **/
  ////public Properties defaultSettings;

  /**
   * A default setting file name.
   **/
  public static final String DEFAULT_SETTING = "settings";

  /**
   * A default setting property name of linker options.
   **/
  ////public static final String DEFAULT_LINKER_OPTIONS_PROPERTY
  ////= "defaultLinkerOptions";

  /**
   * A default setting property name of system include path options.
   **/
  public static final String SYSTEM_INCLUDE_PATH_PROPERTY
  = "systemIncludePath";

  /**
   * An option name to specify SSA options.
   **/
  public static final String SSA_OPTION = "ssa-opt";

  /**
   * An option name to invoke SMP parallelization.
   **/
  public static final String MACRO_DATA_FLOW_OPTION = "mdf";

  /**
   * Option name to specify debuggin mode.
   **/
  public static final String DEBUG_OPTION = "debug";

  /* ---------------------------------------------------------------------- */

  /**
   *
   * Save the options
   *
   * @param args a command line.
   */
  public Options(File sourceFile,
                 Suffix suffix,
                 InputStream in,
                 OutputStream out,
                 IoRoot io) {

    this.sourceFile = sourceFile;
    this.suffix = suffix;
    this.in = in;
    this.out = out;
    this.io = io;

    spec = io.getCompileSpecification();
    coinsOptions = spec.getCoinsOptions();
    trace = spec.getTrace();

  }
}
