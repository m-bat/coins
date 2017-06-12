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
 * This class provides the basis for the drawing of edges.
 * Extensions of this class and its subclasses allow the drawing of edges
 * to be customized.
 *
 * @version 1.1, 30 Sep 1999; Copyright 1996 - 1999 by AT&T Corp.
 * @author  <a href="mailto:john@research.att.com">John Mocenigo</a>, <a href="http://www.research.att.com">Research @ AT&T Labs</a>
 */
public class DrawEdge extends DrawObject
{
  int lineStyle      = Grappa.LINE_SOLID;
  int lineWidth      = Grappa.LINE_WIDTH;

  /**
   * Arrow head length
   */
  public final static int arrowLength     = 10;
  /**
   * Arrow head width
   */
  public final static int arrowWidth      = 5;

  int arrowType = -1;

  /**
   * Default edge representation class name.
   */
  public final static String defaultEdgeClassName = "Line";

  private Point ePoint = null;
  private Point sPoint = null;
  private LineVector linePoints = null;

  /**
   * The spline corresponding to the supplied control points (for splines).
   */
  BezierSpline spline = null;

  /**
   * Indicator for spline (bezier)
   */
  protected final static int SPLINE     = 1;
  /**
   * Indicator for line
   */
  protected final static int LINE       = 2;

  /**
   * Defines the drawing style of the node; its
   * value is set when the class object is created
   */
  protected int shape     = DrawEdge.SPLINE; // as a default

  /**
   * Total number of specified points.
   */
  protected int totalPoints = 0;

  /**
   * The actual shape used for drawing.
   * This value may differ from <code>shape</code> since
   * <code>setBounds()</code> may alter the value of
   * <code>drawShape</code> from that
   * of <code>shape</code> to use based on the number
   * of supplied points.
   */
  protected int drawShape = shape;

  private Polygon edgePolygon = null;
  private Polygon startArrow = null;
  private Polygon endArrow = null;

  /**
   * This constructor creates an uninitialized <code>DrawEdge</code> with a default
   * set of attributes that it will observe (in its role as an Observer)
   *
   * @see java.util.Observer
   */
  public DrawEdge() {
    //super();

    attrOfInterest("lp");
    attrOfInterest("pos");
    attrOfInterest("style");
  }

  /**
   * This method to be called when the bounding box needs to be calculated.
   *
   * @return a rectangle representing the bounding box of the object in its
   *         original co-ordinates
   */
  public Rectangle setBounds() {
    Rectangle oldBox = BoundingBox;
    if(!getBoundsFlag()) return oldBox;

    BoundingBox = null;

      if(linePoints == null) {
	linePoints = new LineVector();
      }
    
    drawShape = shape;
    if(linePoints.size() < 4 || (linePoints.size()-1)%3 != 0 || linePoints.isStraight()) {
      drawShape = DrawEdge.LINE;
    }

    totalPoints = 0;
    if(sPoint != null) totalPoints++;
    if(ePoint != null) totalPoints++;
    totalPoints += linePoints.size();


    if(totalPoints < 2) {
      sPoint = null;
      ePoint = null;
      linePoints.removeAllElements();
      if(getElement().getGraph().isDirected()) {
	sPoint = pointForTuple(((Edge)getElement()).getHead().getAttributeValue("pos"));
	linePoints.addPoint(pointForTuple(((Edge)getElement()).getTail().getAttributeValue("pos")));
      } else {
	linePoints.addPoint(pointForTuple(((Edge)getElement()).getHead().getAttributeValue("pos")));
	linePoints.addPoint(pointForTuple(((Edge)getElement()).getTail().getAttributeValue("pos")));
      }
    }

    switch(drawShape) {
    case DrawEdge.SPLINE:
      if(linePoints != null && linePoints.size() > 0) {
	if(spline == null) {
	  spline = new BezierSpline();
	}
	spline.setSpline((Vector)linePoints);
	edgePolygon = computePolygon(sPoint,ePoint,(LineVector)spline);
	BoundingBox = new Rectangle(edgePolygon.getBounds());
	if(startArrow != null) BoundingBox.add(startArrow.getBounds());
	if(endArrow != null) BoundingBox.add(endArrow.getBounds());
      }
      break;
    case DrawEdge.LINE:
      edgePolygon = computePolygon(sPoint,ePoint,linePoints);
      BoundingBox = new Rectangle(edgePolygon.getBounds());
      if(startArrow != null) BoundingBox.add(startArrow.getBounds());
      if(endArrow != null) BoundingBox.add(endArrow.getBounds());
      break;
    }
    if(getTextLabel() != null) {
      if(BoundingBox != null) {
	BoundingBox.add(getTextLabel().getBounds());
      } else {
	BoundingBox = new Rectangle(getTextLabel().getBounds());
      }
    }
    /*
     * Rectangle bbox = BoundingBox.getBounds();
     * int fuzz = (int)Math.round(DrawObject.fudgeFactor);
     * BoundingBox.setBounds(bbox.x - fuzz, bbox.y - fuzz, bbox.width + 2*fuzz, bbox.height + 2*fuzz);
    */
    setBoundsFlag(false);
    needSetup();
    return oldBox;
  }

