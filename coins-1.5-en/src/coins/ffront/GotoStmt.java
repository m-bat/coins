/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

public class GotoStmt extends FStmt{

  Token fLabel;
  
  public GotoStmt(Token pLabel, int line, FirToHir pfHir){
	super(line, pfHir);
    fLabel = pLabel;
  }
  public void print(int level, String spaces){
    super.print(level, spaces);
    fHir.debugPrint(level, spaces+"Goto "+fLabel+"\n");
  }
  public String toString(){
    return super.toString()+"GOTO statement";
  }
  public void process(){
    // stmt = hir.jumpStmt(fHir.makeLabel("L_"+fLabel.getLexem()));
    stmt = fESMgr.makeGotoStmt(fLabel);
    super.process();
  }
}
