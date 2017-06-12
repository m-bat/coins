/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.flow;

import coins.ir.hir.SubpDefinition;
import coins.sym.Subp;

//: Modified on Jun. 2002.
//: Modified on Dec. 2001.

/** Flow interface
 <PRE>
 *  Interface for flow analysis.
 *  See also interfaces SubpFlow, BBlock, LoopInf, and BitVector.
 *
 ** Interface hierarchy concerning flow analysis ** ()
 *
 *  Flow
 *  SubpFlow
 *   |- HirSubpFlow
 *   |- LirSubpFlow
 *
 *  BBlock
 *  Edge
 *  BBlockSubtreeIterator
 *  BBlockNodeIterator
 *
 *  BitVector
 *   |- ExpVector
 *   |- PointVector
 *   |- DefVector
 *
 *  ExpVectorIterator
 *  PointVectorIterator
 *
 *  DefUseCell
 *  DefUseList
 *  FlowIrLinkCell
 *
 ** Class hierarchy concerning flow analysis **
 *
 *   Root
 *   |- FlowImpl    implements Flow
 *   |- SubpFlowImpl     implements SubpFlow
 *   |  |- HirSubpFlowImpl implements HirSubpFlow
 *   |  |- LirSubpFlowImpl implements LirSubpFlow
 *   |
 *   |- BBlockImpl   implements BBlock
 *   |- EdgeImpl   implement  Edge
 *   |
 *   |- BBlockSubtreeIteratorImpl implements BBlockSubtreeIterator
 *   |- BBlockNodeIteratorImpl  implements BBlockNodeIterator
 *   |- BitVectorImpl     implements BitVector
 *   |  |- ExpVectorImpl  implements DefVector
 *   |  |- PositionVectorImpl   implements PositionVector
 *   |    |- DefVectorImpl    implements DefVector
 *   |- BitVectorIteratorImpl   implements BitVectorIterator
 *   |   |- ExpVectorIteratorImpl    implements ExpVectorIterator
 *   |   |- PositionVectorIteratorImpl implements PositionVectorIterator
 *   |
 *   |- DefUseCellImpl   implements DefUseCell
 *   |- DefUseListImpl   implements DefUseCell
 *   |- FlowIrLinkCellImpl implements FlowIrLinkCell
 *
 ** Position of flow analysis in compiling process **  ()
 *
 *   Compiler set up;
 *   Front {
 *   Translate all subprograms in a compile unit into HIR; }
 *
 *   Prepare for HIR flow analysis {
 *   FlowRoot hirFlowRoot = new FlowRoot(hirRoot); }
 *   for each subprogram definition lSubpDef do {
 *   hirFlowRoot.controlFlow
 *   = hirFlowRoot.flow.controlFlowAnal(lSubpDef);
 *   hirFlowRoot.dataFlow
 *   = hirFlowRoot.flow.dataFlowAnal(lSubpDef);
 *   Do HIR transformations for optimization or parallelization;
 *   }
 *
 * Caution:
 *  Result of data flow may be incorrect if flow analysis of all
 *  subprogram is done and then the result of flow analysis
 *  is used, because data flow information of global variables may
 *  reflect the data for the last subprogram, not the subprogram
 *  under processing.
 *
 ** Control flow analysis and data flow analysis **  ()
 *
 *  Control flow analysis and data flow analysis are done
 *  based on the graph of basic blocks in each subprogram.
 *  Contents of a basic block can be treated as a sequence of subtrees
 *  which may be either HIR subtree or LIR subtree.
 *  Such subtree is called a "top-subtree".
 *  The structure of basic blocks is explained
 *  in the interface BBlock.
 *
 *  The flow information (either control flow or data flow) can be
 *  got by methods of SubpFlow which corresponds to each subprogram.
 *  The SubpFlow can be accessed by subp.getSubpFlow()
 *  where subp is a subprogram. (The SubpFlow computed last is active.)
 *  Detailed information for each basic block can be got by
 *  methods in BBlock.
 *  The BBlock can be got by subpFlow.getEntryBBlock() or
 *  subp.getEntryBBlock() where subpFlow is SubpFlow of some
 *  subprogram. Other BBlocks can be reached by traversing
 *  the contro flow graph (see cfgIterator of SubpFlow).
 *
 *  The control flow is based on predecessor/successor relation
 *  of basic blocks in either HIR or LIR.
 *  Elementary information of the data flow is indexed by symbol
 *  number or node number in either HIR or LIR.
 *
 *  Each basic block is assigned a source label or internal label.
 *  The first top-subtree is either LabeledStmt in HIR or
 *  DefLabel in LIR.
 *
 *  The graph of basic blocks can be gotten from subprogram symbol
 *  (using getEntryBBlock, getDefUseList, getFlowInf, etc.).
 *  Some methods for accessing flow information:
 *  getIndex()    in Sym  : get the index of a symbol .
 *  getNodeIndex()  in HIR  : get the node index of HIR or LIR.
 *  getBBlock()   in Label  : get BBlock to which this label is attached.
 *  getIndexedSym(int pSymIndex)   in SubpFlow : get Sym from sym-index.
 *  getIndexedNode(int pNodeIndex) in SubpFlow
 *      : get IR node from node index.
 *  getBBlock(int pBBlockNumber)   in SubpFlow
 *      : get BBlock from BBlock number.
 *  cfgIterator()   in SubpFlow :
 *     : used in traversing BBlock in DFO from entry BBlock.
 *  cfgFromExitIterator() in SubpFlow :
 *     : used in traversing BBlock from exit BBlock.
 *  symListIterator() in SubpFlow :
 *     : traverse symbols accessed in the subprogram.
 *  bblockSubtreeIterator in BBlock
 *     : used in traversing top-subtrees in BBlock.
 *  bblockNodeIterator in BBlock
 *     : used in traversing all IR nodes in BBlock.
 *  setPointVectorLeng(int pBitLeng) in SubpFlow
 *     : set length of bit vectors for PointVector.
 *  setExpVectorLeng(int pBitLeng) in SubpFlow
 *     : set length of bit vectors for ExpVector.
 *  isDefined(Sym pSym) in BBlock: pSym is defned in the BBlock.
 *  isUsed   (Sym pSym) in BBlock: pSym is used   in the BBlock.
 *  isLiveIn (Sym pSym) in BBlock: pSym is in LiveIn in the BBlock.
 *  getReach()  in BBlock: get bit vector showing reaching definition.
 *  ....
 *
 *  Data flow information of HIR is destroied when
 *  LIR flow analysis is performed. HIR data flow information
 *  is not valid when LIR data flow analysis is performed. ()
 *
 ** Relation between control flow and data flow:  ()
 *
 *  Methods listed in BBlock are used after flow analysis to get
 *  information or to modify control flow.
 *  (There may be many methods private to flow analysis
 *   modules other than the methods listed in BBlock.)
 *  When control flow is changed, data flow information
 *  may not be consistent because data flow information
 *  is not automatically updated by the control flow
 *  modification. In order to make them consistent,
 *  it is necessary to do data flow analysis again
 *  after a group of control flow modifications are
 *  completed.
 *
 *** Basic data flow information:
 *
 *  In the implementation of data flow analysis, each variable
 *  and each register are assigned a unique index number to identify
 *  them in data structures for flow analysis. Each program point
 *  defining or using variables and registers is assigned a unique
 *  position number to identify it quickly.
 *
 *  Each expression is assigned an expression identifier
 *  if the expression has r-value or l-value.
 *  If two expressions has the same form then
 *  the corresponding expression identifier is the same.
 *
 * Basic data flow information is as follows:
 *
 *  Notations
 *  x, y, t, u : variable or register representing an operand.
 *  op   : operator.
 *  def(x)   : shows that value of x is defined.
 *  use(x)   : shows that x is used.
 *  p(def(x))  : value of x is defined at program point p.
 *  p(use(x))  : x is used at program point p.
 *  or_all(z)  : construct a set by applying or-operation
 *   on all components indicated by z.
 *  and_all(z) : construct a set by applying and-operation
 *   on all components indicated by z.
 *
 *  Def(B)  =
 *    { p | for some x, p(def(x)) is included in B and after
 *    that point there is no def(x) in B. }
 *  Kill(B) =
 *    { p | for some x, p(def(x)) is included in B'
 *    (where, B' != B) and there exists some defining
 *    point of x p'(def(x)) in B. }
 *  Reach(B)=
 *    { p | there is some path from program point p
 *    defining x (that is p(def(x))) to the entry of B
 *    such that there is no p'(def(x)) on that path. }
 *    Reach(B) = or_all( (Def(B') | (Reach(B') - Kill(B')))
 *       for all predecessors B' of B)
 *  Defined(B) =
 *    { x | x is set in B. }
 *  Used(B) =
 *    { x | x is used in B. }
 *  Exposed(B) =
 *    { x | x is used in B and x is not set in B
 *    before x is used. }
 *  EGen(B) =
 *    { op(x,y) | expression op(x,y) is computed in B and after
 *    that point, neither x nor y are set in B. }
 *    Thus, the result of op(x,y) is available after B.
 *  EKill(B) =
 *    { op(x,y) | operand x or y is defined in B and the
 *    expression op(x,y) is not re-evaluated after
 *    that definition in B. }
 *    If t = op(x,y) is killed in B,
 *    then op(t,u) should also be killed in B.
 *  AvailIn(B) =
 *    { op(x,y) | op(x,y) is computed in every paths to B and
 *    x, y are not set after the computations
 *    on the paths. }
 *    Thus, the result of op(x,y) can be used without
 *    re-evaluation in B.
 *  AvailOut(B) =
 *    { op(x,y) | op(x,y) is computed in B and after that
 *    point, x, y are not set in B. }
 *    Thus, op(x,y) can be used without re-evaluation after B.
 *    Following relations hold.
 *    AvailIn(B) = and_all(AvailOut(B') for all predecessors
 *     B' of B) if B is not an entry block;
 *    AvailIn(B) = { } if B is an entry block.
 *    AvailOut(B) = EGen(B) | (AvailIn(B) - EKill(B))
 *  LiveIn(B) =
 *    { x | x is alive at entry to B, that is, on some path from
 *    entrance point of B to use point of x, x is not set. }
 *  LiveOut(B) =
 *    { x | x is live at exit from B, that is, there is some
 *    path from B to B' where x is in Exposed(B'). }
 *    Following relations hold.
 *    LiveOut(B) = or_all(LiveIn(B') for all successors B' of B)
 *    LiveIn(B)  = Exposed(B) | (LiveOut(B) - Defined(B))
 *  DefIn(B) =
 *    { x | x is always defined at entry to B whichever path
 *    may be taken. }
 *    DefIn(B) = and_all(DefOut(B') for all predecessors B' of B)
 *  DefOut(B) =
 *    { x | x is always defined at exit from B whichever path
 *    may be taken.}
 *    DefOut(B) = Defined(B) | DefIn(B)
 *
 * In the computation of EGen, EKill, AvailIn, AvailOut,
 * the result of alias analysis is used unconditionally.
 * In the computation of Defined and Used, the result of
 * alias analysis is not used. How to use alias information
 * such as the set of symbols that may be aliased to a variable.
 *
 * Data structure
 *
 *  There are several ways of representing data flow information
 *  such as bit vector representation and discrete list representation.
 *  When a new data flow information is introduced, it will be necessary
 *  to solve new data flow equation concerning it. For that purpose,
 *  several methods treating bit vector data structures are provided.
 *  By using these methods and methods in BitVectorInterface,
 *  we will be able to solve new data flow equation and to access the new
 *  data flow information.
 *
 *  In the bit vector representation, information can be accessed by
 *  position number of IR nodes and index number of symbols which
 *  can be get from IR node or symbol table each respectively.
 *  PosVector is a bit vector representing 0/1 information for
 *  each IR node. If its value at position p is 1, true is represented
 *  for the IR node at p, if it is 0, false is represented at that node.
 *  ExpVector is a bit vector representing 0/1 information
 *  for each symbol such as variable or register. An expression is
 *  represented by the index number assigned to an abstract register
 *  corresponding to the expression. If ExpVector value at position
 *  i is 1, true is represented for the symbol having index number i,
 *  if it is 0. false is represented for that symbol.
 </PRE>
 **/
