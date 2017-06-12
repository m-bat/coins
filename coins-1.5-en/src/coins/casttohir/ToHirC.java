/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.casttohir;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet; //##70
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Set; //##70

import coins.HirRoot;
import coins.SymRoot;
import coins.ast.ASTList;
import coins.ast.ASTree;
import coins.ast.Declarator;
import coins.ast.DeclaratorList;
import coins.ast.Enum;
import coins.ast.Expr;
import coins.ast.Function;
import coins.ast.Pair;
import coins.ast.Pragma;
import coins.ast.Stmnt;
import coins.ast.Struct;
import coins.ast.TokenId;
import coins.ast.TypeId;
import coins.ast.Union;
import coins.ast.Visitor;
import coins.ast.expr.AddressExpr;
import coins.ast.expr.ArithBinaryExpr;
import coins.ast.expr.ArithUnaryExpr;
import coins.ast.expr.ArrayExpr;
import coins.ast.expr.ArrayInitializer;
import coins.ast.expr.AsmExpr; //##70
import coins.ast.expr.AssignExpr;
import coins.ast.expr.CallExpr;
import coins.ast.expr.CastExpr;
import coins.ast.expr.CommaExpr;
import coins.ast.expr.ConditionalExpr;
import coins.ast.expr.ConstantExpr;
import coins.ast.expr.DereferenceExpr;
import coins.ast.expr.MemberExpr;
import coins.ast.expr.PointerBinaryExpr;
import coins.ast.expr.PostfixExpr;
import coins.ast.expr.PrefixExpr;
import coins.ast.expr.SizeofExpr;
import coins.ast.expr.StringLiteral;
import coins.ast.expr.WcharLiteral;
import coins.ast.expr.VariableExpr;
import coins.ast.stmnt.BreakStmnt;
import coins.ast.stmnt.CaseLabel;
import coins.ast.stmnt.CompoundStmnt;
import coins.ast.stmnt.ContinueStmnt;
import coins.ast.stmnt.DefaultLabel;
import coins.ast.stmnt.DoStmnt;
import coins.ast.stmnt.ExpressionStmnt;
import coins.ast.stmnt.ForStmnt;
import coins.ast.stmnt.GotoStmnt;
import coins.ast.stmnt.IfStmnt;
import coins.ast.stmnt.NamedLabel;
import coins.ast.stmnt.NullStmnt;
import coins.ast.stmnt.ReturnStmnt;
import coins.ast.stmnt.SwitchStmnt;
import coins.ast.stmnt.WhileStmnt;
import coins.cfront.GccLex;
import coins.cfront.Lex;
import coins.cfront.Parser;
import coins.FatalError;
import coins.ir.IrList;
import coins.ir.IrListImpl; //##70
import coins.ir.hir.BlockStmt;
import coins.ir.hir.ConstNode;
import coins.ir.hir.SetDataStmt;
import coins.ir.hir.Exp;
import coins.ir.hir.HIR;
import coins.ir.hir.HirList; //##70
import coins.ir.hir.HirSeq;
import coins.ir.hir.LabelNode;
import coins.ir.hir.Program;
import coins.ir.hir.Stmt;
import coins.ir.hir.SubpDefinition;
import coins.ir.hir.VarNode; //##81
import coins.sym.Const;
import coins.sym.Elem;
import coins.sym.EnumType;
import coins.sym.Label;
import coins.sym.Param;
import coins.sym.PointerType;
import coins.sym.StringConst;
import coins.sym.StructType;
import coins.sym.Subp;
import coins.sym.SubpImpl;  //##81
import coins.sym.SubpType;
import coins.sym.Sym;
import coins.sym.SymIterator;
import coins.sym.SymTable;
import coins.sym.SymTableImpl;
import coins.sym.Type;
import coins.sym.UnionType;
import coins.sym.Var;
import coins.sym.VectorType;

/**
 * Convert ASTree(abstruct syntax tree) to HIR-C.
 *
 * @author  Shuichi Fukuda
**/
public final class ToHirC implements Visitor, TypeId, TokenId
{
  private SubpDefinition nowSubpDef; // now processing subprogram definition
  private BlockStmt      nowBlock;   // now processing block
  private BlockStmt      nowLocal;   // local variable initialization block
  private int            nowDepth;   // depth of now processing block scope
  private HIR            nowHir;     // now processing HIR
  private Lex            lex;        // lex

  private Type   ctrlExpType   = null; // controlling exp. type of switch
  private Label  continueLabel = null; // continue destination label
  private Label  breakLabel    = null; // break    destination label
  private IrList caseList      = null; // const-label pair list for switch
  private Label  defaultLabel  = null; // default point label   for switch

  private final ToHir     toHir;
  private final HirRoot   hirRoot; //SF041014
  private final SymRoot   symRoot; //SF041014
  private final HIR       hir;    // HIR instance (used to create HIR objects)
  private final Sym       sym;    // Sym instance (used to create Sym objects)
  private final ToHirInit toInit; // initializer creator
  private final ToHirSym  toSym;  // expression caster
  private final ConditionalReporter reporter; // warnings and errors reporter

  static final byte[] astPrototype = // default function prototype of AST
  {
    (byte)FUNCTION_T,
    //SF041030 (byte)ELLIPSIS_T,
    (byte)RETURN_T,
    (byte)INT_T
  };

