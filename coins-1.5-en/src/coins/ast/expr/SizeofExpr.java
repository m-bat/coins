/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
//##17 BEGIN
package coins.ast.expr;

import coins.ast.*;

/**
 * Sizeof expression.
 */
public class SizeofExpr extends UnaryExpr implements TokenId {
    private byte[] srcType;

    public SizeofExpr( byte[] stype ) {
  super(null);
  srcType = stype;
    }

    public SizeofExpr(Expr expr ) {
  super(expr);
  srcType = null;
    }

    public void accept(Visitor v) {
  v.atSizeofExpr(this);
    }

    /**
     * Returns the given type or operand type.
     */
    public byte[] getType() { return srcType; }

    public int operatorId() {
  return SIZEOF;
    }

    public String operatorName() {
  return "<sizeof>";
    }
}
//##17 END