public interface
  Flow
{

  /**====== Interface for control flow analysis ======**/

  /** controlFlowAnal
   *  Do control flow analysis of the subprogram specified by pSubpFlow, i.e.
   *  do initiation
   *    by initiateHirControlFlowAnalysis or initiateLirControlFlowAnalisis;
   *  invoke control flow analyzer (makeControlFlowGraph);
   *  show the result by showAll.
   *  @param pSubpDef definition of the subprogram to be analyzed.
   *  @return control flow information block.
   **/
  public ControlFlow
    controlFlowAnal(SubpFlow pSubpFlow);

  public void
    resetAllFlowInf(Subp pSubp);

//====== Methods to create objects for data flow analysis ======

  /** dataFlowAnal // REFINE comment.
   *  Do data flow analysis of the subprogram defined by pSubpDef.
   *  It is expected that the control flow analysis has already been done.
   *  If it is not yet done, initiateHirControlFlowAnayisis or
   *  initiateLirControlFlowAnayisis is invoked before the actual
   *  data flow analysis takes place.
   *  Thus, before the actual data flow analysis, the subprogram is
   *  already devided into basic blocks, every executable nodes are
   *  indexed, and symbols actually used in the subprogram are recorded.
   *  @param pSubpDefinition definition of the subprogram to be analyzed.
   *  @return DataFlow information showing the result of analysis.
   **/
  public DataFlow
    dataFlowAnal(SubpDefinition pSubpDef);

  public SubpFlow
    getSubpFlow();

  public DataFlow
    dataFlow();

  public DataFlow
    dataFlowAnal();

  public int
    getFlowAnalStateLevel();

  public void
    setFlowAnalStateLevel(int pStateLevel);

   /** getSubpFlow
    *  Get currently effective SubpFlow information.
    **/
  public Subp
    getSubpUnderAnalysis();

  public ControlFlow
    controlFlow();

  /**
   * Do control flow analysis and data flow analysis.
   * This may be invoked from Driver by flow analysis option in command.
   */
  public void
   doHir();

//##60 BEGIN
  public void dbg(int level, String pHeader, Object pObject);

  public void dbg(int level, Object pObject);

//##60 END

//===== Constants ======

  public static final int
    STATE_DATA_UNAVAILABLE = 0,
    STATE_CFG_RESTRUCTURING = 1,
    STATE_CFG_AVAILABLE = 2,
    STATE_DATA_FLOW_AVAILABLE = 3,
    STATE_HIR_FLOW_AVAILABLE = 4,
    STATE_LIR_FLOW_AVAILABLE = 5;

} // Flow interface
