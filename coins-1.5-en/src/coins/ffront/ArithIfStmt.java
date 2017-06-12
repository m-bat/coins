/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

import coins.ir.hir.Exp;
import coins.ir.hir.HIR;
import coins.ir.hir.Stmt;
import coins.sym.Type;

/** Arithmetic IF statement
 */
public class ArithIfStmt extends FStmt {
  private Node fExp; // e of "IF (e) 10, 20, 30"
  private Token fMinusLab, fZeroLab, fPlusLab;//10, 20, 30 of "IF (e) 10, 20, 30"
  public ArithIfStmt(Node pNode,
                     Token pMinusLab, Token pZeroLab, Token pPlusLab,
                     int line, FirToHir pfHir) {
    
    super(line, pfHir);
    fExp = pNode;
    fMinusLab = pMinusLab;
    fZeroLab  = pZeroLab;
    fPlusLab  = pPlusLab;
  }
  
  public void print(int level, String spaces){
    super.print(level, spaces);
    dp("= ArithIfStmt"+"\n");
    fExp.print     (level, spaces+"  ");
    fMinusLab.print(level, spaces+"  ");
    fZeroLab.print (level, spaces+"  ");
    fPlusLab.print (level, spaces+"  ");
  }
  public String toString(){
    return super.toString()+"ArithIf statement";
  }

  /** Translate
   *   IF (e) 10, 20, 30
   * to
   *   v = e;
   *   if (v<0) goto L_10;
   *   if (v==0) goto L_20;
   *   else goto L_30;
   */
  public void process(){
    Exp lExp   = fExp.makeExp();
    Type lType = lExp.getType();
    Exp constZeroNode = fHirUtil.makeTyped0Node(lType);
    
    if (!(fExp instanceof Token)){
      Exp leftExp = hir.varNode(fESMgr.makeTempVar(lType));
      addGeneratedStmt(fHirUtil.makeAssignStmt(leftExp, lExp)); // v = e;
      lExp = leftExp; // replace lExp by the assigned variable
    }
    
    Stmt gotoStmtMinus = fESMgr.makeGotoStmt(fMinusLab);
    Stmt gotoStmtZero  = fESMgr.makeGotoStmt(fZeroLab);
    Stmt gotoStmtPlus  = fESMgr.makeGotoStmt(fPlusLab);
    Exp  minusExp      = hir.exp(HIR.OP_CMP_LT, lExp, constZeroNode);
    Exp  plusExp       = hir.exp(HIR.OP_CMP_GT, lExp, constZeroNode);
    
    stmt = hir.ifStmt(minusExp, gotoStmtMinus, gotoStmtZero);
    stmt = hir.ifStmt(plusExp, gotoStmtPlus, stmt);
    super.process();
  }
}
