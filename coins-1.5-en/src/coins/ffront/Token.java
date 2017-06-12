/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

import coins.ir.hir.Exp;
import coins.ir.hir.HIR;
import coins.sym.Param;
import coins.sym.PointerType;
import coins.sym.StructType;
import coins.sym.Subp;
import coins.sym.Sym;
import coins.sym.Type;
import coins.sym.Var;

public class Token implements Node, HasConstValue{
  FirToHir    fHir;

  DeclManager     fDeclMgr;
  HirUtility      fHirUtil;
  TypeUtility     fTypeUtil;

  private int lineNo;
  private     String  lexem;
  private int kind;

  Token ( int n, String s, FirToHir pfHir) {
    lineNo = n; lexem = s.intern(); kind = -1; addFHir(pfHir);
  }
  Token ( int n, String s) {
    this(n, s, null);
  }
  Token ( int n, String s, int k, FirToHir pfHir) {
    lineNo = n; lexem = s.intern(); kind = k; addFHir(pfHir);
  }
  Token ( int n, String s, int k) {
    this(n, s, k, null);
  }
  public Token addFHir(FirToHir pfHir){
    fHir = pfHir;
    if(fHir != null){
      fHirUtil = fHir.getHirUtility();
      fTypeUtil = fHir.getTypeUtility();
    }
    return this;
  }
  public Token setKind(int k) {
    kind = k; return this;
  }

  public String changeLexem(){
    lexem = (lexem + "_").intern();
    return lexem;
  }

  public Token copy(){
    return new Token(lineNo, lexem, kind, fHir);
  }
  public Token copy(String l){
    return new Token(lineNo, l.intern() , kind, fHir);
  }
  public String toString() {
    return "line:"+lineNo+" \""+lexem+"\" "+kindName();
  }
  public void print(int level, String spaces){
    fHir.debugPrint(level, spaces+toString()+"\n");
  }
  public int getKind() { return kind; }
  public String getLexem() { return lexem; }

  public String kindName() {
    switch (kind) {
    case Parser.REAL:        return "REAL";
    case Parser.INTEGER:     return "INTEGER";
    case Parser.DOUBLE_PREC: return "DOUBLE_PREC";
    case Parser.LOGICAL:     return "LOGICAL";
    case Parser.COMPLEX:     return "COMPLEX";
    case Parser.CHARACTER:   return "CHARACTER";
    case Parser.DIMENSION:   return "DIMENSION";
    case Parser.PROGRAM:     return "PROGRAM";
    case Parser.IDENT:       return "IDENT";
    case Parser.LET:         return "LET";
    case Parser.REAL_CONST:  return "REAL_CONST";
    case Parser.DOUBLE_CONST:return "DOUBLE_CONST";
    case Parser.INT_CONST:   return "INT_CONST";
    case Parser.END:         return "END";
    case Parser.EOS:         return "EOS";
    case Parser.LABEL:       return "LABEL";
    case Parser.LABEL_DEF:   return "LABEL_DEF";
    case Parser.CHAR_CONST:  return "CHAR_CONST";
    default: return ""+kind;
    }
  }


