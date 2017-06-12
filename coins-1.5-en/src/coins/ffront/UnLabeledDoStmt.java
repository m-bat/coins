/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

import coins.ir.hir.Exp;
import coins.ir.hir.HIR;
import coins.ir.hir.Stmt;

public class UnLabeledDoStmt extends FStmt{
  private Quad doSpec;   // i = 1, 30 of "DO 10 i = 1, 30"
  private Exp leftExp;   //
  private Node doTail;
  private boolean stepIsPositive = true;

  public UnLabeledDoStmt(Node pDoSpec, Node pDoTail, int line, FirToHir pfHir){
    super(line, pfHir);
    doSpec = (Quad)pDoSpec;
    doTail = pDoTail;
  }

  public void print(int level, String spaces){
    super.print(level, spaces);
    dp("UnLabeledDo");
    doSpec.print(level, spaces+"  ");
    doTail.print(level, spaces+"  ");
  }

  public String toString(){
    return super.toString()+" UnLabeledDoStmt statement";
  }

  /** Make Stmt "i = 1" from "DO 10 i = 1, 30"
   *
   * @return initiation statement.
   */
  public Stmt makeInitStmt(){
    leftExp = doSpec.left.makeExp();  // i of "DO 10 i = 1, 30"

    Exp initExp;
    initExp = doSpec.right.makeExp();  //  1 of "DO 10 i = 1, 30"
    initExp = fHirUtil.checkAssignExpType(leftExp, initExp);
    stmt = fHirUtil.makeAssignStmt(leftExp, initExp);  // i = 1 of "DO 10 i = 1, 30"
    return stmt;

    //super.process();
    //return getResult();
  }

  /** Make Exp "i <= 30" from "DO 10 i = 1, 30"
   *
   * @return constant expression.
   */
  public Exp makeCondExp(){
    int relOp;
    if (stepIsPositive){
      relOp = HIR.OP_CMP_LE;
    }
    else{
      relOp = HIR.OP_CMP_GE;
    }
    return hir.exp(relOp, leftExp, doSpec.extra.makeExp());
  }

  /** Make Stmt "i = i + 5" from "DO 10 i = 1, 30, 5"
   *
   * @return step part Stmt.
   */
  public Stmt makeStepStmt(){
    Exp stepExp;  Node optStep = doSpec.last;
    if (optStep == null)
      stepExp = fHirUtil.makeConstInt1Node();
    else {
      stepExp = optStep.makeExp();
      if (optStep instanceof UnaryNode){
        if (((UnaryNode)optStep).op == HIR.OP_NEG){
          stepIsPositive = false;
        }
      }
    }
    stepExp = fHirUtil.checkAssignExpType(leftExp, stepExp);

    Exp binaryExp = hir.exp(HIR.OP_ADD, leftExp, stepExp);
    return fHirUtil.makeAssignStmt(leftExp, binaryExp);
  }

  Stmt makeLoopBodyStmt(){
    Stmt bodystmt = fHir.getExecStmtManager().processExecStmt((FirList)doTail);
    fESMgr.setCurrentStmt(this);
    return bodystmt;
  }

  public void process(){
    Stmt init = makeInitStmt();
    Stmt step = makeStepStmt();
    Exp  cond = makeCondExp();
    Stmt body = makeLoopBodyStmt();
    
    stmt = (Stmt)hir.forStmt(init,
                             cond,
                             body,
                             step).copyWithOperands();
    super.process();
  }
}
