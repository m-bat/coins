/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.ast.stmnt;

import coins.ast.*;

/**
 * Goto statement.
 */
public class GotoStmnt extends LeafStmnt {
    private String label;

    public GotoStmnt(String fname, int line) {
  super(fname, line);
    }

    public void accept(Visitor v) {
  v.atGotoStmnt(this);
    }

    public void setLabel(String name) { label = name; }

    /**
     * Returns the destination label.
     */
    public String getLabel() { return label; }

    public String toString() {
  return "<goto " + label + ">";
    }
}

