/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.casttohir;

import java.util.HashSet; //##70
import java.util.Iterator; //##70
import java.util.ListIterator;
import java.util.Set; //##70

import coins.ir.IR;
import coins.ir.IrList; //##70
import coins.ir.IrListImpl; //##70
import coins.ir.hir.AssignStmt;
import coins.ir.hir.BlockStmt;
import coins.ir.hir.ConstNode;
import coins.ir.hir.SetDataStmt;
import coins.ir.hir.ElemNode;
import coins.ir.hir.Exp;
import coins.ir.hir.ExpListExp;
import coins.ir.hir.ExpStmt;
import coins.ir.hir.FunctionExp;
import coins.ir.hir.HIR;
import coins.ir.hir.HIR_Impl; //##70
import coins.ir.hir.HirList;
import coins.ir.hir.IfStmt;
import coins.ir.hir.InfStmt; //##70
import coins.ir.hir.InfStmtImpl; //##70
import coins.ir.hir.JumpStmt;
import coins.ir.hir.LabeledStmt;
import coins.ir.hir.LoopStmt;
import coins.ir.hir.Program;
import coins.ir.hir.ReturnStmt;
import coins.ir.hir.Stmt;
import coins.ir.hir.StmtImpl;
import coins.ir.hir.SubpDefinition;
import coins.ir.hir.SubpNode;
import coins.ir.hir.SwitchStmt;
import coins.ir.hir.VarNode;
import coins.sym.Const;  //##70
import coins.sym.Subp;  //##70
import coins.sym.SubpImpl;  //##81
import coins.sym.Sym;
import coins.sym.SymImpl; //##70
import coins.sym.SymTable;
import coins.sym.Var; //##71

/**
 * HIR tree visitor.
 *
 * @author  Shuichi Fukuda
**/
abstract class ToHirVisit
{
  /**
   * Offers cooperation with the object of other packages.
  **/
  protected final ToHir toHir;
  /**
   * HIR instance (used to create HIR objects).
  **/
  protected final HIR hir;
  /**
   * Sym instance (used to create Sym objects).
  **/
  protected final Sym sym;
  /**
   * Now processing block (used to create initializer).
  **/
  protected BlockStmt nowBlock;

