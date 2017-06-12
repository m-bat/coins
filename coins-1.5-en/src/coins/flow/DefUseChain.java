/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.flow;

import coins.ir.IR;
import coins.ir.IrList;
import java.util.List; //##63

/** DefUse Chain interface
 *
 *  Def-use list cell representaing
 *  a definition and list of its use points.
 **/
public interface
  DefUseChain
{

  public IR
    getDefNode();

  //##63 public IrList
  public List //##63
    getUseList();

  public void
    addUseNode(IR pUseNode);

  public String
    toStringByName();

} // DefUseChain interface
