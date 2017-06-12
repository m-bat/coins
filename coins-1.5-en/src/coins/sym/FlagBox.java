/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;

public interface
FlagBox {

/** getFlag
 *  getFlag returns the value (true/false) of the flag indicated
 *  by pFlagNumber.
 *  @param pFlagNumber flag identification number.
 *  @return boolean which indicates pFlagNumber's flag.
**/
public boolean 
getFlag( int pFlagNumber );

/** setFlag
 *  setFlag sets the flag of specified number.
 *  @param pFlagNumber flag identification number.
 *  @param pYesNo true or false to be set to the flag.
**/
public void 
setFlag( int pFlagNumber, boolean pYesNo );

/** allFalse
 *  true if all flags are false,
 *  flase if some flag is true;
**/
public boolean
allFalse();

/** toString
 *  Get all true flags by giving their flag number.
**/
public String
toString();

} // FlagBox

