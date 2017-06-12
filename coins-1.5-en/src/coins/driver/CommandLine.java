/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.driver;

import coins.IoRoot;

import java.io.File;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.text.ParseException;

/**
 *
 * Abstraction of a COINS compiler command line.<br>
 *
 * This class is used when the compiler is invoked from command line.
 * A COINS compiler command line is in the form:
 *
 * <blockquote>
 * <tt>
 * java <i>driver</i> [{<i>option</i> | <i>file</i> }] ...
 * </tt>
 * </blockquote>
 *
 * `option' is a string starting with a `-'.  As for compile options, see
 * CompileSpecification in more detail.
 *
 * @see CompileSpecification
 **/

public class CommandLine implements CompileSpecification, Serializable {
  public final  static String COINS_TARGET_OPTION = "target";
  public final static String COINS_DEFAULT_TARGET_NAME = "sparc";
  public final static String COINS_TARGET_NAME_OPTION = "target-arch";
  public final static String COINS_TARGET_CONVENTION_OPTION
    = "target-convention";
  public final static String COINS_DEFAULT_TARGET_CONVENTION = "standard";

  private CoinsOptions fCoinsOptions;
  private Trace fTrace;
  private Warning fWarning;
  private Map fOptions;
  private List fSourceFiles;
  private String[] fArgs;
  private List fLinkerOptions;

  private boolean isSimpleFlag(String pOptionString) {
    if (pOptionString.equals(PREPROCESS_ONLY)) {
      return true;
    } else if (pOptionString.equals(COMPILE_ONLY)) {
      return true;
    } else if (pOptionString.equals(ASSEMBLE_ONLY)) {
      return true;
    } else if (pOptionString.equals(HELP)) {
      return true;
    } else if (pOptionString.equals(PIPE)) {
      return true;
    } else if (pOptionString.equals(PRESERVE_COMMENTS)) {
      return true;
    } else if (pOptionString.equals(INHIBIT_NUMBER_LINE)) {
      return true;
    } else if (pOptionString.equals(PRESERVE_SYMBOLS)) {
      return true;
    } else if (pOptionString.equals(PERFORMANCE_MONITOR)) {
      return true;
    } else if (pOptionString.equals(DYNAMIC_LINKAGE)) {
      return true;
    } else if (pOptionString.equals(STATIC_LINKAGE)) {
      return true;
    } else if (pOptionString.equals(VERBOSE)) {
      return true;
    } else {
      return false;
    }
  }

  private boolean isSingleArgOption(String pOptionString) {
    if (pOptionString.equals(OUTPUT_FILE)) {
      return true;
    } else if (pOptionString.equals(TARGET_ARCHITECTURE)) {
      return true;
    } else {
      return false;
    }
  }

  private boolean isTrailingArgOption(String pOptionString) {
    if (pOptionString.startsWith(TARGET_ARCHITECTURE)) {
      return true;
    } else {
      return false;
    }
  }

  private boolean isDuplicableOption(String pOptionString) {
    if (pOptionString.startsWith(WARNING_CATEGORY)) {
      return true;
    } else if (pOptionString.startsWith(DEFINE_MACRO)) {
      return true;
    } else if (pOptionString.startsWith(INCLUDE_PATH)) {
      return true;
    } else if (pOptionString.startsWith(UNDEFINE_MACRO)) {
      return true;
    } else if (pOptionString.startsWith(OPTIMIZE_LEVEL)) {
      return true;
    } else if (pOptionString.startsWith(LINK_PATH)) {
      return true;
    } else if (pOptionString.startsWith(ARCHIVE_TO_LINK)) {
      return true;
    } else {
      return false;
    }
  }

  private boolean isCoins(String pOptionString) {
    if (pOptionString.startsWith(COINS)) {
      return true;
    } else {
      return false;
    }
  }

  private Warning createWarning(Map pOptions) {
    if (pOptions.containsKey(WARNING_CATEGORY)) {
      return new Warning((List)pOptions.get(WARNING_CATEGORY));
    } else {
      return new Warning();
    }
  }

