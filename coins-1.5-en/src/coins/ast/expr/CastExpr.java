/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.ast.expr;

import coins.ast.*;

/**
 * Cast expression.
 */
public class CastExpr extends UnaryExpr implements TokenId {
    private byte[] srcType;
    private byte[] destType;

    public CastExpr(Expr expr, byte[] stype, byte[] dtype) {
  super(expr);
  srcType = stype;
  destType = dtype;
    }

    public void accept(Visitor v) {
  v.atCastExpr(this);
    }

    /**
     * Returns the resulting type.  The operand type is converted into
     * the resulting type.
     */
    public byte[] getType() { return destType; }

    /**
     * Returns the operand type.
     */
    public byte[] getOperandType() { return srcType; }

    public int operatorId() {
  return CAST_OP;
    }

    public String operatorName() {
  StringBuffer sbuf = new StringBuffer();
  sbuf.append("<cast ");
  for (int i = 0; i < srcType.length; ++i)
      sbuf.append((char)srcType[i]);

  sbuf.append("->");
  for (int i = 0; i < destType.length; ++i)
      sbuf.append((char)destType[i]);

  sbuf.append('>');
  return sbuf.toString();
    }
}
