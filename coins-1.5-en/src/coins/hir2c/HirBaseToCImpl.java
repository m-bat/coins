/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.hir2c;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import coins.HirRoot;
import coins.SymRoot;
import coins.driver.Trace;
import coins.ir.IR;
import coins.ir.IrList;
import coins.ir.hir.AsmStmt; //##70
import coins.ir.hir.AssignStmt;
import coins.ir.hir.BlockStmt;
import coins.ir.hir.ConstNode;
import coins.ir.hir.Exp;
import coins.ir.hir.ExpListExp;
import coins.ir.hir.ExpStmt;
import coins.ir.hir.ForStmt;
import coins.ir.hir.FunctionExp;
import coins.ir.hir.HIR;
import coins.ir.hir.HirList; //##70
import coins.ir.hir.HirSeq;
import coins.ir.hir.IfStmt;
import coins.ir.hir.LabelDef;
import coins.ir.hir.LabeledStmt;
import coins.ir.hir.PointedExp;
import coins.ir.hir.Program;
import coins.ir.hir.RepeatStmt; //##70
import coins.ir.hir.ReturnStmt;
import coins.ir.hir.Stmt;
import coins.ir.hir.SubpDefinition;
import coins.ir.hir.SubpNode;
import coins.ir.hir.SwitchStmt;
//##70 import coins.ir.hir.UntilStmt;
import coins.ir.hir.VarNode;
import coins.ir.hir.WhileStmt;
import coins.sym.BoolConst;
import coins.sym.Const;
import coins.sym.Elem;
import coins.sym.EnumType;
import coins.sym.Label;
import coins.sym.PointerType;
import coins.sym.StringConst;
import coins.sym.StructType;
import coins.sym.Subp;
import coins.sym.SubpType;
import coins.sym.Sym;
import coins.sym.SymIterator;
import coins.sym.SymTable;
import coins.sym.Type;
import coins.sym.UnionType;
import coins.sym.Var;
import coins.sym.VectorType;
/////////////////////////////////////////////////////////////////////////////
//
//
//    HIR-Base  To  C  Language
//    HIR:(High level intermediate representation)
//
//
/////////////////////////////////////////////////////////////////////////////
public class HirBaseToCImpl implements HirBaseToC  {
    protected String myName = "HIR2C";

    private HirRoot fhirRoot;
    private  SymRoot fsymRoot;

    private boolean fFirstSearch;
    private KeyWords fKeyWord;
    private PrintDef fPrintDefine ;
    private AssociationList fInf;
    private Label fContinueLabel;
    private LinkedList fInitSymList;
    private boolean fInitSym;
    private SymTable fInitSymTable;

    private LabelRef fLabelRef;

    private PrintWriter fprintOut;
    private StringBuffer fOutString;

