/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
//##8 : Changed when control structure was changed (Sep. 2001).

package coins.cfront;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

import coins.ast.TokenId;
import coins.ast.TypeId;
import coins.ast.Pragma;
import coins.Debug;
import coins.Message;
import coins.IoRoot;

class Token {
    public Token next = null;
    public int tokenId;
    public int lineNumber;
    public String fileName;

    public long longValue;
    public double doubleValue;
    public String textValue;
}

/**
 * Lexical analyzer.
 */
public class Lex implements TokenId, TypeId {

    final IoRoot ioRoot; //SF041014
    final Debug debug; //SF041014
    final Message warning; //SF041014
    final Message error; //SF041014

    Parser parser; //SF040217

    private int lastChar;
    private int lastTokenId;
    private InputStream input;
    private int lineNumber;
    private String fileName;

    private Token currentToken;
    private Token lookAheadTokens;

    private StringBuffer textBuffer;
    //SF040217 private SymbolTable typeNames;

    private static final KeywordTable ktable = new KeywordTable(40);
    public final int fDbgLevel; //##67

    static {
        // the ordering must be alphabetical order

        ktable.append("__builtin_va_list", INT); //SF041121 ignore va_list because va_arg is not supported.
        ktable.append("asm", ASM);
        ktable.append("auto", AUTO);
        ktable.append("break", BREAK);
        ktable.append("case", CASE);
        ktable.append("char", CHAR);
        ktable.append("const", CONST);
        ktable.append("continue", CONTINUE);
        ktable.append("default", DEFAULT);
        ktable.append("do", DO);
        ktable.append("double", DOUBLE);
        ktable.append("else", ELSE);
        ktable.append("enum", ENUM);
        ktable.append("extern", EXTERN);
        ktable.append("float", FLOAT);
        ktable.append("for", FOR);
        ktable.append("goto", GOTO);
        ktable.append("if", IF);
        ktable.append("inline", INLINE);
        ktable.append("int", INT);
        ktable.append("long", LONG);
        //##83 ktable.append("pragma", PRAGMA); //##70
        // pragma is not a reserved word.
        // It is recognized by readLineDirective. //##83
        //SF031210 ktable.append("mutable", MUTABLE);
        //##18 is this a reserved word ?
        //Spec2000/gap/list.c use mutable as a variable.
        ktable.append("register", REGISTER);
        ktable.append("restrict", RESTRICT);  //##81
        ktable.append("return", RETURN);
        ktable.append("short", SHORT);
        ktable.append("signed", SIGNED);
        ktable.append("sizeof", SIZEOF);
        ktable.append("static", STATIC);
        ktable.append("struct", STRUCT);
        ktable.append("switch", SWITCH);
        ktable.append("typedef", TYPEDEF);
        ktable.append("union", UNION);
        ktable.append("unsigned", UNSIGNED);
        ktable.append("void", VOID);
        ktable.append("volatile", VOLATILE);
        ktable.append("while", WHILE);
    } // static (initiation) procedure.

    /**
     * Constructs a lexical analyzer.
     */
    //## public Lex(InputStream s)
    public Lex(IoRoot ioroot, InputStream s)
    {
        ioRoot = ioroot;
        debug = ioroot.dbgParse; //SF041014
        warning = ioroot.msgWarning;
        error = ioroot.msgError;

        lastChar = -1;
        lastTokenId = '\n';
        input = s;
        lineNumber = 1;
        fileName = "unknown";

        currentToken = new Token();
        lookAheadTokens = null;

        textBuffer = new StringBuffer();
        //SF040217 typeNames = new SymbolTable();
        fDbgLevel = ioroot.dbgParse.getLevel(); //##67
    } // Lex

    public int get() throws IOException
    {
        if (lookAheadTokens == null)
            return get(currentToken);
        else {
            Token t;
            currentToken = t = lookAheadTokens;
            lookAheadTokens = lookAheadTokens.next;
            if (fDbgLevel > 3) //##67
              debug.print(6, "lex.get",
                (char)t.tokenId+" "+t.tokenId+" "+getString()+" " );
            return t.tokenId;
        }
    } // get

