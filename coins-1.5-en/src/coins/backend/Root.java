/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend;

import java.io.PrintWriter;
import java.io.OutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.lang.Class;
import java.lang.NoSuchMethodException;
import java.lang.SecurityException;
import java.util.Map;
import java.util.HashMap;
import coins.driver.CoinsOptions;
import coins.driver.CompileSpecification;
import coins.driver.Trace;
import coins.backend.util.BiList;

/** BackEnd global variables **/
public class Root {

  /** Default plugin directory. **/
  private static final String CONTRIBDIR = "coins.backend.contrib";

  /** Stream to print debugging information. **/
  public final PrintWriter debOut;

  /** Whether or not generating source-level debugging information. **/
  public final boolean sourceDebugInfo;

  /** Loop Inversion Flag. **/
  public final boolean optLoopInversion;

  /** True if live range splitting will be taken place. **/
  public final boolean liveRangeSplitting;

  /** Flag indicating whether or not print laptime. **/
  public final boolean dispIntervalTime;
  public final boolean GCflush;

  /** Interval Timer Object. **/
  public final IntervalTimer timer;


  /** Compiler command line options **/
  public final CompileSpecification spec;
  public final Trace trace;

  /** Code generator in Java **/
  public final boolean javaCG;

  /** Optional pass spec. **/
  public final String additionalPass;


  /** Table for hooks i.e. extension programs. **/
  private Map hookTable = new HashMap();


  /** option **/
  private CoinsOptions opts;

  /** Assembler output stream. **/
  private PrintWriter asmWriter;

  /** Data for simulation **/
  private Object simulationData;


  /** Obsolete initializer... **/
  public static void init(CompileSpecification spec) {}



  /** Create BackEnd's global variables.
   ** @deprecated **/
  public Root(CompileSpecification spec, PrintWriter debOut, OutputStream asmOut) {
    this(spec, debOut);
    setAsmStream(asmOut);
  }

  /** Create BackEnd's global variables. **/
  public Root(CompileSpecification spec, PrintWriter debOut) {

    this.spec = spec;
    this.debOut = debOut;

    trace = spec.getTrace();
    dispIntervalTime = trace.shouldTrace("backtime");
    GCflush = trace.shouldTrace("backgc");

    opts = spec.getCoinsOptions();
    optLoopInversion = opts.isSet("loopinversion");
    sourceDebugInfo = opts.isSet("debuginfo");
    liveRangeSplitting = opts.isSet("liverangesplit");

    additionalPass = opts.getArg("lirpass");

    javaCG = !opts.isSet("schemecg");

    timer = new IntervalTimer();

    /** Load and initialization of plugin classes. **/
    for (String plugins = spec.getCoinsOptions().getArg("attach");
         plugins != null;  plugins = nextSlash(plugins))
      attach(getSlashedWord(plugins));
  }


  /** Return options **/
  public boolean isOptionSet(String optionName) {
    return opts.isSet(optionName);
  }

    

  /** Load extension module. **/
  public void attach(String className) {
    if (className.indexOf('.') < 0)
      className = CONTRIBDIR + "." + className;
    try {
      Class custom = Class.forName(className);
      Method m = custom.getDeclaredMethod
        ("attach", new Class[]{Class.forName
                               ("coins.driver.CompileSpecification"),
                               Class.forName("coins.backend.Root")
        });
      m.invoke(null, new Object[]{spec, this});

    } catch (ClassNotFoundException e) {
      throw new Error("Class not found: " + e.getMessage());
    } catch (NoSuchMethodException e) {
      throw new Error("No such method: " + e.getMessage());
    } catch (SecurityException e) {
      throw new Error("Security Exception: " + e.getMessage());
    } catch (IllegalAccessException e) {
      throw new Error("Illegal Access Exception: " + e.getMessage());
    } catch (IllegalArgumentException e) {
      throw new Error("Illegal Argument Exception: " + e.getMessage());
    } catch (InvocationTargetException e) {
      throw new Error("Invocation Target Exception: " + e.getMessage());
    }
  }


  /** Set assembler output stream.
   ** @deprecated **/
  public void setAsmStream(OutputStream stream) {
    asmWriter = new PrintWriter(stream);
  }

  /** Return assembler output stream.
   ** @deprecated **/
  public PrintWriter asmWriter() { return asmWriter; }


  /** Set data object for simulator. **/
  public void setSimulationData(Object data) {
    this.simulationData = data;
  }

  /** Return data object for simulator. **/
  public Object simulationData() {
    return simulationData;
  }


  /** Return true if trace designated <code>tag</code> should be done. **/
  public boolean traceOK(String tag, int thresh) {
    int level = trace.getTraceLevel(tag);
    return level >= thresh;
  }


  /** Register transformer to named hook. **/
  public void registerTransformer(Transformer trans) {
    if (trans != null)
      setHook(trans.name(), trans);
  }


  /** Set a new value to named hook.
   ** @param name name of hook.
   ** @param val value set to hook. **/
  public void setHook(String name, Object val) {
    hookTable.put(name, val);
  }
  

  /** Add a new value to named hook.
   ** @param name name of hook.
   ** @param val value added to hook. **/
  public void addHook(String name, Object val) {
    BiList list = (BiList)hookTable.get(name);
    if (list == null) {
      list = new BiList();
      hookTable.put(name, list);
    }
    list.add(val);
  }


  /** Return value of hook.
   ** @param name name of hook.
   ** @return list of hook objects. **/
  public Object getHook(String name) {
    return hookTable.get(name);
  }


  /** Return first word delimited by '/'. **/
  private static String getSlashedWord(String s) {
    int n = s.indexOf('/');
    if (n < 0)
      return s;
    else
      return s.substring(0, n);
  }


  /** Remove first word delimited by '/'. **/
  private static String nextSlash(String s) {
    int n = s.indexOf('/');
    if (n < 0 || n + 1 == s.length())
      return null;
    else
      return s.substring(n + 1);
  }

}
