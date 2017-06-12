/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.flow;

import coins.ir.IR;
import coins.sym.FlowAnalSym;

import java.util.List; //##63

/** DefUseList interface
 *  DefUseList is used to map a def-node to corresponding DefUseChain.
 *  An instance of DefUseChain contains one def-node defining the value of
 *  a symbol and the list of use-nodes refering the symbol.
 *  An instance of DefUseList created by
 *     new DefUseListImpl(flowRoot, lSym)
 *  maps def-node defining lSym to corresponding DefUseChain, where
 *  lSym usually represents a variable.
 *    FlowAnalSym
 *    --> DefUSeList
 *        --> DefUseChain
 *            --> def-node defining the value of the symbol
 *            --> list of use-nodes refering the symbol
 *        --> DefUseChain
 *        ...
 *  An instance of DefUseList created by
 *     new DefUseListImpl(flowRoot)
 *  maps def-node to corresponding DefUseChain, where the def-node
 *  may not be restricted to define one paticular symbol, for example,
 *  the instance may map all def-nodes in a subprogram to corresponding
 *  DefUseChain irrespective of symbols.
 **/
public interface
DefUseList
{

  /** addDefUseChain
   *  Add DefUseChain instance which will be created by pDefNode that
   *  defines some symbol.
   **/
  public DefUseChain
    addDefUseChain(IR pDefNode);

  /** getDefUseChain
   *  Get DefUseChain having pDefNode as its define node in this DefUseList.
   * @param pDefNode definition node of a DefUseChain.
   * @return DefUseChain having pDefNode as its define node.
   **/
  public DefUseChain
    getDefUseChain(IR pDefNode);

  /** getDefUseChainList
   *  Get the list of Definition nodes in this DefUseList.    //##92
   * @return the list of definition-nodes in this DefUseList. //##92
   **/
public List
  getDefUseChainList();

/**
 *  Get the list of DefUseChains having pSym as its define node symbol.
 * @param pSym definition node symbol of DefUseChain.
 * @return List of DefUseChains having pSym as its define node symbol.
 **/
public List
  getDefUseChainListOfSym( FlowAnalSym pSym ); //##70

//##63  public DefUseChain
//##63    getDefUseChain( FlowAnalSym pSym );

  /** print
   *  Print this DefUseList.
   **/
  public void
    print();

} // DefUseList interface
