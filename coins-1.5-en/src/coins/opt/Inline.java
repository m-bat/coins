/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.opt;

import coins.HirRoot; //##64
import coins.IoRoot; //##64
import coins.SymRoot; //##64
import coins.driver.CoinsOptions; //##64
import coins.ir.IrList; //##64
import coins.ir.hir.BlockStmt; //##64
import coins.ir.hir.Exp; //##64
import coins.ir.hir.FunctionExp; //##64
import coins.ir.hir.HIR; //##64
import coins.ir.hir.HirIterator; //##64
import coins.ir.hir.HirList; //##77
import coins.ir.hir.IfStmt; //##65
import coins.ir.hir.LabeledStmt; //##64
import coins.ir.hir.LoopStmt; //##64
import coins.ir.hir.NullNode; //##64
import coins.ir.hir.Program; //##64
import coins.ir.hir.ReturnStmt; //##70
import coins.ir.hir.Stmt; //##64
import coins.ir.hir.SubpDefinition; //##64
import coins.ir.hir.SubpNode; //##64
import coins.ir.hir.SwitchStmt; //##65
import coins.ir.hir.VarNode; //##64
import coins.sym.Elem;
import coins.sym.IntConst; //##64
import coins.sym.Label;
import coins.sym.Param;
import coins.sym.Subp;
import coins.sym.Sym;
import coins.sym.SymTable;
import coins.sym.Var;
import coins.flow.FlowImpl; //##64

import java.util.HashMap; //##64
import java.util.List; //##70
import java.util.Map; //##64
import java.util.Iterator; //##64
import java.util.Vector; //##64

/**
 * Inline class
 * does inline expansion of small subprograms.
 * @author Nonaka, revised by Aota, revised again by Tan. //##64
 * @version 1.0
 */
public class Inline
{

  protected final HirRoot hirRoot;
  protected final SymRoot symRoot;
  protected final IoRoot io;
  protected final int fDbgLevel; //##64
  protected int fLimitOfExpansionDepth; //##64
  // fLimitOfExpansionDepth: Depth of inline expansion.
  // 2 means to do expansion again after the first expansion.
  // This is effective for recursive subprograms.
  protected int fUpperLimitOfNodeCount; //##64
  // fUpperLimitOfNodeCount: A subprogram is not expanded
  // if its node count exceeds this number.
  protected int VALUESUM = 100;
  // VALUESUM: Size of array containing local variables
  // declared in a subprogram to be inline expanded.
  protected final FlowImpl fFlowImpl; //##64
  private Subp fSubpWithStaticVar; //##64
  protected Map fOptionMap; //##64
  protected CoinsOptions fOptions; //##64

  protected Vector SubpBlackList = new Vector();
  protected List fPragmaInlineList; // List of subprograms
    // specified in #pragma inline subp1 subp2 ... //##70
  protected boolean fWithHirOpt; // true if hirOpt=inline is specified.

  public Inline(HirRoot pHirRoot, SymRoot pSymRoot, IoRoot pIoRoot,
                String pOptionValue
                , List pPragmaInlineList, boolean pWithHirOpt    //##70
                , String pInlineDepth) //##77
  {
    hirRoot = pHirRoot;
    symRoot = pSymRoot;
    io = pIoRoot;
    //##64 BEGIN
    fFlowImpl = (FlowImpl)hirRoot.getFlowRoot().flow;
    fSubpWithStaticVar = null;
    fDbgLevel = io.dbgOpt1.getLevel();
    if ((pOptionValue != "") &&
        (Character.isDigit(pOptionValue.charAt(0)))) {
      // Upper limit of node count is specified. Use it.
      int lDigitCount = 0;
      while ((lDigitCount < pOptionValue.length()) &&
             Character.isDigit(pOptionValue.charAt(lDigitCount))) {
        lDigitCount++;
      }
      String lOptionValue = pOptionValue.substring(0, lDigitCount);
      Integer lValue = Integer.valueOf(lOptionValue);
      fUpperLimitOfNodeCount = lValue.intValue();
    }
    else {
      fUpperLimitOfNodeCount = 100;
    }
    //##77 BEGIN
    // Get the depth of recursive inline expansion depth nn
    //   from inlinedepth.nn
    // where nn is 1 or 2 or 3. Max is 3, default is 1.
    char lInlineDepth = pInlineDepth.charAt(0);
    if (lInlineDepth == '2')
      fLimitOfExpansionDepth = 2;
    else if (lInlineDepth == '3')
      fLimitOfExpansionDepth = 3;
    else
      fLimitOfExpansionDepth = 1;
    //##77 END
    if (fDbgLevel > 0) {
      io.dbgOpt1.print(2, "\ninline: upper limit of node count "
        + fUpperLimitOfNodeCount
        + " recursive inline depth " + fLimitOfExpansionDepth
        + " " + lInlineDepth); //##77
    }
    //##77 fLimitOfExpansionDepth = 2;
    //##64 END
    fPragmaInlineList = pPragmaInlineList; //##70
    fWithHirOpt = pWithHirOpt;  //##70
  } // Inline

