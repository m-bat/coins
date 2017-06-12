/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * AliasAnal.java
 *
 * Created on June 17, 2003, 1:25 PM
 */

package coins.alias;

import coins.ir.hir.Exp;
import coins.ir.hir.SubpDefinition;

/**
 * <p>Interface for HIR alias analysis.
 * All future alias analysis classes will implement this interface.</p>
 * <p>The query interfaces mayAlias, mustAlais, and getAliasGroupFor
 * all deal with lvalues. An lvalue is an HIR node that is
 * associated with an object, and its operator code
 * (see {Link coins.ir.hir.HIR#getOperator}) is one
 * of <code>HIR.OP_VAR</code>, <code>HIR.OP_SUBS</code>,
 * <code>HIR.OP_QUAL</code>, <code>HIR.OP_ARROW</code>,
 * <code>HIR.OP_CONTENTS</code>, and <code>HIR.OP_UNDECAY</code>.
 * HIR nodes with the operator code HIR.OP_SUBS or HIR.OP_QUAL,
 * however, are lvalue only when their first child
 * (specified by {Link coins.ir.hir.SubscriptedExp#getArrayExp}
 *  and {Link coins.ir.hir.Exp#getQualifierExp}, respectively)
 * is lvalue. To see if an Exp node is lvalue, the isLvalue
 *  method may be used.</p>
 *
 * @author  hasegawa
 */
public interface AliasAnal
{

  /**
   * The category name of the alias analysis package.
   * It is "<code>Alias</code>". Used in debug-tracing.
   *
   */
  static final String CATEGORY_NAME = "Alias";

  /**
   * <p>Performs alias analysis for the given argument.
   * After calling this method, the alias analysis object
   * (object that implements this interface) is ready to
   * accept the queries (mayAlias etc.) for the specified
   * argument.</p>
     * <p>For intraprocedural analyses only (so far).</p>
   *
   * @param pSubpDef the <code>SubpDefinition</code> instance
   * nodes belonging to which are to be analyzed for aliasing.
   */
  void prepareForAliasAnalHir(SubpDefinition pSubpDef);

    /**
     * Returns true if the specified argument is considered
     * lvalue in this alias analysis.
     *
     * @return true if the specified argument is lvalue.
     */
    boolean isLvalue(Exp pExp);

  /**
   * Returns <code>true</code> if the two arguments <em>may</em>
   * refer to an overlapping area in memory. The two arguments
   * both must be contained in the <code>SubpDefinition</code>
   * which was analyzed most recently by this analysis instance.
   *
   * @param pExp lvalue node to check for aliasing.
   * @param pExp0 lvalue node to check for aliasing.
   * @return true if the two arguments <em>may</em> refer to an
   * overlapping area in memory.
   * @exception IllegalArgumentException if either of the arguments
   * is not lvalue.
   */
  boolean mayAlias(Exp pExp, Exp pExp0);

  /**
   * Returns <code>true</code> if the two arguments
   * <em>definitely</em> refer to an overlapping area in memory.
   * The two arguments both must be contained in the
   * <code>SubpDefinition</code> which was analyzed most
   * recently by this analysis instance.
   *
   * @param pExp lvalue node to check for aliasing.
   * @param pExp0 lvalue node to check for aliasing.
   * @return true if the two arguments <em>definitely</em>
   * refer to an overlapping area in memory.
   * @exception IllegalArgumentException if either of the
   * arguments is not lvalue.
   */
  boolean mustAlias(Exp pExp, Exp pExp0);

  /**
   * Returns the set of lvalue nodes that are possibly
   * aliased (<code>mayAlias</code>) to the specified argument.
   * The argument must be contained in the
   * <code>SubpDefinition</code> which was analyzed most recently
   * by this analysis instance.
   *
   * @param pExp lvalue node to check for aliasing.
   * @return the set of lvalue nodes that mayAlias to
   * the specified argument.
   * @exception IllegalArgumentException if the argument
   * is not lvalue.
   * See #mayAlias
   */
  AliasGroup getAliasGroupFor(Exp pExp);

  /**
   * Prints out alias pairs in <code>IoRoot.printOut</code> object.
   * For debugging.
   *
   * @param pSubpDef the <code>SubpDefinition</code> object for
   * which alias pairs will be printed.
   */
  void printAliasPairs(SubpDefinition pSubpDef);


}

