package coins.ffront;

%%

%public
%class	Scanner
%implements  Parser.yyInput

%type	int
%eofval{
	return YYEOF;
%eofval}

%{

    private int token;
    private StringBuffer format_string;
    private int format_h_num;
    private int paren_level;

    /** move on to next token.
        @returns false if positioned beyond tokens.
        @throws IOException on input error.
      */
    public boolean advance () throws java.io.IOException {
	  token=yylex();
	  return token != YYEOF;
    }

    /** classifies current token.
        Should not be called if advance() returned false.
        @returns current %token or single character.
      */
    public int token () {
	  dp("TK: " + value);
	  return token;
    }

    protected void dp(String msg){
	//
    }

    /** associated with current token.
        Should not be called if advance() returned false.
        @returns value for token().
      */
    public Object value () {
	  return value;
    }

    Object value;
    int lineNo = 1;
%}


newline = \n
ws      = [\ \t\012\015]
symbol  = [=(),+*/:]
digit   = [0-9]
digits  = {digit}+
letter  = [a-zA-Z]
id      = {letter}({letter}|{digit})*

double  = ({digits}"."{digits}|{digits}"."|"."{digits}|{digits})D([+-]?{digits})

real    = ({digits}"."{digits}|"."{digits})
real2   = ({digits}"."{digits}|{digits}"."|"."{digits}|{digits})E([+-]?{digits})
real3   = {digits}"."[^LGENOA0-9ED][^Q]

