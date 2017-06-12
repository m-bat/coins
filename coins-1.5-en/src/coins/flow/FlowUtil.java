/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * FlowUtil.java
 *
 * Created on June 13, 2002, 2:18 PM
 */
//##60 package coins.aflow;
package coins.flow; //##60

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set; //##65


//##60 import coins.aflow.util.FAList;
//##60 import coins.aflow.util.FlowError;
//##60 import coins.aflow.util.UnimplementedMethodException;
import coins.ir.IR;
import coins.ir.hir.AssignStmt; //##53
import coins.ir.hir.BlockStmt;
import coins.ir.hir.ConstNode;
import coins.ir.hir.FunctionExp;
import coins.ir.hir.HIR;
import coins.ir.hir.HIR_Impl;
import coins.ir.hir.HirIterator;
import coins.ir.hir.HirList;
import coins.ir.hir.Stmt;
import coins.ir.hir.SubscriptedExp;
//import coins.ir.lir.Call; // 2004.05.31 S.Noishi
//import coins.ir.lir.Condition; // 2004.05.31 S.Noishi
//import coins.ir.lir.Jump; // 2004.05.31 S.Noishi
//import coins.ir.lir.LConst; // 2004.05.31 S.Noishi
//import coins.ir.lir.LIRNode; // 2004.05.31 S.Noishi
//import coins.ir.lir.LIRNodeIterator; // 2004.05.31 S.Noishi
//import coins.ir.lir.LIRType; // 2004.05.31 S.Noishi
//import coins.ir.lir.LLabel; // 2004.05.31 S.Noishi
//import coins.ir.lir.Ltype; // 2004.05.31 S.Noishi
//import coins.ir.lir.Memory; // 2004.05.31 S.Noishi
//import coins.ir.lir.Operator; // 2004.05.31 S.Noishi
//import coins.ir.lir.RegSymbolic; // 2004.05.31 S.Noishi
//import coins.ir.lir.Register; // 2004.05.31 S.Noishi
//import coins.ir.lir.Symbol; // 2004.05.31 S.Noishi
//import coins.ir.lir.Symbolic; // 2004.05.31 S.Noishi
//import coins.ir.lir.Variable; // 2004.05.31 S.Noishi
import coins.sym.FlowAnalSym;
import coins.sym.Label;
//##41 import coins.sym.Reg;
import coins.sym.StructType; //##63
import coins.sym.Sym;
import coins.sym.Type;
import coins.sym.Var;

/**
 * Collection of utility methods. Many of these methods simply wrap the difference in interface/behavior of HIR and LIR.
 * @author  hasegawa
 */
public class FlowUtil
{
  /**
   * Returns the first child.
   */
  public static IR getChild1(IR pIR)
  {
    if (pIR instanceof HIR) {
      return pIR.getChild1();
    }
    else {
      throw new FlowError(); // 2004.06.01 S.Noishi
      // return ((LIRNode) pIR).getLeftChild();
    }
  }

  /**
   * Returns the second child.
   */
  public static IR getChild2(IR pIR)
  {
    if (pIR instanceof HIR) {
      return pIR.getChild2();
    }
    else {
      throw new FlowError(); // 2004.06.01 S.Noishi
      // return ((LIRNode) pIR).getRightChild();
    }
  }

  public static FlowAnalSym flowAnalSym(IR pIR)
  {
    Sym lSym;

    if (pIR instanceof HIR) {
      if ((lSym = pIR.getSym())instanceof FlowAnalSym) {
        return (FlowAnalSym)lSym;
      }
      // } else if (pIR instanceof Register || pIR instanceof Variable)
    }
    else { // 2004.06.01 S.Noishi
      throw new FlowError(); // 2004.06.01 S.Noishi
      // return (FlowAnalSym) pIR.getSym();
    }

    return null;
  }

  public static FlowAnalSym derefedFlowAnalSym(IR pIR)
  {
    Sym lSym;
    // LIRNode lGrandChild; // 2004.06.01 S.Noishi

    if (pIR instanceof HIR) {
      if ((lSym = pIR.getSym())instanceof FlowAnalSym) {
        if (((HIR)pIR.getParent()).getOperator() != HIR.OP_ADDR) {
          return (FlowAnalSym)lSym;
        }
      }
    }
    /* 2004.06.01 S.Noishi
       else if (pIR instanceof Register)
       {
      return (Reg) pIR.getSym();
       } else if (pIR instanceof Memory)
       {
      if ((lGrandChild = ((LIRNode) pIR).getLeftChild().getLeftChild()).getLIRType() == LIRType.VARIABLE)
      {
       return (FlowAnalSym) lGrandChild.getSym();
      }
       }
     */

    return null;
  }

  /**
   * Returns the number of children of the given node.
   */
  public static int getChildCount(IR pIR)
  {
    int lChildCount = 0;
    //LIRNode lLIRNode; // 2004.06.01 S.Noishi

    if (pIR instanceof HIR) {
      lChildCount = ((HIR)pIR).getChildCount();
    }
    else {
      throw new FlowError(); // 2004.06.01 S.Noishi
      /*
           lLIRNode = (LIRNode) pIR;
           if (lLIRNode.hasLeftChild())
           {
         lChildCount++;
           }
           if (lLIRNode.hasRightChild())
           {
         lChildCount++;
           }
       */
    }

    return lChildCount;
  }

