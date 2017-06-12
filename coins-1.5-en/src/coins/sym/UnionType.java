/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;

import coins.ir.IrList;

/** UnionType interface
**/
public interface
UnionType extends Type {

public SymTable
getSymTable();                   // (##4)

public void
setSymTable( SymTable pSymTalbe ); // (##4)

/** getElemList
 *  @return the list of elements for this union.
**/
public IrList
getElemList();

/** addElem         Add element to this union type.
 *  finishUnionType Finish to define this union type.
 * addElem is called after instantiating UnionType without
 * giving element list. After successive call of addElem,
 * finishUnionType should be called.
 * See unionType of Sym.
 * @param pElem Element of this union type.
 * @param pSeparately true after successive call of addElem,
 *     false if element list is given in bundle.
**/
public void
addElem( Elem pElem );

/** finishUnionType Finish to define this union type.
 * After successive call of addElem,
 * finishUnionType should be called.
 * This will make the union type name and then compute the size of this
 * union.
 * See unionType of Sym, addElem.
 * @param pSeparately false if UnionType is constructed with complete
 *     element list, true if UnionType is constructed as incomplete
 *     and sequence of addElem call is performed and then finishUnionType
 *     is called.
**/
public void
finishUnionType( boolean pSeparately );

/* getTag
 * @return the tag name symbol of this type.
**/
public Sym
getTag();  

/* setTag
 * Set pTag as the tag symbol of this type.
 * It is not recommended to use this method except in
 * factory methods where implementation specifications
 * are known.  
 * @param pSym should be a tag name.
**/
public void
setTag( Sym pTag ); 

/** setAlignment 
 * Set alignment value to be equal to the pAlignment.
 * Default value of alignment is set to the alignment of
 * element type with the largest alignment value.
 * @param pAlignment alignment value to be set.
 */
public void
setAlignment( int pAlignment ); 

} // UnionTypeImpl
