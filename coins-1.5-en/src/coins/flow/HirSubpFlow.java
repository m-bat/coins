/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.flow;

import coins.ir.hir.HIR;
import coins.sym.ExpId; //##60

/** HirSubpFlow interface
 *
 *  HIR subprogram flow analysis interface.
 **/
public interface
  HirSubpFlow
  extends SubpFlow
{

//====== Methods to correlate HIR with index & ExpId ======

  /** divideHirIntoBasicBlocks
   *  Divide the current subprogram into basic block,
   *  create BBlock and make link to HIR node,
   *  after
   *  resetting FlowSymLink, reset LabelLink,
   *  setting index to each node,
   *  recording the symbols actually used in the current
   *    subprogram setting index to the symbols and .
   *  making label reference list for labels that are explicitly
   *   refered as jump target in JumpStmt and SwitchStmt
   *   (IfStmt, LoopStmt implicitly refer labels but they are not listed),
   *  create FlowIrLinkCell,
   * @return false if failed (by bad node index), otherwise return true. //##78
   **/
  //##78 public void
  public boolean  //##78
    divideHirIntoBasicBlocks();

  public void
    divideLirIntoBasicBlocks();

  /** allocateExpIdForSubp
   *  For each HIR node of current subprogram,
   *  allocate ExpId to the node if it does computation directly,
   *  and record DefRefPosition number for the node.
   *  If two subtrees have statically the same form and type,
   *  then they are allocated the same ExpId.
   **/
  public void
    allocateExpIdForSubp();

} // HirSubpFlow interface
