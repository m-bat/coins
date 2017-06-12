/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend;

/**
 * Definitions of LIR keywords.
 * Users of these words may implement this interface.
 */
public interface Keyword {
  String MODULE = "MODULE";
  String SYMTAB = "SYMTAB";
  String DATA = "DATA";
  String FUNCTION = "FUNCTION";
  String STATIC = "STATIC";
  String FRAME = "FRAME";
  String REG = "REG";

  String UNKNOWN = "UNKNOWN";

  String REGSET = "&regset";

  /* Thu May 29 15:25:21 JST 2003 by ak */
  String LDEF = "LDEF";
  String XDEF = "XDEF";
  String XREF = "XREF";
  String ZEROS = "ZEROS";
  String SPACE = "SPACE";
}

