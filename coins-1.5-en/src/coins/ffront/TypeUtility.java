/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

import java.util.Iterator;

import coins.HirRoot;
import coins.SymRoot;
import coins.ir.IrList;
import coins.ir.hir.Exp;
import coins.ir.hir.HIR;
import coins.sym.Elem;
import coins.sym.PointerType;
import coins.sym.StructType;
import coins.sym.Sym;
import coins.sym.SymTable;
import coins.sym.Type;
import coins.sym.UnionType;
import coins.sym.Var;
import coins.sym.VectorType;

public class TypeUtility{
  FirToHir fHir;
  HirUtility fHirUtil;

  Sym sym;                 // an instance to generate Sym objects
  HIR hir;                 // an instance to generate HIR objects
  SymRoot symRoot;  // root of symbol information
  HirRoot hirRoot;

  public TypeUtility(FirToHir fth){
    fHir = fth;
    sym  = fth.getSym();
    hir  = fth.getHir();
    symRoot = fHir.getSymRoot();
    hirRoot = fHir.getHirRoot();
  }

  Exp getDimSizeParamExp(Node n, DeclManager declMgr){
    String punit_name = declMgr.getProgramUnitName();
    String var_name = fHir.getExecStmtManager().getTempName(punit_name + "_dim_size_var"); 
    Var var = (Var)declMgr.searchOrAddVar(var_name, getIntType());
    Exp exp = n.makeExp();
    declMgr.setInitialValue(var, fHirUtil.castToInteger(exp));
    return hir.varNode(var);
  }

//  Exp getDimSizeParamExp(String name, Token tk, DeclManager declMgr){
//    String sized_var_name = ("_dim_size_param__" + name +
//                             declMgr.getProgramUnitName()).intern();
//    
//    Var var = (Var)declMgr.search(sized_var_name);
//    if(var == null){
//      Param param = declMgr.defineParam(name);
//      var = declMgr.searchOrAddVar(sized_var_name, getIntType());
//      declMgr.setInitialValue(var, fHirUtil.castToInteger(tk.makeExp()));
//    }
//    else{
//      dp("found: " + var);
//    }
//    return hir.varNode(var);
//  }
  
  /**
    make Vector type.

    ary(3,4,5) => a[5][4][3]

    An array dimension is limited to 7.

    @param type  type of this variable
    @param dims  dimension information
   */
  Type getArrayType(Type array_type, FirList dims, DeclManager declMgr){
    int dim_lows[] = {1,1,1,1,1,1,1,};
    int dim_upps[] = {0,0,0,0,0,0,0,};

    Exp dim_lp[] = {null, null, null, null, null, null, null,};
    Exp dim_up[] = {null, null, null, null, null, null, null,};

    Type type = null;
    dp(" array type: " + array_type);
    int dimnum = 0;
    Iterator dit = dims.iterator();
    while(dit.hasNext()){
      Pair d  = (Pair)dit.next();
      Node tl = d.getLeft();
      Node tr = d.getRight();

      if(tl != null){
        if(tl instanceof HasConstValue &&
           ((HasConstValue)tl).getConstValue() != null){
          dim_lows[dimnum] = ((HasConstValue)tl).getConstValue().intValue();
        }
        else if(tl != null){
          // if size specifier is variable, it's adjust array.
          Exp e = getDimSizeParamExp(tl, declMgr);
          dim_lp[dimnum] = e;
        }
        else{
          fHir.printMsgFatal("unkown array lower index type");
        }
      }

      if(tr instanceof HasConstValue &&
         ((HasConstValue)tr).getConstValue() != null){
        // constant
        dim_upps[dimnum] = ((HasConstValue)tr).getConstValue().intValue();
      }
      else if(tr != null){
        // adjustable array
        dim_up[dimnum] = getDimSizeParamExp(tr, declMgr);
      }
      else{
        // assumed size array
        dp("[TYPE] assumed-size array");
        dim_up[dimnum] = null;
      }
      dimnum ++;
    }
    int i = 0;
    // first dimention
    type = makeVectorType(array_type, i, dim_lp, dim_up, dim_lows, dim_upps);

    while(++i < dimnum){
      type = makeVectorType(type, i, dim_lp, dim_up, dim_lows, dim_upps);
    }
    dp("[TYPE getArrayType]: " + type);
    return type;
  }

