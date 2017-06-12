/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow;

import java.util.List;

import coins.ir.IR;
import coins.ir.hir.Exp;
import coins.sym.Label;
import coins.sym.Sym;


//                      (##10): modified on Dec. 2001.
//                      (##5 ): modified on May  2001.

/** BBlock interface:
 *  Interface for basic block class BBlock.
 *
 *  Data flow analysis and basic control flow analysis are done
 *  based on the graph of basic blocks.
 *  All methods other than static ones are a method used for
 *  BBlock object ("this" is a BBlock instance).
 *
 * Structure of basic blocks:
 *
 *  Input program represented in HIR or LIR is decomposed into
 *  basic blocks. Control flow of the program can be represented
 *  by a graph of basic blocks connected by predecessor/successor
 *  relation. Information of the basic block is represented by
 *  an object of BBlock class.
 *
 *  Contents of a basic block can be treated as a sequence of subtrees
 *  which may be either HIR subtree or LIR subtree.
 *  Such subtree is called a "top-subtree".
 *  The top-subtree of HIR may represent a part of statement or
 *  expression where some other part of the statement or expression
 *  may be included in different basic blocks. For LIR, on the other hand, an LIR subtree is just a member of the aforementioned sequence, and there are no links or complex relations between LIR subtrees.
 *
 *  An HIR statement attached with label (LabeledStmt)
 *  starts a new basic block.
 *  If an HIR expression has label definition, it starts a new basic block(This may change in the future).
 *  Depth first traversal of HIR tree from left to right represents
 *  a sequence of HIR nodes. In this traversal, if an HIR statement
 *  node is encountered, it starts a new top-subtree.
 *  If a top-subtree is an expression subtree and another
 *  expression subtree or statement subtree without label definition
 *  succeeds immediately after the top-subtree, then the
 *  succeeding subtree is treated as the next top-subtree.
 *
 *  The LIR top-subtree represents an LIR instruction.
 *  In LIR flow analysis, a basic block is composed of a list of
 *  LIR top-subtees (LIR instructions). LIR basic block is based solely on the flow of control. The control flow graph LIR basic blocks form will therefore be "irreducible".
 *
 *
 *  Data flow analysis are done on the graph of basic blocks
 *  and most of data flow information is associated with basic blocks.
 *  The graph of basic blocks can be get from a SubpFlow object, which corresponds to a subprogram.
 *
 *
 *  While HIR flow analysis and optimization can be thought of as utilizing the information supplied by the control flow graph, LIR flow analysis/optimization are done totally based on the control flow graph. An LIR basic block object holds the whole LIR subtree sequence that are associated to the basic block object, while in HIR there is only a link between the the basic block and the <code>LabeledStmt</code> that signifies the start of the basic block. A <code>SetRefRepr</code> class that wraps an HIR statement/LIR instruction along with its collection class <code>SetRefReprList</code> will blur the distinction, however.
 *
 *
 * Relation between control flow and data flow:
 *  This class provides accessor methods for data flow information for convinience, but actual data flow information is stored in an external object, namely, a <code>FlowResults</code> object. When there is a modification of code that changes data flow but not control flow, the data flow information the external object holds must be cleared, but the control flow information this class holds will remain valid.
 *  Methods listed here are used after flow analysis to get
 *  information or to modify control flow.
 *  (There may be many methods private to flow analysis
 *   modules other than the methods listed below.)
 *  When control flow is changed, data flow information
 *  may not be consistent because data flow information
 *  is not automatically updated by the control flow
 *  modification. In order to make them consistent,
 *  it is necessary to do data flow analysis again
 *  after a group of control flow modifications are
 *  completed.
 *
 **/
public interface BBlock {
    //====== Interface for control flow analysis ======//
    //------ Methods to get attributes ------//

    /** getBBlockNumber:
     *  Get the block number attached to this basic block.
     *  @return the block number attached to this basic block.
     */
    int getBBlockNumber();

