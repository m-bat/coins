/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.HirRoot;
import coins.sym.Type;

/**
 * Type name node class.
**/ 
public class 
TypeNodeImpl extends SymNodeImpl implements TypeNode 
{

public 
TypeNodeImpl( HirRoot pHirRoot, Type pSym) 
{
  super(pHirRoot, pSym); 
  fOperator = HIR.OP_TYPE;
  fChildCount = 0;
  fType = pSym;
}

public Type
getType() { return (Type)fSym; }

public String
toString() {
  String nodeString = super.toString();
  nodeString = nodeString + " " + fSym.getName();
  return nodeString;
} // toString

public void 
accept( HirVisitor pVisitor )
{
  pVisitor.atTypeNode(this);
}

} // TypeNodeImpl class
