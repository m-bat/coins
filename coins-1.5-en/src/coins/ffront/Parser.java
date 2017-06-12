/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
// created by jay 0.8 (c) 1998 Axel.Schreiner@informatik.uni-osnabrueck.de

					// line 2 "../coins-0.10.2/src/coins/ffront/f77k.jay"
package coins.ffront;

import coins.HirRoot;
import coins.IoRoot;
import coins.SymRoot;
import coins.ir.hir.HIR;

public class Parser {
	F77Hir fHir;
	F77Sym fSym;
	Scanner yyLex;
	
	public Parser(SymRoot sRoot, HirRoot hRoot, IoRoot iRoot, Scanner lexer) {
		fHir = new F77Hir(sRoot, hRoot, iRoot);
		fSym = new F77Sym(sRoot, hRoot, iRoot, fHir);
		fHir.setF77Sym(fSym);
		yyLex = lexer;
	}
    public F77Hir getHir(){
      return fHir;
    }
					// line 30 "-"
// %token constants

  public static final int REAL_CONST = 257;
  public static final int CHAR_CONST = 258;
  public static final int INT_CONST = 259;
  public static final int DOUBLE_CONST = 260;
  public static final int IDENT = 261;
  public static final int LABEL_DEF = 262;
  public static final int LABEL = 263;
  public static final int EOS = 264;
  public static final int NO_LABEL = 265;
  public static final int TRUE_CONST = 266;
  public static final int FALSE_CONST = 267;
  public static final int INTEGER = 268;
  public static final int REAL = 269;
  public static final int COMPLEX = 270;
  public static final int DOUBLE_PREC = 271;
  public static final int LOGICAL = 272;
  public static final int CHARACTER = 273;
  public static final int PARAM = 274;
  public static final int LET = 275;
  public static final int ARITH_IF = 276;
  public static final int IF = 277;
  public static final int ASSIGN = 278;
  public static final int BLOCKDATA = 279;
  public static final int CALL = 280;
  public static final int CLOSE = 281;
  public static final int COMMON = 282;
  public static final int CONTINUE = 283;
  public static final int DATA = 284;
  public static final int DIMENSION = 285;
  public static final int DO = 286;
  public static final int END_DO = 287;
  public static final int ELSE = 288;
  public static final int ELSE_IF = 289;
  public static final int END = 290;
  public static final int END_FILE = 291;
  public static final int END_IF = 292;
  public static final int ENTRY = 293;
  public static final int EQUIVALENCE = 294;
  public static final int EXTERNAL = 295;
  public static final int FORMAT = 296;
  public static final int FUNCTION = 297;
  public static final int GOTO = 298;
  public static final int ASSIGN_GOTO = 299;
  public static final int COMP_GOTO = 300;
  public static final int IMPLICIT = 301;
  public static final int INTRINSIC = 302;
  public static final int PAUSE = 303;
  public static final int PRINT = 304;
  public static final int PROGRAM = 305;
  public static final int READ = 306;
  public static final int RETURN = 307;
  public static final int SAVE = 308;
  public static final int STOP = 309;
  public static final int SUBROUTINE = 310;
  public static final int THEN = 311;
  public static final int TO = 312;
  public static final int WRITE = 313;
  public static final int OPEN = 314;
  public static final int INQUIRE = 315;
  public static final int BACKSPACE = 316;
  public static final int REWIND = 317;
  public static final int AND = 318;
  public static final int OR = 319;
  public static final int NEQV = 320;
  public static final int EQV = 321;
  public static final int NOT = 322;
  public static final int EQUAL = 323;
  public static final int LESS_THAN = 324;
  public static final int GREATER_THAN = 325;
  public static final int LESS_OR_EQUAL = 326;
  public static final int GREATER_OR_EQUAL = 327;
  public static final int NOT_EQUAL = 328;
  public static final int POWER = 329;
  public static final int DOUBLE_SLASH = 330;
  public static final int FORMAT_SPECIFICATION = 331;
  public static final int UMINUS = 332;
  public static final int UPLUS = 333;
  public static final int yyErrorCode = 256;

  /** thrown for irrecoverable syntax errors and stack overflow.
    */
  public static class yyException extends java.lang.Exception {
    public yyException (String message) {
      super(message);
    }
  }

  /** must be implemented by a scanner object to supply input to the parser.
    */
  public interface yyInput {
    /** move on to next token.
        @return false if positioned beyond tokens.
        @throws IOException on input error.
      */
    boolean advance () throws java.io.IOException;
    /** classifies current token.
        Should not be called if advance() returned false.
        @return current %token or single character.
      */
    int token ();
    /** associated with current token.
        Should not be called if advance() returned false.
        @return value for token().
      */
    Object value ();
  }

  /** simplified error message.
      @see <a href="#yyerror(java.lang.String, java.lang.String[])">yyerror</a>
    */
  public void yyerror (String message) {
    yyerror(message, null);
  }

  /** (syntax) error message.
      Can be overwritten to control message format.
      @param message text to be displayed.
      @param expected vector of acceptable tokens, if available.
    */
  public void yyerror (String message, String[] expected) {
    if (expected != null && expected.length > 0) {
      System.err.print(message+", expecting");
      for (int n = 0; n < expected.length; ++ n)
        System.err.print(" "+expected[n]);
      System.err.println();
    } else
      System.err.println(message);
  }

  /** debugging support, requires the package jay.yydebug.
      Set to null to suppress debugging messages.
    */
//t  protected jay.yydebug.yyDebug yydebug;

  protected static final int yyFinal = 3;

  /** index-checked interface to yyName[].
      @param token single character or %token value.
      @return token name or [illegal] or [unknown].
    */
//t  public static final String yyname (int token) {
//t    if (token < 0 || token > YyNameClass.yyName.length) return "[illegal]";
//t    String name;
//t    if ((name = YyNameClass.yyName[token]) != null) return name;
//t    return "[unknown]";
//t  }

  /** computes list of expected tokens on error by tracing the tables.
      @param state for which to compute the list.
      @return list of token names.
    */
  protected String[] yyExpecting (int state) {
    int token, n, len = 0;
    boolean[] ok = new boolean[YyNameClass.yyName.length];

    if ((n = YySindexClass.yySindex[state]) != 0)
      for (token = n < 0 ? -n : 0;
           token < YyNameClass.yyName.length && n+token < YyTableClass.yyTable.length; ++ token)
        if (YyCheckClass.yyCheck[n+token] == token && !ok[token] && YyNameClass.yyName[token] != null) {
          ++ len;
          ok[token] = true;
        }
    if ((n = YyRindexClass.yyRindex[state]) != 0)
      for (token = n < 0 ? -n : 0;
           token < YyNameClass.yyName.length && n+token < YyTableClass.yyTable.length; ++ token)
        if (YyCheckClass.yyCheck[n+token] == token && !ok[token] && YyNameClass.yyName[token] != null) {
          ++ len;
          ok[token] = true;
        }

    String result[] = new String[len];
    for (n = token = 0; n < len;  ++ token)
      if (ok[token]) result[n++] = YyNameClass.yyName[token];
    return result;
  }

  /** the generated parser, with debugging messages.
      Maintains a state and a value stack, currently with fixed maximum size.
      @param yyLex scanner.
      @param yydebug debug message writer implementing yyDebug, or null.
      @return result of the last reduction, if any.
      @throws yyException on irrecoverable parse error.
    */
  public Object yyparse (yyInput yyLex, Object yydebug)
				throws java.io.IOException, yyException {
//t    this.yydebug = (jay.yydebug.yyDebug)yydebug;
    return yyparse(yyLex);
  }

  /** initial size and increment of the state/value stack [default 256].
      This is not final so that it can be overwritten outside of invocations
      of yyparse().
    */
  protected int yyMax;

  /** executed at the beginning of a reduce action.
      Used as $$ = yyDefault($1), prior to the user-specified action, if any.
      Can be overwritten to provide deep copy, etc.
      @param first value for $1, or null.
      @return first.
    */
  protected Object yyDefault (Object first) {
    return first;
  }

