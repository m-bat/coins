/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.HirRoot;
import coins.ir.IR;
import coins.ir.IrList;
import coins.sym.Label;
import coins.sym.Subp;
import coins.sym.SymTable;

/**
 *  Subprogram definition node.
 *
**/
public class
SubpDefinitionImpl extends HIR_Impl implements SubpDefinition
{

//====== Field declarations ======//

/** Fields in SubpDefinition
 *  fChildnode1     SubpNode representing the subprogram symbol.
 *  fChildNode2         HIR initiation part (BlockStmt)
 *  fAdditionalChild[0]: HIR body
 *  fAdditionalChild[1]: LIR body
 */
  private Subp fSubp;  // Subprogram symbol.

  /**
   * fNodeIndexMin Minimum of the node index.
   * fNodeIndexMax Maximum of the node index.
   * They are set by setIndexNumberToAllNodes of HIR interface.
   */
   public int
     fNodeIndexMin = 0,
     fNodeIndexMax = 0;

//====== Constructor declaration ======//

public
SubpDefinitionImpl( HirRoot pHirRoot, Subp pSubpSym, SymTable pLocalSymTable,
                    BlockStmt pInitiation,
                    BlockStmt pHirBody ) {
  super(pHirRoot, IR.OP_SUBP_DEF);
  Label     lStartLabel, lEndLabel;
  SubpNode  lSubpNode = hirRoot.hir.subpNode(pSubpSym);
  BlockStmt lHirBody  = pHirBody;
  fAdditionalChild = new IR[2];
  fChildCount = 4;
  fSubp       = pSubpSym;
  if (pSubpSym != null) {
    if (lHirBody == null)
      lHirBody = hirRoot.hir.blockStmt(null);
    lHirBody.setSubpBodyFlag(true);
    hirRoot.symRoot.subpCurrent = pSubpSym;

    hirRoot.symRoot.subpCurrent = pSubpSym;
    if (pLocalSymTable != null) {
      pSubpSym.setSymTable(pLocalSymTable);
      hirRoot.symRoot.symTableCurrentSubp = pLocalSymTable;
    }
    lStartLabel = hirRoot.symRoot.symTableCurrent.generateLabel();
    lEndLabel   = hirRoot.symRoot.symTableCurrent.generateLabel();
    lStartLabel.setLabelKind(Label.ENTRY_LABEL);
    lEndLabel.setLabelKind(Label.RETURN_POINT_LABEL);
    pSubpSym.setHirBody(lHirBody, lStartLabel, lEndLabel);
    pSubpSym.setSubpDefinition((SubpDefinition)this);
    setChildren(lSubpNode, pInitiation, lHirBody, null);
  }else {
    hirRoot.ioRoot.msgRecovered.put(1110,
        "Subprogram symbol is required in SubpDefinition");
  }
  fType = hirRoot.symRoot.typeVoid;
}

//====== Methods to get/set information ======//

public SymTable
getSymTable()
{
  return fSubp.getSymTable();
}

public void
setSymTable( SymTable pSymTable )
{
  fSubp.setSymTable(pSymTable);
}

public Subp
getSubpSym() {
  return fSubp;
}

public BlockStmt
getInitiationPart() {
  return (BlockStmt)fChildNode2;
}

public void
addInitiationStmt( Stmt pStmt ) {
  IR lInitPart;
  if ((fChildNode2 == null)||
      (fChildNode2.getOperator() != HIR.OP_BLOCK))
    {
      fChildNode2 = hirRoot.hir.blockStmt((Stmt)pStmt);
      ((HIR_Impl)fChildNode2).setParent(this); //##54
      ((BlockStmt)fChildNode2).setSymTable( getSymTable() );
      ((BlockStmt)fChildNode2).setFlag(HIR.FLAG_INIT_BLOCK,true); //SF040925
    }
  else
    ((BlockStmtImpl)fChildNode2).addLastStmt((Stmt)pStmt);
} // addInitiationStmt

public Stmt
getHirBody()
{
  //##67 return (Stmt)(getSubpSym().getHirBody());
  return (Stmt)getChild(3); //##67
}

public void
setHirBody( BlockStmt pHirBody ) {
  Stmt lHirBody;
  if (pHirBody != null) {
    fSubp.setHirBody(pHirBody, fSubp.getStartLabel(),
                     fSubp.getEndLabel());
    // REFINE The end label of fSubp should be generated
    //        in SubpDefinitionImpl.
    //        (SubpImpl does not generate the end label.)
    // HirBody may be changed to LabeledStmt.
    lHirBody = fSubp.getHirBody();
    fAdditionalChild[0] = lHirBody;
    ((HIR_Impl)lHirBody).setParent(this); //##54
  }
}

public Label
getStartLabel()
{
  return fSubp.getStartLabel();
}

/* //##91
public Label
getEndLabel()
{
  return fSubp.getEndLabel();
}
*/ //##91

public HIR
copyWithOperandsChangingLabel( IrList pLabelCorrespondence ) {
  HIR lNode = super.copyWithOperandsChangingLabels(pLabelCorrespondence);
  return lNode;
} // copyWithOperandsChangingLabel

public Object
clone() throws CloneNotSupportedException
{
  SubpDefinitionImpl lSubpDef;

  try {
    lSubpDef = (SubpDefinitionImpl)super.clone();
    lSubpDef.fSubp = fSubp;
    if (fDbgLevel > 2) //##58
      hirRoot.ioRoot.dbgHir.print(3, "clone of SubpDefinition", fSubp.getName());
  }catch (CloneNotSupportedException e) {
    hirRoot.ioRoot.msgRecovered.put(1100,
                   "CloneNotSupportedException(SubpDefinitionImpl) "
                     + this.toString());
    return null;
  }
  return (Object)lSubpDef;
} // clone

public void
print( int pIndent, boolean pDetail )
{
  String lineImage, lSpace;
  lSpace = hirRoot.hir.getIndentSpace(pIndent);
  if (pDetail)
    lineImage = lSpace + "(" + this.toStringDetail();
  else
    lineImage = lSpace + "(" + this.toString();
  hirRoot.ioRoot.printOut.print("\n" + lineImage);
  ((HIR)fChildNode1).print( pIndent+1, pDetail );
  if (fChildNode2 != null)
    ((HIR)fChildNode2).print( pIndent+1, pDetail );
  else
    hirRoot.ioRoot.printOut.print("\n" + lSpace + "<null 0 void>");
  if ((fSubp != null)&&(fSubp.getHirBody() != null))
    fSubp.getHirBody().print(pIndent+1, pDetail);
  hirRoot.ioRoot.printOut.print(")");
} // print

public void
printHir( String pHeader )
{
  hirRoot.ioRoot.printOut.print("\nHIR of "
        + getSubpSym().getName() + " " + pHeader + "\n");
  this.print(2, true);  //##57
} // printHir

public void
accept( HirVisitor pVisitor )
{
  pVisitor.atSubpDefinition(this);
}

public int
getNodeIndexMin()
{
  return fNodeIndexMin;
}

public int
getNodeIndexMax()
{
  return fNodeIndexMax;
}
} // SubpDefinitionImpl class

