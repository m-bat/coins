/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins;

import coins.*;
import coins.sym.*;
import java.lang.*;
import java.util.HashSet; //##71

/** SourceLanguageFortran class
 *  Defines IR and Sym methods that depend on source language.
 *  Methods in this interface can be accessed in such way as
 *    symRoot.sourceLanguage.xxx(....).
 *  This module is implemented for C language.
**/

public class
SourceLanguageFortran extends SourceLanguage
{

public
SourceLanguageFortran( SymRoot pSymRoot )
{
  super(pSymRoot);
}

/** isC:
 *  @return true if the source language is C, false otherwise.
**/
public static boolean
isC()
{
  return false;
} // isC

/** isFortran:
 *  @return true if the source language is FORTRAN, false otherwise.
**/
public static boolean
isFortran()
{
  return true;
} // isFortran

/** makeStringBody: Make internal representation of string
 *  (pure string) from its source program representation
 *  which may differ by source language.
 *  Pure string means string body which does not contain
 *  enclosing quotation marks that may be requested by
 *  source language, and in which, a pair of escape character
 *  and special character is changed to the special character
 *  itself. In C language, trailing \0 is not included in
 *  the string body.  //##47
 *  @param pSourceString: Same to the string literal
 *      representaion in source language except that
 *      enclosing quotation marks were pealed off;
 *      If it has '"' (even at heading and trailing position),
 *      the character is treated as a part of the resultant
 *      pure string.
 *  @return internal representation of string literal excluding
 *      heading/trailing quotes and escape characters used to
 *      represent special character.
**/
public String
makeStringBody( String pSourceString )
{
  //##42 return makeStringBodyForCstatic(pSourceString);
  return makeStringBodyForC(pSourceString); //##42
} // makeStringBody

/** makeStringBodyStatic:
 *  Same as makeStringBody except that this is a static method.
**/
public static String
makeStringBodyStatic( String pSourceString )
{
  return makeStringBodyForCstatic(pSourceString);
} // makeStringBodyStatic

/** makeStringBodyForC: Make internal representation of a string
 *  (pure string) from its C language string representation.
 *  Escape characters are processed to represent proper
 *  character code.
 *  @param pCstring: String literal representaion
 *      in C language. If it has '"' (even at heading
 *      and trailing position), the character is treated
 *      as a part of the resultant pure string.
 *  @return internal representation of string literal
 *      excluding quotes and escape characters.
**/
public String
makeStringBodyForC( String pCstring )
{
  //##42 return makeStringBodyForCstatic(pCstring);
  // ast.expr.StringLiteral already made pure string. //##42
  return pCstring; //##42
} // makeStringBodyForC

/** makeStringBodyForCstatic:
 *  Same as makeStringBodyForC except that this is a static method.
**/
public static String
makeStringBodyForCstatic( String pCstring )
{
  StringBuffer lString = new StringBuffer();
  int          i, lLength, lCharCode;
  char         lChar;
  lLength = pCstring.length();
  if ((pCstring == null)||(lLength <= 0))
    return "".intern();
  if (lLength == 1)
    return pCstring.intern();
  i = 0;
  while (i < lLength) {
    lChar = pCstring.charAt(i);
    if (lChar == '\\') {
      i++;
      lChar = pCstring.charAt(i);
      switch (lChar) {
//##  case 'a' : lString.append('\u0007'); break; // alarm
      case 'b' : lString.append('\b');   break; // back space
      case 'f' : lString.append('\f');   break; // form feed
      case 'n' : lString.append('\n');   break; // new line
      case 'r' : lString.append('\r');   break; // return to col. 1
      case 't' : lString.append('\t');   break; // horisontal tab
//##  case 'v' : lString.append('\u0013'); break; // vertical tab // REFINE
      case '\\': lString.append('\\');   break; // backslash
//##  case '?' : lString.append('?');    break; // ?
      case '\'': lString.append('\'');   break; // single quote
      case '\"': lString.append('\"');   break; // double quote
      case '0':  case '1': case '2': case '3':
      case '4':  case '5': case '6': case '7':
        lCharCode = Character.digit(lChar, 8);
        lChar = pCstring.charAt(++i);
        lCharCode = lCharCode * 8 + Character.digit(lChar, 8);
        lChar = pCstring.charAt(++i);
        lCharCode = lCharCode * 8 + Character.digit(lChar, 8);
        lString.append((char)lCharCode);
        break;
      case 'x':
        lChar = pCstring.charAt(++i);
        lCharCode = Character.digit(lChar, 16);
        lChar = pCstring.charAt(++i);
        lCharCode = lCharCode * 16 + Character.digit(lChar, 16);
        lString.append((char)lCharCode);
        break;
      default:
        lString.append(lChar); //##42
        break;  // REFINE check error
      }
    }else {
      lString.append(lChar);
    }
    i++;
  }
  return lString.toString().intern();
} // makeStringBodyForCstatic

/** makeCstring:
 *  Change the pure string pStringBody to C string
 *  representation adding heading, trailing quotations and
 *  escape characters if required. (See makeStringBody.)
 *  Trailing \0 is not added as printable character.
 *  See makeCstringWithTrailing0
 *  @param pStringBody: String made by makeStringBody of
 *      coins.SourceLanguage.
 *  @return the string changed in C form.
**/
public String
makeCstring( String pStringBody )
{
  return makeCstringStatic(pStringBody);
} // makeCstring

/** makeCstringStatic:
 *  Same as makeCstring except that this is a static method.
**/
public static String
makeCstringStatic( String pStringBody )
{
  StringBuffer lString = new StringBuffer();
  int          i, lLength, lCharCode;
  char         lChar;
  lLength = pStringBody.length();
  if ((pStringBody == null)||(lLength <= 0))
    return "\"\"";
  if (lLength == 1)
    return pStringBody.intern();
  lString.append("\""); // Add heading quote.
  i = 0;
  while (i < lLength) {
    lChar = pStringBody.charAt(i);
    switch (lChar) {
//##43    case '\u0007' : lString.append("\\a"); break; // alarm. Java has no \a
    case '\u0007' : lString.append('\u0007'); break; // alarm. Java has no \a
    case '\b' : lString.append("\\b");  break; // back space
    case '\f' : lString.append("\\f");  break; // form feed
    case '\n' : lString.append("\\n");  break; // new line
    case '\r' : lString.append("\\r");  break; // return to col. 1
    case '\t' : lString.append("\\t");  break; // horisontal tab
//##43    case '\u0013' : lString.append("\\v"); break; // vertical tab. Java has no \v
    case '\u000b' : lString.append('\u000b'); break; // vertical tab. Java has no \v
//##42    case '\\':  lString.append("\\");   break; // backslash
    case '\\':  lString.append("\\\\"); break; // backslash //##42
    case '?' :  lString.append("\\?");  break; // question mark. Java has no \?
    case '\'':  lString.append("\\\'"); break; // single quote
    case '\"':  lString.append("\\\""); break; // double quote
    default:
      lString.append(lChar);
      break;
    }
    i++;
  }
  lString.append("\""); // Add trailing quote.
  return lString.toString().intern();
} // makeCstring

/** makeCstringWithTrailing0:
 *  Change the pure string pStringBody to C string
 *  representation adding heading, trailing quotations and
 *  escape characters if required. (See makeStringBody.)
 *  Trailing \0 is added as printable character with escape char.
 *  See makeCstringWithTrailing0
 *  @param pStringBody: String made by makeStringBody of
 *      coins.SourceLanguage.
 *  @return the string changed in C form with trailing \0
 *      as printable character.
**/
public String
makeCstringWithTrailing0( String pStringBody )
{
  return makeCstringWithTrailing0static(pStringBody);
} // makeCstringWithTrailing0

/** makeCstringWithTrailing0static:
 *  Same as makeCstringWithTrailing0 except that this is a static method.
**/
public static String
makeCstringWithTrailing0static( String pStringBody )
{
  String lString, lResult;
  int    lLeng;
  lString = makeCstringStatic(pStringBody);
  lLeng = lString.length();
  if ((lLeng > 2)&&
      (lString.charAt(0) == '"')&&
      (lString.charAt(lLeng-1) == '"')) {
    lResult = lString.substring(1, lLeng-1);
    lResult = "\"" + lResult + "\\0\"";
  }else {
    lResult = "\"\\0\"";
  }
  return lResult.intern();
} // makeCstringWithTrailing0

/** getSourceStringConst:
 *  Change the pure string pStringBody to a string constant
 *  in source language. (See makeStringBody.)
 *  If the source language is C then return the string constant
 *  in C form.
 *  See makeCstringWithTrailing0
 *  @param pStringBody: String made by makeStringBody of
 *      coins.SourceLanguage.
 *  @return the string changed to the form of source language
 *      string constant.
**/
public String
getSourceStringConst( String pStringBody )
{
  return makeCstringWithTrailing0static(pStringBody);
}

/** getSourceStringConstStatic:
 *  Same as getSourceStringConst except that this is a static method.
public static String
getSourceStringConstStatic( String pStringBody )
{
  return makeCstringWithTrailing0static(pStringBody);
}

/** getStringLength:
 *  Get the length of string constant.
 *  @param pStringBody: pure character string excluding
 *      escape characters and quotation mark even if the source
 *      language requires them in writing string constant.
 *  @return the length of pStringBody.
 *      In C language, trailing \0 is counted as 1.
**/
public int
getStringLength( String pStringBody )
{
  return pStringBody.length() + 1;
}

/** makeIntConstString: //##20
 *  Make the string representation of integer constant
 *  in the form of source language constant.
 *  For C language,
 *  supply L for long int, UL for unsigned long int,
 *  U for unsigned int, U for unsigned short,
 *  LL for long long int, ULL for unsigned long long int //##21
 *  if the last character
 *  of given constant is a digit.
 *  @param pInternedConstString: integer constant in string form.
 *  @param pType: type of the constant.
 *  @return the resultant constant in interned string form.
 */
public static String
makeIntConstString( String pInternedConstString, Type pType )
{
  String lConstString = pInternedConstString;
  int lEndPos = lConstString.length()-1;
  if ((pType != null)&&
      Character.isDigit(lConstString.charAt(lEndPos))) {
    switch (pType.getTypeKind()) {
      case Type.KIND_U_SHORT:
        lConstString = (lConstString + "U").intern();
        break;
      case Type.KIND_U_INT:
        lConstString = (lConstString + "U").intern();
        break;
      case Type.KIND_LONG:
        lConstString = (lConstString + "L").intern();
        break;
      case Type.KIND_U_LONG:
        lConstString = (lConstString + "UL").intern();
        break;
      case Type.KIND_LONG_LONG:
        lConstString = (lConstString + "LL").intern();
        break;
      case Type.KIND_U_LONG_LONG:
        lConstString = (lConstString + "ULL").intern();
        break;
      default:
    }
  }
  return lConstString;
} // makeIntConstString

/** getPureIntString: //##20
 *  Get the string representation of integer constant
 *  composed of only digits.
 *  For C language, peal off trailing U, u, L, l, UL, ul,
 *  ULL, ull. //##21
 *  @param pInternedName: integer constant in string form.
 *  @param pType: type of the constant.
 *  @return the resultant constant in interned string form.
 */
public static String
getPureIntString( String pInternedName )
{
  String lConstString = pInternedName;
  int lEndPos = lConstString.length() - 1;
  if (! Character.isDigit(lConstString.charAt(lEndPos))) {
    if ((lConstString.charAt(lEndPos) == 'L')||
        (lConstString.charAt(lEndPos) == 'l'))
      lEndPos = lEndPos - 1;
    if ((lConstString.charAt(lEndPos) == 'L')||
        (lConstString.charAt(lEndPos) == 'l'))
      lEndPos = lEndPos - 1;
    if ((lConstString.charAt(lEndPos) == 'U')||
        (lConstString.charAt(lEndPos) == 'u'))
      lEndPos = lEndPos - 1;
    lConstString = lConstString.substring(0, lEndPos+1).intern();
  }
  return lConstString;
} // getPureIntString

/** makeFloatConstString: //##21
 *  Make the string representation of floating constant
 *  in the form of source language constant.
 *  For C language,
 *  supply D for double, F for float,
 *  if the last character of given constant is a digit or period.
 *  @param pInternedConstString: floating constant in string form.
 *  @param pType: type of the constant.
 *  @return the resultant constant in interned string form.
 */
public static String
makeFloatConstString( String pInternedConstString, Type pType )
{
  String lConstString = pInternedConstString;
  int lEndPos = lConstString.length()-1;
  char lLastChar = lConstString.charAt(lEndPos);
  if ((pType != null)&&
      (Character.isDigit(lLastChar)||lLastChar == '.')) {
    if (pType.getTypeKind() == Type.KIND_FLOAT ) {
        lConstString = (lConstString + "F").intern();
    }else if (pType.getTypeKind() == Type.KIND_LONG_DOUBLE ) {
        lConstString = (lConstString + "D").intern();
    }
  }
  return lConstString;
} // makeFloatConstString

/** getPureFloatString: //##21
 *  Get the string representation of floating constant
 *  without language specific suffix.
 *  For C language, peal off trailing F and D.
 *  @param pInternedName: floating constant in string form.
 *  @param pType: type of the constant.
 *  @return the resultant constant in interned string form.
 */
public static String
getPureFloatString( String pInternedName )
{
  String lConstString = pInternedName;
  int lEndPos = lConstString.length() - 1;
  char lLastChar = lConstString.charAt(lEndPos);
  if ((! Character.isDigit(lLastChar))&&
      (lLastChar != '.')) {
    if ((lLastChar == 'F')||(lLastChar == 'D'))
      lEndPos = lEndPos - 1;
    lConstString = lConstString.substring(0, lEndPos+1).intern();
  }
  return lConstString;
} // getPureFloatString

/** baseTypeOrigin: get origin of given base type pBaseType.
 *  @param pBaseType: base type.
 *  @return the origin type of pBaseType.
**/
public Type
baseTypeOrigin( Type pBaseType )
{
  Type lOrigin;
  switch (pBaseType.getTypeKind()) {
  case Type.KIND_BOOL    : lOrigin = symRoot.typeInt;  break;
//  case Type.KIND_S_CHAR  : lOrigin = symRoot.typeInt; break; //##10
  case Type.KIND_CHAR    : lOrigin = symRoot.typeInt; break;
  case Type.KIND_U_CHAR  : lOrigin = symRoot.typeU_Int; break;
  case Type.KIND_ADDRESS :
  case Type.KIND_OFFSET  : lOrigin = symRoot.typeU_Long; break;
  case Type.KIND_POINTER : lOrigin = symRoot.typeU_Long; break;
  case Type.KIND_ENUM    : lOrigin = symRoot.typeInt; break;
  case Type.KIND_STRING  :
  default:  lOrigin = pBaseType; break;
  }
  return lOrigin;
} // baseTypeOrigin

/** isMainProgram:
 *  @return true if pSubp is a main program, false otherwise.
**/
public boolean
isMainProgram( Subp pSubp )
{
  if ((pSubp != null)&&
      (pSubp.getName().intern() == "main".intern()))
    return true;
  else
    return false;
} // isMainProgram

  // Redefinable combination of symbols having the same spelling.
  // (REDEFINABLE[i][j]=1: i is redefinable as j, =<1: unallowable,
  //  where, i, j are symbol kind such as Sym.KIND_VAR,
  //  Sym.KIND_SUBP, etc.)
  public static final int
    REDEFINABLE[][] = {
//r ot bc cc ic fc sc nc var par elm tag sub typ lbl arg mrg xid
 {0,0, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0},//remove
 {0,1, 1, 1, 1, 1, 1, 1, 1,  1,  1,  1,  1,  1,  1,  1,  1,  1},//other
 {0,0, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0},//bc
 {0,0, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0},//cc
//{0,0,0, 0, 1, 0, 0, 0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0},//cc//Fortran
 {0,0, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0},//ic
//{0,0,0, 1, 0, 0, 0, 0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0},//ic//Fortran
 {0,0, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0},//fc
 {0,0, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0},//sc
 {0,0, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0},//nc
 {0,0, 0, 0, 0, 0, 0, 0, 0,  0,  1,  1,  0,  0,  1,  0,  0,  0},//var
 {0,0, 0, 0, 0, 0, 0, 0, 0,  0,  1,  1,  0,  0,  1,  0,  0,  0},//par
 {0,0, 0, 0, 0, 0, 0, 1, 1,  1,  0,  1,  1,  1,  1,  0,  0,  0},//elm
 {0,0, 0, 0, 0, 0, 0, 1, 1,  1,  1,  0,  1,  1,  1,  0,  0,  0},//tag
 {0,0, 0, 0, 0, 0, 0, 0, 0,  0,  1,  1,  0,  0,  1,  0,  0,  0},//sub
 {0,0, 0, 0, 0, 0, 0, 0, 0,  0,  1,  1,  0,  0,  1,  0,  0,  0},//typ
 {0,0, 0, 0, 0, 0, 0, 1, 1,  1,  1,  1,  1,  1,  0,  0,  0,  0},//lbl
 {0,0, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0},//reg
 {0,0, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0},//mrg
 {0,0, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0} //xid
 };

  // Conversion check matrix
  // (CONVERTIBLE[i][j]=1: i is convertible to j, 0: not convertible,
  //  where, i, j are type kind such as Type.KIND_INT, KIND_FLOAT,
  //  etc.)
  public static final int
    CONVERTIBLE[][] = {
//ud      i   ll  uc  ui  ull of  f   ld  sg  pt  st  df
//  bo  sh  l   ch  us  ul  ad  vo  d       en  ve  un  sp
 {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},//undef
 {0,1,0,1,1,1,1,1,1,1,1,1,1,1,1,0,1,1,1,0,0,1,0,0,0,0,0,0},//BOOL
 {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},//
 {0,1,0,1,1,1,1,1,1,1,1,1,1,1,1,0,1,1,1,0,0,1,0,0,0,0,0,0},//SHORT
 {0,1,0,1,1,1,1,1,1,1,1,1,1,1,1,0,1,1,1,0,0,1,0,0,0,0,0,0},//INT
 {0,1,0,1,1,1,1,1,1,1,1,1,1,1,1,0,1,1,1,0,0,1,0,0,0,0,0,0},//LONG
 {0,1,0,1,1,1,1,1,1,1,1,1,1,1,1,0,1,1,1,0,0,1,0,0,0,0,0,0},//LONG_LONG
 {0,1,0,1,1,1,1,1,1,1,1,1,1,1,1,0,1,1,1,0,0,1,0,0,0,0,0,0},//CHAR
 {0,1,0,1,1,1,1,1,1,1,1,1,1,1,1,0,1,1,1,0,0,1,0,0,0,0,0,0},//U_CHAR
 {0,1,0,1,1,1,1,1,1,1,1,1,1,1,1,0,1,1,1,0,0,1,0,0,0,0,0,0},//U_SHORT
 {0,1,0,1,1,1,1,1,1,1,1,1,1,1,1,0,1,1,1,0,0,1,0,0,0,0,0,0},//U_INT
 {0,1,0,1,1,1,1,1,1,1,1,1,1,1,1,0,1,1,1,0,0,1,0,0,0,0,0,0},//U_LONG
 {0,1,0,1,1,1,1,1,1,1,1,1,1,1,1,0,1,1,1,0,0,1,0,0,0,0,0,0},//U_LONG_LONG
 {0,1,0,1,1,1,1,1,1,1,1,1,1,1,1,0,1,1,1,0,0,1,0,0,0,0,0,0},//ADDRESS
 {0,1,0,1,1,1,1,1,1,1,1,1,1,1,1,0,1,1,1,0,0,1,0,0,0,0,0,0},//OFFSET
 {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0},//VOID
 {0,1,0,1,1,1,1,1,1,1,1,1,1,1,1,0,1,1,1,0,0,1,0,0,0,0,0,0},//FLOAT
 {0,1,0,1,1,1,1,1,1,1,1,1,1,1,1,0,1,1,1,0,0,1,0,0,0,0,0,0},//DOUBLE
 {0,1,0,1,1,1,1,1,1,1,1,1,1,1,1,0,1,1,1,0,0,1,0,0,0,0,0,0},//L_DOUBLE
 {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},//
 {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0},//STRING
 {0,1,0,1,1,1,1,1,1,1,1,1,1,1,1,0,1,1,1,0,0,1,0,0,0,0,0,0},//ENUM
 {0,1,0,1,1,1,1,1,1,1,1,1,1,1,1,0,1,1,1,0,0,1,0,0,0,0,0,0},//PTR
 {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},//VECT
 {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},//STRUCT
 {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},//UNION
 {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},//DEFINED
 {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},//SUBP
 };

/** subscriptWithIndex:
 *  Temporal parameter to be removed.
 *  true: generate subs exp of HIR with index operand.
 *  false: generate subs exp of HIR without index operand.
 */
 public static final boolean  //##26
    subscriptWithIndex = false;
//##35    subscriptWithIndex = true;  //##28

//##71 BEGIN
  public void
  initiateFunctionsWithoutSideEffect()
{
  functionsWithoutSideEffect = new HashSet();
  functionsWithoutSideEffect.add("EXP");
  functionsWithoutSideEffect.add("DEXP");
  functionsWithoutSideEffect.add("CEXP");
  functionsWithoutSideEffect.add("ALOG");
  functionsWithoutSideEffect.add("DLOG");
  functionsWithoutSideEffect.add("CLOG");
  functionsWithoutSideEffect.add("ALOG10");
  functionsWithoutSideEffect.add("DLOG10");
  functionsWithoutSideEffect.add("SIN");
  functionsWithoutSideEffect.add("DSIN");
  functionsWithoutSideEffect.add("CSIN");
  functionsWithoutSideEffect.add("COS");
  functionsWithoutSideEffect.add("DCOS");
  functionsWithoutSideEffect.add("CCOS");
  functionsWithoutSideEffect.add("TAN");
  functionsWithoutSideEffect.add("DTAN");
  functionsWithoutSideEffect.add("ASIN");
  functionsWithoutSideEffect.add("DASIN");
  functionsWithoutSideEffect.add("ACOS");
  functionsWithoutSideEffect.add("DACOS");
  functionsWithoutSideEffect.add("ATAN");
  functionsWithoutSideEffect.add("DATAN");
  functionsWithoutSideEffect.add("ATAN2");
  functionsWithoutSideEffect.add("DATAN2");
  functionsWithoutSideEffect.add("SINH");
  functionsWithoutSideEffect.add("DSINH");
  functionsWithoutSideEffect.add("COSH");
  functionsWithoutSideEffect.add("DCOSH");
  functionsWithoutSideEffect.add("TANH");
  functionsWithoutSideEffect.add("DTANH");
  functionsWithoutSideEffect.add("SQRT");
  functionsWithoutSideEffect.add("DSQRT");
  functionsWithoutSideEffect.add("CSQRTZ");
} // initiateFunctionsWithoutSideEffect

//##71 END

} // SourceLanguageFortran class
