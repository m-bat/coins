/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.ir.IR;
import coins.ir.IrList;
import coins.sym.RegionType;
import coins.sym.SymTable;

/** Program interface
 **/
public interface
Program extends HIR
{

public SymTable
getSymTable();

public void
setSymTable( SymTable pSymTable );

/** addSubpDefinition
 *  Add the definition of a subprogram to the end of
 *  subprogram definition list of this program.
 *  @param pSubpDefinition SubpDefinition node
 *      defining a subprogram.
**/
public void
addSubpDefinition( SubpDefinition pSubpDefinition );

/** getSubpDefinitionList
 *  Get subprogram definition list of this program.
 *  Each SubpDefinition can be get and treated by methods
 *  in IrList.
 *  @return SubpDefinition list of this program.
**/
public IrList
getSubpDefinitionList();

/** getRegionList
 *  Get the list of RegionType instances.
 *  Each instance of RegionType can be get and treated by methods
 *  in IrList.
 *  @return RegionType list of this program.
**/
public IrList
getRegionList(); 

/** addRegion 
 *  Add pRegionType to the end of region list of this program.
 *  If pRegionType is already recorded in the list, then
 *  it is not added.
 *  @param pRegionType RegionType to be recorded.
**/
public void
addRegion( RegionType pRegionType ); 


/** getInitiationPart
 *  Get the initiation part that initiates global variables.
 *  @return BlockStmt containing initiation statements.
**/
public IR
getInitiationPart();

/** addInitiationStmt
 *  Add the initiation statement pStmt that initiates global variables
 *  to the initiation part (BlockStmt) of this program.
**/
public void
addInitiationStmt( IR pStmt );

} // Program interface

