/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.ast.stmnt;

import coins.ast.*;

/**
 * Expression statement.
 */
public class ExpressionStmnt extends TreeStmnt {
    public ExpressionStmnt(String fname, int line) { super(fname, line); }

    public ExpressionStmnt setExpr(Expr expr) {
  setLeft((ASTree)expr);
  return this;
    }

    public void accept(Visitor v) {
  v.atExpressionStmnt(this);
    }

    /**
     * Returns the expression.
     */
    public Expr getExpr() { return (Expr)getLeft(); }

    protected String getTag() { return "<expr>"; }
}
