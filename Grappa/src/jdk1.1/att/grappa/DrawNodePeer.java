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
 * This class provides the basis for actually drawing node elements on a pane.
 * Extensions of this class and its subclasses allow for the specific drawing
 * requirements of a particular node element.  For each <code>DrawNode</code>
 * instance and target <code>DrawPane</code> instance, there is one
 * <code>DrawNodePeer</code> instance.
 * The size and position of the object are possibly scaled and translated from
 * the values originally supplied through the element attributes as
 * a consequence of the characteristics of the <code>DrawPane</code>.
 * Certainly the y-axis is flipped to account for the origin at the upper-left
 * as used by the AWT versus the origin to the lower-left as used by the graph.
 *
 * @version 1.1, 30 Sep 1999; Copyright 1996 - 1999 by AT&T Corp.
 * @author  <a href="mailto:john@research.att.com">John Mocenigo</a>, <a href="http://www.research.att.com">Research @ AT&T Labs</a>
 */
public class DrawNodePeer extends DrawObjectPeer
{
  Point center = null;
  Object[] outlines = null;
  
  /**
   * Create an instance of a <code>DrawNodePeer</code>
   * related to the supplied <code>DrawNode</code>
   * and <code>DrawPane</code>.
   *
   * @param drawNode the source object for this peer object
   * @param pane the pane to use when drawing this peer
   */
  public DrawNodePeer(DrawNode drawNode, DrawPane pane) {
    super((DrawObject)drawNode,pane);
  }

  /**
   * Gets the bounds of this peer in terms of the <code>DrawPane</code> coordinates.
   *
   * @return the bounding box of the peer
   */
  public Rectangle getBounds() {
    if(outlines == null || outlines.length == 0) {
      setupPeer(true);
      //throw new IllegalStateException("cannot ask for bounds before Peer is built (" + getDrawObject().getElement().getName() + ")");
    }
    return ((Shape)outlines[0]).getBounds();
  }


  /**
   * Draws the node using the specified graphics information.
   *
   * @param gr the AWT graphics context to use for drawing, if the value
   *           is null, then the off-screen canvas <code>Graphics</code> is used.
   * @param context the graphic context to use when drawing.
   *                If the context is null, the object's context is used.
   */
  public void draw(Graphics gr, GraphicContext context) {
    if(context == null) {
      context = getDrawObject().getGC();
    }

    if(gr == null) gr = getDrawPane().getCanvasGraphics();

    if(getDrawObject().getColorGC().getXORMode()) {
      gr.setXORMode(getDrawObject().getColorGC().getXORColor());
    } else {
      gr.setPaintMode();
    }

    Rectangle[] rectangles = null;
    Polygon[] polygons = null;

    if(getDrawNode().getPeripheries() >= 1) {
      switch(getDrawNode().getDrawShape()) {
      case DrawNode.OVAL:
	rectangles = (Rectangle[])outlines;
	for(int i = 0; i < rectangles.length; i++) {
	  gr.setColor(getDrawObject().getColorGC().getBackground());
	  gr.fillOval(rectangles[i].x,rectangles[i].y,rectangles[i].width,rectangles[i].height);
	  gr.setColor(getDrawObject().getColorGC().getForeground());
	  gr.drawOval(rectangles[i].x,rectangles[i].y,rectangles[i].width,rectangles[i].height);
	  if((getDrawNode().getDrawStyle()&DrawNode.DIAGONALS) != 0) {
	    Mcircle_hack(center,(Rectangle)outlines[i],gr);
	  }
	}
	break;
      case DrawNode.POLYGON:
	polygons = (Polygon[])outlines;
	for(int i = 0; i < polygons.length; i++) {
	  gr.setColor(getDrawObject().getColorGC().getBackground());
	  gr.fillPolygon(polygons[i]);
	  gr.setColor(getDrawObject().getColorGC().getForeground());
	  gr.drawPolygon(polygons[i]);
	}
	break;
      case DrawNode.TEXT:
	break;
      case DrawNode.RECT:
	rectangles = (Rectangle[])outlines;
	for(int i = 0; i < rectangles.length; i++) {
	  gr.setColor(getDrawObject().getColorGC().getBackground());
	  gr.fillRect(rectangles[i].x,rectangles[i].y,rectangles[i].width,rectangles[i].height);
	  gr.setColor(getDrawObject().getColorGC().getForeground());
	  gr.drawRect(rectangles[i].x,rectangles[i].y,rectangles[i].width,rectangles[i].height);
	}
	break;
      default:
	throw new IllegalStateException("node drawing shape is not recognized (value is " + getDrawNode().getShape() + ")");
      }
    }

    if((getDrawNode().getDrawStyle()&DrawNode.AUXLABELS) != 0) {
      Attribute attr = null;
      if((attr = getDrawObject().getElement().getAttribute("toplabel")) != null) {
	Mlabel_hack(attr.getValue(),true,getCenter(),getBounds(),context,gr);
      }
      if((attr = getDrawObject().getElement().getAttribute("bottomlabel")) != null) {
	Mlabel_hack(attr.getValue(),false,getCenter(),getBounds(),context,gr);
      }
    }
      
    if(getTextLabelPeer() != null) {
      getTextLabelPeer().draw(gr, context);
    }
  }

