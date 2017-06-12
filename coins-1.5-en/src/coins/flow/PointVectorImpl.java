/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
//##60 package coins.aflow;
package coins.flow; //##60

//##60 import coins.aflow.util.BitVectorImpl;
//##60 import coins.aflow.util.FAList;
//##60 import coins.aflow.util.FlowError;


/** PointVectorImpl class
 *
**/
public class PointVectorImpl extends BitVectorImpl implements PointVector {
   //##60  public FlowResults fResults;
   public final SubpFlow fSubpFlow;

  /** PointVectorImpl
   *  Constructor for creating PointVector.
   *  Before invoking PointVectorImpl, PointVectorImpl.setVectorLength should be
   *  called with the same FlowRoot parameter.
  **/

  //##8
  //##60 protected PointVectorImpl() {
  //##60 }

  protected PointVectorImpl(SubpFlow pSubpFlow) {
    /* //##60
  if (true) {
    throw new FlowError();
  }
    */ //##60

  fSubpFlow = pSubpFlow;
  //##60 fResults = fSubpFlow.results();
  //##60 fBitLength = ((FAList) fResults.get("SubpFlowSetReprs", fSubpFlow)).size();
  fBitLength = fSubpFlow.getPointVectorBitCount(); //##60

  fLongWordLength = (fBitLength / 64) + 1;
  //##62 fVectorWord = new long[fLongWordLength];
  fVectorWord = new long[fLongWordLength+1]; //## REFINE //##62
  }

//  public SubpFlow subpFlow() {
//  return fSubpFlow;
//  }

  /** Returns a SubpFlow object this PointVector is associated with.
   */
  public SubpFlow getSubpFlow()
  {
  return fSubpFlow;
  }

}
 // PointVectorImpl class
