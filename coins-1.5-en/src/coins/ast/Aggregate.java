/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.ast;

/**
 * Declaration of a struct/union type.
 * If the declaration appears in a type name, the type name is decomposed
 * into the declaration and a reference to the declared structure/union.
 *
 * <p>For example, a variable declaration:
 *
 * <ul><pre>struct Point {
 *     int x, y;
 *     struct Color { int color; } c;
 * } p;</pre></ul>
 *
 * <p>is decomposed into three declarations.
 *
 * <ul><pre>struct Color { int color; };    => ast.Struct object
 * struct Point {                           => ast.Struct object
 *     int x, y;
 *     struct Color c;
 * };
 * struct Point p;                          => ast.Declarator object
 * </pre></ul>
 *
 * <p>The <code>struct</code> declaration never appears as part of
 * a type name.
 */
public abstract class Aggregate extends ASTree implements Stmnt {
    private String fileName;
    private int lineNumber;

    protected String name;
    protected DeclaratorList members;
    //##22 protected int size;
    protected long size;  //##22

    /**
     * This variable used for computing word alignment.
     */
    public static int WORD_SIZE = 4;

    public Aggregate(String aname, DeclaratorList mem,
         String fname, int line)
    {
  fileName = fname;
  lineNumber = line;
  name = aname;
  members = mem;
  size = 0;
    }

    //##22 protected void setSize(int s) { size = s; }
    protected void setSize(long s) { size = s; }  //##22

    public String fileName() { return fileName; }

    public int lineNumber() { return lineNumber; }

    /**
     * Returns the tag name.  If the tag name is not explicitly specified,
     * an arbitrary chosen unique name is given by the parser.
     */
    public String name() { return name; }

    /**
     * Returns members.
     */
    public ASTree getLeft() { return members; }

    /**
     * Returns null.
     */
    public ASTree getRight() { return null; }

    public void setLeft(ASTree _left) {
  members = (DeclaratorList)_left;
    }

    public void setRight(ASTree _right) {
  throw new RuntimeException("cannot do it");
    }

    /**
     * Returns the size (in byte) of the struct/union data type.
     */
    //##22 public int getSize() {
      public long getSize() {  //##22
  return size;
    }

    /**
     * Returns the members of the struct/union data type.
     */
    public DeclaratorList getMembers() {
  return members;
    }

    /**
     * Returns the member of the given name.
     */
    public Declarator getMember(String name) {
  DeclaratorList list = members;
  while (list != null) {
      Declarator d = list.get();
      if (d.getName() == name)	// this is not a bug!
    return d;

      list = list.next();
  }

  return null;
    }
}
