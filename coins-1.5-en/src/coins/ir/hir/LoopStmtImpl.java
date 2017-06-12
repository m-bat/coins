/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.CompileError;
import coins.HirRoot;
//##63 import coins.aflow.BBlock;
import coins.flow.LoopInf; //##63
import coins.ir.IrList;
import coins.sym.Label;

/**
<PRE>
 *  Loop-statement class.
// Components (children) of loop statement
//   child1: LoopInitPart. (Stmt)
//           This may be null.
//           getLoopInitPart returns this statement.
//   child2: ConditionalInitPart (This has been deleted. Give null.)
//           This is executed only once if loop condition is satisfied.
//           (At present, this is implemented by if-stmt attached to
//            LoopInitPart so that hir2lir has no need of
//            special treatment.)
//   child3: StartConditionPart with loopBackLabel. (LabeledStmt)
//           This should be given but its LabeledStmt may be null.
//           getLoopBackPoint() returns this LabeledStmt.
//   child4: LoopBody that is a LabeledStmt with loopBodyLabel.
//           StmtBody part of the LabeledStmt is BlockStmt
//           having LabeledStmt with loopStepLabel (LabeledStmt)
//           as the last statement of the loop-body BlockStmt.
//           An implementation of the LoopBody is
//           (labeledStmt
//            (list <labeleDef loopBodyLabel>)
//            (block
//             sequence of statements given as pLoopBody parameter
//               of setChildrenOfLoop
//             (labeledStmt
//              (list <labelDef loopStepLabel>)
//              null)))
//           LoopBody should be given but its BlockStmt may have
//           no executable statements.
//           getLoopBodyPart() returns this LabeledStmt.
//   child5: EndCondition.  (ExpStmt)
//           This may be null.
//           getLoopEndCondition() returns the expression of the ExpStmt.
//   child6: LoopStepPart jumping to loopBackLabel. (Stmt)
//           This may be null.
//           getLoopStepPart() returns this statement.
//   child7: LoopEndpart with loopEndLabel. (LabeledStmt)
//           This should be given but its LabeledStmt may be null.
//           getLoopEndPart() returns this LabeledStmt.
</PRE>
**/
public abstract class
LoopStmtImpl extends StmtImpl implements LoopStmt
{
  public //##85
  Label
    fLoopBackLabel,
    fLoopStepLabel,
    fLoopEndLabel;
  coins.flow.LoopInf fLoopInf= null;  // Loop information for this loop. //##63
  private BlockStmt fConditionalInitBlock = null; // then-part
                           // of conditional init if-statement.
                           // Null if conditional init is not yet given.

public
LoopStmtImpl()
{
}

public
LoopStmtImpl( HirRoot pHirRoot )
{
  super(pHirRoot);
}

public void
setChildrenOfLoop(
    Stmt   pInitPart,
    Label  pLoopBackLabel,
    Stmt   pConditionalInitPart,
    Exp    pStartCondition,
    Stmt   pLoopBody,
    Label  pLoopStepLabel,
    Exp    pEndCondition,
    Stmt   pLoopStepPart,
    Label  pLoopEndLabel,
    Stmt   pLoopEndPart )
{
  Label     lLoopBodyLabel, lLoopStepPartLabel,
            lConditionalInitLabel;
  Stmt      lLoopBody, lExpStmt;
  Stmt      lLoopBody2, lEndConditionStmt;
  BlockStmt lLoopBodyBlock;
  Stmt      lConditionalInitPart;
  Exp       lEndCondition, lInitCondition;
  LabeledStmt
    lLoopBackPoint        = null,
    lLoopBodyPoint        = null,
    lEndConditionPoint    = null,
    lLoopStepPoint        = null,
    lLoopEndPoint         = null;
  fLoopBackLabel = pLoopBackLabel;
  if (fLoopBackLabel == null)
    fLoopBackLabel = hirRoot.symRoot.symTableCurrent.generateLabel();
  fLoopBackLabel.setLabelKind(Label.LOOP_BACK_LABEL);
  fLoopBackLabel.setOriginHir(this);
  if (pStartCondition == null)
    lLoopBackPoint  = hirRoot.hir.labeledStmt(fLoopBackLabel, null);
  else lLoopBackPoint  = hirRoot.hir.labeledStmt(fLoopBackLabel,
                         hirRoot.hir.expStmt(pStartCondition));
  lLoopBody = pLoopBody;
  if (pLoopBody == null)
    lLoopBody = hirRoot.hir.blockStmt(null);
  if ((lLoopBody.getOperator() != HIR.OP_LABELED_STMT)||
      (lLoopBody.getLabel().getLabelKind() != Label.LOOP_BODY_LABEL)) { // S.Fukuda 2002.10.30
    lLoopBodyLabel = hirRoot.symRoot.symTableCurrent.generateLabel();
    lLoopBodyLabel.setLabelKind(Label.LOOP_BODY_LABEL);
    lLoopBodyLabel.setOriginHir(this);
    //##79 if (lLoopBody instanceof BlockStmt)
    //##79   lLoopBodyBlock = (BlockStmt)lLoopBody;
    //##79 else
      lLoopBodyBlock = hirRoot.hir.blockStmt(lLoopBody);
    lLoopBodyPoint  = hirRoot.hir.labeledStmt(lLoopBodyLabel, lLoopBodyBlock);
  }else { // LabeledStmt with LOOP_BODY_LABEL
    //##79 if (((LabeledStmt)lLoopBody).getStmt() instanceof BlockStmt) {
    //##79   lLoopBodyBlock = (BlockStmt)((LabeledStmt)lLoopBody).getStmt();
    //##79 }else { // Not a BlockStmt
      lLoopBody2 = ((LabeledStmt)lLoopBody).getStmt();
      if (lLoopBody2 != null)
        lLoopBody2.cutParentLink();
      lLoopBodyBlock = hirRoot.hir.blockStmt(lLoopBody2);
      ((LabeledStmt)lLoopBody).setStmt(lLoopBodyBlock);
    //##79 }
    lLoopBodyPoint = (LabeledStmt)lLoopBody;
  }
  fLoopStepLabel = pLoopStepLabel;
  if (fLoopStepLabel == null)
    fLoopStepLabel = hirRoot.symRoot.symTableCurrent.generateLabel();
  fLoopStepLabel.setLabelKind(Label.LOOP_STEP_LABEL);
  fLoopStepLabel.setOriginHir(this);
  lLoopStepPoint = hirRoot.hir.labeledStmt(fLoopStepLabel, null);
  lLoopBodyBlock.addLastStmt(lLoopStepPoint);
  lEndConditionStmt = hirRoot.hir.expStmt(pEndCondition);
  fLoopEndLabel = pLoopEndLabel;
  if (fLoopEndLabel == null)
    fLoopEndLabel = hirRoot.symRoot.symTableCurrent.generateLabel();
  fLoopEndLabel.setLabelKind(Label.LOOP_END_LABEL);
  fLoopEndLabel.setOriginHir(this);
  if (pLoopEndPart == null)
    lLoopEndPoint  = hirRoot.hir.labeledStmt(fLoopEndLabel, null);
  else {
    if (pLoopEndPart.getOperator() != HIR.OP_LABELED_STMT) // S.Fukuda 2002.10.30
      lLoopEndPoint  = hirRoot.hir.labeledStmt(fLoopEndLabel, pLoopEndPart);
    else
      lLoopEndPoint = (LabeledStmt)pLoopEndPart;
  }

  fAdditionalChild = new HIR[6];
  fChildCount = 7;
  //-- Set children.  Set conditionalInitPart later because
  //   it uses labels of other parts.
  this.setChildren(pInitPart, pConditionalInitPart,
                   lLoopBackPoint, lLoopBodyPoint, lEndConditionStmt);
  fAdditionalChild[3] = pLoopStepPart; // child(6)
  fAdditionalChild[4] = lLoopEndPoint; //child(7)
  if (pLoopStepPart != null)
    ((HIR_Impl)pLoopStepPart).fParentNode = this;
  ((HIR_Impl)lLoopEndPoint).fParentNode = this;
  if (pConditionalInitPart != null) { // Set conditionalInitPart.
    if (pStartCondition != null)
      lInitCondition
        = (Exp)(pStartCondition.copyWithOperandsChangingLabels(null));
    else
      lInitCondition = null;
    lConditionalInitPart
      = makeConditionalInitPart(lInitCondition, pConditionalInitPart);
    lConditionalInitLabel = lConditionalInitPart.getLabel();
    lConditionalInitLabel.setLabelKind(Label.LOOP_COND_INIT_LABEL);
    lConditionalInitLabel.setOriginHir(this);
    this.setChild2(lConditionalInitPart);
  }
  fMultiBlock = true;
  if (fDbgLevel > 6) //##67 //##67
    hirRoot.ioRoot.dbgHir.print(7, "LoopStmtImpl", this.toStringWithChildren()); //##59 //##74
} // setChildrenOfLoop

//==== Get some part of LoopStmt ====//

public Stmt
getLoopInitPart() {
  return (Stmt)fChildNode1;
}

public BlockStmt
getConditionalInitPart() {
  return fConditionalInitBlock;
}

public Stmt
getConditionalInitPart2() {
  return fConditionalInitBlock;
}

public Exp
getLoopStartCondition() {
  Exp lResult = null; //##71
  LabeledStmt lLoopBackPoint;
  lLoopBackPoint = (LabeledStmt)getChild(3);
  /* //##71
  if ((lLoopBackPoint != null)&&
      (lLoopBackPoint.getStmt() != null)&&
      (lLoopBackPoint.getStmt().getOperator() == HIR.OP_EXP_STMT))
    return ((ExpStmt)(lLoopBackPoint.getStmt())).getExp();
  else
   return null;
  */ //##71
  //##71 BEGIN
  if ((lLoopBackPoint != null)&&
      (lLoopBackPoint.getStmt() != null)) {
    Stmt lLoopBackStmt = lLoopBackPoint.getStmt();
    if (lLoopBackStmt instanceof LabeledStmt)
      lLoopBackStmt = ((LabeledStmt)lLoopBackStmt).getStmt();
    if (lLoopBackStmt instanceof ExpStmt) {
      lResult = ((ExpStmt)lLoopBackStmt).getExp();
    }
  }
  if (fDbgLevel > 3)
    hirRoot.ioRoot.dbgHir.print(5, " getLoopStartCondition " + lResult);
  return lResult;
  //##71 END
} // getLoopStartCondition

public LabeledStmt
getLoopBackPoint()
{
  return (LabeledStmt)fAdditionalChild[0];  // child(3)
} // getLoopBackPoint

//##76 public LabeledStmt
public Stmt //##76
getLoopBodyPart() {

  //##76 return (LabeledStmt)fAdditionalChild[1];         // child(4)
  return (Stmt)fAdditionalChild[1];         // child(4) //##76
}

public Exp
getLoopEndCondition()
{
  Exp lResult = null; //##71
  HIR lEndConditionPoint;
  lEndConditionPoint = (HIR)fAdditionalChild[2]; // child(5)
  if (lEndConditionPoint != null) {
    if (lEndConditionPoint.getOperator() == HIR.OP_EXP_STMT)
      lResult = (Exp)((ExpStmt)lEndConditionPoint).getExp();
    else if (lEndConditionPoint instanceof Exp)
      lResult = (Exp)lEndConditionPoint;
    //##71 BEGIN
    else if (lEndConditionPoint instanceof LabeledStmt) {
      LabeledStmt lLabeledStmt = (LabeledStmt)lEndConditionPoint;
      if (lLabeledStmt.getStmt() instanceof ExpStmt)
        lResult = (Exp)((ExpStmt)lLabeledStmt.getStmt()).getExp();
    }
    //##71 END
  }
  if (fDbgLevel > 3)
    hirRoot.ioRoot.dbgHir.print(5, " getLoopEndCondition " + lResult);
  return lResult;
} // getLoopEndCondition

public Stmt
getLoopStepPart() {
  return (Stmt)fAdditionalChild[3];  // child(6)
}

public LabeledStmt
getLoopEndPart() {
  return (LabeledStmt)fAdditionalChild[4];  // child(7)
}

//==== Get labels and some items of LoopStmt ====//

public Label
getLoopBackLabel() {
  return getLoopBackPoint().getLabel();
}

public Label
getLoopBodyLabel() {
  if (fAdditionalChild[1] instanceof LabeledStmt)  //##76
    return ((LabeledStmt)fAdditionalChild[1]).getLabel(); // child(4)
  else  //##76
    return null; //##76
}

public Label
getLoopStepLabel()
{
  return fLoopStepLabel;
}

private LabeledStmt
getLoopStepLabeledStmt() //##53
{
  //##76 LabeledStmt lLoopBody = getLoopBodyPart();
  Stmt lLoopBody = getLoopBodyPart(); //##76
  if (lLoopBody != null) {
    //##76 Stmt lLoopBodyBlock = lLoopBody.getStmt();
    Stmt lLoopBodyBlock = lLoopBody; //##76
    if (lLoopBody instanceof LabeledStmt) //##76
      lLoopBodyBlock = ((LabeledStmt)lLoopBody).getStmt();  //##76
    if (lLoopBodyBlock instanceof BlockStmt) {
      Stmt lLoopStepLabeledStmt = ((BlockStmt)lLoopBodyBlock).getLastStmt();
      if (lLoopStepLabeledStmt instanceof LabeledStmt) {
        return (LabeledStmt)lLoopStepLabeledStmt;
      }
    }
  }
  return null;
} // getLoopStepLabeledStmt

public Label
getLoopEndLabel() {
  return getLoopEndPart().getLabel();
}

public coins.flow.LoopInf //##63
getLoopInf()
{
  return fLoopInf;
}

public void
setLoopInf( LoopInf pLoopInf )
{
  fLoopInf = pLoopInf;
}

//==== Add statement to some part of LoopStmt ====//

public void
addToLoopInitPart( Stmt pStmt ) {
  Stmt lStmt, lNewStmt;
  if (pStmt == null)
    return;
  pStmt.cutParentLink();
  lStmt = getLoopInitPart();
  if (lStmt == null) {
    lNewStmt = (Stmt)pStmt.copyWithOperands();
  }else {
    //##70 lNewStmt = lStmt.combineStmt(pStmt, true);
    lNewStmt = ((Stmt)lStmt.copyWithOperands()).combineStmt(pStmt, true);
  }
  //##70 if (lNewStmt != lStmt)
  replaceSource(1, lNewStmt);
} // addToLoopInitPart

  public void
  addToConditionalInitPart( Stmt pStmt )
  {
    //##63 BBlock    lNewBBlock;
    Label     lNewLabel, lThenLabel;
    BlockStmt lBlockStmt;
    Stmt      lStmt1, lStmt2, lStmt3, lConditionalInit,
              lNewConditionalInit, lThenPart, lElsePart;
    Exp       lExp;
    if (pStmt == null)
      return;
    if (fDbgLevel > 3) //##58
      hirRoot.ioRoot.dbgHir.print( 4, "addToConditionalInitPart",
          pStmt.toStringWithChildren());
    if (getLoopStartCondition() == null) {
      //  LoopInitPart_ is changed as follows:
      //    {
      //      oroginal LoopInitPart_;
      //      { // ConditionalInitBlock.
      //        // getConditionalInitPart() returns this else-block.
      //        Sequence of statements added by addToConditionalInitPart;
      //      }
      //    }
      if (fConditionalInitBlock == null) {
        fConditionalInitBlock = hirRoot.hir.blockStmt(pStmt);
        addToLoopInitPart(fConditionalInitBlock);
      }else
        fConditionalInitBlock.addLastStmt(pStmt);
      return;
    }
    // LoopStartCondition is null.
    // LoopInitPart_ is changed as follows:
    //    {
    //      oroginal LoopInitPart_;
    //      if (loopStartConditionExpression == false) {
    //        jump to loopEndLabel;
    //      }else { // ConditionalInitBlock.
    //              // getConditionalInitPart() returns this else-block.
    //        Sequence of statements added by addToConditionalInitPart;
    //        jump to loopBodyLabel;
    //      }
    //    }
    lStmt3 = null;
    if (fConditionalInitBlock == null) {
      addConditionalInitToLoopInit(pStmt);
    }else {  // There is already conditionalInitBlock.
      lNewConditionalInit = fConditionalInitBlock.combineStmt(pStmt, true);
      if (lNewConditionalInit != fConditionalInitBlock)
        fConditionalInitBlock.replaceThisNode(lNewConditionalInit);
    }
  } // addToConditionalInitPart

  /** addConditionalInitToLoopInit:
   * Add conditional initiation if-statement to loop initiation part
   * by changing
   *   loopInitPart
   * to
   *   (block
   *     loopInitPart
   *     (if (startCondition)
   *       (block
   *          fConditionalInitThenPart2
   *          (jump loopBodyLabel) ) ) )
   * If loopInitPart is null, then
   *    (if (startCondition)
   *       (block
   *          fConditionalInitThenPart2
   *          (jump loopBodyLabel) ) )
   * is treated as new loopInitPart.
   * The parameter pConditionalInitpart is combined with
   * fConditionalInitThenPart2.
   * @param pConditionalInitPart: Statement to be executed when
   *     loopStartCondition is true before repeating to execute
   *     loop body part.
   */
  private void
  addConditionalInitToLoopInit(
      Stmt pConditionalInitPart )
  {
    Label     lLoopBodyLabel, lLoopEndLabel;
    BlockStmt lThenBlock;
    Exp       lStartCondition;
    Stmt      lConditionalInitIfStmt;
    Stmt      lLoopInitPart, lCombinedLoopInitPart;
    if (fDbgLevel > 3) //##58
      hirRoot.ioRoot.dbgHir.print( 4, "addConditionalInitToLoopInit",
          hirRoot.ioRoot.toStringObject(pConditionalInitPart));
        if (pConditionalInitPart == null) //##70
          return; //##70
    lLoopBodyLabel = getLoopBodyLabel();
    lLoopEndLabel  = getLoopEndLabel();
    lThenBlock = fConditionalInitBlock;
    if (lThenBlock == null) { // ConditionalInit has not yet appeared.
      lThenBlock = hirRoot.hir.blockStmt(pConditionalInitPart);
      lThenBlock.addLastStmt(hirRoot.hir.jumpStmt(lLoopBodyLabel));
      fConditionalInitBlock = lThenBlock;
      lStartCondition = getLoopStartCondition();
      if (lStartCondition == null)               // This does not occur. See
        lStartCondition = hirRoot.hir.trueNode();// addToConditionalInitPart
      lConditionalInitIfStmt = hirRoot.hir.ifStmt(
                   //##70 lStartCondition, lThenBlock,
                   (Exp)lStartCondition.copyWithOperands(), lThenBlock, //##70
                   hirRoot.hir.jumpStmt(lLoopEndLabel));
      lLoopInitPart = getLoopInitPart();
      if (lLoopInitPart == null) {         // Set lConditionalInitIfStmt
        setChild1(lConditionalInitIfStmt); // as LoopInitPart.
      }else {
        //##70 lCombinedLoopInitPart =
        //##70   lLoopInitPart.combineStmt(lConditionalInitIfStmt, true);
        //##70 if (lCombinedLoopInitPart != lLoopInitPart)
        //##70 replaceSource1(lCombinedLoopInitPart);
        //##70 BEGIN
        if (lLoopInitPart instanceof BlockStmt) {
          ((BlockStmt)lLoopInitPart).addLastStmt(lConditionalInitIfStmt);
        }else {
          lCombinedLoopInitPart = ((Stmt)lLoopInitPart.copyWithOperands())
            .combineStmt(lConditionalInitIfStmt, true);
          replaceSource1(lCombinedLoopInitPart);
        }
        //##70 END
      }
    }else { // ConditionalInit has already appeared.
      lThenBlock.addBeforeBranchStmt(pConditionalInitPart);
    }
    this.setFlag(HIR.FLAG_LOOP_WITH_CONDITIONAL_INIT, true);
    if (fDbgLevel > 3) //##58
      hirRoot.ioRoot.dbgHir.print( 4, "init " +
          hirRoot.ioRoot.toStringObject(getLoopInitPart()) + " then " +
          hirRoot.ioRoot.toStringObject(fConditionalInitBlock));
  } // addConditionalInitToLoopInit

protected Stmt
makeConditionalInitPart(
    Exp pStartCondition, Stmt pConditionalInitPart )
{
  Label     lLoopBodyLabel, lLoopEndLabel,
            lConditionalInitLabel;
  BlockStmt lThenBlock;
  Exp       lStartCondition;
  Stmt      lConditionalInitStmt;
  lLoopBodyLabel = getLoopBodyLabel();
  lLoopEndLabel  = getLoopEndLabel();
  lConditionalInitLabel = hirRoot.symRoot.symTableCurrent.generateLabel();
  lThenBlock = hirRoot.hir.blockStmt(pConditionalInitPart);
  lThenBlock.addLastStmt(hirRoot.hir.jumpStmt(lLoopBodyLabel));
  lStartCondition = pStartCondition;
  if (lStartCondition == null)
    lStartCondition = hirRoot.hir.trueNode();
  lConditionalInitStmt = hirRoot.hir.labeledStmt(lConditionalInitLabel,
      hirRoot.hir.ifStmt(lStartCondition, lThenBlock,
                 hirRoot.hir.jumpStmt(lLoopEndLabel)));
  if (fDbgLevel > 3) //##58
    hirRoot.ioRoot.dbgHir.print( 4, "makeConditionalInitPart",
        hirRoot.ioRoot.toStringObject(lConditionalInitStmt));
  return lConditionalInitStmt;
} // makeConfitionalInitPart

public void
addToLoopBodyPart( Stmt pStmt ) {
  Stmt lStmt, lNewStmt;
  if (pStmt == null)
    return;
  pStmt.cutParentLink();
  //##76 LabeledStmt lLoopBodyPart = getLoopBodyPart();
  //##76 Stmt lLoopBodyPartStmt = lLoopBodyPart.getStmt();
  Stmt lLoopBodyPartStmt = getLoopBodyPart(); //##76
  if (lLoopBodyPartStmt instanceof LabeledStmt) //##76
    lLoopBodyPartStmt = ((LabeledStmt)lLoopBodyPartStmt).getStmt(); //##76
  LabeledStmt lStepLabeledStmt = getLoopStepLabeledStmt();
  if ((lLoopBodyPartStmt instanceof BlockStmt)&&
      (lStepLabeledStmt.getParent() == lLoopBodyPartStmt)) {
    lStepLabeledStmt.insertPreviousStmt(pStmt);
    return;
  }
  lNewStmt = lLoopBodyPartStmt.combineStmt(pStmt, false); //### Should be before loop step labeledStmt
  if (lNewStmt != lLoopBodyPartStmt) {
    if (lLoopBodyPartStmt instanceof LabeledStmt) //##76
      lLoopBodyPartStmt.setChild1(lNewStmt);
    else //##76
      lLoopBodyPartStmt.replaceThisStmtWith(lNewStmt); //##76
  }
} // addToLoopBodyPart

public void
addToLoopStepPart( Stmt pStmt ) {
  Stmt lStmt, lNewStmt;
  if (pStmt == null)
    return;
  pStmt.cutParentLink();
  lStmt = getLoopStepPart();
  if (lStmt == null)
    setChild(6, pStmt);
  else {
    /* //##71
    lNewStmt = lStmt.combineStmt(pStmt, false);
    if (lNewStmt != lStmt)
      replaceSource(6, lNewStmt);
    */ //##71
   if (lStmt instanceof BlockStmt) {
     ((BlockStmt)lStmt).addBeforeBranchStmt(pStmt);
   }else {
     if (lStmt instanceof LabeledStmt) {
       LabeledStmt lLabeledStmt = (LabeledStmt)lStmt;
       if (lLabeledStmt.getStmt() == null)
         lLabeledStmt.setStmt(pStmt);
       else {
         lLabeledStmt.getStmt().combineStmt(pStmt, true);
       }
     }
   }
  }
} // addToLoopStepPart

public void
addToLoopEndPart( Stmt pStmt ) {
  Stmt lStmt, lNewStmt;
  if (pStmt == null)
    return;
  pStmt.cutParentLink();
  lStmt = getLoopEndPart();
  lNewStmt = lStmt.combineStmt(pStmt, false);
  if (lNewStmt != lStmt)
    replaceSource(7, lStmt);
}

//==== Set or replace some part of LoopStmt ====//

public void
setLoopStartCondition(Exp pCondition)
{
  LabeledStmt lLoopBackPoint;
  lLoopBackPoint = (LabeledStmt)fAdditionalChild[0]; // child(3)
  /* //##59
  if ((lLoopBackPoint != null)&&
      (lLoopBackPoint.getStmt() != null)&&
      (lLoopBackPoint.getStmt().getOperator() == HIR.OP_EXP_STMT)) {
    ((ExpStmt)(lLoopBackPoint.getStmt())).setExp(pCondition);
  }
  */ //##59
  //##59 BEGIN
  if (lLoopBackPoint != null) {
    if (lLoopBackPoint.getStmt() == null) {
      lLoopBackPoint.setStmt(hirRoot.hir.expStmt(pCondition));
    }else {
      if (lLoopBackPoint.getStmt().getOperator() == HIR.OP_EXP_STMT) {
        ((ExpStmt)(lLoopBackPoint.getStmt())).setExp(pCondition);
      }else {
        lLoopBackPoint.setStmt(hirRoot.hir.expStmt(pCondition));
      }
    }
  }
  //##59 END
} // setLoopStartCondition

public void
setLoopEndCondition(Exp pCondition)
{
  ExpStmt lEndConditionPoint;
  lEndConditionPoint = (ExpStmt)fAdditionalChild[2];  // child(5)
  if ((lEndConditionPoint != null)&&
      (lEndConditionPoint.getOperator() == HIR.OP_EXP_STMT))
    lEndConditionPoint.setExp(pCondition);
} // setLoopEndCondition

public void
replaceConditionalInitPart( LabeledStmt pNewStmt ) throws CompileError {
  Stmt  lStmtBody;
  Label lLabel;
  if (getConditionalInitPart() != null)
    getConditionalInitPart().getLabel().
      setLabelKind(Label.UNCLASSIFIED_LABEL);
  if (pNewStmt == null) {
    replaceSource2(pNewStmt);
    return;
  }
  try {
    lStmtBody = pNewStmt.getStmt();
    lLabel = pNewStmt.getLabel();
    if ((lStmtBody == null)||
        (lStmtBody.getOperator() != HIR.OP_IF)) {
      throw new CompileError(1211, "ConditionalInitPart does not take if-statement form");
    }
    replaceSource2(pNewStmt);
    lLabel.setLabelKind(Label.LOOP_COND_INIT_LABEL);
    lLabel.setOriginHir(this);
  }catch (CompileError error) {
    hirRoot.ioRoot.msgRecovered.put(error.getMessage());
  }
} // replaceConditionalInitPart

public void
replaceBodyPart( LabeledStmt pNewStmt ) {
  Label lLabel;
  Stmt lLoopBodyStmt;
  BlockStmt lLoopBodyBlock;
  if (getLoopBodyPart() != null)
    getLoopBodyPart().getLabel().
      setLabelKind(Label.UNCLASSIFIED_LABEL);
  lLabel = pNewStmt.getLabel();
  lLoopBodyStmt = pNewStmt.getStmt();
  if (lLoopBodyStmt instanceof BlockStmt)
    lLoopBodyBlock = (BlockStmt)lLoopBodyStmt;
  else {
    lLoopBodyBlock = hirRoot.hir.blockStmt(lLoopBodyStmt);
    pNewStmt.setChild1(lLoopBodyBlock);
  }
  replaceSource(4, pNewStmt);
  lLabel.setLabelKind(Label.LOOP_BODY_LABEL);
  lLabel.setOriginHir(this);
  LabeledStmt lLoopStepLabeledStmt = getLoopStepLabeledStmt();
  if (lLoopStepLabeledStmt == null) {
    Label lLoopStepLabel = hirRoot.symRoot.symTableCurrent.generateLabel();
    lLoopStepLabel.setLabelKind(Label.LOOP_STEP_LABEL);
    lLoopStepLabel.setOriginHir(this);
    fLoopStepLabel = lLoopStepLabel;
    lLoopStepLabeledStmt = hirRoot.hir.labeledStmt(fLoopStepLabel, null);
    lLoopBodyBlock.addLastStmt(lLoopStepLabeledStmt);
  }
} // replaceBodyPart

public boolean
isSimpleForLoop()
{
  if ((this instanceof ForStmt)&&
      isEmpty((HIR)getChild2())&&  // conditional init
      isEmpty((HIR)getChild(5)))   // end condition
    return true;
  else
    return false;
}

public boolean
isSimpleWhileLoop()
{
  if ((this instanceof WhileStmt)&&
      isEmpty((HIR)getChild1())&&   // loop init
      isEmpty((HIR)getChild2())&&   // conditional init
      isEmpty((HIR)getChild(5))&&   // end condition
      isEmpty((HIR)getChild(6)))    // loop step
    return true;
  else
    return false;
}

public boolean
isSimpleRepeatLoop()
{
  if ((this instanceof RepeatStmt)&&
      isEmpty((HIR)getChild1())&&  // loop init
      isEmpty((HIR)getChild2())&&  // conditional init
      isEmpty((HIR)getChild(3))&&  // start condition
      isEmpty((HIR)getChild(6)))   // loop step
    return true;
  else
    return false;
}

public boolean
isSimpleUntilLoop()
{
  if ((this instanceof UntilStmt)&&
      isEmpty((HIR)getChild1())&&   // loop init
      isEmpty((HIR)getChild2())&&   // conditional init
      isEmpty((HIR)getChild(3))&&   // start condition
      isEmpty((HIR)getChild(6)))    // loop step
    return true;
  else
    return false;
}

public boolean
isSimpleIndexedLoop()
{
  if ((this instanceof IndexedLoopStmt)&&
      isEmpty((HIR)getChild2())&&  // conditional init
      isEmpty((HIR)getChild(5)))   // end condition
    return true;
  else
    return false;
}

//==== Methods that override super class method ====//

public boolean
isLoopStmt() { return true; }

/**Combine pStmt with conditional expression part pCond
 * of control statement so that pStmt should be executed before
 * pCond.
 * @param pStmt statement to be executed before pCond.
 * @param pCond conditional expression to be combined with pStmt.
 */
public void
combineWithConditionalExp(Stmt pStmt, HIR pCond) //##53
{
  LabeledStmt lStartConditionPart = (LabeledStmt)getChild(3);
  ExpStmt     lEndConditionPart   = (ExpStmt)getChild(5);
  Stmt        lOriginalStmt, lResultStmt; //##71
  if (fDbgLevel > 3) //##58
    hirRoot.ioRoot.dbgHir.print(4, "combineWithConditionalExp ",
              toStringShort() + " " + pStmt.toStringWithChildren()
              + " Condition " + pCond.toStringWithChildren()
              + " StartCond " + lStartConditionPart +
              " EndCond " + lEndConditionPart + "\n");
  if ((lStartConditionPart != null)&&
      (lStartConditionPart.getStmt() != null)&&
      (lStartConditionPart.getStmt().contains(pCond))) {
    // Add pStmt to the tail of initiation part and
    // to the tail of step part.
    Stmt lInitiationPart = getLoopInitPart();
    if (lInitiationPart == null)
      setChild(1, pStmt);
    else {
      lOriginalStmt = lInitiationPart; //##71
      lResultStmt = lInitiationPart.combineStmt(pStmt, true);
      lOriginalStmt.cutParentLink(); //##71
      setChild1(lResultStmt); //##71
    }
    if (fDbgLevel > 3) //##58
      hirRoot.ioRoot.dbgHir.print(4, "StartConditionPart contains "
        + pCond.toStringShort(),
        "modifiedInitPart " +getLoopInitPart());
    addToLoopStepPart((Stmt)pStmt.copyWithOperands());
    if (fDbgLevel > 3) //##58
      hirRoot.ioRoot.dbgHir.print(4, " modifiedStepPart " +getLoopStepPart().toStringWithChildren());
  }else if (lEndConditionPart.contains(pCond)) {
    if (fDbgLevel > 3) //##71
     hirRoot.ioRoot.dbgHir.print(4, "EndConditionPart contains ",
               pCond.toStringShort() + " Add to loop step part.");  //##71
    // Combine pStmt with labeled statement with loop step label.
    LabeledStmt lLoopStepLabeledStmt = getLoopStepLabeledStmt();
    if (lLoopStepLabeledStmt != null) {
      lLoopStepLabeledStmt.combineStmt(pStmt, true);
      if (fDbgLevel > 3) //##58
        hirRoot.ioRoot.dbgHir.print(4, " modifiedStepPart " +getLoopStepLabeledStmt());
    }else
      hirRoot.ioRoot.msgRecovered.put(5024,
       "combineWithConditionalExp got null from getLoopStepLabeledStmt() " + this);
  }else {
    hirRoot.ioRoot.msgRecovered.put(5024,
     "combineWithConditionalExp has illegal ExpStmt parameter " + pCond);
  }
  return;
} // combineWithConditionalExp

public Object
clone()  throws CloneNotSupportedException {
  LoopStmt lTree;
  try {
    lTree = (LoopStmt)(super.clone());
  }catch (CloneNotSupportedException e) {
    hirRoot.ioRoot.msgRecovered.put(1100,
      "CloneNotSupportedException(LoopStmtImpl) " + this.toString());
    return null;
  }
  ((LoopStmtImpl)lTree).fLoopBackLabel = fLoopBackLabel;
  ((LoopStmtImpl)lTree).fLoopStepLabel = fLoopStepLabel;
  ((LoopStmtImpl)lTree).fLoopEndLabel  = fLoopEndLabel;
  return (Object)lTree;
} // clone

/* //##85
public HIR
copyWithOperandsChangingLabels( IrList pLabelCorrespondence)
{
  LoopStmtImpl lTree = (LoopStmtImpl)(super.copyWithOperandsChangingLabels(
                        pLabelCorrespondence));
  lTree.fLoopBackLabel = ((Stmt)(lTree.getChild(3))).getLabel(); // start cond
  // Loop step label is the label attached to the last statement
  // of the loop body.
  Stmt lLoopBody  = lTree.getLoopBodyPart();
  if (lLoopBody instanceof LabeledStmt)
    lLoopBody = ((LabeledStmt)lLoopBody).getStmt();
  lTree.fLoopStepLabel = null;
  if (lLoopBody instanceof BlockStmt) {
    lTree.fLoopStepLabel = ((BlockStmt)lLoopBody).getLastStmt().getLabel();
  }
  if (lTree.fLoopStepLabel == null)
    lTree.fLoopStepLabel = ((Stmt)(lTree.getChild(5))).getLabel(); // end condition
  lTree.fLoopEndLabel  = ((Stmt)(lTree.getChild(7))).getLabel(); // loop end
  return (HIR)lTree;
} // copyWithOperandsChangingLabels
*/ //##85

public void
accept( HirVisitor pVisitor )
{
  pVisitor.atLoopStmt(this);
}

} // LoopStmtImpl class
