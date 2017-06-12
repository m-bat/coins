/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.mdf;

import coins.driver.CompileSpecification;
import coins.driver.CoinsOptions;
import coins.driver.Trace;
import coins.IoRoot;

import java.io.PrintStream;

/**
 * The environment of the MDF module.
 **/
public class MdfEnvironment{
  /** The threshold for debugging: trace=100 **/
  public static final int MsgThr=100;
  /** The threshold for debugging: trace=1000 **/
  public static final int MinThr=1000;
  /** The threshold for debugging: trace=1500 **/
  public static final int MdfThr=1500;
  /** The threshold for debugging: trace=2000 **/
  public static final int OptThr=2000;
  /** The threshold for debugging: trace=10000 **/
  public static final int AllThr=10000;
  /** The default number of threads **/
  public static final int DEFAULT_NUM_THREADS=2;

  /** The current standard output stream **/
  public final PrintStream output;
  /** The current compiler specification **/
  public final CompileSpecification spec;
  /** The current coins compiler option **/
  public final CoinsOptions opt;
  /** The current IoRoot **/
  public final IoRoot ioRoot;
  /** The current trace information **/
  public final Trace trace;
  /** The name of this module **/
  public final String MODULENAME="MDF";

  /**
   * Constructor:
   * @param root The current IoRoot
   * @param coinsSpec The current compiler specification
   **/
  public MdfEnvironment(IoRoot root,CompileSpecification coinsSpec){
    ioRoot=root;
    output=ioRoot.printOut;
    spec=coinsSpec;
    opt=spec.getCoinsOptions();
    trace=spec.getTrace();
  }

  /**
   * Get whether the module execute something.
   * @param threshold The specified value
   * @return True if the specified value is over the threshold
   **/
  boolean shouldDo(int threshold){
    return(trace.shouldTrace(MODULENAME,threshold));
  }

  /**
   * The debug output method. Output strings if the specified value is over
   * the threshold. And also, this method makes a new line.
   * @param str The debug string.
   * @param threshold The specified value
   **/
  void println(String str,int threshold){
    if(shouldDo(threshold)){
      output.println(str);
    }
  }

  /**
   * The debug output method. Output strings if the specified value is over
   * the threshold.
   * @param str The debug string.
   * @param threshold The specified value
   **/
  void print(String str,int threshold){
    if(shouldDo(threshold)){
      output.print(str);
    }
  }
}
