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

public abstract class DrawNode extends DrawObject
{
  protected static GraphicContext selectGc = null;
  protected static GraphicContext deleteGc = null;
  public final static int PERIPHERY_GAP = 4;

  public final static int ARC        = 1; // not much used
  public final static int OVAL       = 2;
  public final static int POLYGON    = 3;
  public final static int RECT       = 4;
  public final static int TABLE      = 5;
  public final static int TEXT       = 6;

  public final static int DIAGONALS  = 1;
  public final static int ROUNDED    = 2;
  public final static int AUXLABELS  = 4;
  public final static int FILLED     = 8;

  public final static double RBCONST = 12.0;
  public final static double RBCURVE = 0.5;

  public final static int START_ANGLE    = 90;
  public final static int ARC_ANGLE      = 135;
  /**
   * defines the drawing style of the node; its
   * value is set when the class object is created
   *
   * @see #OVAL
   * @see #OVAL
   */
  protected int shape     = DrawNode.OVAL; // as a default
  protected MyPolygon outline = null;

  // setBounds may alter drawShape to use a polygon to handle skewing, etc.
  protected int drawShape = shape;
  
  protected MyDimension size = new MyDimension(0,0);
  protected MyPoint position = new MyPoint(0,0);

  /**
   * set to true when width should equal height
   */
  protected boolean regular    = false;

  protected int peripheries    = 1;

  protected double orientation     = 0;
  protected double attrOrientation = 0;
  private double drawOrientation   = 0;

  protected int sides          = 0;
  protected double distortion  = 0;
  protected double skew        = 0;
  protected Vector extras      = null;

  protected int style          = 0;
  protected int attrStyle      = 0;
  private int drawStyle        = 0;

  private FPoint polyScale = new FPoint(1.0,1.0);

  /**
   * a vector of IntPairs for drawing (a Vector is used to handle the
   * case of peripheries > 1)
   */
  protected Vector drawPoints = new Vector();

  /**
   * The number of points in gap between periphery lines
   */
  public final static int pointsInGap = 4;

  public DrawNode() {
    //super();
  }

  public MyRectangle setBounds() {
    drawOrientation = attrOrientation + orientation;
    drawStyle = attrStyle | style;

    MyRectangle oldBox = BoundingBox;
    if(!getBoundsFlag()) return oldBox;

    gc.setFillMode((drawStyle&DrawNode.FILLED) != 0);

    setBBox();
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
    return oldBox;
  }

  private void setBBox() {
    int x = position.x - (int)Math.ceil((double)size.width/2.0);
    /*
     * Assume center point (position) is already adjusted for flipped y-axis
     * and just subtract height adjustment (i.e., do y - dy rather
     * than -(y + dy)).
     */
    int y = position.y - (int)Math.ceil((double)size.height/2.0);
    int width = ((size.width % 2) == 0) ? size.width : size.width + 1;
    int height = ((size.height % 2) == 0) ? size.height : size.height + 1;
    BoundingBox.setBounds(x,y,width,height);
    setBoundsFlag(false);
  }

