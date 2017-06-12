/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;


//========================================//
//                        Jan. 19, 2001.
//                            (##2): modified on Nov. 2000.
//                            (##3): modified on Jan. 2001.


public interface
ReturnStmt extends Stmt
{

/** ReturnStmt           (##3)
 *  Build return statement that terminates the execution of
 *      current subprogram under construction.
 *  @param pReturnValue return value of function subprogram.
 *      If the subprogram has no return value, it is null.
 *  @return the subtree of the built statement
 *      with statement body operator opReturn.
 **/
// Constructor      (##3)
// ReturnStmt( Exp pReturnValue );

/** getReturnValue Get the return value expression of return statement.
 *  setReturnValue Set the return value expression of this statement.
 *  "this" should be a return statement build by buildReturnStmt.
**/
Exp
getReturnValue();
public void
setReturnValue(Exp pReturnValue);


} // ReturnStmt interface
