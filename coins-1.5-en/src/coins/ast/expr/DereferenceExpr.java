/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.ast.expr;

import coins.ast.*;

/**
 * Pointer dereference (<code>'*'</code>) expression.
 */
public class DereferenceExpr extends UnaryExpr implements LvalueExpr {
    private byte[] type;

    public DereferenceExpr(Expr expr, byte[] _type) {
  super(expr);
  type = _type;
    }

    public void accept(Visitor v) {
  v.atDereferenceExpr(this);
    }

    /**
     * Returns the type of the value pointed by the operand expression.
     */
    public byte[] getType() { return type; }

    public boolean isLvalue() {
  return !TypeDecoder.isArrayOrFunction(type, 0);
    }

    public boolean hasAddress() { return true; }

    public int operatorId() {
  return '*';
    }

    public String operatorName() {
  return "*";
    }

    protected String getTag() { return "*@"; }
}
