/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend;

import coins.backend.util.ImList;


/** Interface for function transformer. */
public interface LocalTransformer extends Transformer {

  /** Transform the L-function <code>func</code>.
   ** @param func L-function to be transformed.
   ** @param args list of optional arguments.
   ** @return true if transformation suceeded. **/
  boolean doIt(Function func, ImList args);

  /** Transform the DATA component.
   ** @param data DATA to be transformed.
   ** @param args list of optional arguments.
   ** @return true if transformation suceeded. **/
  boolean doIt(Data data, ImList args);
}
