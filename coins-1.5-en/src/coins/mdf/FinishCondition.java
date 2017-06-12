/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.mdf;

import coins.ir.hir.Stmt;
import coins.ir.hir.HIR;
import coins.ir.hir.VarNode;
import coins.sym.Var;

import java.util.ListIterator;

/**
 * The status when a macro task finish execution.
 * This condition is used by the dynamic scheduler.
 **/
public class FinishCondition extends Conditions{
  /** The HIR factory **/
  private HIR hirFact;

  /**
   * Constructor:
   * @param e The environment of the MDF module
   * @param g The current macro flow graph
   **/
  public FinishCondition(MdfEnvironment e,MacroFlowGraph g){
    super(e,g,(g.bound()+g.controlBranch.size()));
    hirFact=mfg.hirRoot.hir;

    int offset=mfg.bound();

    // Making the conditions which set into the executable conditions when
    // the macro task finishes executing.
    for(ListIterator ite=mfg.listIterator();ite.hasNext();){
      MacroTask mt=(MacroTask)ite.next();

      // Set the bit which represents the termination of the current macro 
      // task.
      vector[mt.taskNumber()][mt.taskNumber()]=1;

      // If the current macro task has a conditional branch.
      if(mt.succList.size()>1){
        for(ListIterator succIte=mt.succList.listIterator();
            succIte.hasNext();){
          MacroTask succ=(MacroTask)succIte.next();
          // Get the unique number of the conditional branch.
          int num=mfg.controlBranch.branchUniqueNum(mt,succ);
          // Set the bit which represents the current macro task has a 
          // conditional branch.
          vector[num+offset][num+offset]=1;
          // When the flow is decided, the conditions are reported to 
          // scheduler.
          vector[num+offset][mt.taskNumber()]=1;
        }
      }
    }
  }

  /**
   * Get the HIR statement which means the conditions when the macro task
   * `src' is finished. If the macro task `dst' is not equal to `null', 
   * that means `src' includes a conditional branch and `dst' is decided as the
   * next macro task.
   * @param src The macro task which is finished
   * @param dst The macro task which is decided as the next macro task
   * @return The HIR statement which means a report the current condition to
   *         the scheduler
   **/
  Stmt finishCond(MacroTask src,MacroTask dst){
    int offset=mfg.bound();
    int number=src.taskNumber();

    // `src' has a conditional branch
    if(dst!=null){
      int bnum=mfg.controlBranch.branchUniqueNum(src,dst);

      if(bnum!=-1) 
        number=offset+bnum;
    }

    VarNode finishVar=hirFact.varNode((Var)mfg.taskSym);
    Stmt finishStmt=hirFact.assignStmt(finishVar,
                                       hirFact.intConstNode(number));

    //env.output.println(finishStmt.toStringWithChildren());
    return(finishStmt);
  }
}
