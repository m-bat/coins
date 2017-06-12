/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;


//========================================//

/**
<PRE>
 *  StringConst interface
 *  Const (constant) class interface.
 *  A constant in source program is recorded as a Const instance
 *  by using its character string representation as its name.
 *  There are access methods to get the value of constants and
 *  to record a constant giving its value.
 *  Constants are usually represented by symbol table instance
 *  in order to represent attributes attached to them.
 *  Subclasses of Const is not so fine as type, foe example,
 *  int, short, long, unsigned int, unsigned short, unsigned long
 *  all belongs to IntConst. To see the type of Const object,
 *  use getSymType() of SymInterface.
</PRE>
**/
public interface
StringConst extends Const
{

/**
<PRe>
 *  getStringBody Get the body of string (pure string)
 *      excluding quotes, etc., where the pure string
 *      is composed of characters excluding escape characters
 *      and heading, trailing delimiters.
 *      If source language is C,
 *      heading and trailing quotes and escape
 *      characters are removed from the source expression,
 *      and trailing 0x00 (0x0000 in case of wchar_t) is
 *      also deleted.
</PRE>
**/
public String
getStringBody();

/** setStringBody Set the body of the string and its length.
 *  The parameter should be changed to pure string by using
 *  makeStringBody of SourceLanguage before calling
 *  this method.
 *  @param pStringBody Body of the string.
**/
public void
setStringBody( String pStringBody );

/** makeCstring
 *  Change the string body of this string to C string
 *  representation adding heading, trailing quotes and
 *  escape characters if required.
 *  Trailing \0 is not added as printable character.
 *  See coins.SourceLanguage.
 *  @param pStringBody String made by makeStringBody of
 *      coins.SourceLanguage.
 *  @return the string changed in C form.
**/
public String
makeCstring();

/** makeCstringWithTrailing0
 *  Change the string body of this string to C string
 *  representation adding heading, trailing quotes and
 *  escape characters if required.
 *  Trailing \0 is added as printable character with escape char.
 *  See coins.SourceLanguage.
 *  @param pStringBody String made by makeStringBody of
 *      coins.SourceLanguage.
 *  @return the string changed in C form with trailing \0
 *      as printable character.
**/
public String
makeCstringWithTrailing0();

/** getLength
 *  Get the length (number of bytes) of the string body.
**/
public int
getLength();

//////////////////// S.Fukuda 2002.10.30 begin
/**
 * Get the String object representing this constant.
 * @return the String object.
 */
public Object evaluateAsObject();
//////////////////// S.Fukuda 2002.10.30 enf

} // StringConst
