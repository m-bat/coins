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
 * This class provides the basis for actually drawing subgraph elements on a pane.
 * For each <code>DrawSubgraph</code>
 * instance and target <code>DrawPane</code> instance, there is one
 * <code>DrawSubgraphPeer</code> instance.
 * The size and position of the object are possibly scaled and translated from
 * the values originally supplied through the element attributes as
 * a consequence of the characteristics of the <code>DrawPane</code>.
 * Certainly the y-axis is flipped to account for the origin at the upper-left
 * as used by the AWT versus the origin to the lower-left as used by the graph.
 *
 * @version 1.1, 30 Sep 1999; Copyright 1996 - 1999 by AT&T Corp.
 * @author  <a href="mailto:john@research.att.com">John Mocenigo</a>, <a href="http://www.research.att.com">Research @ AT&T Labs</a>
 */
public class DrawSubgraphPeer extends DrawObjectPeer
{
  private Rectangle BBox = null;
  
  /**
   * Create an instance of a <code>DrawSubgraphPeer</code>
   * related to the supplied <code>DrawSubgraph</code>
   * and <code>DrawPane</code>.
   *
   * @param drawSubgraph the source object for this peer object
   * @param pane the pane to use when drawing this peer
   */
  public DrawSubgraphPeer(DrawSubgraph drawSubgraph, DrawPane pane) {
    super((DrawObject)drawSubgraph,pane);
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
    return getBounds().contains(x,y);
  }

  /**
   * Gets the bounds of this peer in terms of the <code>DrawPane</code> coordinates.
   *
   * @return the bounding box of the peer
   */
  public Rectangle getBounds() {
    if(BBox == null) {
      setupPeer(true);
      //throw new IllegalStateException("cannot ask for bounds before Peer is built");
    }
    return BBox;
  }

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
    getDrawPane().setupPane();
    BBox = getDrawPane().scaleRect(getDrawObject().getBounds());
  }

  /**
   * Draws the subgraph using the specified graphics information.
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

    if(gr == null) return;

    if(getDrawObject().getColorGC().getXORMode()) {
      gr.setXORMode(getDrawObject().getColorGC().getXORColor());
    } else {
      gr.setPaintMode();
    }

    if(
       ((Subgraph)(getDrawObject().getElement())).isCluster()
       &&
       !((Subgraph)(getDrawObject().getElement())).isRoot()
       ) {
      gr.setColor(getDrawObject().getColorGC().getBackground());
      gr.fillRect(BBox.x,BBox.y,BBox.width,BBox.height);
      gr.setColor(getDrawObject().getColorGC().getForeground());
      gr.drawRect(BBox.x,BBox.y,BBox.width,BBox.height);
    }
    if(getTextLabelPeer() != null) {
      getTextLabelPeer().draw(gr, context);
    }
  }
}
