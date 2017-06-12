/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;


/** SymNestIterator interface
 *  Iterator that traverse symbols in given symbol table and its children.
**/
public interface 
SymNestIterator  {

/** next
 *  Get next symbol in the specified symbol table and its children.
 *  If all symbols are traversed in the specified symbol table, 
 *  then symbols in the first child symbol table and then
 *  its brothers are traversed, that is, traversing in the order:
 *    this,
 *     1st child of this, 
 *      1st child of 1st child of this, ...
 *      2nd child of 1st child of this, ...
 *      ...
 *     2nd child of this,
 *      1st child of 2nd child of this, ...
 *      ...
 *     ...
 *  recursively.
**/
public Sym 
next();

/** nextVar
 *  Get the next symbol in the specified symbol table and its children
 *  skipping symbols that are not a Var symbol.
 *  If the last symbol is not a variable, then null will be returned
 *  as the last symbol.
**/
public Var
nextVar();

/** hasNext
 *  True if there are symbols remaining in the specified symbol table
 *  or its children, false otherwise.
**/
public boolean 
hasNext();

} // SymNestIterator
