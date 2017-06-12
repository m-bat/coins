/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.HirRoot;
import coins.sym.Subp;

/**
 * Subprogram name node.
**/
public class
SubpNodeImpl extends SymNodeImpl implements SubpNode
{

public
SubpNodeImpl( HirRoot pHirRoot, Subp pSym)
{
  super(pHirRoot, pSym);
  fOperator = HIR.OP_SUBP;
  fChildCount = 0;
  if (pSym.getSymType() != null)
    //fType = pSym.pointerType(null, pSym.getSymType(), // S.Fukuda 2002.5.16
    //                hirRoot.symRoot.subpCurrent);
    fType = pSym.getSymType(); // S.Fukuda 2002.5.16
  else
    fType = pSym.getReturnValueType();  //## REFINE
  if (fDbgLevel > 3) //##58
    pHirRoot.ioRoot.dbgHir.print(4, "subpNode", pSym.getName()
                  + " " +  fType.toString());
}

public Subp
getSubp() { return (Subp)fSym; }

public void
accept( HirVisitor pVisitor )
{
  pVisitor.atSubpNode(this);
}

} // SubpNodeImpl class
