/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.ast;

/**
 * A node of a a binary tree.  This class provides concrete methods
 * overriding abstract methods in ASTree.
 */
public class Pair extends ASTree {
    protected ASTree left, right;

    public Pair(ASTree _left, ASTree _right) {
  left = _left;
  right = _right;
    }

    public void accept(Visitor v) {
  v.atPair(this);
    }

    protected String getTag() { return "<pair>"; }

    public ASTree getLeft() { return left; }

    public ASTree getRight() { return right; }

    public void setLeft(ASTree _left) { left = _left; }

    public void setRight(ASTree _right) { right = _right; }
}