  static int lListNum = 0; //###
  //-------------------------------------------------------------------
  /**
   * Constructor.
   *
   * @param  tohir Offers cooperation with the object of other packages.
  **/
  public ToHirC(ToHir tohir)
  {
    toHir    = tohir;
    hirRoot  = toHir.hirRoot; //SF041014
    symRoot  = toHir.symRoot; //SF041014
    hir      = hirRoot.hir;
    sym      = hirRoot.sym;
    message(1,"ToHirC\n"); //##71
    toInit   = new ToHirInit(toHir,this);
    toSym    = new ToHirSym(toHir);
    reporter = new ConditionalReporter(toHir);
  }
  //-------------------------------------------------------------------
  /**
   * Output debug message.
   *
   * @param  level Debug level.
   * @param  mes Debug message.
  **/
  protected void message(int level,String mes)
  {
    toHir.debug.print( level, "C1", mes );
  }
  //-------------------------------------------------------------------
  /**
   * Create ASTree by ast.Parser.read(),
   * and visit ASTree to create corresponding HIR-C.
   *
   * @param  stream Input stream.
  **/
  public void astToHirC(InputStream stream) throws IOException, FatalError
  {
    nowHir     = null;
    nowSubpDef = null;
    nowBlock   = null;
    nowLocal   = null;
    nowDepth   = 0;

             lex     = new GccLex(toHir.ioRoot,stream);
    Parser   parser  = new Parser(toHir.ioRoot,lex,this);
    SymTable astroot = new SymTableImpl(symRoot);
    message(1,"\nastToHirC\n"); //##70

    while( lex.lookAhead()!=EOF )
    {
      message(4,"lookAhead in astToHirC " + lex.lookAhead() + " " + lex.getString());

      symRoot.symTableCurrent = astroot;
      symRoot.subpCurrent     = null;
      ASTList list = parser.read();

      symRoot.symTableCurrent = symRoot.symTableRoot;
      symRoot.subpCurrent     = null;
      for( ; list!=null; list=list.tail() )
      {
        message(1,"C parser\n"+list.head());
        visit( list.head() );
      }
    }
    createZeroInitializerForGlobal();
  }
  //-------------------------------------------------------------------
  /**
   * Visit ASTree.
   *
   * @param  sat ASTree to visit.
  **/
  public HIR visit(ASTree ast) //SF050111
  {
    if( ast==null )
      return null;

    if( ast instanceof Stmnt )
    {
      String oldfile = toHir.nowFile; // push
      int    oldline = toHir.nowLine;
      String nowfile = ((Stmnt)ast).fileName();
      int    nowline = ((Stmnt)ast).lineNumber();
      toHir.nowFile = nowfile!=null ? nowfile : oldfile;
      toHir.nowLine = nowline!=0    ? nowline : oldline;
      //toHir.nowFile = ((Stmnt)ast).fileName();
      //toHir.nowLine = ((Stmnt)ast).lineNumber();
      //if( toHir.nowFile==null ) toHir.nowFile = oldfile;
      //if( toHir.nowLine==0    ) toHir.nowLine = oldline;

      message(8,"{"+ast.getClass().getName()+"}\t"+nowfile+"("+nowline+")");
      ast.accept(this);

      if( nowHir instanceof Stmt )
      {
        ((Stmt)nowHir).setFileName( toHir.nowFile );
        ((Stmt)nowHir).setLineNumber( toHir.nowLine );
      }
      toHir.nowFile = oldfile; // pop
      toHir.nowLine = oldline;
    }
    else
    {
      message(8,"<"+ast.getClass().getName()+">");
      ast.accept(this);
    }

    HIR h = nowHir;
    nowHir = null;
    return h;
  }
  //-------------------------------------------------------------------
  // create HIR-C
  //-------------------------------------------------------------------
  /**
   * At AST list.
   * Declarator list is processed at upper level. Must not reach here.
   *
   * @param  ast ASTList
  **/
  public void atASTList(ASTList ast)
  {
    toHir.fatal("atASTList");
  }
  //-------------------------------------------------------------------
  /**
   * atPragma changes Pragma to InfStmt whose inf-body
   * is the String specified in the pragma statement.
   * A pragma statement may take the form of
   *   #pragma kindName pragmaBody
   * where kindName is a String and pragmaBody is a sequence
   * of String composed of Sym and list (sequence of Syms and lists
   * enclosed in parenthesis.
   * The resultant InfStmt is tentatively composed by
   * attaching information by addInf(kindName, pragmaBody).
   * It will be transformed into final form by atInfStmt of
   * coins.casttohir.HirVisit. The reason to take tentative
   * form in ToHirC is symbols appearing in pragmaBody
   * may not yet be defined (registered) in SymTable.
   * @param  ast Pragma character string contained in the pragma statement.
  **/
  public void atPragma(Pragma ast) //SF050320
  {
    String text = ast.getText();
    int length = text.length();
    if( length==0 )
       return;
    message(4,"atPragma " + text); //##70
    String lPragmaKind, lPragmaBody; //##70
    int i = 0;
    if( Character.isJavaIdentifierStart(text.charAt(0)) ) {
      // Get the name indicating the kind of pragma.
      for (i = 1; i < length; i++) {
        if (!Character.isJavaIdentifierPart(text.charAt(i)))
          break;
      }
    }
    if( i==0 ) {
      if (text.charAt(0) == '"') {
        StringBuffer lBuffer = new StringBuffer();
        char lNextChar;
        for (i++; i < length; i++) {
          lNextChar = text.charAt(i);
          if (lNextChar != '"') {
            lBuffer.append(lNextChar);
          }else {
            i++;
            break;
          }
        }
        lPragmaKind = lBuffer.toString();
        lPragmaBody = text.substring(i).trim();
      }else {
        // No name is given. Assume that the kind name is null string
        // and remaining characters represent its operands.
        //##70 nowHir = hir.infStmt( "", text.intern() );
        lPragmaKind = ""; //##70
        lPragmaBody = text; //##70
      }
    }else if( i==text.length() ) {
      // Name is given but no other characters.
      // Treat it as a pragma with kind-name and null operand.
      //##70 nowHir = hir.infStmt( text.intern(), "" );
      lPragmaKind = text; //##70
      lPragmaBody = "";   //##70
    }else {
      // Pragma with kind name and its operands.
      //##70 nowHir = hir.infStmt(
      //##70   text.substring(0, i).intern(),
      //##70   text.substring(i).trim().intern());
      lPragmaKind = text.substring(0, i);
      lPragmaBody = text.substring(i).trim();
    }
    //##70 BEGIN
    message(4," kind " + lPragmaKind + " body " + lPragmaBody);
    //##81 BEGIN
    if (lPragmaBody.intern() == "safeArrayAll") {
      if (symRoot.subpCurrent != null) {
        ((SubpImpl)symRoot.subpCurrent).fSafeArrayAll = true;
      }else {
        toHir.ioRoot.msgRecovered.put(
          "safeArrayAll in the pragma should be specified within some function");
      }
    }
    //##81 END
    /*
    if ((lPragmaKind != "")&&(lPragmaBody != "")) {
      // Parse the pragma body.
      Set lDelimiters = new HashSet();
      Set lSpaces     = new HashSet();
      Set lIdSpChars  = new HashSet();
      lDelimiters.add("(");
      lDelimiters.add(")");
      lDelimiters.add(",");
      lDelimiters.add(";");
      lSpaces.add(" ");
      lSpaces.add("\t");
      lIdSpChars.add("_");
      lIdSpChars.add("$");
      ParseString lParseString = new ParseString(lPragmaBody,
        lDelimiters, lSpaces, lIdSpChars);
      HirList lHirList = (HirList)hir.irList();
      lHirList.setIndex(++lListNum); //###
      HirList lList;
      String lItem;
      while (lParseString.hasNext()) {
        lItem = lParseString.getNextToken();
        message(4," item " + lItem + " kind " + lParseString.getTokenKind());
        lList = processPragmaItem(lParseString, lItem, lHirList);
        if ((lList != null)&&(lList != lHirList)) {
          message(4,"\n add list " + lList.getIndex() + " to list " + lHirList.getIndex());
          lHirList.add(lList);
          lList = null;
        }
      }
      message(4,"\n lHirList " + lHirList.toString() + " size " + lHirList.size()
              + " 1stElem " + lHirList.get(0).toString()); //###
      if (lHirList.size() > 1) {
        nowHir = hir.infStmt(lPragmaKind.intern(), lHirList);
      }else {
        nowHir = hir.infStmt(lPragmaKind.intern(), lHirList.get(0));
      }
    }else {
      // (lPragmaKind == "")||(lPragmaBody =="")
      nowHir = hir.infStmt( lPragmaKind.intern(), lPragmaBody.intern() );
    }
    */
    IrList lIrList = new IrListImpl(hirRoot); //##
    lIrList.add(lPragmaBody.intern()); //##
    nowHir = hir.infStmt( lPragmaKind.intern(), lIrList);
    if ((hirRoot.symRoot.symTableCurrent == null)||
        (hirRoot.symRoot.symTableCurrent.getOwner() == null)) {
      ((Stmt)nowHir).setFileName(toHir.nowFile);
      ((Stmt)nowHir).setLineNumber(toHir.nowLine);
      ((Program)hirRoot.programRoot).addInitiationStmt(nowHir);
    }
    message(4," pragma result " + nowHir.toString());
    //##70 END
  } // atPragma

//##70 BEGIN
  public void atAsmExpr(AsmExpr ast)
  {
    message(4,"atAsmExpr " + ast);
    String lInstructions = null;
    HirList lActualParams = hir.hirList();

    // visit arguments
    int lIndex = 0;
    for( ASTList al=ast.getArguments(); al!=null; al=al.tail() )
    {
      ASTree lTree = (ASTree)al.head();
      Exp lExp = (Exp)visit(lTree);
      lIndex++;
      if (toHir.fDbgLevel > 1) //##70
      message(4,"arg" + lIndex + " "  + lExp.toStringWithChildren());
      if ((lIndex == 1)&&
          (lExp instanceof ConstNode)
          // &&((ConstNode)lExp).getSymNodeSym().getName().substring(1, 7).intern()== "#param"
             ) {
        lInstructions = ((ConstNode)lExp).getSymNodeSym().getName();
        continue;
      }else {
        Exp lExp2 = pPromotion(lExp);   //##70
        lActualParams.add(lExp2);       //##70
        lExp2.setParent(lActualParams); //##70
      }
    }
    if (lInstructions == null) {
      toHir.warning("Instructions of asm( ) is null string");
      lInstructions = "";
    }
    nowHir = hir.asmStmt(lInstructions, lActualParams);
    /*
    if ((hirRoot.symRoot.symTableCurrent == null)||
        (hirRoot.symRoot.symTableCurrent.getOwner() == null)) {
      ((Stmt)nowHir).setFileName(toHir.nowFile);
      ((Stmt)nowHir).setLineNumber(toHir.nowLine);
      ((Program)hirRoot.programRoot).addInitiationStmt(nowHir);
    }
   */
    if (toHir.fDbgLevel > 1) //##70
      message(4," asmExpr result " + nowHir.toString());
  } // atAsmExpr
  //##70 END