  /*  public static Sym getSym(IR pIR)
    {
    if (pIR instanceof HIR || pIR instanceof Memory)
      return pIR.getSym();
    return null;
    }
   */
  public static boolean isConstNode(IR pIR)
  {
    if (pIR instanceof ConstNode) // 2004.06.01 S.Noishi
    // if (pIR instanceof ConstNode || pIR instanceof LConst)
    {
      return true;
    }

    return false;
  }


  /**
   * Returns a FlowAnalSymVector that corresponds to the global symbols that appear in that given SubpFlow object.
   */
  public static FlowAnalSymVector globalSymVector(SubpFlow pSubpFlow)
  {
    FlowAnalSymVector lGlobal = pSubpFlow.flowAnalSymVector();
    for (int lIndex = 0; lIndex < pSubpFlow.getUsedSymCount();
         lIndex++) {
      Sym lSym = pSubpFlow.getIndexedSym(lIndex);
      if ((lSym instanceof Var)&&(lSym.isGlobal()))
      //##62 END
        lGlobal.setBit(lIndex);
    }
    return lGlobal;
  }

  public static FlowAnalSymVector staticSymVector(SubpFlow pSubpFlow)
  {
    FlowAnalSymVector lStatic = pSubpFlow.flowAnalSymVector();
    for (int lIndex = 0; lIndex < pSubpFlow.getUsedSymCount(); lIndex++) {
      Sym lSym = pSubpFlow.getIndexedSym(lIndex);
      if ((lSym instanceof Var)&&
          ((Var)lSym).getStorageClass() == Var.VAR_STATIC)
        lStatic.setBit(lIndex);
      }
    return lStatic;
  }

  /**
   * Get the List of destination Labels of the given Jump, in the order that appears in the subtree when iterated from top to left to right in depth first order.
   */
  /* 2004.05.31 S.Noishi
     public static List destinationLabels(Jump pJump)
     {
     List lLabels = new ArrayList();
     LIRNode lNode;
     for (LIRNodeIterator lNodeIterator = lirIterator(pJump);
     lNodeIterator.hasNext();)
     {
    lNode = lNodeIterator.getNext();
    if (lNode instanceof LLabel)
    {
     Symbol lSymbol = ((LLabel) lNode).getSymbol();
     if (lSymbol instanceof Label)
     {
    lLabels.add(lSymbol);
     }
    }
     }
     return lLabels;
     }
   */

  /**
   * Breadth first search. Returns the List of BBlocks in the breadth-first order from <code>pEntry</code> up to <code>pExit</code>. <code>BBlock</code>s unreachable from <code>pEntry</code> or strictly dominated by <code>pExit</code> (when considering the dominator tree rooted at <code>pEntry</code>) will not be included in the returned list. If <code>pExit</code> is not reachable from <code>pEntry</code>, then simply all the <code>BBlock</code>s reachable from <code>pEntry</code> are searched.
   *
   * @param pForward if true, the search is in the forward direction of the control flow, otherwise, the direction is opposite of that of the control flow.
   */
  public static List bfSearch(BBlock pEntry, BBlock pExit, boolean pForward)
  {
    List lBFList = new ArrayList();
    lBFList.add(pEntry);
    bfs(lBFList, pExit, 0, pForward);

    return lBFList;
  }

  private static void bfs(List pBFList, BBlock pExit, int pPos,
    boolean pForward)
  {
    List lSuccList;
    BBlock lCurrent = (BBlock)pBFList.get(pPos);
    BBlock lSucc;

    if (lCurrent != pExit) {
      lSuccList = pForward ? lCurrent.getSuccList() : lCurrent.getPredList();

      for (Iterator lIt = lSuccList.iterator(); lIt.hasNext(); ) {
        lSucc = (BBlock)lIt.next();

        if (!pBFList.contains(lSucc)) {
          pBFList.add(lSucc);
        }
      }
    }

    if (pPos < (pBFList.size() - 1)) {
      bfs(pBFList, pExit, ++pPos, pForward);
    }
  }

  /**
   * Iterator that iterates HIR nodes from top to left to right in depth first order.
   */
  public static HirIterator hirIterator(HIR pSubtree)
  {
    return (HirIterator)new HirIt(pSubtree, true);
  }

  /**
   * Iterator that iterates LIR nodes from top to left to right in depth first order.
   */
  /* 2004.05.31 S.Noishi
     public static LIRNodeIterator lirIterator(LIRNode pLIRNode)
     {
     return new LirIt(pLIRNode, true);
     }
   */

  /**
   * Iterator that iterates IR nodes from top to left to right in depth first order.
   */
  public static NodeIterator nodeIterator(IR pIR)
  {
    if (pIR instanceof HIR) {
      return new HirIt0(pIR);
    }
    else {
      //##60 throw new FlowError(); // 2004.06.01 S.Noishi
      return null; //##60
      // return new LirIt0(pIR);
    }
  }

  /**
   * Iterator that iterates IR nodes from top to left to right in depth first order.
   */
  public static NodeListIterator nodeListIterator(IR pIR)
  {
    return nodeListIterator(pIR, true, true);
  }

