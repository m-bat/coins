/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.ana;

import coins.backend.LocalAnalysis;
import coins.backend.cfg.BasicBlk;
import coins.backend.sym.Symbol;
import coins.backend.util.BiList;
import coins.backend.util.NumberSet;


/** Interface of live variable analysis. **/
public interface LiveVariableAnalysis extends LocalAnalysis {
  /** Return true if variable regvar is live at entry of blk. **/
  boolean isLiveAtEntry(Symbol regvar, BasicBlk blk);

  /** Return true if variable regvar is live at entry of blk. **/
  boolean isLiveAtEntry(int regvar, BasicBlk blk);

  /** Return true if variable regvar is live at exit of blk. **/
  boolean isLiveAtExit(Symbol regvar, BasicBlk blk);

  /** Return true if variable regvar is live at exit of blk. **/
  boolean isLiveAtExit(int regvar, BasicBlk blk);

  /** Return the list of live variables at exit of basic block blk. **/
  BiList liveOut(BasicBlk blk);

  /** Return the list of live variables at entry of basic block blk. **/
  BiList liveIn(BasicBlk blk);

  /** Return set of live variable numbers at entry of basic block. **/
  NumberSet liveInSet(BasicBlk blk);

  /** Return set of live variable numbers at exit of basic block. **/
  NumberSet liveOutSet(BasicBlk blk);

  /** Copy set of live variable numbers at exit of block blk to NumberSet x. **/
  void getLiveOutSet(NumberSet x, BasicBlk blk);

  /** Add set of live variable numbers at exit of block blk to NumberSet x. **/
  void addLiveOutSet(NumberSet x, BasicBlk blk);

  /** Copy set of live variable numbers at entry to NumberSet x. **/
  void getLiveInSet(NumberSet x, BasicBlk blk);

  /** Add set of live variable numbers at entry to NumberSet x. **/
  void addLiveInSet(NumberSet x, BasicBlk blk);
}

