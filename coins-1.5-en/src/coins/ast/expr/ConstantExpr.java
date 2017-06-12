/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.ast.expr;

import coins.ast.ASTree;
import coins.ast.Expr;
import coins.ast.TypeId;
import coins.ast.Visitor;

/* Implementation note:
 * If you change these classes, you also have to change cfront.Evaluator
 * to be consistent.
 */
////////SF030212[
/**
 * Constant class.
 * This class has been rewritten because the original ConstantExpr class
 * does not hold type information when constant folding is done.
 * Objects of ConstantExpr can not be changed as it is for String object.
 * If you are tempted to change the value of constant object,
 * you should construct new object instead of changing the value.
 */
public abstract class ConstantExpr extends ASTree implements Expr, TypeId
{
  protected byte[] fType; // in case of int, [SU][csilj];
                          // in case of float, [fdr].
  public abstract long longValue(); // Declaration of abstract method.
  public abstract double doubleValue();
  public abstract char getSignChar();
  public abstract char getTypeChar();

  public ASTree getLeft (){ return null; } // Implementation of the abstract method.
  public ASTree getRight(){ return null; }
  public void setLeft (ASTree left ){}
  public void setRight(ASTree right){}
  public void accept(Visitor v){ v.atConstantExpr(this); }
  public byte[] getType(){ return fType; }
}
////////SF030212]
