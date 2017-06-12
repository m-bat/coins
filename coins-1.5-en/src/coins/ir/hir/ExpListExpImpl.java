/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;  //##51

import coins.HirRoot;
import coins.HasStringObject; //##51
import coins.sym.Sym;         //##51

/** ExpListExpImpl
 * Expression representing a list of expressions (Exp).
 * Its operator is OP_EXPLIST.
 * This is used to represent a list of initial values.
 * The element of the list may be a repetition specification
 * that specifies repetition count (as child 1) and elements
 * to be repeated (as child 2).
 * @author  Shuichi Fukuda
 * @version 2002.9.3
**/

public class ExpListExpImpl extends ExpImpl implements ExpListExp
{
  //##51 Object[] values = null; // Array to hold the elements of the list.
  LinkedList values = null; // Array to hold the elements of the list.
  public ExpListExpImpl(HirRoot pHirRoot,List list) // Constructor
  {
    super(pHirRoot, HIR.OP_EXPLIST);
    fChildCount = 0;
    //##51 values = list!=null
    //##51        ? list.toArray()
    //##51        : new Object[0];
    values = new LinkedList(); //##51
    if (list != null)         //##51
      for( Iterator i=list.iterator(); i.hasNext(); ) //##51
      {
        Exp exp = (Exp)i.next(); //##51
        values.add(exp); //##51
        exp.cutParentLink(); //##51
        exp.setParent(this); //##51
      }
  }
  public int size()
  {
    //##51 return values.length;
    return values.size(); //##51
  }
  public int length()
  {
    //##51 return values.length;
    return values.size(); //##51
  }
  public Exp getExp(int index)  //##51
  {
    /* //##51
    return 0<=index && index<values.length
         ? (Exp)values[index]
         : null;
    */ //##51
    //##51 BEGIN
    if ((0 <= index)&&(index < size()))
      return (Exp)values.get(index);
    else
      return null;
    //##51 END
  }
  public Exp getWithRepeat(int index)
  {
    /* //##51
    if( values.length>0 && 0<=index )
    {
      Exp repeat = (Exp)values[values.length-1];
      if( repeat.getOperator()==HIR.OP_EXPREPEAT )
      {
        if( index < values.length-1 )
          return (Exp)values[index];
        if( index < values.length-1+((ConstNode)repeat.getExp2()).getIntValue() )
          return repeat.getExp1();
      }
      else
      {
        if( index < values.length )
          return (Exp)values[index];
      }
    }
    */ //##51
   //##51 BEGIN
   if( values.size()>0 && 0<=index )
   {
     Exp repeat = (Exp)values.get(values.size()-1);
     if( repeat.getOperator()==HIR.OP_EXPREPEAT )
     {
       if( index < values.size()-1 )
         return (Exp)values.get(index);
       if( index < values.size()-1+((ConstNode)repeat.getExp2()).getIntValue() )
         return repeat.getExp1();
     }
     else
     {
       if( index < values.size() )
         return (Exp)values.get(index);
     }
   }
   //##51 END
    return null;
  }
  public void setExp(int index,Exp exp)
  {
    //##51 if( 0<=index && index<values.length )
    //##51   values[index] = exp;
    if( 0<=index && index<values.size() ) { //##51
      values.set(index, exp); //##51
      exp.cutParentLink(); //##51
      exp.setParent(this); //##51
    }
  }

  public void
  set( int pIndex, Object pElement ) {
    values.set(pIndex, pElement);
    if ((pElement instanceof HIR)&&
        (((HIR)pElement).getParent() == null))
      ((HIR)pElement).setParent(this);
  }

//##51 BEGIN
  public void
  add( Object pElement )
  {
    if (pElement == null)
      return;
    values.add(pElement);
    if ((pElement instanceof HIR)&&
        (((HIR)pElement).getParent() == null))
      ((HIR)pElement).setParent(this);
  }

