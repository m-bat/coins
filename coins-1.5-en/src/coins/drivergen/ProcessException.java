/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.drivergen;

import coins.PassException;

/**
 * ProcessException is derived clas of PassException.
 * If the process stopped with some error, but not need to stop the follwing
 * compile processes, the process throws ProcessException.
 **/

public class ProcessException extends PassException{
  /**
   * Creates an instance with a message and a process name
   *
   * @param op          Options
   * @param processName the process name where this exception is occurred.
   * @param message     the message describing this exception.
   **/
  public ProcessException(Options op, String processName, String message) {
    super(op.sourceFile, processName, message);
  }
}
