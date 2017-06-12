/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.flow;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import coins.FlowRoot;
import coins.ir.IR;
import coins.ir.hir.AssignStmt;
import coins.ir.hir.Exp;
import coins.ir.hir.HIR;
import coins.ir.hir.HirIterator;
import coins.sym.ExpId;
import coins.sym.FlowAnalSym;
import coins.sym.Sym;
import coins.sym.Var; //##60

//   Modified on Jan. 2002 by Tan
//     to prepare for LIR data flow analysis.
// DataFlowLirImpl is added as a subclass of DataFlowImpl
// so that to implement data flow analysis for LIR.
// "private" is changed to "protected" to allow access
// from the subclass DataFlowLirImpl.
//   Modified on Dec. 2001. by Tan
/**
 * Implementation of the DataFlow interface for HIR.
 *
 * There are some dependencies between the methods in this class.
 * For example, findReach() mustn't be called before both findDef() and findKill() have been called,
 * and findKill() in turn depends on the result of findDefined().
 * <b>Access restrictions (or the like) that reflect these dependencies are not yet implemented.</b> Please be careful in using.
 * {@link #findAll()} method calls these methods in the correct order.
 */
public class DataFlowHirImpl
  extends DataFlowImpl
{
  /**
   * Constructs a DataFlow instance and prepares for DFA.
   */
  public
    DataFlowHirImpl(FlowRoot pFlowRoot, HirSubpFlow pSubpFlow)
  {
    super(pFlowRoot, pSubpFlow);
    //##60 setRefRepr=new SetRefReprImpl(flowRoot); // Pass flowRoot.
    flowRoot.dataFlow = this; //##62
    initiateDataFlow();
  } // DataFlowHirImpl

//##60 END

  /**
   *
   * getExpId
   *
   **/
  // public ExpId  getExpId(int ExpIndex)
  public FlowAnalSym getFlowAnalSym(int ExpIndex)
  {
    //return (ExpId)fSubpFlow.getIndexedSym(ExpIndex);
    //##65 return fFlowAnalSymTable[ExpIndex];
    return fSubpFlow.getFlowAnalSymTable()[ExpIndex]; //##65
  }

  /**
   * Finds and sets the EGen vector for the given BBlock.
   * This also does VectorOr the EKill bit that corresponds
   * to the ExpId killed by the EGen symbol. //##62
   * @param pBBlock BBlock whose EGen vector to find.
   */
//##60 BEGIN
  public void findEGen(BBlock pBBlock)
  {
    //##63 List lBBlockSetRefReprList = (List)pBBlock.getWorkFA();
    SetRefReprList lBBlockSetRefReprList = fSubpFlow.getSetRefReprList(pBBlock); //##63
    SetRefRepr lSetRefRepr;
    Set lEGenSet = new HashSet();
    Set lEKillSet = new HashSet();
    Set lEKillAllSet = new HashSet(); //##62
    ExpVector lEGen, lEKill, lEKillAll;
    if (fDbgFlow > 0)
      flow.dbg(5, "\nFindEGen HIR B" + pBBlock.getBBlockNumber());
    if (fDbgFlow > 4)
      flow.dbg(7, "BBlockSetRefReprList " + lBBlockSetRefReprList);

    if (lBBlockSetRefReprList != null) {
      for (Iterator lBBlockSetRefReprIterator = lBBlockSetRefReprList.iterator();
           lBBlockSetRefReprIterator.hasNext(); ) {
        lSetRefRepr = (SetRefRepr)lBBlockSetRefReprIterator.next();
        // lSetRefRepr may include items corresponding to all subexpressions
        // of a source program expression such as qualifying expression.
        // If its subexpression is already killed, then the subexpression
        // should be removed from EGen.
        addEGenExpId(lEGenSet, lEKillSet, lSetRefRepr);
        lEKillAllSet.addAll(lEKillSet); //##62
        lEKillSet.removeAll(lEGenSet); //##65
      }
    }
    //##65 lEGen = pBBlock.getEGen();
    lEGen = ((BBlockImpl)pBBlock).getEGenVector(); //##65
    lEGen.vectorOr(ExpVectorImpl.expVectorFromSet(lEGenSet, fSubpFlow),
                   lEGen);
    //##65 lEKill = pBBlock.getEKill();
    lEKill = ((BBlockImpl)pBBlock).getEKillVector(); //##65
    lEKill.vectorOr(ExpVectorImpl.expVectorFromSet(lEKillSet, fSubpFlow),
                   lEKill);
    //##65 lEKillAll = pBBlock.getEKillAll();
    lEKillAll = ((BBlockImpl)pBBlock).getEKillAllVector(); //##65
    lEKillAll.vectorOr(ExpVectorImpl.expVectorFromSet(lEKillAllSet, fSubpFlow),
                  lEKillAll);
    if (ioRoot.dbgFlow.getLevel() >= 5) { //##60
      ioRoot.dbgFlow.print(5, " EGen of B" + pBBlock.getBBlockNumber(),
        ((BBlockImpl)pBBlock).getEGenVector().toStringShort()); //##65
      ioRoot.dbgFlow.print(5, " EKill of B"+ pBBlock.getBBlockNumber(),
        ((BBlockImpl)pBBlock).getEKillVector().toStringShort()); //##65
      ioRoot.dbgFlow.print(5, " EKillAll of B"+ pBBlock.getBBlockNumber(),
        ((BBlockImpl)pBBlock).getEKillAllVector().toStringShort()); //##65
    }
  } // findEGen
//##60 END

//##62 BEGIN
  /**
   * Finds and sets the EGen vector for the given BBlock.
   *
   * @param pBBlock BBlock whose EGen vector to be found.
   */
  public void
    findEKill(BBlock pBBlock)
  {
    // EKill set is computed by findEGen. //##62
  } // findEKill
//##62 END
  /** !!HIR
   * Returns the Set of ExpIds that fall under the given subtree and are used.
   * The ExpId that is attached to the "Def node" will not be included if the given subtree is a value-setting node (AssignStmt in HIR).
   * The ExpId that corresponds to the given subtree is also included in the Set. Supports HIR only so far.
   *
   * @param pSubtree IR node that is the root of the subtree to examine.
   * @return the Set of ExpIds that fall under the given subtree and are used.
   */
  // public java.util.Set getUseExpIds(IR pSubtree)
  public java.util.Set getUseFlowAnalSymsForHir(HIR pSubtree)
  {
    java.util.Set lUse = new HashSet();
    if (pSubtree == null)
      return lUse;
    IR lDefNode;
    HirIterator lHirIterator = hirRoot.hir.hirIterator(pSubtree);
    // ExpId lExpId;
    FlowAnalSym lExpId;
    if (fDbgFlow > 3)
      ioRoot.dbgFlow.print(7, "getUseFlowAnalSymsForHir ",
        pSubtree.toStringShort());
    if (pSubtree.getOperator() == HIR.OP_ASSIGN)
      lDefNode = ((AssignStmt)pSubtree).getLeftSide();
      // lDefNode will be excluded from the Set.
    else
      lDefNode = null;
    for (IR lIR = lHirIterator.getNextExecutableNode(); lIR != null;
         lIR = lHirIterator.getNextExecutableNode()) { // Iterate through nodes under lIR.
      //##60 if (lIR != lDefNode && (lExpId = ((HIR)lIR).getExpId()) != null)
      //##60   lUse.add(((HIR)lIR).getExpId());
      if (lIR != lDefNode) { //##60
        // If the node is not lDefNode and is attached an ExpId,
        // Add the ExpId attached to the node to the Set.
        lExpId = fSubpFlow.getExpId(lIR); //##60
        if (lExpId != null) //##60
          lUse.add(lExpId); //##60
      }
    }
    return lUse;
  }

  /**
     * Returns the Set of ExpIds that are contained in the given ExpId and are used.
   * The ExpId that is attached to the "Def node" will not be included if the given ExpId is attached to a value-setting node (AssignStmt in HIR).
   * The given ExpId is also contained in the returned Set.
   * See #getUseExpIds(IR)
   *
   * @param pExpId ExpId whose content to be extracted.
   * @return the Set of ExpIds that are contained and used.
   */
  // java.util.Set getUseExpIds(ExpId pExpId)
  //	return getUseExpIds(pExpId.getLinkedNode());
  public java.util.Set
    getUseFlowAnalSyms(FlowAnalSym pFlowAnalSym)
  {
    //##60 return ((DataFlowHirImpl)this).getUseFlowAnalSyms(((ExpId)pFlowAnalSym).getLinkedNode());
    HIR lLinkedNode = ((SubpFlowImpl)fSubpFlow).getLinkedSubtreeOfExpId((ExpId)
      pFlowAnalSym); //##60
    return ((DataFlowHirImpl)this).getUseFlowAnalSyms(lLinkedNode); //##60
  }

} // DataFlowHirImpl
