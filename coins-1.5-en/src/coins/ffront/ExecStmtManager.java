/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

import java.util.Iterator;
import java.util.LinkedList;

import coins.ir.hir.BlockStmt;
import coins.ir.hir.Exp;
import coins.ir.hir.Stmt;
import coins.sym.Label;
import coins.sym.Type;
import coins.sym.Var;

public class ExecStmtManager extends BaseManager{
  private int tempCount = 0;

  DeclManager fDeclMgr;

  FStmt currentStmt = null;

  ExecStmtManager(FirToHir fth){
    super(fth);
    fDeclMgr = fth.getDeclManager();
  }

  /** Process list of executable statements and make HIR blockStmt.
   * Process nest of DO statements using doNestIn/doNestOut.
   * @param blockBody
   * @return HIR BlockStmt.
   */
  BlockStmt processExecStmt(FirList blockBody){
    BlockStmt blockStmt   = hir.blockStmt(null);
    Iterator  it          = blockBody.iterator();
    FStmt     currentStmt;

    while (it.hasNext()){
      currentStmt = (FStmt)it.next();
      if(currentStmt == null){
        continue;
      }
      this.currentStmt = currentStmt;
      dp("program header: " + fDeclMgr.getProgramHeader());
      dp("# Current Stmt => " + currentStmt + " (line: " + currentStmt.fLine + ")");

      currentStmt.preprocess();
      currentStmt.process();              // make HIR node of currentStmt
      currentStmt.addResultTo(blockStmt); // add the result to blockStmt

      // LabeldDoStmt
      if (currentStmt instanceof LabeledDoStmt){
        blockStmt = doNestIn((LabeledDoStmt)currentStmt, blockStmt);
      }
      // DoEndStmt
      if (isDoEndStmt(currentStmt)){
        blockStmt = doNestOut(currentStmt, blockStmt);
      }
    }
    return blockStmt;
  }

  public FStmt getCurrentStmt(){
    return this.currentStmt;
  }

  public void setCurrentStmt(FStmt pFStmt){
    this.currentStmt = pFStmt;
  }

  public FirList getAssignList(){
    return fHir.f7Hir.assignList;
  }

  public void addStmt(Stmt stmt){
    currentStmt.addGeneratedStmt(stmt);

  }


  /** Inner class for nested DO loop information.
   */
  class DoInf {
    LabeledDoStmt doStmt;
    String    stmtNumber; // label of the last statement of this DO loop
    int       subNumber;  // depth number for nested loops which have the same last statement
    BlockStmt prevBlock;  // the enclosing block

    DoInf(LabeledDoStmt pStmt, BlockStmt block, int sNumber){
      stmtNumber = pStmt.getDoLabelString();
      subNumber  = sNumber;
      doStmt     = pStmt;
      prevBlock  = block;
    }

    /** Translate DO statement to for statement
     *
     * @param block
     * @return for-statement.
     */
    Stmt makeForStmt(BlockStmt block){
      Stmt initStmt = doStmt.makeInitStmt();
      Stmt stepStmt = doStmt.makeStepStmt();
      Exp condition = doStmt.makeCondExp();
      String labelString = stmtNumber + "_" + subNumber;

      Label loopBackLabel = makeLabel(("B_"+labelString));
      Label loopStepLabel = makeLabel(("S_"+labelString));
      Label loopEndLabel  = makeLabel(("E_"+labelString));

      return (Stmt)hir.forStmt(initStmt,
                               loopBackLabel,
                               condition, block,
                               loopStepLabel,
                               stepStmt,
                               loopEndLabel).copyWithOperands();
    }
  }

  private LinkedList doNestStack = new LinkedList();

  /** Push DoInf and return blank blockStmt.
   *
   * @param fStmt DO statement
   * @param block enclosing block
   * @return
   */
  private BlockStmt doNestIn (LabeledDoStmt fStmt, BlockStmt block) {
    if(doNestStack.size() == 0){
      doNestStack.addLast(new DoInf(fStmt, block, 0));
    }
    else{
      DoInf prev = (DoInf)doNestStack.getLast();
      String stmtNumber = fStmt.getDoLabelString();
      if (stmtNumber == prev.stmtNumber){
        doNestStack.addLast(new DoInf(fStmt, block, prev.subNumber+1));
      }
      else{
        doNestStack.addLast(new DoInf(fStmt, block, 0));
      }
    }
    return hir.blockStmt(null);
  }

  private boolean isDoEndStmt(FStmt fStmt){
    if(fStmt.hasNotLabel()){
      return false;
    }
    if(doNestStack.size() == 0){
      return false;
    }
    String fStmtLabel = fStmt.getLabelString();
    DoInf  doInf      = (DoInf)doNestStack.getLast();
    return (fStmtLabel == doInf.stmtNumber);
  }

  /** Pop DoInf, make HIR forStmt, and add to the current block.
   * Repeat this process for all DoInf which have the same last statement.
   * @param fStmt DoEndStmt
   * @param block current block
   * @return
   */
  private BlockStmt doNestOut(FStmt fStmt, BlockStmt block){
    String fStmtLabel = fStmt.getLabelString();
    BlockStmt currentBlock = block;
    BlockStmt prevBlock;
    DoInf doInf;

    do {
      doInf = (DoInf)doNestStack.removeLast();
      prevBlock = doInf.prevBlock;
      prevBlock.addLastStmt(doInf.makeForStmt(currentBlock));
      currentBlock = prevBlock;
    } while(doInf.subNumber != 0);

    return prevBlock;
  }

  public Stmt makeGotoStmt(Token pLabel){
    return hir.jumpStmt(makeLabel(""+Integer.parseInt(pLabel.getLexem())));
  }
  public Label makeLabel(String pString){
    String ln = ("L_"+pString).intern();
    Label label = (Label)fDeclMgr.fSymTable.search(ln);
    if(label == null){
      label = sym.defineLabel(ln);
    }
    dp("makeNewLabel: " + label);
    return label;
  }

  private int labelCount = 0;
  public Label makeNewLabel(String str){
    String label = ("lab_" + str +labelCount++).intern();
    return makeLabel(label);
  }
  public Label makeNewLabel(){
    return makeNewLabel("");
  }
  ////////////////////////////////////////////////////////////////////
  public String getTempName(){
    return ("_temp_" + tempCount++).intern();
  }
  public String getTempName(String name){
    return ("_" + name + "_" + tempCount++).intern();
  }

  public Var makeIntTemp(){
    return fDeclMgr.searchOrAddVar(getTempName("i_temp"), symRoot.typeInt);
  }
  
  public Var makeRealTemp(){
    return fDeclMgr.searchOrAddVar(getTempName("r_temp"), symRoot.typeFloat);
  }

  public Var makeDoubleTemp(){
    return fDeclMgr.searchOrAddVar(getTempName("d_temp"), symRoot.typeDouble);
  }
  
  public Var makeTempVar(Type pType){
    if(pType.isInteger()){
      return makeIntTemp();
    }
    int typeKind = pType.getTypeKind();
    if(typeKind == Type.KIND_FLOAT){
      return makeRealTemp();
    }
    if(typeKind == Type.KIND_DOUBLE){
      return makeDoubleTemp();
    }
    else{
      printMsgFatal("unimplemented type temp\n");
    }
    return null;
  }
}

