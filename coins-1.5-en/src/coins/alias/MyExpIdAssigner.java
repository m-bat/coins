/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * MyExpIdAssigner.java
 *
 * Created on July 3, 2003, 11:53 AM
 */

package coins.alias;

import java.util.Iterator;
import java.util.LinkedList;
import coins.HirRoot;
import coins.ir.hir.AssignStmt;
import coins.ir.hir.Exp;
import coins.ir.hir.FunctionExp;
import coins.ir.hir.HIR;
import coins.ir.hir.HirIterator;
import coins.ir.hir.HirVisitorModel2;
import coins.ir.hir.PhiExp;
import coins.ir.hir.PointedExp;
import coins.ir.hir.QualifiedExp;
import coins.ir.hir.SubpDefinition;
import coins.ir.hir.SubscriptedExp;
import coins.ir.hir.VarNode;
import coins.sym.Sym;

/**
 * Assigns MyExpIds to relevant nodes in a subprogram.
 *  Nodes <code>MyExpId</code>s are assigned to are
 * <ul>
 * <li><code>VarNode</code>s</li>
 * <li><code>SubscriptedExp</code> nodes</li>
 * <li><code>QualifiedExp</code> (both structure and
 * union member qualifications) nodes</li>
 * <li><code>PointedExp</code> nodes</li>
 * <li><code>FunctionExp</code>nodes</li>
 * <li><code>PhiExp</code> (not implemented, as far as I know)
 * nodes</li>
 * <li>arithmetic <code>Exp</code> nodes
 * (including conversion)</li>
 * <li><code>ADDRESS</code> nodes (HIR nodes whose operator code
 * (specified by <code>HIR.getOperator()</code>) is
 *  HIR.OP_ADDRESS)</li>
 * <li><code>CONTENTS</code> nodes
 * <li><code>DECAY</code> nodes
 * <li><code>UNDECAY</code> nodes
 * <li><code>AssignStmt</code> nodes
 * </ul>
 * These (except for <code>AssignStmt</code> nodes) are
 * the nodes that can be considered to have values.
 * (Nodes with aggregate type are considered to have
 * arrays of values.) Operands of the <code>ADDRESS</code>
 * nodes are also included, although their values are irrelevant.
 *
 * @author  hasegawa
 */
