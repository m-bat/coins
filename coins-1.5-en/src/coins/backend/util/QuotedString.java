/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.util;


/** Represent quoted string in S-expression. **/
public class QuotedString {
  public final String body;

  /** Create QuotedString. **/
  public QuotedString(String str) {
    body = str.intern();
  }

  /** Visualize. **/
  public String toString() {
    return quoteString(body);
  }

  public int hashCode() {
    return body.hashCode();
  }

  public boolean equals(Object obj) {
    return obj instanceof QuotedString
      && ((QuotedString)obj).body.equals(body);
  }


  /** Return quoted string of s. **/
  public static String quoteString(String s) {
    StringBuffer buff = new StringBuffer();
    char[] a = s.toCharArray();
    buff.append('\"');
    for (int i = 0; i < a.length; i++) {
      switch(a[i]) {
      case '\"': buff.append("\\\""); break;
      case '\\': buff.append("\\\\"); break;
      case '\n': buff.append("\\n"); break;
      case '\t': buff.append("\\t"); break;
      case '\r': buff.append("\\r"); break;
      case '\b': buff.append("\\b"); break;
      case '\f': buff.append("\\f"); break;
      default:
        if (Character.isISOControl(a[i])) {
          buff.append('\\');
          buff.append(lastNchars("00" + Integer.toOctalString(a[i]), 3));
        } else {
          buff.append(a[i]);
        }
        break;
      }
    }
    buff.append('\"');
    return buff.toString();
  }

  /** Return last n-character string **/
  private static String lastNchars(String s, int n) {
    return s.substring(s.length() - n);
  }
}
