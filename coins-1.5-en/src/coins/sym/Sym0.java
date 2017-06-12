/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;

import coins.ir.IrList;
import coins.ir.hir.Exp;

//===================================================================//
//                           Added on Nov. 2004.

/** Sym0 interface
<PRE>
 * Simplified Symbol interface (Sym0).
 *
 * Sym0 is the simplified interface of Sym (Symbol).
 * Simple compiler can be built by using IR0, HIR0, and Sym0.
 * Advanced methods to make complicated compilers will be find in
 * the interface IR which extends IR0, HIR which extends HIR0, and
 * Sym which extends Sym0.
 *
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
Sym0 //SF040730
{

//==== Factory methods to build a program symbol object ==========//

/** Make constant object corresponding to pChar.
 *  @param pChar Character representing the constant.
 *  @param pType Type of the constant object to be created;
 *      It may be typeChar or typeU_Char of SymRoot.
 *  @return constant object of the type specified by pType.
**/
public CharConst
charConst( char pChar, Type pType );

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
public IntConst
intConst( long pLongConst, Type pType );

/** Make constant object corresponding to pDoubleConst.
 *  @param pDoubleConst Double constant representing the constant.
 *  @param pType Type of the constant object to be created;
 *      It may be typeFloat, typeDouble of SymRoot;
 *      For LongDouble, this method can not be used, but use
 *      another floatConst method.
 *  @return constant object of the type specified by pType.
**/
public FloatConst
floatConst( double pDoubleConst,  Type pType );

/** Make a constant named as pInternedName.
 *  @param pInternedName Name of Sym whose kind is
 *      Sym.KIND_NAMED_CONST to represent the constant.
 *  @param pConst Constant symbol to be named.
 *      If it is an object that can be converted to integer,
 *      index of the named constant is computed.
 *  @return the named constant object.
**/
public NamedConst
namedConst( String pInternedName, Const pConst );

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
 *  derived from pInternedName.
 *  If pInternedName has heading or trailing quotes, they are
 *  treated as a part of the pure string.
 *  getStringBody() will give the pure string.
 *  getName() or getSymName() will give a string that is composed
 *  of heading quote '"', pure string, and trailing quote '"'.
 *  Debug print method (toString()) will change the pure string
 *  to the form of Java String adding heading, trailing '"',
 *  and escape characters.
 *  The type of the string constant is set as a vector of characters
 *  with length given by getStringLength() of SourceLanguage.
 *  See makeJavaString(), makeCstring(), makeCstringWithTrailing0().
 *  @param pInternedName string from which StringConst is to be made.
 *  @return the string constant (StringConst object).
</PRE>
**/
public StringConst
stringConst( String pInternedName );

/** defineVar
 *  Define a variable with name shown by pInternedName
 *  in the current symbol table (symTableCurrent of SymRoot).
 *  It is treated as a local variable of SymRoot.subpCurrent
 *  or global variable if SymRoot.subpCurrent is null.
 *  See seach, searchLocal of SymTable for searching defined variable.
 *  @param pInternedName Character string representing the name
 *      of the variable to be defined.
 *  @param pType Type of the variable to be defined.
 *  @return the Var object created;
 *      If there is already a symbol with the same name
 *      in the current symbol table (symTableCurrent), then
 *      issue message and return null.
**/
public Var
defineVar( String pInternedName, Type pType );

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
 *  @param pType Type of the parameter to be defined.
 *  @return the Param object created;
 *      If there is already a symbol with the same name
 *      in the current symbol table (symTableCurrent), then
 *      issue message and return null.
**/
public Param
defineParam( String pInternedName, Type pType );

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
 *  @param pType Type of the element to be defined.
 *  @return the Elem object created;
 *      If there is already a symbol with the same name
 *      in the current symbol table (symTableCurrent), then
 *      issue message and return null.
**/
public Elem
defineElem( String pInternedName, Type pType );

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
public Subp
defineSubp( String pInternedName, Type pType );

/** defineLabel
 *  Define a label named as pInternedName in the symbol table
 *  of current subprogram (symRoot.symTableCurrentSubp).
 *  See seachLocal of SymTable for searching defined label.
 *  @param pInternedName Name of the label.
 *  @return the label symbol;
 *      If there is already a label with the same name, then
 *      message is issued and null is returned.
**/
public Label
defineLabel( String pInternedName );

//==== Factory methods to build a type symbol object ============//

