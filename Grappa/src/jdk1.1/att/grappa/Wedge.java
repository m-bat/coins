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
 * The DrawObject to use for rendering nodes whose shape is a wedge.
 *
 * @version 1.1, 30 Sep 1999; Copyright 1996 - 1999 by AT&T Corp.
 * @author  <a href="mailto:john@research.att.com">John Mocenigo</a>, <a href="http://www.research.att.com">Research @ AT&T Labs</a>
 */
public class Wedge extends DrawNode
{
  /**
   * The beginning angle of the wedge.
   */
  public final static int START_ANGLE    = 90;
  /**
   * The angle over which the wedge extends.
   */
  public final static int ARC_ANGLE      = 135;

  private int startAngle = START_ANGLE;
  private int arcAngle = ARC_ANGLE;
  
  /**
   * This constructor creates an uninitialized Wedge object.
   * Upon creation, a default
   * set of attributes for observing are specified (in addition to those
   * specified when its <code>super()</code> constructor is called.
   *
   * @see java.util.Observer
   */
  public Wedge() {
    //super();

    attrOfInterest("start");
    attrOfInterest("arc");
  }

  /**
   * Hash code for start angle attribute (start)
   */
  public final static int START_HASH = "start".hashCode();
  /**
   * Hash code for arc angle attribute (arc)
   */
  public final static int ARC_HASH   = "arc".hashCode();

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

    if(attr.getNameHash() == Wedge.START_HASH) {
      int oldAngle = startAngle;
      startAngle = Integer.valueOf(attr.getValue()).intValue();
      if(oldAngle != startAngle) {
	setRedrawFlag(true);
	setBoundsFlag(true);
      }
      return;
    } else if(attr.getNameHash() == Wedge.ARC_HASH) {
      int oldAngle = arcAngle;
      arcAngle = Integer.valueOf(attr.getValue()).intValue();
      if(oldAngle != arcAngle) {
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
    new WedgePeer(this, pane);
    return;
  }

  /**
   * Get the start angle (in degrees).
   *
   * @return the start angle.
   */
  public int getStartAngle() {
    return startAngle;
  }

  /**
   * Get the arc angle (in degrees).
   *
   * @return the arc angle.
   */
  public int getArcAngle() {
    return arcAngle;
  }
}
