/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.ast.stmnt;

import coins.ast.*;

/**
 * For statement.
 *
 * <p>The for statement is decomposed into four parts:
 *
 * <ul><pre>for ( <i>initializer</i> ; <i>conditin</i> ; <i>iterator</i> )
 *     <i>statement</i></pre></ul>
 */
public class ForStmnt extends TreeStmnt {
    public ForStmnt(String fname, int line) { super(fname, line); }

    public ForStmnt set(Expr init, Expr cond, Expr iterate,
      CompoundStmnt body)
    {
  setLeft(new Pair((ASTree)init, (ASTree)cond));
  setRight(new Pair((ASTree)iterate, (ASTree)body.simplify()));
  //##79 setRight(new Pair((ASTree)iterate, body)); //##79
  // Tentatively changed as above for GlobalReform but restored. //##79
  return this;
    }

    public void accept(Visitor v) {
  v.atForStmnt(this);
    }

    /**
     * Returns the initializer expression.
     */
    public Expr getInitializer() {
  Pair p = (Pair)getLeft();
  return (Expr)p.getLeft();
    }

    /**
     * Returns the condition expression.
     */
    public Expr getCondition() {
  Pair p = (Pair)getLeft();
  return (Expr)p.getRight();
    }

    /**
     * Returns the iteration expression.
     */
    public Expr getIteration() {
  Pair p = (Pair)getRight();
  return (Expr)p.getLeft();
    }

    /**
     * Returns the loop body.
     */
    public Stmnt getBody() {
  Pair p = (Pair)getRight();
  return (Stmnt)p.getRight();
    }

    protected String getTag() { return "<for>"; }
}
