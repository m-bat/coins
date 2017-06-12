/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.opt;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList; //##77
import java.util.Map; //##64
import java.util.Set;
import java.util.Vector;

import coins.HirRoot;
import coins.ir.hir.AssignStmt;
import coins.ir.hir.BlockStmt;
import coins.ir.hir.Exp;
import coins.ir.hir.ForStmt;
import coins.ir.hir.HIR;
import coins.ir.hir.HirIterator;
import coins.ir.hir.IfStmt;
import coins.ir.hir.JumpStmt;
import coins.ir.hir.LabeledStmt;
import coins.ir.hir.LoopStmt; //##64
import coins.ir.hir.Stmt;
import coins.ir.hir.SubpDefinition;
import coins.ir.hir.SubscriptedExp;
import coins.ir.hir.SwitchStmt;
import coins.ir.hir.VarNode;
import coins.sym.Const;
import coins.sym.Sym;
import coins.sym.SymIterator;
import coins.sym.Type;
import coins.sym.Var;

/**
 * title: LoopUnrolling class.
 * description: Expand LoopStmt.
 * @author S.Noishi
 * @version 1.0
 */
public class LoopUnrolling
  extends LoopExpansion
{

  /**
   * Control variable of loop.
   */
  private Var fLoopVar;

  /**
   * Child number of fLoopVar in HIR structure of cond part.
   */
  private int fChildNumofLoopVarinCond;

  /**
   *  Variables that can't be applied any optimizations.
   */
  private Set fBadVarSet = new HashSet();

  /**
   * Variables that can be applied merge-assignment only.
   */
  private Set fBadMergeableVarSet = new HashSet();

  /**
   * Variables that are used.
   * (whose value is not set in the loop) //##77
   */
  private Set fUsedVarSet = new HashSet();

  /**
   * Set of variables that appear in both LHS and RHS of AssignStmt.
   * Ex:  variable 'a' of 'a = a + 1'
   *  Note that it is an element of this set for
   *   variable 'a' of 'a = b + c * a;'
   */
  private Set fIncrementalVarSet = new HashSet();

  /**
   * Set of variables that do not appear in both side of LHS and RHS of
   * the same AssignStmt.
   * Ex:  variable 'a' of 'a = b + c'
   *
   *   Note that all LHS variable of AssignStmt is an element of
   *   fIncrementalVarSet or  fNotIncrementalVarSet.
   */
  private Set fNotIncrementalVarSet = new HashSet();

  /**
   * Optimize option: Rate of loop unrolling; expand count of loop body.
   */
  //##64 private int fExpRate = 8;
  protected int fExpRate = 4; // Default is 4. //##64

  protected int fUpperLimitOfExpansionFactor; //##64
  protected int fNodeCountMultipliedByExpFactor; //##64



  /**
   * Optimize option is considered for SIMD environment or not.
   */
  private boolean fIsSIMD = false;

  /**
   * Get Optimize option considering SIMD environment.
   * @return true if Optimize is to be considered SIMD environment, false if else.
   */
  public boolean isSIMD()
  {
    return fIsSIMD;
  }

  /**
   * Set Optimize option by Considering SIMD environment.
   * @param isSIMD  Optimize by considering SIMD environment.
   */
  public void setSIMD_Environment(boolean isSIMD)
  {
    fIsSIMD = isSIMD;
  }

  /**
   * Get the rate of loop unrolling; expand count of loop body.
   * @return Rate of loop unrolling; expand count of loop body.
   */
  public int getExpRate()
  {
    return fExpRate;
  }

  /**
   * Set the rate of loop unrolling; expand count of loop body.
   * @param pRate  Rate of loop unrolling; expand count of loop body.
   *       it is allowed more than 2.
   */
  public void setExpRate(int pRate)
  {
    if (pRate >= 2) {
      fExpRate = pRate;
    }
  }

  /**
   * Construct this object.
   * @param phirRoot  HirRoot object.
   */
  public LoopUnrolling(HirRoot phirRoot)
  {
    super(phirRoot);

    // Set default value of detail options for optimization.
    //##64 setExpRate(8);
    //##64 setExpRate(4); //##64
    //##64 BEGIN
    if (fDbgLevel > 0)
      ioRoot.dbgOpt1.print(1, "\nLoopUnrolling ");
    // Compute default value of loop expansion factor.
    int lExpansionFactor;
    if (fNumberOfGeneralRegisters <= 8) {
      //##67 lExpansionFactor = 4; // 2;
      //##67 fUpperLimitOfExpansionFactor = 8; // 4;
      //##67 fMaxAllowableNodesInLoopBody = 100; // 60;
      //##67 fNodeCountMultipliedByExpFactor = 200; //120;
      lExpansionFactor = 2; //##67
      fUpperLimitOfExpansionFactor = 4; //##67
      fMaxAllowableNodesInLoopBody = 50; //##67
      fNodeCountMultipliedByExpFactor = 100; //##67
    }else if (fNumberOfGeneralRegisters <= 16) {
      //##67 lExpansionFactor = 4;
      //##67 fUpperLimitOfExpansionFactor = 8;
      //##67 fMaxAllowableNodesInLoopBody = 150;
      //##67 fNodeCountMultipliedByExpFactor = 300;
      lExpansionFactor = 2;
      fUpperLimitOfExpansionFactor = 4;
      fMaxAllowableNodesInLoopBody = 60;
      fNodeCountMultipliedByExpFactor = 120;
    }else if (fNumberOfGeneralRegisters <= 32) {
      //##67 lExpansionFactor = 6;
      //##67 fUpperLimitOfExpansionFactor = 8; //12;
      //##67 fMaxAllowableNodesInLoopBody = 150; //200;
      //##67 fNodeCountMultipliedByExpFactor = 300; //400;
      lExpansionFactor = 2; //##67
      fUpperLimitOfExpansionFactor = 4; //##67
      fMaxAllowableNodesInLoopBody = 100; //##67
      fNodeCountMultipliedByExpFactor = 200; //##67
    }else {
      //##67 lExpansionFactor = 8;
      //##67 fUpperLimitOfExpansionFactor = 12;
      //##67 fMaxAllowableNodesInLoopBody = 300;
      //##67 fNodeCountMultipliedByExpFactor = 600;
      lExpansionFactor = 4; //##67
      fUpperLimitOfExpansionFactor = 8; //##67
      fMaxAllowableNodesInLoopBody = 200; //##67
      fNodeCountMultipliedByExpFactor = 400; //##67
    }
    String lOptionValue = (String)fOptionMap.get("loopexp");
    // 2013.3.26
    //    if ((lOptionValue != "") &&
    if ((lOptionValue != null) && (lOptionValue != "") &&
        (Character.isDigit(lOptionValue.charAt(0)))) {
      // Loop expansion factor is specified. Use it.
      if ((lOptionValue.length() >= 2)&&
          (Character.isDigit(lOptionValue.charAt(1))))
        lOptionValue = lOptionValue.substring(0, 2); // Use up to 2nd digit.
      else
        lOptionValue = lOptionValue.substring(0, 1); // Use 1st digit only.
      Integer lValue = Integer.valueOf(lOptionValue);
      lExpansionFactor = lValue.intValue();
      fUpperLimitOfExpansionFactor = lExpansionFactor;
    }
    setExpRate(lExpansionFactor);
    if (fDbgLevel > 0)
      ioRoot.dbgOpt1.print(2, " upper limit of expansion factor "
        + fUpperLimitOfExpansionFactor);
    //##64 END
    if (fOptions.isSet("simd"))  //##64
      setSIMD_Environment(true); //##64
    else //##64
      setSIMD_Environment(false);
  } // LoopUnrolling(pHirRoot)
  /**
   * Constructor specifying loop unrolling parameters.
   * @param pHirRoot HirRoot instance.
   * @param pExpansionFactor upper limit of loop expansion factor.
   * @param pMaxAllowableNodesInLoopBody maximum number of allowable
   *   nodes in loop body.
   * @param pNodeCountMultipliedByExpFactor upper limit of the number of nodes
   *   in loop body after loop expansion.
   */
  public LoopUnrolling(HirRoot pHirRoot, int pExpansionFactor,
    int pMaxAllowableNodesInLoopBody, int pNodeCountMultipliedByExpFactor)
  {
    this(pHirRoot);
    fUpperLimitOfExpansionFactor = pExpansionFactor;
    fMaxAllowableNodesInLoopBody = pMaxAllowableNodesInLoopBody;
    fNodeCountMultipliedByExpFactor = pNodeCountMultipliedByExpFactor;
    if (fDbgLevel > 0)
      ioRoot.dbgOpt1.print(2, " UpperLimitOfExpansionFactor="
        + fUpperLimitOfExpansionFactor +
        " MAxAllowableNodesInLoopBody=" + fMaxAllowableNodesInLoopBody
        + " NodeCountMultipliedByExpFactor=" + fNodeCountMultipliedByExpFactor);
  } // LoopUnrolling(phirRoot, pExpansionFactor, pMaxAllowableNodesInLoopBody)

  /**
   * Do Loop Expansion optimization in subprogram.
   * @param  SubpDefinition to do Loop Expansion optimization.
   * @return  true if optimized, false if else.
   */
  public boolean doSubprogram(SubpDefinition pSubpDef)
  {
    final String lMethodName = "doHir";

    HirIterator lHirIterator; // HirIterator for subprogram
    Stmt lStmt; // Statement of subprogram
    getDebug().print(2, lMethodName, "Start process");

    flowRoot.subpUnderAnalysis = pSubpDef.getSubpSym();
    symRoot.symTableCurrent = pSubpDef.getSymTable();
    symRoot.symTableCurrentSubp = symRoot.symTableCurrent;

    boolean lChanged = false;
    // Do each For-LoopStmt of HIR body of SubpDef.
    for (lHirIterator = hirRoot.hir.hirIterator(pSubpDef.getHirBody());
         lHirIterator.hasNextStmt(); //##62
         ) {
      lStmt = lHirIterator.getNextStmt();
      if (lStmt != null && lStmt.getOperator() == HIR.OP_FOR) {
        int lExpansionFactor = computeExpansionFactor((ForStmt)lStmt); //##64
        getDebug().print(2, lMethodName, "Loop is found. ExpansionFactor "
          + lExpansionFactor + " Line " + lStmt.getLineNumber()); //##67
        if (lExpansionFactor <= 1) //##64
          continue; // Do not expand. //##64
        setExpRate(lExpansionFactor); //##64
        if (isExpansible((ForStmt)lStmt)) {
          expandLoop((ForStmt)lStmt);
          lChanged = true;
        }
      }
    }

    // 2005.03.16 S.Noishi
    if (lChanged) {
      //##62  pSubpDef.setIndexNumberToAllNodes(0);
    }

    return lChanged;
  } // doSubprogram

  /**
   * Analyze variables contained in ForStmt to
   * get information to doing Loop expansion optimization.
   * @param pForStmt ForStmt to be analyzed.
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
    // lBlock is the block that contains body and step part to be checked.
    Set lAssignedVarSet = new HashSet(); // Variables that are assigned

    getDebug().print(3, lMethodName, "Start process");

    fBadVarSet.clear();
    fBadMergeableVarSet.clear();
    fUsedVarSet.clear();
    fIncrementalVarSet.clear();
    fNotIncrementalVarSet.clear();

    //==================================== 1 ==============================
    // Create a block and add body part and step part to it to do chacking.
    //// Create HIR that is chained loop-body and loop-step-part,
    //// then generate sets to refer for optimization.
    lBlock = hirRoot.hir.blockStmt(null);
    //##76 lStmt =
    //##76   ((BlockStmt)((LabeledStmt)pForStmt.getLoopBodyPart())
    //##76    .getStmt().copyWithOperandsChangingLabels(null))
    //##76   .getFirstStmt();
    //##76 BEGIN
    Stmt lLoopBodyPart = pForStmt.getLoopBodyPart();
    if (lLoopBodyPart instanceof LabeledStmt)
      lLoopBodyPart = ((LabeledStmt)lLoopBodyPart).getStmt();
      if (lLoopBodyPart instanceof BlockStmt) {
        lStmt= ((BlockStmt)lLoopBodyPart).getFirstStmt();
      }else
        lStmt = lLoopBodyPart;
    lStmt = (Stmt)lStmt.copyWithOperandsChangingLabels(null);
    //##76 END
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
      //    lStmt = lStmt.getNextStmt();
      lStmt = lStmtNext;
    }
    if (pForStmt.getLoopStepPart() != null) {
      // Fix 2005.03.04 S.Noishi
      lStmt = (Stmt)pForStmt.getLoopStepPart()
        .copyWithOperandsChangingLabels(null);
      if (lStmt instanceof BlockStmt) {
        lStmt = ((BlockStmt)lStmt).getFirstStmt();
      }
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

    // Pick up variables that can't be used for LoopVar ===================
    // Check whether this loop has function call --------------------------
    //// Add all global variables to fBadSet if CallStmt is contained in
    //// Loop, because all global variables may be changed.
    boolean lHasFunction = false;
    for (lHirIterator = hirRoot.hir.hirIterator(lBlock);
         !lHasFunction && lHirIterator.hasNext();
         ) {
      lnode = lHirIterator.next();
      if (lnode != null && lnode.getOperator() == HIR.OP_CALL) {
        lHasFunction = true;
      }
    }
    // Except global variables --------------------------------------------
    if (lHasFunction) {
      lSymIterator = hirRoot.symRoot.symTableRoot.getSymIterator();
      while ((lVar = lSymIterator.nextVar()) != null) {
        fBadVarSet.add(lVar);
      }
    }

    // Except pointers and variables whose address are taken --------------
    //// Add to fBadVarSet is pointed, or address taken variables.
    lSymIterator = hirRoot.symRoot.symTableCurrentSubp.getSymIterator();
    while ((lVar = lSymIterator.nextVar()) != null) {
      if (lVar.getSymType().getTypeKind() == Type.KIND_POINTER
          || lVar.getFlag(Sym.FLAG_ADDRESS_TAKEN)) {
        fBadVarSet.add(lVar);

        //==================================== 2 ==============================
        // Pick up used variables =============================================
        //// Add used variables to fUsedVarSet.
      }
    }
    boolean lusedVar;
    for (lHirIterator = hirRoot.hir.hirIterator(lBlock);
         lHirIterator.hasNext();
         ) {
      lnode = lHirIterator.next();
      if (lnode instanceof VarNode) {
        lnode2 = lnode;
        lusedVar = true;
        while (lnode2 != lBlock
               && lnode2.getOperator() != HIR.OP_INDEX
               && lusedVar) {
          // Note: lusedVar is set to false if lnode2 is
          // the destination of AssginStmt, like Assign( lnode2, ...),
          // because it is changed.
          if (lnode2.getParent()instanceof AssignStmt) {
            if (lnode2.getChildNumber() == 1) { // LHS of AssignStmt
              //##100 BEGIN 
              if ((lnode2.getOperator() == HIR.OP_CONV)&&
                  (lnode2.getChild1() instanceof VarNode)&&
                  (((VarNode)lnode2.getChild1()).getVar() ==
                  ((VarNode)lnode).getVar())) 
                //##100 END
            	lusedVar = false;            	
            }
            else { // RHS of AssignStmt
              // Note: Do not add if used RHS variable is
              // appeared in LHS, like 'i = i + 1 '.
              lExp =
                ((AssignStmt)lnode2.getParent()).getLeftSide();
              if (getSimpleExp(lExp).getVar()
                  == ((VarNode)lnode).getVar()) {
                lusedVar = false;
              }
            }
          }
          lnode2 = (HIR)lnode2.getParent(); //##100 
        }
        if (lusedVar) {
          fUsedVarSet.add(((VarNode)lnode).getVar());
        }
      }
    }

    //==================================== 3 ==============================
    // Pick up assigned variables in body part and step part ===================
    //// Getting lAssignedVarSet which is Assigned to LHS.
    //// if it is Array, Struct or Union, then add to fBadVarSet, too
    ////   to exclude optimization.
    //// if it is child of If, or Switch Stmt, then add to fBadVarSet, too
    ////   to exclude optimization, because it is not Assigned exactly.
    for (lHirIterator = hirRoot.hir.hirIterator(lBlock);
         lHirIterator.hasNextStmt(); //##62
         ) {
      lStmt = lHirIterator.getNextStmt();
      if (lStmt instanceof AssignStmt) {
        // Get Var which is in LHS of Assignment.
        lExp = getSimpleExp(((AssignStmt)lStmt).getLeftSide());
        if (lExp.getOperator() == HIR.OP_SUBS
            || lExp.getOperator() == HIR.OP_QUAL) {
          lVar = lExp.getExp1().getVar();
        }
        else {
          lVar = lExp.getVar();
        }
        // Note: about checking of '!fBadVarSet' in if statement.
        //   It is not necessary to add lAssignedVarSet, if
        //  it has already added to fBadVarSet. (maybe).
        if (lVar != null && !fBadVarSet.contains(lVar)) {
          lAssignedVarSet.add(lVar);
          if (lVar.getSymType().getTypeKind() == Type.KIND_VECTOR
              || lVar.getSymType().getTypeKind() == Type.KIND_STRUCT
              || lVar.getSymType().getTypeKind() == Type.KIND_UNION) {
            fBadVarSet.add(lVar);
          }
          else {
            lnode = (HIR)lStmt.getParent();
            while (lnode != lBlock) {
              if (lnode instanceof IfStmt
                  || lnode instanceof SwitchStmt) {
                fBadVarSet.add(lVar);
              }
              lnode = (HIR)lnode.getParent();
            }
          }
        }
      }
    }

    //==================================== 4 ==============================
    // Pick up array variables that has variant index =====================
    //// Add array variable to fBadVarSet to do no optimization
    //// if it's subscript var is contained in fBadVarSet.

    //     CheckBadSubscript(lBlock, lAssignedVarSet, fBadVarSet);
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
          if (lAssignedVarSet.contains(var) ||
              fBadVarSet.contains(var)) {
            fBadVarSet.add(lArrayVar);
            break;
          }
        }
      }
    }

    //==================================== 5 ==============================
    // Pick up bad assignment =============================================
    for (lHirIterator = hirRoot.hir.hirIterator(lBlock);
         lHirIterator.hasNextStmt(); //##62
         ) {
      lStmt = lHirIterator.getNextStmt();
      if (lStmt instanceof AssignStmt) {
        //// Do for each AssignStmt.
        // Get LHS Var.
        Var lAssignedVar =
          getSimpleExp(((AssignStmt)lStmt).getLeftSide()).getVar();
        if (lAssignedVar != null) {
          //// Do for each RHS Node of each AssignStmt.
          boolean lIncremental = false;
          for (lHirIterator2 =
               hirRoot.hir.hirIterator(
            ((AssignStmt)lStmt).getRightSide());
               lHirIterator2.hasNext();
               ) {
            HIR lRSideNode = lHirIterator2.next();
            // Note: lRSideNode is instance of Exp at all time
            //    because It is Exp of RHS of AssginStmt(may be).
            if (lRSideNode instanceof Exp) {
              Exp lRSideExp = getSimpleExp((Exp)lRSideNode);
              if (lRSideExp.getVar() != null) {
                // In case of both LHS and RHS contain identical.
                if (lRSideExp.getVar() == lAssignedVar) {
                  lIncremental = true;
                  // Add LHS ( is equal to RHS ) variable
                  // to fBadMergeableVarSet
                  // in NOT case of as following in RHS:
                  // (following case is allowed.)
                  //   ADD:i+x, x+i
                  //     SUB:i-x
                  //   Use not CONV
                  while (lRSideExp.getParent() != lStmt
                    && !fBadMergeableVarSet.contains(
                    lAssignedVar)) {
                    if (!(lRSideExp.getParent().getOperator()
                      == HIR.OP_CONV
                      || lRSideExp.getParent().getOperator()
                      == HIR.OP_ADD
                      || lRSideExp.getParent().getOperator()
                      == HIR.OP_SUB)
                      || (lRSideExp.getParent().getOperator()
                      == HIR.OP_SUB
                      && lRSideExp.getChildNumber() != 1)) {
                      fBadMergeableVarSet.add(lAssignedVar);
                    }
                    lRSideExp = (Exp)lRSideExp.getParent();
                  }
                }
                // Add RHS variable to fBadVarSet if RHS variable
                // is not LHS variable, and RHS variable is
                // contained in fBadMergeableVarSet.
                else if (
                  fBadVarSet.contains(lRSideExp.getVar())
                  || fBadMergeableVarSet.contains(
                  lRSideExp.getVar())) {
                  fBadVarSet.add(lAssignedVar);
                }
                // Add LHS variable to fBadMergeableVarSet
                // if RHS variable is
                // no LHS variable, and RHS variable is contained
                // in lAssignedVarSet.
                // Because it may be Loop-control-var.
                else if (
                  lAssignedVarSet.contains(lRSideExp.getVar())) {
                  fBadMergeableVarSet.add(lAssignedVar);
                }
              }

            }
            else {
              // In case of RHS of AssignStmt is not Exp.
              // Note: it may be better to do nothing in this case.
              fBadVarSet.add(lAssignedVar);
            }
          }
          if (lIncremental) {
            fIncrementalVarSet.add(lAssignedVar);
          }
          else {
            fNotIncrementalVarSet.add(lAssignedVar);
          }
        }
      }
    }
    if (fDbgLevel >= 3) { //##77
    getDebug().print(4, lMethodName, "Variables are picked up");

    //// trace pickup variables informations.
    getDebug().print(4, lMethodName, "fBadVarSet:" + fBadVarSet);
    getDebug().print(4, lMethodName,
      "fBadMergeableVarSet:" + fBadMergeableVarSet);
    getDebug().print(4, lMethodName, "fUsedVarSet:" + fUsedVarSet);
    getDebug().print(4, lMethodName, "fIncrementalVarSet:" + fIncrementalVarSet);
    getDebug().print(4, lMethodName,
      "fNotIncrementalVarSet:" + fNotIncrementalVarSet);
    } //##77
  } // pickUpVariables

  /**
   * Check whether loop-unrolling optimization can be applied to pForStmt
   * The Loop-control-variable is set to fLoopVar.
   * if Loop-control-variable is not found,  it is set to null.
   * @param  pForStmt ForStmt object.
   * @return  true  pForStmt may be optimized by Loop-if expansion.
   *         false pForStmt is not optimized by Loop-if expansion.
   */
  protected boolean isExpansible(ForStmt pForStmt)
  {
    final String lMethodName = "isExpansible";
    HirIterator lHirIterator, lHirIterator2;
    HIR lnode;
    if (fDbgLevel > 0) //##77
    getDebug().print(3, lMethodName, "Start process:" + pForStmt.toStringDetail());

    // Do not optimize if ForStmt contains bad element.
    if (hasBadElement(pForStmt)) {
      getDebug().print(3, lMethodName, "Bad element is found.");
      return false;
    }

    // Check whether this loop is innermost loop ========================
    //// 1.Do not optimize if it is not innermost loop.
    ////  ; Check whether it contains loop-statement.
    Stmt lLoopBodyPart = pForStmt.getLoopBodyPart(); //##76
    if (lLoopBodyPart instanceof LabeledStmt) //##76
      lLoopBodyPart = ((LabeledStmt)lLoopBodyPart).getStmt(); //##76
    for (lHirIterator =
         //##76 hirRoot.hir.hirIterator(((LabeledStmt)pForStmt.getLoopBodyPart()).getStmt());
         hirRoot.hir.hirIterator(lLoopBodyPart); //##76
         lHirIterator.hasNextStmt(); //##62
         ) {
      lnode = lHirIterator.getNextStmt();
      if (lnode instanceof ForStmt) { //  && ((Stmt)lnode).isLoopStmt()){
        getDebug().print(4, lMethodName, "Not innermost loop");
        return false;
      }
    }
    getDebug().print(4, lMethodName, "Innermost loop");

    // Do not optimize if Stmt count in Loop-body-part
    // is greater than the allowable value
    // which is get by getMaxAllowableStmtsInLoopBody().
    int cntStmtInBody = calcStatementCount(pForStmt.getLoopBodyPart());
    getDebug().print(
      4,
      lMethodName,
      "statement count in body is " + cntStmtInBody);
    /* //##64
    if (cntStmtInBody > getMaxAllowableStmtsInLoopBody()) {
      getDebug().print(
        3,
        lMethodName,
        "statement count "
        + cntStmtInBody
        + " is overd for "
        + getMaxAllowableStmtsInLoopBody());
      return false;
    }
    */ //##64

    // Check start condition ==============================================
    //// 2. Do not optimize if condition part does not exist, or it's
    ////  expression is not composed by  '<', '<=', '>' or '>=' operator.
    ////  Because this optimization only treat Loop-control-variable
    ////  that is incremented or decremented by a constant value.
    if (pForStmt.getLoopStartCondition() == null) {
      return false;
    }
    getDebug().print(4, lMethodName, "Single start condition");
    int lCondOP = pForStmt.getLoopStartCondition().getOperator();
    if (!(lCondOP == HIR.OP_CMP_GT
          || lCondOP == HIR.OP_CMP_GE
          || lCondOP == HIR.OP_CMP_LT
          || lCondOP == HIR.OP_CMP_LE)) {
      return false;
    }
    getDebug().print(4, lMethodName, "Compare GT|GE|LT|LE");

    //// 3. Get set of variables which is referred by
    ////   loop expansion optimization.
    pickUpVariables(pForStmt);
    // Choose loop control variable =======================================
    fLoopVar = null; // Loop-control-variable
    //// Note: The reason of not do once, but do twice by looping:
    ////  Check whether Loop-control-variable is more then one,
    ////  In that case, the loop is not expanded.
    for (int i = 1; i <= 2; i++) {

      // Do each Node in condition part.
      for (lHirIterator =
           hirRoot.hir.hirIterator(
        pForStmt.getLoopStartCondition().getChild(i));
           lHirIterator.hasNext();
           ) {
        lnode = lHirIterator.next();
        if (lnode instanceof VarNode) {

          //// 4. Check whether Loop condition expression is changed
          ////  in Loop.
          ////  As following, variable 'i' become an element of
          ////  fNotIncrementalVarSet,
          ////  so It is invalid to optimize.
          ////  for( ; i < N ; i++ ){
          ////    i = 5;
          ////  }
          ////
          ////  As following, variable 'i' is not become an element
          ////  of fNotIncrementalVarSet,
          ////  so It is valid to optimize.
          ////  for( ; i < N ; i++ ){
          ////    i = 5 + i;
          ////  }
          if (fBadVarSet.contains(((VarNode)lnode).getVar())
              || fBadMergeableVarSet.contains(
            ((VarNode)lnode).getVar())
              || fNotIncrementalVarSet.contains(
            ((VarNode)lnode).getVar())) {
            getDebug().print(
              4,
              lMethodName,
              "Condpart maybe changed.");
            return false;
          }
          else if (
            // fIncrementalVarSet: 'i = i + 1', etc...
            // this element is Loop-control-variable.
            //  Note that in case of 'i = i * 2', etc,
            //  It is contained in fBadMergeableVarSet.
            fIncrementalVarSet
            .contains(
            ((VarNode)lnode).getVar())) {
            if (fLoopVar == null) {
              fLoopVar = ((VarNode)lnode).getVar();
              fChildNumofLoopVarinCond = i;
            }
            else {
              // Note that it is not able to optimize
              // if Loop-control-variable found more than one.
              getDebug().print(
                4,
                lMethodName,
                "Loop has control variables multiply " + fLoopVar);
              return false;
            }
          }
        }
      }
    }
    // fLoopVar is null in this point, that is to say,
    // Loop-control-variable is not found.
    if (fLoopVar == null) {
      getDebug().print(4, lMethodName, "This loop doesn't have LoopControlVar");
      return false;
    }
    //##100 BEGIN
    if (! fLoopVar.getSymType().isBasicType()) {
      getDebug().print(4, lMethodName, "LoopControlVar " + fLoopVar.getName()
    		  + " type " + fLoopVar.getSymType().getName() + 
    		  " is not a basic type. This loop is not expansible.");
   	  return false;
    }
    //##100 END
    getDebug().print(2, lMethodName, "This loop is expansible");
    return true;
  } // isExpansible

  // Apply loop-unrolling optimization to pForStmt
  protected void expandLoop(ForStmt pForStmt)
  {
    final String lMethodName = "expandLoop";
    HirIterator lHirIterator, lHirIterator2;
    HIR lnode = null;
    Stmt lStmt = null, lStmt2 = null;
    Stmt lStepPart = null;
    Stmt lBodyPart, lBodyCopy; // Loop body part and its copy
    Exp lStartCondcopy; // Loop start condition and its copy
    Exp lStepExpInStep; // Exp that represents amount of increase of
    // Loop-control-variable in Loop-step-part.
    Exp lStepExpInBody; // Exp that represents amount of increase of
    // Loop-control-variable in Loop-body-part.
    BlockStmt lNewBody, lNewStep; // New body part and new step part
    Exp lExp, lExp2;
    Var lVar, lVar2;
    Const lConst;
    Type lAddExpType, lStepExpType;  // Type of ADD-exp and step exp. //##77

    // Constant value specified in lStepExpInStep, if Exp which
    // represents amount of increase in Loop-step-part
    // does not include any VarNode.
    Const lStepConst = null;

    getDebug().print(3, lMethodName, "Start process " + pForStmt.toStringShort());

    //=============== 6.5.1 Conversion of Loop-step-part ==================
    // Remove assignment to variables that are not LoopVar from step part
    lBodyPart = (Stmt)pForStmt.getLoopBodyPart()
                .copyWithOperandsChangingLabels(null);
    if (pForStmt.getLoopStepPart() != null) {
      lStepPart = (Stmt)pForStmt.getLoopStepPart()
                .copyWithOperandsChangingLabels(null);
      // Fix 2005.03.04
      if (lStepPart instanceof BlockStmt) {
        lStmt = ((BlockStmt)lStepPart).getFirstStmt();
      }
      else {
        lStmt = lStepPart;
      }

      //// Add the whole of Loop-Step-part to the last of the loop body if
      //// Loop-control-value is referred in RHS of assignment.
      while (lStmt != null) {
        // In case of assignment whose LHS's variable
        //  is not Loop-control variable.
        if (lStmt instanceof AssignStmt
            && getSimpleExp(((AssignStmt)lStmt).getLeftSide()).getVar()
            != fLoopVar) {

          // Do for each RHS node in case of assignment
          //  whose LHS's variable
          //  is not Loop-control-variable.
          for (lHirIterator = hirRoot.hir.hirIterator(
                 ((AssignStmt)lStmt).getRightSide());
               lHirIterator.hasNext(); ) {
            lnode = lHirIterator.next();
            // In case of assignment whose RHS's any variable is
            // Loop-control-variable;
            // Loop-control-variable is referred.
            if (lnode instanceof VarNode
                && ((VarNode)lnode).getVar() == fLoopVar) {
              // Add the whole of Loop-step-part to the last of
              // the loop body, then break.
              lStmt = ((BlockStmt)lStepPart).getFirstStmt();
              lStmt2 = ((LabeledStmt)lBodyPart).getStmt();
              while (lStmt != null) {
                ((BlockStmt)lStmt2).addLastStmt(
                  (Stmt)lStmt.copyWithOperands());
                lStmt = lStmt.getNextStmt();
              }
              lStepPart = null;
              break;
            }
          }
        }
        if (lStmt != null) {
          lStmt = lStmt.getNextStmt();
        }
      }
    }
    // lStmt is not used. Why ? //##77

    // Add AssignStmt whose RHS is not Loop-control-variable
    // to the last of the loop body.
    if (lStepPart != null) {
      // Fix 2005.03.04
      //lStmt = ((BlockStmt) lStepPart).getFirstStmt();
      if (lStepPart instanceof BlockStmt) {
        lStmt = ((BlockStmt)lStepPart).getFirstStmt();
      }
      else {
        lStmt = lStepPart;
      }

      lStmt2 = ((LabeledStmt)lBodyPart).getStmt();
      while (lStmt != null) {
        if (!(lStmt instanceof AssignStmt)
            || getSimpleExp(((AssignStmt)lStmt).getLeftSide()).getVar()
            != fLoopVar) {
          ((BlockStmt)lStmt2).addLastStmt(
            (Stmt)lStmt.copyWithOperands());
          lStmt = lStmt.deleteThisStmt();  //## ?
        }
        else {
          lStmt = lStmt.getNextStmt();
        }
      }
    }
    getDebug().print(4, lMethodName, "Step part is converted " + lStmt);
    if ((fDbgLevel > 0)&&(lStmt != null)) //##77
      getDebug().print(4, lMethodName, "step part "
        + lStmt.toStringWithChildren()); //##77


    // Get Step Exp ======================================================
    lStepExpInBody = lStepExpInStep = null;
    boolean lStepWithVar = false;
    // Get Exp which represents the amount of increase for
    // Loop-control-variable in Loop-step-part.
    if (lStepPart != null) {
      // Fix 2005.03.04
//      lStepExpInStep = getStepExp((BlockStmt) lStepPart);
      lStepExpInStep = getStepExp(lStepPart);
    }

    // Check whether this StepExp contains VarNode ------------------------
    //// Check whether Exp which represents amount of increase for
    //// Loop-control-variable in Loop-step-part contains any VarNode.
    //// if that contains, set lStepWithVar to true.
    if (lStepExpInStep != null) {
      for (lHirIterator = hirRoot.hir.hirIterator(lStepExpInStep);
           lHirIterator.hasNext(); ) {
        lnode = lHirIterator.next();
        if (lnode instanceof VarNode) {
          lStepWithVar = true;
        }
      }
    }
    // if Exp which represents amount of increase of Loop-control-variable
    // in Loop-step-part is not contains any VarNode, then
    // create a constant value to calculate lStepExpInStep.
    if (lStepExpInStep != null && !lStepWithVar) {
      /*
             Number lNum = (Number) lStepExpInStep.evaluateAsObject();
             if (lStepExpInStep.evaluateAsObject() instanceof Long)
        lStepConst =
        symRoot.sym.intConst(lNum.longValue(), symRoot.typeLong);
             else
        lStepConst =
        symRoot.sym.floatConst(
          lNum.doubleValue(),
          symRoot.typeDouble);
       */
      // Fix as following,
      // as evaluateAsObject() is removed. 2005.02.16 S.Noishi
      lStepConst = lStepExpInStep.evaluate();
      if (lStepConst != null)   //##100
        lStepExpInStep = hirRoot.hir.constNode(lStepConst);
       else                     //##100
        lStepExpInStep = null;  //##100
    }

    // Get Exp which represents amount of increase of
    // Loop-control-variable in Loop-body.
    // Fix 2005.03.04
    lStepExpInBody =
//    getStepExp((BlockStmt) ((LabeledStmt) lBodyPart).getStmt());
      getStepExp(((LabeledStmt)lBodyPart).getStmt());

    // Check whether this StepExp contains VarNode ------------------------
    //// Check whether Exp which represents amount of increase for
    //// Loop-control-variable in Loop-body-part,
    //// if VarNode is contained, set lStepWithVar to true.
    if (lStepExpInBody != null) {
      for (lHirIterator = hirRoot.hir.hirIterator(lStepExpInBody);
           lHirIterator.hasNext(); ) {
        lnode = lHirIterator.next();
        if (lnode instanceof VarNode) {
          lStepWithVar = true;
        }
      }
    }
    getDebug().print(4, lMethodName, "StepExp is generated " + lnode);

    //============== 6.5.2 Expanding of Loop body =========================
    // Expand loop body part ==============================================
    lNewBody = hirRoot.hir.blockStmt(null);
    //// if Exp which represents the amount of increase for
    //// Loop-control-variable in loop body is found,
    //// or if any VarNode is found in the expression representing
    //// the amount to increase, then there may be
    //// some constraint in any point of optimize.
    //// So in this case, do only loop expansion,
    //// and do not merge assign statement.
    if (lStepExpInBody != null || lStepWithVar) {
      // Create lNewBody by generating followings sequence getExpRate() times.
      //   loop-body
      //   i = i + step;
      lAddExpType = typeForArithmeticExp(fLoopVar.getSymType()); //##77
      for (int i = 0; i < getExpRate(); i++) {
        lBodyCopy =
          (Stmt)lBodyPart.copyWithOperandsChangingLabels(null);
        lStmt =
          ((BlockStmt)((LabeledStmt)lBodyCopy).getStmt())
          .getFirstStmt();
        while (lStmt != null) {
          // Fix 2005.02.18 S.Noishi
          Stmt lStmtNext = lStmt.getNextStmt();
          //// Do not add LabeledNull considering
          //// other optimization 2005.03.17 S.Noishi
          if ((lStmt instanceof LabeledStmt) &&
              ((LabeledStmt)lStmt).getStmt() == null) {
            ; // do not add.
          }
          else {
            lNewBody.addLastStmt(lStmt);
          }
          lStmt = lStmt.getNextStmt();

        }
        if (lStepExpInStep != null) {
          // Add 'i = i + step'
          /*  //##77
          lExp =
            hirRoot.hir.exp(
            HIR.OP_ADD,
            hirRoot.hir.varNode(fLoopVar),
            lStepExpInStep);
          */ //##77
          lExp = integralPromotion( HIR.OP_ADD,
            hirRoot.hir.varNode(fLoopVar), (Exp)lStepExpInStep //##77
              .copyWithOperands()); //##78
          //##77 lStmt = hirRoot.hir.assignStmt(hirRoot.hir.varNode(fLoopVar), lExp);
          lStmt = makeAssignStmt(fLoopVar, (Exp)lExp  //##77
              .copyWithOperands()); //##78

          lNewBody.addLastStmt(lStmt);
          //##77 BEGIN
          if (fDbgLevel > 0) {
            getDebug().print(4, lMethodName, "Add step stmt " +
              lStmt.toStringWithChildren() + " to loop body");
          }
          //##77 END
        }
      }
    }
    // In case of:
    //   Exp which represents the amount of increase for Loop-control-variable
    //   in Loop-body-part is not found.
    //   and any VarNode is not found in the amount of increase exp for
    //   Loop-control-variable.
    // That may be able to be adapted all optimization,
    // so do checking each optimization.
    else {
      // Check whether this Loop doesn't contain JumpStmt ===============
      //// If so, do not do merge assignment by setting lmergeable to false.
      BlockStmt lMerged = hirRoot.hir.blockStmt(null);
      boolean lmergeable = true;
      Stmt lLoopBodyPart2 = pForStmt.getLoopBodyPart(); //##76
      if (lLoopBodyPart2 instanceof LabeledStmt)  //##76
        lLoopBodyPart2 = ((LabeledStmt)lLoopBodyPart2).getStmt(); //##76
      for (lHirIterator =
           //##76 hirRoot.hir.hirIterator(((LabeledStmt)pForStmt.getLoopBodyPart()).getStmt());
           hirRoot.hir.hirIterator(lLoopBodyPart2); //##76
           lHirIterator.hasNextStmt(); //##62
           ) {
        lnode = lHirIterator.getNextStmt();
        if (lmergeable && lnode instanceof JumpStmt) {
          lmergeable = false;
        }
      }

      //// In case of doing merge of AssignStmt.
      if (lmergeable) {
        //============== 6.5.3 Merger of AssignStmt ==================
        //// The Loop may be change by doing conversion of
        ////  Loop-step-part(6.5.1), so do pickUpVariables(),
        //// from newly created LoopStmt
        //// which consists of the result of changing.
        pickUpVariables(
          hirRoot.hir.forStmt(
          null,
          pForStmt.getLoopStartCondition(),
          lBodyPart,
          lStepPart));

        // Pick up mergeable variables ================================
        //// Calculate lMergeableVarSet:
        ////  exclude follows from
        ////  fIncrementalVarSet: ( 'a' of 'a = a * b', etc.)
        ////  fNotIncrementalVarSet: ('a' of 'a = b + c', etc.)
        ////  fBadVarSet
        ////  fUsedVarSet:( 'a' of 'x = a', etc.
        ////       because value of 'a' may be changed at
        ////       point of 'x = a' to move somewhere of
        ////       loop body. )
        Set lMergeableVarSet = new HashSet(fIncrementalVarSet);
        lMergeableVarSet.removeAll(fNotIncrementalVarSet);
        lMergeableVarSet.removeAll(fBadVarSet);
        lMergeableVarSet.removeAll(fUsedVarSet);
        // Do fore each AssignStmt in loop body.
        for (lHirIterator =
             hirRoot.hir.hirIterator(
          ((LabeledStmt)lBodyPart).getStmt());
             lHirIterator.hasNextStmt(); //##62
             ) {
          lStmt = lHirIterator.getNextStmt();
          if (lStmt instanceof AssignStmt) {
            // Get LHS variable in AssignStmt.
            Var lAssignedVar =
              getSimpleExp(((AssignStmt)lStmt).getLeftSide())
              .getVar();
            // In case of enable for merge AssignStmt
            // of LHS variable.
            if (lMergeableVarSet.contains(lAssignedVar)) {

              // Do for each node in RHS which is
              // merged assign statement.
              for (lHirIterator2 =
                   hirRoot.hir.hirIterator(
                ((AssignStmt)lStmt).getRightSide());
                   lHirIterator2.hasNext(); ) {
                lnode = lHirIterator2.next();
                // if RHS variable, which is not identical to LHS,
                // and is not Loop-control-variable, is changed,
                // then remove LHS form lMergeableVarSet.
                // That is to say, checking whether
                // 'c' of 'a = a + c' is never changed.
                if (lnode instanceof VarNode
                    && ((VarNode)lnode).getVar() != lAssignedVar
                    && ((VarNode)lnode).getVar() != fLoopVar) {

                  //// Check whether RHS variable is changed.
                  // Note:
                  // all of LHS variable is contained
                  // one of both of follows.
                  // fIncrementalVarSet:('c' of 'c = c + a')
                  // fNotIncrementalVarSet:('c' of 'c= a + b')
                  if (fIncrementalVarSet
                    .contains(((VarNode)lnode).getVar())
                    || fNotIncrementalVarSet.contains(
                    ((VarNode)lnode).getVar())) {
                    lMergeableVarSet.remove(lAssignedVar);
                    break;
                  }
                }
              }
            }
          }
        }
        if (fDbgLevel > 0)
        getDebug().print(
          4,
          lMethodName,
          "Mergeable variables are picked up " + lMergeableVarSet);
        // Remove mergeable assignment from body part =================
        //// Merge assign statement, then add to lMerged.
        for (lHirIterator =
             hirRoot.hir.hirIterator(
          ((LabeledStmt)lBodyPart).getStmt());
             lHirIterator.hasNextStmt(); //##62
             ) {
          lStmt = lHirIterator.getNextStmt();
          // Merge LHS variable which is mergeable,
          // then add to lMerged.
          if (lStmt instanceof AssignStmt) {
            lExp = getSimpleExp(((AssignStmt)lStmt).getLeftSide());
            if (lMergeableVarSet.contains(lExp.getVar())) {
              // merge assign statement.
              lStmt2 = mergeAssignStmt(lStmt, lStepConst);
              // add merged statement to lMerged.
              lMerged.addLastStmt(
                (Stmt)lStmt2.copyWithOperandsChangingLabels(
                null));
              // remove original assign statement
              lStmt.deleteThisStmt(); //## conflict ?
            }
          }
        }
      }

      //// Expand loop body.
      for (int i = 0; i < getExpRate(); i++) {
        if (fDbgLevel > 0)  //##77
          getDebug().print(4,lMethodName, "Expand loop body (" + i + "-th)"); //##77
        // At first, copy loop body to lBodyCopy.
        lBodyCopy =
          (Stmt)lBodyPart.copyWithOperandsChangingLabels(null);
        // Modify lBodyCopy as n times of expansion
        // (no change at n=1), then add to lNewBody.
        if (i != 0) {
          // Do for each Node of lBodyCopy.
          for (lHirIterator =
               hirRoot.hir.hirIterator(
            ((LabeledStmt)lBodyCopy).getStmt());
               lHirIterator.hasNext();
               ) {
            lnode = lHirIterator.next();
            if (lnode instanceof VarNode
                && ((VarNode)lnode).getVar() == fLoopVar) {
              //// Process in case of Node is
              //// Loop-control-variable.
              // Create Const by calculating 'step * i'
              /*
                             Number lNum =
                (Number) lStepConst.evaluateAsObject();
                             if (lNum instanceof Long)
                lConst =
                symRoot.sym.intConst(
                  lNum.longValue() * i,
                  symRoot.typeLong);
                             else
                lConst =
                symRoot.sym.floatConst(
                  lNum.doubleValue() * i,
                  symRoot.typeDouble);
               */
              // Fix as follows as evaluateAsObject()
              //  is removed. 2005.02.16 S.Noishi
              if (lStepConst.getSymType().isInteger()) {
                lConst =
                  symRoot.sym.intConst(
                  lStepConst.intValue() * i,
                  //##77 symRoot.typeLong
                  typeForArithmeticExp(lStepConst.getSymType()) //##77
                  );
              }
              else {
                lConst =
                  symRoot.sym.floatConst(
                  lStepConst.doubleValue() * i,
                  symRoot.typeDouble);
              }
              // In case of type of Const created is
              // different from Loop-control-variable's,
              // convert it to become identical type.
              lExp = hirRoot.hir.constNode(lConst);
              /* //##77
              if (fLoopVar.getSymType() != lExp.getType()) {
                lExp =
                  hirRoot.hir.convExp(
                  fLoopVar.getSymType(),
                  lExp);
              }
              // Note: Replace VarNode:I to Exp:I + (step*i)
              lExp =
                hirRoot.hir.exp(
                HIR.OP_ADD,
                hirRoot.hir.varNode(fLoopVar),
                lExp);
               */ //##77
              lExp = integralPromotion(HIR.OP_ADD,
                hirRoot.hir.varNode(fLoopVar), lExp); //##77
              //##78 lnode.replaceThisNode(lExp);  //## conflict ?
              replaceExpAdjustingType((Exp)lnode, lExp); //##78
              if (fDbgLevel > 0) //##77
                getDebug().print(4,lMethodName, "Loop var is replaced by "
                  + lExp.toStringWithChildren()); //##77
            }
          }
        }
        // Add The all (of Stmt) of lBodycopy which
        // Modified as n times of expand to lNewBody.
        lStmt =
          ((BlockStmt)((LabeledStmt)lBodyCopy).getStmt())
          .getFirstStmt();
        while (lStmt != null) {
          //// Do not add LabeledNull considering
          //// other optimization 2005.03.17 S.Noishi
          if ((lStmt instanceof LabeledStmt) &&
              ((LabeledStmt)lStmt).getStmt() == null) {
            ; // do not add.
          }
          else {
            lNewBody.addLastStmt(
              (Stmt)lStmt.copyWithOperandsChangingLabels(null));
          }
          lStmt = lStmt.getNextStmt();
        }
      }
      // if do merge assignment, then add (all of Stmt of ) lMerged
      // to the last of lNewBody.
      lStmt = lMerged.getFirstStmt();
      while (lStmt != null) {
        lNewBody.addLastStmt(
          (Stmt)lStmt.copyWithOperandsChangingLabels(null));
        lStmt = lStmt.getNextStmt();
      }
    }
    getDebug().print(4, lMethodName, "New body part is generated");

    //============== 6.5.4 Generating optimized loop  =====================
    //============= Optimize other of loop body part  =====================

    // Check whether parent of this loop is BlockStmt =====================
    //// if loop body is not child of BlockStmt, modify loop body to child.
    if (!(pForStmt.getParent()instanceof BlockStmt)) {
      lStmt = hirRoot.hir.blockStmt((Stmt)pForStmt.copyWithOperands());
      pForStmt.replaceThisNode(lStmt);  //## conflict ?
      pForStmt = (ForStmt)((BlockStmt)lStmt).getFirstStmt();
    }

    // Copy Loop-init-part as previous statement of Loop-body-part.
    // Note: This Loop-body-part is replaced by new LoopStmt later, whose
    //   Loop-init-part is empty.
    // Generate init part before new loop =================================
    if (pForStmt.getLoopInitPart() != null) {
      lStmt = pForStmt.getLoopInitPart();
      // Fix 2005.03.04
      //       lStmt = ((BlockStmt) pForStmt.getLoopInitPart()).getFirstStmt();
      if (lStmt instanceof BlockStmt) {
        lStmt = ((BlockStmt)lStmt).getFirstStmt();
      }

      while (lStmt != null) {
        pForStmt.insertPreviousStmt(
          (Stmt)lStmt.copyWithOperandsChangingLabels(null),
          (BlockStmt)pForStmt.getParent());
        lStmt = lStmt.getNextStmt();
      }
    }

    // Generate temporal variable ========================================
    //// Generate expression which is addition of
    //// lStepExpInBody and lStepExpInStep to lStepExp.
    if (lStepExpInBody != null) {
      if (lStepExpInStep != null) {
        /* //##77
        lExp =
          hirRoot.hir.exp(
          HIR.OP_ADD,
          (Exp)lStepExpInBody.copyWithOperands(),
          (Exp)lStepExpInStep.copyWithOperands());
        */ //##77
        lExp = integralPromotion(HIR.OP_ADD,
          (Exp)lStepExpInBody.copyWithOperands(),
          (Exp)lStepExpInStep.copyWithOperands()); //##77
      }
      else {
        lExp = (Exp)lStepExpInBody.copyWithOperands();
      }
    }
    else {
      lExp = (Exp)lStepExpInStep.copyWithOperands();
    }

    // Generate AssignStmt to temporal variable
    // as previous statement of Loop-body-part
    //    _var0 = (step) * (getExpRate()-1);

    // Step * (getExpRate()-1) --------------------------------------------
    Var lStepTimes7 =
      symRoot.symTableCurrentSubp.generateVar(
      lExp.getType(),
      fLoopVar.getDefinedIn());
    // Generate Const represents 'getExpRate()-1'
    if (lExp.getType().isInteger()) {
      lConst = symRoot.sym.intConst(getExpRate() - 1, lExp.getType());
    }
    else {
      lConst = symRoot.sym.floatConst(getExpRate() - 1, lExp.getType());
    }
    // Note: lExp:step * getExpRate()-1
    // Here, lExp is already a copy.
    lExp =
      //##77 hirRoot.hir.exp(HIR.OP_MULT, lExp, hirRoot.hir.constNode(lConst));
      integralPromotion(HIR.OP_MULT, lExp, hirRoot.hir.constNode(lConst)); //##77
    // Note:  I = step * getExpRate()-1
    //##77 lStmt = hirRoot.hir.assignStmt(hirRoot.hir.varNode(lStepTimes7), lExp);
    lStmt = makeAssignStmt(lStepTimes7, lExp); //##77

    if (fDbgLevel > 0) {
      getDebug().print(4,lMethodName, "Insert " + lStmt.toStringWithChildren()); //##77
    }
    pForStmt.insertPreviousStmt(lStmt, (BlockStmt)pForStmt.getParent());

    // Convert loop start condition =======================================
    //// Generate Copy of condition part to lStartCondcopy
    lStartCondcopy =
      (Exp)pForStmt
      .getLoopStartCondition()
      .copyWithOperandsChangingLabels(null);
    ////
    //// <<< Loop-control-variable is LHS of conditional expression. >>>
    ////  [Original conditional expression (ex.)]
    ///    i < N
    ////
    ////  [HIR representation and it's ChildNumber]
    ////  OP(0)
    ////  i(1)  N(2)
    ////
    ////  [Target of modification]
    ////  getChild(3-1) =N
    ////
    ////  [Conditional expression (after modification)]
    ////  i < N - var0  (var0:(step) * (getExpRate()-1)  )
    ////
    //// <<< Loop-control-variable is RHS of conditional expression. >>>
    ////  [Original conditional expression (ex.)]
    ///    N < i
    ////
    ////  [HIR representation and it's ChildNumber]
    ////  OP(0)
    ////  N(1)  i(2)
    ////
    ////  [Target of modification]
    ////  getChild(3-2) = N
    ////
    ////  [Conditional expression (after modification)]
    ////  N - var0 < i
    ////
    lExp = (Exp)lStartCondcopy.getChild(3 - fChildNumofLoopVarinCond);
    lExp2 = hirRoot.hir.varNode(lStepTimes7);
    /* //##78
    if (lExp.getType() != lExp2.getType()) {
      lExp2 = hirRoot.hir.convExp(lExp.getType(), lExp2);
    }
    lnode =
      hirRoot.hir.exp(HIR.OP_SUB, (Exp)lExp.copyWithOperands(), lExp2);
    lExp.replaceThisNode(lnode);  //## conflict ?
    */ //##78
    //##78 BEGIN
    Type lExpType = lExp.getType();
    Exp lStartCondOperand = integralPromotion(HIR.OP_SUB,
        (Exp)lExp.copyWithOperands(), lExp2);
    lnode = replaceExpAdjustingType((Exp)lExp, lStartCondOperand);
    //##78 END
    getDebug().print(4, lMethodName, "Start condition part is converted to "
      + lnode.toStringWithChildren());

    // Generate new for-loop ==============================================
    //// Modify Loop-step-part.
    //// Note if simple expansion is adapted, then it is not necessary
    //// to do follows, because The all of Loop-step-part is moved.
    ////   Checking this as :
    ////   if (lStepExpInBody == null && !lStepWithVar)
    ////
    lNewStep = null;
    if (lStepExpInBody == null && !lStepWithVar) {
      // Step * getExpRate() --------------------------------------------
      //// Generate temporal variable,
      //// and insert previous of Loop-body-part.
      ////  _var1 = step * getExpRate();
      Var lStepTimes8 =
        symRoot.symTableCurrentSubp.generateVar(
        lStepExpInStep.getType(),
        fLoopVar.getDefinedIn());
      if (lStepExpInStep.getType().isInteger()) {
        lConst =
          symRoot.sym.intConst(
          getExpRate(),
          //##77 lStepExpInStep.getType()
          typeForArithmeticExp(lStepExpInStep.getType()) //##77
          );
      }
      else {
        lConst =
          symRoot.sym.floatConst(
          getExpRate(),
          lStepExpInStep.getType());
      }
      /* //##77
      lExp =
        hirRoot.hir.convExp(
        lStepExpInStep.getType(),
        hirRoot.hir.constNode(lConst));
      lExp =
        hirRoot.hir.exp(
        HIR.OP_MULT,
        (Exp)lStepExpInStep.copyWithOperands(),
        lExp);
      */ //##77
      lExp = integralPromotion(HIR.OP_MULT,
        (Exp)lStepExpInStep.copyWithOperands(),
        hirRoot.hir.constNode(lConst)); //##77
      lStmt =
        //##77 hirRoot.hir.assignStmt(hirRoot.hir.varNode(lStepTimes8), lExp);
        makeAssignStmt(lStepTimes8, lExp); //##77
      pForStmt.insertPreviousStmt(
        lStmt,
        (BlockStmt)pForStmt.getParent());
      // Generate step part ---------------------------------------------
      lExp = hirRoot.hir.varNode(lStepTimes8);
      /* //##77
      if (fLoopVar.getSymType() != lExp.getType()) {
        lExp = hirRoot.hir.convExp(fLoopVar.getSymType(), lExp);
      }
      lExp =
        hirRoot.hir.exp(
        HIR.OP_ADD,
        hirRoot.hir.varNode(fLoopVar),
        lExp);
       */ //##77
      lExp = integralPromotion(HIR.OP_ADD,
        hirRoot.hir.varNode(fLoopVar), lExp); //##77

      //##77 lStmt = hirRoot.hir.assignStmt(hirRoot.hir.varNode(fLoopVar), lExp);
      // Here, lExp is already a copy.
      lStmt = makeAssignStmt(fLoopVar, lExp); //##77
      lNewStep = hirRoot.hir.blockStmt(lStmt);
      if (fDbgLevel > 0) //##77
        getDebug().print(4,lMethodName, "Change step part to "
          + lNewStep.toStringWithChildren()); //##77
    }
    //// Generate new ForStmt from Result of Modification(Optimization).
    lStmt = hirRoot.hir.forStmt(null, lStartCondcopy, lNewBody, lNewStep);

    //==== 6.5.5 Adding statements
    //====      checking regional overlapping from each arrays ====//
    // Do only SIMD environment.  2005.02.23 S.Noishi
    if (isSIMD()) {
      //// Set result of calculation by
      //// count pointed variables in Loop-body-part to lPointerCount.
      // Pick up pointers in loop =======================================
      List lPointerList = new Vector();
      int lPointerCount = 0;
      for (lHirIterator =
           hirRoot.hir.hirIterator(((ForStmt)lStmt).getLoopBodyPart());
           lHirIterator.hasNext();
           ) {
        lnode = lHirIterator.next();
        if (lnode instanceof VarNode) {
          lVar = ((VarNode)lnode).getVar();
          if (lVar.getSymType().getTypeKind() == Type.KIND_POINTER) {
            if (!lPointerList.contains(lVar)) {
              lPointerList.add(lVar);
              lPointerCount++;
            }
          }
        }
      }
      //// Add result of calculation by counting
      //// pointed variables in Loop-step-part to lPointerCount.
      if (((ForStmt)lStmt).getLoopStepPart() != null) {
        for (lHirIterator =
             hirRoot.hir.hirIterator(
          ((ForStmt)lStmt).getLoopStepPart());
             lHirIterator.hasNext();
             ) {
          lnode = lHirIterator.next();
          if (lnode instanceof VarNode) {
            lVar = ((VarNode)lnode).getVar();
            if (lVar.getSymType().getTypeKind()
                == Type.KIND_POINTER) {
              if (!lPointerList.contains(lVar)) {
                lPointerList.add(lVar);
                lPointerCount++;
              }
            }
          }
        }
      }

      //// Do checking regional overlapping
      //// if pointer count is more than two.
      //// if pointer count is 1, then
      //// the following process is not to be executed.
      ////
      //// Check combination of each pointer.
      ////   ex: pointer : pa, pb, pc, pd exist in loop-body-part.
      ////  do checking about:
      ////    (pa,pb) (pa,pc) (pa,pd)
      ////    (pb,pc) (pb,pd)
      ////    (pc,pd)
      ////
      ////  [details]
      ////  Modify Loop-body-part as follows in case of (pa, pb).
      ////  if(pb + getExpRate() <= pa)
      ////    if(pa + getExpRate() <= pb)
      ////    Loop-body-part
      ////  Do this recursively as following pair, like:
      ////   ....
      ////  if(pc + getExpRate() <= pa)
      ////    if(pa + getExpRate() <= pc)
      ////    if(pb + getExpRate() <= pa)
      ////      if(pa + getExpRate() <= pb)
      ////      Loop-body-part
      for (int i = 0; i < lPointerCount; i++) {
        for (int j = i + 1; j < lPointerCount; j++) {
          lVar = (Var)lPointerList.get(i); // Pointer A
          lVar2 = (Var)lPointerList.get(j); // Pointer B

          // make 'if( make A + getExp < B )'
          Exp lPtrAdd = makePointerAddExp(lVar, getExpRate());
          Exp lCmpExp =
            hirRoot.hir.exp(
            HIR.OP_CMP_LE,
            lPtrAdd,
            hirRoot.hir.varNode(lVar2));
          lStmt = hirRoot.hir.ifStmt(lCmpExp, lStmt, null);

          // make 'if( make B + getExp < A )'
          lPtrAdd = makePointerAddExp(lVar2, getExpRate());
          lCmpExp =
            hirRoot.hir.exp(
            HIR.OP_CMP_LE,
            lPtrAdd,
            hirRoot.hir.varNode(lVar));
          lStmt = hirRoot.hir.ifStmt(lCmpExp, lStmt, null);
        }
      }
    }

    //// Insert the expanded loop as the previous of original loop.
    pForStmt.insertPreviousStmt(lStmt, (BlockStmt)pForStmt.getParent());

    //============== 6.5.6 Generate remainder of the loop =======================
    //// Generate loop which exclude Loop-init-part from original loop,
    //// then replace to original loop.
    // Add rest of loop ===================================================
    Stmt lStepCopy = null;
    lStartCondcopy = (Exp)pForStmt.getLoopStartCondition()
       .copyWithOperandsChangingLabels(null);
    lBodyCopy = (Stmt)pForStmt.getLoopBodyPart()
       .copyWithOperandsChangingLabels(null);
    if (pForStmt.getLoopStepPart() != null) {
      lStepCopy = (Stmt)pForStmt.getLoopStepPart()
        .copyWithOperandsChangingLabels(null);
    }
    lStmt = hirRoot.hir.forStmt(null, lStartCondcopy, lBodyCopy, lStepCopy);

    pForStmt.replaceThisStmtWith(lStmt);
    getDebug().print(3, lMethodName, "Loop is Expanded");
  } // expandLoop

  /**
   * Make Exp of 'pPtrVar + pAddValue':
   * ex. Exp of 'p + 3'
   * @param pPtrVar  PointedVar object.
   * @param pAddValue  Increasing/Decreasing integer value.
   * @return Exp of 'pPtrVar + pAddValue'
   */
  private Exp makePointerAddExp(Var pPtrVar, int pAddValue)
  {
    Const lRateConst = symRoot.sym.intConst(pAddValue, symRoot.typeInt);
    Exp lRateConstNode = hirRoot.hir.constNode(lRateConst);
    Exp lConvOffsetExp =
      hirRoot.hir.convExp(symRoot.typeOffset, lRateConstNode);
    long lSizeValue = pPtrVar.getSymType().getSizeValue();
    Const lOffsetConst =
      symRoot.sym.intConst(lSizeValue, symRoot.typeOffset);
    Exp lOffsetConstNode = hirRoot.hir.constNode(lOffsetConst);
    Exp lMultExp =
      hirRoot.hir.exp(HIR.OP_MULT, lOffsetConstNode, lConvOffsetExp);
    Exp lVarNode = hirRoot.hir.varNode(pPtrVar);
    return hirRoot.hir.exp(HIR.OP_ADD, lVarNode, lMultExp);
  } // makePointerAddExp

  /**
   * Generate Exp of step in pBlock
   * it is called to get Exp which represents amount of increase of
   * Loop-control-variable in Loop-step-part.
   * @param  pBlock  BBlock object
   * @return  Exp which represents amount of increase of
   *     Loop-control-variable in Loop-step-part.
   */
  //   protected Exp getStepExp(BlockStmt pBlock) {
  protected Exp getStepExp(Stmt pStmt)
  {
    HirIterator lHirIterator, lHirIterator2;
    HIR lnode;
    Stmt lStmt;
    Exp lExp, lExp2;
    Exp lStepExp, lStepExpCopy;

    lStepExp = null;
//    for (lHirIterator = hirRoot.hir.hirIterator(pBlock);
    for (lHirIterator = hirRoot.hir.hirIterator(pStmt);
         lHirIterator.hasNextStmt(); //##62
         ) {
      lStmt = lHirIterator.getNextStmt();
      if (lStmt instanceof AssignStmt) {
        if (getSimpleExp(((AssignStmt)lStmt).getLeftSide()).getVar()
            == fLoopVar) {
          lStepExpCopy =
            (Exp)((AssignStmt)lStmt)
            .getRightSide()
            .copyWithOperands();
          for (lHirIterator2 = hirRoot.hir.hirIterator(lStepExpCopy);
               lHirIterator2.hasNext();
               ) {
            lnode = lHirIterator2.next();
            if (lnode instanceof VarNode
                && ((VarNode)lnode).getVar() == fLoopVar) {
              if (lStepExp == null) {
                while (lnode.getParent().getOperator()
                  == HIR.OP_CONV) {
                  lnode = (HIR)lnode.getParent();
                }
                lExp = (Exp)lnode.getParent();
                lExp2 =
                  (Exp)((Exp)lExp
                  .getChild(3 - lnode.getChildNumber()))
                  .copyWithOperands();
                if (lExp.getOperator() == HIR.OP_SUB) {
                  //##78 lExp2 = hirRoot.hir.exp(HIR.OP_NEG, lExp2);
                  lExp2 = integralPromotion(HIR.OP_NEG,lExp2); //##78
                }
                if (lExp.getParent() == null) {
                  lStepExpCopy = lExp2;
                }
                else {
                  //##78 lExp.replaceThisNode(lExp2);
                  replaceExpAdjustingType(lExp, lExp2); //##78
                }
              }
              else {
                //##78 lnode.replaceThisNode(lStepExp);
                replaceExpAdjustingType((Exp)lnode, lStepExp); //##78
              }
              lStepExp = lStepExpCopy;
              break;
            }
          }
        }
      }
    }
    if ((fDbgLevel > 0)&&(lStepExp != null)) //##77
      getDebug().print(4, "getStepExp", lStepExp.toStringWithChildren()); //##77
    return lStepExp;
  } // getStepExp

  /**
   * Applies merge-assignment optimization to pStepConst.
   *   'a + i' to
   *   'a + i + ( i + step ) + ( i + step*2 ) + ...'
   *
   * @param   pStmt    Stmt object
   * @param   pStepConst Const object
   *        which represents increase in loop step
   * @return Stmt which is merged assignments.
   *
   */
  protected Stmt mergeAssignStmt(Stmt pStmt, Const pStepConst)
  {
    final String lMethodName = "mergeAssignStmt";
    HirIterator lHirIterator;
    HIR lnode;
    Stmt lStmt;
    Exp lExp, lRightSide, lRightSideCopy;
    // Exp at right side of AssignStmt
    Var lVar;
    Type lLoopVarType, lAddExpType; //##77

    //##77 getDebug().print(4, lMethodName, "Start process");
    lStmt = (Stmt)pStmt.copyWithOperandsChangingLabels(null);
    lVar = getSimpleExp(((AssignStmt)lStmt).getLeftSide()).getVar();
    //##77 BEGIN
    getDebug().print(4, lMethodName, "Start process. Left side var "
      + lVar.getName());
    // Integral promotion.
    lAddExpType = typeForArithmeticExp(fLoopVar.getSymType());
    //##77 END
    lRightSide = null;
    for (int i = 0; i < getExpRate(); i++) {
      lRightSideCopy =
        (Exp)getSimpleExp(((AssignStmt)lStmt).getRightSide())
        .copyWithOperands();
      if (lRightSide != null) {
        //##77 BEGIN
        if (fDbgLevel > 0)
          getDebug().print(4, lMethodName, " right side " +
            lRightSide.toStringWithChildren());
        // Replace the mergeable variable with right side expression
        // by recording the correspondence and change later so that
        // the replacements do not disturb the iterator.
        ArrayList lListOfOld = new ArrayList();
        ArrayList lListOfNew = new ArrayList();
        //##77 END
        for (lHirIterator = hirRoot.hir.hirIterator(lRightSideCopy);
             lHirIterator.hasNext();
             ) {
          lnode = lHirIterator.next();
          if (lnode instanceof VarNode) {
            if (((VarNode)lnode).getVar() == lVar) {
              while (lnode.getParent().getOperator()
                == HIR.OP_CONV) {
                lnode = (HIR)lnode.getParent();
              }
              //##77 lnode.replaceThisNode(lRightSide);
              lListOfOld.add(lnode); //##77
              lListOfNew.add(lRightSide.copyWithOperands()); //##77
            }
            else if (((VarNode)lnode).getVar() == fLoopVar) {
              Const lConst;
              /*
                             Number lNum =
                (Number) pStepConst.evaluateAsObject();
                pStepConst.getType()
                             if (lNum instanceof Long)
                lConst =
                symRoot.sym.intConst(
                  lNum.longValue() * i,
                  symRoot.typeLong);
                             else
                lConst =
                symRoot.sym.floatConst(
                  lNum.doubleValue() * i,
                  symRoot.typeDouble);
               */
              //// Fix follows as removing
              //  evaluateAsObject() 2005.02.16 S.Noishi
              if (pStepConst.getSymType().isInteger()) {
                lConst =
                  symRoot.sym.intConst(
                  pStepConst.intValue() * i,
                  //##77 symRoot.typeLong
                  typeForArithmeticExp(pStepConst.getSymType()) //##77
                  );
              }
              else {
                lConst =
                  symRoot.sym.floatConst(
                  pStepConst.doubleValue() * i,
                  symRoot.typeDouble);
              }

              lExp = hirRoot.hir.constNode(lConst);
              /* //##77
              if (fLoopVar.getSymType() != lExp.getType()) {
                lExp =
                  hirRoot.hir.convExp(
                  fLoopVar.getSymType(),
                  lExp);
              }
              lExp =
                hirRoot.hir.exp(
                HIR.OP_ADD,
                hirRoot.hir.varNode(fLoopVar),
                lExp);
              */ //##77
               lExp = integralPromotion(HIR.OP_ADD,
                hirRoot.hir.varNode(fLoopVar), lExp); //##77
              if (lnode.getParent() == null) {
                lRightSideCopy = lExp;
              }
              else {
                //##77 lnode.replaceThisNode(lExp);
                lListOfOld.add(lnode); //##77
                lListOfNew.add(lExp.copyWithOperands()); //##77
              }
            }
          }
        }
        //##77 BEGIN
        Iterator lIter1 = lListOfNew.iterator();
        for (Iterator lIter2 = lListOfOld.iterator();
             lIter2.hasNext(); ) {
          HIR lOld = (HIR)lIter2.next();
          Exp lNew = (Exp)lIter1.next();
          //##78 lOld.replaceThisNode(lNew);
          replaceExpAdjustingType((Exp)lOld, lNew); //##78
          // REFINE to decrease type conversion.
        }
        //##77 END
      }
      lRightSide = lRightSideCopy;
    }
    /* //##77
    if (lRightSide.getType() != lVar.getSymType()) {
      lRightSide = hirRoot.hir.convExp(lVar.getSymType(), lRightSide);
    }
    lStmt = hirRoot.hir.assignStmt(hirRoot.hir.varNode(lVar), lRightSide);
    */ //##77
    lStmt = makeAssignStmt(lVar, lRightSide); //##77
    if (fDbgLevel > 0) //##77
      getDebug().print(4,lMethodName, "result " + lStmt.toStringWithChildren()); //##77
    return lStmt;
  } // mergeAssignStmt

  /**
   * Check whether hir element is bad for loop expansion,
   * is follows
   *    CallStmt
   *    volatiled sym
   *    and JumpStmt at loop expand optimization.
   * @param pStmt  Stmt object.
   * @return true if Stmt contains CallStmt, false if else.
   */
  protected boolean isBadElement(HIR hirElement)
  {
    final String lMethodName = "isBadElement";

    if (super.isBadElement(hirElement)) {
      return true;
    }

    if (hirElement instanceof JumpStmt) {
      getDebug().print(3, lMethodName,
        "Jump statement is found.");
      return true;
    }

    return false;
  } // isBadElement

  /**
   * This is helper routine for
   *  Add array variable to fBadVarSet to do not work optimization
   *  if it's subscripted var is contained in fBadVarSet.
   *
   * @param pHir  Hir object
   * @param pAssignedVarSet   AssignedVarSet object.
   * @param pBadVarSet    BadVarSet object.
   */
  private void CheckBadSubscript(HIR pHir, Set pAssignedVarSet, Set pBadVarSet)
  {
    // Pick up array variables that has variant index =====================
    for (HirIterator lHirIterator = hirRoot.hir.hirIterator(pHir);
         lHirIterator.hasNext();
         ) {
      HIR lnode = lHirIterator.next();
      if (lnode instanceof SubscriptedExp) {
        Var lVar =
          getSimpleExp(((SubscriptedExp)lnode).getArrayExp())
          .getVar();
        if (lVar != null) {
          for (HirIterator lHirIterator2 =
               hirRoot.hir.hirIterator(
            ((SubscriptedExp)lnode).getSubscriptExp());
               lHirIterator2.hasNext();
               ) {
            HIR lnode2 = lHirIterator2.next();
            if (lnode2 instanceof VarNode) {
              if (pAssignedVarSet
                  .contains(((VarNode)lnode2).getVar()) ||
                  pBadVarSet.contains(((VarNode)lnode2).getVar())) {
                pBadVarSet.add(lVar);
              }
            }
          }
        }
      }
    }
  } // CheckBadSubscript

//##64 BEGIN
  protected int
    computeExpansionFactor( LoopStmt pLoopStmt )
  {
    int lStartNodeIndex, lNextStmtIndex = 0;
    HIR lLoopBody = pLoopStmt.getLoopBodyPart();
    if ((lLoopBody == null)||(lLoopBody.getIndex() == 0)) {
      return 0; // Empty loop. Do not expand.
    }
    lStartNodeIndex = lLoopBody.getIndex();
    HIR lEndCondition = pLoopStmt.getLoopEndCondition();
    if ((lEndCondition != null)&&(lEndCondition.getIndex() > 0)) {
      lNextStmtIndex = lEndCondition.getIndex();
    }else {
      HIR lStepPart = pLoopStmt.getLoopStepPart();
      if ((lStepPart != null)&&(lStepPart.getIndex() > 0)) {
        lNextStmtIndex = lStepPart.getIndex();
      }else {
        HIR lEndPart = pLoopStmt.getLoopEndPart();
        lNextStmtIndex = lEndPart.getIndex();
      }
    }
    int lNodeCount = lNextStmtIndex - lStartNodeIndex;
    if (fDbgLevel > 3)
      ioRoot.dbgOpt1.print(4, "computeExpansionFactor " +
        pLoopStmt.toStringShort() + " node count of loop body " + lNodeCount);
    if (lNodeCount > fNodeCountMultipliedByExpFactor / 2) {
      // The loop is too big.
      return 0;
    }
    int lExpansionFactor = fNodeCountMultipliedByExpFactor / lNodeCount;
    if (lExpansionFactor > fUpperLimitOfExpansionFactor)
      lExpansionFactor = fUpperLimitOfExpansionFactor;

    if (isSIMD()) {
      // If SIMD optimization can be applicable to pLoopStmt,
      // adjust the expansion factor.
    }
    return lExpansionFactor;
  } // computeExpansionFactor
  //##64 END

//##77 BEGIN
protected Exp
integralPromotion( int pOperator, Exp pOperand1, Exp pOperand2 )
{
  Type lType1 = pOperand1.getType();
  Type lType2 = pOperand2.getType();
  Type lExpType = lType1;
  if (lType2.getTypeRank() > lExpType.getTypeRank())
    lExpType = lType2;
  if (lExpType.getTypeRank() < symRoot.typeInt.getTypeRank())
    lExpType = symRoot.typeInt;
  Exp lExp1 = pOperand1;
  if (lExp1.getType() != lExpType)
    lExp1 = hirRoot.hir.convExp(lExpType, (Exp)lExp1.copyWithOperands());
  Exp lExp2 = pOperand2;
  if (lExp2.getType() != lExpType)
    lExp2 = hirRoot.hir.convExp(lExpType, (Exp)lExp2.copyWithOperands());
  return hirRoot.hir.exp(pOperator, lExp1, lExp2);
} // integralPromotion (binary)

protected Exp
integralPromotion( int pOperator, Exp pOperand1 )
{
  Type lExpType = pOperand1.getType();
  if (lExpType.getTypeRank() < symRoot.typeInt.getTypeRank())
    lExpType = symRoot.typeInt;
  Exp lExp1 = pOperand1;
  if (lExp1.getType() != lExpType)
    lExp1 = hirRoot.hir.convExp(lExpType, (Exp)lExp1.copyWithOperands());
  return hirRoot.hir.exp(pOperator, lExp1);
} // integralPromotion (unary)

protected AssignStmt
makeAssignStmt( Var pVariable, Exp pExp )
{
  AssignStmt lStmt;
  Type lType = pVariable.getSymType();
  Exp lExp = pExp;
  if (pExp.getType() != lType)
    lExp = hirRoot.hir.convExp(lType, lExp);
  return hirRoot.hir.assignStmt(
      hirRoot.hir.varNode(pVariable), lExp);
} // makeAssignStmt

protected Type
typeForArithmeticExp( Type pType )
{
  Type lType = pType;
  if (lType.getTypeRank() < symRoot.typeInt.getTypeRank())
    lType = symRoot.typeInt;
  return lType;
} //typeForArithmeticExp
//##77 END
//##78 BEGIN
/**
 * Replace expression lOld by lNew adjusting type to that of lOld.
 * @param lOld old expression to be replaced.
 * @param lNew new expression for replacement.
 * @return replaced expression.
 */
protected HIR
replaceExpAdjustingType( Exp lOld, Exp lNew )
{
  Exp lExp = lNew;
  if (lOld.getType() != lNew.getType()) {
    lExp = hirRoot.hir.convExp(lOld.getType(), lNew);
  }
  Exp lNewExp = (Exp)lOld.replaceThisNode(lExp);
  return lNewExp;
} // replaceExpAdjustingType
//##78 END

} // LoopUnrolling
