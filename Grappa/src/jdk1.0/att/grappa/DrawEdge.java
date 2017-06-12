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

public abstract class DrawEdge extends DrawObject
{
  protected static GraphicContext selectGc = null;
  protected int lineStyle = 0;
  /**
   * Arrow length info
   */
  public final static int arrowWidth      = 5;
  public final static int arrowLength     = 10;
  public final static int ARROW_LENGTH    = arrowLength - 1;
  public final static int ARROW_LENGTH_SQ = ARROW_LENGTH * ARROW_LENGTH;

  /**
   * Initial size of the list of edge points.
   *
   * @see Vector
   */
  public final static int initialLine = 4;
  /**
   * Size increment of the list of edge points.
   *
   * @see Vector
   */
  public final static int lineIncrement = 3;

  int arrowType = -1;

  /**
   * Default edge representation class name.
   */
  public final static String defaultEdgeClassName = "Line";

  private MyPoint ePoint = null;
  private MyPoint sPoint = null;
  private Vector linePoints = new Vector(initialLine,lineIncrement);

  protected Vector splinePoints = null;
  protected MyPolygon startArrow = null;
  protected MyPolygon endArrow = null;

  public MyRectangle setBounds() {
    MyRectangle oldBox = BoundingBox;
    if(!getBoundsFlag()) return oldBox;

    splinePoints = computeSpline(linePoints);
    computeArrows();
    
    if(linePoints.size() == 0) {
      BoundingBox.setBounds(0,0,0,0);
      return oldBox;
    }
    MyPoint pt = (MyPoint)linePoints.elementAt(0);
    BoundingBox.setBounds(pt.x,pt.y,0,0);
    for(int i = 1; i < linePoints.size(); i++) {
      pt = (MyPoint)linePoints.elementAt(i);
      BoundingBox.add(pt);
    }
    if(textLabel != null) {
      BoundingBox.add(textLabel.getBounds());
    }
    if(startArrow != null) {
      BoundingBox.add(arrowBBox(true));
    }
    if (endArrow != null) {
      BoundingBox.add(arrowBBox(false));
    }
    MyRectangle bbox = BoundingBox.getBounds();
    int fuzz = (int)Math.round(DrawObject.fudgeFactor);
    BoundingBox.setBounds(bbox.x - fuzz, bbox.y - fuzz, bbox.width + 2*fuzz, bbox.height + 2*fuzz);
    setBoundsFlag(false);
    return oldBox;
  }

  public DrawEdge() {
    //super();
  }

  /**
   * Returns the ending point of the edge.
   *
   * @return the end point of the edge.
   */
  public MyPoint getEPoint() {
    return ePoint;
  }

  /**
   * Returns the starting point of the edge.
   *
   * @return the start point of the edge.
   */
  public MyPoint getSPoint() {
    return sPoint;
  }

  /**
   * Returns the list of edge points.
   *
   * @return the edge points.
   */
  public Vector getLinePoints() {
    return linePoints;
  }

  public boolean handleAttribute(Attribute attr) {
    if(attr == null) {
      return true;
    }

    String key = attr.getName();

    if(key.equals("label")) {
      TextLabel oldTl = textLabel;

      String label = (String)attr.getValue();
      if(label == null || label.length() == 0) {
	label = null;
      }
      if(oldTl == null) {
	textLabel = new TextLabel(label,gc,new MyPoint());
	setRedrawFlag(true);
	setBoundsFlag(true);
      } else if(!oldTl.sameText(label)) {
	textLabel = new TextLabel(label,gc,oldTl.getPosition());
	setRedrawFlag(true);
	setBoundsFlag(true);
      }
    } else if(key.equals("lp")) {
      if(emptyMeansRemove(attr)) return true;
      MyPoint newPosition = pointForTuple(attr.getValue());
      if(textLabel == null) {
	textLabel = new TextLabel(null,gc,newPosition);
      } else if(!textLabel.getPosition().equals(newPosition)) {
	textLabel.setPosition(newPosition);
	setRedrawFlag(true);
	setBoundsFlag(true);
      }
      return true;
    } else if(key.equals("pos")) {
      if(emptyMeansRemove(attr)) return true;
      MyPoint oldSPoint = sPoint;
      MyPoint oldEPoint = ePoint;
      Vector oldPoints = linePoints;

      String points = attr.getValue();
      MyPoint newPoint = null;

      sPoint = null;
      ePoint = null;
      linePoints = new Vector(initialLine,lineIncrement);

      int space = 0;
      arrowType = Grappa.ARROW_NONE;
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
      while (points.length() > 0) {
	space = points.indexOf(' ');
	if(space < 0) {
	  newPoint = pointForTuple(points);
	  points = "";
	} else {
	  newPoint = pointForTuple(points.substring(0,space));
	  points = points.substring(space+1);
	}
	linePoints.addElement(newPoint);
      }
      if(pointsChanged(oldSPoint,oldEPoint,oldPoints)) {
	setRedrawFlag(true);
	setBoundsFlag(true);
	getElement().getGraph().addToBBox(getBounds());
      }
      return true;
    } else if(key.equals("style")) {
      if(emptyMeansRemove(attr)) return true;
      int oldStyle = lineStyle;
      boolean inParens = false;
      lineStyle = 0;
      Vector styles = parseStyle(attr.getValue());
      String token = null;
      for(int i = 0; i < styles.size(); i++) {
	token = (String)styles.elementAt(i);
	if(token.equals(Grappa.DOT_LINE_SOLID_STRING)) {
	  lineStyle = Grappa.DOT_LINE_SOLID;
	} else if(token.equals(Grappa.DOT_LINE_DASHED_STRING)) {
	  lineStyle = Grappa.DOT_LINE_DASHED;
	} else if(token.equals(Grappa.DOT_LINE_DOTTED_STRING)) {
	  lineStyle = Grappa.DOT_LINE_DOTTED;
	}
      }
      if(lineStyle != oldStyle) {
	setRedrawFlag(true);
	setBoundsFlag(true); // needed?
      }
      return true;
    }
    return super.handleAttribute(attr);
  }

