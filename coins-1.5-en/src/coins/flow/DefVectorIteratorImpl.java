/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.flow;

import coins.FlowRoot;
import coins.ir.IR;

/** DefVectorIteratorImpl class  ()
 *
 **/
public class
  DefVectorIteratorImpl
  extends PointVectorIteratorImpl
  implements DefVectorIterator
{

  public
    DefVectorIteratorImpl(SubpFlow pSubpFlow, DefVector pDefVector)
  {
    super(pSubpFlow, (PointVector)pDefVector);
  }

  public IR
    nextDefNode()
  {
    int lNextIndex;
    lNextIndex = nextIndex();
    if (lNextIndex == 0)
      return null;
    else {
      //##60 return fSubpFlow.getIndexedNode(lNextNodeIndex);
      //##62 int lNodeIndex = ((SubpFlowImpl)fSubpFlow).flow.dataFlow().getNodeIndex(lNextIndex);
      int lNodeIndex = fSubpFlow.getDefPoint(lNextIndex).getIndex(); //##62

      return fSubpFlow.getIndexedNode(lNodeIndex);
    }
  } // nextDefNode

//##63 BEGIN

  public SetRefRepr nextSetRefRepr() {
      int lNextIndex = nextIndex();

      if (lNextIndex == 0) {
          return null;
      } else {
          //##63 return (SetRefRepr) ((DefVector) fVect).getSubpFlow().getSetRefReprs().get(lNextIndex);
          //##63 BEGIN
          IR lIR = ((DefVector) fBitVector).getSubpFlow().getDefPoint(lNextIndex);
          return (SetRefRepr) ((DefVector) fBitVector).getSubpFlow().getSetRefReprOfIR(lIR);
          //##63 END
      }
  }
//##63 END

} // DefVectorIteratorImpl class