  // helper function
  Type makeVectorType(Type type, int dimnum,
                      Exp dim_lp[], Exp dim_up[],
                      int dim_lows[], int dim_upps[]){

    if(dim_lp[dimnum] != null || dim_up[dimnum] != null){
      Exp el = dim_lp[dimnum];
      Exp eu = dim_up[dimnum];
      Exp ec = null;
      int il = -1;

      if(el == null){
        il = dim_lows[dimnum] - 1;
        el = fHirUtil.makeIntConstNode(dim_lows[dimnum]);
      }
      if(eu == null){
        if(dim_upps[dimnum] == 0){
          // assumed-size array
        }
        else{
          eu = fHirUtil.makeIntConstNode(dim_upps[dimnum]);
        }
      }
      if(il == 0){
        ec = eu;
      }
      else if(il != -1){
        ec = hir.exp(HIR.OP_SUB, eu, fHirUtil.makeIntConstNode(il));
      }
      else{
        // upper - lower + 1
        ec = hir.exp(HIR.OP_ADD, hir.exp(HIR.OP_SUB, eu, el),
                     fHirUtil.makeConstInt1Node());
      }
      type = sym.vectorType(null, type, ec, el);
    }
    else{
      type = sym.vectorType(null, type,
                            dim_upps[dimnum]-dim_lows[dimnum] + 1,
                            dim_lows[dimnum]);
    }
    return type;
  }

  /*
  FirList getDimType(String ident){
    Iterator it = f7Sym.typedDeclList.iterator();
    while(it.hasNext()){
      Pair     oneDeclStmt = (Pair)it.next(); // one decl statement
      Type     declType    = type((Pair)oneDeclStmt.getLeft()); //declared type
      Iterator varIt       = ((FirList)oneDeclStmt.getRight()).iterator();
      while(varIt.hasNext()){
        Pair   oneDecl = (Pair)varIt.next();  // declared variable
        if(((Token)oneDecl.getLeft()).getLexem() == ident){
          return (FirList)oneDecl.getRight();
        }
      }
    }
    return null;
  }
   */


  /** Make Sym Type from F77 Type Pair
   *  F77 Type is Pair(type name, optional length spec)
   * @param pTypeName
   * @return sym-type.
   */
  Type getType(Pair pTypeName){
    Token typeName = (Token)pTypeName.getLeft();
    int  length    = 0;
    Node optLength = pTypeName.getRight();
    if (optLength != null){
      if(optLength instanceof HasConstValue){
        length = ((HasConstValue)optLength).getConstValue().intValue();
      }
    }
    else if(typeName.getKind() == Parser.CHARACTER){
      length = 1;
    }
    return getType(typeName.getKind(), length);
  }

  /** Make Sym Type from F77 Type kind and length
   *
   * @param typeKind
   * @param length
   * @return the Sym type.
   */
  Type getType(int typeKind, int length){

    switch (typeKind){
    case Parser.INTEGER:
      if (length == 8) return symRoot.typeLong;
      else             return symRoot.typeInt;

    case Parser.REAL:
      if (length == 8) return symRoot.typeDouble;
      else             return symRoot.typeFloat;

    case Parser.DOUBLE_PREC: 
      return symRoot.typeDouble;
    
    case Parser.CHARACTER:
      return charArray(length);
    
    case Parser.LOGICAL:
      return symRoot.typeBool; //LogicalType.type;
    
    case Parser.COMPLEX:
      if (length == 16) return getComplexDoubleStructType();
      else              return getComplexStructType();
    
    default: return null;
    }
  }

  /** Make Sym Type from F77 Type kind
   *
   * @param typeKind
   * @return the type.
   */
  Type getType(int typeKind){
    if(typeKind == Parser.CHARACTER){
      return getType(typeKind, 1);
    }
    else{
      return getType(typeKind, 0);
    }
  }

  Type charArray(int length){
    return sym.vectorType(
      symRoot.typeChar,
      fHirUtil.makeIntConstNode(length));
  }


