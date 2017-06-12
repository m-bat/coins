/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.lir;

import coins.backend.Debug;
import coins.backend.Op;
import coins.backend.Type;
import coins.backend.util.ImList;


/**
 * Represent integer constant
 */
public class LirIconst extends LirNode {

  /** Value of this node */
  public final long value;

  public LirIconst(int id, int t, long v, ImList opt) {
    super(id, Op.INTCONST, t, opt);
    value = v;
  }

  public LirNode makeCopy(LirFactory fac) {
    return fac.iconst(type, value, opt);
  }

  public LirNode replaceOptions(LirFactory fac, ImList newOpt) {
    return fac.iconst(type, value, newOpt);
  }

  /** Return signed value of this node. **/
  public long signedValue() {
    int bits = Type.bits(type);
    if (bits >= 64)
      return value;
    else {
      long signbit = 1L << (bits - 1);
      long mask = (1L << bits) - 1;
      return -((value & signbit) << 1) | (value & mask);
    }
  }

  /** Return unsigned value of this node. **/
  public long unsignedValue() {
    if (Type.bits(type) >= 64)
      return value;
    else
      return value & ((1L << Type.bits(type)) - 1);
  }

  public int hashCode() {
    return (int)(value ^ (value >> 32) ^ type);
  }

  /** Convert to external LIR format. **/
  public Object toSexp() {
    if (opt.locate("&untagged") != null)
      return Long.toString(value).intern();

    return ImList.list(Op.toName(opCode), Type.toString(type), value + "")
      .append(opt.makeCopy());
  }
    
  public String toString() {
    if (opt.locate("&untagged") != null)
      return Long.toString(value);

    return "(" + Op.toName(opCode) + Debug.TypePrefix + Type.toString(type)
      + " " + value
      + (Debug.showId ? " &id " + id : "")
      + ")";
  }

  public boolean equals(Object x) {
    return (x instanceof LirIconst && super.equals(x)
            && value == ((LirIconst)x).value);
  }

  public void accept(LirVisitor v) {
    v.visit(this);
  }


  /** @deprecated **/
  public String toStringExp() {
    return Long.toString(signedValue());
  }
}
