/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

/** SetDataStmt interface
<PRE>
 * Specify initial values in the form
 *    (setData l-value valueSpec)
 * where, l-value is an expression such as variable representing the storage
 * area where initial value is to be set, and valueSpec is a constant expression
 * representing initial values to be set.
 * The setData is used in Initiation part of Program node and SubpDefinition node.
</PRE>
**/
public interface
SetDataStmt extends Stmt
{

/** getLeftSide
 * @return l-value expression such as variable representing the storage area
 *     where initial value (valueSpec) is to be set.
 */
public Exp getLeftSide();

/** getRightSide
 * @return value specification expression (valueSpec) given as initial value
 *   to be set to the storage area represented by getLeftSide().
 */
public Exp getRightSide();

/** setLeftSide
 * Give the l-value expression for this SetDataStmt.
 * @param pLeftSide l-value constant expression such as variable
 *          (of scalar, array, struct, union).
 * @param pRightSide value specification expression (valueSpec), that is either
 *        constant, list of constants, (OP_EXPREPEAT valueSpec repeatCount).
 */
public void setLeftSide(Exp pLeftSide);

/** setRightSide
 * Give the value specification expression (valueSpec) for this SetDataStmt.
 * @param pRightSide value specification expression (valueSpec), that is either
 *        constant, list of constants, (OP_EXPREPEAT valueSpec repeatCount).
 */
public void setRightSide(Exp pRightSide);

}
