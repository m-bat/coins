/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

import java.util.Iterator;

import coins.ir.IrList;
import coins.ir.hir.BlockStmt;
import coins.ir.hir.Exp;
import coins.ir.hir.HirSeq;
import coins.ir.hir.Stmt;
import coins.sym.Label;

/** Computed goto statement
 *
 */
public class ComputedGoto extends FStmt{
  private FirList fLabels; // (10, 20, ...) of "GO TO (10, 20, ...) i"
  private Node fExp;  // i of "GO TO (10, 20, ...) i"
  public ComputedGoto(FirList pLabels, Node pExp, int line, FirToHir pfHir){
	super(line, pfHir);;
    fLabels = pLabels; fExp = pExp;
  }
  public ComputedGoto(int line, FirToHir pfHir){ // make dummy object to call makeSwitchStmt
	super(line, pfHir);;
    fLabels = null; fExp = null;
  }
  public void print(int level, String spaces){
    super.print(level, spaces);
    fHir.debugPrint(level, spaces+" ComputedGoto "+fExp+"\n");
  }
  public String toString(){
    return super.toString()+" ComputedGoto statement ";
  }

  /** Translate
   *    GO TO (10, 20, ...) i
   * to
   *    switch (i) {
   *    case 1: goto L_10;
   *    case 2: goto L_20
   *     ...
   *    }
   */
  public void process(){
    stmt = makeSwitchStmt(fExp.makeExp(), fLabels);
    super.process();
  }
  public Stmt makeSwitchStmt(Exp pExp, FirList pLabels){
	fESMgr = fHir.getExecStmtManager();
	
    IrList jumpList = hir.irList();
    BlockStmt switchBlock = hir.blockStmt(null);
    if (pLabels == null){
      fHir.printMsgRecovered("Computed Goto without labels");
      return null;
    }
    int counter;
    Iterator it;
    for (counter = 1, it = pLabels.iterator(); it.hasNext(); ){
        Token labelToken = (Token)it.next();
        Label gotoLabel = fESMgr.makeLabel(labelToken.getLexem());
        Label switchLabel = fESMgr.makeNewLabel();
        HirSeq pair = hir.hirSeq(fHirUtil.makeIntConstNode(counter++),
                      hir.labelNode(switchLabel));
        jumpList.add(pair);
        switchBlock.addLastStmt(hir.labeledStmt(switchLabel, null));
        switchBlock.addLastStmt(hir.jumpStmt(gotoLabel));
    }
    return hir.switchStmt(pExp, jumpList, null,
            switchBlock, fESMgr.makeNewLabel());
  }
}
