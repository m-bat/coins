/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.flow;

import coins.ir.IR;

/** DefVectorIterator interface  ()
 *  Interface to traverse all nodes defining some symbol value
 *  in specified DefVector.
 **/
public interface
  DefVectorIterator
  extends PointVectorIterator
{

  /** nextDefNode
   *  Get the next IR node that defines some symbol
   *  in the given DefVector (DefVector used to instanciate this iterator).
   *  Nodes that have 0 in the DefVector are skipped.
   *  If the last node is not a defining node, then null is
   *  returned at the last time.
   **/
  public IR
    nextDefNode();

  public SetRefRepr
    nextSetRefRepr();
} // DefVectorIterator interface
