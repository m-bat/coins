/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;

import coins.SymRoot;
import coins.ir.hir.Exp;
import coins.ir.hir.HIR;
import coins.ir.hir.ConstNode;

public class
VectorTypeImpl extends TypeImpl implements VectorType
{

  Type fElemType     = null;
  long fElemCount    = 0;    // 0 means that the element count is
                             // not yet fixed.
  Exp  fElemCountExp = null;
  long fLowerBound   = 0;
  Exp  fLowerBoundExp= null;
  int  fDimension    = 0;
  int  fAlignment; // Default value is set equal to that of element type.

public
VectorTypeImpl( SymRoot pSymRoot, String pTypeName, Type pElemType, long pElemCount )
{
  super(pSymRoot);
  setVectorTypeFields(pTypeName, pElemType, pElemCount, null, 0, null);
} // VectorTypeImpl

/** VectorTypeImpl
 *  General constructor of VectorTypeImpl.
 *  If pTypeName is null, the type name is generated.
 *  If pElemCountExp is evaluable then element count is decided by pElemCountExp
 *  else the element count is decided by pElemCount.
 *  If pLowerBoundExp is evaluable then lower bound is decided by pLowerBoundExp
 *  else the lower bound is decided by pLowerBound.
**/
public
VectorTypeImpl( SymRoot pSymRoot, String pTypeName, Type pElemType, long pElemCount, Exp pElemCountExp, long pLowerBound, Exp pLowerBoundExp )
{
  super(pSymRoot);
  setVectorTypeFields(pTypeName, pElemType, pElemCount, pElemCountExp,
      pLowerBound, pLowerBoundExp );
} // VectorTypeImpl

public
VectorTypeImpl( SymRoot pSymRoot, Type pElemType, long pElemCount )
{
  super(pSymRoot);
  setVectorTypeFields(null, pElemType, pElemCount, null, 0, null);
} // VectorTypeImpl

protected void
setVectorTypeFields( String pTypeName, Type pElemType, long pElemCount,
          Exp pElemCountExp, long pLowerBound, Exp pLowerBoundExp )
{
  Exp lSizeExp, lElemSizeExp;
  long lSize = 0;
  fKind = Sym.KIND_TYPE;
  fType = (Type)this;
  fTypeKind = Type.KIND_VECTOR;
  fElemType = pElemType;
  //##64
  if (pElemType.getFlag(Sym.FLAG_UNFIXED_SIZE)||
      ((pElemType instanceof VectorType)&&
      (((VectorType)pElemType).getElemCount() <= 0)&&
        (((VectorType)pElemType).getElemCountExp() == null))) {
      symRoot.ioRoot.msgWarning.put(
         "Element type of VectorType is unfixed length type "+ pTypeName);
  }
  //##64
  if (pElemCountExp != null) {
    fElemCountExp = pElemCountExp;
    if (pElemCountExp.isEvaluable())
      fElemCount = pElemCountExp.evaluateAsInt();
  }else { // pElemCountExp == null.
    fElemCount = pElemCount;
    if (pElemCount > 0)
      fElemCountExp = symRoot.getHirRoot().hir.intConstNode(pElemCount);
    else
      fElemCountExp = symRoot.getHirRoot().hir.intConstNode(0);
  }
  if (pElemCount > 0)
    setFlag(Sym.FLAG_UNIFORM_SIZE,
            pElemType.getFlag(Sym.FLAG_UNIFORM_SIZE));
  else
    setFlag(Sym.FLAG_UNIFORM_SIZE, false);
  if (pLowerBoundExp != null) {
    fLowerBoundExp = pLowerBoundExp;
    if (pLowerBoundExp.isEvaluable()) {
      fLowerBound = pLowerBoundExp.evaluateAsInt();
    }
  }else { // pLowerBoundExp == null.
    fLowerBound    = pLowerBound;
    fLowerBoundExp = symRoot.getHirRoot().hir.intConstNode(pLowerBound);
  }
  if (pElemType != null)
    fDimension = pElemType.getDimension() + 1;
  else
    fDimension = 1;
  if (pTypeName != null)
    fName = pTypeName;
  else {  // Type name is not given.
    fName = makeVectorTypeName(pElemType, fElemCountExp, fElemCount,
                               fLowerBoundExp, fLowerBound);
  }
  if (fElemCountExp != null) { //###
    if ((! fElemCountExp.isEvaluable())||
        (pElemType.getSizeValue() <= 0)) {
      setSizeExp( symRoot.getHirRoot().hir.exp(
                  HIR.OP_MULT,
                  (Exp)pElemType.getSizeExp().copyWithOperands(),
                  (Exp)fElemCountExp.copyWithOperands() ) );
    }else {// if( pElemType.getSizeValue()>=0 ) type size of pElemType is constant.
      setSizeValue( pElemType.getSizeValue()*fElemCount );
    }
  } else { // fElemCountExp is null.
    if( pElemType.getSizeExp()!=null ) { // type size of pElemType is not constant.
      setSizeExp( symRoot.getHirRoot().hir.exp(
                    HIR.OP_MULT,
                    (Exp)pElemType.getSizeExp().copyWithOperands(),
                    symRoot.getHirRoot().hir.offsetConstNode(pElemCount) ) );
    }else   // pElemType does not have type size info.
      setSizeValue( pElemType.getSizeValue()*fElemCount );
  }
  if (fElemCount <= 0) {
    setFlag(Sym.FLAG_INCOMPLETE_TYPE, true);
    setFlag(Sym.FLAG_UNIFORM_SIZE, false);
    if (fElemCountExp == null)  //##64
      setFlag(Sym.FLAG_UNFIXED_SIZE, true); //##64
  }
  if (pElemType.getFlag(Sym.FLAG_UNFIXED_SIZE)) //##64
    setFlag(Sym.FLAG_UNFIXED_SIZE, true); //##64
  //////////////////// S.Fukuda 2002.10.30 end
// If array type has a type qualifier, the type qualifier qualifies
//     element type, not the array type. (JIS 6.5.3, p65)
  fAlignment = pElemType.getAlignment();
  if (fDbgLevel > 2) //##58
    symRoot.ioRoot.dbgSym.print(3, "\n VectorType " + fName + " size " +
      getSizeValue() + " " + " count " + fElemCount + " LB " + fLowerBound);
  if (getSizeExp() != null)
    if (fDbgLevel > 2) //##58
      symRoot.ioRoot.dbgSym.print(3, " sizeExp "
        + getSizeExp().toStringWithChildren());
} // setVectorTypeFields

public int
getDimension() { return fDimension; }

public Type
getElemType() { return fElemType; }

public long
getElemCount() { return fElemCount; }

public Exp
getElemCountExp() { return fElemCountExp; }

public long
getLowerBound() { return fLowerBound; }

public Exp
getLowerBoundExp() { return fLowerBoundExp; }

    /** isCountEvaluable
     *  "this" should be an array type symbol.
     *  @return boolean true if element count is evaluable
     *      as integer value, false otherwise.
     **/
/*
public boolean  //  DELETED
isCountEvaluable() {
  if (fDbgLevel > 0)
    symRoot.ioRoot.msgWarning.put(" Do not use isCountEvaluable. Use exp.isEvaluable().");
  if (fElemCountExp != null)
    return fElemCountExp.isEvaluable( );
  else
    return false;
}

public boolean  //  DELETED
isLowerBoundEvaluable() {
  if (fDbgLevel > 0)
    symRoot.ioRoot.msgWarning.put(" Do not use isLowerBoundEvaluable. Use exp.isEvaluable().");
  if (fLowerBoundExp != null)
    return fLowerBoundExp.isEvaluable( );
  else
    return false;
}
*/

    /** evaluateElemCount
     *  Get the characteristics of "this" array type.
     *  "this" should be an array type symbol.
     *  evaluateElemCount: returns element count as integer value.
     *      If it can not be evaluated, return 0.
     **/
/*
 public int
*/

/* ##38
public long
evaluateElemCountExp() {
  if( isCountEvaluable( ) )
    fElemCount = fElemCountExp.evaluateAsInt( );
  return fElemCount;
}

public long
evaluateLowerBound() {
  if( isCountEvaluable( ) )
    fLowerBound = fLowerBoundExp.evaluateAsInt( );
  return fLowerBound;
}
*/

public boolean
isCompatibleWith( Type pType )
{
  Type lElemType;
  if (pType == this)
    return true;
  else {
    if (pType.getTypeKind() == Type.KIND_VECTOR) {
      lElemType = ((VectorType)pType).getElemType();
      if ((lElemType.isCompatibleWith(fElemType))&&
          (pType.isConst() == isConst())&&
          (pType.isVolatile() == isVolatile())) {
        if (pType.getFlag(Sym.FLAG_INCOMPLETE_TYPE)) {
          return true;  //## Confirm
        }else {
          if (((VectorType)pType).getElemCount() == fElemCount)
            return true;
          //else if (((VectorType)pType).getElemCountExp() == fElemCountExp) return true;
        }
      }
    }
  }
  return false;
} // isCompatibleWith

public boolean
isRectangularArray()
{
  return getFlag(Sym.FLAG_UNIFORM_SIZE);
} // isRectangularArray

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

public String
toString()
{
  String lSymString = super.toStringShort();
  if (getSizeValue() > 0)
    lSymString = lSymString + " size " + getSizeValue();
  else if (getSizeExp() != null) {
    lSymString = lSymString + " sizeExp " + getSizeExp().toStringWithChildren();
  }
  lSymString = lSymString + " elemCount "; //##64
  if (fElemCount > 0)
    lSymString = lSymString + getElemCount(); //##64
  else if (fElemCountExp != null)
    lSymString = lSymString + ((SymImpl)this).makeExpString(getElemCountExp()); //##64
  else // fElemCount <=0 and fElemCountExp == null.  //##64
    lSymString = lSymString + "unfixed"; //##64
  if ((fLowerBoundExp != null)&&(! (fLowerBoundExp instanceof ConstNode)))
    lSymString = lSymString + " lowerBound " +
      ((SymImpl)this).makeExpString(getLowerBoundExp());
  else
    lSymString = lSymString + " lowerBound " + getLowerBound();
  return lSymString;
} // toString

public String
toStringDetail()
{
  String lSymString = getSymKindName() + " " + super.toStringShort();
  if (! fFlagBox.allFalse())
    lSymString = lSymString + fFlagBox.toString(); //##64
  if (getSizeValue() > 0)
    lSymString = lSymString + " size " + getSizeValue();
  else if (getSizeExp() != null) {
    lSymString = lSymString + " sizeExp " + getSizeExp().toStringWithChildren();
  }
  if (getFlag(Sym.FLAG_UNFIXED_SIZE))
    lSymString = lSymString + " unfixedSize"; //##64
  if (fElemCount > 0)
    lSymString = lSymString + " elemCount " + getElemCount();
  else if (fElemCountExp != null)
    lSymString = lSymString + " elemCount " + getElemCountExp().toStringWithChildren();
  if ((fLowerBoundExp != null)&&(! (fLowerBoundExp instanceof ConstNode)))
    lSymString = lSymString + " lowerBound " + getLowerBoundExp().toStringWithChildren();
  else
    lSymString = lSymString + " lowerBound " + getLowerBound();
//  if ((getSizeExp() != null)&&(! (getSizeExp() instanceof ConstNode)))
//    lSymString = lSymString + " sizeExp " + getSizeExp().toStringWithChildren();
  return lSymString;
} // toStringDetail

} // VectororType class

