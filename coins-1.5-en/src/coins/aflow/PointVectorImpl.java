/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow;

import coins.aflow.util.BitVectorImpl;
import coins.aflow.util.FAList;
import coins.aflow.util.FlowError;


/** PointVectorImpl class:
 *
**/
public class PointVectorImpl extends BitVectorImpl implements PointVector {
    public FlowResults fResults;
    SubpFlow fSubpFlow;

    /** PointVectorImpl:
     *  Constructor for creating PointVector.
     *  Before invoking PointVectorImpl, PointVectorImpl.setVectorLength should be
     *  called with the same FlowRoot parameter.
    **/

    //##8
    protected PointVectorImpl() {
    }

    protected PointVectorImpl(SubpFlow pSubpFlow) {
        if (true) {
            throw new FlowError();
        }

        fSubpFlow = pSubpFlow;
        fResults = fSubpFlow.results();
        fBitLength = ((FAList) fResults.get("SubpFlowSetReprs", fSubpFlow)).size();
        fLongWordLength = (fBitLength / 64) + 1;
        fVectorWord = new long[fLongWordLength];
    }

//    public SubpFlow subpFlow() {
//        return fSubpFlow;
//    }
	
	/** Returns a SubpFlow object this PointVector is associated with.
	 */
	public SubpFlow getSubpFlow()
	{
		return fSubpFlow;
	}
	
}
 // PointVectorImpl class
