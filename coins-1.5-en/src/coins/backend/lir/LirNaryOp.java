/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.lir;

import coins.backend.Op;
import coins.backend.sym.Label;
import coins.backend.util.ImList;


/** Represents N-ary operators */
public class LirNaryOp extends LirNode {
  LirNode[] kid;

  public LirNaryOp(int id, int op, int t, LirNode operands[], ImList opt) {
    super(id, op, t, opt);
    kid = operands;
  }

  public LirNode makeCopy(LirFactory fac) {
    LirNode[] newkid = new LirNode[kid.length];
    for (int i = 0; i < kid.length; i++)
      newkid[i] = kid[i].makeCopy(fac);
    return fac.operator(opCode, type, newkid, opt);
  }

  public LirNode makeShallowCopy(LirFactory fac) {
    LirNode[] newkid = new LirNode[kid.length];
    for (int i = 0; i < kid.length; i++)
      newkid[i] = kid[i];
    return fac.operator(opCode, type, newkid, opt);
  }

  public LirNode replaceOptions(LirFactory fac, ImList newOpt) {
    LirNode[] newkid = new LirNode[kid.length];
    for (int i = 0; i < kid.length; i++)
      newkid[i] = kid[i];
    return fac.operator(opCode, type, newkid, newOpt);
  }

  public int nKids() { return kid.length; }

  public LirNode kid(int n) { return kid[n]; }

  public void setKid(int n, LirNode x) { kid[n] = x; }

  public boolean equals(Object x) {
    if (!(x instanceof LirNaryOp) || !super.equals(x)
        || kid.length != ((LirNaryOp)x).kid.length)
      return false;
    for (int i = 0; i < kid.length; i++)
      if (!kid[i].equals(((LirNaryOp)x).kid[i]))
        return false;
    return true;
  }

  public void accept(LirVisitor v) { v.visit(this); }

}

