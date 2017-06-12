/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.HirRoot;
import coins.sym.Const;
import coins.sym.Sym;

/**
 *
 * Constant node
 *
**/
public class
ConstNodeImpl extends SymNodeImpl implements ConstNode
{

public
ConstNodeImpl( HirRoot pHirRoot, Const pConstSym ) 
{
  super(pHirRoot); 
  fOperator = HIR.OP_CONST;
  fSym  = (Sym)pConstSym;
  fType = pConstSym.getSymType();
  fChildCount = 0;
  setFlag(HIR.FLAG_CONST_EXP, true);
}

public Const
getConstSym()
{
  return ((Const)fSym).getConstSym();
}

public Sym
getSym() { return fSym; }

public int
getIntValue() {
  return ((Const)fSym).intValue();
}

public long
getLongValue() {
  return ((Const)fSym).longValue();
}

public boolean
isIntConst0() {
  if (fSym == hirRoot.symRoot.intConst0)  
    return true;
  else
    return false;
}

public boolean
isIntConst1() {
  if (fSym == hirRoot.symRoot.intConst1) 
    return true;
  else
    return false;
}

public boolean
isTrueConstNode() {
  if (fSym == hirRoot.symRoot.boolConstTrue) 
    return true;
  else
    return false;
}

public boolean
isFalseConstNode() {
  if (fSym == hirRoot.symRoot.boolConstFalse) 
    return true;
  else
    return false;
}

/*##
public String
toString() {
  String nodeString = super.toString();
  nodeString = nodeString + " " + fSym.getName();
  if (fType != null)
    nodeString = nodeString + " " + fType.getName();
  return nodeString;
} // toString
##*/

public void
accept( HirVisitor pVisitor )
{
  pVisitor.atConstNode(this);
}

} // ConstNodeImpl class
