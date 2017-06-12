/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

import java.util.Iterator;

import coins.sym.Var;

public class HeaderStmt extends FStmt{
  private Token fIdent;
  private String originalIdString = null;
  private FirList fArgs;
  private boolean isFunction;
  private Pair fType;
  private int numberOfStar;
  private Var returnVar;
  private Token returnVarToken;
  private Token firstIntArg;

  public HeaderStmt(Token pIdent, FirList pArgs,
                    boolean isF, Pair pType, int line, FirToHir pfHir){
    super(line, pfHir);
    fIdent       = pIdent;
    fArgs        = pArgs;
    isFunction   = isF;
    fType        = pType;
    numberOfStar = 0;
    returnVar    = null;
  }
  public void print(int level, String spaces){
    super.print(level, spaces);
    fHir.debugPrint(level, spaces+"HeaderStmt"+fIdent+"\n");
  }
  public String toString(){
    return super.toString()+"Header statement"+ fIdent;
  }

  /** Make an entry statement and add it to entryList.
   * For example, from
   *   FUNCTION f(r)
   * make
   *   ENTRY f(r)
   * and add it to entryList and to programBody as the first ENTRY statement.
   * Add "i_" to fArgs as the first parameter.
   * @param entryList
   */
  public void addEntryStmt(FirList entryList){
    EntryStmt headEntry = new EntryStmt(fIdent.copy(), fArgs, fLine, fHir);
    entryList.addFirst(headEntry);
    fHir.f7Hir.addFirstStmt(headEntry);
    fArgs = (FirList)fArgs.clone(); // this.fArgs = int-param + headEntry.fArgs
    firstIntArg = new Token(0, "i_".intern(), Parser.IDENT, fHir);
    fArgs.addFirst(firstIntArg);
  }

  /** Change this header statement as follows:
   * (1) change subprogram name "abc" to "abc_"
   * (2) replace parameter list
   * (3) make computed GOTO statement to go to the place of
   *     each ENTRY statement and add it as the first stmt
   * @param pArgs
   * @param entryStmtList
   */
  public void change(FirList pArgs, FirList entryStmtList){
    originalIdString = fIdent.getLexem();
    fIdent.changeLexem();
    fArgs = pArgs;
    FirList labelList = new FirList(fHir);
    for (Iterator it = entryStmtList.iterator(); it.hasNext(); ){
      EntryStmt entryStmt = (EntryStmt)it.next();
      labelList.addLast(entryStmt.getIdentToken());
    }
    FStmt cGoto = new ComputedGoto(labelList, firstIntArg, fLine, fHir);
    fHir.f7Hir.addFirstStmt(cGoto);
  }

  /** Change this function statement to subprogram statement.
   *  Change
   *     funcType FUNCTION funcName(...)
   *  to
   *     SUBROUTINE funcName_(funcType funcName, ...)
   *
   */
  public void changeToSubprogram(){
    isFunction       = false;
    originalIdString = fIdent.getLexem();
    fIdent.changeLexem();
    
    Token newParam = new Token(0, originalIdString, Parser.IDENT, fHir);
    fArgs.addFirst(newParam);
    
    if(fType != null){
      // add (funcType funcName) to typedDeclList
      // !!K 
      FirList typedDeclList = fHir.f7Sym.typedDeclList;
      typedDeclList.addLast(
        new Pair(fType,
                 new FirList(new Pair(newParam,null,fHir), fHir), fHir));
    }
  }

  public String getReturnVarString(){
    if (returnVarToken == null){
      getReturnVarToken();
    }
    return returnVarToken.getLexem();
  }
  
  public Token getReturnVarToken(){
    if (returnVarToken == null){
      returnVarToken =
        new Token(0,
                  ("_"+fIdent.getLexem()).intern(),
                  Parser.IDENT, fHir);
    }
    return returnVarToken;
  }
  
  public boolean isFunction(){
    return isFunction;
  }
  public Pair getTypePair(){
    return fType;
  }
  public String getLexem(){
    return fIdent.getLexem();
  }
  public Token getIdToken(){
    return fIdent;
  }
  public FirList getArgs(){
    return fArgs;
  }
  public void setStar(int pInt){
    numberOfStar = pInt;
  }
  public int getStar(){
    return numberOfStar;
  }
  public void setReturnVar(Var pVar){
    returnVar = pVar;
  }
  public Var getReturnVar(){
    return returnVar;
  }
  public String getOriginalIdString(){
    return originalIdString;
  }
  public void process(){
    stmt = null;
    super.process();
  }
}


