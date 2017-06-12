/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.ast;

import coins.ast.expr.ConstantExpr;

/**
 * Enum declaration.
 */
public class Enum extends ASTree implements Stmnt {
    private String typeName;
    private Item itemList, itemTail;
    private String fileName;
    private int lineNumber;

    public static class Item {
  Item next;
  String name;
  ConstantExpr value;

  Item(String n, ConstantExpr v) {
      next = null;
      name = n;
      value = v;
  }

  public Item getNext() { return next; }

  /**
   * Returns the name of the enum constant.
   */
  public String getName() { return name; }

  /**
   * Returns the value of the enum constant.
   */
  public ConstantExpr getValue() { return value; }
    }

    public Enum(String name, String fname, int line) {
  typeName = name;
  itemList = null;
  itemTail = null;
  fileName = fname;
  lineNumber = line;
    }

    /**
     * Returns the tag name of the enum type.
     * If the tag name is not explicitly specified,
     * an arbitrary chosen unique name is given by the parser.
     */
    public String name() { return typeName; }

    /**
     * Returns a list of enum constants.
     */
    public Item getItems() {
  return itemList;
    }

    public void add(String name, ConstantExpr value) {
  Item i = new Item(name, value);
  if (itemTail == null)
      itemList = itemTail = i;
  else {
      itemTail.next = i;
      itemTail = i;
  }
    }

    public void accept(Visitor v) {
  v.atEnum(this);
    }

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

    public String fileName() { return fileName; } //SF041126

    public int lineNumber() { return lineNumber; } //SF041126

    public String toString() {
  StringBuffer sbuf = new StringBuffer();
  sbuf.append("<enum ");
  sbuf.append(typeName);
  sbuf.append(' ');
  for (Item list = itemList; list != null; list = list.next) {
      sbuf.append(list.name);
      if (list.value != null) {
    sbuf.append('=');
    sbuf.append(list.value.toString());
      }

      sbuf.append(", ");
  }

  sbuf.append('>');
  return sbuf.toString();
    }
}
