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
import java.util.*;

public abstract class DrawObject implements Shape
{
  /**
   * The number of points per inch as determined by the layout program (dot).
   */
  public final static double pointsPerInch = 72.0;
  public final static double fudgeFactor = 1.42;


  public final static int bezierDegree =  3;

  /*
   * Bezier matrix
   */
  private static final double[][] bezierMatrix = {
    {-1,  3, -3,  1},
    { 3, -6,  3,  0},
    {-3,  3,  0,  0},
    { 1,  0,  0,  0}
  };

  /*
   * Bezier multiplier for spline drawing
   */
  private static final double bezierMultiplier = 1.0 / 6.0;

  /*
   * Bezier precision for spline drawing
   */
  protected static final int bezierPrecision = 15;

  /*
   * Step size for spline drawing
   */
  private static final double stepSize = 1.0 / (double)bezierPrecision;

  /*
   * Bezier precision matrix
   */
  private static final double[][] bezierPrecisionMatrix = {
    { 0, 0, 0, 1 },
    { stepSize * stepSize * stepSize, stepSize * stepSize, stepSize, 0 },
    { 6.0 * stepSize * stepSize * stepSize, 2.0 * stepSize * stepSize, 0, 0 },
    { 6.0 * stepSize * stepSize * stepSize, 0, 0, 0 }
  };

  // associated graph element
  protected AppObject appObject = null;

  protected GraphicContext gc = new GraphicContext();
  protected GraphicContext deleteGc = null;

  protected TextLabel textLabel = null;

  private boolean redrawFlag = true;

  private boolean initialized = false;

  /**
   * This flag indicates if there is a need to call setBounds() 
   */
  private boolean boundsFlag = true;

  protected MyRectangle BoundingBox = new MyRectangle();
  
  public void setBoundsFlag(boolean mode) {
    boundsFlag = mode;
    if(mode == true) {
      getElement().getGraph().setBoundsFlag(mode);
    }
  }
  
  public boolean getBoundsFlag() {
    return boundsFlag;
  }

  public MyRectangle getBounds() {
    if(getBoundsFlag()) {
      setBounds();
    }
    return BoundingBox.getBounds();
  }

  public abstract MyRectangle setBounds();

  public boolean PointInObject(MyPoint p) {
    if(!getBounds().contains(p)) return false;
    return(PointInSpecificObject(p));
  }

  public abstract boolean PointInSpecificObject(MyPoint p);

  /**
   * Of course, this abstract class cannot be directly instantiated.
   */
  public DrawObject() {
    initialized = false;
  }

  public void initialize(AppObject obj) {
    if(initialized) return;
    appObject = obj;
    if(appObject.getSetDrawDefaults()) {
      setDefaultDrawAttributes();
    }
    initializeFromAttributes();
    initialized = true;
  }
  
  /**
   * Called during construction to initialize values based on
   * attributes of element.
   */
  private void initializeFromAttributes() {
    Enumeration attrs = getElement().getAttributePairs();

    Attribute attr = null;

    while(attrs.hasMoreElements()) {
      attr = (Attribute)attrs.nextElement();
      handleAttribute(attr);
    }
  }

  /**
   * Adjust values based on specified attribute and value.
   * Possibly set redraw flag.
   *
   * @param attr the attribute to be handled.
   * @return true if attribute was handled.
   */
  public boolean handleAttribute(Attribute attr) {
    if(attr == null) {
      return true;
    }
    
    String key = attr.getName();
    
    if(key.equals("color")) {
      if(emptyMeansRemove(attr)) return true;
      Color oldColor = gc.setForeground(attr.getValue());
      if(!oldColor.equals(gc.getForeground())) {
	setRedrawFlag(true);
      }
      return true;
    } else if(key.equals("fontcolor")) {
      if(emptyMeansRemove(attr)) return true;
      Color oldColor = gc.setFontcolor(attr.getValue());
      if(!oldColor.equals(gc.getFontcolor())) {
	setRedrawFlag(true);
      }
      return true;
    } else if(key.equals("fontname")) {
      if(emptyMeansRemove(attr)) return true;
      String oldName = gc.setFontname(attr.getValue());
      if(!oldName.equals(gc.getFontname())) {
	setRedrawFlag(true);
      }
      return true;
    } else if(key.equals("fontsize")) {
      if(emptyMeansRemove(attr)) return true;
      int oldSize = gc.setFontsize(attr.getValue());
      if(oldSize != gc.getFontsize()) {
	setRedrawFlag(true);
      }
      return true;
    } else if(key.equals("fontstyle")) {
      if(emptyMeansRemove(attr)) return true;
      int oldStyle = gc.setFontstyle(GraphicContext.xlateFontStyle(attr.getValue()));
      if(oldStyle != gc.getFontstyle()) {
	setRedrawFlag(true);
      }
      return true;
    }
    return false;
  }

