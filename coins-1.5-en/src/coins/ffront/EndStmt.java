/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

import coins.sym.Var;

/** END statement
 */
public class EndStmt extends FStmt{
  public EndStmt(int line, FirToHir pfHir){
    super(line, pfHir);
  }
  public void print(int level, String spaces){
    super.print(level, spaces);
    fHir.debugPrint(level, spaces+" EndStmt"+"\n");
  }
  public String toString(){
    return super.toString()+" END statement";
  }

  /** If current subprogram is a function generate
   *    return returnVar;
   *  else if current subprogram has alternate returns generate
   *    return 0;
   *  else generate
   *    return;
   */
  public void process(){
  	preprocess();
    HeaderStmt  headerStmt = fDeclMgr.getProgramHeader();
    if (headerStmt.isFunction()){
      Var returnVar = headerStmt.getReturnVar();
      stmt = hir.returnStmt(hir.varNode(returnVar));
    }else{
      if (headerStmt.getStar() == 0)
        stmt = hir.returnStmt();
      else
        stmt = hir.returnStmt(fHirUtil.makeConstInt0Node());
    }
    super.process();
    fHir.getTypeUtility().popSymTable();
  }
}
