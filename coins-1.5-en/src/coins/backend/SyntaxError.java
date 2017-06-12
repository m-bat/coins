/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend;


/** Parse error exception */
public class SyntaxError extends Exception {
  private String fmsg;

  public String getMessage() { return fmsg; }

  public SyntaxError(String pmsg) {
    fmsg = pmsg;
  }
}