/** vectorType with element count given as integer value
 *  Get the vector type that is composed of element type pElemType
 *  and element count pElemCount. The lower bound is assumed to be 0.
 *  Its name is
 *  <VECT elemCount 0 elemType>.
 *  where, elemCount is integer constant represented by pElemCount,
 *  elemType is the type represented by pElemType.
 *  If a vector type with the same element type and element count
 *  is found, then it is returned.
 *  If not found, then VectorType object is created and returned.
 *  The vector type is searched or created in the same symbol
 *  table where pElemType is defined.
 *  @param pElemType type of the vector element.
 *  @param pElemCount number of elements in the vector type;
 *      If pElemCount is 0, FLAG_INCOMPLETE_TYPE is set.
 *  @return the vector type.
**/
public VectorType                               //##54
vectorType( Type pElemType, long pElemCount );  //##54

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
public VectorType
vectorTypeUnfixed(Type pElemType, long pLowerBound);
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
public PointerType
pointerType( Type pPointedType );

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
 *  If some element is unfixed-size (getFlag(Sym.FLAG_UNFIXED_SIZE) //##64
 *  is true), then the resultant StructType become unfixed-size //##64
 *  and message will be issued informing that. //##64
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
public StructType
structType( IrList pElemList, Sym pTag );

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
 *  If some element is unfixed-size (getFlag(Sym.FLAG_UNFIXED_SIZE) //##64
 *  is true), then the resultant UnionType become unfixed-size //##64
 *  and warning message will be issued informing that. //##64
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
public UnionType
unionType( IrList pElemList, Sym pTag );

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
public EnumType
enumType( IrList pElemList, Sym pTag );

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
public DefinedType
definedType( String pInternedName, Type pOrigin );

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
public SubpType  //##53
subpType( Type pReturnType, IrList pParamList,
           boolean pOptionalParam, boolean pNoParamSpec,
           Sym pDefinedIn );

//==========================================//
//   Methods to get/set symbol information  //
//==========================================//

/** getName
* Get the name of this symbol.( The same name as in the source file. )
* If this is a reserved name with preceeding dot ('.'), the dot
* is not included in the resultant string.
* @return String symbol name.
**/
public String
getName();

/** getNextSym
 *  Get the next symbol recorded in the symbol table.
 *  It is recommended not to use this method but to use
 *  getSymIterator() of SymTable to traverse all symbols defined.
 *  @return the next symbol.
**/
public Sym
getNextSym( );

/** getUniqueName
 * Get the UniqueName of this symbol.
 * The unique name is generated to avoid name-conflict for
 * symbols having the same name with different scope
 * in the compile unit. There is no same
 * unique name in the given compile unit.
 * The generated unique name of symbol xxx will take the form
 * of s_xxx_n, where s is getDefinedInName()
 * and n is a sequence of digits.
 * The unique name of constants take the form of const_n
 * where n is a sequence of digits.
 * If unique name is not given (e.g. global symbol),
 * then return getName().
 * @return the unique name.
 * Note The unique name is given by calling
 *     setUniqueNameToAllSym() of SymTable interface;
 *     setUniqueNameToAllSym() is usually called after generating HIR for
 *     all subprograms and immediately before generating LIR,
 *     but it may be called before that time if required.
**/
public String
getUniqueName();

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
public Sym
getDefinedIn();

/** getRecordedIn
 *  Get the symbol table that recorded this symbol.
 */
public SymTable
getRecordedIn( );

/** getSymKind
 *  Get the symbol kind of this symbol (KIND_VAR, KIND_SUBP, etc.).
 *  @return the symbol kind that is set when this symbol is created.
**/
public int
getSymKind();

/** getSymType
 *  Get the type of this symbol.
 *  @return type symbol representing the type of this symbol;
 *      If this is a symbol that has no type (label, key words,
 *       etc.), return null.
**/
public Type
getSymType();

/** getFlag returns the value (true/false) of the flag indicated
 *  by pFlagNumber.
 *  "this" may be any symbol that may have flags.
 *  @param pFlagNumber flag identification number
 *      such as Sym.FLAG_GENERATED_SYM, etc.
 *      As for detail, see getFlag of classes and interfaces
 *      using FlagBox.
**/
public boolean
getFlag( int pFlagNumber);

/** setFlag sets the flag of specified by pFlagNumber.
 *  "this" may be any symbol that may have flags.
 *  @param pFlagNumber flag identification number
 *      such as Sym.FLAG_GENERATED_SYM, etc.
 *      As for detail, see getFlag of classes and interfaces
 *      using FlagBox.
 *  @param pYesNo true or false to be set to the flag.
**/
public void
setFlag( int pFlagNumber, boolean pYesNo);

/** isGlobal
 *  @return true if global symbol,  //## REFINE
 *      false otherwise.
**/
public boolean
isGlobal();

/** getDefinedFile
 *  Get the symbol table entry representing the name of the file
 *  that defined this symbol for the first time.
 *  @return the name entry of the file that defined this symbol;
 *      If this is not declared in any source file, then return null.
**/
public String
getDefinedFile();

//====== Constants ======//

/** Symbol kind number
**/
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

/** Visibility attribute attribute
**/
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

/** Flag numbers
**/
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
    FLAG_COMMON            = 15, // Should be set to true for StructType
                                 // defining Fortran common.     //##53
    FLAG_CASTLESS_SUBP     = 16; // Subprogram that does not request
                                 // casting of actual parameter to
                                 // formal parameter type (as some
                                 // old style C subprogram declaration.)
  static final int
    FLAG_UNFIXED_SIZE  = 17;     // Vector element count is unfixed, that is,
                                 // it is given neither as constant nor as
                                 // as expression. See vectorType(pElemType). //##64
      // Flag numbers 24-31 can be used in each phase for arbitrary purpose.
      // They may be destroyed by other phases.

} // Sym0 interface
