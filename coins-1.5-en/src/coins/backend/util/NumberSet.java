/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.util;

/**
 * Interface for set of nonnegative integers.
 */
public interface NumberSet {

  public static interface Iterator {
    public boolean hasNext();
    public int next();
  }

  /** Return the number of elements. **/
  int size();

  /** Clear contents of set. **/
  void clear();

  /** Add number x to set. **/
  void add(int x);

  /** Add all elements of another NumberSet x to this set. **/
  void addAll(NumberSet x);

  /** Add all elements of this set to another NumberSet x. **/
  void addAllTo(NumberSet x);

  /** Remove number x from set. **/
  void remove(int x);

  /** Remove all elements of another NumberSet x from this set. **/
  void removeAll(NumberSet x);

  /** Remove all elements of this set from another NumberSet x. **/
  void removeAllFrom(NumberSet x);

  /** Return true if x is in the set. **/
  boolean contains(int x);

  /** Enumerate all members. **/
  NumberSet.Iterator iterator();

  /** Set elements to array a. **/
  void toArray(int[] a);

  /** Copy another NumberSet x to this object. **/
  void copy(NumberSet x);

  /** Return true if x is same as this set. */
  boolean equals(Object x);

  /** Return copy of the object. **/
  Object clone();
}
