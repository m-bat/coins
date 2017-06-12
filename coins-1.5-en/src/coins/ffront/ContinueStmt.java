/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;


/** CONTINUE statement
 */
public class ContinueStmt extends FStmt{
  public ContinueStmt(int line, FirToHir pfHir){
	super(line, pfHir);
  }
  public void print(int level, String spaces){
    super.print(level, spaces);
    fHir.debugPrint(level, spaces+"ContinueStmt\n");
  }
  public String toString(){
    return super.toString()+"CONTINUE statement";
  }

  public void process(){
    stmt = null;
    super.process();
  }
}
