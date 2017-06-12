/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.ir.IrList;

//========================================//
//                            (##5): modified on Apr. 2001.

/**
 * FunctionExp
 *    Function call interface.
**/
public interface
FunctionExp extends Exp {

/** getFunctionSpec
 *  Get the function specification part of the subprogram call Exp.
 *  It may be a SubpNode or an expression poionting to a subprogram.
 *  @return a SubpNode or an expression poionting to a subprogram.
**/
public Exp
getFunctionSpec();

/** getFunctionNode
<PRE>
 *  Get the function node of the subprogram call Exp.
 *  If the function specification part is SubpNode then return it
 *  else if it is an addr exp to a subprogram such as
 *  (addr (subp ....))
 *  then return the SubpNode.
 *  If the function specification part is a function pointer
 *  expression such as
 *    (contents (var ....))
 *  showing no definite subprogram statically, then return null.
</PRE>
 *  @return the SubpNode or null.
**/
public SubpNode
getFunctionNode();

/** setFunctionSpec
 *  Set the function specification part of the subprogram call Exp.
 *  @param pSubpExp may be a SubpNode or pointer to a subprogram.
**/
public void
setFunctionSpec(Exp pSubpExp);

/** getParamList
 *  @return the parameter list part of the subprogram call Exp.
 *      return null if there is no parameter.
**/
public IrList
getParamList();

} // FunctionExp
