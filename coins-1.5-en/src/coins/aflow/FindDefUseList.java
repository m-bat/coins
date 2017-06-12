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
import coins.ir.hir.HIR;
import coins.sym.FlowAnalSym;
import coins.sym.Type;
import coins.sym.Sym;


/**
 * Finds <code>DefUseList</code>s for <code>FlowAnalSym</code>s.
 */
public class FindDefUseList extends FlowAdapter
{
    private Set fDefinedSyms = new HashSet();
    private FlowAnalSym fFlowAnalSym = null;
    private long fFlowCount = 0;
    
    public final Flow flow;
    //        private AnalOptions fOpts;
    //        private boolean fIsCDefUseCheck = false;

    public FindDefUseList(FlowResults pResults)
    {
        super(pResults);
        flow = flowRoot.aflow;
    }
    
    /**
     * Finds <code>DefUseList</code>s for all the <code>FlowAnalSym</code>s in <code>pSubpFlow</code>.
     */
    public void find(SubpFlow pSubpFlow)
    {
        BBlock lBBlock;
        FlowAnalSym lFlowAnalSym;
        
        for (Iterator lSymIt = pSubpFlow.getSymIndexTable().iterator(); lSymIt.hasNext();)
        {
            lFlowAnalSym = (FlowAnalSym)lSymIt.next();
            fResults.put("DefUseList", lFlowAnalSym, pSubpFlow,
            flow.defUseList());
            fResults.put("UDList", lFlowAnalSym, pSubpFlow,
            flow.udList());
        }
        
        for (Iterator lIt = pSubpFlow.getBBlocks().iterator(); lIt.hasNext();)
        {
            lBBlock = (BBlock) lIt.next();
            find(lBBlock);
        }
        
        // Register unused variables to UD-chains with empty use nodes. Unnecessary?
        DefUseCell lDefUseCell;
        FlowAnalSym lDefined;
        
        for (Iterator lIt = fDefinedSyms.iterator(); lIt.hasNext();)
        {
            lDefined = (FlowAnalSym) lIt.next();
            
            Iterator lDefUseCellListIt = ((DefUseListImpl) fResults.getRaw("DefUseList",
            lDefined, pSubpFlow)).fDefUseCellList.iterator();
            
            for (; lDefUseCellListIt.hasNext();)
            {
                lDefUseCell = (DefUseCell) lDefUseCellListIt.next();
                
                if (lDefUseCell.getUseList().isEmpty())
                {
                    ((UDListImpl) fResults.getRaw("UDList", lDefined, pSubpFlow)).getOrAddUDChain(UDChain.UNUSED)
                    .addDefNode(lDefUseCell.getDefNode());
                }
            }
        }
    }
    
    public void find(FlowAnalSym pFlowAnalSym, SubpFlow pSubpFlow)
    {
        fFlowAnalSym = pFlowAnalSym;
        find(pSubpFlow);
    }
    
