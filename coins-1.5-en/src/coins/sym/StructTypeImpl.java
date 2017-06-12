/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;

import coins.SymRoot;
import coins.ir.IrList;
import coins.ir.hir.Exp;
import coins.ir.hir.HIR;

/** StructTypeImpl Class
**/
public class
StructTypeImpl extends TypeImpl implements StructType
{

  protected IrList
    fElemList = null;

  protected SymTable
    fLocalSymTable;

  protected Sym
    fTag;       // Tag name

  protected int
    fAlignment; // Alignment of structure

/** StructTypeImpl constructor
 *  Make an instance of StructType in symRoot.symTableCurrent.
 *  It is not recommended to use this constructor directly
 *  but use structType method of HIR because there are
 *  several restrictions in using this constructor.
 * The alignment of a struct type is the same to that of its element
 * having the largest alignment among the elements in the structure.
 * Elements of the struct type is aligned according to the alignment
 * of element type. This may cause to place gap between preceeding element.
 * The size of the struct type is multiple of the alignment value of
 * the struct type, thus, there may be a gap at the tail of the struct type
 * if the last element has small alignment value.
 *  @param pSymRoot SymRoot instance to be passed to the
 *      resultant type.
 *  @param pTypeName Name of the structure type.
 *      It shoul be <STRUCT tagName> where tagName is the
 *      tag name the structure. If it has no tag name, generate it
 *      by generateTag() of SymTable.
 *  @param pElemList List of elements of the structure.
 *      If it is not known, then give null and later addElem()
 *      and finishStructType().
**/
public
StructTypeImpl( SymRoot pSymRoot, String pTypeName, IrList pElemList )
{
  super(pSymRoot);
  SymTable lSymTable;
  Elem     lElem;
  int      lDisplacement, lSize;
  fKind = Sym.KIND_TYPE;
  fType = this;
  fTypeKind = Type.KIND_STRUCT;
  fAlignment = Type.KIND_ALIGNMENT[Type.KIND_STRUCT]; // Set default.
     // Actual value is set by finishStructType
  if (pElemList == null) {
    fElemList = symRoot.getHirRoot().hir.irList(null);
    setFlag(Sym.FLAG_INCOMPLETE_TYPE, true);
    fCompleteType = null;
  }else {
    fElemList = pElemList;
  }
  fName = pTypeName;
  if (pElemList != null)
    finishStructType(false);
  else
    setFlag(Sym.FLAG_UNIFORM_SIZE, false);
} // StructTypeImpl

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
  if (fElemList == null)
    fElemList = symRoot.getHirRoot().hir.irList();
  fElemList.add(pElem);
} // addElem

