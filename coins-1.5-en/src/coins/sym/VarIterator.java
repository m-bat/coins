/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;


/** VarIterator interface  (##4)
 *  Scan all varaibles and parameters in a symbol table.
 **/
public interface 
VarIterator {

public Var next();

public boolean hasNext();

} // VarIterator interface
