/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.ast.stmnt;

import coins.ast.*;

/**
 * Switch statement.
 */
public class SwitchStmnt extends TreeStmnt {
    public SwitchStmnt(String fname, int line) { super(fname, line); }

    public SwitchStmnt set(Expr cond, CompoundStmnt body) {
  setLeft((ASTree)cond);
  setRight((ASTree)body.simplify());
  return this;
    }

    public void accept(Visitor v) {
  v.atSwitchStmnt(this);
    }

    /**
     * Returns the expression computing the tested value.
     */
    public Expr getExpr() { return (Expr)getLeft(); }

    /**
     * Returns the switch body.
     */
    public Stmnt getBody() { return (Stmnt)getRight(); }

    protected String getTag() { return "<switch>"; }
}
