/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;

import coins.ir.IR;
import coins.ir.hir.HIR; //##65
import coins.aflow.FlowExpId;
import coins.flow.ExpInf; //##60
import coins.flow.SetRefRepr; //##63
import java.util.Set; //##60

/** class ExpId
 * ExpId (expression identifier) is used to identify expressions.
 * Expressions having the same form and type have the
 * same ExpId.
 */

public interface
ExpId extends /* OperandSym,*/ FlowAnalSym
{

/** getLinkedNode
 *  Get the IR node for which this ExpId is first created.
 *  It may be affected by some optimization after the ExpId assignment.
 *  There may be other IR nodes with the same subtree structure as the it.
 *  @return the IR node corresponding to this ExpId.
**/
public IR
getLinkedNode();

//##65 BEGIN
/** getCopiedExp
 *  Get the expression for which this ExpId is first created.
 *  It is not affected by optimization after the ExpId assignment.
 *  @return the IR node corresponding to this ExpId.
**/
public HIR
getCopiedExp();
//##65 END
/** getLinkedSym
 *  If the linked node of this ExpId has symbol connected to it,
 *  then return it, else return null.
 *  @return the corresponding symbol or return null.
**/
public Sym
getLinkedSym();

/** getNextId Get the next ExpId in the hash chain.
 *  @return the next ExpId in the hash chain.
**/
public ExpId
getNextId();

/** setNextId Set the next ExpId in the hash chain.
 *  @param pNextId the next ExpId in the hash chain.
**/
public void
setNextId( ExpId pNextId );

/** getFlowExpId
 *  Get the FlowExpId that is used in aflow.
 *  @return FlowExpId.
**/
public FlowExpId
getFlowExpId();

/** setFlowExpId
 *  Set the FlowExpId that is used in aflow.
 *  @param pFlowExpId corresponding FlowExpId instance.
**/
public void
setFlowExpId( FlowExpId pFlowExpId );

//##60 BEGIN Moved from ExpIdE

public ExpInf
getExpInf();

public void
setExpInf( ExpInf pExpInf );

/**
 * Returns the set of FlowAnalSyms that are operands of this FlowExpId.
 *  An operand of a FlowExpId is simply a FlowAnalSym that is
 * attached to one of the nodes that comprise the tree represented
 * by the FlowExpId.
 */
Set getOperandSet();

/**
 * Returns the set of FlowAnalSyms that are operands of this FlowExpId,
 * and that hold the value that may contribute to the result of
 * the computation of this FlowExpId. For example, in HIR,
 * if a symbol node is operated by the addressOf operator,
 * and there is no contentsOf operator operating at all afterwards,
 * the symbol attached to the symbol node is not included
 * in the set returned by this method.
 */
Set getOperandSet0();

/**
 * Returns the number of operations this FlowExpId involves.
 * This is not more than but roughly equal to the number of this FlowExpId's
 * linked node's descendant nodes.
 */
int getNumberOfOperations();

public boolean isLHS();

//##60 END

/**
<PRE>
 * Set SetRefRepr information that is used in
 * data flow analysis for the expression corresponding
 * to this ExpId.
 * The SetRefRepr represents such information as
 *   set of symbols refered by the expression/statement
 *   set of symbols modified by the expression/statement
 *   ...
</PRE>
 * @param pSetRefRepr information to be set.
 */
public void
setSetRefRepr( SetRefRepr pSetRefRepr ); //##63

/**
 <PRE>
  * Get SetRefRepr information that is used in
  * data flow analysis for the expression corresponding
  * to this ExpId.
  * The SetRefRepr represents such information as
  *   set of symbols refered by the expression/statement
  *   set of symbols modified by the expression/statement
  *   ...
 </PRE>
 * @return the corresponding SetRefRepr instance.
 */
public SetRefRepr
getSetRefRepr(); //##63

} // ExpId
