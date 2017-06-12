/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.lir;

import coins.backend.Debug;
import coins.backend.Op;
import coins.backend.Type;
import coins.backend.sym.Label;
import coins.backend.util.ImList;
import coins.backend.util.QuotedString;


/** Represent LABEL node */
public class LirLabelRef extends LirNode {

  /** Label this node holds **/
  public final Label label;

  /** Variant id */
  public final int variant;

  public LirLabelRef(int id, int op, int type, Label lab, ImList opt) {
    super(id, op, type, opt);
    label = lab;
    variant = 0;
  }

  public LirLabelRef(int id, int op, int type, Label lab, int variant, ImList opt) {
    super(id, op, type, opt);
    label = lab;
    this.variant = variant;
  }

  public LirNode makeCopy(LirFactory fac) {
    return this;
  }

  public LirNode replaceOptions(LirFactory fac, ImList newOpt) {
    return fac.labelRef(opCode, type, label, newOpt);
  }

  /** Convert to external LIR format. **/
  public Object toSexp() {
    ImList list;
    if (type == Type.UNKNOWN)
      list = ImList.list(Op.toName(opCode),
                         new QuotedString(label.toString()));
    else
      list = ImList.list(Op.toName(opCode), Type.toString(type),
                         new QuotedString(label.toString()));
    return list.append(opt.makeCopy());
  }
    
  public String toString() {
    return "(" + Op.toName(opCode)
      + (type == Type.UNKNOWN ? "" : Debug.TypePrefix + Type.toString(type))
      + " \"" + label + "\""
      + (variant != 0 ? " $" + variant : "")
      + (Debug.showId ? " &id " + id : "")
      + (opt.atEnd() ? "" : (" " + opt.toStringWOParen()))
      + ")";
  }

  public boolean equals(Object x) {
    return (x instanceof LirLabelRef && super.equals(x)
            && label == ((LirLabelRef)x).label
            && variant == ((LirLabelRef)x).variant);
  }

  public int hashCode() {
    return label.hashCode() ^ (variant << 16);
  }

  public void accept(LirVisitor v) {
    v.visit(this);
  }
}
