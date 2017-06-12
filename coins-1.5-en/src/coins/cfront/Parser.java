/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
//##19 11/24-
//##3 : Changed when the Cparser was integrated with Coins compiler.
//##8 : Changed when control structure was changed (Sep. 2001).
//## Change
//##   Parser.java, Lex.java, ParseError.java,
//##   StdVisitor.java, Visitor.java.
//## Move LexTest.java, ParseTest.java to FrontTest.

package coins.cfront;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import coins.Debug;
import coins.IoRoot;
import coins.SymRoot;
import coins.ast.ASTList;
import coins.ast.ASTree;
import coins.ast.Aggregate;
import coins.ast.Declarator;
import coins.ast.DeclaratorList;
import coins.ast.Enum;
import coins.ast.Expr;
import coins.ast.Function;
import coins.ast.Pragma;
import coins.ast.Stmnt;
import coins.ast.Struct;
import coins.ast.TokenId;
import coins.ast.TypeId;
import coins.ast.Union;
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
import coins.ast.expr.LvalueExpr;
import coins.ast.expr.MemberExpr;
import coins.ast.expr.PointerBinaryExpr;
import coins.ast.expr.PostfixExpr;
import coins.ast.expr.PrefixExpr;
import coins.ast.expr.SizeofExpr;
import coins.ast.expr.StringLiteral;
import coins.ast.expr.VariableExpr;
import coins.ast.expr.WcharLiteral;
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
import coins.ast.stmnt.TreeStmnt;
import coins.ast.stmnt.WhileStmnt;
import coins.casttohir.ToHirC;
import coins.ir.hir.Exp;
import coins.ir.hir.HIR;
import coins.sym.Const;
import coins.sym.IntConst;

/**
 * Parser for the C language.
 */
