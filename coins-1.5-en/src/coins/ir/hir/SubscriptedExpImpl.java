/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.HirRoot;
import coins.sym.PointerType;
import coins.sym.Sym; //##64
import coins.sym.Type;
import coins.sym.VectorType;

/**
 * Subscripted variable class.
**/
public class
SubscriptedExpImpl extends ExpImpl implements SubscriptedExp
{

/////////////////////////////////////// S.Fukuda 2002.6.17 begin
// (subs     int
//  <var      <VECT n int> array>
//  <var      int i>)
public
SubscriptedExpImpl( HirRoot pHirRoot, Exp pArrayExp, Exp pSubscript )
{
  super(pHirRoot, HIR.OP_SUBS);
  fChildCount = 2;
  setChildren(pArrayExp, pSubscript);
  fType = ((VectorType)pArrayExp.getType()).getElemType();
  //##64 BEGIN
  if (fType.getFlag(Sym.FLAG_UNFIXED_SIZE))
    hirRoot.ioRoot.msgWarning.put(
        "Element of SubscriptedExp is unfixed-size "
        + pArrayExp.toStringWithChildren());
  //##64 END
}
/////////////////////////////////////// S.Fukuda 2002.6.17 end
  // SubscriptedExpImpl with element-size is used when
  // SourceLanguage.subscriptWithIndex is true, that is
  // (subs arrayExp (index indexValueExp elemSizeExp)) representation is used.
  // This is not used by hir.subscriptedExp(arrayExp, subscriptExp, elemSizeExp). //##64
  public
  SubscriptedExpImpl( HirRoot pHirRoot, Exp pArrayExp, Exp pSubscript,
                      Exp pElemSize )
  {
    this(pHirRoot,pArrayExp,pSubscript);
  }

public Exp
getArrayExp() { return (Exp)fChildNode1; }

public Exp
getSubscriptExp()
{
  Exp lExp = null;
  if (fChildNode2 != null) {
      lExp = (Exp)fChildNode2;
  }
  return (Exp)lExp;
} // getSubscriptExp

public Exp
getElemSizeExp()
{
  Exp lExp = null;
  if (fChildNode2 != null) {
      Type lType = ((HIR)fChildNode1).getType();
      if (lType instanceof VectorType)
        lExp = ((VectorType)lType).getElemType().getSizeExp();
      else if (lType instanceof PointerType)
        lExp = ((PointerType)lType).getPointedType().getSizeExp();
  }
  return (Exp)lExp;
} // getElemSizeExp

public void
accept( HirVisitor pVisitor )
{
  pVisitor.atSubscriptedExp(this);
}

} // SubscriptedExpImpl class
