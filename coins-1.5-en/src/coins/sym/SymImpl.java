/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
// SymImpl.java
//                        : modified Dec. 2001.
//                        : modified to eraze global symbol.
package coins.sym;

import coins.HasStringObject;
import coins.MachineParam;
import coins.SourceLanguage;
import coins.SymRoot;
import coins.ir.IrList;
import coins.ir.IrListImpl;
import coins.ir.SourceInf;
import coins.ir.SourceInfImpl;
import coins.ir.hir.Exp;
import coins.ir.hir.HIR;
import coins.ir.hir.VarNode;
import coins.ir.hir.ConstNode;

/** SymImpl class that implements Sym.
 *  To use methods in this class, read Sym.java.
 *  Sym interface is written so that methods can be used without
 *  reading SymImpl.java except when user is interested in the
 *  implementation of the methods.
**/

public class
SymImpl implements Sym, HasStringObject, Cloneable
{

//====== Public field ======//

  public final SymRoot
    symRoot;     // Used to access Root fields.

/** The unique name which has been made by this compiler. */
  //##70 public String fUniqueName = null;
  public Sym fUniqueNameSym = null; //##70

//====== Protected/private fields ======//

/** The symbol name in the source file. */
  protected String fName = null; 	// Symbol name within source program.

/** The Symbol kind */
  protected int fKind = 0;

/** The type of this symbol */
  protected Type fType = null;

/** fRecordedIn Symbol table recording this symbol.
**/
  protected SymTable
    fRecordedIn;

/** Upper construct that defines this symbol such as
 *  subprogram for local variable,
 *  struct/union tag name for struct/union element,
 *  enum tag for enumeration constant, etc.
 *  It may be null if there is no upper construct
 *  (in such case as global variable, global subprogram, etc.)
**/
  protected Sym fDefinedIn = null;	//  TO BE DELETED

/** The next symbol */
  protected Sym fNextSym = null;

/** The flag, which represents this symbol's kind by bit mask. */
  protected FlagBox fFlagBox;

/** Information attached to this symbol */
  protected  SymInf fSymInf = null;

/** Source program information --
 *   file name, line number, column defining this symbol.  */
  protected SourceInf fSourceInf = null;

/** Phase-wise work used for arbitrary purpose in each phase */
  protected Object fWork;

//##51
  protected static MachineParam
    machineParam;

  protected static SourceLanguage
    sourceLanguage;
//##51

  protected final int fDbgLevel; //##58

//====== Constructors ======//

public
SymImpl()  // Default constructor used in subclasses.
{
  symRoot = null;
  fFlagBox = (FlagBox)(new FlagBoxImpl());
  fDbgLevel = 0; //##58
}

/** SymImpl Constructor to set symRoot.
**/
public
SymImpl( SymRoot pSymRoot )
{
  symRoot = pSymRoot;
  fFlagBox = (FlagBox)(new FlagBoxImpl());
  fDbgLevel = symRoot.ioRoot.dbgSym.getLevel(); //##58
}

public
SymImpl( SymRoot pSymRoot, String pInternedName ) {
  symRoot = pSymRoot;
  fKind   = Sym.KIND_OTHER;
  fName   = pInternedName;
  fFlagBox = (FlagBox)(new FlagBoxImpl());
  fDbgLevel = symRoot.ioRoot.dbgSym.getLevel(); //##58
} // SymImpl

//====== Methods to create symbol objects ======//

// //##51 BEGIN
/** setParameters makes machineParam and sourceLanguage
 *  available to Sym methods.
 *  setParameters is not recommended to be used except in SymRoot.
 */
public void
setParameters( MachineParam pMachineParam, SourceLanguage pSourceLanguage)
{
  machineParam   = pMachineParam;
  sourceLanguage = pSourceLanguage;
}
// //##51 END

/*
public BoolConst
boolConst( String pInternedName ) {
  return (BoolConst)(symRoot.symTableConst.searchOrAdd( pInternedName,
                           Sym.KIND_BOOL_CONST, null, true, true ));
} // boolConst
*/

public BoolConst
boolConst( boolean pTrueFalse ) {
  if (pTrueFalse)
    return symRoot.boolConstTrue;
  else
    return symRoot.boolConstFalse;
} // boolConst

public CharConst
charConst( String pInternedName, Type pType ) {
  CharConst lCharConst;
  lCharConst = (CharConst)(symRoot.symTableConst.
                 searchOrAdd( pInternedName,
                           Sym.KIND_CHAR_CONST, null, true, true ));
  ((CharConstImpl)lCharConst).fType = pType;
  return lCharConst;
} // charConst

public CharConst
charConst( char pChar, Type pType ) {
  CharConst lCharConst;
  lCharConst = (CharConst)(symRoot.symTableConst.searchOrAdd(
               (String.valueOf(pChar)),
                           Sym.KIND_CHAR_CONST, null, true, true ));
  ((CharConstImpl)lCharConst).fType = pType;
  ((CharConstImpl)lCharConst).fType = pType;
  return lCharConst;
} // charConst

public CharConst
charConst( int pCharCode, Type pType ) {
  CharConst lCharConst;
  lCharConst = (CharConst)(symRoot.symTableConst.searchOrAdd(
               (String.valueOf(pCharCode)).intern(), //##?
                           Sym.KIND_CHAR_CONST, null, true, true ));
  ((CharConstImpl)lCharConst).fType = pType;
  return lCharConst;
} // charConst

public IntConst
intConst( String pInternedName, Type pType ) {
  String lName = sourceLanguage.makeIntConstString(pInternedName, pType);
  IntConst lIntConst;
  lIntConst = (IntConst)(symRoot.symTableConst.searchOrAdd( lName,
                         Sym.KIND_INT_CONST, null, true, true ));
  ((IntConstImpl)lIntConst).fType = pType;
  return lIntConst;
} // intConst

public IntConst
intConst( long pIntValue, Type pType ) {
  IntConst lIntConst;
  String   lIntString = String.valueOf(pIntValue).intern();
  String lName = sourceLanguage.makeIntConstString(lIntString, pType);
  lIntConst = (IntConst)(symRoot.symTableConst.searchOrAdd( lName,
                         Sym.KIND_INT_CONST, null, true, true ));
  ((IntConstImpl)lIntConst).fType = pType;
  return lIntConst;
} // intConst

public FloatConst
floatConst( String pInternedName, Type pType ) {
  FloatConst lFloatConst;
  String lName = sourceLanguage.makeFloatConstString(pInternedName, pType);
  lFloatConst = (FloatConst)(symRoot.symTableConst.
                 searchOrAdd( lName,
                      Sym.KIND_FLOAT_CONST, null, true, true ));
  ((FloatConstImpl)lFloatConst).fType = pType;
  return lFloatConst;
} // floatConst

public FloatConst
floatConst( double pFloatValue, Type pType ) {
  FloatConst lFloatConst;
  String     lFloatString = String.valueOf(pFloatValue).intern();
  String lName = sourceLanguage.makeFloatConstString(lFloatString, pType);
  lFloatConst = (FloatConst)(symRoot.symTableConst.
                 searchOrAdd( lName,
                      Sym.KIND_FLOAT_CONST, null, true, true ));
  ((FloatConstImpl)lFloatConst).fType = pType;
  return lFloatConst;
} // floatConst

public StringConst
stringConst( String pInternedName )
{
  StringConst lStringConst;
  String      lStringBody;
  int         lLeng = pInternedName.length();
  Type        lType, lTypeChar;
  if (lLeng <= 0) {
    lStringBody = "";
  }else {
    lStringBody = symRoot.sourceLanguage.makeStringBody(pInternedName);
  }
  lStringConst = (StringConst)(symRoot.symTableConst.
                   searchOrAdd( ('"' + lStringBody + '"').intern(),
                        Sym.KIND_STRING_CONST, null, true, true ));
  lStringConst.setStringBody(lStringBody);
  lType = symRoot.sym.vectorType(symRoot.getCharType(),
                  symRoot.sourceLanguage.getStringLength(lStringBody));
  lStringConst.setSymType(lType);
  return lStringConst;
} // stringConst

public StringConst
stringConstFromQuotedString( String pInternedName )
{
  StringConst lStringConst;
  String      lString, lStringBody;
  int         lLeng = pInternedName.length();
  Type        lType;
  if ((lLeng > 2)&&
      (pInternedName.charAt(0) == '"')&&
      (pInternedName.charAt(lLeng-1) == '"')) {
    lString = pInternedName.substring(1, lLeng-1);
    lStringBody = symRoot.sourceLanguage.makeStringBody(lString);
  }else {
    if ((lLeng == 2)&&
        (pInternedName.charAt(0) == '"')&&
        (pInternedName.charAt(1) == '"')) {
      lString     = "";
      lStringBody = "";
    }else {
      symRoot.ioRoot.msgRecovered.put(1016,
          "stringConstFromQuotedString param has no quote "
          + pInternedName);
      lString     = pInternedName;
      lStringBody = pInternedName;
    }
  }
  lStringConst = (StringConst)(symRoot.symTableConst.
                   searchOrAdd( ('"' + lStringBody + '"').intern(),
                        Sym.KIND_STRING_CONST, null, true, true ));
  lStringConst.setStringBody(lStringBody);
  lType = symRoot.sym.vectorType(symRoot.getCharType(),
                  symRoot.sourceLanguage.getStringLength(lStringBody));
  lStringConst.setSymType(lType);
  return lStringConst;
} // stringConstFromQuotedString

public StringConst
bareStringConst( String pInternedName )
{
  StringConst lStringConst;
  String      lStringBody;
  int         lLeng = pInternedName.length();
  Type        lType;
  if (lLeng <= 0) {
    lStringBody = "";
  }else {
    lStringBody = pInternedName;
  }
  lStringConst = (StringConst)(symRoot.symTableConst.
                   searchOrAdd( lStringBody.intern(),
                        Sym.KIND_STRING_CONST, null, true, true ));
  lStringConst.setStringBody(lStringBody);
  lType = symRoot.sym.vectorType(symRoot.getCharType(), lLeng);
  lStringConst.setSymType(lType);
  return lStringConst;
} // bareStringConst

public String
makeJavaString( String pStringBody )
{
  StringBuffer lString;
  String lString1 = new String();
  int          i, lCharCode, lOct0, lOct1, lOct2;
  char         lChar;
  if ((pStringBody == null)||(pStringBody.length() <= 0))
    return "\"\"";
  lString = new StringBuffer("\"");
  i = 0;
  while(i < pStringBody.length()) {   //by chen
    lChar = pStringBody.charAt(i);
    switch(lChar) {
    case '\n':lString.append("\\n");   break;
    case '\t':lString.append("\\t");   break;
    case '\'':lString.append("\\'");   break;
    case '\"':lString.append('\\');
              lString.append('\"');
              break;
    case '\f':lString.append("\\f");   break;
    case '\b':lString.append("\\b");   break;
    case '\r':lString.append("\\r");   break;
    case '\\':lString.append('\\');
              lString.append('\\');
              break;       //need REDEFINE
    default:
      if ((int)lChar <= 27) {
        lCharCode = (int)lChar;
        lOct1 = lCharCode / 8;
        lOct2 = lCharCode - lOct1 * 8;
        lString.append('\\');
        lString.append("0");
        lString.append(Character.forDigit(lOct1, 8));
        lString.append(Character.forDigit(lOct2, 8));
      }else
        lString.append(lChar);
      break;
    }
    i++;
  }
  lString=lString.append("\"");
  lString1 = lString.toString();
  return lString1.intern();
}//makeJavaString

public String
makeCstring( String pStringBody )
{
  return symRoot.sourceLanguage.makeCstring(pStringBody);
}//makeCstring

public String
makeCstringWithTrailing0( String pStringBody )
{
  return symRoot.sourceLanguage.makeCstringWithTrailing0(pStringBody);
} // makeCstringWithTrailing0

public NamedConst
namedConst( String pInternedName, Const pConst ) {
  NamedConst lNamedConst;
  lNamedConst = (NamedConst)(symRoot.symTableConst.
                  searchOrAdd( pInternedName,
                      Sym.KIND_NAMED_CONST, null, true, true ));
  ((NamedConstImpl)lNamedConst).fType = pConst.getSymType();
  ((NamedConstImpl)lNamedConst).fConstValue = pConst;
  ((NamedConstImpl)lNamedConst).fIndexValue = pConst.longValue();
  return lNamedConst;
} // namedConst

public NamedConst
namedConst( String pInternedName, int pIndex, Type pType ) {
  NamedConst lNamedConst;
  lNamedConst = (NamedConst)(symRoot.symTableConst.
                   searchOrAdd( pInternedName,
                      Sym.KIND_NAMED_CONST, null, true, true ));
  ((NamedConstImpl)lNamedConst).fType = pType;
  ((NamedConstImpl)lNamedConst).fIndexValue = pIndex;
  ((NamedConstImpl)lNamedConst).fConstValue = symRoot.sym.intConst(
          (long)pIndex, symRoot.typeInt);
  return lNamedConst;
} // namedConst

public java.lang.Integer
intObject( int pIntValue )
{
  return Integer.valueOf( Integer.toString(pIntValue, 10), 10);
}

public Var
defineVar( String pInternedName, Type pType )
{
  Var lVar =defineVar(pInternedName,
                   pType, symRoot.symTableCurrent.getOwner()); //##61
  return lVar; //##61
}

public Var
defineVar( String pInternedName, Type pType, Sym pDefinedIn )
{
  Var lVar;
  lVar = (Var)(symRoot.symTableCurrent.defineUnique( pInternedName,
               Sym.KIND_VAR, pDefinedIn ));
  if (lVar != null) {
    ((VarImpl)lVar).fType = pType;
    if (symRoot.symTableCurrent == symRoot.symTableRoot)         //##61
      lVar.setStorageClass(Var.VAR_STATIC); // Set default class.//##61
  }
  return lVar;
} // defineVar

/** defineRegionVar
 *  Define the region variable of the region type pType in the symTableRoot.
 *  The region variable is recorded in the region type and
 *  the region type is set as the type of the region variable.
 */

public Var
defineRegionVar( String pInternedName, RegionType pType )
{
  Var lVar;
  if (fDbgLevel > 3) //##58
    symRoot.ioRoot.dbgSym.print( 6, "defineRegionVar", pInternedName);
  lVar = (Var)(symRoot.symTableRoot.defineUnique( pInternedName,
               Sym.KIND_VAR, null ));
  if (lVar != null) {
    ((VarImpl)lVar).fType = pType;
  }else {
    lVar = (Var)(symRoot.symTableRoot.searchLocal(pInternedName,
                   Sym.KIND_VAR, true));
  }
  return lVar;
} // defineRegionVar

public Param
defineParam( String pInternedName, Type pType )
{
  Param lParam;
  lParam = (Param)(symRoot.symTableCurrent.defineUnique( pInternedName,
           Sym.KIND_PARAM, symRoot.symTableCurrent.getOwner() ));
  if (lParam != null) {
    ((ParamImpl)lParam).fType = pType;
  }
  return lParam;
} // defineParam

public Elem
defineElem( String pInternedName, Type pType )
{
  Elem lElem;
  lElem = (Elem)(symRoot.symTableCurrent.defineUnique( pInternedName,
               Sym.KIND_ELEM, symRoot.symTableCurrent.getOwner() ));
  if (lElem != null) {
    ((ElemImpl)lElem).fType = pType;
  }
  return lElem;
} // defineElem

public Subp
defineSubp( String pInternedName, Type pType )
{
  Subp lSubp;
  lSubp = (Subp)(symRoot.symTableCurrent.searchOrAdd( pInternedName,
                 Sym.KIND_SUBP,
                 symRoot.symTableCurrent.getOwner(), true, true ));
  lSubp.setReturnValueType(pType);
  return lSubp;
} // subp

public Label
defineLabel( String pInternedName )
{
  Label lLabel;
  lLabel = (Label)(symRoot.symTableCurrentSubp.defineUnique(
    pInternedName, Sym.KIND_LABEL, symRoot.symTableCurrent.getOwner() ));
  if (lLabel != null) {
    ((LabelImpl)lLabel).fType = symRoot.typeVoid;
    ((SubpImpl)symRoot.subpCurrent).addToLabelDefList(lLabel); //##62
  }
  return lLabel;
} // defineLabel

public BaseType
baseType( String pInternedName, int pTypeKind ) {
  SymTableEntry lEntry;
  BaseType      lType;
  int           lSize;
  lEntry = symRoot.symTableRoot.searchOrAddEntry( pInternedName,
                 Sym.KIND_TYPE, null, true, true );
  lType = (BaseType)(lEntry.getEntrySym());
  if (lType == null) {
    lType = (BaseType)(new BaseTypeImpl(symRoot, pInternedName, pTypeKind));
    lEntry.setEntrySym(lType);
    symRoot.symTableRoot.linkSym(lType);
    lSize = machineParam.evaluateSize(pTypeKind);
    /* //##52
    switch (pTypeKind) {
    case Type.KIND_BOOL     : lSize = machineParam.SIZEOF_BOOL;    break; //##51
    case Type.KIND_SHORT    : lSize = machineParam.SIZEOF_SHORT;   break; //##51
    case Type.KIND_INT      : lSize = machineParam.SIZEOF_INT;     break; //##51
    case Type.KIND_LONG     : lSize = machineParam.SIZEOF_LONG;    break; //##51
    case Type.KIND_LONG_LONG: lSize = machineParam.SIZEOF_LONG_LONG; break; //##51
    case Type.KIND_CHAR     : lSize = machineParam.SIZEOF_CHAR;    break; //##51
    case Type.KIND_U_CHAR   : lSize = machineParam.SIZEOF_CHAR;  break; //##51
    case Type.KIND_U_SHORT  : lSize = machineParam.SIZEOF_SHORT; break; //##51
    case Type.KIND_U_INT    : lSize = machineParam.SIZEOF_INT;   break; //##51
    case Type.KIND_U_LONG   : lSize = machineParam.SIZEOF_LONG;  break; //##51
    case Type.KIND_U_LONG_LONG: lSize = machineParam.SIZEOF_LONG_LONG; break; //##51
    case Type.KIND_ADDRESS  : lSize = machineParam.SIZEOF_ADDRESS; break; //##51
    case Type.KIND_OFFSET   : lSize = machineParam.SIZEOF_OFFSET;  break; //##51
    case Type.KIND_FLOAT    : lSize = machineParam.SIZEOF_FLOAT;   break; //##51
    case Type.KIND_DOUBLE   : lSize = machineParam.SIZEOF_DOUBLE;  break; //##51
    case Type.KIND_LONG_DOUBLE: lSize = machineParam.SIZEOF_LONG_DOUBLE; break; //##51
    default:                  lSize = machineParam.SIZEOF_INT;     break; //##51
    }
*/ //##52
    //////////////////////////////////// S.Fukuda 2002.9.3 begin
    //if (pTypeKind != Type.KIND_INT)  // typeInt is not yet created.
    //  lType.setSizeExp((ConstNode)(new ConstNodeImpl(symRoot.getHirRoot(),
    //        (Const)(new IntConstImpl(symRoot,lSize, symRoot.typeInt)))));
    //lType.setSizeValue(lSize);
    if( pTypeKind!=Type.KIND_INT  // typeInt and typeOffset are not yet created.
    &&  pTypeKind!=Type.KIND_OFFSET )
      lType.setSizeValue(lSize);
    //////////////////////////////////// S.Fukuda 2002.9.3 end
  }
  return lType;
} // baseType

/** vectorType with element count given as integer number.
 *  Type name is generated by makeVectorTypeName.
 *  The lower bound is assumed to be 0.
 *  If 0 is given as element count, then the vector type is assumed to be
 *  incomplete type.
 *  @param pElemType Type of the vactor element.
 *  @param pElemCount Number of elements in the vector.
**/
public VectorType
vectorType(Type pElemType, long pElemCount)
{                                          // Fukuda
  VectorType lType;
  VectorType vec = new VectorTypeImpl(symRoot,pElemType,pElemCount);
  SymTable   tbl = symRoot.symTableCurrent.searchTableHaving(pElemType);
  if( tbl==null )
    symRoot.ioRoot.msgError.put(1013,
                                "Element type of VectorType not found");
  lType = (VectorType)tbl.searchOrAddSym(vec);
   return lType;
} // vectorType

/** vectorType with element count given as an expression.
 *  Type name is generated by makeVectorTypeName.
 *  The lower bound is assumed to be 0.
 *  If 0 is given as element count expression, then the vector type is
 *  assumed to be incomplete type.
 *  @param pElemType Type of the vactor element.
 *  @param pElemCountExp Expression representing the number of elements
 *      in the vector. The element count is computed by evaluating this
 *      parameter.
**/
public VectorType
vectorType(Type pElemType, Exp pElemCountExp)
{
  VectorType lType;
  //##64 BEGIN
  long lElemCount;
  if (pElemCountExp != null)
    lElemCount = pElemCountExp.evaluateAsLong();
  else
    lElemCount = 0;
    //##64 END
  VectorType vec = new VectorTypeImpl(symRoot,
      //##64 makeVectorTypeName(pElemType, pElemCountExp.evaluateAsInt()),
      makeVectorTypeName(pElemType, lElemCount), //##64
      pElemType, 0, pElemCountExp, 0, null);
  SymTable   tbl = symRoot.symTableCurrent.searchTableHaving(pElemType);
  if( tbl==null )
    symRoot.ioRoot.msgError.put(1013,
                                "Element type of VectorType not found");
  lType = (VectorType)tbl.searchOrAddSym(vec);
  return lType;
} // vectorType

/** vectorType with element count and lower bound as integer number.
 *  Type name is generated by makeVectorTypeName.
 *  If 0 is given as element count, then the vector type is assumed to be
 *  incomplete type.
 *  @param pTypeName Type name of the vector. It may have const or volatile
 *      qualifier attached to ordinary vector type name.
 *      If pTypeName is null, a vector type name will be generated by
 *      makeVectorTypeName method.
 *  @param pElemType Type of the vactor element.
 *  @param pElemCount Number of elements in the vector.
 *  @param pLowerBound Lower bound of the subscript of the vector.
**/
public VectorType
vectorType( String pTypeName, Type pElemType, long pElemCount, long pLowerBound)
{
  VectorType lType;
  VectorType vec = new VectorTypeImpl(symRoot, pTypeName, pElemType,
      pElemCount, null, pLowerBound, null);
  SymTable   tbl = symRoot.symTableCurrent.searchTableHaving(pElemType);
  if( tbl==null )
    symRoot.ioRoot.msgError.put(1013,
                                "Element type of VectorType not found");
  lType = (VectorType)tbl.searchOrAddSym(vec);
  return lType;
} // vectorType

/** vectorType with element count and lower bound as expression.
 *  Type name is generated by makeVectorTypeName.
 *  If 0 is given as element count, then the vector type is assumed to be
 *  incomplete type.
 *  @param pElemType Type of the vactor element.
 *  @param pElemCountExp Number of elements in the vector.
 *  @param pLowerBoundExp Lower bound of the subscript of the vector.
**/
public VectorType
vectorType( String pTypeName, Type pElemType, Exp pElemCountExp,
            Exp pLowerBoundExp)
{
  VectorType lType;
  VectorType vec = new VectorTypeImpl(symRoot, pTypeName, pElemType,0,
      pElemCountExp, 0, pLowerBoundExp);
  SymTable   tbl = symRoot.symTableCurrent.searchTableHaving(pElemType);
  if( tbl==null )
    symRoot.ioRoot.msgError.put(1013,
                                "Element type of VectorType not found");
  lType = (VectorType)tbl.searchOrAddSym(vec);
  return lType;
} // vectorType

//##64 BEGIN
public VectorType
vectorTypeUnfixed(Type pElemType, long pLowerBound)
{
  VectorType lType;
  VectorType vec = new VectorTypeImpl(symRoot,
      makeVectorTypeName(pElemType, 0), pElemType, 0, null, pLowerBound, null);
  vec.setFlag(Sym.FLAG_UNFIXED_SIZE, true);
  SymTable   tbl = symRoot.symTableCurrent.searchTableHaving(pElemType);
  if( tbl==null )
    symRoot.ioRoot.msgError.put(1013,
                                "Element type of VectorType not found");
  lType = (VectorType)tbl.searchOrAddSym(vec);
  lType.setFlag(Sym.FLAG_UNFIXED_SIZE, true);
  return lType;
} // vectorTypeUnfixed

public VectorType
vectorTypeUnfixed(Type pElemType, Exp pLowerBoundExp)
{
  VectorType lType;
  VectorType vec = new VectorTypeImpl(symRoot,
      makeVectorTypeName(pElemType, 0), pElemType, 0, null, 0, pLowerBoundExp);
  vec.setFlag(Sym.FLAG_UNFIXED_SIZE, true);
  SymTable   tbl = symRoot.symTableCurrent.searchTableHaving(pElemType);
  if( tbl==null )
    symRoot.ioRoot.msgError.put(1013,
                                "Element type of VectorType not found");
  lType = (VectorType)tbl.searchOrAddSym(vec);
  lType.setFlag(Sym.FLAG_UNFIXED_SIZE, true);
  return lType;
} // vectorTypeUnfixed

//##64 END

public PointerType
pointerType( Type pPointedType )
{                                // Fukuda
  return pointerType(pPointedType, symRoot.symTableCurrent);
} // pointerType

public PointerType
pointerType( Type pPointedType, SymTable pSymTable )
{
  SymTableEntry lEntry;
  PointerType   lType;
  String        lName;
  SymTable      lSymTable = pSymTable;
  if (lSymTable == null)
    lSymTable = symRoot.symTableCurrent;
  lSymTable = lSymTable.searchTableHaving(pPointedType);
  lName = "<PTR " + pPointedType.getName() + ">";
  lName = lName.intern();
  lEntry = lSymTable.searchOrAddEntry( lName,
                 Sym.KIND_TYPE, lSymTable.getOwner(), true, true );
  lType = (PointerType)(lEntry.getEntrySym());
  if (lType == null) {
    lType = (PointerType)(new PointerTypeImpl(symRoot, lName,
                                              pPointedType));
    lEntry.setEntrySym(lType);
    lSymTable.linkSym(lType);
  }
  return lType;
} // pointerType

public PointerType
pointerType( String pInternedName, Type pPointedType,
                Sym pDefinedIn )  // Used only within coins.sym package.
{
  SymTableEntry lEntry;
  PointerType   lType;
  String        lName;
  SymTable      lSymTable;
  lSymTable = symRoot.symTableCurrent;
  lSymTable = lSymTable.searchTableHaving(pPointedType);
  if (pInternedName == null) {
    lName = "<PTR " + pPointedType.getName() + ">";
    lName = lName.intern();
  }else
    lName = pInternedName;
  lEntry = lSymTable.searchOrAddEntry( lName,
                 Sym.KIND_TYPE, pDefinedIn, true, true );
  lType = (PointerType)(lEntry.getEntrySym());
  if (lType == null) {
    lType = (PointerType)(new PointerTypeImpl(symRoot, lName,
                                              pPointedType));
    lEntry.setEntrySym(lType);
    lSymTable.linkSym(lType);
  }
  return lType;
} // pointerType

public PointerType
pointerType( Type pPointedType, long pElemCount )
{
  return pointerType(pPointedType, pElemCount, 0L);
} // pointerType

public PointerType
pointerType( Type pPointedType, long pElemCount, long pLowerBound )
{
  SymTableEntry lEntry;
  PointerType   lType;
  String        lName;
  SymTable      lSymTable;
  lSymTable = symRoot.symTableCurrent;
  lSymTable = lSymTable.searchTableHaving(pPointedType);
  // Make name <PTR elemCount lowerBound pointedElemName>
  // to avoid name collision between pointer that does not represent a vector.
  StringBuffer lBuffer = new StringBuffer();
  lBuffer.append("<PTR ");
  lBuffer.append(Long.toString(pElemCount, 10));
  lBuffer.append(" ");
  lBuffer.append(Long.toString(pLowerBound, 10));
  lBuffer.append(" ");
  lBuffer.append(pPointedType.getName());
  lBuffer.append(">");
  lName = lBuffer.toString().intern();
  lEntry = lSymTable.searchOrAddEntry( lName,
                 Sym.KIND_TYPE, lSymTable.getOwner(), true, true );
  lType = (PointerType)(lEntry.getEntrySym());
  if (lType == null) {
    lType = (PointerType)(new PointerTypeImpl(symRoot,
            pPointedType, pElemCount, pLowerBound));
    lEntry.setEntrySym(lType);
    lSymTable.linkSym(lType);
  }
  return lType;
} // pointerType

//## PointerType without name

public StructType
structType( IrList pElemList, Sym pTag )
{
  SymTableEntry lEntry;
  StructType    lType;
  String        lName;
  Sym           lSym, lTag;
  if ((pTag != null)&&(pTag.getSymKind() == Sym.KIND_TAG))
    lTag = pTag;
  else
    lTag =  symRoot.symTableCurrent.generateTag();
  lName = ("<STRUCT " + lTag.getName() + ">").intern();
  lEntry = symRoot.symTableCurrent.searchOrAddEntry( lName,
                 Sym.KIND_TYPE,
                 symRoot.symTableCurrent.getOwner(), true, true );
  lSym = lEntry.getEntrySym();
  if (lSym == null) { // New struct
    lType = (StructType)(new StructTypeImpl(symRoot, lName, pElemList));
    lEntry.setEntrySym(lType);
    symRoot.symTableCurrent.linkSym(lType);
  }else { // Already declared struct.
    if (fDbgLevel > 3) //##58
      symRoot.ioRoot.dbgSym.print( 6, "structType", lSym.getName()
          + " kind " + lSym.getSymKindName());
    lType = (StructType)(lEntry.getEntrySym());
  }
  lType.setDefinedIn(symRoot.symTableCurrent.getOwner());
  lType.setTag(lTag);
  return lType;
} // structType

public StructType  // // used only in sym package.
structType( String pInternedName, IrList pElemList, Sym pTag )
{
  SymTableEntry lEntry;
  StructType    lType;
  String        lName = pInternedName;
  Sym           lSym;
  lEntry = symRoot.symTableCurrent.searchOrAddEntry( lName,
                 Sym.KIND_TYPE,
                 symRoot.symTableCurrent.getOwner(), true, true );
  lSym = lEntry.getEntrySym();
  if (lSym == null) { // New struct
    lType = (StructType)(new StructTypeImpl(symRoot, lName, pElemList));
    lEntry.setEntrySym(lType);
    symRoot.symTableCurrent.linkSym(lType);
  }else { // Already declared struct.
    if (fDbgLevel > 3) //##58
      symRoot.ioRoot.dbgSym.print( 6, "structType", lSym.getName()
          + " kind " + lSym.getSymKindName());
    lType = (StructType)(lEntry.getEntrySym());
  }
  if ((pTag != null)&&(pTag.getSymKind() == Sym.KIND_TAG))
    lType.setTag(pTag);
  return lType;
} // structType

public UnionType
unionType( IrList pElemList, Sym pTag )
{
  SymTableEntry lEntry;
  UnionType     lType;
  String        lName;
  Sym           lSym, lTag;
  if ((pTag != null)&&(pTag.getSymKind() == Sym.KIND_TAG))
    lTag = pTag;
  else
    lTag =  symRoot.symTableCurrent.generateTag();
  lName = ("<UNION " + lTag.getName() + ">").intern();
  lEntry = symRoot.symTableCurrent.searchOrAddEntry( lName,
                 Sym.KIND_TYPE,
                 symRoot.symTableCurrent.getOwner(), true, true );
  lSym = lEntry.getEntrySym();
  if (lSym == null) { // New union
    lType = (UnionType)(new UnionTypeImpl(symRoot, lName, pElemList));
    lEntry.setEntrySym(lType);
    symRoot.symTableCurrent.linkSym(lType);
  }else { // Already declared union.
    if (fDbgLevel > 3) //##58
      symRoot.ioRoot.dbgSym.print( 6, "unionType", lSym.getName()
          + " kind " + lSym.getSymKindName());
    lType = (UnionType)(lEntry.getEntrySym());
  }
  lType.setDefinedIn(symRoot.symTableCurrent.getOwner());
  lType.setTag(lTag);
  return lType;
} // unionType

public UnionType //  used only in sym package
unionType( String pInternedName, IrList pElemList, Sym pTag )
{
  SymTableEntry lEntry;
  UnionType     lType;
  String        lName = pInternedName;
  lEntry = symRoot.symTableCurrent.searchOrAddEntry( lName,
                 Sym.KIND_TYPE,
                 symRoot.symTableCurrent.getOwner(), true, true );
  lType = (UnionType)(lEntry.getEntrySym());
  if (lType == null) { // New union.
    lType = (UnionType)(new UnionTypeImpl(symRoot, lName, pElemList));
    lEntry.setEntrySym(lType);
    symRoot.symTableCurrent.linkSym(lType);
  }
  if ((pTag != null)&&(pTag.getSymKind() == Sym.KIND_TAG))
    lType.setTag(pTag);
  return lType;
} // unionType

public RegionType
regionType( String pRegionNameString, int pStorageClass )
{
  SymTableEntry lEntry;
  RegionType     lType;
  String        lName, lRegionNameString;
  Sym           lSym, lTag;
  if ((pRegionNameString == null)||
      (pRegionNameString.intern() == "")||
      (pRegionNameString.charAt(0) == ' ')) {
    lTag =  symRoot.symTableRoot.generateSym(symRoot.typeRegion, Sym.KIND_TYPE,
       machineParam.blankRegionName(), symRoot.symTableRoot.getOwner()); //##51
    lRegionNameString = lTag.getName();
  }else
    lRegionNameString = pRegionNameString;
  lName = ("<REGION " + lRegionNameString + ">").intern();
  lEntry = symRoot.symTableRoot.searchOrAddEntry( lName,
                 Sym.KIND_TYPE,
                 symRoot.symTableCurrent.getOwner(), true, true );
  lSym = lEntry.getEntrySym();
  if (lSym == null) { // New region
    lType = (RegionType)(new RegionTypeImpl(symRoot, lRegionNameString,
                                            pStorageClass));
    lEntry.setEntrySym(lType);
    symRoot.symTableRoot.linkSym(lType);
  }else { // Already declared region.
    lType = (RegionType)(lEntry.getEntrySym());
  }
  lType.setDefinedIn(symRoot.symTableRoot.getOwner());
  if (fDbgLevel > 3) //##58
    symRoot.ioRoot.dbgSym.print( 6, "regionType", lType.toString());
  return lType;
} // regionType

public RegionType
regionType( String pRegionNameString )
{
  return regionType(pRegionNameString, Var.VAR_STATIC);
}

public EnumType
enumType( IrList pElemList, Sym pTag )
{
  SymTableEntry lEntry;
  EnumType      lType;
  String        lName;
  Sym           lSym, lTag;
  if ((pTag != null)&&(pTag.getSymKind() == Sym.KIND_TAG))
    lTag = pTag;
  else
    lTag =  symRoot.symTableCurrent.generateTag();
  lName = ("<ENUM " + lTag.getName() + ">").intern();
  lEntry = symRoot.symTableCurrent.searchOrAddEntry( lName,
                 Sym.KIND_TYPE,
                 symRoot.symTableCurrent.getOwner(), true, true );
  lSym = lEntry.getEntrySym();
  if (lSym == null) { // New enum
    lType = (EnumType)(new EnumTypeImpl(symRoot, lName, pElemList));
    lEntry.setEntrySym(lType);
    symRoot.symTableCurrent.linkSym(lType);
  }else { // Already declared enum.
    if (fDbgLevel > 3) //##58
      symRoot.ioRoot.dbgSym.print( 6, "enumType", lSym.getName()
          + " kind " + lSym.getSymKindName());
    lType = (EnumType)(lEntry.getEntrySym());
  }
  lType.setDefinedIn(symRoot.symTableCurrent.getOwner());
  lType.setTag(lTag);
  return lType;
} // enumType

public EnumType //  used only in sym package.
enumType( String pInternedName, IrList pEnumList, Sym pTag )
{
  SymTableEntry lEntry;
  EnumType      lType;
  String        lName = pInternedName;
  Sym           lSym;
  lEntry = symRoot.symTableCurrent.searchOrAddEntry( lName,
                 Sym.KIND_TYPE,
                 symRoot.symTableCurrent.getOwner(), true, true );
  lSym = lEntry.getEntrySym();
  if (lSym == null) { // New enum
    lType = (EnumType)(new EnumTypeImpl(symRoot, lName, pEnumList));
    lEntry.setEntrySym(lType);
    symRoot.symTableCurrent.linkSym(lType);
  }else { // Already declared struct.
    if (fDbgLevel > 3) //##58
      symRoot.ioRoot.dbgSym.print( 6, "enumType", lSym.getName()
          + " kind " + lSym.getSymKindName());
    lType = (EnumType)(lEntry.getEntrySym());
  }
  lType.setDefinedIn(symRoot.symTableCurrent.getOwner());
  if ((pTag != null)&&(pTag.getSymKind() == Sym.KIND_TAG))
    lType.setTag(pTag);
  return lType;
} // enumType

public DefinedType
definedType( String pInternedName, Type pOriginalType )
{
  SymTableEntry lEntry;
  DefinedType   lType;
  Sym           lSym;
  String        lName;
  if (fDbgLevel > 3) //##58
   symRoot.ioRoot.dbgSym.print(4, "definedType", pInternedName +
      " " + pOriginalType.getName() );
  lName = ("<TYPEDEF " + pInternedName + ">").intern();
  lEntry = symRoot.symTableCurrent.searchOrAddEntry( lName,
                 Sym.KIND_TYPE,
                 symRoot.symTableCurrent.getOwner(), true, true );
  lSym = lEntry.getEntrySym();
  if (lSym == null) {
    lType = new DefinedTypeImpl(symRoot, lName, pOriginalType);
    lEntry.setEntrySym(lType);
    symRoot.symTableCurrent.linkSym(lType);
  }else
    lType = (DefinedType)lSym;
  return lType;
} // definedType

public DefinedType
definedType( String pInternedName, Type pOriginalType,
                Sym pDefinedIn ) {
  SymTableEntry lEntry;
  DefinedType   lType;
  Sym           lSym;
  String        lName;
  if (fDbgLevel > 3) //##58
    symRoot.ioRoot.dbgSym.print(4, "definedType", pInternedName +
      " " + pOriginalType.getName() );
  lName = ("<TYPEDEF " + pInternedName + ">").intern();
  lEntry = symRoot.symTableCurrent.searchOrAddEntry( lName,
                 Sym.KIND_TYPE, pDefinedIn, true, true );
  lSym = lEntry.getEntrySym();
  if (lSym == null) {
    lType = new DefinedTypeImpl(symRoot, lName, pOriginalType);
    lEntry.setEntrySym(lType);
    symRoot.symTableCurrent.linkSym(lType);
  }else
    lType = (DefinedType)lSym;
  return lType;
} // definedType

public SubpType
subpType( Type pReturnType, IrList pParamList,
          boolean pOptionalParam, Sym pDefinedIn )
{
  return subpType(pReturnType, pParamList, pOptionalParam,
                  false, pDefinedIn);           //##53
} // subpType

public SubpType
subpType( Type pReturnType, IrList pParamList,
          boolean pOptionalParam, boolean pNoParamSpec, //##53
          Sym pDefinedIn )
{
  SymTableEntry lEntry;
  SubpType lType;
  String lSubpTypeName = symRoot.sym.makeSubpTypeName( pReturnType,
          pParamList, pOptionalParam, pNoParamSpec).intern(); //##53
  lEntry = symRoot.symTableCurrent.searchOrAddEntry( lSubpTypeName.intern(),
                 Sym.KIND_TYPE, pDefinedIn, true, true );
  lType = (SubpType)(lEntry.getEntrySym());
  if (lType == null) {
    lType = (SubpType)(new SubpTypeImpl(symRoot, lSubpTypeName, pReturnType,
                                        pParamList, pOptionalParam,
                                        pNoParamSpec)); //##53
    lEntry.setEntrySym(lType);
    symRoot.symTableCurrent.linkSym(lType);
  }
  if (fDbgLevel > 3) //##58
    symRoot.ioRoot.dbgSym.print(4, "subpType", lType.getName());
  return lType;
} // subpType

public Sym
symbol( String pInternedName, Type pType, Sym pDefinedIn ) {
  Sym lSym;
  lSym = (Sym)(symRoot.symTableCurrent.searchOrAdd( pInternedName,
                       Sym.KIND_OTHER, pDefinedIn, true, false ));
  ((SymImpl)lSym).fType = pType;
  return lSym;
} // symbol

public Sym
derivedSym()
{
  if (symRoot.symTableCurrentSubp != null)
    return symRoot.symTableCurrentSubp.generateDerivedSym(this);
  else
    return symRoot.symTableCurrent.generateDerivedSym(this);
} // derivedSym

//====== Methods used in creating symbol objects ======//

public String
makeVectorTypeName( Type pElemType, long pElemCount)
{
  StringBuffer lBuffer = new StringBuffer();
  lBuffer.append("<VECT ");
  lBuffer.append(Long.toString(pElemCount, 10));
  lBuffer.append(" 0 ");
  lBuffer.append(pElemType.getName());
  lBuffer.append(">");
  return lBuffer.toString().intern();
} // makeVectorTypeName

public String
makeVectorTypeName( Type pElemType, long pElemCount, long pLowerBound)
{
  StringBuffer lBuffer = new StringBuffer();
  lBuffer.append("<VECT ");
  lBuffer.append(Long.toString(pElemCount, 10));
  lBuffer.append(" ");
  lBuffer.append(Long.toString(pLowerBound, 10));
  lBuffer.append(" ");
  lBuffer.append(pElemType.getName());
  lBuffer.append(">");
  return lBuffer.toString().intern();
} // makeVectorTypeName

public String
makeVectorTypeName( Type pElemType, Exp pElemCountExp, long pElemCount,
                        Exp pLowerBoundExp, long pLowerBound)
{
  StringBuffer lBuffer = new StringBuffer();
  lBuffer.append("<VECT ");
  if (pElemCountExp != null) {
    lBuffer.append(makeExpString(pElemCountExp));
  }else
    lBuffer.append(Long.toString(pElemCount, 10));
  lBuffer.append(" ");
  if (pLowerBoundExp != null) {
    lBuffer.append(makeExpString(pLowerBoundExp));
  }else
    lBuffer.append(Long.toString(pLowerBound, 10));
  lBuffer.append(" ");
  lBuffer.append(pElemType.getName());
  lBuffer.append(">");
  return lBuffer.toString().intern();
} // makeVectorTypeName with Exp parameters

public String
makeExpString( Exp pExp )
{
  StringBuffer lBuffer = new StringBuffer();
  if (pExp instanceof VarNode) {
    Sym lVar = ((VarNode)pExp).getSymNodeSym();
    lBuffer.append(lVar.getName());
    // lBuffer.append( "_" + System.identityHashCode(lVar));
  }else if (pExp instanceof ConstNode) {
    lBuffer.append(((ConstNode)pExp).getIntValue());
  }else {
    if ((pExp.getOperator() == HIR.OP_CONTENTS) &&
        (pExp.getChild1() instanceof VarNode))
      lBuffer.append(makeExpString((Exp)pExp.getChild1()));
    else
      lBuffer.append(pExp.toStringWithChildren());
  }
// System.out.println(" makeExpString " + pExp.toString() + "=" + lBuffer.toString()); //###
  return lBuffer.toString().intern();
} // makeExpString

public String
makeStructUnionTypeName( boolean pStruct, IrList pElemList ) {
  StringBuffer lBuffer = new StringBuffer();
  if (pStruct)
    lBuffer.append("<STRUCT ");
  else
    lBuffer.append("<UNION ");
  if (pElemList != null)
    lBuffer.append(TypeImpl.getElemListString(pElemList));
  lBuffer.append(">");
  if (fDbgLevel > 3) //##58
    symRoot.ioRoot.dbgSym.print( 6, "makeStructUnionTypeName",
          lBuffer.toString());
  return lBuffer.toString().intern();
} // makeStructUnionTypeName

public String
makeEnumTypeName( IrList pElemList ) {
  StringBuffer lBuffer = new StringBuffer();
  lBuffer.append("<ENUM ");
  if (pElemList != null)
    lBuffer.append(TypeImpl.getEnumListString(pElemList));
  lBuffer.append(">");
  if (fDbgLevel > 3) //##58
    symRoot.ioRoot.dbgSym.print( 6, "makeEnumTypeName",
          lBuffer.toString());
  return lBuffer.toString().intern();
} // makeEnumTypeName

public String
makeSubpTypeName( Type pReturnType,
                  IrList pParamList, boolean pOptionalParam)
{
  return makeSubpTypeName(pReturnType, pParamList, pOptionalParam,
                          false);  //##53
} // makeSubpTypeName

public String  //##53
makeSubpTypeName( Type pReturnType,
                  IrList pParamList, boolean pOptionalParam,
                  boolean pNoParamSpec)
{
  Sym lParamListSym;
  if (fDbgLevel > 3) //##58
    symRoot.ioRoot.dbgSym.print( 6, "makeSubpTypeName",
          "ReturnType " + pReturnType.getName() + " " +
          symRoot.ioRoot.toStringObject(pParamList));
  StringBuffer lBuffer = new StringBuffer();
  IrList lParamTypeList = new IrListImpl(symRoot.getHirRoot()); //SF021208
  if (pParamList != null) {
    for (java.util.Iterator lParamIterator = pParamList.iterator();
         lParamIterator.hasNext(); ) {
      lParamListSym = (Sym) (lParamIterator.next());
      if (lParamListSym.getSymKind() == Sym.KIND_TYPE)
        lParamTypeList.add( (Type) lParamListSym);
      else if (lParamListSym.getSymKind() == Sym.KIND_PARAM)
        lParamTypeList.add( ( (Param) lParamListSym).getSymType());
    }
  }
  lBuffer.append("<SUBP <").append(lParamTypeList.toString());
  lBuffer.append("> ").append(String.valueOf(pOptionalParam))
         .append(" ").append(String.valueOf(pNoParamSpec))             //##53
         .append(" ").append(pReturnType.getName());
  lBuffer.append(">");
  if (fDbgLevel > 3) //##58
    symRoot.ioRoot.dbgSym.print( 6, lBuffer.toString());
  return lBuffer.toString().intern();
} // makeSubpTypeName

//====== Methods to access information of symbol objects ======//

/** getSymKindName
 *  Get the symbol kind name from GlobalConstants.
 *  See GlobalConstants
 *  @return String Symbol kind name.
*/
public String
getSymKindName() {
    return Sym.KIND_NAME[fKind];
}

/** linkSym
 *  Link pNextSym as the next to this symbol.
 *  @param pNextSym Symbol to be inserted as the one next to this symbol.
**/
public void
linkSym( Sym pNextSym) {
  ((SymImpl)pNextSym).fNextSym = fNextSym;
  fNextSym = pNextSym;
} // linkSym

/** getName
 * Get the name of this symbol.( The same name as in the source file. )
 * @return String symbol name
**/
public String
getName() {
  if (fName != null) {
    if (getFlag(Sym.FLAG_RESERVED_NAME))
      return fName.substring(1);
    else
      return fName;
  }else
    return "";
} // getName

public String
getPureName()
{
  if (fName != null) {
      return fName;
  }else
    return "";
} // getName

public String
getNameOrNull( Sym pSym )
{
  if (pSym == null)
    return "null";
  else
    return pSym.getName();
}

/** getNextSym
 *  Get the next symbol
 *  @return Sym the next symbol
*/
public Sym
getNextSym( ) {
  Sym lNextSym = fNextSym;
  while ((lNextSym != null)&&
         lNextSym.isRemoved())
    lNextSym = ((SymImpl)lNextSym).fNextSym;
  return lNextSym;
} // getNextSym

/** getUniqueName
 * Get the UniqueName of this symbol.
 * @return String symbol unique name.
**/
public String
getUniqueName()
{
  //##70 if (fUniqueName != null)
  //##70   return fUniqueName;
  if (fUniqueNameSym != null) { //##70
    return fUniqueNameSym.getName(); //##70
  }else
    return getName();
}

/** setUniqueNameSym
 * Set the UniqueName of this symbol.
**/
public void
setUniqueNameSym( Sym pUniqueNameSym ) //##70
{
  fUniqueNameSym = pUniqueNameSym; //##70
  ((SymImpl)pUniqueNameSym).setOriginalSym(this);
}
  /* //##70
  public void
  setUniqueName( String pUniqueName ) {
      fUniqueName = pUniqueName;
  }
*/ //##70

//##70 BEGIN
public Sym
getOriginalSym()
{
  if (fSymInf == null)
    return this;
  if (fSymInf.getOriginalSym() == null)
    return this;
  else
    return fSymInf.getOriginalSym();
}

public void
setOriginalSym( Sym pOriginalSym )
{
  if (fSymInf == null) {
    fSymInf = new SymInf();
  }
  fSymInf.setOriginalSym(pOriginalSym);
}
//##70 END

//##74 BEGIN
public Sym
  getOriginalSym( String pName )
{
  Sym lUniqueSym = symRoot.symTableUnique.search(pName);
  if (lUniqueSym != null) {
    //##82 return getOriginalSym();
    return lUniqueSym.getOriginalSym(); //##82
  }else {
    // Not in symTableUnique.
    Sym lSym = symRoot.symTableCurrent.search(pName);
    if (lSym != null)
      return lSym.getOriginalSym();
    else
      return null;

  }
} // getOriginalSym
//##74 END

    /** getDefinedIn
     *  Get the name of the construct containing the definition of this symbol
     *  (see defineUnique, Define, etc.).
     *  "this" may be any symbol.
     *  @return the name of the construct containing the definition of this
     *      symbol, e.g.
     *      subprogram name for its local variables and formal parameters,
     *      structure name or union name for their elements (anonymous
     *      structure or union should have name generated by compiler),
     *      class name for its fields, etc.
     *      Return null for symbols that has no surrounding constructs,
     *      e.g. global variables in C, predefined symbols inherent in
     *      the source language, etc.
     */
public Sym
getDefinedIn() {
    return fDefinedIn;
}

    /** setDefinedIn
     *  Set "definedIn" symbol of this symbol if it is not set by defineUnique,
     *  Define, and redefine.
     *  "this" may be any symbol.
     *  @param pDefiningSym name of the construct that include the definition
     *      of this symbol.
     *  Anonymous structure or union shuold be named by GenerateVar so as
     *  pDefiningSym (and pDefinedIn) can be specified.
     */
public void
setDefinedIn( Sym pDefiningSym ) {
    fDefinedIn = pDefiningSym;
}

public String
getDefinedInName() {
  if (fDefinedIn != null)
    return fDefinedIn.getName();
  else
    return "";
}

/** getRecordedIn
 *  Get the symbol table that recorded this symbol.
 */
public SymTable
getRecordedIn( ) {
  return fRecordedIn;
}

/** setRecordedIn
 *  Link to the symbol table that recorded this symbol.
 *  See linkSym of SymTableImpl.
 *  @param pSymTable Symbol table that recorded this symbol.
 */
public void
setRecordedIn( SymTable pSymTable ) {
    fRecordedIn = pSymTable;
}

/** getSymKind
 *  Get the symbol kind of this symbol (KIND_VAR, KIND_SUBP, etc.).
 *  @return the symbol kind that is set when this symbol is created.
**/
public int
getSymKind() {
  return fKind;
}

/** setSymKind
 *  Set the symbol kind of this symbol (KIND_VAR, KIND_SUBP, etc.).
 *  "this" may be any symbol.
**/
public void
setSymKind( int pSymKind ) {
  if ((fKind == Sym.KIND_OTHER)||
      (pSymKind == Sym.KIND_REMOVED))
    fKind = pSymKind;
}

/** getSymType
 *  Get the type of this symbol.
 *  "this" is any symbol.
 *  @return type symbol representing the type of this symbol.
 *      If this is a symbol that has no type (label, key words, etc.),
 *      return typeVoid.
 */
public Type
getSymType() {
    return fType;
}

/**  setSymType
 *  Set the type of this symbol.
 *  "this" is any symbol.
 *  @param psymType type symbol representing the type of this symbol.
 *   set the type of this symbol as pSymType.
**/
public void
setSymType( Type pSymType ) {
  fType = pSymType;
}

/** getFlag
 *  getFlag returns the value (true/false) of the flag indicated
 *  by pFlagNumber.
 *  "this" may be any symbol that may have flags.
 *  @param pFlagNumber flag identification number.
 *  @return boolean which indicates pFlagNumber's flag.
**/
public boolean
getFlag( int pFlagNumber ) {
  return fFlagBox.getFlag(pFlagNumber);
}

/** setFlag
 *  setFlag sets the flag of specified number.
 *  "this" may be any symbol that may have flags.
 *  @param pFlagNumber flag identification number.
 *  @param pYesNo true or false to be set to the flag.
**/
public void
setFlag( int pFlagNumber, boolean pYesNo ) {
  fFlagBox.setFlag(pFlagNumber, pYesNo);
}

public boolean
isGlobal() {
  if (getDefinedIn() == null)   //## REFINE
    return true;
  else
    return false;
} // isGlobal

public void
remove()
{
  if (fDbgLevel > 2) //##58
    symRoot.ioRoot.dbgSym.print(3, "remove", getName());
  fKind = Sym.KIND_REMOVED;
} // remove

public boolean
isRemoved()
{
  if (getSymKind() == Sym.KIND_REMOVED)
    return true;
  else
    return false;
}

/** getInf
 *  Get additional information (for optimization, parallelization, etc.)
 *  of this symbol.
 *  return@ information attached to this symbol,
 *          return null if no information is attached.
 */
public SymInf
getInf() {
    return fSymInf;
}

public SymInf
getOrAddInf() {
  if (fSymInf == null)
    fSymInf = new SymInf();
  return fSymInf;
}

public SourceInf getSourceInf() { return fSourceInf; }

public void setSourceInf( SourceInf pInf ) {
  fSourceInf = pInf;
}
    /** getDefinedFile
     *  Get the symbol table entry representing the name of the file
     *  that defined this symbol for the first time.
     *  @return Sym the name entry of the file that defined this symbol.
     *      If this is not declared in any source file, then return null.
     */
public String
getDefinedFile() {
  if (fSourceInf != null)
    return fSourceInf.getDefinedFile();
  else
    return null;
}

public void
setDefinedFile( String pDefinedFile )
{
  if (fSourceInf == null)
    fSourceInf = (SourceInf)(new SourceInfImpl());
  fSourceInf.setDefinedFile(pDefinedFile);
}

    /** getDefinedLine
     *  Get the line number of the first declaration for this symbol.
     *  The line number is relative within a file defining this symbol.
     *  @return int the name entry of the file that defined this symbol.
     *      If this is not declared in any source file, then return 0.
     */
public int
getDefinedLine() {
  if (fSourceInf != null)
    return fSourceInf.getDefinedLine();
  else
    return 0;
}

public void
setDefinedLine( int pDefinedLine )
{
  if (fSourceInf == null)
    fSourceInf = (SourceInf)(new SourceInfImpl());
  fSourceInf.setDefinedLine(pDefinedLine);
}

    /** getDefinedColumn
     *  Get the column number of the first declaration for this symbol.
     *  The column number indicates the column position of this symbol
     *  in the line declaring this symbol.<BR>
     * @return   int
     *  the name entry of the file that defined this symbol.<BR>
     *  If this is not declared in any source file, then return 0.<BR>
     */
public int getDefinedColumn() {
  if (fSourceInf != null)
    return fSourceInf.getDefinedColumn();
  else
    return 0;
}

public Object
getWork() { return fWork; }

public void
setWork( Object pWork ) { fWork = pWork; }

//==== Methods of HasStringObject ====//

public boolean
isSym() { return true; }

public boolean
isHIR() { return false; }

public String
toString() {
  String symString = fName;
  if (this instanceof FlowAnalSym)
    symString = symString + " " + ((FlowAnalSym)this).getIndex();
//  if (( fType != null )&&(fKind != Sym.KIND_TYPE)&&
//      (symRoot.dbgSym.getLevel() > 3))
//    symString = symString + " " + fType.getName();
  return symString;
} // toString

public String
toStringShort() {
  return fName;
} // toStringShort

public String
toStringDetail() {
  String lString = getSymKindName() + " " + toString();
  if (( fType != null )&&(fKind != Sym.KIND_TYPE))
    lString = lString + " " + fType.getName();
  //##70 if (fUniqueName != null)
  //##70   lString = lString + " unq:" + fUniqueName;
  if (fUniqueNameSym != null) //##70
    lString = lString + " unq:" + fUniqueNameSym.getName(); //##70
  String lDefinedIn = getDefinedInName();
  if (lDefinedIn != "")
    lString = lString + " in " + lDefinedIn;
  if ((fSourceInf != null)&&
      (fSourceInf.getDefinedFile() != null))
    lString = lString + " " + fSourceInf.toString();
  if (! fFlagBox.allFalse())
    lString = lString + fFlagBox.toString();
  return lString;
} // toStringDetail

public void
print(int pIndent) {
  String lString, lSpace;
  // lSpace = ir.getIndentSpace(pIndent);
  lString = getSymKindName() + " " + toString();
  symRoot.ioRoot.printOut.print(" " + lString);
} // print

public void
print(int pIndent, boolean pDetail) {
  String lString, lSpace;
  // lSpace = ir.getIndentSpace(pIndent);
  if (pDetail)
    lString = getSymKindName() + " " + toStringDetail();
  else
    lString = getSymKindName() + " " + toString();
  symRoot.ioRoot.printOut.print(" " + lString);
} // print

} // SymImpl class