  /** the generated parser.
      Maintains a state and a value stack, currently with fixed maximum size.
      @param yyLex scanner.
      @return result of the last reduction, if any.
      @throws yyException on irrecoverable parse error.
    */
  public Object yyparse (yyInput yyLex)
				throws java.io.IOException, yyException {
    if (yyMax <= 0) yyMax = 256;			// initial size
    int yyState = 0, yyStates[] = new int[yyMax];	// state stack
    Object yyVal = null, yyVals[] = new Object[yyMax];	// value stack
    int yyToken = -1;					// current input
    int yyErrorFlag = 0;				// #tks to shift

    yyLoop: for (int yyTop = 0;; ++ yyTop) {
      if (yyTop >= yyStates.length) {			// dynamically increase
        int[] i = new int[yyStates.length+yyMax];
        System.arraycopy(yyStates, 0, i, 0, yyStates.length);
        yyStates = i;
        Object[] o = new Object[yyVals.length+yyMax];
        System.arraycopy(yyVals, 0, o, 0, yyVals.length);
        yyVals = o;
      }
      yyStates[yyTop] = yyState;
      yyVals[yyTop] = yyVal;
//t      if (yydebug != null) yydebug.push(yyState, yyVal);

      yyDiscarded: for (;;) {	// discarding a token does not change stack
        int yyN;
        if ((yyN = YyDefRedClass.yyDefRed[yyState]) == 0) {	// else [default] reduce (yyN)
          if (yyToken < 0) {
            yyToken = yyLex.advance() ? yyLex.token() : 0;
//t            if (yydebug != null)
//t              yydebug.lex(yyState, yyToken, yyname(yyToken), yyLex.value());
          }
          if ((yyN = YySindexClass.yySindex[yyState]) != 0 && (yyN += yyToken) >= 0
              && yyN < YyTableClass.yyTable.length && YyCheckClass.yyCheck[yyN] == yyToken) {
//t            if (yydebug != null)
//t              yydebug.shift(yyState, YyTableClass.yyTable[yyN], yyErrorFlag-1);
            yyState = YyTableClass.yyTable[yyN];		// shift to yyN
            yyVal = yyLex.value();
            yyToken = -1;
            if (yyErrorFlag > 0) -- yyErrorFlag;
            continue yyLoop;
          }
          if ((yyN = YyRindexClass.yyRindex[yyState]) != 0 && (yyN += yyToken) >= 0
              && yyN < YyTableClass.yyTable.length && YyCheckClass.yyCheck[yyN] == yyToken)
            yyN = YyTableClass.yyTable[yyN];			// reduce (yyN)
          else
            switch (yyErrorFlag) {
  
            case 0:
              yyerror("syntax error", yyExpecting(yyState));
//t              if (yydebug != null) yydebug.error("syntax error");
  
            case 1: case 2:
              yyErrorFlag = 3;
              do {
                if ((yyN = YySindexClass.yySindex[yyStates[yyTop]]) != 0
                    && (yyN += yyErrorCode) >= 0 && yyN < YyTableClass.yyTable.length
                    && YyCheckClass.yyCheck[yyN] == yyErrorCode) {
//t                  if (yydebug != null)
//t                    yydebug.shift(yyStates[yyTop], YyTableClass.yyTable[yyN], 3);
                  yyState = YyTableClass.yyTable[yyN];
                  yyVal = yyLex.value();
                  continue yyLoop;
                }
//t                if (yydebug != null) yydebug.pop(yyStates[yyTop]);
              } while (-- yyTop >= 0);
//t              if (yydebug != null) yydebug.reject();
              throw new yyException("irrecoverable syntax error");
  
            case 3:
              if (yyToken == 0) {
//t                if (yydebug != null) yydebug.reject();
                throw new yyException("irrecoverable syntax error at end-of-file");
              }
//t              if (yydebug != null)
//t                yydebug.discard(yyState, yyToken, yyname(yyToken),
//t  							yyLex.value());
              yyToken = -1;
              continue yyDiscarded;		// leave stack alone
            }
        }
        int yyV = yyTop + 1-YyLenClass.yyLen[yyN];
//t        if (yydebug != null)
//t          yydebug.reduce(yyState, yyStates[yyV-1], yyN, YyRuleClass.yyRule[yyN], YyLenClass.yyLen[yyN]);
        yyVal = yyDefault(yyV > yyTop ? null : yyVals[yyV]);
        switch (yyN) {
case 1:
					// line 91 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.executableProgram(((Node)yyVals[0+yyTop])); }
  break;
case 2:
					// line 92 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.executableProgram(((Node)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])); }
  break;
case 3:
					// line 96 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.mainProgram(((FirList)yyVals[0+yyTop])); }
  break;
case 4:
					// line 97 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.mainProgram( ((FirList)yyVals[0+yyTop])); }
  break;
case 5:
					// line 98 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.funcSubProgram(((FStmt)yyVals[-1+yyTop]), ((FirList)yyVals[0+yyTop])); }
  break;
case 6:
					// line 99 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.subrSubProgram(((FStmt)yyVals[-1+yyTop]), ((FirList)yyVals[0+yyTop])); }
  break;
case 7:
					// line 100 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.blockDataSubp(((Node)yyVals[0+yyTop])); }
  break;
case 8:
					// line 103 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.list(((FStmt)yyVals[0+yyTop])); }
  break;
case 9:
					// line 104 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((FirList)yyVals[0+yyTop]).addedFirst(((FStmt)yyVals[-1+yyTop])); }
  break;
case 10:
					// line 107 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.endStmt(((Token)yyVals[-2+yyTop])); }
  break;
case 11:
					// line 111 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.completeStmt(((Token)yyVals[-2+yyTop]), ((FStmt)yyVals[-1+yyTop])); }
  break;
case 12:
					// line 114 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((FStmt)yyVals[0+yyTop]); }
  break;
case 13:
					// line 115 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((FStmt)yyVals[0+yyTop]); }
  break;
case 14:
					// line 116 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((FStmt)yyVals[0+yyTop]); }
  break;
case 15:
					// line 120 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.modifiedToken(((Token)yyVals[0+yyTop])); }
  break;
case 16:
					// line 121 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = null; }
  break;
case 17:
					// line 124 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.programStmt(fSym.modifiedToken(((Token)yyVals[-1+yyTop]))); }
  break;
case 18:
					// line 129 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.funcStmt(((Node)yyVals[-4+yyTop]), fSym.modifiedToken(((Token)yyVals[-2+yyTop])), ((FirList)yyVals[-1+yyTop])); }
  break;
case 19:
					// line 132 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.funcStmt(null, fSym.modifiedToken(((Token)yyVals[-2+yyTop])), ((FirList)yyVals[-1+yyTop])); }
  break;
case 20:
					// line 137 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.subrStmt(fSym.modifiedToken(((Token)yyVals[-2+yyTop])), ((FirList)yyVals[-1+yyTop])); }
  break;
case 21:
					// line 142 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.entryStmt(fSym.modifiedToken(((Token)yyVals[-1+yyTop])), ((FirList)yyVals[0+yyTop])); }
  break;
case 22:
					// line 147 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.blockDataSubProgram(((Node)yyVals[-2+yyTop]), ((Node)yyVals[-1+yyTop])); }
  break;
case 23:
					// line 152 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.blockDataStmt(((Token)yyVals[-3+yyTop]), ((Token)yyVals[-1+yyTop])); }
  break;
case 24:
					// line 156 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = null; }
  break;
case 25:
					// line 157 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.modifiedToken(((Token)yyVals[0+yyTop])); }
  break;
case 26:
					// line 161 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = null; }
  break;
case 27:
					// line 163 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.blockDataBody(((Node)yyVals[-3+yyTop]), ((Token)yyVals[-2+yyTop]), ((FStmt)yyVals[-1+yyTop])); }
  break;
case 28:
					// line 167 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = null; }
  break;
case 29:
					// line 168 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((FirList)yyVals[-1+yyTop]); }
  break;
case 30:
					// line 172 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((FirList)yyVals[0+yyTop]); }
  break;
case 31:
					// line 176 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = null; }
  break;
case 32:
					// line 177 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = null; }
  break;
case 33:
					// line 178 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((FirList)yyVals[-1+yyTop]); }
  break;
case 34:
					// line 182 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.list(((Token)yyVals[0+yyTop])); }
  break;
case 35:
					// line 183 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((FirList)yyVals[-2+yyTop]).addedLast(((Token)yyVals[0+yyTop])); }
  break;
case 36:
					// line 187 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.modifiedToken(((Token)yyVals[0+yyTop])); }
  break;
case 37:
					// line 188 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.modifiedToken(new Token(0, "*".intern())); }
  break;
case 38:
					// line 191 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((FStmt)yyVals[0+yyTop]); }
  break;
case 39:
					// line 192 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((FStmt)yyVals[0+yyTop]); }
  break;
case 40:
					// line 195 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.declList(((Node)yyVals[-1+yyTop]), ((FirList)yyVals[0+yyTop])); }
  break;
case 41:
					// line 196 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.commonDecl(((FirList)yyVals[0+yyTop])); }
  break;
case 42:
					// line 197 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.equivalenceDecl(((FirList)yyVals[0+yyTop])); }
  break;
case 43:
					// line 198 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.dataDecl(((FirList)yyVals[0+yyTop])); }
  break;
case 44:
					// line 199 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.implicitDecl(((FirList)yyVals[0+yyTop])); }
  break;
case 45:
					// line 200 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.saveDecl(((FirList)yyVals[0+yyTop])); }
  break;
case 46:
					// line 201 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.parameterDecl(((FirList)yyVals[-1+yyTop])); }
  break;
case 47:
					// line 204 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = null; }
  break;
case 48:
					// line 205 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((FirList)yyVals[0+yyTop]); }
  break;
case 49:
					// line 209 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.externalDecl(((FirList)yyVals[0+yyTop])); }
  break;
case 50:
					// line 210 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.intrinsicDecl(((FirList)yyVals[0+yyTop])); }
  break;
case 51:
					// line 213 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.list(((Node)yyVals[0+yyTop])); }
  break;
case 52:
					// line 214 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((FirList)yyVals[-2+yyTop]).addedLast(((Node)yyVals[0+yyTop])); }
  break;
case 53:
					// line 218 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.arrayDecl (fSym.modifiedToken(((Token)yyVals[-2+yyTop])), ((Node)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])); }
  break;
case 54:
					// line 219 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.scalarDecl(fSym.modifiedToken(((Token)yyVals[-1+yyTop])), ((Node)yyVals[0+yyTop])); }
  break;
case 55:
					// line 223 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = null; }
  break;
case 56:
					// line 224 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((Node)yyVals[0+yyTop]); }
  break;
case 57:
					// line 225 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.modifiedToken(new Token(-1, "0", Parser.INT_CONST)); }
  break;
case 58:
					// line 229 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.type(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])); }
  break;
case 59:
					// line 230 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.modifiedToken(((Token)yyVals[0+yyTop])); }
  break;
case 60:
					// line 234 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.modifiedToken(((Token)yyVals[0+yyTop])); }
  break;
case 61:
					// line 235 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.modifiedToken(((Token)yyVals[0+yyTop])); }
  break;
case 62:
					// line 236 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.modifiedToken(((Token)yyVals[0+yyTop])); }
  break;
case 63:
					// line 237 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.modifiedToken(((Token)yyVals[0+yyTop])); }
  break;
case 64:
					// line 238 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.modifiedToken(((Token)yyVals[0+yyTop])); }
  break;
case 65:
					// line 239 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.modifiedToken(((Token)yyVals[0+yyTop])); }
  break;
case 66:
					// line 243 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.list(((Node)yyVals[0+yyTop])); }
  break;
case 67:
					// line 244 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((FirList)yyVals[-2+yyTop]).addedLast(fSym.block(((Token)yyVals[-1+yyTop]), ((FirList)yyVals[0+yyTop]))); }
  break;
case 68:
					// line 249 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.block(((Token)yyVals[-1+yyTop]), ((FirList)yyVals[0+yyTop])); }
  break;
case 69:
					// line 253 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = null; }
  break;
case 70:
					// line 254 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((Token)yyVals[0+yyTop]); }
  break;
case 71:
					// line 258 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = null; }
  break;
case 72:
					// line 259 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.modifiedToken(((Token)yyVals[-1+yyTop])); }
  break;
case 73:
					// line 263 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.list(((Node)yyVals[0+yyTop])); }
  break;
case 74:
					// line 264 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((FirList)yyVals[-1+yyTop]); }
  break;
case 75:
					// line 265 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((FirList)yyVals[-2+yyTop]).addedLast(((Node)yyVals[0+yyTop])); }
  break;
case 76:
					// line 268 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.arrayDecl(fSym.modifiedToken(((Token)yyVals[-1+yyTop])), ((Node)yyVals[0+yyTop]), null); }
  break;
case 77:
					// line 269 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.scalarDecl(fSym.modifiedToken(((Token)yyVals[0+yyTop])), null);;   }
  break;
case 78:
					// line 273 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((FirList)yyVals[0+yyTop]); }
  break;
case 79:
					// line 277 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.list(fSym.modifiedToken(((Token)yyVals[0+yyTop]))); }
  break;
case 80:
					// line 278 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((FirList)yyVals[-2+yyTop]).addedLast(fSym.modifiedToken(((Token)yyVals[0+yyTop]))); }
  break;
case 81:
					// line 282 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((FirList)yyVals[0+yyTop]); }
  break;
case 82:
					// line 286 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.list(((FirList)yyVals[0+yyTop])); }
  break;
case 83:
					// line 287 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((FirList)yyVals[-2+yyTop]).addedLast(((FirList)yyVals[0+yyTop])); }
  break;
case 84:
					// line 291 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((FirList)yyVals[-1+yyTop]); }
  break;
case 85:
					// line 295 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.list(((Node)yyVals[0+yyTop])); }
  break;
case 86:
					// line 296 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((FirList)yyVals[-2+yyTop]).addedLast(((Node)yyVals[0+yyTop])); }
  break;
case 87:
					// line 300 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.list(((Node)yyVals[0+yyTop])); }
  break;
case 88:
					// line 301 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((FirList)yyVals[-1+yyTop]).addedLast(((Node)yyVals[0+yyTop])); }
  break;
case 89:
					// line 302 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((FirList)yyVals[-2+yyTop]).addedLast(((Node)yyVals[0+yyTop])); }
  break;
case 90:
					// line 305 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.dataSeq(((FirList)yyVals[-3+yyTop]), ((FirList)yyVals[-1+yyTop])); }
  break;
case 91:
					// line 309 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.list(((Node)yyVals[0+yyTop])); }
  break;
case 92:
					// line 310 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((FirList)yyVals[-2+yyTop]).addedLast(((Node)yyVals[0+yyTop])); }
  break;
case 93:
					// line 314 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.dataVal(null, ((Node)yyVals[0+yyTop])); }
  break;
case 94:
					// line 315 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.dataVal(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])); }
  break;
case 95:
					// line 319 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.value('+', ((Node)yyVals[0+yyTop])); }
  break;
case 96:
					// line 320 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.value('+', ((Node)yyVals[0+yyTop])); }
  break;
case 97:
					// line 321 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.value('-', ((Node)yyVals[0+yyTop])); }
  break;
case 98:
					// line 325 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.modifiedToken(((Token)yyVals[0+yyTop])); }
  break;
case 99:
					// line 326 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((Token)yyVals[0+yyTop]); }
  break;
case 100:
					// line 327 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((Node)yyVals[0+yyTop]); }
  break;
case 101:
					// line 331 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.list(((Token)yyVals[0+yyTop])); }
  break;
case 102:
					// line 332 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((FirList)yyVals[-2+yyTop]).addedLast(((Token)yyVals[0+yyTop])); }
  break;
case 103:
					// line 336 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.modifiedToken(((Token)yyVals[0+yyTop])); }
  break;
case 104:
					// line 337 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((Token)yyVals[0+yyTop]); }
  break;
case 105:
					// line 341 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.list(((Node)yyVals[0+yyTop])); }
  break;
case 106:
					// line 342 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((FirList)yyVals[-2+yyTop]).addedLast(((Node)yyVals[0+yyTop])); }
  break;
case 107:
					// line 346 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.constItem(fSym.modifiedToken(((Token)yyVals[-2+yyTop])), ((Node)yyVals[0+yyTop])); }
  break;
case 108:
					// line 350 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.list(((Node)yyVals[0+yyTop])); }
  break;
case 109:
					// line 351 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((FirList)yyVals[-2+yyTop]).addedLast(((Node)yyVals[0+yyTop])); }
  break;
case 110:
					// line 355 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.dataVarOne(((Node)yyVals[0+yyTop])); }
  break;
case 111:
					// line 356 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.dataVarDoList(((FirList)yyVals[-3+yyTop]), ((Node)yyVals[-1+yyTop])); }
  break;
case 112:
					// line 359 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((FirList)yyVals[-1+yyTop]); }
  break;
case 113:
					// line 362 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.list(((Node)yyVals[0+yyTop])); }
  break;
case 114:
					// line 363 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((FirList)yyVals[-2+yyTop]).addedLast(((Node)yyVals[0+yyTop])); }
  break;
case 115:
					// line 365 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.dim(null, ((Node)yyVals[0+yyTop])); }
  break;
case 116:
					// line 366 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.dim(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])); }
  break;
case 117:
					// line 369 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = null; }
  break;
case 118:
					// line 370 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((Node)yyVals[0+yyTop]); }
  break;
case 119:
					// line 372 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.list(((Node)yyVals[0+yyTop])); }
  break;
case 120:
					// line 373 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((FirList)yyVals[-2+yyTop]).addedLast(((Node)yyVals[0+yyTop])); }
  break;
case 121:
					// line 378 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.impItem(((Token)yyVals[-4+yyTop]), ((Node)yyVals[-3+yyTop]), ((FirList)yyVals[-1+yyTop])); }
  break;
case 122:
					// line 379 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.impItem(((Token)yyVals[0+yyTop]), null, null); }
  break;
case 123:
					// line 384 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.list(((Node)yyVals[0+yyTop])); }
  break;
case 124:
					// line 385 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((FirList)yyVals[-2+yyTop]).addedLast(((Node)yyVals[0+yyTop])); }
  break;
case 125:
					// line 389 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.letterGroup(fSym.modifiedToken(((Token)yyVals[0+yyTop])), null); }
  break;
case 126:
					// line 390 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.letterGroup(fSym.modifiedToken(((Token)yyVals[-2+yyTop])),
                                                      fSym.modifiedToken(((Token)yyVals[0+yyTop]))); }
  break;
case 127:
					// line 395 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((FStmt)yyVals[0+yyTop]); }
  break;
case 128:
					// line 396 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.doLabeled(((Token)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])); }
  break;
case 129:
					// line 397 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.doLabeled(((Token)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])); }
  break;
case 130:
					// line 398 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.doUnLabeled(((Node)yyVals[-2+yyTop]), ((FirList)yyVals[0+yyTop])); }
  break;
case 131:
					// line 400 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.ifStmt(((Node)yyVals[-2+yyTop]), ((FStmt)yyVals[0+yyTop])); }
  break;
case 132:
					// line 402 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.blockIfStmt(((Node)yyVals[-3+yyTop]), ((FirList)yyVals[-2+yyTop]), ((FirList)yyVals[-1+yyTop])); }
  break;
case 133:
					// line 403 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.format(((Token)yyVals[0+yyTop])); }
  break;
case 134:
					// line 407 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.list(fHir.completeStmt(((Token)yyVals[-1+yyTop]), null)); }
  break;
case 135:
					// line 409 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((FirList)yyVals[0+yyTop]).addedFirst(fHir.completeStmt(((Token)yyVals[-3+yyTop]), ((FStmt)yyVals[-2+yyTop]))); }
  break;
case 136:
					// line 412 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.list(); }
  break;
case 137:
					// line 414 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((FirList)yyVals[-3+yyTop]).addedLast(fHir.completeStmt(((Token)yyVals[-2+yyTop]), ((FStmt)yyVals[-1+yyTop]))); }
  break;
case 138:
					// line 418 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.blockIfPart(((Node)yyVals[-4+yyTop]), ((FirList)yyVals[0+yyTop])); }
  break;
case 139:
					// line 421 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.list(); }
  break;
case 140:
					// line 423 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((FirList)yyVals[-7+yyTop]).addedLast(fHir.elseIfPart(((Node)yyVals[-4+yyTop]), ((FirList)yyVals[0+yyTop]))); }
  break;
case 141:
					// line 426 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = null; }
  break;
case 142:
					// line 427 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((FirList)yyVals[0+yyTop]); }
  break;
case 143:
					// line 431 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.doSpec(fSym.modifiedToken(((Token)yyVals[-5+yyTop])), ((Node)yyVals[-3+yyTop]), ((Node)yyVals[-1+yyTop]), ((Node)yyVals[0+yyTop])); }
  break;
case 144:
					// line 434 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = null; }
  break;
case 145:
					// line 435 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((Node)yyVals[0+yyTop]); }
  break;
case 146:
					// line 439 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.assignOrFunc(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])); }
  break;
case 147:
					// line 440 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.assignLabel(((Token)yyVals[-2+yyTop]), fSym.modifiedToken(((Token)yyVals[0+yyTop]))); }
  break;
case 148:
					// line 441 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.continueStmt(); }
  break;
case 149:
					// line 442 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.gotoStmt(((Token)yyVals[0+yyTop])); }
  break;
case 150:
					// line 445 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.aGoto(fSym.modifiedToken(((Token)yyVals[-1+yyTop])), ((FirList)yyVals[0+yyTop])); }
  break;
case 151:
					// line 447 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.aGoto(fSym.modifiedToken(((Token)yyVals[-2+yyTop])), ((FirList)yyVals[0+yyTop])); }
  break;
case 152:
					// line 449 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.cGoto(((FirList)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])); }
  break;
case 153:
					// line 450 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.cGoto(((FirList)yyVals[-3+yyTop]), ((Node)yyVals[0+yyTop])); }
  break;
case 154:
					// line 452 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.aIf(((Node)yyVals[-6+yyTop]), ((Token)yyVals[-4+yyTop]), ((Token)yyVals[-2+yyTop]), ((Token)yyVals[0+yyTop])); }
  break;
case 155:
					// line 453 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.call(fSym.modifiedToken(((Token)yyVals[-1+yyTop])), ((FirList)yyVals[0+yyTop])); }
  break;
case 156:
					// line 454 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.returnStmt(((Node)yyVals[0+yyTop])); }
  break;
case 157:
					// line 455 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.pause(null); }
  break;
case 158:
					// line 456 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.pause(((Node)yyVals[0+yyTop])); }
  break;
case 159:
					// line 457 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.stop(null); }
  break;
case 160:
					// line 458 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.stop(((Node)yyVals[0+yyTop])); }
  break;
case 161:
					// line 459 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((FStmt)yyVals[0+yyTop]); }
  break;
case 162:
					// line 462 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = null; }
  break;
case 163:
					// line 463 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = null; }
  break;
case 164:
					// line 464 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((FirList)yyVals[-1+yyTop]); }
  break;
case 165:
					// line 468 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.list(((Node)yyVals[0+yyTop])); }
  break;
case 166:
					// line 469 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((FirList)yyVals[-2+yyTop]).addedLast(((Node)yyVals[0+yyTop])); }
  break;
case 167:
					// line 470 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = null; }
  break;
case 168:
					// line 474 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((Node)yyVals[0+yyTop]); }
  break;
case 169:
					// line 475 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.argLabel(((Token)yyVals[0+yyTop])); }
  break;
case 170:
					// line 479 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = null; }
  break;
case 171:
					// line 480 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((FirList)yyVals[-1+yyTop]); }
  break;
case 172:
					// line 484 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.list(((Token)yyVals[0+yyTop])); }
  break;
case 173:
					// line 485 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((FirList)yyVals[-2+yyTop]).addedLast(((Token)yyVals[0+yyTop])); }
  break;
case 174:
					// line 488 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.label(fSym.modifiedToken(((Token)yyVals[0+yyTop]))); }
  break;
case 175:
					// line 492 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.printStmt(((Node)yyVals[-1+yyTop]), ((FirList)yyVals[0+yyTop])); }
  break;
case 176:
					// line 493 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.writeStmt(((FirList)yyVals[-2+yyTop]), ((FirList)yyVals[0+yyTop])); }
  break;
case 177:
					// line 494 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.readStmt(((FirList)yyVals[-2+yyTop]), ((FirList)yyVals[0+yyTop]));  }
  break;
case 178:
					// line 495 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.readFStmt(((Node)yyVals[-1+yyTop]), ((FirList)yyVals[0+yyTop])); }
  break;
case 179:
					// line 496 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.openStmt(((FirList)yyVals[-1+yyTop]));      }
  break;
case 180:
					// line 497 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.closeStmt(((FirList)yyVals[-1+yyTop]));     }
  break;
case 181:
					// line 498 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.backspace(((FirList)yyVals[-1+yyTop]));     }
  break;
case 182:
					// line 499 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.backspaceF(((Node)yyVals[0+yyTop]));    }
  break;
case 183:
					// line 500 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.endfile(((FirList)yyVals[-1+yyTop]));       }
  break;
case 184:
					// line 501 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.endfileF(((Node)yyVals[0+yyTop]));      }
  break;
case 185:
					// line 502 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.rewind(((FirList)yyVals[-1+yyTop]));        }
  break;
case 186:
					// line 503 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.rewindF(((Node)yyVals[0+yyTop]));       }
  break;
case 187:
					// line 504 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.inquire(((FirList)yyVals[-1+yyTop]));       }
  break;
case 188:
					// line 508 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.list(((Node)yyVals[0+yyTop])); }
  break;
case 189:
					// line 509 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((FirList)yyVals[-2+yyTop]).addedLast(((Node)yyVals[0+yyTop])); }
  break;
case 190:
					// line 513 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.ioClause(null, ((Node)yyVals[0+yyTop])); }
  break;
case 191:
					// line 514 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.ioClause(null, null); }
  break;
case 192:
					// line 517 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.ioClause(fSym.modifiedToken(((Token)yyVals[-2+yyTop])), ((Node)yyVals[0+yyTop])); }
  break;
case 193:
					// line 518 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.ioClause(fSym.modifiedToken(((Token)yyVals[-2+yyTop])), null); }
  break;
case 194:
					// line 522 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = null; }
  break;
case 195:
					// line 523 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.modifiedToken(((Token)yyVals[0+yyTop])); }
  break;
case 196:
					// line 524 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.modifiedToken(((Token)yyVals[0+yyTop])); }
  break;
case 197:
					// line 525 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.modifiedToken(((Token)yyVals[0+yyTop])); }
  break;
case 198:
					// line 531 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = null; }
  break;
case 199:
					// line 532 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((FirList)yyVals[0+yyTop]); }
  break;
case 200:
					// line 536 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.list(((Node)yyVals[0+yyTop])); }
  break;
case 201:
					// line 537 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((FirList)yyVals[-2+yyTop]).addedLast(((Node)yyVals[0+yyTop])); }
  break;
case 202:
					// line 541 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = null; }
  break;
case 203:
					// line 542 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((FirList)yyVals[0+yyTop]);   }
  break;
case 204:
					// line 546 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.ioItemExpr(((Node)yyVals[0+yyTop]));   }
  break;
case 205:
					// line 547 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.ioItemDoList(((FirList)yyVals[-1+yyTop])); }
  break;
case 206:
					// line 551 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.list(((Node)yyVals[0+yyTop])); }
  break;
case 207:
					// line 552 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((FirList)yyVals[0+yyTop]).addedFirst(((Node)yyVals[-2+yyTop])); }
  break;
case 208:
					// line 556 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((Node)yyVals[0+yyTop]); }
  break;
case 209:
					// line 557 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((Token)yyVals[0+yyTop]); }
  break;
case 210:
					// line 558 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((Node)yyVals[0+yyTop]); }
  break;
case 211:
					// line 559 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.enclosed(((Node)yyVals[-1+yyTop])); }
  break;
case 212:
					// line 560 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.exprUnary(HIR.OP_NEG, ((Node)yyVals[0+yyTop])); }
  break;
case 213:
					// line 561 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((Node)yyVals[0+yyTop]); }
  break;
case 214:
					// line 562 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.exprPower( ((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])); }
  break;
case 215:
					// line 563 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.exprBinary(HIR.OP_MULT, ((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])); }
  break;
case 216:
					// line 564 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.exprBinary(HIR.OP_DIV, ((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])); }
  break;
case 217:
					// line 565 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.exprBinary(HIR.OP_ADD, ((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])); }
  break;
case 218:
					// line 566 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.exprBinary(HIR.OP_SUB, ((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])); }
  break;
case 219:
					// line 567 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.exprCat(((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])); }
  break;
case 220:
					// line 568 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.exprBinary(HIR.OP_CMP_GT, ((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])); }
  break;
case 221:
					// line 569 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.exprBinary(HIR.OP_CMP_EQ, ((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])); }
  break;
case 222:
					// line 570 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.exprBinary(HIR.OP_CMP_LT, ((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])); }
  break;
case 223:
					// line 571 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.exprBinary(HIR.OP_CMP_NE, ((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])); }
  break;
case 224:
					// line 572 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.exprBinary(HIR.OP_CMP_LE, ((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])); }
  break;
case 225:
					// line 573 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.exprBinary(HIR.OP_CMP_GE, ((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])); }
  break;
case 226:
					// line 574 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.exprUnary(HIR.OP_NOT, ((Node)yyVals[0+yyTop])); }
  break;
case 227:
					// line 575 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.exprBinary(HIR.OP_AND, ((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])); }
  break;
case 228:
					// line 576 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.exprBinary(HIR.OP_OR, ((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])); }
  break;
case 229:
					// line 577 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.exprUnary(HIR.OP_NOT, 
                                          fHir.exprBinary(HIR.OP_XOR, ((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop]))); }
  break;
case 230:
					// line 579 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.exprBinary(HIR.OP_XOR, ((Node)yyVals[-2+yyTop]), ((Node)yyVals[0+yyTop])); }
  break;
case 231:
					// line 583 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.modifiedToken(((Token)yyVals[0+yyTop])); }
  break;
case 232:
					// line 584 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.leftNameSubstr(fSym.modifiedToken(((Token)yyVals[-1+yyTop])), null, ((Pair)yyVals[0+yyTop])); }
  break;
case 233:
					// line 585 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.leftNameSubstr(fSym.modifiedToken(((Token)yyVals[-1+yyTop])), null, ((Pair)yyVals[0+yyTop])); }
  break;
case 234:
					// line 586 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.leftName(fSym.modifiedToken(((Token)yyVals[-3+yyTop])), ((FirList)yyVals[-1+yyTop])); }
  break;
case 235:
					// line 588 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.leftNameSubstr(fSym.modifiedToken(((Token)yyVals[-4+yyTop])), ((FirList)yyVals[-2+yyTop]), ((Pair)yyVals[0+yyTop])); }
  break;
case 236:
					// line 591 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.substring(((Node)yyVals[-3+yyTop]), ((Node)yyVals[-1+yyTop])); }
  break;
case 237:
					// line 594 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = null; }
  break;
case 238:
					// line 595 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((Node)yyVals[0+yyTop]); }
  break;
case 239:
					// line 599 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = ((Token)yyVals[0+yyTop]); }
  break;
case 240:
					// line 600 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.modifiedToken(((Token)yyVals[0+yyTop]));  }
  break;
case 241:
					// line 601 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.modifiedToken(((Token)yyVals[0+yyTop]));  }
  break;
case 242:
					// line 602 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.modifiedToken(((Token)yyVals[0+yyTop]));  }
  break;
case 243:
					// line 606 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.modifiedToken(((Token)yyVals[0+yyTop]));  }
  break;
case 244:
					// line 607 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.modifiedToken(((Token)yyVals[0+yyTop]));  }
  break;
case 245:
					// line 608 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fSym.modifiedToken(((Token)yyVals[0+yyTop]));  }
  break;
case 246:
					// line 618 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.constComplex(((Node)yyVals[-3+yyTop]), ((Token)yyVals[-1+yyTop])); }
  break;
case 247:
					// line 620 "../coins-0.10.2/src/coins/ffront/f77k.jay"
  { yyVal = fHir.constComplex(((Node)yyVals[-4+yyTop]), fHir.exprUnary(HIR.OP_NEG, ((Token)yyVals[-1+yyTop]))); }
  break;
					// line 1317 "-"
        }
        yyTop -= YyLenClass.yyLen[yyN];
        yyState = yyStates[yyTop];
        int yyM = YyLhsClass.yyLhs[yyN];
        if (yyState == 0 && yyM == 0) {
//t          if (yydebug != null) yydebug.shift(0, yyFinal);
          yyState = yyFinal;
          if (yyToken < 0) {
            yyToken = yyLex.advance() ? yyLex.token() : 0;
//t            if (yydebug != null)
//t               yydebug.lex(yyState, yyToken,yyname(yyToken), yyLex.value());
          }
          if (yyToken == 0) {
//t            if (yydebug != null) yydebug.accept(yyVal);
            return yyVal;
          }
          continue yyLoop;
        }
        if ((yyN = YyGindexClass.yyGindex[yyM]) != 0 && (yyN += yyState) >= 0
            && yyN < YyTableClass.yyTable.length && YyCheckClass.yyCheck[yyN] == yyState)
          yyState = YyTableClass.yyTable[yyN];
        else
          yyState = YyDgotoClass.yyDgoto[yyM];
