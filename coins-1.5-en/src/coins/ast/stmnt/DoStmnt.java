/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.ast.stmnt;

import coins.ast.*;

/**
 * Do-while statement.
 */
public class DoStmnt extends TreeStmnt {
    public DoStmnt(String fname, int line) { super(fname, line); }

    public void accept(Visitor v) {
  v.atDoStmnt(this);
    }

    public DoStmnt set(CompoundStmnt body, Expr cond) {
  setLeft((ASTree)body.simplify());
  setRight((ASTree)cond);
  return this;
    }

    /**
     * Returns the loop body.
     */
    public Stmnt getBody() { return (Stmnt)getLeft(); }

    /**
     * Returns the condition expression.
     */
    public Expr getExpr() { return (Expr)getRight(); }

    protected String getTag() { return "<do>"; }
}