  /** Make HIR Exp node from this token which is ident.
   *
   *
   * @return exp
   */
  public Exp makeIdentExp(){

    // const check
    Exp const_e;
    if((const_e = fDeclMgr.getConstManager().getConstExp(lexem)) != null){
      dp("makeExp from token(" + lexem + ") as constant");
      return const_e;
    }
    ///////////////////////////////////////////////////////////////
    // equiv check
    if(fDeclMgr.isEquivVariable(lexem)){
      dp("- makeExp from token(" + lexem + ") as equivalence variable");
      return fDeclMgr.makeEquivVariableExp(lexem);
    }

    // block check
    if(fDeclMgr.isBlockVariable(lexem)){
      dp("- makeExp from token(" + lexem + ") as a part of block variable");
      return fDeclMgr.makeBlockVariableExp(lexem);
    }

    ///////////////////////////////////////////////////////////////

    Sym lSym   = fDeclMgr.searchSymOrAddVar(lexem);
    Type lType = lSym.getSymType();
    HIR hir = fDeclMgr.hir;

    // if this token is SubProgram
    if (lSym instanceof Subp){
      HeaderStmt header = fDeclMgr.getProgramHeader();
      if (header.getLexem() == lexem){
        // and if this is the function name of me, this is variable for return
        dp("- At makeExp, function name identifier: " + lexem);
        return header.getReturnVarToken().makeExp();
      }
      else if(fDeclMgr.isDefinedInLocal(lSym)){
        dp("- Token#makeExp/External Exp: " + lexem);
        return hir.exp(HIR.OP_ADDR, hir.subpNode((Subp)lSym));
      }
      else{
        // may be variable
        dp("- Token#makeExp from token(" + lexem + ") as variable");
        return hir.varNode(fDeclMgr.defineVar(lexem));
      }
    }

    ///////////////////////////////////////////////////////////////
    Var lVar;
    if (lSym instanceof Var){
      lVar = (Var)lSym;
    }
    else{
      fHir.printMsgFatal("not a variable/subprogram "
                         +this+"(" + lSym.getClass() + ")");
      lVar = fDeclMgr.searchOrAddVar(lexem);
    }

    ///////////////////////////////////////////////////////////////
    // parameter
    if (lVar instanceof Param){

      if (lType instanceof StmtFuncParamType){
        StmtFuncParamType stmtFuncParamType = (StmtFuncParamType)lType;
        SubscrOrFunCallNode stmtFunCall = stmtFuncParamType.getStmtFunCall();
        int paramIndex = stmtFuncParamType.getParamIndex();

        dp("- Token#makeExp from token(" + lexem + ") as StmtFuncParamType");
        return stmtFunCall.getAParamAt(paramIndex);
      }
      lType = ((PointerType)lType).getPointedType();
      /*
      if(lType instanceof VectorType){
        // adjustable table
        dp("- Token#makeExp(" + lexem + ") as parameter array variable");
        return hir.undecayExp(hir.varNode(lVar));
      }
      else{
        lType = ((PointerType)lType).getPointedType();
      }
       */
      if (lType instanceof StructType){ // Complex type
        dp("- Token#makeExp(" + lexem + ") as complex parameter");
        return fHirUtil.makeComplexExp(hir.contentsExp(hir.varNode(lVar)));
      }
      else{
        dp("- Token#makeExp(" + lexem + ") as parameter variable");
        return hir.contentsExp(hir.varNode(lVar));
      }
    }
    ///////////////////////////////////////////////////////////////

    // complex
    if (lType instanceof StructType){ // Complex type
      dp("- Token#makeExp(" + lexem + ") as complex");
      return fHirUtil.makeComplexExpFromVar(lVar);
    }
    ///////////////////////////////////////////////////////////////

    // entry
    if (lType instanceof EntryType){
      dp("- Token#makeExp(" + lexem + ") as entry");
      return fDeclMgr.getProgramHeader().getReturnVarToken().makeExp();
    }
    ///////////////////////////////////////////////////////////////

    // otherwise
    dp("- Token#makeExp(" + lexem + ") as variable");
    return hir.varNode(lVar);
  }

  /** Make HIR Exp node from this token.
   * case IDENT: (thisVar = defined var of this token)
   *   case Param:
   *     case complex: complexExp(contentsExp(thisVar))
   *     default: contentsExp(thisVar)
   *   case StmtFuncParam: actual parameter of StmtFuncCall
   *   case complex: complexExp(thisVar)
   *   case ENTRY name: returnVar of this subprogram
   *   default: thisVar
   * case CONST: ConstNode
   *
   * @return HIR Exp.
   */
  public Exp makeExp(){
    fDeclMgr = fHir.getDeclManager();

    dp("Token#makeExp: " + getLexem() + " as " + kindName());

    switch (kind){
    case Parser.IDENT:
      Exp exp = makeIdentExp();
      Type type = exp.getType();

      if(fHir.getTypeUtility().isFortranCharacterType(type)){
        // for character
        return fHirUtil.makeFortranCharacterExp(
          fHir.getHir().decayExp(exp), fTypeUtil.getFortranCharacterLengthExp(type, this));
      }
      else{
        return exp;
      }
    case Parser.INT_CONST:
      return fHirUtil.makeIntConstNode(lexem);
    case Parser.REAL_CONST:
      return fHirUtil.makeFloatConstNode(lexem);
    case Parser.CHAR_CONST:
      return fHirUtil.makeCharsConstNode(lexem);
    case Parser.TRUE_CONST:
      return fHirUtil.makeTrueConstNode();
    case Parser.FALSE_CONST:
      return fHirUtil.makeFalseConstNode();
    case Parser.DOUBLE_CONST:
      return fHirUtil.makeDoubleConstNode(lexem.replace('D','E'));
    default:
      if(lexem == "*"){
        return null;
      }
      fHir.printMsgFatal("unknown token " + this);
      return null;
    }
  }

