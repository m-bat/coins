/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.casttohir;

import java.util.HashMap;
import java.util.Iterator; //SF050215

import coins.ast.Expr;
import coins.ast.TypeId;
import coins.ir.IrList;
import coins.ir.hir.HIR;
import coins.sym.Elem; //SF050215
import coins.sym.StructType;
import coins.sym.Subp;
import coins.sym.SubpType;
import coins.sym.Sym;
import coins.sym.SymImpl;
import coins.sym.SymTable;
import coins.sym.Type;
import coins.sym.UnionType;
import coins.sym.Var;
import coins.sym.VectorType;

/**
 * Create symbol and register it to the symbol table.
 *
 * @author  Shuichi Fukuda
**/
public class ToHirSym implements TypeId
{
  private byte[]  byteArray; //          ASTree type information byte array
  private int     byteIndex; // index of ASTree type information byte array
  private HashMap tagMap = new HashMap();  // AST tag->HIR type object

  private final ToHir toHir;
  private final HIR   hir; // HIR instance (used to create HIR objects)
  private final Sym   sym; // Sym instance (used to create Sym objects)

  private static final String CONST_SUFFIX = "#const";
  private static final String VOLATILE_SUFFIX = "#volatile";
  private static final String RESTRICT_SUFFIX = "#restrict"; //##81
  protected int fDbgLevel; //##67

