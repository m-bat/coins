/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.flow;

import coins.ir.IR;

/** FlowIrLinkCell interface
 *
 *  Flow analysis and IR link information cell.
 *  Every IR node corresponds to a FlowIrLinkCell.
 **/
public interface
  FlowIrLinkCell
{

  public IR
    getIrNode();

  public int
    getDefRefPosition();

  public void
    setDefRefPosition(int pDefRefPosition);

  /** getBBlock Get basic block containing the node corresponding
   *     to this cell.
   *  setBBlock Set BBlock corresponding to this cell.
   **/
  public BBlock getBBlock();

  public void setBBlock(BBlock pBBlock);

  public int
    getHashCode();

  public void
    setHashCode(int pHashCode);

} // FlowIrLinkCell interface
