/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import coins.HirRoot;
import coins.IoRoot;
import coins.SymRoot;
import coins.ir.hir.HIR;
import coins.sym.Sym;

public class F77Sym{

  private SymRoot  symRoot;
  private Sym sym;          // an instance to generate Sym objects
  private HirRoot hirRoot;
  private HIR hir;          // an instance to generate HIR objects
  private IoRoot  ioRoot;
  private F77Hir fHir;

  FirToHir fFirToHir;

  FirList dimensionList;
  FirList typedDeclList;
  FirList implicitList;
  FirList entryStmtList;
  FirList externalList;
  FirList dataList;
  FirList commonList;
  FirList equivList;
  FirList saveVarsList;
  
  Map     formatMap;


  String programName = null;
  HeaderStmt programHeader;

  public F77Sym(SymRoot sRoot, HirRoot hRoot, IoRoot iRoot,
                F77Hir fir) {
    symRoot = sRoot;
    sym     = symRoot.sym;
    hirRoot = hRoot;
    hir     = hirRoot.hir;
    ioRoot  = iRoot;

    fHir      = fir;
    fFirToHir = fHir.fFirToHir;

    initialize();
  }
  public void initialize(){ // for next program_unit
    programHeader = null;
    dimensionList = new FirList(fFirToHir);
    typedDeclList = new FirList(fFirToHir);
    implicitList  = new FirList(fFirToHir);
    entryStmtList = new FirList(fFirToHir);
    externalList  = new FirList(fFirToHir);
    dataList      = new FirList(fFirToHir);
    commonList    = new FirList(fFirToHir);
    equivList     = new FirList(fFirToHir);
    saveVarsList  = new FirList(fFirToHir);
    formatMap     = new HashMap();
  }

  public void debugPrint(int level, String pMsg){
    ioRoot.dbgToHir.print(level, pMsg);
  }

  public void print(int level){
    level = 0;
    ioRoot.dbgToHir.print(level, "\nF77Sym\n");
    ioRoot.dbgToHir.print(level, "programHeader : \n");
    programHeader.print(level, "  ");
    ioRoot.dbgToHir.print(level, "dimensionList : \n");
    if(dimensionList != null){
      dimensionList.print(level, "  ");
    }
    ioRoot.dbgToHir.print(level, "typedDeclList : \n");
    if(typedDeclList != null){
      typedDeclList.print(level, "  ");
    }
  }

  public FirList list(Object pElem){
    return new FirList(pElem, fFirToHir);
  }
  public FirList list(){
    return new FirList(fFirToHir);
  }
  public Pair pair(Node n1, Node n2){
    return new Pair(n1, n2, fFirToHir);
  }

  /** Add an instance of FirToHir to Token
   *
   * @param t old Token
   * @return modified Token
   */
  public Token modifiedToken(Token t){
    fHir.fLine = t.getLineNo();
    return t.addFHir(fFirToHir);
  }

  public void setProgramHeader(FStmt pHeader) {
    programHeader = (HeaderStmt)pHeader;
  }

  /** make Fir node of function header statement
   * [f77.jay] function_stmt : opt_label_def type FUNCTION IDENT func_dummy_args
   *				 { $$ = fSym.funcStmt($2, $4, $5); }
   *	| opt_label_def FUNCTION IDENT func_dummy_args
   *          { $$ = fSym.funcStmt(null, $3, $4); }
   * @param pType
   * @param pIdent
   * @param pArgs
   * @return function header statement. 
   */
  public Node funcStmt(Node pType, Token pIdent, FirList pArgs) {
    //	ioRoot.dbgToHir.print(8, "function_stmt : type FUNCTION IDENT func_dummy_args\n");
    return new HeaderStmt(pIdent, pArgs, true, (Pair)pType, fHir.fLine , fFirToHir);
  }

  /** make Fir node of subroutine header statement
   * [f77.jay] subroutine_stmt : opt_label_def SUBROUTINE IDENT subr_dummy_args
   *				{ $$ = fSym.subrStmt($3, $4); }
   * @param pIdent
   * @param pArgs
   * @return subroutine header.
   */
  public Node subrStmt(Token pIdent, FirList pArgs) {
    //	ioRoot.dbgToHir.print(8, "subroutine_stmt : SUBROUTINE IDENT subr_dummy_args\n");
    return new HeaderStmt(pIdent, pArgs, false, null, fHir.fLine, fFirToHir);
  }

