/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

import coins.HirRoot;
import coins.IoRoot;
import coins.SymRoot;
import coins.ir.hir.HIR;
import coins.sym.Sym;

public class F77Hir{

  private SymRoot  symRoot; // root of symbol information
  private Sym sym;          // an instance to generate Sym objects
  private HirRoot hirRoot;
  private HIR hir;          // an instance to generate HIR objects
  private IoRoot  ioRoot;
  private F77Sym fSym;
  
  int fLine;
  
  FirList programBody;
  FirList assignList = null;
  FirToHir fFirToHir;
  

  public F77Hir(SymRoot sRoot, HirRoot hRoot, IoRoot iRoot) {
    symRoot = sRoot;
    sym     = symRoot.sym;
    hirRoot = hRoot;
    hir     = hirRoot.hir;
    ioRoot  = iRoot;
    
    fFirToHir = new FirToHir(sRoot, hRoot, iRoot);
  }

  public void setF77Sym(F77Sym fSym) {
    this.fSym = fSym;
  }
  public FirList getProgramBody(){
    return programBody;
  }
  public void addFirstStmt(FStmt pStmt){
    programBody.addFirst(pStmt);
  }
  public int getLineNo(){
    return fLine;
  }  
  
  /** make Hir from the Fir node of a program_unit
   *  and initializes for next program_unit
   * [f77.jay] executable_program : program_unit
   *        { $$ = fHir.executableProgram($1); }
   * @param pUnit program_unit node
   * @return null
   */
  public Node executableProgram(Node pUnit) {
    //	ioRoot.dbgToHir.print(8, "executable_program : program_unit\n");
    //  fFirToHir = new FirToHir(symRoot, hirRoot, ioRoot);
    if(ioRoot.getCompileSpecification().getTrace().shouldTrace("ToHir", 4)){
      fSym.print(4);
      programBody.print(4, "");
    }
    
    fFirToHir.makeHirFromFir(fSym, this);
    ((HIR)hirRoot.programRoot).setIndexNumberToAllNodes(0);
    
    initialize();
    return null;
  }

  private void initialize(){ // for next program_unit
    assignList = null;
    fSym.initialize();
  }

  /** make Hir from the Fir node of next program_unit
   *  and initializes ... ??? (Sequence of program_units is not yet considered) //##68
   * [f77.jay] executable_program : executable_program  program_unit
   *          { $$ = fHir.executableProgram($1, $2); }
   * @param pUnit program_unit node
   * @return null
   */
  public Node executableProgram(Node pExPrgrm, Node pUnit) {
    //	ioRoot.dbgToHir.print(8, "executable_program : executable_program program_unit\n");
    if (ioRoot.getCompileSpecification().getTrace().shouldTrace("ToHir", 4)) {
      fSym.print(4);
      programBody.print(4, "");
    }
    
    fFirToHir.makeHirFromFir(fSym, this);
    initialize();
    return null;
  }

  /** make Fir node of main program
   * [f77.jay] program_unit : program_stmt program_body  { $$ = fHir.mainProgram($2); }
   *                        | program_body    { $$ = fHir.mainProgram($1); }
   * @param pBody statement list
   * @return Fir node of program_body
   */
  public Node mainProgram(FirList pBody) {
    //	ioRoot.dbgToHir.print(8, "program_unit: program_body\n");
    programBody = pBody;
    Token mainToken = new Token(0, "main".intern(), Parser.IDENT, fFirToHir);
    fSym.setProgramHeader(new HeaderStmt(mainToken, null, false, null, fLine, fFirToHir));
    return programBody;
  }
  /** make Fir node of function subprogram
   * [f77.jay] program_unit : function_stmt program_body
   *         { $$ = fHir.funcSubProgram($1, $2); }
   * @param pFuncStmt function statement
   * @param pBody statement list
   * @return Fir node of program_body
   */
  public Node funcSubProgram(FStmt pFuncStmt, FirList pBody) {
    //	ioRoot.dbgToHir.print(8, "program_unit: function_stmt program_body\n");
    programBody = pBody;
    fSym.setProgramHeader(pFuncStmt);
    return programBody;
  }
  /** make Fir node of subroutine subprogram
   * [f77.jay] program_unit : subroutine_stmt program_body
   *         { $$ = fHir.subrSubProgram($1, $2); }
   * @param pSubrStmt subroutine statement
   * @param pBody statement list
   * @return Fir node of program_body
   */
  public Node subrSubProgram(FStmt pSubrStmt, FirList pBody) {
    //	ioRoot.dbgToHir.print(8, "program_unit: subroutine_stmt program_body\n");
    programBody = pBody;
    fSym.setProgramHeader(pSubrStmt);
    return programBody;
  }
  /** make Fir node of block data subprogram
   * [f77.jay] program_unit : block_data_subprogram
   *         { $$ = fHir.blockDataSubp($1);  }
   * @param p1
   * @return block data node.
   */
  public Node blockDataSubp(Node p1) {
    programBody = (FirList)p1;
    return p1;
  }