    /** getIrLink:
     *  Get the top-subtree that starts this basic block.
     *  See getLirTreeList for using LIRTreeListIterator.
     *       returned value of getIrLink is not applicable for
     *       LIRTreeListIterator.
     *  @return LabeledStmt or DefLabel subtree that is the first
     *      top-subtree in this basic block.
     **/
    public IR getIrLink();

    public void setIrLink(IR pIR);

    /** getLabel:
     *  Get a label attached to the first top-subtree of this basic block.
     *  @return the label attached to the first top-subtree of this
     *      basic block. If multiple labels are attached, return the
     *      first one which show a relation with other blocks.
     **/
    Label getLabel();

    /**
     * Returns the <code>SubpFlow</code> object this <code>BBlock</code> belongs to.
     */
    SubpFlow getSubpFlow();

    /**
     * Returns the <code>FlowResults</code> object that holds data flow information.
     */
    FlowResults results();

    // -- Methods that delegate access to the FlowResults map which actually stores data

    /**
     * Gets the <code>SetRefReprList</code> object that is the list of <code>SetRefRepr</code> objects, each of which wrap a statement/instruction.
     */
    SetRefReprList getSetRefReprs();

    /**
     * Sets the <code>SetRefReprList</code> object that is the list of <code>SetRefRepr</code> objects, each of which wrap a statement/instruction.
     */
    void setSetRefReprs(SetRefReprList pSetRefReprs);

    //------ Methods for control flow analysis ------//

    /** getPredList:
     *  getSuccList:
     *  Get the predecessor/successor list of this basic block.
     *  Elements of the list can be accessed by List methods.
     *  See ListIterator for traversing BBlocks in the
     *      predecessor/successor list.
     *  @return the predecessor/successor list of this basic block.
     *      If this has no predecessor/successor, empty list is returned.
     **/
    List getPredList();

    List getSuccList();

    /** getPredEdge:
     *  getSuccEdge:
     *  Get the edge corresponding to the predecessor/successor
     *  to/from this basic block.
     *  getPredEdge: Get the edge spanning from pFromBBlock to this.
     *  getSuccEdge: Get the edge spanning from this to pToBBlock.
     **/
    public Edge getPredEdge(BBlock pFromBBlock);

    public Edge getSuccEdge(BBlock pToBBlock);

    //------ See the characteristics of basic block ------//

    /** isEntryBlock:
     *  See if this block is the entry block in the graph of
     *  basic blocks.
     *  @return true if this is the entry block in the graph of basic
     *      blocks, else return false.
     **/

    //        boolean
    //        isEntryBlock();
    boolean isEntryBBlock();

    /** isExitBlock:
     *  See if this block is the exit block in the graph of
     *  basic blocks.
     *  @return true if this is the exit block in the graph of basic
     *      blocks, else return false.
     **/

    //        boolean
    //        isExitBlock();
    boolean isExitBBlock();

    //----- Dominator/Postdominator stuff-----//

    /**
     * Get the list of <code>BBlock</code>s that dominate this <code>BBlock</code> in the <code>SubpFlow</code> this <code>BBlock</code> belongs to.
     */
    List getDomForSubpFlow();

    /**
     * Set the list of <code>BBlock</code>s that dominate this <code>BBlock</code> in the <code>SubpFlow</code> this <code>BBlock</code> belongs to.
     */
    void setDomForSubpFlow(List pDom);

    /**
     * Get the strict dominator for this <code>BBlock</code> in the <code>SubpFlow</code> this </code>BBlock</code> belongs to. A strict dominator is the dominator list minus the <code>BBlock</code> itself.
     */
    List getStrictDomForSubpFlow();

    /** getImmediateDominator:
     *  Get/set a basic block immediately dominating this block.
     *  getImmediateDominator: return the basic block immediately
     *      dominating this block.
     *  setImmediateDominator: set pDominator as the immediate
     *      dominator of this block.
     **/
    public BBlock getImmediateDominatorForSubpFlow();