  /**
   * Iterator that iterates IR nodes.  If <code>pFromTop</code> is true, it iterates from top to bottom. If pFromLeft is true, it iterates from left to right. In either case, the order is depth-first (or reverse depth-first, for that matter).
   */
  public static NodeListIterator nodeListIterator(IR pIR, boolean pFromTop,
    boolean pFromLeft)
  {
    if (pIR instanceof HIR) {
      return new HirListIt0((HIR)pIR, pFromTop, pFromLeft);
    }
    else {
      throw new FlowError(); // 2004.06.01 S.Noishi
      // return new LirListIt0((LIRNode) pIR, pFromTop, pFromLeft);
    }
  }

  /* //##60
    public static int computeHashCodeOfNode(IR pIR)
    {
    if (pIR instanceof HIR)
    {
      return computeHashCodeOfNode((HIR) pIR);
    } else
    {
      throw new FlowError(); // 2004.06.01 S.Noishi
      // return computeHashCodeOfNode((LIRNode) pIR);
    }
    }
   */
  //##60
  public static boolean isUnder(IR pAncestor, IR pDescendant)
  {
    IR lIR;

    if (pAncestor == null) {
      return false;
    }
    while (!pAncestor.equals(pDescendant)) {
      if (pDescendant.getParent() == null) {
        return false;
      }
      else {
        pDescendant = pDescendant.getParent();
      }
    }
    return true;
  } // isUnder


   public static String toString(HIR pHIR)
  {
    StringBuffer lBuff = new StringBuffer();
    if (pHIR == null) //##25
      return ""; //##25
    lBuff.append("(");
    //##25 lBuff.append(pHIR);
    lBuff.append(pHIR.toStringShort()); //##25
    Sym lSym = pHIR.getSym(); //##25
    if (lSym != null) //##25
      lBuff.append(" " + lSym.getName()); //##25
    for (int i = 1; i <= pHIR.getChildCount(); i++)
      lBuff.append(toString((HIR)pHIR.getChild(i)));
    lBuff.append(")");
    return lBuff.toString();
  }

//##60 BEGIN
  /**
   * Returns true if the given node should be assigned a FlowExpId.
   */
  public static boolean shouldAssignFlowExpId(IR pIR)
  {
    if (pIR instanceof HIR) {
      switch (((HIR)pIR).getOperator()) {
        case HIR.OP_PROG:
        case HIR.OP_SUBP_DEF:
        case HIR.OP_LABEL_DEF:
        case HIR.OP_INF:
        case HIR.OP_CONST:
        case HIR.OP_SYM:
        case HIR.OP_SUBP:
        case HIR.OP_TYPE:
        case HIR.OP_LABEL:
        case HIR.OP_LIST:
        case HIR.OP_SEQ:
        case HIR.OP_ENCLOSE:
        case HIR.OP_LABELED_STMT:
        case HIR.OP_ASSIGN:
        case HIR.OP_IF:
        case HIR.OP_WHILE:
        case HIR.OP_FOR:
        case HIR.OP_UNTIL:
        case HIR.OP_INDEXED_LOOP:
        case HIR.OP_JUMP:
        case HIR.OP_SWITCH:
        case HIR.OP_RETURN:
        case HIR.OP_BLOCK:
        case HIR.OP_EXP_STMT:
        case HIR.OP_STMT_UPPER:
        case HIR.OP_SETDATA:
        case HIR.OP_NULL:
        case HIR.OP_SELECT:
        case HIR.OP_COMMA:
          return false;

        case HIR.OP_VAR:
        case HIR.OP_PARAM:
        case HIR.OP_ELEM:
        case HIR.OP_SUBS:
        case HIR.OP_INDEX:
        case HIR.OP_QUAL:
        case HIR.OP_ARROW:
        case HIR.OP_CALL:
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
        case HIR.OP_NOT:
        case HIR.OP_NEG:
        case HIR.OP_ADDR:
        case HIR.OP_CONV:
        case HIR.OP_DECAY:
        case HIR.OP_UNDECAY: //##10
        case HIR.OP_CONTENTS:
        case HIR.OP_SIZEOF:
        case HIR.OP_PHI:
        case HIR.OP_OFFSET:
        case HIR.OP_LG_AND:
        case HIR.OP_LG_OR:
        case HIR.OP_PRE_INCR:
        case HIR.OP_PRE_DECR:
        case HIR.OP_POST_INCR:
        case HIR.OP_POST_DECR:
        case HIR.OP_ADD_ASSIGN:
        case HIR.OP_SUB_ASSIGN:
        case HIR.OP_MULT_ASSIGN:
        case HIR.OP_DIV_ASSIGN:
        case HIR.OP_MOD_ASSIGN:
        case HIR.OP_SHIFT_L_ASSIGN:
        case HIR.OP_SHIFT_R_ASSIGN:
        case HIR.OP_AND_ASSIGN:
        case HIR.OP_OR_ASSIGN:
        case HIR.OP_XOR_ASSIGN:
          return true;

        default:
          throw new FlowError();
      }
    }
    else
      return false;
  }

//##60 END

