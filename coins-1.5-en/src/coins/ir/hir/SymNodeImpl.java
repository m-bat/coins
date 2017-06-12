/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.HirRoot;
import coins.sym.StringConst;
import coins.sym.Sym;

/**
 * Symbol node
**/
public class
SymNodeImpl extends ExpImpl implements SymNode
{

  protected Sym fSym;

public
SymNodeImpl( HirRoot pHirRoot )
{
    super(pHirRoot, HIR.OP_SYM);
    fChildCount = 0;
}

public
SymNodeImpl( HirRoot pHirRoot, Sym pSym)
{
    super(pHirRoot, HIR.OP_SYM);
    fSym = pSym;
    fChildCount = 0;
    if (pSym != null)
      fType = pSym.getSymType();
    else
      fType = hirRoot.symRoot.typeVoid;
}

public Sym
getSym() { return fSym; }

public Sym
getSymNodeSym() { // Used to avoid confusion
  return fSym;    // with getSym in other class.
}

public void
setSymNodeSym( Sym pSym ) { fSym = pSym; }

public String
toString() {
  String nodeString = super.toString();
  if (fSym != null) {
    if (fSym instanceof StringConst)
      nodeString = nodeString + " " + fSym.toString();
    else
      nodeString = nodeString + " " + fSym.getName();
  }else
    nodeString = nodeString + " null";
  // if ((fType != null)&&(fOperator != HIR.OP_TYPE))
  //   nodeString = nodeString + " " + fType.getName();
  return nodeString;
} // toString

//##70 BEGIN
public String
toStringShort() {
  String nodeString = super.toStringShort();
  if (fSym != null) {
    if (fSym instanceof StringConst)
      nodeString = nodeString + " " + fSym.toString();
    else
      nodeString = nodeString + " " + fSym.getName();
  }else
    nodeString = nodeString + " null";
  // if ((fType != null)&&(fOperator != HIR.OP_TYPE))
  //   nodeString = nodeString + " " + fType.getName();
  return nodeString;
} // toStringShort

//##70 END

public String
getIrName() {
  String lString = super.getIrName();
  if (fSym instanceof StringConst)
    lString = lString + " " + fSym.toString();
  else
    lString = lString + " " + fSym.getName();
  return lString;
} // getIrName

public Object
clone() throws CloneNotSupportedException {
  SymNode lTree;
  try {
    lTree = (SymNode)super.clone();
  }catch (CloneNotSupportedException e) {
    hirRoot.ioRoot.msgRecovered.put(1100,
                   "CloneNotSupportedException(SymNodeImpl) "
                     + this.toStringShort());
    return null;
  }
  ((SymNodeImpl)lTree).fSym = fSym;
  if (fDbgLevel > 3) //##58
    hirRoot.ioRoot.dbgHir.print( 9, "clone of SymNode " + lTree.toStringShort());
  return (Object)lTree;
} // clone

/*##
public HIR
copyWithOperandsChangingLabel(
    SymTable pFromSymTable, SymTable pToSymTable,
    BBlock pNewBBlock ) {
  HIR lTree = super.copyWithOperandsChangingLabel(pFromSymTable,
                    pToSymTable, pNewBBlock);
  if (pFromSymTable.isInThisSymTable(fSym))
    ((SymNodeImpl)lTree).fSym =
       pToSymTable.searchOrAdd(fSym.getName().intern(),
                   fSym.getSymKind(), subpCurrent, true, true);
  else
    ((SymNodeImpl)lTree).fSym = fSym;
  return (HIR)lTree;
} // copyWithOperandsChangingLabel
##*/

public void
accept( HirVisitor pVisitor )
{
  pVisitor.atSymNode(this);
}

} // SymNodeImpl
