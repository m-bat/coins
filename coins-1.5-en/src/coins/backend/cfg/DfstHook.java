/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.cfg;

/** Depth First Ordering Hook */
public interface DfstHook {

  /** Entry called when the block blk first visited.
   *  Block from is the parent node. */
  void preOrder(BasicBlk blk, BasicBlk from);

  /** Entry called when the block blk last visited. */
  void postOrder(BasicBlk blk);
}
