/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.flow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import coins.FlowRoot;
import coins.HirRoot;
import coins.IoRoot;
import coins.SymRoot;
import coins.alias.RecordAlias; //##60
import coins.ir.IR;
import coins.ir.hir.AsmStmt; //##70
import coins.ir.hir.AssignStmt; //##65
import coins.ir.hir.HIR;
import coins.ir.hir.HirIterator; //##65
import coins.ir.hir.HirList; //##70
import coins.ir.hir.HirSeq; //##70
import coins.sym.ExpId;
import coins.sym.FlowAnalSym;
import coins.sym.PointerType; //##65
//##60 import coins.sym.OperandSym;
import coins.sym.Sym;
import coins.sym.Type; //##63
import coins.sym.Var; //##63
import coins.sym.VectorType; //##65

//   Modified on Jan. 2002 by Tan
//     to prepare for LIR data flow analysis.
// DataFlowLirImpl is added as a subclass of DataFlowImpl
// so that to implement data flow analysis for LIR.
// "private" is changed to "protected" to allow access
// from the subclass DataFlowLirImpl.
//   Modified on Dec. 2001. by Tan
/**
 * Implementation of the DataFlow interface.
 *
 * There are some dependencies between the methods in this class.
 * For example, findReach() mustn't be called before both findDef() and findKill() have been called,
 * and findKill() in turn depends on the result of findDefined().
 * <b>Access restrictions (or the like) that reflect these dependencies are not yet implemented.</b> Please be careful in using.
 * {@link #findAll()} method calls these methods in the correct order.
 */
