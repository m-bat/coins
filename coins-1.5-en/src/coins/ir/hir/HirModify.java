/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.HirRoot;
import coins.IoRoot;
import coins.Registry;      //##93
import coins.ir.IR;
import coins.ir.IrList;
import coins.ir.IrListImpl; //##85
import coins.opt.OptHirPrimary; //##95
import coins.sym.Label;
import coins.sym.SymTable;
import coins.sym.Type;      //##88
import coins.sym.Var;       //##92
import java.util.ArrayList; //##91
import java.util.Iterator;  //##87
import java.util.List;      //##91

//========================================//

/** HirModify class
 * This class contains methods to modify HIR tree.
**/
public class
HirModify extends HIR_Impl
{
  protected final IoRoot
    ioRoot;  // Used to access Root information.

  protected final HIR
    hir;    // Used to invoke HIR methods.

//===== Constructor ======//

/**
 * Construct HirModify object that can be used
 * to invoke methods of HirModyfy in other classes.
 * @param pHirRoot Instance of HirRoot to be recorded
 *   in the HirModify object.
 */
public
HirModify( HirRoot pHirRoot )
{
  super(pHirRoot);
  ioRoot = pHirRoot.ioRoot;
  hir    = pHirRoot.hir;
  fOperator = HIR.OP_NULL;
}

//==== Access methods to modify HIR subtree. ====//

//====== Methods for manipulating HIR nodes ======//

/** changeLabelsInTree
<PRE>
 *  Change labels defined in pTree and return the list of
 *  labels showing the old-new correspondence.
 *  pTree should not contain labels listed in (IrList of new labels)
 *  so as not to fall in infinite replacement loop.
 *  If pTree contains an instance of LoopStmt, IfStmt, SwitchStmt,
 *  SubpDefinition, then the labels got by getLoopBackLabel(),
 *  getLoopEndLabel(), etc. are also changed.
</PRE>
 *  @param pTree: HIR tree to be changed.
 *  @param pLabelCorrespondence List of label correspondence
 *      (IrList (IrList of original labels) (IrList of new labels) )
**/
public void
changeLabelsInTree( HIR pTree, IrList pLabelCorrespondence ) {
  SymTable    lSubpSymTable;
  HIR         lNode;
  IrList      lOldLabelList = hir.irList();
  IrList      lNewLabelList = hir.irList();
  IrList      lLabelCorrespondence;
  IrList      lLabelDefListOrg, lLabelDefListNew;
  HirIterator lHirIterator;
  Label       lOldLabel, lNewLabel;
  Label       lLoopBackLabel = null, lLoopStepLabel = null,
              lLoopEndLabel = null; //##70
  int         lIndex;
  if (fDbgLevel > 3) //##58
    ioRoot.dbgHir.print( 4, "changeLabelsInTree",
                 ioRoot.toStringObject(pTree));
  if (pTree == null)
    return;
  lOldLabelList = ((IrList)pLabelCorrespondence.get(0));
  lNewLabelList = ((IrList)pLabelCorrespondence.get(1));
  if (lOldLabelList.isEmpty())  //##53
    return;                     //##53
  //---- Change old labels to corresponding new label.
  for (lHirIterator = hir.hirIterator(pTree);
       lHirIterator.hasNext(); ) {
    lNode = lHirIterator.next();
    if (lNode == null)
      continue;
    if (lNode.getOperator() == HIR.OP_LABELED_STMT) {
      lLabelDefListOrg = ((LabeledStmt)lNode).getLabelDefList();
      lLabelDefListNew = hir.irList();
      for (java.util.Iterator lListIterator = lLabelDefListOrg.iterator();
           lListIterator.hasNext(); ) {
        lOldLabel = ((LabelDef)lListIterator.next()).getLabel();
        lIndex = lOldLabelList.indexOf(lOldLabel);
        if (lIndex >= 0) {
          lNewLabel = (Label)(lNewLabelList.get(lIndex));
          lLabelDefListNew.add(hir.labelDef(lNewLabel));
          if (fDbgLevel > 3) //##58
            ioRoot.dbgHir.print( 5, "change " + lNode.toStringShort(),
                 lOldLabel.getName() + " to " + lNewLabel.getName());
        }
      }
      ((LabeledStmt)lNode).setLabelDefList(lLabelDefListNew); //## CONFLICT with iterator ?
    } else if (lNode.getOperator() == HIR.OP_LABEL) {
      lOldLabel = (Label)(((LabelNode)lNode).getSymNodeSym());
      lIndex = lOldLabelList.indexOf(lOldLabel);
      if (lIndex >= 0) {
        lNewLabel = (Label)(lNewLabelList.get(lIndex));
        ((LabelNode)lNode).setSymNodeSym(lNewLabel);
        if (fDbgLevel > 3) //##58
          ioRoot.dbgHir.print( 5, "change " + lNode.toStringShort(),
               lOldLabel.getName() + " to " + lNewLabel.getName());
      }
    //##70 BEGIN
    }else if (lNode instanceof LoopStmtImpl) {
      LoopStmtImpl lLoopStmt = (LoopStmtImpl)lNode;
      lIndex = lOldLabelList.indexOf(lLoopStmt.getLoopBackLabel());
      if (lIndex >= 0) {
        lLoopBackLabel = (Label)(lNewLabelList.get(lIndex));
        lLoopStmt.fLoopBackLabel = lLoopBackLabel;
      }
      lIndex = lOldLabelList.indexOf(lLoopStmt.getLoopStepLabel());
      if (lIndex >= 0) {
        lLoopStepLabel = (Label)(lNewLabelList.get(lIndex));
        lLoopStmt.fLoopStepLabel = lLoopStepLabel;
      }
      lIndex = lOldLabelList.indexOf(lLoopStmt.getLoopEndLabel());
      if (lIndex >= 0) {
        lLoopEndLabel = (Label)(lNewLabelList.get(lIndex));
        lLoopStmt.fLoopEndLabel = lLoopEndLabel;
      }
      ioRoot.dbgHir.print( 4, "change labels of LoopStmt",
                   " back " + lLoopBackLabel + " step " + lLoopStepLabel +
                   " end " + lLoopEndLabel);
    // IfStmt does not record labels.
    //##70 END
    }
  }
} // changeLabelsInTree

/** makeLabelCorrespondenceList
 *  Change labels defined in pTree and return the list of
 *  labels showing the old-new correspondence.
 *  @param pTree HIR tree to be changed.
 *  @return (IrList (IrList original labels) (IrList new labels) )
**/
public IrList
makeLabelCorrespondenceList( HIR pTree ) {
  SymTable    lSubpSymTable;
  HIR         lNode;
  IrList      lOldLabelList = hir.irList();
  IrList      lNewLabelList = hir.irList();
  IrList      lLabelDefList;
  IrList      lLabelCorrespondence;
  HirIterator lHirIterator;
  Label       lOldLabel, lNewLabel;
  if (fDbgLevel > 3) //##58
    ioRoot.dbgHir.print( 5, "makeLabelCorrespondenceList",
        ioRoot.toStringObject(pTree)
        + " in " + hirRoot.symRoot.subpCurrent); //##80
  if (pTree == null)
    return null;
  lSubpSymTable = hirRoot.symRoot.subpCurrent.getSymTable();
  //---- List up label definitions in the subtree pTree.
  for (lHirIterator = hir.hirIterator(pTree);
       lHirIterator.hasNext(); ) {
    lNode = lHirIterator.next();
    if ((lNode != null)&&(lNode.getOperator() == HIR.OP_LABELED_STMT)) {
      lLabelDefList = ((LabeledStmt)lNode).getLabelDefList();
      for (java.util.Iterator lListIterator = lLabelDefList.iterator();
           lListIterator.hasNext(); ) {
        lOldLabel = ((LabelDef)lListIterator.next()).getLabel();
        lNewLabel = lSubpSymTable.generateLabel();
        lOldLabelList.add(lOldLabel);
        lNewLabelList.add(lNewLabel);
        lNewLabel.setLabelKind(lOldLabel.getLabelKind());//##85
        if (fDbgLevel > 3) //##58
          ioRoot.dbgHir.print( 5, "label org",
                lOldLabel.toString() + " new " + lNewLabel.toString());
      }
    }
  }
  //---- Make label correspondence list ----//
  lLabelCorrespondence = hir.irList();
  lLabelCorrespondence.add(lOldLabelList);
  lLabelCorrespondence.add(lNewLabelList);
  return lLabelCorrespondence;
} // makeLabelCorrespondenceList

/** getNewLabel
 *  Get the new label corresponding to pOldLabel
 *  by looking up pLabelCorresp list.
 *  @param pOldLabel Old label to be changed.
 *  @param pLabelCorresp List of label correspondence
 *      (IrList (IrList original labels) (IrList new labels) )
 *  @return the new label corresponding to pOldLabel.
 *      If pOldLabel is not listed in pLabelCorresp,
 *      return pOldLabel.
**/
public Label
getNewLabel( Label pOldLabel, IrList pLabelCorresp ) {
  int    lIndex;
  Label  lNewLabel;
  IrList lOldLabelList = (IrList)(pLabelCorresp.get(0));
  IrList lNewLabelList = (IrList)(pLabelCorresp.get(1));
  lIndex = lOldLabelList.indexOf(pOldLabel);
  if (lIndex >= 0) {
    lNewLabel = (Label)(lNewLabelList.get(lIndex));
  }else {
    if (fDbgLevel > 3)
      ioRoot.dbgHir.print( 5, "label",
            pOldLabel.toString() + " is not listed.");
    lNewLabel = pOldLabel;
  }
  return lNewLabel;
} // getNewLabel

//##85 BEGIN

/**
<PRE>
 * adjustLabelInf is called from HIR.copyWithOperandsChangingLabels
 * and adjust labal information for compound HIR subtrees
 * that may contain labeled statements such as
 *     SWITCH, LOOP, IF, BLOCK, SEQ, LIST, LabeledStmt.
</PRE>
 * @param pHir copied HIR after changing labels but
 *     before adsjusting label information.
 * @param pLabelCorresp shows correspondence between old labels and
 *     new labels.
 */
public void
adjustLabelInf( HIR pHir, IrList pLabelCorresp )
{
  if ((pHir == null)||(pLabelCorresp == null))
    return;
  switch (pHir.getOperator()) {
  case HIR.OP_SWITCH:
    ((SwitchStmtImpl)pHir).defaultLabel
        = ((LabelNode)(pHir.getChild2().getChild2())).getLabel();
    ((SwitchStmtImpl)pHir).endLabel
        = ((LabeledStmt)(pHir.getChild(4))).getLabel();
    for (HirIterator lIt = pHir.hirIterator(pHir.getChild2());
         lIt.hasNext(); ) {
      HIR lHir = lIt.next();
      if (lHir instanceof LabelNode) {
        Label lLabel = ((LabelNode)lHir).getLabel();
        lLabel.setOriginHir(pHir);
        if (fDbgLevel > 3)
          hirRoot.ioRoot.dbgHir.print(5, " setOrigin to " + lLabel.getName());
      }
    }
    adjustLabelInf(((SwitchStmt)pHir).getBodyStmt(), pLabelCorresp);
    break;
  case HIR.OP_FOR:
  case HIR.OP_WHILE:
  case HIR.OP_REPEAT:
    //-- Loop statement.
    ((LoopStmtImpl)pHir).fLoopBackLabel
        = ((Stmt)(pHir.getChild(3))).getLabel(); // start cond
    // Loop step label is the label attached to the last statement
    // of the loop body.
    Stmt lLoopBody  = ((LoopStmt)pHir).getLoopBodyPart();
    if (lLoopBody instanceof LabeledStmt)
      lLoopBody = ((LabeledStmt)lLoopBody).getStmt();
    ((LoopStmtImpl)pHir).fLoopStepLabel = null;
    if (lLoopBody instanceof BlockStmt) {
      ((LoopStmtImpl)pHir).fLoopStepLabel = ((BlockStmt)lLoopBody).getLastStmt().getLabel();
    }
    if (((LoopStmtImpl)pHir).fLoopStepLabel == null)
      ((LoopStmtImpl)pHir).fLoopStepLabel
          = ((Stmt)(pHir.getChild(5))).getLabel(); // end condition
    ((LoopStmtImpl)pHir).fLoopEndLabel
         = ((Stmt)(pHir.getChild(7))).getLabel(); // loop end
    if (fDbgLevel > 3)
      hirRoot.ioRoot.dbgHir.print(5, " adjustLabelInf ", pHir.toStringShort());
    adjustLabelInf(((LoopStmt)pHir).getLoopInitPart(), pLabelCorresp);
    adjustLabelInf(lLoopBody, pLabelCorresp);
    break;
  case HIR.OP_IF:
    if (fDbgLevel > 3)
      hirRoot.ioRoot.dbgHir.print(5, " adjustLabelInf ", pHir.toStringShort());
    adjustLabelInf(((IfStmt)pHir).getThenPart(), pLabelCorresp);
    adjustLabelInf(((IfStmt)pHir).getElsePart(), pLabelCorresp);
    break;
  case HIR.OP_SEQ:
    if (fDbgLevel > 3)
      hirRoot.ioRoot.dbgHir.print(5, " adjustLabelInf ", pHir.toStringShort());
    int lChildCount = pHir.getChildCount();
    for (int lInx = 1; lInx <= lChildCount; lInx++) {
      HIR lChild = (HIR)pHir.getChild(lInx);
      adjustLabelInf(lChild, pLabelCorresp);
    }
    break;
  case HIR.OP_LIST:
    if (fDbgLevel > 3)
      hirRoot.ioRoot.dbgHir.print(5, " adjustLabelInf ", pHir.toStringShort());
    //##87 for (HirIterator lIt2 = pHir.hirIterator(pHir);
    for (Iterator lIt2 = ((HirList)pHir).iterator(); //##87
         lIt2.hasNext(); ) {
      //##87 HIR lElem = lIt2.next();
      HIR lElem = (HIR)lIt2.next(); //##87
      adjustLabelInf(lElem, pLabelCorresp);
    }
    break;
  case HIR.OP_BLOCK:
    if (fDbgLevel > 3)
      hirRoot.ioRoot.dbgHir.print(5, " adjustLabelInf ", pHir.toStringShort());
    Stmt lStmt = ((BlockStmt)pHir).getFirstStmt();
    while (lStmt != null) {
      adjustLabelInf(lStmt, pLabelCorresp);
      lStmt = lStmt.getNextStmt();
    }
    break;
  case HIR.OP_LABELED_STMT:
    if (fDbgLevel > 3)
      hirRoot.ioRoot.dbgHir.print(5, " adjustLabelInf ", pHir.toStringShort());
    Stmt lStmtBody = ((LabeledStmt)pHir).getStmt();
    if (lStmtBody != null)
      adjustLabelInf(lStmtBody, pLabelCorresp);
    break;
  default:
    break;
  }
} // adjustLabelInf

//##85 END

//##91 BEGIN
/**
<PRE>
 * popoutStmtInExp pops out statements within expressions
 * in front of the statement where the popped out
 * statement can be placed.
 * When an HIR containing a block or a list where expression
 * is expected is produced by optimization, etc.,
 * the popoutStmtInExp method changes the HIR to a normal
 * HIR so that other modules can handle it.
 * When a block having ExpStmt as its last statement is
 * included where expression is expected, then statements
 * except the last one are popped out and the block
 * is replaced with the expression reppresented by the
 * last ExpStmt.
 * When a list having an expression as its last element is
 * included where expression is expected, then statements
 * included in the list are popped out and the list
 * is replaced with the last expression in the list.
 * If no modification is necessary, then the given parameter
 * (pStmt) itsself is returned.
</PRE>
 * @param pStmt statement that may contain statements where
 *     expression is expected.
 * @return the normalized HIR that does not contain statements
 *     where expression is expected.
 */
public HIR
popoutStmtInExp( Stmt pStmt )
{
  if (fDbgLevel > 0)
    ioRoot.dbgHir.print(2, "popoutStmtInExp", pStmt.toStringShort()
       + "\n  checkStmtInExp ");
  List lModPairList = new ArrayList();
  List lPeelList = new ArrayList();
  checkStmtInExp(pStmt, lModPairList, lPeelList, pStmt, false);
  if (lModPairList.isEmpty()) {
    if (fDbgLevel > 0)
      ioRoot.dbgHir.print(2, "\n No change in popoutStmtInExp");
    return pStmt;
  }
  Stmt lNewStmt = pStmt;
  Stmt lDefaultAncestor = pStmt;
  for (Iterator lIt = lModPairList.iterator();
       lIt.hasNext(); ) {
    Stmt lStmt = (Stmt)lIt.next();
    Stmt lAncestorStmt = (Stmt)lIt.next();
    if (lAncestorStmt == null)
      lAncestorStmt = lDefaultAncestor;
    if (fDbgLevel > 0)
      ioRoot.dbgHir.print(3, "\n move " + lStmt.toStringShort(),
        " in front of " + lAncestorStmt.toStringShort());
    lAncestorStmt.insertPreviousStmt((Stmt)lStmt.copyWithOperandsChangingLabels(null));
    lStmt.deleteThisStmt();
  }
  for (Iterator lIt2 = lPeelList.iterator();
       lIt2.hasNext(); ) {
    HIR lExp = (HIR)lIt2.next();
    HIR lParent = (HIR)lExp.getParent();
    if (lExp instanceof ExpStmt)
      lExp = ((ExpStmt)lExp).getExp();
    if (fDbgLevel > 0)
      ioRoot.dbgHir.print(3, "\n replace " + lParent.toStringShort(),
        " with " + lExp.toStringShort());
    lParent.replaceThisNode(lExp);
  }
  return pStmt;
}  // popoutStmtInExp

protected void // This method is called from popoutStmtInExp.
checkStmtInExp( HIR pHir, List pModPairList,
                List pPeelList,
                Stmt pAncestorStmt, boolean pWithinExp )
{
  if (fDbgLevel > 3)
    ioRoot.dbgHir.print(3, "checkStmtInExp", pHir
       + " in " + pAncestorStmt.toStringShort() + " " + pWithinExp);

  Stmt lAncestorStmt = pAncestorStmt;
  boolean lWithinExp = pWithinExp;
  if (pHir != null) {
    if (pWithinExp && (pHir instanceof BlockStmt)) {
      BlockStmt lBlockStmt = (BlockStmt)pHir;
      Stmt lLastStmt = lBlockStmt.getLastStmt();
      if (lLastStmt instanceof ExpStmt) {
        for (Stmt lStmt = lBlockStmt.getFirstStmt();
             (lStmt != null)&&(lStmt != lLastStmt);
             lStmt = lStmt.getNextStmt()) {
          pModPairList.add(lStmt);
          pModPairList.add(lAncestorStmt);
          if (fDbgLevel > 0)
            ioRoot.dbgHir.print(3, " (" + lStmt.toStringShort()
               + "," + lAncestorStmt.toStringShort() + ")");
           checkStmtInExp(lStmt, pModPairList, pPeelList,
                           lAncestorStmt, false);
        }
        pPeelList.add(lLastStmt);
        if (fDbgLevel > 0)
          ioRoot.dbgHir.print(3, "\n peel " + lLastStmt.toStringShort());
        return;
      }
    }else if (pWithinExp && (pHir instanceof Stmt)) {
      // Stmt other that BlockStmt
      pModPairList.add(pHir);
      pModPairList.add(lAncestorStmt);
      if (fDbgLevel > 0)
        ioRoot.dbgHir.print(3, " (" + pHir.toStringShort()
           + "," + lAncestorStmt.toStringShort() + ")");
      lWithinExp = false;
    }
    if (pHir.getChildCount() > 0) {
      if (pHir instanceof BlockStmt) {
        for (Stmt lStmt = ((BlockStmt)pHir).getFirstStmt();
             lStmt != null;
             lStmt = lStmt.getNextStmt()) {
          checkStmtInExp(lStmt, pModPairList, pPeelList,
            (BlockStmt)pHir, lWithinExp);
        }
      }else { // Not a block
        if (pHir instanceof Stmt)
          lAncestorStmt = (Stmt)pHir;
        if (pHir instanceof Exp)
          lWithinExp = true;
        boolean lWithinExpForChild = lWithinExp;
        HIR lChild1 = (HIR)pHir.getChild1();
        int lOperator = pHir.getOperator();
        if (lChild1 != null) {
          if ((lOperator == HIR.OP_ASSIGN)||
              (lOperator == HIR.OP_IF)||
              (lOperator == HIR.OP_RETURN)||
              (lOperator == HIR.OP_SWITCH)||
              (lOperator == HIR.OP_EXP_STMT) //##92
              ) {
            lWithinExpForChild = true;
          }
          checkStmtInExp(lChild1, pModPairList, pPeelList,
             lAncestorStmt, lWithinExpForChild);
        }
        for (int i = 2; i <= pHir.getChildCount(); i++) {
          HIR lChild = (HIR) pHir.getChild(i);
          if (lChild != null) {
            if (
                (((pHir instanceof LoopStmt)&&((i == 3)||(i == 5)))
                 &&(lChild instanceof Exp)) //##92
                ||((lOperator == HIR.OP_ASSIGN)&&(i == 2))) {
              // Start condition or end condition of the loop
              // or 2nd operand of assign stmt.
              lWithinExpForChild = true;
            }else {
              lWithinExpForChild = lWithinExp;
            }
            checkStmtInExp(lChild, pModPairList, pPeelList,
              lAncestorStmt, lWithinExpForChild);
          }
        }
      }
    }else { // Child count is 0.
      if (pHir instanceof IrList) {
        Object lObject;
        Object lLastObject = null;
        for (java.util.Iterator listIterator = ((IrList)pHir).iterator();
             listIterator.hasNext(); ) {
          lObject = listIterator.next();
          lLastObject = lObject;
          if (lObject instanceof HIR) {
            checkStmtInExp((HIR)lObject, pModPairList, pPeelList,
              lAncestorStmt, true);
          }
        }
        if ((lLastObject instanceof Exp)&&
            (pHir.getParent() != null)&&
            (pHir.getParent().getOperator() != HIR.OP_CALL)) {
          pPeelList.add(lLastObject);
          if (fDbgLevel > 0)
            ioRoot.dbgHir.print(3, " peel " +
              ((Exp)lLastObject).toStringShort());
        }
      }
    } // End of child count = 0.
  } // pHir != null
} // checkStmtInExp

//##91 END

//##88 BEGIN
/**
 * Called from finishHir() to modify HIR or
 * examine HIR if necessary.
 * This may be used also to change or examine HIR
 * for tentative purpose such as checking and debugging.
 * In that case, appropriate processing should be
 * tentatively added to this method.
 * @param pHir HIR to be modified/examined.
 * @return true if changed, false if not changed.
 */
public boolean
modifyHirIfNecerrary( HIR pHir )
{
  ioRoot.dbgHir.print(4, "\nmodifyHirIfNecessary " + pHir);
  ioRoot.dbgHir.print(4, " number of bits in addressing unit "
    + ioRoot.machineParam.numberOfBitsInAddressingUnit()); //###
  ioRoot.dbgHir.print(4, " sizeof pointer "
    + ioRoot.machineParam.evaluateSize(Type.KIND_POINTER)); //###
//------
// Add processing here tentatively
// and remove it when debug is completed.
//##92 BEGIN
  /* //##92
  if (pHir instanceof Program) {
    IrList lSubpDefList = ((Program)pHir).getSubpDefinitionList();
    for (Iterator lIt = lSubpDefList.iterator();
         lIt.hasNext(); ) {
      SubpDefinition lSubpDef = (SubpDefinition)lIt.next();
      //##92 modifyLoopStmt( lSubpDef);
      modifyIfStmt(lSubpDef); //##92
    }
  }
  */ //##92
//##92 END
//##95 BEGIN
// Show how to traverse HIR using visitor, and
// show an example of modifying HIR
 //       (test/c/OptAdd/tpconstFold3.c, tpconstFold4.c).
//  OptHirPrimary lOptHir = new OptHirPrimary(hirRoot);
//  lOptHir.visit((HIR)hirRoot.programRoot);
//##95 END
//------
  return false;
} //modifyHirIfNecessary

//##92 BEGIN
// modifyLoopStmt shows merely how to modify loop statements.
// Execution result of the modified program will be bad.
private void
modifyLoopStmt( SubpDefinition pSubpDef )
{
  SymTable lSymTable = pSubpDef.getSymTable();
  // Do not change HIR during traversing.
  List lLoopList = new ArrayList();
  for (HirIterator lIterator = hir.hirIterator(pSubpDef);
       lIterator.hasNext(); ) {
    HIR lHir = lIterator.next();
    if (lHir instanceof ForStmt) {
      lLoopList.add(lHir);
    }
  }
  if (lLoopList.isEmpty())
    return;
  // Modify HIR after finishing to traverse.
  if (lLoopList.size() > 1) {
    // Replace the loop control part of the 2nd loop.
    Var lVar1 = lSymTable.generateVar(hirRoot.symRoot.typeInt);
    ForStmt lForStmt1 = (ForStmt)lLoopList.get(1);
    Stmt lLoopBody =
      (Stmt)lForStmt1.getLoopBodyPart().copyWithOperandsChangingLabels(null);
    ForStmt lNewFor1 = hir.forStmt(
        hir.assignStmt(hir.varNode(lVar1),hir.intConstNode(0)),
        hir.exp(HIR.OP_CMP_LT, hir.varNode(lVar1), hir.intConstNode(5)),
        lLoopBody,
        hir.assignStmt(hir.varNode(lVar1),
           hir.exp(HIR.OP_ADD, hir.varNode(lVar1),hir.intConstNode(1))));
    lForStmt1.replaceThisStmtWith(lNewFor1);
  }
  if (lLoopList.size() > 0) {
    // Enclose the 1st loop with a new for-statement.
    Var lVar2 = lSymTable.generateVar(hirRoot.symRoot.typeInt);
    ForStmt lForStmt2 = (ForStmt)lLoopList.get(0);
    ForStmt lNewFor2 = hir.forStmt(
        hir.assignStmt(hir.varNode(lVar2),hir.intConstNode(0)),
        hir.exp(HIR.OP_CMP_LT, hir.varNode(lVar2), hir.intConstNode(8)),
        (Stmt)lForStmt2.copyWithOperandsChangingLabels(null),
        hir.assignStmt(hir.varNode(lVar2),
           hir.exp(HIR.OP_ADD, hir.varNode(lVar2),hir.intConstNode(1))));
    lForStmt2.replaceThisStmtWith(lNewFor2);
  }
} // modifyLoopStmt
//##92 END
//##93 BEGIN
private void
modifyIfStmt( SubpDefinition pSubpDef )
{
  SymTable lSymTable = pSubpDef.getSymTable();
  // Do not change HIR during traversing.
  List lIfList = new ArrayList();
  for (HirIterator lIterator = hir.hirIterator(pSubpDef);
       lIterator.hasNext(); ) {
    HIR lHir = lIterator.next();
    if (lHir instanceof IfStmt) {
      lIfList.add(lHir);
    }
  }
  if (lIfList.isEmpty())
    return;
  // Modify HIR after finishing to traverse.
  if (lIfList.size() > 0) {
    // Enclose the 1st loop with a new for-statement.
    Var lVar2 = lSymTable.generateVar(hirRoot.symRoot.typeInt);
    IfStmt lIfStmt2 = (IfStmt)lIfList.get(0);
    IrList lAttr = hir.irList();
    lIfStmt2.addInf(Registry.INF_KIND_OPEN_MP, lAttr);
    lAttr.add("#pragma omp reset ");
    hirRoot.ioRoot.printOut.print("\n IfStmt-1 ");
    lIfStmt2.print(2, true);
    IrList lAttr2 = hir.irList();
    lIfStmt2.addInf(Registry.INF_KIND_OPEN_MP, lAttr2);
    lAttr2.add("#pragma omp flush ");
    hirRoot.ioRoot.printOut.print("\n IfStmt-2 ");
    lIfStmt2.print(2, true);
  }
} // modifyIfStmt
//##93 END

} // HirModify class

