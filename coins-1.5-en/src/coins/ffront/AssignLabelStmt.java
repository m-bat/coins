/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

import java.util.Iterator;

/** ASSIGN statement
 *
 */
public class AssignLabelStmt extends FStmt{
  private static FirList assignLabels = null; // list of all ASSIGN statement
  Token fIdent; // i of "ASSIGN 10 TO i". This is accessed from an AssignGoto.
  Token fLabel; // 10 of "ASSIGN 10 TO i". This is accessed from an AssignGoto.
  public AssignLabelStmt(Token pLabel, Token pIdent, int line, FirToHir pfHir){
    super(line, pfHir);
    fLabel = pLabel;  fIdent = pIdent;
  }
  public void print(int level, String spaces){
    super.print(level, spaces);
    fHir.debugPrint(level, spaces+"Assign Label "+fLabel+
                " to "+fIdent+"\n");
  }
  public String toString(){
    return super.toString()+"Assign Label "+fLabel+
                " to "+fIdent;
  }

  /** Add fLabel to list of fIdent in assignLabels and tranlate
   *     ASSIGN 10 TO i
   * to
   *     i = 10;
   */
  public void process(){
    if (assignLabels == null){
      assignLabels = new FirList(fHir);
      assignLabels.addFirst(new Pair(fIdent,
                  (new FirList(fHir)).addedFirst(fLabel), fHir));
    }
    else {
      String   ident = fIdent.getLexem();
      boolean  found = false;
      Iterator it = assignLabels.iterator();
      
      while (it.hasNext()){
        Pair pair = (Pair)it.next();
        Token id = (Token)pair.getLeft();
        if (id.getLexem() == ident){
          ((FirList)pair.getRight()).addFirst(fLabel);
          found = true;
          break;
        }
      }
      if (!found){
        assignLabels.addFirst(new Pair(fIdent,
                  (new FirList(fHir)).addedFirst(fLabel), fHir));
      }
    }
    fLabel.setKind(Parser.INT_CONST);
    stmt = fHirUtil.makeAssignStmt(fIdent.makeExp(), fLabel.makeExp());
    super.process();
  }
}
