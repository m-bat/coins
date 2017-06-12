/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend;


/**
 * Storage class encoder/decoder.
 * Storage class is represented by int, not an instance of the class.
 */
public class Storage {

  public static final int STATIC = 0;
  public static final int FRAME = 1;
  public static final int REG = 2;

  // Inhibit invokation of constructor.
  private Storage() {}

  public static String toString(int storage) {
    switch (storage){
    case STATIC: return Keyword.STATIC;
    case FRAME: return Keyword.FRAME;
    case REG: return Keyword.REG;
    default: return "?";
    }
  }

  public static int decode(String word) throws SyntaxError {
    if (word == Keyword.STATIC)
      return STATIC;
    else if (word == Keyword.FRAME)
      return FRAME;
    else if (word == Keyword.REG)
      return REG;
    else
      throw new SyntaxError("Expected STATIC/FRAME/REG but " + word);
  }
}

