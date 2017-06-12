/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.ast.expr;

/**
 * Constant <code>wchar_t</code> character string.
 *
 * <p><code>wchar_t</code> strings are not correctly supported.
 * They are dealt as normal char strings.
 */
public class WcharLiteral extends StringLiteral {
    public WcharLiteral(String s) {
  super(s);
    }

    public String toString() {
  return "L\"" + string + "\"";
    }
}
