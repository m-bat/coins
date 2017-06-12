/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import java.util.Iterator;
import java.util.ArrayList;


import coins.HirRoot;
import coins.IoRoot;
import coins.ir.IR;
import coins.ir.IrList;

/** HirIteratorImpl class
 *  Traverse HIR node in depth first order.
**/
public class
HirIteratorImpl implements HirIterator
{

//====== Fields ======//

  public final HirRoot
    hirRoot;         // Used for accessing Root information.

  public final IoRoot
    ioRoot;         // Used for accessing Root information.

  protected int
    fDbgLevel;

  protected ArrayList
    fNodeList = new ArrayList(1000);

  protected ArrayList
    fStmtList = new ArrayList(100);

  protected int
    fNodeCount = 0;

  protected int
    fStmtCount = 0;

  protected Iterator
    fNodeListIterator;

  protected Iterator
    fStmtListIterator;

  protected int
    fDebLevel;

/** HirIteratorImpl
 *  Create an itrerator that traverses nodes of HIR subtree.
 *  @param pSubtree Subtree to be traversed.
 *  @param pGoUpward true if traverse of nodes positioned
 *      higher than the root of pSubtree is permitted.
 *      Normally this is false; true is used in BBlockNodeIterator.
**/
public                           // Create an itrerator that traverse
HirIteratorImpl( HirRoot pHirRoot,  // nodes of HIR subtree,
          IR pSubtree, boolean pGoUpward )
{
  HIR lParent;
  hirRoot = pHirRoot;
  ioRoot  = pHirRoot.ioRoot;
  fDbgLevel = ioRoot.dbgHir.getLevel();
  if (fDbgLevel > 3)
    hirRoot.ioRoot.dbgHir.print(5, "HirIteratorImpl",
      ioRoot.toStringObjectShort(pSubtree));
  recordNodes((HIR)pSubtree);
  fNodeListIterator = fNodeList.iterator();
  fStmtListIterator = fStmtList.iterator();
} // HirIteratorImpl

//====== Methods to traverse nodes in this subtree ======//

public HIR
next()
{
  HIR lHir;
  if (fNodeListIterator.hasNext())
    lHir = (HIR)fNodeListIterator.next();
  else
    lHir = null;
  if (fDbgLevel > 3)
    ioRoot.dbgHir.print(6, "HirIterator.next",
      ioRoot.toStringObjectShort(lHir));
  return lHir;
} // next

public boolean
hasNext()
{
  return fNodeListIterator.hasNext();
} // hasNext

public boolean
hasNextStmt()
{
  return fStmtListIterator.hasNext();
} // hasNext

public HIR                 // Get the node that refer/set data or
getNextExecutableNode()    // change control flow directly.
{
  HIR     lNextNode = null;
  boolean lExecutable = false;
 search:
  for (lNextNode = next(); lExecutable == false;
       lNextNode = next()) {
    if (lNextNode != null) {
      switch (lNextNode.getOperator()) {
      case HIR.OP_PROG:
      case HIR.OP_SUBP_DEF:
      case HIR.OP_LABEL_DEF:
      case HIR.OP_INF:
      case HIR.OP_SUBP:
      case HIR.OP_TYPE:
      case HIR.OP_LABEL:
      case HIR.OP_LIST:
      case HIR.OP_EXPLIST:     //##51
      case HIR.OP_SEQ:
      case HIR.OP_LABELED_STMT:
      case HIR.OP_IF:
      case HIR.OP_WHILE:
      case HIR.OP_FOR:
      case HIR.OP_REPEAT:
      case HIR.OP_SWITCH:
      case HIR.OP_BLOCK:
      case HIR.OP_EXP_STMT:
      case HIR.OP_NULL:
        lExecutable = false;
        break;
      default:
        lExecutable = true;
        break search;        // Do not advance to the next.
      }
    }else {  // lNextNode is null.
      if (! hasNext())
        break;
    }
  }
  return lNextNode;
} // getNextExecutableNode

public Stmt      // Get the node that is an instance of Stmt
nextStmt()
{
  Stmt lStmt;
  if (fStmtListIterator.hasNext())
    lStmt = (Stmt)fStmtListIterator.next();
  else {
    ioRoot.msgError.
      put("HirIterator.nextStmt should be called after testing by hasNextStmt()");
    lStmt = null;
  }
  if (fDbgLevel > 3)
    ioRoot.dbgHir.print(6, "HirIterator.nextStmt",
      ioRoot.toStringObjectShort(lStmt));
  return lStmt;
} // nextStmt

public Stmt      // Get the node that is an instance of Stmt
getNextStmt()
{
  Stmt lStmt;
  if (fStmtListIterator.hasNext())
    lStmt = (Stmt)fStmtListIterator.next();
  else {
    ioRoot.msgRecovered.put(
      "HirIterator.getNextStmt should be called after testing by hasNextStmt()");
    if (fDbgLevel > 0) {
      ioRoot.dbgHir.print(2, "HirIterator",
        "getNextStmt should be called after testing by hasNextStmt()");
  }
    while (hasNext()) {
      HIR lHirTemp = next();
    }
    lStmt = null;
  }
  if (fDbgLevel > 3)
    ioRoot.dbgHir.print(6, "HirIterator.nextStmt",
      ioRoot.toStringObjectShort(lStmt));
  return lStmt;
} // nextStmt

protected void
recordNodes(HIR pHir)
{
    if (fDbgLevel > 3)
      hirRoot.ioRoot.dbgHir.print(6, "recordNodes",
        pHir.toStringShort()+ " " + fNodeCount);
    fNodeList.add(fNodeCount, pHir);
    fNodeCount++;
    if (pHir instanceof Stmt) {
      if (fDbgLevel > 3)
        hirRoot.ioRoot.dbgHir.print(6, " stmt" + fStmtCount);
      fStmtList.add(fStmtCount, pHir);
      fStmtCount++;
    }
    if (pHir.getChildCount() == 0) {
      if (pHir instanceof HirList) {
        HirList lHirList = (HirList)pHir;
        int lListSize = lHirList.size(); //##64
        int lIndex = 0; //##64
        for (int lElemIndex = 0; lElemIndex < lHirList.size(); lElemIndex++) {
          HIR lElem = (HIR)lHirList.get(lElemIndex);
          lIndex++; //##64
          if (lIndex > lListSize) //##64
            break; //##64
          if (lElem != null)
            recordNodes(lElem);
        }
      }else if (pHir instanceof ExpListExp) {
        ExpListExp lExpListExp = (ExpListExp)pHir;
        for (int lElemIndex = 0; lElemIndex < lExpListExp.size(); lElemIndex++) {
          HIR lElem = (HIR)lExpListExp.getExp(lElemIndex);
          if (lElem != null)
            recordNodes(lElem);
         }
      }
    }else {
      if (pHir instanceof BlockStmt) {
        BlockStmt lBlockStmt = (BlockStmt)pHir;
        for (Stmt lStmt = lBlockStmt.getFirstStmt(); lStmt != null;
             lStmt = lStmt.getNextStmt()) {
          recordNodes(lStmt);
        }
      }else {
        for (int lChildNumber = 1; lChildNumber <= pHir.getChildCount();
             lChildNumber++) {
          HIR lChild = (HIR)pHir.getChild(lChildNumber);
          if (lChild != null) {
            recordNodes(lChild);
          }
        }
      }
    }
   } // recordNodes

} // HirIteratorImpl

