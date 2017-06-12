/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */

package coins.ast.stmnt;

import coins.ast.*;

/**
 * Empty statement.
 */
public class NullStmnt extends LeafStmnt {
    public NullStmnt(String fname, int line) {
  super(fname, line);
    }

    public void accept(Visitor v) {
  v.atNullStmnt(this);
    }

    public String toString() {
  return "<;>";
    }
}
