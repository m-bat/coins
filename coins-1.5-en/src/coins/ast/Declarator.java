/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.ast;

/**
 * Declaration of a single symbol.
 */
public class Declarator extends ASTree implements TypeId, Stmnt {
    private String fileName;
    private int lineNumber;

    private String dname;
    private byte[] type;
    //##22 private int dSize;
    private long dSize;  //##22
    private int storage;
    private DeclaratorList argList;
    private Expr initialzer;
    private int bitFieldSize;
    private boolean isBitField; // true if bit field. //##16
        // There may be a bit field with size 0 (JIS C 6.5.2.1) //##16
    private boolean typedefed;
    ////////////////////SF030531[
    //##22 private int arrayParamSize = 0; // array parameter size
    private long arrayParamSize = 0; // array parameter size //##22
  // ex. func(int p[2][4],int q[][3],int *r)
  // int p[2][4] => int (*q)[4] (arrayParamSize==2)
  // int q[][3]  => int (*p)[3] (arrayParamSize==-1)
  // int *r      => not changed (arrayParamSize==0)
    ////////////////////SF030531]

    /**
     * Constructs a declarator.
     *
     * @param name		the name of the declared variable.
     * @param fname		the file name including the declaration.
     * @param line		the line number.
     */
    public Declarator(String name, String fname, int line) {
  fileName = fname;
  lineNumber = line;
  dname = name;
  type = null;
  dSize = -1;
  storage = 0;
  argList = null;
  initialzer = null;
  bitFieldSize = 0;	// this declarator is not a bit-field.
  isBitField = false; //##16
  typedefed = false;
  arrayParamSize = 0; //SF030531
    }

    /**
     * Returns an initializer expression or null.
     */
    public ASTree getLeft() { return (ASTree)initialzer; }

    /**
     * Returns null.
     */
    public ASTree getRight() { return null; }

    public void setLeft(ASTree _left) {
  initialzer = (Expr)initialzer;
    }

    public void setRight(ASTree _right) {
  throw new RuntimeException("cannot do it");
    }

    public void accept(Visitor v) {
  v.atDeclarator(this);
    }

    public String fileName() { return fileName; }

    public int lineNumber() { return lineNumber; }

    public void setTypedefed(boolean b) {
  typedefed = b;
    }

    public void setName(String name) {
  dname = name;
    }

    //##22 public void setType(byte[] t, int s) {
public void setType(byte[] t, long s) {  //##22
  type = t;
  dSize = s;	// -1 if error
    }

    public void setStorage(int s) {
  storage = s;
    }

    public void setArgs(DeclaratorList args) {
  argList = args;
    }

    public void setInitializer(Expr i) {
  initialzer = i;
    }

    public void setBitFieldSize(int s) {
  bitFieldSize = s;
    }

    //##16 BEGIN
    public void setAsBitField() {
        isBitField = true;
    }

    public boolean
    isItBitField() {
      return isBitField;
    }
    //##16 END

    /**
     * Returns true if this declarator is part of a typedef declaration.
     */
    public boolean isTypedef() { return typedefed; }

    /**
     * Returns the encoded type of the declared symbol.
     *
     * See ast.TypeId
     */
    public byte[] getType() {
  return type;
    }

    /**
     * Returns the size (in byte) of the type of this declarator.
     * It returns -1 if the type is void, function, ...
     */
    //##22 public int getSize() {
  public long getSize() {  //##22
  return dSize;
    }

    /**
     * Returns the symbol name.  It may be null.
     */
    public String getName() {
  return dname;
    }

    /**
     * Returns storage specifiers.
     *
     * See ast.TypeId
     */
    public int getStorage() {
  return storage;
    }

    /**
     * Returns the argument list if the declared symbol is a function.
     */
    public DeclaratorList getArgs() {
  return argList;
    }

    /**
     * Returns an initializer expression if any.
     */
    public Expr getInitializer() {
  return initialzer;
    }

    /**
     * If this declarator is not a bit-field member, then this method
     * returns 0.
     */
    public int getBitFieldSize() { return bitFieldSize; }

    protected String getTag() {
  StringBuffer sbuf = new StringBuffer();
  if (typedefed)
      sbuf.append("<typedef ");
  else {
      sbuf.append("<decl ");
      sbuf.append(storage);
      sbuf.append(' ');
  }

  for (int i = 0; i < type.length; ++i)
      sbuf.append((char)type[i]);

  sbuf.append(' ');
  if (dname == null)
      sbuf.append('?');
  else
      sbuf.append(dname);
  sbuf.append('>');
  return sbuf.toString();
    }

    ////////////////////SF030531[
    /**
     * get/set array parameter size.
     */
    //##22 public int getArrayParamSize() {
  public long getArrayParamSize() { //##22
  return arrayParamSize;
    }
    //##22 public void setArrayParamSize(int s) {
  public void setArrayParamSize(long s) {  //##22
  arrayParamSize = s;
    }
    public String toString() {
  return new StringBuffer().
      append('(').
      append(getTag()).
      append("[").
      append(arrayParamSize).
      append("]").
      append(initialzer!=null?initialzer.toString():"<null>").
      append(')').
      toString();
    }
    ////////////////////SF030531]
}
