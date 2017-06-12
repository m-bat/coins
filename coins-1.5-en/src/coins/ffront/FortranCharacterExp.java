/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

import coins.ir.hir.Exp;
import coins.ir.hir.ExpImpl;

/*
 * Fortran Character Exp
 */

public class FortranCharacterExp extends ExpImpl {
  Exp fBody;
  Exp fLength;
  FirToHir fHir;

  /**
   * @param body Character body exp
   * @param len  length exp
   * @param pHirRoot
   */
  public FortranCharacterExp(Exp body, Exp len, FirToHir fth) {
    super(fth.getHirRoot());
    fBody   = body;
    fLength = len;
  }
  Exp getBody(){
    return fBody;
  }
  Exp getLength(){
    return fLength;
  }
  public String toString(){
    return "[FChars:" + fBody + "]";
  }
}
