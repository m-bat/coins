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
 * This class provides the basis for actually drawing edge elements on a pane.
 * Extensions of this class and its subclasses allow for the specific drawing
 * requirements of a particular edge element.  For each <code>DrawEdge</code>
 * instance and target <code>DrawPane</code> instance, there is one
 * <code>DrawEdgePeer</code> instance.
 * The size and position of the object are possibly scaled and translated from
 * the values originally supplied through the element attributes as
 * a consequence of the characteristics of the <code>DrawPane</code>.
 * Certainly the y-axis is flipped to account for the origin at the upper-left
 * as used by the AWT versus the origin to the lower-left as used by the graph.
 *
 * @version 1.1, 30 Sep 1999; Copyright 1996 - 1999 by AT&T Corp.
 * @author  <a href="mailto:john@research.att.com">John Mocenigo</a>, <a href="http://www.research.att.com">Research @ AT&T Labs</a>
 */
public class DrawEdgePeer extends DrawObjectPeer
{
  protected Polygon polyline = null;
  protected Polygon startArrow = null;
  protected Polygon endArrow = null;
  protected Rectangle bbox = new Rectangle();
  protected int lineSize = 0;

  /**
   * Creates a <code>DrawEdgePeer</code> related to the supplied <code>DrawEdge</code>
   * and <code>DrawPane</code>.
   *
   * @param drawEdge the source object for this peer object
   * @param pane the pane to use when drawing this peer
   */
  public DrawEdgePeer(DrawEdge drawEdge, DrawPane pane) {
    super((DrawObject)drawEdge,pane);
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
    if(polyline == null) {
      setupPeer(true);
      //throw new IllegalStateException("cannot ask for bounds before Peer is built");
    }
    int[] fuzz = { 0, 1, -1 };
    int xp = 0;
    int yp = 0;
    for(int i = 0; i < fuzz.length; i++) {
      for(int j = 0; j < fuzz.length; j++) {
	xp = x + fuzz[i];
	yp = y + fuzz[j];
	if(DrawObjectPeer.polygonContainsPoint(polyline,xp,yp)) {
	  return true;
	}
	if(startArrow != null && DrawObjectPeer.polygonContainsPoint(startArrow,xp,yp)) {
	  return true;
	}
	if(endArrow != null && DrawObjectPeer.polygonContainsPoint(endArrow,xp,yp)) {
	  return true;
	}
	if(getTextLabelPeer() != null) {
	  return getTextLabelPeer().inPeer(xp,yp);
	}
      }
    }
    return false;
  }

  /**
   * Gets the bounds of this peer in terms of the <code>DrawPane</code> coordinates.
   *
   * @return the bounding box of this object 
   */
  public Rectangle getBounds() {
    if(polyline == null) {
      setupPeer(true);
      //throw new IllegalStateException("cannot ask for bounds before Peer is built");
    }
    return bbox;
  }

  /**
   * Setup this peer.
   * The setup does the actual co-ordinate mappings from what is available
   * in the <code>DrawEdge</code> object to the <code>DrawPane</code> co-ordinates.
   *
   * @param setupTextToo indicates if text peer  should be setup as well
   */
  public void setupPeer(boolean setupTextToo) {
    if(getDrawEdge() == null) return;
    if(setupTextToo && getTextLabelPeer() != null) {
	getTextLabelPeer().setupPeer();
    }
    polyline = getDrawPane().scalePoly(getDrawEdge().getEdgePolygon());
    lineSize = (int)Math.round(polyline.npoints/2);
    bbox.setBounds(polyline.getBounds());
    startArrow = null;
    endArrow = null;
    if(getDrawEdge().getStartArrow() != null) {
      startArrow = getDrawPane().scalePoly(getDrawEdge().getStartArrow());
      bbox.add(startArrow.getBounds());
    }
    if(getDrawEdge().getEndArrow() != null) {
      endArrow = getDrawPane().scalePoly(getDrawEdge().getEndArrow());
      bbox.add(endArrow.getBounds());
    }
    if(getTextLabelPeer() != null && getTextLabelPeer().getBounds() != null) {
      bbox.add(getTextLabelPeer().getBounds());
    }
  }

  /**
   * Get the <code>DrawEdge</code> object associated with this peer
   *
   * @return the associated <code>DrawEdge</code> object
   */
  public DrawEdge getDrawEdge() {
    return (DrawEdge)getDrawObject();
  }

  /**
   * Draws the edge using the specified graphics information.
   *
   * @param gr the AWT graphic context to use for drawing
   * @param context the Grappa context to use for drawing
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

    gr.setColor(getDrawObject().getColorGC().getForeground());

    if(getDrawEdge().getLineWidth() == 1) {
      if(getDrawEdge().getLineStyle() == Grappa.LINE_SOLID) {
	gr.drawPolyline(polyline.xpoints,polyline.ypoints,lineSize);
      } else if(getDrawEdge().getLineStyle() == Grappa.LINE_DOTTED) {
	for(int i = 0; i < lineSize; i++) {
	  gr.drawLine(polyline.xpoints[i],polyline.ypoints[i],polyline.xpoints[i],polyline.ypoints[i]);
	}
      } else if(getDrawEdge().getLineStyle() == Grappa.LINE_DASHED) {
	for(int i = 0; i < (lineSize-1); i+=2) {
	  gr.drawLine(polyline.xpoints[i],polyline.ypoints[i],polyline.xpoints[i+1],polyline.ypoints[i+1]);
	}
      } else {
	// solid
	gr.drawPolyline(polyline.xpoints,polyline.ypoints,lineSize);
      }
    } else {
      if(getDrawEdge().getLineStyle() == Grappa.LINE_SOLID) {
	gr.fillPolygon(polyline);
      } else if(getDrawEdge().getLineStyle() == Grappa.LINE_DOTTED) {
	int lineWidth = getDrawEdge().getLineWidth();
	for(int i = 0; i < lineSize; i++) {
	  gr.fillOval(polyline.xpoints[i],polyline.ypoints[i],lineWidth+1,lineWidth+1);
	}
      } else if(getDrawEdge().getLineStyle() == Grappa.LINE_DASHED) {
	int[] xpts = new int[4];
	int[] ypts = new int[4];
	for(int i = 0; i < (lineSize-1); i+=2) {
	  xpts[0] = polyline.xpoints[i];
	  ypts[0] = polyline.ypoints[i];
	  xpts[1] = polyline.xpoints[i+1];
	  ypts[1] = polyline.ypoints[i+1];
	  xpts[2] = polyline.xpoints[polyline.npoints-2-i];
	  ypts[2] = polyline.ypoints[polyline.npoints-2-i];
	  xpts[3] = polyline.xpoints[polyline.npoints-1-i];
	  ypts[3] = polyline.ypoints[polyline.npoints-1-i];
	  gr.fillPolygon(xpts,ypts,4);
	}
      } else {
	// solid
	gr.fillPolygon(polyline);
      }
    }
    if(startArrow != null) {
      gr.fillPolygon(startArrow);
    }
    if(endArrow != null) {
      gr.fillPolygon(endArrow);
    }

    if(getTextLabelPeer() != null) {
      getTextLabelPeer().draw(gr,context);
    }
  }
}

