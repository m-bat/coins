/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.casttohir; //##36

import java.util.LinkedList;
import java.util.Stack;

import coins.ast.ASTree;
import coins.ast.Expr;
import coins.ast.expr.ArrayInitializer;
import coins.ast.expr.StringLiteral;
import coins.ir.IrList;
import coins.ir.hir.BlockStmt;
import coins.ir.hir.Exp;
import coins.ir.hir.ExpListExpImpl;
import coins.ir.hir.ForStmt;
import coins.ir.hir.HIR;
import coins.ir.hir.LabeledStmt;
import coins.ir.hir.Stmt;
import coins.ir.hir.VarNode;
import coins.sym.Const;
import coins.sym.Elem;
import coins.sym.StructType;
import coins.sym.Sym;
import coins.sym.SymTable;
import coins.sym.Type;
import coins.sym.UnionType;
import coins.sym.Var;
import coins.sym.VectorType;

/**
 * Converet initializer of ASTree to HIR-C expression.
 * * @auther Shuichi Fukuda
**/
final class ToHirInit
{
  private ArrayInitializer init;  // Node under processing where the node is
                                  // contained in the initiation list.
  private Stack            stack; // Stack the node that is to be processed
                                  // when inner parennthesis is processed over.
  private SymTable initSymTable; // Symbol table containing local symbols
                                 // of the initiation block under processing.

