/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow;

import coins.ir.IR;
import coins.ir.hir.HIR;
import coins.ir.hir.HIR_Impl;
import coins.ir.hir.LabeledStmt;

/**
 * Unreliable
 * @author  hasegawa
 */
class BBlockHirNodeIteratorImpl implements BBlockNodeIterator
{	
	private HIR
	fNext = null; // Node that would be returned by the next call of next().
	
	final static HIR EOB = new HIR_Impl();
	
        BBlockHirNodeIteratorImpl(BBlockHir pBBlock)
	{
		fNext = (LabeledStmt)pBBlock.getIrLink();
	}

	public boolean hasNext()
	{
		return fNext != EOB && fNext != null;
	}
	
	public IR next()
	{
		if (fNext == null)
			return null;
		HIR lCurrent = fNext;
		HIR lHIR = fNext;
		HIR lHIRNext;
		int lOpCode = 0;
		
		lHIRNext = tryNext(lHIR);
		if (lHIRNext == null)
			lHIRNext = tryNeitherDescendantsNorAncestors(lHIR);
		if (lHIRNext instanceof LabeledStmt)
			lHIRNext = EOB;
		lHIR = lHIRNext;
		fNext = lHIR;
		return lCurrent;
	}

	public IR getNextExecutableNode()
	{
                HIR lHIR;
		do
                        lHIR = (HIR)next();
                while (!Flow.isExecutable(lHIR) && lHIR != null && lHIR != EOB);
		if (lHIR == EOB)
			lHIR = null;
		return lHIR;
	}
	
	private static HIR tryNext(HIR pHIR)
	{
		HIR lNext;
		int i = 1;
		for (i = 1; i <= pHIR.getChildCount(); i++)
		{
			lNext = (HIR)pHIR.getChild(i);
			if (lNext != null)
				return lNext;
		}
		return tryNeitherDescendantsNorAncestors(pHIR);		
	}
	
	private static HIR tryNeitherDescendantsNorAncestors(HIR pHIR)
	{
		HIR lNext;
		int lChildNumber;
		HIR lParent;
		int lChildCount;
		
		if ((lNext = pHIR.getNextStmt()) != null)
			return lNext;
		lChildNumber = pHIR.getChildNumber();
		lParent = (HIR)pHIR.getParent();
		if (lParent == null)
			return EOB; // End of subprogram reached.
		lChildCount = lParent.getChildCount();
		while (0 < lChildNumber && lChildNumber < lChildCount)
			if ((lNext = (HIR)lParent.getChild(++lChildNumber)) != null)
				return lNext;			
		return tryNeitherDescendantsNorAncestors(lParent);
	}
		
}


