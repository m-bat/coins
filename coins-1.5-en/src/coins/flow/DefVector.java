/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
//##60 package coins.aflow;
package coins.flow; //##60

import java.util.Set;


/** DefVector interface
 * A bit in this <code>BitVector</code> corresponds to
 * a <code>SetRefRepr</code>. The bit length corresponds to
 * the number of <code>SetRefRepr</code>s in a <code>SubpFlow</code>.
 **/
public interface DefVector extends PointVector {
  DefVectorIterator defVectorIterator();
}
 // DefVector interface
