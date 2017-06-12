/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */

package coins.ast.expr;

import coins.ast.*;

/**
 * An array of initial values.
 *
 * <p>For example, the initializing expression in the following code:
 *
 * <ul><pre>int a[] = { 1, 2, 3, 4 };</pre></ul>
 *
 * <p>is represented as a linked-list of <code>ConstantExpr</code>
 * objects representing 1, 2, 3, and 4.
 *
 * <p>A structure also takes an array of initial values:
 *
 * <ul><pre>struct price list[] = { { "book", 500 }, { "coffee", 200 } };
 *
 * <p>is represented as a linked-list of two <code>ArrayInitializer</code>
 * objects.  Each of them contains a <code>StringLiteral</code> and a
 * <code>ConstantExpr</code>.
 */
public class ArrayInitializer extends ASTList implements Expr {
    private byte[] type;

    public ArrayInitializer(Expr expr, byte[] _type) {
  super((ASTree)expr);
  type = _type;
    }

    public void accept(Visitor v) {
  v.atArrayInitializer(this);
    }

    public byte[] getType() {
  return type;
    }

    public static ArrayInitializer append(ArrayInitializer init, Expr expr,
             byte[] type) {
  return (ArrayInitializer)ASTList.concat(init,
          new ArrayInitializer(expr, type));
    }
}
