/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.flow;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import coins.FlowRoot;
import coins.IoRoot;
import coins.ir.IR;
import coins.ir.hir.LabeledStmt; //##73

public class ShowControlFlow
{

  public final FlowRoot
    flowRoot; // Used to access Root inflomation.

  public final IoRoot
    ioRoot; // Used to access Root inflomation.

  // private  HirSubpFlow  fFlow;
  private SubpFlow fFlow;
  public ControlFlowImpl fcFlow;
  /**
   *
   * ShowControlFlow
   *
   **/
  // public  ShowControlFlow(HirSubpFlow pFlow,ControlFlowImpl pcFlow)
  public ShowControlFlow(SubpFlow pFlow, ControlFlow pcFlow) //##60
  {
    flowRoot = ((ControlFlowImpl)pcFlow).flowRoot;
    ioRoot = ((ControlFlowImpl)pcFlow).ioRoot;
    fFlow = pFlow;
    fcFlow = ((ControlFlowImpl)pcFlow);
  }

  /**
   * showAll
   *
   **/
  public void showAll()
  {
    ioRoot.printOut.print("\n*[START]******** ControlFlowGraph *******\n");
    showBasicBlock();
    showDominator();
    showPostDominator(); //##70
    //showStrictlyDominator();
    showImmediatelyDominate();
    showDominatedChildren();
    //showPostDominator();
    //showPostStrictlyDominator();
    showImmediatelyPostDominate();
    showPostDominatedChildren();
    ioRoot.printOut.print("\n*[END]********** ControlFlowGraph *******\n");
  }

  /**
   *  showBasicBlock
   *  Show basic blocks with predecessors ans successors.
   *
   **/
  public void showBasicBlock()
  {
    BBlock b;
    BBlock edge;
    IR s; //## Tan
    List l;
    ListIterator Ie;
    Iterator Ib;
    BBlockSubtreeIterator Is;
    int maxBBlockNo;
    int i;
    boolean first;

    maxBBlockNo = fFlow.getNumberOfBBlocks();

    ioRoot.printOut.print("=====[Basic Block]=====\n");
    for (i = 1; i <= maxBBlockNo; i++) {

      //  Print Basic Block

      b = fFlow.getBBlock(i);
      ioRoot.printOut.print("BlockNO =");
      ioRoot.printOut.print(b.getBlockNumber()+
        " " + b.getIrLink().toStringShort() +
        " " + b.getLabel().getName() +
        " " + ((LabeledStmt)b.getIrLink()).getStmt()+"\n");
      if (b.isEntryBlock() == true) {
        ioRoot.printOut.print("(ENTRY BLOCK)\n");
      }
      if (b.isExitBlock() == true) {
        ioRoot.printOut.print("(EXIT BLOCK)\n");
      }

      //  Print Succ List

      ioRoot.printOut.print("	 ==Succ List==>(");
      l = b.getSuccList();

      first = true;
      for (Ie = l.listIterator(); Ie.hasNext(); ) {
        edge = (BBlock)Ie.next();
        if (first == true)
          first = false;
        else
          ioRoot.printOut.print(",");
        ioRoot.printOut.print(edge.getBlockNumber());
      }
      ioRoot.printOut.print(")\n");

      //  Print Pred List

      ioRoot.printOut.print("	 ==Pred List==>(");
      l = b.getPredList();
      first = true;
      for (Ie = l.listIterator(); Ie.hasNext(); ) {
        edge = (BBlock)Ie.next();
        if (first == true)
          first = false;
        else
          ioRoot.printOut.print(",");
        ioRoot.printOut.print(edge.getBlockNumber());
      }
      ioRoot.printOut.print(")\n");

      //  Print Statment List

      ioRoot.printOut.print("	 ==Stmt List==\n");
      for (Is = b.bblockSubtreeIterator(); Is.hasNext(); ) {
        s = (IR)Is.next(); //## Tan
        ioRoot.printOut.print("	 ");
        ioRoot.printOut.print(s + "\n");
        //ioRoot.printOut.println("Node (Def/Use)");
        //BBlockDefUse bdef;
        //bdef = new BBlockDefUse(s);
      }
    }
  }

