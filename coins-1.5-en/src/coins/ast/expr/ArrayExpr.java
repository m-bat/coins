/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.ast.expr;

import coins.ast.*;

/**
 * Array access expression.
 */
public class ArrayExpr extends BinaryExpr implements TokenId, LvalueExpr {
    private byte[] encodedType;

    public ArrayExpr(Expr array, Expr index, byte[] type) {
  super(array, index);
  encodedType = type;
    }

    public void accept(Visitor v) {
  v.atArrayExpr(this);
    }

    public byte[] getType() { return encodedType; }

    public boolean isLvalue() {
  return !TypeDecoder.isArrayOrFunction(encodedType, 0);
    }

    public boolean hasAddress() { return true; }

    public Expr getArray() { return getLeftOperand(); }

    public Expr getIndex() { return getRightOperand(); }

    public int operatorId() {
  return INDEX_OP;
    }

    public String operatorName() {
  return "[]";
    }
}
