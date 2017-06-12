/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import java.util.LinkedList;

import coins.HasStringObject;
import coins.HirRoot;
import coins.ir.IR;
import coins.sym.Sym;

/** class HirListImpl
 *  List of expressions, symbols, and others.
 *  LinkedList in java.util is wrapped so that it can be
 *  treated as IR node and IR methods can be applied.
**/
public class
HirListImpl extends HIR_Impl implements HirList
{

  private LinkedList fList;  // List of objects.
                    // LinkedList methods can be applied.

/** HirList constructor
 *  Wrap pList so that it can be treated as IR node.
 *  @param pList LinkedList that may contain any objects.
 *      If pList is null, an empty LinkedList is created.
**/
public
HirListImpl( HirRoot pHirRoot, LinkedList pList )
{
  super(pHirRoot);
  fOperator = IR.OP_LIST;
  if (pList == null)
    fList = new LinkedList();
  else
    fList = pList;
  fType = hirRoot.symRoot.typeVoid;
} // HirList

public
HirListImpl( HirRoot pHirRoot)
{
  super(pHirRoot);
  fOperator = IR.OP_LIST;
  fList = new LinkedList();
  fType = hirRoot.symRoot.typeVoid;
} // HirList

public void
add( Object pElement )
{
  if (pElement == null)
    return;
  fList.add(pElement);
  if ((pElement instanceof HIR)&&
      (((HIR_Impl)pElement).getParent() == null)) //##54
    ((HIR_Impl)pElement).setParent(this); //##54
  if (hirRoot.ioRoot.dbgHir.getLevel() > 1) //##70
    hirRoot.ioRoot.dbgHir.print(7, "HirList add", pElement.toString()); //##70
}

public void
add( int pInsertionPosition, Object pObjectToBeInserted )
{
  fList.add(pInsertionPosition, pObjectToBeInserted);
  if ((pObjectToBeInserted instanceof HIR)&&
      (((HIR_Impl)pObjectToBeInserted).getParent() == null)) //##54
     ((HIR_Impl)pObjectToBeInserted).setParent(this); //##54
}

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
  if ((pElement instanceof HIR)&&
      (((HIR)pElement).getParent() == null))
    ((HIR_Impl)pElement).setParent(this); //##54
}

public boolean
isEmpty() { return fList.isEmpty(); }

public int
size() { return fList.size(); }

public boolean
contains( Object pObject ) { return fList.contains(pObject); }

public int
indexOf( Object pObject ) {
return fList.indexOf(pObject); }

public Object
remove( int pRemovePosition ) { return fList.remove(pRemovePosition); }

public boolean
remove( Object pObject ) { return fList.remove(pObject); }

//##77 BEGIN
public void
clear()
{
  fList.clear();
}
//##77 END

public java.util.ListIterator
iterator() { return fList.listIterator(); }

public Object
clone()  throws CloneNotSupportedException  {
  Object          lObject = null;
  HasStringObject lHasStringObject;
  HIR             lHir, lElem;
  LinkedList      lList;
  HirListImpl lTree;
  try {
    lTree = (HirListImpl)super.clone();
  }catch (CloneNotSupportedException e) {
    hirRoot.ioRoot.msgRecovered.put(1100,
               "CloneNotSupportedException(HirListImpl) " +
                e.getMessage() + " " + this.toString());
    return null;
  }
  if (fList != null) {
    lList = new LinkedList();
    for (java.util.Iterator listIterator = fList.iterator();
         listIterator.hasNext(); ) {
      try {
        lObject = listIterator.next();
        if (lObject != null) {
          lHasStringObject = (HasStringObject)lObject;
          if (lHasStringObject.isHIR()) {
            //## lList.add(((HIR)lHasStringObject).clone()); // REFINE
            //## "HirListImpl.java": error # 306 :
            //##    method clone() is protected access.
            lHir = (HIR)lHasStringObject;
            lElem = lHir.hirClone();
            lList.add(lElem);
            ((HIR_Impl)lElem).fParentNode = lTree;
          }else if (lHasStringObject.isSym())
            lList.add((Sym)lHasStringObject);
          else
            lList.add(lObject); //## REFINE lObject.clone() ?
        }else
          lList.add(null);
      }
      catch (ClassCastException e) {
        lList.add(lObject);
        hirRoot.ioRoot.msgFatal.put(1103,
              "HirList.clone() ClassCastException " +
              e.getMessage() + " " + hirRoot.ioRoot.toStringObject(lObject));
      }
    }
    lTree.fList = lList;
    if (hirRoot.ioRoot.dbgHir.getLevel() > 1) //##70
      hirRoot.ioRoot.dbgHir.print(5, "HirList clone", lTree.toStringWithChildren()); //##70
  }
  return (Object)lTree;
} // clone

public HirList
hirListClone()throws CloneNotSupportedException
{
  return (HirList)this.clone();
}
/*##19 Use super.toString()
public String
toString() {
  Object lObject;
  StringBuffer lString = new StringBuffer();
  if (fList != null) {
    for (java.util.Iterator listIterator = fList.iterator();
         listIterator.hasNext(); ) {
      lObject = (Object)listIterator.next();
      if (lObject instanceof String)
        lString.append((String)lObject + " ");
      else if (lObject instanceof HasStringObject)
        lString.append(((HasStringObject)lObject)
                      .toString()).append(" ");
    }
  }
  return lString.toString().intern();
} // toString
**/

/*##19 Use super.toString()
public String
toStringShort() {
  Object lObject;
  StringBuffer lString = new StringBuffer();
  if (fList != null) {
    for (java.util.Iterator listIterator = fList.iterator();
         listIterator.hasNext(); ) {
      lObject = (Object)listIterator.next();
      if (lObject instanceof String)
        lString.append((String)lObject + " ");
      else if (lObject instanceof HasStringObject)
        lString.append(((HasStringObject)lObject)
                      .toString()).append(" ");
    }
  }
  return lString.toString().intern();
} // toStringShort
**/

public void
print(int pIndent) {
  String lSpace = hirRoot.hir.getIndentSpace(pIndent);
  if (fList != null) {
    hirRoot.ioRoot.printOut.print("\n" + lSpace + "(list " + getIndex());
    for (java.util.Iterator listIterator = fList.iterator();
         listIterator.hasNext(); ) {
      ((HasStringObject)listIterator.next()).print(pIndent+1);
    }
    hirRoot.ioRoot.printOut.print(")");
  }else
    hirRoot.ioRoot.printOut.print("\n" + lSpace + "(list )");
} // print

public void
print(int pIndent, boolean pDetail) {
  String lSpace = hirRoot.hir.getIndentSpace(pIndent);
  HasStringObject lObject;
  if (fList != null) {
    hirRoot.ioRoot.printOut.print("\n" + lSpace + "(list " + getIndex());
    int lListSize = fList.size(); //##64
    int lIndex = 0; //##64
    for (java.util.Iterator listIterator = fList.iterator();
         listIterator.hasNext(); ) {
      lObject = (HasStringObject)listIterator.next();
      //##70 lIndex++; //##64
      if (lIndex > lListSize) //##64
        break; // Escape from erronous list (ring form). //##64
      lIndex++; //##70
      if (lObject instanceof HIR)
        ((HIR)lObject).print(pIndent+1, pDetail);
      else
        lObject.print(pIndent+1);
    }
    hirRoot.ioRoot.printOut.print(")");
  }else
    hirRoot.ioRoot.printOut.print("\n" + lSpace + "(list )");
} // print

public void
accept( HirVisitor pVisitor )
{
  pVisitor.atHirList(this);
}

} // HirListImpl

