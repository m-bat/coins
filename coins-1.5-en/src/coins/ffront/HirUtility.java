/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

import coins.HirRoot;
import coins.SymRoot;
import coins.ir.IrList;
import coins.ir.hir.BlockStmt;
import coins.ir.hir.ConstNode;
import coins.ir.hir.ElemNode;
import coins.ir.hir.Exp;
import coins.ir.hir.HIR;
import coins.ir.hir.PointedExp;
import coins.ir.hir.QualifiedExp;
import coins.ir.hir.Stmt;
import coins.ir.hir.SubscriptedExp;
import coins.ir.hir.VarNode;
import coins.sym.Label;
import coins.sym.PointerType;
import coins.sym.StructType;
import coins.sym.Subp;
import coins.sym.Sym;
import coins.sym.Type;
import coins.sym.Var;
import coins.sym.VectorType;

/**
  HIR wrapper class
 */
public class HirUtility{
  FirToHir fHir;
  TypeUtility fTypeUtil;

  Sym sym;                 // an instance to generate Sym objects
  HIR hir;                 // an instance to generate HIR objects
  SymRoot symRoot;  // root of symbol information
  HirRoot hirRoot;

  public HirUtility(FirToHir fth, HIR h){
    fHir = fth;
    sym  = fth.getSym();
    hir  = fth.getHir();
    symRoot = fHir.getSymRoot();
    hirRoot = fHir.getHirRoot();
  }

  
  /** Make a call-by-address node from pExp.
   *  Make a dummy variable v, and assignment node v = pExp.
   *  Then return a node of address of v.
   * @param pCallStmt
   * @param pExp
   * @return call-by-address node.
   */
  public Exp makeArgAddr(FStmt pCallStmt, Exp pExp){
    Type expType = pExp.getType();

    dp("HirUtility#makeArgAddr: " + pExp + " as " + expType);
    if(pExp instanceof FortranCharacterExp){
      return ((FortranCharacterExp)pExp).getBody();
    }
    else if(expType instanceof VectorType){
      return hir.exp(HIR.OP_ADDR, pExp);
    }
    else if(expType instanceof PointerType){
      return pExp;
    }
    else if(pExp instanceof VarNode){
      return hir.exp(HIR.OP_ADDR, pExp);
    }

    Exp dummyExp;
    String dummyName = fHir.getExecStmtManager().getTempName();

    if (pExp instanceof DoubleComplexExp){
      expType  = fTypeUtil.getComplexDoubleStructType();
      dummyExp = hir.varNode(sym.defineVar(dummyName, expType));
      DoubleComplexExp ce = makeDoubleComplexExp(dummyExp);
      
      pCallStmt.addGeneratedStmt(
        makeAssignStmt(
          ce.getImagPart(),
          ((DoubleComplexExp)pExp).getImagPart()));
      pCallStmt.addGeneratedStmt(
        makeAssignStmt(
          ce.getRealPart(),
          ((DoubleComplexExp)pExp).getRealPart()));
    }
    else if (pExp instanceof ComplexExp){
      expType  = fTypeUtil.getComplexStructType();
      dummyExp = hir.varNode(sym.defineVar(dummyName, expType));
      ComplexExp ce = makeComplexExp(dummyExp);

      pCallStmt.addGeneratedStmt(makeAssignStmt(
        ce.getImagPart(),
        ((ComplexExp)pExp).getImagPart()));
      pCallStmt.addGeneratedStmt(makeAssignStmt(
        ce.getRealPart(),
        ((ComplexExp)pExp).getRealPart()));
    }
    else{
      dummyExp = hir.varNode(sym.defineVar(dummyName, expType));
      pCallStmt.addGeneratedStmt(makeAssignStmt(dummyExp, pExp));
    }
    return hir.exp(HIR.OP_ADDR, dummyExp);
  }

  DoubleComplexExp makeDoubleComplexExp(Exp pExp){
    ElemNode elemNode;
    StructType type = (StructType)pExp.getType();

    elemNode    = hir.elemNode(fTypeUtil.getRealPart(type));
    Exp realExp = hir.qualifiedExp(pExp, elemNode);

    elemNode    = hir.elemNode(fTypeUtil.getImagPart(type));
    Exp imagExp = hir.qualifiedExp(pExp, elemNode);
    return new DoubleComplexExp(realExp, imagExp, fHir);
  }

