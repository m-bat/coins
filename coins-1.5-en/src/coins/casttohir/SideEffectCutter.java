/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.casttohir;

import coins.ir.hir.Exp;
import coins.ir.hir.FunctionExp;
import coins.ir.hir.HIR;
import coins.ir.hir.VarNode;
import coins.sym.Sym;

/** SideEffectCutter
 * Cut out subexpression with side effect from given expression-statement
 * ExtStmt.
 *
 * @author  Shuichi Fukuda
**/
public class SideEffectCutter extends ToHirVisit
{
  /** toHir
   * Offers cooperation with the object of other packages.
  **/
  private final ToHir toHir;

  /** hir
   * HIR instance (used to create HIR objects).
  **/
  private final HIR hir;

  /** sym
   * Sym instance (used to create Sym objects).
  **/
  private final Sym sym;

  /** buffer
   * Side effect buffer.
  **/
  private final SideEffectBuffer buffer;

  //-------------------------------------------------------------------
  /** SideEffectCutter
   * Constructor.
   *
   * @param  tohir Offers cooperation with the object of other packages.
   * @param  buf Side effect buffer.
  **/
  public SideEffectCutter(ToHir tohir,SideEffectBuffer buf)
  {
    super(tohir);
    toHir  = tohir;
    hir    = tohir.hirRoot.hir;
    sym    = tohir.hirRoot.sym;
    buffer = buf;
  }
  //-------------------------------------------------------------------
  // expression converter
  //-------------------------------------------------------------------
  /**
   * At variable node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atVar(VarNode e)
  {
    if( e.getType().isVolatile() )
      buffer.addPrev( toHir.newExpStmt(e) );
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At member-access expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atQual(Exp e) // .
  {
    if( e.getType().isVolatile() )
      buffer.addPrev( toHir.newExpStmt(e) );
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At arrow expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atArrow(Exp e) // ->
  {
    if( e.getType().isVolatile() )
      buffer.addPrev( toHir.newExpStmt(e) );
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At function call expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atCall(FunctionExp e)
  {
    buffer.addPrev( toHir.newExpStmt(e) );
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At indirection expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atContents(Exp e) // *
  {
    if( e.getType().isVolatile() )
      buffer.addPrev( toHir.newExpStmt(e) );
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At assign expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atAssign(Exp e)
  {
    buffer.addPrev( toHir.newExpStmt(e) );
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At pre-operator expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atPre(int op,Exp e) // ++@ --@
  {
    buffer.addPrev( toHir.newExpStmt(e) );
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At post-operator expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atPost(int op,Exp e) // @++ @--
  {
    buffer.addPrev( toHir.newExpStmt(e) );
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At add-assign expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atAddAssign(Exp e)
  {
    buffer.addPrev( toHir.newExpStmt(e) );
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At sub-assign expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atSubAssign(Exp e)
  {
    buffer.addPrev( toHir.newExpStmt(e) );
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At mul-assign expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atMulAssign(Exp e)
  {
    buffer.addPrev( toHir.newExpStmt(e) );
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At div-assign expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atDivAssign(Exp e)
  {
    buffer.addPrev( toHir.newExpStmt(e) );
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At mod-assign expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atModAssign(Exp e)
  {
    buffer.addPrev( toHir.newExpStmt(e) );
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At L-shift-assign expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atLShiftAssign(Exp e)
  {
    buffer.addPrev( toHir.newExpStmt(e) );
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At R-shift-assign expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atRShiftAssign(Exp e)
  {
    buffer.addPrev( toHir.newExpStmt(e) );
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At and-assign expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atAndAssign(Exp e)
  {
    buffer.addPrev( toHir.newExpStmt(e) );
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At or-assign expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atOrAssign(Exp e)
  {
    buffer.addPrev( toHir.newExpStmt(e) );
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At xor-assign expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atXorAssign(Exp e)
  {
    buffer.addPrev( toHir.newExpStmt(e) );
    return e;
  }
  //-------------------------------------------------------------------
}
