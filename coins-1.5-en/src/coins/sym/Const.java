/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;


//========================================//

/** Const interface
<PRE>
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
//##60 Const extends OperandSym
Const extends Sym //##60
{

/** intValue
 *  Get the value of this constant.
 *  If type conversion is required, the returned value is the
 *  result of conversion.
 *  "this" should be a constant of type integer, short integer,
 *  long integer, or character of either signed or unsigned.
 *  @return the integer value of this constant.
**/
int   intValue();

/** shortValue
<PRE>
 *  Get the value of this constant.
 *  If type conversion is required, the returned value is the
 *  result of conversion.
 *  "this" should be a constant of type integer, short integer,
 *  long integer, or character of either signed or unsigned.
</PRE>
 *  @return the short int value of this constant.
**/
short shortValue();

/** longValue
<PRE>
 *  Get the value of this constant.
 *  If type conversion is required, the returned value is the
 *  result of conversion.
 *  "this" should be a constant of type integer, short integer,
 *  long integer, or character of either signed or unsigned.
</PRE>
 *  @return the long int value of this constant.
**/
long  longValue();

/** charValue
<PRE>
 *  Get the value of this constant.
 *  If type conversion is required, the returned value is the
 *  result of conversion.
 *  "this" should be a constant of type integer, short integer,
 *  long integer, or character of either signed or unsigned.
</PRE>
 *  @return the char value of this constant.
**/
char  charValue();

/** floatValue
<PRE>
 *  Get floating value of this constant.
 *  If type conversion is required, the returned value is the
 *  result of conversion.
 *  "this" should be a constant of type float, or double.
</PRE>
 *  @return the float  value of this constant.
 */
float  floatValue();

/** doubleValue
<PRE>
 *  Get double value of this constant.
 *  If type conversion is required, the returned value is the
 *  result of conversion.
 *  "this" should be a constant of type float, or double.
</PRE>
 *  @return the double value of this constant.
 */
double doubleValue();

/** getConstSym
 <PRE>
 *  If      boolean true  then return symRoot.intConst1
 *  else if boolean false then return symRoot.intConst0
 *  else if enumeration constant then return corresponding IntConst
 *  else return this.
</PRE>
**/
public Const
getConstSym();

//////////////////// S.Fukuda 2002.10.30 begin
/**
<PRE>
 * Get the value of this constant symbol.
 * This method is overrided as follows:
 * class       returned value
 * IntConst     Long
 * FloatConst   Double
 * StringConst  String
 * Var          Initail value (ExpListExp, etc.) if qualified by const,
 *              else null.
</PRE>
 * @return the constant value of appropriate type.
 **/
public Object evaluateAsObject();
//////////////////// S.Fukuda 2002.10.30 enf
} // Const
