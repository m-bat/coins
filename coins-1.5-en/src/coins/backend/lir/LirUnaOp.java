/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.lir;

import coins.backend.Op;
import coins.backend.sym.Label;
import coins.backend.util.ImList;


/** Represents unary operators */
public class LirUnaOp extends LirNode {
  LirNode kid;

  public LirUnaOp(int id, int op, int t, LirNode operand, ImList opt) {
    super(id, op, t, opt);
    kid = operand;
  }

  public LirNode makeCopy(LirFactory fac) {
    return fac.operator(opCode, type, kid.makeCopy(fac), opt);
  }

  public LirNode makeShallowCopy(LirFactory fac) {
    return fac.operator(opCode, type, kid, opt);
  }

  public LirNode replaceOptions(LirFactory fac, ImList newOpt) {
    return fac.operator(opCode, type, kid, newOpt);
  }

  public int nKids() { return 1; }

  public LirNode kid(int n) {
    if (n == 0)
      return kid;
    else
      throw new IllegalArgumentException();
  }

  public void setKid(int n, LirNode x) {
    if (n == 0)
      kid = x;
    else
      throw new IllegalArgumentException();
  }

  public boolean equals(Object x) {
    return (x instanceof LirUnaOp && super.equals(x)
            && kid.equals(((LirUnaOp)x).kid));
  }

  public void accept(LirVisitor v) { v.visit(this); }

}