  /**
   *  showDominator
   *
   **/
  public void showDominator()
  {
    BBlock b;
    int maxBBlockNo;
    int lookUp;
    int i;
    int j;
    boolean first;
    ioRoot.printOut.print("=====[Dominator]=====\n");
    maxBBlockNo = fFlow.getNumberOfBBlocks();
    for (i = 1; i <= maxBBlockNo; i++) {
      b = fFlow.getBBlock(i);
      ioRoot.printOut.print("BlockNO =");
      ioRoot.printOut.print(b.getBlockNumber());
      ioRoot.printOut.print(" (");

      //  Print Dominator List

      lookUp = fcFlow.domLookUp(b.getBlockNumber());
      first = true;
      if (fcFlow.fDom[lookUp] != null) { //##60
        for (j = 1; j <= maxBBlockNo; j++) {
          if (fcFlow.fDom[lookUp].getBit(j) == 1) {
            if (first == true)
              first = false;
            else
              ioRoot.printOut.print(",");
            ioRoot.printOut.print(fcFlow.domBitLookUp(j));
          }
        }
      } //##60
      ioRoot.printOut.print(")\n");
    }
  }

  /**
   *
   *  showImmediatelyDominate
   *
   **/
  public void showImmediatelyDominate()
  {
    BBlock b;
    BBlock Idom;
    int maxBBlockNo;
    int lookUp;
    int i;
    int j;
    ioRoot.printOut.print("=====[ImmediateDominator]=====\n");
    maxBBlockNo = fFlow.getNumberOfBBlocks();
    for (i = 1; i <= maxBBlockNo; i++) {
      b = fFlow.getBBlock(i);
      ioRoot.printOut.print("BlockNO =");
      ioRoot.printOut.print(b.getBlockNumber()+ "\n");
      Idom = b.getImmediateDominator();

      //  Print Dominator

      ioRoot.printOut.print("	ImmediateDominator=");
      if (Idom == null)
        ioRoot.printOut.print("NULL\n");
      else
        ioRoot.printOut.print(Idom.getBlockNumber()+"\n");
    }
  }

  /**
   *
   *  showDominatedChildren
   *
   **/
  public void showDominatedChildren()
  {
    BBlock b;
    BBlock child;
    int maxBBlockNo;
    int i;
    List l;
    ListIterator Ie;
    boolean first;
    ioRoot.printOut.print("=====[DominatedChildren]=====\n");
    maxBBlockNo = fFlow.getNumberOfBBlocks();
    for (i = 1; i <= maxBBlockNo; i++) {
      b = fFlow.getBBlock(i);
      ioRoot.printOut.print("BlockNO =");
      ioRoot.printOut.print(b.getBlockNumber()+"\n");
      l = b.getDominatedChildren();
      ioRoot.printOut.print("	 Child  List==>(");
      first = true;
      for (Ie = l.listIterator(); Ie.hasNext(); ) {
        child = (BBlock)Ie.next();
        if (first == true)
          first = false;
        else
          ioRoot.printOut.print(",");
        ioRoot.printOut.print(child.getBlockNumber());
      }
      ioRoot.printOut.print(")\n");
    }
  }

  /**
   *
   *  showPostDominatedChildren
   *
   **/
  public void showPostDominatedChildren()
  {
    BBlock b;
    BBlock child;
    int maxBBlockNo;
    int i;
    List l;
    ListIterator Ie;
    boolean first;
    ioRoot.printOut.print("=====[PostDominatedChildren]=====\n");
    maxBBlockNo = fFlow.getNumberOfBBlocks();
    for (i = 1; i <= maxBBlockNo; i++) {
      b = fFlow.getBBlock(i);
      ioRoot.printOut.print("BlockNO =");
      ioRoot.printOut.print(b.getBlockNumber()+"\n");
      l = b.getPostDominatedChildren();
      ioRoot.printOut.print("	 Child  List==>(");
      first = true;
      for (Ie = l.listIterator(); Ie.hasNext(); ) {
        child = (BBlock)Ie.next();
        if (first == true)
          first = false;
        else
          ioRoot.printOut.print(",");
        ioRoot.printOut.print(child.getBlockNumber());
      }
      ioRoot.printOut.print(")\n");
    }
  }

