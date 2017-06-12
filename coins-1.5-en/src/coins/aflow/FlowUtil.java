/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * FlowUtil.java
 *
 * Created on June 13, 2002, 2:18 PM
 */
package coins.aflow;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import coins.aflow.util.FAList;
import coins.aflow.util.FlowError;
import coins.aflow.util.UnimplementedMethodException;
import coins.ir.IR;
import coins.ir.hir.AssignStmt;  //##53
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
import coins.sym.Sym;
import coins.sym.Type;
import coins.sym.StructType;
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
        if (pIR instanceof HIR)
        {
            return pIR.getChild1();
        } else
        {
            throw new FlowError(); // 2004.06.01 S.Noishi
            // return ((LIRNode) pIR).getLeftChild();
        }
    }

    /**
     * Returns the second child.
     */
    public static IR getChild2(IR pIR)
    {
        if (pIR instanceof HIR)
        {
            return pIR.getChild2();
        } else
        {
            throw new FlowError(); // 2004.06.01 S.Noishi
            // return ((LIRNode) pIR).getRightChild();
        }
    }

    public static FlowAnalSym flowAnalSym(IR pIR)
    {
        Sym lSym;

        if (pIR instanceof HIR)
        {
            if ((lSym = pIR.getSym()) instanceof FlowAnalSym)
            {
                return (FlowAnalSym) lSym;
            }
        // } else if (pIR instanceof Register || pIR instanceof Variable)
        } else // 2004.06.01 S.Noishi
        {
            throw new FlowError(); // 2004.06.01 S.Noishi
            // return (FlowAnalSym) pIR.getSym();
        }

        return null;
    }

    public static FlowAnalSym derefedFlowAnalSym(IR pIR)
    {
        Sym lSym;
        // LIRNode lGrandChild; // 2004.06.01 S.Noishi

        if (pIR instanceof HIR)
        {
            if ((lSym = pIR.getSym()) instanceof FlowAnalSym)
            {
                if (((HIR) pIR.getParent()).getOperator() != HIR.OP_ADDR)
                {
                    return (FlowAnalSym) lSym;
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

        if (pIR instanceof HIR)
        {
            lChildCount = ((HIR) pIR).getChildCount();
        } else
        {
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

    /*        public static Sym getSym(IR pIR)
            {
                    if (pIR instanceof HIR || pIR instanceof Memory)
                            return pIR.getSym();
                    return null;
            }
     */
    public static boolean isConstNode(IR pIR)
    {
        if (pIR instanceof ConstNode ) // 2004.06.01 S.Noishi
        // if (pIR instanceof ConstNode || pIR instanceof LConst)
        {
            return true;
        }

        return false;
    }

    /**
     * See if the given two arguments can be identified as same. Two subtrees are the same if they have precisely the same structure (subtrees).
     */
    public static boolean isSameTree(IR pIR, IR pIR0)
    {
        if (pIR instanceof HIR)
        {
            return isSameTree((HIR) pIR, (HIR) pIR0);
        } else
        {
            throw new FlowError(); // 2004.06.01 S.Noishi
            /*
            if ((pIR != null) && (pIR0 != null))
            {
                //     System.out.println("IS SAME TREE");
                //     System.out.println("Tree 1 = " + ((LIRTree)pIR).dumpTree());
                ///     System.out.println("Tree 2 = " + ((LIRTree)pIR0).dumpTree());
            }

            boolean a = isSameTree((LIRNode) pIR, (LIRNode) pIR0);

            //    System.out.println(a ? "SAME" : "NOT SAME");
            return a;
            */
        }
    }

    /**
     * See if the given two arguments can be identified as same, in terms of FlowExpId.
     */
    public static boolean isSameTree(IR pIR, IR pIR0, FlowResults pResults)
    {
        try
        {
            return pResults.getFlowExpIdForNode(pIR) == pResults.getFlowExpIdForNode(pIR0);
        } catch (IllegalArgumentException e)
        {
            return false;
        }
    }

    public static boolean hasPointerAssign(SubpFlow pSubpFlow)
    {
        for (Iterator lIt = pSubpFlow.getBBlocks().iterator(); lIt.hasNext();)
        {
            if (hasPointerAssign((BBlock) lIt.next()))
            {
                return true;
            }
        }

        return false;
    }

    public static boolean hasPointerAssign(BBlock pBBlock)
    {
        SetRefRepr lSetRefRepr;
        java.util.Set lSetReprModSyms = new HashSet();

        for (SetRefReprIterator lIt = pBBlock.getSetRefReprs()
        .setRefReprIterator();
        lIt.hasNext();)
        {
            lSetRefRepr = (SetRefRepr) lIt.next();

            if (lSetRefRepr.sets())
            {
                if (lSetRefRepr.defSym() == null)
                {
                    lSetReprModSyms = lSetRefRepr.modSyms();

                    for (Iterator lModSymIt = lSetReprModSyms.iterator();
                    lModSymIt.hasNext();)
                        if (((FlowAnalSym) lModSymIt.next()).getSymType()
                        .getTypeKind() == Type.KIND_POINTER)
                        {
                            return true;
                        }
                }
            }
        }

        return false;
    }

    /**
     * Returns a FlowAnalSymVector that corresponds to the global symbols that appear in that given SubpFlow object.
     */
    public static FlowAnalSymVector globalSymVector(SubpFlow pSubpFlow)
    {
        FlowAnalSymVector lGlobal = pSubpFlow.flowAnalSymVector();
        FAList lSymIndexTable = pSubpFlow.getSymIndexTable();

        for (int lIndex = 1; lIndex <= lSymIndexTable.size(); lIndex++)
        {
            if (((Sym) lSymIndexTable.get(lIndex)).isGlobal())
            {
                lGlobal.setBit(lIndex);
            }
        }

        return lGlobal;
    }

        public static FlowAnalSymVector staticSymVector(SubpFlow pSubpFlow)
        {
            FlowAnalSymVector lStatic = pSubpFlow.flowAnalSymVector();
            FAList lSymIndexTable = pSubpFlow.getSymIndexTable();

            for (int lIndex = 1; lIndex <= lSymIndexTable.size(); lIndex++)
            {
                if (((Var)lSymIndexTable.get(lIndex)).getStorageClass() == Var.VAR_STATIC)
                {
                    lStatic.setBit(lIndex);
                }
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

    private static void bfs(List pBFList, BBlock pExit, int pPos, boolean pForward)
    {
        List lSuccList;
        BBlock lCurrent = (BBlock) pBFList.get(pPos);
        BBlock lSucc;

        if (lCurrent != pExit)
        {
            lSuccList = pForward ? lCurrent.getSuccList() : lCurrent.getPredList();

            for (Iterator lIt = lSuccList.iterator(); lIt.hasNext();)
            {
                lSucc = (BBlock) lIt.next();

                if (!pBFList.contains(lSucc))
                {
                    pBFList.add(lSucc);
                }
            }
        }

        if (pPos < (pBFList.size() - 1))
        {
            bfs(pBFList, pExit, ++pPos, pForward);
        }
    }

    /**
     * Iterator that iterates HIR nodes from top to left to right in depth first order.
     */
    public static HirIterator hirIterator(HIR pSubtree)
    {
        return (HirIterator) new HirIt(pSubtree, true);
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
        if (pIR instanceof HIR)
        {
            return new HirIt0(pIR);
        } else
        {
            throw new FlowError(); // 2004.06.01 S.Noishi
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
        if (pIR instanceof HIR)
        {
            return new HirListIt0((HIR) pIR, pFromTop, pFromLeft);
        } else
        {
            throw new FlowError(); // 2004.06.01 S.Noishi
            // return new LirListIt0((LIRNode) pIR, pFromTop, pFromLeft);
        }
    }

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

    public static boolean isUnder(IR pAncestor, IR pDescendant)
    {
        IR lIR;

        if (pAncestor == null)
        {
            return false;
        }

        while (!pAncestor.equals(pDescendant))
        {
            if (pDescendant.getParent() == null)
            {
                return false;
            } else
            {
                pDescendant = pDescendant.getParent();
            }
        }

        return true;
    }

    /** isSameTree:
     *  @return true if pTree1 and pTree2 have the same shape,
     *      false otherwise.
     **/
    private static boolean isSameTree(HIR pTree1, HIR pTree2)
    {
        Sym lSym1;
        Sym lSym2;
        int lChildCount;
        int lChild;

        if (pTree1 == pTree2)
        {
            return true;
        }

        if ((pTree1 == null) || (pTree2 == null))
        { // One is null, the other is not.

            return false;
        }

        if (computeHashCodeOfNode(pTree1) != computeHashCodeOfNode(pTree2))
        {
            return false;
        }

        //-- With the same hash key. --//
        lSym1 = pTree1.getSym();

        if (lSym1 != null)
        {
            if (lSym1 == pTree2.getSym())
            {
                return true;
            } else
            {
                return false;
            }
        } else
        { // The trees has no symbol attached.
            lChildCount = pTree1.getChildCount();

            if ((pTree1.getOperator() != pTree2.getOperator()) ||
            (pTree2.getChildCount() != lChildCount) ||
            (((HIR) pTree1).getType() != ((HIR) pTree2).getType()))
            {
                return false; // Both nodes have the same characteristics.
            } else
            { // Examine children.

                for (lChild = 1; lChild <= lChildCount; lChild++)
                {
                    if (!isSameTree((HIR) (pTree1.getChild(lChild)),
                    (HIR) (pTree2.getChild(lChild))))
                    {
                        return false;
                    }
                }

                return true; // All children of pTree1 are the same

                // to the corresponding child of pTree2.
            }
        }
    }
    // isSameTree

    /** computeHashCodeOfNode:
     *  Compute hash code of node pNode taking into account
     *  the hash codes of its children so that any two subtrees
     *  have the same hash code if they have the same shape.
     **/
    private static int computeHashCodeOfNode(HIR pNode)
    {
        int lCode;
        int lNodeIndex;
        int lChild;
        int lChildCount;
        Sym lSym;

        if (pNode == null)
        {
            return 0;
        }

        lCode = pNode.getOperator() +
        System.identityHashCode(((HIR) pNode).getType());
        lSym = pNode.getSym();

        if (lSym != null)
        { // This is a leaf node attached with a symbol.
            lCode = (lCode * 2) + System.identityHashCode(lSym);
        } else
        {
            lChildCount = pNode.getChildCount();
            //##55 BEGIN
            if (pNode instanceof HirList) {
              for (Iterator lIterator = ((HirList)pNode).iterator();
                   lIterator.hasNext(); ) {
                Object lObject = lIterator.next();
                if (lObject instanceof HIR)
                  lCode = (lCode * 2) +
                    computeHashCodeOfNode((HIR)lObject);
              }
            }else {
              //##55 END
              for (lChild = 1; lChild <= lChildCount; lChild++) {
                lCode = (lCode * 2) +
                  computeHashCodeOfNode((HIR)(pNode.getChild(lChild)));
              }
            }
        }
        //##53 BEGIN
        // Assign different hash code for l-value compared to r-value.
        if ((pNode.getParent() instanceof AssignStmt)&&
            (pNode.getParent().getChild1() == pNode)) { //##56
          lCode = lCode * 2;
        }
        //##53 END
        lCode = (lCode & 0x7FFFFFFF) % AssignHashBasedFlowExpId.EXP_ID_HASH_SIZE;

        return lCode;
    }

    /** isSameTree:
     *  @return true if pTree1 and pTree2 have the same shape,
     *      false otherwise.
     **/
    /* 2004.05.31 S.Noishi
    private static boolean isSameTree(LIRNode pTree1, LIRNode pTree2)
    {
        Sym lSym1;
        Sym lSym2;
        int lChildCount;
        int lChild;
        LIRNode lTree = (LIRNode) pTree1;
        LIRNode lTree0 = (LIRNode) pTree2;

        if (lTree == lTree0)
        {
            return true;
        }

        if ((lTree == null) || (lTree0 == null))
        { // One is null, the other is not.

            return false;
        }

        //                System.out.println("IS SAME TREE");
        //                System.out.println("Tree 1 = " + ((LIRTree)pTree1).dumpTree());
        //                System.out.println("Tree 2 = " + ((LIRTree)pTree2).dumpTree());
        LIRType lLIRType = lTree.getLIRType();
        LIRType lLIRType0 = lTree0.getLIRType();
        Number lValue;

        //		if (lTree.getClass() != lTree0.getClass())
        //			return false;
        if (lLIRType != lLIRType0)
        {
            return false;
        }

        if (lLIRType == LIRType.OPERATOR)
        {
            if (((Operator) lTree).getOperatorType() != ((Operator) lTree0).getOperatorType())
            {
                return false;
            }
        }

        if (lLIRType == LIRType.CONDITION)
        {
            if (((Condition) lTree).getConditionType() != ((Condition) lTree0).getConditionType())
            {
                return false;
            }
        }

        if (lLIRType == LIRType.JUMP)
        {
            if (((Jump) lTree).getJumpType() != ((Jump) lTree0).getJumpType())
            {
                return false;
            }
        }

        if (lLIRType == LIRType.CALL)
        {
            if (((Call) lTree).getSymbol() != ((Call) lTree0).getSymbol())
            {
                return false;
            }
        }

        if ((lLIRType == LIRType.REGISTER) || (lLIRType == lLIRType.SUBREG))
        {
            if (((RegSymbolic) lTree).getRegSymbol() == ((RegSymbolic) lTree0).getRegSymbol())
            {
                return isSameTree(lTree.getRightChild(), lTree0.getRightChild());
            }

            return false;
        }

        if (lLIRType == LIRType.VARIABLE)
        {
            if (((Symbolic) lTree).getSymbol() == ((Symbolic) lTree0).getSymbol())
            {
                return true;
            } else
            {
                return false;
            }
        }

        if (lLIRType == LIRType.CONST)
        {
            lValue = ((LConst) lTree).getValue();

            if (lValue != null)
            {
                if (lValue.equals(((LConst) lTree0).getValue()))
                {
                    return true;
                }
            } else if (lTree.getSym().equals(lTree0.getSym()))
            {
                return true;
            }

            return false;
        }

        if ((lLIRType == LIRType.LABEL) || (lLIRType == LIRType.LABELDEF) ||
        (lLIRType == LIRType.VARIABLE))
        {
            if (((Symbolic) lTree).getSymbol() == ((Symbolic) lTree).getSymbol())
            {
                return true;
            }

            return false;
        }

        if (computeHashCodeOfNode(lTree) != computeHashCodeOfNode(lTree0))
        {
            return false;
        }

        //-- With the same hash code. --//
        if (isSameTree(lTree.getLeftChild(), lTree0.getLeftChild()) &&
        isSameTree(lTree.getRightChild(), lTree0.getRightChild()))
        {
            return true;
        }

        return false;
    }
    // isSameTree
    */

    /** computeHashCodeOfNode:
     *  Compute hash code of node pNode taking into account
     *  the hash codes of its children so that any two subtrees
     *  have the same hash code if they have the same shape.
     **/
    /* 2004.05.31 S.Noishi
    private static int computeHashCodeOfNode(LIRNode pNode)
    {
        int lCode;
        int lNodeIndex;
        int lChild;
        int lChildCount;
        Sym lSym;
        LIRType lLIRType;

        Number lValue;

        if (true)
        {
            return 0;
        }

        if (pNode == null)
        {
            return 0;
        }

        lCode = pNode.getOperator() +
        System.identityHashCode(lLIRType = ((LIRNode) pNode).getLIRType());

        if ((lLIRType == LIRType.CONST) &&
        ((lValue = ((LConst) pNode).getValue()) != null))
        {
            lCode = lCode + (int) lValue.doubleValue();
        }

        lCode = (lCode * 2) +
        computeHashCodeOfNode(((LIRNode) pNode).getLeftChild());
        lCode = (lCode * 2) +
        computeHashCodeOfNode(((LIRNode) pNode).getRightChild());
        lCode = (lCode & 0x7FFFFFFF) % AssignHashBasedFlowExpId.EXP_ID_HASH_SIZE;

        return lCode;
    }
    */

    public static String toString(HIR pHIR)
    {
        StringBuffer lBuff = new StringBuffer();
                if (pHIR == null) //##25
                  return "";      //##25
        lBuff.append("(");
        //##25 lBuff.append(pHIR);
        lBuff.append(pHIR.toStringShort());   //##25

        //##71 Sym lSym = pHIR.getSym();             //##25
        //##71 if (lSym != null)                     //##25
        //##71   lBuff.append(" " + lSym.getName()); //##25
        for (int i = 1; i <= pHIR.getChildCount(); i++)
            lBuff.append(toString((HIR)pHIR.getChild(i)));
        lBuff.append(")");
        return lBuff.toString();
    }

//	/**
//	 * Unfinished.
//	 */
//	public static String toString(HIR pHIR, int pIndent)
//	{
//		String lLs = System.getProperty("line.separator");
//		StringBuffer lBuff = new StringBuffer();
//
//		for (int i = 0; i < pIndent; i++)
//			lBuff.append(" ");
//
//		lBuff.append(pHIR + lLs);
//
//		for (int i = 1; i <= pHIR.getChildCount(); i++)
//			lBuff.append(toString((HIR) pHIR.getChild(i), pIndent + 1));
//
//		return lBuff.toString();
//	}

    /* 2004.05.31 S.Noishi
    public static boolean hasJavaPrimitive(Ltype pLtype)
    {
        if ((pLtype == Ltype.I8) || (pLtype == Ltype.I16) ||
        (pLtype == Ltype.I32) || (pLtype == Ltype.I64) ||
        (pLtype == Ltype.F32) || (pLtype == Ltype.F64))
        {
            return true;
        }

        return false;
    }
    */

    public static boolean shouldAssignFlowExpId(IR pIR)
    {
        return AssignHashBasedFlowExpId.shouldAssignFlowExpId(pIR);
    }

    /**
     * Dynamically scans nodes.
     */
    public static DNodeIterator dNodeIterator(IR pIR)
    {
        if (pIR instanceof HIR)
        {
            return new HirIt1((HIR) pIR);
        } else
        {
            throw new UnimplementedMethodException();
        }
    }

    public static boolean isDefSymNode(IR pIR)
    {
        //LIRNode lParent; // 2004.06.01 S.Noishi
        //LIRNode lGrandParent; // 2004.06.01 S.Noishi
        //LIRNode lGreatGrandParent; // 2004.06.01 S.Noishi

        if (pIR instanceof HIR)
        {
            if ((((HIR) pIR.getParent()).getOperator() == HIR.OP_ASSIGN) &&
            ((HIR) pIR.getParent().getChild1() == pIR))
            {
                return true;
            }
        }
        else // 2004.06.01 S.Noishi
        {
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

    public static boolean isUnderCall(IR pIR)
    {
        if (pIR instanceof HIR)
        {
            while (pIR != null)
            {
                if (((HIR) pIR).getOperator() == HIR.OP_CALL)
                {
                    return true;
                }

                pIR = pIR.getParent();
            }
        } else
        {
            throw new FlowError(); // 2004.06.01 S.Noishi
            /*
            while (pIR != null)
            {
                if (((LIRNode) pIR).getLIRType() == LIRType.CALL)
                {
                    return true;
                }

                pIR = pIR.getParent();
            }
            */
        }

        return false;
    }

    public static boolean notDereferenced(IR pIR)
    {
        int lOpCode;

        if (pIR instanceof HIR)
        {
            //			if ((lOpCode = ((HIR)pIR.getParent()).getOperator()) == HIR.OP_ADDR || lOpCode == HIR.OP_DECAY)
            if ((lOpCode = ((HIR) pIR.getParent()).getOperator()) == HIR.OP_ADDR)
            { // || lOpCode == HIR.OP_DECAY)

                return true;
            }
        }

        return false;
    }

    public static boolean isCall(IR pIR)
    {
        if (pIR instanceof HIR)
        {
            if (((HIR) pIR).getOperator() == HIR.OP_CALL)
            {
                return true;
            }

            return false;
        }
        else // 2004.06.01 S.Noishi
        {
            throw new FlowError();
        }
        /*
        else if (((LIRNode) pIR).getLIRType() == LIRType.CALL)
        {
            return true;
        }

        return false;
        */
    }

    public static List argsOf(IR pIR)
    {
        List lArgs = new ArrayList();

        if (pIR instanceof HIR)
        {
            for (Iterator lIt = ((FunctionExp) pIR).getActualParamList()
            .iterator(); lIt.hasNext();)
                lArgs.add(lIt.next());
        }
        else // 2004.06.01 S.Noishi
        {
            throw new FlowError();
        }
        /*
        } else
        {
            LIRNodeIterator lLIRIt = lirIterator((LIRNode) pIR);

            for (; lLIRIt.hasNext();)
            {
                LIRNode lLIRNode = lLIRIt.getNext();

                if (lLIRNode.getLIRType() == LIRType.LISTA)
                {
                    lArgs.add(lLIRNode.getLeftChild());
                }
            }
        }
        */

        return lArgs;
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

    /* 2004.05.31 S.Noishi
    private static boolean hasDefiniteValue(LIRNode pLIRNode)
    {
        LIRType lLIRType = pLIRNode.getLIRType();

        if (lLIRType == LIRType.OPERATOR)
        {
            return hasDefiniteValue(pLIRNode.getLeftChild()) &&
            (!pLIRNode.hasRightChild() ||
            hasDefiniteValue(pLIRNode.getRightChild()));
        } else if ((lLIRType == LIRType.CONDITION) ||
        (lLIRType == LIRType.JUMP) || (lLIRType == LIRType.LABEL) ||
        (lLIRType == LIRType.LABELDEF) || (lLIRType == LIRType.LISTA) ||
        (lLIRType == LIRType.LISTD) || (lLIRType == LIRType.SUBREG) ||
        (lLIRType == LIRType.USE) || (lLIRType == LIRType.CLOBBER) ||
        (lLIRType == LIRType.ASMCONST) ||
        (lLIRType == LIRType.PROLOGUE) ||
        (lLIRType == LIRType.EPILOGUE) || (lLIRType == LIRType.PHI) ||
        (lLIRType == LIRType.LINEINFO))
        {
            throw new FlowError("Unexpected.");
        } else if ((lLIRType == LIRType.REGISTER) ||
        (lLIRType == LIRType.MEMORY) || (lLIRType == LIRType.CALL))
        {
            return false;
        } else if ((lLIRType == LIRType.CONST) ||
        (lLIRType == LIRType.VARIABLE))
        {
            return true;
        } else
        {
            throw new FlowError("Unexpected.");
        }
    }
    */

    private static class HirIt implements HirIterator
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

            if (pSubtree == null)
            {
                return;
            }

            fList.add(pSubtree);

            if ((lOpCode = pSubtree.getOperator()) == HIR.OP_BLOCK)
            {
                for (lStmt = ((BlockStmt) pSubtree).getFirstStmt();
                lStmt != null; lStmt = lStmt.getNextStmt())
                    addUnder(lStmt, pFromTopAndLeft);
            } else if (lOpCode == HIR.OP_LIST)
            {
                lArgs = new ArrayList();

                for (Iterator lIt = ((HirList) pSubtree).iterator();
                lIt.hasNext();)
                {
                    lHir = (HIR) lIt.next();

                    if (pFromTopAndLeft)
                    {
                        addUnder(lHir, pFromTopAndLeft);
                    } else
                    {
                        lArgs.add(lHir);
                    }
                }

                if (!pFromTopAndLeft)
                {
                    for (ListIterator lListIt = lArgs.listIterator(lArgs.size());
                    lListIt.hasPrevious();)
                        addUnder((HIR) lListIt.previous(), pFromTopAndLeft);
                }
            } else
            {
                lChildCount = pSubtree.getChildCount();

                if (pFromTopAndLeft)
                {
                    for (int i = 1; i <= lChildCount; i++)
                        addUnder((HIR) pSubtree.getChild(i), pFromTopAndLeft);
                } else
                {
                    for (int i = lChildCount; i > 0; i--)
                        addUnder((HIR) pSubtree.getChild(i), pFromTopAndLeft);
                }
            }
        }

        public HIR next()
        {
            return (HIR) fIt.next();
        }

        public boolean hasNext()
        {
            return fIt.hasNext();
        }

        public HIR getNextExecutableNode()
        {
            throw new UnsupportedOperationException();
        }

        public boolean hasNextStmt() //##60
        {
          throw new UnsupportedOperationException(); //##60
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
            return (HIR) fList.get(fIt.nextIndex());
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

    private static class HirIt0 implements NodeIterator
    {
        HirIt fHirIt;

        HirIt0(IR pIR)
        {
            fHirIt = new HirIt((HIR) pIR, true);
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

    private static class HirListIt0 extends HirIt0 implements NodeListIterator
    {
        ListIterator fListIt;
        boolean fFromTop;
        HirIt fHirIt;

        HirListIt0(HIR pHIR, boolean pFromTop, boolean pFromLeft)
        {
            fFromTop = pFromTop;
            fHirIt = new HirIt(pHIR, !(pFromTop ^ pFromLeft));

            if (pFromTop)
            {
                fListIt = fHirIt.fList.listIterator();
            } else
            {
                fListIt = fHirIt.fList.listIterator(fHirIt.fList.size());
            }
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
                return fHirIt.fList.size() - 1 - fListIt.previousIndex();
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

    private static class HirIt1 implements DNodeIterator
    {
        final static HIR EOB = new HIR_Impl();
        private HIR fNext = null; // Node that would be returned by the next call of next().

        HirIt1(HIR pHIR)
        {
            fNext = pHIR;
        }

        public boolean hasNext()
        {
            return (fNext != EOB) && (fNext != null);
        }

        public IR next()
        {
            if (fNext == null)
            {
                return null;
            }

            HIR lCurrent = fNext;
            HIR lHIR = fNext;
            HIR lHIRNext;
            int lOpCode = 0;

            lHIRNext = tryNext(lHIR);
            lHIR = lHIRNext;
            fNext = lHIR;

            return lCurrent;
        }

        public IR getNextExecutableNode()
        {
            HIR lHIR;

            do
                lHIR = (HIR) next();
            while (!Flow.isExecutable(lHIR) && (lHIR != null) && (lHIR != EOB));

            if (lHIR == EOB)
            {
                lHIR = null;
            }

            return lHIR;
        }

        private static HIR tryNext(HIR pHIR)
        {
            HIR lNext;
            int i = 1;

            for (i = 1; i <= pHIR.getChildCount(); i++)
            {
                lNext = (HIR) pHIR.getChild(i);

                if (lNext != null)
                {
                    return lNext;
                }
            }

            return tryNeitherDescendantsNorAncestors(pHIR);
        }

        private static HIR tryNeitherDescendantsNorAncestors(HIR pHIR)
        {
            HIR lNext;
            int lChildNumber;
            HIR lParent;
            int lChildCount;

            if ((lNext = pHIR.getNextStmt()) != null)
            {
                return lNext;
            }

            lChildNumber = pHIR.getChildNumber();
            lParent = (HIR) pHIR.getParent();

            if (lParent == null)
            {
                return EOB; // End of subprogram reached.
            }

            lChildCount = lParent.getChildCount();

            while ((0 < lChildNumber) && (lChildNumber < lChildCount))

                if ((lNext = (HIR) lParent.getChild(++lChildNumber)) != null)
                {
                    return lNext;
                }

            return tryNeitherDescendantsNorAncestors(lParent);
        }

        public IR skipSubtree()
        {
            if (fNext == null)
            {
                return null;
            }

            HIR lCurrent = fNext;
            HIR lHIR = fNext;
            HIR lHIRNext;
            int lOpCode = 0;

            lHIRNext = tryNeitherDescendantsNorAncestors(lHIR);
            lHIR = lHIRNext;
            fNext = lHIR;

            return lCurrent;
        }
    }

    public static java.util.Set modLvalues(SetRefRepr pSetRefRepr, FlowResults pResults)
    {
        java.util.Set lLvalues = new HashSet();
        if (pSetRefRepr.sets())
        {
            if (pSetRefRepr instanceof SetRefReprHir)
                modLvaluesUnder((HIR)pSetRefRepr.defNode(), lLvalues, pSetRefRepr.getBBlock().getSubpFlow(), pResults);
            else
                throw new FlowError(); // 2004.06.01 S.Noishi
                // modLvaluesUnder((LIRNode)pSetRefRepr.defNode(), lLvalues, pSetRefRepr.getBBlock().getSubpFlow(), pResults);
        }
        return lLvalues;
    }



    private static void modLvaluesUnder(HIR pHIR, java.util.Set pModLvalues, SubpFlow pSubpFlow, FlowResults pResults)
    {
        Sym lSym = pHIR.getSym();

        int lOpCode = pHIR.getOperator();

        if (pHIR instanceof Stmt)
        {
            throw new FlowError("Invalid argument.");
        }

        if (lSym instanceof FlowAnalSym)
        {
            pModLvalues.add(pResults.getFlowExpIdForNode(pHIR));
        }

        switch (lOpCode)
        {
            case HIR.OP_SUBS:
                pModLvalues.add(pResults.getFlowExpIdForNode(pHIR));
                modLvaluesUnder(((SubscriptedExp) pHIR).getArrayExp(), pModLvalues, pSubpFlow, pResults);

                break;

            case HIR.OP_CONTENTS:
                pModLvalues.addAll(pSubpFlow.getFlowExpIdTable());
                //            fResults.put("WritesToIndefiniteAddress", this, new Boolean(true));

                break;

            case HIR.OP_ADDR:
                break;

            default:

                for (int lChildNumber = 1; lChildNumber <= pHIR.getChildCount();
                lChildNumber++)
                    modLvaluesUnder((HIR) pHIR.getChild(lChildNumber), pModLvalues, pSubpFlow, pResults);
        }
    }

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

        //       return lModSyms;
    }
    */

    public static boolean isLvalue(IR pIR)
    {
        if (pIR instanceof HIR)
            switch(pIR.getOperator())
            {
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
        else
        {
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

    /**
     * @param pFlowExpId lvalue
     * @param pFlowExpId lvalue
     */
    public static boolean possiblyOverlaps(FlowExpId pFlowExpId, FlowExpId pFlowExpId0, SubpFlow pSubpFlow)
    {
        java.util.Set lModSyms = new HashSet();
        java.util.Set lModSyms0 = new HashSet();

        if (pFlowExpId.getLinkedNode() instanceof HIR)
        {
            modSymsUnder((HIR)pFlowExpId.getLinkedNode(), lModSyms, pSubpFlow);
            modSymsUnder((HIR)pFlowExpId0.getLinkedNode(), lModSyms0, pSubpFlow);
        }
        else
        {
            throw new FlowError(); // 2004.06.01 S.Noishi
        }
        /*
        else
        {
            modSymsUnder((LIRNode)pFlowExpId.getLinkedNode(), lModSyms, pSubpFlow);
            modSymsUnder((LIRNode)pFlowExpId0.getLinkedNode(), lModSyms0, pSubpFlow);
        }
        */
        return lModSyms.removeAll(lModSyms0);
    }

    public static boolean definitelyOverlaps(FlowExpId pFlowExpId, FlowExpId pFlowExpId0, SubpFlow pSubpFlow)
    {
        IR lIR;
        SubscriptedExp lSubs;
        FlowResults lResults = pSubpFlow.results();
        FlowExpId lLeftChildExpId;
        // LIRNode lLIRNode; // 2004.06.01 S.Noishi

        if (pFlowExpId != pFlowExpId0)
            return false;
        if ((lIR = pFlowExpId.getLinkedNode()) instanceof HIR)
        {
            switch (((HIR)lIR).getOperator())
            {
                case HIR.OP_VAR:
                    return pFlowExpId == pFlowExpId0;
                case HIR.OP_SUBS:
                    return definitelyOverlaps((lLeftChildExpId = lResults.getFlowExpIdForNode((lSubs = (SubscriptedExp)lIR).getArrayExp())), lLeftChildExpId, pSubpFlow) && lSubs.getSubscriptExp().getOperator() == HIR.OP_CONST;
                case HIR.OP_CONTENTS:
                case HIR.OP_ARROW:
                    return false;
                case HIR.OP_QUAL:
                    return definitelyOverlaps(lLeftChildExpId = lResults.getFlowExpIdForNode(lIR.getChild1()), lLeftChildExpId, pSubpFlow);
                case HIR.OP_UNDECAY:
                    return false;
                default:
                    throw new FlowError();
            }
        } else
            throw new FlowError(); // 2004.06.01 S.Noishi
            // return (lLIRNode = (LIRNode)lIR).getLIRType() == LIRType.MEMORY ? hasDefiniteValue(lLIRNode.getLeftChild()) : true;
    }

    private static void modSymsUnder(HIR pHIR, java.util.Set pModSyms, SubpFlow pSubpFlow)
    {
        Sym lSym;
        lSym = pHIR.getSym();

        int lOpCode = pHIR.getOperator();

        if (pHIR instanceof Stmt)
        {
            throw new FlowError("Invalid argument.");
        }

        if (lSym instanceof FlowAnalSym)
        {
            pModSyms.add(pHIR.getSym());
        }

        switch (lOpCode)
        {
            case HIR.OP_SUBS:
                modSymsUnder(((SubscriptedExp) pHIR).getArrayExp(), pModSyms, pSubpFlow);

                break;

            case HIR.OP_CONTENTS:
                pModSyms.addAll(pSubpFlow.getSymIndexTable());

                break;

            case HIR.OP_ADDR:
                break;

            default:

                for (int lChildNumber = 1; lChildNumber <= pHIR.getChildCount();
                lChildNumber++)
                    modSymsUnder((HIR) pHIR.getChild(lChildNumber), pModSyms, pSubpFlow);
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

        //       return lModSyms;
    }
    */

        /**
     * Returns true if the given IR node has as its decendant a call node.
     */
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
    public static HIR getQualVarNode(HIR pNode) {
        HIR c;
        c= getComplexNode(pNode);
        if(c != null)
            return c;
        if (pNode.getOperator() ==  HIR.OP_QUAL) {
            if (IsCommonQUAL(pNode) == true)
                return  (HIR)getQualVarNode((HIR)pNode.getChild2());
            else
                return  (HIR)getQualVarNode((HIR)pNode.getChild1());
        }
        return  pNode;
    }
    //----------------------------------------------------------------
    //
    // IsCommonQUAL:
    //                                                       I.Fukuda
    //----------------------------------------------------------------
    public static boolean IsCommonQUAL(HIR pNode) {
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
             if(IsCommonQUAL((HIR)pNode.getChild1()) == true)
                     return true;
             if(IsCommonQUAL((HIR)pNode.getChild2()) == true)
                     return true;
        }
        return true;
    }
    //----------------------------------------------------------------
    //
    // IsComplexQUAL:
    //  ex) complex c (Node = QUAL)
    //                                                       I.Fukuda
    //----------------------------------------------------------------
    public static boolean IsComplexQUAL(HIR pNode) {
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
           if (IsComplexNode(cNode) == true)
                return cNode;
           cNode =(HIR)pNode.getChild2();
           if (IsComplexNode(cNode) == true)
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
    // IsComplexNode:
    //  ex) complex c (Node = c)
    //                                                       I.Fukuda
    //----------------------------------------------------------------
    public static boolean IsComplexNode(HIR pNode) {

        //System.out.println("IsComplexNode="+pNode.toString());
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
    // IsComplexElemNode:
    //
    //  ex) complex c (Node =_imag)
    //                                                       I.Fukuda
    //----------------------------------------------------------------
    public static boolean IsComplexElemNode(HIR pNode) {
        //System.out.println("IsComplexElemNode="+pNode.toString());
        if (IsComplexNode(pNode)  == true)
            return false;
        HIR  lParent = (HIR) pNode.getParent();
        if (lParent.getOperator() ==  HIR.OP_QUAL)  {
            if(IsComplexQUAL(lParent)  == true)
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
}
