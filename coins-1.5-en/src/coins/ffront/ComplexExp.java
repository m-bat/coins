/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

import coins.ir.hir.Exp;
import coins.ir.hir.ExpImpl;
import coins.sym.Type;

/** Complex expression
 */
public class ComplexExp extends ExpImpl{
  protected Exp fRealPart;
  protected Exp fImagPart;
  protected FirToHir fHir;
  
  public ComplexExp(Exp realExp, Exp imagExp, FirToHir pfHir) {
    super(pfHir.getHirRoot());
    fHir      = pfHir;
    
    fRealPart = realExp;
    if(imagExp != null){
      fImagPart =imagExp;
    }
    else{
      fImagPart = pfHir.getHirUtility().makeConstReal0Node();
    }
    
    super.setType(pfHir.getTypeUtility().getComplexStructType());
  }

  Type getElemType(){
    TypeUtility typeutil = fHir.getTypeUtility();
    return typeutil.getRealType();
  }
  
  public Exp getRealPart(){
    return (Exp)fRealPart;
  }

  public Exp getImagPart(){
    return (Exp)fImagPart;
  }
  
  public String toString(){
    return "ComplexExp";
  }
}

