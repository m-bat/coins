/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;

import coins.ir.IR;
import coins.ir.IrList;
//##63 import coins.aflow.DefUseList;

/** FlowAnalSym interface
 *  Symbol for flow analysis such as
 *    Var, Reg, ExpId.
 * Note: Information for flow analysis should stem
 * from subpFlow so that resetFlowAnalInf is
 * executed without trouble (To be improved). //##94
**/

public interface
FlowAnalSym extends Sym {

/** getDefUseList Get DefUseList (list of definition points and
 *                 use points) of this symbol.
 *  setDefUseList Set DefUseList of this symbol.
 *  @param pDefUseList DefUseList to be set to this symbol.
 *  @return the DefUseList of this symbol if it is computed.
 *      If it is not yet computed, return null.
 *  @note It is assumed that attachFlowRoot of SymRoot have
 *      already been executed before calling getDefUseList,
 *      setDefUseList.
**/
//##63 coins.aflow.DefUseList getDefUseList();
//##63 void setDefUseList( coins.aflow.DefUseList pDefUseList );

/** addDefPoint
 *  Add definition point of this symbol to its definition list.
 *  If the definition list is not yet created, create it.
 *  The definition list can be get by getDefList.
 *  @param pDefNode IR node that defines the value of this symbol.
**/
//##63 void
//##63 addDefPoint( IR pDefNode );

/** addUsePoint
 *  Add use point of this symbol to its use list.
 *  If the use list is not yet created, create it.
 *  The use list can be get by getUseList.
 *  @param pDefNode IR node that uses this symbol.
**/
//##63 void
//##63 addUsePoint( IR pUseNode );

/** getDefList
 *  Get the definition list of this symbol.
 *  The list contains the IR nodes that define the value
 *  of this symbol.
 *  @return definition list of this symbol.
**/
//##63 IrList
//##63 getDefList();

/** getUseList
 *  Get the use list of this symbol.
 *  The list contains the IR nodes that uses this symbol.
 *  @return use list of this symbol.
**/
//##63 IrList
//##63 getUseList();


/** getIndex
 *  Get the index number assigned to the symbol.
 *  The index number is used in data flow analysis, etc.
 *  For ExpId, do not use setIndex but use setLinkedNode.
 *      If no index number is assigned to this symbol, return 0.
 * @return the index number assigned to the symbol.
**/
int  getIndex();

/** setIndex
 *  Set the index number assigned to the symbol.
 *  The index number is used in data flow analysis, etc.
 *  For ExpId, do not use setIndex but use setLinkedNode.
 *  @param pIndex index number to be set to this symbol.
**/
void setIndex( int pIndex);

/** resetFlowAnalInf
 *  Reset information for flow analysis, that is,
 *  nullify DefUseList, definition list, use list, ExpId,
 *  and reset the index value to 0.
**/
void
resetFlowAnalInf();

/** getOperandSet
 *  Get the set of operands used in computing the value
 *  of this abstract register. The operands are either
 *  Var or Reg, excluding Const (Const is not FlowAnalSym).
 *  @return the set of operands.
**/
public java.util.Set
    getOperandSet();

/** addOperand
 *  Add pOperand as a member of operand set.
 *  @param pOperand Var or Reg as an operand to be added.
**/
//##63 public void
//##63   addOperand( FlowAnalSym pOperand );

} // FlowAnalSym interface


