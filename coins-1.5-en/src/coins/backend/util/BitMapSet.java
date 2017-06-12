/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.util;


/**
 * Set of small integers represented in bit vector.
 */
public class BitMapSet implements NumberSet {
  private int[] bitmap;

  static final int WORDSIZE = 32;

  /** Return the number of elements. **/
  public int size() {
    int n = 0;
    for (int i = 0; i < bitmap.length; i++)
      n += Misc.count1s(bitmap[i]);
    return n;
  }

  /** Create empty set. **/
  public BitMapSet() {
    bitmap = new int[1];
  }

  /** Create set of given size **/
  public BitMapSet(int size) {
    bitmap = new int[(size+(WORDSIZE-1))/WORDSIZE];
  }


  /** Clear set **/
  public void clear() {
    for (int i = 0; i < bitmap.length; i++)
      bitmap[i] = 0;
  }


  /** Grow vector upto size **/
  private int[] grow(int[] map, int size) {
    int[] newmap = new int[size];
    for (int i = 0; i < map.length; i++)
      newmap[i] = map[i];
    return newmap;
  }


  /** Add integer x. **/
  public void add(int x) {
    if (x < 0)
      return;
    int m = x / WORDSIZE;
    if (m >= bitmap.length)
      bitmap = grow(bitmap, m + 1);
    int mask = 1 << (x % WORDSIZE);
    bitmap[m] |= mask;
  }


  /** Remove integer x. **/
  public void remove(int x) {
    if (x < 0)
      return;
    int m = x / WORDSIZE;
    if (m < bitmap.length)
      bitmap[m] &= ~(1 << (x % WORDSIZE));
  }


  /** Return true if set has the integer x in it. **/
  public boolean exist(int x) { return contains(x); }


  /** Return true if set has the integer x in it. **/
  public boolean contains(int x) {
    if (x < 0)
      return false;
    int m = x / WORDSIZE;
    if (m < bitmap.length)
      return (bitmap[m] & (1 << (x % WORDSIZE))) != 0;
    else
      return false;
  }


  /** Copy another NumberSet x to this object. **/
  public void copy(NumberSet x) {
    if (x instanceof BitMapSet) {
      BitMapSet y = (BitMapSet)x;
      if (bitmap.length < y.bitmap.length)
        bitmap = new int[y.bitmap.length];
      int i;
      for (i = 0; i < y.bitmap.length; i++)
        bitmap[i] = y.bitmap[i];
      for (; i < bitmap.length; i++)
        bitmap[i] = 0;
    } else {
      clear();
      NumberSet.Iterator it = x.iterator();
      while (it.hasNext())
        add(it.next());
    }
  }


  /** Add all elements of another NumberSet x to this set. **/
  public void addAll(NumberSet x) {
    x.addAllTo(this);
  }


  /** Add all elements of this set to another NumberSet x. **/
  public void addAllTo(NumberSet x) {
    if (x instanceof BitMapSet)
      ((BitMapSet)x).join(this);
    else {
      int base = 0;
      for (int i = 0; i < bitmap.length; i++) {
        int w = bitmap[i];
        while (w != 0) {
          x.add(base + Misc.binlog(w));
          w &= w - 1; // drop rightmost 1
        }
        base += WORDSIZE;
      }
    }
  }


  /** Join operation. Merge another BitMapSet x. **/
  public void join(BitMapSet x) {
    if (x.bitmap.length > bitmap.length)
      bitmap = grow(bitmap, x.bitmap.length);
    for (int i = 0; i < bitmap.length; i++)
      bitmap[i] |= x.bitmap[i];
  }


  /** Meet operation. Leave elements only those belong to both sets. */
  public void meet(BitMapSet x) {
    int i = 0;
    for (; i < bitmap.length && i < x.bitmap.length; i++)
      bitmap[i] &= x.bitmap[i];
    for (; i < bitmap.length; i++)
      bitmap[i] = 0;
  }


  /** Remove all elements of another NumberSet x from this set. **/
  public void removeAll(NumberSet x) {
    x.removeAllFrom(this);
  }


