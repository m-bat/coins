/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.lir;

import coins.backend.CantHappenException;
import coins.backend.Debug;
import coins.backend.Op;
import coins.backend.Type;
import coins.backend.sym.Symbol;
import coins.backend.util.ImList;
import coins.backend.util.QuotedString;


/** STATIC/FRAME/REG LirNode */
public class LirSymRef extends LirNode {

  /** Symbol this node holds */
  public final Symbol symbol;

  // /** Variant id (for experimental SSA) **/
  // public final int variant;

  public LirSymRef(int id, int op, int t, Symbol sym, ImList opt) {
    super(id, op, t, opt);
    symbol = sym;
    // variant = 0;
  }

  // public LirSymRef(int id, int op, int t, Symbol sym, int variant, ImList opt) {
  //   super(id, op, t, opt);
  //  symbol = sym;
  //  this.variant = variant;
  // }

  public LirNode makeCopy(LirFactory fac) {
    return this;
  }

  public LirNode replaceOptions(LirFactory fac, ImList newOpt) {
    return fac.symRef(opCode, type, symbol, newOpt);
  }


  /** Return true if node is a physical register. **/
  public boolean isPhysicalRegister() {
    return opCode == Op.REG && symbol.name.charAt(0) == '%';
  }

  /** Convert to external LIR format. **/
  public Object toSexp() {
    return ImList.list(Op.toName(opCode), Type.toString(type),
                       new QuotedString(symbol.name))
      .append(opt.makeCopy());
  }

  public String toString() {
    return "(" + Op.toName(opCode)
      + Debug.TypePrefix + Type.toString(type)
      + " " + symbol
      + (Debug.showId ? " &id " + id : "")
      + (opt.atEnd() ? "" : (" " + opt.toStringWOParen()))
      + ")";
  }

  public boolean equals(Object x) {
    return (x instanceof LirSymRef && super.equals(x)
            && symbol == ((LirSymRef)x).symbol
            // && variant == ((LirSymRef)x).variant
            );
  }

  public int hashCode() {
    return symbol.hashCode();
  }

  public void accept(LirVisitor v) {
    v.visit(this);
  }


  /** @deprecated **/
  public String toStringExp() {
    if (opCode == Op.STATIC)
      return symbol.name;
    else
      throw new CantHappenException("not an address expression: " + this);
  }
}
