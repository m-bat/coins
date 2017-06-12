/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend;


/** Miscellaneous error has happened **/
public class CantHappenException extends RuntimeException {
  private String msg;

  public String getMessage() { return msg; }

  public CantHappenException(String msg) {
    this.msg = msg;
  }

  public CantHappenException() {
    this.msg = "Can't happen.";
  }
}

