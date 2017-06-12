/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.mdf;

import coins.sym.Label;
import coins.ir.hir.HIR;
import coins.ir.hir.IfStmt;
import coins.ir.hir.BlockStmt;
import coins.ir.hir.Stmt;
import coins.ir.hir.LabeledStmt;

import java.util.LinkedList;

/**
 * This class treats `IF statement'. In MDF module, the `IF statement' is
 * a kind of special statement because it include the flow information.
 **/
class TreatIf extends TreatNode{
  /** The current IF statement **/
  private IfStmt node;
  /** The current executable conditions **/
  private MdfConditions cond;

  /**
   * Constructor:
   * @param e The environment of the MDF module
   * @param g The current macro flow graph
   * @param stmt The current IF statement
   * @param list The list of the labels which the MDF module is already treated
   * @param c The current conditions
   **/
  TreatIf(MdfEnvironment e,MacroFlowGraph g,IfStmt stmt,LinkedList list,
          MdfConditions c){
    super(e,g,list,(Stmt)stmt);
    cond=c;
    node=stmt;
  }

  /**
   * Replace the `then part' and `else part' of the current IF statement.
   * @param mt The current macro task
   * @param label The target label
   **/
  void replace(MacroTask mt,Label label){
    if(isChange(mt,node.getThenPart().getLabel()))
      replacePart(mt,label,true);
    if(isChange(mt,node.getElsePart().getLabel()))
      replacePart(mt,label,false);
  }

  /**
   * Replace the `then part' or `else part' into the specified label. 
   * And also, the MDF module inserts the conditions into the inserted
   * macro task, the condtions which report to the current dynamic scheduler.
   * @param src The current macro task
   * @param label The targete label
   * @param isThen True if the module should treat `then part'
   **/
  private void replacePart(MacroTask src,Label label,boolean isThen){
    BlockStmt blk=makeJumpBlk(label);

    LabeledStmt part=null;
    if(isThen)
      part=node.getThenPart();
    else
      part=node.getElsePart();

    if(src.succList.size()>1){
      MacroTask dst=mfg.macroTask(part.getLabel());
      Stmt assign=cond.finish.finishCond(src,dst);
      blk.addBeforeBranchStmt(assign);
    }
    else{
      Stmt assign=cond.finish.finishCond(src,null);
      blk.addBeforeBranchStmt(assign);
    }

    Label newLab=symTab.generateLabel();
    HIR newLabStmt=((Stmt)blk).attachLabel(newLab);

    if(isThen)
      node.replaceThenPart((LabeledStmt)newLabStmt);
    else
      node.replaceElsePart((LabeledStmt)newLabStmt);
  }
}
