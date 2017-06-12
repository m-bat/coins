/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

import coins.ir.IrList;
import coins.ir.hir.Exp;
import coins.ir.hir.HIR;
import coins.sym.Sym;
import coins.sym.Type;


/** Binary Expression
 */
public class BinaryNode extends Pair implements HasConstValue{
  // static HIR hir;
  // static FirToHir fHir;
  // static Sym sym;
  private int op;

  public BinaryNode(int op, Node left, Node right, FirToHir pfHir) {
    super(left, right, pfHir);
    this.op = op;
  }
  public void print(int level, String spaces){
    fHir.debugPrint(level, spaces+"<binary:"+opString()+" "+ left + " " + right + ">\n");
    super.print(level, spaces);
  }
  public String toString(){
    // return "<BinaryExp:" + opString() + " " + left + " " + right + ">";
    return "BinaryExp:" + opString();
  }
  public String opString(){
    switch(op){
    case HIR.OP_ADD:    return "+";
    case HIR.OP_AND:    return ".AND.";
    case HIR.OP_CMP_EQ: return "==";
    case HIR.OP_CMP_GE: return ">=";
    case HIR.OP_CMP_GT: return ">";
    case HIR.OP_CMP_LE: return "<=";
    case HIR.OP_CMP_LT: return "<";
    case HIR.OP_CMP_NE: return "!=";
    case HIR.OP_DIV:    return "/";
    case HIR.OP_LG_AND: return ".AND.";
    case HIR.OP_LG_OR:  return ".OR.";
    case HIR.OP_MULT:   return "*";
    case HIR.OP_SUB:    return "-";
    case HIR.OP_XOR:    return ".XOR.";
    default: return "error";
    }
  }

  HirUtility      fHirUtil;
  TypeUtility     fTypeUtil;
  DeclManager     fDeclMgr;
  ExecStmtManager fESMgr;
  HIR hir;

