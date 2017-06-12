/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.util;

/** Union-Find algorithm **/
public class UnionFind {
  private int nelements;
  private int[] parent;
  private int[] count;

  /** Create Union/Find data structure. **/
  public UnionFind(int nelements) {
    this.nelements = nelements;
    parent = new int[nelements];
    count = new int[nelements];
    for (int i = 0; i < nelements; i++)
      parent[i] = i;
  }

  /** Return the name of the set which x belongs to. **/
  public int find(int x) {
    int y, w;

    // y = root of x
    for (y = x; parent[y] != y; y = parent[y])
      ;
    // path compression
    for (; (w = parent[x]) != x; x = w)
      parent[x] = y;
    return y;
  }

  /** Merge two sets **/
  public int union(int x, int y) {
    x = find(x);
    y = find(y);
    if (x != y) {
      if (count[x] < count[y]) {
        int w = x; x = y; y = w;
      }
      parent[y] = x;
      if (count[x] == count[y])
        count[x]++;
    }
    return x;
  }

  /** Return true if x and y are members of the same set. **/
  public boolean isEquiv(int x, int y) {
    return (find(x) == find(y));
  }
  
  // Begin(2010.2)
  /** Undo **/
  public void undo(int x) {
	  parent[x]=x;
  }
  // End(2010.2)
}
