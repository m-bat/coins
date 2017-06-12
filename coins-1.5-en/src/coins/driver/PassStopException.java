/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.driver;

import coins.PassException;

import java.io.File;
import java.io.IOException;

/**
 * Even if the PassStopException is a derived class of PassException, it is
 * not treated as an exceptional event, but a global exitting from a depth of
 * compiler modules.  By throwing PassStopException,
 * CompilerImplementation#compile() is stopped at once, and
 * CompilerImplementation#assemble() is called (if no option preventing it is
 * specified.  Exit status of the compilation is not affected, but linking is
 * canceled.
 **/

public class PassStopException extends PassException {
  /**
   * Creates an instance with a message, a source file name, and a line
   * number.
   *
   * @param sourceFile
   *    The source file which contains this exception.
   * @param lineNumber
   *    The line number in the source file which contains this exception.
   * @param passName
   *    The pass name where this exception is occurred.
   * @param message
   *    The message describing this exception.
   */
  public PassStopException(File sourceFile, int lineNumber,
			   String passName, String message) {
    super(sourceFile, lineNumber, passName, message);
  }

  /**
   * Creates an instance with a message and a source file name.
   * Use when a line number cannot be specified.
   *
   * @param sourceFile
   *    The source file which contains this exception.
   * @param passName
   *    The pass name where this exception is occurred.
   * @param message
   *    The  message describing this exception.
   */
  public PassStopException(File sourceFile, String passName, String message) {
    super(sourceFile, passName, message);
  }

  /**
   * Creates an instance with a message.
   * Use when a source file cannot be specified (e.g., linker).
   *
   * @param passName
   *    The pass name where this exception is occurred.
   * @param message
   *    The message describing this exception.
   */
  public PassStopException(String passName, String message) {
    super(passName, message);
  }
}
