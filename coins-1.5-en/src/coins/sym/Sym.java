/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;

import coins.ir.IrList;
import coins.ir.hir.Exp;

//===================================================================//
//                        //##54: Moved to Sym0 on Nov. 2004
// ## to be DELETED: the method will be deleted in the next version.

/** Sym interface
<PRE>
 * Symbol interface extends Sym0.
 *   Sym instances can be created by methids in this
 *   interface and it is not necessary to use "new" directly.
 *   Read HIR.java before using this interface.
 *   Related interfaces: SymRoot.java, SymTable.java.
 *
 * Relation of the symbol class and related classes:
 *
 *   SymTable // Symbol table class. An instance of SymTable
 *            // is created for each scope defining new symbols
 *            // such as Var, Subp, Type, Const, etc.
 *
 * Sym0  // Simplified symbol class.
 *   |
 *   Sym // Symbol class (super class of all symbol classes).
 *     | //  Symbols in the same scope are contained in the same
 *     | //  symbol table (instance of SymTable).
 *     |
 *     |- OperandSym // Symbol that may be used as an operand
 *     |   |         //  of executable expressions.
 *     |   |
 *     |   |- Var   // Variable that can be assigned a value
 *     |   |   |    //  and variable with const attribute.
 *     |   |   |- Param // Formal parameter class.
 *     |   |   |- Elem  // Class for structure element,
 *     |   |            //  union element, etc.
 *     |   |
 *     |   |- Const // Constant class
 *     |   |   |- IntConst    // integer constant
 *     |   |   |- FloatConst  // floating constant
 *     |   |   |- CharConst   // character constant
 *     |   |   |- StringConst // string constant
 *     |   |   |- BoolConst   // boolean constant
 *     |   |   |- NamedConst  // Named constant including
 *     |   |             //  enumeration constant (other than bool).
 *     |   |- Label  // Statement label class.
 *     |
 *     |- Subp  // Subprogram class (procedures, functions,
 *     |        //  methods, constructors, destructors, etc.)
 *     |
 *     |- Type  // Type information class.
 *     |    |- BaseType    // Base types intrinsic
 *     |    |              //  to many programming languages.
 *     |    |- VectorTyepe // Vector (1 dim. array) type.
 *     |    |- EnumType    // Enumeration type.
 *     |    |- PtrType     // Pointer type.
 *     |    |- StructType  // Structure type.
 *     |    |- UnionType   // Union type.
 *     |    |- SubpType    // Subprogram type.
 *     |    |- RegionType  // Region type to define storage area
 *     |    |              //   shared between subprograms.
 *     |    |- DefinedType // Types defined in a program (by typedef).
 *     |
 *     |- ExpId  // Expression identifier
 *               //  (generated for data flow analysis, etc.)
 *
 * Class of symbols:
 *   It is assumed that class of a symbol can be fixed before
 *   recording it in the symbol table. If it is difficult
 *   to fix its class, then do not record it in the
 *   symbol table until its class can be fixed, or record temporally
 *   as some class and when it become clear that the former class is not
 *   correct, then remove (by remove()) the symbol and record again
 *   as an instance of the proper class.
 *   The later approach is not recommended except for the case where
 *   the former approach is not possible.
 *
 * Relations between symbol table and program constructs:
 *   Several symbol tables (SymTable instance) are constructed
 *   according to the structure of given source program.
 *   At first, a global symbol table symTableRoot is created by
 *   initiate() of SymRoot. Symbols inherent to the HIR and
 *   source language should be recorded in it.
 *   When a new scope of symbols is opened, a new symbol table should
 *   be created and linked to ancestor symbol table that contains
 *   symbols to be inherited by the new scope (by pushSymTable()).
 *   When the current scope is to be closed, the current symbol table
 *   should be closed and the ancestor symbol table should be the
 *   current symbol table again (by using popSymTable()).
 *   A new scope may be opened by subprogram definition,
 *   structure declaration, etc. (BlockStmt may have local symbol
 *   table but it is not automatically created.)
 *   Symbols are searched in the current symbol table and its
 *   ancestors in the reverse order of scope addition.
 *   Popped symbol table is not discarded unless it is empty but
 *   made invisible for search procedures so as to make
 *   inter-procedure optimization and parallelization can be done.
 *   A symbol table usually has corresponding program construct
 *   such as subprogram. There are links between such constructs
 *   and corresponding symbol table to show their correspondence.
 *   The link to the corresponding construct can be get by getOwner()
 *   and getOwnerName() applied to the symbol table (see SymTable).
 *   Anonymous construct may have name generated by the compiler
 *   (such as <STRUCT #1>, subprogName_2, etc.) Symbol table for
 *   symbol-declaring block may have owner named as
 *   subprogName_1, _2, etc.
 *   Following sequence of statements will make the global symbol
 *   table symTableRoot and local symbol table lLocalSymTable
 *   for subprogram "main" to be ready for use.
 *     symRoot  = new SymRoot(ioRoot);
 *     hirRoot  = new HirRoot(symRoot);
 *     symRoot.attachHirRoot(hirRoot);
 *     symRoot.initiate();
 *     sym = symRoot.sym;
 *     lMain = sym.subp("main".intern(), symRoot.typeVoid, null);
 *        ....
 *     lLocalSymTable = symRoot.symTableRoot.pushSymTable(lMain);
 *     (See SimpleMain.java)
 *
 * Scope of symbols:
 *   Source program symbols (symbols appearing in source program)
 *   have their scope as defined by the grammar of the language.
 *   Scope of constants is the entire compile unit.
 *   Scope of temporal variables generated by the compiler
 *   is the subprogram within which the temporal variables are defined.
 *
 * Parameters common to several methods:
 *  pInternedName:
 *     Character string that is made as unique object by intern().
 *     Most String parameters in Sym interface methods should be
 *     interned name. If such parameter has not intern(), comparison
 *     operation may fail and causes bug. Extraneous intern() has no
 *     effect but may decrease compile speed.
 *   pDefinedIn:
 *     Upper construct that defines this symbol such as
 *     subprogram for local variable,
 *     struct/union tag symbol for struct/union element,
 *     enum tag symbol for enumeration constant, etc.
 *     It may be null if there is no upper construct
 *     (in such case as global variable, global subprogram, etc.).
 *
 * Attention:
 *   Sym and SymTable objects should be created by factory methods
 *   (methods to create objects) specified in this interface.
 *   If constructors are invoked directly without using methods
 *   in this interface, some methods might not work correctly
 *   because there are several restrictions to keep consistency
 *   between objects.
 *
*** Abstract syntax of Sym

  Sym ->            // Symbol definition.
     NameString     // Name of the string.
     KindWiseAttr   // Attribute that may differ by symbol kind.
     OptionalInf    // Optional information that may be null.
  KindWiseAttr ->
     OperandSymAttr // Attribute of symbol that may be treated as operand.
   | SubpAttr       // Subprogram attribute.
   | TypeAttr       // Type definition attribute.
   | RegionAttr     // Region attribute.
   | ExpIdAttr      // Expression identifier attribute.
  OperandSymAttr ->
     VarAttr ComputedAttr    // Attributes for variable.
   | ConstAttr ComputedAttr  // Attributes for constant.
   | LabelAttr ComputedAttr  // Attributes for label.
   | RegAttr ComputedAttr    // Attributes for register.
  VarAttr ->
     VarType
     StorageClass
     Visibility
     InitialValue
     ParamAttr
     ElemAttr
  StorageClass ->   // Storage class specification.
     VAR_STATIC     // Life time spans entire execution.
   | VAR_AUTO       // Life time begins at entry to a subprogram
                    // and ends at exit from the subprogram.
   | VAR_REGISTER   // Special case of VAR_AUTO, where access to the
                    // variable is desirable to be as first as possible.
  Visibility ->
     SYM_EXTERN         // External symbol not defined but visible
                        // in this compile unit.
   | SYM_PUBLIC         // Symbol defined in this compile unit (or class)
                        // and visible from other compile unit( or class).
   | SYM_PROTECTED      // Symbol that is defined in a class and
                        // visible in the class and its subclasses.
   | SYM_PRIVATE        // Symbol that is visible only in the class
                        // or subprogram defining the symbol.
   | SYM_COMPILE_UNIT;  // Visible only in the compile unit
                        // defining the symbol.
  InitialValue ->
     Exp                // Initial value expression.
   | ConstValue         // Initial value constant
   | null
  ParamAttr ->
     CallByReference
   | null
  CallByReference ->
     true               // Call by reference
   | false              // Call by value
  ElemAttr ->           // Struct or Union element
     BitFieldAttr
   | null
  BitFieldAttr ->       // Bit field attribute if the element is a bit field.
     BitSize BitOffset
  BitSize ->            // Number of bit positions in the bit field.
     intConstValue
  BitOffset ->          // Bit field offset.
     intConstValue
  ConstAttr ->          // Attribute of constant.
     ConstType ConstValue
  ConstValue ->         // Value of the constant.
     intConstValue
   | floatConstValue
   | charConstValue
   | AggregateConstValue // Value for vector, struct, union.
  AggregateConstValue -> // Constant value for aggregate variable.
     ( ConstValueSeq )   // Each constant corresponds to an element
                         // of the aggregate in the same sequence as
                         // the aggregate elements.
  ConstValueSeq ->
     ConstValue ConstValueSeq
   | null
  LabelAttr ->           // Attributes of label.
     LabelKind           // Kind of the label.
     HirPosition         // HIR node to which the label is attached.
     LirPosition         // LIR node to which the label is attached.
     BasicBlock          // Basic block that starts with the label.
  LabelKind ->
     UNCLASSIFIED_LABEL   // Label not yet classified.
   | ENTRY_LABEL          // Label at subprogram entry.
   | THEN_LABEL           // Label at then-part of IfStmt.
   | ELSE_LABEL           // Label at else-part of IfStmt.
   | LOOP_COND_INIT_LABEL // Label at conditional-init-part of loop
   | LOOP_BACK_LABEL      // Loop-back label.
   | LOOP_BODY_LABEL      // Loop-body label.
   | LOOP_STEP_LABEL      // Loop-step label.
   | SWITCH_CASE_LABEL    // Case-selection label of SwitchStmt.
   | SWITCH_DEFAULT_LABEL // Switch-default label.
   | RETURN_POINT_LABEL   // Label at return point from subprogram.
   | JUMP_LABEL           // Jump target label.
   | CONTINUE_LABEL       // Continue without branch  (a kind of join
                          // point or successive LabeledStmt).
   | SOURCE_LABEL         // Label in source program
                          // (may be jump target label).
   | END_IF_LABEL         // End-if label.
   | LOOP_END_LABEL       // Loop-end label.
   | SWITCH_END_LABEL     // Switch-end label.
  HirPosition ->          // HIR defining the corresponding label.
     LabeledStmt
   | null
  LirPosition ->          // LIR defining the corresponding label.
     LIR
   | null
  BasicBlock ->
     BBlock
   | null
  RegAttr ->              // Attributes of register.
     ARegAttr
   | MRegAttr
  ARegAttr ->             // Abstract (pseudo) register.
     implementationDefined
  MRegAttr ->             // Machine register.
     implementationDefined
  SubpAttr ->             // Attributes of subprogram.
     SubpType             // Type of the subprogram.
     Visibility           // Visibility attribute.
     HirBody              // Definition of subprogram operation in HIR.
     LirBody              // Definition of subprogram operation in LIR.
     ComputedAttr         // Attributes that may be computed when
                          // compilation proceeds.
  HirBody ->
     HIR
   | null
  LirBody ->
     LIR
   | null
  VarType ->              // Type attributes of variables.
     BaseType             // Base type.
   | PointerType          // Pointer type.
   | AggregateType        // Aggregate type.
   | EnumType             // Enumeration type.
   | DefinedType          // Defined by type definition.
  ConstType ->
     BaseType
   | PointerType
   | AggregateType
   | EnumType
   | DefinedType
  TypeAttr ->             // Symbol table contains the specifications
     TypeSpecification    // (description) of types.
  TypeSpecification ->    // Type specification.
     BaseType
   | PointerType
   | AggregateType
   | EnumType
   | SubpType             // SubpType is not in VarType, ConstType.
   | DefinedType
  Type ->
     TypeSpecification    // Type specification.
   | TypeReference        // Reference to a type.
  BaseType ->
     int                  // int
   | short                // short int
   | long                 // long int
   | long_long            // long long int
   | u_int                // unsigned int
   | u_short              // unsigned short int
   | u_long               // unsigned long int
   | u_long_long          // unsigned long long int
   | char                 // character
   | u_char               // unsigned character
   | float                // floating point
   | double               // double floating point
   | long_double          // long double floating point
   | bool                 // boolean
   | void                 // void
   | offset               // address offset
  PointerType ->          // Pointer type.
     ( PTR Type )         // Type: type of pointed object.
  AggregateType ->        // Type of aggregate object.
     VectorType           // Vector type.
   | StructType           // Structure type.
   | UnionType            // Union type.
   | RegionType           // Region type.
  VectorType ->
     ( VECT ElemCount LowerBound Type )
  ElemCount ->            // Number of elements in the vector.
     Exp
   | intConstValue
  LowerBound              // Lower bound of subscript index.
     Exp
   | intConstValue
  StructType ->
     ( STRUCT ElemTypeSeq )
  ElemTypeSeq ->          // Element of struct or union.
     ElemType ElemTypeSeq
   | null
  ElemType ->             // Element type specification.
     ( ElemName Type )
  ElemName ->             // Name of struct/union element.
     Identifier
  UnionType ->
     ( UNION ElemTypeSeq )
  EnumType ->
     ( ENUM EnumSpecSeq )
  EnumSpecSeq ->          // Specification of enumeration sequence.
     EnumSpec EnumSpecSeq
   | null
  EnumSpec ->
     ( EnumName EnumValue )
  EnumName ->             // Enumeration name.
     Identifier
  EnumValue ->            // Enumeration value.
     intConstValue
  SubpType ->             // Type specification of subprogram.
     ( SUBP ( ParamTypeSeq ) OptionalParam ReturnValueType )
  ParamTypeSeq ->         // Sequence of formal parameter types.
     Type ParamTypeSeq
   | null
  OptionalParam ->
     true                 // There is optional parameters that may be abbreviated.
   | false                // There is no optional parameter.
  ReturnValueType ->      // Type of value returned by the subprogram.
     Type
  RegionType ->           // Define storage area shared between compile units.
     ( REGION regionSym ) //
  RegionAttr ->           // List of pairs comprising subprogram and
     ( listCode  attr     // symbol table specifying variables in the region.
       list_of_SubpSymTablePair )
  SubpSymTablePair ->     // Subprogram and symbol table specifying
     Subp SymTable        // variables in the region for the subprogram.
  DefinedType ->          // Type defined in the input program.
     ( TYPEDEF DefinedTypeName OriginType ) // Define the DefinedTypeName
                          // as a type same to OriginType.
   | DefinedTypeName      // Type name defined by ( TYPEDEF .... ).
  DefinedTypeName ->
     Identifier           // Name of the defined type.
   | ( STRUCT Tag )       // Struct identified by tag.
   | ( UNION Tag )        // Union  identified by tag.
   | ( ENUM Tag )         // Enumeration identified by tag.
   | LanguageDependentTypeRepresentation
  TypeReference ->        // Reference to a type.
     BasicType
   | DefinedTypeName
  Tag ->
     Identifier           // Tag name.
  OriginType ->           // The defined type has the same attributes
     Type                 // as the specified origin type.
  ExpIdAttr ->            // Attributes of expression identifier.
     NodePosition         // IR node position represented by the identifier.
     IndexValue           // Index value to be used in flow analysis, etc.
  NodePosition ->
     IR
   | null
  IndexValue ->
     intConstValue
   | null
  ComputedAttr ->         // Attributes that may be computed during compilation.
     FlowInf              // Control-flow/data-flow information.
   | ParallelInf          // Information for parallelization.
   | RegAllocInf          // Information for register allocation.
   | CodeGenInf           // Information for code generation.
   | null
  FlowInf ->
     DefList UseList
   | ...
  DefList ->
     List_of_IR           // List of definition point.
   | null
  UseList ->
     List_of_IR           // List of use points.
   | null
  ParallelInf ->
     implementationDefined
   | null
  RegAllocInf ->
     implementationDefined
   | null
  CodeGenInf ->
     implementationDefined
   | null
  OptionalInf ->          // Optional information.
     UniqueName           // Name unique in the current compilation unit.
   | null

  SymTable ->             // Symbol tables in a compilation unit
     NextBrother          // composes a tree by brother and child link
     FirstChild           // corresponding to the scopes of symbols.
     Owner                // Owner of the symbol table.
     SymSeq               // Sequence of symbols registered in the symbol table.
  NextBrother ->          // Symbol table corresponding to the next scope
     SymTable             // in the same nesting level of this symbol table.
   | null                 // Null if there is no such scope.
  FirstChild ->           // Symbol table corresponding to the scope
     SymTable             // nested in the scope of this symbol table.
   | null                 // Null if there is no nested scope.
  Owner ->                // Program construct to which the symbol table
                          // is attached.
     Subp                 // Subprogram for SymTable local to the subprogram.
   | Struct               // Struct for SymTable local to the struct.
   | Union                // Union  for SymTable local to the union.
   | BlockStmt            // BlockStmt for SymTable local to the block.
   | null                 // Top symbol table may have no owner.
  SymSeq ->               // Sequence of symbols.
     Sym SymSeq
   | null

*** Symbol kind number:
  public static final int   // Symbol kind number. Keep this order.
    KIND_REMOVED          =  0,  // Symbol removed from the symbol table.
    KIND_OTHER            =  1,  // Symbol other than followings.
    KIND_CONST_FIRST      =  2,  // First number in constant kind.
    KIND_BOOL_CONST       =  2,  // Bool constant
    KIND_CHAR_CONST       =  3,  // Character (char, signed char,
                                 // unsigned char)
    KIND_INT_CONST        =  4,  // Integer constant (int, short, long,
                                 //   u_int, u_short, u_long)
    KIND_FLOAT_CONST      =  5,  // Float constant (float, double,
                                 //   long double)
    KIND_STRING_CONST     =  6,  // String constant.
    KIND_NAMED_CONST      =  7,  // Named constant including enumulation.
    KIND_CONST_LAST       =  7,  // Last number in constant kind.
    KIND_VAR              =  8,  // Variable.
    KIND_PARAM            =  9,  // Parameter.
    KIND_ELEM             = 10,  // struct/union element.
    KIND_TAG              = 11,  // struct/union/enum tag.
    KIND_SUBP             = 12,  // Subprogram.
    KIND_TYPE             = 13,  // Type symbol.
    KIND_LABEL            = 14,  // Label.
    KIND_AREG             = 15,  // Abstract register. // to be DELETED
    KIND_MREG             = 16,  // Machine register.  // to be DELETED
    KIND_EXP_ID           = 17;  // Expression identifier.

*** Visibility attribute applied to subprograms and variables.
    SYM_EXTERN    = 1,  // External symbol not defined but visible
                        // in this compile unit.
                        // setVisibility method in Var and Subp will
                        // refuse to change from PUBLIC to EXTERN
                        // but will accept to change from EXTERN to PUBLIC.
    SYM_PUBLIC    = 2,  // Symbol defined in this compile unit or class
                        // and visible from other compile unit or class.
    SYM_PROTECTED = 3,  // Symbol that is defined in a class and
                        // visible in the class and its subclasses.
    SYM_PRIVATE   = 4,  // Symbol that is visible only in the class
                        // or subprogram defining the symbol.
    SYM_COMPILE_UNIT = 5; // Visible only in the compile unit
                          // defining the symbol.
    // See VAR_STATIC, VAR_AUTOMATIC, VAR_REGISTER in Var.java.
    // Note for C language:
    //   A symbol declared as extern and having no definition
    //   in this compile unit has SYM_EXTERN attribute.
    //   A symbol declared as extern but having definition
    //   in this compile unit does not have SYM_EXTERN attribute.

*** Flag numbers used for the symbol.
                       //  They should be a number between 1 to 31.
                       //  Flags applicable to all symbols:
    FLAG_DERIVED_SYM       = 1,  // This is a derived symbol.
    FLAG_GENERATED_SYM     = 2,  // This is a generated symbol.
    FLAG_RESERVED_NAME     = 3,  // Reserved name of Sym (.int, .char, ... ).
                                 // Reserved name begins with dot so that
                                 // it does not conflict with identifiers.
                            // Flags applicable to Var:
    FLAG_SIZEOF_TAKEN      = 5,  // true if sizeof operation is applied
    FLAG_ADDRESS_TAKEN     = 6,  // address is taken (& operator is applied)
    FLAG_POINTER_OPERATION = 7,  // true if pointer operation is applied
    FLAG_AREG_ALLOCATED    = 8,  // Abstract register is allocated.
                            // Flags applicable to Type:
    FLAG_INCOMPLETE_TYPE   = 11, // Incomplete type.
    FLAG_UNIFORM_SIZE      = 12; // Type with fixed size other than pointer
                                 // (and other than SubpType) or
                                 // Vector of fixed number of elements
                                 // whose type has FLAG_UNIFORM_SIZE.
      // Flag numbers 24-31 can be used in each phase for arbitrary purpose.
      // They may be destroyed by other phases.

*** Coding rules applied to all classes in the sym packege:
 *
 * Methods begin with lower case letter.
 * Constants (final static int, etc.) are spelled in upper case letters.
 * Indentation is 2 characters. Tab is not used for indentation.
 * Formal parameters begin with p.
 * Local variables begin with l.
 * Methods and variables are named so that meaning is easily guessed.
 * Short names less than 3 characters are not used except for
 * very local purpose.
 *
</PRE>
**/
public interface
Sym extends Sym0
{

//==== Factory methods to build a program symbol object ==========//

/** boolConst
 *  Make BoolConst object corresponding to pInternedName.
 *  @param pInternedName " true" or " false".
 *  @return boolConstTrue or boolConstFalse that are the same
 *      to those ones in SymRoot corresponding to pInternedName.
**/
//##54 public BoolConst
//##54 boolConst( String pInternedName );

/** boolConst
 *  Make BoolConst object corresponding to pBoolConst.
 *  @param pBoolConst true or false.
 *  @return boolConstTrue or boolConstFalse that are the same
 *      to those ones in SymRoot corresponding to pInternedName.
**/
public BoolConst
boolConst( boolean pBoolConst );

/** Make constant object corresponding to pInternedName.
 *  @param pInternedName Character string representing the constant.
 *  @param pType Type of the constant object to be created;
 *      It may be typeChar or typeU_Char of SymRoot.
 *  @return constant object of the type specified by pType.
**/
public CharConst
charConst( String pInternedName, Type pType );

/** Make constant object corresponding to pChar.
 *  @param pChar Character representing the constant.
 *  @param pType Type of the constant object to be created;
 *      It may be typeChar or typeU_Char of SymRoot.
 *  @return constant object of the type specified by pType.
**/
//##54 public CharConst
//##54 charConst( char pChar, Type pType );

/** Make constant object corresponding to pInternedName.
 *  The string representation of the constant takes the form
 *  specified in the source language. As for C language,
 *  when the last character of pInternedname is a digit,
 *  L is appended for long int, UL for unsigned long int,
 *  U for unsigned int, U for unsigned short.
 *  @param pInternedName Character string representing
 *      an integer constant.
 *  @param pType Type of the constant object to be created;
 *      It may be typeShort, typeInt, typeLong, typeLongLong,
 *      typeU_short, typeU_int, typeU_Long, typeU_LongLong of SymRoot.
 *  @return constant object of the type specified by pType.
**/
public IntConst
intConst( String pInternedName, Type pType );

/** Make integer constant object corresponding to pLongConst.
 *  The string representation of the constant converted from
 *  the binary representation pLongConst takes the form
 *  specified in the source language. As for C language,
 *  L is appended for long int, UL for unsigned long int,
 *  U for unsigned int, U for unsigned short.
 *  @param pLongConst Long integer representing the constant.
 *  @param pType Type of the constant object to be created;
 *      It may be typeShort, typeInt, typeLong, typeLongLong,
 *      typeU_short, typeU_int, typeU_Long, typeU_LongLong of SymRoot.
 *  @return constant object of the type specified by pType.
**/
//##54 public IntConst
//##54 intConst( long pLongConst, Type pType );

/** Make java.lang.Integer object corresponding to pIntValue.
 *  @param pIntValue Integer representing the constant.
 *  @return Integer constant object.
**/
public java.lang.Integer
intObject( int pIntValue );

/** Make constant object corresponding to pDoubleConst.
 *  @param pDoubleConst Double constant representing the constant.
 *  @param pType Type of the constant object to be created;
 *      It may be typeFloat, typeDouble of SymRoot;
 *      For LongDouble, this method can not be used, but use
 *      another floatConst method.
 *  @return constant object of the type specified by pType.
**/
//##54 public FloatConst
//##54 floatConst( double pDoubleConst,  Type pType );

/** Make constant object corresponding to pInternedName.
 *  @param pInternedName Character string representing the constant.
 *  @param pType Type of the constant object to be created;
 *      It may be typeFloat, typeDouble, typeLongDouble of SymRoot.
 *  @return constant object of the type specified by pType.
**/
public FloatConst
floatConst( String pInternedName, Type pType );

/** Make a constant named as pInternedName.
 *  @param pInternedName Name of Sym whose kind is
 *      Sym.KIND_NAMED_CONST to represent the constant.
 *  @param pConst Constant symbol to be named.
 *      If it is an object that can be converted to integer,
 *      index of the named constant is computed.
 *  @return the named constant object.
**/
//##54 public NamedConst
//##54 namedConst( String pInternedName, Const pConst );

/** Make a constant named as pInternedName.
 *  @param pInternedName Name of Sym whose kind is
 *      Sym.KIND_NAMED_CONST to represent the constant.
 *  @param pIndex Index to be assigned to the named constant.
 *  @param pType Type of the constant to be named;
 *      It may be typeChar, typeShort, typeInt,
 *      typeU_Char, typeU_short, typeU_int.
 *  @return the named constant object.
**/
public NamedConst
namedConst(  String pInternedName, int pIndex, Type pType );

/** stringConst
<PRE>
 *  Make a string constant (StringConst object) from
 *  given string pInternedName which has no heading and trailing
 *  quotes ('"').
 *  If the source language requests heading and trailing quotes
 *  in constant representation, they should be pealed off in
 *  pInternedName.
 *  The string constant is recorded as a pure string (processing
 *  escape characters by makeStringBody of coins.SourceLanguage)
 *  derived from pInternedName. The pInterned name may include
 *  escape character to represent some special character as it
 *  is requested by the source language. Such escape characters
 *  are processed according to the grammar of the source language
 *  by makeStringBody to make the corresponding pure string
 *  (string body). For example, a pair of escape character
 *  and special character may be changed to the special character
 *  itself, and in C language, trailing \0 is not included in
 *  the string body.
 *  If pInternedName has heading or trailing quotes, they are
 *  treated as a part of the string body.
 *  getStringBody() will give the pure string.
 *  getName() or getSymName() will give a string that is composed
 *  of heading quote '"', pure string, and trailing quote '"'.
 *  Debug print method (toString()) will change the pure string
 *  to the form of Java String adding heading, trailing '"',
 *  and escape characters according to the Java grammar.
 *  The type of the string constant is set as a vector of characters
 *  with length given by getStringLength() of SourceLanguage.
 *  See makeJavaString(), makeCstring(), makeCstringWithTrailing0().
 *  @param pInternedName Same to the string literal
 *      representaion in source language except that
 *      enclosing quotation marks (if present) were pealed off;
 *      If it has '"' (even at heading and trailing position),
 *      the character is treated as a part of the resultant
 *      pure string.
 *  @return the string constant (StringConst object).
</PRE>
**/
//##54 public StringConst
//##54 stringConst( String pInternedName );

/** stringConstFromQuotedString
 *  Make a string constant (StringConst object) from
 *  given string pInternedName that has heading and trailing
 *  quote '"'. This method is almost the same as stringConst
 *  except that pInternedName has heading and trailing quotes.
 *  The string constant is recorded as pure string
 *  (peeling off the heading and trailing '"' and processing
 *  escape characters by makeStringBody of coins.SourceLanguage)
 *  just like stringConst.
 *  As for other items, the specifications are the same as stringConst.
 *  @param pInternedName string from which StringConst is to be made;
 *      It should have heading and trailing quotes.
 *  @return the string constant (StringConst object).
**/
public StringConst
stringConstFromQuotedString( String pInternedName );

/** bareStringConst
 *  Make a string constant (StringConst object) from
 *  given string pInternedName.
 *  The string constant is recorded as the same sequence
 *  of characters as pInternedName.
 *  @param pInternedName string from which StringConst is to be made.
 *  @return the string constant (StringConst object).
**/
//  public StringConst
//  bareStringConst( String pInternedName );

/** makeJavaString
 *  Change the pure string pStringBody to Java String
 *  representation adding heading, trailing quotes and
 *  escape characters if required.
 *  @param pStringBody String made by makeStringBody of
 *      coins.SourceLanguage.
 *  @return the interned string changed in Java form.
**/
public String
makeJavaString( String pStringBody );

/** makeCstring
 *  Change the pure string pStringBody to C string
 *  representation adding heading, trailing quotes and
 *  escape characters if required.
 *  Trailing \0 is not added as printable character.
 *  See makeCstring of StringConst,
 *      and makeCstringWithTrailing0 of Sym.
 *  @param pStringBody String made by makeStringBody of
 *      coins.SourceLanguage.
 *  @return the interned string changed in C form.
**/
public String
makeCstring( String pStringBody );

/** makeCstringWithTrailing0
 *  Change the pure string pStringBody to C string
 *  representation adding heading, trailing quotes and
 *  escape characters if required.
 *  Trailing \0 is added as printable character.
 *  See makeCstringWithTrailing0 of StringConst.
 *  @param pStringBody String made by makeStringBody of
 *      coins.SourceLanguage.
 *  @return the interned string changed in C form with trailing \0
 *      as printable character; For example, "abc\?d\0"
 *      for a pure string abc?d.
**/
public String
makeCstringWithTrailing0( String pStringBody );

/** defineVar
 *  Define a variable with name shown by pInternedName
 *  in the current symbol table (symTableCurrent of SymRoot).
 *  It is treated as a local variable of SymRoot.subpCurrent
 *  or global variable if SymRoot.subpCurrent is null.
 *  See seach, searchLocal of SymTable for searching defined variable.
 *  @param pInternedName Character string representing the name
 *      of the variable to be defined.
 *  @pType Type of the variable to be defined.
 *  @return the Var object created;
 *      If there is already a symbol with the same name
 *      in the current symbol table (symTableCurrent), then
 *      issue message and return null.
**/
//##54 public Var
//##54 defineVar( String pInternedName, Type pType );

/** defineVar with defined-in parameter
 *  Define a variable with name shown by pInternedName
 *  in the current symbol table (symTableCurrent of SymRoot).
 *  Usage and function are the same as previous defineVar
 *  except that this has pDefinedIn parameter.
 *  @param pDefinedIn outer language construct such as
 *      subprogram that defines the variable.
**/
public Var
defineVar( String pInternedName, Type pType, Sym pDefinedIn );

/** defineParam
 *  Define a paramater with the name shown by pInternedName
 *  in the current symbol table (symTableCurrent of SymRoot)
 *  which will be the local symbol table of current subprogram.
 *  (See pushSymTable() of SymTable.java).
 *  The parameter is assumed to be a parameter of the
 *  current subprogram (symRoot.subpCurrent).
 *  See seach, searchLocal of SymTable for searching defined parameter.
 *  @param pInternedName Character string representing the name
 *      of the parameter to be defined.
 *  @pType Type of the parameter to be defined.
 *  @return the Param object created;
 *      If there is already a symbol with the same name
 *      in the current symbol table (symTableCurrent), then
 *      issue message and return null.
**/
//##54 public Param
//##54 defineParam( String pInternedName, Type pType );

/** defineElem
 *  Define a struct/union element with the name shown by pInternedName
 *  in the current symbol table (symTableCurrent of SymRoot)
 *  which will be the local symbol table of the struct or union
 *  including the element.
 *  It is assumed that a local symbol table is created by
 *  pushSymTable(structUnionType) at struct or union processing
 *  before calling defineElem, where, structUnionType is
 *  the Type object of the struct or union that includes the
 *  element.
 *  The owner (definedIn) of the element is structUnionType.
 *  See searchLocal of SymTable for searching defined element.
 *  @param pInternedName Character string representing the name
 *      of the element to be defined.
 *  @pType Type of the element to be defined.
 *  @return the Elem object created;
 *      If there is already a symbol with the same name
 *      in the current symbol table (symTableCurrent), then
 *      issue message and return null.
**/
//##54 public Elem
//##54 defineElem( String pInternedName, Type pType );

/** defineSubp
<PRE>
 *  Define a subprogram in the current symbol table
 *  symRoot.symTableCurrent.
 *  In order to define a subprogram symbol,
 *    make the subprogram symbol by defineSubp(...),
 *    add formal parameters by addParam(....),
 *    close the subprogram declaration by closeSubpHeader(....)
 *  in such way as
 *    Subp lSubp = symRoot.sym.defineSubp("name".intern(), returnType);
 *    symRoot.symTableRoot.pushSymTable(lSubp);
 *    lSubp.addParam(param1);
 *    lSubp.addParam(param2);
 *    ....
 *    lSubp.setOptionalparam(); // only when optional parameter is given.
 *    lSubp.setVisibility(Sym.SYM_PUBLIC); // only if public.
 *    lSubp.closeSubpHeader();
 *    Var lVi = symRoot.sym.defineVar("i".intern(), symRoot.typeInt); // local symbols
 *    ....
 *    symRoot.symTableCurrent.popSymTable();
 *  Above procedsure will make a subprogram object with
 *  inevitable fields such as parameter list,
 *  return value type, and subprogram type.
 *  closeSubpHeader() will make subprogram type of the form
 *    <SUBP < paramType_1 paramType_2 ... > returnValueType
 *          optionalParam >
 *  where, paramType_1, paramType_2, ... are parameter type,
 *  returnValueType is return value type,
 *  optionalParam is true or false depending on whether optional
 *  parameter (... in C) is specified or not.
 *  In processing prototype declaration (without subprogram body),
 *  use closeSubpPrototype instead of closeSubpHeader.
 *  The subprogram type of a subprogram lSubp can be get
 *  by lSubp.getSymType(), whereas the return value type
 *  can be get by lSubp.getReturnValueType().
 *  Multiple definition of subprogram symbol is allowed
 *  because there may be multiple prototype declaration
 *  or extern declaration for the same subprogram in languages
 *  such as C. It is the task of semantic analyzer to check
 *  the inconsistency between the multiple declarations
 *  and to restrict the update of specifications for previously
 *  declared subprogram.
 *  Until closeSubpHeader() is called for the subprogram lSubp,
 *  lSubp.getSymType() is null, and until subprogram body is
 *  defined, lSubp.getHirBody() is null. These information
 *  may be used in checking the declarations for a subprogram.
 *  See seach method of SymTable for searching subprogram
 *  with specific name.
 *  As for detail, see Subp interface, too.
 *  As for prototype declaration, see also Subp interface.
 *  @param pInternedName Name of the subprogram.
 *  @param pType Return value type; If no value is returned
 *      then specify void type (symRoot.typeVoid).
</PRE>
**/
//##54 public Subp
//##54 defineSubp( String pInternedName, Type pType );

/** defineLabel
 *  Define a label named as pInternedName in the symbol table
 *  of current subprogram (symRoot.symTableCurrentSubp).
 *  See seachLocal of SymTable for searching defined label.
 *  @param pInternedName Name of the label.
 *  @return the label symbol;
 *      If there is already a label with the same name, then
 *      message is issued and null is returned.
**/
//##54 public Label
//##54 defineLabel( String pInternedName );

//==== Factory methods to build a type symbol object ============//

/** baseType
 *  Create an instance of base type.
 *  (Usually, it is not necessary to call baseType method
 *  because all base types are created in SymRoot.)
 *  @param pInternedName name of the base type
 *      ("int", float", etc.).
 *  @param pTypeKind type kind defined in Type.java
 *      (KIND_INT, KIND_FLOAT, etc.).
 *  @return the created Type object.
**/
public BaseType
baseType( String pInternedName, int pTypeKind );

/** vectorType with element count given as integer value
 *  Get the vector type that is composed of element type pElemType
 *  and element count pElemCount. The lower bound is assumed to be 0.
 *  Its name is
 *  <VECT elemCount 0 elemType>.
 *  where, elemCount is integer constant represented by pElemCount,
 *  elemType is the type represented by pElemType.
 *  If a vector type with the same element type, element count
 *  and lower bound is found, then it is returned.
 *  If not found, then VectorType object is created and returned.
 *  The vector type is searched or created in the same symbol
 *  table where pElemType is defined.
 *  @param pElemType type of the vector element.
 *  @param pElemCount number of elements in the vector type;
 *      If pElemCount is 0, FLAG_INCOMPLETE_TYPE is set.
 *  @return the vector type.
**/
//##64 public VectorType
//##64 vectorType( Type pElemType, long pElemCount );

/** vectorType with element count given as expression
 *  Get the vector type that is composed of element type pElemType
 *  and element count pElemCountExp. The lower bound is assumed to be 0.
 *  Its name is
 *  <VECT elemCount 0 elemType>
 *  where, elemCount is integer number represented by pElemCountExp,
 *  elemType is the name of type represented by pElemType
 *  and 0 is lower bound of index.
 *  If a vector type with the same element type, element count
 *  and lower bound is found, then it is returned.
 *  If not found, then VectorType object is created and returned.
 *  The vector type is searched or created in the same symbol
 *  table where pElemType is defined.
 *  @param pElemType type of the vector element.
 *  @param pElemCountExp expression showing the number of elements;
 *      The element count is computed by evaluating this parameter;
 *      If the element count is 0 or pElemCountExp is null,
 *      FLAG_INCOMPLETE_TYPE is set.
 *  @return the vector type.
**/
public VectorType
vectorType(Type pElemType, Exp pElemCountExp);

/** vectorType with element count and lower bound given as integer value
 *  Get the vector type that is composed of element type pElemType
 *  and element count pElemCount, lower bound pLowerBound.
 *  Its name is created by makeVectorTypeName in such form as
 *  <VECT elemCount lowerBound elemType>
 *  where, elemCount is integer constant represented by pElemCountExp,
 *  lowerBound is integer constant represented by pLowerBound,
 *  elemType is the name of type represented by pElemType.
 *  See makeVectorTypeName below.
 *  If a vector type with the same element type, element count
 *  and lower bound is found, then it is returned.
 *  If not found, then VectorType object is created and returned.
 *  The vector type is searched or created in the same symbol
 *  table where pElemType is defined.
 *  @param pTypeName Interned name of this vector type;
 *      It takes the form
 *        <VECT elemCount lowerBound elemType>
 *      where, elemCount is integer number represented by pElemCountExp,
 *      lowerBound is integer number represented by pLowerBound,
 *      elemType is the name of type represented by pElemType;
 *      (See makeVectorTypeName below;)
 *      The name may have const or volatile qualifier.
 *  @param pElemType type of the vector element.
 *  @param pElemCount number of elements in the vector type;
 *      If pElemCount is 0, FLAG_INCOMPLETE_TYPE is set.
 *  @param pLowerBound Lower bound of the subscript of the vector.
 *  @return the vector type.
**/
public VectorType
vectorType( String pTypeName, Type pElemType, long pElemCount, long pLowerBound );

/** vectorType with element count and lower bound as expression
 *  Get the vector type that is composed of element type pElemType
 *  and element count pElemCountExp, lower bound pLowerBoundExp.
 *  Its name is created by makeVectorTypeName in such form as
 *  <VECT pElemCountExp pLowerBoundExp pElemType>
 *  (see makeVecotrTypeName).
 *  If a vector type with the same element type and element count
 *  is found, then it is returned.
 *  If not found, then VectorType object is created and returned.
 *  The vector type is searched or created in the same symbol
 *  table where pElemType is defined.
 *  @param pTypeName Interned name of this vector type;
 *      It takes the form
 *        <VECT elemCount lowerBound elemType>
 *      where, elemCount is the expression represented by pElemCountExp,
 *      lowerBound is the expression represented by pLowerBound,
 *      elemType is the name of type represented by pElemType;
 *      (See makeVectorTypeName below;)
 *      The name may have const or volatile qualifier.
 *  @param pElemType type of the vector element.
 *  @param pElemCountExp Expression representing the number of elements
 *      in the vector type;
 *      The element count is computed by evaluating this parameter;
 *      If pElemCount is 0, FLAG_INCOMPLETE_TYPE is set.
 *  @param pLowerBoundExp Expression representing the lower bound of
 *      the subscript of the vector;
 *      The lower bound is computed by evaluating this parameter.
 *      If pLowerBoundExp is null, then 0 is assumed as lower bound.
 *  @return the vector type.
**/
public VectorType
vectorType( String pTypeName, Type pElemType, Exp pElemCountExp,
            Exp pLowerBoundExp );

//##64 BEGIN
/** vectorType having unfixed number of element of pElemType.
 *  The element count is to be fixed at execution time
 *  and at compile time, it is tentatively treated as 0.
 *  Its name is
 *  <VECT 0 lowerBound elemType>.
 *  where, elemType is the type represented by pElemType and
 *  lowerBound is the value given by pLowerBound.
 *  If a vector type with the same element type, element count
 *  and lower bound is found, then it is returned.
 *  If not found, then VectorType object is created and returned.
 *  The vector type is searched or created in the same symbol
 *  table where pElemType is defined.
 *  For the VectorType with unfixed element count,
 *  getFlag(Sym.FLAG_UNFIXED_SIZE) returns true.
 *  If pElemType is unfixed length type, then warning message is issued.
 *  @param pElemType is the type of the vector element.
 *  @param pLowerBound is the lower bound of subscript.
 *  @return the vector type.
**/
//##64 public VectorType
//##64 vectorTypeUnfixed(Type pElemType, long pLowerBound);

/** vectorType having unfixed number of element of pElemType.
 *  The element count is to be fixed at execution time
 *  and at compile time, it is tentatively treated as 0.
 *  Its name is
 *  <VECT 0 lowerBound elemType>.
 *  where, elemType is the type represented by pElemType and
 *  lowerBound is the expression given by pLowerBoundExp.
 *  If a vector type with the same element type, element count
 *  and lower bound is found, then it is returned.
 *  If not found, then VectorType object is created and returned.
 *  The vector type is searched or created in the same symbol
 *  table where pElemType is defined.
 *  For the VectorType with unfixed element count,
 *  getFlag(Sym.FLAG_UNFIXED_SIZE) returns true.
 *  If pElemType is unfixed length type, then warning message is issued.
 *  @param pElemType is the type of the vector element.
 *  @param pLowerBoundExp is an expression showing the lower bound of subscript.
 *  @return the vector type.
**/
public VectorType
vectorTypeUnfixed(Type pElemType, Exp pLowerBoundExp);
//##64 END


/** pointerType with simple parameter
 *  Get the pointer type that points to an object of
 *  type pPointedType. Its name is
 *  <PTR pPointedType>.
 *  If a pointer type with the same pointed type is found,
 *  then it is returned. If not found, then PointerType object is
 *  created and returned.
 *  The pointer type is searched or created in the
 *  current symbol table (symRoot.symTableCurrent) from where
 *  pPointedType should be visible.
 *  @param pPointedType type of the object to be pointed.
 *  @return the pointer type.
**/
//##54 public PointerType
//##54 pointerType( Type pPointedType );

/** pointerType specifying symbol table
 *  Get the pointer type that points to an object of
 *  type pPointedType. Its name is
 *  <PTR pPointedType>.
 *  If a pointer type with the same pointed type is found,
 *  then it is returned. If not found, then PointerType object is
 *  created and returned.
 *  The pointer type is searched or created in pSymTable.
 *  @param pPointedType type of the object to be pointed.
 *  @param pSymTable the symbol table in which the pointer type
 *      is searched or created, where, the pointed type should
 *      be visible from the symbol table.
 *  @return the pointer type.
**/
public PointerType
pointerType( Type pPointedType, SymTable pSymTable );

/** pointerType with element count
 *  Get a PointeType that points to an object of type pPointedType
 *  with element count. It represents a an array.
 *  This pointer represents a vector whose element count is pElemCount
 *  and lower bound is 0.
 *  Its name is
 *  <PTR pElemCount 0 pPointedType>.
 *  Other items are the same to pointerType.
 *  @param pPointedType type of the object to be pointed.
 *  @param pElemCount number of elements of the vector
 *                     represented by the pointer.
 *  @return the pointer type.
**/
public PointerType
pointerType( Type pPointedType, long pElemCount );

/** pointerType with element count
 *  Get a PointeType that points to an object of type pPointedType
 *  with element count and lower bound. It represents a an array.
 *  This pointer represents a vector whose element count is pElemCount
 *  and lower bound pLowerBound.
 *  Its name is
 *  <PTR pElemCount pLowerBound pPointedType>.
 *  Other items are the same to pointerType.
 *  @param pPointedType type of the object to be pointed.
 *  @param pElemCount number of elements of the vector
 *                     represented by the pointer.
 *  @param pLowerBound subscript lower bound of the array represented
 *                     by the pointer.
 *  @return the pointer type.
**/
public PointerType
pointerType( Type pPointedType, long pElemCount, long pLowerBound );

/** structType with element list and tag
<PRE>
 *  Make an instance of StructType
 *     <STRUCT nameOfTag>
 *  in symRoot.symTableCurrent and prepare for making its
 *  origin type
 *     <STRUCT (elemSym elemType) (elemSym elemType) ... >
 *  where nameOfTag is the name string of pTag, elemSym is
 *  struct element and elemType is its type.
 *  If pElemList is null, addElem of StructType should be called for
 *  each element of the structure in the order of declaration.
 *  After all elements has been added, finishStructType of StructType
 *  should be called to close the declaration of the structure.
 *  If pElemList is null, <STRUCT nameOfTag> is an incomplete type
 *  until finishStructType is called. If pElemList is not null,
 *  the origin type
 *     <STRUCT (elemSym elemType) (elemSym elemType) ... >
 *  is also created as a complete type.
 *  Example of using structType:
 *    As for
 *       struct listNode {
 *         int nodeValue;
 *         struct listNode *next;
 *        } listAnchor, listNode1;
 *    following coding will make corresponding StructType.
 *      Sym lTag = symRoot.symTableCurrent.generateTag("listNode".intern());
 *      StructType lListStruct = sym.structType(null, lTag); // Incomplete type.
 *      PointerType lListPtrType = sym.pointerType(lListStruct);
 *      PointerType lIntPtrType = sym.pointerType(symRoot.typeInt);
 *      symRoot.symTableCurrent.pushSymTable(lListStruct);
 *      Elem lValue = sym.defineElem("nodeValue".intern(), symRoot.typeInt);
 *      Elem lNext  = sym.defineElem("next".intern(), lListPtrType);
 *      lListStruct.addElem(lValue);
 *      lListStruct.addElem(lNext);
 *      lListStruct.finishStructType(true);
 *      symRoot.symTableCurrent.popSymTable();
 *  As for the size, alignment, element displacement of structure type,
 *  see finishStructType in StructType interface.
 *  @param pElemList List of structure element (may be null);
 *      It can be created by statement sequence
 *        IrList lElemList = symRoot.hirRoot.hir.irList();
 *        lElemList.add(elem1); lElemList.add(elem2); ...
 *      If pElemList is not null, it should contain all elements
 *      of the structure.
 *  @param pTag Tag name of the struct;
 *      If it is not given in source program, give it by
 *      generating it by generateTag() of SymTable.
 *  @return StructType instance.
</PRE>
**/
//##54 public StructType
//##54 structType( IrList pElemList, Sym pTag );

/** unionType with element list and tag
<PRE>
 *  Make an instance of UnionType
 *     <UNION nameOfTag>
 *  in symRoot.symTableCurrent and prepare for making its
 *  origin type
 *     <UNION (elemSym elemType) (elemSym elemType) ... >
 *  where nameOfTag is the name string of pTag, elemSym is
 *  union element and elemType is its type.
 *  If pElemList is null, addElem of UnionType should be called for
 *  each element of the union in the order of declaration.
 *  After all elements has been added, finishUnionType of UnionType
 *  should be called to close the declaration of the union.
 *  If pElemList is null, <UNION nameOfTag> is an incomplete type
 *  until finishUnionType is called. If pElemList is not null,
 *  the origin type
 *     <UNION (elemSym elemType) (elemSym elemType) ... >
 *  is also created as a complete type.
 *  @param pElemList List of union element (may be null);
 *      It can be created by statement sequence
 *        IrList lElemList = symRoot.hirRoot.hir.irList();
 *        lElemList.add(elem1); lElemList.add(elem2); ...
 *      If pElemList is not null, it should contain all elements
 *      of the structure.
 *  @param pTag Tag name of the union;
 *      If it is not given in source program, give it by
 *      generating it by generateTag() of SymTable.
 *  @return UnionType instance.
</PRE>
**/
//##54 public UnionType
//##54 unionType( IrList pElemList, Sym pTag );

/** enumType with element list and tag
<PRE>
 *  Make an instance of EnumType
 *     <ENUM nameOfTag>
 *  in symRoot.symTableCurrent and prepare for making its
 *  origin type
 *     <ENUM (enumName enumValue) (enumName enumValue) ... >
 *  where nameOfTag is the name string of pTag, enumName is
 *  enumeration name and enumValue is value.
 *  If pElemList is null, addElem of EnumType should be called for
 *  each element of the enum in the order of declaration.
 *  After all elements has been added, finishEnumType of EnumType
 *  should be called to close the declaration of the enum.
 *  If pElemList is null, <ENUM nameOfTag> is an incomplete type
 *  until finishEnumType is called. If pElemList is not null,
 *  the origin type
 *     <ENUM (enumName enumValue) (enumName enumValue) ... >
 *  is also created as a complete type.
 *  @param pElemList List of the pairs of enum name and value;
 *      It can be created by statement sequence
 *        IrList lElemList = symRoot.hirRoot.hir.irList();
 *        IrList lEnumPair = symRoot.hirRoot.hir.irList();
 *        lEnumPair.add(elem1); lEnumPair.add(symRoot.sym.intObject(value1);
 *        lElemList.add(lEnumPair);
 *        lEnumPair = symRoot.hirRoot.hir.irList();
 *        lEnumPair.add(elem2); lEnumPair.add(symRoot.sym.intObject(value2);
 *        lElemList.add(lEnumPair);
 *        ....
 *  @param pTag Tag name of the enum;
 *      If it is not given in source program, give it by
 *      generating it by generateTag() of SymTable.
 *  @return EnumType instance.
</PRE>
**/
//##54 public EnumType
//##54 enumType( IrList pElemList, Sym pTag );

/** regionType:
<PRE>
 *  Make an instance of RegionType
 *     <REGION regionName>
 *  in symRoot.symTableRoot.
 *  Region is a global area shared between subprograms
 *  and between compile units. A region may have several
 *  symbol tables containing declarations of variables to be
 *  allocated in it.
 *  After all elements has been added to a region in a subprogram,
 *  finishCurrentRegion of RegionType should be called for the region
 *  to close the declaration of the region.
 *  For unnamed region (blank region), regionType is already called in
 *  SymRoot and can be accessed by symRoot.typeRegion,
 *  but it is necessary to add elements and call finishCurrentRegion
 *  when there is unnamed region in given program.
 *  Processing sequence for defining a region is as follows
 *    RegionType lRegionType;   // Region type to be defined.
 *    SymTable lRegionSymTable; // Symbol table to record the
 *                       // elements declared for the region.
 *                       // It may be the symbol table local to
 *                       // the current subprogram.
 *    Subp lCurrentSubp; // Subprogram that includes the
 *                       // declaration of the region.
 *    Var  lRegionVar;   // Aggregate variable that represents
 *                       // the whole elements declared
 *                       // in the region.
 *    Elem lEmem;        // Element declared in the region.
 *    lRegionType = symRoot.sym.regionType(regionName.intern());
 *    lRegionSymTable = symRoot.symTableCurrentSubp;
 *    lRegionType.addSubp(symRoot.subpCurrent, lRegionSymTable);
 *    // For each declaration of variable to be included in
 *    // the region do {
 *      lElem = symRoot.sym.defineElem(....);
 *      lRegionType.addElemToCurrentRegion(lElem);
 *    // }
 *    lRegionType.finishCurrentRegion();
 *  To use the variables included in the region,
 *  treat them in the similar way as structure elements:
 *    lRegionVar = lRegionType.getRegionVar();
 *    Exp lExp = hirRoot.hir.qualifiedExp(
 *       hirRoot.hir.varNode(lRegionVar),
 *       hirRoot.hir.elemNode(lElem));
 *    // See HIR.java for qualifiedExp.
 *  @param pRegionNameString Interned name of the region;
 *  @param pStorageClass give
 *      VAR_STATIC or VAR_AUTO of Var interface.
 *  @return RegionType instance.
</PRE>
**/
public RegionType
regionType( String pRegionNameString, int pStorageClass );

/** definedType with simple parameter
 *  Make a defined type <TYPEDEF pInternedName> having pOrigin
 *  as its origin type.
 *  The defined type will be made in symRoot.symTableCurrent.
 *  @param pInternedName name string composing the defined name;
 *      It may be a tag name. It should be given.
 *  @param pOrigin origin type of the defined type;
 *      It should be given.
 *  @return the defined type <TYPEDEF pInernedName>.
**/
//##54 public DefinedType
//##54 definedType( String pInternedName, Type pOrigin );

/** subpType
 *  Make subprogram type (SubpType) from parameter list, etc.
 *  This method is not recommended to be used directly.
 *  SubpType of a subprogram can be made by closeSubpHeader()
 *  or closeSubpPrototype() of Subp without calling subpType
 *  directly. (See defineSubp(....)).
 *  @param pReturnType return value type;
 *      If no return value, then give symRoot.typeVoid.
 *  @param pParamList Formal parameter list or
 *      IrList of parameter types.
 *  @param pOptionalParam true if optional param ("..." in C)
 *      is present, false if no optional parameter.
 *  @param pDefinedIn null for source languages where
 *      nested subprogram definition is not allowed
 *      (such as C, Fortran), or parent subprogram
 *      which include the subprogram definition for
 *      source languages where nested subprogram definition
 *      is allowed (such as Pascal).
 *  @return the HIR type of the subprogram specified by above
 *      parameters.
 * ***deprecated
**/
//##62 SubpType
//##62 subpType( Type pReturnType, IrList pParamList,
//##62            boolean pOptionalParam, Sym pDefinedIn );

  /** subpType (with pPermitAnyParam)
   *  Make subprogram type (SubpType) from parameter list, etc.
   *  This method is not recommended to be used directly.
   *  SubpType of a subprogram can be made by closeSubpHeader()
   *  or closeSubpPrototype() of Subp without calling subpType
   *  directly. (See defineSubp(....)).
   *  @param pReturnType return value type;
   *      If no return value, then give symRoot.typeVoid.
   *  @param pParamList Formal parameter list or
   *      IrList of parameter types.
   *  @param pOptionalParam true if optional param ("..." in C)
   *      is present, false if no optional parameter.
   *  @param pNoParamSpec true if any number of parameters
   *      of any type are permitted in such case as
   *        extern sub();
   *        sub(a); sub(a, b);
   *      in old C language style.
   *  @param pDefinedIn null for source languages where
   *      nested subprogram definition is not allowed
   *      (such as C, Fortran), or parent subprogram
   *      which include the subprogram definition for
   *      source languages where nested subprogram definition
   *      is allowed (such as Pascal).
   *  @return the HIR type of the subprogram specified by above
   *      parameters.
  **/
//##54  SubpType  //##53
//##54  subpType( Type pReturnType, IrList pParamList,
//##54             boolean pOptionalParam, boolean pNoParamSpec,
//##54             Sym pDefinedIn );

//==== Factory methods to build miscellaneous symbol object =======//

/** symbol
 *  Create a Sym object of pType.
 *  This method is used for creating symbol which can not be
 *  created by other factory methods defined in Sym interface.
 *  Symbols such as variable, constant, subprogram, label,
 *  etc. should not be created by this method but create by
 *  other method (corresponding to the class of the symbol)
 *  definid in Sym interface.
 *  @param pInternedName name of the symbol to be created.
 *  @param pType type of the symbol to be created.
 *  @param pDefinedIn owner symbol.
 *  @return the created symbol object.
**/
public Sym
symbol( String pInternedName, Type pType, Sym pDefinedIn );

/** derivedSym
 *  Generate a symbol having the same type and kind
 *  as that of this symbol in symTableCurrentSubp, or
 *  symTableCurrent if symTableCurrentSubp is null.
 *  The name of the generated symbol begins with the name
 *  of pSym and ending with one of suffixes _1, _2, _3, ... .
 *  The suffix is selected so that
 *  the same name does not appear in the symbol table.
 *  "this" may be any symbol.
 *  @param pSym symbol from which the generated symbol
 *      is to be derived.
 *  @return the generated symbol.
**/
public Sym
derivedSym();

//====== Methods used in factory methods ======//
//
//  It is not recommended to use these methods
//  in methods that are not factory method of Sym.

/** makeVectorTypeName with default lower bound
 *  Make a vector type name of the form
 *    <VECT elemCount 0 elemType>.
 *  where, elemCount is integer number represented by pElemCountExp,
 *  elemType is the name of type represented by pElemType.
 *  The lower bound is assumed to be 0.
 *  @param pElemType Type of the vector element.
 *  @param pElemCount Number of elements in the vector.
 *  @return the type name interned.
**/
public String
makeVectorTypeName( Type pElemType, long pElemCount);

/** makeVectorTypeName
 *  Make a vector type name of the form
 *    <VECT elemCount lowerBound elemType>.
 *  where, elemCount is integer number represented by pElemCount,
 *  lowerBound is integer number represented by pLowerBound,
 *  elemType is the name of type represented by pElemType.
 *  @param pElemType Type of the vector element.
 *  @param pElemCount Number of elements in the vector.
 *  @param pLowerBound Lower bound of the subscript of the vector.
 *  @return the type name interned.
**/
public String
makeVectorTypeName( Type pElemType, long pElemCount, long pLowerBound);

/** makeVectorTypeName
 *  Make a vector type name of the form
 *    <VECT elemCount lowerBound elemType>.
 *  If pElemCountExp is ConstNode, then elemCount is a constant value
 *  else elemCount is the expression string for pElemCountExp,
 *  If pLowerBoundExp is ConstNode, then lowerBound is a constant value
 *  else lowerBound is expression string for pLowerBoundExp,
 *  elemType is the name of type represented by pElemType.
 *  pElemCountExp and pLowerBoundExp may be either
 *     VarNode, ConstNode, or (contents VarNode) representing
 *     a formal parameter value.
 *  // If pElemCountExp/pLowerBoundExp is either VarNode or (contents Varnode)
 *  // then elemCountExp/lowerBoundExp take the form of varName_hashCode
 *  // where varName is the name string of Var represented by the VarNode, and
 *  // hashCode is System.identifyHashCode of the Var.
 *  @param pElemType Type of the vector element.
 *  @param pElemCountExp Expression representing the
 *      number of elements in the vector, or null.
 *  @param pElemCount If pElemCountExp is null, give the number of elements
 *      in the vector, else 0 (which is not used).
 *  @param pLowerBoundExp Expression representing the
 *      lower bound of the subscript of the vector, or null.
 *  @param pLowerBound If pLowerBoundExp is null, give the lower bound of the
 *      subscript of the vector, else 0 (which is not used).
 *  @return the type name interned.
**/
public String
makeVectorTypeName( Type pElemType, Exp pElemCountExp, long pElemCount,
                        Exp pLowerBoundExp, long pLowerBound);

/** Make a string of
 *  <STRUCT Type1 Type2 ... > or
 *  <UNION  Type1 Type2 ... >
 *  where, Type1, Type2, ... are type name of struct/union
 *  element 1, element 2, ... .
 *  @param pStruct true for generating <STRUCT ... >,
 *      false for generating <UNION ... >
 *  @param pElemList list of struct/union elements.
 *  @return the generated name interned.
**/
public String
makeStructUnionTypeName( boolean pStruct, IrList pElemList );

/** Make a string
 *  <ENUM (name1 value1) (name2 value2) ... >
 *  where, name1, name2, ... are the name of enum element 1,
 *  element 2, ... , and value1, value2, ... are the value of
 *  enum element 1, element 2, ... .
 *  @param pElemList list of enum elements.
 *  @return the generated string interned.
**/
public String
makeEnumTypeName( IrList pElemList );

/** Make a string
 *  <SUBP <paramType1 paramType2 ... > optionalParam returnType >
 *  where, paramType1, paramType2, ... are the type name of
 *  parameter 1, parameter 2, ... , and optionalParam is true or
 *  false, and returnType is the type name of return value type.
 *  @param pReturnType type of return value.
 *  @param pParamList list of parameters.
 *  @param pOptionalParam true if optional param is specified,
 *      false otherwise.
 *  @return the generated string.
 *  ***deprecated
**/
//##62 public String
//##62 makeSubpTypeName( Type pReturnType,
//##62                   IrList pParamList, boolean pOptionalParam);

  /** Make a string
   *  <SUBP <paramType1 paramType2 ... > optionalParam returnType >
   *  where, paramType1, paramType2, ... are the type name of
   *  parameter 1, parameter 2, ... , and optionalParam is true or
   *  false, and returnType is the type name of return value type.
   *  @param pReturnType type of return value.
   *  @param pParamList list of parameters.
   *  @param pOptionalParam true if optional param is specified,
   *      false otherwise.
   *  @param pPermitAnyParam is true if any number of parameters
   *      of any type are permitted in such case as
   *        extern sub();
   *        sub(a); sub(a, b);
   *      in old C language style.
   *  @return the generated string.
  **/
  public String  //##53
  makeSubpTypeName( Type pReturnType,
                    IrList pParamList, boolean pOptionalParam,
                    boolean pPermitAnyParam);

//==========================================//
//   Methods to get/set symbol information  //
//==========================================//

/** Get the name of the symbol kind of this symbol
 *  to be used for compiler debug, etc.
 *  @return the name of the symbol kind. It may be
 *      not interned.
**/
String
getSymKindName();

/** getName
* Get the name of this symbol.( The same name as in the source file. )
* If this is a reserved name with preceeding dot ('.'), the dot
* is not included in the resultant string.
* @return String symbol name.
**/
//##54 String getName();

/** getPureName
* Get the name of this symbol.
* If this is a reserved name with preceeding dot ('.'), the dot
* is also included in the resultant string.
* @return String symbol name.
**/
String getPureName();

/** getNameOrNull
 *  If pSym is not null, return its name, else return null.
 *  @param pSym any symbol or null.
 *  @return the name of the symbol or "null".intern().
**/
public String
getNameOrNull( Sym pSym );

/** getNextSym
 *  Get the next symbol recorded in the symbol table.
 *  It is recommended not to use this method but to use
 *  getSymIterator() of SymTable to traverse all symbols defined.
 *  @return the next symbol.
**/
//##54 public Sym getNextSym( );

/** getUniqueName
 * Get the UniqueName of this symbol.
 * The unique name is generated if there is the same name with
 * different scope in the compile unit. There is no same
 * unique name in the given compile unit.
 * The generated unique name of symbol xxx will take the form
 * of s_xxx_n, where s is getDefinedInName()
 * and n is a sequence of digits.
 * The unique name of constants take the form of const_n
 * where n is a sequence of digits.
 * @return the unique name.
 *     If unique name is not given, then return getName().
 * Note The unique name is given by calling
 *     setUniqueNameToAllSym() of SymTable interface;
 *     setUniqueNameToAllSym() is usually called after generating HIR for
 *     all subprograms and immediately before generating LIR,
 *     but it may be called before that time if required.
**/
//##54 String getUniqueName();

 /** setUniqueNameSym
  * Set the UniqueName symbol corresponding to this symbol.
 **/
public void //##70
setUniqueNameSym( Sym pUniqueNameSym ); //##70
//##70 public void
//##70 setUniqueName( String pUniqueName );

//##70 BEGIN
/**
 * Get original symbol corresponding to uniqueNameSym
 * if this is a unique name symbol generated by setUniqueNameToAllSym().
 * If this is not a symbol insymTableUnique, return the symbol itself.
 * @return the original symbol.
 */
public Sym
getOriginalSym();
//##70 END

//##74 BEGIN
/**
 * Get original symbol corresponding to the symbol named pName.
 * pName should be a unique name registered in symTableUnique,
 * if not, then return this.getOriginalSym().
 * @param pName the name of the symbol (in symTableUnique).
 * @return the original symbol.
 */
public Sym
getOriginalSym( String pName);
//##74 END

/** getDefinedIn
 *  Get the name of the construct containing the definition of this symbol
 *  (see defineUnique, Define, etc.).
 *  @return the name of the construct containing the definition of this
 *      symbol, e.g.
 *      subprogram name for its local variables and formal parameters,
 *      structure type or union type for their elements
 *      Return null for symbols that has no sorrounding constructs,
 *      e.g. global variables in C, predefined symbols inherent in
 *      the source language, etc.
**/
//##54 Sym
//##54 getDefinedIn();

/** getDefinedInName
 *  Get the name of getDefinedIn(). If getDefinedIn() is null,
 *  return "";
**/
public String
getDefinedInName();

/** getRecordedIn
 *  Get the symbol table that recorded this symbol.
 */
//##54 public SymTable
//##54 getRecordedIn( );

/** getSymKind
 *  Get the symbol kind of this symbol (KIND_VAR, KIND_SUBP, etc.).
 *  @return the symbol kind that is set when this symbol is created.
**/
//##54 int
//##54 getSymKind();

/** getSymType
 *  Get the type of this symbol.
 *  @return type symbol representing the type of this symbol;
 *      If this is a symbol that has no type (label, key words,
 *       etc.), return null.
**/
//##54 Type
//##54 getSymType();

/** getFlag returns the value (true/false) of the flag indicated
 *  by pFlagNumber.
 *  "this" may be any symbol that may have flags.
 *  @param pFlagNumber flag identification number
 *      such as Sym.FLAG_GENERATED_SYM, etc.
 *      As for detail, see getFlag of classes and interfaces
 *      using FlagBox.
**/
//##54 boolean
//##54 getFlag( int pFlagNumber);

/** setFlag sets the flag of specified by pFlagNumber.
 *  "this" may be any symbol that may have flags.
 *  @param pFlagNumber flag identification number
 *      such as Sym.FLAG_GENERATED_SYM, etc.
 *      As for detail, see getFlag of classes and interfaces
 *      using FlagBox.
 *  @param pYesNo true or false to be set to the flag.
**/
//##54 void
//##54 setFlag( int pFlagNumber, boolean pYesNo);

/** isGlobal
 *  @return true if global symbol,  //## REFINE
 *      false otherwise.
**/
//##54 boolean
//##54 isGlobal();

/** remove
 *  Remove this symbol.
 *  (The symbol instance remains to exist but can not be accessed
 *   after removed.)
**/
public void
remove();

/** isRemoved
 *  @return true if this is a remoded symbol,
 *      false otherwise.
**/
public boolean
isRemoved();

/**
 *  setIndex:
 *  See FlowAnalSym.
**/
// int getIndex();
// void setIndex( int pIndex);

/** getInf
 *  Get additional information (for optimization,
 *    parallelization, etc.) of this symbol.
 *  @return information attached to this symbol,
 *          return null if no information is attached.
**/
SymInf getInf( );

/** getOrAddInf get attached information.
 *  If SymInf is not yet attached, blank SymInf is attached
 *  and it is returned.
**/
SymInf getOrAddInf( );

/** getDefinedFile
 *  Get the symbol table entry representing the name of the file
 *  that defined this symbol for the first time.
 *  @return the name entry of the file that defined this symbol;
 *      If this is not declared in any source file, then return null.
**/
//##54 String
//##54 getDefinedFile();

/** setDefinedFile
 *  Set the name of the file defining this symbol.
 *  It is not recommended to use this method except in parser.
**/
public void
setDefinedFile( String pDefinedFile );

/** getDefinedLine
 *  Get the line number of the first declaration for this symbol.
 *  The line number is relative within a file defining this symbol.
 *  @return the line number of statement that defined this symbol;
 *      If this is not declared in any source file, then return 0.
**/
int
getDefinedLine();

/** setDefinedLine
 *  Set the line number of declaration defining this symbol.
 *  The line number is counted within the file get by getDefinedFile().
 *  It is not recommended to use this method except in parser.
 *  @param pDefinedLine line number of declaration defining this symbol.
**/
public void
setDefinedLine( int pDefinedLine );

/** getDefinedColumn
 *  Get the column number of the first declaration for this symbol.
 *  The column number indicates the column position of this symbol
 *  in the line declaring this symbol.
 *  @return the name entry of the file that defined this symbol;
 *      If this is not declared in any source file, then return 0.
**/
int
getDefinedColumn();

/** Set phase-wise work used for arbitrary purpose in each phase.
 *  The work may be destroyed by other phases (aliased with others).
 *  As for Subp, see getFlowInf(), getOptInf(), getParallelInf(),
 *  getRegAllocInf(), getCodeGenInf(), etc. which can hold information
 *  that will not be destroyed by other phase.
 *  @param pWork an object to be attached to this symbol;
 *      It may contain any information such as Sym, HIR, etc.
**/
public void
setWork( Object pWork );

/** Get phase-wise work used for arbitrary purpose in each phase.
 *  The work may be destroyed by other phases (aliased with others).
 +  @return the object set by setWork.
**/
public Object
getWork();

//------ Methods not recommended to be used ------//
//       except in factory methods to get/set symbol information

/** setDefinedIn
 *  Set "definedIn" symbol of this symbol if it is not set by defineUnique,
 *  Define, and redefine.
 *  @param pDefiningSym name of the construct that include the definition
 *      of this symbol.
 *  Anonymous structure or union shuold be named by GenerateVar so as
 *  pDefiningSym (and pDefinedIn) can be specified.
**/
void
setDefinedIn( Sym pDefiningSym);

/** setRecordedIn
 *  Link to the symbol table that recorded this symbol.
 *  @param pSymTable Symbol table that recorded this symbol.
 */
public void
setRecordedIn( SymTable pSymTable );

/** setSymKind
 *  Set the symbol kind of this symbol to pSymKind
 *  if current kind is not KIND_OTHER, then the kind is not changed.
 *  This method is not recommended to be used execept in the
 *  factory methods of Sym.
 *  @param pSymKind kind number to be set.
 *       (KIND_TAG, etc.)
**/
void
setSymKind( int pSymKind );

/** setSymType
 *  Set the type of this symbol.
 *  This method is not recommended to be used execept in the
 *  factory methods of Sym.
 *  @param pSymType type symbol representing the type of this symbol.
**/
void
setSymType( Type pSymType);

//====== Method for printing ======//

/** toStringShort
 *  Get name and index of this symbol in text which is not interned.
**/
public String
toStringShort();

/** toStringDetail
 *  Get detailed attributes of this symbol in text which is not
 *  interned.
**/
public String
toStringDetail();

//====== Constants ======//

/** Symbol kind number
**/
/* //##54 BEGIN
  public static final int   // Symbol kind number. Keep this order.
    KIND_REMOVED          =  0,  // Symbol removed from the symbol table.
    KIND_OTHER            =  1,  // Symbol other than followings.
    KIND_CONST_FIRST      =  2,  // First number in constant kind.
    KIND_BOOL_CONST       =  2,  // Bool constant
    KIND_CHAR_CONST       =  3,  // Character (char, signed char,
                                 // unsigned char)
    KIND_INT_CONST        =  4,  // Integer constant (int, short, long,
                                 //   u_int, u_short, u_long)
    KIND_FLOAT_CONST      =  5,  // Float constant (float, double,
                                 //   long double)
    KIND_STRING_CONST     =  6,  // String constant.
    KIND_NAMED_CONST      =  7,  // Named constant including enumulation.
    KIND_CONST_LAST       =  7,  // Last number in constant kind.
    KIND_VAR              =  8,  // Variable.
    KIND_PARAM            =  9,  // Parameter.
    KIND_ELEM             = 10,  // struct/union element.
    KIND_TAG              = 11,  // struct/union/Region/enum tag.
    KIND_SUBP             = 12,  // Subprogram.
    KIND_TYPE             = 13,  // Type symbol.
    KIND_LABEL            = 14,  // Label.
    KIND_AREG             = 15,  // Abstract register. // to be DELETED
    KIND_MREG             = 16,  // Machine register.  // to be DELETED
    KIND_EXP_ID           = 17;  // Expression identifier.
*/ //##54 END

/** Symbol kind name
**/
  public static final String
    KIND_NAME[] =  {         "removed",
       "other  ", "boolC  ", "charC  ", "intC   ", "floatC ",
       "stringC", "namedC ", "var    ", "param  ", "elem   ",
       "tag    ", "subp   ", "type   ", "label  ", "areg   ",
       "mreg   ", "expId  "
    };

/** Visibility attribute attribute
**/
/* //##54 BEGIN
  public static final int  // Visibility attribute applied to
                           // subprograms and variables.
    SYM_EXTERN    = 1,  // External symbol not defined but visible
                        // in this compile unit.
                        // setVisibility method in Var and Subp will
                        // refuse to change from PUBLIC to EXTERN
                        // but will accept to change from EXTERN to PUBLIC.
    SYM_PUBLIC    = 2,  // Symbol defined in this compile unit or class
                        // and visible from other compile unit or class.
    SYM_PROTECTED = 3,  // Symbol that is defined in a class and
                        // visible in the class and its subclasses.
    SYM_PRIVATE   = 4,  // Symbol that is visible only in the class
                        // or subprogram defining the symbol.
    SYM_COMPILE_UNIT = 5; // Visible only in the compile unit
                          // defining the symbol.
    // See VAR_STATIC, VAR_AUTOMATIC, VAR_REGISTER in Var.java.
    // Note for C language:
    //   A symbol declared as extern and having no definition
    //   in this compile unit has SYM_EXTERN attribute.
    //   A symbol declared as extern but having definition
    //   in this compile unit does not have SYM_EXTERN attribute.
*/ //##54 END

  public final String
    VISIBILITY[] = {
      "", "extern", "public", "protected", "private",  "compileUnit"
    };
/** Flag numbers
**/
/* //##54 BEGIN
  public static final int     // Flag numbers should be
                              // a number between 1 to 31.
                              // Flags applicable to all symbols:
    FLAG_DERIVED_SYM       = 1,  // This is a derived symbol.
    FLAG_GENERATED_SYM     = 2,  // This is a generated symbol.
    FLAG_RESERVED_NAME     = 3,  // Reserved name of Sym (.int, .char, ... ).
                                 // Reserved name begins with dot so that
                                 // it does not conflict with identifiers.
                              // Flags applicable to Var:
    FLAG_SIZEOF_TAKEN      = 5,  // true if sizeof operation is applied
    FLAG_ADDRESS_TAKEN     = 6,  // address is taken (& operator is applied)
    FLAG_POINTER_OPERATION = 7,  // true if pointer operation is applied
//##54    FLAG_AREG_ALLOCATED    = 8,  // Abstract register is allocated.
    FLAG_VALUE_IS_ASSIGNED = 9,  // Value is assigned.
                              // Flags applicable to Type:
    FLAG_INCOMPLETE_TYPE   = 11, // Incomplete type.
    FLAG_UNIFORM_SIZE      = 12, // Type with fixed size other than pointer
                                 // (and other than SubpType) or
                                 // Vector of fixed number of elements
                                 // whose type has FLAG_UNIFORM_SIZE.
                                 // Flag value is false if the size is not
                                 // statically fixed at compile time.
    FLAG_REGION_ELEM       = 13, // true for elements of RegionType.
                                 // (Automatically set if addElemToCurrentRegion
                                 // is invoked.)
    FLAG_COMPLEX_STRUCT    = 14, // Should be set to true for StructType
                                 // defining complex numbers
                                 // (real_part, imaginary_part).
    FLAG_COMMON            = 15; // Should be set to true for StructType
                                 // defining Fortran common.     //##53
      // Flag numbers 24-31 can be used in each phase for arbitrary purpose.
      // They may be destroyed by other phases.
*/ //##54 END

  // Redefinable combination of symbols having the same spelling.
  // (REDEFINABLE[i][j]=1: i is redefinable as j, =0: unallowable.)
  //  --> This table was moved to SourceLanguage.

//====== Methods to be deleted in the next version ======//

} // Sym interface