  public static boolean isDefSymNode(IR pIR)
  {
    //LIRNode lParent; // 2004.06.01 S.Noishi
    //LIRNode lGrandParent; // 2004.06.01 S.Noishi
    //LIRNode lGreatGrandParent; // 2004.06.01 S.Noishi

    if (pIR instanceof HIR) {
      if ((((HIR)pIR.getParent()).getOperator() == HIR.OP_ASSIGN) &&
          ((HIR)pIR.getParent().getChild1() == pIR)) {
        return true;
      }
    }
    else { // 2004.06.01 S.Noishi
      throw new FlowError();
    }

    /*
       else if (((lParent = (LIRNode) pIR.getParent()) instanceof coins.ir.lir.Set &&
       (((LIRNode) pIR).getLIRType() == LIRType.REGISTER) &&
       (lParent.getLeftChild() == pIR)) ||
       ((lGrandParent = (LIRNode) lParent.getParent()) instanceof Memory &&
       (lGreatGrandParent = (LIRNode) lGrandParent.getParent()) instanceof coins.ir.lir.Set &&
       (lGrandParent == lGreatGrandParent.getLeftChild())))
       {
      return true;
       }
     */

    return false;
  }

  public static boolean isCall(IR pIR)
  {
    if (pIR instanceof HIR) {
      if (((HIR)pIR).getOperator() == HIR.OP_CALL) {
        return true;
      }

      return false;
    }
    else { // 2004.06.01 S.Noishi
      throw new FlowError();
    }
  }

   private static class HirIt
     implements HirIterator
   {
     List fList = new ArrayList();
     ListIterator fIt;

     HirIt(HIR pSubtree, boolean pFromTopAndLeft)
     {
       addUnder(pSubtree, pFromTopAndLeft);
       fIt = fList.listIterator();
     }

     private void addUnder(HIR pSubtree, boolean pFromTopAndLeft)
     {
       Stmt lStmt;
       HIR lHir;
       List lArgs;
       int lOpCode;
       int lChildCount;

       if (pSubtree == null) {
         return;
       }

       fList.add(pSubtree);

       if ((lOpCode = pSubtree.getOperator()) == HIR.OP_BLOCK) {
         for (lStmt = ((BlockStmt)pSubtree).getFirstStmt();
              lStmt != null; lStmt = lStmt.getNextStmt())
           addUnder(lStmt, pFromTopAndLeft);
       }
       else if (lOpCode == HIR.OP_LIST) {
         lArgs = new ArrayList();

         for (Iterator lIt = ((HirList)pSubtree).iterator();
              lIt.hasNext(); ) {
           lHir = (HIR)lIt.next();

           if (pFromTopAndLeft) {
             addUnder(lHir, pFromTopAndLeft);
           }
           else {
             lArgs.add(lHir);
           }
         }

         if (!pFromTopAndLeft) {
           for (ListIterator lListIt = lArgs.listIterator(lArgs.size());
                lListIt.hasPrevious(); )
             addUnder((HIR)lListIt.previous(), pFromTopAndLeft);
         }
       }
       else {
         lChildCount = pSubtree.getChildCount();

         if (pFromTopAndLeft) {
           for (int i = 1; i <= lChildCount; i++)
             addUnder((HIR)pSubtree.getChild(i), pFromTopAndLeft);
         }
         else {
           for (int i = lChildCount; i > 0; i--)
             addUnder((HIR)pSubtree.getChild(i), pFromTopAndLeft);
         }
       }
     }

     public HIR next()
     {
       return (HIR)fIt.next();
     }

     public boolean hasNext()
     {
       return fIt.hasNext();
     }

     public boolean hasNextStmt() //##60
     {
       return false;
     }

     public HIR getNextExecutableNode()
     {
       throw new UnsupportedOperationException();
     }

     public Stmt getNextStmt()
     {
       throw new UnsupportedOperationException();
     }

     public Stmt nextStmt() //##60
     {
       throw new UnsupportedOperationException();
     }

     public HIR getParentNode()
     {
       throw new UnsupportedOperationException();
     }

     public int getStackDepth()
     {
       throw new UnsupportedOperationException();
     }

     public HIR peekCurrent()
     {
       throw new UnsupportedOperationException();
     }

     public HIR peekNext()
     {
       return (HIR)fList.get(fIt.nextIndex());
     }

     public void replaceNext(HIR pHIR)
     {
       throw new UnsupportedOperationException();
     }
   }

  /* 2004.05.31 S.Noishi
     private static class LirIt implements LIRNodeIterator
     {
     List fList = new ArrayList();
     ListIterator fIt;
     private LIRNode fLastReturned;
     LirIt(LIRNode pLIRNode, boolean pFromTopAndLeft)
     {
    addUnder(pLIRNode, pFromTopAndLeft);
    fIt = fList.listIterator();
     }
     private void addUnder(LIRNode pLIRNode, boolean pFromTopAndLeft)
     {
    LIRNode lLeft;
    LIRNode lRight;
    if (pLIRNode == null)
    {
     return;
    }
    fList.add(pLIRNode);
    lLeft = pLIRNode.getLeftChild();
    lRight = pLIRNode.getRightChild();
    if (pFromTopAndLeft)
    {
     addUnder(lLeft, pFromTopAndLeft);
     addUnder(lRight, pFromTopAndLeft);
    } else
    {
     addUnder(lRight, pFromTopAndLeft);
     addUnder(lLeft, pFromTopAndLeft);
    }
     }
     public boolean hasNext()
     {
    return fIt.hasNext();
     }
     public boolean hasPrev()
     {
    return fIt.hasPrevious();
     }
     public LIRNode getNext()
     {
    fLastReturned = (LIRNode) fIt.next();
    return fLastReturned;
     }
     public LIRNode getPrev()
     {
    fLastReturned = (LIRNode) fIt.previous();
    return fLastReturned;
     }
     public Object clone()
     {
    throw new UnsupportedOperationException();
     }
     private void remove()
     {
    fIt.remove();
    fLastReturned = null;
     }
     }
   */

