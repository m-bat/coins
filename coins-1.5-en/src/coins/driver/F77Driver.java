/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.driver;

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

/**
 * A FORTRAN compiler driver.
 **/

public class F77Driver extends Driver {
  /*
   * A property name of the default setting file, which specifies a location
   * of libf2c to construct a -L option. 
   */
  protected static final String LIBF2C_LOCATION = "libf2cLocation";

  /**
   * A main function to invoke a FORTRAN driver instance.
   *
   * @param args a command line.
   **/
  public static void main(String[] args) {
    new F77Driver().go(args);
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
    if ((spec != null)&&(spec.getCoinsOptions() != null)&&
        (spec.getCoinsOptions().getArg("linker") != null)&&
	spec.getCoinsOptions().getArg("linker").equals("g77"))
    	;
    else
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
  
  /**
   * HIR tree creation from source code.
   *
   * @param sourceFile the source file.
   * @param hirRoot an HirRoot object
   * @param suffix suffix rule of the source file.
   * @param in an input stream from which the C source program can be read
   * @param io an IoRoot object
   * @throws IOException any IO error
   * @throws PassException unrecoverable error(s) found in processing
   **/
  
  protected void makeHirFromSource(File sourceFile,
                                   HirRoot hirRoot,
                                   Suffix  suffix,
                                   InputStream in,
                                   IoRoot io)
  throws IOException, PassException {
    hirRoot.programRoot
      = makeHirFromFortranSource(sourceFile, hirRoot, in, io);
  }
}
