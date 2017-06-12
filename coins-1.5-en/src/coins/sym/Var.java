/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;

import coins.ir.hir.Exp;

//==================================================================//
//                                 Nov. 27, 2000.
//                                 (##2): modified on Nov. 2000.

/** Var interface
 *  Variable interface
 *    Var    Variable
 *    Param  Formal parameter
 *    Elem   struct/union element
 *    Field  Class field
 *
 * Deleted methods     (##2)
 *   getType, setType, setSize, setDimension
**/

public interface
//##60 Var extends OperandSym
Var extends Sym //##60
{

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
 *      return null if this is the last one in the same construct.
**/
Var
getNext( );

/** setNext
 *  Set the variable specified by parameter as the one next to this symbol,
 *  and make the original next variable of this symbol as the one
 *  next to pNext (that is, insert pNext after this symbol).
 *  (Default ordering is the order of creation by DefineUnique, Define,
 *   Redefine.)
 *  "this" is a variable.
 *  @param pNext variable to be set as the next one. It should be defined
 *      in the same construct as this variable.
**/
void
setNext( Var pNext );

/** getSize
 *  isSizeEvaluable
 *  Get the size of this variable in byte if it is evaluable.
 *  "this" may be any variable.
 *  getSize rerturn the size of the type of this variable if
 *      it is evaluable. If it is not evaluable, return 0.
 *  isSizeEvaluable return true if the size of the type of this
 *      variable is evaluable, false otherwise.
**/
long    getSize( );
boolean isSizeEvaluable( );

/** getDimension
 *  Get the dimension of this variable.
 *  "this" is a variable.
 *  @return the dimension of this variable if this is an array variable.
 *      1: 1-dimensinal array, 2: 2-dimensional array, ,,, .
 *      If this is not array variable, return 0.
**/
int
getDimension( );

/** getInitialValue
 *  Get the expression of initial value for this variable.
 *  "this" may be any variable that may have initial value.
 *  getInitialValue return initial value expression subtree
 *      if initial value is given, return null if no initial value
 *      is specified.
**/
Exp  getInitialValue( );

/** setInitialValue
 *  Set the expression of initial value for this variable.
 *  "this" may be any variable that may have initial value.
 *  @param pInitialValue initial value expression subtree.
 *  setInitialValue Set initial value expression for this variable.
**/
void setInitialValue( Exp pInitialValue );

/** getStorageClass
 *  Get the storage class of the variable.
 *  "this" may be any variable.
 *  getStorageClass; return the storage class of the variable
 *      (VAR_STATIC, VAR_AUTO, VAR_REGISTER)
 * @return the storage class.
**/
int  getStorageClass( );

/** setStorageClass
 *  Set the storage class of the variable.
 *  "this" may be any variable.
 *  @param pStorageClass storage class to be set to this variable
 *      (VAR_STATIC, VAR_AUTO, VAR_REGISTER)
**/
void setStorageClass( int pStorageClass );

/** getVisibility
 *  Get the visibility attribute of the variable.
 *      (SYM_EXTERN, SYM_PUBLIC, SYM_PROTECTED, SYM_PRIVATE,
 *       SYM_COMPILE_UNIT)
 * @return the visibility attribute.
**/
int  getVisibility( );

/** setVisibility
 *  Set the visibility attribute of the variable.
 *  "this" may be any variable.
 *  @param pVisibility visibility attribute to be set to this variable
 *      (SYM_EXTERN, SYM_PUBLIC, SYM_PROTECTED, SYM_PRIVATE,
 *       SYM_COMPILE_UNIT)
**/
void setVisibility( int pVisibility );

//##78 public ExpId
//##78 getExpId();

//##62 public void
//##62 setExpId( ExpId pExpId );

//==== Constants ====//

  public static final int  // Storage class
    VAR_STATIC   = 6,  // Life time spans entire execution.
    VAR_AUTO     = 7,  // Life time begins at entry to a subprogram
                       // and ends at exit from the subprogram.
    VAR_REGISTER = 8;  // Special case of VAR_AUTO, where access to the
                       // variable is desirable to be as first as
                       // possible.
    // See SYM_EXTERN and SYM_PUBLIC in Sym.java.
    // Note for C language:
    //   A static variable defined outside subprograms in C has
    //   VAR_STATIC and SYM_COMPILE_UNIT attributes.
    //   A static variable defined within a subprogram in C has
    //   VAR_STATIC and SYM_PRIVATE attributes,
    //   A local variable defined within a subprogram as
    //   auto in C has VAR_AUTO and SYM_PRIVATE attributes.
    //   A local variable defined within a subprogram as
    //   register in C has VAR_REGISTER and SYM_PRIVATE attributes.
    //   A global variable defined outside ubprograms in C without
    //   having extern modifier has SYM_PUBLIC attribute and has not
    //   VAR_REGISTER attribute even if it has register modifier.
    //   REGISTER in C has VAR_REGISTER and SYM_PRIVATE attributes.

   public final String
     STORAGECLASS[] = {
       "", "", "", "", "", "", "static", "auto", "register"
     };

  /** Available flags
   *  Sym.FLAG_SIZEOF_TAKEN
   *  Sym.FLAG_ADDRESS_TAKEN
   *  Sym.FLAG_POINTER_OPERATION
   *  Sym.FLAG_AREG_ALLOCATED
   *  Sym.FLAG_DERIVED_SYM
   *  Sym.FLAG_GENERATED_SYM
  **/

//////////////////// S.Fukuda 2002.10.30 begin
public Object evaluateAsObject();
//////////////////// S.Fukuda 2002.10.30 end

} // Var interface