  /** make Fir node of end statement
   * [f77.jay] end statement : opt_label_def END EOS
   *        { $$ = fHir.endStmt($1);  }
   * @param pLabel optional label
   * @return Fir node of end statement
   */
  public FStmt endStmt(Token pLabel) {
    //  ioRoot.dbgToHir.print(8, "end_statement \n");
    FStmt lStmt = new EndStmt(fLine, fFirToHir);
    if (pLabel != null){
      lStmt.addLabel( pLabel);
    }
    return lStmt;
  }

  /** make Fir node of optionally labeled statement
   * [f77.jay] complete_statement : opt_label_def statement EOS
   *        { $$ = fHir.completeStmt($1, $2);  }
   * @param pLabel optional label
   * @param pStmt
   * @return optionally-labeled statement.
   */
  public FStmt completeStmt(Token pLabel, FStmt pStmt) {
    //	ioRoot.dbgToHir.print(8, "completeStmt(Token pLabel, FStmt pStmt) \n");
    if(pStmt instanceof FormatStmt){
      if(pLabel != null &&
         pStmt  != null){
        fSym.registFormat(pLabel, ((FormatStmt)pStmt).getLexem());
      }
      else{
        //K error
        // unlabeled FORMAT statement
      }
    }
    else if (pLabel != null &&
             pStmt  != null){
      pStmt.addLabel(pLabel);
    }
    return pStmt;
  }

  //block_data_subprogram :
  //	block_data_stmt block_data_body end_statement
  //				{ $$ = fHir.blockDataSubProgram($1, $2); }
  //	;
  public Node blockDataSubProgram(Node p1, Node p2) {
    // ioRoot.dbgToHir.print(8, "block_data_subprogram : \n");
    return p1;
  }

  //block_data_body : 	{ $$ = null; }
  //	| block_data_body opt_label_def data_spec_stmt EOS
  //				{ $$ = fHir.blockDataBody($1, $2, $3); }
  //	;
  public Node blockDataBody(Node p1, Node p2, Node p3) {
    // ioRoot.dbgToHir.print(8, "** block_data_body :\n");
    // ioRoot.dbgToHir.print(8, "   " + p1 + ", " + p2 + ", " + p3 + "\n");
    return p1;
  }

  //functional_spec_stmt : EXTERNAL external_decl   { $$ = fSym.externalDecl($2); }
  //	| FORMAT CHAR_CONST    { $$ = fHir.format($2); }
  //	;
  public FStmt format(Node p1) {
    // ioRoot.dbgToHir.print(8, "functional_spec_stmt : FORMAT CHAR_CONST\n");
    FStmt stmt = new FormatStmt(p1, fLine, fFirToHir);
    return stmt;
  }

  //data_seq : data_var_list '/' data_val_list '/'   { $$ = fHir.dataSeq($1, $3); }
  //	;
  public Node dataSeq(FirList p1, FirList p2) {
    // ioRoot.dbgToHir.print(8, "data_seq : data_var_list '/' data_val_list '/' \n");
    return new Pair(p1,p2,fFirToHir);
  }

  //data_val : value			  { $$ = fHir.dataVal(null, $1); }
  //	| simple_value '*' value      { $$ = fHir.dataVal($1, $3); }
  //	;
  public Node dataVal(Node p1, Node p2) {
    // ioRoot.dbgToHir.print(8, "data_val : value\n");
    if(p1 == null){
      return p2;
    }
    return new Pair(p1,p2,fFirToHir);
  }

  //value :  simple_value       { $$ = fHir.value('+', $1); }
  //	| '+' simple_value     { $$ = fHir.value('+', $2); }
  //	| '-' simple_value     { $$ = fHir.value('-', $2); }
  //	;
  public Node value(char p1, Node p2) {
    // ioRoot.dbgToHir.print(8, "value :  simple_value\n");
    if(p1 == '-'){
      return  exprUnary(HIR.OP_NEG,p2);
    }
    else{
      return p2;
    }
  }

  //simple_value : IDENT   { $$ = $1; }
  //	| constant       { $$ = $1; }
  //	| complex_const     { $$ = $1; }
  //	;

