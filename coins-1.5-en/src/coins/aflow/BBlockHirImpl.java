/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow;

import java.util.LinkedList;
import java.util.Map; //##25

import coins.aflow.util.UnimplementedMethodException;
import coins.ir.IR;
import coins.ir.IrList; //##52
import coins.ir.hir.Exp;
import coins.ir.hir.HIR;
import coins.ir.hir.LabelDef; //##51
import coins.ir.hir.LabeledStmt;
import coins.ir.hir.Stmt;
import coins.sym.Const;
import coins.sym.Label;
import coins.sym.Sym;
import coins.sym.ExpId;      //##25
import java.util.HashMap;    //##25
import java.util.LinkedList; //##25
import java.util.List;       //##25
import java.util.ArrayList;  //##25
import java.util.Iterator;   //##52


/** BBlockHirImpl:
 *  Implements BBlock methods applicalble for HIR.
 **/
public class BBlockHirImpl extends BBlockImpl implements BBlockHir //##11
 {
    //======== Fields ========//
    private HIR // First subtree of this BBlock.
     fFirstSubtree = null; // Null while it is not yet obtained.
    private HIR // Last subtree of this BBlock.
     fLastSubtree = null; // Null while it is not yet obtained.

    /** fExpNodeListMap:               //##25
     * key: ExpId, value: list of Ext nodes other than assignment.
     */
    protected Map                      //##25
      fExpNodeListMap = new HashMap(); //##25

    protected BBlockHirImpl(LabeledStmt pLabeledStmt, SubpFlow pSubpFlow) {
    super(pSubpFlow);
    setIrLink(pLabeledStmt);
    flowRoot.aflow.dbg(4, "BBlockHirImpl", pLabeledStmt.toString()); //##25
    }

    //====== Interface for control flow analysis ======//
    //------ Methods to get attributes ------//

    /**
     * Sets the <code>LabeledStmt</code> and <code>Label</code> associated with this <code>BBlock</code>.
     */
    public void setIrLink(IR pIR) // (##6)
     {
        fIrLink = pIR;

        Label lLabel = ((LabeledStmt) pIR).getLabel();

        if (lLabel != null) {
          //##52 fResults.setBBlockForLabel(lLabel, this);
          //##52 BEGIN
          IrList lLabelDefList = ((LabeledStmt) pIR).getLabelDefList();
          for (Iterator lIterator = lLabelDefList.iterator();
               lIterator.hasNext(); ) {
            LabelDef lLabelDef = (LabelDef)lIterator.next();
            Label lDefLabel = lLabelDef.getLabel();
            fResults.setBBlockForLabel(lDefLabel, this);
          }
          //##52 END
        }
    }

    public Label getLabel() {
        Label lLabel = null;
        Sym lSym;

        if (fIrLink != null) {
            return ((LabeledStmt) fIrLink).getLabel();
        }

        return null;
    }
     // getLabel

    //------ Iterators and methods for traversing ------//
    public BBlockSubtreeIterator bblockSubtreeIterator() {
        return (BBlockSubtreeIterator) (new BBlockStmtIterator(this)); //##8
    }

    public BBlockNodeIterator bblockNodeIterator() //##31
    {
        return (BBlockNodeIterator) (new BBlockHirNodeIteratorImpl(this)); //##8
    }

     //------ Methods for printing ------//

    /** printSubtrees:
     *  Print the sequence of subtrees contained in this block.
     *  The order of print is the same as that of bblockSubtreeIterator.
     *  "this" is any basic block.
     **/
    public void printSubtrees() {
        BBlockSubtreeIterator lIterator;
        HIR lSubtree;
        flowRoot.ioRoot.printOut.print("\nBBlock %d subtrees" +
            getBBlockNumber());

        for (lIterator = bblockSubtreeIterator(); lIterator.hasNext();) {
            lSubtree = (HIR) lIterator.next();

            if (lSubtree != null) {
                lSubtree.print(2); // Print with indent 2.
            }
        }
    }
     // printSubtrees

    public BBlock splitTailPart(Stmt pStmt) {
        BBlock lNewBBlock;
        Label lNewLabel = flowRoot.symRoot.symTableCurrent.generateLabel();
        LabeledStmt lNewLabeledStmt;
        lNewLabeledStmt = pStmt.attachLabel(lNewLabel);
        lNewBBlock = getSubpFlow().bblock(lNewLabeledStmt);

        //-- Change predecessor/successor relations.
        ((BBlockImpl) lNewBBlock).fSuccList = fSuccList;
        ((BBlockImpl) lNewBBlock).fPredList = new LinkedList();
        fSuccList = new LinkedList();
        addToSuccList(lNewBBlock);
        lNewBBlock.addToPredList(this);

        //		flowRoot.aflow.setFlowAnalStateLevel(FlowRoot.STATE_CFG_RESTRUCTURING);
        return lNewBBlock;
    }
     // splitTailPart

    /** addSwitchCase:
     *  Add case selection part of switch statement contained in this
     *  block and adjust linkages between basic blocks
     *  (to change multiway jumps in HIR).
     *  If basic blocks corresponding to pLabeledStmt of the case
     *  selection part is not yet constructed, then they should be
     *  constructed by divideIntoBBlocks before calling
     *  addSwitchCase.     (##4)
     *  @param pConst: case selection constant.
     *  @param pLabeledStmt: labeled statement which is to be executed
     *      when case selection expression is evaluated as pConst.
     **/
    public void addSwitchCase(Const pConst, Stmt pLabeledStmt) {
        throw new UnimplementedMethodException();
    }
     // addSwitchCase

    /** deleteSwitchCase:                (##4)
     *  Delete a case selection constant of switch statement contained
     *  in this block. If all constants of a case selection statement
     *  are deleted, then the case selection statement itself is also
     *  deleted and linkages between basic blocks are adjusted
     *  (to change multiway jumps in HIR).
     *  @param pconst: case selection constant which is to be deleted.
     **/
    public void deleteSwitchCase(Const pConst) { // (##4)
        throw new UnimplementedMethodException();
    }
     // deleteSwitchCase

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
     */
    public void fuseSuccessor(BBlock pToBlock) {
        throw new UnimplementedMethodException();
    }

    // Factory methods.

    /** Returns the <code>SetRefRepr</code> object enclosing the given IR.
     */
    public SetRefRepr setRefRepr(IR pIR) {
      if (((SubpFlowImpl)getSubpFlow()).fHirAnalExtended)     //##25
        return new SetRefReprHirEImpl((HIR)pIR, this, false, null); //##25
      else                                                    //##25
        return new SetRefReprHirImpl((Stmt) pIR, this);
    }

    /*        public FlowAnalSymVector getDExposed()
            {
                    return (FlowAnalSymVector)fResults.get("DExposed", this);
            }
    */

    /** Returns a <code>FlowAnalSymVector</code> whose set bits correspond to <code>FlowAnalSym</code>s that are <em>definitely</em> defined in this <code>BBlock</code>. Only scalar variables that are obviously set (i.e. that appear in the top node of the LHS of an AssignStmt) are included in the set that corresponds to this <code>BitVector</code>.
     */

    /*        public FlowAnalSymVector getDefined()
            {
            }
    */

    /** isKill:
     *  See if definition at position pPos is killed in this block.
     *  @param pPos: position number attached to the definition node.
     *  @return true if the definition at pPos is killed in this block,
     *      false otherwise.
     */

    /*        public boolean isKill(SetRefRepr pSetRefRepr)
            {
            }
    */
    /*        public void setDExposed(FlowAnalSymVector pVect)
            {
            }
    */

 //##25 BEGIN

   /** addToExpNodeList: //##25
    *  Add pExpNode to the list corresponding to pExpId to show
    *  that the expression for pExpId is computed at pExpNode.
    *  @param pExpId: ExpId for pExpNode.
    *  @param pExpNode: Expression other than LHS of assignment.
    */

   public void //##25
   addToExpNodeList( ExpId pExpId, HIR pExpNode )
   {
     List lNodeList;
     if (! fExpNodeListMap.containsKey(pExpId)) {
       lNodeList = new ArrayList();
       fExpNodeListMap.put(pExpId, lNodeList);
     }else
       lNodeList = (List)fExpNodeListMap.get(pExpId);
     flowRoot.ioRoot.dbgOpt1.print(5, " addToExpNodeList", pExpNode.toStringShort()
                                   + " " + pExpId + " " + this);
     lNodeList.add(pExpNode);
   } // addToExpNodeList

   public List //##25
   getExpNodeList( ExpId pExpId ) {
     if (fExpNodeListMap.containsKey(pExpId)) {
       return (List)fExpNodeListMap.get(pExpId);
     }else
       return null;
   } // getExpNodeList

public Stmt
getFirstStmt()
{
     return (Stmt)getIrLink();
} // getFirstStmt

public Stmt
getLastStmt()
{
  Stmt lPreviousStmt = null;
  Stmt lStmt;
  for (BBlockSubtreeIterator lIterator = bblockSubtreeIterator();
          lIterator.hasNext(); ) {
    lStmt = (Stmt)lIterator.next();
    if (lStmt == null)
      break;
    else
      lPreviousStmt = lStmt;
  }
  return lPreviousStmt;
} // getLastStmt

//##25 END

} // BBlockHirImpl class
