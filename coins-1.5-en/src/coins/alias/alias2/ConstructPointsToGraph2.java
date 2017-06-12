/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * PointsToGraph.java
 *
 * Created on July 18, 2003, 2:13 PM
 */

package coins.alias.alias2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import coins.HirRoot;
import coins.IoRoot;
import coins.alias.AliasError;
import coins.alias.AliasUtil;
import coins.alias.util.Scanner;
import coins.ir.IrList;
import coins.ir.hir.AssignStmt;
import coins.ir.hir.ConstNode;
import coins.ir.hir.Exp;
import coins.ir.hir.ForStmt;
import coins.ir.hir.FunctionExp;
import coins.ir.hir.HIR;
import coins.ir.hir.HirVisitorModel2;
import coins.ir.hir.IfStmt;
import coins.ir.hir.JumpStmt;
import coins.ir.hir.LabeledStmt;
import coins.ir.hir.LoopStmt;
import coins.ir.hir.PointedExp;
import coins.ir.hir.QualifiedExp;
import coins.ir.hir.ReturnStmt;
import coins.ir.hir.Stmt;
import coins.ir.hir.SubpDefinition;
import coins.ir.hir.SubpNode;
import coins.ir.hir.SubscriptedExp;
import coins.ir.hir.SwitchStmt;
import coins.ir.hir.UntilStmt;
import coins.ir.hir.VarNode;
import coins.ir.hir.WhileStmt;
import coins.sym.Label;
import coins.sym.PointerType;
import coins.sym.Type;

/**
 * This class constructs the points-to graph, the bit matrix
 * representing the points-to relation, implemented as
 * the array of <code>TagVector2</code>s. It analyzes
 * the propagation of address values, tracing the subprogram
 * from top to bottom. There is no consideration for types;
 * not only pointer types can hold the address values.
 * @author  hasegawa
 */
class ConstructPointsToGraph2
{
  private boolean fIsOptimistic;

  private List fAssigns = new ArrayList();
  private List fExps = new ArrayList();

  Map fHIRToLoc = new HashMap(50);
  private int fTagBitCount;

  private final AliasFactory2 fFactory;
  private final AliasUtil fUtil;

  TagVector2 fPointsTo[];
  //    private final TagVector2 fExternPes;
  private final TagVector2 fExternOpt;
  private final AliasAnalHir2 fAliasAnal;

  /**
   * The <code>HirRoot</code> object shared by every module
   * in the program.
   */
  public final HirRoot hirRoot;

  /**
   * The <code>IoRoot</code> object shared by every module
   * in the program.
   */
  public final IoRoot ioRoot;

  Map fVarToLoc;
  Map fSubscriptToMask;
  Map fElemToMask;
  Map fmallocToLoc;

  TagVector2 fLocalRootVects[];

  /** Creates a new instance of ConstructPointsToGraph2.
   * Some information is passed from the <code>AliasAnalHir2</code>
   * object to this module.
   *
   * @param pAnal <code>AliasAnalHir2</code> object,
   * the object that drives the alias analysis.
   * @param pHirRoot <code>HirRoot</code> object shared
   * by every module in the program.
   */
  ConstructPointsToGraph2(SubpDefinition pSubpDef, AliasAnalHir2 pAnal)
  {
    hirRoot = pAnal.hirRoot;
    ioRoot = hirRoot.ioRoot;
    fTagBitCount = pAnal.fTagBitCount;
    fPointsTo = pAnal.fPointsTo;
    fFactory = pAnal.fFactory;
    fUtil = pAnal.fUtil;
    fAliasAnal = pAnal;

    fIsOptimistic = pAnal.fIsOptimistic;
    TagVector2 lTagVect;
    fVarToLoc = pAnal.fVarToLoc;
    fSubscriptToMask = pAnal.fSubscriptToMask;
    fElemToMask = pAnal.fElemToMask;
    fmallocToLoc = pAnal.fmallocToLoc;
    fLocalRootVects = pAnal.fLocalRootVects;

    fExternOpt = pAnal.fExternOpt;
    //        fExternPes = fExternOpt;

    new SubpVisitor(pSubpDef).process();
  }

