/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.casttohir;

import java.util.Set;
import java.util.HashSet;

/** ParseString class
 * Parse given String operand.
 * Successive call of getNextToken() will return tokens in
 * the given string successively. The token kind is one of
 *   DIGITS, IDENTIFIER, STRING, DELIMITER, UNKNOWN
 * The token kind are listed up as public static final int item in this class.
 * DIGITS is a string consisting of digit character only.
 * IDENTIFIER is a string consisting of alphabet (upper case and lower case),
 * digits, and special characters specified by the parameter pSpIdChars.
 * IDENTIFIER does not begin with digit.
 * STRING is a character string starting with '"' and ending with '"'.
 * DELIMITER is a character specified by the parameter pDelimiters.
 * Characters indicated by the parameter pSpaces are skipped in the parsing
 * operation.
 * To see if there is no more token left, call hasNext().
 * If there is no token remaining, getNextToken() will return "";
 * If token kind is undefined, getNextToken() will return "".
**/
public class ParseString
{

  private final String fText;       // Text string to be processed.
  private final Set    fDelimiters; // Delimiting character strings.
  private final Set    fSpaces;     // Character strings to be treated
                                    // as a space.
  private final Set fIdSpChars;     // Special character strings
                                    // used in identifier.
  private final Set fIdHeadingChars;// Character strings used as heading
                                    // character of identifier.
  private final Set fIdBodyChars;   // Character strings used as body
                                    // character of identifier.
  private final int fLength;    // Length of the given text.
  private int   fNextPosition;  // Search position for the next token.
  private int   fTokenKind;     // Shows token kind for the token
                                // indicated by getNextToken().
  private String fCurrentToken; // Current token that is returned by most
                                // recent getNextToken().
  /** ParseString constructor
   * Example:
   *   ParseString lParseString = new ParseString(
   *     "#param %I32, %I32, w%I32\n",
   *     fromStringToSet("#%,\n"),
   *     fromStringToSet(" \t"),
   *     fromStringToSet("_"));
   * @param pText        Text string to be parsed.
   * @param pDelimiters  Set of character strings (character in the form of String)
   *                     to be treated as a delimiting token.
   * @param pSpaces      Set of character strings to be treated
   *                     as a space (not a token).
   * @param pIdSpChars   Set of special character (non-alphanumeric character)
   *                     strings used in identifiers.
  **/
  public ParseString( String pText,
                      Set pDelimiters, Set pSpaces,
                      Set pIdSpChars )
  {
    fText  = pText;
    fDelimiters   = pDelimiters;
    fSpaces       = pSpaces;
    fLength       = pText.length();
    fNextPosition = 0;
    fTokenKind    = UNDEFINED;
    fIdSpChars    = pIdSpChars;
    fIdHeadingChars = new HashSet(pIdSpChars);
    for (int i = 0; i < alphabetCharacters.length; i++) {
      fIdHeadingChars.add(alphabetCharacters[i]);
    }
    fIdBodyChars = new HashSet(fIdHeadingChars);
    for (int i = 0; i < digitCharacters.length; i++) {
      fIdBodyChars.add(digitCharacters[i]);
    }
    // System.out.print("\nParseString " + " delimiters " +
    //   pDelimiters + " spaces " + pSpaces + " pIdSpChars " + pIdSpChars
    //  + " text " + pText );
  } // ParseString

  public boolean
  hasNext()
  {
    return (fNextPosition < fLength);
  } // hasNext