  /** make Fir node of entry statement and add it to entryStmtList
   * [f77.jay] entry_stmt : opt_label_def ENTRY IDENT subr_dummy_args
   *                { $$ = fSym.entryStmt($3, $4); }
   * @param pIdent
   * @param pArgs
   * @return entry statement.
   */
  public Node entryStmt(Token pIdent, FirList pArgs) {
    //	ioRoot.dbgToHir.print(8, "entry_stmt : ENTRY IDENT subr_dummy_args\n");
    FStmt entryStmt = new EntryStmt(pIdent, pArgs, fHir.fLine, fFirToHir);
    entryStmtList.addLast(entryStmt);
    return entryStmt;
  }

  /** set main program name
   * [f77.jay] program_stmt : opt_label_def PROGRAM IDENT EOS
   *                  { $$ = fSym.programStmt($3); }
   * @param pName
   * @return null.
   */
  public Node programStmt(Token pName) {
    //	ioRoot.dbgToHir.print(8, "program_stmt : opt_label_def PROGRAM\n");
    programName = pName.getLexem();
    return null;
  }

  //block_data_stmt :  opt_label_def BLOCK DATA opt_identifier EOS
  //				{ $$ = fSym.blockDataStmt($1, $4); }
  //	;
  public Node blockDataStmt(Node p1, Node p2) {
    //ioRoot.dbgToHir.print(8, "\n** block_data_stmt : \n");
    //ioRoot.dbgToHir.print(8, "  " + p1 + ", ");
    //ioRoot.dbgToHir.print(8, "  " + p2 + "\n");
    return p1;
  }

  /** add declaration list to dimensionList or typedDeclList
   * [f77.jay] data_spec_stmt : type declaration_list
   *        { $$ = fSym.declList($1, $2); }
   * @param pType
   * @param pList
   * @return null.
   */
  public FStmt declList(Node pType, FirList pList) {
    //	ioRoot.dbgToHir.print(8, "data_spec_stmt : type declaration_list\n");
    if (pType instanceof Token){
      // DIMENSION
      // (declList Token(DIMENSION) List(args))
      dimensionList.addAll(pList);
    }
    else{
      // Pair
      typedDeclList.addLast(pair(pType, pList));
    }
    return null;
  }

  /** add declaration list to commonList ??? not yet
   * [f77.jay] data_spec_stmt : COMMON common_decl
   *         { $$ = fSym.commonDecl($2); }
   *
   * @param p1
   * @return null.
   */
  public FStmt commonDecl(FirList p1) {
    // ioRoot.dbgToHir.print(8, "data_spec_stmt : COMMON common_decl\n");
    // commonList.add(p1);

    // p1.print(0,"common : ");
    commonList.addAll(p1);
    return null;
  }

  /** add declaration list to equivalenceList ??? not yet
   * [f77.jay] data_spec_stmt : EQUIVALENCE equivalence_decl
   *         { $$ = fSym.equivalenceDecl($2); }
   * @param p1
   * @return equivalence list.
   */
  public FStmt equivalenceDecl(Node p1) {
    // ioRoot.dbgToHir.print(8, "data_spec_stmt : EQUIVALENCE equivalence_decl\n");
    // p1.print(0,"equiv list :");
    equivList.add(p1);
    return null;
  }

  /** add declaration list to dataDeclList ??? not yet
   * [f77.jay] data_spec_stmt : DATA data_decl
   *             { $$ = fSym.dataDecl($2); }
   * @param p1
   * @return null.
   */
  public FStmt dataDecl(FirList p1) {
    // ioRoot.dbgToHir.print(8, "data_spec_stmt : DATA data_decl\n");
    //fFirToHir.p("------------------------------------------");
    //p1.print(0,"--");

    dataList.add(p1);
    return null;
  }

