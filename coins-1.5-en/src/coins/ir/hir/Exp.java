/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.ir.IrList;
import coins.sym.Const;
import coins.sym.Elem;
import coins.sym.Label;
import coins.sym.Subp;
import coins.sym.SubpType;
import coins.sym.Var;

//========================================//
//               Jan. 19, 2001.
//               (##2): modified on Nov. 20.
//               (##3): modified on Jan. 2001.
//  Deleted methods:
//    buildTernaryExp, getExp3, buildPointedSubp. (##2)

/** Exp interface
 *  HIR Exp class interface.
 *  The expression class Exp is a subclass of HIR.
 *  See IR, HIR, Stmt, Sym, IrList.
**/
public interface
Exp extends HIR
{

/** ConstNode                            (##3)
 *  Build HIR constant node.
 *  @param pConstSym constant symbol to be attached to the node.
 *  @return constant leaf node having operation code opConst.
**/
// Constructor            (##3)
// ConstNode( Const pConstSym );

/** getConstSym
 *  Get constant symbol attached to this node.
 *  "this" should be a constant node.
 *  @return constant symbol attached to this node.
**/
Const
getConstSym();

/** SymNode           (##3)
 *  Build an HIR symbol name node from a symbol name.
 *  @param pVar   variable   name symbol to be attached to the node.
 *  @param pSubp  subprogram name symbol to be attached to the node.
 *  @param pLabel label      name symbol to be attached to the node.
 *  @param pElem  struct/union element name to be attached to the node.
 *  @param pField class field name to be attached to the node.
 *  @return symbol name node of corresponding class             (##2)
 *      having operation code opSym.
**/
// Constructor                (##3)
// VarNode(   Var   pVar );    (##3)
// SubpNode(  Subp  pSubp );   (##3)
// LabelNode( Label pLabel );  (##3)
// ElemNode(  Elem  pElem );   (##3)
// FieldNode( Field pField );  (##3)

/** getSym
 *  Get symbol from SymNode.      (##2)
 *  "this" should be a SymNode    (##2)
 *  (either VarNode, SubpNode, LabelNode, ElemNode, or FieldNode). (##2)
 *  @return the symbol attached to the node
 *    (either Var, Subp, Label, Elem, or Field).  (##2)
**/
//## Sym
//## getSym();

/** getVar
 *  Get symbol of specified class from SymNode.   (##2)
 *  "this" should be a SymNode.
 *  @return the symbol of specified class attached to the node.
 *      If the symbol is not attached to the node or if its class is
 *      not the specified class, then return null.
**/
Var   getVar();

/** getSubp
 *  Get symbol of spefified class from SymNode.   (##2)
 *  "this" should be a SymNode.
 *  @return the symbol of specified class attached to the node.
 *      If the symbol is not attached to the node or if its class is
 *      not the specified class, then return null.
**/
Subp  getSubp();

/**
 *  getLabel
 *  Get symbol of spefified class from SymNode.   (##2)
 *  "this" should be a SymNode.
 *  @return the symbol of specified class attached to the node.
 *      If the symbol is not attached to the node or if its class is
 *      not the specified class, then return null.
**/
Label getLabel();

/**
 *  getElem
 *  Get symbol of spefified class from SymNode.   (##2)
 *  "this" should be a SymNode.
 *  @return the symbol of specified class attached to the node.
 *      If the symbol is not attached to the node or if its class is
 *      not the specified class, then return null.
**/
Elem  getElem();
// Field getField();

/** UnaryExp         (##3)
 *  Build an unary expression having pOperator as its operator
 *      and pExp as its source operand.
 *  @param pOperator unary operator.
 *  @param pExp source operand expression.
 *  @return unary expression using pOperator and pExp as its
 *      operator and operand.
**/
// Constructor            (##3)
// UnaryExp( int pOperator, Exp pExp );

/** BinaryExp             (##3)
 *  Build a binary expression having pOperator as its operator
 *      and pExp1, pExp2 as its source operand 1 and 2.
 *  @param pOperator binary operator.
 *  @param pExp1 source operand 1 expression.
 *  @param pExp2 source operand 2 expression.
 *  @return binary expression using pOperator and
 *      pExp1 and pExp2 as its operator and operands.
**/
// Constructor            (##3)
// BinaryExp( int pOperator, Exp pExp1, Exp pExp2 );

/** getExp1
 *  Get source operand 1 from unary or binary expression.
 *  "this" should be either unary or binary expression.
 *  @return the source operand 1 expression of this node.
**/
Exp
getExp1();

/** getExp2
 *  Get source operand 2 from binary expression.
 *  "this" should be a binary expression.
 *  @return the source operand 2 expression of this node.
**/
Exp
getExp2();

/** getArrayExp                   (##2)
 *  getSubscriptExp
 *  getElemSizeExp                (##2)
 *  Get a component of a subscripted variable.
 *  "this" should be a node built by buildSubscriptedVar method.
 *  @return a component expression of this subscripted variable.
**/
Exp getArrayExp();     // return array     expression part (pArrayExp).
Exp getSubscriptExp(); // return subscript expression part (pSubscript).
Exp getElemSizeExp();  // return element size part         (pElemSize).

/** PointedVar        (##3)
 *  Build a pointed variable node.
 *  @param pPointer pointer expression which may be a compond variable.
(##2)
 *  @param pElement struct/union element name pointed by pPointer.
 *  @return pointed variable node having operation code opArrow.
**/
// Constructor            (##3)
// PointedVar( Exp pPointer, Elem pElement );  // (##2)

/** getPointerExp
 *  getPointedElem
 *  Get a component of pointed variable expression.
 *  "this" should be a node built by buildPointedVar method.
 *  @return a component expression of this pointed variable.
**/
Exp  getPointerExp();   // return the pointer expression part (pPointer).
Elem getPointedElem();  // return the pointed element part    (pElem).

/** QualifiedVar       (##3)
 *  Build qualified variable node that represents an element
 *      of structure or union.
 *  @param pQualifier struct/union variable having elements.
 *  @param pElement struct/union element name to be represented.
 *  @return qualified variable node having operation code opQual.
**/
// Constructor            (##3)
// QualifiedVar( Exp pQualifier, Elem pElement );

/** getQualifier
 *  getQualifiedElem
 *  Get a component of qualified variable expression.
 *  "this" should be a node built by BuildQualifiedVar method.
 *  @return a component of "this" QualifiedVar expression.  (##2)
**/
Exp  getQualifierExp();     // return qualifier part (pQualifier).
Elem getQualifiedElem(); // return qualified element part (pelement).

/** FunctionExp           (##3)
 *  Build a function expression node that computes function value.
 *  @param pSubpExp function specification part which may be either
 *      a function name, or an expression specifying a function name.
 *  @param pParamList actual parameter list.
 *  @return function expression node having operation code opCall.
 *  @see IrList.
**/
// Constructor            (##3)
// FunctionExp( Subp pSubpSpec, IrList pParamList );

/** getSubpSpec                                           (##2)
 *  getActualParamList
 *  Get a component expression of the function expression. (##2)
 *  "this" should be a node built by buildFunctionExp.
 *  getSubpSpec        return the expression specifying the subprogram
 *                      to be called (pSubpSpec).              (##2)
 *  getActualParamList return the actual parameter list (pParamList).
 *                      If this has no parameter, then return null.
**/
Exp     getSubpSpec();
IrList getActualParamList();

/** findSubpType
 *  Find SubpType represented by this expression.
 *  If this is SubpNode then return SubpType pointed by this node type,
 *  else decompose this expression to find Subpnode.
 *  If illegal type is encountered, return null.
**/
public SubpType    // (##6)
findSubpType();

/** isEvaluable
 *  See if "this" expression can be evaluated or not.
 *  Following expressions are evaluable expression:  //##14
 *    constant expression,
 *    expression whose operands are all evaluable expressions.
 *  Expressions with OP_ADDR or OP_NULL are treated as non evaluable.
 *  Variable with initial value is also treated as non evaluable
 *  because its value may change. //##43
 *  @return true if this expression can be evaluated as constant value
 *      at the invocation of this method, false otherwise.
**/
boolean isEvaluable();

//SF050111[
///** evaluate
// *  Evaluate "this" expression.
// *  "this" should be an evaluable expression.
// *  If not, this method returns null.
// *  It is strongly recommended to confirm isEvaluable() returns true
// *  for this expression before calling this method.
// *  @return constant node as the result of evaluation.
//**/
//ConstNode evaluate();

/**
 * Evaluate "this" expression.
 * @return  constant as the result of evaluation or null(when failing in the
 *          evaluation)
**/
public Const evaluate();

/**
 * Fold "this" expression.
 * evaluate() is called by recursive. If the evaluation succeeded, former node
 * is substituted for the constant node generated as evaluation result.
 * @return  constant as the result of evaluation or null (when failing in the
 *          evaluation)
**/
public Exp fold();
//SF050111]

/** evaluateAsInt
 *  Evaluate "this" expression as int.
 *  "this" should be an evaluable expression.
 *  If not, this method returns 0.
 *  It is strongly recommended to confirm isEvaluable() returns true
 *  for this expression before calling this method.
 *  @return integer value as the result of evaluation.
**/
//SF050111 int evaluateAsInt();
public int evaluateAsInt(); //SF050111

/**
 * Evaluate "this" expression as long.
 * "this" should be an evaluable expression. If not, this method returns 0.
 * @return  long value as the result of evaluation.
**/
public long evaluateAsLong(); //SF050111

/** evaluateAsFloat
 *  Evaluate "this" expression as float.
 *  "this" should be an evaluable expression.
 *  If not, this method returns 0.0.
 *  It is strongly recommended to confirm isEvaluable() returns true
 *  for this expression before calling this method.
 *  @return float value as the result of evaluation.
**/
//SF050111 float evaluateAsFloat();
public float evaluateAsFloat(); //SF050111

/** evaluateAsDouble
 *  Evaluate "this" expression as double.
 *  "this" should be an evaluable expression.
 *  If not, this method returns 0.0.
 *  It is strongly recommended to confirm isEvaluable() returns true
 *  for this expression before calling this method.
 *  @return float value as the result of evaluation.
**/
//SF050111 double evaluateAsDouble();
public double evaluateAsDouble(); //SF050111

/** getValueString //##40
 * Evaluate this subtree and return the result as a string.
 * If the result is constant, then return the string representing
 * the constant.
 * If the result is not a constant, then return a string
 * representing the resultant expression.
 *  It is strongly recommended to confirm isEvaluable() returns true
 *  for this expression before calling this method.
 * @return a string representing the evaluated result.
**/
public String
getValueString();

//##84 BEGIN
/**
 * Adjust the types of binary operands according to the
 *  C language specifications
 *  (See ISO/IEC 9899-1999 Programming language C section 6.3.1.8).
 * The result is an expression
 * (HIR.OP_SEQ, adjusted_operand1, adjusted_operand2).
 * The operands can be get by
 *    ((HIR)lResult.getChild1()).copyWithOperands()
 *    ((HIR)lResult.getChild2()).copyWithOperands()
 * @param pExp1 operand 1.
 * @param pExp2 operand 2.
 * @return (HIR.OP_SEQ, adjusted_operand1, adjusted_operand2)
 */
public Exp
adjustTypesOfBinaryOperands( Exp pExp1, Exp pExp2 );
//##84 END

/** initiateArray //##15
 *  Create loop statement to initiate all elements of
 *  the array pArray and append it to the initiation block
 *  of subprogram pSubp.
 *  The initiation statement to be created for pSubp is
 *    for (i = pFrom; i <= pTo; i++)
 *      pArray[i] = pInitExp;
 *  If pSubp is null, set-data statement is generated.
 *  @param pArray array variable expression.
 *  @param pInitExp initial value to be set.
 *  @param pFrom    array index start position
 *  @param pTo      array index end   position
 *  @param pSubp    subprogram containing the initiation statement.
 *                   null for global variable initiation.
 *  @return the loop statement to set initial value.
**/
public Stmt
initiateArray(
    Exp pArray, Exp pInitExp,
    Exp pFrom, Exp pTo, Subp pSubp ); //##15

} // Exp interface
