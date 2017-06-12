/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;


/** VarIteratorImpl class   (##4)
 **/
public class 
VarIteratorImpl implements VarIterator {

  public SymTable fSymTable = null; 
  public Sym      fSymNext  = null;

public
VarIteratorImpl( SymTable pSymTable ) {
  fSymTable = pSymTable;
  fSymNext = pSymTable.getFirstSym();
} // VarIteratorImpl

//====== Methods ======//

public Var next() {
  int lKind;
  Sym lNext;
  for (lNext = fSymNext; lNext != null; 
       lNext = lNext.getNextSym()) {
    lKind = lNext.getSymKind();
    if ((lKind == Sym.KIND_VAR)||(lKind == Sym.KIND_PARAM))
      break;
  }
  if (lNext != null)
    fSymNext = lNext.getNextSym();
  else
    fSymNext = null;
  return (Var)lNext;
} // next

public boolean hasNext() {
  Sym lSymCurr, lSymNext;
  int lKind;
  if (fSymNext == null)
    return false;
  else if (fSymNext.getNextSym() != null) {
    lSymCurr = fSymNext;
    for (lSymNext = fSymNext; lSymNext != null; 
         lSymNext = lSymNext.getNextSym()) {
      lKind = lSymNext.getSymKind();
      if ((lKind == Sym.KIND_VAR)||(lKind == Sym.KIND_PARAM))
        break;
      else
        lSymCurr = lSymNext;
    }
    if (lSymNext != null) {
      fSymNext = lSymCurr;
      return true;
    }else {
      fSymNext = null;
      return false;
    }
  }else
    return false;
} // hasNext

} // VarIteratorImpl class
