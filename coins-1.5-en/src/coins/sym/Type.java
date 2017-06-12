/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;

import coins.MachineParam;
import coins.ir.IrList;
import coins.ir.hir.Exp;

//========================================//
//                            (##39): Revised on May 2004.
//                            (##9): modified on Oct. 2001.

/** Type interface
<PRE>
 *  Interface for Type class which specifies type information.
 *
** Base type
 *
 *  Following types are base types corresponding to abstract operations
 *  of the HIR model:
 *      bool             boolean (false=0, true=1)
 *      s_char           signed character
 *      short            signed short integer
 *      int              signed integer
 *      long             signed long integer
 *      long_long        signed long long integer
 *      char             character
 *      wchar            multi-byte character
 *      u_char           unsigned character
 *      u_short          unsigned short integer
 *      u_int            unsigned integer
 *      u_long           unsigned long integer
 *      u_long_long      unsigned long long integer
 *      offset           storage address offset, sizeof
 *      void             void
 *      float            floating point
 *      double           floating point double
 *      long_double      floating point long double
 *      string           character string
 *      wstring          multi-byte character string
 *
 *  In some cases, int, long, long_long, u_short, u_int,
 *  u_long, u_long_long are called integer as whole.
 *
** Defined type
 *
 *      pointer          pointer type
 *      vector           vector type (array)
 *      struct           structure type
 *      union            union (overlaid) type
 *      enum             enumeration type
 *      subp             subprogram type
 *      region           region type
 *      defined          type defined by typedef
 *
 *  A pointer type is defined by pointer indication (* in C) and
 *  the type of the target of the pointer.
 *  The value of pointer is integer value representing
 *  storage address.
 *  A vector type is derived from element type by specifying the type
 *  of vector element and the number of elements in the vector.
 *  A structure type is defined by specifying its elements
 *  that represent object different with each other.
 *  A union type is defined by specifying overlaid elements.
 *  A subprogram type is defined by specifying type of parameters
 *  and the type of return value.
 *  An enumeration type is defined by specifying enumeration
 *  literals representing some integer value.
 *  Defined types may be a renaming of base type or a compound
 *  type derived from base type or defined type.
 *
 *  Type defining construct such as typedef in C introduces
 *  a new type name. Declaration
 *      typedef typeSpec typeName;
 *  gives a type name (typeName) to the type specification (typeSpec),
 *  where, let us say typeName can be reduced to typeSpec.
 *  If type1 can be reduced to type2 and type2 can be reduced to type3,
 *  then let us say type1 can be reduced to type3.
 *
** Origin
 *
 *  Type defining construct such as
 *      typedef typeSpec typeName;
 *  introduces a new type (typeName in this case) from type
 *  specification (typeSpec in this case). Let us say the type
 *  described in the specification part (typeSpec) as "origin"
 *  of the introduced type (typeName).
 *  Operations allowed on the origin can be performed on the
 *  introduced type, too.
 *  In Pascal, etc., a new type T1 can be introduced by delimiting
 *  the value range of a type T. In such case, T is called the origin
 *  of the intruduced type T1 in HIR.
 *  Let the origin of basic type be itself except for some cases
 *  that depend on input language specifications. (How to set the
 *  origin for base types is treated as source language parameter.)
 *  In C language, char can be treaed as int in arithmetic operations,
 *  and so, let int be the origin of char in HIR for C language.
 *  In C language, enumeration literals can be treaed as int
 *  constant, and so, let int be the origin of enumeration litrals
 *  in HIR for C language. The bool type also has int as its origin
 *  in HIR for C language.
 *  Type qualifiers such as
 *      const, volatile, restrict
 *  give variation of types. If type T2 is defined by qualifying
 *  a type T with const, volatile or restrict, then T2 is treated as a new
 *  type introduced from the origin T. In this case, the type
 *  qualified by const can not be used as the type of the first
 *  operand of assign operator, and operatons on expression having
 *  the type qualified by volatile can not be deleted in
 *  optimization.
 *
** Type of operands and result
 *
 *  Binary arithmetic operators, comparison operators,
 *  and bit-wise logical operators take the same type as their
 *  operands and result except for some special cases as
 *  listed below:
 *    Operator  Operand 1  Operand 2   Result
 *      add     pointer    offset      pointer
 *      add     offset     offset      offset
 *      sub     pointer    offset      pointer
 *      sub     offset     offset      offset
 *      mult    offset     integer     offset
 *      div     offset     integer     offset
 *      div     offset     offset      u_long
 *      index   offset     integer     offset
 *      index   offset     integer     integer
 *  Pointer is not permitted as operands of mult and div.
 *  More over, following combinations are not permitted (in HIR-base):
 *      add     pointer    pointer
 *      add     pointer    int
 *      add     offset     pointer
 *      add     offset     int
 *      add     int        pointer
 *      add     int        offset
 *      sub     pointer    pointer
 *      sub     pointer    int
 *      sub     offset     pointer
 *      sub     offset     int
 *      sub     int        pointer
 *      sub     int        offset
 *      mult    offset     offset
 *      mult    int        offset
 *      div     int        offset

*** Type kind code
    KIND_UNDEF            = 0, // Not yet defined but defined later.
    KIND_BOOL             = 1,
//##    KIND_S_CHAR           = 2,
    KIND_WCHAR            = 2,  //##51
    KIND_SHORT            = 3,
    KIND_INT              = 4,
    KIND_LONG             = 5,
    KIND_LONG_LONG        = 6,
    KIND_CHAR             = 7,
    KIND_UNSIGNED_LOWER_LIM = 8, // Unsigned type may not have
                                 // kind number less than this.
    KIND_U_CHAR           = 8,
    KIND_U_SHORT          = 9,
    KIND_U_INT            = 10,
    KIND_U_LONG           = 11,
    KIND_U_LONG_LONG      = 12,
    KIND_INT_UPPER_LIM    = 12,
    KIND_ADDRESS          = 13,  // Not used. POINTER is used.
    KIND_OFFSET           = 14,
    KIND_VOID             = 15,
    KIND_FLOAT_LOWER_LIM  = 16,
    KIND_FLOAT            = 16,
    KIND_DOUBLE           = 17,
    KIND_LONG_DOUBLE      = 18,
    KIND_FLOAT_UPPER_LIM  = 19,
    KIND_STRING           = 20,
    KIND_BASE_LIM         = 20, // if (getTypeKind() <= KIND_BASE_LIM)
                                // then it is a base type.
    KIND_ENUM             = 21,
    KIND_POINTER          = 22,
    KIND_VECTOR           = 23,
    KIND_STRUCT           = 24,
    KIND_UNION            = 25,
    KIND_DEFINED          = 26,
    KIND_SUBP             = 27,
    KIND_REGION           = 28;

*** Type conversion between scalar types ***

 Operand x   Result r   Conversion operation
 -------   --------   --------------------
 int       char       The character code of r is the value of x.
           enum       The order number of r is the value of x.
           offset     The value of r is the value of x.
           pointer    The value of r is the value of x.       (##25)
 char      int        The value of r is the character code of x.
           enum       The order number of r is the character code
                      of x.
           offset     Same as (conv offset (conv int (x char))).
 enum      int        The value of r is the order number of x.
           enum       The order number of r is the order number of x.
           offset     Same as (conv offset (conv int (x Ts))).
 offset    int         The value of r is the value of x..
           enum        The order number of r is the value of x.
           pointer     If the value of pointer is signed        (##25)
                       then the value of r is the value of x.
                       If the value of pointer is unsigend      (##25)
                       then the value of r is signed-to-unsigned
                       converted value of x.
 pointer   int        The value of r is the address represented by x. (##25)
           enum       The order number of r is the value of x.    (##25)
           offset     If address is signed
                      then the value of r is the value of x.
                      If address is unsigend
                      then the value of r is unsigned-to-signed
                      converted value of x.
           pointer    Address represented by r is the same to
                      the address represented by x.
                      Whether the internal representation of the
                      objects pointed by x can be treated as an
                      object pointed by r or not is programmer's
                      responsibility.

*** Type conversion rank indexed by type kind code.
  KIND_RANKS[] = { 0: undefined.
    // Operand with lower rank will be casted to higher rank if its
    // type differs from that of other operand of arithmetic operator.
    // The last bit of rank number is 1 if it can be treated as
    // integer ultimately.
    // undefined
       0,
    // bool      wchar    short    int      long     long_long //##51
       11,       31,        31,      41,       51,       61,   //##51
    // char      u_char   u_short  u_int    u_long   u_long_long
       21,       25,       33,       43,       53,       63,
    // address  offset    void
    // 0,        41,        0,
       0,        35,        0,
    // float    double   long_double float_lim string   enum
       70,       72,       74,       0,        0,        35,
    // pointer  vector   struct   union    defined   subprog
       -10,      0,        0,        0,        0,        0
    //  region
        0 };
  }
*** Alignment number indexed by type kind code.
  KIND_ALIGNMENT[] = {     // indexed by type kind code.
    MachineParam.ALIGN_INT,        // undefined // Not used.
    MachineParam.ALIGN_BOOL,       // bool
    MachineParam.ALIGN_WCHAR,      // wchar  //##51
    MachineParam.ALIGN_SHORT,      // short
    MachineParam.ALIGN_INT,        // int
    MachineParam.ALIGN_LONG,       // long
    MachineParam.ALIGN_LONG_LONG,  // long_long
    MachineParam.ALIGN_CHAR,       // char
    MachineParam.ALIGN_CHAR,       // u_char
    MachineParam.ALIGN_SHORT,      // u_short
    MachineParam.ALIGN_INT,        // u_int
    MachineParam.ALIGN_LONG,       // u_long
    MachineParam.ALIGN_LONG_LONG,  // u_long_long
    MachineParam.ALIGN_ADDRESS,    // address  // Not used
    MachineParam.ALIGN_OFFSET,     // offset
    MachineParam.ALIGN_INT,        // void    // Not used.
    MachineParam.ALIGN_FLOAT,      // float
    MachineParam.ALIGN_DOUBLE,     // double
    MachineParam.ALIGN_LONG_DOUBLE,// long_double
    MachineParam.ALIGN_LONG_DOUBLE,// float upper lim
    MachineParam.ALIGN_CHAR,       // string
    MachineParam.ALIGN_INT,        // enum
    MachineParam.ALIGN_ADDRESS,    // pointer
    MachineParam.ALIGN_INT,        // vector // Not used.
    MachineParam.ALIGN_INT,        // struct // Not used
    MachineParam.ALIGN_INT,        // union  // Not used
    MachineParam.ALIGN_INT,        // defined// Not used.
    MachineParam.ALIGN_SUBP        // subp
  };

*** Note about the size of type.

  There are 3 states in type size representation:
    State 1: fSizeExp==null  fSizeValue<0
     No information is given about Size value.
    State 2: fSizeExp!=null  fSizeValue<0
     Size is given as expression. Its size will be given at execution time.
    State 3: fSizeExp!=null  fSizeValue>=0
      Size value is given as constant expression or constant value.
      The size may be 0 in some case.

    In C language, we can deside if a type is incomplete or not
      by seeing whether getSizeExp()==null or not because types of C are
      statically defined.
    fSizeExp/fSizeValue are set by the methods setSizeExp/setSizeValue.
      The method setSizeValue will set not only fSizeValue but also
      set fSizeExp by making the expression representing the value.
      The method setSizeExp will set not only fSizeExp but also
      set fSizeValue by converting the expression into a value
      if the expression is a constant expression.
    If setSizeExp(null) or setSizeValue(-1) are given,
      the type state changes to state 1.
      If setSizeExp(non-constant expression) is given,
      the type state changes to state 2.
      If setSizeExp(constant expression) or setSizeValue(0 or plus value)
      is  are given,  the type state changes to state 3.

</PRE>
**/
public interface
Type extends Sym
{

/** getTypeKind
 *  Get the type kind code (Type.KIND_INT, KIND_FLOAT, KIND_POINTER,
 *      KIND_STRUCT, etc. defined in Type interface).
 *  @return the type kind code of this type.
**/
public int
getTypeKind();

/** getOrigin
 *  @return the origin type of this type.
**/
public Type
getOrigin();

/** getFinalOrigin
 *  Trace the chain of origin types and return the origin as the last one
 *  of the chain.
 *  @return the final origin type of this type.
**/
public Type
getFinalOrigin();

/**
 *  Trace the chain of origin types and return unqualified type.
 *  @return unqualified type of this type.
**/
public Type
getUnqualifiedType(); //SF041014

/** setOrigin
 *  Set the origin type.
 *  @param pOrigin origin type of this type.
**/
public void
setOrigin( Type pOrigin );

/** isBasicType
 *  @return true if this is a basic type, false otherwize.
**/
public boolean isBasicType();

/** isInteger
 *  @return true if this is an integer type, that is, either
 *     int, short, long, long_long,
 *     u_int, u_short, u_long, u_long_long.
**/
public boolean
isInteger();

/** isUnsigned
 *  @return true if this is an unsigned integer type, that is, either
 *     u_int, u_short, u_long, u_long_long.
**/
public boolean
isUnsigned();

/** isUnsigned
 *  @return true if this is a floating point type, that is, either
 *     float, double, long_double.
**/
public boolean
isFloating();

public boolean
isScalar();       //##53

/** getSizeValue
 *  Get the size of this type in bytes.
 *  If size value is given as constant, then return 0 or plus value.
 *  If getSizeValue() < 0 and getSizeExp() != null, then
 *  the size is given as non-constant expression (use getSizeExp()).
 *  If getSizeValue() < 0 and getSizeExp() == null, then
 *  no size information is given (incomplete type).
 *  @return the size of this type.
**/
long getSizeValue();

/** getSizeExp
 *  Get the expression representing the size of this type in bytes.
 *  If the size information is not given, the result is null
 *  (in such case as incomplete type).
 *  If the size is constant expression, then its value can be get
 *  by getSizeValue(). If isSizeEvaluable() is false, then
 *  getSizeExp() should be used instead of getSizeValue() because the
 *  size expression may contain variable that will be defined at execution time.
 *  @return the size expression of this type.
**/
Exp     getSizeExp();

/** isSizeEvaluable
 *  @return true if the size is evaluable as long integer
 *      value, false otherwise.
**/
boolean isSizeEvaluable();

//int     evaluateSize(); // S.Fukuda 2002.10.30
/** setSizeExp
 *  Set the size expression of this type.
 *  If pSizeExp can be evaluated as long integer, then
 *  the size value is adjusted to it.
 *  @param pSizeExp Size expression or null if the size is not yet defined.
**/
void    setSizeExp( Exp pSizeExp );

/** setSizeValue
 *  Set the size of this type.
 *  if pSizeValue >= 0, then the size expression is adjusted to it.
 *  @param pSizeValue size of this type;
**/
void    setSizeValue( long pSizeValue );

/** makeConstType
 *  Make a new type qualifying this type by "const"
 *  and return it.
 *  If this is already a qualified type, then return this.
 *  If this is StructType/UnionType/EnumType,
 *  finishStructType/finishUnionType/finishEnumType should be
 *  called before calling makeConstType/makeVolatileType/makeRestrictType.
 *  Error if const is given for volatile type.
 *  @return the qualified type.
**/
public Type makeConstType();

/** makeVolatileType
 *  Make a new type qualifying this type by "volatile"
 *  and return it.
 *  If this is already a qualified type, then return this.
 *  If this is StructType/UnionType/EnumType,
 *  finishStructType/finishUnionType/finishEnumType should be
 *  called before calling makeConstType/makeVolatileType.
 *  Error if volatile is given to const type.
 *  @return the qualified type.
**/
public Type makeVolatileType();

//##81 BEGIN
/** makeRestrictType
 *  Make a new type qualifying this type by "restrict"
 *  and return it.
 *  If this is already a qualified type, then return this.
 *  If this is StructType/UnionType/EnumType,
 *  finishStructType/finishUnionType/finishEnumType should be
 *  called before calling makeConstType/makeVolatileType/makeRestrict.
 *  @return the qualified type.
**/
public Type makeRestrictType();
//##81 END

/** isConst
 *  @return true if "this" type is qualified as const,
 *              false otherwise.
**/
boolean isConst();

/** isVolatile
 *  @return true if "this" type is qualified as volatile,
 *              false otherwise.
**/
boolean isVolatile();

//##81 BEGIN
/** isRestrict
 *  @return true if "this" type is qualified as restrict,
 *              false otherwise.
**/
boolean isRestrict();
//##81 END

/** getTypeRank
 *  The type rank shows conversion rank.
 *  The type of operand with lower rank is to be converted to the type
 *  of operand with higher rank in binary arithmetic operation.
 *  @return teh conversion rank of this type.
**/
public int
getTypeRank();

/** getDimension
 *  Get the dimension of this type.
 *  "this" is an array type.
 *  @return the dimension of this type if this is an array type;
 *      1: 1-dimensinal array, 2: 2-dimensional array, ,,, ;
 *      If this is not an array type, return 0.
**/
int
getDimension( );

/** getElemList:
 *  Get the list of struct/union elements.
 *  The elements of the list is an instance of Elem.
 *  If this type is neither struct nore union,
 *  then null is returned.
 *  @return the list of elements or null.
**/
public IrList
getElemList();

/** getPointedType
 *  Get the type of pointed object for PointerType.
 *  If this type is not PointerType, then null is returned.
 *  @return the type of pointed object if this is a pointer,
 *    otherwise return null.
**/
public Type
getPointedType();

/** getElemListString
 *  Get element type list of struct, union type in String form.
 *  The elements of the list represents the type name of
 *  struct/union type.
 *  If this type has no element, then "{}" is returned.
 *  @return the list of element type names of struct/union.
**/
public String
getElemListString();

/** getAlignment
 *  Get alignment value for this type.
 *  It depends on target machine.
 *  For basic types, the alignment value is a byte count
 *  specified in MachineParam.java.
 *  The default value of alignment of VectorType is
 *  the alignment of element type which may differ
 *  vector by vector.
 *  The defalut alignment value of StructType or UnionType is set
 *  to that of its element with the largest alignment value
 *  which may differ struct by struct or union by union.
 *  The alignment value of StructType, UnionType, or VectorType
 *  should be set by setAlignment method of corresponding type
 *  when the type is defined.
 *  As for subprogram, if alignment of return value is required,
 *  get it by subp.getReturnValueType().getAlignment(), and
 *  if alignment of subprogram object code is required,
 *  get it by symRoot.machineParam.getAlignment(Type.KIND_SUBP). //##67
 *  @return the alignment number of this type in bytes.
**/
public int
getAlignment();

/** getAlignmentGap
 *  Get alignment gap size if cumulative size of preceeding elements
 *  is pPreceedingSize.
**/
public int
getAlignmentGap( long pPrecedingSize );

/** isCompatibleWith
 *  The origin type of this type and pType are compared to be
 *  equal or not.
 *  Strict type compatibility check sh
 * ould be done in
 *  the semantic analyzer of each language.
 *  This method can not afford to do strict semantic check
 *  considering language specifications.
 *  @param  pType a type to be compared with this type.
 *  @return true if this type is compatible with pType.
**/
public boolean
isCompatibleWith( Type pType );

/** getCompleteType
 *  Get complete type corresponding to this type.
 *  If this is a complete type, return this type.
 *  If this is incomplete, find complete type corresponding to this type.
 *  If no complete type is found for this type, return null.
 *  If the type is neither VectorType, StructType, UnionType,
 *  nore  EnumType, then return this type.
**/
public Type
getCompleteType();

//====== Constants ======//

/** Type kind code
**/
  public static final int      // Type kind code (##4)
                               // Keep this sequence at extension.
    KIND_UNDEF            = 0, // Not yet defined but defined later.
    KIND_BOOL             = 1,
//##    KIND_S_CHAR           = 2,
    KIND_WCHAR            = 2,  //##51
    KIND_SHORT            = 3,
    KIND_INT              = 4,
    KIND_LONG             = 5,
    KIND_LONG_LONG        = 6,
    KIND_CHAR             = 7,
    KIND_UNSIGNED_LOWER_LIM = 8, // Unsigned type may not have
                                 // kind number less than this.
    KIND_U_CHAR           = 8,
    KIND_U_SHORT          = 9,
    KIND_U_INT            = 10,
    KIND_U_LONG           = 11,
    KIND_U_LONG_LONG      = 12,
    KIND_INT_UPPER_LIM    = 12,
    KIND_ADDRESS          = 13,  // Not used. POINTER is used.
    KIND_OFFSET           = 14,
    KIND_VOID             = 15,
    KIND_FLOAT_LOWER_LIM  = 16,
    KIND_FLOAT            = 16,
    KIND_DOUBLE           = 17,
    KIND_LONG_DOUBLE      = 18,
    KIND_FLOAT_UPPER_LIM  = 19,
    KIND_STRING           = 20,
    KIND_BASE_LIM         = 20, // if (getTypeKind() <= KIND_BASE_LIM)
                                // then it is a base type.
    KIND_ENUM             = 21,
    KIND_POINTER          = 22,
    KIND_VECTOR           = 23,
    KIND_STRUCT           = 24,
    KIND_UNION            = 25,
    KIND_DEFINED          = 26,
    KIND_SUBP             = 27,
    KIND_REGION           = 28;

  public static final int    // Type conversion rank (##9)
  KIND_RANKS[] = {  // indexed by type kind code. 0: undefined.
    // Operand with lower rank will be casted to higher rank if its
    // type differs from that of other operand of arithmetic operator.
    // The last bit of rank number is 1 if it can be treated as
    // integer ultimately.
    // undefined
       0,
    // bool      wchar    short    int      long     long_long  //##51
       11,       31,        31,      41,       51,       61,    //##51
    // char      u_char   u_short  u_int    u_long   u_long_long
       21,       25,       33,       43,       53,       63,
    // address  offset    void
    // 0,        41,        0,
       0,        45,        0,

    // float    double   long_double float_lim string   enum
       70,       72,       74,       0,        0,        35,
    // pointer  vector   struct   union    defined   subprog
       -10,      0,        0,        0,        0,        0,
    // region
        0 };

/** Alignment number indexed by type kind code.
 *  This is set by SymRoot.
**/
  public static int               //##51
  KIND_ALIGNMENT[] = new int[29]; //##51
  /* //##51
  public static final int
  KIND_ALIGNMENT[] = {
    MachineParam.ALIGN_INT,        // undefined // Not used.
    MachineParam.ALIGN_BOOL,       // bool
    MachineParam.ALIGN_WCHAR,      // wchar  //##51
    MachineParam.ALIGN_SHORT,      // short
    MachineParam.ALIGN_INT,        // int
    MachineParam.ALIGN_LONG,       // long
    MachineParam.ALIGN_LONG_LONG,  // long_long
    MachineParam.ALIGN_CHAR,       // char
    MachineParam.ALIGN_CHAR,       // u_char
    MachineParam.ALIGN_SHORT,      // u_short
    MachineParam.ALIGN_INT,        // u_int
    MachineParam.ALIGN_LONG,       // u_long
    MachineParam.ALIGN_LONG_LONG,  // u_long_long
    MachineParam.ALIGN_ADDRESS,    // address
    MachineParam.ALIGN_OFFSET,     // offset
    MachineParam.ALIGN_INT,        // void    // Not used.
    MachineParam.ALIGN_FLOAT,      // float
    MachineParam.ALIGN_DOUBLE,     // double
    MachineParam.ALIGN_LONG_DOUBLE,// long_double
    MachineParam.ALIGN_LONG_DOUBLE,// float upper lim
    MachineParam.ALIGN_CHAR,       // string
    MachineParam.ALIGN_INT,        // enum
    MachineParam.ALIGN_ADDRESS,    // pointer
    MachineParam.ALIGN_INT,        // vector // Not used.
    MachineParam.ALIGN_INT,        // struct // Not used
    MachineParam.ALIGN_INT,        // union  // Not used
    MachineParam.ALIGN_INT,        // defined// Not used.
    MachineParam.ALIGN_SUBP,       // subp
    MachineParam.ALIGN_LONG_DOUBLE // set largest size for region
  };
*/ //##51

/** Available flags
 *  Sym.FLAG_GENERATED_SYM
 *  Sym.FLAG_INCOMPLETE_TYPE
 *  Sym.FLAG_UNIFORM_SIZE
**/
}  // Type interface


