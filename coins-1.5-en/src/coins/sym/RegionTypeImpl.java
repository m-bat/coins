/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;

import java.util.Iterator;

import coins.MachineParam;
import coins.SymRoot;
import coins.ir.IrList;

/** RegionTypeImpl Class
**/
public class
RegionTypeImpl extends TypeImpl implements RegionType
{

  protected IrList
    fSubpList = symRoot.getHirRoot().hir.irList();

  protected IrList
    fSymTableList = symRoot.getHirRoot().hir.irList();

  protected IrList
    fListOfElemList = symRoot.getHirRoot().hir.irList();

  protected IrList
    fCurrentElemList;

  protected SymTable
    fCurrentSymTable;

  protected Var
    fRegionVar;

  protected String  // Region name is used to construct type name
    fRegionName;    // ("<REGION " + fRegionName + ">").intern()

  protected Sym
    fTag;       // Tag name

  protected int
    fStorageClass;

/** RegionTypeImpl constructor
 *  Make an instance of RegionType in symRoot.symTableRoot.
 *  It is not recommended to use this constructor directly
 *  but use regionType method of Sym because there are
 *  several restrictions in using this constructor.
 *  List of elements should be given by calling addElem
 *  method repeatedly and then call finishRegionType().
 *  @param pSymRoot SymRoot instance to be passed to the
 *      resultant type.
 *  @param pRegionName Name of the region type.
 *      It shoul be <REGION regionName>.
 *      If it has no region name, generate it
 *      by generateTag() of SymTable.
 *  @param pStorageClass give
 *      VAR_STATIC or VAR_AUTO of Var interface.
**/
public
RegionTypeImpl( SymRoot pSymRoot, String pRegionName, int pStorageClass )
{
  super(pSymRoot);
  int  lSize;
  Sym  lNonSubp;
  Sym  lTag;
  Elem lElem;
  fKind = Sym.KIND_TYPE;
  fType = this;
  fTypeKind = Type.KIND_REGION;
  String lTypeName = ("<REGION " + pRegionName + ">").intern();
  // Prepare for region defined outside subprogram.
  fTag = symRoot.symTableRoot.generateTag();
  fTag.setSymType((Type)this);
  if (symRoot.symTableCurrentSubp != null)
    fSubpList.add(symRoot.symTableCurrentSubp);
  fSymTableList.add(symRoot.symTableRoot);
  setFlag(Sym.FLAG_INCOMPLETE_TYPE, true);
  fCompleteType = null;
  fName = lTypeName;
  fRegionName = pRegionName;
  setFlag(Sym.FLAG_UNIFORM_SIZE, false);
  fStorageClass = pStorageClass;
} // RegionTypeImpl


public int
addSubp( Subp pSubp, SymTable pRegionSymTable )
{
  int lIndex;
  if (pSubp == null) {
    // Region defined outside subprogram.
    // Use predefined position 0.
    lIndex = 0;
  }else {
    fSubpList.add(pSubp);
    lIndex = fSubpList.indexOf(pSubp);
  }
  fSymTableList.add(lIndex, pRegionSymTable);
  fCurrentSymTable = pRegionSymTable;
  fCurrentElemList = symRoot.getHirRoot().hir.irList(null);
  fListOfElemList.add(fCurrentElemList);
  return lIndex;
} // addSubp

public SymTable
getCurrentRegionSymTable()
{
  return fCurrentSymTable;
}

public SymTable
getRegionSymTable( Subp pSubp )
{
  int lIndex;
  if (pSubp == null)
    return (SymTable)fSymTableList.get(0);
  lIndex = fSubpList.indexOf(pSubp);
  if (lIndex >= 0)
    return (SymTable)fSymTableList.get(lIndex);
  else
    return null;
} // getRegionSymTable

public SymTable
getRegionSymTable( int pIndex )
{
  return (SymTable)fSymTableList.get(pIndex);
} // getRegionSymTable

public IrList
getSubpList()
{
  return fSubpList;
}

public void
addElemToCurrentRegion( Elem pElem ) {
  int lSize;
  fCurrentElemList.add(pElem);
  pElem.setDisplacement(0);
  pElem.setDispExp(symRoot.getHirRoot().hir.constNode(symRoot.intConst0));
  pElem.setFlag(Sym.FLAG_REGION_ELEM, true);
  pElem.setStorageClass(fStorageClass);
} // addElemToCurrentRegion

public IrList
getRegionElemList( Subp pSubp )
{
  int lIndex = fSubpList.indexOf(pSubp);
  if (lIndex >= 0)
    return (IrList)fListOfElemList.get(lIndex);
  else
    return null;
} // getRegionElemList

public void
finishCurrentRegion( )
{
  Elem    lElem;
  long    lDisplacement, lSize, lElemSize;
  int     lAlignment, lElemAlignment, lAlignmentGap;
  long    lResidue;
  String  lName;
  boolean lIncomplete, lUniformSize, lSizeIsSet, lBitFieldBegin;
  Type    lElemType = null;
  //-- Compute displacement for each element.
  if (fDbgLevel > 3) //##58
    symRoot.ioRoot.dbgSym.print(4, "finishCurrentRegion", getName());
  lSize = 0;
  lDisplacement = 0;
  lAlignment = machineParam.getLargestAlignment(); //##51
  lElemAlignment = 1;
  lSizeIsSet   = true;
  for (java.util.Iterator lElemIterator = fCurrentElemList.iterator();
       lElemIterator.hasNext(); ) {
    lElem = ((Elem)lElemIterator.next());
    if (lElem != null) {
      lElemType = lElem.getSymType();
      lElemSize = lElemType.getSizeValue();
        lDisplacement = lSize + lElemType.getAlignmentGap(lSize);
        lElem.setDisplacement(lDisplacement);
        lSize = lDisplacement + lElemSize;
        lElemAlignment = lElemType.getAlignment();
        if (! lElemType.isSizeEvaluable())
          lSizeIsSet = false;
      if (fDbgLevel > 3) //##58
        symRoot.ioRoot.dbgSym.print(4, " elem " + lElem.getName(),
          " size " + lSize + " disp " + lDisplacement );
    }
  }
  lAlignmentGap = 0;
  if (lElemType != null) {  // Add padding size.
    lResidue = lSize % lAlignment;
    if (lResidue != 0)
      lAlignmentGap = (int)(lAlignment - lResidue);
    lSize = lSize + lAlignmentGap;
  }
  if (fDbgLevel > 3) //##58
    symRoot.ioRoot.dbgSym.print(4, " total " + lSize);
  if (getSizeValue() < lSize)
    setSizeValue(lSize);
  fRegionVar = ((SymImpl)symRoot.sym).defineRegionVar(fRegionName, this);
// lRegionVar is recorded in lType in defineRegionVar.

  fOrigin = this;
  fCompleteType = this;
  fRegionVar.setStorageClass(fStorageClass);
} // finishCurrentRegion

public Var
getRegionVar()
{
  return fRegionVar;
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

public String
toStringDetail() {
  String symString = super.toStringDetail();
  String lSubpName;
  for (Iterator lIterator = fSubpList.iterator();
       lIterator.hasNext(); ) {
    lSubpName = lIterator.next().toString();
    symString = symString + " " + lSubpName;
  }
  return symString;
} // toStringDetail

} // RegionTypeImpl
