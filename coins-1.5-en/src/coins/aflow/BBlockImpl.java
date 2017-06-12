/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import coins.CompileError;
import coins.FlowRoot;
import coins.aflow.util.UnimplementedMethodException;
import coins.ir.IR;
import coins.ir.hir.Exp;
import coins.ir.hir.LoopStmt;
import coins.ir.hir.Stmt;
import coins.sym.Sym;


//                      (##10): modified on Dec. 2001.
public abstract class  //##9

// BBlockImpl extends FlowImpl implements BBlock
BBlockImpl implements BBlock //##10
 {
    //========================================//
    //======== Fields ========//
    //------ Public fields ------//
    public final FlowRoot flowRoot; // Used to access Root information.  //##8

    //------ Private/protected fields for each basic block ------//
    protected final int fBBlockNumber; // Id number of this BBlock.
    protected IR fIrLink;
    protected LinkedList fPredList; // Predecessor list.
    protected LinkedList fSuccList; // Successor list.
    protected LinkedList // List of edges to predecessors.
     fPredEdgeList; // Each entry has the same index as the

    // corresponding BBlock in fPredList.
    protected LinkedList // List of edges to succeassors.
     fSuccEdgeList; // Each entry has the same index as the

    // corresponding BBlock in fSuccList.
    protected BBlock fNextInDFO; // Next BBlock in depth first order.
    protected BBlock fNextInInverseDFO; // Next BBlock in inverse depth first order.
    private List fDom;
    private BBlock fImmediateDom;
    private List fDominatedChildren;
    private List fPostdom;
    private BBlock fImmediatePostdom;
    private List fPostdominatedChildren;
    protected Object fWork; // Information privately used in each phase.

    //------ Fields for data flow analysis ------//
    private final SubpFlow fSubpFlow;
    final FlowResults fResults;

    //====== Constructors ========//
    protected BBlockImpl(SubpFlow pSubpFlow) {
        fSubpFlow = pSubpFlow;
        fResults = pSubpFlow.results();
        flowRoot = fResults.flowRoot;
        fBBlockNumber = fSubpFlow.getNumberOfBBlocks() + 1;
        initiateFields();
    }

    protected void initiateFields() {
        fPredList = new LinkedList();
        fSuccList = new LinkedList();
        fPredEdgeList = new LinkedList();
        fSuccEdgeList = new LinkedList();

        //                fDom = new LinkedList();
        fDominatedChildren = new LinkedList();

        //                fPostdom = new LinkedList();
        //                fStrictPostdom = new LinkedList();
        fPostdominatedChildren = new LinkedList();
    }
     // initiateFields

    public IR getIrLink() {
        return fIrLink;
    }

    public int getBBlockNumber() {
        return fBBlockNumber;
    }

    public FlowResults results() {
        return fResults;
    }

    public SetRefReprList getSetRefReprs() {
        flowRoot.ioRoot.dbgFlow.print(5, " getSetRefReprs B "
          + this.getBBlockNumber()); //##25
        return (SetRefReprList) fResults.get("BBlockSetRefReprs", this);
    }

    public void setSetRefReprs(SetRefReprList pSetRefReprs) {
      flowRoot.ioRoot.dbgFlow.print(5, " setSetRefReprs B"
          + this.getBBlockNumber()); //##25
        fResults.put("BBlockSetRefReprs", this, pSetRefReprs);
    }

    //------ Methods for control flow analysis ------//
    public List getPredList() {
        return fPredList;
    }

    public List getSuccList() {
        return fSuccList;
    }

    public Edge getPredEdge(BBlock pFromBBlock) {
        int lIndex = fPredList.indexOf(pFromBBlock);

        if (lIndex >= 0) {
            return (Edge) (fPredEdgeList.get(lIndex));
        } else {
            return null;
        }
    }
     // getPredEdge

    public Edge getSuccEdge(BBlock pToBBlock) {
        int lIndex = fSuccList.indexOf(pToBBlock);

        if (lIndex >= 0) {
            return (Edge) (fSuccEdgeList.get(lIndex));
        } else {
            return null;
        }
    }
     // getSuccEdge

    public boolean isEntryBlock() {
        return fSubpFlow.getEntryBBlock() == this;
    }
     // isEntryBlock

    public boolean isEntryBBlock() {
        return fSubpFlow.getEntryBBlock() == this;
    }

    /** isExitBlock:
     *  See if this block is the exit block in the graph of
     *  basic blocks.
     *  @return true if this is the exit block in the graph of basic
     *      blocks, else return false.
     **/
    public boolean isExitBlock() {
        return fSubpFlow.getExitBBlock() == this;
    }

    // isExitBlock
    public boolean isExitBBlock() {
        return fSubpFlow.getExitBBlock() == this;
    }

    //---- Methods to set/get relations between basic blocks ----//
    public List getDomForSubpFlow() {
        return fDom;
    }

    public void setDomForSubpFlow(List pDom) {
        fDom = pDom;
    }

    public List getStrictDomForSubpFlow() {
        List lStrictDom;
        BBlock lBBlock;

        if (fDom instanceof ArrayList) {
            lStrictDom = (List) ((ArrayList) fDom).clone();
            lStrictDom.remove(this);
        } else if (fDom instanceof LinkedList) {
            lStrictDom = (List) ((LinkedList) fDom).clone();
            lStrictDom.remove(this);
        } else {
            lStrictDom = new LinkedList();

            for (Iterator lIt = fDom.iterator(); lIt.hasNext();) {
                lBBlock = (BBlock) lIt.next();

                if (lBBlock != this) {
                    lStrictDom.add(lBBlock);
                }
            }
        }

        return lStrictDom;
    }

    /** getImmediateDominator:
     *  Get a basic block immediately dominating this block.
     *  @return the basic block immediately dominating this block.
     **/
    public BBlock getImmediateDominatorForSubpFlow() {
        return fImmediateDom;
    }
     // getImmediateDominator

    public void setImmediateDominatorForSubpFlow(BBlock pDominator) {
        fImmediateDom = pDominator;
    }
     // getImmediateDominator

    /** getDominatedChildren:
     *  Get the list of basic blocks immediately dominated by this
     *  block. Elements of the list can be handled by List methods.
     *  @return the list of basic blocks immediately dominated by
     *      this block.
     **/
    public List getDominatedChildrenForSubpFlow() {
        return (List) fDominatedChildren;
    }
     // getDominatedChildren

    public void setDominatedChildrenForSubpFlow(List pDominatedChildren) {
        fDominatedChildren = pDominatedChildren;
    }
     // setDominatedChildren

    public List getPostdomForSubpFlow() {
        return fPostdom;
    }

    public void setPostdomForSubpFlow(List pPostdom) {
        fPostdom = pPostdom;
    }

    public List getStrictPostdomForSubpFlow() {
        List lStrictPostdom;
        BBlock lBBlock;

        if (fPostdom instanceof ArrayList) {
            lStrictPostdom = (List) ((ArrayList) fPostdom).clone();
            lStrictPostdom.remove(this);
        } else if (fPostdom instanceof LinkedList) {
            lStrictPostdom = (List) ((LinkedList) fPostdom).clone();
            lStrictPostdom.remove(this);
        } else {
            lStrictPostdom = new LinkedList();

            for (Iterator lIt = fPostdom.iterator(); lIt.hasNext();) {
                lBBlock = (BBlock) lIt.next();

                if (lBBlock != this) {
                    lStrictPostdom.add(lBBlock);
                }
            }
        }

        return lStrictPostdom;
    }

    /** getImmediatePostDominator:
     *  Get a basic block immediately post dominating this block.
     *  @return the basic block immediately post dominating this block.
     **/
    public BBlock getImmediatePostdominatorForSubpFlow() {
        return fImmediatePostdom;
    }
     // getImmediatePostDOminator

    public void setImmediatePostdominatorForSubpFlow(BBlock pPostDominator) {
         fImmediatePostdom = pPostDominator;
    }
     // getImmediatePostDOminator

    public List getPostdominatedChildrenForSubpFlow() {
        return (List) fPostdominatedChildren;
    }
     // getPostDominatorChildren

    public void setPostdominatedChildrenForSubpFlow(List pPostDominatedChildren) {
        fPostdominatedChildren = pPostDominatedChildren;
    }
     // setPostDominatedChildren

    public BBlock getNextInDFO() {
        return fNextInDFO;
    }

    public void setNextInDFO(BBlock pNext) {
        fNextInDFO = pNext;
    }

    public BBlock getNextInInverseDFO() {
        return fNextInInverseDFO;
    }

    public void setNextInInveseDFO(BBlock pNext) {
        fNextInInverseDFO = pNext;
    }

    //------ Get/set phase-wise information ------//
    public Object getWork() {
        return fWork;
    }

    public void setWork(Object pWork) {
        fWork = pWork;
    }

    //------ Methods for printing ------//
    public String toStringShort() //##10
     {
        StringBuffer lBuffer;
        BBlock lBBlock;
        lBuffer = new StringBuffer("BBlock " + fBBlockNumber + " pred(");

        for (Iterator lIterator = fPredList.iterator(); lIterator.hasNext();) {
            lBBlock = (BBlock) (lIterator.next());
            lBuffer.append(" " + lBBlock.getBBlockNumber());
        }

        lBuffer.append(") succ( ");

        for (Iterator lIterator = fSuccList.iterator(); lIterator.hasNext();) {
            lBBlock = (BBlock) (lIterator.next());
            lBuffer.append(" " + lBBlock.getBBlockNumber());
        }

        lBuffer.append(")");

        return lBuffer.toString();
    }
     // toStringShort

    public String toStringDetail() //##10
     {
        StringBuffer lBuffer;
        BBlock lBBlock;
        lBuffer = new StringBuffer();
        lBuffer = lBuffer.append(toStringShort()).append(getIrLink().toString());

        if (isEntryBlock()) {
            lBuffer.append(" isEntry");
        }

        if (isExitBlock()) {
            lBuffer.append(" isExit");
        }

        lBuffer.append(" Immediate Dominator for SubpFlow: " +
            getImmediateDominatorForSubpFlow());
        lBuffer.append(" Dominated Children for SubpFlow: " +
            getDominatedChildrenForSubpFlow());

        //		if (getFlag(BBlock.HAS_CALL))
        //			lBuffer.append(" hasCall");
        //		if (getFlag(BBlock.LOOP_HEAD))
        //			lBuffer.append(" loopHead");
        //		if (getFlag(BBlock.LOOP_TAIL))
        //			lBuffer.append(" loopTail");
        //		if (getFlag(BBlock.HAS_PTR_ASSIGN))
        //			lBuffer.append(" ptrAssign");
        //		if (getFlag(BBlock.USE_PTR))
        //			lBuffer.append(" usePtr");
        //		if (getFlag(BBlock.HAS_STRUCT_UNION))
        //			lBuffer.append(" hasStructUnion");
        return lBuffer.toString();
    }
     // toStringDetail

    /** printSubtrees:
     *  Print the sequence of subtrees contained in this block.
     *  The order of print is the same as that of bblockSubtreeIterator.
     *  "this" is any basic block.
     **/
    public void printSubtrees() {
        throw new UnimplementedMethodException();
    }
     // printSubtrees

    public BBlock insertLoopPreheader() {
        throw new UnimplementedMethodException();
    }
     // insertLoopPreheader

    /** insertConditionalInitPart:
     *  Insert a basic block as the conditional initiation block
     *  (conditionalInitBlock) of this loop (the loop starting with this
     *  basic block having loop header flag).
     *  The inserted conditionalInitBlock is preceeded by the
     *  expression statement built by copying the start condition of
     *  the loop so that the conditionalInitBlock is executed only when
     *  the start condition is satisfied.
     *  Edges from predecessors of this block are changed to
     *  go to the inserted start condition except loop back edge.
     *  The destination of the loop back edge is not changed.
     *  The conditionalInitBlock goes to loop body part of this loop
     *  treating the body part as its only one successor.
     *  Branch addresses of this block and its predecessors are
     *  changed so that consistency is kept.
     *  "this" should be a loop header basic block that dominates all
     *  basic blocks in the loop.
     *  @return the inserted conditionalInitBlock.
     **/
    public BBlock insertConditionalInitPart() throws CompileError {
        Stmt lLoopNode;
        Stmt lConditionalInit;

        if (flowRoot.isHirAnalysis()) { //##8

            //-- Modify HIR.
            lLoopNode = (Stmt) (getIrLink());

            if (lLoopNode instanceof LoopStmt) {
                lConditionalInit = ((LoopStmt) lLoopNode).getConditionalInitPart();

                if (lConditionalInit == null) {
                    ((LoopStmt) lLoopNode).addToConditionalInitPart(null);
                    lConditionalInit = ((LoopStmt) lLoopNode).getConditionalInitPart();
                } else {
                }
            } else { // Loop made by goto. //## REFINE
            }
        } else { // LIR analysis
        }

        //-- Maintain BBlock connections.
        return this; //## CHANGE
    }
     // insertConditionalInitPart

    public void changeSuccEdge(BBlock pBefore, BBlock pAfter) {
        deleteFromSuccList(pBefore);
        addToSuccList(pAfter);
    }
     // changeEdge

    public void changePredEdge(BBlock pBefore, BBlock pAfter) {
        deleteFromPredList(pBefore);
        addToPredList(pAfter);
    }

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
    public void addEdge(Exp pConditionalExp, BBlock pToBlock) {
        throw new UnimplementedMethodException();
    }
     // addEdge

    /** deleteEdge:
     *  Delete the edge from this block to pToBlock and adjust
     *  related addresses and links so as to keep consistency.
     *  If pToBlock has this block as its only one predecessor,
     *  then pToBlock is deleted.
     *  @param pToBlock: successor of this block.
     **/
    public void deleteEdge(BBlock pToBlock) {
        throw new UnimplementedMethodException();
    }
     // deleteEdge

    /** deleteBBlock:
     *  Delete this block and add successors of this block to the set of
     *  successors of all predecessors of this block. Branch statement
     *  or instructions in this block is moved to all predecessors of
     *  this block so as to keep consistency.
     *  "this" is a basic block whose predecessors have this block
     *  as their only one successor. If the successors have more than
     *  one successors, they should be modified before invoking
     *  this method.
     **/
    public void deleteBBlock() {
        throw new UnimplementedMethodException();
    }
     // deleteBBlock

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
    public void addToPredList(BBlock pPred) {
        int lIndex;
        Edge lEdge;
        List lList = getPredList();

        if (!lList.contains(pPred)) {
            lList.add(pPred);
            lIndex = lList.indexOf(pPred);
            lEdge = (Edge) (new EdgeImpl(flowRoot, pPred, this)); //##8
            fPredEdgeList.add(lIndex, lEdge);
            ((BBlockImpl) pPred).addToSuccList0(this, lEdge);
        }
    }
     // addToPredList

    public void addToSuccList(BBlock pSucc) {
        int lIndex;
        Edge lEdge;
        List lList = getSuccList();

        if (!lList.contains(pSucc)) {
            lList.add(pSucc);
            lIndex = lList.indexOf(pSucc);
            lEdge = (Edge) (new EdgeImpl(flowRoot, this, pSucc)); //##8
            fSuccEdgeList.add(lIndex, lEdge);
            ((BBlockImpl) pSucc).addToPredList0(this, lEdge);
        }
    }
     // addToSuccList

    private void addToPredList0(BBlock pPred, Edge pEdge) {
        int lIndex;
        Edge lEdge;
        List lList = getPredList();

        if (!lList.contains(pPred)) {
            lList.add(pPred);
            lIndex = lList.indexOf(pPred);
            fPredEdgeList.add(lIndex, pEdge);
        }
    }
     // addToPredList

    private void addToSuccList0(BBlock pSucc, Edge pEdge) {
        int lIndex;
        Edge lEdge;
        List lList = getSuccList();

        if (!lList.contains(pSucc)) {
            lList.add(pSucc);
            lIndex = lList.indexOf(pSucc);
            fSuccEdgeList.add(lIndex, pEdge);
        }
    }
     // addToSuccList

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
    public void deleteFromPredList(BBlock pPred) {
        int lIndex;
        List lList = getPredList();

        if (lList.contains(pPred)) {
            lIndex = lList.indexOf(pPred);
            lList.remove(pPred);
            fPredEdgeList.remove(lIndex);
            ((BBlockImpl) pPred).deleteFromSuccList0(this);
        }
    }
     // deleteFromPredList

    public void deleteFromSuccList(BBlock pSucc) {
        int lIndex;
        List lList = getSuccList();

        if (lList.contains(pSucc)) {
            lIndex = lList.indexOf(pSucc);
            lList.remove(pSucc);
            fSuccEdgeList.remove(lIndex);
            ((BBlockImpl) pSucc).deleteFromPredList0(this);
        }
    }
     // deleteFromSuccList

    private void deleteFromPredList0(BBlock pPred) {
        int lIndex;
        List lList = getPredList();

        if (lList.contains(pPred)) {
            lIndex = lList.indexOf(pPred);
            lList.remove(pPred);
            fPredEdgeList.remove(lIndex);
        }
    }

    public void deleteFromSuccList0(BBlock pSucc) {
        int lIndex;
        List lList = getSuccList();

        if (lList.contains(pSucc)) {
            lIndex = lList.indexOf(pSucc);
            lList.remove(pSucc);
            fSuccEdgeList.remove(lIndex);
        }
    }
     // deleteFromSuccList

    /** fuseSuccessor:
     *  Fuse the successor pToBlock with this block and make
     *  all successors of pToBlock as successors of this block.
     *  "this" is a basic block having pToBlock as its only one successor,
     *  and pToBlock should have "this" as its only one predecessor.
     *  Before invoking this method, this block should be changed
     *  to have only one successor pToBlock if the condition is
     *  not satisfied.
     * This class(<code>BBlockImpl</code>)'s implementation changes only the predecessor/successor relations.
     * @param pToBlock: only one succeccor of this block.
     *      pToBlock has only one predecessor and it is "this" block.
     **/
    public void fuseSuccessor(BBlock pToBlock) {
        BBlock lSuccOfSucc;
        List lSuccListOfSucc = pToBlock.getSuccList();

        for (Iterator lIt = lSuccListOfSucc.iterator(); lIt.hasNext();) {
            lSuccOfSucc = (BBlock) lIt.next();
            addToSuccList(lSuccOfSucc);
            ((BBlockImpl) lSuccOfSucc).deleteFromPredList0(pToBlock);
        }

        deleteFromSuccList(pToBlock);

        //		subpFlow().getReachableBBlocks().remove(pToBlock);
    }
     // fuseSuccessor

    public SubpFlow getSubpFlow() {
        return fSubpFlow;
    }

    public String toString() {
        return toStringVeryShort();
    }

    public String toStringVeryShort() {
        return "BBlock " + getBBlockNumber();
    }

    //------ Get/set bit vector for data flow analysis ------//

    /** getDef:
     *  Get bit vector showing Def set of this block.
     *  @return bit vector representing Def(B) of this block B.
     **/
    public DefVector getDef() {
        return (DefVector) fResults.get("Def", this);
    }

    public void setDef(DefVector pVect) {
        fResults.put("Def", this, pVect);
    }

    public DefVector getDDef() {
        return (DefVector) fResults.get("DDef", this);
    }

    public void setDDef(DefVector pVect) {
        fResults.put("DDef", this, pVect);
    }

    public DefVector getPDef() {
        return (DefVector) fResults.get("PDef", this);
    }

    public void setPDef(DefVector pVect) {
        fResults.put("PDef", this, pVect);
    }

    /** getKill:
     *  Get bit vector showing Kill set of this block.
     *  @return bit vector representing Kill(B) of this block B.
     **/
    public DefVector getDKill() {
        return (DefVector) fResults.get("DKill", this);
    }

    public void setDKill(DefVector pVect) {
        fResults.put("DKill", this, pVect);
    }

    /** getDestroy:
     *  Get bit vector showing Destroy set of this block.
     *  @return bit vector representing Destroy(B) of this block B.
     **/
    public DefVector getPKill() {
        return (DefVector) fResults.get("PKill", this);
    }

    public void setPKill(DefVector pVect) {
        fResults.put("PKill", this, pVect);
    }

    /** getReach:
     *  Get bit vector showing Reach set of this block.
     *  @return bit vector representing Reach(B) of this block B.
     **/
    public DefVector getReach() {
        return (DefVector) fResults.get("Reach", this);
    }

    public void setReach(DefVector pVect) {
        fResults.put("Reach", this, pVect);
    }

    /** getReach0:
     *  Get bit vector showing Reach0 set of this block.
     *  @return bit vector representing Reach0(B) of this block B.
     **/
    public DefVector getPReach() {
        return (DefVector) fResults.get("PReach", this);
    }

    public void setPReach(DefVector pVect) {
        fResults.put("PReach", this, pVect);
    }

//    /** getDReach:
//     *  Get bit vector showing DReach set of this block.
//     *  @return bit vector representing DReach(B) of this block B.
//     **/
//    public DefVector getDReach() {
//        return (DefVector) fResults.get("DReach", this);
//    }

    public void setDReach(DefVector pVect) {
        fResults.put("DReach", this, pVect);
    }

    /** getDefined:
     *  Get bit vector showing Defined set of this block.
     *  @return bit vector representing Defined(B) of this block B.
     **/
    public FlowAnalSymVector getDDefined() {
        return (FlowAnalSymVector) fResults.get("DDefined", this);
    }

    public void setDDefined(FlowAnalSymVector pVect) {
        fResults.put("DDefined", this, pVect);
    }

    /*        public FlowAnalSymVector getModified()
            {
                    return (FlowAnalSymVector)fResults.get("Modified", this);
            }

            public void      setModified( FlowAnalSymVector pVect )
            {
                    fResults.put("Modified", this, pVect);
            }
    */
    public FlowAnalSymVector getPDefined() {
        return (FlowAnalSymVector) fResults.get("PDefined", this);
    }

    public void setPDefined(FlowAnalSymVector pVect) {
        fResults.put("PDefined", this, pVect);
    }

    /** getUsed: //##12
     *  Get bit vector showing Used set of this block.
     *  @return bit vector representing Used(B) of this block B.
     **/
    public FlowAnalSymVector getPUsed() //##12
     {
        return (FlowAnalSymVector) fResults.get("PUsed", this);
    }

    public void setPUsed(FlowAnalSymVector pVect) //##12
     {
        fResults.put("PUsed", this, pVect);
    }

    /** getExposed:
     *  Get bit vector showing Exposed set of this block.
     *  @return bit vector representing Exposed(B) of this block B.
     **/
    public FlowAnalSymVector getDExposed() {
        return (FlowAnalSymVector) fResults.get("DExposed", this);
    }

    public void setDExposed(FlowAnalSymVector pVect) {
        fResults.put("DExposed", this, pVect);
    }

    /** getExposed:
     *  Get bit vector showing Exposed set of this block.
     *  @return bit vector representing Exposed(B) of this block B.
     **/
    public FlowAnalSymVector getPExposed() {
        return (FlowAnalSymVector) fResults.get("PExposed", this);
    }

    public void setPExposed(FlowAnalSymVector pVect) {
        fResults.put("PExposed", this, pVect);
    }

    /** getEGen:
     *  Get bit vector showing EGen set of this block.
     *  @return bit vector representing EGen(B) of this block B.
     **/
    public ExpVector getDEGen() {
        return (ExpVector) fResults.get("DEGen", this);
    }

    public void setDEGen(ExpVector pVect) {
        fResults.put("DEGen", this, pVect);
    }

    /** getEKill:
     *  Get bit vector showing EKill set of this block.
     *  @return bit vector representing EKill(B) of this block B.
     **/
    public ExpVector getPEKill() {
        return (ExpVector) fResults.get("PEKill", this);
    }

    public void setPEKill(ExpVector pVect) {
        fResults.put("PEKill", this, pVect);
    }

    /** getAvailIn:
     *  Get bit vector showing AvailIn set of this block.
     *  @return bit vector representing AvailIn(B) of this block B.
     **/
    public ExpVector getDAvailIn() {
        return (ExpVector) fResults.get("DAvailIn", this);
    }

    public void setDAvailIn(ExpVector pVect) {
        fResults.put("DAvailIn", this, pVect);
    }

    /** getAvailOut:
     *  Get bit vector showing AvailOut set of this block.
     *  @return bit vector representing AvailOut(B) of this block B.
     **/
    public ExpVector getDAvailOut() {
        return (ExpVector) fResults.get("DAvailOut", this);
    }

    public void setDAvailOut(ExpVector pVect) {
        fResults.put("DAvailOut", this, pVect);
    }

    /** getLiveIn:
     *  Get bit vector showing LiveIn set of this block.
     *  @return bit vector representing LiveIn(B) of this block B.
     **/
    public FlowAnalSymVector getPLiveIn() {
        return (FlowAnalSymVector) fResults.get("PLiveIn", this);
    }

    public void setPLiveIn(FlowAnalSymVector pVect) {
        fResults.put("PLiveIn", this, pVect);
    }

    /** getLiveOut:
     *  Get bit vector showing LiveOut set of this block.
     *  @return bit vector representing LiveOut(B) of this block B.
     **/
    public FlowAnalSymVector getPLiveOut() {
        return (FlowAnalSymVector) fResults.get("PLiveOut", this);
    }

    public void setPLiveOut(FlowAnalSymVector pVect) {
        fResults.put("PLiveOut", this, pVect);
    }

    /** getDefIn:
     *  Get bit vector showing DefIn set of this block.
     *  @return bit vector representing DefIn(B) of this block B.
     **/
    public FlowAnalSymVector getDDefIn() {
        return (FlowAnalSymVector) fResults.get("DDefIn", this);
    }

    public void setDDefIn(FlowAnalSymVector pVect) {
        fResults.put("DDefIn", this, pVect);
    }

    /** getDefOut:
     *  Get bit vector showing DefOut set of this block.
     *  @return bit vector representing DefOut(B) of this block B.
     **/
    public FlowAnalSymVector getDDefOut() {
        return (FlowAnalSymVector) fResults.get("DDefOut", this);
    }

    public void setDDefOut(FlowAnalSymVector pVect) {
        fResults.put("DDefOut", this, pVect);
    }

    //------ See data flow information of a symbol or expression ------//
    public boolean isDDef(SetRefRepr pSetRefRepr) {
        if (getDef().isSet(getSubpFlow().getSetRefReprs().indexOf(pSetRefRepr))) {
            return true;
        }

        return false;
    }
     // isDef

    public boolean isPDef(SetRefRepr pSetRefRepr) {
        if (getPDef().isSet(getSubpFlow().getSetRefReprs().indexOf(pSetRefRepr))) {
            return true;
        }

        return false;
    }
     // isMod

    public boolean isDKill(SetRefRepr pSetRefRepr) {
        if (getDKill().isSet(getSubpFlow().getSetRefReprs().indexOf(pSetRefRepr))) {
            return true;
        }

        return false;
    }
     // isKill

    public boolean isPKill(SetRefRepr pSetRefRepr) {
        if (getReach().isSet(getSubpFlow().getSetRefReprs().indexOf(pSetRefRepr))) {
            return true;
        }

        return false;
    }
     // isReach

    /** isReach:
     *  See if definition at position pPos reaches to the entry point
     *  of this block.
     *  @param pPos: position number attached to the definition node.
     *  @return true if the definition at pPos reaches to the entry point
     *      of this block, false otherwise.
     */
//    public boolean isDReach(SetRefRepr pSetRefRepr) {
//        if (getDReach().isSet(getSubpFlow().getSetRefReprs().indexOf(pSetRefRepr))) {
//            return true;
//        }
//
//        return false;
//    }

    public boolean isPReach(SetRefRepr pSetRefRepr) {
        if (getPReach().isSet(getSubpFlow().getSetRefReprs().indexOf(pSetRefRepr))) {
            return true;
        }

        return false;
    }
     // isReach0

    public boolean isDDefined(Sym pSym) {
        if (getDDefined().isSet(getSubpFlow().getSymIndexTable().indexOf(pSym))) {
            return true;
        }

        return false;
    }
     // isDefined

    public boolean isPDefined(Sym pSym) {
        if (getPDefined().isSet(getSubpFlow().getSymIndexTable().indexOf(pSym))) {
            return true;
        }

        return false;
    }
     // isModified

    public boolean isDUsed(Sym pSym) {
        if (getDUsed().isSet(getSubpFlow().getSymIndexTable().indexOf(pSym))) {
            return true;
        }

        return false;
    }
     // isUsed

    public boolean isPUsed(Sym pSym) {
        if (getPUsed().isSet(getSubpFlow().getSymIndexTable().indexOf(pSym))) {
            return true;
        }

        return false;
    }

    public boolean isDExposed(Sym pSym) {
        if (getDExposed().isSet(getSubpFlow().getSymIndexTable().indexOf(pSym))) {
            return true;
        }

        return false;
    }
     // isExposed

    public boolean isPExposed(Sym pSym) {
        if (getPExposed().isSet(getSubpFlow().getSymIndexTable().indexOf(pSym))) {
            return true;
        }

        return false;
    }

    public boolean isDEGen(FlowExpId pExpId) {
        int lInx = pExpId.getIndex();

        if (getDEGen().isSet(lInx)) {
            return true;
        } else {
            return false;
        }
    }
     // isEGen

    public boolean isPEKill(FlowExpId pExpId) {
        int lInx = pExpId.getIndex();

        if (getPEKill().isSet(lInx)) {
            return true;
        } else {
            return false;
        }
    }
     // isEKill

    public boolean isDAvailIn(FlowExpId pExpId) {
        int lInx = pExpId.getIndex();

        if (getDAvailIn().isSet(lInx)) {
            return true;
        } else {
            return false;
        }
    }
     // isAvailIn

    public boolean isDAvailOut(FlowExpId pExpId) {
        int lInx = pExpId.getIndex();

        if (getDAvailOut().isSet(lInx)) {
            return true;
        } else {
            return false;
        }
    }
     // isAvailOut

    public boolean isPLiveIn(Sym pSym) {
        if (getPLiveIn().isSet(getSubpFlow().getSymIndexTable().indexOf(pSym))) {
            return true;
        }

        return false;
    }
     // isLiveIn

    public boolean isPLiveOut(Sym pSym) {
        if (getPLiveOut().isSet(getSubpFlow().getSymIndexTable().indexOf(pSym))) {
            return true;
        }

        return false;
    }
     // isLiveOut

    public boolean isDDefIn(Sym pSym) {
        if (getDDefIn().isSet(getSubpFlow().getSymIndexTable().indexOf(pSym))) {
            return true;
        }

        return false;
    }
     // isDefIn

    public boolean isDDefOut(Sym pSym) {
        if (getDDefOut().isSet(getSubpFlow().getSymIndexTable().indexOf(pSym))) {
            return true;
        }

        return false;
    }

    public FlowAnalSymVector getDUsed() {
        return (FlowAnalSymVector) fResults.get("DUsed", this);
    }

    public void setDUsed(FlowAnalSymVector pVect) {
        fResults.put("DUsed", this, pVect);
    }

    /** isEKill:
     *  See if expression designated by pReg is killed
     *  (its operand is defined) in this block.
     *  @param pExpId: expression identifier assigned to the
     *      expression.
     *  @return true if the expression is killed in this block,
     *      false otherwise.
     */

    /*public boolean isDEKill(FlowExpId pExpId)
    {
            int lInx = pExpId.getIndex();
            if (getDEKill().isSet(lInx))
                    return true;
            else
                    return false;
    } // isEKill
     */

    // isDefOut
}
 // BBlockImpl class
