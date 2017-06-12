/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.ir.IrList; //##70

//========================================//
//                        Oct. 8, 2002.

/**
 *  Information node treated as a statement
 *	 (pragma, comment line, etc.)
 *  See IR.
 **/
public interface
InfStmt extends Stmt
{

/**
<PRE>
 * Get the kind of infromation attached to this Stmt.
 * For example, int the pragma
 *   #pragma optControl inline subp1 subp2
 * the string "optControl" is returned.
</PRE>
 * @return the String representing the kind of information.
 */
  public String
  getInfKind();

//##71 BEGIN
/**
<PRE>
 * Get the first element of the InfList as a String
 * which may be a string showing the sub-kind of the information
 * attached to this Stmt. The result is a string interned().
 * For example, int the pragma
 *   #pragma optControl inline subp1 subp2
 * the string "inline" is returned.
 * If there is no such element, return null.
</PRE>
 * @param pInfKind the kind of information attached to this Strmt.
 * @return the first element of the InfList.
 */
public String
getInfSubkindOf( String pInfKind );
//##71 END

/**
 * Get the list of infromation attached to this Stmt.
 * @param pInfKind the kind of information attached to this Strmt.
 * @return the list of infromation attached to this Stmt.
 */
  public IrList
    getInfList(String pInfKind);

} // InfStmt interface