    private LinkedList fTraceList;    // Trace List
    private int fTraceLevel ;         // Trace Level
    private LinkedList fDebugTrace;   //##98
    /**
    *
    * HirBaseToCImpl:
    *
    *
    **/
    public HirBaseToCImpl(HirRoot phirRoot,SymRoot psymRoot,OutputStream pOut,Trace pTrace)     {
        fhirRoot = phirRoot;
        fsymRoot = psymRoot;
        fprintOut = new PrintWriter(pOut,true);
        fContinueLabel = null;
        fOutString = new StringBuffer();
        fKeyWord   = new KeyWords();
        fLabelRef = new LabelRef();
        fPrintDefine = new PrintDef(fprintOut);
        fInf = new AssociationList(fhirRoot,fprintOut);
        fTraceLevel =0;
        fTraceList = new LinkedList();
        fTraceLevel =pTrace.getTraceLevel(myName);
        fInitSymList= new LinkedList();
        fInitSym = false;
        fDebugTrace = new LinkedList();  //##98
    }
    /**
    *
    * Converter:
    *
    **/
    public void Converter() {

        fFirstSearch = true;
        fPrintDefine.setDefList();
        ConvertHIR();

        fFirstSearch = false;
        fPrintDefine.Converter();
        ConvertHIR();
        //##98 BEGIN
        if (fTraceLevel > 0) {
          System.out.println("\nHIR2C trace output\n");
          for (Iterator Ie =fDebugTrace.iterator(); Ie.hasNext();) {
            System.out.println((String)Ie.next());
          }
          fDebugTrace.clear();
        }
        //##98 END
    }
    /**
    *
    * ConvertHIR:
    *
    **/
    private void ConvertHIR() {
        IrList subpDefList ;
        Iterator subpDefIterator ;
        SubpDefinition subpDef;

        //
        // (1) Convert Global  Symbol
        //

        ConvertSymTable(fsymRoot.symTableRoot,0);

        //
        //  (2)Convert SubpDefinition
        //

        subpDefList = ((Program)fhirRoot.programRoot).getSubpDefinitionList();
        subpDefIterator = subpDefList.iterator();

        while (subpDefIterator.hasNext()) {
            subpDef = (SubpDefinition)(subpDefIterator.next());
            ConvertSubpDefnision(subpDef);
        }
    }
    /**
    *
    * ConvertSubpDefnision:
    *
    * ex1)
    * double sin(double x)
    * {
    *      double y;
    *        ...
    *        ...
    *      return y;
    * }
    *
    *
    * ex2) return value --> Function Pointer
    *
    * int     (*func())(int a)
    * {
    *        ...
    *        ...
    *    return sub;
    * }
    *
    *
    **/
    private void  ConvertSubpDefnision(SubpDefinition pSubpDef) {
        StringBuffer tmpString; //
        Subp   subpSym       = pSubpDef.getSubpSym();
        Stmt   hirBody       = (Stmt)pSubpDef.getHirBody();
        Type   ReturnType    = subpSym.getReturnValueType();
        IrList ParamList     = subpSym.getParamList();
        SymTable LocalSymTable = subpSym.getSymTable();
        SubpType subpType = (SubpType)subpSym.getSymType();
        int indentlevel      = 0;
        tmpString = new StringBuffer();
        tmpString.setLength(0);
        if (fFirstSearch == true)
            fLabelRef.initLabelRef(pSubpDef);

        //
        // (1)  Convert Subp()
        //

        String name;
        fInf.setAttribute((IR)pSubpDef);
        name=subpSym.getName();
        tmpString.append(subpSym.getName());
        tmpString.append("( ");
        if (subpType.getParamTypeList().size()==0) {
            //
            // Old type function definition
            //
            // double sin(x)
            // double x;
            // {
            //   ...
            //
            if (ParamList != null) {
                for (Iterator Ite=ParamList.iterator(); Ite.hasNext(); ) {
                    tmpString.append(((Sym)Ite.next()).getName() );
                    if (Ite.hasNext())
                        tmpString.append(", ");
                }
                tmpString.append(" )");
                fOutString.append(ConvertBaseType(ReturnType,tmpString.toString()));
                PrintLine(fOutString.toString(),indentlevel);
                if (ParamList!=null ) {
                    for (Iterator Ite=ParamList.iterator(); Ite.hasNext(); ) {
                        PrintLine(ConvertDECLARATION((Sym)Ite.next())+";", indentlevel );
                    }
                }
            }
        } else {
            if (ParamList != null) {
                boolean ParamExist;
                ParamExist = false;
                Iterator It = ParamList.iterator();
                while (It.hasNext()) {
                    ParamExist = true;
                    tmpString.append(ConvertDECLARATION((Sym)It.next()));
                    tmpString.append(",");
                }
                if (subpType.hasOptionalParam() == true && ParamExist == true) {
                    tmpString.append(" ... ");
                }
            }
            tmpString.setLength(tmpString.length()-1); // Cut ,
            tmpString.append(" )");

            fOutString.append(ConvertBaseType(ReturnType,tmpString.toString()));
            PrintLine(fOutString.toString(),indentlevel) ;
        }
//##70 BEGIN
// Following statements generates symbol declarations
// at illegal position
// (after function header and before block statement).
// Local symbol declaration should be generated in the
// block statement of the subprogram definition.
/*
        Stmt lHirBodyPart = hirBody;
        if (lHirBodyPart instanceof LabeledStmt)
          lHirBodyPart = ((LabeledStmt)lHirBodyPart).getStmt();
        if ((lHirBodyPart instanceof BlockStmt)&&
            (((BlockStmt)lHirBodyPart).getSymTable() == null)&&
            (LocalSymTable != null)) {
          ConvertSymTable(LocalSymTable, 1);
        }
 */
//##70 END
        //
        // (2)  Convert Body
        //

        ConvertHirBody(hirBody);
    }
    /**
    *
    * ConvertHirBody:
    *
    *
    **/
    private void  ConvertHirBody( Stmt pTree ) {
        ConvertStmt(pTree,0);
    }
    /**
    *
    * ConvertStmt:
    *
    *
    **/
    private void ConvertStmt(Stmt pstmt,int indentlevel )
    {
        int  op;
        int  index;
        Stmt nextStmt;

        nextStmt = pstmt;
        while (nextStmt != null) {
            op= nextStmt.getOperator();
            fInf.setAttribute((IR)nextStmt);

Trace("[HIR STMT]" + "[" +indentlevel+"]" + "[op="+ op+ "]" + nextStmt.toString(),1);

            switch (op) {
            case HIR.OP_IF:
                nextStmt = ConvertIF((IfStmt)nextStmt,indentlevel);
                break;
            case HIR.OP_FOR:
            case HIR.OP_INDEXED_LOOP:
                nextStmt = ConvertFOR((ForStmt)nextStmt,indentlevel);
                break;
            case HIR.OP_WHILE:
                nextStmt = ConvertWHILE((WhileStmt)nextStmt,indentlevel);
                break;
           //##70 case HIR.OP_UNTIL:
           //##70     nextStmt = ConvertUNTIL((UntilStmt)nextStmt,indentlevel);
            case HIR.OP_REPEAT: //##70
                nextStmt = ConvertREPEAT((RepeatStmt)nextStmt,indentlevel);
                break;
            case HIR.OP_LABELED_STMT:
Trace("[HIR STMT]" + "[ConvertLABEL STRAT]" ,1);
                nextStmt = ConvertLABEL((LabeledStmt)nextStmt,indentlevel);
Trace("[HIR STMT]" + "[ConvertLABEL END]" ,1);
                break;
            case HIR.OP_JUMP:
                nextStmt = ConvertGOTO(nextStmt,indentlevel);
                break;
            case HIR.OP_SWITCH:
                nextStmt = ConvertSWITCH((SwitchStmt)nextStmt,indentlevel);
                break;
            case HIR.OP_EXP_STMT: // ==>OP_CALL
            case HIR.OP_CALL:     //?
                nextStmt = ConvertEXPSTMT((ExpStmt)nextStmt,indentlevel);
                break;
            case HIR.OP_BLOCK:
                nextStmt = ConvertBLOCK((BlockStmt)nextStmt,indentlevel);
                break;
            case HIR.OP_ASSIGN:
                nextStmt = ConvertASSIGN((AssignStmt)nextStmt,indentlevel);
                break;
            case HIR.OP_RETURN:
                nextStmt = ConvertReturn((ReturnStmt)nextStmt,indentlevel);
                break;
            case HIR.OP_SEQ:
                nextStmt = ConvertSEQ((HirSeq)nextStmt,indentlevel);
                break;
            case HIR.OP_ASM:    //##70
                nextStmt = ConvertASMSTMT((AsmStmt)nextStmt, indentlevel); //##70
                break; //##70
            default:
                // DEBUG
                PrintLine("// OP Default",indentlevel);
                nextStmt = nextStmt.getNextStmt();
            }
        }
Trace("[HIR STMT]" + "[" +indentlevel+"]"+  "--END--",1);
    }
    /**
    *
    * ConvertIF:
    *
    * ex)
    * if (a == 1) { x=1; y=1; } else { x=2;y=2;}
    *
    **/
    private Stmt ConvertIF(IfStmt pstmt,int indentlevel) {
        Stmt ThenStmt ;
        Stmt ElseStmt ;
        Exp  ConditionExp;
        boolean BlockPrint;
        ThenStmt = pstmt.getThenPart();
        ElseStmt = pstmt.getElsePart();
        ConditionExp =pstmt.getIfCondition();

        //
        //  (1) Convert: if (Condition)
        //

        fOutString.append(fKeyWord.getOpKeyWord(HIR.OP_IF));
        fOutString.append("(");
        fOutString.append(ConvertEXP(ConditionExp));
        fOutString.append(")");
        PrintLine(fOutString.toString(),indentlevel) ;

        //
        //  (2) Convert:   ThenPart
        //

        BlockPrint =IsBlockPrint(ThenStmt);
        if (BlockPrint == true) {
            PrintLine("{",indentlevel) ;
            ConvertStmt(ThenStmt,indentlevel+1);
            PrintLine("}",indentlevel) ;
        } else {
            ConvertStmt(ThenStmt,indentlevel);
        }

        //
        //  (3) Convert:   ElsePart
        //

        if (IsElsePrint(ElseStmt) == true) {
            PrintLine("else",indentlevel) ;
            BlockPrint =IsBlockPrint(ElseStmt);
            if (BlockPrint == true) {
                PrintLine("{",indentlevel) ;
                ConvertStmt(ElseStmt,indentlevel+1);
                PrintLine("}",indentlevel) ;
            } else {
                ConvertStmt(ElseStmt,indentlevel);
            }
        }
        return(pstmt.getNextStmt());
    }
    /**
    *
    * ConvertFOR:
    *
    * ex)
    * for (i=0;i<10;i=i+1)
    * {
    *     a[i] = x;
    *     b[i] = x +i;
    * }
    *
    **/
    private Stmt ConvertFOR(ForStmt pstmt,int indentlevel) {
        Stmt InitStmt ;
        Exp ConditionExp ;
        Stmt StepStmt ;
        Stmt BodyStmt ;
        boolean BlockPrint;

        InitStmt = pstmt.getLoopInitPart();
        ConditionExp = pstmt.getLoopStartCondition();
        BodyStmt = pstmt.getLoopBodyPart();
        StepStmt = pstmt.getLoopStepPart();
        fContinueLabel = null;
        //LoopBackLabel = pstmt.getLoopBackLabel();
        fContinueLabel  = pstmt.getLoopStepLabel();
        //BreakLabel    = pstmt.getLoopEndLabel();

        ConvertStmt(pstmt.getConditionalInitPart(),indentlevel);

        //
        //  (1) Convert: for  ()
        //

        fOutString.append(fKeyWord.getOpKeyWord(HIR.OP_FOR));
        fOutString.append("( ");
        fOutString.append(ConvertPart(InitStmt));
        fOutString.setLength(fOutString.length()-1); // Cut ,
        fOutString.append(";");
        fOutString.append(ConvertEXP((Exp)ConditionExp));
        fOutString.append("; ");
        fOutString.append(ConvertPart(StepStmt));
        fOutString.setLength(fOutString.length()-1); // Cut ,
        fOutString.append(")");
        PrintLine(fOutString.toString(),indentlevel) ;

        //
        //  (2) Convert:    Body
        //

        BlockPrint =IsBlockPrint(BodyStmt);
        if (BlockPrint == true) {
            PrintLine("{",indentlevel) ;
            ConvertStmt(BodyStmt,indentlevel+1);
            PrintLine("}",indentlevel) ;
        } else {
            ConvertStmt(BodyStmt,indentlevel);
        }
        //##98 BEGIN
        LabeledStmt loopEndPart = ((coins.ir.hir.LoopStmt)pstmt).getLoopEndPart();
        if (loopEndPart != null) {
        	ConvertStmt(loopEndPart, indentlevel);
        }
        //##98 END 
        
        else { //##99
          fOutString.append(pstmt.getLoopEndLabel().getName());
          fOutString.append(":;");
          PrintLine(fOutString.toString(),indentlevel) ;
        } //##99 END
        return(pstmt.getNextStmt());
    }
    /**
    *
    * ConvertWHILE:
    *
    * ex)
    * while (i<10)
    * {
    *     a[i] = x;
    *     b[i] = x +i;
    * }
    *
    **/
    private Stmt ConvertWHILE(WhileStmt pstmt,int indentlevel) {
        Exp ConditionExp ;
        boolean BlockPrint;
        Stmt StepStmt ;
        Stmt BodyStmt ;

        ConditionExp = pstmt.getLoopStartCondition();
        BodyStmt = pstmt.getLoopBodyPart();
        fContinueLabel = null;

        //LoopBackLabel = pstmt.getLoopBackLabel();
        fContinueLabel  = pstmt.getLoopStepLabel();
        //BreakLabel    = pstmt.getLoopEndLabel();

        //
        //  (1) Convert: while  ()
        //

        fOutString.append(fKeyWord.getOpKeyWord(HIR.OP_WHILE));
        fOutString.append("(");
        fOutString.append(ConvertEXP((Exp)ConditionExp));
        fOutString.append(")");
        PrintLine(fOutString.toString(),indentlevel) ;

        //
        //  (2) Convert:    Body
        //

        BlockPrint =IsBlockPrint(BodyStmt);
        if (BlockPrint == true) {
            PrintLine("{",indentlevel) ;
            ConvertStmt(BodyStmt,indentlevel+1);
            PrintLine("}",indentlevel) ;
        } else {
            ConvertStmt(BodyStmt,indentlevel);
        }
        //##98 BEGIN
        LabeledStmt loopEndPart = ((coins.ir.hir.LoopStmt)pstmt).getLoopEndPart();
        if (loopEndPart != null) {
        	ConvertStmt(loopEndPart, indentlevel);
        }
        //##98 END
        else { //##99
          fOutString.append(pstmt.getLoopEndLabel().getName());
          fOutString.append(":;");
          PrintLine(fOutString.toString(),indentlevel) ;
        } //##99
        return(pstmt.getNextStmt());
    }
    /**
    *
    * ConvertUNTIL:
    *
    * ex)
    *  do
    * {
    *     a[i] = x;
    *     b[i] = x +i;
    * } while (a>b) ;
    *
    **/
    //##70 private Stmt ConvertUNTIL(UntilStmt pstmt,int indentlevel)
      private Stmt ConvertREPEAT(RepeatStmt pstmt,int indentlevel)
    {
        Exp ConditionExp ;
        Stmt StepStmt ;
        Stmt BodyStmt ;
        boolean BlockPrint;
        ConditionExp = pstmt.getLoopEndCondition();
        BodyStmt = pstmt.getLoopBodyPart();
        fContinueLabel = null;

        fContinueLabel  = pstmt.getLoopStepLabel();

        //
        //  (1) Convert:    Body
        //

        PrintLine("do",indentlevel) ;
        BlockPrint =IsBlockPrint(BodyStmt);
        if (BlockPrint == true) {
            PrintLine("{",indentlevel) ;
            ConvertStmt(BodyStmt,indentlevel+1);
            PrintLine("}",indentlevel) ;
        } else {
            ConvertStmt(BodyStmt,indentlevel);
        }

        //
        //  (2) Convert: while  ()
        //

        fOutString.append(fKeyWord.getOpKeyWord(HIR.OP_WHILE));
        fOutString.append("(");
        fOutString.append(ConvertEXP((Exp)ConditionExp));
        fOutString.append(") ;");
        PrintLine(fOutString.toString(),indentlevel) ;
        //##98 BEGIN
        LabeledStmt loopEndPart = ((coins.ir.hir.LoopStmt)pstmt).getLoopEndPart();
        if (loopEndPart != null) {
        	ConvertStmt(loopEndPart, indentlevel);
        }
        //##98 END
        else { //##99
          fOutString.append(pstmt.getLoopEndLabel().getName());
          fOutString.append(":;");
          PrintLine(fOutString.toString(),indentlevel) ;
        } //##99
        return(pstmt.getNextStmt());
    }
    /*
    *
    * ConvertPart:
    *
    * ex)
    * [for] InitStmt
    *   i=0,j=0,
    *
    **/
    private String ConvertPart(Stmt pstmt) {
        StringBuffer tmpString;
        Stmt nextStmt;
        int  op;
        tmpString = new StringBuffer();
        tmpString.setLength(0);

        nextStmt = pstmt;
        while (nextStmt != null) {
            fInf.setAttribute((IR)nextStmt);
            op= nextStmt.getOperator();

Trace("[HIR STMT]" + "[op="+ op+ "]" + nextStmt.toString(),1);

            switch (op) {
            case HIR.OP_LABELED_STMT:
                nextStmt = ((LabeledStmt)nextStmt).getStmt();
                break;
            case HIR.OP_BLOCK:
                tmpString.append(ConvertPart(((BlockStmt)nextStmt).getFirstStmt()));
                nextStmt = pstmt.getNextStmt();
                break;
            case HIR.OP_ASSIGN:
                tmpString.append(ConvertEXP(((AssignStmt)nextStmt).getLeftSide()));
                tmpString.append(fKeyWord.getOpKeyWord(HIR.OP_ASSIGN));
                tmpString.append(ConvertEXP(((AssignStmt)nextStmt).getRightSide()));
                nextStmt = nextStmt.getNextStmt();
                tmpString.append(",");
                break;
            case HIR.OP_EXP_STMT: // ==>OP_CALL
            case HIR.OP_CALL:     // ==>OP_CALL
                tmpString.append(ConvertEXP(((ExpStmt)nextStmt).getExp()));
                tmpString.append(",");
                nextStmt = nextStmt.getNextStmt();
                break;
            default:
                nextStmt = nextStmt.getNextStmt();
            }
        }
        return (tmpString.toString());
    }
    /*
    *
    * ConvertLoop:
    *
    *
    **/
    private Stmt ConvertLoop(ForStmt pstmt,int indentlevel) {
        Stmt InitStmt ;
        Exp ConditionExp ;
        Stmt StepStmt ;
        Stmt BodyStmt ;
        boolean BlockPrint;
        Label  LoopBackLabel ;
        Label  LoopEndLabel ;
        Label  LoopStepLabel  ;

        InitStmt = pstmt.getLoopInitPart();
        ConditionExp = pstmt.getLoopStartCondition();
        BodyStmt = pstmt.getLoopBodyPart();
        StepStmt = pstmt.getLoopStepPart();
        LoopBackLabel = pstmt.getLoopBackLabel();
        LoopStepLabel = pstmt.getLoopStepLabel();
        LoopEndLabel = pstmt.getLoopEndLabel();

        //
        //   InitStmt
        //

        ConvertStmt(InitStmt,indentlevel);

        //
        //    LoopBackLabel
        //

        if (LoopBackLabel != null ) {
            fOutString.append(LoopBackLabel.getName());
            fOutString.append(":");
            PrintLine(fOutString.toString(),indentlevel) ;
            fOutString.append(fKeyWord.getOpKeyWord(HIR.OP_IF));
        }

        //
        //   If
        //

        if (ConditionExp != null) {
            fOutString.append(fKeyWord.getOpKeyWord(HIR.OP_IF));
            fOutString.append("(");
            fOutString.append(ConvertEXP((Exp)ConditionExp));
            fOutString.append(")");
            fOutString.append(fKeyWord.getOpKeyWord(HIR.OP_JUMP));
            fOutString.append(LoopEndLabel.getName());
            fOutString.append(";");
            PrintLine(fOutString.toString(),indentlevel) ;
        }

        //
        //  Body
        //

        BlockPrint = IsBlockPrint(BodyStmt);
        if (BlockPrint == true) {
            PrintLine("{",indentlevel) ;
                    ConvertStmt(BodyStmt,indentlevel+1);
            PrintLine("}",indentlevel) ;
        } else {
                    ConvertStmt(BodyStmt,indentlevel);
        }
        PrintLine(fOutString.toString(),indentlevel) ;

        //
        //  Step
        //

        fOutString.append(LoopStepLabel.getName());
        fOutString.append(":;");
        PrintLine(fOutString.toString(),indentlevel) ;
        ConvertStmt(StepStmt,indentlevel);

        if (BlockPrint == true)
            PrintLine("}",indentlevel) ;

        fOutString.append(fKeyWord.getOpKeyWord(HIR.OP_JUMP));
        fOutString.append(LoopBackLabel.getName());
        fOutString.append(";");
        PrintLine(fOutString.toString(),indentlevel) ;

        //
        //  End
        //

        fOutString.append(LoopEndLabel.getName());
        fOutString.append(":;");
        PrintLine(fOutString.toString(),indentlevel) ;
        return(pstmt.getNextStmt());
    }
    /**
    *
    * ConvertSWITCH:
    *
    * ex)
    * swicth (a)
    * {
    *
    * case 1:
    * ....
    *
    * }
    *
    **/
    private Stmt ConvertSWITCH(SwitchStmt pstmt,int indentlevel) {

        Exp SelectionExp =pstmt.getSelectionExp();
        Stmt Body= pstmt.getBodyStmt();
        Label EndLabel = pstmt.getEndLabel();

        fOutString.append(fKeyWord.getOpKeyWord(HIR.OP_SWITCH));
        fOutString.append("(");
        fOutString.append(ConvertEXP(SelectionExp));
        fOutString.append(")");
        PrintLine(fOutString.toString(),indentlevel) ;
        ConvertStmt(Body,indentlevel);
        PrintLine(EndLabel.getName() + ":;",indentlevel) ;

        return(pstmt.getNextStmt());
    }
    /**
    *
    * ConvertSEQ:
    *
    *
    **/
    private Stmt ConvertSEQ(HirSeq pstmt,int indentlevel) {
        return(pstmt.getNextStmt());
    }
    /**
    *
    * ConvertGOTO:
    *
    * ex)
    * goto JUMP;
    *
    **/
    private Stmt ConvertGOTO(Stmt pstmt,int indentlevel) {
        Label label ;
        label = pstmt.getLabel();

        if (fFirstSearch == true)
            fLabelRef.putLabelRef(label);
        fOutString.append(fKeyWord.getOpKeyWord(HIR.OP_JUMP));
        fOutString.append(label.getName());
        fOutString.append(";");
        PrintLine(fOutString.toString(),indentlevel) ;

        return (pstmt.getNextStmt());
    }
    /**
    *
    * ConvertASSIGN:
    *
    * ex)
    * a[i] = b[j] + c[k+1];
    *
    **/
    private Stmt ConvertASSIGN(AssignStmt pstmt,int indentlevel) {
        int op;
        boolean     bConstString ;
        StringConst sConstString ;

        bConstString = false;
        sConstString = null;
        op= pstmt.getRightSide().getOperator();

        if (op == HIR.OP_CONST) {
            ConstNode sConstNode     =  (ConstNode)pstmt.getRightSide();
            Const     sConstSym      =  sConstNode.getConstSym();
            Type   sConstType     =  sConstSym.getSymType();
            if (sConstType.isFloating() == false  && sConstType.isInteger() == false) {
                bConstString = true;
                sConstString = (StringConst)sConstSym;
            }
        }

        if (bConstString == true) {
            int size;
            //
            //  HIR   :        a= "Coins";
            //  C-CODE:        memcpy(a,"Coins",n);
            //
            String ConstStringValue=ConvertSTRING(sConstString.getStringBody());
            size =sConstString.getLength();
            fOutString.append(fKeyWord.getHir2cKeyWord(KeyWords.HIR2C_STRINGCOPY)+ "("
                        +  ConvertEXP(pstmt.getLeftSide()) +"," + ConstStringValue
                        +  "," + size
                        +")");
        }else {
            //
            // a[i] = b[j] + c[k+1];
            //
            fOutString.append(ConvertEXP(pstmt.getLeftSide()));
            fOutString.append(fKeyWord.getOpKeyWord(HIR.OP_ASSIGN));
            fOutString.append(ConvertEXP(pstmt.getRightSide()));
        }
        fOutString.append(";");
        PrintLine(fOutString.toString(),indentlevel) ;

        return (pstmt.getNextStmt());
    }
    /**
    *
    * getArrayVarType:
    *
    *
    *
    **/
    private VectorType  getArrayVarType(Exp pExp) {
        int op;
        Exp ParentExp;
        VarNode vNode;
        Var     vSym;
        ParentExp = pExp;

        op= ParentExp.getOperator();
        while ( op != HIR.OP_SUBS) {
            ParentExp=(Exp)ParentExp.getParent();
            op= ParentExp.getOperator();
        }
        ParentExp=(Exp)ParentExp.getExp1();
        return (VectorType) ParentExp.getType();
    }
    /**
    *
    * ConvertEXP:
    *
    * ex1)
    * (a + b) * c
    * ex2)
    * a < b
    *
    **/
    private String  ConvertEXP(Exp pExp) {
        StringBuffer tmpString;
        int op;
        String OpKeyWord;

        if (pExp == null) return ("");

        tmpString = new StringBuffer();
        tmpString.setLength(0);
        op= pExp.getOperator();
        OpKeyWord = fKeyWord.getOpKeyWord(op);
        fInf.setAttribute((IR)pExp);

Trace("[HIR EXP ]" + "[op="+ op+ "]" + pExp.toString(),5);

        switch (op) {
        case HIR.OP_SUBP :
            SubpNode sNode    =  (SubpNode)pExp;
            Subp    sSym      =  sNode.getSubp();
            if (fInitSym == true) {
                if (fInitSymTable.searchTableHaving((Sym)sSym) == fInitSymTable) {
                    fInitSymList.add(sSym);
                }
             }
            tmpString.append(sSym.getName());
            break;
        case HIR.OP_VAR :
            VarNode sVarNode     =  (VarNode)pExp;
            Var     sVarSym      =  sVarNode.getVar();
            if (fInitSym == true) {
                if (fInitSymTable.searchTableHaving((Sym)sVarSym) == fInitSymTable) {
                    fInitSymList.add(sVarSym);
                }
            }
            tmpString.append(sVarSym.getName());
            break;
        case HIR.OP_PARAM :
            VarNode sVarNode1     =  (VarNode)pExp;
            Var     sVarSym1      =  sVarNode1.getVar();
            tmpString.append(sVarSym1.getName());
            break;
        case HIR.OP_SUBS:
            tmpString.append(ConvertEXP(pExp.getArrayExp()));
            tmpString.append(ConvertSubscriptEXP(pExp.getSubscriptExp()));
            break;
        //
        //case   HIR.OP_INDEX:
        //    break;
        //
        case    HIR.OP_CONST:
            ConstNode sConstNode     =  (ConstNode)pExp;
            Const     sConstSym      =  sConstNode.getConstSym();
            //Const     sConstSym      =  (Const)sConstNode.getSym();
            tmpString.append(ConvertCONST(sConstSym));
            break;
        case HIR.OP_CALL:
            FunctionExp fExp = (FunctionExp)pExp;
            IrList ParamList     = fExp.getParamList();
            ParamList     = fExp.getParamList();
            Exp ExpSpec=fExp.getFunctionSpec();

            tmpString.append(ConvertEXP(ExpSpec));
            tmpString.append("( ");
            for (Iterator It = ParamList.iterator(); It.hasNext(); ) {
                tmpString.append(ConvertEXP((Exp)It.next()));
                tmpString.append(",");
            }
            tmpString.setLength(tmpString.length()-1); // Cut ,
            tmpString.append(")");
            break;
            /* Binary OP */
        case    HIR.OP_ENCLOSE :
        case    HIR.OP_DIV:
        case    HIR.OP_MOD:
        case    HIR.OP_CMP_EQ :
        case    HIR.OP_CMP_NE :
        case    HIR.OP_CMP_GT:
        case    HIR.OP_CMP_GE  :
        case    HIR.OP_CMP_LT :
        case    HIR.OP_CMP_LE :
        case    HIR.OP_SHIFT_LL :
        case    HIR.OP_SHIFT_R :
        case    HIR.OP_SHIFT_RL :
        case    HIR.OP_UNDECAY:
        case    HIR.OP_AND:
        case    HIR.OP_OR:
        case    HIR.OP_XOR:
            tmpString.append(OpKeyWord);
            tmpString.append("("+ConvertEXP(pExp.getExp1()));
            tmpString.append(","+ConvertEXP(pExp.getExp2())+")");
            break;
        case    HIR.OP_ADD :
        case    HIR.OP_SUB :
            //
            // ex1)
            //  int i,j;
            //   i + j
            //
            // ex2)
            //  int *i;
            //   (int*)((chra*) i + size*n)
            //
            if (pExp.getType().getTypeKind() == Type.KIND_POINTER) {
                tmpString.append("(");
                tmpString.append(ConvertBaseType(pExp.getType(),""));
                tmpString.append(")");
            }
            tmpString.append("(");

            tmpString.append(OpKeyWord);
            tmpString.append("(");

            if (pExp.getExp1().getType().getTypeKind() == Type.KIND_POINTER) {
                tmpString.append(fKeyWord.getHir2cKeyWord(KeyWords.HIR2C_POINTER));
                tmpString.append("("+ConvertEXP(pExp.getExp1()) +")");
            } else {
                     tmpString.append(ConvertEXP(pExp.getExp1()));
            }

            tmpString.append(",");

            if (pExp.getExp2().getType().getTypeKind() == Type.KIND_POINTER) {
                tmpString.append(fKeyWord.getHir2cKeyWord(KeyWords.HIR2C_POINTER));
                tmpString.append("("+ConvertEXP(pExp.getExp2()) +")");
            } else {
                     tmpString.append(ConvertEXP(pExp.getExp2()));
            }
            tmpString.append(")");
            tmpString.append(")");
            break;
        case    HIR.OP_MULT:
            tmpString.append(OpKeyWord);
            tmpString.append("("+ConvertEXP(pExp.getExp1()));
            tmpString.append(","+ConvertEXP(pExp.getExp2())+")");
            break;
        case    HIR.OP_CONV:
            if (pExp.getType().getTypeKind() != Type.KIND_STRUCT) {
                if (pExp.getType().getTypeKind() == Type.KIND_ADDRESS)
                    tmpString.append(ConvertBaseType(pExp.getType(),""));
                else
                   tmpString.append("("+ConvertBaseType(pExp.getType(),"") +")");
            }
            tmpString.append("("+ ConvertEXP(pExp.getExp1())+ ")");
            break;
        case    HIR.OP_NOT:
        case    HIR.OP_NEG:
        case    HIR.OP_ADDR:
        case    HIR.OP_DECAY:
            tmpString.append("(");
            tmpString.append(OpKeyWord);
            tmpString.append("("+ ConvertEXP(pExp.getExp1())+ ")");
            tmpString.append(")");
            break;
        case    HIR.OP_SIZEOF:
            tmpString.append(OpKeyWord);
            tmpString.append("("+ ConvertEXP(pExp.getExp1())+ ")");
            break;
        case    HIR.OP_CONTENTS:
            tmpString.append("(");
            tmpString.append(OpKeyWord);
            tmpString.append("("+ ConvertEXP(pExp.getExp1())+ ")");
            tmpString.append(")");
            break;
        case    HIR.OP_QUAL:
            tmpString.append( ConvertEXP(pExp.getQualifierExp()));
            tmpString.append( ".");
            tmpString.append( pExp.getQualifiedElem().getName());
                        break;
        case    HIR.OP_ARROW:
            PointedExp ptrExp = (PointedExp)pExp;
            tmpString.append( "(");
            tmpString.append(ConvertEXP(ptrExp.getPointerExp()));
            tmpString.append( ")");
            tmpString.append( "->");
            Elem sElem = ptrExp.getPointedElem();
            tmpString.append( sElem.getName());
            break;
        case    HIR.OP_OFFSET:
            break;
        case    HIR.OP_EXPLIST:
            ExpListExp listexp = (ExpListExp)pExp;
            tmpString.append("{ ");
            for (int i=0; i<listexp.length(); i++ ) {
                if (i!=0)
                    tmpString.append(", ");
                if (listexp.getExp(i).getOperator()!=HIR.OP_EXPREPEAT )
                    tmpString.append( ConvertEXP(listexp.getExp(i)) );
            }
            tmpString.append(" }");
            break;
        default :
            tmpString.append(OpKeyWord);
            break;
        }
        return (tmpString.toString());
    }
    /**
    *
    * ConvertSubscriptEXP:
    *
    *   Array[pExp]
    *
    *   [hir__LowerBoud(pExp,n)]
    *
    **/
    private String  ConvertSubscriptEXP(Exp pExp) {
            long LowerBound ;
            Exp LowerBoundExp ;
            StringBuffer tmpString;
            VectorType vType ;

            tmpString = new StringBuffer();
            tmpString.setLength(0);

            tmpString.append("[");
            vType =getArrayVarType(pExp);

            if (vType == null)
                LowerBoundExp = null;
            else
                LowerBoundExp =vType.getLowerBoundExp() ;
            if (LowerBoundExp != null) {
                LowerBound =vType.getLowerBound();
                if (LowerBound != 0) {
                    tmpString.append(fKeyWord.getHir2cKeyWord(KeyWords.HIR2C_LOWERBOUND)+"("
                        +ConvertEXP(pExp) +"," + ConvertEXP(LowerBoundExp)+")");
                } else {
                    tmpString.append(ConvertEXP(pExp));
                }
            } else {
                tmpString.append(ConvertEXP(pExp));
            }
            tmpString.append("]");

            return (tmpString.toString());
    }
    /**
    *
    * IsBinaryOP:
    *
    *
    **/
    private boolean  IsBinaryOP(Exp pExp) {
        if (pExp == null)
            return false;
        switch (pExp.getOperator()) {
        case    HIR.OP_ADD :
        case    HIR.OP_SUB :
        case    HIR.OP_MULT:
        case    HIR.OP_DIV:
        case    HIR.OP_CMP_EQ:
        case    HIR.OP_CMP_NE:
        case    HIR.OP_CMP_GT:
        case    HIR.OP_CMP_GE:
        case    HIR.OP_CMP_LT:
        case    HIR.OP_CMP_LE:
            return true;
        default:
            return false;
        }
    }
    /**
    *
    * ConvertReturn:
    *
    * ex)
    * return  sin(x+y);
    *
    **/
    private Stmt ConvertReturn(ReturnStmt pstmt,int indentlevel) {

        fOutString.append(fKeyWord.getOpKeyWord(HIR.OP_RETURN));
        fOutString.append(ConvertEXP(pstmt.getReturnValue()));
        fOutString.append(";");
        PrintLine(fOutString.toString(),indentlevel) ;

        return (pstmt.getNextStmt());
    }
    /*
    *
    * ConvertBLOCK:
    *
    *
    **/
    private Stmt ConvertBLOCK(BlockStmt pstmt,int indentlevel) {
        Label lContinue;

        lContinue = fContinueLabel;
        fContinueLabel = null;

        PrintLine("{",indentlevel);

        ConvertSymTable(pstmt.getSymTable(),indentlevel+1);
        ConvertStmt(pstmt.getFirstStmt(),indentlevel+1);

    //if (lContinue != null) {
    //    fOutString.append(lContinue.getName());
    //    fOutString.append(":;");
    //    PrintLine(fOutString.toString(),indentlevel+1) ;
    //   fContinueLabel = null;
    //}
        PrintLine("}",indentlevel);
        return (pstmt.getNextStmt());
    }
    /**
    *
    * ConvertEXPSTMT:
    *
    * ex)
    *   sub(a[i],b);
    *
    **/
    private Stmt ConvertEXPSTMT(ExpStmt pstmt,int indentlevel) {
        fOutString.append(ConvertEXP(pstmt.getExp()));
        PrintLine(fOutString.toString()+ ";",indentlevel);
        return (pstmt.getNextStmt());
    }
    /**
    *
    * ConvertLABEL:
    *
    * ex)
    *   JUMP:
    *
    *
    **/
    private Stmt ConvertLABEL(LabeledStmt pstmt,int indentlevel) {
        Stmt nextStmt;
        IrList LabeledList;
        LabelDef  labelDef;
        Label     label;
        ListIterator  Ie;
        int LabelKind ;
        int i;

        LabeledList = pstmt.getLabelDefList();
        for (Ie =LabeledList.iterator();Ie.hasNext();) {
            labelDef = (LabelDef)Ie.next();
            label    = labelDef.getLabel();

            fInf.setAttribute((IR)labelDef);
            if (IsLabelPrint(label) == true) {
                PrintLine(label.getName()  + ":;",0);
            } else {
                LabelKind =label.getLabelKind() ;
                if (LabelKind ==  Label.SWITCH_CASE_LABEL) {
                    SwitchStmt Stmt = (SwitchStmt)label.getOriginHir();
                    int caseCount = Stmt.getCaseCount();
                    for (i=0;i<caseCount;i++) {
                        if (Stmt.getCaseLabel(i) == label) {
                            Const caseConst=Stmt.getCaseConst(i);
                            PrintLine("case " +
                                    ConvertCONST(caseConst)+":",indentlevel);
                            break;
                        }
                    }
                } else  if (LabelKind == Label.SWITCH_DEFAULT_LABEL) {
                    PrintLine("default: ",indentlevel);
                } else {
                    //PrintLine(label.getName()  + ":;" + LabelKind ,0);
                }
            }
        }

        ConvertStmt(((LabeledStmt)pstmt).getStmt(),indentlevel);
        nextStmt = pstmt.getNextStmt();
        return (nextStmt);

    }
    /**
    *
    * ConvertCONST:
    *
    * ex)
    *  1.0
    *
    **/
    private String ConvertCONST(Const sConst) {
        Type   sConstType     =  sConst.getSymType();
        String ConstStringKey   = "";
        String ConstStringValue  = "";

        if (sConstType.getTypeKind() == Type.KIND_OFFSET) {
            ConstStringValue =  Long.toString(sConst.longValue());
            //ConstStringValue = "1";
        } else if (sConstType.getTypeKind() == Type.KIND_BOOL_CONST) {
            ConstStringValue =  Long.toString(((BoolConst)sConst).longValue());
        } else {
            if (sConstType.isFloating() == true) {
                ConstStringValue = "("+ConvertBaseType(sConstType,"")+")"+
                                                Double.toString(sConst.doubleValue());
            }else if (sConstType.isInteger() == true) {
                ConstStringValue = "("+ConvertBaseType(sConstType,"")+")"+
                                                Long.toString(sConst.longValue());
            } else  {
                StringConst sconstString = (StringConst)sConst;
                if (sconstString != null) {
                    ConstStringValue=ConvertSTRING(sconstString.getStringBody());
                    return (ConstStringValue);
                }

            }
        }
        return ConstStringValue;
    }
    /**
    *
    * ConvertSTRING:
    *
    *  ex)
    *  abc
    *
    **/
    private String ConvertSTRING( String pCstring ) {
        StringBuffer lString = new StringBuffer();
        int          i;
        char         lChar;

        if (pCstring == null)
            return "\"\"";
        lString.append("\"");
        i = 0; // Cut "" --> 0 to len
        while (i < pCstring.length()) {
            lChar = pCstring.charAt(i);
            switch (lChar) {
            case '\007': lString.append("\\a");   break; // alarm
            case '\013': lString.append("\\013");   break;
            case '\b'  : lString.append("\\b");   break; // back space
            case '\f'  : lString.append("\\f");   break; // form feed
            case '\n'  : lString.append("\\n");   break; // new line
            case '\r'  : lString.append("\\r");   break; // return to col. 1
            case '\t'  : lString.append("\\t");   break; // horisontal tab
            case '\\'  : lString.append("\\\\");  break; // backslash
            case '\''  : lString.append("\\\'");  break; // single quote
            case '\"'  : lString.append("\\\"");  break; // double quote
            default:
                int value;
                value =  lChar;
                if (value >= 0x20 && value <= 0x7E) {
                    lString.append(lChar);
                } else {
                    lString.append("\\");
                    lString.append(Integer.toOctalString(value));
                }
            }
            i++;
        }
        lString.append("\"");
        return lString.toString();
    }
    /**
    *
    * ChangeConstSTRING:
    *
    *
    **/
    private String ChangeConstSTRING( String pCstring ) {
        StringBuffer lString = new StringBuffer();
        int          i;
        char         lChar;
        i = 0;
        while (i < pCstring.length()) {
            lChar = pCstring.charAt(i);
            switch (lChar) {
            case '-' : lString.append("_SUB_");     break;
            case '.' : lString.append("_POINT_");   break;
            default: lString.append(lChar);
            }
            i++;
        }
        return lString.toString();
    }
    /**
    *
    * ChangeTagName:
    *
    *
    **/
    private String ChangeTagName( String pCstring ) {
        StringBuffer lString = new StringBuffer();
        int          i, lCharCode;
        char         lChar;
        i = 0;
        while (i < pCstring.length()) {
            lChar = pCstring.charAt(i);
            switch (lChar) {
            case '#': lString.append("___");   break;
            default: lString.append(lChar);
            }
            i++;
        }
        return lString.toString();
    }
    /**
    *
    * IsLabelPrint:
    *
    *
    **/
    private boolean IsLabelPrint(Label label) {

Trace("[HIR DEBUG ]" + label.getName() +"[LabelKind]" + label.getLabelKind(),1); //##98
Trace("[HIR DEBUG ]" + "[LabelRefCount]" + label.getHirRefCount(),1); //##98
        if (label == fContinueLabel)
            fContinueLabel = null;
        //if( label.getHirRefCount() != 0 )
        if (fLabelRef.IsLabelRef(label) == true)
            return true;
        else if (label.getLabelKind() == Label.UNCLASSIFIED_LABEL)
            return true;
        else
            return false;
    }
    /**
    *
    * IsBlockPrint:
    *
    *
    **/
    private boolean IsBlockPrint(Stmt Part) {
        Stmt nextStmt;
        IrList LabeledList;
        LabelDef  labelDef;
        Label     label;
        ListIterator  Ie;
        if (Part == null)
            return true;
        nextStmt = ((LabeledStmt)Part).getStmt();
        if (nextStmt == null ) {
                nextStmt = Part.getNextStmt();
        } else {
            LabeledList = nextStmt.getLabelDefList();
            for (Ie =LabeledList.iterator();Ie.hasNext();) {
                labelDef = (LabelDef)Ie.next();
                label    = labelDef.getLabel();
                if (IsLabelPrint(label) == true) {
                    return true;
                }
            }
        }
        if (nextStmt == null)
            return true;
        if (nextStmt.getOperator() == HIR.OP_BLOCK)
            return false;
        else
            return true;
    }
    /**
    *
    * IsThenPrint:
    *
    *
    **/
    private boolean IsThenPrint(Stmt ThenPart) {
        Stmt nextStmt;
        if (ThenPart == null)
            return false;

        nextStmt = ((LabeledStmt)ThenPart).getStmt();
        if (nextStmt == null )
            nextStmt = ThenPart.getNextStmt();

        if (nextStmt == null)
            return false;
        else
            return true;
    }
    /**
    *
    * IsElsePrint:
    *
    *
    **/
    private boolean IsElsePrint(Stmt ElsePart) {
        Stmt nextStmt;
        if (ElsePart == null)
            return false;
        nextStmt = ((LabeledStmt)ElsePart).getStmt();
        if (nextStmt == null )
            nextStmt = ElsePart.getNextStmt();
        if (nextStmt == null)
            return false;
        else
            return true;
    }
    /**
    *
    * ConvertTAG:
    *
    *
    **/
    private void ConvertTAG(HashSet PutTag ,Sym pSym,int indentlevel)
    {
        ListIterator Ie;
        LinkedList NestTagList;
        if (PutTag.contains(pSym) == true)
            return;

        NestTagList = new LinkedList();
        getNestTagList(NestTagList,pSym);

        for (Ie =NestTagList.listIterator();Ie.hasNext();) {
            Sym lSym = (Sym)Ie.next();
            if (lSym == null)
                continue;
            if (lSym == pSym)
                continue; // Nest Error!!
            ConvertTAG(PutTag,lSym,indentlevel);
        }

Trace("[HIR SYM (TAG)]" + "[kind="+ pSym.getSymKind() + "]" + pSym.toString(),1);
        PutTag.add(pSym);
        ConvertSymELEM(pSym,indentlevel);
    }
    /**
    *
    * getNestTagList:
    *
    *
    **/
    private void getNestTagList(LinkedList NestTagList ,Sym symVar)
    {
        StringBuffer   tmpString;
        Elem       lElem;
        IrList       lElemList ;
        Type symType;
        Type nextType;
        Sym Tag;

        int TypeKind;
        tmpString = new StringBuffer();
        tmpString.setLength(0);
        symType = symVar.getSymType();
        TypeKind = symType.getTypeKind();
        Tag = null;
        switch (TypeKind) {
        case  Type.KIND_STRUCT:
        case  Type.KIND_UNION:
            lElemList = symVar.getSymType().getElemList();
            for (Iterator lElemIterator = lElemList.iterator();lElemIterator.hasNext();) {
                lElem = (Elem)lElemIterator.next();
                if (lElem == null)
                    break;
                Type ESymType =((Sym)lElem).getSymType();
                int EType = ESymType.getTypeKind();
                while (EType == Type.KIND_VECTOR) {
                    //
                    // struct tag v[n][m] ;
                    //
                    ESymType = ((VectorType)ESymType).getElemType();
                    EType = ESymType.getTypeKind();
                }
                switch (EType) {
                case  Type.KIND_STRUCT:
                    Tag = ((StructType)ESymType).getTag();
                    break;
                case  Type.KIND_UNION:
                    Tag = ((UnionType)ESymType).getTag();
                    break;
                case  Type.KIND_ENUM:
                    Tag = ((EnumType)ESymType).getTag();
                    break;
                default:
                    Tag =null;
                }
                if (Tag != null) {
                    NestTagList.add(Tag);
                }
            }
            break;
        }
    }
    /**
    *
    * ConvertSymTable:
    *
    *
    **/
    private void ConvertSymTable(SymTable sTable,int indentlevel) {
        SymIterator Ie;
       Iterator Ite;
        Sym lSym;
        Type lType;
        int kind;
        HashSet PutTag;

        if (sTable == null)
            return ;

        //
        // (1) TAG :Struct & Union
        //
        //  EX)
        // input: Symbol Table (SymKind=KIND_TAG)
        //
        //  Current TAG: struct A{
        //                  struct B v;
        //
        //               };
        //  Next    TAG: struct B {
        //                  int v1;
        //               };
        //
        //  output: C-Code
        //
        //  struct B {
        //      int v1;
        //   };
        //
        //  struct A{
        //       struct B v;
        //   };
        //

        PutTag = new HashSet();
        for ( Ie= sTable.getSymIterator(); Ie.hasNext(); ) {
            lSym = Ie.next();
            if (lSym.getSymKind()== Sym.KIND_TAG) {
                ConvertTAG(PutTag,lSym,indentlevel);
            }
        }

        fInitSym = true;
        fInitSymList.clear();
        fInitSymTable= sTable;
        for ( Ie= sTable.getSymIterator(); Ie.hasNext(); ) {
            lSym = (Sym)Ie.next();
            if (lSym == null)
                break;
            kind = lSym.getSymKind();
            if (kind == Sym.KIND_VAR) {
                if (((Var)lSym).getStorageClass()==Var.VAR_STATIC) {
                    String InitString =ConvertEXP( ((Var)lSym).getInitialValue());
                }
            }
        }
        fInitSym = false;

        //
        // (2) Symbol
        //
        // fInitSymList.clear();
        for ( Ite= fInitSymList.iterator(); Ite.hasNext(); ) {
            lSym = (Sym)Ite.next();
            if (lSym == null)
                break;
            kind = lSym.getSymKind();
            if (kind == Sym.KIND_TAG)
                continue;
           PrintSym(lSym,indentlevel);
        }

        //
        // (3) Symbol
        //

        for ( Ie= sTable.getSymIterator(); Ie.hasNext(); ) {
            lSym = (Sym)Ie.next();
            if (lSym == null)
                break;
            kind = lSym.getSymKind();
            if (kind == Sym.KIND_TAG)
                continue;
           if (fInitSymList.contains(lSym) == true)
               continue;
           PrintSym(lSym,indentlevel);
        }
    }
    /*
    *
    * PrintSym:
    *
    *
    **/
    private void PrintSym(Sym pSym,int indentlevel) {
        Sym lSym ;
        Type lType ;
        int kind;
        lSym = pSym;
        String visibility;     // public ,extern ...
        String StorageClass;   // auto ,static ...

        kind = lSym.getSymKind();
        Trace("[HIR SYM Symbol]" + "[kind="+ kind + "]" + lSym.toString(),1);
        switch (kind) {
        case Sym.KIND_VAR:
            lType = lSym.getSymType();

            visibility = fKeyWord.getSymKeyWord(((Var)lSym).getVisibility());
            StorageClass= "";
            if (((Var)lSym).getVisibility() != Sym.SYM_EXTERN &&
                ((Var)lSym).getVisibility() != Sym.SYM_PUBLIC) {
                StorageClass=
                fKeyWord.getSymKeyWord(((Var)lSym).getStorageClass());

            }

            String initializer = ((Var)lSym).getStorageClass()==Var.VAR_STATIC
                                    && ((Var)lSym).getInitialValue()!=null
                                    ? " = "+ConvertEXP( ((Var)lSym).getInitialValue()) : "";

            PrintLine(visibility+StorageClass+
                                    ConvertDECLARATION(lSym)+initializer+";",indentlevel);
            break;
        case Sym.KIND_SUBP:
            StorageClass= "";
            if (((Subp)lSym).getVisibility() == Sym.SYM_COMPILE_UNIT ) {
                    StorageClass=fKeyWord.getSymKeyWord(Var.VAR_STATIC);

            }
            visibility=fKeyWord.getSymKeyWord(((Subp)lSym).getVisibility());
            PrintLine(visibility +StorageClass+ConvertDECLARATION(lSym)+";",indentlevel);
            break;
        default: // KIND_LABEL,KIND_TYPE
            break;
        }
    }
    /*
    *
    * ConvertDECLARATION:
    *
    *  ex)
    *   int a;
    *   float x[10];
    *
    **/
    private String ConvertDECLARATION(Sym symVar) {
        StringBuffer tmpString;
        Sym nextSym;
        Type symType;
        Type nextType;
        Sym Def;
        int TypeKind;
        int i;
        int dim;

        tmpString = new StringBuffer();
        tmpString.setLength(0);
        symType = symVar.getSymType();
        TypeKind = symType.getTypeKind();

Trace("[HIR Decl]" + "[kind="+ TypeKind + "]" + symVar.toString(),1);
        switch (TypeKind) {
        case Type.KIND_VECTOR:  // Array
            nextType = symType;
            tmpString.append(symVar.getName());
            dim = symType.getDimension();
            for (i=0;i<dim; i++) {
                tmpString.append("[");
                long count =((VectorType)nextType).getElemCount();
                if (count >0)
                    tmpString.append(((VectorType)nextType).getElemCount());
                tmpString.append("]");
                nextType = ((VectorType)nextType).getElemType();
            }
            break;
        case  Type.KIND_STRUCT: // struct tag v (symVar)
        case  Type.KIND_UNION:
        case  Type.KIND_ENUM:
            nextType = symType;
            tmpString.append(symVar.getName());
            break;
        case  Type.KIND_SUBP: //  Subp
            SubpType    subpType      ;
            IrList     ParamList      ;
            subpType =(SubpType) symType;
            nextType = subpType.getReturnType();
            ParamList = subpType.getParamTypeList();
            tmpString.append(symVar.getName());
            tmpString.append(" ( ");
            if (ParamList != null) {
                Iterator It = ParamList.iterator();
                boolean ParamExist=false;
                while (It.hasNext()) {
                    ParamExist=true;
                    Type BaseType = ((Sym)It.next()).getSymType();
                        tmpString.append(ConvertBaseType(BaseType,""));
                        tmpString.append(",");
                }
                if (subpType.hasOptionalParam() == true && ParamExist == true) {
                    tmpString.append(" ... ");
                }
            }
            tmpString.setLength(tmpString.length()-1); // Cut ,
            tmpString.append(" ) ");
            break;
        default:
            nextType = symType;
            tmpString.append(symVar.getName());
            break;
        }

Trace("[HIR Decl]" + "Name=" + tmpString.toString(),1);
        return (ConvertBaseType(nextType, tmpString.toString()));
    }
    /**
    *
    * ConvertSymELEM:
    *
    * ex)
    *   struct a {
    *       int aa;
    *       int x[100];
    *    };
    *
    **/
    private void ConvertSymELEM(Sym symVar,int indentlevel) {
        StringBuffer   tmpString;
        Type symType;
        Type nextType;
        Sym tag ;
        int TypeKind;

        tmpString = new StringBuffer();
        tmpString.setLength(0);
        symType = symVar.getSymType();
        TypeKind = symType.getTypeKind();

        switch (TypeKind) {
        case  Type.KIND_STRUCT:
        case  Type.KIND_UNION:
            fOutString.append(fKeyWord.getTypeKeyWord(TypeKind));
            fOutString.append(ChangeTagName(symVar.getName()));
            Elem         lElem;
            IrList       lElemList = symVar.getSymType().getElemList();
            if (lElemList.size() == 0) {
                fOutString.append(" ;");
            } else {
                fOutString.append(" { ");
                PrintLine(fOutString.toString(),indentlevel) ;
                for (java.util.Iterator lElemIterator = lElemList.iterator();
                                                        lElemIterator.hasNext(); ) {
                    lElem = ((Elem)lElemIterator.next());
                    if (lElem != null) {
                        if (lElem.isBitField() == true)  {
                            PrintLine(
                                ConvertDECLARATION((Sym)lElem)+ ":"
                                +lElem.getBitSize()+";", indentlevel);
                        } else {
                            PrintLine(ConvertDECLARATION((Sym)lElem)+";", indentlevel);
                        }
                    }
                }
                fOutString.append(" };  ");
            }
            PrintLine(fOutString.toString(),indentlevel) ;
            break;
        case  Type.KIND_ENUM:
            fOutString.append(fKeyWord.getTypeKeyWord(TypeKind));
            fOutString.append(ChangeTagName(symVar.getName()));
            fOutString.append(" { ");
            PrintLine(fOutString.toString(),indentlevel) ;
            IrList       List1 = symVar.getSymType().getElemList();
            for (java.util.Iterator lElemIterator = List1.iterator();
                                                lElemIterator.hasNext();){
                IrList List2 = ((IrList)lElemIterator.next());
                if (List2 != null) {
                    for (java.util.Iterator Ie = List2.iterator();Ie.hasNext();){
                        Object s= Ie.next();
                        if (!(s instanceof java.lang.Integer)) {
                            tmpString.append(((Sym)s).getName());
                            tmpString.append(",");
                        }
                    }
                }
            }
            tmpString.setLength(tmpString.length()-1); // Cut ,
            fOutString.append(tmpString.toString());
            fOutString.append(" };  ");
            PrintLine(fOutString.toString(),indentlevel) ;
            break;
        case  Type.KIND_REGION:
            PrintLine("",indentlevel); // blankTag;
            break;
        default:
            PrintLine(ConvertDECLARATION(symVar)+";",indentlevel);
            break;
        }
    }
    /**
    *
    * ConvertBaseType:
    *
    * ex1)
    *  char
    *  ...
    * ex2)
    * char *
    *
    **/
    private String ConvertBaseType(Type pType,String Var) {
        int BaseType;
        PointerType ptrType;
        StructType ptrsType;
        SubpType    subpType;
        Type nextType;
        IrList ElemList;
        Elem   lElem;
        StringBuffer tmpString;
        Sym tag;

        tmpString = new StringBuffer();
        tmpString.setLength(0);

        if (pType.isConst() == true)  {
            tmpString.append("const  ");
            pType = pType.getOrigin();
        }
        if (pType.isVolatile() == true) {
            tmpString.append("volatile ");
            pType = pType.getOrigin();
        }

        BaseType =pType.getTypeKind();

Trace("[HIR BaseType DEBUG ]" + "[BaseType="+ BaseType+ "]" + pType.toStringDetail(),1);
Trace("[HIR BaseType DEBUG ]" + "[Base Var="+ Var+ "]" ,1);

        switch (BaseType) {
        case Type.KIND_POINTER:   // Pointer  *
            ptrType = (PointerType)pType;
            Type PointedType;

            PointedType =ptrType.getPointedType();
            int PointedKind=PointedType.getTypeKind();
            switch (PointedKind) {
            case Type.KIND_VECTOR:
                int dim = ((VectorType)PointedType).getDimension();
                Type ElementType =  PointedType;
                for (int i=0;i<dim; i++) {
                    ElementType = ((VectorType)ElementType).getElemType();
                }
                tmpString.append( ConvertBaseType(ElementType,
                    "("+fKeyWord.getTypeKeyWord(BaseType) + " " + Var + ")"
                                       + ConvertBaseType(PointedType,"") ) );
                break;
            case Type.KIND_SUBP:
                subpType =(SubpType) PointedType;
                nextType = subpType.getReturnType();
                            tmpString.append(ConvertBaseType(nextType,
                    "("+fKeyWord.getTypeKeyWord(BaseType) + " " + Var + ")"
                            + ConvertBaseType(PointedType,"") ) );
                break;
            case Type.KIND_POINTER:
                tmpString.append(ConvertBaseType(PointedType,
                fKeyWord.getTypeKeyWord(BaseType) + " " + Var));
                break;
            default:
                tmpString.append(ConvertBaseType(PointedType,""));
                tmpString.append(fKeyWord.getTypeKeyWord(BaseType));
                tmpString.append( " " + Var );
            }
            break;
        case Type.KIND_STRUCT:
            tmpString.append(fKeyWord.getTypeKeyWord(BaseType));
            tag = ((StructType)pType).getTag();
            tmpString.append(ChangeTagName(tag.getName())+ " ");
            tmpString.append( " " + Var );
            break;
        case Type.KIND_UNION:
            tmpString.append(fKeyWord.getTypeKeyWord(BaseType));
            tag = ((UnionType)pType).getTag();
            tmpString.append(ChangeTagName(tag.getName())+ " ");
            tmpString.append( " " + Var );
            break;
        case Type.KIND_ENUM:
            tmpString.append(fKeyWord.getTypeKeyWord(BaseType));
            tag = ((EnumType)pType).getTag();
            tmpString.append(ChangeTagName(tag.getName())+ " ");
            tmpString.append( " " + Var );
            break;
        case Type.KIND_VECTOR:  // Array;
            long dim;
            long count;
            long i;

            dim = pType.getDimension();
            nextType = pType;
            tmpString.append(Var);

            for (i=0;i<dim; i++) {
                tmpString.append("[");
                count=((VectorType)nextType).getElemCount();
                if (count != 0)
                    tmpString.append(Long.toString(count));
                tmpString.append("]");
                nextType = ((VectorType)nextType).getElemType();
            }
            break;
        case Type.KIND_SUBP:   //
            IrList  ParamList ;
            boolean ParamExist;

            subpType =(SubpType) pType;
            nextType = subpType.getReturnType();
            ParamList = subpType.getParamTypeList();
            tmpString.append("( ");
            if (ParamList != null) {
                Iterator It = ParamList.iterator();
                ParamExist = false;
                while (It.hasNext()) {
                    Type BaseTypes = ((Sym)It.next()).getSymType();
                        tmpString.append(ConvertBaseType(BaseTypes,""));
                        tmpString.append(",");
                    ParamExist = true;
                }
                if (subpType.hasOptionalParam() == true && ParamExist == true) {
                    tmpString.append(" ... ");
                }
            }
            tmpString.setLength(tmpString.length()-1); // Cut ,
            tmpString.append(")");
            break;
        case Type.KIND_ADDRESS:
            tmpString.append(fKeyWord.getTypeKeyWord(BaseType) + " " + Var);
            break;
        default:
            tmpString.append(fKeyWord.getTypeKeyWord(BaseType) + " " + Var);
            break;
        }
        return tmpString.toString();
    }

