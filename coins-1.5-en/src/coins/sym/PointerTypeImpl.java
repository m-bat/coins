/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;

import coins.MachineParam;
import coins.SymRoot;

/** PointerTypeImpl Class
**/
public class
PointerTypeImpl extends TypeImpl implements PointerType
{
                   /** Type of pointed object. */
  Type fPointedType;
                   /** Element count
                   if this pointer is declared as an array. */
  long fElemCount    = 0;
                   /** Lower bound of array subscript
                    if this pointer is declared as an array. */
  long fLowerBound   = 0;

public
PointerTypeImpl( SymRoot pSymRoot, String pTypeName, Type pPointedType )
{
  super(pSymRoot);
  fKind = Sym.KIND_TYPE;
  fType = this;
  fTypeKind = Type.KIND_POINTER;
  fPointedType = pPointedType;
  if (pTypeName == null) {
    //fName = "<PTR " + fPointedType.toString() + ">"; // S.Fukuda 2002.5.12 original
    fName = "<PTR " + fPointedType.getName() + ">"; // S.Fukuda 2002.5.12
    fName = fName.intern();
  }else
    fName = pTypeName;
  //////////////////// S,Fukuda 2002.10.30
  //fTypeSize = MachineParam.SIZEOF_PTR;
  //fTypeSizeExp = symRoot.getHirRoot().hir.intConstNode(fTypeSize);
  //fSizeIsSet = true;
  //##64 setSizeValue(machineParam.evaluateSize(Type.KIND_INT)); //##52
  setSizeValue(machineParam.evaluateSize(Type.KIND_POINTER)); //##64
  //////////////////// S,Fukuda 2002.10.30
  setFlag(Sym.FLAG_UNIFORM_SIZE, false);
} // PointerTypeImpl

public
PointerTypeImpl( SymRoot pSymRoot, Type pPointedType )
{
  ///////////////////////////////////// S.Fukuda 2002.5.12 begin
  this(pSymRoot,null,pPointedType);
  /*/////////////////////////////////// S.Fukuda 2002.5.12 original
  super(pSymRoot);
  fKind = Sym.KIND_TYPE;
  fType = this;
  fTypeKind = Type.KIND_POINTER;
  fPointedType = pPointedType;
  fName = "<PTR " + fPointedType.getName() + ">";
  fName = fName.intern();
  fTypeSize = MachineParam.SIZEOF_PTR;
  fSizeIsSet = true;
  *//////////////////////////////////// S.Fukuda 2002.5.12 end
} // PointerTypeImpl

public
PointerTypeImpl( SymRoot pSymRoot, Type pPointedType,
    long pElemCount, long pLowerBound )
{
  super(pSymRoot);
  fKind = Sym.KIND_TYPE;
  fType = this;
  fTypeKind = Type.KIND_POINTER;
  fPointedType = pPointedType;
  // Make name <PTR elemCount lowerBound pointedElemName>
  // to avoid name collision between pointer that does not represent a vector.
  StringBuffer lBuffer = new StringBuffer();
  lBuffer.append("<PTR ");
  lBuffer.append(Long.toString(pElemCount, 10));
  lBuffer.append(" ");
  lBuffer.append(Long.toString(pLowerBound, 10));
  lBuffer.append(" ");
  lBuffer.append(pPointedType.getName());
  lBuffer.append(">");
  fName = lBuffer.toString().intern();
  fElemCount = pElemCount;
  fLowerBound = pLowerBound;
  //##64 setSizeValue(machineParam.evaluateSize(Type.KIND_INT)); //##52
  setSizeValue(machineParam.evaluateSize(Type.KIND_POINTER)); //##64
  setFlag(Sym.FLAG_UNIFORM_SIZE, false);
} // PointerRepresentingVectorImpl


/** getPointedType
 *  Get the pointed type of this pointer type.
 *  "this" should be a pointer type.
 *  @return Type the pointed type of this type.
 *      If "this" is not a pointer, then return null.
**/
public Type
getPointedType()
{
  return fPointedType;
}

public boolean
isCompatibleWith( Type pType )
{
  if (pType == this)
    return true;
  else if (pType == null)
    return false;
  else {
    if ((pType.getTypeKind() == Type.KIND_POINTER)&&
        (pType.isConst() == isConst())&&
        (pType.isVolatile() == isVolatile())) {
      if (getPointedType().
          isCompatibleWith(((PointerType)pType).getPointedType()))
        return true;
    }
  }
  return false;
} // isCompatibleWith

public boolean
isDeclaredAsArray()
{
  if (fElemCount > 0)
    return true;
  else
    return false;
} // isDeclaredAsArray

public long
getElemCount() { return fElemCount; }

public long
getLowerBound() { return fLowerBound; }

} // PointerTypeImpl