  /**
   * Get the center point of the node.
   * This value is the <i>pos</i> attribute value translated into the
   * <code>DrawPane</code> co-ordinates.
   *
   * @return the center point of the node
   */
  public Point getCenter() {
    return center;
  }

  int count = 0;

  /**
   * Setup this peer.
   * The setup does the actual co-ordinate mappings from what is available
   * in the <code>DrawNode</code> object to the <code>DrawPane</code> co-ordinates.
   *
   * @param setupTextToo indicates if text peer  should be setup as well
   */
  public void setupPeer(boolean setupTextToo) {
    if(setupTextToo && getTextLabelPeer() != null) {
	getTextLabelPeer().setupPeer();
    }
    center = getDrawPane().scalePoint(getDrawNode().getPosition());
    if(getDrawNode().getPeripheries() < 1) return;
    Vector drawPoints = getDrawNode().getDrawPoints();
    IntPairs drawPairs = null;
    switch(getDrawNode().getDrawShape()) {
    case DrawNode.TEXT:
      break;
    case DrawNode.RECT:
    case DrawNode.OVAL:
      Rectangle[] rectangles = new Rectangle[getDrawNode().getPeripheries()];
      outlines = (Object[])rectangles;
      double factor = 0;
      for(int i = 0; i < drawPoints.size(); i++) {
	drawPairs = (IntPairs)(drawPoints.elementAt(i));
	if(getDrawNode().isFixedSize()) {
	  int x = center.x - (int)Math.round((float)drawPairs.xArray[1]/2.0);
	  int y = center.y + (int)Math.round((float)drawPairs.yArray[1]/2.0);
	  rectangles[i] = new Rectangle(x,y,drawPairs.xArray[1],drawPairs.yArray[1]);
	} else if((factor = getDrawPane().getFixedSizeFactor()) != 0) {
	  int w = (int)Math.round(factor*(double)drawPairs.xArray[1]);
	  int h = (int)Math.round(factor*(double)drawPairs.yArray[1]);
	  int x = center.x - (int)Math.round((float)w/2.0);
	  int y = center.y - (int)Math.round((float)h/2.0);
	  rectangles[i] = new Rectangle(x,y,w,h);
	} else {
	  rectangles[i] = getDrawPane().scaleRect(drawPairs.xArray[0],drawPairs.yArray[0],drawPairs.xArray[1],drawPairs.yArray[1]);
	}
      }
      break;
    case DrawNode.POLYGON:
      Polygon[] polygons = new Polygon[getDrawNode().getPeripheries()];
      outlines = (Object[])polygons;
      for(int i = 0; i < drawPoints.size(); i++) {
	drawPairs = (IntPairs)(drawPoints.elementAt(i));
	polygons[i] = getDrawPane().scalePoly(drawPairs);
      }
      break;
    default:
      throw new IllegalStateException("node drawing shape is not recognized (value is " + getDrawNode().getShape() + ")");
    }
  }