  private void resolveTargetArchitectureOptions(Map pOptions,
            CoinsOptions pCoinsOptions,
            List pDuplicatedOptions,
            boolean oneway) {
    /* When oneway is set, resolution is made only from COINS option to -b
       option.  */
    if ((! oneway) && fOptions.containsKey(TARGET_ARCHITECTURE)) {
      String lbopt = (String)fOptions.get(TARGET_ARCHITECTURE);
      String lbname;
      String lbconv;
      int lbdi = lbopt.indexOf('-');
      if (lbdi == -1) {
  lbname = lbopt;
  lbconv = COINS_DEFAULT_TARGET_CONVENTION;
      } else {
  lbname = lbopt.substring(0, lbdi );
  lbconv = lbopt.substring(lbdi + 1);
      }

      if (fCoinsOptions.isSet(COINS_TARGET_OPTION)) {
  String lcopt = fCoinsOptions.getArg(COINS_TARGET_OPTION);
  if (! lcopt.equals(lbopt)) {
    pDuplicatedOptions.add("-coins:" + COINS_TARGET_OPTION + lcopt);
  }
      } else if (fCoinsOptions.isSet(COINS_TARGET_NAME_OPTION)) {
  String lcname = fCoinsOptions.getArg(COINS_TARGET_NAME_OPTION);
  if (! lcname.equals(lbname)) {
    pDuplicatedOptions.add("-coins:" + COINS_TARGET_NAME_OPTION + "="
         + lcname);
  }
      } else if (fCoinsOptions.isSet(COINS_TARGET_CONVENTION_OPTION)) {
  String lcconv = fCoinsOptions.getArg(COINS_TARGET_CONVENTION_OPTION);
  if (! lcconv.equals(lbconv)) {
    pDuplicatedOptions.add("-coins:" + COINS_TARGET_NAME_OPTION + "="
         + lcconv);
  }
      } else {
  fCoinsOptions.set(COINS_TARGET_OPTION, lbopt);
  fCoinsOptions.set(COINS_TARGET_NAME_OPTION, lbname);
  fCoinsOptions.set(COINS_TARGET_CONVENTION_OPTION, lbconv);
      }
    } else if (fCoinsOptions.isSet(COINS_TARGET_OPTION)) {
      /* In this case, fOptions must not have TARGET_ARCHITECTURE option. */
      String lbopt = fCoinsOptions.getArg(COINS_TARGET_OPTION);
      String lbname;
      String lbconv;
      int lbdi = lbopt.indexOf('-');
      if (lbdi == -1) {
  lbname = lbopt;
  lbconv = COINS_DEFAULT_TARGET_CONVENTION;
      } else {
  lbname = lbopt.substring(0, lbdi );
  lbconv = lbopt.substring(lbdi + 1);
      }

      if (fCoinsOptions.isSet(COINS_TARGET_NAME_OPTION)) {
  String lcname = fCoinsOptions.getArg(COINS_TARGET_NAME_OPTION);
  if (! lcname.equals(lbname)) {
    pDuplicatedOptions.add("-coins:" + COINS_TARGET_NAME_OPTION + "="
         + lcname);
  }
      } else if (fCoinsOptions.isSet(COINS_TARGET_CONVENTION_OPTION)) {
  String lcconv = fCoinsOptions.getArg(COINS_TARGET_CONVENTION_OPTION);
  if (! lcconv.equals(lbconv)) {
    pDuplicatedOptions.add("-coins:" + COINS_TARGET_CONVENTION_OPTION + "="
         + lcconv);
  }
      } else {
  fOptions.put(TARGET_ARCHITECTURE, lbopt);
  fCoinsOptions.set(COINS_TARGET_NAME_OPTION, lbname);
  fCoinsOptions.set(COINS_TARGET_CONVENTION_OPTION, lbconv);
      }
    } else if (fCoinsOptions.isSet(COINS_TARGET_NAME_OPTION)) {
      /* In this case, fOptions must not have TARGET_ARCHITECTURE option. */
      String lcname;
      String lcconv;
      lcname = fCoinsOptions.getArg(COINS_TARGET_NAME_OPTION);
      if (fCoinsOptions.isSet(COINS_TARGET_CONVENTION_OPTION)) {
  lcconv = fCoinsOptions.getArg(COINS_TARGET_CONVENTION_OPTION);
      } else {
  lcconv = COINS_DEFAULT_TARGET_CONVENTION;
      }
      String lcopt = lcname + "-" + lcconv;
      fCoinsOptions.set(COINS_TARGET_OPTION, lcopt);
      fOptions.put(TARGET_ARCHITECTURE, lcopt);
    }
  }

