/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.flow;

import coins.ir.hir.Stmt; //##60

//      ( ): modified on Jan  2002.

/** BBlockHir interface
 *  Interface for HIR basic block.
 *
 **/
public interface
  BBlockHir
  extends BBlock
{

//##25 BEGIN

  public Stmt
    getFirstStmt();

  public Stmt
    getLastStmt();

//##25 END

} // BBlockHir interface
