/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

import coins.ir.hir.Exp;

/** Complex constant
 */
public class ComplexConstNode extends Pair implements HasConstValue{
  // Node left; // real part
  // Node right; // imaginary part
  public ComplexConstNode(Node pReal, Node pImag, FirToHir pfHir) {
    super(pReal, pImag, pfHir);
  }

  Node getReal(){
    return left;
  }
  Node getImag(){
    return right;
  }
  
  public void print(int level, String spaces) {
    fHir.debugPrint(level, spaces + "ComplexConst");
    super.print(level, spaces);
  }

  /** Make HIR Exp node of this complex constant.
   *
   * @return  HIR Exp.
   */
  public Exp makeExp(){
    return new ComplexExp(makeRealExp(left), makeRealExp(right), fHir);
  }

  /** Make HIR Exp node from this complex constant
   *  as a call-by-address parameter in pCallStmt.
   *    typedef struct { float _real; float _imag; } _complex_struct;
   *    _complex_struct dummyName;
   *    dummyName._real = left;
   *    dummyName._imag = right;
   *    return Exp node of address of dummyName
   * @param pCallStmt
   * @return HIR Exp. 
   */
  public Exp makeArgAddr(FStmt pCallStmt){
    return fHir.getHirUtility().makeArgAddr(pCallStmt, makeExp());
  }

  Exp makeRealExp(Node pNode){
    Exp lExp = pNode.makeExp();
    if(lExp.getType().isInteger()){
      return fHir.getHir().convExp(fHir.getTypeUtility().getRealType(), lExp);
    }
    return lExp;
  }

  // for get integer(for array size)
  public FNumber getConstValue(){
    if(left instanceof HasConstValue){
      return FNumber.make(((HasConstValue)left).getConstValue().doubleValue(),
                          ((HasConstValue)right).getConstValue().doubleValue());
    }
    return null;
  }
}
