/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.flow;

import coins.ir.IR;
import coins.ir.IrList;
import java.util.List; //##63

/** UseDefChain interface
 *
 *  Use-def list chain representaing
 *  a use node and list of its definition points.
 **/
public interface
  UseDefChain
{

  public IR
    getUseNode();

  //##63 public IrList
  public List //##63
    getDefList();

  public void
    addDefNode(IR pDefNode);

  public String
    toStringByName();

} // UseDefChain interface