  /** Make ComplexExp node from Exp node of _complex_struct.
   *     typedef struct { float _real; float _imag; } _complex_struct;
   * @param pExp Exp node of _complex_struct
   * @return complex exp node.
   */
  ComplexExp makeComplexExp(Exp pExp){
    ElemNode elemNode;
    StructType type = (StructType)pExp.getType();

    elemNode    = hir.elemNode(fTypeUtil.getRealPart(type));
    Exp realExp = hir.qualifiedExp(pExp, elemNode);

    elemNode    = hir.elemNode(fTypeUtil.getImagPart(type));
    Exp imagExp = hir.qualifiedExp(pExp, elemNode);

    if(type == fTypeUtil.getComplexDoubleStructType()){
      return new DoubleComplexExp(realExp, imagExp, fHir);
    }
    else{
      return new ComplexExp(realExp, imagExp, fHir);
    }
  }

  ComplexExp makeComplexExpByType(Exp rexp, Exp iexp, Type type){
    if(type == fTypeUtil.getDoubleType()){
      return new DoubleComplexExp(rexp, iexp, fHir);
    }
    else{
      return new ComplexExp(rexp, iexp, fHir);
    }
  }

  ComplexExp makeComplexExpFromVar(Var v){
    Type t = v.getSymType();
    if(t == fTypeUtil.getComplexStructType()){
      return makeComplexExp(hir.varNode(v));
    }
    else if(t == fTypeUtil.getComplexDoubleStructType()){
      return makeDoubleComplexExp(hir.varNode(v));
    }
    else{
      return null;
    }
  }

  public Exp makeNullNode(){
    return hir.nullNode(); 
  }
  
  public Exp makeConstInt1Node(){
    return hir.constNode(symRoot.intConst1);
  }
  public Exp makeConstInt0Node(){
    return hir.constNode(symRoot.intConst0);
  }
  public Exp makeConstReal0Node(){
    return hir.constNode(symRoot.floatConst0);
  }
  public Exp makeConstDouble0Node(){
    return hir.constNode(symRoot.doubleConst0);
  }
  public Exp makeConstReal1Node(){
    return hir.constNode(sym.floatConst("1.0", symRoot.typeFloat));
  }
  public Exp makeIntConstNode(String lexem){
    return hir.constNode(sym.intConst(lexem, symRoot.typeInt));
  }
  public Exp makeIntConstNode(int num){
    return hir.constNode(sym.intConst(String.valueOf(num), symRoot.typeInt));
  }
  public Exp makeLongConstNode(int num){
	return hir.constNode(sym.intConst(String.valueOf(num), symRoot.typeLong));
  }
  public Exp makeFloatConstNode(String lexem){
    return hir.constNode(sym.floatConst(lexem, symRoot.typeFloat));
  }
  public Exp makeDoubleConstNode(String lexem){
    return hir.constNode(sym.floatConst(lexem, symRoot.typeDouble));
  }
  public Exp makeTrueConstNode(){
    return hir.constNode(symRoot.boolConstTrue);
  }
  public Exp makeFalseConstNode(){
    return hir.constNode(symRoot.boolConstFalse);
  }
  public FortranCharacterExp makeCharsConstNode(String lexem){
    return makeFortranCharacterExp(
      hir.decayExp(hir.constNode(sym.stringConst(lexem))),
      makeIntConstNode(lexem.length()));
  }

  public FortranCharacterExp makeFortranCharacterExp(Exp body, Exp len){
    dp("FortranCharacterExp: " + body);
    dp("FortranCharacterExp: " + body.getType());
    if(!(body.getType() instanceof PointerType)){
      body = hir.decayExp(body);
    }
    dp("FortranCharacterExp: " + body.getType());
    return new FortranCharacterExp(body, len, fHir);
  }

  /**
   * Add type conversion node if necessary.
   * Convert pExp2 to type of pExp1.
   *
   * @param pExp1
   * @param pExp2
   * @return type-converted node.
   */
  public Exp checkAssignExpType(Exp pExp1, Exp pExp2){
    Type exp1Type = pExp1.getType();
    Type exp2Type = pExp2.getType();

    // pointer: ptr = 0 : pass
    if(exp1Type instanceof PointerType &&
       pExp2    instanceof ConstNode &&
       ((ConstNode)pExp2).getIntValue() == 0){
      return pExp2;
    }

    if(exp1Type == exp2Type ||
       (pExp1 instanceof ComplexExp &&
        pExp2 instanceof ComplexExp)){
      return pExp2;
    }
    else{
      if ((exp1Type.isFloating() || exp1Type.isInteger()) &&
          (exp2Type.isFloating() || exp2Type.isInteger())) {
        return hir.convExp(exp1Type, pExp2);
      }
      else if(pExp2 instanceof ComplexExp){
        return checkAssignExpType(pExp1, ((ComplexExp)pExp2).getRealPart());
      }
      else if(pExp1 instanceof ComplexExp){
        return new ComplexExp(pExp2, makeConstReal0Node(), fHir);
      }

      dp("illegal assignment (pExp1): " + pExp1);
      dp("illegal assignment (pExp2): " + pExp2);
      // error
      fHir.printMsgError("illegal assignment: " +
                         "(left type: " + exp1Type + ", right type: " + exp2Type + ")");

      return pExp2;
    }
  }

