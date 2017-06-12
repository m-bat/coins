/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
//##3 : Changed when the Cparser was integrated with Coins compiler.

package coins.ast;

import coins.ast.expr.*;
import coins.ast.stmnt.*;

/**
 * Standard Visitor.
 *
 * <p>All the <code>atXXX()</code> methods in this class
 * call <code>accept()</code>
 * on the left child and then the right child.
 * This class should be subclassed so that <code>atXXX()</code> performs
 * something meaningful.
 */
//## public class StdVisitor extends Visitor { //##3 Original
public class StdVisitor implements Visitor { //##3
    private void visitChildren(ASTree t) {
  ASTree left = t.getLeft();
  if (left != null)
      left.accept(this);

  ASTree right = t.getRight();
  if (right != null)
      right.accept(this);
    }

    public void atASTList(ASTList n) { visitChildren(n); }
    public void atPragma(Pragma n) { visitChildren(n); } //SF050304
    public void atAsmExpr(AsmExpr n) { visitChildren(n); } //##70

    public void atCompoundStmnt(CompoundStmnt n) { visitChildren(n); } //##17
    public void atStruct(Struct n) { visitChildren(n); }
    public void atUnion(Union n) { visitChildren(n); }
    public void atDeclarator(Declarator n) { visitChildren(n); }
    public void atDeclaratorList(DeclaratorList n) { visitChildren(n); }
    public void atEnum(Enum n) { visitChildren(n); }
    public void atFunction(Function n) { visitChildren(n); }
    public void atPair(Pair n) { visitChildren(n); }

    public void atAddressExpr(AddressExpr n) { visitChildren(n); }
    public void atArithBinaryExpr(ArithBinaryExpr n) { visitChildren(n); }
    public void atArithUnaryExpr(ArithUnaryExpr n) { visitChildren(n); }
    public void atArrayExpr(ArrayExpr n) { visitChildren(n); }
    public void atAssignExpr(AssignExpr n) { visitChildren(n); }
    public void atCallExpr(CallExpr n) { visitChildren(n); }
    public void atCastExpr(CastExpr n) { visitChildren(n); }
    public void atSizeofExpr(SizeofExpr n) { visitChildren(n); } //##17
    public void atCommaExpr(CommaExpr n) { visitChildren(n); }
    public void atConditionalExpr(ConditionalExpr n) { visitChildren(n); }
    public void atConstantExpr(ConstantExpr n) { visitChildren(n); }
    public void atDereferenceExpr(DereferenceExpr n) { visitChildren(n); }
    public void atArrayInitializer(ArrayInitializer n) { visitChildren(n); }
    public void atMemberExpr(MemberExpr n) { visitChildren(n); }
    public void atPointerBinaryExpr(PointerBinaryExpr n) { visitChildren(n); }
    public void atPostfixExpr(PostfixExpr n) { visitChildren(n); }
    public void atPrefixExpr(PrefixExpr n) { visitChildren(n); }
    public void atStringLiteral(StringLiteral n) { visitChildren(n); }
    public void atVariableExpr(VariableExpr n) { visitChildren(n); }

    public void atBreakStmnt(BreakStmnt n) { visitChildren(n); }
    public void atCaseLabel(CaseLabel n) { visitChildren(n); }
    public void atContinueStmnt(ContinueStmnt n) { visitChildren(n); }
    public void atDefaultLabel(DefaultLabel n) { visitChildren(n); }
    public void atDoStmnt(DoStmnt n) { visitChildren(n); }
    public void atExpressionStmnt(ExpressionStmnt n) { visitChildren(n); }
    public void atForStmnt(ForStmnt n) { visitChildren(n); }
    public void atGotoStmnt(GotoStmnt n) { visitChildren(n); }
    public void atIfStmnt(IfStmnt n) { visitChildren(n); }
    public void atNamedLabel(NamedLabel n) { visitChildren(n); }
    public void atNullStmnt(NullStmnt n) { visitChildren(n); }
    public void atReturnStmnt(ReturnStmnt n) { visitChildren(n); }
    public void atSwitchStmnt(SwitchStmnt n) { visitChildren(n); }
    public void atWhileStmnt(WhileStmnt n) { visitChildren(n); }
}
