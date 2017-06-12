/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.ast.expr;

import coins.ast.*;

/**
 * Function call expression.
 */
public class AsmExpr extends NnaryExpr implements TokenId
{

    private byte[] returnType;

    public AsmExpr(ASTList args)
    {
  super(null, args);
  returnType =  new byte[] {(byte)TypeId.VOID_T};

    }

    public void accept(Visitor v) {
  v.atAsmExpr(this);
    }

    /**
     * Returns the actual parameters.
     *
     * @return		A linked list of <code>Expr</code> objects.
     */
    public ASTList getArguments() { return (ASTList)super.getRight(); }

    public byte[] getType()
    {
      return returnType;
    }

    /**
     * Returns the string representation of the operator name.
     */
    public String operatorName() {
  return "<asm>";
    }

    public ASTree getLeft() { return super.getRight(); }

    public ASTree getRight() { return null; }

    public void setLeft(ASTree _left) {  }

    public int operatorId()
    {
      return TokenId.ASM;
    }

} // AsmExpr
