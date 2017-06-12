/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.ast.expr;

import coins.ast.*;

/**
 * <code>'&'</code> Expression for computing the address of the operand.
 */
public class AddressExpr extends UnaryExpr {
    private byte[] type;

    public AddressExpr(Expr expr, byte[] _type) {
  super(expr);
  type = _type;
    }

    public void accept(Visitor v) {
  v.atAddressExpr(this);
    }

    public byte[] getType() { return type; }

    public int operatorId() {
  return '&';
    }

    public String operatorName() {
  return "&";
    }

    protected String getTag() { return "&@"; }
}
