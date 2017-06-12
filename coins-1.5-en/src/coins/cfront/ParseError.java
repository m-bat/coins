/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
//##8 : Changed when control structure was changed (Sep. 2001).

package coins.cfront;

/**
 * Compilation error.
 */
public class ParseError extends Exception {
    private String fileName;
    private int lineNumber;
    private String reason;
    private Lex lex; //##81

    public ParseError(Lex l, String s) {
  super(l.getFileName() + ':' + l.getLineNumber());
  fileName = l.getFileName();
  lineNumber = l.getLineNumber();
  reason = s;
  l.ioRoot.addToTotalErrorCount(1);
  //if (l.ioRoot.dbgControl.getLevel() > 0) //##15
  //  l.ioRoot.printOut.println("ParserError " + //##15
  //    fileName + ':' + lineNumber + " " + s + " " + l.getString() + " in " + l.getTextBuffer().toString()); //##17
  if (l.ioRoot.dbgControl.getLevel() > 0) //##81
    l.ioRoot.printOut.println("ParserError " + //##81
      fileName + ':' + lineNumber + " " + s + " " + l.getString() + " in " + l.getTextBuffer().toString()); //##81
    }

    public ParseError(Lex l, char c) {
  this(l, c + " is missing.");
  printMessage(l, c+ " is missing."); //##68
    }

    public ParseError(Lex l) {
  this(l, "syntax error");
  lex = l;  //##81
  printMessage(l, "syntax error"); //##68
    }

    public ParseError(Lex l, String fname, int line, String s) {
  super(fname + ':' + line + ' ' + s);
  fileName = fname;
  lineNumber = line;
  reason = s;
  l.ioRoot.addToTotalErrorCount(1);
  printMessage(l, fname, line, s); //##68
    }

    public ParseError badLvalue(String op) {
  reason = "invalid lvalue in " + op;
  return this;
    }

    public String getMessage() {
  return super.getMessage() + ' ' + reason;
    }

//##68 BEGIN
private void printMessage( Lex pLex, String pString )
{
  if ((pLex.ioRoot.dbgControl.getLevel() > 0)||
      (pLex.ioRoot.dbgParse.getLevel() > 0)) {
    System.out.print("\n" + pLex.getFileName()+":"
      + pLex.getLineNumber() + " " + pString);
  }
} // printMessage
private void printMessage(Lex pLex, String pFile, int pLine, String pMessage)
{
  if ((pLex.ioRoot.dbgControl.getLevel() > 0)||
      (pLex.ioRoot.dbgParse.getLevel() > 0)) {
    System.out.print("\n" + pFile+":"
      + pLine + " " + pMessage);
  }
} // printMessage
//##68 END
}
