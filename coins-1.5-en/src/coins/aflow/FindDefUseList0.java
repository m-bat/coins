/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import coins.aflow.util.FAList;
import coins.aflow.util.ListValuedMap;
import coins.ir.IR;
import coins.sym.Sym;


/**
 * Finds <code>DefUseList</code>s for <code>FlowExpId/code>s.
 */
public class FindDefUseList0 extends FlowAdapter
{
	private Set fDefinedFlowExpIds = new HashSet();
	
	public final Flow flow;
	//        private AnalOptions fOpts;
	//        private boolean fIsCDefUseCheck = false;
	public FindDefUseList0(FlowResults pResults)
	{
		super(pResults);
		flow = flowRoot.aflow;
	}
	
	/**
	 * Finds <code>DefUseList</code>s for all the <code>FlowExpId</code>s in <code>pSubpFlow</code>.
	 */
	public void find(SubpFlow pSubpFlow)
	{
		BBlock lBBlock;
		FlowExpId lFlowExpId;
		
		for (Iterator lFlowExpIdIt = pSubpFlow.getFlowExpIdTable().iterator(); lFlowExpIdIt.hasNext();)
		{
			lFlowExpId = (FlowExpId)lFlowExpIdIt.next();
			fResults.put("DefUseList0", lFlowExpId, pSubpFlow, flow.defUseList());
			fResults.put("UDList0", lFlowExpId, pSubpFlow, flow.udList());
		}
		
		for (Iterator lIt = pSubpFlow.getBBlocks().iterator(); lIt.hasNext();)
		{
			lBBlock = (BBlock) lIt.next();
			find(lBBlock);
		}
		
		// Register unused FlowExpIds to UD-chains with empty use nodes. Unnecessary?
		DefUseCell lDefUseCell;
		FlowExpId lDefined;
		
		for (Iterator lIt = fDefinedFlowExpIds.iterator(); lIt.hasNext();)
		{
			lDefined = (FlowExpId) lIt.next();
			
			Iterator lDefUseCellListIt = ((DefUseListImpl) fResults.get("DefUseList0",
			lDefined, pSubpFlow)).fDefUseCellList.iterator();
			
			for (; lDefUseCellListIt.hasNext();)
			{
				lDefUseCell = (DefUseCell) lDefUseCellListIt.next();
				
				if (lDefUseCell.getUseList().isEmpty())
				{
					((UDListImpl) fResults.get("UDList0", lDefined, pSubpFlow)).getOrAddUDChain(UDChain.UNUSED)
					.addDefNode(lDefUseCell.getDefNode());
				}
			}
		}
	}
	
	public void find(FlowExpId pFlowExpId, SubpFlow pSubpFlow)
	{
		find(pSubpFlow);
	}
	
