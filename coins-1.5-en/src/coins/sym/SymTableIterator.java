/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;


/** SymTableIterator interface
 *  Iterator that traverse symbol tables starting from this 
 *  symbol table and going down to subtree stemming from this table.
**/
public interface 
SymTableIterator  
{

/** next
 *  Get next symbol table traversing in the order
 *  this,
 *   1st child of this, 
 *    1st child of 1st child of this, ...
 *    2nd child of 1st child of this, ...
 *    ...
 *   2nd child of this,
 *    1st child of 2nd child of this, ...
 *    ...
 *   ...
 *  recursively.
**/
public SymTable 
next();

/** hasNext
 *  True if there are symbol tables remaining to be traversed.
**/
public boolean 
hasNext();

} // SymTableIterator
