/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.HirRoot;

/**
 *
 * Return statement.
 *
**/
public class
ReturnStmtImpl extends StmtImpl implements ReturnStmt 
{

public
ReturnStmtImpl( HirRoot pHirRoot ) 
{
  super(pHirRoot, HIR.OP_RETURN); 
  fChildCount = 0;
  fType = hirRoot.symRoot.typeVoid; 
}

public
ReturnStmtImpl( HirRoot pHirRoot, Exp pReturnValue ) 
{
  super(pHirRoot, HIR.OP_RETURN); 
  fChildCount = 1;
  setChild1(pReturnValue);
  if (pReturnValue == null)
    fType = hirRoot.symRoot.typeVoid; 
  else 
    fType = pReturnValue.getType();
}

    /** getReturnValue
     *  Get the return value expression of return statement.
     *  "this" should be a return statement build by buildReturnStmt.
     *  @return the return value expression of this statement.
     **/
public Exp
getReturnValue() {
    return (Exp)fChildNode1;
}

public void
setReturnValue(Exp pReturnValue) {
    setChild1(pReturnValue);
}

public void
accept( HirVisitor pVisitor )
{
  pVisitor.atReturnStmt(this);
}

} // ReturnStmtImpl
