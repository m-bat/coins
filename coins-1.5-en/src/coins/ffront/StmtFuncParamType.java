/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

import coins.sym.Type;
import coins.sym.TypeImpl;

/** Statement-function-parameter type
 *
 */
public class StmtFuncParamType extends TypeImpl{
  private Type baseType;
  private SubscrOrFunCallNode stmtFunCall;
  private int paramIndex;
  public StmtFuncParamType(Type pType,
                           SubscrOrFunCallNode pStmtFunCall,
                           int pParamIndex, FirToHir pfHir){
    super(pfHir.getSymRoot());
    baseType    = pType;
    stmtFunCall = pStmtFunCall;
    paramIndex  = pParamIndex;
  }
  public SubscrOrFunCallNode getStmtFunCall() {
    return stmtFunCall;
  }
  public int getParamIndex() {
    return paramIndex;
  }
}
