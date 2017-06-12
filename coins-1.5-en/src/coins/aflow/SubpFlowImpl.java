/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow;

import java.util.Iterator;
import java.util.List;

import coins.FlowRoot;
import coins.HirRoot;
import coins.IoRoot;
import coins.SymRoot;
import coins.aflow.util.FAList;
import coins.aflow.util.FlowError;
import coins.alias.AliasAnal;      //##57
import coins.alias.RecordAlias;    //##53
import coins.ir.IrList;
import coins.ir.hir.LabeledStmt;
import coins.ir.hir.SubpDefinition;
// import coins.ir.lir.Prologue; // 2004.05.31 S.Noishi
import coins.sym.FlowAnalSym;
import coins.sym.Subp;
import coins.ir.IR;            //##25
import coins.ir.hir.BlockStmt; //##25
import coins.ir.hir.Exp;       //##57
import coins.ir.hir.HIR;       //##25
import coins.ir.hir.HirList;   //##25
import coins.ir.hir.Stmt;      //##25
import coins.ir.hir.VarNode;   //##25
import coins.sym.Sym;          //##25
import coins.sym.SymIterator;  //##25
import coins.sym.Var;          //##25
import coins.sym.Type;         //##53
import java.util.HashSet;      //##25
import java.util.Map;          //##57
import java.util.HashMap;      //##57


//                            (##11): Jan. 2002.

/** SubpFlowImpl class:
 *
 *  Subprogram flow analysis class.
 *  Fields and methods are common between HIR and LIR.
 **/
public abstract class  //##8

