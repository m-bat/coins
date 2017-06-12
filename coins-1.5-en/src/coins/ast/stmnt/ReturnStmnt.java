/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.ast.stmnt;

import coins.ast.*;

/**
 * Return statement.
 */
public class ReturnStmnt extends TreeStmnt {
    public ReturnStmnt(Expr expr, String fname, int line) {
  super((ASTree)expr, null, fname, line);
    }

    public void accept(Visitor v) {
  v.atReturnStmnt(this);
    }

    /**
     * Returns the expression computing the returned value.
     */
    public Expr getExpr() { return (Expr)getLeft(); }

    protected String getTag() { return "<return>"; }
}
