/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.casttohir;

import java.util.ArrayList; //##81
import java.util.Iterator;
import java.util.HashMap; //##81
import java.util.Map; //##81
import java.util.HashSet; //##85

import coins.Debug;
import coins.FatalError;
import coins.HirRoot;
import coins.IoRoot;
import coins.MachineParam;
import coins.SymRoot;
import coins.driver.CoinsOptions; //##81
import coins.driver.CompileSpecification; //##81
import coins.ir.IrList;
import coins.ir.hir.AssignStmt;
import coins.ir.hir.BlockStmt;
import coins.ir.hir.ConstNode;
import coins.ir.hir.Exp;
import coins.ir.hir.ExpStmt;
import coins.ir.hir.HIR;
import coins.ir.hir.IfStmt;
import coins.ir.hir.LabeledStmt;
import coins.ir.hir.QualifiedExpImpl;
import coins.ir.hir.Stmt;
import coins.ir.hir.SubscriptedExp;
import coins.ir.hir.SubscriptedExpImpl;
import coins.ir.hir.SymNode;
import coins.ir.hir.VarNode;
import coins.sym.Label;
import coins.sym.PointerType;
import coins.sym.SubpType;
import coins.sym.Sym;
import coins.sym.SymTable;
import coins.sym.Type;
import coins.sym.Var;
import coins.sym.VectorType;

/** ToHir
 * Offer cooperation with other packages to all classes included in this
 * package, for example, I/O files, compile option information, etc.
 *
 * @author  Shuichi Fukuda
**/
public class ToHir
{
  /** hirRoot
   * Used to access HIR root fields.
  **/
  final HirRoot hirRoot;
  /** symRoot
   * Used to access symbol root fields.
  **/
  final SymRoot symRoot;
  /** ioRoot
   * Used to access I/O root fields.
  **/
  final IoRoot ioRoot;
  /** debug
   * Used to access debugging fields.
  **/
  final Debug debug;
  /** hir
   * HIR instance (used to create HIR objects).
  **/
  final HIR hir;
  /** sym
   * Sym instance (used to create Sym objects).
  **/
  final Sym sym;
  /** typeVoidPtr
   * Type of void pointer.
  **/
  final Type typeVoidPtr;
  /**
   * Define target machine parameters.
  **/
  final MachineParam machineParam;

  /** nowFile
   * Now procesing file name.
  **/
  String nowFile;
  /** nowLine
   * Now procesing line number.
  **/
  int nowLine;

  /** useSubsForPtr
   * Use subscripted expression for pointer (decided by compiler option).
  **/
  final boolean useSubsForPtr;
  /** useArrayParameterSize
   * Use array parameter size (decided by compiler option).
  **/
  final boolean useArrayParameterSize;

  /** useOldLir
   * Use old LIR generator (temporary switch for development).
  **/
  final boolean useOldLir;
  /** optHirFromC
   * Optimize HIR-C (temporary switch for development).
  **/
  final boolean optHirFromC;

  protected Map fHirOptMap; //##81
  final boolean safeArrayAll; //##81

  protected HashSet fAssignsForInitiation; //##85

