/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;

import coins.SymRoot;
import coins.ir.IrList;
import coins.ir.IrListImpl;

/** SubpTypeImpl Class
**/
public class
SubpTypeImpl extends TypeImpl implements SubpType
{

  /** Parameter type list if this is function type */
  protected IrList
    fParamTypeList = null;

  /** Return Type if this is function type */
  protected Type
    fReturnType = null;

  /** True if this subprogram has optional param (... in C) */
  protected boolean
    fOptionalParam = false;

  /** True if any number of parameters of any kind are permitted. */
  protected boolean
    fNoParamSpec = false;              //##53

public
SubpTypeImpl( SymRoot pSymRoot, String pSubpTypeName, Type pReturnType,
              IrList pParamList, boolean pOptionalParam,
             boolean pNoParamSpec ) //##53
{
  super(pSymRoot);
  fName = pSubpTypeName;
  fKind = Sym.KIND_TYPE;
  fType = this;
  fTypeKind = Type.KIND_SUBP;
  ///////////////////////////////////// S.Fukuda
  fParamTypeList = new IrListImpl(symRoot.getHirRoot());
  Sym lParamListSym;
  if (pParamList != null) {
    for (java.util.Iterator lParamIterator = pParamList.iterator();
         lParamIterator.hasNext(); ) {
      lParamListSym = (Sym) (lParamIterator.next());
      if (lParamListSym.getSymKind() == Sym.KIND_TYPE)
        fParamTypeList.add( (Type) lParamListSym);
      else if (lParamListSym.getSymKind() == Sym.KIND_PARAM)
        fParamTypeList.add( ( (Param) lParamListSym).getSymType());
    }
  }
  fReturnType    = pReturnType;
  fOptionalParam = pOptionalParam;
  fNoParamSpec = pNoParamSpec; //##53
  //fTypeSize = pReturnType.getSizeValue();
    // S.Fukuda 2002.10.20 Function has no size value.
  setFlag(Sym.FLAG_UNIFORM_SIZE, false);
} // SubpTypeImpl

/**
 * @param pSymRoot
 * @param pSubpTypeName
 * @param pReturnType
 * @param pParamList
 * @param pOptionalParam
 * ***deprecated
 */
//##68 public
//##68 SubpTypeImpl( SymRoot pSymRoot, String pSubpTypeName, Type pReturnType,
//##68               IrList pParamList, boolean pOptionalParam)        //##53
//##68 {
//##68  this(pSymRoot, pSubpTypeName, pReturnType, pParamList,
//##68       pOptionalParam, false);
//##68 }

/** getParamTypeList
 *  Get a component of function type.
 *  "this" should be a function type symbol.
 *  @return IrList parameter type list.
**/
public IrList
getParamTypeList() {
  return fParamTypeList;
}

/** getReturnType
 *  Get a component of function type.
 *  "this" should be a function type symbol.
 *  @return Type function value type.
**/
public Type
getReturnType() {
  return fReturnType;
}

public boolean
hasOptionalParam()
{
  return fOptionalParam;
}

public boolean
hasNoParamSpec()  //##53
{
  return fNoParamSpec;
}

public boolean
isCompatibleWith( Type pType )
{
  IrList lParamTypeList1, lParamTypeList2;
  Type   lType1, lType2;
  if (pType == this)
    return true;
  else if (pType == null)
    return false;
  else {
    if ((pType.getTypeKind() == Type.KIND_SUBP)&&
        (pType.isConst() == isConst())&&
        (pType.isVolatile() == isVolatile())) {
      if (! ((SubpType)pType).getReturnType().
             isCompatibleWith(getReturnType()))
        return false;
      if (((SubpType)pType).hasOptionalParam() != hasOptionalParam())
        return false;
      lParamTypeList1 = getParamTypeList();
      lParamTypeList2 = ((SubpType)pType).getParamTypeList();
      if ((lParamTypeList1 == null)&&(lParamTypeList2 == null))
        return true;
      if ((lParamTypeList1 != null)&&(lParamTypeList2 != null)) {
        java.util.ListIterator lIterator1 = lParamTypeList1.iterator();
        java.util.ListIterator lIterator2 = lParamTypeList2.iterator();
        while (lIterator1.hasNext()&&lIterator2.hasNext()) {
          lType1 = (Type)lIterator1.next();
          lType2 = (Type)lIterator2.next();
          if (! lType2.isCompatibleWith(lType1))
            return false;
        }
        if ((! lIterator1.hasNext())&&(! lIterator2.hasNext()))
          return true;
      }
      return true;
    }
  }
  return false;
} // isCompatibleWith

} // SubpTypeImpl
