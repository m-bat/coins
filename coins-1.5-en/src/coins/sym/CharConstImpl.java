/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;

import coins.SymRoot;

/** CharConstImpl class */

public class
CharConstImpl extends ConstImpl implements CharConst
{

    /** char value. "this" is char constant */
    protected char fCharValue = 0;    //##

public
CharConstImpl( SymRoot pSymRoot, String pSingleCharString, Type pType ) 
{
  super(pSymRoot); 
  fKind = Sym.KIND_CHAR_CONST;
  fName = pSingleCharString;
  fType = pType;
  fCharValue = pSingleCharString.charAt(0);
}

public
CharConstImpl( SymRoot pSymRoot, char pChar, Type pType ) 
{
  super(pSymRoot);
  fKind = Sym.KIND_CHAR_CONST;
  fName = (String.valueOf(Character.digit(pChar, 10))).intern(); //##?
  fType = pType;
  fCharValue = pChar;
}

/**<PRE> intValue 
 *  Get the value of this constant as an integer number.
 *  @return int the integer value of this constant.</PRE>
 */
public int intValue() {
    return (int)Character.digit(fCharValue, 10);
}

    /**<PRE> longValue
     *  Get the value of this constant as a long integer number.
     *  @return int the integer value of this constant.</PRE>
     */
    public long longValue() {
        return (long)Character.digit(fCharValue, 10);
    }

    /**<PRE> charValue
     *  Get the value of this constant.
     *  If type conversion is required, the returned value is the
     *  result of conversion.
     *  "this" should be a constant of type char.
     *  @return char the char value of this constant.</PRE>
     */
    public char  charValue() {
        return fCharValue;
    }

    public double doubleValue() {
        return (double)Character.digit(fCharValue, 10);
    }

    public String
    toString() 
    {
      return String.valueOf(fCharValue);
    } //toString

} // CharConstImpl class