//t        if (yydebug != null) yydebug.shift(yyStates[yyTop], yyState);
	 continue yyLoop;
      }
    }
  }

  protected static final class YyLhsClass {

    public static final short yyLhs [] = {              -1,
          0,    0,   11,   11,   11,   11,   11,   53,   53,   14,
         15,   19,   19,   19,   10,   10,   12,   20,   20,   21,
         22,   23,   24,    1,    1,   25,   25,   54,   54,   55,
         56,   56,   56,   57,   57,    2,    2,   26,   26,   27,
         27,   27,   27,   27,   27,   27,   58,   58,   28,   28,
         59,   59,   29,   29,   30,   30,   30,   13,   13,    3,
          3,    3,    3,    3,    3,   60,   60,   31,    6,    6,
          7,    7,   61,   61,   61,   32,   32,   62,   63,   63,
         64,   65,   65,   66,   67,   67,   68,   68,   68,   33,
         69,   69,   34,   34,   35,   35,   35,   36,   36,   36,
         70,   70,    8,    8,   71,   71,   37,   72,   72,   38,
         38,   39,   73,   73,   40,   40,   41,   41,   78,   78,
         42,   42,   79,   79,   43,   43,   16,   16,   16,   16,
         16,   16,   16,   80,   80,   81,   81,   44,   82,   82,
         83,   83,   45,   46,   46,   17,   17,   17,   17,   17,
         17,   17,   17,   17,   17,   17,   17,   17,   17,   17,
         17,   84,   84,   84,   85,   85,   85,   47,   47,   86,
         86,   87,   87,    9,   18,   18,   18,   18,   18,   18,
         18,   18,   18,   18,   18,   18,   18,   88,   88,   48,
         48,   48,   48,   49,   49,   49,   49,   75,   75,   76,
         76,   74,   74,   50,   50,   77,   77,   89,   89,   89,
         89,   89,   89,   89,   89,   89,   89,   89,   89,   89,
         89,   89,   89,   89,   89,   89,   89,   89,   89,   89,
         91,   91,   91,   91,   91,   52,   90,   90,    4,    4,
          4,    4,    5,    5,    5,   51,   51,
    };
  } /* End of class YyLhsClass */

  protected static final class YyLenClass {

    public static final short yyLen [] = {           2,
          1,    2,    2,    1,    2,    2,    1,    1,    2,    3,
          3,    1,    1,    1,    1,    1,    4,    6,    5,    5,
          3,    3,    4,    0,    1,    0,    4,    2,    3,    1,
          0,    2,    3,    1,    3,    1,    1,    1,    1,    2,
          2,    2,    2,    2,    2,    4,    0,    1,    2,    2,
          1,    3,    3,    2,    0,    2,    4,    2,    1,    1,
          1,    1,    1,    1,    1,    1,    3,    2,    0,    1,
          1,    3,    1,    2,    3,    2,    1,    1,    1,    3,
          1,    1,    3,    3,    1,    3,    1,    2,    3,    4,
          1,    3,    1,    3,    1,    2,    2,    1,    1,    1,
          1,    3,    1,    1,    1,    3,    3,    1,    3,    1,
          5,    3,    1,    3,    1,    3,    1,    1,    1,    3,
          5,    1,    1,    3,    1,    3,    1,    3,    4,    4,
          5,    4,    2,    2,    4,    0,    4,    7,    0,    8,
          0,    3,    6,    0,    2,    4,    4,    1,    2,    3,
          4,    5,    6,    9,    3,    2,    1,    2,    1,    2,
          1,    0,    2,    3,    1,    3,    0,    1,    2,    0,
          3,    1,    3,    1,    3,    5,    5,    3,    4,    4,
          4,    2,    4,    2,    4,    2,    4,    1,    3,    1,
          1,    3,    3,    1,    1,    1,    1,    0,    1,    1,
          3,    0,    2,    1,    3,    1,    3,    1,    1,    1,
          3,    2,    2,    3,    3,    3,    3,    3,    3,    3,
          3,    3,    3,    3,    3,    2,    3,    3,    3,    3,
          1,    2,    2,    4,    5,    5,    0,    1,    1,    1,
          1,    1,    1,    1,    1,    5,    6,
    };
  } /* End class YyLenClass */

  protected static final class YyDefRedClass {

    public static final short yyDefRed [] = {            0,
         15,   16,    0,    0,    1,    0,    8,    0,    0,    0,
          7,   26,    4,    2,   60,   61,   62,   63,   64,   65,
          0,    0,    0,    0,    0,    0,    0,    0,    0,  148,
          0,   59,    0,    0,    0,    0,    0,    0,    0,    0,
          0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
          0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
         14,  127,  161,    0,   12,   13,   38,   39,  139,    0,
          3,    9,    5,    6,    0,    0,    0,    0,    0,    0,
          0,  174,    0,   25,    0,    0,    0,   71,    0,    0,
         70,   66,    0,    0,   87,  108,    0,    0,  110,    0,
          0,    0,   10,  197,  196,  195,  194,    0,  184,    0,
          0,    0,   82,   79,   49,    0,  133,    0,  149,    0,
          0,    0,  119,    0,    0,   50,  244,    0,  243,  245,
        240,  241,    0,    0,    0,    0,  209,  239,  210,    0,
        208,    0,    0,    0,    0,    0,  156,  103,  104,  101,
         45,    0,    0,    0,    0,    0,    0,    0,  182,    0,
        186,    0,   58,    0,    0,   51,    0,   11,    0,    0,
          0,   22,    0,  105,    0,    0,  232,    0,  233,    0,
          0,    0,    0,   23,    0,  155,    0,  191,  188,    0,
          0,    0,    0,   73,    0,    0,    0,    0,   88,    0,
          0,    0,    0,  128,    0,    0,    0,   21,    0,   85,
          0,    0,    0,    0,    0,    0,  150,  172,    0,    0,
          0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
          0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
          0,    0,    0,    0,  175,   17,    0,  178,    0,    0,
          0,    0,    0,    0,    0,    0,    0,    0,   54,    0,
          0,    0,    0,    0,    0,    0,    0,    0,   46,    0,
          0,  165,    0,    0,    0,    0,    0,  147,  163,    0,
          0,    0,    0,  180,   72,   76,    0,    0,    0,   89,
        109,  242,   98,    0,    0,    0,   99,   91,   93,    0,
        100,    0,    0,  129,    0,  130,  183,   36,   37,   32,
         34,    0,    0,   84,   83,   80,   28,    0,    0,   19,
        151,    0,    0,    0,    0,  120,    0,  206,    0,    0,
          0,  211,    0,    0,    0,    0,    0,    0,    0,    0,
          0,    0,    0,    0,    0,    0,    0,    0,  200,    0,
          0,    0,  102,   20,    0,  179,  187,  181,  185,    0,
        117,  113,  115,    0,    0,   53,    0,   52,  136,    0,
        132,   27,    0,  106,    0,  169,    0,    0,    0,    0,
        131,  164,  193,    0,  189,   75,    0,   96,   97,    0,
          0,   90,    0,  134,    0,    0,   33,   86,   29,  171,
        173,    0,    0,    0,  123,    0,    0,  205,    0,    0,
          0,  177,    0,  176,   57,    0,  112,    0,   18,    0,
          0,    0,  166,  235,    0,  136,  111,   94,   95,   92,
          0,    0,   35,    0,    0,    0,  121,    0,    0,    0,
        207,    0,  246,  201,  114,  116,    0,    0,    0,  236,
          0,    0,    0,  143,  135,  126,  124,    0,    0,  247,
          0,    0,    0,    0,  137,  136,  154,    0,
    };
  } /* End of class YyDefRedClass */

  protected static final class YyDgotoClass {

    public static final short yyDgoto [] = {             3,
         85,  311,   59,  137,  138,   90,  149,  150,  218,   70,
          5,    6,  170,    7,    8,   61,   62,   63,   64,    9,
         10,   65,   11,   12,   75,   66,   67,   68,  166,  163,
         92,  194,   95,  298,  299,  300,  174,   96,  260,  362,
        363,  123,  405,   69,  328,  454,  272,  189,  109,  226,
        139,  179,   13,  214,  318,  208,  312,  151,  167,   93,
        195,  115,  116,  126,  112,  113,  209,   97,  302,  152,
        175,   98,  364,  245,  412,  413,  441,  124,  406,  306,
        420,  169,  265,  186,  273,  217,  219,  190,  191,  270,
        141,
    };
  } /* End of class YyDgotoClass */

  protected static final class YySindexClass {

    public static final short yySindex [] = {         -192,
          0,    0, -192, 1243,    0, -192,    0, -192, -192, -192,
          0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
         55, -159,  118,  125, -177, -117,  -95,  137,  -36,    0,
         16,    0, -123,  -85,  113,  -78,  163,   10,  -99,   18,
       -177,   49,  224,   54,   10, 1013,  120,   59,  369, 1013,
        -41, 1013,   65,  291,  296,  301,  512,  804,  310, -194,
          0,    0,    0,   89,    0,    0,    0,    0,    0, 1293,
          0,    0,    0,    0, -192,   96,  329,  336,  325, 1013,
       1013,    0,   85,    0,  135,  358,  581,    0, -117,  146,
          0,    0,  -36,   16,    0,    0,   90,  104,    0,  347,
         21,  154,    0,    0,    0,    0,    0,  581,    0,  380,
       -159,  386,    0,    0,    0,  401,    0,  411,    0,   44,
       -177,  310,    0,  417,  401,    0,    0,  329,    0,    0,
          0,    0, 1013, 1013, 1013, 1026,    0,    0,    0,  678,
          0,  419,  204,  581,  419,  678,    0,    0,    0,    0,
          0,  428,  678,  380,  581,  581,  581,  581,    0,  581,
          0, 1046,    0,  112,  212,    0,  437,    0,  -34,  228,
       1472,    0,  435,    0,  126, 1013,    0,  862,    0, 1013,
        313,  603,  241,    0,  777,    0,   28,    0,    0,  132,
        678,  461,  481,    0,  478,  146,  483,   16,    0,   16,
       1198, 1013,  279,    0, -192,  134,   30,    0,  197,    0,
        163,  288,    7,  287,  525, -177,    0,    0,  229,  538,
         54,  554,  265,  265, 1057,  541,  618, 1013, 1013, 1013,
       1013, 1013, 1013, 1013, 1013, 1013, 1013, 1013, 1013, 1013,
       1013, 1013, 1013, 1026,    0,    0,  280,    0,  -41,  334,
        321,  322,  352,  371,  373,  873,  678,  909,    0,  310,
        411,  228,  355,  595,  362,  391, 1013,   96,    0,  599,
       -177,    0,  388,  678,  678, -177, 1422,    0,    0,  418,
        678,  920,  581,    0,    0,    0,  146,  478,   34,    0,
          0,    0,    0,  570,  570, 1026,    0,    0,    0,  620,
          0,  423,  205,    0, 1336,    0,    0,    0,    0,    0,
          0,  506, -159,    0,    0,    0,    0,  623,  401,    0,
          0,  514, -177,  969,  405,    0,   35,    0,  624,  626,
        -17,    0,  554,  722,  702,  702,  -24,  -24,  -24,  -24,
        -24,  -11,  265,  -18,  -27,  -27,  265,  265,    0,  627,
        678, 1026,    0,    0, 1026,    0,    0,    0,    0,  629,
          0,    0,    0,  515,  664,    0,  409,    0,    0, 1013,
          0,    0,  678,    0, 1013,    0,  862,  329,  631,  412,
          0,    0,    0,  678,    0,    0,  637,    0,    0, 1198,
       1198,    0, 1013,    0,  426,   52,    0,    0,    0,    0,
          0, 1013,  678,  638,    0,  516, 1070,    0, -147,  654,
       1026,    0,  627,    0,    0,  909,    0,  909,    0, -192,
        651,  656,    0,    0, -177,    0,    0,    0,    0,    0,
        433, -192,    0,  678,  439,  405,    0, 1013,  654,  657,
          0,  661,    0,    0,    0,    0,  678, 1379,  392,    0,
        660, -192, 1013,    0,    0,    0,    0,  661, 1057,    0,
        444,  448, -177,  678,    0,    0,    0, -192,
    };
  } /* End of class YySindexClass */

  protected static final class YyRindexClass {

    public static final short yyRindex [] = {            0,
          0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
          0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
          0,    0,    0,    0,    0,  449,    0,    0,  453,    0,
          0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
          0,    0,    0,    0,    0,  451,    0,    0,    0,  452,
        454,  455,    0,    0,    0,    0,    0,    0, -181,    0,
          0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
          0,    0,    0,    0,    0,    0,    0,  413,    0,    0,
          0,    0,    0,    0,    0,  460,    0,    0,  670,    0,
          0,    0,  466,    0,    0,    0,  471,    0,    0,    0,
          0,    0,    0,    0,    0,    0,    0,    0,    0,  482,
          0,  484,    0,    0,    0,  486,    0,    0,    0,  491,
          0,   14,    0,  502,  504,    0,    0,  571,    0,    0,
          0,    0,    0,    0,    0,    0,    0,    0,    0,  508,
          0,  523,    0,    0,  523,  -28,    0,    0,    0,    0,
          0,  524,  526,  482,    0,    0,    0,    0,    0,    0,
          0,    0,    0,  -30,    0,    0,  527,    0,  497,    0,
          0,    0,    0,    0,    0,  720,    0,   42,    0,    0,
          0,    0,    0,    0,  748,    0,  243,    0,    0,    0,
        559,    0,    3,    0,   -8,    0,    0,    0,    0,    0,
          0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
          0,    0,    0,    0,  491,    0,    0,    0,    0,    0,
          0,  106,   19,   64,    0,    0,  749,    0,    0,    0,
          0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
          0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
          0,    0,    0,    0,    0,    0,   11,    0,    0,  -30,
          0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
          0,    0,    0,   73,  530,    0,    0,    0,    0,    0,
        561,    0,    0,    0,    0,    0,    5,   -7,    0,    0,
          0,    0,    0,    0,    0,    0,    0,    0,    0,  562,
          0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
          0,    0,    0,    0,    0,    0,    0,    0,  754,    0,
          0,    0,    0,    0,    0,    0,  243,    0,    0,    0,
          0,    0,  408, 1174, 1176, 1211, 1097, 1102, 1113, 1148,
       1163,  758,  167,  116,  256,  361,  186,  218,    0,  532,
         43,  533,    0,    0,  533,    0,    0,    0,    0,    0,
          0,    0,    0,    0,  567,    0,    0,    0,    0,    0,
          0,    0,  576,    0,  759,    0,    0,  546,    0,    0,
          0,    0,    0,  608,    0,    0,    0,    0,    0,    0,
          0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
          0,    0,  537,  612,    0,    0,    0,    0,    0,    0,
          0,    0,  539,    0,    0,    0,    0,    0,    0,  513,
          0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
        -29,    0,    0,  540,    0,    0,    0,    0,  456,    0,
          0,    0,    0,    0,    0,    0,  567,    0,    0,    0,
          0, -160,    0,    0,    0,    0,    0,  456,    0,    0,
          0,    0,    0,  -19,    0,    0,    0, -149,
    };
  } /* End of class YyRindexClass */

  protected static final class YyGindexClass {

    public static final short yyGindex [] = {            0,
        717,  415,    0, -168, -289,    0,  -12,  560,  -25,    1,
        805,    0,  808,  738,    0, -264,  544,    0,    0,    0,
          0,    0,    0,    0,    0,    0,  643,    0,  553,  -79,
          0,  536,  -53,  434,  436, -249,  556, -121,  640,  429,
        416,  628,  414,    0,  -23,    0,  474,  569,  114,  240,
        128,  -75,  115,  592,    0,  700,    0,    0,    0,    0,
        659,    0,   24,    0,    0,  645,    0,    0,    0,    0,
          0,  763,    0,  713,  505,  615,  636,    0,    0,  430,
       -369,    0,    0,    0,  701,  648,  669,   61,  -43,  -45,
          4,
    };
  } /* End of class YyGindexClass */

  protected static final class YyTableClass {

    public static final short yyTable [] = {            83,
          4,  177,  140,    4,  147,   89,  146,  101,  153,  102,
         89,  144,  238,   55,  242,  119,   91,  242,  240,  243,
        241,  145,  243,  242,  240,   79,  241,  409,  243,  238,
        242,  240,  297,  241,   99,  243,  181,  182,   68,   67,
        395,  410,  220,  199,  388,  389,   77,  317,   74,   77,
         56,   74,  177,   55,   56,   94,  452,  122,  213,  213,
        213,  213,  213,  213,  203,  213,  164,  178,  125,    1,
        310,  309,    2,   94,  178,  171,  213,  204,  291,   55,
        196,   82,  167,  216,  259,  167,  204,  215,  282,  222,
        223,  224,  227,  309,   76,  202,  468,   99,   77,  237,
         99,   78,  165,  212,  212,  212,  212,  212,  212,  127,
        212,  129,  130,  168,  210,   55,  168,  439,  257,  442,
         71,  212,   72,   73,   74,  297,  297,  138,  138,   94,
        238,  138,  146,  198,  274,   82,  275,  100,  140,  140,
        429,  281,  140,   84,  290,  226,  226,  200,  458,  226,
        201,  258,  108,  162,  107,  219,  219,   80,  303,  219,
        142,  107,  145,  226,   81,   86,  269,  291,  206,  268,
        159,  161,  284,  219,  307,  283,   87,  283,  103,  304,
        366,  227,  110,  461,  333,  334,  335,  336,  337,  338,
        339,  340,  341,  342,  343,  344,  345,  346,  347,  348,
        351,   99,  111,   99,  247,  305,  214,  214,  214,  214,
        214,  214,  227,  214,  365,  251,  252,  253,  254,  148,
        255,  297,  297,  373,  214,  215,  215,  215,  215,  215,
        215,  117,  215,   55,  144,  238,  319,  314,  384,  127,
        313,  129,  130,  215,  145,  376,  242,  240,  393,  241,
        379,  243,  351,  263,  264,   68,   67,  216,  216,  216,
        216,  216,  216,  121,  216,  387,   77,  114,   74,  324,
        114,   56,  323,   77,   56,  216,   78,  122,  118,  213,
        403,  100,  213,  231,  231,  231,  231,  231,   88,  231,
        308,   77,   99,   88,  327,  217,  217,  401,  217,  217,
        217,  238,  424,  237,  238,  239,  204,   56,  351,  120,
        238,  351,  308,  217,  122,  213,  398,  238,  239,  143,
        352,   68,   67,  283,  212,  154,  421,  212,  301,  422,
        155,  146,   77,  281,   74,  156,  213,  213,  213,  213,
        157,  213,  213,  213,  213,  213,  213,   77,  213,  431,
         78,  162,  168,  276,  242,  240,  173,  241,  434,  243,
        212,  355,  356,  351,  283,  283,  226,  351,  176,  226,
        104,  105,  365,  106,  447,  178,  219,  104,  105,  219,
        106,  212,  212,  212,  212,  180,  212,  212,  212,  212,
        212,  212,  357,  212,  224,  283,  183,  185,  184,  451,
        218,  218,  226,  218,  218,  218,  193,  202,  144,  464,
        107,  358,  219,  359,  283,  351,  283,  205,  218,  207,
        448,  301,  301,  226,  226,  226,  226,  214,  378,  211,
        214,  377,  305,  219,  219,  219,  219,  467,  219,  219,
        219,  219,  219,  219,  212,  219,  215,  227,  227,  215,
        213,  227,  448,  231,  231,  231,  231,  231,  382,  231,
        221,  377,  244,  214,  329,  227,  391,  246,  448,  392,
        231,  249,  261,  231,  242,  240,  453,  241,  216,  243,
        262,  216,  215,  349,  214,  214,  214,  214,  164,  214,
        214,  214,  214,  214,  214,  267,  214,  239,  239,  239,
        239,  278,  239,  215,  215,  215,  215,  285,  215,  215,
        215,  215,  215,  215,  216,  215,  217,  301,  301,  217,
        258,  287,  228,  229,  230,  231,  289,  232,  233,  234,
        235,  236,  237,  238,  239,  216,  216,  216,  216,  100,
        216,  216,  216,  216,  216,  216,  397,  216,  316,  396,
        320,  158,  217,  107,  400,  417,  437,  323,  416,  436,
        231,  231,  231,  231,  216,  231,  231,  231,  231,  231,
        231,  231,  231,  217,  217,  217,  217,  325,  217,  217,
        217,  217,  217,  217,  331,  217,  234,  234,  234,  234,
        234,  349,  234,  238,  349,  242,  240,  354,  241,  190,
        243,  168,  190,  234,  168,   95,  234,  118,   95,  296,
        118,  242,  242,  242,  242,  242,  107,  242,  369,  107,
        136,  218,  188,  134,  218,  135,  104,  105,  242,  106,
        228,  229,  230,  231,  370,  232,  233,  234,  235,  236,
        237,  238,  239,  277,  242,  240,  440,  241,  192,  243,
        444,  192,  125,  371,  372,  125,  375,  218,  332,  242,
        240,  390,  241,  399,  243,  404,  408,  407,  227,  415,
        411,  227,  419,  231,  425,  426,  231,  427,  218,  218,
        218,  218,  435,  218,  218,  218,  218,  218,  218,  432,
        218,  449,  242,  240,  443,  241,  450,  243,  440,  456,
        459,  460,  462,  463,  227,  242,  240,  465,  241,  231,
        243,  466,   24,   69,  157,  237,   24,   47,  159,  242,
        240,  418,  241,  162,  243,  227,  227,  227,  227,   41,
        231,  231,  231,  231,   43,  231,  231,  231,  231,  231,
        231,  231,  231,  242,  240,   31,  241,   42,  243,   78,
        228,  229,  230,  231,  170,  232,  233,  234,  235,  236,
        237,  238,  239,  242,  240,   44,  241,   81,  243,  104,
        105,  158,  106,  239,  239,  239,  239,  237,  239,  239,
        239,  239,  239,  239,  239,  239,  202,   48,  141,  160,
         40,  167,  204,  146,   30,  203,  198,  223,  223,  237,
        152,  223,  199,  153,  142,  192,  234,   14,  353,  234,
        433,   60,  172,  266,  368,  223,  136,  279,  271,  134,
        381,  135,  386,  374,  430,  428,  127,  292,  129,  130,
        293,  242,  286,  446,  242,  131,  132,  127,  128,  129,
        130,  187,  234,  160,  445,  107,  131,  132,  326,  457,
        423,  385,  367,  250,  288,  315,  197,  248,  350,  414,
        330,  455,  321,  234,  234,  234,  234,  242,  234,  234,
        234,  234,  234,  234,  234,  234,  232,  233,  234,  235,
        236,  237,  238,  239,  322,  280,    0,    0,  242,  242,
        242,  242,    0,  242,  242,  242,  242,  242,  242,  242,
        242,  136,  133,  271,  134,    0,  135,    0,    0,    0,
          0,    0,  225,    0,  360,  134,    0,  135,    0,    0,
        228,  229,  230,  231,    0,  232,  233,  234,  235,  236,
        237,  238,  239,    0,    0,  228,  229,  230,  231,    0,
        232,  233,  234,  235,  236,  237,  238,  239,  136,    0,
        361,  134,    0,  135,    0,    0,    0,    0,    0,  136,
          0,  383,  134,    0,  135,    0,    0,    0,  228,  229,
        230,  231,    0,  232,  233,  234,  235,  236,  237,  238,
        239,  228,  229,  230,  231,    0,  232,  233,  234,  235,
        236,  237,  238,  239,    0,  228,  229,  230,  231,    0,
        232,  233,  234,  235,  236,  237,  238,  239,  136,    0,
          0,  134,  402,  135,    0,    0,    0,    0,  223,  228,
        229,  223,    0,    0,  232,  233,  234,  235,  236,  237,
        238,  239,    0,  127,  128,  129,  130,   78,    0,  228,
          0,    0,  131,  132,  232,  233,  234,  235,  236,  237,
        238,  239,  136,    0,  223,  134,    0,  135,    0,    0,
          0,  104,  105,    0,  106,  225,    0,    0,  134,    0,
        135,    0,    0,    0,    0,  223,  223,  223,  223,    0,
        223,  223,  223,  223,  223,  256,    0,    0,  134,    0,
        135,    0,    0,    0,    0,    0,  225,    0,  133,  134,
          0,  135,    0,    0,    0,    0,    0,    0,    0,  225,
          0,    0,  134,    0,  438,    0,    0,    0,  127,  128,
        129,  130,   78,    0,    0,    0,    0,  131,  132,  127,
        128,  129,  130,   78,    0,    0,  221,  221,  131,  132,
        221,  222,  222,    0,    0,  222,    0,    0,    0,    0,
          0,    0,  220,  220,  221,    0,  220,    0,    0,  222,
          0,    0,    0,    0,    0,  127,  128,  129,  130,   78,
        220,    0,    0,    0,  131,  132,  127,  128,  129,  130,
         78,    0,    0,  133,    0,  131,  132,  224,  224,    0,
          0,  224,    0,    0,  133,    0,    0,    0,    0,    0,
          0,    0,  225,  225,    0,  224,  225,    0,    0,    0,
          0,    0,    0,  228,  228,  230,  230,  228,    0,  230,
        225,    0,    0,    0,    0,  127,  128,  129,  130,   78,
        133,  228,    0,  230,  131,  132,    0,  296,    0,    0,
        294,  133,  295,    0,    0,    0,    0,    0,    0,    0,
        229,  229,    0,    0,  229,    0,    0,    0,    0,    0,
          0,    0,    0,    0,    0,    0,    0,    0,  229,  127,
        128,  129,  130,   78,    0,    0,    0,    0,  131,  132,
          0,    0,  127,  128,  129,  130,   78,    0,    0,    0,
        133,  131,  132,    0,    0,    0,    0,    0,    0,    0,
          0,    0,  127,  128,  129,  130,   78,    0,    0,    0,
          0,  131,  132,  127,  128,  129,  130,  327,    0,    0,
          0,    0,  131,  132,    0,    0,  127,  128,  129,  130,
        327,    0,    0,    0,  133,  131,  132,    0,    0,    0,
          0,    0,    0,    0,    0,    0,    0,  133,    0,    0,
          0,    0,    0,    0,    0,    0,    0,  221,    0,    0,
        221,    0,  222,    0,    0,  222,    0,  133,    0,    0,
          0,    0,    0,  220,    0,    0,  220,    0,  133,    0,
          0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
          0,  133,    0,  221,    0,    0,    0,    0,  222,    0,
          0,    0,    0,    0,    0,    0,    0,    0,  224,  220,
          0,  224,    0,    0,  221,  221,  221,  221,    0,  222,
        222,  222,  222,  225,    0,    0,  225,    0,    0,    0,
        220,  220,  220,  220,  228,    0,  230,  228,    0,  230,
          0,    0,    0,    0,  224,    0,    0,    0,    0,    0,
          0,    0,    0,    0,  127,  292,  129,  130,  293,  225,
          0,    0,    0,  131,  132,  224,  224,  224,  224,    0,
        228,  229,  230,    0,  229,    0,    0,    0,    0,    0,
        225,  225,  225,  225,    0,    0,    0,    0,    0,    0,
          0,    0,  228,  228,  228,  230,  230,    0,    0,    0,
          0,    0,    0,    0,    0,    0,    0,  229,    0,    0,
         15,   16,   17,   18,   19,   20,   21,   22,   23,   24,
         25,   26,   27,   28,   29,   30,   31,   32,   33,    0,
        229,  229,   34,   35,    0,   36,   37,   38,   39,   40,
         41,   42,   43,   44,   45,   46,   47,   48,   49,   50,
         51,   52,   53,    0,    0,   54,   55,   56,   57,   58,
         15,   16,   17,   18,   19,   20,   21,   22,   23,   24,
         25,    0,   27,   28,   29,   30,   31,   32,   33,    0,
          0,    0,   34,   35,    0,   36,   37,   38,   39,    0,
         41,   42,   43,   44,   45,   46,   47,    0,   49,   50,
         51,   52,    0,    0,    0,   54,   55,   56,   57,   58,
         22,   23,   24,   25,    0,   27,   28,    0,   30,    0,
          0,   33,  394,    0,    0,    0,   35,    0,    0,    0,
          0,   39,    0,   41,   42,   43,    0,    0,   46,   47,
          0,   49,   50,    0,   52,    0,    0,    0,   54,   55,
         56,   57,   58,   22,   23,   24,   25,    0,   27,   28,
          0,   30,    0,    0,   33,    0,    0,    0,    0,   35,
          0,    0,    0,    0,   39,    0,   41,   42,   43,    0,
          0,   46,   47,    0,   49,   50,    0,   52,    0,    0,
          0,   54,   55,   56,   57,   58,   22,   23,    0,   25,
          0,   27,   28,    0,   30,    0,    0,    0,    0,    0,
          0,    0,   35,    0,    0,    0,    0,    0,    0,   41,
         42,   43,    0,    0,   46,   47,    0,   49,   50,    0,
         52,    0,  380,    0,   54,   55,   56,   57,   58,   15,
         16,   17,   18,   19,   20,   21,    0,    0,    0,    0,
          0,    0,    0,   29,    0,   31,   32,    0,    0,    0,
          0,   34,    0,    0,    0,   37,    0,    0,    0,    0,
          0,    0,   44,    0,    0,    0,    0,    0,    0,   51,
    };
  } /* End of class YyTableClass */

  protected static final class YyCheckClass {

    public static final short yyCheck [] = {            25,
          0,   77,   46,    3,   50,   47,   50,   33,   52,   33,
         47,   41,   41,   44,   42,   41,   29,   42,   43,   47,
         45,   41,   47,   42,   43,   22,   45,   45,   47,   58,
         42,   43,  201,   45,   31,   47,   80,   81,   47,   47,
        305,  331,  122,   97,  294,  295,   44,   41,   44,   47,
         40,   47,  128,   40,   44,   40,  426,   44,   40,   41,
         42,   43,   44,   45,   44,   47,  261,   40,   45,  262,
         41,   42,  265,   40,   40,   75,   58,  101,  200,  261,
         93,  259,   41,   40,  164,   44,   44,   44,   61,  133,
        134,  135,  136,   42,   40,   61,  466,   94,  258,   58,
         97,  261,  297,   40,   41,   42,   43,   44,   45,  257,
         47,  259,  260,   41,  111,  297,   44,  407,  162,  409,
          6,   58,    8,    9,   10,  294,  295,  288,  289,   40,
         58,  292,  176,   44,  178,  259,  180,  261,  288,  289,
        390,  185,  292,  261,  198,   40,   41,   44,  438,   44,
         47,   40,   40,   42,   42,   40,   41,   40,  202,   44,
         47,   42,   49,   58,   40,  261,   41,  289,  108,   44,
         57,   58,   41,   58,   41,   44,   40,   44,  264,  203,
        260,  225,  261,  448,  228,  229,  230,  231,  232,  233,
        234,  235,  236,  237,  238,  239,  240,  241,  242,  243,
        244,  198,   40,  200,  144,  205,   40,   41,   42,   43,
         44,   45,  256,   47,  258,  155,  156,  157,  158,  261,
        160,  390,  391,  267,   58,   40,   41,   42,   43,   44,
         45,  331,   47,  264,  264,  264,  213,   41,  282,  257,
         44,  259,  260,   58,  264,  271,   42,   43,   44,   45,
        276,   47,  296,  288,  289,  264,  264,   40,   41,   42,
         43,   44,   45,   40,   47,  289,  264,  261,  264,   41,
        261,  261,   44,  258,  264,   58,  261,  264,  261,  261,
        324,  261,  264,   41,   42,   43,   44,   45,  330,   47,
        261,  258,  289,  330,  261,   40,   41,  323,   43,   44,
         45,  329,  378,  328,  329,  330,  264,  297,  352,  261,
        329,  355,  261,   58,  261,  297,  313,  329,  330,  261,
         41,  330,  330,   44,  261,  261,  370,  264,  201,  375,
         40,  375,  330,  377,  330,   40,  318,  319,  320,  321,
         40,  323,  324,  325,  326,  327,  328,  258,  330,  393,
        261,   42,  264,   41,   42,   43,  261,   45,  402,   47,
        297,   41,   41,  407,   44,   44,  261,  411,   40,  264,
        258,  259,  416,  261,  418,   40,  261,  258,  259,  264,
        261,  318,  319,  320,  321,   61,  323,  324,  325,  326,
        327,  328,   41,  330,  438,   44,  312,   40,  264,  425,
         40,   41,  297,   43,   44,   45,  261,   61,   40,  453,
         42,   41,  297,   41,   44,  459,   44,  264,   58,   40,
        420,  294,  295,  318,  319,  320,  321,  261,   41,   44,
        264,   44,  432,  318,  319,  320,  321,  463,  323,  324,
        325,  326,  327,  328,   44,  330,  261,   40,   41,  264,
         40,   44,  452,   41,   42,   43,   44,   45,   41,   47,
         44,   44,   44,  297,  225,   58,   44,  264,  468,   47,
         58,   44,  261,   61,   42,   43,   44,   45,  261,   47,
         44,  264,  297,  244,  318,  319,  320,  321,  261,  323,
        324,  325,  326,  327,  328,   61,  330,   42,   43,   44,
         45,  261,   47,  318,  319,  320,  321,   47,  323,  324,
        325,  326,  327,  328,  297,  330,  261,  390,  391,  264,
         40,   44,  318,  319,  320,  321,   44,  323,  324,  325,
        326,  327,  328,  329,  330,  318,  319,  320,  321,  261,
        323,  324,  325,  326,  327,  328,   41,  330,  261,   44,
        264,   40,  297,   42,   41,   41,   41,   44,   44,   44,
        318,  319,  320,  321,   40,  323,  324,  325,  326,  327,
        328,  329,  330,  318,  319,  320,  321,   40,  323,  324,
        325,  326,  327,  328,   44,  330,   41,   42,   43,   44,
         45,  352,   47,  329,  355,   42,   43,  264,   45,   41,
         47,   41,   44,   58,   44,   44,   61,   41,   47,   40,
         44,   41,   42,   43,   44,   45,   41,   47,  264,   44,
         40,  261,   42,   43,  264,   45,  258,  259,   58,  261,
        318,  319,  320,  321,   40,  323,  324,  325,  326,  327,
        328,  329,  330,   41,   42,   43,  407,   45,   41,   47,
        411,   44,   41,  292,  264,   44,   58,  297,   41,   42,
         43,   42,   45,   41,   47,  261,   41,   44,  261,   41,
         44,  264,  264,  261,   44,  264,  264,   41,  318,  319,
        320,  321,   45,  323,  324,  325,  326,  327,  328,  264,
        330,   41,   42,   43,   41,   45,   41,   47,  459,  261,
         44,   41,  311,   44,  297,   42,   43,  264,   45,  297,
         47,  264,  264,  261,  264,  264,   47,  264,  264,   42,
         43,   58,   45,  264,   47,  318,  319,  320,  321,  264,
        318,  319,  320,  321,  264,  323,  324,  325,  326,  327,
        328,  329,  330,   42,   43,  264,   45,  264,   47,  264,
        318,  319,  320,  321,  264,  323,  324,  325,  326,  327,
        328,  329,  330,   42,   43,  264,   45,  264,   47,  258,
        259,  264,  261,  318,  319,  320,  321,   58,  323,  324,
        325,  326,  327,  328,  329,  330,  264,  264,  292,  264,
        264,   44,   44,  264,   41,  264,  264,   40,   41,   41,
        264,   44,  264,  264,  292,   89,  261,    3,  249,  264,
        396,    4,   75,  171,  262,   58,   40,   41,   42,   43,
        277,   45,  287,  268,  391,  390,  257,  258,  259,  260,
        261,  261,  193,  418,  264,  266,  267,  257,  258,  259,
        260,  261,  297,   40,  416,   42,  266,  267,  221,  436,
        377,  283,  261,  154,  196,  211,   94,  145,  244,  355,
        225,  432,  215,  318,  319,  320,  321,  297,  323,  324,
        325,  326,  327,  328,  329,  330,  323,  324,  325,  326,
        327,  328,  329,  330,  216,  185,   -1,   -1,  318,  319,
        320,  321,   -1,  323,  324,  325,  326,  327,  328,  329,
        330,   40,  322,   42,   43,   -1,   45,   -1,   -1,   -1,
         -1,   -1,   40,   -1,   42,   43,   -1,   45,   -1,   -1,
        318,  319,  320,  321,   -1,  323,  324,  325,  326,  327,
        328,  329,  330,   -1,   -1,  318,  319,  320,  321,   -1,
        323,  324,  325,  326,  327,  328,  329,  330,   40,   -1,
         42,   43,   -1,   45,   -1,   -1,   -1,   -1,   -1,   40,
         -1,   42,   43,   -1,   45,   -1,   -1,   -1,  318,  319,
        320,  321,   -1,  323,  324,  325,  326,  327,  328,  329,
        330,  318,  319,  320,  321,   -1,  323,  324,  325,  326,
        327,  328,  329,  330,   -1,  318,  319,  320,  321,   -1,
        323,  324,  325,  326,  327,  328,  329,  330,   40,   -1,
         -1,   43,   44,   45,   -1,   -1,   -1,   -1,  261,  318,
        319,  264,   -1,   -1,  323,  324,  325,  326,  327,  328,
        329,  330,   -1,  257,  258,  259,  260,  261,   -1,  318,
         -1,   -1,  266,  267,  323,  324,  325,  326,  327,  328,
        329,  330,   40,   -1,  297,   43,   -1,   45,   -1,   -1,
         -1,  258,  259,   -1,  261,   40,   -1,   -1,   43,   -1,
         45,   -1,   -1,   -1,   -1,  318,  319,  320,  321,   -1,
        323,  324,  325,  326,  327,   40,   -1,   -1,   43,   -1,
         45,   -1,   -1,   -1,   -1,   -1,   40,   -1,  322,   43,
         -1,   45,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   40,
         -1,   -1,   43,   -1,   45,   -1,   -1,   -1,  257,  258,
        259,  260,  261,   -1,   -1,   -1,   -1,  266,  267,  257,
        258,  259,  260,  261,   -1,   -1,   40,   41,  266,  267,
         44,   40,   41,   -1,   -1,   44,   -1,   -1,   -1,   -1,
         -1,   -1,   40,   41,   58,   -1,   44,   -1,   -1,   58,
         -1,   -1,   -1,   -1,   -1,  257,  258,  259,  260,  261,
         58,   -1,   -1,   -1,  266,  267,  257,  258,  259,  260,
        261,   -1,   -1,  322,   -1,  266,  267,   40,   41,   -1,
         -1,   44,   -1,   -1,  322,   -1,   -1,   -1,   -1,   -1,
         -1,   -1,   40,   41,   -1,   58,   44,   -1,   -1,   -1,
         -1,   -1,   -1,   40,   41,   40,   41,   44,   -1,   44,
         58,   -1,   -1,   -1,   -1,  257,  258,  259,  260,  261,
        322,   58,   -1,   58,  266,  267,   -1,   40,   -1,   -1,
         43,  322,   45,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
         40,   41,   -1,   -1,   44,   -1,   -1,   -1,   -1,   -1,
         -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   58,  257,
        258,  259,  260,  261,   -1,   -1,   -1,   -1,  266,  267,
         -1,   -1,  257,  258,  259,  260,  261,   -1,   -1,   -1,
        322,  266,  267,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
         -1,   -1,  257,  258,  259,  260,  261,   -1,   -1,   -1,
         -1,  266,  267,  257,  258,  259,  260,  261,   -1,   -1,
         -1,   -1,  266,  267,   -1,   -1,  257,  258,  259,  260,
        261,   -1,   -1,   -1,  322,  266,  267,   -1,   -1,   -1,
         -1,   -1,   -1,   -1,   -1,   -1,   -1,  322,   -1,   -1,
         -1,   -1,   -1,   -1,   -1,   -1,   -1,  261,   -1,   -1,
        264,   -1,  261,   -1,   -1,  264,   -1,  322,   -1,   -1,
         -1,   -1,   -1,  261,   -1,   -1,  264,   -1,  322,   -1,
         -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
         -1,  322,   -1,  297,   -1,   -1,   -1,   -1,  297,   -1,
         -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  261,  297,
         -1,  264,   -1,   -1,  318,  319,  320,  321,   -1,  318,
        319,  320,  321,  261,   -1,   -1,  264,   -1,   -1,   -1,
        318,  319,  320,  321,  261,   -1,  261,  264,   -1,  264,
         -1,   -1,   -1,   -1,  297,   -1,   -1,   -1,   -1,   -1,
         -1,   -1,   -1,   -1,  257,  258,  259,  260,  261,  297,
         -1,   -1,   -1,  266,  267,  318,  319,  320,  321,   -1,
        297,  261,  297,   -1,  264,   -1,   -1,   -1,   -1,   -1,
        318,  319,  320,  321,   -1,   -1,   -1,   -1,   -1,   -1,
         -1,   -1,  319,  320,  321,  320,  321,   -1,   -1,   -1,
         -1,   -1,   -1,   -1,   -1,   -1,   -1,  297,   -1,   -1,
        268,  269,  270,  271,  272,  273,  274,  275,  276,  277,
        278,  279,  280,  281,  282,  283,  284,  285,  286,   -1,
        320,  321,  290,  291,   -1,  293,  294,  295,  296,  297,
        298,  299,  300,  301,  302,  303,  304,  305,  306,  307,
        308,  309,  310,   -1,   -1,  313,  314,  315,  316,  317,
        268,  269,  270,  271,  272,  273,  274,  275,  276,  277,
        278,   -1,  280,  281,  282,  283,  284,  285,  286,   -1,
         -1,   -1,  290,  291,   -1,  293,  294,  295,  296,   -1,
        298,  299,  300,  301,  302,  303,  304,   -1,  306,  307,
        308,  309,   -1,   -1,   -1,  313,  314,  315,  316,  317,
        275,  276,  277,  278,   -1,  280,  281,   -1,  283,   -1,
         -1,  286,  287,   -1,   -1,   -1,  291,   -1,   -1,   -1,
         -1,  296,   -1,  298,  299,  300,   -1,   -1,  303,  304,
         -1,  306,  307,   -1,  309,   -1,   -1,   -1,  313,  314,
        315,  316,  317,  275,  276,  277,  278,   -1,  280,  281,
         -1,  283,   -1,   -1,  286,   -1,   -1,   -1,   -1,  291,
         -1,   -1,   -1,   -1,  296,   -1,  298,  299,  300,   -1,
         -1,  303,  304,   -1,  306,  307,   -1,  309,   -1,   -1,
         -1,  313,  314,  315,  316,  317,  275,  276,   -1,  278,
         -1,  280,  281,   -1,  283,   -1,   -1,   -1,   -1,   -1,
         -1,   -1,  291,   -1,   -1,   -1,   -1,   -1,   -1,  298,
        299,  300,   -1,   -1,  303,  304,   -1,  306,  307,   -1,
        309,   -1,  311,   -1,  313,  314,  315,  316,  317,  268,
        269,  270,  271,  272,  273,  274,   -1,   -1,   -1,   -1,
         -1,   -1,   -1,  282,   -1,  284,  285,   -1,   -1,   -1,
         -1,  290,   -1,   -1,   -1,  294,   -1,   -1,   -1,   -1,
         -1,   -1,  301,   -1,   -1,   -1,   -1,   -1,   -1,  308,
    };
  } /* End of class YyCheckClass */


