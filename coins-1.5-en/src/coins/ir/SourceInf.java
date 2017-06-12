/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir;


/** SourceInf interface
 *  Source program information such as file name, line number,
 *  column number are gathered in this interface.
 *  The source program information may be attached to IR nodes
 *  or symbols to show the source program position corresponding to
 *  the IR node, or to show the source program position where
 *  the symbol is defined. The file name, line number, column number
 *  are got by corresponding methods.
**/

public interface
SourceInf {

/** getDefinedFile
 *  @return the name of the source program file.
 *      If it is not given, then return null.
 */
public String getDefinedFile();

/** setDefinedFile
 *  Set the name of the source program file.
 *  @param pDefinedFile name of the file.
**/
public void
setDefinedFile( String pDefinedFile );

/** getDefinedLine
 *  @return the line number.
 *      If it is not given, then return 0.
**/
public int getDefinedLine();

/** setDefinedLine
 *  Set the line number.
 *  @param pDefinedLine the line number to be set.
**/
public void
setDefinedLine( int pDefinedLine );

/** getDefinedColumn
*  @return the column number.
*      If it is not given, then return 0.
*/
public int getDefinedColumn();

public String
toString();

}
