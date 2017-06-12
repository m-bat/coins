/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend;

import java.io.PrintWriter;
import java.io.IOException;
import java.io.OutputStream;
import coins.backend.util.ImList;
import coins.driver.CoinsOptions;
import coins.driver.CompileSpecification;

/**
 * Back end of COINS compiler.
 * This class exists only for backward compatibility.
 * New driver does not need it.
 */
public class BackEnd {

  /** BackEnd's global variables. **/
  private Root root;

  public BackEnd(CompileSpecification spec, PrintWriter debOut) {
    root = new Root(spec, debOut);
  }

  /** Compiler Back End. **/
  public void doIt(ImList sexp, OutputStream codeStream)
    throws SyntaxErrorException, IOException {

    CoinsOptions opts = root.spec.getCoinsOptions();
    Module compileUnit = Module.loadSLir(sexp, opts.getArg("target"), "standard", root);
    compileUnit.basicOptimization();
    compileUnit.generateCode(codeStream);
  }

}