public class DataFlowImpl
  implements DataFlow
{
  public final FlowRoot
    flowRoot; // Used to access Root information.

  public final IoRoot
    ioRoot; // Used to access Root information.

  public final SymRoot
    symRoot; // Used to access Root information.

  public final HirRoot
    hirRoot; // Used to access Root information.

  public final Flow
    flow; // Used to invoke Flow methods.

  public ShowDataFlowByName
    showDataFlowByName;

  protected SubpFlow fSubpFlow; // SubpFlow instance to analyze.
  protected ShowDataFlow fShowDataFlow; // ShowDataFlow instance used for
  // printing "this" DataFlow.
  protected int fDefCount = 0; // Number of value-setting statements.
  //##63 protected List fBBlockList = new ArrayList();
  //##63 protected List fBBlockList = null; //##63
  // List of BBlocks relevant
  // for DFA BBlocks that are never visited may not be added in recordBBlocks.
  //##62 protected SetRefRepr fSetRefReprTable[]; // Only used for debugging so far.
  // Shows SetRefRepr instance for each node.
  // Instance to use static-like methods in the SetRefRepr class.
  //##60 protected SetRefRepr setRefRepr=new SetRefReprImpl();
  protected DefVector DEF_ZERO; // 0 DefVector.
  protected DefVector DEF_INVERTED; // "Full" DefVector (all the bits are 1)
  // for caching purposes.
  protected ExpVector EXP_ZERO; // 0 ExpVector
  protected ExpVector EXP_INVERTED; // "Full" ExpVector (all the bits are 1)
  // for caching purposes.
  //##62 protected int fDefSetRefReprNoTable[]; // Show the def index for node index.
  //##62 protected int fNodeIndexTable[]; // Show node index for def index.
  protected int fDefNodeIndexTable[]; // Show node index for def-index. //##62
  // def-index is incremented by AssignStmt. //##62
  // protected ExpId fExpIdTable[];
  protected FlowAnalSym fFlowAnalSymTable[]; // Show flow anal symbol
  // from symbol index (FlowAnalSym.getIndex())
  // fFlowAnalSymTable, getFlowAnalSym(), recordExpId() may be unnecessary
  // if fSubpFlow.getIndexedSym(pIndex) is used. //##62
  //protected List fDefNodeListOfSym[]; // List of Def nodes for each FAS. //##60
  // Indexed by FlowAnalSym.getIndex().
  protected java.util.Set fUndefinedUseNodesOfSym[]; // Set of Use nodes //##60
  //for each FAS that has no corresponding Def node.
  // Indexed by FlowAnalSym.getIndex().
  protected RecordAlias fRecordAlias; //##62
  // fRecordAlias is set by findEkill() and findEKill(). //##62
  public final int
    fDbgFlow; //##60

  /**
   * Constructs a DataFlow instance and prepares for DFA.
   */
  public DataFlowImpl(FlowRoot pFlowRoot, SubpFlow pSubpFlow)
  {
    flowRoot = pFlowRoot;
    ioRoot = pFlowRoot.ioRoot;
    symRoot = pFlowRoot.symRoot;
    hirRoot = pFlowRoot.hirRoot;
    flow = pFlowRoot.flow;
    fSubpFlow = pSubpFlow;
    flowRoot.dataFlow = this; //##62
    fDbgFlow = ioRoot.dbgFlow.getLevel();
  } // DataFlowImpl

  public
    DataFlowImpl()
  {
    flowRoot = null;
    ioRoot = null;
    symRoot = null;
    hirRoot = null;
    flow = null;
    fDbgFlow = 0; //##60
  }

  protected void // Part of constructor common between HIR and LIR.
    initiateDataFlow()
  {
    int lTreeCount, lNodeCount;
    if (fDbgFlow > 0) {
      flowRoot.ioRoot.dbgFlow.print(2, "DataFlowImpl.initiateDataFlow",
        "state "
        + flow.getFlowAnalStateLevel());
    }
    if (flow.getFlowAnalStateLevel() < flow.STATE_CFG_AVAILABLE) {
      throw new RuntimeException("CFG Must Be Constructed Before DFA.");
    }
    clean();
    lNodeCount = fSubpFlow.getNumberOfNodes();
    if (fDbgFlow > 0) {
      flowRoot.ioRoot.dbgFlow.print(1, "\n Node count " +
        lNodeCount + " SymExpCount " + fSubpFlow.getSymExpCount()
        + " AssignCount " + fSubpFlow.getAssignCount()
        + " CallCount " + fSubpFlow.getCallCount()); //##62
    }
    if (flowRoot.isHirAnalysis()) {
      //##62 fSetRefReprTable = new SetRefRepr[lNodeCount + 1];
      //##62 fDefSetRefReprNoTable = new int[lNodeCount + 1];
    }
    //##63 ((SubpFlowImpl)fSubpFlow).allocateDefUseListOfSym(fSubpFlow.getSymExpCount() +1); //##60
    // fDefNodeListOfSym = new LinkedList[fSubpFlow.getSymExpCount()+1]; //##60
    fUndefinedUseNodesOfSym = new HashSet[fSubpFlow.getSymExpCount() + 1]; //##60
    // fDefSetRefReprNoTable=new int[lNodeCount+1];
    //##62 fNodeIndexTable = new int[lNodeCount + 2]; // The size is bigger than actually needed.
    fDefNodeIndexTable = new int[fSubpFlow.getAssignCount()
      + fSubpFlow.getCallCount() + 1]; //##62
    // fExpIdTable=new ExpId[fSubpFlow.getSymExpCount()+1];
    //##65 if (flowRoot.isHirAnalysis()) { //##62
    //##65   recordExpId(); //##62
      // Makes the table that retrieves ExpId from its index. //##60
      // Won't be needed if ExpVectorIterator's nextExpId works.
      // c.f. #toSet().
      //##60 fFlowAnalSymTable = new FlowAnalSym[fSubpFlow.getSymExpCount() + 2];
      // Should be allocated after ExpId allocation
    //##65 }
    // Register BBlocks in a List
    // so that the Iterator methods can be used.
    // The order registered in the List is determined by the BBlock index,
    // which should make solving data flow eqs. faster.
    // (Otherwise the order is irrelevant.)
    // BBlocks are already recorded when BBlock is created
    // (see SubpFlowImpl.bblock(....). //##65
    //##65 recordBBlocks();

    recordSetRefReprs(); // Records the SetRefReprs in BBlocks to a List,
    // which is reused several times.
    fShowDataFlow = new ShowDataFlow(this);
    showDataFlowByName = new ShowDataFlowByName(this);
    fShowDataFlow.setShowDataFlowByName(showDataFlowByName); //##60
    allocateSpace(); // Allocates space for BitVectors in BBlocks.
    //##62 if (flowRoot.isHirAnalysis())
    //##60 allocateExpId(); // Makes the table that retrieves ExpId from
    // its index. Won't be needed if ExpVectorIterator's nextExpId works.
    // c.f. #toSet().
    //##62   recordExpId(); // Makes the table that retrieves ExpId from its index. //##60
    // Won't be needed if ExpVectorIterator's nextExpId works.
    // c.f. #toSet().
    DEF_ZERO = fSubpFlow.defVector();
    DEF_INVERTED = fSubpFlow.defVector();
    DEF_ZERO.vectorNot(DEF_INVERTED); // Creates a "full" DefVector for caching purposes.

    EXP_ZERO = fSubpFlow.expVector();
    EXP_INVERTED = fSubpFlow.expVector();
    EXP_ZERO.vectorNot(EXP_INVERTED); // Creates a "full" ExpVector for caching purposes.
  } // initiateDataFlow

  /**
   * Records the BBlocks in the flow in the order suitable for
     * solving data flow equations. BBlocks that are never visited are not included.
   **/
  /**
   * Records the SetRefReprs each BBlock contains.
   * This is moved to HirSubpFlowImpl. //##63
   */
  void recordSetRefReprs()
  {
    ((HirSubpFlowImpl)fSubpFlow).recordSetRefReprs(); //##63
    fDefCount = fSubpFlow.getDefCount(); //##63
  } // recordSetRefReprs

  /**
   * Returns the List of BBlocks in the flow.
   * (Exclude null and 0-numbered BBlock)
   * @return the List of BBlocks in the flow.
   **/
  public List getBBlockList()
  {
    return fSubpFlow.getBBlockList(); //##63
  }

  /**
   *
   * allocateExpId
   *
   * Creates a table that retrieves ExpId from its index value.
   **/
  //##60 protected void allocateExpId()
  protected void recordExpId()
  {
  } // recordExpId

  /**
   *
   * getDefCount
   *
   **/
  public int getDefCount()
  {
    return fDefCount;
  }

  /**
   *
   * // getExpIdCount
   * getFlowAnalSymCount
   *
   **/
  // public int getExpIdCount()
  public int getFlowAnalSymCount()
  {
    return fSubpFlow.getSymExpCount();
  }

  /**
   *
   * getPointCount
   *
   **/
  public int getPointCount()
  {
    return fSubpFlow.getNumberOfNodes();
  }

  /**
   *
   * getExpId
   *
   **/
  public FlowAnalSym
    getFlowAnalSym(int ExpIndex)
  {
    if (fDbgFlow > 0) {
      ioRoot.dbgFlow.print(1, "getFlowAnalSyms",
        "should use subclass method");
    }
    return null;
  }

  /**
   *
   * getDefIndex
   *
   **/
  public int getDefIndex(int NodeIndex)
  {
    //##62 return fDefSetRefReprNoTable[NodeIndex - fSubpFlow.getIrIndexMin()]; //##60
    return fSubpFlow.getDefIndex(NodeIndex - fSubpFlow.getIrIndexMin()); //##60
  }

  /**
   *
   * getNodeIndex
   *
   **/
  //##62 public int getNodeIndex(int pDefSetRefReprNo)
  public int getDefNodeIndex(int pDefSetRefReprNo)
  {
    //##62 return fNodeIndexTable[pDefSetRefReprNo];
    return fDefNodeIndexTable[pDefSetRefReprNo]; //##62
  }

  /**
   *
   * getNode(int)
   *
   * Returns the node that has the given index.
   *
   */
  public
    IR getNode(int pNodeIndex)
  {
    if (pNodeIndex == 0) {
      return null;
    }
    return fSubpFlow.getIndexedNode(pNodeIndex);
  }

  /**
   * Returns the IR node that corresponds to the given DefSetRefRepr index
   *      (entry of the DefVector).
   *
   * @return the IR node that corresponds to the given DefSetRefRepr index
   *      (entry of the DefVector).
   */
  public IR getNodeFromDefIndex(int pDefIndex)
  {
    //##62 return fSubpFlow.getIndexedNode(getNodeIndex(pDefIndex));
    return fSubpFlow.getIndexedNode(getDefNodeIndex(pDefIndex)); //##62
  }

  /**
   *
   * allocateSpace
   *
   * Allocates space to store BitVector instances for the entire flow.
   *
   **/
  protected void allocateSpace()
  {
    BBlock b;
    IR s;
    Iterator Ie;
    BBlockSubtreeIterator Is;

    // for(Ie =getBBlockList().iterator();Ie.hasNext();) {
    //   b = (BBlock)Ie.next();
    //##60 BEGIN
    int lDefBitCount;
    int lPointBitCount, lExpBitCount;
    if (flowRoot.isHirAnalysis()) {
      lPointBitCount = fSubpFlow.getIrIndexMax() -
        fSubpFlow.getIrIndexMin() + 2;
      lDefBitCount = lPointBitCount;
      fSubpFlow.setDefVectorBitCount(lDefBitCount);
      lExpBitCount = getFlowAnalSymCount() + 1;
      fSubpFlow.setPointVectorBitCount(lPointBitCount);
      fSubpFlow.setExpVectorBitCount(lExpBitCount);
      if (fDbgFlow > 3) {
        flowRoot.ioRoot.dbgFlow.print(4, "allocateSpace",
          " pointBitCount " + lPointBitCount + " defBitCount "
          + lDefBitCount + " fDefCount " + fDefCount +
          " expBitCount " + lExpBitCount);
      }
      for (int i = 1; i <= fSubpFlow.getNumberOfBBlocks(); i++) {
        b = fSubpFlow.getBBlock(i);
        // Some basic blocks (such as dummy BBlock) may not be
        // included in BBlockList. (See recordBBlocks())
        b.allocateSpaceForDataFlowAnalysis(lPointBitCount,
          lDefBitCount, lExpBitCount);
      }
    }
    else {
      fSubpFlow.setDefVectorBitCount(fDefCount);
      fSubpFlow.setPointVectorBitCount(fSubpFlow.getPointVectorBitCount());
      // Should be LIR SET count
      fSubpFlow.setExpVectorBitCount(fSubpFlow.getUsedSymCount());
      for (int i = 1; i <= fSubpFlow.getNumberOfBBlocks(); i++) {
        b = fSubpFlow.getBBlock(i);
        // Some basic blocks (such as dummy BBlock) may not be
        // included in BBlockList. (See recordBBlocks())
        b.allocateSpaceForDataFlowAnalysis(fSubpFlow.getPointVectorBitCount(),
          fDefCount, fSubpFlow.getUsedSymCount()); //##60
      }
    }
    //##60 END
  } // allocateSpace

  /**
   * Finds and sets the Def vectors for the entire flow.
   */
  public void findDef()
  {
    BBlock lBBlock;
    if (fDbgFlow > 3) {
      flowRoot.ioRoot.dbgFlow.print(5, "findDef ");
    }
    if (fSubpFlow.isComputedOrUnderComputation(fSubpFlow.DF_DEF)) {
      flow.dbg(5, " Use computed one.");
      return;
    }
    fSubpFlow.setUnderComputation(fSubpFlow.DF_DEF); //##62
    for (Iterator lCFGIterator = getBBlockList().iterator();
         lCFGIterator.hasNext(); ) { // For each BBlock,
      lBBlock = (BBlock)lCFGIterator.next();
      findDef(lBBlock); // Find the Def vector.
    }
    fSubpFlow.setComputedFlag(fSubpFlow.DF_DEF); //##60
  } // findDef

  /**
   * Finds and sets the Def vector for the given BBlock.
   *
   * @param pBBlock BBlock whose Def vector to find.
   */
  public void findDef(BBlock pBBlock)
  {
    if (fDbgFlow > 3) {
      ioRoot.dbgFlow.print(5, "findDef", "B" + pBBlock.getBlockNumber());
      //##63 List lBBlockSetRefReprList = (List)pBBlock.getWorkFA();
    }
    SetRefReprList lBBlockSetRefReprList = fSubpFlow.getSetRefReprList(pBBlock); //##63
    SetRefRepr lSetRefRepr;
    // ExpId lDef;
    FlowAnalSym lDef;
    java.util.Set lBBlockDefSet = new HashSet(); // Set that incrementally
    // stores the variables defined in the BBlock and is referred
    // to determine if the current SetRefRepr qualifies as an element of Def.
    int lDefBitPosition;

    for (ListIterator lBBlockSetRefReprIterator = lBBlockSetRefReprList.
         listIterator(lBBlockSetRefReprList.size());
         lBBlockSetRefReprIterator.hasPrevious(); ) {
      // Iterate from the bottom of the BBlock for SetRefReprs.
      lSetRefRepr = (SetRefRepr)lBBlockSetRefReprIterator.previous();
      if (fDbgFlow > 3) {
        ioRoot.dbgFlow.printObject(6, " SetRefRepr", lSetRefRepr);
        //if ((lDef=lSetRefRepr.getDefExpId())!=null)
        //##60 if ((lDef=lSetRefRepr.getDefFlowAnalSym())!=null)
      }
      if ((lDef = lSetRefRepr.getDefSym()) != null) { //##60
        if (fDbgFlow > 3) {
          ioRoot.dbgFlow.printObject(6, " def ", lDef);
        }
        if (lDef instanceof ExpId) { //##63
          if (!((SubpFlowImpl)fSubpFlow).getMaximalCompoundVars().contains(lDef)) { //##65
            continue; //##63
          }
        }
        //##70 if (lBBlockDefSet.add(lDef))
          // If the registration succeeded,
          // i.e., if lBBlockDefSet didn't formerly contain lDef ,
        lBBlockDefSet.add(lDef); //##70
        //##70 {
          //##60 lDefBitPosition=defLookup(lSetRefRepr.getNode().getIndex());
          lDefBitPosition = defLookup(lSetRefRepr.getIR().getIndex()); //##60
          if (fDbgFlow > 3) {
            ioRoot.dbgFlow.print(6, " defPosition " + lDefBitPosition);
          }
          //##65 pBBlock.getDef().setBit(lDefBitPosition); // Set the Def vector's bit.
          ((BBlockImpl)pBBlock).getDefVector().setBit(lDefBitPosition); // Set the Def vector's bit.
          // Registers to a DefUseCell's DefNode.
          //##63 fSubpFlow.getDefUseList(lDef).addDefUseChain(lSetRefRepr.defNode()); //##63
          //##73 fSubpFlow.getDefUseList().addDefUseChain(lSetRefRepr.getIR()); //##63
          ((SubpFlowImpl)fSubpFlow).getListOfDefUseList().addDefUseChain(lSetRefRepr.getIR()); //##63
        //##70 }
      }
    }
    if (fDbgFlow > 3)
      flowRoot.flow.dbg(6, "findDef B"+ pBBlock.getBBlockNumber(),
        ((BBlockImpl)pBBlock).getDefVector().toStringDescriptive()); //##65
  } // findDef BBlock

  /**
   * Finds and sets the Kill vectors for the entire flow.
   *
   * See #findDefined()
   */
  public void findKill()
  {
    if (fDbgFlow > 3) {
      flowRoot.ioRoot.dbgFlow.print(5, "findKill ");
    }
    if (fSubpFlow.isComputedOrUnderComputation(fSubpFlow.DF_KILL)) {
      flow.dbg(5, " Use computed one.");
      return;
    }
    fSubpFlow.setUnderComputation(fSubpFlow.DF_KILL); //##62
    BBlock lBBlock;
    for (Iterator lCFGIterator = getBBlockList().iterator();
         lCFGIterator.hasNext(); ) { // For each BBlock
      lBBlock = (BBlock)lCFGIterator.next();
      findKill(lBBlock); // Find the Kill vector.
    }
    fSubpFlow.setComputedFlag(fSubpFlow.DF_KILL); //##60
  } // findKill

  /**
   * Finds and sets the Kill vector for the given BBlock using
   *  the Defined vector for that BBlock.
   *
   * @param pBBlock BBlock whose Kill vector to find.
   * See #findDefined()
   */
  public void findKill(BBlock pBBlock)
  {
    if (fDbgFlow > 3) {
      ioRoot.dbgFlow.print(5, "findKill", "B" + pBBlock.getBlockNumber());
    }
    BBlock lBBlock; // BBlock that contains the SetRefRepr "killed"
    // by the BBlock under consideration (pBBlock)
    //##63 List lBBlockSetRefReprList; // List of SetRefReprs in lBBlock.
    SetRefReprList lBBlockSetRefReprList; //##63
    SetRefRepr lSetRefRepr;
    // ExpId lDef;
    FlowAnalSym lDef;
    int lDefBitPosition;

    for (Iterator lCFGIterator = getBBlockList().iterator();
         lCFGIterator.hasNext(); ) { // For each BBlock lBBlock,
      lBBlock = (BBlock)lCFGIterator.next();
      if (pBBlock != lBBlock) { // If lBBlock is different from pBBlock,
        //##63 lBBlockSetRefReprList = (List)lBBlock.getWorkFA();
        //##65 lBBlockSetRefReprList = fSubpFlow.getSetRefReprList(pBBlock); //##63
        lBBlockSetRefReprList = fSubpFlow.getSetRefReprList(lBBlock); //##65
        for (Iterator lBBlockSetRefReprIterator = lBBlockSetRefReprList.
             iterator(); lBBlockSetRefReprIterator.hasNext(); ) {
          // Iterate through lBBlock for SetRefReprs.
          lSetRefRepr = (SetRefRepr)lBBlockSetRefReprIterator.next();
          // if ((lDef=lSetRefRepr.getDefExpId())!=null)
          //     If the SetRefRepr is value-setting (AssignStmt in HIR),
          //##60 if ((lDef=lSetRefRepr.getDefFlowAnalSym())!=null) {
          if ((lDef = lSetRefRepr.getDefSym()) != null) { //##60
            // If the SetRefRepr is value-setting (AssignStmt in HIR),
            if (pBBlock.isDefined((Sym)lDef)) { // If the variable whose value
              // is to be set in the SetRefRepr is defined in pBBlock,
              //##60 lDefBitPosition=defLookup(lSetRefRepr.getNode().getIndex());
              lDefBitPosition = defLookup(lSetRefRepr.getIR().getIndex()); //##60
              // Set the Kill vector's bit.
              //##65 pBBlock.getKill().setBit(lDefBitPosition);
              ((BBlockImpl)pBBlock).getKillVector().setBit(lDefBitPosition); //##65
            }
          }
        }
      //## 70 BEGIN
      }else { // lBBlock == pBBlock
        // Kill previous definition if its operand is defined.
        SetRefReprList lBBlockSetRefReprList2;
        SetRefRepr lSetRefRepr2;
        FlowAnalSym lDef2;
        HIR lPreviousDef;
        int lDefBitPosition2;
        HashMap lVarToDefPosition = new HashMap();
        lBBlockSetRefReprList2 = fSubpFlow.getSetRefReprList(lBBlock); //##65
        for (Iterator lBBlockSetRefReprIterator2 = lBBlockSetRefReprList2.
             iterator(); lBBlockSetRefReprIterator2.hasNext(); ) {
          // Iterate through lBBlock for SetRefReprs.
          lSetRefRepr2 = (SetRefRepr)lBBlockSetRefReprIterator2.next();
          lDef2 = lSetRefRepr2.getDefSym();
          if (lDef2 != null) {
            // If lSetRefRepr2 is value-setting (AssignStmt in HIR).
            if (lVarToDefPosition.containsKey(lDef2)) {
              lPreviousDef = (HIR)lVarToDefPosition.get(lDef2);
              // Kill the previous definition.
              lDefBitPosition2 = defLookup(lPreviousDef.getIndex());
              ((BBlockImpl)pBBlock).getKillVector().setBit(lDefBitPosition2); //##65
              if (fDbgFlow > 3)
                flowRoot.flow.dbg(6, "Kill previous def " + lDef2.getName()
                   + " at " + lPreviousDef.toStringShort());
            }
            lVarToDefPosition.put(lDef2, lSetRefRepr2.getIR());
          }
        }

      //##70 END
      }
    }
    if (fDbgFlow > 3)
      flowRoot.flow.dbg(6, "findKill B"+ pBBlock.getBBlockNumber(),
        ((BBlockImpl)pBBlock).getKillVector().toStringDescriptive()); //##65
  } // findKill BBlock

  /**
   * Finds and sets the Defined vectors for the entire flow.
   */
  public void findDefined()
  {
    if (fDbgFlow > 3) {
      flowRoot.ioRoot.dbgFlow.print(5, "findDefined ");
    }
    if (fSubpFlow.isComputedOrUnderComputation(fSubpFlow.DF_DEFINED)) {
      flow.dbg(5, " Use computed one.");
      return;
    }
    fSubpFlow.setUnderComputation(fSubpFlow.DF_DEFINED); //##62
    BBlock lBBlock;
    for (Iterator lCFGIterator = getBBlockList().iterator();
         lCFGIterator.hasNext(); ) { // For each BBlock,
      lBBlock = (BBlock)lCFGIterator.next();
      findDefined(lBBlock); // Find the Defined vector.
    }
    fSubpFlow.setComputedFlag(fSubpFlow.DF_DEFINED); //##60
  } // findDefined

  /**
   * Finds and sets the Defined vector for the given BBlock.
   *
   * @param pBBlock BBlock whose Defined vector to find.
   */
  public void findDefined(BBlock pBBlock)
  {
    //##63 List lBBlockSetRefReprList = (List)pBBlock.getWorkFA();
    SetRefReprList lBBlockSetRefReprList = fSubpFlow.getSetRefReprList(pBBlock); //##63
    SetRefRepr lSetRefRepr;
    // ExpId lDef;
    FlowAnalSym lDef;
    int lExpBitPosition;

    if (fDbgFlow > 3) {
      ioRoot.dbgFlow.print(5, "findDefined B", " " + pBBlock.getBlockNumber());
    }
    for (Iterator lBBlockSetRefReprIterator = lBBlockSetRefReprList.iterator();
         lBBlockSetRefReprIterator.hasNext(); ) {
      // Iterate through the BBlock for SetRefRepr.
      lSetRefRepr = (SetRefRepr)lBBlockSetRefReprIterator.next();
      HIR lIrSubtree = (HIR)lSetRefRepr.getIR(); //##65
      if (fDbgFlow > 4) {
        //##60 if (lSetRefRepr.getNode() != null)
        ioRoot.dbgFlow.print(5,
            " " + ioRoot.toStringObjectShort(lIrSubtree)); //##65
      }
      // if ((lDef=lSetRefRepr.getDefExpId())!=null)
      //   If the SetRefRepr is value-setting (AssignStmt in HIR),
      //##60 if ((lDef=lSetRefRepr.getDefFlowAnalSym())!=null) {
      lDef = lSetRefRepr.getDefSym(); //##73
      if (lDef  != null) { //##73
        // If the SetRefRepr is value-setting (AssignStmt in HIR),
        if (fDbgFlow > 3)
          ioRoot.dbgFlow.print(5, " def " + lDef );
        //##73 BEGIN
        if (lDef instanceof Var) {
          fSubpFlow.getDefinedSyms().add(lDef);
        }else
        //##73 END
        //##65 BEGIN
        if ((lDef instanceof ExpId)&&
            ((ExpId)lDef).isLHS()) {
          lDef = ((ExpId)lDef).getExpInf().getRValueExpId();
          if (fDbgFlow > 3)
            ioRoot.dbgFlow.print(5, " r-value " + lDef.getName());
        }
        //##65 END
        lExpBitPosition = expLookup(lDef.getIndex());
        // Set the Defined vector's bit.
        //##65 pBBlock.getDefined().setBit(lExpBitPosition);
        ((BBlockImpl)pBBlock).getDefinedVector().setBit(lExpBitPosition); //##65
        //##65 fFlowAnalSymTable[lDef.getIndex()] = lDef;
        //##65 fSubpFlow.getDefinedSyms().add(lDef); //##63
      }
    }
    if (fDbgFlow > 3)
      flowRoot.flow.dbg(6, "findDefined B"+ pBBlock.getBBlockNumber(),
        ((BBlockImpl)pBBlock).getDefinedVector().toStringDescriptive()); //##65
  } // findDefined BBlock

//##70 BEGIN
  /**
   * Finds and sets the Used vectors for the entire flow.
   */
  public void findUsed()
  {
    if (fDbgFlow > 3) {
      flowRoot.ioRoot.dbgFlow.print(5, "findUsed ");
    }
    if (fSubpFlow.isComputedOrUnderComputation(fSubpFlow.DF_USED)) {
      flow.dbg(5, " Use computed one.");
      return;
    }
    fSubpFlow.setUnderComputation(fSubpFlow.DF_USED);
    BBlock lBBlock;
    for (Iterator lCFGIterator = getBBlockList().iterator();
         lCFGIterator.hasNext(); ) { // For each BBlock,
      lBBlock = (BBlock)lCFGIterator.next();
      findUsed(lBBlock); // Find theUsed vector.
    }
    fSubpFlow.setComputedFlag(fSubpFlow.DF_USED);
  } // findUsed

  /**
   * Finds and sets the Used vector for the given BBlock.
   *
   * @param pBBlock BBlock whose Used vector to find.
   */
  public void findUsed(BBlock pBBlock)
  {
    SetRefReprList lBBlockSetRefReprList = fSubpFlow.getSetRefReprList(pBBlock);
    SetRefRepr lSetRefRepr;
    FlowAnalSym lUse;
    int lExpBitPosition;

    if (fDbgFlow > 3) {
      ioRoot.dbgFlow.print(5, "\nfindUsed B" + pBBlock.getBlockNumber());
    }
    for (Iterator lBBlockSetRefReprIterator = lBBlockSetRefReprList.iterator();
         lBBlockSetRefReprIterator.hasNext(); ) {
      // Iterate through the BBlock for SetRefRepr.
      lSetRefRepr = (SetRefRepr)lBBlockSetRefReprIterator.next();
      HIR lIrSubtree = (HIR)lSetRefRepr.getIR();
      if (fDbgFlow > 4) {
        ioRoot.dbgFlow.print(5,
            " " + ioRoot.toStringObjectShort(lIrSubtree));
      }
      List lUseList = lSetRefRepr.useSymList();
      for (Iterator lIt = lUseList.iterator();
           lIt.hasNext(); ) {
        FlowAnalSym lUsedSym = (FlowAnalSym)lIt.next();
        if (fDbgFlow > 3)
          ioRoot.dbgFlow.print(5, " use " + lUsedSym );
        lExpBitPosition = expLookup(lUsedSym.getIndex());
        ((BBlockImpl)pBBlock).getUsedVector().setBit(lExpBitPosition); //##65
      }
    }
    if (fDbgFlow > 3)
      flowRoot.flow.dbg(6, "findUsed B"+ pBBlock.getBBlockNumber(),
        ((BBlockImpl)pBBlock).getUsedVector().toStringDescriptive()); //##65
  } // findUsed BBlock

//##70 END

  /**
   * Finds and sets the Exposed vectors for the entire flow.
   */
  public void findExposed()
  {
    if (fDbgFlow > 3) {
      flowRoot.ioRoot.dbgFlow.print(5, "findExposed ");
    }
    if (fSubpFlow.isComputedOrUnderComputation(fSubpFlow.DF_EXPOSED)) {
      flow.dbg(5, " Use computed one.");
      return;
    }
    fSubpFlow.setUnderComputation(fSubpFlow.DF_EXPOSED); //##62
    BBlock lBBlock;
    for (Iterator lCFGIterator = getBBlockList().iterator();
         lCFGIterator.hasNext(); ) { // For each BBlock
      lBBlock = (BBlock)lCFGIterator.next();
      findExposed(lBBlock); // Find the Exposed vector.
    }
    fSubpFlow.setComputedFlag(fSubpFlow.DF_EXPOSED); //##60
  } // findExposed

  /**
   * Finds and sets the Exposed vector for the given BBlock.
   *
   * @param pBBlock BBlock whose Exposed vector to find.
   */
  //##82 BEGIN
  public void findExposed(BBlock pBBlock)
  { //##82 Recoded 071012
    if (fDbgFlow > 3) {
      ioRoot.dbgFlow.print(5, "findExposed", "B" + pBBlock.getBlockNumber());
    }
    SetRefReprList lBBlockSetRefReprList = fSubpFlow.getSetRefReprList(pBBlock); //##63
    SetRefRepr lSetRefRepr;
    FlowAnalSym lDefSym;
    int lExpBitPosition;
    Set lUseFlowAnalSyms;
    ExpVector lExposed = ((BBlockImpl)pBBlock).getExposedVector(); //##65
    Set lSetOfDefSyms = new HashSet(); //##65
    for (Iterator lBBlockSetRefReprIterator = lBBlockSetRefReprList.
         iterator();
         lBBlockSetRefReprIterator.hasNext(); ) {
      lSetRefRepr = (SetRefRepr)lBBlockSetRefReprIterator.next();
      if (fDbgFlow > 3) {
        ioRoot.dbgFlow.print(6, " IR ", lSetRefRepr.getIR().toStringShort());
      }
      //##62 if ((lDefSym = lSetRefRepr.getDefFlowAnalSym()) != null)
      lUseFlowAnalSyms = lSetRefRepr.getUseFlowAnalSyms();
      if (fDbgFlow > 3)
        ioRoot.dbgFlow.printObject(6, " lUse ", lUseFlowAnalSyms);
      lUseFlowAnalSyms.removeAll(lSetOfDefSyms);
      if (fDbgFlow > 3)
        ioRoot.dbgFlow.printObject(6, " exposed ", lUseFlowAnalSyms);
      lExposed.vectorOr(toExpVector(lUseFlowAnalSyms), lExposed);
      // Set the bits that are used in the current SetRefRepr.
      if ((lDefSym = lSetRefRepr.getDefSym()) != null) {
        // If the SetRefRepr is value-setting (AssignStmt in HIR),
        if (fDbgFlow > 3)
          ioRoot.dbgFlow.printObject(6, " lDef ", lDefSym);
        lSetOfDefSyms.add(lDefSym); //##65
      }
    } // lBBlockSetRefReprIterator
    if (fDbgFlow > 3)
      flowRoot.flow.dbg(6, "findExposed B"+ pBBlock.getBBlockNumber(),
        ((BBlockImpl)pBBlock).getExposedVector().toStringDescriptive()); //##65
  } // findExposed BBlock
//##82 END

  /**
   * Finds and sets the EGen vectors for the entire flow.
   */
  public void findEGen()
  {
    if (fDbgFlow > 3) {
      flowRoot.ioRoot.dbgFlow.print(5, "findEGen ");
    }
    if (fSubpFlow.isComputedOrUnderComputation(fSubpFlow.DF_EGEN)) {
      flow.dbg(5, " Use computed one.");
      return;
    }
    BBlock lBBlock;
    fSubpFlow.setUnderComputation(fSubpFlow.DF_EGEN); //##62
    fRecordAlias = fSubpFlow.getRecordAlias(); //##62
    for (Iterator lCFGIterator = getBBlockList().iterator();
         lCFGIterator.hasNext(); ) { // For each BBlock
      lBBlock = (BBlock)lCFGIterator.next();
      findEGen(lBBlock); // Find the EGen vector.
    }
    fSubpFlow.setComputedFlag(fSubpFlow.DF_EGEN); //##60
  } // findEGen

  /** addEGenExpId
   * EGen computation by using ExpId.
   * Result of alias analysis is considered in the computation.
   * @param pEGenSet accumulated set of EGen ExpIds in the current BBlock.
   * @param pEKillSet accumulated set of EKill ExpIds in the current BBlock.
   * @param pSetRefRepr SetRefRepr of an expression.
   */
  protected void addEGenExpId(Set pEGenSet,
    Set pEKillSet, SetRefRepr pSetRefRepr)
  {
    //##60 Set lGenerated = (Set)fResults.get("UseFlowExpIdsForSetRefRepr",
    //##60  pSetRefRepr); // ExpIds generated by expression represented
    // by pSetRefRepr.
    Set lGenerated = ((SetRefReprHirEImpl)pSetRefRepr).expIdSet(); //##60
    HIR lHir = (HIR)pSetRefRepr.getIR(); //##57
    if (fDbgFlow > 3) {
      flow.dbg(5, "\n addEGenExpId", lHir.toStringShort() +
               " generates " + lGenerated); //##57
    }
    if (fDbgFlow > 3) {
      flow.dbg(5, " kill", pEKillSet);
      //##57 lGenerated.removeAll(pEKillSet); // Remove killed subexpressions.
    }
    pEGenSet.addAll(lGenerated); // Add subexpressions generated by this expression.
    pEKillSet.removeAll(lGenerated); // Remove the generated expression.
    Set lModSyms = new HashSet(); // Symbols modified by the statement
    // including the expression corresponding to pSetRefRepr
    // considering alias.
    lModSyms.addAll(pSetRefRepr.modSyms00());
    if (pSetRefRepr instanceof SetRefReprHirEImpl) {
      lModSyms.addAll(((SetRefReprHirEImpl)pSetRefRepr).modSymsStmt());
    }
    if (fDbgFlow > 3) {
      flow.dbg(6, "addEGen lModSyms", lModSyms);
    }
    if (fSubpFlow != null) {
      if (fRecordAlias != null) {
        Set lModAlias = fRecordAlias.aliasSymGroup(lModSyms);
        lModSyms.addAll(lModAlias);
        if (fDbgFlow > 3) {
          flow.dbg(6, " modAlias ", lModSyms); // Symbols aliased with
          // the modified symbols.
        }
      }
    }
    // Remove symbols that are computed using the operands whose value
    // is modified by the statement including this expression.
    for (Iterator lIt = pEGenSet.iterator(); lIt.hasNext(); ) {
      //##60 FlowExpId lFlowExpId = (FlowExpId)lIt.next();
      ExpId lExpId = (ExpId)lIt.next(); //##60
      for (Iterator lOperandIt = lExpId.getOperandSet0().iterator();
           lOperandIt.hasNext(); ) {
        if (lModSyms.contains(lOperandIt.next())) {
          lIt.remove(); // Remove the symbol whose operand is modified
          // by this statement considering alias.
          break;
        }
      }
    }
    // Add expressions killed by lModSyms to pEKillSet.
    ExpId lExpId;
    if (fSubpFlow != null) { //##57
      //##60 FAList lExpIdTable = fSubpFlow.getExpIdTable();
      List lExpIdList = fSubpFlow.getExpIdList(); //##60
      for (Iterator lIterator2 = lExpIdList.iterator();
           lIterator2.hasNext(); ) {
        lExpId = (ExpId)lIterator2.next();
        if (lExpId != null) {
          for (Iterator lOperandIt = lExpId.getOperandSet0().iterator();
               lOperandIt.hasNext(); ) {
            if (lModSyms.contains(lOperandIt.next())) {
              pEKillSet.add(lExpId);
              if (fDbgFlow > 3) {
                flow.dbg(7, " add-to-EKill ", lExpId);
              }
            }
          }
        }
      }
      if (fDbgFlow > 3) {
        flow.dbg(5, "\n  pEKillSet", pEKillSet);
        flow.dbg(6, " addEGen-result=", pEGenSet);
      }
    } //##57
  } // addEGenExpId

  /**
   * Finds and sets the EGen vector for the given BBlock.
   *
   * @param pBBlock BBlock whose EGen vector to find.
   */
// See findEGen(BBlock pBBlock) of
// DataFlowHirImpl and DataFlowLirImpl.
  public void findEGen(BBlock pBBlock)
  {
    if (fDbgFlow > 0) {
      flowRoot.ioRoot.dbgFlow.print(1, "findEGen",
        "should be used that of subclass");
    }
  }

  /**
   * Finds and sets the EKill vectors for the entire flow.
   */
  public void findEKill()
  {
    if (fDbgFlow > 3) {
      flowRoot.ioRoot.dbgFlow.print(5, "findEKill ");
    }
    if (fSubpFlow.isComputedOrUnderComputation(fSubpFlow.DF_EKILL)) {
      flow.dbg(5, " Use computed one.");
      return;
    }
    //##62 BEGIN
    fSubpFlow.setUnderComputation(fSubpFlow.DF_EKILL); //##62
    // EKill uses data computed by findEGen.
    findEGen();
    BBlock lBBlock;
    for (Iterator lCFGIterator = getBBlockList().iterator();
         lCFGIterator.hasNext(); ) { // For each BBlock
      lBBlock = (BBlock)lCFGIterator.next();
      findEKill(lBBlock); // Find the EKill vector.
    }
    //##62 END
    fSubpFlow.setComputedFlag(fSubpFlow.DF_EKILL); //##60
  } // findEKill

  /**
   * Finds and sets the EGen vector for the given BBlock.
   *
   * @param pBBlock BBlock whose EGen vector to be found.
   */
  public void // See DataFlowHirImpl & DataFlowLirImpl
    findEKill(BBlock pBBlock)
  {}

  /**
   * Finds and sets the Reach vectors from the Def and
   * Kill vectors using the data flow equations.
   * Implementation of the algorithm on p.275
   * {@link <a href="http://www.ulis.ac.jp/~nakata/aCompiler.html">Nakada (1999)</a>}.
   *
   * See #findDef()
   * See #findKill()
   */
  public void findReach()
  {
    if (fDbgFlow > 3) {
      flowRoot.ioRoot.dbgFlow.print(5, "findReach ");
    }
    if (fSubpFlow.isComputedOrUnderComputation(fSubpFlow.DF_REACH)) {
      flow.dbg(5, " Use computed one.");
      return;
    }
    fSubpFlow.setUnderComputation(fSubpFlow.DF_REACH); //##62
    if (!fSubpFlow.isComputed(fSubpFlow.DF_DEF)) {
      findDef(); // Def is required to compute Reach.
    }
    if (!fSubpFlow.isComputed(fSubpFlow.DF_KILL)) {
      findKill(); // Def is required to compute Reach.
    }
    BBlock lBBlock;
    boolean lChanged;
    List lPredList;
    BBlock lPredBBlock;
    DefVector lPredDef, lPredKill, lPredReach;
    DefVector lSurvived = fSubpFlow.defVector();
    DefVector lPredReachOut = fSubpFlow.defVector();

    // Initialize.
    for (Iterator lCFGIterator = getBBlockList().iterator();
         lCFGIterator.hasNext(); ) {
      lBBlock = (BBlock)lCFGIterator.next();
      //##65 lBBlock.getReach().vectorReset();
      ((BBlockImpl)lBBlock).getReachVector().vectorReset(); //##65
    }

    // Solve by iteration.
    int lRepetition = 0; //##60
    do {
      lRepetition++; //##60
      if (fDbgFlow > 3) {
        flowRoot.ioRoot.dbgFlow.print(4, "reach loop " + lRepetition); //##60
      }
      lChanged = false;
      for (Iterator lCFGIterator = getBBlockList().iterator();
           lCFGIterator.hasNext(); ) {
        DefVector lNewReach = fSubpFlow.defVector();
        DefVector lDefSurvived = fSubpFlow.defVector(); //##70
        lBBlock = (BBlock)lCFGIterator.next();
        lPredList = lBBlock.getPredList();
        for (Iterator lPredIterator = lPredList.iterator();
             lPredIterator.hasNext(); ) {
          //Find the union of all incoming flows(lPredReachs).
          lPredBBlock = (BBlock)lPredIterator.next();
          lPredDef = lPredBBlock.getDef();
          lPredKill = lPredBBlock.getKill();
          if (fDbgFlow >= 5) {
            ioRoot.dbgFlow.print(5, "predDef ",
              "B" + lPredBBlock.getBBlockNumber());
            showDataFlowByName.showPointVectorByName(lPredDef);
            ioRoot.dbgFlow.print(5, "predKill ",
              "B" + lPredBBlock.getBBlockNumber());
            showDataFlowByName.showPointVectorByName(lPredKill);
          }
          //##65 lPredReach = lPredBBlock.getReach();
          lPredReach = ((BBlockImpl)lPredBBlock).getReachVector(); //##65
          //##70 lPredReach.vectorSub(lPredKill, lSurvived);
          //##70 lPredDef.vectorOr(lSurvived, lPredReachOut);
          //##70 lPredDef.vectorSub(lPredKill, lDefSurvived); //##70
          lPredReach.vectorOr(lPredDef, lPredReachOut); //##70
          lPredReachOut.vectorSub(lPredKill, lPredReachOut); //##7
          lNewReach.vectorOr(lPredReachOut, lNewReach);
        }
        //##65 if (!lNewReach.vectorEqual(lBBlock.getReach()))
        if (!lNewReach.vectorEqual(((BBlockImpl)lBBlock).getReachVector())) { //##65
          //If Reach has changed, continue the loop.
          lChanged = true;
          ((BBlockImpl)lBBlock).setReach(lNewReach);
          if (fDbgFlow >= 5) {
            ioRoot.dbgFlow.print(5, "changed reach ",
              "B" + lBBlock.getBBlockNumber()); //##60
            showDataFlowByName.showPointVectorByName(lNewReach);
          }
        }
      }
    }
    while (lChanged);
    fSubpFlow.setComputedFlag(fSubpFlow.DF_REACH); //##60
  } // findReach(BBlock)

  /**
   * Finds and sets the AvailIn and AvailOut vectors from the EGen and
   *  EKill vectors using the data flow equations.
   * Implementation of the algorithm on p. 280,
   * {@link <a href="http://www.ulis.ac.jp/~nakata/aCompiler.html">Nakada (1999)</a>}.
   */
  public void findAvailInAvailOut()
  {
    if (fDbgFlow > 3) {
      flowRoot.ioRoot.dbgFlow.print(5, "findAvailInAvailOut ");
    }
    if (fSubpFlow.isComputedOrUnderComputation(fSubpFlow.DF_AVAILIN)) {
      flow.dbg(5, " Use computed one.");
      return;
    }
    fSubpFlow.setUnderComputation(fSubpFlow.DF_AVAILIN); //##62
    fSubpFlow.setUnderComputation(fSubpFlow.DF_AVAILOUT); //##62
    BBlock lBBlock;
    ExpVector lAvailIn;
    boolean lChanged;
    List lPredList;
    BBlock lPredBBlock;
    ExpVector lPredEGen, lPredEKill, lPredAvailIn;
    ExpVector lSurvived = fSubpFlow.expVector();
    ExpVector lPredAvailOut = fSubpFlow.expVector();
    ExpVector lEGen, lEKill, lAvailOut;

    // Initialize.
    for (Iterator lCFGIterator = getBBlockList().iterator();
         lCFGIterator.hasNext(); ) {
      lBBlock = (BBlock)lCFGIterator.next();
      //##65 lAvailIn = lBBlock.getAvailIn();
      lAvailIn = ((BBlockImpl)lBBlock).getAvailInVector(); //##65
      if (lBBlock.isEntryBlock()) {
        lAvailIn.vectorReset();
      }
      else {
        EXP_INVERTED.vectorCopy(lAvailIn);
      }
    }

    // Solve the AvailIn vectors by iteration.
    do {
      lChanged = false;
      for (Iterator lCFGIterator = getBBlockList().iterator();
           lCFGIterator.hasNext(); ) {
        ExpVector lNewAvailIn = fSubpFlow.expVector();
        lBBlock = (BBlock)lCFGIterator.next();
        if (fDbgFlow > 3)
          flowRoot.ioRoot.dbgFlow.print(5, " B" + lBBlock.getBBlockNumber());
        if (!lBBlock.isEntryBlock()) {
          //Calculate only for non-entry blocks.
          lPredList = lBBlock.getPredList();
          EXP_INVERTED.vectorCopy(lNewAvailIn);
          for (Iterator lPredIterator = lPredList.iterator();
               lPredIterator.hasNext(); ) {
            //Find the intersection of all incoming flows(lPredAvailIns).
            lPredBBlock = (BBlock)lPredIterator.next();
            lPredEGen = lPredBBlock.getEGen();
            lPredEKill = lPredBBlock.getEKill();
            //##65 lPredAvailIn = lPredBBlock.getAvailIn();
            lPredAvailIn = ((BBlockImpl)lPredBBlock).getAvailInVector(); //##65
            lPredAvailIn.vectorSub(lPredEKill, lSurvived);
            lPredEGen.vectorOr(lSurvived, lPredAvailOut);
            lNewAvailIn.vectorAnd(lPredAvailOut, lNewAvailIn);
          }
          //##65 if (!lNewAvailIn.vectorEqual(lBBlock.getAvailIn()))
          if (!lNewAvailIn.vectorEqual(((BBlockImpl)lBBlock).getAvailInVector())) //##65
          {
            //If AvailIn has changed, continue the loop.
            lChanged = true;
            ((BBlockImpl)lBBlock).setAvailIn(lNewAvailIn);
          }
        }
      }
    }
    while (lChanged);

    // Compute the AvailOut vectors.
    for (Iterator lCFGIterator = getBBlockList().iterator();
         lCFGIterator.hasNext(); ) {
      lBBlock = (BBlock)lCFGIterator.next();
      lEGen = lBBlock.getEGen();
      lEKill = lBBlock.getEKill();
      //##65 lAvailIn = lBBlock.getAvailIn();
      lAvailIn = ((BBlockImpl)lBBlock).getAvailInVector(); //##65
      lAvailIn.vectorSub(lEKill, lSurvived);
      lAvailOut = fSubpFlow.expVector();
      lEGen.vectorOr(lSurvived, lAvailOut);
      ((BBlockImpl)lBBlock).setAvailOut(lAvailOut);
    }
    fSubpFlow.setComputedFlag(fSubpFlow.DF_AVAILIN); //##60
    fSubpFlow.setComputedFlag(fSubpFlow.DF_AVAILOUT); //##60
  } // findAvailInAvailOut

  /**
   * Finds and sets the LiveIn and LiveOut vectors from the Exposed and Defined vectors using the data flow equations.
   *
   * See #findExposed()
   * See #findDefined()
   */
  public void findLiveInLiveOut()
  {
    if (fDbgFlow > 3) {
      flowRoot.ioRoot.dbgFlow.print(5, "findLiveInLiveOut\n");
    }
    if (fSubpFlow.isComputedOrUnderComputation(fSubpFlow.DF_LIVEIN)) {
      flow.dbg(5, " Use computed one.");
      return;
    }
    fSubpFlow.setUnderComputation(fSubpFlow.DF_LIVEIN); //##62
    BBlock lBBlock;
    boolean lChanged;
    List lSuccList;
    BBlock lSuccBBlock;
    //##60 ExpVector lSuccExposed, lSuccDefined, lSuccLiveOut;
    //##60 ExpVector lSurvived = fSubpFlow.expVector();
    //##60 ExpVector lSuccLiveIn = fSubpFlow.expVector();
    //##60 ExpVector lExposed, lDefined, lLiveIn, lLiveOut;
    FlowAnalSymVector lSuccExposed, lSuccDefined, lSuccLiveOut; //##60
    FlowAnalSymVector lSurvived = fSubpFlow.flowAnalSymVector(); //##60
    FlowAnalSymVector lSuccLiveIn = fSubpFlow.flowAnalSymVector(); //##60
    FlowAnalSymVector lExposed, lDefined, lLiveIn, lLiveOut; //##60

    // Initialize.
    for (Iterator lCFGIterator = getBBlockList().iterator();
         lCFGIterator.hasNext(); ) {
      lBBlock = (BBlock)lCFGIterator.next();
      //##65 lBBlock.getLiveOut().vectorReset();
      ((BBlockImpl)lBBlock).getLiveOutVector().vectorReset(); //##65
    }

    // Solve the LiveOut vectors by iteration.
    do {
      lChanged = false;
      if (fDbgFlow > 3) {
        ioRoot.dbgFlow.print(5, "findLiveInLiveOut", "iteration ");
      }
      for (ListIterator lCFGIterator = getBBlockList().listIterator(
        getBBlockList().size()); lCFGIterator.hasPrevious(); ) {
        //##60 ExpVector lNewLiveOut = fSubpFlow.expVector();
        FlowAnalSymVector lNewLiveOut = fSubpFlow.flowAnalSymVector(); //##60
        lBBlock = (BBlock)lCFGIterator.previous();
        lSuccList = lBBlock.getSuccList();
        if (fDbgFlow > 3) {
          ioRoot.dbgFlow.print(5, " B " + lBBlock.getBlockNumber());
        }
        for (Iterator lSuccIterator = lSuccList.iterator();
             lSuccIterator.hasNext(); ) {
          // Find the union of all incoming flows(lSuccLiveOuts).
          lSuccBBlock = (BBlock)lSuccIterator.next();
          lSuccExposed = lSuccBBlock.getExposed();
          lSuccDefined = lSuccBBlock.getDefined();
          //##65 lSuccLiveOut = lSuccBBlock.getLiveOut();
          lSuccLiveOut = ((BBlockImpl)lSuccBBlock).getLiveOutVector(); //##65
          lSuccLiveOut.vectorSub(lSuccDefined, lSurvived);
          lSuccExposed.vectorOr(lSurvived, lSuccLiveIn);
          lNewLiveOut.vectorOr(lSuccLiveIn, lNewLiveOut);
        }
        //##65 if (!lNewLiveOut.vectorEqual(lBBlock.getLiveOut())
        if (!lNewLiveOut.vectorEqual(((BBlockImpl)lBBlock).getLiveOutVector())) //##65
        {
          // If LiveOut has changed, continue the loop.
          lChanged = true;
          ((BBlockImpl)lBBlock).setLiveOut(lNewLiveOut);
        }
      }
    }
    while (lChanged);

    // Compute the LiveIn vectors.
    for (Iterator lCFGIterator = getBBlockList().iterator();
         lCFGIterator.hasNext(); ) {
      lBBlock = (BBlock)lCFGIterator.next();
      lExposed = lBBlock.getExposed();
      lDefined = lBBlock.getDefined();
      //##65 lLiveOut = lBBlock.getLiveOut();
      lLiveOut = ((BBlockImpl)lBBlock).getLiveOutVector(); //##65
      lLiveOut.vectorSub(lDefined, lSurvived);
      //##60 lLiveIn = fSubpFlow.expVector();
      lLiveIn = fSubpFlow.flowAnalSymVector(); //##60
      lExposed.vectorOr(lSurvived, lLiveIn);
      ((BBlockImpl)lBBlock).setLiveIn(lLiveIn);
    }
    fSubpFlow.setComputedFlag(fSubpFlow.DF_LIVEIN); //##60
    fSubpFlow.setComputedFlag(fSubpFlow.DF_LIVEOUT); //##60
  } // findLiveInLiveOut

  /**
   * Finds and sets the DefIn and DefOut vectors from the Defined vectors using the data flow equations.
   *
   * See #findDefined()
   */
  public void findDefInDefOut()
  {
    if (fDbgFlow > 3) //##67
      flowRoot.ioRoot.dbgFlow.print(5, "\n findDefInDefOut "); //##65
    if (fSubpFlow.isComputedOrUnderComputation(fSubpFlow.DF_DEFIN)) {
      flow.dbg(5, " Use computed one.");
      return;
    }
    fSubpFlow.setUnderComputation(fSubpFlow.DF_DEFIN); //##62
    BBlock lBBlock;
    boolean lChanged;
    List lPredList;
    BBlock lPredBBlock;
    //##60 ExpVector lDefIn;
    //##60 ExpVector lPredDefined, lPredDefIn;
    //##60 ExpVector lPredDefOut = fSubpFlow.expVector();
    //##60 ExpVector lDefined, lDefOut;
    FlowAnalSymVector lDefIn; //##60
    FlowAnalSymVector lPredDefined, lPredDefIn; //##60
    FlowAnalSymVector lPredDefOut = fSubpFlow.flowAnalSymVector(); //##60
    FlowAnalSymVector lDefined, lDefOut; //##60

    // Initialize.
    for (Iterator lCFGIterator = getBBlockList().iterator();
         lCFGIterator.hasNext(); ) {
      lBBlock = (BBlock)lCFGIterator.next();
      //##65 lDefIn = lBBlock.getDefIn();
      lDefIn = ((BBlockImpl)lBBlock).getDefInVector(); //##65
      if (lBBlock.isEntryBlock()) {
        lDefIn.vectorReset();
      }
      else {
        EXP_INVERTED.vectorCopy(lDefIn);
      }
    }

    // Solve the DefIn vectors by iteration.
    do {
      lChanged = false;
      for (Iterator lCFGIterator = getBBlockList().iterator();
           lCFGIterator.hasNext(); ) {
        //##60 ExpVector lNewDefIn = fSubpFlow.expVector();
        FlowAnalSymVector lNewDefIn = fSubpFlow.flowAnalSymVector(); //##60
        lBBlock = (BBlock)lCFGIterator.next();
        if (!lBBlock.isEntryBlock()) {
          //Calculate only for non-entry blocks.
          lPredList = lBBlock.getPredList();
          EXP_INVERTED.vectorCopy(lNewDefIn);
          for (Iterator lPredIterator = lPredList.iterator();
               lPredIterator.hasNext(); ) {
            //Find the intersection of all incoming flows(lPredDefIns).
            lPredBBlock = (BBlock)lPredIterator.next();
            lPredDefined = lPredBBlock.getDefined();
            //##65 lPredDefIn = lPredBBlock.getDefIn();
            lPredDefIn = ((BBlockImpl)lPredBBlock).getDefInVector(); //##65
            lPredDefined.vectorOr(lPredDefIn, lPredDefOut);
            lNewDefIn.vectorAnd(lPredDefOut, lNewDefIn);
          }
          //##65 if (!lNewDefIn.vectorEqual(lBBlock.getDefIn()))
          if (!lNewDefIn.vectorEqual(((BBlockImpl)lBBlock).getDefInVector())) //##65
          {
            //If DefIn has changed, continue the loop.
            lChanged = true;
            ((BBlockImpl)lBBlock).setDefIn(lNewDefIn);
          }
        }
      }
    }
    while (lChanged);

    // Compute the DefOut vectors.
    for (Iterator lCFGIterator = getBBlockList().iterator();
         lCFGIterator.hasNext(); ) {
      lBBlock = (BBlock)lCFGIterator.next();
      lDefined = lBBlock.getDefined();
      //##65 lDefIn = lBBlock.getDefIn();
      lDefIn = ((BBlockImpl)lBBlock).getDefInVector(); //##65
      //##60 lDefOut = fSubpFlow.expVector();
      lDefOut = fSubpFlow.flowAnalSymVector(); //##60
      lDefined.vectorOr(lDefIn, lDefOut);
      ((BBlockImpl)lBBlock).setDefOut(lDefOut);
    }
    fSubpFlow.setComputedFlag(fSubpFlow.DF_DEFIN); //##60
    fSubpFlow.setComputedFlag(fSubpFlow.DF_DEFOUT); //##60
  } // findDefInDefOut

  /** Performs transformation between "natural indices" and bit positions. */

  /**
   * Changes the IR node index into the "Def" index.
   * @param pNodeIndex index of the value-setting node (AssignStmt in HIR).
   */
  public int defLookup(int pNodeIndex)
  {
    int lBitPosition = getDefIndex(pNodeIndex);
    return lBitPosition;
  } // defLookUp

  /**
   * Changes the "Def" index into the IR node index.
     * @param pBitPosition index in the DefVector for which to find the node index.
   */
  public int defReverseLookup(int pBitPosition)
  {
    //##62 int lNodeIndex = fNodeIndexTable[pBitPosition];
    int lNodeIndex = fDefNodeIndexTable[pBitPosition]; //##62
    return lNodeIndex;
  }

  /**
   * Changes the ExpId index into the position in the ExpVector.
   *  Currently does nothing.
   * @param pExpIdIndex index of ExpId.
   */
  public int expLookup(int pExpIdIndex)
  {
    int lBitPosition = pExpIdIndex;
    return lBitPosition;
  }

  /**
   * Changes the position in the ExpVector into the ExpId index.
   *  Currently does nothing.
   * @param pBitPosition position in the ExpVector.
   */
  public
    int expReverseLookup(int pBitPosition)
  {
    int lExpIdIndex = pBitPosition;
    return lExpIdIndex;
  }

  /** !!HIR
   * Returns the Set of ExpIds that fall under the given subtree and are used.
   * The ExpId that is attached to the "Def node" will not be included
   * if the given subtree is a value-setting node (AssignStmt in HIR).
   * The ExpId that corresponds to the given subtree is also included
   *  in the Set. Supports HIR only so far.
   *
   * @param pSubtree IR node that is the root of the subtree to examine.
   * @return the Set of ExpIds that fall under the given subtree and are used.
   */
// public java.util.Set getUseExpIds(IR pSubtree)
  public java.util.Set
    getUseFlowAnalSyms(IR pSubtree)
  {
    return ((DataFlowHirImpl)this).getUseFlowAnalSymsForHir((HIR)pSubtree);
  }

  public java.util.Set
    getUseFlowAnalSymsForHir(HIR pSubtree)
  {
    if (fDbgFlow > 0) {
      ioRoot.dbgFlow.print(1, "getUseFlowAnalSymsForHir",
        "should use subclass method");
    }
    return null;
  }

  /**
     * Returns the Set of ExpIds that are contained in the given ExpId and are used.
   * The ExpId that is attached to the "Def node" will not be included
     *  if the given ExpId is attached to a value-setting node (AssignStmt in HIR).
   * The given ExpId is also contained in the returned Set.
   *
   * @param pExpId ExpId whose content to be extracted.
   * @return the Set of ExpIds that are contained and used.
   * See #getUseExpIds()
   */
  public java.util.Set
    getUseFlowAnalSyms(FlowAnalSym pFlowAnalSym)
  {
    return ((DataFlowHirImpl)this).getUseFlowAnalSyms(pFlowAnalSym);
  }

  /**
   * Cleans the environment that may contain junk information
   *  for the current DFA.
   */
  public void clean()
  {
    if (fDbgFlow > 0) {
      flow.dbg(3, "\nclean");
    }
    ((SubpFlowImpl)fSubpFlow).failed = false; //##78
    BBlock lBBlock;

    for (Iterator lCFGIterator = getBBlockList().iterator();
         lCFGIterator.hasNext(); ) { // Clear each BBlock's fWorkFA field.
      lBBlock = (BBlock)lCFGIterator.next();
      if (lBBlock != null) { //##63
        lBBlock.setWorkFA(null);
      }
    }
    //##60 setRefRepr.reset(); // Clear SetRefRepr's static fields.
  }

  /**
   * Finds and sets the data flow items that are independent of the control flow, that is, Def, Kill, Defined, Exposed, EGen, and EKill vectors.
   *
   * See #findDef()
   * See #findDefined()
   * See #findKill()
   * See #findExposed()
   * See #findEGen()
   * See #findEKill()
   */
  public void findBasic()
  {
    findDef();
    findDefined();
    findKill();
    findExposed();
    findEGen();
    findEKill();
  }

  /**
   * Solves all the data flow equations to find In, Out, Reach, AvailIn, AvailOut, LiveIn, LiveOut, DefIn, and DefOut vectors.
   *
   * See #findInOut()
   * See #findReach()
   * See #findAvailInAvailOut()
   * See #findLiveInLiveOut()
   * See #findDefInDefOut()
   */
  public void solveAll()
  {
    //findInOut();
    findReach();
    findAvailInAvailOut();
    findLiveInLiveOut();
    findDefInDefOut();
  } // solveAll

  /**
   * Finds and sets all the BitVectors, that is, Def, Kill, In, Out,
   *  Reach, Defined, Exposed,
   * EGen, EKill, AvailIn, AvailOut, LiveIn, LiveOut, DefIn,
   *  and DefOut vectors.
   *
   * See #findBasic()
   * See #solveAll()
   */
  public void findAllBitVectors()
  {
    findBasic();
    solveAll();
  }

  /**
   * Finds and sets the DefUseList for each FlowAnalSym
   * without considering side effects of call and alias.
   */
  public void findDefUse()
  {
    flow.dbg(5, "findDefUse ");
    if (fSubpFlow.isComputedOrUnderComputation(fSubpFlow.DF_DEFUSELIST)) {
      flow.dbg(5, " Use computed one.");
      return;
    }
    fSubpFlow.setUnderComputation(fSubpFlow.DF_DEFUSELIST); //##62
    //##60 recordDefPoints();
    //##60 recordUsePoints();
    //##60 BEGIN
    // recordDefUsePoints();
    //##63 BEGIN
    if (!fSubpFlow.isComputed(fSubpFlow.DF_DEF)) {
      findDef();
    }
    //##63 END
    if (!fSubpFlow.isComputed(fSubpFlow.DF_REACH)) {
      findReach(); // Find reaching definitions.
    }
    /* //##63
         BBlock lBBlock;
         for (Iterator lCFGIterator = getBBlockList().iterator();
         lCFGIterator.hasNext(); ) { // For each BBlock,
      lBBlock = (BBlock)lCFGIterator.next();
      findDefUse(lBBlock); // Find the Def vector.
         }
     */
    //##63
    findUseDef(); //##63
    fSubpFlow.setComputedFlag(fSubpFlow.DF_DEFUSELIST);
    //##60 END
  } // findDefUse

//##73 BEGIN
  /**
   * Finds and sets the DefUseList for each FlowAnalSym
   * considering side effects of call and alias.
   */
  public void findDefUseExhaustively()
  {
    flow.dbg(5, "findDefUseExhaustively ");
    if (fSubpFlow.isComputedOrUnderComputation(fSubpFlow.DF_DEFUSEEXHAUST)) {
      flow.dbg(5, " Use computed one.");
      return;
    }
    fSubpFlow.setUnderComputation(fSubpFlow.DF_DEFUSEEXHAUST);
    if (!fSubpFlow.isComputed(fSubpFlow.DF_DEF)) {
      findDef();
    }
    if (!fSubpFlow.isComputed(fSubpFlow.DF_REACH)) {
      findReach(); // Find reaching definitions.
    }
    findUseDefExhaustively();
    fSubpFlow.setComputedFlag(fSubpFlow.DF_DEFUSEEXHAUST);
  } // findDefUseExhaustively

//##73 END

// Methods from aflow.FindDefUseList

  /** Find UseDef relations and DefUse relations
   * without considering side effects of call and alias.
   */
  public void
    findUseDef()
  {
    if (fDbgFlow > 3) {
      flow.dbg(5, "findUseDef ");
    }
    if (fSubpFlow.isComputedOrUnderComputation(fSubpFlow.DF_USEDEFLIST)) {
      if (fDbgFlow > 3) {
        flow.dbg(5, " Use computed one.");
      }
      return;
    }
    fSubpFlow.setUnderComputation(fSubpFlow.DF_USEDEFLIST); //##62
    if (!fSubpFlow.isComputed(fSubpFlow.DF_DEFINED)) { //##63
      findDefined(); // Find defined symbols. //##63
    } //##63
    //##73 BEGIN
    if (!fSubpFlow.isComputed(fSubpFlow.DF_USED)) {
      findUsed();
    }
    //##73 END
    if (!fSubpFlow.isComputed(fSubpFlow.DF_REACH)) {
      findReach(); // Find reaching definitions.
    }
    BBlock lBBlock;
    for (Iterator lCFGIterator = getBBlockList().iterator();
         lCFGIterator.hasNext(); ) { // For each BBlock,
      lBBlock = (BBlock)lCFGIterator.next();
      findUseDef(lBBlock, false);
    }
    fSubpFlow.setComputedFlag(fSubpFlow.DF_USEDEFLIST);
    fSubpFlow.setComputedFlag(fSubpFlow.DF_DEFUSELIST);
  } // findUseDef

  /** Find UseDef relations and DefUse relations
   * considering side effects of call and alias.
   */
  public void
    findUseDefExhaustively()
  {
    if (fDbgFlow > 3) {
      flow.dbg(5, "findUseDefExhaustively ");
    }
    if (fSubpFlow.isComputedOrUnderComputation(fSubpFlow.DF_USEDEFEXHAUST)) {
      if (fDbgFlow > 3) {
        flow.dbg(5, " Use computed one.");
      }
      return;
    }
    fSubpFlow.setUnderComputation(fSubpFlow.DF_USEDEFEXHAUST); //##62
    if (!fSubpFlow.isComputed(fSubpFlow.DF_DEFINED)) {
      findDefined(); // Find defined symbols.
    }
    if (!fSubpFlow.isComputed(fSubpFlow.DF_USED)) {
      findUsed();
    }
    if (!fSubpFlow.isComputed(fSubpFlow.DF_REACH)) {
      findReach(); // Find reaching definitions.
    }
    BBlock lBBlock;
    for (Iterator lCFGIterator = getBBlockList().iterator();
         lCFGIterator.hasNext(); ) { // For each BBlock,
      lBBlock = (BBlock)lCFGIterator.next();
      findUseDef(lBBlock, true);
    }
    fSubpFlow.setComputedFlag(fSubpFlow.DF_USEDEFEXHAUST);
    fSubpFlow.setComputedFlag(fSubpFlow.DF_DEFUSEEXHAUST);
  } // findUseDefExhaustively


  /** findUseDef
   * Compute both DefUse relations and UseDef relations for pBBlock.
   * @param pBBlock
   */
  protected void
    findUseDef(BBlock pBBlock, boolean pExhaustive)
  {
    //##73 SubpFlow lSubpFlow = pBBlock.getSubpFlow();
    if (fDbgFlow > 3) {
      flow.dbg(5, "\nfindUseDef B" +pBBlock.getBBlockNumber()
               + " " + pBBlock.getIrLink()
               + " exhaustive " + pExhaustive);
      //##63 SetRefRepr lSetRefRepr;
      //##63 IR lUseNode;
    }
    IR lDefNode;
    FlowAnalSym lDefSym, lFlowAnalSym;
    DefVector lReach = pBBlock.getReach();
    DefVectorIterator lReachIt;
    SetRefRepr lReachSetRefRepr;
    DefUseList lDefUseList;
    UseDefList lUseDefList;
    int lPos;
    IR lCallNode;
    IR lSetRefReprNode; //##65
    ArrayList lCallNodeStack = new ArrayList();
    //##63 FAList lSymIndexTable = lSubpFlow.getSymIndexTable();
    // lSymToDefNode: Map symbol to node that may define the symbol.
    // It is firstly set by reaching definitions and then
    // maintained by definitions in this BBlock.
    ListValuedMap lSymToDefNode = new ListValuedMap();
    //##73 lDefUseList = (DefUseList)fSubpFlow.getDefUseList(); //##63
    //##73 lUseDefList = (UseDefList)fSubpFlow.getUseDefList(); //##63
    if (pExhaustive) {
      lDefUseList = (DefUseList)((SubpFlowImpl)fSubpFlow).getListOfDefUseExhaustiveList();
      lUseDefList = (UseDefList)((SubpFlowImpl)fSubpFlow).getListOfUseDefExhaustiveList();
    }else {
      lDefUseList = (DefUseList)((SubpFlowImpl)fSubpFlow).getListOfDefUseList();
      lUseDefList = (UseDefList)((SubpFlowImpl)fSubpFlow).getListOfUseDefList();
    }
    Set lDefSyms = new HashSet(); // Symbols defined in this BBlock ?
    Set lUsedSyms = pBBlock.getUsed().flowAnalSyms(); // Symbols used
                                  // in this BBlock. //##73
    Set lModSyms; //##73
    lReachIt = lReach.defVectorIterator();
    //-- For each reaching definition, record it as the def-node
    //   of symbols possibly modified by the definition.
    while (true) { // Repeat for each reaching definition.
      // Get SetRefRepr of the reaching definition.
      lReachSetRefRepr = lReachIt.nextSetRefRepr();
      if (lReachSetRefRepr == null) {
        break;
      }
      lSetRefReprNode = lReachSetRefRepr.getIR(); //##65
      if (pExhaustive)
        lModSyms = lReachSetRefRepr.modSyms00();
      else
        lModSyms = lReachSetRefRepr.modSyms();
      if (fDbgFlow > 3) {
        flow.dbg(5, "lReachSetRefRepr",
                 "modSyms" + lModSyms + " "
                 + lSetRefReprNode.toStringShort()); //##65
      }
      for (Iterator lDefSymIt = lModSyms.iterator();
           lDefSymIt.hasNext(); ) {
        // Repeat for each symbol possibly modified by the definition
        lFlowAnalSym = (FlowAnalSym)lDefSymIt.next();
        if ((lFlowAnalSym instanceof ExpId) &&
            (! fSubpFlow.getMaximalCompoundVars().contains(lFlowAnalSym))) { //##65
          // Exclude expressions that are included in larger expression.
          continue; //##65
        }
        // If the symbol (set by the reaching definition)
        // is not used in this BBlock, skip it. //##73
        if (! lUsedSyms.contains(lFlowAnalSym)) //##73
          continue; //##73
        //##73 BEGIN
        ((List)lSymToDefNode.get(lFlowAnalSym)).add(lSetRefReprNode);
        if (fDbgFlow > 3) {
          flow.dbg(5, " add " + lSetRefReprNode.toStringShort()
          + " to " + lFlowAnalSym.getName()); //##65
        }
        //##73 END
        lDefSyms.add(lFlowAnalSym); //##63
        /*
        if (fDbgFlow > 3) {
          flow.dbg(5, " defSym " + lFlowAnalSym.getName()); //##63
        }
        if (! lSymToDefNode.containsKey(lFlowAnalSym)) {
          // Record the correspondence between the symbol and
          // the reaching definition.
          // It may happen the definition is killed by other definition
          // in this BBlock.
          ((List)lSymToDefNode.get(lFlowAnalSym)).add(lReachSetRefRepr.getIR());
          if (fDbgFlow > 3) {
            flow.dbg(5, " add " + lSetRefReprNode.toStringShort()
              + " to " + lFlowAnalSym.getName()); //##65
          }
        }
        else {
          //
          Set lBBlocksOfReachingDefs = new HashSet();
          for (Iterator lReachIterator
               = ((List)lSymToDefNode.get(lFlowAnalSym)).iterator();
               lReachIterator.hasNext(); ) {
            // Record the BBlock of the reaching def as dominator BBlocks.
            //##63 lDominatorBBlocks.add(fResults.get("BBlockForNode", lDReachIterator.next()));
            HIR lReachingDefNode = (HIR)lReachIterator.next(); //##65
            //##65 lDominatorBBlocks.add(fSubpFlow.getBBlock((HIR)lReachIterator.next())); //##63
            lBBlocksOfReachingDefs.add(fSubpFlow.getBBlock(lReachingDefNode)); //##65
            if (fDbgFlow > 3) {
              flow.dbg(5, "add BBlock of reaching def",
                lReachingDefNode.toStringShort() + " " + lBBlocksOfReachingDefs); //##65
            }
          }
          // Record the correspondence between the symbol and the reaching definition
          // if the reaching definition is not post dominated by the dominators.
          if (!postdominates(lBBlocksOfReachingDefs, lReachSetRefRepr.getBBlock(),
            pBBlock)) {
            ((List)lSymToDefNode.get(lFlowAnalSym)).add(lReachSetRefRepr.
              getIR());
            if (fDbgFlow > 3) {
              flow.dbg(5, " add " + lSetRefReprNode.toStringShort()
                + " to " + lFlowAnalSym.getName()); //##65
            }
          }
        }
        */
      } // End of lReachSetRefRepr.modSyms().iterator()
      /*
      // For each call nodes, treat callModSyms as defined by the call node.
      for (Iterator lCallNodeIt = lReachSetRefRepr.callNodes().iterator();
           lCallNodeIt.hasNext(); ) {
        lCallNode = (IR)lCallNodeIt.next();
        if (fDbgFlow > 3) {
          flow.dbg(5, "lCallNode", lCallNode.toStringShort()); //##65
        }
        for (Iterator lCallModSymsIt = callModSyms(lCallNode, fSubpFlow).
             iterator(); lCallModSymsIt.hasNext(); ) {
          FlowAnalSym lCallModSym = (FlowAnalSym)lCallModSymsIt.next();
          if (fDbgFlow > 3) {
            flow.dbg(5, " lCallModSym " + lCallModSym.getName()); //##63
          }
          if (!lSymToDefNode.containsKey(lCallModSym)) {
            ((List)lSymToDefNode.get(lCallModSym)).add(lCallNode);
          }
          else {
            Set lDominatorBBlocks = new HashSet();
            for (Iterator lReachIterator0 = ((List)lSymToDefNode.get(
              lCallModSym)).iterator(); lReachIterator0.hasNext(); ) {
              //##63 lDominatorBBlocks.add(fResults.get("BBlockForNode", lDReachIterator0.next()));
              lDominatorBBlocks.add(fSubpFlow.getBBlock((HIR)lReachIterator0.
                next())); //##63
            }
            if (!postdominates(lDominatorBBlocks, lReachSetRefRepr.getBBlock(),
              pBBlock)) {
              ((List)lSymToDefNode.get(lCallModSym)).add(lCallNode);
            }
          }
        }
      } // End of for each call node.
      */
    } // End of repeat for each reaching definition.
    //      lDDefSyms.addAll(lSymToDDefNode.keySet());
    if (fDbgFlow > 3) {
      flow.dbg(5, " lDefSyms of reaching def " + lDefSyms.toString()); //##63
      flow.dbg(5, "lSymToDefNode (by reaching def)", lSymToDefNode.toString()); //##65
    }
    //-- For each SetRefRepr of this BBlock, traverse its expressions
    //   and record use-def relations considering call, qualification
    //   and pointer expressions.
    SetRefRepr lSetRefReprOfBBlock = null; //##63
    HIR lNode; //##63
    HIR lUseNode; //##63
    for (Iterator lListIt = ((SetRefReprList)fSubpFlow.getSetRefReprList(pBBlock)).iterator();
         lListIt.hasNext(); ) {
      lSetRefReprOfBBlock = (SetRefRepr)lListIt.next();
      if (lSetRefReprOfBBlock == null)
        continue;
      flow.dbg(5, "\nlSetRefReprOfBBlock " + lSetRefReprOfBBlock.toString());
      List lSetNodeList = new ArrayList(); //##70
      Set lDeferedNodes = new HashSet(); //##70
      // lSetNodes contains nodes that set operand value
      // (such as LHS operand of AssignStmt, mod- and write-operands of AsmStmt).
      // lDeferedNodes contains nodes to be processed later.
      // (LHS operand of AssignStmt, write-operands of AsmStmt should be
      //  treated after processing referencing nodes.)
      HIR lSubtree = (HIR)lSetRefReprOfBBlock.getIR();
      for (HirIterator lHirIt = lSubtree.hirIterator(lSubtree);
           lHirIt.hasNext(); ) {
        lNode = (HIR)lHirIt.next(); //##65
        if (lNode == null) { //##63
          continue; //##63
        }
        Sym lDefSymOfNode = null; //##65
        if (fDbgFlow > 3)
          flow.dbg(5, "\n lNode " + lNode.toStringShort()); //##63
        //##65 if (lSetRefReprOfBBlock.sets()
        //##65     && (lNode.getOperator() != HIR.OP_CALL)) {
        if (lNode instanceof AssignStmt) { //##65
          lSetNodeList.add(lNode.getChild1()); //##70
          lDeferedNodes.add(lNode.getChild1()); //##70
          continue; // AssignStmt node is treated at the end of node loop.
        //##70 BEGIN
        }else if (lNode instanceof AsmStmt) {
          if (((SubpFlowImpl)fSubpFlow).fMultipleSetRef == null) //##85
            ((SubpFlowImpl)fSubpFlow).fMultipleSetRef = new HashMap(); //##85
          if (((SubpFlowImpl)fSubpFlow).fMultipleSetRef.containsKey(lNode)) {
            HirSeq lSeq = (HirSeq)((SubpFlowImpl)fSubpFlow).fMultipleSetRef.get(lNode);
            HirList lSetOperands = (HirList)lSeq.getChild(2);
            for (Iterator lIter = lSetOperands.iterator();
                 lIter.hasNext(); ) {
              lSetNodeList.add(lIter.next());
            }
            HirList lWriteOperands = (HirList)lSeq.getChild(3);
            for (Iterator lIter = lWriteOperands.iterator();
                 lIter.hasNext(); ) {
              HIR lWriteNode = (HIR)lIter.next();
              if (! lSetNodeList.contains(lWriteNode))
                lSetNodeList.add(lWriteNode);
              lDeferedNodes.add(lWriteNode);
             }
          }
          continue;
        //##70 END
        //##65 BEGIN
        }else {
          //##70 if ((lNode.getParent() instanceof AssignStmt)&&
          //##70     (lNode.getParent().getChild1() == lNode))
          if (lDeferedNodes.contains(lNode))  //##70
          {
            // Child 1 node of AssignStmt and
            // write-operand node of AsmStmt should be treated later.
            if (fDbgFlow > 3)
              flow.dbg(5, " skip defered top node " + lNode.toStringShort());
            continue;
          }
        //##65 END
        }
        //##63 fFlowCount = fFlowCount + 1;
        //System.out.println("lFlowCount="+fFlowCount);
        //
        // Fukuda
        //
        // lNode is a use node.
        lUseNode = lNode; //##65
        HIR parentUseNode;
        HIR TopNode;
        TopNode = (HIR)lUseNode;
        lFlowAnalSym = null; //##65
        int op;
        // If QUAL node, get non-QUAL ancestor.
        while (true) {
          parentUseNode = (HIR)((HIR)TopNode).getParent();
          if (parentUseNode == null) {
            break;
          }
          op = parentUseNode.getOperator();
          if (op != HIR.OP_QUAL) {
            break;
          }
          TopNode = parentUseNode;
        } // end of while
        if (TopNode != lUseNode) {
          HIR c = FlowUtil.getQualVarNode(TopNode);
          if (lUseNode != c) {
            continue;
          }
        }
        if (!lCallNodeStack.isEmpty() &&
            !FlowUtil.isUnder((lCallNode = (IR)
          lCallNodeStack.get(lCallNodeStack.size() - 1)), lUseNode)) {
          handleCall(lCallNode, fSubpFlow, lDefSyms, lSymToDefNode);
          lCallNodeStack.remove(lCallNodeStack.size() - 1);
        }
        if ((FlowUtil.flowAnalSym(lUseNode) != null) &&
            !FlowUtil.isDefSymNode(lUseNode) &&
            !FlowUtil.notDereferenced(lUseNode)) {
          lFlowAnalSym = (FlowAnalSym)lUseNode.getSym();
        }
        else {
          if (fDbgFlow > 3)
            flow.dbg(6, " may be use node " + lUseNode.toStringShort()
              + " " + ioRoot.toStringObjectShort(lUseNode.getSymOrExpId())); //###
          if (lUseNode.getSymOrExpId() instanceof FlowAnalSym) {
            lFlowAnalSym = (FlowAnalSym)lUseNode.getSymOrExpId();
          }
          if (FlowUtil.isCall(lUseNode)) {
            lCallNodeStack.add(lUseNode);
            if (fDbgFlow > 3)
              flow.dbg(6, " stack " + lUseNode.toStringShort()); //##63
            continue; // isCall
          }
          else { // Non-call
            if (lFlowAnalSym != null) {
             if ((lFlowAnalSym instanceof ExpId)&&
                 (!fSubpFlow.getMaximalCompoundVars().contains(lFlowAnalSym))) {
               if (fDbgFlow > 3)
                 flow.dbg(6, " ignore non-maximalCompoundVar "
                   + lFlowAnalSym.getName()); //###
               continue;
             }
            }else {
              // Ignore node with non FlowAnalSym.
              continue;
            }
          }
        }
        if (lFlowAnalSym != null) {
          if (fDbgFlow > 3)
            flow.dbg(6, " lUseNode " + lNode.toStringShort() +
              " " + lFlowAnalSym.getName()); //##63
          Type lType = lFlowAnalSym.getSymType();
          //##65 if (kind != Type.KIND_VECTOR && kind != Type.KIND_POINTER)
          if ((lType != null) &&
              (!(lType instanceof VectorType)) &&
              (!(lType instanceof PointerType))) {
            //##63 lDefUseList = (DefUseList) fResults.getRaw("DefUseList", lFlowAnalSym, lSubpFlow);
            //##63 lUseDefList = (UseDefList) fResults.getRaw("UseDefList", lFlowAnalSym, lSubpFlow);
            int CallCount = 0;
            for (Iterator lDefNodeIt = ((List)lSymToDefNode.get(lFlowAnalSym)).
                 iterator();
                 lDefNodeIt.hasNext(); ) {
              lDefNode = (IR)lDefNodeIt.next();
              if (lDefNode == null)
                continue;
              if (fDbgFlow > 3)
                flow.dbg(6, " lDefNode " + lDefNode.toStringShort()); //##63
              if (lDefNode.getOperator() == HIR.OP_CALL) {
                CallCount = CallCount + 1;
                if (CallCount > 2) {
                  continue; // Skip this DefNode
                }
              }
              else { // Not call
                //##65.1 if (lDefNode instanceof AssignStmt) {
                  // Exclude the case of defining in the same AssignStmt.
                //##65.1   if (lDefNode == FlowUtil.getAncestorAssign(lUseNode))
                //##65.1     continue;
                //##65.1 }
                ((DefUseListImpl)lDefUseList).getOrAddDefUseChain(lDefNode).
                  addUseNode(lUseNode);
                if (fDbgFlow > 3)
                  flow.dbg(6, " addUseNode " + lUseNode.toStringShort()); //##63
              }
              ((UseDefListImpl)lUseDefList).getOrAddUseDefChain(lUseNode)
                .addDefNode(lDefNode); //##63
              if (fDbgFlow > 3) {
                flow.dbg(6, " addDefNode " + lDefNode.toStringShort()); //##63
              }
            }
          } // End of if(kind != Type.KIND_VECTOR && kind != Type.KIND_POINTER)
          //##65 BEGIN
          else { // VectorType || PointerType || lType == null
            int lOperator = lUseNode.getOperator();
            if ((lOperator == HIR.OP_SUBS) ||
                (lOperator == HIR.OP_QUAL) ||
                (lOperator == HIR.OP_ARROW)) {
              ExpId lCompoundExpId = ((HIR)lUseNode).getExpId();
              if (((SubpFlowImpl)fSubpFlow).getMaximalCompoundVars().
                  contains(lCompoundExpId)) {
                int CallCount = 0;
                for (Iterator lDefNodeIt = ((List)lSymToDefNode.get(
                  lCompoundExpId)).
                  iterator();
                  lDefNodeIt.hasNext(); ) {
                  lDefNode = (IR)lDefNodeIt.next();
                  if (lDefNode == null) {
                    continue;
                  }
                  if (fDbgFlow > 3) {
                    flow.dbg(6, " lDefNode " + lDefNode.toStringShort()); //##63
                  }
                  if (lDefNode.getOperator() == HIR.OP_CALL) {
                    CallCount = CallCount + 1;
                    if (CallCount > 2) {
                      continue;
                    }
                  }
                  //##65 if (lDefNode.getOperator() != HIR.OP_CALL) {
                  else { //##65
                    //##65.1 if (lDefNode instanceof AssignStmt) {
                      // Exclude the case of defining in the same AssignStmt.
                    //##65.1   if (lDefNode == FlowUtil.getAncestorAssign(lUseNode))
                    //##65.1     continue;
                    //##65.1 }
                    ((DefUseListImpl)lDefUseList).getOrAddDefUseChain(lDefNode).
                      addUseNode(lUseNode);
                    if (fDbgFlow > 3) {
                      flow.dbg(6, " addUseNode " + lUseNode.toStringShort()); //##63
                    }
                  }
                  ((UseDefListImpl)lUseDefList).getOrAddUseDefChain(lUseNode)
                    .addDefNode(lDefNode);
                  if (fDbgFlow > 3) {
                    flow.dbg(6, " addDefNode " + lDefNode.toStringShort()); //##63
                  }
                }
              }
            }
          }
          //##65 END
          if (!lDefSyms.contains(lFlowAnalSym)) { // Definite assignment check.
            if (fDbgFlow > 3) {
              flow.dbg(5, "lDefSyms " + lDefSyms.toString()
                + " does not contain " + lFlowAnalSym.getName()); //##63
              //##63 lDefUseList = (DefUseList) fResults.getRaw("DefUseList", lFlowAnalSym, lSubpFlow);
              //##63 lUseDefList = (UseDefList)fResults.getRaw("UseDefList", lFlowAnalSym,
            }
            if (lFlowAnalSym.getSymKind() == Sym.KIND_PARAM) {
              ((DefUseListImpl)lDefUseList).getOrAddDefUseChain(
                fSubpFlow.getFlowAdapter().dummyUninitialization)
                .addUseNode(lUseNode); //##63
              ((UseDefListImpl)lUseDefList).getOrAddUseDefChain(lUseNode). //##63
                addDefNode(fSubpFlow.getFlowAdapter().dummySettingByParam); //##63
            }
            else {
              // Fukuda
              if (lDefUseList == null) {
                continue;
              }
              // Fukuda:
              //##63 ((DefUseListImpl)lDefUseList).getOrAddDefUseCell(
              //##63 DefUseCell.UNINITIALIZED).addUseNode(lUseNode);
              ((DefUseListImpl)lDefUseList).getOrAddDefUseChain(
                fSubpFlow.getFlowAdapter().dummyUninitialization).addUseNode(
                lUseNode); //##63
              //##63 ((UseDefImpl)lUseDefList).getOrAddUDChain(lUseNode).addDefNode(UDChain.UNINITIALIZED);
              ((UseDefListImpl)lUseDefList).getOrAddUseDefChain(lUseNode). //##63
                addDefNode(fSubpFlow.getFlowAdapter().dummyUninitialization); //##63
            }
          } // End of !lDefSyms.contains(lFlowAnalSym)
        } // lFlowAnalSym != null
      } // End of node iteration
      //##65 BEGIN
      if ((lSubtree instanceof AssignStmt)||
          (lSubtree instanceof AsmStmt)) {
        // Operand nodes setting value are treated at the end of node loop.
        for (Iterator lSetIter = lSetNodeList.iterator();
             lSetIter.hasNext(); ) {
          HIR lSetNode = (HIR)lSetIter.next();
          FlowAnalSym lSymAssigned;
          if (lSubtree instanceof AssignStmt)
            lSymAssigned = lSetRefReprOfBBlock.getDefSym();
          else
            lSymAssigned = lSetNode.getSymOrExpId();
        //##92 BEGIN
        if (lSymAssigned == null) {
          if (fDbgFlow > 3)
              flow.dbg(4, " set operand", " is null");
          continue;
        }
        //##92 END
        if (fDbgFlow > 3)
            flow.dbg(5, "set operand", lSymAssigned.getName());
        if (lSymAssigned instanceof ExpId) {
          // The defSym of AssignStmt is the child 1 or ExpId of the child 1.
          // (lDefSymOfNode instanceof ExpId) means that the child 1 is
          // a compound variable.
          if (((ExpId)lSymAssigned).getExpInf().getRValueExpId() != null) {
            // Record r-value ExpId corresponding to ExpId of child 1
            // so that def-sym and use-sym represent the same ExpId.
            lSymAssigned = ((ExpId)lSymAssigned).getExpInf().
              getRValueExpId();
            if (fDbgFlow > 3)
              flow.dbg(5, " defSym as r-value " + lSymAssigned.getName()); //##65
          }
        }
        if (fDbgFlow > 3)
          flow.dbg(5, "AssignStmt clear previous def nodes of defSym ",
            lSymAssigned.getName()); //##65
        lSymToDefNode.removeAllEntries(lSymAssigned);
        if (fDbgFlow > 3)
          flow.dbg(5, " record as DefNode " + lSubtree.toStringShort()); //##65
        lDefSyms.add(lSymAssigned);
        ((List)lSymToDefNode.get(lSymAssigned)).add(lSubtree);
      }
    }
      //##65 END
      //##63 } // End of getSetRefReprList(pBBlock)).iterator()
      if (lSetRefReprOfBBlock != null) { //##63
        HIR lHirOfSetRefREprOfBBlock = (HIR)lSetRefReprOfBBlock.getIR(); //##65
        while (!lCallNodeStack.isEmpty()) {
          lCallNode = (IR)lCallNodeStack.get(lCallNodeStack.size() - 1);
          handleCall(lCallNode, fSubpFlow, lDefSyms, lSymToDefNode);
          lCallNodeStack.remove(lCallNodeStack.size() - 1);
        }
        if (lSetRefReprOfBBlock.sets()) {
          lDefSym = lSetRefReprOfBBlock.defSym();
          if (fDbgFlow > 3) {
            flow.dbg(6,
              "lSetRefRepr of " + lHirOfSetRefREprOfBBlock.toStringShort(),
              " set " + lDefSym); //##63
          }
          if (lDefSym != null) {
            ((List)lSymToDefNode.get(lDefSym)).clear();
            lDefSyms.add(lDefSym);
            ((List)lSymToDefNode.get(lDefSym)).add(lHirOfSetRefREprOfBBlock); //##65
            if (fDbgFlow > 3) {
              flow.dbg(6, " add " + lHirOfSetRefREprOfBBlock.toStringShort() +
                " to " + lDefSym.getName());
            }
            if (((HIR)lSetRefReprOfBBlock.getIR()).getOperator() != HIR.OP_CALL) {

              //##73 ((DefUseListImpl)fSubpFlow.getDefUseList()). //##63
              ((DefUseListImpl)((SubpFlowImpl)fSubpFlow).getListOfDefUseList()). //##73
                getOrAddDefUseChain(lHirOfSetRefREprOfBBlock); //##65
            }
            fSubpFlow.getDefinedSyms().add(lDefSym); //##63
          }
          else { // lDefSym == null
            for (Iterator lModIt = (lSetRefReprOfBBlock).modSyms().iterator();
                 lModIt.hasNext(); ) {
              lFlowAnalSym = (FlowAnalSym)lModIt.next();
              ((List)lSymToDefNode.get(lFlowAnalSym)).add(
                lHirOfSetRefREprOfBBlock); //##65
              if (fDbgFlow > 3) {
                flow.dbg(6, " add " + lHirOfSetRefREprOfBBlock.toStringShort() +
                  " to " + lFlowAnalSym.getName());
              }
              if (((HIR)lSetRefReprOfBBlock.getIR()).getOperator() !=
                  HIR.OP_CALL) {

                //##73 ((DefUseListImpl)fSubpFlow.getDefUseList()) //##63
                ((DefUseListImpl)((SubpFlowImpl)fSubpFlow).getListOfDefUseList()) //##73
                  .getOrAddDefUseChain(lHirOfSetRefREprOfBBlock); //##65
              }
              fSubpFlow.getDefinedSyms().add(lFlowAnalSym); //##63
            }
          }
        } // End of lSetRefRepr.sets()
      } // BBlock setRefReprList
    } // End of subtree iteration
  } // findUseDef

  /** findUseDefObsolete
   * Compute both DefUse relations and UseDef relations for pBBlock.
   * @param pBBlock
   */
  /*
  protected void
    findUseDefObsolete(BBlock pBBlock)
  {
    SubpFlow lSubpFlow = pBBlock.getSubpFlow();
    if (fDbgFlow > 3) {
      flow.dbg(5, "\nfindUseDef B" +pBBlock.getBBlockNumber());
      //##63 SetRefRepr lSetRefRepr;
      //##63 IR lUseNode;
    }
    IR lDefNode;
    FlowAnalSym lDefSym, lFlowAnalSym;
    DefVector lReach;
    DefVectorIterator lReachIt;
    SetRefRepr lReachSetRefRepr;
    DefUseList lDefUseList;
    UseDefList lUseDefList;
    int lPos;
    IR lCallNode;
    IR lSetRefReprNode; //##65
    Set lFModFlowAnalSyms = new HashSet();
    ArrayList lCallNodeStack = new ArrayList();
    //##63 FAList lSymIndexTable = lSubpFlow.getSymIndexTable();
    // lSymToDefNode: Map symbol to node that may define the symbol
    //   (It may happen the definition is killed by oter definition ?)
    ListValuedMap lSymToDefNode = new ListValuedMap();
    //##73 lDefUseList = (DefUseList)fSubpFlow.getDefUseList(); //##63
    lDefUseList = (DefUseList)((SubpFlowImpl)fSubpFlow).getListOfDefUseList(); //##73
    //##73 lUseDefList = (UseDefList)fSubpFlow.getUseDefList(); //##63
    lUseDefList = (UseDefList)((SubpFlowImpl)fSubpFlow).getListOfUseDefList(); //##73
    Set lDefSyms = new HashSet(); // Symbols defined in this BBlock ?
    Set lUsedSyms = pBBlock.getUsed().flowAnalSyms(); // Symbols used
                                  // in this BBlock. //##73
    lReach = pBBlock.getReach();
    lReachIt = lReach.defVectorIterator();
    //-- For each reaching definition, record it as the def-node
    //   of symbols possibly modified by the definition.
    while (true) { // Repeat for each reaching definition.
      // Get SetRefRepr of the reaching definition.
      lReachSetRefRepr = lReachIt.nextSetRefRepr();
      if (lReachSetRefRepr == null) {
        break;
      }
      lSetRefReprNode = lReachSetRefRepr.getIR(); //##65
      if (fDbgFlow > 3) {
        flow.dbg(5, "lReachSetRefRepr",
                 "modSyms" + lReachSetRefRepr.modSyms() + " "
                 + lSetRefReprNode.toStringShort()); //##65
      }
      for (Iterator lDefSymIt = lReachSetRefRepr.modSyms().iterator();
           lDefSymIt.hasNext(); ) {
        // Repeat for each symbol possibly modified by the definition
        lFlowAnalSym = (FlowAnalSym)lDefSymIt.next();
        if ((lFlowAnalSym instanceof ExpId) &&
            (!fSubpFlow.getMaximalCompoundVars().contains(lFlowAnalSym))) { //##65
          continue; //##65
        }
        // If the symbol (set by the reaching definition)
        // is not used in this BBlock, skip it. //##73
        if (! lUsedSyms.contains(lFlowAnalSym)) //##73
          continue; //##73
        lDefSyms.add(lFlowAnalSym); //##63
        if (fDbgFlow > 3) {
          flow.dbg(5, " defSym " + lFlowAnalSym.getName()); //##63
        }
        if (! lSymToDefNode.containsKey(lFlowAnalSym)) {
          // Record the correspondence between the symbol and the reaching definition.
          // Restrict reaching def defining the symbol ??
          // It may happen the definition is killed by other definition in this BBlock ??
          ((List)lSymToDefNode.get(lFlowAnalSym)).add(lReachSetRefRepr.getIR());
          if (fDbgFlow > 3) {
            flow.dbg(5, " add " + lSetRefReprNode.toStringShort()
              + " to " + lFlowAnalSym.getName()); //##65
          }
        }
        else {
          Set lBBlocksOfReachingDefs = new HashSet();
          for (Iterator lReachIterator
               = ((List)lSymToDefNode.get(lFlowAnalSym)).iterator();
               lReachIterator.hasNext(); ) {
            // Record the BBlock of the reaching def as dominator BBlocks.
            //##63 lDominatorBBlocks.add(fResults.get("BBlockForNode", lDReachIterator.next()));
            HIR lReachingDefNode = (HIR)lReachIterator.next(); //##65
            //##65 lDominatorBBlocks.add(fSubpFlow.getBBlock((HIR)lReachIterator.next())); //##63
            lBBlocksOfReachingDefs.add(fSubpFlow.getBBlock(lReachingDefNode)); //##65
            if (fDbgFlow > 3) {
              flow.dbg(5, "add BBlock of reaching def",
                lReachingDefNode.toStringShort() + " " + lBBlocksOfReachingDefs); //##65
            }
          }
          // Record the correspondence between the symbol and the reaching definition
          // if the reaching definition is not post dominated by the dominators.
          if (!postdominates(lBBlocksOfReachingDefs, lReachSetRefRepr.getBBlock(),
            pBBlock)) {
            ((List)lSymToDefNode.get(lFlowAnalSym)).add(lReachSetRefRepr.
              getIR());
            if (fDbgFlow > 3) {
              flow.dbg(5, " add " + lSetRefReprNode.toStringShort()
                + " to " + lFlowAnalSym.getName()); //##65
            }
          }
        }
      } // End of lReachSetRefRepr.modSyms().iterator()
      // For each call nodes, treat callModSyms as defined by the call node.
      for (Iterator lCallNodeIt = lReachSetRefRepr.callNodes().iterator();
           lCallNodeIt.hasNext(); ) {
        lCallNode = (IR)lCallNodeIt.next();
        if (fDbgFlow > 3) {
          flow.dbg(5, "lCallNode", lCallNode.toStringShort()); //##65
        }
        for (Iterator lCallModSymsIt = callModSyms(lCallNode, lSubpFlow).
             iterator(); lCallModSymsIt.hasNext(); ) {
          FlowAnalSym lCallModSym = (FlowAnalSym)lCallModSymsIt.next();
          if (fDbgFlow > 3) {
            flow.dbg(5, " lCallModSym " + lCallModSym.getName()); //##63
          }
          if (!lSymToDefNode.containsKey(lCallModSym)) {
            ((List)lSymToDefNode.get(lCallModSym)).add(lCallNode);
          }
          else {
            Set lDominatorBBlocks = new HashSet();
            for (Iterator lReachIterator0 = ((List)lSymToDefNode.get(
              lCallModSym)).iterator(); lReachIterator0.hasNext(); ) {
              //##63 lDominatorBBlocks.add(fResults.get("BBlockForNode", lDReachIterator0.next()));
              lDominatorBBlocks.add(fSubpFlow.getBBlock((HIR)lReachIterator0.
                next())); //##63
            }
            if (!postdominates(lDominatorBBlocks, lReachSetRefRepr.getBBlock(),
              pBBlock)) {
              ((List)lSymToDefNode.get(lCallModSym)).add(lCallNode);
            }
          }
        }
      } // End of for each call node.
    } // End of repeat for each reaching definition.
    //      lDDefSyms.addAll(lSymToDDefNode.keySet());
    if (fDbgFlow > 3) {
      flow.dbg(5, " lDefSyms of reaching def " + lDefSyms.toString()); //##63
      flow.dbg(5, "lSymToDefNode", lSymToDefNode.toString()); //##65
    }
    //-- For each SetRefRepr of this BBlock, traverse its expressions
    //   and record use-def relations considering call, qualification
    //   and pointer expressions.
    SetRefRepr lSetRefReprOfBBlock = null; //##63
    HIR lNode; //##63
    HIR lUseNode; //##63
    for (Iterator lListIt = ((SetRefReprList)fSubpFlow.getSetRefReprList(pBBlock)).iterator();
         lListIt.hasNext(); ) {
      lSetRefReprOfBBlock = (SetRefRepr)lListIt.next();
      if (lSetRefReprOfBBlock == null)
        continue;
      flow.dbg(5, "\nlSetRefReprOfBBlock " + lSetRefReprOfBBlock.toString());
      List lSetNodeList = new ArrayList(); //##70
      Set lDeferedNodes = new HashSet(); //##70
      // lSetNodes contains nodes that set operand value
      // (such as LHS operand of AssignStmt, mod- and write-operands of AsmStmt).
      // lDeferedNodes contains nodes to be processed later.
      // (LHS operand of AssignStmt, write-operands of AsmStmt should be
      //  treated after processing referencing nodes.)
      HIR lSubtree = (HIR)lSetRefReprOfBBlock.getIR();
      for (HirIterator lHirIt = lSubtree.hirIterator(lSubtree);
           lHirIt.hasNext(); ) {
        lNode = (HIR)lHirIt.next(); //##65
        if (lNode == null) { //##63
          continue; //##63
        }
        Sym lDefSymOfNode = null; //##65
        if (fDbgFlow > 3)
          flow.dbg(5, "\n lNode " + lNode.toStringShort()); //##63
        //##65 if (lSetRefReprOfBBlock.sets()
        //##65     && (lNode.getOperator() != HIR.OP_CALL)) {
        if (lNode instanceof AssignStmt) { //##65
          lSetNodeList.add(lNode.getChild1()); //##70
          lDeferedNodes.add(lNode.getChild1()); //##70
          continue; // AssignStmt node is treated at the end of node loop.
        //##70 BEGIN
        }else if (lNode instanceof AsmStmt) {
          if (((SubpFlowImpl)fSubpFlow).fMultipleSetRef.containsKey(lNode)) {
            HirSeq lSeq = (HirSeq)((SubpFlowImpl)fSubpFlow).fMultipleSetRef.get(lNode);
            HirList lSetOperands = (HirList)lSeq.getChild(2);
            for (Iterator lIter = lSetOperands.iterator();
                 lIter.hasNext(); ) {
              lSetNodeList.add(lIter.next());
            }
            HirList lWriteOperands = (HirList)lSeq.getChild(3);
            for (Iterator lIter = lWriteOperands.iterator();
                 lIter.hasNext(); ) {
              HIR lWriteNode = (HIR)lIter.next();
              if (! lSetNodeList.contains(lWriteNode))
                lSetNodeList.add(lWriteNode);
              lDeferedNodes.add(lWriteNode);
             }
          }
          continue;
        //##70 END
        //##65 BEGIN
        }else {
          //##70 if ((lNode.getParent() instanceof AssignStmt)&&
          //##70     (lNode.getParent().getChild1() == lNode))
          if (lDeferedNodes.contains(lNode))  //##70
          {
            // Child 1 node of AssignStmt and
            // write-operand node of AsmStmt should be treated later.
            if (fDbgFlow > 3)
              flow.dbg(5, " skip defered top node " + lNode.toStringShort());
            continue;
          }
        //##65 END
        }
        //##63 fFlowCount = fFlowCount + 1;
        //System.out.println("lFlowCount="+fFlowCount);
        //
        // Fukuda
        //
        // lNode is a use node.
        lUseNode = lNode; //##65
        HIR parentUseNode;
        HIR TopNode;
        TopNode = (HIR)lUseNode;
        lFlowAnalSym = null; //##65
        int op;
        // If QUAL node, get non-QUAL ancestor.
        while (true) {
          parentUseNode = (HIR)((HIR)TopNode).getParent();
          if (parentUseNode == null) {
            break;
          }
          op = parentUseNode.getOperator();
          if (op != HIR.OP_QUAL) {
            break;
          }
          TopNode = parentUseNode;
        } // end of while
        if (TopNode != lUseNode) {
          HIR c = FlowUtil.getQualVarNode(TopNode);
          if (lUseNode != c) {
            continue;
          }
        }
        if (!lCallNodeStack.isEmpty() &&
            !FlowUtil.isUnder((lCallNode = (IR)
          lCallNodeStack.get(lCallNodeStack.size() - 1)), lUseNode)) {
          handleCall(lCallNode, lSubpFlow, lDefSyms, lSymToDefNode);
          lCallNodeStack.remove(lCallNodeStack.size() - 1);
        }
        if ((FlowUtil.flowAnalSym(lUseNode) != null) &&
            !FlowUtil.isDefSymNode(lUseNode) &&
            !FlowUtil.notDereferenced(lUseNode)) {
          lFlowAnalSym = (FlowAnalSym)lUseNode.getSym();
        }
        else {
          if (fDbgFlow > 3)
            flow.dbg(6, " may be use node " + lUseNode.toStringShort()
              + " " + ioRoot.toStringObjectShort(lUseNode.getSymOrExpId())); //###
          if (lUseNode.getSymOrExpId() instanceof FlowAnalSym) {
            lFlowAnalSym = (FlowAnalSym)lUseNode.getSymOrExpId();
          }
          if (FlowUtil.isCall(lUseNode)) {
            lCallNodeStack.add(lUseNode);
            if (fDbgFlow > 3)
              flow.dbg(6, " stack " + lUseNode.toStringShort()); //##63
            continue; // isCall
          }
          else { // Non-call
            if (lFlowAnalSym != null) {
             if ((lFlowAnalSym instanceof ExpId)&&
                 (!fSubpFlow.getMaximalCompoundVars().contains(lFlowAnalSym))) {
               if (fDbgFlow > 3)
                 flow.dbg(6, " ignore non-maximalCompoundVar "
                   + lFlowAnalSym.getName()); //###
               continue;
             }
            }else {
              // Ignore node with non FlowAnalSym.
              continue;
            }
          }
        }
        if (lFlowAnalSym != null) {
          if (fDbgFlow > 3)
            flow.dbg(6, " lUseNode " + lNode.toStringShort() +
              " " + lFlowAnalSym.getName()); //##63
          Type lType = lFlowAnalSym.getSymType();
          //##65 if (kind != Type.KIND_VECTOR && kind != Type.KIND_POINTER)
          if ((lType != null) &&
              (!(lType instanceof VectorType)) &&
              (!(lType instanceof PointerType))) {
            //##63 lDefUseList = (DefUseList) fResults.getRaw("DefUseList", lFlowAnalSym, lSubpFlow);
            //##63 lUseDefList = (UseDefList) fResults.getRaw("UseDefList", lFlowAnalSym, lSubpFlow);
            int CallCount = 0;
            for (Iterator lDefNodeIt = ((List)lSymToDefNode.get(lFlowAnalSym)).
                 iterator();
                 lDefNodeIt.hasNext(); ) {
              lDefNode = (IR)lDefNodeIt.next();
              if (lDefNode == null)
                continue;
              if (fDbgFlow > 3)
                flow.dbg(6, " lDefNode " + lDefNode.toStringShort()); //##63
              if (lDefNode.getOperator() == HIR.OP_CALL) {
                CallCount = CallCount + 1;
                if (CallCount > 2) {
                  continue; // Skip this DefNode
                }
              }
              else { // Not call
                //##65.1 if (lDefNode instanceof AssignStmt) {
                  // Exclude the case of defining in the same AssignStmt.
                //##65.1   if (lDefNode == FlowUtil.getAncestorAssign(lUseNode))
                //##65.1     continue;
                //##65.1 }
                ((DefUseListImpl)lDefUseList).getOrAddDefUseChain(lDefNode).
                  addUseNode(lUseNode);
                if (fDbgFlow > 3)
                  flow.dbg(6, " addUseNode " + lUseNode.toStringShort()); //##63
              }
              ((UseDefListImpl)lUseDefList).getOrAddUseDefChain(lUseNode)
                .addDefNode(lDefNode); //##63
              if (fDbgFlow > 3) {
                flow.dbg(6, " addDefNode " + lDefNode.toStringShort()); //##63
              }
            }
          } // End of if(kind != Type.KIND_VECTOR && kind != Type.KIND_POINTER)
          //##65 BEGIN
          else { // VectorType || PointerType || lType == null
            int lOperator = lUseNode.getOperator();
            if ((lOperator == HIR.OP_SUBS) ||
                (lOperator == HIR.OP_QUAL) ||
                (lOperator == HIR.OP_ARROW)) {
              ExpId lCompoundExpId = ((HIR)lUseNode).getExpId();
              if (((SubpFlowImpl)fSubpFlow).getMaximalCompoundVars().
                  contains(lCompoundExpId)) {
                int CallCount = 0;
                for (Iterator lDefNodeIt = ((List)lSymToDefNode.get(
                  lCompoundExpId)).
                  iterator();
                  lDefNodeIt.hasNext(); ) {
                  lDefNode = (IR)lDefNodeIt.next();
                  if (lDefNode == null) {
                    continue;
                  }
                  if (fDbgFlow > 3) {
                    flow.dbg(6, " lDefNode " + lDefNode.toStringShort()); //##63
                  }
                  if (lDefNode.getOperator() == HIR.OP_CALL) {
                    CallCount = CallCount + 1;
                    if (CallCount > 2) {
                      continue;
                    }
                  }
                  //##65 if (lDefNode.getOperator() != HIR.OP_CALL) {
                  else { //##65
                    //##65.1 if (lDefNode instanceof AssignStmt) {
                      // Exclude the case of defining in the same AssignStmt.
                    //##65.1   if (lDefNode == FlowUtil.getAncestorAssign(lUseNode))
                    //##65.1     continue;
                    //##65.1 }
                    ((DefUseListImpl)lDefUseList).getOrAddDefUseChain(lDefNode).
                      addUseNode(lUseNode);
                    if (fDbgFlow > 3) {
                      flow.dbg(6, " addUseNode " + lUseNode.toStringShort()); //##63
                    }
                  }
                  ((UseDefListImpl)lUseDefList).getOrAddUseDefChain(lUseNode)
                    .addDefNode(lDefNode);
                  if (fDbgFlow > 3) {
                    flow.dbg(6, " addDefNode " + lDefNode.toStringShort()); //##63
                  }
                }
              }
            }
          }
          //##65 END
          if (!lDefSyms.contains(lFlowAnalSym)) { // Definite assignment check.
            if (fDbgFlow > 3) {
              flow.dbg(5, "lDefSyms " + lDefSyms.toString()
                + " does not contain " + lFlowAnalSym.getName()); //##63
              //##63 lDefUseList = (DefUseList) fResults.getRaw("DefUseList", lFlowAnalSym, lSubpFlow);
              //##63 lUseDefList = (UseDefList)fResults.getRaw("UseDefList", lFlowAnalSym,
            }
            if (lFlowAnalSym.getSymKind() == Sym.KIND_PARAM) {
              ((DefUseListImpl)lDefUseList).getOrAddDefUseChain(
                fSubpFlow.getFlowAdapter().dummyUninitialization)
                .addUseNode(lUseNode); //##63
              ((UseDefListImpl)lUseDefList).getOrAddUseDefChain(lUseNode). //##63
                addDefNode(fSubpFlow.getFlowAdapter().dummySettingByParam); //##63
            }
            else {
              // Fukuda
              if (lDefUseList == null) {
                continue;
              }
              // Fukuda:
              //##63 ((DefUseListImpl)lDefUseList).getOrAddDefUseCell(
              //##63 DefUseCell.UNINITIALIZED).addUseNode(lUseNode);
              ((DefUseListImpl)lDefUseList).getOrAddDefUseChain(
                fSubpFlow.getFlowAdapter().dummyUninitialization).addUseNode(
                lUseNode); //##63
              //##63 ((UseDefImpl)lUseDefList).getOrAddUDChain(lUseNode).addDefNode(UDChain.UNINITIALIZED);
              ((UseDefListImpl)lUseDefList).getOrAddUseDefChain(lUseNode). //##63
                addDefNode(fSubpFlow.getFlowAdapter().dummyUninitialization); //##63
            }
          } // End of !lDefSyms.contains(lFlowAnalSym)
        } // lFlowAnalSym != null
      } // End of node iteration
      //##65 BEGIN
      if ((lSubtree instanceof AssignStmt)||
          (lSubtree instanceof AsmStmt)) {
        // Operand nodes setting value are treated at the end of node loop.
        for (Iterator lSetIter = lSetNodeList.iterator();
             lSetIter.hasNext(); ) {
          HIR lSetNode = (HIR)lSetIter.next();
          FlowAnalSym lSymAssigned;
          if (lSubtree instanceof AssignStmt)
            lSymAssigned = lSetRefReprOfBBlock.getDefSym();
          else
            lSymAssigned = lSetNode.getSymOrExpId();
        if (fDbgFlow > 3)
          flow.dbg(5, "set operand", lSymAssigned.getName());
        if (lSymAssigned instanceof ExpId) {
          // The defSym of AssignStmt is the child 1 or ExpId of the child 1.
          // (lDefSymOfNode instanceof ExpId) means that the child 1 is
          // a compound variable.
          if (((ExpId)lSymAssigned).getExpInf().getRValueExpId() != null) {
            // Record r-value ExpId corresponding to ExpId of child 1
            // so that def-sym and use-sym represent the same ExpId.
            lSymAssigned = ((ExpId)lSymAssigned).getExpInf().
              getRValueExpId();
            if (fDbgFlow > 3)
              flow.dbg(5, " defSym as r-value " + lSymAssigned.getName()); //##65
          }
        }
        if (fDbgFlow > 3)
          flow.dbg(5, "AssignStmt clear previous def nodes of defSym ",
            lSymAssigned.getName()); //##65
        lSymToDefNode.removeAllEntries(lSymAssigned);
        if (fDbgFlow > 3)
          flow.dbg(5, " record as DefNode " + lSubtree.toStringShort()); //##65
        lDefSyms.add(lSymAssigned);
        ((List)lSymToDefNode.get(lSymAssigned)).add(lSubtree);
      }
    }
      //##65 END
      //##63 } // End of getSetRefReprList(pBBlock)).iterator()
      if (lSetRefReprOfBBlock != null) { //##63
        HIR lHirOfSetRefREprOfBBlock = (HIR)lSetRefReprOfBBlock.getIR(); //##65
        while (!lCallNodeStack.isEmpty()) {
          lCallNode = (IR)lCallNodeStack.get(lCallNodeStack.size() - 1);
          handleCall(lCallNode, lSubpFlow, lDefSyms, lSymToDefNode);
          lCallNodeStack.remove(lCallNodeStack.size() - 1);
        }
        if (lSetRefReprOfBBlock.sets()) {
          lDefSym = lSetRefReprOfBBlock.defSym();
          if (fDbgFlow > 3) {
            flow.dbg(6,
              "lSetRefRepr of " + lHirOfSetRefREprOfBBlock.toStringShort(),
              " set " + lDefSym); //##63
          }
          if (lDefSym != null) {
            ((List)lSymToDefNode.get(lDefSym)).clear();
            lDefSyms.add(lDefSym);
            ((List)lSymToDefNode.get(lDefSym)).add(lHirOfSetRefREprOfBBlock); //##65
            if (fDbgFlow > 3) {
              flow.dbg(6, " add " + lHirOfSetRefREprOfBBlock.toStringShort() +
                " to " + lDefSym.getName());
            }
            if (((HIR)lSetRefReprOfBBlock.getIR()).getOperator() != HIR.OP_CALL) {
              ((DefUseListImpl)fSubpFlow.getDefUseList()). //##63
                getOrAddDefUseChain(lHirOfSetRefREprOfBBlock); //##65
            }
            fSubpFlow.getDefinedSyms().add(lDefSym); //##63
          }
          else { // lDefSym == null
            for (Iterator lModIt = (lSetRefReprOfBBlock).modSyms().iterator();
                 lModIt.hasNext(); ) {
              lFlowAnalSym = (FlowAnalSym)lModIt.next();
              ((List)lSymToDefNode.get(lFlowAnalSym)).add(
                lHirOfSetRefREprOfBBlock); //##65
              if (fDbgFlow > 3) {
                flow.dbg(6, " add " + lHirOfSetRefREprOfBBlock.toStringShort() +
                  " to " + lFlowAnalSym.getName());
              }
              if (((HIR)lSetRefReprOfBBlock.getIR()).getOperator() !=
                  HIR.OP_CALL) {
                ((DefUseListImpl)fSubpFlow.getDefUseList()) //##63
                  .getOrAddDefUseChain(lHirOfSetRefREprOfBBlock); //##65
              }
              fSubpFlow.getDefinedSyms().add(lFlowAnalSym); //##63
            }
          }
        } // End of lSetRefRepr.sets()
      } // BBlock setRefReprList
    } // End of subtree iteration
  } // findUseDefObsolete
*/

  protected void
    handleCall(IR pCallNode, SubpFlow pSubpFlow, Set pDDefSyms,
               ListValuedMap pSymToPDefNode)
  {
    FlowAnalSym lFlowAnalSym;
    if (fDbgFlow > 3) {
      flow.dbg(6, "handleCall " + pCallNode.toStringShort()); //##63
      //##63 for (Iterator lIt = pSymToPDefNode.keySet().iterator();
    }
    for (Iterator lIt = fSubpFlow.setOfGlobalVariables().iterator();
         lIt.hasNext(); ) {
      lFlowAnalSym = (FlowAnalSym)lIt.next();
      pSymToPDefNode.addUnique(lFlowAnalSym, pCallNode);
    }
    if (fDbgFlow > 3) {
      flow.dbg(6, "SymToPDefNode", pSymToPDefNode.toString()); //##65
    }
  } // handleCall

  protected Set
    callModSyms(IR pCallNode, SubpFlow pCurrentSubpFlow)
  {
    //##63 return new HashSet(pCurrentSubpFlow.getSymIndexTable());
    return pCurrentSubpFlow.getSetRefReprOfIR(pCallNode).modSyms00();
  } // callModSyms

  private static boolean postdominates(Set pPostdominatorBBlocks,
    BBlock pDominatedBBlock, BBlock pExitBBlock)
  {
    final Object lVisitedFlag = new Object();
    //	if (pDominatedBBlock == pExitBBlock && pPostdominatorBBlocks.contains(pDominatedBBlock))
    //		return true;
    return !search(pDominatedBBlock, pExitBBlock, pPostdominatorBBlocks,
                  lVisitedFlag);
  }

  private static boolean search(BBlock pCurrent, final BBlock pGoal,
    final Set pObstacles, final Object pVisitedFlag)
  {
    //  if (pObstacles.contains(pCurrent)) return false;
    //	if (pCurrrent == pGoal)	return true;
    if (pObstacles.contains(pCurrent)) {
      return false;
    }
    List lSuccList = pCurrent.getSuccList();
    BBlock lSuccBBlock;
    for (Iterator lIt = lSuccList.iterator(); lIt.hasNext(); ) {
      lSuccBBlock = (BBlock)lIt.next();
      if (lSuccBBlock == pGoal) {
        return true;
      }
      else if (lSuccBBlock.getWork() != pVisitedFlag) {
        lSuccBBlock.setWork(pVisitedFlag);
        if (search(lSuccBBlock, pGoal, pObstacles, pVisitedFlag)) {
          return true;
        }
      }
    }
    return false;
  }

//##63 END
  /**
   * Finds and sets all the data flow information.
   * This method consists of findAllBitVectors and findDefUse.
   *
   * See #findAllBitVectors()
   * See #findDefUse()
   */
  public void findAll()
  {
    findAllBitVectors();
    findDefUse();
    findReach(); //##60
  }

  /**
   * Shows the Def vector for every BBlock.
   *
   * See #findDef()
   * See ShowDataFlow#showDef()
   */
  public void showDef()
  {
    fShowDataFlow.showDef();
  }

  /**
   * Shows the Kill vector for every BBlock.
   *
   * See #findKill()
   * See ShowDataFlow#showKill()
   */
  public void showKill()
  {
    fShowDataFlow.showKill();
  }

  /**
   * Shows the Reach vector for every BBlock.
   *
   * See #findReach()
   * See ShowDataFlow#showReach()
   */
  public void showReach()
  {
    fShowDataFlow.showReach();
  }

  /**
   * Shows the Defined vector for every BBlock.
   *
   * See #findDefined()
   * See ShowDataFlow#showDefined()
   */
  public void showDefined()
  {
    fShowDataFlow.showDefined();
  }

  /**
   * Shows the Exposed vector for every BBlock.
   *
   * See #findExposed()
   * See ShowDataFlow#showExposed()
   */
  public void showExposed()
  {
    fShowDataFlow.showExposed();
  }

  /**
   * Shows the EGen vector for every BBlock.
   *
   * See #findEGen()
   * See ShowDataFlow#showEGen()
   */
  public void showEGen()
  {
    fShowDataFlow.showEGen();
  }

  /**
   * Shows the EKill vector for every BBlock.
   *
   * See #findEKill()
   * See ShowDataFlow#showEKill()
   */
  public void showEKill()
  {
    fShowDataFlow.showEKill();
  }

  /**
   * Shows the AvailIn vector for every BBlock.
   *
   * See #findAvailInAvailOut()
   * See ShowDataFlow#showAvailIn()
   */
  public void showAvailIn()
  {
    fShowDataFlow.showAvailIn();
  }

  /**
   * Shows the AvailOut vector for every BBlock.
   *
   * See #findAvailInAvailOut()
   * See ShowDataFlow#showAvailOut()
   */
  public void showAvailOut()
  {
    fShowDataFlow.showAvailOut();
  }

  /**
   * Shows the LiveIn vector for every BBlock.
   *
   * See #findLiveInLiveOut()
   * See ShowDataFlow#showLiveIn()
   */
  public void showLiveIn()
  {
    fShowDataFlow.showLiveIn();
  }

  /**
   * Shows the LiveOut vector for every BBlock.
   *
   * See #findLiveInLiveOut()
   * See ShowDataFlow#showLiveOut()
   */
  public void showLiveOut()
  {
    fShowDataFlow.showLiveOut();
  }

  /**
   * Shows the DefIn vector for every BBlock.
   *
   * See #findDefInDefOut()
   * See ShowDataFlow#showDefIn()
   */
  public void showDefIn()
  {
    fShowDataFlow.showDefIn();
  }

  /**
   * Shows the DefOut vector for every BBlock.
   *
   * See #findDefInDefOut()
   * See ShowDataFlow#showDefOut()
   */
  public void showDefOut()
  {
    fShowDataFlow.showDefOut();
  }

  /**
   * Shows all the DefVectors for all the BBlocks.
   *
   * See #showDef()
   * See #showKill()
   * See #showIn()
   * See #showOut()
   * See #showReach()
   * See ShowDataFlow#showDefVectors()
   */
  public void showDefVectors()
  {
    fShowDataFlow.showDefVectors();
  }

  /**
   * Shows all the ExpVectors for all the BBlocks.
   *
   * See #showDefined()
   * See #showExposed()
   * See #showEGen()
   * See #showEKill()
   * See #showAvailIn()
   * See #showAvailOut()
   * See #showLiveIn()
   * See #showLiveOut()
   * See #showDefIn()
   * See #showDefOut()
   * See ShowDataFlow#showExpVectors()
   */
  public void showExpVectors()
  {
    fShowDataFlow.showExpVectors();
  }

  /**
   * Shows all the data flow items that are independent
   *  of the control flow, that is, Def, Kill, Defined,
   *  Exposed, EGen, and EKill vectors.
   *
   * See #showDef()
   * See #showKill()
   * See #showDefined()
   * See #showExposed()
   * See #showEGen()
   * See #showEKill()
   * See ShowDataFlow#showBasic()
   */
  public void showBasic()
  {
    fShowDataFlow.showBasic();
  }

  /**
   * Shows all the data flow items data flow equations have found,
   *  that is, In, Out, Reach, AvailIn, AvailOut, LiveIn, LiveOut,
   * DefIn, and DefOut vectors.
   *
   * See #showIn()
   * See #showOut()
   * See #showReach()
   * See #showAvailIn()
   * See #showAvailOut()
   * See #showLiveIn()
   * See #showLiveOut()
   * See #showDefIn()
   * See #showDefOut()
   * See ShowDataFlow#showSolved()
   */
  public void showSolved()
  {
    fShowDataFlow.showSolved();
  }

  /**
   * Shows BitVectors related to (needed to solve) In/Out vectors,
   *  that is, Def, Kill, In, and Out vectors.
   *
   * See #showDef()
   * See #showKill()
   * See #showIn()
   * See #showOut()
   * See ShowDataFlow#showInOutRelated()
   */
  //  void showInOutRelated();
  /*public void showInOutRelated() {
     fShowDataFlow.showInOutRelated();
     }*/

  /**
   * Shows BitVectors related to (needed to solve) Reach vectors, that is, Def, Kill, and Reach vectors.
   *
   * See #showDef()
   * See #showKill()
   * See #showReach()
   * See ShowDataFlow#showReachRelated()
   */
  public void showReachRelated()
  {
    fShowDataFlow.showReachRelated();
  }

  /**
   * Shows BitVectors related to (needed to solve) AvailIn/AvailOut
   *  vectors, that is, EGen, EKill, AvailIn, and AvailOut vectors.
   *
   * See #showEGen()
   * See #showEKill()
   * See #showAvailIn()
   * See #showAvailOut()
   * See ShowDataFlow#showAvailInAvailOutRelated()
   */
  public void showAvailInAvailOutRelated()
  {
    fShowDataFlow.showAvailInAvailOutRelated();
  }

  /**
   * Shows BitVectors related to (needed to solve) LiveIn/LiveOut
   *  vectors, that is, Exposed, Defined, LiveIn, and LiveOut vectors.
   *
   * See #showExposed()
   * See #showDefined()
   * See #showLiveIn()
   * See #showLiveOut()
   * See ShowDataFlow#showLiveInLiveOutRelated()
   */
  public void showLiveInLiveOutRelated()
  {
    fShowDataFlow.showLiveInLiveOutRelated();
  }

  /**
   * Shows BitVectors related to (needed to solve) DefIn/DefOut vectors,
   *  that is, Defined, DefIn, and DefOut vectors.
   *
   * See #showDefined()
   * See #showDefIn()
   * See #showDefOut()
   * See ShowDataFlow#showDefInDefOutRelated()
   */
  public void showDefInDefOutRelated()
  {
    fShowDataFlow.showDefInDefOutRelated();
  }

  /**
   * Shows all the BitVectors.
   *
   * See #showDefVectors()
   * See #showExpVectors()
   * See ShowDataFlow#showAllBitVectors()
   */
  public void showAllBitVectors()
  {
    fShowDataFlow.showAllBitVectors();
  }

  /**
   * Shows the DefUseList for each FlowAnalSym.
   *
   * See ShowDataFlow#showDefUse()
   */
  public void showDefUse()
  {
    fShowDataFlow.showDefUse();
  }

  /**
   * Shows the UseDefList for each FlowAnalSym.
   *
   * See ShowDataFlow#showUseDef()
   */
  public void showUseDef()
  {
    fShowDataFlow.showUseDef();
  }

  /**
   * Shows all the information found in this DFA.
   *
   * See #showAllBitVectors()
   * See #showDefUse()
   * See ShowDataFlow#showAll()
   */
  public void showAll()
  {
    ioRoot.printOut.print("\n*[START]******** DataFlowGraph *******\n");
    fShowDataFlow.showAll();
    ioRoot.printOut.print("\n*[END]********** DataFlowGraph *******\n");
  }

  public void showSummary()
  {
    ioRoot.printOut.print("\nData flow summary of " +
      flowRoot.subpUnderAnalysis.getName() + "\n");
    fShowDataFlow.showSummary();
  } // showSummary

  /**
   * Shows the given BitVector.
   *
   * @param pBitVector BitVector to display
   * See ShowDataFlow#showVector()
   */
  void showVector(BitVector pBitVector)
  {
    fShowDataFlow.showVector(pBitVector);
  }

  /**
   * Shows the given BitVector w/ a comment(heading).
   *
   * @param pBitVector BitVector to display
   * @param pComment Comment that is displayed before the BitVector
   * See ShowDataFlow#showVector()
   */
  void showVector(BitVector pBitVector, String pComment)
  {
    fShowDataFlow.showVector(pBitVector, pComment);
  }

//##60 BEGIN
  public Set getUndefinedUseNodeOfSym(FlowAnalSym lSym)
  {
    return fUndefinedUseNodesOfSym[lSym.getIndex()];
  }

// BEGIN
  /** toExpVector
   * Converts the given Set of symbols to an ExpVector where each bit
   * of the vector corresponds to an element in the Set set.
   * @param pSymbol Set of symbols to convert.
   * @return ExpVector w/ the relevant bits set.
   **/
  ExpVector
    toExpVector(java.util.Set pSymbolSet)
  {
    Sym lSymbol;
    int lExpBitPosition;
    if (fDbgFlow > 3) {
      ioRoot.dbgFlow.printObject(8, " toExpVector", pSymbolSet);
    }
    if (pSymbolSet == null) {
      return null;
    }
    ExpVector lExpVector = fSubpFlow.expVector();
    for (Iterator lSetIterator = pSymbolSet.iterator();
         lSetIterator.hasNext(); ) { // For each element in the Set,
      lSymbol = (Sym)lSetIterator.next();
      if (fDbgFlow > 3) {
        ioRoot.dbgFlow.printObject(7, " sym ", lSymbol);
      }
      lExpBitPosition = expLookup(((FlowAnalSym)lSymbol).getIndex());
      lExpVector.setBit(lExpBitPosition); // Set the bit that
      // corresponds to the element.
    }
    return lExpVector;
  } // toExpVector

// END

  /**
   * Converts the given ExpVector to a Set that has elements that
   *  correspond to the set bits of the ExpVector.
   *
   * @param pExpVector ExpVector to convert.
   * @return Set that contains the relevant elements.
   */
  java.util.Set toSet(ExpVector pExpVector)
  {
    java.util.Set lSet = new HashSet();

    /*  for (ExpVectorIterator lExpVectorIterator=fSubpFlow.expVectorIterator(pExpVector);
      lExpVectorIterator.hasNext();)
       lSet.add(lExpVectorIterator.nextExpId());
     */
    // DOESN'T WORK.

    // for (int i=1; i<=getExpIdCount(); i++)
    for (int i = 1; i <= getFlowAnalSymCount(); i++) {
      if (pExpVector.getBit(i) == 1) { // For each set bit in the ExpVector,

        // lSet.add(getExpId(i));
        lSet.add(getFlowAnalSym(i));
        // Add the element to the Set that correspond to the bit.
      }
    }
    return lSet;
  } // toSet

} // DataFlowImpl