  private class SubpVisitor
    extends HirVisitorModel2
  {
    SubpDefinition fSubpDef;

    TagVector2 fLoc = fFactory.tagVector(fTagBitCount);
    TagVector2 fPointingTo = fFactory.tagVector(fTagBitCount);

    List fLoopSwitchStack = new ArrayList();

    Map fLoopToBreakPointsTo = new HashMap();
    Map fLoopToContPointsTo = new HashMap();

    Map fSwitchToLabels = new HashMap();
    Map fSwitchToPointsToGraphAtEntry = new HashMap();
    Map fSwitchToBreakPointsTo = new HashMap();

    private SubpVisitor(SubpDefinition pSubpDef)
    {
      super(ConstructPointsToGraph2.this.hirRoot);
      fSubpDef = pSubpDef;
    }

    void process()
    {
      visit(fSubpDef);
    }

    public void atAssignStmt(AssignStmt pAssign)
    {
      TagVector2 lLHS, lRHS;
      visit(pAssign.getLeftSide());
      if (fLoc == null) {
        this.ioRoot.getCompileSpecification().getWarning().warning(1, "Alias",
          "Assignment to meaningless location/nonlvalue.");
        fLoc = fFactory.tagVector(fTagBitCount);
      }
      lLHS = ConstructPointsToGraph2.this.clone(fLoc);
      visit(pAssign.getRightSide());
      lRHS = fPointingTo;
      for (Scanner lScanner = lLHS.toBriggsSet().scanner(); lScanner.hasNext(); ) {
        int lNext = lScanner.next();
        if (lLHS.fIsDefinite)
          fPointsTo[lNext] = ConstructPointsToGraph2.this.clone(lRHS);
        else {
          fPointsTo[lNext].vectorOr(lRHS, fPointsTo[lNext]);
          fPointsTo[lNext].fAssociatedParam = null;
        }
      }
      fLoc = null;
      fPointingTo = null;
    }

    public void atIfStmt(IfStmt pIf)
    {
      TagVector2 lPointsTo[] = new TagVector2[fTagBitCount];
      TagVector2 lPointsTo0[] = new TagVector2[fTagBitCount];

      visit(pIf.getIfCondition());

      fLoc = null;
      fPointingTo = null;

      lPointsTo = ConstructPointsToGraph2.this.clone(fPointsTo);

      visit(pIf.getThenPart());

      fLoc = null;
      fPointingTo = null;

      lPointsTo0 = ConstructPointsToGraph2.this.clone(fPointsTo);
      fPointsTo = lPointsTo;

      visit(pIf.getElsePart());
      merge(fPointsTo, lPointsTo0);

      fLoc = null;
      fPointingTo = null;

    }

    public void atSwitchStmt(SwitchStmt pSwitch)
    {
      visit(pSwitch.getSelectionExp());

      fLoc = null;
      fPointingTo = null;

      TagVector2 lPointsTo[] = new TagVector2[fTagBitCount];
      lPointsTo = ConstructPointsToGraph2.this.clone(fPointsTo);
      TagVector2 lBreakPointsTo[] = new TagVector2[fTagBitCount];
      for (int i = 0; i < fTagBitCount; i++)
        lBreakPointsTo[i] = fFactory.tagVector(fTagBitCount);

      Set lLabels = new HashSet();
      for (int i = 0; i < pSwitch.getCaseCount(); i++) {
        lLabels.add(pSwitch.getCaseLabel(i));
      }
      if (pSwitch.getDefaultLabel() != null)
        lLabels.add(pSwitch.getDefaultLabel());

      fSwitchToLabels.put(pSwitch, lLabels);
      fSwitchToPointsToGraphAtEntry.put(pSwitch, lPointsTo);
      fSwitchToBreakPointsTo.put(pSwitch, lBreakPointsTo);

      fLoopSwitchStack.add(pSwitch);

      visit(pSwitch.getBodyStmt());

      if (pSwitch.getDefaultLabel() == null)
        merge(fPointsTo, lPointsTo);
      merge(fPointsTo, lBreakPointsTo);
      fLoc = null;
      fPointingTo = null;
      fLoopSwitchStack.remove(pSwitch);
    }

    public void atForStmt(ForStmt pFor)
    {
      processLoop(pFor, true);
    }

    public void atWhileStmt(WhileStmt pWhile)
    {
      processLoop(pWhile, true);
    }

    public void atUntilStmt(UntilStmt pUntil)
    {
      processLoop(pUntil, false);
    }

    private void processLoop(LoopStmt pLoop, boolean pIsForWhile)
    {

      TagVector2 lPointsTo[] = null; //new TagVector2[fTagBitCount];
      TagVector2 lCont[] = new TagVector2[fTagBitCount];
      TagVector2 lBreak[]; // = new TagVector2[fTagBitCount];
      for (int i = 0; i < fTagBitCount; i++)
        lCont[i] = fFactory.tagVector(fTagBitCount);
      lBreak = ConstructPointsToGraph2.this.clone(lCont);
      //            System.arraycopy(lCont, 0, lBreak, 0, fTagBitCount);
      fLoopToContPointsTo.put(pLoop, lCont);
      fLoopToBreakPointsTo.put(pLoop, lBreak);
      fLoopSwitchStack.add(pLoop);
      visit(pLoop.getLoopInitPart());
      fLoc = null;
      fPointingTo = null;
      visit(pLoop.getLoopStartCondition());
//            System.out.println("fPointsTo " + Arrays.asList(fPointsTo));
      fLoc = null;
      fPointingTo = null;
      TagVector2 lPointsToUpToPrev[] = null;

      if (pIsForWhile) {
        lPointsTo = ConstructPointsToGraph2.this.clone(fPointsTo);
        lPointsToUpToPrev = ConstructPointsToGraph2.this.clone(lPointsTo);
      }
      visit(pLoop.getLoopBodyPart());
      fLoc = null;
      fPointingTo = null;
      visit(pLoop.getLoopEndCondition());
      fLoc = null;
      fPointingTo = null;
      if (!pIsForWhile) {
        lPointsTo = ConstructPointsToGraph2.this.clone(fPointsTo);
        lPointsToUpToPrev = ConstructPointsToGraph2.this.clone(lPointsTo);
      }
      while (true) {
        fLoc = null;
        fPointingTo = null;
        merge(fPointsTo, (TagVector2[])fLoopToContPointsTo.get(pLoop));
        visit(pLoop.getLoopStepPart());
        fLoc = null;
        fPointingTo = null;
        visit(pLoop.getLoopStartCondition());
        fLoc = null;
        fPointingTo = null;
        if (pIsForWhile) {
          merge(lPointsTo, fPointsTo);
          if (Arrays.equals(lPointsTo, lPointsToUpToPrev))
            break;
          lPointsToUpToPrev = ConstructPointsToGraph2.this.clone(lPointsTo);
        }
        visit(pLoop.getLoopBodyPart());
        fLoc = null;
        fPointingTo = null;
        visit(pLoop.getLoopEndCondition());
        fLoc = null;
        fPointingTo = null;
        if (!pIsForWhile) {
          merge(lPointsTo, fPointsTo);
          if (Arrays.equals(lPointsTo, lPointsToUpToPrev))
            break;
          lPointsToUpToPrev = ConstructPointsToGraph2.this.clone(lPointsTo);
        }
      }
//            System.out.println("fVarToLoc: " + fVarToLoc);
//            System.out.println("lPointsTo " + Arrays.asList(lPointsTo));
      fPointsTo = lPointsTo;
      merge(fPointsTo, (TagVector2[])fLoopToBreakPointsTo.get(pLoop));
      fLoc = null;
      fPointingTo = null;
      fLoopSwitchStack.remove(pLoop);
    }

    //        private void processUntil(UntilStmt pUntil)
    //        {
    //        }

    public void atLabeledStmt(LabeledStmt pLabeledStmt)
    {
      Stmt lStmt;
      Set lLabels;

      for (ListIterator lListIt = fLoopSwitchStack.listIterator(
        fLoopSwitchStack.size()); lListIt.hasPrevious(); ) {
        lStmt = (Stmt)lListIt.previous();
        if (lStmt.getOperator() == HIR.OP_SWITCH) {
          lLabels = (Set)fSwitchToLabels.get(lStmt);
          if (lLabels.contains(pLabeledStmt.getLabel()))
            merge(fPointsTo,
                  (TagVector2[])fSwitchToPointsToGraphAtEntry.get(lStmt));
          break;
        }
      }
      visitChildren(pLabeledStmt);
      fLoc = null;
      fPointingTo = null;
    }

    public void atJumpStmt(JumpStmt pJump)
    {
      Label lLabel = pJump.getLabel();
      int lLabelKind = lLabel.getLabelKind();
      TagVector2 lPointsTo[];

      Stmt lStmt;

      OutMost:
        for (ListIterator lListIt = fLoopSwitchStack.listIterator(
        fLoopSwitchStack.size()); lListIt.hasPrevious(); ) {
        lStmt = (Stmt)lListIt.previous();
        switch (lStmt.getOperator()) {
          case HIR.OP_FOR:
          case HIR.OP_WHILE:
          case HIR.OP_UNTIL:
            if (lLabelKind == Label.LOOP_END_LABEL &&
                ((LoopStmt)lStmt).getLoopEndLabel() == lLabel) {
              lPointsTo = ((TagVector2[])fLoopToBreakPointsTo.get(lStmt));
              merge(lPointsTo, fPointsTo);
              clear(fPointsTo);
              break OutMost;
            }
            else if (lLabelKind == Label.LOOP_STEP_LABEL &&
              ((LoopStmt)lStmt).getLoopStepLabel() == lLabel) {
              lPointsTo = (TagVector2[])fLoopToContPointsTo.get(lStmt);
              merge(lPointsTo, fPointsTo);
              clear(fPointsTo);
              break OutMost;
            }
            break;
          case HIR.OP_SWITCH:
            if (lLabelKind == Label.SWITCH_END_LABEL &&
                ((SwitchStmt)lStmt).getEndLabel() == lLabel) {
              lPointsTo = (TagVector2[])fSwitchToBreakPointsTo.get(lStmt);
              merge(lPointsTo, fPointsTo);
              clear(fPointsTo);
              break OutMost;
            }
            break;
        }
      }
      for (int i = 0; i < fTagBitCount; i++)
        if (!fPointsTo[i].isZero()) {
          System.out.println("label kind of " + pJump + " is " +
            pJump.getLabel().getLabelKind());
          throw new AliasError();
        }
      fLoc = null;
      fPointingTo = null;
    }

    public void atReturnStmt(ReturnStmt pReturn)
    {
      visitChildren(pReturn);
      clear(fPointsTo);
      fLoc = null;
      fPointingTo = null;
    }

    public void atConstNode(ConstNode pConstNode)
    {
      if (pConstNode.getType().getTypeKind() == Type.KIND_VECTOR) {
        fLoc = null;
      }
      fPointingTo = fFactory.tagVector(fTagBitCount);
    }

    public void atVarNode(VarNode pVarNode)
    {
      fLoc = (TagVector2)fVarToLoc.get(pVarNode.getVar());
      //            TagVector2 lTagVect = ConstructPointsToGraph2.this.clone(fLoc);
      fPointingTo = deref(fPointsTo, fLoc);
      fHIRToLoc.put(pVarNode, fLoc);
      fLoc = ConstructPointsToGraph2.this.clone(fLoc);
      //          fLoc = lTagVect;
    }

    public void atSubscriptedExp(SubscriptedExp pSubs)
    {
      TagVector2 lLoc, lPointingTo;
      Long lSubscript;
      TagVector2 lMask;
      List lPair;

      visit(pSubs.getArrayExp());
      if (fLoc == null) // nonlvalue
        lLoc = null;
      else
        lLoc = ConstructPointsToGraph2.this.clone(fLoc);
      lPointingTo = ConstructPointsToGraph2.this.clone(fPointingTo);
      visit(pSubs.getSubscriptExp());
      if (lLoc != null) {
        if (pSubs.getSubscriptExp().getOperator() == HIR.OP_CONST) {
          lPair = new ArrayList(2);
          lSubscript = new Long(((ConstNode)pSubs.getSubscriptExp()).
            getConstSym().longValue());
          lPair.add(lSubscript);
          lPair.add(fUtil.toBareAndSigned(pSubs.getType()));
          lMask = (TagVector2)fSubscriptToMask.get(lPair);
          if (lMask != null) // null means the array access does not correspond to a visible variable
            lLoc.vectorAnd(lMask, lLoc);
          else
            lLoc.fIsDefinite = false;
        }
        else
          lLoc.fIsDefinite = false;
        mergeWithPrev(fHIRToLoc, pSubs, lLoc);
        //                    fHIRToLoc.put(pSubs, ConstructPointsToGraph2.this.clone(lLoc));
        fPointingTo = deref(fPointsTo, lLoc);
      }
      else
        fPointingTo = lPointingTo;
      fLoc = lLoc;
      //       fHIRToLoc.remove(pSubs.getArrayExp());
    }

    public void atQualifiedExp(QualifiedExp pExp)
    {
      TagVector2 lMask;

      visit(pExp.getQualifierExp());
      if (fLoc != null) {
        if (pExp.getQualifierExp().getType().getTypeKind() == Type.KIND_STRUCT) {
          lMask = (TagVector2)fElemToMask.get(pExp.getQualifiedElem());
          if (lMask != null) { // null means the struct access does not correspond to a visible variable,
            fLoc.vectorAnd(lMask, fLoc);
          }
          mergeWithPrev(fHIRToLoc, pExp, fLoc);
          //                    fHIRToLoc.put(pExp, ConstructPointsToGraph2.this.clone(fLoc));
        }
        else {
          //    fLoc = ConstructPointsToGraph2.this.clone(fLoc);
          fLoc.fIsDefinite = false;
          mergeWithPrev(fHIRToLoc, pExp, fLoc);
          //                    fHIRToLoc.put(pExp, ConstructPointsToGraph2.this.clone(fLoc));
        }
        fPointingTo = deref(fPointsTo, fLoc);
      }
      //     fHIRToLoc.remove(pExp.getQualifierExp());
    }

    public void atPointedExp(PointedExp pPointedExp)
    {
      TagVector2 lMask;
      visit(pPointedExp.getPointerExp());
      lMask = (TagVector2)fElemToMask.get(pPointedExp.getPointedElem());
      if (lMask != null) {
        fLoc = fFactory.tagVector(fTagBitCount);
        fPointingTo.vectorAnd(lMask, fLoc);
        fLoc.fIsDefinite = fPointingTo.fIsDefinite;
      }
      else {
        fLoc = fPointingTo;
        //        fLoc = ConstructPointsToGraph2.this.clone(fLoc);
        fLoc.fIsDefinite = false;
      }
      fPointingTo = deref(fPointsTo, fLoc);
      mergeWithPrev(fHIRToLoc, pPointedExp, fLoc);
      //            fHIRToLoc.put(pPointedExp, ConstructPointsToGraph2.this.clone(fLoc));
    }

    public void atExp(Exp pExp)
    {
      switch (pExp.getOperator()) {
        case HIR.OP_CONTENTS:
          visit(pExp.getExp1());
          fLoc = fPointingTo;
          fPointingTo = deref(fPointsTo, fLoc);
          mergeWithPrev(fHIRToLoc, pExp, fLoc);
          //                    fHIRToLoc.put(pExp, ConstructPointsToGraph2.this.clone(fLoc));
          break;
        case HIR.OP_UNDECAY:
          visit(pExp.getExp1());
          fLoc = promote(fPointingTo);
          fPointingTo = deref(fPointsTo, fLoc);
          mergeWithPrev(fHIRToLoc, pExp, fLoc);
          //                    fHIRToLoc.put(pExp, ConstructPointsToGraph2.this.clone(fLoc));
          break;
        case HIR.OP_ADDR:
          visit(pExp.getExp1());
          if (fLoc == null)
            fPointingTo = fFactory.tagVector(fTagBitCount);
          else
            fPointingTo = fLoc;
            //                    fPointingTo = ConstructPointsToGraph2clone(fLoc);
          fLoc = null;
          break;
        case HIR.OP_DECAY:
          visit(pExp.getExp1());
          fPointingTo = fLoc == null ? fFactory.tagVector(fTagBitCount) : fLoc;
          List lPair = new ArrayList(2);
          lPair.add(new Long(0L));
          lPair.add(fUtil.toBareAndSigned(((PointerType)pExp.getType()).
            getPointedType()));
          TagVector2 lMask = (TagVector2)fSubscriptToMask.get(lPair);
          if (lMask != null)
            fPointingTo.vectorAnd(lMask, fPointingTo);
          else
            fPointingTo.fIsDefinite = false;
          fLoc = null;
          //                    fPointingTo.fIsDefinite = false;
          break;
        case HIR.OP_CONV:
          visit(pExp.getExp1());
          fPointingTo = promote(fPointingTo);
          fLoc = null;
          break;
        case HIR.OP_MULT:
        case HIR.OP_DIV:
        case HIR.OP_MOD:
        case HIR.OP_AND:
        case HIR.OP_OR:
        case HIR.OP_XOR:
        case HIR.OP_SHIFT_LL:
        case HIR.OP_SHIFT_R:
        case HIR.OP_SHIFT_RL:
        case HIR.OP_NOT:
        case HIR.OP_NEG:
          visit(pExp.getExp1());
          TagVector2 lTemp = ConstructPointsToGraph2.this.clone(fPointingTo);
          visit(pExp.getExp2());
          //                    fPointingTo = ConstructPointsToGraph2.this.clone(fPointingTo);
          fPointingTo.vectorOr(lTemp, fPointingTo);
          fPointingTo.fIsDefinite = false;
          fLoc = null;
          break;

        case HIR.OP_ADD:
        case HIR.OP_SUB:
          visit(pExp.getExp1());
          TagVector2 lTemp0 = promote(fPointingTo);
          visit(pExp.getExp2());
          fPointingTo = promote(fPointingTo);
          merge(fPointingTo, lTemp0);
          //                    fPointingTo.vectorOr(lTemp0, fPointingTo);
          fLoc = null;
          break;
        case HIR.OP_CMP_EQ:
        case HIR.OP_CMP_NE:
        case HIR.OP_CMP_LT:
        case HIR.OP_CMP_LE:
        case HIR.OP_CMP_GT:
        case HIR.OP_CMP_GE:
          visit(pExp.getExp1());
          visit(pExp.getExp2());
          //                    fPointingTo = ConstructPointsToGraph2.this.clone(fPointingTo);
          fPointingTo.vectorReset();
          fPointingTo.fIsDefinite = false;
          fLoc = null;
          break;
        default:
          throw new AliasError("Unexpected.");
      }
    }

    public void atFunctionExp(FunctionExp pExp)
    {
      IrList lActuals = pExp.getActualParamList();
      boolean lIsmalloc = false;
      boolean lIsPredefined = false;
      Exp lFuncSpec, lSubpNode;
      TagVector2 lAddressExternallyVisibles = fFactory.tagVector(fTagBitCount);

      if (fmallocToLoc.containsKey(pExp))
        lIsmalloc = true;
      else if ((lFuncSpec = pExp.getSubpSpec()).getOperator() == HIR.OP_ADDR &&
               (lSubpNode = lFuncSpec.getExp1()).getOperator() == HIR.OP_SUBP &&
               fUtil.isPredefined(((SubpNode)lSubpNode).getSubp(),
        fAliasAnal.fPredefined))
        lIsPredefined = true;
      else
        visit(pExp.getFunctionSpec());
      fLoc = null;
      fPointingTo = null;

      for (Iterator lIt = lActuals.iterator(); lIt.hasNext(); ) {
        visit((Exp)lIt.next());
        lAddressExternallyVisibles.vectorOr(fPointingTo,
          lAddressExternallyVisibles);
      }
      if (lIsmalloc)
        fPointingTo = ConstructPointsToGraph2.this.clone((TagVector2)
          fmallocToLoc.get(pExp));
      else if (lIsPredefined)
        fPointingTo = fFactory.tagVector(fTagBitCount);
      else {
        // if (fIsOptimistic)
        lAddressExternallyVisibles.vectorOr(fExternOpt,
          lAddressExternallyVisibles);
        // else
        //   lAddressExternallyVisibles.vectorOr(fExternPes,
        //     lAddressExternallyVisibles);
        TagVector2 lTemp = fFactory.tagVector(fTagBitCount);
        do {
          lAddressExternallyVisibles.vectorCopy(lTemp);
          for (Scanner lScanner = lAddressExternallyVisibles.toBriggsSet().
               scanner(); lScanner.hasNext(); ) {
            int lNext = lScanner.next();
            lAddressExternallyVisibles.vectorOr(fPointsTo[lNext],
              lAddressExternallyVisibles);
          }
        }
        while (!lAddressExternallyVisibles.vectorEqual(lTemp));

        if (ioRoot.dbgAlias.getLevel() > 3) //##67
          fAliasAnal.dbg(6, "lAddressExternallyVisibles",
            lAddressExternallyVisibles);
        for (Scanner lScanner0 = lAddressExternallyVisibles.toBriggsSet().
             scanner(); lScanner0.hasNext(); ) {
          int lNext0 = lScanner0.next();
          fPointsTo[lNext0].vectorOr(lAddressExternallyVisibles,
            fPointsTo[lNext0]);
          fPointsTo[lNext0].fIsDefinite = false;
          fPointsTo[lNext0].fAssociatedParam = null;
          if (ioRoot.dbgAlias.getLevel() > 3) //##67
            fAliasAnal.dbg(6, "fPointsTo", Arrays.asList(fPointsTo));
        }
        fPointingTo = lAddressExternallyVisibles;
      }
      fLoc = null;
    }

  }

