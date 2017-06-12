/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.HirRoot;
import coins.sym.Elem;

/**
 * Qualified variable class.
**/ 
public class 
QualifiedExpImpl extends ExpImpl implements QualifiedExp 
{

public 
QualifiedExpImpl( HirRoot pHirRoot, Exp pStructUnionExp, ElemNode pElemNode ) 
{
  super(pHirRoot, HIR.OP_QUAL);	
  fChildCount = 2;
  setChildren(pStructUnionExp, pElemNode);
  fType = pElemNode.getType();
}

public Exp
getQualifierExp() { return (Exp)fChildNode1; }

public Elem
getQualifiedElem() { return ((ElemNode)fChildNode2).getElem(); }

public void 
accept( HirVisitor pVisitor )
{
  pVisitor.atQualifiedExp(this);
}

} // QualifiedExpImpl class
