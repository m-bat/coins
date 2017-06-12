/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.flow;

import coins.FlowRoot;
import coins.HirRoot;
import coins.IoRoot;
import coins.SymRoot;
import coins.ir.hir.AssignStmt;
import coins.ir.hir.BlockStmt;
import coins.ir.hir.HIR;
import coins.ir.hir.HirIterator;
import coins.ir.hir.InfStmt;
import coins.ir.hir.LabeledStmt;
//##60 import coins.ir.hir.PrintStmtVisitor;
//##60 import coins.ir.hir.PrintVisitor;
import coins.ir.hir.Stmt;
import coins.ir.hir.SubpDefinition;
import coins.sym.FlagBox;
import coins.sym.Subp;
import coins.sym.Sym;
import coins.sym.SymIterator;
import coins.sym.SymTable;
import coins.sym.SymTableIterator;
import coins.sym.Type;

import coins.ir.hir.Program; //##60
import coins.ir.hir.SubpDefinition; //##60
import coins.ir.IrList; //##60
import coins.sym.FlowAnalSym; //##92
import java.util.Iterator; //##60
import java.util.List;     //##92
import java.util.Map; //##64
import java.util.Set; //##92

// Modified on Dec. 2001.
/** FlowImpl class
 *
 *  Flow analysis class.
 **/
