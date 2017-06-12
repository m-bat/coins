/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.ast;

/**
 * Expression.
 */
public interface Expr {
    /**
     * Returns the type of the resulting value of evaluating
     * the expression.
     * See ast.TypeId
     *
     * @return	the encoded type.
     */
    byte[] getType();
}
