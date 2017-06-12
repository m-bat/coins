/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

import coins.sym.Var;

/** RETURN statement
 */
public class ReturnStmt extends FStmt{
  private HeaderStmt headerStmt;
  private Node fOptExpr;
  public ReturnStmt(Node pOptExpr, int line, FirToHir pfHir){
    super(line, pfHir);
    fOptExpr = pOptExpr;
  }
  public void print(int level, String spaces){
    super.print(level, spaces);
    fHir.debugPrint(level, spaces+"ReturnStmt"+"\n");
  }
  public String toString(){
    return super.toString()+"Return statement";
  }

  /** If this is in a subroutine subprogram, translate
   *    RETURN n
   * to
   *    return n;
   * and
   *    RETURN
   * to
   *    return;  or  return 0;
   *  If this is in a function, translate RETURN to
   *    return returnVar;
   *
   */
  public void process(){
    if(headerStmt == null){
      headerStmt = fDeclMgr.getProgramHeader();
    }
    if(headerStmt.isFunction()){
      Var returnVar = headerStmt.getReturnVar();
      stmt = hir.returnStmt(hir.varNode(returnVar));
    }
    else{
      dp("opt : " + fOptExpr);
      if(headerStmt.getStar() == 0){
        stmt = hir.returnStmt();
      }
      else if(fOptExpr == null){
        stmt = hir.returnStmt(fHirUtil.makeConstInt0Node());
      }
      else{
        stmt = hir.returnStmt(fOptExpr.makeExp());
      }
    }
    super.process();
  }
}
