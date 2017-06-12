/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import java.util.Iterator;
import java.util.LinkedList;

import coins.HirRoot;
import coins.ir.IR;
import coins.ir.IrList;
import coins.sym.RegionType;
import coins.sym.Sym;
import coins.sym.SymTable;

//========================================//
//                           Jan 19, 2001.
//                           (##2): modified on Nov. 2000
//                           (##3): modified on Jan. 2001.

/** ProgramImpl class
 **/
public class
ProgramImpl extends HIR_Impl implements Program
{

  protected SymTable fGlobalSymTable = null;

  protected IrList fRegionTypeList = hirRoot.hir.irList();  

public
ProgramImpl( HirRoot pHirRoot, Sym pProgSym, SymTable pGlobalSymTable, 
             IR pInitiationPart, IrList pSubpList )
{
  super(pHirRoot); 
  fOperator = IR.OP_PROG;
  fGlobalSymTable = pGlobalSymTable;
  fAdditionalChild = new IR[1];
  fChildCount = 3;
  if (pProgSym != null)
    fChildNode1 = (IR)(hirRoot.hir.symNode(pProgSym)); 
  else
    fChildNode1 = null;
  if (pInitiationPart != null)
    fChildNode2 = (IR)pInitiationPart;
  else
    fChildNode2 = (IR)(new NullNodeImpl(hirRoot)); 
  if (pSubpList != null)
    fAdditionalChild[0] = (IR)pSubpList;
  else
    fAdditionalChild[0] = (IR)(hirRoot.hir.irList(new LinkedList())); 
  if (fChildNode1 != null)
    ((HIR_Impl)fChildNode1).setParent(this); //##54
  if (fChildNode2 != null)
    ((HIR_Impl)fChildNode2).setParent(this); //##54
  if (fAdditionalChild[0] != null)
    ((HIR_Impl)fAdditionalChild[0]).setParent(this); //##54
  fType = hirRoot.symRoot.typeVoid;
} // ProgramImpl

public SymTable
getSymTable() { return fGlobalSymTable; }

public void
setSymTable( SymTable pSymTable) {
  fGlobalSymTable = pSymTable; }

/** getSubpDefinitionList
 *  Get subprogram definition list of this program.
 *  Each SubpDefinition can be get and treated by methods
 *  in IrList.
 *  @return SubpDefinition list of this program.
**/
public IrList
getSubpDefinitionList() {
  return (IrList)fAdditionalChild[0];
}

public void
addSubpDefinition( SubpDefinition pSubpDefinition ) {
  IrList subpDefList = getSubpDefinitionList();
  subpDefList.add(pSubpDefinition);
} // addSubpDefinition

public IrList
getRegionList() 
{
  return fRegionTypeList;
}

public void
addRegion( RegionType pRegionType ) 
{
  if (fRegionTypeList.contains(pRegionType))
    return;
  else {
    fRegionTypeList.add(pRegionType);
  }
} // addRegionType

public IR
getInitiationPart() {
  return (IR)fChildNode2;
}

public void
addInitiationStmt( IR pStmt ) {
  if ((fChildNode2 == null)||
      (fChildNode2.getOperator() != HIR.OP_BLOCK)) {
    fChildNode2 = hirRoot.hir.blockStmt((Stmt)pStmt); 
    if (fChildNode2 != null)
    /*////////////////////////////////// S.Fukuda 2002.7.1 begin
      fChildNode2.setParent(this); 
    */
    {
      ((HIR_Impl)fChildNode2).setParent(this); //##54
      // SymTable of InitiationPart of Program is symTableRoot.
      ((BlockStmt)fChildNode2).setSymTable(hirRoot.symRoot.symTableRoot);
      ((BlockStmt)fChildNode2).setFlag(HIR.FLAG_INIT_BLOCK,true); //SF040925
    }
    //////////////////////////////////// S.Fukuda 2002.7.1 end
  }else
    ((BlockStmt)fChildNode2).addLastStmt((Stmt)pStmt);
} // addInitiationStmt

public Object
clone() {
  Program lTree;
  try {
    lTree = (Program)super.clone();
  }catch (CloneNotSupportedException e) {
    lTree = null;
  }
  ((ProgramImpl)lTree).fGlobalSymTable = fGlobalSymTable;
  return (Object)lTree;
} // clone


public void
print(int pIndent, boolean pDetail)
{
  String         lineImage, lSpace;
  IrList         subpDefList;
  SubpDefinition lSubpDef;
  lSpace = hirRoot.hir.getIndentSpace(pIndent);
  lineImage = lSpace + "(" + this.toString();
    hirRoot.ioRoot.printOut.print("\n" + lineImage); 
    if (fChildNode1 != null)  // program name node.
       ((HIR)fChildNode1).print( pIndent+1, pDetail );
    else
      hirRoot.ioRoot.printOut.print("\n" + lSpace + " <null 0 void>");
    if (fChildNode2 != null)  // Initiation block.
      ((HIR)fChildNode2).print( pIndent+1, pDetail );
    else
      hirRoot.ioRoot.printOut.print("\n" + lSpace + " <null 0 void>");
    if (fAdditionalChild[0] != null) { // SubpDefinition list.
      subpDefList = getSubpDefinitionList();
      Iterator subpDefIterator = subpDefList.iterator();
      while (subpDefIterator.hasNext()) {
        lSubpDef = (SubpDefinition)(subpDefIterator.next());
        lSubpDef.print(1, pDetail);
      }
    }
    hirRoot.ioRoot.printOut.print(")"); 
} // print

public void
accept( HirVisitor pVisitor )
{
  pVisitor.atProgram(this);
}

} // ProgramImpl class

