/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;

import coins.SymRoot;
import coins.ir.IrList;
import coins.ir.hir.Exp;

/** Var Class represents Variable symbols.
 *   Sym --- Symbol class (super class of all symbol classes).
 *     |     Symbols in the same scope are contained in the same
 *     |     symbol table (instance of SymTable).
 *     |
 *     |- Var   --- Variable that can be assigned a value
 *     |    |       and variable with const attribute.
 *     |    |- Param --- Formal parameter class.
 *     |    |- Elem  --- Class for structure element,		<---------- This!
 *     |    |            union element, etc.
 *     |    |- Field --- Class for field in a class.
 */

/** class Elem */

public class ElemImpl extends VarImpl implements Elem
{
    private long fDisplacement = 0;// Displacement (in byte)
    private Exp fDispExp = null; //  Expression representing the displacement.
    private boolean fIsBitField; // true if this is a bit field.
    private int     fBitSize;    // Size of bit field if fIsBitField is true.
    private int     fBitOffset;  // Offset from the least significant bit
                    // of register or object (word, byte) containing
                    // this field.
    private boolean fDispIsSet = false; // true if fDisplacement is set.

public
ElemImpl( SymRoot pSymRoot )
{
  super(pSymRoot);
}

public
ElemImpl( SymRoot pSymRoot, String pElemName, Sym pDefinedIn )
{
  super(pSymRoot);
  fName = pElemName;
  fDefinedIn = pDefinedIn;
  fKind = Sym.KIND_ELEM;
}

public
ElemImpl( SymRoot pSymRoot, String pElemName, Sym pDefinedIn, Exp pDispExp )

{
  super(pSymRoot);
  fName = pElemName;
  fDefinedIn = pDefinedIn;
  fDispExp = pDispExp;
  if ((pDispExp != null)&&
      (pDispExp.isEvaluable() == true)) {
    fDisplacement = pDispExp.evaluateAsInt();
    fDispIsSet    = true;
  }
  fKind = Sym.KIND_ELEM;
  fIsBitField = false;
  fBitOffset = 0;
}
/** isDispEvaluable
 *  Get the displacement of elements.
 *  "this" should be an element of struct/union type symbol.
 *  @return true if the displacement of this element
 *      is evaluable as integer value, false otherwise.
**/
public boolean isDispEvaluable() {
  if (fDispIsSet)
    return true;
  if ((fDispExp != null)&&
      (fDispExp.isEvaluable()))
    return true;
  else
    return false;
}

/** evaluateDisp
 *  "this" should be an element of struct/union type symbol.
 *  @return integer value representing the displacement
**/
public long evaluateDisp()
{
  if (fDispIsSet)
    return fDisplacement;
  if (fDispExp != null)  {
    fDisplacement = fDispExp.evaluateAsInt();
    fDispIsSet    = true;
    return fDisplacement;
  }else {
    symRoot.ioRoot.msgRecovered.put(1014, "Displacement is not evaluable "
                             + getName());
    return 0;
  }
} // evaluateDisp

public void
setDispExp( Exp pDispExp ) {
  fDispExp = pDispExp;
}

public void
setDisplacement( long pDisplacement ) {
  fDisplacement = pDisplacement;
  fDispIsSet    = true;
}

public void
setBitFieldSize( int pBitSize )
{
  fBitSize = pBitSize;
  fIsBitField = true;
}

public void
setBitFieldOffset( int pBitOffset )
{
  fBitOffset = pBitOffset;
}

public void
setBitField( int pBitSize, int pBitOffset ) // deprecated
{
  fBitSize    = pBitSize;
  fBitOffset  = pBitOffset;
  fIsBitField = true;
}

public int
getBitSize()
{
  return fBitSize;
}

public int
getBitOffset()
{
  return fBitOffset;
}

public boolean
isBitField()
{
  return fIsBitField;
}

public Elem
searchElem( Var pVar ) {
  return searchElem(pVar.getSymType());
}

public Elem
searchElem( Type pRecordedIn )
{
  IrList lElemList = null;
  Elem   lElem = null;
  String lThisName = getName( );
  if (pRecordedIn instanceof StructType)
    lElemList = ((StructType)pRecordedIn).getElemList();
  else if (pRecordedIn instanceof UnionType)
    lElemList = ((UnionType)pRecordedIn).getElemList();
  if (lElemList != null) {
    for (java.util.ListIterator lIterator = lElemList.iterator();
         lIterator.hasNext(); ) {
      lElem = (Elem)lIterator.next();
      if( lThisName == lElem.getName( ) ){
        return lElem;
      }
    }
  }
  return null;
} // searchElem

public Type
getUpperType()
{
  Sym  lOwner;
  if (fRecordedIn != null) {
    lOwner = fRecordedIn.getOwner();
    if ((lOwner.getSymKind() == Sym.KIND_TYPE)&&
        ((lOwner instanceof StructType)||
         (lOwner instanceof UnionType)))
      return (Type)lOwner;
    else
      return null;
  }else
    return null;
} // getUpperType

public String
toString()
{
  String lString = super.toString();
  if (fDispIsSet)
    lString = lString + " disp " + fDisplacement;
  if (fIsBitField)
    lString = lString + " bitSize " + fBitSize + " bitOffset " + fBitOffset;
  return lString;
} // toString

} // ElemImpl class
