/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow;

import java.util.Iterator;
import java.util.List;

import coins.aflow.util.FAList;
import coins.alias.RecordAlias;  //##53
import coins.ir.IR;       //##25
import coins.ir.hir.Exp;  //##57
import coins.ir.hir.LabeledStmt;
import coins.ir.hir.SubpDefinition;
import coins.sym.Var;     //##57

// import coins.ir.lir.Prologue; // 2004.05.31 S.Noishi
import coins.sym.FlowAnalSym;
import coins.sym.Subp;


//                          (##6): Jul. 2001.

/** SubpFlow interface:
 *  Interface for subprogram flow analysis.
 **/
public interface SubpFlow {
    public BBlock getEntryBBlock();

    public BBlock getExitBBlock();

    public void setEntryBBlock(BBlock pBlock);

    public void setExitBBlock(BBlock pBBlock);

    //	public BBlock
    //	getCurrentBBlock();
    //
    //	public void
    //	setCurrentBBlock( BBlock pBlock );
    //====== Methods to create basic blocks ======//

    /** Create new basic block corresponding to the HIR labeled statement
     *  pLabeledStmt.
     **/
    public BBlock bblock(LabeledStmt pLabeledStmt);

    /** Create new basic block corresponding to the LIR labeled node
     *  pDefLabel.
     **/
    /* 2004.05.31 S.Noishi
    public BBlock bblock(coins.ir.lir.LabelDef pDefLabel); //###12
    */

    /** Create new basic block corresponding to the LIR Prologue node
     *  pPrologue.
     **/
    /* 2004.05.31 S.Noishi
    public BBlock bblock(Prologue pPrologue); //##10 //## DELETE
    */
    public BBlockVector bblockVector();

    public ExpVector expVector();

    public PointVector pointVector();

    public DefVector defVector();

    public FlowAnalSymVector flowAnalSymVector();

    //====== Methods to correlate with index number ======//

    /** getBBlock:
     *  Get the basic block whose block number is pBlockNumber.
     *  @param pBlockNumber: block number of the basic block to be got.
     *  @return basic block that has pBlockNumber as its block number.
     *      If the BBlock having pBlocknumber is deleted (has IS_DELETED
     *      flag) then return null.
     **/
    public BBlock getBBlock(int pBlockNumber);

    public void recordBBlock(BBlock pBlock);

    public int getNumberOfBBlocks();

    public SubpDefinition getSubpDefinition();

    public Subp getSubpSym();

    //	/** setPrevBBlockInSearch:
    //	 *  getPrevBBlockInSearch:
    //	 *    Methods used in linking basic blocks in depth first order,
    //	 *    etc.
    //	 *  @param pPrev: previous BBlock from which this BBlock is
    //	 *      to be linked.
    //	 **/
    //	public void   setPrevBBlockInSearch( BBlock pPrev ); //##10
    //	public BBlock getPrevBBlockInSearch(); //##10
    //##8 Addition end
    //##12 END of methods moved from Flow.java
    //===== Iterators ======//

    /** cfgIterator:
     *  Traverse basic blocks in CFG (control flow graph)
     *  in depth first order from the entry BBlock.
     *  Available methods:
     *    next(), hasNext().
     **/
    public Iterator cfgIterator();

    /** cfgFromExitIterator:
     *  Traverse basic blocks in CFG (control flow graph)
     *  in inverse depth first order from the exit BBlock.
     *  Available methods:
     *    next(), hasNext().
     **/
    public Iterator cfgFromExitIterator();

    Iterator cfgBfoIterator();

    /** bblockSubtreeItrator
     *  Get iterator that traverse top subtrees of
     *  the basic block pBBlock.
     *  For HIR, all top subtrees are to be traversed.
     *  For LIR, all LIRTrees are to be traversed.
     **/
    public BBlockSubtreeIterator bblockSubtreeIterator(BBlock pBBlock);


    /**
     * List of BBlock object that this SubpFlow has ever recorded. Deleted (unreachable, redundant) BBlocks are included.
     */
    public FAList getBBlockTable();

    /**
     * Same as { #getReachableBBlocks()}.
     */
    List getBBlocks();

    /**
     * Same as { #setReachableBBlocks()}.
     */
    void setBBlocks(List pBBlocks);

    /**
     * Returns a list of BBlocks that are reachable from the entry BBlock. Usually this is the set of BBlocks to consider.
     */
    List getReachableBBlocks();

    /**
     * Sets a list of BBlocks that are reachable from the entry BBlock. Usually this is the set of BBlocks to consider.
     */
    void setReachableBBlocks(List pBBlocks);

    /**
     * Returns the list of BBlocks in the breadth first order beginning from the entry bblock.
     */
    List getBBlocksFromEntry();

//##25 BEGIN
    /**
     * Returns the list of BBlocks in the breadth first order beginning
     * from the exit bblock and traversing backward.
     */
    List getBBlocksFromExit();
//##25 END

    /**
     * Number of reachable BBlocks.
     */
    int getNumberOfRelevantBBlocks();

    /**
     * Returns the FlowExpId table (index number and its associated FlowExpId) for this SubpFlow.
     */
    FAList getFlowExpIdTable();

