/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.ast.expr;

import coins.ast.*;

/**
 * Comma expression.
 */
public class CommaExpr extends BinaryExpr implements TokenId, LvalueExpr {
    public CommaExpr(Expr expr1, Expr expr2) {
  super(expr1, expr2);
    }

    public void accept(Visitor v) {
  v.atCommaExpr(this);
    }

    public byte[] getType() { return ((Expr)getRight()).getType(); }

    public int operatorId() {
  return ',';
    }

    public String operatorName() {
  return ",";
    }

    public boolean isLvalue() {
  Object right = getRight();
  if (right instanceof LvalueExpr)
      return ((LvalueExpr)right).isLvalue();

  return false;
    }

    public boolean hasAddress() { return true; }
}
