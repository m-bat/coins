/* ----------------------------------------------------------
 %   Copyright (C) 2003 The Coins Project Group
 %       (Read COPYING for detailed information.)
 ----------------------------------------------------------- */
// Compile:
//   javac -d ./classes examples/SimpleMain.java
// Run:
//   java -classpath ./classes SimpleMain -coins:trace=Sym.1/HIR.1/back

import java.io.IOException;
import coins.FatalError;
import coins.FlowRoot;
import coins.HirRoot;
import coins.IoRoot;
import coins.IrRoot;
import coins.SymRoot;
import coins.sym.Const;
import coins.sym.Elem;
import coins.sym.Label;
import coins.sym.NamedConst;
import coins.sym.Param;
import coins.sym.PointerType;
import coins.sym.RegionType;
import coins.sym.StructType;
import coins.sym.Subp;
import coins.sym.Sym;
import coins.sym.SymTable;
import coins.sym.Type;
import coins.sym.Var;
import coins.sym.VectorType;
import coins.hir2lir.ConvToNewLIR;
import coins.casttohir.ToHirC;
import coins.casttohir.ToHirBase;
import coins.ir.IrList;
import coins.ir.hir.AssignStmt;
import coins.ir.hir.BlockStmt;
import coins.ir.hir.ConstNode;
import coins.ir.hir.ElemNode;
import coins.ir.hir.Exp;
import coins.ir.hir.ExpStmt;
import coins.ir.hir.ForStmt;
import coins.ir.hir.HIR;
import coins.ir.hir.IfStmt;
import coins.ir.hir.IndexedLoopStmt;
import coins.ir.hir.JumpStmt;
import coins.ir.hir.Program;
import coins.ir.hir.SetDataStmt;
import coins.ir.hir.Stmt;
import coins.ir.hir.SubpDefinition;
import coins.ir.hir.SubpDefinitionImpl;
import coins.ir.hir.SubpNode;
import coins.ir.hir.VarNode;
import coins.aflow.FlowResults;
import coins.aflow.RegisterFlowAnalClasses;
import coins.aflow.ShowFlow;
import coins.aflow.SubpFlow;
import coins.backend.BackEnd;
import coins.backend.SyntaxError;
import coins.backend.SyntaxErrorException;
import coins.backend.util.ImList;
import coins.driver.CommandLine;
import coins.driver.CompileSpecification;
import coins.driver.Trace;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.PrintWriter;
import java.io.File;
import java.text.ParseException;

/**
 SimpleMain.java:
 Usually, a parser produces HIR and symbol table from
 source program using HIR access methods and Sym access methods.
 main() of SimpleMain class produces HIR and symbol table
 by using the access methods without calling parser.
 There are many knowhows in using the HIR and Sym access methods.
 The explanation of these knowhows may take very many words.
 Reading the main(), you will understand how to build HIR and
 symbol table in your parser and can guess some of the knowhows.
 If you have difficulties in understanding this program,
 please read the interfaces IR, HIR, Sym, IoRoot, SymRoot, HirRoot, etc.
 as it is written in README.txt. You should also read the
 specifications of methods used in this class.
 **/
