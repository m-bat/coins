/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.flow;

import java.util.LinkedList;
import java.util.List;

import coins.CompileError;
import coins.ir.IR;
import coins.ir.hir.Exp;
import coins.ir.hir.HIR;  //##73
import coins.ir.hir.LabeledStmt;
import coins.ir.hir.Stmt;
//##60 import coins.ir.lir.LIR;
//##60 import coins.ir.lir.LIRTree;
//##60 import coins.ir.lir.LIRTreeList;
import coins.sym.Const;
import coins.sym.ExpId;
import coins.sym.Label;
import coins.sym.Subp;
import coins.sym.Sym;

//      Drastically revised on Apr.-Jul. 2005.
//      Modified on Dec. 2001.
//      Modified on May  2001.

/** BBlock interface
 <PRE>
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
 *  an object of BBlock class. A label definition in IR tree shows
 *  a link to BBlock object. An HIR statement node or LIR instruction
 *  node attached with label indicates the beginning of a new basic
 *  block.
 *
 *  Contents of a basic block can be treated as a sequence of subtrees
 *  which may be either HIR subtree or LIR subtree.
 *  Such subtree is called a "top-subtree".
 *  The top-subtree of HIR may represent a part of statement or
 *  expression where some other part of the statement or expression
 *  may be included in different basic blocks.
 *
 *  An HIR statement attached with label (LabeledStmt)
 *  starts a new basic block.
 *  If an HIR expression has label definition, it starts a new basic block.
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
 *  LIR top-subtees (LIR instructions).
 *  A label definition in LIR (DefLabel node) starts a new
 *  basic block.
 *
 *  Data flow analysis are done on the graph of basic blocks
 *  and most of data flow information is get via basic blocks.
 *  The graph of basic blocks can be get from subprogram symbol.
 *
 * Relation between control flow and data flow:
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
 </PRE>
 **/
