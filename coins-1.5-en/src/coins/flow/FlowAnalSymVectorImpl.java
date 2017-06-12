/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * SymVectorImpl.java
 *
 * Created on September 6, 2002, 2:11 PM
 */
//##60 package coins.aflow;
package coins.flow; //##60

import java.util.ArrayList; //##78
import java.util.HashSet;
import java.util.Iterator;
import java.util.List; //##78
import java.util.Set;

//##60 import coins.aflow.util.BitVectorImpl;
import coins.flow.BitVectorImpl;
//##60 import coins.aflow.util.FAList;
//##60 import coins.flow.FAList;
import coins.sym.FlowAnalSym;
import coins.sym.ExpId; //##60
import coins.sym.Sym; //##68

/**
 *
 * @author  hasegawa
 */
public class FlowAnalSymVectorImpl
  //##60 extends BitVectorImpl
  extends ExpVectorImpl //##60
  implements FlowAnalSymVector
{
  //##60 private final SubpFlow fSubpFlow;
  //## public final SubpFlow fSubpFlow; //##60
  //##60 final FlowResults fResults;

  /** Creates a new instance of SymVectorImpl */
  public FlowAnalSymVectorImpl(SubpFlow pSubpFlow)
  {
    //##60 fSubpFlow = pSubpFlow;
    super(pSubpFlow); //##60
    //##60 fResults = fSubpFlow.results();
    //##60 fBitLength = ((FAList) fResults.get("SymIndexTable", fSubpFlow)).size();
    fBitLength = pSubpFlow.getSymExpCount() + 2; //REFINE //##60
    fLongWordLength = (fBitLength / 64) + 1;
    fVectorWord = new long[fLongWordLength];
  }

  public SubpFlow getSubpFlow()
  {
    return fSubpFlow;
  }

  public Set flowAnalSyms()
  {
    FlowAnalSym lFlowAnalSym;
    Set lFlowAnalSyms = new HashSet();

    for (FlowAnalSymVectorIterator lFASVectIt = flowAnalSymVectorIterator();
         lFASVectIt.hasNext(); )
      if ((lFlowAnalSym = lFASVectIt.nextFlowAnalSym()) != null) {
        lFlowAnalSyms.add(lFlowAnalSym);
      }

    return lFlowAnalSyms;
  }

  public boolean contains(FlowAnalSym pFlowAnalSym)
  {
    //##25 Inefficient to compute flowAnalSyms() each time.
    //##25 Should save and reuse it.
    return flowAnalSyms().contains(pFlowAnalSym);
  }

  public boolean remove(FlowAnalSym pFlowAnalSym)
  {
    int lIndex;

    //##62 if (isSet(lIndex = fSubpFlow.getSymIndexTable().indexOf(pFlowAnalSym)))
    lIndex = pFlowAnalSym.getIndex(); //##62
    if (isSet(lIndex))  {//##62
      resetBit(lIndex);
      return true;
    }

    return false;
  }

  /**
   * Creates and returns a FlowAnalSymVector view of the given Set argument.
   */
  //##60 public static FlowAnalSymVector forSet(Set pFlowAnalSyms, SubpFlow pSubpFlow)
  public static FlowAnalSymVector
    flowAnalSymVectorFromSet(Set pFlowAnalSyms, SubpFlow pSubpFlow) //##60
  {
    FlowAnalSymVector lFlowAnalSymVect = pSubpFlow.flowAnalSymVector();

    for (Iterator lIt = pFlowAnalSyms.iterator(); lIt.hasNext(); ) {
      //##60 Object lNext = lIt.next();
      FlowAnalSym lNext = (FlowAnalSym)lIt.next(); //##60
      //##60 lFlowAnalSymVect.setBit(((FAList) pSubpFlow.results().get("SymIndexTable",
      //##60   pSubpFlow)).indexOf(lNext));
      lFlowAnalSymVect.setBit(lNext.getIndex()); //##60
    }

    return lFlowAnalSymVect;
  }

  public FlowAnalSymVectorIterator flowAnalSymVectorIterator()
  {
    return new FlowAnalSymVectorIteratorImpl(this);
  }

  public boolean addAll(Set pFlowAnalSyms)
  {
    //##60 FlowAnalSymVector lVect = forSet(pFlowAnalSyms, fSubpFlow);
    FlowAnalSymVector lVect = flowAnalSymVectorFromSet(pFlowAnalSyms, fSubpFlow); //##60
    FlowAnalSymVector lVectResult = fSubpFlow.flowAnalSymVector();
    boolean lReturnVal;

    vectorOr(lVect, lVectResult);
    lReturnVal = lVectResult.vectorEqual(this);
    lVectResult.vectorCopy(this);

    return lReturnVal;
  }

  public String toStringDescriptive()
  {
    StringBuffer lBuff = new StringBuffer();
    FlowAnalSym lFlowAnalSym;
    Sym lSym;
    List lFlowAnalSymList = new ArrayList(fSubpFlow.getSymExpCount()); //##78

    for (FlowAnalSymVectorIterator lIt = flowAnalSymVectorIterator();
         lIt.hasNext(); ) {
      lFlowAnalSym = lIt.nextFlowAnalSym();
      if (lFlowAnalSym != null) {
        lFlowAnalSymList.add(lFlowAnalSym); //##78
        /* //##78
        lBuff.append(lFlowAnalSym.getName());
        //##68 BEGIN
        if (lFlowAnalSym instanceof ExpId) {
          lSym = ((ExpId)lFlowAnalSym).getLinkedSym();
          if (lSym != null)
            lBuff.append("(" + lSym.getName() + ")");
        }
        //##68
        lBuff.append(" ");
        */ //##78
      }
    }
    //##78 BEGIN
    List lFlowAnalSymsSorted = fSubpFlow.sortExpIdCollection(lFlowAnalSymList);
    for (Iterator lIt2 = lFlowAnalSymsSorted.iterator();
      lIt2.hasNext(); ) {
      lFlowAnalSym = (FlowAnalSym)lIt2.next();
      if (lFlowAnalSym != null) {
        lBuff.append(lFlowAnalSym.getName());
        if (lFlowAnalSym instanceof ExpId) {
          lSym = ((ExpId)lFlowAnalSym).getLinkedSym();
          if (lSym != null)
            lBuff.append("(" + lSym.getName() + ")");
        }
        lBuff.append(" ");
      }
    }
    //##78 END
    return lBuff.toString();
  } // toStringDescriptive

//##25 BEGIN
  public String toStringShort()
  {
    StringBuffer lBuff = new StringBuffer();
    FlowAnalSym lFlowAnalSym;
    List lFlowAnalSymList = new ArrayList(fSubpFlow.getSymExpCount()); //##78
    for (FlowAnalSymVectorIterator lIt = flowAnalSymVectorIterator();
         lIt.hasNext(); ) {
      if ((lFlowAnalSym = lIt.nextFlowAnalSym()) != null) {
        //##78 lBuff.append(lFlowAnalSym.toStringShort() + " ");
        lFlowAnalSymList.add(lFlowAnalSym); //##78
      }
    }
    //##78 BEGIN
    List lFlowAnalSymsSorted = fSubpFlow.sortExpIdCollection(lFlowAnalSymList);
    for (Iterator lIt2 = lFlowAnalSymsSorted.iterator();
         lIt2.hasNext(); ) {
      lFlowAnalSym = (FlowAnalSym)lIt2.next();
      if (lFlowAnalSym != null) {
        lBuff.append(lFlowAnalSym.toStringShort() + " ");
      }
    }
    //##78 END
    return lBuff.toString();
 } // toStringShort

  public ExpVector
    flowAnalSymToExpVector()
  {
    ExpVector lExpVector = fSubpFlow.expVector();
    for (FlowAnalSymVectorIterator lIterator = flowAnalSymVectorIterator();
         lIterator.hasNext(); ) {
      FlowAnalSym lSym = lIterator.nextFlowAnalSym();
      //##60 if (lSym instanceof FlowExpId)
      if (lSym instanceof ExpId) { //##60
        //##60 lExpVector.setBit(((FlowExpId)lSym).getIndex());
        lExpVector.setBit(((ExpId)lSym).getIndex()); //##60
      }
    }
    if (((SubpFlowImpl)fSubpFlow).fDbgLevel > 0)
      ((SubpFlowImpl)fSubpFlow).flow.dbg(4, " flowAnalSymToExpVector",
      lExpVector.toStringDescriptive());
    return lExpVector;
  }
//##25 END

}
