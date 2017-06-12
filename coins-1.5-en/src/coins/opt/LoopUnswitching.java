/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.opt;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import coins.HirRoot;
import coins.ir.hir.AssignStmt;
import coins.ir.hir.BlockStmt;
import coins.ir.hir.Exp;
import coins.ir.hir.ForStmt;
import coins.ir.hir.HIR;
import coins.ir.hir.HirIterator;
import coins.ir.hir.IfStmt;
import coins.ir.hir.LabeledStmt;
import coins.ir.hir.Stmt;
import coins.ir.hir.SubpDefinition;
import coins.ir.hir.SubscriptedExp;
import coins.ir.hir.VarNode;
import coins.sym.Sym;
import coins.sym.SymIterator;
import coins.sym.Type;
import coins.sym.Var;

/**
 *
 * <p>title: LoopUnswitching class.</p>
 * <p>description: If-Expand LoopStmt.</p>
 * @author S.Noishi
 * @version 1.0
 */
public class LoopUnswitching
  extends LoopExpansion
{

  /**
   *  Variables that can't be applied optimization
   */
  private Set fBadVarSet = new HashSet();

  public LoopUnswitching(HirRoot phirRoot)
  {
    super(phirRoot);
  }

  /**
   * Do Loop-If Expand optimization in subprogram.
   * @param  SubpDefinition to do Loop-If Expand optimization.
   * @return  true if optimized, false if else.
   */
  public boolean doSubprogram(SubpDefinition pSubpDef)
  {
    final String lMethodName = "pickUpVariables";
    HirIterator lHirIterator; // HirIterator for Subprogram
    Stmt lStmt; // Statement of Subprogram
    getDebug().print(2, lMethodName, "Start process");

    flowRoot.subpUnderAnalysis = pSubpDef.getSubpSym();
    symRoot.symTableCurrent = pSubpDef.getSymTable();
    symRoot.symTableCurrentSubp = symRoot.symTableCurrent;

    boolean lChanged = false;
    // main loop ==========================================================
    for (lHirIterator = hirRoot.hir.hirIterator(pSubpDef.getHirBody());
         lHirIterator.hasNextStmt(); //##62
         ) {
      lStmt = lHirIterator.getNextStmt();
      if (lStmt != null && lStmt.getOperator() == HIR.OP_FOR) {
        getDebug().print(3, lMethodName, "Loop is found " + lStmt.toStringShort());
        if (isExpansible((ForStmt)lStmt)) {
          lChanged |= removeIfStmt((ForStmt)lStmt);
        }
      }
    }

    // 2005.03.16 S.Noishi
    if (lChanged) {
      //##62  pSubpDef.setIndexNumberToAllNodes(0);
    }

    return lChanged;
  }

  /**
   * Check ForStmt may be optimized by Loop-if expansion.
   * @param pForStmt
   * @return  true  pForStmt may be optimized by Loop-if expansion.
   *     false pForStmt is not optimized by Loop-if expansion.
   */
  protected boolean isExpansible(ForStmt pForStmt)
  {
    final String lMethodName = "isExpansible";

    // Do not optimize if ForStmt conatins bad element.
    if (hasBadElement(pForStmt)) {
      getDebug().print(3, lMethodName, "Bad element is found.");
      return false;
    }

    // Do not optimize if Stmt count in Loop-body-part
    // is over of allowable value,
    // is get by getMaxAllowableStmtsInLoopBody().
    int cntStmtInBody = calcStatementCount(pForStmt.getLoopBodyPart());
    getDebug().print(
      4,
      lMethodName,
      "statement count in body is " + cntStmtInBody);
    if (cntStmtInBody > getMaxAllowableStmtsInLoopBody()) {
      getDebug().print(
        3,
        lMethodName,
        "statement count "
        + cntStmtInBody
        + " is overd for "
        + getMaxAllowableStmtsInLoopBody()
        + " do not expand");
      return false;
    }

    return true;
  }

  /**
   * Analize variables in ForStmt contains to
   * get information to doing Loop-If expanding optimization.
   * @param pForStmt ForStmt to analize.
   */
  protected void pickUpVariables(ForStmt pForStmt)
  {
    final String lMethodName = "pickUpVariables";
    HirIterator lHirIterator, lHirIterator2;
    SymIterator lSymIterator;
    HIR lnode, lnode2;
    Stmt lStmt;
    Exp lExp;
    Var lVar;
    BlockStmt lBlock;
    // block that contains body and step part for any check

    getDebug().print(3, "Start process");
    fBadVarSet.clear();

    // Generate block that contains body and step part for any check.
    //// Create HIR that is chained loop-body and loop-step-part,
    //// then generate sets to refer for optimizetion.
    lBlock = hirRoot.hir.blockStmt(null);
    lStmt =
      ((BlockStmt)((LabeledStmt)pForStmt.getLoopBodyPart())
       .getStmt()
       .copyWithOperandsChangingLabels(null))
      .getFirstStmt();
    while (lStmt != null) {
      // Fix 2005.02.18 S.Noishi
      Stmt lStmtNext = lStmt.getNextStmt();
      // Fix 2005.04.01 S.Noishi
      if ((lStmt instanceof LabeledStmt) &&
          (((LabeledStmt)lStmt).getLabel() == null)) {
        ;
      }
      else {
        lBlock.addLastStmt(lStmt);
      }
      //lStmt = lStmt.getNextStmt();
      lStmt = lStmtNext;
    }
    if (pForStmt.getLoopStepPart() != null)

      // Fix 2005.03.04
//      lStmt =
//        ((BlockStmt) pForStmt
//          .getLoopStepPart()
//          .copyWithOperandsChangingLabels(null))
//          .getFirstStmt();
      lStmt = (Stmt)pForStmt
        .getLoopStepPart()
        .copyWithOperandsChangingLabels(null);
    if (lStmt instanceof BlockStmt) {
      lStmt = ((BlockStmt)lStmt).getFirstStmt();
    }

    while (lStmt != null) {
      // Fix 2005.02.18 S.Noishi
      Stmt lStmtNext = lStmt.getNextStmt();
      // Fix 2005.04.01 S.Noishi
      if ((lStmt instanceof LabeledStmt) &&
          (((LabeledStmt)lStmt).getLabel() == null)) {
        ;
      }
      else {
        lBlock.addLastStmt(lStmt);
      }
      //lStmt = lStmt.getNextStmt();
      lStmt = lStmtNext;
    }

    // Check whether this loop has function call --------------------------
    //// Add all global variables to fBadSet if CallStmt is contained in
    //// Loop, because all global variables may be changed.
    boolean lHasFunction = false;
    for (lHirIterator = hirRoot.hir.hirIterator(lBlock);
         !lHasFunction && lHirIterator.hasNext();
         ) {
      lnode = lHirIterator.next();
      if (lnode != null && lnode.getOperator() == HIR.OP_CALL)
        lHasFunction = true;
    }
    // Except global variables --------------------------------------------
    if (lHasFunction) {
      lSymIterator = hirRoot.symRoot.symTableRoot.getSymIterator();
      while ((lVar = lSymIterator.nextVar()) != null)
        fBadVarSet.add(lVar);
    }

    // Except pointers and variables whose address are taken --------------
    //// Add to fBadVarSet is pointed, or address takened variables.
    lSymIterator = hirRoot.symRoot.symTableCurrentSubp.getSymIterator();
    while ((lVar = lSymIterator.nextVar()) != null)
      if (lVar.getSymType().getTypeKind() == Type.KIND_POINTER
          || lVar.getFlag(Sym.FLAG_ADDRESS_TAKEN))
        fBadVarSet.add(lVar);

        // Pick up assigned variables in body and step part ===================
        //// Getting lAssignedVarSet which is Assigned to LHS.
        //// if it is Array, Struct or Union, then add to fVadVarSet, too
        ////   for exclude optimization.
        //// if it is chaild of If, or Switch Stmt, then add to fVadVarSet, too
        ////   for exclude optimization. because it is not Assigned exactly.
    for (lHirIterator = hirRoot.hir.hirIterator(lBlock);
         lHirIterator.hasNextStmt(); //##62
         ) {
      lStmt = lHirIterator.getNextStmt();
      if (lStmt instanceof AssignStmt) {
        lExp = getSimpleExp(((AssignStmt)lStmt).getLeftSide());
        if (lExp.getOperator() == HIR.OP_SUBS
            || lExp.getOperator() == HIR.OP_QUAL)
          lVar = lExp.getExp1().getVar();
        else
          lVar = lExp.getVar();
        if (lVar != null && !fBadVarSet.contains(lVar))
          fBadVarSet.add(lVar);
      }
    }

    // Pick up array variables that has variant index =====================
    //// Add array variable to fBadVarSet to do not work optimization
    //// if it's subscripted var is contained in fBadVarSet.
    for (lHirIterator = hirRoot.hir.hirIterator(lBlock);
         lHirIterator.hasNext(); ) {
      lnode = lHirIterator.next();
      if (lnode instanceof SubscriptedExp) {
        Var lArrayVar = getArrayVar((SubscriptedExp)lnode);
        if (lArrayVar == null) {
          continue;
        }
        Set lVarSet = getSubscriptVar((SubscriptedExp)lnode);
        Iterator itr = lVarSet.iterator();
        while (itr.hasNext()) {
          Var var = (Var)itr.next();
          if (fBadVarSet.contains(var)) {
            fBadVarSet.add(lArrayVar);
            break;
          }
        }
      }
    }
    getDebug().print(4, lMethodName, "Variables are picked up");

    //// trace pickuped variables informations.
    getDebug().print(6, lMethodName, "fBadVarSet:" + fBadVarSet);
  }

  /**
   * Remove IfStmt from pForStmt
   * @param pForStmt ForStmt object.
   * @return  true if optimized, false if else.
   */
  protected boolean removeIfStmt(ForStmt pForStmt)
  {
    final String lMethodName = "removeIfStmt";
    HirIterator lHirIterator, lHirIterator2;
    HIR lnode;
    Stmt lStmt, lStmt2;
    ForStmt lForCopy1, lForCopy2;
    IfStmt lIfCopy;
    Exp lExp;
    boolean lremovable;
    boolean lChanged = false; // 2004.02.23 S.Noishi

    getDebug().print(3, lMethodName, "Start process:" + pForStmt.toStringDetail());
    //// 1. Get set of variables whitch is refered by
    ////   loop expand optimization.
    pickUpVariables(pForStmt);

    //// 2. find if statement in Loop-body-part.
    for (lHirIterator =
         hirRoot.hir.hirIterator(
      ((LabeledStmt)pForStmt.getLoopBodyPart()).getStmt());
         lHirIterator.hasNextStmt(); //##62
         ) {
      lStmt = lHirIterator.getNextStmt();
      // Process in case of if statement is found.
      if (lStmt instanceof IfStmt) {
        lExp = ((IfStmt)lStmt).getIfCondition();
        // Get Loop-condition-part
        //// Check whearther Loop-condition-part possibly change.
        //// if Loop-condition-part contains VarNode which is contained
        //// in BadVarSet, then it possibly change,
        //// so it is not possible to optimize.
        lremovable = true;
        for (lHirIterator2 = hirRoot.hir.hirIterator(lExp);
             lHirIterator2.hasNext();
             ) {
          lnode = lHirIterator2.next();
          if (lnode instanceof VarNode)
            if (fBadVarSet.contains(((VarNode)lnode).getVar())) {
              lremovable = false;
              break;
            }
        }
        // Execute optimizaion ========================================
        if (lremovable) {
          getDebug().print(3, lMethodName, "Removable " + lStmt.toStringDetail()); //##78
          // Check whether parent of this loop is BlockStmt =========
          //// if loop body is not child of BlockStmt,
          //// modify loop body to child.
          if (!(lStmt.getParent()instanceof BlockStmt)) {
            lStmt2 =
              hirRoot.hir.blockStmt(
              (Stmt)lStmt.copyWithOperands());
            lStmt.replaceThisNode(lStmt2);
            lStmt = ((BlockStmt)lStmt2).getFirstStmt();
          }
          //
          //  for(  )      if(  )
          //  if(  )  ->    for(  )
          //    A      A
          //  else    else
          //    B      for (  )
          //          B
          //

          // lIfCopy: Copyed object
          //   if( )
          //    A
          //   else
          //    B
          //
          lIfCopy =
            (IfStmt)lStmt.copyWithOperandsChangingLabels(null);
          // lStmt2: A or nullStmt
          if (((LabeledStmt)lIfCopy.getThenPart()).getStmt()
              != null)
            lStmt2 =
              ((LabeledStmt)lIfCopy.getThenPart()).getStmt();
          else
            lStmt2 = hirRoot.hir.nullStmt();

            //lStmt.addNextStmt(lStmt2,(BlockStmt)lStmt.getParent());
          lStmt.addNextStmt(lStmt2); // Fix 2005.02.23 S.Noishi

          lStmt = lStmt.deleteThisStmt();

          lForCopy1 =
            (ForStmt)pForStmt.copyWithOperandsChangingLabels(null);

          // Cut off because recursive expand is not work exactly
          // on this implementation. 2005.02.23 S.Noishi
          // removeIfStmt(lForCopy1);

          if (((LabeledStmt)lIfCopy.getElsePart()).getStmt()
              != null) {
            //lStmt.addNextStmt((Stmt)lIfCopy.getElsePart().getStmt(),
            //         (BlockStmt)lStmt.getParent());
            // Fix 2005.02.23 S.Noishi
            lStmt.addNextStmt(
              (Stmt)lIfCopy.getElsePart().getStmt());
          }
          lStmt.deleteThisStmt();
          lForCopy2 =
            (ForStmt)pForStmt.copyWithOperandsChangingLabels(null);

          // Cut off because recursive expand is not work exactly
          // on this implementation. 2005.02.23 S.Noishi
          //removeIfStmt(lForCopy2);

          lStmt =
            hirRoot.hir.ifStmt(
            lIfCopy.getIfCondition(),
            lForCopy1,
            lForCopy2);
          pForStmt.replaceThisNode(lStmt);
          getDebug().print(3, lMethodName, "If statement is removed");
          lChanged = true;
          break; // Do not recursive expand 2005.02.23 S.Noishi
        }
      }
    }
    return lChanged;
  }

}
