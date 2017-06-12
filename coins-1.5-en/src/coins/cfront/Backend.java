/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.cfront;

import coins.ast.ASTList;

/**
 * Backend compiler.
 *
 * See cfront.Parser#runParser(String)
 */
public interface Backend {
    /**
     * Compiles a parse tree.
     *
     * <p>If a parser reads a top-level declaration, then it calls this
     * method with a parse tree representing that declaration.
     * If this method throws <code>StopException</code>, the compilation
     * is terminated.
     */
    void compile(ASTList tree) throws StopException;

    /**
     * Finishes compilation.
     *
     * <p>A parser calls this method after reading the whole source
     * program.
     */
    void doEpilogue();
}
