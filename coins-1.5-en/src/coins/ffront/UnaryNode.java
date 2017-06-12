/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

import coins.ir.hir.Exp;
import coins.ir.hir.HIR;

public class UnaryNode implements Node, HasConstValue{
  FirToHir fHir;
  int op;
  Node exp;
  public UnaryNode(int op, Node pExp, FirToHir pfHir) {
    this.op = op;
    exp = pExp;
    fHir = pfHir;
  }
  public int getOp(){
    return op;
  }
  public Node getExp(){
    return exp;
  }
  
  public void print(int level, String spaces){
    fHir.debugPrint(level, spaces+"unary:"+opString()+"\n");
    exp.print(level, spaces+"  ");
  }
  public String toString(){
    return "UnaryExp "+opString();
  }
  public String opString(){
    switch(op){
      case HIR.OP_ENCLOSE: return "()";
      case HIR.OP_NEG: return "-";
      case HIR.OP_NOT: return ".NOT.";
      default: return "error";
    }
  }

  /** Make HIR Exp node of this unary expression.
   * In case of complex expression make a special ComplexExp node
   * which is composed of a real part expression node and
   * an imaginary part expression node.
   * @return HIR Exp.
   */
  public Exp makeExp(){
    Exp lExp = exp.makeExp();
    HIR hir = fHir.getHir();
    if (op == HIR.OP_ENCLOSE){
      // unsupported in LIR
      return lExp;
    }
    else if(op == HIR.OP_NOT){
      return hir.exp(HIR.OP_XOR, lExp, fHir.getHirUtility().makeIntConstNode(1));
    }
    else {
      if (lExp instanceof ComplexExp){
        Exp rexp = ((ComplexExp)lExp).getRealPart();
        Exp iexp = ((ComplexExp)lExp).getImagPart();
        
        return
          fHir.getHirUtility().makeComplexExpByType(
            hir.exp(op, rexp), hir.exp(op, iexp), rexp.getType());
      }
      return hir.exp(op, exp.makeExp());
    }
  }

  /** Make HIR Exp node from this expression
   * as a call-by-address parameter in pCallStmt.
   *
   * @param pCallStmt a call statement or a function call expression
   * @return HIR Exp node.
   */
  public Exp makeArgAddr(FStmt pCallStmt){
    return fHir.getHirUtility().makeArgAddr(pCallStmt, makeExp());
  }

  public FNumber getConstValue(){
    FNumber n = null;
    if(exp instanceof HasConstValue &&
       (n = ((HasConstValue)exp).getConstValue()) != null){
      switch(op){
      case HIR.OP_ENCLOSE: return n;
      case HIR.OP_NEG:     return n.neg();
      }
    }
    return null;
  }
}
