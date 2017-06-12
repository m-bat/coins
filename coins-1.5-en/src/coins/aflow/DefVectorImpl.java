/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import coins.aflow.util.FAList;


/** DefVectorImpl class:
 *
 **/
public class DefVectorImpl extends PointVectorImpl implements DefVector {
    DefVectorImpl(SubpFlow pSubpFlow) {
        fSubpFlow = pSubpFlow;
        fResults = fSubpFlow.results();
        fBitLength = ((FAList) fResults.get("SubpFlowSetReprs", fSubpFlow)).size();
        fLongWordLength = (fBitLength / 64) + 1;
        fVectorWord = new long[fLongWordLength];

        //                super(pSubpFlow);
    }

    private Set setRefReprs() {
        SetRefRepr lSetRefRepr;
        Set lSetRefReprs = new HashSet();

        for (DefVectorIterator lDefVectIt = defVectorIterator();
                lDefVectIt.hasNext();)
            if ((lSetRefRepr = lDefVectIt.nextSetRefRepr()) != null) {
                lSetRefReprs.add(lSetRefRepr);
            }

        return lSetRefReprs;
    }

    public boolean contains(SetRefRepr pSetRefRepr) {
        return setRefReprs().contains(pSetRefRepr);
    }

    public DefVectorIterator defVectorIterator() {
        return new DefVectorIteratorImpl(this);
    }

    /**
     * Creates a DefVector from a set of SetRefReprs.
     */
    public static DefVector forSet(Set pSetRefReprs, SubpFlow pSubpFlow) {
        DefVector lDefVect = pSubpFlow.defVector();

        for (Iterator lIt = pSetRefReprs.iterator(); lIt.hasNext();)
            lDefVect.setBit(((FAList) pSubpFlow.getSetRefReprs()).indexOf(
                    lIt.next()));

        //		((DefVectorImpl)lDefVect).fSetReprs = pSetReprs;
        return lDefVect;
    }

    /**
     * Prints all the SetRefReprs whose corresponding bit is set in this DefVector.
     */
    public String toStringDescriptive() {
        StringBuffer lBuffer = new StringBuffer();

        for (Iterator lIt = setRefReprs().iterator(); lIt.hasNext();) {
            lBuffer.append(lIt.next() + "\n");
        }

        return lBuffer.toString();
    }
	
	/** Returns the Set view of this DefVector (in terms of the set of SetRefReprs).
	 */
	public Set getSetRefReprs()
	{
		return setRefReprs();
	}
	
}
 // DefVectorImpl class
