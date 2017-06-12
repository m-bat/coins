/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.HirRoot;

/** SetDataStmtImpl class
 * Specify initial values in the form
 *    (setData l-value valueSpec)
**/

public class SetDataStmtImpl extends StmtImpl implements SetDataStmt //SF040525
{

/** SetDataStmtImpl
 * A constructor to make an instance of DataCodeStmt.
 * The type of pLeftSide and pRightSide should be the same.
 * @param pHirRoot HirRoot instance.
 * @param pLeftSide l-value constant expression such as variable
 *          (of scalar, array, struct, union).
 * @param pRightSide value specification expression (valueSpec), that is either
 *        constant, list of constants, (OP_EXPREPEAT valueSpec repeatCount).
 */
public SetDataStmtImpl( HirRoot pHirRoot, Exp pLeftSide, Exp pRightSide ) //SF040525
{
  super(pHirRoot, HIR.OP_SETDATA);
  fChildCount = 2;
  setChildren(pLeftSide, pRightSide);
  if (pLeftSide != null)          
    setType(pLeftSide.getType()); 
}
public Exp getLeftSide()
{
  return (Exp)fChildNode1;
}
public Exp getRightSide()
{
  return (Exp)fChildNode2;
}
public void setLeftSide(Exp e)
{
  fChildNode1 = e;
  if( e!=null )
    e.setParent(this);
}
public void setRightSide(Exp e)
{
  fChildNode2 = e;
  if( e!=null )
    e.setParent(this);
}
}