  //-------------------------------------------------------------------
  /**
   * Constructor.
   *
   * @param  tohir Offers cooperation with the object of other packages.
  **/
  ToHirSym(ToHir tohir)
  {
    toHir = tohir;
    hir   = toHir.hirRoot.hir;
    sym   = toHir.hirRoot.sym;
    fDbgLevel = toHir.ioRoot.dbgToHir.getLevel(); //##67
    message(1,"ToHirSym\n"); //##71
  }
  //-------------------------------------------------------------------
  /**
   * Output debug message.
   *
   * @param  level Debug level.
   * @param  mes Debug message.
  **/
  private void message(int level,String mes)
  {
    toHir.debug.print(level,"Sy",mes);
  }
  //-------------------------------------------------------------------
  // type converter
  //-------------------------------------------------------------------
  /**
   * make qualified types of t (=struct or union which has tag of tagname).
   * The qualified type of t might be defined while t is an incomplete type.
   * To define the qualified type of t as a complete type again
   * when t became a complete type, this method is called.
   * @param  t        complete type (struct or union)
   * @param  tagname  tag name of the complete type
  **/
  void makeQualifiedTypes(Type t,String tagname) //SF050215
  {
    Type ct = null;
    Type vt = null;
    Type rt = null; //##81
    String newtagname;

    // Keep the order of const, volatile, restrict. //##81
    newtagname = (tagname+ToHirSym.CONST_SUFFIX).intern();
    if (fDbgLevel > 0) //##77
      message(4,"makeQualifiedTypes tagName="+newtagname
              + " based on " + t.toStringShort()); //##77
    if( toHir.symRoot.symTableCurrent.search(newtagname,Sym.KIND_TAG)!=null )
      ct = makeQualifiedType(t,ToHirSym.CONST_SUFFIX);

    newtagname = (tagname+ToHirSym.VOLATILE_SUFFIX).intern();
    if( toHir.symRoot.symTableCurrent.search(newtagname,Sym.KIND_TAG)!=null )
      vt = makeQualifiedType(t,ToHirSym.VOLATILE_SUFFIX);

//##81 BEGIN
    newtagname = (tagname+ToHirSym.RESTRICT_SUFFIX).intern();
    if( toHir.symRoot.symTableCurrent.search(newtagname,Sym.KIND_TAG)!=null )
      rt = makeQualifiedType(t,ToHirSym.RESTRICT_SUFFIX);
//##81 END

    newtagname =
      (tagname+ToHirSym.CONST_SUFFIX+ToHirSym.VOLATILE_SUFFIX).intern();
    if( toHir.symRoot.symTableCurrent.search(newtagname,Sym.KIND_TAG)!=null )
      if( ct!=null )
        makeQualifiedType(ct,ToHirSym.VOLATILE_SUFFIX);
      else if( vt!=null )
        makeQualifiedType(vt,ToHirSym.CONST_SUFFIX);
      else
        toHir.fatal("makeQualifiedTypes: "+newtagname);
    //##81 BEGIN
    newtagname =
      (tagname+ToHirSym.CONST_SUFFIX+ToHirSym.RESTRICT_SUFFIX).intern();
    if( toHir.symRoot.symTableCurrent.search(newtagname,Sym.KIND_TAG)!=null ) {
      if (ct != null)
        makeQualifiedType(ct, ToHirSym.RESTRICT_SUFFIX);
      else if (rt != null)
        makeQualifiedType(vt, ToHirSym.CONST_SUFFIX);
      else
        toHir.fatal("makeQualifiedTypes: " + newtagname);
    }
    newtagname =
      (tagname+ToHirSym.VOLATILE_SUFFIX+ToHirSym.RESTRICT_SUFFIX).intern();
    if( toHir.symRoot.symTableCurrent.search(newtagname,Sym.KIND_TAG)!=null ) {
      if (vt != null)
        makeQualifiedType(ct, ToHirSym.RESTRICT_SUFFIX);
      else if (rt != null)
        makeQualifiedType(vt, ToHirSym.VOLATILE_SUFFIX);
      else
        toHir.fatal("makeQualifiedTypes: " + newtagname);
    }
    newtagname =
      (tagname+ToHirSym.CONST_SUFFIX+ToHirSym.VOLATILE_SUFFIX
           + ToHirSym.RESTRICT_SUFFIX).intern();
    if( toHir.symRoot.symTableCurrent.search(newtagname,Sym.KIND_TAG)!=null ) {
      if( ct!=null ) {
        if ( vt != null)
          makeQualifiedType(ct,ToHirSym.RESTRICT_SUFFIX); // ct!=null vt!=null
        else if (rt != null) {
          makeQualifiedType(ct,ToHirSym.VOLATILE_SUFFIX); // ct!=null vt==null
        }
      }else if( vt!=null ) { // ct==null vt!=null
        makeQualifiedType(ct,ToHirSym.CONST_SUFFIX);
        if ( rt == null)     // ct==null vt!=null rt==null
          makeQualifiedType(ct,ToHirSym.RESTRICT_SUFFIX);
        else  ; // ct==null vt != null rt!=null
      }else if( rt!=null ) { // ct==null rt!=null
        makeQualifiedType(ct,ToHirSym.CONST_SUFFIX);
        if ( vt == null)     // ct==null vt==null rt!=null
          makeQualifiedType(ct,ToHirSym.VOLATILE_SUFFIX);
        else  ; // ct==null vt != null rt!=null
      }else
        toHir.fatal("makeQualifiedTypes: "+newtagname);
    }
    //##81 NED
  }
  //-------------------------------------------------------------------
  /**
   * Convert type.
   * Created type symbol is added appropriate symbol table.
   *
   * @param   b AST type information byte array.
   * @return  HIR type.
  **/
  //##83 public Type convertType(byte[] b)
  public Type convertType(byte[] b, boolean pLocal) //##83
  {
    byteArray = b;
    byteIndex = 0;
    if (fDbgLevel > 3) //##67
      message(6,"convertType  TYPE="+new String(b));
    return cnvType(pLocal);
    //Type t = cnvType();
    //message(6,"TYPESIZE="+t.getSizeValue());
    //return t;
  }
  //-------------------------------------------------------------------
  /**
   * Convert type.
   *
   * @return  HIR type.
  **/
  //##83 private Type cnvType()
  private Type cnvType( boolean pLocal )
  {
    switch(byteArray[byteIndex++])
    {
    //SF050215[
    case CONST_T: return makeQualifiedType( cnvType(pLocal), CONST_SUFFIX );
    case VOLATILE_T: return makeQualifiedType( cnvType(pLocal), VOLATILE_SUFFIX );
    //SF050215]
    case RESTRICT_T: return makeQualifiedType( cnvType(pLocal), RESTRICT_SUFFIX ); //##81

    case SIGNED_T  : return cnvSigned();
    case UNSIGNED_T: return cnvUnsigned();

    case STRUCT_BEGIN : return cnvStruct(pLocal); //##83
    case STRUCT_END   : toHir.fatal("cnvTypeStruct");
    case UNION_BEGIN  : return cnvUnion(pLocal); //##83
    case UNION_END    : toHir.fatal("cnvTypeUnion");
    case ENUM_BEGIN   : return cnvEnum(pLocal);
    case ENUM_END     : toHir.fatal("cnvTypeEnum");
    case POINTER_T    : return sym.pointerType(cnvType(pLocal));
    case ARRAY_T      : return cnvArray(pLocal); //##83
    case FUNCTION_T   : return cnvFunction(pLocal); //##83
    case ELLIPSIS_T   : return null; // argument elipsis  =  "..."
    case CHAR_T       : return toHir.symRoot.getCharType(); //SF041111
    case SHORT_T      : return toHir.symRoot.typeShort;
    case INT_T        : return toHir.symRoot.typeInt;
    case LONG_T       : return toHir.symRoot.typeLong;
    case LONG_LONG_T  : return toHir.symRoot.typeLongLong;
    case FLOAT_T      : return toHir.symRoot.typeFloat;
    case DOUBLE_T     : return toHir.symRoot.typeDouble;
    case LONG_DOUBLE_T: return toHir.symRoot.typeLongDouble;
    case VOID_T       : return toHir.symRoot.typeVoid;
    default           : toHir.fatal("cnvType: "+(char)byteArray[byteIndex-1]+
                                    " of "+new String(byteArray));
                        return toHir.symRoot.typeVoid;
    }
  }
  //-------------------------------------------------------------------
  private Type makeQualifiedType(Type t,String suffix) //SF050215
  {
    if (fDbgLevel > 0) //##77
      message(6,"makeQualifiedType suffix="+suffix
              + " based on " + t.toStringShort()); //##77
    switch( t.getTypeKind() )
    {
    case Type.KIND_STRUCT:
    {
      String tagname = addSuffix( ((StructType)t).getTag().getName(), suffix );
      SymTable tgt = toHir.symRoot.symTableCurrent.searchTableHaving(t);
      Sym tag = tgt.search(tagname,Sym.KIND_TAG);
      if( tag==null )
        tag = tgt.generateTag(tagname); // define tag of qualified struct

      StructType strct = (StructType)tag.getSymType();
      if( strct==null )
      {
        strct = sym.structType(null,tag); // define qualified struct
        strct.setOrigin(t);
        tag.setSymType(strct);
      }
      if (fDbgLevel > 0) //##77
        message(6," elemSize "+strct.getElemList().size()
                + " elemListSize " + t.getElemList().size()); //##77
      //##77 if( strct.getElemList().size()==0 &&  t.getElemList().size()>0 )
      if( strct.getElemList().size() < t.getElemList().size() ) //##77
      {
        SymTable bak = toHir.symRoot.symTableCurrent; // push symbol table
        toHir.symRoot.symTableCurrent = tgt;
        toHir.symRoot.symTableCurrent.pushSymTable(strct);
        strct.getElemList().clear(); //##77
        for( Iterator i=t.getElemList().iterator(); // define qualified members
             i.hasNext(); )
        {
          Elem oldelem = (Elem)i.next();
          Type type    = makeQualifiedType( oldelem.getSymType(), suffix );
          Elem elem    = sym.defineElem( oldelem.getName(), type );
          if( oldelem.isBitField() )
            elem.setBitFieldSize( oldelem.getBitSize() );
          elem.setDefinedFile( oldelem.getDefinedFile() );
          elem.setDefinedLine( oldelem.getDefinedLine() );
          strct.addElem(elem);
        }
        toHir.symRoot.symTableCurrent.popSymTable(); // pop symbol table
        toHir.symRoot.symTableCurrent = bak;

        //SymTable table = symRoot.symTableCurrent; //?
        //symRoot.symTableCurrent = toHir.getSubpTable(); //?
        strct.finishStructType(true);
        //symRoot.symTableCurrent = table; //?
      }
      return strct;
    }
    case Type.KIND_UNION:
    {
      String tagname = addSuffix( ((UnionType)t).getTag().getName(), suffix );
      SymTable tgt = toHir.symRoot.symTableCurrent.searchTableHaving(t);
      Sym tag = tgt.search(tagname,Sym.KIND_TAG);
      if( tag==null )
        tag = tgt.generateTag(tagname); // define tag of qualified union

      UnionType unon = (UnionType)tag.getSymType();
      if( unon==null )
      {
        unon = sym.unionType(null,tag); // define qualified union
        unon.setOrigin(t);
        tag.setSymType(unon);
      }
      if (fDbgLevel > 0) //##77
        message(6," elemSize "+unon.getElemList().size()
                + " elemListSize " + t.getElemList().size()); //##77
      //##77 if( unon.getElemList().size()==0&& t.getElemList().size()>0 )
      if( unon.getElemList().size() < t.getElemList().size() ) //##77
      {
        SymTable bak = toHir.symRoot.symTableCurrent;
        toHir.symRoot.symTableCurrent = tgt;
        toHir.symRoot.symTableCurrent.pushSymTable(unon);
        unon.getElemList().clear(); //##77
        for( Iterator i=t.getElemList().iterator(); // define qualified members
             i.hasNext(); )
        {
          Elem oldelem = (Elem)i.next();
          Type type    = makeQualifiedType( oldelem.getSymType(), suffix );
          Elem elem    = sym.defineElem( oldelem.getName(), type );
          elem.setDefinedFile( oldelem.getDefinedFile() );
          elem.setDefinedLine( oldelem.getDefinedLine() );
          unon.addElem(elem);
        }
        toHir.symRoot.symTableCurrent.popSymTable();
        toHir.symRoot.symTableCurrent = bak;

        //SymTable table = symRoot.symTableCurrent; //?
        //symRoot.symTableCurrent = toHir.getSubpTable(); //?
        unon.finishUnionType(true);
        //symRoot.symTableCurrent = table; //?
      }
      return unon;
    }
    case Type.KIND_SUBP:
      toHir.error("ISO C forbids qualified function types");
      return t;

    case Type.KIND_VECTOR:
      t = sym.vectorType( // create vector which has const elements
        makeQualifiedType( ((VectorType)t).getElemType(), suffix ),
        ((VectorType)t).getElemCount() );
      // and create const vector as follows

    default:
      return suffix==CONST_SUFFIX ? t.makeConstType()
           : suffix==VOLATILE_SUFFIX ? t.makeVolatileType()
           : suffix==RESTRICT_SUFFIX ? t.makeRestrictType() //##81
           : t;
    }
  }
  //-------------------------------------------------------------------
  private String addSuffix(String name,String suffix) //SF050215
  {
    if( name.indexOf(suffix)>=0 )
      return name;

    int index;
    /* //##81
    if( suffix==CONST_SUFFIX ) {
      if( (index=name.indexOf(VOLATILE_SUFFIX))>=0 ) {
        name = name.substring(0,index) + CONST_SUFFIX + VOLATILE_SUFFIX;
      }else
        name += CONST_SUFFIX;
    }else if( suffix==VOLATILE_SUFFIX ) {
      name += VOLATILE_SUFFIX;
    }
    */ //##81
   //##81 BEGIN
   int index1;
   if( suffix==CONST_SUFFIX ) {
     if( (index=name.indexOf(VOLATILE_SUFFIX))>=0 ) {
       if( (index1=name.indexOf(RESTRICT_SUFFIX))>=0 ) {
         name = name.substring(0,index) + CONST_SUFFIX + VOLATILE_SUFFIX
                                        + RESTRICT_SUFFIX;
       }else
         name = name.substring(0,index) + CONST_SUFFIX + VOLATILE_SUFFIX;
     }else if( (index1=name.indexOf(RESTRICT_SUFFIX))>=0 ) {
         name = name.substring(0,index) + CONST_SUFFIX + RESTRICT_SUFFIX;
     }else
       name += CONST_SUFFIX;
   }else if( suffix==VOLATILE_SUFFIX ) {
     if( (index1=name.indexOf(RESTRICT_SUFFIX))>=0 ) {
       name = name.substring(0,index1)+ VOLATILE_SUFFIX
                                      + RESTRICT_SUFFIX;
     }else
       name += VOLATILE_SUFFIX;
   }else if( suffix==RESTRICT_SUFFIX ) {
     name += RESTRICT_SUFFIX;
   }
   //##81 END
    return name.intern();
  }
  //-------------------------------------------------------------------
  /**
   * Convert signed type.
   *
   * @return  HIR type
  **/
  private Type cnvSigned()
  {
    switch(byteArray[byteIndex++])
    {
    case CHAR_T     : return toHir.symRoot.typeChar;
    case SHORT_T    : return toHir.symRoot.typeShort;
    case INT_T      : return toHir.symRoot.typeInt;
    case LONG_T     : return toHir.symRoot.typeLong;
    case LONG_LONG_T: return toHir.symRoot.typeLongLong;
    default         : toHir.error("cannot declare 'signed'");
                      return toHir.symRoot.typeInt;
    }
  }
  //-------------------------------------------------------------------
  /**
   * Convert signed type.
   *
   * @return  HIR type.
  **/
  private Type cnvUnsigned()
  {
    switch(byteArray[byteIndex++])
    {
    case CHAR_T     : return toHir.symRoot.typeU_Char;
    case SHORT_T    : return toHir.symRoot.typeU_Short;
    case INT_T      : return toHir.symRoot.typeU_Int;
    case LONG_T     : return toHir.symRoot.typeU_Long;
    case LONG_LONG_T: return toHir.symRoot.typeU_LongLong;
    default         : toHir.error("cannot declare 'unsigned'");
                      return toHir.symRoot.typeU_Int;
    }
  }
  //-------------------------------------------------------------------
  /**
   * Convert struct type.
   *
   * @return  HIR type.
  **/
  //##83 private Type cnvStruct()
  private Type cnvStruct(boolean pLocal) //##83
  {
    Sym  tag  = cnvTag(STRUCT_END, pLocal); //##83
    Type type = tag.getSymType();
    if( type==null )
    {
      // declare incomplete type of struct
      SymTable bak = toHir.symRoot.symTableCurrent;
      toHir.symRoot.symTableCurrent = getNormalTable();
      type = sym.structType(null,tag);
      toHir.symRoot.symTableCurrent = bak;

      tag.setSymType( type );
      ((StructType)type).setTag(tag); //##12
    }
    else if( type.getTypeKind()!=Type.KIND_STRUCT )
    {
      toHir.error("'"+tag.getName()+"' is not struct tag");
    }
    return type;
  }
  //-------------------------------------------------------------------
  /**
   * Convert union type.
   *
   * @return  HIR type.
  **/
  //##83 private Type cnvUnion()
  private Type cnvUnion(boolean pLocal) //##83
  {
    Sym   tag = cnvTag(UNION_END, pLocal); //##83
    Type type = tag.getSymType();
    if( type==null )
    {
      // declare incomplete type of union
      SymTable bak = toHir.symRoot.symTableCurrent;
      toHir.symRoot.symTableCurrent = getNormalTable();
      type = sym.unionType(null,tag);
      toHir.symRoot.symTableCurrent = bak;

      tag.setSymType( type );
      ((UnionType)type).setTag(tag); //##12
    }
    else if( type.getTypeKind()!=Type.KIND_UNION )
    {
      toHir.error("'"+tag.getName()+"' is not union tag");
    }
    return type;
  }
  //-------------------------------------------------------------------
  /**
   * Convert enum type.
   *
   * @return  HIR type.
  **/
  //##83 private Type cnvEnum()
  private Type cnvEnum(boolean pLocal) //##83
  {
    Sym   tag = cnvTag(ENUM_END, pLocal); //##83
    Type type = tag.getSymType();
    if( type==null )
    {
      // declare incomplete(no member) type of enum
      SymTable bak = toHir.symRoot.symTableCurrent;
      toHir.symRoot.symTableCurrent = getNormalTable();
      type = sym.enumType(null,tag);
      toHir.symRoot.symTableCurrent = bak;

      tag.setSymType( type );
      //##17 ((EnumType)type).setTag(tag); //##12
    }
    else if( type.getTypeKind()!=Type.KIND_ENUM )
    {
      toHir.error("'"+tag.getName()+"' is not enum tag");
    }
    return type;
  }
  //-------------------------------------------------------------------
  /**
   * Convert array type.
   *
   * @return  HIR type.
  **/
  //##83 private Type cnvArray()
  private Type cnvArray(boolean pLocal) //##83
  {
    long size=0;
    if(byteArray[byteIndex]==NO_DIMENSION_T)
    {
      byteIndex++;
      size = 0; // Treat the size of incomplete array as 0.
    }
    else
    {
      for( int c=byteArray[byteIndex];
           '0'<=c && c<='9';
           c=byteArray[++byteIndex] )
        size = size*10+c-'0';
      if( size==0 ) //SF041126
        toHir.warning("ISO C forbids zero-size array");
    }
    Type elemtype = cnvType(pLocal);
    if( elemtype.getSizeExp()==null ) //SF041126
    {
      toHir.error("elements of array have incomplete type");
      elemtype = toHir.symRoot.typeInt;
    }
    if( elemtype.getTypeKind()==Type.KIND_VOID ) //SF041126
    {
      toHir.error("declaration as array of voids");
      elemtype = toHir.symRoot.typeInt;
    }
    if( elemtype.getTypeKind()==Type.KIND_SUBP ) //SF041126
    {
      toHir.error("declaration as array of functions");
      elemtype = toHir.symRoot.typeInt;
    }
    Type type = sym.vectorType(elemtype,size);
    if( elemtype.isConst() ) //SF050215
      type = type.makeConstType(); //SF050215
    return type;
  }
  //-------------------------------------------------------------------
  /**
   * Convert function type.
   *
   * @return  HIR type.
  **/
  //##83 private Type cnvFunction()
  private Type cnvFunction(boolean pLocal) //##83
  {
    IrList il = hir.irList(); //SF041030
    boolean optionalparam = false; //SF041030
    boolean noparamspec = true; //SF041030

    while( byteArray[byteIndex]!=RETURN_T )
    {
      Type t = cnvType(pLocal); //##83
      if( t==null )
      {
        if( il.size()>0 )
          optionalparam = true;
      }
      else if( t==toHir.symRoot.typeVoid )
      {
        if( il.size()>0 || byteArray[byteIndex]!=RETURN_T )
          toHir.error("invalid function parameter type 'void'");
        noparamspec = false;
      }
      else
      {
        switch( t.getTypeKind() )
        {
        case Type.KIND_VECTOR:
          t = sym.pointerType( ((VectorType)t).getElemType() );
          break;
        case Type.KIND_SUBP:
          t = sym.pointerType( (SubpType)t );
          break;
        }
        il.add(t);
        noparamspec = false;
      }
    }
    byteIndex++;
    Type rettype = cnvType(pLocal); //##83
    if (fDbgLevel > 0)                                //##101
      message(4," convFunction "+rettype.toString()); //##101
    if( rettype.isConst() || rettype.isVolatile()
       || rettype.isRestrict() ) //SF041126 //##81
    {
      if (fDbgLevel > 0)                                      //##101
        message(4, " rettype " + rettype.isConst() + " " +    //##101
          rettype.isVolatile() + " " + rettype.isRestrict() );//##101
      rettype = rettype.getUnqualifiedType();
      message(4, " return_type " + rettype.toString());       //##101
      //##101 toHir.warning("type qualifiers ignored on function return type");
      toHir.warning("adjust the type qualifiers for function return type"); //##101
    }
    return sym.subpType( rettype, il, optionalparam, noparamspec, null ); //SF041030
  }
  //-------------------------------------------------------------------
  /**
   * Get normal (block scope or global) symbol table
   * other than SymTable owned by STRUCT, UNION, ENUM.
   *
   * @return  Normal symbol table.
  **/
  //##83 private SymTable getNormalTable()
  public SymTable getNormalTable() //##83
  {
    SymTable table = toHir.symRoot.symTableCurrent;
    if (fDbgLevel > 3) //##67
      message(6,"getNormalTable OWNERSYM="+table.getOwner());
    while( table.getOwner()!=null
    &&     table.getOwner().getSymKind()==Sym.KIND_TYPE )
    {
      table = table.getParent();
      if (fDbgLevel > 3) //##67
        message(6,"getNormalTable PARENTOWNERSYM="+table.getOwner());
    }
    return table;
  }
  //-------------------------------------------------------------------
  /**
   * Convert tag symbol.
   * If tag name is number generated by ast,
   * generate tag name which corresponds to the number.
   *
   * @param   nameend AST type name terminator.
   * @return  HIR type name string.
  **/
  //##83 private Sym cnvTag(char nameend)
  private Sym cnvTag(char nameend, boolean pLocal) //##83
  {
    int index = byteIndex; // (asterisk is pointer)  index = <*agname>
    while( (char)byteArray[byteIndex++]!=nameend ); // byteIndex = <tagname>*
    String tagname = new String( byteArray, index, byteIndex-1-index ).intern();
    //##83 Sym tag = getNormalTable().search(tagname,Sym.KIND_TAG);
    //##83 BEGIN
    Sym tag;
    if (pLocal)
      tag = getNormalTable().searchLocal(tagname,Sym.KIND_TAG); //##83
    else
      tag = getNormalTable().search(tagname,Sym.KIND_TAG);
    //##83 END
    if( tag==null ) // if no tag symbol, generate it.
      tag = getNormalTable().generateTag(tagname);
    return tag;
  }
  //-------------------------------------------------------------------
  // declarator
  //-------------------------------------------------------------------
  // Storage class (defined in Var.java)
  //   VAR_STATIC    = 6,  // Life time spans entire execution.
  //   VAR_AUTO      = 7,  // Life time begins at entry to a subprogram
  //                       // and ends at exit from the subprogram.
  // Visibility (defined in Sym.java)
  //   SYM_EXTERN    = 1,  // External symbol not defined but visible
  //                       // in this compile unit.
  //                       // setVisibility method in Var and Subp will
  //                       // refuse to change from PUBLIC to EXTERN
  //                       // but will accept to change from EXTERN to PUBLIC.
  //   SYM_PUBLIC    = 2,  // Symbol defined in this compile unit or class
  //                       // and visible from other compile unit or class.
  //   SYM_PROTECTED = 3,  // Symbol that is defined in a class and
  //                       // visible in the class and its subclasses.
  //   SYM_PRIVATE   = 4,  // Symbol that is visible only in the class
  //                       // or subprogram defining the symbol.
  //   SYM_COMPILE_UNIT = 5; // Visible only in the compile unit
  //                         // defining the symbol.
  //-------------------------------------------------------------------
  /**
   * Declare type (=typedef).
   * Create type symbol and add into symTableCurrent.
   *
   * @param  type Type of the symbol.
   * @param  name Name of the symbol.
  **/
  void declareType(Type type,String name)
  {
    if (fDbgLevel > 3) //##67
      message(4,"declareType NAME="+name+" TYPE="+type);
    Sym s = toHir.searchLocalOrdinaryId(name);
    if( s==null )
    {
      ((SymImpl)sym).definedType(
        name, type, toHir.symRoot.symTableCurrent.getOwner() );
    }
    else if( s.getSymKind()!=Sym.KIND_TYPE )
      toHir.error("symbol '"+name+"' is redeclared in different kind");
    else if( s.getSymType()!=type )
      toHir.error("redefinition of typedef '"+name+"'");
  }
  //-------------------------------------------------------------------
  /**
   * Declare function at global scope.
   * Create subprogram symbol and add into symTableRoot.
   *
   * @param   storage AST storage class.
   * @param   type HIR SubpType object.
   * @param   name Function name.
   * @param   init True if declaration has function body.
   * @return  Declared Subp.
  **/
  Subp declareGlobalFunction(int storage,SubpType type,String name,boolean init)
  {
    if (fDbgLevel > 3) //##67
      message(4,"declareGlobalFunction NAME="+name+" TYPE="+type+" STORAGE="+storage);

    int oldvisible = 0;
    int newvisible = Sym.SYM_EXTERN;
    Sym s = toHir.searchGlobalOrdinaryId(name);

    // crete/check symbol
    if( s==null ) // Function symbol with the same name has not yet appeared.
    {
      //SF041030[
      //if( init && type.hasNoParamSpec() )
      //  type = sym.subpType(
      //    type.getReturnType(),
      //    type.getParamTypeList(),
      //    false, false, null );
      //SF041030]
      SymTable table = toHir.symRoot.symTableCurrent; //SF030127
      toHir.symRoot.symTableCurrent = toHir.symRoot.symTableRoot; //SF030127
      s = sym.defineSubp( name, type.getReturnType() ); //SF030127
      toHir.symRoot.symTableCurrent = table; //SF030127
      s.setSymType(type);
    }
    else if( s.getSymKind()!=Sym.KIND_SUBP ) // Same name was found but not a function.
    {
      toHir.error("symbol '"+name+"' is redeclared in different kind");
      return null;
    }
    else // Function symbol with the same name has already appeared.
    {
      Type t = toHir.compositeType( s.getSymType(), type, true );
      if( t==null ) // Failed to get composite type.
        toHir.error("function '"+name+"' is redeclared in different type");
      else //SF041126
        s.setSymType(t); // Assume the new type to continue processing in case of error.
      oldvisible = ((Subp)s).getVisibility();
    }
    // set storage class
    switch( storage )
    {
    case S_NONE:
      break;
    case S_STATIC:
      newvisible = Sym.SYM_COMPILE_UNIT;
      break;
    case S_EXTERN:
      if( init )
        toHir.warning("function '"+name+"' is defined though declared 'extern'");
      break;
    case S_AUTO:
      toHir.warning("cannot use storage class 'auto' here");
      newvisible = Sym.SYM_COMPILE_UNIT;
      break;
    case S_REGISTER:
      toHir.warning("cannot use storage class 'register' here");
      newvisible = Sym.SYM_COMPILE_UNIT;
      break;
    case S_INLINE:
      toHir.warning("storage class 'inline' is disregarded");
      break;
    default:
      toHir.fatal("declareFunction");
    }
    if( newvisible==Sym.SYM_EXTERN && init ) // Make it public even if declared as
      newvisible = Sym.SYM_PUBLIC; //extern because difinition is given.
    if( oldvisible<newvisible )
      ((Subp)s).setVisibility(newvisible);
    return (Subp)s;
  }
  //-------------------------------------------------------------------
  /**
   * Declare function at local scope.
   * Create subprogram symbol and add into symTableCurrent.
   *
   * @param   astorage AST storage class.
   * @param   type HIR SubpType object.
   * @param   name Function name.
   * @return  Declared Subp.
  **/
  Subp declareLocalFunction(int storage,SubpType type,String name)
  {
    // Local function declaration is treated in the same way as global
    // function in Borland C, and in gcc, it is treated as global.
    // So, the local function declaration is treated in the same way
    // as global function in this compiler.
    // If the function symbol is made local, then the result of Hir2C
    // can not be processed by gcc.
    // gcc treat int func(); as a function without parameters
    // and do not treat it as abbreviation of parameters. //SF030131
    return declareGlobalFunction(storage,type,name,false);
  }
  //-------------------------------------------------------------------
  /**
   * Declare variable at global scope.
   * Create variable symbol and add into symTableRoot.
   *
   * @param   storage AST storage class.
   * @param   type HIR Type object.
   * @param   name Variable name.
   * @param   ini Initializer.
   * @return  Declared Var.
  **/
  Var declareGlobalVariable(int storage,Type type,String name,Expr ini)
  {
    if (fDbgLevel > 3) //##67
      message(4,"declareGlobalVariable NAME="+name+" TYPE="+type+" STORAGE="+storage);
    int oldvisible = 0;
    int newvisible ;

    Sym s = toHir.searchGlobalOrdinaryId(name);
    if( s==null )
    {
      // declare variable
      SymTable table = toHir.symRoot.symTableCurrent; //SF040420
      toHir.symRoot.symTableCurrent = toHir.symRoot.symTableRoot; //SF040420
      s = sym.defineVar( name, type ); //SF040420
      toHir.symRoot.symTableCurrent = table; //SF040420
      s.setSymType(type); //SF040420
    }
    else if( s.getSymKind()!=Sym.KIND_VAR )
    {
      toHir.error("symbol '"+name+"' is redeclared in different kind");
      return null;
    }
    else
    {
      Type t = toHir.compositeType( s.getSymType(), type, true ); // Get compound type.
      if( t==null ) // Failed to get compound type.
        toHir.error("variable '"+name+"' is redeclared in different type");
      else //SF041006
        s.setSymType(t); // Assume the new type to continue processing in case of error.
      oldvisible = ((Var)s).getVisibility();
    }
    // set storage class
    switch( storage )
    {
    case S_NONE:
      newvisible = Sym.SYM_PUBLIC;
      break;
    case S_STATIC:
      newvisible = Sym.SYM_COMPILE_UNIT;
      break;
    case S_EXTERN:
      newvisible = Sym.SYM_EXTERN;
      if( ini!=null )
      {
        toHir.warning("variable '"+name+"' initialized though declared 'extern'");
        newvisible = Sym.SYM_PUBLIC; // when the variable has initial value.
      }
      break;
    case S_AUTO:
      newvisible = Sym.SYM_COMPILE_UNIT;
      toHir.warning("cannot use storage class 'auto' here");
      break;
    case S_REGISTER:
      newvisible = Sym.SYM_COMPILE_UNIT;
      toHir.warning("cannot use storage class 'register' here");
      break;
    case S_INLINE:
      newvisible = Sym.SYM_PUBLIC;
      toHir.warning("storage class 'inline' is disregarded");
      break;
    default:
      newvisible = Sym.SYM_EXTERN;
      toHir.fatal("declareVariable");
    }
    // CoinsCC permits linkage conflict, and narrower linkage is adopted.
    //if( oldvisible!=0
    //&&  oldvisible!=Sym.SYM_COMPILE_UNIT
    //&&  newvisible==Sym.SYM_COMPILE_UNIT )
    //  toHir.error("static declaration of '"+name+"' follows non-static declaration");
    //if( oldvisible==Sym.SYM_COMPILE_UNIT
    //&&  newvisible!=Sym.SYM_COMPILE_UNIT )
    //  toHir.error("non-static declaration of '"+name+"' follows static declaration");

    if( oldvisible<newvisible )
      ((Var)s).setVisibility(newvisible);
    ((Var)s).setStorageClass(Var.VAR_STATIC);

    return (Var)s;
  }
  //-------------------------------------------------------------------
  /**
   * Declare variable at local scope.
   * Create variable symbol and add into symTableCurrent.
   *
   * @param   storage AST storage class.
   * @param   type HIR Type object.
   * @param   name Variable name.
   * @param   ini Initializer.
   * @return  Declared Var.
  **/
  Var declareLocalVariable(int storage,Type type,String name,Expr ini)
  {
    // Treat extern variable in local scope as global variable
    // (same as Borland C).
    if( storage==S_EXTERN )
      return declareGlobalVariable(storage,type,name,ini);
    if (fDbgLevel > 3) //##67
      message(4,"declareLocalVariable NAME="+name+" TYPE="+type+" STORAGE="+storage);
    Sym s = toHir.searchLocalOrdinaryId(name);
    if( s==null)
    {
      //declare variable
      s = sym.defineVar( name, type ); //SF040420
      s.setSymType(type); //SF040420
    }
    else if( s.getSymKind()!=Sym.KIND_VAR && s.getSymKind()!=Sym.KIND_PARAM )
    {
      toHir.error("symbol '"+name+"' is redeclared in different kind");
      return null;
    }
    else
    {
      toHir.error("variable '"+name+"' is redeclared");
      return (Var)s;
    }
    // set storage class
    int newstorage = Var.VAR_AUTO;
    switch( storage )
    {
      case S_NONE:
        break;
      case S_STATIC:
        newstorage = Var.VAR_STATIC;
        break;
      //case S_EXTERN:
      //  break;
      case S_AUTO:
        break;
      case S_REGISTER:
        //newstorage = Var.VAR_REGISTER;
        //!! register storage class causes illegal result.
        break;
      case S_INLINE:
        toHir.warning("storage class 'inline' is disregarded");
        break;
      default:
        toHir.fatal("declareLocallVariable: "+storage);
    }
    if( type.getSizeValue()<=0 ) //SF050215
      toHir.error("'"+name+"' is incomplete type or size 0"); //SF050215

    ((Var)s).setStorageClass(newstorage);
    ((Var)s).setVisibility(Sym.SYM_PRIVATE);
    return (Var)s;
  }
//-------------------------------------------------------------------
}
