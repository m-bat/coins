/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

import coins.sym.TypeImpl;

public class EntryType extends TypeImpl{

  public EntryType(FirToHir pfHir) {
    super(pfHir.getSymRoot());
  }

  public boolean isInteger(){
    return false;
  }
  public boolean isFloating(){
    return false;
  }
}
