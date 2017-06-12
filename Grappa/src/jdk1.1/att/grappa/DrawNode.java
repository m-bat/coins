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

/**
 * The base class for node drawing.
 * Simple extensions of this class permit the drawing of a wide variety
 * of shapes.  Consequently, many of the variables are made directly
 * available to subclasses of this one.
 *
 * @version 1.1, 30 Sep 1999; Copyright 1996 - 1999 by AT&T Corp.
 * @author  <a href="mailto:john@research.att.com">John Mocenigo</a>, <a href="http://www.research.att.com">Research @ AT&T Labs</a>
 */
public class DrawNode extends DrawObject
{
  /**
   * Default gap in pixels between peripheries
   */
  protected static int PERIPHERY_GAP = 4;

  /**
   * Instance gap in pixels between peripheries
   */
  protected int periphery_gap = PERIPHERY_GAP;

  /**
   * Indicator for an arc shape
   */
  protected final static int ARC        = 1; // not much used
  /**
   * Indicator for an oval shape
   */
  protected final static int OVAL       = 2;
  /**
   * Indicator for a polygon shape
   */
  protected final static int POLYGON    = 3;
  /**
   * Indicator for a rectangle shape
   */
  protected final static int RECT       = 4;
  /**
   * Indicator for a table (record) shape
   */
  protected final static int TABLE      = 5;
  /**
   * Indicator for a text (plain text) shape
   */
  protected final static int TEXT       = 6;

  /**
   * Indicator for drawing additional lines within the shape (e.g., Msquare)
   */
  protected final static int DIAGONALS  = 1;
  /**
   * Indicator for rounding the corner of a shape
   */
  protected final static int ROUNDED    = 2;
  /**
   * Indicator that the shape has auxiliary labels
   */
  protected final static int AUXLABELS  = 4;
  /**
   * Indicator that the shape should be fill-shaded
   */
  protected final static int FILLED     = 8;
  /**
   * Indicator that the shape should be of fixed size (when scaled)
   */
  protected final static int FIXED_SIZE = 16;

  /*
   * There are too obscure to make protected
   */
  private final static double RBCONST = 12.0;
  private final static double RBCURVE = 0.5;

  /**
   * Defines the drawing style of the node; its
   * value is set when the class object is created
   */
  protected int shape     = DrawNode.POLYGON; // as a default

  // setBounds may alter drawShape to use a polygon to handle skewing, etc.
  /**
   * The actual shape used for drawing.
   * This value may differ from <code>shape</code> since
   * <code>setBounds()</code> may alter the value of
   * <code>drawShape</code> from that
   * of <code>shape</code> to use
   * a polygon to handle skewing a rectangle, for example.
   */
  protected int drawShape = shape;
  
  /**
   * The size of the node (resulting from interpreting the width and
   * height attributes).
   */
  protected Dimension size = new Dimension(0,0);
  /**
   * The position of the node (resulting from interpreting the pos attributes).
   */
  protected Point position = new Point(0,0);

  /**
   * Set to true when width should equal height (as with a Circle or Square).
   */
  protected boolean regular    = false;

  /**
   * The number of peripheries
   */
  protected int peripheries    = 1;

  /**
   * The default orientation of this node
   */
  protected double orientation     = 0;
  /**
   * The orientation specified by the orientation attribute of this node
   */
  protected double attrOrientation = 0;
  /**
   * The orientation used for drawing.
   * It is the sum of the orientation and attrOrientation variables.
   */
  protected double drawOrientation   = 0;

  /**
   * The number of sides for this node.
   * When sides is less than three, an oval shape is indicated; otherwise
   * it specifies the number of sides of a polygon.  If the bounding box
   * is square, then the sides are sure to be of equal length.
   */
  protected int sides          = 0;
  /**
   * The amount of distortion to introduce into the drawn shape
   */
  protected double distortion  = 0;
  /**
   * The amount of skewness to introduce into the drawn shape
   */
  protected double skew        = 0;

  /**
   * The default style of this node
   */
  protected int style          = 0;
  /**
   * The style specified by the style attribute of this node
   */
  protected int attrStyle      = 0;
  /**
   * The style used for drawing.
   * It is the bitwise OR-ing of the style and attrStyle variables.
   */
  protected int drawStyle        = 0;

