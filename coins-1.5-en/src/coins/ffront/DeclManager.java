/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

import java.util.Iterator;
import java.util.LinkedList;

import coins.ir.IrList;
import coins.ir.hir.BlockStmt;
import coins.ir.hir.Exp;
import coins.ir.hir.ExpListExp;
import coins.ir.hir.HIR;
import coins.ir.hir.NullNode;
import coins.ir.hir.Program;
import coins.ir.hir.SetDataStmt;
import coins.ir.hir.Stmt;
import coins.ir.hir.SubpDefinition;
import coins.sym.Param;
import coins.sym.Subp;
import coins.sym.Sym;
import coins.sym.SymTable;
import coins.sym.Type;
import coins.sym.Var;
import coins.sym.VectorType;

/**
  Declaration Manager
 */
public class DeclManager extends BaseManager{

  ImplicitManager fImplMgr;
  CommonManager   fCommonMgr;
  EquivManager    fEquivMgr;
  ConstManager    fConstMgr;

  F77Sym   f7Sym;

  Type functionType;
  HeaderStmt programHeader;

  Subp           fSubp;
  SymTable       fSymTable;
  SubpDefinition fSubpDef;
  BlockStmt      fInitialPart;

  FirList        fCharParamList;

  public DeclManager(FirToHir fth, F77Sym f7s){
    super(fth);

    f7Sym    = f7s;

    fImplMgr   = new ImplicitManager(fth);
    fCommonMgr = new CommonManager(fth, this);
    fEquivMgr  = new EquivManager (fth, fCommonMgr, this);
    fConstMgr  = new ConstManager (fth, this);
    fInitialPart=    hir.blockStmt(null);
    
    fCharParamList = new FirList(fHir);
  }

  //
  public Type getImplicitType(String id){
    return fImplMgr.getImplicitType(id);
  }

  public Type getDeclType(String pIdent){
    //
    printMsgFatal("unimplemented");
    return null;
  }

  ConstManager getConstManager(){
    return fConstMgr;
  }

  //////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////
  /**
    Process Declare.
   */
  void processDecl(){

    dp("* processImplicit");
    fImplMgr.processImplicit(f7Sym);

    if(f7Sym.programHeader == null){
      dp("BLOCK DATA statement");
    }
    else{
      dp("* programHeader");
      programHeader = f7Sym.programHeader;

      dp("* defineFunctionType");
      defineFunctionType();
      
      dp("* checkEntryStmt");
      checkEntryStmt();

      // declare variables and so on
      dp("* processProgramHeader");
      processProgramHeader();
    }
    
    dp("* processCommon");
    fCommonMgr.processCommon(programHeader == null ?
                             "__BlockData" :
                             programHeader.getLexem());

    dp("* processEquiv");
    fEquivMgr.processEquiv(programHeader == null ?
                           "__BlockData" :
                           programHeader.getLexem());

    //////////////////////////////////////////////

    dp("* processData");
    ProcessData pd = new ProcessData(fHir);
    pd.process();

    dp("* processSave");
    processSave();
    // do nothing.
  }

  //////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////

  /** define function type (void if subroutine or main).
   *
   */
  void defineFunctionType(){
    String ident  = programHeader.getLexem();
    Type type     = null;
    Pair typePair = null;

    if(programHeader.isFunction()){
      dp("- This program unit is function");
      typePair = programHeader.getTypePair();
      if(typePair == null){
        type = searchType(ident);
        dp("type : " + type);
      }
      else{
        type = fTypeUtil.getType(typePair);
      }
    }
    else {
      dp("- This program unit is Subroutine");
      type = symRoot.typeVoid;
    }

    // if the procedure type is complex, set function type 'void'
    if (fTypeUtil.isComplexType(type)){
      //    complex function cf(a,b,...)
      // => subroutine cf(complex cf, a,b,...)
      programHeader.changeToSubprogram();
      type = symRoot.typeVoid;
    }
    // define a subprogram in the current symbol table
    functionType = type;
    defineSubp(programHeader.getLexem(), type, Sym.SYM_PUBLIC, null);
  }

