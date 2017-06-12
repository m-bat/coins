/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
//##60 package coins.aflow;
package coins.flow; //##60

//##60 import coins.aflow.util.BitVector;
//##60 import coins.aflow.util.BitVectorIteratorImpl;
//##60 import coins.aflow.util.FAList;
import coins.sym.ExpId; //##60
import coins.sym.FlowAnalSym; //##60


/** ExpVectorIteratorImpl class  (##6)
 *
 **/
public class ExpVectorIteratorImpl extends BitVectorIteratorImpl
  implements ExpVectorIterator {
  //##60 private final FlowResults fResults;
  //##60 protected final ExpVector fVect;

  //##60 protected ExpVectorIteratorImpl(ExpVector pExpVector) //##8
  public ExpVectorIteratorImpl(SubpFlow pSubpFlow, ExpVector pExpVector) //##60
   {
  super(pSubpFlow, (BitVector) pExpVector); //##8
  //##60 fResults = ((ExpVectorImpl) pExpVector).fResults;
  //##60 fVect = pExpVector;
  }

  //##60 public FlowExpId nextFlowExpId()
  public ExpId nextExpId() //##60
  {
    int lNextExpIndex = 0;
    //##60 FlowExpId lIndexedSym; //##11
    ExpId lIndexedSym; //##11
    FlowAnalSym lSym; //##60
    while (hasNext()) {
      lNextExpIndex = nextIndex();
      lSym = fSubpFlow.getIndexedSym(lNextExpIndex);
      if (lSym instanceof ExpId)
        return (ExpId)lSym;
    }
    return null;
    //##60 END
  } // nextExpId
} // ExpVectorIteratorImpl class