    //##70 BEGIN
private Stmt ConvertASMSTMT(AsmStmt pStmt,int pIndentlevel)
{
  fOutString.append(fKeyWord.getOpKeyWord(HIR.OP_ASM));
  fOutString.append("(");
  fOutString.append(ConvertEXP((Exp)pStmt.getChild1()));
  for (Iterator lIter = ((HirList)pStmt.getChild2()).iterator();
       lIter.hasNext(); ) {
    HIR lHir = (HIR)lIter.next();
    fOutString.append(", ");
    fOutString.append(ConvertEXP((Exp)lHir));
  }
  fOutString.append(")");
  PrintLine(fOutString.toString()+ ";",pIndentlevel);
  return (pStmt.getNextStmt());
} // ConvertASMSTMT

private String ConvertHIRLIST(HirList pHirList)
{
  StringBuffer tmpString = new StringBuffer();
  boolean lInsertComma = false;
  tmpString.append("(");
  for (Iterator lIter = pHirList.iterator();
       lIter.hasNext(); ) {
    HIR lHir = (HIR)lIter.next();
    if (lInsertComma)
      tmpString.append(", ");
    tmpString.append(ConvertEXP((Exp)lHir));
  }
  tmpString.append(")");
  return (tmpString.toString());
} // ConvertASMSTMT
    //##70 END
    /*
    *
    * putDefListConst:
    *
    *
    **/
    private String putDefListConst(String value,boolean isunsigned) {
        String name ;
        String Umark;

        if (isunsigned == true)
            Umark = "U";
        else
            Umark = "";
        name =fKeyWord.getOpKeyWord(HIR.OP_CONST)
                                    + ChangeConstSTRING(value) + Umark+" ";
        if (fFirstSearch == true) {
            fPrintDefine.DefList.put(name,"("+value+Umark+")");
        }
        return name;
    }
    /**
    *
    * Trace:
    *
    *
    **/
    private void Trace(String s,int level) {
        if (level <= fTraceLevel) {
            //##98 fTraceList.add("//HIR2C:" +s);
            fDebugTrace.add("HIR2C:"+s); //##98
         }
    }
    /**
    *
    * TraceDebug:
    *
    *
    **/
    private void TraceDebug(String s,int level) {
        if (level <= fTraceLevel) {
            PrintOutln("//HIR2C[DEBUG]"+s);
        }
    }
    /**
    *
    * PrintLine:
    *
    *
    **/
    void PrintLine(String s,int indentlevel) {
        int i;
        //System.out.println(s);
        if (fFirstSearch == false) {
            //
            // (1)Print Trace
            //

            for (Iterator Ie =fTraceList.iterator(); Ie.hasNext();) {
                PrintOutln((String)Ie.next());
            }

            //
            // (2) Print Infomation(#pragma etc...)
            //

            fInf.PrintValue();
            for (i=0;i<indentlevel;i++) {
                PrintOut("    ");
            }
            //
            // (3) Print C-CODE
            //

            PrintOutln(s);
        }
        fTraceList.clear();
        fOutString.setLength(0);
        fInf.clear();
    }
    /**
    *
    * PrintOutln:
    *
    *
    **/
    private void PrintOutln(String s) {
        fprintOut.println(s);
    }
    /**
    *
    * PrintOut:
    *
    *
    **/
    private void PrintOut(String s) {
        fprintOut.print(s);
    }
}
