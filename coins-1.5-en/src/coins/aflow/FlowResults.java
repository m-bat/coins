/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * FlowResults.java
 *
 * Created on August 20, 2002, 5:07 PM
 */
package coins.aflow;

import java.util.HashMap;
import java.util.Map;

import coins.FlowRoot;
import coins.aflow.util.SelfCollectingResults;
import coins.ir.IR;
import coins.ir.hir.HIR;
import coins.sym.Label;


/**
 * <p>This class is a SelfCollectingResults class that has several convenience methods. It also has its own map (fCFGInfo) that holds some CFG related info, and that won't be destroyed by calling clear().</p>
 *
 * <p>Because this class is associated with CFG, it has to be instantiated for every SubpFlow object. This is not what was originally intended.</p>
 * @author  hasegawa
 */
public class FlowResults extends SelfCollectingResults {
    /**
     * Holds the <code>Map</code> from <code>Label</code> to BBlock.
     */
    protected final Map fCFGInfo = new HashMap();
    protected final int fDbgLevel; //##66

    public final FlowRoot flowRoot;

    /** Creates a new instance of FlowResults */
    public FlowResults(FlowRoot pFlowRoot) {
        flowRoot = pFlowRoot;
        fDbgLevel = flowRoot.ioRoot.dbgFlow.getLevel(); //##66

        if (flowRoot.isHirAnalysis()) {
            ((RegisterFlowAnalClasses) regClasses).registerHir(this);
        } else {
            ((RegisterFlowAnalClasses) regClasses).registerLir(this);
        }

        //		fCFGInfo = new FlowResults(flowRoot);
    }

    public static void putRegClasses(RegisterFlowAnalClasses pRegClasses) {
        regClasses = pRegClasses;
    }

    /**
     * Clear all the information this object holds.
     */
    public void clearAll() {
        clear();
        fCFGInfo.clear();
    }

    public BBlock getBBlockForLabel(Label pLabel) {
        //##70 return (BBlock) fCFGInfo.get(pLabel);
         //##70 BEGIN
        BBlock lBBlock = (BBlock) fCFGInfo.get(pLabel);
        if ((lBBlock == null)&&(fDbgLevel > 0))
          flowRoot.ioRoot.dbgFlow.print(1, "getBBlockForLabel",
            "BBlock not found for " + pLabel.getName());
        return lBBlock;
        //##70 END
    }

    public void setBBlockForLabel(Label pLabel, BBlock pBBlock) {
        fCFGInfo.put(pLabel, pBBlock);
    }

    public FlowExpId getFlowExpIdForNode(IR pIR) {
        return (FlowExpId) get("FlowExpIdForNode", pIR);
    }

    public void setFlowExpIdForNode(IR pIR, FlowExpId pFlowExpId) {
        put("FlowExpIdForNode", pIR, pFlowExpId);
    }

    public BBlock getBBlockForNode(HIR pHIR)
    {
        return (BBlock)get("BBlockForNode", pHIR);
    }

  //##66 BEGIN
    public void putAnalyzer(String pAnal, Class pAnalClass)
    {
      if (fDbgLevel > 1) {
        flowRoot.ioRoot.dbgFlow.print(2, "\nputAnalyzer "
          + pAnal + " " + pAnalClass.getName());
      }
      super.putAnalyzer(pAnal, pAnalClass);
    }

    public void putAnalyzer(String pAnal, Class pAnalClass, String pMethodName)
    {
      if (fDbgLevel > 1) {
        flowRoot.ioRoot.dbgFlow.print(2, "\nputAnalyzer "
          + pAnal + " " + pAnalClass.getName() + " " + pMethodName);
      }
      super.putAnalyzer(pAnal, pAnalClass, pMethodName);
    }

    public void find(String pAnal, Object pObj)
    {
      if (fDbgLevel > 3) {
        flowRoot.ioRoot.dbgFlow.print(4, "\nfind "
          + pAnal + " " + pObj);
      }
      super.find(pAnal, pObj);
    }

    public void find(String pAnal, Object pObj, Object pObj0)
    {
      if (fDbgLevel > 3) {
        flowRoot.ioRoot.dbgFlow.print(4, "\nfind "
          + pAnal + " " + pObj + " " + pObj0);
      }
      super.find(pAnal, pObj, pObj0);
    }

    private void find(String pAnal, Object[] pArgs)
    {
      if (fDbgLevel > 3) {
        flowRoot.ioRoot.dbgFlow.print(4, "\nfind-objects "
          + pAnal + " " + pArgs);
      }
      super.find(pAnal, pArgs);
    }

    public Object get(String pAnal, Object pObj)
    {
      if (fDbgLevel > 3) {
        flowRoot.ioRoot.dbgFlow.print(6, "\nget " //##73
          + pAnal + " " + pObj);
      }
      return super.get(pAnal, pObj);
    }

    public Object get(String pAnal, Object pObj, Object pObj0)
    {
      if (fDbgLevel > 3) {
        flowRoot.ioRoot.dbgFlow.print(6, "\nget " //##73
          + pAnal + " " + pObj + " " + pObj0);
      }
      return super.get(pAnal, pObj, pObj0);
    }


    public Object getRaw(String pAnal)
    {
      if (fDbgLevel > 3) {
        flowRoot.ioRoot.dbgFlow.print(6, "\ngetRaw " //##73
          + pAnal );
      }
      return super.getRaw(pAnal);
    }

    public Object getRaw(String pAnal, Object pObj)
    {
      if (fDbgLevel > 3) {
        flowRoot.ioRoot.dbgFlow.print(6, "\ngetRaw " //##73
          + pAnal + " " + pObj);
      }
      return super.getRaw(pAnal, pObj);
    }

    public Object getRaw(String pAnal, Object pObj, Object pObj0)
    {
      if (fDbgLevel > 3) {
        flowRoot.ioRoot.dbgFlow.print(6, "\ngetRaw " //##73
          + pAnal + " " + pObj + " " + pObj0);
      }
      return super.getRaw(pAnal, pObj, pObj0);
    }

//##66 END

}
