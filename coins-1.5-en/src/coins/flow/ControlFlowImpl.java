/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.flow;

import java.util.ArrayList; //##60
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import coins.FlowRoot;
import coins.IoRoot;
import coins.ir.hir.BlockStmt;
import coins.ir.hir.Exp;
import coins.ir.hir.HIR;
import coins.ir.hir.IfStmt;
import coins.ir.hir.JumpStmt;
import coins.ir.hir.LabeledStmt;
import coins.ir.hir.LoopStmt;
import coins.ir.hir.ReturnStmt; //##65
import coins.ir.hir.Stmt;
import coins.ir.hir.SubpDefinition;
import coins.ir.hir.SwitchStmt;
import coins.sym.Label;
import coins.sym.Subp; //##60
import coins.sym.Sym;

// Modified on Jan. 2002 by Tan
//   to prepare for LIR control flow analysis.
public class ControlFlowImpl
  implements ControlFlow
{

  public final FlowRoot
    flowRoot; // Used to access Root information.

  public final IoRoot
    ioRoot; // Used to access Root information.

  public final Flow
    flow; // Used to invoke Flow methods.

  // private  HirSubpFlow  fFlow;
  private SubpFlow fSubpFlow; //##60
  private ShowControlFlow fShowFlow;

  public BBlockVector[] fDom;
  public BBlockVector[] fsDom;
  public BBlockVector[] fPostDom;
  public BBlockVector[] fPostsDom;

  //##65 protected ArrayList fStmtInBlock; //##60
  //##65 protected int fStmtInBlockIndex; //##60
  public final int fDbgLevel; //##60
  /**
   *
   *  Make control flow graph.
   *
   **/
  public ControlFlowImpl(FlowRoot pFlowRoot,
    //##    HirSubpFlow pFlow,SubpDefinition pSubDef)
    SubpFlow pFlow, SubpDefinition pSubDef)
  {
    flowRoot = pFlowRoot;
    ioRoot = pFlowRoot.ioRoot;
    flow = pFlowRoot.flow;
    fSubpFlow = pFlow;
    fDbgLevel = ioRoot.dbgFlow.getLevel(); //##60
    if (fDbgLevel > 0)
      ioRoot.dbgFlow.print(1, "ControlFlowImpl", pSubDef.getSubpSym().getName());
    fSubpFlow.initiateControlFlowAnal(pSubDef, pSubDef.getNodeIndexMin(),
      pSubDef.getNodeIndexMax()); //##60
    fShowFlow = new ShowControlFlow(pFlow, this);
    if (flowRoot.isHirAnalysis()) {
      //##78 ((HirSubpFlow)fSubpFlow).divideHirIntoBasicBlocks(); //##60
      ((SubpFlowImpl)fSubpFlow).failed =
          ! ((HirSubpFlow)fSubpFlow).divideHirIntoBasicBlocks(); //##78
      if (! ((SubpFlowImpl)fSubpFlow).failed) { //##78
        makeControlFlowGraph((Stmt)pSubDef.getHirBody());
      }else
        ioRoot.msgRecovered.put(5011, "Skip to make control flow graph of "
          + pSubDef.getSubpSym().getName()); //##78
    }
    //## if (ioRoot.dbgFlow.getLevel() > 0) //##60
    //##  fShowFlow.showAll(); //## REFINE
    if (fDbgLevel > 0)
      ioRoot.dbgFlow.print(2,
        " To show control flow, getShowControlFlow.showAll()\n");
    flowRoot.flow.setFlowAnalStateLevel(flowRoot.flow.STATE_CFG_AVAILABLE);
  } // ControlFlowImpl

  /**
   *  Get an instance of ShowCOntrolFlow.
   **/
  public ShowControlFlow getShowControlFlow()
  {
    return fShowFlow;
  }

  /**
   *
   * Make following information
   *  (1) CFG (control flow graph)
   *  (2) EntryBlock
   *  (3) ExitBlock
   *  (4) Dominate
   *  (5) StrictlyDominate
   *  (6) ImmediatelyDominate
   *  (7) DominatedChildren
   *  (8) PostDominate
   *  (9) PostStrictlyDominate
   *  (10) PostImmediatelyDominate
   *  (11) PostDominatedChildren
   *
   **/
  private void makeControlFlowGraph(Stmt pTree)
  {
    BBlock firstBBlock;
    BBlock lastBBlock;
    if (fDbgLevel > 0)
      flowRoot.ioRoot.dbgFlow.print(2,
        // "makeControlFlowGraph", "of LIR");
        "makeControlFlowGraph", "of " + pTree.toString());
    //##65 fStmtInBlock = new ArrayList(); //##60
    //##65 fStmtInBlockIndex = 0; //##60
    firstBBlock = null;
    lastBBlock = null;
    lastBBlock = makeEdge(pTree, firstBBlock);
    firstBBlock = fSubpFlow.getEntryBBlock(); //##60
    deleteEdge(firstBBlock);
    linkBBlockInDfoAndInverseDfo(); //##60
    fSubpFlow.setComputedFlag(fSubpFlow.CF_CFG); //##60
    // Dominate

    findDominate();
    findStrictlyDominate();
    findImmediatelyDominate();
    findDominatedChildren();
    fSubpFlow.setComputedFlag(fSubpFlow.CF_DOMINATOR); //##60

    // Post Dominate

    findPostDominate();
    findPostStrictlyDominate();
    findPostImmediatelyDominate();
    findPostDominatedChildren();
    fSubpFlow.setComputedFlag(fSubpFlow.CF_POSTDOMINATOR); //##60
  } // makeControlFlowGraph

  /*
   * Cut off the edges that can not be reached from the entry block.
   */
  private void deleteEdge(BBlock pBBlock)
  {
    int maxBBlockNo;
    BBlockVector mark;
    BBlock Curr;
    BBlock Succ;
    BBlock Pred;
    List l;
    List delList;
    ListIterator Ie;
    int i;

    maxBBlockNo = fSubpFlow.getNumberOfBBlocks();
    mark = (BBlockVector)new BBlockVectorImpl(fSubpFlow); //##60

    markRootDom(pBBlock, mark);
    delList = new LinkedList();

    for (i = 1; i <= maxBBlockNo; i++) {
      if (mark.getBit(domBitLookUp(i)) == 0) {
        Curr = fSubpFlow.getBBlock(i);

        // Delete Succ List

        l = Curr.getSuccList();
        ListCopy(l, delList);
        for (Ie = delList.listIterator(); Ie.hasNext(); ) {
          Succ = (BBlock)Ie.next();
          Succ.deleteFromPredList(Curr);
          Curr.deleteFromSuccList(Succ);

        }

        // Delete Pred List

        l = Curr.getPredList();
        ListCopy(l, delList);
        for (Ie = delList.listIterator(); Ie.hasNext(); ) {
          Pred = (BBlock)Ie.next();
          Pred.deleteFromPredList(Curr);
          Curr.deleteFromPredList(Pred);
        }
      }
    }
  } // deleteEdge

  /*
   * Search for blocks that can not be reached from the entry block,
   * set 1 to the corresponding bit of pmark bit vector, where
   * bits of pmark have following meaning:
   *  1: the corresponding block can be reached from the entry block.
   *  0: the corresponding block can not be reached from the entry block.
   */
  private void markRootDom(BBlock pBBlock, BBlockVector pmark)
  {
    BBlock b;
    BBlock edge;
    List l;
    ListIterator Ie;
    l = pBBlock.getSuccList();
    pmark.setBit(domLookUp(pBBlock.getBlockNumber())); // Set bit
    for (Ie = l.listIterator(); Ie.hasNext(); ) {
      b = (BBlock)Ie.next();
      if (pmark.getBit(domBitLookUp(b.getBlockNumber())) == 0) {
        markRootDom(b, pmark);
      }
    }
  } // markRootDom

  /**
   * Search for the entry block.
   **/
  private BBlock
    findEntryBlock()
  {
    Label lStartLabel;
    BBlock lEntryBBlock;
    lStartLabel = flowRoot.subpUnderAnalysis.getStartLabel();
    lEntryBBlock = flowRoot.fSubpFlow.getBBlock0(lStartLabel); //##60
    if (lEntryBBlock != null) //##60
      lEntryBBlock.setFlag(BBlock.IS_ENTRY, true);
    return lEntryBBlock;
  } // findEntryBBlock;

  /**
   * Search for the exit block.
   **/
  private List findExitBBlock()
  {
    int maxBBlockNo;
    int i;
    BBlock lBBlock, lExitBBlock = null;
    List lSuccList;
    if (fDbgLevel > 0)
      flow.dbg(3, "findExitBBlock", "BBlock with no successors: ");
    List lBBlockWithNoSuccessors = new LinkedList();
    maxBBlockNo = fSubpFlow.getNumberOfBBlocks();
    for (i = 1; i <= maxBBlockNo; i++) {
      lBBlock = fSubpFlow.getBBlock(i);
      lSuccList = lBBlock.getSuccList();
      if (lSuccList.size() == 0) {
        lBBlockWithNoSuccessors.add(lBBlock);
        lExitBBlock = lBBlock;
        if (fDbgLevel > 0)
          flow.dbg(3, "B" + lBBlock.getBBlockNumber());
      }
    }
    if (lExitBBlock != null) {
      lExitBBlock.setFlag(BBlock.IS_EXIT, true);
    }
    return lBBlockWithNoSuccessors;
  } // findExitBlock

  /**
   * Get dominators which is represented by the bit vector fDom.
   * See Nakata: Compiler construction and optimization, p.283 (1999).
   **/
  private void findDominate()
  {
    int maxBBlockNo;
    int i;
    int j;
    BBlock lCurrBBlock;
    BBlock lPredBBlock;
    List lPredList;
    ListIterator lPred;
    int lookUp;
    BBlockVector lnewDom;
    BBlockVector lPredDom;
    BBlockVector lPredDom1;
    boolean lChanged;

    // Allocate Area
    maxBBlockNo = fSubpFlow.getNumberOfBBlocks();
    if (fDbgLevel > 3)
      ioRoot.dbgFlow.print(4, "flowDominate",
        "number of BBlocks " + maxBBlockNo); //
    fDom = (BBlockVector[])new BBlockVectorImpl[maxBBlockNo + 1];
    fsDom = (BBlockVector[])new BBlockVectorImpl[maxBBlockNo + 1]; //##60
    for (i = 1; i <= maxBBlockNo; i++)
      fDom[i] = new BBlockVectorImpl(fSubpFlow); //##60
    lPredDom = (BBlockVector)new BBlockVectorImpl(fSubpFlow); //##60
    lPredDom1 = (BBlockVector)new BBlockVectorImpl(fSubpFlow); //##60
    lnewDom = (BBlockVector)new BBlockVectorImpl(fSubpFlow); //##60

    // Initialize
    for (i = 1; i <= maxBBlockNo; i++) {
      lCurrBBlock = fSubpFlow.getBBlock(i);
      if (lCurrBBlock.isEntryBlock() == true) {
        for (j = 1; j <= maxBBlockNo; j++) {
          fDom[domLookUp(i)].resetBit(j);
        }
        fDom[domLookUp(i)].setBit(domLookUp(i));
      }
      else {
        for (j = 1; j <= maxBBlockNo; j++) {
          fDom[domLookUp(i)].setBit(j);
        }
      }
    }
    do {
      lChanged = false;
      for (i = 1; i <= maxBBlockNo; i++) {
        lCurrBBlock = fSubpFlow.getBBlock(i);
        lPredList = lCurrBBlock.getPredList();
        if (lPredList.size() != 0) {
          for (j = 1; j <= maxBBlockNo; j++) {
            lPredDom.setBit(j);
          }
          for (lPred = lPredList.listIterator(); lPred.hasNext(); ) {
            lPredBBlock = (BBlock)lPred.next();
            lookUp = domLookUp(lPredBBlock.getBlockNumber());
            fDom[lookUp].vectorAnd(lPredDom, lPredDom);
            //	lPredDom1.vectorCopy((BitVector)lPredDom);
          }
        }
        else {
          lPredDom.vectorReset();
          if (fDbgLevel > 3)
            ioRoot.dbgFlow.print(6, "no predecessor", "i " + i); //##60
          continue; //##60
        }
        lPredDom.vectorCopy((BitVector)lnewDom);
        lookUp = domLookUp(lCurrBBlock.getBlockNumber());
        lnewDom.setBit(lookUp);
        if (fDbgLevel > 3)
          ioRoot.dbgFlow.print(7, "i", i + " loopUp " + lookUp); //##60
        if (!lnewDom.vectorEqual(fDom[lookUp])) {
          lnewDom.vectorCopy((BitVector)fDom[lookUp]);
          lChanged = true;
          if (fDbgLevel > 3)
            ioRoot.dbgFlow.print(6, "changed", "i " + i + " loopUp " + lookUp); //##60
        }
      } // end of for
      // Debug
      //fShowFlow.showDominator();
    }
    while (lChanged);
    if (fDbgLevel > 3)
      ioRoot.dbgFlow.print(6, "end of ", "findDominate"); //##60
  } // findDominate

  /**
   * Find the post dominator
   * (the algorithm is the inverse of that of dominator.
   **/
  private void findPostDominate()
  {
    int maxBBlockNo;
    int i;
    int j;
    BBlock lCurrBBlock;
    BBlock lSuccBBlock;
    List lSuccList;
    ListIterator lSucc;
    int lookUp;
    BBlockVector lnewDom;
    BBlockVector lSuccDom;
    BBlockVector lSuccDom1;
    boolean lChanged;

    // Allocate Area
    maxBBlockNo = fSubpFlow.getNumberOfBBlocks();
    if (fDbgLevel > 3)
      ioRoot.dbgFlow.print(4, "findPostDominate",
        "number of BBlocks " + maxBBlockNo);
    fSubpFlow.setBBlockVectorBitCount(maxBBlockNo + 1); //##60
    fPostDom = (BBlockVector[])new BBlockVectorImpl[maxBBlockNo + 1];
    for (i = 1; i <= maxBBlockNo; i++)
      fPostDom[i] = new BBlockVectorImpl(fSubpFlow); //##60
    lSuccDom = (BBlockVector)new BBlockVectorImpl(fSubpFlow); //##60
    lSuccDom1 = (BBlockVector)new BBlockVectorImpl(fSubpFlow); //##60
    lnewDom = (BBlockVector)new BBlockVectorImpl(fSubpFlow); //##60

    // Initialize

    for (i = 1; i <= maxBBlockNo; i++) {
      lCurrBBlock = fSubpFlow.getBBlock(i);
      //##70 BEGIN
      if (lCurrBBlock.isExitBlock() == true) {
        for (j = 1; j <= maxBBlockNo; j++) {
          fPostDom[domLookUp(i)].resetBit(j);
        }
        fPostDom[domLookUp(i)].setBit(domLookUp(i));
      }
      else {
        //##70 END
        for (j = 1; j <= maxBBlockNo; j++) {
          fPostDom[domLookUp(i)].setBit(j);
        }
      } //##70
    }
    do {
      lChanged = false;
      for (i = maxBBlockNo; 0 < i; i--) {
        lCurrBBlock = fSubpFlow.getBBlock(i);
        if (lCurrBBlock.getBBlockNumber() <= 0)
          continue;
        lSuccList = lCurrBBlock.getSuccList();
        if (lSuccList.size() != 0) {
          for (j = 1; j <= maxBBlockNo; j++)
            lSuccDom.setBit(j);
          for (lSucc = lSuccList.listIterator(); lSucc.hasNext(); ) {
            lSuccBBlock = (BBlock)lSucc.next();
            lookUp = domLookUp(lSuccBBlock.getBlockNumber());
            fPostDom[lookUp].vectorAnd(lSuccDom, lSuccDom);
          }
        }
        else {
          lSuccDom.vectorReset();
          continue; //##60
        }
        lSuccDom.vectorCopy((BitVector)lnewDom);
        lookUp = domLookUp(lCurrBBlock.getBlockNumber());
        lnewDom.setBit(lookUp);
        if (!lnewDom.vectorEqual(fPostDom[lookUp])) {
          if (fDbgLevel > 3)
            ioRoot.dbgFlow.print(5, "changed", "B" + i + " " +
              lCurrBBlock.getBBlockNumber() + " lookUp " + lookUp);
          lnewDom.vectorCopy((BitVector)fPostDom[lookUp]);
          lChanged = true;
        }
      } // end of for
    }
    while (lChanged);
  } // findPostDominate

  /**
   * Find StrictlyDominate SDOM
   *   SDOM(B) = DOM(B) -B
   **/
  private void findStrictlyDominate()
  {
    int maxBBlockNo;
    int i;
    int j;
    BBlock lCurrBBlock;
    maxBBlockNo = fSubpFlow.getNumberOfBBlocks();
    if (fDbgLevel > 3)
      ioRoot.dbgFlow.print(4, "findStrictlyDominate",
        "number of BBlocks " + maxBBlockNo);
    for (i = 1; i <= maxBBlockNo; i++) {
      fsDom[i] = new BBlockVectorImpl(fSubpFlow);
      fDom[i].vectorCopy((BitVector)fsDom[i]);
    }
    for (i = 1; i <= maxBBlockNo; i++) {
      lCurrBBlock = fSubpFlow.getBBlock(i);
      fsDom[domLookUp(i)].resetBit(domLookUp(i));
    }
  } // findStrictlyDominate

  /**
   * Find strictly post dominators.
   **/
  private void findPostStrictlyDominate()
  {
    int maxBBlockNo;
    int i;
    int j;
    BBlock lCurrBBlock;
    maxBBlockNo = fSubpFlow.getNumberOfBBlocks();
    fPostsDom = (BBlockVector[])new BBlockVectorImpl[maxBBlockNo + 1];
    for (i = 1; i <= maxBBlockNo; i++) {
      fPostsDom[i] = new BBlockVectorImpl(fSubpFlow);
      fPostDom[i].vectorCopy((BitVector)fPostsDom[i]);
    }
    for (i = 1; i <= maxBBlockNo; i++) {
      lCurrBBlock = fSubpFlow.getBBlock(i);
      fPostsDom[domLookUp(i)].resetBit(domLookUp(i));
    }
  } // findPostStrictlyDominate

  /**
   * Find ImmediatelyDominate
   * Algorithm:
   *  Bi = null;
   *  for (each basic block B) {
   *    if (SDOM) {
   *      Bi <-- SDOM(B)
   *      SDOM(B) = SDOM(B) - DOM(Bi)
   *    }
   *  setting Bi as IDOM.
   *
   *  where,
   *  SDOM(B):StrictlyDominate
   *  IDOM   :ImmediatelyDominate
   *  DOM(B) :Dominate
   *
   **/
  private void findImmediatelyDominate()
  {
    int maxBBlockNo;
    int i;
    int j;
    BBlock CurBBlock;
    BBlock Idom;
    BBlockVectorImpl sdom;
    int bj;
    maxBBlockNo = fSubpFlow.getNumberOfBBlocks();
    if (fDbgLevel > 3)
      ioRoot.dbgFlow.print(4, "findImmediatelyDominate",
        "number of BBlocks " + maxBBlockNo);
      //ioRoot.printOut.println("====== [ IDOM(B)] =====");
    for (i = 1; i <= maxBBlockNo; i++) {
      //ioRoot.printOut.println("BlockNO=" + i);
      bj = -1; // Null BBlock Number
      sdom = new BBlockVectorImpl(fSubpFlow);
      fsDom[i].vectorCopy((BitVector)sdom);
      for (j = 1; j <= maxBBlockNo; j++) {
        if (sdom.getBit(domLookUp(j)) == 1) {
          bj = j;
          sdom.vectorSub(fDom[domLookUp(bj)], sdom);
        }
      }
      //ioRoot.printOut.println("IDOM(B)=" + bj);
      //setImmediateDominator(bj);
      CurBBlock = fSubpFlow.getBBlock(i);
      if (bj == -1)
        Idom = (BBlock)null;
      else
        Idom = fSubpFlow.getBBlock(bj);
      CurBBlock.setImmediateDominator(Idom);
    }
  } // findImmediatelyDominate

  /**
   * Find PostImmediatelyDominate.
   **/
  private void findPostImmediatelyDominate()
  {
    int maxBBlockNo;
    int i;
    int j;
    BBlock CurBBlock;
    BBlock Idom;
    BBlockVectorImpl postsdom;
    int bj;
    maxBBlockNo = fSubpFlow.getNumberOfBBlocks();
    if (fDbgLevel > 3)
      ioRoot.dbgFlow.print(4, "findPostImmediatelyDominate",
        "number of BBlocks " + maxBBlockNo);
      //ioRoot.printOut.println("====== [ POST IDOM(B)] =====");
    for (i = 1; i <= maxBBlockNo; i++) {
      //ioRoot.printOut.println("BlockNO=" + i);
      bj = -1; // Null BBlock Number
      postsdom = new BBlockVectorImpl(fSubpFlow);
      fPostsDom[i].vectorCopy((BitVector)postsdom);
      for (j = 1; j <= maxBBlockNo; j++) {
        if (postsdom.getBit(domLookUp(j)) == 1) {
          bj = j;
          postsdom.vectorSub(fPostDom[domLookUp(bj)], postsdom);
        }
      }
      //ioRoot.printOut.println("POST IDOM(B)=" + bj);
      CurBBlock = fSubpFlow.getBBlock(i);
      if (bj == -1)
        Idom = (BBlock)null;
      else
        Idom = fSubpFlow.getBBlock(bj);
      CurBBlock.setImmediatePostDominator(Idom);

    }
  } // findPostImmediatelyDominate

  /**
   * Find DominatedChildren
   *   DominatedChildren(B)={ B' | B =IDOM(B')}
   **/
  private void findDominatedChildren()
  {
    int maxBBlockNo;
    int i;
    BBlock lChildBBlock; // Child Basic Block
    BBlock lIDomBBlock; // IDOM(B) Basic Block
    List lChild;
    maxBBlockNo = fSubpFlow.getNumberOfBBlocks();
    if (fDbgLevel > 3)
      ioRoot.dbgFlow.print(4, "findDominatedChildren",
        "number of BBlocks " + maxBBlockNo);
    lChildBBlock = null;
    lIDomBBlock = null;
    //ioRoot.printOut.println("====== [ DominatedChildren] =====");

    // Create (new) Linkded List
    for (i = 1; i <= maxBBlockNo; i++) {
      lChildBBlock = fSubpFlow.getBBlock(i);
      lChildBBlock.setDominatedChildren(new LinkedList());
    }

    //  Set Child
    for (i = 1; i <= maxBBlockNo; i++) {
      lChildBBlock = fSubpFlow.getBBlock(i);
      // get IDOM(B)
      lIDomBBlock = lChildBBlock.getImmediateDominator();
      if (lIDomBBlock != null) {
        lChild = lIDomBBlock.getDominatedChildren();
        lChild.add(lChildBBlock);
      }
    }
  } // findDominatedChildren

  /**
   * Find PostDominatedChildren.
   **/
  private void findPostDominatedChildren()
  {
    int maxBBlockNo;
    int i;
    BBlock lChildBBlock; // Child Basic Block
    BBlock lIDomBBlock; // POST IDOM(B) Basic Block
    List lChild;
    maxBBlockNo = fSubpFlow.getNumberOfBBlocks();
    if (fDbgLevel > 3)
      ioRoot.dbgFlow.print(4, "findPostDominatedChildren",
        "number of BBlocks " + maxBBlockNo);
    lChildBBlock = null;
    lIDomBBlock = null;
    //ioRoot.printOut.println("====== [ Post Dominated Children] =====");

    // Create (new) Linkded List
    for (i = 1; i <= maxBBlockNo; i++) {
      lChildBBlock = fSubpFlow.getBBlock(i);
      lChildBBlock.setPostDominatedChildren(new LinkedList());
    }
    for (i = 1; i <= maxBBlockNo; i++) {
      lChildBBlock = fSubpFlow.getBBlock(i);
      // get POST IDOM(B)
      lIDomBBlock = lChildBBlock.getImmediatePostDominator();
      if (lIDomBBlock != null) {
        lChild = lIDomBBlock.getPostDominatedChildren();
        lChild.add(lChildBBlock);
      }
    }
  } // findPostDominatedChildren

  /**
   * Find bit position in BBlockVector
   * from basic block number.
   **/
  public int domLookUp(int ppNo)
  {
    int lBitPosition = ppNo;
    return lBitPosition;
  }

  /**
   * domBitLookUp
   * Get the basic block number from the bit position
   * in BBlockVector.
   **/
  public int domBitLookUp(int pBitPos)
  {
    int lBlockNo = pBitPos;
    return lBlockNo;
  }

  /**
   * makeEdge
   * Make CFG (control flow graph).
   *  Stmt pstmt:   : Make the CFG that corresponds to pstmt.
   *  BBlock pblock : current basic block.
   **/
  private BBlock makeEdge(Stmt pstmt, BBlock pblock)
  {
    Stmt nextStmt; // Used in makeEdge. //##60
    BBlock currBlock;
    int lop;
    //##65 LabelBlock = null;
    boolean lAfterJumpReturn = false; //##65
    nextStmt = pstmt;
    currBlock = pblock;
    if (fDbgLevel > 3) {
      flowRoot.ioRoot.dbgFlow.print(4,
        "makeEdge " + ioRoot.toStringObject(pblock),
        " from " + ioRoot.toStringObjectShort(pstmt) + " to "); //##62
      if (nextStmt != null) //##62
        flowRoot.ioRoot.dbgFlow.print(4,
          ioRoot.toStringObjectShort(nextStmt.getNextStmt())); //##60
    }
    while (nextStmt != null) {
      //##65 BEGIN
      Stmt nextStmt1;
      LabeledStmt lastStmt;
      //##65 Label lLabel; //##60
      IfStmt stmtIF;
      LoopStmt stmtLOOP;
      SwitchStmt stmtSWITCH;
      BBlock thenBlock;
      BBlock elseBlock;
      //BBlock LabelBlock;
      BBlock bodyBlock;
      BBlock stepBlock;
      BBlock loopbackBlock;
      BBlock CondInitBlock;
      Stmt loopInitPart; //##60
      BBlock loopInitBlock; //##60
      BBlock defaultBlock; //##60
      //##65 BBlock endBlock;
      BBlock switchBlock;
      //##65 END
      if (fDbgLevel > 3)
        flowRoot.ioRoot.dbgFlow.print(5, "  nextStmt",
          nextStmt.toStringShort() + " nextToNext " +
          ioRoot.toStringObjectShort(nextStmt.getNextStmt())); //##60
      if (currBlock != null) {
        if (fDbgLevel > 3)
          flowRoot.ioRoot.dbgFlow.print(5,
            " BB" + currBlock.getBBlockNumber()); //##60
      }
      //##65 if (fStmtInBlockIndex > 0) {
      //##65   if (fStmtInBlock.size() <= fStmtInBlockIndex) {
      //##65     fStmtInBlock.add(fStmtInBlockIndex - 1, nextStmt);
      //##65   }else
      //##65     fStmtInBlock.set(fStmtInBlockIndex - 1, nextStmt);
      //##65 }
      lop = nextStmt.getOperator();
      switch (lop) {
        case HIR.OP_IF:
          stmtIF = (IfStmt)nextStmt;
          thenBlock = makeEdge(stmtIF.getThenPart(), currBlock);
          elseBlock = makeEdge(stmtIF.getElsePart(), currBlock);
          BBlock lIfEndBlock = flowRoot.fSubpFlow.getBBlock0(stmtIF.getEndLabel()); //##65
          if (fDbgLevel > 4)
            flow.dbg(6, "thenPart " +stmtIF.getThenPart()
              + " " + thenBlock
              + " elsePart " + stmtIF.getElsePart()
              + " " + elseBlock
              +  " endif", lIfEndBlock.toStringShort()
                         + " " + stmtIF.getEndLabel().getName()); //##65
          if (thenBlock.controlTransfer() == null) //##73
            addEdge(thenBlock, lIfEndBlock);
          if (elseBlock.controlTransfer() == null) //##73
            addEdge(elseBlock, lIfEndBlock);
          //##60 currBlock = LabelBlock;
          //##60 nextStmt = nextStmt.getNextStmt();
          //##60 BEGIN
          LabeledStmt lIfEnd = (LabeledStmt)stmtIF.getChild(4);
          if (fDbgLevel > 0)
            flow.dbg(6, " ", "if-end " + lIfEnd.toStringShort());
          if (lIfEnd.getStmt() != null)
            currBlock = makeEdge(lIfEnd.getStmt(), lIfEndBlock);
          else
            currBlock = lIfEndBlock;
          nextStmt = getNextStmtSeeingAncestor(stmtIF);
          //##60 END
          break;
        case HIR.OP_LABELED_STMT:
          //##65 BBlock LabelBlock2 = flowRoot.fSubpFlow.getBBlock(nextStmt); //##65
          BBlock LabelBlock2
            = flowRoot.fSubpFlow.getBBlock0(((LabeledStmt)nextStmt).getLabel()); //##65
          if (! lAfterJumpReturn) { //##65
            addEdge(currBlock, LabelBlock2);
          }else
            lAfterJumpReturn = false; //##65
          currBlock = LabelBlock2;
          if (fDbgLevel > 0)
            flow.dbg(6, "labeledSt", "currBlock " + currBlock.toStringShort());
          nextStmt1 = ((LabeledStmt)nextStmt).getStmt();
          if (nextStmt1 == null) {
            nextStmt = getNextStmtSeeingAncestor(nextStmt); //##60
          }
          else {
            nextStmt = nextStmt1;
          }
          break;
        case HIR.OP_SWITCH:
          int CaseCount;
          stmtSWITCH = (SwitchStmt)nextStmt;
          CaseCount = stmtSWITCH.getCaseCount();
          for (int i = 0; i < CaseCount; i++) {
            BBlock lCaseBlock = flowRoot.fSubpFlow.getBBlock0( //##65
              stmtSWITCH.getCaseLabel(i)); //##65
            addEdge(currBlock, lCaseBlock);
          }
          defaultBlock = flowRoot.fSubpFlow.getBBlock0(stmtSWITCH.
            getDefaultLabel()); //##60
          BBlock lSwitchEndBlock = flowRoot.fSubpFlow.getBBlock0(stmtSWITCH.getEndLabel()); //##65
          if (defaultBlock == null) { //##60
            addEdge(currBlock, lSwitchEndBlock); //##60
          }else {
            addEdge(currBlock, defaultBlock); //##60
          }
          bodyBlock = makeEdge(stmtSWITCH.getBodyStmt(), currBlock);
          if (bodyBlock.controlTransfer() == null) //##73
            addEdge(bodyBlock, lSwitchEndBlock);
          //##53 currBlock = endBlock;
          //##53 nextStmt = nextStmt.getNextStmt();
          //##53 BEGIN
          LabeledStmt lSwitchEnd = (LabeledStmt)stmtSWITCH.getChild(4);
          if (fDbgLevel > 0)
            flow.dbg(6, " ", "switch-end " + lSwitchEnd.toStringShort());
          if (lSwitchEnd.getStmt() != null)
            currBlock = makeEdge(lSwitchEnd.getStmt(), lSwitchEndBlock);
          else
            currBlock = lSwitchEndBlock;
          nextStmt = getNextStmtSeeingAncestor(stmtSWITCH);
          //##53 END
          break;
        case HIR.OP_WHILE:
        case HIR.OP_FOR:
        case HIR.OP_INDEXED_LOOP:
        case HIR.OP_REPEAT:
          stmtLOOP = (LoopStmt) nextStmt;
          Label lBackLabel = stmtLOOP.getLoopBackLabel(); //##65
          loopbackBlock = flowRoot.fSubpFlow.getBBlock0(lBackLabel); //##60
          BBlock lLoopEndBlock = flowRoot.fSubpFlow.getBBlock0(stmtLOOP.getLoopEndLabel()); //##65
          //##53 BEGIN
          loopInitPart = stmtLOOP.getLoopInitPart();
          if (fDbgLevel >= 4) {
            flow.dbg(6, "loop", "back " + loopbackBlock.toStringShort()
              + " " + lBackLabel.getName() + " end " + lLoopEndBlock.toStringShort()
              + " body " + stmtLOOP.getLoopBodyPart().toStringShort()
              + " step " + stmtLOOP.getLoopStepLabel()
              + " end " + stmtLOOP.getChild(7).toStringShort());
          }
          // loopInitPart may contain jump-to-loopBodyPart to implement
          // conditional initiation.
          loopInitBlock = makeEdge(loopInitPart, currBlock);
          if (! isEndedWithJump(loopInitPart)) //##62
            addEdge(currBlock, loopbackBlock);
          bodyBlock = makeEdge(stmtLOOP.getLoopBodyPart(),
                     loopbackBlock);
          //##53 END
          Label lStepLabel = stmtLOOP.getLoopStepLabel();
          if ((lStepLabel != null) && (lStepLabel.getHirPosition() != null))  {
             stepBlock = flowRoot.fSubpFlow.getBBlock0(lStepLabel); //##60
             if (fDbgLevel >= 4)
               flow.dbg(6, "stepBlock", stepBlock + " " + lStepLabel.getName());
              //	addEdge(bodyBlock,stepBlock);
              if (stepBlock != null) {
                  addEdge(stepBlock, loopbackBlock);
                  stepBlock.getSuccEdge(loopbackBlock).flagBox().setFlag(Edge.LOOP_BACK_EDGE,
                      true); //## Tan //##9
                  BBlock lStepBlock2 = makeEdge(stmtLOOP.getLoopStepPart(),
                                                stepBlock);  //##53
              } else if (bodyBlock != null) // no step part (or merged with the body)
               {
                 if (bodyBlock.controlTransfer() == null) //##73
                   addEdge(bodyBlock, loopbackBlock);
                 bodyBlock.getSuccEdge(loopbackBlock).flagBox().setFlag(Edge.LOOP_BACK_EDGE,
                      true); //## Tan //##9
              } else {
                  //System.out.println("Returned or Jumped");//
              }
              if (stmtLOOP.getLoopEndCondition() == (Exp) null) {
                  addEdge(loopbackBlock, lLoopEndBlock);
              } else if (stepBlock != null) {
                  addEdge(stepBlock, lLoopEndBlock);
              } else {
                  if (bodyBlock.controlTransfer() == null) //##73
                    addEdge(bodyBlock, lLoopEndBlock);
              }
          } else {
            if (bodyBlock.controlTransfer() == null) //##73
              addEdge(bodyBlock, loopbackBlock);
            if (loopbackBlock.controlTransfer() == null) //##73
              addEdge(loopbackBlock, lLoopEndBlock);
          }
          //##53 currBlock = endBlock;
          //##53 nextStmt = nextStmt.getNextStmt();
          //##53 BEGIN
          LabeledStmt lLoopEnd = (LabeledStmt)stmtLOOP.getChild(7);
          flow.dbg(6, " ", "loop-end " + lLoopEnd.toStringShort());
          if (lLoopEnd.getStmt() != null) {
            currBlock = makeEdge(lLoopEnd.getStmt(), lLoopEndBlock);
          }else
            currBlock = lLoopEndBlock;
          nextStmt = getNextStmtSeeingAncestor(stmtLOOP);
          //##53 END
          break;

        case HIR.OP_RETURN:
          //##65 currBlock = null;
          nextStmt = getNextStmtSeeingAncestor(nextStmt); //##60
          lAfterJumpReturn = true; //##65
          break;
        case HIR.OP_JUMP:
          Label lJumpLabel = ((JumpStmt)nextStmt).getLabel(); //##65
          BBlock LabelBlock4 = flowRoot.fSubpFlow.getBBlock0(lJumpLabel); //##65
          addEdge(currBlock, LabelBlock4);
          //##65 currBlock = null;
          nextStmt = getNextStmtSeeingAncestor(nextStmt); //##60
          lAfterJumpReturn = true; //##65
          break;
        case HIR.OP_BLOCK:

          //##60 currBlock = makeEdge(nextStmt, currBlock); //## Fukuda 020322
          //##60 nextStmt = ((BlockStmt) nextStmt).getNextStmt(); //## Fukuda 020322
          //##60 BEGIN
          BlockStmt lBlockStmt = (BlockStmt)nextStmt;
          Stmt lStmtInBlock = lBlockStmt.getFirstStmt();
          if (lStmtInBlock != null) {
            nextStmt = lStmtInBlock; //##65

          }else //##65
            nextStmt = getNextStmtSeeingAncestor(lBlockStmt);
          //##60 END
          break;
        case HIR.OP_LIST: //##60
          // It is assumed that HIR list does not contain labeled statement.
          nextStmt = getNextStmtSeeingAncestor(nextStmt); //##60
          break;
        default:
          nextStmt = getNextStmtSeeingAncestor(nextStmt); //##60
      }
    }
    if (fDbgLevel > 0) {
      if (currBlock == null)
        flow.dbg(6, "return", "BB0");
      else
        flow.dbg(6, "return", "BB" + currBlock.getBBlockNumber());
    }
    return currBlock;
  } // makeEdge

  /**
   * addEdge
   *   Make edge that goes from ppred to psucc.
   **/
  private void addEdge(BBlock ppred, BBlock psucc)
  {
    if (fDbgLevel > 3)
      ioRoot.dbgFlow.print(4, "addEdge "); //##60
    if (psucc == null)
      return;
    if (ppred == null)
      return;
    if (fDbgLevel > 3)
      ioRoot.dbgFlow.print(4, "from BB" + ppred.getBlockNumber()
        + " to BB" + psucc.getBlockNumber()); //##60

    psucc.addToPredList(ppred);
    ppred.addToSuccList(psucc);
  } // addEdge

  /*
   * ListCopy
   * Add all the elements of from-list to to-list.
   */
  private void ListCopy(List from, List to)
  {
    ListIterator Ie;
    to.clear();
    for (Ie = from.listIterator(); Ie.hasNext(); ) {
      to.add(Ie.next());
    }
  } // ListCopy

//##60 BEGIN
  /** linkBBlockInDfoAndInverseDfo
   *
   * @return true if normal, false if virtual edge to exit block is created.
   */
  public boolean
    linkBBlockInDfoAndInverseDfo()
  {
    boolean lNormal = true;
    Subp lSubp = fSubpFlow.getSubpSym();
    BBlock lBBlock, lCurrentBBlock, lNextBBlock, lEntryBBlock, lExitBBlock;
    ArrayList lBBlockTable;
    if (fDbgLevel > 0)
      flow.dbg(2, "linkBBlockInDfoAndInverseDfo", lSubp.getName());
    lBBlockTable = fSubpFlow.getBBlockTable();
    lEntryBBlock = fSubpFlow.getEntryBBlock();
    List lBBlockWithNoSuccessors = findExitBBlock();
    int lSize = lBBlockWithNoSuccessors.size();
    if (lSize == 0)
      lExitBBlock = null;
    else {
      lExitBBlock = (BBlock)lBBlockWithNoSuccessors.get(lSize - 1);
      fSubpFlow.setExitBBlock(lExitBBlock);
      if (lSize > 1) { // There are multiple BBlocks with no successors.
        if (fDbgLevel > 0)
          flow.dbg(2, " There are multiple BBlocks with no successors",
                 " " + lSize);
        // Select the last one as the exit BBlock and make virtual edge
        // from others to the exit BBlock.
        for (int lIndex = 0; lIndex < lSize - 1; lIndex++) {
          BBlock lElem = (BBlock)lBBlockWithNoSuccessors.get(lIndex);
          lElem.addToSuccList(lExitBBlock);
          if (fDbgLevel > 0)
            flow.dbg(3, "  Make virtual edge from B" + lElem.getBBlockNumber(),
                   " to B" + lExitBBlock.getBBlockNumber());
        }
        lNormal = false;
      }
    }
    flowRoot.fSubpFlow.setPrevBBlockInSearch(null);
    lEntryBBlock.linkInDepthFirstOrder(lSubp);
    if (lExitBBlock != null)
      lExitBBlock.linkInInverseDepthFirstOrder(lSubp);
    return lNormal;
  } // linkBBlockInDfoAndInverseDfo

  private Stmt
    getNextStmtSeeingAncestor(Stmt pStmt)
  {
    if (pStmt == null)
      return null;
    if (fDbgLevel > 0)
      flow.dbg(5, " getNextStmtSeeingAncestor " + pStmt.toStringShort()); //###
    /* //##65
    if (pStmt instanceof JumpStmt) //##65
      return null; //##65
    if (pStmt instanceof ReturnStmt) //##65
      return null; //##65
    */ //##65
    if (pStmt.getNextStmt() != null)
      return pStmt.getNextStmt();
    // getNextStmt() is null. Get the next statement of ancestor.
    HIR lAncestor = pStmt;
    do {
      lAncestor = (HIR)lAncestor.getParent();
      if (lAncestor == null)
        return null;
      if (fDbgLevel > 0)
        flow.dbg(5, " ancestor " + lAncestor.toStringShort()); //###
      if ((lAncestor instanceof IfStmt) ||
          (lAncestor instanceof LoopStmt) ||
          //##65 (lAncestor instanceof SwitchStmt)||
          //##65 (lAncestor instanceof BlockStmt)) //##60
          (lAncestor instanceof SwitchStmt))
        return null;
      else if (lAncestor instanceof BlockStmt) //##65
        return getNextStmtSeeingAncestor((Stmt)lAncestor); //##65
      else if (lAncestor.getNextStmt() != null)
        return lAncestor.getNextStmt();
    }
    while (lAncestor != null);
    return null;
  } // getNextStmtSeeingAncestor
//##60 END

//##62 BEGIN
protected boolean
    isEndedWithJump( Stmt pStmt )
  {
    if (pStmt == null)
      return false;
    switch (pStmt.getOperator()) {
      case HIR.OP_JUMP:
      case HIR.OP_RETURN:
        return true;
      case HIR.OP_BLOCK:
        return isEndedWithJump(((BlockStmt)pStmt).getLastStmt());
      case HIR.OP_LABELED_STMT:
        return isEndedWithJump(((LabeledStmt)pStmt).getStmt());
      default:
       return false;
    }
  }
//##62 END
} // ControlFlowImpl class
