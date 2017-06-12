/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.lir;

import coins.backend.Debug;
import coins.backend.Op;
import coins.backend.Type;
import coins.backend.CantHappenException;
import coins.backend.util.ImList;
import coins.backend.util.QuotedString;


/** Represent Immediate String node. */
public class LirString extends LirNode {
  public final String string;

  public LirString(int id, String s) {
    super(id, Op.STRING, Type.UNKNOWN, null);
    string = s.intern();
  }

  public LirNode makeCopy(LirFactory fac) {
    return fac.stringconst(string);
  }

  public LirNode replaceOptions(LirFactory fac, ImList newOpt) {
    throw new CantHappenException();
  }

  public int hashCode() {
    return string.hashCode();
  }

  /** Convert to external LIR format. **/
  public Object toSexp() {
    return new QuotedString(string);
  }
    
  public String toString() {
    return QuotedString.quoteString(string);
  }

  public boolean equals(Object x) {
    return (x instanceof LirString && super.equals(x)
            && string == ((LirString)x).string);
  }

  public void accept(LirVisitor v) {
    // v.visit(this);
    // pending...
  }
}
