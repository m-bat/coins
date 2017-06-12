/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.hir2c;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.ParseException;

import coins.FlowRoot;
import coins.HirRoot;
import coins.IoRoot;
import coins.PassException;
import coins.SymRoot;
import coins.driver.CommandLine;
import coins.driver.CompileSpecification;
import coins.driver.CompileStatus;
import coins.driver.CompilerDriver;
import coins.driver.Driver;
import coins.driver.Trace;
import coins.cfront.Cfront; //##71
import coins.ir.hir.HIR;

/**
*
* A driver implementation using COINS Compiler Driver API.
* This is to produce C source from HIR structure.
**/

public class Hir2C
  //##71 extends Driver
  extends Cfront
{
/**
*
*  hir2c ( HIR-BASE to C Source Program)
*
* @param hirRoot an HirRoot object
* @param symRoot a SymRoot object
* @param io an IoRoot object
**/
    private void makeCSourceFromHirBase(HirRoot hirRoot,
              SymRoot symRoot,
              IoRoot ioRoot) {
        HirBaseToCImpl HirToC;
        Trace trace = ioRoot.getCompileSpecification().getTrace();
        HirToC = new HirBaseToCImpl(hirRoot,symRoot,ioRoot.printOut,trace);
        HirToC.Converter();
    }

    /**
    * Compiler.<br>
    *
    * This compiler has five passes:
    * <ol>
    *   <li> C source to HIR-C,
    *   <li> HIR-C to HIR-Base,
    *   <li> Flow analysis on HIR,
    *   <li> Optimization and Parallelization on HIR,
    *   <li> HIR to C source.
    * </ol>
    *
    * @param sourceFile the source file name.
    * @param in input.
    * @param out output.
    * @param spec the command line options and arguments.
    **/
    public void compile(File sourceFile,
          InputStream in,
          OutputStream out,
          IoRoot pio)
    throws IOException, PassException {
        CompileSpecification spec;
        spec=pio.getCompileSpecification();
        boolean hirAnal = spec.getCoinsOptions().isSet("hirAnal");
        boolean lirAnal = spec.getCoinsOptions().isSet("lirAnal");

        IoRoot io = new IoRoot(sourceFile,
         System.out, new PrintStream(out),
         System.err, spec);
        SymRoot symRoot  = new SymRoot(io);
        HirRoot hirRoot  = new HirRoot(symRoot);
        symRoot.attachHirRoot(hirRoot);
        symRoot.initiate();

        /* pass 1 -- AST to HIR-C */
        hirRoot.programRoot = makeHirCFromCSource(hirRoot, in, io);
        spec.getTrace().trace(myName, 5000, "compile(1)");

        /* pass 2 -- HIR-C to HIR-Base */
        hirRoot.programRoot = makeHirBaseFromC(hirRoot, (HIR)hirRoot.programRoot, io);
        spec.getTrace().trace(myName, 5000, "compile(2)");

        /* pass 3 -- flow analysis */
        FlowRoot hirFlowRoot;
        if (hirAnal)
            hirFlowRoot = makeHIRFlowAnalysis(hirRoot, symRoot, io);
        else
            hirFlowRoot = new FlowRoot(hirRoot); //##12
        spec.getTrace().trace(myName, 5000, "compile(3)");

        /* pass 4 -- Optimization & Parallelization in HIR will be here. */
        //##26 optimizeHir(hirRoot, hirFlowRoot, symRoot, io);
        optimizeHirAfterFlowAnalysis(hirRoot, hirFlowRoot, symRoot, io); //##26
        spec.getTrace().trace(myName, 5000, "compile(4)");

        /* pass 5 -- hir2c  (HIR-Base to C Source) */
        makeCSourceFromHirBase(hirRoot, symRoot, io);

        Trace trace = io.getCompileSpecification().getTrace();
        if (io.getCompileSpecification().getTrace().shouldTrace("Sym", 1)) {
            trace.trace(myName, 1, "\nSym after code generation ");
            symRoot.symTable.printSymTableAllDetail(symRoot.symTableRoot);
            symRoot.symTableConst.printSymTableDetail();
        }
        throw new PassException(sourceFile,
                            myName, "No further processing is required.");

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
    public static void main(String[] args) {
  try {
            CompileSpecification spec = new CommandLine(args);
            int status = new CompilerDriver(spec).go(new Hir2C());
            System.exit(status);
  } catch (ParseException e) {
      System.err.println(e.getMessage());
      System.exit(CompileStatus.ABNORMAL_END);
  }
    }
}