  /**
   * Returns the polygon enclosing the edge.
   *
   * @return the edge polygon.
   */
  public Polygon getEdgePolygon() {
    setBounds();
    return edgePolygon;
  }

  /**
   * Gets the polygon representing the arrow at the head-end of the edge.
   *
   * @return the outline of the head-end arrow or null.
   */
  public Polygon getStartArrow() {
    setBounds();
    return startArrow;
  }

  /**
   * Gets the polygon representing the arrow at the tail-end of the edge.
   *
   * @return the outline of the tail-end arrow or null.
   */
  public Polygon getEndArrow() {
    setBounds();
    return endArrow;
  }

  /**
   * Checks if the current edge co-ordinates are different from those supplied.
   *
   * @param oldSPoint the head-end point to compare
   * @param oldEPoint the tail-end point to compare
   * @param oldPoints the control points to compare
   *
   * @return true if the points are different, false otherwise
   */
  public boolean pointsChanged(Point oldSPoint, Point oldEPoint, LineVector oldPoints) {
    if(sPoint != oldSPoint && (sPoint == null || !sPoint.equals(oldSPoint))) {
      return true;
    }
    if(ePoint != oldEPoint && (ePoint == null || !ePoint.equals(oldEPoint))) {
      return true;
    }
    if(linePoints == oldPoints) return false;
    if(linePoints == null) return true;
    return !linePoints.equals(oldPoints);
  }

  /**
   * Sets the points describing the edge.
   *
   * @param newSPoint the new head-end point
   * @param newEPoint the new tail-end point
   * @param newPoints the new control points
   */
  public void setPoints(Point newSPoint, Point newEPoint, LineVector newPoints) {
    Point oldSPoint = sPoint;
    Point oldEPoint = ePoint;
    LineVector oldPoints = linePoints;

    if (newEPoint != null) {
      ePoint = new Point(newEPoint.x,newEPoint.y);
    } else {
      ePoint = null;
    }
    if (newSPoint != null) {
      sPoint = new Point(newSPoint.x,newSPoint.y);
    } else {
      sPoint = null;
    }
    linePoints = new LineVector(newPoints.size(),9);
    Point tmpPoint;
    for(int i = 0; i < newPoints.size(); i++) {
      tmpPoint = newPoints.getPointAt(i);
      linePoints.addPoint(tmpPoint.x,tmpPoint.y);
    }
    if(pointsChanged(oldSPoint,oldEPoint,oldPoints)) {
      setRedrawFlag(true);
      setBoundsFlag(true);
      setBounds();
      getElement().getGraph().addToBBox(getBounds());
    }
  }

