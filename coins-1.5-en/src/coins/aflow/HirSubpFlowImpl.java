/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow;

import java.util.List;

import coins.ir.hir.SubpDefinition;
import coins.aflow.util.FAList;         //##25
import coins.ir.hir.HIR;                //##25
import coins.ir.hir.HirIterator;        //##25
import coins.ir.hir.LabeledStmt;        //##25
import coins.ir.hir.SubpDefinitionImpl; //##25
import coins.sym.ExpId;                 //##25
import java.util.Iterator;              //##25


/** HirSubpFlowImpl class:
 *
 *  HIR subprogram flow analysis class.
 **/
public  class HirSubpFlowImpl extends SubpFlowImpl implements HirSubpFlow {
    //====== Fields ======//
//    protected static final Class fAssignerClass = coins.aflow.AssignHashBasedFlowExpIdHir.class;

    //        private ExpId[]
    //        fExpIdHashTable = new ExpId[EXP_ID_HASH_SIZE];
    //       private ExpId
    //       fFirstExpId = null;   // First ExpId in the current subprogram.
    private final static Class flowExpIdClass = coins.aflow.HashBasedFlowExpIdHir.class;

    //	private final static Class flowExpIdClass = coins.aflow.HashBasedFlowExpId.class;
    //====== Constructors ======//
//##73 protected
public //##73
HirSubpFlowImpl(SubpDefinition pSubpDefinition, FlowResults pResults)
{
    super(pSubpDefinition, pResults);
    flowRoot.setHirAnalysis();
    if (flowRoot.subpFlow != null) {       //##94
      flowRoot.resetAllFlowAnalSymLinks(); //##94
    }                                      //##94
    flowRoot.subpFlow = this; //##60
//##25 BEGIN
    flowRoot.aflow.dbg(1, "\ncoins.aflow.HirSubpFlowImpl ", //##94
                       pSubpDefinition.getSubpSym().toString());
    //## pSubpDefinition.setIndexNumberToAllNodes(
    //##      ((SubpDefinitionImpl)pSubpDefinition).fNodeIndexMin);
    fIrIndexMin = ( (SubpDefinitionImpl) pSubpDefinition).fNodeIndexMin;
    fIrIndexMax = ( (SubpDefinitionImpl) pSubpDefinition).fNodeIndexMax;
    fIrIndexMin = 0; //Ogasawara 040827
    flowRoot.aflow.dbg(2, "IndexMin=" + fIrIndexMin,
                       " IndexMax=" + fIrIndexMax);
    // Register before get !
    if (!pResults.containsKey("SetRefReprTable", pSubpDefinition)) {
      pResults.put("SetRefReprTable", pSubpDefinition,
                   new SetRefRepr[fIrIndexMax - fIrIndexMin + 1]);
      fSetRefReprTable = (SetRefRepr[]) pResults.get("SetRefReprTable",
          pSubpDefinition);
      fRecordSetRefReprs = new RecordSetRefReprsHir(pResults);
    }
//##25 END
}

    protected HirSubpFlowImpl(FlowResults pResults, SubpDefinition pSubpDef) {
        this(pSubpDef, pResults);
    }

    public BBlockSubtreeIterator bblockSubtreeIterator(BBlock pBBlock) {
        return (BBlockSubtreeIterator) (new BBlockStmtIterator((BBlockHir) pBBlock));
    }

