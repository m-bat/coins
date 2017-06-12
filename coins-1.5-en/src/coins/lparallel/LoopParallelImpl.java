/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.lparallel;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.ListIterator;
import coins.FlowRoot;
import coins.HirRoot;
import coins.IoRoot;
import coins.aflow.Flow;
import coins.aflow.FlowResults;
import coins.aflow.ShowFlow; //##70
import coins.aflow.SubpFlow;
import coins.ir.hir.SubpDefinition;

///////////////////////////////////////////////////////////////////////////////
//
//
// [parallel loop]  LoopParallelImpl
//
//
///////////////////////////////////////////////////////////////////////////////
public class LoopParallelImpl
  implements LoopParallel
{
  public HirRoot hirRoot;
  public IoRoot ioRoot;
  public FlowRoot flowRoot;
  public PrintStream printOut;
  public SubpFlow fSubpFlow;
  public Flow fFlow;
  public FlowResults fresults;
  /**
   *
   * LoopParallelImpl:
   *
   *
   **/
  public LoopParallelImpl(HirRoot phirRoot, IoRoot pioRoot,
    SubpDefinition pSubpDef, FlowRoot pflowRoot)
  {
    boolean lOptimized;
    hirRoot = phirRoot;
    ioRoot = pioRoot;
    flowRoot = pflowRoot;
    fFlow = pflowRoot.aflow;
    fresults = new FlowResults(flowRoot);
    fSubpFlow = fFlow.subpFlow(pSubpDef, fresults);
    flowRoot.subpFlow = fSubpFlow; //##70
    fFlow.setSubpFlow(fSubpFlow); //##70
    printOut = ioRoot.printOut;
  }

  /**
   *
   * LoopAnal:
   *
   *
   **/
  public void LoopAnal()
  {
    LinkedList LoopInfo;
    LoopTable lTable;
    if (ioRoot.dbgPara1.getLevel() > 0)   //##70
      ioRoot.dbgPara1.print(1, "LoopAnal " + fSubpFlow.getSubpSym().getName()); //##70
    fSubpFlow.controlFlowAnal();
    fSubpFlow.makeDominatorTree();
    fSubpFlow.makePostdominatorTree();
    //##70 fresults.clear();
    fresults.find("Initiate", fSubpFlow);
    //##70 BEGIN
    if (ioRoot.dbgPara1.getLevel() > 1) {
      ShowFlow lShowFlow = new ShowFlow(fresults);
      lShowFlow.showControlFlow(fSubpFlow);
      lShowFlow.showDominatorTree(fSubpFlow);
      lShowFlow.showPostdominatorTree(fSubpFlow);
      if (ioRoot.dbgPara1.getLevel() > 3) {
        lShowFlow.showVectorsByName("PDef", fSubpFlow);
        lShowFlow.showVectorsByName("DKill", fSubpFlow);
        lShowFlow.showVectorsByName("PReach", fSubpFlow);
        lShowFlow.showVectorsByName("DDefined", fSubpFlow);
        lShowFlow.showVectorsByName("PDefined", fSubpFlow);
        lShowFlow.showVectorsByName("PExposed", fSubpFlow);
        lShowFlow.showVectorsByName("PUsed", fSubpFlow);
        lShowFlow.showVectorsByName("DEGen", fSubpFlow);
        lShowFlow.showVectorsByName("PEKill", fSubpFlow);
        lShowFlow.showVectorsByName("DAvailIn", fSubpFlow);
        lShowFlow.showVectorsByName("DAvailOut", fSubpFlow);
        lShowFlow.showVectorsByName("PLiveIn", fSubpFlow);
        lShowFlow.showVectorsByName("PLiveOut", fSubpFlow);
        lShowFlow.showVectorsByName("DDefIn", fSubpFlow);
        lShowFlow.showVectorsByName("DDefOut", fSubpFlow);
        lShowFlow.showPDefUse(fSubpFlow);
        lShowFlow.showPUseDef(fSubpFlow);
      }
    }
    //##70 END
    fresults.find("LoopParallelList", fSubpFlow);
    LoopInfo = (LinkedList)fresults.get("LoopParallelList", fSubpFlow);
    for (Iterator Ie = LoopInfo.iterator(); Ie.hasNext(); ) {
      lTable = (LoopTable)Ie.next();
      //##70 060823 fresults.get("LoopParallel", fSubpFlow, lTable);
      fresults.get("LoopParallel", fSubpFlow, lTable); //###
    }
  }

  /**
   *
   * LoopAnalLoopList:
   *
   *
   **/
  public void LoopAnalLoopList(LinkedList pLoopList)
  {
    LinkedList LoopInfo;
    LoopTable lTable;
    fSubpFlow.controlFlowAnal();
    fSubpFlow.makeDominatorTree();
    fSubpFlow.makePostdominatorTree();
    fresults.clear();
    fresults.find("Initiate", fSubpFlow);
    fresults.find("LoopParallelList", fSubpFlow, pLoopList);
    LoopInfo = (LinkedList)fresults.get("LoopParallelList", fSubpFlow);
    if (LoopInfo == null) {
      return;
    }
    for (Iterator Ie = LoopInfo.iterator(); Ie.hasNext(); ) {
      lTable = (LoopTable)Ie.next();
      fresults.get("LoopParallel", fSubpFlow, lTable);
    }
  }

  /**
   *
   * ReconstructHir:
   *
   *
   **/
  public void ReconstructHir()
  {
    LoopParallelConv Inf;

    Inf = new LoopParallelConv(fSubpFlow, fresults);
    Inf.DeleteInduction();
    Inf.ChangeCommonPrivate();
    Inf.AddConditionalInitPart();
  }

  /**
   *
   * SetOpneMPInfo:
   *
   *
   **/
  public void SetOpenMPInfo()
  {
    OpenMPInfo Inf;

    Inf = new OpenMPInfo(fSubpFlow, fresults);
    //Inf.showLoopAnalResult();
    if (ioRoot.dbgPara1.getLevel() > 0) {  //##70
      System.out.print("\nshowLoopAnalResult|n"); //##70
      Inf.showLoopAnalResult(); //##70
    }
    Inf.setPragmaInfo();
    Inf.setErrorInfo();
  }

  /**
   *
   * Trace:
   *
   *
   **/
  private void Trace(String s)
  {
    //ioRoot.printOut.println(s);
    System.out.println(s);
  }
}