  private DoublePoint polyScale = new DoublePoint(1.0,1.0);

  /**
   * A vector of IntPairs for drawing (a Vector is used to handle the
   * case of peripheries > 1)
   */
  protected Vector drawPoints = new Vector();

  /**
   * This constructor creates an uninitialized DrawNode object.
   * Upon creation, a default
   * set of attributes for observing are specified (in addition to those
   * specified when its <code>super()</code> constructor is called.
   *
   * @see java.util.Observer
   */
  public DrawNode() {
    //super();

    attrOfInterest("distortion");
    attrOfInterest("height");
    attrOfInterest("orientation");
    attrOfInterest("peripheries");
    attrOfInterest("pos");
    attrOfInterest("sides");
    attrOfInterest("skew");
    attrOfInterest("style");
    attrOfInterest("width");
  }

  /**
   * This method to be called when the bounding box needs to be calculated.
   *
   * @return the bounding box of this object in graph co-ordinates
   */
  public Rectangle setBounds() {
    drawOrientation = attrOrientation + orientation;
    drawStyle = attrStyle | style;

    Rectangle oldBox = BoundingBox;
    if(!getBoundsFlag()) return oldBox;

    getGC().setFillMode((drawStyle&DrawNode.FILLED) != 0);

  setBBox: {
      int x = position.x - (int)Math.ceil((double)size.width/2.0);
      /*
       * Assume center point (position) is already adjusted for flipped y-axis
       * and just subtract height adjustment (i.e., do y - dy rather
       * than -(y + dy)).
       */
      int y = position.y - (int)Math.ceil((double)size.height/2.0);
      int width = ((size.width % 2) == 0) ? size.width : size.width + 1;
      int height = ((size.height % 2) == 0) ? size.height : size.height + 1;
      BoundingBox = new Rectangle(x,y,width,height);
    }
    setBoundsFlag(false);
    drawShape = shape;
    switch(drawShape) {
    case DrawNode.ARC:
      if(skew != 0 || distortion != 0 || drawOrientation != 0) {
	// sorry, cannot skew, distort or reorient this shapes
	skew = 0;
	distortion = 0;
	drawOrientation = 0;
      }
      setBox();
      break;
    case DrawNode.TABLE:
      if(skew != 0 || distortion != 0 || drawOrientation != 0) {
	// sorry, cannot skew, distort or reorient this shapes
	skew = 0;
	distortion = 0;
	drawOrientation = 0;
      }
      if(peripheries != 1) {
	// sorry, cannot mess with peripheries either!
	peripheries = 1;
      }
      if((drawStyle&(DrawNode.ROUNDED|DrawNode.DIAGONALS)) != 0) {
	drawShape = DrawNode.POLYGON;
      } else {
	drawShape = DrawNode.RECT;
      }
      setTable();
      break;
    case DrawNode.RECT:
    case DrawNode.OVAL:
      if(skew != 0 || distortion != 0 || Math.round(drawOrientation)%90 != 0 || (drawStyle&(DrawNode.ROUNDED|DrawNode.DIAGONALS)) != 0) {
	drawShape = DrawNode.POLYGON;
	setPolygon();
      } else {
	setBox();
      }
      break;
    case DrawNode.TEXT:
      break;
    case DrawNode.POLYGON:
      if(sides < 3 && skew == 0 && distortion == 0) {
	drawShape = DrawNode.OVAL;
	setBox();
      } else if(sides == 4 && skew == 0 && distortion == 0 && Math.round(drawOrientation)%90 == 0) {
	if((drawStyle & (DrawNode.ROUNDED|DrawNode.DIAGONALS)) == 0) {
	  drawShape = DrawNode.RECT;
	}
	setBox();
      } else {
	setPolygon();
      }
      break;
    default:
      throw new IllegalStateException("node drawing shape is not recognized (value is " + drawShape + ")");
    }
    if(getTextLabel() != null) {
      BoundingBox.add(getTextLabel().getBounds());
    }
    needSetup();
    return oldBox;
  }

