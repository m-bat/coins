/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;

import coins.SymRoot;
import coins.ir.IrList;

/** UnionTypeImpl Class
**/
public class
UnionTypeImpl extends TypeImpl implements UnionType
{

  protected IrList  fElemList = null;

  protected SymTable
    fLocalSymTable;

  protected Sym
    fTag;       // Tag name symbol.

  protected int
    fAlignment;

/** UnionTypeImpl constructor
 *  Make an instance of UnionType in symRoot.symTableCurrent.
 *  It is not recommended to use this constructor directly
 *  but use unionType method of HIR because there are
 *  several restrictions in using this constructor.
 *  @param pSymRoot SymRoot instance to be passed to the
 *      resultant type.
 *  @param pTypeName Name of the union type.
 *      It shoul be <UNION tagName> where tagName is the
 *      tag name the union. If it has no tag name, generate it
 *      by generateTag() of SymTable.
 *  @param pElemList List of elements of the union.
 *      If it is not known, then give null and later addElem()
 *      and finishUnionType().
**/
public
UnionTypeImpl( SymRoot pSymRoot, String pTypeName, IrList pElemList )
{
  super(pSymRoot);
  int  lSize;
  Elem lElem;
  fKind = Sym.KIND_TYPE;
  fType = this;
  fTypeKind = Type.KIND_UNION;
  fAlignment = Type.KIND_ALIGNMENT[Type.KIND_UNION]; // Set default.
     // Actual value is set by finishUnionType
  if (pElemList == null) {
    fElemList = symRoot.getHirRoot().hir.irList(null);
    setFlag(Sym.FLAG_INCOMPLETE_TYPE, true);
    fCompleteType = null;
  }else
    fElemList = pElemList;
  fName = pTypeName;
  if (pElemList != null)
    finishUnionType(false);
  else
    setFlag(Sym.FLAG_UNIFORM_SIZE, false);
} // UnionTypeImpl

public SymTable
getSymTable() { return fLocalSymTable; }

public void
setSymTable( SymTable pSymTable ) { fLocalSymTable = pSymTable; }

public IrList
getElemList() {
  return fElemList;
}

public void
addElem( Elem pElem ) {
  int lSize;
  if (fElemList == null)
    fElemList = symRoot.getHirRoot().hir.irList();
  fElemList.add(pElem);
  pElem.setDisplacement(0);
  pElem.setDispExp(symRoot.getHirRoot().hir.constNode(symRoot.intConst0));
  //////////////////// S.Fukuda 2002.10.30 begin
  // Size of type is computed in finishUnionTyp.
  //lSize = pElem.getSymType().getSizeValue();
  //if (fTypeSize < lSize)
  //  fTypeSize = lSize;
  //////////////////// S.Fukuda 2002.10.30 end
} // addElem

public void
finishUnionType( boolean pSeparately )
{
  Elem    lElem;
  long    lDisplacement, lSize, lMaxSize;
  int     lAlignment, lElemAlignment, lAlignmentGap;
  long    lResidue;
  String  lName;
  boolean lIncomplete, lUniformSize, lSizeIsSet;
  Type    lMaxSizeType = null, lElemType;
  lName = symRoot.sym.makeStructUnionTypeName(false, fElemList);
  if (fDbgLevel > 3) //##58
    symRoot.ioRoot.dbgSym.print(4, "finishUnionType", getName());
  //-- Compute length and set displacement for each element.
  lMaxSize = 0;
  lAlignment = 0;
  lAlignmentGap = 0;
  lIncomplete  = false;
  lSizeIsSet   = true;
  lUniformSize = true;
  for (java.util.Iterator lElemIterator = fElemList.iterator();
       lElemIterator.hasNext(); ) {
    lElem = ((Elem)lElemIterator.next());
    if (lElem != null) {
      lElemType = lElem.getSymType();
      lElem.setDisplacement(0);
      lElem.setDispExp(symRoot.getHirRoot().
                       hir.constNode(symRoot.intConst0));
      lSize = lElemType.getSizeValue();
      lElemAlignment = lElemType.getAlignment();
      if (lElemAlignment > lAlignment)
        lAlignment = lElemAlignment;
      if (lElemType.getFlag(Sym.FLAG_INCOMPLETE_TYPE)) {
        lIncomplete = true;
        lUniformSize = false;
      }
      if (! lElemType.getFlag(Sym.FLAG_UNIFORM_SIZE))
        lUniformSize = false;
      if (! lElemType.isSizeEvaluable())
        lSizeIsSet = false;
      if (lSize > lMaxSize) {
        lMaxSize = lSize;
        lMaxSizeType = lElemType;
      }
      if (lElemType.isConst())
        fConst = true;
      if (lElemType.isVolatile())
        fVolatile = true;
      //##64 BEGIN
      if (lElemType.getFlag(Sym.FLAG_UNFIXED_SIZE)) {
        symRoot.ioRoot.msgWarning.put("Union " + getName()
          + " has unfixed size element " + lElem.getName());
        setFlag(Sym.FLAG_UNFIXED_SIZE, true);
      }
      //##64 END
      if (fDbgLevel > 3) //##58
        symRoot.ioRoot.dbgSym.print(4, " elem " + lElem.getName() +
          " size " + lSize +
          " incomp " + lElemType.getFlag(Sym.FLAG_INCOMPLETE_TYPE));
    }
  }
  if (lMaxSizeType != null) {  // Get padding size.
    lResidue = lMaxSize % lAlignment;
    if (lResidue != 0)
      lAlignmentGap = (int)(lAlignment - lResidue);
  }
  lMaxSize = lMaxSize + lAlignmentGap;
  //////////////////// S.Fukuda 2002.10.30 begin
  //fTypeSize = lMaxSize;
  //fSizeIsSet = lSizeIsSet;
  //fTypeSizeExp = symRoot.getHirRoot().hir.intConstNode(lMaxSize);
  setSizeValue(lMaxSize);
  //////////////////// S.Fukuda 2002.10.30 begin
  lName = symRoot.sym.makeStructUnionTypeName(false, fElemList);
  if (pSeparately) {
    if (fOrigin == null)
      fOrigin = ((SymImpl)symRoot.sym).unionType(lName, fElemList, null);
  }else if (fOrigin == null)
    fOrigin = this;
  if (lIncomplete) {
    fCompleteType = null;
    lUniformSize = false;
  }else {
    if (fOrigin != null)
      fCompleteType = fOrigin;
    else
      fCompleteType = this;
  }
  fAlignment = lAlignment;
  setFlag(Sym.FLAG_INCOMPLETE_TYPE, lIncomplete);
  setFlag(Sym.FLAG_UNIFORM_SIZE, lUniformSize);
} // finishUnionType

public int
getAlignment()
{
  return fAlignment;
}

public Sym
getTag()
{
  return fTag;
}

public void
setTag( Sym pTag )
{
  fTag = pTag;
}

public boolean
isCompatibleWith( Type pType )
{
  if (pType == this)
    return true;
  else if ((pType == null)||(!(pType instanceof UnionType)))
    return false;
  else {
    if ((pType.isConst() == isConst())&&
        (pType.isVolatile() == isVolatile())) {
      if (getOrigin() != null) {
        if ((getOrigin() == this)||
            (((UnionType)pType).getOrigin() == this))
          return true;
        if (getFinalOrigin().isCompatibleWith(
                ((UnionType) pType).getFinalOrigin()))
          return true;
      }
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


public void
setAlignment( int pAlignment )
{
  fAlignment = pAlignment;
}


public String
toStringDetail() {
  String symString = super.toStringDetail();
  if (fTag != null)
    symString = symString + " tag " + fTag.getName();
  else
    symString = symString + " tag null";
  return symString;
} // toStringDetail

} // UnionTypeImpl
