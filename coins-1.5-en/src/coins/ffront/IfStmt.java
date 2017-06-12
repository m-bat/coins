/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

/** IF statement
 */
public class IfStmt extends FStmt{
  private Node fExp;   // e of "IF (e) s"
  private FStmt fStmt; // s of "IF (e) s"
  public IfStmt(Node pExp, FStmt pStmt, int line, FirToHir pfHir) {
    super(line, pfHir);
    fExp = pExp; fStmt = pStmt;
  }
  public void print(int level, String spaces){
    super.print(level, spaces);
    fHir.debugPrint(level, spaces+"IfStmt"+"\n");
    fExp.print(level, spaces+"  ");
    fStmt.print(level, spaces+"  ");
  }
  public String toString(){
    return super.toString()+"If statement";
  }
  
  /** Translate Fir IF statement to HIR if statement
   *
   */
  public void process(){
    fStmt.preprocess();
    fStmt.process();
    // stmt = fStmt.getResult();

    stmt = fHirUtil.makeIfStmt(fExp.makeExp(), fStmt.getResult(), null);
    
    //Exp lExp = hir.exp(HIR.OP_CMP_NE, fExp.makeExp(), fHirUtil.makeFalseConstNode());
    //if(lExp != null){
    //  stmt = hir.ifStmt(lExp, stmt, null);
    //}
    super.process();
  }
}


