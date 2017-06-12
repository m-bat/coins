/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.ast;

/**
 * A linked list of <code>Declarator</code> objects.
 */
public class DeclaratorList extends ASTList {
    public DeclaratorList(Declarator d) {
  super(d);
    }

    public DeclaratorList(Declarator d, DeclaratorList rest) {
  super(d, rest);
    }

    public void accept(Visitor v) {
  v.atDeclaratorList(this);
    }

    protected String getTag() { return "<decl-list>"; }

    public Declarator get() { return (Declarator)head(); }

    public DeclaratorList next() { return (DeclaratorList)tail(); }

    protected void putSeparator(StringBuffer sbuf) {
  sbuf.append("\n\t");
    }

    public static DeclaratorList append(DeclaratorList list,
          Declarator d) {
  return (DeclaratorList)ASTList.concat(list,
              new DeclaratorList(d));
    }

    public static DeclaratorList concat(DeclaratorList list,
          DeclaratorList list2) {
  return (DeclaratorList)ASTList.concat(list, list2);
    }
}
