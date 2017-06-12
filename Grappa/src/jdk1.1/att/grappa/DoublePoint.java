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
 * A re-implementation of <code>java.awt.Point</code> using doubles instead of ints.
 *
 * @version 1.1, 30 Sep 1999; Copyright 1996 - 1999 by AT&T Corp.
 * @author  <a href="mailto:john@research.att.com">John Mocenigo</a>, <a href="http://www.research.att.com">Research @ AT&T Labs</a>
 */
public class DoublePoint implements java.io.Serializable {
  /**
   * The <i>x</i> coordinate. 
   */
  public double x;

  /**
   * The <i>y</i> coordinate. 
   */
  public double y;

  /**
   * Constructs and initializes a point at the origin 
   * (0,&nbsp;0) of the coordinate space. 
   * @param       x   the <i>x</i> coordinate.
   * @param       y   the <i>y</i> coordinate.
   */
  public DoublePoint() {
    this(0, 0);
  }

  /**
   * Constructs and initializes a point with the same location as
   * the specified <code>DoublePoint</code> object.
   * @param       p a point.
   */
  public DoublePoint(DoublePoint p) {
    this(p.x, p.y);
  }

  /**
   * Constructs and initializes a point at the specified 
   * (<i>x</i>,&nbsp;<i>y</i>) location in the coordinate space. 
   * @param       x   the <i>x</i> coordinate.
   * @param       y   the <i>y</i> coordinate.
   */
  public DoublePoint(double x, double y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Constructs and initializes a <code>DoublePoint</code> from the specified <code>Point</code>.
   * @param pt the initial <code>java.awt.Point</code>.
   */
  public DoublePoint(java.awt.Point pt) {
    this.x = (double)pt.x;
    this.y = (double)pt.y;
  }

  /**
   * Sets the location of this point.
   *
   * @param x new X value.
   * @param y new Y value.
   */
  public void setLocation(int x, int y) {
    this.x = (double)x;
    this.y = (double)y;
  }	

  /**
   * Rounds the <code>DoublePoint</code> to a standard (integer) <code>Point</code>
   *
   * @return the <code>DoublePoint</code> rounded to the nearest <code>Point</code>
   */
  public java.awt.Point getApproximation() {
    return new java.awt.Point((int)Math.round(x),(int)Math.round(y));
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
   * Computes the point that lies some fraction of the distance along the line
   * connecting the two supplied points.
   *
   * @param fraction the fraction of the distance along the line at which to
   *                 compute the new point (a 0 value indicates point <code>Pt0</code> and
   *                 a 1 value indicates point <code>Pt1</code>).
   * @param Pt0 the starting point of the connecting line
   * @param Pt1 the ending point of the connecting line
   *
   * @exception IllegalArgumentException whenever <code>fraction</code> is not between 0 and 1, inclusive
   *
   * @return the point lying at the specified portion along the connecting line
   */
  public static DoublePoint interpolate(double fraction, DoublePoint Pt0, DoublePoint Pt1) throws IllegalArgumentException {
    if(fraction < 0 || fraction > 1) {
      throw new IllegalArgumentException("value of \"fraction\" must be between 0 and 1, inclusive");
    }
    return new DoublePoint(Pt0.x + fraction * (Pt1.x - Pt0.x), Pt0.y + fraction * (Pt1.y - Pt0.y));
  }

  /**
   * Sets the location of the point to the specificed location.
   * @param       p  a point, the new location for this point.
   */
  public void setLocation(DoublePoint p) {
    setLocation(p.x, p.y);
  }	

  /**
   * Changes the point to have the specificed location.
   * @param       x  the <i>x</i> coordinate of the new location.
   * @param       y  the <i>y</i> coordinate of the new location.
   */
  public void setLocation(double x, double y) {
    this.x = x;
    this.y = y;
  }	

  /**
   * Translates this point, at location (<i>x</i>,&nbsp;<i>y</i>), 
   * by <code>dx</code> along the <i>x</i> axis and <code>dy</code> 
   * along the <i>y</i> axis so that it now represents the point 
   * (<code>x</code>&nbsp;<code>+</code>&nbsp;<code>dx</code>, 
   * <code>y</code>&nbsp;<code>+</code>&nbsp;<code>dy</code>). 
   * @param       dx   the distance to move this point 
   *                            along the <i>x</i> axis.
   * @param       dy    the distance to move this point 
   *                            along the <i>y</i> axis.
   */
  public void translate(double x, double y) {
    this.x += x;
    this.y += y;
  }	

  /**
   * Returns the hashcode for this point.
   * @return      a hash code for this point.
   */
  public int hashCode() {
    long xbits = Double.doubleToLongBits(x);
    long ybits = Double.doubleToLongBits(y);
    return (int)(xbits ^ (ybits*31));
  }

  /**
   * Determines whether two points are equal. Two instances of
   * <code>Point</code> are equal if the values of their 
   * <code>x</code> and <code>y</code> member fields, representing
   * their position in the coordinate space, are the same.
   * @param      obj   an object to be compared with this point.
   * @return     <code>true</code> if the object to be compared is
   *                     an instance of <code>DoublePoint</code> and has
   *                     the same values; <code>false</code> otherwise.
   */
  public boolean equals(Object obj) {
    if (obj instanceof DoublePoint) {
      DoublePoint pt = (DoublePoint)obj;
      return (x == pt.x) && (y == pt.y);
    }
    return false;
  }

  /**
   * Returns a representation of this point and its location
   * in the (<i>x</i>,&nbsp;<i>y</i>) coordinate space as a string.
   * @return    a string representation of this point, 
   *                 including the values of its member fields.
   */
  public String toString() {
    return getClass().getName() + "[x=" + x + ",y=" + y + "]";
  }
}