  private void setTargetArchitecture() {
    /* set default */
    if (! fCoinsOptions.isSet(COINS_TARGET_NAME_OPTION)) {
      fCoinsOptions.set(COINS_TARGET_NAME_OPTION, COINS_DEFAULT_TARGET_NAME);
      fCoinsOptions.set(COINS_TARGET_CONVENTION_OPTION,
      COINS_DEFAULT_TARGET_CONVENTION);
      fOptions.put(TARGET_ARCHITECTURE,
       COINS_DEFAULT_TARGET_NAME + "-"
       + COINS_DEFAULT_TARGET_CONVENTION);
    }

    /* set -D__arch__ */
    if (fCoinsOptions.isSet(COINS_TARGET_NAME_OPTION)) {
      // 2005.03.11 I.Fukuda
      //String lArg = "__" + fOptions.get(COINS_TARGET_NAME_OPTION) + "__";
      String lArg = "__" + fCoinsOptions.getArg(COINS_TARGET_NAME_OPTION) + "__";
      fOptions.put(DEFINE_MACRO + lArg, lArg);
      List lList;
      if (fOptions.containsKey(DEFINE_MACRO)) {
  lList = (List)fOptions.get(DEFINE_MACRO);
      } else {
  lList = new ArrayList();
      }
      lList.add(lArg);
      fOptions.put(DEFINE_MACRO, lList);
    }
  }

  private void resolveEquivalentOptions(List lDuplicatedOptions,
          boolean oneway) {
    resolveTargetArchitectureOptions(fOptions, fCoinsOptions,
             lDuplicatedOptions, oneway);
  }

  private void warnUnknownOptions(List pList, Warning pWarning) {
    Iterator i = pList.iterator();
    while (i.hasNext()) {
      pWarning.warning("Driver", "Unknown option: " + i.next());
    }
  }

  private void warnDuplicatedOptions(List pList, Warning pWarning) {
    Iterator i = pList.iterator();
    while (i.hasNext()) {
      pWarning.warning("Driver",
           "Ignoring multiple specification: " + i.next());
    }
  }

  private void warnNoArgOptions(List pList, Warning pWarning) {
    Iterator i = pList.iterator();
    while (i.hasNext()) {
      pWarning.warning("Driver", "Missing argument: " + i.next());
    }
  }

  private CoinsOptions createCoinsOptions(List pList) {
    StringBuffer buffer = new StringBuffer();
    Iterator i = pList.iterator();
    if (i.hasNext()) {
      buffer.append(i.next());
      while (i.hasNext()) {
  buffer.append("," + i.next());
      }
    }
    return new CoinsOptions(buffer.toString());
  }

  private Trace createTrace(CoinsOptions pCoinsOptions, Warning pWarning) {
    if (pCoinsOptions.isSet(pCoinsOptions.TRACE)) {
      return new Trace(System.out, pCoinsOptions.getArg(pCoinsOptions.TRACE),
           pWarning);
    } else {
      return new Trace(System.out, pWarning);
    }
  }

  /**
   * Constructs a CommandLine object representing an empty command line string.
   **/
  public CommandLine() {
    fOptions = new Hashtable();
    fSourceFiles = new ArrayList();
    fArgs = new String[0];
    fLinkerOptions = new ArrayList();
    fWarning = createWarning(fOptions);
    fCoinsOptions = createCoinsOptions(new ArrayList());
    fCoinsOptions.readPropertyFile(fWarning);
    List lDuplicatedOptions = new ArrayList();
    resolveEquivalentOptions(lDuplicatedOptions, true);
    setTargetArchitecture();
    warnDuplicatedOptions(lDuplicatedOptions, fWarning);
    fTrace = createTrace(fCoinsOptions, fWarning);
  }

