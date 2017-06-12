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
import java.awt.Point;

/**
 * Calculates and stores a bezier spline of degree 3 from a Vector of
 * control points.
 *
 * @version 1.1, 30 Sep 1999; Copyright 1996 - 1999 by AT&T Corp.
 * @author  <a href="mailto:john@research.att.com">John Mocenigo</a>, <a href="http://www.research.att.com">Research @ AT&T Labs</a>
 */
public class BezierSpline extends LineVector
{
  /*
   * Bezier matrix
   */
  private static double[][] bezierMatrix = {
    {-1,  3, -3,  1},
    { 3, -6,  3,  0},
    {-3,  3,  0,  0},
    { 1,  0,  0,  0}
  };

  /*
   * Bezier multiplier for spline drawing
   */
  private static double bezierMultiplier = 1.0 / 6.0;

  /*
   * Bezier precision for spline drawing
   */
  private static int bezierPrecision = 15;

  /*
   * Step size for spline drawing
   */
  private static double stepSize = 1.0 / (double)bezierPrecision;

  /*
   * Bezier precision matrix
   */
  private static double[][] bezierPrecisionMatrix = {
    { 0, 0, 0, 1 },
    { stepSize * stepSize * stepSize, stepSize * stepSize, stepSize, 0 },
    { 6.0 * stepSize * stepSize * stepSize, 2.0 * stepSize * stepSize, 0, 0 },
    { 6.0 * stepSize * stepSize * stepSize, 0, 0, 0 }
  };

  /**
   * Sets the bezier precision to the specified value.  The precision
   * determines how many spline points, in addition to the control points,
   * are to be computed are to be computed for each control point.
   * The default is 15.
   *
   * @param prec the new precision value
   *
   * @return the previous precision value
   */
  public static int setBezierPrecision(int prec) {
    int oldPrec = bezierPrecision;
    if(prec > 1 && prec != oldPrec) {
      stepSize = 1.0 / (double)bezierPrecision;
      setBezierPrecisionMatrix();
    }
    return oldPrec;
  }

  /**
   * Gets the current bezier precision.
   *
   * @return the current precision value
   */
  public static int getBezierPrecision() {
    return bezierPrecision;
  }

  private static int bezierDegree =  3;
  private static double[][] geometry = new double[bezierDegree+1][2];
  private static double[][] cg = new double[bezierDegree+1][2];
  private static double[][] plot = new double[bezierDegree+1][2];

  // to be made public once additional degrees are allowed
  private static int setBezierDegree(int deg) {
    // begin temporary code
    if(deg != 3) {
      throw new IllegalArgumentException("feature to setBezierDegree not yet implemented");
    }
    // end temporary code
    int oldDeg = bezierDegree;
    if(deg > 2 && deg != oldDeg) {
      bezierDegree = deg;
      setBezierMatrix();
      setBezierPrecisionMatrix();
      if(geometry.length < (bezierDegree+1)) {
	geometry = new double[bezierDegree+1][2];
	cg = new double[bezierDegree+1][2];
	plot = new double[bezierDegree+1][2];
      }
    }
    return oldDeg;
  }
  
  // @see setBezierDegree
  private static int getBezierDegree() {
    return bezierDegree;
  }

  // not really implemented
  private static void setBezierPrecisionMatrix() {
    if(bezierPrecisionMatrix.length < (bezierDegree+1)) {
      bezierPrecisionMatrix = new double[bezierDegree+1][bezierDegree+1];
    }
    for(int i = 0; i <= bezierDegree; i++) {
      for(int j = 0; j <= bezierDegree; j++) {
	bezierPrecisionMatrix[i][j] = 0;
      }
    }

    // need formula (only have degree == 3)

    bezierPrecisionMatrix[0][3] = 1;

    bezierPrecisionMatrix[1][2] = stepSize;
    bezierPrecisionMatrix[1][1] = stepSize * stepSize;
    bezierPrecisionMatrix[1][0] = stepSize * stepSize * stepSize;

    bezierPrecisionMatrix[2][1] = 2.0 * stepSize * stepSize;
    bezierPrecisionMatrix[2][0] = 6.0 * stepSize * stepSize * stepSize;

    bezierPrecisionMatrix[3][0] = bezierPrecisionMatrix[2][0];
  }

