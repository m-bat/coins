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

import java.awt.*;

/**
 * A class for storing pairs of int arrays.
 *
 * @version 1.1, 30 Sep 1999; Copyright 1996 - 1999 by AT&T Corp.
 * @author  <a href="mailto:john@research.att.com">John Mocenigo</a>, <a href="http://www.research.att.com">Research @ AT&T Labs</a>
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

  //private Rectangle bbox = null;

  /**
   * Constructs and initializes an <code>IntPairs</code> instance
   * with the given size for its arrays.
   *
   * @param size the size of each of the two <code>IntPairs</code> arrays
   */
  public IntPairs(int size) {
    npoints = size;
    this.xArray = new int[size];
    this.yArray = new int[size];

    //bbox = new Rectangle();
  }

  /**
   * Get the size of the arrays in this object.
   *
   * @return the arrays' size
   */
  public int size() {
    return npoints;
  }

  /**
   * Adds a pair of integers at the specified location.
   * 
   * @param x the int to be added to xArray
   * @param y the int to be added to yArray
   * @param n the location in xArray and yArray for adding x and y
   *
   * @exception ArrayIndexOutOfBoundsException whenever n is greater than or equal to size() or less than 0
   */
  public void insertPairAt(int x, int y, int n) throws ArrayIndexOutOfBoundsException {
    // could throw array index out of bounds
    xArray[n] = x;
    yArray[n] = y;
    //if(largest == 0) {
      //bbox.setBounds(x,y,0,0);
    //} else {
      //bbox.add(x,y);
    //}
    largest = (largest <= n) ? n + 1 : largest;
  }

  /**
   * Adds a pair of integers just beyond the last occupied slot in the arrays.
   * 
   * @param x the int to be added to xArray
   * @param y the int to be added to yArray
   *
   * @exception ArrayIndexOutOfBoundsException whenever the last element in the arrays is already occupied
   */
  public void addPair(int x, int y) throws ArrayIndexOutOfBoundsException {
    // could throw array index out of bounds
    xArray[largest] = x;
    yArray[largest] = y;
    //if(largest == 0) {
      //bbox.setBounds(x,y,0,0);
    //} else {
      //bbox.add(x,y);
    //}
    largest++;
  }

  //public Rectangle getBounds() {
    //return bbox;
  //}
}
