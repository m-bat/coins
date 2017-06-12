/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

import coins.ir.hir.Exp;

public interface Node {
  public void print(int level, String spaces);
  public String toString();
  public Exp makeExp();
  public Exp makeArgAddr(FStmt pFStmt);
}