  // called from setBounds (where drawOrientation is set)
  protected void setBox() {
    if(peripheries < 1) return;
    // account for drawing being 1 pixel larger than specified.
    MyRectangle bbox = getBounds();
    int width = bbox.width - 1;
    int height = bbox.height - 1;

    drawPoints.removeAllElements();
    drawPoints.ensureCapacity(peripheries);
    int pairs = 2;
    if(drawShape == DrawNode.ARC) {
      pairs = 3;
      if(extras == null) {
	extras = new Vector(2);
      }
      if(extras.size() == 0) {
	int[] array = new int[2];
	extras.addElement(array);
	array[0] = DrawNode.START_ANGLE;
	array[1] = DrawNode.ARC_ANGLE;
      }
    }
    IntPairs current = new IntPairs(pairs);
    drawPoints.addElement(current);
    current.xArray[0] = bbox.x;
    current.yArray[0] = bbox.y;
    current.xArray[1] = width;
    current.yArray[1] = height;
    IntPairs previous = current;
    if(pairs == 3) {
      int[] extra = (int[])extras.elementAt(0);
      current.insertPairAt(extra[0],extra[1],2);
    }
    for(int i = 1; i < peripheries; i++) {
      current = new IntPairs(pairs);
      drawPoints.addElement(current);
      current.xArray[0] = previous.xArray[0] + DrawNode.PERIPHERY_GAP;
      current.yArray[0] = previous.yArray[0] + DrawNode.PERIPHERY_GAP;
      current.xArray[1] = previous.xArray[1] - 2 * DrawNode.PERIPHERY_GAP;
      current.yArray[1] = previous.yArray[1] - 2 * DrawNode.PERIPHERY_GAP;
      previous = current;
      if(pairs == 3) {
	int[] extra = (int[])extras.elementAt(i);
	current.insertPairAt(extra[0],extra[1],2);
      }
    }
  }

  // called from setBounds (where drawOrientation is set)
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
    FPoint Rpt = new FPoint(0.5 * cosX, 0.5 * sinX);
    angle += (Math.PI - sectorAngle)/2.0;
    //angle = Math.PI/2.0;
    FPoint Ppt = new FPoint();
    double dtmp, alpha;

    Vector rawVertices = new Vector(drawSides);

    FPoint Bpt = new FPoint(0,0);
    for(int i = 0; i < drawSides; i++) {
      // next regular vertex
      angle += sectorAngle;
      sinX = Math.sin(angle);
      cosX = Math.cos(angle);
      Rpt.translate(sideLength*cosX,sideLength*sinX);
      // distort and skew
      Ppt.move(Rpt.x * (skewDist + Rpt.y * gDist) + Rpt.y * gSkew, Rpt.y);
      // orient Ppt
      if(drawOrientation != 0) {
	alpha = ( Math.PI * drawOrientation / 180.0 ) + Math.atan2(Ppt.y,Ppt.x); 
	sinX = Math.sin(alpha);
	cosX = Math.cos(alpha);
	dtmp = Math.sqrt(Ppt.x * Ppt.x + Ppt.y * Ppt.y);
	Ppt.move(dtmp * cosX, dtmp * sinX);
      }
      // scale
      Ppt.scale((double)width,(double)height);
      // store result
      rawVertices.addElement(new FPoint(Ppt));
      if(Bpt.x < Math.abs(Ppt.x)) Bpt.x = Math.abs(Ppt.x);
      if(Bpt.y < Math.abs(Ppt.y)) Bpt.y = Math.abs(Ppt.y);
    }

    Bpt.scale(2.0,2.0);
    Bpt.x = (double)width / Bpt.x;
    Bpt.y = (double)height / Bpt.y;

    drawPoints.removeAllElements();
    drawPoints.ensureCapacity(peripheries);

    int dS = drawSides;
    if((drawStyle&DrawNode.DIAGONALS) != 0) {
      dS = drawSides * 4;
    } else if((drawStyle&DrawNode.ROUNDED) != 0) {
      dS = (bezierPrecision + 2) * drawSides;
    }
    for(int j = 0; j < peripheries; j++) {
      drawPoints.addElement(new IntPairs(dS));
    }
    IntPairs current = (IntPairs)drawPoints.elementAt(0);
	
    MyPoint Ipt = null, Jpt = null, Kpt = null;
    FPoint Tpt = null;
    Vector tmpVertices = null;
    Vector splinePoints = null;
    Vector inputPoints = null;

