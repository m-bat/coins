/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.lparallel;
import coins.SymRoot; //##71
import coins.aflow.BBlock; //##70
import coins.ir.hir.AssignStmt;
import coins.ir.hir.BlockStmt;
import coins.ir.hir.Exp;
import coins.ir.hir.ExpStmt;
import coins.ir.hir.FunctionExp;
import coins.ir.hir.HIR;
import coins.ir.hir.HIR_Impl; //##71
import coins.ir.hir.IfStmt;
import coins.ir.hir.JumpStmt; //##70
import coins.ir.hir.LabeledStmt;
import coins.ir.hir.LoopStmt;
import coins.ir.hir.Stmt;
import coins.ir.hir.SubpNode;
import coins.ir.hir.SwitchStmt;
import coins.ir.hir.VarNode;
import coins.sym.Label; //##70
import coins.sym.Subp;
import coins.sym.Type;
import coins.sym.Var; //##71
import coins.sym.PointerType;
/**
*
* CheckLoopBody:
*
*  The class which investigates whether a pointer variable, a structure object, etc. are in the
*  LoopBody section.
*
*
**/
public class CheckLoopBody {
    private LoopTable fLoopTable;
    private LoopUtil fUtil;
    private PreDefinedFunctions fPreDefined;
    private Stmt InitStmt ;
    private Stmt StepStmt ;
    private Stmt BodyStmt ;
    protected SymRoot symRoot; //##71
    /**
    *
    * CheckLoopBody:
    *
    *  The class which investigates whether a pointer variable, a structure object, etc. are in the
    *  LoopBody section.
    *
    * @param pLoopTable  the table for loop
    * @param pUtil  utility for parallelization
    *
    **/
    public CheckLoopBody(LoopTable pLoopTable,LoopUtil pUtil) {
        fLoopTable = pLoopTable;
        fUtil = pUtil;
        //##71 fPreDefined = new PreDefinedFunctions();
        fPreDefined = new PreDefinedFunctions(
          fUtil.fResults.flowRoot.symRoot.sourceLanguage); //##71
        InitStmt = fLoopTable.LoopStmt.getLoopInitPart();
        BodyStmt = fLoopTable.LoopStmt.getLoopBodyPart();
        StepStmt = fLoopTable.LoopStmt.getLoopStepPart();
        symRoot = pUtil.fResults.flowRoot.symRoot; //##71
    }
    /**
    *
    *  HIR_CheckLoopBody:
    *
    *  It investigates whether a pointer variable, a structure object, etc. are in the
    *  LoopBody section.
    *
    *
    *
    **/
    public boolean  HIR_CheckLoopBody() {

        fUtil.Trace("---pass:CheckLoopBody",1);

        if (CheckLoopBodyPart(BodyStmt) == false) {
          fUtil.Trace("--- return false",2);
          return false;
        }
        if (CheckLoopBodyPart(StepStmt) == false) {
          fUtil.Trace("--- return false",2);
          return false;
        }
        fUtil.Trace("--- return true",2);
        return  true;

    }
    /**
    *
    *  CheckLoopBodyPart:
    *
    *  It investigates whether a pointer variable, a structure object, etc. are in the
    *  HIR-stmt.
    *
    * @param pstmt   HIR stmt
    *
    **/
    private boolean CheckLoopBodyPart(Stmt pstmt) {
        Stmt nextStmt;
        IfStmt stmtIF;
        LoopStmt  stmtLOOP;
        SwitchStmt  stmtSWITCH;
        int  op;
        if (pstmt != null) //##70
          fUtil.Trace(" CheckLoopBodyPart "+ pstmt.toString(),2); //##70
        nextStmt = pstmt;
        while (nextStmt != null) {
          if (fUtil.TraceFlag) //##70
            fUtil.Trace(" " + nextStmt.toString(), 4); //##70
          op = nextStmt.getOperator();
          switch (op) {
            case HIR.OP_IF:
              stmtIF = (IfStmt)nextStmt;
              if (CheckEXP(stmtIF.getIfCondition()) == false)
                return false;
              if (CheckLoopBodyPart(stmtIF.getThenPart()) == false)
                return false;
              if (CheckLoopBodyPart(stmtIF.getElsePart()) == false)
                return false;
              nextStmt = nextStmt.getNextStmt();
              break;
            case HIR.OP_FOR:
              stmtLOOP = (LoopStmt)nextStmt;
              if (CheckLoopBodyPart(stmtLOOP.getConditionalInitPart()) == false)
                return false;
              if (CheckLoopBodyPart(stmtLOOP.getLoopInitPart()) == false)
                return false;
              if (CheckEXP(stmtLOOP.getLoopStartCondition()) == false)
                return false;
              if (CheckLoopBodyPart(stmtLOOP.getLoopStepPart()) == false)
                return false;
              if (CheckLoopBodyPart(stmtLOOP.getLoopBodyPart()) == false)
                return false;
              nextStmt = nextStmt.getNextStmt();
              break;
            case HIR.OP_WHILE:
              stmtLOOP = (LoopStmt)nextStmt;
              if (CheckEXP(stmtLOOP.getLoopStartCondition()) == false)
                return false;
              if (CheckLoopBodyPart(stmtLOOP.getLoopBodyPart()) == false)
                return false;
              break;
            case HIR.OP_UNTIL:

              //case HIR.OP_INDEXED_LOOP:
              stmtLOOP = (LoopStmt)nextStmt;
              if (CheckLoopBodyPart(stmtLOOP.getLoopBodyPart()) == false)
                return false;
              if (CheckEXP(stmtLOOP.getLoopEndCondition()) == false)
                return false;
              break;
            case HIR.OP_LABELED_STMT:

              //##70 nextStmt = ((LabeledStmt)nextStmt).getStmt();
              //##70 BEGIN
              if (((LabeledStmt)nextStmt).getStmt() != null)
                nextStmt = ((LabeledStmt)nextStmt).getStmt();
              else
                nextStmt = nextStmt.getNextStmt();
                //##70 END
              break;
            case HIR.OP_JUMP:
              //##70 nextStmt = nextStmt.getNextStmt();
              //##70 break;
              //##70 BEGIN
              // Jump exit from loop should return false.
              Label lJumpLabel = ((JumpStmt)nextStmt).getLabel();
              BBlock lJumpTarget = fUtil.fResults.getBBlockForLabel(lJumpLabel);
              if (fLoopTable.BBlockList.contains(lJumpTarget)) {
                fUtil.Trace(" Jump within the loop " + lJumpLabel.getName(),1);
                nextStmt = nextStmt.getNextStmt();
                break;
              }else {
                fUtil.Trace(" Jump exit from the loop " + lJumpLabel.getName(),1);
                return false;
              }
              //##70 END
            case HIR.OP_SWITCH:
              stmtSWITCH = (SwitchStmt)nextStmt;
              if (CheckEXP(stmtSWITCH.getSelectionExp()) == false)
                return false;
              if (CheckLoopBodyPart(stmtSWITCH.getBodyStmt()) == false)
                return false;
              nextStmt = nextStmt.getNextStmt();
              break;
            case HIR.OP_EXP_STMT: // ==>OP_CALL
              //##71 BEGIN
              if (CheckEXP((Exp)nextStmt.getChild1()) == false)
                return false;
              nextStmt = nextStmt.getNextStmt();
              break;
              //##71 END
            case HIR.OP_CALL:
              Exp funcExp = ((ExpStmt)nextStmt).getExp();
              if (CheckEXP(funcExp) == false)
                return false;
              nextStmt = nextStmt.getNextStmt();
              break;
            case HIR.OP_BLOCK:
              if (CheckLoopBodyPart(((BlockStmt)nextStmt).getFirstStmt()) == false)
                return false;
              nextStmt = ((BlockStmt)nextStmt).getNextStmt();
              break;
            case HIR.OP_ASSIGN:
              if (CheckASSIGN((AssignStmt)nextStmt) == false)
                return false;
              nextStmt = nextStmt.getNextStmt();
              break;
            case HIR.OP_RETURN:
              return false;
            case HIR.OP_SEQ:
              return false;
            default:
              nextStmt = nextStmt.getNextStmt();
          }
        } // End of While
        return true;
    }
    /**
    *
    * CheckASSIGN:
    *
    *  It investigates whether a pointer variable, a structure object, etc. are in the
    *  HIR-AssignStmt.
    *
    * @param pstmt   HIR Assignstmt
    *
    *
    **/
    private boolean CheckASSIGN(AssignStmt pstmt) {
         HIR lLeft;
         lLeft = fUtil.SkipConv((Exp)pstmt.getLeftSide());
         if (fUtil.IsVarNode(lLeft) == true) {
            //
            // Set ASSIGN
            // def_check()...
            //
            fLoopTable.DefVarList.add(lLeft); // addDefVar
        }
        if (CheckEXP(pstmt.getLeftSide())== false)
            return  false;
        if (CheckEXP(pstmt.getRightSide())== false)
            return  false;
        return (true);

    }
    /**
    *
    * CheckEXP:
    *
    *  It investigates whether a pointer variable, a structure object, etc. are in the
    *  HIR-Exp.
    *
    * @param pExp   HIR-Exp
    *
    *
    **/
    private boolean CheckEXP(Exp pExp) {
        int op;
        String OpKeyWord;
        Type opType;
        if (pExp == null)
            return  true;
        fUtil.Trace(" CheckExp "+ pExp.toStringShort(),5); //##70
        op = pExp.getOperator(); //##71
        opType =pExp.getType();
        if ((opType.getTypeKind() == Type.KIND_POINTER)
            &&((op != HIR.OP_ADDR)&&(op != HIR.OP_DECAY)  //##71
               &&(op != HIR.OP_ADD)&&(op != HIR.OP_SUB))) //##71
        {
           Type PointedType = ((PointerType)opType).getPointedType();
           if(PointedType.getTypeKind() != Type.KIND_VECTOR) {
             fUtil.Trace(" Pointer expression in loop",1); //##70
              return  false;
           }
        }
        //##71 op = pExp.getOperator();
        switch (op) {
        case HIR.OP_SUBP:
            SubpNode sNode    =  (SubpNode)pExp;
            Subp    sSym      =  sNode.getSubp();
            String  fname     = sSym.getName();
            fname = fname.intern(); //##70
            if (fPreDefined.contains(fname))  {
              return true;
            }else {
              fUtil.Trace(" Subprogram call in loop",1); //##70
              return false;
            }
        case HIR.OP_VAR:
            break;

        case HIR.OP_PARAM:
            fUtil.Trace(" Parameter is used in loop",2); //##70
            return false;

        case HIR.OP_SUBS:
            if (CheckEXP(pExp.getExp1()) == false)
                return false;
            if (CheckEXP(pExp.getExp2()) == false)
                return false;
            break;
        case HIR.OP_CONST:
            break;
        //##71 BEGIN
        case HIR.OP_EXP_STMT:
          if (pExp.getChild1().getOperator() == HIR.OP_CALL) {
            Exp lChildExp = (Exp)pExp.getChild1().getChild1();
            fUtil.Trace(" lChild of CALL " + lChildExp.toStringShort(), 5); //##70
            if ((lChildExp.getChild1().getOperator() == HIR.OP_ADDR) &&
                (lChildExp.getChild1()instanceof SubpNode)) {
              String lSubpName = ((SubpNode)lChildExp.getChild1()).
                getSymNodeSym().getName().intern();
              if (fPreDefined.contains(lSubpName)) {
                fUtil.Trace(" call without side effect " + lSubpName, 4);
                return true;
              }
            }
          }else {
            if (CheckEXP((Exp)pExp.getChild1()) == false)
                return false;
            break;
          }
        //##71 END
        case HIR.OP_CALL:
            FunctionExp funcExp = (FunctionExp)pExp;
            Exp ExpSpec=funcExp.getFunctionSpec();
            //##70 BEGIN
            fUtil.Trace(" funcExpSpec " + ExpSpec.toStringShort(), 5); //##71
            if ((ExpSpec.getOperator() == HIR.OP_ADDR)&&
                (ExpSpec.getChild1() instanceof SubpNode)) {
              String lSubpName = ((SubpNode)ExpSpec.getChild1()).
                getSymNodeSym().getName().intern();
              if (fPreDefined.contains(lSubpName)) {
                fUtil.Trace(" predefined " + lSubpName,4);
                return true;
              }
            }
            //##70 END
            return CheckEXP(ExpSpec);
            /* Binary OP */
        case HIR.OP_ENCLOSE:
        //##71 case HIR.OP_ADD:
        //##71 case HIR.OP_SUB:
        case HIR.OP_DIV:
        case HIR.OP_CMP_EQ:
        case HIR.OP_CMP_NE:
        case HIR.OP_CMP_GT:
        case HIR.OP_CMP_GE:
        case HIR.OP_CMP_LT:
        case HIR.OP_CMP_LE:
        case HIR.OP_SHIFT_LL:
        case HIR.OP_SHIFT_R:
        case HIR.OP_SHIFT_RL:
        //##70 case HIR.OP_UNDECAY:
            if (CheckEXP(pExp.getExp1()) == false)
                return false;
            if (CheckEXP(pExp.getExp2()) == false)
                return false;
            break;
        //##70 BEGIN
        case HIR.OP_UNDECAY:
          break;
        //##70 END
        //##71 BEGIN
        case HIR.OP_ADD:
        case HIR.OP_SUB:
          Exp lChild1 = pExp.getExp1();
          if ((lChild1 instanceof VarNode)&&
               symRoot.safeArray.contains(((VarNode)lChild1).getSymNodeSym())) {
             if (CheckEXP(pExp.getExp2()) == false)
                 return false;
             else
               break;
           }else if (lChild1.getOperator() == HIR.OP_DECAY) {
             if (CheckEXP(pExp.getExp1()) == false)
               return false;
             else
               break;
           }
          if (CheckEXP(pExp.getExp1()) == false)
              return false;
          if (CheckEXP(pExp.getExp2()) == false)
              return false;
        break;
        //##71 END
        case HIR.OP_MULT:
            if (CheckEXP(pExp.getExp1()) == false)
                return false;
            if (CheckEXP(pExp.getExp2()) == false)
                return false;
            break;
        case HIR.OP_CONV:
            if (CheckEXP(pExp.getExp1()) == false)
                return false;
            break;
        case HIR.OP_NOT:
        case HIR.OP_NEG:
        case HIR.OP_ADDR:
        case HIR.OP_DECAY:
            if (CheckEXP(pExp.getExp1()) == false)
                return false;
            break;
        case HIR.OP_SIZEOF:
            if (CheckEXP(pExp.getExp1()) == false)
                return false;
            break;
        case HIR.OP_CONTENTS:
            if (CheckEXP(pExp.getExp1()) == false)
                return false;
            break;
        case HIR.OP_OFFSET:
            break;
        case HIR.OP_QUAL:
            fLoopTable.StructNodeHash.add(pExp);
            if (CheckEXP(pExp.getExp1()) == false)
                return false;
            break;
        case HIR.OP_ARROW:
            fUtil.Trace(" Pointer expression in loop",1); //##70
            return false;
        default :
            break;
        }
        return (true);
    }
}
