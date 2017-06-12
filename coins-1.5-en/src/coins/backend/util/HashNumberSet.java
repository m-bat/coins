/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.util;


/**
 * Set of small integers represented in hash table.
 */
public class HashNumberSet implements NumberSet {

  /** Hash table size (it grows) **/
  private static final int INITIALSHIFT = 3;
  private int hsize;
  private int nshift;

  private int[] table;
  private int nelem;

  /** Hash value **/
  private final int hash(int x) {
    return ((x >> nshift) ^ x) & (hsize - 1);
  }

  /** Create a set of numbers which is supposed to be less than max **/
  public HashNumberSet(int max) {
    nshift = INITIALSHIFT;
    hsize = 1 << nshift;
    table = new int[hsize];
    for (int i = 0; i < hsize; i++)
      table[i] = -1;
    nelem = 0;
  }

  /** Extend hashtable **/
  private void grow() {
    int[] oldtable = table;
    int oldsize = hsize;
    nshift++;
    hsize <<= 1;
    table = new int[hsize];
    for (int i = 0; i < hsize; i++)
      table[i] = -1;
    for (int i = 0; i < oldsize; i++) {
      int x = oldtable[i];
      if (x >= 0) {
        int j = hash(x);
        while (table[j] >= 0)
          j = (j + 1) & (hsize - 1);
        table[j] = x;
      }
    }
  }

  /** Return the number of elements. **/
  public int size() { return nelem; }


  public int tableSize() { return hsize; }

  /** Clear contents of set. **/
  public void clear() {
    for (int i = 0; i < hsize; i++)
      table[i] = -1;
    nelem = 0;
  }

  /** Add number x to set. **/
  public void add(int x) {
    if (x < 0)
      return;

    int i = hash(x);
    int n = 0;
    while (table[i] >= 0) {
      if (table[i] == x)
        return;
      i = (i + 1) & (hsize - 1);
      if (++n >= hsize) {
        grow();
        add(x);
        return;
      }
    }
    table[i] = x;
    nelem++;
    if (n > nshift)
      grow();
  }

  /** Add all elements of another NumberSet x to this set. **/
  public void addAll(NumberSet x) {
    x.addAllTo(this);
  }


  /** Add all elements of this set to another NumberSet x. **/
  public void addAllTo(NumberSet x) {
    for (int i = 0; i < hsize; i++) {
      if (table[i] >= 0)
        x.add(table[i]);
    }
  }


  /** Remove number x from set. **/
  public void remove(int x) {
    if (x < 0)
      return;
    int i = hash(x);
    int n = hsize;
    while (table[i] != -1 && n != 0) {
      if (table[i] == x) {
        table[i] = -2;
        nelem--;
        return;
      }
      i = (i + 1) & (hsize - 1);
      n--;
    }
  }

  /** Remove all elements of another NumberSet x from this set. **/
  public void removeAll(NumberSet x) {
    x.removeAllFrom(this);
  }

  /** Remove all elements of this set from another NumberSet x. **/
  public void removeAllFrom(NumberSet x) {
    for (int i = 0; i < hsize; i++) {
      if (table[i] >= 0)
        x.remove(table[i]);
    }
  }

  /** Return true if x is in the set. **/
  public boolean contains(int x) {
    int i = hash(x);
    int n = hsize;
    while (table[i] != -1 && n != 0) {
      if (table[i] == x)
        return true;
      i = (i + 1) & (hsize - 1);
      n--;
    }
    return false;
  }


  public static class Iterator implements NumberSet.Iterator {
    HashNumberSet set;
    int ptr;

    public Iterator(HashNumberSet x) {
      set = x;
      ptr = 0;
    }

    public boolean hasNext() {
      while (ptr < set.hsize) {
        if (set.table[ptr] >= 0)
          return true;
        ptr++;
      }
      return false;
    }

    public int next() {
      while (ptr < set.hsize) {
        if (set.table[ptr] >= 0)
          return set.table[ptr++];
        ptr++;
      }
      return -1;
    }
  }


  /** Enumerate all members. **/
  public NumberSet.Iterator iterator() { return new Iterator(this); }


  /** Convert to array **/
  public void toArray(int[] a) {
    int j = 0;
    for (int i = 0; i < hsize; i++) {
      if (table[i] >= 0)
        a[j++] = table[i];
    }
  }


  /** Copy another NumberSet x to this object. **/
  public void copy(NumberSet x) {
    clear();
    addAll(x);
  }

  /** Return true if x is same as this set. */
  public boolean equals(Object x) {
    if (!(x instanceof HashNumberSet))
      return false;

    HashNumberSet h = (HashNumberSet)x;
    if (h.nelem != nelem)
      return false;
    for (int i = 0; i < hsize; i++) {
      if (table[i] >= 0 && !h.contains(table[i]))
        return false;
    }
    return true;
  }

  /** Return copy of the object. **/
  public Object clone() {
    return this;
  }
}