  //const_item : IDENT '=' expr   { $$ = fHir.constItem($1, $3); }
  //	;
  public Node constItem(Token p1, Node p2) {
    // ioRoot.dbgToHir.print(8, "const_item : IDENT '=' expr \n");
    return new Pair(p1,p2,fFirToHir);
  }

  //data_var :  left_name 		  { $$ = fHir.dataVarOne($1); }
  //	| '(' data_var_list do_spec ')'   { $$ = fHir.dataVarDoList($2, $3); }
  //	;
  public Node dataVarOne(Node p1) {
    // ioRoot.dbgToHir.print(8, "data_var :  left_name \n");
    return p1;
  }
  public Node dataVarDoList(Node p1, Node p2) {
    //ioRoot.dbgToHir.print(8, "data_var :  '(' data_var_list do_spec ')' \n");
    return new Pair(p1,p2,fFirToHir);
  }

  /** make Fir node of labeled do statement
   * [f77.jay] executable_statement : DO label do_spec
   *       { $$ = fHir.doLabeled($2, $3); }
   * @param pLabel
   * @param pDoSpec
   * @return labeled-do statement.
   */
  public FStmt doLabeled(Token pLabel, Node pDoSpec) {
    //	ioRoot.dbgToHir.print(8, "executable_statement : DO label do_spec  \n");
    return new LabeledDoStmt(pLabel, pDoSpec, fLine, fFirToHir);
  }
  /** make Fir node of unlabeled do statement
   * [f77.jay] executable_statement : DO do_spec EOS do_tail
   *       { $$ = fHir.doUnLabeled($2, $4); }
   * @param p1
   * @param p2
   * @return unlabeled-do statement.
   */
  public FStmt doUnLabeled(Node p1, Node p2) {
    // ioRoot.dbgToHir.print(8, "executable_statement : DO do_spec EOS do_tail  \n");
    return new UnLabeledDoStmt(p1, p2, fLine, fFirToHir);
  }
  /** make Fir node of if statement
   * [f77.jay] executable_statement : IF '(' expr ')' ifable_statement
   *        { $$ = fHir.ifStmt($3, $5); }}
   * @param pExp
   * @param pStmt
   * @return if-statement.
   */
  public FStmt ifStmt(Node pExp, FStmt pStmt) {
    //	ioRoot.dbgToHir.print(8, "executable_statement : IF '(' expr ')' ifable_statement  \n");
    return new IfStmt(pExp, pStmt, fLine, fFirToHir);
  }
  //
  /** make Fir node of block if statement
   * [f77.jay] executable_statement : block_if_part opt_else_if_parts opt_else_part END_IF
   *    { $$ = fHir.blockIfStmt($1, $2, $3); }
   * @param pIfPart
   * @param pOptElseIfs
   * @param pOptElse
   * @return block_if statement.
   */
  public FStmt blockIfStmt(Node pIfPart, FirList pOptElseIfs, FirList pOptElse) {
    //	ioRoot.dbgToHir.print(8, "executable_statement : block_if  \n");
    return new BlockIfStmt(pIfPart, pOptElseIfs, pOptElse, fLine, fFirToHir);
  }

  /** make Fir node of block_if_part
   * [f77.jay] block_if_part : IF '(' expr ')' THEN EOS part_tail
   *       { $$ = fHir.blockIfPart($3, $7); }
   * @param pExp
   * @param pTail
   * @return block_if_part.
   */
  public Node blockIfPart(Node pExp, FirList pTail) {
    //	ioRoot.dbgToHir.print(8, "block_if_part : IF '(' expr ')' THEN ...\n");
    return new Pair(pExp, pTail, fFirToHir);
  }

  /** make Fir node of else_if_part
   * [f77.jay] opt_else_if_parts :    	{ $$ = fList.list(); }
   * 	| opt_else_if_parts ELSE_IF  '(' expr ')' THEN EOS part_tai
   * 		{ $$ = $1.addLast(fHir.elseIfPart($4, $8)); }
   * @param pExp
   * @param pTail
   * @return else-if part.
   */
  public Node elseIfPart(Node pExp, FirList pTail) {
    //	ioRoot.dbgToHir.print(8, "elseIfPart(Node p1, Node p2)\n");
    return new Pair(pExp, pTail, fFirToHir);
  }

  /** make Fir node of do_spec
   * [f77.jay] do_spec : IDENT '=' expr ',' expr opt_step
   *         { $$ = fHir.doSpec($1, $3, $5, $6); }
   * @param pIdent
   * @param pExp1
   * @param pExp2
   * @param pOptStep
   * @return do_spec node.
   */
  public Node doSpec(Token pIdent, Node pExp1, Node pExp2, Node pOptStep) {
    //	ioRoot.dbgToHir.print(8, "do_spec : IDENT '=' expr ',' expr opt_step\n");
    return new Quad(pIdent, pExp1, pExp2, pOptStep, fFirToHir);
  }