  /** Remove all elements of this set from another NumberSet x. **/
  public void removeAllFrom(NumberSet x) {
    if (x instanceof BitMapSet)
      ((BitMapSet)x).subtract(this);
    else {
      int base = 0;
      for (int i = 0; i < bitmap.length; i++) {
        int w = bitmap[i];
        while (w != 0) {
          x.remove(base + Misc.binlog(w));
          w &= w - 1; // drop rightmost 1
        }
        base += WORDSIZE;
      }
    }
  }


  /** Subtract operation. Same as this.meet(x.complement) **/
  public void subtract(BitMapSet x) {
    for (int i = 0; i < bitmap.length; i++)
      bitmap[i] &= ~x.bitmap[i];
  }



  /** Return an element greater than or equal to 'from'.
   *   Return -1 if there's no such thing. **/
  public int nextElement(int from) {
    int m = from / WORDSIZE;
    if (m >= bitmap.length)
      return -1;
    int mask = bitmap[m] & ~((1 << (from % WORDSIZE)) - 1);
    while (mask == 0) {
      if (++m >= bitmap.length)
        return -1;
      mask = bitmap[m];
    }
    return (m * WORDSIZE) + Misc.binlog(mask);
  }


  public static class Iterator implements NumberSet.Iterator {
    BitMapSet set;
    int nextNum;

    public Iterator(BitMapSet x) {
      set = x;
      nextNum = 0;
    }

    public boolean hasNext() {
      return set.nextElement(nextNum) >= 0;
    }

    public int next() {
      int val = set.nextElement(nextNum);
      nextNum = val + 1;
      return val;
    }
  }


  /** Enumerate all members. **/
  public NumberSet.Iterator iterator() { return new Iterator(this); }


  /** Convert to array **/
  public void toArray(int[] a) {
    int j = 0;

    int base = 0;
    for (int i = 0; i < bitmap.length; i++) {
      int w = bitmap[i];
      while (w != 0) {
        a[j++] = base + Misc.binlog(w);
        w &= w - 1; // drop rightmost 1
      }
      base += WORDSIZE;
    }
  }


  

  /** Return true if x is same as this set. */
  public boolean equals(Object x) {
    if (!(x instanceof BitMapSet))
      return false;

    int[] v = ((BitMapSet)x).bitmap;
    int n = bitmap.length;
    if (v.length < n)
      n = v.length;
    for (int i = 0; i < n; i++) {
      if (bitmap[i] != v[i])
        return false;
    }
    for (; n < bitmap.length; n++) {
      if (bitmap[n] != 0)
        return false;
    }
    for (; n < v.length; n++) {
      if (v[n] != 0)
        return false;
    }
    return true;
  }

  public Object clone() {
    BitMapSet copy = new BitMapSet(bitmap.length * WORDSIZE);
    for (int i = 0; i < bitmap.length; i++)
      copy.bitmap[i] = bitmap[i];
    return copy;
  }


  public String toString() {
    StringBuffer buf = new StringBuffer();
    buf.append("[");
    String pref = "";
    for (int i = 0; (i = nextElement(i)) >= 0; i++) {
      buf.append(pref + i);
      pref = " ";
    }
    buf.append("]");
    return buf.toString();
  }
    

  /** Unit test driver **/
  public static void main(String argv[]) {
    BitMapSet s = new BitMapSet();

    s.add(1);
    s.add(100);
    s.add(5);
    s.add(33);
    s.add(7);
    System.out.println("s = " + s);

    s.remove(5);
    System.out.println("s = " + s);

    BitMapSet t = new BitMapSet();

    t.add(5);
    t.add(100);
    System.out.println("t = " + t);

    BitMapSet a = (BitMapSet)s.clone();
    System.out.println("a = " + a);
    a.join(t);
    System.out.println("a + t = " + a);

    a = (BitMapSet)s.clone();
    System.out.println("a = " + a);
    a.meet(t);
    System.out.println("a & t = " + a);

    a = (BitMapSet)s.clone();
    System.out.println("a = " + a);
    a.subtract(t);
    System.out.println("a - t = " + a);

    a = (BitMapSet)s.clone();
    a.add(500);
    System.out.println("a = " + a);
    System.out.println("s = " + s);
    System.out.println("a==s? = " + a.equals(s));
    System.out.println("s==a? = " + s.equals(a));

    a.remove(500);
    System.out.println("a = " + a);
    System.out.println("a==s? = " + a.equals(s));
    System.out.println("s==a? = " + s.equals(a));
  }
}
