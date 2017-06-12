/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.flow; //##60

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import coins.sym.ExpId; //##60
import coins.sym.Sym;

/** ExpVectorImpl class
 *
 **/
public class ExpVectorImpl
  extends BitVectorImpl
  implements ExpVector
{
  //  public final FlowRoot flowRoot;
  //##60 FlowResults fResults;
  public final SubpFlow fSubpFlow;
  private Set fExps;

  protected ExpVectorImpl(SubpFlow pSubpFlow)
  {
    fSubpFlow = pSubpFlow;
    //##60 fResults = fSubpFlow.results();
    //##60 fBitLength = ((FAList) fResults.get("FlowExpIdTable", fSubpFlow)).size();
    fBitLength = fSubpFlow.getExpVectorBitCount(); //##60
    fLongWordLength = (fBitLength / 64) + 1;
    //##62 fVectorWord = new long[fLongWordLength];
    fVectorWord = new long[fLongWordLength+1]; //## REFINE //##62
  }

  public SubpFlow getSubpFlow()
  {
    return fSubpFlow;
  }

  public Set exps()
  {
    //##60 FlowExpId lFlowExpId;
    ExpId lExpId; //##60

    //		if (fExps != null)
    //			return fExps;
    fExps = new HashSet();

    for (ExpVectorIterator lExpVectIt = new ExpVectorIteratorImpl(fSubpFlow, this);
         lExpVectIt.hasNext(); )
      if ((lExpId = lExpVectIt.nextExpId()) != null) {
        fExps.add(lExpId);
      }
    return fExps;
  }

//##60 public boolean contains(FlowExpId pFlowExpId)
  public boolean contains(ExpId pExpId)
  { //##60
    return isSet(pExpId.getIndex());
  }

  /**
   * Returns an ExpVector whose bits that are set correspond to
   * the elements of the given Set pExps.
   */
  //##60 public static ExpVector forSet(Set pExps, SubpFlow pSubpFlow)
  public static ExpVector expVectorFromSet(Set pExps, SubpFlow pSubpFlow) //##60
  {
    ExpVector lExpVect = pSubpFlow.expVector();
    ExpId lExpId; //##60
    for (Iterator lIt = pExps.iterator(); lIt.hasNext(); ) {
      lExpId = (ExpId)lIt.next(); //##60
      lExpVect.setBit(lExpId.getIndex());
    }
    return lExpVect;
  }

  public String toStringConcise()
  {
    return super.toString();
  }

//##25 BEGIN
  public String toStringShort()
  {
    Sym lNextSym = null;
    int lNextIndex;
    ExpId lFlowExpId;
    StringBuffer lBuffer = new StringBuffer();
    lBuffer.append("["); //##25
    for (ExpVectorIterator lIterator = expVectorIterator();
         lIterator.hasNext(); ) {
      lFlowExpId = lIterator.nextExpId();
      if (lFlowExpId != null) {
        lBuffer.append(lFlowExpId.toStringShort() + ' '); //##25
      }
    }
    lBuffer.append("]"); //##25
    return lBuffer.toString();
  }

//##25 END

  public String toStringByName()
  {
    return exps().toString();
  }

  public String toStringDescriptive()
  {
    Sym lNextSym = null;
    int lNextIndex;
    ExpId lFlowExpId;
    SubpFlow lSubpFlow = getSubpFlow(); //##8
    StringBuffer lBuffer = new StringBuffer();
    lBuffer.append("["); //##25
    for (ExpVectorIterator lIterator = expVectorIterator();
         lIterator.hasNext(); ) {
      lFlowExpId = lIterator.nextExpId();

      if (lFlowExpId != null) {
        //##53 lBuffer.append(lFlowExpId.toString() + '\n'); //##8
        lBuffer.append('\n' + lFlowExpId.toString()); //##53
      }
    }
    //##53 lBuffer.append("]"); //##25
    lBuffer.append("]\n"); //##53
    return lBuffer.toString();
  }

  public ExpVectorIterator expVectorIterator()
  {
    return new ExpVectorIteratorImpl(fSubpFlow, this);
  }
}
