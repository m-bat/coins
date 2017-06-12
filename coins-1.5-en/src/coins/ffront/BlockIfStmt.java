/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

import coins.ir.hir.Exp;
import coins.ir.hir.Stmt;


/** Block IF statement:
 * block_if_part opt_else_if_parts opt_else_part END_IF
 */
public class BlockIfStmt extends FStmt{
  private Pair fIfPart; // block_if_part
  private FirList fOptElseIfs; // list of else_if_part
  private FirList fOptElse; // list of statements of else_part
  public BlockIfStmt(Node pIfPart, FirList pOptElseIfs, FirList pOptElse, int line, FirToHir pfHir){
    super(line, pfHir);
    fIfPart = (Pair)pIfPart;
    fOptElseIfs = pOptElseIfs;
    fOptElse = pOptElse;
  }
  public void print(int level, String spaces){
    super.print(level, spaces);
    fHir.debugPrint(level, spaces+"BlockIfStmt"+"\n");
    fHir.debugPrint(level, spaces+" BlockIfPart"+"\n");
    fIfPart.print(level, spaces+"  ");
    fHir.debugPrint(level, spaces+" OptElseIfParts"+"\n");
    fOptElseIfs.print(level, spaces+"  ");
    fHir.debugPrint(level, spaces+" OptElsePart"+"\n");
    if (fOptElse != null)
      fOptElse.print(level, spaces+"  ");
  }
  public String toString(){
    return super.toString()+"BlockIf statement";
  }

  Stmt fThenPart, fElsePart;

  /** Translate block if statement
   * <pre>
   *   IF (e1) THEN       // fIfPart.left
   *     s1               // fIfPart.right
   *   ELSE IF (e2) THEN  // fOptElseIfs.get(0).left
   *     s2               // fOptElseIfs.get(0).right
   *   ELSE IF (e3) THEN  // fOptElseIfs.get(1).left
   *     s3               // fOptElseIfs.get(1).right
   *   ELSE
   *     s4               // fOptElse
   *   END IF
   * to
   *   if (e1) s1
   *   else { if (e2) s2
   *          else { if (e3) s3
   *                 else s4  }
   *        }
   * </pre>
   */
  public void process(){
    Exp lExp;
    if (fOptElse == null){
      fElsePart = null;
    }
    else {
      fElsePart = fESMgr.processExecStmt(fOptElse);
      fESMgr.setCurrentStmt(this);
    }
    
    if (fOptElseIfs != null){
      for (int i = fOptElseIfs.size()-1; i >= 0; i--){
        Pair elseIfPart = (Pair)fOptElseIfs.get(i);
        if (elseIfPart.right == null){
          fThenPart = null;
        }
        else{
          fThenPart = fESMgr.processExecStmt((FirList)elseIfPart.right);
          fESMgr.setCurrentStmt(this);
        }
        lExp = elseIfPart.left.makeExp();
        if(lExp == null){
          fElsePart = stmt;
        }
        else{
          fElsePart = fHirUtil.makeIfStmt(lExp, fThenPart, fElsePart);
          //hir.ifStmt(lExp, fThenPart, fElsePart);
        }
      }
    }

    fThenPart = fESMgr.processExecStmt((FirList)fIfPart.right);
    fESMgr.setCurrentStmt(this);
    
    Exp hoge = fIfPart.left.makeExp();
    stmt = fHirUtil.makeIfStmt(hoge, fThenPart, fElsePart);
    
    super.process();
  }
}

