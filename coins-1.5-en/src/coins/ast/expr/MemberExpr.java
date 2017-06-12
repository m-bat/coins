/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */

package coins.ast.expr;

import coins.ast.*;

/**
 * Member access expression "<code>-&gt;</code>" (arrow) or
 * "<code>.</code>" (dot).
 */
public class MemberExpr extends UnaryExpr implements TokenId, LvalueExpr {
    private boolean arrow;
    private String memberName;
    private Aggregate memberDef;
    private Declarator memberDecl;

    public MemberExpr(Expr expr, boolean _arrow, String member,
          Aggregate def, Declarator d) {
  super(expr);
  arrow = _arrow;
  memberName = member;
  memberDef = def;
  memberDecl = d;
    }

    public void accept(Visitor v) {
  v.atMemberExpr(this);
    }

    public byte[] getType() { return memberDecl.getType(); }

    public boolean isLvalue() {
  ////////SF040909[
  //return !TypeDecoder.isArray(memberDecl.getType(), 0);
  if( TypeDecoder.isArray(memberDecl.getType(),0) )
    return false;
  else
    if( arrow )
      return true;
    else
      return expr instanceof LvalueExpr
          && ((LvalueExpr)expr).isLvalue();
  ////////SF040901]
    }

    public boolean hasAddress() { return true; }

    /**
     * Returns the member name.
     */
    public String name() { return memberName; }

    /**
     * Returns the declaration of the struct or union data type
     * that the accessed member belongs to.
     */
    public Aggregate getStructure() { return memberDef; }

    /**
     * Returns the declaration of the accessed member.
     */
    public Declarator getMember() { return memberDecl; }

    /**
     * Returns <code>TokenId.ARROW</code> or "<code>.</code>" (dot).
     */
    public int operatorId() {
  if (arrow)
      return ARROW;
  else
      return '.';
    }

    public String operatorName() {
  if (arrow)
      return "->";
  else
      return ".";
    }

    protected String getTag() { return operatorName() + memberName; }
}
