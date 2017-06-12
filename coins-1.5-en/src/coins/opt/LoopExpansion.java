/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.opt;

import java.util.HashSet;
import java.util.Set;
import java.util.Map; //##64
import java.lang.Integer; //##64
import java.lang.Character; //##63

import coins.Debug;
import coins.FlowRoot;
import coins.HirRoot;
import coins.IoRoot;
import coins.MachineParam; //##64
import coins.SymRoot;
import coins.driver.CoinsOptions; //##64
import coins.ir.hir.BlockStmt;
import coins.ir.hir.Exp;
import coins.ir.hir.HIR;
import coins.ir.hir.HirIterator;
import coins.ir.hir.LabeledStmt;
import coins.ir.hir.Stmt;
import coins.ir.hir.SubpDefinition;
import coins.ir.hir.SubscriptedExp;
import coins.ir.hir.VarNode;
import coins.sym.Sym;
import coins.sym.Type;
import coins.sym.Var;

/**
 *
 * <p>title: LoopExpansion class.</p>
 * <p>description: Abstract class for Loop Expansion implement class.</p>
 * @author S.Noishi
 * @version 1.0
 */
public abstract class LoopExpansion
{

  protected HirRoot hirRoot;
  protected FlowRoot flowRoot;
  protected SymRoot symRoot;
  //##64 private IoRoot ioRoot;
  protected IoRoot ioRoot; //##64
  protected Map fOptionMap; //##64
  protected CoinsOptions fOptions; //##64
  protected int fNumberOfGeneralRegisters; //##64
  protected int fMaxAllowableNodesInLoopBody; //##64
  protected int fDbgLevel; //##64

  /**
   * Max statement count in loopbody of which allow expand.
   */
  private int fMaxAllowableStmtsInLoopBody;
  /**
   * Get max statement count in loopbody of which allow expand.
   * @return max statement count in loopbody of which allow expand.
   */
  public int getMaxAllowableStmtsInLoopBody()
  {
    return fMaxAllowableStmtsInLoopBody;
  }

  /**
   * Set max statement count in loopbody of which allow expand.
   * @param pMaxAllowableStmtCount
   *     max statement count in loopbody of which allow expand.
   */
  public void setMaxAllowableStmtsInLoopBody(int pMaxAllowableStmtCount)
  {
    fMaxAllowableStmtsInLoopBody = pMaxAllowableStmtCount;
  }

  /**
   * Construct this object
   * @param phirRoot  HIR root object.
   */
  protected LoopExpansion(HirRoot phirRoot)
  {
    hirRoot = phirRoot;
    symRoot = phirRoot.symRoot;
    ioRoot = phirRoot.ioRoot;
    //##65 flowRoot = new FlowRoot(hirRoot);
    flowRoot = hirRoot.getFlowRoot(); //##65
    flowRoot.setFlowAnalOption(FlowRoot.OPTION_STANDARD_DATA_FLOW, true);
    //##64 BEGIN
    fDbgLevel = ioRoot.dbgOpt1.getLevel();
    fOptions = ioRoot.getCompileSpecification().getCoinsOptions(); //##64
    // System.out.print("\nLoopExpansion CoinsOptions " + lOptions); //###
    String lOptArg = fOptions.getArg("hirOpt");
    // System.out.print("\nhirOpt " + lOptArg); //###
    fOptionMap = fOptions.parseArgument(lOptArg, '/', '.');
    if (fDbgLevel > 0)
      ioRoot.dbgOpt1.print(2, "\n Option map " + fOptionMap);
    fNumberOfGeneralRegisters = ioRoot.machineParam.getNumberOfGeneralRegisters();
    //##64 END

    // Default value of
    // max statement count in loopbody of which allow expand is 6.
    setMaxAllowableStmtsInLoopBody(6);
  }

  /**
   * Do Optimize in subprogram.
   * @param pSubpDef  SubpDefinition to do optimization.
   * @return true if optimized, false if else.
   */
  abstract public boolean doSubprogram(SubpDefinition pSubpDef);

  /**
   * Get Debug this object refer.
   * @return Debug this object refer
   */
  protected Debug getDebug()
  {
    return ioRoot.dbgOpt1;
  }

  /**
   * Get simple Exp for Conv'ed, or Undecay'ed Exp.
   * @param pExp  Exp object.
   * @return  Simple Exp for Conv'ed, or Undecay'ed Exp.
   */
  protected Exp getSimpleExp(Exp pExp)
  {
    while (pExp.getOperator() == HIR.OP_CONV
           || pExp.getOperator() == HIR.OP_UNDECAY)
      pExp = pExp.getExp1();
    return pExp;
  }

