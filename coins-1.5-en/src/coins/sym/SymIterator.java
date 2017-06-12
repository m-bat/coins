/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;


/** SymIterator interface
 **/
public interface 
SymIterator {

/** next
 *  Get next symbol in the specified symbol table.
**/
public Sym 
next();

/** nextVar
 *  Get next symbol in the specified symbol table
 *  skipping symbols that are not a Var symbol.
 *  If the last symbol is not a variable, then null will be returned
 *  as the last symbol.
**/
public Var
nextVar();

/** hasNext
 *  True if there are symbols remaining in the specified symbol table,
 *  false otherwise.
**/
public boolean 
hasNext();

} // SymIterator interface
