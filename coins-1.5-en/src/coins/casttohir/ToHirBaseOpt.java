/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.casttohir;

import coins.ir.hir.AssignStmt;
import coins.ir.hir.BlockStmt;
import coins.ir.hir.Exp;
import coins.ir.hir.ExpStmt;
import coins.ir.hir.HIR;
import coins.ir.hir.IfStmt;
import coins.ir.hir.JumpStmt;
import coins.ir.hir.LabeledStmt;
import coins.ir.hir.LoopStmt;
import coins.ir.hir.ReturnStmt;
import coins.ir.hir.Stmt;
import coins.ir.hir.SwitchStmt;
import coins.sym.Const;
import coins.sym.Label;
import coins.sym.Sym;

/**
 * Do simple HIR-base optimizations such as elimination of useless statements,
 * expansion of control statements with constant conditional expression.
 * <pre>
 * 1. Elimination of useless statements
 *
 * Unreachable ExtStmt/return/goto --&gt; eliminate
 * Jump to the next statement      --&gt; eliminate
 * Unrefered label      --&gt; eliminate
 * Successive label     --&gt; combine the labels
 * Empty block          --&gt; eliminate
 * Expression statement --&gt; eliminate except for side effect part.
 *
 * 2. Expansion of if-statement
 *
 * if(e    ); --&gt; side_effect_of_e;
 * if(true ) THEN; else ELSE;
 * --&gt; { THEN; goto LABEL_END; ELSE; LABEL_END:; }
 * if(false) THEN; else ELSE;
 * --&gt; { ELSE; goto LABEL_END; THEN; LABEL_END:; }
 * unreachable if(e) THEN; else ELSE;
 * --&gt; { goto LABEL_END; THEN; goto LABEL_END; ELSE; LABEL_END:; }
 *
 * 3. Expansion of while-statement
 *
 * while(false) BODY;
 * --&gt; { goto LABEL_BREAK; BODY; LABEL_BREAK:; }
 *
 * Expansion of for-statement
 *
 * for(INIT;false;STEP) BODY;
 * --&gt; { INIT; goto LABEL_BREAK; BODY; STEP; LABEL_BREAK:; }
 *
 * 5. Expansion of do-while statement
 *
 * do BODY; while(false); --&gt; BODY;
 * unreachable do BODY; while(false);
 * --&gt; { goto LABEL_BREAK; BODY; LABEL_BREAK:; }
 *
 * 6. Expansion of switch statement
 *
 * switch(CONST2)
 * {
 * case CONST1: STMT1;
 * case CONST2: STMT2;
 * default:     STMT3;
 * }
 * --&gt;
 * {
 *   goto LABEL_CONST2;
 *   STMT1;
 *   LABEL_CONST2:;
 *   STMT2;
 *   STMT3;
 *   LABEL_BREAK:;
 * }
 *
 * unreachable switch(...)
 * {
 * case CONST1: STMT1;
 * case CONST2: STMT2;
 * default:     STMT3;
 * }
 * --&gt;
 * {
 *   goto LABEL_BREAK;
 *   STMT1;
 *   STMT2;
 *   STMT3;
 *   LABEL_BREAK:;
 * }
 * </pre>
 * @author  Shuichi Fukuda
**/
public class ToHirBaseOpt extends ToHirVisit
{
  private final ToHir            toHir;
  private final ToHirCast        toCast;
  private final HIR              hir;
  private final Sym              sym;
  private final SideEffectBuffer buffer;
  private final SideEffectCutter cutter;

  private boolean reachable; // Reachability of the now processing statement

