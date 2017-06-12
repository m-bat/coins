/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.ast.stmnt;

import coins.ast.*;

/**
 * Case label.
 */
public class CaseLabel extends LeafStmnt {
    private long value; //SF041026

    public CaseLabel(long v, String fname, int line) { //SF041026
  super(fname, line);
  value = v;
    }

    public void accept(Visitor v) {
  v.atCaseLabel(this);
    }

    /**
     * Returns the constant value of the case label.
     */
    public long getConstant() { return value; } //SF041026

    public String toString() {
  return "<case " + value + ":>";
    }
}
