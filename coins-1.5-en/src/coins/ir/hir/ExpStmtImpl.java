/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.HirRoot;

//========================================//
//                            (##5): modified on Apr. 2001.

/**
 * ExpStmtImpl
 *    Expression treated as a statement.
**/
public class
ExpStmtImpl extends StmtImpl implements ExpStmt
{

public
ExpStmtImpl( HirRoot pHirRoot, Exp pExp)
{
  super(pHirRoot, HIR.OP_EXP_STMT);
  fChildCount = 1;
  setChild1(pExp);
  if (pExp != null)
    fType = pExp.getType();
  else
    fType = hirRoot.symRoot.typeInt;  //default
}

public Exp
getExp() { return (Exp)fChildNode1; }

public void
setExp( Exp pExp )
{
  if (fChildNode1 == pExp)   //##59
    return;                  //##59
  setChild1(pExp);
}

public void
accept( HirVisitor pVisitor )
{
  pVisitor.atExpStmt(this);
}

} // ExpStmtImpl
