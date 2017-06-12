/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;


/** EnumType
 *  Enumeration type interface
**/
public interface
EnumType extends Type {

public int
getEnumSeqNumber();

public void
setEnumSeqNumber( int pNumber );

/** addElem
 * Add element to this enum type.
 * addElem is called after instantiating EnumType without
 * giving element list. After successive call of addElem,
 * finishEnumType should be called.
 * See enum of Sym.
 * @param pElem Element of this enum type.
**/
public void
addElem( NamedConst pElem );

/**
 * Finish to define this enum type.
 * After successive call of addElem,
 * finishEnumType should be called.
 * See enum of Sym.
 * @param pSeparately true after successive call of addElem,
 *     false if element list is given in bundle.
**/
public void
finishEnumType( boolean pSeparately );

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

} // EnumType interface