  /** add declaration list to implicitList ??? not yet
   * [f77.jay] data_spec_stmt : IMPLICIT implicit_decl
   *          { $$ = fSym.implicitDecl($2); }
   * @param p1 declaration list.
   * @return null.
   */
  public FStmt implicitDecl(FirList p1) {
    // ioRoot.dbgToHir.print(8, "data_spec_stmt : IMPLICIT implicit_decl \n");
    implicitList.addAll(p1);
    return null;
  }

  /** add declaration list to saveList ??? not yet
   * [f77.jay] data_spec_stmt : SAVE opt_save_list
   *             { $$ = fSym.saveDecl($2); }
   * @param p1
   * @return null.
   */
  public FStmt saveDecl(Node p1) {
    // ioRoot.dbgToHir.print(8, "data_spec_stmt : SAVE opt_save_list \n");
    // do nothing
    saveVarsList.addLast(p1);
    return null;
  }

  /** add declaration list to paramList ??? not yet
   * [f77.jay] data_spec_stmt : PARAM  '(' const_list ')'
   *           { $$ = fSym.parameterDecl($3); }
   * @param p1
   * @return parameter list.
   */
  public FStmt parameterDecl(Node p1) {
    //parameterList.addLast(p1);
    typedDeclList.addLast(p1);
    // ioRoot.dbgToHir.print(8, "data_spec_stmt : PARAM  '(' const_list ')'\n");
    return null;
  }

  /** add declaration list to externalList.
   *  [f77.jay] functional_spec_stmt : EXTERNAL external_decl
   *             { $$ = fSym.externalDecl($2); }
   * @param p1
   * @return  null.
   */
  public FStmt externalDecl(Node p1) {
    //	ioRoot.dbgToHir.print(8, "functional_spec_stmt : EXTERNAL external_decl\n");
    externalList.addList((FirList)p1);
    return null;
  }

  /**
   *  [f77.jay] functional_spec_stmt : INTRINSIC  intrinsic_decl
   *              { $$ = fSym.intrinsicDecl($2); }
   * @param p1
   * @return intrinsic declaration.
   */
  public FStmt intrinsicDecl(Node p1) {
    ioRoot.dbgToHir.print(8, "functional_spec_stmt :INTRINSIC  intrinsic_decl\n");
    return null;
  }

  /** make Fir node of array declaration
   * [f77.jay] one_declaration : IDENT dims opt_length_spec
   *           { $$ = fSym.arrayDecl($1, $2, $3); }
   *        common_var  : IDENT dims  { $$ = fSym.arrayDecl($1, $2, null); }
   * @param pIdent
   * @param pDim
   * @param pOptLength
   * @return array declaration node.
   */
  public Node arrayDecl(Token pIdent, Node pDim, Node pOptLength) {
    // ioRoot.dbgToHir.print(8, "one_declaration : IDENT dims opt_length_spec\n");
    // ioRoot.dbgToHir.print(8, "   " + pIdent + "(" + pDim + ")" + " : " + pOptLength + "\n");
    return new Triple(pIdent, pDim, pOptLength, fFirToHir);
  }

  /** make Fir node of scalar declaration
   * [f77.jay] one_declaration : IDENT opt_length_spec
   *            { $$ = fSym.scalarDecl($1, $2); }
   *       common_var : IDENT     { $$ = fSym.scalarDecl($1, null);; }
   * @param pIdent
   * @param pOptLength
   * @return scalar declaration.
   */
  public Node scalarDecl(Token pIdent, Node pOptLength) {
    // ioRoot.dbgToHir.print(8, "** one_declaration : IDENT opt_length_spec\n");
    // ioRoot.dbgToHir.print(8, "   " + pIdent + ":" + pOptLength + "\n");
    return pair(pIdent, pOptLength);
  }

  /** make Fir node of type declaration
   * [f77.jay] type : type_name opt_length_spec   { $$ = fSym.type($1, $2); }
   *	| DIMENSION       { $$ = $1; }
   * @param pType
   * @param pOptLength
   * @return type declaration.
   */
  public Node type(Token pType, Node pOptLength) {
    //	ioRoot.dbgToHir.print(8, "type :  type_name opt_length_spec \n");
    return pair(pType, pOptLength);
  }

