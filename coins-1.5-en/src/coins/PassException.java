/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins;

import java.io.File;
import java.io.IOException;

/**
 * Various exceptions in passes.<br>
 * When a compiler driver catches a <tt>PassException</tt>, it is expected
 * that 1) the driver restores the contexts (i.e., HirRoot, LirRoot, etc.) in
 * the state at prior to the beginning of the pass and restarts the
 * compilation of the source file by going an alternative way, or 2) the
 * driver abandons the source file, goes on the next source file and doesn't
 * link object files.<br>
 * <tt>PassException</tt> thrower must write an error message if it is
 * required, because the compiler driver never writes it.
 **/

public class PassException extends Exception {
  /**
   * Creates a pass exception with a message, a source file name, and a line
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
  public PassException(File sourceFile, int lineNumber,
		       String passName, String message) {
    super(sourceFile.getAbsolutePath() + ":" + lineNumber + ": " +
	  passName + ": " + message);
  }

  /**
   * Creates a pass exception with a message and a source file name.
   * Use when a line number cannot be specified.
   *
   * @param sourceFile
   *    The source file which contains this exception.
   * @param passName
   *    The pass name where this exception is occurred.
   * @param message
   *    The  message describing this exception.
   */
  public PassException(File sourceFile, String passName, String message) {
    super(sourceFile.getAbsolutePath() + ":" + passName + ": " + message);
  }

  /**
   * Creates a pass exception with a message.
   * Use when a source file cannot be specified (e.g., linker).
   *
   * @param passName
   *    The pass name where this exception is occurred.
   * @param message
   *    The message describing this exception.
   */
  public PassException(String passName, String message) {
    super(passName + ": " + message);
  }
}
