/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend;

import coins.backend.sym.SymStatic;
import coins.backend.util.ImList;
import java.io.PrintWriter;

/** Common attributes of Function/Data, components of Module. **/
public abstract class ModuleElement {

  /** Module this element belongs to */
  public final Module module;

  /** Collection of global variables. **/
  public final Root root;

  /** Symbol table entry of this element. */
  public final SymStatic symbol;

  /** Source line position. **/
  public final int sourceLineNo;

  /** Initialize fields. **/
  ModuleElement(Module module, String name) throws SyntaxError {
    this.module = module;
    root = module.root;
    symbol = (SymStatic)module.globalSymtab.get(name);
    if (symbol == null)
      throw new SyntaxError("Undefined symbol: " + name);
    symbol.setBody(this);
    sourceLineNo = module.getCurrentLineNo();
  }

  /** Initialize fields. **/
  ModuleElement(Module module, SymStatic symbol) {
    this.module = module;
    root = module.root;
    this.symbol = symbol;
    symbol.setBody(this);
    sourceLineNo = module.getCurrentLineNo();
  }

  /** Convert to external LIR format. **/
  public abstract Object toSexp();

  /** Print in standard form. **/
  public abstract void printStandardForm(PrintWriter out);

  /** Print for debugging. **/
  public abstract void printIt(PrintWriter out);

  /** Print for debugging with specified analysis. **/
  public abstract void printIt(PrintWriter out, LocalAnalyzer[] anals);
}