  public final int fDbgLevel; //##67
  //-------------------------------------------------------------------
  /**
   * Constructor.
   *
   * @param  tohir Offers cooperation with the object of other packages.
  **/
  protected ToHirVisit(ToHir tohir)
  {
    toHir = tohir;
    hir   = tohir.hirRoot.hir;
    sym   = tohir.hirRoot.sym;
    fDbgLevel = tohir.hirRoot.ioRoot.dbgToHir.getLevel(); //##67
    tohir.hirRoot.ioRoot.dbgToHir.print(1,"ToHirVisit\n"); //##71
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
    toHir.debug.print(level,"Vi",mes);
  }
  //-------------------------------------------------------------------
  /**
   * Return true if now processing in the initialization block.
   *
   * @return  boolean
  **/
  protected boolean inInitBlock()
  {
    return nowBlock==null || nowBlock.getFlag(HIR.FLAG_INIT_BLOCK);
  }
  //-------------------------------------------------------------------
  /**
   * Visit HIR program tree.
   *
   * @param  program Program
  **/
  public final void visitProgram()
  {
    visitProgram((Program)toHir.hirRoot.programRoot);
  }
  //-------------------------------------------------------------------
  /**
   * Visit HIR program tree.
   *
   * @param  program Program
  **/
  final void visitProgram(Program program)
  {
    toHir.symRoot.symTableCurrent = toHir.symRoot.symTableRoot;
    toHir.symRoot.subpCurrent = null;

    // visit global variable initialization block
    IR ir = program.getInitiationPart();
    if( ir!=null && ir.getOperator()==HIR.OP_BLOCK )
      visitStmt((Stmt)ir);

    // visit each subprograms
    for( ListIterator i=program.getSubpDefinitionList().iterator(); i.hasNext(); )
    {
      SubpDefinition subpdef = (SubpDefinition)i.next();
      toHir.symRoot.subpCurrent     = subpdef.getSubpSym();
      toHir.symRoot.symTableCurrent = subpdef.getSubpSym().getSymTable();
      // visit static variable initialization block
      visitStmt( subpdef.getInitiationPart() );
      // visit body
      visitStmt( subpdef.getHirBody() );
    }
  }
  //-------------------------------------------------------------------
  // statement visitor
  //-------------------------------------------------------------------
  /**
   * Call appropriate method by operator of statement node.
   *
   * @param  s Visited statement.
  **/
  final void visitStmt(Stmt s)
  {
    if( s==null )
      return;
    toHir.nowFile = s.getFileName();
    toHir.nowLine = s.getLineNumber();
    if (fDbgLevel > 3) //##67
      message(8,"{"+s+"}\t"+toHir.nowFile+"("+toHir.nowLine+")");
    switch( s.getOperator() )
    {
    case HIR.OP_BLOCK:
      SymTable oldsymtable = toHir.symRoot.symTableCurrent; // push sym table
      if( ((BlockStmt)s).getSymTable()!=null )
        toHir.symRoot.symTableCurrent = ((BlockStmt)s).getSymTable();
      atBlock((BlockStmt)s);
      toHir.symRoot.symTableCurrent = oldsymtable; // pop sym table
      break;
    case HIR.OP_LABELED_STMT:
      atLabeledStmt((LabeledStmt)s);
      break;
    case HIR.OP_ASSIGN:
      atAssignStmt((AssignStmt)s);
      break;
    case HIR.OP_IF:
      atIf((IfStmt)s);
      break;
    case HIR.OP_WHILE:
      atWhile((LoopStmt)s);
      break;
    case HIR.OP_FOR:
      atFor((LoopStmt)s);
      break;
    case HIR.OP_UNTIL:
      atUntil((LoopStmt)s);
      break;
    case HIR.OP_JUMP:
      atJump((JumpStmt)s);
      break;
    case HIR.OP_SWITCH:
      atSwitch((SwitchStmt)s);
      break;
    case HIR.OP_RETURN:
      atReturn((ReturnStmt)s);
      break;
    case HIR.OP_EXP_STMT:
      atExpStmt((ExpStmt)s);
      break;
    case HIR.OP_SETDATA:
      atSetDataStmt((SetDataStmt )s); //SF040525
      break;
    case HIR.OP_INF: //SF050304
      atInfStmt((InfStmt )s); //##70
      break;
    case HIR.OP_ASM: //##70
      break;  //##70
    default:
      toHir.fatal("not supported statement: "+s);
    }
  } // visitStmt
  //-------------------------------------------------------------------
  /**
   * At block statement node.
   *
   * @param  s BlockStmt
  **/
  protected void atBlock(BlockStmt s)
  {
    BlockStmt oldblock = nowBlock;
    nowBlock = s;
    // visit each statement
    for( Stmt now=s.getFirstStmt(), next; now!=null; now=next )
    {
      next = ((StmtImpl)now).getNextStmt();
      visitStmt(now);
    }
    nowBlock = oldblock;
  }
  //-------------------------------------------------------------------
  /**
   * At labeled statement node.
   *
   * @param  s LabeledStmt
  **/
  protected void atLabeledStmt(LabeledStmt s)
  {
    visitStmt( s.getStmt() );
  }
  //-------------------------------------------------------------------
  /**
   * At assign statement node.
   *
   * @param  s AssignStmt
  **/
  protected void atAssignStmt(AssignStmt s)
  {
    Exp old, now;
    old=s.getLeftSide(); now=visitExp(old); if(now!=old) s.setLeftSide(now);
    old=s.getRightSide(); now=visitExp(old); if(now!=old) s.setRightSide(now);
  }
  //-------------------------------------------------------------------
  /**
   * At if statement node.
   *
   * @param  s IfStmt
  **/
  protected void atIf(IfStmt s)
  {
    visitStmt(s.getThenPart());
    visitStmt(s.getElsePart());
    Exp old, now;
    old=s.getIfCondition(); now=visitExp(old); if(now!=old) s.setIfCondition(now);
  }
  //-------------------------------------------------------------------
  /**
   * At while statement node.
   *
   * @param  s LoopStmt
  **/
  protected void atWhile(LoopStmt s)
  {
    visitStmt(s.getLoopBodyPart());
    Exp old, now;
    old=s.getLoopStartCondition(); now=visitExp(old); if(now!=old) s.setLoopStartCondition(now);
  }
  //-------------------------------------------------------------------
  /**
   * At for statement node.
   *
   * @param  s LoopStmt
  **/
  protected void atFor(LoopStmt s)
  {
    visitStmt(s.getLoopInitPart());
    visitStmt(s.getLoopBodyPart());
    visitStmt(s.getLoopStepPart());
    Exp old, now;
    old=s.getLoopStartCondition(); now=visitExp(old); if(now!=old) s.setLoopStartCondition(now);
  }
  //-------------------------------------------------------------------
  /**
   * At do-while statement node.
   *
   * @param  s LoopStmt
  **/
  protected void atUntil(LoopStmt s)
  {
    visitStmt(s.getLoopBodyPart());
    Exp old, now;
    old=s.getLoopEndCondition(); now=visitExp(old); if(now!=old) s.setLoopEndCondition(now);
  }
  //-------------------------------------------------------------------
  /**
   * At goto statement node.
   *
   * @param  s JumpStmt
  **/
  protected void atJump(JumpStmt s)
  {
  }
  //-------------------------------------------------------------------
  /**
   * At switch statement node.
   *
   * @param  s SwitchStmt
  **/
  protected void atSwitch(SwitchStmt s)
  {
    visitStmt(s.getBodyStmt());
    Exp old, now;
    old=s.getSelectionExp(); now=visitExp(old); if(now!=old) s.setSelectionExp(now);
  }
  //-------------------------------------------------------------------
  /**
   * At return statement node.
   *
   * @param  s ReturnStmt
  **/
  protected void atReturn(ReturnStmt s)
  {
    Exp old, now;
    old=s.getReturnValue(); now=visitExp(old); if(now!=old) s.setReturnValue(now);
  }
  //-------------------------------------------------------------------
  /**
   * At expression statement node.
   *
   * @param  s ExpStmt
  **/
  protected void atExpStmt(ExpStmt s)
  {
    Exp old, now;
    old=s.getExp(); now=visitExp(old); if(now!=old) s.setExp(now);
  }
  //-------------------------------------------------------------------
  /**
   * At datacode statement node.
   *
   * @param  s SetDataStmt
  **/
  protected void atSetDataStmt(SetDataStmt s) //SF040525
  {
    Exp old, now;
    old=s.getLeftSide(); now=visitExp(old); if(now!=old) s.setLeftSide(now);
    old=s.getRightSide(); now=visitExp(old); if(now!=old) s.setRightSide(now);
  }
  //-------------------------------------------------------------------
  // expression visitor
  //-------------------------------------------------------------------
  /*
   * Call appropriate method by operator of expression node.
   *
   * @param  e Visited expression.
  **/
  final Exp visitExp(Exp e)
  {
    if( e==null )
      return null;
    if (fDbgLevel > 3) //##67
      message(8,"<"+e+">");
    switch( e.getOperator() )
    {
    case HIR.OP_CONST:
      return atConst((ConstNode)e);
    case HIR.OP_VAR:
      return atVar((VarNode)e);
    case HIR.OP_SUBP:
      return atSubp((SubpNode)e);
    case HIR.OP_ELEM:
      return atElem((ElemNode)e);

    //case HIR.OP_LIST:    // list of CompilerObjects
    //case HIR.OP_SEQ:     // sequence of HIR objects
    //case HIR.OP_ENCLOSE: // enclose subexpression
    case HIR.OP_SUBS:    // subscripted variable
      return atSubs(e);
    //case HIR.OP_INDEX:   // index modification of subscripted var
    //  return atIndex(e);
    case HIR.OP_QUAL:    // .
      return atQual(e);
    case HIR.OP_ARROW:   // ->
      return atArrow(e);
    case HIR.OP_CALL:    // function call
      return atCall((FunctionExp)e);

    case HIR.OP_ADD:
      return atAdd(e);
    case HIR.OP_SUB:
      return atSub(e);
    case HIR.OP_MULT:
      return atMul(e);
    case HIR.OP_DIV:
      return atDiv(e);
    case HIR.OP_MOD:
      return atMod(e);
    case HIR.OP_AND:
      return atAnd(e);
    case HIR.OP_OR:
      return atOr(e);
    case HIR.OP_XOR:
      return atXor(e);
    case HIR.OP_CMP_EQ:
      return atCmpEq(e);
    case HIR.OP_CMP_NE:
      return atCmpNe(e);
    case HIR.OP_CMP_GT:
      return atCmpGt(e);
    case HIR.OP_CMP_GE:
      return atCmpGe(e);
    case HIR.OP_CMP_LT:
      return atCmpLt(e);
    case HIR.OP_CMP_LE:
      return atCmpLe(e);
    case HIR.OP_SHIFT_LL: // L-shift
      return atLShift(e);
    case HIR.OP_SHIFT_R : // arithmetic R-shift
      return atARShift(e);
    case HIR.OP_SHIFT_RL: // R-shift
      return atRShift(e);

    case HIR.OP_NOT:      // ~@
      return atNot(e);
    case HIR.OP_NEG:      // -@
      return atNeg(e);
    case HIR.OP_ADDR:     // &@
      return atAddr(e);
    case HIR.OP_CONV:     // cast
      return atConv(e);
    case HIR.OP_DECAY:    // array to pointer
      return atDecay(e);
    case HIR.OP_UNDECAY:  // pointer to array
      return atUndecay(e);
    case HIR.OP_CONTENTS: // *@
      return atContents(e);
    //case HIR.OP_SIZEOF:
    //case HIR.OP_PHI:
    //case HIR.OP_NULL:

    // HIR-C only
    case HIR.OP_ASSIGN: // assign expression
      return atAssign(e);
    case HIR.OP_OFFSET:
      return atOffset(e);
    case HIR.OP_LG_AND:
      return atLgAnd(e);
    case HIR.OP_LG_OR:
      return atLgOr(e);
    case HIR.OP_SELECT:
      return atSelect(e);
    case HIR.OP_COMMA:
      return atComma(e);
    case HIR.OP_EQ_ZERO: // !@
      return atEqZero(e);
    case HIR.OP_PRE_INCR:
      return atPre(HIR.OP_ADD,e);
    case HIR.OP_PRE_DECR:
      return atPre(HIR.OP_SUB,e);
    case HIR.OP_POST_INCR:
      return atPost(HIR.OP_ADD,e);
    case HIR.OP_POST_DECR:
      return atPost(HIR.OP_SUB,e);
    case HIR.OP_ADD_ASSIGN:
      return atAddAssign(e);
    case HIR.OP_SUB_ASSIGN:
      return atSubAssign(e);
    case HIR.OP_MULT_ASSIGN:
      return atMulAssign(e);
    case HIR.OP_DIV_ASSIGN:
      return atDivAssign(e);
    case HIR.OP_MOD_ASSIGN:
      return atModAssign(e);
    case HIR.OP_SHIFT_L_ASSIGN:
      return atLShiftAssign(e);
    case HIR.OP_SHIFT_R_ASSIGN:
      return atRShiftAssign(e);
    case HIR.OP_AND_ASSIGN:
      return atAndAssign(e);
    case HIR.OP_OR_ASSIGN:
      return atOrAssign(e);
    case HIR.OP_XOR_ASSIGN:
      return atXorAssign(e);

    // initialization block
    case HIR.OP_EXPLIST:   // expression list for datacode
      return atExpList((ExpListExp)e);
    case HIR.OP_EXPREPEAT: // expression repeatation for datacode
      return atExpRepeat(e);

    // illegal node
    case HIR.OP_NULL:
      return e;
    default:
      toHir.fatal("unexpected operator: "+e);
      return null;
    }
  }
  //-------------------------------------------------------------------
  /**
   * At constant node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atConst(ConstNode e)
  {
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At variable node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atVar(VarNode e)
  {
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At function node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atSubp(SubpNode e)
  {
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At element node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atElem(ElemNode e)
  {
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At subscript expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atSubs(Exp e) // []
  {
    IR old, now;
    old=e.getChild1(); now=visitExp((Exp)old); if(now!=old) e.setChild1(now);
    old=e.getChild2(); now=visitExp((Exp)old); if(now!=old) e.setChild2(now);
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At index expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atIndex(Exp e)
  {
    IR old, now;
    old=e.getChild1(); now=visitExp((Exp)old); if(now!=old) e.setChild1(now);
    old=e.getChild2(); now=visitExp((Exp)old); if(now!=old) e.setChild2(now);
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At member-access expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atQual(Exp e) // .
  {
    IR old, now;
    old=e.getChild1(); now=visitExp((Exp)old); if(now!=old) e.setChild1(now);
    old=e.getChild2(); now=visitExp((Exp)old); if(now!=old) e.setChild2(now);
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At arrow expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atArrow(Exp e) // ->
  {
    IR old, now;
    old=e.getChild1(); now=visitExp((Exp)old); if(now!=old) e.setChild1(now);
    old=e.getChild2(); now=visitExp((Exp)old); if(now!=old) e.setChild2(now);
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At function call expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atCall(FunctionExp e)
  {
    Exp old, now;
    old = e.getFunctionSpec();
    now = visitExp(old);
    if(now!=old)
      e.setFunctionSpec(now);

    HirList actuallist = (HirList)e.getParamList();
    for( ListIterator i=actuallist.iterator(); i.hasNext(); )
    {
      old = (Exp)i.next();
      now = visitExp(old);
      if( now!=old )
      {
        i.set(now);
        now.setParent(actuallist);
      }
    }
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At add expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atAdd(Exp e)
  {
    IR old, now;
    old=e.getChild1(); now=visitExp((Exp)old); if(now!=old) e.setChild1(now);
    old=e.getChild2(); now=visitExp((Exp)old); if(now!=old) e.setChild2(now);
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At sub expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atSub(Exp e)
  {
    IR old, now;
    old=e.getChild1(); now=visitExp((Exp)old); if(now!=old) e.setChild1(now);
    old=e.getChild2(); now=visitExp((Exp)old); if(now!=old) e.setChild2(now);
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At mul expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atMul(Exp e)
  {
    IR old, now;
    old=e.getChild1(); now=visitExp((Exp)old); if(now!=old) e.setChild1(now);
    old=e.getChild2(); now=visitExp((Exp)old); if(now!=old) e.setChild2(now);
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At div expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atDiv(Exp e)
  {
    IR old, now;
    old=e.getChild1(); now=visitExp((Exp)old); if(now!=old) e.setChild1(now);
    old=e.getChild2(); now=visitExp((Exp)old); if(now!=old) e.setChild2(now);
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At mod expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atMod(Exp e)
  {
    IR old, now;
    old=e.getChild1(); now=visitExp((Exp)old); if(now!=old) e.setChild1(now);
    old=e.getChild2(); now=visitExp((Exp)old); if(now!=old) e.setChild2(now);
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At and expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atAnd(Exp e)
  {
    IR old, now;
    old=e.getChild1(); now=visitExp((Exp)old); if(now!=old) e.setChild1(now);
    old=e.getChild2(); now=visitExp((Exp)old); if(now!=old) e.setChild2(now);
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At or expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atOr(Exp e)
  {
    IR old, now;
    old=e.getChild1(); now=visitExp((Exp)old); if(now!=old) e.setChild1(now);
    old=e.getChild2(); now=visitExp((Exp)old); if(now!=old) e.setChild2(now);
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At xor expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atXor(Exp e)
  {
    IR old, now;
    old=e.getChild1(); now=visitExp((Exp)old); if(now!=old) e.setChild1(now);
    old=e.getChild2(); now=visitExp((Exp)old); if(now!=old) e.setChild2(now);
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At EQ expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atCmpEq(Exp e) // ==
  {
    IR old, now;
    old=e.getChild1(); now=visitExp((Exp)old); if(now!=old) e.setChild1(now);
    old=e.getChild2(); now=visitExp((Exp)old); if(now!=old) e.setChild2(now);
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At NE expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atCmpNe(Exp e) // !=
  {
    IR old, now;
    old=e.getChild1(); now=visitExp((Exp)old); if(now!=old) e.setChild1(now);
    old=e.getChild2(); now=visitExp((Exp)old); if(now!=old) e.setChild2(now);
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At GT expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atCmpGt(Exp e) // >
  {
    IR old, now;
    old=e.getChild1(); now=visitExp((Exp)old); if(now!=old) e.setChild1(now);
    old=e.getChild2(); now=visitExp((Exp)old); if(now!=old) e.setChild2(now);
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At GE expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atCmpGe(Exp e) // >=
  {
    IR old, now;
    old=e.getChild1(); now=visitExp((Exp)old); if(now!=old) e.setChild1(now);
    old=e.getChild2(); now=visitExp((Exp)old); if(now!=old) e.setChild2(now);
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At LT expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atCmpLt(Exp e) // <
  {
    IR old, now;
    old=e.getChild1(); now=visitExp((Exp)old); if(now!=old) e.setChild1(now);
    old=e.getChild2(); now=visitExp((Exp)old); if(now!=old) e.setChild2(now);
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At LE expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atCmpLe(Exp e) // <=
  {
    IR old, now;
    old=e.getChild1(); now=visitExp((Exp)old); if(now!=old) e.setChild1(now);
    old=e.getChild2(); now=visitExp((Exp)old); if(now!=old) e.setChild2(now);
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At L-shift expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atLShift(Exp e) // <<
  {
    IR old, now;
    old=e.getChild1(); now=visitExp((Exp)old); if(now!=old) e.setChild1(now);
    old=e.getChild2(); now=visitExp((Exp)old); if(now!=old) e.setChild2(now);
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At arithmetic R-shift expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atARShift(Exp e) // >>
  {
    IR old, now;
    old=e.getChild1(); now=visitExp((Exp)old); if(now!=old) e.setChild1(now);
    old=e.getChild2(); now=visitExp((Exp)old); if(now!=old) e.setChild2(now);
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At logical R-shift expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atRShift(Exp e) // >>>
  {
    IR old, now;
    old=e.getChild1(); now=visitExp((Exp)old); if(now!=old) e.setChild1(now);
    old=e.getChild2(); now=visitExp((Exp)old); if(now!=old) e.setChild2(now);
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At not expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atNot(Exp e) // ~
  {
    IR old, now;
    old=e.getChild1(); now=visitExp((Exp)old); if(now!=old) e.setChild1(now);
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At negative expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atNeg(Exp e) // -
  {
    IR old, now;
    old=e.getChild1(); now=visitExp((Exp)old); if(now!=old) e.setChild1(now);
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At address expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atAddr(Exp e) // &@
  {
    IR old, now;
    old=e.getChild1(); now=visitExp((Exp)old); if(now!=old) e.setChild1(now);
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At cast expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atConv(Exp e)
  {
    IR old, now;
    old=e.getChild1(); now=visitExp((Exp)old); if(now!=old) e.setChild1(now);
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At decay expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atDecay(Exp e)
  {
    IR old, now;
    old=e.getChild1(); now=visitExp((Exp)old); if(now!=old) e.setChild1(now);
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At undecay expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atUndecay(Exp e)
  {
    IR old, now;
    old=e.getChild1(); now=visitExp((Exp)old); if(now!=old) e.setChild1(now);
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At indirection expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atContents(Exp e) // *@
  {
    IR old, now;
    old=e.getChild1(); now=visitExp((Exp)old); if(now!=old) e.setChild1(now);
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At assign expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atAssign(Exp e)
  {
    IR old, now;
    old=e.getChild1(); now=visitExp((Exp)old); if(now!=old) e.setChild1(now);
    old=e.getChild2(); now=visitExp((Exp)old); if(now!=old) e.setChild2(now);
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At offset(difference of address) expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atOffset(Exp e)
  {
    IR old, now;
    old=e.getChild1(); now=visitExp((Exp)old); if(now!=old) e.setChild1(now);
    old=e.getChild2(); now=visitExp((Exp)old); if(now!=old) e.setChild2(now);
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At logical-and expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atLgAnd(Exp e) // &&
  {
    IR old, now;
    old=e.getChild1(); now=visitExp((Exp)old); if(now!=old) e.setChild1(now);
    old=e.getChild2(); now=visitExp((Exp)old); if(now!=old) e.setChild2(now);
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At logical-or expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atLgOr(Exp e) // ||
  {
    IR old, now;
    old=e.getChild1(); now=visitExp((Exp)old); if(now!=old) e.setChild1(now);
    old=e.getChild2(); now=visitExp((Exp)old); if(now!=old) e.setChild2(now);
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At selection expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atSelect(Exp e) // ?:
  {
    IR old, now;
    old=e.getChild1(); now=visitExp((Exp)old); if(now!=old) e.setChild1(now);
    old=e.getChild2(); now=visitExp((Exp)old); if(now!=old) e.setChild2(now);
    old=e.getChild(3); now=visitExp((Exp)old); if(now!=old) e.setChild(3,now);
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At comma expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atComma(Exp e)
  {
    IR old, now;
    old=e.getChild1(); now=visitExp((Exp)old); if(now!=old) e.setChild1(now);
    old=e.getChild2(); now=visitExp((Exp)old); if(now!=old) e.setChild2(now);
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At logical-not expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atEqZero(Exp e) // !
  {
    IR old, now;
    old=e.getChild1(); now=visitExp((Exp)old); if(now!=old) e.setChild1(now);
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At pre-operator expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atPre(int op,Exp e) // ++@ --@
  {
    IR old, now;
    old=e.getChild1(); now=visitExp((Exp)old); if(now!=old) e.setChild1(now);
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At post-operator expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atPost(int op,Exp e) // @++ @--
  {
    IR old, now;
    old=e.getChild1(); now=visitExp((Exp)old); if(now!=old) e.setChild1(now);
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At add-assign expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atAddAssign(Exp e)
  {
    IR old, now;
    old=e.getChild1(); now=visitExp((Exp)old); if(now!=old) e.setChild1(now);
    old=e.getChild2(); now=visitExp((Exp)old); if(now!=old) e.setChild2(now);
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At sub-assign expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atSubAssign(Exp e)
  {
    IR old, now;
    old=e.getChild1(); now=visitExp((Exp)old); if(now!=old) e.setChild1(now);
    old=e.getChild2(); now=visitExp((Exp)old); if(now!=old) e.setChild2(now);
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At mul-assign expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atMulAssign(Exp e)
  {
    IR old, now;
    old=e.getChild1(); now=visitExp((Exp)old); if(now!=old) e.setChild1(now);
    old=e.getChild2(); now=visitExp((Exp)old); if(now!=old) e.setChild2(now);
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At div-assign expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atDivAssign(Exp e)
  {
    IR old, now;
    old=e.getChild1(); now=visitExp((Exp)old); if(now!=old) e.setChild1(now);
    old=e.getChild2(); now=visitExp((Exp)old); if(now!=old) e.setChild2(now);
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At mod-assign expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atModAssign(Exp e)
  {
    IR old, now;
    old=e.getChild1(); now=visitExp((Exp)old); if(now!=old) e.setChild1(now);
    old=e.getChild2(); now=visitExp((Exp)old); if(now!=old) e.setChild2(now);
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At L-shift-assign expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atLShiftAssign(Exp e)
  {
    IR old, now;
    old=e.getChild1(); now=visitExp((Exp)old); if(now!=old) e.setChild1(now);
    old=e.getChild2(); now=visitExp((Exp)old); if(now!=old) e.setChild2(now);
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At R-shift-assign expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atRShiftAssign(Exp e)
  {
    IR old, now;
    old=e.getChild1(); now=visitExp((Exp)old); if(now!=old) e.setChild1(now);
    old=e.getChild2(); now=visitExp((Exp)old); if(now!=old) e.setChild2(now);
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At and-assign expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atAndAssign(Exp e)
  {
    IR old, now;
    old=e.getChild1(); now=visitExp((Exp)old); if(now!=old) e.setChild1(now);
    old=e.getChild2(); now=visitExp((Exp)old); if(now!=old) e.setChild2(now);
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At or-assign expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atOrAssign(Exp e)
  {
    IR old, now;
    old=e.getChild1(); now=visitExp((Exp)old); if(now!=old) e.setChild1(now);
    old=e.getChild2(); now=visitExp((Exp)old); if(now!=old) e.setChild2(now);
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At xor-assign expression node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atXorAssign(Exp e)
  {
    IR old, now;
    old=e.getChild1(); now=visitExp((Exp)old); if(now!=old) e.setChild1(now);
    old=e.getChild2(); now=visitExp((Exp)old); if(now!=old) e.setChild2(now);
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At expression list node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atExpList(ExpListExp e)
  {
    Exp old, now;
    for( int i=0; (old=e.getExp(i))!=null; i++ )
      { now=visitExp(old); if(now!=old) e.setExp(i,now); }
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * At expression repeatation node.
   *
   * @param   e Exp
   * @return  Exp
  **/
  protected Exp atExpRepeat(Exp e)
  {
    IR old, now;
    old=e.getChild1(); now=visitExp((Exp)old); if(now!=old) e.setChild1(now);
    old=e.getChild2(); now=visitExp((Exp)old); if(now!=old) e.setChild2(now);
    return e;
  }
  //-------------------------------------------------------------------
 /**
  * At block statement node.
  *
  * @param  s BlockStmt
 **/
 protected SubpDefinition atSubpDefinition(SubpDefinition s)
 {
   SubpDefinition lSubpDefinition = s;
   Subp lSubp = lSubpDefinition.getSubpSym();
   message(3,"\n atSubpDefinition " + lSubp.getName());

   ((HIR_Impl)hir).hirRoot.symRoot.symTableCurrent
      = lSubp.getSymTable();
   ((HIR_Impl)hir).hirRoot.symRoot.symTableCurrentSubp
       = lSubp.getSymTable();
   ((HIR_Impl)hir).hirRoot.symRoot.subpCurrent = lSubp;
   visitStmt(lSubpDefinition.getInitiationPart());
   visitStmt(lSubpDefinition.getHirBody());
   ((HIR_Impl)hir).hirRoot.symRoot.symTableCurrent
      = ((HIR_Impl)hir).hirRoot.symRoot.symTableRoot;
   ((HIR_Impl)hir).hirRoot.symRoot.subpCurrent = null;
   return lSubpDefinition;
 } // atSubpDefinition

