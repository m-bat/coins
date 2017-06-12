/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.flow;

import java.util.ArrayList;
import java.util.Collection; //##78
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import coins.FlowRoot; //##60
import coins.alias.RecordAlias; //##60
import coins.ir.IR;
import coins.ir.hir.Exp; //##60
import coins.ir.hir.HIR; //##60
import coins.ir.hir.LabeledStmt; //##60
import coins.ir.hir.SubpDefinition;
//##60 import coins.ir.lir.DefLabel;
//##60 import coins.ir.lir.Prologue;
import coins.sym.FlowAnalSym;
import coins.sym.ExpId; //##60
import coins.sym.Label; //##60
import coins.sym.Subp;
import coins.sym.SymTable;
import coins.sym.Var; //##60

//    (): Jul. 2001.

/** SubpFlow interface
 *  Interface for subprogram flow analysis.
 **/
public interface
  SubpFlow
{

//====== Methods used in dividing program into basic blocks ======

  /**
   * Get the entry basic block.
   * The entry BBlock is started by a statement having entry label
   * (sually has block number 1.
   * @return the entry BBlock
   */
  public BBlock
    getEntryBBlock();

  public void
    setEntryBBlock(BBlock pBlock);

  public BBlock
    getExitBBlock();

  public void
    setExitBBlock(BBlock pBlock);
/* //##62
  public BBlock
    getCurrentBBlock();

  public void
    setCurrentBBlock(BBlock pBlock);
*/ //##62
  public void // Copy flow analysis data
    copyFlowAnalData(SubpFlow pSubpFlow); // of HirSubpFlow to LirSubpFlow.

//====== Methods to create basic blocks ======

  /** Create new basic block corresponding to the HIR labeled statement
   *  pLabeledStmt.
   **/
  public BBlock
    bblock(LabeledStmt pLabeledStmt);

  /** Create dummy basic block. (see linkHirWithFlowForSubp)
   **/
  public BBlock
    bblock();

//====== Methods to create objects for data flow analysis ======

  public ExpVector
    expVector();

  public PointVector
    pointVector();

  public DefVector
    defVector();

  public FlowAnalSymVector //##60
    flowAnalSymVector(); //##60

//====== Methods to correlate flow information with IR ======

  /** linkLirWithDataFlowForSubp
   *  Reset links between flow information and LIR nodes, symbols, etc.
   *  Set index to each LIR node in the subprogram under analysis.
   *  Assign index number to symbols actually used in the current
   *  subprogram.
   *  Create basic blocks and make link between BBlock and LIR node
   *  and create FlowIrLink table showing the link to LIR node.
   **/
//## public void
//## linkLirWithDataFlowForSubp();

//====== Methods to correlate with index number ======

  public void
    resetFlowSymLinkForRecordedSym();

  /** resetFlowSymLink
   *  Reset all flow analysis information of symbols
   *  in pSymTable and its descendents.
   *  Symbols to be reset are those ones that are recorded
   *  as accessed symbols (getAccessedSyms()) in a subprogram,
   *  and so, this method is effective to reset the effect of previous
   *  flow analysis for a subprogram. For global symbols, use
   *  resetGlobalFlowSymLink().
   **/
  public void
    resetFlowSymLink(SymTable pSymTable);

  /** resetGlobalFlowSymLink
   *  Reset all flow analysis information of symbols
   *  that are contained in ancestor symbol table (symTableRoot, etc)
   *  without traversing its descendants.
   **/
  public void
    resetGlobalFlowSymLink();

  /** getBBlock
   *  Get the basic block whose block number is pBlockNumber.
   *  @param pBlockNumber block number of the basic block to be got.
   *  @return basic block that has pBlockNumber as its block number.
   *    If the BBlock having pBlocknumber is deleted (has IS_DELETED
   *    flag) then return null.
   **/
  public BBlock
    getBBlock(int pBlockNumber);

  public void
    recordBBlock(BBlock pBlock, int pBlockNumber);

  public FlowAnalSym
    getIndexedSym(int pSymIndex);

  //##62 public ArrayList
  //##62   getSymIndexTable(); //##60

  //##62 public void
  //##62  recordSym(FlowAnalSym pSym, int pIndex);

  public IR
    getIndexedNode(int pNodeIndex);

  public BBlock
    getBBlockFromNodeIndex(int pNodeIndex);

  public int
    getNumberOfBBlocks();

  public int             //##100
    getLabelDefCount();  //##100
  
  public int getNumberOfNodes();

  public void setNumberOfNodes(int pCount);

  public int getSymExpCount();

  //##62 public void setSymExpCount(int pCount);

  public int getUsedSymCount();

  //##62 public void setUsedSymCount(int pCount);

  //##62 public void setExpIdBase(int pExpIdBase);

  //##62 public int incrementSymExpCount();

  //##62 public int getExpIdNumber();

  /** generateExpIdName
   *  Generate a name of expression identifier _xId1, _xId2, _xId3, ...
   *  using SymExpCount as trailing index.
   *  If a symbol having the name is not found in symTableCurrent,
   *  then it is returned, else if the name is ExpId,
   *  then it is reused after calling resetFlowAnalInf().
   *  If the symbol other than ExpId is found, then SymExpCount is incremented
   *  and new name is generated.
   *  The SymExpCount is incremented when a new name is generated.
   *  @return the name with SymExpCount as the trailing index.
   **/
  public String
    generateExpIdName();

  public void initiateControlFlowAnal(SubpDefinition pSubpDefinition,
    int pIndexMin, int pIndexMax); //##60

  /** initiateDataFlowAnal
   *  Initiate bit vectors, and
   *  initiate fields in SetRefRepls, etc.
   *  @param pSubpDefinition definition of subprogram to be analyzed.
   **/
  public void
    initiateDataFlowAnal( SubpDefinition pSubpDefinition );

  public SubpDefinition
    getSubpDefinition();

  public Subp
    getSubpSym();

//-----

  public int
    getNumberOfDefUsedGlobalSymbols();

// BEGIN

  public int
    getPointVectorBitCount();

  public void
    setPointVectorBitCount(int pBitCount);

  public int
    getPointVectorWordCount();

  public int
    getExpVectorBitCount();

  public void
    setExpVectorBitCount(int pBitCount);

  public int
    getExpVectorWordCount();

  public int
    getDefVectorBitCount(); //##60

  public void
    setDefVectorBitCount(int pBitCount); //##60

  public int
    getDefVectorWordCount(); //##60

  public int
    getBBlockVectorBitCount();

  public void
    setBBlockVectorBitCount(int pBitCount);

  public int
    getBBlockVectorWordCount();

//##62 BEGIN
  /**
   * Get IR node from ref-index
   * @param pIndex ref index (fSubpFlow.fDefRefIndex[nodeIndex])
   * @return the corresponding node.
   */
  public IR
    getRefPoint(int pIndex);

  /**
   * Get IR node from def-index.
   * Def-point is AssignStmt node.
   * @param pIndex def index (fSubpFlow.fDefIndex[nodeIndex])
   * @return the corresponding node.
   */
  public IR
    getDefPoint(int pIndex);

  /**
   * Get def-index from node index.
   * @param pNodeIndex IR node index.
   * @return def-index
   */
  public int
    getDefIndex( int pNodeIndex ); //##62

  //##62 public int incrementDefCount(); //##62
  //##62 public int incrementDefRefCount(); //##62
  public int
    recordDefRefPoint( IR pIR ); //##62

  public int
    recordDefPoint( IR pIR ); //##62

  public int
    getAssignCount();

  public int
    getCallCount();

//##62 END

// END

  /** setPointVectorLength
   *  setExpVectorLength
   *  Set bit length of PointVector or ExpVector.
   **/
//##60 public void setPointVectorLength( int pBitLeng );
//##60 public void setExpVectorLength( int pBitLeng );

// Additon BEGIN

  //##62 public int getSetRefReprNo();

  //##62 public void setSetRefReprNo(int pNumber);

  //##62 public int getDefSetRefReprNo();

  //##62 public void setDefSetRefReprNo(int pNumber);

  //##62 public int incrementDefSetRefReprCount();

  //##62 public int incrementSetRefReprCount();

  //##62 public int getSetRefReprCount();

  //##62 public void setSetRefReprCount(int pCount);

  //##62 public int getDefSetRefReprCount();

  //##62 public void setDefSetRefReprCount(int pCount);

  //##62 public List getDefSetRefReprList();

  /** setPrevBBlockInSearch
   *  getPrevBBlockInSearch
   *  Methods used in linking basic blocks in depth first order,
   *  etc.
   *  @param pPrev previous BBlock from which this BBlock is
   *    to be linked.
   **/
  public void setPrevBBlockInSearch(BBlock pPrev);

  public BBlock getPrevBBlockInSearch();

// Addition end

// END of methods moved from Flow.java

  /** attachLoopInf
   *  Make LoopInf for all loops in the current subprogram
   *  and attach them to loop start nodes and Subp symbol.
   **/
  //##78 public void
  //##78   attachLoopInf();

  /** getDefinedSyms
   *  Set of all symbols whose value is set in this subprogram.
   **/
  public java.util.Set
    getDefinedSyms();

  /** getUsed
   *  Set of all symbols whose value is used in this subprogram.
   **/
  public java.util.Set
    getUsedSyms();

  /** summarize
   *  Summarize the data flow analysis for this subprogram,
   *  that is,
   *  compute defined set and used set after creating
   *  these sets which can be accessed by getDefinedSyms &
   *  getUsedSyms.
   **/
  public void
    summarize();

//===== Iterators ======

  /** cfgIterator
   *  Traverse basic blocks in CFG (control flow graph)
   *  in depth first order from the entry BBlock.
   *  Available methods:
   *  next(), hasNext().
   **/
  public Iterator
    cfgIterator();

  /** cfgFromExitIterator
   *  Traverse basic blocks in CFG (control flow graph)
   *  in inverse depth first order from the exit BBlock.
   *  Available methods:
   *  next(), hasNext().
   **/
  public Iterator
    cfgFromExitIterator();

  /** symListIterator   ()
   *  Traverse all symbols accessed in this subprogram definition.
   *  (Traverse all variables and registers appeared in executable
   *   Stmt/LIRTree in this subprogram definition.)
   *  Available methods:
   *  next(), hasNext().
   **/
  //##62 public Iterator
  //##62   symListIterator();

  /** bblockSubtreeItrator
   *  Get iterator that traverse top subtrees of
   *  the basic block pBBlock.
   *  For HIR, all top subtrees are to be traversed.
   *  For LIR, all LIRTrees are to be traversed.
   * IR tree should not be changed after instanciation of
   * BBlockSubtreeIteratorImpl until all invocations of
   *   hasNext() and next() are finished.
   * Traversed top-subtrees are
   *   LabeledStmt, AssignStmt, ExpStmt, ReturnStmt,
   *   IfStmt, LoopStmt, SwitchStmt
   *   Conditional expression in IfStmt and LoopStmt
   *   Case-selection expression in SwitchStmt
   *   Call subtree (irrespective of contained in ExpStmt or Exp)
   **/
  public BBlockSubtreeIterator
    bblockSubtreeIterator(BBlock pBBlock);

  /** bblockStmtIterator get iterator to traverse
   * statements in the basic block pBBlock.
   * IR tree should not be changed after instanciation of
   * BBlockStmtIteratorImpl until all invocations of
   *   hasNext() and next() are finished.
   */
  public BBlockStmtIterator
    bblockStmtIterator(BBlockHir pBBlock); //##63

  /** bblockNodeItrator
   *  Get iterator that traverse all nodes of
   *  the basic block pBBlock
   *  in the order of from top to bottom,
   *  from left to right.
   *  If the basic block contains a list of subtrees,
   *  then the list is traversed from head to tail
   *  traversing each subtree.
   **/
  public BBlockNodeIterator
    bblockNodeIterator(BBlock pBBlock);

  public PointVectorIterator
    pointVectorIterator(PointVector pPointVector);

  public DefVectorIterator
    defVectorIterator(DefVector pDefVector);

  public ExpVectorIterator
    expVectorIterator(ExpVector pExpVector);

//===== Flow analysis status ======

  public int
    getFlowAnalStateLevel();

  public void
    setFlowAnalStateLevel(int pState);

  /**
   * Returns the List of BBlocks in the flow
   * excluding null and 0-numbered BBlock.
   * (Moved from DataFlowImpl.) //##63
   * @return the List of BBlocks in the flow.
   **/
  public List getBBlockList(); //##63

  /**
   * Get BBlockTable of this subprogram.
   * Use getBBlockList() instead of this method.
   * @return BBlockTable
   */
  public ArrayList
    getBBlockTable();

//##73 BEGIN
  /**
   * Get list of BBlocks reachable from entry BBLock.
   * @return teh list of reachable BBlocks.
   */
  public List
  getReachableBBlocks();
//##73 END
//##60 BEGIN
  public BBlock
    getBBlock(HIR pHIR);

  public BBlock
    getBBlockOfIR(int pIndex);

//##60 public BBlock
//##60 getBBlock0( Label pLabel );

  public BBlock
    getBBlockForLabel(Label pLabel);

  public void
    setBBlock(HIR pHir, BBlock pBBlock);

  public void
    setBBlock(Label pLabel, BBlock pBBlock);

  public HIR
    getLinkedSubtreeOfExpId(ExpId pExpId);

//##63 BEGIN

  public DefUseList
    getDefUseList();

  public DefUseList //##73
    getDefUseExhaustiveList(); //##73

  public List
    getDefNodeList(FlowAnalSym pSym); //##63

  //##60 public void
  //##60   setDefUseList(FlowAnalSym pSym, DefUseList pDefUseList);
  // setDefUseList is used only in DefUseListImpl.

  // public List
  //   getUseNodesOfSym(FlowAnalSym pSym); //##60

  public UseDefList
    getUseDefList();

  public UseDefList //##73
    getUseDefExhaustiveList(); //##73

  public List
    geUseNodeList(FlowAnalSym pSym); //##63

 public int
    getIrIndexMin();

  public int
    getIrIndexMax();

  public int
    getDefCount();

  //##62 public void
  //##62   allocateExpIdTable();

  public ExpId
    getExpId(IR pIr);

  public ExpId
    getExpId(IR pIr, int pIndex); //##60

  public void
    setExpId(IR pIr, ExpId pFlowExpId);

  public List
    getExpIdList(); //##60

  public void
    printExpIdAndIrCorrespondence(); //##60

  /** Clear all control/data flow analysis information.
   */
  public void
    clearControlFlow();

  /**
   * Clear data flow information.
   * Before calling clearDataFlow, clearControlFlow should be
   * called. clearDataFlow may be called after CFG creation
   * (after flow.controlFlowAnal(SubpFlow)) in which case,
   * CFG information is not cleared.
   */
  public void
    clearDataFlow();

  /** Clear all control/data flow analysis information
   * after executing setIndexNumberTOAllNodes.
   */
  public void
    resetControlAndDataFlowInformation();

  public java.util.Set
    computeSetOfGlobalVariables();

  public java.util.Set
    computeSetOfAddressTakenVariables();

  public void
    computeBBlockSetRefReprs(); //##63

  public java.util.Set
    setOfGlobalVariables();

  public java.util.Set
    setOfAddressTakenVariables();

  public void
    setRestructureFlag();

  public boolean
    getRestructureFlag(); //##51

  /**
   * Record a RecordAlias instance in order to indicate that
   * alias is considered in data flow analysis.
   * If pRecordAlias is not null, alias is considered.
   * If pRecordAlias is null, alias is not considered.
   * @param pRecordAlias is an instance of RecordAlias keeping the
   *  the result of alias analysis for this subprogram.
   */
  public void
    setRecordAlias(RecordAlias pRecordAlias);

  /**
   * If returned value is not null, then alias is considered in data flow
   * analysis. If it is null, then alias is not considered.
     * The returned value is used to get alias information for data flow analysis.
   * @return RecordAlias instance set by setRecordAlias method.
   */
  public RecordAlias
    getRecordAlias();

  public SetRefReprList
    getSetRefReprList(BBlock pBBlock);

  public void
    setSetRefReprList(BBlock pBBlock, SetRefReprList pSetRefReprList);

  public Set
    subtreesContainingCall();

//## public int incrementUsePointCount( IR pIr );

//## public int incrementDefPointCount( IR pIr );

  /**
   * Record pExp as the expression represented by the temporal
   * variable pTempVar.
   * @param pTempVar variable introduced to represent an expression
   *  (in process of optimization, etc.).
   * @param pExp
   */
  public void
    setExpOfTemp(Var pTempVar, Exp pExp);

  /**
   * Get the expression represented by the temporal variable pTempVar.
   * @param pTempVar variable introduced to represent an expression
   *  (in process of optimization, etc.).
   * @return the expression represented by the temporal variable.
   */
  public Exp
    getExpOfTemp(Var pTempVar);

  public BBlock
    getBBlock0(Label pLabel);

  public SetRefRepr
    getSetRefReprOfIR(IR pIR);

  public void
    setSetRefReprOfIR(SetRefRepr pSetRefRepr, IR pIR);

  public List
    getListOfBBlocksFromEntry();

  public List
    getListOfBBlocksFromExit();

  public FlowRoot
    getFlowRoot();

  public boolean isFailed(); //##78
    // When control/data flow analysis failed, then return true,
    // otherwise return false.

  /**
   * Item index used in calling methods isComputed, setComputedFlag.
   * Item numbers should be in ascending order of computation so that
   * resetComputedFlag(i) may reset all items greater or equal to i.
   * fComputedFlag[i] = 0: Item i is not yet computed.
   * fComputedFlag[i] = 1: Item i is under computation
   * fComputedFlag[i] = i: Item i is already computed.
   */
  public static final int
    CF_INDEXED   =  2,  // IR is indexed
    CF_BBLOCK    =  3,  // Subp is divided into basic blocks
    CF_CFG       =  4,  // Control flow graph
    CF_DOMINATOR =  5,  // Dominator
    CF_POSTDOMINATOR = 6, // Post dominator
    DF_MIN       =  7,  // Minimum index for data flow analysis
    DF_EXPID     =  7,  // ExpId is assigned
    DF_SETREFREPR=  8,  // SetRefRepl is computed
    DF_DEF       =  9,  // Def
    DF_KILL      = 10,  // Kkill
    DF_REACH     = 11,  // Reach
    DF_DEFINED   = 12,  // Defined
    DF_USED      = 13,  // Used
    DF_EXPOSED   = 14,  // Exposed
    DF_EGEN      = 15,  // EGen
    DF_EKILL     = 16,  // EKill
    DF_AVAILIN   = 17,  // AvailIn
    DF_AVAILOUT  = 18,  // AvailOut
    DF_LIVEIN    = 19,  // LiveIn
    DF_LIVEOUT   = 20,  // LiveOut
    DF_DEFIN     = 21,  // DefIn
    DF_DEFOUT    = 22,  // DefOut
    DF_DEFUSE    = 23,  // DefUse //##62
    DF_DEFUSELIST= 24,  // DefUseList
    DF_USEDEFLIST= 25,  // UseDefList //##63
    DF_DEFUSEEXHAUST= 26,  // DefUseExhaustiveList  //##73
    DF_USEDEFEXHAUST= 27,  // UseDefExahaustiveList //##73
    DF_TRSNSPARENT=28,  // Used in the test of MySubpFlow (in FlowImpl)
    DF_MAX       = 30;  // Maximum item index

  public static final int
      // Sym flags 24-31 can be used in each phase.
      // They may be destroyed by other phases.
    FLAG_EXPID_LHS = 24;   // Corresponding expression is LHS of AssignStmt.

  /**
   * Test if a control/data flow item is already computed or not.
   * Control flow items are reset by initiateControlFlowAnal,
   * data flow items are reset by initiateDataFlowAnal of SubpFlow.
   * @param pItemIndex item index CF_INDEXED .. DF_DEFUSELIST.
   * @return true if the item specified by pItemIndex is already computed.
   */
  public boolean
    isComputed(int pItemIndex);

  /**
   * Set the control/data flow item to be computed as true.
   * The result can be seen by isComputed.
   * @param pItemIndex item index CF_INDEXED .. DF_DEFUSELIST.
   */
  public void
    setComputedFlag( int pItemIndex);

  //##60 end

//##62 BEGIN

  /**
   * Set computed flag for the item indicated by pItemIndex.
   * @param pItemIndex
   */
  public void
    setUnderComputation(int pItemIndex);

  /**
   * Reset computed flag for all items whose item numbers are
   * greater or equal to pItemIndex.
   * @param pItemIndex
   */
  public void
    resetComputedFlag(int pItemIndex);

  public boolean
    isComputedOrUnderComputation(int pItemIndex);

  /**
   * Get the complexity level of this subprogram.
   * 1: simple. Do full analysis.
   * 2: medium. Alias analysis is simplified.
   * 3: complex. Alias analysis and data flow analysis are simplified.
   * 4: very complex. Some optimizations may be omitted.       //##100
   * 5: extremely complex.  More optimizations may be omitted. //##100
   * @return the complexity level.
   */
  public int
  getComplexityLevel(); //##62

  public boolean
    hasCall();

//##62 END

//##63 BEGIN
  /**
   * Returns true if the given IR node has as its descendant a call node.
   */
  public boolean hasCallUnder(IR pIR);

  public FlowAdapter
    getFlowAdapter();

public List
  changeListOfFlowBBlocksToListOfAflowBBlocks( List pListOfFlowBBlocks );

//##63 END

/**
 * Get the set of maximal compound variables.
 * A maximal compound variable is either
 *   subscripted variable,
 *   maximal struct/union element whose parent is not QuarifiedExp,
 *   maximal pointer qualification whose parent is not PpointedExp.
 * This set is computed when ExpIds are allocated.
 * @return the set of maximal compound variable expressions.
 */
public Set
  getMaximalCompoundVars(); //##65

public FlowAnalSym[]
  getFlowAnalSymTable();

//##70 BEGIN
/**
 * Get BBlockVector representing dominators of pBBlock.
 * As for immediate dominators,
 * see getImmediateDominator() of BBlock interface.
 * @param pBBlock Basic block.
 * @return BBlockVector representing dominators of pBBlock.
 */
public BBlockVector
  getDominators( BBlock pBBlock );

/**
 * Get BBlockVector representing post dominators of pBBlock.
 * As for immediate post dominators,
 * see getImmediateDominator() of BBlock interface.
 * @param pBBlock Basic block.
 * @return BBlockVector representing post dominators of pBBlock.
 */
public BBlockVector
  getPostDominators( BBlock pBBlock );

/**
 * Get the list of dominators of pBBlock.
 * @param pBBlock Basic block.
 * @return the list representing dominators of pBBlock.
 */
public List
  getDominatorList( BBlock pBBlock );

/**
 * Get the list of post dominators of pBBlock.
 * @param pBBlock Basic block.
 * @return the list representing post dominators of pBBlock.
 */
public List
  getPostDominatorList( BBlock pBBlock );
//##70 END

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
sortExpIdCollection( Collection pExpIdCollection );
//##78 END

} // SubpFlow interface
