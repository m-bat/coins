/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

/** Pause statement
 */
public class PauseStmt extends FStmt{
  public PauseStmt(Node n, int line, FirToHir pfHir){
    super(line, pfHir);
    n_ = n;
  }
  Node n_;
  
  public void print(int level, String spaces){
    super.print(level, spaces);
    fHir.debugPrint(level, spaces+"Pause statement" + "(" + n_ + ")" +"\n");
  }
  public String toString(){
    return super.toString()+"Pause statement" + "(" + n_ + ")";
  }

  public void process(){
    stmt = null;
    super.process();
  }
}
