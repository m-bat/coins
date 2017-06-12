/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;

import coins.SymRoot;
import coins.flow.DefUseList; //##63
import coins.flow.DefUseListImpl; //##63
import coins.ir.IR;
import coins.ir.IrList;
import coins.ir.hir.Exp;

/** VarImpl Class represents Variable symbols.
 *
 *  Sym --- Symbol class (super class of all symbol classes).
 *   |
 *   |- OperandSym
 *   |   |
 *   |   |- Var --- Variable that can be assigned a value <---- This!
 *   |   |    |       and variable with const attribute.
 *   |   |    |- Param --- Formal parameter class.
 *   |   |    |- Elem  --- Class for structure element,
 *   |   |                 union element, etc.
 */


public class
//##60 VarImpl extends OperandSymImpl implements Var, FlowAnalSym
VarImpl extends SymImpl implements Var, FlowAnalSym //##60
{
  /** Next variable in the same scope and the same kind.*/
  private Var
    fNextVar = null;

  /** Dimention */
  private int
    fDimension = 0;	  // Dimension 0: scalar, 1: 1-dimension array.

  /** Strage class
   *   (VAR_STATIC, VAR_AUTO, VAR_REGISTER)
  **/
  private int
    fStorageClass = VAR_AUTO;

  /** Visibility attribute
   *   (SYM_EXTERN, SYM_PUBLIC, SYM_PROTECTED, SYM_PRIVATE,
   *    SYM_COMPILE_UNIT)
  **/
  private int
    fVisibility = Sym.SYM_PRIVATE;

  /** Initial expression for this variable of value. */
  private Exp
    fInitialExp = null;  // Initial value expression.

  /** Size of this variable in byte */
  private int
    fVarSize = 0;

  /** fExpId ExpId corresponding to this variable if it exists.
  **/
  //##78 protected ExpId
  //##78   fExpId = null;

//---- Fields for FlowAnalSym interface ----//

  /** Definition point and using points of this variable */
  protected coins.flow.DefUseList fDefUseList; //##63
    //##63 fDefUseList = null;

  /** Definition list containing IR nodes that define the
   *   value of this symbol.
  **/
  protected IrList
    fDefList;

  /** Use list containing IR nodes that use (refer) this symbol.
  **/
  protected IrList
    fUseList;

  protected int
    fIndex;    // Index number to be used in flow anal, etc.

  protected java.util.Set
    fOperandSet;  // Set of operands (Var and Reg, excluding Const)

//==== Constructor ====//

public
VarImpl ( SymRoot pSymRoot )
{
  super(pSymRoot);
}

public
VarImpl ( SymRoot pSymRoot, String pName )
{
  super(pSymRoot);
  fName = pName;
  fKind = Sym.KIND_VAR;
  fDefinedIn = symRoot.subpCurrent;
}

/** getNext
 *  Get the next variable having the same scope and the same kind.
 *  The order of symbols is the same as the order of creation by
 *  defineUnique, define, redefine or setting by setNext.
 *  "this" should be a variable.
 *  @return the variable next to this one in the same scope and the same
 *      kind, e.g. the next local variable in the same construct,
 *      the next formal parameter of the same subprogram,
 *      the next element of the same structure,
 *      the next field of the same class, etc.
 *      return null if this is the last one in the same construct. </PRE>
**/
public Var
getNext( ) {
  return fNextVar;
}

/** setNext
 *  Set the variable specified by parameter as the one next to this symbol,
 *  and make the original next variable of this symbol as the one
 *  next to pNext (that is, insert pNext after this symbol).
 *  (Default ordering is the order of creation by DefineUnique, Define,
 *   Redefine.)
 *  "this" is a variable.
 *  @param pNext variable to be set as the next one. It should be defined
 *      in the same construct as this variable. </PRE>
**/
public void
setNext( Var pNext ) {
  fNextVar = pNext;
}

/** getSize
 *  Get the size of this variable in byte if it is evaluable.
 *  "this" may be any variable.
 *  @return the size of the type of this variable if
 *      it is evaluable. If it is not evaluable, return 0.
**/
public long
getSize( ) {
  if (fType != null) {
    return fType.getSizeValue();
  }else
    return 0;
}
/** isSizeEvaluable
 *  Check this variable is evaluable or not..
 *  @return boolean the size of the type of this variable is evaluable true
 *  false otherwise. </PRE>
**/
public boolean
isSizeEvaluable( ) {
  if( fType != null)
    return fType.isSizeEvaluable();
  else
    return false;
}

/** getDimension
 *  Get the dimension of this variable.
 *  "this" is a variable.
 *  @return the dimension of this variable if this is an array variable.
 *      1: 1-dimensinal array, 2: 2-dimensional array, ,,, .
 *      If this is not array variable, return 0. </PRE>
**/
public int
getDimension( ) {
  return fDimension;
}

/** getInitialValue
 *  Get the expression of initial value for this variable.
 *  "this" may be any variable that may have initial value.
 *  @return Exp initial value expression subtree
 *          If no initial value null. </PRE>
*/
public Exp
getInitialValue( ) {
  return fInitialExp;
}
/** setInitialValue
 *  Set the expression of initial value for this variable.
 *  "this" may be any variable that may have initial value.
 *  @param pInitialValue initial value expression subtree.
 *  Set initial value expression for this variable. </PRE>
**/
public void
setInitialValue( Exp pInitialValue ) {
  fInitialExp = pInitialValue;
}

/** getStrageClass
 *  Get the storage class of the variable.
 *  "this" may be any variable.
 *  @return  int the storage class of the variable
 */
public int
getStorageClass(  ) {
  return fStorageClass;
}

/** setStrageClass
 *  Set the storage class of the variable.
 *  "this" may be any variable.
 *  @param pStorageClass storage class to be set to this variable
*/
public void
setStorageClass( int pStorageClass ) {
  fStorageClass = pStorageClass;
}

/** getVisibility
 *  Get the visibility attribute of the variable.
 *     (SYM_EXTERN, SYM_PUBLIC, SYM_PROTECTED, SYM_PRIVATE,
 *      SYM_COMPILE_UNIT). </PRE>
 */
public int
getVisibility() {
  return fVisibility;
}

/** setVisibility
 *  Set the visibility attribute of the variable.
 *  "this" may be any variable.
 *  @param pVisibility visibility attribute to be set by setVisibility.
 *     (SYM_EXTERN, SYM_PUBLIC, SYM_PROTECTED, SYM_PRIVATE,
 *      SYM_COMPILE_UNIT). </PRE>
 */
public void
setVisibility( int pVisibility ) {
  if ((fVisibility == Sym.SYM_PUBLIC)&&
      (pVisibility == Sym.SYM_EXTERN))
    fVisibility = Sym.SYM_PUBLIC;
   else
    fVisibility = pVisibility;
}

/* //##78
public ExpId
getExpId()
{
  return fExpId;
}
 */ //##78

//==== Methods for FlowAnalSym interface ====//

/** getDefUseList
 *  Get DefUseList (list of definition points and use points) of this variable.
 *  "this" is a variable.
 *  @return the DefUseList of this variable if it is computed.
 *      If it is not computed, return null.
 *  Note symRoot.flow should be set before call by FlowRoot.
**/
/* //##63
public coins.flow.DefUseList
getDefUseList( )
{
  if (fDefUseList == null)
  fDefUseList = symRoot.getFlowRoot().aflow.defUseList();
  return fDefUseList;
}

public void
setDefUseList( coins.aflow.DefUseList pDefUseList )
{
  fDefUseList = pDefUseList;
}

public void
addDefPoint( IR pDefNode )
{
  if (fDefList == null)
    fDefList = symRoot.getHirRoot().hir.irList();
  fDefList.add(pDefNode);
} // addDefPoint

public void
addUsePoint( IR pUseNode )
{
  if (fUseList == null)
    fUseList = symRoot.getHirRoot().hir.irList();
  fUseList.add(pUseNode);
} // addUsePoint

public IrList
getDefList()
{
  return fDefList;
}

public IrList
getUseList()
{
  return fUseList;
}
*/ //##63

public int
getIndex()
{
  return fIndex;
}

public void
setIndex( int pIndex)
{
  fIndex = pIndex;
}

public void
resetFlowAnalInf()
{
  //##63 fDefUseList = null;
  fDefList    = null;
  fUseList    = null;
  fDefUseList = null; //##78
  fIndex      = 0;
  //##78 fExpId      = null;  // For Var only.
} // resetFlowAnalInf

public java.util.Set
getOperandSet()
{
  if (fOperandSet == null)
    fOperandSet = new java.util.HashSet();
  return fOperandSet;
}

public void
addOperand( FlowAnalSym pOperand )
{
  if (fOperandSet == null)
    fOperandSet = new java.util.HashSet();
  fOperandSet.add(pOperand);
}

//==== Methods for printing ====//

public String
toStringDetail() {
  String lString = super.toStringDetail();
  lString = lString + " " + Sym.VISIBILITY[fVisibility] +
      "  " + Var.STORAGECLASS[fStorageClass];
  if ((getInitialValue() != null)&&(fDbgLevel >= 4)) { //##64
    lString = lString + " initial " + getInitialValue().getValueString();
  }
  return lString;
} // toStringDetail

/**
 * Get the value of this constant symbol.
 * This method is overrided as follows
 * class       returned value
 * IntConst     Long
 * FloatConst   Double
 * StringConst  String
 * Var          Initail value (ExpListExp, etc.) if qualified by const,
 *              else null.
 * @return the constant value of appropriate type.
*/
public Object evaluateAsObject()
{
  Exp e;
  if( getSymType().isConst() )
    return getInitialValue();
  else
    return null;
}

} // VarImpl class
