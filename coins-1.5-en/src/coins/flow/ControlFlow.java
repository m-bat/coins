/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.flow;

public interface ControlFlow
{
  public ShowControlFlow getShowControlFlow(); //##60

  public int domLookUp(int ppNo);

  public int domBitLookUp(int pBitPos);
} // Flow interface