  /** return checked assign statement
   */
  public Stmt makeAssignStmt(Exp lExp1, Exp lExp2){
    // lExp1
    if(!((lExp1 instanceof VarNode)        ||
         (lExp1 instanceof SubscriptedExp) ||
         (lExp1 instanceof ComplexExp)     ||
         (lExp1 instanceof QualifiedExp)   ||
         (lExp1 instanceof PointedExp)     ||
         (lExp1.getOperator() == HIR.OP_CONTENTS)
         )){
      //K
      fHir.printMsgFatal("left value must be assinable : " + lExp1);
      return null;
    }

    lExp2 = checkAssignExpType(lExp1, lExp2);

    if (lExp1 instanceof ComplexExp){
      BlockStmt bstmt = hir.blockStmt(null);
      bstmt.addLastStmt(makeAssignStmt(((ComplexExp)lExp1).getImagPart(),
                                       ((ComplexExp)lExp2).getImagPart()));
      bstmt.addLastStmt(makeAssignStmt(((ComplexExp)lExp1).getRealPart(),
                                       ((ComplexExp)lExp2).getRealPart()));
      return bstmt;
    }
    else if (lExp2 instanceof ComplexExp){
      return makeAssignStmt(lExp1,
                            castIfNeeded(
                              ((ComplexExp)lExp2).getRealPart(),
                              lExp1.getType()));
    }
    else if (lExp2.getType() == fTypeUtil.getBoolType()){
      return hir.assignStmt(lExp1, makeBooleanExp(lExp2));
    }
    else{
      return hir.assignStmt(lExp1,
                             castIfNeeded(lExp2, lExp1.getType()));
    }
  }
  
  /** return boolean checked exp
   *  
   */
  public Exp makeBooleanExp(Exp e){
    int op = e.getOperator();
    if(op == HIR.OP_CMP_EQ ||
       op == HIR.OP_CMP_GE ||
       op == HIR.OP_CMP_GT ||
       op == HIR.OP_CMP_LE ||
       op == HIR.OP_CMP_LT ||
       op == HIR.OP_CMP_NE){
      ExecStmtManager esmgr = fHir.getExecStmtManager();
      Var v = sym.defineVar(esmgr.getTempName("booleantemp"),
                            fTypeUtil.getBoolType());
      esmgr.currentStmt.addGeneratedStmt(
        makeIfStmt(e,
                   hir.assignStmt(hir.varNode(v), makeTrueConstNode()),
                   hir.assignStmt(hir.varNode(v), makeFalseConstNode())));

      return hir.varNode(v);
    }
    else{
      return e;
    }
  }

  /** return character assign exp
   */
  public Stmt makeCharacterAssignStmt(Exp e1, Exp e2, Exp l1, Exp l2){
    Exp s_copy = makeSubpNode("s_copy", Parser.INTEGER, hir.irList(), Sym.SYM_EXTERN);

    dp("CharacterAssign: " + e1 + " = " + e2);

    IrList args = hir.irList();
    args.add(e1); args.add(e2);
    args.add(l1); args.add(l2);

    return hir.callStmt(s_copy, args);
  }



  /**
    Make subprogram node
   */
  public Exp makeSubpNode(String pSubpName, int pType,
                          IrList pParamList, int visibility){
    Type returnType = fTypeUtil.getType(pType);
    Subp lSubp = sym.defineSubp(pSubpName.intern(), returnType);
    lSubp.setVisibility(Sym.SYM_PUBLIC);
    lSubp.closeSubpHeader();
    
    return makeSubpExp(lSubp);
  }

  /** make subpNode to pointer of subp node
   *
   * SubpNode -> &(SubpNode)
   */
  public Exp makeSubpExp(Subp subp){
    return hir.exp(HIR.OP_ADDR,
                   hir.subpNode(subp));
  }

  public Exp convToDouble(Exp pExp){
    Type type = pExp.getType();
    if (type.getTypeKind() == Type.KIND_DOUBLE)
      return pExp;
    else
      return hir.convExp(symRoot.typeDouble, pExp);
  }

  /**
    qualified assign
   */
  Stmt qassign(Var var, String ident, Exp exp){
    ElemNode elem = hir.elemNode(fTypeUtil.searchElem(ident, var.getSymType()));
    if(elem == null){
      fHir.printMsgFatal("no elem: " + ident);
    }
    return makeAssignStmt(hir.qualifiedExp(hir.varNode(var), elem), exp);
  }