  /*
   * hacks a la dot's shapes.c
   */

  private void Mlabel_hack(String auxlabel, boolean atTop, Point center, Rectangle bounds, GraphicContext context, Graphics gr) {
    int side = 1;

    if(atTop) side = -1;

    int fontsize = context.getFontsize();
    int fs = (int)Math.round(fontsize * 0.8);

    double ht = (double) getBounds().height;
    double factor = ht / (double) bounds.height;
    Point pos = new Point(getDrawNode().getPosition().x, getDrawNode().getPosition().y + side * (int)Math.round((ht - factor * (double)fs ) / 2.0));
    

    context.setFontsize(fs);
    context.setFont();
    TextLabelPeer.drawString(auxlabel,pos,TextLabel.JUSTIFY_CENTER|TextLabel.JUSTIFY_MIDDLE,context,gr,getDrawPane());
    context.setFontsize(fontsize);
    context.setFont();
  }

  // xhack^2 + yhack^2 == 1
  private final static double Mcircle_xhack = Math.sqrt(7.0/16.0);
  private final static double Mcircle_yhack = 0.75;

  private void Mcircle_hack(Point center, Rectangle scaled, Graphics gr) {
    // assumes line color is set and rectangle is scaled and symmetric
    Point pt = new Point((int)Math.round((double)(scaled.width) * DrawNodePeer.Mcircle_xhack / 2.0), (int)Math.round((double)(scaled.height) * DrawNodePeer.Mcircle_yhack / 2.0));

    int x1 = center.x + pt.x;
    int y1 = center.y - pt.y;
    int x2 = x1 - 2 * pt.x;
    int y2 = y1;
    gr.drawLine(x1,y1,x2,y2);
    y1 += (2 * pt.y) - 1;
    y2 = y1;
    gr.drawLine(x1,y1,x2,y2);
  }

  /**
   * Checks if the supplied co-ordinates are in the specific shape of
   * this peer.
   *
   * @param x the x co-ordinate to check
   * @param y the y co-ordinate to check
   * @return true if the co-ordinates lie inside the peer or on its boundary,
   *         false otherwise.
   */
  public boolean inPeer(int x, int y) {
    switch(getDrawNode().getDrawShape()) {
    case DrawNode.POLYGON:
      if(outlines != null && getDrawNode().getPeripheries() >= 1) {
	Polygon outline = (Polygon)outlines[0];
	return outline.contains(x, y);
      }
      break;
    case DrawNode.OVAL:
      return DrawNodePeer.inOval(x, y ,getBounds(),1); // fixed lineWidth for now
    case DrawNode.TEXT:
      //TODO: refine the text test
    case DrawNode.RECT:
    case DrawNode.TABLE:
      return getBounds().contains(x, y);
    }

    return false;
  }

  /**
   * Check if the supplied co-ordinates are in the oval specified by the
   * given bounding box and line width.
   *
   * @param x the x co-ordinate
   * @param y the y co-ordinate
   * @param bbox the bounding box of the oval
   * @param lineWidth the width of the oval outline
   * @return true if the co-ordinates are in the oval, false otherwise
   */
  public static boolean inOval(int x, int y, Rectangle bbox, int lineWidth) {
    double lwAdj = 0.5;

    if(lineWidth > 1) lwAdj = (double)lineWidth / 2.0;

    Point delta = new Point((2*(x - bbox.x) - bbox.width + 1)/2,
			      (2*(y - bbox.y) - bbox.height + 1)/2);
    DoublePoint normal = new DoublePoint((double)(2 * delta.x) / ((double)bbox.width - 1.0 + lwAdj), (double)(2 * delta.y) / ((double)bbox.height - 1.0 + lwAdj));

    return(Math.sqrt(normal.x*normal.x + normal.y*normal.y) <= 1.0);
  }

  /**
   * Get the <code>DrawNode</code> object associated with this peer
   *
   * @return the associated <code>DrawNode</code> object
   */
  public DrawNode getDrawNode() {
    return (DrawNode)getDrawObject();
  }
}