  /** Make HIR Exp node of this binary expression.
   * In case of complex expression make a special ComplexExp node
   * which is composed of a real part expression node and
   * an imaginary part expression node.
   * @return HIR Exp.
   */
  public Exp makeExp(){
    hir = fHir.getHir();

    fHirUtil = fHir.getHirUtility();
    fDeclMgr = fHir.getDeclManager();
    fESMgr   = fHir.getExecStmtManager();
    fTypeUtil = fHir.getTypeUtility();

    Exp lExp1 = left.makeExp();
    Exp lExp2 = right.makeExp();
    
    if (lExp1 instanceof ComplexExp ||
        lExp2 instanceof ComplexExp){

      Exp lExp1Real = null;
      Exp lExp1Imag = null;
      Exp lExp2Real = null;
      Exp lExp2Imag = null;
      Type ctype;
      
      if(lExp1 instanceof DoubleComplexExp ||
         lExp2 instanceof DoubleComplexExp){
        ctype = fTypeUtil.getDoubleType();
      }
      else{
        ctype = fTypeUtil.getRealType();
      }
      
      if(lExp1 instanceof ComplexExp){
        lExp1Real = fHirUtil.castIfNeeded(((ComplexExp)lExp1).getRealPart(),
                                          ctype);
        lExp1Imag = fHirUtil.castIfNeeded(((ComplexExp)lExp1).getImagPart(),
                                          ctype);
      }
      if(lExp2 instanceof ComplexExp){
        lExp2Real = fHirUtil.castIfNeeded(((ComplexExp)lExp2).getRealPart(),
                                          ctype);
        lExp2Imag = fHirUtil.castIfNeeded(((ComplexExp)lExp2).getImagPart(),
                                          ctype);
      }
      
      
      if(lExp1Real == null){
        lExp1Real = fHirUtil.castIfNeeded(lExp1, ctype);
        lExp1Imag = fHirUtil.makeTyped0Node(ctype);
      }
      if(lExp2Real == null){
        lExp2Real = fHirUtil.castIfNeeded(lExp2, ctype);
        lExp2Imag = fHirUtil.makeTyped0Node(ctype);
      }
      
      lExp2Real = fHirUtil.castIfNeeded(lExp2Real, lExp1Real.getType());
      lExp2Imag = fHirUtil.castIfNeeded(lExp2Imag, lExp1Imag.getType());
      
      switch (op){
      case HIR.OP_ADD:
      case HIR.OP_SUB:
        return fHirUtil.makeComplexExpByType(
          hir.exp(op, lExp1Real, lExp2Real),
          hir.exp(op, lExp1Imag, lExp2Imag), ctype);
        
      case HIR.OP_MULT:
        Exp realPart = hir.exp(HIR.OP_SUB,
                               hir.exp(HIR.OP_MULT, lExp1Real, lExp2Real),
                               hir.exp(HIR.OP_MULT, lExp1Imag, lExp2Imag));
        Exp imagPart = hir.exp(HIR.OP_ADD,
                               hir.exp(HIR.OP_MULT, lExp1Real, lExp2Imag),
                               hir.exp(HIR.OP_MULT, lExp1Imag, lExp2Real));
        return fHirUtil.makeComplexExpByType(realPart, imagPart, ctype);
        
      case HIR.OP_DIV:
        realPart = hir.exp(HIR.OP_ADD,
                           hir.exp(HIR.OP_MULT, lExp1Real, lExp2Real),
                           hir.exp(HIR.OP_MULT, lExp1Imag, lExp2Imag));
        imagPart = hir.exp(HIR.OP_SUB,
                           hir.exp(HIR.OP_MULT, lExp1Imag, lExp2Real),
                           hir.exp(HIR.OP_MULT, lExp1Real, lExp2Imag));
        Exp divisor = hir.exp(HIR.OP_ADD,
                              hir.exp(HIR.OP_MULT, lExp2Real, lExp2Real),
                              hir.exp(HIR.OP_MULT, lExp2Imag, lExp2Imag));
        return fHirUtil.makeComplexExpByType(
          hir.exp(HIR.OP_DIV, realPart, divisor),
          hir.exp(HIR.OP_DIV, imagPart, divisor), ctype);

      /* compare */
      case HIR.OP_CMP_EQ:
      case HIR.OP_CMP_NE:
        Exp cmpExp = op == HIR.OP_CMP_EQ ?
          fHirUtil.makeTrueConstNode() :
          fHirUtil.makeFalseConstNode();
        
        return
          hir.exp(HIR.OP_CMP_EQ,
                  hir.exp(HIR.OP_AND,
                          hir.exp(HIR.OP_CMP_EQ, lExp1Real, lExp2Real),
                          hir.exp(HIR.OP_CMP_EQ, lExp1Imag, lExp2Imag)),
                  cmpExp);

      case HIR.OP_CMP_LE:
      case HIR.OP_CMP_LT:
      case HIR.OP_CMP_GE:
      case HIR.OP_CMP_GT:
        fHir.printMsgFatal("Can not compare between complex variables");
        return null;
      default:
        fHir.printMsgFatal("unkown complex operation:" + op);
        return null;

      }
    }

    if(lExp1 instanceof FortranCharacterExp){
      return character_operation((FortranCharacterExp)lExp1,
                                 (FortranCharacterExp)lExp2);
    }


    // Complex, Character
    Type exp1Type = lExp1.getType();
    Type exp2Type = lExp2.getType();

    switch (op) {
    case HIR.OP_ADD: // case real or integer
    case HIR.OP_SUB:
    case HIR.OP_MULT:
    case HIR.OP_DIV:
    case HIR.OP_CMP_EQ:
    case HIR.OP_CMP_NE:
    case HIR.OP_CMP_GT:
    case HIR.OP_CMP_GE:
    case HIR.OP_CMP_LT:
    case HIR.OP_CMP_LE:
      if ((exp1Type.isFloating() || exp1Type.isInteger()) &&
          (exp2Type.isFloating() || exp2Type.isInteger()) ) {
        int rank1 = exp1Type.getTypeRank();
        int rank2 = exp2Type.getTypeRank();
        if ( rank1 > rank2 )
          lExp2 = hir.convExp(exp1Type, lExp2);
        else if ( rank1 < rank2 )
          lExp1 = hir.convExp(exp2Type, lExp1);
      } break;
    case HIR.OP_AND: // case logical
    case HIR.OP_OR:
    case HIR.OP_LG_AND:
    case HIR.OP_LG_OR:
    case HIR.OP_XOR:
      if (exp1Type.getTypeKind() != Type.KIND_BOOL ||
          exp2Type.getTypeKind() != Type.KIND_BOOL ){
        fHir.printMsgRecovered("must be Logical");
      }
      FStmt currentStmt = fESMgr.getCurrentStmt();
      break;
    default: fHir.printMsgRecovered("unknown operation");
    }
    return hir.exp(op, lExp1, lExp2);
  }
  
  
  
  
  
  Exp character_operation(FortranCharacterExp e1, FortranCharacterExp e2){
    // s_cmp(a, b, 10L, 10L) == 0; # eq
    fHir.dp("character_operation: " + e1 + opString() + e2);
    Exp s_cmp = fHirUtil.makeSubpNode(
      "s_cmp", Parser.INTEGER, hir.irList(), Sym.SYM_EXTERN);
    IrList args = hir.irList();
    args.add(hir.exp(HIR.OP_ADDR, e1.getBody()));
    args.add(hir.exp(HIR.OP_ADDR, e2.getBody()));
    args.add(e1.getLength());
    args.add(e2.getLength());

    return hir.exp(op,
                   hir.functionExp(s_cmp, args),
                   fHirUtil.makeConstInt0Node());
  }

  /** Make HIR Exp node from this expression
   * as a call-by-address parameter in pCallStmt.
   *
   * @param pCallStmt a call statement or a function call expression
   * @return HIR Exp.
   */
  public Exp makeArgAddr(FStmt pCallStmt){
    return fHir.getHirUtility().makeArgAddr(pCallStmt, makeExp());
  }

  public FNumber getConstValue(){
    FNumber li = null,ri = null;
    //fHir.p(left.toString());fHir.p(right.toString());

    if(left instanceof HasConstValue){
      li = ((HasConstValue)left).getConstValue();
    }
    if(right instanceof HasConstValue){
      ri = ((HasConstValue)right).getConstValue();
    }
    if(li != null && ri != null){
      switch(op){
      case HIR.OP_ADD:  return li.add(ri);
      case HIR.OP_SUB:  return li.sub(ri);
      case HIR.OP_MULT: return li.mul(ri);
      case HIR.OP_DIV:  return li.div(ri);
      }
    }
    return null;
  }
}