string1 = "'"[^']*"'"
string2 = "\""[^\"]*"\""

string  = ({string1}+|{string2}+)
other   = .
format_H= {digits}"H"

%state FORMAT
%state FORMAT_H


%%
<YYINITIAL>{

{newline}  { lineNo++; }
             
{ws}       { }
{symbol}   { value = new Token(lineNo, yytext().intern()); return yytext().charAt(0); }
"-"        { value = new Token(lineNo, yytext().intern()); return yytext().charAt(0); }
"**"       { value = new Token(lineNo, yytext().intern());  return Parser.POWER; }
"//"       { value = new Token(lineNo, yytext().intern());  return Parser.DOUBLE_SLASH; }

"<"        { value = new Token(lineNo, yytext().intern(), Parser.LESS_THAN);     return Parser.LESS_THAN; }
".LT."     { value = new Token(lineNo, yytext().intern(), Parser.LESS_THAN);     return Parser.LESS_THAN; }

"<="       { value = new Token(lineNo, yytext().intern(), Parser.LESS_OR_EQUAL); return Parser.LESS_OR_EQUAL; }
".LE."     { value = new Token(lineNo, yytext().intern(), Parser.LESS_OR_EQUAL); return Parser.LESS_OR_EQUAL; }

">"        { value = new Token(lineNo, yytext().intern(), Parser.GREATER_THAN);  return Parser.GREATER_THAN; }
".GT."     { value = new Token(lineNo, yytext().intern(), Parser.GREATER_THAN);  return Parser.GREATER_THAN; }

">="       { value = new Token(lineNo, yytext().intern(), Parser.GREATER_OR_EQUAL); return Parser.GREATER_OR_EQUAL; }
".GE."     { value = new Token(lineNo, yytext().intern(), Parser.GREATER_OR_EQUAL); return Parser.GREATER_OR_EQUAL; }

"=="       { value = new Token(lineNo, yytext().intern(), Parser.EQUAL); return Parser.EQUAL; }
".EQ."     { value = new Token(lineNo, yytext().intern(), Parser.EQUAL); return Parser.EQUAL; }

"!="       { value = new Token(lineNo, yytext().intern(), Parser.NOT_EQUAL);     return Parser.NOT_EQUAL; }
".NE."     { value = new Token(lineNo, yytext().intern(), Parser.NOT_EQUAL);     return Parser.NOT_EQUAL; }

".AND."    { value = new Token(lineNo, yytext().intern(), Parser.AND); return Parser.AND; }
".OR."     { value = new Token(lineNo, yytext().intern(), Parser.OR);  return Parser.OR; }
".NOT."    { value = new Token(lineNo, yytext().intern(), Parser.NOT); return Parser.NOT; }
".EQV."    { value = new Token(lineNo, yytext().intern(), Parser.EQV); return Parser.EQV; }
".NEQV."   { value = new Token(lineNo, yytext().intern(), Parser.NEQV);return Parser.NEQV; }

// line label

"#"         { value = new Token(lineNo, "#"); return Parser.NO_LABEL; }
"@"{digits} { value = new Token(lineNo, yytext().substring(1).intern(), 
              Parser.LABEL_DEF); return Parser.LABEL_DEF; }

";"         { value = new Token(lineNo, ";"); return Parser.EOS; }


// consts
{digits}   { value = new Token(lineNo, yytext().intern(), Parser.INT_CONST); 
                       return Parser.INT_CONST; }
{real}     { value = new Token(lineNo, yytext().intern(), Parser.REAL_CONST); 
                       return Parser.REAL_CONST; }
{real2}    { value = new Token(lineNo, yytext().intern(), Parser.REAL_CONST); 
                       return Parser.REAL_CONST; }
// real constants formed : r.
{real3}    { yypushback(2) ; value = new Token(lineNo, yytext().intern(), Parser.REAL_CONST); 
                       return Parser.REAL_CONST; }

{double}   { value = new Token(lineNo, yytext().intern(), Parser.DOUBLE_CONST); 
                       return Parser.DOUBLE_CONST; }

".TRUE."   { value = new Token(lineNo, yytext().intern(),Parser.TRUE_CONST);
                       return Parser.TRUE_CONST; }
".FALSE."  { value = new Token(lineNo, yytext().intern(),Parser.FALSE_CONST);
                       return Parser.FALSE_CONST; }



"_let"      { value = new Token(lineNo, yytext().intern()); 
                       return Parser.LET; }
"_INTEGER"  { value = new Token(lineNo, yytext().intern(), Parser.INTEGER); 
                       return Parser.INTEGER; }
"_REAL"     { value = new Token(lineNo, yytext().intern(), Parser.REAL); 
                       return Parser.REAL; }
"_DOUBLEPRECISION" { value = new Token(lineNo, yytext().intern(), Parser.DOUBLE_PREC); 
                       return Parser.DOUBLE_PREC; }
"_LOGICAL"  { value = new Token(lineNo, yytext().intern(), Parser.LOGICAL); 
                       return Parser.LOGICAL; }
"_COMPLEX"  { value = new Token(lineNo, yytext().intern(), Parser.COMPLEX); 
                       return Parser.COMPLEX; }
"_CHARACTER" { value = new Token(lineNo, yytext().intern(), Parser.CHARACTER); 
                       return Parser.CHARACTER; }
"_DIMENSION" { value = new Token(lineNo, yytext().intern(), Parser.DIMENSION); 
                       return Parser.DIMENSION; }
"_COMMON"   { value = new Token(lineNo, yytext().intern());
                       return Parser.COMMON; }
"_DATA"     { value = new Token(lineNo, yytext().intern()); 
                       return Parser.DATA; }
"_PARAMETER" { value = new Token(lineNo, yytext().intern()); 
                       return Parser.PARAM; }
"_EQUIVALENCE" { value = new Token(lineNo, yytext().intern()); 
                       return Parser.EQUIVALENCE; }
"_EXTERNAL" { value = new Token(lineNo, yytext().intern()); 
                       return Parser.EXTERNAL; }

"_FORMAT"   { value = new Token(lineNo, yytext().intern());
              format_string = new StringBuffer();
              yybegin(FORMAT);
              return Parser.FORMAT; }

"_IMPLICIT" { value = new Token(lineNo, yytext().intern()); 
                       return Parser.IMPLICIT; }
"_SAVE"     { value = new Token(lineNo, yytext().intern()); 
                       return Parser.SAVE; }
"_INTRINSIC" { value = new Token(lineNo, yytext().intern()); 
                       return Parser.INTRINSIC; }
"_PROGRAM"  { value = new Token(lineNo, yytext().intern(), Parser.PROGRAM); 
                       return Parser.PROGRAM; }
"_FUNCTION" { value = new Token(lineNo, yytext().intern(), Parser.FUNCTION); 
                       return Parser.FUNCTION; }
"_ENTRY"    { value = new Token(lineNo, yytext().intern(), Parser.ENTRY); 
                       return Parser.ENTRY; }
"_SUBROUTINE" { value = new Token(lineNo, yytext().intern(), Parser.SUBROUTINE); 
                       return Parser.SUBROUTINE; }
"_BLOCKDATA"  { value = new Token(lineNo, yytext().intern(), Parser.BLOCKDATA); 
                       return Parser.BLOCKDATA; }
"_RETURN"   { value = new Token(lineNo, yytext().intern()); 
                       return Parser.RETURN; }
"_CALL"     { value = new Token(lineNo, yytext().intern()); 
                       return Parser.CALL; }
"_END"      { value = new Token(lineNo, yytext().intern()); 
                       return Parser.END; }
"_CONTINUE" { value = new Token(lineNo, yytext().intern()); 
                       return Parser.CONTINUE; }
"_IF"       { value = new Token(lineNo, yytext().intern()); 
                       return Parser.IF; }
"_THEN"     { value = new Token(lineNo, yytext().intern()); 
                       return Parser.THEN; }
"_ELSE"     { value = new Token(lineNo, yytext().intern()); 
                       return Parser.ELSE; }
"_ENDIF"    { value = new Token(lineNo, yytext().intern()); 
                       return Parser.END_IF; }
"_ELSEIF"   { value = new Token(lineNo, yytext().intern()); 
                       return Parser.ELSE_IF; }
"_ENDDO"    { value = new Token(lineNo, yytext().intern());
                       return Parser.END_DO; }
"_DO"       { value = new Token(lineNo, yytext().intern()); 
                       return Parser.DO; }
"_GOTO"     { value = new Token(lineNo, yytext().intern()); 
                       return Parser.GOTO; }
"_END_DO"   { value = new Token(lineNo, yytext().intern()); 
                       return Parser.END_DO; }
"_ARITH_IF" { value = new Token(lineNo, yytext().intern()); 
                       return Parser.ARITH_IF; }
"_ASSIGN"   { value = new Token(lineNo, yytext().intern()); 
                       return Parser.ASSIGN; }
"_TO"       { value = new Token(lineNo, yytext().intern()); 
                       return Parser.TO; }
"_ASSIGN_GOTO" { value = new Token(lineNo, yytext().intern()); 
                       return Parser.ASSIGN_GOTO; }
"_COMP_GOTO" { value = new Token(lineNo, yytext().intern()); 
                       return Parser.COMP_GOTO; }

// I/O statement
"_READ"     { value = new Token(lineNo, yytext().intern()); 
                       return Parser.READ; }
"_WRITE"    { value = new Token(lineNo, yytext().intern()); 
                       return Parser.WRITE; }
"_PRINT"    { value = new Token(lineNo, yytext().intern());
                       return Parser.PRINT; }

"_OPEN"     { value = new Token(lineNo, yytext().intern());
                       return Parser.OPEN; }
"_CLOSE"    { value = new Token(lineNo, yytext().intern());
                       return Parser.CLOSE; }

"_STOP"     { value = new Token(lineNo, yytext().intern());
              return Parser.STOP; }
"_PAUSE"    { value = new Token(lineNo, yytext().intern());
              return Parser.PAUSE; }
"_INQUIRE"  { value = new Token(lineNo, yytext().intern());
              return Parser.INQUIRE; }
"_BACKSPACE" { value = new Token(lineNo, yytext().intern());
              return Parser.BACKSPACE; }
"_REWIND"   { value = new Token(lineNo, yytext().intern());
              return Parser.REWIND; }
"_ENDFILE"  { value = new Token(lineNo, yytext().intern());
              return Parser.END_FILE; }

{string}   { String str = yytext();
             str = str.substring(1, str.length() - 1);
             int i;
             while((i = str.indexOf("''")) != -1){
               str = str.substring(0,i) + str.substring(i+1);
             }
             value = new Token(lineNo, str.intern(), Parser.CHAR_CONST);
             return Parser.CHAR_CONST; }

{id}       { value = new Token(lineNo, yytext().toLowerCase().intern(),
                               Parser.IDENT);
             return Parser.IDENT; }

{other}    { System.err.println("Illegal char " + yytext().charAt(0) + " ignored"); }
}

<FORMAT>{
	{string}   {format_string.append(yytext());}
	{format_H} {
	  // numHstr
	  String text = yytext();
	  format_h_num = Integer.parseInt(text.substring(0, text.indexOf('H')));
	  format_string.append(text);
	  yybegin(FORMAT_H);
	}
	
	")" {
          paren_level--;
          format_string.append(yytext());
          if(paren_level == 0){
            yybegin(YYINITIAL);
            value = new Token(lineNo, format_string.toString(), Parser.FORMAT_SPECIFICATION);
            return Parser.FORMAT_SPECIFICATION;
          }
          else if(paren_level < 0){
            // error
          }
        }

    "(" {
      paren_level++;
      format_string.append(yytext());
    }

    [^\(\)] { format_string.append(yytext()); }
}


<FORMAT_H>{
  . {
    format_string.append(yytext());
    format_h_num--;
    if(format_h_num == 0){
      yybegin(FORMAT);
    }
  }
}  

