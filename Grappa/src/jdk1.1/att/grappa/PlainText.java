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

/**
 * The DrawObject to use for rendering nodes whose shape is a plaintext.
 * The drawn shape consists of text only (if a label is present).
 *
 * @version 1.1, 30 Sep 1999; Copyright 1996 - 1999 by AT&T Corp.
 * @author  <a href="mailto:john@research.att.com">John Mocenigo</a>, <a href="http://www.research.att.com">Research @ AT&T Labs</a>
 */
public class PlainText extends DrawNode
{
  /**
   * Construct a <code>DrawObject</code> consisting only of text for nodes.
   */
  public PlainText() {
    //super();
    peripheries = 0;
    shape = DrawNode.TEXT;
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
    new PlainTextPeer(this, pane);
    return;
  }
}
