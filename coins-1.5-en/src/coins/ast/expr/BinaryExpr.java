/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.ast.expr;

import coins.ast.*;

/**
 * Binary expression.
 */
public abstract class BinaryExpr extends OperatorExpr {
    protected Expr left, right;

    public BinaryExpr(Expr _left, Expr _right) {
  left = _left;
  right = _right;
    }

    /**
     * Returns the left operand.
     */
    public ASTree getLeft() { return (ASTree)left; }

    /**
     * Returns the right operand.
     */
    public ASTree getRight() { return (ASTree)right; }

    public Expr getLeftOperand() { return left; }

    public Expr getRightOperand() { return right; }

    public void setLeft(ASTree _left) { left = (Expr)_left; }

    public void setRight(ASTree _right) { right = (Expr)_right; }
}
