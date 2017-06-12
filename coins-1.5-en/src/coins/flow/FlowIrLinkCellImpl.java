/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.flow;

import coins.ir.IR;

/** FlowIrLinkCellImpl class
 *
 *  Flow analysis and IR link information cell.
 *  Every IR node corresponds to a FlowIrLinkCell.
 **/
public class
  FlowIrLinkCellImpl
  implements FlowIrLinkCell
{

//====== Fields ======

  protected IR
    fIrNode = null; // IR node corresponding to this cell.

  protected int
    fDefRefPosition = 0; // Symbol definition/reference point number
  // corresponding to this cell.
  // 0 if the cell does neither set nor refer
  // a symbol directly.

  protected BBlock // BBlock including the node
    fBBlock = null; // corresponding to this cell.

  private int
    fHashCode = 0;

//====== Constructors ======

  public
    FlowIrLinkCellImpl(IR pIrNode, BBlock pBBlock, int pDefRefNumber)
  {
    fIrNode = pIrNode;
    fDefRefPosition = pDefRefNumber;
    fBBlock = pBBlock;
  }

//====== Methods ======

  public IR
    getIrNode()
  {
    return fIrNode;
  }

  public int
    getDefRefPosition()
  {
    return fDefRefPosition;
  }

  public void
    setDefRefPosition(int pDefRefPosition)
  {
    fDefRefPosition = pDefRefPosition;
  }

  public BBlock
    getBBlock()
  {
    return fBBlock;
  }

  public void
    setBBlock(BBlock pBBlock)
  {
    fBBlock = pBBlock;
  }

  public int
    getHashCode()
  {
    return fHashCode;
  }

  public void
    setHashCode(int pHashCode)
  {
    fHashCode = pHashCode;
  }

} // FlowIrLinkCellImpl class
