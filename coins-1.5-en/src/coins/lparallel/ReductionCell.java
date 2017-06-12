/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.lparallel;
import coins.ir.hir.AssignStmt;
import coins.ir.hir.VarNode;
///////////////////////////////////////////////////////////////////////////////
//
// [parallel loop] ReductionCell 
//
//
///////////////////////////////////////////////////////////////////////////////
public class ReductionCell  {

    AssignStmt stmt;        // v(DEF) = v(USE) op Exp ...
    VarNode DefVarNode;     // v(DEF)
    VarNode UseVarNode;     // v(USE)
    int op;                 // Op (+)
    /**
    *  
    * ReductionCell:
    *  
    *  
    **/
    ReductionCell (AssignStmt pstmt,VarNode pDefVarNode,VarNode pUseVarNode,int pop)
    {
        stmt = pstmt;
        DefVarNode = pDefVarNode;
        UseVarNode = pUseVarNode;
        op = pop;
    }
}
