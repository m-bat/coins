/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow;

import coins.aflow.util.FlowError;
import coins.aflow.util.RegisterAnalClasses;
import coins.aflow.util.SelfCollectingResults;


/**
 * This  class defines the correspondence between the flow analysis item name and the class that analyzes the item. The methods are essentially static, but are made per-instance to enable inheriting or overriding.
 */
public class RegisterFlowAnalClasses implements RegisterAnalClasses {
    //        FlowResults pResults;

    /**
     * Analyzers that are shared between HIR and LIR.
     */
    public void register(SelfCollectingResults pResults) {
 //       FlowResults pResults = (FlowResults) pResults;

        //		pResults.putAnalyzer("Def", coins.aflow.FindDef.class);
//        pResults.putAnalyzer("DDef", coins.aflow.FindDDef.class);

        pResults.putAnalyzer("PDef", coins.aflow.FindPDef.class);
        pResults.putAnalyzer("DKill", coins.aflow.FindDKill.class);

//        pResults.putAnalyzer("PKill", coins.aflow.FindPKill.class);
        pResults.putAnalyzer("DDefined", coins.aflow.FindDDefined.class);
        pResults.putAnalyzer("PDefined", coins.aflow.FindPDefined.class);

        //		pResults.putAnalyzer("Kill", coins.aflow.FindKill.class);
        //		pResults.putAnalyzer("Destroy", coins.aflow.FindDestroy.class);
//        pResults.putAnalyzer("DExposed", coins.aflow.FindDExposedUsed.class);
//        pResults.putAnalyzer("DUsed", coins.aflow.FindDExposedUsed.class);
        pResults.putAnalyzer("PExposed", coins.aflow.FindPExposedUsed.class);
        pResults.putAnalyzer("PUsed", coins.aflow.FindPExposedUsed.class);
        pResults.putAnalyzer("DEGen", coins.aflow.FindDEGen.class);
        pResults.putAnalyzer("PEKill", coins.aflow.FindPEKill.class);

        //		pResults.putAnalyzer("Reach", coins.aflow.FindReach.class);
        //		pResults.putAnalyzer("Reach0", coins.aflow.FindReach0.class);
//        pResults.putAnalyzer("DReach", coins.aflow.FindDReach.class);
        pResults.putAnalyzer("PReach", coins.aflow.FindPReach.class);

        pResults.putAnalyzer("DAvailIn", coins.aflow.FindDAvailInAvailOut.class);
        pResults.putAnalyzer("DAvailOut", coins.aflow.FindDAvailInAvailOut.class);
        pResults.putAnalyzer("PLiveIn", coins.aflow.FindPLiveInLiveOut.class);
        pResults.putAnalyzer("PLiveOut", coins.aflow.FindPLiveInLiveOut.class);
        pResults.putAnalyzer("DDefIn", coins.aflow.FindDDefInDefOut.class);
        pResults.putAnalyzer("DDefOut", coins.aflow.FindDDefInDefOut.class);

        pResults.putAnalyzer("DefUseList", coins.aflow.FindDefUseList.class);
        pResults.putAnalyzer("UDList", coins.aflow.FindDefUseList.class);

        pResults.putAnalyzer("DefUseList0", coins.aflow.FindDefUseList0.class);
        pResults.putAnalyzer("UDList0", coins.aflow.FindDefUseList0.class);

 //       pResults.putAnalyzer("DDefUseList", coins.aflow.FindDDefUseList.class);
  //      pResults.putAnalyzer("DUDList", coins.aflow.FindDDefUseList.class);

//        pResults.putAnalyzer("DDefUseList0", coins.aflow.FindDDefUseList0.class);
 //       pResults.putAnalyzer("DUDList0", coins.aflow.FindDDefUseList0.class);
    }

    /**
     * Analyzers that are specific to HIR.
     */
    public void registerHir(FlowResults pResults) {
        pResults.putAnalyzer("Initiate", coins.aflow.InitiateFlowHir.class);
        pResults.putAnalyzer("SymIndexTable", coins.aflow.InitiateFlowHir.class);
        pResults.putAnalyzer("BBlockSetRefReprs",
            coins.aflow.RecordSetRefReprsHir.class);
        pResults.putAnalyzer("SubpFlowSetReprs",
            coins.aflow.RecordSetRefReprsHir.class);
    }

    /**
     * Analyzers that are specific to LIR.
     */
    public void registerLir(FlowResults pResults) {
        throw new FlowError();
    }
}
