/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;

import coins.MachineParam;
import coins.SymRoot;

/** BaseTypeImpl Class
**/
public class
BaseTypeImpl extends TypeImpl implements BaseType
{

/** BaseTypeImpl  BaseType constructor.
 *  Use sym.baseType to construct a BaseType object because
 *  there are several fields to be set consistently.
**/
public BaseTypeImpl( SymRoot pSymRoot, String pTypeName, int pTypeKind ) 
{
  super(pSymRoot); 
  fName = pTypeName;
  fKind = Sym.KIND_TYPE;
  fType = this;
  fTypeKind = pTypeKind;
  fTypeCore = fName;
  fOrigin   = pSymRoot.sourceLanguage.baseTypeOrigin(this);
  //fTypeSize = MachineParam.evaluateSize(pTypeKind); // S.Fukuda 2002.10.30
  setSizeValue( machineParam.evaluateSize(pTypeKind) ); // S.Fukuda 2002.10.30 //##51
  //fSizeIsSet = true; // S.Fukuda 2002.10.30
  setFlag(Sym.FLAG_UNIFORM_SIZE, true); 
}

} // BaseTypeImpl class
