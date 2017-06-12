/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.ast;

import coins.casttohir.ToHirC;
import coins.sym.StructType;

/**
 * Declaration of a struct type.
 *
 * See ast.Aggregate
 */
public class Struct extends Aggregate
{
  public Struct(
    String name,
    DeclaratorList mems,
    String fname,
    int line,
    ToHirC tohirc )
  {
    super(name,mems,fname,line);
    StructType st = tohirc.atStructDeclarator(this); //SF040123
    setSize( st!=null ? st.getSizeValue() : 0 ); //SF041014
  }

  public void accept(Visitor v)
  {
    v.atStruct(this);
  }

  protected String getTag()
  {
    return "<struct " + name() + ">";
  }
}
