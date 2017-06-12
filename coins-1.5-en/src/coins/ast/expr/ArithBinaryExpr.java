/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.ast.expr;

import coins.ast.*;

/**
 * Arithmetic binary expression.
 *
 * <p>If an operand must be type-coerced, the operand is explicitly
 * surrounded with a cast expression.  For example, if
 * <code>k</code> is an integer variable, then an expression
 * <code>k + 1.0</code> is transformed into <code>((double)k)+1.0</code>.
 */
public class ArithBinaryExpr extends BinaryExpr implements TokenId {
    private int operatorToken;
    private byte[] resultType;

    public ArithBinaryExpr(Expr expr1, int op, Expr expr2,
         byte[] type) {
  super(expr1, expr2);
  operatorToken = op;
  resultType = type;
    }

    public void accept(Visitor v) {
  v.atArithBinaryExpr(this);
    }

    public byte[] getType() { return resultType; }

    public int operatorId() {
  return operatorToken;
    }

    public String operatorName() {
  switch (operatorToken) {
  case LSHIFT :
      return "<<";
  case RSHIFT :
      return ">>";
  case LE :
      return "<=";
  case GE :
      return ">=";
  case EQ :
      return "==";
  case NEQ :
      return "!=";
  case ANDAND :
      return "&&";
  case OROR :
      return "||";
  default :
      return new StringBuffer().append((char)operatorToken)
             .toString();
  }
    }
}
