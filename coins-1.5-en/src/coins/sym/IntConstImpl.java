/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;

import coins.SourceLanguage;
import coins.SymRoot;

/** IntConstImpl class */

public class
IntConstImpl extends ConstImpl implements IntConst
{

    /** Integer constants are recorded as long value. */
    protected long fLongValue = 0;

public
IntConstImpl( SymRoot pSymRoot, String pInternedName, Type pType ) 
{
  super(pSymRoot); 
  fKind = Sym.KIND_INT_CONST;
  fName = SourceLanguage.makeIntConstString(pInternedName, pType); 
  fType = pType;
  fLongValue = Long.parseLong(
        SourceLanguage.getPureIntString(pInternedName)); 
}

public
IntConstImpl( SymRoot pSymRoot, long pLongValue, Type pType ) 
{
  super(pSymRoot); 
  fLongValue = pLongValue;
  fType = pType;
  fKind = pType.getTypeKind(); 
  fName = SourceLanguage.makeIntConstString(
            Long.toString(pLongValue, 10), pType); 
}

    /**<PRE> longValue
     *  Get the value of this constant.
     *  If type conversion is required, the returned value is the
     *  result of conversion.
     *  "this" should be a constant of type long.
     *  @return long the long value of this constant.</PRE>
     */
    public long  longValue() {
        return fLongValue;
    }

    public double  doubleValue() {
        return (double)fLongValue;
    }

//////////////////// S.Fukuda 2002.10.30 begin
public Object evaluateAsObject()
{
  return new Long(fLongValue);
}
//////////////////// S.Fukuda 2002.10.30 enf

} // IntConstImpl class
