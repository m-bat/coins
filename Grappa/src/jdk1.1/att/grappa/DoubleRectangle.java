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
 * An re-implementation of <code>java.awt.Rectangle</code> using doubles instead of ints.
 *
 * @version 1.1, 30 Sep 1999; Copyright 1996 - 1999 by AT&T Corp.
 * @author  <a href="mailto:john@research.att.com">John Mocenigo</a>, <a href="http://www.research.att.com">Research @ AT&T Labs</a>
 */
public class DoubleRectangle implements java.awt.Shape, java.io.Serializable {

  /**
   * The <i>x</i> coordinate of the rectangle.
   */
  public double x;

  /**
   * The <i>y</i> coordinate of the rectangle.
   */
  public double y;

  /**
   * The width of the rectangle.
   */
  public double width;

  /**
   * The height of the rectangle.
   */
  public double height;

  /**
   * Constructs a new rectangle whose top-left corner is at (0,&nbsp;0) 
   * in the coordinate space, and whose width and height are zero. 
   */
  public DoubleRectangle() {
    this(0, 0, 0, 0);
  }

  /**
   * Constructs a new rectangle, initialized to match the values of
   * the specificed rectangle.
   * @param r  a DoubleRectangle from which to copy initial values.
   */
  public DoubleRectangle(DoubleRectangle r) {
    this(r.x, r.y, r.width, r.height);
  }

  /**
   * Constructs a new rectangle, initialized to match the values of
   * the specificed (integer) rectangle.
   * @param r  a rectangle from which to copy initial values.
   */
  public DoubleRectangle(java.awt.Rectangle r) {
    this((double)r.x, (double)r.y, (double)r.width, (double)r.height);
  }

