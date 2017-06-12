/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.HirRoot;
import coins.sym.Elem;

/**
 * Pointed variable class.
**/ 
public class 
PointedExpImpl extends ExpImpl implements PointedExp 
{

public 
PointedExpImpl( HirRoot pHirRoot, Exp pStructUnionExp, ElemNode pElemNode ) 
{
  super(pHirRoot, HIR.OP_ARROW); 
  fChildCount = 2;
  setChildren(pStructUnionExp, pElemNode);
  if (pElemNode != null)
    fType = pElemNode.getType();
  else 
    fType = pStructUnionExp.getType().getPointedType();
}

public Exp
getPointerExp() { return (Exp)fChildNode1; }

public Elem
getPointedElem() { return ((ElemNode)fChildNode2).getElem(); }

public void 
accept( HirVisitor pVisitor )
{
  pVisitor.atPointedExp(this);
}

} // PointedExpImpl class