  /**
   * Constructs a CommandLine object representing a command line string.
   *
   * @param args The command line string.
   * @throws ParseException error(s) in the command line.
   **/
  public CommandLine(String[] args) throws ParseException {
    fOptions = new Hashtable();
    fSourceFiles = new ArrayList();
    fArgs = args;
    fLinkerOptions = new ArrayList();
    int len = args.length;
    List lCoinsArgumentList = new ArrayList();
    List lUnknownOptions = new ArrayList();
    List lDuplicatedOptions = new ArrayList();
    List lNoArgOptions = new ArrayList();

    for (int i = 0; i < len; i ++) {
      if (! args[i].equals("")) {
  if (isSimpleFlag(args[i])) {
    /* -C, -E, -P, -S, -c, -dynamic, -g, -p, -v, -pipe, -static */
    fOptions.put(args[i], "");
  } else if (isDuplicableOption(args[i])) {
    /* -D<macro>[=<defn>], -I<path>, -L<path>, -O<level>, -U<macro>,
       -W<category>, -l<archive>, */
    String lOptionArgument = args[i].substring(2);
    if (lOptionArgument.length() == 0) {
      if (args[i].equals(OPTIMIZE_LEVEL)) {
        args[i] = "-O1";
        lOptionArgument = "1";
      } else {
        throw new ParseException("No argument is found for a "
               + args[i].substring(0, 2) + " option.",
               i);
      }
    }
    fOptions.put(args[i], lOptionArgument);
    String lKey = args[i].substring(0, 2);
    List lList;
    if (fOptions.containsKey(lKey)) {
      lList = (List)fOptions.get(lKey);
    } else {
      lList = new ArrayList();
    }
    lList.add(lOptionArgument);
    fOptions.put(lKey, lList);
  } else if (isCoins(args[i])) {
    /* -coins:options */
    String lOptionArgument = args[i].substring(7);
    fOptions.put(args[i], lOptionArgument);
    lCoinsArgumentList.add(lOptionArgument);
    fOptions.put("-coins", lCoinsArgumentList);
  } else if (isSingleArgOption(args[i])) {
    /* -o outfile, -b arch */
    if (fOptions.containsKey(args[i])) {
      lDuplicatedOptions.add(args[i] + " " + args[i + 1]);
      i++;
      continue;
    } else {
      if (args.length > i + 1) {
        fOptions.put(args[i], args[i + 1]);
        i++;
      } else {
        throw new ParseException("No argument is found for a "
               + args[i] + " option.", i);
      }
    }
  } else if (isTrailingArgOption(args[i])) {
    /* -barch */
    String lOptionArgument = args[i].substring(2);
    if (lOptionArgument.length() == 0) {
      lNoArgOptions.add(args[i]);
      continue;
    } else {
      String lOptionName = args[i].substring(0, 2);
      if (fOptions.containsKey(lOptionName)) {
        lDuplicatedOptions.add(args[i]);
        continue;
      } else {
        fOptions.put(lOptionName, lOptionArgument);
      }
    }
  } else {
    if (args[i].charAt(0) == '-') {
      lUnknownOptions.add(args[i]);
    } else {
      fSourceFiles.add(args[i]);
      fLinkerOptions.add(args[i]);
    }
  }
  if (isLinkerOption(args[i])) {
    fLinkerOptions.add(args[i]);
  }
      }
    }
    fWarning = createWarning(fOptions);
    fCoinsOptions = createCoinsOptions(lCoinsArgumentList);
    resolveEquivalentOptions(lDuplicatedOptions, false);
    fCoinsOptions.readPropertyFile(fWarning);
    resolveEquivalentOptions(lDuplicatedOptions, true);
    setTargetArchitecture();
    warnUnknownOptions(lUnknownOptions, fWarning);
    warnDuplicatedOptions(lDuplicatedOptions, fWarning);
    warnNoArgOptions(lNoArgOptions, fWarning);
    fTrace = createTrace(fCoinsOptions, fWarning);
  }

  /**
   * Shows help messages.
   *
   * @param out A PrintStream to which the help messages are written.
   * @param driver A driver object which has the method `main'.
   **/
  public void showHelp(PrintStream out,
           CompilerImplementation driver) {
    String className = driver.getClass().getName();
    out.println("Usage: java " + className + " [options] file...");
    out.println("-help               Display this information");
    out.println("-E                  Stop after preprocessing");
    out.println("-S                  Stop after assembly code generation");
    out.println("-c                  Do not link");
    out.println("-o <file>           Set output file");
    out.println("-pipe               Use pipe instead of temporary files");
    out.println("-W<category>        Turn on warning messages of <category>");
    out.println("-Wno-<category>     Turn off warning messages of <category>");
    out.println("-Wall               Turn on all warning messages");
    out.println("-C                  Preserve comments in source file");
    out.println("-D<macro>[=<defn>]  Define a preprocessor macro");
    out.println("-I<path>            Specify an include path");
    out.println("-P                  Inhibit #line directive");
    out.println("-U<macro>           Undefine a preprocessor macro");
    out.println("-O<level>           Set optimization level");
    out.println("-g                  Preserve all symbols");
    out.println("-p                  Use performance monitor");
    out.println("-L<path>            Specify an archive search path");
    out.println("-dynamic            Use dynamic linkage");
    out.println("-l<archive>         Specify an archive");
    out.println("-static             Use static linkage");
  }

