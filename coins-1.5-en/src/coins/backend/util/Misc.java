/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.util;

/** Miscellaneous functions **/
public class Misc {


  /** Sort integer vector in ascending order. **/
  public static void sort(int[] a, int n) {
    quicksort(a, 0, n - 1);
  }

  private static void quicksort(int[] a, int m, int n) {
    int i, j;
    int v;
    int x;
    
    if (n <= m) return;
    
    i = m - 1; j = n; v = a[n];
    for (;;) {
      while (a[++i] < v)
        ;
      while (--j > i && a[j] >= v)
        ;
      if (i >= j)
        break;
      x = a[i]; a[i] = a[j]; a[j] = x;
    }
    x = a[i]; a[i] = a[n]; a[n] = x;
    quicksort(a, m, i - 1);
    quicksort(a, i + 1, n);
  }


  /** Round up to next powers of 2.
   ** @param x number to be rounded up.
   ** @return next powers of 2 greater than or equal to x. **/
  public static int clp2(int x) {
    x--;
    x |= x >> 1;
    x |= x >> 2;
    x |= x >> 4;
    x |= x >> 8;
    x |= x >> 16;
    return x + 1;
  }


  /** Return true if x is powers of 2. **/
  public static boolean powersOf2(long x) {
    return (((x - 1) & x) == 0);
  }

  /** Return binary logarithm. **/
  public static int binlog(long pattern) {
    final int[] logtbl = { 0, 0, 1, 0, 2, 0, 1, 0,
                           3, 0, 1, 0, 2, 0, 1, 0 };
    if (pattern == 0)
      return -1;

    int n = 0;
    if ((pattern & 0xffffffff) == 0) {
      pattern >>= 32;
      n = 32;
    }
    if ((pattern & 0xffff) == 0) {
      pattern >>= 16;
      n += 16;
    }
    if ((pattern & 0xff) == 0) {
      pattern >>= 8;
      n += 8;
    }
    if ((pattern & 0xf) == 0) {
      pattern >>= 4;
      n += 4;
    }
    return logtbl[(int)pattern & 0xf] + n;
  }


  /** Return rightmost 1's position **/
  public static int binlog(int pattern) {
    final int[] logtbl = { 0, 0, 1, 0, 2, 0, 1, 0,
                           3, 0, 1, 0, 2, 0, 1, 0 };
    if (pattern == 0)
      return -1;

    int n = 0;
    if ((pattern & 0xffff) == 0) {
      pattern >>= 16;
      n = 16;
    }
    if ((pattern & 0xff) == 0) {
      pattern >>= 8;
      n += 8;
    }
    if ((pattern & 0xf) == 0) {
      pattern >>= 4;
      n += 4;
    }
    return logtbl[pattern & 0xf] + n;
  }


  /** Return number of 1's in x. **/
  public static int count1s(int x) {
    int x1 = ((x  & 0xaaaaaaaa) >>>  1) + (x  & 0x55555555);
    int x2 = ((x1 & 0xcccccccc) >>>  2) + (x1 & 0x33333333);
    int x3 = ((x2 & 0xf0f0f0f0) >>>  4) + (x2 & 0x0f0f0f0f);
    int x4 = ((x3 & 0xff00ff00) >>>  8) + (x3 & 0x00ff00ff);
    int x5 = ((x4 & 0xffff0000) >>> 16) + (x4 & 0x0000ffff);
    return x5;
  }

}
