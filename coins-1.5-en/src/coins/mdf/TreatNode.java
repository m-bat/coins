/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.mdf;

import coins.sym.SymTable;
import coins.sym.Label;
import coins.ir.hir.HIR;
import coins.ir.hir.HirIterator;
import coins.ir.hir.Stmt;
import coins.ir.hir.LabeledStmt;
import coins.ir.hir.Exp;
import coins.ir.hir.VarNode;
import coins.ir.hir.BlockStmt;
import coins.ir.hir.ReturnStmt;

import java.util.LinkedList;

/**
 * This class treats some statements specially in MDF module.
 **/
abstract class TreatNode{
  protected MdfEnvironment env;
  protected SymTable symTab;
  protected MacroFlowGraph mfg;
  protected LinkedList visited;
  protected Stmt root;

  /**
   * Constructor:
   * @param e The environment of the MDF module
   * @param g The current macro flow graph
   * @param list The list of the labels which the MDF module is already treated
   * @param rootStmt The statement which treat by this class
   **/
  protected TreatNode(MdfEnvironment e,MacroFlowGraph g,LinkedList list,
                      Stmt rootStmt){
    env=e;
    mfg=g;
    symTab=mfg.subpDef.getSubpSym().getSymTable();
    visited=list;
    root=rootStmt;
  }

  /**
   * Replace the return statement into the jump statement.
   * @param label The label of the final return statement
   * @param decLocal The declaration of the local variables
   **/
  protected void replaceReturn(Label label,DeclareLocalVariables decLocal){
    HIR hirFact=mfg.hirRoot.hir;

//    env.output.println("-----------------------------------------");
    for(HirIterator ite=root.hirIterator(root);
        ite.hasNextStmt();) //##72
    {
      Stmt node=(Stmt)ite.getNextStmt();
      if(node instanceof ReturnStmt){
        node.addNextStmt((Stmt)hirFact.jumpStmt(label));
      }
    }

    for(HirIterator ite=root.hirIterator(root);
        ite.hasNextStmt();) //##72
    {
      Stmt node=(Stmt)ite.getNextStmt();
      if(node instanceof ReturnStmt){
        VarNode finishVar=hirFact.varNode(decLocal.allFinishVar);
        Stmt finishAssign=hirFact.assignStmt(finishVar,
                                             hirFact.intConstNode(1));
        node.addNextStmt(finishAssign);

      }
    }

    for(HirIterator ite=root.hirIterator(root);
        ite.hasNextStmt();) //##72
    {
      Stmt node=(Stmt)ite.getNextStmt();
      if(node instanceof ReturnStmt){
        if(decLocal.returnStatus!=null){
          Exp statusExp=(Exp)hirFact.varNode(decLocal.returnStatus);
          Stmt setStatus=hirFact.assignStmt(statusExp,
                                            (Exp)node.getChild(1));
          node.replaceThisStmtWith(setStatus);
        }
        else{
          node.deleteThisStmt();
        }
      }
    }
//    env.output.println("-----------------------------------------\n");
//    root.print(0);
  }

  /**
   * Make a new block structure which include a new JUMP statement.
   * @param label The target label
   * @return A new block structure
   **/
  protected BlockStmt makeJumpBlk(Label label){
    HIR newJump=mfg.subpDef.jumpStmt(label);
    BlockStmt blkStmt=(BlockStmt)mfg.subpDef.blockStmt((Stmt)newJump);

    return(blkStmt);
  }

  /**
   * Get whether the specified macro task has the specified label.
   * @param mt The target macro task
   * @param label The target label
   * @return True if the specified macro task has the specified label
   **/
  protected boolean isChange(MacroTask mt,Label label){
    MacroTask caseTask=mfg.macroTask(label);
    if(caseTask!=mt) return(true);

    HIR child=label.getHirPosition();
    for(HirIterator ite=child.hirIterator(child);ite.hasNext();){
      HIR stmt=ite.next();
      if(stmt!=null && (stmt instanceof LabeledStmt)){
//        env.output.println(stmt.toStringWithChildren());
        Label l=((LabeledStmt)stmt).getLabel();
        if(!visited.contains(l)) visited.add(l);
      }
    }
    return(false);
  }
}