class MyExpIdAssigner
  extends HirVisitorModel2
{
  /**
   * Table of <code>MyExpId</code> objects whose index is
   * the HIR node index.
   */
  MyExpId fMyExpIdTable[]; // HIR index -> MyExpId. Since
  //  not all HIR nodes have MyExpId, this array has many
  // null elements. Should I have used HashMap instead?

  /**
   * Factory object used to create objects.
   */
  final AliasFactory fFactory;

  /**
   * <code>SubpDefinition</code> instance nodes contained
   * in which to assign <code>MyExpId</code>s.
   */
  SubpDefinition fSubpDef;

  private static final int EXP_ID_HASH_SIZE = 127; // Prime number
  private final LinkedList[] fMyExpIdHashtable = new LinkedList[
    EXP_ID_HASH_SIZE];

  /** Creates a new instance of MyExpIdAssigner
   *
   * @param pSubpDef <code>SubpDefinition</code> instance
   * nodes contained in which to assign <code>MyExpId</code>s.
   * @param pHirRoot <code>HirRoot</code> object shared by every
   *  module in the program.
   */
  MyExpIdAssigner(SubpDefinition pSubpDef, HirRoot pHirRoot)
  {
    super(pHirRoot);
    ioRoot.dbgAlias.print(2, "\nMyExpIdAssigner " //##51
      + pSubpDef.getSubpSym().toString()); //##51

    //##51 int lIndexTop = pSubpDef.setIndexNumberToAllNodes(0);
    //##51 BEGIN
    int lIndexTop = pSubpDef.getNodeIndexMax();
    if ((lIndexTop <= 0) ||
        ((pHirRoot.getFlowRoot() != null) &&
         (pHirRoot.getFlowRoot().fSubpFlow != null) && //##63
         (!pHirRoot.getFlowRoot().fSubpFlow.getRestructureFlag()))) { //##63
      //##62 lIndexTop = pSubpDef.setIndexNumberToAllNodes(
      //##62   pSubpDef.getNodeIndexMin());
    }
    //##51 END
    fMyExpIdTable = new MyExpId[lIndexTop];
    fSubpDef = pSubpDef;
    fFactory = new AliasFactory(pHirRoot);
  }

  /**
     * Performs the assignment for all the nodes contained in
     * <code>fSubpDef</code>.
   *
   * @return <code>fMyExpIdTable</code>, the table of assigned
   * <code>MyExpId</code>s where indexes are HIR node indexes.
   */
  MyExpId[] assign()
  {
    visit(fSubpDef.getHirBody());

    if (ioRoot.getCompileSpecification().getTrace().shouldTrace(AliasAnal.
      CATEGORY_NAME, 5)) {
      HIR lHIR;
      for (HirIterator lHirIt = hirRoot.hir.hirIterator(fSubpDef.getHirBody());
           lHirIt.hasNext(); ) {
        lHIR = lHirIt.next();
        if (lHIR != null)
          ioRoot.printOut.println("HIR: " + lHIR.getIndex() + ", MyExpId: " +
            fMyExpIdTable[lHIR.getIndex()]);
      }
    }

    return fMyExpIdTable;
  }

  /**
   * Assigns <code>MyExpId</code> to the specified <code>VarNode</code>.
   *
   * @param pVarNode <code>VarNode</code> to assign
   * <code>MyExpId</code> to.
   */
  public void atVarNode(VarNode pVarNode)
  {
    MyExpId lMyExpId = assignToNode(pVarNode);
  }

  /**
   * Assigns <code>MyExpId</code> to the specified <code>Exp</code>.
   *
   * @param pExp <code>Exp</code> to assign <code>MyExpId</code> to.
   */
  public void atExp(Exp pExp)
  {
    MyExpId lMyExpId = assignToNode(pExp);
    visitChildren(pExp);
  }

  /**
   * Assigns <code>MyExpId</code> to the specified
   * <code>SubscriptedExp</code>.
   *
   * @param pExp <code>SubscriptedExp</code> to assign
   * <code>MyExpId</code> to.
   */
  public void atSubscriptedExp(SubscriptedExp pExp)
  {
    MyExpId lMyExpId = assignToNode(pExp);
    visitChildren(pExp);
  }

  /**
   * Assigns <code>MyExpId</code> to the specified
   * <code>QualifiedExp</code>.
   *
   * @param pExp <code>QualifiedExp</code> to assign
   * <code>MyExpId</code> to.
   */
  public void atQualifiedExp(QualifiedExp pExp)
  {
    assignToNode(pExp);
    visitChildren(pExp);
  }

  /**
   * Assigns <code>MyExpId</code> to the specified
   * <code>PointedExp</code>.
   *
   * @param pExp <code>PointedExp</code> to assign
   * <code>MyExpId</code> to.
   */
  public void atPointedExp(PointedExp pExp)
  {
    assignToNode(pExp);
    visitChildren(pExp);
  }

  /**
   * Assigns <code>MyExpId</code> to the specified
   * <code>FunctionExp</code>.
   *
   * @param pExp <code>FunctionExp</code> to assign
   * <code>MyExpId</code> to.
   */
  public void atFunctionExp(FunctionExp pExp)
  {
    assignToNode(pExp);
    visitChildren(pExp);
  }

  /**
   * Assigns <code>MyExpId</code> to the specified
   * <code>AssignStmt</code>.
   *
   * @param pStmt <code>AssignStmt</code> to assign
   * <code>MyExpId</code> to.
   */
  public void atAssignStmt(AssignStmt pStmt)
  {
    assignToNode(pStmt);
    visitChildren(pStmt);
  }

  /**
   * Assigns <code>MyExpId</code> to the specified
   * <code>PhiExp</code>.
   *
   * @param pExp <code>PhiExp</code> to assign
   * <code>MyExpId</code> to.
   */
  public void atPhiExp(PhiExp pExp)
  {
    assignToNode(pExp);
    visitChildren(pExp);
  }

  private MyExpId assignToNode(HIR pHir)
  {
    int lHashCode = computeHashCodeOfNode(pHir);
    LinkedList lMyExpIdChain = fMyExpIdHashtable[lHashCode];
    MyExpId lMyExpId = null;
    if (lMyExpIdChain == null) {
      lMyExpIdChain = new LinkedList();
      fMyExpIdHashtable[lHashCode] = lMyExpIdChain;
    }

    boolean lBroken = false;

    for (Iterator lIt = lMyExpIdChain.iterator(); lIt.hasNext(); ) {
      lMyExpId = (MyExpId)lIt.next();
      if (isSameTree(lMyExpId.getHir(), pHir)) {
        lBroken = true;
        break;
      }
    }

    if (!lBroken) {
      lMyExpId = fFactory.myExpId(pHir);
      lMyExpIdChain.addFirst(lMyExpId);
    }

    fMyExpIdTable[pHir.getIndex()] = lMyExpId;
    return lMyExpId;
  }

  private static int computeHashCodeOfNode(HIR pNode)
  {
    int lCode;
    int lNodeIndex;
    int lChild;
    int lChildCount;
    Sym lSym;

    if (pNode == null) {
      return 0;
    }

    lCode = pNode.getOperator() +
      System.identityHashCode(((HIR)pNode).getType());
    lSym = pNode.getSym();

    if (lSym != null) { // This is a leaf node attached with a symbol.
      lCode = (lCode * 2) + System.identityHashCode(lSym);
    }
    else {
      lChildCount = pNode.getChildCount();

      for (lChild = 1; lChild <= lChildCount; lChild++) {
        lCode = (lCode * 2) +
          computeHashCodeOfNode((HIR)(pNode.getChild(lChild)));
      }
    }

    lCode = (lCode & 0x7FFFFFFF) % EXP_ID_HASH_SIZE;

    return lCode;
  }

  /** isSameTree
   *  @return true if pTree1 and pTree2 have the same shape,
   *      false otherwise.
   **/
  private static boolean isSameTree(HIR pTree1, HIR pTree2)
  {
    Sym lSym1;
    Sym lSym2;
    int lChildCount;
    int lChild;

    if (pTree1 == pTree2) {
      return true;
    }

    if ((pTree1 == null) || (pTree2 == null)) { // One is null, the other is not.

      return false;
    }

    if (computeHashCodeOfNode(pTree1) != computeHashCodeOfNode(pTree2)) {
      return false;
    }

    //-- With the same hash key. --//
    lSym1 = pTree1.getSym();

    if (lSym1 != null) {
      if (lSym1 == pTree2.getSym()) {
        return true;
      }
      else {
        return false;
      }
    }
    else { // The trees has no symbol attached.
      lChildCount = pTree1.getChildCount();

      if ((pTree1.getOperator() != pTree2.getOperator()) ||
          (pTree2.getChildCount() != lChildCount) ||
          (((HIR)pTree1).getType() != ((HIR)pTree2).getType())) {
        return false; // Both nodes have the same characteristics.
      }
      else { // Examine children.

        for (lChild = 1; lChild <= lChildCount; lChild++) {
          if (!isSameTree((HIR)(pTree1.getChild(lChild)),
            (HIR)(pTree2.getChild(lChild)))) {
            return false;
          }
        }

        return true; // All children of pTree1 are the same

        // to the corresponding child of pTree2.
      }
    }
  }
  // isSameTree

}
