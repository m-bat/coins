/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.util;


/**
 * Set of small integers represented in arrays.
 */
public class VectorSet implements NumberSet {

  private static final int MAXNUM = 2 << 15;

  private short[] check;
  private short[] list;
  private int nelem;

  /** Create a set of numbers which is supposed to be less than max **/
  public VectorSet(int max) {
    if (max > MAXNUM)
      throw new Error("Too big");
    nelem = 0;
    check = new short[max];
    list = new short[max];
  }

  /** Return the number of elements. **/
  public int size() { return nelem; }

  /** Clear contents of set. **/
  public void clear() {
    nelem = 0;
  }

  /** Add number x to set. **/
  public void add(int x) {
    if (x < 0 || x >= check.length)
      return;	// boundary error

    if (check[x] < nelem && list[check[x]] == x)
      return;     // Already registered

    if (nelem >= list.length)
      return;     // Already full
    
    list[nelem] = (short)x;
    check[x] = (short)nelem++;
  }

  /** Add all elements of another NumberSet x to this set. **/
  public void addAll(NumberSet x) {
    x.addAllTo(this);
  }

  /** Add all elements of this set to another NumberSet x. **/
  public void addAllTo(NumberSet x) {
    for (int i = 0; i < nelem; i++)
      x.add(list[i]);
  }


  /** Remove number x from set. **/
  public void remove(int x) {
    if (x < 0 || x >= check.length)
      return;	// boundary error

    int p = check[x];
    if (p < nelem && list[p] == x) {
      list[p] = list[--nelem];
      check[list[p]] = (short)p;
    }
  }

  /** Remove all elements of another NumberSet x from this set. **/
  public void removeAll(NumberSet x) {
    x.removeAllFrom(this);
  }

  /** Remove all elements of this set from another NumberSet x. **/
  public void removeAllFrom(NumberSet x) {
    for (int i = 0; i < nelem; i++)
      x.remove(list[i]);
  }

  /** Return true if x is in the set. **/
  public boolean contains(int x) {
    if (x < 0 || x >= check.length)
      return false;	// boundary error

    int p = check[x];
    return (p < nelem && list[p] == x);
  }


  public static class Iterator implements NumberSet.Iterator {
    VectorSet set;
    int ptr;

    public Iterator(VectorSet x) {
      set = x;
      ptr = set.nelem;
    }

    public boolean hasNext() {
      return ptr > 0;
    }

    public int next() {
      if (ptr > 0)
        return set.list[--ptr];
      else
        return -1;
    }
  }


  /** Enumerate all members. **/
  public NumberSet.Iterator iterator() { return new Iterator(this); }


  /** Convert to array **/
  public void toArray(int[] a) {
    int j = 0;
    for (int i = 0; i < nelem; i++)
      a[j++] = list[i];
  }



  /** Copy another NumberSet x to this object. **/
  public void copy(NumberSet x) {
    clear();
    addAll(x);
  }

  /** Return true if x is same as this set. */
  public boolean equals(Object x) {
    if (!(x instanceof VectorSet))
      return false;

    VectorSet v = (VectorSet)x;
    if (v.nelem != nelem)
      return false;
    for (int i = 0; i < nelem; i++) {
      if (!v.contains(list[i]))
        return false;
    }
    return true;
  }

  /** Return copy of the object. **/
  public Object clone() {
    return this;
  }
}