  public void
  add( int pInsertionPosition, Object pObjectToBeInserted )
  {
    values.add(pInsertionPosition, pObjectToBeInserted);
    if ((pObjectToBeInserted instanceof HIR)&&
        (((HIR)pObjectToBeInserted).getParent() == null))
       ((HIR)pObjectToBeInserted).setParent(this);
  }

public Object
getFirst()
{
  if (isEmpty())
    return null;
  else
    return values.get(0);
}

public Object
get( int pIndex ) { return values.get(pIndex); }

public boolean
isEmpty() { return values.isEmpty(); }

public boolean
contains( Object pObject ) { return values.contains(pObject); }

public int
indexOf( Object pObject ) {
return values.indexOf(pObject); }

public Object
remove( int pRemovePosition ) { return values.remove(pRemovePosition); }

public boolean
remove( Object pObject ) { return values.remove(pObject); }

//##77 BEGIN
public void
clear()
{
  values.clear();
}
//##77 END

public java.util.ListIterator
iterator() { return values.listIterator(); }

public Object
clone()  throws CloneNotSupportedException  {
  Object          lObject = null;
  HasStringObject lHasStringObject;
  HIR             lHir, lElem;
  LinkedList      lList;
  ExpListExpImpl  lTree;
  try {
    lTree = (ExpListExpImpl)super.clone();
  }catch (CloneNotSupportedException e) {
    hirRoot.ioRoot.msgRecovered.put(1100,
               "CloneNotSupportedException(ExpListExpImpl) " +
                e.getMessage() + " " + this.toString());
    return null;
  }
  if (values != null) {
    lList = new LinkedList();
    for (java.util.Iterator listIterator = values.iterator();
         listIterator.hasNext(); ) {
      try {
        lObject = listIterator.next();
        if (lObject != null) {
          lHasStringObject = (HasStringObject)lObject;
          if (lHasStringObject.isHIR()) {
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
    lTree.values = lList;
  }
  return (Object)lTree;
} // clone

public HirList
hirListClone()throws CloneNotSupportedException
{
  return (HirList)this.clone();
}

//##51 END

//  public String toString() //SF020912
//  {
//    StringBuffer sb = new StringBuffer();
//    for(int i=0; i<values.length; i++)
//      sb.append(values[i].toString()).append(" ");
//    return sb.toString().intern();
//  }
  public void print(int indent) // Print the expression list.
  {
    print(indent, false);
  }// print

  public void print(int indent, boolean detail)// Print the expression list.
  {
    String space = hirRoot.hir.getIndentSpace(indent);
    //##51 if( values.length>0 )
     if( values.size()>0 ) //##51
    {
      //hirRoot.ioRoot.printOut.print("\n"+space+"(list "); //SF020912
      hirRoot.ioRoot.printOut.print("\n"+space+"("+this); //SF020912
     //##51 for(int i=0; i<values.length; i++)
     //##51    ((Exp)values[i]).print(indent+1, detail);
     for(int i=0; i<values.size(); i++)
        ((Exp)values.get(i)).print(indent+1, detail);
      hirRoot.ioRoot.printOut.print(")");
    }
    else
      //hirRoot.ioRoot.printOut.print("\n" + space + "(list )"); //SF020912
      hirRoot.ioRoot.printOut.print("\n" + space + "("+this+")"); //SF020912
  }// print

  public String
  toStringWithChildren()
  {
    String lString = "<ExpList ";
    /* //##51
    if( values.length>0 )
    {
      for(int i=0; i<values.length; i++) {
        lString = lString + ((Exp)values[i]).toStringWithChildren() + " ";
      }
    }
      */ //##51
     //##51 BEGIN
     if( values.size()>0 )
     {
       for(int i=0; i<values.size(); i++) {
         lString = lString + ((Exp)values.get(i)).toStringWithChildren() + " ";
       }
     }
     //##51 END
    lString = lString + ">";
    return lString;
  } // toStringWithChildren

} // ExpListExp