  /**
   * This method sets up the information needed to draw the node.
   * It is called from setBounds (where drawOrientation is set).
   * After obtaining the bounding box, it sets up the outline and
   * additional periphery information for shapes other than those that
   * require the <code>Graphics</code> method <code>drawPolygon()</code>.
   *
   * @see #setPolygon()
   */
  protected void setBox() {
    if(peripheries < 1) return;
    // account for drawing being 1 pixel larger than specified.
    Rectangle bbox = getBounds();
    int width = bbox.width - 1;
    int height = bbox.height - 1;

    drawPoints.removeAllElements();
    drawPoints.ensureCapacity(peripheries);
    IntPairs current = new IntPairs(2);
    drawPoints.addElement(current);
    current.xArray[0] = bbox.x;
    current.yArray[0] = bbox.y;
    current.xArray[1] = width;
    current.yArray[1] = height;
    IntPairs previous = current;
    for(int i = 1; i < peripheries; i++) {
      current = new IntPairs(2);
      drawPoints.addElement(current);
      current.xArray[0] = previous.xArray[0] + DrawNode.periphery_gap;
      current.yArray[0] = previous.yArray[0] + DrawNode.periphery_gap;
      current.xArray[1] = previous.xArray[1] - 2 * DrawNode.periphery_gap;
      current.yArray[1] = previous.yArray[1] - 2 * DrawNode.periphery_gap;
      previous = current;
    }
  }