public interface
  BBlock
{

//====== Interface for control flow analysis ======

//------ Methods to get attributes ------

  /** getBlockNumber
   *  Get the block number attached to this basic block.
   *  @return the block number attached to this basic block.
   **/
  int
    getBlockNumber();

  /** getIrLink
   *  Get the top-subtree that starts this basic block.
   *  See bblockSubtreeIterator or bblockNodeIterator for
   *    traversing top-subtrees or nodes in the basic block.
   *  See getLirTreeList for using LIRTreeListIterator.
   *     returned value of getIrLink is not applicable for
   *     LIRTreeListIterator.
   *  @return LabeledStmt or DefLabel subtree that is the first
   *    top-subtree in this basic block.
   **/
  public IR
    getIrLink();

  /** setIrLink
   *  Set HIR statement pLabeledStmt or LIR DefLabel subtree
   *  as the first top-subtree that starts this basic block.
   **/
  public void setIrLink(LabeledStmt pLabeledStmt);

//##60 public void setIrLink( LIRTreeList pTreeList );

  /** getLabel
   *  Get a label attached to the first top-subtree of this basic block.
   *  @return the label attached to the first top-subtree of this
   *    basic block. If multiple labels are attached, return the
   *    first one which show a relation with other blocks.
   **/
  Label
    getLabel();

//------ Methods for control flow analysis ------

  /** getPredList
   *  Get the predecessor list of this basic block.
   *  If this has no predecessor, empty list is returned.
   *  Elements of the list can be accessed by List methods.
   *  See ListIterator for traversing BBlocks in the
   *    predecessor/successor list.
   *  @return the predecessor list of this basic block.
   **/
  List getPredList();

  /** getSuccList
   *  Get the successor list of this basic block.
   *  If this has no successor, empty list is returned.
   *  Elements of the list can be accessed by List methods.
   *  See ListIterator for traversing BBlocks in the
   *    successor list.
   *  @return the successor list of this basic block.
   **/
  List getSuccList();

  /** getPredEdge
   *  Get the edge corresponding to the predecessor/successor
   *  to/from this basic block.
   *  getPredEdge Get the edge spanning from pFromBBlock to this.
   *  getSuccEdge Get the edge spanning from this to pToBBlock.
   **/
  public Edge getPredEdge(BBlock pFromBBlock);

  /** getSuccEdge
   *  Get the edge corresponding to the predecessor/successor
   *  to/from this basic block.
   *  getPredEdge Get the edge spanning from pFromBBlock to this.
   *  getSuccEdge Get the edge spanning from this to pToBBlock.
   **/
  public Edge getSuccEdge(BBlock pToBBlock);


//------ See the characteristics of basic block ------

  /** isEntryBlock
   *  See if this block is the entry block in the graph of
   *  basic blocks.
   *  @return true if this is the entry block in the graph of basic
   *    blocks, else return false.
   **/
  boolean
    isEntryBlock();

  /** isExitBlock
   *  See if this block is the exit block in the graph of
   *  basic blocks.
   *  @return true if this is the exit block in the graph of basic
   *    blocks, else return false.
   **/
  boolean
    isExitBlock();

  /** isLoopBackEdge
   *  See if an edge is a loop back edge or not.
   *  @param pPredecessor basic block contained in a basic
   *    block graph containing this block.
   *  @return true if the edge from pPredecessor to this block
   *    is a loop back edge, otherwise return false.
   boolean
   isLoopBackEdge( BBlock pPredecessor );
   */

//##73 BEGIN
  /**
   * If this basic block ends with control transfer statement
   * such as JUMP, RETURN, then this method returns the control transfer
   * statement. If not, this method returns null.
   * @return trailing control transfer statement if
   *     this BBlock ends with such statement.
   */
  public HIR
   controlTransfer();
 //##73 END

//------ Get/set relations between basic blocks ------
   /** getImmediateDominator
    *  Get/set a basic block immediately dominating this block.
    *  getImmediateDominator: return the basic block immediately
    *    dominating this block.
    *  setImmediateDominator: set pDominator as the immediate
    *    dominator of this block.
    **/
   public BBlock
     getImmediateDominator();

  public void
    setImmediateDominator(BBlock pDominator); // ()

  /** getDominatedChildren
   *  setDominatedChildren
   *  Get/set the list of basic blocks immediately dominated by this
   *  block. Elements of the list can be handled by List methods.
   *  getDominatedChildren: return the list of basic blocks
   *    immediately dominated by this block.
   *  setDominatedChildren: set pDominatedChildren as the list of
   *    basic blocks dominated by this block.
   *  See ListIterator for traversing dominated children.
   **/
  List
    getDominatedChildren();

  public void
    setDominatedChildren(LinkedList pDominatedChildren);

  /** getImmediatePostDominator
   *  Get a basic block immediately post dominating this block.
   *  @return the basic block immediately post dominating this block.
   **/
  BBlock
    getImmediatePostDominator();

  public void
    setImmediatePostDominator(BBlock pPostDominator); // ()

  /** getPostDominatedChildren
   *  setPostDominatedChildren
   *  Get/set the list of basic blocks post dominated by
   *  this block. Elements of the list can be handled by List methods.
   *  getPostDominatedChildren: return the list of basic blocks
   *    post dominated by this block.
   *  setPostDominatedChildren: set pPostDominatedChildren as the
   *    list of basic blocks post dominated by this block.
   *  See ListIterator for traversing dominated children.
   **/
  public List
    getPostDominatedChildren();

  public void
    setPostDominatedChildren(LinkedList pPostDominatedChildren);

  /** linkInDepthFirstOrder(Subp pSubp)
   *  Link basic blocks of subprogram pSubp in depth first
   *  order starting from the entry BBlock.
   **/
  public void
    linkInDepthFirstOrder(Subp pSubp);

  /** linkInInverseDepthFirstOrder(Subp pSubp)
   *  Link basic blocks of subprogram pSubp in inverse depth first
   *  order starting from the exit BBlock.
   **/
  public void
    linkInInverseDepthFirstOrder(Subp pSubp);

  /** getNextInDFO Get the next BBlock in depth first order.
   *  Before calling getNextInDFO, call linkInDepthFirstOrder.
   **/
  public BBlock
    getNextInDFO();

  /** setNextInDFO Record the next BBlock in depth first order.
   *  @param pNext Next BBlock in depth first order.
   *  This method is called in linkInDepthFirstOrder.
   **/
  public void
    setNextInDFO(BBlock pNext);

  /** getNextInInverseDFO Get the next BBlock in inverse
   *  depth first order from exit BBlock.
   /** setNextInInverseDFO: Set the next BBlock in inverse
    *  depth first order from exit BBlock.
    *  @param pNext Next BBlock in inverse depth first order.
    *  Before calling these methods, call linkInInverseDepthFirstOrder.
    **/
   public BBlock
     getNextInInverseDFO();

  public void
    setNextInInverseDFO(BBlock pNext);

//------ Get/set loop information ------

  /** getLoopInf
   *  setLoopInf
   *  Get/set LoopInf corresponding to the loop
   *  directly containing this BBlock.
   *  If this BBlock is not contained in a loop or
   *  LoopInf is not computed, then getLoopInf returns null.
   *  @param pLoopInf LoopInf directly containing this BBlock.
   *  @return LoopInf directly containing this BBlock.
   **/
  //##78 public LoopInf getLoopInf();

  //##78 public void setLoopInf(LoopInf pLoopInf);

//------ Get/set phase-wise information ------

  /** getWork
   *  setWork
   *  get/set information privately used in each phase.
   **/
  Object getWork();

  /** setWork
   *  get/set information privately used in each phase.
   **/
  void setWork(Object pWork);

  /** getWorkFA
   *  setWorkFA
   *  get/set information privately used in flow analysis.
   **/
  public Object getWorkFA();

  /** getWorkFA
   *  setWorkFA
   *  get/set information privately used in flow analysis.
   **/
  public void setWorkFA(Object pWorkFA);

//------ Iterators and methods for traversing ------

  /** bblockSubtreeIterator
   *  Create an iterator to traverse all top-subtree in this basic block.
   *  All top-subtrees can be get by invoking next() for the iterator.
   *  The iterator skips such non-executable nodes as
   *  blockNode, listNode, stmtNode,
   *  ifNode, forNode, whileNode, untilNode, switchNode,
   *  labeledStmtNode with non-null Stmt body,
   *  and get executable statement body or expression
   *  under the skipped node.
   *  If a labeled statement has null statement body,
   *  it is not skipped.
   *  @return the BBlockSubtree iterator for this BBlock.
   **/
  public BBlockSubtreeIterator
    bblockSubtreeIterator();

  /** bblockNodeIterator
   *  Create an iterator to traverse all HIR nodes in this basic block.
   *  All nodes can be get by invoking next() for the iterator.
   *  To traverse executable node only, use nextExecutableNode()
   *  of the iterator (see BBlockNodeIterator interface).
   *  @return the node iterator for this BBlock.
   **/
  public BBlockNodeIterator
    bblockNodeIterator();

  /** getFirstSubtree
   *  Get the first subtree contained in this block.
   *  The first subtree is the one that is traversed first by
   *  bblockSubtreeIterator.
   *  @return the non-null first subtree of IR nodes contained
   *    in this block. If there is no non-null subtree,
   *    return null.
   **/
  IR
    getFirstSubtree();

  /** getLastSubtree
   *  Get the last subtree contained in this block.
   *  The last subtree is the one that is traversed last by
   *  bblockSubtreeIterator.
   *  @return the non-null last subtree of IR nodes contained
   *    in this block. If there is no non-null subtree,
   *    return null.
   **/
  IR
    getLastSubtree();

//------ Methods for printing ------

  /** toStringShort
   *  @return Basic block number and predecessor/successor list.
   **/
  public String
    toStringShort();

  /** toStringDetail
   *  @return toStringShort(), linked node, and flags.
   **/
  public String
    toStringDetail();

  /** printSubtrees
   *  Print the sequence of subtrees contained in this block.
   *  The order of print is the same as that of bblockSubtreeIterator.
   *  "this" is any basic block.
   **/
//## void
//## printSubtrees();

//======== Methods to modify CFG ========

//======== Elementary methods that change CFG ========

  /** addToPredList
   *  addToSuccList
   *  Add the basic block pPred/pSucc to the list of predecessors/successors
   *  of this basic block, and this block is added to the list of
   *  successors/predecessors of pPred/pSucc.
   *  @param pPred basic block to be added to the predecessor list
   *    of this basic block.
   *  @param pSucc basic block to be added to the successor list
   *    of this basic block.
   *  Note For method xxxPredxxx, take 1st word of predecessor/successor
   *    or the like, and for method xxxSuccxxx, take 2nd word of
   *    predecessor/successor or the like. The same rule applies to
   *    other methods in this interface.
   **/
  void addToPredList(BBlock pPred);

  void addToSuccList(BBlock pSucc);

  /** deleteFromPredList
   *  deleteFromSuccList
   *  Delete the basic block pPred/pSucc from to the list of
   *  predecessors/successors of this basic block, and this block is
   *  deleted from the list of successors/predecessors of pPred/pSucc.
   *  @param pPred basic block to be deleted from the predecessor list
   *    of this basic block.
   *  @param pSucc basic block to be deleted from the successor list
   *    of this basic block.
   **/
  void deleteFromPredList(BBlock pPred);

  void deleteFromSuccList(BBlock pSucc);

//================

// Following dependence checking methods are not to be implemented.

  /** isFlowDependent
   *  See if this block is flow dependent on pBlock (use some result
   *  produced by pBlock) or not.
   *  @param pBlock any basic block.
   *  @return true if this block is flow dependent on pBlock,
   *     false otherwise.
   **/
// boolean
// isFlowDependent( BBlock pBlock );

  /** isAntiDependent
   *  See if this block is anti-dependent on pBlock (this block sets value
   *  on a variable whose value is previously refered by pBlock) or not.
   *  @param pBlock any basic block.
   *  @return true if this block is anti-dependent on pBlock,
   *     false otherwise.
   **/
// boolean
// isAntiDependent( BBlock pBlock );

  /** isOutputDependent
   *  See if this block is output dependent on pBlock (this block sets value
   *  on a variable whose value is previously set by pBlock) or not.
   *  @param pBlock any basic block.
   *  @return true if this block is output dependent on pBlock,
   *     false otherwise.
   **/
// boolean
// isOutputDependent( BBlock pBlock );

  /** isInputDependent
   *  See if this block is input dependent on pBlock (this block refers value
   *  of a variable whose value is previously refered by pBlock) or not.
   *  @param pBlock any basic block.
   *  @return true if this block is input dependent on pBlock,
   *     false otherwise.
   **/
// boolean
// isInputDependent( BBlock pBlock );

  /** directDependenceList
   *  Get a list of basic blocks on which this block directly depends.
   *  Elements of the list can be accessed by List methods.
   *  @return the list of basic blocks on which this block
   *    directly depends.
   **/
// List
// directDependenceList();

//====== Methods to get/set flag ======

  /** getFlag
   *  setFlag
   *  getFlag returns the value (true/false) of the flag indicated
   *  by pFlagNumber.
   *  setFlag sets the flag of specified number.
   *  @param pFlagNumber flag identification number (see below).
   *  @param pYesNo true or false to be set to the flag.
   **/
  boolean getFlag(int pFlagNumber);

  void setFlag(int pFlagNumber, boolean pYesNo);

  public static final int // Flag number used in getFlag/setFlag.
    IS_RESTRUCTURED = 1, // Pred/Succ relation has been changed.
    IS_MODIFIED = 2, // IR in this BBlock has been changed.
    IS_DELETED = 3, // This basic block has been deleted.
    UNDER_VISIT = 5, // false: not yet visited.
    // true : under visit.
    VISIT_OVER = 6, // true : visit over.
    // false: visit is not over.
    IS_ENTRY = 7, // This is an entry BBlock.
    IS_EXIT = 8, // This is an exit BBlock.
    LOOP_HEAD = 9, // Loop head (loop back point) BBlock.
    LOOP_TAIL = 10, // Loop tail (going to loop back point).
    HAS_CALL = 13, // Contains subprogram call.
    HAS_PTR_ASSIGN = 14, // Contains pointed object assignment.
    USE_PTR = 15, // Use pointer (either set or reffer).
    HAS_STRUCT_UNION = 16, // Contains struct/union set/ref.
    HAS_JUMP = 17 // Ended by Jump (LIR)
    ;

//======= Methods for data flow analysis =======

  public void
//##60 allocateSpaceForDataFlowAnalysis( int pDefCount, int pExpCount);
    allocateSpaceForDataFlowAnalysis(int pPointCount, int pDefCount,
    int pExpCount); //##60

//------ See data flow information of a symbol or expression ------

  /** isDef
   *  See if definition at position pPos is done in this block.
   *  @param pPos position number attached to the definition node.
   *  @return true if the definition is done in this block,
   *    false otherwise.
   **/
  boolean
    isDef(int pPos);

  /** isKill
   *  See if definition at position pPos is killed in this block.
   *  @param pPos position number attached to the definition node.
   *  @return true if the definition at pPos is killed in this block,
   *    false otherwise.
   **/
  boolean
    isKill(int pPos);

  /** isReach
   *  See if definition at position pPos reaches to the entry point
   *  of this block.
   *  @param pPos position number attached to the definition node.
   *  @return true if the definition at pPos reaches to the entry point
   *    of this block, false otherwise.
   **/
  boolean
    isReach(int pPos);

  /** isDefined
   *  See if the value of pSym is defined in this block.
   *  @param pSym symbol representing a variable or a register
   *    or expression identifier.
   *  @return true if p(def(pSym)) is included in this block,
   *    false otherwise.
   **/
  boolean
    isDefined(Sym pSym);

  /** isUsed
   *  See if the value of pSym is used in this block.
   *  @param pSym symbol representing a variable or a register
   *    or expression identifier.
   *  @return true if p(use(pSym)) is included in this block,
   *    false otherwise.
   **/
  boolean
    isUsed(Sym pSym);

  /** isExposed
   *  See if the variable of pSym is used in this block without
   *  setting its value in this block.
   *  @param pSym symbol representing a variable or a register
   *    or expression identifier.
   *  @return true if pSym is used in this block and it is not set
   *    in this block before the use point.
   **/
  boolean
    isExposed(Sym pSym);

  /** isEGen
   *  See if expression designated by pReg is generated (computed in
   *  this block and afterwards its operand is not changed) in this block.
   *  @param pExpId expression identifier assigned to the
   *    expression.
   *  @return true if the expression is generated in this block,
   *    false otherwise.
   **/
  boolean
    isEGen(ExpId pExpId);

  /** isEKill
   *  See if expression designated by pReg is killed
   *  (its operand is defined) in this block.
   *  @param pExpId expression identifier assigned to the
   *    expression.
   *  @return true if the expression is killed in this block,
   *    false otherwise.
   **/
  boolean
    isEKill(ExpId pExpId);

  /** isAvailIn
   *  See if the expression represented by pExpId is available
   *  (value is always computed) at entry to this block.
   *  @param pExpId expression identifier
   *    representing an expression.   ()
   *  @return true if pExpId is available at entry to this block,
   *    false otherwise.
   **/
  boolean
    isAvailIn(ExpId pExpId);

  /** isAvailOut
   *  See if the expression represented by pExpId is available
   *  (value is always computed) at exit from this block.
   *  @param pExpId expression identifier
   *    representing an expression.   ()
   *  @return true if pExpId is available at exit from this block,
   *    false otherwise.
   **/
  boolean
    isAvailOut(ExpId pExpId);

  /** isLiveIn
   *  See if pSym is live at entry to this block (value at entry is
   *  used in this block or in some successor of this block).
   *  @param pSym variable or register or expression identifier
   *    representing an expression.
   *  @return true if pSym is live at entry to this block,
   *    false otherwise.
   **/
  boolean
    isLiveIn(Sym pSym);

  /** isLiveOut
   *  See if pSym is live at exit from this block (value at exit is
   *  used in some successor of this block).
   *  @param pSym variable or register or expression identifier
   *    representing an expression.
   *  @return true if pSym is live at exit from this block,
   *    false otherwise.
   **/
  boolean
    isLiveOut(Sym pSym);

  /** isDefIn
   *  See if pSym is always defined at entry to this block whichever
   *  path may be take.
   *  @param pSym variable or register or expression identifier
   *    representing an expression.
   *  @return true if pSym is always defined at entry to this block,
   *    false otherwise.
   **/
  boolean
    isDefIn(Sym pSym);

  /** isDefOut
   *  See if pSym is always defined at exit from this block.
   *  @param pSym variable or register or expression identifier
   *    representing an expression.
   *  @return true if pSym is always defined at exit from this block,
   *    false otherwise.
   **/
  boolean
    isDefOut(Sym pSym);

//------ Get/set bit vector for data flow analysis ------

  /** getDef
   *  Get bit vector showing Def set of this block.
   *  @return bit vector representing Def(B) of this block B.
   **/
  DefVector getDef();

  //##62 void setDef(DefVector pVect);

  /** getKill
   *  Get bit vector showing Kill set of this block.
   *  @return bit vector representing Kill(B) of this block B.
   **/
  DefVector getKill();

  //##62 void setKill(DefVector pVect);

  /** getReach
   *  Get bit vector showing Reach set of this block.
   *  @return bit vector representing Reach(B) of this block B.
   **/
  DefVector getReach();

  //##62 void setReach(DefVector pVect);

  /** getDefined
   *  Get bit vector showing Defined set of this block.
   *  @return bit vector representing Defined(B) of this block B.
   **/
  FlowAnalSymVector getDefined(); //##60

  //##62 void setDefined(FlowAnalSymVector pVect); //##60

  /** getUsed
   *  Get bit vector showing Used set of this block.
   *  @return bit vector representing Used(B) of this block B.
   **/
  FlowAnalSymVector getUsed(); //##60

  //##62 void setUsed(FlowAnalSymVector pVect); //##60

  /** getExposed
   *  Get bit vector showing Exposed set of this block.
   *  @return bit vector representing Exposed(B) of this block B.
   **/
  FlowAnalSymVector getExposed(); //##60

  //##62 void setExposed(FlowAnalSymVector pVect); //##60

  /** getEGen
   *  Get bit vector showing EGen set of this block.
   *  @return bit vector representing EGen(B) of this block B.
   **/
  ExpVector getEGen();

  //##62 void setEGen(ExpVector pVect);

  /** getEKill
   *  Get bit vector showing EKill set of this block.
   *  @return bit vector representing EKill(B) of this block B.
   **/
  ExpVector getEKill();

  /** getEKillAll
   *  Get bit vector showing EKillAll set of this block.
   *  EKillAll is cumulative set of expressions killed by some
   *  statements in this BBlock, that is, once an expression is
   *  killed by some staement, then it is included in EKillAll
   *  even if it is recomputed.
   *  @return bit vector representing EKillAll(B) of this block B.
   **/
  ExpVector getEKillAll();

  //##62 void setEKill(ExpVector pVect);

  /** getAvailIn
   *  Get bit vector showing AvailIn set of this block.
   *  @return bit vector representing AvailIn(B) of this block B.
   **/
  ExpVector getAvailIn();

  //##62 void setAvailIn(ExpVector pVect);

  /** getAvailOut
   *  Get bit vector showing AvailOut set of this block.
   *  @return bit vector representing AvailOut(B) of this block B.
   **/
  ExpVector getAvailOut();

  //##62 void setAvailOut(ExpVector pVect);

  /** getLiveIn
   *  Get bit vector showing LiveIn set of this block.
   *  @return bit vector representing LiveIn(B) of this block B.
   **/
  FlowAnalSymVector getLiveIn(); //##60

  //##62 void setLiveIn(FlowAnalSymVector pVect); //##60

  /** getLiveOut
   *  Get bit vector showing LiveOut set of this block.
   *  @return bit vector representing LiveOut(B) of this block B.
   **/
  FlowAnalSymVector getLiveOut(); //##60

  //##62 void setLiveOut(FlowAnalSymVector pVect); //##60

  /** getDefIn
   *  Get bit vector showing DefIn set of this block.
   *  @return bit vector representing DefIn(B) of this block B.
   **/
  FlowAnalSymVector getDefIn(); //##60

  //##62 void setDefIn(FlowAnalSymVector pVect); //##60

  /** getDefOut
   *  Get bit vector showing DefOut set of this block.
   *  @return bit vector representing DefOut(B) of this block B.
   **/
  FlowAnalSymVector getDefOut(); //##60

  //##62 void setDefOut(FlowAnalSymVector pVect); //##60

  /** getDefNodes
   *  Get the set of nodes defining a value.
   *  To get each node in the set, see java.util.Set interface.
   *  @return the set of nodes that define a value of variable, etc.
   **/
  public java.util.Set getDefNodes();

  //##62 public void setDefNodes(java.util.Set pSet);

  /** getUseNodes
   *  Get the set of nodes using value of variable or register.
   *  To get each node in the set, see java.util.Set interface.
   *  @return the set of nodes that uses value of variable, etc.
   **/
  //##60 public java.util.Set getUseNodes();

  //##60 public void setUseNodes(java.util.Set pSet);

  public SubpFlow getSubpFlow(); //##60

  public int getBBlockNumber(); //##60

  public void
    setAflowBBlock( coins.aflow.BBlock pBBlock ); //##63
  public coins.aflow.BBlock
    getAflowBBlock();        //##63

} // BBlock interface
