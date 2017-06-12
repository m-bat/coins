/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.ast.stmnt;

import coins.ast.*;

/**
 * While statement.
 */
public class WhileStmnt extends TreeStmnt {
    public WhileStmnt(String fname, int line) { super(fname, line); }

    public WhileStmnt set(Expr cond, CompoundStmnt body) {
  setLeft((ASTree)cond);
  setRight((ASTree)body.simplify());
  return this;
    }

    public void accept(Visitor v) {
  v.atWhileStmnt(this);
    }

    /**
     * Returns the loop condition.
     */
    public Expr getExpr() { return (Expr)getLeft(); }

    /**
     * Returns the loop body.
     */
    public Stmnt getBody() { return (Stmnt)getRight(); }

    protected String getTag() { return "<while>"; }
}
