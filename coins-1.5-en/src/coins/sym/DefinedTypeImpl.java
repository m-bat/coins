/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;

import coins.SymRoot;

/** DefinedTypeImpl Class  
**/
public class
DefinedTypeImpl extends TypeImpl implements DefinedType
{

  protected Type
    fOriginalType; // Original type of this type.

public
DefinedTypeImpl( SymRoot pSymRoot, String pTypeName, Type pOriginalType )
{
  super(pSymRoot);
  fKind = Sym.KIND_TYPE;
  fType = this;
  fTypeKind = Type.KIND_DEFINED;
  fOrigin = pOriginalType;
  fName =  pTypeName;
  fName = fName.intern();
  //////////////////// S.Fukuda 2002.10.30
  //fSizeIsSet = ((TypeImpl)pOriginalType).fSizeIsSet;
  //if (fSizeIsSet) { 
  //  fTypeSize = pOriginalType.getSizeValue();
  //  fTypeSizeExp = pOriginalType.getSizeExp();
  //}
  if( pOriginalType.getSizeValue()>=0 )
    setSizeValue( pOriginalType.getSizeValue() );
  else if( pOriginalType.getSizeExp()!=null )
    setSizeExp( pOriginalType.getSizeExp() );
  //////////////////// S.Fukuda 2002.10.30
} // DefinedTypeImpl

} // DefinedTypeImpl