  /**
   * This method sets up the information needed to draw the node.
   * It is called from setBounds (where drawOrientation is set).
   * After obtaining the bounding box, it sets up the outline and
   * additional periphery information for shapes that
   * require the <code>Graphics</code> method <code>drawPolygon()</code>.
   *
   * @see #setPolygon()
   */
  protected void setPolygon() {
    if(peripheries < 1) return;

    int width = getBounds().width;
    int height = getBounds().height;

    int drawSides = sides;
    if(shape == DrawNode.OVAL || sides < 3) {
      drawSides = 120;
    }

    double sectorAngle = 2.0 * Math.PI / (double)drawSides;
    double sideLength  = Math.sin(sectorAngle/2.0);
    double skewDist, gDist, gSkew;
    if(skew == 0 && distortion == 0) {
      skewDist = 1;
      gDist = 0;
      gSkew = 0;
    } else {
      skewDist = Math.abs(distortion) + Math.abs(skew);
      skewDist = Math.sqrt(skewDist * skewDist + 1.0);
      gDist = distortion * Math.sqrt(2.0) / Math.cos(sectorAngle/2.0);
      gSkew = skew / 2.0;
    }
    double angle = (sectorAngle - Math.PI)/2.0;
    //double angle = sectorAngle/2.0;
    double sinX = Math.sin(angle);
    double cosX = Math.cos(angle);
    DoublePoint Rpt = new DoublePoint(0.5 * cosX, 0.5 * sinX);
    angle += (Math.PI - sectorAngle)/2.0;
    //angle = Math.PI/2.0;
    DoublePoint Ppt = new DoublePoint();
    double dtmp, alpha;

    Vector rawVertices = new Vector(drawSides);

    DoublePoint Bpt = new DoublePoint(0,0);
    for(int i = 0; i < drawSides; i++) {
      // next regular vertex
      angle += sectorAngle;
      sinX = Math.sin(angle);
      cosX = Math.cos(angle);
      Rpt.translate(sideLength*cosX,sideLength*sinX);
      // distort and skew
      Ppt.setLocation(Rpt.x * (skewDist + Rpt.y * gDist) + Rpt.y * gSkew, Rpt.y);
      // orient Ppt
      if(drawOrientation != 0) {
	alpha = ( Math.PI * drawOrientation / 180.0 ) + Math.atan2(Ppt.y,Ppt.x); 
	sinX = Math.sin(alpha);
	cosX = Math.cos(alpha);
	dtmp = Math.sqrt(Ppt.x * Ppt.x + Ppt.y * Ppt.y);
	Ppt.setLocation(dtmp * cosX, dtmp * sinX);
      }
      // scale
      Ppt.scale((double)width,-(double)height);
      // store result
      rawVertices.addElement(new DoublePoint(Ppt));
      if(Bpt.x < Math.abs(Ppt.x)) Bpt.x = Math.abs(Ppt.x);
      if(Bpt.y < Math.abs(Ppt.y)) Bpt.y = Math.abs(Ppt.y);
    }

    Bpt.scale(2.0,2.0);
    Bpt.x = (double)width / Bpt.x;
    Bpt.y = (double)height / Bpt.y;

    drawPoints.removeAllElements();
    drawPoints.ensureCapacity(peripheries);

    BezierSpline spline = new BezierSpline();
    int dS = drawSides;
    if((drawStyle&DrawNode.DIAGONALS) != 0) {
      dS = drawSides * 4;
    } else if((drawStyle&DrawNode.ROUNDED) != 0) {
      dS = (spline.getPrecision() + 2) * drawSides;
    }
    for(int j = 0; j < peripheries; j++) {
      drawPoints.addElement(new IntPairs(dS));
    }
    IntPairs current = (IntPairs)drawPoints.elementAt(0);
	
    Point Ipt = null, Jpt = null, Kpt = null;
    DoublePoint Tpt = null;
    Vector tmpVertices = null;
    Vector inputPoints = null;

    if((drawStyle&(DrawNode.ROUNDED|DrawNode.DIAGONALS)) != 0) {
      inputPoints = new Vector(4);
      for(int i = 0; i < 4; i++) inputPoints.addElement(new Point());

      tmpVertices = new Vector(2*(drawSides+1),2*(drawSides+1));
      double rbconst = DrawNode.RBCONST, rbcurve = DrawNode.RBCURVE;
      
      DoublePoint Pt0;
      DoublePoint Pt1;
      double dist = 0;
      double tmp = 0;
      Pt0 = (DoublePoint)rawVertices.elementAt(0);
      Pt0.scale(Bpt.x,Bpt.y);
      for(int i = 0; i < drawSides; i++) {
	// already scaled
	Pt0 = (DoublePoint)rawVertices.elementAt(i);
	if(i < drawSides - 1) {
	  Pt1 = (DoublePoint)rawVertices.elementAt(i+1);
	  Pt1.scale(Bpt.x,Bpt.y);
	} else {
	  Pt1 = (DoublePoint)rawVertices.elementAt(0);
	}
	dist = Math.sqrt((Pt1.x-Pt0.x)*(Pt1.x-Pt0.x) + (Pt1.y-Pt0.y)*(Pt1.y-Pt0.y));
	tmp = rbconst / dist;
	if((drawStyle&DrawNode.DIAGONALS) != 0) {
	  tmpVertices.addElement(new DoublePoint(Pt0));
	} else {
	  tmpVertices.addElement(DoublePoint.interpolate(rbcurve*tmp,Pt0,Pt1));
	}
	tmpVertices.addElement(DoublePoint.interpolate(tmp,Pt0,Pt1));
	tmpVertices.addElement(DoublePoint.interpolate(1.0-tmp,Pt0,Pt1));
	if((drawStyle&DrawNode.ROUNDED) != 0) {
	  tmpVertices.addElement(DoublePoint.interpolate(1.0-rbcurve*tmp,Pt0,Pt1));
	}
      }
      tmpVertices.addElement(new DoublePoint((DoublePoint)tmpVertices.elementAt(0)));
      tmpVertices.addElement(new DoublePoint((DoublePoint)tmpVertices.elementAt(1)));
      tmpVertices.addElement(new DoublePoint((DoublePoint)tmpVertices.elementAt(2)));

      for(int i = 0; i < drawSides; i++) {
	if((drawStyle&DrawNode.DIAGONALS) != 0) {
	  Tpt = (DoublePoint)tmpVertices.elementAt(3*i);
	  current.addPair(position.x + (int)Math.round(Tpt.x),position.y - (int)Math.round(Tpt.y));
	  Tpt = (DoublePoint)tmpVertices.elementAt(3*i+3);
	  current.addPair(position.x + (int)Math.round(Tpt.x),position.y - (int)Math.round(Tpt.y));
	  Tpt = (DoublePoint)tmpVertices.elementAt(3*i+2);
	  current.addPair(position.x + (int)Math.round(Tpt.x),position.y - (int)Math.round(Tpt.y));
	  Tpt = (DoublePoint)tmpVertices.elementAt(3*i+4);
	  current.addPair(position.x + (int)Math.round(Tpt.x),position.y - (int)Math.round(Tpt.y));
	} else {
	  Tpt = (DoublePoint)tmpVertices.elementAt(4*i+1);
	  current.addPair(position.x + (int)Math.round(Tpt.x),position.y - (int)Math.round(Tpt.y));
	  Tpt = (DoublePoint)tmpVertices.elementAt(4*i+2);
	  current.addPair(position.x + (int)Math.round(Tpt.x),position.y - (int)Math.round(Tpt.y));
	  for(int j = 0; j<4; j++) {
	    Pt0 = (DoublePoint)tmpVertices.elementAt(4*i+2+j);
	    Ipt = (Point)inputPoints.elementAt(j);
	    Ipt.x = (int)Math.round(Pt0.x);
	    Ipt.y = (int)Math.round(Pt0.y);
	  }
	  spline.setSpline(inputPoints);
	  // start at 1?
	  for(int j = 1; j<spline.size(); j++) {
	    Ipt = spline.getPointAt(j);
	    current.addPair(position.x + Ipt.x,position.y - Ipt.y);
	  }
	}
      }
    } else {
      for(int i = 0; i < drawSides; i++) {
	Tpt = (DoublePoint)rawVertices.elementAt(i);
	Tpt.scale(Bpt.x,Bpt.y);
	current.insertPairAt(position.x + (int)Math.round(Tpt.x),position.y - (int)Math.round(Tpt.y),i);
      }
    }

    if(peripheries > 1) {
      DoublePoint Spt = null;
      DoublePoint Upt = null, Vpt = null;
      DoublePoint Qpt = new DoublePoint();
      DoublePoint PriorVertex = null;
      double beta, gamma, delta;
      int start = 0;
      int end = drawSides;

      Qpt.setLocation((DoublePoint)rawVertices.elementAt(drawSides-1));
      Rpt.setLocation((DoublePoint)rawVertices.elementAt(0));
      beta = Math.atan2(Rpt.y-Qpt.y,Rpt.x-Qpt.x);

      if((drawStyle&(DrawNode.DIAGONALS|DrawNode.ROUNDED)) != 0) {
	Jpt = new Point();
	Kpt = new Point();
	Spt = new DoublePoint();
	// set-up to compute this vertex and next vertex
	Qpt.setLocation(Rpt);
	Rpt.setLocation((DoublePoint)rawVertices.elementAt(1));
	alpha = beta;
	beta = Math.atan2(Rpt.y-Qpt.y,Rpt.x-Qpt.x);
	gamma = (alpha + Math.PI - beta) / 2.0;
	delta = alpha - gamma;
	sinX = Math.sin(delta);
	cosX = Math.cos(delta);
	dtmp = DrawNode.periphery_gap / Math.sin(gamma);
	sinX *= dtmp;
	cosX *= dtmp;
	PriorVertex = new DoublePoint(cosX,sinX);

	start = 1;
	end = drawSides + 1;
      }

      for(int i = start; i < end; i++) {
	// for each vertex, find the bisector
	//Ppt.setLocation(Qpt);
	Qpt.setLocation(Rpt);
	Rpt.setLocation((DoublePoint)rawVertices.elementAt((i+1)%drawSides));
	alpha = beta;
	beta = Math.atan2(Rpt.y-Qpt.y,Rpt.x-Qpt.x);
	gamma = (alpha + Math.PI - beta) / 2.0;

	/*
	 * find the distance along the bisector to the
	 * intersection of the next periphery
	 */
	dtmp = DrawNode.periphery_gap / Math.sin(gamma);

	// convert this distance to x and y
	delta = alpha - gamma;
	sinX = Math.sin(delta);
	cosX = Math.cos(delta);
	sinX *= dtmp;
	cosX *= dtmp;

	if((drawStyle&(DrawNode.DIAGONALS|DrawNode.ROUNDED)) == 0) {
	  // save the vertices of the peripheries
	  for(int j = 1; j<peripheries; j++) {
	    current = (IntPairs)drawPoints.elementAt(j);
	    Qpt.translate(cosX,sinX);
	    current.insertPairAt(position.x + (int)Math.round(Qpt.x),position.y - (int)Math.round(Qpt.y),i);
	  }
	} else if((drawStyle&DrawNode.DIAGONALS)!= 0) {
	  int k = i - 1;
	  Tpt = (DoublePoint)tmpVertices.elementAt(3*k);
	  Ppt.setLocation((DoublePoint)tmpVertices.elementAt(3*k+3));
	  Upt = (DoublePoint)tmpVertices.elementAt(3*k+2);
	  Vpt = (DoublePoint)tmpVertices.elementAt(3*k+4);
	  for(int j = 1; j<peripheries; j++) {
	    current = (IntPairs)drawPoints.elementAt(j);
	    Tpt.translate(PriorVertex.x,PriorVertex.y);
	    current.insertPairAt(position.x + (int)Math.round(Tpt.x),position.y - (int)Math.round(Tpt.y),4*k);
	    Ppt.translate(cosX,sinX);
	    current.insertPairAt(position.x + (int)Math.round(Ppt.x),position.y - (int)Math.round(Ppt.y),4*k+1);
	    Upt.translate(cosX,sinX);
	    current.insertPairAt(position.x + (int)Math.round(Upt.x),position.y - (int)Math.round(Upt.y),4*k+2);
	    Vpt.translate(cosX,sinX);
	    current.insertPairAt(position.x + (int)Math.round(Vpt.x),position.y - (int)Math.round(Vpt.y),4*k+3);
	  }
	  PriorVertex.setLocation(cosX,sinX);
	} else {
	  int k = i - 1;
	  Ppt.setLocation((DoublePoint)tmpVertices.elementAt(4*k+1));
	  Ppt.translate(PriorVertex.x,PriorVertex.y);
	  Jpt.setLocation((int)Math.round(Ppt.x),(int)Math.round(Ppt.y));
	  Ppt.setLocation((DoublePoint)tmpVertices.elementAt(4*k+2));
	  Ppt.translate(cosX,sinX);
	  Kpt.setLocation((int)Math.round(Ppt.x),(int)Math.round(Ppt.y));

	  for(int j = 0; j<4; j++) {
	    Spt.setLocation((DoublePoint)tmpVertices.elementAt(4*k+2+j));
	    Spt.translate(cosX,sinX);
	    Ipt = (Point)inputPoints.elementAt(j);
	    Ipt.x = (int)Math.round(Spt.x);
	    Ipt.y = (int)Math.round(Spt.y);
	  }
	  spline.setSpline(inputPoints);
	  for(int j = 1; j<peripheries; j++) {
	    current = (IntPairs)drawPoints.elementAt(j);

	    current.addPair(position.x + Jpt.x,position.y - Jpt.y);
	    Jpt.translate((int)Math.round(PriorVertex.x),(int)Math.round(PriorVertex.y));
	    current.addPair(position.x + Kpt.x,position.y - Kpt.y);
	    Kpt.translate((int)Math.round(cosX),(int)Math.round(sinX));
	    for(k = 1; k<spline.size(); k++) {
	      Ipt = spline.getPointAt(k);
	      current.addPair(position.x + Ipt.x,position.y - Ipt.y);
	      Ipt.translate((int)Math.round(cosX),(int)Math.round(sinX));
	    }
	  }
	  PriorVertex.setLocation(cosX,sinX);
	}
      }
    }
  }