  private Type entryType;
  public Type getEntryType(){
    if (entryType == null){
      entryType = new EntryType(fHir);
    }
    return entryType;
  }

  private StructType complexStructType;
  private StructType complexDoubleStructType;

  boolean isComplexType(Type type){
    if(getComplexStructType()       == type ||
       getComplexDoubleStructType() == type){
      return true;
    }
    else{
      return false;
    }
  }

  boolean isDoubleComplexType(Type type){
    if(getComplexDoubleStructType() == type){
      return true;
    }
    else{
      return false;
    }
  }

  /**
   * Return Complex type as a StructType
   * <pre>
     typedef struct{
        float _real;
        float _imag;
     } _complex_struct;
     </pre>
   *
   * @return complexStructType
   */
  public StructType getComplexStructType(){
    if (complexStructType == null){
      SymTable c_table = symRoot.symTableCurrent;
      symRoot.symTableCurrent = symRoot.symTableRoot;
      // into the global scope
      {
        Type realType = getRealType();
        IrList elemList = hir.irList();

        pushSymTable(null);
        elemList.add(sym.defineElem("_real", realType));
        elemList.add(sym.defineElem("_imag", realType));
        popSymTable();


        // complexStructType = sym.structType("_complex_struct", elemList, null);
        Sym tag = symRoot.symTableCurrent.generateTag("_complex_struct");
        complexStructType = sym.structType(elemList, tag);
        tag.setSymType(complexStructType);
        tag.setFlag(Sym.FLAG_COMPLEX_STRUCT, true);
      }
      symRoot.symTableCurrent = c_table;
    }
    return complexStructType;
  }
  /**
   * Return Double Complex type as a StructType
   * <pre>
     typedef struct{
        double _real;
        double _imag;
     } _complex_struct;
     </pre>
   *
   * @return complexStructType
   */
  public StructType getComplexDoubleStructType(){
    if (complexDoubleStructType == null){
      SymTable c_table = symRoot.symTableCurrent;
      symRoot.symTableCurrent = symRoot.symTableRoot;
      // into the global scope
      {
        Type doubleType   = getDoubleType();
        IrList elemList = hir.irList();

        pushSymTable(null);
        elemList.add(sym.defineElem("_real", doubleType));
        elemList.add(sym.defineElem("_imag", doubleType));
        popSymTable();

        Sym tag = symRoot.symTableCurrent.generateTag("_complex_double_struct");
        complexDoubleStructType = sym.structType(elemList, tag);
        tag.setSymType(complexDoubleStructType);
        tag.setFlag(Sym.FLAG_COMPLEX_STRUCT, true);
      }
      symRoot.symTableCurrent = c_table;
    }
    return complexDoubleStructType;
  }

  // complex element accessor
  public Elem getRealPart(){
    return (Elem)complexStructType.getElemList().get(0);
  }
  public Elem getImagPart(){
    return (Elem)complexStructType.getElemList().get(1);
  }
  // double complex element accessor
  public Elem getDoubleRealPart(){
    return (Elem)complexDoubleStructType.getElemList().get(0);
  }
  public Elem getDoubleImagPart(){
    return (Elem)complexDoubleStructType.getElemList().get(1);
  }
  public Elem getRealPart(StructType type){
    return (Elem)type.getElemList().get(0);
  }
  public Elem getImagPart(StructType type){
    return (Elem)type.getElemList().get(1);
  }


  /**
    make global struct.
    names and types must be same length.
    ex: "stname", [a, b, c], [int, float, int[10]] is given, this method
        define following struct type in global scope.
    <pre>
    struct stname{
      int a;
      float b;
      int c[10];
    };
    </pre>
   */
  public Type getGlobalStructType(String type_name, String names[], Type types[]){
    Type type = (Type)symRoot.symTableRoot.searchLocal(type_name, Sym.KIND_TYPE);
    if(type == null){
      SymTable c_table = symRoot.symTableCurrent;
      symRoot.symTableCurrent = symRoot.symTableRoot;
      {
        IrList el = hir.irList();
        int sz = names.length;

        pushSymTable(null);

        for(int i=0;i<sz;i++){
          el.add(sym.defineElem(names[i], types[i]));
        }
	popSymTable();

        Sym tag = symRoot.symTableCurrent.generateTag(type_name);
        type = sym.structType(el, tag);
        tag.setSymType(type);

      }
      symRoot.symTableCurrent = c_table;
    }
    return type;
  }