  //##64 BEGIN
//---- Do inline expansion for pSubpDef. (added by Aota) -----//
  public boolean
    changeSubp(SubpDefinition pSubpDef)
  {
    boolean lChanged; //##64
    SymTable lSubpSymTable = pSubpDef.getSymTable(); //###
    symRoot.symTableCurrent = lSubpSymTable; //###
    symRoot.symTableCurrentSubp = lSubpSymTable; //###
    if (fDbgLevel > 0) {
      io.dbgOpt1.print(2, " changeSubp "
        + pSubpDef.getSubpSym().getName());
    }
    // Traverse nodes of SubpDefinition and do inline expansion
    // for subprogram to be expanded.
    lChanged = checkCallerSubp_B(pSubpDef, hirRoot, symRoot, io);
    return lChanged; //##64
  } // changeSubp

  //##64 END

//------ checkCallerSubp_B (inline expansion for pattern B -----//
  //##64 protected void checkCallerSubp_B(SubpDefinition lSubpDef,
  protected boolean checkCallerSubp_B(SubpDefinition lSubpDef, HirRoot hirRoot,
    SymRoot symRoot, IoRoot io)
  {
    boolean lChanged = false; //##64
    HirIterator linlineIterator;
    HIR lNode; // Current HIR node.
    //##100 HIR callNodeList[] = new HIR[100];
    HIR callNodeList[] = new HIR[500];  //##100
    int callNodeNum = 1;
    int serchptr;
    int lPass;

    if (fDbgLevel > 0) {
      io.dbgOpt1.print(1, "Inline:checkCallerSubp_pattern_B " +
        lSubpDef.getSubpSym().getName() + "\n");
    }
    for (lPass = 0; lPass < fLimitOfExpansionDepth; lPass++) {
      if (fDbgLevel > 0) {
        io.dbgOpt1.print(2, "Inline: pass = " + lPass + "\n");
      }
      if (callNodeNum == 0) {
        break; // Do nothing if no expansion has been done in the previous cycle.
      }
      callNodeNum = 0;
      for (linlineIterator = hirRoot.hir.hirIterator(lSubpDef.getHirBody());
           linlineIterator.hasNext(); ) {
        lNode = linlineIterator.next();
        if (lNode != null && lNode.getOperator() == HIR.OP_CALL) {
          //-- It is a call node.
          //-- This is not a call by function pointer.
          if (inlineCond(lNode)) {
            //-- To be expanded.
            // Record the node for inline expansion.
            callNodeList[callNodeNum] = lNode;
            callNodeNum++;
            lChanged = true; //##64
            if (fDbgLevel > 2) {
              io.dbgOpt1.print(3, " expand ");
            }
          }
          else {
            // Not to be expanded.
            if (fDbgLevel > 3) {
              io.dbgOpt1.print(4, " not to be expanded ");
            }
          }
        }
      }
      for (serchptr = 0; serchptr < callNodeNum; serchptr++) {
        inlineExpansion(callNodeList[serchptr]);
      }
    } //for
    return lChanged; //##64
  } //checkCallerSubp_B

