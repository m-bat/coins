/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.flow;

import java.util.ArrayList;
import java.util.Collection; //##78
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map; //##60
import java.util.HashMap; //##60
import java.util.HashSet; //##60
import java.util.LinkedList; //##60
import java.util.Set; //##60

import coins.FlowRoot;
import coins.HirRoot;
import coins.IoRoot;
//##60 import coins.LirRoot;
import coins.SymRoot;
import coins.alias.AliasAnal; //##62
import coins.alias.AliasAnalHir1; //##62
import coins.alias.alias2.AliasAnalHir2; //##62
import coins.alias.RecordAlias; //##60
import coins.driver.CoinsOptions;
import coins.ir.IR;
import coins.ir.IrList;
import coins.ir.hir.AsmStmt; //##70
import coins.ir.hir.AssignStmt; //##60
import coins.ir.hir.BlockStmt; //##60
import coins.ir.hir.Exp; //##60
import coins.ir.hir.HIR; //##60
import coins.ir.hir.HIR_Impl; //##63
import coins.ir.hir.HirList; //##60
import coins.ir.hir.LabeledStmt; //##60
import coins.ir.hir.Stmt; //##60
import coins.ir.hir.SubpDefinition;
import coins.ir.hir.VarNode; //##60

//##60 import coins.ir.lir.DefLabel;
//##60 import coins.ir.lir.Prologue;
//##60 import coins.sym.AReg;
import coins.sym.ExpId;
import coins.sym.FlowAnalSym;
import coins.sym.Label; //##60
import coins.sym.Subp;
import coins.sym.Sym;
import coins.sym.SymIterator;
import coins.sym.SymTable;
import coins.sym.SymTableImpl; //##78
import coins.sym.Var; //##60
import coins.sym.Type; //##60

//      (): Jan. 2002.

/** SubpFlowImpl class
 *
 *  Subprogram flow analysis class.
 *  Fields and methods are common between HIR and LIR.
 **/