  public boolean pointsChanged(MyPoint oldSPoint, MyPoint oldEPoint, Vector oldPoints) {
    if(sPoint != oldSPoint && (sPoint == null || !sPoint.equals(oldSPoint))) {
      return false;
    }
    if(ePoint != oldEPoint && (ePoint == null || !ePoint.equals(oldEPoint))) {
      return false;
    }
    int sz = linePoints.size();
    int i = 0;
    if(oldPoints != null && oldPoints.size() == sz) {
      MyPoint p1, p2;
      for(i = 0; i < sz; i++) {
	p1 = (MyPoint)oldPoints.elementAt(i);
	p2 = (MyPoint)linePoints.elementAt(i);
	if(!p1.equals(p2)) {
	  break;
	}
      }
      sz = i;
    }
    return (sz == i);
  }

  public void setPoints(MyPoint newEPoint, MyPoint newSPoint, Vector newLinePoints) {
    MyPoint oldSPoint = sPoint;
    MyPoint oldEPoint = ePoint;
    Vector oldPoints = linePoints;

    if (newEPoint != null) {
      ePoint = new MyPoint(newEPoint.x,newEPoint.y);
    } else {
      ePoint = null;
    }
    if (newSPoint != null) {
      sPoint = new MyPoint(newSPoint.x,newSPoint.y);
    } else {
      sPoint = null;
    }
    linePoints = new Vector(newLinePoints.size(), lineIncrement);
    MyPoint tmpPoint;
    for(int i = 0; i < newLinePoints.size(); i++) {
      tmpPoint = (MyPoint) newLinePoints.elementAt(i);
      linePoints.addElement(new Point(tmpPoint.x,tmpPoint.y));
    }
    if(pointsChanged(oldSPoint,oldEPoint,oldPoints)) {
      setRedrawFlag(true);
      setBounds();
      getElement().getGraph().addToBBox(getBounds());
    }
  }

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
    MyPoint aPoint;
    for (int i = 0; i < linePoints.size(); i++) {
      aPoint = (MyPoint) linePoints.elementAt(i);
      if(posString.length() > 0) posString.append(" ");
      posString.append(aPoint.x);
      posString.append(",");
      posString.append(aPoint.y);
    }
    return posString.toString();
  }

  /**
   * Draw a spline using a bezier curve
   *
   */
  public void drawSpline(DrawPane pane, int lineStyle, Color lineColor) {
    Graphics gr = pane.getOffscreenGraphics();
    gr.setColor(lineColor);

    MyPoint spt1 = null;
    MyPoint spt2 = null;
    MyPoint pt1 = null;
    MyPoint pt2 = null;
    
    int count = 0;
    for (int j = 1; j < splinePoints.size(); j++) {
      if(lineStyle != Grappa.DOT_LINE_DASHED || count % 2 == 0) {
	spt1 = (MyPoint)splinePoints.elementAt(j-1);
	spt2 = (MyPoint)splinePoints.elementAt(j);
	pt1 = pane.scalePoint(spt1);
	pt2 = pane.scalePoint(spt2);
	if(lineStyle == Grappa.DOT_LINE_DOTTED) {
	  gr.drawLine(pt1.x,pt1.y,pt1.x+1,pt1.y+1);
	  gr.drawLine(pt2.x,pt2.y,pt2.x+1,pt2.y+1);
	} else {
	  gr.drawLine(pt1.x,pt1.y,pt2.x,pt2.y);
	}
      }
      count++;
    }

    if(startArrow != null) {
      drawArrow(pane, lineColor, true);
    }
    if (endArrow != null) {
      drawArrow(pane, lineColor, false);
    }
  }

  public void drawArrow(DrawPane pane, Color lineColor, boolean isStart) {
    MyPolygon poly = null;
    MyPoint toPt = null;
    MyPoint fromPt = null;

    if(isStart) {
      poly = startArrow;
      toPt = sPoint;
      fromPt = (MyPoint)splinePoints.firstElement();
    } else {
      poly = endArrow;
      toPt = ePoint;
      fromPt = (MyPoint)splinePoints.lastElement();
    }
    
    if(poly == null) {
      return;
    }

    Graphics gr = pane.getOffscreenGraphics();
    gr.setColor(lineColor);

    MyPoint pa = new MyPoint();
    MyPoint pb = new MyPoint();

    toPt = pane.scalePoint(toPt);
    fromPt = pane.scalePoint(fromPt);

    gr.drawLine(fromPt.x,fromPt.y,toPt.x,toPt.y);

    poly = pane.scalePoly(poly);

    gr.fillPolygon(poly);

    return;
  }

  public boolean inArrow(MyPoint p, boolean isStart) {
    MyPolygon poly = null;
    MyPoint toPt = null;
    MyPoint fromPt = null;

    if(isStart) {
      poly = startArrow;
      toPt = sPoint;
      fromPt = (MyPoint)splinePoints.firstElement();
    } else {
      poly = endArrow;
      toPt = ePoint;
      fromPt = (MyPoint)splinePoints.lastElement();
    }
    
    if(poly == null) {
      return false;
    }

    if(poly.contains(p)) {
      return true;
    }

   int fuzz = (int)Math.round(DrawObject.fudgeFactor);
   MyRectangle trct = new MyRectangle(Math.min(fromPt.x,toPt.x)-fuzz,Math.min(fromPt.y,toPt.y)-fuzz,Math.abs(fromPt.x-toPt.x)+2*fuzz,Math.abs(fromPt.y-toPt.y)+2*fuzz);

   if(trct.contains(p)) {
     int dx1 = fromPt.x - toPt.x;
     int dy1 = fromPt.y - toPt.y;
     int dx2 = p.x - toPt.x;
     int dy2 = p.y - toPt.y;

     if(dx1 == 0 && dy1 == 0) {
       double dist = Math.sqrt(dx2*dx2 + dy2*dy2);
       if(dist <= DrawObject.fudgeFactor) return true;
     } else {
       double dist = Math.sqrt(dx1*dx1 + dy1*dy1);
       double val = ((double)Math.abs(dy2*dx1-dy1*dx2))/dist;
       if(val <= DrawObject.fudgeFactor) return true;
     }
   }

   return false;
  }

  public MyRectangle arrowBBox(boolean isStart) {
    MyPolygon poly = null;
    MyPoint toPt = null;
    MyPoint fromPt = null;
    MyRectangle bbox = new MyRectangle();

    if(isStart) {
      poly = startArrow;
      toPt = sPoint;
      fromPt = (MyPoint)splinePoints.firstElement();
    } else {
      poly = endArrow;
      toPt = ePoint;
      fromPt = (MyPoint)splinePoints.lastElement();
    }
    
    if(poly == null) {
      return bbox;
    }

    bbox.setBounds(poly.getBounds());
    bbox.add(toPt);
    bbox.add(fromPt);

    return bbox;
  }

  private int distanceSq(MyPoint p1, MyPoint p2) {
    return ((p1.x - p2.x)*(p1.x - p2.x) + (p1.y - p2.y)*(p1.y - p2.y));
  }

  public void computeArrows() {
    MyPoint pt1 = null, pt2 = null;
    MyPoint spt1 = null, spt2 = null;
    Vector pts = null; 
    
    if(sPoint != null) {
      int startp = 0;
      spt1 = (MyPoint)splinePoints.elementAt(0);
      spt2 = (MyPoint)splinePoints.elementAt(bezierDegree);
      if(distanceSq(spt1,spt2) < ARROW_LENGTH_SQ) {
	startp += bezierDegree;
      }
      if(pts == null) {
	pts = new Vector(bezierDegree+1,bezierDegree+1);
      } else {
	pts.removeAllElements();
      }
      double dist = 0;
      for(int i = 0; i <= bezierDegree; i++) {
	pts.addElement(splinePoints.elementAt(startp+i));
	if(i > 0) {
	  dist += Math.sqrt((double)distanceSq((MyPoint)pts.elementAt(i-1),(MyPoint)pts.elementAt(i)));
	}
      }
      double param = ((double)ARROW_LENGTH) / dist;
      if(param > 1.0) {
	param = 1.0;
      } else if(param < 0.1) {
	param = 0.1;
      }
      FPoint[] fpts = new FPoint[bezierDegree+1];
      while(true) {
	pt1 = evaluateBezier(pts,bezierDegree,param,null,fpts);
	if(distanceSq(pt1,(MyPoint)splinePoints.elementAt(0)) < ARROW_LENGTH_SQ) {
	  break;
	}
	param *= (2.0/3.0);
      }
      for(int i = 0; i <= bezierDegree; i++) {
	pt1 = (MyPoint)splinePoints.elementAt(startp+i);
	pt1.move((int)Math.round(fpts[i].x),(int)Math.round(fpts[i].y));
      }
      startArrow = computeArrow(sPoint,(MyPoint)splinePoints.firstElement());
    } else {
      startArrow = null;
    }
    if(ePoint != null) {
      int endp = splinePoints.size() - (bezierDegree+1);
      spt1 = (MyPoint)splinePoints.elementAt(endp);
      spt2 = (MyPoint)splinePoints.elementAt(endp+bezierDegree);
      if(distanceSq(spt1,spt2) < ARROW_LENGTH_SQ) {
	endp -= bezierDegree;
      }
      if(pts == null) {
	pts = new Vector(bezierDegree+1,bezierDegree+1);
      } else {
	pts.removeAllElements();
      }
      double dist = 0;
      for(int i = 0; i <= bezierDegree; i++) {
	pts.insertElementAt(splinePoints.elementAt(endp+i),0);
	if(i > 0) {
	  dist += Math.sqrt((double)distanceSq((MyPoint)pts.elementAt(0),(MyPoint)pts.elementAt(1)));
	}
      }
      double param = ((double)ARROW_LENGTH) / dist;
      if(param > 1.0) {
	param = 1.0;
      } else if(param < 0.1) {
	param = 0.1;
      }
      FPoint[] fpts = new FPoint[bezierDegree+1];
      while(true) {
	pt1 = evaluateBezier(pts,bezierDegree,param,null,fpts);
	int d = 0;
	if((d = distanceSq(pt1,(MyPoint)splinePoints.lastElement())) < ARROW_LENGTH_SQ) {
	  break;
	}
	param *= (2.0/3.0);
      }
      for(int i = 0; i <= bezierDegree; i++) {
	pt1 = (MyPoint)splinePoints.elementAt(endp+i);
	pt1.move((int)Math.round(fpts[bezierDegree-i].x),(int)Math.round(fpts[bezierDegree-i].y));
      }
      endArrow = computeArrow(ePoint,(MyPoint)splinePoints.lastElement());
    } else {
      endArrow = null;
    }
  }

  public MyPolygon computeArrow(MyPoint toPt, MyPoint fromPt) {
    if(toPt == null || fromPt == null || toPt.equals(fromPt)) {
      return null;
    }

    MyPoint pa = new MyPoint();
    MyPoint pb = new MyPoint();

    double theta = Math.atan2((double)(fromPt.y - toPt.y), (double)(fromPt.x-toPt.x));

    double len = arrowLength;
    double wid = (double)arrowWidth / 1.75; // using 1.75 rather than 2.0 looks better

    pa.x = toPt.x + (int)Math.round(len * Math.cos(theta) + wid * Math.sin(theta));
    pa.y = toPt.y + (int)Math.round(len * Math.sin(theta) - wid * Math.cos(theta));
    pb.x = toPt.x + (int)Math.round(len * Math.cos(theta) - wid * Math.sin(theta));
    pb.y = toPt.y + (int)Math.round(len * Math.sin(theta) + wid * Math.cos(theta));

    MyPolygon poly = new MyPolygon();

    poly.addPoint(pa.x,pa.y);
    poly.addPoint(toPt.x,toPt.y);
    poly.addPoint(pb.x,pb.y);

    return poly;
  }

  /***************************
  public MyDimension labelSize(String label, FontMetrics fm) {
    int stringHead = 0;
    int stringWidth = 0;
    String thisLine = null;
    int newLine = 0;
    int width;
    int lines = 0;
    if(label == null || label.length() == 0) {
      newLine = -1;
    }
    while(newLine >= 0) {
      lines++;
      newLine = label.indexOf("\\n", stringHead);
      if(newLine > 0) {
	thisLine = label.substring(stringHead, newLine);
      } else {
	thisLine = label.substring(stringHead);
      }

      if((width = fm.stringWidth(thisLine)) > stringWidth) {
	stringWidth = width;
      }

      stringHead = newLine + 2;
    }
    return new MyDimension(stringWidth, lines * fm.getHeight());
  }

  public void drawLabel(DrawPane pane, String label, MyPoint labelPoint) {
    if(label == null || label.length() == 0) {
      return;
    }

    Graphics gr = pane.getOffscreenGraphics();
    FontMetrics fm = gr.getFontMetrics();

    MyDimension sizeOfLabel = labelSize(label, fm);

    MyPoint labelPos = pane.scalePoint(labelPoint);
    //drawStringsInBox(gr, labelPos, sizeOfLabel, label);
  }
  *************************/

  public boolean PointInSpecificObject(MyPoint p) {
    MyPoint spt1, spt2;
    MyRectangle trct = new MyRectangle();

    if(textLabel != null) {
      if(textLabel.contains(p)) return true;
    }
    if(startArrow != null) {
      if(inArrow(p, true)) return true;
    }
    if (endArrow != null) {
      if(inArrow(p, false)) return true;
    }
   
    int fuzz = (int)Math.round(DrawObject.fudgeFactor);

    for (int j = 1; j < splinePoints.size(); j++) {
      spt1 = (MyPoint)splinePoints.elementAt(j-1);
      spt2 = (MyPoint)splinePoints.elementAt(j);

      trct.setBounds(Math.min(spt1.x,spt2.x)-fuzz,Math.min(spt1.y,spt2.y)-fuzz,Math.abs(spt2.x-spt1.x)+2*fuzz+1,Math.abs(spt2.y-spt1.y)+2*fuzz+1);

      if(trct.contains(p)) {
	int dx1 = spt2.x - spt1.x;
	int dy1 = spt2.y - spt1.y;
	int dx2 = p.x - spt1.x;
	int dy2 = p.y - spt1.y;

	if(dx1 == 0 && dy1 == 0) {
	  double dist = Math.sqrt(dx2*dx2 + dy2*dy2);
	  if(dist <= DrawObject.fudgeFactor) return true;
	} else {
	  double dist = Math.sqrt(dx1*dx1 + dy1*dy1);
	  double val = ((double)Math.abs(dy2*dx1-dy1*dx2))/dist;
	  if(val <= DrawObject.fudgeFactor) return true;
	}
      }
    }
    return false;
  }

  public void highlightSelect() {
    if(DrawEdge.selectGc == null) {
      DrawEdge.selectGc = new GraphicContext();
      DrawEdge.selectGc.setForeground(Grappa.getSelectColor());
      DrawEdge.selectGc.setFontcolor(Grappa.getSelectColor());
      DrawEdge.selectGc.setFillMode(false);
    }
    DrawEdge.selectGc.setBackground(gc.getBackground());
    DrawEdge.selectGc.setFont(gc.getFont());
    DrawEdge.selectGc.setXORColor(gc.getXORColor());
    DrawEdge.selectGc.setXORMode(gc.getXORMode());
    DrawEdge.selectGc.setClipRect(gc.getClipRect());
    DrawEdge.selectGc.setLineStyle(gc.getLineStyle());
    draw(DrawEdge.selectGc,null,true);
  }

  public void highlightDelete() {
    if(DrawEdge.deleteGc == null) {
      DrawEdge.deleteGc = new GraphicContext();
      DrawEdge.deleteGc.setForeground(Grappa.getDeleteColor());
      DrawEdge.deleteGc.setFontcolor(Grappa.getDeleteColor());
    }
    DrawEdge.deleteGc.setFillMode(gc.getFillMode());
    DrawEdge.deleteGc.setBackground(gc.getBackground());
    DrawEdge.deleteGc.setFont(gc.getFont());
    DrawEdge.deleteGc.setXORColor(gc.getXORColor());
    DrawEdge.deleteGc.setXORMode(gc.getXORMode());
    DrawEdge.deleteGc.setClipRect(gc.getClipRect());
    DrawEdge.deleteGc.setLineStyle(gc.getLineStyle());
    draw(DrawEdge.deleteGc,null,true);
  }
}