  // this method is overwritten in the Table class
  protected void setTable() {
    throw new IllegalStateException("the setTable() method should only be used by Table and Table-derived objects");
  }

  /**
   * @return the node position (center point).
   */
  public Point getPosition() {
    return position;
  }

  /**
   * @return the node size.
   */
  public Dimension getSize() {
    return size;
  }

  /**
   * Get the integer representation of the style as used for drawing for this node.
   *
   * @return the drawing style of this node as an integer
   * @see #DIAGONALS
   * @see #ROUNDED
   * @see #AUXLABELS
   * @see #FILLED
   */
  public int getDrawStyle() {
    return drawStyle;
  }

  /**
   * Get the angle in degrees representing the drawing orientation of this node.
   *
   * @return the drawing angle in degrees
   */
  public double getDrawOrientation() {
    return drawOrientation;
  }

  /**
   * Get the points needed for drawing this node.
   * The drawing points are contained in a vector of <code>IntPairs</code>,
   * each element of the vector is one of the peripheries of the node.
   * The first element in the vector is the outermost periphery.
   * The points represented by the elements in the <code>IntPairs</code> are
   * either the actual points needed to draw the shape, as in the case of
   * polygon shapes, or are the points that define the shapes bounding box,
   * which are sufficient for drawing rectangles, ovals and arcs.
   *
   * @return a vector of <code>IntPairs</code> objects
   */
  public Vector getDrawPoints() {
    return drawPoints;
  }