public class
  FlowImpl
  implements Flow
{

//------ Public fields ------

  public final FlowRoot // Used to access Root information.
    flowRoot;

  public final IoRoot // Used to access Root information.
    ioRoot;

  public final SymRoot // Used to access Root information.
    symRoot;

  public final HirRoot // Used to access Root information.
    hirRoot;

  //### public coins.flow.Flow //##60
  //###   flow;

  public coins.flow.SubpFlow // SubpFlow under analysis.     //##60
    fSubpFlow;               // This is set by SubpFlowImpl. //##60

  public Object              // Either coins.flow.SubpFlow //##78
    fSubpFlowCurrent = null; // or coins.aflow.SubpFlow    //##78
                             // thast is currently effective. //##78

  public coins.sym.Subp // Subp under analysis.         //##60
    fSubp;              // This is set by SubpFlowImpl. //##60

  public coins.flow.ControlFlow //##60
    fControlFlow;

  public coins.flow.DataFlow //##60
    fDataFlow;

//---- Protected/private fields used in contor/data flow anal ----

  protected FlagBox
    fFlowAnalState;

  protected int
    fFlowAnalStateLevel;

  /**
   * Map a map of static variable and corresponding temporal variable.
   * For each subprogram to be expanded inline,
   * a map showing the correspondence between local static variable
   * and temporal variable to be used in replacement is created.
   * staticVariableMapOfSubp maps such map to the subprogram to be inlined.
   */
  public Map
    staticVariableMapOfSubp = null; //##64

  public final int
    fDbgLevel;

//====== Constructors ======

  public
    FlowImpl()
  {
    flowRoot = null;
    ioRoot = null;
    symRoot = null;
    hirRoot = null;
    fDbgLevel = 0;
  }

  public
    FlowImpl(FlowRoot pFlowRoot)
  {
    flowRoot = pFlowRoot;
    ioRoot = pFlowRoot.ioRoot;
    symRoot = pFlowRoot.symRoot;
    //-- Pass information from ControlFlow to DataFlow.
    hirRoot = pFlowRoot.hirRoot;
//##60   lirRoot     = pFlowRoot.lirRoot;
    //##60 flow  = pFlowRoot.flow;
    fDbgLevel = ioRoot.dbgFlow.getLevel(); //##60
  }

//====== Methods to control data flow analysis ======

// BEGIN

  public ControlFlow
    controlFlowAnal(SubpFlow pSubpFlow)
  {
    SubpDefinition lSubpDef = pSubpFlow.getSubpDefinition();
    Subp lSubp = lSubpDef.getSubpSym();
    BBlock lBBlock;
    //##60 flow = flowRoot.flow;
    ioRoot.dbgFlow.println(1);
    if (fDbgLevel > 0)
      dbg(1, "\nControl flow analysis of " + lSubp.getName());
    if (flowRoot.flow.getFlowAnalStateLevel() >=
        flowRoot.flow.STATE_CFG_RESTRUCTURING)
      pSubpFlow.resetControlAndDataFlowInformation(); //##60
    if (flowRoot.isHirAnalysis()) {
      if (fDbgLevel >= 4) {
        lSubpDef.printHir("before control flow analysis");
      }
      ioRoot.dbgFlow.println(2);
      flowRoot.controlFlow = new coins.flow.ControlFlowImpl(flowRoot,
        (HirSubpFlow)pSubpFlow, lSubpDef);
      if (fDbgLevel >= 3)
        lSubpDef.printHir("after dividing into basic blocks");
    }
    //##60 ((FlowImpl)flow).fControlFlow = flowRoot.controlFlow;
    fControlFlow = flowRoot.controlFlow; //##60
    //##60 ShowControlFlow sFlow = flow.controlFlow().getShowFlow();
    ShowControlFlow sFlow = controlFlow().getShowControlFlow(); //##60
    if (fDbgLevel > 1)
      sFlow.showAll();

      // BEGIN
      //-- Link BBlocks in depth first order.
      //## Skip by compiler option.
      //##60 lSubp.getEntryBBlock().linkInDepthFirstOrder(lSubp);
    flowRoot.fSubpFlow.getEntryBBlock().linkInDepthFirstOrder(lSubp); //##60
    if (fDbgLevel >= 3) {
      ioRoot.printOut.print(
        "\nBasic block information in depth first order.\n");
      //##60 lBBlock = lSubp.getEntryBBlock();
      lBBlock = flowRoot.fSubpFlow.getEntryBBlock(); //##60
      while (lBBlock != null) {
        ioRoot.printOut.print(" " + lBBlock.toStringDetail() + "\n");
        lBBlock = lBBlock.getNextInDFO();
      }
    }
    // END

    //##60 flow.setFlowAnalStateLevel(Flow.STATE_CFG_AVAILABLE);
    setFlowAnalStateLevel(Flow.STATE_CFG_AVAILABLE); //##60
    pSubpFlow.setComputedFlag(pSubpFlow.CF_CFG); //##60
    if (flowRoot.isHirAnalysis()) {
      /* //##60
         if (ioRoot.dbgHir.getLevel() >= 4) {
        testNodeIterator();    //## Tan
        testInf();
         }
         if (ioRoot.dbgSym.getLevel() >= 3) {
        symRoot.symTableRoot.printSymTableAll(symRoot.symTableRoot);
        if (ioRoot.dbgSym.getLevel() >= 6) {
         testSymTable();
        }
         }
       */
      //##60
    }
//##  testSymTable0(); //## DELETE
    return flowRoot.controlFlow;
  } // controlFlowAnal

  public DataFlow
    dataFlowAnal(SubpDefinition pSubpDef)
  {
    SubpFlow lSubpFlow;
    Subp lSubp = pSubpDef.getSubpSym();
    //##60 flow = flowRoot.flow;
    if (fDbgLevel > 0) {
      ioRoot.dbgFlow.println(1);
      dbg(1, "Data flow analysis of " +
          lSubp.getName() + " state " + fFlowAnalStateLevel);
    }
    lSubpFlow = flowRoot.fSubpFlow; //##60
    ((SubpFlowImpl)lSubpFlow).initiateDataFlowAnal(pSubpDef);
    if (flowRoot.isHirAnalysis()) {
      InitiateFlowHir lInitiateFlowHir = new InitiateFlowHir(); // Make instance. //##60
      lInitiateFlowHir.initiateDataFlow(lSubpFlow); //##60
      //##60 ((HirSubpFlow)flowRoot.fSubpFlow).allocateExpIdForSubp();
      flowRoot.dataFlow = (coins.flow.DataFlow)(new coins.flow.DataFlowHirImpl(
        flowRoot, (HirSubpFlow)lSubpFlow));
    }
    //##60 ((FlowImpl)flow).fDataFlow = flowRoot.dataFlow;
    fDataFlow = flowRoot.dataFlow; //##60
    //##78 lSubpFlow.attachLoopInf(); //##60
   if (fDbgLevel >= 2) {
      dbg(2, "\nbegin findAll\n");
      fDataFlow.findAll(); //##60
      lSubpFlow.summarize();
      if (fDbgLevel >= 7) //##63
        //##60 flow.dataFlow().showAll();
        dataFlow().showAll(); //##60
      //##60 flow.dataFlow().showSummary();
      dataFlow().showSummary(); //##60
      //##60 lSubpFlow.attachLoopInf();
      if (flowRoot.isHirAnalysis()) {
        if (fDbgLevel >= 5) {
          symRoot.symTable.printSymTableAllDetail(
            symRoot.symTableRoot);
          //##60 testBitVectorIterator();    //## Tan
        }
        if (ioRoot.dbgHir.getLevel() >= 3) {
          //##60 testVisitor();
        }
      }
    }
    //##60 flow.setFlowAnalStateLevel(Flow.STATE_HIR_FLOW_AVAILABLE);
    setFlowAnalStateLevel(Flow.STATE_HIR_FLOW_AVAILABLE); //##60
    //## testRemove(flowRoot.symRoot.symTableCurrent);
    //##60 if (ioRoot.dbgHir.getLevel() >= 6)
    //##60   testClone(pSubpDef);
    return flowRoot.dataFlow;
  } // dataFlowAnal

// END

  public void
    resetAllFlowInf(Subp pSubp)
  {
    // Reset flow information of all symbols.
    //##60 SubpFlow lSubpFlow = pSubp.getSubpFlow();
    coins.flow.SubpFlow lSubpFlow = flowRoot.fSubpFlow; //##60
    if (lSubpFlow != null) {
      lSubpFlow.resetFlowSymLinkForRecordedSym();
      fControlFlow = null;  //##78
      fDataFlow    = null;  //##78
      fSubpFlow    = null;  //##78
      fSubpFlowCurrent = null; //##78
    }
  }

//----

  public coins.flow.SubpFlow
    getSubpFlow()
  {
    //##60 return flowRoot.fSubpFlow;
    return fSubpFlow; //##60
  } //##60

  public Subp
    getSubpUnderAnalysis()
  {
    //##60 return flowRoot.subpUnderAnalysis;
    return fSubp; //##60
  }

  public ControlFlow
    controlFlow()
  {
    return fControlFlow;
  }

  public DataFlow
    dataFlow()
  {
    return fDataFlow;
  }

//##60 BEGIN
  public DataFlow
    dataFlowAnal()
  {
    dbg(1, "\ndataFlowAnal()",
      "do data flow analysis for all subprograms\n");
    coins.ir.IrList subpDefList
      = ((Program)hirRoot.programRoot).getSubpDefinitionList();
    Iterator subpDefIterator = subpDefList.iterator();
    while (subpDefIterator.hasNext()) {
      SubpDefinition subpDef = (SubpDefinition)(subpDefIterator.next());
      if (subpDef == null)
        continue;
      coins.flow.HirSubpFlow lHirSubpFlow =
        (coins.flow.HirSubpFlow)(new coins.flow.HirSubpFlowImpl(flowRoot,
        subpDef));
      //##65 flowRoot.fSubpFlow = lHirSubpFlow; // This is set by HirSubpFlowImpl.
      fSubpFlow = lHirSubpFlow; //##60
      //##60 ((SubpFlowImpl)lHirSubpFlow).initiateControlFlowAnal( lHirSubpFlow,
      //##60    subpDef.getNodeIndexMin(), subpDef.getNodeIndexMax());
      //##60 lHirSubpFlow.divideHirIntoBasicBlocks();
      flowRoot.controlFlow = controlFlowAnal(lHirSubpFlow);
      flowRoot.dataFlow = dataFlowAnal(subpDef);
    }
    return fDataFlow;
  } // dataFlowAnal

//##60 END

  public boolean
    getFlowAnalState(int pFlagId)
  {
    return fFlowAnalState.getFlag(pFlagId);
  }

  public void
    setFlowAnalState(int pFlagId, boolean pYesNo)
  {
    fFlowAnalState.setFlag(pFlagId, pYesNo);
  }

  public int
    getFlowAnalStateLevel()
  {
    return fFlowAnalStateLevel;
  }

  public void
    setFlowAnalStateLevel(int pStateLevel)
  {
    fFlowAnalStateLevel = pStateLevel;
    if (ioRoot.dbgFlow.getLevel() >= 2) {
      ioRoot.dbgFlow.print(2, " setFlowAnalStateLevel", "=" + pStateLevel); //##62
    }
  }

//====== Flow analyzer test methods  ======

  private void
    testHirModify(HIR pSubpBody)
  {
    HIR lHir1;
    BlockStmt lBlock, lBlock1, lBlock2;
    Stmt lStmt, lStmt2;
    ioRoot.dbgHir.print(3, "testHirModify subpBody", pSubpBody.toString());
    if (ioRoot.dbgHir.getLevel() >= 3) {
      if (pSubpBody.getOperator() == HIR.OP_LABELED_STMT) {
        lStmt = ((LabeledStmt)pSubpBody).getStmt();
        if (lStmt.getOperator() == HIR.OP_BLOCK) {
          lBlock1 = (BlockStmt)(lStmt.copyWithOperands());
          ioRoot.dbgHir.println(3);
          ioRoot.dbgHir.print(3, "CopyWithOperands result", lStmt.toStringShort());
          lBlock1.print(0);
          lBlock2 = (BlockStmt)(lStmt.copyWithOperandsChangingLabels(null));
          ioRoot.dbgHir.println(3);
          ioRoot.dbgHir.print(3, "CopyWithOperandsChangingLabel result",
            lStmt.toStringShort());
          lStmt2 = hirRoot.hir.labeledStmt(
            symRoot.symTableCurrent.generateLabel(), lBlock2);
          lStmt2.print(0);
          ((BlockStmt)((LabeledStmt)pSubpBody).getStmt()).addLastStmt(lStmt2);
        }
      }
    }
  } // testHirModify

  private void
    testClone(HIR pSubpDef)
  {
    HIR lSubpDef;
    ioRoot.dbgHir.print(2, "testClone",
      ((SubpDefinition)pSubpDef).getSubpSym().getName());
    if (ioRoot.dbgHir.getLevel() >= 2) {
      lSubpDef = (HIR)pSubpDef.copyWithOperands();
      lSubpDef.print(1, true);
    }
  }

  private void
    testNodeIterator()
  {
    //## Tan
    //---- Test iterators ----
    SubpFlow lSubpFlow;
    BBlockSubtreeIterator lStmtIterator;
    BBlockNodeIterator lNodeIterator;
    coins.ir.hir.HirIterator lHirIterator, lHirIterator2;
    BBlock lBBlock;
    HIR lStmt; //## Tan
    HIR lNode;
    Sym lSym;
    if (ioRoot.dbgHir.getLevel() >= 4) {
      if (ioRoot.dbgHir.getLevel() >= 5) {
        ioRoot.printOut.print("\nHirIterator applied to programRoot");
        for (lHirIterator = hirRoot.hir.hirIterator(hirRoot.programRoot);
             lHirIterator.hasNext(); ) {
          lNode = lHirIterator.next();
          ioRoot.printOut.print("\nHir " +
            ioRoot.toStringObject(lNode));
        }
      }
      ioRoot.printOut.print("\nHirIterator");
      for (lHirIterator = hirRoot.hir.hirIterator(
        symRoot.subpCurrent.getHirBody());
           lHirIterator.hasNext(); ) {
        lNode = lHirIterator.next();
        ioRoot.printOut.print("\nHir " +
          ioRoot.toStringObject(lNode));
      }
      ioRoot.printOut.print("\nHirIterator getNextStmt");
      for (lHirIterator = hirRoot.hir.hirIterator(
        symRoot.subpCurrent.getHirBody());
           lHirIterator.hasNext(); ) {
        lStmt = lHirIterator.nextStmt(); //##60
        ioRoot.printOut.print("\n Stmt " +
          ioRoot.toStringObject(lStmt));
      }
      ioRoot.printOut.print("\nHirIterator getNextExecutableNode");
      for (lHirIterator = hirRoot.hir.hirIterator(
        symRoot.subpCurrent.getHirBody());
           lHirIterator.hasNext(); ) {
        lNode = lHirIterator.getNextExecutableNode();
        ioRoot.printOut.print("\nHir " +
          ioRoot.toStringObject(lNode));
      }
    }
    ioRoot.dbgHir.print(4, "BBlock", "SubtreeIterator ");
    lSubpFlow = flowRoot.fSubpFlow; //##60
    for (int lBlockNo = 1; lBlockNo <= lSubpFlow.getNumberOfBBlocks();
         lBlockNo++) {
      lBBlock = lSubpFlow.getBBlock(lBlockNo);
      ioRoot.dbgHir.print(4, "BBlock", "No: " + lBlockNo +
        " " + lBBlock.getBlockNumber() +
        " HirLink " + ((HIR)lBBlock.getIrLink()).toStringShort());
      for (lStmtIterator = lBBlock.bblockSubtreeIterator();
           lStmtIterator.hasNext(); ) {
        lStmt = (HIR)(lStmtIterator.next());
        ioRoot.dbgHir.print(4, " Subtree",
          ioRoot.toStringObject(lStmt));
        if (lStmt != null) {
          ioRoot.dbgHir.print(4, " Parent " +
            ioRoot.toStringObject(lStmt.getParent()));
          if (ioRoot.dbgHir.getLevel() > 4) {
            for (lHirIterator2 = hirRoot.hir.hirIterator(lStmt);
                 lHirIterator2.hasNext(); ) {
              lNode = lHirIterator2.next();
              ioRoot.printOut.print(
                "\n  HirInStmt " + ioRoot.toStringObject(lNode));
            }
          }
        }
      }
    }
    //## Addition begin  //## Tan
    if (ioRoot.dbgHir.getLevel() >= 4) {
      ioRoot.dbgHir.print(4, "BBlock", "NodeIterator ");
      for (int lBlockNo = 1; lBlockNo <= lSubpFlow.getNumberOfBBlocks();
           lBlockNo++) {
        ioRoot.dbgHir.print(3, " NodeIterator", "for B" + lBlockNo);
        lBBlock = lSubpFlow.getBBlock(lBlockNo);
        for (lNodeIterator = lBBlock.bblockNodeIterator();
             lNodeIterator.hasNext(); ) {
          lNode = (HIR)lNodeIterator.next();
          ioRoot.printOut.print("\n  Node " +
            ioRoot.toStringObject(lNode));
        }
      }
    }

    //---- Test data flow analysis methods ----
    //## These test statements are deleted.  //## Tan
  } // testNodeIterator

//##60 BEGIN
  public void dbg(int level, String pHeader, Object pObject) //##52
  {
    ioRoot.dbgFlow.printObject(level, pHeader, pObject);
    ioRoot.dbgFlow.println(level);
  }

  public void dbg(int level, Object pObject) //##52
  {
    if (pObject == null)
      ioRoot.dbgFlow.print(level, " null ");
    else
      ioRoot.dbgFlow.print(level, " " + pObject.toString());
  }

//##63 BEGIN
public void
doHir()
{
  dbg(1, "\ndoHir() ");
  //##67 BEGIN
  coins.ir.IrList subpDefList
    = ((Program)hirRoot.programRoot).getSubpDefinitionList();
  Iterator subpDefIterator = subpDefList.iterator();
  while (subpDefIterator.hasNext()) {
    SubpDefinition subpDef = (SubpDefinition)(subpDefIterator.next());
    if (subpDef == null)
      continue;
    SubpFlow lSubpFlow = (SubpFlow)(new MySubpFlow(flowRoot, subpDef)); // Test of MySubpFlow
    //##67 SubpFlow lSubpFlow = (SubpFlow)(new HirSubpFlowImpl(flowRoot, subpDef));
    fSubpFlow = lSubpFlow;
    //##67 END
    //##67 if (fSubpFlow != null) {
    doHir0(fSubpFlow.getSubpDefinition(), fSubpFlow);
  }
} // doHir
//##63 END

// Flow analysis used in PRE
  public void
    doHir0(SubpDefinition pSubpDef, SubpFlow pSubpFlow)
  {
    dbg(1, "\ndoHir0", " BEGIN " + pSubpDef.getSubpSym().getName()); //##56;
    BBlock lBBlock;
    int lReturnValue = 0;
    //##92 testDataFlow(pSubpDef, pSubpFlow); //##92
    {
      //##60 ShowFlow lShowFlow = new ShowFlow(lResults);

      //##60 if (!checkTreeStructure(pSubpDef, ioRoot)) {
      //##60   throw new FlowError("Not tree.");
      //##60 }
      //##60 lSubpFlow.controlFlowAnal();
      //##60 ControlFlow lControlFlow = flow.controlFlowAnal(pSubpFlow);
      ControlFlow lControlFlow; //##67
      DataFlow lDataFlow; //##67
      ShowControlFlow lShowControlFlow; //##60
      //##67 if ((getFlowAnalStateLevel() < STATE_CFG_AVAILABLE)) {
        lControlFlow = controlFlowAnal(pSubpFlow);
        lShowControlFlow = new ShowControlFlow(pSubpFlow, lControlFlow); //##67
        lShowControlFlow.showAll(); //##67
      //##67 }
      //##67 if ((getFlowAnalStateLevel() < STATE_DATA_FLOW_AVAILABLE)) { //##60
        lDataFlow = dataFlowAnal(pSubpDef); //##60
        ((MySubpFlow)pSubpFlow).computeTransparent(); //##67
        ShowDataFlow lShowDataFlow = new ShowDataFlow(lDataFlow); //##60
        if (ioRoot.dbgFlow.getLevel() > 4) //##60
          lShowDataFlow.showAll(); //##60
        else //##60
          lShowDataFlow.showSummary(); //##60
      //##67 }
    }
    dbg(1, "\ndoHir0", " END"); //##56;
  }

//##60 END

//##92 BEGIN
protected void
testDataFlow( SubpDefinition pSubpDef, SubpFlow pSubpFlow )
{
  dbg(1, "\ntestDataFlow", " BEGIN " + pSubpDef.getSubpSym().getName()); //##56;
  BBlock lBBlock;
  ControlFlow lControlFlow;
  DataFlow lDataFlow;
  /*
  ShowControlFlow lShowControlFlow;
  lControlFlow = controlFlowAnal(pSubpFlow);
  lShowControlFlow = new ShowControlFlow(pSubpFlow, lControlFlow); //##67
  lShowControlFlow.showAll();
  */
  lDataFlow = dataFlowAnal(pSubpDef);
  DefUseList lDefUseList = pSubpFlow.getDefUseList();
  FlowAnalSym lFlowAnalSym[] = pSubpFlow.getFlowAnalSymTable();
  dbg(4, "lFlowAnalSymTable", " " + lFlowAnalSym);
  for (int i = 0; i < lFlowAnalSym.length; i++) {
   List lDefUseChainList =
     lDefUseList.getDefUseChainListOfSym(lFlowAnalSym[i]);
   for (Iterator lIter1 = lDefUseChainList.iterator();
        lIter1.hasNext(); ) {
     DefUseChain lDefUseChain = (DefUseChain)lIter1.next();
     dbg(4, " DefUseChain ", lDefUseChain.toStringByName());
     HIR lDefNode = (HIR)lDefUseChain.getDefNode();
     dbg(4, "  DefNode ", lDefNode);
   }
 }
 Set lDefinedSyms = pSubpFlow.getDefinedSyms();
 dbg(4, "lDefinedSyms", " " + lDefinedSyms);
 for (Iterator lIter3 = lDefinedSyms.iterator();
      lIter3.hasNext(); ) {
    FlowAnalSym lFlowAnalSym2 = (FlowAnalSym)lIter3.next();
    List lDefUseChainList =
      lDefUseList.getDefUseChainListOfSym(lFlowAnalSym2);
    for (Iterator lIter1 = lDefUseChainList.iterator();
         lIter1.hasNext(); ) {
      DefUseChain lDefUseChain = (DefUseChain)lIter1.next();
      dbg(4, " DefUseChain ", lDefUseChain.toStringByName());
      HIR lDefNode = (HIR)lDefUseChain.getDefNode();
      dbg(4, "  DefNode ", lDefNode);
      List lUseList = lDefUseChain.getUseList();
      for (Iterator lIter2 = lUseList.iterator();
           lIter2.hasNext(); ) {
        HIR lUseNode = (HIR)lIter2.next(); pSubpFlow.getBBlockFromNodeIndex(lUseNode.getIndex());
        dbg(4, "\n useNode " + lUseNode);
        BBlock lUseBlock;
        if (lUseNode != null) {
          lUseBlock = pSubpFlow.getBBlockFromNodeIndex(lUseNode.getIndex());
          dbg(4, " use block " + lUseBlock);
        }
      }
    }
  }
  resetAllFlowInf(pSubpDef.getSubpSym());
  dbg(1, "\ntestDataFlow", " END");
} // testDataFlow
//##92 END
} // FlowImpl class
