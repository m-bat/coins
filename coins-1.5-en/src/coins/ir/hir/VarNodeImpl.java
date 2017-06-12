/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.HirRoot;
import coins.sym.Var;

/**
 * Variable name node class.
**/ 
public class 
VarNodeImpl extends SymNodeImpl implements VarNode 
{

public 
VarNodeImpl( HirRoot pHirRoot ) 
{ 
  super(pHirRoot); 
  fOperator = HIR.OP_VAR;
  fChildCount = 0;
  fType = hirRoot.symRoot.typeInt;  // Set default. 
}

public 
VarNodeImpl(HirRoot pHirRoot, Var pSym) 
{
  super(pHirRoot, pSym); 
  fOperator = HIR.OP_VAR;
  fChildCount = 0;
  fType = pSym.getSymType();
}

public Var
getVar() { return (Var)fSym; }

public void 
accept( HirVisitor pVisitor )
{
  pVisitor.atVarNode(this);
}

} // VarNodeImpl class
