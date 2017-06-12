/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * SymVectorImpl.java
 *
 * Created on September 6, 2002, 2:11 PM
 */
package coins.aflow;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import coins.aflow.util.BitVectorImpl;
import coins.aflow.util.FAList;
import coins.sym.FlowAnalSym;


/**
 *
 * @author  hasegawa
 */
public class FlowAnalSymVectorImpl extends BitVectorImpl
    implements FlowAnalSymVector {
    private final SubpFlow fSubpFlow;
    final FlowResults fResults;

    /** Creates a new instance of SymVectorImpl */
    public FlowAnalSymVectorImpl(SubpFlow pSubpFlow) {
        fSubpFlow = pSubpFlow;
        fResults = fSubpFlow.results();
        fBitLength = ((FAList) fResults.get("SymIndexTable", fSubpFlow)).size();
        fLongWordLength = (fBitLength / 64) + 1;
        fVectorWord = new long[fLongWordLength];
    }

    public SubpFlow getSubpFlow() {
        return fSubpFlow;
    }

    public Set flowAnalSyms() {
        FlowAnalSym lFlowAnalSym;
        Set lFlowAnalSyms = new HashSet();

        for (FlowAnalSymVectorIterator lFASVectIt = flowAnalSymVectorIterator();
                lFASVectIt.hasNext();)
            if ((lFlowAnalSym = lFASVectIt.nextFlowAnalSym()) != null) {
                lFlowAnalSyms.add(lFlowAnalSym);
            }

        return lFlowAnalSyms;
    }

    public boolean contains(FlowAnalSym pFlowAnalSym) {
      //##25 Inefficient to compute flowAnalSyms() each time.
      //##25 Should save and reuse it.
        return flowAnalSyms().contains(pFlowAnalSym);
    }

    public boolean remove(FlowAnalSym pFlowAnalSym) {
        int lIndex;

        if (isSet(lIndex = fSubpFlow.getSymIndexTable().indexOf(pFlowAnalSym))) {
            resetBit(lIndex);

            return true;
        }

        return false;
    }

    /**
     * Creates and returns a FlowAnalSymVector view of the given Set argument.
     */
    public static FlowAnalSymVector forSet(Set pFlowAnalSyms, SubpFlow pSubpFlow) {
        FlowAnalSymVector lFlowAnalSymVect = pSubpFlow.flowAnalSymVector();

        for (Iterator lIt = pFlowAnalSyms.iterator(); lIt.hasNext();) {
            Object lNext = lIt.next();
            lFlowAnalSymVect.setBit(((FAList) pSubpFlow.results().get("SymIndexTable",
                    pSubpFlow)).indexOf(lNext));
        }

        return lFlowAnalSymVect;
    }

    public FlowAnalSymVectorIterator flowAnalSymVectorIterator() {
        return new FlowAnalSymVectorIteratorImpl(this);
    }

    public boolean addAll(Set pFlowAnalSyms) {
        FlowAnalSymVector lVect = forSet(pFlowAnalSyms, fSubpFlow);
        FlowAnalSymVector lVectResult = fSubpFlow.flowAnalSymVector();
        boolean lReturnVal;

        vectorOr(lVect, lVectResult);
        lReturnVal = lVectResult.vectorEqual(this);
        lVectResult.vectorCopy(this);

        return lReturnVal;
    }

    public String toStringDescriptive() {
        StringBuffer lBuff = new StringBuffer();
        FlowAnalSym lFlowAnalSym;

        for (FlowAnalSymVectorIterator lIt = flowAnalSymVectorIterator();
                lIt.hasNext();)
            if ((lFlowAnalSym = lIt.nextFlowAnalSym()) != null) {
                lBuff.append(lFlowAnalSym + "\n");
            }

        return lBuff.toString();
    }
//##25 BEGIN
public String toStringShort() {
  StringBuffer lBuff = new StringBuffer();
  FlowAnalSym lFlowAnalSym;
  for (FlowAnalSymVectorIterator lIt = flowAnalSymVectorIterator();
                lIt.hasNext();)
    if ((lFlowAnalSym = lIt.nextFlowAnalSym()) != null) {
      lBuff.append(lFlowAnalSym.toStringShort()+" ");
  }
 return lBuff.toString();
}

public ExpVector
flowAnalSymToExpVector()
{
  ExpVector lExpVector = fSubpFlow.expVector();
  for (FlowAnalSymVectorIterator lIterator = flowAnalSymVectorIterator();
       lIterator.hasNext(); ) {
    FlowAnalSym lSym = lIterator.nextFlowAnalSym();
    if (lSym instanceof FlowExpId) {
      lExpVector.setBit(((FlowExpId)lSym).getIndex());
    }
  }
  ((SubpFlowImpl)fSubpFlow).flow.dbg(4, " flowAnalSymToExpVector" ,
                                     lExpVector.toStringDescriptive());
  return lExpVector;
}
//##25 END

}
