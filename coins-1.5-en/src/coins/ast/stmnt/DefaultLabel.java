/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.ast.stmnt;

import coins.ast.*;

/**
 * The "<code>default</code>" label.
 */
public class DefaultLabel extends LeafStmnt {
    public DefaultLabel(String fname, int line) {
  super(fname, line);
    }

    public void accept(Visitor v) {
  v.atDefaultLabel(this);
    }

    public String toString() {
  return "<default:>";
    }
}