	private void find(BBlock pBBlock)
	{
		SubpFlow lSubpFlow = pBBlock.getSubpFlow();
		SetRefRepr lSetRefRepr;
		IR lUseNode;
		IR lDefNode;
		FlowExpId lDefFlowExpId, lFlowExpId;
		DefVector lDReach, lPReach;
		DefVectorIterator lDReachIt, lPReachIt;
		SetRefRepr lDReachSetRefRepr, lPReachSetRefRepr;
		DefUseList lDefUseList;
		UDList lUDList;
		int lPos;
		IR lCallNode;
		Set lFModFlowExpIds = new HashSet();
		ArrayList lCallNodeStack = new ArrayList();
		FAList lSymIndexTable = lSubpFlow.getSymIndexTable();
		ListValuedMap lSymToDDefNode = new ListValuedMap();
		ListValuedMap lSymToPDefNode = new ListValuedMap();
		Set lDDefFlowExpIds = new HashSet();
//		lDReach = pBBlock.getDReach();
//		lDReachIt = lDReach.defVectorIterator();
		IR lIR;
		Set lModLvalues;
		
		while (true)
		{
//			lDReachSetRefRepr = lDReachIt.nextSetRefRepr();
//			if (lDReachSetRefRepr == null)
				break;
//			lDefFlowExpId = lDReachSetRefRepr.defFlowExpId();
//			if (lDefFlowExpId != null)
//			{
//				((List)lSymToDDefNode.get(lDefFlowExpId)).add(lDReachSetRefRepr.getIR());
//				((List)lSymToPDefNode.get(lDefFlowExpId)).add(lDReachSetRefRepr.getIR());
//			}
		}
		
		lPReach	= pBBlock.getPReach();
		lPReachIt = lPReach.defVectorIterator();
		while (true)
		{
			lPReachSetRefRepr = lPReachIt.nextSetRefRepr();
			if (lPReachSetRefRepr == null)
				break;
			
			if (lPReachSetRefRepr.sets())
			{
				lModLvalues = FlowUtil.modLvalues(lPReachSetRefRepr, fResults);
				
				//			for (Iterator lLHSIt = lSetRefRepr.defNode()
				for (Iterator lDefFlowExpIdIt = lModLvalues.iterator(); lDefFlowExpIdIt.hasNext();)
				{
					lFlowExpId = (FlowExpId)lDefFlowExpIdIt.next();
					if (!lSymToDDefNode.containsKey(lFlowExpId))
						((List)lSymToPDefNode.get(lFlowExpId)).add(lPReachSetRefRepr.getIR());
					else
					{
						Set lDominatorBBlocks = new HashSet();
						for (Iterator lDReachIterator = ((List)lSymToDDefNode.get(lFlowExpId)).iterator(); lDReachIterator.hasNext();)
						{
							lDominatorBBlocks.add(fResults.get("BBlockForNode", lDReachIterator.next()));
						}
						if (!postdominates(lDominatorBBlocks, lPReachSetRefRepr.getBBlock(), pBBlock))
							((List)lSymToPDefNode.get(lFlowExpId)).add(lPReachSetRefRepr.getIR());
					}
				}
			}
			for (Iterator lCallNodeIt = lPReachSetRefRepr.callNodes().iterator(); lCallNodeIt.hasNext();)
			{
				lCallNode = (IR)lCallNodeIt.next();
				for (Iterator lCallModSymsIt = callModLvalues(lCallNode, lSubpFlow).iterator(); lCallModSymsIt.hasNext();)
				{
					FlowExpId lCallModSym = (FlowExpId)lCallModSymsIt.next();
					if (!lSymToDDefNode.containsKey(lCallModSym))
						((List)lSymToPDefNode.get(lCallModSym)).add(lCallNode);
					else
					{
						Set lDominatorBBlocks = new HashSet();
						for (Iterator lDReachIterator0 = ((List)lSymToDDefNode.get(lCallModSym)).iterator(); lDReachIterator0.hasNext();)
						{
							lDominatorBBlocks.add(fResults.get("BBlockForNode", lDReachIterator0.next()));
						}
						if (!postdominates(lDominatorBBlocks, lPReachSetRefRepr.getBBlock(), pBBlock))
							((List)lSymToPDefNode.get(lCallModSym)).add(lCallNode);
					}
				}
			}
		}
	//	lDDefFlowExpIds.addAll(lSymToDDefNode.keySet());
		
		
		
		for (Iterator lIt = ((SetRefReprList) fResults.get(
		"BBlockSetRefReprs", pBBlock)).iterator(); lIt.hasNext();)
		{
			lSetRefRepr = (SetRefRepr) lIt.next();
			for (Iterator lUseNodeIt = lSetRefRepr.expIterator();
			lUseNodeIt.hasNext();)
			{
				lUseNode = (IR) lUseNodeIt.next();
				if (!lCallNodeStack.isEmpty() && !FlowUtil.isUnder((lCallNode = (IR)lCallNodeStack.get(lCallNodeStack.size() - 1)), lUseNode))
				{
					handleCall(lCallNode, lSubpFlow, lDDefFlowExpIds, lSymToPDefNode);
					lCallNodeStack.remove(lCallNodeStack.size() - 1);
				}
				//				System.out.println("lUseNode = " + lUseNode);
				if (fResults.getFlowExpIdForNode(lUseNode) != null && FlowUtil.isLvalue(lUseNode) &&
				!FlowUtil.notDereferenced(lUseNode))
				{
					lFlowExpId = (FlowExpId)fResults.getFlowExpIdForNode(lUseNode);
				} else if (FlowUtil.isCall(lUseNode))
				{
					lCallNodeStack.add(lUseNode);
					continue;
				} else
					continue;
				
				
				//
				lDefUseList = (DefUseList) fResults.getRaw("DefUseList0",
				lFlowExpId, lSubpFlow);
				lUDList = (UDList) fResults.getRaw("UDList0", lFlowExpId,
				lSubpFlow);
				
				//					FlowExpId lFlowExpId = lFlowExpIdIt.next();
				if (FlowUtil.isLvalue(lFlowExpId.getLinkedNode()))
					for (Iterator lFlowExpIdIt = lSubpFlow.getFlowExpIdTable().iterator(); lFlowExpIdIt.hasNext();)
					{
						FlowExpId lFlowExpIdRef = (FlowExpId)lFlowExpIdIt.next();
						if (FlowUtil.isLvalue(lFlowExpIdRef.getLinkedNode()))
							if (FlowUtil.possiblyOverlaps(lFlowExpId, lFlowExpIdRef, lSubpFlow))
							{
								for (Iterator lDefNodeIt = ((List)lSymToPDefNode.get(lFlowExpIdRef)).iterator(); lDefNodeIt.hasNext();)
								{
									lDefNode = (IR)lDefNodeIt.next();
									((DefUseListImpl)lDefUseList).getOrAddDefUseCell(lDefNode)
									.addUseNode(lUseNode);
									((UDListImpl) lUDList).getOrAddUDChain(lUseNode)
									.addDefNode(lDefNode);
								}
							}
					}
				
				
				if (!lDDefFlowExpIds.contains(lFlowExpId)) // Definite assignment check.
				{
					Sym lFlowExpIdSym = lFlowExpId.getTree().getSym();
					if (lFlowExpIdSym != null && lFlowExpIdSym.getSymKind() == Sym.KIND_PARAM)
					{
						((DefUseListImpl) lDefUseList).getOrAddDefUseCell(DefUseCell.PARAM)
						.addUseNode(lUseNode);
						((UDListImpl) lUDList).getOrAddUDChain(lUseNode)
						.addDefNode(UDChain.PARAM);
					} else
					{
						((DefUseListImpl) lDefUseList).getOrAddDefUseCell(DefUseCell.UNINITIALIZED)
						.addUseNode(lUseNode);
						((UDListImpl) lUDList).getOrAddUDChain(lUseNode)
						.addDefNode(UDChain.UNINITIALIZED);
					}
				}
				
			}
			
			while (!lCallNodeStack.isEmpty())
			{
				lCallNode = (IR)lCallNodeStack.get(lCallNodeStack.size() - 1);
				handleCall(lCallNode, lSubpFlow, lDDefFlowExpIds, lSymToPDefNode);
				lCallNodeStack.remove(lCallNodeStack.size() - 1);
			}
			
			if (lSetRefRepr.sets())
			{
				lDefFlowExpId = lSetRefRepr.defFlowExpId();
				if (lDefFlowExpId != null)
				{
					//						((List)lSymToDDefNode.get(lDefFlowExpId)).clear();
					((List)lSymToPDefNode.get(lDefFlowExpId)).clear();
					lDDefFlowExpIds.add(lDefFlowExpId);
					((List)lSymToPDefNode.get(lDefFlowExpId)).add(lSetRefRepr.getIR());
					
					((DefUseListImpl)fResults.get("DefUseList0", lDefFlowExpId, lSubpFlow)).getOrAddDefUseCell(lSetRefRepr.getIR());
					
					fDefinedFlowExpIds.add(lDefFlowExpId);
					
				} else
				{
					for (Iterator lModIt = FlowUtil.modLvalues(lSetRefRepr, fResults).iterator();
					lModIt.hasNext();)
					{
						lFlowExpId = (FlowExpId) lModIt.next();
						//							((List)lSymToDDefNode.get(lFlowExpId)).clear();
						((List)lSymToPDefNode.get(lFlowExpId)).add(lSetRefRepr.getIR());
						
						((DefUseListImpl)fResults.getRaw("DefUseList", lFlowExpId, lSubpFlow)).getOrAddDefUseCell(lSetRefRepr.getIR());
						
						fDefinedFlowExpIds.add(lFlowExpId);
						
					}

				}
			}
		}
	}
	
