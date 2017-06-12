/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.flow;

import java.util.ArrayList;
import java.util.Set; //##63
import java.util.HashSet; //##63

import coins.FlowRoot;
import coins.IoRoot;
import coins.ir.IR;
import coins.ir.hir.BlockStmt;
import coins.ir.hir.ExpStmt;
import coins.ir.hir.HIR;
import coins.ir.hir.IfStmt;
import coins.ir.hir.LabeledStmt;
import coins.ir.hir.LoopStmt;
import coins.ir.hir.Stmt;
import coins.ir.hir.SwitchStmt;
import coins.sym.Label;

/** BBlockHirSubtreeIteratorImpl class
 *
 *  Basic block subtree iterator
 *  to traverse HIR top-subtrees in a basic block.
 **/
public class
  BBlockHirSubtreeIteratorImpl
  extends BBlockSubtreeIteratorImpl //##65
  implements BBlockSubtreeIterator
{

//====== Fields ======

//====== Constructors ======

  public
    BBlockHirSubtreeIteratorImpl(
    FlowRoot pFlowRoot,
    BBlock pBBlock)
  {
    super(pFlowRoot, pBBlock); //##65
  } // BBlockHirSubtreeIteratorImpl

//====== Methods to traverse subtrees in a basic block ======


} // BBlockHirSubtreeIteratorImpl class