  /** getNextToken
   * Get the next token and advance the token position.
   * The resultant string is applied intern().
   * To see its token kind, call getTokenKind().
   * @return the character string of the next token.
  **/
  public String
  getNextToken()
  {
    String lResult;
    char lTokenHeaderChar = getNextNonSpaceChar();
    if ((lTokenHeaderChar >= '0')&&(lTokenHeaderChar <= '9')) {
      lResult = digitString();
      fTokenKind = DIGITS;
    }else if (lTokenHeaderChar == '"') {
      lResult = characterString();
      fTokenKind = STRING;
    }else {
      String lHead = String.valueOf(lTokenHeaderChar);
      if (fIdHeadingChars.contains(lHead)) {
        lResult = identifierString();
        fTokenKind = IDENTIFIER;
      }else {
        if (fDelimiters.contains(lHead)) {
          lResult = lHead;
          fTokenKind = DELIMITER;
        }else {
          lResult = "";
          fTokenKind = UNDEFINED;
        }
        fNextPosition++;
      }
    }
    fCurrentToken = lResult;
    // System.out.print("\n getNextToken" + "" + "=" + lResult );  //###
    return lResult.intern();
  } // getNextToken

  /** getNextToken
   * Get the next token and advance the token position.
   * To see its token kind, call getTokenKind().
   * @return  the character string of the next token.
  **/
  public String
  seeCurrentToken()
{
  return fCurrentToken;
} // seeCurrentToken

  /**
   * Get the token kind of the token returned by getNextToken().
   * The token kind is one of
   *   DIGIT, IDENTIFIER, STRING, DELIMITER, UNDEFINED.
   * @return the token kind of the token returned by
   */
  public int
  getTokenKind()
{
  return fTokenKind;
} // getTokenKind

/**
 * Make a set of Strings by taking out characters contained in the
 * parameter pString and changing each character to a string of
 * length 1.
 * For example, if pString is "(),:;" then the resultant set is
 * { "(", ")", ",", ":", ";" }
 * @param pString specifies the characters to be included in the resultant set.
 * @return the set of strings obtained from pString.
 */
public static Set
fromStringToSet( String pString )
{
  Set lSet = new HashSet();
  for (int lIndex = 0; lIndex < pString.length(); lIndex++) {
    String lSubstring = pString.substring(lIndex, lIndex+1);
    lSet.add(lSubstring);
  }
  return lSet;
} // fromSrtingToSet

private char
  getNextNonSpaceChar()
{
    char lChar = ' ';
    for (; fNextPosition < fLength; fNextPosition++) {
      lChar = fText.charAt(fNextPosition);
      if (! fSpaces.contains(String.valueOf(lChar))) {
        break;
      }
    }
    return lChar;
} //

String digitString()
  {
    StringBuffer lBuffer = new StringBuffer();
    char lNextChar;
    for (; fNextPosition < fLength; fNextPosition++) {
      lNextChar = fText.charAt(fNextPosition);
      if ((lNextChar >= '0')&&(lNextChar <= '9')) {
        lBuffer.append(lNextChar);
      }else
        break;
    }
    return lBuffer.toString();
  } // digitString

  String characterString()
    {
      StringBuffer lBuffer = new StringBuffer();
      char lNextChar;
      for (fNextPosition++; fNextPosition < fLength; fNextPosition++) {
        lNextChar = fText.charAt(fNextPosition);
        if (lNextChar != '"') {
          lBuffer.append(lNextChar);
        }else {
          fNextPosition++;
          break;
        }
      }
      return lBuffer.toString();
    } // identifierString

    String identifierString()
      {
        StringBuffer lBuffer = new StringBuffer();
        String lNextChar;
        for (; fNextPosition < fLength; fNextPosition++) {
          lNextChar = String.valueOf(fText.charAt(fNextPosition));
          if (fIdBodyChars.contains(lNextChar)) {
            lBuffer.append(lNextChar);
          }else
            break;
        }
        return lBuffer.toString();
      } // characterString


  // Constants indicating token kind.
  public static final int
    UNDEFINED  = 0,
    IDENTIFIER = 1,
    DELIMITER  = 2,
    DIGITS     = 3,
    STRING     = 4;

  static final String[] alphabetCharacters = {
    "A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z",
   "a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"
  };
  static final String[] digitCharacters = {
    "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"
  };
} // ParseString class
