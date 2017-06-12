/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;

import coins.SymRoot;
import coins.ir.IrList;
import coins.ir.IrListImpl;
import coins.ir.hir.HIR;
import coins.ir.hir.HirList;
import coins.ir.hir.LabelNode;
import coins.ir.hir.LabeledStmt;

/** class Label */

public class
//##60 LabelImpl extends OperandSymImpl implements Label
LabelImpl extends SymImpl implements Label //##60
{

//==== Fields ====//

  coins.ir.IrList fHirRefList = null;
  coins.ir.IrList fLirRefList = null;
  LabeledStmt fHIRpos = null; // LabeledStmt defining this label.
  int    fLabelKind  = 0;
  HIR    fOriginHir  = null;
  coins.aflow.BBlock fBBlock = null; // Basic block corresponding to this label.

//==== Constructor ====//

public
LabelImpl( SymRoot pSymRoot, String pLabelName, Sym pDefinedIn )
{
  super(pSymRoot);
  fKind = Sym.KIND_LABEL;
  fName = pLabelName;
  fDefinedIn = pDefinedIn;
}

//==== Methods ====//

/** getHirPosition
 *  Get the HIR statement having this label.
 *  @return HIR statement to which this is defined as its label.
 */
public LabeledStmt
getHirPosition() {
  return fHIRpos;
}

/**setHirPosition
 *  Set the HIR statement having this label.
  *  @param pHirPosition HIR statement to which this is defined as its label.
 *  set pHirPosition as the HIR position of this label.
 */
public void
setHirPosition( LabeledStmt pHirPosition ) {
  fHIRpos = pHirPosition;
}

/** getHirRefList
 *  Get reference list of this label.
 *  @return IrList the list of LabelNodes refering this label.
**/
public coins.ir.IrList
getHirRefList() {
  if (fHirRefList == null) //##76
    fHirRefList = (coins.ir.IrList)(new IrListImpl(symRoot.getHirRoot())); //##76
  return fHirRefList;
}

/**
 * Reset HirRefList that shows the list of LabelNodes refering this label.
 *  This method is called from JumpStmt, SwitchStmt and
 *  buildLabelRefList of Subp.
 *  It is not recommended to be used from other modules.
 */
public void
resetHirRefList( )
{
  fHirRefList = null;
}

/**  addToHirRefList
 *  Add reference list of this label.
 *  This method is called from JumpStmt, SwitchStmt and
 *  buildLabelRefList of Subp.
 *  It is not recommended to be used from other modules.
 *  @param pHirRefPosition HIR refenence node of this label.
 *  add pHirRefPosition to the reference list of this label.
 *  If there is no HIR reference, return null.
**/
public void
addToHirRefList( LabelNode pHirRefPosition ) {
  if (fHirRefList == null)
    fHirRefList = (coins.ir.IrList)(new IrListImpl(symRoot.getHirRoot()));
  fHirRefList.add( pHirRefPosition );
}

////////////////////SF031111[
/**
 * Remove LabelNode from HirRefList.
 *  This method is called from JumpStmt, SwitchStmt and
 *  buildLabelRefList of Subp.
 *  It is not recommended to be used from other modules.
 * @param labelnode
 * @return true if removed, false if not.
 */
public boolean
removeFromHirRefList(LabelNode labelnode){
  if( fHirRefList!=null )
    return fHirRefList.remove(labelnode);
  return false;
}
////////////////////SF031111]

    /**  getLirRefList
     *  Getd reference list of this label.
     *  @return the list of LIR instructions refering this label.
     **/
/*#
    public coins.ir.IrList getLirRefList() {
        return fLirRefList;
    }
#*/

    /**
     *  addToLirRefList
     *  add reference list of this label.
     *  @param pLirRefPosition LIR refenence statement of this label.
     *  addToLirRefList add pLirRefPosition to the reference list of this label.
     *  If there is no LIR reference, return null.
     **/
/*#
    public void addToLirRefList( LIR pLirRefPosition ) {
        fLirRefList.appendData( pLirRefPosition );
        fLirRefCount++;
    }
#*/

public int
getHirRefCount()
{
  if (fHirRefList != null)
    return fHirRefList.size();
  else
    return 0;
}

public coins.aflow.BBlock
getBBlock() { return fBBlock; }

public void
setBBlock( coins.aflow.BBlock pBBlock ) { fBBlock = pBBlock; }

public int
getLabelKind() { return fLabelKind; }

public void
setLabelKind( int pLabelKind ) { fLabelKind = pLabelKind; }

public boolean
endPointLabel() {
  if (fLabelKind >= Label.END_IF_LABEL)
    return true;
  else
    return false;
}

public HIR
getOriginHir() { return fOriginHir; }

public void
setOriginHir( HIR pOriginHir ) { fOriginHir = pOriginHir; }

public void
replaceHirLabel( Label pToLabel )
{
  java.util.ListIterator lIterator;
  LabelNode lLabelNode, lNewLabelNode;
  HIR       lParent;
  int       lChildNumber, lIndex;
  if (fDbgLevel > 0) //##58
    symRoot.ioRoot.dbgSym.print(3, "replaceHirLabel from " + getName(),
      "to " + pToLabel.getName());
  if ((fHirRefList != null)&&(this != pToLabel)) {
    // Copy the list in order to escape from
    // java.util.ConcurrentModificationException.
    IrList lHirRefList = (coins.ir.IrList)(new IrListImpl(symRoot.getHirRoot()));
    for (lIterator = fHirRefList.iterator(); lIterator.hasNext(); ) {
      lLabelNode = (LabelNode)lIterator.next();
      lHirRefList.add(lLabelNode);
    }
    for (lIterator = lHirRefList.iterator(); lIterator.hasNext(); ) {
      lLabelNode = (LabelNode)lIterator.next();
      lNewLabelNode = symRoot.getHirRoot().hir.labelNode(pToLabel);
      lParent = (HIR)lLabelNode.getParent();
      if (lParent != null) {
        if (lParent instanceof HirList) {
          lIndex = ((HirList)lParent).indexOf(lLabelNode);
          if (lIndex >= 0) {
            ((HirList)lParent).remove(lLabelNode);
            ((HirList)lParent).add(lIndex, lNewLabelNode);
          }else {
            symRoot.ioRoot.msgRecovered.put(1025,
              "replaceHirLabel han no instance of " +
                 lLabelNode.toString());
          }
        }else {
          lChildNumber = lLabelNode.getChildNumber();
          lParent.replaceSource(lChildNumber, lNewLabelNode);
        }
      }else {
        symRoot.ioRoot.msgRecovered.put(1025,
              "replaceHirLabel han no no parent for " +
                 lLabelNode.toString());
      }
     pToLabel.getHirRefList().add(lNewLabelNode);
     fHirRefList.remove(lLabelNode);
    }
  }
} // replaceLabel

public String
toString()
{
  String lString = super.toString();
  lString = lString + " kind " + fLabelKind;
  if (fBBlock != null)
    lString = lString + " BBlock " + fBBlock.getBBlockNumber();
  return lString;
} // toString

public String
toStringDetail()
{
  String lString = super.toStringDetail();
  if (fHirRefList != null)
    lString = lString + " refered " + fHirRefList.toStringShort();
  else if (fLirRefList != null)
    lString = lString + " refered " + fLirRefList.toString();
  return lString;
} // toStringDetail

} // LabelImpl
