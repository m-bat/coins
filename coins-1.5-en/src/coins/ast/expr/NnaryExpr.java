/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.ast.expr;

import coins.ast.*;

/**
 * Multiple-operands expression.
 */
public abstract class NnaryExpr extends OperatorExpr {
    private Expr operatorExpr;
    private ASTree operands;

    /**
     * Constructs an <code>NnaryExpr</code> object.
     *
     * @param _op		the operator expression.
     * @param _operands		the operands.
     */
    public NnaryExpr(Expr _op, ASTree _operands) {
  operatorExpr = _op;
  operands = _operands;
    }

    protected Expr getExpr() { return operatorExpr; }

    /**
     * Returns the operator expression.
     */
    public ASTree getLeft() { return (ASTree)operatorExpr; }

    /**
     * Returns the operands.
     */
    public ASTree getRight() { return operands; }

    public void setLeft(ASTree _left) { operatorExpr = (Expr)_left; }

    public void setRight(ASTree _right) { operands = _right; }
}
