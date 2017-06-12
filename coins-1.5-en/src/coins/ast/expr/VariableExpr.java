/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.ast.expr;

import coins.ast.*;

/**
 * Variable.
 */
public class VariableExpr extends ASTree implements LvalueExpr {
    private Declarator decl;

    public VariableExpr(Declarator d) {
  decl = d;
    }

    public boolean isLvalue() {
  return !TypeDecoder.isArrayOrFunction(decl.getType(), 0);
    }

    public boolean hasAddress() { return true; }

    public void accept(Visitor v) {
  v.atVariableExpr(this);
    }

    /**
     * Returns null.
     */
    public ASTree getLeft() { return null; }

    /**
     * Returns null.
     */
    public ASTree getRight() { return null; }

    public void setLeft(ASTree _left) {}

    public void setRight(ASTree _right) {}

    public byte[] getType() { return decl.getType(); }

    /**
     * Returns the declarator of the variable.
     */
    public Declarator getDeclarator() { return decl; }

    public String toString() {
  return decl.getName();
    }
}
