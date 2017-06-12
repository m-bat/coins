/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.ast.expr;

import coins.ast.*;

/**
 * Assignment expression.
 */
public class AssignExpr extends BinaryExpr implements TokenId {
    private int opId;
    private String fileName;   //##18
    private int    lineNumber; //##18

    public AssignExpr(Expr lvalue, int op, Expr expr) {
  super(lvalue, expr);
  opId = op;
    }

    public void accept(Visitor v) {
  v.atAssignExpr(this);
    }

    public byte[] getType() {
  return ((Expr)getLeft()).getType();
    }

    public int operatorId() {
  return opId;
    }

    public String operatorName() {
  if (opId == '=')
      return "=";
  else
      switch (opId) {
      case MOD_E :
    return "%=";
      case AND_E :
    return "&=";
      case MUL_E :
    return "*=";
      case PLUS_E :
    return "+=";
      case MINUS_E :
    return "-=";
      case DIV_E :
    return "/=";
      case LSHIFT_E :
    return "<<=";
      case EXOR_E :
    return "^=";
      case OR_E :
    return "|=";
      case RSHIFT_E :
    return ">>=";
      }

  throw new RuntimeException("unknown operator: " + opId);
    }

//##18 BEGIN

    public void setPosition( String pFileName, int pLineNumber )
    {
      fileName = pFileName;
      lineNumber = pLineNumber;
    }

    public String fileName() { return fileName; }

    public int lineNumber() { return lineNumber; }

//##18 END

}
