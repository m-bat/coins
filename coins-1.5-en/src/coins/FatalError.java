/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins;

/**
 *
 * Abstraction of a fatal error.<br>
 *
 * A <tt>FatalError</tt> represents a disastarous event and throwing it will
 * stops the entire compilation.  A compiler driver is not expected to catch a
 * <tt>FatalError</tt>, and the VM will show a stack trace.<br>
 *
 * <tt>FatalError</tt> thrower must write an error message, because the
 * compiler driver never writes it.
 **/

public class FatalError extends Error {
  /**
   * Creates a fatal error.
   *
   * @param message a message describing this error.
   **/
  public FatalError(String message) {
    super("Fatal: " + message);
  }
  /**
   * Creates a fatal error.  //## Tan
   *
   * @param messageNumber message number. 
   * @param message a message describing this error.
   **/
  public FatalError(int messageNumber, String message) { 
    super("Fatal (" + messageNumber + "): " + message);
  }
}
