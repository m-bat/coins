/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.lparallel;
import coins.ffront.F77Scanner;
import coins.ffront.Parser;
import coins.ffront.Scanner;
import coins.ir.hir.HIR;
import coins.HirRoot;
import coins.IoRoot;
import coins.PassException;
import coins.SymRoot;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
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
import coins.ir.hir.HIR;
import coins.ir.hir.Program;
import coins.ir.hir.SubpDefinition;
import coins.opt.Opt;
/**
 *
 *
 * A driver implementation using COINS Compiler Driver API.
 *
 *
 *
 **/
public class F77LoopPara extends LoopPara {
    private static final String HIR_OPT_OPTION = "hirOpt";
    private final static char OPT_OPTION_DELIMITER = '/';
    protected static final String LIBF2C_LOCATION = "libf2cLocation";
    protected static final String DEFAULT_OPENMP_NAME = "omcc";
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
		      IoRoot io)
    throws IOException, PassException {

    FlowRoot hirFlowRoot;
    CoinsOptions coinsOptions = io.getCompileSpecification().getCoinsOptions();
    boolean hirAnal = coinsOptions.isSet("hirAnal");
    boolean hirOpt = coinsOptions.isSet(HIR_OPT_OPTION);


    fstophir2c = coinsOptions.isSet("hir2c");

    Trace trace = io.getCompileSpecification().getTrace();

    SymRoot symRoot  = new SymRoot(io);
    HirRoot hirRoot  = new HirRoot(symRoot);
    symRoot.attachHirRoot(hirRoot);
    symRoot.initiate();


    makeHirFromSource(sourceFile, hirRoot,suffix, in, io);


    if (hirOpt) {
        hirFlowRoot = new FlowRoot(hirRoot);
        optimizeHir(hirRoot, hirFlowRoot, symRoot, io);  
    }
    LoopParallel(hirRoot,symRoot,io);


    trace.trace(myName, 5000, "compile(pass 4 -- HIR to C Source) "); 
    if(makeCSourceFromHirBase("loop",hirRoot,symRoot,io) == true) {

         /* pass 5 -- OpenMP Compile  */
          trace.trace(myName, 5000, "compile(pass 5 -- OpenMP Compile)"); 
        if(fstophir2c == false) {
            OpenMPCompile("loop",hirRoot,symRoot,out,io);
	}else { 
            trace.trace(myName, 5000, "compile(PASSExcept)"); 
          throw new PassException("HIR to C","Stop HIR TO C");
	}
        trace.trace(myName, 5000, "compile(END)"); 
    }
  }
    /**
   * Sets default linker options.
   * <ol>
   *   <li> Sets default linker options specified in the default settings.
   *        </li>
   *   <li> Sets a -L option if the location of libf2c is specified in the
   *        default settings. </li>
   *   <li> Sets a -lf2c option. </li>
   * </ol>
   *
   * @param spec a CompileSpecification object.
   * @param options a list of linker options where the options to be set.
   **/
  protected void setDefaultLinkerOptions(CompileSpecification spec,
                                         List options) {
    super.setDefaultLinkerOptions(spec, options);
    String s = defaultSettings.getProperty(LIBF2C_LOCATION);
    if ((s != null) && (! s.equals(""))) {
      options.add("-L" + s);
    }
    options.add("-lf2c");
    options.add("-lm");
  }

  /**
   * HIR tree creation from source code.
   *
   * @param sourceFile the source file.
   * @param hirRoot an HirRoot object.
   * @param in an input stream from which the C source program can be read.
   * @param io an IoRoot object.
   * @throws IOException any IO error.
   * @throws PassException unrecoverable error(s) found in processing.
   **/
  protected HIR makeHirFromFortranSource(File sourceFile,
                                         HirRoot hirRoot,
                                         InputStream in,
                                         IoRoot io)
  throws IOException, PassException {

    Scanner scanner = null;
    SymRoot symRoot = hirRoot.symRoot;

    try {
      scanner = new F77Scanner(in, io);
    } catch (RuntimeException re) {
      throw re;
    } catch (Exception e) {
      io.msgError.put("syntax erorr: " + e.getMessage());
      throw new PassException(sourceFile,
                              "FORTRAN preprocessor",
                              "syntax error: " + e.getMessage());
    }
    Parser yyparser = new Parser(symRoot, hirRoot, io, scanner);
    try {
      yyparser.yyparse(scanner);
    } catch (Parser.yyException ye) {
      io.msgError.put("syntax error: " + ye.getMessage());
      throw new PassException(sourceFile,
                              "FORTRAN parser",
                              "syntax error: " + ye.getMessage());
    }

    if (io.getCompileSpecification().getCoinsOptions().isSet("printhir")) {
      hirRoot.programRoot.print(0);
      io.printOut.print("\n");
      symRoot.symTable.printSymTableAllDetail(symRoot.symTableRoot);
      symRoot.symTableConst.printSymTableDetail();
      io.printOut.print("\n");
    }
    return (coins.ir.hir.HIR)hirRoot.programRoot;
  }
   protected void makeHirFromSource(File sourceFile,
                                   HirRoot hirRoot,
                                   Suffix  suffix,
                                   InputStream in,
                                   IoRoot io)
  throws IOException, PassException {
    hirRoot.programRoot
      = makeHirFromFortranSource(sourceFile, hirRoot, in, io);
  }
   /**
   * A main function.<br>
   *
   **/
   public static void main(String[] args) {
        new F77LoopPara().go(args);
  }
}
