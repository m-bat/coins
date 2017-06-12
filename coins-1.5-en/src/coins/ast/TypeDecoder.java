/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.ast;

/**
 * Utility methods for interpreting an encoded type.
 */
public class TypeDecoder implements TypeId {
    /**
     * Skips const, volatile, signed, and unsigned.
     *
     * @param start	the index to start the skiping from.
     * @return		the index of the first character that is not
     *			const, volatile, signed, or unsigned.
     */
public static int skipCVSU(byte[] type, int start) {
  int len = type.length;
  for (int i = start; i < len; ++i) {
      int c = type[i] & 0xFF;
      if (c != CONST_T && c != VOLATILE_T
          && c != RESTRICT_T  //##81
    && c != SIGNED_T && c != UNSIGNED_T) {
    return i;
    }
  }
  return -1;	// error
}

    public static boolean isArray(byte[] type, int start) {
  int c = type[skipCVSU(type, 0)] & 0xFF;
  return c == ARRAY_T;
    }

    public static boolean isFunction(byte[] type, int start) {
  int c = type[skipCVSU(type, 0)] & 0xFF;
  return c == FUNCTION_T;
    }

    public static boolean isArrayOrFunction(byte[] type, int start) {
  int c = type[skipCVSU(type, 0)] & 0xFF;
  return c == FUNCTION_T || c == ARRAY_T;
    }

    public static String toString(byte[] type, int start) {
  StringBuffer sbuf = new StringBuffer();
  for (int i = start; i < type.length; ++i)
      sbuf.append((char)type[i]);

  return sbuf.toString();
    }
}
