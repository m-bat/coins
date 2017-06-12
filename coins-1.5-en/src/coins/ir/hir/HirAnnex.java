/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.HirRoot;
import coins.HasStringObject;
import coins.ir.IrList;
import coins.ir.IrListImpl;
import coins.sym.ExpId;
import coins.sym.FlagBox;
import coins.sym.FlagBoxImpl;
import coins.sym.FlowAnalSym;
import coins.sym.Sym;
import java.util.Iterator; //##70

/** class HirAnnex
 *  Additional information that is not given in some case
 *  and not given to some nodes.
**/
public class
HirAnnex
{
  public final HirRoot
    hirRoot;         // Used for accessing HIR information.

  protected FlagBox  // Flags attached to the HIR node that has
    fFlagBox;        // this annex (as for flags, see HIR).

  protected int      // Node index number used in flow analysis,
    fIndexNo;        // etc.

  protected ExpId    // Expression identifier used in
    fExpId = null;          // data flow analysis.

  protected IrList    // List of information to be attached.
    fInfList = null;  // It is a list of pairs infKind and infBody
                      // where infKind is an interned string showing information
                      // kind, and infBody is any object enclosed in a list.
                      // As for infKind, see coins.Registry.
                      // Example:
                      //   ( "coins_comment" ("comment string")
                      //     "coins_pragma"  ( pragmaObject ) )
    // fInfList is not HirList because it may contain Sym, Const, and String
    // which are not HIR object.

  protected Object   // Infromation to be attached to the HIR node
    fWork;           // having this annex.  The information is
                     // used for arbitrary purpose in each phase
                     // and may be destroyed by other phases.

public
HirAnnex( HirRoot pHirRoot )
{
  hirRoot = pHirRoot;
} // HirAnnex

/**
 *  getFlag returns the value (true/false) of the flag indicated
 *  by pFlagNumber.
 *  @param pFlagNumber flag identification number.
 *      As for detail, see getFlag of HIR.
**/
boolean
getFlag( int pFlagNumber)
{
  if (fFlagBox == null)
    return false;
  else
    return fFlagBox.getFlag(pFlagNumber);
} // getFlag

/** setFlag
 *  setFlag sets the flag of specified number.
 *  @param pFlagNumber flag identification number.
 *      As for detail, see getFlag of HIR.
 *  @param pYesNo true or false to be set to the flag.
**/
void
setFlag( int pFlagNumber, boolean pYesNo)
{
  if (fFlagBox == null)
    fFlagBox = (FlagBox)(new FlagBoxImpl());
  fFlagBox.setFlag(pFlagNumber, pYesNo);
} // setFlag

/**
 * @return true if all flags are false,
 *   else return false.
 */
public boolean
flagsAreAllFalse()
{
  if (fFlagBox == null)
    return true;
  else
    return fFlagBox.allFalse();
}

/**
 * Get the flag box attached to this annex.
 * See also the flags FLAG_xx defined in HIR (HIR0).
 * @return the flag box.
 */
public FlagBox
getFlagBox()
{
  return fFlagBox;
}

/**
 * Add pInfObject as the information attached to this annex.
 * Its information kind is pInfKindInterned.
 * @param pInfKindInterned String constant showing the kind
 *     of information (it should be unique (intern()).
 * @param pInfObject Information to be attached.
 */
public void
addInf( String pInfKindInterned, Object pInfObject )
{
  IrList lObjList; //##70
  //##70 BEGIN
  if ((pInfObject instanceof IrList)||(pInfObject instanceof HirList)) {
    lObjList = (IrList)pInfObject;
  }else {
  //##70 END
    lObjList = (IrList)(new IrListImpl(hirRoot)); //##70
    lObjList.add(pInfObject); //##70
  }
  //##70 lObjList.add(pInfObject);
  if (fInfList == null) {
    fInfList = new IrListImpl(hirRoot);
    fInfList.add(pInfKindInterned);
    fInfList.add(lObjList);
  }else if (fInfList.contains(pInfKindInterned)) {
    lObjList = (IrList)fInfList.get(fInfList.indexOf(pInfKindInterned)+1);
    //##92 lObjList.remove(0);
    lObjList.set(0, pInfObject);
  }else {
    fInfList.add(pInfKindInterned);
    fInfList.add(lObjList);
  }
} // addInf

/**
 * Get the information of the kind pInfKindInterned.
 * @param pInfKindInterned shows the kind of information
 *     (it should be unique (intern()).
 * @return the information (null if not attached).
 */
public Object
getInf( String pInfKindInterned )
{
  // getInf may return non-HIR elements such as
  // Sym, Const, String.
  IrList lInfList;
  int    lIndex;
  if (fInfList == null)
    return null;
  lIndex   = fInfList.indexOf(pInfKindInterned);
  if (lIndex < 0) //##62
    return null;  //##62
  lInfList = (IrList)fInfList.get(lIndex+1);
  if (lInfList == null)
    return null;
  else {
    if (lInfList.isEmpty()) //##70
      return null; //##70
    //##70 return (Object)lInfList.get(0); //## REFINE.
    // This should be "return (Object)lInfList;"
    return (Object)lInfList; //##70
  }
} // getInf

/**
 * Remove the information of the kind pInfKindInterned.
 * @param pInfKindInterned show the kind of informaiotn.
 */
public void
removeInf( String pInfKindInterned )
{
  IrList lInfList;
  int    lIndex;
  if (fInfList == null)
    return;
  lIndex   = fInfList.indexOf(pInfKindInterned);
  if (lIndex >= 0) {
    fInfList.remove(lIndex+1);
    fInfList.remove(lIndex);
  }
}

/**
 * Get the list of information.
 * @return the list of information.
 */
public IrList
getInfList()
{
    return fInfList;
}

/**
 * Set pWork as the information that is used for
 * arbitrary purpose in each phase.
 * The information in work may be destroyed by other phases.
 * @param pWork represents the information to be set.
 */
public void
setWork( Object pWork )
{
  fWork = pWork;
}

/**
 * Get the information in work set by setWork.
 * @return the information in work.
 */
public Object
getWork(){
  return fWork;
}

/**
 * Get the index number attached to the HIR node
 * correponding to this annex.
 * @return the index number.
 */
public int
getIndex()
{
  return fIndexNo;
}

/**
 * Set pIndex as the index number attached to the HIR node
 * corresponding to this annex.
 * @param pIndex index number to be set.
 */
public void
setIndex( int pIndex )
{
  fIndexNo = pIndex;
}

/* //##78
public ExpId
getExpId( )
{
  return fExpId;
}

public void
setExpId( ExpId pId )
{
  fExpId = pId;
}

public FlowAnalSym
getFlowAnalSym( )
{
  return fExpId;
}

public void
setFlowAnalSym( ExpId pId )
{
  fExpId = (ExpId)pId;
}
*/ //##78

/** clone
 * Override Object.clone in HIR.
 * @return cloned HirAnnex.
 * @exception CloneNotSupportedException
**/
public Object
clone() throws CloneNotSupportedException {
  HirAnnex lAnnex;
  try {
    lAnnex = (HirAnnex)super.clone();
    if (fFlagBox != null)
      lAnnex.fFlagBox = (FlagBox)(new FlagBoxImpl());
    lAnnex.fIndexNo = 0;
    lAnnex.fExpId = fExpId;
    lAnnex.fWork = fWork; //##62
    if (hirRoot.ioRoot.dbgHir.getLevel() > 4)
      hirRoot.ioRoot.dbgHir.print(6, " lAnnex.work " + lAnnex.fWork);
    if (fInfList != null)
      lAnnex.fInfList = (IrList)((HIR)fInfList).copyWithOperands();
  }catch (CloneNotSupportedException e) {
    // ioRoot.msgRecovered.put(1100,
    //   "CloneNotSupportedException(HirAnnex) " + this.toString());
    return null;
  }
  return lAnnex;
} // clone

public String
toString() {
  StringBuffer lBuffer = new StringBuffer(" " + fIndexNo); //##62
  if (fExpId != null)
    lBuffer.append(" ").append(fExpId.getName());
  if (fFlagBox != null)
    lBuffer.append(" " + fFlagBox.toString()); //##62
  if (fInfList != null) {
    lBuffer.append(toStringInfList(fInfList));
  }
  return lBuffer.toString();
} // toString

/**
 * Get the information attached to this annex
 * in the form of String.
 * @return the information changed to String.
 */
public String
toStringInf() {
  StringBuffer lBuffer = new StringBuffer("inf "); //##62
  if (fInfList != null)
    lBuffer.append(toStringInfList(fInfList)); //##62
  return lBuffer.toString();
} // toString

//##62 BEGIN
/**
 * Change the list of information to String.
 * @param pInfList list of information.
 * @return the String showing the information.
 */
protected String
  toStringInfList( IrList pInfList )
{
  StringBuffer lBuffer = new StringBuffer();
  Object lObject;
  lBuffer.append(" (list ");
  for (java.util.Iterator listIterator = pInfList.iterator();
       listIterator.hasNext(); ) {
    lObject = (Object)listIterator.next();
    if (lObject instanceof String)
      lBuffer.append((String)lObject + " ");
    else if (lObject instanceof IrList) {
      lBuffer.append(toStringInfList((IrList)lObject)).append(" ");
    }else if (lObject instanceof HirList) {
      lBuffer.append(toStringInfList((IrList)lObject)).append(" "); // Infinite loop ?
    }else if (lObject instanceof HIR) {
      lBuffer.append("<"+((HIR)lObject).toStringShort()+"> ");
    }else if (lObject instanceof Sym) {
      lBuffer.append(((Sym)lObject).getName()+" ");
    }else if (lObject instanceof HasStringObject) {
      lBuffer.append(((HasStringObject)lObject)
        .toString()).append(" ");
    //##70 BEGIN
    }else if (lObject == null) {
      continue;
    }else {
      lBuffer.append(" "+ lObject + " " + lObject.getClass());
    //##70 END
    }

  }
  lBuffer.append(")");
  return lBuffer.toString();
} // toStringInflist
//##62 END

} // HirAnnex class

