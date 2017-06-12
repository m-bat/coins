/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.ast.stmnt;

import coins.ast.*;

/**
 * The super class of statements that do not contain nested statements.
 */
abstract class LeafStmnt extends ASTree implements Stmnt {
    private String fileName;
    private int lineNumber;

    protected LeafStmnt(String fname, int line) {
  fileName = fname;
  lineNumber = line;
    }

    public String fileName() { return fileName; }

    public int lineNumber() { return lineNumber; }

    /**
     * Returns null.
     */
    public ASTree getLeft() { return null; }

    /**
     * Returns null.
     */
    public ASTree getRight() { return null; }

    public void setLeft(ASTree _left) {}

    public void setRight(ASTree _right) {}
}
