/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;

import coins.SymRoot;

/** FloatConstImpl class */

public class 
FloatConstImpl extends ConstImpl implements FloatConst 
{

    /** double value. "this" is double constant */ 
    protected double	fDoubleValue	= 0;

public
FloatConstImpl( SymRoot pSymRoot, String pInternedName, Type pType ) 
{
  super(pSymRoot); 
  fKind = Sym.KIND_FLOAT_CONST; 
  fName = pInternedName;
  fType = pType;
  fDoubleValue = (new Double(pInternedName)).doubleValue();
}

public
FloatConstImpl( SymRoot pSymRoot, double pDoubleValue, Type pType ) 
{
  super(pSymRoot); 
  fKind = Sym.KIND_FLOAT_CONST; 
  fName = String.valueOf(pDoubleValue);
  fType = pType;
  fDoubleValue = pDoubleValue; 
}

    public long longValue() {
        return (long)fDoubleValue;
    }

    /**<PRE> doubleValue
     *  Get floating/double value of this constant.
     *  If type conversion is required, the returned value is the
     *  result of conversion.
     *  "this" should be a constant of type float, or double.
     *  @return the double value of this constant.</PRE>
     */
    public double doubleValue() {
        return fDoubleValue;
    }

//////////////////// S.Fukuda 2002.10.30 begin
public Object evaluateAsObject()
{
  return new Double(fDoubleValue);
}
//////////////////// S.Fukuda 2002.10.30 enf

} // FloatConstImpl class