 //-------------------------------------------------------------------
//##70 BEGIN
 /**
  * atInfStmt parses the pragma body in the form of String
  * and change its symbols to instances of Sym (Var, Subp, Label,
  *  Const) and items enclosed in parenthesis to IrList changing its
  * elements to Sym, etc.
  * This may be called twice for the same pragma. In the second
  * call, return pInf unchanged because it is already in the final form.
  * @param e InfStmt generated by Parser.
  * @return InfStmt having Sym elements and (nested) IrList.
  */
protected InfStmt atInfStmt( InfStmt pInf )
  {
    IrList lInfList = pInf.getInfList();
    //## System.out.print("\n InfStmt " + e.toString() + " infList " + lInfList); //###
    if (lInfList == null)  //##
      return pInf; //##
    if (((InfStmtImpl)pInf).fReformed) {
      message(4,"\natInfStmt reformed " + pInf); //##71
      return pInf;
    }
    String lPragmaKind = pInf.getInfKind(); //##
    String lPragmaBody;
    Object lBodyItem = lInfList.get(1);
    message(4,"\natInfStmt " + lPragmaKind + " " + lBodyItem);
    if ((lBodyItem instanceof IrList)&&(((IrList)lBodyItem).size() == 1)&&
        (((IrList)lBodyItem).get(0) instanceof String)) {
      lPragmaBody = (String)((IrList)lBodyItem).get(0);
    }else if (lBodyItem instanceof String) {
      lPragmaBody = (String)lBodyItem;
    }else {
      // pInf is already in the final form or lPragmaBody is ""
      //##71 BEGIN
      message(4," already processed " + pInf); //##71
      String lInfKind = pInf.getInfKind();
      if (lInfKind == "optControl"){
        String lSubKind = pInf.getInfSubkindOf("optControl");
        if (lSubKind == "safeArray") {
          IrList lItemList = pInf.getInfList("optControl");
          int lIndex = 0;
          for (Iterator lIt = lItemList.iterator();
               lIt.hasNext(); lIndex++) {
            if (lIndex <= 0)
              continue;
            Object lItem = lIt.next();
            if (lItem instanceof Var) {
              message(4," add to safeArray " + ((Sym)lItem).getName());
              ((SymImpl)sym).symRoot.safeArray.add(lItem);
            }
          }
        /*
        //##81 BEGIN
        }else if (lSubKind == "safeArrayAll") {
          ((SubpImpl)toHir.symRoot.subpCurrent).fSafeArrayAll = true;
        //##81 END
        */
        }
      }
      //##71 END
      return pInf;
    }
    message(4,"body " + lPragmaBody);
    InfStmt lResult = null;
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
     IrList lIrList = new IrListImpl(toHir.hirRoot);
     IrList lList;
     String lItem;
     while (lParseString.hasNext()) {
       lItem = lParseString.getNextToken();
       message(4," item " + lItem + " kind " + lParseString.getTokenKind());
       lList = processPragmaItem(lParseString, lItem, lIrList);
       if ((lList != null)&&(lList != lIrList)) {
         message(4,"\n add list " + lList + " to list " + lIrList);
         lIrList.add(lList);
         lList = null;
       }
     }
     message(4,"\n lIrList " + lIrList.toString() + " size " + lIrList.size()); //###
     if (! lIrList.isEmpty())
       message(4," 1stElem " + lIrList.get(0) + " " + lIrList.get(0).getClass());
       if ((lIrList.size() == 1)&&(lIrList.get(0) instanceof IrList)) {
         lResult = hir.infStmt(lPragmaKind.intern(), (IrList)lIrList.get(0));
       }else {
         lResult = hir.infStmt(lPragmaKind.intern(), lIrList);
       }
   }else {
     // (lPragmaKind == "")||(lPragmaBody =="")
     IrList lIrList = new IrListImpl(toHir.hirRoot);
     if (lPragmaBody != "") {
       lIrList.add(sym.stringConst(lPragmaBody));
     }
     lResult = hir.infStmt( lPragmaKind.intern(), lIrList );
   }
   message(4,"\n lResult " + lResult.toString() + " " + lResult.getInfList(lPragmaKind)); //###
    if (lResult.getInfList(lPragmaKind) instanceof IrList) {
      pInf.replaceThisStmtWith(lResult);
    }
    ((InfStmtImpl)pInf).fReformed = true;
    return lResult;
  } // atInfStmt

  //-------------------------------------------------------------------
  //##70 BEGIN