  /**
   * inlineCond decides whether the subprogram called at pxNode
   * should be expanded or not.
   *  false
   *    number of nodes > fUpperLimitOfNodeCount ||
   *    external subprogram (subprogram body is not given) ||
   *    contained in conditional expression (in IfStmt, LoopStmt)//##65
   *    boolean expression   //##65
   *    contained in switch selection expression //##65
   *    contained in HirList or IrList  //##77
   *    //64 not contained in loop body
   *  true
   *    number of nodes <= fUpperLimitOfNodeCount
   *    //##64 (called in loop body) and
   * @param pxNode node calling a subprogram.
   * @return true if inline expansion is to be done.
   */
  protected boolean inlineCond(HIR pxNode)
  {
    coins.ir.hir.HirIterator lSubpBodyIterator;
    HIR lNode, lloopNode;
    int StmtCount = 0;
    SubpDefinition lSubpDefinition; //##64
    if (fDbgLevel > 2) {
      io.dbgOpt1.print(3, "\n inlineCond " + pxNode.toStringShort());
    }
    SubpNode lSubpNode = null; //##64
    if (pxNode.getChild1().getOperator() == HIR.OP_ADDR) {
      if (pxNode.getChild1().getChild1()instanceof SubpNode) {
        lSubpNode = (SubpNode)pxNode.getChild1().getChild1();
      }
    }
    else if (pxNode.getChild1()instanceof SubpNode) {
      lSubpNode = (SubpNode)pxNode.getChild1();
    }
    if (lSubpNode != null) {
      //---- Get subprogram name and subprogram body ------//
      Subp lSubp = (Subp)lSubpNode.getSymNodeSym();
      if ((! fWithHirOpt)&&
          (! fPragmaInlineList.contains(lSubp))) {
        // Do not expand if neither hirOpt=inline is specified
        // nor #pragma inline lSubp is specified.
        return false;
      }
      Sym lSubpName = ((FunctionExp)pxNode).getFunctionNode().getSymNodeSym();
      Stmt lSubpBody = lSubp.getHirBody();
      if (fDbgLevel > 2) {
        io.dbgOpt1.print(3, " of " + lSubpName);
      }
      lSubpDefinition = lSubp.getSubpDefinition(); //##64
      //---- Check whether the subprogram body is defined or not. -------//
      if (lSubpBody == null) { // Return false if external subprogram.
        return false;
      }
//--------------------------------------------------------------------//
      /* //##64
            //---- Check whether the subprogram is called in loop body or not. ---//
            lloopNode = pxNode;
            // See ancestors up to root.
            while (lloopNode != null) {
              if (lloopNode.getParent() instanceof LoopStmt
                  && lloopNode.getChildNumber() != 4) {
                return false; // called not in loop body.
              }
              lloopNode = (HIR)lloopNode.getParent();
            }
       */
      //##64
//--------------------------------------------------------------------//
      //##64 BEGIN
      //---- Exclude subprogram definition with too many nodes. ----//
      if (lSubpDefinition == null) {
        return false;
      }
      if (lSubpDefinition.getNodeIndexMax() - lSubpDefinition.getNodeIndexMin()
          > fUpperLimitOfNodeCount) {
        if (! fPragmaInlineList.contains(lSubp)) { //##70
          if (fDbgLevel > 2)                             //##102
              io.dbgOpt1.print(3, " Too many nodes." );  //##102
          return false;
        }
        // If the subprogram is specified in #pragma inline,
        // permit expansion even if its complexity exceeds
        // the limit.  //##70
      }
      //##64 END
      //##65 BEGIN
      HIR lCurrNode = pxNode;
      HIR lAncestor = pxNode;
      while ((lAncestor != null)&&
              (lAncestor.getType() != symRoot.typeBool)) {
         lAncestor = (HIR)lCurrNode.getParent();
         if (lAncestor instanceof IfStmt) {
           if (lAncestor.getChild1() == lCurrNode) {
             if (fDbgLevel > 2)                             //##102
               io.dbgOpt1.print(3, " Conditional exp in IF." );  //##102
             return false; } // COnditional expression in IfStmt.
         }else if (lAncestor instanceof LoopStmt) {
           if (((LoopStmt)lAncestor).getLoopStartCondition() == lCurrNode){
             if (fDbgLevel > 2)                             //##102
                 io.dbgOpt1.print(3, " Conditional exp in LOOP." );  //##102
             return false; }
           if (((LoopStmt)lAncestor).getLoopEndCondition() == lCurrNode) {
             if (fDbgLevel > 2)                             //##102
                 io.dbgOpt1.print(3, " Conditional exp in LOOP." );  //##102
             return false; }
         }else if (lAncestor instanceof SwitchStmt) {
           if (((SwitchStmt)lAncestor).getSelectionExp() == lCurrNode) {
             if (fDbgLevel > 2)                             //##102
                 io.dbgOpt1.print(3, " Conditional exp in SWITCH." );  //##102
             return false; }
         //##77 BEGIN
         }else if (lAncestor instanceof HirList) {
        	 if (containsCall(lSubpDefinition)) { //##102
        	   if (fDbgLevel > 2)
        		 io.dbgOpt1.print(3, lSubpName + " contains CALL."); //##102
               return false;
        	 }
        	 if (fDbgLevel > 2)
        	   io.dbgOpt1.print(3, lSubpName + " does not contain CALL."); //##102        	 
         }else if (lAncestor instanceof SubpDefinition) {
             return true;
         //##77 END
         }
         lCurrNode = lAncestor;
      }
      if ((lAncestor != null)&&
          (lAncestor.getType() == symRoot.typeBool)) {
     	if (fDbgLevel > 2)
      	  io.dbgOpt1.print(3, lSubpName + " is contained in BOOL EXP."); //##102        	 
        return false;
      }
      //##65 END
      return true;
    }
    else {
      if (fDbgLevel > 2)
        io.dbgOpt1.print(3, " Not a subpNode."); //##102        	 
      return false; // Not SubpNode
    }
  } // inlineCond

