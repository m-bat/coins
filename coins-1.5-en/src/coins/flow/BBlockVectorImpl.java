/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.flow; //##60

import java.util.ArrayList; //##70
import java.util.List; //##70

/** BBlockVectorImpl class
 *
 **/
public class BBlockVectorImpl
  extends BitVectorImpl
  implements BBlockVector
{
  protected final SubpFlow fSubpFlow;

  public BBlockVectorImpl(SubpFlow pSubpFlow)
  {
    this(pSubpFlow, false);
  }

  private BBlockVectorImpl(SubpFlow pSubpFlow, boolean pAll)
  {
    fSubpFlow = pSubpFlow;
    fBitLength = pSubpFlow.getBBlockTable().size();
    fLongWordLength = (fBitLength / 64) + 1;
    fVectorWord = new long[fLongWordLength];
  }

  public SubpFlow getSubpFlow()
  {
    return fSubpFlow;
  }

//##70 BEGIN
  public List getBBlockList()
  {
    BBlock lBBlock;
    List lBBlockList = new ArrayList();
    for (BitVectorIterator lIt = this.bitVectorIterator(fSubpFlow);
         lIt.hasNext(); ) {
      int lNextIndex = lIt.nextIndex();
      if (lNextIndex > 0)
        lBBlockList.add(fSubpFlow.getBBlock(lNextIndex));
    }

    return lBBlockList;
  } // getBBlockList

//##70 END

} // BBlockVectorImpl