  /** make Fir node of assign statement or statement function statement
   * [f77.jay] ifable_statement : LET left_name '=' expr
   *        { $$ = fHir.assignOrFunc($2, $5); }
   * @param pLeft
   * @param pRight
   * @return assign statement or statement function statement.
   */
  public FStmt assignOrFunc(Node pLeft, Node pRight) {
    //	ioRoot.dbgToHir.print(8, "LET left_name '=' expr\n");
    return new AssignOrFuncStmt(pLeft, pRight, fLine, fFirToHir);
  }

  /** make Fir node of label assign statement
   * [f77.jay] ifable_statement : ASSIGN  label TO IDENT
   *        { $$ = fHir.assignLabel($2, $4); }
   * @param pLabel
   * @param pIdent
   * @return label assign statement.
   */
  public FStmt assignLabel(Token pLabel, Token pIdent) {
    //	ioRoot.dbgToHir.print(8, "ASSIGN  label TO IDENT\n");
    FStmt assignStmt = new AssignLabelStmt(pLabel, pIdent, fLine, fFirToHir);
    if (assignList == null){
      assignList = new FirList(fFirToHir);
    }
    assignList.addLast(assignStmt);
    return assignStmt;
  }

  /** make Fir node of continue statement
   * [f77.jay] ifable_statement : CONTINUE
   *       { $$ = fHir.continueStmt(); }
   * @return continue statement.
   */
  public FStmt continueStmt() {
    //	ioRoot.dbgToHir.print(8, "CONTINUE\n");
    return new ContinueStmt(fLine, fFirToHir);
  }

  /** make Fir node of goto statement
   * [f77.jay] ifable_statement : GOTO label
   *       { $$ = fHir.gotoStmt($2); }
   * @param pLabel
   * @return goto statement.
   */
  public FStmt gotoStmt(Token pLabel) {
    //	ioRoot.dbgToHir.print(8, "GOTO  label\n");
    return new GotoStmt(pLabel, fLine, fFirToHir);
  }

  /** make Fir node of assigned goto statement
   * [f77.jay] ifable_statement : ASSIGN_GOTO IDENT opt_labels
   *      { $$ = fHir.aGoto($2, $3); }
   * @param pIdent
   * @param pOptLabels
   * @return assigned-goto statement.
   */
  public FStmt aGoto(Token pIdent, FirList pOptLabels) {
    //	ioRoot.dbgToHir.print(8, "ASSIGN_GOTO IDENT opt_labels\n");
    return new AssignGotoStmt(pIdent, pOptLabels, fLine, fFirToHir);
  }

  /** make Fir node of computed goto statement
   * [f77.jay] ifable_statement : COMP_GOTO '(' label_list ')' expr
   *     { $$ = fHir.cGoto($3, $5); }
   * @param pLabels
   * @param pExp
   * @return computed-goto statement.
   */
  public FStmt cGoto(FirList pLabels, Node pExp) {
    //	ioRoot.dbgToHir.print(8, "COMP_GOTO '(' label_list ')'  expr\n");
    return new ComputedGoto(pLabels, pExp, fLine, fFirToHir);
  }

  /** make Fir node of arithmetic if statement
   * [f77.jay] ifable_statement : ARITH_IF '(' expr ')' label ',' label ',' label
   *        { $$ = fHir.aIf($3, $5, $7, $9); }
   * @param pNode
   * @param pMinusLab
   * @param pZeroLab
   * @param pPlusLab
   * @return arithmetic-if statement.
   */
  public FStmt aIf(Node pNode, Token pMinusLab, Token pZeroLab, Token pPlusLab) {
    //	ioRoot.dbgToHir.print(8, "ARITH_IF '(' expr ')' label ',' ...\n");
    return new ArithIfStmt(pNode, pMinusLab, pZeroLab, pPlusLab, fLine, fFirToHir);
  }

  /** make Fir node of call statement
   * [f77.jay] ifable_statement : CALL IDENT opt_actual_args
   *         { $$ = fHir.call($2, $3); }
   * @param pIdent
   * @param pOptArgs
   * @return call statement.
   */
  public FStmt call(Token pIdent, FirList pOptArgs) {
    //	ioRoot.dbgToHir.print(8, "CALL IDENT opt_actual_args\n");
    return new CallStmt(pIdent, pOptArgs, fLine, fFirToHir);
  }

