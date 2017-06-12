/*
 *  This software may only be used by you under license from AT&T Corp.
 *  ("AT&T").  A copy of AT&T's Source Code Agreement is available at
 *  AT&T's Internet website having the URL:
 *  <http://www.research.att.com/sw/tools/graphviz/license/source.html>
 *  If you received this software without first entering into a license
 *  with AT&T, you have an infringing copy of this software and cannot use
 *  it without violating AT&T's intellectual property rights.
 */

package att.grappa;

/**
 * A class for storing pairs of int arrays.
 *
 * @version 0.3, 05 Dec 1996; Copyright 1996 by AT&T Corp.
 * @author  John Mocenigo (john@research.att.com)
 */
public class IntPairs
{
  /**
   * The x array.
   */
  public int xArray[];

  /**
   * The y array.
   */
  public int yArray[];

  private int npoints = 0;
  private int largest = 0;

  /**
   * Constructs and initializes an IntPairs with the given size
   * coordinates.
   * @param size the size of each of the arrays
   */
  public IntPairs(int size) {
    npoints = size;
    this.xArray = new int[size];
    this.yArray = new int[size];
  }

  public int size() {
    return npoints;
  }

  public void insertPairAt(int x, int y, int n) {
    // could throw array index out of bounds
    xArray[n] = x;
    yArray[n] = y;
    largest = (largest <= n) ? n + 1 : largest;
  }

  public void addPair(int x, int y) {
    // could throw array index out of bounds
    xArray[largest] = x;
    yArray[largest] = y;
    largest++;
  }
}