    public void setImmediateDominatorForSubpFlow(BBlock pDominator); // (##5)

    /** getDominatedChildren:
     *  setDominatedChildren:
     *  Get/set the list of basic blocks immediately dominated by this
     *  block. Elements of the list can be handled by List methods.
     *  getDominatedChildren: return the list of basic blocks
     *      immediately dominated by this block.
     *  setDominatedChildren: set pDominatedChildren as the list of
     *      basic blocks dominated by this block.
     *  See ListIterator for traversing dominated children.
     **/
    List getDominatedChildrenForSubpFlow();

    public void setDominatedChildrenForSubpFlow(List pDominatedChildren);

    List getPostdomForSubpFlow();

    void setPostdomForSubpFlow(List pPostdom);

    List getStrictPostdomForSubpFlow();

    /** getImmediatePostDominator:
     *  Get a basic block immediately post dominating this block.
     *  @return the basic block immediately post dominating this block.
     **/
    BBlock getImmediatePostdominatorForSubpFlow();

    public void setImmediatePostdominatorForSubpFlow(BBlock pPostDominator); // (##5)

    /** getPostDominatedChildren:
     *  setPostDominatedChildren:
     *  Get/set the list of basic blocks post dominated by
     *  this block. Elements of the list can be handled by List methods.
     *  getPostDominatedChildren: return the list of basic blocks
     *      post dominated by this block.
     *  setPostDominatedChildren: set pPostDominatedChildren as the
     *      list of basic blocks post dominated by this block.
     *  See ListIterator for traversing dominated children.
     **/
    public List getPostdominatedChildrenForSubpFlow();

    public void setPostdominatedChildrenForSubpFlow(List pPostDominatedChildren);

    //------ Get/set phase-wise information ------//

    /** getWork:
     *  setWork:
     *  get/set information privately used in each phase.
     **/
    Object getWork();

    void setWork(Object pWork);

    //------ Iterators and methods for traversing ------//

    /** bblockSubtreeIterator:
     *  Create an iterator to traverse all top-subtree in this basic block.
     *  All top-subtrees can be get by invoking next() for the iterator.
     *  Using <code>SetRefReprIterator</code> is the preferred method of iterating subtrees.
     *  See SetRefReprIterator
     *  @return the BBlockSubtree iterator for this BBlock.
     **/
    public BBlockSubtreeIterator bblockSubtreeIterator();

    public BBlockNodeIterator bblockNodeIterator(); //##31

    //------ Methods for printing ------//

    /** toStringShort: //##10
     *  @return Basic block number and predecessor/successor list.
     **/
    public String toStringShort();

    /** toStringDetail: //##10
     *  @return toStringShort(), linked node, and flags.
     **/
    public String toStringDetail();

    /** fuseSuccessor:
     *  Fuse the successor pToBlock with this block and make
     *  all successors of pToBlock as successors of this block.
     *  "this" is a basic block having pToBlock as its only one successor,
     *  and pToBlock should have "this" as its only one predecessor.
     *  Before invoking this method, this block should be changed
     *  to have only one successor pToBlock if the condition is
     *  not satisfied.
     *  Branch addresses of this block and linkages between successors
     *  of pToBlock are changed so that consistency is kept.
     *  @param pToBlock: only one succeccor of this block.
     *      pToBlock has only one predecessor and it is "this" block.
     **/
    void fuseSuccessor(BBlock pToBlock);

    //======== Elementary methods that change CFG ========//
    //
    // Following methods are changed to be private in BBlock class:
    //   changeEdge, addEdge, deleteEdge, deleteBBlock,    (##5)
    //   addToPredList, addToSuccList,
    //   deleteFromPredList, deleteFromSuccList.

    /** changeEdge:
     *  Change the edge between this block and pBefore to a new edge
     *  between this block and pAfter.
     *  Branch addresses of this block and linkages between successors
     *  of this block are changed so that consistency is kept.
     *  @param pBefore: successor block of this block.
     *  @param pAfter:  block to be made as the new successor of this block.
     **/
    void changeSuccEdge(BBlock pBefore, BBlock pAfter);