    /**
     * Looks at the next token.
     */
    public int lookAhead() throws IOException {
        return lookAhead(0);
    }

    /**
     * Looks at the n-th token.
     *
     * @param i         equal to or more than zero
     */
    public int lookAhead(int i) throws IOException
    {
        Token tk = lookAheadTokens;
        if (tk == null) {
            lookAheadTokens = tk = currentToken;  // reuse an object!
            tk.next = null;
            get(tk);
        }

        for (; i-- > 0; tk = tk.next)
            if (tk.next == null) {
                Token tk2;
                tk.next = tk2 = new Token();
                get(tk2);
            }

        currentToken = tk;
        if (parser.fDbgLevel > 3) //##67
          debug.print(6, "lookAhead",
          (char)tk.tokenId+" "+tk.tokenId+" "+getString()+" " );
        return tk.tokenId;
    } // lookAhead

    public int getLineNumber()
    {
        return currentToken.lineNumber;
    }

    public String getFileName() {
        return currentToken.fileName;
    }

    public String getString() {
        return currentToken.textValue;
    }

    public long getLong() {
        return currentToken.longValue;
    }

    public double getDouble() {
        return currentToken.doubleValue;
    }

////////SF040217[
//      public byte[] lookupType(String name) {
//      return (byte[])typeNames.get(name);
//    }
////////SF040217]

//##17 BEGIN

    // If pTokenId is a token for specifying type, then return true.
    public boolean isType(int pTokenId)
    {
        switch (pTokenId) {
        case  AUTO :
        case  CHAR :
        case  CONST :
        case  DOUBLE :
        case  ENUM :
        case  EXTERN :
        case  FLOAT :
        case  INT :
        case  LONG :
        case  REGISTER :
        case  RESTRICT:  //##81
        case  SHORT :
        case  SIGNED :
        case  STATIC :
        case  STRUCT :
        case  TYPEDEF :
        case  UNION :
        case  UNSIGNED :
        case  VOID :
        case  VOLATILE :
        case  TYPEDEF_NAME :
          return true;
        default:
          return false;
        }
    } // isType

public StringBuffer
getTextBuffer()
{
  return textBuffer;
}

//##17 END

////////SF040217[
//    public void recordType(String name, byte[] encodedType) {
//      typeNames.put(name, encodedType);
//    }
//
//    public void enterBlock() {
//      typeNames = new SymbolTable(33, typeNames);
//    }
//
//    public void leaveBlock() {
//      typeNames = typeNames.getParent();
//    }
////////SF040217]

    private int get(Token token) throws IOException
    {
        int t;
        do {
            t = readLine(token);
            lastTokenId = t;
            //##70 if (t == ASM)
            if (t == SKIP_GCC_ASM) { //##70
              warning.put("Skip gcc asm(...) at " +         //##85
                     getFileName()+ ":" + getLineNumber()); //##85
              t = IGNORE;
              skipAsm();
            //##85 BEGIN
            }else if (t == SKIP_GCC_ATTRIBUTE) { //##85
                t = IGNORE;
                skipAsm();
            //##85 END
            }else if (t == ASM) {  //##70
              /* //##70
              int c;
              do {
                  c = getc(); // Search for left parenthesis.
              } while (c != EOF && c != '(');
              if (c == EOF) {
                return EOF;
              }
              StringBuffer lBuffer = new StringBuffer("(");
              int lLevel = 1;  // Parenthesis level.
              do {
                c = getc();
                if (c== EOF)
                  break;
                lBuffer.append(c);
                if (c == '(')
                  ++lLevel;
                else if (c == ')')
                  --lLevel;
              } while (lLevel > 0);
              lBuffer.append(")");
              token.textValue = lBuffer.toString();
              */ //##70
              token.textValue = "asm";  //##70
            } // end of ASM
        } while (t == '\n' || t == IGNORE);
        token.tokenId = t;
        token.lineNumber = lineNumber;
        token.fileName = fileName;
        if (parser.fDbgLevel > 3) //##67
          debug.print(6, "lex.get",
            (char)t+" "+t+" curr:"+currentToken.tokenId+" "+getString()+" " );
        return t;
    } // get

