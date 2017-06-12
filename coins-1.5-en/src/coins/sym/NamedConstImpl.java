/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;

import coins.SymRoot;

/** ConstImpl class */

public class
NamedConstImpl extends ConstImpl implements NamedConst
{

    /** Index value. */
    protected long fIndexValue = 0;

    /** Constant value */
    protected Const fConstValue = null;

//==== Constructors ====//

public
NamedConstImpl( SymRoot pSymRoot, String pInternedName, int pIndex )
{
  super(pSymRoot);
  fKind = Sym.KIND_NAMED_CONST;
  fName  = pInternedName;
  fType  = symRoot.typeInt;
  fIndexValue = pIndex;
  fConstValue = symRoot.sym.intConst((long)pIndex, symRoot.typeInt);
}

public
NamedConstImpl( SymRoot pSymRoot, String pInternedName, Const pConst )
{
  super(pSymRoot);
  fKind = Sym.KIND_NAMED_CONST;
  fName  = pInternedName;
  fType  = pConst.getSymType();
  fConstValue = pConst;
  fIndexValue = pConst.longValue();
}

//==== Methods ====//

public Const
getConstValue() {
  return fConstValue;
}

public int
getIndexValue() {
  return (int)fIndexValue;
}

    /** longValue
     *  Get the value of this constant.
     *  If type conversion is required, the returned value is the
     *  result of conversion.
     *  "this" should be a constant of type long.
     *  @return long the long value of this constant.
     */
    public long  longValue() {
        return fIndexValue;
    }

    /** doubleValue
     *  Get double value of this constant.
     *  If type conversion is required, the returned value is the
     *  result of conversion.
     *  "this" should be a constant of type float, or double.
     *  @return the double value of this constant.
     */
    public double doubleValue() {
        return (double)fIndexValue;
    }

public Const
getConstSym()
{
  return fConstValue;
}

public String
toString() {
  return fName + " " + fIndexValue;
} // toString

public String
toStringShort() {
  return fName + " " + fIndexValue;
} // toStringShort

public String
toStringDetail() {
  String lString = getSymKindName() + " " + toString();
  String lUniqueName = getUniqueName();
  if (lUniqueName != "")
    lString = lString + " unique:" + lUniqueName;
  String lDefinedIn = getDefinedInName();
  if (lDefinedIn != "")
    lString = lString + " in " + lDefinedIn;
  if ((fSourceInf != null)&&
      (fSourceInf.getDefinedFile() != null))
    lString = lString + " " + fSourceInf.toString();
  return lString;
} // toStringDetail

} // NamedConstImpl class
