/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;


/** SymIteratorImpl class
 **/
public class 
SymIteratorImpl implements SymIterator 
{

  public SymTable fSymTable = null; 
  public Sym      fSymNext  = null;

public
SymIteratorImpl( SymTable pSymTable ) {
  fSymTable = pSymTable;
  fSymNext = pSymTable.getFirstSym();
} // SymIteratorImpl

//====== Methods ======//

public Sym next() {
  Sym lNext = fSymNext;
  if (fSymNext != null)
    fSymNext = fSymNext.getNextSym();
  return lNext;
} // next

public Var
nextVar()
{
  int lKind = 0;
  Sym lNext;
  for (lNext = null; hasNext();) {
    lNext = next();
    if (lNext != null) {
      lKind = lNext.getSymKind();
      if ((lKind == Sym.KIND_VAR)||(lKind == Sym.KIND_PARAM))
        break;
    }
  }
  if (lNext != null) {
    if ((lKind != Sym.KIND_VAR)&&(lKind != Sym.KIND_PARAM))
      lNext = null;
  } 
  return (Var)lNext;
} // nextVar

public boolean hasNext() {
  if (fSymNext == null)
    return false;
  else
    return true;
} // hasNext

} // SymIterator class
