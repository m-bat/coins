/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;

import coins.SymRoot;
import coins.MachineParam; //##51
import coins.ir.IrList;
import coins.ir.hir.Exp;
import coins.ir.hir.ConstNode;

/**<PRE> TypeImpl Class
**/
public class
TypeImpl extends SymImpl implements Type
{

//====== Field declarations ======//

  /** The indication of this Type kind */
  protected int
    fTypeKind = 0;

  /** Type from which this type derived. */
  protected Type
    fOrigin = null;

  private Exp fSizeExp   = null; // Expression representing the size of this type.
  private long fSizeValue = -1;  // Value of type size.
  //
  // There are 3 states in type size representation:
  //
  // State 1: fSizeExp==null  fSizeValue<0
  //   No information is given about Size value.
  // State 2: fSizeExp!=null  fSizeValue<0
  //   Size is given as expression. Its size will be given at execution time.
  // State 3: fSizeExp!=null  fSizeValue>=0
  //   Size value is given as constant expression or constant value.
  //   The size may be 0 in some case.
  //
  // In C language, we can deside if a type is incomplete or not
  //   by seeing whether getSizeExp()==null or not because types of C are
  //   statically defined.
  // fSizeExp/fSizeValue are set by the methods setSizeExp/setSizeValue.
  //   The method setSizeValue will set not only fSizeValue but also
  //   set fSizeExp by making the expression representing the value.
  //   The method setSizeExp will set not only fSizeExp but also
  //   set fSizeValue by converting the expression into a value
  //   if the expression is a constant expression.
  // If setSizeExp(null) or setSizeValue(-1) are given,
  //   the type state changes to state 1.
  //   If setSizeExp(non-constant expression) is given,
  //   the type state changes to state 2.
  //   If setSizeExp(constant expression) or setSizeValue(0 or plus value)
  //   is  are given,  the type state changes to state 3.
  //
  //////////////////// S.Fukuda 2002.10.30 end

  /** This type is const or not */
  protected boolean
    fConst = false;

  /** This type is volatile or not */
  protected boolean
    fVolatile = false;

  /** This type is restrict or not */ //##81
  protected boolean    //##81
    fRestrict = false; //##81

  /** fCompleteType
   *  Complete type corresponding to this type.
  **/
  protected Type
    fCompleteType;

  /** fTypeCore is core part of this type, that is,
   *  excluding type qualifier and vector element count.
   *  It should be a name with intern().
  **/
  protected String
    fTypeCore;

//====== Constructors ======//

public
TypeImpl() { }

public
TypeImpl( SymRoot pSymRoot )
{
  super(pSymRoot);
  fKind = Sym.KIND_TYPE;
  fType = this;
  fCompleteType = this;  // Set null for incomplete type at first.
}

//====== Methods to get/set attributes ======//

public int
getTypeKind() {
  return fTypeKind;
}

public Type
getOrigin()
{
  return fOrigin;
}

public Type
getFinalOrigin()
{
  if (fOrigin == this)
    return fOrigin;
  else if (fOrigin != null)
    return fOrigin.getFinalOrigin();
  else return this;
}

public Type
getUnqualifiedType() //SF041206
{
  if( fConst || fVolatile
     || fRestrict )  //##81
    if( fOrigin!=null && fOrigin!=this )
      return  fOrigin.getUnqualifiedType();
  return this;
}

public void
setOrigin( Type pOrigin )
{
  fOrigin = pOrigin;
}

public boolean
isBasicType() {
  if (fTypeKind <= Type.KIND_BASE_LIM)
    return true;
  else
    return false;
}

public boolean
isInteger() {
  if ((fTypeKind >= Type.KIND_SHORT)&&
      (fTypeKind <= Type.KIND_INT_UPPER_LIM))
    return true;
  else
    return false;
} // isInteger

public boolean
isUnsigned() {
  if ((fTypeKind >= Type.KIND_UNSIGNED_LOWER_LIM)&&
      (fTypeKind <= Type.KIND_INT_UPPER_LIM))
    return true;
  else
    return false;
} // isInteger

public boolean
isFloating() {
  if ((fTypeKind >= Type.KIND_FLOAT_LOWER_LIM)&&
      (fTypeKind <= Type.KIND_FLOAT_UPPER_LIM))
    return true;
  else
    return false;
} // isInteger

public boolean
isScalar()      //##53
{
  if ((fTypeKind <= Type.KIND_FLOAT_UPPER_LIM)||
      (fTypeKind == Type.KIND_POINTER))
    return true;
  Type lOrigin = getFinalOrigin();
  if (lOrigin != null) {
    int lTypeKind = lOrigin.getTypeKind();
    if ((lTypeKind <= Type.KIND_FLOAT_UPPER_LIM)||
        (lTypeKind == Type.KIND_POINTER))
      return true;
  }
  return false;
} // isScalar

/** getSizeExp
 *  Get size of type.
 *  "this" should be a type symbol.
 *  return@  get the size of this type.
**/
public Exp getSizeExp()
{
  return fSizeExp;
}
public void setSizeExp(Exp pSizeExp)
{
  fSizeExp = pSizeExp;
  if( fSizeExp!=null )
  {
    //SF050111[
    //try
    //{
    //  Object o = fSizeExp.evaluateAsObject(); // Evaluate the expression.
    //  if( o instanceof Long )
    //  {
    //    fSizeValue = ((Long)o).longValue(); // Set the size value. //##22
    //    return;
    //  }
    //  symRoot.ioRoot.dbgSym.print(3,"evaluate size exp: ","value is not integer");
    //}
    //catch(NumberFormatException e) // Expression is not constant and can not be evaluated.
    //{
    //  symRoot.ioRoot.dbgSym.print(3,"evaluate size exp: ",e.getMessage());
    //}
    Const c = fSizeExp.evaluate();
    if( c!=null && c.getSymKind()==Sym.KIND_INT_CONST )
      fSizeValue = c.longValue();
    else
      symRoot.ioRoot.dbgSym.print(3,"evaluate size exp: ","not evaluable");
    //SF050111]
  }
  // If pSizeExp is null, fSizeExp is set null to represent reset.

  //  fSizeValue = -1; // Reset the size value.
}
public long getSizeValue()
{
  return fSizeValue;
}
public void setSizeValue(long pSizeValue)
{
  if( pSizeValue>=0 ) // Type size is statically given.
  {
    fSizeExp   = symRoot.getHirRoot().hir.offsetConstNode(pSizeValue);
    fSizeValue = pSizeValue;
  }
  else // Type size is not statically given. It will be desided at execution time.
  {
      // Size may be given as an expression.
      // If size expression is not given, then fSizeExp is null
      // by initial value setting.
    fSizeValue = -1;
  }
}
public boolean isSizeEvaluable() // ture if the type size is set statically.
{
  return fSizeValue>=0;
}
//////////////////// S.Fukuda 2002.10.30 end

public Type
makeConstType()
{
  Type lType;
  if (isConst())
    return this;
  else
    lType = makeQualifiedType("const");
  if (lType != this)
    ((TypeImpl)lType).fConst = true;
  return lType;
} // makeConstType

public Type
makeVolatileType()
{
  Type lType;
  if (isVolatile())
    return this;
  else
    lType = makeQualifiedType("volatile");
  if (lType != this)
    ((TypeImpl)lType).fVolatile = true;
  return lType;
} // makeVolatileType

//##81 BEGIN
public Type
makeRestrictType()
{
  Type lType;
  if (isRestrict())
    return this;
  else
    lType = makeQualifiedType("restrict");
  if (lType != this)
    ((TypeImpl)lType).fRestrict = true;
  return lType;
} // makeRestrictType
//##81 END

/* makeQualifiedType
 * Make a new type from this type by qualifying const or volatile.
 * structType, pointerType, etc. are invoked according to
 * the type kind.
 * If this is not a suitsble type for qualification,
 * then return this.
**/
private Type
makeQualifiedType( String pQualifier )
{
  String   lName, lTypeCore;
  Type     lType;
  Sym      lSym, lDefinedIn;
  SymTable lSymTable;
  int      lTypeKind;
  lTypeKind = getTypeKind();
  lTypeCore = fTypeCore;
  lName     = lTypeCore;
  if (pQualifier == "const") {
    if (isConst())
      return this;
    else if (isVolatile()) {
      lName = (lTypeCore + " const volatile").intern();
    }else if (isRestrict()) { //##81
      lName = (lTypeCore + " const restrict").intern(); //##81
    }else
      lName = (getName() + " " + pQualifier).intern();
  }else if (pQualifier == "volatile") {
    if (isVolatile())
      return this;
    else if (isConst()) {
      lName = (lTypeCore + " const volatile").intern();
    }else if (isRestrict()) { //##81
      lName = (lTypeCore + " volatile restrict").intern(); //##81
    }else
      lName = (getName() + " " + pQualifier).intern();
  //##81 BEGIN
  }else if (pQualifier == "restrict") {
    if (isRestrict())
      return this;
    else if (isConst()) {
      lName = (lTypeCore + " const restrict").intern();
    }else if (isVolatile()) {
      lName = (lTypeCore + " volatile restrict").intern();
    }else
      lName = (getName() + " " + pQualifier).intern();
  //##81 END
  }
  lSymTable = symRoot.symTableCurrent.searchTableHaving(this);
  lSym = lSymTable.search(lName);
  if ((lSym != null)&&(lSym.getSymKind() == Sym.KIND_TYPE))
    return (Type)lSym;  // Already exists.
  if (lTypeKind <= Type.KIND_BASE_LIM)
    lType = symRoot.sym.baseType(lName, lTypeKind);
  else {
    lDefinedIn = symRoot.symTableCurrent.getOwner();
    switch (lTypeKind) {
    case Type.KIND_ENUM:
      lType = ((SymImpl)symRoot.sym).enumType(lName,
          ((EnumType)this).getElemList(), null);
      break;
    case Type.KIND_POINTER:
      lType = ((SymImpl)symRoot.sym).pointerType(lName,
              ((PointerType)this).getPointedType(), lDefinedIn);
      break;
    case Type.KIND_VECTOR:
      lType = symRoot.sym.vectorType(lName,
              ((VectorType)this).getElemType(),
              ((VectorType)this).getElemCount(),
              ((VectorType)this).getLowerBound());
      break;
    case Type.KIND_STRUCT:
      lType = ((SymImpl)symRoot.sym).structType(lName,
                ((StructType)this).getElemList(), null);
      break;
    case Type.KIND_UNION:
      lType = ((SymImpl)symRoot.sym).unionType(lName,
                ((UnionType)this).getElemList(), null);
      break;
    case Type.KIND_REGION:
      lType = symRoot.sym.regionType(lName, Var.VAR_STATIC);
      break;
    case Type.KIND_DEFINED:
      lType = symRoot.sym.definedType(lName, this);
      break;
    default:
      symRoot.ioRoot.msgRecovered.put(1011,
        "illegal qualification " + getName() + " " + pQualifier);
      return this;
    }
  }
  lType.setOrigin(this);
  ((TypeImpl)lType).fTypeCore = fTypeCore;
     // fTypeCore does not contain qualifier.
  //##85 BEGIN
  if (pQualifier == "const")
    ((TypeImpl)lType).fConst = true;
  else if (pQualifier == "volatile")
    ((TypeImpl)lType).fVolatile = true;
  else if (pQualifier == "restrict")
    ((TypeImpl)lType).fRestrict = true;
  //##85 END
  return lType;
} // makeQualifiedType

/** isConst
 *  See the qualification of "this" type.
 *  "this" should be a type symbol.
 *  @return boolean true if "this" type is qualified as const,
 *              false otherwise.
**/
public boolean
isConst() {
  return fConst;
}

/** isVolatile
 *  See the qualification of "this" type.
 *  "this" should be a type symbol.
 *  @return true if "this" type is qualified as volatile,
 *              false otherwise.
**/
public boolean
isVolatile() {
  return fVolatile;
}

//##81 BEGIN
/** isRestrict
 *  See the qualification of "this" type.
 *  "this" should be a type symbol.
 *  @return true if "this" type is qualified as restrict,
 *              false otherwise.
**/
public boolean
isRestrict() {
  return fRestrict;
}
//##81 END

public int
getTypeRank() {
  return Type.KIND_RANKS[fTypeKind];
}

    /** getDimension
     *  Get the dimension of this type.
     *  "this" is an array type.
     *  @return the dimension of this type if this is an array type.
     *      1: 1-dimensinal array, 2: 2-dimensional array, ,,, .
     *      If this is not an array type, return 0.
     **/
public int
getDimension( ) {
  return 0;
}

public IrList
getElemList() {
  return null;
}

public Type
getPointedType() { return null; }

public String
getElemListString() {
  Elem         lElem;
  IrList       lElemList = getElemList();
  StringBuffer lBuffer = new StringBuffer();
  if (lElemList == null)
    return "{}";
  lBuffer.append("(");
  for (java.util.Iterator lElemIterator = lElemList.iterator();
       lElemIterator.hasNext(); ) {
    lElem = ((Elem)lElemIterator.next());
    if (lElem != null) {
      lBuffer.append("(")
             .append(lElem.getSymType().toStringShort()).append(" ")
             .append(lElem.getName())
             .append(") ");
    }
  }
  lBuffer.append(")");
  return lBuffer.toString().intern();
} // getElemListString

public static String
getElemListString( IrList pElemList ) {
  Elem         lElem;
  StringBuffer lBuffer = new StringBuffer();
  if (pElemList == null)
    return "( )";
  for (java.util.Iterator lElemIterator = pElemList.iterator();
       lElemIterator.hasNext(); ) {
    lElem = (Elem)(lElemIterator.next());
    if (lElem != null) {
      lBuffer.append("(")
             .append(lElem.getSymType().toStringShort()).append(" ")
             .append(lElem.getName())
             .append(") ");
    }
  }
  return lBuffer.toString().intern();
} // getElemListString

/** getEnumListString
 *  Get element list of enum list pEnumList in String form.
**/
public static String
getEnumListString( IrList pEnumList ) {
  IrList       lElemIndexPair;
  IrList       lElemList = pEnumList;
  StringBuffer lBuffer = new StringBuffer();
  if (lElemList == null)
    return "( )";
  lBuffer.append("(");
  for (java.util.Iterator lElemIterator = lElemList.iterator();
       lElemIterator.hasNext(); ) {
    lElemIndexPair = ((IrList)lElemIterator.next());
    if (lElemIndexPair != null) {
      lBuffer.append("(")
             .append(lElemIndexPair.get(0).toString()).append(") ");
    }
  }
  lBuffer.append(")");
  return lBuffer.toString().intern();
} // getEnumListString

public int
getAlignment()
{
  int lAlignment;
  //##67 if (fTypeKind == Type.KIND_SUBP) {
  //##67   lAlignment = ((SubpType)this).getReturnType().getAlignment();
  //##67 }else {
  lAlignment = Type.KIND_ALIGNMENT[fTypeKind];
            // STRUCT, UNION, VECTOR have their own method.
  //##67 }
  return lAlignment;
}

public int                             // to be refined
getAlignmentGap( long pPrecedingSize ) {
  int lAlignment, lAlignmentGap;
  long lResidue;
  lAlignment = getAlignment();
  if (lAlignment <= 0)  //##79
    lResidue = 0; //##79
  else   //##79
    lResidue = pPrecedingSize % lAlignment;
  if (lResidue == 0)
    lAlignmentGap = 0;
  else
    lAlignmentGap = (int)(lAlignment - lResidue);
  return lAlignmentGap;
} // getAlignmentGap

/** isCompatibleWith
 *  See isCompatibleWith in VectorType, StructType, UnionType,
 *      EnumType, PointerType, SubpType.
 *  @return true if this type is compatible with pType.
**/
public boolean
isCompatibleWith( Type pType )
{
  if (pType == this)
    return true;
  else {
    Type lType1 = getFinalOrigin();
    Type lType2 = pType.getFinalOrigin();
    if (lType1 == lType2)
      return true;
    else {
      return false;
    }
  }
} // isCompatibleWith

/** getCompleteType
 *  Get complete type corresponding to this type.
 *  If this is a complete type, return this type.
 *  See getCompleteType in VectorType, StructType, UnionType,
 *      EnumType.
**/
public Type
getCompleteType()
{
  return fCompleteType;
} // getCompleteType

public Type
searchTypeCompatibleWithThis()
{
  SymTable lSymTable;
  Sym      lSym;
  lSymTable = getRecordedIn();
  if (lSymTable == null)
    return null;
  for (SymIterator lIterator = lSymTable.getSymIterator();
       lIterator.hasNext(); ) {
    lSym = lIterator.next();
    if ((lSym != null)&&(lSym.getSymKind() == Sym.KIND_TYPE)) {
      if (((Type)lSym).getTypeKind() == getTypeKind())
        if (((Type)lSym).isCompatibleWith(this))
          return (Type)lSym;
    }
  }
  return null;
} // searchTypeCompatibleWithThis

/** setStaticTable set up static table KIND_ALIGNMENT.
 * This method is not recommended to be called except from SymRoot.
 */
public void
setStaticTable(MachineParam pMachineParam)
{
  for (int i = 0; i < KIND_ALIGNMENT.length; i++) {    //##52
    KIND_ALIGNMENT[i] = pMachineParam.getAlignment(i); //##52
  }                                                    //##52
  /* //##52
  KIND_ALIGNMENT[Type.KIND_UNDEF  ] = pMachineParam.ALIGN_INT;
  KIND_ALIGNMENT[Type.KIND_BOOL   ] = pMachineParam.ALIGN_BOOL;
  KIND_ALIGNMENT[Type.KIND_WCHAR  ] = pMachineParam.ALIGN_WCHAR;
  KIND_ALIGNMENT[Type.KIND_SHORT  ] = pMachineParam.ALIGN_SHORT;
  KIND_ALIGNMENT[Type.KIND_INT    ] = pMachineParam.ALIGN_INT;
  KIND_ALIGNMENT[Type.KIND_LONG   ] = pMachineParam.ALIGN_LONG;
  KIND_ALIGNMENT[Type.KIND_LONG_LONG] = pMachineParam.ALIGN_LONG_LONG;
  KIND_ALIGNMENT[Type.KIND_CHAR   ] = pMachineParam.ALIGN_CHAR;
  KIND_ALIGNMENT[Type.KIND_U_CHAR ] = pMachineParam.ALIGN_CHAR;
  KIND_ALIGNMENT[Type.KIND_U_SHORT] = pMachineParam.ALIGN_SHORT;
  KIND_ALIGNMENT[Type.KIND_U_INT  ] = pMachineParam.ALIGN_INT;
  KIND_ALIGNMENT[Type.KIND_U_LONG ] = pMachineParam.ALIGN_LONG;
  KIND_ALIGNMENT[Type.KIND_U_LONG_LONG] = pMachineParam.ALIGN_LONG_LONG;
  KIND_ALIGNMENT[Type.KIND_ADDRESS] = pMachineParam.ALIGN_ADDRESS;
  KIND_ALIGNMENT[Type.KIND_OFFSET ] = pMachineParam.ALIGN_OFFSET;
  KIND_ALIGNMENT[Type.KIND_VOID   ] = pMachineParam.ALIGN_VOID;
  KIND_ALIGNMENT[Type.KIND_FLOAT  ] = pMachineParam.ALIGN_FLOAT;
  KIND_ALIGNMENT[Type.KIND_DOUBLE ] = pMachineParam.ALIGN_DOUBLE;
  KIND_ALIGNMENT[Type.KIND_LONG_DOUBLE] = pMachineParam.ALIGN_LONG_DOUBLE;
  KIND_ALIGNMENT[Type.KIND_FLOAT_UPPER_LIM] = pMachineParam.ALIGN_LONG_DOUBLE;
  KIND_ALIGNMENT[Type.KIND_STRING ] = pMachineParam.ALIGN_CHAR;
  KIND_ALIGNMENT[Type.KIND_ENUM   ] = pMachineParam.ALIGN_ENUM;
  KIND_ALIGNMENT[Type.KIND_POINTER] = pMachineParam.ALIGN_ADDRESS;
  KIND_ALIGNMENT[Type.KIND_VECTOR ] = pMachineParam.ALIGN_INT;// Not used.
  KIND_ALIGNMENT[Type.KIND_STRUCT ] = pMachineParam.ALIGN_STRUCT_MIN;
  KIND_ALIGNMENT[Type.KIND_UNION  ] = pMachineParam.ALIGN_STRUCT_MIN;
  KIND_ALIGNMENT[Type.KIND_DEFINED] = pMachineParam.ALIGN_INT;
  KIND_ALIGNMENT[Type.KIND_SUBP   ] = pMachineParam.ALIGN_SUBP;
  KIND_ALIGNMENT[Type.KIND_REGION ] = pMachineParam.ALIGN_REGION_MIN;
  KIND_ALIGNMENT[29] = pMachineParam.ALIGN_LONG_DOUBLE;// largest alignment size
  */ //##52
} // setStaticTable

public String
toString() {
  String symString = fName;
  return symString;
} // toString

public String
toStringShort() {
  String symString = fName;
  return symString;
} // toStringShort

public String
toStringDetail() {
  String symString = super.toStringDetail();
  Type   lType, lType2, lType3;
  if (fSizeValue > 0)
    symString = symString + " size " + fSizeValue; // S.Fukuda 2002.10.30
  else if (fSizeExp != null) {
    symString = symString + " sizeExp " + fSizeExp.toStringWithChildren();
  }
  if (getFlag(Sym.FLAG_UNFIXED_SIZE))
    symString = symString + " unfixedSize"; //##64
  if (fConst)
    symString = symString + " const";
  if (fVolatile)
    symString = symString + " volatile";
  if (fRestrict)  //##81
    symString = symString + " restrict";  //##81
  if ((fOrigin != null)&&(fOrigin != this))
    symString = symString + " org " + fOrigin.toStringShort();
  if (fDbgLevel >= 4) {
    lType = searchTypeCompatibleWithThis();
    if ((lType != null)&&(lType != this))
      symString = symString + " compatibleWith " + lType.toStringShort();
    lType2 = getCompleteType();
    if (lType2 == null)
      symString = symString + " completeType unfixed ";
    else {
      if (lType2 != this)
        symString = symString + " completeType " + lType2.toStringShort();
    }
  }
  return symString;
} // toStringDetail

} // TypeImpl
