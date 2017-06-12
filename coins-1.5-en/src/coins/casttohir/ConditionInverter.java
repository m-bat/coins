/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.casttohir;

import coins.ir.hir.Exp;
import coins.ir.hir.HIR;

/** ConditionInverter
 * Invert comparison operator in condition expression
 * according to De Morgan rule.
 *
 * @author  Shuichi Fukuda
**/
public class ConditionInverter extends ToHirVisit
{
  private final HIR hir;

  //-------------------------------------------------------------------
  /** ConditionInverter
   * Constructor.
   *
   * @param  tohir Offers cooperation with the object of other packages.
  **/
  public ConditionInverter(ToHir tohir)
  {
    super(tohir);
    hir = tohir.hir;
  }
  //-------------------------------------------------------------------
  // expression converter
  //-------------------------------------------------------------------
  /** atCmpEq
   * At EQ expression node.
   *
   * @param   e Expression to be visited.
   * @return  Resultant expression.
  **/
  protected Exp atCmpEq(Exp e) // ==
  {
    return hir.exp( HIR.OP_CMP_NE, e.getExp1(), e.getExp2() );
  }
  //-------------------------------------------------------------------
  /** atCmpNe
   * At NE expression node.
   *
   * @param   e Expression to be visited.
   * @return  Resultant expression.
  **/
  protected Exp atCmpNe(Exp e) // !=
  {
    return hir.exp( HIR.OP_CMP_EQ, e.getExp1(), e.getExp2() );
  }
  //-------------------------------------------------------------------
  /** atCmpGt
   * At GT expression node.
   *
   * @param   e Expression to be visited.
   * @return  Resultant expression.
  **/
  protected Exp atCmpGt(Exp e) // >
  {
    return hir.exp( HIR.OP_CMP_LE, e.getExp1(), e.getExp2() );
  }
  //-------------------------------------------------------------------
  /** atCmpGe
   * At GE expression node.
   *
   * @param   e Expression to be visited.
   * @return  Resultant expression.
  **/
  protected Exp atCmpGe(Exp e) // >=
  {
    return hir.exp( HIR.OP_CMP_LT, e.getExp1(), e.getExp2() );
  }
  //-------------------------------------------------------------------
  /** atCmpLt
   * At LT expression node.
   *
   * @param   e Expression to be visited.
   * @return  Resultant expression.
  **/
  protected Exp atCmpLt(Exp e) // <
  {
    return hir.exp( HIR.OP_CMP_GE, e.getExp1(), e.getExp2() );
  }
  //-------------------------------------------------------------------
  /** atCmpLe
   * At LE expression node.
   *
   * @param   e Expression to be visited.
   * @return  Resultant expression.
  **/
  protected Exp atCmpLe(Exp e) // <=
  {
    return hir.exp( HIR.OP_CMP_GT, e.getExp1(), e.getExp2() );
  }
  //-------------------------------------------------------------------
  /** atLgAnd
   * At logical AND expression node.
   *
   * @param   e Expression to be visited.
   * @return  Resultant expression.
  **/
  protected Exp atLgAnd(Exp e) // &&
  {
    return hir.exp( HIR.OP_LG_OR, visitExp(e.getExp1()), visitExp(e.getExp2()) );
  }
  //-------------------------------------------------------------------
  /** atLgOr
   * At logical OR expression node.
   *
   * @param   e Expression to be visited.
   * @return  Resultant expression.
  **/
  protected Exp atLgOr(Exp e) // ||
  {
    return hir.exp( HIR.OP_LG_AND, visitExp(e.getExp1()), visitExp(e.getExp2()) );
  }
  //-------------------------------------------------------------------
  /** atEqZero
   * At logical NOT expression node.
   *
   * @param   e Expression to be visited.
   * @return  Resultant expression.
  **/
  protected Exp atEqZero(Exp e) // !
  {
    return e.getExp1();
  }
  //-------------------------------------------------------------------
}
