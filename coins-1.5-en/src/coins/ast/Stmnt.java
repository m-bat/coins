/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.ast;

/**
 * Statement.
 *
 * <p>The declarations of struct/union data types and the declarations
 * of symbols are dealt with independent statements.
 * The declaration of multiple
 * symbols is decomposed into multiple stataments.  For example,
 *
 * <ul><pre>int x, y;</pre></ul>
 *
 * <p>is decomposed into:
 *
 * <ul><pre>int x;    => ast.Declarator object
 * int y;    => ast.Declarator object</pre></ul>
 */
public interface Stmnt {
    /**
     * Returns the file name including the statement.
     */
    String fileName();

    /**
     * Returns the line number of the statement.
     */
    int lineNumber();
}
