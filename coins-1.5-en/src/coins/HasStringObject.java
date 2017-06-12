/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins;

import java.util.*; //##20

/** HasStringObject:
 *  Interface for objects that has text string image.
 *  (IR, HIR, Sym, etc.)
 *  This interface declares methods toString and print.
**/
public interface
HasStringObject extends Cloneable
{

/** Get the string image of object.
**/
public String
toString();

/** Print the object.
 * @param pIndent: indentation level.
**/
public void
print( int pIndent );

/** Print the object.
 * @param pIndent: indentation level.
 * @param pDetail: true if detailed information is requested,
 *                 false otherwise.
**/
public void
print( int pIndent, boolean pDetail );

/**  Test if this is a Sym object.
 *  @return  true if this is a Sym object, false otherwise.
**/
boolean
isSym();      // (##5)

/**  Test if this is an HIR object.
 *  @return  true if this is an HIR object, false otherwise.
**/
boolean
isHIR();      // (##5)

} // HasStringObject