  /** If entryStmtList is not empty,
   * (1) make new ENTRY statement and add it to the program body
   * (2) add the parameters of each ENTRY statement to programHeader.fArgs
   * (3) change program header (change header's name and parameters and add
   *     computed goto statement to goto the place of each ENTRY statement)
   * (4) make new subprogram for each ENTRY statement
   */
  void checkEntryStmt(){
    FirList entryStmtList = f7Sym.entryStmtList;
    if (entryStmtList.size() == 0){
      return;
    }

    dp("This program unit has entrys");
    programHeader = f7Sym.programHeader;
    programHeader.addEntryStmt(entryStmtList); // as the first entry stmt
    FirList paramList = (FirList)programHeader.getArgs();

    Iterator it;
    for (it = entryStmtList.iterator(); it.hasNext(); ){
      EntryStmt eStmt = (EntryStmt)it.next();
      eStmt.addParamTo(paramList); // add eStmt's param to paramList
      dp("Entry : " + eStmt);
    }
    programHeader.change(paramList, entryStmtList);// change programHeader

    int entryCount;
    for (entryCount = 1, it = entryStmtList.iterator(); it.hasNext(); ){
      EntryStmt eStmt = (EntryStmt)it.next();
      eStmt.makeSubp(entryCount++, paramList); // make subprogram from entry stmt
    }
  }

  //////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////

  /** Process program header statement.
   * (1) define subprogram and symTable
   * (2) define declared variables
   * (3) add formal parameters to the subprogram
   * (4) if it has "*" parameter change SUBROUTINE to INTEGER FUNCTION
   * (5) in case of FUNCTION make a variable for return value
   */
  void processProgramHeader(){
    String ident = programHeader.getLexem();
    Type   type  = functionType;

    fSubp        = defineSubpWithoutClose(ident, type, Sym.SYM_PUBLIC);
    fSymTable    = symRoot.symTableRoot.pushSymTable(fSubp);

    processTypeDecl();   // define declared variables
    processExternal();   // external declaration
    processDimension();  // dimension declaration

    // add formal parameters
    FirList lParams        = programHeader.getArgs();
    IrList  lParamTypeList = hir.irList();
    int     countStar = 0; // number of "*" formal parameter

    if (lParams != null){
      // character length check
      
      for (Iterator it = lParams.iterator(); it.hasNext(); ){
        Object next = it.next();
        String id   = ((Token)next).getLexem();

        if (id != "*"){
          Param param    = defineParam(id);
          Type paramType = param.getSymType();

          fSubp.addParam(param);
          lParamTypeList.add(paramType);
          dp("PARAM: " + param + " as " + paramType);
        }
        else { // "*"
          countStar++;
        }
      }
      for (Iterator it = fCharParamList.iterator(); it.hasNext(); ){
        Object next = it.next();
        String id   = ((Token)next).getLexem();

        sym.defineVar(characterLengthVarName(id), fTypeUtil.getIntType());

        Param param = sym.defineParam(id, fTypeUtil.getIntType());
        param.markAsCallByReference();
        fSubp.addParam(param);
        lParamTypeList.add(fTypeUtil.getIntType());
      }
    }
    if (countStar != 0){
      // SUBROUTINE -> INTEGER FUNCTION
      dp("param/star: " + countStar);
      programHeader.setStar(countStar);
      type = symRoot.typeInt;
      fSubp.setReturnValueType(type);
    }

    if (programHeader.isFunction()){
      // add return variable
      Var returnVar = sym.defineVar(programHeader.getReturnVarString(), type);
      programHeader.setReturnVar(returnVar);
    }

    dp("subp => " + fSubp.toStringDetail());
    fSubpDef = hir.subpDefinition(fSubp, fSymTable);
    ((Program)hirRoot.programRoot).addSubpDefinition(fSubpDef);
    fSubp.closeSubpHeader();
  }

  /** define id as parameter.
   * if id is already declared as parameter, ignore this.
   *
   */
  Param defineParam(String id){
    Sym lSym = symRoot.symTableCurrent.search(id);
    Param param;
    Type  paramType;

    if(lSym instanceof Param){
      // do nothing, already declared as adjust array size.
      param = (Param)lSym;
    }
    else{
      if (lSym == null){
        paramType = sym.pointerType(getImplicitType(id));
      }
      else{
        paramType = lSym.getSymType();
        if(fTypeUtil.isFortranCharacterType(paramType)){
          fCharParamList.add(new Token(0, characterLengthVarName(id), fHir));
        }
        paramType = sym.pointerType(paramType);
        lSym.remove();
      }
      dp("[DECL] SubrParam: " + id + " as " + paramType);
      param = sym.defineParam(id, paramType);
      param.markAsCallByReference();
    }
    return param;
  }

