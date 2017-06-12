/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;

import coins.MachineParam;
import coins.SymRoot;
import coins.ir.IrList;

public class
EnumTypeImpl extends TypeImpl implements EnumType {

  protected IrList  fElemList = null;
  private   int     fEnumSeqNumber = 0; // Enum sequence number.

  protected Sym
    fTag;       // Tag name symbol.

/** EnumTypeImpl constructor
 *  Make an instance of EnumType in symRoot.symTableCurrent.
 *  It is not recommended to use this constructor directly
 *  but use enumType method of HIR because there are
 *  several restrictions in using this constructor.
 *  @param pSymRoot SymRoot instance to be passed to the
 *      resultant type.
 *  @param pTypeName Name of the enum type.
 *      It shoul be <ENUM tagName> where tagName is the
 *      tag name the enum. If it has no tag name, generate it
 *      by generateTag() of SymTable.
 *  @param pElemList List of elements of the enum.
 *      If it is not known, then give null and later addElem()
 *      and finishEnumType().
**/
public
EnumTypeImpl( SymRoot pSymRoot, String pTypeName, IrList pElemList )
{
  super(pSymRoot);
  fKind = Sym.KIND_TYPE;
  fType = this;
  fTypeKind    = Type.KIND_ENUM;
  setSizeValue(machineParam.evaluateSize(Type.KIND_INT)); //##52
  fName = pTypeName;
  if (pElemList == null) {
    fElemList = symRoot.getHirRoot().hir.irList();
    setFlag(Sym.FLAG_INCOMPLETE_TYPE, true);
    fCompleteType = null;
  }else { // pElemList != null
    fElemList = pElemList;
    finishEnumType(false);
  }
  setFlag(Sym.FLAG_UNIFORM_SIZE, true);
} // EnumTypeImpl

public int
getEnumSeqNumber() { return fEnumSeqNumber; }

public void
setEnumSeqNumber( int pNumber ) { fEnumSeqNumber = pNumber; }

public IrList
getElemList() {
  return fElemList;
}

public void
addElem( NamedConst pElem ) {
  IrList lElemNumberPair = symRoot.getHirRoot().hir.irList();
  lElemNumberPair.add(pElem);
  lElemNumberPair.add(symRoot.sym.intObject(pElem.getIndexValue()));
  if (fDbgLevel > 3) //##58
    symRoot.ioRoot.dbgSym.print(6, "addElem", pElem.getName() + " "
      + pElem.getIndexValue());
  if (fElemList == null)
    fElemList = symRoot.getHirRoot().hir.irList();
  fElemList.add(lElemNumberPair);
} // addElem

public void
finishEnumType( boolean pSeparately )
{
  String lName;
  lName = symRoot.sym.makeEnumTypeName(fElemList);
  if (fDbgLevel > 3) //##58
    symRoot.ioRoot.dbgSym.print(6, "finishEnumType", toString() + " " +lName
      + " tag " + ((fTag==null) ? "null" : fTag.getName()) + " ");
  //fTypeSize = MachineParam.SIZEOF_INT; //S.Fukuda 2002.10.30
  if (pSeparately) {
    if (fOrigin == null)
      fOrigin = ((SymImpl)symRoot.sym).enumType(lName, fElemList, null);
  }else if (fOrigin == null)
    fOrigin = this;
/**
  if (fTag == null) {
    setTag(symRoot.symTableCurrent.generateTag());
    fTag.setSymType(this);
  }
**/
  setFlag(Sym.FLAG_INCOMPLETE_TYPE, false);
  if (fOrigin != null)
    fCompleteType = fOrigin;
  else
    fCompleteType = this;
} // finishEnumType

public Sym
getTag()
{
  return fTag;
}

public void
setTag( Sym pTag )
{
  if ((pTag != null)&&(pTag.getSymKind() == Sym.KIND_TAG))
    fTag = pTag;
}

public boolean
isCompatibleWith( Type pType )
{
  if (pType == this)
    return true;
  else if (pType == null)
    return false;
  else {
    if ((pType.getTypeKind() == Type.KIND_ENUM)&&
        (pType.isConst() == isConst())&&
        (pType.isVolatile() == isVolatile())) {
      if ((getOrigin() != null)&&
          (getOrigin().isCompatibleWith(((EnumType)pType).getOrigin())))
        return true;
    }
  }
  return false;
} // isCompatibleWith

public Type
getCompleteType()
{
  if (getFlag(Sym.FLAG_INCOMPLETE_TYPE)) {
    if (getOrigin() != null)
      return getOrigin().getCompleteType();
    else
      return null;
  }else
    return this;
} // getCompleteType

public String
toStringDetail() {
  String symString = super.toStringDetail();
  symString = symString + " tag " + getNameOrNull(fTag);
  return symString;
} // toStringDetail

} // EnumTypeImpl class

