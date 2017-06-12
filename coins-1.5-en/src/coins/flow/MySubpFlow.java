/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.flow;

import coins.FlowRoot;
import coins.ir.hir.SubpDefinition;
import java.util.Iterator;

/**
 * MySubpFlow is an example of extending data flow analysis
 * by making subclass of HirSugpFlow.
 * This class adds new data flow information which can be get by
 *   getTransparent(bblock).
 * The method computeTransparent() of this calss is called from
 * FlowImpl if hirAnal option is specified.
 */
public class
  MySubpFlow
  extends HirSubpFlowImpl implements HirSubpFlow
{
  ExpVector fTransparent[];

  public
    MySubpFlow(FlowRoot pFlowRoot, SubpDefinition pSubpDefinition)
  {
    super(pFlowRoot, pSubpDefinition);
  } // MySubpFlow

  /**
   * The method computeTransparent finds tranparent expressions
   * that are neither killed nore defined within a basic block.
   */
  public void
    computeTransparent()
  {
    ExpVector lEKillAll;
    ExpVector lTemp1 = expVector();
    ExpVector lTemp2 = expVector();
    FlowAnalSymVector lDefined;
    int lBBlockNum;
    fTransparent = new ExpVector[fBBlockCount + 1]; // Get space
      // to record transparent vectors for all basic blocks.
    for (Iterator lIterator = cfgIterator();
         lIterator.hasNext(); ) { // Repeat for each basic block.
      BBlock lBBlock = (BBlock)lIterator.next();
      if (lBBlock == null)
        continue;
      lBBlockNum = lBBlock.getBBlockNumber(); // Get basic block number.
      fTransparent[lBBlockNum] = expVector(); // Initiate by zero vector.
      lEKillAll = lBBlock.getEKillAll(); // Get the cumulative set of
         //expressions killed by some statements in this BBlock.
      lEKillAll.vectorNot(lTemp1); // lTemp1 is negation of lEKillAll..
      lDefined = (FlowAnalSymVector)lBBlock.getDefined(); // Set of defined variables.
      lTemp2 = lDefined.flowAnalSymToExpVector(); // Change the set to vector.
      lTemp1.vectorSub(lTemp2, fTransparent[lBBlockNum]);
          // fTransparent[lBBlockNum] = lTemp1 - lTemp2
      if (fDbgLevel > 1)
        ioRoot.dbgFlow.print(2, "Transparent B"+lBBlockNum, fTransparent[lBBlockNum].toStringShort());
    }
    setComputedFlag(DF_TRSNSPARENT); // Set already-computed flag.
} // computeTransparent

  /**
   * Get the transparent expression for the basic block pBBlock.
   * Expressions are represented by ExpId correnponding to the expression.
   * @param pBBlock basic block.
   * @return extression vector showing transparent expressions.
   */
public ExpVector
getTransparent( BBlock pBBlock )
{
  if (! isComputed(DF_TRSNSPARENT)) // If already computed,
    computeTransparent();           // do not re-compute but reuse.
  return fTransparent[pBBlock.getBBlockNumber()];
} // getTransparent

} // MySubpFlow
