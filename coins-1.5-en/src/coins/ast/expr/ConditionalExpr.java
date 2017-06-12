/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.ast.expr;

import coins.ast.*;

/**
 * Conditional expression.
 */
public class ConditionalExpr extends NnaryExpr
  implements TokenId, LvalueExpr
{
    public ConditionalExpr(Expr cond, Expr thenExpr, Expr elseExpr) {
  super(cond, new Pair((ASTree)thenExpr, (ASTree)elseExpr));
    }

    public void accept(Visitor v) {
  v.atConditionalExpr(this);
    }

    public byte[] getType() {
  Pair pair = (Pair)getRight();
  return ((Expr)pair.getLeft()).getType();
    }

    /**
     * Returns the condition part.
     */
    public Expr getCondition() { return getExpr(); }

    /**
     * Returns the then part.
     */
    public Expr getThen() { return (Expr)getRight().getLeft(); }

    /**
     * Returns the else part.
     */
    public Expr getElse() { return (Expr)getRight().getRight(); }

    /**
     * Returns false.
     * ANSI C does not allow a conditional expresion as an L-value.
     */
    public boolean isLvalue() {
  return false;
    }

    public boolean hasAddress() { return true; }

    public int operatorId() {
  return COND_OP;
    }

    public String operatorName() {
  return "?:";
    }
}
