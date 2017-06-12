/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.ast.expr;

import coins.ast.*;

/**
 * Expressions including an operator.
 */
public abstract class OperatorExpr extends ASTree implements Expr {

    /**
     * Returns the token identifier of the operator name.
     *
     * See ast.TokenId
     */
    abstract public int operatorId();

    /**
     * Returns the string representation of the operator name.
     */
    abstract public String operatorName();

    protected String getTag() { return operatorName(); }
}