  //////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////
  String characterLengthVarName(String id){
    return (id + "_len").intern();
  }

  void declVariable(Type type, String ident){
    if(search(ident) != null){
      /* ignore */
      return ;
    }
    Var v = sym.defineVar(ident, type);
    dp("- decl: " + ident + " as " + type.toString());
  }

  void declDimensionVariable(Type type, String ident, FirList dims){
    Type vt = fTypeUtil.getArrayType(type, dims, this);
    Var  v  = sym.defineVar(ident, vt);

    dp("- decl vector: " + ident + " as " + vt.toString());
  }

  void declVariableWithLength(Type type, String ident, Token length){
    // type must be character
    if(fTypeUtil.isFortranCharacterType(type) == false){
      printMsgFatal("Declaration with length spec must be Character type: " +
                    ident + " as " + type);
    }

    Type vt = sym.vectorType(fTypeUtil.getCharType(), length.makeExp());
    Var  v  = sym.defineVar(ident, vt);

    dp("- decl var with length spec: " + ident + " as " + vt.toString() + " / "
       + length.toString());
  }

  void declDimensionVariableWithLength(Type type, String ident,
                                       FirList dims, Token length){
    // type must be character
    if(fTypeUtil.isFortranCharacterType(type) == false){
      printMsgFatal("Declaration with length spec must be Character type: " +
                    ident + " as " + type);
    }
    type = sym.vectorType(fTypeUtil.getCharType(), length.makeExp());

    Type vt = fTypeUtil.getArrayType(type, dims, this);
    Var  v  = sym.defineVar(ident, vt);

    dp("- decl vector: " + ident + " as " + vt.toString() + "/" + length);
  }