public void
finishStructType( boolean pSeparately )
{
  Elem    lElem;
  long    lDisplacement, // Byte offset (number of bytes from struct origin)
                         // of each element.
          lSize,         // Cummulative size (bytes) of the current structure.
          lElemSize;     // Number of elements for each element.
  long    lBitOffsetFromStructOrg, // Bit position within the structure.
          lCumulativeSizeInBits;   // Number of bits in the structure.
          // lBitOffsetFromStructOrg = lDisplacement*8 + lBitOffset
          // lCumulativeSizeInBits = lBitOffsetFromStructOrg + lBitFieldSize
          // lBitFieldSumWithinWOrd = lCumulativeSizeInBits - lDisplacement*8
  int     lBitFieldSize, // Number of bits in a bit field.
          lBitOffset,
          lBitFieldSumWithinWord, lNewBitfieldSumWithinWord,
          lDispIncrementValue = 0;
  int     lAlignment, lElemAlignment, lAlignmentGap;
  long    lResidue;
  String  lName;
  boolean lIncomplete, lUniformSize, lSizeIsSet, lBitFieldBegin;
  Type    lElemType = null;
  lName = symRoot.sym.makeStructUnionTypeName(true, fElemList);
  //-- Compute displacement for each element.
  if (fDbgLevel > 3) //##58
    symRoot.ioRoot.dbgSym.print(4, "finishStructType", getName() +
    " packedFromLeft " + machineParam.isPackedFromLeft());
  lSize = 0;
  lDisplacement = 0;
  lAlignment = 0;
  lElemAlignment = 1;
  lIncomplete  = false;
  lUniformSize = true;
  lSizeIsSet   = true;
  if (fElemList.isEmpty()) {
    lIncomplete = true;
    lUniformSize = false;
  }
  //-- Items for bit field treatment.
  lBitFieldBegin = false;
  lBitOffsetFromStructOrg = 0;
  lCumulativeSizeInBits   = 0;
  lBitFieldSumWithinWord  = 0;
  boolean lPackedFromRight = machineParam.isPackedFromRight();
  int lCharSize  = machineParam.evaluateSize(Type.KIND_CHAR);
  int lShortSize = machineParam.evaluateSize(Type.KIND_SHORT);
  int lIntSize   = machineParam.evaluateSize(Type.KIND_INT);
  int lLongSize  = machineParam.evaluateSize(Type.KIND_LONG);
  int lCharBit  = lCharSize * 8;
  int lShortBit = lShortSize * 8;
  int lIntBit   = lIntSize * 8;
  int lLongBit  = lLongSize * 8;
  int lMinimumByteForBitFieldSequence =
    //##87 machineParam.minimumByteForBitFieldSequence();
    machineParam.minimumNumberOfAddressingUnitsForBitFieldSequence(); //##87
  Type lAlignmentType;
  int lElemTypeBit;          // lElemType.getSizeValue() * 8
  long lContainingObjectDisplacement = 0;

  //-- Process each element.
  for (java.util.Iterator lElemIterator = fElemList.iterator();
       lElemIterator.hasNext(); ) {
    lElem = ((Elem)lElemIterator.next());
    if (lElem != null) {
      lElemType = lElem.getSymType();
      lElemSize = lElemType.getSizeValue();
      //##53 BEGIN
      if (lElem.isBitField()) {
        //---- Bit field element ----//
        // As for the bit field allocation, see the explanation of
        // setBitFieldOffset method in Elem interface.
        lBitFieldSize = lElem.getBitSize();
        lElemTypeBit = (int)lElemSize * 8;
        // Set lAlignmentType as the type of bit field.
        if (lElemAlignment <= (int)lElemType.getSizeValue()) { //##55
          lAlignmentType = lElemType;  //##55
          lElemAlignment = (int)lAlignmentType.getSizeValue();
        } //##55
        if (! lBitFieldBegin) { // First bit field.
          lBitFieldBegin = true;
          int lBitOffsetOfDisplacement =
              (int)(lSize % lElemSize) * 8;
          lBitOffset = lBitOffsetOfDisplacement;
          lBitOffsetFromStructOrg = lSize * 8;
          lCumulativeSizeInBits = lBitOffsetFromStructOrg;
          lContainingObjectDisplacement =
            (lSize / lElemSize) * lElemSize;
          lDisplacement = lContainingObjectDisplacement;
        } // End of first bit field
        lBitFieldSumWithinWord =
          (int)(lCumulativeSizeInBits - lContainingObjectDisplacement*8);
        lNewBitfieldSumWithinWord = lBitFieldSumWithinWord + lBitFieldSize;
        if (lNewBitfieldSumWithinWord > lElemTypeBit) {
          // If this field can not be contained in lElemType word, stop to
          // use this elem type word and advance to the next elem type word.
          lSize = lContainingObjectDisplacement
                  + (lBitFieldSumWithinWord + 7) / 8;
          lDisplacement = lSize + lElemType.getAlignmentGap(lSize);
          lContainingObjectDisplacement = lDisplacement;
          lSize = lDisplacement;
          lBitFieldSumWithinWord = 0;
        }
        lBitOffset = lBitFieldSumWithinWord;
        lBitFieldSumWithinWord = lBitFieldSumWithinWord + lBitFieldSize;
        lBitOffsetFromStructOrg
          = lContainingObjectDisplacement * 8 + lBitOffset;
        lCumulativeSizeInBits
          = lContainingObjectDisplacement * 8 + lBitFieldSumWithinWord;
        lElem.setBitFieldOffset(lBitOffset);
        lElem.setDisplacement(lContainingObjectDisplacement);
        if (fDbgLevel > 3) //##58
          symRoot.ioRoot.dbgSym.print(4, "\n   bitSize " + lElem.getBitSize() +
           " bitOffset " + lElem.getBitOffset() +
           " disp " + lElem.evaluateDisp() + " size " + lSize
           + " bitfieldSum " + lBitFieldSumWithinWord
           + " offsetFromOrg " + lBitOffsetFromStructOrg
           + " cummulativeSize " + lCumulativeSizeInBits);
         //---- End of bit field elements ----//
         //##53 END
      }else { // Not a bit field.
        if (lBitFieldBegin) { // Previous fields are bit field.
          lSize = lContainingObjectDisplacement
                  + (lBitFieldSumWithinWord+7) / 8;
          lBitFieldSumWithinWord = 0;
        }
        lBitFieldBegin = false;
        lDisplacement = lSize + lElemType.getAlignmentGap(lSize);
        lContainingObjectDisplacement = lDisplacement;
        lElem.setDisplacement(lDisplacement);
        //## lElem.setDispExp( ... );   // REFINE
        lSize = lDisplacement + lElemSize;
        lElemAlignment = lElemType.getAlignment();
        if (lElemType.getFlag(Sym.FLAG_INCOMPLETE_TYPE)) {
          lIncomplete = true;
          lUniformSize = false;
        }
        if (! lElemType.getFlag(Sym.FLAG_UNIFORM_SIZE))
          lUniformSize = false;
        if (! lElemType.isSizeEvaluable())
          lSizeIsSet = false;
      }
      if (lElemType.isConst())
        fConst = true;
      if (lElemType.isVolatile())
        fVolatile = true;
      if (lElemAlignment > lAlignment)
        lAlignment = lElemAlignment;
        //##64 BEGIN
        if (lElemType.getFlag(Sym.FLAG_UNFIXED_SIZE)) {
          symRoot.ioRoot.msgWarning.put("Struct " + getName()
            + " has unfixed size element " + lElem.getName());
          setFlag(Sym.FLAG_UNFIXED_SIZE, true);
        }
        //##64 END
      if (fDbgLevel > 3) //##58
        symRoot.ioRoot.dbgSym.print(4, " elem " + lElem.getName(),
          " size " + lSize + " disp " + lElem.evaluateDisp() +
          " incomp " + lElemType.getFlag(Sym.FLAG_INCOMPLETE_TYPE));
    }
  }
  if (lBitFieldBegin) { // Bit field sequence continued.
    lSize = lContainingObjectDisplacement
            + (lBitFieldSumWithinWord+7) / 8;
  }
  lAlignmentGap = 0;
  if (lElemType != null) {  // Add padding size.
    if (lAlignment <= 0) //##79
      lResidue = 0; //##79
    else  //##79
      lResidue = lSize % lAlignment;
    if (lResidue != 0)
      lAlignmentGap = (int)(lAlignment - lResidue);
    lSize = lSize + lAlignmentGap;
  }
  //////////////////// S.Fukuda 2002.10.30 begin
  //fTypeSize = lSize;
  if (fDbgLevel > 3) //##58
    symRoot.ioRoot.dbgSym.print(4, " total " + lSize + " align " + lAlignment);
  //if (lSizeIsSet) {
  ////## fTypeSizeExp = computeStructSizeExp(fElemList);
  //  setSizeValue(fTypeSize); // Set sizeExp, too.
  //}
  setSizeValue(lSize);
  //////////////////// S.Fukuda 2002.10.30 ed
  if (pSeparately) {
    if (fOrigin == null)
      fOrigin = ((SymImpl)symRoot.sym).structType(lName, fElemList, null);
  }else if (fOrigin == null)
    fOrigin = this;
/**
  if (fTag == null) {
    fTag = symRoot.symTableCurrent.generateTag();
    fTag.setSymType(this);
  }
**/
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
} // finishStructType

