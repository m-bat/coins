/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

import java.util.Iterator;

import coins.ir.hir.BlockStmt;
import coins.ir.hir.Exp;
import coins.ir.hir.HIR;
import coins.ir.hir.Stmt;
import coins.sym.Label;
import coins.sym.SymIterator;
import coins.sym.SymTable;

/** Super class for all FORTRAN statements
 *
 */
public class FStmt implements Node{
  FirToHir fHir;
  int fLine;

  HIR hir;

  Token   defLabel       = null;
  Stmt    stmt           = null;
  FirList generatedStmts = null;

  HirUtility      fHirUtil;
  TypeUtility     fTypeUtil;
  DeclManager     fDeclMgr;
  ExecStmtManager fESMgr;

  public FStmt(int line, FirToHir pfHir){
    fHir = pfHir;
    fLine= line;
    hir  = fHir.getHirRoot().hir;
    fHirUtil = fHir.getHirUtility();
    fTypeUtil= fHir.getTypeUtility();
  }

  public void print(int level, String spaces){
    if (defLabel != null){
      defLabel.print(level, spaces);
    }
  }
  public String toString(){
    if (defLabel != null){
      return defLabel.toString();
    }
    return "";
  }

  public void addLabel(Token t){
    defLabel = t;
  }

  public void preprocess(){
    fDeclMgr = fHir.getDeclManager();
    fESMgr   = fHir.getExecStmtManager();
    fTypeUtil = fHir.getTypeUtility();
    fHirUtil = fHir.getHirUtility();
  }

  /** define optional label and add it to generated statements
   *
   */
  public void process(){
    //   System.out.println("process currentStmt:"+this);
    if (defLabel != null){
      Label hLabel = fESMgr.makeLabel(defLabel.getLexem());

      if(generatedStmts == null){
        generatedStmts = new FirList(fHir);
      }
      generatedStmts.addFirst(
        hir.labeledStmt(hLabel, null));
    }
  }

  public Exp makeExp(){
    fHir.printMsgFatal("Fatal Error: FStmt is not an Expression.");
    return null;
  }

  public Exp makeArgAddr(FStmt pFStmt){
    fHir.printMsgFatal("Fatal Error: FStmt is not an Argument.");
    return null;
  }

  public String getLabelString(){
    if (defLabel == null)
      return "";
    else
      return defLabel.getLexem();
  }

  public boolean hasNotLabel(){
    return defLabel == null;
  }

  /** add generated statements and original statement to the block
   * Called after the process of original statement
   * @param block
   */
  public void addResultTo(BlockStmt block){
    Stmt result = getResult();
    if(result != null){
      Stmt stmt = (Stmt)result.copyWithOperands();

      stmt.setLineNumber(fLine);
      stmt.setFileName(fHir.fFileName);

      block.addLastStmt(stmt);
    }
  }

  void addGeneratedStmt(Stmt pStmt){
    if (generatedStmts == null){
      generatedStmts = new FirList(fHir);
    }
    generatedStmts.addedLast(pStmt);
  }
  void addGeneratedStmtFirst(Stmt pStmt){
    if (generatedStmts == null){
      generatedStmts = new FirList(fHir);
    }
    generatedStmts.addedFirst(pStmt);
  }

  SymTable fSymTable;

  SymTable mergeSymTable(SymTable dst, SymTable src){
    SymIterator it = src.getSymIterator();
    while(it.hasNext()){
      coins.sym.Sym sym = (coins.sym.Sym)it.next();
      dst.linkSym(sym);
    }
    return dst;
  }

  SymTable setSymTable(SymTable table){
    if(fSymTable == null){
      return (fSymTable = table);
    }
    else{
      return mergeSymTable(fSymTable, table);
    }
  }
  
  public void setLineAndFileInfo(Stmt pStmt){
    Stmt lStmt = pStmt;
    if(pStmt == null){
      return;
    }
    
    while(lStmt != null){
      lStmt.setLineNumber(fLine);
      lStmt.setFileName(fHir.fFileName);
      
      if(lStmt instanceof BlockStmt){
        BlockStmt bstmt = (BlockStmt)lStmt;
        setLineAndFileInfo(bstmt.getFirstStmt());
      }
      lStmt = lStmt.getNextStmt();
    }
  }

  public Stmt getResult(){
    setLineAndFileInfo(stmt);
        
    if (generatedStmts == null && fSymTable == null){
      return stmt;
    }
    else{
      BlockStmt blockStmt = hir.blockStmt(null);

      if(generatedStmts != null){
        Iterator it = generatedStmts.iterator();
        while (it.hasNext()){
          blockStmt.addLastStmt((Stmt)it.next());
        }
      }
      if (stmt != null){
        blockStmt.addLastStmt(stmt);
      }
      if(fSymTable != null){
        blockStmt.setSymTable(fSymTable);
      }
      setLineAndFileInfo(blockStmt);
      return blockStmt;
    }
  }

  /// debug print
  protected void dp(String str){
    fHir.dp(str);
  }
}