  /**
   * Constructs a position string representation of the edge points.
   *
   * @return a position string representation of the edge points suitable
   *         for the value of the "pos" attribute
   */
  public String positionString() {
    StringBuffer posString = new StringBuffer();
    if (sPoint != null) {
      posString.append("s,");
      posString.append(sPoint.x);
      posString.append(",");
      posString.append(sPoint.y);
    }
    if (ePoint != null) {
      if(posString.length() > 0) posString.append(" ");
      posString.append("e,");
      posString.append(ePoint.x);
      posString.append(",");
      posString.append(ePoint.y);
    }
    Point aPoint;
    for (int i = 0; i < linePoints.size(); i++) {
      aPoint = linePoints.getPointAt(i);
      if(posString.length() > 0) posString.append(" ");
      posString.append(aPoint.x);
      posString.append(",");
      posString.append(aPoint.y);
    }
    return posString.toString();
  }

  /**
   * Compute the polygon that encloses the edge.
   * The arrow heads, if any, are not included.
   *
   * @return a polygon representing the edge
   */
  Polygon computePolygon(Point sp, Point ep, LineVector pts) {
    Point pt1 = null, pt2 = null;
    Point spt1 = null, spt2 = null;

    int startOffset = 0;
    int endOffset = 0;

    startArrow = null;
    endArrow = null;
    
    if(sp != null) {
      startOffset = 1;
    }
    if(ep != null) {
      endOffset = 1;
    }

    int npoints = 2 * (pts.size() + startOffset + endOffset);
    int[] xpoints = new int[npoints];
    int[] ypoints = new int[npoints];

    Point np = null;
    if(sp != null) {
      try {
	np = pts.firstPoint();
      } catch(java.util.NoSuchElementException nse) {
	np = ep;
      }
      startArrow = computeArrow(sp,np,lineWidth,true);
      if(lineWidth == 1) {
	xpoints[0] = startArrow.xpoints[4];
	ypoints[0] = startArrow.ypoints[4];
      } else {
	xpoints[0] = startArrow.xpoints[0];
	ypoints[0] = startArrow.ypoints[0];
      }
      xpoints[npoints-1] = startArrow.xpoints[4];
      ypoints[npoints-1] = startArrow.ypoints[4];
    }

    if(ep != null) {
      try {
	np = pts.lastPoint();
      } catch(java.util.NoSuchElementException nse) {
	np = sp;
      }
      endArrow = computeArrow(ep,np,lineWidth,false);
      if(lineWidth == 1) {
	xpoints[startOffset+pts.size()] = endArrow.xpoints[4];
	ypoints[startOffset+pts.size()] = endArrow.ypoints[4];
      } else {
	xpoints[startOffset+pts.size()] = endArrow.xpoints[0];
	ypoints[startOffset+pts.size()] = endArrow.ypoints[0];
      }
      xpoints[startOffset+pts.size()+1] = endArrow.xpoints[4];
      ypoints[startOffset+pts.size()+1] = endArrow.ypoints[4];
    }

    Point leftPt = new Point();
    Point rightPt = new Point();
    if(pts.size() > 1) {
      double lastAngle = -361; 
      for(int i = 1; i < pts.size(); i++) {
	lastAngle = computePoints(pts.getPointAt(i-1),pts.getPointAt(i),lastAngle,lineWidth,leftPt,rightPt);
	xpoints[(i+startOffset)-1] = leftPt.x;
	ypoints[(i+startOffset)-1] = leftPt.y;
	xpoints[(npoints-startOffset)-i] = rightPt.x;
	ypoints[(npoints-startOffset)-i] = rightPt.y;
      }
      computePoints(pts.getPointAt(pts.size()-1),pts.getPointAt(pts.size()-2),-361.0,lineWidth,leftPt,rightPt);
      if(lineWidth == 1) {
	xpoints[startOffset+pts.size()-1] = leftPt.x;
	ypoints[startOffset+pts.size()-1] = leftPt.y;
      } else {
	xpoints[startOffset+pts.size()-1] = rightPt.x;
	ypoints[startOffset+pts.size()-1] = rightPt.y;
      }
      xpoints[startOffset+pts.size()+2*endOffset] = leftPt.x;
      ypoints[startOffset+pts.size()+2*endOffset] = leftPt.y;
    } else {
      Point ptA = null;
      Point ptB = null;
      if(pts.size() == 1) {
	if(sPoint != null) {
	  ptA = sPoint;
	  ptB = pts.lastPoint();
	} else {
	  ptA = ePoint;
	  ptB = pts.firstPoint();
	}
      } else {
	ptA = sPoint;
	ptB = ePoint;
      }
      computePoints(ptA,ptB,-361.0,lineWidth,leftPt,rightPt);
      xpoints[startOffset] = leftPt.x;
      ypoints[startOffset] = leftPt.y;
      xpoints[(npoints-startOffset)-1] = rightPt.x;
      ypoints[(npoints-startOffset)-1] = rightPt.y;
      if(lineWidth == 1) {
	xpoints[startOffset+pts.size()-1] = leftPt.x + (ptB.x-ptA.x);
	ypoints[startOffset+pts.size()-1] = leftPt.y + (ptB.y-ptA.y);
      } else {
	xpoints[startOffset+pts.size()-1] = rightPt.x + (ptB.x-ptA.x);
	ypoints[startOffset+pts.size()-1] = rightPt.y + (ptB.y-ptA.y);
      }
      xpoints[startOffset+pts.size()+2*endOffset] = leftPt.x + (ptB.x-ptA.x);
      ypoints[startOffset+pts.size()+2*endOffset] = leftPt.y + (ptB.y-ptA.y);
    }
    return new Polygon(xpoints,ypoints,npoints);
  }

