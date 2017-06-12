/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;

import coins.ir.IrList;

/** RegionType interface.
 *  See regionType of Sym.
**/
public interface
RegionType extends Type
{

public int
addSubp( Subp pSubp, SymTable pRegionSymTable );

public IrList
getSubpList();

/** getCurrentRegionSymTable
 * @return the symbol table containing the definitions of elements
 *     to be included in the region in the current subprogram
 *     (symRoot.subpCurrent).
**/
public SymTable
getCurrentRegionSymTable();

/** getRegionSymTable
 * @param pSubp Subprogram for which the corresponding symbol table
 *     is to be gotten.
 * @return the symbol table containing the definitions of elements
 *     declared in pSubp to be included in the region.
**/
public SymTable
getRegionSymTable( Subp pSubp );

/** getRegionSymTable
 * Get pIndex-th symbol table from the list of symbol tables of
 * this RegionType. This method is mainly used to get the symbol table
 * corresponding to the region declared outside subprogram.
 * @param pIndex index of the symbol tabel to be gotten in the list
 *     of the symbol tables.
 * @return the pIndex-th symbol table containing the definitions of
 *    elements to be included in the region.
**/
public SymTable
getRegionSymTable( int pIndex );

/** addElemToCurrentRegion
 * Add element pElem to this region.
 * addElem is called after instantiating RegionType.
 * After successive call of addElemToCurrentRegion,
 * finishCurrentRegion should be called.
 * Sym.FLAG_REGION_ELEM is set true for pElem.
 * See regionType of Sym.
 * @param pElem Element to be included in this region.
**/
public void
addElemToCurrentRegion( Elem pElem );

public IrList
getRegionElemList( Subp pSubp );

/**
<PRE>
 *  finishCurrentRegion
 *  Close the current region (this region) by setting all required fields
 *  so that methods
 *    getRegionElemList, getRegionVar, getSizeValue of this RegionType
 *  and
 *    evaluateDisp() of region elements
 *  become effective.
</PRE>
**/
public void
finishCurrentRegion( );

/* getRegionVar
 * See regionType of Sym.
 * @return the region variable corresponding to this region type.
**/
public Var
getRegionVar();

} // RegionTypeImpl
