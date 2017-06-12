/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.driver;

/**
 *
 * This object keeps the exit status of the compiler process.  Shared by the
 * all compile threads.<br>
 *
 * User compiler modules are expected to use interface of class
 * CompileThread.  This class is used to implment the interface.
 **/

class CompilerExitStatus {
  static final private int NORMAL_END = 0;
  static final private int ABNORMAL_END = 1;

  private int fStatus;

  CompilerExitStatus() {fStatus = NORMAL_END;}
  synchronized int get() {return fStatus;}
  synchronized void set(int s) {fStatus = s;}
  synchronized void setABEND() {fStatus = ABNORMAL_END;}
}