public class
  SimpleMain
{

  public static void
    main(String[] args)
  {
    IoRoot ioRoot;
    SymRoot symRoot;
    IrRoot irRoot;
    HirRoot hirRoot;
    FlowRoot hirFlowRoot;
    ToHirC toHirC;
    ToHirBase toHirBase;

    coins.ir.IrList lSubpDefList;
    SubpDefinition lSubpDef;
    Iterator lSubpDefIterator;
    boolean lLirAnalIsCoded;

    Sym sym;
    HIR hir;
    SymTable lLocalSymTable1, lLocalSymTable2, lLocalSymTable3;
    Var lVi, lVj, lVk, lVa, lVb, lVc, lVd, lVaa;
    Const lC0, lC1, lC2, lC3, lC4, lC5, lC10, lC100, lStr1;
    Subp lPrintf, lMain;
    Param lParam, lParam1, lParam2;
    Label lLab1, lLab2, lLab3, lLab4, lLab5, lLab6;
    Label lLoopBackLabel1, lLoopStepLabel1, lLoopEndLabel1;
    Type lCharPtr;
    Stmt lAssign0, lAssign1, lAssign2, lAssign3, lAssign4,
      lAssign5, lAssign6, lAssign7, lAssign8, lAssign9,
      lAssign10, lAssign11, lAssign12, lAssign13, lAssign14,
      lAssign15, lAssign16, lAssign17, lAssign18, lAssign19,
      lNull1;
    JumpStmt lJump1;
    IfStmt lIf1;
    ForStmt lFor1, lFor2;
    Stmt lLoopInitStmt, lLoopStepPart, lConditionalInit,
      lConditionalInit2;
    Stmt lCallStmt, lLabeledStmt1, lLabeledStmt2;
    Exp lMult1, lAdd1, lAdd2, lSub1, lExp1, lExp2, lExp3, lExp4;
    Exp lExp5, lExp6, lExp7, lExp8;
    Exp lComp1, lComp2, lComp3;
    BlockStmt lBlockStmt, lBlockStmt2;
    VarNode lVarNode0, lVarNode1, lVarNode2, lVarNode3, lVarNode4,
      lVarNode5, lVarNode6, lVarNode7, lVarNode8, lVarNode9,
      lVarNode10, lVarNode11, lVarNode12, lVarNode13, lVarNode14,
      lVarNode15, lVarNode16, lVarNode17, lVarNode18, lVarNode19;
    ConstNode lConstNode0, lConstNode1, lConstNode2, lConstNode3,
      lConstNode4, lConstNode5, lConstNode6, lConstNode7,
      lConstNode8, lConstNode9, lConstNode10, lConstNode11;
    SubpNode lSubpNode1;
    SubpDefinition lMainDef;
    coins.ir.IrList lParamList, lFormalParamList;
    Type lSubpType, lVectorType1;

    // Prepare ioRoot to be passed to I/O access methods and SymRoot.
    // See IoRoot.java.
    try {
      CompileSpecification lSpec = new CommandLine(args);
      ioRoot = new IoRoot(new File("examples", "simplemaininput.txt"),
                          System.out, System.out, System.err, lSpec);
      //=== Preparation.
      // Prepare symRoot, hirRoot, and initiate Sym information
      // and HIR information. This sequence of statements are mostly
      // the same for many compilers based on the COINS infrastructure.
      // (coins.driver.Driver includes similar processing.)
      symRoot = new SymRoot(ioRoot);
      hirRoot = new HirRoot(symRoot);
      symRoot.attachHirRoot(hirRoot);
      symRoot.initiate(); // Do initiation for symbol table SymTableRoot, etc.
      sym = symRoot.sym;  // Prepare for quick access to symRoot.sym.
      hir = hirRoot.hir;  // Prepare for quick access to hirRoot.hir.

      //=== C type expressions.
      // Prepare to define main program.
      // See defineSubp of Sym.java.
      lMain = sym.defineSubp("main".intern(), symRoot.typeInt);
      lMain.setVisibility(Sym.SYM_PUBLIC); //##44
      //---- Build HIR ----//
      //-- int printf(char* pFormat, ...);
      lPrintf = sym.defineSubp("printf".intern(), symRoot.typeInt);
      lCharPtr = sym.pointerType(symRoot.typeChar);
      lLocalSymTable1 = symRoot.symTableRoot.pushSymTable(lPrintf);
//      lParam1 = sym.defineParam("pFormat".intern(), lCharPtr);
//##44  lParam1 = sym.defineParam("pFormat".intern(), symRoot.typeStringAny);
      lParam1 = sym.defineParam("pFormat".intern(), lCharPtr); //##44
      lPrintf.addParam(lParam1);
      lPrintf.setVisibility(Sym.SYM_EXTERN);
      lPrintf.setOptionalParam();
      lPrintf.closeSubpPrototype(); //##47
      lLocalSymTable1.popSymTable();
      //-- int main()
      lLocalSymTable2 = symRoot.symTableRoot.pushSymTable(lMain);
      lMain.closeSubpHeader();
      lMainDef = hir.subpDefinition(lMain, lLocalSymTable2);
      ((Program)hirRoot.programRoot).addSubpDefinition(lMainDef);
      //-- {
      lBlockStmt = hir.blockStmt(null); // Make an empty block to which
                                        // statements are to be added.
      //-- int i;
      lVi = sym.defineVar("i".intern(), symRoot.typeInt);
      //   Build HIR subtree from leaf to root.
      //   To make a Sym node, you should make an instance of Sym
      //   to be attached to the Sym node.
      //   It is strongly recommended to use factory methods
      //   of Sym such as defineVar, intConst, etc. and to use
      //   factory methods of HIR such as varNode, constNode,
      //   assignStmt, ifStmt, etc. It is not recommended to use
      //   constructors directly without using factory methods.
      //   Direct use of constructors may cause misuse of
      //   Sym and HIR access methods.
      //   See HIR.java and Sym.java.
      //-- lab1: i = 0;
      lC0 = sym.intConst("0".intern(), symRoot.typeInt);
      lC1 = sym.intConst("1".intern(), symRoot.typeInt);
      lVarNode0 = hir.varNode(lVi);
      lConstNode0 = hir.constNode(lC0);
      AssignStmt lAssign01 = hir.assignStmt(lVarNode0, lConstNode0);
      lLab1 = sym.defineLabel("lab1".intern());
      lAssign0 = lAssign01.attachLabel(lLab1);
      lBlockStmt.addLastStmt(lAssign0);
      //-- a = 3;
      lVa = sym.defineVar("a".intern(), symRoot.typeInt);
      lC3 = sym.intConst("3".intern(), symRoot.typeInt);
      lVarNode1 = hir.varNode(lVa);
      lConstNode1 = hir.constNode(lC3);
      lAssign1 = hir.assignStmt(lVarNode1, lConstNode1);
      lBlockStmt.addLastStmt(lAssign1);
      //-- b =  a * 5;
      lVb = sym.defineVar("b".intern(), symRoot.typeInt);
      lVarNode2 = hir.varNode(lVa);
      lVarNode3 = hir.varNode(lVb);
      lC5 = sym.intConst(5, symRoot.typeInt);
      lConstNode2 = hir.constNode(lC5);
      lMult1 = hir.exp(HIR.OP_MULT, lVarNode2, lConstNode2);
      lAssign2 = hir.assignStmt(lVarNode3, lMult1);
      lBlockStmt.addLastStmt(lAssign2);
      //-- printf("%d\n", b);
      lSubpNode1 = hir.subpNode(lPrintf);
      lParamList = hir.irList();
      lStr1 = sym.stringConst( ("\"" + "%d\n" + "\"").intern());
      lConstNode3 = hir.constNode(lStr1);
      lParamList.add(hir.decayExp(lConstNode3));  //##44
      //##44 lParamList.add(lConstNode3); // This simplification is also permitted.
      lVarNode4 = hir.varNode(lVb);
      lParamList.add(lVarNode4);
      lCallStmt = hir.callStmt(hir.exp(HIR.OP_ADDR, lSubpNode1), lParamList); //##44
      //##44 lCallStmt may be simplified as
      //##44   lCallStmt = hir.callStmt(lSubpNode1, lParamList);
      lBlockStmt.addLastStmt(lCallStmt);
      //-- i = i + 1;
      lVarNode5 = hir.varNode(lVi);
      lVarNode6 = hir.varNode(lVi);
      lConstNode4 = hir.constNode(lC1);
      lAdd1 = hir.exp(HIR.OP_ADD, lVarNode5, lConstNode4);
      lAssign3 = hir.assignStmt(lVarNode6, lAdd1);
      lBlockStmt.addLastStmt(lAssign3);
      //-- if (i < 5) goto lab1;
      lConstNode5 = hir.constNode(lC5);
      lVarNode7 = hir.varNode(lVi);
      lComp1 = hir.exp(HIR.OP_CMP_LT, lVarNode7, lConstNode5);
      lJump1 = hir.jumpStmt(lLab1);
      lIf1 = hir.ifStmt(lComp1, lJump1, null);
      lBlockStmt.addLastStmt(lIf1);
      //-- float c;
      lVc = sym.defineVar("c".intern(), symRoot.typeFloat);
      lC2 = sym.floatConst("2.0".intern(), symRoot.typeFloat);
      lVarNode5 = hir.varNode(lVc);
      lConstNode4 = hir.constNode(lC2);
      lAssign4 = hir.assignStmt(lVarNode5, lConstNode4);
      //-- lab2: c = 2.0;
      lLab2 = sym.defineLabel("lab2".intern());
      lAssign4.attachLabel(lLab2);
      //## BEGIN test (not recommended; should use attachLabel as above.)
      //-- lab3: ;
      lLab3 = sym.defineLabel("lab3".intern());
      lNull1 = hir.nullStmt();
      lLabeledStmt1 = hir.labeledStmt(lLab3, lNull1);
      lBlockStmt.addLastStmt(lLabeledStmt1);
      //## END test
      //--  i = 0;
      lVarNode6 = hir.varNode(lVi);
      lConstNode5 = hir.constNode(lC0);
      lLoopInitStmt = hir.assignStmt(lVarNode6, lConstNode5);
      //-- int j;
      lVj = sym.defineVar("j".intern(), symRoot.typeInt);
      //-- j = 100;
      lVarNode7 = hir.varNode(lVj);
      lConstNode6 = hir.intConstNode(100); //##44
      lConditionalInit = hir.assignStmt(lVarNode7, lConstNode6);
      //-- int aa[100];
      lVectorType1 = sym.vectorType(symRoot.typeInt, 100);
      lVaa = sym.defineVar("aa".intern(), lVectorType1);
      //-- {
      lBlockStmt2 = hir.blockStmt(null);
      //-- aa[i] = j;  //##44
      lVarNode8 = hir.varNode(lVi);
      lVarNode9 = hir.varNode(lVaa);
      lExp1 = hir.subscriptedExp(lVarNode9, lVarNode8);
      lAssign6 = hir.assignStmt(lExp1, hir.varNode(lVj)); //##44
      lBlockStmt2.addLastStmt(lAssign6);
      //-- j = j - 1;
      lVarNode10 = hir.varNode(lVj);
      lVarNode11 = hir.varNode(lVj);
      lSub1 = hir.exp(HIR.OP_SUB, lVarNode10, hir.intConstNode(1));
      lAssign7 = hir.assignStmt(lVarNode11, lSub1);
      lBlockStmt2.addLastStmt(lAssign7);
      //-- }
      lVarNode12 = hir.varNode(lVi);
      lComp2 = hir.exp(HIR.OP_CMP_LT, lVarNode12,
                       hir.intConstNode(100));
      //-- i = i + 1;
      lVarNode13 = hir.varNode(lVi);
      lVarNode14 = hir.varNode(lVi);
      lAdd2 = hir.exp(HIR.OP_ADD, lVarNode14, hir.intConstNode(1));
      lLoopStepPart = hir.assignStmt(lVarNode13, lAdd2);
      //-- for (i = 0; i < 100; i = i + 1) {
      //     aa[i] = j; j = j - 1; }  //##44
      //    // with {j = 100;} as conditional init statement. //##44
      //    // ConditionalInit is deleted.
      lLoopBackLabel1 = lLocalSymTable2.generateLabel();
      lLoopStepLabel1 = lLocalSymTable2.generateLabel();
      lLoopEndLabel1 = lLocalSymTable2.generateLabel();
      lFor1 = hir.forStmt(lLoopInitStmt, lLoopBackLabel1, lComp2,
                          lBlockStmt2, lLoopStepLabel1, lLoopStepPart,
                          lLoopEndLabel1);
      // lFor1.addToConditionalInitPart(lConditionalInit); // Deleted.
      lBlockStmt.addLastStmt(lConditionalInit); //##44
      lBlockStmt.addLastStmt(lFor1);

      // Named constant test.
      //-- const m = 3;
      //   var x1;
      //   x1 = m;
      NamedConst lNamedConst = sym.namedConst("m".intern(), lC3);
      Var lX1 = sym.defineVar("x1".intern(), symRoot.typeInt);
      AssignStmt lAssignX1 = hir.assignStmt(hir.varNode(lX1),
                                            hir.constNode(lNamedConst));
      lBlockStmt.addLastStmt(lAssignX1);

      // Define a structure and use its elements.
      //   See structType of Sym.java.
      // struct listNode {
      //   int nodeValue;
      //   struct listNode *next;
      //  } listAnchor, listNode1;
      //  int *valuePtr;
      //  listAnchor.next = &listNode1;
      //  valuePtr = &list1.nodeValue;
      Sym lTag = symRoot.symTableCurrent.generateTag("listNode".intern());
      StructType lListStruct = sym.structType(null, lTag); // Make incomplete type.
      PointerType lListPtrType = sym.pointerType(lListStruct);
      PointerType lIntPtrType = sym.pointerType(symRoot.typeInt);
      symRoot.symTableCurrent.pushSymTable(lListStruct);
      Elem lValue = sym.defineElem("nodeValue".intern(), symRoot.typeInt);
      Elem lNext = sym.defineElem("next".intern(), lListPtrType);
      lListStruct.addElem(lValue);
      lListStruct.addElem(lNext);
      lListStruct.finishStructType(true);
      symRoot.symTableCurrent.popSymTable();
      Var lListAnchor = sym.defineVar("listAnchor".intern(), lListStruct);
      Var lListNode1 = sym.defineVar("listNode1".intern(), lListStruct);
      VarNode lListAnchorNode1 = hir.varNode(lListAnchor);
      VarNode lListVarNode1 = hir.varNode(lListNode1);
      ElemNode lNextNode1 = hir.elemNode(lNext);
      Exp lQualExp1 = hir.qualifiedExp(lListAnchorNode1, lNextNode1);
      AssignStmt lAssignAddr1 =
          hir.assignStmt(lQualExp1,hir.exp(HIR.OP_ADDR, lListVarNode1));
      lBlockStmt.addLastStmt(lAssignAddr1);
      Var lValuePtr = sym.defineVar("valuePtr".intern(), lIntPtrType);
      ElemNode lValueNode1 = hir.elemNode(lValue);
      VarNode lListVarNode2 = hir.varNode(lListNode1);              //##51
      Exp lQualExp2 = hir.qualifiedExp(lListVarNode2, lValueNode1); //##51
      AssignStmt lAssignElemAddr1 = hir.assignStmt(hir.varNode(lValuePtr),
        hir.exp(HIR.OP_ADDR, lQualExp2));
      lBlockStmt.addLastStmt(lAssignElemAddr1);
      //  struct listNode *listNodePtr;
      //  listNodePtr = &listNode1;
      //  listNodePtr->nodeValue = 3;
      //  int *castPtr;
      //  castPtr = (int *)listNodePtr;
      //  ((struct listNode *)castPtr)->nodeValue = 2;
      Var lListNodePtr = sym.defineVar("listNodePtr".intern(), lListPtrType);
      AssignStmt lAssignAddr2 =
          hir.assignStmt(hir.varNode(lListNodePtr),
                         hir.exp(HIR.OP_ADDR, hir.varNode(lListNode1)));
      lBlockStmt.addLastStmt(lAssignAddr2);
      Exp lPointedExp3 = hir.pointedExp(hir.varNode(lListNodePtr),
                                        hir.elemNode(lValue));
      AssignStmt lAssignValue2 = hir.assignStmt(lPointedExp3,
                                                hir.intConstNode(3));
      lBlockStmt.addLastStmt(lAssignValue2);
      Var lCastPtr = sym.defineVar("castPtr".intern(), lIntPtrType);
      Exp lConvExp1 = hir.convExp(lIntPtrType, hir.varNode(lListNode1));
      AssignStmt lAssignAddr3 = hir.assignStmt(hir.varNode(lCastPtr),
                                               lConvExp1);
      lBlockStmt.addLastStmt(lAssignAddr3);
      Exp lConvExp2 = hir.convExp(lListPtrType, hir.varNode(lCastPtr));
      Exp lPointedExp4 = hir.pointedExp(lConvExp2, hir.elemNode(lValue));
      AssignStmt lAssignAddr4 = hir.assignStmt(lPointedExp4,
                                               hir.intConstNode(2));
      lBlockStmt.addLastStmt(lAssignAddr3);

      //---- Call Fortran subroutine
      //-- Prototype declaration
      //   SUBROUTINE MATVECT( MATRIX, VECTIN, VECTOUT, ELEMCOUNT )
      //     REAL*8 MATRIX(ELEMCOUNT, ELEMCOUNT)
      //     REAK*8 VECTIN(ELEMCOUNT), VECTOUT(ELEMCOUNT)
      //     INTEGER ELEMCOUNT
      //   END SUBROUTINE   // End of prototype declaration.
      // Record prototype declaration in the global symbol table outside main.
      lLocalSymTable2.popSymTable();
      Subp lFortSubp0 = sym.defineSubp("MATVECT".intern(),
                                       symRoot.typeVoid);
      SymTable lFortSymTable0 = symRoot.symTableCurrent.pushSymTable(
        lFortSubp0);
      PointerType lIntPtrT0 = sym.pointerType(symRoot.typeInt);
      Param lElemCount0 = sym.defineParam("ELEMCOUNT".intern(),
                                          lIntPtrT0);
      VectorType lFortVectT0 =
          sym.vectorType(null, symRoot.typeDouble,
                          hir.exp(HIR.OP_CONTENTS, hir.varNode(lElemCount0)),
                          hir.constNode(lC1));
      PointerType lFortVectPtrT0 = sym.pointerType(lFortVectT0);
      VectorType lFort2dimArrayT0 = sym.vectorType(null, lFortVectT0,
        hir.exp(HIR.OP_CONTENTS, hir.varNode(lElemCount0)),
        hir.constNode(lC1));
      PointerType lFort2dimArrayPtrT0 = sym.pointerType(
        lFort2dimArrayT0);
      lFortSubp0.addParamType(lFort2dimArrayPtrT0);
      lFortSubp0.addParamType(lFortVectPtrT0);
      lFortSubp0.addParamType(lFortVectPtrT0);
      lFortSubp0.addParamType(lIntPtrT0);
      lFortSubp0.setVisibility(Sym.SYM_PUBLIC);
      lFortSubp0.closeSubpPrototype();
      lFortSymTable0.popSymTable(); // Exit from the scope of prototype declaration.
      lLocalSymTable2 = symRoot.symTableCurrent.
                        reopenSymTable(lLocalSymTable2);
      //-- REAL*8 MM(100, 100)
      //   REAL*8 XX(100), YY(100)
      VectorType lDoubleVectT = sym.vectorType(null,
                                               symRoot.typeDouble, 100L, 1L);
      VectorType lDoubleMatrixT = sym.vectorType(null, lDoubleVectT,
                                                 100L, 1L);
      Var lVarMM = sym.defineVar("MM".intern(), lDoubleMatrixT);
      Var lVarXX = sym.defineVar("XX".intern(), lDoubleVectT);
      Var lVarYY = sym.defineVar("YY".intern(), lDoubleVectT);
      //-- CALL MATVECT(MM, XX, YY, 100)

      SubpNode lSubpNode2 = hir.subpNode(lFortSubp0);
      coins.ir.IrList lParamList2 = hir.irList();
      lParamList2.add(hir.exp(HIR.OP_ADDR, hir.varNode(lVarMM)));
      lParamList2.add(hir.exp(HIR.OP_ADDR, hir.varNode(lVarXX)));
      lParamList2.add(hir.exp(HIR.OP_ADDR, hir.varNode(lVarYY)));
      lC100 = sym.intConst("100".intern(), symRoot.typeInt);
      lParamList2.add(hir.exp(HIR.OP_ADDR, hir.constNode(lC100)));
      ExpStmt lCallStmt2 = hir.callStmt(lSubpNode2, lParamList2);
      lBlockStmt.addLastStmt(lCallStmt2);

      //-- }
      lMainDef.setHirBody(lBlockStmt);

      //=== Fortran subroutine definition ====//

      //-- SUBROUTINE MATVECT( MATRIX, VECTIN, VECTOUT, ELEMCOUNT )
      //--   REAL*8 MATRIX(ELEMCOUNT, ELEMCOUNT)
      //--   REAK*8 VECTIN(ELEMCOUNT), VECTOUT(ELEMCOUNT)
      //--   INTEGER ELEMCOUNT
      // For subprogram definition with subprogram body.
      Subp lFortSubp1;
      SymTable lFortSymTable1;
      Subp lFortSubpx = (Subp) symRoot.symTableCurrent.search(
        "MATVECT".intern(), Sym.KIND_SUBP);

      if (lFortSubpx == null) { // Not yet defined.
        lFortSubp1 = sym.defineSubp("MATVECT".intern(),
                                    symRoot.typeVoid);
        lFortSymTable1 = symRoot.symTableRoot.pushSymTable(lFortSubp1);
      }
      else { // Already defined.
        lFortSubp1 = lFortSubpx;
        lFortSymTable1 = symRoot.symTableCurrent.
                         reopenSymTable(lFortSubpx.getSymTable());
      }
      PointerType lIntPtrT = sym.pointerType(symRoot.typeInt);
      Param lElemCount = (Param) lFortSymTable1.search("ELEMCOUNT".
        intern(),
        Sym.KIND_PARAM);
      // Get the parameter if it is already registered.
      if (lElemCount == null) // If not, register it.
        lElemCount = sym.defineParam("ELEMCOUNT".intern(), lIntPtrT);
      VectorType lFortVectT = sym.vectorType(null, symRoot.typeDouble,
                                             hir.exp(HIR.OP_CONTENTS,
        hir.varNode(lElemCount)),
                                             hir.constNode(lC1));
      PointerType lFortVectPtrT = sym.pointerType(lFortVectT);
      VectorType lFort2dimArrayT =
          sym.vectorType(null, lFortVectT,
                         hir.exp(HIR.OP_CONTENTS, hir.varNode(lElemCount0)),
                        hir.constNode(lC1));
      PointerType lFort2dimArrayPtrT = sym.pointerType(lFort2dimArrayT);
      Param lMatrix = sym.defineParam("MATRIX".intern(),
                                      lFort2dimArrayPtrT);
      Param lVectorIn = sym.defineParam("VECTORIN".intern(),
                                        lFortVectPtrT);
      Param lVectorOut = sym.defineParam("VECTOROUT".intern(),
                                         lFortVectPtrT);
      IrList lParamTypeList = lFortSubp1.getParamTypeList();
      lFortSubp1.addParam(lMatrix);
      lFortSubp1.addParam(lVectorIn);
      lFortSubp1.addParam(lVectorOut);
      lFortSubp1.addParam(lElemCount);
      if (lParamTypeList != null) { // Prototype is given. Check consistency.
        Type lParamType;
        Iterator lParamIterator = lParamTypeList.iterator();
        if (lParamIterator.hasNext()) {
          lParamType = (Type) lParamIterator.next();
          if (lParamType.getFinalOrigin() !=
              lMatrix.getSymType().getFinalOrigin())
            ioRoot.msgWarning.put(3111, "parameter unmatch " +
                                  lMatrix.toString());
        }
        if (lParamIterator.hasNext()) {
          lParamType = (Type) lParamIterator.next();
          if (lParamType.getFinalOrigin() !=
              lVectorIn.getSymType().getFinalOrigin())
            ioRoot.msgWarning.put(3111, "parameter unmatch " +
                                  lVectorIn.toString());
        }
        if (lParamIterator.hasNext()) {
          lParamType = (Type) lParamIterator.next();
          if (lParamType.getFinalOrigin() !=
              lVectorOut.getSymType().getFinalOrigin())
            ioRoot.msgWarning.put(3111, "parameter unmatch " +
                                  lVectorOut.toString());
        }
        if (lParamIterator.hasNext()) {
          lParamType = (Type) lParamIterator.next();
          if (lParamType.getFinalOrigin() !=
              lElemCount.getSymType().getFinalOrigin())
            ioRoot.msgWarning.put(3111, "parameter unmatch " +
                                  lElemCount.toString());
        }
      }
      lFortSubp1.setVisibility(Sym.SYM_PUBLIC);
      lFortSubp1.closeSubpHeader();
      SubpDefinition lFortSubpDef1 = hir.subpDefinition(lFortSubp1,
        lFortSymTable1);
      ( (Program) hirRoot.programRoot).addSubpDefinition(
        lFortSubpDef1);
      BlockStmt lSubpBody2 = hir.blockStmt(null);
      //--    INTEGER II, JJ
      Var lVarII = sym.defineVar("II".intern(), symRoot.typeInt);
      Var lVarJJ = sym.defineVar("JJ".intern(), symRoot.typeInt);
      //--    DO 20 II = 1, ELEMCOUNT
      Exp lStartVal1 = hir.constNode(lC1);
      Exp lEndVal1 = hir.exp(HIR.OP_CONTENTS, hir.varNode(lElemCount));
      Exp lStepVal1 = hir.constNode(lC1);
      BlockStmt lLoopBody1 = hir.blockStmt(null);
      //--      VECTOUT(II) = 0.0
      Const lDoubleZero = sym.floatConst("0.0".intern(), //##44
                                         symRoot.typeDouble);
      lAssign15 = hir.assignStmt(
        hir.subscriptedExp(
        hir.exp(HIR.OP_CONTENTS, hir.varNode(lVectorOut)),
        hir.varNode(lVarII)),
        hir.constNode(lDoubleZero));
      lLoopBody1.addLastStmt(lAssign15);
      //--      DO 10 JJ = 1; ELEMCOUNT
      Exp lStartVal2 = hir.constNode(lC1);
      Exp lEndVal2 = hir.exp(HIR.OP_CONTENTS, hir.varNode(lElemCount));
      Exp lStepVal2 = hir.constNode(lC1);
      BlockStmt lLoopBody2 = hir.blockStmt(null);
      //--        VECTOUT(II) = VECTOUT(II) + MATRIX(JJ,II) * VECTIN(JJ)
      Exp lSubsExp0 = hir.subscriptedExp( //##29
        hir.exp(HIR.OP_CONTENTS, hir.varNode(lMatrix)),
        hir.varNode(lVarJJ)); //##29
      Exp lSubsExp1 = hir.subscriptedExp(lSubsExp0, //##29
                                         hir.varNode(lVarII));
      Exp lSubsExp2 = hir.subscriptedExp(
        hir.exp(HIR.OP_CONTENTS, hir.varNode(lVectorIn)),
        hir.varNode(lVarJJ));
      Exp lMultExp1 = hir.exp(HIR.OP_MULT, lSubsExp1, lSubsExp2);
      Exp lSubsExp3 = hir.subscriptedExp(
        hir.exp(HIR.OP_CONTENTS, hir.varNode(lVectorOut)),
        hir.varNode(lVarII));
      Exp lAddExp1 = hir.exp(HIR.OP_ADD, lSubsExp3, lMultExp1);
      Exp lSubsExp4 = hir.subscriptedExp(
        hir.exp(HIR.OP_CONTENTS, hir.varNode(lVectorOut)),
        hir.varNode(lVarII));
      lAssign16 = hir.assignStmt(lSubsExp4, lAddExp1);
      lLoopBody2.addLastStmt(lAssign16);
      //-- 10   CONTINUE
      IndexedLoopStmt lIndexedLoop2 =
        hir.indexedLoopStmt(lVarJJ, lStartVal2, lEndVal2,
                            lStepVal2, true, lLoopBody2);
      lLoopBody1.addLastStmt(lIndexedLoop2);
      //-- 20 CONTINUE
      IndexedLoopStmt lIndexedLoop1 =
        hir.indexedLoopStmt(lVarII, lStartVal1, lEndVal1,
                            lStepVal1, true, lLoopBody1);
      lSubpBody2.addLastStmt(lIndexedLoop1);
      //--    RETURN
      Stmt lReturn2 = hir.returnStmt();
      lSubpBody2.addLastStmt(lReturn2);
      //--    END
      lFortSubpDef1.setHirBody(lSubpBody2);
      lFortSymTable1.popSymTable();

      //-- function tableValue(index)
      //--   integer index
      //--   common /table/length, value
      //--   integer length, value(100)
      //--   tableValue = value(index)
      //--   return
      //-- end
      // See regionType of Sym.java.
      Subp lFortSubp2 = sym.defineSubp("tableValue".intern(),
                                       symRoot.typeInt);
      SymTable lFortSymTable2 = symRoot.symTableRoot.pushSymTable(
        lFortSubp2);
      Param lIndex = sym.defineParam("index".intern(),
                                     symRoot.typeInt);
      lFortSubp2.addParam(lIndex);
      lFortSubp2.setVisibility(Sym.SYM_PUBLIC);
      lFortSubp2.closeSubpHeader();
      SubpDefinition lFortSubpDef2 = hir.subpDefinition(lFortSubp2,
        lFortSymTable2);
      ( (Program) hirRoot.programRoot).addSubpDefinition(
        lFortSubpDef2);
      BlockStmt lSubpBody3 = hir.blockStmt(null);
      RegionType lCommonTable = sym.regionType("table".intern(), Var.VAR_STATIC); //##43
      lCommonTable.addSubp(lFortSubp2, lFortSymTable2);
      Elem lElem01 = sym.defineElem("length".intern(),
                                    symRoot.typeInt);
      lCommonTable.addElemToCurrentRegion(lElem01);
      VectorType lIntVect100 = sym.vectorType(null, symRoot.typeInt,
                                              100L, 1L);
      Elem lElem02 = sym.defineElem("value".intern(), lIntVect100);
      lCommonTable.addElemToCurrentRegion(lElem02);
      lCommonTable.finishCurrentRegion();
      Var lRegionVar = lCommonTable.getRegionVar();
      Exp lQualExp01 = hir.qualifiedExp(hir.varNode(lRegionVar),
                                        hir.elemNode(lElem02));
      Exp lReturnValue2 = hir.subscriptedExp(lQualExp01,
                                             hir.varNode(lIndex));
      Stmt lReturnStmt02 = hir.returnStmt(lReturnValue2);
      lSubpBody3.addLastStmt(lReturnStmt02);
      lFortSubpDef2.setHirBody(lSubpBody3);
      lFortSymTable2.popSymTable();

      //##39 BEGIN
      //-- BLOCK DATA
      //--   common /table/length, value
      //--   integer length, value(100)
      //--   DATA length /50/
      //--   DATA value /0, 49*1/
      //-- end
      // See setDataStmt, expRepeat of HIR.java.
      Subp lFortSubp3 = sym.defineSubp("blockdata".intern(),
                                       symRoot.typeInt);
      SymTable lFortSymTable3 = symRoot.symTableRoot.pushSymTable(lFortSubp3);
      lFortSubp3.setVisibility(Sym.SYM_PUBLIC);
      lFortSubp3.closeSubpHeader();
      SubpDefinition lFortSubpDef3 = hir.subpDefinition(lFortSubp3,
        lFortSymTable2);
      ( (Program) hirRoot.programRoot).addSubpDefinition(lFortSubpDef3);
      BlockStmt lSubpBody4 = hir.blockStmt(null);
      RegionType lCommonTable2 = sym.regionType("table".intern(), Var.VAR_STATIC); //##43
      lCommonTable2.addSubp(lFortSubp2, lFortSymTable2);
      Elem lElem01_2 = sym.defineElem("length".intern(), symRoot.typeInt);
      lCommonTable.addElemToCurrentRegion(lElem01_2);
      VectorType lIntVect100_2 = sym.vectorType(null, symRoot.typeInt, 100L,
                                                1L);
      Elem lElem02_2 = sym.defineElem("value".intern(), lIntVect100_2);
      lCommonTable2.addElemToCurrentRegion(lElem02_2);
      lCommonTable2.finishCurrentRegion();
      Var lRegionVar2 = lCommonTable2.getRegionVar();
      lRegionVar2.setStorageClass(Var.VAR_STATIC);
      Exp lQualExp02 = hir.qualifiedExp(hir.varNode(lRegionVar2), //##43
                                        hir.elemNode(lElem01_2)); //##43
      Stmt lSetDataStmt1 = hir.setDataStmt(lQualExp02,            //##43
                                           hir.intConstNode(50)); //##39
      lFortSubpDef3.addInitiationStmt(lSetDataStmt1);
      java.util.List lList = new ArrayList();
      lList.add(hir.intConstNode(0));
      Exp lExpRepeat = hir.expRepeat(hir.intConstNode(1),
                                     hir.intConstNode(49));
      lList.add(lExpRepeat);
      Exp lQualExp03 = hir.qualifiedExp(hir.varNode(lRegionVar2), //##43
                                        hir.elemNode(lElem02_2)); //##43
      Stmt lSetDataStmt2 = hir.setDataStmt(lQualExp03,
                                           hir.expList(lList));   //##39
      lFortSubpDef3.addInitiationStmt(lSetDataStmt2);             //##43
      lFortSubpDef3.setHirBody(lSubpBody4);                       //##51
      lFortSymTable3.popSymTable();

      // HIR nodes should be numbered when entire HIR tree is created.
      ( (HIR) hirRoot.programRoot).setIndexNumberToAllNodes(1);

      if (ioRoot.dbgHir.getLevel() >= 1)
        hirRoot.programRoot.print(0);

      //---- Modify HIR ----//
      //-- Test replaceThisNode
      lVarNode4.replaceThisNode( (HIR) lAdd1.copyWithOperands());
      lAssign6.replaceThisNode( (HIR) lAssign7.copyWithOperands());
      //-- copyWithOperandsChangingLabels
      lFor2 = (ForStmt) lFor1.copyWithOperandsChangingLabels(null);
      lBlockStmt.addLastStmt(lFor2);
      if (ioRoot.dbgHir.getLevel() >= 1) {
        ioRoot.dbgHir.print(1, "HIR after modification ",
                            "replace call(,b); to call(,i+1); and  c[i]=100; to j=j+1;, and then copy for-loop");
        hirRoot.programRoot.print(0);
      }
      if (hir.isTree())                                          //##51
        ioRoot.dbgHir.print(1, "HIR-base of simpleMain",         //##51
                            "does not violate tree structure."); //##51

      /*---- HIR flow analysis ----*/
      // See README.Flow.en.txt.
      ( (HIR) hirRoot.programRoot).setIndexNumberToAllNodes(0);
      if (ioRoot.dbgFlow.getLevel() >= 2) {
        ioRoot.dbgFlow.print(2, "HIR", "before flow analysis");
        hirRoot.programRoot.print(0);
      }
      hirFlowRoot = new FlowRoot(hirRoot);
      FlowResults.putRegClasses(new RegisterFlowAnalClasses());
      FlowResults lResults = new FlowResults(hirFlowRoot);
      lSubpDefList = ( (Program) hirRoot.programRoot).
                     getSubpDefinitionList();
      for (lSubpDefIterator = lSubpDefList.iterator();
                              lSubpDefIterator.hasNext(); ) {
        lSubpDef = (SubpDefinition) (lSubpDefIterator.next());
        // It is necessary to set subprogram and its symbol table to be
        // analyzed/optimized in symRoot.
        symRoot.subpCurrent = lSubpDef.getSubpSym();
        symRoot.symTableCurrent = lSubpDef.getSymTable();
        symRoot.symTableCurrentSubp = symRoot.symTableCurrent;
        hirFlowRoot.subpUnderAnalysis = lSubpDef.getSubpSym();
        ioRoot.dbgFlow.print(1, "HIR Flow Analysis",
                             hirFlowRoot.subpUnderAnalysis.toString() +
                             "\n");
        lSubpDef.setIndexNumberToAllNodes( ( (SubpDefinitionImpl)
                                            lSubpDef).
                                          fNodeIndexMin);
        SubpFlow lSubpFlow = hirFlowRoot.aflow.subpFlow(lSubpDef,
          lResults);
        lResults.clearAll();
        lSubpFlow.controlFlowAnal();
        ShowFlow lShowFlow = new ShowFlow(lResults);
        lShowFlow.showControlFlow(lSubpFlow);
        lSubpFlow.initiateDataFlow();
        lShowFlow.showPDefUse(lSubpFlow); //##43 showDDfeUse failed.
      }

      /* print result */
      if (ioRoot.dbgHir.getLevel() >= 1) {
        ioRoot.dbgHir.print(1, "Main",
                            "HIR and SymTable before HirToLir conversion");
        hirRoot.programRoot.print(0);
        symRoot.symTable.printSymTableAllDetail(symRoot.symTableRoot);
        symRoot.symTableConst.printSymTableDetail();
      }

      /*---- Optimization & Parallelization in HIR ----*/

      /*---- HIR to LIR  Conversion ----*/
      CompileSpecification spec = ioRoot.getCompileSpecification();
      ConvToNewLIR newconvert = new ConvToNewLIR(ioRoot.getSourceFile(),
                                                 ioRoot.printOut, hirRoot);
      ImList sexp = newconvert.doConvert( (HIR) hirRoot.programRoot);
      newconvert.print(sexp);

      lLirAnalIsCoded = true;

      if (lLirAnalIsCoded) {
        /*---- LIR flow analysys ----*/
      }

      /*---- Optimization & Parallelization in LIR ----*/

      /*---- Code generation ----*/
      try {
        BackEnd backendSession
          = new BackEnd(spec, new PrintWriter(System.out, true));
        backendSession.doIt(sexp, ioRoot.objectFile);
      }
      catch (SyntaxErrorException e) {
        ioRoot.printOut.println(e.getMessage());
      }
      catch (IOException ioe) {
        ioRoot.printOut.println(ioe.getMessage());
      }

      // end LIR-phase

      ioRoot.dbgSym.print(1, "SymTable ", "final state");
      symRoot.symTable.printSymTableAllDetail(symRoot.symTableRoot);
      symRoot.symTable.printSymTableAllDetail(symRoot.symTableConst);
      System.exit(0);
    }
    catch (FatalError fatalError) {
      System.err.print(fatalError.getMessage());
      fatalError.printStackTrace(); //##8
      System.err.print("Error exit 2 \n");
      System.exit(2);
    }
    catch (ParseException e) {
      System.err.println(e.getMessage());
      // System.exit(CompileStatus.ABNORMAL_END);
    }
    catch (RuntimeException runtimeException) {
      System.err.print(runtimeException.getMessage());
      runtimeException.printStackTrace(); //##8
      System.err.print("Error exit 1 \n");
      System.exit(1);
    }
  } // main

} // SimpleMain class