  //-------------------------------------------------------------------
  /**
    Constructor.

    @param  tohir Offers cooperation with the object of other packages.
  **/
  public ToHirBaseOpt(ToHir tohir)
  {
    super(tohir);
    //##97 message(1,"ToHirBaseOpt\n"); //##71
    toHir  = tohir;
    message(1,"ToHirBaseOpt\n"); //##97
    toCast = new ToHirCast(tohir);
    hir    = tohir.hirRoot.hir;
    sym    = tohir.hirRoot.sym;
    buffer = new SideEffectBuffer(tohir);
    cutter = new SideEffectCutter(tohir,buffer);
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
    toHir.debug.print( level, "BO", mes );
  }
  //-------------------------------------------------------------------
  /**
   * At block statement node.
   *
   * @param  s BlockStmt
  **/
  protected void atBlock(BlockStmt s)
  {
    if( s.getSubpBodyFlag() )
      reachable = true;

    super.atBlock(s);
    if( s.getFirstStmt()==null )
      s.deleteThisStmt();
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

    Label label = s.getLabel();
    if( label.getHirRefCount()==0 // If this label is not refered and
    &&( label.getLabelKind()==Label.JUMP_LABEL
    ||  label.getLabelKind()==Label.LOOP_STEP_LABEL )) // CONTINUE_LABEL ?
    //if( s.getLabel().getLabelKind()==Label.JUMP_LABEL
    //&&  s.getLabel().getHirRefCount()==0 ) // If this label is not refered
    {
      if (fDbgLevel > 3) //##67
        message(6,"not referenced --> delete "+s);
      s.deleteThisStmt(); // Delete this statement.
      return;
    }
    reachable = true;

    for( Stmt prev = s.getPreviousStmt(); prev!=null; )
      if( prev.getOperator()==HIR.OP_LABELED_STMT ) // If previous statement is a LabeledStmt
      {
        if (fDbgLevel > 3) //##67
          message(6,"consecutive label --> merge "+s);
        ((LabeledStmt)prev).merge(s); // Combine the statements.
        s.deleteThisStmt();
        s = (LabeledStmt)prev;
        prev = s.getPreviousStmt();
      }
      else if( prev.getOperator()==HIR.OP_JUMP // If previous statement is jump
      && ((JumpStmt)prev).getLabel().getHirPosition()==s ) // to this statement,
      {
        Stmt pp = prev.getPreviousStmt();
        if (fDbgLevel > 3) //##67
          message(6,"zero jump --> delete "+prev);
        prev.deleteThisStmt(); // Delete the jump statement.
        prev = pp;
      }
      else
        break;
  }
  //-------------------------------------------------------------------
  /**
   * At assign statement node.
   *
   * @param  s AssignStmt
  **/
  protected void atAssignStmt(AssignStmt s)
  {
    if( !reachable )
    {
      if (fDbgLevel > 3) //##67
        message(6,"unreachable --> delete "+s);
      s.deleteThisStmt();
    }
  }
  //-------------------------------------------------------------------
  /**
   * At if statement node.
   *
   * @param  s IfStmt
  **/
  protected void atIf(IfStmt s)
  {
    Exp cond = s.getIfCondition();

    // visit then
    boolean reachableif = reachable;
    reachable = reachableif && cond.getSym()!=toHir.symRoot.boolConstFalse;
    visitStmt( s.getThenPart().getStmt() );
    boolean reachablethen = reachable;
    // visit else
    reachable = reachableif && cond.getSym()!=toHir.symRoot.boolConstTrue;
    visitStmt( s.getElsePart().getStmt() );
    reachable |= reachablethen;

    Stmt then0 = s.getThenPart().getStmt();
    Stmt else0 = s.getElsePart().getStmt();

    // optimize
    if( reachableif ) // if is reachable
    {
      if( cond.getSym()==toHir.symRoot.boolConstTrue )
        if( else0!=null )
        {
          if (fDbgLevel > 3) //##67
            message(6,"if(true) THEN; else ELSE; "+s);
          // if(true) THEN; else ELSE; --> { THEN; goto LABEL_END; ELSE; LABEL_END:; }
          Label endlabel = s.getEndLabel();
          endlabel.setLabelKind(Label.JUMP_LABEL);
          BlockStmt block = buffer.getBlockStmt(else0);
          block.addLastStmt( hir.labeledStmt(endlabel,null) );
          block.addFirstStmt( hir.jumpStmt(endlabel) );
          block.addFirstStmt( then0 );
          s.replaceThisStmtWith(block);
        }
        else
        {
          if (fDbgLevel > 3) //##67
            message(6,"if(true) THEN; "+s);
          // if(true) THEN; --> THEN;
          s.replaceThisStmtWith(then0);
        }
      else if( cond.getSym()==toHir.symRoot.boolConstFalse )
        if( then0!=null )
        {
          if (fDbgLevel > 3) //##67
            message(6,"if(false) THEN; else ELSE; "+s);
          // if(false) THEN; else ELSE; --> { ELSE; goto LABEL_END; THEN; LABEL_END:; }
          Label endlabel = s.getEndLabel();
          endlabel.setLabelKind(Label.JUMP_LABEL);
          BlockStmt block = buffer.getBlockStmt(then0);
          block.addLastStmt( hir.labeledStmt(endlabel,null) );
          block.addFirstStmt( hir.jumpStmt(endlabel) );
          block.addFirstStmt( else0 );
          s.replaceThisStmtWith(block);
        }
        else
        {
          if (fDbgLevel > 3) //##67
            message(6,"if(false); else ELSE; "+s);
          // if(false); else ELSE; --> ELSE;
          s.replaceThisStmtWith(else0);
        }
      else
        if( then0==null
        &&  else0==null )
        {
          if (fDbgLevel > 3) //##67
            message(6,"if(e); "+s);
          // if(e); --> side_effect_of_e;
          cutter.visitExp(cond);
          buffer.addToStmtPrev(s,false);
          s.deleteThisStmt();
        }
    }
    else // if is unreachable
    {
      Label endlabel = s.getEndLabel();
      endlabel.setLabelKind(Label.JUMP_LABEL);
      if( then0!=null )
        if( else0!=null )
        {
          if (fDbgLevel > 3) //##67
            message(6,"unreachable if(e) THEN; else ELSE; "+s);
          // unreachable if(e) THEN; else ELSE;
          // --> { goto LABEL_END; THEN; goto LABEL_END; ELSE; LABEL_END:; }
          BlockStmt block = buffer.getBlockStmt(else0);
          block.addLastStmt( hir.labeledStmt(endlabel,null) );
          block.addFirstStmt( hir.jumpStmt(endlabel) );
          block.addFirstStmt( then0 );
          block.addFirstStmt( hir.jumpStmt(endlabel) );
          s.replaceThisStmtWith(block);
        }
        else
        {
          if (fDbgLevel > 3) //##67
            message(6,"unreachable if(e) THEN; "+s);
          // unreachable if(e) THEN; --> { goto LABEL_END; THEN; LABEL_END:; }
          BlockStmt block = buffer.getBlockStmt(then0);
          block.addLastStmt( hir.labeledStmt(endlabel,null) );
          block.addFirstStmt( hir.jumpStmt(endlabel) );
          s.replaceThisStmtWith(block);
        }
      else
        if( else0!=null )
        {
          if (fDbgLevel > 3) //##67
            message(6,"unreachable if(e); else ELSE; "+s);
          // unreachable if(e); else ELSE; --> { goto LABEL_END; ELSE; LABEL_END:; }
          BlockStmt block = buffer.getBlockStmt(else0);
          block.addLastStmt( hir.labeledStmt(endlabel,null) );
          block.addFirstStmt( hir.jumpStmt(endlabel) );
          s.replaceThisStmtWith(block);
        }
        else
        {
          if (fDbgLevel > 3) //##67
            message(6,"unreachable if(e); "+s);
          // unreachable if(e); --> delete
          s.deleteThisStmt();
        }
    }
  }
  //-------------------------------------------------------------------
  /**
   * At while statement node.
   *
   * @param  s LoopStmt
  **/
  protected void atWhile(LoopStmt s)
  {
    // delete continue lable
    Label continuelabel = s.getLoopStepLabel();
    if( continuelabel.getHirRefCount()==0
    &&  continuelabel.getHirPosition()!=null ) // If continue is not included
      continuelabel.getHirPosition().deleteThisStmt(); // delete continue label.

    Exp cond = s.getLoopStartCondition();

    // visit body
    boolean reachablewhile = reachable;
    reachable = cond.getSym()!=toHir.symRoot.boolConstFalse;
    visitStmt( ((LabeledStmt)s.getLoopBodyPart()).getStmt() );
    reachable |= reachablewhile;

    Stmt body = ((LabeledStmt)s.getLoopBodyPart()).getStmt();

    // optimize
    if( cond.getSym()==toHir.symRoot.boolConstFalse )
    {
      Label endlabel = s.getLoopEndLabel();
      endlabel.setLabelKind(Label.JUMP_LABEL);
      if( body==null )
      {
        if (fDbgLevel > 3) //##67
          message(6,"while(false); "+s);
        // while(false); --> delete
        s.deleteThisStmt();
      }
      else
      {
        if (fDbgLevel > 3) //##67
          message(6,"while(false) BODY; "+s);
        // while(false) BODY; --> { goto LABEL_BREAK; BODY; LABEL_BREAK:; }
        BlockStmt block = buffer.getBlockStmt( body );
        block.addLastStmt( hir.labeledStmt(endlabel,null) );
        block.addFirstStmt( hir.jumpStmt(endlabel) );
        s.replaceThisStmtWith(block);
      }
    }
  }
  //-------------------------------------------------------------------
  /**
   * At for statement node.
   *
   * @param  s LoopStmt
  **/
  protected void atFor(LoopStmt s)
  {
    // delete continue lable
    Label continuelabel = s.getLoopStepLabel();
    if( continuelabel.getHirRefCount()==0
    &&  continuelabel.getHirPosition()!=null ) // If continue is not included
      continuelabel.getHirPosition().deleteThisStmt(); // delete continue label.

    Exp cond = s.getLoopStartCondition();

    // visit init
    boolean reachablefor = reachable;
    visitStmt(s.getLoopInitPart());
    // visit body
    reachable = cond.getSym()!=toHir.symRoot.boolConstFalse;
    visitStmt( ((LabeledStmt)s.getLoopBodyPart()).getStmt() );
    //visit step
    visitStmt(s.getLoopStepPart());
    reachable |= reachablefor;

    Stmt init = s.getLoopInitPart();
    Stmt body = ((LabeledStmt)s.getLoopBodyPart()).getStmt();
    Stmt step = s.getLoopStepPart();

    // optimize
    if( cond.getSym()==toHir.symRoot.boolConstFalse )
    {
      Label endlabel = s.getLoopEndLabel();
      endlabel.setLabelKind(Label.JUMP_LABEL);
      if( body!=null )
      {
        if (fDbgLevel > 3) //##67
          message(6,"for(INIT;false;STEP) BODY; "+s);
        // for(INIT;false;STEP) BODY;
        // --> { INIT; goto LABEL_BREAK; BODY; STEP; LABEL_BREAK:; }
        BlockStmt block = buffer.getBlockStmt(body);
        block.addLastStmt(step);
        block.addLastStmt( hir.labeledStmt(endlabel,null) );
        block.addFirstStmt( hir.jumpStmt(endlabel) );
        block.addFirstStmt(init);
        s.replaceThisStmtWith(block);
      }
      else if( step!=null )
      {
        if (fDbgLevel > 3) //##67
          message(6,"for(INIT;false;STEP); "+s);
        // for(INIT;false;STEP); --> { INIT; goto LABEL_BREAK; STEP; LABEL_BREAK:; }
        BlockStmt block = buffer.getBlockStmt(step);
        block.addLastStmt( hir.labeledStmt(endlabel,null) );
        block.addFirstStmt( hir.jumpStmt(endlabel) );
        block.addFirstStmt(init);
        s.replaceThisStmtWith(block);
      }
      else if( init!=null )
      {
        if (fDbgLevel > 3) //##67
          message(6,"for(INIT;false;); "+s);
        // for(INIT;false;); --> INIT;
        s.replaceThisStmtWith(init);
      }
      else
      {
        if (fDbgLevel > 3) //##67
          message(6,"for(;false;); "+s);
        // for(;false;); --> delete
        s.deleteThisStmt();
      }
    }
  }
  //-------------------------------------------------------------------
  /**
   * At do-while statement node.
   *
   * @param  s LoopStmt
  **/
  protected void atUntil(LoopStmt s)
  {
    // delete continue lable
    Label continuelabel = s.getLoopStepLabel();
    if( continuelabel.getHirRefCount()==0
    &&  continuelabel.getHirPosition()!=null ) // If continue is not included
      continuelabel.getHirPosition().deleteThisStmt(); // delete continue label

    Exp cond = s.getLoopEndCondition();

    // visit body
    boolean reachableuntil = reachable;
    reachable |= cond.getSym()!=toHir.symRoot.boolConstFalse;
    visitStmt( ((LabeledStmt)s.getLoopBodyPart()).getStmt() );
    reachable |= reachableuntil;

    Stmt body = ((LabeledStmt)s.getLoopBodyPart()).getStmt();

    // optimize
    if( cond.getSym()==toHir.symRoot.boolConstFalse )
    {
      if( reachableuntil )
        if( body!=null )
        {
          if (fDbgLevel > 3) //##67
            message(6,"do BODY; while(false); "+s);
          // do BODY; while(false); --> BODY;
          BlockStmt block = buffer.getBlockStmt( body );
          s.replaceThisStmtWith(block);
        }
        else
        {
          if (fDbgLevel > 3) //##67
            message(6,"do; while(false); "+s);
          // do; while(false); --> delete
          s.deleteThisStmt();
        }
      else
        if( body!=null )
        {
          if (fDbgLevel > 3) //##67
            message(6,"unreachable do BODY; while(false); "+s);
          // unreachable do BODY; while(false);
          // --> { goto LABEL_BREAK; BODY; LABEL_BREAK:; }
          Label endlabel = s.getLoopEndLabel();
          endlabel.setLabelKind(Label.JUMP_LABEL);
          BlockStmt block = buffer.getBlockStmt( body );
          block.addLastStmt( hir.labeledStmt(endlabel,null) );
          block.addFirstStmt( hir.jumpStmt(endlabel) );
          s.replaceThisStmtWith(block);
        }
        else
        {
          if (fDbgLevel > 3) //##67
            message(6,"unreachable do; while(false); "+s);
          // unreachable do; while(false); --> delete
          s.deleteThisStmt();
        }
    }
    else
    {
      if( !reachableuntil
      &&  body==null )
      {
        if (fDbgLevel > 3) //##67
          message(6,"unreachable do; while(e); "+s);
        // unreachable do; while(e); --> delete
        s.deleteThisStmt();
      }
    }
  }
  //-------------------------------------------------------------------
  /**
   * At goto statement node.
   *
   * @param  s JumpStmt
  **/
  protected void atJump(JumpStmt s)
  {
    if( !reachable )
    {
      if (fDbgLevel > 3) //##67
        message(6,"unreachable --> delete "+s);
      s.deleteThisStmt();
    }
    reachable = false;
  }
  //-------------------------------------------------------------------
  /**
   * At switch statement node.
   *
   * @param  s SwitchStmt
  **/
  protected void atSwitch(SwitchStmt s)
  {
    Exp  cond = s.getSelectionExp();
    Stmt body = s.getBodyStmt();

    if( !reachable ) // This is unreachable switch statement.
    {                // Delete unreachable statements in this switch.
      if (fDbgLevel > 3) //##67
        message(6,"unreachable switch(...) "+s);
      // unreachable switch(...)
      // {
      // case CONST1: STMT1;
      // case CONST2: STMT2;
      // default:     STMT3;
      // }
      // -->
      // {
      //   goto LABEL_BREAK;
      //   STMT1;
      //   STMT2;
      //   STMT3;
      //   LABEL_BREAK:;
      // }
      Label caselabel;
      for( int i=0; (caselabel=s.getCaseLabel(i))!=null; i++ )
        if( caselabel.getHirPosition()!=null )
          caselabel.getHirPosition().deleteThisStmt(); // Delete case label.
      if( s.getDefaultLabel().getHirPosition()!=null ) // If with default,
        s.getDefaultLabel().getHirPosition().deleteThisStmt(); // delete default label.

      Label endlabel  = s.getEndLabel();
      endlabel.setLabelKind(Label.JUMP_LABEL);
      BlockStmt block = buffer.getBlockStmt(body);
      block.addLastStmt( hir.labeledStmt(endlabel,null) );
      block.addFirstStmt( hir.jumpStmt(endlabel) );
      s.replaceThisStmtWith(block);
      visitStmt(block);
    }
    else if( cond.getOperator()==HIR.OP_CONST ) // const
    {
      if (fDbgLevel > 3) //##67
        message(6,"switch(CONST) "+s);
      // switch(CONST2)
      // {
      // case CONST1: STMT1;
      // case CONST2: STMT2;
      // default:     STMT3;
      // }
      // -->
      // {
      //   goto LABEL_CONST2;
      //   STMT1;
      //   LABEL_CONST2:;
      //   STMT2;
      //   STMT3;
      //   LABEL_BREAK:;
      // }
      // Delete labels that do not correspond to the switch selector,
      // but leave default label if there is no case label corresponding to
      // the switch selector.
      long val = ((Const)cond.getSym()).longValue();
      Const caseconst = null;
      Label caselabel = null;
      Label endlabel  = s.getEndLabel();
      endlabel.setLabelKind(Label.JUMP_LABEL);

      for( int i=0; (caseconst=s.getCaseConst(i))!=null; i++ )
        if( caseconst.longValue()==val ) // The label corresponding to the switch selector
          caselabel = s.getCaseLabel(i); // was found.
        else
          s.getCaseLabel(i).getHirPosition().deleteThisStmt(); // Delete other case labels.
      if( caselabel!=null ) // The is a case label corresponding to the switch selector.
      {
        if( s.getDefaultLabel().getHirPosition()!=null ) // If with default
          s.getDefaultLabel().getHirPosition().deleteThisStmt(); // delete default label.
      }
      else // The is no case label corresponding to the swtich selector.
      {
        if( s.getDefaultLabel().getHirPosition()!=null ) // If with default then
          caselabel = s.getDefaultLabel(); // make it the jump target.
        else // If without default then make the break target (endLabel)
          caselabel = endlabel; // as the jump target.
      }
      BlockStmt block = buffer.getBlockStmt(body);
      caselabel.setLabelKind(Label.JUMP_LABEL);
      block.addLastStmt( hir.labeledStmt(endlabel,null) );
      block.addFirstStmt( hir.jumpStmt(caselabel) );
      s.replaceThisStmtWith(block);
      reachable = false;
      visitStmt(block);
    }
    else
    {
      reachable = false;
      visitStmt(body);
      reachable |= s.getEndLabel().getHirRefCount()!=0;
    }
  }
  //-------------------------------------------------------------------
  /**
   * At return statement node.
   *
   * @param  s ReturnStmt
  **/
  protected void atReturn(ReturnStmt s)
  {
    if( !reachable )
    {
      if (fDbgLevel > 3) //##67
        message(6,"unreachable --> delete "+s);
      s.deleteThisStmt();
    }
  }
  //-------------------------------------------------------------------
  /**
   * At expression statement node.
   *
   * @param  s ExpStmt
  **/
  protected void atExpStmt(ExpStmt s)
  {
    if( !reachable )
    {
      if (fDbgLevel > 3) //##67
        message(6,"unreachable --> delete "+s);
    }
    else
    {
      cutter.visitExp( s.getExp() );
      buffer.addToStmtPrev(s,false);
    }
    s.deleteThisStmt();
  }
  //-------------------------------------------------------------------
}