  //##102 BEGIN
  /**
   * containsCall(pNode)
   * returns true if HIR expression pNode contains CALL,
   * otherwise returns false.
   * @param pNode subprogram body of a subprogram.
   * @return true if the subprogram body contains CALL.
   */
  protected boolean containsCall(HIR pNode)
  {
    HIR lHirNode;
    coins.ir.hir.HirIterator lHirIterator;
	for (lHirIterator = hirRoot.hir.hirIterator(pNode);
        lHirIterator.hasNext(); ) {
      lHirNode = (HIR)lHirIterator.next();
      if ((lHirNode != null)&&(lHirNode.getOperator() == HIR.OP_CALL)) {
        return true;
      }
	}
	return false;
  } // containsCall
  //##102 END
  
//==== inlineExpansion ====//
  //##64 protected void inlineExpansion(HIR lNode, HirRoot hirRoot, SymRoot symRoot, IoRoot io)
  /**
   * Expand subprogram called at pCallNode
   * @param pCallNode call node
   */
  protected void inlineExpansion(HIR pCallNode) //##64
  {

    coins.ir.hir.HirIterator lSubpIterator, lcallBlockIterator;
    HIR lStmt, lSubptreeNode;

//---- Make a copy of subprogram body. ----//
    SubpNode lSubpNode = (SubpNode)(pCallNode.getChild1()).getChild1();
    Subp lSubp = (Subp)lSubpNode.getSymNodeSym();
    Stmt lSubpBody = lSubp.getHirBody();
    HIR lSubpcopy = lSubpBody.copyWithOperandsChangingLabels(null);
    //##64 BEGIN
    if (lSubpBody instanceof BlockStmt) {
      // Do not copy SymTable because
      // local symbols are all replaced by temporal variables.
      // If SymTable is copied, temporal variables declared
      // in the subprogram cause error for hir2c translator.
      ((BlockStmt)lSubpcopy).setSymTable(null);
    }
    else if ((lSubpBody instanceof LabeledStmt) &&
             (((LabeledStmt)lSubpBody).getStmt()instanceof BlockStmt)) {
      ((BlockStmt)((LabeledStmt)lSubpcopy).getStmt()).setSymTable(null);
    }
    //##64 END
    // lSubpcopy: copy of subprogram body.
    HIR lNode = pCallNode; //##64
    if (fDbgLevel > 1) {
      io.dbgOpt1.print(2, "inlineExpansion", lSubp.getName() + "\n");
//--------------------------------------------------------------------//

    }
    //##77 Var ltempVarTable[] = new Var[30];
    Var ltempVarTable[] = new Var[100]; //##77
    // ltempVar[i] is the temporal variable
    // representing i-th actual parameter.
    int ParamNum = 1;
    // ParamNum has been changed from 0, 1, 2, 3, ... to 1, 2, 3, ... at 2005/1.
    IrList lSubpFormalParamList = (IrList)lSubp.getParamList();
    // lSubpFormalParamList: Formal parameter list of this subprogram.

    //---- Make a temporal variable for each parameter
    // listed in the formal parameter list and record in ltempVar.
    for (Iterator lFIterator = lSubpFormalParamList.iterator();
         lFIterator.hasNext(); ) {
      Param lFormalParam = (Param)lFIterator.next();
      if (lFormalParam != null) {
        //-- Make a temporal variable corresponding to the parameter.
        ltempVarTable[ParamNum] = hirRoot.symRoot.symTableCurrent.generateVar
          (lFormalParam.getSymType(), lSubp);
        if (fDbgLevel > 2) {
          io.dbgOpt1.print(3, "lFormalParam = " + lFormalParam.getName()
            + " ltempVar[" + ParamNum + "] = "
            + ltempVarTable[ParamNum].getName() + "\n");
        }
        ParamNum++;
      }
    }
//--------------------------------------------------------------------//

    Sym lValueSym[] = new Sym[VALUESUM];
    // lValueSym contains local variables declared in the callee subprogram.
    // (It may be better to use Map instead of array but
    //  the number of temporal variables within the subprogram to be
    //  expanded will be small, and so, it is not recoded to Map.)
    Var ltempValueVar[] = new Var[VALUESUM];
    // ltempValueVar[i] represent the temporal variable
    // corresponding to lValueSym[i].
    int ValueNum = 0;
    int ValueNumber = 0;

    //---- At the 1st pass, change local variables and parameters in lSubpcopy
    //     to corresponding temporal variables. ----//
    for (lSubpIterator = hirRoot.hir.hirIterator(lSubpcopy);
         lSubpIterator.hasNext(); ) {
      //---- Search for VarNode representing local-var/parameter in lSubpcopy.
      lSubptreeNode = lSubpIterator.next();
      if ((lSubptreeNode != null) && (lSubptreeNode instanceof VarNode)) {
        Sym lSym = ((VarNode)lSubptreeNode).getSymNodeSym();
        if (lSym instanceof Param) {
          //---- Formal parameter. ----//
          if (fDbgLevel > 3) {
            io.dbgOpt1.print(4, "\n param ", lSym.getName() +
              " paramIndex=" + ((Param)lSym).getParamIndex() +
              " ltempVar[" + ((Param)lSym).getParamIndex() + "]="
              + ltempVarTable[((Param)lSym).getParamIndex()].getName());
          }
          // Replace with the temporal variable.
          ((VarNode)lSubptreeNode).setSymNodeSym
            (ltempVarTable[((Param)lSym).getParamIndex()]);
        }
        else if (lSym instanceof Var) {
          //---- Variable other than formal parameter. ----//
          // Do not change global variable.
          //##64 if (lSym.getDefinedIn() != null) {
          if (((Var)lSym).getVisibility() == Sym.SYM_PRIVATE) { //##64
            if (!(lSym instanceof Elem)) { // Do not change struct/union element.
              //---- Local variable ----//
              ValueNum = 0;
              while (ValueNum < VALUESUM) {
                if ( //##64 (lValueSym[ValueNum] != null)&&
                  (lSym == lValueSym[ValueNum])) {
                  //---- The variable is already registered.
                  // Replace with the corresponding temporal variable.
                  ((VarNode)lSubptreeNode).setSymNodeSym
                    (ltempValueVar[ValueNum]);
                  break;
                }
                // Unmatched.
                else if (ValueNum >= ValueNumber) {
                  //---- The variable is not recorded. ----//
                  // Register the variable,
                  // generate corresponding temporal variable,
                  // and then replace with it.
                  // In case of static local variable, change to
                  // unique global variable.
                  lValueSym[ValueNum] = lSym;
                  //##64 ltempValueVar[ValueNum] = hirRoot.symRoot.
                  //##64   symTableCurrent.generateVar(lSubptreeNode.getType(), lSubp);
                  //##64 BEGIN
                  Var lTempVar;
                  if (((Var)lSym).getStorageClass() == Var.VAR_STATIC) {
                    lTempVar = getTempForStaticVar((Var)lSym, lSubp);
                  }
                  else {
                    lTempVar = hirRoot.symRoot.
                      symTableCurrent.generateVar(lSubptreeNode.getType(),
                      lSubp);
                  }
                  ltempValueVar[ValueNum] = lTempVar;
                  //##64 END
                  ValueNumber++;
                  ((VarNode)lSubptreeNode).setSymNodeSym
                    (ltempValueVar[ValueNum]);
                  break;
                }
                ValueNum++;
              }
            }
          }
        }
      }
    } // End of node iteration
//--------------------------------------------------------------------//

//---- At the 2nd pass, change return statements in lSubpCopy ----//
// Return statement is replaced by assign-to-returnVariable and jump
// to the exit point of subprogram body.
    Var lReturnVar = null;
    Stmt lAssignReturnStmt = null;
    Stmt lcallStmt = null;
    HIR lReturncopy = null; // Copy of return statement.
    HIR lReturnValueNode = null; //Return-value node of lReturncopy.
    Label lReturnLabel = null; // Exit point label
    Stmt lReturnJumpStmt = null;
    ReturnStmt lReturn; //##70
    boolean lReturnIsLastStmt = true; //##70
    boolean lChangeToReturnVar = false; //##70

    //---- Change return statement to assign statement and jump statement. ----//
    for (lSubpIterator = hirRoot.hir.hirIterator(lSubpcopy);
         lSubpIterator.hasNext(); ) {
      lSubptreeNode = lSubpIterator.next();
      if ((lSubptreeNode != null) &&
          (lSubptreeNode.getOperator() == HIR.OP_RETURN)) {
        //##70 BEGIN
        lReturn = (ReturnStmt)lSubptreeNode;
        /* //##84
        if ((lReturn.getNextStmt() != null)||
            (!(lReturn.getParent() instanceof BlockStmt))||
            (!(lReturn.getParent().getParent() instanceof LabeledStmt))||
            (((Stmt)lReturn.getParent().getParent()).getNextStmt() != null)) {
          lReturnIsLastStmt = false;
        }
        */ //##84
        lReturnIsLastStmt = isLastStmtOfSubp(lReturn); //##84
        //##70 END
        lReturncopy = lSubptreeNode.copyWithOperands(); // Make copy of return statement.
        lReturnValueNode = (HIR)lReturncopy.getChild1(); // return value node.
        if (fDbgLevel > 2) {
          //##84 io.dbgOpt1.print(3, "\nlReturnValueNode " + lReturnValueNode + "\n");
          io.dbgOpt1.print(3, "\nlReturnValueNode " + lReturnValueNode +
            " isLastStmt " + lReturnIsLastStmt + "\n"); //##84
        }
        if ((lReturnLabel == null)&&
           (! lReturnIsLastStmt)) { //##70
          //-- Exit point label is not yet generated. Generate it.
          lReturnLabel = hirRoot.symRoot.symTableCurrent.generateLabel();
          if (fDbgLevel > 2) {
            io.dbgOpt1.print(3, " returnLabel", lReturnLabel.getName());
          }
          lReturnJumpStmt = hirRoot.hir.jumpStmt(lReturnLabel); // ?
        }
        if (lSubp.getReturnValueType() != symRoot.typeVoid) {
          //-- The subprogram returns a value.
          if (lReturnVar == null) {
            //-- Return value variable is not yet created.
            //   Create it as a variable with the return value type.
            lReturnVar = hirRoot.symRoot.symTableCurrent.generateVar
              (lSubp.getReturnValueType(), lSubp);
          }
          //##64 if (lReturnValueNode instanceof NullNode) {
          if (lReturnValueNode == null) { //##64
            //-- Return value is not specified.
            // Set default value 0.
            io.msgRecovered.put("Return value is not specified for " +
              lSubp.getName());
            lReturnValueNode = hirRoot.hir.intConstNode(0);
            //##64 BEGIN
            if (!(lSubp.getReturnValueType()instanceof IntConst)) {
              // Change to the return value type.
              lReturnValueNode = hirRoot.hir.convExp(lSubp.getReturnValueType(),
                (Exp)lReturnValueNode);
            }
            //##64 END
            if (fDbgLevel > 3) {
              io.dbgOpt1.print(4, "changed returnValueNode",
                lReturnValueNode.toStringWithChildren());
            }
          }
          //-- Create assign statement setting the return value to
          //   the return value variable.
          lAssignReturnStmt = hirRoot.hir.assignStmt
            (hirRoot.hir.varNode(lReturnVar),
             (Exp)(lReturnValueNode.copyWithOperands()));
        }

        //-- Get the return statement and the inner-most block containing it.
        Stmt lReturnStmt = lSubptreeNode.getStmtContainingThisNode();
        BlockStmt lReturnBlock = lReturnStmt.getBlockStmt();
        if (!(lReturnStmt.getParent()instanceof BlockStmt)) {
          //-- The parent of the return statement is not a block.
          //   Create a block with assign statement and jump statement.
          if (lReturnStmt instanceof LabeledStmt) {
            lReturnStmt = ((LabeledStmt)lReturnStmt).getStmt();
          }
          // Create an empty block.
          lReturnBlock = hirRoot.hir.blockStmt(null);
          if (lSubp.getReturnValueType() != symRoot.typeVoid) {
            // Add a statement assigning value to the return-value-variable.
            lReturnBlock.addFirstStmt
              ((Stmt)lAssignReturnStmt.copyWithOperands());
          }
          if (lReturnLabel != null) {
            // Add jump statement jumping to the exit-point.
            lReturnBlock.addLastStmt
              ((Stmt)lReturnJumpStmt.copyWithOperands());
          }
          if (fDbgLevel > 3) {
            io.dbgOpt1.print(4, " Changed returnStmt",
              lReturnBlock.toStringWithChildren());
          }
          lReturnStmt.replaceThisStmtWith(lReturnBlock);
          lChangeToReturnVar = true;
        }
        else {
          //-- The parent of the return statement is a block statement.
          if (fDbgLevel > 2) {
            io.dbgOpt1.print(3, "Unnecessary to make Block for return stmt\n");
            // Add a statement assigning value to the return-value-variable.
          }
          if (lSubp.getReturnValueType() != symRoot.typeVoid) {
            lReturnStmt.insertPreviousStmt
              ((Stmt)lAssignReturnStmt.copyWithOperands());
            lChangeToReturnVar = true;
            if (fDbgLevel > 3) {
              io.dbgOpt1.print(4, " Insert ",
                lAssignReturnStmt.toStringWithChildren());
            }
          }
          if (lReturnLabel != null) {
            // Add jump statement jumping to the exit-point.
            lReturnStmt.replaceThisStmtWith
              ((Stmt)(lReturnJumpStmt.copyWithOperands()));
            lChangeToReturnVar = true;
            if (fDbgLevel > 3) {
              io.dbgOpt1.print(4, " Chang returnStmt to",
                lReturnJumpStmt.toStringWithChildren());
            }
            //lReturnStmt.addNextStmt(lReturnJumpStmt);
          //##70 BEGIN
          }else {
            /*
            // Replace the return statement with
            //   lAssignReturnStmt.
            lReturnStmt.replaceThisStmtWith
              ((Stmt)(lAssignReturnStmt.copyWithOperands()));
            lChangeToReturnVar = true;
            if (fDbgLevel > 3) {
              io.dbgOpt1.print(4, " Chang returnStmt to",
                lAssignReturnStmt.toStringWithChildren());
            }
          */
           lReturnStmt.deleteThisStmt();
          //##70 END
          }
          //lReturnStmt.deleteThisStmt();
        }
      }
    } // End of process for return and jump
//--------------------------------------------------------------------//
    //---- Enclose the expanded part by a block statement ----//
    //     if it is not already a block.
    // Get statement containing the call and
    // get the block containing the statement (that contains the call).
    // The call node is not yet replaced by the expanded code.
    Stmt lCallOldStmt = pCallNode.getStmtContainingThisNode();
    BlockStmt lCallBlock = lCallOldStmt.getBlockStmt();
    lNode = pCallNode;
    // lNode is pCallNode if lCallOldStmt (Stmt containing pCallNode)
    // is directly included in BlockStmt.
    // lNode is the call node corresponding to pCallNode in BlockStmt
    // containing the copy of lCallOldStmt.

    if (!(lCallOldStmt.getParent()instanceof BlockStmt)) {
      //---- The parent of the call node is not a block. ----//
      // If LabeledStmt, get its statement body.
      if (lCallOldStmt instanceof LabeledStmt) {
        lCallOldStmt = ((LabeledStmt)lCallOldStmt).getStmt();
      }
      //-- Enclose the copy of the statement by a block. ----//
      //   (here, the statement contains the call)
      // Set mark to pCallNode so that the mark is transfered to
      // the copied node and can be find later easily.
      pCallNode.setWork(pCallNode); //##64
      if (fDbgLevel > 2) {
        io.dbgOpt1.print(3, " setWork to " + pCallNode.toString()
          + " and make BlockStmt ");
      }
      lCallBlock = hirRoot.hir.blockStmt(null);
      lcallStmt = (Stmt)lCallOldStmt.copyWithOperandsChangingLabels(null);
      lCallBlock.addLastStmt(lcallStmt);
      // Get the parent and child number of the expanded call node.
      HIR lcallStmtParent = (HIR)lCallOldStmt.getParent();
      //-- Replace the containing statement by the block statement
      //   that encloses the copy of the call node.
      //### lcalloldStmt.cutParentLink();
      //### lcallStmtParent.setChild(lcallStmtChildNumber, lcallBlock);
      lCallOldStmt.replaceThisStmtWith(lCallBlock); //###
      //##64 BEGIN
      for (lcallBlockIterator = hirRoot.hir.hirIterator((HIR)lcallStmt);
           lcallBlockIterator.hasNext(); ) {
        lNode = lcallBlockIterator.next();
        if (lNode != null && lNode.getOperator() == HIR.OP_CALL) {
          if (lNode.getWork() == pCallNode) {
            break; // Found
          }
        }
      }
      if ((lNode != null) && (lNode.getWork() == pCallNode)) {
        lNode.setWork(null); // Reset the work.
        if (fDbgLevel > 2) {
          io.dbgOpt1.print(3, " Call statement was found ",
            lNode.toString());
        }
      }
      else {
        // Not found. Error.
        if (fDbgLevel > 1) {
          io.dbgOpt1.print(2, " Call statement was not found ",
            "Error for " + pCallNode.toString());
        }
      }
      //##64 END
    }
    else {
      // Parent of lCallOldStmt is BlockStmt.
      if (fDbgLevel > 3) {
        io.dbgOpt1.print(4, " Unnecessary to make block for callStmt ",
          lCallOldStmt.toString());
      }
      lcallStmt = lCallOldStmt;
    }
//--------------------------------------------------------------------//
    //---- Set up temporal variables corresponding to actual parameters. ----//
    // Get the actual parameter list.
    IrList lSubpActualParamList = (IrList)lNode.getChild2();
    ParamNum = 1;
    // Make assign statements that set actual parameter to
    // corresponding temporal variable, and then insert the
    // assign statements in front of the statement containing
    // the call node.
    for (Iterator lAIterator = lSubpActualParamList.iterator();
         lAIterator.hasNext(); ) {
      HIR lActualParam = (HIR)lAIterator.next();
      if (lActualParam != null) {
        //##91 BEGIN
        Var lTempVar;
        lTempVar = ltempVarTable[ParamNum];
        if (lTempVar == null) {
          // It may be the case of variable number of arguments.
          lTempVar = symRoot.symTableCurrent.generateVar
                     (lActualParam.getType(), lSubp);
        }
        //##91 END
        // Make assign statement.
        Stmt lAssignParamStmt = hirRoot.hir.assignStmt
          //##91 (hirRoot.hir.varNode(ltempVarTable[ParamNum]), (Exp)lActualParam);
          (hirRoot.hir.varNode(lTempVar), (Exp)lActualParam); //##91
        // Insert the assign statement.
        // lcallStmt is Stmt containing the call node (pCallNode)
        // or Stmt containing the copy of the call node.
        lcallStmt.insertPreviousStmt(lAssignParamStmt, lCallBlock);
        ParamNum++;
      }
    } // End of parameter list loop

//----------------------------------------------------------------------------//

    // Make a block statement containing lSubpcopy
    // and add a statement attached with the exit label to which the
    // jump statement (corresponding to return) jumps.
    BlockStmt lSubpcopyBlock =
      hirRoot.hir.blockStmt((Stmt)lSubpcopy); //##64
    if (lReturnLabel != null) {
      if (fDbgLevel > 2) //##67
        io.dbgOpt1.print(3,
          "\n addReturnLabel " + lReturnLabel.getName()); //###
      lSubpcopyBlock.addNextStmt(hirRoot.hir.labeledStmt(lReturnLabel, null));
    }

    // Insert the block statement in front of the statement containing the call.
    lcallStmt.insertPreviousStmt(lSubpcopyBlock, lCallBlock);

    // Replace the call node by the temporal variable representing
    // the return value (if the subprogram returns a value).
    //##70 if ((lSubp.getReturnValueType() != symRoot.typeVoid) &&
    //##70     (lReturnLabel != null))
    //##78 if (lChangeToReturnVar) //##70
    if (lChangeToReturnVar&&(lReturnVar != null)) //##78
    {
      lNode.replaceThisNode(hirRoot.hir.varNode(lReturnVar).copyWithOperands());
    }
    else {
      // The subprogram does not return a value.
      // Delete the call statement.
      lcallStmt.deleteThisStmt();
    }
    //---- Replace local static variables in the subprogram definition ----//
    //     if it is not yet replaced.
    if (fSubpWithStaticVar != null) {
      // This is the 1st time expansion of lSubp.
      // Replace local static variables by corresponding temporal variables.
      replaceLocalStaticVariables(lSubp.getSubpDefinition());
      fSubpWithStaticVar = null; // Reset the indication.
    }

  } // inlineExpansion

