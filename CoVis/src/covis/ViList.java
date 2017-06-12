/* ----------------------------------------------------------
%   Copyright (C) 2005 The Coins Project Group               
%       (Read COPYING for detailed information.)             
----------------------------------------------------------- */
package covis;

/**
 * The ViList class is a kind of list
 * which has pointers for next and previous element
 */
public class ViList {

  /** contents */
  Object elem;
  /** points next element. */
  ViList next;
  /** points previous element. */
  ViList prev;

  /** Constructor 
   *  Create empty list.
   */
  public ViList() {
    next = this; // First node
    prev = this; // Last node
    ViList dummy = new ViList("dummy");
    next = dummy;
    dummy.prev = this;
    dummy.next = null;

  }

  /** Create unlinked Object */
  ViList(Object obj) {
    elem = obj;
  }

  /** Return contents of this link. */
  public Object elem() { return elem; }

  /** Return next link. */
  public ViList next() { return next; }

  /** Return previous link. */
  public ViList prev() { return prev; }

  /** Return true if this list is dummy for the last. */
  public boolean atEnd() {
    if (next == null) return true;
    else              return false;
  }

  public boolean atFirst() {
    if (prev == null) return true;
    else              return false;
  }

  /** Return true if the next is the dummy list for the last. */
  public boolean isEmpty() { return next.next == null; }

  /** Return true if this list has next (but not dummy) list. */
  public boolean hasNext() { return next.next != null; }

  /** Replace contents of the link. */
  public Object getElem(int i) {
    int n = 0;
    ViList q;
    for (q = first(); !q.atEnd(); q=q.next()) {
      if (n == i)
        break;
      ++n;
    }
    if (q.atEnd()) {
      return null;
    }
    return q.elem();
  }

  /** Return the first link. */
  public ViList first() { return next; }

  /** Return the last link. */
  public ViList last() {
    ViList q = first();
    while(!q.atFirst()) {
      q = q.prev();
    }
    return q;
  }

  /** Return the first element */
  public Object firstElem() {
    return (Object)next.elem();
  }

  /** Append an element to the end of the list. */
  public ViList add(Object obj) {
    ViList link = new ViList(obj);
    ViList p = this.next;
    while ( p.next != null) p = p.next;
    ViList dummy = p;
    p = p.prev;
    p.next = link;
    link.prev = p;
    link.next = dummy;
    dummy.prev = link;
    return link;
  }


  /** Return length of the list */
  public int length() {
    int n = 0;
    for (ViList q = first(); !q.atEnd(); q=q.next()) {
      n++;
    }
    return n;
  }

  /****
  public static void main(String [] args) {
    ViList slist = new ViList();

    if (slist.isEmpty()) System.out.println("empty!!");
    slist.add("tokyo");
    slist.add("newyork");
    slist.add("london");
    slist.add("paris");
    slist.add("rome");
    slist.add("berlin");
    slist.add("moscow");
    slist.add("beijing");
    slist.add("geneve");

    for (ViList q = slist.first(); !q.atEnd(); q=q.next()) {
      String s = (String)q.elem();
      System.out.println(s);
    }
    System.out.println("slist.length() = "+slist.length());
    System.out.println("slist 3d element is "+(String)slist.getElem(3));

  }
 ******/
}
