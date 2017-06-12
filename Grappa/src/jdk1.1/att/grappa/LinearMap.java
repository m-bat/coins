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
import java.awt.Rectangle;
import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;


/**
 * This class is for scaling co-ordinates to fit a drawing to a specific area.
 * It provides methods for calculating and applying a linear mapping of the
 * form:
 * <center>
 * <code>p' = m * p + b</code>
 * </center>
 * given a starting rectangular containing area and a target rectangular
 * drawing area.
 *
 * @version 1.1, 30 Sep 1999; Copyright 1996 - 1999 by AT&T Corp.
 * @author  <a href="mailto:john@research.att.com">John Mocenigo</a>, <a href="http://www.research.att.com">Research @ AT&T Labs</a>
 */
public class LinearMap extends Observable
{
  /**
   * The default value for recessing the bounding box of the drawing from the 
   * edges of the drawing canvas.  It represents a percentage of the total
   * canvas width (or height, whichever is controlling) that should be reserved,
   * in total, for a margin.  The actual margin is half of that width
   * (or height) on each side of the canvas.
   */
  public static final int PERCENT_MARGIN = 5;
  
  private boolean magnifyOK = false;
  private DoublePoint m = null;
  private DoublePoint b = null;
  private int shrinkPercentage = 0;
  private DoublePoint areaOrigin = null;
  private DoubleDimension areaSize = null;
  private Rectangle drawRectangle = null;
  private double edgeFactor = 1.0;
  private DoubleRectangle boundRect = null;

  /**
   * Create an empty <code>LinearMap</code> instantiation.
   * The object is created with a default mapping defined.
   * @see #setMap()
   */
  public LinearMap() {
    this(LinearMap.PERCENT_MARGIN);
  }

  /**
   * Create an empty <code>LinearMap</code> instantiation.
   * The object is created with the specified edge margin and
   * a default mapping.
   *
   * @param pct the value to use for the edge margin, expressed as a percentage
   * @see #PERCENT_MARGIN
   * @see #setMap()
   */
  public LinearMap(int pct) {
    super();
    setPercentMargin(pct);
    setMap();
  }

  /**
   * Create a <code>LinearMap</code> instantiation.
   * The object is created and the mapping requested is defined.
   *
   * @param areaOrigin the top-left point of the rectangle bounding the area to be mapped
   * @param areaSize the dimensions of the rectangle bounding the area to be mapped
   * @param drawRectangle the rectangle defining the drawing area which is the target of the mapping
   */
  public LinearMap(DoublePoint areaOrigin, DoubleDimension areaSize, Rectangle drawRectangle) {
    super();
    setPercentMargin(LinearMap.PERCENT_MARGIN);
    setMap(areaOrigin, areaSize, drawRectangle);
  }

  /**
   * Create a <code>LinearMap</code> instantiation.
   * The object is created with the specified edge margin,
   * and the mapping requested is defined.
   *
   * @param areaOrigin the top-left point of the rectangle bounding the area to be mapped
   * @param areaSize the dimensions of the rectangle bounding the area to be mapped
   * @param drawRectangle the rectangle defining the drawing area which is the target of the mapping
   * @param pct the value to use for the edge margin, expressed as a percentage
   * @see #PERCENT_MARGIN
   */
  public LinearMap(DoublePoint areaOrigin, DoubleDimension areaSize, Rectangle drawRectangle, int pct) {
    super();
    setPercentMargin(pct);
    setMap(areaOrigin, areaSize, drawRectangle);
  }

  /**
   * Set-up the mapping.
   * The mapping will be from a unit rectangle to a unit rectangle, with some
   * adjustment for the edge margin.
   *
   * @return the previous linear mapping in effect
   */
  public LinearMap setMap() {
    return setMap(new DoublePoint(0,0),new DoubleDimension(1,1),new Rectangle(0,0,1,1));
  }

  /**
   * Indicates if it is OK for the mapping to cause the result to be magnified.
   *
   * @return true if magnification is allowed, false otherwise.
   */
  public boolean getMagnifyOK() {
    return magnifyOK;
  }
  
