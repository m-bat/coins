/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.util;

import java.util.Random;


class SetTest {

  /** NumberSet test driver **/

  void test1(String kind, BitMapSet s, BitMapSet t) {
    System.out.println("Test for " + kind);

    s.add(1);
    s.add(100);
    s.add(5);
    s.add(33);
    s.add(7);
    System.out.println("s = " + s);

    s.remove(5);
    System.out.println("s = " + s);

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

  void setprint(NumberSet x) {
    for (NumberSet.Iterator it = x.iterator(); it.hasNext(); )
      System.out.print(" " + it.next());
  }
      
  Random r = new Random();

  void fillRandom(NumberSet x, int n, int max) {
    for (int i = 0; i < n; i++) {
      int v = r.nextInt(max);
      x.add(v);
    }
  }

  void compare(NumberSet x, NumberSet y) {
    String xname = x.getClass().getName();
    String yname = y.getClass().getName();
    if (x.size() != y.size())
      System.out.println(xname + " size " + x.size()
                         + ", " + yname + " size " + y.size());

    boolean ok = true;
    for (NumberSet.Iterator it = x.iterator(); it.hasNext(); ) {
      int val = it.next();
      if (!y.contains(val)) {
        System.out.println(xname + " x has " + val
                           + " but " + yname + " y doesn't.");
        ok = false;
      }
    }
    if (ok)
      System.out.println(xname + " x and " + yname + " y match.");
  }



  public void run() {
    int n = 50;
    int m = 1000;

    HashNumberSet h = new HashNumberSet(m);
    r.setSeed(77611071);
    fillRandom(h, n, m);
    BitMapSet b = new BitMapSet(m);
    r.setSeed(77611071);
    fillRandom(b, n, m);
    VectorSet v = new VectorSet(m);
    r.setSeed(77611071);
    fillRandom(v, n, m);

    compare(h, b);
    compare(b, v);
    compare(v, h);

    System.out.println("Hash size: " + h.tableSize());

    r.setSeed(19580330);
    v.clear();
    fillRandom(v, 100, m);
    h.addAll(v);
    b.addAll(v);
    compare(h, b);

    System.out.println("Hash size: " + h.tableSize());

    r.setSeed(77611071);
    b.clear();
    fillRandom(b, n, m);
    r.setSeed(77611071);
    v.clear();
    fillRandom(v, n, m);

    r.setSeed(19580330);
    h.clear();
    fillRandom(h, 100, m);
    b.addAll(h);
    v.addAll(h);
    compare(b, v);

    System.out.println("Hash size: " + h.tableSize());

    r.setSeed(77611071);
    v.clear();
    fillRandom(v, n, m);
    r.setSeed(77611071);
    h.clear();
    fillRandom(h, n, m);

    r.setSeed(19580330);
    b.clear();
    fillRandom(b, 100, m);
    v.addAll(b);
    h.addAll(b);
    compare(v, h);

    System.out.println("Hash size: " + h.tableSize());
  }


  public static void main(String argv[]) {
    new SetTest().run();
  }
  
}