//## SubpFlowImpl extends FlowImpl implements SubpFlow  //##12
SubpFlowImpl implements SubpFlow
{
  //====== Fields ======//
  public final FlowRoot flowRoot;
  public final IoRoot ioRoot;
  public final SymRoot symRoot;
  public final HirRoot hirRoot;
  public final Flow flow;
  // public LirRoot lirRoot = null; // 2004.05.31 S.Noishi

  //##12 BEGIN Fields moved from FlowImpl
  protected SubpDefinition // Defining node of subprogram
  fSubpDefinition; // to be flow-analyzed.
  protected BBlock fPrevBBlockInSearch; // Previous BBlock in linkInDepthFirstOrder.
  //##12 END of moved fields

  protected BBlock // Entry BBlock of this subprogram.
  fEntryBBlock; // It should have block number 1.
  protected BBlock fExitBBlock;
  protected BBlock // Current basic block under processing
  fCurrentBBlock; // traversing IR (def/use anal, etc.)
  protected FAList // Table to get BBlock
  fBBlockTable = new FAList(20); // from its Id number.
  protected IrList // List of basic blocks
  fDfoList = null; // in depth-first order.
  protected IrList // List of basic blocks in inverse
  fInverseDfoList = null; // depth-first order from exit.
  protected List fReachableBBlocks;
  //##70 final FlowResults fResults;
  public final FlowResults fResults; //##70

//##25 BEGIN
  public boolean fHirAnalExtended = false;
  public RecordSetRefReprs fRecordSetRefReprs;
  // fSetRefReprTable and fBBlockOfIr are indexed
  // by IR node index.
  protected SetRefRepr[] fSetRefReprTable;
  protected BBlock[] fBBlockOfIR;
  protected int     // Min/Max of IR index for this subprogram.
    fIrIndexMin = 0, fIrIndexMax = 0;
  // ExpIdTable[i] contains FlowExpId of IR whose index is i.
  // It is set by AssignHashBasedFlowExpId.
  protected FlowExpId[] fExpIdTable;
  // Variables fSetRefReprTable, fIrIndexMin, fIrIndexMax,
  // and each item of fSetRefReprTable are
  // set by HirSubpFlowImpl (and LirSubpFlowImpl).

  protected boolean fRestructured = false; //##25-1
  protected java.util.Set fSetOfGlobalVariables = null;
  protected java.util.Set fSetOfAddressTakenVariables = null;
  public java.util.Set fSetOfFlowAnalSyms = null; //##94
  protected Map fTempExpCorrespondence; // Map to show correspondence
     // between temporal variable and expression represented by the
    /// temporal variable.  //##57
//##25 END

  protected RecordAlias fRecordAlias = null;  //##53

  //====== Constructor ======//
  public SubpFlowImpl(SubpDefinition pSubpDef, FlowResults pResults)
  {
    fSubpDefinition = pSubpDef;
    fResults = pResults;

    flowRoot = fResults.flowRoot; //##8
    flow = flowRoot.aflow;
    ioRoot = flowRoot.ioRoot; //##8
    symRoot = flowRoot.symRoot; //##8
    hirRoot = flowRoot.hirRoot; //##8
    //lirRoot = flowRoot.lirRoot; //##8 // 2004.06.01 S.Noishi

    flowRoot.subpUnderAnalysis = fSubpDefinition.getSubpSym();
    //##60            flowRoot.subpUnderAnalysis.setSubpFlow(this); //##41
    symRoot.symTableCurrent = fSubpDefinition.getSymTable();
    symRoot.symTableCurrentSubp = symRoot.symTableCurrent;
    fTempExpCorrespondence = new HashMap();  //##57
    //##78 if (flowRoot.flow != null) //##78
    //##78   ((coins.flow.FlowImpl)flowRoot.flow).fSubpFlowCurrent = this; //##78
    // Above statement overwrites coins.flow.SubpFlow. //##78
    flowRoot.aflow.dbg(1, "\ncoins.aflow.SubpFlowImpl",  //##94
      pSubpDef.getSubpSym().toString()); //##25;
    //##57 fSetOfGlobalVariables = new java.util.HashSet(); //Ogasawara 040901
    //##57 fSetOfAddressTakenVariables = new java.util.HashSet(); //Ogasawara 040901

  }

  //====== Methods to see/set characteristics of subprogram ======//

  public BBlock getEntryBBlock()
  {
    return fEntryBBlock;
  }

  public void setEntryBBlock(BBlock pBlock)
  {
    fEntryBBlock = pBlock;

    //    pBlock.setFlag(BBlock.IS_ENTRY, true);
  }
  // setEntryBBlock

  public BBlock getExitBBlock()
  {
    return fExitBBlock;
  }

  public void setExitBBlock(BBlock pBBlock)
  {
    fExitBBlock = pBBlock;
  }

  //  public BBlock
  //  getCurrentBBlock()
  //  {
  //    return fCurrentBBlock;
  //  }
  //  public void
  //  setCurrentBBlock( BBlock pBlock )
  //  {
  //    fCurrentBBlock = pBlock;
  //  } // setCurrentBBlock
  //====== Methods to create basic blocks ======//
  public BBlock bblock(LabeledStmt pLabeledStmt)
  {
    BBlock lBBlock;

    if (flowRoot.isHirAnalysis())
    {
      lBBlock = (BBlock) (BBlockHir) (new BBlockHirImpl(pLabeledStmt, this));
      recordBBlock(lBBlock);
    } else
    {
      throw new FlowError(
      "FlowRoot suggests LIR flow analysis is being performed now.");
    }

    return lBBlock;
  }
  // bblock

  /* 2004.05.31 S.Noishi
  public BBlock //###12
  bblock(coins.ir.lir.LabelDef pLabelDef)
  {
    BBlock lBBlock;

    if (flowRoot.isLirAnalysis())
    {
      lBBlock = (BBlock) (BBlockLir) (new BBlockLirImpl(pLabelDef, this));
      recordBBlock(lBBlock);
    } else
    {
      throw new FlowError(
      "FlowRoot suggests HIR flow analysis is being performed now.");
    }

    return lBBlock;
  }
  // bblock
  */

  /* 2004.05.31 S.Noishi
  public BBlock //##11  DELETE
  bblock(Prologue pPrologue)
  {
    BBlock lBBlock;

    if (flowRoot.isLirAnalysis())
    {
      lBBlock = (BBlock) (new BBlockLirImpl(pPrologue, this)); //##11
      recordBBlock(lBBlock);
    } else
    {
      throw new FlowError(
      "FlowRoot suggests HIR flow analysis is being performed now.");
    }

    return lBBlock;
  }
  // bblock
  */

  //====== Methods to create objects for data flow analysis ======//
  public BBlockVector bblockVector()
  {
    BBlockVector lBBlockVector = new BBlockVectorImpl(this);

    return lBBlockVector;
  }

  public ExpVector expVector()
  {
    ExpVector lExpVector = (ExpVector) (new ExpVectorImpl(this)); //##8

    return lExpVector;
  }
  // expVector

  public PointVector pointVector()
  {
    PointVector lPointVector = (PointVector) (new PointVectorImpl(this)); //##8

    return lPointVector;
  }
  // pointVector

  public DefVector defVector()
  {
    DefVector lDefVector = (DefVector) (new DefVectorImpl(this)); //##8

    return lDefVector;
  }
  // defVector

  public FlowAnalSymVector flowAnalSymVector()
  {
    return new FlowAnalSymVectorImpl(this);
  }

  public PointVectorIterator pointVectorIterator(PointVector pPointVector)
  {
    return (PointVectorIterator) (new PointVectorIteratorImpl(pPointVector)); //##8
  }
  // PointVectorIterator

  public DefVectorIterator defVectorIterator(DefVector pDefVector)
  {
    return (DefVectorIterator) (new DefVectorIteratorImpl(pDefVector)); //##8
  }
  // defVectorIterator

  public ExpVectorIterator expVectorIterator(ExpVector pExpVector)
  {
    return (ExpVectorIterator) (new ExpVectorIteratorImpl(pExpVector)); //##8
  }
  // expVectorIterator

  public BBlock getBBlock(int pBlockNumber)
  {
    BBlock lBBlock;
    lBBlock = (BBlock) (getBBlockTable().get(pBlockNumber));

    if (lBBlock != null)
    {
      return lBBlock;
    } else
    {
      return null;
    }
  }

  public void recordBBlock(BBlock pBlock)
  {
    ioRoot.dbgFlow.print(4, "recordBBlock "
      + pBlock.getBBlockNumber()); //##25
    fBBlockTable.add(pBlock);

    if (pBlock.getLabel() != null)
    {
      ioRoot.dbgFlow.print(5, " " + pBlock.getLabel().getName()); //##8
    }
  }
  // recordBBlock

  public int getNumberOfBBlocks()
  {
    return fBBlockTable.size();
  }

  public SubpDefinition getSubpDefinition()
  {
    return fSubpDefinition;
  }

  public Subp getSubpSym()
  {
    return fSubpDefinition.getSubpSym();
  }

  //  public void
  //  setPrevBBlockInSearch( BBlock pPrev )
  //  {
  //    fPrevBBlockInSearch = pPrev;
  //  }
  //
  //  public BBlock
  //  getPrevBBlockInSearch()
  //  {
  //    return fPrevBBlockInSearch;
  //  }
  //===== Iterators ======//

  /** cfgIterator:
   *  Traverse basic blocks in CFG (control flow graph)
   *  in depth first order.
   *  Available methods:
   *    next(), hasNext().
   **/
  public Iterator cfgIterator()
  {
//##41    if ((flowRoot.flow.getFlowAnalStateLevel() >= FlowRoot.STATE_CFG_AVAILABLE) &&
//##41    (fDfoList != null))
//##41    {
//##41      return fDfoList.iterator();
//##41    } else
    {
      ioRoot.msgRecovered.put(5011,
      "Incomplete flow information (cfgIterator)"); //##8

      //## Make depth first order list of BBlocks.
      return fDfoList.iterator();
    }
  }
  // cfgIterator

  public Iterator cfgFromExitIterator()
  {
//##41    if ((flowRoot.flow.getFlowAnalStateLevel() >= FlowRoot.STATE_CFG_AVAILABLE) &&
//##41    (fInverseDfoList != null))
//##41    {
//##41      return fInverseDfoList.iterator();
//##41    } else
    {
      ioRoot.msgRecovered.put(5012,
      "Incomplete flow information (cfgFromExitIterator)");

      //## Make inverse depth first order list of BBlocks.
      return fInverseDfoList.iterator();
    }
  }
  // cfgFromExitIterator

  //===== Flow analysis status ======//
  public FAList getBBlockTable()
  {
    if (fBBlockTable.isEmpty())
    {
      fResults.find("ControlFlowGraph", this);
    }

    return fBBlockTable;
  }

  public List getBBlocks()
  {
    return getReachableBBlocks();
  }

  public void setBBlocks(List pBBlocks)
  {
    fReachableBBlocks = pBBlocks;
  }

  public List getReachableBBlocks()
  {
    if (fReachableBBlocks == null)
    {
      ioRoot.msgRecovered.put(5555,
      "SubpFlowImpl: BBlocks not found. Has control flow graph been created?");
    }

    return fReachableBBlocks;
  }

  public void setReachableBBlocks(List pBBlocks)
  {
    flowRoot.aflow.dbg(2, "setReachableBBlocks", pBBlocks.toString()); //##25;
    fReachableBBlocks = pBBlocks;
  }

  public List getBBlocksFromEntry() // Performance aware version
  {
    return FlowUtil.bfSearch(getEntryBBlock(), getExitBBlock(), true);
  }

//##25 BEGIN
  public List getBBlocksFromExit()
  {
    return FlowUtil.bfSearch(getExitBBlock(), getEntryBBlock(), false);
  }
//##25 END

  public int getNumberOfRelevantBBlocks()
  {
    return getReachableBBlocks().size();
  }

  public FAList getFlowExpIdTable()
  {
    return (FAList) fResults.get("FlowExpIdTable", this);
  }

  public void setFlowExpIdTable(FAList pFlowExpIdTable)
  {
    ioRoot.dbgFlow.print(2, " setFlowExpIdTable", "to fResults"); //##56
    fResults.put("FlowExpIdTable", this, pFlowExpIdTable);
  }

  public FlowResults results()
  {
    return fResults;
  }

  public AssignFlowExpId assigner()
  {
    AssignFlowExpId lAssigner
    = (AssignFlowExpId) fResults.getRaw("AssignerForSubpFlow", this);

    if (lAssigner == null)
    {
      return flow.assigner(this);
      //         lAssigner = new HashBasedFlowExpIdHir(this);
      //            try {
      //                Constructor lConstr = assignerClass().getConstructor(new Class[] {
      //                            coins.aflow.SubpFlow.class
      //                        });
      //                lAssigner = (AssignFlowExpId) lConstr.newInstance(new Object[] {
      //                            this
      //                        });
      //            } catch (Exception e) {
      //                System.out.println(e);
      //                throw new FlowError("Assigner cannot be invoked.");
      //            }
    }

    return lAssigner;
  }

  //abstract protected Class assignerClass();

  public FAList getSetRefReprs()
  {
    flowRoot.aflow.dbg(5, "getSetRefReprs",
        this.getSubpSym().toString()); //##53
    return (FAList) fResults.get("SubpFlowSetReprs", this);
  }

  public void setSetRefReprs(FAList pSetReprs)
  {
    flowRoot.aflow.dbg(4, "setSetRefReprs",
       this.getSubpSym().toString()); //##53
    fResults.put("SubpFlowSetReprs", this, pSetReprs);
  }

//  public void trimBBlockTable()
//  {
//  }
//
  public Iterator cfgBfoIterator()
  {
    return FlowUtil.bfSearch(getEntryBBlock(), getExitBBlock(), true).iterator();
  }

  public DefUseList getDefUseList(FlowAnalSym pFlowAnalSym)
  {
    return (DefUseList) fResults.get("DefUseList", pFlowAnalSym, this);
  }

  public void setDefUseList(FlowAnalSym pFlowAnalSym, DefUseList pDefUseList)
  {
    fResults.put("DefUseList", pFlowAnalSym, this, pDefUseList);
  }

  public UDList getUDList(FlowAnalSym pFlowAnalSym)
  {
    return (UDList) fResults.get("UDList", pFlowAnalSym, this);
  }

  public void setUDList(FlowAnalSym pFlowAnalSym, UDList pUDList)
  {
    fResults.put("UDList", pFlowAnalSym, this, pUDList);
  }

  public FAList getSymIndexTable()
  {
    return (FAList) fResults.get("SymIndexTable", this);
  }

  public void setSymIndexTable(FAList pSymIndexTable)
  {
    fResults.put("SymIndexTable", this, pSymIndexTable);
  }

  public void makeDominatorTree()
  {
    new MakeDominatorTreeForSubpFlow(this).makeDominatorTree();

    //    fResults.find("Dominators", this);
  }

  public void makePostdominatorTree()
  {
    new MakePostdominatorTreeForSubpFlow(this).makePostdominatorTree();
  }

  public void initiateDataFlow()
  {
    ioRoot.dbgFlow.print(1, "initiateDataFlow",
      fSubpDefinition.getSubpSym().getName());  //##56
    fResults.find("Initiate", this);
  }
  public void findPDef()
  {
    fResults.find("PDef", this);
  }

  public void findDDefined()
  {
    fResults.find("DDefined", this);
  }

  public void findPDefined()
  {
    fResults.find("PDefined", this);
  }

  public void findDKill()
  {
    fResults.find("DKill", this);
  }

  public void findPKill()
  {
    fResults.find("PKill", this);
  }

  public void findDExposedUsed()
  {
    fResults.find("DExposed", this);
  }

  public void findPExposedUsed()
  {
    fResults.find("PExposed", this);
  }

  public void findDEGen()
  {
    fResults.find("DEGen", this);
  }

  public void findPEKill()
  {
    fResults.find("PEKill", this);
  }

  public void findDReach()
  {
    fResults.find("DReach", this);
  }

  public void findPReach()
  {
    fResults.find("PReach", this);
  }

  public void findDAvailInAvailOut()
  {
    fResults.find("DAvailIn", this);
  }

  public void findPLiveInLiveOut()
  {
    fResults.find("PLiveIn", this);
  }

  public void findDDefInDefOut()
  {
    fResults.find("DDefIn", this);
  }

  public void findDDefUse()
  {
    fResults.find("DDefUseList", this);
  }

  public void findDUseDef()
  {
    fResults.find("DUDList", this);
  }

  public void findDefUse()
  {
    fResults.find("DefUseList", this);
  }

  public void findUseDef()
  {
    fResults.find("UDList", this);
  }

//##25 BEGIN

public void
allocateSetRefReprTable()
{
  flowRoot.aflow.dbg(2, "allocateSetRefReprTable", "size " + fIrIndexMax + "-" + fIrIndexMin + "-1");
  fSetRefReprTable = new SetRefRepr[fIrIndexMax - fIrIndexMin + 1];
} // allocateSetRefReprTable

public SetRefRepr
getSetRefReprOfIR( int pIndex)
{
  if (fSetRefReprTable == null)
    allocateSetRefReprTable();
  return fSetRefReprTable[pIndex - fIrIndexMin];
} // getSetRefReprsOfIR

public void
setSetRefReprOfIR( SetRefRepr pSetRefRepr, int pIndex)
{
  if (fSetRefReprTable == null)
    allocateSetRefReprTable();
  fSetRefReprTable[pIndex - fIrIndexMin] = pSetRefRepr;
} // setSetRefReprOfIR

public void
correlateBBlockAndIR()
{
  // See HirSubpFlowImpl.
} // correlateBBlockAndIR

public void
allocateBBlockOfIR()
{
  flowRoot.aflow.dbg(2, "allocateBBlockOfIR", "size " + fIrIndexMax + "-" + fIrIndexMin + "-1");
  fBBlockOfIR = new BBlock[fIrIndexMax - fIrIndexMin + 1];
} // allocateBBlockOfIR

public BBlock
getBBlockOfIR( int pIndex)
{
  if (fBBlockOfIR == null) { //##52
    flowRoot.ioRoot.msgRecovered.put(5555,
       "getBBlockOfIR requires allocateBBlockOfIR. index " + pIndex);
      allocateBBlockOfIR();
  }
  if (pIndex < fIrIndexMin) { //##52
    flowRoot.ioRoot.msgRecovered.put(5555,
       "setBBlockOfIR with invalid index " + pIndex +
       " fIndexMin " + fIrIndexMin);
  }
  return fBBlockOfIR[pIndex - fIrIndexMin];
} // getBBlockOfIR

public void
setBBlockOfIR( BBlock pBBlock, int pIndex)
{
  if (fBBlockOfIR == null)
    allocateBBlockOfIR();
  if (pIndex < fIrIndexMin) { //##52
    flowRoot.ioRoot.msgRecovered.put(5555,
       "setBBlockOfIR with invalid index " + pIndex +
       " fIndexMin " + fIrIndexMin);
  }else
    fBBlockOfIR[pIndex - fIrIndexMin] = pBBlock;
} // setBBlockOfIR

public int
getIrIndexMin()
{
  return fIrIndexMin;
}

public int
getIrIndexMax()
{
  return fIrIndexMax;
}

public void
allocateExpIdTable()
{
  fExpIdTable = new FlowExpId[fIrIndexMax - fIrIndexMin + 1];
}

public FlowExpId
getExpId( IR pIr )
{
  if ((pIr != null)&&(fExpIdTable != null)&&
      (pIr.getIndex() >= fIrIndexMin)&&
      (pIr.getIndex() <= fIrIndexMax)&&    //##51
      (! getRestructureFlag())) //##25-1
    return fExpIdTable[pIr.getIndex() - fIrIndexMin];
  else
    return null;
} // getExpId

//##56 BEGIN
public FlowExpId
getExpId( IR pIr, int pIndex )
{
  if (pIr != null)
    return fExpIdTable[pIndex - fIrIndexMin];
  else
    return null;
} // getExpId
//##56 END

public void
setExpId( IR pIr, FlowExpId pFlowExpId )
{
  if ((pIr != null)&&(fExpIdTable != null)&&
      (pIr.getIndex() >= fIrIndexMin))
    fExpIdTable[pIr.getIndex() - fIrIndexMin] = pFlowExpId;
} // setExpId

public void
printExpIdTable()
{
  System.out.print("\nExpIdTable for " + getSubpSym());
  FlowExpId lExpId;
  IR lTree;
  if (fResults != null) {
    FAList lFlowExpIdTable = getFlowExpIdTable();
    if (lFlowExpIdTable != null) {
      for (Iterator lIterator = lFlowExpIdTable.iterator();
           lIterator.hasNext(); ) {
        lExpId = (FlowExpId)lIterator.next();
        if (lExpId != null) {
          lTree = lExpId.getLinkedNode();
          System.out.print("\n " + lExpId.toStringShort()+ " ");
          if (lTree instanceof HIR)
            System.out.print(FlowUtil.toString((HIR)lTree));
          else
            System.out.print(" " + lTree.toString());
        }
      }
    }
  }
} // printExpIdTable

public java.util.Set
setOfGlobalVariables()
{
  return fSetOfGlobalVariables;
}

public java.util.Set
setOfAddressTakenVariables()
{
  return fSetOfAddressTakenVariables;
}

public void
clear()
{
  fSetRefReprTable = null; //##25
  fIrIndexMin = fSubpDefinition.getNodeIndexMin(); //##25
  fIrIndexMax = fSubpDefinition.getNodeIndexMax(); //##25
  fExpIdTable = new FlowExpId[fIrIndexMax - fIrIndexMin + 1]; //##25-1
  fBBlockTable = new FAList(20); //##25
  fBBlockOfIR = new BBlock[fIrIndexMax - fIrIndexMin + 1]; //##25-1
  fSetOfAddressTakenVariables = null;
  fSetOfGlobalVariables = null;       //##57
  fSetOfFlowAnalSyms = null;   //##94
  fRecordAlias = null;   //##56
  flowRoot.ioRoot.dbgOpt1.print(2, "\n aflow.subpFlow.clear() IrIndex "
                                + fIrIndexMin + " - " + fIrIndexMax);
}

/** computeSetOfGlobalVariables:
 *  Compute the set of global variables and
 *  record it in fSetOfGlobalVariables.
 * @return the set of global variables.
 */
public java.util.Set
computeSetOfGlobalVariables()
{
  if (fSetOfGlobalVariables == null) {
    java.util.Set lGlobalVariables = new HashSet();
    Sym lSym;
    for (SymIterator lIterator = flowRoot.symRoot.symTableRoot.getSymIterator();
         lIterator.hasNext(); ) {
      lSym = (Sym) lIterator.nextVar();
      if (lSym != null)
        lGlobalVariables.add(lSym);
    }
    fSetOfGlobalVariables = lGlobalVariables;
    flowRoot.aflow.dbg(2, "\nglobalVariables ", lGlobalVariables.toString());
    return lGlobalVariables;
  }else {
    flowRoot.aflow.dbg(2, "\nglobalVariables ",
       fSetOfGlobalVariables.toString()); //##57
    return fSetOfGlobalVariables;
  }
}  // computeSetOfGlobalVariables

/** computeSetOfAddressTakenVariables:
 *  Compute the set of variables whose address is taken
 *   (e.g. (addr (var )), (addr (subs (var ) ... ) )
 *  for this subprogram definition.
 */

public java.util.Set
computeSetOfAddressTakenVariables()
{
  HIR lHir, lHirChild;
  Sym lSym;
  java.util.Set lAddressTakenVariables = new HashSet();
  fSetOfFlowAnalSyms = new HashSet(); //##94
  if (flowRoot.isHirAnalysis()) {
    HIR lSubpBody = fSubpDefinition.getHirBody();
     computeSetOfAddressTakenVariables(lSubpBody, lAddressTakenVariables,false);
  }else {
    // REFINE
  }
  fSetOfAddressTakenVariables = lAddressTakenVariables;  //##53
  flowRoot.aflow.dbg(2, "\naddressTakenVariables ", lAddressTakenVariables.toString());
  flowRoot.aflow.dbg(2, "\nSet of FlowAnalSyms ", fSetOfFlowAnalSyms.toString()); //##94
  return lAddressTakenVariables;
} // computeSetOfAddressedVariables

/** computeSetOfAddressTakenVariables:
 *  Compute the set of variables whose address is taken
 *   (e.g. (addr (var )), (addr (subs (var ) ... ) )
 *  within the given HIR subtree.
 * @param pHir: HIR subtree to be computed.
 * @param pSet: Set in which variables are recorded.
 * @param pAddrOperand: true if address is to be taken,
 *     false otherwise.
 */
public void
computeSetOfAddressTakenVariables(
          HIR pHir,
          java.util.Set pSet,
          boolean pAddrOperand  )
{
  HIR lHir, lHirChild;
  Sym lSym;
  if (pHir != null) {
    if (pHir instanceof VarNode) {
      lSym = pHir.getSymOrExpId();
      if (pAddrOperand && (lSym instanceof Var))
        pSet.add(lSym);
      if (lSym instanceof FlowAnalSym) { //##94
          fSetOfFlowAnalSyms.add(lSym);  //##94
      }                                  //##94
    }else {
      switch (pHir.getOperator()) {
      case HIR.OP_ADDR:
        computeSetOfAddressTakenVariables((HIR)pHir.getChild1(), pSet, true);
        break;
      case HIR.OP_SUBS:
        computeSetOfAddressTakenVariables((HIR)pHir.getChild1(), pSet, pAddrOperand);
        computeSetOfAddressTakenVariables((HIR)pHir.getChild2(), pSet, false);
        break;
      case HIR.OP_ARROW:
        computeSetOfAddressTakenVariables((HIR)pHir.getChild1(), pSet, false);
        computeSetOfAddressTakenVariables((HIR)pHir.getChild2(), pSet, pAddrOperand);
        break;
      //##53 BEGIN
      case HIR.OP_CONTENTS:
        computeSetOfAddressTakenVariables((HIR)pHir.getChild1(), pSet, true); // REFINE
        break;
      case HIR.OP_ADD:
      case HIR.OP_SUB:
        if (pHir.getType().getTypeKind() == Type.KIND_POINTER) { // Address computation.
          computeSetOfAddressTakenVariables((HIR)pHir.getChild1(), pSet, true); // REFINE
        }else {
          computeSetOfAddressTakenVariables((HIR)pHir.getChild1(), pSet, false);
        }
        computeSetOfAddressTakenVariables((HIR)pHir.getChild2(), pSet, false);
        break;
      case HIR.OP_LABELED_STMT:
        computeSetOfAddressTakenVariables((HIR)pHir.getChild2(), pSet, false);
        break;
      //##53 END
      case HIR.OP_BLOCK:
        for (Stmt lStmt = ((BlockStmt)pHir).getFirstStmt(); lStmt != null;
          lStmt = lStmt.getNextStmt()) {
          computeSetOfAddressTakenVariables(lStmt, pSet, false);
        }
        break;
      case HIR.OP_LIST:
        for (Iterator lIterator = ((HirList)pHir).iterator(); lIterator.hasNext(); ) {
          lHir = (HIR)lIterator.next();
          if (lHir != null)
            computeSetOfAddressTakenVariables(lHir, pSet, false);
        }
        break;
      default:
        if (pHir.getChildCount() > 0) {
          for (int i = 1; i <= pHir.getChildCount(); i++) {
            computeSetOfAddressTakenVariables((HIR)pHir.getChild(i), pSet, false);
          }
        }
      }
    }
  }
} // computeSetOfAddressedVariables

public void
setRestructureFlag()
{
  fRestructured = true;
}

public boolean
getRestructureFlag()
{
  return fRestructured;
}

//====== Methods to correlate with index number ======//

/* //###
public void //##20 Not used.
resetFlowSymLinkForRecordedSym() //##20
{
  Sym lSym;
  flowRoot.ioRoot.dbgFlow.print(4, "resetFlowSymLinkForRecordedSym",
    "SymExpCount " + getSymExpCount() + " UsedSymCount " + getUsedSymCount());
  for (int i = 0; i < getUsedSymCount(); i++) {
    lSym = getIndexedSym(i);
    if (lSym instanceof FlowAnalSym) {
      ((FlowAnalSym)lSym).resetFlowAnalInf();
    }
  }
  setSymExpCount(0);
  setUsedSymCount(0);
}

public void
resetFlowSymLink( SymTable pSymTable )
{
  java.util.Set lAccessedSyms;
  Sym           lSym;
  if (pSymTable == null)
    return;
  flowRoot.ioRoot.dbgFlow.print(4, "resetFlowSymLink",
      pSymTable.getOwnerName() );
  if ((pSymTable == flowRoot.symRoot.symTableRoot)||
      (pSymTable.getOwner() == null)) {
    resetGlobalFlowSymLink();
    return;
  }
  lAccessedSyms = fSubpDefinition.getSubpSym().getAccessedSyms();
  if (lAccessedSyms == null)  // Not yet analyzed.
    return;
  for (SymIterator lIterator = pSymTable.getSymIterator();
       lIterator.hasNext(); ) {
    lSym = lIterator.next();
    if ((lSym instanceof FlowAnalSym)&&
        (lAccessedSyms.contains(lSym))) {
      flowRoot.ioRoot.dbgFlow.print(4, " " + lSym.getName());
      //## if (lSym instanceof ExpId) //##20
      //##   lSym.remove();  //##20
      //## else //##20
        ((FlowAnalSym)lSym).resetFlowAnalInf();
    }
//##    if (lSym instanceof Subp)
//##      ((Subp)lSym).setSubpFlow(null);
  }
  resetFlowSymLink(pSymTable.getFirstChild());
  resetFlowSymLink(pSymTable.getBrother());
} // resetFlowSymLink

public void
resetGlobalFlowSymLink( )
{ //##11
  SymTable      lSymTableCurrent, lSymTable;
  Sym           lSym;
  lSymTableCurrent = getSubpDefinition().getSymTable();
  if (lSymTableCurrent == null)
    lSymTable = flowRoot.symRoot.symTableRoot;
  else {
    lSymTable = lSymTableCurrent.getParent();
    if (lSymTable == null)
      lSymTable = flowRoot.symRoot.symTableRoot;
  }
  flowRoot.ioRoot.dbgFlow.print(4, "resetGlobalFlowSymLink",
      "owner " + lSymTable.getOwnerName() );
  resetFlowSymLinkForTable(lSymTable);
} // resetGlobalFlowSymLink

public void
resetFlowSymLinkForTable( SymTable pSymTable )
{
  Sym           lSym;
  if (pSymTable == null)
    return;
  for (SymIterator lIterator = pSymTable.getSymIterator();
       lIterator.hasNext(); ) {
    lSym = lIterator.next();
    if (lSym instanceof FlowAnalSym) {
      flowRoot.ioRoot.dbgFlow.print(4, " " + lSym.getName());
      ((FlowAnalSym)lSym).resetFlowAnalInf();
    }
  }
  resetFlowSymLinkForTable(pSymTable.getParent());
} // resetFlowSymLinkForTable
*/ //###

//##25 END


//##53 BEGIN

public void
setRecordAlias( RecordAlias pRecordAlias )
{
  fRecordAlias = pRecordAlias;
  ioRoot.dbgFlow.print(4, " setRecordAlias",
      fSubpDefinition.getSubpSym().getName());  //##56
}

public RecordAlias
getRecordAlias()
{
  return fRecordAlias;
}
//##53 END

//##57 BEGIN

public void
setExpOfTemp( Var pTempVar, Exp pExp )
{
  fTempExpCorrespondence.put(pTempVar, pExp);
}

public Exp
getExpOfTemp( Var pTempVar )
{
  if (fTempExpCorrespondence.containsKey(pTempVar))
    return (Exp)fTempExpCorrespondence.get(pTempVar);
  else
    return null;
}
//##57 END

}
// SubpFlowImpl class