  /**
   * Sets the magnification rule.
   * When set true, the mapping is allowed to cause the result to be magnified
   * whenever the source rectangle is smaller than the target rectangle.
   * When false, the mapping remains 1-to-1 whenever possible.
   *
   * @param value the new value for the magnification rule
   * @return the former value
   */
  public boolean setMagnifyOK(boolean value) {
    boolean oldValue = magnifyOK;
    magnifyOK = value;
    return oldValue;
  }

  /**
   * Sets the size of the area reserved for the edge margin.
   * The value is expressed as a percentage of the controlling dimension
   * (width or height) that will be reserved for an edge margin in total
   * (i.e., both sides, so half of that is the actual minimum margin width 
   * (or height).
   *
   * @param pct the new value of the edge margin percentage
   * @return the former value
   */
  public int setPercentMargin(int pct) {
    int oldPct = shrinkPercentage;
    if(pct < 100) {
      shrinkPercentage = pct;
      edgeFactor = (double)(100 - shrinkPercentage) / 100.;
    }
    return oldPct;
  }

  /**
   * Set-up the mapping.
   * The mapping will be from the given rectangle to a rectangle
   * with the given dimension located at the origin, with some
   * adjustment for the edge margin.
   *
   * @param box the bounding box of the source area
   * @param size the size of the target area
   * @return the previous linear mapping in effect
   */
  public LinearMap setMap(Rectangle box, Dimension size) {
    return setMap(new DoublePoint(box.x,box.y),new DoubleDimension(box.width,box.height),new Rectangle(0,0,size.width,size.height));
  }