public class Parser
  implements TokenId, TypeId
{
  /**
   * Used to generate an identifier that never appears in a source
   * program.
   */
  public static final String invalidCChar = "#";

  final Debug debug; //SF041014
  final Lex lex; //##27
  Evaluator evaluator; //SF041014
  LinkedList pragmaList; //SF050304

  private int uniqueId = 0;
  private int errorCounter;
  /**
   * symTable contains byte[]       as typedefed type.
   *                   ConstantExpr as enumeration.
   *                   Declaration  as variable.
   **/
  private SymbolTable symTable;
  private SymbolTable tagTable; // struct/union

  private final ToHirC toHirC; // Used for computing aggregate type size.
  private final SymRoot symRoot;
  public final int fDbgLevel; //##67

  /**
   * Constructs a parser.
   *
   * @param lex        a lexical analyzer
   */
  //SF031217 public Parser(Lex lex) {
  public Parser(IoRoot ioroot, Lex lex, ToHirC tohirc)
  { //SF031217
    symRoot = ioroot.symRoot;
    debug = ioroot.dbgParse;
    this.lex = lex;
    toHirC = tohirc; //SF031217

    symTable = new SymbolTable();
    tagTable = new SymbolTable();
    errorCounter = 0;
    evaluator = new Evaluator(symRoot.machineParam); //SF041014
    pragmaList = new LinkedList();

    lex.parser = this; //SF031217
    fDbgLevel = ioroot.dbgParse.getLevel(); //##67
    if (fDbgLevel > 0)  //##70
      debug.print(1, "\nParser "); //##70
  }

  /**
   * The command string for invoking a preprocessor.
   */
  public static String cppCommand = "gcc -E";

  /**
   * Runs a preprocessor (gcc -E) and returns the input stream obtaining
   * the output of the preprocessor.
   */
  public static InputStream runCpp(String source) throws IOException
  {
    Process p = Runtime.getRuntime().exec(cppCommand + " " + source);
    return p.getInputStream();
  }

  boolean isTypedefedType(String typename)
  {
    return symTable.get(typename.intern())instanceof byte[];
  }

  /**
   * Parses the whole source file.  This method repeatedly reads
   * a top-level declaration and invokes <code>Backend.compile()</code>.
   * See cfront.Backend#compile(ast.ASTList)
   *
   * @param verbose        true if an epilogue message is printed out.
   */
  public void parse(Backend backend, boolean verbose) throws IOException
  {
    try {
      if (fDbgLevel > 0) { //##70
        debug.print(1, "\nparse "); //##70
      }
      while (hasNext()) {
        ASTList tree = read();
        backend.compile(tree);
      }
    }
    catch (StopException e) {
      // no messages are printed out.
    }

    backend.doEpilogue();
    if (verbose) {
      showEpilogue();
    }
  }

  /**
   * Returns false if the parser reaches the end of file.
   */
  public boolean hasNext() throws IOException
  {
    return lex.lookAhead() != EOF;
  }

  /**
   * Creates a <code>Declarator</code> object.
   *
   * This method should be overwritten if the client provides
   * a subclass of
   * <code>Declarator</code> and the parser must instantiate that
   * subclass.  The implementation in this class is as follows:
   *
   * <ul><pre>return new Declarator(name, fname, line);</pre></ul>
   */
  protected Declarator makeDeclarator(String name, String fname, int line)
  {
    if (fDbgLevel > 3) { //##67
      debug.print(4, " makeDeclarator " + name); //####
    }
    return new Declarator(name, fname, line);
  }

  /**
   * Creates a <code>Function</code> object.
   *
   * This method should be overwritten if the client provides
   * a subclass of
   * <code>Function</code> and the parser must instantiate that
   * subclass.  The implementation in this class is as follows:
   *
   * <ul><pre>return new Function(decl, body);</pre></ul>
   */
  protected Function makeFunction(Declarator decl, Stmnt body)
  {
    if (fDbgLevel > 3) { //##70
      debug.print(4, "makeFunction "); //##70
    }
    return new Function(decl, body);
  }

  /**
   * Creates a <code>Struct</code> object.
   *
   * This method should be overwritten if the client provides
   * a subclass of
   * <code>Struct</code> and the parser must instantiate that
   * subclass.  The implementation in this class is as follows:
   *
   * <ul><pre>return new Struct(name, mems, fname, line);</pre></ul>
   */
  protected Struct makeStruct(String name, DeclaratorList mems,
    String fname, int line)
  {
    if (fDbgLevel > 3)  //##70
      debug.print(4, " makeStruct " + name); //##70
    //##15 return new Struct(name, mems, fname, line);
    //SF031217 return new Struct(name, mems, fname, line, lex.hirRoot); //##15
    return new Struct(name, mems, fname, line, toHirC); //SF031217
  }

  /**
   * Creates a <code>Union</code> object.
   *
   * This method should be overwritten if the client provides
   * a subclass of
   * <code>Union</code> and the parser must instantiate that
   * subclass.  The implementation in this class is as follows:
   *
   * <ul><pre>return new Union(name, mems, fname, line);</pre></ul>
   */
  protected Union makeUnion(String name, DeclaratorList mems,
    String fname, int line)
  {
    if (fDbgLevel > 3)  //##70
      debug.print(4, " makeUnion " + name); //##70
    //##27 return new Union(name, mems, fname, line);
    //SF031217 return new Union(name, mems, fname, line, lex.hirRoot); //##27
    return new Union(name, mems, fname, line, toHirC); //SF031217
  }

  protected void showErrorMessage(ParseError e)
  {
    ++errorCounter;
    System.err.println(e.getMessage());
  }

  public void showErrorMessage(Stmnt pos, String message)
  {
    ++errorCounter;
    System.err.print(pos.fileName());
    System.err.print(':');
    System.err.print(pos.lineNumber());
    System.err.print(' ');
    System.err.println(message);
  }

  public void showWarningMessage(String message)
  {
    System.err.print(lex.getFileName());
    System.err.print(':');
    System.err.print(lex.getLineNumber());
    System.err.print(" (Warning) ");
    System.err.println(message);
  }

  public void showEpilogue()
  {
    System.err.println(errorCounter + " error(s)");
  }

  public void recordSymbol(String name, Declarator d)
  {
    if (fDbgLevel > 3) { //##67
      debug.print(4, " recordSymbol", name + d.toString()); //### //##19

    }
    symTable.put(name, d);
    if (name.charAt(0) == '_') { //##51
      symRoot.conflictingSpecialSyms.add(name); //##51
    }
  }

  private String uniqueName()
  {
    String name = "_" + invalidCChar + uniqueId++;
    return name.intern();
  }

  protected void enterNewEnvironment()
  {
    if (fDbgLevel > 3) { //##67
      debug.print(4, "\n enterNewEnvironment "); //###
    }
    symRoot.symTableCurrent.pushSymTable(null); //SF031217
    symTable = new SymbolTable(33, symTable);
    tagTable = new SymbolTable(33, tagTable);
  }

  protected void exitEnvironment()
  {
    if (fDbgLevel > 3) { //##67
      debug.print(4, "\n exitEnvironment "); //###
    }
    symRoot.symTableCurrent.popSymTable(); //SF031217
    symTable = symTable.getParent();
    tagTable = tagTable.getParent();
  }

  protected Aggregate lookupEncodedTag(String tagName)
  {
    if (fDbgLevel > 3)  //##70
      debug.print(4, " lookupEncodedTag " + tagName); //##70
    // SymbolTable deals with a given key as a canonical representation
    // for the String object.
    tagName = tagName.intern();

    Aggregate agg = null;
    Object found = null;
    SymbolTable st = tagTable;
    do {
      if (found instanceof Aggregate) {
        agg = (Aggregate)found;

      }
      found = st.get(tagName);
      st = st.getParent();
    }
    while (found != null && st != null);
    if (found instanceof Aggregate) { //##17
      agg = (Aggregate)found; //##17
    }
    return agg;
  }

  /**
   * Returns a new unique tag name if the given tag name overrides
   * another one declared in an outer scope.
   */
  protected String toEncodedTag(String tagName)
  {
    if (fDbgLevel > 3)  //##70
      debug.print(4, " toEncodedTag " + tagName); //##70
    // SymbolTable deals with a given key as a canonical representation
    // for the String object.
    Object found = tagTable.get(tagName.intern());
    if (found == null) {
      return tagName;
    }
    else if (found instanceof Aggregate) {
      return ((Aggregate)found).name();
    }
    else {
      return (String)found;
    }
  }

  protected String decodeTagName(String tagName)
  {
    //##18 return tagName.substring(0, tagName.indexOf(invalidCChar));
    int index = tagName.indexOf(invalidCChar);
    if (index > 0) { //##18
      return tagName.substring(0, index); //##18
    }
    else { //##18
      return tagName; //##18
    }
  }

  protected String getNewEncodedTag(String name)
  {
    SymbolTable parent = tagTable.getParent();
    if (parent == null) {
      return name;
    }
    else {
      // SymbolTable deals with a given key as a canonical
      // representation for the String object.
      while (parent.get(name.intern()) != null) {
        name = name + invalidCChar;

      }
      return name.intern();
    }
  }

  /**
   * Declares that a struct/union will be declared later in the current
   * name scope.
   */
  protected String recordTag(String tagName) throws ParseError
  {
    if (fDbgLevel > 3)  //##70
      debug.print(4, " recordTag " + tagName); //##70
    // SymbolTable deals with a given key as a canonical
    // representation for the String object.
    tagName = tagName.intern();

    String encodedName = getNewEncodedTag(tagName);
    if (tagTable.get(encodedName) != null) {
      return encodedName;
    }

    if (tagTable.put(tagName, encodedName) != null) {
      throw new ParseError(lex,
        "fatal error (this should never happen)");
    }
    if (tagName.charAt(0) == '_') { //##51
      symRoot.conflictingSpecialSyms.add(tagName); //##51
    }
    return encodedName;
  }

  protected void recordTag(String tagName, Aggregate a) throws ParseError
  {
    if (fDbgLevel > 3)  //##70
      debug.print(4, " recordTag " + tagName); //##70
    // SymbolTable deals with a given key as a canonical
    // representation for the String object.

    tagName = tagName.intern();
    Object found = tagTable.put(tagName, a);
    if (tagName.charAt(0) == '_') { //##51
      symRoot.conflictingSpecialSyms.add(tagName); //##51
    }
    if (found != null && found instanceof Aggregate) {
    //##70  throw new ParseError(lex, "struct/union tag " + tagName
    //##70    + " was redefined.");
    showWarningMessage("struct/union tag " + tagName
            + " was redefined."); //##70
    }

    String name2 = a.name().intern();
    if (name2 != tagName) {
      tagTable.put(name2, a);
    }
    if (name2.charAt(0) == '_') { //##51
      symRoot.conflictingSpecialSyms.add(name2); //##51
    }
  }

  //##22 public int sizeofStruct(String tagName) throws ParseError {
  public long sizeofStruct(String tagName) throws ParseError
  { //##22
    if (fDbgLevel > 3)  //##70
      debug.print(4, " sizeofStruct " + tagName); //##70
    long lSize; //##27
    Aggregate agg = lookupEncodedTag(tagName);
    if (agg == null) {
      return -1;
    }
    else if (agg instanceof Struct) {
      lSize = ((Struct)agg).getSize(); //##27
      if (fDbgLevel > 3) { //##67
        debug.print(4, " sizeofStruct " + lSize); //##27
      }
      return lSize; //##27
    }
    else if (agg instanceof Union) {
      lSize = ((Union)agg).getSize(); //##27
      if (fDbgLevel > 3) { //##67
        debug.print(4, " sizeofUnion " + lSize); //##27
      }
      return lSize; //##27
    }
    else {
      throw new ParseError(lex, "wrong tag name");
    }
  }

  //##22public int sizeofUnion(String tagName) throws ParseError  {
  public long sizeofUnion(String tagName) throws ParseError
  { //##22
    return sizeofStruct(tagName);
  }

  private void skipChar(char c) throws ParseError, IOException
  {
    if (lex.get() != c) {
      if (fDbgLevel > 3)  //##70
        debug.print(4, " skipChar " + c); //##70
      throw new ParseError(lex, c);
    }
  }

  /**
   * Reads a top-level declaration and returns a parse tree.
   * This method returns a LIST of parse tree because it may
   * divide the given declaration into a few simple declaration
   * although the number of elements of the returned list is
   * usually one.  For example,
   *
   * <ul><pre>struct A {
   *     int a;
   * } var_a;</pre></ul>
   *
   * is divided into the following two simple declarations:
   *
   * <ul><pre>struct A { int a; };    // type declaration
   * struct A var_a;         // variable declaration</pre></ul>
   *
   * <p>Note that this method may return null.
   */
  //## private ASTList read() throws IOException { //##3 Tan Original
  public ASTList read() throws IOException
  { //##3 Tan
    try {
      //SF050304[
      //return definition();
      if (fDbgLevel > 3) //##70
        debug.print(4, "read ", lex.lookAhead() + " "+(char)lex.lookAhead()
                    + " " + lex.getString()); //##70
      return CompoundStmnt.concat(pragmaList(), definition());
      //SF050304]
    }
    catch (ParseError err) {
      showErrorMessage(err);
      ASTList result = new CompoundStmnt(
        new NullStmnt(lex.getFileName(),
        lex.getLineNumber()));
      for (; ; ) {
        int t;
        do {
          t = lex.get();
          if (t == EOF) {
            return result;
          }
        }
        while (t != '}');
        try {
          return definition();
        }
        catch (ParseError err2) {
        }
      }
    }
  }

  //SF050304[
  private CompoundStmnt pragmaList() throws IOException
  {
    if (fDbgLevel > 3)  //##70
      debug.print(4, " pragmaList "); //##70
    lex.lookAhead(); // read pragma

    CompoundStmnt list = null;
    while (pragmaList.size() > 0) {
      list = new CompoundStmnt((Pragma)pragmaList.removeLast(), list);
    }
    return list;
  }

  //private ASTList appendToPragmaList(ASTList list,ASTree ast)
  //{
  //  if( list==null )
  //    return ast;
  //
  //  ASTList l = list;
  //  while( l.tail()!=null )
  //    l = l.tail();
  //  l.setRight(ast);
  //  return list;
  //}
  //SF050304]

  /** definition
   * <pre>
   * definition
   * : ';'
   * | typdef.declaration
   * | toplevel.declaration
   *
   * This method may return null.
   */
  private CompoundStmnt definition() throws ParseError, IOException
  {
    if (fDbgLevel > 3)  //##70
      debug.print(4, " definition"); //##70
    int t = lex.lookAhead();
    if (fDbgLevel > 3)  //##67
      debug.print(4, "definition ", t + " "+(char)t); //###
    if (t == ';') {
      lex.get();
      return new CompoundStmnt(new NullStmnt(lex.getFileName(),
        lex.getLineNumber()));
    }
    else if (t == TYPEDEF) {
      return typdefDeclaration();
    }
    else {
      return toplevelDeclaration();
    }
  }

  /** typedef.declaration
   * <pre>
   * typedef.declaration
   * : TYPEDEF type.specifier declarator.list ';'
   */
  private CompoundStmnt typdefDeclaration() throws ParseError, IOException
  {
    if (fDbgLevel > 3)  //##70
      debug.print(4, " typedefDeclaration "); //##70
    if (lex.get() != TYPEDEF) {
      throw new ParseError(lex);
    }

    String filename = lex.getFileName();
    int lineNumber = lex.getLineNumber();

    EncodedType etype = new EncodedType();
    CompoundStmnt types = typeSpecifier(etype, null);
    CompoundStmnt decls = declaratorList(etype, /*0,*/ false);
    CompoundStmnt list = decls;
    while (list != null) {
      Declarator d = (Declarator)list.head();
      if (d.getBitFieldSize() > 0) {
        throw new ParseError(lex, filename, lineNumber, "bad bit field"); //##8
      }

      d.setTypedefed(true);
      symTable.put(d.getName(), d.getType());
      if (d.getName().charAt(0) == '_') { //##51
        symRoot.conflictingSpecialSyms.add(d.getName()); //##51
      }
      list = list.next();
    }

    skipChar(';');
    return CompoundStmnt.concat(types, decls);
  }

  /** type.specifier
   * <pre>
   * type.specifier
   * : cv.qualifier type.name.or.struct cv.qualifier
   */
  private CompoundStmnt typeSpecifier(EncodedType etype,
    CompoundStmnt typeDecls) throws ParseError, IOException
  {
    if (fDbgLevel > 3) { //##67
      debug.print(4, "typeSpecifier ", etype.toString()); //###
      ////////SF041212[
      //final String emsg = "both const and volatile are specified.";
      //int cv = cvQualifier();
      //typeDecls = typeNameOrStruct(etype, typeDecls);
      //declaratorArray(etype, false); //##19 Permit int[2].
      //int cv2 = cvQualifier();
      //if (cv != cv2 && cv != 0 && cv2 != 0)
      //    throw new ParseError(lex, emsg);
      //else if (cv == 0)
      //    cv = cv2;
      //
      //char topChar = etype.top();
      //if (topChar == CONST_T) {
      //    if (cv == VOLATILE)
      //        throw new ParseError(lex, emsg);
      //}
      //else if (topChar == VOLATILE_T) {
      //    if (cv == CONST)
      //        throw new ParseError(lex, emsg);
      //}
      //else
      //    etype.insertCv(cv);
      //!!int cv = cvQualifier();
      //!!int cv2 = cvQualifier();
    }
    typeDecls = typeNameOrStruct(etype, typeDecls);
    declaratorArray(etype, false); //##19 Permit int[2].
    //!!int cv3 = cvQualifier();
    //!!int cv4 = cvQualifier();
    //!!if( cv==CONST || cv2==CONST || cv3==CONST || cv4==CONST )
    //!!    etype.insertCv(CONST);
    //!!if( cv==VOLATILE || cv2==VOLATILE || cv3==VOLATILE || cv4==VOLATILE )
    //!!    etype.insertCv(VOLATILE);
    ////////SF041212]
    return typeDecls;
  }

  //SF041212[
  ///** cv.qualifier
   // * <pre>
   // * cv.qualifier
   // * : [ CONST | VOLATILE ]
   // *
   // * @return CONST, VOLATILE, or 0.
   // */
  //private int cvQualifier() throws IOException {
  //    /* kluge: we simply ignore redundant const/volatile.
   //     */
  //    int result = 0;
  //    int r;
  //    do {
  //        r = cvQualifier2();
  //        if (result == 0 || result == CONST)
  //            result = r;
  //    } while (r != 0);
  //    return result;
  //}
  //
  //private int cvQualifier2() throws IOException {
  //    int result = 0;
  //    int t = lex.lookAhead();
  //    if (t == CONST)
  //        result = CONST;
  //    else if (t == VOLATILE)
  //        result = VOLATILE;
  //    else
  //        return 0;
  //
  //    lex.get();
  //    return result;
  //}
  private void typeQualifier(EncodedType etype) throws ParseError, IOException
  {
    if (fDbgLevel > 3)  //##70
      debug.print(4, " typeQualifier "); //##70
    int constcount = 0;
    int volatilecount = 0;
    int restrictcount = 0; //##81

    for (; ; ) { // read type qualifiers
      switch (lex.lookAhead()) {
        case CONST:
          constcount++;
          lex.get();
          continue;
        case VOLATILE:
          volatilecount++;
          lex.get();
          continue;
        case RESTRICT:
          restrictcount++;
          lex.get();
          continue;
      }
      break;
    }

    if (constcount > 1) {
      throw new ParseError(lex, "duplicated const");
    }
    if (volatilecount > 1) {
      throw new ParseError(lex, "duplicated volatile");
    }
    if (restrictcount > 1) {
      throw new ParseError(lex, "duplicated restrict"); //##81
    }
    if (constcount > 0) {
      etype.insertCv(CONST);
    }
    if (volatilecount > 0) {
      etype.insertCv(VOLATILE);
    }
    if (restrictcount > 0) {    //##81
      etype.insertCv(RESTRICT); //##81
    }                           //##81
  }

  //SF041212]

  /** type.name.or.struct
   * <pre>
   * type.name.or.struct
   * : integral.type
   * | TYPEDEF_NAME
   * | struct.decl
   * | union.decl
   * | enum.decl
   *
   * integral.type
   * : VOID | FLOAT | [ LONG ] DOUBLE
   * | SIGNED | UNSIGNED
   * | ( SIGNED | UNSIGNED ) int.type
   *
   * int.type
   * : CHAR | INT | SHORT INT | SHORT
   * | LONG INT | LONG | LONG LONG INT | LONG LONG
   */
  private CompoundStmnt typeNameOrStruct(EncodedType etype,
    CompoundStmnt typeDecls) throws ParseError, IOException
  {
    if (fDbgLevel > 3) { //##67
      debug.print(4, "typeNameOrStruct ", etype.toString()); //###

      ////////SF041212[
      //int t = lex.get();
      //if (t == TYPEDEF_NAME)
      //    return typedefName(etype, typeDecls);
      //else if (t == STRUCT)
      //    return structDecl(etype, false, typeDecls);
      //else if (t == UNION)
      //    return structDecl(etype, true, typeDecls);
      //else if (t == ENUM)
      //    return enumDecl(etype, typeDecls);
      //else if (t == VOID)
      //    etype.insert(VOID_T);
      //else if (t == FLOAT)
      //    etype.insert(FLOAT_T);
      //else if (t == DOUBLE)
      //    etype.insert(DOUBLE_T);
      //else if (t == LONG && lex.lookAhead() == DOUBLE) {
      //    etype.insert(LONG_DOUBLE_T);
      //    lex.get();
      //}
      //else {
      //    char sign;
      //    if (t == SIGNED)
      //        sign = SIGNED_T;
      //    else if(t == UNSIGNED)
      //        sign = UNSIGNED_T;
      //    else
      //        sign = 0;
      //
      //    if (sign != 0) {
      //        t = lex.lookAhead();
      //        if (t != CHAR && t != INT && t != SHORT && t != LONG)
      //            t = INT;
      //        else
      //            t = lex.get();
      //    }
      //
      //    if (t == CHAR)
      //        etype.insert(CHAR_T);
      //    else if (t == INT)
      //        etype.insert(INT_T);
      //    else if (t == SHORT) {
      //        sign = signedOrUnsigend(sign);
      //        if (lex.lookAhead() == INT)
      //            lex.get();
      //
      //        etype.insert(SHORT_T);                // short ((un)signed) (int)
      //    }
      //    else if (t == LONG) {
      //        sign = signedOrUnsigend(sign);        // long (un)signed ...
      //        int t2 = lex.lookAhead();
      //        if (t2 == INT) {
      //            lex.get();
      //            etype.insert(LONG_T);        // long int
      //        }
      //        else if (t2 == LONG) {
      //            lex.get();
      //            sign = signedOrUnsigend(sign); // long long (un)signed ..
      //            if (lex.lookAhead() == INT)
      //                lex.get();
      //
      //            etype.insert(LONG_LONG_T);        // long long (int)
      //        }
      //        else
      //            etype.insert(LONG_T);        // long
      //    }
      //    else { // Assume INT as default.  //##19 11.24
      //        etype.insert(INT_T); //##19 11.24
      //        //##19 throw new ParseError(lex); // 11.24
      //    } //##19 11.24
      //
      //    if (sign != 0)
      //        etype.insert(sign);
      //}

      // Read one of following items:
      //   typedef name
      //   struct
      //   union
      //   enum
      //   void
      //   [signed|unsigned] char
      //   [signed|unsigned] [short|long|long long] [int]
      //   float
      //   [long] double
      // and coexistence of following items:
      //   extern
      //   static
      //   auto
      //   register
      //   inline
      //   const
      //   volatile

    }
    int externcount = 0;
    int staticcount = 0;
    int autocount = 0;
    int registercount = 0;
    int inlinecount = 0;
    int constcount = 0;
    int volatilecount = 0;
    int restrictcount = 0; //##81
    int signedcount = 0;
    int unsignedcount = 0;
    int shortcount = 0;
    int longcount = 0;
    int type = 0; // 0/VOID/INT/CHAR/FLOAT/DOUBLE/TYPEDEF_NAME/STRUCT/UNION/ENUM

    for (; ; ) { // read declaration specifiers
      switch (lex.lookAhead()) {
        case EXTERN:
          externcount++;
          lex.get();
          continue;
        case STATIC:
          staticcount++;
          lex.get();
          continue;
        case AUTO:
          autocount++;
          lex.get();
          continue;
        case REGISTER:
          registercount++;
          lex.get();
          continue;
        case INLINE:
          inlinecount++;
          lex.get();
          continue;
        case CONST:
          constcount++;
          lex.get();
          continue;
        case VOLATILE:
          volatilecount++;
          lex.get();
          continue;
        case RESTRICT:     //##81
          restrictcount++; //##81
          lex.get();       //##81
          continue;        //##81
        case SIGNED:
          signedcount++;
          lex.get();
          continue;
        case UNSIGNED:
          unsignedcount++;
          lex.get();
          continue;
        case SHORT:
          shortcount++;
          lex.get();
          continue;
        case LONG:
          longcount++;
          lex.get();
          continue;
        case VOID:
        case INT:
        case CHAR:
        case FLOAT:
        case DOUBLE:
          if (type != 0) {
            throw new ParseError(lex, "two or more data types");
          }
          type = lex.get();
          continue;
        case TYPEDEF_NAME:

          //FS041220[
          //if( type!=0 )
          //  throw new ParseError(lex,"two or more data types");
          if (type != 0 ||
              signedcount + unsignedcount + shortcount + longcount > 0) {
            break;
          }

          //FS041220]
          type = lex.get();
          typeDecls = typedefName(etype, typeDecls);
          continue;
        case STRUCT:
          if (type != 0) {
            throw new ParseError(lex, "two or more data types");
          }
          type = lex.get();
          typeDecls = structDecl(etype, false, typeDecls);
          continue;
        case UNION:
          if (type != 0) {
            throw new ParseError(lex, "two or more data types");
          }
          type = lex.get();
          typeDecls = structDecl(etype, true, typeDecls);
          continue;
        case ENUM:
          if (type != 0) {
            throw new ParseError(lex, "two or more data types");
          }
          type = lex.get();
          typeDecls = enumDecl(etype, typeDecls);
          continue;
      }
      break;
    }

    if (externcount > 1) {
      throw new ParseError(lex, "duplicate extern");
    }
    if (staticcount > 1) {
      throw new ParseError(lex, "duplicate static");
    }
    if (autocount > 1) {
      throw new ParseError(lex, "duplicate auto");
    }
    if (registercount > 1) {
      throw new ParseError(lex, "duplicate register");
    }
    if (inlinecount > 1) {
      throw new ParseError(lex, "duplicate inline");
    }
    //##85 if (externcount + staticcount + autocount + registercount + inlinecount > 1) {
    if (externcount + staticcount + autocount + registercount > 1) { //##85
      throw new ParseError(lex, "multiple storage classes in declaration");
    }
    if (constcount > 1) {
      throw new ParseError(lex, "duplicated const");
    }
    if (volatilecount > 1) {
      throw new ParseError(lex, "duplicated volatile");
    }
    if (restrictcount > 1) {
      throw new ParseError(lex, "duplicated restrict");
    }

    switch (type) {
      case VOID: // void
        if (signedcount > 0) {
          throw new ParseError(lex, "signed specified with void");
        }
        if (unsignedcount > 0) {
          throw new ParseError(lex, "unsigned specified with void");
        }
        if (shortcount > 0) {
          throw new ParseError(lex, "short specified with void");
        }
        if (longcount > 0) {
          throw new ParseError(lex, "long specified with void");
        }
        etype.insert(VOID_T);
        break;
      case 0: // [signed|unsigned] [short|long|long long] [int]
      case INT:
        if (signedcount > 1) {
          throw new ParseError(lex, "duplicate signed");
        }
        if (unsignedcount > 1) {
          throw new ParseError(lex, "duplicate unsigned");
        }
        if (signedcount + unsignedcount > 1) {
          throw new ParseError(lex, "both signed and unsigned specified");
        }
        if (shortcount > 1) {
          throw new ParseError(lex, "duplicate short");
        }
        if (longcount > 2) {
          throw new ParseError(lex, "a lot of long specified");
        }
        if (shortcount > 0 && longcount > 0) {
          throw new ParseError(lex, "both long and short specified");
        }
        etype.insert(shortcount == 1 ? SHORT_T
          : longcount == 1 ? LONG_T
          : longcount == 2 ? LONG_LONG_T
          : INT_T);
        etype.insert(unsignedcount == 1 ? UNSIGNED_T : SIGNED_T);
        break;
      case CHAR: // [signed|unsigned] char
        if (signedcount > 1) {
          throw new ParseError(lex, "duplicate signed");
        }
        if (unsignedcount > 1) {
          throw new ParseError(lex, "duplicate unsigned");
        }
        if (signedcount + unsignedcount > 1) {
          throw new ParseError(lex, "both signed and unsigned specified");
        }
        if (shortcount + longcount > 0) {
          throw new ParseError(lex, "long or short specified with char");
        }
        etype.insert(CHAR_T);
        etype.insert(signedcount == 1 ? SIGNED_T
          : unsignedcount == 1 ? UNSIGNED_T
          : symRoot.getCharType() == symRoot.typeChar ? SIGNED_T
          : UNSIGNED_T);
        break;
      case FLOAT: // float
        if (signedcount > 0) {
          throw new ParseError(lex, "signed specified with float");
        }
        if (unsignedcount > 0) {
          throw new ParseError(lex, "unsigned specified with float");
        }
        if (shortcount > 0) {
          throw new ParseError(lex, "short specified with float");
        }
        if (longcount > 0) {
          throw new ParseError(lex, "long specified with float");
        }
        etype.insert(FLOAT_T);
        break;
      case DOUBLE: // [long] double
        if (signedcount > 0) {
          throw new ParseError(lex, "signed specified with double");
        }
        if (unsignedcount > 0) {
          throw new ParseError(lex, "unsigned specified with double");
        }
        if (shortcount > 0) {
          throw new ParseError(lex, "short specified with double");
        }
        if (longcount > 1) {
          throw new ParseError(lex, "a lot of long specified with double");
        }
        etype.insert(longcount == 1 ? LONG_DOUBLE_T : DOUBLE_T);
        break;
      case TYPEDEF_NAME:
      case STRUCT:
      case UNION:
      case ENUM:
        if (signedcount > 0) {
          throw new ParseError(lex, "signed is invalid");
        }
        if (unsignedcount > 0) {
          throw new ParseError(lex, "unsigned is invalid");
        }
        if (shortcount > 0) {
          throw new ParseError(lex, "short is invalid");
        }
        if (longcount > 0) {
          throw new ParseError(lex, "long is invalid");
        }
        break;
    }

    if (volatilecount > 0) {
      etype.insertCv(VOLATILE);
    }
    if (constcount > 0) {
      etype.insertCv(CONST);
    }
    if (restrictcount > 0) {
      etype.insertCv(RESTRICT);
    }
    etype.setStorageClass(
      externcount > 0 ? S_EXTERN :
      staticcount > 0 ? S_STATIC :
      autocount > 0 ? S_AUTO :
      registercount > 0 ? S_REGISTER :
      inlinecount > 0 ? S_INLINE : S_NONE);
    ////////SF041212]

    return typeDecls;
  }

  private char signedOrUnsigend(char sign) throws ParseError, IOException
  {
    char sign2 = 0;
    int t = lex.lookAhead();
    if (t == SIGNED) {
      sign2 = SIGNED_T;
    }
    else if (t == UNSIGNED) {
      sign2 = UNSIGNED_T;
    }
    else {
      return sign;
    }

    lex.get();
    if (sign != 0 && sign != sign2) {
      throw new ParseError(lex);
    }

    return sign2;
  }

  private CompoundStmnt typedefName(EncodedType etype,
    CompoundStmnt typeDecls) throws ParseError, IOException
  {
    if (fDbgLevel > 3)  //##70
      debug.print(4, " typedefName "); //##70
    Object encoded = symTable.get(lex.getString());
    if (!(encoded instanceof byte[])) {
      throw new ParseError(lex, "unknown type: " + lex.getString());
    }
    etype.insert((byte[])encoded);
    return typeDecls;
  }

  /** struct.decl
   * <pre>
   * struct.decl
   * : STRUCT [ name ] '{' struct.member* '}'
   *        | STRUCT name
   *
   * union.decl : UNION [ name ] '{' struct.member* '}'
   *                  | UNION name
   *
   * name : IDENTIFIER | TYPEDEF_NAME
   *
   * struct.member
   * : ';'
   * | member.declaration
   */
  private CompoundStmnt structDecl(EncodedType etype, boolean isUnion,
    CompoundStmnt typeDecls) throws ParseError, IOException
  {
    if (fDbgLevel > 3) { //##67
      debug.print(4, "structDecl ", etype.toString()); //###
    }
    String fileName = lex.getFileName();
    int lineNumber = lex.getLineNumber();
    String name;
    int t = lex.get();
    if (t == IDENTIFIER || t == TYPEDEF_NAME) {
      name = lex.getString();
      if (lex.lookAhead() != '{') {
        if (lex.lookAhead() == ';') {
          name = recordTag(name);
        }
        else {
          name = toEncodedTag(name);

        }
        if (isUnion) {
          etype.insertUnion(name);
        }
        else {
          etype.insertStruct(name);

        }
        return typeDecls; // no type declaration
      }

      t = lex.get();
    }
    else {
      name = uniqueName(); // no name is specified.

    }
    if (t != '{') {
      throw new ParseError(lex, '{');
    }

    DeclaratorList members = null;
    typeDecls = new CompoundStmnt(null, typeDecls);
    for (; ; ) {
      t = lex.lookAhead();
      if (t == ';') {
        lex.get(); // ignore
      }
      else if (t != '}') {
        members = DeclaratorList.concat(members,
          memberDeclarator(typeDecls));
      }
      else {
        lex.get();
        Aggregate r;
        String encodedName = getNewEncodedTag(name);
        if (isUnion) {
          etype.insertUnion(encodedName);
          r = makeUnion(encodedName, members, fileName, lineNumber);
        }
        else {
          etype.insertStruct(encodedName);
          r = makeStruct(encodedName, members, fileName, lineNumber);
        }

        recordTag(name, r);
        return CompoundStmnt.append(typeDecls.next(), (Stmnt)r);
      }
    }
  }

  /** member.declaration
   * <pre>
   * member.declaration
   * : type.specifier declarator.list ';'
   */
  private DeclaratorList memberDeclarator(CompoundStmnt typeDecls) throws
    ParseError, IOException
  {
    if (fDbgLevel > 3)  //##70
      debug.print(4, " memberDeclarator "); //##70
    EncodedType etype = new EncodedType();
    typeSpecifier(etype, typeDecls);
    CompoundStmnt dlist = declaratorList(etype, /*0,*/ true);
    skipChar(';');
    return checkMemDecls(dlist);
    // declared types are appended to typeDecls, which is non-null.
  }

  private DeclaratorList checkMemDecls(ASTList list) throws ParseError
  {
    if (fDbgLevel > 3)  //##70
      debug.print(4, " checkMemDecls "); //##70
    if (list == null) {
      return null;
    }
    else {
      Declarator d = (Declarator)list.head();
      int c = EncodedType.getTypeChar(d.getType(), 0);
      if (c == FUNCTION_T || c == VOID_T) {
        throw new ParseError(lex, "invalid member type: "
          + d.getName());
      }

      return new DeclaratorList(d, checkMemDecls(list.tail()));
    }
  }

  /** enum.decl
   * <pre>
   * enum.decl
   * : ENUM [ name ] '{' enum.members '}'
   * | ENUM name
   *
   * enum.members : (null) | enum.member ( ',' enum.member )* ','*
   *
   * enum.member : Identifier [ '=' int.constant ]
   */
  private CompoundStmnt enumDecl(EncodedType etype,
    CompoundStmnt typeDecls) throws ParseError, IOException
  {
    if (fDbgLevel > 3)  //##70
      debug.print(4, " enumDecl "); //##70
    String fileName = lex.getFileName();
    int lineNumber = lex.getLineNumber();
    String name;
    int t = lex.get();
    //##83 if (t == IDENTIFIER)
    if ((t == IDENTIFIER)||(t == TYPEDEF_NAME)) //##83
    {
      name = lex.getString();
      if (lex.lookAhead() != '{') {
        etype.insertEnum(name);
        return typeDecls;
      }

      t = lex.get();
    }
    else {
      name = uniqueName(); // no name is specified.

    }
    if (t != '{') {
      throw new ParseError(lex, '{');
    }

    etype.insertEnum(name);
    Enum e = new Enum(name, fileName, lineNumber);
    enumBody(e);
    return CompoundStmnt.append(typeDecls, (Stmnt)e);
  }

  private void enumBody(Enum enumDecl) throws ParseError, IOException
  {
    if (fDbgLevel > 3)  //##70
      debug.print(4, " enumBody "); //##70
    long value = 0;
    for (; ; ) {
      switch (lex.get()) {
        case IDENTIFIER:
          String name = lex.getString();
          if (lex.lookAhead() == '=') {
            lex.get();
            value = constantExp(); //SF041026
          }

          //ConstantExpr valueExpr = ConstantExpr.makeInt((long)value++);
          ConstantExpr valueExpr = evaluator.make((long)value++); //SF030212
          enumDecl.add(name, valueExpr);
          symTable.put(name, valueExpr);
          if (name.charAt(0) == '_') { //##51
            symRoot.conflictingSpecialSyms.add(name); //##51
          }
          if (lex.lookAhead() == ',') {
            lex.get();
          }
          continue;
        case '}':
          return;
        default:
          throw new ParseError(lex, '}');
      }
    }
  }

  /** declarator.list
   * <pre>
   * declarator.list
   * : declarator.with.init ( ',' declarator.with.init )*
   *
   * declarator.with.init
   * : declarator [ '=' initialize.expr ]
   * | declarator [ ':' int.constant ]        (if acceptBitfiled is true)
   * | ':' int.constant                        (if acceptBitfiled is true)
   */
  private CompoundStmnt declaratorList(EncodedType etype, //SF041212 int storage,
    boolean acceptBitfiled) throws ParseError, IOException
  {
    ////////SF030531[
    //return declaratorList(etype, storage, acceptBitfiled, false);
    return declaratorList(etype, /*storage,*/ acceptBitfiled, false, false);
    ////////SF030531]
  }

  private CompoundStmnt declaratorList(EncodedType etype, //SF041212 int storage,
    boolean acceptBitfiled,
    ////////SF030531[
    //boolean recordVar)
    boolean recordVar, boolean isArg)
    ////////SF030531]
    throws ParseError, IOException
  {
    CompoundStmnt decls = null;
    Declarator d;
    int p = etype.save();
    if (fDbgLevel > 3) { //##67
      debug.print(4, "declaratorList ", etype.toString()); //##18
    }
    if (lex.lookAhead() != ';') { //SF041121
      for (; ; ) {
        if (acceptBitfiled && lex.lookAhead() == ':') {
          d = makeDeclarator(uniqueName(), lex);
          d.setType(etype.get(), etype.computeSizeof(this));
          d.setArrayParamSize(etype.getArrayParamSize()); //SF030531
          d.setStorage(etype.getStorageClass() /*storage*/);
          lex.get(); // skip ':'
          setBitFieldSize(d, (int)constantExp()); //SF041026
          d.setAsBitField(); //##16
        }
        else {
          etype.restore(p);
          //d = declarator(etype, false); //SF030531
          d = declarator(etype, isArg); //SF030531
          if (d == null) { //SF030716
            break; //SF030716
          }
          d.setType(etype.get(), etype.computeSizeof(this)); //SF040220
          d.setArrayParamSize(etype.getArrayParamSize()); //SF040220
          d.setStorage(etype.getStorageClass() /*storage*/);
          int t = lex.lookAhead();
          if (t == '=') {
            lex.get();
            d.setInitializer(initializeExpr(etype, d));
          }
          else if (acceptBitfiled && t == ':') {
            lex.get();
            setBitFieldSize(d, (int)constantExp()); //SF041026
            d.setAsBitField(); //##16
          }
        }
        if (recordVar) {
          recordSymbol(d.getName(), d);
        }
        decls = CompoundStmnt.concat(pragmaList(), decls); //SF050304
        decls = CompoundStmnt.append(decls, (Stmnt)d);
        if (lex.lookAhead() == ',') {
          lex.get();
        }
        else { //SF041115 if (lex.lookAhead() == ';')
          break;
        }
      }
    }
    return decls;
  }

  private Declarator makeDeclarator(Lex lex)
  {
    return makeDeclarator(lex.getString(), lex);
  }

  private Declarator makeDeclarator(String name, Lex lex)
  {
    return makeDeclarator(name, lex.getFileName(), lex.getLineNumber());
  }

  private void setBitFieldSize(Declarator d, int s) throws ParseError
  {
    if (s < 1) {
      throw new ParseError(lex, "bad bit-field member size");
    }

    if (!EncodedType.isIndex(d.getType(), 0)) {
      throw new ParseError(lex, "bad bit-field member type");
    }

    d.setBitFieldSize(s);
  }

  /** declarator
   * <pre>
   * declarator
   * : ( '*' [ cv.qualifier ] )* decl.name declarator.tail
   *
   * decl.name : IDENTIFIER | '(' declarator ')'
   *
   * If isArg is true, then
   *
   * arg.declarator : ( '*' [ cv.qualifier ] )* [ arg.decl.name ]
   *                        declarator.tail
   *
   * arg.decl.name : IDENTIFIER  | '(' arg.declarator ')'
   *
   * Also the returned value may be null.
   */
  private Declarator declarator(EncodedType etype, boolean isArg) throws
    ParseError, IOException
  {
    if (fDbgLevel > 3) { //##67
      debug.print(4, " declarator ",
                  lex.getString() + " etype " + etype.toString() + " " + isArg); //###
    }
    //##70 BEGIN
    if (lex.lookAhead() == PRAGMA) {
      lex.get(); // pragma
      lex.get(); // pragma body
      Pragma lPragma = new Pragma(lex.getString(),lex.getFileName(),lex.getLineNumber());
      // System.out.print("\nPRAGMA1 " + lPragma + " lookAhead " + lex.lookAhead()); //###
      pragmaList.add(lPragma);
      return null;
    }
    //##70 END
    while (lex.lookAhead() == '*') {
      lex.get();
      if (fDbgLevel > 3) { //##67
        debug.print(4, " insert pointer "); //####
      }
      etype.insert(POINTER_T);
      typeQualifier(etype); //SF041212
      //SF041212 etype.insertCv(cvQualifier());
    }

    Declarator decl;
    int c = lex.lookAhead();

    /* kluge: a typedef name is regarded as an identifier
     * if it cannot be interpreted as a type name in the
     * context.
     */
    if (c == IDENTIFIER || c == TYPEDEF_NAME) {
      lex.get();
      decl = makeDeclarator(lex);
      decl.setArgs(declaratorTail(etype, isArg));
      //SF040220 decl.setType(etype.get(), etype.computeSizeof(this));
      //SF040220 decl.setArrayParamSize(etype.getArrayParamSize()); //SF030531
      //SF040220 debug.print(4, " decl " + decl.toString()); //###
      return decl;
    }
    //SF040220 else if (c == '(') {
    else if (c == '(' && !lex.isType(lex.lookAhead(1)) &&
             lex.lookAhead(1) != ')') { //SF040220
      if (fDbgLevel > 3) { //##67
        debug.print(4, " within parenthesis "); //####
      }
      lex.get();
      EncodedType etype2 = new EncodedType();
      decl = declarator(etype2, isArg);
      // pragma does not appear here. //##70
      skipChar(')');
      declaratorTail(etype, false); // ignore the returned value
      etype.insert(etype2);
      //SF040220 if (decl != null)
      //SF040220     decl.setType(etype.get(), etype.computeSizeof(this));
      return decl;
    }
    else if (isArg) {
      declaratorTail(etype, isArg);
      return null;
    }
    else {
      throw new ParseError(lex);
    }
  }

  private Declarator declaratorOld(EncodedType etype, boolean isArg) throws //## DELETE
    ParseError, IOException
  {
    while (lex.lookAhead() == '*') {
      lex.get();
      etype.insert(POINTER_T);
      typeQualifier(etype); //SF041212
      //SF041212 etype.insertCv(cvQualifier());
    }

    Declarator decl;
    int c = lex.lookAhead();

    /* kluge: a typedef name is regarded as an identifier
     * if it cannot be interpreted as a type name in the
     * context.
     */
    if (c == IDENTIFIER || c == TYPEDEF_NAME) {
      lex.get();
      decl = makeDeclarator(lex);
      decl.setArgs(declaratorTail(etype, isArg));
      decl.setType(etype.get(), etype.computeSizeof(this));
      decl.setArrayParamSize(etype.getArrayParamSize()); //SF030531
      return decl;
    }
    else if (c == '(') {
      lex.get();
      EncodedType etype2 = new EncodedType();
      decl = declarator(etype2, isArg);
      skipChar(')');
      declaratorTail(etype, false); // ignore the returned value
      etype.insert(etype2);
      if (decl != null) {
        decl.setType(etype.get(), etype.computeSizeof(this));
        decl.setArrayParamSize(etype.getArrayParamSize()); //SF030531
      }
      return decl;
    }
    else if (isArg) {
      declaratorTail(etype, isArg);
      return null;
    }
    else {
      throw new ParseError(lex);
    }
  }

  /** declarator.tail
   * <pre>
   * declarator.tail
   * : declarator.array [ '(' arg.type.list ')' [ cv.qualifier ] ]
   *
   * This method returns non-null if arg.type.list exists and it is
   * not empty (not void).
   */
  private DeclaratorList declaratorTail(EncodedType etype, boolean isArg) throws
    ParseError, IOException
  {
    if (fDbgLevel > 3)  //##70
      debug.print(4, " declaratorTail "); //##70
    while (lex.lookAhead() == '*') {
      lex.get();
      etype.insert(POINTER_T);
      typeQualifier(etype); //SF041212
      //SF041212 etype.insertCv(cvQualifier());
    }
    DeclaratorList args = null;
    declaratorArray(etype, isArg);
    if (lex.lookAhead() == '(') {
      lex.get();
      etype.insert(RETURN_T);
      args = argTypeList(etype);
      etype.insert(FUNCTION_T);
      if (fDbgLevel > 3) { //##67
        debug.print(4, " insert function " + etype.toString()); //####
      }
      skipChar(')');
    }
    typeQualifier(etype); //SF041212
    //SF041212 etype.insertCv(cvQualifier());
    return args;
  }

  /** declarator.array
   * <pre>
   * declarator.array
   * : ( '[' [ int.constant ] ']' )*
   *
   * if this declarator is a function parameter, then the first array
   * index is interpreted as a pointer type.  For example,
   *
   * int p[][3] => int (*p)[3];
   * int q[2][4] => int (*q)[4];
   */
  private void declaratorArray(EncodedType etype, boolean isArg) throws
    ParseError, IOException
  {
    if (fDbgLevel > 3) { //##67
      debug.print(4, " declaratorArray " + etype.toString() + " isArg " + isArg); //###
    }
    if (lex.lookAhead() != '[') {
      return;
    }

    lex.get();
    long size;
    if (lex.lookAhead() == ']') {
      size = -1;
    }
    else {
      size = constantExp(); //SF041026
      if (size < 0) {
        throw new ParseError(lex, "negative array size");
      }
    }

    skipChar(']');
    declaratorArray(etype, false);
    if (isArg)
    ////////SF030531[
    //    etype.insert(POINTER_T);
    {
      etype.insert(POINTER_T);
      etype.setArrayParamSize(size);
    }
    ////////SF030531]
    else {
      etype.insertDim(size);
    }
  }

  /** arg.type.list
   * <pre>
   * arg.type.list
   * : <empty>
   * | VOID
   * | ELLIPSIS
   * | arg.type ( ',' arg.type )* [ ',' ] [ ELLIPSIS ]
   *
   * The returned value is null if arg.type.list is empty or VOID.
   *
   * Note: arg.type.list must be followed by ')'.
   */
  private DeclaratorList argTypeList(EncodedType etype) throws ParseError,
    IOException
  {
    if (fDbgLevel > 3)  //##70
      debug.print(4, " argTypeList "); //##70
    int t = lex.lookAhead();
    if (t == ')') {
      etype.insert(ELLIPSIS_T);
      return null;
    }
    else if (t == VOID && lex.lookAhead(1) == ')') {
      lex.get();
      etype.insert(VOID_T);
      return null;
    }
    else if (t == ELLIPSIS) {
      lex.get();
      if (lex.lookAhead() != ')') {
        throw new ParseError(lex, ')');
      }

      etype.insert(ELLIPSIS_T);
      return null;
    }
    else {
      return argTypeList2(etype);
    }
  }

  private DeclaratorList argTypeList2(EncodedType etype) throws ParseError,
    IOException
  {
    if (fDbgLevel > 3)  //##70
      debug.print(4, " argTypeList2 "); //##70
    DeclaratorList args = null;
    EncodedType etype2 = new EncodedType();
    Declarator a = argType(etype2);
    int c = lex.lookAhead();
    if (c == ELLIPSIS) {
      lex.get();
      etype.insert(ELLIPSIS_T);
    }
    else if (c == ',') {
      lex.get();
      c = lex.lookAhead();
      if (c == ')' || c == ELLIPSIS) {
        if (c == ELLIPSIS) {
          lex.get();

        }
        etype.insert(ELLIPSIS_T);
      }
      else {
        args = argTypeList2(etype);
      }
    }

    etype.insert(etype2);
    return new DeclaratorList(a, args);
  }

  /** arg.type
   * <pre>
   * arg.type
   * : [ REGISTER ] type.specifier arg.declarator
   *
   * If arg.decl.name is omitted, this method returns null.
   * Only the type information is stored in etype.
   */
  private Declarator argType(EncodedType etype) throws ParseError, IOException
  {
    if (fDbgLevel > 3) { //##67
      debug.print(4, " argType " + etype.toString()); //###
    }
    int t = lex.lookAhead();
    if (t == REGISTER) {
      lex.get(); // ignore!

    }
    if (typeSpecifier(etype, null) != null) {
      throw new ParseError(lex, "cannot declare a struct/union here");
    }
    if (fDbgLevel > 3) { //##67
      debug.print(4, " argType " + etype.toString()); //###
    }
    ////////SF040220[
    //return declarator(etype, true);
    Declarator decl = declarator(etype, true);
    // pragma does not appear here. //##70
    if (etype.isFunction()) {
      etype.insert(POINTER_T);
    }
    if (decl == null) {
      decl = makeDeclarator(uniqueName(), lex);
    }
    decl.setType(etype.get(), etype.computeSizeof(this));
    decl.setArrayParamSize(etype.getArrayParamSize());
    return decl;
    ////////SF040220]
  }

  /** toplevel.declaration
   * <pre>
   * toplevel.declaration
   * : storage.specifiers [ type.specifier ] top.declarator
   * | type.specifier ';'
   *
   * If this is a function declaration and top.declarator starts
   * with IDENTIFIER, type.specifier can be skipped.
   *
   * top.declarator
   * : declarator top.func.decl
   * | var.declarator ( ',' var.declarator )* ';'
   * | old.func.decl
   *
   * var.declarator
   * : declarator [ '=' initialize.expr ]
   */
  private CompoundStmnt toplevelDeclaration() throws IOException, ParseError
  {
    if (fDbgLevel > 3) { //##67
      debug.print(4, " toplevelDeclaration " + lex.getString() +
                  " lookAhead " + lex.lookAhead()); //###
    }
    //##70 BEGIN  // NO PRAGMA Should be deleted
    if (lex.lookAhead() == PRAGMA) {
      debug.print(1, "\npragma in toplevelDeclaration "); //###
      lex.get(); // pragma body
      Pragma lPragma = new Pragma(lex.getString(),lex.getFileName(),lex.getLineNumber());
      return new CompoundStmnt(lPragma);
    }
    //##70 END
    //boolean noTypeSpec;
    CompoundStmnt types = null;
    EncodedType etype = new EncodedType();
    //SF041212 int storage = storageSpecifiers();

    if (lex.lookAhead() == IDENTIFIER) {
      //noTypeSpec = true;
      etype.insert(INT_T);
    }
    else {
      //noTypeSpec = false;
      types = typeSpecifier(etype, null);
      //SF041212 if (storage == 0 && lex.lookAhead() == ';') {
      if (lex.lookAhead() == ';') { //SF041212
        lex.get();
        return types;
      }
    }

    int starcount = 0;
    while (lex.lookAhead(starcount) == '*') {
      starcount++;
    }
    if (lex.lookAhead(starcount) == IDENTIFIER
        && lex.lookAhead(starcount + 1) == '('
        && lex.lookAhead(starcount + 2) == IDENTIFIER) {
      return oldFuncDecl( /*storage,*/etype, types);
    }

    Declarator d;
    CompoundStmnt decls = types;
    EncodedType etype2 = new EncodedType(); //##18
    etype2.copy(etype); //##18

    int p = etype.save();
    for (int loopCount = 0; ; loopCount++) { //##35
      if (1000 <= loopCount) { //##35
        throw new ParseError(lex, "unexpected symbol"); //##35
      }
      etype.restore(p);
      etype.copy(etype2); //##18
      d = declarator(etype, false);
      // pragma already processed here. //##70
      if (lex.lookAhead() == '{') { //SF041212
        etype.ellipsisToVoid(); //SF041212
      }
      if (d == null) { // May be PRAGMA //##70
        if (fDbgLevel > 3)  //##70
          debug.print(4, "\n pragma may have appeared "); //###
        continue; //##70
      }
      d.setType(etype.get(), etype.computeSizeof(this)); //SF040220
      d.setArrayParamSize(etype.getArrayParamSize()); //SF040220
      d.setStorage(etype.getStorageClass() /*storage*/); //SF041212
      int t = lex.lookAhead();
      if (loopCount == 0) { //##35
        if (t == '{') {
          return topFuncDecl(etype, d, types);
        }
        //else if (noTypeSpec) //SF030725
        //    throw new ParseError(lex, "no type specifier"); //SF030725
      }
      recordSymbol(d.getName(), d);
      if (t == '=') {
        lex.get();
        d.setInitializer(initializeExpr(etype, d));
      }
      decls = CompoundStmnt.append(decls, (Stmnt)d);
      if (lex.lookAhead() == ',') {
        lex.get();
      }
      else if (lex.lookAhead() == ';') {
        break;
      }
    }

    skipChar(';');
    return decls;
  }

  /** initialize.expr
   * <pre>
   * initialize.expr
   * : expression
   * | '{' initialize.expr (',' initialize.expr)* [','] '}'
   */
  private Expr initializeExpr(EncodedType etype, Declarator decl) throws
    ParseError, IOException
  {
    if (fDbgLevel > 2) { //##67
      debug.print(3, "initializeExpr", etype.toString());
    }
    //SF041126[
    if (decl.isTypedef()) {
      throw new ParseError(lex, "typedef '" + decl.getName() +
        "' is initialized like a variable");
    }
    if (etype.isFunction()) {
      throw new ParseError(lex, "function '" + decl.getName() +
        "' is initialized like a variable");
    }
    if (etype.computeSizeof(this) <= 0
        && !etype.hasIncompleteArray()) {
      throw new ParseError(lex, "variable '" + decl.getName() +
        "' has initializer but incomplete type");
    }
    //SF041126]
    ////////SF031210[
    //switch( etype.getTypeChar() )
    //{
    //case STRUCT_BEGIN:
    //case UNION_BEGIN:
    //  if( lex.lookAhead()!='{' )
    //    throw new ParseError(lex, "aggregate initializer is required");
    //  break;
    //case ARRAY_T:
    //  if( lex.lookAhead()!='{'
    //  &&  lex.lookAhead()!=STRING_L
    //  &&  lex.lookAhead()!=STRING_WL )
    //    throw new ParseError(lex, "aggregate initializer is required");
    //  break;
    //}
    ////////SF031210]
    ////////SF040217[
    if (lex.lookAhead() != '{'
        && lex.lookAhead() != STRING_L
        && lex.lookAhead() != STRING_WL) { // Not aggregate initiator.
      EncodedType etype2 = new EncodedType();
      Expr expr = expression(etype2);
      decl.setType(etype.get(), etype.computeSizeof(this));
      decl.setArrayParamSize(etype2.getArrayParamSize());
      return expr;
    }
    ////////SF040217]
    Expr expr = initializeExprBrace(etype); // Get initiator.
    if (etype.hasIncompleteArray()) {
      throw new ParseError(lex, "elements of array have incomplete type");
    }
    if (decl != null) {
      decl.setType(etype.get(), etype.computeSizeof(this));
      decl.setArrayParamSize(etype.getArrayParamSize()); //SF030531
    }
    return expr;
  }

  /**
   * Process a brace { } in initializer.
   */
  private Expr initializeExprBrace(EncodedType etype) throws ParseError,
    IOException
  {
    if (fDbgLevel > 3)  //##70
      debug.print(4, " initializerExprBrace "); //##70
    boolean hasBrace = lex.lookAhead() == '{'; // true if the initializer begins with brace.
    if (hasBrace) {
      lex.get(); // Skip '{'

    }
    Expr expr = null;
    switch (etype.getTypeChar()) {
      case ARRAY_T: // Initializer for array.
        EncodedType elemtype = etype.getArrayElemType();
        if (elemtype.getTypeChar() == CHAR_T // Initiate by character string, e.g. str[]="abc"
            && (lex.lookAhead() == STRING_L || lex.lookAhead() == STRING_WL)) {
          EncodedType etype2 = new EncodedType();
          expr = expression(etype2);
          etype.setArraySizeIfCharArray(etype2.getArraySize(), lex);
        }
        else { // Initiate by a constant list, e. g. str[]={65,66,67}
          int arraysize = etype.getArraySize();
          int n = 0;
          //SF041115[
          //for( n=0; (n<arraysize||arraysize==0) && lex.lookAhead()!='}'; n++ )
          //  expr = ArrayInitializer.append(
          //    (ArrayInitializer)expr, initializeExprBrace(elemtype), elemtype.get() );
          //etype.setArraySize(n,lex);
          if (!isEndOfAggregateInitializer()) {
            expr = ArrayInitializer.append(
              (ArrayInitializer)expr, initializeExprBrace(elemtype),
              elemtype.get());
            for (n++;
                 !isEndOfAggregateInitializer() && (arraysize == 0 || n < arraysize);
                 n++) {
              skipChar(',');
              expr = ArrayInitializer.append(
                (ArrayInitializer)expr, initializeExprBrace(elemtype),
                elemtype.get());
            }
          }
          if (arraysize == 0) {
            etype.setArraySize(n, lex);
            //SF041115]
          }
        }
        break;
      case STRUCT_BEGIN: // structure initiation.
        Aggregate aggregate = lookupEncodedTag(etype.getTagName());
        DeclaratorList memberlist = aggregate.getMembers();
        //SF041115[
        //while( memberlist!=null && lex.lookAhead()!='}' ) // each struct member
        //{
        //  EncodedType membertype = etype.getDeclaratorType(memberlist.get());
        //  expr = ArrayInitializer.append(
        //    (ArrayInitializer)expr,initializeExprBrace(membertype),membertype.get());
        //  memberlist = memberlist.next();
        //}
        if (!isEndOfAggregateInitializer()) {
          EncodedType membertype = etype.getDeclaratorType(memberlist.get());
          expr = ArrayInitializer.append(
            (ArrayInitializer)expr, initializeExprBrace(membertype),
            membertype.get());
          for (memberlist = memberlist.next();
               !isEndOfAggregateInitializer() && memberlist != null;
               memberlist = memberlist.next()) {
            if (memberlist.get().getName().indexOf(invalidCChar) < 0) { //SF050130
              skipChar(',');
              membertype = etype.getDeclaratorType(memberlist.get());
              expr = ArrayInitializer.append(
                (ArrayInitializer)expr, initializeExprBrace(membertype),
                membertype.get());
            }
            else { //SF050130
              expr = ArrayInitializer.append(
                (ArrayInitializer)expr, evaluator.make(0, membertype.get()),
                membertype.get());
            }
          }
        }

        //SF041115]
        break;
      case UNION_BEGIN: // union initiation.
        aggregate = lookupEncodedTag(etype.getTagName());
        memberlist = aggregate.getMembers();
        if (!isEndOfAggregateInitializer() && memberlist != null) { // first union member
          EncodedType membertype = etype.getDeclaratorType(memberlist.get());
          expr = ArrayInitializer.append(
            (ArrayInitializer)expr, initializeExprBrace(membertype),
            membertype.get());
        }
        break;
      default: // scalar initiation.
        EncodedType etype2 = new EncodedType();
        expr = expression(etype2);
        //if( !(expr instanceof ConstantExpr)  // Deleted because these
        //&&  !(expr instanceof StringLiteral) // checks are done in ToHirC.
        //&&  !(expr instanceof VariableExpr) ) // function name
        //  throw new ParseError(lex, "invalid initializer");
        break;
    }

    if (hasBrace) {
      if (lex.lookAhead() == ',') { //SF041126
        lex.get(); // Skip ',' //SF041126
      }
      if (lex.lookAhead() == '}') {
        lex.get(); // Skip '}'
      }
      else {
        //##77 throw new ParseError(lex, "excess initializer");
        //##77 BEGIN
        lex.warning.put("excess initializer at " +
          lex.getFileName()+ ":" + lex.getLineNumber());
        while ((lex.lookAhead() != '}')&&
               (lex.lookAhead() != ';')) {
          lex.get(); // Skip until '}' or ';'.
        }
        if (lex.lookAhead() == '}')
          lex.get();  // Skip '}'
        //##77 END
      }
    }
    //SF041115 if( lex.lookAhead()==',' )
    //SF041115   lex.get(); // Skip ','

    if (fDbgLevel > 2) { //##67
      debug.print(3, "initializeExprBrace  " + etype + "=" + expr);
    }
    return expr;
  }

  //SF041126[
  private boolean
    isEndOfAggregateInitializer() throws IOException
  {
    return lex.lookAhead() == '}'
      || lex.lookAhead() == ',' && lex.lookAhead(1) == '}';
  }

  //SF041126]

//##18 BEGIN

  /** initialize.expr
   * <pre>
   * initialize.expr
   * : expression
   * | '{' initialize.expr (',' initialize.expr)* [','] '}'
   * initializeExpr is modified by Watanabe to accept subarray
   * initialization without '{' '}' such as
   *    int a[2][2] = { 1, 2, 3, 4 };
   * which has same meaning as
   *    int a[2][2] = { {1, 2}, {3, 4} };
   * Under construction !
   */
  private Expr
    initializeExpr2(
    EncodedType etype, // Encoded type of decl given by typeSpecifier.
    Declarator decl) throws ParseError, IOException // Declarator to which initial values are given.
  {
    if (fDbgLevel > 3)  //##70
      debug.print(4, " initializeExpr2 "); //##70
    // Modify to process sub-array and declarator-less type
    // such as "int a[2][3]; ... sizeof(a[0]), sizeof(int[2]) ... "
    EncodedType etype2 = new EncodedType();
    EncodedType lElemType, // Encoded type of array element.
      lMemberType; // Encoded type of struct member.
    Declarator lD; // Declarator for member.
    int lArraySize, // Size of array at position pos.
      // 0 if unspecified;
      lIndex; // Index of array element
    Expr expr; // Initial value expression
    boolean lWithinBrace = false; // Data is enclosed by { }
    if (fDbgLevel > 2) { //##67
      debug.print(3, "initializeExpr2", etype.toString());
    }
    if (lex.lookAhead() == ',') {
      lex.get(); // Skip comma
    }
    if (etype.isArray()) {
      return initializeArray(etype, decl);
    }
    else if (etype.isStruct()) {
      return initializeStruct(etype, decl);
    }
    if (lex.lookAhead() == '{') {
      lex.get(); // Skip left brace
      lWithinBrace = true;
    }
    expr = expression(etype2);
    if (expr instanceof StringLiteral) {
      etype.setArraySizeIfCharArray(etype2.getArraySize(), lex);
      if (decl != null) {
        decl.setType(etype.get(), etype.computeSizeof(this));
        decl.setArrayParamSize(etype.getArrayParamSize()); //SF030531
      }
    }
    if (fDbgLevel > 2) { //##67
      debug.print(3, " scalar " + expr.toString());
    }
    if (lWithinBrace && (lex.lookAhead() == '}')) {
      lex.get(); // Skip right brace
    }
    return expr;
  } // initializeExpr2

  private ArrayInitializer
    initializeArray(
    EncodedType etype, // Encoded type of decl given by typeSpecifier.
    Declarator decl) throws ParseError, IOException // Declarator to which initial values are given.
  {
    // Modify to process sub-array and declarator-less type
    // such as "int a[2][3]; ... sizeof(a[0]), sizeof(int[2]) ... "
    EncodedType etype2 = new EncodedType();
    EncodedType etype3 = new EncodedType();
    EncodedType lElemType, // Encoded type of array element.
      lMemberType; // Encoded type of struct member.
    Declarator lD; // Declarator for member.
    int lArraySize, // Size of array at position pos.
      // 0 if unspecified;
      lIndex; // Index of array element
    int n; // Data count
    Expr expr; // Initial value expression
    boolean lWithinBrace = false;
    if (fDbgLevel > 2) { //##67
      debug.print(3, "initializeArray", etype.toString());
    }
    ArrayInitializer init = null;
    etype2.copy(etype);
    lArraySize = etype.getArraySize();
    lElemType = etype.getArrayElemType();
    lArraySize = etype.getArraySize();
    if (fDbgLevel > 2) { //##67
      debug.print(3, " elem " +
                  lElemType.toString() + " array size " + lArraySize);
    }
    if (lex.lookAhead() == '}') {
      if (fDbgLevel > 2) { //##67
        debug.print(3, " no data ");
      }
      return ArrayInitializer.append(init, null, lElemType.get());
    }
    if ((lex.lookAhead() != '{') &&
        lElemType.isChar()) { // It may be a string literal.
      expr = expression(etype2); //##19 11.24
      if (expr instanceof StringLiteral) {
        etype.setArraySizeIfCharArray(etype2.getArraySize(), lex);
        if (decl != null) {
          decl.setType(etype.get(), etype.computeSizeof(this));
          decl.setArrayParamSize(etype.getArrayParamSize()); //SF030531
        }
        init = ArrayInitializer.append(init, expr, lElemType.get());
      }
      else {
        init = ArrayInitializer.append(init, expr, lElemType.get());
        for (lIndex = 1;
             (lIndex < lArraySize) && (lex.lookAhead() != '}');
             lIndex++) {
          etype3.copy(lElemType); //##19 11.24
          Expr expr3 = initializeExpr2(etype3, null); //##19 11.24
          if (!(expr3 instanceof ConstantExpr)
              && !(expr3 instanceof StringLiteral)
              && !(expr3 instanceof ArrayInitializer)
              && !(expr3 instanceof VariableExpr)) { // function name
            throw new ParseError(lex, "invalid initializer");
          }
          //##19 init = ArrayInitializer.append(init, expr3, lElemType.get()); 11.24
          init = ArrayInitializer.append(init, expr3, etype3.get()); //##19 11.24
          if (lex.lookAhead() == ',') {
            lex.get(); // Skip comma
          }
        }
      }
      if (fDbgLevel > 2) { //##67
        debug.print(3, " without brace " + init.toString());
      }
      return init;
    }
    else {
      if ((lArraySize == 0) && (lex.lookAhead() == '{')) {
        // Initialize indefinite size array, e.g.
        //    init a[] = {1, 2, 3};
        if (fDbgLevel > 2) { //##67
          debug.print(3, " with brace ");
        }
        lex.get();
        n = 0;
        while (lex.lookAhead() != '}') {
          etype3.copy(lElemType); //##19 11.24
          expr = initializeExpr2(etype3, null);
          if (!(expr instanceof ConstantExpr)
              && !(expr instanceof StringLiteral)
              && !(expr instanceof ArrayInitializer)
              && !(expr instanceof VariableExpr) // function name
              && !(expr instanceof AddressExpr)) {
            throw new ParseError(lex, "invalid initializer");
          }

          //## etype2.insertDim(-1);
          //##19 init = ArrayInitializer.append(init, expr, lElemType.get()); 11.24
          init = ArrayInitializer.append(init, expr, etype3.get()); //##19 11.24
          ++n;
          if (lex.lookAhead() == ',') {
            lex.get();
          }
        }
        lex.get(); // Skip '}'
        if (init == null) {
          throw new ParseError(lex, "an empty initializer");
        }
        etype.setArraySize(n, lex);
        if (decl != null) {
          decl.setType(etype.get(), etype.computeSizeof(this));
          decl.setArrayParamSize(etype.getArrayParamSize()); //SF030531
        }
        return init;
      }
      if (lex.lookAhead() == '{') {
        lex.get(); // Skip left brace.
        lWithinBrace = true;
      }

      for (lIndex = 0;
           (lIndex < lArraySize) && (lex.lookAhead() != '}');
           lIndex++) {
        etype3.copy(lElemType); //##19 11.24
        //##19 Expr expr3 = initializeExpr2(lElemType, null); 11.24
        Expr expr3 = initializeExpr2(etype3, null); //##19 11.24
        if (!(expr3 instanceof ConstantExpr)
            && !(expr3 instanceof StringLiteral)
            && !(expr3 instanceof ArrayInitializer)
            && !(expr3 instanceof VariableExpr) // function name
            && !(expr3 instanceof AddressExpr)) {
          throw new ParseError(lex, "invalid initializer");
        }
        //## etype2.insertDim(-1);
        //##19 init = ArrayInitializer.append(init, expr3, lElemType.get()); 11.24
        init = ArrayInitializer.append(init, expr3, etype3.get()); //##19 11.24
        if (lex.lookAhead() == ',') {
          lex.get(); // Skip comma
          //## n++;
        }
      }
      if (lWithinBrace && (lex.lookAhead() == '}')) {
        lex.get(); // Skip right brace

      }
      if (init == null) {
        throw new ParseError(lex, "an empty initializer");
      }

      //## etype.setArraySize(n, lex);
      //## if (decl != null)
      //##   decl.setType(etype.get(), etype.computeSizeof(this));
      if (fDbgLevel > 2) { //##67
        debug.print(3, "  init " + init.toString());
      }
      return init;
    }
  } // initializeArray

  private ArrayInitializer
    initializeStruct(
    EncodedType etype, // Encoded type of decl given by typeSpecifier.
    Declarator decl) throws ParseError, IOException // Declarator to which initial values are given.
  {
    EncodedType etype2 = new EncodedType();
    EncodedType etype3 = new EncodedType();
    EncodedType lElemType, // Encoded type of array element.
      lMemberType; // Encoded type of struct member.
    Declarator lD; // Declarator for member.
    Expr expr; // Initial value expression
    String tagName = etype.getTagName();
    ArrayInitializer init = null;
    if (fDbgLevel > 2) { //##67
      debug.print(3, "initializeStruct",
                  etype.toString() + " tag " + tagName);
    }
    Aggregate agg = lookupEncodedTag(tagName);
    DeclaratorList memberList = agg.getMembers();
    if (lex.lookAhead() == '}') {
      if (fDbgLevel > 2) { //##67
        debug.print(3, " no data ");
      }
      return ArrayInitializer.append(init, null, null);
    }
    if (lex.lookAhead() == '{') {
      // struct initializer list with brace.
      lex.get(); // Skip '{'.
      int n = 0;
      while ((memberList != null) &&
             (lex.lookAhead() != '}')) {
        lD = memberList.get();
        lMemberType = etype.getDeclaratorType(lD);
        etype3.copy(lMemberType); //##19 11.24
        Expr expr3 = initializeExpr2(etype3, null); //##19 lMemberType 11.24
        if (fDbgLevel > 2) { //##67
          debug.print(3, " member " + lD.toString()
            + " " + etype3.toString() + " " + expr3.toString());
        }
        if (!(expr3 instanceof ConstantExpr)
            && !(expr3 instanceof StringLiteral)
            && !(expr3 instanceof ArrayInitializer)
            && !(expr3 instanceof VariableExpr) // function name
            && !(expr3 instanceof AddressExpr)) {
          throw new ParseError(lex, "invalid initializer");
        }
        //##19 init = ArrayInitializer.append(init, expr3, etype.get()); 11.24
        init = ArrayInitializer.append(init, expr3, etype3.get()); //##19 11.24
        if (lex.lookAhead() == ',') {
          lex.get(); // Skip comma
        }
        memberList = memberList.next();
      }

      lex.get(); // Skip '}'
      if (init == null) {
        throw new ParseError(lex, "an empty initializer");
      }

      if (decl != null) {
        decl.setType(etype.get(), etype.computeSizeof(this));
        decl.setArrayParamSize(etype.getArrayParamSize()); //SF030531
      }
      if (fDbgLevel > 2) { //##67
        debug.print(3, "  init " + init.toString());
      }
      return init;
    }
    else if (memberList != null) {
      if (fDbgLevel > 2) { //##67
        debug.print(3, "struct init without brace");
      }
      while ((memberList != null) &&
             (lex.lookAhead() != '}')) {
        lD = memberList.get();
        lMemberType = etype.getDeclaratorType(lD);
        etype3.copy(lMemberType); //##19 11.24
        //##19 Expr expr3 = initializeExpr2(lMemberType, null); 11.24
        Expr expr3 = initializeExpr2(etype3, null); //##19 11.24
        if (fDbgLevel > 2) { //##67
          debug.print(3, " member " +
            etype3.toString() + " " + expr3.toString());
        }
        if (!(expr3 instanceof ConstantExpr)
            && !(expr3 instanceof StringLiteral)
            && !(expr3 instanceof ArrayInitializer)
            && !(expr3 instanceof VariableExpr)) { // function name
          throw new ParseError(lex, "invalid initializer");
        }
        //##19 init = ArrayInitializer.append(init, expr3, etype.get()); 11.24
        init = ArrayInitializer.append(init, expr3, etype3.get()); //##19 11.24
        if (lex.lookAhead() == ',') {
          lex.get(); // Skip comma
        }
        memberList = memberList.next();
      }
      return init;
    }
    return null;
  } // initializeStruct

//##18 END

  /** storage.specifiers
   * <pre>
   * storage.specifiers
   * : [INLINE] [ STATIC | EXTERN | AUTO | REGISTER]
   */
  //SF041212[
  //private int storageSpecifiers() throws IOException {
  //    int result = S_NONE;
  //    int t = lex.lookAhead();
  //
  //    if (t == INLINE) {
  //        lex.get();
  //        result = S_INLINE;
  //        t = lex.lookAhead();
  //    }
  //    if (t == STATIC)
  //        result |= S_STATIC;
  //    else if (t == EXTERN)
  //        result |= S_EXTERN;
  //    else if (t == AUTO)
  //        result |= S_AUTO;
  //    else if (t == REGISTER) //SF041126
  //        result |= S_REGISTER;
  //    else
  //        return result;
  //
  //    lex.get();
  //    return result;
  //}
  //SF041212]

  /** old.func.decl
   * <pre>
   * old.func.decl
   * : IDENTIFIER '(' IDENTIFIER ( ',' IDENTIFER )* ')'
   *   [ cv.qualifier ] top.arg.list top.func.decl
   *
   * old.arg.list
   * : [ type.specifier declarator.list ';' ]+
   *
   * Note: The rule:
   *     IDENTIFER '(' ')' [ cv.qualifier ] top.func.decl
   * is not needed.  It is absorbed in the rule:
   *     top.declarator : declarator top.func.decl
   */
  private CompoundStmnt oldFuncDecl( //SF041212 int storage,
    EncodedType etype,
    CompoundStmnt types) throws IOException, ParseError
  {
    if (fDbgLevel > 3) { //##67
      debug.print(4, "oldFuncDecl etype " + etype.toString()); //###
    }
    while (lex.lookAhead() == '*') {
      lex.get();
      etype.insert(POINTER_T);
      typeQualifier(etype); //SF041212
      //SF041212 etype.insertCv(cvQualifier());
    }
    int t;
    if (lex.get() != IDENTIFIER) {
      throw new ParseError(lex);
    }

    Declarator decl = makeDeclarator(lex);
    decl.setStorage(etype.getStorageClass() /*storage*/);
    skipChar('(');
    OldFuncArgs args = null;
    do {
      if ((t = lex.get()) == IDENTIFIER) {
        args = new OldFuncArgs(lex.getString(), args);
      }
      else {
        throw new ParseError(lex);
      }
    }
    while ((t = lex.get()) == ',');

    if (t != ')') {
      throw new ParseError(lex, ')');
    }

    //SF041013 int cv = cvQualifier();

    EncodedType etype2 = new EncodedType();
    //do { //SF030206
    while (lex.lookAhead() != '{') { //SF030206
      if (lex.lookAhead() == REGISTER) { //SF030317
        lex.get(); // ignore! //SF030317
      }
      etype2.clear();
      types = typeSpecifier(etype2, types);
      ////////SF030531[
      //CompoundStmnt dlist = declaratorList(etype2, 0, false);
      CompoundStmnt dlist = declaratorList(etype2, /*0,*/ false, false, true);
      ////////SF030531]
      skipChar(';');
      while (dlist != null) {
        Declarator d = (Declarator)dlist.head();
        if (d.getInitializer() != null || d.getArgs() != null) {
          throw new ParseError(lex, "bad parameter");
        }

        if (!args.subst(d, d.getName())) {
          throw new ParseError(lex, "bad parameter name");
        }

        dlist = dlist.next();
      }
    } //while (lex.lookAhead() != '{'); //SF030206

    decl.setArgs(OldFuncArgs.toDeclList(args, lex));
    etype.insert(RETURN_T);
    //SF041212[
    //etype.insertArgTypes(args); //etype.insert(ELLIPSIS_T); //SF041030
    etype.insert(args == null ? VOID_T : ELLIPSIS_T);
    //SF041212]
    etype.insert(FUNCTION_T);
    //SF041013 etype.insertCv(cv);
    decl.setType(etype.get(), etype.computeSizeof(this));
    decl.setArrayParamSize(etype.getArrayParamSize()); //SF030531
    return topFuncDecl(etype, decl, types);
  }

  /** top.func.decl
   * <pre>
   * top.func.decl
   * : compound.statement
   */
  private CompoundStmnt topFuncDecl(EncodedType etype,
    Declarator decl, CompoundStmnt types) throws IOException, ParseError
  {
    if (fDbgLevel > 3) { //##67
      debug.print(4, "topFuncDecl etype " + etype.toString() + " name " + decl.getName()); //###
    }
    String fname = decl.getName();
    if (symTable.get(fname) == null) {
      /* If no prototype declaration precedes,
         implictly declare here. */
      recordSymbol(fname, decl);
    }

    Function f;
    enterNewEnvironment();
    try {
      DeclaratorList args = decl.getArgs();
      while (args != null) {
        Declarator d = args.get();
        recordSymbol(d.getName(), d);
        args = args.next();
      }
      // decl.setType(etype.get(), EncodedType.FUNCTION_TYPE_SIZE); //SF030204
      byte[] lbuf = etype.get(); //##70
      decl.setType(lbuf, EncodedType.FUNCTION_TYPE_SIZE); //##70
      //##17 f = makeFunction(decl, compoundStatement(false));
      f = makeFunction(decl, statement(false)); //##17
    }
    finally {
      exitEnvironment();
    }

    return CompoundStmnt.append(types, (Stmnt)f);
  }

  /** statement
   * <pre>
   * statement
   * : compound.statement
   * | if.statement
   * | while.statement
   * | do.statement
   * | for.statement
   * | switch.statement
   * | BREAK ';'
   * | CONTINUE ';'
   * | RETURN { comma.expression } ';'
   * | typedef.declaration
   * | GOTO Identifier ';'
   * | CASE int.constant ':' statement
   * | DEFAULT ':' statement
   * | Identifier ':' statement
   * | expr.statement
   */
  private CompoundStmnt statement(boolean inSwitch) throws ParseError,
    IOException
  {
    Stmnt s;
    TreeStmnt t;
    CompoundStmnt r;
    if (fDbgLevel > 3) { //##67
      debug.print(4, "statement", "lex " + lex.getString() +
          " ahead " + lex.lookAhead() + " " + (char)lex.lookAhead()); //###
    }
    switch (lex.lookAhead()) {
      case '{':
        r = compoundStatement(inSwitch);
        break;
      case IF:
        r = ifStatement(inSwitch);
        break;
      case WHILE:
        r = whileStatement(inSwitch);
        break;
      case DO:
        r = doStatement(inSwitch);
        break;
      case FOR:
        r = forStatement(inSwitch);
        break;
      case SWITCH:
        r = switchStatement(inSwitch);
        break;
      case BREAK:
        lex.get();
        r = statement0(new BreakStmnt(lex.getFileName(),
          lex.getLineNumber()));
        break;
      case CONTINUE:
        lex.get();
        r = statement0(new ContinueStmnt(lex.getFileName(),
          lex.getLineNumber()));
        break;
      case RETURN:
        lex.get();
        t = new ReturnStmnt(null, lex.getFileName(),
          lex.getLineNumber());
        if (lex.lookAhead() != ';') {
          t.setLeft((ASTree)commaExpr(new EncodedType()));

        }
        r = statement0(t);
        break;
      case TYPEDEF:
        r = typdefDeclaration();
        break;
      case GOTO:
        lex.get();
        GotoStmnt gs = new GotoStmnt(lex.getFileName(),
          lex.getLineNumber());
        if (lex.get() != IDENTIFIER) {
          throw new ParseError(lex);
        }

        gs.setLabel(lex.getString());
        r = statement0(gs);
        break;
      case CASE:
        lex.get();
        s = new CaseLabel(constantExp(), lex.getFileName(), //SF041026
          lex.getLineNumber());
        r = statement1(s, inSwitch);
        break;
      case DEFAULT:
        lex.get();
        s = new DefaultLabel(lex.getFileName(), lex.getLineNumber());
        r = statement1(s, inSwitch);
        break;
        //##70 BEGIN
     case PRAGMA:
        // lex.get(); //?
        String lPragmaString = lex.getString(); //##060816
        lex.get(); //##060816
        //##060816 Pragma lPragma = new Pragma(lex.getString(),lex.getFileName(),lex.getLineNumber());
        Pragma lPragma = new Pragma(lPragmaString,lex.getFileName(),lex.getLineNumber()); //##060816
        // System.out.print("\nPRAGMA2 " + lPragma); //###
        r = new CompoundStmnt(lPragma);
        //##060816 lex.get(); //?
        break;
     //##70 END
     case IDENTIFIER:
        if (lex.lookAhead(1) == ':') {
          lex.get();
          s = new NamedLabel(lex.getString(),
            lex.getFileName(), lex.getLineNumber());
          if (lex.getString().charAt(0) == '_') { //##51
            symRoot.conflictingSpecialSyms.add(lex.getString()); //##51
          }
          r = statement1(s, inSwitch);
          if (fDbgLevel > 3) { //##67
            debug.print(4, "label", "lex " + lex.getString() +
                " ahead " + lex.lookAhead() + " " + (char)lex.lookAhead()); //###
          }
          break;
        }
        // don't break here.
        // don't insert any statement here.
      default:
        r = exprStatement();
        break;
    }
    return CompoundStmnt.concat(pragmaList(), r); //SF050304
  }

  private CompoundStmnt statement0(Stmnt s) throws ParseError, IOException
  { //## break, continue, return statement
    skipChar(';');
    return new CompoundStmnt(s);
  }

  private CompoundStmnt statement1(Stmnt s, boolean inSwitch) throws ParseError,
    IOException
  { //## case statement, labeled statement. s: label definition.
    if (fDbgLevel > 3)  //##70
      debug.print(4, " statement1 "); //##70
    skipChar(':');
    /* //##18
            if (lex.lookAhead() != '}')
                return new CompoundStmnt(s, statement(inSwitch));
            else
//##18 */
//##18 BEGIN
    if (lex.lookAhead() != '}') {
      int lHead = lex.lookAhead();
      CompoundStmnt lStmnt = statement(inSwitch);
      if (lHead == '{') {
        return new CompoundStmnt(s, new CompoundStmnt(lStmnt));
      }
      else {
        return new CompoundStmnt(s, lStmnt);
      }
    }
    else {

//##18 END
      return new CompoundStmnt(s,
        new CompoundStmnt(new NullStmnt(lex.getFileName(),
        lex.getLineNumber())));
    }
  }

  /** compound.statement
   * <pre>
   * compound.statement
   * : '{' statement* '}'
   */
  private CompoundStmnt compoundStatement(boolean inSwitch) throws IOException,
    ParseError
  {

    skipChar('{');
    if (fDbgLevel > 3) { //##67
      debug.print(4, "compoundStatement",
                  "lex " + lex.getString() + " lookAhead " +
                  (char)lex.lookAhead()); //###
    }
    if (lex.lookAhead() == '}') {
      lex.get(); // Skip '}'
      return new CompoundStmnt(new NullStmnt(lex.getFileName(),
        lex.getLineNumber()));
    }
    else {
      CompoundStmnt s = null;
      enterNewEnvironment();
      try {
        do {
          int t1 = lex.lookAhead();
          if (fDbgLevel > 3) { //##67
            debug.print(4, " within compound ",
              "lex " + lex.getString() + " ahead " + t1); //###
          }
          //##70 BEGIN
          if (t1 == PRAGMA) {
            Pragma lPragma = new Pragma(lex.getString(), lex.getFileName(),
              lex.getLineNumber());
            // System.out.print("\nPRAGMA " + lPragma); //###
            lex.get();
            CompoundStmnt cs = new CompoundStmnt(lPragma);
            s = CompoundStmnt.concat(s, cs);
            debug.print(4, "concat pragma ", s.toString()); //###
            continue;
          }
          //##70 END
          CompoundStmnt cs = statement(inSwitch);
          if (t1 == '{') {
            if (fDbgLevel > 3) { //##67
              debug.print(4, " nested compound ",
                "lex " + lex.getString() + " ahead " + t1); //###
            }
            cs = new CompoundStmnt(cs);
          }
          s = CompoundStmnt.concat(s, cs);
          debug.print(4, "concat stmt ", s.toString()); //###
        }
        while (lex.lookAhead() != '}');
        if (fDbgLevel > 3) { //##70
          debug.print(4, "compoundStatement result ", s.toString()); //##70
        }//##70
      }
      finally {
        exitEnvironment();
      }
      if (!(s instanceof CompoundStmnt)) { //##17
        s = new CompoundStmnt(s); //##17
      }
      if (lex.lookAhead() == '}') {
        lex.get(); // Skip '}'
      }
      if (fDbgLevel > 3) { //##67
        debug.print(5, " compoundStatement", "result " + s); //###
      }
      return s;
    }
  }

  /** if.statement
   * <pre>
   * if.statement
   * : IF '(' comma.expr ')' statement [ ELSE statement ]
   */
  private CompoundStmnt ifStatement(boolean inSwitch) throws ParseError,
    IOException
  {
    if (fDbgLevel > 3)  //##70
      debug.print(4, " ifStatement "); //##70
    lex.get(); // if
    IfStmnt s = new IfStmnt(lex.getFileName(), lex.getLineNumber());
    skipChar('(');
    Expr cond = commaExpr(new EncodedType());
    skipChar(')');
    CompoundStmnt thenp = statement(inSwitch);
    CompoundStmnt elsep = null;
    if (lex.lookAhead() == ELSE) {
      lex.get();
      elsep = statement(inSwitch);
    }

    return new CompoundStmnt(s.set(cond, thenp, elsep));
  }

  /** while.statement
   * <pre>
   * while.statement
   * : WHILE '(' comma.expr ')' statement
   */
  private CompoundStmnt whileStatement(boolean inSwitch) throws ParseError,
    IOException
  {
    if (fDbgLevel > 3)  //##70
      debug.print(4, " whileStatement "); //##70
    lex.get(); // while
    WhileStmnt s = new WhileStmnt(lex.getFileName(), lex.getLineNumber());
    skipChar('(');
    Expr cond = commaExpr(new EncodedType());
    skipChar(')');
    CompoundStmnt body = statement(inSwitch);
    return new CompoundStmnt(s.set(cond, body));
  }

  /** do.statement
   * <pre>
   * do.statement
   * : DO statement WHILE '(' comma.expr ')' ';'
   */
  private CompoundStmnt doStatement(boolean inSwitch) throws ParseError,
    IOException
  {
    if (fDbgLevel > 3)  //##70
      debug.print(4, " doStatement "); //##70
    lex.get(); // do
    DoStmnt s = new DoStmnt(lex.getFileName(), lex.getLineNumber());
    CompoundStmnt body = statement(inSwitch);
    if (lex.get() != WHILE) {
      throw new ParseError(lex);
    }

    skipChar('(');
    Expr cond = commaExpr(new EncodedType());
    skipChar(')');
    skipChar(';');
    return new CompoundStmnt(s.set(body, cond));
  }

  /** for.statement
   * <pre>
   * for.statement
   * : FOR '(' [ comma.expr ] ';'
   *                 [ comma.expr ] ';'
   *           [ comma.expr ] ')' statement
   *
   * ANSI C does not allow variable declaration in the for statement.
   */
  private CompoundStmnt forStatement(boolean inSwitch) throws ParseError,
    IOException
  {
    if (fDbgLevel > 3)  //##70
      debug.print(4, " forStatement "); //##70
    Expr init, cond, iterate;
    lex.get(); // for
    ForStmnt s = new ForStmnt(lex.getFileName(), lex.getLineNumber());
    skipChar('(');
    if (lex.lookAhead() == ';') {
      init = null;
    }
    else {
      init = commaExpr(new EncodedType());

    }
    skipChar(';');
    if (lex.lookAhead() == ';') {
      cond = null;
    }
    else {
      cond = commaExpr(new EncodedType());

    }
    skipChar(';');
    if (lex.lookAhead() == ')') {
      iterate = null;
    }
    else {
      iterate = commaExpr(new EncodedType());

    }
    skipChar(')');
    CompoundStmnt body = statement(inSwitch);
    return new CompoundStmnt(s.set(init, cond, iterate, body));
  }

  /** switch.statement
   * <pre>
   * switch.statement
   * : SWITCH '(' comma.expr ')' statement
   */
  private CompoundStmnt switchStatement(boolean inSwitch) throws ParseError,
    IOException
  {
    if (fDbgLevel > 3)  //##70
      debug.print(4, " switchStatement "); //##70
    lex.get(); // switch
    SwitchStmnt s = new SwitchStmnt(lex.getFileName(),
      lex.getLineNumber());
    skipChar('(');
    Expr cond = commaExpr(new EncodedType());
    skipChar(')');
    CompoundStmnt body = statement(true);
    return new CompoundStmnt(s.set(cond, body));
  }

  /** expr.statement
   * <pre>
   * expr.statement
   * : ';'
   * | declaration.statement
   * | comma.expr ';'
   */
  private CompoundStmnt exprStatement() throws ParseError, IOException
  {
    if (fDbgLevel > 3) { //##67
      debug.print(4, "exprStatement ", "lex " + lex.getString()); //###
    }
    if (lex.lookAhead() == ';') {
      lex.get();
      return new CompoundStmnt(new NullStmnt(lex.getFileName(),
        lex.getLineNumber()));
    }
    else if (isExpression(0)) {
      if (fDbgLevel > 3) { //##67
        debug.print(4, "ExpressionStmnt "); //###
      }
      ExpressionStmnt s = new ExpressionStmnt(lex.getFileName(),
        lex.getLineNumber());
      s.setExpr(commaExpr(new EncodedType()));
      skipChar(';');
      return new CompoundStmnt(s);
    //##70 BEGIN
    }else if (lex.lookAhead() == ASM) {
      if (fDbgLevel > 3) {
        debug.print(4, "asm as a statement ");
      }
      ExpressionStmnt s = new ExpressionStmnt(lex.getFileName(),
        lex.getLineNumber());
      s.setExpr(commaExpr(new EncodedType()));
      skipChar(';');
      return new CompoundStmnt(s);
    //##70 END
    }
    else {
      return declarationStatement();
    }
  }

  private boolean isExpression(int pos) throws IOException
  {
    int t = lex.lookAhead(pos);
    return t == IDENTIFIER || t == PLUSPLUS || t == MINUSMINUS
      || t == CHAR_CONST
      || t == INT_CONST || t == UINT_CONST
      || t == LONG_CONST || t == ULONG_CONST
      || t == LONGLONG_CONST || t == ULONGLONG_CONST //SF041020
      || t == FLOAT_CONST || t == DOUBLE_CONST
      || t == STRING_L || t == STRING_WL
      || t == '+' || t == '-'
      || t == '!' || t == '~'
      || t == '.' //##18
      || t == SIZEOF
      || t == '(' || t == '*' || t == '&';
  }

  /** declaration.statement
   * <pre>
   * declaration.statement
   * : storage.specifiers type.specifier declarator.list ';'
   * | type.specifier ';'
   */
  private CompoundStmnt declarationStatement() throws ParseError, IOException
  {
    //##70 BEGIN
    if (fDbgLevel > 3) {
      debug.print(4, "declarationStatement ", "lex " + lex.getString());
    }
    if (lex.lookAhead() == PRAGMA) {
      lex.get(); // pragma
      lex.get(); // pragma body
      Pragma lPragma = new Pragma(lex.getString(),lex.getFileName(),lex.getLineNumber());
      // System.out.print("\nPRAGMA3 " + lPragma); //###
      return new CompoundStmnt(lPragma);
    }
    //##70 END
    EncodedType etype = new EncodedType();
    //SF041212 int storage = storageSpecifiers();
    CompoundStmnt typeDecls = typeSpecifier(etype, null);
    if ( /*storage == 0 &&*/lex.lookAhead() == ';') {
      lex.get();
      if (typeDecls == null) {
        typeDecls = new CompoundStmnt(
          new NullStmnt(lex.getFileName(),
          lex.getLineNumber()));

      }
      return typeDecls;
    }

    ////////SF030531[
    //CompoundStmnt dlist = declaratorList(etype, storage, false, true);
    CompoundStmnt dlist = declaratorList(etype, /*storage,*/ false, true, false);
    ////////SF030531]
    skipChar(';');
    return CompoundStmnt.concat(typeDecls, dlist);
  }

  /** constant.exp
   * <pre>
   * constant.exp
   * : expression
   */
  private long constantExp() throws ParseError, IOException //SF041026
  {
    if (fDbgLevel > 3)  //##70
      debug.print(4, " constantExp "); //##70
    Expr expr = expression(new EncodedType());
    //SF050111[
    //if (expr instanceof IntConstantExpr) //SF041126
    //    return ((IntConstantExpr)expr).longValue();
    HIR h;
    Const c;
    if (expr instanceof ASTree
        && (h = toHirC.visit((ASTree)expr))instanceof Exp
        && (c = ((Exp)h).evaluate())instanceof IntConst) {
      return ((IntConst)c).longValue();
    }
    //SF050111]
    else {

      //throw new ParseError(lex, "bad integer constant"); //SF0550215
      throw new ParseError(lex, "bad integer constant (This error occurs when the constant expression contains floating point operation. In this case, if you specify the compile option '-coins:hirOpt=evalFloat', you can evade this error. However, the operation result on JavaVM has a possibility not correct because it depends floating point operation on the target machine.)"); //SF050215
    }
  }

  /** comma.expr
   * <pre>
   * comma.expr                lvalue
   * : expression
   * | comma.expr ',' expression        (left-to-right)
   */
  private Expr commaExpr(EncodedType etype) throws ParseError, IOException
  {
    if (fDbgLevel > 3)  //##70
      debug.print(4, " commaExpr "); //##70
    Expr expr = expression(etype);
    if (lex.lookAhead() != ',') {
      return expr;
    }
    else {
      do {
        lex.get();
        etype.clear();
        Expr expr2 = expression(etype);
        if (expr instanceof ConstantExpr) {
          expr = expr2;
        }
        else {
          expr = new CommaExpr(expr, expr2);
        }
      }
      while (lex.lookAhead() == ',');
    }

    return expr;
  }

  /** expression
   * <pre>
   * expression
   * : conditional.expr
     * | conditional.expr (assign.op | '=') expression                right-to-left
   */
  private Expr expression(EncodedType etype) throws ParseError, IOException
  {
    if (fDbgLevel > 3) { //##67
      debug.print(4, "expression ", "lex " + lex.getString()); //###
    }
    Expr lvalue = conditionalExpr(etype);
    if (!nextIsAssignOp()) {
      return lvalue;
    }

    if (!(lvalue instanceof LvalueExpr)
        || !((LvalueExpr)lvalue).isLvalue()) {
      throw new ParseError(lex, "invalid lvalue in assignment");
    }

    int op = lex.get();
    EncodedType etype2 = new EncodedType();
    Expr expr = expression(etype2);
    return new AssignExpr(lvalue, op, expr);
  }

  private boolean nextIsAssignOp() throws IOException
  {
    int t = lex.lookAhead();
    return t == '=' || (MOD_E <= t && t <= RSHIFT_E);
  }

  /** conditional.expr
   * <pre>
   * conditional.expr                lvalue
   * : logical.or.expr
   *   [ '?' comma.expression ':' conditional.expr ]  right-to-left
   */
  private Expr conditionalExpr(EncodedType etype) throws ParseError,
    IOException
  {
    if (fDbgLevel > 3)  //##70
      debug.print(4, " conditionalExpr "); //##70
    Expr expr = binaryExpr(etype);
    if (lex.lookAhead() != '?') {
      return expr;
    }

    /* ##15
             if (!etype.isInteger())
        throw new ParseError(lex, "bad operand");
             ##15 */

    lex.get();
    etype.clear();
    Expr thenExpr = commaExpr(etype);
    etype.clear();
    if (lex.get() != ':') {
      throw new ParseError(lex);
    }

    Expr elseExpr = conditionalExpr(etype);

    if (elseExpr instanceof ConstantExpr
        && ((ConstantExpr)elseExpr).longValue() == 0) {
      etype.clear();
      etype.insert(thenExpr.getType());
    }
    ////////SF021208[
    // Fold if conditional.expr is constant.
    if (expr instanceof ConstantExpr) {
      if (((ConstantExpr)expr).doubleValue() != 0) {
        return thenExpr;
      }
      else {
        return elseExpr;
      }
    }
    ////////SF021208]
    return new ConditionalExpr(expr, thenExpr, elseExpr);
  }

  /** logical.or.expr
   * <pre>
   * logical.or.expr                10 (operator precedence)
   * : logical.and.expr
   * | logical.or.expr OROR logical.and.expr                left-to-right
   *
   * logical.and.expr                9
   * : inclusive.or.expr
   * | logical.and.expr ANDAND inclusive.or.expr
   *
   * inclusive.or.expr        8
   * : exclusive.or.expr
   * | inclusive.or.expr '|' exclusive.or.expr
   *
   * exclusive.or.expr        7
   *  : and.expr
   * | exclusive.or.expr '^' and.expr
   *
   * and.expr                        6
   * : equality.expr
   * | and.expr '&' equality.expr
   *
   * equality.expr                5
   * : relational.expr
   * | equality.expr (EQ | NEQ) relational.expr
   *
   * relational.expr                4
   * : shift.expr
   * | relational.expr (LE | GE | '<' | '>') shift.expr
   *
   * shift.expr                3
   * : additive.expr
   * | shift.expr (LSHIFT | RSHIFT) additive.expr
   *
   * additive.expr                2
   * : multiply.expr
   * | additive.expr ('+' | '-') multiply.expr
   *
   * multiply.expr                1
   * : cast.expr
   * | multiply.expr ('*' | '/' | '%') cast.expr
   */
  private Expr binaryExpr(EncodedType etype) throws ParseError, IOException
  {
    if (fDbgLevel > 3)  //##70
      debug.print(4, " binaryExpr "); //##70
    Expr expr = castExpr(etype);
    for (; ; ) {
      int t = lex.lookAhead();
      int p = getOpPrecedence(t);
      if (p == 0) {
        return expr;
      }
      else {
        expr = binaryExpr2(etype, expr, p);
      }
    }
  }

  private Expr binaryExpr2(EncodedType etype, Expr expr, int prec) throws
    ParseError, IOException
  {
    if (fDbgLevel > 3)  //##70
      debug.print(4, " binaryExpr2 "); //##70
    int t = lex.get();
    EncodedType etype2 = new EncodedType();
    Expr expr2 = castExpr(etype2);
    for (; ; ) {
      int t2 = lex.lookAhead();
      int p2 = getOpPrecedence(t2);
      if (p2 != 0 && prec > p2) {
        expr2 = binaryExpr2(etype2, expr2, p2);
      }
      else {
        break;
      }
    }

    if (!etype.isValueOrFunction() || !etype2.isValueOrFunction()) {
      throw new ParseError(lex, "bad operands");
    }

    //SF050111[
    //if (expr instanceof ConstantExpr && expr2 instanceof ConstantExpr) {
    //    //SF030212[
    //    //return evaluator.applyBinaryOp((ConstantExpr)expr, etype, t,
    //    //                                   (ConstantExpr)expr2, etype2);
    //    ConstantExpr cexpr = evaluator.applyBinaryOp(
    //      lex,(ConstantExpr)expr,t,(ConstantExpr)expr2);
    //    etype.clear();
    //    etype.insert(cexpr.getType());
    //    return cexpr;
    //    //SF030212]
    //}
    //SF050111]

    boolean leftIs, rightIs;
    leftIs = etype.isLongDoubleType();
    rightIs = etype2.isLongDoubleType();
    if (leftIs || rightIs) {
      return floatingBinaryExpr(t, leftIs, expr, etype, rightIs, expr2, etype2);
    }
    leftIs = etype.isDoubleType();
    rightIs = etype2.isDoubleType();
    if (leftIs || rightIs) {
      return floatingBinaryExpr(t, leftIs, expr, etype, rightIs, expr2, etype2);
    }
    leftIs = etype.isFloatType();
    rightIs = etype2.isFloatType();
    if (leftIs || rightIs) {
      return floatingBinaryExpr(t, leftIs, expr, etype, rightIs, expr2, etype2);
    }
    if (t == '+' || t == '-') {
      leftIs = etype.isPointer();
      rightIs = etype2.isPointer();
      if (leftIs || rightIs) {
        return pointerBinaryExpr(t, leftIs, expr, etype,
          rightIs, expr2, etype2);
      }
    }

    return intBinaryExpr(t, expr, etype, expr2, etype2);
  }

  private Expr intBinaryExpr(int op,
    Expr expr1, EncodedType etype1,
    Expr expr2, EncodedType etype2) throws ParseError, IOException
  {
    boolean leftIs = etype1.isLongLong();
    boolean rightIs = etype2.isLongLong();
    boolean longlong = leftIs || rightIs;
    //##83 BEGIN
    boolean leftIsLong = etype1.isLong();
    boolean rightIsLong = etype2.isLong();
    boolean expIsLong = leftIsLong || rightIsLong;
    //##83 END
    boolean s = etype1.isSigned() && etype2.isSigned();
    byte[] type1 = etype1.get();
    etype1.clear();
    //##83 etype1.insert(longlong ? LONG_LONG_T : INT_T);
    //##83 BEGIN
    if (longlong)
      etype1.insert(LONG_LONG_T);
    else if (expIsLong)
      etype1.insert(LONG_T);
    else
      etype1.insert(INT_T);
    //##83 END
    if (s) {
      etype1.insert(SIGNED_T);
    }
    byte[] destType = etype1.get();

    return new ArithBinaryExpr(expr1, op, expr2, destType);
  }

  private Expr castToLLong(boolean dontCast, Expr expr,
    byte[] stype, byte[] dtype)
  {
    if (dontCast) {
      return expr;
    }
    else {
      return new CastExpr(expr, stype, dtype);
    }
  }

  private static final byte[] intType = {
    (byte)INT_T};

  private Expr floatingBinaryExpr(int op,
    boolean leftIsFloating,
    Expr expr, EncodedType etype,
    boolean rightIsFloating,
    Expr expr2, EncodedType etype2) throws ParseError, IOException
  {
    switch (op) {
      case EQ:
      case NEQ:
      case '>':
      case GE:
      case '<':
      case LE:
      case ANDAND:
      case OROR:
        etype.clear();
        etype.insert(INT_T);
        return new ArithBinaryExpr(expr, op, expr2, intType);

      case '%':
      case '&':
      case '^':
      case '|':
      case LSHIFT:
      case RSHIFT:
        throw new ParseError(lex, "invalid operator applied to floating type");

      default:
        byte[] rtype;
        byte[] type1 = expr.getType();
        byte[] type2 = expr2.getType();
        if (leftIsFloating) {
          rtype = type1;
        }
        else {
          etype.copy(etype2); // must copy.  don't swap pointers.
          rtype = type2;
        }
        return new ArithBinaryExpr(expr, op, expr2, rtype);
    }
  }

  private Expr pointerBinaryExpr(int op, boolean leftIsPointer,
    Expr expr, EncodedType etype,
    boolean rightIsPointer,
    Expr expr2, EncodedType etype2) throws ParseError, IOException
  {
    if (fDbgLevel > 3)  //##70
      debug.print(4, " pointerBinaryExpr "); //##70
    if (leftIsPointer && rightIsPointer) {
      if (op == '-') {
        etype.clear();
        etype.insert(OFFSET_T);
        return new PointerBinaryExpr(expr, op, expr2);
      }
      else {
        throw new ParseError(lex, "invalid pointer arithmetic");
      }
    }

    byte[] type;
    if (leftIsPointer) {
      if (!etype2.isIndex()) {
        throw new ParseError(lex, "bad pointer arithmetic");
      }

      etype.bePointer();
      return new PointerBinaryExpr(expr, op, expr2);
    }
    else {
      if (op == '-' || !etype.isIndex()) {
        throw new ParseError(lex, "bad pointer arithmetic");
      }

      etype.copy(etype2); // must copy. don't swap pointers.
      etype.bePointer();
      return new PointerBinaryExpr(expr2, op, expr);
    }
  }

  // !"#$%&'(    )*+,-./0    12345678    9:;<=>?
  private static final int[] binaryOpPrecedence
    = {
    0, 0, 0, 0, 1, 6, 0, 0,
    0, 1, 2, 0, 2, 0, 1, 0,
    0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 4, 0, 4, 0};

  private int getOpPrecedence(int c)
  {
    if ('!' <= c && c <= '?') {
      return binaryOpPrecedence[c - '!'];
    }
    else if (c == '^') {
      return 7;
    }
    else if (c == '|') {
      return 8;
    }
    else if (c == ANDAND) {
      return 9;
    }
    else if (c == OROR) {
      return 10;
    }
    else if (c == EQ || c == NEQ) {
      return 5;
    }
    else if (c == LE || c == GE) {
      return 4;
    }
    else if (c == LSHIFT || c == RSHIFT) {
      return 3;
    }
    else {
      return 0; // not a binary operator
    }
  }

  /** cast.expr
   * <pre>
   * cast.expr
   * : unary.expr
   * | '(' arg.type ')' cast.expr
   */
  private Expr castExpr(EncodedType etype) throws ParseError, IOException
  {
    if (lex.lookAhead() == '(' && !isExpression(1)) {
      if (fDbgLevel > 3) { //##67
        debug.print(4, "castExpr ", "lex " + lex.getString()); //### //##19 11.24
      }
      lex.get();
      //##19 BEGIN REFINE 11.24
      if (lex.lookAhead() == '{') { // Block statement as an expression (used in the lower() library function)
        // Expr lCompoundStatementExpr = compoundStatementExpr(); // Treat in the similar way as comma expression.
        // return ompoundStatementExpr;
      }
      //##19 END 11.24
      argType(etype);
      skipChar(')');
      EncodedType etype2 = new EncodedType();
      Expr expr = castExpr(etype2);
      //SF050111[
      //if (expr instanceof ConstantExpr && etype.isNumber())
      //    return evaluator.cast(lex, (ConstantExpr)expr, etype);
      //##18 else if (etype.isValue() && etype2.isValueOrFunction())
      //else if (etype.isVoid()|| //##18
      //SF050111]
      if (etype.isVoid() || //##18
          (etype.isValue() && etype2.isValueOrFunction())) { //##18
        return new CastExpr(expr, etype2.get(), etype.get());
      }
      else {
        throw new ParseError(lex, "invalid cast expression");
      }
    }
    else {
      return unaryExpr(etype);
    }
  }

  /** unary.expr
   * <pre>
   * unary.expr
   * : postfix.expr
   * | '*' cast.expr                lvalue
   * | '&' cast.expr
   * | sizeof.expr
   * | arith.unary.expr
   */
  private Expr unaryExpr(EncodedType etype) throws ParseError, IOException
  {
    Expr expr;
    int t = lex.lookAhead();
    if (fDbgLevel > 3) { //##67
      debug.print(6, " unaryExpr " + t);
    }
    if (t == PLUSPLUS || t == MINUSMINUS) {
      lex.get();
      expr = castExpr(etype);
      if (expr instanceof LvalueExpr) {
        LvalueExpr lexpr = (LvalueExpr)expr;
        //##18 if (lexpr.isLvalue())
        return new PrefixExpr(lexpr, t == PLUSPLUS);
      }

      throw new ParseError(lex).badLvalue(t == PLUSPLUS
        ? "++" : "--");
    }
    else if (t == '*') {
      lex.get();
      expr = castExpr(etype);
      if (etype.isFunction()) { //##29
        return expr; //##29
      }
      if (!etype.dereference()) {
        throw new ParseError(lex, "non pointer value in *");
      }

      return new DereferenceExpr(expr, etype.get());
    }
    else if (t == '&') {
      lex.get();
      expr = castExpr(etype);
      etype.insert(POINTER_T);
      if (expr instanceof LvalueExpr) {
        LvalueExpr lexpr = (LvalueExpr)expr;
        if (lexpr.hasAddress()) {
          return new AddressExpr(lexpr, etype.get());
        }
      }

      throw new ParseError(lex).badLvalue("&");
    }
    else if (t == SIZEOF)
    //##17 return sizeofExpr(etype);
    //return sizeofExpr2(etype); //##17 //SF030716
    { //##29 BEGIN
      return sizeofExpr(etype); //SF030716
    } //##29
    else if (t == '+' || t == '-' || t == '!' || t == '~') {
      return arithUnaryExpr(etype);
    }
    else {
      return postfixExpr(etype);
    }
  }

//##17 BEGIN

  private Expr sizeofExpr2(EncodedType etype) throws ParseError, IOException
  {
    Expr lSizeExpr;
    int lLookAhead1, lLookAhead2;
    //##22 int  lSize;
    long lSize; //##22
    lex.get();
    if (fDbgLevel > 3) { //##67
      debug.print(4,
                  "sizeofExpr2 lex " + lex.getString() + " lookAhead " +
                  (char)lex.lookAhead()); //###
    }
    if (lex.lookAhead() == '(' && !isExpression(1)) { // sizeof (type)
      lex.get(); // Skip '('
      lLookAhead1 = lex.lookAhead();
      //## if (lex.isType(lLookAhead1)&&
      //##    ((lex.lookAhead(1) == ')')||(lex.lookAhead(2) == ')')))
      if (lex.isType(lLookAhead1)) {
        argType(etype);
        if (lex.lookAhead() == ')') {
          skipChar(')');
        }
        lSize = etype.computeSizeof(this);
        if (fDbgLevel > 3) { //##67
          debug.print(4, " compute type size as const " + lSize);
        }
        //lSizeExpr = ConstantExpr.makeInt((long)lSize);
        lSizeExpr = evaluator.make((long)lSize); //SF030212
      }
      else {
        if (fDbgLevel > 3) { //##67
          debug.print(4, " compute type size as Expr ");
        }
        declarator(etype, false); // Store the type information in etype.
        skipChar(')');
        lSizeExpr = new SizeofExpr(etype.get());
      }
    }
    else { // sizeof expression
      lLookAhead1 = lex.lookAhead(0);
      if ((lLookAhead1 == '(') &&
          (lex.lookAhead(1) == IDENTIFIER) &&
          (lex.lookAhead(2) == ')')) {
        unaryExpr(etype);
        lSize = etype.computeSizeof(this);
        if (fDbgLevel > 3) { //##67
          debug.print(4, " compute object size as const " + lSize);
        }
        //lSizeExpr = ConstantExpr.makeInt((long)lSize);
        lSizeExpr = evaluator.make((long)lSize); //SF030212
      }
      else {
        if (fDbgLevel > 3) { //##67
          debug.print(4, " compute object size as Expr ");
        }
        Expr e = unaryExpr(new EncodedType());
        lSizeExpr = new SizeofExpr(e);
      }
    }
    etype.clear();
    etype.insert(SIZE_T);
    return lSizeExpr;
  }

  //##17 END

  /** sizeof.expr
   * <pre>
   * sizeof.expr
   * : SIZEOF unary.expr
   * | SIZEOF '(' arg.type ')'
   */
  private Expr sizeofExpr(EncodedType etype) throws ParseError, IOException
  {
    if (fDbgLevel > 3) { //##67
      debug.print(4, " sizeofExpr ");
    }
    Expr lExpr = null; //##77
    lex.get();
    if (lex.lookAhead() == '(' && !isExpression(1)) {
      lex.get();
      argType(etype);
      skipChar(')');
    }
    else {
      //##77 unaryExpr(etype);
      lExpr = unaryExpr(etype); //##77
      //##22 int size = etype.computeSizeof(this);
    }
    long size = etype.computeSizeof(this); //##22
    //##77 BEGIN
    if (lExpr instanceof ArithUnaryExpr) {
      // Do integral promotion.
      if (size < evaluator.toSize[INT_T]) { //##83
        size = evaluator.toSize[INT_T];
      } //##83
    }
    //##77 END
    if (size < 0) {
      if (etype.isFunction()) { //##29
        size = 1; // sizeof cannot be applied to function type //##29
        // but can be applied to function ? JIS C 6.3.3.4
      }
      else { //##29
        throw new ParseError(lex, "cannot compute sizeof");
      }
    }
    ////////SF040420[
    if (size == 0) {
      throw new ParseError(lex, "sizeof applied to an incomplete type");
    }
    ////////SF040420]
    //ConstantExpr expr = IntConstantExpr.makeInt((long)size);
    ConstantExpr expr = evaluator.make((long)size); //SF030212
    etype.clear();
    etype.insert(SIZE_T);
    return expr;
  }

  /** arith.unary.expr
   * <pre>
   * arith.unary.expr
   * : ('+' | '-' | '!' | '~' ) cast.expr
   */
  private Expr arithUnaryExpr(EncodedType etype) throws ParseError, IOException
  {
    if (fDbgLevel > 3)  //##70
      debug.print(4, " arithUnaryExpr etype " + etype); //##84
    int t = lex.get();
    Expr expr = castExpr(etype);
    ////////SF040909[
    //if (t == '+') {
    //    if (etype.isNumber())
    //        return expr;
    //}
    //else if (expr instanceof ConstantExpr) {
    //    //##18 evaluator.applyUnaryOp(lex, (ConstantExpr)expr, t);
    //    //##18 return expr;
    //    return evaluator.applyUnaryOp(lex, (ConstantExpr)expr, t); //##18
    //}
    //else
    //    if ((t == '-' && etype.isNumber()) || etype.isInteger())
    //        return new ArithUnaryExpr(expr, t);
    ////##15 BEGIN
    //if (t == '!') {
    //    if (etype.isPointer() || etype.isInteger())
    //        return new ArithUnaryExpr(expr, t);
    //    else if (etype.isNumber()) { //##18 BEGIN
    //      EncodedType etype2 = new EncodedType();
    //      etype2.insert(LONG_T);
    //      return new ArithUnaryExpr(new CastExpr(expr, etype.get(), etype2.get()), t);
    //    } //##18 END
    //}
    ////##15 END
    //SF050111[
    //if (expr instanceof ConstantExpr) {
    //    ConstantExpr cexpr =
    //        evaluator.applyUnaryOp(lex,t,(ConstantExpr)expr);
    //    etype.clear();
    //    etype.insert(cexpr.getType());
    //    return cexpr;
    //}
    //SF050111]
    switch (t) {
      case '+':
      case '-':
        if (etype.isNumber()) {
          if (fDbgLevel >= 4) //##77
            debug.print(4, " return ArithUnaryExpr " + (char)t); //##77
            //##84 BEGIN
            if (etype.toBePromotedToInteger()) {
              EncodedType etype2 = new EncodedType();
              etype2.insert(INT_T);
              Expr expr2 = new CastExpr(expr, etype.get(), etype2.get());
              etype.clear();
              etype.insert(INT_T);
              return new ArithUnaryExpr(expr2, t);
            }
            //##84 END
          return new ArithUnaryExpr(expr, t);
        }
        break;
      case '!':
        if (etype.isPointer() || etype.isNumber()) {
          etype.clear();
          etype.insert(INT_T);
          return new ArithUnaryExpr(expr, t);
        }
        break;
      case '~':
        if (etype.isInteger()) {
          return new ArithUnaryExpr(expr, t);
        }
        break;
    }
    ////////SF040909]
    throw new ParseError(lex, "invalid operand in " + (char)t);
  }

  /** postfix.expr
   * <pre>
   * postfix.expr
   * : primary.expr
   * | postfix.expr '[' comma.expr ']'                lvalue
   * | postfix.expr '(' func.arguments ')'
   * | postfix.expr '.' IDENTIFIER                        lvalue
   * | postfix.expr ARROW IDENTIFIER                        lvalue
   * | postfix.expr PLUSPLUS
   * | postfix.expr MINUSMINUS
   */
  private Expr postfixExpr(EncodedType etype) throws ParseError, IOException
  {
    if (fDbgLevel > 3) { //##67
      debug.print(6, " postfixExpr ");
    }
    //##70 BEGIN
    Expr expr;
    if (lex.lookAhead() == ASM) {
      // Does not come here ?
      lex.get();
      if (lex.lookAhead() == '(') {
        ASTList args = funcArguments();
        expr = new AsmExpr(args);
      }else {
        expr = null;
        throw new ParseError(lex, "invalid asm operand " + lex.getString());
      }
      return expr;
    }
    //##70 END
    //##70 Expr expr = primaryExpr(etype);
    expr = primaryExpr(etype); //##70
    for (; ; ) {
      int t = lex.lookAhead();
      if (t == '[') {
        expr = arrayExpr(etype, expr);
      }
      else if (t == '(') {
        expr = callExpr(etype, expr);
      }
      else if (t == '.') {
        expr = memberExpr(etype, expr, false);
      }
      else if (t == ARROW) {
        expr = memberExpr(etype, expr, true);
      }
      else if (t == PLUSPLUS || t == MINUSMINUS) {
        expr = postIncExpr(etype, expr, t == PLUSPLUS);
      }
      else {
        return expr;
      }
    }
  }

  /** array.expr
   * <pre>
   * array.expr
   * : postfix.expr '[' comma.expr ']'                lvalue
   */
  private Expr arrayExpr(EncodedType etype, Expr expr) throws ParseError,
    IOException
  {
    lex.get();

    /* an array expression might be <index>[<array>].
     */
    boolean reversed = etype.isIndex();

    if (fDbgLevel > 3)  //##70
      debug.print(4, " arrayExpr "); //##70
    if (!reversed && !etype.dereference()) {
      throw new ParseError(lex, "not array value with an index");
    }

    EncodedType etype2 = new EncodedType();
    Expr index = commaExpr(etype2);
    if (reversed) {
      if (!etype2.dereference()) {
        throw new ParseError(lex, "not array value with an index");
      }
    }
    else
    if (!etype2.isIndex()) {

      //throw new ParseError(lex, "bad index type");
      throw new ParseError(lex, "bad index type: " + etype2 + "=" + index);
    }

    if (lex.get() != ']') {
      throw new ParseError(lex, ']');
    }

    if (reversed) {
      Expr tmp = expr;
      expr = index;
      index = tmp;
      etype.copy(etype2);
    }

    return new ArrayExpr(expr, index, etype.get());
  }

  /** call.expr
   * <pre>
   * call.expr
   * : postfix.expr '(' func.arguments ')'
   */
  private Expr callExpr(EncodedType etype, Expr expr) throws ParseError,
    IOException
  {
    if (fDbgLevel > 3)  //##70
      debug.print(4, " callExpr "); //##70
    if (!etype.isFunction()) {
      if (!etype.dereference() || !etype.isFunction()) {
        throw new ParseError(lex,
          "called object is not a function");
      }
    }

    byte[] type = etype.get();
    etype.toReturnType();
    ASTList args = funcArguments();
    return new CallExpr(expr, args, type, etype.get());
  }

  /** function.arguments
   * <pre>
   * function.arguments
   * : '(' ')'
   * | '(' expression (',' expression)* ')'
   */
  private ASTList funcArguments() throws ParseError, IOException
  {
    if (fDbgLevel > 3)  //##70
      debug.print(4, " funcArguments "); //##70
    skipChar('(');
    if (lex.lookAhead() == ')') {
      lex.get();
      return null;
    }
    else {
      int t;
      ASTList args = null;
      EncodedType etype = new EncodedType();
      do {
        etype.clear();
        args = ASTList.append(args, (ASTree)expression(etype));
      }
      while ((t = lex.get()) == ',');
      if (t == ')') {
        return args;
      }
      else {
        throw new ParseError(lex, "");
      }
    }
  }

  /** member.expr
   * <pre>
   * member.expr
   * : postfix.expr '.' IDENTIFIER                lvalue
   * | postfix.expr ARROW IDENTIFIER                lvalue
   */
  private Expr memberExpr(EncodedType etype, Expr expr,
    boolean arrow) throws ParseError, IOException
  {
    if (fDbgLevel > 3)  //##70
      debug.print(4, " memberExpr "); //##70
    lex.get();

    /* kluge: a typedef name is regarded as an identifier
     * if it cannot be interpreted as a type name in the
     * context.
     */
    int c = lex.get();
    if (c != IDENTIFIER && c != TYPEDEF_NAME) {
      throw new ParseError(lex);
    }

    String member = lex.getString();
    if (arrow) {
      if (!etype.dereference()) {
        throw new ParseError(lex, "non pointer value given to ->");
      }
    }

    int tag = etype.getTypeChar();
    if (tag != STRUCT_BEGIN && tag != UNION_BEGIN) {
      throw new ParseError(lex, "non aggregate type for " + member);
    }

    String tagName = etype.getTagName();
    Aggregate agg = lookupEncodedTag(tagName);
    if (agg == null) {
      throw new ParseError(lex,
        "undefined aggregate type: " + decodeTagName(tagName));
    }
    else if ((tag == STRUCT_BEGIN && (agg instanceof Union))
             || (tag == UNION_BEGIN && (agg instanceof Struct))) {
      throw new ParseError(lex, "wrong tag name");
    }

    Declarator memberDecl = agg.getMember(member);
    if (memberDecl == null) {
      throw new ParseError(lex, "undefined member: " + member);
    }

    etype.clear();
    etype.insert(memberDecl.getType());
    return new MemberExpr(expr, arrow, member, agg, memberDecl);
  }

  /** post.inc.expr
   * <pre>
   * post.inc.expr
   * : postfix.expr PLUSPLUS
   * | postfix.expr MINUSMINUS
   */
  private Expr postIncExpr(EncodedType etype, Expr expr,
    boolean plus) throws ParseError, IOException
  {
    if (fDbgLevel > 3)  //##70
      debug.print(4, " postIncExpr "); //##70
    lex.get();
    if (expr instanceof LvalueExpr) {
      LvalueExpr lexpr = (LvalueExpr)expr;
      if (lexpr.isLvalue()) {
        return new PostfixExpr(lexpr, plus);
      }
    }

    throw new ParseError(lex).badLvalue(plus ? "++" : "--");
  }

  private static final byte[] defaultFunctionType
    = new byte[] {
    (byte)FUNCTION_T, (byte)ELLIPSIS_T, (byte)RETURN_T,
    (byte)INT_T};

  /** primary.expr
   * <pre>
   * primary.expr
   * : CHAR_CONST | INT_CONST | LONG_CONST | UINT_CONST | ULONG_CONST
   * | FLOAT_CONST | DOUBLE_CONST
   * | STRING_L | STRING_WL
   * | IDENTIFIER                        lvalue
   * | '(' comma.expr ')'
   */
  private Expr primaryExpr(EncodedType etype) throws ParseError, IOException
  {
    int t = lex.get();
    if (fDbgLevel > 3) { //##67
      debug.print(6, "primaryExpr lex " + t + " " + lex.getString()); //###
    }

    /* kluge: a typedef name is regarded as an identifier
     * if it cannot be interpreted as a type name in the
     * context.
     */
    if (t == IDENTIFIER || t == TYPEDEF_NAME) {
      String identifier = lex.getString();
      Object obj = symTable.get(identifier);
      Declarator d;

      if (obj != null) {
        if (obj instanceof Declarator) {
          d = (Declarator)obj;
        }
        else if (obj instanceof ConstantExpr) {
          etype.insert(INT_T);
          return (ConstantExpr)obj;
        }
        else {
          throw new ParseError(lex, "fatal error (illegal symbol=" + obj + ")");
        }
      }
      else
      if (lex.lookAhead() == '(') {
        ////////SF040816[
        if (identifier.equals("__builtin_next_arg")
            || identifier.equals("__builtin_saveregs")
            || identifier.equals("__builtin_va_start")
            || identifier.equals("__builtin_va_arg")
            || identifier.equals("__builtin_va_end")) { //SF041121
          showWarningMessage("va_start,va_arg,va_end are not supported");
        }
        else {

          ////////SF040816]
          showWarningMessage("function call without declaration: "
            + identifier + "()");
        }
        d = makeDeclarator(identifier, lex);
        d.setType(defaultFunctionType, EncodedType.FUNCTION_TYPE_SIZE);
        recordSymbol(identifier, d);
      }
      else {
        throw new ParseError(lex, "undeclared variable: " + identifier);
      }
      etype.clear();
      etype.insert(d.getType());
      return new VariableExpr(d);
    }
    else if (t == '(') {
      Expr expr = commaExpr(etype);
      if (lex.get() == ')') {
        return expr;
      }
      else {
        throw new ParseError(lex, ')');
      }
    }
    else if (t == STRING_L) {
      String s = lex.getString();
      etype.insert(CHAR_T);
      etype.insertDim(s.length() + 1);
      return new StringLiteral(s);
    }
    else if (t == STRING_WL) {
      //SF040816 showWarningMessage("wchar_t string is not supported.");
      String s = lex.getString();
      etype.insert(CHAR_T);
      etype.insertDim(s.length() + 1);
      return new WcharLiteral(s);
    }
    //##27 else if (t == CHAR_CONST || t == INT_CONST) {
    //##27 etype.insert(INT_T);
    //return ConstantExpr.makeInt(lex.getLong());
    //##27 return evaluator.make(lex.getLong()); //SF030212
    else if (t == INT_CONST) { //##27
      etype.insert(INT_T); //##27
      return evaluator.make(lex.getLong(), etype); //##27
    }
    else if (t == CHAR_CONST) { //##27
      //SF040525[
      //if (MachineParam.INT_TYPE_OF_CHAR == //##27
      //    MachineParam.INT_TYPE_OF_CHAR_IS_INT) { //##27
      //    etype.insert(INT_T);   //##27
      //}else { //##27
      //    etype.insert(UNSIGNED_T);   //##27
      //}
      //return evaluator.make(charcode, etype); //##27

      //ConstantExpr charconst;
      //if( lex.hirRoot.machineParam.getIntKindForChar()
      //==  MachineParam.INT_TYPE_OF_CHAR_IS_INT )
      //  charconst = evaluator.make(lex.getLong(),SIGNED_T,CHAR_T);
      //else
      //  charconst = evaluator.make(lex.getLong(),UNSIGNED_T,CHAR_T);
      ConstantExpr charconst = evaluator.make(
        lex.getLong(),
        symRoot.getCharType() == symRoot.typeChar ? SIGNED_T : UNSIGNED_T,
        CHAR_T);
      etype.insert(INT_T);
      return evaluator.cast(lex, charconst, etype);
      //SF040525]
    }
    else if (t == UINT_CONST) {
      etype.insert(INT_T);
      etype.insert(UNSIGNED_T);
      //return new IntConstantExpr(lex.getLong(), false);
      return evaluator.make(lex.getLong(), etype); //SF030212
    }
    else if (t == LONG_CONST) {
      etype.insert(LONG_T);
      //return ConstantExpr.make(lex.getLong());
      return evaluator.make(lex.getLong(), etype); //SF030212
    }
    else if (t == ULONG_CONST) {
      etype.insert(LONG_T);
      etype.insert(UNSIGNED_T);
      //return ConstantExpr.make(lex.getLong(), false);
      return evaluator.make(lex.getLong(), etype); //SF030212
    }
    ////////SF041020[
    else if (t == LONGLONG_CONST) {
      etype.insert(LONG_LONG_T);
      return evaluator.make(lex.getLong(), etype);
    }
    else if (t == ULONGLONG_CONST) {
      etype.insert(LONG_LONG_T);
      etype.insert(UNSIGNED_T);
      return evaluator.make(lex.getLong(), etype);
    }
    ////////SF041020]
    ////////SF040615[
    //else if (t == FLOAT_CONST || t == DOUBLE_CONST) {
    //    etype.insert(DOUBLE_T);
    //    //return ConstantExpr.make(lex.getDouble());
    //    return evaluator.make(lex.getDouble(),etype); //SF030212
    //}
    else if (t == DOUBLE_CONST) {
      etype.insert(DOUBLE_T);
      return evaluator.make(lex.getDouble(), etype);
    }
    else if (t == FLOAT_CONST) {
      etype.insert(FLOAT_T);
      return evaluator.make(lex.getDouble(), etype); //SF030212
    }
    ////////SF040615]
    //##18 BEGIN
    else if (t == '{') {
      return structExpr(etype); // bc-optag.c of gcc ?
    }
    //##18 END
    else {
      throw new ParseError(lex);
    }
  }

//##18 BEGIN
  private Expr structExpr(EncodedType etype) throws ParseError, IOException // UNNECESSARY ?
  {
    if (fDbgLevel > 3) { //##67
      debug.print(5, "structExpr", lex.getString()); //###
    }
    do {
      lex.get();
    }
    while (lex.get() != '}');
    //return ConstantExpr.makeInt(0);
    return evaluator.make((long)0); //SF030212
  }

//##18 END

//##19 BEGIN
  public SymbolTable getCurrentSymbolTable()
  {
    return symTable;
  }
//##19 END

}
