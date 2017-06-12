/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.CompileError;
import coins.sym.Label;

/**
<PRE>
*  LoopStmt ->  // Loop statement is either for-loop, while-loop,
*               // repeat-loop, indexed loop, or other general loop.
*               // All of them are implemented as a general loop
*               // with some restriction depending on the loop type.
*               // Compiler components (other than front part) should
*               // treat general loop, that is, do not assume some child
*               // is null without checking whether the child is null
*               // or not.
*    ( LoopCode_ attr     // Loop kind code.
*      LoopInitPart_ @    // Loop initiation part to be executed
*                         // before repetition. This may be null.
*      ConditionalInitPart_ @ // Old conditional initiation part.
*                         // Give null for this item but use
*                         // addToConditionalInitPart method.
*      StartConditionPart_ @  // Loop start conditional expression
*                         // with loopBackLabel.
*                         // If true, pass through to LoopBody_,
*                         // otherwise transfer to LoopEndPart_.
*                         // If loop start condition part is null,
*                         //  pass through to LoopBody_.
*      LoopBody_ @        // Loop body repetitively executed.
*                         // Pass through to EndCondition_.
*                         // It is a block statement (BlockStmt)
*                         // with loop start label and the blcok
*                         // statement contains a labeled statement
*                         // with loopStepLabel as its last statement.
*                         // This should not be null but the block may
*                         // have no executable statement and contains
*                         // only a labeled statement with loopStepLabel.
*                         // "continue" jumps to the loopStepLabel.
*                         // The loopStepLabel may be omitted if
*                         // there is no "jump loopStepLabel".
*     EndCondition_ @     // Loop end condition expression.
*                         // If true, transfer to LoopEndPart_,
*                         // otherwise pass through to
*                         // LoopStepPart_.
*     LoopStepPart_ @     // Loop step part executed
*                         // before jumping to loopBackLabel.
*     LoopEndPart_ @ )    // Loop end part
*                         // with loopEndLabel.
*                         // "exit" (break in C) jumps to here.
*     IndexedLoop_ attr   // Attributes for IndexedLoop.
*                         // Not given for other loops.
*    // IndexRange_ @ )   // Index range may be null in general loop.
*  LoopCode_ attr ->
*     whileCode attr      // while-loop
*   | forCode attr        // for-loop
*   | repeatCode attr      // repeat-loop
*   | indexedLoopCode attr// indexed-loop
*   | loopCode attr       // general loop other than above loops.
*  LoopInitPart_   ->  // Loop initiation part.
*     Stmt
*   | null
*  ConditionalInitPart_ ->  // Executed at first time only.
*     Stmt              // Semantics of this is the same
*   | null              // to the following if-statement with label:
*                       //   if (exp_of_StartCondition_) {
*                       //     statement of ConditionalInitPart_;
*                       //     goto loopStartlabel of LoopBody_;
*                       //   }else
*                       //     goto loopEndlabel:
*                       // where, statement of ConditionalInitPart_
*                       // is executed once if start condition
*                       // (exp_of_StartCondition_) is true.
*                       // Control transfers to LoobBody_
*                       // if the exp_of_StartCondition_ is true.
*  StartConditionPart_ ->      // Show start condition with
*     ( labeledStmtCode attr   // loopBacklabel.
*       LabelDefinitionList_ @
*       ExpStmtOrNull_ @ )     // loopStartConditionExpression.
*  LoopBody_  ->               // Block statement with loopBodyLabel.
*     ( labeledStmtCode attr   // The last statement of the block
*       LabelDefinitionList_ @ // is a LabeledNull_ statement with
*       BlockStmt_ @ )         // loopStepLabel.
*  EndCondition_ ->            // ExpStmt showing loop end condition.
*     ExpStmtOrNull_
*  LoopStepPart_  ->           // Statement to be executed before jumping
*     Stmt                     // to loopBackLabel.
*   | null
*  LoopEndPart_  ->            // LabeledNull_ statement with loopEndLabel.
*     LabeledNull_
*  NullOrStmt_ ->       // Usually null but it may be
*     null              // a statement (created during
*   | Stmt              // HIR transformation).
*  ExpStmtOrNull_ ->    // Expression statement or null.
*     ExpStmt
*   | null
*  IndexedLoop_ attr  -> // Attributes for IndexedLoop.
*     loopIndex attr     // Loop index (induction variable).
*                        // See getLoopIndex().
*     startValue attr    // Start value of the loop index.
*                        // See getStartValue().
*     endValue attr      // End value of the loop index.
*                        // See getEndValue().
*     stepValue attr     // Step value for the loop index.
*                        // See getStepValue().
*
*  // Note. LoopInf may contain goto-loop that is difficult or
*  //   impossible to be represent by above LoopStmt.
*  //   (goto-loop is not implemented by LoopStmt.)
*
 *  // LoopStmt is executed as follows:
 *  //   LoopInitPart_;
 *  //   if (loopStartConditionExpression == null) {
 *  //     Sequence of statements added by addToConditionalInitPart();
 *  //   }else {
 *  //     if (loopStartConditionExpression == false) {
 *  //       jump to loopEndLabel;
 *  //     }else { // ConditionalInitBlock
 *  //       Sequence of statements added by addToConditionalInitPart().
 *  //       jump to loopBodyLabel;
 *  //     }
 *  //   }
 *  //   loopBackLabel:
 *  //     if ((loopStartConditionExpression != null)&&
 *  //         (loopStartConditionExpression == false))
 *  //       jump to loopEndLabel;
 *  //   loopBodyLabel:
 *  //     Start of BlockStmt of LoopBody_
 *  //       Stastement sequence of the BlockStmt;
 *  //       (break statement jumps to loopEndLabel;)
 *  //       (continue statement jumps to loopStepLabel;)
 *  //       Rest of stastement sequence of the LoopBody_;
 *  //       loopStepLabel:;
 *  //     End of BlockStmt of LoopBody_
 *  //     if ((loopEndConditionExpression != null)&&
 *  //         (loopEndConditionExpression == false))
 *  //       jump to loopEndLabel;
 *  //     LoopStepPart;
 *  //     jump to loopBackLabel;
 *  //   loopEndLabel:
 *  //     Loop end part;
*
*  // BEGIN #21
*  // ForStmt is created as a general loop where contents of
*  //   ConditionalInitPart_, EndCondition_, LoopEndPart_
*  //   are null at first (but they may become not null
*  //   by some optimizing transformation).
*  //   See isSimpleForLoop().
*  // WhileStmt is created as a general loop where contents of
*  //   LoopInitPart_, ConditionalInitPart_, EndCondition_,
*  //   LoopStepPart_, LoopEndPart_
*  //   are null at first (but they may become not null
*  //   by some optimizing transformation).
*  //   See isSimpleWhileLoop().
*  // UntilStmt is created as a general loop where contents of
*  //   LoopInitPart, ConditionalInitPart_, StartCondition_,
*  //   LoopStepPart_, LoopEndPart_
*  //   are null at first (but they may become not null
*  //   by some optimizing transformation).
*  //   See isSimpleUntilLoop().
*  // IndexedLoopStmt is created as a general loop where contents of
*  //   ConditionalInitPart_, EndCondition_, LoopEndPart_
*  //   are null at first (but they may become not null
*  //   by some optimizing transformation).
*  //   See isSimpleIndexedLoop().
*  // IndexedLoopStmt represents a Fortran type loop where
*  //   value of loop index is incremented or decremented by loop
*  //   step value starting from loop start value and stops
*  //   to loop before crossing the barrier of loop end value.
*  //   (See IndexedLoopStmt interface.)
*  // Indexed loop attributes (IndexedLoopAttr_) are available
*  // only for IndexedLoopStmt.
*  // END #21
*
* // Components of loop statement:
//   child1: Initiation part.
//           This may be null.
//   child2: Conditional initiation part (may be null).
//           This is executed only once if loop condition is satisfied.
//           (At present, this is implemented by if-stmt attached to
//            LoopInitPart so that hir2lir has no need of
//            special treatment.
//            You should use addToConditionalInitPart(....)
//            without giving conditional init part in
//            new LoopStmtImpl(...). This restriction will be removed
//            when hir2lir supports ConditionalInitPart treatment.)
//   child3: Loop start condition part with loopBackLabel.
//           This should be given but its LabeledStmt may be null.
//   child4: Loop body part. It is a block statement (BlockStmt)
//           with loop start label and the blcok statement contains
//           a labeled statement with loopStepLabel as the last
//           statement of the block.
//           This should not be null but the block may have no
//           executable statement and contains only a labeled
//           statement with loopStepLabel.
//    child5: Loop end condition taking the form of ExpStmt.
//            If its statement body is null, control passes through
//            to LoopStep part.
//   child6: Loop step part jumping to loopBackLabel
//           This may be null.
//   child7: Loop end part with loopEndLabel
//           This should be given but its LabeledStmt may be null.
</PRE>
**/
public interface
LoopStmt extends Stmt {

//==== Get some part of LoopStmt ====//

/**
<PRE>
 * Get the loop initiation part that is executed before
 * repetition.
 * Ex.
 * For a loop statement
 *   for (i=0; i<n; i++) { ... }
 * the statement i=0; is returned.
 * This may be null.
</PRE>
 * @return the loop initiation part;
 */
public Stmt
getLoopInitPart();

/** getConditionalInitPart
 * Get the BlockStmt containing the statements added by
 * addToConditionalInitPart. If there is no such statements,
 * then return null.
 * @return ConditionalInitBlock containing statements added by
 *     addToConditionalInitPart.
**/
public BlockStmt
getConditionalInitPart();

/** getLoopStartCondition
<PRE>
 * Get the loop start conditional expression that has
 * the loopBackLabel.
 * For a loop statement
 *   for (i=0; i<n; i++) { ... }
 * the expression i<n is returned.
 * This may be null.
</PRE>
 * @return the loop start condition expression.
 */
public Exp
getLoopStartCondition();

/** getLoopBackPoint
 * Get the statement with loopBackLabel.
 * Even empty loop has a LabeledStmt with loopBackLabel
 * where its statement body may be null.
 * @return the statement with loopBackLabel.
**/
public LabeledStmt
getLoopBackPoint();

/** getLoopBodyPart
<PRE>
 * Get the loop body that is repetitively executed.
 * It is a block statement (BlockStmt)
 * with loop start label and the blcok
 * statement contains a labeled statement.
 * with loopStepLabel as its last statement.
 * For a statement
 *   for (i = 0; i < 10; i++)
 *     a[i] = 0;
 * a labeled statement
 *   _lab6: { a[i] = 0; }
 * will be returned where _lab6 is a loop start label
 * generated by the compiler.
 * For a statement
 *   while (i < 10) {
 *     a = a + b;
 *     if (a >= 10) {
 *       i = i + 1;
 *       continue;
 *     }
 *     i = i + 1;
 *   }
 * a labeled statement
 *   _lab10: {
 *     a = a + b;
 *     if (a >= 10) {
 *       i = i + 1;
 *       goto _lab3;
 *     }
 *     i = i + 1;
 *     _lab4:
 *   }
 * will be returned where _lab10 is the loop start label
 * and _lab4 is the loop step label generated by the compiler.
</PRE>
 * @return the loop body that is repetitively executed.
 */
Stmt
getLoopBodyPart();

/** getLoopEndCondition
<PRE>
 * Get the loop end conditional expression.
 * For a loop statement
 *   do { ... }
 *   while (i>0);
 * the expression i>0 is returned.
 * For a loop statement
 *   for (i=0; i<n; i++) { ... }
 * null is returned.
</PRE>
 * @return the loop end condition expression.
  */
public Exp
getLoopEndCondition();

/** getLoopStepPart
<PRE>
 * Get the loop step part that is executed before
 * jumping to loopBackLabel.
 * For a loop statement
 *   for (i=0; i<n; i++) { ... }
 * the statement i=i+1; will be returned.
</PRE>
 * @return the loop step part.
 */
public Stmt
getLoopStepPart();

/** getLoopEndPart
<PRE>
 * Get the loop end part having the loopEndLabel.
 * For a loop statement
 *   for (i=0; i<n; i++) { ... }
 * a statement _lab6:; will be returned where
 * _lab6 is the loopEndLabel generated by the compiler.
</PRE>
 * @return the loop end part having the loopEndLabel.
 */
public LabeledStmt
getLoopEndPart();

//==== Get labels and some items of LoopStmt ====//

/**
 * @return the loopBacklabel thet is attached to
 *   loop start condition part.
 */
public Label
getLoopBackLabel();

/**
 * @return return the loopBodyLabel that is attached to
 *   the loop body repetitively executed.
 */
public Label
getLoopBodyLabel();

/**
 * @return the loopStepLabel that is attached to a
 * null stetement placed as the last statement in the
 * loop body part and before the loop step part.
 */
public Label
getLoopStepLabel();

/**
 * @return the loopEndLabel that is attached to a
 * (usually null) statement located at the end of the loop.
 */
public Label
getLoopEndLabel();

/**
 * This method is not used anymore.
 * (lparallel uses LoopInfo of lparallel.)
 * @return the LoopInf.
 */
public coins.flow.LoopInf //##63
getLoopInf();

/**
 * This method is not used anymore.
 */
public void
setLoopInf( coins.flow.LoopInf pLoopInf ); //##63

//==== See the type of the loop ====//

/** isSimpleForLoop
<PRE>
 *  Check if this is a simple for loop, that is,
 *    an instance of ForStmt and
 *    conditional init part is null and
 *    loop end condition is null.
</PRE>
 *  @return true if above conditions are satisfied, else return false.
**/
public boolean
isSimpleForLoop();

/** isSimpleWhileLoop
<PRE>
 *  Check if this is a simple while loop, that is,
 *    an instance of WhileStmt and
 *    conditional init part is null and
 *    loop step part is null and
 *    loop end condition is null.
</PRE>
 *  @return true if above conditions are satisfied, else return false.
**/
public boolean
isSimpleWhileLoop();

/** isSimpleRepeatLoop
<PRE>
 *  Check if this is a simple repeat-while-true loop, that is,
 *    an instance of RepeatStmt and
 *    conditional init part is null and
 *    loop start condition is null and
 *    loop step part is null.
</PRE>
 *  @return true if above conditions are satisfied, else return false.
**/
public boolean
isSimpleRepeatLoop();

/** isSimpleUntilLoop
 *  Check if this is a simple until loop, that is,
 *    an instance of UntilStmt and
 *    conditional init part is null and
 *    loop start condition is null and
 *    loop step part is null.
 *  @return true if above conditions are satisfied, else return false.
 *  ***deprecated Use isSimpleRepeatLoop.
**/
//##68 public boolean
//##68 isSimpleUntilLoop();

/** isSimpleIndexedLoop
<PRE>
 *  Check if this is a simple indexed loop, that is,
 *    an instance of IndexedLoopStmt and
 *    conditional init part is null and
 *    loop end condition is null.
</PRE>
 *  @return true if above conditions are satisfied, else return false.
**/
public boolean
isSimpleIndexedLoop();


//==== Add statement to some part of LoopStmt ====//
/**
 * Add pStmt as the last statement of loop-init-part.
 * If there is no loop-init-part, then pStmt is attached as the new
 * loop-init-part. If there is already loop-init-part, then
 * pStmt is added as the last statement of the loop-init-part.
 * @param pStmt statement to be added to loop-init-part.
 */
public void
addToLoopInitPart( Stmt pStmt );

/** addToConditionalInitPart
<PRE>
 * ConditionalInitPart is executed once if the LoopStartCondition
 * is satisfied. It is a block to where loop invariant expressions
 * are to be moved so that they are executed only once. The
 * ConditionalInitPart is created by addToConditionalInitPart(pStmt)
 * as a block containing ConditionalInitBlock in the LoopInitPart
 * in the following way:
 * Case 1: LoopStartCondition is null:
 *   LoopInitPart_ is changed as follows:
 *     {
 *       oroginal LoopInitPart_;
 *       { // ConditionalInitBlock.
 *         // getConditionalInitPart() returns this else-block.
 *         Sequence of statements added by addToConditionalInitPart;
 *       }
 *     }
 *   The transformation procedure is:
 *     If ConditionalInitBlock is not yet created,
 *       create it as a BlockStmt and add it as the last statement
 *       of LoopInitBlock
 *     pStmt is added as the last statement of ConditionalInitBlock.
 * case 2:  LoopStartCondition is not null and ConditionalInitPart
 *          is not yet created:
 *   LoopInitPart_ is changed as follows:
 *     {
 *       oroginal LoopInitPart_;
 *       if (loopStartConditionExpression == false) {
 *         jump to loopEndLabel;
 *       }else { // ConditionalInitBlock.
 *               // getConditionalInitPart() returns this else-block.
 *         Sequence of statements added by addToConditionalInitPart;
 *         jump to loopBodyLabel;
 *       }
 *     }
 *
 *   The else-part of above if-statement is called as
 *   ConditionalInitBlock.
 * case 3: ConditionalInitBlock is already created:
 *   pStmt is inserted before "goto loopBodyLabel" of ConditionalInitBlock.
 * Expressions to be executed only once for this loop
 * may be added to ConditionalInitBlock by calling
 * addToConditionalInitPart successively.
 * When ConditionalInitBlock with "jump to loopBodyLabel" is created,
 *   setFlag(HIR.FLAG_LOOP_WITH_CONDITIONAL_INIT, true)
 * is executed to show that the loop became irreducible but
 * it is a tame loop that can be treated in many optimization/
 * parallelization procedures.
 * No special treatment is required for ConditionalInitPart in
 * HIR-to-LIR conversion, HIR-to-C conversion, HIR flow analysis,
 * etc. because it takes a form of normal HIR expression.
 * @param pStmt: statement to be added to ConditionalInitPart;
 *     It should not contain statements going outside pStmt.
</PRE>
 */
public void
addToConditionalInitPart( Stmt pStmt );

/**
 * Add the statement pStmt before the step-labeled
 * statement (as the last statement except the null
 * statement with the loop-step label).
 * @param pStmt statement to be added to the boop body.
 */
public void
addToLoopBodyPart( Stmt pStmt );

/**
 * Add the statement pStmt to the loop-step part
 * (add as the statement next to the existing statement
 * of the loop-step part).
 * @param pStmt statement to be added to the loop-step part.
 */
public void
addToLoopStepPart( Stmt pStmt );

/**
 * Add pStmt as the statement next to the existing
 * loop-end statement.
 * @param pStmt statement to be added to the loop-end part.
 */
public void
addToLoopEndPart( Stmt pStmt );

//==== Set or replace some part of LoopStmt ====//

/**
 * Set the expression pCondition as the loop-start condition
 * expression.
 * @param pCondition expression to be set.
 */
public void
setLoopStartCondition(Exp pCondition);

/**
 * Set the expression pCondition as the loop-end condition
 * expression.
 * @param pCondition expression to be set.
 */
public void
setLoopEndCondition(Exp pCondition);

/**
 * Replace the loop body with pNewStmt.
 * @param pNewStmt statement to be set as the new
 *   loop body statement.
 */
public void
replaceBodyPart( LabeledStmt pNewStmt );

} // LoopStmt interface
