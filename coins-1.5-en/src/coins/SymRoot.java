/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
// SymRoot.java
//                      //##10 Dec. 2001.
package coins;

// import coins.*;
import coins.sym.BoolConst;    //##51
import coins.sym.FloatConst;   //##51
import coins.sym.IntConst;     //##51
import coins.sym.StringConst;  //##82
import coins.sym.Sym;          //##51
import coins.sym.SymImpl;      //##51
import coins.sym.SymTable;     //##51
import coins.sym.SymTableImpl; //##51
import coins.sym.Subp;         //##51
import coins.sym.Type;         //##51
import coins.sym.TypeImpl;     //##51
import coins.sym.Var;          //##51
//##32 import coins.flow.Flow;
import coins.ir.IrList;        //##51
import coins.ir.IrListImpl;    //##51
import coins.ir.hir.HIR;
import coins.ir.hir.BlockStmt; //##82
import coins.ir.hir.InfStmt;   //##82
import coins.ir.hir.Program;
import coins.ir.hir.SubpDefinition;
import coins.ir.hir.Stmt;      //##82
import java.util.Iterator;     //##82
import java.util.LinkedList;
import java.util.Set;          //##51
import java.util.HashSet;      //##51

/**
<PRE>
 *  SymRoot class is used to access Sym (symbol) information and
 *  information prepared by other classes such as IoRoot, HIR, etc.
 *  All Sym objects contain a reference to the SymRoot object
 *  from which symbol information and methods
 *  can be quickly accessed.
 *  The SymRoot object contains a reference to IoRoot.
 *  Thus, every Sym objects can access input/output methods, too.
 *
*** Symbol tables:
 *
  public SymTable
    symTable        = null, // Instance to call SymTable methods.
    symTableRoot    = null, // Root of SymTable.
    symTableConst   = null, // Constant table.
    symTableUnique  = null, // SymTable that contains
                            // generated unique name.
    symTableCurrent = null, // Referes to the symbol table for
        // subprogram, etc. under construction or under processing.
        // At parsing phase, Sym methods pushSymTable and popSymTable
        // set symTableCurrent automatically. In parsing, at the point
        // of scope change, such as structure definition, symTableCurrent
        // should be properly set (by pushSymTable, popSymTable, or
        // by explicitly setting statements.)
        // In phases that modify HIR (optimization phase, etc.),
        // symTableCurrent should be set to the symbol table local
        // to the subprogram under processing.
    symTableCurrentSubp = null; // Symbol table of current subprogram.
                        // Some kind of symbols (type, etc.) are
                        // registered not in symTableCurrent
                        // but in symTableCurrentSubp.

*** Type symbols representing basic types:
 *    They are recorded in SymTableRoot as symbols whose name
 *    is preceeded by " " so that they do not conflict with
 *    identifiers used in input program.
 *    The names are shown in the tail of following lines.
  public Type
    typeBool,       // bool  " bool"
    typeChar,       // character  " char"
    typeShort,      // short integer  " short"
    typeInt,        // integer  " int"
    typeLong,       // long integer  " long"
    typeLongLong,   // long long integer  " long_long"
    typeU_Char,     // unsigned character  " u_char"
    typeU_Short,    // unsigned short integer  " u_short"
    typeU_Int,      // unsigned integer  " u_int"
    typeU_Long,     // unsigned long integer  " u_long"
    typeU_LongLong, // unsigned long long integer  " u_long_long"
    typeFloat,      // float  " float"
    typeDouble,     // double float  " double"
    typeLongDouble, // long double  " long_double"
    typeVoid,       // void  " void"
    typeOffset,     // offset of address  " offset"
    typeAddress;    // address  " address"
  public Type
    typeStringAny;  // String type whose length is not fixed
                    // (VECT 0 typeU_Char).

*** Other public symbols

  public Subp
     subpCurrent; // Current subprogram under processing.
                  // In flow analysis, alias analysis,
                  // optimization, parallelization, it is
                  // recommended to set this field to reflect
                  // the subprogram under processing.

  public BoolConst
    boolConstTrue,   // Bool constant representing true  " true"
    boolConstFalse;  // Bool constant representing false " false"

  public IntConst
    intConst0,             // constant 0
    intConst1,             // constant 1
    longConst0;            // long int const 0

  public FloatConst
    floatConst0,           // float  const 0.0
    doubleConst0;          // double const 0.0

*** Coding rules in SymRoot
 *
 * Methods begin with lower case letter.
 * Constants (final static int, etc.) are spelled in upper case letters.
 * Indentation is 2 characters.
 * Formal parameters begin with p.
 * Local variables begin with l.
 * Methods and variables are named so that meaning is easily guessed.
 * Short names less than 3 characters are not used except for
 * very local purpose.
 * Protected or private fields starts with character f.
 * Public fields of xxxRoot class might not start with character f.
</PRE>
**/
public class
SymRoot
{

//==== Public fields ====//

  /** ioRoot records the reference to the IoRoot object
   *  passed as a parameter of SymRoot constructor.
   *  It is used in accessing IoRoot information and
   *  invoking IoRoot methods.
  **/
  public final IoRoot
    ioRoot;

  /** symRoot referes SymRoot object that is used to
   *  access SymRoot information.
   *  All Sym objects contain a reference to the SymRoot object.
   *  As the SymRoot object contains a reference to the
   *  IoRoot object, all SymRoot information and IoRoot information
   *  can be accessed from Sym methods.
  **/
  public final SymRoot
    symRoot;

  /** sym is an instance of Sym class used to access
   *  Sym methods such as creation of Sym instance, etc.
   *  from othe classes in such way as
   *    symRoot.sym.defineVar("abc".intern(), symRoot.typeInt).
  **/
  public final Sym
    sym;

  /** sourceLanguage is an instance of SourceLanguage class
   *  used to access source language dependent information
   *  and methods in such way as
   *    symRoot.sourceLanguage.makeCstringWithTrailing0(pureString).
  **/
  public final SourceLanguage // Used to invoke methods
    sourceLanguage;           // that depend on source language.

  /** Reference to MachineParam copied from ioRoot*/
  public final MachineParam //##51
    machineParam;

  //---- Symbol tables ----//
  public SymTable
    symTable        = null, // Instance to call SymTable methods.
    symTableRoot    = null, // Root of SymTable.
    symTableConst   = null, // Constant table.
    symTableUnique  = null, // SymTable that contains
                            // generated unique name.
    symTableCurrent = null, // Referes to the symbol table for
                        // subprogram, etc. under construction
                        // or under processing.
    symTableCurrentSubp = null; // Symbol table of current subprogram. (##6)
                        // Some kind of symbols (type, etc.) are
                        // registered not in symTableCurrent
                        // but in symTableCurrentSubp.
  //##78 BEGIN
  public SymTable
    symTableFlow    = null; // Contains ExpId etc. used for
                            // flow analysis.
  //##78 END

  /** Type symbols representing basic types
  **/
  public Type
    typeBool,       // bool
    typeChar,       // character
    typeShort,      // short integer
    typeInt,        // integer
    typeLong,       // long integer
    typeLongLong,   // long long integer
    typeU_Char,     // unsigned character
    typeU_Short,    // unsigned short integer
    typeU_Int,      // unsigned integer
    typeU_Long,     // unsigned long integer
    typeU_LongLong, // unsigned long long integer
    typeFloat,      // float
    typeDouble,     // double float
    typeLongDouble, // long double
    //##14 typeString,
    typeVoid,       // void
    typeOffset,     // offset of address
    typeAddress;    // address
  public Type
    typeStringAny,  // String type whose length is not fixed //##14
                    // (VECT 0 getCharType()). //##29
    typeRegion;     // RegionType for unnamed region (blank region). //##29
                    // It is not effective until its elements are added
                    // and finishCurrentRegion of Region is called.
                    // This is used when RegionType parameter is required.

  public Subp
     //##14 subpMain,    // Symbol for main subprogram.
                  //## refered from ToHirC, Convereter
     subpCurrent; // Current subprogram under processing

  public BoolConst
    boolConstTrue,         // "true"
    boolConstFalse;        // "false"

  public IntConst
    intConst0,             // constant 0
    intConst1,             // constant 1
    longConst0;            // long int const 0

  public FloatConst
    floatConst0,           // float const 0
    doubleConst0;          // double const 0

  //##51 BEGIN
  /** conflictingSpecialSyms:
   * Record symbols with '_' as heading character
   * in order to avoid conflicts in generating symbols
   * by generateLabel, generateVar, etc.
   * All symbols with heading '_' are recorded
   * irrespective of source program symbol or generated symbols.
  **/
  public Set
    conflictingSpecialSyms = new HashSet();
  //##51 END

  //##71 BEGIN
  /**
   * Set of arrays which are declared as safe, that is,
   * their subscript values does not exceed their limit
   * and accessed locations are always within the storage
   * area allocated to the array variable.
   * The declaration may be given in such way as
   *   #pragma optControl safeArray var1 var2 ...
   */
  public Set
    safeArray = new HashSet();
  //##71 END

//---- Protected/private fields ----//

  private HirRoot
    fHirRoot = null;        // Used to access HirRoot information.

  private FlowRoot
    fFlowRoot = null;       // Used to access FlowRoot information.

  private HIR
    fHir = null;            // Used to invoke HIR methods.

//##32  private coins.aflow.Flow //##32
//##32   fFlow = null;           // Used to invoke Flow methods.

  private int
    fEntranceCount = 0; // 0 if no SymTable is initiated.

  private Set
    fFunctionsWithoutSideEffect = null; //##82

  protected int
    fVarCount   = 0,  // Generated variable  counter.
    fParamCount = 0,  // Generated parameter counter.
    fElemCount  = 0,  // Generated element   counter.
    fLabelCount = 0,  // Generated label     counter.
    fGenSymCount= 0,  // Generated symbol/tag counter.
    fARegCount  = 0,  // Generated abstract register counter.
    fMRegCount  = 0;  // Generated machine  register counter.

//==== Constructor & other methods ====//

public            // Constructor of the SymRoot class.
SymRoot( IoRoot pIoRoot )
{
  ioRoot  = pIoRoot;  // Record the parameter.
  symRoot = this;
  sym = new SymImpl(this); // Sym instance used in Sym object creation.

  //##51 BEGIN
  machineParam = ioRoot.machineParam;
  String lLanguage = ioRoot.getLanguageName();
  if (lLanguage.equals("C")) {
    sourceLanguage = new SourceLanguageC(this);
  }else if (lLanguage.equals("FORTRAN")) {
    sourceLanguage = new SourceLanguageFortran(this);
  }else
    sourceLanguage = new SourceLanguage(this);
  //##51 END

  ioRoot.symRoot = this; //##12
  ((SymImpl)sym).setParameters(machineParam, sourceLanguage); //##51
  (new TypeImpl(this)).setStaticTable(machineParam);          //##51
} // SymRoot

/** irList creates an instance of IrList from LinkedList.
 *  It makes a list that can be treated as an instance of IR
 *  from linked list of objects.
 *  @param pList: list of objects.
 *  @return an instance of IrList containing objects given in pList.
**/
public IrList                              //##8
irList( LinkedList pList )                 //##8
{                                          //##8
  return new IrListImpl(fHirRoot, pList);  //##8
}                                          //##8

/** initiate() does the initiation procedure of SymRoot
 *  such as symbol table initiation, etc.
**/
public void
initiate()
{
  symTableRoot    = new SymTableImpl(this);
  symTableConst   = new SymTableImpl(this);
  symTableUnique  = new SymTableImpl(this);
  symTableCurrent = symTableRoot;
  symTable        = symTableRoot;
  ((SymTableImpl)symTableRoot).fTableName  = "Root";
  ((SymTableImpl)symTableConst).fTableName = "Constant";
  ((SymTableImpl)symTableUnique).fTableName = "Unique"; //##16
//##  sym = new SymImpl(this);  // sym instance that is used in Sym object creation.
  if (fEntranceCount == 0) {  // Do initalization only onece.
    fEntranceCount++;
    typeInt        = sym.baseType(
                       " int".intern(), Type.KIND_INT); //##31
    //////////////////////////////////// S.Fukuda 2002.9.3 begin
    // typeInt should be set 1st because it is used in baseType later.
    //typeInt.setSizeExp((ConstNode)(new ConstNodeImpl(fHirRoot, //##8
    //                   (Const)(new IntConstImpl(this, MachineParam.SIZEOF_INT,
    //                                            typeInt)))));

    typeOffset     = sym.baseType(
                       " offset".intern(), Type.KIND_OFFSET); //##31
    //typeInt.setSizeExp( fHirRoot.hir.offsetConstNode(MachineParam.SIZEOF_INT) );
    typeInt.setSizeValue(machineParam.evaluateSize(Type.KIND_INT));       //##52
    typeOffset.setSizeValue(machineParam.evaluateSize(Type.KIND_OFFSET)); //##52




    //////////////////////////////////// S.Fukuda 2002.9.3 end
    //##14 typeString     = sym.baseType(
    //##14                   "string".intern(), Type.KIND_STRING);
    typeVoid       = sym.baseType(
                       " void".intern(), Type.KIND_VOID); //##31
    typeBool       = sym.baseType(
                       " bool".intern(), Type.KIND_BOOL); //##31
    typeChar       = sym.baseType(
                       " char".intern(), Type.KIND_CHAR); //##31
    typeShort      = sym.baseType(
                       " short".intern(), Type.KIND_SHORT); //##31
    typeLong       = sym.baseType(
                       " long".intern(), Type.KIND_LONG); //##31
    typeLongLong   = sym.baseType(
                       " long_long".intern(), Type.KIND_LONG_LONG); //##31
    typeU_Char     = sym.baseType(
                       " u_char".intern(), Type.KIND_U_CHAR); //##31
    typeU_Short    = sym.baseType(
                       " u_short".intern(), Type.KIND_U_SHORT); //##31
    typeU_Int      = sym.baseType(
                       " u_int".intern(), Type.KIND_U_INT); //##31
    typeU_Long     = sym.baseType(
                       " u_long".intern(), Type.KIND_U_LONG); //##31
    typeU_LongLong = sym.baseType(
                       " u_long_long".intern(), Type.KIND_U_LONG_LONG); //##31
    typeAddress    = sym.baseType(
                       " address".intern(), Type.KIND_ADDRESS); //##31
    //////////////////////////////////// S.Fukuda 2002.9.3 begin
    //typeOffset     = sym.baseType(
    //                   " offset".intern(), Type.KIND_OFFSET);
    //////////////////////////////////// S.Fukuda 2002.9.3 end
    typeFloat      = sym.baseType(
                       " float".intern(), Type.KIND_FLOAT); //##31
    typeDouble     = sym.baseType(
                       " double".intern(), Type.KIND_DOUBLE); //##31
    typeLongDouble = sym.baseType(
                       " long_double".intern(), Type.KIND_LONG_DOUBLE); //##31
    typeStringAny  = sym.vectorType(getCharType(), 0); //##29
    typeRegion     = sym.regionType(machineParam.blankRegionName(), Var.VAR_STATIC); //##51

    //##14 subpMain = (Subp)symTableRoot.define( "main".intern(), Sym.KIND_SUBP, null);
    //##45 boolConstTrue = sym.boolConst( " true".intern()); //##31 See BoolConstImpl.
    //##45 boolConstFalse = sym.boolConst( " false".intern()); //##31
    // As for boolConstTrue, boolConstFalse, see BoolConstImpl.
    boolConstTrue = (BoolConst)(symRoot.symTableConst.searchOrAdd(
       " true".intern(), Sym.KIND_BOOL_CONST, null, true, true )); //##45
    boolConstFalse = (BoolConst)(symRoot.symTableConst.searchOrAdd(
       " false".intern(), Sym.KIND_BOOL_CONST, null, true, true )); //##45
    typeAddress.setFlag(Sym.FLAG_RESERVED_NAME, true); //##31
    typeBool.setFlag(Sym.FLAG_RESERVED_NAME, true); //##31
    typeChar.setFlag(Sym.FLAG_RESERVED_NAME, true); //##31
    typeDouble.setFlag(Sym.FLAG_RESERVED_NAME, true); //##31
    typeFloat.setFlag(Sym.FLAG_RESERVED_NAME, true); //##31
    typeInt.setFlag(Sym.FLAG_RESERVED_NAME, true); //##31
    typeLong.setFlag(Sym.FLAG_RESERVED_NAME, true); //##31
    typeLongDouble.setFlag(Sym.FLAG_RESERVED_NAME, true); //##31
    typeLongLong.setFlag(Sym.FLAG_RESERVED_NAME, true); //##31
    typeOffset.setFlag(Sym.FLAG_RESERVED_NAME, true); //##31
    typeShort.setFlag(Sym.FLAG_RESERVED_NAME, true); //##31
    typeU_Char.setFlag(Sym.FLAG_RESERVED_NAME, true); //##31
    typeU_Int.setFlag(Sym.FLAG_RESERVED_NAME, true); //##31
    typeU_Long.setFlag(Sym.FLAG_RESERVED_NAME, true); //##31
    typeU_LongLong.setFlag(Sym.FLAG_RESERVED_NAME, true); //##31
    typeU_Short.setFlag(Sym.FLAG_RESERVED_NAME, true); //##31
    typeVoid.setFlag(Sym.FLAG_RESERVED_NAME, true); //##31
    boolConstTrue.setFlag(Sym.FLAG_RESERVED_NAME, true); //##31
    boolConstFalse.setFlag(Sym.FLAG_RESERVED_NAME, true); //##31
    intConst0 = sym.intConst("0".intern(), typeInt);
    intConst1 = sym.intConst("1".intern(), typeInt);
    //## longConst0 = sym.intConst("0L".intern(), typeLong);
    longConst0 = sym.intConst("00".intern(), typeLong); //## 0L is not supported in Windows Java ?
    floatConst0 = sym.floatConst("0.0F".intern(), typeFloat);
    doubleConst0 = sym.floatConst("0.0".intern(), typeDouble);
    if (fHirRoot != null)  // Attach symTableRoot to programRoot.
      ((Program)fHirRoot.programRoot).setSymTable(symTableRoot);
  }
} // initiate

/** getHirRoot: Get the reference to the HirRoot object.
 *  The reference to the HirRoot object will be set by
 *  attachHirRoot when HirRoot information become available.
 *  This method is used to access HIR information from Sym methods.
**/
public HirRoot
getHirRoot()
{
  return fHirRoot;
}

/** getFlowRoot: Get FlowRoot from symbol information.
 *  It will be set by attachFlowRoot when FlowRoot information become available.
**/
public FlowRoot
getFlowRoot()
{
  return fFlowRoot;
}

/** attachHirRoot: Attach HirRoot information when it become available
 *  after the creation of SymRoot object.
**/
public void
attachHirRoot( HirRoot pHirRoot )
{
  fHirRoot = pHirRoot;
}

/** attachFlowRoot: Attach FLowRoot information when it become available.
**/
public void
attachFlowRoot( FlowRoot pFlowRoot )
{
  fFlowRoot = pFlowRoot;
}

/** useSymTableOfSubpDefinition: //##16
 *  Set the subpCurrent to the one corresponding to the
 *  subprogram definition pSubpDef, and set symTableCurrent,
 *  symTableCurrentSubp to the one corresponding to pSubpDef.
 *  This should be called before processing a subprogram
 *  where new symbol may be created (for optimizing/parallelizing
 *  transformation, etc.) in the process.
 *  @param pSubpDef: subprogram definition under processing.
**/
public void
useSymTableOfSubpDefinition( SubpDefinition pSubpDef ) //##16
{
  if (pSubpDef != null) {
    subpCurrent = pSubpDef.getSubpSym();
    if (pSubpDef.getSymTable() != null) {
      symTableCurrent = pSubpDef.getSymTable();
      symTableCurrentSubp = pSubpDef.getSymTable();
    }
  }
} // useSymTableOfSubpDefinition

/** getIntTypeOfAddress: //##10
 *  Get integer type corresponding to the address type.
 *  @return one of typeInt, typeU_Int, typeLong, typeU_Long,
 *      type_U_LongLong.
**/
public Type
getIntTypeOfAddress()
{
  switch (machineParam.getIntKindForAddress()) {          //##52
  case MachineParam.INT_TYPE_OF_ADDRESS_IS_INT:   return typeInt;
  case MachineParam.INT_TYPE_OF_ADDRESS_IS_U_INT: return typeU_Int;
  case MachineParam.INT_TYPE_OF_ADDRESS_IS_LONG:  return typeLong;
  case MachineParam.INT_TYPE_OF_ADDRESS_IS_U_LONG:return typeU_Long;
  case MachineParam.INT_TYPE_OF_ADDRESS_IS_U_LONG_LONG: return typeU_LongLong;
  default: return typeU_Long;
  }
}

/** getIntTypeOfChar: //##10
 *  Get integer type corresponding to the char type.
 *  @return one of typeInt, typeU_Int.
**/
public Type
getIntTypeOfChar()
{
  switch (machineParam.getIntKindForChar()) {           //##52
  case MachineParam.INT_TYPE_OF_CHAR_IS_INT:   return typeInt;
  case MachineParam.INT_TYPE_OF_CHAR_IS_U_INT: return typeU_Int;
  default: return typeU_Int;
  }
}

/** getCharType: //##27
 *  @return typeChar if
 *       INT_TYPE_OF_CHAR is INT_TYPE_OF_CHAR_IS_INT
 *     else return typeU_Char.
**/
public Type
getCharType()  //##27
{
  switch (machineParam.getIntKindForChar()) {            //##52
  case MachineParam.INT_TYPE_OF_CHAR_IS_INT:   return typeChar;
  case MachineParam.INT_TYPE_OF_CHAR_IS_U_INT: return typeU_Char;
  default: return typeU_Int;
  }
}

//##82 BEGIN
/**
 * Get the set of functions without side effect.
 * If HIR is not yet built or program initiation part is not given
 * then retuen sourcelanguage.fFunctionsWithoutSideEffect;
 * else {
 *   if the set is not yet recorded,
 *     copy sourcelanguage.fFunctionsWithoutSideEffect
 *     and adjust the copied set by
 *     #pragma optControl functionsWithoutSideEffect f1 f2 ...
 *     #pragma optControl functionsWithSideEffect g1 g2 ...
 *     and return the result after recording the set.
 *   else
 *     return the recorded set.
 * }
 * @return the set of functions without side effect.
 */
public Set
  getFunctionsWithoutSideEffect()
{
  if (fFunctionsWithoutSideEffect == null) {
    if ((fHirRoot == null)||
        (! (((Program)fHirRoot.programRoot).getInitiationPart() instanceof BlockStmt))) {
      return sourceLanguage.functionsWithoutSideEffect;
    }
    else {
      // Scan the program initiation part to process
      //   #pragma optControl functionsWithoutSideEffect f1 f2 ...
      //   #pragma optControl functionsWithSideEffect g1 g2 ...
      BlockStmt lProgInitBlock = (BlockStmt)((Program)fHirRoot.programRoot).
        getInitiationPart();
      fFunctionsWithoutSideEffect = new HashSet();
      fFunctionsWithoutSideEffect.addAll(sourceLanguage.
        functionsWithoutSideEffect);
      for (Stmt lStmt = lProgInitBlock.getFirstStmt();
           lStmt != null;
           lStmt = lStmt.getNextStmt()) {
        if ((lStmt instanceof InfStmt) &&
            (((InfStmt)lStmt).getInfKind() == "optControl")) {
          ioRoot.dbgSym.print(3, lStmt.toString() + "\n");
          IrList lOptionList = ((InfStmt)lStmt).getInfList("optControl");
          int lIndex;
          Object lObject = lOptionList.get(0);
          ioRoot.dbgSym.print(3, " option name " + lObject
            + " " + lObject.getClass() + " " + lOptionList + "\n");
          String lOptionName;
          if (lObject instanceof String) {
            lOptionName = (String)lObject;
          }
          else if (lObject instanceof StringConst) {
            lOptionName = ((StringConst)lObject).getStringBody();
          }
          else if (lObject instanceof Sym) {
            lOptionName = ((Sym)lObject).getName();
          }
          else {
            ioRoot.dbgSym.print(1, "\nUnknown option kind" + lObject + "\n");
            continue;
          }
          lOptionName = lOptionName.intern();
          if (lOptionName == "functionsWithoutSideEffect") {
            lIndex = 0;
            for (Iterator lIt = lOptionList.iterator();
                 lIt.hasNext(); lIndex++) {
              Object lSubp = lIt.next();
              ioRoot.dbgSym.print(4, " " + lSubp
                + " " + lSubp.getClass() + "\n");
              if ((lSubp instanceof Subp)) {
                fFunctionsWithoutSideEffect.add(((Subp)lSubp).getName().intern());
              }
            }
          }
          else if (lOptionName == "functionsWithSideEffect") {
            lIndex = 0;
            for (Iterator lIt = lOptionList.iterator();
                 lIt.hasNext(); lIndex++) {
              Object lSubp = lIt.next();
              ioRoot.dbgSym.print(4, " " + lSubp
                + " " + lSubp.getClass() + "\n");
              if ((lSubp instanceof Subp)) {
                if (fFunctionsWithoutSideEffect.
                    contains(((Subp)lSubp).getName().intern())) {
                  fFunctionsWithoutSideEffect.
                     remove(((Subp)lSubp).getName().intern());
                }
              }
            }
          }
          else {
            // Other optControl items.
            continue;
          }
        } // End for lparallel option
      } // End for each Stmt in InitiationPart
    } // End of InitiationPart processing.
    if (ioRoot.dbgSym.getLevel() > 0) {
      ioRoot.dbgSym.print(1, "\nfunctionsWithoutSideEffect="
        + fFunctionsWithoutSideEffect);
      /*
      for (Iterator lIt = fFunctionsWithoutSideEffect.iterator();
           lIt.hasNext(); ) {
        Sym lSym = (Sym)lIt.next();
        ioRoot.dbgSym.print(1, " " + lSym.getName());
      }
      ioRoot.dbgSym.print(1, " }");
      */
    }
  }
  return fFunctionsWithoutSideEffect;
} // getFunctionsWithoutSideEffect

/** getVarCount:
 *  Get the generated variable counter.
**/
public int
getVarCount()
{
  return fVarCount;
}

/** incrementVarCount:
 *  Increment the generated variable counter and return updated value.
**/
public int
incrementVarCount()
{
  return ++fVarCount;
}

/** getParamCount:
 *  Get the generated parameter counter.
**/
public int
getParamCount()
{
  return fParamCount;
}

/** incrementParamCount:
 *  Increment the generated parameter counter and return updated value.
**/
public int
incrementParamCount()
{
  return ++fParamCount;
}

/** getElemCount:
 *  Get the generated element counter.
**/
public int
getElemCount()
{
  return fElemCount;
}

/** incrementElemCount:
 *  Increment the generated element counter and return updated value.
**/
public int
incrementElemCount()
{
  return ++fElemCount;
}

/** getLabelCount:
 *  Get the generated label counter.
**/
public int
getLabelCount()
{
  return fLabelCount;
}

/** incrementLabelCount:
 *  Increment the generated label counter and return updated value.
**/
public int
incrementLabelCount()
{
  return ++fLabelCount;
}

/** getSymCount:
 *  Get the generated symbol counter.
**/
public int
getSymCount()
{
  return fGenSymCount;
}

/** incrementSymCount:
 *  Increment the generated symbol counter and return updated value.
**/
public int
incrementSymCount()
{
  return ++fGenSymCount;
}

/** getARegCount:
 *  Get the generated abstract-register counter.
**/
public int
getARegCount()
{
  return fARegCount;
}

/** incrementARegCount:
 *  Increment the generated abstract register counter and return updated value.
**/
public int
incrementARegCount()
{
  return ++fARegCount;
}

/** getMRegCount:
 *  Get the generated machine-register counter.
**/
public int
getMRegCount()
{
  return fMRegCount;
}

/** incrementMRegCount:
 *  Increment the generated machine register counter and return updated value.
**/
public int
incrementMRegCount()
{
  return ++fMRegCount;
}

/** resetCounters: reset the counters for generated symbols.
**/
public void
resetCounters()
{
//  fVarCount   = 0;  // Generated variable  counter.
//  fParamCount = 0;  // Generated parameter counter.
//  fElemCount  = 0;  // Generated element   counter.
//  fLabelCount = 0;  // Generated label     counter.
//  fGenSymCount= 0;  // Generated symbol    counter.
    fARegCount  = 0;  // Generated abstract register counter.
    fMRegCount  = 0;  // Generated machine  register counter.
} // resetCounters

} // SymRoot class