  /**
   * Set-up the mapping.
   * The mapping will be from the rectangle described by the
   * given origin and dimension to the given rectangle, with some
   * adjustment for the edge margin.
   *
   * @param areaOrigin the origin of the source area (top-left corner)
   * @param areaSize the size of the source area
   * @param drawRectangle the target rectangle
   * @return the previous linear mapping in effect
   */
  public LinearMap setMap(DoublePoint areaOrigin, DoubleDimension areaSize, Rectangle drawRectangle) {
    this.m = new DoublePoint(1,1);
    this.b = new DoublePoint(0,0);
    this.drawRectangle = new Rectangle(0,0,1,1);

    this.areaOrigin = new DoublePoint(areaOrigin);
    this.areaSize = new DoubleDimension(areaSize);
    this.drawRectangle = new Rectangle(drawRectangle);


    if(areaSize.width == 0 || areaSize.height == 0) {
      areaSize.setSize((double)drawRectangle.width,(double)drawRectangle.height);
    }

    if(areaSize.width == 0.0 || areaSize.height == 0.0) {
      m.setLocation(1.0,-1.0);
      b.setLocation(0.0,0.0);
    } else {
      /*
       * Underlying calculations are as follows.
       *    Let Px and Py be plotted x and y points,
       *    let Fx and Fy be the input figure points (points
       *    to be scaled), and let Cw and Ch be the core
       *    width and height (i.e., width and height
       *    of the screen area).  Furthermore, let E be
       *    the edge factor (0<=E<=1) and assume the
       *    screen co-ordinates start at the upper left
       *    with value (0,0) and run positively.
       *    Finally, let Fw and Fh be the width and height
       *    of the figure (i.e. Fw = Xmax-Xmin and
       *    Fh = Ymax-Ymin, where Xmax, Xmin, Ymax, Ymin
       *    are the respective min/max for the Fx and Fy).
       *  So:
       *    if(Cw/Ch > Fw/Fh) {
       *       Fx' = (Fx - Xmin)
       *       Px  = Fx' * [(E * Ch)/Fh] + (Cw - Fw * [(E * Ch)/Fh]) / 2
       *       Fy' = (Ymax - Fy)
       *       Py  = Fy' * [(E * Ch)/Fh] + (Ch * [1 - E]) / 2
       *    } else {
       *       Fx' = (Fx - Xmin)
       *       Px  = Fx' * [(E * Cw)/Fw] + (Cw * [1 - E]) / 2
       *       Fy' = (Ymax - Fy)
       *       Py  = Fy' * [(E * Cw)/Fw] + (Ch - Fh * [(E * Cw)/Fw]) / 2
       *    }
       *
       *   The equations below simply re-arrange the above
       *   to generate Xm, Xb, Ym, Yb, where:
       *  
       *        Px = Fx * Xm + Xb
       *        Py = Fy * Ym + Yb
       * 
       *   In other words:
       *
       *    if(Cw/Ch > Fw/Fh) {
       *       Xm = (Ch * E) / Fh
       *       Xb = (Fh * Cw - (2 * Xmin + Fw) * Ch * E) / (2 * Fh)
       *       Ym = - (Ch * E) / Fh
       *       Yb = Ch * ((1 - E) * Fh + 2 * E * Ymax) / (2 * Fh)
       *    } else {
       *       Xm = (Cw * E) / Fw
       *       Xb = Cw * ((1 - E) * Fw - 2 * E * Xmin) / (2 * Fw)
       *       Ym = - (Cw * E) / Fw
       *       Yb = (Fw * Ch + (2 * Ymax - Fh) * Cw * E) / (2 * Fw)
       *    }
       *
       */
      double ratio = 1;
      double eFactor = edgeFactor;
      if ( ((double) (drawRectangle.width) / (double) (drawRectangle.height)) >
	   (areaSize.width / areaSize.height)) {
	ratio = (double) (drawRectangle.height) / areaSize.height;
	double tmp = edgeFactor * ratio;
	if(!magnifyOK && tmp > 1) {
	  tmp = 1;
	  eFactor = 1. / ratio;
	}
	m.setLocation(tmp,-tmp);
	b.setLocation(
	       ratio * (areaSize.height * (double) (drawRectangle.width) / (double)(drawRectangle.height) - (2. * areaOrigin.x + areaSize.width) * eFactor) / 2.,
	       ratio * (2. * eFactor * (areaOrigin.y + areaSize.height) + (1. - eFactor) * areaSize.height) / 2.
	       );
      } else {
	ratio = (double) (drawRectangle.width) / areaSize.width; 
	double tmp = edgeFactor * ratio;
	if(!magnifyOK && tmp > 1) {
	  tmp = 1;
	  eFactor = 1. / ratio;
	}
	m.setLocation(tmp,-tmp);
	b.setLocation(
	       ratio * ((1. - eFactor) * areaSize.width - 2. * eFactor * areaOrigin.x) / 2.,
	       ratio * (areaSize.width * (double) (drawRectangle.height) / (double) (drawRectangle.width) + (2. * (areaOrigin.y + areaSize.height) - areaSize.height) * eFactor) / 2.
	       );
      }
    }
    boundRect = revMap((double)drawRectangle.x,(double)drawRectangle.y,(double)drawRectangle.width,(double)drawRectangle.height);
    setChanged();
    return this;
  }

  /**
   * Perform the reverse mapping on the supplied point.
   * The point in the target area will be mapped to the corresponding point
   * in the source area.
   *
   * @param p the point in the target area
   * @return the point in the source area
   */
  public Point revMap(Point p) {
    return revMap(p.x,p.y);
  }

  /**
   * Perform the reverse mapping on the co-ordinates.
   * The co-ordinates in the target area will be mapped to the
   * corresponding point in the source area.
   *
   * @param x the x co-ordinate in the target area
   * @param y the y co-ordinate in the target area
   * @return the point in the source area
   */
  public Point revMap(int x, int y) {
    return revMap((double)x,(double)y).getApproximation();
  }

  /**
   * Perform the reverse mapping on the supplied point.
   * The point in the target area will be mapped to the corresponding point
   * in the source area.
   *
   * @param p the point in the target area
   * @return the point in the source area
   */
  public DoublePoint revMap(DoublePoint p) {
    return revMap(p.x,p.y);
  }