    /**
     * Skip tokens starting from '(' until ')'
     * @throws IOException
     */
    private void skipAsm() throws IOException
    {
        int c;
        do {
            c = getc();
        } while (c != EOF && c != '(');
        int i = 1;
        do {
            c = getc();
            if (c == '(')
                ++i;
            else if (c == ')')
                --i;
            else if (c == EOF)
                break;
        } while (i > 0);
    } // skipAsm

    private int readLine(Token token) throws IOException
    {
        int c = getNextNonWhiteChar();
        // System.out.print(" readLine " + c + " " + (char)c + " "); //###
        if(c < 0)
            return c;
        else if(c == '\n') {
            ++lineNumber;
            return '\n';
        }
        else if (c == '#' && lastTokenId == '\n') {
            //##70 readLineDirective();
            int lDirectiveValue = readLineDirective(token); //##70
            if (lDirectiveValue == PRAGMA) { //##70
              return PRAGMA; //##70
            }else { //##70
              return '\n';
            } //##70
        }
        else if (c == '\'')
            return readCharConst(token);
        else if (c == '"')
            return readStringL(token);
        else if ('0' <= c && c <= '9')
            return readNumber(c, token);
        else if(c == '.'){
            c = getc();
            if ('0' <= c && c <= '9') {
                StringBuffer tbuf = textBuffer;
                tbuf.setLength(0);
                tbuf.append('.');
                return readDouble(tbuf, c, token);
            }
            else{
                ungetc(c);
                return readSeparator('.');
            }
        }
        else if (c == 'L') {
            int c2 = getc();
            if (c2 == '"')
                return readStringWL(token);
            else {
                ungetc(c2);
                return readIdentifier(c, token);
            }
        }
        else if ('A' <= c && c <= 'Z' || 'a' <= c && c <= 'z' || c == '_'
                 || c == '$')
            return readIdentifier(c, token);
        else
            return readSeparator(c);
    } // readLine

    private int getNextNonWhiteChar() throws IOException
    {
        int c;
        for (;;) {
            do {
                c = getc();
            } while(isBlank(c));
            //##85 BEGIN
            //-- Check if there is a line tail comment.
            if (c == '/') {
              int c2 = getc();
              if (c2 == '/')
              //### if (c2 == '#') //### Tentative coding for test
              {
                // Skip //-comment
                do {
                  c = getc();
                }while ((c != '\n')&&(c != '\r'));
                return getNextNonWhiteChar();
              // }else if (c2 == '\\') {
              // Refine for the case where the 2nd slash is
              // written after '\\'.
              // (It will require successive ungetc() if
              //  the next character is not slash.)
              }else {
                // Restore the character after '/'
                ungetc(c2);
              }
            }
            //##85 END
            if (c != '\\')
                break;
            // c == '\\'
            c = getc();
            if (c != '\n' && c!= '\r') {
                ungetc(c);
                break;
            }
            // if ((c == '\\')&&((c == '\n')||(c == '\r')))
            //   go back to the next iteration.
        }

        return c;
    } // getNextNonWhiteChar