  /** make Fir node of return statement
   * [f77.jay] ifable_statement : RETURN  opt_expr
   *          { $$ = fHir.returnStmt($2); }
   * @param pOptExpr
   * @return return-statement.
   */
  public FStmt returnStmt(Node pOptExpr) {
    //	ioRoot.dbgToHir.print(8, "RETURN  opt_expr\n");
    return new ReturnStmt(pOptExpr, fLine, fFirToHir);
  }

  /** make Fir node of pause statement
   * [f77.jay] ifable_statement : PAUSE  expr
   *          { $$ = fHir.pause($2); }
   * @param p1
   * @return pause statement.
   */
  public FStmt pause(Node p1) {
    // ioRoot.dbgToHir.print(8, "PAUSE  expr\n");
    return new PauseStmt(p1, fLine, fFirToHir);
  }

  /** make Fir node of stop statement
   * [f77.jay] ifable_statement : STOP  expr
   *          { $$ = fHir.stop($2); }
   * @param p1
   * @return stop-statement.
   */
  public FStmt stop(Node p1) {
    //ioRoot.dbgToHir.print(8, "STOP  expr\n");
    return new StopStmt(p1, fLine, fFirToHir);
  }

  /** make Fir node of '*' label argument
   * [f77.jay] arg : '*' label
   * 		{ $$ = fHir.argLabel($2); }
   * @param pLabel
   * @return label token
   */
  public Token argLabel(Token pLabel) {
    //	ioRoot.dbgToHir.print(8, "arg :  '*' label\n");
    return pLabel;
  }

  /** make Fir node of statement label
   * [f77.jay] label : INT_CONST
   *       { $$ = fHir.label($1); }
   * @param pIntConst
   * @return statement label.
   */
  public Token label(Token pIntConst){
    //  ioRoot.dbgToHir.print(8, "label : INT_CONST\n");
    return pIntConst.setKind(Parser.LABEL);
  }

  //io_statement : PRINT format_spec opt_comma_io_list  { $$ = fHir.printStmt($2, $3); }
  //	| WRITE '(' ci_list ')' opt_io_list 		{ $$ = fHir.writeStmt($3, $5); }
  //	| READ '(' ci_list ')' opt_io_list				{ $$ = fHir.readStmt($3, $5); }
  //	| READ format_spec opt_comma_io_list	{ $$ = fHir.readFStmt($2, $3); }
  //	|  OPEN '(' ci_list ')' 			{ $$ = fHir.openStmt($3); }
  //	| CLOSE '(' ci_list ')' 			{ $$ = fHir.closeStmt($3); }
  //	| BACKSPACE '(' ci_list ')' 		{ $$ = fHir.backspace($3); }
  //	| BACKSPACE format_spec 		{ $$ = fHir.backspaceF($2); }
  //	| END_FILE '(' ci_list ')' 		{ $$ = fHir.endfile($3); }
  //	| END_FILE format_spec 		{ $$ = fHir.endfileF($2); }
  //	| REWIND '(' ci_list ')' 		{ $$ = fHir.rewind($3); }
  //	| REWIND format_spec 		{ $$ = fHir.rewindF($2); }
  //	| INQUIRE '(' ci_list ')' 		{ $$ = fHir.inquire($3); }
  //	;
  public FStmt printStmt(Node pFormat, FirList pOptIoList) {
    ioRoot.dbgToHir.print(8, "PRINT format_spec opt_comma_io_list\n");
    FirList ciList = new FirList(new Pair(null, null, fFirToHir), fFirToHir);
    ciList.addLast(new Pair(null, pFormat, fFirToHir));
    return new WriteStmt(ciList, pOptIoList, fLine, fFirToHir);
  }
  public FStmt writeStmt(FirList pCiList, FirList pOptIoList) {
    //	ioRoot.dbgToHir.print(8, "WRITE '(' ci_list ')' opt_io_list\n");
    return new WriteStmt(pCiList, pOptIoList, fLine, fFirToHir);
  }
  public FStmt readStmt(Node p1, Node p2) {
    //ioRoot.dbgToHir.print(8, "READ '(' ci_list ')' opt_io_list\n");

    return new ReadStmt((FirList)p1, (FirList)p2, fLine, fFirToHir);
  }
  public FStmt readFStmt(Node p1, Node p2) {
    //ioRoot.dbgToHir.print(8, "READ format_spec opt_comma_io_list\n");

    return new ReadStmt((Node)p1, (FirList)p2, fLine, fFirToHir);
  }
  public FStmt openStmt(FirList p1) {
    // ioRoot.dbgToHir.print(8, "OPEN '(' ci_list ')' \n");
    return new OpenStmt(p1, fLine, fFirToHir);
  }
  public FStmt closeStmt(FirList p1) {
    // ioRoot.dbgToHir.print(8, "CLOSE '(' ci_list ')'\n");
    return new CloseStmt(p1, fLine, fFirToHir);
  }
  public FStmt backspace(FirList p1) {
    // ioRoot.dbgToHir.print(8, "BACKSPACE '(' ci_list ')'\n");
    return new OtherIOStmt("f_back", p1, null, fLine, fFirToHir);
  }
  public FStmt backspaceF(Node p1) {
    return new OtherIOStmt("f_back", null, p1, fLine, fFirToHir);
  }
  public FStmt endfile(FirList p1) {
    return new OtherIOStmt("f_end", p1, null, fLine, fFirToHir);
  }
  public FStmt endfileF(Node p1) {
    return new OtherIOStmt("f_end", null, p1, fLine, fFirToHir);
  }
  public FStmt rewind(FirList p1) {
    return new OtherIOStmt("f_rew", p1, null, fLine, fFirToHir);
  }
  public FStmt rewindF(Node p1) {
    return new OtherIOStmt("f_rew", null, p1, fLine, fFirToHir);
  }
  
