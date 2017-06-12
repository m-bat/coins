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
import java.util.Observable;
import java.util.Observer;

/**
 * This class provides the basis for actually drawing graph elements on a pane.
 * Extensions of this class and its subclasses allow for the specific drawing
 * requirements of a particular graph element.  For each <code>DrawObject</code>
 * instance and target <code>DrawPane</code> instance, there is one
 * <code>DrawObjectPeer</code> instance.
 * The size and position of the object are possibly scaled and translated from
 * the values originally supplied through the element attributes as
 * a consequence of the characteristics of the <code>DrawPane</code>.
 * Certainly the y-axis is flipped to account for the origin at the upper-left
 * as used by the AWT versus the origin to the lower-left as used by the graph.
 *
 * @version 1.1, 30 Sep 1999; Copyright 1996 - 1999 by AT&T Corp.
 * @author  <a href="mailto:john@research.att.com">John Mocenigo</a>, <a href="http://www.research.att.com">Research @ AT&T Labs</a>
 */
public abstract class DrawObjectPeer implements Observer
{
  private DrawObject drwObj = null;
  private DrawPane pane = null;

  private DrawObjectPeer next = null;

  private TextLabelPeer textLabelPeer = null;

  Rectangle bounds = null;
  
  /**
   * Creates a <code>DrawObjectPeer</code> related to the supplied <code>DrawObject</code>
   * and <code>DrawPane</code>.
   *
   * @param drawObject the source object for this peer object
   * @param pane the pane to use when drawing this peer
   */
  public DrawObjectPeer(DrawObject drawObject, DrawPane pane) {
    super();
    this.drwObj = drawObject;
    this.pane = pane;
    pane.addObserver(this);
    DrawObjectPeer prev = drwObj.setPeer(this);
    if(prev == null) {
      next = this;
    } else {
      next = prev.setNext(this);
    }
    if(drwObj.getTextLabel() != null) {
      new TextLabelPeer(this,drwObj.getTextLabel());
    }
  }

  /**
   * Gets the bounds of this peer in terms of the <code>DrawPane</code> coordinates.
   */
  public abstract Rectangle getBounds();

  /**
   * Checks if the supplied co-ordinates are in the specific shape of
   * this peer.
   *
   * @param x the x co-ordinate to check
   * @param y the y co-ordinate to check
   * @return true if the co-ordinates lie inside the peer or on its boundary,
   *         false otherwise.
   */
  protected abstract boolean inPeer(int x, int y);

  /**
   * Checks if the supplied point is in the peer.
   * Initially, containment by the bounding box is checked and then,
   * if contained by the bounding box, <code>inPeer</code> is called.
   *
   * @param pt the point to be checked
   * @return true if the peer contains the point, false otherwise
   * @see #inPeer(int,int)
   */
  public boolean contains(Point pt) {
    return contains(pt.x,pt.y);
  }

  /**
   * Checks if the supplied co-ordinates are in the peer.
   * Initially, containment by the bounding box is checked and the,
   * if contained by the bounding box, <code>inPeer</code> is called.
   *
   * @param x the x co-ordinate to be checked
   * @param y the y co-ordinate to be checked
   * @return true if the peer contains the point, false otherwise
   * @see #inPeer(int,int)
   */
  public boolean contains(int x, int y) {
    if(!rectangleContainsPoint(getBounds(),x,y)) return false;
    return inPeer(x,y);
  }

  /**
   * Checks if this peer uses the supplied pane for drawing.
   *
   * @param candidate the <code>DrawPane</code> to check
   * @return true, if the supplied pane is used for drawing, false otherwise.
   */
  public boolean isDrawnOn(DrawPane candidate) {
    return(candidate == pane);
  }

  /**
   * Get the next peer object sharing the same <code>DrawObject</code> source,
   * but using a different <code>DrawPane</code>.  Peers are stored as a linked
   * chain.
   * @return the next peer in the linked chain of peers of the source <code>DrawObject</code>
   * @see #getDrawObject()
   * @see DrawObject#getPeer()
   * @see #getDrawPane()
   */
  public DrawObjectPeer getNext() {
    return next;
  }

  /**
   * Set the next peer object sharing the same <code>DrawObject</code> source,
   * but using a different <code>DrawPane</code>.  Peers are stored as a linked
   * list with the initial link being available from the <code>DrawObject</code>.
   * If the supplied peer is already in the linked list, it is not added and it
   * is set as the return value.
   *
   * @return the value of the peer previously stored in the next slot (which may be null) or the supplied peer if that peer is already somewhere in the linked list
   * @see #getDrawObject()
   * @see DrawObject#getPeer()
   * @see #getDrawPane()
   */
  public DrawObjectPeer setNext(DrawObjectPeer next) {
    DrawObjectPeer oldNext = this.next;
    /*
    DrawObjectPeer checker = getDrawObject().getPeer();
    if(checker == next) return next;
    checker = checker.getNext();
    while(checker != next && checker != getDrawObject().getPeer()) {
      checker = checker.getNext();
    }
    if(checker == getDrawObject().getPeer()) {
      this.next = next;
    } else {
      oldNext = next;
    }
    */
    this.next = next;
    return oldNext;
  }

  /**
   * Setup this peer.
   *
   * @param setupTextToo indicates if text peer  should be setup as well
   */
  public abstract void setupPeer(boolean setupTextToo);

