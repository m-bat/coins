/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.HirRoot;
import coins.ir.IrList;
import coins.sym.Label;
import coins.sym.Type;
import coins.sym.Var;

/** PhiExpImpl
 *  Phi expression class.
**/
public class
PhiExpImpl extends ExpImpl implements PhiExp
{

//====== Constructors ======//

public
PhiExpImpl( HirRoot pHirRoot, Var pVar, Label pLabel ) 
{
  super(pHirRoot, HIR.OP_PHI); 
  IrList lVarLabelPair = hirRoot.hir.irList(); 
  lVarLabelPair.add((Object)pVar);  
  lVarLabelPair.add((Object)pLabel); 
  fChildNode1 = hirRoot.hir.irList(); 
  ((HIR_Impl)fChildNode1).setParent(this); //##54
  ((IrList)fChildNode1).add(lVarLabelPair);
  fChildCount = 1;
  fType = pVar.getSymType();
}

//==== Methods ====//

public void
addAlternative( Var pVar, Label pLabel )   
{
  IrList lVarLabelPair = hirRoot.hir.irList(); 
  lVarLabelPair.add((Object)pVar);
  lVarLabelPair.add((Object)pLabel);
  ((IrList)fChildNode1).add(lVarLabelPair);
}

public IrList
getVarLabelList()  
{
  return (IrList)fChildNode1;
}

public Type
getVarType()
{
  return ((Var)((IrList)((IrList)fChildNode1).get(0)).get(0)).getSymType();
}

public void
accept( HirVisitor pVisitor )
{
  pVisitor.atPhiExp(this);
}

} // PhiExpImpl