    //##70 private void readLineDirective() throws IOException
    private int readLineDirective(Token token) throws IOException //##70
    {
        int lReturnValue = 0;  //##70
        int c;
        do {
            c = getc();
        } while (isBlank(c));
        // System.out.print(" readLineDirective " + c); //###
        if (isDigit(c)) {       /* # <line> <file> */
            int num = c - '0';
            for (;;) {
                c = getc();
                if (isDigit(c))
                    num = num * 10 + c - '0';
                else
                    break;
            }

            lineNumber = num - 1;       // it will be incremented soon.

            while (isBlank(c))
                c = getc();

            if (c == '"') {
                StringBuffer tbuf = textBuffer;
                tbuf.setLength(0);
                while ((c = getc()) != '"')
                    tbuf.append((char)c);

                fileName = tbuf.toString();
            }
        }
        //SF050304[
        else { // #pragma
            StringBuffer tbuf = textBuffer;
            tbuf.setLength(0);
            tbuf.append((char)c);
            while ((c = getc()) != '\n')
                tbuf.append((char)c); // Get all characters in this line.
            if (tbuf.indexOf("pragma")==0) {
                String tokens = tbuf.substring(6).trim();
                //##70 parser.pragmaList.add(new Pragma(tokens,fileName,lineNumber));
                //##70 BEGIN
                // tokens contains remaining characters after "pragma".
                // System.out.print(" pragma text " + tokens); //###
                token.textValue = tokens.intern();
                lReturnValue = PRAGMA;
                ungetc(c);
                //##70 END
            }
        }
        //SF050304]

        while (c != '\n')
            c = getc();

        ++lineNumber;
        return lReturnValue; //##70
    } // readLineDirective

    private int readCharConst(Token token) throws IOException
    {
        int c;
        int value = 0;
        while ((c = getc()) != '\'') {
            if (c == '\\')
                value = readEscapeChar();
            else if (c < 0x20) {
                if (c == '\n')
                    ++lineNumber;

                return BAD_TOKEN;
            }
            else
                value = c;
        }
        token.longValue = value;
        return CHAR_CONST;
    } // readCharConst

    private int readEscapeChar() throws IOException
    {
      int c1, c2; //##15
      int c = getc();
      if (c == 'n')
        c = '\n';
      else if (c == 't')
        c = '\t';
      else if (c == 'r')
        c = '\r';
      else if (c == 'f')
        c = '\f';
      else if (c == '\n') //##14
        ++lineNumber;
      else if (c == 'b')  //##14
        c = '\b';         //##14
      else if (c == 'v')  //##43
        c = '\u000b';     //##43
      else if (c == 'a')  //##43
        c = '\u0007';     //##43
      else if (c == '\\') //##14
        c = '\\';         //##14
      else if (c == '\'') //##14
        c = '\'';         //##14
      else if (c == '"')  //##14
        c = '\"';         //##14
      else if (Character.isDigit((char)c) && (c <= '7')) { //##15
        c = Character.digit((char)c, 8); //##15
        c1 = getc();  //##15
        if (Character.isDigit((char)c1) && (c1 <= '7')) { //##15
          c2 = getc(); //##15
          if (Character.isDigit((char)c2) && (c2 <= '7')) { //##15
            c = c * 64 + Character.digit((char)c1, 8) * 8  //##15
                       + Character.digit((char)c2, 8); //##15
          }else { //##15
            ungetc(c2); //##15
            c = c * 8 + Character.digit((char)c1, 8); //##15
          } //##15
        }else { //##15
          ungetc(c1); //##15
        } //##15
      }
      //SF041115[
      else if (c == 'x') {
        c = 0;
        for(;;) {
          c1 = getc();
          int digit = Character.digit((char)c1, 16);
          if (0 <= digit)
            c = c * 16 + digit;
          else
            break;
        }
        ungetc(c1);
      }
      //SF041115]
      return c;
    } // readEscapeChar

    /* wchar_t string literal (e.g. L"abc")
     */
    private int readStringWL(Token token) throws IOException
    {
        int t = readStringL(token);
        if (t == STRING_L)
            return STRING_WL;
        else
            return t;
    } //  readStringWL