  public FStmt inquire(FirList p1) {
    // ioRoot.dbgToHir.print(8, "INQUIRE '(' ci_list ')'\n");
    return new InquireStmt(p1, fLine, fFirToHir);
  }

  //io_clause : expr 			{ $$ = fHir.ioClause(null, $1); }
  //	| '*'					{ $$ = fHir.ioClause(null, null); }
  ///	| IDENT '=' expr 		{ $$ = fHir.ioClause($1, $3); }
  //	| IDENT '=' '*' 		{ $$ = fHir.ioClause($1, null); }
  //	;
  public Node ioClause(Token pIdent, Node pExpr) {
    // ioRoot.dbgToHir.print(8, "io_clause\n");
    return new Pair(pIdent, pExpr, fFirToHir);
  }

  //io_item :  expr 				{ $$ = fHir.ioItemExpr($1); }
  //	| '(' do_list  do_spec ')' 	{ $$ = fHir.ioItemDoList($2, $3); }
  //	;
  public Node ioItemExpr(Node pExpr) {
    // ioRoot.dbgToHir.print(8, "io_item :  expr \n");
    return pExpr;
  }
  /**
  public Node ioItemDoList(FirList pList, Node pDoSpec) {
    // ioRoot.dbgToHir.print(8, "io_item :'(' do_list  do_spec ')' \n");
    return new DoList(pList, pDoSpec, fFirToHir);
  }
   */
  public Node ioItemDoList(FirList pList) {
    // ioRoot.dbgToHir.print(8, "io_item :'(' do_list  do_spec ')' \n");
    // last item is dospec
    Node doSpec = (Node)pList.removeLast();
    // pList.print(0,"==>");
    // doSpec.print(0,"==>");
    return new DoListNode(pList, doSpec, fFirToHir);
  }

  //expr :  left_name 			{ $$ = fHir.leftName($1); }
  //	| constant 			{ $$ = fHir.constant($1); }
  //	| complex_const 		{ $$ = $1; }
  //	| '(' expr ')' 			{ $$ = fHir.enclosed($2); }
  //	| '-' expr %prec UMINUS	{ $$ = fHir.exprUnary(HIR.OP_NEG, $2); }
  //	| expr POWER expr		{ $$ = fHir.exprPower( $1, $3); }
  //	| expr '*' expr			{ $$ = fHir.exprBinary(HIR.OP_MULT, $1, $3); }
  //	| expr '/' expr			{ $$ = fHir.exprBinary(HIR.OP_DIV, $1, $3); }
  //	| expr '+' expr			{ $$ = fHir.exprBinary(HIR.OP_ADD, $1, $3); }
  //	| expr '-' expr			{ $$ = fHir.exprBinary(HIR.OP_SUB, $1, $3); }
  //	| expr DOUBLE_SLASH expr	{ $$ = fHir.exprCat($1, $3); }
  //	| expr GREATER_THAN expr	{ $$ = fHir.exprBinary(HIR.OP_CMP_GT, $1, $3); }
  //	| expr EQUAL expr			{ $$ = fHir.exprBinary(HIR.OP_CMP_EQ, $1, $3); }
  //	| expr LESS_THAN expr		{ $$ = fHir.exprBinary(HIR.OP_CMP_LT, $1, $3); }
  //	| expr NOT_EQUAL expr		{ $$ = fHir.exprBinary(HIR.OP_CMP_NE, $1, $3); }
  //	| expr LESS_OR_EQUAL expr	{ $$ = fHir.exprBinary(HIR.OP_CMP_LE, $1, $3); }
  //	| expr GREATER_OR_EQUAL expr	{ $$ = fHir.exprBinary(HIR.OP_CMP_GE, $1, $3); }
  //	| NOT expr			{ $$ = fHir.exprUnary(HIR.OP_NOT, $2); }
  //	| expr AND expr		{ $$ = fHir.exprBinary(HIR.OP_AND, $1, $3); }
  //	| expr OR expr		{ $$ = fHir.exprBinary(HIR.OP_OR, $1, $3); }
  //	| expr EQV expr		{ $$ = fHir.expUnary(HIR.OP_NOT,
  //						fHir.exprBinary(HIR.OP_XOR, $1, $3)); }
  //	| expr NEQV expr	{ $$ = fHir.exprBinary(HIR.OP_XOR, $1, $3); }
  //	;
  /** make Fir node of parenthesized expression
   * [f77.jay] expr : '(' expr ')'
   * 			{ $$ = fHir.enclosed($2); }
   * @param pExp
   * @return parenthesized expression.
   */
  public Node enclosed(Node pExp) {
    //	ioRoot.dbgToHir.print(8, "expr : '(' expr ')' \n");
    return new UnaryNode(HIR.OP_ENCLOSE, pExp, fFirToHir);
  }