  double computePoints(Point ptA, Point ptB, double lastAngle, int lineWidth, Point leftPt, Point rightPt) {
    double baseAngle = Math.atan2((double)(ptB.y - ptA.y), (double)(ptB.x-ptA.x));

    double theta = 0;
    if(lastAngle >= -360.0 && lastAngle <= 360.0) {
      theta = (baseAngle + lastAngle) / 2.0;
    } else {
      theta = baseAngle;
    }

    double widone  = (double)Math.round((double)lineWidth * 0.5 - 0.001);
    double widtwo;
    if(lineWidth%2 != 0) {
      widtwo = widone + 1.0;
      if(lineWidth == 1) widtwo+=2.0;
    } else {
      widtwo = widone;
    }

    leftPt.x = ptA.x + (int)Math.round(widone * Math.sin(theta));
    leftPt.y = ptA.y - (int)Math.round(widone * Math.cos(theta));
    rightPt.x = ptA.x - (int)Math.round(widtwo * Math.sin(theta));
    rightPt.y = ptA.y + (int)Math.round(widtwo * Math.cos(theta));

    return baseAngle;
  }

  Polygon computeArrow(Point tipPt, Point midBase, int lineWidth, boolean atStart) {
    int[] xpoints = new int[5];
    int[] ypoints = new int[5];
    
    double theta = Math.atan2((double)(midBase.y - tipPt.y), (double)(midBase.x-tipPt.x));

    double len = arrowLength;

    double wid  = (double)Math.round((double)(arrowWidth + 2 * lineWidth + 1) * 0.5);

    xpoints[3] = tipPt.x + (int)Math.round(len * Math.cos(theta) + wid * Math.sin(theta));
    ypoints[3] = tipPt.y + (int)Math.round(len * Math.sin(theta) - wid * Math.cos(theta));
    xpoints[2] = tipPt.x;
    ypoints[2] = tipPt.y;
    xpoints[1] = tipPt.x + (int)Math.round(len * Math.cos(theta) - wid * Math.sin(theta));
    ypoints[1] = tipPt.y + (int)Math.round(len * Math.sin(theta) + wid * Math.cos(theta));

    double widone  = (double)Math.round((double)lineWidth * 0.5 - 0.001);
    double widtwo;
    if(lineWidth%2 != 0) {
      widtwo = widone + 1.0;
      if(lineWidth == 1) widtwo+=2.0;
    } else {
      widtwo = widone;
    }

    xpoints[4] = tipPt.x + (int)Math.round(len * Math.cos(theta) + widone * Math.sin(theta));
    ypoints[4] = tipPt.y + (int)Math.round(len * Math.sin(theta) - widone * Math.cos(theta));
    xpoints[0] = tipPt.x + (int)Math.round(len * Math.cos(theta) - widtwo * Math.sin(theta));
    ypoints[0] = tipPt.y + (int)Math.round(len * Math.sin(theta) + widtwo * Math.cos(theta));

    return new Polygon(xpoints,ypoints,5);
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

    if(attr.getNameHash() == DrawObject.POS_HASH) {
      if(emptyMeansRemove(attr)) return;
      Point oldSPoint = sPoint;
      Point oldEPoint = ePoint;
      LineVector oldPoints = linePoints;

      String points = attr.getValue();
      Point newPoint = null;

      arrowType = Grappa.ARROW_NONE;
      sPoint = null;
      ePoint = null;
      linePoints = new LineVector(4,9);

      int space = 0;
      if(points.startsWith("s")) {
	arrowType |= Grappa.ARROW_FIRST;
	space = points.indexOf(' ', 2);
	if(space < 0) {
	  sPoint = pointForTuple(points.substring(2));
	  points = "";
	} else {
	  sPoint = pointForTuple(points.substring(2,space));
	  points = points.substring(space+1);
	}
      }
      if(points.startsWith("e")) {
	arrowType |= Grappa.ARROW_LAST;
	space = points.indexOf(' ', 2);
	if(space < 0) {
	  ePoint = pointForTuple(points.substring(2));
	  points = "";
	} else {
	  ePoint = pointForTuple(points.substring(2,space));
	  points = points.substring(space+1);
	}
      }
      if(points.startsWith("s")) {
	arrowType |= Grappa.ARROW_FIRST;
	space = points.indexOf(' ', 2);
	if(space < 0) {
	  sPoint = pointForTuple(points.substring(2));
	  points = "";
	} else {
	  sPoint = pointForTuple(points.substring(2,space));
	  points = points.substring(space+1);
	}
      }
      while (points.length() > 0) {
	space = points.indexOf(' ');
	if(space < 0) {
	  newPoint = pointForTuple(points);
	  points = "";
	} else {
	  newPoint = pointForTuple(points.substring(0,space));
	  points = points.substring(space+1);
	}
	linePoints.addPoint(newPoint);
      }
      if(pointsChanged(oldSPoint,oldEPoint,oldPoints)) {
	setRedrawFlag(true);
	setBoundsFlag(true);
	getElement().getGraph().addToBBox(getBounds());
      }
      return;
    } else if(attr.getNameHash() == DrawObject.STYLE_HASH) {
      if(emptyMeansRemove(attr)) return;
      int oldStyle = lineStyle;
      int oldWidth = lineWidth;
      boolean inParens = false;
      lineStyle = Grappa.LINE_SOLID;
      lineWidth = Grappa.LINE_WIDTH;
      Vector styles = parseStyle(attr.getValue());
      String token = null;
      for(int i = 0; i < styles.size(); i++) {
	token = (String)styles.elementAt(i);
	if(token.equals(Grappa.LINE_SOLID_STRING)) {
	  lineStyle = Grappa.LINE_SOLID;
	} else if(token.equals(Grappa.LINE_DASHED_STRING)) {
	  lineStyle = Grappa.LINE_DASHED;
	} else if(token.equals(Grappa.LINE_DOTTED_STRING)) {
	  lineStyle = Grappa.LINE_DOTTED;
	} else if(token.equals(Grappa.LINE_WIDTH_STRING)) {
	  token = (String)styles.elementAt(++i);
	  if(token.equals("(")) {
	    token = (String)styles.elementAt(++i);
	    try {
	      lineWidth = Integer.valueOf(token).intValue();
	      token = (String)styles.elementAt(++i);
	    } catch(Exception nfe) { // Number format or array bounds
	      getElement().getGraph().printError("style line width format error");
	      lineWidth = oldWidth;
	    }
	  }
	}
      }
      if(lineStyle != oldStyle || lineWidth != oldWidth) {
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
    // no need to capture the new peer
    new DrawEdgePeer(this, pane);
    return;
  }

  /**
   * Get the line width value of the edge.
   *
   * @return the line width
   */
  public int getLineWidth() {
    return lineWidth;
  }

  /**
   * Get the line style value of the edge.
   *
   * @return the style width
   */
  public int getLineStyle() {
    return lineStyle;
  }
}