    /**
     * Sets the FlowExpId table (index number and its associated FlowExpId) for this SubpFlow.
     */
    void setFlowExpIdTable(FAList pFlowExpIdTable);

    FlowResults results();

    /**
     * Retuns the AssignFlowExpId object this SubpFlow is associated with. The AssignFlowExpId object knows which FlowExpId to issue next.
     */
    AssignFlowExpId assigner();

    /**
     * Returns the list of SetRefRepr objects within this SubpFlow.
     */
    FAList getSetRefReprs();

    /**
     * Sets the list of SetRefRepr objects within this SubpFlow.
     */
    void setSetRefReprs(FAList pSetReprs);

    /**
     * Performs the control flow analysis of this SubpFlow and makes a list of BBlocks.
     *
     * @return list of BBlocks that are reachable.
     */
    List controlFlowAnal();

    /**
     * Returns the DefUseList for the given FlowAnalSym in this SubpFlow.
     */
    DefUseList getDefUseList(FlowAnalSym pSym);

    /**
     * Sets the DefUseList for the given FlowAnalSym in this SubpFlow.
     */
    void setDefUseList(FlowAnalSym pSym, DefUseList pList);

    /**
     * Returns the UDList for the given FlowAnalSym in this SubpFlow.
     */
    UDList getUDList(FlowAnalSym pSym);


    /**
     * Sets the UDList for the given FlowAnalSym in this SubpFlow.
     */
    void setUDList(FlowAnalSym pSym, UDList pList);

    /**
     * Returns the table of symbols possibly accessed in this SubpFlow. Globol symbols are always included.
     */
    FAList getSymIndexTable();

    /**
     * Sets the table of symbols possibly accessed in this SubpFlow.
     */
    void setSymIndexTable(FAList pSymIndexTable);

    /**
     * Makes the dominator tree.
     */
    void makeDominatorTree();

    /**
     * Makes the postdominator tree.
     */
    void makePostdominatorTree();

    /**
     * Initiate data flow analysis. See subinterfaces for what is actually done.
     */
    void initiateDataFlow();

//    /**
//     * Finds the "DDef" vectors for all <code>BBlocks</code> contained in this SubpFlow. The control flow graph must already be created.
//     */
//    void findDDef();

    /**
     * Finds the "PDef" vectors for all <code>BBlocks</code> contained in this SubpFlow. The control flow graph must already be created.
     */
    void findPDef();

    /**
     * Finds the "DDefined" vectors for all <code>BBlocks</code> contained in this SubpFlow. The control flow graph must already be created.
     */
    void findDDefined();

    /**
     * Finds the "PDefined" vectors for all <code>BBlocks</code> contained in this SubpFlow. The control flow graph must already be created.
     */
    void findPDefined();

    /**
     * Finds the "DKill" vectors for all <code>BBlocks</code> contained in this SubpFlow. The control flow graph must already be created.
     */
    void findDKill();

    /**
     * Finds the "PKill" vectors for all <code>BBlocks</code> contained in this SubpFlow. The control flow graph must already be created.
     */
    void findPKill();

    /**
     * Finds the "DExpesed" and "PExposed" vectors for all <code>BBlocks</code> contained in this SubpFlow. The control flow graph must already be created.
     */
    void findDExposedUsed();

    /**
     * Finds the "PExposed" and "PUsed" vectors for all <code>BBlocks</code> contained in this SubpFlow. The control flow graph must already be created.
     */
    void findPExposedUsed();

    /**
     * Finds the "DEGen" vectors for all <code>BBlocks</code> contained in this SubpFlow. The control flow graph must already be created.
     */
    void findDEGen();

    /**
     * Finds the "PEKill" vectors for all <code>BBlocks</code> contained in this SubpFlow. The control flow graph must already be created.
     */
    void findPEKill();

    /**
     * Finds the "DReach" vectors for all <code>BBlocks</code> contained in this SubpFlow. The control flow graph must already be created.
     */
    void findDReach();

    /**
     * Finds the "PReach" vectors for all <code>BBlocks</code> contained in this SubpFlow. The control flow graph must already be created.
     */
    void findPReach();

    /**
     * Finds the "DAvailIn" and "DAvailOut" vectors for all <code>BBlocks</code> contained in this SubpFlow. The control flow graph must already be created.
     */
    void findDAvailInAvailOut();

    /**
     * Finds the "PLiveIn" and "PLiveOut" vectors for all <code>BBlocks</code> contained in this SubpFlow. The control flow graph must already be created.
     */
    void findPLiveInLiveOut();

    /**
     * Finds the "DDefIn" and "DDefOut" vectors for all <code>BBlocks</code> contained in this SubpFlow. The control flow graph must already be created.
     */
    void findDDefInDefOut();

//    /**
//     * Finds the "DDef" vectors for all <code>BBlocks</code> contained in this SubpFlow. The control flow graph must already be created.
//     */
//    void findDDefUse();
//
//    /**
//     * Finds the "DDef" vectors for all <code>BBlocks</code> contained in this SubpFlow. The control flow graph must already be created.
//     */
//    void findDUseDef();

