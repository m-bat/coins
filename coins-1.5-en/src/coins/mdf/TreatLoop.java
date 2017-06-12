/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.mdf;

import coins.sym.Label;
import coins.ir.hir.HIR;
import coins.ir.hir.LoopStmt;
import coins.ir.hir.BlockStmt;
import coins.ir.hir.JumpStmt;
import coins.ir.hir.IfStmt;
import coins.ir.hir.ReturnStmt;
import coins.ir.hir.SwitchStmt;
import coins.ir.hir.LoopStmt;
import coins.ir.hir.Stmt;
import coins.ir.hir.LabeledStmt;

import java.util.LinkedList;
import java.util.Stack;

/**
 * This class treats `LOOP statement'. In MDF module, the `LOOP statement' is
 * a kind of special statement because it include the flow information.
 **/
class TreatLoop extends TreatNode{
  /** The current LOOP statement **/
  private LoopStmt node;
  /** The current executable conditions **/
  private MdfConditions cond;

  /** 
   * Constructor:
   * @param e The environment of the current MDF module
   * @param g The current macro flow graph
   * @param stmt The current LOOP statement
   * @param list The list of the labels which the MDF module is already treated
   * @param c The current conditions
   **/
  TreatLoop(MdfEnvironment e,MacroFlowGraph g,LoopStmt stmt,LinkedList list,
            MdfConditions c){
    super(e,g,list,(Stmt)stmt);
    cond=c;
    node=stmt;
  }

  /** 
   * Constructor:
   * @param e The environment of the current MDF module
   * @param g The current macro flow graph
   * @param stmt The current LOOP statement
   * @param list The list of the labels which the MDF module is already treated
   **/
  TreatLoop(MdfEnvironment e,MacroFlowGraph g,LoopStmt stmt,LinkedList list){
    super(e,g,list,(Stmt)stmt);
    cond=null;
    node=stmt;
  }

  /**
   * Replace the basic block if it is outside of the current loop.
   * @param mt The current macro task
   * @param label The target label
   **/
  void replace(MacroTask mt,Label label){
    LinkedList labelsInLoop=getLabelsInLoop(node,new LinkedList());
    LinkedList check=new LinkedList();

    Stmt stmt=node.getLoopBodyPart();
    //env.output.println(stmt.toStringWithChildren());

    Stack stack=new Stack();
    stack.push(stmt);
    check.add(stmt);

    while(!stack.empty()){
      stmt=(Stmt)stack.pop();

      if(stmt instanceof JumpStmt){
        //env.output.println("FIND : "+stmt.toStringDetail());
        JumpStmt jump=(JumpStmt)stmt;
        Label lab=jump.getLabel();

        if(label!=lab && 
           mt.taskNumber()!=mfg.macroTask(lab).taskNumber()){
          jump.changeJumpLabel(label);
          //env.output.println("JUMP TO OUT SIDE");

          if(mt.succList.size()==1){
            Stmt assign=cond.finish.finishCond(mt,null);
            Stmt inserted=jump.insertPreviousStmt(assign);
            //env.output.println(inserted.getNextStmt().toStringDetail());
          }
          else{
            MacroTask dst=mfg.macroTask(lab);
            Stmt assign=cond.finish.finishCond(mt,dst);
            jump.insertPreviousStmt(assign);
          }
        }
      }
      else if(stmt instanceof ReturnStmt){
        env.output.println("find");
      }
      else{
        for(int i=1;i<=stmt.getChildCount();i++){
          HIR child=(HIR)stmt.getChild(i);
          if(child!=null && child instanceof Stmt && !check.contains(child)){
            check.add(child);
            stack.push(child);
            //env.output.println("+ "+child);
          }
        }

        while(stmt.getNextStmt()!=null){
          stmt=stmt.getNextStmt();
          //env.output.println(stmt);
          if(!check.contains(stmt)){
            check.add(stmt);
            stack.push(stmt);
            //env.output.println("- "+stmt);
          }
        }
      }
    }
  }

  /**
   * Get the list of the labels which are included in the current LOOP
   * statement.
   * @param child The body of the current LOOP statement
   * @param list The list of the labels
   * @return The list of the labels
   **/
  LinkedList getLabelsInLoop(HIR child,LinkedList list){
    if(child==null) return(list);
    else if(child instanceof LabeledStmt) list.add(child);

    for(int i=1;i<=child.getChildCount();i++){
      list=getLabelsInLoop((HIR)child.getChild(i),list);
    }

    list=getLabelsInLoop((HIR)child.getNextStmt(),list);

    return(list);
  }
}
