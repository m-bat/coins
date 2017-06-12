/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import java.util.Iterator;
import java.util.List;

import coins.FlowRoot;
import coins.HirRoot;
import coins.IoRoot;
import coins.SymRoot;
import coins.flow.BBlock; //##63
import coins.flow.BBlockNodeIterator; //##63
import coins.flow.BBlockSubtreeIterator; //##63
//##63 import coins.aflow.FlowResults;
//##63 import coins.aflow.RegisterFlowAnalClasses;
import coins.flow.SubpFlow; //##63
import coins.alias.AliasAnalHir1;
import coins.alias.RecordAlias;
import coins.sym.CharConst;
import coins.sym.IntConst;
import coins.sym.Subp;
import coins.sym.Sym;
import coins.sym.Var;

/** TestHir class
<PRE>
 * Test HIR
 * User may tentatively add a method call in order to test some HIR
 * expression or nullify (by changing to comment) some method calls in
 * the TestHir constructor.
 * addStatements: add some statements to the tail of main().
 * testAdjustableDim: test adjustable dimension.
 * testBlockStmt: test getBlockStmt().
 * testVisitor: test HirVisitorModel1 and HirVisitorModel2.
 * testSymTable: test symbol table.
 *               You may tentatively add some other statements.
 * testBBlockIterator: testBBlockSubtreeIterator and test BBlockNodeIterator.
 * testClone: test copyWithOperands.
 * testHirComponents: get some information for each kind of HIR expressions
 *               by using HirIterator. You may tentatively add other statements.
 * testHirModify: make a copy of block and add it at the tail of subprogram.
 * testInf: test to add information to InfStmt.
 * testNodeIterator: test HIR iterator by using next(), getNextStmt(),
 *               getNexeExecutableNode().
 * testReplace: test to replace assignment statement to generated
 *               assignment statement.
</PRE>
**/
public class
TestHir
{

//------ Public fields ------//

  public final IoRoot     // Used to access Root information.
    ioRoot;

  public final SymRoot    // Used to access Root information.
    symRoot;

  public final HirRoot    // Used to access Root information.
    hirRoot;

  public final FlowRoot    // Used to access Root information.
    flowRoot;

  protected HIR
     hir;

  protected Sym
     sym;

//====== Constructors ======//

/** TestHir
 * Test HIR and HIR related methods.
 * Invoked when testHir option is given.
 */
public
TestHir( HirRoot pHirRoot, FlowRoot pFlowRoot )
{
  ioRoot   = pHirRoot.ioRoot;
  symRoot  = pHirRoot.symRoot;
  hirRoot  = pHirRoot;
  flowRoot = pFlowRoot;
  System.out.println("\nTestHir (invoked by coins option testHir)");
  sym = symRoot.sym;
  hir = hirRoot.hir;
  // testSymTable();
  AliasAnalHir1 lAliasAnal = new AliasAnalHir1(true, hirRoot);
  if (ioRoot.dbgHir.getLevel() >= 2){
    // testNodeIterator();
    // testBBlockIterator();
  }
  if (ioRoot.dbgHir.getLevel() >= 1) {
    coins.ir.IrList lSubpDefList
      = ((Program)hirRoot.programRoot).getSubpDefinitionList();
    Iterator lSubpDefIterator = lSubpDefList.iterator();
    while (lSubpDefIterator.hasNext()) {
      SubpDefinition lSubpDef = (SubpDefinition)(lSubpDefIterator.next());
      Subp lSubp = lSubpDef.getSubpSym();
      System.out.println("\nTestHir " + lSubp.toString());
      RecordAlias lRecordAlias = new RecordAlias(lAliasAnal, lSubpDef,
        pFlowRoot.fSubpFlow); //##62
      // testgetBlockStmt(lSubpDef);
        if (ioRoot.dbgHir.getLevel() >= 4) {
          // testInf(lSubpDef);
          // testVisitor(lSubpDef);
        }
        if (ioRoot.dbgHir.getLevel() >= 6) {
          // HIR lClone = testClone(lSubpDef.getHirBody());
          // testHirModify(lSubpDef.getHirBody());
          // testReplace(lClone);
        } //
      if (ioRoot.dbgHir.getLevel() >= 2) {
        // testHirComponents(lSubpDef.getHirBody()); //##040127
        // System.out.println("\nToStringWithChildren");  //###
        // System.out.println(lSubpDef.getHirBody().toStringWithChildren()); //###
      }
      if (lSubp.getName() == "main") {
        // addStatements(lSubpDef);
      }
    }
    if (ioRoot.dbgHir.getLevel() >= 2) {
      TestHir2 lTestHir2 = new TestHir2(hirRoot);
      // lTestHir2.visit((HIR)hirRoot.programRoot);
    }
  }
} // TestHir

void testSymTable()
{
  CharConst lChar9 = symRoot.sym.charConst('9', symRoot.typeChar);
  IntConst lInt9 = symRoot.sym.intConst(9, symRoot.typeInt);
  IntConst lInt9c = symRoot.sym.intConst('9', symRoot.typeInt);
  CharConst lCharA = symRoot.sym.charConst('A', symRoot.typeChar);
  IntConst lIntA = symRoot.sym.intConst((long)'A', symRoot.typeInt);
  System.out.println("charConst('A') " + lCharA.toStringDetail()
                      + " intConst((long)'A') " + lIntA.toStringDetail());
} // testSymTable

void testAdjustableDim()
 {

 } // testAdjustableDim

void
testgetBlockStmt( SubpDefinition pSubpDef )
{
  HIR lHirBody = pSubpDef.getSubpSym().getHirBody();
  HIR lNode;
  Stmt lStmt;
  for (HirIterator lHirIterator = lHirBody.hirIterator(lHirBody);
     lHirIterator.hasNext(); ) {
    lNode = lHirIterator.next();
    ioRoot.printOut.print("\nHir " + ioRoot.toStringObject(lNode));
    if (lNode instanceof Stmt)
    ioRoot.printOut.print(" getBlockStmt " +
      ioRoot.toStringObject(((Stmt)lNode).getBlockStmt()));
  }
} // testParentLink

private void
testHirModify( HIR pSubpBody )
{
  HIR lHir1;
  BlockStmt lBlock, lBlock1, lBlock2;
  Stmt      lStmt, lStmt2;
  ioRoot.dbgHir.print( 3, "testHirModify subpBody", pSubpBody.toString());
  if (ioRoot.dbgHir.getLevel() >= 3) {
    if (pSubpBody.getOperator() == HIR.OP_LABELED_STMT) {
      lStmt = ((LabeledStmt)pSubpBody).getStmt();
      if (lStmt.getOperator() == HIR.OP_BLOCK) {
        lBlock1 = (BlockStmt)(lStmt.copyWithOperands());
        ioRoot.dbgHir.println( 3);
        ioRoot.dbgHir.print( 3, "CopyWithOperands result", lStmt.toStringShort());
        lBlock1.print(0);
        lBlock2 = (BlockStmt)(lStmt.copyWithOperandsChangingLabels(null));
        ioRoot.dbgHir.println( 3);
        ioRoot.dbgHir.print( 3, "CopyWithOperandsChangingLabel result",
              lStmt.toStringShort());
        lStmt2 = hirRoot.hir.labeledStmt(
             symRoot.symTableCurrent.generateLabel(), lBlock2);
        lStmt2.print(0);
        ((BlockStmt)((LabeledStmt)pSubpBody).getStmt()).addLastStmt(lStmt2);
      }
    }
  }
} // testHirModify

private void
testHirComponents( HIR pSubpBody )
{
  HIR lNode;
  ioRoot.printOut.println("\ntestHirComponents ");
  for (HirIterator lHirIterator = pSubpBody.hirIterator(pSubpBody);
   lHirIterator.hasNext(); ) {
     lNode = lHirIterator.next();
     if (lNode instanceof SubscriptedExp) {
       Exp lSubscriptExp = ((SubscriptedExp)lNode).getSubscriptExp();
       Exp lElemSizeExp  = ((SubscriptedExp)lNode).getElemSizeExp();
       ioRoot.printOut.println(" SubscriptedExp " + lNode.toString()
                            + " SubscriptExp " + lSubscriptExp.toString()
                            + " ElemSizeExp " + lElemSizeExp.toString() );

    }else if (lNode instanceof LoopStmt) {
      ioRoot.printOut.println(" isSimpleWhileLoop " + lNode.toString()
                           + " " + ((LoopStmt)lNode).isSimpleWhileLoop() );
     ioRoot.printOut.println(" isSimpleForLoop " + lNode.toString()
                        + " " + ((LoopStmt)lNode).isSimpleForLoop() );
     ioRoot.printOut.println(" isSimpleRepeatLoop " + lNode.toString()
                     + " " + ((LoopStmt)lNode).isSimpleRepeatLoop() ); //##55
    }else if ((lNode instanceof ConstNode)&&
              (lNode.getType() == symRoot.typeBool)) {
      ioRoot.printOut.println(" BoolConst " + lNode.toString()
         + " getSym " + ((ConstNode)lNode).getSym().toString()
         + " getConstSym " + ((ConstNode)lNode).getConstSym().toString());
      }
  }
}// testHirComponents

private HIR
testClone( HIR pHir )
{
  HIR lHir = null;
  ioRoot.dbgHir.print(2, "testClone",pHir.toString());
  if (ioRoot.dbgHir.getLevel() >= 2) {
    lHir = ((HIR)pHir).copyWithOperands();
  }
  lHir.print(1, true);
  return lHir;
} // testClone

private void
testReplace( HIR pHir )
{
  HIR lNode, lParent;
  Stmt lStmt, lStmt2, lStmt3;
  BlockStmt lBlock;
  ioRoot.dbgHir.print(2, "replaceTest", pHir.toString());
  System.out.println("Before replace");
  pHir.print(1, true);
  Var lNewVar = symRoot.sym.defineVar("REPLVAR", symRoot.typeInt);
  lNewVar.setFlag(Sym.FLAG_GENERATED_SYM, true);
  Var lNewVar2 = symRoot.sym.defineVar("REPLVAR2", symRoot.typeInt);
  lNewVar2.setFlag(Sym.FLAG_GENERATED_SYM, true);
  for (HirIterator lHirIterator = hirRoot.hir.hirIterator(pHir);
       lHirIterator.hasNext(); ) {
    lNode = lHirIterator.next();
    if (lNode != null)
      lParent = (HIR)lNode.getParent();
    else
      lParent = null;
//     if ((lNode instanceof VarNode)&&
//         (!((VarNode)lNode).getSymNodeSym().getFlag(Sym.FLAG_GENERATED_SYM))) {
//       lNode.replaceThisNode(hirRoot.hir.varNode(lNewVar));
//    }
  }
  System.out.println("\nAfter replacing VarNode");
  pHir.print(1, true);
  int lCount = 0;
  for (HirIterator lHirIterator = hirRoot.hir.hirIterator(pHir);
       lHirIterator.hasNextStmt(); ) { //##60
    lStmt = lHirIterator.nextStmt(); //##60
    if (lStmt != null)
      lParent = (HIR) lStmt.getParent();
    else
      lParent = null;
    if (lStmt instanceof BlockStmt) {
      for (lStmt2 = ((BlockStmt)lStmt).getFirstStmt(); lStmt2 != null;
           lStmt2 = lStmt2.getNextStmt()) {
        if (lStmt2 instanceof AssignStmt) {
          lStmt3 = hirRoot.hir.assignStmt(
              hirRoot.hir.varNode(lNewVar2),
              hirRoot.hir.constNode(symRoot.sym.intConst(0, symRoot.typeInt)));
          lStmt2.replaceThisStmtWith(lStmt3);
          lCount++;
          lStmt2 = lStmt3;
        }
      }
    }
    if (lCount > 0) {
      break;
    }
  }
  System.out.println("\nAfter replacing AssignStmt");
  pHir.print(1, true);
} // testReplace

private void
testNodeIterator()
{
  //---- Test iterators ----//
  /* //##63
  SubpFlow           lSubpFlow;
  BBlockSubtreeIterator lStmtIterator;
  BBlockNodeIterator lNodeIterator;
  coins.ir.hir.HirIterator  lHirIterator, lHirIterator2;
  BBlock             lBBlock;
  HIR                lStmt;
  HIR                lNode;
  Sym                lSym;
  if (ioRoot.dbgHir.getLevel() >= 5) {
    ioRoot.printOut.print("\nHirIterator applied to programRoot");
      for (lHirIterator = hirRoot.hir.hirIterator(hirRoot.programRoot);
           lHirIterator.hasNext(); ) {
        lNode = lHirIterator.next();
        ioRoot.printOut.print("\nHir " +
            ioRoot.toStringObject(lNode));
      }
  }
  if (ioRoot.dbgHir.getLevel() >= 2) {
    ioRoot.printOut.print("\nHirIterator getNextStmt");
    for (lHirIterator = hirRoot.hir.hirIterator(
      symRoot.subpCurrent.getHirBody());
      lHirIterator.hasNextStmt(); ) { //##60
      lStmt = lHirIterator.nextStmt(); //##60
      ioRoot.printOut.print("\n Stmt " + ioRoot.toStringObject(lStmt));
    }
  }
  if (ioRoot.dbgHir.getLevel() >= 4) {
    ioRoot.printOut.print("\n\nHirIterator " + symRoot.subpCurrent);
    for (lHirIterator = hirRoot.hir.hirIterator(
       symRoot.subpCurrent.getHirBody());
         lHirIterator.hasNext(); ) {
      lNode = lHirIterator.next();
      ioRoot.printOut.print("\nHir " +
          ioRoot.toStringObject(lNode));
    }
    ioRoot.printOut.print("\n\nHirIterator getNextExecutableNode");
    for (lHirIterator = hirRoot.hir.hirIterator(
          symRoot.subpCurrent.getHirBody());
         lHirIterator.hasNext(); ) {
      lNode = lHirIterator.getNextExecutableNode();
      ioRoot.printOut.print("\nHir " +
         ioRoot.toStringObject(lNode));
    }
  }
      */ //##63
} // testNodeIterator

private void
testBBlockIterator()
{
     /* //##63
  ioRoot.dbgHir.print( 2, "\ntestBBlockIterator", " ");
  FlowRoot flowRoot = new FlowRoot(hirRoot);
  flowRoot.setHirAnalysis();
  FlowResults.putRegClasses(new RegisterFlowAnalClasses());
  coins.ir.IrList lSubpDefList
    = ((Program)hirRoot.programRoot).getSubpDefinitionList();
  Iterator lSubpDefIterator = lSubpDefList.iterator();
  while (lSubpDefIterator.hasNext()) {
    SubpDefinition lSubpDef = (SubpDefinition)(lSubpDefIterator.next());
    System.out.println("\nTestBBlock of " + lSubpDef.getSubpSym().toString());
    FlowResults lResults = new FlowResults(flowRoot);
    SubpFlow lSubpFlow = flowRoot.aflow.subpFlow(lSubpDef, lResults);
    List lBBlockList = lSubpFlow.controlFlowAnal();
    System.out.println("  Number of BBlocks " + lSubpFlow.getNumberOfBBlocks());
    for (int lBlockNo = 1; lBlockNo <= lSubpFlow.getNumberOfBBlocks();
       lBlockNo++) {
      BBlock lBBlock = lSubpFlow.getBBlock(lBlockNo);
      ioRoot.dbgHir.print( 2, "BBlockSubtreeIterator", "No: " + lBlockNo +
             " HirLink " + ((HIR)lBBlock.getIrLink()).toStringShort());
      for (BBlockSubtreeIterator lStmtIterator = lBBlock.bblockSubtreeIterator();
           lStmtIterator.hasNext(); ) {
        HIR lStmt = (HIR)(lStmtIterator.next());
        ioRoot.dbgHir.print( 2, " Subtree",  ioRoot.toStringObject(lStmt));
        if (lStmt != null)
          ioRoot.dbgHir.print( 4, " Parent " +  ioRoot.toStringObject(lStmt.getParent()));
      }
      if (ioRoot.dbgHir.getLevel() >= 4) {
        ioRoot.dbgHir.print( 3, " BBlockNodeIterator", "for B" + lBlockNo);
        for (BBlockNodeIterator lNodeIterator = lBBlock.bblockNodeIterator();
             lNodeIterator.hasNext(); ) {
          HIR lNode = (HIR)lNodeIterator.next();
          ioRoot.printOut.print("\n  Node " +  ioRoot.toStringObject(lNode));
        }
      }
    }
  }
      */ //##63
} // testBBlockIterator

private void
testInf( SubpDefinition pSubpDef)
{
  SubpFlow           lSubpFlow;
  BBlockSubtreeIterator lStmtIterator;
  BBlockNodeIterator lNodeIterator;
  coins.ir.hir.HirIterator  lHirIterator, lHirIterator2;
  BBlock             lBBlock;
  HIR                lStmt;      //## Tan
  HIR                lNode;
  Sym                lSym;
  if (ioRoot.dbgHir.getLevel() >= 4) {
    ioRoot.printOut.println("\ntestInf. Add index as inf for InfStmt");
    for (lHirIterator = hirRoot.hir.hirIterator(
       pSubpDef.getHirBody());
         lHirIterator.hasNext(); ) {
      lNode = lHirIterator.next();
      if (lNode instanceof InfStmt) {
        lNode.addInf(coins.Registry.INF_KIND_IR, hirRoot.hir.intConstNode(lNode.getIndex()));
      }else if (lNode instanceof AssignStmt) {
//        lNode.addInf(coins.Registry.INF_KIND_IR, "AssignStmt");
//        lNode.addInf(coins.Registry.INF_KIND_PRAGMA, hirRoot.hir.intConstNode(lNode.getIndex()));
      }
    }
    ioRoot.printOut.print("\n Print inf\n ");
    for (lHirIterator = hirRoot.hir.hirIterator(
         pSubpDef.getHirBody());
         lHirIterator.hasNext(); ) {
      lNode = lHirIterator.next();
      if ((lNode != null)&&(lNode.getInfList() != null))
        ioRoot.printOut.print("\n " + lNode.toStringDetail());
    }
  }
} // testInf

void
testVisitor( SubpDefinition pSubpDef )
{
  Subp lSubpSym = pSubpDef.getSubpSym();
  ioRoot.printOut.print("\ntest Stmt visitor (HirVisitorModel1)\n" + lSubpSym.getName());
  PrintStmtVisitor printStmtVisitor = new PrintStmtVisitor(hirRoot);
  printStmtVisitor.visit((HIR)hirRoot.programRoot);
  ioRoot.printOut.print("\ntest Node visitor (HirVisitorModel2)\n" + lSubpSym.getName());
  PrintVisitor printVisitor = new PrintVisitor(hirRoot);
  // printVisitor.visit(lSubpSym.getHirBody());
  printVisitor.visit((HIR)hirRoot.programRoot);
} // testVisitor

void
addStatements( SubpDefinition pSubpDef )
{
  ioRoot.printOut.print("\naddStatements " + pSubpDef.getSubpSym().getName() + "\n");
  HIR lSubpBody = pSubpDef.getHirBody();
  BlockStmt lSubpBodyBlock = null;
  if (lSubpBody instanceof BlockStmt)
    lSubpBodyBlock = (BlockStmt)lSubpBody;
  else if ((lSubpBody instanceof LabeledStmt)&&
           (((LabeledStmt)lSubpBody).getStmt() instanceof BlockStmt))
    lSubpBodyBlock = (BlockStmt)((LabeledStmt)lSubpBody).getStmt();
  if (lSubpBodyBlock != null) {
    Stmt lStmt = lSubpBodyBlock.getLastStmt();
    ReturnStmt lReturnStmt; // Statements are added before the return statement.
    if (lStmt instanceof ReturnStmt)
      lReturnStmt = (ReturnStmt)lStmt;
    else {
      lReturnStmt = hirRoot.hir.returnStmt();
      lSubpBodyBlock.addLastStmt(lReturnStmt);
    }

    Var lBoolVar = symRoot.symTableCurrent.generateVar(symRoot.typeBool);
    Stmt lBoolAssign = hirRoot.hir.assignStmt(hir.varNode(lBoolVar),
                                      hir.constNode(sym.boolConst(true)));
    lReturnStmt.insertPreviousStmt(lBoolAssign);

  }
} // addStatements

Exp
reorderOperands( Exp pExp )
{
  if ((pExp == null )||
      (pExp.getChildCount() != 2))
    return pExp;
  Exp lChild1 = (Exp)pExp.getChild1();
  Exp lChild2 = (Exp)pExp.getChild2();
  Exp lNewChild1 = reorderOperands(lChild1);
  Exp lNewChild2 = reorderOperands(lChild2);
  if (lNewChild2.isEvaluable()&&
      (! lNewChild1.isEvaluable())&&
      isCommutative(pExp)) {
    Exp lExp = hirRoot.hir.exp(pExp.getOperator(),
             lNewChild2, lNewChild1);
    return lExp;
  }
  if ((lChild1 != lNewChild1)||
      (lChild2 != lNewChild2)) {
    Exp lExp = hirRoot.hir.exp(pExp.getOperator(),
               lNewChild1, lNewChild2);
    return lExp;
  }
  return pExp;
} // reorderOperands

boolean
isCommutative( Exp pExp )
{
  if (pExp == null)
    return false;
  switch (pExp.getOperator()) {
  case HIR.OP_ADD:
  case HIR.OP_MULT:
  case HIR.OP_AND:
  case HIR.OP_OR:
  case HIR.OP_XOR:
    return true;
  default:
    return false;
  }
} // isInterchangeable

} // TestHir class
