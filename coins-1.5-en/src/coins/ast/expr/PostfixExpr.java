/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.ast.expr;

import coins.ast.*;

/**
 * Postfix "<code>++</code>" or "<code>--</code>" expression.
 */
public class PostfixExpr extends UnaryExpr implements TokenId {
    private boolean increment;

    /**
     * Constructs a <code>PoistfixExpr</code> object.
     *
     * @param plusplus		true if the operator <code>++</code>.
     */
    public PostfixExpr(LvalueExpr expr, boolean plusplus) {
  super(expr);
  increment = plusplus;
    }

    public void accept(Visitor v) {
  v.atPostfixExpr(this);
    }

    public byte[] getType() { return getExpr().getType(); }

    /**
     * Returns <code>TokenId.PLUSPLUS</code>
     * or <code>TokenId.MINUSMINUS</code>.
     */
    public int operatorId() {
  if (increment)
      return PLUSPLUS;
  else
      return MINUSMINUS;
    }

    public String operatorName() {
  if (increment)
      return "++";
  else
      return "--";
    }

    protected String getTag() {
  if (increment)
      return "@++";
  else
      return "@--";
    }
}
