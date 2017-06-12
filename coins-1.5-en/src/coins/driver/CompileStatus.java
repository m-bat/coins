/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.driver;

/**
 *
 * This object keeps a status of compilation process, i.e., an exit status of
 * a compiler process and a flag to show a linking is required or not.  Shared
 * by all compile threads.<br>
 *
 * User compiler modules are expected to use interface of class CompileThread.
 * This class is used to implement it.  Thus, not a public class.
 **/

public class CompileStatus {
  static final public int NORMAL_END = 0;
  static final public int ABNORMAL_END = 1;

  private int fExitStatus;
  private boolean fLinking;

  CompileStatus() {fExitStatus = NORMAL_END; fLinking = true;}
  synchronized int getExitStatus() {return fExitStatus;}
  synchronized void setExitStatus(int s) {fExitStatus = s;}
  synchronized void setABEND() {fExitStatus = ABNORMAL_END; cancelLinking();}

  synchronized void cancelLinking() {fLinking = false;}
  synchronized boolean isLinkingRequired() {return fLinking;}
  synchronized boolean isLinkingCanceled() {return ! fLinking;}
}
