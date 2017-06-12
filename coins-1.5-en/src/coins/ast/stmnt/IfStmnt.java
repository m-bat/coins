/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */

package coins.ast.stmnt;

import coins.ast.*;

/**
 * If statement.
 */
public class IfStmnt extends TreeStmnt {
    public IfStmnt(String fname, int line) { super(fname, line); }

    public void accept(Visitor v) {
  v.atIfStmnt(this);
    }

    public IfStmnt set(Expr cond, CompoundStmnt thenp,
           CompoundStmnt elsep)
    {
  Stmnt elsep2;
  setLeft((ASTree)cond);
  if (elsep == null)
      elsep2 = null;
  else
      elsep2 = elsep.simplify();

  setRight(new Pair((ASTree)thenp.simplify(), (ASTree)elsep2));
  return this;
    }

    /**
     * Returns the condition expression.
     */
    public Expr getExpr() { return (Expr)getLeft(); }

    /**
     * Returns the then statement.
     */
    public Stmnt getThen() {
  Pair p = (Pair)getRight();
  return (Stmnt)p.getLeft();
    }

    /**
     * Returns the else statement.
     */
    public Stmnt getElse() {
  Pair p = (Pair)getRight();
  return (Stmnt)p.getRight();
    }

    protected String getTag() { return "<if>"; }
}
