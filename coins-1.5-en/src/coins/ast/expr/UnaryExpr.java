/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.ast.expr;

import coins.ast.*;

/**
 * Unary expression.
 */
public abstract class UnaryExpr extends OperatorExpr {
    protected Expr expr;

    public UnaryExpr(Expr _expr) {
  expr = _expr;
    }

    public Expr getExpr() {
  return expr;
    }

    /**
     * Returns the operand.
     */
    public ASTree getLeft() { return (ASTree)expr; }

    /**
     * Returns null.
     */
    public ASTree getRight() { return null; }

    public void setLeft(ASTree _left) { expr = (Expr)_left; }

    public void setRight(ASTree _right) {
  throw new RuntimeException("cannot do it");
    }
}
