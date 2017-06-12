/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.HirRoot;
import coins.sym.Elem;

/**
 * Element name node class.
**/ 
public class 
ElemNodeImpl extends VarNodeImpl implements ElemNode 
{

public 
ElemNodeImpl( HirRoot pHirRoot, Elem pSym) {
  super(pHirRoot, pSym);	
  fOperator = HIR.OP_ELEM;
  fChildCount = 0;
  fType = pSym.getSymType();
}

public Elem
getElem() { return (Elem)fSym; }

public void 
accept( HirVisitor pVisitor )
{
  pVisitor.atElemNode(this);
}

} // ElemNodeImpl class