Exp
computeStructSizeExp( IrList pElemList ) { // DELETE
  //## Should consider alignment !       // REFINE
  Exp  lSizeExp = null, lElemSizeExp;
  Elem lElem;
  for (java.util.Iterator lElemIterator = pElemList.iterator();
       lElemIterator.hasNext(); ) {
    lElem = (Elem)(lElemIterator.next());
    if (lElem != null) {
      lElemSizeExp = lElem.getSymType().getSizeExp();
      if (fDbgLevel > 3) //##58
        symRoot.ioRoot.dbgSym.printObject(5, lElem.toStringShort(),
         lElemSizeExp);
      if (lSizeExp == null)
        lSizeExp = lElemSizeExp;
      else {
        if (lElemSizeExp != null)
          lSizeExp = symRoot.getHirRoot().hir.exp(HIR.OP_ADD, lSizeExp,
                           lElemSizeExp);
      }
    }
  }
  return lSizeExp;
} // computeStructSizeExp

Exp
addStructSizeExp( Elem pElem ) {  // DELETE
  //## Should consider alignment !       // REFINE
  //Exp  lSizeExp = fTypeSizeExp; // S.Fukuda 2002.10.30
  Exp  lSizeExp = getSizeExp(); // S.Fukuda 2002.10.30
  if (lSizeExp == null)
    lSizeExp = pElem.getSymType().getSizeExp();
  else {
    lSizeExp = symRoot.getHirRoot().hir.exp(HIR.OP_ADD, lSizeExp,
                       pElem.getSymType().getSizeExp());
  }
  return lSizeExp;
} // addStructSizeExp

public int
getAlignment()
{
  return fAlignment;
}

public void
setAlignment( int pAlignment )
{
  fAlignment = pAlignment;
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
  else if ((pType == null)||(!(pType instanceof StructType)))
    return false;
  else {
    if ((pType.isConst() == isConst())&&
        (pType.isVolatile() == isVolatile())) {
      if (getOrigin() != null) {
        if ((getOrigin() == this)||
            (((StructType)pType).getOrigin() == this))
          return true;
        if (getFinalOrigin().isCompatibleWith(
              ((StructType) pType).getFinalOrigin()))
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
    if (getOrigin() == this) //##79
      return this;  //##79
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
  if (fTag != null)
    symString = symString + " tag " + fTag.getName();
  else
    symString = symString + " tag null";
  return symString;
} // toStringDetail

} // StructTypeImpl
