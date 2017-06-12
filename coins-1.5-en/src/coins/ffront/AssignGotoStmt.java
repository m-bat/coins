/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

import java.util.HashSet;
import java.util.Iterator;

import coins.ir.IrList;
import coins.ir.hir.BlockStmt;
import coins.ir.hir.HirSeq;
import coins.sym.Label;

/** assigned GO TO statement
 */
public class AssignGotoStmt extends FStmt{
  private Token fIdent; // i of "GO TO i [(10, 20, ...)]"
  private FirList fLabels; // (10, 20, ...) of "GO TO i [(10, 20, ...)]"
  public AssignGotoStmt(Token pIdent, FirList pLabels, int line, FirToHir pfHir){
    super(line, pfHir);

    fIdent = pIdent;
    fLabels = pLabels;
  }
  public void print(int level, String spaces){
    super.print(level, spaces);
    fHir.debugPrint(level, spaces+"AssignGoto "+fIdent+"\n");
  }
  public String toString(){
    return super.toString()+"AssignGoto statement";
  }

  /** Translate
   *    GO TO i [(10, 20, ...)]
   * to
   *    switch(i){
   *    case 10: goto L_10;
   *    case 20: goto L_20;
   *    ...
   *    }
   */
  public void process(){
    IrList jumpList = hir.irList();
    BlockStmt switchBlock = hir.blockStmt(null);
    if (fLabels == null){
      //make fLabels from assignList of f77Hir
      fLabels = new FirList(fHir);
      FirList assignList = fESMgr.getAssignList();
      Iterator it = assignList.iterator();
      while (it.hasNext()){
        AssignLabelStmt assignLabel = (AssignLabelStmt)it.next();
        if(assignLabel.fIdent.getLexem() == fIdent.getLexem()){
          // if assigned var is the same
          // as the var of this assigned GOTO then add the value to the list
          fLabels.addFirst(assignLabel.fLabel);
        }
      }

    }
    Iterator it = fLabels.iterator();
    java.util.HashSet  dupcheck = new HashSet();
    
    while (it.hasNext()){
      Token labelToken = (Token)it.next();
      String labelString = labelToken.getLexem();

      // duplicate check
      if(dupcheck.contains(labelString)){
        continue;
      }
      else{
        dupcheck.add(labelString);
      }
      
      Label gotoLabel   = fESMgr.makeLabel(labelString);
      Label switchLabel = fESMgr.makeNewLabel();
      HirSeq pair = hir.hirSeq(fHirUtil.makeIntConstNode(labelString),
                               hir.labelNode(switchLabel));
      jumpList.add(pair);
      switchBlock.addLastStmt(hir.labeledStmt(switchLabel, null));
      switchBlock.addLastStmt(hir.jumpStmt(gotoLabel));
    }
    stmt = hir.switchStmt(fIdent.makeExp(), jumpList, null,
                          switchBlock, fESMgr.makeNewLabel());
    super.process();
  }
}