  /**
   * Constructs a new rectangle whose top-left corner is specified as
   * (<code>x</code>,&nbsp;<code>y</code>) and whose width and height 
   * are specified by the arguments of the same name. 
   * @param     x   the <i>x</i> coordinate.
   * @param     y   the <i>y</i> coordinate.
   * @param     width    the width of the rectangle.
   * @param     height   the height of the rectangle.
   */
  public DoubleRectangle(double x, double y, double width, double height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  /**
   * Constructs a new rectangle whose top-left corner is at (0,&nbsp;0) 
   * in the coordinate space, and whose width and height are specified 
   * by the arguments of the same name. 
   * @param     width    the width of the rectangle.
   * @param     height   the height of the rectangle.
   */
  public DoubleRectangle(double width, double height) {
    this(0, 0, width, height);
  }

  /**
   * Constructs a new rectangle whose top-left corner is specified 
   * by the <code>point</code> argument, and whose width and height  
   * are specified by the <code>dimension</code> argument. 
   * @param     p   a DoublePoint, the top-left corner of the rectangle.
   * @param     d   a DoubleDimension, representing the width and height.
   */
  public DoubleRectangle(DoublePoint p, DoubleDimension d) {
    this(p.x, p.y, d.width, d.height);
  }
    
  /**
   * Constructs a new rectangle whose top-left corner is the  
   * specified point, and whose width and height are zero. 
   * @param     p   the top left corner of the rectangle.
   */
  public DoubleRectangle(DoublePoint p) {
    this(p.x, p.y, 0, 0);
  }
    
  /**
   * Constructs a new rectangle whose top left corner is  
   * (0,&nbsp;0) and whose width and height are specified  
   * by the <code>dimension</code> argument. 
   * @param     d   a dimension, specifying width and height.
   */
  public DoubleRectangle(DoubleDimension d) {
    this(0, 0, d.width, d.height);
  }

  /**
   * Gets the closest (using <code>Math.round()</code>) <code>Rectangle</code> to this rectangle.
   * @return    a standard (integer) rectangle, that approximates this one
   */
  public java.awt.Rectangle getApproximation() {
    int ix = (int)Math.round(x);
    int iy = (int)Math.round(y);
    int iw = (int)Math.round(x+width) - ix;
    int ih = (int)Math.round(y+height) - iy;
    return new java.awt.Rectangle(ix,iy,iw,ih);
  }	

  /**
   * Gets the bounding rectangle of this rectangle.
   * <p>
   * This method is included for completeness, to parallel the
   * <code>getBounds</code> method of <code>Component</code>.
   * @return    a new rectangle, equal to the bounding rectangle 
   *                for this rectangle.
   * @see       java.awt.Component#getBounds
   */
  public java.awt.Rectangle getBounds() {
    int ix = (int)Math.floor(x);
    int iy = (int)Math.ceil(y);
    int iw = (int)Math.ceil(x+width) - ix;
    int ih = (int)Math.floor(y+height) - iy;
    return new java.awt.Rectangle(ix,iy,iw,ih);
  }	

  /**
   * Sets the bounding rectangle of this rectangle to match 
   * the specified rectangle.
   * <p>
   * This method is included for completeness, to parallel the
   * <code>setBounds</code> method of <code>Component</code>.
   * @param     r   a rectangle.
   */
  public void setBounds(java.awt.Rectangle r) {
    setBounds((double)r.x, (double)r.y, (double)r.width, (double)r.height);
  }	

  /**
   * Sets this rectangle to match the specified rectangle.
   * <p>
   * @param     r   a DoubleRectangle.
   */
  public void setBounds(DoubleRectangle r) {
    setBounds(r.x, r.y, r.width, r.height);
  }	

  /**
   * Sets this rectangle to the specified 
   * values for <code>x</code>, <code>y</code>, <code>width</code>, 
   * and <code>height</code>.
   * <p>
   * This method is included for completeness, to parallel the
   * <code>setBounds</code> method of <code>Component</code>.
   * @param     x       the new <i>x</i> coordinate for the top-left
   *                    corner of this rectangle.
   * @param     y       the new <i>y</i> coordinate for the top-left
   *                    corner of this rectangle.
   * @param     width   the new width for this rectangle.
   * @param     height  the new height for this rectangle.
   */
  public void setBounds(double x, double y, double width, double height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }	

  /**
   * Gets the location of this rectangle.
   * @return the top-left point of this rectangle
   */
  public DoublePoint getLocation() {
    return new DoublePoint(x, y);
  }	

  /**
   * Moves the rectangle to the specified location.
   * <p>
   * This method is included for completeness, to parallel the
   * <code>setLocation</code> method of <code>Component</code>.
   * @param     p  the new location for the point.
   */
  public void setLocation(DoublePoint p) {
    setLocation(p.x, p.y);
  }	

  /**
   * Moves the rectangle to the specified location.
   * <p>
   * This method is included for completeness, to parallel the
   * <code>setLocation</code> method of <code>Component</code>.
   * @param     x  the <i>x</i> coordinate of the new location.
   * @param     y  the <i>y</i> coordinate of the new location.
   * @see       java.awt.Component#setLocation(int, int)
   */
  public void setLocation(double x, double y) {
    this.x = x;
    this.y = y;
  }	

  /**
   * Translates the rectangle the indicated distance,
   * to the right along the <i>x</i> coordinate axis, and 
   * downward along the <i>y</i> coordinate axis.
   * @param     dx   the distance to move the rectangle 
   *                 along the <i>x</i> axis.
   * @param     dy   the distance to move the rectangle 
   *                 along the <i>y</i> axis.
   */
  public void translate(double x, double y) {
    this.x += x;
    this.y += y;
  }	

  /**
   * Gets the size (width and height) of this rectangle.
   * <p>
   * This method is included for completeness, to parallel the
   * <code>getSize</code> method of <code>Component</code>.
   * @return    a DoubleDimension, representing the size.
   */
  public DoubleDimension getSize() {
    return new DoubleDimension(width, height);
  }	

  /**
   * Sets the size of this rectangle to match the specified dimension.
   * @param d  the new size for the Dimension object
   */
  public void setSize(DoubleDimension d) {
    setSize(d.width, d.height);
  }	

  /**
   * Sets the size of this rectangle to the specified width and height.
   * @param     width    the new width for this rectangle object.
   * @param     height   the new height for this rectangle object.
   */
  public void setSize(double width, double height) {
    this.width = width;
    this.height = height;
  }	

  /**
   * Checks whether this rectangle contains the specified point.
   * @param p the point (location) to test.
   * @return    <code>true</code> if the point 
   *            (<i>x</i>,&nbsp;<i>y</i>) is inside this rectangle; 
   *            <code>false</code> otherwise.
   */
  public boolean contains(DoublePoint p) {
    return contains(p.x, p.y);
  }

  /**
   * Checks whether this rectangle contains the point
   * at the specified location (<i>x</i>,&nbsp;<i>y</i>).
   * @param     x   the <i>x</i> coordinate.
   * @param     y   the <i>y</i> coordinate.
   * @return    <code>true</code> if the point 
   *            (<i>x</i>,&nbsp;<i>y</i>) is inside this rectangle; 
   *            <code>false</code> otherwise.
   */
  public boolean contains(double x, double y) {
    return (x >= this.x) && ((x - this.x) < this.width) && (y >= this.y) && ((y-this.y) < this.height);
  }

  /**
   * Determines whether this rectangle and the specified rectangle  
   * intersect. Two rectangles intersect if their intersection is 
   * nonempty. 
   * @param     r   a DoubleRectangle.
   * @return    <code>true</code> if the specified rectangle 
   *            and this rectangle insersect; 
   *            <code>false</code> otherwise.
   */
  public boolean intersects(DoubleRectangle r) {
    return !((r.x + r.width <= x) ||
	     (r.y + r.height <= y) ||
	     (r.x >= x + width) ||
	     (r.y >= y + height));
  }

  /**
   * Computes the intersection of this rectangle with the 
   * specified rectangle. Returns a new rectangle that 
   * represents the intersection of the two rectangles.
   * @param     r   a DoubleRectangle.
   * @return    the largest rectangle contained in both the 
   *            specified rectangle and in this rectangle.
   */
  public DoubleRectangle intersection(DoubleRectangle r) {
    double x1 = Math.max(x, r.x);
    double x2 = Math.min(x + width, r.x + r.width);
    double y1 = Math.max(y, r.y);
    double y2 = Math.min(y + height, r.y + r.height);
    return new DoubleRectangle(x1, y1, x2 - x1, y2 - y1);
  }

  /**
   * Computes the union of this rectangle with the 
   * specified rectangle. Returns a new rectangle that 
   * represents the union of the two rectangles.
   * @param     r   a rectangle.
   * @return    the smallest rectangle containing both the specified 
   *            rectangle and this rectangle.
   */
  public DoubleRectangle union(DoubleRectangle r) {
    double x1 = Math.min(x, r.x);
    double x2 = Math.max(x + width, r.x + r.width);
    double y1 = Math.min(y, r.y);
    double y2 = Math.max(y + height, r.y + r.height);
    return new DoubleRectangle(x1, y1, x2 - x1, y2 - y1);
  }

  /**
   * Adds a point, specified by the arguments <code>newx</code>
   * and <code>newy</code>, to this rectangle. The resulting rectangle is
   * the smallest rectangle that contains both the original rectangle 
   * and the specified point.
   * <p>
   * After adding a point, a call to <code>contains<code> with the 
   * added point as an argument will not necessarily return 
   * <code>true</code>. The <code>contains</code> method does not 
   * return <code>true</code> for points on the right or bottom 
   * edges of a rectangle. Therefore if the added point falls on 
   * the left or bottom edge of the enlarged rectangle, 
   * <code>contains</code> will return <code>false</code> for that point.
   * 
   * @param     newx   the <i>x</i> coordinate of the new point.
   * @param     newy   the <i>y</i> coordinate of the new point.
   */
  public void add(double newx, double newy) {
    double x1 = Math.min(x, newx);
    double x2 = Math.max(x + width, newx);
    double y1 = Math.min(y, newy);
    double y2 = Math.max(y + height, newy);
    x = x1;
    y = y1;
    width = x2 - x1;
    height = y2 - y1;
  }

  /**
   * Adds the point <code>pt</code> to this rectangle. The resulting 
   * rectangle is the smallest rectangle that contains both the 
   * original rectangle and the specified point.
   * <p>
   * After adding a point, a call to <code>contains<code> with the 
   * added point as an argument will not necessarily return 
   * <code>true</code>. The <code>contains</code> method does not 
   * return <code>true</code> for points on the right or bottom 
   * edges of a rectangle. Therefore if the added point falls on 
   * the left or bottom edge of the enlarged rectangle, 
   * <code>contains</code> will return <code>false</code> for that point.
   * 
   * @param     pt the new point to add to the rectangle.
   */
  public void add(DoublePoint pt) {
    add(pt.x, pt.y);
  }

  /**
   * Adds a rectangle to this rectangle. The resulting rectangle is
   * the union of the two rectangles. 
   * @param     a rectangle.
   */
  public void add(DoubleRectangle r) {
    double x1 = Math.min(x, r.x);
    double x2 = Math.max(x + width, r.x + r.width);
    double y1 = Math.min(y, r.y);
    double y2 = Math.max(y + height, r.y + r.height);
    x = x1;
    y = y1;
    width = x2 - x1;
    height = y2 - y1;
  }

  /**
   * Grows the rectangle both horizontally and vertically.
   * <p>
   * This method modifies the rectangle so that it is 
   * <code>h</code> units larger on both the left and right side, 
   * and <code>v</code> units larger at both the top and bottom. 
   * <p>
   * The new rectangle has (<code>x&nbsp;-&nbsp;h</code>, 
   * <code>y&nbsp;-&nbsp;v</code>) as its top-left corner, a 
   * width of 
   * <code>width</code>&nbsp;<code>+</code>&nbsp;<code>2h</code>, 
   * and a height of 
   * <code>height</code>&nbsp;<code>+</code>&nbsp;<code>2v</code>. 
   * <p>
   * If negative values are supplied for <code>h</code> and 
   * <code>v</code>, the size of the rectangle decreases accordingly. 
   * The <code>grow</code> method does not check whether the resulting 
   * values of <code>width</code> and <code>height</code> are 
   * non-negative. 
   * @param     h   the horizontal expansion.
   * @param     v   the vertical expansion.
   */
  public void grow(double h, double v) {
    x -= h;
    y -= v;
    width += h * 2;
    height += v * 2;
  }

  /**
   * Determines whether this rectangle is empty. A rectangle is empty if 
   * its width or its height is less than or equal to zero. 
   * @return     <code>true</code> if this rectangle is empty; 
   *             <code>false</code> otherwise.
   */
  public boolean isEmpty() {
    return (width <= 0) || (height <= 0);
  }

  /**
   * Returns the hashcode for this rectangle.
   * @return     the hashcode for this rectangle.
   */
  public int hashCode() {
    long xbits = Double.doubleToLongBits(x);
    long ybits = Double.doubleToLongBits(y);
    long wbits = Double.doubleToLongBits(width);
    long hbits = Double.doubleToLongBits(height);
    return (int)(xbits ^ (ybits*37) ^ (wbits*43) ^ (hbits*47));
  }

  /**
   * Checks whether two rectangles are equal.
   * <p>
   * The result is <kbd>true</kbd> if and only if the argument is not 
   * <kbd>null</kbd> and is a <kbd>DoubleRectangle</kbd> object that has the 
   * same top-left corner, width, and height as this rectangle. 
   * @param     obj   the object to compare with.
   * @return    <code>true</code> if the objects are equal; 
   *            <code>false</code> otherwise.
   */
  public boolean equals(Object obj) {
    if (obj instanceof DoubleRectangle) {
      DoubleRectangle r = (DoubleRectangle)obj;
      return (x == r.x) && (y == r.y) && (width == r.width) && (height == r.height);
    }
    return false;
  }

  /**
   * Returns a string representation of this rectangle 
   * and its values.
   * @return     a string representation of this rectangle.
   */
  public String toString() {
    return getClass().getName() + "[x=" + x + ",y=" + y + ",width=" + width + ",height=" + height + "]";
  }
}