  /**
   * Returns an CoinsOptions object which represents -coins options included
   * in this command line.
   *
   * @return a CoinsOptions object.
   **/
  public CoinsOptions getCoinsOptions() {
    return fCoinsOptions;
  }

  /**
   * Registers a file as an object file of a specified source file.  Without
   * calling this method, getLinkerOptions cannot return a correct command
   * line arguments.
   *
   * @param sourceFile the source file name
   * @param objectFile the object file name
   **/
  public synchronized void setObjectFile(String sourceFile,
           String objectFile) {
    int i;
    int s = fLinkerOptions.size();
    for (i = 0; i < s; i ++) {
      String arg = (String)fLinkerOptions.get(i);
      if (arg.equals(sourceFile)) {
  fLinkerOptions.set(i, objectFile);
  return;
      }
    }
    throw new Error("COINS driver API internal error: there is no file "
        + sourceFile + " in linker options");
  }

  /**
   * Returns a Trace object which filters trace messages in a manner
   * specified in this command line.
   *
   * @return a Trace object.
   */
  public Trace getTrace() {
    return fTrace;
  }

  /**
   * Returns a Warning object which filters warning messages in a manner
   * specified in this command line.
   *
   * @return a Warning object.
   */
  public Warning getWarning() {
    return fWarning;
  }

  /**
   * Tests if an option is set or not in this command line.
   *
   * @param option the option string, including leading `-'.
   * @return <tt>true</tt> if the option is set, <tt>false</tt> otherwise.
   */
  public boolean isSet(String option) {
    return fOptions.containsKey(option);
  }

  /**
   * Returns an argument of an option in this command line.  When the option
   * is not set, a <tt>null</tt> is returned.  The argument is a
   * <tt>String</tt> for <tt>-o</tt>, a <tt>List</tt> of <tt>String</tt>s for
   * <tt>-D</tt>, <tt>-I</tt>, <tt>-L</tt>, <tt>-O</tt>, <tt>-U</tt>,
   * <tt>-W</tt> and <tt>-l</tt>.
   *
   * @param option the option string, including leading `-'.
   * @return the argument of <tt>option</tt>.
   **/
  public Object getArg(String option) {
    return fOptions.get(option);
  }

  /**
   * Return a <tt>List</tt> of source file names listed in this command line.
   *
   * @return the <tt>List</tt> of source files names.
   **/
  public List getSourceFiles() {
    List ret = new ArrayList();
    ret.addAll(fSourceFiles);
    return ret;
  }

  /**
   * Returns a <tt>String</tt> representation of this command line.
   *
   * @return the <tt>String</tt> representation of this command line.
   **/
  public String toString() {
    int len = fArgs.length;
    if (len > 0) {
      StringBuffer buffer = new StringBuffer(fArgs[0]);
      for (int i = 1; i < len; i ++) {
  buffer.append(" " + fArgs[i]);
      }
      return buffer.toString();
    } else {
      return "";
    }
  }

  /**
   *
   * Tests if the argument string is one of preprocessor options.  The
   * argument string should start with `-' to be judged as a preprocessor
   * option.
   *
   * @param arg the argument string
   * @return true if `arg' is one of the preprocessor options; false otherwise.
   **/
  public boolean isPreprocessorOption(String arg) {
    return arg.equals(PRESERVE_COMMENTS)
      || arg.startsWith(DEFINE_MACRO)
      || arg.startsWith(INCLUDE_PATH)
      || arg.equals(INHIBIT_NUMBER_LINE)
      || arg.startsWith(UNDEFINE_MACRO);
  }

  private void addCollectionToList(List pList, String key,
           Collection pCollection) {
    Iterator lI = pCollection.iterator();
    while (lI.hasNext()) {
      pList.add(key + (String)lI.next());
    }
  }