  /**
   * Get the integer representation of the shape as used for drawing for this node.
   *
   * @return the drawing shape of this node as an integer
   * @see #ARC
   * @see #OVAL
   * @see #POLYGON
   * @see #RECT
   * @see #TABLE
   * @see #TEXT
   */
  public int getDrawShape() {
    return drawShape;
  }

  /**
   * Get the integer representation of the shape as initially specified for this node.
   *
   * @return the shape of this node as an integer
   * @see #ARC
   * @see #OVAL
   * @see #POLYGON
   * @see #RECT
   * @see #TABLE
   * @see #TEXT
   */
  public int getShape() {
    return shape;
  }

  /**
   * Get the number of peripheries for this node.
   *
   * @return the number of peripheries
   */
  public int getPeripheries() {
    return peripheries;
  }

  /**
   * This method is called whenever an observed Attribute is changed.
   * It is required by the <code>Observer</code> interface.
   *
   * @param obs the observable object that has been updated
   * @param arg when not null, it indicates that <code>obs</code> need no longer be
   *            observed and in its place <code>arg</code> should be observed.
   */
  public void update(Observable obs, Object arg) {
    // begin boilerplate
    if(!(obs instanceof Attribute)) {
      throw new IllegalArgumentException("expected to be observing attributes only (obs)");
    }
    Attribute attr = (Attribute)obs;
    if(arg != null) {
      if(!(arg instanceof Attribute)) {
	throw new IllegalArgumentException("expected to be observing attributes only (arg)");
      }
      attr.deleteObserver(this);
      attr = (Attribute)arg;
      attr.addObserver(this);
      // in case we call: super.update(obs,arg)
      obs = attr;
      arg = null;
    }
    // end boilerplate

    Attribute attr2 = null;

    if(attr.getNameHash() == DrawObject.DISTORTION_HASH) {
      if(emptyMeansRemove(attr)) return;
      double oldDistortion = distortion;
      distortion = Double.valueOf(attr.getValue()).doubleValue();
      if(oldDistortion != distortion) {
	setRedrawFlag(true);
	setBoundsFlag(true);
      }
      return;
    } else if(attr.getNameHash() == DrawObject.HEIGHT_HASH) {
      if(emptyMeansRemove(attr)) return;
      attr2 = getElement().getAttribute("width");
      if(attr2 == null) {
	attr2 = attr;
      } else if(regular) {
	if(Double.valueOf(attr.getValue()).doubleValue() < Double.valueOf(attr2.getValue()).doubleValue()) {
	  attr2 = attr;
	} else {
	  attr = attr2;
	}
      }
      Dimension oldSize = size;
      size = dimensionForWidthHeight(attr2.getValue(),attr.getValue());
      if(!oldSize.equals(size)) {
	setRedrawFlag(true);
	setBoundsFlag(true);
      }
      return;
    } else if(attr.getNameHash() == DrawObject.LABEL_HASH) {
      TextLabel oldTl = getTextLabel();

      String label = (String)attr.getValue();
      if(label == null || label.length() == 0) {
	label = null;
      } else if(label.equals("\\N")) {
	label = getElement().getName();
      }
      if(oldTl == null || !oldTl.sameText(label)) {
	setTextLabel(new TextLabel(this,label,getGC(),position));
	setRedrawFlag(true);
	setBoundsFlag(true);
      }
      return;
    } else if(attr.getNameHash() == DrawObject.LP_HASH) {
      // to skip super class handling
      return;
    } else if(attr.getNameHash() == DrawObject.ORIENTATION_HASH) {
      if(emptyMeansRemove(attr)) return;
      double oldOrientation = attrOrientation;
      attrOrientation = Double.valueOf(attr.getValue()).doubleValue();
      if(oldOrientation != attrOrientation) {
	setRedrawFlag(true);
	setBoundsFlag(true);
      }
      return;
    } else if(attr.getNameHash() == DrawObject.PERIPHERIES_HASH) {
      if(emptyMeansRemove(attr)) return;
      int oldPeripheries = peripheries;
      peripheries = Integer.valueOf(attr.getValue()).intValue();
      if(oldPeripheries != peripheries) {
	setRedrawFlag(true);
	setBoundsFlag(true);
      }
      return;
    } else if(attr.getNameHash() == DrawObject.POS_HASH) {
      if(emptyMeansRemove(attr)) return;
      Point newPosition = pointForTuple(attr.getValue());
      if(!position.equals(newPosition)) {
	position.setLocation(newPosition.x,newPosition.y);
	if(getTextLabel() != null) getTextLabel().setPosition(position);
	setRedrawFlag(true);
	setBoundsFlag(true);
      }
      return;
    } else if(attr.getNameHash() == DrawObject.SIDES_HASH) {
      if(emptyMeansRemove(attr)) return;
      int oldSides = sides;
      sides = Integer.valueOf(attr.getValue()).intValue();
      if(oldSides != sides) {
	setRedrawFlag(true);
	setBoundsFlag(true);
      }
      return;
    } else if(attr.getNameHash() == DrawObject.SKEW_HASH) {
      if(emptyMeansRemove(attr)) return;
      double oldSkew = skew;
      skew = Double.valueOf(attr.getValue()).doubleValue();
      if(oldSkew != skew) {
	setRedrawFlag(true);
	setBoundsFlag(true);
      }
      return;
    } else if(attr.getNameHash() == DrawObject.STYLE_HASH) {
      int oldStyle = attrStyle;
      boolean inParens = false;
      attrStyle = 0;
      Vector styles = parseStyle(attr.getValue());
      String token = null;
      for(int i = 0; i < styles.size(); i++) {
	token = (String)styles.elementAt(i);
	if(token.equals("filled")) {
	  attrStyle |= DrawNode.FILLED;
	} else if(token.equals("rounded")) {
	  attrStyle |= DrawNode.ROUNDED;
	} else if(token.equals("diagonals")) {
	  attrStyle |= DrawNode.DIAGONALS;
	} else if(token.equals("fixedSize")) {
	  attrStyle |= DrawNode.FIXED_SIZE;
	}
      }
      if((style|attrStyle) != (style|oldStyle)) {
	setRedrawFlag(true);
	setBoundsFlag(true);
      }
      return;
    } else if(attr.getNameHash() == DrawObject.WIDTH_HASH) {
      if(emptyMeansRemove(attr)) return;
      attr2 = getElement().getAttribute("height");
      if(attr2 == null) {
	attr2 = attr;
      } else if(regular) {
	if(Double.valueOf(attr.getValue()).doubleValue() < Double.valueOf(attr2.getValue()).doubleValue()) {
	  attr2 = attr;
	} else {
	  attr = attr2;
	}
      }
      Dimension oldSize = size;
      size = dimensionForWidthHeight(attr.getValue(),attr2.getValue());
      if(!oldSize.equals(size)) {
	setRedrawFlag(true);
	setBoundsFlag(true);
      }
      return;
    }
    // if we got to here, then let the super class have a crack at it
    super.update(obs,arg);
  }

  /**
   * Creates the drawing peer specific for this object and the specified pane.
   *
   * @param pane the <code>DrawPane</code> upon which the object will be drawn.
   */
  public void createPeer(DrawPane pane) {
    if(pane == null) {
      throw new IllegalArgumentException("supplied DrawPane cannot be null");
    }
    // references to and from the Peer are set during its init, so
    // no need to capture the new peer in this object
    new DrawNodePeer(this, pane);
    return;
  }

  public boolean isFixedSize() {
    return((attrStyle&DrawNode.FIXED_SIZE) != 0);
  }

    public int getPeripheryGap() {
	return periphery_gap;
    }

    public int setPeripheryGap(int gap) {
	int old = periphery_gap;
	periphery_gap = gap;
	return old;
    }

    public static int setDefaultPeripheryGap(int gap) {
	int old = PERIPHERY_GAP;
	PERIPHERY_GAP = gap;
	return old;
    }
}
