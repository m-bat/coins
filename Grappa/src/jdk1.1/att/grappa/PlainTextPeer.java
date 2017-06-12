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

/**
 * This class provides the basis for actually drawing plain text elements on a pane.
 *
 * @version 1.1, 30 Sep 1999; Copyright 1996 - 1999 by AT&T Corp.
 * @author  <a href="mailto:john@research.att.com">John Mocenigo</a>, <a href="http://www.research.att.com">Research @ AT&T Labs</a>
 */
public class PlainTextPeer extends DrawNodePeer
{
  /**
   * Create an instance of a <code>PlainTextPeer</code>
   * related to the supplied <code>PlainText</code>
   * and <code>DrawPane</code>.
   *
   * @param plainText the source object for this peer object
   * @param pane the pane to use when drawing this peer
   */
  public PlainTextPeer(PlainText plainText, DrawPane pane) {
    super((DrawNode)plainText,pane);
  }

  /**
   * Setup this peer.
   * The setup does the actual co-ordinate mappings from what is available
   * in the <code>DrawNode</code> object to the <code>DrawPane</code> co-ordinates.
   *
   * @param setupTextToo indicates if text peer  should be setup as well
   */
  public void setupPeer(boolean setupTextToo) {
    center = getDrawPane().scalePoint(getDrawNode().getPosition());
    Rectangle[] rect = new Rectangle[1];
    if(setupTextToo && getTextLabelPeer() != null) {
	getTextLabelPeer().setupPeer();
    }
    if(getTextLabelPeer() == null) {
      rect[0] = new Rectangle(center.x,center.y,0,0);
    } else {
      rect[0] = getTextLabelPeer().getBounds();
    }
    outlines = (Object[])rect;
  }
}