IrList processPragmaItem( ParseString pParseString, String pNextItem,
              IrList pList )
{
  message(5," processPragmaItem " + pNextItem + " kind " +
          pParseString.getTokenKind() + " pList " + pList.toStringShort());
  IrList lList;
  if (pNextItem == "(") {
    IrList lInnerList = new IrListImpl(toHir.hirRoot);
    String lListItem;
    boolean lExpectComma = false;
    while (pParseString.hasNext()) {
      lListItem = pParseString.getNextToken();
      if (lExpectComma &&(lListItem == ",")) {
        // Advance to the next Item
        lListItem = pParseString.getNextToken();
        lExpectComma = false;
      }
      if (lListItem == ")")
        break;
      lList = processPragmaItem(pParseString, lListItem, lInnerList);
      if ((lList != null)&&(lList != lInnerList)&&(lList != pList)) {
        message(4,"\n add list " + lList.getIndex() + " to list " + pList.getIndex());
        lInnerList.add(lList);
        lList = null;
      }
      lExpectComma = true;
    }
    pList.add(lInnerList);
  }else if (pNextItem == ")") {
    // Do nothing.
  }else {
    int lKind = pParseString.getTokenKind();
    if (lKind == pParseString.IDENTIFIER) {
      Sym lSym = ((SymImpl)sym).symRoot.symTableCurrent.search(pNextItem);
      //## System.out.print(" SymTableCurrent " + ((SymImpl)sym).symRoot.symTableCurrent.getOwner() + " lSym " + lSym ); //###
      if (lSym != null) {
        message(4," add " + lSym);
        pList.add(lSym);
      }else {
        message(4," unknown name " + pNextItem);
        pList.add(pNextItem);
      }
    }else if (lKind == pParseString.DIGITS) {
       Const lConst;
      long lValue = Long.parseLong(pNextItem,10);
      if (lValue <= Integer.MAX_VALUE)
        lConst = sym.intConst(pNextItem, ((SymImpl)sym).symRoot.typeInt);
      else
        lConst = sym.intConst(pNextItem, ((SymImpl)sym).symRoot.typeLong);
      message(4," add " + lConst);
      pList.add(lConst);
    }else {
      // STRING and UNDEFINED. Treat as String constant.
      Const lConst = sym.stringConst(pNextItem);
      //## pList.add(hir.constNode(lConst));
      message(4," add " + lConst);
      pList.add(lConst);
    }
  }
  message(5," pList " + pList + " size " + pList.size() );
  return pList;
} // processPragmaItem

//-------------------------------------------------------------------

//##70 END

} // ToHirVisit
