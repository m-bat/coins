/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

import coins.ir.hir.Exp;
import coins.ir.hir.HIR;
import coins.ir.hir.SubscriptedExp;
import coins.sym.PointerType;
import coins.sym.StructType;
import coins.sym.Sym;
import coins.sym.Type;
import coins.sym.VectorType;

/** Assignment statement or statement-function defining statement.
 *
 */
public class AssignOrFuncStmt extends FStmt{
  private Node left; // left hand side of the statement
  private Node right; // right hand expression of the statement
  private Type returnType; // in case of statement function

  public AssignOrFuncStmt(Node pLeft, Node pRight, int line, FirToHir pfHir) {
    super(line, pfHir);
    left  = pLeft;
    right = pRight;
    if(left == null || right == null){
      fHir.printMsgFatal("parser error in assignment statement: " +
                         left + "," + right);
    }
    returnType = null;
  }

  public Node getLeft(){
    return left;
  }

  public Node getRight(){
    return right;
  }

  public void print(int level, String spaces){
    super.print(level, spaces);
    fHir.debugPrint(level, spaces+"AssignOrFunc"+"\n");
    left.print(level, spaces+"  ");
    right.print(level, spaces+"  ");
  }

  public String toString(){
    return super.toString()+"assign statement : <" + left + "> = <" + right + ">";
  }

  /** Make HIR node of assignment statement.
   * COMPLEX type assignment is composed of real part assignment
   * and imaginary part assignment
   */
  public void process(){
    if (isStmtFunc()){
      return;
    }

    //dp(right.getClass().toString());
    //dp(right.toString());

    // str = str1 // str2
    if(right instanceof ConcatNode){
      stmt = ((ConcatNode)right).assignWithConcatString(left);
      super.process();// process optional label and generated statement
      return;
    }

    Exp lExp1 = left.makeExp();
    Exp lExp2 = right.makeExp();

    if(lExp1 instanceof FortranCharacterExp){
      characterAssignment((FortranCharacterExp)lExp1,
                          fTypeUtil.castFortranCharacterExp(lExp2));
      super.process();// process optional label and generated statement
      return;
    }

    stmt = fHirUtil.makeAssignStmt(lExp1, lExp2);
    super.process();// process optional label and generated statement
  }

  void characterAssignment(FortranCharacterExp e1, FortranCharacterExp e2){
    stmt = fHirUtil.makeCharacterAssignStmt(
            hir.exp(HIR.OP_ADDR, e1.getBody()),
            hir.exp(HIR.OP_ADDR, e2.getBody()),
                                            e1.getLength(), e2.getLength());
  }


  /**
    complex?
   */
  Exp complexCheck(Exp e){
    if(e instanceof SubscriptedExp){
      if(e.getType() instanceof StructType){
        // complex
        e = fHirUtil.makeComplexExp(e);
      }
    }

    return e;
  }

  /** Return true if this is a statement-function defining statement.
   *  If lhs is a form of SubscrOrFunCall and its name is undefined
   * or not an array name then this is a statement-function defining statement.
   * In these cases, define this statement function as a SymtFuncType subprogram.
   * @return true if statement function.
   */
  boolean isStmtFunc(){
    if (left instanceof SubscrOrFunCallNode){
      // array or function call
      String ident = ((SubscrOrFunCallNode)left).getIdent();
      Sym leftSym = fDeclMgr.search(ident);

      if (leftSym == null){
        returnType = fDeclMgr.getImplicitType(ident) ;
        leftSym = fDeclMgr.defineSubp(ident, returnType, Sym.SYM_PRIVATE, null);
      }
      else {
        //K check type first
        returnType = leftSym.getSymType();

        if(returnType instanceof VectorType ||
           returnType instanceof PointerType){
          // This is array
          return false;
        }
        leftSym.remove();  // redefine as a subprogram symbol
        leftSym = fDeclMgr.defineSubp(ident, returnType, Sym.SYM_PRIVATE, null);
      }
      leftSym.setSymType(new StmtFuncType(this, returnType, fHir));
      return true;
    }
    return false;
  }
}