  private TagVector2 merge(TagVector2 pOperand1AndDest, TagVector2 pOperand2)
  {
    TagVector2 lTemp;

    if (pOperand1AndDest.isZero())
      copy(pOperand2, pOperand1AndDest);
    else if (pOperand2.isZero())
      ;
    else {
      if (pOperand1AndDest.equals(pOperand2))
        ;
      else {
        pOperand1AndDest.vectorOr(pOperand2, pOperand1AndDest);
        pOperand1AndDest.fIsDefinite = false;
        pOperand1AndDest.fAssociatedParam = null;
      }
    }
    return pOperand1AndDest;
  }

  private TagVector2[] merge(TagVector2 pOperand1AndDest[],
    TagVector2 pOperand2[])
  {
    for (int i = 0; i < fTagBitCount; i++) {
      pOperand1AndDest[i].fIsDefinite = pOperand1AndDest[i].isZero() ?
        pOperand2[i].fIsDefinite : pOperand2[i].isZero() ?
        pOperand1AndDest[i].fIsDefinite :
        pOperand1AndDest[i].fIsDefinite && pOperand2[i].fIsDefinite &&
        pOperand1AndDest[i].vectorEqual(pOperand2[i]);
      pOperand1AndDest[i].vectorOr(pOperand2[i], pOperand1AndDest[i]);
    }
    return pOperand1AndDest;
  }