  /**
   * Perform the reverse mapping on the co-ordinates.
   * The co-ordinates in the target area will be mapped to the
   * corresponding point in the source area.
   *
   * @param x the x co-ordinate in the target area
   * @param y the y co-ordinate in the target area
   * @return the point in the source area
   */
  public DoublePoint revMap(double x, double y) {
    DoublePoint pt = new DoublePoint(0,0);
    if(m.x == 0) {
      throw new RuntimeException("ERROR: somehow LinearMap X slope is 0");
    } else {
      pt.x = (x - b.x) / m.x;
    }
    if(m.y == 0) {
      throw new RuntimeException("ERROR: somehow LinearMap Y slope is 0");
    } else {
      pt.y = (y - b.y) / m.y;
    }
    return pt;
  }

  /**
   * Perform the reverse mapping to a described rectangle.
   * The rectangle in the target area will be mapped to the
   * corresponding rectangle in the source area.
   *
   * @param x the top-left x co-ordinate of the rectangle in the target area
   * @param y the top-left y co-ordinate of the rectangle in the target area
   * @param w the width of the rectangle in the target area
   * @param h the height of the rectangle in the target area
   * @return the rectangle in the source area
   */
  public DoubleRectangle revMap(double x, double y, double w, double h) {
    DoublePoint pt = revMap(x,y);
    // above revMap already tested for zero slope
    return new DoubleRectangle(pt.x,pt.y,w/m.x,h/m.y);
  }

  /**
   * Apply the mapping on the supplied point.
   * The point in the source area will be mapped to the corresponding point
   * in the target area.
   *
   * @param p the point in the source area
   * @return the point in the target area
   */
  public DoublePoint map(DoublePoint p) {
    return map(p.x,p.y);
  }

  /**
   * Apply the mapping on the co-ordinates.
   * The co-ordinates in the source area will be mapped to the
   * corresponding point in the target area.
   *
   * @param x the x co-ordinate in the source area
   * @param y the y co-ordinate in the source area
   * @return the point in the target area
   */
  public DoublePoint map(double x, double y) {
    return new DoublePoint(m.x * x + b.x, m.y * y + b.y);
  }

  /**
   * Apply the mapping on the supplied point.
   * The point in the source area will be mapped to the corresponding point
   * in the target area.
   *
   * @param p the point in the source area
   * @return the point in the target area
   */
  public Point map(Point p) {
    return map(p.x,p.y);
  }

  /**
   * Apply the mapping on the co-ordinates.
   * The co-ordinates in the source area will be mapped to the
   * corresponding point in the target area.
   *
   * @param x the x co-ordinate in the source area
   * @param y the y co-ordinate in the source area
   * @return the point in the target area
   */
  public Point map(int x, int y) {
    return new Point(
		     (int)Math.round(m.x * (double)x + b.x),
		     (int)Math.round(m.y * (double)y + b.y)
		     );
  }

  /**
   * Apply the mapping on the rectangle.
   * The rectangle in the source area will be mapped to the
   * corresponding rectangle in the target area.
   *
   * @param r the rectangle in the source area
   * @return the rectangle in the target area
   */
  public DoubleRectangle map(DoubleRectangle r) {
    return map(r.x,r.y,r.width,r.height);
  }

  /**
   * Apply the mapping on the rectangle co-ordinates.
   * The rectangle in the source area described by
   * the given co-ordinates will be mapped to the
   * corresponding rectangle in the target area.
   *
   * @param x the x co-ordinate in the source area of the top-left rectangle corner
   * @param y the y co-ordinate in the source area of the top-left rectangle corner
   * @param w the width in the source area of the rectangle
   * @param h the height in the source area of the rectangle
   * @return the rectangle in the target area
   */
  public DoubleRectangle map(double x, double y, double w, double h) {
    DoublePoint pt = map(x,y+h);
    DoubleDimension dm = scale(w,h);
    return new DoubleRectangle(pt.x,pt.y,dm.width,dm.height);
  }

  /**
   * Scale the supplied width and height using the mapping.
   * 
   * @param width the x-dimension in the source area
   * @param height the y-dimension in the source area
   * @return the dimension in the target area
   */
  public DoubleDimension scale(double width, double height) {
    return new DoubleDimension(Math.abs(scaleX(width)),Math.abs(scaleY(height)));
  }

