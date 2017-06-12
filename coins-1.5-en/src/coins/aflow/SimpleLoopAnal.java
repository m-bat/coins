/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * SimpleLoopAnal.java
 *
 * Created on January 6, 2004, 3:46 PM
 */

package coins.aflow;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Set;
import java.util.List;
import java.util.Iterator;

/**
 *
 * @author  hasegawa
 */
public class SimpleLoopAnal
{
    private SubpFlow fSubpFlow;
    /** Creates a new instance of SimpleLoopAnal */
    public SimpleLoopAnal(SubpFlow pSubpFlow)
    {
        fSubpFlow = pSubpFlow;
    }
    
    
    public Set process() // Dominator tree has to be already constructed.
    {
        BBlock lEntry = fSubpFlow.getEntryBBlock();
        LinkedList lPO = new LinkedList();
        postOrderWalk(lEntry, lPO);
        
        for (ListIterator lListIt = lPO.listIterator(lPO.size()); lListIt.hasPrevious();)
        {
            BBlock lBBlock = (BBlock)lListIt.previous();
            List lPreds = lBBlock.getPredList();
            for (Iterator lPredIt = lPreds.iterator(); lPredIt.hasNext();)
            {
                BBlock lPred = (BBlock)lPredIt.next();
                List lDoms = lPred.getDomForSubpFlow();
                if (lDoms.contains(lBBlock))
                {
                    findLoop(lBBlock, lPred);
                    break;
                }
            }
        }
        throw new RuntimeException();
    }
    
    private void findLoop(BBlock pHeader, BBlock pTail)
    {
        LinkedList lWorkList = new LinkedList();
        lWorkList.addAll(pHeader.getPredList());
        Set lLoop = new HashSet();
        
        while (!lWorkList.isEmpty())
        {
            BBlock lBBlock = (BBlock)lWorkList.removeFirst();
            List lPredList = lBBlock.getPredList();
            for (Iterator lIt = lPredList.iterator(); lIt.hasNext();)
            {
                BBlock lPred = (BBlock)lIt.next();
            }
        }
    }
    
    private static void postOrderWalk(BBlock pBBlock, LinkedList pList)
    {
        List lSuccs = pBBlock.getSuccList();
        
        for (Iterator lIt = lSuccs.iterator(); lIt.hasNext();)
        {
            BBlock lBBlock = (BBlock)lIt.next();
            if (!pList.contains(lBBlock))
                postOrderWalk(lBBlock, pList);
        }
        
        pList.addLast(pBBlock);
    }
        
}
