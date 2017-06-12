/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.gen;

import coins.backend.lir.LirNode;


/** Interface for state. **/
interface State {
  /** Fill this state for node t. **/
  void label(LirNode t, State k0, State k1, State k2, State k3);

  /** Return production rule object. **/
  Rule ruleFor(int goal);
}
