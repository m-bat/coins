/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
//##60 package coins.aflow.util;
package coins.flow; //##60

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * <p>Title: </p>
 * <p>Description: 1 based list that prohibits the <code>remove</code> operation. </p>
 * <p>Since many indexes in COINS are 1 based, it should be convenient to use a list whose base is 1. Collection of such indexed objects are often expressed in terms of <code>BitVector</code>, and the meaning of the bits in the vector should be maintained by such a 1-based list, which serves as a map between the bit space and the space of indexed objects. Removal of elements in such a list, which results in re-indexing of list elements, will break the map and is not desirable. This list therefore prohibits such a removal.
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public class FAList
  implements Collection
{
  /**
   * Zeroth element of the underlying list.
   */
  private final static Object zeroElem = new Object();

  /**
   * Underlying zero based list.
   */
  private List fList = new ArrayList();

  /**
   * Creates a new <code>FAList</code> instance.
   */
  public FAList()
  {
    //  fList.add(zeroElem);
  }

  /**
   * Creates a new <code>FAList</code> instance with given initial capacity.
   */
  public FAList(int initialCapacity)
  {
    fList = new ArrayList(initialCapacity);
    //     fList.add(zeroElem);
  }

  public boolean add(Object pElement)
  {
    return fList.add(pElement);
  }

  /**
   * Inserts the specified element at the specified position in this list.
   *
   * @param index position into which to insert the element.
   * @param element element to insert.
   * @exception IndexOutOfBoundsException if the index is not positive or is greater than the size of this list.
   */

  /*  public void add(int index, Object element)
    {
      if (index == size() + 1)
        add(element);
      else
        throw new UnsupportedOperationException();
    }
   */
  public boolean addAll(Collection pCollection)
  {
    return fList.addAll(pCollection);
    //  throw new UnsupportedOperationException();
  }

  public boolean addAll(int pIndex, Collection pCollection)
  {
    return fList.addAll(pIndex - 1, pCollection);
    //  throw new UnsupportedOperationException();
  }

  /**
   * Removes all of the elements from this list. This
   * list will be empty after this call returns.
   */
  public void clear()
  {
    fList.clear();
    //    fList.add(zeroElem);
  }

  public boolean contains(Object pObj)
  {
    return fList.contains(pObj);
  }

  public boolean containsAll(Collection pCollection)
  {
    return fList.containsAll(pCollection);
  }

  public boolean equals(Object pObj)
  {
    if (getClass() == pObj.getClass()) {
      return fList.equals(((FAList)pObj).fList);
    }

    return false;
  }

  public int hashCode()
  {
    return fList.hashCode();
  }

  public Object get(int pIndex)
  {
    //  if (pIndex == 0) {
    //    throw new IndexOutOfBoundsException();
    //  }

    return fList.get(pIndex - 1);
  }

  /**
   * Returns the index in this list of the first occurrence of the specified element, or -1 if this list does not contain this element. More formally, returns the lowest index i such that (o==null ? get(i)==null : o.equals(get(i))), or -1 if there is no such index.
   *
   *   * @param elem element to search for.
   * @return the index in this list of the first occurrence of the specified
   *  element, or -1 if this list does not contain this element.
   */
  public int indexOf(Object elem)
  {
    return fList.indexOf(elem) + 1;
  }

  /**
   * Tests whether this list has any elements.
   *
   * @return <tt>true</tt> if this list is empty.
   */
  public boolean isEmpty()
  {
    return fList.isEmpty();
  }

  /**
   * Returns the iterator over this list.
   *
   * @return the iterator over this list.
   */
  public Iterator iterator()
  {
    return listIterator();
  }

  public int lastIndexOf(Object pObj)
  {
    return fList.lastIndexOf(pObj) + 1;
  }

  public ListIterator listIterator()
  {
    return listIterator(1);
  }

  public ListIterator listIterator(int pIndex)
  {
    //  if (pIndex == 0) {
    //    throw new IndexOutOfBoundsException();
    //  }
    return new ListItr(pIndex);

  }

  public Object remove(int pIndex)
  {
    throw new UnsupportedOperationException("Use deref instead.");
  }

  public boolean remove(Object pObj)
  {
    throw new UnsupportedOperationException("Use deref instead.");
  }

  public boolean removeAll(Collection pCollection)
  {
    throw new UnsupportedOperationException();
  }

  public boolean retainAll(Collection pCollection)
  {
    throw new UnsupportedOperationException();
  }

  public Object set(int pIndex, Object pElement)
  {
    return fList.set(pIndex - 1, pElement);
  }

  /**
   * Sets the indexth entry of this list to null.
   *
   * @param index index that is set to null.
   * @return the original indexth element.
   * @exception IndexOutOfBoundsException if the index is not positive or is greater than the size of this list.
   */
  public Object deref(int index)
  {
    //  if (index == 0) {
    //    throw new IndexOutOfBoundsException();
    //  }

    Object orig = get(index);
    set(index, null);

    return orig;
  }

  /**
   * Returns the number of elements in this list.  If this list contains
   * more than <tt>Integer.MAX_VALUE</tt> elements, returns
   * <tt>Integer.MAX_VALUE - 1</tt>.
   *
   * @return the number of elements in this list.
   */
  public int size()
  {
    return fList.size();
  }

  //  public List subList(int pFrom, int pTo) {
  //  if (pFrom == 0) {
  //    throw new IndexOutOfBoundsException();
  //  }
  //
  //  return fList.subList(pFrom -1 , pTo - 1);
  //  }

  public Object[] toArray()
  {
    return fList.toArray();
  }

  public Object[] toArray(Object[] pObj)
  {
    //  fList.set(0, null);

    return fList.toArray(pObj);
  }

  public String toString()
  {
    return fList.toString();
  }

  /**
   * Returns the ordinary java.util.List's view of this list. The zero'th index becomes accessible. The member objects' indexes do <i>not</i> change.
   */
  public List toList()
  {
    return fList;
  }

  private class ListItr
    implements ListIterator
  {
    ListIterator fListIt;

    private ListItr(int pIndex)
    {
      fListIt = fList.listIterator(pIndex - 1);
    }

    public boolean hasNext()
    {
      return fListIt.hasNext();
    }

    public Object next()
    {
      return fListIt.next();
    }

    public boolean hasPrevious()
    {
      return fListIt.hasPrevious();
    }

    public Object previous()
    {
      return fListIt.previous();
    }

    public int nextIndex()
    {
      return fListIt.nextIndex() + 1;
    }

    public int previousIndex()
    {
      return fListIt.previousIndex() + 1;
    }

    public void remove()
    {
      fListIt.remove();
    }

    public void set(Object o)
    {
      fListIt.set(o);
    }

    public void add(Object o)
    {
      fListIt.add(o);
    }

  }
}
