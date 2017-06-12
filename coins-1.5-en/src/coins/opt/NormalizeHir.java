/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.opt;

import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.lang.Integer; //##62

import coins.FlowRoot;
import coins.IoRoot;
import coins.SymRoot;
//##60 import coins.flow.AssignFlowExpId;
import coins.flow.BBlockSubtreeIterator; //##60
import coins.flow.ExpVector; //##60
import coins.flow.ExpVectorImpl; //##60
import coins.flow.ExpVectorIterator; //##60
import coins.flow.FlowAnalSymVector; //##60
//##60 import coins.flow.FlowExpId;
//##60 import coins.flow.FlowResults;
//##60 import coins.flow.InitiateFlowHir;
import coins.flow.SetRefRepr;
import coins.flow.SetRefReprList;
import coins.flow.SubpFlow;
import coins.flow.BitVector;
import coins.flow.BitVectorIterator;
import coins.flow.FAList;
import coins.flow.FAListIterator;
import coins.ir.IR;
import coins.ir.IrList;
import coins.ir.hir.AssignStmt;
import coins.ir.hir.BlockStmt;
import coins.ir.hir.Exp;
import coins.ir.hir.HIR;
import coins.ir.hir.HirVisitorModel2;
import coins.ir.hir.VarNode;
import coins.flow.BBlock;
import coins.flow.BBlockHir;
import coins.flow.BBlockHirImpl;
import coins.flow.BBlockNodeIterator;
import coins.flow.ControlFlow; //##60
import coins.flow.DataFlow; //##60
import coins.flow.Flow;
import coins.flow.FlowAnalSymVector;
import coins.flow.HirSubpFlowImpl;
import coins.flow.SetRefReprHirEImpl;
import coins.flow.ShowControlFlow;
import coins.flow.ShowDataFlow;
import coins.flow.SubpFlowImpl;
import coins.alias.AliasAnalHir1;
import coins.alias.AliasGroup;
import coins.alias.RecordAlias;
import coins.ir.hir.AssignStmt;
import coins.ir.hir.BlockStmt;
import coins.ir.hir.ExpStmt;
import coins.ir.hir.FunctionExp;
import coins.ir.hir.HirIterator;
import coins.ir.hir.HIR;
import coins.ir.hir.HirList;
import coins.ir.hir.HirVisitorModel2;
import coins.ir.hir.IfStmt;
import coins.ir.hir.JumpStmt;
import coins.ir.hir.LabelDef;
import coins.ir.hir.LabeledStmt;
import coins.ir.hir.LoopStmt;
import coins.ir.hir.ReturnStmt;
import coins.ir.hir.Stmt;
import coins.ir.hir.SubpDefinition;
import coins.ir.hir.SwitchStmt;
import coins.sym.EnumType;
import coins.sym.ExpId;
import coins.sym.ExpIdImpl;
import coins.sym.FlowAnalSym;
import coins.sym.Label;
import coins.sym.PointerType;
import coins.sym.Sym;
import coins.sym.Type;
import coins.sym.Var;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 <PRE>
 * NormalizeHir
 *   normalizes HIR to prepare for PRE.
 *
 *   Remove critical edge (an edge from a basic block with multiple
 *   successor to a basic block with multiple predecessor) by inserting
 *   an empty basic block in the critical edge.
 *
 * if (condition) then goto joinLabel
 *  ==> unnecessary to change.
 *  (can be inserted ahead of goto-statement in thenBBlock.)
 *
 * if (condition) then thenBBlock else elseBBlock;
 *  ==> unnecessary to change.
 *
 * if (condition1) { // if (condition1 && condition2) ...
 *  if (condition2) {
 *  thenLabel2:
 *  thenPart1; goto endIfLabel;
 *  }else
 *  elseLabel2:
 *  goto elsLabel1;
 * }else {
 *  elseLabel1:
 *   elsePart;
 * endIfLabel:
 *   ==>
 *   if (condition1) {
 *  if (condition2) {
 *  thenLabel2:
 *    thenPart1; goto endIfLabel;
 *  }else
 *  elseLabel2:
 *    goto elsLabel1-2;
 *   }else { (block
 *  elseLabel1:
 *  elseLabel1-2:
 *   elsePart;
 *   ) // block end
 *   endIfLabel:
 *
 * if (condition1) { // if (condition1 || condition2) ...
 *  thenLabel1:
 *  thenPart; goto endIfLabel;
 * }else {
 *  elseLabel1:
 *  if (condition2) {
 *  thenLabel2:
 *  goto thenLabel1;
 *  }else
 *  elseLabel2:
 *  elsePart;
 *  }
 * endIfLabel:
 *   ==>
 *   if (condition1) {
 *  thenLabel1: (block
 *    thenLabel1-2:
 *    thenPart; goto endIfLabel;
 *  ) // block end
 *   }else {
 *  elseLabel1:
 *  if (condition2) {
 *  thenLabel2:
 *    goto thenLabel1-2;
 *  }else
 *  elseLabel2:
 *    elsePart;
 *  }
 *   endIfLabel:
 *
 * if (condition) then thenBBlock; (null elseBBlock)
 *  ==> if (condition) then thenBBlock else emptyBBlock;
 *
 * if (condition) else elseBBlock; (null thenBBlock)
 *  ==> if (condition) then emptyBBlock else elseBBlock;
 *
 * swith (exp) { case I1: statement1; break;
 *     case I2; statement2; break;
 *     ... default: defaultBBlock; }
 *  ==> unnecessary to change.
 *
 * swith (exp) { case I1: case I2; statement2; break;
 *     ...
 *     default: defaultBBlock; }
 *  ==>
 *   swith (exp) { case (I1-I2): // case label I1, I2 are
 *     // attached to one statement.
 *     statement2; goto swithcEndLabel;
 *     ... default: defaultBBlock; }
 *
 * swith (exp) { case I1: statement1;
 *     case I2: statement2; break;
 *     ...
 *     case In: statementn;
 *     default: defaultBBlock; }
 *  ==>
 *   swith (exp) { case I1: statement1; goto L2;
 *     case I2; goto L2;
 *    L2: statement2; goto swithcEndLabel;
 *     case In: statementn; goto Ln;
 *     ...
 *     default:
 *    Ln: defaultBBlock; }
 *
 * (loop
 *   loopInitPart
 *   loopBackLabel:
 *   startCondition (if (! startCondition) goto loopEndLabel)
 *   loopBodyLabel:
 *   loopBody  (without break;)
 *   loopStepLabel:null
 *   // endCondition is null
 *   loopStepPart (goto loopBackLabel)
 *   loopEndLabel:
 *   loopEndPart
 *  )
 *  ==>  unnecessary to change.
 *
 * (loop
 *   loopInitPart
 *   loopBackLabel:
 *   // startCondition is null
 *   loopBodyLabel:
 *   loopBody  (without break;)
 *   loopStepLabel:null
 *   endCondition (if (! endCondition) goto loopEndLabel)
 *   loopStepPart (goto loopBackLabel)
 *   loopEndLabel:
 *   loopEndPart
 *  )
 *  ==>
 * (loop
 *   loopInitPart
 *   (jump loopBodyLabel) -- insert
 *   loopBackLabel:
 *   // startCondition is null
 *   loopBodyLabel:
 *   loopBody  (without break;)
 *   loopStepLabel:null
 *   endCondition (if (! endCondition) goto loopEndLabel)
 *   loopStepPart (goto loopBackLabel)
 *   loopEndLabel:
 *   loopEndPart
 *  )
 *
 * (loop
 *   loopInitPart
 *   loopBackLabel:
 *   startCondition (if (! startCondition) goto loopEndLabel)
 *   loopBodyLabel:
 *   loopBody with break
 *     (if (breakCondition) goto loopEndLabel)
 *   rest of loopBody
 *   loopStepLabel: // loopEndCondition is not null.
 *     if (! loopEndCondition) goto loopEndLabel;
 *   loopStepPart (goto loopBackLabel)
 *   loopEndLabel:
 *   loopEndPart
 *  )
 *  ==>
 *  (block
 *  (loop
 *     loopInitPart
 *     loopBackLabel:
 *   startCondition (if (! startCondition) goto loopEndLabel)
 *     loopBodyLabel:
 *   loopBody with break
 *   (if (breakCondition) goto loopExitLabel)
 *   rest of loopBody
 *   loopStepLabel:  // loopEndCondition is not null.
 *   if (! loopEndCondition) goto loopExitLabel;
 *     loopStepPart (goto loopBackLabel)
 *     loopEndLabel: // set loopEndPart as null)
 *   ) // loop end
 *   loopExitLabel:
 *     loopEndPart
 *   ) // block end
 *
 * (loop // Most general loop.
 *   loopInitPart;
 *   if (startCondition) {
 *   statements added by addToconditionalInitPart
 *   goto loopBodyLabel;
 *   }else goto looEndLabel;
 *   loopBackLabel:
 *   startConditionPart(if (! startCondition) goto loopEndLabel;)
 *   loopBodyLabel:
 *   loopBody;
 *   if (breakCondition) goto loopEndLabel;
 *   ...
 *   if (continueCondition) goto loopStepLabel;
 *   ...
 *   loopStepLabel:
 *   endCondition (if (! endCondition) goto loopEndLabel)
 *   loopStepPart (goto loopBackLabel;)
 *   loopEndLabel:
 *   loopEndPart;
 * )
 *  ==>
 * (block
 *  (loop
 *     loopInitPart;
 *     if (startCondition) {
 *   statements added by addToconditionalInitPart
 *   goto conditionalInitLabel;
 *     }else goto loopExitLabel;
 *     loopBackLabel:
 *   startConditionPart(if (! startCondition) goto loopEndLabel)
 *     loopBodyLabel: null
 *     conditionalInitLabel:
 *   loopBody;
 *   if (breakCondition) goto loopExitLabel;
 *   ...
 *   if (continueCondition) goto loopStepLabel;
 *   ...
 *   loopStepLabel:null
 *     endCondition (if (! endCondition) goto loopEndLabel)
 *     loopStepPart (goto loopBackLabel)
 *     loopEndLabel: null
 *   ) // loop end
 *   loopExitlabel:
 *  loopEndPart;
 *   ) // block end
 *
 *  Rules for loop conversion:
 *  If break or conditionalInit are contained in the loop,
 *  add loopExitlabel after loopEndLabel and attach loopEndPart
 *  to loopExitLabel, and then change loopEndLabel of jump
 *  statements in conditionalInit and break to loopExitLabel.
 *  Also change loopBodyLabel of conditionalInit to
 *  conditionalInitLabel attached after loopBodyLabel.
 </PRE>
 */
public class
  NormalizeHir
{
  IoRoot ioRoot;
  SymRoot symRoot;
  FlowRoot flowRoot;
  Flow flow; //##60
  SubpFlow fSubpFlow;
  HIR hir;
  SubpDefinition fSubpDef;

  /** fTransformed is set to true if HIR is changed by normalization,
   * else it is set to false.
   */
  protected boolean fTransformed;

  /** fStmtWithCriticalEdge lists control statements having
   * critical edges. The listed statements may be
   * IfStmt, LoopStmt, SwitchStmt.
   * This should be maintained (by adjustLinkages) when
   * statements are copied/deleted.
   */
  protected List fStmtWithCriticalEdge; //##62

  /**
   * fStmtWithCriticalEdge[fCriticalStmtIndex] is the statement
   * under processing by eliminateCriticalEdge.
   */
  protected int fCriticalStmtIndex;

  /** fCriticalEdgeTargetLabels is the set of labels of critical edge
   *  target statements.
   */
  protected List fCriticalEdgeTargetLabels;

  /** fJumpListt is the list of jump statements in thte current subprogram.
   * If the current subprogram has no critical edge, it is empty.
   * This is used at the first statge of eliminateCritical edges
   * and after that, it is not necessary to maintain.
   */
  //protected List fJumpList;

  protected int
    fDbgLevel;
  /**
   * Constructor to normalize HIR.
   * @param pFlowRoot
   */
  public NormalizeHir(FlowRoot pFlowRoot, SubpDefinition pSubpDef)
  {
    flowRoot = pFlowRoot;
    flow = pFlowRoot.flow;
    ioRoot = pFlowRoot.ioRoot;
    symRoot = pFlowRoot.symRoot;
    hir = pFlowRoot.hirRoot.hir;
    fSubpFlow = flow.getSubpFlow(); //##60
    fSubpDef = pSubpDef;
    fTransformed = false;
    //##62 fCriticalEdgeMap = new HashMap();
    fStmtWithCriticalEdge = new LinkedList();
    //##62 fJumpMap = new HashMap();
    fDbgLevel = ioRoot.dbgOpt1.getLevel(); //##60
  } // NormalizeHir

  protected boolean
    processCriticalEdge()
  {
    boolean lDetected = false;
    BBlock lBBlock, lFromBBlock;
    BBlock lPredBBlock, lSuccBBlock;
    HIR lFromHir, lToHir; // HIR linked to start/target block of an edge.
    HIR lParent;
    Stmt lParentFrom = null, lParentTo = null;
    Stmt lContainingStmt, lTargetStmt;
    List lPredList, lSuccList;
    ListIterator lIterator;
    int maxBBlockNo;
    int i;

    //		maxBBlockNo = pSubpFlow.getNumberOfRelevantBBlocks();
    maxBBlockNo = fSubpFlow.getNumberOfBBlocks();

    if (fDbgLevel > 1)
      dbg(2, " processCriticalEdge for",
        fSubpDef.getSubpSym().toStringShort() +
        " " + ioRoot.getSourceFile().getName()
        + " maxBBlockNo " + maxBBlockNo + "\n");

    // Build fJumpTargetMap.
    //##62 fJumpTargetMap = new HashMap();
    //##62 fCriticalEdgeTarget = new HashSet();
    fCriticalEdgeTargetLabels = new LinkedList();
    /* //##62
    for (i = 1; i <= maxBBlockNo; i++) {
      lBBlock = fSubpFlow.getBBlock(i);
      LabeledStmt lBBlockHead = (LabeledStmt)lBBlock.getIrLink();
      IrList lLabelDefList = lBBlockHead.getLabelDefList();
      for (Iterator lLabelIterator = lLabelDefList.iterator();
           lLabelIterator.hasNext(); ){
        LabelDef lLabelDef = (LabelDef)lLabelIterator.next();
        Label lLabel = lLabelDef.getLabel();
        fJumpTargetMap.put(lLabel, lBBlockHead);
      }
    }
    if (fDbgLevel > 0)
      dbg(5, "JumpTargetMap", fJumpTargetMap.toString());
    */ //##62

    // Find critical edges and build fCriticalEdgeMap, fJumpMap
    // and fCriticalEdgeTarget, fCriticalEdgeTargetLabels.
    for (i = 1; i <= maxBBlockNo; i++) {
      lFromBBlock = fSubpFlow.getBBlock(i);
      lSuccList = lFromBBlock.getSuccList();
      lFromHir = (HIR)lFromBBlock.getIrLink();
      // lFromHir: 1st Stmt of edge starting block.
      lContainingStmt = null;
      if (lSuccList.size() >= 2) {
        LinkedList lEdgeList = new LinkedList(); // List of critical edges(from-to pair).
        //##62 LinkedList lLabelList = new LinkedList(); // List of target label of
                                    // critical edges in this BBlock ?.
        Stmt lCompoundControlStmtOfEdgeStart
          = getCompoundControlStmtOfBBlock(lFromBBlock);
        if (lCompoundControlStmtOfEdgeStart != null)
          lParentFrom = lCompoundControlStmtOfEdgeStart;
        else
          lParentFrom = containingControlStmt(lFromHir);
          // lParentFrom = containingControlStmtInBBlock(lFromHir, lFromBBlock);
        // lParentFrom: ancestor of lFromHir or compound control statement in
        // start BBlock.
        lParentTo = null;  // Ancestor of the critical edge target block.
        for (lIterator = lSuccList.listIterator();
             lIterator.hasNext(); ) {
          lSuccBBlock = (BBlock)lIterator.next();
          lPredList = lSuccBBlock.getPredList();
          if (lPredList.size() >= 2) { // Critical edge was found.
            lDetected = true;
            List lEdge = new LinkedList(); // from-block, to-block pair
                                           //  showing critical edge.
            lEdge.add(lFromBBlock);
            lEdge.add(lSuccBBlock);
            lEdgeList.add(lEdge); // Critical edge.
            lToHir = (HIR)lSuccBBlock.getIrLink(); // lToHir: 1st Stmt of target block.
            if (lToHir instanceof LabeledStmt) {
              Label lLabel1 = ((LabeledStmt)lToHir).getLabel();
             //##62  lLabelList.add(lLabel1);
              //##62 fCriticalEdgeTarget.add(lToHir);
              IrList lLabelDefList = ((LabeledStmt)lToHir).getLabelDefList();
              for (Iterator lLabelDefIterator = lLabelDefList.iterator();
                   lLabelDefIterator.hasNext(); ) {
                LabelDef lLabelDef = (LabelDef)lLabelDefIterator.next();
                if (! fCriticalEdgeTargetLabels.contains(lLabelDef.getLabel()))
                  fCriticalEdgeTargetLabels.add(lLabelDef.getLabel());
              }
            }
            lParentTo = containingControlStmt(lToHir);
            // lParentFrom: Ancestor of the critical edge target block.
            lContainingStmt = containingStmt(lParentFrom, lParentTo);
           if (fDbgLevel > 2)
             dbg(3, "\n critical edge", "B" + lFromBBlock.getBBlockNumber()
                + " - B" + lSuccBBlock.getBBlockNumber()
                + " " + lFromHir.toStringShort() + " - " + lToHir.toStringShort()
                + " parentFrom " + ioRoot.toStringObjectShort(lParentFrom)
                + " parentTo " + ioRoot.toStringObjectShort(lParentTo));
          }
        }
        if (lParentTo != null) { // lBBlock has critical edge.
          if (fDbgLevel > 2)
            dbg(3, " containingStmt", ioRoot.toStringObject(lContainingStmt) +
              " parentFrom " + ioRoot.toStringObjectShort(lParentFrom) +
              " parentTo " + ioRoot.toStringObjectShort(lParentTo));
          if (lContainingStmt == null)
            lContainingStmt = (Stmt)lParentTo;
          if (lContainingStmt == null)
            lContainingStmt = fSubpDef.getHirBody();
          //##62 BEGIN
          if ((lContainingStmt instanceof IfStmt)||
              (lContainingStmt instanceof LoopStmt)||
              (lContainingStmt instanceof SwitchStmt)) {
            fStmtWithCriticalEdge.add(lContainingStmt);
          }
          //##62 END
        }
      }
    } // End of BBlock loop
    if (lDetected) {
      eliminateCriticalEdge();
    }
    return lDetected;
  } // processCriticalEdge

  /**
   * If pHir1 contains pHir2 then return pHir1;
   * else if pHir2 contains pHir1 then return pHir2;
   * else return null;
   * @param pHir1 statement.
   * @param pHir2 statement.
   * @return containing statement.
   */
  protected Stmt
    containingStmt(Stmt pHir1, Stmt pHir2)
  {
    HIR lParent, lContainingStmt = null;
    if (pHir1 == pHir2)
      lContainingStmt = pHir1;
    else if (pHir1 == null)
      lContainingStmt = pHir2;
    if (lContainingStmt == null) {
      lParent = pHir1;
      while ((lParent != null) && (lParent != pHir2))
        lParent = (HIR)lParent.getParent();
      if (lParent == pHir2)
        lContainingStmt = pHir2;
      if (lContainingStmt == null) {
        lParent = pHir2;
        while ((lParent != null) && (lParent != pHir1))
          lParent = (HIR)lParent.getParent();
        if (lParent == pHir1)
          lContainingStmt = pHir1;
      }
    }
    if (fDbgLevel > 3)
      ioRoot.dbgOpt1.print(5, "containingStmt of", ioRoot.toStringObjectShort(pHir1)
       + " and " + ioRoot.toStringObjectShort(pHir2) + " = " +
       ioRoot.toStringObjectShort(lContainingStmt));
    return (Stmt)lContainingStmt;
  } // containingStmt;

  /**
   * Get control statement that either contains pHir or
   * compound control statement in pFromBBlock.
   * @param pHir
   * @param pFromBBlock starting BBlock of critical edge.
   * @return the control statement
   */
  protected Stmt
    containingControlStmtInBBlock(HIR pHir, BBlock pFromBBlock)
  {
    Stmt lContainingStmt = containingControlStmt(pHir);
    if (lContainingStmt == null) {
      for (BBlockSubtreeIterator lIterator = pFromBBlock.bblockSubtreeIterator();
           lIterator.hasNext(); ) {
        HIR lStmt = (HIR)lIterator.next(); //##62
        if (isCompoundControlStmt(lStmt))
            lContainingStmt = (Stmt)lStmt;
      }
    }
    if (fDbgLevel > 3)
      ioRoot.dbgOpt1.print(5, "containingControlStmtInBBlock B" +
        pFromBBlock.getBBlockNumber(), pHir.toStringShort()
       + " = " + ioRoot.toStringObjectShort(lContainingStmt));
   return lContainingStmt;
  } // containingControlStmtInBBlock

  /**
   * Get the control statement containing pHir.
   * If there is no control statement containing pHir,
   * then return null.
   * @param pHir
   * @return the control statement containing pHir.
   */
  protected Stmt
    containingControlStmt(HIR pHir)
  {
    HIR lParent = pHir;
    while ((lParent != null) &&
           (!(lParent instanceof LoopStmt)) &&
           (!(lParent instanceof SwitchStmt)) &&
           (!(lParent instanceof IfStmt))) {
      lParent = (HIR)lParent.getParent();
    }
    if (fDbgLevel > 3)
      ioRoot.dbgOpt1.print(5, "containingControlStmt of", pHir.toStringShort()
       + " = " + ioRoot.toStringObjectShort(lParent));
    return (Stmt)lParent;
  } // containingControlStmt

  /**
   * Get the compound control statement in pBBlock.
   * If not found, return null.
   * The conpound control statement is found, then it is the last statement
   * in pBBlock.
   * @param pBBlock within which compound control statement is to be found.
   * @return the compound control statement or null.
   */
  protected Stmt
    getCompoundControlStmtOfBBlock( BBlock pBBlock )
  {
    if (fDbgLevel > 3)
     ioRoot.dbgOpt1.print(4, "getCompoundControlStmtOfBBlock",
        "B" + pBBlock.getBBlockNumber());
    Stmt lControlStmt = null;
    for (BBlockNodeIterator lIterator = pBBlock.bblockNodeIterator();
         lIterator.hasNext(); ) {
      HIR lHir = (HIR)lIterator.next();
      if (lHir instanceof Stmt) {
        if (fDbgLevel > 3)
          ioRoot.dbgOpt1.print(5, " nextStmt "
            + ioRoot.toStringObjectShort(lHir));
        if (isCompoundControlStmt((Stmt)lHir)) {
          lControlStmt = (Stmt)lHir;
          break;
        }
      }
    }
    if (fDbgLevel > 3)
      ioRoot.dbgOpt1.print(4, " result ",
        ioRoot.toStringObjectShort(lControlStmt));
    return lControlStmt;
  } // getCompoundControlStmtOfBBlock

  protected boolean
    isCompoundControlStmt(HIR pHir)
  {
    if (pHir != null) {
      switch (pHir.getOperator()) {
        case HIR.OP_IF:
        case HIR.OP_SWITCH:
        case HIR.OP_FOR:
        case HIR.OP_WHILE:
        case HIR.OP_REPEAT:
          return true;
      }
    }
    return false;
  } // isCompoundControlStmt

  public static boolean
    isEmptyStmt( Stmt pStmt )
  {
    if ((pStmt == null)||
        (pStmt.getOperator() == HIR.OP_NULL))
      return true;
    if (pStmt instanceof LabeledStmt)
      return isEmptyStmt(((LabeledStmt)pStmt).getStmt());
    if (pStmt instanceof BlockStmt) {
        Stmt lFirstStmt = ((BlockStmt)pStmt).getFirstStmt();
        if (lFirstStmt == null)
          return true;
        if (isEmptyStmt(lFirstStmt)&&
            (lFirstStmt.getNextStmt() == null))
          return true;
    }
    return false;
  } // isEmptyStmt

  protected void
    eliminateCriticalEdge()
  {
    if (fDbgLevel > 1) {
      dbg(2, "\n eliminateCriticalEdge of", fSubpDef.getSubpSym().toStringShort()
          + " " + ioRoot.getSourceFile().getName());
      dbg(3, " StmtWithCriticalEdge", fStmtWithCriticalEdge.toString());
      //##62 dbg(3, " CriticalEdgeTarget", fCriticalEdgeTarget.toString());
      dbg(3, " CriticalEdgeTargetLabels", fCriticalEdgeTargetLabels.toString());
      //##62 dbg(3, " JumpMap", fJumpMap.toString());
    }
    // Make jump list and reset work filed of Stmt.
    List lJumpList = new LinkedList(); // list of jump statements in thte current subprogram.
    for (HirIterator lIterator3 = hir.hirIterator(fSubpDef.getHirBody());
         lIterator3.hasNextStmt(); ) { //##60
      Stmt lStmt3 = lIterator3.nextStmt();  //##60
      if (lStmt3 != null)
        lStmt3.setWork(null); // Reset work.
      if (lStmt3 instanceof JumpStmt)
        lJumpList.add(lStmt3);
    }
    if (fDbgLevel > 2) //##67
      dbg(3, " JumpList", lJumpList.toString());
    // For statements in fStmtWithCriticalEdge,
    // set its index to work field as a string.
    for (int lIndex = 0; lIndex < fStmtWithCriticalEdge.size();
         lIndex++) {
      Stmt lStmt = (Stmt)fStmtWithCriticalEdge.get(lIndex);
      lStmt.setWork(Integer.toString(lIndex));
      if (fDbgLevel > 2) //##67
        dbg(3, " setWork " + lIndex + " to " + lStmt.toStringShort());
    }

    // Process jump statements that cause critical edge.
    JumpStmt lJumpStmt3;
    Label lLabel3;
    LabeledStmt lLabeledStmt3;
    for (Iterator lIterator1 = lJumpList.iterator();
         lIterator1.hasNext(); ) {
      lJumpStmt3 = (JumpStmt)lIterator1.next();
      lLabel3 = lJumpStmt3.getLabel();
      lLabeledStmt3 = lLabel3.getHirPosition(); //##62
      if (labelListContainsLabelOfStmt(
            fCriticalEdgeTargetLabels, lLabeledStmt3)) {
        separateJumpTarget(lJumpStmt3, lLabeledStmt3);
      }
    }
    for (fCriticalStmtIndex = 0; fCriticalStmtIndex < fStmtWithCriticalEdge.size();
         fCriticalStmtIndex++) {
      Stmt lStmt = (Stmt)fStmtWithCriticalEdge.get(fCriticalStmtIndex);
      eliminateCriticalEdgeOfCompoundStmt(lStmt);
    }
    fTransformed = true;
    fSubpFlow.resetComputedFlag(fSubpFlow.CF_CFG); //##62
    flow.setFlowAnalStateLevel(flow.STATE_CFG_RESTRUCTURING); //##60
  } // eliminateCriticalEdge

  protected void
    eliminateCriticalEdgeOfCompoundStmt( Stmt pStmt )
  {
    if (fDbgLevel > 2) //##67
      dbg(3, "\n eliminateCriticalEdgeOfCompoundStmt", pStmt.toStringShort());
    if (pStmt instanceof IfStmt) {
      processIfStmt((IfStmt)pStmt);
    }
    else if (pStmt instanceof LoopStmt) {
      processLoopStmt((LoopStmt)pStmt);
    }
    else if (pStmt instanceof SwitchStmt) {
      processSwitchStmt((SwitchStmt)pStmt);
    }else if (pStmt instanceof BlockStmt) {
      for (Stmt lStmt = ((BlockStmt)pStmt).getFirstStmt();
           lStmt != null; lStmt = lStmt.getNextStmt()) {
        if (fDbgLevel > 2) //##67
          dbg(3, " getWork " +ioRoot.toStringObject(lStmt.getWork()) +
            " of " + lStmt.toStringShort());
        if (lStmt.getWork() != null) {
          eliminateCriticalEdgeOfCompoundStmt(lStmt);
        }
      }
    }
    if (ioRoot.dbgOpt1.getLevel() >= 5) {
      dbg(2, "\n Transformed Stmt ");
      pStmt.print(2, false);
    }
  } // eliminateCriticalEdgeOfCompoundStmt

  protected void
    separateJumpTarget(JumpStmt pJumpStmt, LabeledStmt pLabeledStmt)
  {
    Label lLabel = pJumpStmt.getLabel();
    Label lNewLabel = symRoot.symTableCurrentSubp.generateLabel();
    int lLabelKind = lLabel.getLabelKind();
    if (fDbgLevel > 2)
      dbg(3, "separateJumpTarget of",
        pJumpStmt.toStringShort() + " " + lLabel.getName()
        + " kind " + lLabelKind + " newLabel " + lNewLabel.getName() +
        " target " + pLabeledStmt.toStringShort());
    Stmt lStmtBody = pLabeledStmt.getStmt();
    // Stmt lPrevStmt = pLabeledStmt.getPreviousStmt();
    // Stmt lNextStmt = pLabeledStmt.getNextStmt();
    BlockStmt lLabeledBlock;
    if (lStmtBody == null) {
      lLabeledBlock = hir.blockStmt(null);
      pLabeledStmt.setStmt(lLabeledBlock);
    }
    else if (lStmtBody instanceof BlockStmt) {
      lLabeledBlock = (BlockStmt)lStmtBody;
    }
    else {
      Stmt lCopyOfStmtBody = (Stmt)lStmtBody.copyWithOperands();
      lLabeledBlock = hir.blockStmt(lCopyOfStmtBody);
      pLabeledStmt.setStmt(lLabeledBlock);
    }
    LabeledStmt lDummyLabeledStmt = hir.labeledStmt(lNewLabel, null);
    lLabeledBlock.addFirstStmt(lDummyLabeledStmt);
    pJumpStmt.changeJumpLabel(lNewLabel);
    adjustLinkages(pLabeledStmt, pLabeledStmt);
  } // separateJumpTarget

  /** processIfStmt
   * Remove critical edge of if-statement.
   */
  protected void
    processIfStmt(IfStmt pIfStmt)
  {
    if (fDbgLevel > 3)
      dbg(4, "processIfStmt", pIfStmt.toStringShort());
    LabeledStmt lThenPart = pIfStmt.getThenPart();
    LabeledStmt lElsePart = pIfStmt.getElsePart();
    if (lElsePart == null) {
      Label lLabel = flowRoot.symRoot.symTableCurrentSubp.generateLabel();
      LabeledStmt lStmt = hir.labeledStmt(lLabel, null);
      pIfStmt.replaceElsePart(lStmt);
      lElsePart = pIfStmt.getElsePart();
      adjustLinkages(null, lElsePart); //##62
      if (fDbgLevel > 2)
        dbg(3, " Else-part added " + pIfStmt.toString() + "\n");
    }
    if (lThenPart == null) {
      Label lLabel = flowRoot.symRoot.symTableCurrentSubp.generateLabel();
      LabeledStmt lStmt = hir.labeledStmt(lLabel, null);
      pIfStmt.replaceThenPart(lStmt);
      lThenPart = pIfStmt.getThenPart();
      adjustLinkages(null, lThenPart); //##62
      if (fDbgLevel > 2)
        dbg(3, " Then-part added " + pIfStmt.toString() + "\n");
    }
  } // processIfStmt

  /** processLoopStmt
   * removes critical edges from the given loop statement.
   * @param pLoopStmt loop statement.
   */
  protected void
    processLoopStmt(LoopStmt pLoopStmt)
  {
    if (fDbgLevel > 3)
      dbg(4, "processLoopStmt", pLoopStmt.toStringShort());
    //##62 BEGIN
    Stmt lLoopInitpart = pLoopStmt.getLoopInitPart();
    if ((pLoopStmt.getLoopStartCondition() == null)&&
        (pLoopStmt.getLoopEndCondition() != null)) {
      // Insert (jump to LoopBodyLabel) at the tail of loopInitPart.
      JumpStmt lJumpStmt = hir.jumpStmt(pLoopStmt.getLoopBodyLabel());
      pLoopStmt.addToLoopInitPart(lJumpStmt);
      Stmt lNewLoopInitPart = pLoopStmt.getLoopInitPart();
      adjustLinkages(lLoopInitpart, lNewLoopInitPart); //##62
    }
    //##62 END
  } // processLoopStmt

  /** processSwitchStmt
   * removes critical edges from the given switch statement.
   * @param pSwitchStmt switch statement.
   */
  protected void
    processSwitchStmt(SwitchStmt pSwitchStmt)
  {
    Label lCaseLabel;
    LabeledStmt lCaseStmt;
    //## lResultantLabeledStmt;
    //## Stmt lResultantCaseStmt;
    Stmt lPreviousStmt, lNextStmt;
    if (fDbgLevel > 1)
      dbg(2, "\nprocessSwitchStmt", pSwitchStmt.toStringShort());
    int lCaseCount = pSwitchStmt.getCaseCount();
    // Make the list of case labels and default label.
    List lCaseLabelList = new ArrayList();
    for (int i = 0; i < lCaseCount; i++)
      lCaseLabelList.add(pSwitchStmt.getCaseLabel(i));
    lCaseLabelList.add(pSwitchStmt.getDefaultLabel());
    if (fDbgLevel > 2)
      dbg(3, "case label list", lCaseLabelList.toString());
    // Set lProcessedCaseLabels = new HashSet();
    // lProcessedCaseLabels holds the labels of case statements already
    // processed to skip processing for them.
    // Traverse case statements (which are LabeledStmt) in descending order..
    for (int lCaseIndex = lCaseCount; lCaseIndex >= 0;
         lCaseIndex--) {
      if (lCaseIndex < lCaseCount) { // Ordinary case statement.
        lCaseLabel = pSwitchStmt.getCaseLabel(lCaseIndex);
      }else {  // Default statement.
        lCaseLabel = pSwitchStmt.getDefaultLabel();
      }
      lCaseStmt = lCaseLabel.getHirPosition();
      if (lCaseStmt == null) {// This case statement has been deleted.
        if (fDbgLevel > 1) //##67
          dbg(2, "\ncaseStmt is null for index " + lCaseIndex +
            " " + lCaseLabel.getName() + " May be ERROR\n" );
        adjustLinkages(pSwitchStmt, pSwitchStmt); //##62
        lCaseStmt = lCaseLabel.getHirPosition();
      }
      // Case statement may be changed in processSwitchStmt,
      // but if changed, setHirPosition is executed for its case
      // label so that getHirPosition returns correct position.
      // lCaseStmt is either ordinary case statement or default statement.
      // The default statement may be combined with switch-end LabeledStmt.
      boolean lCriticalEdgeTarget = labelListContainsLabelOfStmt(
        fCriticalEdgeTargetLabels, lCaseStmt);
      if (fDbgLevel > 2)
        dbg(3, "\ncaseStmt " + lCaseIndex, ioRoot.toStringObjectShort(lCaseStmt)
          + " " + lCaseLabel.getName() + " criticalEdgeTarget " + lCriticalEdgeTarget);
      Stmt lStmtBody = lCaseStmt.getStmt();
      if (fDbgLevel > 2)
        dbg(3, "stmtBody", ioRoot.toStringObjectShort(lStmtBody));
      if (lCriticalEdgeTarget) {
        // This case statement is a critical edge target.
        // Insert jump statement to the end of previous statement
        // which may be a case statement.
        if (fDbgLevel > 2)
          dbg(3,  " lCaseStmt is critical edge target "
            + lCaseStmt.toStringShort());
        // This is the target of some critical edge.
        // Add LabeledStmt and add jump statement going to it.
        lPreviousStmt = getPreviousStmtOfCaseStmt(lCaseStmt,
            pSwitchStmt, lCaseIndex );
        Stmt lNewPreviousStmt;
        if (lPreviousStmt != null) {
          if (fDbgLevel > 2)
            dbg(3, " Add jump to lPreviousStmt ",
                lPreviousStmt.toStringShort() + " jump " + lCaseLabel.getName());
          JumpStmt lJumpStmt = hir.jumpStmt(lCaseLabel);
          Stmt lCopiedStmt = (Stmt)lPreviousStmt.copyWithOperands();
          lNewPreviousStmt = lCopiedStmt.combineStmt(lJumpStmt, true);
          if (fDbgLevel > 2)
            dbg(3, " combined lNewStmt ", lNewPreviousStmt.toStringShort());
          lPreviousStmt.replaceThisStmtWith(lNewPreviousStmt);
          adjustLinkages(lPreviousStmt, lNewPreviousStmt); //##62
          //##62 adjustHirPositionOfLabels(lCaseStmt.getPreviousStmt()); //##62
          separateJumpTarget(lJumpStmt, lCaseStmt);
        }else { // lPreviousStmt == null
            // This case statement has no provious statement in this
            // switch statement.
            // There must be some jump statement that jumps to this
            // statement, but such jump statement has already been
            // processed by separateJumpTarget before
            // processSwitchStmt is executed in eliminateCriticalEdge().
        } // End of lPreviousStmt == null
      }
    }
  } // processSwitchStmt

/* //##62
  protected Stmt
    getNextStmtWithinSwitchStmt( Stmt pStmt, SwitchStmt pSwitchStmt )
  {
    if (fDbgLevel > 0)
      dbg(4, " getNextStmtWithinSwitchStmt", pStmt.toStringShort());
    Stmt lNextStmt = pStmt.getNextStmt();
    if (lNextStmt == null) {
      HIR lParent = (HIR)pStmt.getParent();
      if (lParent.getNextStmt() != null)
        lNextStmt = lParent.getNextStmt();
      else if (lParent instanceof BlockStmt) {
        if (lParent.getNextStmt() != null)
          lNextStmt = lParent.getNextStmt();
        else {
          if (lParent == pSwitchStmt.getBodyStmt()) {
            lNextStmt = pSwitchStmt.getSwitchEndNode();
            if (fDbgLevel > 0)
              dbg(3, " switchEndNode " + flowRoot.ioRoot.toStringObjectShort(lNextStmt));
          }
        }
      }
    }
    return lNextStmt;
  } // getNextStmtWithinSwitchStmt
*/ //##62

protected Stmt
  getPreviousStmtOfCaseStmt( Stmt pCaseStmt, SwitchStmt pSwitchStmt,
                             int pCaseIndex )
{
    if (fDbgLevel > 3)
      dbg(4, "\n getPreviousStmtOfCaseStmt " + pCaseStmt.toStringShort()
        + " index " + pCaseIndex);
    Stmt lPreviousStmt = pCaseStmt.getPreviousStmt();
    if (lPreviousStmt == null) {
      if (pCaseIndex == pSwitchStmt.getCaseCount()) {
        lPreviousStmt = pSwitchStmt.getBodyStmt();
      }else {
        Stmt lGivenStmt = pCaseStmt;
        while ((lGivenStmt.getPreviousStmt() == null)&&
               (lGivenStmt.getParent()instanceof BlockStmt)) {
          lGivenStmt = (Stmt)lGivenStmt.getParent();
        }
        if (lGivenStmt != null)
          lPreviousStmt = lGivenStmt.getPreviousStmt();
      }
    }
    while (lPreviousStmt instanceof BlockStmt) {
      lPreviousStmt = ((BlockStmt)lPreviousStmt).getLastStmt();
    }
    if (fDbgLevel > 3)
      dbg(4, " result " + ioRoot.toStringObjectShort(lPreviousStmt));
    return lPreviousStmt;
} // getPreviousStmtOfCaseStmt

  protected boolean
    labelListContainsLabelOfStmt( List pLabelList, LabeledStmt pLabeledStmt)
  {
    IrList lLabelDefList = pLabeledStmt.getLabelDefList();
    for (Iterator lIterator = lLabelDefList.iterator();
         lIterator.hasNext(); ) {
      LabelDef lLabelDef = (LabelDef)lIterator.next();
      Label lLabel = lLabelDef.getLabel();
      if (pLabelList.contains(lLabel))
        return true;
    }
    return false;
  } // labelListContainsLabelOfStmt

  protected void
    mergeLabelsOfStmt1ToStmt2( LabeledStmt pStmt1, LabeledStmt pStmt2 )
  {
    if (fDbgLevel > 2)
      dbg(3, "mergeLabelsOfStmt1ToStmt2",
        pStmt1.toStringShort() + " to " +pStmt2.toStringShort());
    HirList lLabelDefList1 = (HirList)pStmt1.getLabelDefList();
    HirList lLabelDefList2 = (HirList)pStmt2.getLabelDefList();
    for (Iterator lIterator1 = lLabelDefList1.iterator();
         lIterator1.hasNext(); ) {
      LabelDef lLabelDef1 = (LabelDef)lIterator1.next();
      Label lLabel1 = lLabelDef1.getLabel();
      boolean lFound = false;
      for (Iterator lIterator2 = lLabelDefList2.iterator();
           lIterator2.hasNext();) {
        LabelDef lLabelDef2 = (LabelDef)lIterator2.next();
        if (lLabelDef2.getLabel() == lLabel1) {
          lFound = true;
          break;
        }
      }
      if (! lFound) {
        pStmt2.attachLabel(lLabel1);
        lLabel1.setHirPosition(pStmt2);
      }
    }
    if (fDbgLevel >=3) {
      dbg(3, " resultant label list ");
      for (Iterator lIterator3 = pStmt2.getLabelDefList().iterator();
           lIterator3.hasNext();) {
        LabelDef lLabelDef3 = (LabelDef)lIterator3.next();
        dbg(3, lLabelDef3.getLabel().getName() + " ");
      }
    }
  } // mergeLabelsOfStmt1ToStmt2

protected void
  adjustLinkages(Stmt pOldStmt, Stmt pNewStmt)
{
if (fDbgLevel > 2)
  dbg(3, "adjustLinkages", ioRoot.toStringObjectShort(pOldStmt)+
      " newStmt " + ioRoot.toStringObjectShort(pNewStmt));
if (pOldStmt != null) {
  int lOldStmtIndex = fStmtWithCriticalEdge.indexOf(pOldStmt);
  if (lOldStmtIndex >= 0) {
    if (lOldStmtIndex > fCriticalStmtIndex) {
      fStmtWithCriticalEdge.set(lOldStmtIndex, pNewStmt);
      if (fDbgLevel > 2)
        dbg(3, " replace " + lOldStmtIndex + " " +
            pNewStmt.toStringShort());
    }
    else {
      if (pOldStmt != pNewStmt) {
        fStmtWithCriticalEdge.add(pNewStmt);
        if (fDbgLevel > 2)
          dbg(3, " add " + pNewStmt.toStringShort());
      }
    }
  }
}
if (pNewStmt != null) {
  for (HirIterator lIterator = pNewStmt.hirIterator(pNewStmt);
       lIterator.hasNextStmt(); ) {
    Stmt lStmt = lIterator.nextStmt();
    if (fDbgLevel > 3)
      dbg(4, " " + lStmt.toStringShort()); //###
      // Adjust HirPosition for labels.
    if (lStmt instanceof LabeledStmt) {
      IrList lLabelDefList = ((LabeledStmt)lStmt).getLabelDefList();
      for (Iterator lLabelIterator = lLabelDefList.iterator();
           lLabelIterator.hasNext(); ) {
        LabelDef lLabelDef = (LabelDef)lLabelIterator.next();
        Label lLabel = lLabelDef.getLabel();
        lLabel.setHirPosition((LabeledStmt)lStmt);
        if (fDbgLevel > 3)
          dbg(4, " " + lLabel.getName() + " " + lStmt.toStringShort());
      }
    }
    // If lStmt has critical edge, then record it in fStmtWithCriticalEdge.
    if (lStmt.getWork() != null) {
      if (lStmt.getWork()instanceof String) {
        String lString = (String)lStmt.getWork();
        if (fDbgLevel > 3) //##67
          dbg(4, " work " + lString);
        int lIndex = Integer.parseInt(lString);
        if (fDbgLevel > 3) //##67
          dbg(4, " " + lIndex);
        if ((lIndex >= 0) && (lIndex < fStmtWithCriticalEdge.size())) {
          fStmtWithCriticalEdge.set(lIndex, lStmt);
          if (fDbgLevel > 3) //##67
            dbg(4, " replace by " + lStmt.toStringShort());
        }
      }
    }
  }
}
} // adjustLinkages

protected void
adjustHirPositionOfLabels1( Stmt pStmt )
  {
    if (fDbgLevel > 2)
      dbg(3, "adjustHirPositionOfLabels", ioRoot.toStringObjectShort(pStmt));
    if (pStmt == null)
      return;
    for (HirIterator lIterator = pStmt.hirIterator(pStmt);
        lIterator.hasNextStmt(); ) {
     Stmt lStmt = lIterator.nextStmt();
     if (fDbgLevel > 3)
       dbg(4, " " + lStmt.toStringShort()); //###
     if (lStmt instanceof LabeledStmt) {
       IrList lLabelDefList = ((LabeledStmt)lStmt).getLabelDefList();
       for (Iterator lLabelIterator = lLabelDefList.iterator();
            lLabelIterator.hasNext();) {
         LabelDef lLabelDef = (LabelDef)lLabelIterator.next();
         Label lLabel = lLabelDef.getLabel();
         lLabel.setHirPosition((LabeledStmt)lStmt);
         if (fDbgLevel > 3)
           dbg(4, " " + lLabel.getName()+ " "+lStmt.toStringShort());
       }
     }
   }
  } // adjustHirPositionOfLabels

  public boolean
    checkCriticalEdges(SubpFlow pSubpFlow, SubpDefinition pSubpDef)
  {
    ControlFlow lControlFlow;
    //##60 FlowResults flowResults = pSubpFlow.results();
    //##60 FlowRoot flowRoot = flowResults.flowRoot;
    flowRoot = pSubpFlow.getFlowRoot();
    IoRoot ioRoot = flowRoot.ioRoot;
    // SubpFlow subpFlow = flowRoot.subpFlow;
    if (fDbgLevel > 1)
      ioRoot.dbgOpt1.print(2, "checkCriticalEdge", pSubpDef.getSubpSym().getName());
    //##60 pSubpFlow.clear();
    //##60 flowResults.clearAll();
    //##60 ShowFlow lShowFlow = new ShowFlow(flowResults);
    //##60 pSubpFlow.controlFlowAnal();
    if (pSubpFlow.isComputed(pSubpFlow.CF_CFG)) { //##60
      // Get the instance of ControlFlow representing the
      // result of control flow analysis already done.
      lControlFlow = flow.controlFlow(); //##60
    }else { //##60
      // Do control flow analysis.
      lControlFlow = flow.controlFlowAnal(fSubpFlow); //##60
      /* //##60
      ShowControlFlow lShowControlFlow
        = new ShowControlFlow(fSubpFlow, lControlFlow); //##60
      if (ioRoot.dbgOpt1.getLevel() >= 3)
        lShowControlFlow.showAll(); //##60 REFINE
      */ //##60
    }
    // Find critical edges.
    boolean lDetected = false;
    BBlock lBBlock, lSuccBBlock;
    List lSuccList, lPredList;
    for (int i = 1; i <= pSubpFlow.getNumberOfBBlocks(); i++) {
      lBBlock = pSubpFlow.getBBlock(i);
      HIR lHir1 = (HIR)lBBlock.getIrLink();
      lSuccList = lBBlock.getSuccList();
      if (lSuccList.size() >= 2) {
        for (Iterator lIterator = lSuccList.listIterator();
             lIterator.hasNext(); ) {
          lSuccBBlock = (BBlock)lIterator.next();
          lPredList = lSuccBBlock.getPredList();
          if (lPredList.size() >= 2) { // Critical edge found.
            lDetected = true;
            HIR lHir2 = (HIR)lSuccBBlock.getIrLink();
            if (fDbgLevel > 2)
              dbg(3, " critical edge", "B" + lBBlock.getBBlockNumber()
                + " - B" + lSuccBBlock.getBBlockNumber()
                + " " + lHir1.toStringShort() + " - " + lHir2.toStringShort());
          }
        }
      }
    } // End of BBlock loop
    if (lDetected) {
      if (fDbgLevel > 0)
        ioRoot.dbgOpt1.print(2, " Critical edge found. ");
    }else {
      if (fDbgLevel > 0)
        ioRoot.dbgOpt1.print(2, " No critical edge found. ");
    }
    return lDetected;
  } // checkCriticalEdges

  public void dbg(int level, String pHeader, Object pObject)
  { //##52
    ioRoot.dbgOpt1.printObject(level, pHeader, pObject);
    ioRoot.dbgOpt1.println(level);
  }

  public void dbg(int level, Object pObject)
  { //##52
    if (pObject == null)
      ioRoot.dbgOpt1.print(level, " null ");
    else
      ioRoot.dbgOpt1.print(level, " " + pObject.toString());
  }

} // NormalizeHir
