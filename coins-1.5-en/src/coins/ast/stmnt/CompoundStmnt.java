/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.ast.stmnt;

import coins.ast.*;

/**
 * A linked-list of statements.
 */
public class CompoundStmnt extends ASTList implements Stmnt {
    public CompoundStmnt(Stmnt s) {
  super((ASTree)s);
    }

    public CompoundStmnt(Stmnt s, CompoundStmnt rest) {
  super((ASTree)s, rest);
    }

    protected String getTag() { return "<block>"; }

    /**
     * Returns the first element of the list.
     */
    public Stmnt get() { return (Stmnt)head(); }

    /**
     * Returns the cdr part of the list.
     */
    public CompoundStmnt next() { return (CompoundStmnt)tail(); }

    public Stmnt simplify() {
  //##101 if (tail() == null)
  //##101   return (Stmnt)head();
  //##101 BEGIN
  if (tail() == null) {
	if (head() instanceof Declarator) { // Declaration within a block.
	  return this;
	}else
	  return (Stmnt)head();
  }
  //##101 END
  else
      return this;
    }

    public String fileName() { return null; }

    public int lineNumber() { return 0; }

    protected void putSeparator(StringBuffer sbuf) {
  sbuf.append("\n\t");
    }

    public static CompoundStmnt append(CompoundStmnt block, Stmnt s) {
  return (CompoundStmnt)ASTList.concat(block, new CompoundStmnt(s));
    }

    public static CompoundStmnt concat(CompoundStmnt block1,
               CompoundStmnt block2) {
  return (CompoundStmnt)ASTList.concat(block1, block2);
    }

    public void accept(Visitor v) {
  v.atCompoundStmnt(this);
    }

}