  /**
   * Check whether hir element is bad for loop optimizations,
   * is follows at least.
   *    CallStmt
   *    volatiled sym
   * @param pStmt  Stmt object.
   * @return true if Stmt contains CallStmt, false if else.
   */
  protected boolean isBadElement(HIR hirElement)
  {
    final String lMethodName = "isBadElement";
    // Check whether lnode is call statement.
    if (hirElement.getOperator() == HIR.OP_CALL) {
      getDebug().print(3, lMethodName,
        "Call statement is found.");
      return true;
    }
    //##78 BEGIN
    else if (hirElement.getOperator() == HIR.OP_JUMP) {
      getDebug().print(3, lMethodName,
        "Jump statement is found.");
      return true;
    }
    //##78 END
    // Check whether lnode's sym is volatile.
    Sym sym = hirElement.getSym();
    if (sym != null) {
      Type type = sym.getSymType();
      if (type != null) {
        if (type.isVolatile()) {
          getDebug().print(3, lMethodName,
            "Volatile sym is found.");
          return true;
        }
      }
    }
    return false;
  }

  /**
   *  Check whether Stmt contains bad element.
   * @param pStmt  Stmt object.
   * @return  true if Stmt contains CallStmt, false if else.
   */
  protected boolean hasBadElement(Stmt pStmt)
  {
    final String lMethodName = "hasBadElement";
    HirIterator lHirIterator = hirRoot.hir.hirIterator(pStmt);
    while (lHirIterator.hasNext()) {
      HIR lnode = lHirIterator.next();
      if (lnode != null) {
        if (isBadElement(lnode)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Calculate Stmt count in pStmt.
   * but exclude follows:
   *   LabeledStmt
   *   BlockStmt
   *
   * @param pStmt  Stmt object.
   * @return  Stmt count of pStmt has.
   */
  protected int calcStatementCount(Stmt pStmt)
  {
    int cnt = 0;
    HirIterator lHirIterator = hirRoot.hir.hirIterator(pStmt);
    while (lHirIterator.hasNextStmt()) { //##62

      HIR lnode = lHirIterator.getNextStmt();
      if (lnode != null) {

        if (lnode instanceof LabeledStmt) {
          continue;
        }
        if (lnode instanceof BlockStmt) {
          continue;
        }
        cnt++;
      }
    }
    return cnt;
  }

  /**
   * Get array Var SubscriptedExp has.
   * @param Subs  SubscriptedExp object
   * @return  Var that is array var of array.
   */
  protected Var getArrayVar(SubscriptedExp Subs)
  {
    final String lMethodName = "getSubscriptedVar";
    Var lVar = null;
    Exp arrayExp = Subs.getArrayExp();
    if (arrayExp instanceof SubscriptedExp) {
      lVar = getArrayVar((SubscriptedExp)arrayExp);
    }
    Exp simpleExp = getSimpleExp(arrayExp);
    if (simpleExp instanceof Var) {
      lVar = (Var)simpleExp;
    }
    else {
      lVar = null;
    }
    getDebug().print(6, lMethodName, "getArrayVar var:" + lVar);
    return lVar;
  }

  /**
   * Get subscript Vars SubscriptedExp has.
   * which is counted SubscriptedExp
   * that is child of Parent SubscriptedExp.
   *
   * @param Subs   SubscriptedExp object
   * @return Set of Var that is subscript exp of array.
   */
  protected Set getSubscriptVar(SubscriptedExp Subs)
  {
    final String lMethodName = "getSubscriptedVar";
    Set lVarSet = new HashSet();

    Exp arrayExp = Subs.getArrayExp();
    if (arrayExp instanceof SubscriptedExp) {
      lVarSet.addAll(getSubscriptVar((SubscriptedExp)arrayExp));
    }

    HirIterator lIt = hirRoot.hir.hirIterator(Subs.getSubscriptExp());
    while (lIt.hasNext()) {
      HIR lNode = (HIR)lIt.next();
      if (lNode instanceof VarNode) {
        Var lVar = ((VarNode)lNode).getVar();
        if (lVar != null) {
          lVarSet.add(lVar);
        }
      }
    }
    getDebug().print(6, lMethodName, "subcripted vars:" + lVarSet);
    return lVarSet;
  }

}
