/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.ast;

import coins.casttohir.ToHirC;
import coins.sym.UnionType;

/**
 * Declaration of a union type.
 *
 * See ast.Aggregate
 */
public class Union extends Aggregate
{
  public Union(
    String name,
    DeclaratorList mems,
    String fname,
    int line,
    ToHirC tohirc )
  {
    super(name,mems,fname,line);
    UnionType un = tohirc.atUnionDeclarator(this); //SF040123
    setSize( un!=null ? un.getSizeValue() : 0 ); //SF041014
  }

  public void accept(Visitor v)
  {
    v.atUnion(this);
  }

  protected String getTag()
  {
    return "<union " + name() + ">";
  }
}