	protected void handleCall(IR pCallNode, SubpFlow pSubpFlow, Set pDDefSyms, ListValuedMap pSymToPDefNode)
	{
		FlowExpId lFlowExpId;
		IR lDefNode;
		DefUseList lDefUseList;
		UDList lUDList;
		
		for (Iterator lIt = pSubpFlow.getFlowExpIdTable().iterator(); lIt.hasNext();)
		{
			lFlowExpId = (FlowExpId)lIt.next();
			//				((List)pSymToDDefNode.get(lFlowExpId)).clear();
			pSymToPDefNode.addUnique(lFlowExpId, pCallNode);
			
			lDefUseList = (DefUseList) fResults.getRaw("DefUseList0",
			lFlowExpId, pSubpFlow);
			lUDList = (UDList) fResults.getRaw("UDList0", lFlowExpId,
			pSubpFlow);
			
			for (Iterator lDefNodeIt = ((List)pSymToPDefNode.get(lFlowExpId)).iterator(); lDefNodeIt.hasNext();)
			{
				lDefNode = (IR)lDefNodeIt.next();
				((DefUseListImpl)lDefUseList).getOrAddDefUseCell(lDefNode)
				.addUseNode(pCallNode);
				((UDListImpl) lUDList).getOrAddUDChain(pCallNode)
				.addDefNode(lDefNode);
			}
			
		}
	}
	
