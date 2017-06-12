/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.lir;

/** Call-back interface for LirNode#pickUpUses, #pickUpDefs */
public interface PickUpVariable {
  void meetVar(LirNode node);
}

