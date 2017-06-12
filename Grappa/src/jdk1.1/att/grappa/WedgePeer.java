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
 * This class provides the basis for actually drawing wedge elements on a pane.
 * This shape is not implemented in a very thorough manner because there has
 * not been much call for it.
 *
 * @version 1.1, 30 Sep 1999; Copyright 1996 - 1999 by AT&T Corp.
 * @author  <a href="mailto:john@research.att.com">John Mocenigo</a>, <a href="http://www.research.att.com">Research @ AT&T Labs</a>
 */
public class WedgePeer extends DrawNodePeer
{
  /**
   * Create an instance of a <code>WedgePeer</code>
   * related to the supplied <code>Wedge</code>
   * and <code>DrawPane</code>.
   *
   * @param wedge the source object for this peer object
   * @param pane the pane to use when drawing this peer
   */
  public WedgePeer(Wedge wedge, DrawPane pane) {
    super((DrawNode)wedge, pane);
  }


  /**
   * Draws the wedge using the specified graphics information.
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

    Color fillColor = getDrawObject().getParentGC().getBackground();
    Color lineColor = context.getForeground();
    if(context.getFillMode()) {
      fillColor = context.getForeground();
      lineColor = getDrawObject().getParentGC().getForeground();
    }

    if(gr == null) gr = getDrawPane().getCanvasGraphics();

    if(context.getXORMode()) {
      gr.setXORMode(context.getXORColor());
    } else {
      gr.setPaintMode();
    }

    Point scaledCenter = null;
    Rectangle scaledBounds = null;
    IntPairs drawPairs = null;
    IntPairs scaledPairs = null;
    Rectangle scaledRect = null;

    Rectangle[] rectangles = null;

    if(getDrawNode().getPeripheries() >= 1) {
      rectangles = (Rectangle[])outlines;
      for(int i = 0; i < rectangles.length; i++) {
	gr.setColor(fillColor);
	gr.fillArc(rectangles[i].x,rectangles[i].y,rectangles[i].width,rectangles[i].height,getWedge().getStartAngle(),getWedge().getArcAngle());
	gr.setColor(lineColor);
	gr.drawArc(rectangles[i].x,rectangles[i].y,rectangles[i].width,rectangles[i].height,getWedge().getStartAngle(),getWedge().getArcAngle());
      }
    }
      
    if(getTextLabelPeer() != null) {
      getTextLabelPeer().draw(gr, context);
    }
  }

  //public void setPoints() {
    //center = getDrawPane().scalePoint(getDrawNode().getPosition());
    //if(getDrawNode().getPeripheries() < 1) return;
    //Vector drawPoints = getDrawNode().getDrawPoints();
    //IntPairs drawPairs = null;
    //Rectangle[] rectangles = new Rectangle[getDrawNode().getPeripheries()];
    //outlines = (Object[])rectangles;
    //for(int i = 0; i < drawPoints.size(); i++) {
      //drawPairs = (IntPairs)(drawPoints.elementAt(i));
      //rectangles[i] = getDrawPane().scaleRect(drawPairs.xArray[0],drawPairs.yArray[0],drawPairs.xArray[1],drawPairs.yArray[1]);
    //}
  //}

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
    return getBounds().contains(x, y);
  }

  /**
   * Get the <code>Wedge</code> object associated with this peer.
   *
   * @return the associated <code>Wedge</code> object
   */
  public Wedge getWedge() {
    return (Wedge)getDrawObject();
  }
}
