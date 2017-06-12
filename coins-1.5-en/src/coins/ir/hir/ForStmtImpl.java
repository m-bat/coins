/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.HirRoot;
import coins.sym.Label;

/**
 *  For-statement class.
**/
public class
ForStmtImpl extends LoopStmtImpl implements ForStmt
{

public
ForStmtImpl( HirRoot pHirRoot, Stmt pInitStmt, Label pLoopBackLabel, 
             Exp pCondition, Stmt pLoopBody,
             Label pLoopStepLabel, Stmt pStepPart,
             Label pLoopEndLabel )
{
  super(pHirRoot); 
  fOperator = HIR.OP_FOR;
  setChildrenOfLoop(pInitStmt, pLoopBackLabel, null,
                    pCondition, pLoopBody,
                    pLoopStepLabel, null, pStepPart, pLoopEndLabel,
                    null); 
  fType = pHirRoot.symRoot.typeVoid; 
} // ForStmtImpl

public void
accept( HirVisitor pVisitor )
{
  pVisitor.atForStmt(this);
}

} // ForStmtImpl class
