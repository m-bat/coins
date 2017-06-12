/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.flow;

import java.util.Iterator;

import coins.ir.IR;
import coins.ir.hir.HIR;
//##60 import coins.sym.AReg;
import coins.sym.ExpId;
import coins.sym.FlowAnalSym;
import coins.sym.Sym;

public class //## Addition by Tan Watanabe
  ShowDataFlowByName
  extends ShowDataFlow
{

  protected final DataFlow dataFlow;

  public
    ShowDataFlowByName(DataFlow pDataFlow)
  {
    super(pDataFlow);
    dataFlow = pDataFlow;
  }

  /**
   * showPointVectorByName
   **/
  public void
    showPointVectorByName(PointVector pPoint)
  {
    IR lNextPoint;
    SubpFlow lSubpFlow = flowRoot.fSubpFlow;
    for (PointVectorIterator lIterator = lSubpFlow.pointVectorIterator(pPoint);
         lIterator.hasNext(); ) {
      //## lNextPoint = lIterator.nextPoint();
      //##62 lNextPoint = (IR)(dataFlow.getNode(dataFlow.getNodeIndex(lIterator.nextIndex())));
      lNextPoint = lSubpFlow.getDefPoint(lIterator.nextIndex()); //##62
      if (lNextPoint != null) {
        ioRoot.printOut.print(" " + ((HIR)lNextPoint).toStringShort());
      }
    }
    ioRoot.printOut.print("\n");
  } // showDefVectorByName

  /**
   * showExpVectorByName
   **/
  public void
    showExpVectorByName(ExpVector pexp)
  {
    //##68 BEGIN
    if (pexp instanceof FlowAnalSymVector) {
      ioRoot.printOut.print(
        ((FlowAnalSymVector)pexp).toStringDescriptive() + "\n");
      return;
    }
    //##68 END
    // Sym lNextSym = null;
    String lExpString; //##73
    int lNextIndex;
    FlowAnalSym lFlowAnalSym;
    SubpFlow lSubpFlow = flowRoot.fSubpFlow;
    for (ExpVectorIterator lIterator = lSubpFlow.expVectorIterator(pexp);
         lIterator.hasNext(); ) {
      //##73 lNextSym = null;
      lExpString = null; //##73
      lNextIndex = lIterator.nextIndex();
      if (lNextIndex > 0) {
        // lExpId = dataFlow.getExpId(dataFlow.expReverseLookup(lNextIndex));
        lFlowAnalSym = dataFlow.getFlowAnalSym(dataFlow.expReverseLookup(
          lNextIndex));
        if (lFlowAnalSym != null) {
          ioRoot.printOut.print(" " + ((Sym)lFlowAnalSym).getName());
          if (lFlowAnalSym instanceof ExpId) {
            //##73 lNextSym = ((ExpId)lFlowAnalSym).getLinkedSym();
            //##73 BEGIN
            if (((ExpId)lFlowAnalSym).getLinkedSym() != null)
              lExpString = ((ExpId)lFlowAnalSym).getLinkedSym().getName();
            if ((lExpString == null)&&(ioRoot.dbgFlow.getLevel() >= 7))
              lExpString = ((HIR)((ExpId)lFlowAnalSym).getLinkedNode()).toStringWithChildren();
          }
          if (lExpString != null)
            ioRoot.printOut.print("(" + lExpString + ")");
          //##73 END
          //##73 if (lNextSym != null)
          //##73   ioRoot.printOut.print("(" + lNextSym.getName() + ")");
        }
      }
    }
    ioRoot.printOut.print("\n");
  } // showExpVectorByName

  public void
    showNodeSet(java.util.Set pSet)
  {
    IR lNextPoint;
    for (Iterator lIterator = pSet.iterator();
         lIterator.hasNext(); ) {
      lNextPoint = (IR)lIterator.next();
      if (lNextPoint != null) {
        ioRoot.printOut.print(" " + ((HIR)lNextPoint).toStringShort());
      }
    }
    ioRoot.printOut.print("\n");
  } // showNodeSet

} // showDataFlowByName
