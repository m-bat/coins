/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * FlowAnalSymVectorIteratorImpl.java
 *
 * Created on September 25, 2002, 9:37 AM
 */
//##60 package coins.aflow;
package coins.flow; //##60

//##60 import coins.aflow.util.BitVectorIteratorImpl;
//##60 import coins.aflow.util.FAList;
import coins.sym.FlowAnalSym;

/**
 *
 * @author  hasegawa
 */
public class FlowAnalSymVectorIteratorImpl
  //##60 extends BitVectorIteratorImpl
  extends ExpVectorIteratorImpl //##60
  implements coins.flow.FlowAnalSymVectorIterator
{
  //##60 FlowResults fResults;
  //##60 protected coins.flow.FlowAnalSymVector fVect;

  /** Creates a new instance of FlowAnalSymVectorIteratorImpl */
  public FlowAnalSymVectorIteratorImpl(FlowAnalSymVector pFlowAnalSymVector)
  {
    super(pFlowAnalSymVector.getSubpFlow(), pFlowAnalSymVector);
    //##60 fResults = ((FlowAnalSymVectorImpl) pFlowAnalSymVector).fResults;
    //##60 fVect = pFlowAnalSymVector;
  }

  public FlowAnalSym nextFlowAnalSym()
  {
    int lNextExpIndex = 0;
    FlowAnalSym lIndexedSym; //##11
    lNextExpIndex = nextIndex();

    if (lNextExpIndex == 0) {
      return null;
    }

    //##60 lIndexedSym = (FlowAnalSym) ((FAList) fResults.get("SymIndexTable",
    //##60   fVect.getSubpFlow())).get(lNextExpIndex);
    //##60 lIndexedSym = flowRoot.fSubpFlow.getIndexedSym(lNextExpIndex);
    lIndexedSym = fSubpFlow.getIndexedSym(lNextExpIndex); //##60
    return lIndexedSym;
  }
  // nextExpId
}