  public boolean emptyMeansRemove(Attribute attr) {
    if(attr.getValue() != null && attr.getValue().length() > 0) {
      return false;
    }
    Attribute dfltAttr = getElement().removeAttribute(attr.getName());
    return true;
  }

  public void setRedrawFlag(boolean mode) {
    redrawFlag = mode;
  }
  
  public boolean getRedrawFlag() {
    return redrawFlag;
  }

  /**
   * Converts string of comma separated integers to a Point.
   *
   * @param tuple a string of two comma separated integers.
   * @return the supplied string as a Point.
   */
  protected MyPoint pointForTuple(String tuple) throws IllegalArgumentException, NumberFormatException {
    if(tuple == null) return null;
    
    int comma = tuple.lastIndexOf(',');
    if(comma < 0) {
      throw new IllegalArgumentException("tuple \"" + tuple + "\" should have form \"x,y\" (has layout been performed?)");
    }
    String x = tuple.substring(0,comma).trim();
    String y = tuple.substring(comma+1).trim();
    // flip y-axis
    return(new MyPoint(
		     Integer.valueOf(x).intValue(),
		     -Integer.valueOf(y).intValue()));
  }

  /**
   * Converts pair of floating point strings to a Dimension.
   * In addition, the units are converted from inches to points.
   *
   * @param width the width string.
   * @param height the height string.
   * @return the supplied width and height strings as a Dimension.
   * @see DrawObject#pointsPerInch
   */
  protected MyDimension dimensionForWidthHeight(String w, String h) {
    double width  = Double.valueOf(w).doubleValue();
    double height = Double.valueOf(h).doubleValue();
    int sizeW = (int)(width  * pointsPerInch);
    int sizeH = (int)(height * pointsPerInch);
    return new MyDimension(sizeW,sizeH);
  }

  /**
   * TODO - add scaling
   */
  public MyPoint getDrawPoint(MyPoint input) {
    if(input == null) return null;
    MyPoint output = new MyPoint(input.x, input.y);
    return output;
  }

  /**
   * Object specific draw routine.
   *
   * @param gc the graphic context to use when drawing, if this value
               is null, then the objects context is used.
   * @param pane limit drawing to the specified pane, if this value
   *               is null, then draw on all panes for this graph.
   */
  public abstract void draw(GraphicContext gc, DrawPane pane, boolean paintNow);

  /**
   * A convenience method that is equivalent to draw(null,null,false).
   */
  public void draw() {
    draw(null,null,false);
  }

  public GraphicContext getGC() {
    return gc;
  }
  
  public GraphicContext getParentGC() {
    DotSubgraph subg = getElement().getSubgraph();
    if(subg == null) {
      // root subgraph, so return graph GC
      return getElement().getGraph().getGC();
    }
    return subg.getDrawObject().getGC();
  }

  public double rotateX(double x, double y, double cosine, double sine) {
    return(x * cosine + y * sine);
  }

  public double rotateY(double x, double y, double cosine, double sine) {
    return(y * cosine - x * sine);
  }

  public Vector parseStyle(String style) {
    Vector output = new Vector(1,5);
    if(style == null) return output;
    if(style.length() == 0) {
      output.addElement("");
      return output;
    }
    int pos = 0, offset = 0;
    char[] array = style.toCharArray();
    while(pos < array.length) {
      while(pos < array.length && (Character.isSpace(array[pos]) || array[pos] == ',')) pos++;
      if(pos == array.length) {
	output.addElement("");
	return output;
      }
      offset = pos;
      if(array[pos] == '(' || array[pos] == ')') {
	pos++;
      } else {
	while(pos < array.length && array[pos] != ',' && array[pos] != '(' && array[pos] != ')') pos++;
      }
      output.addElement(new String(array,offset,pos - offset));
    }
    return output;
  }

