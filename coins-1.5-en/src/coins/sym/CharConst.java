/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;


//========================================//

/** CharConst interface
<PRE>
 *  Const (constant) class interface.
 *  A constant in source program is recorded as a Const instance
 *  by using its character string representation as its name.
 *  There are access methods to get the value of constants and
 *  to record a constant giving its value.
 *  Constants are usually represented by symbol table instance
 *  in order to represent attributes attached to them.
 *  Subclasses of Const is not so fine as type, for example,
 *  int, short, long, unsigned int, unsigned short, unsigned long
 *  all belongs to IntConst. To see the type of Const object,
 *  use getSymType() of SymInterface.
</PRE>
**/
public interface
CharConst extends Const
{

/**<PRE> intValue
 *  Get the value of this constant as an integer number.
 *  @return the integer value of this constant.</PRE>
 */
public int intValue();

} // CharConst interface