  private static class HirIt0
    implements NodeIterator
  {
    HirIt fHirIt;

    HirIt0(IR pIR)
    {
      fHirIt = new HirIt((HIR)pIR, true);
    }

    HirIt0()
    {
    }

    public boolean hasNext()
    {
      return fHirIt.hasNext();
    }

    public IR next()
    {
      return fHirIt.next();
    }

    public void remove()
    {
      throw new UnsupportedOperationException();

      //			fHirIt.fLastReturned.cutParentLink();
      //			fHirIt.remove();
    }

    public boolean hasPrevious()
    {
      throw new FlowError();
    }

    public IR previous()
    {
      throw new FlowError();
    }
  }

  /* 2004.05.31 S.Noishi
     private static class LirIt0 implements NodeIterator
     {
     LirIt lLIRNodeIt;
     LirIt0(IR pIR)
     {
    lLIRNodeIt = new LirIt((LIRNode) pIR, true);
     }
     LirIt0()
     {
     }
     public boolean hasNext()
     {
    return lLIRNodeIt.hasNext();
     }
     public IR next()
     {
    return lLIRNodeIt.getNext();
     }
     public void remove()
     {
    lLIRNodeIt.fLastReturned.remove();
    lLIRNodeIt.remove();
     }
     public boolean hasPrevious()
     {
    throw new FlowError();
     }
     public IR previous()
     {
    throw new FlowError();
     }
     }
   */

  private static class HirListIt0
    extends HirIt0
    implements NodeListIterator
  {
    ListIterator fListIt;
    boolean fFromTop;
    HirIt fHirIt;

    HirListIt0(HIR pHIR, boolean pFromTop, boolean pFromLeft)
    {
      fFromTop = pFromTop;
      fHirIt = new HirIt(pHIR, !(pFromTop ^ pFromLeft));

      if (pFromTop) {
        fListIt = fHirIt.fList.listIterator();
      }
      else {
        fListIt = fHirIt.fList.listIterator(fHirIt.fList.size());
      }
    }

    public boolean hasNext()
    {
      if (fFromTop) {
        return fListIt.hasNext();
      }
      else {
        return fListIt.hasPrevious();
      }
    }

    public IR next()
    {
      if (fFromTop) {
        return (IR)fListIt.next();
      }
      else {
        return (IR)fListIt.previous();
      }
    }

    public boolean hasPrevious()
    {
      if (fFromTop) {
        return fListIt.hasPrevious();
      }
      else {
        return fListIt.hasNext();
      }
    }

    public int nextIndex()
    {
      if (fFromTop) {
        return fListIt.nextIndex();
      }
      else {
        return fHirIt.fList.size() - 1 - fListIt.previousIndex();
      }
    }

    public IR previous()
    {
      if (fFromTop) {
        return (IR)fListIt.previous();
      }
      else {
        return (IR)fListIt.next();
      }
    }

    public int previousIndex()
    {
      if (fFromTop) {
        return fListIt.previousIndex();
      }
      else {
        return fHirIt.fList.size() - 1 - fListIt.nextIndex();
      }
    }

    public void remove()
    {
      throw new UnsupportedOperationException();
    }

    public void set(Object o)
    {
      throw new UnsupportedOperationException();
    }

    public void set(IR pIR)
    {
    }
  }

  /* 2004.05.31 S.Noishi
     private static class LirListIt0 extends LirIt0 implements NodeListIterator
     {
     ListIterator fListIt;
     boolean fFromTop;
     LirIt fLirIt;
     LirListIt0(LIRNode pLIRNode, boolean pFromTop, boolean pFromLeft)
     {
    fLirIt = new LirIt(pLIRNode, !(pFromTop ^ pFromLeft));
    fFromTop = pFromTop;
    if (pFromTop)
    {
     fListIt = fLirIt.fList.listIterator();
    } else
    {
     fListIt = fLirIt.fList.listIterator(fLirIt.fList.size());
    }
     }
     public void add(Object o)
     {
    throw new UnsupportedOperationException();
     }
     public boolean hasNext()
     {
    if (fFromTop)
    {
     return fListIt.hasNext();
    } else
    {
     return fListIt.hasPrevious();
    }
     }
     public IR next()
     {
    if (fFromTop)
    {
     return (IR) fListIt.next();
    } else
    {
     return (IR) fListIt.previous();
    }
     }
     public boolean hasPrevious()
     {
    if (fFromTop)
    {
     return fListIt.hasPrevious();
    } else
    {
     return fListIt.hasNext();
    }
     }
     public int nextIndex()
     {
    if (fFromTop)
    {
     return fListIt.nextIndex();
    } else
    {
     return fLirIt.fList.size() - 1 - fListIt.previousIndex();
    }
     }
     public IR previous()
     {
    if (fFromTop)
    {
     return (IR) fListIt.previous();
    } else
    {
     return (IR) fListIt.next();
    }
     }
     public int previousIndex()
     {
    if (fFromTop)
    {
     return fListIt.previousIndex();
    } else
    {
     return fLirIt.fList.size() - 1 - fListIt.nextIndex();
    }
     }
     public void remove()
     {
    throw new UnsupportedOperationException();
     }
     public void set(Object o)
     {
    throw new UnsupportedOperationException();
     }
     public void set(IR pIR)
     {
    throw new UnimplementedMethodException();
     }
     }
   */

