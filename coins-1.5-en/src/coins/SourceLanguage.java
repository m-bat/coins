/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins;

import java.math.BigInteger;
import java.util.HashSet;  //##71
import java.util.Set;      //##71
import java.util.StringTokenizer;

import coins.sym.Const;
import coins.sym.FloatConst;
import coins.sym.IntConst;
import coins.sym.Subp;
import coins.sym.Type;

/** SourceLanguage class
 *  Defines IR and Sym methods that depend on source language.
 *  Methods in this interface can be accessed in such way as
 *    symRoot.sourceLanguage.xxx(....).
 *  This is the super class for SourceLanguageC, SourceLanguageFortran, etc.
**/

public class
SourceLanguage
{

  public final SymRoot
    symRoot;

//##71 BEGIN
  /**
   * Set of the names of functions that have no side effect
   * (do not write to memory).
   * This may be changed by directives such as pragma
   * and can be restored to the initial state by calling
   * initiateFunctionsWithoutSideEffect().
   */
  public Set
    functionsWithoutSideEffect;
//##71 END

  //SF050111[
  private boolean evalFloat = false;
  private static final BigInteger longMask =
    BigInteger.valueOf(-1).shiftLeft(64).not();
  //SF050111]

public
SourceLanguage( SymRoot pSymRoot )
{
  symRoot = pSymRoot;
  //SF050111[
  String hiropt = symRoot.ioRoot.
    getCompileSpecification().getCoinsOptions().getArg("hirOpt");
  if( hiropt!=null )
    for( StringTokenizer st = new StringTokenizer(hiropt,"/");
         st.hasMoreTokens(); )
      if( st.nextToken().equals("evalFloat") )
        evalFloat = true;
  //SF050111]
  initiateFunctionsWithoutSideEffect(); //##71
}

/** isC:
 *  @return true if the source language is C, false otherwise.
**/
public static boolean
isC()
{
  return true;
} // isC

/** isFortran:
 *  @return true if the source language is FORTRAN, false otherwise.
**/
public static boolean
isFortran()
{
  return false;
} // isFortran

//##87 BEGIN
 /**
  * Get the number of addressing units for the character
  * string whose element count is given by elemCount.
  * If the character string contains terminator (such as
  * \0 in C string) then it is counted in giving elemCount.
  * Thus, in C language, the elemCount for "", "a", "abc"
  * is 1, 2, 4, each respecrtively.
  * For the language that represents a character in one
  * addressing unit (may be a byte), the returned value will be
  *   elemCount*machineParam.evaluateSize(Type.KIND_CHAR).
  * For the language (and word addressing machine)
  * that packs several characters in one addressing unit
  * (may be a word), the returned value will be different.
  * Such computation should be given in a subclass
  * corresponding to the language.
  * Example:
  *   Consider that character code is represented in 8 bit and
  *   character strings declared as
  *     packed array[1..n] of char
  *   are to be implemented on a 32-bit word addressing machine
  *   where the top position of the character strings should
  *   start at 32-bit word boundary. Then this function may
  *   return the value
  *     (elemCount * 8 + 31)/32
  *   as the number of 32-bit words required to represent
  *   character strings.
  * @param machineParam MachineParam instance.
  * @param elemCount number of characters in the string.
  * @return the number of addressing units.
  */
public int
numberOfAddressingUnitsForCharString(
       MachineParam machineParam, int elemCount )
{
  //##88 return elemCount * machineParam.SIZEOF_CHAR;
  return elemCount * machineParam.evaluateSize(Type.KIND_CHAR);
  /*
  In general, returned value will be
    (elemCount * machineParam.NUMBER_OF_BITS_IN_PACKED_CHAR
      + machineParam.numberOfBitsInAddressingUnit() - 1)
      / machineParam.numberOfBitsInAddressingUnit();
  if the character string is to be aligned to the addressing
  unit and the minimum number of bits required to represent
  a packed character is shown by numberOfBitsInPackedChar().
  */
}

/**
 * Get the number of addressing units for the short int
 * array whose element count is given by elemCount.
 * For the language that represents a short int value by using one
 * or more addressing units (may be bytes), the returned value
 * will be elemCount*evaluateSize(Type.KIND_SHORT). For the language (and
 * word addressing machine) that packs several short int values
 * in one addressing unit (may be a word), the returned
 * value will be different. Such computation should be
 * given in a subclass corresponding to the language.
 * Example:
 *   Consider that short int is represented in 16 bit and
 *   short int arrays declared as
 *     packed array[1..n] of short int
 *   are to be implemented on a 32-bit word addressing machine
 *   where the top position of the short int arrays should
 *   start at 32-bit word boundary. Then this function may return
 *   the value
 *     (elemCount * 16 + 31)/32
 *   as the number of 32-bit words required to represent
 *   packed short int arrays.
 * @param machineParam MachineParam instance.
 * @param elemCount number of short int elements in the array.
 * @return the number of addressing units.
 */

public int
numberOfAddressingUnitsForShortArray(
      MachineParam machineParam, int elemCount )
{
  //##88 return elemCount * machineParam.SIZEOF_SHORT;
  return elemCount * machineParam.evaluateSize(Type.KIND_SHORT);
  /*
  In general, returned value will be
    (elemCount * machineParam.numberOfBitsInPackedShort()
      + machineParam.numberOfBitsInAddressingUnit() - 1)
      / machineParam.numberOfBitsInAddressingUnit();
  if the short int array is to be aligned to the addressing
  unit and the minimum number of bits required to represent
  a packed short int data is shown by numberOfBitsInPackedShort().
  */
}

//##87 END

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

//SF050111[
/**
 * Evaluate negate operation.
 * The evaluation conforms to C language.
 * @param  c1  constant operand
 * @return     constant resultant or null(when failing in the evaluation)
**/
public Const evaluateNeg(Const c1) // unary -
{
  Type t = c1.getSymType();
  if (t.getSizeValue() > 8) //##81
    return null; //##81
  if( t.isInteger() ) {
    //##81 BEGIN
    if (t.isUnsigned()) {
      long lComplement = ~c1.longValue();
      return newIntConst( lComplement + 1, getIpType(t) );
    }else
    //##81 END
      return newIntConst( -c1.longValue(), getIpType(t) );
  }else
    //if( evalFloat ) //evaluate unary - always.
      return newFloatConst( -c1.doubleValue(), t );
  //return null;
}

/**
 * Evaluate add operation.
 * The evaluation conforms to C language.
 * @param  c1  constant operand
 * @param  c2  constant operand
 * @return     constant resultant or null(when failing in the evaluation)
**/
public Const evaluateAdd(Const c1,Const c2) // +
{
  Type t = getUacType(c1,c2);
  if( t!=null ) {
    if (t.getSizeValue() > 8) //##81
      return null; //##81
    if( t.isInteger() )
      return newIntConst( c1.longValue()+c2.longValue(), t );
    else
      if( evalFloat )
        return newFloatConst( c1.doubleValue()+c2.doubleValue(), t );
  }
  return null;
}

/**
 * Evaluate subtract operation.
 * The evaluation conforms to C language.
 * @param  c1  constant operand
 * @param  c2  constant operand
 * @return     constant resultant or null(when failing in the evaluation)
**/
public Const evaluateSub(Const c1,Const c2) // -
{
  Type t = getUacType(c1,c2);
  if( t!=null ) {
    if (t.getSizeValue() > 8) //##81
      return null; //##81
    if( t.isInteger() )
      return newIntConst( c1.longValue()-c2.longValue(), t );
    else
      if( evalFloat )
        return newFloatConst( c1.doubleValue()-c2.doubleValue(), t );
  }
  return null;
}

/**
 * Evaluate multiply operation.
 * The evaluation conforms to C language.
 * @param  c1  constant operand
 * @param  c2  constant operand
 * @return     constant resultant or null(when failing in the evaluation)
**/
public Const evaluateMult(Const c1,Const c2) // *
{
  Type t = getUacType(c1,c2);
  if( t!=null ) {
    if (t.getSizeValue() > 8) //##81
      return null; //##81
    if( t.isInteger() )
      return newIntConst( c1.longValue()*c2.longValue(), t );
    else
      if( evalFloat )
        return newFloatConst( c1.doubleValue()*c2.doubleValue(), t );
  }
  return null;
}

/**
 * Evaluate divide operation.
 * The evaluation conforms to C language.
 * @param  c1  constant operand
 * @param  c2  constant operand
 * @return     constant resultant or null(when failing in the evaluation)
**/
public Const evaluateDiv(Const c1,Const c2) // /
{
  if( c2.doubleValue()==0 )
    return null;
  Type t = getUacType(c1,c2);
  if( t!=null ) {
    if (t.getSizeValue() > 8) //##81
      return null; //##81
    if (t.isInteger()) {
      long l1 = newBigInteger(c1).divide(newBigInteger(c2)).longValue();
      return newIntConst(
        newBigInteger(c1).divide(newBigInteger(c2)).longValue(),
        t);
    }
    else {
      if (evalFloat)
        return newFloatConst(c1.doubleValue() / c2.doubleValue(), t);
    }
  }
  return null;
}

/**
 * Evaluate remainder operation.
 * The evaluation conforms to C language.
 * @param  c1  constant operand
 * @param  c2  constant operand
 * @return     constant resultant or null(when failing in the evaluation)
**/
public Const evaluateMod(Const c1,Const c2) // %
{
  if( c2.doubleValue()==0 )
    return null;
  Type t = getUacType(c1,c2);
  if( t!=null ) {
    if (t.getSizeValue() > 8) //##81
      return null; //##81
    if (t.isInteger())
      return newIntConst(
        newBigInteger(c1).remainder(newBigInteger(c2)).longValue(),
        t);
  }
  return null;
}

/**
 * Evaluate not operation.
 * The evaluation conforms to C language.
 * @param  c1  constant operand
 * @return     constant resultant or null(when failing in the evaluation)
**/
public Const evaluateNot(Const c1) // ~
{
  Type t = c1.getSymType();
  if( t.isInteger() ) {
    if (t.getSizeValue() <= 8) //##81
      return newIntConst(~c1.longValue(), getIpType(t));
  }
  return null;
}

/**
 * Evaluate logical left shift operation.
 * The evaluation conforms to C language.
 * @param  c1  constant operand
 * @param  c2  constant operand
 * @return     constant resultant or null(when failing in the evaluation)
**/
public Const evaluateShiftLl(Const c1,Const c2) // <<
{
  Type t = c1.getSymType();
  if( t.isInteger() && c2.getSymType().isInteger() ) {
    if (t.getSizeValue() <= 8) //##81
      return newIntConst( c1.longValue()<<c2.longValue(), getIpType(t) );
  }
  return null;
}

/**
 * Evaluate logical right shift operation.
 * The evaluation conforms to C language.
 * @param  c1  constant operand
 * @param  c2  constant operand
 * @return     constant resultant or null(when failing in the evaluation)
**/
public Const evaluateShiftRl(Const c1,Const c2) // >>>
{
  Type t = c1.getSymType();
  if( t.isInteger() && c2.getSymType().isInteger() ) {
    if (t.getSizeValue() <= 8) //##81
      return newIntConst( c1.longValue()>>>c2.longValue(), getIpType(t) );
  }
  return null;
}

/**
 * Evaluate arithmetic right shift operation.
 * The evaluation conforms to C language.
 * @param  c1  constant operand
 * @param  c2  constant operand
 * @return     constant resultant or null(when failing in the evaluation)
**/
public Const evaluateShiftRa(Const c1,Const c2) // >>
{
  Type t = c1.getSymType();
  if( t.isInteger() && c2.getSymType().isInteger() ) {
    if (t.getSizeValue() <= 8) //##81
      return newIntConst( c1.longValue()>>c2.longValue(), getIpType(t) );
  }
  return null;
}

/**
 * Evaluate and operation.
 * The evaluation conforms to C language.
 * @param  c1  constant operand
 * @param  c2  constant operand
 * @return     constant resultant or null(when failing in the evaluation)
**/
public Const evaluateAnd(Const c1,Const c2) // &
{
  Type t = getUacType(c1,c2);
  if( t!=null && t.isInteger() ) {
    if (t.getSizeValue() <= 8) //##81
      return newIntConst(c1.longValue() & c2.longValue(), t);
  }
  return null;
}

/**
 * Evaluate or operation.
 * The evaluation conforms to C language.
 * @param  c1  constant operand
 * @param  c2  constant operand
 * @return     constant resultant or null(when failing in the evaluation)
**/
public Const evaluateOr(Const c1,Const c2) // |
{
  Type t = getUacType(c1,c2);
  if( t!=null && t.isInteger() ) {
    if (t.getSizeValue() <= 8) //##81
      return newIntConst(c1.longValue() | c2.longValue(), t);
  }
  return null;
}

/**
 * Evaluate xor operation.
 * The evaluation conforms to C language.
 * @param  c1  constant operand
 * @param  c2  constant operand
 * @return     constant resultant or null(when failing in the evaluation)
**/
public Const evaluateXor(Const c1,Const c2) // ^
{
  Type t = getUacType(c1,c2);
  if( t!=null && t.isInteger() ) {
    if (t.getSizeValue() <= 8) //##81
      return newIntConst( c1.longValue()^c2.longValue(), t );
  }
  return null;
}

/**
 * Evaluate cast operation.
 * The evaluation conforms to C language.
 * @param  t  type to cast
 * @param  c  constant operand
 * @return    constant resultant or null(when failing in the evaluation)
**/
public Const evaluateCast(Type t,Const c)
{
  if( t.getUnqualifiedType()==c.getSymType().getUnqualifiedType() )
    return c;
  Type lConstType = c.getSymType();  //##81
  switch( t.getTypeKind() )
  {
  case Type.KIND_BOOL: //boolean
    switch( c.getSymType().getTypeKind() )
    {
    case Type.KIND_BOOL: //boolean
    case Type.KIND_ENUM: //integer
    case Type.KIND_WCHAR:
    case Type.KIND_CHAR:
    case Type.KIND_SHORT:
    case Type.KIND_INT:
    case Type.KIND_LONG:
    case Type.KIND_LONG_LONG:
    case Type.KIND_U_CHAR:
    case Type.KIND_U_SHORT:
    case Type.KIND_U_INT:
    case Type.KIND_U_LONG:
    case Type.KIND_U_LONG_LONG:
    case Type.KIND_FLOAT: //floating point number
    case Type.KIND_DOUBLE:
    case Type.KIND_LONG_DOUBLE:
      return c.doubleValue()!=0
           ? symRoot.boolConstTrue
           : symRoot.boolConstFalse;
    }
    return null;

  case Type.KIND_ENUM: //integer
    t = symRoot.typeInt;
  case Type.KIND_WCHAR:
  case Type.KIND_CHAR:
  case Type.KIND_SHORT:
  case Type.KIND_INT:
  case Type.KIND_LONG:
  case Type.KIND_LONG_LONG:
  case Type.KIND_U_CHAR:
  case Type.KIND_U_SHORT:
  case Type.KIND_U_INT:
  case Type.KIND_U_LONG:
  case Type.KIND_U_LONG_LONG:
    switch( c.getSymType().getTypeKind() )
    {
    case Type.KIND_BOOL: //boolean
    case Type.KIND_ENUM: //integer
    case Type.KIND_WCHAR:
    case Type.KIND_CHAR:
    case Type.KIND_SHORT:
    case Type.KIND_INT:
    case Type.KIND_LONG:
    case Type.KIND_LONG_LONG:
    case Type.KIND_U_CHAR:
    case Type.KIND_U_SHORT:
    case Type.KIND_U_INT:
    case Type.KIND_U_LONG:
    case Type.KIND_U_LONG_LONG:
    case Type.KIND_FLOAT: //floating point number
    case Type.KIND_DOUBLE:
    case Type.KIND_LONG_DOUBLE:
      if (t.getSizeValue() <= 8) //##81
        return newIntConst(c.longValue(),t);
    }
    return null;

  case Type.KIND_FLOAT: //floating point number
  case Type.KIND_DOUBLE:
  case Type.KIND_LONG_DOUBLE:
    switch( c.getSymType().getTypeKind() )
    {
    case Type.KIND_BOOL: //boolean
    case Type.KIND_ENUM: //integer
    case Type.KIND_WCHAR:
    case Type.KIND_CHAR:
    case Type.KIND_SHORT:
    case Type.KIND_INT:
    case Type.KIND_LONG:
    case Type.KIND_LONG_LONG:
    case Type.KIND_U_CHAR:
    case Type.KIND_U_SHORT:
    case Type.KIND_U_INT:
    //##81 case Type.KIND_U_LONG:
    //##81 case Type.KIND_U_LONG_LONG:
    case Type.KIND_FLOAT: //floating point number
    case Type.KIND_DOUBLE:
    case Type.KIND_LONG_DOUBLE:
      return newFloatConst(c.doubleValue(),t);
    //##81 BEGIN
    case Type.KIND_U_LONG:
    case Type.KIND_U_LONG_LONG:
      if (t.getSizeValue() <= 8) {
        long lLValue = c.longValue();
        double lDValue;
        if (lLValue < 0) {
          lDValue = ((double)(lLValue >>> 1)) * 2.0;
        }
        else {
          lDValue = (double)lLValue;
        }
        return newFloatConst(lDValue, t);
      }
    //##81 END
    }
    return null;
  }
  return null;
}
/**
 * Evaluate conditional expression.
 * The evaluation conforms to C language.
 * This method defines what value the supported language considers to be true
 * in 'if', 'for', '?:', etc.
 * @param  c  constant operand
 * @return    1, 0 or MIN_VALUE as value of c is true, false, or unevaluable
**/
public int evaluateCondition(Const c)
{
  if( c.getSymType().getTypeRank()>0 )
    return c.doubleValue()!=0 ? 1 : 0;
  return Integer.MIN_VALUE;
}

/**
 * Evaluate relational expression.
 * The evaluation conforms to C language.
 * @param  c1  constant operand
 * @param  c2  constant operand
 * @return     -1, 0, 1 or MIN_VALUE as c1 is numerically
               less than,equal to, greater than, or incomparable to c2

**/
public int evaluateRelation(Const c1,Const c2)
{
  Type t = getUacType(c1,c2);
  if( t!=null )
    if( t.isInteger() ) {
      return newBigInteger(c1).compareTo(newBigInteger(c2));
    }else {
      if (t.getSizeValue() > 8) //##81
        return Integer.MIN_VALUE; //##81
      return c1.doubleValue() < c2.doubleValue() ? -1
        : c1.doubleValue() == c2.doubleValue() ? 0
        : 1;
    }
  return Integer.MIN_VALUE;
}

// get usual arithmetic conversion type
private Type getUacType(Const c1,Const c2)
{
  Type t1 = c1.getSymType();
  Type t2 = c2.getSymType();

  // The operand with higher rank is made the left operand.
  if( t1.getTypeRank()<t2.getTypeRank() )
    { Type t=t1; t1=t2; t2=t; }

  // If one of operands is not arithmetic
  if( t2.getTypeRank()<=0 )
    return null;

  // If rank of operand >= rank of float
  if( t1.getTypeRank()>=symRoot.typeFloat.getTypeRank() )
  {
    // return left operand type.
    return  t1.getUnqualifiedType();
  }
  // If rank of operand >= rank of int
  else if( t1.getTypeRank()>=symRoot.typeInt.getTypeRank() )
  {
    // If the precision of both oprerand is the same,
    // and one of operands is unsigned
    if( t1.getSizeValue()==t2.getSizeValue()
    &&( t1.isUnsigned() || t2.isUnsigned() ) )
      // return the left oprerand type (changed to) unsigned.
      switch( t1.getTypeKind() )
      {
      case Type.KIND_SHORT:       return symRoot.typeU_Short;
      case Type.KIND_INT:         return symRoot.typeU_Int;
      case Type.KIND_LONG:        return symRoot.typeU_Long;
      case Type.KIND_LONG_LONG:   return symRoot.typeU_LongLong;
      case Type.KIND_CHAR:        return symRoot.typeU_Char;
      case Type.KIND_U_CHAR:      return symRoot.typeU_Char;
      case Type.KIND_U_SHORT:     return symRoot.typeU_Short;
      case Type.KIND_U_INT:       return symRoot.typeU_Int;
      case Type.KIND_U_LONG:      return symRoot.typeU_Long;
      case Type.KIND_U_LONG_LONG: return symRoot.typeU_LongLong;
      default:                    return null;
      }
    else
      // return left operand type.
      return  t1.getUnqualifiedType();
  }
  // If rank of operand < rank of int
  else
  {
    if( t1.getSizeValue()>=symRoot.typeInt.getSizeValue() && t1.isUnsigned()
    ||  t2.getSizeValue()>=symRoot.typeInt.getSizeValue() && t2.isUnsigned() )
      return symRoot.typeU_Int;
    else
      return symRoot.typeInt;
  }
}

// get integral promotion type
private Type getIpType(Type t)
{
  return t.getTypeRank()<symRoot.typeInt.getTypeRank() ? symRoot.typeInt : t;
}

// generate IntConst of appropriate precision
private IntConst newIntConst(long v,Type t)
{
  long unusedbits = 64-8*t.getSizeValue();
  if( t.isUnsigned() )
    return symRoot.sym.intConst( v<<unusedbits>>>unusedbits, t );
  else
    return symRoot.sym.intConst( v<<unusedbits>>unusedbits, t );
}

// generate FloatConst of appropriate precision
private FloatConst newFloatConst(double v,Type t)
{
  if( t.getTypeKind()==Type.KIND_FLOAT ) //useless ?
    return symRoot.sym.floatConst( (double)(float)v, t ); //useless ?
  else //useless ?
    return symRoot.sym.floatConst( v, t );
}

// generate BigInteger of appropriate precision
private BigInteger newBigInteger(Const c)
{
  long unusedbits = 64-8*c.getSymType().getSizeValue();
  if( unusedbits==0 )
    if( c.getSymType().isUnsigned() )
      return BigInteger.valueOf(c.longValue()).and(longMask);
    else
      return BigInteger.valueOf(c.longValue());
  else
    if( c.getSymType().isUnsigned() )
      return BigInteger.valueOf(c.longValue()<<unusedbits>>>unusedbits);
    else
      return BigInteger.valueOf(c.longValue()<<unusedbits>>unusedbits);
}
//SF050111]

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

  /**
   * Set the names of functions without side effect.
   * This may be overridden by defining a function having
   * the same name in SourceLanguage corresponding to
   * the language to be processed.
   */
  public void
  initiateFunctionsWithoutSideEffect()
{
  functionsWithoutSideEffect = new HashSet();
  functionsWithoutSideEffect.add("sin");
  functionsWithoutSideEffect.add("cos");
  functionsWithoutSideEffect.add("tan");
  functionsWithoutSideEffect.add("asin");
  functionsWithoutSideEffect.add("acos");
  functionsWithoutSideEffect.add("atan");
  functionsWithoutSideEffect.add("sinh");
  functionsWithoutSideEffect.add("cosh");
  functionsWithoutSideEffect.add("tanh");
  functionsWithoutSideEffect.add("log");
  functionsWithoutSideEffect.add("exp");
  functionsWithoutSideEffect.add("pow");
  functionsWithoutSideEffect.add("sqrt");
} // initiateFunctionsWithoutSideEffect

/**
 * Get the set of functions wituout side effect.
 * @return the set of functions wituout side effect.
 */
public Set
  getFunctionsWithoutSideEffect()
{
  return functionsWithoutSideEffect;
}
//##71 END

} // SourceLanguageImpl class