    if((drawStyle&(DrawNode.ROUNDED|DrawNode.DIAGONALS)) != 0) {
      inputPoints = new Vector(4);
      for(int i = 0; i < 4; i++) inputPoints.addElement(new MyPoint());

      tmpVertices = new Vector(2*(drawSides+1),2*(drawSides+1));
      double rbconst = DrawNode.RBCONST, rbcurve = DrawNode.RBCURVE;
      
      FPoint Pt0;
      FPoint Pt1;
      double dist = 0;
      double tmp = 0;
      Pt0 = (FPoint)rawVertices.elementAt(0);
      Pt0.scale(Bpt.x,Bpt.y);
      for(int i = 0; i < drawSides; i++) {
	// already scaled
	Pt0 = (FPoint)rawVertices.elementAt(i);
	if(i < drawSides - 1) {
	  Pt1 = (FPoint)rawVertices.elementAt(i+1);
	  Pt1.scale(Bpt.x,Bpt.y);
	} else {
	  Pt1 = (FPoint)rawVertices.elementAt(0);
	}
	dist = Math.sqrt((Pt1.x-Pt0.x)*(Pt1.x-Pt0.x) + (Pt1.y-Pt0.y)*(Pt1.y-Pt0.y));
	tmp = rbconst / dist;
	if((drawStyle&DrawNode.DIAGONALS) != 0) {
	  tmpVertices.addElement(new FPoint(Pt0));
	} else {
	  tmpVertices.addElement(FPoint.interpolate(rbcurve*tmp,Pt0,Pt1));
	}
	tmpVertices.addElement(FPoint.interpolate(tmp,Pt0,Pt1));
	tmpVertices.addElement(FPoint.interpolate(1.0-tmp,Pt0,Pt1));
	if((drawStyle&DrawNode.ROUNDED) != 0) {
	  tmpVertices.addElement(FPoint.interpolate(1.0-rbcurve*tmp,Pt0,Pt1));
	}
      }
      tmpVertices.addElement(new FPoint((FPoint)tmpVertices.elementAt(0)));
      tmpVertices.addElement(new FPoint((FPoint)tmpVertices.elementAt(1)));
      tmpVertices.addElement(new FPoint((FPoint)tmpVertices.elementAt(2)));

      for(int i = 0; i < drawSides; i++) {
	if((drawStyle&DrawNode.DIAGONALS) != 0) {
	  Tpt = (FPoint)tmpVertices.elementAt(3*i);
	  current.addPair(position.x + (int)Math.round(Tpt.x),position.y - (int)Math.round(Tpt.y));
	  Tpt = (FPoint)tmpVertices.elementAt(3*i+3);
	  current.addPair(position.x + (int)Math.round(Tpt.x),position.y - (int)Math.round(Tpt.y));
	  Tpt = (FPoint)tmpVertices.elementAt(3*i+2);
	  current.addPair(position.x + (int)Math.round(Tpt.x),position.y - (int)Math.round(Tpt.y));
	  Tpt = (FPoint)tmpVertices.elementAt(3*i+4);
	  current.addPair(position.x + (int)Math.round(Tpt.x),position.y - (int)Math.round(Tpt.y));
	} else {
	  Tpt = (FPoint)tmpVertices.elementAt(4*i+1);
	  current.addPair(position.x + (int)Math.round(Tpt.x),position.y - (int)Math.round(Tpt.y));
	  Tpt = (FPoint)tmpVertices.elementAt(4*i+2);
	  current.addPair(position.x + (int)Math.round(Tpt.x),position.y - (int)Math.round(Tpt.y));
	  for(int j = 0; j<4; j++) {
	    Pt0 = (FPoint)tmpVertices.elementAt(4*i+2+j);
	    Ipt = (MyPoint)inputPoints.elementAt(j);
	    Ipt.x = (int)Math.round(Pt0.x);
	    Ipt.y = (int)Math.round(Pt0.y);
	  }
	  splinePoints = computeSpline(inputPoints);
	  // start at 1?
	  for(int j = 1; j<splinePoints.size(); j++) {
	    Ipt = (MyPoint)splinePoints.elementAt(j);
	    current.addPair(position.x + Ipt.x,position.y - Ipt.y);
	  }
	}
      }
    } else {
      for(int i = 0; i < drawSides; i++) {
	Tpt = (FPoint)rawVertices.elementAt(i);
	Tpt.scale(Bpt.x,Bpt.y);
	current.insertPairAt(position.x + (int)Math.round(Tpt.x),position.y - (int)Math.round(Tpt.y),i);
      }
    }