  /* 2004.05.31 S.Noishi
     private static void modLvaluesUnder(LIRNode pLIRNode, java.util.Set pModLvalues, SubpFlow pSubpFlow, FlowResults pResults)
     {
     java.util.Set lModSyms = new HashSet();
     LIRNode lLIRNode;
     LIRType lLIRType;
     Sym lModSym;
     Sym lDefSym;
     LIRNode lGrandParent;
     for (LIRNodeIterator lIt = FlowUtil.lirIterator(pLIRNode);
     lIt.hasNext();)
     {
    lLIRNode = lIt.getNext();
    lLIRType = lLIRNode.getLIRType();
    if ((lModSym = lLIRNode.getSym()) != null)
    {
     if ((lLIRType == LIRType.MEMORY) ||
     (lLIRType == LIRType.REGISTER) ||
     (lLIRType == LIRType.SUBREG))
     {
    continue;
     }
     lGrandParent = (LIRNode) lLIRNode.getParent().getParent();
     if ((lGrandParent == pLIRNode) ||
     (((LIRNode) lGrandParent).getLIRType() != LIRType.MEMORY))
     {
    pModLvalues.add(pResults.getFlowExpIdForNode(lLIRNode));
     }
    }
     }
     pModLvalues.add(pResults.getFlowExpIdForNode(pLIRNode));
     if (lModSyms.isEmpty())
     {
    pModLvalues.addAll(pSubpFlow.getFlowExpIdTable());
     }
     //     return lModSyms;
     }
   */

  public static boolean isLvalue(IR pIR)
  {
    if (pIR instanceof HIR)
      switch (pIR.getOperator()) {
        case HIR.OP_VAR:
        case HIR.OP_SUBS:
        case HIR.OP_QUAL:
        case HIR.OP_ARROW:
        case HIR.OP_CONTENTS:
        case HIR.OP_UNDECAY:
          return true;
        default:
          return false;
      }
    else {
      throw new FlowError(); // 2004.06.01 S.Noishi
    }
    /*
       else
       {
      LIRType lLIRType = ((LIRNode)pIR).getLIRType();
      if (lLIRType == LIRType.MEMORY || lLIRType == LIRType.REGISTER)
       return true;
      return false;
       }
     */
  }

//##63 BEGIN
  public static boolean notDereferenced(IR pIR)
  {
      int lOpCode;
      if (pIR instanceof HIR)
      {
        lOpCode = ((HIR) pIR.getParent()).getOperator();
          //			if ((lOpCode = ((HIR)pIR.getParent()).getOperator()) == HIR.OP_ADDR || lOpCode == HIR.OP_DECAY)
          if (lOpCode == HIR.OP_ADDR)
          { // || lOpCode == HIR.OP_DECAY)
              return true;
          }
      }
      return false;
  }
//##63 END

  private static void modSymsUnder(HIR pHIR, java.util.Set pModSyms,
    SubpFlow pSubpFlow)
  {
    Sym lSym;
    lSym = pHIR.getSym();

    int lOpCode = pHIR.getOperator();

    if (pHIR instanceof Stmt) {
      throw new FlowError("Invalid argument.");
    }

    if (lSym instanceof FlowAnalSym) {
      pModSyms.add(pHIR.getSym());
    }

    switch (lOpCode) {
      case HIR.OP_SUBS:
        modSymsUnder(((SubscriptedExp)pHIR).getArrayExp(), pModSyms, pSubpFlow);

        break;

      case HIR.OP_CONTENTS:
        //##62 pModSyms.addAll(pSubpFlow.getSymIndexTable());
        pModSyms.addAll(pSubpFlow.setOfAddressTakenVariables()); //##62
        pModSyms.addAll(pSubpFlow.setOfGlobalVariables()); //##62
        break;

      case HIR.OP_ADDR:
        break;

      default:

        for (int lChildNumber = 1; lChildNumber <= pHIR.getChildCount();
             lChildNumber++)
          modSymsUnder((HIR)pHIR.getChild(lChildNumber), pModSyms, pSubpFlow);
    }
  }

  /* 2004.05.31 S.Noishi
     private static void modSymsUnder(LIRNode pLIRNode, java.util.Set pModSyms, SubpFlow pSubpFlow)
     {
//		java.util.Set lModSyms = new HashSet();
     LIRNode lLIRNode;
     LIRType lLIRType;
     Sym lModSym;
     Sym lDefSym;
     LIRNode lGrandParent;
     for (LIRNodeIterator lIt = FlowUtil.lirIterator(pLIRNode);
     lIt.hasNext();)
     {
    lLIRNode = lIt.getNext();
    lLIRType = lLIRNode.getLIRType();
    if ((lModSym = lLIRNode.getSym()) != null)
    {
     if ((lLIRType == LIRType.MEMORY) ||
     (lLIRType == LIRType.REGISTER) ||
     (lLIRType == LIRType.SUBREG))
     {
    continue;
     }
     lGrandParent = (LIRNode) lLIRNode.getParent().getParent();
     if ((lGrandParent == pLIRNode) ||
     (((LIRNode) lGrandParent).getLIRType() != LIRType.MEMORY))
     {
    pModSyms.add(lModSym);
     }
    }
     }
     if ((lDefSym = pLIRNode.getSym()) != null)
     {
    pModSyms.add(lDefSym);
     }
     if (pModSyms.isEmpty())
     {
    pModSyms.addAll(pSubpFlow.getSymIndexTable());
     }
     //     return lModSyms;
     }
   */