    private int readStringL(Token token) throws IOException
    {
        int c;
        StringBuffer tbuf = textBuffer;
        tbuf.setLength(0);
        for (;;) {
            while ((c = getc()) != '"') {
                if (c == '\\')
                    c = readEscapeChar();
                else if (c == '\n' || c < 0) {
                    ++lineNumber;
                    return BAD_TOKEN;
                }

                tbuf.append((char)c);
            }

            for (;;) {
                c = getc();
                if (c == '\n')
                    ++lineNumber;
                else if (!isBlank(c))
                    break;
            }

            if (c != '"') {
                ungetc(c);
                break;
            }
        }

        //##19 token.textValue = tbuf.toString();
        token.textValue = tbuf.toString().intern(); //##19
        return STRING_L;
    } // readStringL

////////////////////SF040123[
//    private int readNumber(int c, Token token) throws IOException {
//      long value = 0;
//      int c2 = getc();
//      if (c == '0')
//          if (c2 == 'X' || c2 == 'x')
//              for (;;) {
//                  c = getc();
//                  if ('0' <= c && c <= '9')
//                      value = value * 16 + (long)(c - '0');
//                  else if ('A' <= c && c <= 'F')
//                      value = value * 16 + (long)(c - 'A' + 10);
//                  else if ('a' <= c && c <= 'f')
//                      value = value * 16 + (long)(c - 'a' + 10);
//                  else {
//                      token.longValue = value;
//                      if (c == 'L' || c == 'l')
//                          return LONG_CONST;
//                      else
//                          return readIntegerType(c);
//                  }
//              }
//          else if ('0' <= c2 && c2 <= '7') {
//              value = c2 - '0';
//              c = getc();
//              while ('0' <= c && c <= '7') {
//                  value = value * 8 + (long)(c - '0');
//                  c = getc();
//              }
//
//              token.longValue = value;
//              if (c == 'L' || c == 'l')
//                  return LONG_CONST;
//              else
//                  return readIntegerType(c);
//          }
//
//      value = c - '0';
//      while ('0' <= c2 && c2 <= '9') {
//          value = value * 10 + c2 - '0';
//          c2 = getc();
//      }
//
//      if (c2 == 'F' || c2 == 'f') {
//          token.doubleValue = (double)value;
//          return FLOAT_CONST;
//      }
//      else if (c2 == 'E' || c2 == 'e' || c2 == '.') {
//          StringBuffer tbuf = textBuffer;
//          tbuf.setLength(0);
//          tbuf.append(value);
//          return readDouble(tbuf, c2, token);
//      }
//      else if (c2 == 'L' || c2 == 'l') {
//          token.longValue = value;
//          return LONG_CONST;
//      }
//      else {
//          token.longValue = value;
//          return readIntegerType(c2);
//      }
//    }
//
//    private int readIntegerType(int c) throws IOException {
//      if (c == 'U' || c == 'u') {
//          c = getc();
//          if (c == 'L' || c == 'l')
//              return ULONG_CONST;
//
//          ungetc(c);
//          return UINT_CONST;
//      }
//
//      ungetc(c);
//      return INT_CONST;
//    }

    private int readNumber(int c, Token token) throws IOException
    {
      StringBuffer tbuf = textBuffer;
      tbuf.setLength(0);
      if( c=='0' )
      {
        int radix;
        if( (c=getc())=='X' || c=='x' )
        {
          c = readDigits(tbuf,16);
          return readInteger(c,token,16); //SF040615
        }
        if( 0 <= Character.digit((char)c,8) )
        {
          c = readDigits(tbuf.append((char)c),8);
          return readInteger(c,token,8); //SF040615
        }
        ungetc(c);
        c = '0';
      }
      c = readDigits(tbuf.append((char)c),10);
      if( c=='E' || c=='e' || c=='.' )
        return readDouble(tbuf,c,token);
      //SF041115 if( c=='F' || c=='f' )
      //SF041115 {
      //SF041115   token.doubleValue = Double.parseDouble(tbuf.toString());
      //SF041115   return FLOAT_CONST;
      //SF041115 }
      return readInteger(c,token,10); //SF040615
    } // readNumber