  private final ToHir  toHir; // Offers cooperation with the object of other packages.
  private final HIR    hir;   // HIR instance (used to create HIR objects)
  private final Sym    sym;   // Sym instance (used to create Sym objects)
  private final ToHirC toC;   // Owner of this object (ToHirC).
  protected int fDbgLevel;    //##77
  //-------------------------------------------------------------------
  /**
   * Constructor.
   *
   * @param  tohir Offers cooperation with the object of other packages.
  **/
  ToHirInit(ToHir tohir,ToHirC toc)
  {
    toHir = tohir;
    hir   = tohir.hirRoot.hir;
    sym   = tohir.hirRoot.sym;
    toC   = toc;
    message(1,"ToHirInit\n"); //##71
    stack = new Stack();
    fDbgLevel = tohir.ioRoot.dbgToHir.getLevel(); //##77
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
    toHir.debug.print(level,"In",mes);
  }
  //-------------------------------------------------------------------
  // create data codes
  //-------------------------------------------------------------------
  /**
   * Create initializer.
   *
   * @param   lval Variable to be initialized.
   * @param   expr Initial data expression (AST).
   * @return  Initializer expression.
  **/
  Exp createSetData(Var lval,Expr expr)
  {
    if (fDbgLevel > 3) //##67 //##77
      message(6,"createSetData  NAME="+lval.getName()+"  TYPE="+lval.getSymType());
    stack.clear();
    initSymTable = lval.getRecordedIn();
    init = expr!=null
         ? new ArrayInitializer(expr, null)
         : null;
    //##85 parse( lval.getSymType() );
    Exp lResult = parse( lval.getSymType() ); //##85
    return lResult; //##85
  }
  //-------------------------------------------------------------------
  /**
   * Parse the initial data expression (AST) assuming its type
   * as the type given the parameter.
   *
   * @param   type Assumed type of the initial data expression.
   * @return  Resultant expression.
  **/
  private Exp parse(Type type)
  {
    if (fDbgLevel > 0)  //##77
      message(8,"INIT "+type);
    if( type.getTypeRank()!=0 ) // variable initializer  i = 123
    {
      Exp val = getScalar(type);
      if (fDbgLevel > 0)  //##77
        message(8,"SCALAR="+val);
      return val;
    }
    Exp ret = null;
    in(); // into initializer brace
    switch( type.getTypeKind() )
    {
      case Type.KIND_VECTOR: // array initializer
      {
        long size     = ((VectorType)type).getElemCount();
        Type elemtype = ((VectorType)type).getElemType();
        if((elemtype.getTypeKind()==Type.KIND_CHAR
        ||  elemtype.getTypeKind()==Type.KIND_U_CHAR)
        && (ret=getString(type))!=null ) // string initializer  str[] = "abc"
        {
          if (fDbgLevel > 0)  //##77
            message(8,"STRING="+ret);
          break;
        }
        LinkedList list = new LinkedList(); //list code
        for(int i=0; i<size; i++)
          if( init==null && i<size-1 )
          {
            if (fDbgLevel > 0)  //##77
              message(8,"REPEAT="+i+"..."+size);
            list.add( // repeat code
              hir.exp(
                HIR.OP_EXPREPEAT,
                parse(elemtype),
                hir.intConstNode((int)size-i) ));
            break;
          }
          else
          {
            if (fDbgLevel > 0)  //##77
              message(8,"INDEX="+i);
            list.add( parse(elemtype) );
          }
        ret = new ExpListExpImpl(toHir.hirRoot,list);
        break;
      }
      case Type.KIND_STRUCT: // struct initializer
      {
        //##18 Struct assignment initialization should be permitted.
        // Test/Add1/tpstructinit1.c
        IrList il = ((StructType)type).getElemList();
        int  size = il.size();
        if (fDbgLevel > 0) //##77
          message(8,"elemList="+il + " size " + size); //##77
        LinkedList list = new LinkedList(); //list code
        for( int i=0; i<size; i++ )
        {
          Elem elem = (Elem)il.get(i);
          if (fDbgLevel > 0)  //##77
            message(8,"ELEM="+elem.getName());
          list.add( parse( elem.getSymType() ) );
        }
        ret = new ExpListExpImpl(toHir.hirRoot,list);
        break;
      }
      case Type.KIND_UNION: // union initializer
      {
        IrList il = ((UnionType)type).getElemList();
        LinkedList list = new LinkedList(); //list code
        if( il.size()>0 )
        {
          Elem elem = (Elem)il.get(0);
          if (fDbgLevel > 0)  //##77
            message(8,"ELEM="+elem.getName());
          list.add( parse( elem.getSymType() ) );
        }
        ret = new ExpListExpImpl(toHir.hirRoot,list);
        break;
      }
      default:
        toHir.fatal("ToHirInit.parse  TYPE="+type);
    }
    out(); // out initializer brace
    ret.setType(type); //SF030916
    return ret;
  }
  //-------------------------------------------------------------------
  // create initialization statements
  //-------------------------------------------------------------------
  /**
   * Create initiation statements for lval variable by analyzing
   * AST initiator expr and add the created statements to the
   * parameter stmt which is either BlockStmt or LabeledStmt.
   *
   * @param  stmt Statement (BlockStmt or LabeledStmt) to which initializer statements are added.
   * @param  lval Variable to be initialized.
   * @param  expr Initiation expression in the form of AST.
  **/
  void createAssignStmts(Stmt stmt,Var var,Expr expr)
  {
    if (fDbgLevel > 3) //##67 //##77
      message(6,"createAssignStmts  NAME="+var.getName()+"  TYPE="+var.getSymType());
    initSymTable = var.getRecordedIn();
    Exp lval = hir.varNode(var);

    if( expr!=null
    &&  !(expr instanceof ArrayInitializer) ) // Non-aggregate initializer.
    {
      Exp val = (Exp)toC.visit((ASTree)expr);
      if( lval.getType().getTypeKind()!=Type.KIND_VECTOR ) // If the left hand side is not array,
        val = (Exp)toC.pPromotion(val); // cast the right hand side to pointer.
      Exp lAssign = hir.exp(HIR.OP_ASSIGN,lval,val); //##85
      toHir.fAssignsForInitiation.add(lAssign); //##85
      addInitializer(
        stmt,
        //##85 toHir.newExpStmt(hir.exp(HIR.OP_ASSIGN,lval,val)) );
        toHir.newExpStmt(lAssign) );
      return;
    }
    init = new ArrayInitializer(expr, null);

    stack.clear();
    parse(stmt,lval);
  }
  //-------------------------------------------------------------------
  /**
   * Create initiation statements for lval variable, and add the created
   * statements to the stmt which is either BlockStmt or LabeledStmt.
   *
   * @param  stmt Statement (BlockStmt or LabeledStmt) to which initiation statements are added.
   * @param  lval Variable to be initialized.
  **/
  private void parse(Stmt stmt,Exp lval)
  {
    Type type = lval.getType();
    if (fDbgLevel > 0)  //##77
      message(8,"parse INIT "+type);

    if( type.getTypeRank()!=0 ) // variable initializer  i = 123
    {
      Exp val = getScalar(type);
      if (fDbgLevel > 0)  //##77
        message(8,"SCALAR="+val);
      Exp lAssign = hir.exp(HIR.OP_ASSIGN,lval,val); //##85
      toHir.fAssignsForInitiation.add(lAssign); //##85
      addInitializer(
        stmt,
        //##85 toHir.newExpStmt(hir.exp(HIR.OP_ASSIGN,lval,val)) );
        toHir.newExpStmt(lAssign) ); //##85
      return;
    }
    in(); // into initializer brace
    switch( type.getTypeKind() )
    {
      case Type.KIND_VECTOR: // array initializer
      {
        long size     = ((VectorType)type).getElemCount();
        Type elemtype = ((VectorType)type).getElemType();
        Exp val;
        if((elemtype.getTypeKind()==Type.KIND_CHAR
        ||  elemtype.getTypeKind()==Type.KIND_U_CHAR)
        && (val=getString(type))!=null ) // string initializer  str[] = "abc"
        {
          if (fDbgLevel > 0)  //##77
            message(8,"STRING="+val);
          Exp lAssign = hir.exp(HIR.OP_ASSIGN,lval,val); //##85
          toHir.fAssignsForInitiation.add(lAssign);  //##85
          addInitializer(
            stmt,
            //##85 toHir.newExpStmt(hir.exp(HIR.OP_ASSIGN,lval,val)) );
            toHir.newExpStmt(lAssign) ); //##85
          break;
        }
        for( int i=0;
             i<size && isInitialized();
             i++, lval=(Exp)lval.copyWithOperands() )
          if( init==null && i<size-1 )
          {
            if (fDbgLevel > 0)  //##77
              message(8,"REPEAT="+i+"..."+size);
            addInitializerLoop(stmt,lval,i,size); // initializer loop
            break;
          }
          else
          {
            if (fDbgLevel > 0)  //##77
              message(8,"INDEX="+i);
            parse( stmt, toHir.subsExp(lval,hir.intConstNode(i)) );
          }
        break;
      }
      case Type.KIND_STRUCT: // struct initializer
      {
        IrList il = ((StructType)type).getElemList();
        int  size = il.size();
        for( int i=0;
             i<size && isInitialized();
             i++,lval=(Exp)lval.copyWithOperands() )
        {
          Elem elem = (Elem)il.get(i);
          if (fDbgLevel > 0)  //##77
            message(8,"ELEM="+elem.getName());
          parse( stmt, hir.qualifiedExp( lval, hir.elemNode(elem) ) );
        }
        break;
      }
      case Type.KIND_UNION: // union initializer
      {
        IrList il = ((UnionType)type).getElemList();
        if( il.size()>0 && isInitialized() ) // has element
        {
          Elem elem = (Elem)il.get(0);
          if (fDbgLevel > 0)  //##77
            message(8,"ELEM="+elem.getName());
          parse( stmt, hir.qualifiedExp( lval, hir.elemNode(elem) ) );
        }
        break;
      }
      default:
        toHir.fatal("ToHirInit.parse  TYPE: "+type);
    }
    out(); // out initializer brace
  }
  //-------------------------------------------------------------------
  /**
   * For global variables without initiator, they are explicitly
   * initialized by zero only when
   * coins.MachineParam.INIT_GLOBAL_EXPLICITLY is true.
   *
   * @return  True if to be initialized.
  **/
  private boolean isInitialized()
  {
    return toHir.machineParam.initGlobalExplicitly() //##52
        || init!=null
        || initSymTable!=toHir.symRoot.symTableRoot;
  }
  //-------------------------------------------------------------------
  /**
   * Make a loop statement to initialize given array variable by value 0
   * and add it to stmt.
   *
   * @param  stmt Statement (BlockStmt or LabeledStmt) to which initiation statements are added.
   * @param  lval Array variable to be initialized.
   * @param  from Loop start index.
   * @param  to Loop end index.
  **/
  private void addInitializerLoop(Stmt stmt,Exp lval,long from,long to)
  {
    Type elemtype = ((VectorType)lval.getType()).getElemType();
    Var  tempvar  = initSymTable.generateVar(
                      toHir.symRoot.typeInt,
                      initSymTable.getOwner() );
    Stmt loopInit = hir.assignStmt(
                      hir.varNode(tempvar),
                      hir.intConstNode(from));
    Stmt loopStep = hir.assignStmt(
                      hir.varNode(tempvar),
                      hir.exp(HIR.OP_ADD,hir.varNode(tempvar),hir.intConstNode(1)) );
    Exp loopCond  = hir.exp(
                      HIR.OP_CMP_LT,
                      hir.varNode(tempvar),
                      hir.intConstNode(to) );
    ForStmt forstmt = hir.forStmt(
                        loopInit,
                        initSymTable.generateLabel(),
                        loopCond,
                        null, // loop body
                        initSymTable.generateLabel(),
                        loopStep,
                        initSymTable.generateLabel());
    addInitializer(stmt,forstmt);
    parse( forstmt.getLoopBodyPart(), toHir.subsExp(lval,hir.varNode(tempvar)) );
  }
  //-------------------------------------------------------------------
  /**
   * Add initialization statement to statement list.
   *
   * @param  stmt Statement to which initiation statements are added.
   * @param  insertion Initiation statement to be inserted.
  **/
  private void addInitializer(Stmt stmt,Stmt insertion)
  {
    if (fDbgLevel > 1)
      message(6, " addInitializer " + insertion.toStringWithChildren()); //##87
    //##87 BEGIN
    HIR lInitializer = insertion;
    if (lInitializer.getOperator() == HIR.OP_EXP_STMT) {
      if (lInitializer.getChild1().getOperator() == HIR.OP_ASSIGN)
        lInitializer = (HIR)lInitializer.getChild1();
    }
    if (! toHir.fAssignsForInitiation.contains(lInitializer)) {
      toHir.fAssignsForInitiation.add(lInitializer);
      if (fDbgLevel > 1)
        message(6, " add " + lInitializer.toStringWithChildren()); //##87
    }
    if (fDbgLevel > 1)
      message(8, " fAssignsForInitiation " + toHir.fAssignsForInitiation);
    //##87 END
    switch( stmt.getOperator() ) // Branch according to the statement to which
                                 // initiation statements are added.
    {
    case HIR.OP_BLOCK: // Block statement
      ((BlockStmt)stmt).addLastStmt(insertion); // Append as the last statement.
      break;
    case HIR.OP_LABELED_STMT: //LabeledStmt
      Stmt s = ((LabeledStmt)stmt).getStmt();
      if( s==null ) // Without statement body.
         ((LabeledStmt)stmt).setStmt(insertion);
      else if( s.getOperator()==HIR.OP_BLOCK ) // Statement body is BlockStmt.
        ((BlockStmt)s).addLastStmt(insertion);
      else // otherwise
      {
        ((LabeledStmt)stmt).setStmt(null);
        BlockStmt block = hir.blockStmt(s);
        block.setFlag(HIR.FLAG_INIT_BLOCK,true);
        block.addLastStmt(insertion);
        block.setSymTable(initSymTable);
        ((LabeledStmt)stmt).setStmt(block);
      }
      break;
    default:
      toHir.fatal("ToHirInit.addInitializerToStmt OP:"+stmt.getOperator());
    }
  }
  //-------------------------------------------------------------------
  /**
   * Enter into a parenthesis.
   * If without brace, assume there is a virtual brace.
  **/
  private void in()
  {
    if( init!=null ) // With initiator.
      if( init.head()==null ) // Empty brace.
      {
        stack.push(init.tail()); // Enter the brace.
        init = null;
      }
      else if( init.head() instanceof ArrayInitializer ) // Non-empty brace.
      {
        stack.push(init.tail()); // Enter to the brace.
        init = (ArrayInitializer)init.head();
      }
      else // Without parenthisis.
      {
        stack.push(null); // Enter to a virtual brace.
      }
    else // Without initiator.
      stack.push(null); // Enter to a virtual brace.
    //message(8,"in INIT="+stack.peek()+" LEVEL="+stack.size());
  }
  //-------------------------------------------------------------------
  /**
   * Exit from a brace.
   * If without brace, assume there is a virtual brace.
  **/
  private void out()
  {
    ArrayInitializer next = (ArrayInitializer)stack.pop();
    if (fDbgLevel > 0)  //##77
      message(8,"out INIT="+next+" LEVEL="+stack.size());
    if( next!=null ) // Inside of a brace.
    {
      if( init!=null )
        toHir.warning("excess initializer(s) '"+init+"'");
      init = next;
      return;
    }
    else // Inside of a virtual brace.
      if( stack.empty() && init!=null )
        toHir.warning("excess initializer(s) '"+init+"'");
  }
  //-------------------------------------------------------------------
  /**
   * Get a scalar initiation expression for given type t.
   *
   * @param   t Type
   * @return  Initiation expression.
  **/
  private Exp getScalar(Type t)
  {
    if( init!=null ) // With initiator.
    {
      Expr e   = (Expr)init.head(); // Get the initiator.
      Expr bak = e;
      init = (ArrayInitializer)init.tail(); // Advance the current position.
      while( e instanceof ArrayInitializer ) // Egnore extraneous braces.
        e = (Expr)((ArrayInitializer)e).head();
      if( e!=bak )
        toHir.warning("Excess initializer brace(s)");
      if( e!=null )
        return (Exp)toC.pPromotion((Exp)toC.visit((ASTree)e)); // Return the evaluated initiator.
    }
    return getConst0(t); // Return 0 if without initiator.
  }
  //-------------------------------------------------------------------
  /**
   * Create a constant node 0 of the type specified by the parameter.
   * @param   t Type of constant to be created.
   * @return  Const node of 0.
  **/
  private Exp getConst0(Type t)
  {
    Const c = null;
    switch( t.getTypeKind() )
    {
    //case Type.KIND_BOOL:
    case Type.KIND_SHORT:
    case Type.KIND_INT:         return hir.constNode(toHir.symRoot.intConst0);
    case Type.KIND_LONG:
    case Type.KIND_LONG_LONG:   return hir.constNode(toHir.symRoot.longConst0);
    case Type.KIND_CHAR:
    case Type.KIND_U_CHAR:
    case Type.KIND_U_SHORT:
    case Type.KIND_U_INT:       return hir.constNode(toHir.symRoot.intConst0);
    case Type.KIND_U_LONG:
    case Type.KIND_U_LONG_LONG: return hir.constNode(toHir.symRoot.longConst0);
    //case Type.KIND_ADDRESS:
    //case Type.KIND_OFFSET:
    //case Type.KIND_VOID:
    case Type.KIND_FLOAT:       return hir.constNode(toHir.symRoot.floatConst0);
    case Type.KIND_DOUBLE:
    case Type.KIND_LONG_DOUBLE: return hir.constNode(toHir.symRoot.doubleConst0);
    //case Type.KIND_STRING:
    case Type.KIND_ENUM:        return hir.constNode(toHir.symRoot.intConst0);
    case Type.KIND_POINTER:     return hir.convExp(toHir.typeVoidPtr,
                                       hir.constNode(toHir.symRoot.intConst0));
    //case Type.KIND_VECTOR:
    //case Type.KIND_STRUCT:
    //case Type.KIND_UNION:
    //case Type.KIND_SUBP:
    default:                    toHir.fatal("const0Node TYPE: "+t); return null;
    }
  }
  //-------------------------------------------------------------------
  /**
   * Get character string initializer.
   * Advance the current position only when initiator is a character string.
   *
   * @param   t Type of the constant to be created.
   * @return  String literal node.
  **/
  private Exp getString(Type t)
  {
    if( init!=null ) // With initiator.
    {
      Expr e   = (Expr)init.head(); // Get it.
      Expr bak = e;
      //ArrayInitializer next = (ArrayInitializer)init.tail(); //
      while( e instanceof ArrayInitializer ) // Ignore extraneous braces.
        e = (Expr)((ArrayInitializer)e).head();
      if( e instanceof StringLiteral ) // If character string was found
      {
        if( e!=bak )
          toHir.warning("Excess initializer brace(s)");
        init = (ArrayInitializer)init.tail(); //init = next; // Advance the current position.
        return getConstString( t, ((StringLiteral)e).get() ); // Return the evaluated string.
      }
      if( e!=null )
        return null; // It wan not a character string.
    }
    // Return "" if without initiator.
    return getConstString( t, "" );
  }
  //-------------------------------------------------------------------
  /**
   * Make a constant node for given character string.
   *
   * @param   t Type of the variable to which initial value is assigned.
   * @param   s Character string.
   * @return  Constant node.
   */
  private Exp getConstString(Type t,String s)
  {
    Exp val = hir.constNode( sym.stringConst(s.intern()) );
    return val;
  }
  //-------------------------------------------------------------------
  /**
   * Can be global/static initializer ?
   *
   * @param   e Initiation expression.
   * @return  True if e is an initializer expression for global/static variable.
  **/
  private boolean canBeStaticInit(Exp e) //SF030620
  {
    switch( e.getOperator() )
    {
    case HIR.OP_CONST:
       return true;

    case HIR.OP_SYM:
    case HIR.OP_PARAM:
    case HIR.OP_ELEM:
      return false;

    case HIR.OP_VAR:
      Var  v = (Var)((VarNode)e).getSymNodeSym();
      Type t = v.getSymType();
      return v.getStorageClass()==Var.VAR_STATIC
          // Initial value of static variable is decided at translation phase.
          || t.isConst() && !t.isVolatile();
          // Non-volatile const variable can be treated as a constant.

    case HIR.OP_SUBP:
      return true;

    case HIR.OP_SUBS:
      return canBeStaticInit(e.getExp1()) && canBeStaticInit(e.getExp2());

//    case HIR.OP_INDEX:
//      return true;

    case HIR.OP_QUAL:
    case HIR.OP_ARROW:
      return canBeStaticInit(e.getExp1());

    case HIR.OP_ASSIGN: // Assign statement cannot be used in initiation expression.
    case HIR.OP_CALL: // Function call cannot be used in initiation expression.
      return false;

    case HIR.OP_ADD:
    case HIR.OP_SUB:
    case HIR.OP_MULT:
    case HIR.OP_DIV:

    case HIR.OP_MOD:
    case HIR.OP_AND:
    case HIR.OP_OR:
    case HIR.OP_XOR:

    case HIR.OP_CMP_EQ:
    case HIR.OP_CMP_NE:
    case HIR.OP_CMP_GT:
    case HIR.OP_CMP_GE:
    case HIR.OP_CMP_LT:
    case HIR.OP_CMP_LE:

    case HIR.OP_SHIFT_LL:
    case HIR.OP_SHIFT_R:
    case HIR.OP_SHIFT_RL:
      return canBeStaticInit(e.getExp1()) && canBeStaticInit(e.getExp2());

    case HIR.OP_NOT:
    case HIR.OP_NEG:
    case HIR.OP_EQ_ZERO:
      return canBeStaticInit(e.getExp1());

    case HIR.OP_ADDR:
      Exp e1 = e.getExp1();
      switch( e1.getOperator() )
      {
      case HIR.OP_VAR:
        return ((Var)((VarNode)e1).getSymNodeSym()).getStorageClass()==Var.VAR_STATIC;
      case HIR.OP_SUBP:
        return true;
      }
      return false;

    case HIR.OP_CONV:
    case HIR.OP_DECAY:
    case HIR.OP_UNDECAY:
    case HIR.OP_CONTENTS:
      return canBeStaticInit(e.getExp1());

    case HIR.OP_SIZEOF:
      return true; // Without regarding the implementation of OP_SIZEOF.

    case HIR.OP_NULL:
      return false; // OP_NULL indicates there is error.

    case HIR.OP_OFFSET:
      return canBeStaticInit(e.getExp1()) && canBeStaticInit(e.getExp2());

    case HIR.OP_LG_AND: // Short circuit conditional expression can not be used
    case HIR.OP_LG_OR:  // in initiation expression because it is expanded to if statement.

    case HIR.OP_SELECT: // Select expression can not be used in initiation expression.
                        // If select condition is constant, then it has been folded out.
    case HIR.OP_COMMA:  // Comma expression can not be used in initiation expression.

    case HIR.OP_PRE_INCR: // Increment/decrement can not be used in initiation expression.
    case HIR.OP_PRE_DECR:
    case HIR.OP_POST_INCR:
    case HIR.OP_POST_DECR:

    case HIR.OP_ADD_ASSIGN: // Assign statement cannot be used in initiation expression.
    case HIR.OP_SUB_ASSIGN:
    case HIR.OP_MULT_ASSIGN:
    case HIR.OP_DIV_ASSIGN:
    case HIR.OP_MOD_ASSIGN:
    case HIR.OP_SHIFT_L_ASSIGN:
    case HIR.OP_SHIFT_R_ASSIGN:
    case HIR.OP_AND_ASSIGN:
    case HIR.OP_OR_ASSIGN:
    case HIR.OP_XOR_ASSIGN:
      return false;

    default: toHir.fatal("visitExp: "+e); return false;
    }
  }
  //-------------------------------------------------------------------
}
