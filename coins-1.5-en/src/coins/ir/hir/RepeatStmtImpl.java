/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.HirRoot;
import coins.sym.Label;

/**
 *  Repeat-statement (do-while, repeat-until) class. 
**/
public class
RepeatStmtImpl extends LoopStmtImpl implements RepeatStmt
{

/** Constructor */
public
RepeatStmtImpl( HirRoot pHirRoot, Label pLoopBackLabel, Stmt pLoopBody, 
               Label pLoopStepLabel, Exp pCondition,
               Label pLoopEndLabel )
{
  super(pHirRoot); 
  fOperator = HIR.OP_REPEAT;
  setChildrenOfLoop(null, pLoopBackLabel, null, null, pLoopBody,
                    pLoopStepLabel, pCondition, null, pLoopEndLabel,
                    null); 
  fType = hirRoot.symRoot.typeVoid; 
} // RepeatStmtImpl

public void
accept( HirVisitor pVisitor )
{
  pVisitor.atRepeatStmt(this);
}

} // RepeatStmtImpl class