  //-------------------------------------------------------------------
  /**
   * At compound statement.
   *
   * @param  ast ASTList
  **/
  public void atCompoundStmnt(CompoundStmnt ast)
  {
    if( ++nowDepth>1 ) //block depth ++
      symRoot.symTableCurrent.pushSymTable( toHir.createBlockSym() );

    BlockStmt nb = nowBlock; //push block
    BlockStmt nl = nowLocal;
    nowBlock = hir.blockStmt(null);
    nowBlock.setSymTable(symRoot.symTableCurrent);
    nowLocal = null;

    for( ASTList list=ast; list!=null; list=list.tail() ) // each statements
    {
      HIR h = visit( list.head() );
      //if( h!=null )
      //{
      //  Stmt last = nowBlock.getLastStmt();
      //  if( last!=null
      //  &&( last.getOperator()==HIR.OP_JUMP
      //  ||  last.getOperator()==HIR.OP_RETURN ) )
      //    nowBlock.addLastStmt( hir.labeledStmt(toHir.createLabel(),null) );
        nowBlock.addLastStmt((Stmt)h); // add statement
      //}
    }
    createZeroInitializerForStatic();
    nowHir = nowBlock;

    nowBlock = nb; //pop block
    nowLocal = nl;

    if( nowDepth-->1 ) //block depth --
      symRoot.symTableCurrent.popSymTable();
  }
  //-------------------------------------------------------------------
  /**
   * Create StructType and add to symTableCurrent.
   *
   * @param  ast Struct
  **/
  public void atStruct(Struct ast)
  {
    atStructDeclarator(ast);
  }
  public StructType atStructDeclarator(Struct ast)
  {
    String tagname = ast.name();
    Type sttmp = toSym.convertType(
      (""+STRUCT_BEGIN+tagname+STRUCT_END).getBytes()
      , true //##83
      );

    if( sttmp==null || sttmp.getTypeKind()!=Type.KIND_STRUCT )
      return null;

    StructType st = (StructType)sttmp;
    if (toHir.fDbgLevel > 1) //##70
      message(4,"atStructDeclarator STRUCT NAME="+tagname+" TYPE="+st);

    if( st.getElemList().size()>0 )
    {
      toHir.error("redeclared struct '"+st.getName()+"'" );
      return null;
    }
    symRoot.symTableCurrent.pushSymTable(st);

    for( DeclaratorList dl = (DeclaratorList)ast.getLeft();
         dl!=null;
         dl = dl.next() ) // each element
    {
      Declarator d = (Declarator)dl.get();
      String  name = d.getName().intern();
      if( symRoot.symTableCurrent.searchLocal(name,Sym.KIND_ELEM)!=null )
      {
        toHir.error("duplicate member '"+name+"'"); //SF050215
        continue;
      }
      Type    type = toSym.convertType( d.getType()
                     , false //##83
                     );
      Elem    elem = sym.defineElem(name,type);
      if (toHir.fDbgLevel > 1) //##70
        message(6,"ELEM NAME="+name+" TYPE="+type+" SIZE="+type.getSizeValue());
      if( d.isItBitField() )
      {
        int bits = d.getBitFieldSize();
        if (toHir.fDbgLevel > 1) //##70
          message(6,"BITFIELD="+bits);
        if( toHir.isIntegral(type) )
          if( bits <= 8*type.getSizeValue() )
            elem.setBitFieldSize(bits);
          else
            toHir.error( "width of bit-field '"+name+"' exceeds its type" );
        else
          toHir.error( "bit-field '"+name+"' has invalid type" );
      }
      if( type.getSizeValue()<=0 ) //SF050215
        //##79 toHir.error("member '"+name+"' is incomplete type or size 0");
        toHir.warning("member '"+name+"' is incomplete type or size 0"); //##79

      elem.setDefinedFile(d.fileName());
      elem.setDefinedLine(d.lineNumber());
      st.addElem(elem);
    }
    symRoot.symTableCurrent.popSymTable();

    // finish struct definition
    SymTable table = symRoot.symTableCurrent;
    symRoot.symTableCurrent = toHir.getSubpTable();
    st.finishStructType(true);
    symRoot.symTableCurrent = table;

    toSym.makeQualifiedTypes(st,tagname); //SF050215
    return st;
  }
  //-------------------------------------------------------------------
  /**
   * Create UnionType and add symTableCurrent.
   *
   * @param  ast Union
  **/
  public void atUnion(Union ast)
  {
    atUnionDeclarator(ast);
  }
  //-------------------------------------------------------------------
  /**
   * Create UnionType and add symTableCurrent.
   *
   * @param  ast Union
  **/
  public UnionType atUnionDeclarator(Union ast)
  {
    String tagname = ast.name();
    Type uttmp = toSym.convertType(
      (""+UNION_BEGIN+tagname+UNION_END).getBytes()
       , true  //##83
       );

    if( uttmp==null
    ||  uttmp.getTypeKind()!=Type.KIND_UNION )
      return null;

    if (toHir.fDbgLevel > 1) //##70
      message(4,"atUnionDeclarator UNION NAME="+tagname+" TYPE="+uttmp);
    UnionType ut = (UnionType)uttmp;
    if( ut.getOrigin()!=null )
    {
      toHir.error("redeclared union '"+ut.getName()+"'" );
      return null;
    }
    symRoot.symTableCurrent.pushSymTable(ut);
    for( DeclaratorList dl = (DeclaratorList)ast.getLeft();
         dl!=null;
         dl=dl.next() ) // each element
    {
      Declarator d = (Declarator)dl.get();
      String  name = d.getName().intern();
      if( symRoot.symTableCurrent.searchLocal(name,Sym.KIND_ELEM)!=null )
      {
        toHir.error("duplicate member '"+name+"'"); //SF050215
        continue;
      }
      Type    type = toSym.convertType( d.getType()
                       , false //##83
                       );
      if (toHir.fDbgLevel > 1) //##70
        message(6,"ELEM NAME="+name+" TYPE="+type+" SIZE="+type.getSizeExp());
      Elem elem = sym.defineElem(name,type);
      ut.addElem( elem );
      if( type.getSizeValue()<=0 ) {//SF050215
        //##81 toHir.error("member '"+name+"' is incomplete type or size 0");
        toHir.warning("member '"+name+"' is incomplete type or size 0"); //##81
      }
      elem.setDefinedFile(d.fileName());
      elem.setDefinedLine(d.lineNumber());
    }
    symRoot.symTableCurrent.popSymTable();

    // set origin type
    SymTable table = symRoot.symTableCurrent;
    symRoot.symTableCurrent = toHir.getSubpTable();
    ut.finishUnionType(true);
    symRoot.symTableCurrent = table;

    toSym.makeQualifiedTypes(ut,tagname); //SF050215
    return ut;
  }
  //-------------------------------------------------------------------
  /**
   * Create EnumType and add symTableCurrent.
   *
   * @param  ast Enum
  **/
  public void atEnum(Enum ast)
  {
    Type ettmp = toSym.convertType(
      ((""+ENUM_BEGIN)+ast.name()+(""+ENUM_END)).getBytes()
      , true //##83
       );

    if( ettmp==null
    ||  ettmp.getTypeKind()!=Type.KIND_ENUM )
      return;

    EnumType et = (EnumType)ettmp;
    //##83 if( et.getOrigin()!=null )
    if( et.getOrigin()!=null )
    {
      //##83 BEGIN
      if (toHir.fDbgLevel > 3)
        message(6, "atEnum definedIn " + et.getDefinedIn()
               + " recordedIn " + et.getRecordedIn()
               + " normalTable " + toSym.getNormalTable()
                + " incomplete " + et.getFlag(Type.FLAG_INCOMPLETE_TYPE));
      //## if (et.getRecordedIn() != toSym.getNormalTable()) {
        //##83 END
        toHir.error("redeclared enum '" + et.getName() + "'");
        return;
      //## } //##83
    }
    //symRoot.symTableCurrent.pushSymTable(et);
    long value = 0; // value be related enumeration
    for( Enum.Item item=ast.getItems(); item!=null; item=item.getNext() )
    {
      ConstantExpr ce = item.getValue();
      if( ce!=null )
      {
        if( ce.getTypeChar()==INT_T )
          value = ce.longValue();
        else
          toHir.error("definition of enumeration constant requires 'const int'");
      }
      String name = item.getName().intern();
      if (toHir.fDbgLevel > 1) //##70
        message(6,"ENUM NAME="+name+" VALUE="+value);
      et.addElem( sym.namedConst( name, (int)value++, symRoot.typeInt ) );
    }
    //symRoot.symTableCurrent.popSymTable();
    // set origin type
    SymTable table = symRoot.symTableCurrent;
    symRoot.symTableCurrent = toHir.getSubpTable();
    et.finishEnumType(true);
    symRoot.symTableCurrent = table;
  }
  //-------------------------------------------------------------------
  // declarator & initializer
  //-------------------------------------------------------------------
  /**
   * Create symbol and add into symbol table.
   * (= typedef, function prototype, variable declaration)
   *
   * @param  ast Declarator
  **/
  public void atDeclarator(Declarator ast)
  {
    Type   type = toSym.convertType(ast.getType()
                   , false //##83
                   );
    String name = ast.getName().intern();
    Expr   init = ast.getInitializer();
    int storage = ast.getStorage();

    if( ast.isTypedef() ) // declare type
    {
      toSym.declareType(type,name);
      if( init!=null ) //SF041126
        toHir.error("typedef '"+name+"' is initialized like a variable"); //SF041126
    }
    else if( type.getTypeKind()==Type.KIND_SUBP ) //declare function
    {
      Subp f = nowDepth==0
             ? toSym.declareGlobalFunction(storage,(SubpType)type,name,false)
             : toSym.declareLocalFunction (storage,(SubpType)type,name);
      f.setDefinedFile(ast.fileName());
      f.setDefinedLine(ast.lineNumber());
      if( init!=null ) //SF041126
        toHir.error("function '"+name+"' is initialized like a variable"); //SF041126
    }
    else // declare variable
    {
      if( type.getTypeKind()==Type.KIND_VOID ) //SF041126
        toHir.error("variable '"+name+"' declared void");
      Var v = nowDepth==0
            ? toSym.declareGlobalVariable(storage,type,name,init)
            : toSym.declareLocalVariable (storage,type,name,init);
      if( v!=null )
      {
        v.setDefinedFile(ast.fileName()); //##16
        v.setDefinedLine(ast.lineNumber()); //##16
        if( init!=null )
        {
          //Type vt = v.getSymType();
          //if( vt.getTypeRank()==0
          //&&  init instanceof ArrayInitializer // ArrayInitializer to aggregate
          //||  vt.getTypeRank()!=0
          //&&  !(init instanceof ArrayInitializer) // Initializing expression to scalar
          //||  vt.getTypeKind()==Type.KIND_VECTOR
          //&&  ((VectorType)vt).getElemType()==toHir.typeChar
          //&&  init instanceof StringLiteral ) // String constant to char array
            createInitializer( v, init ); // then initialize.
          //else
          //  toHir.error("invalid initializer of '"+v.getName()+"'");
        }
      }
    }
  }
  //-------------------------------------------------------------------
  /**
   * Create variable initializer.
   *
   * @param  v Initialized variable.
   * @param  ast Initialization expression.
   * @param  storage Storage class.
  **/
  private void createInitializer(Var v,Expr ast)
  {
    if( v==null || v.getVisibility()==Sym.SYM_EXTERN )
      return;
    if( v.getInitialValue()!=null ) // already initialized ?
    {
      toHir.error("variable '"+v.getName()+"' is initialized two or more times");
      return;
    }
    // set initial value
    Exp init = toInit.createSetData(v,ast); //SF030620
    v.setInitialValue(init); //SF030620

    // add initialization statements to initialization block
//##52    if( coins.MachineParam.INIT_BY_DATA_CODE ) // use datacode
    if( toHir.machineParam.initByDataCode() ) // use datacode //##52
    {
      if( symRoot.symTableCurrent == symRoot.symTableRoot ) // global
      {
        SetDataStmt data = hir.setDataStmt( hir.varNode(v), init ); //SF040525
        data.setFileName(toHir.nowFile);
        data.setLineNumber(toHir.nowLine);
        ((Program)hirRoot.programRoot).addInitiationStmt(data); //SF030620
      }
      else if( v.getStorageClass()==Var.VAR_STATIC ) // static
      {
        SetDataStmt data = hir.setDataStmt( hir.varNode(v), init ); //SF040525
        data.setFileName(toHir.nowFile);
        data.setLineNumber(toHir.nowLine);
        nowSubpDef.addInitiationStmt(data); //SF030620
      }
      else { // local
        // if (v.getSymType().isConst()) { //##85
        //   toInit.createAssignStmts( getLocalVarInitBlock(), v, ast );
        // }else { //##85
          toInit.createAssignStmts( nowBlock, v, ast ); //##85
        // } //##85
      }
    }
    else // not use datacode
    {
      if( symRoot.symTableCurrent == symRoot.symTableRoot ) // global
      {
        ((Program)hirRoot.programRoot).addInitiationStmt(null);
          // This is required to make initiation part.
        toInit.createAssignStmts(
          (BlockStmt)((Program)hirRoot.programRoot).getInitiationPart(), v, ast );
      }
      else if( v.getStorageClass()==Var.VAR_STATIC ) // static
      {
        nowSubpDef.addInitiationStmt(null);
          // This is required to make initiation part.
        toInit.createAssignStmts(
          (BlockStmt)nowSubpDef.getInitiationPart(), v, ast );
      }
      else { // local
        // if (v.getSymType().isConst()) { //##85
        //   toInit.createAssignStmts( getLocalVarInitBlock(), v, ast );
        // }else {  //##85
          toInit.createAssignStmts( nowBlock, v, ast ); //##85
        // } //##85
      }
    }
  }
  //-------------------------------------------------------------------
  /**
   * Get local variable initialization block.
   *
   * @return  Local variable initialization block.
  **/
  private BlockStmt getLocalVarInitBlock()
  {
    if( nowLocal==null )
    {
      symRoot.symTableCurrent.pushSymTable( toHir.createBlockSym() );
      nowLocal = hir.blockStmt(null);
      nowLocal.setSymTable( symRoot.symTableCurrent );
      nowLocal.setFlag(HIR.FLAG_INIT_BLOCK,true);
      nowBlock.addFirstStmt(nowLocal);
      symRoot.symTableCurrent.popSymTable();
    }
    return nowLocal;
  }
  //-------------------------------------------------------------------
  /**
   * At declaration list.
   * Declarator list is processed at upper level. Must not reach here.
   *
   * @param  ast DeclaratorList
  **/
  public void atDeclaratorList(DeclaratorList ast)
  {
    toHir.fatal("DeclaratorList");
  }
  //-------------------------------------------------------------------
  /**
   * At array initializer.
   * Array initializer is processed at upper level. Must not reach here.
   *
   * @param  ast ArrayInitializer
  **/
  public void atArrayInitializer(ArrayInitializer ast)
  {
    toHir.fatal("atArrayInitializer");
  }
  //-------------------------------------------------------------------
  /**
   * Create function, and add programRoot.
   *
   * @param  ast Function
  **/
  public void atFunction(Function ast)
  {
    String subpname = ast.getName().intern();
    Type type = toSym.convertType(ast.getType()
                , false //##83
                );

    if( type.getTypeKind()!=Type.KIND_SUBP ) //SF041126
    {
      toHir.error("variable is defined like a function"); //SF041126
      return; //SF041126
    }
    //##87 toHir.fAssignsForInitiation = new HashSet(); //##85
    // fAssignsForInitiation is refered after the whole compile unit
    // has been scanned for initiation statements. //##87
    SubpType subptype = (SubpType)type; // Function type

    symRoot.subpCurrent = toSym.declareGlobalFunction(
      ast.getStorage(), subptype, subpname, true ); // Function declaration
    if( symRoot.subpCurrent!=null )
    {
      if( symRoot.subpCurrent.getHirBody()==null ) //SF021010
      {
        symRoot.symTableCurrent = symRoot.symTableRoot;
        symRoot.symTableCurrent.pushSymTable( symRoot.subpCurrent );
        nowSubpDef = hir.subpDefinition(
          symRoot.subpCurrent, symRoot.symTableCurrent );
        ((Program)hirRoot.programRoot).addSubpDefinition(nowSubpDef);

        // parameters
        BlockStmt paramblock = declareParameters( //SF030527
          ast.getArguments(), subptype.getParamTypeList() ); //SF030527
        // body
        nowDepth = 0;
        BlockStmt bodyblock = (BlockStmt)visit(ast.getRight());
        if( paramblock!=null )
          bodyblock.addFirstStmt(paramblock);
        bodyblock.setSubpBodyFlag(true);
        nowSubpDef.setHirBody(bodyblock);
        symRoot.subpCurrent.setDefinedFile(ast.fileName());
        symRoot.subpCurrent.setDefinedLine(ast.lineNumber());
      }
      else
      {
        toHir.error("function '"+subpname+"' is defined two or more times");
      }
    }
    if (toHir.fDbgLevel > 1) //##70
      message(3,"\n End of atFunction " + subpname + " reset SymTable"); //##70
     symRoot.subpCurrent = null;
    symRoot.symTableCurrent = symRoot.symTableRoot;
    nowSubpDef = null;
  }
  //-------------------------------------------------------------------
  ////////////////////SF030527[
  /**
   * Declare parameters.
   *
   * @param   decllist AST parameter declaration list.
   * @param   typelist HIR parameter type list.
   * @return  Parameter conversion block.
  **/
  private BlockStmt declareParameters(DeclaratorList decllist,IrList typelist)
  {
    BlockStmt paramblock = null;

    for( int index=1; decllist!=null; decllist=decllist.next(), index++ )
    {
      Declarator decl      = (Declarator)decllist.get();
      String     paramname = decl.getName().intern();
      Type       paramtype = toSym.convertType(decl.getType()
                             , false //##83
                            );
      Param      param     = null;

      if( toHir.searchLocalOrdinaryId(paramname)==null ) //SF041126
      {
        if( paramtype.getTypeKind()==Type.KIND_VOID ) //SF041126
          toHir.error("parameter '"+paramname+"' is declared as void");

        // Insert decay if the formal parameter is an array.
        if( paramtype.getTypeKind()==Type.KIND_VECTOR )
          paramtype = sym.pointerType(((VectorType)paramtype).getElemType());
        if (toHir.fDbgLevel > 1) //##70
          message(6,"PARAMETER NAME="+paramname+" TYPE="+paramtype);

        // If the parameter is an array, set array size to the pointer. //SF030620
        if( paramtype.getTypeKind()==Type.KIND_POINTER
        &&  decl.getArrayParamSize()>0 )
          paramtype = sym.pointerType(
            paramtype.getPointedType(), decl.getArrayParamSize() );

        if( typelist.size()==0 ) // Prototype missing.
        {
          // If it is an integral type and its precision is less than int,
          // then do integral promotion.
          if( toHir.isIntegral(paramtype)
          &&  paramtype.getTypeRank() < symRoot.typeInt.getTypeRank() )
            param = symRoot.symTableCurrent.generateParam(
              symRoot.typeInt, symRoot.subpCurrent );
          // If float then promote to double.
          else if( paramtype.getTypeKind()==Type.KIND_FLOAT )
            param = symRoot.symTableCurrent.generateParam(
              symRoot.typeDouble, symRoot.subpCurrent );
        }
        if( param!=null )
        {
          Var var = sym.defineVar( paramname, paramtype, symRoot.subpCurrent );
          var.setVisibility(Sym.SYM_PRIVATE);
          var.setStorageClass(Var.VAR_AUTO);
          if( paramblock==null )
            paramblock = hir.blockStmt(null);
          paramblock.addLastStmt(
            toHir.newExpStmt(
              hir.exp(HIR.OP_ASSIGN,hir.varNode(var),hir.varNode(param) ) ) );
        }
        else
        {
          param = sym.defineParam(paramname,paramtype);
        }
        param.setArrayParamSize(decl.getArrayParamSize()); //SF030531
        param.setVisibility(Sym.SYM_PRIVATE);
        param.setStorageClass(Var.VAR_AUTO);
        param.setParamIndex(index);
        param.setDefinedFile(decl.fileName());
        param.setDefinedLine(decl.lineNumber());
        symRoot.subpCurrent.addParam(param);
      }
      else
        toHir.error("parameter '"+paramname+"' is redeclared");
    }
    return paramblock;
  }
  ////////////////////SF030527]
  //-------------------------------------------------------------------
  /**
   * At Pair.
   * Pair is processed at upper level. Must not reach here.
   *
   * @param  ast Pair
  **/
  public void atPair(Pair ast)
  {
    toHir.fatal("atPair");
  }
  //-------------------------------------------------------------------
  // create HIR-C expression
  //-------------------------------------------------------------------
  /**
   * Create reference expression.
   *
   * @param  ast AddressExpr
  **/
  public void atAddressExpr(AddressExpr ast)
  {
    Exp  e = (Exp)visit( ast.getLeft() );
    nowHir = toHir.addrExp(e);
  }
  //-------------------------------------------------------------------
  /**
   * Create arithmetic binary expression.
   *
   * @param  ast ArithBinaryExpr
  **/
  public void atArithBinaryExpr(ArithBinaryExpr ast)
  {
    Exp e1 = pPromotion( (Exp)visit(ast.getLeft ()) );
    Exp e2 = pPromotion( (Exp)visit(ast.getRight()) );
    boolean lAdjustType = false;  //##84
    int op = 0;
    switch( ast.operatorId() ) // Decide HIR operator.
    {
    case'+'    : op = HIR.OP_ADD;  lAdjustType = true; break;
    case'-'    : op = HIR.OP_SUB;  lAdjustType = true; break;
    case'*'    : op = HIR.OP_MULT; lAdjustType = true; break;
    case'/'    : op = HIR.OP_DIV;  lAdjustType = true; break;
    case'%'    : op = HIR.OP_MOD;  lAdjustType = true; break;
    case'&'    : op = HIR.OP_AND;  lAdjustType = true; break;
    case'|'    : op = HIR.OP_OR;   lAdjustType = true; break;
    case'^'    : op = HIR.OP_XOR;  lAdjustType = true; break;
    case EQ    : op = HIR.OP_CMP_EQ; lAdjustType = true; break;
    case NEQ   : op = HIR.OP_CMP_NE; lAdjustType = true; break;
    case '>'   : op = HIR.OP_CMP_GT; lAdjustType = true; break;
    case GE    : op = HIR.OP_CMP_GE; lAdjustType = true; break;
    case '<'   : op = HIR.OP_CMP_LT; lAdjustType = true; break;
    case LE    : op = HIR.OP_CMP_LE; lAdjustType = true; break;
    case LSHIFT: op = HIR.OP_SHIFT_LL; break;
    case RSHIFT: {
      if (e1.getType().isUnsigned()) //##81
        op = HIR.OP_SHIFT_RL;
      else  //##81
        op = HIR.OP_SHIFT_R; //##81
      break; //SF021113
    }
    case ANDAND: op = HIR.OP_LG_AND; lAdjustType = true; break;
    case OROR  : op = HIR.OP_LG_OR;  lAdjustType = true; break;
    case ','   : op = HIR.OP_COMMA;  lAdjustType = true; break;
    default    : toHir.fatal("atArithBinaryExpr "+e1+" "+ast.operatorName()+" "+e2);
    }
    //##84 BEGIN
    boolean lAdjusted = false;
    if (lAdjustType) {
      Type lType1 = e1.getType();
      Type lType2 = e2.getType();
      if (lType1 != lType2) {
        if (toHir.fDbgLevel > 3)
          message(4, " adjustType " + lType1 + " " + lType2);
        Exp lExp1 = e1;
        Exp lExp2 = e2;
        /*
        if (lType1.getTypeRank() > lType2.getTypeRank()) {
          lExp2 = hir.convExp(lType1, lExp2);
        }else if (lType1.getTypeRank() < lType2.getTypeRank()) {
          lExp1 = hir.convExp(lType2, lExp1);
        }
        */
        Exp lPair = lExp1.adjustTypesOfBinaryOperands(lExp1, lExp2);
        lExp1 = (Exp)((Exp)lPair.getChild1()).copyWithOperands();
        lExp2 = (Exp)((Exp)lPair.getChild2()).copyWithOperands();
        nowHir = hir.exp(op, lExp1, lExp2);
        lAdjusted = true;
      }
    }
    if (! lAdjusted) {
      //##84 END
      nowHir = hir.exp(op, e1, e2);
    } //##84
  }
  //-------------------------------------------------------------------
  /**
   * Create arithmetic unary expression.
   *
   * @param  ast ArithUnaryExpr
  **/
  public void atArithUnaryExpr(ArithUnaryExpr ast)
  {
    Exp e1 = pPromotion( (Exp)visit((ASTree)ast.getLeft()) );
    switch( ast.operatorId() )
    {
    case'!': nowHir = hir.exp(HIR.OP_EQ_ZERO,e1); break;
    case'~': nowHir = hir.exp(HIR.OP_NOT,e1); break;
    case'+': nowHir = e1; break;
    case'-': nowHir = hir.exp(HIR.OP_NEG,e1); break;
    default: toHir.fatal("ArithUnary");
    }
  }
  //-------------------------------------------------------------------
  /**
   * Create array subscript expression.
   *
   * @param  ast ArrayExpr
  **/
  public void atArrayExpr(ArrayExpr ast)
  {
    Exp e1 = (Exp)visit(ast.getLeft());  // Array node. pPromotion is unnecessary
                                         // because Type is available for it.
    Exp e2 = (Exp)visit(ast.getRight()); // Index node.
    boolean use = toHir.useSubsForPtr;
    //##81 BEGIN
    if ((! use)&&(e1 instanceof VarNode)) {
      Var lVar = (Var)((VarNode)e1).getSymNodeSym();
      if (toHir.safeArrayAll ||
          ((toHir.symRoot.subpCurrent != null)&&
           toHir.symRoot.subpCurrent.isSafeArrayAll())||
          toHir.symRoot.safeArray.contains(lVar)) {
        use = true;
      }
    }
    //##81 END

    // If right hand side is array or pointer, exchange with left hand side.
    switch( e2.getType().getTypeKind() )
    {
    // If the index node (subscript exp) is pointer (or vector)
    // do not use SubscriptedExp. //##81
    case Type.KIND_POINTER:
    case Type.KIND_VECTOR:
      Exp e=e1; e1=e2; e2=e;
      use = false;
    }

    switch( e1.getType().getTypeKind() ) //SF020512
    {
    case Type.KIND_POINTER: // left is pointer   e1[e2] => undecay(e1)[e2]
      ////////SF020517[
      if( use
          || (e1.getType().isRestrict())) //##81
      {
        long size = 1, t=0; //SF030620
        if( toHir.useArrayParameterSize
           &&  0<(t=((PointerType)e1.getType()).getElemCount()) ) {
          size = t; // If element count is given, then use it
                    // else (i.e. []) assume ths size is 1. //##81
        }
        nowHir = toHir.subsExp( hir.undecayExp(e1,size), e2 );
      }
      else
      {
        nowHir = hir.contentsExp( hir.exp(HIR.OP_ADD,e1,e2) );
        toHir.setFlagPointerOperation(e1);
        toHir.setFlagPointerOperation(e2);
      }
      return;
      ////////SF020517]
    case Type.KIND_VECTOR: // left is array   h1[h2]
      nowHir = toHir.subsExp(e1,e2);
      return;
    default: // toHir.error
      toHir.error("[] requires array or pointer: "+ToC.tos(e1)+" [ "+ToC.tos(e2)+" ]" );
      if (toHir.fDbgLevel > 1) //##70
        message(6,"atArrayExpr  ARRAY="+e1.getType()+" INDEX="+e2.getType());
      // Tentatively set right hand side because appropriate expression is not given.
      nowHir = e2;
      return;
    }
  }
  //-------------------------------------------------------------------
  /**
   * Create assign expression.
   *
   * @param  ast AssignExpr
  **/
  public void atAssignExpr(AssignExpr ast)
  {
    Exp e1 = (Exp)visit(ast.getLeft());
    Exp e2 = pPromotion( (Exp)visit(ast.getRight()) );
    if (toHir.fDbgLevel > 3) //##84
      message(4,"atAssignExpr " + e1 + " = " + e2); //##84
    toHir.setFlagPointerOperation(e1);
    toHir.setFlagPointerOperation(e2);

    int op;
    switch( ast.operatorId() )
    {
    case '='     : op = HIR.OP_ASSIGN; break;
    case PLUS_E  : op = HIR.OP_ADD_ASSIGN; break;
    case MINUS_E : op = HIR.OP_SUB_ASSIGN; break;
    case MUL_E   : op = HIR.OP_MULT_ASSIGN; break;
    case DIV_E   : op = HIR.OP_DIV_ASSIGN; break;
    case MOD_E   : op = HIR.OP_MOD_ASSIGN; break;
    case LSHIFT_E: op = HIR.OP_SHIFT_L_ASSIGN; break;
    case RSHIFT_E: op = HIR.OP_SHIFT_R_ASSIGN; break;
    case AND_E   : op = HIR.OP_AND_ASSIGN; break;
    case OR_E    : op = HIR.OP_OR_ASSIGN; break;
    case EXOR_E  : op = HIR.OP_XOR_ASSIGN; break;
    default      : op = 0; toHir.fatal("AssignExpr");
    }
    toHir.setFlagValueIsAssigned(e1);
    nowHir = hir.exp(op,e1,e2);
    //message(6,ToC.tos((Exp)nowHir));
    if( e1.getType().getTypeKind()==Type.KIND_POINTER )
      nowHir.getFlag(HIR.FLAG_C_PTR);
  }
  //-------------------------------------------------------------------
  /**
   * Create function call expression.
   *
   * @param  ast CallExpr
  **/
  public void atCallExpr(CallExpr ast)
  {
    // visit function
    Expr funcExpr = ast.getFunction();
    Exp  funcExp;
    if( funcExpr instanceof VariableExpr )
    {
      String name = funcExpr.toString().intern();
      if( toHir.searchOrdinaryId(name)==null )
      {
        //SF040816 toHir.warning("implicitly declared function '"+name+"'");
        // Treat it as int function(...).
        SubpType type = (SubpType)toSym.convertType(astPrototype
                           , false //##83
                           );
        Subp subp = toSym.declareLocalFunction(S_EXTERN,type,name);
        funcExp = hir.subpNode(subp);
      }
      else
        funcExp = (Exp)visit( (ASTree)funcExpr );
    }
    else
      funcExp = (Exp)visit((ASTree)funcExpr);
    funcExp = pPromotion(funcExp);

    // visit arguments
    IrList arglist = hir.irList();
    for( ASTList al=ast.getArguments(); al!=null; al=al.tail() )
    {
      Exp e = pPromotion( (Exp)visit((ASTree)al.head()) );
      toHir.setFlagPointerOperation(e);
      arglist.add(e);
      e.setParent(arglist);
    }
    nowHir = funcExp!=null? hir.functionExp( funcExp, arglist ): null;
  }
  //-------------------------------------------------------------------
  /**
   * Create cast expression.
   *
   * @param  ast CastExpr
  **/
  public void atCastExpr(CastExpr ast)
  {
    nowHir = hir.convExp(
      toSym.convertType( ast.getType()
      , false //##83
      ),
      pPromotion( (Exp)visit(ast.getLeft()) ) );
  }
  //-------------------------------------------------------------------
  //##17 BEGIN
  /**
   * Create sizeof expression.
   *
   * @param  ast SizeofExpr
  **/
  public void atSizeofExpr(SizeofExpr ast)
  {
    Type lType;
    if( ast.getType()==null && ast.getExpr()!=null ) // sizeof(expression)
    {
      Exp lExp = (Exp)visit((ASTree)ast.getExpr());
      lType = lExp.getType();
    }
    else if( ast.getType()!=null && ast.getExpr()==null ) // sizeof(type)
    {
      lType = toSym.convertType(ast.getType()
               , false //##83
               );
    }
    else // Default type
    {
      lType = symRoot.typeInt;
    }
    nowHir = hir.intConstNode( lType.getSizeValue() );
  }
  //##17 END
  //-------------------------------------------------------------------
  /**
   * Create comma expression.
   *
   * @param  ast CommaExpr
  **/
  public void atCommaExpr(CommaExpr ast)
  {
    Exp e1 = (Exp)visit(ast.getLeft());
    Exp e2 = pPromotion( (Exp)visit(ast.getRight()) );
    toHir.setFlagPointerOperation(e2);
    nowHir = hir.exp(HIR.OP_COMMA,e1,e2);
  }
  //-------------------------------------------------------------------
  /**
   * Create conditional expression.
   *
   * @param  ast ConditionalExpr
  **/
  public void atConditionalExpr(ConditionalExpr ast)
  {
    Exp e1 = pPromotion( (Exp)visit((ASTree)ast.getCondition()) );
    Exp e2 = pPromotion( (Exp)visit((ASTree)ast.getThen()) );
    Exp e3 = pPromotion( (Exp)visit((ASTree)ast.getElse()) );
    toHir.setFlagPointerOperation(e2);
    toHir.setFlagPointerOperation(e3);
    nowHir = hir.exp(HIR.OP_SELECT,e1,e2,e3);
  }
  //-------------------------------------------------------------------
  /**
   * Create constant expression.
   *
   * @param  ast ConstantExpr
  **/
  public void atConstantExpr(ConstantExpr ast)
  {
    byte[] asttype = ast.getType();
    Type t = toSym.convertType(asttype
              , false //##83
              );

    Const c = null;
    switch( t.getTypeKind() )
    {
    case Type.KIND_CHAR:
    case Type.KIND_SHORT:
    case Type.KIND_INT:
      c = sym.intConst(ast.longValue(),symRoot.typeInt); //SF030412
      break;
    case Type.KIND_U_CHAR:
    case Type.KIND_U_SHORT:
    case Type.KIND_U_INT:
      c = sym.intConst(ast.longValue(),symRoot.typeU_Int); //SF030412
      break;
    case Type.KIND_LONG:
    case Type.KIND_LONG_LONG:
    case Type.KIND_U_LONG:
    case Type.KIND_U_LONG_LONG:
      c = sym.intConst(ast.longValue(),t);
      break;
    case Type.KIND_FLOAT:
    case Type.KIND_DOUBLE:
    case Type.KIND_LONG_DOUBLE:
      c = sym.floatConst(ast.doubleValue(),t);
      break;
    default:
      toHir.fatal("ConstantExpr type="+t);
    }
    if (toHir.fDbgLevel > 1) //##70
      message(6,"CONST="+c);
    nowHir = hir.constNode(c); //SF031003
  }
  //-------------------------------------------------------------------
  /**
   * Create dereference expression.
   *
   * @param  ast DereferenceExpr
  **/
  public void atDereferenceExpr(DereferenceExpr ast)
  {
    Exp e = pPromotion( (Exp)visit(ast.getLeft()) );
    nowHir = e!=null? hir.contentsExp(e): hir.nullNode();
    if( nowHir.getType()==null ) //SF020927
      nowHir.setType(symRoot.typeVoid); //SF020927
  }
  //-------------------------------------------------------------------
  /**
   * Looks for the element of the type.
   *
   * @param   type Struct/union type with element.
   * @param   name Element name.
   * @return  Found element.
  **/
  private Elem searchElement(Type type,String name)
  {
    IrList elemlist = type.getElemList();
    if( elemlist==null )
      toHir.fatal("searchElement");
    if( elemlist.size()==0 )
    {
      toHir.error( "dereferencing to incomplete type '"+type.getName()+"'" );
      return null;
    }
    for( int i=0; i<elemlist.size(); i++ )
    {
      Elem elem = (Elem)elemlist.get(i);
      if(elem.getName().equals(name))
        return elem;
    }
    toHir.error("'"+name+"' is not a struct/union member");
    return null;
  }
  //-------------------------------------------------------------------
  /**
   * Create member-access expression.
   *
   * @param  ast MemberExpr
  **/
  public void atMemberExpr(MemberExpr ast) // . ->
  {
    Exp    e1   = pPromotion( (Exp)visit(ast.getLeft()) );
    Type   t1   = e1.getType();
    String name = ast.name(); // member name
    switch( ast.operatorId() )
    {
    case ARROW:
      if( t1.getTypeKind()==Type.KIND_POINTER )
      {
        t1 = ((PointerType)t1).getPointedType();
        if( t1.getTypeKind()==Type.KIND_STRUCT
        ||  t1.getTypeKind()==Type.KIND_UNION )
        {
          Elem elem = searchElement(t1,name);
          if( elem!=null )
          {
            nowHir = hir.pointedExp(e1,hir.elemNode(elem));
            return;
          }
          else
            toHir.error("right side of -> requires element name: "
                       +ToC.tos((Exp)e1)+" . "+name);
        }
        else
          toHir.error("left side of -> requires pointer to struct/union: "
                     +ToC.tos((Exp)e1)+" -> "+name);
      }
      else
        toHir.error("left side of -> requires pointer: "
                    +ToC.tos((Exp)e1)+" -> "+name);
      nowHir = hir.nullNode();
      return;

    case '.':
      if( t1.getTypeKind()==Type.KIND_STRUCT
      ||  t1.getTypeKind()==Type.KIND_UNION )
      {
        Elem elem = searchElement(t1,name);
        if( elem!=null )
        {
          nowHir = hir.qualifiedExp(e1,hir.elemNode(elem));
          return;
        }
        else
          toHir.error("right side of . requires element name: "
                     +ToC.tos((Exp)e1)+" . "+name);
      }
      else
        toHir.error("left side of . requires struct/union: "
                   +ToC.tos((Exp)e1)+" . "+name);
      nowHir = hir.nullNode();
      return;

    default:
      toHir.fatal("MemberExpr");
    }
  }
  //-------------------------------------------------------------------
  /**
   * Create pointer binary expression.
   *
   * @param  ast PointerBinaryExpr
  **/
  public void atPointerBinaryExpr(PointerBinaryExpr ast)
  {
    Exp e1 = pPromotion( (Exp)visit(ast.getLeft ()) );
    Exp e2 = pPromotion( (Exp)visit(ast.getRight()) );
    int op = 0;
    switch( ast.operatorId() ) //SF020914
    {
    case'+':
      op = HIR.OP_ADD;
      if( e2.getType().getTypeKind()==Type.KIND_POINTER )
        { Exp e=e1; e1=e2; e2=e; } // If right side is a pointer, then exchange.
      break;
    case'-':
      op = HIR.OP_SUB;
      break;
    default:
      toHir.fatal("atPointerBinaryExpr: "+e1.getType()+ast.operatorName()+e2.getType());
    }

    if( toHir.isIntegral(e2.getType()) ) // If right side is integer,
    {                                    // then it is PTR+INT or PTR-IINT.
      nowHir = hir.exp(op,e1,e2);
      //message(6,"atPointerBinaryExpr  PTR="+e1+"("+r1+")  INT="+e2+"("+r2+")" );
      toHir.setFlagPointerOperation(e1);
      nowHir.setFlag(HIR.FLAG_C_PTR,true);
    }
    else // If right side is pointer then it is PTR-PTR.
    {
      nowHir = hir.exp(HIR.OP_OFFSET,e1,e2);
      //message(6,"atPointerBinaryExpr  PTR="+e1+"("+r1+")  PTR="+e2+"("+r2+")" );
    }
  }
  //-------------------------------------------------------------------
  /**
   * Create postfix expression.
   *
   * @param  ast PostfixExpr
  **/
  public void atPostfixExpr(PostfixExpr ast)
  {
    Exp e1 = (Exp)visit( (ASTree)ast.getLeft() );
    toHir.setFlagPointerOperation(e1);

    int op = 0;
    switch( ast.operatorId() )
    {
    case PLUSPLUS  : op = HIR.OP_POST_INCR; break;
    case MINUSMINUS: op = HIR.OP_POST_DECR; break;
    default        : toHir.fatal("PostfixExpr");
    }
    nowHir = hir.exp(op,e1);
  }
  //-------------------------------------------------------------------
  /**
   * Create prefix expression.
   *
   * @param  ast PrefixExpr
  **/
  public void atPrefixExpr(PrefixExpr ast)
  {
    Exp e1 = (Exp)visit( (ASTree)ast.getLeft() );
    toHir.setFlagPointerOperation(e1);

    int op = 0;
    switch( ast.operatorId() )
    {
    case PLUSPLUS  : op = HIR.OP_PRE_INCR; break;
    case MINUSMINUS: op = HIR.OP_PRE_DECR; break;
    default        : toHir.fatal("PrefixExpr");
    }
    nowHir = hir.exp(op,e1);
  }
  //-------------------------------------------------------------------
  /**
   * Create string literal expression.
   *
   * @param  ast StringLiteral
  **/
  public void atStringLiteral(StringLiteral ast)
  {
    String str = ast.get();
    reporter.isWideChar(ast instanceof WcharLiteral); //SF040816
    StringConst sc = sym.stringConst( str.intern() );
    nowHir = hir.constNode(sc);
  }
  //-------------------------------------------------------------------
  /**
   * Create variable expression.
   *
   * @param  ast VariableExpr
  **/
  public void atVariableExpr(VariableExpr ast)
  {
    String name = ast.toString().intern();

    Sym s = toHir.searchOrdinaryId(name);
    if( s==null )
    {
      toHir.error("undeclared identifier '"+name+"'");
      // Treat the undeclared identifier as an int variable.
      s = sym.defineVar(
        name, symRoot.typeInt, symRoot.symTableCurrent.getOwner() );
      ((Var)s).setVisibility(Sym.SYM_PRIVATE);
      ((Var)s).setStorageClass(Var.VAR_AUTO);
    }
    switch( s.getSymKind() )
    {
    ////////SF040816[
    //case Sym.KIND_VAR  : nowHir = hir.varNode  ((Var  )s); break;
    //case Sym.KIND_PARAM: nowHir = hir.varNode((Param)s); break;
    //case Sym.KIND_SUBP : nowHir = hir.subpNode ((Subp )s); break;
    //default            : toHir.fatal("atVariableExpr "+s.getSymKind());
    case Sym.KIND_VAR:
    case Sym.KIND_PARAM:
      reporter.isSupportedType((Var)s);
      nowHir = hir.varNode((Var)s);
      break;
    case Sym.KIND_SUBP:
      reporter.isVaArg((Subp)s);
      nowHir = hir.subpNode((Subp)s);
      break;
    default:
      toHir.fatal("atVariableExpr "+s.getSymKind());
    ////////SF040816]
    }
  }
  //-------------------------------------------------------------------
  // create HIR-C statement
  //-------------------------------------------------------------------
  /**
   * Create break statement.
   *
   * @param  ast: BreakStmnt
  **/
  public void atBreakStmnt(BreakStmnt ast)
  {
    if( breakLabel==null )
    {
      toHir.error("'break' without control structure");
      return;
    }
    nowHir = hir.jumpStmt(breakLabel);
  }
  //-------------------------------------------------------------------
  /**
   * Create case statement.
   *
   * @param  ast CaseLabel
  **/
  public void atCaseLabel(CaseLabel ast)
  {
    if( caseList==null )
    {
      toHir.error("'case' without 'switch'");
      return;
    }
    // create case constant and label
    long unusedbits = 64-8*ctrlExpType.getSizeValue();
    long value = ctrlExpType.isUnsigned()
               ? ast.getConstant()<<unusedbits>>>unusedbits
               : ast.getConstant()<<unusedbits>>unusedbits;
    Const c = sym.intConst(value,ctrlExpType); //SF041025
    Label l = toHir.createLabel();

    // check case constant repetition //SF041027
    for( ListIterator i=caseList.iterator(); i.hasNext(); )
      if( c==((ConstNode)((HirSeq)i.next()).getChild1()).getConstSym() )
      {
        toHir.error("duplicated case constant");
        break;
      }
    // add to case list
    ConstNode cn = hir.constNode(c);
    LabelNode ln = hir.labelNode(l);
    cn.setType(ctrlExpType); //SF041025

    caseList.add( hir.hirSeq(cn,ln) );
    nowHir = hir.labeledStmt(l,null);
  }
  //-------------------------------------------------------------------
  /**
   * Create continue statement.
   *
   * @param  ast ContinueStmnt
  **/
  public void atContinueStmnt(ContinueStmnt ast)
  {
    if( continueLabel==null )
    {
      toHir.error("'continue' without loop structure");
      return;
    }
    nowHir = hir.jumpStmt(continueLabel);
  }
  //-------------------------------------------------------------------
  /**
   * Create default statement.
   *
   * @param  ast DefaultLabel
  **/
  public void atDefaultLabel(DefaultLabel ast)
  {
    if( defaultLabel==null )
    {
      toHir.error("'default' without 'switch'");
      return;
    }
    nowHir = hir.labeledStmt(defaultLabel,null);
  }
  //-------------------------------------------------------------------
  /**
   * Create do-while statement.
   *
   * @param  ast DoStmnt
  **/
  public void atDoStmnt(DoStmnt ast)
  {
    Label cl = continueLabel; //push
    Label bl = breakLabel;
    continueLabel = toHir.createLabel();
    breakLabel    = toHir.createLabel();

    HIR h1 = visit((ASTree)ast.getBody());
    HIR h2 = visit((ASTree)ast.getExpr());
   //##62 nowHir = hir.untilStmt(
   nowHir = hir.repeatStmt( //##62
      null, (Stmt)h1, continueLabel, pPromotion((Exp)h2), breakLabel );

    continueLabel = cl; //pop
    breakLabel    = bl;
  }
  //-------------------------------------------------------------------
  /**
   * Create expression statement.
   *
   * @param  ast ExpressionStmnt
  **/
  public void atExpressionStmnt(ExpressionStmnt ast)
  {
    //##70 nowHir = hir.expStmt( pPromotion( (Exp)visit(ast.getLeft()) ) );
    //##70 BEGIN
    ASTree lLeft = ast.getLeft();
    if (toHir.fDbgLevel > 1) //##70
      toHir.message(4, "\n atExpressionStmnt " + ast +
                  " left " + lLeft + " right " + ast.getRight());
    if (lLeft != null) {
      HIR lHir = visit(ast.getLeft());
      if (lHir instanceof Exp)
        nowHir = hir.expStmt( pPromotion( (Exp)visit(ast.getLeft()) ) );
      else
        nowHir = lHir;
    }else {
      nowHir = hir.expStmt((Exp)visit(ast.getRight()));
    }
    if (toHir.fDbgLevel > 1) //##70
      toHir.message(6, " result " + nowHir.toStringWithChildren());
    //##70 END
  } // atExpressionStmnt
  //-------------------------------------------------------------------
  /**
   * Create for statement.
   *
   * @param  ast ForStmnt
  **/
  public void atForStmnt(ForStmnt ast)
  {
    Label cl = continueLabel; //push
    Label bl = breakLabel;
    continueLabel = toHir.createLabel();
    breakLabel    = toHir.createLabel();

    HIR h1 = visit((ASTree)ast.getInitializer());
    HIR h2 = visit((ASTree)ast.getCondition  ());
    HIR h3 = visit((ASTree)ast.getIteration  ());
    HIR h4 = visit((ASTree)ast.getBody       ());
    nowHir = hir.forStmt(
      h1!=null ? hir.expStmt((Exp)h1) : null,
      null,
      h2!=null ? pPromotion((Exp)h2) : toHir.new1Node(),
      (Stmt)h4,
      continueLabel,
      h3!=null ? hir.expStmt((Exp)h3) : null,
      breakLabel );

    continueLabel = cl; //pop
    breakLabel    = bl;
  }
  //-------------------------------------------------------------------
  /**
   * Create goto statement.
   *
   * @param  ast GotoStmnt
  **/
  public void atGotoStmnt(GotoStmnt ast)
  {
    nowHir = hir.jumpStmt( toHir.createLabel(ast.getLabel()) );
  }
  //-------------------------------------------------------------------
  /**
   * Create if statement.
   *
   * @param  ast IfStmnt
  **/
  public void atIfStmnt(IfStmnt ast)
  {
    HIR h1 = visit((ASTree)ast.getExpr());
    HIR h2 = visit((ASTree)ast.getThen());
    HIR h3 = visit((ASTree)ast.getElse());
    nowHir = hir.ifStmt( pPromotion((Exp)h1), (Stmt)h2, (Stmt)h3 );
  }
  //-------------------------------------------------------------------
  /**
   * Create label statement.
   *
   * @param  ast NamedLabel
  **/
  public void atNamedLabel(NamedLabel ast)
  {
    String name = ast.getName().intern();
    Label label = toHir.createLabel(name);
    if( label.getHirPosition()!=null ) //SF041126
      toHir.error("duplicated label declaration '"+name+"'"); //SF041126
    nowHir = hir.labeledStmt(label,null);
  }
  //-------------------------------------------------------------------
  /**
   * Create null statement.
   *
   * @param  ast NullStmnt
  **/
  public void atNullStmnt(NullStmnt ast)
  {
  }
  //-------------------------------------------------------------------
  /**
   * Create return statement.
   *
   * @param  ast ReturnStmnt
  **/
  public void atReturnStmnt(ReturnStmnt ast)
  {
    Exp e = (Exp)visit(ast.getLeft());
    nowHir = hir.returnStmt(pPromotion(e)); //SF041206
  }
  //-------------------------------------------------------------------
  /**
   * Create switch statement.
   *
   * @param  ast SwitchStmnt
  **/
  public void atSwitchStmnt(SwitchStmnt ast)
  {
    Type  cet = ctrlExpType; //SF041025
    IrList cl = caseList; //push
    Label  dl = defaultLabel;
    Label  bl = breakLabel;
    caseList     = hir.irList();
    defaultLabel = toHir.createLabel();
    breakLabel   = toHir.createLabel();

    HIR h1 = visit((ASTree)ast.getExpr());
    ctrlExpType = toHir.iPromotedType(h1.getType()); //SF041025

    HIR h2 = visit((ASTree)ast.getBody());
    nowHir = hir.switchStmt(
      pPromotion((Exp)h1), caseList, defaultLabel, (Stmt)h2, breakLabel );

    ctrlExpType  = cet; //SF041025
    caseList     = cl; //pop
    defaultLabel = dl;
    breakLabel   = bl;
  }
  //-------------------------------------------------------------------
  /**
   * Create while statement.
   *
   * @param  ast WhileStmnt
  **/
  public void atWhileStmnt(WhileStmnt ast)
  {
    Label cl = continueLabel; //push
    Label bl = breakLabel;
    continueLabel = toHir.createLabel();
    breakLabel    = toHir.createLabel();

    HIR h1 = visit((ASTree)ast.getExpr());
    HIR h2 = visit((ASTree)ast.getBody());
    nowHir = hir.whileStmt(
      null, pPromotion((Exp)h1), (Stmt)h2, continueLabel, breakLabel );

    continueLabel = cl; //pop
    breakLabel    = bl;
  }
  //-------------------------------------------------------------------
  // create zero initializer
  //-------------------------------------------------------------------
  /**
   * Create zero initializer for global variable.
   * If variable visiblity is not decided, decides it. If this compile unit
   * contains static function without definition, output error.
  **/
  private void createZeroInitializerForGlobal()
  {
    SymIterator i = symRoot.symTableRoot.getSymIterator();
    while( i.hasNext() )
    {
      Sym s = i.next();
      switch( s.getSymKind() )
      {
      case Sym.KIND_VAR:

        Var v = (Var)s;
        if( v.getVisibility()==-1 ) // If visibility is not defined, then
          v.setVisibility(Sym.SYM_PUBLIC); // assume to be public
        if( toHir.machineParam.initGlobalExplicitly()
                                              // If with global init flag
        &&  v.getVisibility()!=Sym.SYM_EXTERN // and without extern
        &&  v.getInitialValue()==null )       // and without initial value,
          createInitializer(v,null);          // then initiate by 0.

        Type t = v.getSymType();
        if( v.getVisibility()!=Sym.SYM_EXTERN
        ////////SF050215[
        //&&  v.getSymType().getTypeKind()==Type.KIND_VECTOR
        //&&  ((VectorType)v.getSymType()).getElemCount()==0 )
        //  toHir.warning(
        //    "array '"+v.getName()+"' is incomplete type or size 0");
        &&  t.getSizeValue()<=0 )
          toHir.warning( s, "'"+v.getName()+"' is incomplete type or size 0" );
        ////////SF050215]
        break;

      case Sym.KIND_SUBP:

        Subp subp = (Subp)s;
        if( subp.getVisibility()==Sym.SYM_COMPILE_UNIT // Static function
        &&  subp.getHirBody()==null ) // without function body is error.
          toHir.warning( s, "undefined static function '"+subp.getName()+"'" );
        break;
      }
    }
  }
  //-------------------------------------------------------------------
  /**
   * Create zero initializer for static variable.
  **/
  private void createZeroInitializerForStatic()
  {
    SymIterator i = symRoot.symTableCurrent.getSymIterator();
    while( i.hasNext() )
    {
      Sym s = i.next();
      switch( s.getSymKind() )
      {
      case Sym.KIND_VAR:
        Var v = (Var)s;
        if( v.getVisibility()!=Sym.SYM_EXTERN   // If not extern
        &&  v.getStorageClass()==Var.VAR_STATIC // and is static
        &&  v.getInitialValue()==null )         // and without initial value,
          createInitializer(v,null);            // then initiate by 0.
        break;
      }
    }
  }
  //-------------------------------------------------------------------
  // type conversion
  //-------------------------------------------------------------------
  /**
   * Pointer promotion (selfish naming).
   * Convert expression has VectorType/SubpType to PointerType.
   *
   * @param   e Expression which has VectorType/SubpType.
   * @return  Expression which has PointerType.
  **/
  public Exp pPromotion(Exp e)
  {
    if( e!=null && e.getType()!=null )
      switch( e.getType().getTypeKind() )
      {
      case Type.KIND_VECTOR:
        return toHir.decayExp(e);
      case Type.KIND_SUBP:
        return toHir.addrExp(e);
      }
    return e;
  }
  //-------------------------------------------------------------------
}