	private static boolean postdominates(Set pPostdominatorBBlocks, BBlock pDominatedBBlock, BBlock pExitBBlock)
	{
		final Object lVisitedFlag = new Object();
		//				if (pDominatedBBlock == pExitBBlock && pPostdominatorBBlocks.contains(pDominatedBBlock))
		//					return true;
		return !search(pDominatedBBlock, pExitBBlock, pPostdominatorBBlocks, lVisitedFlag);
	}
	
	private static boolean search(BBlock pCurrent, final BBlock pGoal, final Set pObstacles, final Object pVisitedFlag)
	{
		//				if (pCurrrent == pGoal)
		//					return true;
		if (pObstacles.contains(pCurrent))
			return false;
		List lSuccList = pCurrent.getSuccList();
		BBlock lSuccBBlock;
		for (Iterator lIt = lSuccList.iterator(); lIt.hasNext();)
		{
			lSuccBBlock = (BBlock)lIt.next();
			if (lSuccBBlock == pGoal)
				return true;
			else if (lSuccBBlock.getWork() != pVisitedFlag)
			{
				lSuccBBlock.setWork(pVisitedFlag);
				if (search(lSuccBBlock, pGoal, pObstacles, pVisitedFlag))
					return true;
			}
		}
		return false;
	}
	
	//			List l = FlowUtil.bfSearch(pExitBBlock, pDominatedBBlock, false);
	
	
	
	
	protected Set callModLvalues(IR pCallNode, SubpFlow pCurrentSubpFlow)
	{
		return new HashSet(pCurrentSubpFlow.getFlowExpIdTable());
	}
}
