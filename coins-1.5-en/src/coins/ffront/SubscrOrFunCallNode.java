/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

import java.util.Iterator;
import java.util.LinkedList;

import coins.ir.IrList;
import coins.ir.hir.Exp;
import coins.ir.hir.HIR;
import coins.ir.hir.Stmt;
import coins.sym.Param;
import coins.sym.PointerType;
import coins.sym.StructType;
import coins.sym.Subp;
import coins.sym.SubpType;
import coins.sym.Sym;
import coins.sym.SymTable;
import coins.sym.Type;
import coins.sym.Var;
import coins.sym.VectorType;

/** Subscripted variable reference or function call
 *
 */
public class SubscrOrFunCallNode extends Pair{

  // left:  function name or array name (Token)
  // right: actual parameter list (FirList)
  private String ident;
  //  private Sym fSym;
  private IrList aParamList;  // actual parameter list of statement-function call

  HirUtility      fHirUtil;
  TypeUtility     fTypeUtil;
  DeclManager     fDeclMgr;
  ExecStmtManager fESMgr;
  IntrinsicUtility fIntrUtil;
  HIR hir;

  public SubscrOrFunCallNode(Token t, FirList list, FirToHir pfHir) {
    super(t, list, pfHir);
    fIntrUtil = pfHir.getIntrinsicUtility();
    hir       = fHir.getHir();
  }

  public String getIdent(){
    return  ((Token)left).getLexem();
  }

  /** Translate this to HIR Exp node.
   *
   * @return HIR Exp node.
   */
  public Exp makeExp(){
    ident    = getIdent();

    fHirUtil  = fHir.getHirUtility();
    fDeclMgr  = fHir.getDeclManager();
    fESMgr    = fHir.getExecStmtManager();
    fTypeUtil = fHir.getTypeUtility();

    Sym fSym = fDeclMgr.search(ident);

    if(fIntrUtil.isIntrinsicCall(ident) &&
       !(fSym != null && fSym.getSymKind() == Sym.KIND_PARAM)){

      if(fSym != null && fSym.getSymKind() != Sym.KIND_SUBP){
        fSym.remove();
      }
      dp("- makeExp: intrisic function call: " + ident);
      return fIntrUtil.makeIntrinsicCall(ident, (FirList)right);
    }

    ///////////////////////////////////////////////////////
    if (fSym == null){
      // external function call
      dp("- makeExp: implicit external function call: " + ident);
      return makeNewFunCall(fDeclMgr.getImplicitType(ident));
    }
    //////////////////////////////////////////////////////
    Type fSymType = fSym.getSymType();

    switch(fSym.getSymKind()){
    case Sym.KIND_PARAM:
      fSymType = ((PointerType)fSymType).getPointedType();
    case Sym.KIND_VAR:
    case Sym.KIND_ELEM:
      {
        if (fSymType instanceof VectorType){
          dp("- makeExp: subscripted variable");
          return makeSubscripted();
        }
        if (fSym instanceof Param){
          return makeParamFunCall((Param)fSym);
        }
        else{
          // function name was declared as a variable to define the return type
          fSym.remove(); // to redefine the name

          dp("- makeExp: function call");
          return makeNewFunCall(fSymType);
        }
      }
    case Sym.KIND_SUBP:
      dp("- makeExp: makeFunCall");
      return makeFunCall(fSym);
    default:
      fHir.printMsgFatal("undefined : "+left);
      return null;
    }
  }

  Exp makeParamFunCall(Param param){
    IrList aParamList = makeArgList();
    IrList fParamTypeList = hir.irList();
    
    return hir.functionExp(hir.varNode(param), aParamList);
  }

