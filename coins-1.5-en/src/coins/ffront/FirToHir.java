/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

import coins.HirRoot;
import coins.IoRoot;
import coins.SymRoot;
import coins.ir.hir.BlockStmt;
import coins.ir.hir.HIR;
import coins.sym.Sym;

/** Main class for Fir to Hir translation
 */
public class FirToHir{

  private SymRoot symRoot;  // root of symbol information
  private HirRoot hirRoot;
  private IoRoot  ioRoot;

  String fFileName;
  
  F77Sym   f7Sym;
  F77Hir   f7Hir;
  
  HirUtility  fHirUtil;
  TypeUtility fTypeMgr; // 
  DeclManager fDeclMgr; // 
  ExecStmtManager fESMgr;
  IntrinsicUtility fIntrUtil;

  
  // getter
  HirRoot getHirRoot(){ return hirRoot; }
  SymRoot getSymRoot(){ return symRoot; }
  HIR     getHir()    { return hirRoot.hir; }
  Sym     getSym()    { return symRoot.sym; }
  
  HirUtility  getHirUtility() { return fHirUtil; }
  TypeUtility getTypeUtility(){ return fTypeMgr; }
  DeclManager getDeclManager(){ return fDeclMgr; }
  ExecStmtManager getExecStmtManager(){ return fESMgr; }
  IntrinsicUtility getIntrinsicUtility(){
    return fIntrUtil;
  }

  
  // constructor
  public FirToHir(SymRoot sRoot, HirRoot hRoot, IoRoot iRoot) {
    symRoot = sRoot;
    hirRoot = hRoot;
    ioRoot  = iRoot;
    	
    fFileName = iRoot.getSourceFile().getName();
    
    fHirUtil = new HirUtility(this, hirRoot.hir);
    fTypeMgr = new TypeUtility(this);
    fHirUtil.fTypeUtil = fTypeMgr;
    fTypeMgr.fHirUtil = fHirUtil;

    fIntrUtil = new IntrinsicUtility(this);
    
    hirRoot.programRoot =
      getHir().program(null, symRoot.symTableRoot, null, null);
  }

  public void makeHirFromFir(F77Sym pf7Sym, F77Hir pf7Hir) {
    f7Sym = pf7Sym;
    f7Hir = pf7Hir;
    
    fDeclMgr = new DeclManager(this, f7Sym);
    fESMgr   = new ExecStmtManager(this);

    dp("** start process decl");
    fDeclMgr.processDecl();

    if(f7Sym.programHeader != null){
      dp("** start process execution body");
      // process body
      BlockStmt blockStmt =
        fESMgr.processExecStmt(f7Hir.getProgramBody());
      // add initial part
      blockStmt.addFirstStmt(fDeclMgr.fInitialPart);
    
      //K for Hir2C(temporary)
      blockStmt.setSymTable(fDeclMgr.fSubpDef.getSymTable());

      fDeclMgr.fSubpDef.finishHir();
      fDeclMgr.fSubpDef.setHirBody(blockStmt);
    }
  }
  
  
  //**************************************************
  //* for display
  //**************************************************
  
  public void debugPrint(int level, String pMsg){
    ioRoot.dbgToHir.print(level, pMsg);
  }
  public void dp(String msg){
    ioRoot.dbgToHir.print(2,";; " + msg + "\n");
  }
  public void p(String pMsg){
    ioRoot.dbgToHir.print(0, pMsg);
    ioRoot.dbgToHir.print(0, "\n");
  }

  public void printMsgWarn(String pMsg){
	String msg = pMsg;
	if (fESMgr.currentStmt != null){
	  msg = msg + " at " + fESMgr.currentStmt.toString();
	  msg = msg + "(line: " + fESMgr.currentStmt.fLine + ")";
	}
	ioRoot.msgWarning.put(": " + pMsg);
  }
  public void printMsgFatal(String pMsg){
    String msg = pMsg;
    if (fESMgr.currentStmt != null){
      msg = msg + " at " + fESMgr.currentStmt.toString();
      msg = msg + "(line: " + fESMgr.currentStmt.fLine + ")";
    }
    ioRoot.msgFatal.put(": "+pMsg);
  }
  public void printMsgRecovered(String pMsg){
    String msg = pMsg;
    if (fESMgr.currentStmt != null){
      msg = msg + " at " + fESMgr.currentStmt.toString();
      msg = msg + "(line: " + fESMgr.currentStmt.fLine + ")";
    }
    ioRoot.msgFatal.put(": "+pMsg);
    ioRoot.msgRecovered.put(": "+msg);
  }
  public void printMsgError(String pMsg){
    String msg = pMsg;
    if (fESMgr.currentStmt != null){
      msg = msg + " at " + fESMgr.currentStmt.toString();
      msg = msg + "(line: " + fESMgr.currentStmt.fLine + ")";
    }
    ioRoot.msgFatal.put(": "+pMsg);
    ioRoot.msgError.put(": "+msg);
  }
}