    void changePredEdge(BBlock pBefore, BBlock pAfter);

    /** addEdge:
     *  Add edge from this block to pToBlock that is to be taken
     *  when pConditionalExp is true.
     *  Branch addresses of this block and linkages between successors
     *  of this block are changed so that consistency is kept.
     *  "this" is a basic block having only  one successor.
     *  @param pConditionalExp: conditional expression to be evaluated
     *      as true or false.
     *  @param pToBlock: a block to be made as the new successor of this block.
     **/
    void addEdge(Exp pConditionalExp, BBlock pToBlock);

    /** deleteEdge:
     *  Delete the edge from this block to pToBlock and adjust
     *  related addresses and links so as to keep consistency.
     *  If pToBlock has this block as its only one predecessor,
     *  then pToBlock is deleted.
     *  @param pToBlock: successor of this block.
     **/
    void deleteEdge(BBlock pToBlock);

    /** deleteBBlock:
     *  Delete this block and add successors of this block to the set of
     *  successors of all predecessors of this block. Branch statement
     *  or instructions in this block is moved to all predecessors of
     *  this block so as to keep consistency.
     *  "this" is a basic block whose predecessors have this block
     *  as their only one successor. If the predecessors have more than
     *  one successors, they should be modified before invoking
     *  this method.
     **/
    void deleteBBlock();

    /** addToPredList:
     *  addToSuccList:
     *  Add the basic block pPred/pSucc to the list of predecessors/successors
     *  of this basic block, and this block is added to the list of
     *  successors/predecessors of pPred/pSucc.
     *  Note: For method xxxPredxxx, take 1st word of predecessor/successor
     *      or the like, and for method xxxSuccxxx, take 2nd word of
     *      predecessor/successor or the like. The same rule applies to
     *      other methods in this interface.
     *  @param pPred: basic block to be added to the predecessor list
     *      of this basic block.
     *  @param pSucc: basic block to be added to the successor list
     *      of this basic block.
     **/
    void addToPredList(BBlock pPred);

    void addToSuccList(BBlock pSucc);

    /** deleteFromPredList:
     *  deleteFromSuccList:
     *  Delete the basic block pPred/pSucc from to the list of
     *  predecessors/successors of this basic block, and this block is
     *  deleted from the list of successors/predecessors of pPred/pSucc.
     *  @param pPred: basic block to be deleted from the predecessor list
     *      of this basic block.
     *  @param pSucc: basic block to be deleted from the successor list
     *      of this basic block.
     **/
    void deleteFromPredList(BBlock pPred);

    void deleteFromSuccList(BBlock pSucc);

    //------ Get/set bit vector for data flow analysis ------//
    // For general definitions of the following bit vectors, see Flow.java. For more accurate definitions (if any), see the subinterfaces of this interface. The default implementations (BBlockHirImpl, BBlockLirImpl) do not take aliases into consideration at all (Assumes there is no alias).

    /** getDef:
     *  Get bit vector showing Def set of this block. The Def set of this BBlock is the DefVector whose set bits correspond to SetRefReprs that sets some value (AssignStmt in HIR) and that value is not changed afterwards in this BBlock.
     *  @return bit vector representing Def(B) of this block B.
     **/

    //	DefVector getDef();
    //	void      setDef( DefVector pVect );

    /** getDDef:
     *  Get bit vector showing DDef set of this block. The DDef set of this BBlock corresponds to the  DefVector whose set bits correspond to SetRefReprs that set some value (AssignStmt in HIR), and the memory address the assignment took place can be traced and its content is definitely not overwritten when the program control exits this BBlock. The rationale for this definition is that when an expression with the same form as the LHS of one of the elements of the DDef set appears,
     *  @return bit vector representing Def0(B) of this block B.
     **/
//    DefVector getDDef();
//
//    void setDDef(DefVector pVect);

