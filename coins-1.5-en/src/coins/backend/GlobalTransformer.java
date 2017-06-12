/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend;

import coins.backend.util.ImList;


/** Interface for L-module transformer **/
public interface GlobalTransformer extends Transformer {

  /** Transform the L-Module.
   ** @param mod L-module to be transformed.
   ** @param args list of optional arguments.
   ** @return true if transformation suceeded. **/
  boolean doIt(Module mod, ImList args);
}
