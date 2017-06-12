/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;

import coins.ir.hir.HIR;
import coins.ir.hir.LabelNode;
import coins.ir.hir.LabeledStmt;

/** Label interface
 *  LabelImpl class implements this interface.
**/
public interface
//##60 Var extends OperandSym
Label extends Sym //##60
{

/** getHirPosition
 *  Get the HIR statement defining this label.
 *  @return the LabeledStmt defining this label.
**/
LabeledStmt getHirPosition();

/** setHirPosition
 *  Set the HIR statement defining this label.
 *  @param pHirPosition HIR statement to which this is
 *         defined as its label (it is a LabeledStmt).
**/
void
setHirPosition( LabeledStmt pHirPosition );

/** getHirRefList
 *  Get the list of LabelNodes refering this label.
 *  If there is no HIR reference, return null.
 *  The reference list is built by finishHir() called for
 *  SubpDefinition or Program. The list is also built by
 *  buildLabelRefList() of Subp. Before that time
 *  or after label deletion/insertion, the list may
 *  be null or its contents may be incorrect.
 *  @return the list of LabelNodes refering this label.
**/
coins.ir.IrList
getHirRefList();

/** addToHirRefList
 *  Add pHirRefPosition to the reference list of this label.
 *  @param pHirRefPosition HIR refenence node of this label.
**/
//##62 void
//##62 addToHirRefList( LabelNode pHirRefPosition );


////////////////////SF031111[
//##62 public boolean
//##62 removeFromHirRefList(LabelNode labelnode);
////////////////////SF031111]

/** resetHirRefList
 *  Reset the label reference list (change to null).
**/
//##62 void
//##62 resetHirRefList( );

/** getLirRefList
 *  @return the list of LIR instructions refering this label.
**/
//# IrList getLirRefList();

/** addToLirRefList
 *  Add pLirRefPosition to the reference list of this label.
 *                   If there is no LIR reference, return null.
 *  @param pLirRefPosition LIR refenence instruction of this label.
**/
//# void addToLirRefList( LIR pLirRefPosition );

/** getHirRefCount
 *  Get the reference count of this label in HIR.
 *  The reference list is built by visit(....) of
 *  coins.flow.LabelRefListBuilder. Before that time
 *  or after label deletion/insertion, the count may
 *  incorrect.
 *  @return the HIR reference count of this label.
**/
public int
getHirRefCount();

/** getBBlock Get basic block corresponding to this label.
 @return the basic block.
**/
public coins.aflow.BBlock getBBlock();

/** setBBlock Set pBBlock as the  basic block corresponding to this label.
 * Note: Information for flow analysis should stem
 * from subpFlow so that reset flow anal inf is
 * executed without trouble (To be improved). //##94
 @param pBBlock basic block to be set.
**/
public void   setBBlock( coins.aflow.BBlock pBBlock );

/** getLabelKind Get label kind such as hten-label, else-label, etc.
 *  @return label kind.
**/
public int     getLabelKind();

/** setLabelKind Set label kind.
 *  The label kind is used in modifying control flow graph.
 * @param lPabelKind the label kind to be set.
**/
public void    setLabelKind( int pLabelKind );

/** endPointLabel true if END_IF, LOOP_END, SWITCH_END.
 *  @return true/false
**/
public boolean endPointLabel();

/** getOriginHir
<PRE>
 * Get the node that originate this label
 *                such as if-node for then-label,
 *                while-node for loop-back label, etc.
 *  Correspondence between fLabelKind and fOriginHir ..
        fLabelKind           fOriginHir
      ENTRY_LABEL          : entry node
      THEN_LABEL           : IfStmt node
      ELSE_LABEL           : IfStmt node
      END_IF_LABEL         : IfStmt node
      LOOP_COND_INIT_LABEL : LoopStmt node
      LOOP_BACK_LABEL      : LoopStmt node
      LOOP_BODY_LABEL      : LoopStmt node
      LOOP_STEP_LABEL      : LoopStmt node
      LOOP_END_LABEL       : LoopStmt node
      SWITCH_CASE_LABEL    : SwitchStmt node
      SWITCH_DEFAULT_LABEL : SwitchStmt node
      SWITCH_END_LABEL     : SwitchStmt node
      RETURN_POINT_LABEL   : FunctionExp node
      JUMP_LABEL           : JumpStmt node
      CONTINUE_LABEL       : Predecessor node
      SOURCE_LABEL         : JumpStmt node if there is.
</PRE>
**/
public HIR
getOriginHir();

/** setOriginHir
 *  Set origin node.
 *  @param pOriginHir Origin node of this label.
**/
public void
setOriginHir( HIR pOriginHir );

/** replaceHirLabels
 *  Replace every LabelNode listed in getHirRefList()
 *  refering this label to a LabelNode of pToLabel.
 *  If this label has no reference, then no replecement is done.
 *  @param pToLabel Label by which this label is to be replaced.
**/
public void
replaceHirLabel( Label pToLabel );

//==== Constants ====//

/** Label kind numbers */
public static final int
  UNCLASSIFIED_LABEL   =  0,  // Label not yet classified.
  ENTRY_LABEL          =  1,  // Label at subprogram entry.
  THEN_LABEL           =  2,  // Label at then-part of IfStmt.
  ELSE_LABEL           =  3,  // Label at else-part of IfStmt.
  LOOP_COND_INIT_LABEL =  5,  // Label at conditional-init-part of loop
  LOOP_BACK_LABEL      =  6,  // Loop-back label.
  LOOP_BODY_LABEL      =  7,  // Loop-body label.
  LOOP_STEP_LABEL      =  8,  // Loop-step label.
  SWITCH_CASE_LABEL    = 11,  // Case-selection label of SwitchStmt.
  SWITCH_DEFAULT_LABEL = 12,  // Switch-default label.
  RETURN_POINT_LABEL   = 15,  // Label at return point from subprogram.
  JUMP_LABEL           = 16,  // Jump target label.
  CONTINUE_LABEL       = 17,  // Continue without branch  (a kind of join
                              // point or successive LabeledStmt).
  SOURCE_LABEL         = 18,  // Label in source program
                              // (may be jump target label).
                 // Following END_xx labels should be placed at tail.
  END_IF_LABEL         = 20,  // End-if label.
  LOOP_END_LABEL       = 21,  // Loop-end label.
  SWITCH_END_LABEL     = 22;  // Switch-end label.

} // Label interface