    /** getPDef:
     *  Get bit vector showing PDef set of this block. The PDef set of this BBlock corresponds to the DefVector whose set bits correspond to SetRefReprs execution of which may write some data to a memory location and the data on the the location may not have been overwritten when the program control exits this BBlock.
     *  @return bit vector representing PDef(B) of this block B.
     **/
    DefVector getPDef();

    void setPDef(DefVector pVect);

    /** getDKill:
     *  Get bit vector showing DKill set of this block. The DKill set ot this BBlock corresponds to the DefVector whose set bits correspond to SetRefReprs that definitely writes to the same address as one of the SetRefReprs that belong to this BBlock.
     *  @return bit vector representing DKill(B) of this block B.
     **/
    DefVector getDKill();

    void setDKill(DefVector pVect);

    /** getPKill:
     *  Get bit vector showing PKill set of this block. The PKill set ot this BBlock corresponds to the DefVector whose set bits correspond to SetRefReprs execution of which may write some data to a memory location, and its content may change or the location may become no longer traceable by execution of one of the SetRefReprs that belong to this BBlock.
     *  @return bit vector representing PKill(B) of this block B.
     **/
    DefVector getPKill();
//
    void setPKill(DefVector pVect);

    /**
     * Returns the DReach. DReach is the minimum that satisfies DReach(B) =  or_all( (DDef(B') | (DReach(B') - PKill(B')))
     *                               for all predecessors B' of B.
//     */
//    DefVector getDReach();
//
//    void setDReach(DefVector pVect);

    /**
     * Returns the PReach. PReach is the minimum that satisfies PReach(B) =  or_all( (PDef(B') | (PReach0(B') - DKill(B')))
     *                               for all predecessors B' of B.
     */
    DefVector getPReach();

    void setPReach(DefVector pVect);

    /** getDDefined:
     *  Get bit vector showing DDefined set of this block. The DDefined set is the set of <code>FlowAnalSym</code>s that are definitely defined in this <code>BBlock</code>.
     *
     *  @return bit vector representing DDefined(B) of this block B.
     **/
    FlowAnalSymVector getDDefined();

    void setDDefined(FlowAnalSymVector pVect);

    /** getPDefined:
     *  Get bit vector showing PDefined set of this block. The DDefined set is the set of <code>FlowAnalSym</code>s whose value may have changed in this <code>BBlock</code>..
     *
     *  @return bit vector representing PDefined(B) of this block B.
     **/
    FlowAnalSymVector getPDefined();

    void setPDefined(FlowAnalSymVector pVect);

    /** getDUsed: //##12
     *  Get bit vector showing DUsed set of this block. DUsed set is the union of all used sets of <code>SetRefRepr</code>s (specified by SetRefRepr#getUseSyms()).
     *  @return bit vector representing DUsed(B) of this block B.
     **/
    FlowAnalSymVector getDUsed(); //##12

    void setDUsed(FlowAnalSymVector pVect); //##12

    /** getPUsed: //##12
     *  Get bit vector showing PUsed set of this block. PUsed set is DUsed set + global variables (if this BBlock has external call) that appear in the Subp that this BBlock belongs to.
     *  @return bit vector representing PUsed(B) of this block B.
     **/
    FlowAnalSymVector getPUsed(); //##12

    void setPUsed(FlowAnalSymVector pVect); //##12

    /** getDExposed:
     *  Get bit vector showing DExposed set of this block. The DExposed set is the set of FlowAnalSyms that are definitely referred to before being set within this BBlock.
     *  @return bit vector representing DExposed(B) of this block B.
     **/
    FlowAnalSymVector getDExposed();

    void setDExposed(FlowAnalSymVector pVect);

    /** getPExposed:
     *  Get bit vector showing PExposed set of this block. The PExposed set is the set of FlowAnalSyms that may be referred to before being set within this BBlock.
     *  @return bit vector representing PExposed(B) of this block B.
     **/
    FlowAnalSymVector getPExposed();

