/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow;

import coins.aflow.util.BitVector;
import coins.aflow.util.BitVectorIteratorImpl;
import coins.aflow.util.FAList;


/** ExpVectorIteratorImpl class:  (##6)
 *
 **/
public class ExpVectorIteratorImpl extends BitVectorIteratorImpl
    implements ExpVectorIterator {
    private final FlowResults fResults;
    protected final ExpVector fVect;

    protected ExpVectorIteratorImpl(ExpVector pExpVector) //##8
     {
        super((BitVector) pExpVector); //##8
        fResults = ((ExpVectorImpl) pExpVector).fResults;
        fVect = pExpVector;
    }

    /*       public IR
           nextExpNode()
           {
                   int lNextExpIndex = 0;
    //                FlowAnalSym lIndexedSym; //##11
                   FlowExpId lIndexedSym; //##11
                   lNextExpIndex = nextIndex();
                   if (lNextExpIndex == 0)
                           return null;
                   lIndexedSym = (FlowExpId)((FAList)fResults.get("FlowExpIdTable", fVect.getSubpFlow())).get(lNextExpIndex);
                   if ((lIndexedSym != null)&&
                   (lIndexedSym instanceof FlowExpId))
                           return ((FlowExpId)lIndexedSym).getTree();
                   else
                   {
                           fResults.flowRoot.ioRoot.msgRecovered.put(5021, //##8
                           "Unexpected result.");
                           return null;
                   }
           } // nextExpNode
      */
    public FlowExpId nextFlowExpId() {
        int lNextExpIndex = 0;
        FlowExpId lIndexedSym; //##11
        lNextExpIndex = nextIndex();

        if (lNextExpIndex == 0) {
            return null;
        }

        lIndexedSym = (FlowExpId) ((FAList) fVect.getSubpFlow()
                                                 .getFlowExpIdTable()).get(lNextExpIndex);

        return lIndexedSym;
    }
     // nextExpId
}
 // ExpVectorIteratorImpl class
