/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.ast;

import java.io.Serializable;

/**
 * Abstract Syntax Tree.  An ASTree object represents a node of
 * a binary tree.  If the node is a leaf node, both <code>getLeft()</code>
 * and <code>getRight()</code> returns null.
 */
public abstract class ASTree implements Serializable {
    public abstract ASTree getLeft();

    public abstract ASTree getRight();

    public abstract void setLeft(ASTree _left);

    public abstract void setRight(ASTree _right);

    /**
     * Is a method for the visitor pattern.  It calls
     * <code>atXXX()</code> on the given visitor, where
     * <code>XXX</code> is the class name of the node object.
     */
    public abstract void accept(Visitor v);

    public String toString() {
  StringBuffer sbuf = new StringBuffer();
  sbuf.append('(');
  sbuf.append(getTag());
  toString1(sbuf, getLeft());
  rightToString(sbuf, getRight());
  sbuf.append(')');
  return sbuf.toString();
    }

    /**
     * Returns the type of this node.  This method is used by
     * <code>toString()</code>.
     */
    protected String getTag() { return "<?>"; }

    protected void putSeparator(StringBuffer sbuf) {}

    protected final void toString1(StringBuffer sbuf, ASTree node) {
  sbuf.append(' ');
  if (node == null)
      sbuf.append("<null>");
  else
      sbuf.append(node.toString());
    }

    protected void rightToString(StringBuffer sbuf, ASTree right) {
  if (right != null) {
      putSeparator(sbuf);
      toString1(sbuf, right);
  }
    }
}