//t  protected static final class YyRuleClass {

//t    public static final String yyRule [] = {
//t    "$accept : executable_program",
//t    "executable_program : program_unit",
//t    "executable_program : executable_program program_unit",
//t    "program_unit : program_stmt program_body",
//t    "program_unit : program_body",
//t    "program_unit : function_stmt program_body",
//t    "program_unit : subroutine_stmt program_body",
//t    "program_unit : block_data_subprogram",
//t    "program_body : end_statement",
//t    "program_body : complete_statement program_body",
//t    "end_statement : opt_label_def END EOS",
//t    "complete_statement : opt_label_def statement EOS",
//t    "statement : entry_stmt",
//t    "statement : declaration_statement",
//t    "statement : executable_statement",
//t    "opt_label_def : LABEL_DEF",
//t    "opt_label_def : NO_LABEL",
//t    "program_stmt : opt_label_def PROGRAM IDENT EOS",
//t    "function_stmt : opt_label_def type FUNCTION IDENT func_dummy_args EOS",
//t    "function_stmt : opt_label_def FUNCTION IDENT func_dummy_args EOS",
//t    "subroutine_stmt : opt_label_def SUBROUTINE IDENT subr_dummy_args EOS",
//t    "entry_stmt : ENTRY IDENT subr_dummy_args",
//t    "block_data_subprogram : block_data_stmt block_data_body end_statement",
//t    "block_data_stmt : opt_label_def BLOCKDATA opt_identifier EOS",
//t    "opt_identifier :",
//t    "opt_identifier : IDENT",
//t    "block_data_body :",
//t    "block_data_body : block_data_body opt_label_def data_spec_stmt EOS",
//t    "func_dummy_args : '(' ')'",
//t    "func_dummy_args : '(' func_dummy_arg_list ')'",
//t    "func_dummy_arg_list : identifier_list",
//t    "subr_dummy_args :",
//t    "subr_dummy_args : '(' ')'",
//t    "subr_dummy_args : '(' subr_dummy_arg_list ')'",
//t    "subr_dummy_arg_list : subr_dummy_arg",
//t    "subr_dummy_arg_list : subr_dummy_arg_list ',' subr_dummy_arg",
//t    "subr_dummy_arg : IDENT",
//t    "subr_dummy_arg : '*'",
//t    "declaration_statement : data_spec_stmt",
//t    "declaration_statement : functional_spec_stmt",
//t    "data_spec_stmt : type declaration_list",
//t    "data_spec_stmt : COMMON common_decl",
//t    "data_spec_stmt : EQUIVALENCE equivalence_decl",
//t    "data_spec_stmt : DATA data_decl",
//t    "data_spec_stmt : IMPLICIT implicit_decl",
//t    "data_spec_stmt : SAVE opt_save_list",
//t    "data_spec_stmt : PARAM '(' const_list ')'",
//t    "opt_save_list :",
//t    "opt_save_list : save_list",
//t    "functional_spec_stmt : EXTERNAL external_decl",
//t    "functional_spec_stmt : INTRINSIC intrinsic_decl",
//t    "declaration_list : one_declaration",
//t    "declaration_list : declaration_list ',' one_declaration",
//t    "one_declaration : IDENT dims opt_length_spec",
//t    "one_declaration : IDENT opt_length_spec",
//t    "opt_length_spec :",
//t    "opt_length_spec : '*' expr",
//t    "opt_length_spec : '*' '(' '*' ')'",
//t    "type : type_name opt_length_spec",
//t    "type : DIMENSION",
//t    "type_name : INTEGER",
//t    "type_name : REAL",
//t    "type_name : COMPLEX",
//t    "type_name : DOUBLE_PREC",
//t    "type_name : LOGICAL",
//t    "type_name : CHARACTER",
//t    "common_decl : start_block",
//t    "common_decl : common_decl block_name one_block",
//t    "start_block : opt_block_name one_block",
//t    "opt_block_name :",
//t    "opt_block_name : block_name",
//t    "block_name : DOUBLE_SLASH",
//t    "block_name : '/' opt_identifier '/'",
//t    "one_block : common_var",
//t    "one_block : one_block ','",
//t    "one_block : one_block ',' common_var",
//t    "common_var : IDENT dims",
//t    "common_var : IDENT",
//t    "external_decl : identifier_list",
//t    "identifier_list : IDENT",
//t    "identifier_list : identifier_list ',' IDENT",
//t    "intrinsic_decl : identifier_list",
//t    "equivalence_decl : equivalence_set",
//t    "equivalence_decl : equivalence_decl ',' equivalence_set",
//t    "equivalence_set : '(' left_name_list ')'",
//t    "left_name_list : left_name",
//t    "left_name_list : left_name_list ',' left_name",
//t    "data_decl : data_seq",
//t    "data_decl : data_decl data_seq",
//t    "data_decl : data_decl ',' data_seq",
//t    "data_seq : data_var_list '/' data_val_list '/'",
//t    "data_val_list : data_val",
//t    "data_val_list : data_val_list ',' data_val",
//t    "data_val : value",
//t    "data_val : simple_value '*' value",
//t    "value : simple_value",
//t    "value : '+' simple_value",
//t    "value : '-' simple_value",
//t    "simple_value : IDENT",
//t    "simple_value : constant",
//t    "simple_value : complex_const",
//t    "save_list : save_item",
//t    "save_list : save_list ',' save_item",
//t    "save_item : IDENT",
//t    "save_item : block_name",
//t    "const_list : const_item",
//t    "const_list : const_list ',' const_item",
//t    "const_item : IDENT '=' expr",
//t    "data_var_list : data_var",
//t    "data_var_list : data_var_list ',' data_var",
//t    "data_var : left_name",
//t    "data_var : '(' data_var_list ',' do_spec ')'",
//t    "dims : '(' dim_list ')'",
//t    "dim_list : dim",
//t    "dim_list : dim_list ',' dim",
//t    "dim : upper_bound",
//t    "dim : expr ':' upper_bound",
//t    "upper_bound : '*'",
//t    "upper_bound : expr",
//t    "implicit_decl : imp_item",
//t    "implicit_decl : implicit_decl ',' imp_item",
//t    "imp_item : IDENT opt_length_spec '(' letter_group_list ')'",
//t    "imp_item : IDENT",
//t    "letter_group_list : letter_group",
//t    "letter_group_list : letter_group_list ',' letter_group",
//t    "letter_group : IDENT",
//t    "letter_group : IDENT '-' IDENT",
//t    "executable_statement : ifable_statement",
//t    "executable_statement : DO label do_spec",
//t    "executable_statement : DO label ',' do_spec",
//t    "executable_statement : DO do_spec EOS do_tail",
//t    "executable_statement : IF '(' expr ')' ifable_statement",
//t    "executable_statement : block_if_part opt_else_if_parts opt_else_part END_IF",
//t    "executable_statement : FORMAT FORMAT_SPECIFICATION",
//t    "do_tail : opt_label_def END_DO",
//t    "do_tail : opt_label_def executable_statement EOS do_tail",
//t    "part_tail :",
//t    "part_tail : part_tail opt_label_def executable_statement EOS",
//t    "block_if_part : IF '(' expr ')' THEN EOS part_tail",
//t    "opt_else_if_parts :",
//t    "opt_else_if_parts : opt_else_if_parts ELSE_IF '(' expr ')' THEN EOS part_tail",
//t    "opt_else_part :",
//t    "opt_else_part : ELSE EOS part_tail",
//t    "do_spec : IDENT '=' expr ',' expr opt_step",
//t    "opt_step :",
//t    "opt_step : ',' expr",
//t    "ifable_statement : LET left_name '=' expr",
//t    "ifable_statement : ASSIGN label TO IDENT",
//t    "ifable_statement : CONTINUE",
//t    "ifable_statement : GOTO label",
//t    "ifable_statement : ASSIGN_GOTO IDENT opt_labels",
//t    "ifable_statement : ASSIGN_GOTO IDENT ',' opt_labels",
//t    "ifable_statement : COMP_GOTO '(' label_list ')' expr",
//t    "ifable_statement : COMP_GOTO '(' label_list ')' ',' expr",
//t    "ifable_statement : ARITH_IF '(' expr ')' label ',' label ',' label",
//t    "ifable_statement : CALL IDENT opt_actual_args",
//t    "ifable_statement : RETURN opt_expr",
//t    "ifable_statement : PAUSE",
//t    "ifable_statement : PAUSE expr",
//t    "ifable_statement : STOP",
//t    "ifable_statement : STOP expr",
//t    "ifable_statement : io_statement",
//t    "opt_actual_args :",
//t    "opt_actual_args : '(' ')'",
//t    "opt_actual_args : '(' arg_list ')'",
//t    "arg_list : arg",
//t    "arg_list : arg_list ',' arg",
//t    "arg_list :",
//t    "arg : expr",
//t    "arg : '*' label",
//t    "opt_labels :",
//t    "opt_labels : '(' label_list ')'",
//t    "label_list : label",
//t    "label_list : label_list ',' label",
//t    "label : INT_CONST",
//t    "io_statement : PRINT format_spec opt_comma_io_list",
//t    "io_statement : WRITE '(' ci_list ')' opt_io_list",
//t    "io_statement : READ '(' ci_list ')' opt_io_list",
//t    "io_statement : READ format_spec opt_comma_io_list",
//t    "io_statement : OPEN '(' ci_list ')'",
//t    "io_statement : CLOSE '(' ci_list ')'",
//t    "io_statement : BACKSPACE '(' ci_list ')'",
//t    "io_statement : BACKSPACE format_spec",
//t    "io_statement : END_FILE '(' ci_list ')'",
//t    "io_statement : END_FILE format_spec",
//t    "io_statement : REWIND '(' ci_list ')'",
//t    "io_statement : REWIND format_spec",
//t    "io_statement : INQUIRE '(' ci_list ')'",
//t    "ci_list : io_clause",
//t    "ci_list : ci_list ',' io_clause",
//t    "io_clause : expr",
//t    "io_clause : '*'",
//t    "io_clause : IDENT '=' expr",
//t    "io_clause : IDENT '=' '*'",
//t    "format_spec : '*'",
//t    "format_spec : IDENT",
//t    "format_spec : INT_CONST",
//t    "format_spec : CHAR_CONST",
//t    "opt_io_list :",
//t    "opt_io_list : io_list",
//t    "io_list : io_item",
//t    "io_list : io_list ',' io_item",
//t    "opt_comma_io_list :",
//t    "opt_comma_io_list : ',' io_list",
//t    "io_item : expr",
//t    "io_item : '(' do_list ')'",
//t    "do_list : do_spec",
//t    "do_list : io_item ',' do_list",
//t    "expr : left_name",
//t    "expr : constant",
//t    "expr : complex_const",
//t    "expr : '(' expr ')'",
//t    "expr : '-' expr",
//t    "expr : '+' expr",
//t    "expr : expr POWER expr",
//t    "expr : expr '*' expr",
//t    "expr : expr '/' expr",
//t    "expr : expr '+' expr",
//t    "expr : expr '-' expr",
//t    "expr : expr DOUBLE_SLASH expr",
//t    "expr : expr GREATER_THAN expr",
//t    "expr : expr EQUAL expr",
//t    "expr : expr LESS_THAN expr",
//t    "expr : expr NOT_EQUAL expr",
//t    "expr : expr LESS_OR_EQUAL expr",
//t    "expr : expr GREATER_OR_EQUAL expr",
//t    "expr : NOT expr",
//t    "expr : expr AND expr",
//t    "expr : expr OR expr",
//t    "expr : expr EQV expr",
//t    "expr : expr NEQV expr",
//t    "left_name : IDENT",
//t    "left_name : CHAR_CONST substring",
//t    "left_name : IDENT substring",
//t    "left_name : IDENT '(' arg_list ')'",
//t    "left_name : IDENT '(' arg_list ')' substring",
//t    "substring : '(' opt_expr ':' opt_expr ')'",
//t    "opt_expr :",
//t    "opt_expr : expr",
//t    "constant : number_constant",
//t    "constant : TRUE_CONST",
//t    "constant : FALSE_CONST",
//t    "constant : CHAR_CONST",
//t    "number_constant : INT_CONST",
//t    "number_constant : REAL_CONST",
//t    "number_constant : DOUBLE_CONST",
//t    "complex_const : '(' io_item ',' number_constant ')'",
//t    "complex_const : '(' io_item ',' '-' number_constant ')'",
//t    };
//t  } /* End of class YyRuleClass */

  protected static final class YyNameClass {

    public static final String yyName [] = {    
    "end-of-file",null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    "'('","')'","'*'","'+'","','","'-'",null,"'/'",null,null,null,null,
    null,null,null,null,null,null,"':'",null,null,"'='",null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,"REAL_CONST",
    "CHAR_CONST","INT_CONST","DOUBLE_CONST","IDENT","LABEL_DEF","LABEL",
    "EOS","NO_LABEL","TRUE_CONST","FALSE_CONST","INTEGER","REAL",
    "COMPLEX","DOUBLE_PREC","LOGICAL","CHARACTER","PARAM","LET",
    "ARITH_IF","IF","ASSIGN","BLOCKDATA","CALL","CLOSE","COMMON",
    "CONTINUE","DATA","DIMENSION","DO","END_DO","ELSE","ELSE_IF","END",
    "END_FILE","END_IF","ENTRY","EQUIVALENCE","EXTERNAL","FORMAT",
    "FUNCTION","GOTO","ASSIGN_GOTO","COMP_GOTO","IMPLICIT","INTRINSIC",
    "PAUSE","PRINT","PROGRAM","READ","RETURN","SAVE","STOP","SUBROUTINE",
    "THEN","TO","WRITE","OPEN","INQUIRE","BACKSPACE","REWIND","AND","OR",
    "NEQV","EQV","NOT","EQUAL","LESS_THAN","GREATER_THAN","LESS_OR_EQUAL",
    "GREATER_OR_EQUAL","NOT_EQUAL","POWER","DOUBLE_SLASH",
    "FORMAT_SPECIFICATION","UMINUS","UPLUS",
    };
  } /* End of class YyNameClass */


					// line 623 "../coins-0.10.2/src/coins/ffront/f77k.jay"

}


					// line 2264 "-"
