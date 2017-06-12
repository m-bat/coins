/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.sym.Sym;

/**
 *
 * Symbol node
 *
**/ 
public interface SymNode extends Exp {

/** getSymNodeSym
<PRE>
 *  Get the symbol attached as its symbol attribute.
 *  The correspondence between nodes and its symbol attribute is as follows:
 *    SubpNode    Subp
 *    VarNode     Var
 *    ElemNode    Elem
 *    ConstNode   Const
 *    TypeNode    Type
 *    LabelDef    Label
 *    LabelNode   Label
 *    SymNode     Program name if this node represents program symbol. 
 *                There may be other symbol when HIR is expanded.
 *  @return the symbol attached as its symbol attribute.
</PRE>
**/
Sym
getSymNodeSym();

/** setSymNodSym 
 *  Set pSym as the symbol of this node.
 *  (Unnecessary to use this method except for modifying HIR
 *   because symNode method sets it.)
**/
void
setSymNodeSym( Sym pSym );

}
