/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;

import coins.ir.IrList;

/** StructType interface
 * See structType of Sym interface.
**/
public interface
StructType extends Type {

/**
 * Get the symbol table local to this structure.
 * @return the local symbol table.
 */
public SymTable
getSymTable();                   // (##4)

/**
 * Set the symbol table local to this structure.
 * @param the local symbol table to be set.
 */
public void
setSymTable( SymTable pSymTalbe ); // (##4)

/** getElemList
 *  @return the list of elements for this structure.
**/
public IrList
getElemList();

/** addElem          Add element to this struct type.
 * addElem is called after instantiating StructType without
 * giving element list. After successive call of addElem,
 * finishStructType should be called.
 * See structType of Sym, finishStructType.
 * @param pElem Element of this struct type.
 * @param pSeparately true after successive call of addElem,
 *     false if element list is given in bundle.
**/
public void
addElem( Elem pElem );

/**
<PRE>
 *  finishStructType: Finish to define this struct type.
 * After successive call of addElem,
 * finishStructType should be called.
 * This will make the struct type name and compute displacement
 * of each elements, and then compute the size of this struct.
 * See structType of Sym, addElem.
 * The alignment of a struct type is the same to that of its element
 * having the largest alignment among the elements in the structure.
 * Elements of the struct type is aligned according to the alignment
 * of element type. This may cause to place gap between preceeding element.
 * The size of the struct type is multiple of the alignment value of
 * the struct type, thus, there may be a gap at the tail of the struct type
 * if the last element has small alignment value.
 * finishStructType also allocates bit fields (set bit field offset).
 * As for the allocation of bit field, see the explanation of
 * setBitFieldOffset method of the Elem interface.
</PRE>
 * @param pSeparately false if StructType is constructed with complete
 *     element list, true if StructType is constructed as incomplete
 *     and sequence of addElem call is performed and then finishStructType
 *     is called.
**/
public void
finishStructType( boolean pSeparately );


/* getTag
 * @return the tag name symbol of this type.
**/
public Sym
getTag(); //##12

/* setTag
 * Set pTag as the tag symbol of this type.
 * It is not recommended to use this method except in
 * factory methods where implementation specifications
 * are known.  //##19
 * @param pSym should be a tag name.
**/
public void
setTag( Sym pTag ); //##12

/** setAlignment //##47
 * Set alignment value to be equal to the pAlignment.
 * Default value of alignment is set to the alignment of
 * element type with the largest alignment value.
 * @param pAlignment alignment value to be set.
 */
public void
setAlignment( int pAlignment ); //##47

} // StructType