    private int readDigits(StringBuffer tbuf,int radix) throws IOException
    {
      int c;
      while( 0 <= Character.digit((char)(c=getc()),radix) )
        tbuf.append((char)c);
      return c;
    } // readDigits

    ////////SF040615[
    private int readInteger(int c,Token token,int radix) throws IOException
    {
      boolean signed = true;
      boolean unsigned = true; //radix!=10;
      int nlong = 0;

      if( c=='U' || c=='u' )
      {
        c = getc(); signed = false; unsigned = true;
        if( c=='L' || c=='l' )
        {
          c = getc(); nlong++;
          if( c=='L' || c=='l' )
          {
            c = getc(); nlong++;
          }
        }
      }
      else if( c=='L' || c=='l' )
      {
        c = getc(); nlong++; unsigned = true;
        if( c=='L' || c=='l' )
        {
          c = getc(); nlong++;
          if( c=='U' || c=='u' )
          {
            c = getc(); signed = false;
          }
        }
        //##77 BEGIN
        else if( c=='U' || c=='u' )
        {
          c = getc(); signed = false;
          if( c=='L' || c=='l' )
          {
            c = getc();
            nlong++;
          }
        }
        //##77 END
      }
      ungetc(c);
      BigInteger big = new BigInteger(textBuffer.toString(),radix); //SF041020
      int len = big.bitLength(); //SF041020
      token.longValue = big.longValue(); //SF041020

      if( signed   && nlong<=0 && len<8*parser.evaluator.toSize[INT_T] )
        return INT_CONST;
      else
      if( unsigned && nlong<=0 && len<=8*parser.evaluator.toSize[INT_T] )
        return UINT_CONST;
      else
      if( signed   && nlong<=1 && len<8*parser.evaluator.toSize[LONG_T] )
        return LONG_CONST;
      else //SF041020
      if( /*unsigned &&*/ nlong<=1 && len<=8*parser.evaluator.toSize[LONG_T] )
        return ULONG_CONST;
      else //SF041020
      if( signed   && nlong<=2 && len<8*parser.evaluator.toSize[LONG_LONG_T] )
        return LONGLONG_CONST;
      else //SF041020
        return ULONGLONG_CONST;
    } // readInteger
    ////////SF040615]

////////////////////SF040123]

    private int readDouble(StringBuffer sbuf, int c, Token token)
        throws IOException
    {
        if (c != 'E' && c != 'e') {
            sbuf.append((char)c);
            for (;;) {
                c = getc();
                if ('0' <= c && c <= '9')
                    sbuf.append((char)c);
                else
                    break;
            }
        }

        if (c == 'E' || c == 'e') {
            sbuf.append((char)c);
            c = getc();
            if (c == '+' || c == '-') {
                sbuf.append((char)c);
                c = getc();
            }

            while ('0' <= c && c <= '9') {
                sbuf.append((char)c);
                c = getc();
            }
        }

        int type;
        if (c == 'L' || c == 'l')
            type = LONG_DOUBLE_CONST;   // equivalent to DOUBLE_CONST
        else if (c == 'F' || c == 'f')
            type = FLOAT_CONST;
        else {
            ungetc(c);
            type = DOUBLE_CONST;
        }

        try {
            token.doubleValue = Double.parseDouble(sbuf.toString());
            /* for JDK 1.1
            token.doubleValue = Double.valueOf(sbuf.toString()).doubleValue();
            */
            return type;
        }
        catch (NumberFormatException e) {
            return BAD_TOKEN;
        }
    } // readDouble

    // !"#$%&'(    )*+,-./0    12345678    9:;<=>?
    private static final int[] equalOps
        =  { NEQ, 0, 0, 0, MOD_E, AND_E, 0, 0,
             0, MUL_E, PLUS_E, 0, MINUS_E, ELLIPSIS, DIV_E, 0,
             0, 0, 0, 0, 0, 0, 0, 0,
             0, 0, 0, LE, EQ, GE, 0 };

