/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;


/** SymTableEntry -- Symbol tabel entry interface
**/

public interface 
SymTableEntry  
{

Sym
getEntrySym(); 

void 
setEntrySym( Sym pSym );

void 
setEntrySym( Sym pSym, Sym pDefinedIn );

} // SymTableEntry interface