  // not really implemented
  private static void setBezierMatrix() {
    if(bezierMatrix.length < (bezierDegree+1)) {
      bezierMatrix = new double[bezierDegree+1][bezierDegree+1];
    }
    for(int i = 0; i <= bezierDegree; i++) {
      for(int j = 0; j <= bezierDegree; j++) {
	bezierMatrix[i][j] = 0;
      }
    }

    // need formula (only have degree == 3)

    bezierMatrix[0][0] = -1;
    bezierMatrix[0][1] = 3;
    bezierMatrix[0][2] = -3;
    bezierMatrix[0][3] = 1;

    bezierMatrix[1][0] = 3;
    bezierMatrix[1][1] = -6;
    bezierMatrix[1][2] = 3;

    bezierMatrix[2][0] = -3;
    bezierMatrix[2][1] = 3;

    bezierMatrix[3][0] = 1;
  }

  static {
    setBezierPrecision(bezierPrecision);
    setBezierDegree(bezierDegree);
  }

  /*
   * This function performs the meat of the calculations for the
   * curve plotting. The first matrix is the curve matrix (either the Bezier
   * or precision matrix) with size (bezierDegree+1)x(bezierDegree+1), and
   * the second matrix is the geometry matrix (defined by the control points)
   * with size (bezierDegree+1)x2.
   * To save memory allocation, an answer matrix ((bezierDegree+1)x2) should
   * also supplied.
   */
  private void bezierMultiply(double[][] curve, double[][] geometry, double[][] answer) {

    // Perform the matrix math
    for (int i = 0; i <= bezierDegree; i++) {
      for (int j = 0; j < 2; j++) {
	answer[i][j] = 0;
        for (int k = 0; k <= bezierDegree; k++) {
          answer[i][j] = answer[i][j] + (curve[i][k] * geometry[k][j]);
	}
      }
    }
  }

  // unused at present
  private Point evaluateBezier(Vector pts, double parameter, DoublePoint[] left, DoublePoint[] right) {

    if(eb.length <= splineDegree) {
      eb = new DoublePoint[splineDegree+1][splineDegree+1];
    }

    // copy control points
    for(int i = 0; i <= splineDegree; i++) {
      Point fp = (Point)pts.elementAt(i);
      eb[0][i] = new DoublePoint((double)fp.x,(double)fp.y);
    }

    // triangle computation
    for(int i = 1; i <=splineDegree; i++) {
      for(int j = 0; j <= splineDegree - i; j++) {
	eb[i][j] = new DoublePoint(((1.0-parameter)*eb[i-1][j].x + parameter * eb[i-1][j+1].x),((1.0-parameter)*eb[i-1][j].y + parameter * eb[i-1][j+1].y));
      }
    }

    if(left != null) {
      if(left.length <= splineDegree) {
	throw new ArrayIndexOutOfBoundsException("supplier \"left\" array is too small - should be at least " + (splineDegree+1));
      }
      for(int i = 0; i <= splineDegree; i++) {
	left[i] = eb[i][0];
      }
    }
    if(right != null) {
      if(right.length <= splineDegree) {
	throw new ArrayIndexOutOfBoundsException("supplier \"right\" array is too small - should be at least " + (splineDegree+1));
      }
      for(int i = 0; i <= splineDegree; i++) {
	right[i] = eb[splineDegree-i][i];
      }
    }

    return new Point((int)Math.round(eb[splineDegree][0].x),(int)Math.round(eb[splineDegree][0].y));
  }

  /*
   * Compute a spline using a bezier curve
   *
   */
  private void computeSpline(Vector linePoints) {
    if(linePoints == null) {
      throw new NullPointerException("cannot compute spline from a null set of points");
    }
    computeSpline(linePoints,0,-1);
  }