  /**
   * Scale the given x dimension.
   *
   * @param x the x dimension in the source area
   * @return the x dimension in the target area
   */
  public double scaleX(double x) {
    return (x * m.x);
  }

  /**
   * Scale the given y dimension.
   *
   * @param y the y dimension in the source area
   * @return the y dimension in the target area
   */
  public double scaleY(double y) {
    return (y * m.y);
  }

  /**
   * Get the source area origin
   *
   * @return the source area origin
   */
  public DoublePoint getAreaOrigin() {
    return areaOrigin;
  }

  /**
   * Get the source area dimension
   *
   * @return the source area dimension
   */
  public DoubleDimension getAreaSize() {
    return areaSize;
  }

  /**
   * Get the target area rectangle
   *
   * @return the target area rectangle
   */
  public Rectangle getDrawRectangle() {
    return drawRectangle;
  }

  /**
   * Determines whether two mappings are equal. Two instances of
   * <code>LinearMap</code> are equal if the values of their 
   * slope and intersection are equal.
   * @param      obj   an object to be compared with this point.
   * @return     <code>true</code> if the object to be compared is
   *                     an instance of <code>LinearMap</code> and has
   *                     the same values; <code>false</code> otherwise.
   * @see #getSlope()
   * @see #getIntersect()
   */
  public boolean equals(Object obj) {
    if(obj == null) return false;
    if(!(obj instanceof LinearMap)) return false;
    LinearMap other = (LinearMap)obj;
    DoublePoint om = other.getSlope();
    DoublePoint ob = other.getIntersect();
    if(m == om && b == ob) return true;
    if(m == null) {
      if(om != null) return false;
    } else {
      if(!m.equals(om)) return false;
    }
    if(b == null) {
      if(ob != null) return false;
    } else {
      if(!b.equals(ob)) return false;
    }
    return true;
  }

  /**
   * Get the slope of the mapping.
   * Namely, return the <code>m</code> of <code>p' = m * p + b</code>.
   *
   * @return the mapping slope
   */
  public DoublePoint getSlope() {
    return this.m;
  }

  /**
   * Get the intersection of the mapping.
   * Namely, return the <code>b</code> of <code>p' = m * p + b</code>.
   *
   * @return the mapping intersection
   */
  public DoublePoint getIntersect() {
    return this.b;
  }

  /**
   * Make a copy of this linear map.
   *
   * @return a copy of this linear map
   */
  public LinearMap copy() {
    LinearMap mapCopy = new LinearMap();
    mapCopy.doCopy(m,b,shrinkPercentage,areaOrigin,areaSize,drawRectangle);
    return mapCopy;
  }

  private void doCopy(DoublePoint m, DoublePoint b, int sP, DoublePoint aO, DoubleDimension aS, Rectangle dR) {
    this.m.setLocation(m.x,m.y);
    this.b.setLocation(b.x,b.y);
    this.shrinkPercentage = sP;
    this.areaOrigin.setLocation(aO.x,aO.y);
    this.areaSize.setSize(aS.width,aS.height);
    this.drawRectangle.setBounds(dR.x,dR.y,dR.width,dR.height);
  }

  /**
   * Get the mapping edge factor.
   * The edge factor is the ratio of the dominant axis of the area recessed
   * by the margin percentage to the corresponding axis of the total area
   * available.
   *
   * @return the edge factor.
   */
  public double getEdgeFactor() {
    return edgeFactor;
  }

  /**
   * Gets the bounding rectangle for the mapping.  The bounding rectangle is
   * the reverse mapping of the drawing area into the original co-ordinates.
   *
   * @return the bounding rectangle
   */
  public DoubleRectangle getBoundRect() {
    return boundRect;
  }

  /**
   * Returns a representation of this mapping as a string.
   * @return    a string representation of this mapping, 
   *                 including the values of its slope and intersection.
   */
  public String toString() {
    return getClass().getName() + "[xSlope=" + m.x + ",ySlope=" + m.y + ",xIntercept=" + b.x + ",yIntercept=" + b.y + "]";
  }
}