  /** Define an external function and make Exp node for the external function call.
   * @param returnType type of return value
   * @return external function call.
   */
  Exp makeNewFunCall(Type returnType){
    HIR hir = fHir.getHir();
    Sym sym = fHir.getSym();
    IrList aParamList = makeArgList();
    IrList fParamTypeList = hir.irList();
    Subp lSubp;
    Type lSubpType;

    // Complex type
    if (fTypeUtil.isComplexType(returnType)){
      dp("- makeNewFunCall: complex type");
      // change "funcName(...)" to "CALL funcName_(dummyVar,...); dummyVar"
      String dummyName = fESMgr.getTempName();
      // Type expType = fTypeUtil.getComplexStructType(returnType);
      Type expType = returnType;
      Var dummyVar = sym.defineVar(dummyName, expType);
      Exp dummyExp = hir.varNode(dummyVar);

      aParamList.add(0, hir.exp(HIR.OP_ADDR, dummyExp));
      String id = ((Token)left).changeLexem();
      Sym lSym = fDeclMgr.search(id);

      if ( lSym == null ){
        // funcName_ is not defined
        lSubp = fDeclMgr.defineSubp(id, fTypeUtil.getVoidType(), Sym.SYM_PUBLIC, null);
      }
      else{  // funcName_ is already defined
        lSubp = (Subp)lSym;
      }

      // set subprogram type (is this necessary ?)
      
      Stmt stmt = hir.callStmt(fHirUtil.makeSubpExp(lSubp), aParamList);
      fESMgr.getCurrentStmt().addGeneratedStmt(stmt);
      return fHirUtil.makeComplexExp(hir.varNode(dummyVar));
    }
    else{
      lSubp = fDeclMgr.defineSubp( ident, returnType, Sym.SYM_PUBLIC, null);
      return hir.functionExp(fHirUtil.makeSubpExp(lSubp), aParamList);
    }
  }

  /** Translate FirList actual parameter list to HIR IrList.
   *
   * @return actual parameter list in HIR.
   */
  IrList makeAParams(){
    IrList aParamList = fHir.getHir().irList();
    if(right != null){
      // func(...)
      Iterator it = ((FirList)right).iterator();
      while (it.hasNext()){
        Exp lExp = ((Node)it.next()).makeExp();
        aParamList.add(lExp);
      }
    }
    else{
      // func()
    }
    return aParamList;
  }

  /** Make type list (IrList) from Token list (FirList)
   *
   * @param pList Token list
   * @return type list.
   */
  IrList makeFParamTypes(FirList pList){
    IrList fParamTypeList = fHir.getHir().irList();

    Iterator it = pList.iterator();
    while (it.hasNext()){
      Token paramName = (Token)it.next();
      dp("makeFParamTypes#paramName: " + paramName.getLexem());
      fParamTypeList.add(fDeclMgr.searchType(paramName.getLexem()));
    }


    return fParamTypeList;
  }

  /** Register pamameters of a statement-function to symbol table
   *
   * @param pList Token list of parameters
   * @param fParamList Type list of parameters
   */
  void registerFParams(FirList pList, IrList fParamList){
    Iterator fIt = fParamList.iterator();
    Iterator it  = pList.iterator();
    int index = 0;
    while (it.hasNext()){
      Token paramName = (Token)it.next();
      Type lType = (Type)fIt.next();
      fHir.getSym().defineParam(paramName.getLexem(),
                                new StmtFuncParamType(lType, this, index++, fHir));
    }
  }

  /** Get Exp node of index-th actual parameter of statement-function call.
   *
   * @param index
   * @return index-th actual parameter.
   */
  public Exp getAParamAt(int index){
    return (Exp)aParamList.get(index);
  }


  /**
    Make subscripted exp.
   */
  Exp makeSubscripted(){
    Exp base_exp  = left.makeExp();

    Exp index_exp = null;
    Exp di_exps[] = new Exp[7]; // dimension index expression
    Exp lb_exps[] = new Exp[7]; // lower bound expression

    Type type = base_exp.getType();
    dp("makeSubscripted: "+base_exp);
    dp("base type: "+type);

    FirList dims = (FirList)right;

    int dimnum = 0;
    int dimsize= 0;
    while(type instanceof VectorType && !fTypeUtil.isFortranCharacterType(type)){
      if(((VectorType)type).getLowerBound() != 0 ||
         ((VectorType)type).getLowerBoundExp() != null){
        lb_exps[dimsize] = ((VectorType)type).getLowerBoundExp();
      }
      type = ((VectorType)type).getElemType();
      dimsize++;
    }

    Iterator it = dims.iterator();
    while(it.hasNext()){
      if(dimnum >= dimsize){
        fHir.printMsgFatal("over dimmension size");
        return null;
      }
      di_exps[dimnum++] = ((Node)it.next()).makeExp();
    }
    while(dimnum-->0){
      Exp iexp = di_exps[dimnum];
      if(lb_exps[dimnum] != null){
        iexp = di_exps[dimnum];
      }
      base_exp = hir.subscriptedExp(base_exp, iexp);
    }

    if(type instanceof StructType){
      base_exp = fHirUtil.makeComplexExp(base_exp);
    }
    else if(fTypeUtil.isFortranCharacterType(type)){
      base_exp = fHirUtil.makeFortranCharacterExp(base_exp,
                                                  fTypeUtil.getFortranCharacterLengthExp(type, left));
    }

    return base_exp;
  }