    private void find(BBlock pBBlock)
    {
        SubpFlow lSubpFlow = pBBlock.getSubpFlow();
        SetRefRepr lSetRefRepr;
        IR lUseNode;
        IR lDefNode;
        FlowAnalSym lDefSym, lFlowAnalSym;
        DefVector lDReach, lPReach;
        DefVectorIterator lDReachIt, lPReachIt;
        SetRefRepr lDReachSetRefRepr, lPReachSetRefRepr;
        DefUseList lDefUseList;
        UDList lUDList;
        int lPos;
        IR lCallNode;
        Set lFModFlowAnalSyms = new HashSet();
        ArrayList lCallNodeStack = new ArrayList();
        FAList lSymIndexTable = lSubpFlow.getSymIndexTable();
        ListValuedMap lSymToDDefNode = new ListValuedMap();
        ListValuedMap lSymToPDefNode = new ListValuedMap();
        Set lDDefSyms = new HashSet();
        
        lPReach	= pBBlock.getPReach();
        lPReachIt = lPReach.defVectorIterator();
        while (true)
        {
            lPReachSetRefRepr = lPReachIt.nextSetRefRepr();
            if (lPReachSetRefRepr == null)
                break;
            
            for (Iterator lDefSymIt = lPReachSetRefRepr.modSyms().iterator(); lDefSymIt.hasNext();)
            {
                lFlowAnalSym = (FlowAnalSym)lDefSymIt.next();
                if (!lSymToDDefNode.containsKey(lFlowAnalSym))
                    ((List)lSymToPDefNode.get(lFlowAnalSym)).add(lPReachSetRefRepr.getIR());
                else
                {
                    Set lDominatorBBlocks = new HashSet();
                    for (Iterator lDReachIterator = ((List)lSymToDDefNode.get(lFlowAnalSym)).iterator(); lDReachIterator.hasNext();)
                    {
                        lDominatorBBlocks.add(fResults.get("BBlockForNode", lDReachIterator.next()));
                    }
                    if (!postdominates(lDominatorBBlocks, lPReachSetRefRepr.getBBlock(), pBBlock))
                        ((List)lSymToPDefNode.get(lFlowAnalSym)).add(lPReachSetRefRepr.getIR());
                }
            }
            for (Iterator lCallNodeIt = lPReachSetRefRepr.callNodes().iterator(); lCallNodeIt.hasNext();)
            {
                lCallNode = (IR)lCallNodeIt.next();
                for (Iterator lCallModSymsIt = callModSyms(lCallNode, lSubpFlow).iterator(); lCallModSymsIt.hasNext();)
                {
                    FlowAnalSym lCallModSym = (FlowAnalSym)lCallModSymsIt.next();
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
        //			lDDefSyms.addAll(lSymToDDefNode.keySet());
        
        
        
        for (Iterator lIt = ((SetRefReprList) fResults.get(
        "BBlockSetRefReprs", pBBlock)).iterator(); lIt.hasNext();)
        {
            lSetRefRepr = (SetRefRepr) lIt.next();
            for (Iterator lUseNodeIt = lSetRefRepr.expIterator(); lUseNodeIt.hasNext();) {
                lUseNode = (IR) lUseNodeIt.next();
                fFlowCount = fFlowCount +1;
                //System.out.println("lFlowCount="+fFlowCount);
                //
                //
                //
                // Fukuda
                //
                HIR parentUseNode ;
                HIR TopNode ;
                TopNode = (HIR)lUseNode;
                int op;
                while(true) {
                    parentUseNode =(HIR)((HIR)TopNode).getParent();
                    if(parentUseNode == null )
                       break;
                    op= parentUseNode.getOperator();
                    if(op != HIR.OP_QUAL)
                        break;
                     TopNode=parentUseNode ;
                }
                if (TopNode != lUseNode) {
                    HIR c= FlowUtil.getQualVarNode(TopNode);
                       if (lUseNode != c) {
                            continue;
                        }
                }
                if (!lCallNodeStack.isEmpty() && !FlowUtil.isUnder((lCallNode = (IR)lCallNodeStack.get(lCallNodeStack.size() - 1)), lUseNode))
                {
                    handleCall(lCallNode, lSubpFlow, lDDefSyms, lSymToPDefNode);
                    lCallNodeStack.remove(lCallNodeStack.size() - 1);
                }
                if ((FlowUtil.flowAnalSym(lUseNode) != null) &&
                !FlowUtil.isDefSymNode(lUseNode) && !FlowUtil.notDereferenced(lUseNode))
                {
                    lFlowAnalSym = (FlowAnalSym) lUseNode.getSym();
                } else if (FlowUtil.isCall(lUseNode))
                {
                    lCallNodeStack.add(lUseNode);
                    continue;
                } else
                    continue;
                Type p = lFlowAnalSym.getSymType();
                int kind = p.getTypeKind();
                if( kind != Type.KIND_VECTOR &&  kind != Type.KIND_POINTER) {
                    lDefUseList = (DefUseList) fResults.getRaw("DefUseList",
                            lFlowAnalSym, lSubpFlow);
                 
                    lUDList = (UDList) fResults.getRaw("UDList", lFlowAnalSym, lSubpFlow);
                     int CallCount =0;
                     for (Iterator lDefNodeIt =((List)lSymToPDefNode.get(lFlowAnalSym)).iterator(); 
                           lDefNodeIt.hasNext();) {
                        lDefNode = (IR)lDefNodeIt.next();
                        if(lDefNode == null)
                            continue;
                        if (lDefNode.getOperator() == HIR.OP_CALL)  {
                            CallCount = CallCount +1;
                            if (CallCount > 2)
                                continue;
                        }
                        if (lDefNode.getOperator() != HIR.OP_CALL) {
                            ((DefUseListImpl)lDefUseList).getOrAddDefUseCell(lDefNode).addUseNode(lUseNode);
                        }
                         ((UDListImpl) lUDList).getOrAddUDChain(lUseNode).addDefNode(lDefNode);
                        //System.out.println("lFlowAnalSym="+lFlowAnalSym.toString());
                        //System.out.println("Def="+lDefNode.toString());
                        //System.out.println("Use="+lUseNode.toString());
                    }
                }
                
                if (!lDDefSyms.contains(lFlowAnalSym)) // Definite assignment check.
                {
                    lDefUseList = (DefUseList) fResults.getRaw("DefUseList",
                            lFlowAnalSym, lSubpFlow);
                    lUDList = (UDList) fResults.getRaw("UDList", lFlowAnalSym, lSubpFlow);
                    if (lFlowAnalSym.getSymKind() == Sym.KIND_PARAM)
                    {
                        ((DefUseListImpl) lDefUseList).getOrAddDefUseCell(DefUseCell.PARAM).addUseNode(lUseNode);
                        ((UDListImpl) lUDList).getOrAddUDChain(lUseNode).addDefNode(UDChain.PARAM);
                    } else
                    {
                         // Fukuda 
                         if (lDefUseList == null)
                         continue;
                        // Fukuda:
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
                handleCall(lCallNode, lSubpFlow, lDDefSyms, lSymToPDefNode);
                lCallNodeStack.remove(lCallNodeStack.size() - 1);
            }
            
            if (lSetRefRepr.sets())
            {
                lDefSym = lSetRefRepr.defSym();
                if (lDefSym != null)
                {
                    ((List)lSymToPDefNode.get(lDefSym)).clear();
                    lDDefSyms.add(lDefSym);
                    ((List)lSymToPDefNode.get(lDefSym)).add(lSetRefRepr.getIR());
                    if( ((HIR)lSetRefRepr.getIR()).getOperator() != HIR.OP_CALL) {
                     //System.out.println("Debug="+lDefSym.toString());
                    ((DefUseListImpl)fResults.getRaw("DefUseList", lDefSym, lSubpFlow)).getOrAddDefUseCell(lSetRefRepr.getIR());

                   } 
                    fDefinedSyms.add(lDefSym);
                    
                } else
                {
                    for (Iterator lModIt = (lSetRefRepr).modSyms().iterator();
                    lModIt.hasNext();)
                    {
                        lFlowAnalSym = (FlowAnalSym) lModIt.next();
                        //System.out.println("lFlowAnalSym(2)="+lFlowAnalSym.toString());
                        //System.out.println("Use(2)="+lSetRefRepr.getIR().toString());
                        ((List)lSymToPDefNode.get(lFlowAnalSym)).add(lSetRefRepr.getIR());
                        if( ((HIR)lSetRefRepr.getIR()).getOperator() != HIR.OP_CALL)
                            ((DefUseListImpl)fResults.getRaw("DefUseList", lFlowAnalSym, lSubpFlow)).getOrAddDefUseCell(lSetRefRepr.getIR());
                        
                        fDefinedSyms.add(lFlowAnalSym);
                        
                    }
                }
            }
        }
    }
    
    protected void handleCall(IR pCallNode, SubpFlow pSubpFlow, Set pDDefSyms, ListValuedMap pSymToPDefNode)
    {
        FlowAnalSym lFlowAnalSym;
        IR lDefNode;
        DefUseList lDefUseList;
        UDList lUDList;
      //
      //  for (Iterator lIt = pSubpFlow.getSymIndexTable().iterator(); lIt.hasNext();)
        for (Iterator lIt = pSymToPDefNode.keySet().iterator(); lIt.hasNext();)
        {
            lFlowAnalSym = (FlowAnalSym)lIt.next();
            //((List)pSymToPDefNode.get(lFlowAnalSym)).clear();
            pSymToPDefNode.addUnique(lFlowAnalSym, pCallNode);
           /* 
            lDefUseList = (DefUseList) fResults.getRaw("DefUseList",
            lFlowAnalSym, pSubpFlow);
            lUDList = (UDList) fResults.getRaw("UDList", lFlowAnalSym,
            pSubpFlow);
            for (Iterator lDefNodeIt = ((List)pSymToPDefNode.get(lFlowAnalSym)).iterator(); lDefNodeIt.hasNext();)
            {
                lDefNode = (IR)lDefNodeIt.next();
                ((DefUseListImpl)lDefUseList).getOrAddDefUseCell(lDefNode)
                .addUseNode(pCallNode);
                ((UDListImpl) lUDList).getOrAddUDChain(pCallNode)
                .addDefNode(lDefNode);
            }
           */ 
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
    
    
    
    
    protected Set callModSyms(IR pCallNode, SubpFlow pCurrentSubpFlow)
    {
        return new HashSet(pCurrentSubpFlow.getSymIndexTable());
    }
}