    void setPExposed(FlowAnalSymVector pVect);

    /** getEGen:
     *  Get bit vector showing DEGen set of this block. The DEGen set is the set of <code>FlowExpId</code>s that appear in this BBlock and the result of the computation of the expression of the FlowExpId can definitely be used at the end of this BBlock.
     *  @return bit vector representing EGen(B) of this block B.
     **/
    ExpVector getDEGen();

    void setDEGen(ExpVector pVect);

    /** getEKill:
     *  Get bit vector showing EKill set of this block. The EKill set is the set of <code>FlowExpId</code>s whose operand set (specified by FlowExpId#getOperandSet()) intersects the Modified set of this <code>BBlock</code>.
     *  @return bit vector representing EKill(B) of this block B.
     **/
    ExpVector getPEKill();

    void setPEKill(ExpVector pVect);

    /** getAvailIn:
     *  Get bit vector showing AvailIn set of this block.
     *  @return bit vector representing AvailIn(B) of this block B.
     **/
    ExpVector getDAvailIn();

    void setDAvailIn(ExpVector pVect);

    /** getAvailOut:
     *  Get bit vector showing AvailOut set of this block.
     *  @return bit vector representing AvailOut(B) of this block B.
     **/
    ExpVector getDAvailOut();

    void setDAvailOut(ExpVector pVect);

    /** getLiveIn:
     *  Get bit vector showing LiveIn set of this block.
     *  @return bit vector representing LiveIn(B) of this block B.
     **/
    FlowAnalSymVector getPLiveIn();

    void setPLiveIn(FlowAnalSymVector pVect);

    /** getLiveOut:
     *  Get bit vector showing LiveOut set of this block.
     *  @return bit vector representing LiveOut(B) of this block B.
     **/
    FlowAnalSymVector getPLiveOut();

    void setPLiveOut(FlowAnalSymVector pVect);

    /** getDefIn:
     *  Get bit vector showing DefIn set of this block.
     *  @return bit vector representing DefIn(B) of this block B.
     **/
    FlowAnalSymVector getDDefIn();

    void setDDefIn(FlowAnalSymVector pVect);

    /** getDefOut:
     *  Get bit vector showing DefOut set of this block.
     *  @return bit vector representing DefOut(B) of this block B.
     **/
    FlowAnalSymVector getDDefOut();

    void setDDefOut(FlowAnalSymVector pVect);

    /** isDef:
     *  See if definition at position pPos is done in this block.
     *  @param pPos: position number attached to the definition node.
     *  @return true if the definition is done in this block,
     *      false otherwise.
     **/
    boolean isDDef(SetRefRepr pSetRefRepr);

    boolean isPDef(SetRefRepr pSetRefRepr);

    /** isKill:
     *  See if definition at position pPos is killed in this block.
     *  @param pPos: position number attached to the definition node.
     *  @return true if the definition at pPos is killed in this block,
     *      false otherwise.
     **/
    boolean isDKill(SetRefRepr pSetRefRepr);

    boolean isPKill(SetRefRepr pSetRefRepr);

    /** isReach:
     *  See if definition at position pPos reaches to the entry point
     *  of this block.
//     *  @param pPos: position number attached to the definition node.
//     *  @return true if the definition at pPos reaches to the entry point
//     *      of this block, false otherwise.
//     **/
//    boolean isDReach(SetRefRepr pSetRefRepr);

    /** isReach0:
     *  See if definition at position pPos may reach to the entry point
     *  of this block.
     *  @param pPos: position number attached to the definition node.
     *  @return true if the definition at pPos may reach to the entry point
     *      of this block, false otherwise.
     **/
    boolean isPReach(SetRefRepr pSetRefRepr);

