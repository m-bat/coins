/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;


/** SymTableEntryImpl -- Symbol tabel entry class
**/

public class 
SymTableEntryImpl implements SymTableEntry 
{

  public String        key;
  public Sym           value;
  public SymTableEntry	next;

public 
SymTableEntryImpl( String pKey, Sym pSym, SymTableEntry pNext ) {
  key   = pKey;
  value = pSym;
  next  = pNext;
}

public Sym
getEntrySym() { 
  return value;
}

public void 
setEntrySym( Sym pSym ) {
  value = pSym;
}

public void 
setEntrySym( Sym pSym, Sym pDefinedIn ) {
  value = pSym;
  pSym.setDefinedIn(pDefinedIn);
}

} // SymTableEntryImpl class

