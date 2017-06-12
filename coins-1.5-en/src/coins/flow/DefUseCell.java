/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.flow;

import coins.ir.IR;
import coins.ir.IrList;

/** DefUseCell interface
 *
 *  Def-use list cell representaing
 *  a definition and list of its use points.
 **/
public interface
  DefUseCell
{

  public IR
    getDefNode();

  public IrList
    getUseList();

  public void
    addUseNode(IR pUseNode);

} // DefUseCell interface
