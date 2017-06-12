/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.flow;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.HashMap;

import coins.CompileError;
import coins.FlowRoot;
import coins.ir.IR;
import coins.ir.hir.BlockStmt;
import coins.ir.hir.Exp;
import coins.ir.hir.HIR;
import coins.ir.hir.IfStmt;
import coins.ir.hir.JumpStmt;
import coins.ir.hir.LabeledStmt;
import coins.ir.hir.LoopStmt;
import coins.ir.hir.Stmt;
//##60 import coins.ir.lir.LIR;
//##60 import coins.ir.lir.LIRNode;
//##60 import coins.ir.lir.LIRTree;
//##60 import coins.ir.lir.LIRTreeList;
//##60 import coins.ir.lir.Prologue;
//##60 import coins.ir.lir.Symbol;
import coins.sym.Const;
import coins.sym.ExpId;
import coins.sym.FlagBox;
import coins.sym.FlagBoxImpl;
import coins.sym.FlowAnalSym;
import coins.sym.Label;
import coins.sym.Subp;
import coins.sym.Sym;
import coins.sym.Var;

//      (): modified on Dec. 2001.

public abstract class
// BBlockImpl extends FlowImpl implements BBlock
  BBlockImpl
  implements BBlock
{

//========================================

//======== Fields ========

//------ Public fields ------

  public final FlowRoot
    flowRoot; // Used to access Root information.

  public final SubpFlow
    subpFlow; //##60

//------ Private/protected fields for each basic block ------

  protected int
    fBBlockNumber; // Id number of this BBlock.

  protected IR
    fIrLink; // HIR LabeledStmt or LIRTreeList
  // corresponding to this BBlock.

  protected LinkedList
    fPredList; // Predecessor list.

  protected LinkedList
    fSuccList; // Successor list.

  protected LinkedList // List of edges to predecessors.
    fPredEdgeList; // Each entry has the same index as the
  // corresponding BBlock in fPredList.

  protected LinkedList // List of edges to succeassors.
    fSuccEdgeList; // Each entry has the same index as the
  // corresponding BBlock in fSuccList.

  protected BBlock
    fImmediateDominator; // Immediate dominator;

  protected BBlock
    fImmediatePostDominator; // Immediate post dominator;

  protected LinkedList // List of BBlocks immediately dominated
    fDominatedChildren; // by this BBlock.

  protected LinkedList // List of BBlocks immediately post dominated
    fPostDominatedChildren; // by this BBlock.

  protected BBlock
    fNextInDFO; // Next BBlock in depth first order.

  protected BBlock
    fNextInInverseDFO; // Next BBlock in inverse depth first order.

  protected FlagBox
    fFlagBox; // Flag box used by getFlag/setFlag.

//##78  protected LoopInf // LoopInf directly containing this BBlock.
//##78    fLoopInf = null;

  protected Object
    fWork; // Information privately used in each phase.

  protected Object
    fWorkFA; // Information privately used for flow analysis.

  protected HashMap
    fExpNodeListMap = new HashMap(); //##60

//------ Fields for data flow analysis ------

  protected DefVector fDef;
  protected DefVector fKill;
  protected DefVector fIn;
  protected DefVector fOut;
  protected DefVector fReach;
  protected ExpVector fEGen;
  protected ExpVector fEKill;
  protected ExpVector fEKillAll; //##62
  protected ExpVector fAvailIn;
  protected ExpVector fAvailOut;
  //##60 protected ExpVector fDefined;
  //##60 protected ExpVector fExposed;
  //##60 protected ExpVector fLiveIn;
  //##60 protected ExpVector fLiveOut;
  //##60 protected ExpVector fDefIn;
  //##60 protected ExpVector fDefOut;
  //##60 protected ExpVector fUsed;
  protected FlowAnalSymVector fDefined; //##60
  protected FlowAnalSymVector fExposed; //##60
  protected FlowAnalSymVector fLiveIn; //##60
  protected FlowAnalSymVector fLiveOut; //##60
  protected FlowAnalSymVector fDefIn; //##60
  protected FlowAnalSymVector fDefOut; //##60
  protected FlowAnalSymVector fUsed; //##60
  protected java.util.Set fDefNodes;
  public final int fDbgLevel; //##60
  protected coins.aflow.BBlock aflowBBlock; //##63
  public HIR fControlTransfer; // If BBlock is terminated by
                // Jump/Retun, then record it else null.//##73
  //##60 protected java.util.Set fUseNodes;

//====== Constructors ========

  public
    BBlockImpl()
  {
    flowRoot = null;
    subpFlow = null;
    fDbgLevel = 0; //##60
  }

  /** Create new basic block corresponding to the HIR labeled statement
   *  pLabeledStmt.
   **/
  public
    BBlockImpl(FlowRoot pFlowRoot,
               int pBBlockNumber)
  {
    Label lLabel;
    flowRoot = pFlowRoot;
    subpFlow = pFlowRoot.fSubpFlow; //##60
    fBBlockNumber = pBBlockNumber;
    initiateFields();
    fFlagBox = (FlagBox)(new FlagBoxImpl());
    fDbgLevel = flowRoot.ioRoot.dbgFlow.getLevel(); //##60
    fControlTransfer = null; //##73
  } // BBlockHirImpl

  protected void
    initiateFields()
  {
    fPredList = new LinkedList();
    fSuccList = new LinkedList();
    fPredEdgeList = new LinkedList();
    fSuccEdgeList = new LinkedList();
    fFlagBox = (FlagBox)(new FlagBoxImpl());
    //## ((SubpFlowImpl)subpFlow).fArrayOfSetRefReprList[fBBlockNumber]
    //##   = new SetRefReprList(this); //##60
  } // initiateFields

//====== Interface for control flow analysis ======

//------ Methods to get attributes ------

  public int
    getBlockNumber()
  {
    return fBBlockNumber;
  }

  public IR
    getIrLink()
  {
    return fIrLink;
  }

  public void
    setIrLink(LabeledStmt pLabeledStmt)
  { // Override by BBlockHirImpl
    if (fDbgLevel > 0)
      flowRoot.ioRoot.dbgFlow.print(1, "setIrLink",
        "should be overridden by BBlockHir");
  }

  public Label
    getLabel()
  {
//##60   LIRTree lTree;
//##60   LIRNode lNode;
//##60   LIR   lIR;
    Label lLabel = null;
    Sym lSym;
    if (fIrLink != null) {
      if (flowRoot.isHirAnalysis()) {
        return ((LabeledStmt)fIrLink).getLabel();
      }
    }
    return null;
  } // getLabel

//------ Methods for control flow analysis ------

  public List
    getPredList()
  {
    return fPredList;
  }

  public List
    getSuccList()
  {
    return fSuccList;
  }

  public Edge
    getPredEdge(BBlock pFromBBlock)
  {
    int lIndex = fPredList.indexOf(pFromBBlock);
    if (lIndex >= 0)
      return (Edge)(fPredEdgeList.get(lIndex));
    else
      return null;
  } // getPredEdge

  public Edge
    getSuccEdge(BBlock pToBBlock)
  {
    int lIndex = fSuccList.indexOf(pToBBlock);
    if (lIndex >= 0)
      return (Edge)(fSuccEdgeList.get(lIndex));
    else
      return null;
  } // getSuccEdge

//------ Methods to see the characteristics of basic block ------

  /** isEntryBlock
   *  See if this block is the entry block in the graph of
   *  basic blocks.
   *  @return true if this is the entry block in the graph of basic
   *    blocks, else return false.
   **/
  public boolean
    isEntryBlock()
  {
    return fFlagBox.getFlag(IS_ENTRY);
  } // isEntryBlock

  /** isExitBlock
   *  See if this block is the exit block in the graph of
   *  basic blocks.
   *  @return true if this is the exit block in the graph of basic
   *    blocks, else return false.
   **/
  public boolean
    isExitBlock()
  {
    return fFlagBox.getFlag(IS_EXIT);
  } // isExitBlock

  /** isLoopBackEdge
   *  See if an edge is a loop back edge or not.
   *  @param pPredecessor basic block contained in a basic
   *    block graph containing this block.
   *  @return true if the edge from pPredecessor to this block
   *    is a loop back edge, otherwise return false.
   **/
  public boolean
    isLoopBackEdge(BBlock pPredecessor)
  {
    Edge lEdge;
    if (pPredecessor == null)
      return false;
    lEdge = pPredecessor.getSuccEdge(this);
    if (lEdge != null)
      return lEdge.flagBox().getFlag(Edge.LOOP_BACK_EDGE);
    else
      return false;
  } // isLoopBackEdge

//---- Methods to set/get relations between basic blocks ----

  /** getImmediateDominator
   *  Get a basic block immediately dominating this block.
   *  @return the basic block immediately dominating this block.
   **/
  public BBlock
    getImmediateDominator()
  {
    return fImmediateDominator;
  } // getImmediateDominator

  public void
    setImmediateDominator(BBlock pDominator)
  {
    fImmediateDominator = pDominator;
  } // getImmediateDominator

  /** getDominatedChildren
   *  Get the list of basic blocks immediately dominated by this
   *  block. Elements of the list can be handled by List methods.
   *  @return the list of basic blocks immediately dominated by
   *    this block.
   **/
  public List
    getDominatedChildren()
  {
    return (List)fDominatedChildren;
  } // getDominatedChildren

  public void
    setDominatedChildren(LinkedList pDominatedChildren)
  {
    fDominatedChildren = pDominatedChildren;
  } // setDominatedChildren

  /** getImmediatePostDominator
   *  Get a basic block immediately post dominating this block.
   *  @return the basic block immediately post dominating this block.
   **/
  public BBlock
    getImmediatePostDominator()
  {
    return fImmediatePostDominator;
  } // getImmediatePostDOminator

  public void
    setImmediatePostDominator(BBlock pPostDominator)
  {
    fImmediatePostDominator = pPostDominator;
  } // getImmediatePostDOminator

  /** getPostDominatedChildren
   *  Get the list of basic blocks immediately post dominated by this
   *  block. Elements of the list can be handled by List methods.
   *  @return the list of basic blocks immediately post dominated by
   *    this block.
   **/
  public List
    getPostDominatedChildren()
  {
    return (List)fPostDominatedChildren;
  } // getPostDominatorChildren

  public void
    setPostDominatedChildren(LinkedList pPostDominatedChildren)
  {
    fPostDominatedChildren = pPostDominatedChildren;
  } // setPostDominatedChildren

  public BBlock
    getNextInDFO()
  {
    return fNextInDFO;
  }

  public void
    setNextInDFO(BBlock pNext)
  {
    fNextInDFO = pNext;
  }

  public BBlock
    getNextInInverseDFO()
  {
    return fNextInInverseDFO;
  }

  public void
    setNextInInverseDFO(BBlock pNext)
  {
    fNextInInverseDFO = pNext;
  }

  public void
    linkInDepthFirstOrder(Subp pSubp)
  {
    BBlock lBBlock, lCurrentBBlock, lNextBBlock;
    ArrayList lBBlockTable;
    if (fDbgLevel > 0)
      flowRoot.flow.dbg(2, "\nlinkInDepthFirstOrder", pSubp.getName()); //##60
    //##60 lBBlockTable = pSubp.getBBlockTable();
    lBBlockTable = flowRoot.fSubpFlow.getBBlockTable(); //##60
    for (java.util.Iterator lIterator = lBBlockTable.iterator();
         lIterator.hasNext(); ) { // Reset flags.
      lBBlock = (BBlock)lIterator.next();
      if ((lBBlock == null)||(lBBlock.getBBlockNumber() == 0)) { //##60
        continue;
      }
      // flowRoot.flow.dbg(5, " B" + lBBlock.getBBlockNumber());
      lBBlock.setFlag(BBlock.UNDER_VISIT, false);
      lBBlock.setFlag(BBlock.VISIT_OVER, false);
      //##  System.out.println(" B" + lBBlock.getBlockNumber());
    }
    flowRoot.fSubpFlow.setPrevBBlockInSearch(null);
    //##60 ((BBlockImpl)pSubp.getEntryBBlock()).dfoVisit();
    ((BBlockImpl)flowRoot.fSubpFlow.getEntryBBlock()).dfoVisit();
  } // linkInDepthFirstOrder

  /** dfoVisit
   *  Called when VISIT_OVER flag is false.
   *  Set this BBlock as the next BBlock of PrevBBlockInSearch.
   *  Set UNDER_VISIT flag to true and
   *  visit successors with no VISIT_OVER flag.
   *  If all successors are visited, set VISIT_OVER flag.
   *  @param pPrev Previous BBlock from which this is visited.
   **/
  private void
    dfoVisit()
  {
    BBlock lBBlock, lPrev;
    lPrev = flowRoot.fSubpFlow.getPrevBBlockInSearch();
    if (lPrev != null) {
      lPrev.setNextInDFO(this);
      if (fDbgLevel > 0)
        flowRoot.flow.dbg(3, " B" + lPrev.getBBlockNumber()+
          "-B"+getBBlockNumber());
    }
    flowRoot.fSubpFlow.setPrevBBlockInSearch(this);
    setFlag(BBlock.UNDER_VISIT, true);
    if (getSuccList() != null) {
      for (java.util.Iterator lIterator = getSuccList().iterator();
           lIterator.hasNext(); ) {
        lBBlock = (BBlock)(lIterator.next());
        if (lBBlock.getFlag(BBlock.UNDER_VISIT) == false)
          ((BBlockImpl)lBBlock).dfoVisit();
        else { // The successor is UNDER_VISIT.
          if (lBBlock.getFlag(BBlock.VISIT_OVER) == false) {
            lBBlock.setFlag(BBlock.LOOP_HEAD, true);
            setFlag(BBlock.LOOP_TAIL, true);
            //## REFINE for loop back edge.
          }
        }
      }
    }
    setFlag(BBlock.VISIT_OVER, true);
  } // dfoVisit

//##60 BEGIN
  public void
    linkInInverseDepthFirstOrder(Subp pSubp)
  {
    BBlock lBBlock, lCurrentBBlock, lNextBBlock;
    ArrayList lBBlockTable;
    if (fDbgLevel > 0)
      flowRoot.flow.dbg(2, "\nlinkInInverseDepthFirstOrder", pSubp.getName()); //##60
    lBBlockTable = flowRoot.fSubpFlow.getBBlockTable();
    for (java.util.Iterator lIterator = lBBlockTable.iterator();
         lIterator.hasNext(); ) { // Reset flags.
      lBBlock = (BBlock)lIterator.next();
      if ((lBBlock == null)||(lBBlock.getBBlockNumber() == 0)) {
        continue;
      }
      //##60 flowRoot.flow.dbg(5, " B" + lBBlock.getBBlockNumber());
      lBBlock.setFlag(BBlock.UNDER_VISIT, false);
      lBBlock.setFlag(BBlock.VISIT_OVER, false);
    }
    flowRoot.fSubpFlow.setPrevBBlockInSearch(null);
    ((BBlockImpl)flowRoot.fSubpFlow.getExitBBlock()).inverseDfoVisit();
  } // linkInInverseDepthFirstOrder

  /** inverseDfoVisit
   *  Called when VISIT_OVER flag is false.
   *  Set this BBlock as the next BBlock of PrevBBlockInSearch.
   *  Set UNDER_VISIT flag to true and
   *  visit predeccessors with no VISIT_OVER flag.
   *  If all predeccessors are visited, set VISIT_OVER flag.
   *  @param pPrev Previous BBlock from which this is visited.
   **/
  private void
    inverseDfoVisit()
  {
    BBlock lBBlock, lPrev;
    lPrev = flowRoot.fSubpFlow.getPrevBBlockInSearch();
    if (lPrev != null) {
    if (fDbgLevel > 0)
      flowRoot.flow.dbg(3, " B" + lPrev.getBBlockNumber()+
        "-B"+getBBlockNumber());
      lPrev.setNextInInverseDFO(this);
    }
    flowRoot.fSubpFlow.setPrevBBlockInSearch(this);
    setFlag(BBlock.UNDER_VISIT, true);
    if (getPredList() != null) {
      for (java.util.Iterator lIterator = getPredList().iterator();
           lIterator.hasNext(); ) {
        lBBlock = (BBlock)(lIterator.next());
        if (lBBlock.getFlag(BBlock.UNDER_VISIT) == false)
          ((BBlockImpl)lBBlock).inverseDfoVisit();
        else { // The predeccessor is UNDER_VISIT.
        }
      }
    }
    setFlag(BBlock.VISIT_OVER, true);
  } // inverseDfoVisit
//##60 END

//------ Get/set loop information ------

/* //##78
  public LoopInf
    getLoopInf()
  {
    return fLoopInf;
  }

  public void
    setLoopInf(LoopInf pLoopInf)
  {
    fLoopInf = pLoopInf;
  }
*/ //##78

//------ Get/set phase-wise information ------

  public Object
    getWork()
  {
    return fWork;
  }

  public void
    setWork(Object pWork)
  {
    fWork = pWork;
  }

  public Object
    getWorkFA()
  {
    return fWorkFA;
  }

  public void
    setWorkFA(Object pWorkFA)
  {
    fWorkFA = pWorkFA;
  }

//------ Iterators and methods for traversing ------

  public BBlockSubtreeIterator
    bblockSubtreeIterator()
  {
    return (BBlockSubtreeIterator)
      (new BBlockHirSubtreeIteratorImpl(flowRoot, this)); //##62
  }

  public BBlockNodeIterator
    bblockNodeIterator()
  {
    return (BBlockNodeIterator)(new BBlockHirNodeIteratorImpl(flowRoot, this));
  }

//## IR
//## getFirstSubtree() { }

//## IR
//## getLastSubtree() { }

//------ Methods for printing ------

public String toString() //##60
  {
    return "BBlock" + fBBlockNumber;
  }

  public String
    toStringShort()
  {
    StringBuffer lBuffer;
    BBlock lBBlock;
    lBuffer = new StringBuffer("BBlock " + fBBlockNumber +
      " pred(");
    for (Iterator lIterator = fPredList.iterator();
         lIterator.hasNext(); ) {
      lBBlock = (BBlock)(lIterator.next());
      lBuffer.append(" " + lBBlock.getBlockNumber());
    }
    lBuffer.append(") succ( ");
    for (Iterator lIterator = fSuccList.iterator();
         lIterator.hasNext(); ) {
      lBBlock = (BBlock)(lIterator.next());
      lBuffer.append(" " + lBBlock.getBlockNumber());
    }
    lBuffer.append(")");
    return lBuffer.toString();
  } // toStringShort

  public String
    toStringDetail()
  {
    StringBuffer lBuffer;
    BBlock lBBlock;
    lBuffer = new StringBuffer();
    lBuffer = lBuffer.append(toStringShort())
      .append(getIrLink().toString());
    if (getFlag(BBlock.IS_ENTRY))
      lBuffer.append(" isEntry");
    if (getFlag(BBlock.IS_EXIT))
      lBuffer.append(" isExit");
    if (getFlag(BBlock.HAS_CALL))
      lBuffer.append(" hasCall");
    if (getFlag(BBlock.LOOP_HEAD))
      lBuffer.append(" loopHead");
    if (getFlag(BBlock.LOOP_TAIL))
      lBuffer.append(" loopTail");
    if (getFlag(BBlock.HAS_PTR_ASSIGN))
      lBuffer.append(" ptrAssign");
    if (getFlag(BBlock.USE_PTR))
      lBuffer.append(" usePtr");
    if (getFlag(BBlock.HAS_STRUCT_UNION))
      lBuffer.append(" hasStructUnion");
    return lBuffer.toString();
  } // toStringDetail

  /** printSubtrees
   *  Print the sequence of subtrees contained in this block.
   *  The order of print is the same as that of bblockSubtreeIterator.
   *  "this" is any basic block.
   **/
  public void
    printSubtrees()
  {
    // REFINE
  } // printSubtrees

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
  public void
    addToPredList(BBlock pPred)
  {
    int lIndex;
    Edge lEdge;
    List lList = getPredList();
    if (!lList.contains(pPred)) {
      lList.add(pPred);
      lIndex = lList.indexOf(pPred);
      lEdge = (Edge)(new EdgeImpl(flowRoot, pPred, this));
      fPredEdgeList.add(lIndex, lEdge);
    }
  } // addToPredList

  public void
    addToSuccList(BBlock pSucc)
  {
    int lIndex;
    Edge lEdge;
    List lList = getSuccList();
    if (!lList.contains(pSucc)) {
      lList.add(pSucc);
      lIndex = lList.indexOf(pSucc);
      lEdge = (Edge)(new EdgeImpl(flowRoot, this, pSucc));
      fSuccEdgeList.add(lIndex, lEdge);
    }
  } // addToSuccList

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
  public void
    deleteFromPredList(BBlock pPred)
  {
    int lIndex;
    List lList = getPredList();
    if (lList.contains(pPred)) {
      lIndex = lList.indexOf(pPred);
      lList.remove(pPred);
      fPredEdgeList.remove(lIndex);
    }
  } // deleteFromPredList

  public void
    deleteFromSuccList(BBlock pSucc)
  {
    int lIndex;
    List lList = getSuccList();
    if (lList.contains(pSucc)) {
      lIndex = lList.indexOf(pSucc);
      lList.remove(pSucc);
      fSuccEdgeList.remove(lIndex);
    }
  } // deleteFromSuccList

//====== Non-public methods used in modifying CFG ======

//====== Methods to get/set flag ======

  public boolean
    getFlag(int pFlagNumber)
  {
    return fFlagBox.getFlag(pFlagNumber);
  }

  public void
    setFlag(int pFlagNumber, boolean pYesNo)
  {
    fFlagBox.setFlag(pFlagNumber, pYesNo);
  }

//====== Methods for data flow analysis ======

  public void
    allocateSpaceForDataFlowAnalysis(int pPointCount, int pDefCount,
    int pExpCount)
  {
    //##60 subpFlow.setDefVectorBitCount(pDefCount);    // Moved to DataFlowImpl
    //##60 subpFlow.setPointVectorBitCount(pPointCount);// Moved to DataFlowImpl
    //##60 subpFlow.setExpVectorBitCount(pExpCount);    // Moved to DataFlowImpl
    //## REFINE Selectively allocate space according to analysis selection.
    fDef = (DefVector)(new DefVectorImpl(subpFlow));
    fKill = (DefVector)(new DefVectorImpl(subpFlow));
    fIn = (DefVector)(new DefVectorImpl(subpFlow));
    fOut = (DefVector)(new DefVectorImpl(subpFlow));
    fReach = (DefVector)(new DefVectorImpl(subpFlow));
    fDefined = (FlowAnalSymVector)(new FlowAnalSymVectorImpl(subpFlow));
    fExposed = (FlowAnalSymVector)(new FlowAnalSymVectorImpl(subpFlow));
    fEGen = (ExpVector)(new ExpVectorImpl(subpFlow));
    fEKill = (ExpVector)(new ExpVectorImpl(subpFlow));
    fEKillAll = (ExpVector)(new ExpVectorImpl(subpFlow)); //##62
    fAvailIn = (ExpVector)(new ExpVectorImpl(subpFlow));
    fAvailOut = (ExpVector)(new ExpVectorImpl(subpFlow));
    fLiveIn = (FlowAnalSymVector)(new FlowAnalSymVectorImpl(subpFlow));
    fLiveOut = (FlowAnalSymVector)(new FlowAnalSymVectorImpl(subpFlow));
    fDefIn = (FlowAnalSymVector)(new FlowAnalSymVectorImpl(subpFlow));
    fDefOut = (FlowAnalSymVector)(new FlowAnalSymVectorImpl(subpFlow));
    fUsed = (FlowAnalSymVector)(new FlowAnalSymVectorImpl(subpFlow));
    fDefNodes = new HashSet();
    //##60 fUseNodes = new HashSet();
    if (fDbgLevel > 3)
      flowRoot.ioRoot.dbgFlow.print(4, "allocateSpaceForDataFlowAnalysis",
        "B" + getBlockNumber());
  } // allocateSpaceForDataFlowAnalysis

//------ See data flow information of a symbol or expression ------

  public boolean
    isDef(int pPos)
  {
    if (! subpFlow.isComputed(subpFlow.DF_DEFUSE)){
      flowRoot.dataFlow.findDefUse();
    }
    if (fDef.getBit(pPos) == 1)
      return true;
    else
      return false;
  } // isDef

  public boolean
    isKill(int pPos)
  {
    if (! subpFlow.isComputed(subpFlow.DF_KILL)){
      flowRoot.dataFlow.findKill();
    }
    if (fKill.getBit(pPos) == 1)
      return true;
    else
      return false;
  } // isKill

  public boolean
    isReach(int pPos)
  {
    if (! subpFlow.isComputed(subpFlow.DF_REACH)){
      flowRoot.dataFlow.findReach();
    }
    if (fReach.getBit(pPos) == 1)
      return true;
    else
      return false;
  } // isReach

  public boolean
    isDefined(Sym pSym)
  {
    if (! subpFlow.isComputed(subpFlow.DF_DEFINED)){
      flowRoot.dataFlow.findDefined();
    }
    return isSymExpOn(fDefined, pSym);
  } // isDefined

  public boolean
    isUsed(Sym pSym)
  {
    if (! subpFlow.isComputed(subpFlow.DF_DEFUSE)){
      flowRoot.dataFlow.findDefUse();
    }
    return isSymExpOn(fUsed, pSym);
  } // isUsed

  public boolean
    isExposed(Sym pSym)
  {
    if (! subpFlow.isComputed(subpFlow.DF_EXPOSED)){
      flowRoot.dataFlow.findExposed();
    }
    return isSymExpOn(fExposed, pSym);
  } // isExposed

  public boolean
    isEGen(ExpId pExpId)
  {
    if (! subpFlow.isComputed(subpFlow.DF_EGEN)){
      flowRoot.dataFlow.findEGen();
    }
    int lInx = pExpId.getIndex();
    if (fEGen.getBit(lInx) == 1)
      return true;
    else
      return false;
  } // isEGen

  public boolean
    isEKill(ExpId pExpId)
  {
    if (! subpFlow.isComputed(subpFlow.DF_EKILL)){
      flowRoot.dataFlow.findEKill();
    }
    int lInx = pExpId.getIndex();
    if (fEKill.getBit(lInx) == 1)
      return true;
    else
      return false;
  } // isEKill

  public boolean
    isAvailIn(ExpId pExpId)
  {
    if (! subpFlow.isComputed(subpFlow.DF_AVAILOUT)){
      flowRoot.dataFlow.findAvailInAvailOut();
    }
    int lInx = pExpId.getIndex();
    if (fAvailIn.getBit(lInx) == 1)
      return true;
    else
      return false;
  } // isAvailIn

  public boolean
    isAvailOut(ExpId pExpId)
  {
    if (! subpFlow.isComputed(subpFlow.DF_AVAILOUT)){
      flowRoot.dataFlow.findAvailInAvailOut();
    }
    int lInx = pExpId.getIndex();
    if (fAvailOut.getBit(lInx) == 1)
      return true;
    else
      return false;
  } // isAvailOut

  public boolean
    isLiveIn(Sym pSym)
  {
    if (! subpFlow.isComputed(subpFlow.DF_LIVEIN)){
      flowRoot.dataFlow.findLiveInLiveOut();
    }
    return isSymExpOn(fLiveIn, pSym);
  } // isLiveIn

  public boolean
    isLiveOut(Sym pSym)
  {
    if (! subpFlow.isComputed(subpFlow.DF_LIVEOUT)){
      flowRoot.dataFlow.findLiveInLiveOut();
    }
    return isSymExpOn(fLiveOut, pSym);
  } // isLiveOut

  public boolean
    isDefIn(Sym pSym)
  {
    if (! subpFlow.isComputed(subpFlow.DF_DEFIN)){
      flowRoot.dataFlow.findDefInDefOut();
    }
    return isSymExpOn(fDefIn, pSym);
  } // isDefIn

  public boolean
    isDefOut(Sym pSym)
  {
    if (! subpFlow.isComputed(subpFlow.DF_DEFOUT)){
      flowRoot.dataFlow.findDefInDefOut();
    }
    return isSymExpOn(fDefOut, pSym);
  } // isDefOut

  /** isSymExpOn
   *  Return true/false according to 1/0 of pVector bit
   *  corresponding to pSym.
   *  If pSym is ExpId, its index is used as pVector bit position.
   *  If pSym is Var, Param, Elem, then ExpId corresponding to pSym
   *  is taken and its index is used as the bit position.
   **/
  protected boolean
    isSymExpOn(ExpVector pVector, Sym pSym)
  {
    boolean trueFalse = false;
    int lInx;
    ExpId lExpId;
    if (pSym != null) {
      switch (pSym.getSymKind()) {
        case Sym.KIND_EXP_ID:
          lInx = ((FlowAnalSym)pSym).getIndex();
          if (pVector.getBit(lInx) == 1)
            trueFalse = true;
          break;
        case Sym.KIND_VAR:
        case Sym.KIND_PARAM:
        case Sym.KIND_ELEM:
          //##78 lExpId = ((Var)pSym).getExpId();
          //##78 if (lExpId != null)
          //##78   lInx = lExpId.getIndex();
          //##78 else
          lInx = ((FlowAnalSym)pSym).getIndex();
          if (pVector.getBit(lInx) == 1)
            trueFalse = true;
          break;
        default:
          break;
      }
    }
    return trueFalse;
  } // isSymExpOn

//------ Get/set bit vector for data flow analysis ------

  public DefVector getDef()
  {
    if (! subpFlow.isComputed(subpFlow.DF_DEF)){
      flowRoot.dataFlow.findDef();
    }
    return fDef;
  }

  public void setDef(DefVector pVect)
  {
    fDef = pVect;
  }

  public DefVector getKill()
  {
    if (! subpFlow.isComputed(subpFlow.DF_KILL)){
      flowRoot.dataFlow.findKill();
    }
    return fKill;
  }

  public void setKill(DefVector pVect)
  {
    fKill = pVect;
  }

  public DefVector getReach()
  {
    if (! subpFlow.isComputed(subpFlow.DF_REACH)){
      flowRoot.dataFlow.findReach();
    }
    return fReach;
  }

  public void setReach(DefVector pVect)
  {
    fReach = pVect;
  }

  public FlowAnalSymVector getDefined() //##60
  {
    if (! subpFlow.isComputed(subpFlow.DF_DEFINED)){
      flowRoot.dataFlow.findDefined();
    }
    return fDefined;
  }

  public void setDefined(FlowAnalSymVector pVect) //##60
  {
    fDefined = pVect;
  }

  public FlowAnalSymVector getUsed() //##60
  {
    if (! subpFlow.isComputed(subpFlow.DF_USED)){
      flowRoot.dataFlow.findUsed();
    }
    return fUsed;
  }

  public void setUsed(FlowAnalSymVector pVect) //##60
  {
    fUsed = pVect;
  }

  //##60 public ExpVector getExposed()
  public FlowAnalSymVector getExposed() //##60
  {
    if (! subpFlow.isComputed(subpFlow.DF_EXPOSED)){
      flowRoot.dataFlow.findExposed();
    }
    return fExposed;
  }

  //##60 public void setExposed(ExpVector pVect)
  public void setExposed(FlowAnalSymVector pVect)
  {
    fExposed = pVect;
  }

  public ExpVector getEGen()
  {
    if (! subpFlow.isComputed(subpFlow.DF_EGEN)){
      flowRoot.dataFlow.findEGen();
    }
    return fEGen;
  }

  public void setEGen(ExpVector pVect)
  {
    fEGen = pVect;
  }

  public ExpVector getEKill()
  {
    if (! subpFlow.isComputed(subpFlow.DF_EKILL)){
      flowRoot.dataFlow.findEKill();
    }
    return fEKill;
  }

//##62 BEGIN
  public ExpVector getEKillAll()
  {
    // EKillAll is computed by findEKill.
    if (! subpFlow.isComputed(subpFlow.DF_EKILL)){
      flowRoot.dataFlow.findEKill();
    }
    return fEKillAll;
  }

//##62 END

  public void setEKill(ExpVector pVect)
  {
    fEKill = pVect;
  }

  public ExpVector getAvailIn()
  {
    if (! subpFlow.isComputed(subpFlow.DF_AVAILIN)){
      flowRoot.dataFlow.findAvailInAvailOut();
    }
    return fAvailIn;
  }

  public void setAvailIn(ExpVector pVect)
  {
    fAvailIn = pVect;
  }

  public ExpVector getAvailOut()
  {
    if (! subpFlow.isComputed(subpFlow.DF_AVAILOUT)){
      flowRoot.dataFlow.findAvailInAvailOut();
    }
    return fAvailOut;
  }

  public void setAvailOut(ExpVector pVect)
  {
    fAvailOut = pVect;
  }

  public FlowAnalSymVector getLiveIn() //##60
  {
    if (! subpFlow.isComputed(subpFlow.DF_LIVEIN)){
      flowRoot.dataFlow.findLiveInLiveOut();
    }
    return fLiveIn;
  }

  public void setLiveIn(FlowAnalSymVector pVect) //##60
  {
    fLiveIn = pVect;
  }

  public FlowAnalSymVector getLiveOut() //##60
  {
    if (! subpFlow.isComputed(subpFlow.DF_LIVEOUT)){
     flowRoot.dataFlow.findLiveInLiveOut();
   }
   return fLiveOut;
  }

  public void setLiveOut(FlowAnalSymVector pVect)
  {
    fLiveOut = pVect;
  }

  public FlowAnalSymVector getDefIn()
  {
    if (! subpFlow.isComputed(subpFlow.DF_DEFIN)){
      flowRoot.dataFlow.findDefInDefOut();
    }
    return fDefIn;
  }

  public void setDefIn(FlowAnalSymVector pVect)
  {
    fDefIn = pVect;
  }

  public FlowAnalSymVector getDefOut()
  {
    if (! subpFlow.isComputed(subpFlow.DF_DEFOUT)){
      flowRoot.dataFlow.findDefInDefOut();
    }
    return fDefOut;
  }

  public void setDefOut(FlowAnalSymVector pVect)
  {
    fDefOut = pVect;
  }

  public java.util.Set getDefNodes()
  {
    if (! subpFlow.isComputed(subpFlow.DF_DEFUSE)){
      flowRoot.dataFlow.findDefUse();
    }
    return fDefNodes;
  }

  public void setDefNodes(java.util.Set pSet)
  {
    fDefNodes = pSet;
  }

/* //##60
  public java.util.Set getUseNodes()
  {
    return fUseNodes;
  }

  public void setUseNodes(java.util.Set pSet)
  {
    fUseNodes = pSet;
  }
 */ //##60

//##60 BEGIN

  public SubpFlow
    getSubpFlow()
  {
    return subpFlow;
  }

  public int
    getBBlockNumber()
  {
    return fBBlockNumber;
  }

//##60 END

//##63 BEGIN
public void
setAflowBBlock( coins.aflow.BBlock pBBlock )
{
  aflowBBlock = pBBlock;
}

public coins.aflow.BBlock
getAflowBBlock()
{
  return aflowBBlock;
}

//##63 END
//##65 BEGIN
  //-- Methods of the form getXxxVector() are used only in DataFlowImpl
  //   for computing corresponding Xxx vectors.
  //
  public DefVector getReachVector()
  {
    return fReach;
  }

  public DefVector getDefVector()
  {
    return fDef;
  }

  public DefVector getKillVector()
  {
    return fKill;
  }
  public ExpVector getEGenVector()
  {
    return fEGen;
  }

  public ExpVector getEKillVector()
  {
    return fEKill;
  }

  public ExpVector getEKillAllVector()
  {
    return fEKillAll;
  }

  public ExpVector getAvailInVector()
  {
    return fAvailIn;
  }

  public ExpVector getAvailOutVector()
  {
    return fAvailOut;
  }

  public FlowAnalSymVector getDefinedVector()
  {
    return fDefined;
  }

  public FlowAnalSymVector getExposedVector()
  {
    return fExposed;
  }

  public FlowAnalSymVector getLiveInVector()
  {
    return fLiveIn;
  }

  public FlowAnalSymVector getLiveOutVector()
  {
    return fLiveOut;
  }

  public FlowAnalSymVector getDefInVector()
  {
    return fDefIn;
  }

  public FlowAnalSymVector getDefOutVector()
  {
    return fDefOut;
  }

  public FlowAnalSymVector getUsedVector()
  {
    return fUsed;
  }

  public void
  resetForDataFlowAnal()
{
  fExpNodeListMap = new HashMap();
}

//##65 END

//##73 BEGIN
  public HIR
   controlTransfer()
 {
   return fControlTransfer;
 }
 //##73 END

} // BBlockImpl class