  //##64 BEGIN
  /**
   * getTempForStaticVar get the temporal variable corresponding to
   * the static variable lStaticVar.
   * For a local static variable declared in a subprogram, a global
   * static temporal variable is generated and registered to a map showing the
   * correspondence with the static variable. If the correspondence is
   * already registered, then the registered temporal variable is returned.
   * If the map is not yet prepared, then create the map.
   * The map for a subprogram is created only once for the entire
   * compile unit. When the map is created, fSubpWithStaticVar is set
   * to pSubp in order to invoke renaming of local static variables
   * in the body of pSubp.
   * @param lStaticVar static variable locally declared.
   * @param pSubp subprogram to be expanded inline.
   * @return the temporal variable corresponding to the static variable lStaticVar.
   */
  Var getTempForStaticVar(Var pStaticVar, Subp pSubp)
  {
    Map lStaticVarMap;
    Var lTempVar;
    if (fDbgLevel > 3) {
      io.dbgOpt1.print(4, "getTempForStaticVar", pStaticVar.getName()
        + " in " + pSubp.getName());
    }
    if (fFlowImpl.staticVariableMapOfSubp == null) {
      fFlowImpl.staticVariableMapOfSubp = new HashMap();
      if (fDbgLevel > 1) {
        io.dbgOpt1.print(2, "\n create staticVariableMapOfSubp ");
      }
    }
    if (fFlowImpl.staticVariableMapOfSubp.containsKey(pSubp)) {
      lStaticVarMap = (Map)fFlowImpl.staticVariableMapOfSubp.get(pSubp);
    }
    else {
      lStaticVarMap = new HashMap();
      fFlowImpl.staticVariableMapOfSubp.put(pSubp, lStaticVarMap);
      if (fDbgLevel > 1) {
        io.dbgOpt1.print(2, "\n create staticVariableMap for "
          + pSubp.getName());
      }
    }
    if (lStaticVarMap.containsKey(pStaticVar)) {
      lTempVar = (Var)lStaticVarMap.get(pStaticVar);
    }
    else {
      SymTable lSymTableRoot = symRoot.symTableRoot;
      SymTable lSymTableCurrent = pSubp.getSymTable();
      // Generate unique name to be used as temporal variable.
      String lUniqueName = lSymTableCurrent.generateUniqueName(pStaticVar,
        pSubp);
      lTempVar = (Var)lSymTableRoot.defineUnique(lUniqueName, Sym.KIND_VAR, null);
      lTempVar.setSymType(pStaticVar.getSymType());
      lTempVar.setStorageClass(Var.VAR_STATIC);
      lTempVar.setVisibility(Sym.SYM_COMPILE_UNIT);
      if (pStaticVar.getInitialValue() != null) {
        lTempVar.setInitialValue(pStaticVar.getInitialValue());
        // lTempVar is not visible from other compile unit.
        // In other compile unit, pSubp is not inline expanded
        // (because it is external subprogram in other compile unit)
        // and call pSubp in other compile unit uses this
        // SubpDefinition. Thus, this renaming causes no error.
      }
      lStaticVarMap.put(pStaticVar, lTempVar);
      fSubpWithStaticVar = pSubp;
    }
    if (fDbgLevel > 2) {
      io.dbgOpt1.print(3, "static var " + pSubp.getName() +
        " temp " + lTempVar.getName());
      if (fDbgLevel >= 4) {
        io.dbgOpt1.print(4, "staticVarMap of " + pSubp.getName() +
          " " + lStaticVarMap.toString());
      }
    }
    return lTempVar;
  } // getTempForStaticVar

