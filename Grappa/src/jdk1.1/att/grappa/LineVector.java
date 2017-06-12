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

import java.util.Vector;
import java.util.NoSuchElementException;
import java.awt.Point;

/**
 * Calculates and stores a bezier spline of degree 3 from a Vector of
 * control points.
 *
 * @version 1.1, 30 Sep 1999; Copyright 1996 - 1999 by AT&T Corp.
 * @author  <a href="mailto:john@research.att.com">John Mocenigo</a>, <a href="http://www.research.att.com">Research @ AT&T Labs</a>
 */
public class LineVector extends Vector
{
  /**
   * Construct an empty line vector.
   */
  public LineVector() {
    //super();
  }

  /**
   * Constructs an empty line vector with the specified initial capacity and
   * capacity increment. 
   *
   * @param   initialCapacity     the initial capacity of the vector.
   * @param   capacityIncrement   the amount by which the capacity is
   *                              increased when the vector overflows.
   */
  public LineVector(int initialCapacity, int capacityIncrement) {
    super(initialCapacity,capacityIncrement);
  }

  /**
   * Constructs an empty line vector with the specified initial capacity.
   *
   * @param   initialCapacity   the initial capacity of the vector.
   */
  public LineVector(int initialCapacity) {
    super(initialCapacity);
  }

  /**
   * Get the line point at the given offset.
   *
   * @param idx the index of the line point to return (0 is the first point)
   *
   * @return the line point at the specified index
   */
  public Point getPointAt(int idx) {
    return (Point)elementAt(idx);
  }

  /**
   * Get the first line point.
   *
   * @return the first line point
   */
  public Point firstPoint() throws NoSuchElementException {
    return (Point)firstElement();
  }

  /**
   * Get the last line point.
   *
   * @return the last line point
   */
  public Point lastPoint() throws NoSuchElementException {
    return (Point)lastElement();
  }

  /**
   * Add a point to this vector.
   *
   * @param pt the point to be added
   */
  public void addPoint(Point pt) {
    addElement(pt);
  }

  /**
   * Add a point to this vector.
   *
   * @param x the x-coordinate of the point to be added
   * @param y the y-coordinate of the point to be added
   */
  public void addPoint(int x, int y) {
    addPoint(new Point(x,y));
  }

  public boolean equals(LineVector obj) {
    int sz = size();
    int i = 0;
    if(obj != null && obj.size() == sz) {
      Point p1, p2;
      for(i = 0; i < sz; i++) {
	p1 = obj.getPointAt(i);
	p2 = getPointAt(i);
	if(p1 != p2 && p1 != null && p2 != null && (p1.x != p2.x || p1.y != p2.y)) {
	  break;
	}
      }
    }
    return (sz == i);
  }

  /**
   * Check if object describes a straight line.
   *
   * @return true, if line is straight or there are less than 2 points.
   */
  public boolean isStraight() {
    if(size() <= 2) return true;
    boolean straight = true;
    Point pt = null;
    java.util.Enumeration enum = elements();
    Point firstPt = (Point)enum.nextElement();
    Point lastPt = lastPoint();
    int dy = lastPt.y - firstPt.y;
    int dx = lastPt.x - firstPt.x;
    while(enum.hasMoreElements()) {
      pt = (Point)enum.nextElement();
      if((pt.x - firstPt.x) * dy != (pt.y - firstPt.y) * dx) {
	straight = false;
	break;
      }
    }
    return(straight);
  }
}
