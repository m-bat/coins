/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend;


/**
 * LIR type encoder/decoder without instance.
 * Type is represented by an int, not an instance of the class.
 */
public class Type {

  /* Encoding:
   * LSB=1: Aggregate type, size=bit1..31 
   * 0: void or unknown
   * 2: int
   * 4: float
   * 6 or greater: unassigned
   */
  private static final int TAGWIDTH = 4;
  private static final int TAGMASK = ((1 << TAGWIDTH) - 1);
  private static final int BITSMASK = (1 << (32 - TAGWIDTH)) - 1;
  private static final int MAXBITS = BITSMASK;

  private static final int AGGRBIT = 0x01;
  private static final int AGGRBYTEMASK = (1 << 31) - 1;
  private static final int MAXAGGRBYTES = AGGRBYTEMASK;

  public static final int UNKNOWN = 0;
  public static final int AGGREGATE = 1;
  public static final int INT = 2;
  public static final int FLOAT = 4;


  // Inhibit invokation of constructor.
  private Type() {}

  public static String toString(int type) {
    if (isAggregate(type))
      return "A" + (long)aggregateBytes(type) * 8;

    int bits = bits(type);
    switch (type & TAGMASK) {
    case UNKNOWN:
      return Keyword.UNKNOWN;
    case INT:
      return "I" + bits;
    case FLOAT:
      return "F" + bits;
    case AGGREGATE:
      return "A" + bits;
    default:
      throw new IllegalArgumentException("bad type");
    }
  }

  public static boolean isAggregate(int type) {
    return (type & AGGRBIT) != 0;
  }

  public static int tag(int type) {
    if (isAggregate(type))
      return AGGREGATE;
    else
      return type & TAGMASK;
  }

  private static int aggregateBytes(int type) {
    return (type >> 1) & AGGRBYTEMASK;
  }

  public static int bits(int type) {
    if (isAggregate(type))
      throw new CantHappenException("aggregate bit required");
      // return (long)aggregateBytes(type) * 8;
    else
      return (type >> TAGWIDTH) & BITSMASK;
  }

  public static int bytes(int type) {
    if (isAggregate(type))
      return aggregateBytes(type);
    else
      return (((type >> TAGWIDTH) & BITSMASK) + 7) / 8;
  }

  public static int type(int tag, long bits) {
    try {
      return typeCheck(tag, bits);
    } catch (SyntaxError e) {
      throw new CantHappenException(e.getMessage());
    }
  }
  
  private static int typeCheck(int tag, long bits) throws SyntaxError {
    if (tag == AGGREGATE) {
      if (bits % 8 != 0)
        throw new SyntaxError("Aggregate size not multiple of 8: " + bits);
      int bytes = (int)(bits / 8);
      if (bytes < 0 || bytes > MAXAGGRBYTES)
        throw new SyntaxError("Aggregate size out of range: " + bits
                              + " (MAX " + ((long)MAXAGGRBYTES * 8) + ")");
      return AGGREGATE + (bytes << 1);
    } else {
      if (bits < 0 || bits > MAXBITS)
        throw new SyntaxError("Typebits out of range: " + bits
                              + " (MAX " + MAXBITS + ")");
      return tag + ((int)bits << TAGWIDTH);
    }
  }
  
  public static int decode(String word) throws SyntaxError {
    if (word == Keyword.UNKNOWN)
      return UNKNOWN;
    else if (word.charAt(0) == 'I')
      return typeCheck(INT, Integer.parseInt(word.substring(1)));
    else if (word.charAt(0) == 'F')
      return typeCheck(FLOAT, Integer.parseInt(word.substring(1)));
    else if (word.charAt(0) == 'A')
      return typeCheck(AGGREGATE, Long.parseLong(word.substring(1)));
    else
      throw new SyntaxError("Type expected but " + word);
  }
}
