/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.mdf;

import coins.sym.Label;
import coins.ir.hir.HIR;
import coins.ir.hir.SwitchStmt;
import coins.ir.hir.BlockStmt;
import coins.ir.hir.Stmt;
import coins.ir.hir.LabeledStmt;
import coins.ir.hir.LabelNode;
import coins.ir.hir.HirList;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * This class treats `SWITCH statement'. In MDF module, the `SWITCH statement'
 * is a kind of special statement because it include the flow information.
 **/
class TreatSwitch extends TreatNode{
  /** The current SWITCH statement **/
  private SwitchStmt node;
  /** The body of the current SWITCH statement **/
  private BlockStmt switchBody;
  /** The map between the label and the case structure **/
  private Hashtable labelMap;
  /** The current executable conditions **/
  private MdfConditions cond;

  /**
   * Constructor:
   * @param e The environment of the MDF module
   * @param stmt The current SWITCH statement
   * @param list The list of the labels which the MDF module is already treated
   * @param c The current conditions
   **/
  TreatSwitch(MdfEnvironment e,MacroFlowGraph g,SwitchStmt stmt,
              LinkedList list,MdfConditions c){
    super(e,g,list,(Stmt)stmt);
    node=stmt;
    cond=c;
    labelMap=new Hashtable();
    switchBody=mfg.subpDef.blockStmt(null); // new body

    //node.print(0);

    HIR body=(HIR)node.getBodyStmt().getChild(1);
    //body.print(0);
    // Get the label from the current SWITCH statement and put the label
    // and the basic block which belongs to the label into the map.
    while(body!=null){
      if(body instanceof LabeledStmt){
        Stmt nextStmt=body.getNextStmt();
        if(nextStmt!=null)
          labelMap.put(((Stmt)body).getLabel(),body.getNextStmt());
      }
      body=body.getNextStmt();
    }
  }

  /**
   * Replace the case structure if it is outside of the current macro task.
   * @param mt The current macro task
   * @param label The target label
   **/
  void replace(MacroTask mt,Label label){
    // The labels of the case structures
    HirList labLists=(HirList)node.getChild(2).getChild(1);
    for(ListIterator labIte=labLists.iterator();labIte.hasNext();){
      HIR child=(HIR)labIte.next();
      Label caseLabel=((LabelNode)child.getChild(2)).getLabel();
      Label newCaseLabel=symTab.generateLabel();

      // Replace the labels
      HIR newLabStmt=mfg.subpDef.labeledStmt(newCaseLabel,
                                             mfg.subpDef.nullStmt());
      switchBody.addLastStmt((Stmt)newLabStmt);

      if(isChange(mt,caseLabel)){
        // Replace the JUMP statement to the JUMP which point to the end
        // of the program.
        HIR body=replaceCase(mt,caseLabel,label);
        switchBody.addLastStmt((Stmt)body);
      }
      else{
        // Not replaced
        HIR body=(HIR)labelMap.get(caseLabel);
        if(body!=null)
          switchBody.addLastStmt((Stmt)body);
      }

      newCaseLabel.setLabelKind(Label.SWITCH_CASE_LABEL);
      newCaseLabel.setOriginHir(node);
      child.setChild(2,mfg.subpDef.labelNode(newCaseLabel));
    }

    // default label
    Label newDefaultLabel=symTab.generateLabel();
    HIR newDefaultLabStmt=mfg.subpDef.labeledStmt(newDefaultLabel,null);
    switchBody.addLastStmt((Stmt)newDefaultLabStmt);

    Label defaultLabel=node.getDefaultLabel();
    if(defaultLabel!=null){
      if(isChange(mt,defaultLabel)){
        HIR body=replaceCase(mt,defaultLabel,label);
        switchBody.addLastStmt((Stmt)body);
      }
      else{
        HIR body=(HIR)labelMap.get(defaultLabel);
        if(body!=null)
          switchBody.addLastStmt((Stmt)body);
      }
    }

    // Make a new default label
    newDefaultLabel.setLabelKind(Label.SWITCH_DEFAULT_LABEL);
    newDefaultLabel.setOriginHir(node);
    node.getChild(2).setChild(2,mfg.subpDef.labelNode(newDefaultLabel));


    // Replace the body of the current SWITCH statement.
    node.setChild(3,switchBody);

    //node.print(0);
  }

  /**
   * Replace the case structure of the current SWITCH statement.
   * And also, the MDF module inserts the conditions into the inserted
   * macro task, the condtions which report to the current dynamic scheduler.
   * @param src The current macro task
   * @param caseLabel The label of the target case structure
   * @param endLabel The label which is at the end of the current SWITCH 
   *                 statement
   * @return A new block statement which include the new case structure
   **/
  private HIR replaceCase(MacroTask src,Label caseLabel,Label endLabel){
    BlockStmt blk=makeJumpBlk(endLabel);

    if(src.succList.size()>1){
      MacroTask dst=mfg.macroTask(caseLabel);
      Stmt assign=cond.finish.finishCond(src,dst);
      blk.addBeforeBranchStmt(assign);
    }
    else{
      Stmt assign=cond.finish.finishCond(src,null);
      blk.addBeforeBranchStmt(assign);
    }

    return(blk);
  }
}