  /**
   * Returns true if the given IR node has as its decendant a call node.
   */
  /* //##60
     public static boolean hasCallUnder(IR pIR)
     {
     for (NodeIterator lIt = FlowUtil.nodeIterator(pIR); lIt.hasNext();)
    if (isCall((IR)lIt.next()))
     return true;
     return false;
     }
     public static boolean isSubtreeUnremovable(IR pIR)
     {
     IR lIR;
     for (NodeIterator lIt = FlowUtil.nodeIterator(pIR); lIt.hasNext();)
     {
    lIR = lIt.next();
    if (FlowUtil.isCall(lIR) || FlowUtil.isVolatile(lIR))
     return true;
     }
     return false;
     }
     public static boolean isVolatile(IR pIR)
     {
     Sym lSym = pIR.getSym();
     return lSym == null ? false : lSym.getSymType().isVolatile();
     }
   */

     public static boolean mayBeExternalAddress(IR pIR)
     {
     HIR lHIR;
     if (pIR instanceof HIR)
     {
    if ((lHIR = (HIR)pIR).getOperator() == HIR.OP_SUBS)
    {
      return mayBeExternalAddress(((SubscriptedExp)lHIR).getArrayExp());
    }
     }
     return readsFromIndefiniteAddress(pIR);
     }
     public static boolean readsFromIndefiniteAddress(IR pIR)
     {
         int lOpCode;

         if (pIR instanceof HIR)
         {
             if (((lOpCode = ((HIR) pIR).getOperator()) == HIR.OP_ARROW) ||
             (lOpCode == HIR.OP_CONTENTS || lOpCode == HIR.OP_UNDECAY))
             {
                 return true;
             }

             return false;
         }
         else
         {
             throw new FlowError(); // 2004.06.01 S.Noishi
         }
     }

  //##60
  //##63 BEGIN
  // Methods from aflow.FlowUtil:

  public static boolean isVolatile(IR pIR)
{
    Sym lSym = pIR.getSym();
    return lSym == null ? false : lSym.getSymType().isVolatile();
}

//----------------------------------------------------------------
//
// IsVarSyms:
//
//                                                       I.Fukuda
//----------------------------------------------------------------
public static boolean IsVarSyms(Sym pSym) {
    return IsVarSymType(pSym.getSymType())  ;
}
//----------------------------------------------------------------
//
// getQualVarNode:
//                                                       I.Fukuda
//----------------------------------------------------------------
public static HIR getQualVarNode(HIR pNode)
{
  //##65 BEGIN
  if (pNode == null)
    return null;
  HIR lQualVarNode = getComplexNode(pNode);
  if (lQualVarNode == null) {
    if (pNode.getOperator() ==  HIR.OP_QUAL) {
        if (isCommonQUAL(pNode) == true)
          lQualVarNode = (HIR)getQualVarNode((HIR)pNode.getChild2());
        else
          lQualVarNode = (HIR)getQualVarNode((HIR)pNode.getChild1());
    }else
      lQualVarNode = pNode;
  }
  if (((HIR_Impl)pNode).hirRoot.ioRoot.dbgHir.getLevel() > 3)
    ((HIR_Impl)pNode).hirRoot.ioRoot.dbgFlow.print(5,
      " getQualVarNode " + pNode.toStringShort() + "="
      + lQualVarNode.toStringShort());
  return lQualVarNode;
  //##65 END
}
//----------------------------------------------------------------
//
// isCommonQUAL:
//                                                       I.Fukuda
//----------------------------------------------------------------
public static boolean isCommonQUAL(HIR pNode) {
    if (pNode == null)
        return false;
    if (pNode.getOperator() !=  HIR.OP_QUAL)
        return false;
     Type t = pNode.getType();
    if (t.getTypeKind() == Type.KIND_STRUCT || t.getTypeKind() == Type.KIND_UNION ) {
         Sym tag= ((StructType)t).getTag();
         if (tag == null)
             return false;
         if(tag.getFlag(Sym.FLAG_COMMON) == true) {
                 //System.out.println("Fukuda Common="+tag.getName());
                 return true;
         }
         if(isCommonQUAL((HIR)pNode.getChild1()) == true)
                 return true;
         if(isCommonQUAL((HIR)pNode.getChild2()) == true)
                 return true;
    }
    return true;
}
//----------------------------------------------------------------
//
// isComplexQUAL:
//  ex) complex c (Node = QUAL)
//                                                       I.Fukuda
//----------------------------------------------------------------
public static boolean isComplexQUAL(HIR pNode) {
       if (pNode.getOperator() !=  HIR.OP_QUAL)
            return false;
       HIR c =getComplexNode(pNode);
       if( c== null)
           return false;
        else
           return true;
 }
//----------------------------------------------------------------
//
// getComplexNode:
//                                                       I.Fukuda
//----------------------------------------------------------------
public static HIR getComplexNode(HIR pNode) {
       HIR cNode;
       HIR Child;
       if (pNode.getOperator() !=  HIR.OP_QUAL)
            return null;
       cNode =(HIR)pNode.getChild1();
       if (isComplexNode(cNode) == true)
            return cNode;
       cNode =(HIR)pNode.getChild2();
       if (isComplexNode(cNode) == true)
            return cNode;
       if (pNode.getOperator() ==  HIR.OP_QUAL)  {
           Child =(HIR)getComplexNode((HIR)pNode.getChild1());
           if (Child != null)
            return Child;
           Child =(HIR)getComplexNode((HIR)pNode.getChild2());
           if (Child != null)
            return Child;
        }
        return (HIR)null;
 }
//----------------------------------------------------------------
//
// isComplexNode:
//  ex) complex c (Node = c)
//                                                       I.Fukuda
//----------------------------------------------------------------
public static boolean isComplexNode(HIR pNode) {

    //System.out.println("isComplexNode="+pNode.toString());
FlowAnalSym s =flowAnalSym(pNode);
    if (s!= null) {
         //System.out.println("NodeSym="+s.toString());
         Type t = pNode.getType();
         //System.out.println("NodeT="+t.toString());
        if (t.getTypeKind() == Type.KIND_STRUCT) {
            Type tsym = s.getSymType();
            Sym tag= ((StructType)tsym).getTag();
            //System.out.println("tag="+tag.toString());
             if (tag == null)
                 return false;
            if(tag.getFlag(Sym.FLAG_COMPLEX_STRUCT) == true)
                 return true;
         }
    }
    return false;

}
//----------------------------------------------------------------
//
// isComplexElemNode:
//
//  ex) complex c (Node =_imag)
//                                                       I.Fukuda
//----------------------------------------------------------------
public static boolean isComplexElemNode(HIR pNode) {
    //System.out.println("isComplexElemNode="+pNode.toString());
    if (isComplexNode(pNode)  == true)
        return false;
    HIR  lParent = (HIR) pNode.getParent();
    if (lParent.getOperator() ==  HIR.OP_QUAL)  {
        if(isComplexQUAL(lParent)  == true)
            return true;
    }
   return false;
}
//----------------------------------------------------------------
//
// IsVarSymType:
//
//                                                       I.Fukuda
//----------------------------------------------------------------
public static boolean IsVarSymType(Type pType) {

    if (pType.getTypeKind() == Type.KIND_UNION) {
        return false;
    }
    if (pType.getTypeKind() == Type.KIND_STRUCT) {
        return false;
    }
    return true;
}
//##63 END

//##65 BEGIN
  /**
   * Sort HIR nodes in pSetOfNodes by node index and
   * return the list of nodes sorted in ascending order.
   * @param pSetOfNodes set of HIR nodes to be sorted.
   * @param pMaxIndex maximum value of node indexes.
   * @return the list of sorted nodes.
   */
public static List
  sortSetOfNodesByIndex( Set pSetOfNodes, int pMaxIndex )
{
  HIR[] lNodeVector = new HIR[pMaxIndex *2 + 1];
  ArrayList lListOfNodes = new ArrayList(pSetOfNodes.size()+1);
  HIR lNode;
  for (Iterator lSetIt = pSetOfNodes.iterator();
       lSetIt.hasNext(); ) {
    lNode = (HIR)lSetIt.next();
    lNodeVector[lNode.getIndex()] = lNode;
  }
  for (int lIndex = 0; lIndex <= pMaxIndex; lIndex++) {
    if (lNodeVector[lIndex] != null)
      lListOfNodes.add(lNodeVector[lIndex]);
  }
  return lListOfNodes;
} // sortSetOfNodesByIndex

/**
 * Return true if pHir is a left hand side variable of AssignStmt.
 * A left hand side variable is an instance of either
 * Var or node with SUBS, QUAL, ARROW operator.
 * @param pHir HIR subtree to be examined.
 * @return true if LHS, false otherwise.
 */
public static boolean
  isAssignLHS( HIR pHir )
{
  if (pHir == null)
    return false;
  HIR lCurr = pHir;
  HIR lAncestor = (HIR)pHir.getParent();
  while (lAncestor != null) {
    int lOpCode = lCurr.getOperator();
    if ((lOpCode != HIR.OP_VAR)&&
        (lOpCode != HIR.OP_ELEM)&&
        (lOpCode != HIR.OP_SUBS)&&
        (lOpCode != HIR.OP_QUAL)&&
        (lOpCode != HIR.OP_ARROW))
      return false;
    if ((lAncestor instanceof AssignStmt)&&
        (lAncestor.getChild1() == lCurr))
      return true;
    lCurr = lAncestor;
    lAncestor = (HIR)lAncestor.getParent();
  }
  return false;
} // isAssignLHS

public static HIR
getAncestorAssign( HIR pHir )
{
  if (pHir == null)
    return null;
  if (pHir instanceof AssignStmt)
    return pHir;
  HIR lParent = (HIR)pHir.getParent();
  while ((lParent != null)&&
         (!(lParent instanceof AssignStmt))&&
         (!(lParent instanceof BlockStmt))) {
    lParent = (HIR)lParent.getParent();
  }
  return lParent;
} // getAncestorAssign
//##65 END

} // FlowUtil
