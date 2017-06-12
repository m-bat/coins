/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.ast.expr;

////////SF030212[
/**
 * Constant class of floating type.
 */
public class FloatConstantExpr extends ConstantExpr
{
  double fValue;

  public FloatConstantExpr(double value,char type)
  {
    fValue = value;
    fType = new byte[]{ (byte)type };
  }
  public String toString()
  {
    return Double.toString(fValue)+(char)fType[0];
  }
  public long longValue()
  {
    return (long)fValue;
  }
  public double doubleValue()
  {
    return fValue;
  }
  public char getSignChar()
  {
    return (char)SIGNED_T;
  }
  public char getTypeChar()
  {
    return (char)fType[0];
  }
}
////////SF030212]
