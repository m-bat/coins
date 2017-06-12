/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

import java.util.Iterator;

import coins.ir.hir.BlockStmt;
import coins.sym.Subp;
import coins.sym.Sym;

/** Entry statement
 */
public class EntryStmt extends FStmt{
  private Token fIdent;     // entry name
  private FirList fArgs;    // original formal parameter list
  private Token[] callArgs; // expanded actual parameter list
  private int fEntryCount;
  private boolean isFunctionEntry;

  public EntryStmt(Token pIdent, FirList pArgs, int line, FirToHir pfHir){
    super(line, pfHir);
    fIdent = pIdent; fArgs = pArgs;
  }
  public void print(int level, String spaces){
    super.print(level, spaces);
    dp("= EntryStmt"+fIdent+"\n");
  }
  public String toString(){
    return super.toString()+"Entry statement"+ fIdent;
  }
  public String getIdent(){
    return fIdent.getLexem();
  }
  public Token getIdentToken(){
    return fIdent;
  }

  /** Translate
   *    ENTRY g(s)
   * to
   *   L_g:
   * If this is a function entry then redefine g as EntryType variable.
   */
  public void process(){
    preprocess();
    if (isFunctionEntry){
      Sym lSym = fDeclMgr.search(getIdent());
      if (lSym != null){
        lSym.remove();
      }
      fHir.getSymRoot().sym.defineVar(getIdent(), fTypeUtil.getEntryType());
    }
    stmt = hir.labeledStmt(fESMgr.makeLabel(getIdent()), null);
    super.process();
  }

  /** Add parameters of this ENTRY statement to paramList.
   *
   * @param paramList
   */
  public void addParamTo(FirList paramList){
    callArgs = new Token[fArgs.size()+paramList.size()];
    for (Iterator it = fArgs.iterator(); it.hasNext(); ){
      Token aToken = (Token)it.next();
      String ident = aToken.getLexem();
      int i, pSize = paramList.size();
      for(i = 1; i < pSize; i++){
        if (ident == ((Token)paramList.get(i)).getLexem()){
          callArgs[i] = aToken;
          break; // if i-th of paramList has the same ident
        }        // then this ident becomes i-th param
      }
      if (i == pSize){
        // else this ident becomes pSize-th param
        callArgs[i] = aToken;
        paramList.addLast(aToken);
      }
    }
  }

  /** make HIR subprogram from this ENTRY statement
   *
   * @param entryCount
   * @param pArgs
   */
  public void makeSubp(int entryCount, FirList pArgs){
    preprocess();

    fEntryCount = entryCount;
    HeaderStmt oHeader = fDeclMgr.getProgramHeader(); // save original program header
    isFunctionEntry = oHeader.isFunction();
    // make new header for new subprogram
    HeaderStmt eHeader = new HeaderStmt(fIdent, fArgs, isFunctionEntry, null, fLine, fHir);
    // define original subprogram which is called from new subprogram
    Subp lSubp = fDeclMgr.defineSubp(oHeader.getLexem(),
                                     fDeclMgr.getFunctionType(), Sym.SYM_PUBLIC, null);

    // push const manager
    ConstManager oConstMgr = fDeclMgr.fConstMgr;
    fDeclMgr.fConstMgr     = new ConstManager(fHir, fDeclMgr);
    
    // make new subprogram from this ENTRY statement
    fDeclMgr.setProgramHeader(eHeader);
    fDeclMgr.processProgramHeader();

    // make actual parameters to call original subprogram
    Token firstParam = new Token(0, String.valueOf(fEntryCount), Parser.INT_CONST, fHir);
    Token dummyParam = new Token(0, "dummy_".intern(), Parser.IDENT, fHir);
    FirList aParam = new FirList(firstParam, fHir); // actual parameter list
    for (int i = 1; i < pArgs.size(); i++){
      aParam.addLast(callArgs[i] == null ? dummyParam : callArgs[i]);
    }
    
    // make program body
    BlockStmt blockStmt = hir.blockStmt(null);
    FStmt currentStmt;
    if (isFunctionEntry){
      Node right  = new SubscrOrFunCallNode(oHeader.getIdToken(), aParam, fHir);
      Token left  = new Token(0, eHeader.getReturnVarString(),Parser.IDENT, fHir);
      currentStmt = new AssignOrFuncStmt(left, right, fLine, fHir);
    }
    else{
      currentStmt = new CallStmt(oHeader.getIdToken(), aParam, fLine, fHir);
    }

    fESMgr.setCurrentStmt(currentStmt); // for process currentStmt
    currentStmt.process(); // make HIR node of currentStmt
    currentStmt.addResultTo(blockStmt); // add the result to the block
    currentStmt = new EndStmt(fLine, fHir);
    fESMgr.setCurrentStmt(currentStmt);
    currentStmt.process();
    currentStmt.addResultTo(blockStmt);
    fDeclMgr.setHirBody(blockStmt); // set the HIR body of the subprogram
    blockStmt.setSymTable(fDeclMgr.fSymTable);
    
    fDeclMgr.setProgramHeader(oHeader); // restore original program header
    fDeclMgr.fConstMgr = oConstMgr;     // restore original const manager
  }
}