  /**
    Process define variables as each types.
   */
  void processTypeDecl(){
    dp("* processTypeDecl");

    Iterator it = f7Sym.typedDeclList.iterator();

    while (it.hasNext()){
      Node     declStmt    = (Node)it.next();

      if(declStmt instanceof FirList){
        // parameter decl statement
        fConstMgr.processParameterDeclStatement((FirList)declStmt);
        continue;
      }

      // normal variable decl statement
      Pair     oneDeclStmt = (Pair)declStmt;
      Iterator varIt       = ((FirList)oneDeclStmt.getRight()).iterator();

      // declared type
      Type     declType    = fTypeUtil.getType((Pair)oneDeclStmt.getLeft());
      dp("declType: " + declType);
      
      // type with length spec must be character
      if(declType instanceof VectorType &&
         fTypeUtil.isFortranCharacterType(declType) == false){
        printMsgFatal("Declaration with length spec must be Character type: "
                      + declType);
      }

      while (varIt.hasNext()){
        Pair   oneDecl = (Pair)varIt.next();                   // declared variable
        String ident   = ((Token)oneDecl.getLeft()).getLexem();// variable name
        Node   length  = oneDecl.getRight();

        // already declared?
        if(isDefinedSymbol(ident)){
          printMsgFatal("[Type decl] This symbol is already declared: " + ident);
        }

        // variable
        if(length == null
           /* it's must include declare with length (*8)
              ex> integer*8, real*8, complex*8
            */
           ){
          dp("decl variable: " + ident);
          declVariable(declType, ident);
        }
        else if(oneDecl instanceof Triple &&
                ((Triple)oneDecl).getExtra() != null){
          Triple tr = (Triple)oneDecl;
          dp("decl dimension variable with length spec: " + ident);
          declDimensionVariableWithLength(declType, ident,
                                          (FirList)tr.getRight(), (Token)tr.getExtra());
        }
        else if(length instanceof FirList){
          dp("decl dimension variable: " + ident);
          declDimensionVariable(declType, ident, (FirList)length);
        }
        else if(length instanceof Token){
          dp("decl variable with length spec: " + ident);
          declVariableWithLength(declType, ident, (Token)length);
        }
        else{
          printMsgFatal("unimplemented declaration type(unkown)" + ident + "\n" );
        }

        // if this program unit is function and this variable is same name, ...
        Sym s = search(ident);
        if(s != null){
          if(s instanceof Subp && functionType != symRoot.typeVoid){
            if(declType != functionType){
              printMsgFatal("Return value must be same as Function type.");
            }
            continue;
          }
        }
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////
  /** Process external declaration.
   */
  void processExternal(){
    FirList  externalList = f7Sym.externalList;
    Iterator it           = externalList.iterator();

    while (it.hasNext()){
      Token idToken = (Token)it.next();
      String id     = idToken.getLexem();
      Sym    lSym   = search(id);
      Type   type;

      if (lSym == null){
        type = getImplicitType(id);
      }
      else{
        type = lSym.getSymType();
        lSym.remove();
      }

      Subp lSubp = defineSubp(id, type , Sym.SYM_EXTERN, null);
      dp("external subp: " + lSubp);
    }
  }

  ///////////////////////////////////////////////////////////////////
  /** process Dimension statement
   *
   */
  void processDimension(){
    dp("* processDimension");

    Iterator it = f7Sym.dimensionList.iterator();
    while(it.hasNext()){
      Pair p = (Pair)it.next();
      String ident = ((Token)p.getLeft()).getLexem();
      FirList dims = (FirList)p.getRight();
      Sym  s;
      Type type;
      Type vtype;

      if(isDefinedSymbol(ident)){
        s = search(ident);
        if(s == null){
          printMsgFatal("[Dimension] Constant can't be array");
        }
        type = s.getSymType();
        if(type instanceof VectorType){
          VectorType vt = (VectorType)type;
          if(vt.getElemType() == symRoot.typeChar){
            //
          }
          else{
            printMsgFatal("[Dimension] Invalid variable definition"
                          + "at dimension statement : '" + ident + "'");
            continue;
          }
        }
      }
      else{
        s = searchOrAddVar(ident);
        type = getImplicitType(ident);
      }

      vtype = fTypeUtil.getArrayType(type, dims, this);
      if(s != null){
        s.setSymType(vtype);
      }
      else{
        Var v = sym.defineVar(ident, vtype);
      }
      dp("- DIMENSION: " + ident + " as " + vtype);
    }
  }

  void processSave(){
    Iterator it = f7Sym.saveVarsList.iterator();
    while(it.hasNext()){
      FirList varlist = (FirList)it.next();
      if(varlist == null){
        /* ignore */
        continue;
      }
      Iterator nit = varlist.iterator();
      if(nit.hasNext() == false){
        // all variables is save.

        continue;
      }

      while(nit.hasNext()){
        Token t = (Token)nit.next();
        String name = t.getLexem();
        Var v = searchOrAddVar(name);
        v.setStorageClass(Var.VAR_STATIC);
      }
    }
  }


  //////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////
  boolean isBlockVariable(String ident){
    if(fCommonMgr == null){
      return false;
    }
    return fCommonMgr.isBlockVariable(ident);
  }
  Exp makeBlockVariableExp(String ident){
    return fCommonMgr.makeExp(ident);
  }
  Sym symBlockVariable(String ident){
    return fCommonMgr.symBlockVariable(ident);
  }

  //////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////
  boolean isEquivVariable(String ident){
    if(fEquivMgr == null){
      return false;
    }
    return fEquivMgr.isEquivVariable(ident);
  }
  Exp makeEquivVariableExp(String ident){
    return fEquivMgr.makeExp(ident);
  }
  Sym symEquivVariable(String ident){
    return fEquivMgr.symEquivVariable(ident);
  }


  //////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////
  // symbol management.


  /** If the ident is defined as a Var return the Var.
   *  Else define new Var(ident, implicit type) and return it.
   * @param ident
   * @return Var symbol.
   */
  public Var searchOrAddVar(String ident) {
    return searchOrAddVar(ident, null);
  }

  /** If the ident is defined as a Var of type pType return the Var.
   *  Else define new Var(ident, pType) and return it.
   * @param ident
   * @param pType
   * @return Var symbol.
   */
  public Var searchOrAddVar(String ident, Type pType) {
    // Sym lVar = symRoot.symTableCurrent.search(ident);
    Sym lVar = search(ident);
    
    if (lVar instanceof Var) {
      if(pType != null && lVar.getSymType() != pType){
        printMsgRecovered("unmatched type of "+ident);
      }
      return (Var)lVar;
    }
    if(pType == null){
      pType = getImplicitType(ident);
    }

    Var v = sym.defineVar(ident, pType);
    return v;
  }

  
  boolean isParamIdent(String ident){
    FirList args = programHeader.getArgs();
    if(args != null){
      Iterator it = args.iterator();
      while(it.hasNext()){
        String paramid = ((Token)it.next()).getLexem();
        if(paramid == ident){
          return true;
        }
      }
    }
    return false;
  }
  
  /** If the ident is defined as a Sym return the Sym.
   *  Else define new Var(ident, implicit type) and return it.
   * @param ident
   * @return HIR symbol.
   */
  public Sym searchSymOrAddVar(String ident){
    Sym lSym = search(ident);
    if (lSym == null){
      Type type = getImplicitType(ident);
      lSym = sym.defineVar(ident, type);    
    }
    if(isParamIdent(ident) == true){
       return defineParam(ident);
    }
    else{
      return lSym;
    }
  }

  public Sym search(String ident){
    if(isEquivVariable(ident)){
      return symEquivVariable(ident);
    }
    if(isBlockVariable(ident)){
      return symBlockVariable(ident);
    }
    Sym s = symRoot.symTableCurrent.search(ident);

    if(s == null){
      return null;
    }
    else if(s.getSymKind() == Sym.KIND_TYPE){
      return null;
    }
    else{
      // p("----" + s);
      return s;
    }
  }

  public Type searchType(String ident){
    Sym lVar = search(ident);
    if (lVar == null){
      return getImplicitType(ident);
    }
    return lVar.getSymType();
  }

  /** Define a subprogram in the current symbol table.
   *
   * @param id subprogram name
   * @param returnType type of the subprogram
   * @param pParams parameters
   * @return subprogram symbol.
   */
  public Subp defineSubp(String id, Type returnType, int visibility, IrList pParams){
    Subp lSubp = defineSubpWithoutClose(id, returnType, visibility);
    
    if(pParams != null){
      Iterator it = pParams.iterator();
      
      while(it.hasNext()){
        lSubp.addParam((Param)it.next());
      }
    }
    lSubp.closeSubpHeader();
    return lSubp;
  }
  public Subp defineSubpWithoutClose(String id, Type returnType, int visibility){
    Subp lSubp = sym.defineSubp(id, returnType);
    lSubp.setVisibility(visibility);
    return lSubp;
  }

  /**
    get function type
   */
  Type getFunctionType(){
    return functionType;
  }

  ///
  public void setHirBody(BlockStmt blockStmt){
    fSubpDef.setHirBody(blockStmt);
  }

  ///
  HeaderStmt getProgramHeader(){
    return programHeader;
  }

  ///
  void setProgramHeader(HeaderStmt pHeader){
    programHeader = pHeader;
  }

  ExpListExp flattenExpList(ExpListExp e){
    java.util.List list = new LinkedList();
    flattenExpList_r(list, e);
    return (ExpListExp)hir.expList(list);
  }

  void flattenExpList_r(java.util.List list, ExpListExp explist){
    for(int i=0; i<explist.length(); i++){
      Exp e = explist.getExp(i);
      if(e instanceof ExpListExp){
        flattenExpList_r(list, (ExpListExp)e);
      }
      else{
        list.add(e);
      }
    }
  }

  void setInitialValue(Var v, Exp e){
    setInitialValue(v, e, null);
  }

  void addInitialPart(Stmt stmt){
    fInitialPart.addLastStmt((Stmt)stmt.copyWithOperands());
  }

  void setInitialValue(Var v, Exp e, String lexem){
    dp("setInitialValue(v): " + v);
    dp("setInitialValue(e): " + v); 
    coins.ir.hir.HIR hir = fHir.getHir();
    // set initial value
    v.setInitialValue(e);

    //K Important!!(why...?)
    if(e instanceof coins.ir.hir.ExpListExp){
      coins.ir.hir.ExpListExp el = (coins.ir.hir.ExpListExp)e;
      el.setType(v.getSymType());
    }
    
    // initialize COMMON BLOCK Variable?
    if(isBlockVariable(lexem)){
      dp("initialize block var: " + lexem);
      printMsgFatal("Sorry, unsupport Initialize in BLOCK DATA statement.");

      fCommonMgr.setInitialValue(lexem, e);
      
      //SetDataStmt datacode = (SetDataStmt)hir.setDataStmt(hir.varNode(v), e);
      //((Program)hirRoot.programRoot).addInitiationStmt((Stmt)datacode.copyWithOperands());
    }
    else if(v.getStorageClass() == Var.VAR_STATIC){
      // add datacode
      if(e instanceof FortranCharacterExp){
        e = ((FortranCharacterExp)e).getBody();
      }
      SetDataStmt datacode = (SetDataStmt)hir.setDataStmt(hir.varNode(v), e);
      fSubpDef.addInitiationStmt((Stmt)datacode.copyWithOperands());
    }
    else{
      // auto
      if(e instanceof FortranCharacterExp){
        FortranCharacterExp fe = (FortranCharacterExp)e;
        // copy character
        addInitialPart(
          fHirUtil.makeCharacterAssignStmt(
            // body1, body2
            hir.exp(HIR.OP_ADDR, hir.varNode(v)), fe.getBody(),
            // length1
            fTypeUtil.getFortranCharacterLengthExp(
              v.getSymType(), v.getName()),
            // length2
            fe.getLength()));
      }
      else if(fTypeUtil.isComplexType(v.getSymType())){
        ComplexExp ce = fHirUtil.makeComplexExpFromVar(v);
        addInitialPart(
          fHirUtil.makeAssignStmt(ce, e));
      }
      else{
        if(e instanceof ExpListExp){
          Type ptype = sym.pointerType(
            fTypeUtil.getVectorBaseType((VectorType)v.getSymType()));
          e = flattenExpList((ExpListExp)e);
          for(int i=0; i<((ExpListExp)e).length(); i++){
            Exp init_exp = ((ExpListExp)e).getExp(i);
            if(init_exp instanceof NullNode){
              continue;
            }
            if(init_exp != null){
              dp("ptype: " + ptype);
              dp("idx: " + i);
              dp("int: " + init_exp);
              if(init_exp.getType() instanceof VectorType){
                VectorType base_type =
                  (VectorType)fTypeUtil.getVectorBaseType((VectorType)v.getSymType());
                // ptype = sym.pointerType(fTypeUtil.getCharType());
                dp("type->: " + base_type);
                // TODO: Debug
                addInitialPart(
                  fHirUtil.makeCharacterAssignStmt(
                    hir.exp(HIR.OP_ADDR,
                    hir.subscriptedExp(
                      hir.undecayExp(
                        hir.convExp(ptype, hir.exp(HIR.OP_ADDR, hir.varNode(v))),
                        i),
                      fHirUtil.makeIntConstNode(i))),
                    hir.exp(HIR.OP_ADDR, init_exp),
                    base_type.getElemCountExp(),
                    ((VectorType)init_exp.getType()).getElemCountExp()));

              }
              else{
                addInitialPart(
                  fHirUtil.makeAssignStmt(
                    hir.subscriptedExp(
                      hir.undecayExp(
                        hir.convExp(ptype,
                                    hir.exp(HIR.OP_ADDR, hir.varNode(v))),
                        i),
                      fHirUtil.makeIntConstNode(i)),
                    init_exp));
              }

            }
          }
        }
        else{
          if(v instanceof coins.sym.Elem){
            addInitialPart(
              fHirUtil.makeAssignStmt(makeEquivVariableExp(lexem), e));
          }
          else{
            addInitialPart(
              fHirUtil.makeAssignStmt(hir.varNode(v), e));
          }
        }
      }
    }
  }

  Type getSymbolType(String name){
    Var v = (Var)fSymTable.searchLocal(name, Sym.KIND_VAR);
    if(v == null){
      return fImplMgr.getImplicitType(name);
    }
    else{
      return v.getSymType();
    }
  }

  boolean deleteFromSymbolTable(String name){
    fSymTable.searchLocal(name, Sym.KIND_VAR).remove();
    return true;
  }

  boolean isDefinedSymbol(String name){
    if(fSymTable.searchLocal(name, Sym.KIND_VAR) != null ||
       fConstMgr.isConstName(name) == true){
      return true;
    }
    else{
      return false;
    }
  }

  Var defineVar(String lexem){
    Var var = sym.defineVar(lexem, getImplicitType(lexem));
    return var;
  }

  String getProgramUnitName(){
    return programHeader.getLexem();
  }

  boolean isDefinedInLocal(Sym sym){
    dp("isDefinedInLocal ==> " + sym);
    if(fSymTable.searchLocal(sym.getName(), sym.getSymKind()) != null){
      return true;
    }
    else{
      return false;
    }
  }
}

