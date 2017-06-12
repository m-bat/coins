/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.util;

import coins.backend.CantHappenException;
import java.util.Iterator;
import java.util.Comparator;


/**
 * Bi-directional list (head part)
 */
public class BiList extends BiLink implements Cloneable {

  /** Create empty list. */
  public BiList() {
    next = this; // First node
    prev = this; // Last node
  }

  public Object elem() {
    throw new UnsupportedOperationException();
  }

  public BiLink unlink() {
    throw new UnsupportedOperationException();
  }

  public BiLink next() {
    throw new UnsupportedOperationException("attempt to next past end");
  }

  public BiLink prev() {
    throw new UnsupportedOperationException("attempt to prev past end");
  }

  /** Return first link. */
  public BiLink first() { return next; }

  /** Return last link. */
  public BiLink last() { return prev; }

  /** Return true if this link is either end. */
  public boolean atEnd() { return true; }

  /** Return true if this list is empty. */
  public boolean isEmpty() { return next == this; }

  /** Append an element to the end of the list. */
  public BiLink add(Object obj) {
    return this.addBefore(obj);
  }

  /** Append all element in the list to the end. **/
  public BiLink addAll(BiList list) {
    return this.addAllBefore(list);
  }

  /** Append a link to the end of the list. */
  public BiLink append(BiLink link) {
    return this.insertBefore(link);
  }

  /** Add an element before first element of the list. */
  public BiLink addFirst(Object obj) {
    return this.addAfter(obj);
  }

  /** Add an element before first element of the list. */
  public BiLink addAllFirst(BiList list) {
    return this.addAllAfter(list);
  }

  /** Prepend a link before first element of the list. */
  public BiLink prepend(BiLink link) {
    return this.insertAfter(link);
  }

  /** Return last element and remove it */
  public Object takeLast() {
    if (prev == this)
      throw new CantHappenException("takeLast from empty list");
    Object obj = prev.elem;
    prev.unlink();
    return obj;
  }

  /** Return first element and remove it */
  public Object takeFirst() {
    if (next == this)
      throw new CantHappenException("takeFirst from empty list");
    Object obj = next.elem;
    next.unlink();
    return obj;
  }

  /** Test if the list contains an object obj.  */
  public boolean contains(Object obj) {
    for (BiLink p = next; p != this; p = p.next) {
      if (p.elem == obj)
        return true;
    }
    return false;
  }

  /** Find a link which has an object obj.  */
  public BiLink locate(Object obj) {
    BiLink p;
    for (p = next; p != this; p = p.next) {
      if (p.elem == obj)
        return p;
    }
    return null;
  }

  /** Find a link which has an object which equals to obj.  */
  public BiLink locateEqual(Object obj) {
    BiLink p;
    for (p = next; p != this; p = p.next) {
      if (p.elem.equals(obj))
        return p;
    }
    return null;
  }

  /** Return the position of first occurence of an object obj. */
  public int whereIs(Object obj) {
    int n = 0;
    for (BiLink p = next; p != this; p = p.next) {
      if (p.elem == obj)
        return n;
      n++;
    }
    return -1;
  }


  /** Remove an link which has an object obj. */
  public BiLink remove(Object obj) {
    BiLink p = locate(obj);
    if (p != null)
      p.unlink();
    return p;
  }

  /** Remove an link which has an object which equals to obj. */
  public BiLink removeEqual(Object obj) {
    BiLink p = locateEqual(obj);
    if (p != null)
      p.unlink();
    return p;
  }

  /** Add an object to the list only if not there. */
  public BiList addNew(Object obj) {
    if (!contains(obj))
      add(obj);
    return this;
  }

  /** Clear the list. Make list empty. */
  public void clear() {
    next = prev = this;
  }

  /** Concatenate two lists. Added list will be destroyed.
   * @param aList the list appended
   * @return this object itself */
  public BiList concatenate(BiList aList) {
    prev.next = aList.next;
    aList.next.prev = prev;
    prev = aList.prev;
    prev.next = this;
    // make aList empty (not necessary but safer)
    aList.next = aList.prev = null;
    return this;
  }

  /** Split the list into two parts.
   * @param middle the first element of second half of the list
   * @return second half of the list (first half is <code>this</code> object) */
  public BiList split(BiLink middle) {
    BiList secondHalf = new BiList();
    if (middle != this) {
      secondHalf.next = middle;
      secondHalf.prev = prev;
      prev.next = secondHalf;
      prev = middle.prev;
      middle.prev = secondHalf;
      prev.next = this;
    }
    return secondHalf;
  }

  /** Make a copy of the list and return it.
   * Do not copy the contents; they are shared. */
  public BiList copy() {
    BiList newList = new BiList();
    for (BiLink p = next; p != this; p = p.next)
      newList.add(p.elem);
    return newList;
  }

