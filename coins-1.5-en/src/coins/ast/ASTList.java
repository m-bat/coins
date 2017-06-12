/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.ast;

/**
 * A linked list.
 * The right subtree must be an ASTList object or null.
 */
public class ASTList
  extends ASTree
{
  private ASTree left;
  private ASTList right;
  private ASTList rightMost = null; // right-most ASTList. //##64

  public ASTList(ASTree _head, ASTList _tail)
  {
    left = _head;
    right = _tail;
    rightMost = _tail; //##64
  }

  public ASTList(ASTree _head)
  {
    left = _head;
    right = null;
    rightMost = null; //##64
  }

  public ASTree getLeft()
  {
    return left;
  }

  public ASTree getRight()
  {
    return right;
  }

  public void setLeft(ASTree _left)
  {
    left = _left;
  }

  public void setRight(ASTree _right)
  {
    right = (ASTList)_right;
  }

  /**
   * Returns the car part of the list.
   */
  public ASTree head()
  {
    return left;
  }

  public void setHead(ASTree _head)
  {
    left = _head;
  }

  /**
   * Returns the cdr part of the list.
   */
  public ASTList tail()
  {
    return right;
  }

  public void setTail(ASTList _tail)
  {
    right = _tail;
    rightMost = _tail; //##64
  }

  public void accept(Visitor v)
  {
    v.atASTList(this);
  }

  protected String getTag()
  {
    return "<list>";
  }

  protected void rightToString(StringBuffer sbuf, ASTree _right)
  {
    for (; ; ) {
      if (_right == null) {
        break;
      }
      else {
        if (_right instanceof ASTList) {
          putSeparator(sbuf);
          ASTList list = (ASTList)_right;
          toString1(sbuf, list.getLeft());
          _right = list.getRight();
        }
        else {
          sbuf.append(" . ");
          sbuf.append(_right.toString());
          break;
        }
      }
    }
  }

  public boolean subst(ASTree newObj, ASTree oldObj)
  {
    for (ASTList list = this; list != null; list = list.tail()) {
      if (list.head() == oldObj) {
        list.setHead(newObj);
        return true;
      }
    }

    return false;
  }

  /**
   * Appends an object to a list.
   */
  public static ASTList append(ASTList a, ASTree b)
  {
    return concat(a, new ASTList(b));
  }

  /**
   * Concatenates two lists.
   */
  public static ASTList concat(ASTList a, ASTList b)
  {
    if (a == null) {
      return b;
    }
    else {
      //##64 BEGIN
      if (a.rightMost != null) {
        ASTList list0 = a.rightMost;
        while (list0.right != null) {
          list0 = list0.right;
        }
        list0.right = b;
        a.rightMost = b;
        return a;
      }
      //##64 END
      ASTList list = a;
      while (list.right != null) {
        list = list.right;

      }
      list.right = b;
      a.rightMost = b; //##64
      return a;
    }
  }
}
