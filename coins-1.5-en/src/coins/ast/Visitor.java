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
 * The visitor pattern.
 *
 * See ast.StdVisitor
 * See ast.ASTree#accept(Visitor)
 */
//## public class Visitor { //##3 Original
public interface Visitor {  //##3
    public void atASTList(ASTList n); //##3
    public void atPragma(Pragma n); //SF050304
    public void atAsmExpr(AsmExpr n); //##70

    public void atCompoundStmnt(CompoundStmnt n); //##17
    public void atStruct(Struct n); //##3
    public void atUnion(Union n); //##3
    public void atDeclarator(Declarator n); //##3
    public void atDeclaratorList(DeclaratorList n); //##3
    public void atEnum(Enum n); //##3
    public void atFunction(Function n); //##3
    public void atPair(Pair n); //##3

    public void atAddressExpr(AddressExpr n); //##3
    public void atArithBinaryExpr(ArithBinaryExpr n); //##3
    public void atArithUnaryExpr(ArithUnaryExpr n); //##3
    public void atArrayExpr(ArrayExpr n); //##3
    public void atAssignExpr(AssignExpr n); //##3
    public void atCallExpr(CallExpr n); //##3
    public void atCastExpr(CastExpr n); //##3
    public void atSizeofExpr(SizeofExpr n); //##17
    public void atCommaExpr(CommaExpr n); //##3
    public void atConditionalExpr(ConditionalExpr n); //##3
    public void atConstantExpr(ConstantExpr n); //##3
    public void atDereferenceExpr(DereferenceExpr n); //##3
    public void atArrayInitializer(ArrayInitializer n); //##3
    public void atMemberExpr(MemberExpr n); //##3
    public void atPointerBinaryExpr(PointerBinaryExpr n); //##3
    public void atPostfixExpr(PostfixExpr n); //##3
    public void atPrefixExpr(PrefixExpr n); //##3
    public void atStringLiteral(StringLiteral n); //##3
    public void atVariableExpr(VariableExpr n); //##3

    public void atBreakStmnt(BreakStmnt n); //##3
    public void atCaseLabel(CaseLabel n); //##3
    public void atContinueStmnt(ContinueStmnt n); //##3
    public void atDefaultLabel(DefaultLabel n); //##3
    public void atDoStmnt(DoStmnt n); //##3
    public void atExpressionStmnt(ExpressionStmnt n); //##3
    public void atForStmnt(ForStmnt n); //##3
    public void atGotoStmnt(GotoStmnt n); //##3
    public void atIfStmnt(IfStmnt n); //##3
    public void atNamedLabel(NamedLabel n); //##3
    public void atNullStmnt(NullStmnt n); //##3
    public void atReturnStmnt(ReturnStmnt n); //##3
    public void atSwitchStmnt(SwitchStmnt n); //##3
    public void atWhileStmnt(WhileStmnt n); //##3
}
