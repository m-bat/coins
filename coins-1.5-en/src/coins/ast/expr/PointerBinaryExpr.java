/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.ast.expr;

import coins.ast.*;

/**
 * Pointer binary expression.
 *
 * <p>The left operand is a pointer value.  The right operand is an
 * integer offset.  The parser maintains this order.  If the right
 * operand is a pointer, then the parser implicitly exchanges the operands.
 */
public class PointerBinaryExpr extends BinaryExpr implements TokenId {
    private int operatorToken;
    private byte[] resultType;

    /**
     * Constructs a pointer binary expression.
     *
     * @param expr1	a pointer expression
     * @param op	+ or -
     * @param expr2	an integer expression
     */
    public PointerBinaryExpr(Expr expr1, int op, Expr expr2) {
  super(expr1, expr2);
  operatorToken = op;
  resultType = expr1.getType();
    }

    public void accept(Visitor v) {
  v.atPointerBinaryExpr(this);
    }

    /**
     * Returns the type of the resulting value of the binary expression.
     */
    public byte[] getType() { return resultType; }

    /**
     * Returns the left operand.
     */
    public Expr getPointer() { return (Expr)getLeft(); }

    /**
     * Returns the right operand.
     */
    public Expr getOffset() { return (Expr)getRight(); }

    public int operatorId() {
  return operatorToken;
    }

    public String operatorName() {
  return new StringBuffer().append((char)operatorToken).toString();
    }
}
