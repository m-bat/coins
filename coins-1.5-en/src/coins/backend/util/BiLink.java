/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.util;


/**
 * Bi-directional list (middle links)
 */
public class BiLink {
  /** contents */
  Object elem;
  /** points next element. */
  BiLink next;
  /** points previous element. */
  BiLink prev;

  /** Used internally only. */
  BiLink() {}

  /** Create new link that has an object obj. Used internally. */
  BiLink(Object obj) { elem = obj; }

  /** Return contents of this link. */
  public Object elem() { return elem; }

  /** Return next link. */
  public BiLink next() { return next; }

  /** Return previous link. */
  public BiLink prev() { return prev; }

  /** Return true if this link is either end. */
  public boolean atEnd() { return false; }

  /** Replace contents of the link. */
  public void setElem(Object obj) { elem = obj; }

  /** Insert a link after this link.
   * @param link link to be inserted */
  public BiLink insertAfter(BiLink link) {
    BiLink temp = this.next;
    this.next = link;
    link.prev = this;
    link.next = temp;
    temp.prev = link;
    return link;
  }

  /** Insert an object after this link. */
  public BiLink addAfter(Object obj) {
    return insertAfter(new BiLink(obj));
  }

  /** Insert a link before this link.
   * @param link link to be inserted */
  public BiLink insertBefore(BiLink link) {
    return prev.insertAfter(link);
  }

  /** Insert an object before this link. */
  public BiLink addBefore(Object obj) {
    return prev.insertAfter(new BiLink(obj));
  }

  /** Remove this link from the list.
   * @return the link just removed */
  public BiLink unlink() {
    BiLink temp = next;
    prev.next = next;
    temp.prev = prev;
    return this;
  }

  /** Insert contents of list before this link.
   ** @deprecated renamed to addAllBefore. */
  public BiLink insertAllBefore(BiList list) {
    for (BiLink p = list.first(); !p.atEnd(); p = p.next)
      addBefore(p.elem);
    return this;
  }

  /** Insert contents of list after this link.
   ** @deprecated renamed to addAllAfter. */
  public BiLink insertAllAfter(BiList list) {
    next.insertAllBefore(list);
    return this;
  }

  /** Insert contents of list before this link. */
  public BiLink addAllBefore(BiList list) {
    for (BiLink p = list.first(); !p.atEnd(); p = p.next)
      addBefore(p.elem);
    return this;
  }

  /** Insert contents of list after this link. */
  public BiLink addAllAfter(BiList list) {
    next.addAllBefore(list);
    return this;
  }
}