  /**
   *  showImmediatelyPostDominate
   *
   **/
  public void showImmediatelyPostDominate()
  {
    BBlock b;
    BBlock Idom;
    int maxBBlockNo;
    int lookUp;
    int i;
    int j;
    ioRoot.printOut.print("=====[ImmediatePostDominator]=====\n");
    maxBBlockNo = fFlow.getNumberOfBBlocks();
    for (i = 1; i <= maxBBlockNo; i++) {
      b = fFlow.getBBlock(i);
      ioRoot.printOut.print("BlockNO =");
      ioRoot.printOut.print(b.getBlockNumber()+"\n");
      Idom = b.getImmediatePostDominator();

      //  Print Dominator

      ioRoot.printOut.print("	ImmediatePostDominator=");
      if (Idom == null)
        ioRoot.printOut.print("NULL\n");
      else
        ioRoot.printOut.print(Idom.getBlockNumber()+"\n");
    }
  }

  /**
   *  showPostDominator
   **/
  private void showPostDominator()
  {
    BBlock b;
    int maxBBlockNo;
    int lookUp;
    int i;
    int j;
    boolean first;

    ioRoot.printOut.print("=====[Post Dominator]=====\n");
    maxBBlockNo = fFlow.getNumberOfBBlocks();
    for (i = 1; i <= maxBBlockNo; i++) {
      b = fFlow.getBBlock(i);
      ioRoot.printOut.print("BlockNO =");
      ioRoot.printOut.print(b.getBlockNumber());
      ioRoot.printOut.print(" (");

      //  Print Post Dominator List

      lookUp = fcFlow.domLookUp(b.getBlockNumber());
      first = true;
      for (j = 1; j <= maxBBlockNo; j++) {
        if (fcFlow.fPostDom[lookUp].getBit(j) == 1) {
          if (first == true)
            first = false;
          else
            ioRoot.printOut.print(",");
          ioRoot.printOut.print(fcFlow.domBitLookUp(j));
        }
      }
      ioRoot.printOut.print(")\n");
    }
  }

  /**
   * showStrictlyDominator
   *
   **/
  private void showStrictlyDominator()
  {
    BBlock b;
    int maxBBlockNo;
    int lookUp;
    int i;
    int j;
    boolean first;

    ioRoot.printOut.print("=====[Strictly Dominator]=====\n");
    maxBBlockNo = fFlow.getNumberOfBBlocks();
    for (i = 1; i <= maxBBlockNo; i++) {
      b = fFlow.getBBlock(i);
      ioRoot.printOut.print("BlockNO =");
      ioRoot.printOut.print(b.getBlockNumber());
      ioRoot.printOut.print(" (");

      //  Print Strictly Dominator List

      lookUp = fcFlow.domLookUp(b.getBlockNumber());
      first = true;
      for (j = 1; j <= maxBBlockNo; j++) {
        if (fcFlow.fsDom[lookUp].getBit(j) == 1) {
          if (first == true)
            first = false;
          else
            ioRoot.printOut.print(",");
          ioRoot.printOut.print(fcFlow.domBitLookUp(j));
        }
      }
      ioRoot.printOut.print(")\n");
    }
  }

  /**
   *  showPostStrictlyDominator
   **/
  public void showPostStrictlyDominator()
  {
    BBlock b;
    int maxBBlockNo;
    int lookUp;
    int i;
    int j;
    boolean first;
    ioRoot.printOut.print("=====[PostStrictly Dominator]=====\n");

    maxBBlockNo = fFlow.getNumberOfBBlocks();
    for (i = 1; i <= maxBBlockNo; i++) {
      b = fFlow.getBBlock(i);
      ioRoot.printOut.print("BlockNO =");
      ioRoot.printOut.print(b.getBlockNumber());
      ioRoot.printOut.print(" (");

      //  Print Strictly Dominator List

      lookUp = fcFlow.domLookUp(b.getBlockNumber());
      first = true;
      for (j = 1; j <= maxBBlockNo; j++) {
        if (fcFlow.fPostsDom[lookUp].getBit(j) == 1) {
          if (first == true)
            first = false;
          else
            ioRoot.printOut.print(",");
          ioRoot.printOut.print(fcFlow.domBitLookUp(j));
        }
      }
      ioRoot.printOut.print(")\n");
    }
  }
}