  //common_decl :	start_block     { $$ = fList.list($1); }
  //	| common_decl block_name one_block   { $$ = $1.addLast(fSym.block($2, $3); }
  //	;
  //start_block : opt_block_name one_block   { $$ = fSym.block($1, $2); }
  //	;
  public Node block(Token p1, Node p2) {
    // ioRoot.dbgToHir.print(8, "** start_block : opt_block_name one_block\n");
    // ioRoot.dbgToHir.print(8, "   " + p1 + ", " + p2 + "\n");
    // if(p1 == null) p("block name : !anonymous!"); else p1.print(0, "block name : ");
    // p2.print(0, "block      : ");
    return pair(p1, p2);
  }

  //dim : upper_bound                { $$ = fSym.dim(null, $1); }
  //	| expr ':' upper_bound    { $$ = fSym.dim($1, $3); }
  //	;
  // array(L:U) => Pair(L,U)
  public Node dim(Node p1, Node p2) {
    // ioRoot.dbgToHir.print(8, "** dim : expr ':' upper_bound\n");
    // ioRoot.dbgToHir.print(8, "        (" + p1 + " : " + p2 + ")\n");
    return pair(p1, p2);
    // return null;
  }

  /**


   */
  public void registFormat(Token label, String formatstring){
    formatMap.put(label.getLexem(), formatstring);
  }

  //imp_item : type_name opt_length_spec '(' letter_group_list ')'
  //				   { $$ = fSym.impItem($1, $2, $4); }
  //	;
  // ((type pt_length) . letters)
  final private static HashMap TypeHash = new HashMap();
  static{
    TypeHash.put("integer".intern(),         new Integer(Parser.INTEGER));
    TypeHash.put("real".intern(),            new Integer(Parser.REAL));
    TypeHash.put("doubleprecision".intern(), new Integer(Parser.DOUBLE_PREC));
    TypeHash.put("complex".intern(),         new Integer(Parser.COMPLEX));

    TypeHash.put("logical".intern(),         new Integer(Parser.LOGICAL));
    TypeHash.put("character".intern(),       new Integer(Parser.CHARACTER));

  }
  public Node impItem(Node p1, Node p2, Node p3) {
    // ioRoot.dbgToHir.print(8, "imp_item : type_name opt_length_spec \n");
    String typename = ((Token)p1).getLexem();
    int kind = 0;
    Integer i;

    if(typename == "none".intern()){
      return null;
    }

    if((i = (Integer)TypeHash.get(typename)) == null){
      fFirToHir.printMsgFatal("at Implicit statement, '" + typename + "' is not typename");
    }
    kind = i.intValue();
    Token  type = ((Token)p1).setKind(kind);
    modifiedToken(type);


    Iterator it = ((FirList)p3).iterator();
    StringBuffer str = new StringBuffer();
    while(it.hasNext()){
      Token t = (Token)it.next();
      str.append(t.getLexem());
    }
    return pair(pair(p1, p2), ((Token)p1).copy(str.toString()));
  }

  //letter_group :  IDENT          { $$ = fSym.letterGroup($1, null); }
  //	| IDENT '-' IDENT     { $$ = fSym.letterGroup($1, $3); }
  //	;
  public Node letterGroup(Token p1, Token p2) {
    // ioRoot.dbgToHir.print(8, "letter_group :  IDENT \n");
    char a1 = checkLetter(p1.getLexem());
    if(a1 == '/'){
      return null;
    }

    if(p2 == null){
      return p1;
    }
    else{
      char a2 = checkLetter(p2.getLexem());
      if(a1 > a2){
        //K error
        fFirToHir.printMsgFatal("can't backwards order of letters in letter range");
        return null;
      }
      StringBuffer str = new StringBuffer();
      for(char c = a1;c <= a2;c++){
        str.append(c);
      }
      return p1.copy(str.toString().intern());
    }
  }
  public char checkLetter(String letter){
    if(letter.length() != 1){
      return '/';
    }
    char a = letter.charAt(0);
    if('a' > a || 'z' < a){
      return '/';
    }
    return a;
  }

  void p(String str){
    fFirToHir.debugPrint(0,str + "\n");
  }

}

