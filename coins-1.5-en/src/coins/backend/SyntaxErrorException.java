/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend;

import coins.PassException;
import java.io.File;

/**
 * A parse error in LIR.
 **/
public class SyntaxErrorException extends PassException {
  private final static String fMyName = "LIR Parser";
  private final static String fMessageHeader = "LIR syntax error: ";
  private final String fMessage;

  /**
   * Gets an error message.  Overrides Throwable#getMessage().
   **/
  /*public String getMessage() {return fMessage;}*/

  /**
   * Creates a parse error with a message, a source file name, and a line
   * number.
   *
   * @param pSourceFile
   *    The source file which contains this syntax error.
   * @param pLineNumber
   *    The line number in the source file which contains this syntax error.
   * @param pMessage
   *    The error message describing this syntax error.
   **/
  public SyntaxErrorException(File pSourceFile,
			      int pLineNumber,
			      String pMessage) {
    super(pSourceFile, pLineNumber, fMyName, fMessageHeader + pMessage);
    fMessage = pMessage;
  }

  /**
   * Creates a parse error with a message and a source file name.  Use when a
   * line number cannot be specified.
   *
   * @param pSourceFile
   *    The source file which contains this syntax error.
   * @param pMessage
   *    The error message describing this syntax error.
   **/
  public SyntaxErrorException(File pSourceFile, String pMessage) {
    super(pSourceFile, fMyName, fMessageHeader + pMessage);
    fMessage = pMessage;
  }

  /**
   * Creates a parse error with a message.  Use when a source file cannot be
   * specified.
   *
   * @param pMessage
   *    The error message describing this syntax error.
   **/
  public SyntaxErrorException(String pMessage) {
    super(fMyName, fMessageHeader + pMessage);
    fMessage = pMessage;
  }
}