  /**
   * replaceLocalStaticVariables replaces static variables declared locally
   * in pSubpDef by temporal variable so that local static variables
   * are treated properly in both cases where the subprogram is
   * expanded and not expanded.
   * Local static variables are replaced by temporal variable declared
   * as static global and not visible from other compile unit.
   * @param pSubpDef definition of subprogram to be inline expanded.
   */
  protected void
    replaceLocalStaticVariables(SubpDefinition pSubpDef)
  {
    Subp lSubp = pSubpDef.getSubpSym();
    if (fDbgLevel > 0) {
      io.dbgOpt1.print(2, "replaceLocalStaticVariables", lSubp.getName());
    }
    HIR lInitPart = pSubpDef.getInitiationPart();
    replaceLocalStaticVariablesInSubtree(lInitPart, lSubp);
    HIR lSubpBody = pSubpDef.getHirBody();
    replaceLocalStaticVariablesInSubtree(lSubpBody, lSubp);
  } // replaceLocalStaticVariables

  protected void
    replaceLocalStaticVariablesInSubtree(HIR pHir, Subp pSubp)
  {
    if (pHir == null) {
      return;
    }
    if (fDbgLevel > 0) {
      io.dbgOpt1.print(2, "replaceLocalStaticVariablesInSubtree",
        pHir.toString());
    }
    for (HirIterator lHirIterator = hirRoot.hir.hirIterator(pHir);
         lHirIterator.hasNext(); ) {
      //---- Search for VarNode representing local static var in lSubpBody.
      HIR lSubptreeNode = lHirIterator.next();
      if ((lSubptreeNode != null) && (lSubptreeNode instanceof VarNode)) {
        Var lVar = (Var)((VarNode)lSubptreeNode).getSymNodeSym();
        if (lVar.getStorageClass() == Var.VAR_STATIC) {
          //##64 if (lVar.getDefinedIn() != null) { // Do not change global variable.
          if (lVar.getVisibility() == Sym.SYM_PRIVATE) { //##64
            if (!(lVar instanceof Elem)) { // Do not change struct/union element.
              Var lTempVar = getTempForStaticVar((Var)lVar, pSubp);
              ((VarNode)lSubptreeNode).setSymNodeSym(lTempVar);
              if (fDbgLevel > 2) {
                io.dbgOpt1.print(3, "replace " + lVar.getName(),
                  "by " + lTempVar.getName());
              }
            }
          }
        }
      }
    } // End of node iteration
  } // replaceLocalStaticVariables

  //##64 END

  //##84 BEGIN
  protected boolean
  isLastStmtOfSubp( Stmt pStmt )
  {
    boolean lIsLastStmt = false;
    Stmt lStmt = pStmt;
    HIR lParent = (HIR)pStmt.getParent();
    while ((lStmt instanceof Stmt)&&
           (lStmt.getNextStmt() == null)&&
           (lParent instanceof Stmt)&&
           (!(lParent instanceof SubpDefinition))) {
      lStmt = (Stmt)lParent;
      lParent = (HIR)lStmt.getParent();
      if (io.dbgOpt1.getLevel() > 0)
        io.dbgOpt1.print(4, "Ancestor of " + pStmt.toStringShort()
                   + " : " + lStmt);
    }
    if (lParent instanceof SubpDefinition) {
      lIsLastStmt = true;
    }else if ((!(lParent instanceof Stmt))&&
              (lParent != null)) { // Parent may be null
              // because subpBody is copied in inlineExpansion.
      io.msgRecovered.put("Parent of " + lStmt.toStringShort()
        + " is not a statement but " + lParent +
        " in inline expansion.");
    }
    return lIsLastStmt;
  } // isLastStmtOfSubp
  //##84 END

} //Inline
