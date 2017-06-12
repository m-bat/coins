/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;

import java.util.ArrayList;

/** SymTableIteratorImpl class
 **/
public class 
SymTableIteratorImpl implements SymTableIterator 
{

//====== Fields ======//

  protected int       fStackDepth = 0;
  protected ArrayList fSymTableStack;
  protected SymTable  fNextSymTable;

//====== Constructor ======//

public
SymTableIteratorImpl( SymTable pSymTable ) 
{
  fStackDepth = 1;
  fSymTableStack = new ArrayList();
  fSymTableStack.add(0, pSymTable.getParent());
  fSymTableStack.add(1, pSymTable);
  fNextSymTable = pSymTable;
} // SymTableIteratorImpl

//====== Methods ======//

public SymTable 
next() 
{
  SymTable lCurrent, lNext = null;
  lCurrent = fNextSymTable;
  fNextSymTable = searchNextSymTable(true);
  return lCurrent;
} // next

public boolean 
hasNext() 
{
  if (fNextSymTable == null)
    return false;
  else 
    return true;
} // hasNext

/*  searchNextSymTable
 *  Traverse the symbol table subtree stemming from this
 *  symbol table.
 *  The current SymTable is given by fSymTableStack.get(fStackDepth).
 *  If no grand child is remaining, pop fSymTableStack and 
 *  advance to the next child SymTable.
 *  If fStackDepth < pDepthLimit, then search is over and 
 *  null is returned.
 *  If pSearchFromChild is false, then the search in the grand 
 *  child SymTable is skipped and next child is searched.
**/ 
private SymTable
searchNextSymTable( boolean pSearchFromChild ) 
{
  SymTable lCurrentSymTable, lNextSymTable = null;
  if (fStackDepth <= 0)
    return null;
  lCurrentSymTable = ((SymTable)fSymTableStack.get(fStackDepth));
  if (lCurrentSymTable == null)
    return null;
  if (pSearchFromChild) {
    lNextSymTable = lCurrentSymTable.getFirstChild();
    if (lNextSymTable != null) {
      fStackDepth++;
      fSymTableStack.add(fStackDepth, lNextSymTable);
    }
  }
  if ((lNextSymTable == null)&&
      (fStackDepth > 1)) { // No grand child or 
                               // grand child search is skipped.
    // Search nephew SymTable.
    lNextSymTable = lCurrentSymTable.getBrother();
    if (lNextSymTable != null) {
      fSymTableStack.add(fStackDepth, lNextSymTable);
    }else {  // No nephew is remaining. 
             // Pop the stack and search the next child.
      fStackDepth--;
      lNextSymTable = searchNextSymTable(false);
    }
  } 
  return lNextSymTable;
} // searchNextSymTable

} // SymTableIteratorImpl class
