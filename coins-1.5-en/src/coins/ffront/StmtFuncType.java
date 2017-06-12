/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

import coins.sym.SubpTypeImpl;
import coins.sym.Type;

/** Statement-function type
 */
public class StmtFuncType extends SubpTypeImpl{
  private AssignOrFuncStmt source; // statement-function defining statement
  private Type returnType;  // function type
  public StmtFuncType(AssignOrFuncStmt pSource, Type pReturnType, FirToHir pfHir) {
    super(pfHir.getSymRoot(), null, pReturnType, null, false, false);
    source = pSource;
    returnType = pReturnType;
  }
  public AssignOrFuncStmt getSource() {
     return source;
  }
  public FirList getFParams(){
    return (FirList)((Pair)source.getLeft()).getRight();
  }
  public Node getRight() {
    return source.getRight();
  }
}
