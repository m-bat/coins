/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * FlowAnalSymVector.java
 *
 * Created on September 6, 2002, 1:40 PM
 */
//##60 package coins.aflow;
package coins.flow; //##60

import java.util.Set;

//##60 import coins.aflow.util.BitVector;
import coins.sym.FlowAnalSym;

/**
 * BitVector class where each bit represents a FlowAnalSym.
 * @author  hasegawa
 */
public interface FlowAnalSymVector
  //##60 extends BitVector
  extends ExpVector
{
  /**
   * Returns a SubpFlow instance associated with this vector.
   */
  SubpFlow getSubpFlow();

  /**
   * Returns the set view of this vector.
   */
  Set flowAnalSyms();

  /**
   * Does the set view of this vector contains the specified item?
   */
  boolean contains(FlowAnalSym pFlowAnalSym);

  /**
   * Removes the given argument from the set view of this vector. The corresponding bit in this vector will be reset.
   *
   * @return true if actually the given argument existed and has been removed.
   */
  boolean remove(FlowAnalSym pFlowAnalSym);

  /**
   * Adds the FlowAnalSymVector view of the given set to this vector.
   *
   * @return true if this vector changed after the operation.
   */
  boolean addAll(Set pFlowAnalSyms);

  coins.flow.FlowAnalSymVectorIterator flowAnalSymVectorIterator();

  public ExpVector flowAnalSymToExpVector(); //##25

  public String toStringShort(); //##25

}