  /** Make HIR Exp node from this token as a call-by-address parameter in pCallStmt.
   *  case IDENT: (thisVar = defined var of this token)
   *    case Param: thisVar
   *    default: address of thisVar
   *  case LABEL: add this to pCallStmt's label list and return null.
   *  case CONST: address of temp ( temp = this )
   * @param pCallStmt a call statement or a function call expression
   * @return call-by-address parameter.
   */
  public Exp makeArgAddr(FStmt pCallStmt){
    fDeclMgr = fHir.getDeclManager();

    HIR hir = fDeclMgr.hir;
    switch (kind){
    case Parser.IDENT:
      // if constant
      Exp const_e;
      if ((const_e = fDeclMgr.getConstManager().
           getConstArgAddr(getLexem(),pCallStmt)) != null){
        return const_e;
      }

      // equiv check
      if(fDeclMgr.isEquivVariable(lexem)){
        dp("- Token#makeArgAddr(" + lexem + ") as equivalence variable");
        return hir.exp(HIR.OP_ADDR, fDeclMgr.makeEquivVariableExp(lexem));
      }

      // block check
      if(fDeclMgr.isBlockVariable(lexem)){
        dp("- Token#makeArgAddr(" + lexem + ") as a part of block variable");
        return hir.exp(HIR.OP_ADDR, fDeclMgr.makeBlockVariableExp(lexem));
      }

      // others
      Sym lSym = fDeclMgr.searchSymOrAddVar(lexem);
      if (lSym instanceof Subp){
        HeaderStmt header = fDeclMgr.getProgramHeader();
        if (header.getLexem() == lexem){
          dp("* At makeArgAddr, function name variable: " + lexem);
          return header.getReturnVarToken().makeArgAddr(pCallStmt);
        }
        else{
          dp("Token#makeArgAddr: function name " + lexem);
          return fHirUtil.makeSubpExp((Subp)lSym);
        }
      }
      if (! (lSym instanceof Var)){
        fHir.printMsgFatal("undefined "+lexem);
        return null;
      }

      Var lVar = (Var)lSym;
      Type lType = lVar.getSymType();

      if (lVar  instanceof Param){
        // value of a parameter is an address
        return hir.varNode(lVar);
      }
      else{
        return hir.exp(HIR.OP_ADDR, hir.varNode(lVar));
      }

    case Parser.LABEL: // alternate return
      if (pCallStmt instanceof CallStmt){
        CallStmt callStmt = (CallStmt)pCallStmt;
        callStmt.fLabels.addLast(this);
        callStmt.hasAltReturn = true;
        return null;
      }
      else {
        fHir.printMsgFatal("label as argument");
      }
      return null;

    case Parser.INT_CONST:
    case Parser.REAL_CONST:
    case Parser.DOUBLE_CONST:
      return fHirUtil.makeArgAddr(pCallStmt, makeExp());
    case Parser.CHAR_CONST:
      return ((FortranCharacterExp)makeExp()).getBody();
    case Parser.TRUE_CONST:
    case Parser.FALSE_CONST:
      return fHirUtil.makeArgAddr(pCallStmt, makeExp());
    default:
      fHir.printMsgFatal("unknown token " + this);
      return null;
    }
  }

  void dp(String str){
    fHir.dp(str);
  }

  /**
    Return this token value as integer if it's enable

    ex) int_const node, real_const node, and so on.

   */
  public FNumber getConstValue(){
    switch(kind){
    case Parser.INT_CONST:
      return FNumber.make(Integer.parseInt(lexem));
    case Parser.REAL_CONST:
      return FNumber.make(Float.parseFloat(lexem));
    case Parser.DOUBLE_CONST:
      return FNumber.make(Double.parseDouble(lexem));
    case Parser.IDENT:
      // return const val
      return fHir.getDeclManager().getConstManager().getConstValue(lexem);
    default:
      //
    }
    return null;
  }
  /**
   * @return line no of this token.
   */
  public int getLineNo() {
    return lineNo;
  }

}
