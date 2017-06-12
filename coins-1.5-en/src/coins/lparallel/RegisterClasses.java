/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.lparallel;
import coins.aflow.FlowResults;
import coins.aflow.RegisterFlowAnalClasses;
///////////////////////////////////////////////////////////////////////////////
//
//
// [parallel loop]  RegisterClasses
// 
//
///////////////////////////////////////////////////////////////////////////////
public class RegisterClasses extends RegisterFlowAnalClasses {
    /**
    *
    * Analyzers that are specific to HIR.
    *
    */
    public void registerHir(FlowResults fResults) {
        super.registerHir(fResults);
        fResults.putAnalyzer("LoopParallelList", coins.lparallel.FindLoopParallelList.class);
        fResults.putAnalyzer("LoopParallel", coins.lparallel.FindLoopParallel.class);
    }
}
