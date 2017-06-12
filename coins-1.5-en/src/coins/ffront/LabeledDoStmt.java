/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

import coins.ir.hir.Exp;
import coins.ir.hir.HIR;
import coins.ir.hir.Stmt;

/** DO statement of the form
 *    DO 10 i = 1, 30
 *    ...
 * 10 ...
 */
public class LabeledDoStmt extends FStmt{
  private Token doLabel; // 10 of "DO 10 i = 1, 30"
  private Quad doSpec;   // i = 1, 30 of "DO 10 i = 1, 30"
  private Exp leftExp;   //
  private boolean stepIsPositive = true;

  public LabeledDoStmt(Token pLabel, Node pDoSpec, int line, FirToHir pfHir){
    super(line, pfHir);
    doLabel = pLabel;
    doSpec = (Quad)pDoSpec;
  }
  public void print(int level, String spaces){
    super.print(level, spaces);
    dp("LabeledDo "+doLabel+"\n");
    doLabel.print(level, spaces+"  ");
    doSpec.print(level, spaces+"  ");
  }
  public String toString(){
    return super.toString()+" LabeledDoStmt statement "+doLabel;
  }

  /** Mostly processed in a FirToHir.
   */
  public void process(){
    super.process();
    // do nothing
  }

  public String getDoLabelString(){
    return doLabel.getLexem();
  }

  /** Make Stmt "i = 1" from "DO 10 i = 1, 30"
   *
   * @return loop init part.
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
   * @return condition Exp.
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
   * @return loop-step part.
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
}
