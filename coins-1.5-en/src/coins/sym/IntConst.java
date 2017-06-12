/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;


//========================================//

/**
<PRE>
 *  IntConst interface
 *  Const (constant) class interface.
 *  A constant in source program is recorded as a Const instance
 *  by using its character string representation as its name.
 *  There are access methods to get the value of constants and
 *  to record a constant giving its value.
 *  Constants are usually represented by symbol table instance
 *  in order to represent attributes attached to them.
 *  Subclasses of Const is not so fine as type, foe example,
 *  int, short, long, unsigned int, unsigned short, unsigned long
 *  all belongs to IntConst. To see the type of Const object,
 *  use getSymType() of SymInterface.
</PRE>
**/
public interface
IntConst extends Const
{
//////////////////// S.Fukuda 2002.10.30 begin
/**
 * Evaluate as a Long object (not as a binary
 * integer number).
 * @return the evaluated object.
 */
public Object evaluateAsObject();
//////////////////// S.Fukuda 2002.10.30 enf
} // IntConst
