/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.simd;

import coins.backend.Module;
import coins.backend.lir.LirFactory;
import coins.driver.CompileSpecification;
import coins.driver.CoinsOptions;
import coins.driver.Trace;

import java.io.PrintWriter;

class SimdEnvironment{
  public static final int MsgThr=100;
  public static final int MinThr=1000;
  public static final int SimdThr=1500;
  public static final int OptThr=2000;
  public static final int AllThr=10000;

  public final PrintWriter output;
  public final CompileSpecification spec;
  public final CoinsOptions opt;
  public final Trace trace;
  public final String MODULENAME="SIMD";
  public final Module module;
  public final LirFactory lir;

  SimdEnvironment(Module m,CompileSpecification coinsSpec,PrintWriter writer){
    module=m;
    lir=m.newLir;
    output=writer;
    spec=coinsSpec;
    opt=spec.getCoinsOptions();
    trace=spec.getTrace();
  }

  boolean shouldDo(int threshold){
    return(trace.shouldTrace(MODULENAME,threshold));
  }

  void println(String str,int threshold){
    if(shouldDo(threshold)){
      output.println(str);
    }
  }

  void print(String str,int threshold){
    if(shouldDo(threshold)){
      output.print(str);
    }
  }
}
