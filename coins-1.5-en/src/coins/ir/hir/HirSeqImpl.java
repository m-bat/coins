/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.HirRoot;
import coins.ir.IR;

/** class HirSeqImpl
 *  Sequence of expressions, symbols, and others.
**/
public class
HirSeqImpl extends HIR_Impl implements HirSeq
{

public
HirSeqImpl( HirRoot pHirRoot, HIR pChild1 ) 
{
  super(pHirRoot, HIR.OP_SEQ); 
  fChildCount = 1;
  setChild1(pChild1);
  fType = pChild1.getType();
} // HirSeqImpl

public
HirSeqImpl( HirRoot pHirRoot, HIR pChild1, HIR pChild2 ) 
{
  super(pHirRoot, HIR.OP_SEQ); 
  fChildCount = 2;
  setChildren(pChild1, pChild2);
  if (pChild1.getType() == pChild2.getType())
    fType = pChild1.getType();
  else
    fType = hirRoot.symRoot.typeVoid; 
} // HirSeqImpl

public
HirSeqImpl( HirRoot pHirRoot, HIR pChild1, HIR pChild2, HIR pChild3 ) 
{
  super(pHirRoot, HIR.OP_SEQ);
  fAdditionalChild = new HIR[1];
  fChildCount = 3;
  setChildren(pChild1, pChild2, pChild3);
  if ((pChild1.getType() == pChild2.getType())&&
      (pChild2.getType() == pChild3.getType()))
    fType = pChild1.getType();
  else
    fType = hirRoot.symRoot.typeVoid; 
} // HirSeqImpl

public
HirSeqImpl( HirRoot pHirRoot, HIR pChild1, HIR pChild2, HIR pChild3,
            HIR pChild4 ) 
{
  super(pHirRoot, HIR.OP_SEQ);
  fAdditionalChild = new HIR[2];
  fChildCount = 4;
  setChildren(pChild1, pChild2, pChild3, pChild4);
  if ((pChild1.getType() == pChild2.getType())&&
      (pChild2.getType() == pChild3.getType())&&
      (pChild3.getType() == pChild4.getType()))
    fType = pChild1.getType();
  else
    fType = hirRoot.symRoot.typeVoid; 
} // HirSeqImpl

public
HirSeqImpl( HirRoot pHirRoot, HIR pChild1, HIR pChild2, HIR pChild3, 
            HIR pChild4, HIR pChild5 )
{
  super(pHirRoot, HIR.OP_SEQ);
  fAdditionalChild = new HIR[3];
  fChildCount = 5;
  setChildren(pChild1, pChild2, pChild3,
               pChild4, pChild5);
  if ((pChild1.getType() == pChild2.getType())&&
      (pChild2.getType() == pChild3.getType())&&
      (pChild3.getType() == pChild4.getType())&&
      (pChild4.getType() == pChild5.getType()))
    fType = pChild1.getType();
  else
    fType = hirRoot.symRoot.typeVoid; 
} // HirSeqImpl

public
HirSeqImpl( HirRoot pHirRoot, IR pChild1 ) 
{
  super(pHirRoot, HIR.OP_SEQ); 
  fChildCount = 1;
  setChild1(pChild1);
  if (pChild1 instanceof HIR)
    fType = ((HIR)pChild1).getType();
  else
    fType = hirRoot.symRoot.typeVoid;
} // HirSeqImpl

public
HirSeqImpl( HirRoot pHirRoot, IR pChild1, IR pChild2 ) 
{
  super(pHirRoot, HIR.OP_SEQ); 
  fChildCount = 2;
  setChildren(pChild1, pChild2);
  if ((pChild1 != null)&&(pChild2 != null)&&
      (pChild1 instanceof HIR)&&(pChild2 instanceof HIR)&&
      (((HIR)pChild1).getType() == ((HIR)pChild2).getType()))
    fType = ((HIR)pChild1).getType();
  else
    fType = hirRoot.symRoot.typeVoid; 
} // HirSeqImpl

public void 
accept( HirVisitor pVisitor )
{
  pVisitor.atHirSeq(this);
}

public String
toString() {
  StringBuffer lString = new StringBuffer();
  lString.append("(seq " + getIndex());
  if (getChild1() != null)
    lString.append(" ").append(getChild1().toString());
  if ((fChildCount >= 2)&&(getChild2() != null))
    lString.append(" ").append(getChild2().toString());
  if ((fChildCount >= 3)&&(getChild(3) != null))
    lString.append(" ").append(getChild(3).toString());
  if ((fChildCount >= 4)&&(getChild(4) != null))
    lString.append(" ").append(getChild(4).toString());
  if ((fChildCount >= 5)&&(getChild(5) != null))
    lString.append(" ").append(getChild(5).toString());
  lString.append(")");
  return lString.toString();
} // toString

/*****
public void
print(int pIndent, boolean pDetail)
{
  String lineImage, lSpace;
  lSpace = hirRoot.ir.getIndentSpace(pIndent);
  if (pDetail) {
    lineImage = lSpace + "(seq " + getType().toString();
  }else {
    lineImage = lSpace + "(seq ";
  }
    hirRoot.ioRoot.printOut.print("\n" + lineImage); 
    if (fChildNode1 != null)
       ((HIR)fChildNode1).print( pIndent+1, pDetail );
    else
      hirRoot.ioRoot.printOut.print("\n" + lSpace + "<null 0 void>"); 
    if (fChildCount >= 2) {
      if (fChildNode2 != null)
        ((HIR)fChildNode2).print( pIndent+1, pDetail );
      else
        hirRoot.ioRoot.printOut.print("\n" + lSpace + "<null 0 void>"); 
      for (int i = 2; i < fChildCount; i++) {
        if (fAdditionalChild[i-2] != null)
          ((HIR)fAdditionalChild[i-2]).print(pIndent+1, pDetail);
        else
          hirRoot.ioRoot.printOut.print("\n" + lSpace + "<null 0 void>"); 
      }
    }
    hirRoot.ioRoot.printOut.print(")");
}
******/

} // HirSeqImpl class

