/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir;

/** SourceInfImpl class
**/

public class
SourceInfImpl implements SourceInf
{

    /** The the name of the source program file. */
    private	String	fDefFile = null;

    /** The line number in the source file. */
    private	int	fDefLine = 0;

    /** The column number in fDefLine. */
    private	int	fDefColumn = 0;

    public String getDefinedFile() {
        return fDefFile;
    }

public void
setDefinedFile( String pDefinedFile )
{
  fDefFile = pDefinedFile;
}

public int getDefinedLine() {
   return fDefLine;
}

public void
setDefinedLine( int pDefinedLine )
{
  fDefLine = pDefinedLine;
}

public int getDefinedColumn() {
  return fDefColumn;
}

public String
toString() {
  String lString;
  if (fDefFile != null)
    lString = "File " + fDefFile + " line " + fDefLine;
  else
    lString = "";
  return lString;
} // toString

} // SourceInfImpl