  private TagVector2[] clear(TagVector2 pTagVect[])
  {
    for (int i = 0; i < fTagBitCount; i++) {
      pTagVect[i].vectorReset();
      pTagVect[i].fIsDefinite = false;
    }
    return pTagVect;
  }

  private TagVector2 deref(TagVector2 pPointsTo[], TagVector2 pLoc)
  {
    TagVector2 lTagVect = fFactory.tagVector(fTagBitCount);
    lTagVect.fIsDefinite = pLoc.fIsDefinite;
    for (Scanner lScanner = pLoc.toBriggsSet().scanner(); lScanner.hasNext(); ) {
      int lnext = lScanner.next();
      if (ioRoot.dbgAlias.getLevel() > 3) //##67
        fAliasAnal.dbg(6, "fPointsTo", Arrays.asList(pPointsTo));
      if (lTagVect.isZero())
        lTagVect.fAssociatedParam = pPointsTo[lnext].fAssociatedParam;
      else
        lTagVect.fAssociatedParam = pPointsTo[lnext].fAssociatedParam ==
          lTagVect.fAssociatedParam ? lTagVect.fAssociatedParam : null;
      lTagVect.vectorOr(pPointsTo[lnext], lTagVect);
      lTagVect.fIsDefinite &= fPointsTo[lnext].fIsDefinite;
    }
    return lTagVect;
  }