  /** make Fir node of unary expression
   * [f77.jay] expr : '-' expr %prec UMINUS  { $$ = fHir.exprUnary(HIR.OP_NEG, $2); }
   * 	| NOT expr	{ $$ = fHir.exprUnary(HIR.OP_NOT, $2); }
   * @param op
   * @param pExp
   * @return unary expression.
   */
  public Node exprUnary(int op, Node pExp) {
    //	ioRoot.dbgToHir.print(8, "expr : unaryOp  expr  \n");
    return new UnaryNode(op, pExp, fFirToHir);
  }

  /** make Fir node of binary expression
   * [f77.jay] expr : expr '*' expr   { $$ = fHir.exprBinary(HIR.OP_MULT, $1, $3); }
   *	| expr '/' expr	    { $$ = fHir.exprBinary(HIR.OP_DIV, $1, $3); }
   *	| expr '+' expr	    { $$ = fHir.exprBinary(HIR.OP_ADD, $1, $3); }
   *	| expr '-' expr	    { $$ = fHir.exprBinary(HIR.OP_SUB, $1, $3); }
   * 	| expr GREATER_THAN expr  { $$ = fHir.exprBinary(HIR.OP_CMP_GT, $1, $3); }
   *	| expr EQUAL expr   { $$ = fHir.exprBinary(HIR.OP_CMP_EQ, $1, $3); }
   *	| expr LESS_THAN expr   { $$ = fHir.exprBinary(HIR.OP_CMP_LT, $1, $3); }
   *	| expr NOT_EQUAL expr   { $$ = fHir.exprBinary(HIR.OP_CMP_NE, $1, $3); }
   *	| expr LESS_OR_EQUAL expr   { $$ = fHir.exprBinary(HIR.OP_CMP_LE, $1, $3); }
   *	| expr GREATER_OR_EQUAL expr   { $$ = fHir.exprBinary(HIR.OP_CMP_GE, $1, $3); }
   * 	| expr AND expr   { $$ = fHir.exprBinary(HIR.OP_AND, $1, $3); }
   * 	| expr OR expr   { $$ = fHir.exprBinary(HIR.OP_OR, $1, $3); }
   * 	| expr EQV expr   { $$ = fHir.expUnary(HIR.OP_NOT,
   *		    fHir.exprBinary(HIR.OP_XOR, $1, $3)); }
   *	| expr NEQV expr   { $$ = fHir.exprBinary(HIR.OP_XOR, $1, $3); }
   * @param op
   * @param e1
   * @param e2
   * @return binary expression.
   */
  public Node exprBinary(int op, Node e1, Node e2) {
    //	ioRoot.dbgToHir.print(8, "expr : expr binaryOp expr  \n");
    return new BinaryNode(op, e1, e2, fFirToHir);
  }

  /** make Fir node of powered expression
   * [f77.jay] expr : expr POWER expr   { $$ = fHir.exprPower( $1, $3); }
   * @param pNode1
   * @param pNode2
   * @return powered expression.
   */
  public Node exprPower(Node pNode1, Node pNode2) {
    //	ioRoot.dbgToHir.print(8, "expr : expr POWER expr  \n");
    return new PowerNode(pNode1, pNode2, fFirToHir);
  }

