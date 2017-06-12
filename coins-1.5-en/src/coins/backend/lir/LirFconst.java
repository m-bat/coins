/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.lir;

import coins.backend.Debug;
import coins.backend.Op;
import coins.backend.Type;
import coins.backend.util.ImList;


/** Represent FLOATCONST node. */
public class LirFconst extends LirNode {
  public final double value;

  public LirFconst(int id, int t, double v, ImList opt) {
    super(id, Op.FLOATCONST, t, opt);
    value = v;
  }

  public LirNode makeCopy(LirFactory fac) {
    return fac.fconst(type, value, opt);
  }

  public LirNode replaceOptions(LirFactory fac, ImList newOpt) {
    return fac.fconst(type, value, newOpt);
  }

  public int hashCode() {
    long v = Double.doubleToLongBits(value);
    return (int)(v ^ (v >> 32) ^ type);
  }

  /** Convert to external LIR format. **/
  public Object toSexp() {
    if (opt.locate("&untagged") != null)
      return Double.toString(value).intern();
    
    return ImList.list(Op.toName(opCode), Type.toString(type), value + "")
      .append(opt.makeCopy());
  }
    
  public String toString() {
    if (opt.locate("&untagged") != null)
      return Double.toString(value);

    return "(" + Op.toName(opCode) + Debug.TypePrefix + Type.toString(type)
      + " " + value
      + (Debug.showId ? " &id " + id : "")
      + ")";
  }

  public boolean equals(Object x) {
    return (x instanceof LirFconst && super.equals(x)
            && value == ((LirFconst)x).value);
  }

  public void accept(LirVisitor v) {
    v.visit(this);
  }
}

