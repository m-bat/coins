/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import coins.aflow.util.BitVectorImpl;
import coins.aflow.util.FAList;
import coins.sym.Sym;


/** ExpVectorImpl class:
 *
 **/
public class ExpVectorImpl extends BitVectorImpl implements ExpVector {
    //    public final FlowRoot flowRoot;
    FlowResults fResults;
    private final SubpFlow fSubpFlow;
    private Set fExps;

    protected ExpVectorImpl(SubpFlow pSubpFlow) {
        fSubpFlow = pSubpFlow;
        fResults = fSubpFlow.results();
        fBitLength = ((FAList) fResults.get("FlowExpIdTable", fSubpFlow)).size();
        fLongWordLength = (fBitLength / 64) + 1;
        fVectorWord = new long[fLongWordLength];
    }

    public SubpFlow getSubpFlow() {
        return fSubpFlow;
    }

    public Set exps() {
        FlowExpId lFlowExpId;

        //		if (fExps != null)
        //			return fExps;
        fExps = new HashSet();

        for (ExpVectorIterator lExpVectIt = new ExpVectorIteratorImpl(this);
                lExpVectIt.hasNext();)
            if ((lFlowExpId = lExpVectIt.nextFlowExpId()) != null) {
                fExps.add(lFlowExpId);
            }

        return fExps;
    }

    public boolean contains(FlowExpId pFlowExpId) {
        return isSet(pFlowExpId.getIndex());
    }

    /**
     * Returns an <code>ExpVector</code> whose bits that are set correspond to the elements of the given <code>Set</code> <code>pExps</code>.
     */
    public static ExpVector forSet(Set pExps, SubpFlow pSubpFlow) {
        ExpVector lExpVect = pSubpFlow.expVector();
        FlowExpId lFlowExpId;

        for (Iterator lIt = pExps.iterator(); lIt.hasNext();) {
            lFlowExpId = (FlowExpId) lIt.next();

            //                        System.out.println("Index = " + lFlowExpId.getIndex());
            lExpVect.setBit(lFlowExpId.getIndex());
        }

        return lExpVect;
    }

    public String toStringConcise() {
        return super.toString();
    }

//##25 BEGIN
    public String toStringShort() {
      Sym lNextSym = null;
      int lNextIndex;
      FlowExpId lFlowExpId;
      StringBuffer lBuffer = new StringBuffer();
      lBuffer.append("["); //##25
      for (ExpVectorIterator lIterator = expVectorIterator();
              lIterator.hasNext();) {
          lFlowExpId = lIterator.nextFlowExpId();
          if (lFlowExpId != null) {
              lBuffer.append(lFlowExpId.toStringShort() + ' '); //##25
          }
      }
      lBuffer.append("]"); //##25
      return lBuffer.toString();
    }
//##25 END

    public String toStringByName() {
        return exps().toString();
    }

    public String toStringDescriptive() {
        Sym lNextSym = null;
        int lNextIndex;
        FlowExpId lFlowExpId;
        SubpFlow lSubpFlow = getSubpFlow(); //##8
        StringBuffer lBuffer = new StringBuffer();
        lBuffer.append("["); //##25
        for (ExpVectorIterator lIterator = expVectorIterator();
                lIterator.hasNext();) {
            lFlowExpId = lIterator.nextFlowExpId();

            if (lFlowExpId != null) {
                //##53 lBuffer.append(lFlowExpId.toString() + '\n'); //##8
                lBuffer.append('\n' + lFlowExpId.toString() ); //##53
            }
        }
        //##53 lBuffer.append("]"); //##25
        lBuffer.append("]\n"); //##53
        return lBuffer.toString();
    }

    public ExpVectorIterator expVectorIterator() {
        return new ExpVectorIteratorImpl(this);
    }
}
 // ExpVectorImpl class