  /** make Fir node of concatenated expression
   * [f77.jay] expr : expr DOUBLE_SLASH expr   { $$ = fHir.exprCat($1, $3); }
   * @param p1
   * @param p2
   * @return concatenated expression.
   */
  public Node exprCat(Node p1, Node p2) {
    //	ioRoot.dbgToHir.print(8, "expr : expr DOUBLE_SLASH expr  \n");
    return new ConcatNode(p1, p2, fFirToHir);
  }

  //left_name :  IDENT			{ $$ = $1; }
  //	| IDENT substring 			{ $$ = fHir.leftNameSubstr($1, null, $2); }
  //	| IDENT '(' arg_list ')'		{ $$ = fHir.leftName($1, $3); }
  //	| IDENT '(' arg_list ')' substring 	{ $$ = fHir.leftNameSubstr($1, $3, $5); }
  //	;

  /** make Fir node of subscripted variable or function call
   * [f77.jay] left_name : IDENT '(' arg_list ')'
   *        { $$ = fHir.leftName($1, $3); }
   * @param pIdent
   * @param argList
   * @return left name.
   */
  public Node leftName(Token pIdent, FirList argList) {
    //	ioRoot.dbgToHir.print(8, "left_name :  IDENT '(' arg_list ')' \n");
    return new SubscrOrFunCallNode(pIdent, argList, fFirToHir);
  }
  public Node leftNameSubstr(Token pIdent, FirList pArgs, Pair pSubstr) {
    // ioRoot.dbgToHir.print(8, "left_name :  IDENT ... substr  \n");
    if(pArgs == null){
      return new SubstringNode(pIdent, pSubstr, fFirToHir);
    }
    else{
      return new SubstringNode(
        new SubscrOrFunCallNode(pIdent, pArgs, fFirToHir),
        pSubstr, fFirToHir);
    }
  }

  //substring :  '(' opt_expr ':' opt_expr ')' 	{ $$ = fHir.substring($2, $4); }
  //	;
  public Pair substring(Node p1, Node p2) {
    // ioRoot.dbgToHir.print(8, "substring :  '(' opt_expr ':' opt_expr ')'  \n");
    return fSym.pair(p1, p2);
  }

  // OBSOLETED!!
  //
  //constant :  CHAR_CONST 	{ $$ = fHir.constChar($1); }
  //	| INT_CONST 		 	{ $$ = fHir.constInt($1); }
  //	| TRUE_CONST  			{ $$ = fHir.constTrue(); }
  //	| FALSE_CONST  		{ $$ = fHir.constFalse(); }
  //	| REAL_CONST 			{ $$ = fHir.constReal($1); }
  //	;
  /*
public Node constChar(Token p1) {
	ioRoot.dbgToHir.print(8, "constant :  CHAR_CONST \n");
	return null;
}
public Node constInt(Token pIntConst) {
	ioRoot.dbgToHir.print(8, "constant :  INT_CONST \n");
  return new Constant(pIntConst);
//  IntConst intConst = sym.intConst(p1.getLexem(), symRoot.typeInt);
//	return hir.constNode(intConst);
}
public Node constReal(Token pRealConst) {
	ioRoot.dbgToHir.print(8, "constant :  REAL_CONST \n");
  return new Constant(pRealConst);
//  FloatConst fConst = sym.floatConst(p1.getLexem(), symRoot.typeFloat);
//	return hir.constNode(fConst);
}
public Node constTrue() {
	ioRoot.dbgToHir.print(8, "constant :  TRUE_CONST \n");
	return null;
}
public Node constFalse() {
	ioRoot.dbgToHir.print(8, "constant :  FALSE_CONST \n");
	return null;
}
   */

  /** make Fir node of complex constant
   * [f77.jay] complex_const :  '(' expr ',' expr ')'
   *      { $$ = fHir.constComplex($2, $4); }
   * @param pReal
   * @param pImag
   * @return  true if constant number.
   */

  protected boolean checkConstNumber(Node node){
    if(node instanceof Token){
      Token t = (Token)node;
      int kind = t.getKind();
      if(kind == Parser.REAL_CONST ||
         kind == Parser.DOUBLE_CONST ||
         kind == Parser.INT_CONST){
        return true;
      }
    }
    if(node instanceof UnaryNode){
      return checkConstNumber(((UnaryNode)node).getExp());
    }
    return false;
  }
  
  public Node constComplex(Node pReal, Node pImag) {
    //	ioRoot.dbgToHir.print(8, "complex_const :  '(' expr ',' expr ')' \n");
    if(checkConstNumber(pReal) == false||
       checkConstNumber(pImag) == false){
      // error
      fFirToHir.printMsgFatal("Constant complex's each element must be constant, but :" +
                              "(" + pReal + ", " + pImag + ")");
    }
    
    return new ComplexConstNode(pReal, pImag, fFirToHir);
  }

}