  private void copy(TagVector2 pSource, TagVector2 pDest)
  {
    pSource.vectorCopy(pDest);
    pDest.fIsDefinite = pSource.fIsDefinite;
    pDest.fAssociatedParam = pSource.fAssociatedParam;
  }

  private TagVector2 clone(TagVector2 pTagVector2)
  {
    TagVector2 lTagVect = fFactory.tagVector(fTagBitCount);
    pTagVector2.vectorCopy(lTagVect);
    lTagVect.fIsDefinite = pTagVector2.fIsDefinite;
    lTagVect.fAssociatedParam = pTagVector2.fAssociatedParam;
    return lTagVect;
  }

  void printPointsToGraph()
  {
    for (int i = 0; i < fTagBitCount; i++)
      System.out.println(fPointsTo[i]);
  }

  private TagVector2[] clone(TagVector2 pTagVector2[])
  {
    TagVector2 lTagVect[] = new TagVector2[pTagVector2.length];
    for (int i = 0; i < pTagVector2.length; i++) {
      lTagVect[i] = fFactory.tagVector(pTagVector2[i].getBitLength());
      lTagVect[i] = clone(pTagVector2[i]);
      //            pTagVector2[i].vectorCopy(lTagVect[i]);
    }
    return lTagVect;
  }

  private TagVector2 promote(TagVector2 pTagVect)
  {
    TagVector2 lTagVect = fFactory.tagVector(fTagBitCount);

    for (Scanner lScanner = pTagVect.toBriggsSet().scanner(); lScanner.hasNext(); ) {
      int lNext = lScanner.next();
      lTagVect.vectorOr(fLocalRootVects[lNext], lTagVect);
    }
    if (pTagVect.vectorEqual(lTagVect))
      lTagVect.fAssociatedParam = pTagVect.fAssociatedParam;
    return lTagVect;
  }

  private void mergeWithPrev(Map pHIRToLoc, Exp pLvalue, TagVector2 pLoc)
  {
    TagVector2 lPrev = (TagVector2)pHIRToLoc.get(pLvalue);
    if (lPrev == null)
      fHIRToLoc.put(pLvalue, ConstructPointsToGraph2.this.clone(pLoc));
    else
      fHIRToLoc.put(pLvalue, merge(lPrev, pLoc));
  }

}