    private int readSeparator(int c) throws IOException
    {
        int c2, c3;
        if ('!' <= c && c <= '?') {
            int t = equalOps[c - '!'];
            if (t == 0)
                return c;
            else {
                c2 = getc();
                if (c == '.') {
                    if (c2 == '.') {
                        c3 = getc();
                        if (c3 == '.')
                            return ELLIPSIS;
                        else {
                            ungetc(c3);
                            return BAD_TOKEN;
                        }
                    }
                }
                else if (c == c2)
                    switch (c) {
                    case '=' :
                        return EQ;
                    case '+' :
                        return PLUSPLUS;
                    case '-' :
                        return MINUSMINUS;
                    case '&' :
                        return ANDAND;
                    case '<' :
                        c3 = getc();
                        if (c3 == '=')
                            return LSHIFT_E;
                        else {
                            ungetc(c3);
                            return LSHIFT;
                        }
                    case '>' :
                        c3 = getc();
                        if (c3 == '=')
                            return RSHIFT_E;
                        else {
                            ungetc(c3);
                            return RSHIFT;
                        }
                    default :
                        break;
                    }
                else if (c2 == '=')
                    return t;
                else if (c == '-' && c2 == '>')
                    return ARROW;
            }
        }
        else if (c == '^') {
            c2 = getc();
            if (c2 == '=')
                return EXOR_E;
        }
        else if (c == '|') {
            c2 = getc();
            if (c2 == '=')
                return OR_E;
            else if (c2 == '|')
                return OROR;
        }
        else
            return c;

        ungetc(c2);
        return c;
    } // readSeparator

    private int readIdentifier(int c, Token token) throws IOException
    {
        StringBuffer tbuf = textBuffer;
        tbuf.setLength(0);

        do {
            tbuf.append((char)c);
            c = getc();
        } while ('A' <= c && c <= 'Z' || 'a' <= c && c <= 'z' || c == '_'
                 || c == '$' || '0' <= c && c <= '9');

        ungetc(c);
        int t = ktable.lookup(tbuf);
        if (t >= 0)
            return t;
        else {
            t = checkExtendedKeywords(tbuf);
            if (t >= 0)
                return t;

            /* tbuf.toString() is executed quickly since it does not
             * need memory copy.  Using a hand-written extensible
             * byte-array class instead of StringBuffer is not a good idea
             * for execution speed.  Converting a byte array to a String
             * object is very slow.  Using an extensible char array
             * might be OK.
             */
            String text = tbuf.toString().intern();
            token.textValue = text;
            ////////SF040217
            ////##19 BEGIN
            //SymbolTable lCurrentSymbolTable =
            //  hirRoot.parser.getCurrentSymbolTable();
            //Object lLocalSym = lCurrentSymbolTable.getLocal(text);
            //if (lLocalSym != null) {
            //  if (lLocalSym instanceof Declarator)
            //    return IDENTIFIER;
            //}
            ////##19 END
            //if (lookupType(text) != null)
            //  return TYPEDEF_NAME;
            //else
            //  return IDENTIFIER;
            return parser.isTypedefedType(text) ? TYPEDEF_NAME : IDENTIFIER;
            ////////SF040217
        }
    } // readIdentifier

    protected int checkExtendedKeywords(StringBuffer sbuf) {
        return -1;
    }

    private static boolean isBlank(int c) {
        return c == ' ' || c == '\t' || c == '\f' || c == '\r';
    }

    private static boolean isDigit(int c)
    {
        return '0' <= c && c <= '9';
    }

    private void ungetc(int c)
    {
        lastChar = c;
    }

    private int getc() throws IOException
    {
        if (lastChar < 0)
            return input.read();
        else {
            int c = lastChar;
            lastChar = -1;
            return c;
        }
    } // getc
}

