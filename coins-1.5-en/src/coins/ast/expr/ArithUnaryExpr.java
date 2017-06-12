/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.ast.expr;

import coins.ast.*;

/**
 * Arithmetic unary expression.
 */
public class ArithUnaryExpr extends UnaryExpr implements TokenId {
    private int operatorToken;

    public ArithUnaryExpr(Expr expr, int op) {
  super(expr);
  operatorToken = op;
    }

    public void accept(Visitor v) {
  v.atArithUnaryExpr(this);
    }

    public byte[] getType() { return getExpr().getType(); }

    public int operatorId() {
  return operatorToken;
    }

    public String operatorName() {
  return new StringBuffer().append((char)operatorToken).toString();
    }
}
