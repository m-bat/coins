/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;


/** SymInf Symbol information class to represent
 *  additional information of symbol.
**/

public class
SymInf
{

  /** fDerivedSymCount Derived symbol count for this symbol */
  private int
    fDerivedSymCount = 0;

  private Sym
    fOriginalSym = null; // Original symbol corresponding to UniqueName symbol.
public
SymInf( )
{
}

/** getDerivedSymCount
 *  @return the derived symbol count of this symbol.
**/
public int
getDerivedSymCount( ) {
  return fDerivedSymCount;
}

/** incrementDerivedSymCount Increment the derived symbol
 *  count and return it.
 *  @return the derived symbol count incremented.
**/
public int
incrementDerivedSymCount( ) {
  fDerivedSymCount++;
  return fDerivedSymCount;
}

//##70 BEGIN
public Sym
getOriginalSym()
{
  return fOriginalSym;
}

public void
setOriginalSym( Sym pOriginalSym )
{
  fOriginalSym = pOriginalSym;
}
//##70 END
} // SymInf class
