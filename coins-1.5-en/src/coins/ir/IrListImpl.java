/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir;

import java.util.LinkedList;

import coins.HasStringObject;
import coins.HirRoot;
import coins.ir.hir.HIR;
import coins.ir.hir.HirAnnex;
import coins.sym.FlowAnalSym;
import coins.sym.Sym;
import coins.sym.Const; //##70

/** class IrListImpl
 *  List of expressions, symbols, and others.
 *  LinkedList in java.util is wrapped so that it can be
 *  treated as IR node and IR methods can be applied.
 *  This class may print messages to System.out
 *  instead of using put of Message because hirRoot/lirRoot
 *  might be null.
**/
public class
IrListImpl implements IrList
{

  public final  HirRoot hirRoot;  // Used for accessing Root variables.
                                  // May be null for LIR list.

  protected int fOperator = IR.OP_LIST;  // Operation code

  private LinkedList fList;  // List of objects.
                    // LinkedList methods can be applied.

  protected HirAnnex fHirAnnex = null; // Optional information.
                                       // May be null.

  protected IR  fParent;  // Parent of this list.

/** IrList constructor foe HIR
 *  Wrap pList so that it can be treated as IR node.
 *  @param pList LinkedList that may contain any objects.
 *      If pList is null, an empty LinkedList is created.
**/
public
IrListImpl( HirRoot pHirRoot, LinkedList pList )
{
  hirRoot = pHirRoot;
  if (pList == null)
    fList = new LinkedList();
  else
    fList = pList;
} // IrList

public
IrListImpl( HirRoot pHirRoot)
{
  hirRoot = pHirRoot;
  fList = new LinkedList();
} // IrList

public void
add( Object pElement ) { fList.add(pElement); }

public void
add( int pInsertionPosition, Object pObjectToBeInserted ) {
    fList.add(pInsertionPosition, pObjectToBeInserted); }

public Object
getFirst()
{
  if (isEmpty())
    return null;
  else
    return fList.get(0);
}

public Object
get( int pIndex ) { return fList.get(pIndex); }

public void
set( int pIndex, Object pElement ) {
  fList.set(pIndex, pElement);
}

public boolean
isEmpty() { return fList.isEmpty(); }

public int
size() { return fList.size(); }

public boolean
contains( Object pObject ) { return fList.contains(pObject); }

public int
indexOf( Object pObject ) { return fList.indexOf(pObject); }

public Object
remove( int pRemovePosition ) { return fList.remove(pRemovePosition); }

//##77 BEGIN
public void
clear()
{
  fList.clear();
} // removeAll
//##77 END

public boolean
remove( Object pObject ) { return fList.remove(pObject); }

public java.util.ListIterator
iterator() { return fList.listIterator(); }

public Object
clone() throws ClassCastException {
  Object          lObject = null;
  HasStringObject lHasStringObject;
  LinkedList      lList;
  IrListImpl lTree;
  try {
    lTree = (IrListImpl)super.clone();
  }catch (CloneNotSupportedException e) {
    System.out.println( "1100 CloneNotSupportedException(IrListImpl) "
                + e.getMessage() + " " + this.toString());
    return null;
  }
  return (Object)lTree;
} // clone

public IR
getClone()throws CloneNotSupportedException
{
  return (IR)this.clone();
}

public Object
getWork()
{
  if (fHirAnnex == null)
    return null;
  else
    return fHirAnnex.getWork();
}

public void
setWork( Object pWork )
{
  if (fHirAnnex == null)
    fHirAnnex = new HirAnnex(hirRoot);
  fHirAnnex.setWork(pWork);
}

public String
getIrName()
{
  String  lStr;
  lStr = toString();
  if (getIndex() > 0)
    lStr = lStr + "[" + getIndex() + "]";
  return lStr;
} // getIrName


//===================================================

//==== Access methods available to build IR nodes. ====//

public IR
buildSymNode( Sym pSym ) { return null; }

public IR
buildNode( int pOperator ) { return null; }

public IR
buildNode( int pOperator, IR pSource1 ) { return null; }

public IR
buildNode( int pOperator, IR pSource1, IR pSource2 ) { return null; }

//====== Methods to link child to IR node ======//

//==== Methods to set/get informatin to/from IR node ====//

public int
getOperator() { return IR.OP_LIST; }

public IR
getParent()
{
  return null;
}

public void
setParent( IR pParent )
{
  fParent = pParent;
}

public int
getChildCount() { return 0; }

public IR
getChild1() { return null; }

public IR
getChild2() { return null; }

public IR
getChild( int pNumber ) { return null; }

public void
setChild1( IR p1 ) { }

public void
setChild2( IR p2 ) { }

public void
setChild( int pNumber, IR pIr ) { }

public void
addInf( String pInfKindInterned, Object pInfObject )
{
  if (fHirAnnex == null)
    fHirAnnex = new HirAnnex(hirRoot);
  fHirAnnex.addInf(pInfKindInterned, pInfObject);
} // addInf

public Object
getInf( String pInfKindInterned)
{
  if (fHirAnnex == null)
    return null;
  else
    return fHirAnnex.getInf(pInfKindInterned);
}

public void
removeInf( String pInfKindInterned)
{
  if (fHirAnnex != null)
    fHirAnnex.removeInf(pInfKindInterned);
}

public IrList
getInfList()
{
  if (fHirAnnex != null)
    return fHirAnnex.getInfList();
  else
    return null;
}

public int
getIndex(){
  if (fHirAnnex == null)
    return 0;
  else
    return fHirAnnex.getIndex();
}

public void
setIndex( int pIndex )
{
  if (fHirAnnex == null)
    fHirAnnex = new HirAnnex(hirRoot);
  fHirAnnex.setIndex(pIndex);
}

//======================================================//
//  Following methods require special attention to keep
//  consistency between internal representations.
//======================================================//

public IR
getSourceNode1() { return null; }

public IR
getSourceNode2() { return null; }

public IR
getSourceNode( int pNumber ) { return null; }

public Sym
getSym()
{
    return null;
}

public FlowAnalSym
getFlowAnalSym()
{
  return null;
}

public Sym
getResultOperand() { return null; }

public void
replaceSource1( IR pOperand ) { }

public void
replaceSource2( IR pOperand ) { }

public void
replaceSource( int pNumber, IR pOperand ) { }

public void
replaceResultOperand( IR pOperand ) { }

public void
replaceThisNode( IR pNewNode ) { }

public void
replaceOperator( int pOperator ) { }


//====== Methods for printing ======//

public String
getIndentSpace( int pIndent ) { return null; }


public String
toString()
{
  Object lObject;
  StringBuffer lString = new StringBuffer();
  if (fList != null) {
    lString.append("( "); //##70
    for (java.util.Iterator listIterator = fList.iterator();
         listIterator.hasNext(); ) {
      lObject = (Object)listIterator.next();
      if (lObject instanceof String)
        lString.append((String)lObject + " ");
      else if (lObject instanceof Const)
        lString.append(((Const)lObject).getName() + " ");
      else if (lObject instanceof Sym)
        lString.append(((Sym)lObject).getName() + " ");
      else if (lObject instanceof IrList)
        lString.append(((IrList)lObject).toString() + " ");
      else if (lObject instanceof HasStringObject)
        lString.append(((HasStringObject)lObject)
                      .toString()).append(" ");
    }
    lString.append(")"); //##70
  }
  return lString.toString().intern();
} // toString

public String
toStringShort()
{
  Object lObject;
  StringBuffer lString = new StringBuffer();
  if (fList != null) {
    for (java.util.Iterator listIterator = fList.iterator(); //##8
         listIterator.hasNext(); ) {
      lObject = (Object)listIterator.next();
      if (lObject instanceof String)
        lString.append((String)lObject + " ");
      else if (lObject instanceof HIR)
        lString.append(((HIR)lObject).toStringShort()).append(" ");
      else if (lObject instanceof Sym)
        lString.append(((Sym)lObject).toStringShort()).append(" ");
      else if (lObject instanceof HasStringObject)
        lString.append(((HasStringObject)lObject)
                      .toString()).append(" ");
    }
  }
  return lString.toString().intern();
} // toStringShort

public void
print(int pIndent)
{
  print(pIndent, false);
} // print

public void
print(int pIndent, boolean pDetail ) {
  String lSpace = hirRoot.hir.getIndentSpace(pIndent);
  if (fList != null) {
    System.out.print("\n" + lSpace + "(list ");
    for (java.util.Iterator listIterator = fList.iterator();
         listIterator.hasNext(); ) {
      ((HasStringObject)listIterator.next()).print(pIndent+1);
    }
    System.out.print(")");
  }else
    System.out.print("\n" + lSpace + "(list )");
} // print

} // IrListImpl

