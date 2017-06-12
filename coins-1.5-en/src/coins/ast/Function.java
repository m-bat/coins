/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.ast;

/**
 * Declaration of a function.
 */
public class Function extends ASTree implements TypeId, Stmnt {
    private String fileName;
    private int lineNumber;

    private String dname;
    private byte[] type;
    private int storage;
    private DeclaratorList argList;
    private Stmnt functionBody;

    /**
     * Constructs a function.
     *
     * <p>Note: to parse a function declaration, the parser first tries
     * to construct <code>Declarator</code> object but it discards that
     * object and constructs <code>Function</code> object for substitution.
     *
     * @param decl	the function name, the return type,
     *			and the argument list.
     * @param body	the function body.
     */
    public Function(Declarator decl, Stmnt body) {
  fileName = decl.fileName();
  lineNumber = decl.lineNumber();
  dname = decl.getName();
  type = decl.getType();
  storage = decl.getStorage();
  argList = decl.getArgs();
  functionBody = body;
    }

    /**
     * Returns an argument list.
     */
    public ASTree getLeft() { return argList; }

    /**
     * Returns a function body.
     */
    public ASTree getRight() { return (ASTree)functionBody; }

    public void setLeft(ASTree _left) {
  argList = (DeclaratorList)_left;
    }

    public void setRight(ASTree _right) {
  functionBody = (Stmnt)_right;
    }

    public void accept(Visitor v) {
  v.atFunction(this);
    }

    public String fileName() { return fileName; }

    public int lineNumber() { return lineNumber; }

    /**
     * Returns the function name.
     */
    public String getName() { return dname; }

    /**
     * Returns the encoded function type.
     *
     * See ast.TypeId
     */
    public byte[] getType() { return type; }

    /**
     * Returns the storage specifiers.
     *
     * See ast.TypeId
     */
    public int getStorage() { return storage; }

    /**
     * Returns the argument list.
     */
    public DeclaratorList getArguments() { return argList; }

    /**
     * Returns the function body.
     */
    public Stmnt getBody() { return functionBody; }

    protected String getTag() {
  StringBuffer sbuf = new StringBuffer();
  sbuf.append("<func ");
  sbuf.append(storage);
  sbuf.append(' ');
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

    protected void putSeparator(StringBuffer sbuf) {
  sbuf.append("\n\t");
    }
}