    outline = new MyPolygon(current.xArray,current.yArray,current.size());

    if(peripheries > 1) {
      FPoint Spt = null;
      FPoint Upt = null, Vpt = null;
      FPoint Qpt = new FPoint();
      FPoint PriorVertex = null;
      double beta, gamma, delta;
      int start = 0;
      int end = drawSides;

      Qpt.move((FPoint)rawVertices.elementAt(drawSides-1));
      Rpt.move((FPoint)rawVertices.elementAt(0));
      beta = Math.atan2(Rpt.y-Qpt.y,Rpt.x-Qpt.x);

      if((drawStyle&(DrawNode.DIAGONALS|DrawNode.ROUNDED)) != 0) {
	Jpt = new MyPoint();
	Kpt = new MyPoint();
	Spt = new FPoint();
	// set-up to compute this vertex and next vertex
	Qpt.move(Rpt);
	Rpt.move((FPoint)rawVertices.elementAt(1));
	alpha = beta;
	beta = Math.atan2(Rpt.y-Qpt.y,Rpt.x-Qpt.x);
	gamma = (alpha + Math.PI - beta) / 2.0;
	delta = alpha - gamma;
	sinX = Math.sin(delta);
	cosX = Math.cos(delta);
	dtmp = - DrawNode.PERIPHERY_GAP / Math.sin(gamma);
	sinX *= dtmp;
	cosX *= dtmp;
	PriorVertex = new FPoint(cosX,sinX);

	start = 1;
	end = drawSides + 1;
      }

      for(int i = start; i < end; i++) {
	// for each vertex, find the bisector
	//Ppt.move(Qpt);
	Qpt.move(Rpt);
	Rpt.move((FPoint)rawVertices.elementAt((i+1)%drawSides));
	alpha = beta;
	beta = Math.atan2(Rpt.y-Qpt.y,Rpt.x-Qpt.x);
	gamma = (alpha + Math.PI - beta) / 2.0;

	/*
	 * find the distance along the bisector to the
	 * intersection of the next periphery
	 */
	dtmp = - DrawNode.PERIPHERY_GAP / Math.sin(gamma);

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
	  Tpt = (FPoint)tmpVertices.elementAt(3*k);
	  Ppt.move((FPoint)tmpVertices.elementAt(3*k+3));
	  Upt = (FPoint)tmpVertices.elementAt(3*k+2);
	  Vpt = (FPoint)tmpVertices.elementAt(3*k+4);
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
	  PriorVertex.move(cosX,sinX);
	} else {
	  int k = i - 1;
	  Ppt.move((FPoint)tmpVertices.elementAt(4*k+1));
	  Ppt.translate(PriorVertex.x,PriorVertex.y);
	  Jpt.move((int)Math.round(Ppt.x),(int)Math.round(Ppt.y));
	  Ppt.move((FPoint)tmpVertices.elementAt(4*k+2));
	  Ppt.translate(cosX,sinX);
	  Kpt.move((int)Math.round(Ppt.x),(int)Math.round(Ppt.y));

	  for(int j = 0; j<4; j++) {
	    Spt.move((FPoint)tmpVertices.elementAt(4*k+2+j));
	    Spt.translate(cosX,sinX);
	    Ipt = (MyPoint)inputPoints.elementAt(j);
	    Ipt.x = (int)Math.round(Spt.x);
	    Ipt.y = (int)Math.round(Spt.y);
	  }
	  splinePoints = computeSpline(inputPoints);
	  for(int j = 1; j<peripheries; j++) {
	    current = (IntPairs)drawPoints.elementAt(j);

	    current.addPair(position.x + Jpt.x,position.y - Jpt.y);
	    Jpt.translate((int)Math.round(PriorVertex.x),(int)Math.round(PriorVertex.y));
	    current.addPair(position.x + Kpt.x,position.y - Kpt.y);
	    Kpt.translate((int)Math.round(cosX),(int)Math.round(sinX));
	    for(k = 1; k<splinePoints.size(); k++) {
	      Ipt = (MyPoint)splinePoints.elementAt(k);
	      current.addPair(position.x + Ipt.x,position.y - Ipt.y);
	      Ipt.translate((int)Math.round(cosX),(int)Math.round(sinX));
	    }
	  }
	  PriorVertex.move(cosX,sinX);
	}
      }
    }
  }

  // this method is overwritten in the Table class
  protected void setTable() {
    throw new IllegalStateException("the setTable() method should only be used by Table and Table-derived objects");
  }

  // called from setBounds (where drawOrientation is set)
  private void setRecord() {
  }

  /**
   * Adjust values based on specified attribute pair.
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
    
    if(key.equals("distortion")) {
      if(emptyMeansRemove(attr)) return true;
      double oldDistortion = distortion;
      distortion = Double.valueOf(attr.getValue()).doubleValue();
      if(oldDistortion != distortion) {
	setRedrawFlag(true);
	setBoundsFlag(true);
      }
      return true;
    } else if(key.equals("height")) {
      if(emptyMeansRemove(attr)) return true;
      Attribute attr2 = getElement().getAttribute("width");
      if(attr2 == null) {
	attr2 = attr;
      } else if(regular) {
	if(Double.valueOf(attr.getValue()).doubleValue() < Double.valueOf(attr2.getValue()).doubleValue()) {
	  attr2 = attr;
	} else {
	  attr = attr2;
	}
      }
      MyDimension oldSize = size;
      size = dimensionForWidthHeight(attr2.getValue(),attr.getValue());
      if(!oldSize.equals(size)) {
	setRedrawFlag(true);
	setBoundsFlag(true);
      }
      return true;
    } else if(key.equals("label")) {
      TextLabel oldTl = textLabel;

      String label = (String)attr.getValue();
      if(label == null || label.length() == 0) {
	label = null;
      } else if(label.equals("\\N")) {
	label = getElement().getName();
      }
      if(oldTl == null || !oldTl.sameText(label)) {
	textLabel = new TextLabel(label,gc,position);
	setRedrawFlag(true);
	setBoundsFlag(true);
      }
    } else if(key.equals("orientation")) {
      if(emptyMeansRemove(attr)) return true;
      double oldOrientation = attrOrientation;
      attrOrientation = Double.valueOf(attr.getValue()).doubleValue();
      if(oldOrientation != attrOrientation) {
	setRedrawFlag(true);
	setBoundsFlag(true);
      }
      return true;
    } else if(key.equals("peripheries")) {
      if(emptyMeansRemove(attr)) return true;
      int oldPeripheries = peripheries;
      peripheries = Integer.valueOf(attr.getValue()).intValue();
      if(oldPeripheries != peripheries) {
	setRedrawFlag(true);
	setBoundsFlag(true);
      }
      return true;
    } else if(key.equals("pos")) {
      if(emptyMeansRemove(attr)) return true;
      MyPoint newPosition = pointForTuple(attr.getValue());
      if(!position.equals(newPosition)) {
	position.move(newPosition.x,newPosition.y);
	setRedrawFlag(true);
	setBoundsFlag(true);
      }
      return true;
    } else if(key.equals("sides")) {
      if(emptyMeansRemove(attr)) return true;
      int oldSides = sides;
      sides = Integer.valueOf(attr.getValue()).intValue();
      if(oldSides != sides) {
	setRedrawFlag(true);
	setBoundsFlag(true);
      }
      return true;
    } else if(key.equals("skew")) {
      if(emptyMeansRemove(attr)) return true;
      double oldSkew = skew;
      skew = Double.valueOf(attr.getValue()).doubleValue();
      if(oldSkew != skew) {
	setRedrawFlag(true);
	setBoundsFlag(true);
      }
      return true;
    } else if(key.equals("style")) {
      if(emptyMeansRemove(attr)) return true;
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
	}
      }
      if((style|attrStyle) != (style|oldStyle)) {
	setRedrawFlag(true);
	setBoundsFlag(true);
      }
      return true;
    } else if(key.equals("width")) {
      if(emptyMeansRemove(attr)) return true;
      Attribute attr2 = getElement().getAttribute("height");
      if(attr2 == null) {
	attr2 = attr;
      } else if(regular) {
	if(Double.valueOf(attr.getValue()).doubleValue() < Double.valueOf(attr2.getValue()).doubleValue()) {
	  attr2 = attr;
	} else {
	  attr = attr2;
	}
      }
      MyDimension oldSize = size;
      size = dimensionForWidthHeight(attr.getValue(),attr2.getValue());
      if(!oldSize.equals(size)) {
	setRedrawFlag(true);
	setBoundsFlag(true);
      }
      return true;
    }
    return super.handleAttribute(attr);
  }

  /**
   * @return the node position (center point).
   */
  public MyPoint getPosition() {
    return position;
  }

  /**
   * @return the node size.
   */
  public MyDimension getSize() {
    return size;
  }

  /**
   * Draws the node using the specified graphic context.
   *
   * @param context the graphic context to use when drawing.
   *                If the context is null, the object's context is used.
   * @param pane restrict drawing to the supplied pane, if supplied.
   */
  public void draw(GraphicContext context, DrawPane pane, boolean paintNow) {
    DotGraph graph = getElement().getGraph();
    Enumeration panes = null;
    DrawPane pnl = null;

    if(pane == null) {
      panes = graph.getPanes();
    
      if(!panes.hasMoreElements()) {
	return;
      }
      pnl = (DrawPane)panes.nextElement();
    } else {
      pnl = pane;
    }

    setBounds();

    if(context == null) {
      context = gc;
    }
    Color fillColor = getParentGC().getBackground();
    Color lineColor = context.getForeground();
    if(context.getFillMode()) {
      fillColor = context.getForeground();
      lineColor = getParentGC().getForeground();
    }

    Graphics gr = null;
    MyPoint scaledCenter = null;
    MyRectangle scaledBounds = null;
    IntPairs drawPairs = null;
    IntPairs scaledPairs = null;
    MyRectangle scaledRect = null;
    while(true) {
      gr = pnl.getOffscreenGraphics();

      if(peripheries >= 1) {
	scaledCenter = pnl.scalePoint(position);
	switch(drawShape) {
	case DrawNode.ARC:
	  for(int i = 0; i < drawPoints.size(); i++) {
	    drawPairs = (IntPairs)(drawPoints.elementAt(i));
	    scaledRect = pnl.scaleRect(drawPairs.xArray[0],drawPairs.yArray[0],drawPairs.xArray[1],drawPairs.yArray[1]);

	    gr.setColor(fillColor);
	    gr.fillArc(scaledRect.x,scaledRect.y,scaledRect.width,scaledRect.height,drawPairs.xArray[2],drawPairs.yArray[2]);
	    gr.setColor(lineColor);
	    gr.drawArc(scaledRect.x,scaledRect.y,scaledRect.width,scaledRect.height,drawPairs.xArray[2],drawPairs.yArray[2]);
	  }
	  break;
	case DrawNode.OVAL:
	  for(int i = 0; i < drawPoints.size(); i++) {
	    drawPairs = (IntPairs)(drawPoints.elementAt(i));
	    scaledRect = pnl.scaleRect(drawPairs.xArray[0],drawPairs.yArray[0],drawPairs.xArray[1],drawPairs.yArray[1]);

	    gr.setColor(fillColor);
	    gr.fillOval(scaledRect.x,scaledRect.y,scaledRect.width,scaledRect.height);
	    gr.setColor(lineColor);
	    gr.drawOval(scaledRect.x,scaledRect.y,scaledRect.width,scaledRect.height);
	    if((drawStyle&DrawNode.DIAGONALS) != 0) {
	      Mcircle_hack(scaledCenter,scaledRect,gr);
	    }
	  }
	  break;
	case DrawNode.POLYGON:
	  for(int i = 0; i < drawPoints.size(); i++) {
	    drawPairs = (IntPairs)(drawPoints.elementAt(i));
	    scaledPairs = pnl.scalePairs(drawPairs);

	    gr.setColor(fillColor);
	    gr.fillPolygon(scaledPairs.xArray,scaledPairs.yArray,scaledPairs.size());
	    gr.setColor(lineColor);
	    gr.drawPolygon(scaledPairs.xArray,scaledPairs.yArray,scaledPairs.size());
	  }
	  break;
	case DrawNode.TEXT:
	  break;
	case DrawNode.RECT:
	  for(int i = 0; i < drawPoints.size(); i++) {
	    drawPairs = (IntPairs)(drawPoints.elementAt(i));
	    scaledRect = pnl.scaleRect(drawPairs.xArray[0],drawPairs.yArray[0],drawPairs.xArray[1],drawPairs.yArray[1]);

	    gr.setColor(fillColor);
	    gr.fillRect(scaledRect.x,scaledRect.y,scaledRect.width,scaledRect.height);
	    gr.setColor(lineColor);
	    gr.drawRect(scaledRect.x,scaledRect.y,scaledRect.width,scaledRect.height);
	  }
	  break;
	default:
	  throw new IllegalStateException("node drawing shape is not recognized (value is " + shape + ")");
	}
      }

      if((drawStyle&DrawNode.AUXLABELS) != 0) {
	Attribute attr = null;
	if((attr = getElement().getAttribute("toplabel")) != null) {
	  if(scaledCenter == null) scaledCenter = pnl.scalePoint(position);
	  if(scaledBounds == null) scaledBounds = pnl.scaleRect(getBounds());
	  Mlabel_hack(attr.getValue(),true,scaledCenter,scaledBounds,pnl,context);
	}
	if((attr = getElement().getAttribute("bottomlabel")) != null) {
	  if(scaledCenter == null) scaledCenter = pnl.scalePoint(position);
	  if(scaledBounds == null) scaledBounds = pnl.scaleRect(getBounds());
	  Mlabel_hack(attr.getValue(),false,scaledCenter,scaledBounds,pnl,context);
	}
      }
      
      if(textLabel != null && textLabel.hasText()) {
	textLabel.draw(pnl,context,false);
      }
      if(paintNow) pnl.paintCanvas();
      if(pane != null || !panes.hasMoreElements()) break;
      pnl = (DrawPane)panes.nextElement();
    }
  }

  /*
   * hacks a la dot's shapes.c
   */

  protected void Mlabel_hack(String auxlabel, boolean atTop, MyPoint center, MyRectangle bounds, DrawPane pnl, GraphicContext context) {
    int side = 1;

    if(atTop) side = -1;

    int fontsize = context.getFontsize();
    int fs = (int)Math.round(fontsize * 0.8);

    double ht = (double) getBounds().height;
    double factor = ht / (double) bounds.height;
    MyPoint pos = new MyPoint(position.x, position.y + side * (int)Math.round((ht - factor * (double)fs ) / 2.0));
    

    context.setFontsize(fs);
    context.setFont();
    pnl.drawString(auxlabel,context,pos,DrawPane.JUSTIFY_CENTER|DrawPane.JUSTIFY_MIDDLE);
    context.setFontsize(fontsize);
    context.setFont();
  }

  // xhack^2 + yhack^2 == 1
  private final static double Mcircle_xhack = Math.sqrt(7.0/16.0);
  private final static double Mcircle_yhack = 0.75;

  private void Mcircle_hack(MyPoint center, MyRectangle scaled, Graphics gr) {
    // assumes line color is set and rectangle is scaled and symmetric
    MyPoint pt = new MyPoint((int)Math.round((double)(scaled.width) * DrawNode.Mcircle_xhack / 2.0), (int)Math.round((double)(scaled.height) * DrawNode.Mcircle_yhack / 2.0));

    int x1 = center.x + pt.x;
    int y1 = center.y - pt.y;
    int x2 = x1 - 2 * pt.x;
    int y2 = y1;
    gr.drawLine(x1,y1,x2,y2);
    y1 += (2 * pt.y) - 1;
    y2 = y1;
    gr.drawLine(x1,y1,x2,y2);
  }

  public int getDrawStyle() {
    return drawStyle;
  }

  public double getDrawOrientation() {
    return drawOrientation;
  }

  public boolean PointInSpecificObject(MyPoint p) {

    switch(drawShape) {
    case DrawNode.POLYGON:
      if(outline != null) return outline.contains(p);
      break;
    case DrawNode.OVAL:
      return DrawNode.inOval(p,getBounds(),1); // fixed lineWidth for now
    case DrawNode.TEXT:
    case DrawNode.RECT:
    case DrawNode.TABLE:
      return getBounds().contains(p);
    }

    return false;
  }

  public static boolean inOval(MyPoint p, MyRectangle bbox, int lineWidth) {
    double lwAdj = 0.5;

    if(lineWidth > 1) lwAdj = (double)lineWidth / 2.0;

    MyPoint delta = new MyPoint((2*(p.x - bbox.x) - bbox.width + 1)/2,
			      (2*(p.y - bbox.y) - bbox.height + 1)/2);
    FPoint normal = new FPoint((double)(2 * delta.x) / ((double)bbox.width - 1.0 + lwAdj), (double)(2 * delta.y) / ((double)bbox.height - 1.0 + lwAdj));

    return(Math.sqrt(normal.x*normal.x + normal.y*normal.y) <= 1.0);
  }

  protected void setDefaultDrawAttributes() {
    if(appObject == null) return;

    Attribute oldAttr = null;

    super.setDefaultDrawAttributes();

    if((oldAttr = getElement().getAttribute("width")) == null) {
      oldAttr = getElement().setAttribute(new Attribute("width","0.75"));
    }
    if((oldAttr = getElement().getAttribute("height")) == null) {
      oldAttr = getElement().setAttribute(new Attribute("height","0.5"));
    }

    return;
  }

  public void highlightSelect() {
    if(DrawNode.selectGc == null) {
      DrawNode.selectGc = new GraphicContext();
      DrawNode.selectGc.setForeground(Grappa.getSelectColor());
      DrawNode.selectGc.setFontcolor(Grappa.getSelectFontcolor());
      DrawNode.selectGc.setFillMode(true);
    }
    DrawNode.selectGc.setBackground(gc.getBackground());
    DrawNode.selectGc.setFont(gc.getFont());
    DrawNode.selectGc.setXORColor(gc.getXORColor());
    DrawNode.selectGc.setXORMode(gc.getXORMode());
    DrawNode.selectGc.setClipRect(gc.getClipRect());
    DrawNode.selectGc.setLineStyle(gc.getLineStyle());
    draw(DrawNode.selectGc,null,true);
  }

  public void highlightDelete() {
    if(DrawNode.deleteGc == null) {
      DrawNode.deleteGc = new GraphicContext();
      DrawNode.deleteGc.setForeground(Grappa.getDeleteColor());
      DrawNode.deleteGc.setFontcolor(Grappa.getDeleteColor());
    }
    DrawNode.deleteGc.setFillMode(gc.getFillMode());
    DrawNode.deleteGc.setBackground(gc.getBackground());
    DrawNode.deleteGc.setFont(gc.getFont());
    DrawNode.deleteGc.setXORColor(gc.getXORColor());
    DrawNode.deleteGc.setXORMode(gc.getXORMode());
    DrawNode.deleteGc.setClipRect(gc.getClipRect());
    DrawNode.deleteGc.setLineStyle(gc.getLineStyle());
    draw(DrawNode.deleteGc,null,true);
  }
}
