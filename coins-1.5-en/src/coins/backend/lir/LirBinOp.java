/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.lir;

import coins.backend.util.ImList;


/** Represents binary operators */
public class LirBinOp extends LirNode {
  LirNode kid0, kid1;

  public LirBinOp(int id, int op, int t, LirNode operand0, LirNode operand1,
                  ImList optlist) {
    super(id, op, t, optlist);
    kid0 = operand0;
    kid1 = operand1;
  }

  public LirNode makeCopy(LirFactory fac) {
    return fac.operator(opCode, type, kid0.makeCopy(fac), kid1.makeCopy(fac),
                        opt);
  }

  public LirNode makeShallowCopy(LirFactory fac) {
    return fac.operator(opCode, type, kid0, kid1, opt);
  }

  public LirNode replaceOptions(LirFactory fac, ImList newOpt) {
    return fac.operator(opCode, type, kid0, kid1, newOpt);
  }


  public int nKids() { return 2; }

  public LirNode kid(int n) {
    if (n == 0)
      return kid0;
    else if (n == 1)
      return kid1;
    else
      throw new IllegalArgumentException();
  }

  public void setKid(int n, LirNode x) {
    if (n == 0)
      kid0 = x;
    else if (n == 1)
      kid1 = x;
    else
      throw new IllegalArgumentException();
  }

  public boolean equals(Object x) {
    return (x instanceof LirBinOp && super.equals(x)
            && kid0.equals(((LirBinOp)x).kid0)
            && kid1.equals(((LirBinOp)x).kid1));
  }

  public void accept(LirVisitor v) {
    v.visit(this);
  }

}