  /**
    cast to integer.
   */
  Exp castToInteger(Exp exp){
    dp("--" + exp);
    Type type = exp.getType();
    if(exp instanceof ComplexExp){
      ComplexExp cexp = (ComplexExp)exp;
      return hir.convExp(fTypeUtil.getIntType(), cexp.getRealPart());
    }
    else if(!type.isInteger()){
      return hir.convExp(fTypeUtil.getIntType(), exp);
    }
    return exp;
  }
  
  /**
   * return more complex type between two exp.
   */
  Type checkTwoExpType(Exp arg1, Exp arg2){
    Type t1 = arg1.getType();
    Type t2 = arg2.getType();
    Type check;

    check = fTypeUtil.getDoubleType();
    if(t1 == check || t2 == check){
      return check;
    }

    check = fTypeUtil.getRealType();
    if(t1 == check || t2 == check){
      return check;
    }

    check = fTypeUtil.getIntType();
    if(t1 == check && t2 == check){
      return check;
    }
    else{
      dp("unreachable");
      return null;
    }
  }

  /**
   * return cast expression if cast is needed
   */
  Exp castIfNeeded(Exp exp, Type type){
    if(type == exp.getType()){
      return exp;
    }
    else{
      return hir.convExp(type, exp);
    }
  }

  Exp makeTyped0Node(Type type){
    Exp exp = makeConstInt0Node();
    if(type == fTypeUtil.getIntType()){
      return exp;
    }
    else{
      return castIfNeeded(exp, type);
    }
  }
  
  ////
  void dp(String msg){
    fHir.dp(msg);
  }

  
  /**
    make if statement. it's treat with 'and' 'or' like:

    <pre>
    if EXP1 and EXP2
      T
    else
      F
    end
    
    =>
    IF EXP1
      IF EXP2
        T
      END
      goto ELSE_L
    ELSE
      ELSE_L:
      E
    END
    
    if EXP1 or EXP2
      T
    else
      E
    end

    =>
    IF EXP1
      THEN_L:
      T
    ELSE
      IF EXP2
        goto THEN_L
      END
      E
    END
    </pre>
   */
  
  Stmt makeIfStmt(Exp cond, Stmt thenStmt, Stmt elseStmt){
    int op = cond.getOperator();
    
    if(op == HIR.OP_AND){
      BlockStmt elseBlock = hir.blockStmt(null);
      Label label = fHir.getExecStmtManager().makeNewLabel("if_elseBlock_label_");
      
      elseBlock.addLastStmt(hir.labeledStmt(label, null));
      elseBlock.addLastStmt(elseStmt);
      
      return makeIfStmt(cond.getExp1(),
                        makeIfStmt(cond.getExp2(),
                                   thenStmt,
                                   hir.jumpStmt(label)),
                        elseBlock);

    }
    else if(op == HIR.OP_OR){
      BlockStmt thenBlock = hir.blockStmt(null);
      BlockStmt elseBlock = hir.blockStmt(null);
      
      Label label = fHir.getExecStmtManager().makeNewLabel("if_thenBlock_label_");
      thenBlock.addLastStmt(hir.labeledStmt(label, null));
      thenBlock.addLastStmt(thenStmt);

      elseBlock.addLastStmt(makeIfStmt(cond.getExp2(),
                                   hir.jumpStmt(label),
                                   null));                        
      elseBlock.addLastStmt(elseStmt);
      
      return makeIfStmt(cond.getExp1(),
                        thenBlock,
                        elseBlock);
    }
    else if(op == HIR.OP_CMP_NE ||
            op == HIR.OP_CMP_EQ ||
            op == HIR.OP_CMP_GT ||
            op == HIR.OP_CMP_GE ||
            op == HIR.OP_CMP_LT ||
            op == HIR.OP_CMP_LE){
      return hir.ifStmt(cond, thenStmt, elseStmt);
    }

    else if(op == HIR.OP_XOR){
      Exp e2 = cond.getExp2();

      if(e2 instanceof ConstNode){
        ConstNode ce2 = (ConstNode)e2;
        if(ce2.getIntValue() == 1){
          return makeIfStmt(cond.getExp1(), elseStmt, thenStmt);
        }
        else if(ce2.getIntValue() == 0){
          return makeIfStmt(cond.getExp1(), thenStmt, elseStmt);
        }
        else{
          //K Error
          return null;
        }
      }
      else{
        return hir.ifStmt(hir.exp(HIR.OP_CMP_NE, cond, makeConstInt0Node()),
                          thenStmt, elseStmt);
      }
    }
    
    /* for LIR */
    /* ------- */

    
    else{
      return hir.ifStmt(hir.exp(HIR.OP_CMP_NE, cond, makeConstInt0Node()),
                        thenStmt, elseStmt);
    }
  }
}

