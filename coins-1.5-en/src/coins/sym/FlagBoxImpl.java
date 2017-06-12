/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;


public class
FlagBoxImpl implements FlagBox   
{

  private int fFlagBox = 0;
  private int maxIndex = 32; //##fnami

public
FlagBoxImpl() { }

/** getFlag
 *  getFlag returns the value (true/false) of the flag indicated
 *  by pFlagNumber.
 *  @param pFlagNumber flag identification number.
 *  @return boolean which indicates pFlagNumber's flag.
**/
public boolean 
getFlag( int pFlagNumber ) {
    return ( (fFlagBox & (1 << pFlagNumber)) != 0 );
}

/** setFlag
 *  setFlag sets the flag of specified number.
 *  @param pFlagNumber flag identification number.
 *  @param pYesNo true or false to be set to the flag.
**/
public void 
setFlag( int pFlagNumber, boolean pYesNo ) {
  if (pYesNo)
    fFlagBox = fFlagBox | (1 << pFlagNumber);    
  else
    fFlagBox = fFlagBox & (~(1 << pFlagNumber));
}

public boolean
allFalse()
{
  if (fFlagBox == 0)
    return true;
  else
    return false;
}

public String
toString()
{
  StringBuffer lBuffer = new StringBuffer(" (flags");
  if (fFlagBox == 0)
    lBuffer.append(" none");
  else {
    for (int i = 0; i < maxIndex; i++) { //##fnami
      if (getFlag(i))
        lBuffer.append(" " + i);
    }
  }
  lBuffer.append(")");
  return lBuffer.toString();
} // toString


} // FlagBoxImpl 

