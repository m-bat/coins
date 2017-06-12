/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
//##60 package coins.aflow;
package coins.flow; //##60

//##60 import coins.aflow.util.BitVectorIterator;
import coins.sym.ExpId; //##60


/** ExpVectorIterator interface  (##6)
 *  Interface to traverse all expressions
 *  that has true bit in specified ExpVector.
*/
public interface ExpVectorIterator extends BitVectorIterator {

  /** nextFlowAnalSym
   *  Get the next FlowAnalSym symbol that has true bit in the given
   *  ExpVector. FlowAnalSym that have false bit in the ExpVector
   *  are skipped.
   *  If the last FlowAnalSym has false bit , then null is
   *  returned at the last time. Therefore, having <code>hasNext</code> returned <code>true</code> does not guarantee this method returns meaningful (non-null) value.
  */
  //##60 public FlowExpId nextFlowExpId(); //##11
  public ExpId nextExpId(); //##60
}
 // ExpVectorIterator interface
