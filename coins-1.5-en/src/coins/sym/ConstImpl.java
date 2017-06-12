/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;

import coins.SymRoot;

/** Constant class */

public abstract class
//##60  ConstImpl extends OperandSymImpl implements Const
ConstImpl extends SymImpl implements Const //##60
{

public
ConstImpl( SymRoot pSymRoot )
{
  super(pSymRoot);
}

    /**<PRE> intValue
     *  Get the value of this constant.
     *  If type conversion is required, the returned value is the
     *  result of conversion.
     *  "this" should be a constant of type integer.
     *  @return int the integer value of this constant.</PRE>
     */
public int
intValue()
{
  return (int)longValue();
}

public short
shortValue()
{
    return (short)longValue();
}

    /**<PRE> shortIntValue
     *  Get the value of this constant.
     *  If type conversion is required, the returned value is the
     *  result of conversion.
     *  "this" should be a constant of type short.
     *  @return short the short value of this constant.</PRE>
     */

    /**<PRE> longIntValue
     *  Get the value of this constant.
     *  If type conversion is required, the returned value is the
     *  result of conversion.
     *  "this" should be a constant of type long.
     *  @return long the long value of this constant.</PRE>
     */

    /**<PRE> charValue
     *  Get the value of this constant.
     *  If type conversion is required, the returned value is the
     *  result of conversion.
     *  "this" should be a constant of type char.
     *  @return char the char value of this constant.</PRE>
     */
public char
charValue()
{
  return Character.forDigit(intValue(), 10);
}

    /**<PRE> floatValue
     *  Get floating/double value of this constant.
     *  If type conversion is required, the returned value is the
     *  result of conversion.
     *  "this" should be a constant of type float, or double.
     *  floatValue  return the float  value of this constant.
     *  doubleValue return the double value of this constant.</PRE>
     */
public float
floatValue()
{
  return (float)doubleValue();
}

public Const
getConstSym()
{
  return this;
}

//////////////////// S.Fukuda 2002.10.30 begin
/**
 * Get the value of this constant symbol.
 * This method is overrided as follows
 * class       returned value
 * IntConst     Long
 * FloatConst   Double
 * StringConst  String
 * Var          Initail value (ExpListExp, etc.) if qualified by const,
 *              else null.
 **/
public Object evaluateAsObject(){ return null; }
//////////////////// S.Fukuda 2002.10.30 enf

} // ConstImpl class