  public final int fDbgLevel;  //##70
  //-------------------------------------------------------------------
  /** ToHirUtil
   * Constructor to record hirRoot, ioRoot, symRoot to make them available in
   * methods of this class and subclasses.
  **/
  public ToHir(HirRoot pHirRoot,boolean oldlir,boolean fromc)
  {
    hirRoot     = pHirRoot;
    useOldLir   = oldlir;
    optHirFromC = fromc;

    symRoot = pHirRoot.symRoot;
    ioRoot  = pHirRoot.ioRoot;
    debug   = ioRoot.dbgToHir;
    hir     = hirRoot.hir;
    sym     = hirRoot.sym;
    typeVoidPtr = sym.pointerType(symRoot.typeVoid);
    machineParam = ioRoot.machineParam;
    fDbgLevel = debug.getLevel();  //##70
    useSubsForPtr = true;
    //true : undecay(PTR)[INT]
    //false: *(PTR+pointedTypeSize*INT)
    useArrayParameterSize = true; //SF030530
    //true : f(int a[3]) => f(int *a)     use array param size. (UNDECAY 3 0 a)
    //false: f(int a[3]) => f(int *a) not use array param size. (UNDECAY 1 0 a)
    message(1,"ToHir\n"); //##71
    //##81 BEGIN
    CoinsOptions fOptions = ioRoot.getCompileSpecification().getCoinsOptions();
    String lHirOpt = fOptions.getArg("hirOpt");
    if (lHirOpt != null) {
      fHirOptMap = (Map)fOptions.parseArgument(lHirOpt, '/', '.');
      if (fHirOptMap.keySet().contains("safeArrayAll"))
        safeArrayAll = true;
      else
        safeArrayAll = false;
      if (fDbgLevel > 0)
        message(2, " safeArrayAll=" + safeArrayAll);
    }else {
      fHirOptMap = new HashMap();
      safeArrayAll = false;
    }
    fAssignsForInitiation = new HashSet(); //##85
  }
  //-------------------------------------------------------------------
  // message
  //-------------------------------------------------------------------
  /** message
   * Output debug message.
   * @param   level Debug level
   * @param   mes Debug message
  **/
  void message(int level,String mes)
  {
    // level 2: Important results or errors or warning.
    // level 4: Processed results.
    // level 6: Detailed results.
    // level 8: Line number, node contents.
    ioRoot.dbgToHir.print( level, "  ", mes );
  }
  //-------------------------------------------------------------------
  /** warning
   * Output warning message.
   *
   * @param   mes Message.
  **/
  void warning(String mes)
  {
    ioRoot.msgWarning.put( 3333, nowFile+" "+nowLine+": "+mes );
    message(2,"WARNING; "+nowFile+" "+nowLine+": "+mes );
  }
  void warning(Sym s,String mes) //SF050215
  {
    ioRoot.msgWarning.put(
      3333, s.getDefinedFile()+" "+s.getDefinedLine()+": "+mes );
    message(2,"WARNING; "+s.getDefinedFile()+" "+s.getDefinedLine()+": "+mes );
  }
  //-------------------------------------------------------------------
  /** error
   * Output error message.
   *
   * @param   mes Message.
  **/
  void error(String mes)
  {
    if( nowFile==null )
      return;
    if( symRoot.subpCurrent!=null )
      symRoot.subpCurrent.addToErrorCount(1);
    ioRoot.addToTotalErrorCount(1);
    ioRoot.msgError.put( 4444, ""+nowFile+" "+nowLine+": "+mes );
    message(2,"ERROR; "+nowFile+" "+nowLine+": "+mes );
  }
  void error(Sym s,String mes) //SF050215
  {
    ioRoot.addToTotalErrorCount(1);
    ioRoot.msgError.put(
      4444, s.getDefinedFile()+" "+s.getDefinedLine()+": "+mes );
    message(2,"ERROR; "+s.getDefinedFile()+" "+s.getDefinedLine()+": "+mes );
  }
  //-------------------------------------------------------------------
  /** fatal
   * Throws fatal error.
   *
   * @param   mes Message.
  **/
  void fatal(String mes)
  {
    if( symRoot.subpCurrent!=null )
      symRoot.subpCurrent.addToErrorCount(1);
    ioRoot.addToTotalErrorCount(1);
    throw new FatalError(0,mes+" ("+nowFile+":"+nowLine+")");
  }
  //-------------------------------------------------------------------
  /** getOp
   * Get operation name of HIR.
   *
   * @param   h HIR node.
   * @return  Operation name.
  **/
  String getOp(HIR h)
  {
    return HIR.OP_CODE_NAME_DENSE[h.getOperator()];
  }
  //-------------------------------------------------------------------
  // symbol creator
  //-------------------------------------------------------------------
  /** createBlockSym
   * Create block symbol which has unique name.
   *
   * @return  New block symbol.
  **/
  Sym createBlockSym()
  {
    return symRoot.symTableCurrent.generateSym(
      null,
      Sym.KIND_OTHER,
      symRoot.subpCurrent.getName(),
      symRoot.symTableCurrent.getOwner() );
  }
  //-------------------------------------------------------------------
  /** createLabel
   * Create label. It is defined in symRoot.subpCurrent, and registered in
   * symRoot.symTableCurrent.
   *
   * @param   name Label name.
   * @return  Created label.
  **/
  Label createLabel(String name)
  {
    //##101 Label label = (Label)symRoot.symTableCurrentSubp.search(
    //##101   name.intern(), Sym.KIND_LABEL );
	//##101 BEGIN
	Label label;
	if (symRoot.symTableCurrentSubp == null) { 
	  if (ioRoot.dbgToHir.getLevel() > 0) {
	    message(1, " symTableCurrentSubp==null in ToHir.createLabel symTableCurrent " + symRoot.symTableCurrent);
	  }
	  symRoot.symTableCurrentSubp = symRoot.symTableCurrent;
	  label = sym.defineLabel(name.intern());
	}else {
	  label = (Label)symRoot.symTableCurrentSubp.search(
			         name.intern(), Sym.KIND_LABEL );
	}
	//##101 END
    if( label==null )
    {
      SymTable bak = symRoot.symTableCurrent;
      symRoot.symTableCurrent = getSubpTable();
      label = sym.defineLabel( name.intern() );
      symRoot.symTableCurrent = bak;
    }
    return label;
  }
  //-------------------------------------------------------------------
  /** createLabel
   * Create label with name which does not overlap. It is defined in
   * symRoot.subpCurrent, and registered in symRoot.symTableCurrent.
   *
   * @return  Created label.
  **/
  Label createLabel()
  {
    Label label = getSubpTable().generateLabel();
    label.setDefinedIn( symRoot.subpCurrent );
    return label;
  }
  //-------------------------------------------------------------------
  /** getSubpTable
   * Get symbol table which current subprogram has. If symTableCurrentSubp is
   * null, return symTableRoot.
   *
   * @return  table
  **/
  SymTable getSubpTable()
  {
    return symRoot.symTableCurrentSubp!=null
         ? symRoot.symTableCurrentSubp
         : symRoot.symTableRoot;
  }
  //-------------------------------------------------------------------
  // symbol searcher
  //-------------------------------------------------------------------
  // Name space:
  //   Label
  //     KIND_LABEL
  //   Tag (struct, union, enum)
  //     KIND_TAG
  //   Member (Each struct/union has its own name space for members).
  //     KIND_ELEM
  //   Normal identifier (variable, parameter, function, type)
  //     KIND_VAR
  //     KIND_PARAM
  //     KIND_SUBP
  //     KIND_TYPE
  //     (Enumulation literal has already been changed to int by the C parser.)
  //-------------------------------------------------------------------
  /** searchGlobalOrdinaryId
   * Search ordinary identifier (=KIND_VAR,KIND_PARAM,KIND_SUBP,KIND_TYPE)
   * from symTableRoot.
   *
   * @return  Found symbol.
  **/
  Sym searchGlobalOrdinaryId(String name)
  {
    name = name.intern();
    Sym s;
    if( (s=symRoot.symTableRoot.searchLocal(name,Sym.KIND_VAR  ))!=null
    ||  (s=symRoot.symTableRoot.searchLocal(name,Sym.KIND_PARAM))!=null
    ||  (s=symRoot.symTableRoot.searchLocal(name,Sym.KIND_SUBP ))!=null
    ||  (s=symRoot.symTableRoot.searchLocal(makeTypedefName(name),
                                                 Sym.KIND_TYPE ))!=null )
      return s;
    return null;
  }
  //-------------------------------------------------------------------
  /** searchLocalOrdinaryId
   * Search ordinary identifier (=KIND_VAR,KIND_PARAM,KIND_SUBP,KIND_TYPE)
   * from symTableCurrent.
   *
   * @return  Found symbol.
  **/
  Sym searchLocalOrdinaryId(String name)
  {
    name = name.intern();
    Sym s;
    if( (s=symRoot.symTableCurrent.searchLocal(name,Sym.KIND_VAR  ))!=null
    ||  (s=symRoot.symTableCurrent.searchLocal(name,Sym.KIND_PARAM))!=null
    ||  (s=symRoot.symTableCurrent.searchLocal(name,Sym.KIND_SUBP ))!=null
    ||  (s=symRoot.symTableCurrent.searchLocal(makeTypedefName(name),
                                                    Sym.KIND_TYPE ))!=null )
      return s;
    return null;
  }
  //-------------------------------------------------------------------
  /** searchOrdinaryId
   * Search ordinary identifier (=KIND_VAR,KIND_PARAM,KIND_SUBP,KIND_TYPE)
   * from symTableCurrent and it's parents.
   *
   * @return  Found symbol.
  **/
  Sym searchOrdinaryId(String name)
  {
    name = name.intern();
    for( SymTable table=symRoot.symTableCurrent;
         table!=null;
         table=table.getParent() )
    {
      Sym s;
      if( (s=table.searchLocal(name,Sym.KIND_VAR  ))!=null
      ||  (s=table.searchLocal(name,Sym.KIND_SUBP ))!=null
      ||  (s=table.searchLocal(name,Sym.KIND_PARAM))!=null
      ||  (s=table.searchLocal(makeTypedefName(name),
                                    Sym.KIND_TYPE ))!=null )
        return s;
    }
    return null;
  }
  //-------------------------------------------------------------------
  private String makeTypedefName(String name) //SF041126
  {
    return  ("<TYPEDEF "+name+">").intern();
  }
  //-------------------------------------------------------------------
  // type manager
  //-------------------------------------------------------------------
  /** vectorType
   * Create vector type.
   * Vector type is created in the same symbol table as element type.
   *
   * @param   t Element type.
   * @param   n Element count.
   * @return  Vector type element type t.
  **/
  VectorType vectorType(Type t,long n)
  {
    SymTable table = symRoot.symTableCurrent.searchTableHaving(t); //##12
    if( table==null )
      fatal("vectorType");

    SymTable bak = symRoot.symTableCurrent;
    symRoot.symTableCurrent = table;
    VectorType r = sym.vectorType(t,n);
    symRoot.symTableCurrent = bak;
    if (ioRoot.dbgToHir.getLevel() > 3) //##67
      message(6,"VECTORTYPE="+r);
    return r;
    //VectorType vec = new VectorTypeImpl(symRoot,t,n);
    //SymTable   table = symRoot.symTableCurrent.searchTableHaving(t); //##12
    //if( table==null )
    //  fatal("vectorType");
    //return (VectorType)table.searchOrAddSym(vec); //##12
  }
  //-------------------------------------------------------------------
  /** isCompatible
   * Is compatible types ?
   * @param   t1 Type.
   * @param   t2 Type.
   * @param   checkqualifier  true:Check qualifier completely.
   *                          false:Disregard qualifier.
   * @return  True if compatible type.
  **/
  boolean isCompatible(Type t1,Type t2,boolean checkqualifier)
  {
    if( t1.getTypeKind()!=t2.getTypeKind() ) // Type kind differs.
      return false;
    if( checkqualifier )
      if( t1.isConst()   !=t2.isConst()
      ||  t1.isVolatile()!=t2.isVolatile() ) // Type qualification differs.
        return false;
    switch( t1.getTypeKind() ) // Branch according to the type kind.
    {
      //-----------------------------------------------------------
      case Type.KIND_POINTER: // Pointer.
        return isCompatible( ((PointerType)t1).getPointedType(),
                             ((PointerType)t2).getPointedType(), true );
      //-----------------------------------------------------------
      case Type.KIND_VECTOR: // Array.
        if( ((VectorType)t1).getElemCount()==((VectorType)t2).getElemCount()
        ||  ((VectorType)t1).getElemCount()==0
        ||  ((VectorType)t2).getElemCount()==0 )
          return isCompatible( ((VectorType)t1).getElemType(),
                               ((VectorType)t2).getElemType(), true );
        return false;
      //-----------------------------------------------------------
      case Type.KIND_SUBP: // Function.

        if( !isCompatible( ((SubpType)t1).getReturnType(),
                           ((SubpType)t2).getReturnType(), true ) )
          // Return value type is not compatible
          return false;

        //SF041126[
        IrList ptl1 = ((SubpType)t1).getParamTypeList();
        IrList ptl2 = ((SubpType)t2).getParamTypeList();
        boolean op1 = ((SubpType)t1).hasOptionalParam();
        boolean op2 = ((SubpType)t2).hasOptionalParam();
        boolean nps1 = ((SubpType)t1).hasNoParamSpec();
        boolean nps2 = ((SubpType)t2).hasNoParamSpec();

        if( nps1 && nps2 )
        {
          return true;
        }
        if( nps1 && !nps2 && !op2 )
        {
          // One type is a prototype declaration with empty identifier list and
          // the other is a function definition/declaration without optional parameters.
          for( Iterator i=ptl2.iterator(); i.hasNext(); )
          {
            Type pt = ((Type)i.next()).getUnqualifiedType();
            if( pt!=daPromotedType(pt) )
              return false;
          }
          return true;
        }
        if( !nps1 && !op1 && nps2 )
        {
          // One type is a function definition/declaration without optional parameters
          // and the other is a prototype declaration with empty identifier list.
          for( Iterator i=ptl1.iterator(); i.hasNext(); )
          {
            Type pt = ((Type)i.next()).getUnqualifiedType();
            if( pt!=daPromotedType(pt) )
              return false;
          }
          return true;
        }
        if( !nps1 && !nps2 && op1==op2 && ptl1.size()==ptl2.size() )
        {
          // Parameter counts and optional parameter specifications coinside.
          for( Iterator i=ptl1.iterator(), j=ptl2.iterator(); i.hasNext(); )
            if( !isCompatible( (Type)i.next(), (Type)j.next(), true ) )
              return false;
          return true;
        }
        return false;
        //SF041126]
      //-----------------------------------------------------------
      default: // Other types.
        // True if origins without qualifier are the same.
        return t1.getUnqualifiedType()
            == t2.getUnqualifiedType();
      //-----------------------------------------------------------
    }
  }
  //-------------------------------------------------------------------
  /** compositeType:
   * Create the composite type that is the resultant type of an expression
   * with t1 and t2 as its operand types.
   *
   * @param   t1 Type to be synthesized.
   * @param   t2 Type to be synthesized.
   * @return  Composite type or null if incompatible.
  **/
  Type compositeType(Type t1,Type t2,boolean checkqualifier)
  {
    if( t1.getTypeKind()!=t2.getTypeKind() ) // Type kind differs.
      return null;
    if( checkqualifier )
      if( t1.isConst()   !=t2.isConst()
      ||  t1.isVolatile()!=t2.isVolatile() ) // Type qualifier differs.
        return null;

    Type type = null; // Prepare for resultant type.
    switch( t1.getTypeKind() ) // Branch by type kind.
    {
      //-----------------------------------------------------------
      case Type.KIND_POINTER: // Pointer.
      {
        if( ((PointerType)t1).getPointedType()==symRoot.typeVoid )
          type = t2;
        else if( ((PointerType)t2).getPointedType()==symRoot.typeVoid )
          type = t1;
        else
        {
          Type ptdt = compositeType( // Get the composite type of pointed types.
                        ((PointerType)t1).getPointedType(),
                        ((PointerType)t2).getPointedType(), true );
          if( ptdt==null ) // Cannot get the composite type.
            return null;
          type = sym.pointerType(ptdt); // Create the pointer type pointing the
                                        // composite type of pointed elements.
        }
        break;
      }
      //-----------------------------------------------------------
      case Type.KIND_VECTOR: // Array.
      {
        long l1 = ((VectorType)t1).getElemCount(); // Element count.
        long l2 = ((VectorType)t2).getElemCount();
        if( l1!=l2 ) // Element counts differ.
          if( l1==0 || l2==0 ) // If one of types is incomplete type,
            l1 |= l2;  // then use the element count of the other one.
          else
            return null;
        Type elemt = compositeType( // Get the composite type of array elements.
                       ((VectorType)t1).getElemType(),
                       ((VectorType)t2).getElemType(), true );
        if( elemt==null ) // Cannot get the composite type.
          return null;
        type = sym.vectorType(elemt,l1); // Make array type with element type
                                         // composed from given element types.
        break;
      }
      //-----------------------------------------------------------
      case Type.KIND_SUBP: // Function.
      {
        Type rt = compositeType( // Get composite type of return value types.
          ((SubpType)t1).getReturnType(),
          ((SubpType)t2).getReturnType(), true );
        if( rt==null ) // Cannot get the composite type of return value types.
          return null;

        //SF041126[
        IrList ptl1 = ((SubpType)t1).getParamTypeList();
        IrList ptl2 = ((SubpType)t2).getParamTypeList();
        boolean op1 = ((SubpType)t1).hasOptionalParam();
        boolean op2 = ((SubpType)t2).hasOptionalParam();
        boolean nps1 = ((SubpType)t1).hasNoParamSpec();
        boolean nps2 = ((SubpType)t2).hasNoParamSpec();

        if( nps1 && nps2 )
        {
          type = sym.subpType(rt,ptl1,false,true,null);
        }
        else if( nps1 && !nps2 && !op2 )
        {
          // One type is a prototype declaration with empty identifier list and
          // the other is a function definition/declaration  without optional parameters.
          for( Iterator i=ptl2.iterator(); i.hasNext(); )
          {
            Type pt = ((Type)i.next()).getUnqualifiedType();
            if( pt!=daPromotedType(pt) )
              return null;
          }
          type = sym.subpType(rt,ptl2,op2,nps2,null);
        }
        else if( !nps1 && !op1 && nps2 )
        {
          // One type is a function definition/declaration without optional parameters
          // and the other is a prototype declaration with empty identifier list.
          for( Iterator i=ptl1.iterator(); i.hasNext(); )
          {
            Type pt = ((Type)i.next()).getUnqualifiedType();
            if( pt!=daPromotedType(pt) )
              return null;
          }
          type = sym.subpType(rt,ptl1,op1,nps1,null);
        }
        else if( !nps1 && !nps2 && op1==op2 && ptl1.size()==ptl2.size() )
        {
          // Parameter counts and optional parameter specifications coinside.
          IrList  ptl = hir.irList();
          for( Iterator i=ptl1.iterator(), j=ptl2.iterator(); i.hasNext(); )
          {
            // Make composite type for each parameters.
            Type pt = compositeType( (Type)i.next(), (Type)j.next(), true );
            if( pt==null )
              return null;
            ptl.add(pt);
          }
          type = sym.subpType(rt,ptl,op1,false,null);
        }
        else
        {
          return null;
        }
        //SF041126]
        break;
      }
      //-----------------------------------------------------------
      default: // Other types.
      {
        type = t1.getUnqualifiedType();
        if( type!=t2.getUnqualifiedType() )
          return null;
        break;
      }
      //-----------------------------------------------------------
    }
    if( checkqualifier )
      type = copyQualifier(type,t1);
    return type;
  }
  //-------------------------------------------------------------------
  /**
    Return the default argument promoted type.
    @param   t type.
    @return  default argument promoted type.
  **/
  Type daPromotedType(Type t) //SF041126
  {
    if( isIntegral(t)
    &&  t.getTypeRank()<symRoot.typeInt.getTypeRank()
    &&  t.getTypeKind()!=Type.KIND_ENUM )
      return copyQualifier(symRoot.typeInt,t);
    if( t.getTypeKind()==Type.KIND_FLOAT ) // If float
      return copyQualifier(symRoot.typeDouble,t); // promote to double.
    return t;
  }
  //-------------------------------------------------------------------
  private Type copyQualifier(Type dst,Type src) //SF041126
  {
    // Add in the order of const, volatile, restrict
    if( src.isConst() ) // If with const qualifier,
      dst = dst.makeConstType(); // add const qualifier.
    if( src.isVolatile() ) // If with volatile qualifier,
      dst = dst.makeVolatileType();// add volatile qualifier.
    if( src.isRestrict() ) // If with restrict qualifier,     //##81
      dst = dst.makeRestrictType();// add restrict qualifier. //##81
    return dst;
  }
  //-------------------------------------------------------------------
  /**
   * Get the resultant type of integral promotion.
   * <pre>
   * If the rank of t < the rank of int then {
   *   If (precision of t >= precision of int) and (t is unsigned)
   *   then resultant type is unsigned int
   *   else resultant type is int.
   * }else
   *   do not do integral promotion.
   * </pre>
   * @param   t Type
   * @return  (Promoted) resultant type.
  **/
  Type iPromotedType(Type t) //SF041025
  {
    if( t.getTypeRank()<symRoot.typeInt.getTypeRank() ) // If rank < int
    {
      // then do integral promotion.
      if( t.getSizeValue() >= symRoot.typeInt.getSizeValue()
      &&  t.isUnsigned() ) // If precision >= int and unsigned then
        return symRoot.typeU_Int; // result is unsigned int
      else // otherwise
        return symRoot.typeInt; // resultant type is int.
    }
    return t.getUnqualifiedType(); // No conversion is required. //SF041014
  }
  //-------------------------------------------------------------------
  // type checker
  //-------------------------------------------------------------------
  /** isIntegral
   * Is integral type ?
   *
   * @param   t Type.
   * @return  True if t is integral type (short, int, long, etc.).
  **/
  boolean isIntegral(Type t)
  {
    return (t.getTypeRank()&1)!=0;
  }
  //-------------------------------------------------------------------
  /** isArithmetic
   * Is arithmetic type ?
   *
   * @param   t Type.
   * @return  True if t is arithmetic type.
  **/
  boolean isArithmetic(Type t)
  {
    return t.getTypeRank()>0;
  }
  //-------------------------------------------------------------------
  /** isScalar
   * Is scalar type ?
   *
   * @param   t Type.
   * @return  True if t is scalar type.
  **/
  boolean isScalar(Type t)
  {
    return t.getTypeRank()!=0
        || t.getTypeKind()==Type.KIND_ADDRESS
        || t.getTypeKind()==Type.KIND_OFFSET;
  }
  //-------------------------------------------------------------------
  /** isModifierIncluded
   * Is type modifier included ?
   *
   * @param   t1 Type.
   * @param   t2 Type.
   * @return  True if left side type covers the qualifiers of right side.
  **/
  boolean isModifierIncluded(Type t1,Type t2)
  {
    return ( t1.isConst   () || !t2.isConst   () )
        && ( t1.isVolatile() || !t2.isVolatile() );
  }
  //-------------------------------------------------------------------
  // expression flag setter
  //-------------------------------------------------------------------
  /** setFlagPointerOperation
   * Set FLAG_POINTER_OPERATION flag.
   * If expression has a variable which type is pointer or array,
   * set FLAG_POINTER_OPERATION flag to the variable.
   *
   * @param   e Expression which has a variable.
  **/
  void setFlagPointerOperation(Exp e)
  {
    switch( e.getOperator() )
    {
    case HIR.OP_VAR:
    case HIR.OP_PARAM:
      Sym s = ((SymNode)e).getSymNodeSym();
      if (s != null)
      switch( s.getSymType().getTypeKind() )
      {
      case Type.KIND_POINTER:
      case Type.KIND_VECTOR:
        s.setFlag( Sym.FLAG_POINTER_OPERATION, true );
      }
    }
  }
  //-------------------------------------------------------------------
  /** setFlagAddressTaken
   * Set FLAG_ADDRESS_TAKEN flag.
   * If expression has a variable, set FLAG_ADDRESS_TAKEN flag to the variable.
   * If expression is qualifier or subscript, this method is called recursive.
   *
   * @param   e Expression which has a variable.
  **/
  void setFlagAddressTaken(Exp e)
  {
    switch( e.getOperator() )
    {
    case HIR.OP_CONST:
    //case HIR.OP_SYM:
    case HIR.OP_VAR:
    case HIR.OP_PARAM:
    case HIR.OP_SUBP:
    //case HIR.OP_ELEM:
      ((SymNode)e).getSymNodeSym().setFlag( Sym.FLAG_ADDRESS_TAKEN, true );
      break;
    case HIR.OP_QUAL:
      setFlagAddressTaken( ((QualifiedExpImpl)e).getQualifierExp() );
      break;
    case HIR.OP_SUBS:
      setFlagAddressTaken( ((SubscriptedExp)e).getArrayExp() );
      break;
    }
  }
  //-------------------------------------------------------------------
  /** setFlagValueIsAssigned
   * Set FLAG_VALUE_IS_ASSIGNED flag.
   * If expression has a variable, set FLAG_ADDRESS_TAKEN flag to the variable.
   * If expression is qualifier or subscript, this method is called recursive.
   *
   * @param   e Expression which has a variable.
  **/
  void setFlagValueIsAssigned(Exp e)
  {
    switch( e.getOperator() )
    {
    case HIR.OP_VAR:
    case HIR.OP_PARAM:
      ((SymNode)e).getSymNodeSym().setFlag( Sym.FLAG_VALUE_IS_ASSIGNED, true );
      break;
    case HIR.OP_QUAL:
      setFlagValueIsAssigned( ((QualifiedExpImpl)e).getQualifierExp() );
      break;
    case HIR.OP_SUBS:
      setFlagValueIsAssigned( ((SubscriptedExp)e).getArrayExp() );
      break;
    }
  }
  //-------------------------------------------------------------------
  // expression creator
  //-------------------------------------------------------------------
  /** decayExp
   * Create OP_DECAY (convert array to pointer) expression node.
   *
   * @param   e Expression whose type is array.
   * @return  Decay expression.
  **/
  Exp decayExp(Exp e)
  {
    setFlagAddressTaken(e);
    Exp ne = hir.decayExp(e);
    return ne;
  }
  //-------------------------------------------------------------------
  /** addrExp
   * Create OP_ADDR (= &e) expression node.
   *
   * @param   e Adress taken expression.
   * @return  Taken address.
  **/
  Exp addrExp(Exp e)
  {
    setFlagAddressTaken(e);
    return hir.exp(HIR.OP_ADDR,e);
  }
  //-------------------------------------------------------------------
  /** subsExp
   * Create OP_SUBS (= e1[e2]) expression node.
   *
   * @param   e1 Array expression.
   * @param   e2 Index expression.
   * @return  Subscripted expression.
  **/
  Exp subsExp(Exp e1,Exp e2)
  {
    ////////SF040128[
    //return useSimpleIndex
    //     ? new SubscriptedExpImpl( hirRoot, e1, e2 )
    //     : hir.subscriptedExp( e1, e2 );
    return coins.SourceLanguage.subscriptWithIndex
         ? hir.subscriptedExp( e1, e2 )
         : new SubscriptedExpImpl( hirRoot, e1, e2 );
    ////////SF040128]
  }
  //-------------------------------------------------------------------
  /** newTempVarNode
   * Create temporary variable node, with new variable bymbol.
   *
   * @param   t New symbol type.
   * @return  Created variable node.
  **/
  VarNode newTempVarNode(Type t)
  {
    Var v = symRoot.symTableCurrent.generateVar( t, symRoot.subpCurrent );
    v.setSymType(t);
    return hir.varNode(v);
  }
  //-------------------------------------------------------------------
  /** new0Node
   * Create constant 0 node.
   *
   * @return  Constant 0 node.
  **/
  ConstNode new0Node()
  {
    return hir.constNode(symRoot.intConst0);
  }
  //-------------------------------------------------------------------
  /** new1Node
   * Create constant 1 node.
   *
   * @return  Constant 1 node.
  **/
  ConstNode new1Node()
  {
    return hir.constNode(symRoot.intConst1);
  }
  //-------------------------------------------------------------------
  /** newTrueNode
   * Create true node.
   *
   * @return  True node.
  **/
  Exp newTrueNode()
  {
    return hir.constNode( symRoot.boolConstTrue );
  }
  //-------------------------------------------------------------------
  /** newFalseNode
   * Create false node.
   *
   * @return  False node.
  **/
  Exp newFalseNode()
  {
    return hir.constNode( symRoot.boolConstFalse );
  }
  //-------------------------------------------------------------------
  // statement creator
  //-------------------------------------------------------------------
  /** newBlockStmt
   * Create block statement with statement stmt.
   *
   * @param   stmt Stmt
   * @return  BlockStmt
  **/
  BlockStmt newBlockStmt(Stmt stmt)
  {
    BlockStmt blockstmt = hir.blockStmt(stmt);
    return blockstmt;
  }
  //-------------------------------------------------------------------
  /** newIfStmt
   * Create if statement, and set position.
   *
   * @param   condexp Condition expression.
   * @param   thenstmt Then statement.
   * @param   elsestmt Else statement.
   * @return  IfStmt
  **/
  IfStmt newIfStmt(Exp condexp,Stmt thenstmt,Stmt elsestmt)
  {
    IfStmt ifstmt = hir.ifStmt(condexp,thenstmt,elsestmt);
    ifstmt.setFileName(nowFile);
    ifstmt.setLineNumber(nowLine);
    return ifstmt;
  }
  //-------------------------------------------------------------------
  /** newExpStmt
   * Create expression statement, and set position.
   *
   * @param   exp Exp
   * @return  ExpStmt
  **/
  ExpStmt newExpStmt(Exp exp)
  {
    ExpStmt expstmt = hir.expStmt(exp);
    expstmt.setFileName(nowFile);
    expstmt.setLineNumber(nowLine);
    return expstmt;
  }
  //-------------------------------------------------------------------
  /** newAssignStmt
   * create assign statement.
   *
   * @param   e1 Exp
   * @param   e2 Exp
   * @return  AssignStmt
  **/
  AssignStmt newAssignStmt(Exp e1,Exp e2)
  {
    setFlagValueIsAssigned(e1);
    if( e1.getType().getUnqualifiedType()!=e2.getType().getUnqualifiedType()
    &&  e1.getType().getTypeKind()!=Type.KIND_VECTOR ) //SF041004
      e2 = hir.convExp( e1.getType().getUnqualifiedType(), e2 ); //SF041004
    AssignStmt assignstmt = hir.assignStmt(e1,e2);
    assignstmt.setFileName(nowFile);
    assignstmt.setLineNumber(nowLine);
    return assignstmt;
  }
  //-------------------------------------------------------------------
  /** newLabeledStmt
   * create labeled statement which has statement stmt.
   *
   * @param   stmt Stmt
   * @return  LabeledStmt
  **/
  LabeledStmt newLabeledStmt(Stmt stmt)
  {
    LabeledStmt labeledstmt = hir.labeledStmt(createLabel(),stmt);
    labeledstmt.setFileName(nowFile);
    labeledstmt.setLineNumber(nowLine);
    return labeledstmt;
  }
  //-------------------------------------------------------------------
  // Node which can be lvalue: [] . -> * VarNode
  //-------------------------------------------------------------------
  ///**
  // * returns whether the expression is modifiable lvalue.
  // * @param  e  expression
  // * @return true if the expression is modifiable lvalue.
  //**/
  //static boolean isModifiableLvalue(Exp e) //SF050215
  //{
  //  if( e.getType().getTypeKind()==Type.KIND_VECTOR
  //  ||  e.getType().getSizeExp()==null
  //  ||  e.getType().isConst() )
  //    return false;
  //  return true;
  //}
  //-------------------------------------------------------------------
  ///**
  // * returns whether the expression is address acquirable lvalue.
  // * @param  e  expression
  // * @return true if the expression is address acquirable lvalue.
  //**/
  //static boolean isAddressAcquirableLvalue(Exp e) //SF050215
  //{
  //  if( e.getOperator()==HIR.OP_QUAL
  //  ||  e.getOperator()==HIR.OP_ARROW )
  //    if( e.getExp2().getElem().isBitField() )
  //      return false;
  //  return isAddressAcquirableObject(e);
  //}
  //-------------------------------------------------------------------
  // return true if the object where the object indicated by e is included
  // is not specified for register.
  //private static boolean isAddressAcquirableObject(Exp e) //SF050215
  //{
  //  switch( e.getOperator() )
  //  {
  //  case HIR.OP_CONST:
  //    return e.getConstSym().getSymKind()==Sym.KIND_STRING_CONST;
  //  case HIR.OP_VAR:
  //    return e.getVar().getStorageClass()!=Var.VAR_REGISTER;
  //  case HIR.OP_SUBS:
  //  case HIR.OP_QUAL:
  //    return isAddressAcquirableObject(e.getExp1());
  //  case HIR.OP_SUBP:
  //  case HIR.OP_ARROW:
  //  case HIR.OP_CONTENTS:
  //    return true;
  //  }
  //  return false;
  //}
  //-------------------------------------------------------------------
}
