/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.IoRoot;
import coins.HirRoot;
import coins.driver.CoinsOptions;
import coins.ir.IrList;
import coins.ir.hir.BlockStmt;
import coins.ir.hir.HIR;
import coins.ir.hir.HirIterator;
import coins.ir.hir.HirList;
import coins.ir.hir.IfStmt;
import coins.ir.hir.JumpStmt;
import coins.ir.hir.LabelDef;
import coins.ir.hir.LabelNode;
import coins.ir.hir.LabeledStmt;
import coins.ir.hir.LabeledStmtImpl;
import coins.ir.hir.LoopStmt;
import coins.ir.hir.NullNode;
import coins.ir.hir.Stmt;
import coins.ir.hir.SwitchStmt;
import coins.sym.Label;
import coins.sym.Subp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

//========================================//
//                         Jan. 2007.
/** SimplifyHir:
<PRE>
 * This class simplifies HIR by deleting labeled statements with
 * null statement body if the label is not explicitly refered,
 * in order to suppress the generation of useless basic blocks
 * in converting HIR to LIR. If HIR is changed by this class,
 * finishHir() is called in this class so that HIR may keep
 * consistency.
 * This is invoked just before HIR-to-LIR conversion by the
 * compiler driver. This does nothing if the compiler option
 *   -coins:hirOpt=noSimplify
 * is specified and pForceSimplify is false.
 * It is not recommended to invoke this before HIR transformations
 * such as optimization and parallelization because the simplified
 * HIR may be unsuitablbe for such HIR transformations.
 *
 * Description of simplifications
 *
 * If-statement:
 *
 *  (if (condition)
 *   (labeledStmt (thenLabel) thenPartStatement)
 *   (labeledStmt (elseLabel) <null>)
 *   (labeledStmt (endLabel) <null>))
 *  -->
 *  (if (condition)
 *   (labeledStmt (thenLabel) thenPartStatement)
 *   <null>
 *   (labeledStmt (endLabel) <null>))
*
 *  (if (condition)
 *   (labeledStmt (thenLabel) <null>)
 *   (labeledStmt (elseLabel) elsePartStatement)
 *   (labeledStmt (endLabel) <null>))
 *  -->
 *  (if (condition)
 *   <null>
 *   (labeledStmt (elseLabel) elsePartStatement)
 *   (labeledStmt (endLabel) <null>))
 *
 * Loop-statement:
 *  The general form of a loop statement is
 *   (loop
 *    LoopInitPart
 *    ConditionalInitPart -- usually null
 *    (labeledStmt (loopBackLabel)
 *     (expStmt ... )) -- loop-start conditionPart
 *    (labeledStmt (loopBodyLabel)
 *     (block
 *       ... -- loop body part
 *      (labeledStmt (loopStepLabel) <null>))
 *    (expStmt ... ) -- loop-end condition
 *    (labeledStmt (loopEndLabel) <null>))
 *  If loop-start condition is null, remove loopBodylabel
 *   by changing (labeledStmt (loopBodyLabel) (block ...))
 *            to (block ... ).
 *  If loopStepLabel is not explicitly refered (that is,
 *  the loop does not contain continue-statement), then
 *  (labeledStmt (loopStepLabel) <null>) is deleted.
 *
 * Switch-statement:
 *  The simplification of switch-statement is covered
 *  by the simplification of labeled statement.
 *
 * Labeled-satement:
 *
 *  Simplify LabeledStmt having LabeledStmt as its body:
 *
 *  (labelDef (lab1) (labelDef (lab2) stmtBody))
 *  -->
 *  (labelDef (lab1 lab2) stmtBody)
 *
 *  (labelDef (lab1)
 *   (block
 *    (labelDef (lab2) stmtBody)
 *         .... ))
 *  -->
 *  (labelDef (lab1 lab2)
 *   (block
 *    stmtBody
 *     .... ))
 *
 * After above simplifications, change statements with multiple
 * labels to statements with single label and rewrite HIR nodes
 * refering erazed labels to the remaining label, that is,
 *  (labelDef (lab1 lab2 ... labn) stmtBody)
 *  -->
 *   (labelDef (lab1) stmtBody)
 *    and change references to lab2, ..., labn to lab1.
</PRE>
**/
public class
SimplifyHir
{
  HirRoot hirRoot;
  IoRoot  ioRoot;
  Subp fSubp;  // Subprogram under processing.
  ArrayList fLabelDefToBeDeleted; // LabeledStmt to be changed
                                  // to Stmt without label.
  ArrayList fStmtToBeDeleted; // LabeledStmt to be deleted.
  ArrayList fLabeledStmt;     // LabeledStmt other than listed in
                              // fLabelDefToBeDeleted, fStmtToBeDeleted.
  ArrayList fStmtWithMultipleLabels; // Statements with multiple labels.
  ArrayList fLabelRef;        // Label reference nodes.
  HashMap   fReplaceLabel;    // Map original label to a label to be replaced.
  boolean   fChanged;         // true if HIR is changed for current subprogram.
  boolean   fSomeSubpChanged; // true if HIR is changed for some subprogram.
  int       fDbgLevel;        // HIR debug level.

public
SimplifyHir( HirRoot pHirRoot, boolean pForceSimplify )
{
  hirRoot = pHirRoot;
  ioRoot  = hirRoot.ioRoot;
  Program lProgram = (Program)hirRoot.programRoot;
  fDbgLevel = ioRoot.dbgHir.getLevel();
  CoinsOptions lOptions;
  Map lOptionMap;
  String lHirOpt;
  List lKeyList;
  lOptions = ioRoot.getCompileSpecification().getCoinsOptions();
  lHirOpt = lOptions.getArg("hirOpt");
  if ((! pForceSimplify)&&(lHirOpt != null)) {
    lOptionMap = lOptions.parseArgument(lHirOpt, '/', '.');
    lKeyList = (List)lOptionMap.get("item_key_list");
    if (lKeyList.contains("noSimplify")) {
      ioRoot.dbgHir.print(1, "\nDo not simplify Hir\n");
      return;
    }
  }
  ioRoot.dbgHir.print(1, "\nSimplifyHir\n");
  if (fDbgLevel >= 4) {
    ioRoot.dbgHir.print(4, "HIR before simplification");
    lProgram.print(0, true);
  }
  fSomeSubpChanged = false;
  IrList lSubpList = lProgram.getSubpDefinitionList();
  for (Iterator lIt = lSubpList.iterator();
       lIt.hasNext(); ) {
    SubpDefinition lSubpDef = (SubpDefinition)lIt.next();
    simplifyHir(lSubpDef.getSubpSym());
    if (fChanged)
      fSomeSubpChanged = true;
  }
  if (fSomeSubpChanged) {
    ((HIR)lProgram).finishHir();
  }
} // SimplifyHir

public void
simplifyHir( Subp pSubp )
{
  HirIterator lHirIterator;
  HIR   lNode;
  if ((pSubp == null)||
      (pSubp.getHirBody() == null))
    return;
  ioRoot.dbgHir.print(2, "simplifyHir",
      pSubp.getName());
  fSubp = pSubp;
  fSubp.buildLabelRefList();
  fStmtToBeDeleted        = new ArrayList();
  fLabelDefToBeDeleted    = new ArrayList();
  fStmtWithMultipleLabels = new ArrayList();
  fLabeledStmt = new ArrayList();
  fLabelRef    = new ArrayList();
  fReplaceLabel= new HashMap();
  fChanged     = false;
  //-- Prepare for deletion
  //   by recording statements to be deleted or modified
  //   because if immediate deletion has bad effect to
  //   HIR iterator.
  // Search for labeled statements to be deleted or
  //   jump statements to be deleted,
  // Record label references.
  for (lHirIterator = hirRoot.hir.hirIterator(pSubp.getHirBody());
       lHirIterator.hasNextStmt(); ) {
    lNode = lHirIterator.getNextStmt();
    if (lNode != null) {
      ioRoot.dbgHir.print(4, " getNextStmt", lNode.toStringShort());
      if (lNode instanceof LabeledStmt) {
        if ((! shouldHaveLabel((LabeledStmt)lNode)&&
            (! fStmtToBeDeleted.contains(lNode))&&
            (! fLabelDefToBeDeleted.contains(lNode)))) {
           fLabeledStmt.add(lNode);
         }
      }else if (lNode instanceof IfStmt) {
        deleteUnusedLabelsOfIfStmt((IfStmt)lNode);
      }else if (lNode instanceof LoopStmt) {
        deleteUnusedLabelsOfLoopStmt((LoopStmt)lNode);
      }else if (lNode instanceof JumpStmt) {
        Label lJumpLabel = ((JumpStmt)lNode).getLabel();
        Stmt lNextStmt1 = ((JumpStmt)lNode).getNextStmt();
        if ((lNextStmt1 instanceof LabeledStmt)&&
            (((LabeledStmt)lNextStmt1).getLabel() == lJumpLabel)) {
          // Delete lNode because it jumps to the next statement.
          fStmtToBeDeleted.add(lNode);
        }else if ((lNextStmt1 instanceof BlockStmt)&&
            (((BlockStmt)lNextStmt1).getFirstStmt() instanceof LabeledStmt)&&
            (((BlockStmt)lNextStmt1).getFirstStmt().getLabel() == lJumpLabel)) {
          // Delete lNode because it jumps substantially to the next statement.
          fStmtToBeDeleted.add(lNode);
        }else {
          // In other case, record the label reference.
          fLabelRef.add(lNode.getChild1());
        }
      }else if (lNode instanceof SwitchStmt) {
        recordLabelRefOfSwitchStmt( (SwitchStmt)lNode );
      }
    }
  }
  if (ioRoot.dbgHir.getLevel() >= 4) {
    ioRoot.dbgHir.print(4, "stmtToBeDeleted", fStmtToBeDeleted.toString());
    ioRoot.dbgHir.print(4, "labelDefToBeDeleted", fLabelDefToBeDeleted.toString());
    ioRoot.dbgHir.print(4, "fLabelRef", fLabelRef.toString());
  }
  //-- Delete labeled statements that have null body.
  for ( Iterator lIter1 = fStmtToBeDeleted.iterator();
        lIter1.hasNext(); ) {
    Stmt lStmtToBeDeleted = (Stmt)lIter1.next();
    lStmtToBeDeleted.deleteThisStmt();
    fChanged = true;
  }
  //-- Change LabeledStmt to a statement without label.
  for ( Iterator lIter2 = fLabelDefToBeDeleted.iterator();
        lIter2.hasNext(); ) {
    LabeledStmt lStmtToBeChanged = (LabeledStmt)lIter2.next();
    Stmt lStmtBody = lStmtToBeChanged.getStmt();
    if (lStmtBody != null)
      lStmtBody.cutParentLink();
    lStmtToBeChanged.replaceThisStmtWith(lStmtBody);
    fChanged = true;
  }
  //-- Simplify LabeledStmt by fusing label definitions.
  for ( Iterator lIter3 = fLabeledStmt.iterator();
        lIter3.hasNext(); ) {
    LabeledStmt lLabeledStmt3 = (LabeledStmt)lIter3.next();
    Stmt lStmtBody = lLabeledStmt3.getStmt();
    if (lStmtBody instanceof LabeledStmt) {
      // LabeledStmt having LabeledStmt as its body.
      // Change (labelDef (lab1) (labelDef (lab2) stmtBody))
      //   to   (labelDef (lab1 lab2) stmtBody).
      Stmt lStmtBody2 = ((LabeledStmt)lStmtBody).getStmt();
      lLabeledStmt3.merge((LabeledStmt)lStmtBody);
      if (lStmtBody2 != null)
        lStmtBody2.cutParentLink();
      lLabeledStmt3.setStmt(lStmtBody2);
      fChanged = true;
    }else if (lStmtBody instanceof BlockStmt) {
      Stmt lFirstStmt = ((BlockStmt)lStmtBody).getFirstStmt();
      if (lFirstStmt instanceof LabeledStmt) {
        // Change (labelDef (lab1)
        //         (block
        //          (labelDef (lab2) stmtBody)
        //          .... ))
        //   to    (labelDef (lab1 lab2)
        //          (block
        //           stmtBody
        //           .... ))
        lLabeledStmt3.merge((LabeledStmt)lFirstStmt);
        Stmt lStmtBody3 = ((LabeledStmt)lFirstStmt).getStmt();
        if (lStmtBody3 != null) {
          lStmtBody3.cutParentLink();
          lFirstStmt.replaceThisStmtWith(lStmtBody3);
        }else {
          lFirstStmt.deleteThisStmt();
        }
        fChanged = true;
      }
    }
    if (lLabeledStmt3.getLabelDefList().size() > 1)
      fStmtWithMultipleLabels.add(lLabeledStmt3);
  }
  //-- For statement with multiple labels, reduce multiple
  //   labels to single label and rewrite the nodes
  //   refering erazed label.
  //   That is,
  //     change (labelDef (lab1 lab2 ... labn) stmtBody)
  //       to   (labelDef (lab1) stmtBody)
  //     and change references to lab2, ..., labn to lab1.
  for ( Iterator lIter4 = fStmtWithMultipleLabels.iterator();
        lIter4.hasNext(); ) {
    LabeledStmt lLabeledStmt4 = (LabeledStmt)lIter4.next();
    IrList lLabelList = lLabeledStmt4.getLabelDefList();
    Label lFirstLabel = null;
    for ( Iterator lIter5 = lLabelList.iterator();
          lIter5.hasNext(); ) {
      LabelDef lLabelDef = (LabelDef)lIter5.next();
      Label lLabel5 = lLabelDef.getLabel();
      if (lFirstLabel == null) {
        lFirstLabel = lLabel5;
      }else {
        fReplaceLabel.put(lLabel5, lFirstLabel);
      }
    }
    IrList lNewLabelList = hirRoot.hir.irList();
    lNewLabelList.add(hirRoot.hir.labelDef(lFirstLabel));
    lLabeledStmt4.setLabelDefList(lNewLabelList);
  }
  // Rewrite label references.
  for ( Iterator lIter6 = fLabelRef.iterator();
        lIter6.hasNext(); ) {
    LabelNode lLabelNode6 = (LabelNode)lIter6.next();
    Label lLabel6 = lLabelNode6.getLabel();
    if (fReplaceLabel.containsKey(lLabel6)) {
      Label lLabelForReplace = (Label)fReplaceLabel.get(lLabel6);
      ioRoot.dbgHir.print(4, "replaceLabel", "of " + lLabelNode6.toStringShort()
        + " to " + lLabel6.getName());
      LabelNode lNewLabelNode = hirRoot.hir.labelNode(lLabelForReplace);
      lLabelNode6.replaceThisNode(lNewLabelNode);
      fChanged = true;
    }
  }
  if (fChanged) {
    ioRoot.dbgHir.print(1, "simplifyHir", "changed HIR of "
      + pSubp.getName());
    if (ioRoot.dbgHir.getLevel() >= 3) {
      pSubp.getHirBody().print(1);
    }
  }else {
    ioRoot.dbgHir.print(1, "simplifyHir", "does not change HIR of "
      + pSubp.getName() + "\n");
  }
 } // simplifyHir

public void
deleteUnusedLabelsOfIfStmt( IfStmt pIfStmt )
{
  ioRoot.dbgHir.print(3, "deleteUnusedLabelsOfIfStmt",
    pIfStmt.toStringShort());
  deleteIfNull(pIfStmt.getElsePart());
  deleteIfNull(pIfStmt.getThenPart());
} // deleteUnusedLabelsOfIfStmt

public void
deleteUnusedLabelsOfLoopStmt( LoopStmt pLoopStmt )
{
  // Loop statement in general form:
  //  (loop
  //   LoopInitPart
  //   ConditionalInitPart -- usually null
  //   (labeledStmt (loopBackLabel)
  //     (expStmt ... )) -- loop-start conditionPart
  //   (labeledStmt (loopBodyLabel)
  //    (block
  //      ... -- loop body part
  //     (labeledStmt (loopStepLabel) null))
  //   (expStmt ... ) -- loop-end condition
  //   (labeledStmt (loopEndLabel)))
  // If loop-start condition is null, remove loopBodylabel
  //   by changing (labeledStmt (loopBodyLabel) (block ...))
  //            to (block ... ).
  // If loopStepLabel is not refered (no continue statment),
  //   remove (labeledStmt (loopStepLabel) null).
  ioRoot.dbgHir.print(3, "deleteUnusedLabelsOfLoopStmt",
    pLoopStmt.toStringShort());
  // Loop back label can not be deleted.
  LabeledStmtImpl lLoopBodyPart = (LabeledStmtImpl)pLoopStmt.getLoopBodyPart();
  if (
      (lLoopBodyPart != null)&&  //##92
      (lLoopBodyPart.explicitLabelReference() == null)) {
    // If loop-start-condition is null,
    // delete label of loop body.
    if (pLoopStmt.getLoopStartCondition() == null)
      fLabelDefToBeDeleted.add(lLoopBodyPart);
  }
  Label lLoopStepLabel = pLoopStmt.getLoopStepLabel();
  if (lLoopStepLabel != null) { //##79
    LabeledStmtImpl lLoopStepLabeledStmt =
      (LabeledStmtImpl)lLoopStepLabel.getHirPosition();
    if ((lLoopStepLabeledStmt != null)&&  //##92
        (lLoopStepLabeledStmt.explicitLabelReference() == null)) {
      if (lLoopStepLabeledStmt.getStmt() == null) {
        fStmtToBeDeleted.add(lLoopStepLabeledStmt);
      }
      else {
        fLabelDefToBeDeleted.add(lLoopStepLabeledStmt);
      }
    }
  } //##79
} // deleteUnusedLabelsOfLoopStmt

public void
recordLabelRefOfSwitchStmt( SwitchStmt pSwitchStmt )
{
  // child2: JumpTable of the form (seq jumpList LabelNode)
  //         where labelNode indicates default label, and
  //         jumpList is a list of const-label pairs
  //               (hirList (hirSeq constNode labelNode) ... )
  HirList lJumpTable = (HirList)pSwitchStmt.getChild2().getChild1();
  LabelNode lDefault = (LabelNode)pSwitchStmt.getChild2().getChild2();
  // Record the label references.
  for ( Iterator lListIterator = lJumpTable.iterator();
        lListIterator.hasNext(); ) {
    HirSeq lConstLabelPair = (HirSeq)(lListIterator.next());
    LabelNode lCaseLabelNode = (LabelNode)lConstLabelPair.getChild2();
    fLabelRef.add(lCaseLabelNode);
  }
  fLabelRef.add(lDefault);
} // recordLabelRefOfSwitchStmt


boolean
shouldHaveLabel( LabeledStmt pStmt )
{
  HIR lParent = (HIR)pStmt.getParent();
  if ((lParent instanceof IfStmt)||
      (lParent instanceof LoopStmt)||
      (lParent instanceof SwitchStmt)||
      (lParent instanceof SubpDefinition))
    return true;
  return false;
} // shouldHaveLabel

void
deleteIfNull( LabeledStmt pStmt )
{
  Stmt lStmt;
  if (pStmt != null) {
    lStmt = pStmt.getStmt();
    if ((lStmt == null)||(lStmt instanceof NullNode)) {
      if (((LabeledStmtImpl)pStmt).explicitLabelReference() == null) {
        fStmtToBeDeleted.add(pStmt);
      }
    }
  }
} // deleteIfNull

} // SimplifyHir

