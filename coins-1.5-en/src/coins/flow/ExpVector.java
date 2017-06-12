/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.flow; //##60

import coins.sym.ExpId; //##60
import java.util.Set;

/** ExpVector interface
 * An ExpVector is a BitVector
 * each of whose bit corresponds to an ExpId.
*/
public interface ExpVector extends BitVector {
  /**
   * SubpFlow instance associated with this ExpVector.
   */
  SubpFlow getSubpFlow();

  /**
   * Returns the Set of ExpIds whose corresponding bits are set
   * in this ExpVector.
   */
  Set exps();

  /**
   * Returns true if this ExpVector's bit corresponding to
   * the given argument pExpId is set. Returns false otherwise.
   */
  //##60 boolean contains(FlowExpId pFlowExpId);
  boolean contains(ExpId pExpId);

  ExpVectorIterator expVectorIterator( );

  String toStringShort(); //##25
}

// ExpVector interface
