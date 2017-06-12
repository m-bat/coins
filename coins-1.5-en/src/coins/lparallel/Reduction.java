/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.lparallel;
import coins.ir.hir.AssignStmt;
import coins.ir.hir.Exp;  //##70
import coins.ir.hir.HIR;  //##70
import coins.ir.hir.VarNode;
///////////////////////////////////////////////////////////////////////////////
//
// [parallel loop] Reduction
//
//  Reduction table.
///////////////////////////////////////////////////////////////////////////////
public class Reduction  {

  /* //##70
    AssignStmt stmt;        // v(DEF) = v(USE) op Exp ...
    VarNode DefVarNode;     // v(DEF)
    VarNode UseVarNode;     // v(USE)
    int op;                 // Op (+)
   */ //##70
    //##70 BEGIN
    public final AssignStmt stmt;    // v(DEF) = v(USE) op Exp ...
    public final VarNode DefVarNode; // v(DEF)
    public final VarNode UseVarNode; // v(USE)
    public final int op;             // Op (+)
    public final String opName; // "max", "min", "maxIndex", "minIndex" //##70
    public final Exp arrayExp;  // Array element exp used to get its max/min index. //##70
    //##70 END
    /**
    *
    * Reduction:
    *
    *
    **/
    public //##70
    Reduction (AssignStmt pstmt,VarNode pDefVarNode,VarNode pUseVarNode,int pop)
    {
        stmt = pstmt;
        DefVarNode = pDefVarNode;
        UseVarNode = pUseVarNode;
        op = pop;
        opName = "";     //##70
        arrayExp = null; //##70
    }
    //##70 BEGIN
    public
    Reduction(AssignStmt pstmt,VarNode pDefVarNode,VarNode pUseVarNode,
                   String pOpName, Exp pArrayExp)
    {
        stmt = pstmt;
        DefVarNode = pDefVarNode;
        UseVarNode = pUseVarNode;
        op = HIR.OP_NULL;
        opName = pOpName;
        arrayExp = pArrayExp;
    }
    //##70 END

}
