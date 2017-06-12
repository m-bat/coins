/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

import coins.ir.hir.Exp;
import coins.sym.Type;

/** DoubleComplex expression
 */
public class DoubleComplexExp extends ComplexExp{
  public DoubleComplexExp(Exp realExp, Exp imagExp, FirToHir pfHir) {
    super(realExp, imagExp, pfHir);
    /*
    fRealPart = realExp;
    if(imagExp != null){
      fImagPart = imagExp;
    }
    else{
      fImagPart = pfHir.getHirUtility().makeConstReal0Node();
    }
     */
    fHir      = pfHir;
    super.setType(pfHir.getTypeUtility().getComplexDoubleStructType());
  }

  Type getElemType(){
    TypeUtility typeutil = fHir.getTypeUtility();
    return typeutil.getRealType();
  }

  public DoubleComplexExp(ComplexExp cExp, FirToHir pfHir) {
    super(cExp.getRealPart(), cExp.getImagPart(), pfHir);
    fHir      = pfHir;
    super.setType(pfHir.getTypeUtility().getComplexDoubleStructType());
  }

  public Exp getRealPart(){
    return fRealPart;
  }

  public Exp getImagPart(){
    return fImagPart;
  }
  
  public String toString(){
    return "DoubleComplexExp";
  }
}

