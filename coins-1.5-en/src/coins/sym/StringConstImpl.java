/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;

import coins.SymRoot;

/** StringConstImpl class */

public class
StringConstImpl extends ConstImpl implements StringConst
{

protected String
  fStringBody;

protected int
  fLength;

/** StringConst constructor.
 *  Do not use this directly but use stringConst of Sym
 *  to make a StringConst instance.
**/
public
StringConstImpl( SymRoot pSymRoot, String pInternedName ) 
{
  super(pSymRoot); 
  fKind = Sym.KIND_STRING_CONST;
  fName     = pInternedName;
}

    public long  longValue() {
        return 0;
    }

    public double doubleValue() {
        return 0.0;
    }

  public String stringValue() { return fName; }

public String
getStringBody()
{
  return fStringBody;
}

public void
setStringBody( String pStringBody )  
{
  fStringBody = pStringBody;
  fLength = symRoot.sourceLanguage.getStringLength(pStringBody); 
  if (fDbgLevel >= 6) {
    symRoot.ioRoot.dbgSym.print(6, " setStringBody", "length " +
      fLength + " " + pStringBody + " " +
      symRoot.sym.makeJavaString(pStringBody) );
    for (int i = 0; i < pStringBody.length(); i++)
      System.out.print(" " + Integer.toString((int)pStringBody.charAt(i),
                        16));
    System.out.println();
  }
} // setStringBody

public String
makeJavaString()
{
  return makeJavaString(getStringBody());
}

public String
makeCstring()
{
  return symRoot.sourceLanguage.makeCstring(getStringBody());
}

public String
makeCstringWithTrailing0()
{
  return makeCstringWithTrailing0(getStringBody());
}

public int
getLength()  
{
  return fLength;
}

public String
toString() {
  String symString;
  if (fStringBody == null)
    symString = fName;
  else
    symString = symRoot.sym.makeJavaString(fStringBody);
  return symString;
} // toString

public String
toStringDetail()
{
  String lString = super.toStringDetail();
  lString = lString + " length " + fLength;
  return lString;
}

//////////////////// S.Fukuda 2002.10.30 begin
public Object evaluateAsObject()
{
  return fStringBody;
}
//////////////////// S.Fukuda 2002.10.30 enf

} // StringConstImpl class
