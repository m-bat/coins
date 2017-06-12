/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.ast.expr;

import coins.ast.*;

/**
 * Expression that may be an l-value.
 *
 * <p>Suppose that an expression is <code>VariableExpr</code>.
 * If the expression represents an <code>int</code> variable,
 * it is an l-value.  However, if it represents an array name,
 * then it is not an l-value.  For example,
 *
 * <ul><pre>int a[10];
 * a = ... ;       // a is not an l-value.
 * a[0] = ... ;    // a[0] is an l-value.
 * </pre></ul>
 *
 * <p>Even if the class of an expression implements
 * <code>LvalueExpr</code>, that expression may not be an l-value.
 * <code>isLvalue()</code> returns true only if that expression
 * is really an l-value.
 */
public interface LvalueExpr extends Expr {
    /**
     * Returns true if the expression is really an l-value.
     */
    boolean isLvalue();

    /**
     * Returns true if the expression can be an operand of '&'.
     */
    boolean hasAddress();
}
