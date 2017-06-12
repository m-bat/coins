/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.HirRoot;
import coins.IoRoot;
import coins.SymRoot;
import coins.sym.PointerType;
import coins.sym.Type;
import coins.sym.Var;
import coins.sym.VectorType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

//========================================//
//                        Dec. 2004.

/** SubsPtrTransformation
 *  Array subscript (Subs) expression to pointer expression transformation
 *  and vice versa.
**/
public class
SubsPtrTransformation
{

  IoRoot  ioRoot;
  HirRoot hirRoot;
  SymRoot symRoot;
  protected final int fDbgLevel; //##58

/**
 * Constructor to prepare for transformation
 * @param pHirRoot HirRoot instance to be recorded.
 */
public
SubsPtrTransformation( HirRoot pHirRoot )
{
  hirRoot = pHirRoot;
  symRoot = hirRoot.symRoot;
  ioRoot  = hirRoot.ioRoot;
  fDbgLevel = ioRoot.dbgHir.getLevel(); //##58
}

/** listUpSubsNodes
 * Make an array-list of Subs nodes in pHir in the order of
 * pre-visiting depth-first order.
 * @param pHir the subtree to be searched (usually HIR-body
 *     of subprogram).
 * @return the array-list of Subs nodes.
 */
public ArrayList
listUpSubsNodes( HIR pHir )
{
  if (fDbgLevel > 0) //##58
    ioRoot.dbgHir.print(2, "listUpSubsNodes", pHir.toStringShort());
  HIR lHir;
  ArrayList lSubsNodeList = new ArrayList(300);
  for ( HirIterator lIterator = hirRoot.hir.hirIterator(pHir);
        lIterator.hasNext(); ) {
    lHir = lIterator.next();
    if (lHir instanceof SubscriptedExp) {
      lSubsNodeList.add(lHir);
    }
  }
  return lSubsNodeList;
} // listUpSubsNodes

/** subsToPtrTrasnsformation
 * Transform subscripted expression in pHir to pointer expression and
 * record its correspondence to pPtrSubsCorrespondence.
 * @param pHir HIR subtree to be transformed.
 * @param pPtrSubsCorrespondence record the Subs-Ptr correspondence.
 * @return true if some expression is transformed, false if no
 *     expression is transformed.
 */
public boolean
subsToPtrTrasnsformation( HIR pHir, Map pPtrSubsCorrespondence )
{
  ArrayList lSubsNodeList;
  SubscriptedExp lSubscriptedExp;
  Exp lPointerExp, lSubscriptExp, lSizeExp, lOffsetExp;
  Type lElemType;
  VectorType lVectorType;
  Exp  lVectorVarExp;
  HIR hir = hirRoot.hir;
  HIR lResult = pHir;
  if (fDbgLevel > 0) //##58
    ioRoot.dbgHir.print(2, "subsToPtrTransformation", pHir.toStringShort());
  lSubsNodeList = listUpSubsNodes(pHir);
  // Traverse the list nodes in the order of inverse depth-first
  // search so that inner subscripted expression is processed before
  // outer subscripted expression.
  for (int i = lSubsNodeList.size()-1; i >= 0; i--) {
    lSubscriptedExp = (SubscriptedExp)lSubsNodeList.get(i);
    lVectorVarExp = (Exp)((HIR)lSubscriptedExp.getChild1()).copyWithOperands();
    lSubscriptExp = (Exp)((HIR)lSubscriptedExp.getChild2()).copyWithOperands();
    lElemType = lSubscriptedExp.getType();
    lSizeExp = (Exp)lElemType.getSizeExp().copyWithOperands();
    lOffsetExp = hir.exp(HIR.OP_MULT, lSubscriptExp, lSizeExp);
    lPointerExp =
        hir.exp(HIR.OP_CONTENTS,
          hir.exp(HIR.OP_ADD,
            hir.exp(HIR.OP_DECAY, lVectorVarExp),
            lOffsetExp));
    pPtrSubsCorrespondence.put(lPointerExp, lSubscriptedExp);
    lSubscriptedExp.replaceThisNode(lPointerExp);
  }
  if (fDbgLevel >= 4) {
    ioRoot.dbgHir.print(4, "Result of subsToPtrTransformation of",
                       pHir.toString() );
    lResult.print(1, true);
  }
  return ! lSubsNodeList.isEmpty();
} // subsToPtrTrasnsformation

/** ptrToSubsTrasnsformation
 * Transform pointer expression in pHir to subscript expression
 * if the pointer expression is recorded in pPtrSubsCorrespondence
 * or it is an expression representing simple subscripted expression.
 * @param pHir HIR subtree to be transformed.
 * @param pPtrSubsCorrespondence show the Subs-Ptr correspondence.
 * @return true if some expression is transformed, false if no
 *     expression is transformed.
 */
public boolean
ptrToSubsTransformation( HIR pHir, Map pPtrSubsCorrespondence )
{
  HIR lResult = pHir;
  HIR lNewPtrNode;
  HIR hir = hirRoot.hir;
  HIR lPtrNode;
  Exp lVarExp, lVarExpCopied, lVarVarExp;
  Exp lIndexExp, lIndexExpCopied;
  Exp lSubscriptedExp;
  if (pPtrSubsCorrespondence == null) {
    return false;     // REFINE
  }
  if (fDbgLevel > 0)  { //##58
    ioRoot.dbgHir.print(2, "ptrToSubsTransformation", pHir.toStringShort());
    ioRoot.dbgHir.print(3, "PtrSubsCorrespondence", pPtrSubsCorrespondence.toString());
  }
  ArrayList lPtrNodesList = listUpPtrNodes(pHir);
  // Traverse the list nodes in the order of inverse depth-first
  // search so that inner pointer expression is processed before
  // outer pointer expression.
  for (int i = lPtrNodesList.size()-1; i >= 0; i--) {
    lPtrNode = (Exp)lPtrNodesList.get(i);
    // If lPtrNode is in pPtrSubsCorrespondence then transform.
    // If lPtrNode is a copied node not in pPtrSubsCorrespondence
    // but takes a form of simple subscripted node, then transform.
    if (pPtrSubsCorrespondence.containsKey(lPtrNode)||
        (lPtrNode.getChild1().getChild1().getChild1() instanceof VarNode)) {
      // lPtrNode takes the form
      //   (contents
      //    (add
      //     (decay
      //      (Var-exp))
      //     (mult
      //       (index-exp)
      //       (element-size) ) ) )
      lVarExp = (Exp)((HIR)lPtrNode.getChild1().getChild1().getChild1());
      lVarExpCopied = (Exp)lVarExp.copyWithOperands();
      lIndexExp = (Exp)((HIR)lPtrNode.getChild1().getChild2().getChild1());
      lIndexExpCopied = (Exp)lIndexExp.copyWithOperands();
      lSubscriptedExp = hir.subscriptedExp(lVarExpCopied, lIndexExpCopied);
      /* //56 BEGIN
      // Check nested pointer expression.
      if (pPtrSubsCorrespondence.containsKey(lVarExp)) {
        pPtrSubsCorrespondence.put(lVarExpCopied, lSubscriptedExp.getChild1());
      }
      */ //##56 END
      lPtrNode.replaceThisNode(lSubscriptedExp);
    }
  }
  if (fDbgLevel >= 4) {
    ioRoot.dbgHir.print(4, "Result of ptrToSubsTransformation of",
                       pHir.toString() );
    lResult.print(1, true);
  }
  return ! lPtrNodesList.isEmpty();
} // ptrToSubsTransformation

/** listUpPtrNodes
<PRE>
 * Make an array-list of  pointer expressions taking the form
 *   (contents
 *    (add
 *     (decay
 *      <var >)
 *     (exp ) ) )
 *  in pHir in the order of
 * pre-visiting depth-first order.
</PRE>
 * @param pHir the subtree to be searched (usually HIR-body
 *     of subprogram).
 * @return the array-list of contents-nodes.
 */
public ArrayList
listUpPtrNodes( HIR pHir )
{
  if (fDbgLevel > 0) //##58
    ioRoot.dbgHir.print(2, "listUpPtrNodes", pHir.toStringShort());
  HIR lHir;
  ArrayList lPtrNodeList = new ArrayList(300);
  for ( HirIterator lIterator = hirRoot.hir.hirIterator(pHir);
        lIterator.hasNext(); ) {
    lHir = lIterator.next();
    if ((lHir != null)&&
        (lHir.getOperator() == HIR.OP_CONTENTS)&&
        (lHir.getChild1().getOperator() == HIR.OP_ADD)&&
        (lHir.getChild1().getChild1().getOperator() == HIR.OP_DECAY)&&
        (lHir.getChild1().getChild2().getOperator() == HIR.OP_MULT)&&
        (((HIR)lHir.getChild1().getChild1().getChild1()).getType() instanceof VectorType)) {
      lPtrNodeList.add(lHir);
    }
  }
  if (fDbgLevel > 3) //##58
    ioRoot.dbgHir.print(4, "contents-nodes list", lPtrNodeList.toString());
  return lPtrNodeList;
} // listUpPtrNodes

} // SubsPtrTransformation
