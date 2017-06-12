/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * BBlockStmtIterator.java
 *
 * Created on July 30, 2002, 4:05 PM
 */
package coins.flow; //##60

import coins.ir.IR;
import coins.ir.hir.HIR;
import coins.ir.hir.LabeledStmt;
import coins.ir.hir.Stmt;
import coins.ir.hir.StmtImpl;

/**
 * BBlockStmtIterator traverses statements in the specified BBlock.
 * IR tree should not be changed after instanciation of
 * BBlockStmtIteratorImpl until all invocations of
 *   hasNext() and next() are finished.
 */
public class BBlockStmtIterator
  implements BBlockSubtreeIterator //##25
{
  //##62 final static Stmt EOB = new StmtImpl();
  //##62 private Stmt fSubtreeNext = null; // Subtree that would be returned by the next call of next().
  BBlockHirSubtreeIteratorImpl fSubtreeIterator; //##62

  public BBlockStmtIterator(BBlockHir pBBlock) //##25
  {
    fSubtreeIterator = new BBlockHirSubtreeIteratorImpl(
        pBBlock.getSubpFlow().getFlowRoot(),pBBlock);
  }

  public boolean hasNext()
  {
    return fSubtreeIterator.hasNext();
  }

  public IR
    next()
  {
    //##63 return (HIR)fSubtreeIterator.next();
    HIR lNode = (HIR)fSubtreeIterator.next(); //##63
    while ((lNode != null)&&(! (lNode instanceof Stmt))) {
      if (fSubtreeIterator.hasNext())
        lNode = (HIR)fSubtreeIterator.next();
      else
        return null;
    }
    return lNode;
  }

}