  /**
   *
   * Returns a <tt>List</tt> of <tt>String</tt>s containing all preprocessor
   * options specified in this command line.
   *
   * @return preprocessor options
   **/
  public List getPreprocessorOptions() {
    List lList = new ArrayList();

    if (fOptions.containsKey(PRESERVE_COMMENTS)) {
      lList.add(PRESERVE_COMMENTS);
    }
    if (fOptions.containsKey(DEFINE_MACRO)) {
      addCollectionToList(lList, DEFINE_MACRO,
        (Collection)fOptions.get(DEFINE_MACRO));
    }
    if (fOptions.containsKey(INCLUDE_PATH)) {
      addCollectionToList(lList, INCLUDE_PATH,
        (Collection)fOptions.get(INCLUDE_PATH));
    }
    if (fOptions.containsKey(INHIBIT_NUMBER_LINE)) {
      lList.add(INHIBIT_NUMBER_LINE);
    }
    if (fOptions.containsKey(UNDEFINE_MACRO)) {
      addCollectionToList(lList, UNDEFINE_MACRO,
        (Collection)fOptions.get(UNDEFINE_MACRO));
    }
    return lList;
  }

  /**
   *
   * Tests if the argument string is one of compiler options.  The argument
   * string should start with `-' to be judged as a compiler option.
   *
   * @param arg the argument string
   * @return true if `arg' is one of the compiler options; false otherwise.
   **/
  public boolean isCompilerOption(String arg) {
    return arg.startsWith(WARNING_CATEGORY)
      || arg.startsWith(OPTIMIZE_LEVEL)
      || arg.equals(PRESERVE_SYMBOLS)
      || arg.equals(PERFORMANCE_MONITOR);
  }

  /**
   *
   * Returns a <tt>List</tt> of <tt>String</tt>s containing all compiler
   * options specified in this command line.
   *
   * @return compiler options
   **/
  public List getCompilerOptions() {
    List lList = new ArrayList();

    if (fOptions.containsKey(WARNING_CATEGORY)) {
      addCollectionToList(lList, WARNING_CATEGORY,
        (Collection)fOptions.get(WARNING_CATEGORY));
    }
    if (fOptions.containsKey(OPTIMIZE_LEVEL)) {
      addCollectionToList(lList, OPTIMIZE_LEVEL,
        (Collection)fOptions.get(OPTIMIZE_LEVEL));
    }
    if (fOptions.containsKey(PRESERVE_SYMBOLS)) {
      lList.add(PRESERVE_SYMBOLS);
    }
    if (fOptions.containsKey(PERFORMANCE_MONITOR)) {
      lList.add(PERFORMANCE_MONITOR);
    }
    return lList;
  }

  /**
   *
   * Tests if the argument string is one of assembler options.  The argument
   * string should start with `-' to be judged as a assembler option.
   *
   * @param arg the argument string
   * @return true if `arg' is one of the assembler options; false otherwise.
   **/
  public boolean isAssemblerOption(String arg) {
    return false;
  }

  /**
   *
   * Returns a <tt>List</tt> of <tt>String</tt>s containing all assembler
   * options specified in this command line.
   *
   * @return assembler options
   **/
  public List getAssemblerOptions() {
    return new ArrayList();
  }

  /**
   *
   * Tests if the argument string is one of linker options.  The argument
   * string should start with `-' to be judged as a linker option.
   *
   * @param arg the argument string
   * @return true if `arg' is one of the linker options; false otherwise.
   **/
  public boolean isLinkerOption(String arg) {
    return arg.startsWith(LINK_PATH)
      || arg.equals(DYNAMIC_LINKAGE)
      || arg.equals(STATIC_LINKAGE)
      || arg.startsWith(ARCHIVE_TO_LINK)
    /* || arg.startsWith(TARGET_ARCHITECTURE) */ ;
    /* -b arch cannot be passed to the linker because the naming convention of
       the architecture differs. */
  }

  /**
   *
   * Returns a <tt>List</tt> of <tt>String</tt>s containing all linker options
   * specified in this command line.
   *
   * @return linker options
   **/
  public synchronized List getLinkerOptions() {
    List ret = new ArrayList();
    ret.addAll(fLinkerOptions);
    return ret;
  }

//##71 BEGIN
public Map
commandLineOptions()
{
  return fOptions;
}
//##71 END

}