public abstract class
//## SubpFlowImpl extends FlowImpl implements SubpFlow
  SubpFlowImpl
  implements SubpFlow
{

//====== Fields ======

  public final FlowRoot flowRoot;
  public final IoRoot ioRoot;
  public final SymRoot symRoot;
  public final HirRoot hirRoot;
  public final Map fOptionMap; //##101
  public final CoinsOptions fOptions; //##101
  public final String fHirOpt; //##101
  public int fComplexityAllowanceNumber; //##101

  public Flow flow;
//##60   public   LirRoot   lirRoot = null;

// BEGIN Fields moved from FlowImpl

//  protected  ArrayList   fSymIndexTable = new ArrayList(100);
  protected int fUsedGlobalSymCount = 0;
  protected int // Numbers used in BitVector.
    fPointVectorBitCount, // PointVector  bit count. (fDefRefCount)
    fPointVectorWordCount, // PointVector  long word count.
    fExpVectorBitCount, // ExpVector  bit count. (fSymExpCount)
    fExpVectorWordCount, // ExpVector  long word count.
    fDefVectorBitCount, // DefVector  bit count. (fDefCount) //##60
    fDefVectorWordCount, // DefVector  long word count. //##60
    fBBlockVectorBitCount, // BBlockVector bit count. (fBBlockCount)
    fBBlockVectorWordCount; // BBlockVector long word count.
    // FlowAnalSymVector bit position is the same to Sym index
    // which id given in recordSym method and reset by resetFlowSymLink.
    // ExpVector bit position is the same to the index number of
    // ExpId corresponding to the expression.
    // Point vector bit position is (node index - fIrIndexmin).
  protected SubpDefinition // Defining node of subprogram
    fSubpDefinition; // to be flow-analyzed.

  protected BBlock
    fPrevBBlockInSearch; // Previous BBlock in

// END of moved fields

  protected int // Number of nodes to be treated in
    fNodeCount = 0; // data flow analysis.

  protected IR[] // Get IR node from IR node index.
    fFlowIrLink; // This is set by divideHirIntoBasicBlocks.
                 // This is used in getIndexedNode.

  protected int // Size of fFlowIrLink.
    fFlowIrLinkSize; // (Number of nodes)

  protected int
    fBBlockCount = 0; // Number of basic blocks created.

  protected int
    fUsedSymCount = 0; // Number of source symbols actually defined
  // or used in the current subprogram.

  protected int // fUsedSymCount +
    fSymExpCount = 0; // (number of generated ExpId in current
                      //  subprogram)
  protected int
    fDefRefCount = 0; // Number of Def/Ref positions.

  protected int
    fDefCount = 0; // Number of Def positions. //##62

  protected BBlock // Entry BBlock of this subprogram.
    fEntryBBlock = null; // It should have block number 1.

  protected int
    fAssignCount = 0; // AssignStmt count //##62
      // Used in DataFlowImple to make fDefNodeIndexTable, etc.

  protected int
    fCallCount = 0; // Call node count in this subprogram. //##62

  protected BBlock // Exit BBlock of this subprogram.
    fExitBBlock = null; // If there are multiple BBlocks having
  // no successors, then one of them is selected.

  protected ArrayList // Table to get BBlock
    fBBlockTable = null; // from its Id number.
  // This table records all basic blocks
  // in subpUnderAnalysis in the order of
  // BBlock instanciation.
  // Position 0 is null.

  protected List fBBlockList = null; //##63
  // List of BBlocks relevant for DFA.
  // BBlocks that are never visited may not be added in recordBBlocks.
  // (Moved from DataFlowImpl.) //##63

  protected List // List of BBlocks reachable from entry BBlock.
    fListOfBBlocksFromEntry = null, //##63
    fListOfBBlocksFromExit  = null; //##63

  protected Set // Record symbol used/defined in this subprogram.
    fUsedSymSet = null; // (source symbol and ExpId)

  protected FlowAnalSym[]   // Table to get Sym
    fSymIndexTable = null;  // from its index number. //##62
    // Source symbols are recorded first
    // (in fSymIndexTable[1-fUsedSymCount]) and then
    // ExpIds are recorded because source symbols are indexed
    // in recordSym which is called from divideHirIntoBasicBlocks
    // at first and then called from selectExpId.
    // fSymIndexTable[0] is null.

  // This table records variables and registers
  // used in executable Stmt/LIRTree.
  protected List // List of basic blocks
    fDfoList = null; // in depth-first order.

  protected List // List of basic blocks in inverse
    fInverseDfoList = null; // depth-first order from exit.

  protected int
    //##62 fExpIdBase = 0;
    fExpIdNumber = 0; // Sequence number to generate ExpIdName.
                      // This is not the same to ExpId index. //##62

  protected java.util.Set
    fDefinedSyms = null; // FlowAnalSym defined/used in this subprogram.
    //##62 fUsedSyms = null, // FlowAnalSym used  in this subprogram.
    //##62 fDefinedUsedSyms = null; // FlowAnalSym defined/used in this subp.

  protected FlowAnalSym[]
    fFlowAnalSymTable = null; // FlowAnalSym defined/used in this
     // subprogram. fFlowAnalSymTable is indexed by Sym index,
     // while fDefinedSyms is a set that shows whether a symbol
     // appeared or not.

  //##60 BEGIN

  protected BBlock // Table to get BBlock for each IR
    fBBlockOfIR[]; // from index number of the IR.

  protected int[] // Indexed by IR index.
    fDefRefIndex; // Symbol definition/reference point number. //##62
  // 0 if the cell that does not define/refer a symbol directly.

  protected int[] // Indexed by IR index.
    fDefIndex; // Symbol definition index number //##62
  //  for the IR (PointVector bit position).
  // 0 if the cell that does not set a symbol directly. //##62

  protected Map // Map to get BBlock for each Label
    fBBlockOfLabel = null;

  //##62 protected Map // Map to get subtree linked to ExpId.
  //##62   fLinkedSubtreeOfExpId = null;

  protected List // List of ExpId in the order of creation.
    fExpIdList = null;

  protected Set  // Set of ExpIds corresponding to maximal compound variables
    fMaximalCompoundVars = null; // which are subscripted variables, maximal
                                 // struct/union elements,
                                 // maximal pointer qualifications. //##65

  protected Set
    fGlobalSymsUsed = null;

  protected DefUseList
    fDefUseList = null;  //##63

  protected UseDefList
    fUseDefList = null;  //##63

  //##73 BEGIN
  protected DefUseList
    fDefUseExhaustiveList = null;

  protected UseDefList
    fUseDefExhaustiveList = null;
  //##73 END

  protected List         // List of Def nodes for each symbol.
    fDefNodeListOfSym[]; // Indexed by FlowAnalSym.getIndex(). //##63

  protected List         // List of Use nodes for each symbol.
    fUseNodeListOfSym[]; // Indexed by FlowAnalSym.getIndex(). //##63

//##25 BEGIN
  public boolean fHirAnalExtended = false;
  //##60 public RecordSetRefReprs fRecordSetRefReprs;
  // fSetRefReprTable and fBBlockOfIr are indexed
  // by IR node index.
  protected SetRefRepr[] fSetRefReprTable = null;
  // SetRefRepr[i] is SetRefRepr of the node with index i.
  //##60 protected BBlock[] fBBlockOfIR;
  protected int // Min/Max of IR index for this subprogram.
    fIrIndexMin = 0, fIrIndexMax = 0;
  // fExpIdTable[i] contains ExpId of IR whose index is i.
  // It is set by allocateExpIdToNode.
  protected ExpId[] fExpIdTable; //##60

  // Child1 of AssignStmt etc. are copied during ExpId computation and
  // fMaxIndexOfCopiedNode represents the max value of index for copied nodes.
  //##83 protected int fMaxIndexOfCopiedNode; //##65
  public int fMaxIndexOfCopiedNode; //##83

  public List fSubtreesCopied; //##68
  // Subtrees copied in ExpId allocation for r-value Exp.

 // Variables fSetRefReprTable, fIrIndexMin, fIrIndexMax,
  // and each item of fSetRefReprTable are
  // set by HirSubpFlowImpl (and LirSubpFlowImpl).

  //##62 protected boolean fRestructured = false; //##25-1
  protected java.util.Set fSetOfGlobalVariables = null;
  protected java.util.Set fSetOfAddressTakenVariables = null;
  protected Map fTempExpCorrespondence; // Map to show correspondence
  // between temporal variable and expression represented by the
  /// temporal variable.  //##57
//##25 END
  protected AliasAnal fAlias = null; //##62
  protected RecordAlias fRecordAlias = null; //##53

  //protected int
  // fDefVectorLength,
  // fDefCount = 0;

  protected IR[] fDefRefPoint; // Indexed by fDefRefPosition[nodeIndex] (fDefRefCount)
    // fDefRefPoint is used to get IR node from PointVector bit position.
  protected IR[] fDefPoint; // Indexed by fDefPosition[nodeIndex] (fDefCount) //##62
  // fDefPoint is used to get IR node from PointVector bit position.
  // protected IR[] fRefPoint;  // Indexed by fDefRefPosition[nodeIndex] (fDefRefCount)
  public boolean[] hasCall; // Indexed by BBlock number.
    // hasCall is used to see if a basic block contains call.
  // hsaCallInSubp, hasUsePointer, hsaStructUnion, hasPointerAssign
  // are not used currently (but may be used in lparallel).
  public boolean hasCallInSubp; // true if this subprogram has call. //##62
  public boolean[] hasUsePointer; // Indexed by BBlock number.
  public boolean[] hasStructUnion; // Indexed by BBlock number.
  public boolean[] hasPointerAssign; // Indexed by BBlock number.
  protected SetRefReprList[] fArrayOfSetRefReprList; // Indexed by BBLockNumber.
    // List of SetRefReprs in BBlock (which is set in computeBBlockSetRefReprs).
  protected BBlockVector[] fDom = null;     // Dominators for each BBlock. //##70
  protected BBlockVector[] fPostDom = null; // Post Dominators for each BBlock. //##70
  protected List[] fDomList = null;     // Dominator list for each BBlock. //##70
  protected List[] fPostDomList = null; // Post dominator list for each BBlock. //##70
  public Set fSubtreesContainingCall; // Set of subtrees containing call.
  protected int fComputedFlag[]  // Indexed by CF_INDEXED, ... DF_DEFUSELIST.
                 = new int[DF_MAX + 1]; //##62
  protected int fLabelDefCount = 0; // Number of labels defined. //##100
    // fComputedFlag[i] is i if i-th item is already computed,
    //   1 if under computation, 0 if not yet computed.
    // If fComputedFlag[i] is true then item i is not recomputed but
    // reused.
  public final int
    fDbgLevel;   // Cache of ioRoot.dbgFlow.getLevel().
  protected int  // Complexity level of this subprogram. //##62
    fComplexity; // 1: simple. Do full analysis.
                 // 2: medium. Alias analysis is simplified.
                 // 3: complex. Alias analysis and data flow analysis are simplified.
  protected final int // fComplexity boundary number. //##62
    fNodeCountLim1 =  1000, // node count upper lim for simple.
    fNodeCountLim2 =  3000, // node count upper lim for medium.
    fNodeCountLim3 = 10000, // node count upper lim for complex.           //##100
    fNodeCountLim4 = 30000, // node count upper lim for extremely complex. //##100
    fSymCountLim1  =   100, // used symbol count upper lim for simple.
    fSymCountLim2  =   200, // used symbol count upper lim for medium.
    fSymCountLim3  =   300, // symbol count upper lim for complex.           //##100
    fSymCountLim4  =   500; // symbol count upper lim for very complex. //##100
    // Above numbers will be multiplied by the complexityAllowance level of 
    // HIR optimization option.                                         //##101
  //##60 END
  //##100 BEGIN
  protected final int // fComplexity boundary numbers.
    fBBlockCountLim1   =  20,
    fBBlockCountLim2   = 100,
    fBBlockCountLim3   = 400,
    fBBlockCountLim4   = 800,
    fLabelDefCountLim1 =  20,
    fLabelDefCountLim2 = 100,
    fLabelDefCountLim3 = 400,
    fLabelDefCountLim4 = 800;
  // Above numbers will be multiplied by the complexityAllowance level of 
  // HIR optimization option.                                         //##101 
  //##100 END

  protected FlowAdapter
    fFlowAdapter;       //##63

  public boolean  //##65
    fIteratorInitiated = false; // True if fStmtExpSeq is initiated by
      // BBlockSubtreeIterator. It is set to false when HIR was modified
      // to show that fStmtExpSeq is no more effective (See FlowUtil).
      // If fIteratorInitiated is true, BBlockSubtreeIterator runs in low cost.
      // If fIteratorInitiated is false, BBlockSubtreeIterator runs in high cost.

  public Map
    fMultipleSetRef = null; //##70
      // Record multiple-set-nodes (such as asm(....))
      // each of which sets multiple data.
      // This maps multiple-set-node to a sequence of list of the form
      //   (seq (list referred-nodes) (list set-nodes) (list write-nodes)
      // where referred-nodes are subtree whose value is referred and not set,
      // set-nodes are subtree whose value is set,
      // write-nodes are subtree whose value is set and not referred.
      // These lists may contain the same node when it is referred and set.
      // This map is made by computeSetRefRepr of SetRefReprHirEImpl.

  protected boolean failed = false; //##78
        // This is initially set false in
        // When control/data flow analysis failed, then "failed" is set to true,
        // otherwise it is set to false.

//---- Fields inherited from FlowImpl ----

//  public final FlowRoot flowRoot;
//  public final IoRoot   ioRoot;
//  public final SymRoot  symRoot;
//  public final HirRoot  hirRoot;

//  public   Flow  flow;
//  public   LirRoot   lirRoot = null;
//  public   ControlFlow fControlFlow;
//  public   DataFlow  fDataFlow;
//  protected  FlagBox   fFlowAnalState;
//  protected  int   fFlowAnalStateLevel;
//  protected  ArrayList   fSymIndexTable = new ArrayList(100);
//  protected  int   fUsedGlobalSymCount = 0;
//  protected  int    // Numbers used in BitVector.
//  fPointVectorBitCount,   // PointVector  bit count.
//  fPointVectorWordCount,  // PointVector  long word count.
//  fExpVectorBitCount,   // ExpVector  bit count.
//  fExpVectorWordCount,  // ExpVector  long word count.
//  fBBlockVectorBitCount,  // BBlockVector bit count.
//  fBBlockVectorWordCount; // BBlockVector long word count.

protected coins.aflow.SubpFlow
   fAflowSubpFlow = null;        //##73

//====== Constructor ======

  public
    SubpFlowImpl(FlowRoot pFlowRoot, SubpDefinition pSubpDefinition) //##60
  {
    flowRoot = pFlowRoot;
    ioRoot = pFlowRoot.ioRoot;
    symRoot = pFlowRoot.symRoot;
    //-- Pass information from ControlFlow to DataFlow.
    hirRoot = pFlowRoot.hirRoot;
    hirRoot.attachFlowRoot(flowRoot); // hirRoot may have different flowRoot. //##65
    //##60   lirRoot     = pFlowRoot.lirRoot;
    flow = pFlowRoot.flow;
    fDbgLevel = ioRoot.dbgFlow.getLevel();
    if (fDbgLevel > 0)
      ioRoot.dbgFlow.print(2, "\nSubpFlowImpl",
        pSubpDefinition.getSubpSym().getName());
    if (flowRoot.fSubpFlow != null) {
      if (flowRoot.fSubpFlow.getSubpSym() != pSubpDefinition.getSubpSym()) {
        if (fDbgLevel > 0)
          ioRoot.dbgFlow.print(2, "reset previous flow information",
            flowRoot.fSubpFlow.getSubpSym().getName());
        flowRoot.fSubpFlow.clearControlFlow(); //##60
        flowRoot.fSubpFlow.clearDataFlow(); //##60
      }
    }
    // by HirSubpFlowImpl
    //##60 BEGIN
    fSubpDefinition = pSubpDefinition;
    flowRoot.fSubpFlow = (SubpFlow)this; // This should be overridden
    if (pSubpDefinition != null) {
      pFlowRoot.subpUnderAnalysis = pSubpDefinition.getSubpSym();
      //##60 pFlowRoot.subpUnderAnalysis.setSubpFlow((SubpFlow)this);
      if (pSubpDefinition.getSymTable() != null) {
        symRoot.symTableCurrent = pSubpDefinition.getSymTable();
        symRoot.symTableCurrentSubp = symRoot.symTableCurrent;
      }
    }
    //##60 END
    fSubtreesContainingCall = new HashSet(); //##60
    fComplexity = 1; //##62
    //##63 BEGIN
    fFlowAdapter = new FlowAdapter(flowRoot);
    //##63 END
    //##101 BEGIN
    fOptions = ioRoot.getCompileSpecification().getCoinsOptions();
    fHirOpt = fOptions.getArg("hirOpt");
    if (fHirOpt != null) { 
      fOptionMap = fOptions.parseArgument(fHirOpt, '/', '.');
     }else {
      fOptionMap = new HashMap();
    }
    String lAllowanceLevelString = "1";
    fComplexityAllowanceNumber = 1;
    if (fOptionMap.containsKey("complexityAllowance")) {
      lAllowanceLevelString = (String)fOptionMap.get("complexityAllowance");
      if (lAllowanceLevelString.length() > 0) {
        char lAllowanceLevel = lAllowanceLevelString.charAt(0);
        int lComplexityAllowanceNumber = (int)lAllowanceLevel - (int)'0';
        if ((lComplexityAllowanceNumber > 0)&&
        	(lComplexityAllowanceNumber <= 9)) {
    	  fComplexityAllowanceNumber = lComplexityAllowanceNumber;
        }else {
    	  ioRoot.msgWarning.put("hirOpt complexityAllowance is bad. Ignore it.");
        }
      }else {
    	ioRoot.msgWarning.put("hirOpt complexityAllowance requires number. Ignore the option."); 
      }
    }
    if (fDbgLevel > 0)
        ioRoot.dbgFlow.print(2, "fComplexityAllowanceNumber= " +
      		  fComplexityAllowanceNumber);
    //##101 END

  } // subpFlowImpl

  public
    SubpFlowImpl()
  {
    flowRoot = null;
    ioRoot = null;
    symRoot = null;
    hirRoot = null;
    fDbgLevel = 0;
    fOptions = null;
    fHirOpt  = null;
    fOptionMap = null;
    //##101 BEGIN
    //##101 END
  }

//====== Methods to see/set characteristics of subprogram ======

  public BBlock
    getEntryBBlock()
  {
    return fEntryBBlock;
  }

  public void
    setEntryBBlock(BBlock pBlock)
  {
    fEntryBBlock = pBlock;
    fEntryBBlock.setFlag(BBlock.IS_ENTRY, true);
  } // setEntryBBlock

  public BBlock
    getExitBBlock()
  {
    return fExitBBlock;
  }

  public void
    setExitBBlock(BBlock pBlock)
  {
    fExitBBlock = pBlock;
    fExitBBlock.setFlag(BBlock.IS_EXIT, true);
  } // setExitBBlock
  public void // Copy flow analysis data
    copyFlowAnalData(SubpFlow pSubpFlow)
  { // of HirSubpFlow to LirSubpFlow.

    fBBlockCount = ((SubpFlowImpl)pSubpFlow).fBBlockCount; //##?

  } // copyFlowAnalData

//====== Methods to create basic blocks ======

  public BBlock
    bblock(LabeledStmt pLabeledStmt)
  {
    BBlock lBBlock;
    fBBlockCount++;
    lBBlock = (BBlock)(BBlockHir)(new BBlockHirImpl(flowRoot,
      pLabeledStmt, fBBlockCount));
    flowRoot.fSubpFlow.recordBBlock(lBBlock, fBBlockCount);
    return lBBlock;
  } // bblock

  public BBlock
    bblock() // Create dummy BBlock.
  {
    BBlock lBBlock;
    lBBlock = (BBlock)(new BBlockHirImpl(flowRoot,
      (LabeledStmt)null, 0));
    return lBBlock;
  } // bblock
/* //##78
  public void
    attachLoopInf()
  {}
*/ //##78
//====== Methods to create objects for data flow analysis ======

  public ExpVector
    expVector()
  {
    ExpVector lExpVector = (ExpVector)(new ExpVectorImpl(this));
    return lExpVector;
  } // expVector

  public PointVector
    pointVector()
  {
    PointVector lPointVector = (PointVector)
      (new PointVectorImpl(this));
    return lPointVector;
  } // pointVector

  public DefVector
    defVector()
  {
    DefVector lDefVector = (DefVector)(new DefVectorImpl(this));
    return lDefVector;
  } // defVector

//====== Methods to correlate with index number ======

  public void
    resetFlowSymLinkForRecordedSym()
  {
    Sym lSym;
    if (fDbgLevel > 0) {
      flowRoot.ioRoot.dbgFlow.print(2, "resetFlowSymLinkForRecordedSym",
        "SymExpCount " + getSymExpCount()  + " " +
        fSymExpCount + " UsedSymCount " + getUsedSymCount());
      if (fSymIndexTable != null)
        flowRoot.ioRoot.dbgFlow.print(2, " fUsedSymSet.size " + fUsedSymSet.size());
    }
    if (fSymIndexTable != null) {
      for (int i = 0; i < fSymExpCount; i++) {
        lSym = getIndexedSym(i);
        if (lSym instanceof FlowAnalSym) {
          if (fDbgLevel > 0)
            flow.dbg(6, " " + lSym.getName()); //##62
          ((FlowAnalSym)lSym).resetFlowAnalInf();
        }
      }
    }
    //##78 resetFlowSymLinkForTable(symRoot.symTableFlow); //##78
    //##78 resetFlowSymLinkForTable(symRoot.symTableCurrent); //### //##78
    //##62 fSymExpCount = 0;
    //##62 fUsedSymCount = 0;
  } // resetFlowSymLinkForRecordedSym

  public void
    resetFlowSymLink(SymTable pSymTable)
  {
    java.util.Set lAccessedSyms;
    Sym lSym;
    if (pSymTable == null)
      return;
    if (fDbgLevel > 0)
      flowRoot.ioRoot.dbgFlow.print(2, "resetFlowSymLink",
        ((SymTableImpl)pSymTable).fTableName + //##78
        " owner " + pSymTable.getOwnerName());
    if ((pSymTable == flowRoot.symRoot.symTableRoot) ||
        (pSymTable.getOwner() == null)) {
      resetGlobalFlowSymLink();
      return;
    }
    //##60 lAccessedSyms = fSubpDefinition.getSubpSym().getAccessedSyms();
    //##60 if (lAccessedSyms == null) // Not yet analyzed.
    if (fSymIndexTable == null) // Not yet analyzed. //##60
      return;
    lAccessedSyms = new HashSet(); //##60
    //##62 BEGIN
    Sym lUsedSym;
    for (int lUsedSymIndex = 0; lUsedSymIndex < fSymExpCount; lUsedSymIndex++) {
      lUsedSym = fSymIndexTable[lUsedSymIndex];
      if (lUsedSym != null)
      lAccessedSyms.add(lUsedSym);
    }
    //##62 END
    for (SymIterator lIterator = pSymTable.getSymIterator();
         lIterator.hasNext(); ) {
      lSym = lIterator.next();
      if ((lSym instanceof FlowAnalSym) &&
          (lAccessedSyms.contains(lSym))) {
        if (fDbgLevel > 3)
          flowRoot.ioRoot.dbgFlow.print(4, " " + lSym.getName());
          //## if (lSym instanceof ExpId)
          //##   lSym.remove();
          //## else
        ((FlowAnalSym)lSym).resetFlowAnalInf();
      }
//##  if (lSym instanceof Subp)
//##    ((Subp)lSym).setSubpFlow(null);
    }
    resetFlowSymLink(pSymTable.getFirstChild());
    resetFlowSymLink(pSymTable.getBrother());
  } // resetFlowSymLink

  public void
    resetGlobalFlowSymLink()
  {
    SymTable lSymTableCurrent, lSymTable;
    Sym lSym;
    lSymTableCurrent = getSubpDefinition().getSymTable();
    if (lSymTableCurrent == null)
      lSymTable = flowRoot.symRoot.symTableRoot;
    else {
      lSymTable = lSymTableCurrent.getParent();
      if (lSymTable == null)
        lSymTable = flowRoot.symRoot.symTableRoot;
    }
    if (fDbgLevel > 0)
      flowRoot.ioRoot.dbgFlow.print(2, "resetGlobalFlowSymLink",
        "owner " + lSymTable.getOwnerName());
    resetFlowSymLinkForTable(lSymTable);
  } // resetGlobalFlowSymLink

  public void
    resetFlowSymLinkForTable(SymTable pSymTable)
  {
    Sym lSym;
    if (pSymTable == null)
      return;
    if (fDbgLevel > 0)
      flowRoot.ioRoot.dbgFlow.print(3, "resetFlowSymLinkForTable",
        ((SymTableImpl)pSymTable).fTableName + " owner " + pSymTable.getOwnerName());
    for (SymIterator lIterator = pSymTable.getSymIterator();
         lIterator.hasNext(); ) {
      lSym = lIterator.next();
      if (lSym instanceof FlowAnalSym) {
        if (fDbgLevel > 3)
          flowRoot.ioRoot.dbgFlow.print(4, " " + lSym.getName());
        ((FlowAnalSym)lSym).resetFlowAnalInf();
      }else if (lSym instanceof Label) { //##62
        //##62 ((Label)lSym).resetHirRefList(); //##62
      }
    }
    resetFlowSymLinkForTable(pSymTable.getParent());
  } // resetFlowSymLinkForTable

  public BBlock
    getBBlock(int pBlockNumber)
  {
    BBlock lBBlock;
    lBBlock = (BBlock)(fBBlockTable.get(pBlockNumber));
    if (lBBlock != null) {
      if (lBBlock.getFlag(BBlock.IS_DELETED))
        return null;
      else
        return lBBlock;
    }
    else
      return null;
  }

  public void
    recordBBlock(BBlock pBlock, int pBlockNumber)
  {
    if (fDbgLevel > 3)
      ioRoot.dbgFlow.print(4, "recordBBlock",
        Integer.toString(pBlockNumber, 10));
    fBBlockTable.add(pBlockNumber, pBlock);
    if (pBlock.getLabel() != null) {
      if (fDbgLevel > 3)
        ioRoot.dbgFlow.print(4, " " + pBlock.getLabel().getName());
    }
    if (pBlockNumber == 1)
      setEntryBBlock(pBlock);
  } // recordBBlock

  public FlowAnalSym
    getIndexedSym(int pSymIndex)
  {
    //##62 if (pSymIndex < fSymIndexTable.size()) return (FlowAnalSym)(fSymIndexTable.get(pSymIndex));
    if (pSymIndex < fSymExpCount) //##62
      return fSymIndexTable[pSymIndex]; //##62
    else //##60
      return null; //##60
  }

  protected int //##62
    recordSym(FlowAnalSym pSym) //##62
  {
    if (pSym != null) {
      if (fDbgLevel > 3)
        ioRoot.dbgFlow.print(6, "recordSym", ((Sym)pSym).getName()); //##62
      if (! fUsedSymSet.contains(pSym)) {
        fSymExpCount++; //##62
        fUsedSymSet.add(pSym); //##62
        pSym.setIndex(fSymExpCount); //##62
        if (pSym.isGlobal()) { //##60
          fGlobalSymsUsed.add(pSym); //##60
          if (pSym instanceof Var)
            fSetOfGlobalVariables.add(pSym); //##62
        }
        if (fDbgLevel > 3)
          ioRoot.dbgFlow.print(6, " index " + fSymExpCount); //##62
      }
      return pSym.getIndex(); //##62
    }
    return 0; //##62
  } // recordSym

  public IR
    getIndexedNode(int pNodeIndex)
  {
    IR lIr;
    if (fDbgLevel > 3)
      ioRoot.dbgFlow.print(7, "getIndexedNode", "(" + pNodeIndex + ")");
    lIr = fFlowIrLink[pNodeIndex - fIrIndexMin]; //##60
    if (fDbgLevel > 3)
      ioRoot.dbgFlow.print(7, ioRoot.toStringObjectShort(lIr)); //##62
    return lIr;
  }

  public BBlock
    getBBlockFromNodeIndex(int pNodeIndex)
  {
    return fBBlockOfIR[pNodeIndex - fIrIndexMin]; //##60
  }

  public int
    getNumberOfBBlocks()
  {
    return fBBlockCount;
  }

  public int
    getNumberOfNodes() //## DELETE
  {
    return fNodeCount;
  }
  //##100 BEGIN
 public int
   getLabelDefCount()
 {
	 return fLabelDefCount;
 }
 //##100 END
  public void
    setNumberOfNodes(int pCount) //## DELETE
  {
    fNodeCount = pCount;
  }

  public int
    getSymExpCount()
  {
    return fSymExpCount;
  }

  public int
    getUsedSymCount()
  {
    return fUsedSymCount;
  }


  public String //  Moved from ExpIdImpl
    generateExpIdName()
  {
    String lName = null;
    Sym lSym;
    boolean lFound = false;

    //##62 lExpIdCount = incrementSymExpCount();
    while (lFound == false) {
      fExpIdNumber++; //##62
      lName = ("_xId" + Integer.toString(fExpIdNumber, 10)).intern();
      //##78 lSym = symRoot.symTableCurrent.searchLocal(lName, 0, false);
      lSym = symRoot.symTableFlow.searchLocal(lName, 0, false); //##78
      if (lSym == null)
        break;
      if (lSym instanceof ExpId) {
        ((FlowAnalSym)lSym).resetFlowAnalInf();
        ((ExpId)lSym).resetFlowAnalInf();
        break;
      }
      //##62 fExpIdNumber++;
    }
    return lName;
  } // genareteExpIdName

// BEGIN

//---- Methods moved from FlowImpl ----
  public SubpDefinition
    getSubpDefinition()
  {
    return fSubpDefinition;
  }

  public Subp
    getSubpSym()
  {
    return fSubpDefinition.getSubpSym();
  }

//----

  public int
    getNumberOfDefUsedGlobalSymbols()
  {
    return fUsedGlobalSymCount;
  }

// BEGIN

  public int
    getPointVectorBitCount()
  {
    return fPointVectorBitCount;
  }

  public void
    setPointVectorBitCount(int pBitCount)
  {
    fPointVectorBitCount = pBitCount;
    fPointVectorWordCount = (fPointVectorBitCount + 63) / 64;
  }

  public int
    getPointVectorWordCount()
  {
    return fPointVectorWordCount;
  }

  public int
    getExpVectorBitCount()
  {
    return fExpVectorBitCount;
  }

  public void
    setExpVectorBitCount(int pBitCount)
  {
    fExpVectorBitCount = pBitCount;
    fExpVectorWordCount = (fExpVectorBitCount + 63) / 64;
  }

  public int
    getExpVectorWordCount()
  {
    return fExpVectorWordCount;
  }

  public int
    getDefVectorBitCount()
  {
    return fDefVectorBitCount;
  }

  public void
    setDefVectorBitCount(int pBitCount)
  {
    fDefVectorBitCount = pBitCount;
    fDefVectorWordCount = (fDefVectorBitCount + 63) / 64;
  }

  public int
    getDefVectorWordCount()
  {
    return fDefVectorWordCount;
  }

  public int
    getBBlockVectorBitCount()
  {
    return fBBlockVectorBitCount;
  }

  public void
    setBBlockVectorBitCount(int pBitCount)
  {
    fBBlockVectorBitCount = pBitCount;
    fBBlockVectorWordCount = (fBBlockVectorBitCount + 63) / 64;
  }

  public int
    getBBlockVectorWordCount()
  {
    return fBBlockVectorWordCount;
  }

// Additon BEGIN

  public void
    setPrevBBlockInSearch(BBlock pPrev)
  {
    fPrevBBlockInSearch = pPrev;
  }

  public BBlock
    getPrevBBlockInSearch()
  {
    return fPrevBBlockInSearch;
  }

// Addition end

// END

// BEGIN

  public java.util.Set
    getDefinedSyms()
  {
    return fDefinedSyms;
  }

  public java.util.Set
    getUsedSyms()
  {
    //##62 return fUsedSyms;
    return fUsedSymSet; //##62
  }

  /**
   * Compute fDefinedSyms showing symbols used in this subprogram ??
   * and print if dbgLevel >= 4.
   * (fDefinedSyms can be get from fUsedSymSet without recomputing.) ??
   */
  public void
    summarize()
  {
    BBlock lBBlock;
    ExpVector lDefined, lUsed;
    ExpVectorIterator lVectIterator;
    FlowAnalSym lSym;
    Sym lLinkedSym;
    int lNextIndex; //##62, lSymCount;
    //##62 java.util.Set lAccessedSyms;
    //##63 fDefinedSyms = new HashSet();
    //##62 fUsedSyms = new HashSet();
    //##62 lAccessedSyms = new HashSet();
    if (fDbgLevel > 3) {
      ioRoot.dbgFlow.print(4, "summarize",
        "used source symbols " + fUsedSymCount +
        " used symbols " + getSymExpCount()
        + " BBlock count " + fBBlockCount); //##60
      ioRoot.dbgFlow.print(4, " ", "defined");
    }
    lDefined = (ExpVector)(new ExpVectorImpl(this));
    lUsed = (ExpVector)(new ExpVectorImpl(this));
    for (int lBBlockNumber = 1; lBBlockNumber <= fBBlockCount;
         lBBlockNumber++) {
      lBBlock = getBBlock(lBBlockNumber);
      if (lBBlock == null)
        continue;
      if (lBBlock.getDefined() != null)
        lBBlock.getDefined().vectorOr(lDefined, lDefined);
      if (lBBlock.getUsed() != null)
        lBBlock.getUsed().vectorOr(lUsed, lUsed);
    }
    for (lVectIterator = flowRoot.fSubpFlow.expVectorIterator(lDefined);
         lVectIterator.hasNext(); ) {
      lNextIndex = lVectIterator.nextIndex();
      if (lNextIndex > 0) {
        //##62 lSym = flowRoot.dataFlow.getFlowAnalSym(
        //##62   flowRoot.dataFlow.expReverseLookup(lNextIndex));
        lSym = getIndexedSym(lNextIndex); //##62
        if (lSym != null) {
          fDefinedSyms.add(lSym); // ??
          if (fDbgLevel > 3)
            ioRoot.dbgFlow.print(4, " " + lSym.getName());
          lLinkedSym = null;
          if (lSym instanceof ExpId)
            lLinkedSym = ((ExpId)lSym).getLinkedSym();
          if ((lLinkedSym != null) && (lLinkedSym instanceof FlowAnalSym)) {
            fDefinedSyms.add(lLinkedSym); // ??
            if (fDbgLevel > 3)
              ioRoot.dbgFlow.print(4, "(" + lLinkedSym.getName() + ")");
          }
        }
      }
    }
    if (fDbgLevel > 3)
      ioRoot.dbgFlow.print(4, " ", "used");
    for (lVectIterator = flowRoot.fSubpFlow.expVectorIterator(lUsed);
         lVectIterator.hasNext(); ) {
      lNextIndex = lVectIterator.nextIndex();
      if (lNextIndex > 0) {
        //##62 lSym = flowRoot.dataFlow.getFlowAnalSym(
        //##62   flowRoot.dataFlow.expReverseLookup(lNextIndex));
        lSym = getIndexedSym(lNextIndex); //##62
        if (lSym != null) {
          //##62 fUsedSyms.add(lSym);
          if (fDbgLevel > 3)
            ioRoot.dbgFlow.print(4, " " + lSym.getName());
          lLinkedSym = null;
          if (lSym instanceof ExpId)
            lLinkedSym = ((ExpId)lSym).getLinkedSym();
          if ((lLinkedSym != null) && (lLinkedSym instanceof FlowAnalSym)) {
            fDefinedSyms.add(lLinkedSym);
            if (fDbgLevel > 3)
              ioRoot.dbgFlow.print(4, "(" + lLinkedSym.getName() + ")");
          }
        }
      }
    }
  } // summarize

// END

//===== Iterators ======

  /** cfgIterator
   *  Traverse basic blocks in CFG (control flow graph)
   *  in depth first order.
   *  Available methods:
   *  next(), hasNext().
   **/
  public Iterator
    cfgIterator()
  {
    flow.dbg(3, "get", "cfgIterator");
    if (flow.getFlowAnalStateLevel() >= Flow.STATE_CFG_AVAILABLE) {
      if (fDfoList == null) {
        // Make depth first order list of BBlocks.
        if (fDbgLevel > 0)
          flow.dbg(4, " Make DfoList");
        fDfoList = new LinkedList();
        for (BBlock lBBlock = fEntryBBlock; lBBlock != null;
             lBBlock = lBBlock.getNextInDFO()) {
          fDfoList.add(lBBlock);
          if (fDbgLevel > 0)
            flow.dbg(4, " B" + lBBlock.getBBlockNumber());
        }
      }
    }
    else {
      ioRoot.msgRecovered.put(5011, "Incomplete flow information (cfgIterator)");
    }
    return fDfoList.iterator();
  } // cfgIterator

  public Iterator
    cfgFromExitIterator()
  {
    if (fDbgLevel > 0)
      flow.dbg(3, "get", "cfgFromExitIterator");
    if (flow.getFlowAnalStateLevel() >= Flow.STATE_CFG_AVAILABLE) {
      if (fInverseDfoList == null) {
        // Make inverse depth first order list of BBlocks.
        if (fDbgLevel > 0)
          flow.dbg(4, " Make InverseDfoList");
        fInverseDfoList = new LinkedList();
        for (BBlock lBBlock = fExitBBlock; lBBlock != null;
             lBBlock = lBBlock.getNextInInverseDFO()) {
          fInverseDfoList.add(lBBlock);
          if (fDbgLevel > 0)
            flow.dbg(4, " B" + lBBlock.getBBlockNumber());
        }
      }
    }
    else {
      ioRoot.msgRecovered.put(5012,
        "Incomplete flow information (cfgFromExitIterator)");
    }
    return fInverseDfoList.iterator();
  } // cfgFromExitIterator

  public PointVectorIterator
    pointVectorIterator(PointVector pPointVector)
  {
    return (PointVectorIterator)(new PointVectorIteratorImpl(this, pPointVector)); //##60
  } // PointVectorIterator

  public DefVectorIterator
    defVectorIterator(DefVector pDefVector)
  {
    return (DefVectorIterator)(new DefVectorIteratorImpl(this, pDefVector));
   } // defVectorIterator

  public ExpVectorIterator
    expVectorIterator(ExpVector pExpVector)
  {
    return (ExpVectorIterator)(new ExpVectorIteratorImpl(this, pExpVector)); //##60
  } // expVectorIterator

  public BBlockSubtreeIterator
    bblockSubtreeIterator(BBlock pBBlock)
  {
    return (BBlockSubtreeIterator)
      (new BBlockHirSubtreeIteratorImpl(flowRoot, pBBlock)); //##62
  }

  public BBlockStmtIterator
    bblockStmtIterator(BBlockHir pBBlock)//##63
  {
    return new BBlockStmtIterator(pBBlock); //##63
  }

  /** bblockNodeItrator
   *  Get iterator that traverse all nodes of
   *  the basic block pBBlock.
   **/
  public BBlockNodeIterator
    bblockNodeIterator(BBlock pBBlock)
  {
    return (BBlockNodeIterator)
      (new BBlockHirNodeIteratorImpl(flowRoot, pBBlock));
  }

//===== Flow analysis status ======

  public int
    getFlowAnalStateLevel()
  {
    return flowRoot.flow.getFlowAnalStateLevel();
  }

  public void
    setFlowAnalStateLevel(int pState)
  {
    flowRoot.flow.setFlowAnalStateLevel(pState);
  }

  public ArrayList
    getBBlockTable()
  {
    return fBBlockTable;
  }

//##63 BEGIN

  /**
   * Returns the List of BBlocks in the flow
   * excluding null and 0-numbered BBlock.
   * (Moved from DataFlowImpl.) //##63
   * @return the List of BBlocks in the flow.
   **/
  public List getBBlockList()
  {
    if (fBBlockList == null) {
      fBBlockList = new ArrayList(getBBlockTable().size());
      for (Iterator lIt = getBBlockTable().iterator();
           lIt.hasNext(); ) {
        BBlock lBBlock = (BBlock)lIt.next();
        if (lBBlock != null) {
          if (lBBlock.getBBlockNumber() > 0)
            fBBlockList.add(lBBlock);
        }
      }
      if (fDbgLevel > 3)
        flowRoot.ioRoot.dbgFlow.print(5, "getBBlockList", fBBlockList.toString()); //##65
    }
    return fBBlockList;
  } // getBBlockList

  public List
  getReachableBBlocks()
{
  return getListOfBBlocksFromEntry();
} // getReachableBBLocks

//##63 END

//##60 BEGIN

  public void
    initiateControlFlowAnal(SubpDefinition pSubpDefinition,
    int pIndexMin, int pIndexMax)
  {
    for (int lIndex = 0; lIndex < DF_MAX; lIndex++) //##60
      fComputedFlag[lIndex] = 0; //##60
    fIrIndexMin = pSubpDefinition.getNodeIndexMin();
    fIrIndexMax = pSubpDefinition.getNodeIndexMax();
    if ((fIrIndexMin != pIndexMin) || (fIrIndexMax != pIndexMax) ||
        (fIrIndexMax < fIrIndexMin)) {
      ioRoot.msgRecovered.put(5555, "IndexMin/Max does not match. ");
      resetComputedFlag(CF_INDEXED);
      pSubpDefinition.setIndexNumberToAllNodes(fIrIndexMin, true);
      setComputedFlag(CF_INDEXED);
      fIrIndexMax = pSubpDefinition.getNodeIndexMax();
    }
    setComputedFlag(CF_INDEXED); //##60
    clearControlFlow(); //##62
    if (fDbgLevel > 0)
      flowRoot.ioRoot.dbgFlow.print(2, "initiateControlFlowAnal",
        pSubpDefinition.getSubpSym().getName() + " index "
        + fIrIndexMin + "-" + fIrIndexMax);
  } // initiateControlFlowAnal with index min/max

  public void
    initiateDataFlowAnal(SubpDefinition pSubpDefinition)
  {
    fIrIndexMin = pSubpDefinition.getNodeIndexMin();
    fIrIndexMax = pSubpDefinition.getNodeIndexMax();
    if (fComputedFlag == null) {
      if (fDbgLevel > 0)
        ioRoot.dbgFlow.print(1, "SubpFlowImpl.initiateControlFlowAnal",
          "requires control flow analysis "
          + pSubpDefinition.getSubpSym().getName());
      flow.controlFlowAnal(flowRoot.fSubpFlow);
      fIrIndexMin = pSubpDefinition.getNodeIndexMin();
      fIrIndexMax = pSubpDefinition.getNodeIndexMax();
    }
    if (fDbgLevel > 0)
      flowRoot.ioRoot.dbgFlow.print(2, "SubpFlowImpl.initiateDataFlowAnal end",
        pSubpDefinition.getSubpSym().getName() + " index " + fIrIndexMin
        + "-" + fIrIndexMax + " used source symbols " + fUsedSymCount);
    //-- Set minimum number for bit vector parameters as initial value.
    setPointVectorBitCount(63); //##fnami
    setExpVectorBitCount(63); //##fnami

    clearDataFlow(); //##62
  } // initiateDataFlowAnal with index min/max

  public BBlock
    getBBlock(HIR pHir)
  {
    return fBBlockOfIR[pHir.getIndex() - fIrIndexMin];
  }

  public void
    setBBlock(HIR pHir, BBlock pBBlock)
  {
    fBBlockOfIR[pHir.getIndex() - fIrIndexMin] = pBBlock;
  }

  public BBlock
    getBBlock0(Label pLabel)
  {
    return (BBlock)fBBlockOfLabel.get(pLabel);
  }

  public BBlock
    getBBlockForLabel(Label pLabel)
  {
    return (BBlock)fBBlockOfLabel.get(pLabel);
  }

  public void
    setBBlock(Label pLabel, BBlock pBBlock)
  {
    fBBlockOfLabel.put(pLabel, pBBlock);
  }

  public HIR
    getLinkedSubtreeOfExpId(ExpId pExpId)
  {
    if (pExpId == null)
      return null;
    ExpInf lExpInf = pExpId.getExpInf();
    return (HIR)lExpInf.fLinkedIR;
    //##62 END
  }

//##63 BEGIN
  public DefUseList
    getDefUseList()
  {
    if (fDefUseList == null) {
      fDefUseList = new DefUseListImpl(flowRoot);
    }
    //##70 BEGIN
    if (! isComputed(DF_DEFUSELIST)) {
      if (flowRoot.dataFlow != null)
        flowRoot.dataFlow.findDefUse();
    }
    //##70 END
    return fDefUseList;
  } // getDefUseList

//##73 BEGIN
  public DefUseList
    getListOfDefUseList()
  {
    if (fDefUseList == null) {
      fDefUseList = new DefUseListImpl(flowRoot);
    }
    return fDefUseList;
  } // getListOfDefUseList

  public DefUseList
    getDefUseExhaustiveList()
  {
    if (fDefUseExhaustiveList == null) {
      fDefUseExhaustiveList = new DefUseListImpl(flowRoot);
    }
    if (! isComputed(DF_DEFUSELIST)) {
      if (flowRoot.dataFlow != null)
        flowRoot.dataFlow.findDefUseExhaustively();
    }
    return fDefUseExhaustiveList;
  } // getDefUseExhaustiveList

  public DefUseList
    getListOfDefUseExhaustiveList()
  {
    if (fDefUseExhaustiveList == null) {
      fDefUseExhaustiveList = new DefUseListImpl(flowRoot);
    }
    return fDefUseExhaustiveList;
  } // getListOfDefUseExhaustiveList

//##73 END

  /* //##63
  public void
    allocateDefUseListOfSym(int pSize)
  {
    fDefUseListOfSym = new ArrayList[pSize];
  }
*/ //##63

  public List
    getDefNodeList(FlowAnalSym pSym)
  {
    int lIndex = pSym.getIndex();
    if (fDefNodeListOfSym == null)
      fDefNodeListOfSym = new List[getSymExpCount()+1];
    if (fDefNodeListOfSym[lIndex] == null)
      fDefNodeListOfSym[lIndex] = new ArrayList();
    return fDefNodeListOfSym[lIndex];
  } // getDefNodeList

  public void
    addDefNode(FlowAnalSym pSym, IR pDefNode)
  {
    int lIndex = pSym.getIndex();
    if (fDefNodeListOfSym == null)
      fDefNodeListOfSym = new List[getSymExpCount()+1];
    if (fDefNodeListOfSym[lIndex] == null)
      fDefNodeListOfSym[lIndex] = new ArrayList();
    fDefNodeListOfSym[lIndex].add(pDefNode);
    if (fDbgLevel > 3)
      ioRoot.dbgFlow.print(6, "addDefNodeList " + pSym + " " + pSym.getIndex()
        + pDefNode);
  } // addDefNodeList

/* //##63
  public void
    allocateUseDefListsOfSym(int pSize)
  {
    fUseDefListOfSym = new ArrayList[pSize];
  }
*/ //##63

public UseDefList
  getUseDefList()
{
  if (fUseDefList == null) {
    fUseDefList = new UseDefListImpl(flowRoot);
  }
  //##70 BEGIN
  if (! isComputed(DF_USEDEFLIST)) {
    if (flowRoot.dataFlow != null)
      flowRoot.dataFlow.findUseDef();
  }
  //##70 END
  return fUseDefList;
} // getUseDefList

//##73 BEGIN
public UseDefList
  getListOfUseDefList()
{
  if (fUseDefList == null) {
    fUseDefList = new UseDefListImpl(flowRoot);
  }
  return fUseDefList;
} // getListOfUseDefList

public UseDefList
  getUseDefExhaustiveList()
{
  if (fUseDefExhaustiveList == null) {
    fUseDefExhaustiveList = new UseDefListImpl(flowRoot);
  }
  //##70 BEGIN
  if (! isComputed(DF_USEDEFLIST)) {
    if (flowRoot.dataFlow != null)
      flowRoot.dataFlow.findUseDefExhaustively();
  }
  return fUseDefExhaustiveList;
} // getUseDefExhaustiveList

public UseDefList
  getListOfUseDefExhaustiveList()
{
  if (fUseDefExhaustiveList == null) {
    fUseDefExhaustiveList = new UseDefListImpl(flowRoot);
  }
  return fUseDefExhaustiveList;
} // getListOfUseDefExhaustiveList

//##73 END

  public List
    geUseNodeList(FlowAnalSym pSym)
  {
    int lIndex = pSym.getIndex();
    if (fUseNodeListOfSym == null)
      fUseNodeListOfSym = new List[getSymExpCount()+1];
    if (fUseNodeListOfSym[lIndex] == null)
      fUseNodeListOfSym[lIndex] = new ArrayList();
    return fUseNodeListOfSym[lIndex];
  } // getUseNodeList

  public void
    addUseNode(FlowAnalSym pSym, IR pUseNode)
  {
    int lIndex = pSym.getIndex();
    if (fUseNodeListOfSym == null)
      fUseNodeListOfSym = new List[getSymExpCount()+1];
    if (fUseNodeListOfSym[lIndex] == null)
      fUseNodeListOfSym[lIndex] = new ArrayList();
    fUseNodeListOfSym[lIndex].add(pUseNode);
    if (fDbgLevel > 3)
      ioRoot.dbgFlow.print(6, "addUseDefList " + pSym + " " + pSym.getIndex()
        + pUseNode);
  } // addUseDefList
//##63 END

//====== Methods to create objects for data flow analysis ======//
  public BBlockVector bblockVector()
  {
      BBlockVector lBBlockVector = new BBlockVectorImpl(this);
     return lBBlockVector;
  }

  public FlowAnalSymVector flowAnalSymVector()
  {
    return new FlowAnalSymVectorImpl(this);
  }

  public java.util.Set
    setOfGlobalVariables()
  {
    return fGlobalSymsUsed; //##60
  }

  public java.util.Set
    setOfAddressTakenVariables()
  {
    return fSetOfAddressTakenVariables;
  }

  public void
    clearControlFlow()
  {
    if (fDbgLevel > 0)
      flowRoot.ioRoot.dbgFlow.print(2, "\n subpFlow.clearControlFlow() "
        + fSubpDefinition.getSubpSym().getName()
        + " IrIndex " + fIrIndexMin + "-" + fIrIndexMax);
    failed = false; //##78
    resetFlowSymLinkForRecordedSym(); //##60
    //##78 resetFlowSymLink(symRoot.symTableCurrentSubp); //##60
    resetFlowSymLink(symRoot.symTableFlow); //##78
    resetGlobalFlowSymLink(); //##60ZZ
    //##62 flowRoot.subpUnderAnalysis.resetLabelLink(); //##60
    int lNodeCount = fIrIndexMax - fIrIndexMin + 1; //##60
    //##62 fSymIndexTable = new ArrayList(lNodeCount / 2); //##60
    fUsedSymSet = new HashSet(); //##62
    fSymExpCount = 0; //##62
    fUsedSymCount = 0; //##62
    //##60 fExpIdTable = new FlowExpId[fIrIndexMax - fIrIndexMin + 1]; //##25-1
    //##60 fBBlockTable = new FAList(20); //##25
    fBBlockTable = new ArrayList(lNodeCount / 5); //##60
    fBBlockList = null; //##63
    fBBlockCount = 0; //##60
    //##63 fBBlockTable.add(0, null); // Avoid IndexOutOfRange //##60
    //##65 fBBlockOfIR = new BBlock[lNodeCount + 2]; //##60
    // Sizes of tables indexed by node index may exceed original
    // node count because there are some nodes generated by copying
    // during ExpId computation, etc. //##65
    fLabelDefCount = 0;  //##100
    fBBlockOfIR = new BBlock[lNodeCount * 2 + 2]; //##65
    fBBlockOfLabel = new HashMap(); //##60
    fSetOfAddressTakenVariables = null;
    //##62 fSetOfGlobalVariables = null; //##57
    //##60 BEGIN
    fEntryBBlock = null; //##60
    fExitBBlock = null; //##60
    fListOfBBlocksFromEntry = null; //##63
    fListOfBBlocksFromExit  = null; //##63
    fDfoList = null; //##60
    fInverseDfoList = null; //##60
    fGlobalSymsUsed = new HashSet(); //##60
    fSetOfGlobalVariables = new HashSet(); //##62
    fAssignCount = 0; //##62
    fCallCount   = 0; //##62
    //##62 fRestructured = false; //##60
    fComputedFlag = new int[DF_MAX + 1]; //##60
    flow.setFlowAnalStateLevel(flow.STATE_DATA_UNAVAILABLE); //##60
    if (flowRoot.isHirAnalysis()) {
      ((HirSubpFlowImpl)this).fStmtExpSeq = null;
      ((HirSubpFlowImpl)this).fStmtExpSeqIndexForBBlock = null;
    }
    fSubtreesContainingCall = new HashSet(); // Set of subtrees containing call. //##63
    fSetRefReprTable = new SetRefRepr[lNodeCount + 3]; //##65
    fArrayOfSetRefReprList = null; // Indexed by BBLockNumber. //##65
    fExpIdList = new ArrayList(lNodeCount / 2); //##65
    fExpIdNumber = 1; //##65
    fIteratorInitiated = false; //##65
    fDefinedSyms = new HashSet(); //##65
    fDom = null; //##70
    fPostDom = null; //##70
    fDomList = null; //##70
    fPostDomList = null; //##70
  } // clearControlFlow

  public void
    clearDataFlow()
  {
    if (fDbgLevel > 0)
      flowRoot.ioRoot.dbgFlow.print(2, "\n subpFlow.clearDataFlow() "
        + fSubpDefinition.getSubpSym().getName());
    int lNodeCount = fIrIndexMax - fIrIndexMin + 1;
    failed = false; //##78
    //##62 fSetRefReprTable = null; //##25
    if (fSetRefReprTable == null) {
      fSetRefReprTable = new SetRefRepr[lNodeCount + 2]; //##62
    }
    //##62 fLinkedSubtreeOfExpId = new HashMap(); //##60
    //##62 fExpIdTable = new ExpId[lNodeCount]; //##60

    //##65 fExpIdList = new ArrayList(lNodeCount / 2); //##60
    //##65 fExpIdList.add(0, null); // Avoid IndexOutOfRange //##60
    //##65 fExpIdNumber = 1; //##62
    fTempExpCorrespondence = new HashMap(); //##60
    fRecordAlias = null; //##60
    fDefRefCount = 0; //##62
    fDefCount    = 0; //##62
    fDefUseList = null; //##65
    hasCall = null;
    hasStructUnion = null; // Indexed by BBlock number.
    hasPointerAssign = null; // Indexed by BBlock number.
    //##65 fArrayOfSetRefReprList = null; // Indexed by BBLockNumber.
    //##65 fDefinedSyms = new HashSet(); //##63
    //##63 fSubtreesContainingCall = new HashSet(); // Set of subtrees containing call.
    //##60 END
    //##65 BEGIN
    fMaximalCompoundVars = new HashSet();
    if (fBBlockTable != null) {
      if (fDbgLevel > 3)
        flowRoot.ioRoot.dbgFlow.print(4, "\n resetForDataFlowAnal of BBlocks");
      for (int lNumber = 0; lNumber < fBBlockTable.size(); lNumber++) {
        BBlock lBBlock = (BBlock)fBBlockTable.get(lNumber);
        if (lBBlock != null) {
          ((BBlockImpl)lBBlock).resetForDataFlowAnal();
          if (fDbgLevel > 3)
            flowRoot.ioRoot.dbgFlow.print(4, " B" + lNumber);
        }
      }
    }
    if (isComputed(DF_SETREFREPR))
      resetComputedFlag(DF_SETREFREPR + 1);
    else if (isComputed(DF_EXPID))
      resetComputedFlag(DF_EXPID+1);
    else
      //##65 END
      resetComputedFlag(DF_MIN); //##63
    fMultipleSetRef = null; //##70
  } // clearDataFlow

  public void resetControlAndDataFlowInformation()
  {
    // Renumber HIR nodes of the subprogram.
    if (fDbgLevel > 0)
      flowRoot.ioRoot.dbgFlow.print(2, "\n resetControlAndDataFlowInformation ");
    resetComputedFlag(CF_INDEXED);
    fSubpDefinition.setIndexNumberToAllNodes(fSubpDefinition.getNodeIndexMin(), true);
    setComputedFlag(CF_INDEXED);
    clearControlFlow();
    clearDataFlow();
  } // resetControlAndDataFlowInformation

//##65 BEGIN
public void
  resetExpId()
{
  //##78 if (symRoot.symTableCurrent.getOwner() == null)
  if (symRoot.symTableFlow.getOwner() == null) //##78
    return;
  if (fDbgLevel > 0)
    flowRoot.ioRoot.dbgFlow.print(2, " resetExpId of "
      //##78 + symRoot.symTableCurrent.getOwner().getName());
      + symRoot.symTableCurrent.getOwner().getName() + " " //##78
      + symRoot.symTableFlow.toString()); //##78
  //##78 for (SymIterator lIterator = symRoot.symTableCurrent.getSymIterator();
  for (SymIterator lIterator = symRoot.symTableFlow.getSymIterator(); //##78
       lIterator.hasNext(); ) {
    Sym lSym = lIterator.next();
    if (lSym instanceof ExpId) {
      if (fDbgLevel > 3)
        flowRoot.ioRoot.dbgFlow.print(4, " " + lSym.getName());
      ((FlowAnalSym)lSym).resetFlowAnalInf();
    }
  }
} // resetExpId
//##65 END

  /** computeSetOfGlobalVariables
   *  Compute the set of global variables and
   *  record it in fSetOfGlobalVariables.
   * @return the set of global variables.
   */
  public java.util.Set
    computeSetOfGlobalVariables()
  {
  if (fDbgLevel > 0) //##62
   flowRoot.flow.dbg(2, "\nglobalVariables ", //##63
     fSetOfGlobalVariables.toString()); //##57
 return fSetOfGlobalVariables; //##62

  } // computeSetOfGlobalVariables

  /** computeSetOfAddressTakenVariables
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
    if (flowRoot.isHirAnalysis()) {
      HIR lSubpBody = fSubpDefinition.getHirBody();
      computeSetOfAddressTakenVariables(lSubpBody, lAddressTakenVariables, false);
    }
    else {
      // REFINE
    }
    fSetOfAddressTakenVariables = lAddressTakenVariables; //##53
    flowRoot.flow.dbg(2, "\naddressTakenVariables ", //##63
      lAddressTakenVariables.toString());
    return lAddressTakenVariables;
  } // computeSetOfAddressedVariables

  /** computeSetOfAddressTakenVariables
   *  Compute the set of variables whose address is taken
   *   (e.g. (addr (var )), (addr (subs (var ) ... ) )
   *  within the given HIR subtree.
   * @param pHir HIR subtree to be computed.
   * @param pSet Set in which variables are recorded.
   * @param pAddrOperand true if address is to be taken,
   *   false otherwise.
   */
  public void
    computeSetOfAddressTakenVariables(
    HIR pHir,
    java.util.Set pSet,
    boolean pAddrOperand)
  {
    HIR lHir, lHirChild;
    Sym lSym;
    if (pHir != null) {
      if (pHir instanceof VarNode) {
        lSym = pHir.getSymOrExpId();
        if (pAddrOperand && (lSym instanceof Var))
          pSet.add(lSym);
      }
      else {
        switch (pHir.getOperator()) {
          case HIR.OP_ADDR:
            computeSetOfAddressTakenVariables((HIR)pHir.getChild1(), pSet, true);
            break;
          case HIR.OP_SUBS:
            computeSetOfAddressTakenVariables((HIR)pHir.getChild1(), pSet,
              pAddrOperand);
            computeSetOfAddressTakenVariables((HIR)pHir.getChild2(), pSet, false);
            break;
          case HIR.OP_ARROW:
            computeSetOfAddressTakenVariables((HIR)pHir.getChild1(), pSet, false);
            computeSetOfAddressTakenVariables((HIR)pHir.getChild2(), pSet,
              pAddrOperand);
            break;
          //##67 BEGIN
          case HIR.OP_DECAY:
            computeSetOfAddressTakenVariables((HIR)pHir.getChild1(), pSet, true);
            break;
          //##67 END
          //##53 BEGIN
          /* //##67
          case HIR.OP_CONTENTS:
            computeSetOfAddressTakenVariables((HIR)pHir.getChild1(), pSet, true); // REFINE
            break;
          case HIR.OP_ADD:
          case HIR.OP_SUB:
            if (pHir.getType().getTypeKind() == Type.KIND_POINTER) { // Address computation.
              computeSetOfAddressTakenVariables((HIR)pHir.getChild1(), pSet, true); // REFINE
            }
            else {
              computeSetOfAddressTakenVariables((HIR)pHir.getChild1(), pSet, false);
            }
            computeSetOfAddressTakenVariables((HIR)pHir.getChild2(), pSet, false);
            break;
          */ //##67
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
            for (Iterator lIterator = ((HirList)pHir).iterator();
                 lIterator.hasNext(); ) {
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
    if (fDbgLevel >= 2)
      ioRoot.dbgFlow.print(2, " setResturctureFlag ");
    //##62 fRestructured = true;
    setFlowAnalStateLevel(Flow.STATE_CFG_RESTRUCTURING);
    //##65 BEGIN
    for (int i = CF_BBLOCK; i < DF_MAX; i++) {
      fComputedFlag[i] = 0;
    }
    //##65 END
  }

  public boolean
    getRestructureFlag()
  {
    //##62 return fRestructured;
    if (getFlowAnalStateLevel() <= Flow.STATE_CFG_RESTRUCTURING)
      return true;
    else
      return false;
  }

  public void
    setRecordAlias(RecordAlias pRecordAlias)
  {
    fRecordAlias = pRecordAlias;
    if (fDbgLevel > 3)
      ioRoot.dbgFlow.print(4, " setRecordAlias",
        fSubpDefinition.getSubpSym().getName()); //##56
  }

  public RecordAlias
    getRecordAlias()
  {
    //##62 BEGIN
    if (fRecordAlias == null) {
      if (getComplexityLevel() <= 1) { //##62
        fAlias = new AliasAnalHir2(flowRoot.hirRoot); //##62
      }else {
        fAlias = new AliasAnalHir1(flowRoot.hirRoot); //##62
      }
      fRecordAlias = new RecordAlias(fAlias, fSubpDefinition, this); //##62
    }
    //##62 END
    return fRecordAlias;
  }
//##62 BEGIN
  public SetRefRepr
    getSetRefReprOfIR(IR pIR)
  {
    if (pIR instanceof HIR) {
      ExpId lExpId = getExpId((HIR)pIR);
      if (lExpId != null) {
        return lExpId.getExpInf().getSetRefRepr();
      }else {
        if ((pIR instanceof AssignStmt)||
            (pIR instanceof AsmStmt)||          //##70
            (pIR.getOperator() == HIR.OP_CALL)) //##70
            {
          return fSetRefReprTable[pIR.getIndex() - fIrIndexMin];
        }
      }
    }
    return null;
  } // getSetRefReprsOfIR

  public void
    setSetRefReprOfIR(SetRefRepr pSetRefRepr, IR pIR)
  {
    fSetRefReprTable[pIR.getIndex() - fIrIndexMin] = pSetRefRepr;
  } // setSetRefReprOfIR

  //##62 END

  public void
    correlateBBlockAndIR()
  {
    // See HirSubpFlowImpl.
  } // correlateBBlockAndIR

  public void
    allocateBBlockOfIR()
  {
    if (fDbgLevel > 0)
      flowRoot.flow.dbg(2, "allocateBBlockOfIR", //##63
        "size " + fIrIndexMax + "-" + fIrIndexMin + "-1");
    fBBlockOfIR = new BBlock[fIrIndexMax - fIrIndexMin + 2];
  } // allocateBBlockOfIR

  public BBlock
    getBBlockOfIR(int pIndex)
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
    setBBlockOfIR(BBlock pBBlock, int pIndex)
  {
    if (fBBlockOfIR == null)
      allocateBBlockOfIR();
    if (pIndex < fIrIndexMin) { //##52
      flowRoot.ioRoot.msgRecovered.put(5555,
        "setBBlockOfIR with invalid index " + pIndex +
        " fIndexMin " + fIrIndexMin);
    }
    else
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

  public int
    getDefCount()
  {
    return fDefCount; //##62
  }

  public ExpId
    getExpId(IR pIr)
  {
    // if (fDbgLevel > 3)
    //   flowRoot.ioRoot.dbgFlow.print(7, " getExpId " + pIr.toStringShort()); //###
    if (isComputedOrUnderComputation(DF_EXPID)) { //##65
      if ((pIr != null) && (fExpIdTable != null) &&
          (pIr.getIndex() >= fIrIndexMin) &&
           ((pIr.getIndex() <= fIrIndexMax)||
            (pIr.getIndex() <= fMaxIndexOfCopiedNode)) //##68
          //##62 &&(!getRestructureFlag())
          ) { //##25-1
        // if (fDbgLevel > 3)
        //   flowRoot.ioRoot.dbgFlow.print(7, " " + fExpIdTable[pIr.getIndex() - fIrIndexMin]); //###
        return fExpIdTable[pIr.getIndex() - fIrIndexMin];
      }
      else
        return null;
    }else //##65
      return null; //##65
  } // getExpId

//##56 BEGIN
  public ExpId
    getExpId(IR pIr, int pIndex)
  {
    if (pIr != null)
      return fExpIdTable[pIndex - fIrIndexMin];
    else
      return null;
  } // getExpId

//##56 END

  public void
    setExpId(IR pIr, ExpId pExpId)
  {
    if ((pIr != null)&&(pExpId != null)) {
      int lIndex = pIr.getIndex();
      if ((fExpIdTable != null) &&
          (lIndex >= fIrIndexMin))
        fExpIdTable[pIr.getIndex() - fIrIndexMin] = pExpId;
      //##62 BEGIN
      // Record ExpId to BBlock (moved from SetRefReprHirImpl).
      if (fBBlockOfIR != null) {
        //##62 BBlock lBBlock = fBBlockOfIR[lIndex];
        BBlock lBBlock = fBBlockOfIR[lIndex - fIrIndexMin]; //##62
        if (lBBlock instanceof BBlockHirImpl) {
          ((BBlockHirImpl)lBBlock).addToExpNodeList(pExpId, (HIR)pIr);
        }
      }
      //##62 END
    }
  } // setExpId

  /**
   * Get IR node from ref-index
   * @param pIndex ref index (fSubpFlow.fRefIndex[nodeIndex])
   * @return the corresponding node.
   */
  public IR
    getRefPoint(int pIndex)
  {
    return fDefRefPoint[pIndex]; //##62
  }

  /**
   * Get IR node from def-index
   * @param pIndex def index (fSubpFlow.fDefIndex[nodeIndex])
   * @return the corresponding node.
   */
  public IR
    getDefPoint(int pIndex)
  {
    return fDefPoint[pIndex]; //##62
  }

  public int
    getDefIndex( int pNodeIndex )  //##62
  {
    return fDefIndex[pNodeIndex];
  }


  public int
    recordDefRefPoint( IR pIR )
  {
    fDefRefCount++;
    fDefRefIndex[pIR.getIndex() - fIrIndexMin] = fDefRefCount;
    fDefRefPoint[fDefRefCount] = pIR;
    return fDefRefCount;
  }

  public int
    recordDefPoint( IR pIR )
  {
    fDefCount++;
    fDefIndex[pIR.getIndex() - fIrIndexMin] = fDefCount;
    fDefPoint[fDefCount] = pIR;
    return fDefCount;
  }

  public int
    getAssignCount()
  {
    return fAssignCount;
  }

  public int
    getCallCount()
  {
    return fCallCount;
  }

//##63 BEGIN
  public void
    computeBBlockSetRefReprs()
  {
    if (fDbgLevel > 0)
      flowRoot.flow.dbg(4, "\ncomputeBBlockSetRefReprs");
    if (flowRoot.isHirAnalysis()) {
      ((HirSubpFlowImpl)this).recordSetRefReprs();
    }
  }
//##63 END

  public SetRefReprList
    getSetRefReprList(BBlock pBBlock)
  {
    if (fDbgLevel > 3)
      flowRoot.ioRoot.dbgFlow.print(5, "getSetRefReprList", "B"+pBBlock.getBBlockNumber());
    if (fArrayOfSetRefReprList == null) { //##65
      //##65 ((HirSubpFlowImpl)this).computeBBlockSetRefReprs(this); //##63
      computeBBlockSetRefReprs(); //##65
    } //##65
    SetRefReprList lSetRefReprList = fArrayOfSetRefReprList[pBBlock.getBBlockNumber()];
    if (fDbgLevel > 3)
      if (lSetRefReprList != null)
        flowRoot.ioRoot.dbgFlow.print(6, "lSetRefReprList", lSetRefReprList.toString()); //##63
    return lSetRefReprList;
  }

  public void
    setSetRefReprList(BBlock pBBlock, SetRefReprList pSetRefReprList)
  {
    int lBBlockNumber;
    if (pBBlock == null)
      lBBlockNumber = 0;
    else
      lBBlockNumber = pBBlock.getBBlockNumber();
    fArrayOfSetRefReprList[lBBlockNumber] = pSetRefReprList;
  }

  public Set
    subtreesContainingCall()
  {
    return fSubtreesContainingCall;
  }

  public void
    setExpOfTemp(Var pTempVar, Exp pExp) // Copied from aflow.SubpFlowImpl
  {
    fTempExpCorrespondence.put(pTempVar, pExp);
  }

  public Exp
    getExpOfTemp(Var pTempVar) // Copied from aflow.SubpFlowImpl
  {
    if (fTempExpCorrespondence == null)
      return null;
    if (fTempExpCorrespondence.containsKey(pTempVar))
      return (Exp)fTempExpCorrespondence.get(pTempVar);
    else
      return null;
  }

  public List
    getListOfBBlocksFromEntry()
  {
    if (fListOfBBlocksFromEntry == null) {
      fListOfBBlocksFromEntry = new ArrayList(40);
      BBlock lBBlock;
      for (Iterator lIterator = cfgIterator();
           lIterator.hasNext(); ) {
        lBBlock = (BBlock)lIterator.next();
        if (lBBlock != null)
          fListOfBBlocksFromEntry.add(lBBlock);
      }
    }
    return fListOfBBlocksFromEntry;
  } // getListOfBBlocksFromEntry

  public List
    getListOfBBlocksFromExit()
  {
    if (fListOfBBlocksFromExit == null) {
      fListOfBBlocksFromExit = new ArrayList(40);
      BBlock lBBlock;
      for (Iterator lIterator = cfgFromExitIterator();
           lIterator.hasNext(); ) {
        lBBlock = (BBlock)lIterator.next();
        if (lBBlock != null)
          fListOfBBlocksFromExit.add(lBBlock);
      }
    }
    return fListOfBBlocksFromExit;
  } // getListOfBBlocksFromExit

  public FlowRoot
    getFlowRoot()
  {
    return flowRoot;
  }

  public boolean
    isComputed(int pItemIndex)
  {
    if (fComputedFlag[pItemIndex] <= 1)
      return false;
    else
      return true;
  }

  public void
    setComputedFlag(int pItemIndex)
  {
    fComputedFlag[pItemIndex] = pItemIndex;
    if (fDbgLevel > 0)
      flowRoot.flow.dbg(5, " setComputedFlag "+ pItemIndex); //##65

  }

//##62 BEGIN
  public void
    resetComputedFlag(int pItemIndex)
  {
    //##65 BEGIN
    if ((pItemIndex <= DF_EXPID)&&isComputed(DF_EXPID)) {
      resetExpId();
    }
    //##65 END
    for (int lIndex = pItemIndex; lIndex < DF_MAX; lIndex++) {
      fComputedFlag[lIndex] = 0;
    }
    if (fDbgLevel >= 2)
      flow.dbg(2, "resetComputedFlag",
               "reset all flags greater or equal " + pItemIndex);
  }
//##62 END

  public void
    setUnderComputation(int pItemIndex)
  {
    fComputedFlag[pItemIndex] = 1;
    if (fDbgLevel > 0)
      flowRoot.flow.dbg(5, " setUnderComputation "+ pItemIndex); //##65
  }

  public boolean
    isComputedOrUnderComputation(int pItemIndex)
  {
    if (fComputedFlag[pItemIndex] == 0)
      return false;
    else
      return true;
  }

void printComputedFlag()
  {
    System.out.print("\n fComputedFlag ");
    for (int i = 1; i < DF_MAX; i++)
      System.out.print(" " + fComputedFlag[i]);
  }

  public List getExpIdList()
  {
    return fExpIdList;
  }

  public void
    printExpIdAndIrCorrespondence()
  {
    System.out.print("\nExpId and IR correspondence\n");
    for (Iterator lIterator = fExpIdList.iterator();
         lIterator.hasNext(); ) {
      ExpId lExpId = (ExpId)lIterator.next();
      if (lExpId != null) {
        HIR lHir = (HIR)getLinkedSubtreeOfExpId(lExpId);
        if (lHir != null) {
          System.out.print(" " + lExpId.getName() +
            "  " + lExpId.getIndex()  //##78
            + " " + lHir.toStringWithChildren());
          if (FlowUtil.isAssignLHS(lHir)) //##65
            System.out.print(" LHS\n"); //##65
          else
            System.out.print("\n"); //##65
        }
        else
          System.out.print(" " + lExpId.getName() + " null \n");
    }else
      System.out.print(" null \n");
    }
  } // printExpIdAndIrCorrespondence

  public int
    getComplexityLevel() //##62
  {
    return fComplexity;
  }

//##60 END

//##63 BEGIN
/**
 * Returns true if the given IR node has as its descendant a call node.
 */
public boolean hasCallUnder(IR pIR)
{
  if (pIR instanceof HIR) {
    if (fSubtreesContainingCall.contains(pIR))
      return true;
  }
  return false;
} // hasCallUnder


public FlowAdapter
  //##63 getFlowAdapter( FlowRoot pFlowRoot )
  getFlowAdapter()
{
  return fFlowAdapter;
}

public List
  changeListOfFlowBBlocksToListOfAflowBBlocks( List pListOfFlowBBlocks )
{
  if (pListOfFlowBBlocks == null)
    return null;
  List lListOfAflowBBlocks = new ArrayList(pListOfFlowBBlocks.size());
  for (Iterator lIt = pListOfFlowBBlocks.iterator();
       lIt.hasNext(); ) {
    Object lObject = lIt.next();
    if (lObject instanceof coins.flow.BBlock) {
      lListOfAflowBBlocks.add(((coins.flow.BBlock)lObject).getAflowBBlock());
    }
  }
  return lListOfAflowBBlocks;
} // changeListOfFlowBBlocksToListOfAflowBBlocks
//##63 END

//##65 BEGIN
public Set
  getMaximalCompoundVars()
{
  if (fMaximalCompoundVars == null)
    fMaximalCompoundVars = new HashSet();
  return fMaximalCompoundVars;
}

public FlowAnalSym[]
  getFlowAnalSymTable()
{
  return fFlowAnalSymTable;
}

//##65 END

//##70 BEGIN
public BBlockVector
  getDominators( BBlock pBBlock )
{
  if (fDom == null) {
    fDom = ((ControlFlowImpl)flowRoot.controlFlow).fDom;
  }
  return fDom[pBBlock.getBBlockNumber()];
} // getDominator

public BBlockVector
  getPostDominators( BBlock pBBlock )
{
  if (fPostDom == null) {
    fPostDom = ((ControlFlowImpl)flowRoot.controlFlow).fPostDom;
  }
  return fPostDom[pBBlock.getBBlockNumber()];
} // getPostDominator

public List
  getDominatorList( BBlock pBBlock )
{
  if (fDomList == null) {
    fDomList = new ArrayList[getNumberOfBBlocks() + 1];
    for (Iterator lIterator = cfgIterator();
         lIterator.hasNext(); ) {
      BBlock lBBlock = (BBlock)lIterator.next();
      if (lBBlock != null) {
        fDomList[lBBlock.getBBlockNumber()]
          = getDominators(lBBlock).getBBlockList(); //##70
      }
    }
  }
  return fDomList[pBBlock.getBBlockNumber()];
} // getDominatorList

public List
  getPostDominatorList( BBlock pBBlock )
{
  if (fPostDomList == null) {
    fPostDomList = new ArrayList[getNumberOfBBlocks() + 1];
    for (Iterator lIterator = cfgIterator();
         lIterator.hasNext(); ) {
      BBlock lBBlock = (BBlock)lIterator.next();
      if (lBBlock != null) {
        fPostDomList[lBBlock.getBBlockNumber()]
          = getPostDominators(lBBlock).getBBlockList(); //##70
      }
    }
  }
  //##92 return fDomList[pBBlock.getBBlockNumber()];
  return fPostDomList[pBBlock.getBBlockNumber()]; //##92
} // getPostDominatorList

//##70 END

//##73 BEGIN
public coins.aflow.SubpFlow
getAflowSubpFlow(coins.aflow.FlowResults pFlowResults)
{
  if (fAflowSubpFlow == null) {
    fAflowSubpFlow = new coins.aflow.HirSubpFlowImpl(
        fSubpDefinition, pFlowResults);
  }
  return fAflowSubpFlow;
}
//##73 END

//##78 BEGIN
/**
 * Sort the list or set of FLowAnalSyms (pExpIdCollection)
 * so that non-ExpId symbols come first and then
 * ExpIds sorted in the ascending order
 * of suffix (nn of of the name of ExpId _xIdnn).
 * @param pExpIdCollection list or set of FlowAnalSyms.
 * @return the sorted list of FlowAnalSyms.
 */
public List
sortExpIdCollection( Collection pExpIdCollection )
{
  if ((pExpIdCollection == null)||pExpIdCollection.isEmpty()) {
    return new ArrayList();
  }
  ExpId lExpIdArray[] = new ExpId[fExpIdNumber+1];
  Object lSym;
  ExpId lExpId;
  String lTail;
  int lSuffix;
  ArrayList lSortedList = new ArrayList(pExpIdCollection.size());
  for (Iterator lIt = pExpIdCollection.iterator();
       lIt.hasNext(); ) {
    lSym = lIt.next();
    if (lSym instanceof ExpId) {
      lExpId = (ExpId)lSym;
      lTail = lExpId.getName().substring(4);
      lSuffix = Integer.parseInt(lTail, 10);
      lExpIdArray[lSuffix] = lExpId;
    }else {
      lSortedList.add(lSym);
    }
  }
  for (int i = 0; i < fExpIdNumber+1; i++) {
    if (lExpIdArray[i] != null)
      lSortedList.add(lExpIdArray[i]);
  }
  if (fDbgLevel > 4)
    ioRoot.dbgFlow.print(7, "sortExpIdCollection", lSortedList.toString());
  return lSortedList;
} // sortExpIdList

public boolean
isFailed()
{
  return failed;
}

//##78 END

} // SubpFlowImpl class
