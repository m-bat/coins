/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.HirRoot;
import coins.ir.IR;

/**
 *
 * Information list node (pragma, comment line, etc.)
 *
**/
public class
InfNodeImpl extends HIR_Impl implements InfNode
{

public
InfNodeImpl( HirRoot pHirRoot, String pInfKindInterned, Object pInfObject )
{
  super(pHirRoot);
  fOperator = IR.OP_INF;
  fChildCount = 0;
  addInf(pInfKindInterned, pInfObject);
  fType = hirRoot.symRoot.typeVoid;
}


public Object
clone() {
  InfNodeImpl lTree;
  try {
    lTree = (InfNodeImpl)super.clone();
  }catch (CloneNotSupportedException e) {
   hirRoot.ioRoot.msgRecovered.put(1100, "CloneNotSupportedException(InfNode) "
                     + this.toString());
    return null;
  }
  return (Object)lTree;
} // clone

public String
toString() {
  if (fHirAnnex != null)
    return "inf" + fHirAnnex.toString(); //##62
  else
    return "inf"; //##62
}

public void
accept( HirVisitor pVisitor )
{
  pVisitor.atInfNode(this);
}

} // InfNodeImpl class
