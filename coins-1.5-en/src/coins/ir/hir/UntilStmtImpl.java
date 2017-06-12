/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.HirRoot;
import coins.sym.Label;

/**
 *  Until-statement (do-while, repeat-until) class.
**/
public class
UntilStmtImpl extends LoopStmtImpl implements UntilStmt
{

/** Constructor */
public
UntilStmtImpl( HirRoot pHirRoot, Label pLoopBackLabel, Stmt pLoopBody, 
               Label pLoopStepLabel, Exp pCondition,
               Label pLoopEndLabel )
{
  super(pHirRoot); 
  fOperator = HIR.OP_UNTIL;
  setChildrenOfLoop(null, pLoopBackLabel, null, null, pLoopBody,
                    pLoopStepLabel, pCondition, null, pLoopEndLabel,
                    null); 
  fType = hirRoot.symRoot.typeVoid; 
} // UntilStmtImpl

public void
accept( HirVisitor pVisitor )
{
  pVisitor.atUntilStmt(this);
}

} // UntilStmtImpl class