  private void computeSpline(Vector linePoints,int start, int npts) {
    removeAllElements();
    if(linePoints == null) {
      throw new NullPointerException("cannot compute spline from a null set of points");
    }
    if(start < 0) {
      throw new IllegalArgumentException("starting index must be >= 0");
    }
    if(npts < 0) {
      npts = linePoints.size() - start;
    }
    if(npts <= bezierDegree || (start+npts) > linePoints.size()) {
      throw new IllegalArgumentException("must have at least " + (bezierDegree+1) + " line points to generate a spline using Bezier curves of degree " + bezierDegree + " (has layout been performed?)");
    }
    ensureCapacity((npts - bezierDegree) * bezierPrecision + 1);
    splineDegree = bezierDegree;
    splinePrecision = bezierPrecision;

    Point aPoint, pt1, pt2;
    DoublePoint fpt1 = new DoublePoint(0,0);
    DoublePoint fpt2 = new DoublePoint(0,0);
    int count = 0;
    for (int i = 0; i < npts - bezierDegree;) {
      // (bezierDegree+1) control points are needed to create a curve.
      // If all the control points are used, the last series of
      // points begins with point npts-(bezierDegree+1).
      for (int j = 0; j <= bezierDegree; j++) {
	aPoint = (Point)linePoints.elementAt(i + j);
        geometry[j][0] = aPoint.x;
        geometry[j][1] = aPoint.y;
      }
      bezierMultiply(bezierMatrix,geometry,cg);

      // The beginning of the next Bezier curve is the last
      // point of the previous curve.
      i += bezierDegree;

      // In order to plot the curve using forward differences
      // (a speedier way to plot the curve), another matrix
      // calculation is required, taking into account the precision
      // of the curve.
      bezierMultiply(bezierPrecisionMatrix, cg, plot);
      fpt1.setLocation(plot[0][0],plot[0][1]);
      fpt2.setLocation(fpt1);

      addElement(new Point((int) fpt2.x,(int) fpt2.y));

      // Plot the curve using the forward difference method
      for (int j = 0; j < bezierPrecision; j++) {
        fpt2.translate(plot[1][0],plot[1][1]);
	for(int k = 2; k <= bezierDegree; k++) {
	  plot[k-1][0] += plot[k][0];
	  plot[k-1][1] += plot[k][1];
	}
	addElement(new Point((int) fpt2.x,(int) fpt2.y));
        count++;
        fpt1.setLocation(fpt2);
      }
    }
  }

  private int splineDegree = bezierDegree;
  private int splinePrecision = bezierPrecision;
  private DoublePoint[][] eb = new DoublePoint[splineDegree+1][splineDegree+1];
  
  /**
   * Construct an empty (uncomputed) bezier spline.
   */
  public BezierSpline() {
    //super();
  }

  /**
   * Construct a bezier spline for the given vector of control points.
   *
   * @param a vector of Point objects that specifies the control points for
   *        bezier spline.
   */
  public BezierSpline(Vector linePoints) {
    super();
    computeSpline(linePoints);
  }

  /**
   * Construct a bezier spline for the given vector of control points.
   *
   * @param a vector of Point objects that specifies the control points for
   *        bezier spline.
   * @param start start with points beginning at this offset 
   */
  public BezierSpline(Vector linePoints, int start) {
    super();
    computeSpline(linePoints,start,-1);
  }

  /**
   * Construct a bezier spline for the given vector of control points.
   *
   * @param a vector of Point objects that specifies the control points for
   *        bezier spline.
   * @param start start with points beginning at this offset 
   * @param count the number of points in the vector to use
   */
  public BezierSpline(Vector linePoints, int start, int count) {
    super();
    computeSpline(linePoints,start,count);
  }

  /**
   * Compute the bezier spline for the given vector of control points.
   *
   * @param a vector of Point objects that specifies the control points for
   *        bezier spline.
   */
  public BezierSpline setSpline(Vector linePoints) {
    computeSpline(linePoints);
    return this;
  }

  /**
   * Construct a bezier spline for the given vector of control points.
   *
   * @param a vector of Point objects that specifies the control points for
   *        bezier spline.
   * @param start start with points beginning at this offset 
   * @param count the number of points in the vector to use
   */
  public BezierSpline setSpline(Vector linePoints, int start, int count) {
    computeSpline(linePoints,start,count);
    return this;
  }

  /**
   * Get the spline point at the given offset.
   *
   * @param idx the index of the spline point to return (0 is the first point)
   *
   * @return the spline point at the specified index
   */
  public Point getPointAt(int idx) {
    return (Point)elementAt(idx);
  }

  /**
   * Get the first spline point.
   *
   * @return the first spline point
   */
  public Point firstPoint() {
    return (Point)firstElement();
  }

  /**
   * Get the last spline point.
   *
   * @return the last spline point
   */
  public Point lastPoint() {
    return (Point)lastElement();
  }

  /**
   * Get the degree of this spline.
   *
   * @return the degree of the spline
   */
  public int getDegree() {
    return splineDegree;
  }

  /**
   * Get the precision of this spline.
   *
   * @return the precision of the spline
   */
  public int getPrecision() {
    return splinePrecision;
  }
}
