/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.ast.expr;

////////SF030212[
/**
 * Constant class of integer type.
 */
public class IntConstantExpr extends ConstantExpr
{
  long fValue;

  public IntConstantExpr(long value,char sign,char type)
  {
    fValue = value;
    fType = new byte[]{ (byte)sign, (byte)type };
  }
  public String toString()
  {
    return Long.toString(fValue)+(char)fType[0]+(char)fType[1];
  }
  public long longValue()
  {
    return fValue;
  }
  public double doubleValue()
  {
    return (double)fValue;
  }
  public char getSignChar()
  {
    return (char)fType[0];
  }
  public char getTypeChar()
  {
    return (char)fType[1];
  }
}
////////SF030212]
