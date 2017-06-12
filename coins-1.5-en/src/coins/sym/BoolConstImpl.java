/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;

import coins.SymRoot;

/** BoolConstImpl class */

public class
BoolConstImpl extends ConstImpl implements BoolConst
{

    /** boolean value. "this" is boolean constant */
    protected boolean	fBooleanValue = false;           //##

public
BoolConstImpl( SymRoot pSymRoot, String pInternedName )
{
  super(pSymRoot);
  fKind = Sym.KIND_BOOL_CONST;
  fName = pInternedName;
  fType = symRoot.typeBool;
  if ((pInternedName == " true")||  // See SymRoot.
      (pInternedName == " TRUE"))   // See SymRoot.
    fBooleanValue = true;
  else
    fBooleanValue = false;
}

public
BoolConstImpl( SymRoot pSymRoot, boolean pTrueFalse )
{
  super(pSymRoot);
  fKind = Sym.KIND_BOOL_CONST;
  if (pTrueFalse)
    fName = " true";   // See SymRoot.
  else
    fName = " false";   // See SymRoot.
  fType = symRoot.typeBool;
  fBooleanValue = pTrueFalse;
}

    /**<PRE> longValue
     *  Get the value of this constant.
     *  If type conversion is required, the returned value is the
     *  result of conversion.
     *  "this" should be a constant of type integer.
     *  @return the long integer value of this constant.</PRE>
     */
    public long longValue() {
      if (fBooleanValue)
        return 1;
      else
        return 0;
    }

    public double doubleValue() {
      if (fBooleanValue)
        return 1.0;
      else
        return 0.0;
    }

public Const
getConstSym()
{
  if (fBooleanValue)
    return symRoot.intConst1;
  else
    return symRoot.intConst0;
}

public String
toStringDetail()
{
  String lString = super.toStringDetail();
  return lString + " value " + intValue();
}

} // BoolConstImpl class