  /**
   * Draw this peer.
   *
   * @param gr the AWT graphic context to use for drawing
   * @param context the Grappa context to use for drawing
   */
  public abstract void draw(Graphics gr, GraphicContext context);

  /**
   * Remove this peer from the linked chain of associated peers.
   */
  public final void remove() {
    DrawObjectPeer prev = this;
    while(prev.getNext() != this) prev = prev.getNext();
    if(prev == this) {
      prev = null;
    } else {
      prev.setNext(next);
    }
    drwObj.setPeer(prev);
  }

  /**
   * Get the draw object that is the source of this peer
   *
   * @return the associated draw object
   */
  public DrawObject getDrawObject() {
    return drwObj;
  }

  /**
   * Get the drawing pane used by this peer
   *
   * @return the associated drawing pane
   */
  public DrawPane getDrawPane() {
    return pane;
  }

  /**
   * Set the text label peer that is to be drawn in conjunction with this peer.
   *
   * @return the previously associated text label peer
   */
  public TextLabelPeer setTextLabelPeer(TextLabelPeer peer) {
    // to handle odd case of Table objects
    if(getDrawObject() instanceof Table) return textLabelPeer;
    TextLabelPeer oldTextLabelPeer = textLabelPeer;
    textLabelPeer = peer;
    return oldTextLabelPeer;
  }

  /**
   * Get the text label peer that is drawn in conjunction with this peer.
   *
   * @return the associated text label peer
   */
  public TextLabelPeer getTextLabelPeer() {
    return textLabelPeer;
  }

  /**
   * Reset this object and release its resources for garbage collection.
   */
  public void free() {
    pane.deleteObserver(this);
    pane = null;
    drwObj = null;
    next = null;
    textLabelPeer = null;
  }

  public static boolean rectangleContainsPoint(Rectangle rect, Point pt) {
    return rectangleContainsPoint(rect,pt.x,pt.y);
  }

  public static boolean rectangleContainsPoint(Rectangle rect, int x, int y) {
    return (x >= rect.x) && ((x - rect.x) <= rect.width) && (y >= rect.y) && ((y-rect.y) <= rect.height);
  }

  public static boolean polygonContainsPoint(Polygon poly, Point pt) {
    return polygonContainsPoint(poly,pt.x,pt.y);
  }

  public static boolean polygonContainsPoint(Polygon poly, int x, int y) {
    if (rectangleContainsPoint(poly.getBounds(),x, y)) {
      return polypartsContainsPoint(poly.npoints, poly.xpoints, poly.ypoints, x, y, true);
    }
    return false;
  }
  
  private static boolean polypartsContainsPoint(int npoints, int[] xpoints, int[] ypoints, int x, int y, boolean retry) {
      int hits = 0;
      int ySave = 0;

      // Find a vertex that's not on the halfline
      int i = 0;
      while (i < npoints && ypoints[i] == y) {
	i++;
      }
      if(i == npoints) {
	if(retry) {
	  return polypartsContainsPoint(npoints,ypoints,xpoints,y,x,false);
	} else {
	  return false;
	}
      }

      // Walk the edges of the polygon
      for (int n = 0; n < npoints; n++) {
	int j = (i + 1) % npoints;

	int dx = xpoints[j] - xpoints[i];
	int dy = ypoints[j] - ypoints[i];

	int rx = x - xpoints[i];
	int ry = y - ypoints[i];

	// check if they are on the same line
	if(Math.abs(ry) <= Math.abs(dy) && ((dx == 0 && rx == 0) || (rx*dy == dx*ry))) {
	  return true;
	}

	// Ignore horizontal edges completely
	if (dy != 0) {
	  // Check to see if the edge intersects
	  // the horizontal halfline through (x, y)

	  // Deal with edges starting or ending on the halfline
	  if (ypoints[j] == y && xpoints[j] >= x) {
	    ySave = ypoints[i];
	  }
	  if (ypoints[i] == y && xpoints[i] >= x) {
	    if ((ySave > y) != (ypoints[j] > y)) {
	      hits--;
	    }
	  }

	  // Tally intersections with halfline
	  float s = (float)ry / (float)dy;
	  if (s >= 0.0 && s <= 1.0 && (s * dx) >= rx) {
	    hits++;
	  }
	}
	i = j;
      }

      // Inside if number of intersections odd
      return (hits % 2) != 0;
  }

  /**
   * This method is called whenever an observed LinearMap is changed.
   * It is required by the <code>Observer</code> interface.
   *
   * @param obs the observable object that has been updated
   * @param arg when not null, it indicates that <code>obs</code> need no longer be
   *            observed and in its place <code>arg</code> should be observed.
   */
  public void update(Observable obs, Object arg) {
    // begin boilerplate
    if(!(obs instanceof LinearMap)) {
      throw new IllegalArgumentException("expected to be observing mappings only (obs)");
    }
    LinearMap map = (LinearMap)obs;
    DrawPane mapPane = null;
    if(arg != null) {
      if(!(arg instanceof DrawPane)) {
	throw new IllegalArgumentException("expected argument to be DrawPane or null (arg)");
      }
      mapPane = (DrawPane)arg;
      if(mapPane != getDrawPane()) {
	throw new IllegalArgumentException("DrawPane update argument is not for this peer");
      }
    }
    // end boilerplate
    setupPeer(true);
    // will force setupPeer when needed
    //getDrawPane().getCanvas().resetOSImage();
  }
}
