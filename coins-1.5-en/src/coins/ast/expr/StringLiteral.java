/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.ast.expr;

import coins.ast.*;

/**
 * Constant character string.
 */
public class StringLiteral extends ASTree implements Expr, LvalueExpr, TypeId {
    private static final byte[] type
  = new byte[] { (byte)POINTER_T, (byte)CHAR_T };

    protected String string;

    public StringLiteral(String s) {
  string = s;
    }

    public boolean isLvalue() { return false; }

    public boolean hasAddress() { return true; }

    public String get() { return string; }

    public void accept(Visitor v) {
  v.atStringLiteral(this);
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

    /**
     * Returns the <code>char*</code> type.
     */
    public byte[] getType() { return type; }

    public String toString() {
  return "\"" + string + "\"";
    }
}
