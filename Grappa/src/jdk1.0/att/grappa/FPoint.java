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

import java.awt.Point;

/**
 * A double precision version of Point
 *
 * @version 0.3, 05 Dec 1996; Copyright 1996 by AT&T Corp.
 * @author  John Mocenigo (john@research.att.com)
 * @see java.awt.Point
 */
public class FPoint
{
  /**
   * The x coordinate.
   */
  public double x;

  /**
   * The y coordinate.
   */
  public double y;

  /**
   * Constructs and initializes a FPoint to (0,0)
   */
  public FPoint() {
    this.x = 0;
    this.y = 0;
  }

  /**
   * Constructs and initializes a FPoint from the specified x and y 
   * coordinates.
   * @param x the x coordinate
   * @param y the y coordinate
   */
  public FPoint(double x, double y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Constructs and initializes a FPoint from the specified FPoint.
   * @param pt the initial FPoint.
   */
  public FPoint(FPoint pt) {
    if(pt == null) {
      throw new IllegalArgumentException("argument to FPoint constructor cannot be null");
    }
    this.x = pt.x;
    this.y = pt.y;
  }

  /**
   * Constructs and initializes a FPoint from the specified Point.
   * @param pt the initial Point.
   */
  public FPoint(Point pt) {
    if(pt == null) {
      throw new IllegalArgumentException("argument to FPoint constructor cannot be null");
    }
    this.x = (double)pt.x;
    this.y = (double)pt.y;
  }

  /**
   * Moves the point.
   *
   * @param x new X value.
   * @param y new Y value.
   */
  public void move(double x, double y) {
    this.x = x;
    this.y = y;
  }	

  public void move(FPoint pt) {
    this.x = pt.x;
    this.y = pt.y;
  }	

  /**
   * Scales the point.
   *
   * @param xFactor the x-axis scale factor.
   * @param yFactor the y-axis scale factor.
   */
  public void scale(double xFactor, double yFactor) {
    this.x *= xFactor;
    this.y *= yFactor;
  }	

  /**
   * Uniformly scales the point.
   *
   * @param factor the scale factor.
   */
  public void scale(double factor) {
    this.x *= factor;
    this.y *= factor;
  }	

  /**
   * Translates the point.
   *
   * @param x X translation amount.
   * @param y Y translation amount.
   */
  public void translate(double x, double y) {
    this.x += x;
    this.y += y;
  }	

  public void translate(FPoint pt) {
    this.x += pt.x;
    this.y += pt.y;
  }	

  /**
   * @return the hashcode for this FPoint.
   */
  public int hashCode() {
    long xbits = Double.doubleToLongBits(x);
    long ybits = Double.doubleToLongBits(y);
    return (int)(xbits ^ (ybits*31));
  }

  /**
   * Checks whether this and the supplied reference are equal.
   *
   * @param obj the reference to be checked for equality.
   * @return true if equal, false otherwise.
   */
  public boolean equals(Object obj) {
    if(obj == null) return false;
    if (!(obj instanceof FPoint)) return false;
    FPoint pt = (FPoint)obj;
    return (x == pt.x) && (y == pt.y);
  }

  /**
   * @return the String representation of this FPoint's coordinates.
   */
  public String toString() {
    return getClass().getName() + "[x=" + x + ",y=" + y + "]";
  }

  public static FPoint interpolate(double factor, FPoint Pt0, FPoint Pt1) {
    return new FPoint(Pt0.x + factor * (Pt1.x - Pt0.x), Pt0.y + factor * (Pt1.y - Pt0.y));
  }
}