    public List controlFlowAnal() {
    flowRoot.aflow.dbg(2, "controlFlowAnal",
                       fSubpDefinition.getSubpSym().getName()); //##25
        new MakeControlFlowGraphHir(fResults).find(this);

        return fReachableBBlocks;
    }

//##25 BEGIN

public void
correlateBBlockAndIR()
{
    SubpDefinition lSubpDef = getSubpDefinition();
    HIR lNode;
    BBlock lCurrentBBlock = (BBlockHir) fResults.getBBlockForLabel(lSubpDef.
        getStartLabel());
    BBlock lBBlock;
    if (lSubpDef.getNodeIndexMax() == 0) {
      flowRoot.aflow.dbg(2, "\n Index is not yet assigned. Assign it.");
      lSubpDef.setIndexNumberToAllNodes( ( (SubpDefinitionImpl) lSubpDef).
                                        fNodeIndexMin);
      allocateBBlockOfIR();
    }
    flowRoot.aflow.dbg(2, "\ncorrelateBBlockAndIR",
                       "indexMax " + fIrIndexMax + " BBlock " + lCurrentBBlock);
    for (HirIterator lHirIterator = FlowUtil.hirIterator(lSubpDef.getHirBody());
         lHirIterator.hasNext(); ) {
      lNode = (HIR) lHirIterator.next();
      if (lNode instanceof LabeledStmt) {
        lBBlock = (BBlockHir) fResults.getBBlockForLabel( ( (LabeledStmt) lNode).
            getLabel());
        flowRoot.aflow.dbg(3, "\n  " + lNode.toString(), "BBlock " + lBBlock);
        if (lBBlock != null)
          lCurrentBBlock = lBBlock;
      }
      if (lNode != null)
        setBBlockOfIR(lCurrentBBlock, lNode.getIndex());
    }
} // correlateBBlockAndIR

public void
computeBBlockSetRefReprs(SubpFlow pSubpFlow)
{
    flowRoot.aflow.dbg(4, "\ncomputeBBlockSetRefReprs"); //##25
    FAList lBBlockTable = pSubpFlow.getBBlockTable();
    for (Iterator lIterator = lBBlockTable.iterator();
         lIterator.hasNext(); ) {
      BBlockHir lBBlock = (BBlockHir) lIterator.next();
      LabeledStmt lStmt = (LabeledStmt) lBBlock.getIrLink();
      flowRoot.aflow.dbg(4, " BBlock",
                         lBBlock.toString() + " " + lStmt.toString()); //##25
      SetRefReprList lSetRefReprList = new SetRefReprListHir(lBBlock);
      SetRefReprHir lSetRefRepr = new SetRefReprHirEImpl(lStmt, lBBlock, false, null); // Always false ?
      lSetRefReprList.add(lSetRefRepr);
//      pSubpFlow.results().put("BBlockSetRefReprs", this, lSetRefReprList);
      pSubpFlow.results().put("BBlockSetRefReprs", lBBlock, lSetRefReprList); // Fix 2004.09.23 S.Noishi
      pSubpFlow.setSetRefReprOfIR(lSetRefRepr, lStmt.getIndex());
    }
} // computeBBlockSetRefReprs

public void
clear()
{
    super.clear();
    SubpDefinition lSubpDef = getSubpDefinition();
    HIR lNode;
    flowRoot.aflow.dbg(2, "Clear ExpId ");
    /* //##62
    for (HirIterator lHirIterator = FlowUtil.hirIterator(lSubpDef.getHirBody());
         lHirIterator.hasNext(); ) {
      lNode = (HIR) lHirIterator.next();
      //-- Clear ExpId
      if (lNode != null) {
        ExpId lExpId = lNode.getExpId();
        if (lExpId != null) {
          lNode.setExpId(null);
        }
      }
    }
      */ //##62
    //##62 BEGIN
    int lIndexMin = getIrIndexMin();
    int lIndexMax = getIrIndexMax();
    for (int lIndex = lIndexMin; lIndex < lIndexMax;
         lIndex++) {
      fExpIdTable[lIndex - lIndexMin] = null;
    }
    //##62 END
} // clear

//##25 END

//##57 BEGIN
  public AssignFlowExpId assigner()
  {
    AssignHashBasedFlowExpIdHir lAssigner
    = (AssignHashBasedFlowExpIdHir) fResults.getRaw("AssignerForSubpFlow", this);

    if (lAssigner == null)
    {
      return flow.assigner(this);
    }
    return lAssigner;
  }

//##57 END

}
 // HirSubpFlowImpl class
