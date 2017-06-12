/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.ast.stmnt;

import coins.ast.*;

/**
 * The super class of statements that contain nested statements.
 */
public abstract class TreeStmnt extends ASTree implements Stmnt {
    private String fileName;
    private int lineNumber;
    protected ASTree left, right;

    protected TreeStmnt(String fname, int line) {
  this(null, null, fname, line);
    }

    protected TreeStmnt(ASTree _left, ASTree _right, String fname, int line) {
  left = _left;
  right = _right;
  fileName = fname;
  lineNumber = line;
    }

    public String fileName() { return /*null*/fileName; } //S.Fukuda 2002.10.30

    public int lineNumber() { return /*0*/lineNumber; } //S.Fukuda 2002.10.30

    protected String getTag() { return "<stmnt>"; }

    public ASTree getLeft() { return left; }

    public ASTree getRight() { return right; }

    public void setLeft(ASTree _left) { left = _left; }

    public void setRight(ASTree _right) { right = _right; }
}
