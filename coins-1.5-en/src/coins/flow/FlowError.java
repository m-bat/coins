/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
//##60 package coins.aflow.util;
package coins.flow; //##60

public class FlowError
  extends Error
{
  public FlowError()
  {
    super();
  }

  public FlowError(String s)
  {
    super(s);
  }
}