  /** Return length of the list */
  public int length() {
    int n = 0;
    for (BiLink p = next; p != this; p = p.next) {
      if (p.atEnd()) {
        throw new CantHappenException("abnormal list: tail and head do not match");
      }
      n++;
    }
    return n;
  }


  /** Sort list. **/
  public void sort() {
    for (BiLink p = first(); !p.atEnd(); p = p.next()) {
      Comparable min = (Comparable)p.elem();
      for (BiLink q = p.next(); !q.atEnd(); ) {
        BiLink next = q.next();
        Comparable y = (Comparable)q.elem();
        if (y.compareTo(min) < 0) {
          q.unlink();
          p.insertBefore(q);
          p = q;
          min = y;
        }
        q = next;
      }
    }
  }


  /** Sort list according to Comparator. **/
  public void sort(Comparator cmp) {
    for (BiLink p = first(); !p.atEnd(); p = p.next()) {
      Object min = p.elem();
      for (BiLink q = p.next(); !q.atEnd(); ) {
        BiLink next = q.next();
        Object y = q.elem();
        if (cmp.compare(y, min) < 0) {
          q.unlink();
          p.insertBefore(q);
          p = q;
          min = y;
        }
        q = next;
      }
    }
  }


  /** Compare two lists **/
  public boolean equals(Object x) {
    if (!(x instanceof BiList))
      return false;
    if (this == (BiList)x)
      return true;
    
    BiLink p = first();
    BiLink q = ((BiList)x).first();
    for (; p != this && q != (BiList)x; p = p.next, q = q.next) {
      if (!(p.elem).equals(q.elem))
        return false;
    }
    return (p == this && q == (BiList)x);
    // true if both p and q reach end of list
  }


  /** Convert to array. **/
  public Object[] toArray() {
    int n = length();
    Object[] vec = new Object[n];
    int i = 0;
    for (BiLink p = next; p != this; p = p.next)
      vec[i++] = p.elem;
    return vec;
  }


  /** Visualize */
  public String toString() {
    StringBuffer buf = new StringBuffer();
    buf.append("(");
    boolean first = true;
    for (BiLink p = next; p != this; p = p.next) {
      if (!first) buf.append(" ");
      buf.append(p.elem.toString());
      first = false;
    }
    buf.append(")");
    return buf.toString();
  }


  /** Return copy of the list. **/
  public Object clone() {
    BiList obj = new BiList();
    obj.addAll(this);
    return obj;
  }


  /** Iterator for scanning a BiList. */
  class BiListIterator implements Iterator {
    BiLink current;

    BiListIterator(BiList list) { current = list.next; }

    public boolean hasNext() { return !current.atEnd(); }

    public Object next() {
      Object content = current.elem;
      current = current.next;
      return content;
    }

    public void remove() { throw new UnsupportedOperationException(); }
  }

  /** Return iterator for the list. */
  public Iterator iterator() {
    return new BiListIterator(this);
  }


  /** Check list consistency. **/
  public void sanityTest() {
    int n = 0;
    for (BiLink p = next; p != this; p = p.next) {
      if (p.next == null || p.prev == null)
        throw new Error("BiList sanity: null pointer detected at link#" + n);
      if (p.next.prev != p)
        throw new Error("BiList sanity: next.prev != this at link#" + n);
      n++;
    }
  }


  /** Unit test driver **/
  private static void iteratorTest(Iterator it) {
    System.out.println("//<<<<<<<<");
    for (int n = 1; it.hasNext(); n++) {
      Object elem = it.next();
      System.out.println(n + ": " + elem);
    }
    System.out.println(">>>>>>>>//");
  }

  public static void main(String [] args) {
    BiList alist = new BiList();
    BiList emptylist = new BiList();

    System.out.println(alist);
    alist.add(new Integer(123));
    System.out.println(alist);
    alist.add(new Integer(456));
    System.out.println(alist);
    alist.add(new Integer(789));
    System.out.println(alist);

    iteratorTest(alist.iterator());
    iteratorTest(emptylist.iterator());

    BiList blist = new BiList();
    blist.add("aho");
    blist.add("baka");
    iteratorTest(blist.iterator());
    alist = alist.concatenate(blist);
    iteratorTest(alist.iterator());

    BiList half = alist.split(alist.locate("baka"));
    iteratorTest(alist.iterator());
    iteratorTest(half.iterator());

    BiList slist = new BiList();
    slist.add("tokyo");
    slist.add("newyork");
    slist.add("london");
    slist.add("paris");
    slist.add("rome");
    slist.add("berlin");
    slist.add("moscow");
    slist.add("beijing");
    slist.add("geneve");
    System.out.println("Before sort:" + slist);
    slist.sort();
    System.out.println("After sort:" + slist);
  }
}
