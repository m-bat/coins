/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
//##60 package coins.aflow;
package coins.flow; //##60

//##60 import coins.aflow.util.BitVector;
//##60 import coins.aflow.util.BitVectorIteratorImpl;
//##60 import coins.aflow.util.FlowError;
import coins.ir.IR;


/** PointVectorIteratorImpl class  (##6)
 *
**/
public class PointVectorIteratorImpl extends BitVectorIteratorImpl
  implements PointVectorIterator 
{
  //##60 protected BitVector fVect;

  //##60 public PointVectorIteratorImpl(PointVector pPointVector)
  public PointVectorIteratorImpl(SubpFlow pSubpFlow, PointVector pPointVector)
   {
  //##60 super((BitVector) pPointVector);
  super(pSubpFlow, (BitVector) pPointVector); //##60
  //##60 fVect = pPointVector;
  }

  public IR nextPoint() {
  //##60 throw new FlowError();
  return null; //##60
  }
   // nextPoint
}
 // PointVectorIteratorImpl class