    /** isDefined:
     *  See if the value of pSym is defined in this block.
     *  @param pSym: symbol representing a variable or a register
     *      or expression identifier.
     *  @return true if p(def(pSym)) is included in this block,
     *      false otherwise.
     **/
    boolean isDDefined(Sym pSym);

    /** isModified:
     *  See if the value of pSym is modified in this block.
     *  @param pSym: symbol representing a variable or a register
     *      or expression identifier.
     *  @return true if p(mod(pSym)) is included in this block,
     *      false otherwise.
     **/
    boolean isPDefined(Sym pSym);

    /** isUsed: //##12
     *  See if the value of pSym is used in this block.
     *  @param pSym: symbol representing a variable or a register
     *      or expression identifier.
     *  @return true if p(use(pSym)) is included in this block,
     *      false otherwise.
     **/
    boolean isDUsed(Sym pSym);

    boolean isPUsed(Sym pSym);

    /** isExposed:
     *  See if the variable of pSym is used in this block without
     *  setting its value in this block.
     *  @param pSym: symbol representing a variable or a register
     *      or expression identifier.
     *  @return true if pSym is used in this block and it is not set
     *      in this block before the use point.
     **/
    boolean isDExposed(Sym pSym);

    boolean isPExposed(Sym pSym);

    /** isEGen:
     *  See if expression designated by pReg is generated (computed in
     *  this block and afterwards its operand is not changed) in this block.
     *  @param pExpId: expression identifier assigned to the
     *      expression.
     *  @return true if the expression is generated in this block,
     *      false otherwise.
     **/
    boolean isDEGen(FlowExpId pExpId);

    /** isEKill:
     *  See if expression designated by pReg is killed
     *  (its operand is defined) in this block.
     *  @param pExpId: expression identifier assigned to the
     *      expression.
     *  @return true if the expression is killed in this block,
     *      false otherwise.
     **/
    boolean isPEKill(FlowExpId pExpId);

    /** isAvailIn:
     *  See if the expression represented by pExpId is available
     *  (value is always computed) at entry to this block.
     *  @param pExpId: expression identifier
     *      representing an expression.   (##5)
     *  @return true if pExpId is available at entry to this block,
     *      false otherwise.
     **/
    boolean isDAvailIn(FlowExpId pExpId);

    /** isAvailOut:
     *  See if the expression represented by pExpId is available
     *  (value is always computed) at exit from this block.
     *  @param pExpId: expression identifier
     *      representing an expression.   (##5)
     *  @return true if pExpId is available at exit from this block,
     *      false otherwise.
     **/
    boolean isDAvailOut(FlowExpId pExpId);

    /** isLiveIn:
     *  See if pSym is live at entry to this block (value at entry is
     *  used in this block or in some successor of this block).
     *  @param pSym: variable or register or expression identifier
     *      representing an expression.
     *  @return true if pSym is live at entry to this block,
     *      false otherwise.
     **/
    boolean isPLiveIn(Sym pSym);

    /** isLiveOut:
     *  See if pSym is live at exit from this block (value at exit is
     *  used in some successor of this block).
     *  @param pSym: variable or register or expression identifier
     *      representing an expression.
     *  @return true if pSym is live at exit from this block,
     *      false otherwise.
     **/
    boolean isPLiveOut(Sym pSym);

    /** isDefIn:
     *  See if pSym is always defined at entry to this block whichever
     *  path may be take.
     *  @param pSym: variable or register or expression identifier
     *      representing an expression.
     *  @return true if pSym is always defined at entry to this block,
     *      false otherwise.
     **/
    boolean isDDefIn(Sym pSym);

    /** isDefOut:
     *  See if pSym is always defined at exit from this block.
     *  @param pSym: variable or register or expression identifier
     *      representing an expression.
     *  @return true if pSym is always defined at exit from this block,
     *      false otherwise.
     **/
    boolean isDDefOut(Sym pSym);

    // Factory methods

    /**
     * Returns the <code>SetRefRepr</code> object enclosing the given IR.
     */
    SetRefRepr setRefRepr(IR pIR);
}
