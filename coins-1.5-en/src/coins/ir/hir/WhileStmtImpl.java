/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.HirRoot;
import coins.sym.Label;

/**
 *  While-statement class.
**/
public class
WhileStmtImpl extends LoopStmtImpl implements WhileStmt
{

public
WhileStmtImpl( HirRoot pHirRoot, Label pLoopBackLabel, Exp pCondition, 
      Stmt pLoopBody, Label pLoopStepLabel, Label pLoopEndLabel ) 
{
  super(pHirRoot); 
  fOperator = HIR.OP_WHILE;
  setChildrenOfLoop(null, pLoopBackLabel, null, pCondition, pLoopBody,
                    pLoopStepLabel, null, null, pLoopEndLabel,
                    null); 
  fType = hirRoot.symRoot.typeVoid; 
} // WhileStmtImpl

public void
accept( HirVisitor pVisitor )
{
  pVisitor.atWhileStmt(this);
}

} // WhileStmtImpl class
