/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;

import java.util.ArrayList;

/** SymNestIteratorImpl class
 **/
public class 
SymNestIteratorImpl implements SymNestIterator 
{

//====== Fields ======//

  protected int       fStackDepth = 0;
  protected ArrayList fSymTableStack;
  protected Sym       fSymNext  = null;

//====== Constructor ======//

public
SymNestIteratorImpl( SymTable pSymTable ) 
{
  fStackDepth = 1;
  fSymTableStack = new ArrayList();
  fSymTableStack.add(0, pSymTable.getParent());
  fSymTableStack.add(1, pSymTable);
  fSymNext = pSymTable.getFirstSym();
} // SymNestIteratorImpl

//====== Methods ======//

public Sym 
next() 
{
  Sym lCurrent, lNext = null;
  lCurrent = fSymNext;
  if (fSymNext != null)
    lNext = fSymNext.getNextSym();
  if (lNext == null) {
    lNext = searchNextSym(true); 
  }
  fSymNext = lNext;
  return lCurrent;
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

public boolean 
hasNext() 
{
  if (fSymNext == null)
    return false;
  else 
    return true;
} // hasNext

/*  searchNextSym
 *  Traverse the symbol table subtree stemming from this
 *  symbol table and get their symbols successively.
 *  The current SymTable is given by fSymTableStack.get(fStackDepth).
 *  If no grand child is remaining, pop fSymTableStack and 
 *  advance to the next child SymTable.
 *  pDepthLimit is the lower limit of SymTable stack 
 *  to stop searching not to go upward too much.
 *  If fStackDepth < 1, then search is over and 
 *  null is returned.
 *  If pSearchFromChild is false, then the search in the grand 
 *  child SymTable is skipped and next child is searched.
**/ 
private Sym
searchNextSym( boolean pSearchFromChild ) 
{
  SymTable lCurrentSymTable, lNextSymTable = null;
  Sym      lNextSym = null;
  if (fStackDepth < 1) 
    return null;
  lCurrentSymTable = ((SymTable)fSymTableStack.get(fStackDepth));
  if (lCurrentSymTable == null)
    return null;
  if (pSearchFromChild) {
    lNextSymTable = lCurrentSymTable.getFirstChild();
    if (lNextSymTable != null) {
      fStackDepth++;
      fSymTableStack.add(fStackDepth, lNextSymTable);
      lNextSym = lNextSymTable.getFirstSym();
      if (lNextSym == null) 
        lNextSym = searchNextSym( true);  
    }
  }
  if ((lNextSymTable == null)&&
      (fStackDepth > 1)) { // No child or child search is skipped. 
    // Search in nephew SymTable.
    lNextSymTable = lCurrentSymTable.getBrother();
    if (lNextSymTable != null) {
      fSymTableStack.add(fStackDepth, lNextSymTable);
      lNextSym = lNextSymTable.getFirstSym();
      if (lNextSym == null) 
        lNextSym = searchNextSym(true);  
    }else {  // No nephew is remaining. 
             // Pop the stack and search the next child.
      fStackDepth--;
      lNextSym = searchNextSym(false); 
    }
  } 
  return lNextSym;
} // searchNextSym

} // SymNestIteratorImpl class
