/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend;

import coins.backend.cfg.BasicBlk;
import coins.backend.lir.LirNode;
import java.io.PrintWriter;


/** Interface for result of function analysis. */
public interface LocalAnalysis {

  /** Return true if this analysis is up to date. */
  boolean isUpToDate();

  /** Callback entries for debugging */

  /** Debug print Called before function body. **/
  void printBeforeFunction(PrintWriter output);

  /** Called before each Basic Block. */
  void printBeforeBlock(BasicBlk blk, PrintWriter output);

  /** Called after each Basic Block. */
  void printAfterBlock(BasicBlk blk, PrintWriter output);

  /** Called before each statement. **/
  void printBeforeStmt(LirNode stmt, PrintWriter output);

  /** Called after each statement. **/
  void printAfterStmt(LirNode stmt, PrintWriter output);

  /** Called after function body. **/
  void printAfterFunction(PrintWriter output);
}