  /*
   * This function performs the meat of the calculations for the
   * curve plotting. The first matrix is the curve matrix (either the Bezier
   * or precision matrix) with size 4x4, and the second matrix is the
   * geometry matrix (defined by the control points) with size 4x2.
   * To save memory allocation, an answer matrix (4x2) is also supplied.
   */
  private void bezierMultiply(double[][] curve, double[][] geometry, double[][] answer) {

    // Perform the matrix math
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 2; j++) {
	answer[i][j] = 0;
        for (int k = 0; k < 4; k++) {
          answer[i][j] = answer[i][j] + (curve[i][k] * geometry[k][j]);
	}
      }
    }
  }

  // from Glassner's Graphics Gems
  private final static int W_DEGREE =  5;

  protected MyPoint evaluateBezier(Vector pts, int degree, double parameter, FPoint[] left, FPoint[] right) {
    FPoint[][] eb = new FPoint[W_DEGREE+1][W_DEGREE+1];

    // copy control points
    for(int i = 0; i <= degree; i++) {
      MyPoint fp = (MyPoint)pts.elementAt(i);
      eb[0][i] = new FPoint((double)fp.x,(double)fp.y);
    }

    // triangle computation
    for(int i = 1; i <=degree; i++) {
      for(int j = 0; j <= degree - i; j++) {
	eb[i][j] = new FPoint(((1.0-parameter)*eb[i-1][j].x + parameter * eb[i-1][j+1].x),((1.0-parameter)*eb[i-1][j].y + parameter * eb[i-1][j+1].y));
      }
    }

    if(left != null) {
      for(int i = 0; i <= degree; i++) {
	left[i] = eb[i][0];
      }
    }
    if(right != null) {
      for(int i = 0; i <= degree; i++) {
	right[i] = eb[degree-i][i];
      }
    }

    return new MyPoint((int)Math.round(eb[degree][0].x),(int)Math.round(eb[degree][0].y));
  }

  /**
   * Compute a spline using a bezier curve
   *
   */
  protected Vector computeSpline(Vector linePoints) throws IllegalArgumentException {

    int npts = linePoints.size();
    double[][] geometry = new double[bezierDegree+1][2];
    double[][] cg = new double[bezierDegree+1][2];
    double[][] plot = new double[bezierDegree+1][2];

    if(npts < bezierDegree+1) {
      throw new IllegalArgumentException("must have at least " + (bezierDegree+1) + " line points to generate a spline (has layout been performed?)");
    }
    Vector output = new Vector((npts - bezierDegree) * bezierPrecision + 1);

    MyPoint aPoint, pt1, pt2;
    FPoint fpt1 = new FPoint(0,0);
    FPoint fpt2 = new FPoint(0,0);
    int count = 0;
    for (int i = 0; i < npts - bezierDegree;) {
      // Four control points are needed to create a curve.
      // If all the control points are used, the last series of four
      // points begins with point npts-4.
      for (int j = 0; j < 4; j++) {
	aPoint = (MyPoint)linePoints.elementAt(i + j);
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
      fpt1.move(plot[0][0],plot[0][1]);
      fpt2.move(fpt1);

      output.addElement(new MyPoint((int) fpt2.x,(int) fpt2.y));

      // Plot the curve using the forward difference method
      for (int j = 0; j < bezierPrecision; j++) {
        fpt2.translate(plot[1][0],plot[1][1]);
        plot[1][0] += plot[2][0];
        plot[1][1] += plot[2][1];
        plot[2][0] += plot[3][0];
        plot[2][1] += plot[3][1];
	output.addElement(new MyPoint((int) fpt2.x,(int) fpt2.y));
        count++;
        fpt1.move(fpt2);
      }
    }

    return output;
  }

  protected Vector computeSpline(Vector linePoints,int start, int count) {
    Vector subvector = new Vector(count);
    if(count <= bezierDegree || start >= linePoints.size()) return subvector;
    int slot = 0;
    for(int i = 0; i<count; i++) {
      slot = start + i;
      if(slot >= linePoints.size()) {
	slot = linePoints.size();
      }
      subvector.addElement(linePoints.elementAt(slot));
    }
    return computeSpline(subvector);
  }

  public DotElement getElement() {
    if(appObject == null) return null;
    return appObject.getElement();
  }

  protected void setDefaultDrawAttributes() {
    if(appObject == null) return;

    Attribute oldAttr = null;

    if((oldAttr = getElement().getAttribute("color")) == null) {
      oldAttr = getElement().setAttribute(new Attribute("color","black"));
    }

    return;
  }

  public void free() {
    setRedrawFlag(true);
    setBoundsFlag(true);
    initialized = false;
    appObject = null;
    textLabel = null;
  }

  public abstract void highlightSelect();

  public abstract void highlightDelete();

  public void highlightOff() {
    draw(null,null,true);
  }
}
