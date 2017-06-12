/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.ast.stmnt;

import coins.ast.*;

/**
 * Label.
 */
public class NamedLabel extends LeafStmnt {
    private String name;

    public NamedLabel(String label, String fname, int line) {
  super(fname, line);
  name = label;
    }

    public void accept(Visitor v) {
  v.atNamedLabel(this);
    }

    /**
     * Returns the label name.
     */
    public String getName() {
  return name;
    }

    public String toString() {
  return "<" + name + ":>";
    }
}
