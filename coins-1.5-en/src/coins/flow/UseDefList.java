/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.flow;

import coins.ir.IR;
import coins.sym.FlowAnalSym;

/** UseDefList interface
 *  Use-def list representaing list of use nodes of a symbol
 *  where each use node has list of its definition points.
 *  UseDefList will be accessed from Var, Reg, ExpId, etc.
 **/
public interface
  UseDefList
{

  /** addUseDefChain
   *  Add UseDefChain which will be created by pUseNode that
   *  uses some symbol.
   **/
  public UseDefChain
    addUseDefChain(IR pUseNode);

  /** getUseDefChain
   *  Get UseDefChain having pUseNode as its use node.
   **/
  public UseDefChain
    getUseDefChain(IR pUseNode);

  /** print
   *  Print this DefUseList.
   **/
  public void
    print();

  public String
    toString();

  public String
    toStringByName();

} // UseDefList interface
