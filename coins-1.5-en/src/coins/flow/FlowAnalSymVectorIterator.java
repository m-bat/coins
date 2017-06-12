/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * FlowAnalSymVectorIterator.java
 *
 * Created on September 25, 2002, 9:34 AM
 */
//##60 package coins.aflow;
package coins.flow;

//60 import coins.aflow.util.BitVectorIterator;
import coins.sym.FlowAnalSym;

/**
 *
 * @author  hasegawa
 */
public interface FlowAnalSymVectorIterator
  //##60 extends BitVectorIterator
  extends ExpVectorIterator //##60
{
  /**
   * Gets next FlowAnalSym whose bit is set in the underlying FlowAnalSymVector and advances the implicit cursor just after the bit that is found to be set. If there is no such bit remaining, returns null. hasNext() returning <code>true</code> does not guarantee this method returns non-null value, since hasNext() merely check against the length of the BitVector.
   */
  FlowAnalSym nextFlowAnalSym();
}