    /**
     * Finds the <code>DefUseList</code>s for all the <code>FlowAnalSym</code>s that appear in this SubpFlow. The control flow graph must already be created.
     */
    void findDefUse();

    /**
     * Finds the <code>UDList</code>s for all the <code>FlowAnalSym</code>s that appear in this SubpFlow. The control flow graph must already be created.
     */
    void findUseDef();

//##25 BEGIN

/** Get SetRefRepr corresponding to the IR node
 *  indexed by pIndex.
 *  It is cleared by clear() of FlowResults.
 *  @param pIndex: IR node index.
 *  @return SetRefRepr corresponding to the node;
 *          return null if no corresponding one.
 */
public SetRefRepr
getSetRefReprOfIR( int pIndex);

/** Set SetRefRepr corresponding to the IR node
 *  indexed by pIndex.
 *  It is cleared by clear() of FlowResults.
 *  @param pIndex: IR node index.
 *  @param pSetRefRepr: SetRefRepr corresponding to the node;
 */
public void
setSetRefReprOfIR( SetRefRepr pSetRefRepr, int pIndex);

/** Get BBlock corresponding to the IR node
 *  indexed by pIndex.
 *  It is cleared by clear() of FlowResults.
 *  @param pIndex: IR node index.
 *  @return BBlock corresponding to the node;
 *          return null if no corresponding one.
 */
public BBlock
getBBlockOfIR( int pIndex);

/** Set BBlock corresponding to the IR node
 *  indexed by pIndex.
 *  It is cleared by clear() of FlowResults.
 *  @param pIndex: IR node index.
 *  @param pBBlock: BBlock corresponding to the node;
 */
public void
setBBlockOfIR( BBlock pBBlock, int pIndex);

/** correlateBBlockAndIR:
 * Correlate BBlock and IR so that
 *   getBBlockOfIR(hir.getIndex())
 * become effective.
 * Before calling this, controlFlowAnal() should be called.
 * This may be skipped if setBBlockOfIR is called for each IR
 * in some other processing for the subprogram corresponding to this SubpFlow.
 */
public void  //##25
correlateBBlockAndIR();

public int
getIrIndexMin();

public int
getIrIndexMax();

public void
allocateExpIdTable();

public FlowExpId
getExpId( IR pIr );

public void
setExpId( IR pIr, FlowExpId pFlowExpId );

public void
printExpIdTable();

/** Clear flow analysis information by resetting
 *  fIrIndexMin = 0, fIrIndexMax = 0,
 *  fSetOfGlobalSymbols, fSetOfAddressTakenSymbols.
 */
public void
clear();

public java.util.Set
computeSetOfGlobalVariables();

public java.util.Set
computeSetOfAddressTakenVariables();

public java.util.Set
setOfGlobalVariables();

public java.util.Set
setOfAddressTakenVariables();

public void
setRestructureFlag();

public boolean
getRestructureFlag(); //##51

//====== Methods to correlate with index number ======//

//### public void
//### resetFlowSymLinkForRecordedSym( ); //##20

/** resetFlowSymLink:
 *  Reset all flow analysis information of symbols
 *  in pSymTable and its descendents.
 *  Symbols to be reset are those ones that are recorded
 *  as accessed symbols (getAccessedSyms()) in a subprogram,
 *  and so, this method is effective to reset the effect of previous
 *  flow analysis for a subprogram. For global symbols, use
 *  resetGlobalFlowSymLink().
**/
//### public void
//### resetFlowSymLink( SymTable pSymTable );

/** resetGlobalFlowSymLink:
 *  Reset all flow analysis information of symbols
 *  that are contained in ancestor symbol table (symTableRoot, etc)
 *  without traversing its descendents.
**/
//### public void
//### resetGlobalFlowSymLink();

//##25 END

//##53 BEGIN

/**
 * Record a RecordAlias instance in order to indicate that
 * alias is considered in data flow analysis.
 * If pRecordAlias is not null, alias is considered.
 * If pRecordAlias is null, alias is not considered.
 * @param pRecordAlias is an instance of RecordAlias keeping the
 *    the result of alias analysis for this subprogram.
 */
public void
setRecordAlias( RecordAlias pRecordAlias );

/**
 * If returned value is not null, then alias is considered in data flow
 * analysis. If it is null, then alias is not considered.
 * The returned value is used to get alias information for data flow analysis.
 * @return RecordAlias instance set by setRecordAlias method.
 */
public RecordAlias
getRecordAlias();

//##53 END

//##57 BEGIN
/**
 * Record pExp as the expression represented by the temporal
 * variable pTempVar.
 * @param pTempVar variable introduced to represent an expression
 *    (in process of optimization, etc.).
 * @param pExp
 */
public void
setExpOfTemp( Var pTempVar, Exp pExp );

/**
 * Get the expression represented by the temporal variable pTempVar.
 * @param pTempVar variable introduced to represent an expression
 *    (in process of optimization, etc.).
 * @return the expression represented by the temporal variable.
 */
public Exp
getExpOfTemp( Var pTempVar );

//##57 END

} // SubpFlow interface