  /** Make Exp node of a statement-function call or an external function call
   *
   * @return call expression.
   */
  Exp makeFunCall(Sym funcSym){
    HIR hir = fHir.getHir();
    SubpType subpType = (SubpType)funcSym.getSymType();

    if (subpType instanceof StmtFuncType){
      // statement-function call
      aParamList = makeAParams();
      AssignOrFuncStmt source = ((StmtFuncType)subpType).getSource();
      FirList fParams = (FirList)((SubscrOrFunCallNode)source.getLeft()).getRight();
      if(fParams == null){
        fParams = new FirList(fHir);
      }
      IrList fParamTypeList = makeFParamTypes(fParams);
      Exp result = null;
      {
        //pushed symbol table
        //SymTable lSymTable = fTypeUtil.pushSymTable(funcSym);
        // K!! why null?
        SymTable lSymTable = fTypeUtil.pushSymTable(null);
        registerFParams(fParams, fParamTypeList);

        result = source.getRight().makeExp();
        fTypeUtil.popSymTable();

        // pop
        fESMgr.getCurrentStmt().setSymTable(lSymTable);
      }

      return result;
    }
    else{
      // external function call
      IrList aParamList = makeArgList();
      Type   fSymType   = subpType;
      fSymType = ((SubpType)fSymType).getReturnType();

      dp("- external function call: " + funcSym + "(type as " + fSymType + ")");

      if (fSymType instanceof StructType){ // Complex type
        // fHir.printMsgFatal("unimplemented complex function call");
        return makeNewFunCall(fSymType);
      }
      else{
        //Type checkReturnType = ((Subp)funcSym).getReturnValueType();
        //Exp  functionSpec = fHirUtil.makeSubpExp((Subp)funcSym);
        //Type lType = functionSpec.getType();
        return hir.functionExp(fHirUtil.makeSubpExp((Subp)funcSym), aParamList);
      }
    }
  }

  /** Make Exp node of address of this.
   *
   * @param pCallStmt
   * @return address node.
   */
  public Exp makeArgAddr(FStmt pCallStmt){
    Exp lExp = makeExp();
    // for complex array
    if(lExp instanceof ComplexExp){
      lExp = ((ComplexExp)lExp).getRealPart();
    }

    // Intrinsic
    if(fIntrUtil.isIntrinsicCall(getIdent())){
      return fHirUtil.makeArgAddr(pCallStmt, lExp);
    }

    Sym fSym = fDeclMgr.search(getIdent());
    if (fSym != null){
      switch(fSym.getSymKind()){
      case Sym.KIND_VAR:
      case Sym.KIND_PARAM:
      case Sym.KIND_SUBP:
      case Sym.KIND_ELEM:
        if(lExp instanceof FortranCharacterExp){
          lExp =  ((FortranCharacterExp)lExp).fBody;
        }
        return fHir.getHir().exp(HIR.OP_ADDR, lExp);
        // return fHirUtil.makeArgAddr(pCallStmt, lExp);
      default:
        fHir.printMsgFatal("undefined "+left);
        return null;
      }
    }
    fHir.printMsgFatal("undefined(not found) "+left);return null;
  }

  /** Make IrList of addresses of actual parameters.
   *
   * @return
   */
  private IrList makeArgList(){
    IrList lParamList = fHir.getHir().irList();
    LinkedList strparam_list = new LinkedList();

    if (right != null){

      // actual parameter
      Iterator it = ((FirList)right).iterator();
      while (it.hasNext()){
        Node lParam     = ((Node)it.next());
        Exp  exp        = lParam.makeExp();
        Exp  lParamAddr = lParam.makeArgAddr(fESMgr.getCurrentStmt());

        if(lParamAddr != null){
          /**
           * K!!
           */
          lParamList.add(lParamAddr);
          if(exp instanceof FortranCharacterExp){
            strparam_list.add(((FortranCharacterExp)exp).getLength());
          }
        }
        else{
          fHir.printMsgFatal("error in actual argument");
        }
      }
      // character length if need
      it = strparam_list.iterator();
      while (it.hasNext()){
        Exp length = (Exp)it.next();
        lParamList.add(length);
      }
    }
    return lParamList;
  }
  void dp(String str){
    fHir.dp(str);
  }
}


