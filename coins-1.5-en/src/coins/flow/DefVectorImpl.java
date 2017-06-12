/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
//##60 package coins.aflow;
package coins.flow; //##60

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

//##63 import coins.aflow.util.FAList;
import coins.ir.IR;
import coins.ir.hir.HIR;

/** DefVectorImpl class
 *
 **/
public class DefVectorImpl
  extends PointVectorImpl
  implements DefVector
{
  DefVectorImpl(SubpFlow pSubpFlow)
  {
    super(pSubpFlow);
    //##60 fSubpFlow = pSubpFlow;
    //##60 fResults = fSubpFlow.results();
    //##60 fBitLength = ((FAList) fResults.get("SubpFlowSetReprs", fSubpFlow)).size();
    fBitLength = pSubpFlow.getDefVectorBitCount(); //##60
    fLongWordLength = (fBitLength / 64) + 1;
    fVectorWord = new long[fLongWordLength];

    //  super(pSubpFlow);
  }

  public DefVectorIterator defVectorIterator()
  {
    return new DefVectorIteratorImpl(fSubpFlow, this);
  }

  /**
     * Prints all the SetRefReprs whose corresponding bit is set in this DefVector.
   */
  public String toStringDescriptive()
  {
    StringBuffer lBuffer = new StringBuffer();
    for (int lIndex = 0; lIndex < fSubpFlow.getDefCount(); lIndex++) {
      if (getBit(lIndex) != 0) {
        IR lIr = fSubpFlow.getDefPoint(lIndex);
        if (lIr != null)
          lBuffer.append(((HIR)lIr).toStringShort());
      }
    }
    return lBuffer.toString();
  }

}