  /**
    search "ident" element from Struct or Union type.
   */
  Elem searchElem(String ident, Type type){
    IrList list;
    if(type instanceof StructType){
      list = ((StructType)type).getElemList();
    }
    else if(type instanceof UnionType){
      list = ((UnionType)type).getElemList();
    }
    else{
      return null;
    }

    Iterator it = list.iterator();
    while(it.hasNext()){
      Elem elem = (Elem)it.next();
      if(elem.getName() == ident){
        return elem;
      }
    }
    return null;
  }

  /**
    Get vector base type
    if base type is char, return char vector type.
   */
  Type getVectorBaseType(VectorType vt){
    while(true){
      if(vt.getElemType() instanceof VectorType){
        vt = (VectorType)vt.getElemType();
      }
      else{
        break;
      }
    }
    Type etype = vt.getElemType();
    if(etype == getCharType()){
      return vt;
    }
    else{
      return etype;
    }
  }

  /**
	check if type is fortran character type, in other words, coins' Vector<char> ?
   */
  boolean isFortranCharacterType(Type type){
    if(type instanceof VectorType){
      if(((VectorType)type).getElemType() == getCharType()){
        return true;
      }
    }
    else if(type instanceof PointerType){
      if(((PointerType)type).getPointedType() == getCharType()){
        return true;
      }
    }
    return false;
  }
  /**
    check if type is vector of fortran character type.
    in other words, coins' Vector*<Vector<char>> ?
   */
  boolean isFortranCharacterVectorType(VectorType type){
    return isFortranCharacterType(getVectorBaseType(type));
  }

  /*
   * cast fortran character.
   */
  FortranCharacterExp castFortranCharacterExp(Exp exp){
    if(exp instanceof FortranCharacterExp){
      return (FortranCharacterExp)exp;
    }
    else{
      fHir.printMsgFatal("Should be Character type");
    }
    return null;
  }

  /**
   */
  Exp getFortranCharacterLengthExp(Type type, Node hint){
    return getFortranCharacterLengthExp(type, ((Token)hint).getLexem());
  }

  Exp getFortranCharacterLengthExp(Type type, String name){
    if(isFortranCharacterType(type) == false){
      fHir.printMsgFatal("[BUG] TypeMgr#getCharacterLengthExp: " + type);
    }
    Exp e = ((VectorType)type).getElemCountExp();
    dp("e : " + e);
    if(((VectorType)type).getElemCount() == 0){
      DeclManager fDeclMgr = fHir.getDeclManager();
      String id = fDeclMgr.characterLengthVarName(name);
      dp("getFortranCharacterLengthExp - hint: " + name + ", " + id);
      dp("** : " + fDeclMgr.search(id));
      return hir.varNode((coins.sym.Var)fDeclMgr.search(id));
    }
    return e;
  }

  Exp getFortranCharacterVectorLengthExp(VectorType type, String name){
    return getFortranCharacterLengthExp(getVectorBaseType(type), name);
  }


  // Symbol table operation
  public SymTable pushSymTable(Sym pSym){
    return symRoot.symTableCurrent.pushSymTable(pSym);
  }
  public void popSymTable(){
    symRoot.symTableCurrent.popSymTable();
    symRoot.symTableCurrentSubp = fHir.getDeclManager().fSymTable;
  }

  // get specified type
  public Type getRealType(){
    return symRoot.typeFloat;
  }
  public Type getDoubleType(){
    return symRoot.typeDouble;
  }
  public Type getIntType(){
    return symRoot.typeInt;
  }
  public Type getBoolType(){
    return symRoot.typeBool;
  }
  public Type getVoidType(){
    return symRoot.typeVoid;
  }
  public Type getCharType(){
    return symRoot.typeChar;
  }

  ////
  void dp(String msg){
    fHir.dp(msg);
  }
}



