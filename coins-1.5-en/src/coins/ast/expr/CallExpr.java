/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.ast.expr;

import coins.ast.*;

/**
 * Function call expression.
 */
public class CallExpr extends NnaryExpr implements TokenId {
    private byte[] funcType;
    private byte[] returnType;

    public CallExpr(Expr func, ASTList args,
        byte[] ftype, byte[] rtype)
    {
  super(func, args);
  funcType = ftype;
  returnType = rtype;
    }

    public void accept(Visitor v) {
  v.atCallExpr(this);
    }

    /**
     * Returns the type of the value of the function result.
     */
    public byte[] getType() { return returnType; }

    /**
     * Returns the function prototype.  The encoding method
     * is equivalent to the method used for encoding a type.
     */
    public byte[] getFunctionType() { return funcType; }

    /**
     * Returns the function name.  If the function is invoked through
     * a function pointer, then this method returns the expression
     * including that pointer.  For example, if the function call
     * expression is:
     *
     * <ul><pre>(*fptr)(i, j)</pre></ul>
     *
     * <p>then this method returns an <code>Expr</code> object
     * representing the expression "<code>*fptr</code>".
     */
    public Expr getFunction() { return getExpr(); }

    /**
     * Returns the actual parameters.
     *
     * @return		A linked list of <code>Expr</code> objects.
     */
    public ASTList getArguments() { return (ASTList)getRight(); }

    public int operatorId() {
  return FUNCALL;
    }

    public String operatorName() {
  return "<call>";
    }
}
