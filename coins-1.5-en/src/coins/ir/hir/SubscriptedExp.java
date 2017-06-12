/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;


/**
 *
 * Subscripted variable interface.
 *
**/
public interface
SubscriptedExp extends Exp {

  /** getArrayExp
   *  Get the array expression part of this subscripted expression.
   *  In case of using it to construct new expression, copyWithOperands()
   *  is recommended to be applied to the result.
   *  @return the array expression part of this subscripted expression.
   */
public Exp
getArrayExp();

  /** getSubscriptExp
   *  Get the subscript expression part of this subscripted expression.
   *  In case of using it to construct new expression, copyWithOperands()
   *  is recommended to be applied to the result.
   *  @return the subscript expression part of this subscripted expression.
   */
public Exp
getSubscriptExp();

  /** getElemSizeExp
   *  Get the element size expression for this subscripted expression.
   *  In case of using it to construct new expression, copyWithOperands()
   *  is recommended to be applied to the result.
   *  @return the element size expression for this subscripted expression.
   */
public Exp
getElemSizeExp();

} // SubscriptedExp interface
