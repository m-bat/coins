/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.HirRoot;

/**
 *
 * Null node
 *
**/
public class
NullNodeImpl extends ExpImpl implements NullNode
{

    /* constructor */
public
NullNodeImpl( HirRoot pHirRoot )
{
    super(pHirRoot, HIR.OP_NULL);
    fChildCount = 0;
    fType = hirRoot.symRoot.typeVoid;
}

public void
accept( HirVisitor pVisitor )
{
  pVisitor.atNullNode(this);
}

}
