/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
//##8 : Changed when control structure was changed (Sep. 2001).

package coins.cfront;

import java.io.*;
import coins.IoRoot; //##8

/**
 * Lexical analyzer for GCC.  Use this class if a C program includes
 * GCC's header files.
 */
public class GccLex extends Lex {
    //##81 private static final KeywordTable ktable = new KeywordTable(10);
    private static final KeywordTable ktable = new KeywordTable(12); //##81

    static {
  // the ordering must be alphabetical order

  ktable.append("__asm", ASM);
  //%%85 ktable.append("__asm__", ASM);
  ktable.append("__asm__", SKIP_GCC_ASM); //##85
  //##70 ktable.append("__attribute__", ASM);	// skip
  //##85 ktable.append("__attribute__", SKIP_GCC_ASM);	// skip //##70
  ktable.append("__attribute__", SKIP_GCC_ATTRIBUTE);	// skip //##70
  ktable.append("__const", CONST);
  ktable.append("__extension__", IGNORE); //##81 code from Nakata's callprintE.c
  ktable.append("__inline", IGNORE);
  ktable.append("__inline__", IGNORE);
  ktable.append("__restrict", RESTRICT);  //##81 in alphabetical order
  ktable.append("__signed", SIGNED);
  ktable.append("__signed__", SIGNED);
    }

    public GccLex(IoRoot ioroot, InputStream s) { //##8
  super(ioroot, s); //##8
    }

    protected int checkExtendedKeywords(StringBuffer sbuf) {
  return ktable.lookup(sbuf);
    }
}
