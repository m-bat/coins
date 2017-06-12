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
 * The base class for subgraph drawing.
 *
 * @version 1.1, 30 Sep 1999; Copyright 1996 - 1999 by AT&T Corp.
 * @author  <a href="mailto:john@research.att.com">John Mocenigo</a>, <a href="http://www.research.att.com">Research @ AT&T Labs</a>
 */
public class DrawSubgraph extends DrawObject
{
  private boolean orientLR = false;
  
  Rectangle bbox = null;

  /**
   * Default shape to use when representing a subgraph.
   */
  public final static String defaultGraphShape = "box";

  /**
   * This constructor creates an uninitialized DrawSubgraph object.
   * Upon creation, a default
   * set of attributes for observing are specified (in addition to those
   * specified when its <code>super()</code> constructor is called.
   *
   * @see java.util.Observer
   */
  public DrawSubgraph() {
    //super();

    attrOfInterest("bb");
    attrOfInterest("lp");
    attrOfInterest("rankdir");
  }

  /**
   * Check if the layout of this subgraph is left-to-right in nature.
   *
   * @return true if the layout is left-to-right, false if it is top-to-bottom
   */
  public boolean isLR() {
    return orientLR;
  }

  /**
   * Set the layout orientation indicator.
   * Changing this value does not actually change the layout orientation.
   * This value is only set to reflect the layout orientation as indicated
   * by the element <i>rankdir</i> attribute, which is used by the layout
   * engine to actually accomplish the manner of the layout.
   *
   * @param newLR pass true to indicate that the layout is left-to-right or
   *              pass false to indicate a top-to-bottom layout orientation
   */
  protected void setLR(boolean newLR) {
    orientLR = newLR;
  }

  /**
   * This method to be called when the bounding box needs to be calculated.
   *
   * @return the bounding box of this object in graph co-ordinates
   */
  public Rectangle setBounds() {
    Rectangle oldBox = BoundingBox;
    if(!getBoundsFlag()) return oldBox;

    BoundingBox = null;

    if(bbox != null) {
      BoundingBox = new Rectangle(bbox);
      if(((Subgraph)getElement()).isCluster()) {
	if(getTextLabel() != null) {
	  Rectangle lbox = getTextLabel().getBounds();
	  Point lpos = new Point(bbox.x + (int)Math.round((double)lbox.width/2.0),bbox.y + bbox.height + (int)Math.round((double)lbox.height/2.0));
	  getTextLabel().setPosition(lpos);
	}
	return oldBox;
      }
    }

    Enumeration enum = ((Subgraph)getElement()).nodeElements();

    while(enum.hasMoreElements()) {
      if(BoundingBox != null) {
	BoundingBox.add(((Element)enum.nextElement()).getDrawObject().getBounds());
      } else {
	BoundingBox = new Rectangle(((Node)enum.nextElement()).getDrawObject().getBounds());
      }
    }
    if(getTextLabel() != null) {
      if(BoundingBox != null) {
	BoundingBox.add(getTextLabel().getBounds());
      } else {
	BoundingBox = new Rectangle(getTextLabel().getBounds());
      }
    }
    needSetup();
    setBoundsFlag(false);
    return oldBox;
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

    String key = attr.getName();
    if(attr.getNameHash() == DrawObject.BBOX_HASH) {
      if(emptyMeansRemove(attr)) return;
      Rectangle newBbox = rectForTuple(attr.getValue());
 
      if(bbox == null || !newBbox.equals(bbox)) {
	setRedrawFlag(true);
	setBoundsFlag(true);
	bbox = newBbox;
      }
      return;
    } else if(attr.getNameHash() == DrawObject.RANKDIR_HASH) {
      if(emptyMeansRemove(attr)) return;
      // add "doLayoutFlag" for when this changes
      if(attr.getValue().equals("LR")) {
	setLR(true);
      } else {
	setLR(false);
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
    new DrawSubgraphPeer(this, pane);
    return;
  }
}
